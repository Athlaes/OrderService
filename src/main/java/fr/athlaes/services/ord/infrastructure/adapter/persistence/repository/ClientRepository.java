package fr.athlaes.services.ord.infrastructure.adapter.persistence.repository;

import fr.athlaes.services.ord.application.port.outgoing.ClientPersistence;
import fr.athlaes.services.ord.domain.Client;
import fr.athlaes.services.ord.infrastructure.adapter.persistence.imports.SQLClientRepository;
import fr.athlaes.services.ord.infrastructure.adapter.persistence.mapping.ClientEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ClientRepository implements ClientPersistence {
    SQLClientRepository clientRepository;
    public ClientRepository(SQLClientRepository repository) {
        this.clientRepository = repository;
    }
    @Override
    public Optional<Client> findById(UUID id) {
        return this.clientRepository.findById(id).map(ClientEntity::toDomain);
    }

    @Override
    public List<Client> findAll() {
        return this.clientRepository.findAll().stream().map(ClientEntity::toDomain).toList();
    }

    @Override
    public Client save(Client client) {
        return this.clientRepository.save(new ClientEntity(client)).toDomain();
    }

    @Override
    public void delete(Client client) {
        this.clientRepository.delete(new ClientEntity(client));
    }
}
