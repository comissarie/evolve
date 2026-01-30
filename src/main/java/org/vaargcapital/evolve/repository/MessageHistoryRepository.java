package org.vaargcapital.evolve.repository;

import java.util.UUID;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.vaargcapital.evolve.entity.MessageHistory;

public interface MessageHistoryRepository extends ReactiveCrudRepository<MessageHistory, UUID> {
}
