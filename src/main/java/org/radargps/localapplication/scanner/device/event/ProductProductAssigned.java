package org.radargps.localapplication.scanner.device.event;

import org.radargps.localapplication.common.outbox.DomainEvent;

import java.util.UUID;

public class ProductProductAssigned extends DomainEvent {
    private final DataRecord data;

    public ProductProductAssigned(String productLink, String productId) {
        super("product-product");
        this.data = new DataRecord(productId, productLink);
    }

    public DataRecord getData() {
        return data;
    }

    static class DataRecord {
        String productId;
        String qrcode;

        public DataRecord(String productId, String productLink) {
            this.productId = productId;
            this.qrcode = productLink;
        }

        public String getProductId() {
            return productId;
        }

        public String getQrcode() {
            return qrcode;
        }
    }
}
