package org.radargps.localapplication.scanner.device.event;

import org.radargps.localapplication.common.outbox.DomainEvent;

public class ProductPalletAssigned extends DomainEvent {
    private String palletId;
    private String productId;

    public ProductPalletAssigned(String palletId, String productId) {
        this.palletId = palletId;
        this.productId = productId;
    }
}
