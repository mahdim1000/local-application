package org.radargps.localapplication.scanner.device.event;

import org.radargps.localapplication.common.outbox.DomainEvent;

public class ProductPalletAssigned extends DomainEvent {
    private final DataRecord data;

    public ProductPalletAssigned(String palletId, String productId) {
        super("product-pallet");
        this.data = new DataRecord(productId, palletId);
    }

    public DataRecord getData() {
        return data;
    }

    static class DataRecord {
        String productId;
        String palletId;

        public DataRecord(String productId, String palletId) {
            this.productId = productId;
            this.palletId = palletId;
        }

        public String getProductId() {
            return productId;
        }

        public String getPalletId() {
            return palletId;
        }
    }
}
