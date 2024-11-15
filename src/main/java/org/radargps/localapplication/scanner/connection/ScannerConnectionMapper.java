package org.radargps.localapplication.scanner.connection;

import org.mapstruct.*;
import org.mapstruct.control.DeepClone;
import org.radargps.localapplication.scanner.connection.domain.ScannerConnection;
import org.radargps.localapplication.scanner.connection.dto.ScannerConnectionCreateCommand;
import org.radargps.localapplication.scanner.connection.dto.ScannerConnectionRequest;
import org.radargps.localapplication.scanner.connection.dto.ScannerConnectionUpdateCommand;

@Mapper(componentModel = "spring", mappingControl = DeepClone.class)
public interface ScannerConnectionMapper {
    ScannerConnection toEntity(ScannerConnectionCreateCommand createCommand);
    ScannerConnection toEntity(ScannerConnectionUpdateCommand updateCommand);
    ScannerConnectionRequest toRequest(ScannerConnection scannerConnection);

    @Named("partialUpdate")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void partialUpdate(@MappingTarget ScannerConnection entity, ScannerConnectionUpdateCommand updateCommand);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    ScannerConnection clone(ScannerConnection entity);
}
