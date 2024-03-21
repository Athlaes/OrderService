package fr.athlaes.services.ord.application.port.outgoing;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import fr.athlaes.services.ord.domain.CreditOrder;
import fr.athlaes.services.ord.domain.CreditOrderStatus;

public interface CreditOrderPersistence {
    Optional<CreditOrder> findById(UUID id);

    List<CreditOrder> findAll();

    List<CreditOrder> findByStatus(CreditOrderStatus creditOrderStatus);

    List<CreditOrder> findByAdvisorIdAndStatus(UUID advisorId, CreditOrderStatus status);

    CreditOrder save(CreditOrder order);

    void delete(CreditOrder order);

    List<CreditOrder> findAllByClientId(UUID id);
}
