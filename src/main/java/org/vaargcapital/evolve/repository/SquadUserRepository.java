package org.vaargcapital.evolve.repository;

import java.util.UUID;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.vaargcapital.evolve.entity.SquadUser;
import reactor.core.publisher.Mono;

public interface SquadUserRepository extends ReactiveCrudRepository<SquadUser, UUID> {

    @Query("""
        INSERT INTO squad_user (squad_id, user_id)
        VALUES (:squadId, :userId)
        ON CONFLICT (squad_id, user_id) DO NOTHING
    """)
    Mono<Void> insertSquadUserIfNotExists(
            @Param("squadId") UUID squadId,
            @Param("userId") UUID userId
    );

}
