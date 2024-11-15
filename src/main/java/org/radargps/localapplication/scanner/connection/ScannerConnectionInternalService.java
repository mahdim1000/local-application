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
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
public class ScannerConnectionInternalService {
    private final ScannerConnectionRepository scannerConnectionRepository;
    private final ScannerConnectionCriteria scannerConnectionCriteria;
    private final ScannerConnectionMapper scannerConnectionMapper;

    @Autowired
    private final ScannerInternalService scannerInternalService;

    public ScannerConnectionInternalService(ScannerConnectionRepository scannerConnectionRepository,
                                            ScannerConnectionCriteria scannerConnectionCriteria, ScannerConnectionMapper scannerConnectionMapper,
                                            @Lazy ScannerInternalService scannerInternalService) {
        this.scannerConnectionRepository = scannerConnectionRepository;
        this.scannerConnectionCriteria = scannerConnectionCriteria;
        this.scannerConnectionMapper = scannerConnectionMapper;
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

    @Transactional
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

    @Transactional
    public ScannerConnection update(ScannerConnection updateConnection) {
        var existingConnection = findById(updateConnection.getId())
                .orElseThrow(ResourceNotFoundException::new);

        if (isScannerChanged(existingConnection.getFirstScanner(), updateConnection.getFirstScanner())) {
            var firstScanner = scannerInternalService.findByUniqueId(updateConnection.getFirstScanner().getUniqueId());
            var scannerRole = connectionTypeToRole(updateConnection.getType());

            if (firstScanner.isEmpty()) {
                var uniqueId = updateConnection.getFirstScanner().getUniqueId();
                var companyId = updateConnection.getCompanyId();
                var readEntityType = updateConnection.getFirstScanner().getReadEntityType();

                var createScanner = new Scanner(uniqueId, companyId, null, ScannerType.QR_SCANNER, readEntityType, scannerRole);
                createScanner = scannerInternalService.create(createScanner);
                updateConnection.setFirstScanner(createScanner);
            } else {
                firstScanner.get().setReadEntityType(updateConnection.getFirstScanner().getReadEntityType());
                firstScanner.get().setRole(scannerRole);
                var updateScanner = scannerInternalService.updateDevice(firstScanner.get());
                updateConnection.setFirstScanner(updateScanner);
            }
        }

        if (isScannerChanged(existingConnection.getSecondScanner(), updateConnection.getSecondScanner())) {
            var secondScanner = scannerInternalService.findByUniqueId(updateConnection.getSecondScanner().getUniqueId());
            var scannerRole = connectionTypeToRole(updateConnection.getType());

            if (secondScanner.isEmpty()) {
                var uniqueId = updateConnection.getSecondScanner().getUniqueId();
                var companyId = updateConnection.getCompanyId();
                var readEntityType = updateConnection.getSecondScanner().getReadEntityType();

                var createScanner = new Scanner(uniqueId, companyId, null, ScannerType.QR_SCANNER, readEntityType, scannerRole);
                createScanner = scannerInternalService.create(createScanner);
                updateConnection.setSecondScanner(createScanner);
            } else {
                secondScanner.get().setReadEntityType(updateConnection.getSecondScanner().getReadEntityType());
                secondScanner.get().setRole(scannerRole);
                var updateScanner = scannerInternalService.updateDevice(secondScanner.get());
                updateConnection.setSecondScanner(updateScanner);
            }
        }

        return scannerConnectionRepository.save(updateConnection);
    }

    ScannerConnection merge(ScannerConnection savedEntity, ScannerConnection updatedEntity) {
        ScannerConnection mergedEntity = new ScannerConnection();
        mergedEntity.setId(savedEntity.getId());
        savedEntity.setCapacity(updatedEntity.getCapacity() != null ? updatedEntity.getCapacity() : savedEntity.getCapacity());
        savedEntity.setType(updatedEntity.getType() != null ? updatedEntity.getType() : savedEntity.getType());
        savedEntity.setCompanyId(updatedEntity.getCompanyId() != null ? updatedEntity.getCompanyId() : savedEntity.getCompanyId());

        Scanner firstScanner = updatedEntity.getFirstScanner() != null && updatedEntity.getFirstScanner().getUniqueId() != null ? updatedEntity.getFirstScanner() : savedEntity.getFirstScanner();
        savedEntity.setFirstScanner(updatedEntity.getFirstScanner() != null ? updatedEntity.getFirstScanner() : savedEntity.getFirstScanner());
        savedEntity.setSecondScanner(updatedEntity.getSecondScanner() != null ? updatedEntity.getSecondScanner() : savedEntity.getSecondScanner());
        return savedEntity;
    }

    boolean isScannerChanged(Scanner s1, Scanner s2) {
        return !s1.getUniqueId().equals(s2.getUniqueId())
                || !s1.getReadEntityType().equals(s2.getReadEntityType());
    }

    public void delete(ScannerConnection connection) {
        scannerConnectionRepository.delete(connection);
    }

}
