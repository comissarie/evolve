package org.vaargcapital.evolve.repository;

import java.util.UUID;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.vaargcapital.evolve.entity.Squad;

public interface SquadRepository extends ReactiveCrudRepository<Squad, UUID> {
}
