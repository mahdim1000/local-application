package org.radargps.localapplication.scanner.device.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import org.radargps.localapplication.scanner.device.domain.ScannerReadEntityType;
import org.radargps.localapplication.scanner.device.domain.ScannerRole;
import org.radargps.localapplication.scanner.device.domain.ScannerType;

import java.util.UUID;

public record ScannerUpdateCommand(
        UUID companyId,
        String ipAddress,
        ScannerType type,
        @Enumerated(EnumType.STRING)
        ScannerReadEntityType readEntityType,
        @Enumerated(EnumType.STRING)
        ScannerRole role) {
}
