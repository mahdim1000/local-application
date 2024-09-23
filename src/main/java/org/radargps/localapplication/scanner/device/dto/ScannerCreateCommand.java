package org.radargps.localapplication.scanner.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.radargps.localapplication.scanner.device.domain.ScannerReadEntityType;
import org.radargps.localapplication.scanner.device.domain.ScannerRole;
import org.radargps.localapplication.scanner.device.domain.ScannerType;

import java.util.UUID;

public record ScannerCreateCommand(
        @NotNull
        @NotEmpty
        String uniqueId,
        @NotNull
        UUID companyId,
        String ipAddress,
        @NotNull
        @Enumerated(EnumType.STRING)
        ScannerType type,
        @NotNull
        @Enumerated(EnumType.STRING)
        ScannerReadEntityType readEntityType,
        @NotNull
        @Enumerated(EnumType.STRING)
        ScannerRole role
) {
}
