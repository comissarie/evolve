package org.vaargcapital.evolve.repository;

import java.util.UUID;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.vaargcapital.evolve.entity.IpAddress;
import reactor.core.publisher.Mono;

public interface IpAddressRepository extends ReactiveCrudRepository<IpAddress, UUID> {

    Mono<IpAddress> findByIp(String ip);

    @Modifying
    @Query("INSERT INTO ip_address (ip) VALUES (:ip) ON CONFLICT (ip) DO NOTHING")
    Mono<Void> insertIfNotExists(String ip);
}
