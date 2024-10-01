package org.radargps.localapplication.scanner.connection;

import org.radargps.localapplication.common.errors.exception.ResourceNotFoundException;
import org.radargps.localapplication.common.pageable.Page;
import org.radargps.localapplication.scanner.connection.domain.ScannerConnection;
import org.radargps.localapplication.scanner.connection.domain.ScannerConnectionType;
import org.radargps.localapplication.scanner.connection.dto.ScannerConnectionCreateCommand;
import org.radargps.localapplication.scanner.connection.dto.ScannerConnectionRequest;
import org.radargps.localapplication.scanner.connection.dto.ScannerConnectionUpdateCommand;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
public class ScannerConnectionService {
    private final ScannerConnectionMapper scannerConnectionMapper;
    private final ScannerConnectionInternalService scannerConnectionInternalService;

    public ScannerConnectionService(ScannerConnectionMapper scannerConnectionMapper,
                                    ScannerConnectionInternalService scannerConnectionInternalService) {
        this.scannerConnectionMapper = scannerConnectionMapper;
        this.scannerConnectionInternalService = scannerConnectionInternalService;
    }

    @Transactional
    public ScannerConnectionRequest createConnection(ScannerConnectionCreateCommand createCommand) {
        var createConnection = scannerConnectionMapper.toEntity(createCommand);
        ScannerConnection scannerConnection = this.scannerConnectionInternalService.create(createConnection);
        return scannerConnectionMapper.toRequest(scannerConnection);
    }

    @Transactional(readOnly = true)
    public Optional<ScannerConnectionRequest> findById(UUID scannerConnectionId) {
        return scannerConnectionInternalService.findById(scannerConnectionId)
                .map(scannerConnectionMapper::toRequest);
    }


    @Transactional(readOnly = true)
    public Page<ScannerConnectionRequest> findAll(UUID companyId, ScannerConnectionType type, Integer pageSize, Integer pageNumber) {
        var result = scannerConnectionInternalService.findAll(companyId, type, pageSize, pageNumber);
        var content = result.getContent().stream()
                .map(scannerConnectionMapper::toRequest)
                .toList();

        return new Page<>(content, result.getTotalElements());
    }

    @Transactional
    public ScannerConnectionRequest partialUpdate(UUID scannerConnectionId, ScannerConnectionUpdateCommand updateCommand) {
        var entity = scannerConnectionInternalService.findById(scannerConnectionId)
                        .orElseThrow(ResourceNotFoundException::new);
        scannerConnectionMapper.partialUpdate(entity, updateCommand);
        scannerConnectionInternalService.update(entity);
        return scannerConnectionMapper.toRequest(entity);
    }
}
