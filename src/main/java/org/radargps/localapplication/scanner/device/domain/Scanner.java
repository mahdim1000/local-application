package org.radargps.localapplication.scanner.device.domain;

import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.Instant;
import java.util.UUID;

@Table(name = "scanner")
@Entity
public class Scanner {

    @Id
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
    private String lastDataValue;
    private Long lastDataTime;
    @Column(updatable = false)
    private Long createdAt;
    private Long updatedAt;

    public Scanner() {
    }

    public Scanner(String uniqueId, UUID companyId, String ipAddress,
                   ScannerType type, ScannerReadEntityType readEntityType, ScannerRole role) {
        this.uniqueId = uniqueId;
        this.companyId = companyId;
        this.ipAddress = ipAddress;
        this.type = type;
        this.readEntityType = readEntityType;
        this.role = role;
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

    public String getLastDataValue() {
        return lastDataValue;
    }

    public void setLastDataValue(String lastDataValue) {
        this.lastDataValue = lastDataValue;
    }

    public Long getLastDataTime() {
        return lastDataTime;
    }

    public void setLastDataTime(Long lastDataTime) {
        this.lastDataTime = lastDataTime;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Long updatedAt) {
        this.updatedAt = updatedAt;
    }
}
