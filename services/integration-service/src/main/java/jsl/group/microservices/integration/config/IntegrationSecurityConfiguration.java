package jsl.group.microservices.integration.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class IntegrationSecurityConfiguration {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(AbstractHttpConfigurer::disable);
        httpSecurity.authorizeHttpRequests(authz -> {
            authz.requestMatchers("/openapi/**").permitAll();
            authz.requestMatchers("/product/openapi/**").permitAll();
            authz.requestMatchers("/webjars/**").permitAll();
            authz.requestMatchers("/openapi/v3/api-docs").permitAll();
            authz.requestMatchers("/eureka/**").permitAll();
            authz.requestMatchers("/config/**").permitAll();
            authz.requestMatchers("/actuator/**", "/oauth2/**", "/token").permitAll();
            authz.anyRequest().authenticated();
        });
        httpSecurity.oauth2ResourceServer(authz -> authz.jwt(Customizer.withDefaults()));
        return httpSecurity.build();
    }
}
