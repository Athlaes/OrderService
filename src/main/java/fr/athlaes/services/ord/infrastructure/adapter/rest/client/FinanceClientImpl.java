package fr.athlaes.services.ord.infrastructure.adapter.rest.client;

import fr.athlaes.services.ord.application.port.outgoing.FinanceClient;
import fr.athlaes.services.ord.infrastructure.config.properties.ApplicationConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.UUID;

@Service
public class FinanceClientImpl implements FinanceClient {
    private final Logger logger = LoggerFactory.getLogger(FinanceClientImpl.class);

    private final RestClient client;
    private final ApplicationConfiguration config;

    private final String baseUrl;

    public FinanceClientImpl(RestClient restClient, ApplicationConfiguration config) {
        this.client = restClient;
        this.config = config;
        this.baseUrl = String.format("%s://%s:%s", this.config.getFinanceService().getUrl().getProtocol(),
                this.config.getFinanceService().getUrl().getHost(),
                this.config.getFinanceService().getUrl().getPort());
    }

    @Override
    public boolean verifyDeclaration(UUID clientId, Double amountOverLast3Years) {
        String endpoint = this.config.getFinanceService().getVerificationEndpoint();
        String result = this.client.get()
                .uri(this.baseUrl + endpoint, clientId, amountOverLast3Years)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(String.class);
        logger.info("successfully verified declaration for client {}", clientId);
        return "ok".equalsIgnoreCase(result);
    }
}
