package org.radargps.localapplication.scanner.device;

import com.github.benmanes.caffeine.cache.Cache;
import org.radargps.localapplication.captured.data.DataService;
import org.radargps.localapplication.common.util.TimeUtil;
import org.radargps.localapplication.scanner.connection.ScannerConnectionInternalService;
import org.radargps.localapplication.scanner.device.domain.Scanner;
import org.radargps.localapplication.common.pageable.Page;
import org.radargps.localapplication.captured.data.domain.Data;
import org.radargps.localapplication.scanner.device.domain.ScannerReadEntityType;
import org.radargps.localapplication.scanner.device.domain.ScannerRole;
import org.radargps.localapplication.scanner.device.domain.ScannerType;
import org.radargps.localapplication.scanner.device.event.*;
import org.radargps.localapplication.scanner.device.message.publisher.PalletScannerEventPublisher;
import org.radargps.localapplication.scanner.device.message.publisher.ProductScannerEventPublisher;
import org.radargps.localapplication.scanner.device.message.publisher.ProductPalletEventPublisher;
import org.radargps.localapplication.scanner.device.message.publisher.ProductProductEventPublisher;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
public class ScannerInternalService {
    private final ScannerRepository scannerRepository;
    private final ScannerConnectionInternalService scannerConnectionInternalService;
//    private final DataService dataService;
//    private final OutboxService outboxService;

    private final PalletScannerEventPublisher palletScannerEventPublisher;
    private final ProductScannerEventPublisher productScannerEventPublisher;
    private final ProductPalletEventPublisher productPalletEventPublisher;
    private final ProductProductEventPublisher productProductEventPublisher;
    private final DataService dataService;
    private final Cache<UUID, Data> deviceLastDataCache;

    public ScannerInternalService(ScannerRepository scannerRepository,
                                  @Lazy ScannerConnectionInternalService scannerConnectionInternalService,
                                  PalletScannerEventPublisher palletScannerEventPublisher,
                                  ProductScannerEventPublisher productScannerEventPublisher,
                                  ProductPalletEventPublisher productPalletEventPublisher,
                                  ProductProductEventPublisher productProductEventPublisher,
                                  DataService dataService,
                                  Cache deviceLastDataCache) {
        this.scannerRepository = scannerRepository;
        this.scannerConnectionInternalService = scannerConnectionInternalService;
        this.palletScannerEventPublisher = palletScannerEventPublisher;
        this.productScannerEventPublisher = productScannerEventPublisher;
        this.productPalletEventPublisher = productPalletEventPublisher;
        this.productProductEventPublisher = productProductEventPublisher;
        this.dataService = dataService;
        this.deviceLastDataCache = deviceLastDataCache;
    }

    @Transactional
    public Scanner create(Scanner scanner) {
        if (scanner.getType() == null) {
            scanner.setType(ScannerType.QR_SCANNER);
        }
        if (scanner.getRole() == null) {
            scanner.setRole(ScannerRole.PRODUCT_SCANNER);
        }
        if (scanner.getReadEntityType() == null) {
            scanner.setReadEntityType(ScannerReadEntityType.PRODUCT);
        }
        return scannerRepository.save(scanner);
    }


    @CacheEvict(value = "scanner", key = "#scanner.uniqueId")
    @Transactional
    public Scanner updateDevice(Scanner scanner) {
        return scannerRepository.save(scanner);
    }

    @Cacheable(value = "scanner", key = "#uniqueId")
    @Transactional
    public Optional<Scanner> findOne(String uniqueId) {
        return scannerRepository.findById(uniqueId);
    }

    @CachePut(value = "scanner", key = "#uniqueId")
    @Transactional
    public void updateLatestDeviceData(String uniqueId, Data data) {
        scannerRepository.setLastDataIdAndLastDataTimeByUniqueId(uniqueId, data.getId(), data.getServerTime());
    }

//    @Cacheable(value = "scanner-data", key = "#uniqueId")
    @Transactional
    public Optional<Data> findLatestScannerData(String uniqueId) {
        var scanner = findByUniqueId(uniqueId);
        if (scanner.isPresent() && scanner.get().getLastDataId() != null) {
            return dataService.findById(scanner.get().getLastDataId());
        }
       return Optional.empty();
    }

