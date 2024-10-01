package org.radargps.localapplication.scanner.device.event;

import org.radargps.localapplication.common.outbox.DomainEvent;

public class PalletUnAssigned extends DomainEvent {
    private final String data;

    public PalletUnAssigned(String macAddress, String palletId) {
        super("unassign-pallet");
        this.data = palletId;
    }

    public String getData() {
        return data;
    }
}
