package org.vaargcapital.evolve.entity;

import java.util.UUID;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("user_ip")
public class UserIp {
    @Id
    @Column("user_id")
    private UUID userId;

    @Column("ip_id")
    private UUID ipId;

    public UserIp() {
    }

    public UserIp(UUID userId, UUID ipId) {
        this.userId = userId;
        this.ipId = ipId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public UUID getIpId() {
        return ipId;
    }

    public void setIpId(UUID ipId) {
        this.ipId = ipId;
    }
}
