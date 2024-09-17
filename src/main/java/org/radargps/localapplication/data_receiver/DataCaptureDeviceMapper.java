package org.radargps.localapplication.data_receiver;

import org.mapstruct.*;
import org.radargps.localapplication.data_receiver.domain.DataCaptureDevice;
import org.radargps.localapplication.data_receiver.dto.DataCaptureDeviceCreateCommand;
import org.radargps.localapplication.data_receiver.dto.DataCaptureDeviceRequest;
import org.radargps.localapplication.data_receiver.dto.DataCaptureDeviceUpdateCommand;

@Mapper(componentModel = "spring")
public interface DataCaptureDeviceMapper {

    DataCaptureDevice toEntity(DataCaptureDeviceCreateCommand dto);
    DataCaptureDeviceRequest toRequest(DataCaptureDevice entity);
    @Named("partialUpdate")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void partialUpdate(@MappingTarget DataCaptureDevice entity, DataCaptureDeviceUpdateCommand dto);
}
