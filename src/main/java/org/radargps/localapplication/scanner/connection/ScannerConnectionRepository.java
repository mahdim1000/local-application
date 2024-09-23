package org.radargps.localapplication.scanner.connection;

import org.radargps.localapplication.scanner.connection.domain.ScannerConnection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ScannerConnectionRepository extends JpaRepository<UUID, ScannerConnection> {
}
