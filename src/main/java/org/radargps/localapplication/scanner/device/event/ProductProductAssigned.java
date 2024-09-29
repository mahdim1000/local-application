package org.radargps.localapplication.scanner.device.event;

import org.radargps.localapplication.common.outbox.DomainEvent;

import java.util.UUID;

public class ProductProductAssigned extends DomainEvent {
    private String productLink;
    private String productId;

    public ProductProductAssigned(String productLink, String productId) {
        this.productLink = productLink;
        this.productId = productId;
    }

    public String getProductLink() {
        return productLink;
    }

    public void setProductLink(String productLink) {
        this.productLink = productLink;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }
}
