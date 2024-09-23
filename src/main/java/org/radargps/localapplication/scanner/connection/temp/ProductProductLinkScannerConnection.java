package org.radargps.localapplication.scanner.connection.temp;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Table(name = "product-pallet-scanner-connection")
@Entity
public class ProductProductLinkScannerConnection {

    @Id
    private UUID id;
    private UUID productScannerId;
    private UUID productLinkScannerId;
    private UUID companyId;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getProductScannerId() {
        return productScannerId;
    }

    public void setProductScannerId(UUID productScannerId) {
        this.productScannerId = productScannerId;
    }

    public UUID getProductLinkScannerId() {
        return productLinkScannerId;
    }

    public void setProductLinkScannerId(UUID productLinkScannerId) {
        this.productLinkScannerId = productLinkScannerId;
    }

    public UUID getCompanyId() {
        return companyId;
    }

    public void setCompanyId(UUID companyId) {
        this.companyId = companyId;
    }
}
