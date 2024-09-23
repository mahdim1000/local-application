package org.radargps.localapplication.scanner.connection.temp;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ProductPalletScannerConnectionCommand(
        @NotNull
        UUID firstScannerId,

        @NotNull
        UUID secondScannerId,
        Integer capacity
) {
}
