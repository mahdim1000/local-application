package org.radargps.localapplication.data_receiver.event;

import org.radargps.localapplication.common.outbox.DomainEvent;

public class ProductPalletAssigned extends DomainEvent {
    private String macAddress;
    private String palletId;
    private String productId;

    public ProductPalletAssigned(String macAddress, String palletId, String productId) {
        this.macAddress = macAddress;
        this.palletId = palletId;
        this.productId = productId;
    }
}
