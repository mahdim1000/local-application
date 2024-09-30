package org.radargps.localapplication.scanner.device.event;

import org.radargps.localapplication.common.outbox.DomainEvent;

public class ProductUnAssigned extends DomainEvent {
    private final DataRecord data;

    public ProductUnAssigned(String macAddress, String productId) {
        super("unassign-product");
        this.data = new DataRecord(macAddress, productId);
    }

    public DataRecord getData() {
        return data;
    }

    static class DataRecord {
        String macAddress;
        String productId;

        public DataRecord(String macAddress, String productId) {
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
}
