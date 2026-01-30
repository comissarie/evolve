package org.vaargcapital.evolve.entity;

import java.util.UUID;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("ip_address")
public class IpAddress {
    @Id
    private UUID id;
    private String ip;

    public IpAddress() {
    }

    public IpAddress(UUID id, String ip) {
        this.id = id;
        this.ip = ip;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
