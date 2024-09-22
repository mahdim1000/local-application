package org.radargps.localapplication.data_receiver.event;

import org.radargps.localapplication.common.outbox.DomainEvent;

public class ProductScanned extends DomainEvent {
    String macAddress;
    String product;

    public ProductScanned(String macAddress, String product) {
        this.macAddress = macAddress;
        this.product = product;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public String getProduct() {
        return product;
    }

}
