package org.radargps.localapplication.scanner.connection.temp;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProductPalletScannerConnectionRepository extends JpaRepository<ProductPalletScannerConnection, UUID> {
}
