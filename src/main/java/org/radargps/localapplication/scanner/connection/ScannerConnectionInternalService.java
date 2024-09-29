package org.radargps.localapplication.scanner.connection;

import org.radargps.localapplication.common.errors.exception.ResourceNotFoundException;
import org.radargps.localapplication.common.pageable.Page;
import org.radargps.localapplication.scanner.connection.domain.ScannerConnection;
import org.radargps.localapplication.scanner.connection.domain.ScannerConnectionType;
import org.radargps.localapplication.scanner.device.ScannerInternalService;
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
        var firstScanner = scannerInternalService.findByUniqueId(createConnection.getFirstScanner().getUniqueId())
                .orElseThrow(ResourceNotFoundException::new);
        firstScanner.setReadEntityType(createConnection.getFirstScanner().getReadEntityType());
        scannerInternalService.updateDevice(firstScanner);

        var secondScanner = scannerInternalService.findByUniqueId(createConnection.getSecondScanner().getUniqueId())
                .orElseThrow(ResourceNotFoundException::new);
        secondScanner.setReadEntityType(createConnection.getSecondScanner().getReadEntityType());
        scannerInternalService.updateDevice(secondScanner);

        createConnection.setId(UUID.randomUUID());
        return scannerConnectionRepository.save(createConnection);
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
