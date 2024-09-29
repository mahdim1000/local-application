package org.radargps.localapplication.captured.data.domain;

import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Table(name = "capture_data")
@Entity
public class Data {

    @UuidGenerator
    @Id
    private UUID id;
    private String uniqueId;
    private Long serverTime;
    private String data;

    public Data() {
    }

    public Data(String uniqueId, Long serverTime, String data) {
        this.uniqueId = uniqueId;
        this.serverTime = serverTime;
        this.data = data;
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

    public Long getServerTime() {
        return serverTime;
    }

    public void setServerTime(Long serverTime) {
        this.serverTime = serverTime;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
