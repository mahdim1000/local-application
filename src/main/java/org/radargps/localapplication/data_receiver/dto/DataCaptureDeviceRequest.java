package org.radargps.localapplication.data_receiver.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import org.radargps.localapplication.data_receiver.domain.DeviceReadEntityType;
import org.radargps.localapplication.data_receiver.domain.DeviceRole;
import org.radargps.localapplication.data_receiver.domain.DeviceType;

import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record DataCaptureDeviceRequest(
        UUID id,
        String uniqueId,
        UUID companyId,
        String ipAddress,
        @Enumerated(EnumType.STRING)
        DeviceType type,
        @Enumerated(EnumType.STRING)
        DeviceReadEntityType readEntityType,
        @Enumerated(EnumType.STRING)
        DeviceRole role,
        UUID lastDataId,
        Long lastDataTime
) {
}
