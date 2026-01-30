package org.vaargcapital.evolve.repository;

import java.util.UUID;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.vaargcapital.evolve.entity.AppUser;
import reactor.core.publisher.Mono;

public interface AppUserRepository extends ReactiveCrudRepository<AppUser, UUID> {

    Mono<AppUser> findByNickname(String nickname);

    @Modifying
    @Query("INSERT INTO app_user (nickname) VALUES (:nickname) ON CONFLICT (nickname) DO NOTHING")
    Mono<Void> insertIfNotExists(String nickname);

    @Modifying
    @Query("UPDATE app_user SET email = :email WHERE nickname = :nickname")
    Mono<Integer> updateEmailByNickname(String nickname, String email);
}
