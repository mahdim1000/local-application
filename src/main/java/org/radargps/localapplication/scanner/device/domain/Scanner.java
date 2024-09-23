package org.radargps.localapplication.scanner.device.domain;

import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Table(name = "data_capture_device")
@Entity
public class Scanner {

    @UuidGenerator
    @Id
    private UUID id;
    private String uniqueId;
    private UUID companyId;
    private String ipAddress;

    @Enumerated(EnumType.STRING)
    private ScannerType type;

    @Enumerated(EnumType.STRING)
    private ScannerReadEntityType readEntityType;

    @Enumerated(EnumType.STRING)
    private ScannerRole role;
    private UUID lastDataId;
    private Long lastDataTime;

    public Scanner() {
    }

    public Scanner(String uniqueId, UUID companyId, String ipAddress,
                   ScannerType type, ScannerReadEntityType readEntityType) {
        this.uniqueId = uniqueId;
        this.companyId = companyId;
        this.ipAddress = ipAddress;
        this.type = type;
        this.readEntityType = readEntityType;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public UUID getCompanyId() {
        return companyId;
    }

    public void setCompanyId(UUID companyId) {
        this.companyId = companyId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public ScannerType getType() {
        return type;
    }

    public void setType(ScannerType type) {
        this.type = type;
    }

    public ScannerReadEntityType getReadEntityType() {
        return readEntityType;
    }

    public void setReadEntityType(ScannerReadEntityType readEntityType) {
        this.readEntityType = readEntityType;
    }

    public ScannerRole getRole() {
        return role;
    }

    public void setRole(ScannerRole role) {
        this.role = role;
    }

    public UUID getLastDataId() {
        return lastDataId;
    }

    public void setLastDataId(UUID lastDataId) {
        this.lastDataId = lastDataId;
    }

    public Long getLastDataTime() {
        return lastDataTime;
    }

    public void setLastDataTime(Long lastDataTime) {
        this.lastDataTime = lastDataTime;
    }
}
