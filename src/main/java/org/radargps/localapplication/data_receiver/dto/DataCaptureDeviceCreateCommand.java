package org.radargps.localapplication.data_receiver.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.radargps.localapplication.data_receiver.domain.DeviceReadEntityType;
import org.radargps.localapplication.data_receiver.domain.DeviceRole;
import org.radargps.localapplication.data_receiver.domain.DeviceType;

import java.util.UUID;

public record DataCaptureDeviceCreateCommand(
        @NotNull
        @NotEmpty
        String uniqueId,
        @NotNull
        UUID companyId,
        String ipAddress,
        @NotNull
        @Enumerated(EnumType.STRING)
        DeviceType type,
        @NotNull
        @Enumerated(EnumType.STRING)
        DeviceReadEntityType readEntityType,
        @NotNull
        @Enumerated(EnumType.STRING)
        DeviceRole role
) {
}
