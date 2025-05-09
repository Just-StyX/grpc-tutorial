package jsl.group.microservices.integration;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;

import java.util.UUID;

@TestConfiguration
public class IntegrationSecurityTest {
    @Bean
    public ClientRegistrationRepository repository() {
        ClientRegistration clientRegistration = ClientRegistration.withRegistrationId(UUID.randomUUID().toString())
                .clientId("dummy")
                .clientSecret("secret")
                .redirectUri("http://")
                .issuerUri("http://")
                .authorizationUri("http://")
                .tokenUri("http://")
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS).build();
        return new InMemoryClientRegistrationRepository(clientRegistration);
    }
}
