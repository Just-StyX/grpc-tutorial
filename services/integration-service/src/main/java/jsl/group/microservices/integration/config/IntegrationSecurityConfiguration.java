package jsl.group.microservices.integration.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class IntegrationSecurityConfiguration {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.oauth2Client(Customizer.withDefaults());
        httpSecurity.authorizeHttpRequests(authz -> {
            authz.requestMatchers("/openapi/**").permitAll();
            authz.anyRequest().permitAll();
        });
        return httpSecurity.build();
    }
}
