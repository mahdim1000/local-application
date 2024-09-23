package org.radargps.localapplication.scanner.connection.temp;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ProductPalletScannerConnectionRequest(
        @NotNull
        UUID firstDeviceId,

        @NotNull
        UUID secondDeviceId,
        Integer capacity
) {
}
