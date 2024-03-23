package fr.athlaes.services.ord.application.port.outgoing;

import fr.athlaes.services.ord.domain.Advisor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AdvisorPersistence {
    Optional<Advisor> findById(UUID id);

    List<Advisor> findAll();

    Advisor save(Advisor advisor);

    void delete(Advisor advisor);
}
