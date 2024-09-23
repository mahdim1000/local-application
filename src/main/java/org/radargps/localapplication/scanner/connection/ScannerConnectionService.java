package org.radargps.localapplication.scanner.connection;

import org.radargps.localapplication.common.pageable.Page;
import org.radargps.localapplication.scanner.connection.domain.ScannerConnectionType;
import org.radargps.localapplication.scanner.connection.dto.ScannerConnectionCreateCommand;
import org.radargps.localapplication.scanner.connection.dto.ScannerConnectionRequest;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ScannerConnectionService {
    public ScannerConnectionRequest createConnection(ScannerConnectionCreateCommand command) {
        return null;
    }

    public ScannerConnectionRequest findById(UUID uuid) {
        return null;
    }

    public Page<ScannerConnectionRequest> findByCompanyIdAndType(UUID companyId, ScannerConnectionType type) {
        return null;
    }
}
