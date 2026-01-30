package org.vaargcapital.evolve.repository;

import java.util.UUID;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.vaargcapital.evolve.entity.UserIp;
import reactor.core.publisher.Mono;

public interface UserIpRepository extends ReactiveCrudRepository<UserIp, UUID> {

    @Query("""
        INSERT INTO user_ip (user_id, ip_id)
        VALUES (:userId, :ipId)
        ON CONFLICT (user_id, ip_id) DO NOTHING
    """)
    Mono<Void> insertUserIpIfNotExists(
            @Param("userId") UUID userId,
            @Param("ipId") UUID ipId
    );

}
