package org.radargps.localapplication.data_receiver.domain;

import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Table(name = "data_capture_device")
@Entity
public class DataCaptureDevice {

    @UuidGenerator
    @Id
    private UUID id;
    private String uniqueId;
    private String ipAddress;

    @Enumerated(EnumType.STRING)
    private DeviceType type;

    @Enumerated(EnumType.STRING)
    private DeviceReadEntityType readEntityType;

    @Enumerated(EnumType.STRING)
    private DeviceRole role;
    private UUID lastDataId;
    private Long lastDataTime;

    @OneToOne
    @JoinColumn(name = "connected_device_id")
    private DataCaptureDevice connectedDevice;

    public DataCaptureDevice() {
    }

    public DataCaptureDevice(String uniqueId, String ipAddress,
                             DeviceType type, DeviceReadEntityType readEntityType) {
        this.uniqueId = uniqueId;
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

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public DeviceType getType() {
        return type;
    }

    public void setType(DeviceType type) {
        this.type = type;
    }

    public DeviceReadEntityType getReadEntityType() {
        return readEntityType;
    }

    public void setReadEntityType(DeviceReadEntityType readEntityType) {
        this.readEntityType = readEntityType;
    }

    public DeviceRole getRole() {
        return role;
    }

    public void setRole(DeviceRole role) {
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

    public DataCaptureDevice getConnectedDevice() {
        return connectedDevice;
    }

    public void setConnectedDevice(DataCaptureDevice connectedDevice) {
        this.connectedDevice = connectedDevice;
    }
}
