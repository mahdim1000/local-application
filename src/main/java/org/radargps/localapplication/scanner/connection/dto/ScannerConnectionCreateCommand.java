package org.radargps.localapplication.scanner.connection.dto;

import jakarta.validation.constraints.NotNull;
import org.radargps.localapplication.scanner.connection.domain.ScannerConnectionType;

import java.util.UUID;

public record ScannerConnectionCreateCommand(
        @NotNull
        UUID firstScannerId,

        @NotNull
        UUID secondScannerId,

        @NotNull
        ScannerConnectionType type,
        Integer capacity

) {
}
