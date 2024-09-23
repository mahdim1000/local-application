package org.radargps.localapplication.scanner.connection.dto;

import jakarta.validation.constraints.NotNull;
import org.radargps.localapplication.scanner.connection.domain.ScannerConnectionType;

import java.util.UUID;

public record ScannerConnectionRequest(
        UUID firstScannerId,
        UUID secondScannerId,
        ScannerConnectionType type,
        Integer capacity

) {
}
