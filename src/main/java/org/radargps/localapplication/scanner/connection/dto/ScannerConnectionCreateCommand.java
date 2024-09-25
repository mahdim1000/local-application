package org.radargps.localapplication.scanner.connection.dto;

import jakarta.validation.constraints.NotNull;
import org.radargps.localapplication.scanner.connection.domain.ScannerConnectionType;
import org.radargps.localapplication.scanner.device.domain.ScannerRole;

import java.util.UUID;

public record ScannerConnectionCreateCommand(
        @NotNull
        UUID firstScannerId,
        @NotNull
        ScannerRole firstScannerRole,

        @NotNull
        UUID secondScannerId,
        @NotNull
        ScannerRole secondScannerRole,

        @NotNull
        ScannerConnectionType type,
        Integer capacity

) {
}
