package fr.athlaes.services.ord;

import fr.athlaes.services.ord.infrastructure.config.ClientConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClients;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@LoadBalancerClients({
		@LoadBalancerClient(name = "finance-service", configuration = ClientConfig.class)
})
@EnableWebMvc
public class OrderServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrderServiceApplication.class, args);
	}

}
