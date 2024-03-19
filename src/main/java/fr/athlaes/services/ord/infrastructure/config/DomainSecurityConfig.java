package fr.athlaes.services.ord.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class DomainSecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests(authorize -> authorize
                        .anyRequest().permitAll()
                )
                //.oauth2ResourceServer(oauth2 -> oauth2
                //        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthConverter)))
                //.sessionManagement(session -> session
                //        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .cors(withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .build();
    }

}
