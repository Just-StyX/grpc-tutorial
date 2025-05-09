package jsl.group.microservices.cloud.gateway_service;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.client.registration.*;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;

import java.util.UUID;

@TestConfiguration
public class GatewayTestConfiguration {
    @Bean
    public NimbusReactiveJwtDecoder jwtDecoder() {
        return NimbusReactiveJwtDecoder.withJwkSetUri("http://keycloak:9092/realms/gateway/protocol/openid-connect/certs").build();
    }

    @Bean
    public ReactiveClientRegistrationRepository repository() {
        ClientRegistration clientRegistration = ClientRegistration.withRegistrationId(UUID.randomUUID().toString())
                .clientId("dummy")
                .clientSecret("secret")
                .redirectUri("http://")
                .issuerUri("http://")
                .authorizationUri("http://")
                .tokenUri("http://")
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS).build();
        return new InMemoryReactiveClientRegistrationRepository(clientRegistration);
    }
}
