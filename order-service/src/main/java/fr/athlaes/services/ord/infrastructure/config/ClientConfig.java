package fr.athlaes.services.ord.infrastructure.config;

import org.springframework.cloud.loadbalancer.core.RoundRobinLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

public class ClientConfig {
    @Bean
    public RoundRobinLoadBalancer roundRobinContextLoadBalancer(LoadBalancerClientFactory clientFactory,
                                                                Environment env) {
        String serviceId = LoadBalancerClientFactory.getName(env);
        return new RoundRobinLoadBalancer(clientFactory.getLazyProvider(serviceId, ServiceInstanceListSupplier.class),
                serviceId, -1);
    }
}
