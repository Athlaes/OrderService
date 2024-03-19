package fr.athlaes.services.ord.application.service;

import fr.athlaes.services.ord.application.port.incoming.AdvisorService;
import fr.athlaes.services.ord.application.port.outgoing.AdvisorPersistence;
import fr.athlaes.services.ord.domain.Advisor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DomainAdvisorService implements AdvisorService {

    private AdvisorPersistence repository;

    @Override
    public Advisor getRandomAdvisor() {
        List<Advisor> advisors = this.repository.findAll();
        return advisors.get((int) (Math.random() * advisors.size()-1));
    }
}
