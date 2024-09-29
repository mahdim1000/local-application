package org.radargps.localapplication.scanner.device.event;

import org.radargps.localapplication.common.outbox.DomainEvent;

public class ProductScanned extends DomainEvent {
    String macAddress;
    String productId;

    public ProductScanned(String macAddress, String productId) {
        this.macAddress = macAddress;
        this.productId = productId;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public String getProductId() {
        return productId;
    }

}
