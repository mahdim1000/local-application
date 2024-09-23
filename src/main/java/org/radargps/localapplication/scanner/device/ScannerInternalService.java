package org.radargps.localapplication.scanner.device;

import com.github.benmanes.caffeine.cache.Cache;
import org.radargps.localapplication.scanner.device.domain.Scanner;
import org.radargps.localapplication.common.pageable.Page;
import org.radargps.localapplication.captured.data.domain.Data;
import org.radargps.localapplication.scanner.device.domain.ScannerReadEntityType;
import org.radargps.localapplication.scanner.device.event.PalletScannned;
import org.radargps.localapplication.scanner.device.event.ProductProductAssigned;
import org.radargps.localapplication.scanner.device.event.ProductScanned;
import org.radargps.localapplication.scanner.device.event.ProductUnAssigned;
import org.radargps.localapplication.scanner.device.message.publisher.PalletEventPublisher;
import org.radargps.localapplication.scanner.device.message.publisher.ProductEventPublisher;
import org.radargps.localapplication.scanner.device.message.publisher.ProductPalletEventPublisher;
import org.radargps.localapplication.scanner.device.message.publisher.ProductProductEventPublisher;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

import static org.radargps.localapplication.common.util.TimeUtil.isTimestampDifferenceLessThan;

@Service
public class ScannerInternalService {
    private final ScannerRepository scannerRepository;
//    private final DataService dataService;
//    private final OutboxService outboxService;

    private final PalletEventPublisher palletEventPublisher;
    private final ProductEventPublisher productEventPublisher;
    private final ProductPalletEventPublisher productPalletEventPublisher;
    private final ProductProductEventPublisher productProductEventPublisher;
    private final Cache<UUID, Data> deviceLastDataCache;

    public ScannerInternalService(ScannerRepository scannerRepository,
                                  PalletEventPublisher palletEventPublisher, ProductEventPublisher productEventPublisher, ProductPalletEventPublisher productPalletEventPublisher, ProductProductEventPublisher productProductEventPublisher, Cache deviceLastDataCache) {
        this.scannerRepository = scannerRepository;
        this.palletEventPublisher = palletEventPublisher;
        this.productEventPublisher = productEventPublisher;
        this.productPalletEventPublisher = productPalletEventPublisher;
        this.productProductEventPublisher = productProductEventPublisher;
        this.deviceLastDataCache = deviceLastDataCache;
    }

    @Transactional
    public Scanner create(Scanner device) {
        return scannerRepository.save(device);
    }


    @CacheEvict(value = "device", key = "#device.id")
    @Transactional
    public Scanner updateDevice(Scanner device) {
        return scannerRepository.save(device);
    }

    @Cacheable(value = "device", key = "#deviceId")
    @Transactional
    public Optional<Scanner> findOne(UUID deviceId) {
        return scannerRepository.findById(deviceId);
    }

    @CachePut(value = "device", key = "#deviceId")
    @Transactional
    public void updateLatestDeviceData(UUID deviceId, Data data) {
        scannerRepository.setLastDataIdAndLastDataTimeByDeviceId(deviceId, data.getId(), data.getServerTime());
    }

    @Cacheable(value = "data", key = "#deviceId")
    @Transactional
    public Optional<Data> findLatestDeviceData(UUID deviceId) {
        return scannerRepository.findLatestDeviceData(deviceId);
    }

    @Cacheable(value = "device", key = "#uniqueId")
    public Optional<Scanner> findByUniqueId(String uniqueId) {
        return scannerRepository.findByUniqueId(uniqueId);
    }

    @Transactional(readOnly = true)
    public Page<Scanner> findByCompanyId(UUID companyId, Pageable pageable) {
        var result = scannerRepository.findByCompanyId(companyId, pageable);
        return new Page<>(result.getContent().stream().toList(),
                result.getTotalElements());
    }

    @Transactional
    public void processAndPublish(Data data) {
        var device = findOne(data.getDeviceId());
        if (device.isPresent()) {
            switch (device.get().getRole()) {
                case PRODUCT_SCANNER -> productScanned(device.get(), data);
                case PALLET_SCANNER -> palletScanned(device.get(), data);
                case PRODUCT_PALLET_ASSIGNER -> productPalletAssigned(device.get(), data);
                case PRODUCT_PRODUCT_ASSIGNER -> productProductAssigned(device.get(), data);
                case PALLET_UN_ASSIGNER -> palletUnAssigned(device.get(), data);
                case PRODUCT_UN_ASSIGNER -> productUnAssigned(device.get(), data);
            }
        }
    }

    private void productScanned(Scanner device, Data data) {
        var event = new ProductScanned(device.getUniqueId(), data.getData());
        productEventPublisher.publish(event);
    }
    private void palletScanned(Scanner device, Data data) {
        var event = new PalletScannned(device.getUniqueId(), data.getData());
        palletEventPublisher.publish(event);
    }
    private void productPalletAssigned(Scanner device, Data data) {
        var connectedDevice = device.getConnectedDevice();
        if (connectedDevice != null) {
            var connectdDeviceData = deviceLastDataCache.getIfPresent(connectedDevice.getId());
            if (connectdDeviceData != null) {
                UUID palletId = null;
                UUID productId = null;

                if (device.getReadEntityType().equals(ScannerReadEntityType.PALLET)) {
                    palletId = device.getId();
                } else if (connectedDevice.getReadEntityType().equals(ScannerReadEntityType.PALLET)) {
                    palletId = connectedDevice.getId();
                }

                if (device.getReadEntityType().equals(ScannerReadEntityType.PRODUCT)) {
                    productId = device.getId();
                } else if (connectedDevice.getReadEntityType().equals(ScannerReadEntityType.PRODUCT)) {
                    productId = connectedDevice.getId();
                }

                if (palletId != null && productId != null) {
                    var event = new ProductPalletAssigned(palletId.toString(), productId.toString());
                    productPalletEventPublisher.publish(event);
                }

            }
        }
    }
    private void productUnAssigned(Scanner device, Data data) {
        var event = new ProductUnAssigned(device.getUniqueId(), data.getData());
        palletEventPublisher.publish(event);
    }
    private void palletUnAssigned(Scanner device, Data data) {
        var event = new ProductUnAssigned(device.getUniqueId(), data.getData());
        palletEventPublisher.publish(event);
    }
    private void productProductAssigned(Scanner device, Data data) {
        var connectedDevice = device.getConnectedDevice();
        if (connectedDevice != null) {
            var connectdDeviceData = deviceLastDataCache.getIfPresent(connectedDevice.getId());
            if (connectdDeviceData != null
                    && device.getReadEntityType().equals(ScannerReadEntityType.PRODUCT)
                    && connectedDevice.getReadEntityType().equals(ScannerReadEntityType.PRODUCT)
                    && isTimestampDifferenceLessThan(data.getServerTime(), connectdDeviceData.getServerTime(), 300)) {
                String link = null;
                UUID productId = null;

                try {
                    productId = UUID.fromString(data.getData());
                    link = connectdDeviceData.getData();
                } catch (Exception e) {
                    link = data.getData();
                    productId = UUID.fromString(connectdDeviceData.getData());
                }

                if (link != null && productId != null) {
                    var event = new ProductProductAssigned(link, productId);
                    productPalletEventPublisher.publish(event);
                }

            }
        }
    }
}
