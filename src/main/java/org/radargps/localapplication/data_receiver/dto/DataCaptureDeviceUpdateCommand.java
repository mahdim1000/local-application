package org.radargps.localapplication.data_receiver.dto;

import org.radargps.localapplication.data_receiver.domain.DeviceReadEntityType;
import org.radargps.localapplication.data_receiver.domain.DeviceRole;
import org.radargps.localapplication.data_receiver.domain.DeviceType;

import java.util.UUID;

public record DataCaptureDeviceUpdateCommand(
        String uniqueId,
        UUID companyId,
        String ipAddress,
        DeviceType type,
        DeviceReadEntityType readEntityType,
        DeviceRole role
) {
}
