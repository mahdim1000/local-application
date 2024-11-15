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

    public ScannerConnectionRequest createConnection(ScannerConnectionCreateCommand createCommand) {
        var createConnection = scannerConnectionMapper.toEntity(createCommand);
        ScannerConnection scannerConnection = this.scannerConnectionInternalService.create(createConnection);
        return scannerConnectionMapper.toRequest(scannerConnection);
    }

    public Optional<ScannerConnectionRequest> findById(UUID scannerConnectionId) {
        return scannerConnectionInternalService.findById(scannerConnectionId)
                .map(scannerConnectionMapper::toRequest);
    }


    public Page<ScannerConnectionRequest> findAll(UUID companyId, ScannerConnectionType type, Integer pageSize, Integer pageNumber) {
        var result = scannerConnectionInternalService.findAll(companyId, type, pageSize, pageNumber);
        var content = result.getContent().stream()
                .map(scannerConnectionMapper::toRequest)
                .toList();

        return new Page<>(content, result.getTotalElements());
    }

    public ScannerConnectionRequest partialUpdate(UUID scannerConnectionId, ScannerConnectionUpdateCommand updateCommand) {
        // Find entity once
        var entity = scannerConnectionInternalService.findById(scannerConnectionId)
                .orElseThrow(ResourceNotFoundException::new);

        var entityCopy = scannerConnectionMapper.clone(entity);
        // Apply partial updates
        scannerConnectionMapper.partialUpdate(entityCopy, updateCommand);

        // Update and get result
        var updatedEntity = scannerConnectionInternalService.update(entityCopy);
        return scannerConnectionMapper.toRequest(updatedEntity);
    }

    public void deleteConnection(UUID scannerConnectionId) {
        var connection = scannerConnectionInternalService.findById(scannerConnectionId)
                .orElseThrow(ResourceNotFoundException::new);
        scannerConnectionInternalService.delete(connection);
    }

}
