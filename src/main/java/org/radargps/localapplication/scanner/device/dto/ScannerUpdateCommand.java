package org.radargps.localapplication.scanner.dto;


import org.radargps.localapplication.scanner.device.domain.ScannerReadEntityType;
import org.radargps.localapplication.scanner.device.domain.ScannerRole;
import org.radargps.localapplication.scanner.device.domain.ScannerType;

import java.util.UUID;

public record ScannerUpdateCommand(
        String uniqueId,
        UUID companyId,
        String ipAddress,
        ScannerType type,
        ScannerReadEntityType readEntityType,
        ScannerRole role) {
}
