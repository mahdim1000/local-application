package org.radargps.localapplication.scanner.device.event;

import org.radargps.localapplication.common.outbox.DomainEvent;

public class ProductUnAssigned extends DomainEvent {
    private String macAddress;
    String productId;

    public ProductUnAssigned(String macAddress, String productId) {
        this.macAddress = macAddress;
        this.productId = productId;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }
}
