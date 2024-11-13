package org.radargps.localapplication.scanner.device.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import org.radargps.localapplication.scanner.device.domain.ScannerReadEntityType;
import org.radargps.localapplication.scanner.device.domain.ScannerRole;
import org.radargps.localapplication.scanner.device.domain.ScannerType;

import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ScannerRequest(
        String uniqueId,
        UUID companyId,
        String ipAddress,
        @Enumerated(EnumType.STRING)
        ScannerType type,
        @Enumerated(EnumType.STRING)
        ScannerReadEntityType readEntityType,
        @Enumerated(EnumType.STRING)
        ScannerRole role,
        UUID lastDataId,
        String lastDataValue,
        Long lastDataTime,
        Long createdAt,
        Long updatedAt
) {
}
