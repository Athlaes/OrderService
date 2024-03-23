package fr.athlaes.services.ord.application.service;

import fr.athlaes.services.ord.application.port.incoming.RateService;
import org.springframework.stereotype.Service;

@Service
public class DomainRateService implements RateService {
    /**
     * This should calculate rate based on market tendency or get information from another service.
     * Here it just return 0.035 as an exemple
     */
    @Override
    public Double getCurrentRate() {
        return 0.035;
    }
}
