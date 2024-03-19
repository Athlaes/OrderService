package fr.athlaes.services.ord.infrastructure.adapter.persistence.imports;

import fr.athlaes.services.ord.infrastructure.adapter.persistence.mapping.AdvisorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SQLAdvisorRepository extends JpaRepository<AdvisorEntity, UUID> {

}
