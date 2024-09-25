package org.radargps.localapplication.scanner.connection.dto;

import jakarta.validation.constraints.NotNull;
import org.radargps.localapplication.scanner.connection.domain.ScannerConnectionType;
import org.radargps.localapplication.scanner.device.domain.ScannerRole;

import java.util.UUID;

public record ScannerConnectionUpdateCommand(
        UUID firstScannerId,
        ScannerRole firstScannerRole,

        UUID secondScannerId,
        ScannerRole secondScannerRole,

        ScannerConnectionType type,
        Integer capacity

) {
}
