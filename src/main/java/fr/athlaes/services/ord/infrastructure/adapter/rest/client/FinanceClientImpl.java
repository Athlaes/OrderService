package fr.athlaes.services.ord.infrastructure.adapter.rest.client;

import fr.athlaes.services.ord.application.port.outgoing.FinanceClient;
import fr.athlaes.services.ord.application.service.exceptions.ServiceNotAccesibleException;
import fr.athlaes.services.ord.infrastructure.config.properties.ApplicationConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.loadbalancer.core.RoundRobinLoadBalancer;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.UUID;

@Service
public class FinanceClientImpl implements FinanceClient {
    private final Logger logger = LoggerFactory.getLogger(FinanceClientImpl.class);
    private final RestClient client;
    private final ApplicationConfiguration config;
    private final LoadBalancerClientFactory clientFactory;

    public FinanceClientImpl(RestClient restClient, ApplicationConfiguration config, LoadBalancerClientFactory lbcf) {
        this.client = restClient;
        this.config = config;
        this.clientFactory = lbcf;
    }

    @Override
    public boolean verifyDeclaration(UUID clientId, Double amountOverLast3Years) {
        String endpoint = this.config.getFinanceService().getVerificationEndpoint();
        RoundRobinLoadBalancer lb = clientFactory.getInstance("finance-service", RoundRobinLoadBalancer.class);
        ServiceInstance instance = lb.choose().block().getServer();

        String baseUrl = String.format("%s://%s:%s", "http", instance.getHost(), instance.getPort());
        String result = this.client.get()
                .uri(baseUrl + endpoint, clientId, amountOverLast3Years)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(String.class);

        logger.info("successfully verified declaration for client {}", clientId);
        return "ok".equalsIgnoreCase(result);
    }
}
