package org.radargps.localapplication.scanner.connection;

import org.radargps.localapplication.common.pageable.Page;
import org.radargps.localapplication.scanner.connection.domain.ScannerConnection;
import org.radargps.localapplication.scanner.connection.domain.ScannerConnectionType;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class ScannerConnectionInternalService {
    private final ScannerConnectionRepository scannerConnectionRepository;
    private final ScannerConnectionCriteria scannerConnectionCriteria;

    public ScannerConnectionInternalService(ScannerConnectionRepository scannerConnectionRepository, ScannerConnectionCriteria scannerConnectionCriteria) {
        this.scannerConnectionRepository = scannerConnectionRepository;
        this.scannerConnectionCriteria = scannerConnectionCriteria;
    }

    public ScannerConnection create(ScannerConnection createConnection) {
        createConnection.setId(UUID.randomUUID());
        return scannerConnectionRepository.save(createConnection);
    }

    public Optional<ScannerConnection> findById(UUID scannerConnectionId) {
        return scannerConnectionRepository.findById(scannerConnectionId);
    }

    public Optional<ScannerConnection> findByScannerId(UUID scannerId) {
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
