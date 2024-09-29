package org.radargps.localapplication.scanner.connection.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import org.radargps.localapplication.scanner.connection.domain.ScannerConnectionType;
import org.radargps.localapplication.scanner.device.domain.Scanner;
import org.radargps.localapplication.scanner.device.domain.ScannerReadEntityType;
import org.radargps.localapplication.scanner.device.domain.ScannerRole;

import java.util.UUID;

public record ScannerConnectionCreateCommand(
        @NotNull
        ScannerRecord firstScanner,

        @NotNull
        ScannerRecord secondScanner,

        @NotNull
        @Enumerated(EnumType.STRING)
        ScannerConnectionType type,
        Integer capacity

) {
        public record ScannerRecord(
                @NotNull
                String uniqueId,

                @NotNull
                @Enumerated(EnumType.STRING)
                ScannerReadEntityType readEntityType
        ) {}
}
