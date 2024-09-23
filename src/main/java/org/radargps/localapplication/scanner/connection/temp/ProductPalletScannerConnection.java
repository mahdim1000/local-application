package org.radargps.localapplication.scanner.connection.temp;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Table(name = "product-pallet-scanner-connection")
@Entity
public class ProductPalletScannerConnection {

    @Id
    private UUID id;
    private UUID productScannerId;
    private UUID palletScannerId;
    private Integer capacity;
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

    public UUID getPalletScannerId() {
        return palletScannerId;
    }

    public void setPalletScannerId(UUID palletScannerId) {
        this.palletScannerId = palletScannerId;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public UUID getCompanyId() {
        return companyId;
    }

    public void setCompanyId(UUID companyId) {
        this.companyId = companyId;
    }
}
