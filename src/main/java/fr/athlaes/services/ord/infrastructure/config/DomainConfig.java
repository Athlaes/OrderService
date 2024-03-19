package fr.athlaes.services.ord.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.web.client.RestClient;

import fr.athlaes.services.ord.application.ddd.DomainService;

@Configuration
@ComponentScan(
        basePackages = "fr.athlaes.services.ord.application.service",
        includeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, value = DomainService.class)
)
public class DomainConfig {
    @Bean
    RestClient restClient() {
        return RestClient.create();
    }
}
