package fr.athlaes.services.ord.infrastructure.adapter.persistence.imports;

import java.util.UUID;

import fr.athlaes.services.ord.infrastructure.adapter.persistence.mapping.StatusHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SQLStatusHistoryRepository extends JpaRepository<StatusHistoryEntity, UUID> {

}
