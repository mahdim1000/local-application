package org.radargps.localapplication.scanner.connection.domain;

import jakarta.persistence.*;
import org.radargps.localapplication.scanner.device.domain.Scanner;
import org.radargps.localapplication.scanner.device.domain.ScannerReadEntityType;

import java.util.UUID;

@Table(name = "scanner_connection")
@Entity
public class ScannerConnection {

    @Id
    private UUID id;
    @OneToOne
    private Scanner firstScanner;
    @OneToOne
    private Scanner secondScanner;

    @Column(columnDefinition = "VARCHAR(50)")
    @Enumerated(EnumType.STRING)
    private ScannerConnectionType type;
    private Integer capacity;
    private int currentAssignedCount;
    private UUID companyId;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Scanner getFirstScanner() {
        return firstScanner;
    }

    public void setFirstScanner(Scanner firstScanner) {
        this.firstScanner = firstScanner;
    }

    public Scanner getSecondScanner() {
        return secondScanner;
    }

    public void setSecondScanner(Scanner secondScanner) {
        this.secondScanner = secondScanner;
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

    public int getCurrentAssignedCount() {
        return currentAssignedCount;
    }

    public void setCurrentAssignedCount(int currentAssignedCount) {
        this.currentAssignedCount = currentAssignedCount;
    }

    public UUID getCompanyId() {
        return companyId;
    }

    public void setCompanyId(UUID companyId) {
        this.companyId = companyId;
    }
}
