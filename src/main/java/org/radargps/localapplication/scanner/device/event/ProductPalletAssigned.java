package org.radargps.localapplication.scanner.device.event;

import org.radargps.localapplication.common.outbox.DomainEvent;

public class ProductPalletAssigned extends DomainEvent {
    private String palletId;
    private String productId;

    public ProductPalletAssigned(String palletId, String productId) {
        this.palletId = palletId;
        this.productId = productId;
    }

    public String getPalletId() {
        return palletId;
    }

    public void setPalletId(String palletId) {
        this.palletId = palletId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }
}
