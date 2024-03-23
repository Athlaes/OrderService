package fr.athlaes.services.ord.infrastructure.adapter.persistence.repository;

import fr.athlaes.services.ord.application.port.outgoing.AdvisorPersistence;
import fr.athlaes.services.ord.domain.Advisor;
import fr.athlaes.services.ord.infrastructure.adapter.persistence.imports.SQLAdvisorRepository;
import fr.athlaes.services.ord.infrastructure.adapter.persistence.mapping.AdvisorEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AdvisorRepository implements AdvisorPersistence {
    private SQLAdvisorRepository advisorRepository;

    public AdvisorRepository(SQLAdvisorRepository advisorRepository) {
        this.advisorRepository = advisorRepository;
    }

    @Override
    public Optional<Advisor> findById(UUID id) {
        return this.advisorRepository.findById(id).map(AdvisorEntity::toDomain);
    }

    @Override
    public List<Advisor> findAll() {
        return this.advisorRepository.findAll().stream().map(AdvisorEntity::toDomain).toList();
    }

    @Override
    public Advisor save(Advisor advisor) {
        return this.advisorRepository.save(new AdvisorEntity(advisor)).toDomain();
    }

    @Override
    public void delete(Advisor advisor) {
        this.advisorRepository.delete(new AdvisorEntity(advisor));
    }

}