    @Cacheable(value = "scanner", key = "#uniqueId")
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
        var scanner = findOne(data.getUniqueId());
        if (scanner.isPresent()) {
            switch (scanner.get().getRole()) {
                case PRODUCT_SCANNER -> productScanned(scanner.get(), data);
                case PALLET_SCANNER -> palletScanned(scanner.get(), data);
                case PRODUCT_PALLET_ASSIGNER -> productPalletAssigned(scanner.get(), data);
                case PRODUCT_PRODUCT_ASSIGNER -> productProductAssigned(scanner.get(), data);
                case PALLET_UN_ASSIGNER -> palletUnAssigned(scanner.get(), data);
                case PRODUCT_UN_ASSIGNER -> productUnAssigned(scanner.get(), data);
            }
        }
    }

    private void productScanned(Scanner device, Data data) {
        var event = new ProductScanned(device.getUniqueId(), data.getData());
        productScannerEventPublisher.publish(event);
    }
    private void palletScanned(Scanner device, Data data) {
        var event = new PalletScannned(device.getUniqueId(), data.getData());
        palletScannerEventPublisher.publish(event);
    }
    private void productPalletAssigned(Scanner scanner, Data data) {
        var connection = scannerConnectionInternalService.findByScannerId(scanner.getUniqueId());
        if (connection.isPresent()) {
                String palletId = null;
                String productId = null;

                if (scanner.getUniqueId().equals(connection.get().getFirstScanner().getUniqueId())) {
                    if (connection.get().getFirstScanner().getReadEntityType().equals(ScannerReadEntityType.PRODUCT)) {
                        productId = data.getData();
                        var secondScannerData = findLatestScannerData(connection.get().getSecondScanner().getUniqueId());
                        if (secondScannerData.isPresent()) {
                            palletId = secondScannerData.get().getData();
                        }
                    } else {
                        palletId = data.getData();
                        var secondScannerData = findLatestScannerData(connection.get().getSecondScanner().getUniqueId());
                        if (secondScannerData.isPresent()) {
                            productId = secondScannerData.get().getData();
                        }
                    }
                } else {
                    if (scanner.getUniqueId().equals(connection.get().getSecondScanner().getUniqueId())) {
                        if (connection.get().getSecondScanner().getReadEntityType().equals(ScannerReadEntityType.PRODUCT)) {
                            productId = data.getData();
                            var firstScannerData = findLatestScannerData(connection.get().getFirstScanner().getUniqueId());
                            if (firstScannerData.isPresent()) {
                                palletId = firstScannerData.get().getData();
                            }
                        } else {
                            palletId = data.getData();
                            var firstScannerData = findLatestScannerData(connection.get().getFirstScanner().getUniqueId());
                            if (firstScannerData.isPresent()) {
                                productId = firstScannerData.get().getData();
                            }
                        }
                    }
                }

                if (palletId != null && productId != null) {
                    var event = new ProductPalletAssigned(palletId, productId);
                    productPalletEventPublisher.publish(event);
                }
        }
    }
    private void productUnAssigned(Scanner device, Data data) {
        var event = new ProductUnAssigned(device.getUniqueId(), data.getData());
        productScannerEventPublisher.publish(event);
    }
    private void palletUnAssigned(Scanner device, Data data) {
        var event = new ProductUnAssigned(device.getUniqueId(), data.getData());
        palletScannerEventPublisher.publish(event);
    }
    private void productProductAssigned(Scanner scanner, Data data) {
        if (scanner.getReadEntityType() == null) {
            return;
        }
        var connection = scannerConnectionInternalService.findByScannerId(scanner.getUniqueId());
        if (connection.isPresent()) {
            String link = null;
            String productId = null;

            if (scanner.getUniqueId().equals(connection.get().getFirstScanner().getUniqueId())) {
                if (connection.get().getFirstScanner().getReadEntityType().equals(ScannerReadEntityType.PRODUCT)) {
                    productId = data.getData();
                    var secondScannerData = findLatestScannerData(connection.get().getSecondScanner().getUniqueId());
                    if (secondScannerData.isPresent()
                            && TimeUtil.isTimestampDifferenceLessThan(data.getServerTime(), secondScannerData.get().getServerTime(), 5)) {
                        link = secondScannerData.get().getData();
                    }
                } else {
                    link = data.getData();
                    var secondScannerData = findLatestScannerData(connection.get().getSecondScanner().getUniqueId());
                    if (secondScannerData.isPresent()
                            && TimeUtil.isTimestampDifferenceLessThan(data.getServerTime(), secondScannerData.get().getServerTime(), 5)) {
                        productId = secondScannerData.get().getData();
                    }
                }
            } else {
                if (scanner.getUniqueId().equals(connection.get().getSecondScanner().getUniqueId())) {
                    if (connection.get().getSecondScanner().getReadEntityType().equals(ScannerReadEntityType.PRODUCT)) {
                        productId = data.getData();
                        var firstScannerData = findLatestScannerData(connection.get().getFirstScanner().getUniqueId());
                        if (firstScannerData.isPresent()
                                && TimeUtil.isTimestampDifferenceLessThan(data.getServerTime(), firstScannerData.get().getServerTime(), 500)) {
                            link = firstScannerData.get().getData();
                        }
                    } else {
                        link = data.getData();
                        var firstScannerData = findLatestScannerData(connection.get().getFirstScanner().getUniqueId());
                        if (firstScannerData.isPresent()
                                && TimeUtil.isTimestampDifferenceLessThan(data.getServerTime(), firstScannerData.get().getServerTime(), 500)) {
                            productId = firstScannerData.get().getData();
                        }
                    }
                }
            }

            if (link != null && productId != null) {
                var event = new ProductProductAssigned(link, productId);
                productProductEventPublisher.publish(event);
            }
        }
    }
}
