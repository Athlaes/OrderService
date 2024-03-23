package fr.athlaes.services.ord.infrastructure.adapter.persistence.imports;

import java.util.List;
import java.util.UUID;

import fr.athlaes.services.ord.infrastructure.adapter.persistence.mapping.CreditOrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import fr.athlaes.services.ord.domain.CreditOrderStatus;

public interface SQLCreditOrderRepository extends JpaRepository<CreditOrderEntity, UUID> {
    List<CreditOrderEntity> findByStatus(CreditOrderStatus creditOrderStatus);

    List<CreditOrderEntity> findByAdvisor_MatriculeAndStatus(UUID advisorId, CreditOrderStatus status);

    List<CreditOrderEntity> findAllByClientId(UUID id);
}
