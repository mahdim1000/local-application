package org.radargps.localapplication.scanner.connection;

import org.radargps.localapplication.common.errors.exception.InvalidArgumentException;
import org.radargps.localapplication.common.errors.exception.ResourceNotFoundException;
import org.radargps.localapplication.common.pageable.Page;
import org.radargps.localapplication.scanner.connection.domain.ScannerConnection;
import org.radargps.localapplication.scanner.connection.domain.ScannerConnectionType;
import org.radargps.localapplication.scanner.device.ScannerInternalService;
import org.radargps.localapplication.scanner.device.domain.Scanner;
import org.radargps.localapplication.scanner.device.domain.ScannerRole;
import org.radargps.localapplication.scanner.device.domain.ScannerType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class ScannerConnectionInternalService {
    private final ScannerConnectionRepository scannerConnectionRepository;
    private final ScannerConnectionCriteria scannerConnectionCriteria;

    @Autowired
    private final ScannerInternalService scannerInternalService;

    public ScannerConnectionInternalService(ScannerConnectionRepository scannerConnectionRepository,
                                            ScannerConnectionCriteria scannerConnectionCriteria,
                                            @Lazy ScannerInternalService scannerInternalService) {
        this.scannerConnectionRepository = scannerConnectionRepository;
        this.scannerConnectionCriteria = scannerConnectionCriteria;
        this.scannerInternalService = scannerInternalService;
    }

    public ScannerConnection create(ScannerConnection createConnection) {
        var scannerRole = connectionTypeToRole(createConnection.getType());
        if (scannerRole == null) {
            throw new InvalidArgumentException("type is not valid");
        }
        Scanner firstExistingScanner;
        Scanner secondExistingScanner;

        var firstScanner = scannerInternalService.findByUniqueId(createConnection.getFirstScanner().getUniqueId());
        if (firstScanner.isEmpty()) {
            var uniqueId = createConnection.getFirstScanner().getUniqueId();
            var companyId = createConnection.getCompanyId();
            var readEntityType = createConnection.getFirstScanner().getReadEntityType();
            var createScanner = new Scanner(uniqueId, companyId, null, ScannerType.QR_SCANNER, readEntityType, scannerRole);
            firstExistingScanner = scannerInternalService.create(createScanner);
        } else {
            firstScanner.get().setReadEntityType(createConnection.getFirstScanner().getReadEntityType());
            firstScanner.get().setRole(scannerRole);
            firstExistingScanner = scannerInternalService.updateDevice(firstScanner.get());
        }

        var secondScanner = scannerInternalService.findByUniqueId(createConnection.getSecondScanner().getUniqueId());
        if (secondScanner.isEmpty()) {
            var uniqueId = createConnection.getSecondScanner().getUniqueId();
            var companyId = createConnection.getCompanyId();
            var readEntityType = createConnection.getSecondScanner().getReadEntityType();
            var createScanner = new Scanner(uniqueId, companyId, null, ScannerType.QR_SCANNER, readEntityType, scannerRole);
            secondExistingScanner = scannerInternalService.create(createScanner);
        } else {
            secondScanner.get().setReadEntityType(createConnection.getSecondScanner().getReadEntityType());
            secondScanner.get().setRole(scannerRole);
            secondExistingScanner = scannerInternalService.updateDevice(secondScanner.get());
        }

        var capacity = createConnection.getCapacity() == null ? 0 : createConnection.getCapacity();
        createConnection.setId(UUID.randomUUID());
        createConnection.setFirstScanner(firstExistingScanner);
        createConnection.setSecondScanner(secondExistingScanner);
        createConnection.setCapacity(capacity);
        scannerConnectionRepository.save(createConnection);
        return createConnection;
    }
    private ScannerRole connectionTypeToRole(ScannerConnectionType type) {
        if (type.equals(ScannerConnectionType.PRODUCT_PALLET_ASSIGN)) {
            return ScannerRole.PRODUCT_PALLET_ASSIGNER;
        }
        if (type.equals(ScannerConnectionType.PRODUCT_PRODUCT_LINK_ASSIGN)) {
            return ScannerRole.PRODUCT_PRODUCT_ASSIGNER;
        } else {
            return null;
        }
    }

    public Optional<ScannerConnection> findById(UUID scannerConnectionId) {
        return scannerConnectionRepository.findById(scannerConnectionId);
    }

    public Optional<ScannerConnection> findByScannerId(String scannerId) {
        return scannerConnectionCriteria.findByFirstScannerIdOrSecondSecannerId(scannerId);
    }

    public Page<ScannerConnection> findAll(UUID companyId, ScannerConnectionType type, Integer pageSize, Integer pageNumber) {
        if (pageNumber == null) {
            pageNumber = 0;
        }
        if (pageSize == null) {
            pageSize = 10;
        }
        var dbPage = scannerConnectionCriteria.findAll(companyId, type, PageRequest.of(pageNumber, pageSize));
        return new Page<>(dbPage.getContent(), dbPage.getTotalElements());
    }

    public void update(ScannerConnection entity) {
        scannerConnectionRepository.save(entity);
    }
}
