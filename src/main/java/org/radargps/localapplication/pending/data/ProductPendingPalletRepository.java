package org.radargps.localapplication.pending.data;

import org.radargps.localapplication.pending.data.domain.ProductPendingPallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProductPendingPalletRepository extends JpaRepository<ProductPendingPallet, UUID> {
}
