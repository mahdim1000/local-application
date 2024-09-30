package org.radargps.localapplication.scanner.device.event;

import org.radargps.localapplication.common.outbox.DomainEvent;

public class PalletScannned extends DomainEvent {
    private final DataRecord data;

    public PalletScannned(String macAddress, String palletId) {
        super("pallet-scanner");
        this.data = new DataRecord(macAddress, palletId);
    }

    public DataRecord getData() {
        return data;
    }

    static class DataRecord {
        String macAddress;
        String palletId;

        public DataRecord(String macAddress, String palletId) {
            this.macAddress = macAddress;
            this.palletId = palletId;
        }

        public String getMacAddress() {
            return macAddress;
        }

        public String getPalletId() {
            return palletId;
        }
    }
}
