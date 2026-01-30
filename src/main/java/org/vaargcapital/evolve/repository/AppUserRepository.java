package org.vaargcapital.evolve.repository;

import java.util.UUID;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.vaargcapital.evolve.entity.AppUser;
import reactor.core.publisher.Mono;

public interface AppUserRepository extends ReactiveCrudRepository<AppUser, UUID> {

    Mono<AppUser> findByNickname(String nickname);

    @Query("""
        INSERT INTO app_user (nickname)
        VALUES (:nickname)
        ON CONFLICT (nickname) DO NOTHING
    """)
    Mono<Void> insertIfNotExists(@Param("nickname") String nickname);

    @Query("""
        UPDATE app_user
        SET email = :email
        WHERE nickname = :nickname
    """)
    Mono<Integer> updateEmailByNickname(
            @Param("nickname") String nickname,
            @Param("email") String email
    );

}
