package fr.athlaes.services.ord.application.port.incoming;

import fr.athlaes.services.ord.domain.Advisor;

public interface AdvisorService {
    Advisor getRandomAdvisor();
}
