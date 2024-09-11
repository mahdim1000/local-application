package org.radargps.localapplication.data_receiver.domain;

import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Table(name = "capture_data")
@Entity
public class Data {

    @UuidGenerator
    @Id
    private UUID id;
    private UUID deviceId;
    private String uniqueId;
    private Long lastServerTime;
    private String data;

    public Data() {
    }

    public Data(UUID deviceId, String uniqueId, Long lastServerTime, String data) {
        this.deviceId = deviceId;
        this.uniqueId = uniqueId;
        this.lastServerTime = lastServerTime;
        this.data = data;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(UUID deviceId) {
        this.deviceId = deviceId;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public Long getLastServerTime() {
        return lastServerTime;
    }

    public void setLastServerTime(Long lastServerTime) {
        this.lastServerTime = lastServerTime;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
