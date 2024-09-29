package org.radargps.localapplication.scanner.device.event;

import org.radargps.localapplication.common.outbox.DomainEvent;

public class PalletUnAssigned extends DomainEvent {
    private String macAddress;
    private String palletId;

    public PalletUnAssigned(String macAddress, String pallet) {
        this.macAddress = macAddress;
        this.palletId = pallet;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public String getPalletId() {
        return palletId;
    }

    public void setPalletId(String pallet) {
        this.palletId = pallet;
    }
}
