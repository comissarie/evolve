package org.vaargcapital.evolve.repository;

import java.util.UUID;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.vaargcapital.evolve.entity.LoginHistory;

public interface LoginHistoryRepository extends ReactiveCrudRepository<LoginHistory, UUID> {
}
