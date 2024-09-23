package org.radargps.localapplication.scanner.connection.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Table(name = "scanner_connection")
@Entity
public class ScannerConnection {

    @Id
    private UUID id;
    private UUID firstScannerId;
    private UUID secondScannerId;
    private ScannerConnectionType type;
    private Integer capacity;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getFirstScannerId() {
        return firstScannerId;
    }

    public void setFirstScannerId(UUID firstScannerId) {
        this.firstScannerId = firstScannerId;
    }

    public UUID getSecondScannerId() {
        return secondScannerId;
    }

    public void setSecondScannerId(UUID secondScannerId) {
        this.secondScannerId = secondScannerId;
    }

    public ScannerConnectionType getType() {
        return type;
    }

    public void setType(ScannerConnectionType type) {
        this.type = type;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }
}
