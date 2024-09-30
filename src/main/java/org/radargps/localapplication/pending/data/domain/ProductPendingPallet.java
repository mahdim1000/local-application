package org.radargps.localapplication.pending.data.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;

@Table(name = "product_pending_pallet")
@Entity
public class ProductPendingPallet {

    @Id
    private UUID id;
    private String productScanner;
    private String palletScanner;
    private String productData;
    private Long time;
    private UUID companyId;

    public ProductPendingPallet(String productScanner,
                                String palletScanner,
                                String productData,
                                UUID companyId) {
        this.productScanner = productScanner;
        this.palletScanner = palletScanner;
        this.productData = productData;
        this.companyId = companyId;
        this.id = UUID.randomUUID();
        this.time = Instant.now().getEpochSecond();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getProductScanner() {
        return productScanner;
    }

    public void setProductScanner(String productScanner) {
        this.productScanner = productScanner;
    }

    public String getPalletScanner() {
        return palletScanner;
    }

    public void setPalletScanner(String palletScanner) {
        this.palletScanner = palletScanner;
    }

    public String getProductData() {
        return productData;
    }

    public void setProductData(String productData) {
        this.productData = productData;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public UUID getCompanyId() {
        return companyId;
    }

    public void setCompanyId(UUID companyId) {
        this.companyId = companyId;
    }
}
