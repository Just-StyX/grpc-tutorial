package jsl.group.microservices.integration.config;

import io.grpc.ClientInterceptor;
import jsl.group.commons.InventoryServiceGrpc;
import jsl.group.commons.ProductServiceGrpc;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.grpc.client.ChannelBuilderOptions;
import org.springframework.grpc.client.GrpcChannelFactory;
import org.springframework.grpc.client.interceptor.security.BearerTokenAuthenticationInterceptor;
import org.springframework.security.oauth2.client.endpoint.OAuth2ClientCredentialsGrantRequest;
import org.springframework.security.oauth2.client.endpoint.RestClientClientCredentialsTokenResponseClient;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Configuration
public class ChannelConfiguration {
    @Bean
    public ProductServiceGrpc.ProductServiceBlockingStub productChannel(GrpcChannelFactory channelFactory, ClientRegistrationRepository repository) {
        ClientRegistration clientRegistration = repository.findByRegistrationId("openapi-client");
        List<ClientInterceptor> interceptors = List.of(new BearerTokenAuthenticationInterceptor(() -> token(clientRegistration)));
        return ProductServiceGrpc.newBlockingStub(channelFactory.createChannel("product", ChannelBuilderOptions.defaults()
                .withInterceptors(interceptors)));
    }

    @Bean
    public InventoryServiceGrpc.InventoryServiceBlockingStub inventoryChannel(GrpcChannelFactory channelFactory, ClientRegistrationRepository repository) {
        ClientRegistration clientRegistration = repository.findByRegistrationId("openapi-client");
        List<ClientInterceptor> interceptors = List.of(new BearerTokenAuthenticationInterceptor(() -> token(clientRegistration)));
        return InventoryServiceGrpc.newBlockingStub(channelFactory.createChannel("inventory", ChannelBuilderOptions.defaults()
                .withInterceptors(interceptors)));
    }

    @Bean
    public RestTemplate restTemplate() { return new RestTemplate(); }

    private String token(ClientRegistration clientRegistration) {
        RestClientClientCredentialsTokenResponseClient credentialsTokenResponseClient = new RestClientClientCredentialsTokenResponseClient();
        return credentialsTokenResponseClient.getTokenResponse(new OAuth2ClientCredentialsGrantRequest(clientRegistration))
                .getAccessToken().getTokenValue();
    }
}
