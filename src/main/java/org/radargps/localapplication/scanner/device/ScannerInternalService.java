package org.radargps.localapplication.scanner.device;

import com.github.benmanes.caffeine.cache.Cache;
import org.radargps.localapplication.captured.data.DataService;
import org.radargps.localapplication.common.errors.exception.EntryAlreadyExistsException;
import org.radargps.localapplication.common.errors.exception.ResourceNotFoundException;
import org.radargps.localapplication.common.util.TimeUtil;
import org.radargps.localapplication.pending.data.ProductPendingPalletInternalService;
import org.radargps.localapplication.pending.data.domain.ProductPendingPallet;
import org.radargps.localapplication.scanner.connection.ScannerConnectionInternalService;
import org.radargps.localapplication.scanner.device.domain.Scanner;
import org.radargps.localapplication.common.pageable.Page;
import org.radargps.localapplication.captured.data.domain.Data;
import org.radargps.localapplication.scanner.device.domain.ScannerReadEntityType;
import org.radargps.localapplication.scanner.device.domain.ScannerRole;
import org.radargps.localapplication.scanner.device.domain.ScannerType;
import org.radargps.localapplication.scanner.device.event.*;
import org.radargps.localapplication.scanner.device.message.publisher.*;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class ScannerInternalService {
    private final ScannerRepository scannerRepository;
    private final ScannerConnectionInternalService scannerConnectionInternalService;
    private final ProductPendingPalletInternalService productPendingPalletInternalService;
//    private final DataService dataService;
//    private final OutboxService outboxService;

    private final PalletScannerEventPublisher palletScannerEventPublisher;
    private final ProductScannerEventPublisher productScannerEventPublisher;
    private final ProductPalletEventPublisher productPalletEventPublisher;
    private final ProductProductEventPublisher productProductEventPublisher;
    private final PalletUnAssignEventPublisher palletUnAssignEventPublisher;
    private final ProductUnAssignEventPublisher productUnAssignEventPublisher;
    private final DataService dataService;
    private final Cache<UUID, Data> deviceLastDataCache;
    private final ScannerMapper scannerMapper;


    public ScannerInternalService(ScannerRepository scannerRepository,
                                  @Lazy ScannerConnectionInternalService scannerConnectionInternalService,
                                  ProductPendingPalletInternalService productPendingPalletInternalService,
                                  PalletScannerEventPublisher palletScannerEventPublisher,
                                  ProductScannerEventPublisher productScannerEventPublisher,
                                  ProductPalletEventPublisher productPalletEventPublisher,
                                  ProductProductEventPublisher productProductEventPublisher, PalletUnAssignEventPublisher palletUnAssignEventPublisher, ProductUnAssignEventPublisher productUnAssignEventPublisher,
                                  DataService dataService,
                                  Cache deviceLastDataCache, ScannerMapper scannerMapper) {
        this.scannerRepository = scannerRepository;
        this.scannerConnectionInternalService = scannerConnectionInternalService;
        this.productPendingPalletInternalService = productPendingPalletInternalService;
        this.palletScannerEventPublisher = palletScannerEventPublisher;
        this.productScannerEventPublisher = productScannerEventPublisher;
        this.productPalletEventPublisher = productPalletEventPublisher;
        this.productProductEventPublisher = productProductEventPublisher;
        this.palletUnAssignEventPublisher = palletUnAssignEventPublisher;
        this.productUnAssignEventPublisher = productUnAssignEventPublisher;
        this.dataService = dataService;
        this.deviceLastDataCache = deviceLastDataCache;
        this.scannerMapper = scannerMapper;
    }

    @Transactional
    public Scanner create(Scanner scanner) {
        if (scanner.getType() == null) {
            scanner.setType(ScannerType.QR_SCANNER);
        }
        if (scanner.getReadEntityType() == null) {
            scanner.setReadEntityType(ScannerReadEntityType.PRODUCT);
        }
        if (scanner.getRole() == null) {
            switch (scanner.getReadEntityType()) {
                case PRODUCT_LINK -> scanner.setRole(ScannerRole.PRODUCT_PRODUCT_ASSIGNER);
                case PALLET -> scanner.setRole(ScannerRole.PALLET_SCANNER);
                default -> scanner.setRole(ScannerRole.PRODUCT_SCANNER);
            }
        }
        var existingScanner = findByUniqueId(scanner.getUniqueId());
        if (existingScanner.isPresent()) {
            throw new EntryAlreadyExistsException("Scanner already exists");
        }

        var now = Instant.now().getEpochSecond();
        scanner.setCreatedAt(now);
        scanner.setUpdatedAt(null);
        return scannerRepository.save(scanner);
    }


    @CacheEvict(value = "scanner", key = "#scanner.uniqueId")
    @Transactional
    public Scanner updateDevice(Scanner scanner) {
        var now = Instant.now().getEpochSecond();
        scanner.setUpdatedAt(now);
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
        scannerRepository.setLastDataIdAndLastDataTimeByUniqueId(uniqueId, data.getId(), data.getData(), data.getServerTime());
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

    @Transactional(readOnly = true)
    public Page<Scanner> findAll(UUID companyId, ScannerType type, PageRequest pageRequest) {
        var result = scannerRepository.findAll(
                ScannerRepository.Specifications.withCompanyIdAndType(companyId, type),
                pageRequest
        );        return new Page<>(result.getContent().stream().toList(),
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

    private void productScanned(Scanner scanner, Data data) {
        if (scanner.getLastDataValue() != null && scanner.getLastDataValue().equals(data.getData())) {
            return;
        }
        var event = new ProductScanned(scanner.getUniqueId(), data.getData());
        productScannerEventPublisher.publish(event);
    }

    private void palletScanned(Scanner scanner, Data data) {
        if (scanner.getLastDataValue() != null && scanner.getLastDataValue().equals(data.getData())) {
            return;
        }
        var event = new PalletScannned(scanner.getUniqueId(), data.getData());
        palletScannerEventPublisher.publish(event);
    }

    @Transactional
    private void productPalletAssigned(Scanner scanner, Data data) {
        if (scanner.getLastDataValue() != null && scanner.getLastDataValue().equals(data.getData())) {
            return;
        }
        if (scanner.getReadEntityType().equals(ScannerReadEntityType.PRODUCT)) {
            var connection = scannerConnectionInternalService.findByScannerId(scanner.getUniqueId());
            if (connection.isPresent()) {
                var productScanner = scanner;
                var productData = data;

                var palletScanner = connection.get().getFirstScanner().getUniqueId().equals(productScanner.getUniqueId())
                        ? connection.get().getSecondScanner()
                        : connection.get().getFirstScanner();
                var palletData = findLatestScannerData(palletScanner.getUniqueId());

                if (palletData.isPresent()
                        && connection.get().getCurrentAssignedCount() < connection.get().getCapacity()) {
                    var event = new ProductPalletAssigned(palletData.get().getData(), productData.getData());
                    productPalletEventPublisher.publish(event);
                    connection.get().setCurrentAssignedCount(connection.get().getCurrentAssignedCount() + 1);
                    scannerConnectionInternalService.update(connection.get());
                } else {
                    productPendingPalletInternalService.save(
                            new ProductPendingPallet(productScanner.getUniqueId(), palletScanner.getUniqueId(),
                                    productData.getData(), connection.get().getCompanyId()));
                }

            }
        } else if (scanner.getReadEntityType().equals(ScannerReadEntityType.PALLET)) {
            var connection = scannerConnectionInternalService.findByScannerId(scanner.getUniqueId());
            if (connection.isPresent()) {
                var palletScanner = scanner;
                var palletData = data;

                var productScanner = connection.get().getFirstScanner().getUniqueId().equals(palletScanner.getUniqueId())
                        ? connection.get().getSecondScanner()
                        : connection.get().getFirstScanner();
                var productData = findLatestScannerData(productScanner.getUniqueId());

                if (!Objects.equals(palletScanner.getLastDataValue(), data.getData())) {
                    connection.get().setCurrentAssignedCount(0);
                }

                if (connection.get().getCurrentAssignedCount() < connection.get().getCapacity()) {
                    for (var ppp : productPendingPalletInternalService.findByPalletScanner(palletScanner.getUniqueId())) {
                        var event = new ProductPalletAssigned(palletData.getData(), ppp.getProductData());
                        productPalletEventPublisher.publish(event);
                        connection.get().setCurrentAssignedCount(connection.get().getCurrentAssignedCount() + 1);
                        scannerConnectionInternalService.update(connection.get());
                        productPendingPalletInternalService.deleteAllById(List.of(ppp.getId()));

                        if (connection.get().getCurrentAssignedCount() == connection.get().getCapacity()) {
                            break;
                        }
                    }
                }
//                if (productData.isPresent()) {
//                    if (connection.get().getCurrentAssignedCount() < connection.get().getCapacity()) {
//                        var event = new ProductPalletAssigned(palletData.getData(), productData.get().getData());
//                        productPalletEventPublisher.publish(event);
//                        connection.get().setCurrentAssignedCount(connection.get().getCurrentAssignedCount() + 1);
//                        scannerConnectionInternalService.update(connection.get());
//                    } else {
//                        productPendingPalletInternalService.save(
//                                new ProductPendingPallet(productScanner.getUniqueId(), palletScanner.getUniqueId(),
//                                        productData.get().getData(), connection.get().getCompanyId()));
//                    }
//                }
            }
        }
    }

    private void productUnAssigned(Scanner scanner, Data data) {
        if (scanner.getLastDataValue() != null && scanner.getLastDataValue().equals(data.getData())) {
            return;
        }
        var event = new ProductUnAssigned(scanner.getUniqueId(), data.getData());
//        productUnAssignEventPublisher.publish(event);
//        var connection = scannerConnectionInternalService.findByScannerId(scanner.getUniqueId());
//        if (connection.isPresent()
//                && connection.get().getCurrentAssignedCount() > 0) {
//                connection.get().setCurrentAssignedCount(connection.get().getCurrentAssignedCount() - 1);
//                scannerConnectionInternalService.update(connection.get());
//
//        }
    }

    private void palletUnAssigned(Scanner scanner, Data data) {
        if (scanner.getLastDataValue() != null && scanner.getLastDataValue().equals(data.getData())) {
            return;
        }
        var event = new PalletUnAssigned(scanner.getUniqueId(), data.getData());
        palletUnAssignEventPublisher.publish(event);
//        var connection = scannerConnectionInternalService.findByScannerId(scanner.getUniqueId());
//        if (connection.isPresent()) {
//            connection.get().setCurrentAssignedCount(0);
//            scannerConnectionInternalService.update(connection.get());
//        }
    }

    private void productProductAssigned(Scanner scanner, Data data) {
        if (scanner.getLastDataValue() != null && scanner.getLastDataValue().equals(data.getData())) {
            return;
        }
        if (scanner.getReadEntityType().equals(ScannerReadEntityType.PRODUCT)) {
            var connection = scannerConnectionInternalService.findByScannerId(scanner.getUniqueId());
            if (connection.isPresent()) {
                var productScanner = scanner;
                var productData = data;

                var productLinkScanner = connection.get().getFirstScanner().getUniqueId().equals(productScanner.getUniqueId())
                        ? connection.get().getSecondScanner()
                        : connection.get().getFirstScanner();
                var productLinkData = findLatestScannerData(productLinkScanner.getUniqueId());

                if (productLinkData.isPresent()
                        && TimeUtil.isTimestampDifferenceLessThan(productData.getServerTime(), productLinkData.get().getServerTime(), 5)) {
                    var event = new ProductProductAssigned(productLinkData.get().getData(), productData.getData());
                    productProductEventPublisher.publish(event);
                }
            }
        } else if (scanner.getReadEntityType().equals(ScannerReadEntityType.PRODUCT_LINK)) {
            var connection = scannerConnectionInternalService.findByScannerId(scanner.getUniqueId());
            if (connection.isPresent()) {
                var productLinkScanner = scanner;
                var productLinkData = data;

                var productScanner = connection.get().getFirstScanner().getUniqueId().equals(productLinkScanner.getUniqueId())
                        ? connection.get().getSecondScanner()
                        : connection.get().getFirstScanner();
                var productData = findLatestScannerData(productScanner.getUniqueId());

                if (productData.isPresent()
                        && TimeUtil.isTimestampDifferenceLessThan(productData.get().getServerTime(), productLinkData.getServerTime(), 5)) {

                    var event = new ProductProductAssigned(productLinkData.getData(), productData.get().getData());
                    productProductEventPublisher.publish(event);
                }
            }
        }
    }
}
