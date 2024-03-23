package fr.athlaes.services.ord.application.service;

import fr.athlaes.services.ord.application.port.incoming.AdvisorService;
import fr.athlaes.services.ord.application.port.outgoing.AdvisorPersistence;
import fr.athlaes.services.ord.domain.Advisor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class DomainAdvisorService implements AdvisorService {
    private final AdvisorPersistence repository;
    private final Random rd;

    public DomainAdvisorService(AdvisorPersistence repository) {
        this.repository = repository;
        this.rd = new Random();
    }


    @Override
    public Advisor getRandomAdvisor() {
        List<Advisor> advisors = this.repository.findAll();
        return advisors.get((this.rd.nextInt(1)));
    }
}
