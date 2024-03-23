package fr.athlaes.services.ord.application.port.outgoing;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import fr.athlaes.services.ord.domain.Client;

public interface ClientPersistence {
    Optional<Client> findById(UUID id);

    List<Client> findAll();

    Client save(Client client);

    void delete(Client client);

}
