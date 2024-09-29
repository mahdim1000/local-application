package org.radargps.localapplication.scanner.connection.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import org.radargps.localapplication.scanner.connection.domain.ScannerConnectionType;
import org.radargps.localapplication.scanner.device.domain.ScannerReadEntityType;

import java.util.UUID;

public record ScannerConnectionRequest(
        UUID id,
        ScannerConnectionCreateCommand.ScannerRecord firstScanner,
        ScannerConnectionCreateCommand.ScannerRecord secondScanner,
        ScannerConnectionType type,
        Integer capacity
) {
    record ScannerRecord(
            String uniqueId,
            @Enumerated(EnumType.STRING)
            ScannerReadEntityType scannerReadEntityType
    ) {}
}
