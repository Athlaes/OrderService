package fr.athlaes.services.ord.infrastructure.adapter.persistence.repository;

import fr.athlaes.services.ord.application.port.outgoing.StatusHistoryPersistence;
import fr.athlaes.services.ord.domain.StatusHistory;
import fr.athlaes.services.ord.infrastructure.adapter.persistence.imports.SQLStatusHistoryRepository;
import fr.athlaes.services.ord.infrastructure.adapter.persistence.mapping.StatusHistoryEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class StatusHistoryRepository implements StatusHistoryPersistence {
    SQLStatusHistoryRepository repository;

    public StatusHistoryRepository(SQLStatusHistoryRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<StatusHistory> findById(UUID id) {
        return this.repository.findById(id).map(StatusHistoryEntity::toDomain);
    }

    @Override
    public List<StatusHistory> findAll() {
        return this.repository.findAll().stream()
                .map(StatusHistoryEntity::toDomain)
                .toList();
    }

    @Override
    public StatusHistory save(StatusHistory statusHistory) {
        return this.repository.save(new StatusHistoryEntity(statusHistory)).toDomain();
    }

    @Override
    public void delete(StatusHistory statusHistory) {
        this.repository.delete(new StatusHistoryEntity(statusHistory));
    }
}
