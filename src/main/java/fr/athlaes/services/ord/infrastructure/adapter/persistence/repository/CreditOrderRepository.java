package fr.athlaes.services.ord.infrastructure.adapter.persistence.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import fr.athlaes.services.ord.application.port.outgoing.CreditOrderPersistence;
import fr.athlaes.services.ord.domain.CreditOrder;
import fr.athlaes.services.ord.domain.CreditOrderStatus;
import fr.athlaes.services.ord.infrastructure.adapter.persistence.imports.SQLCreditOrderRepository;
import fr.athlaes.services.ord.infrastructure.adapter.persistence.mapping.CreditOrderEntity;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CreditOrderRepository implements CreditOrderPersistence {
    private final SQLCreditOrderRepository repository;

    public CreditOrderRepository(SQLCreditOrderRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<CreditOrder> findById(UUID id) {
        return repository.findById(id).map(CreditOrderEntity::toDomain);
    }
    

    @Override
    public List<CreditOrder> findAll() {
        return repository.findAll().stream()
                .map(CreditOrderEntity::toDomain)
                .toList();
    }

    @Override
    @Transactional
    public CreditOrder save(CreditOrder order) {
        return repository.save(new CreditOrderEntity(order)).toDomain();
    }

    @Override
    public void delete(CreditOrder order) {
        repository.delete(new CreditOrderEntity(order));
    }

    @Override
    public List<CreditOrder> findByStatus(CreditOrderStatus creditOrderStatus) {
        return repository.findByStatus(creditOrderStatus).stream()
                .map(CreditOrderEntity::toDomain)
                .toList();
    }

    @Override
    public List<CreditOrder> findByAdvisorIdAndStatus(UUID advisorId, CreditOrderStatus status) {
        return repository.findByAdvisor_MatriculeAndStatus(advisorId, status).stream()
                .map(CreditOrderEntity::toDomain)
                .toList();
    }
    
}
