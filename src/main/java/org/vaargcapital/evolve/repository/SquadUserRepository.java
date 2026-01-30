package org.vaargcapital.evolve.repository;

import java.util.UUID;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.vaargcapital.evolve.entity.SquadUser;
import reactor.core.publisher.Mono;

public interface SquadUserRepository extends ReactiveCrudRepository<SquadUser, UUID> {

    @Modifying
    @Query("INSERT INTO squad_user (squad_id, user_id) VALUES (:squadId, :userId) ON CONFLICT DO NOTHING")
    Mono<Void> insertIfNotExists(UUID squadId, UUID userId);
}
