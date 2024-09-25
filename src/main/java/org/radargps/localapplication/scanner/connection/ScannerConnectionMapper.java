package org.radargps.localapplication.scanner.connection;

import org.mapstruct.*;
import org.radargps.localapplication.scanner.connection.domain.ScannerConnection;
import org.radargps.localapplication.scanner.connection.dto.ScannerConnectionCreateCommand;
import org.radargps.localapplication.scanner.connection.dto.ScannerConnectionRequest;
import org.radargps.localapplication.scanner.connection.dto.ScannerConnectionUpdateCommand;

@Mapper(componentModel = "spring")
public interface ScannerConnectionMapper {
    ScannerConnection toEntity(ScannerConnectionCreateCommand createCommand);

    ScannerConnectionRequest toRequest(ScannerConnection scannerConnection);

    @Named("partialUpdate")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void partialUpdate(@MappingTarget ScannerConnection entity, ScannerConnectionUpdateCommand updateCommand);
}
