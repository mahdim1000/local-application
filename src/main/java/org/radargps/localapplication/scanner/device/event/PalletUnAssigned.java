package org.radargps.localapplication.scanner.device.event;

import org.radargps.localapplication.common.outbox.DomainEvent;

public class PalletUnAssigned extends DomainEvent {
    private String macAddress;
    private String pallet;

    public PalletUnAssigned(String macAddress, String pallet) {
        this.macAddress = macAddress;
        this.pallet = pallet;
    }
}
