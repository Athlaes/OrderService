package fr.athlaes.services.ord.application.port.outgoing;

import fr.athlaes.services.ord.domain.StatusHistory;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface StatusHistoryPersistence {
    Optional<StatusHistory> findById(UUID id);

    List<StatusHistory> findAll();

    StatusHistory save(StatusHistory order);

    void delete(StatusHistory order);

}
