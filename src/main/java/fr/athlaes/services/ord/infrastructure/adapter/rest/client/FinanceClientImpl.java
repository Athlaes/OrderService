package fr.athlaes.services.ord.infrastructure.adapter.rest.client;

import fr.athlaes.services.ord.application.port.outgoing.FinanceClient;
import fr.athlaes.services.ord.infrastructure.config.properties.ApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.UUID;

@Service
public class FinanceClientImpl implements FinanceClient {
    RestClient client;
    ApplicationConfiguration config;

    String baseUrl;

    public FinanceClientImpl(RestClient restClient, ApplicationConfiguration config) {
        this.client = restClient;
        this.config = config;
        this.baseUrl = String.format("%s://%s:%s", this.config.getFinanceService().getUrl().getProtocol(),
                this.config.getFinanceService().getUrl().getHost(),
                this.config.getFinanceService().getUrl().getPort());
    }

    @Override
    public boolean verifyDeclaration(UUID clientId, Double amountOverLast3Years) {
        boolean validated = false;
        String endpoint = this.config.getFinanceService().getVerificationEndpoint();
        String result = this.client.get()
                .uri(this.baseUrl + endpoint)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(String.class);
        return "ok".equalsIgnoreCase(result);
    }
}
