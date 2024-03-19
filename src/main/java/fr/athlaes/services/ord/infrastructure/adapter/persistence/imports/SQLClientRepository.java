package fr.athlaes.services.ord.infrastructure.adapter.persistence.imports;

import fr.athlaes.services.ord.infrastructure.adapter.persistence.mapping.ClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SQLClientRepository extends JpaRepository<ClientEntity, UUID> {
}
