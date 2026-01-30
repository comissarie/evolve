package org.vaargcapital.evolve.entity;

import java.util.UUID;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("squad_user")
public class SquadUser {
    @Id
    @Column("squad_id")
    private UUID squadId;

    @Column("user_id")
    private UUID userId;

    public SquadUser() {
    }

    public SquadUser(UUID squadId, UUID userId) {
        this.squadId = squadId;
        this.userId = userId;
    }

    public UUID getSquadId() {
        return squadId;
    }

    public void setSquadId(UUID squadId) {
        this.squadId = squadId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }
}
