package org.radargps.localapplication.scanner.device;

import org.mapstruct.*;
import org.radargps.localapplication.scanner.device.domain.Scanner;
import org.radargps.localapplication.scanner.device.dto.ScannerCreateCommand;
import org.radargps.localapplication.scanner.device.dto.ScannerRequest;
import org.radargps.localapplication.scanner.device.dto.ScannerUpdateCommand;

@Mapper(componentModel = "spring")
public interface ScannerMapper {

    Scanner toEntity(ScannerCreateCommand dto);
    ScannerRequest toRequest(Scanner entity);
    @Named("partialUpdate")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void partialUpdate(@MappingTarget Scanner entity, ScannerUpdateCommand dto);
}
