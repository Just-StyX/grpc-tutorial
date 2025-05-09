package jsl.group.checkout_service.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.OAuthFlow;
import io.swagger.v3.oas.annotations.security.OAuthFlows;
import io.swagger.v3.oas.annotations.security.OAuthScope;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import jsl.group.commons.InventoryServiceGrpc;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.grpc.client.GrpcChannelFactory;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
@SecurityScheme(
        name = "security_authentication",
        type = SecuritySchemeType.OAUTH2,
        flows = @OAuthFlows(
                authorizationCode = @OAuthFlow(
                        authorizationUrl = "http://localhost:9092/realms/gateway/protocol/openid-connect/auth",
                        tokenUrl = "http://localhost:9092/realms/gateway/protocol/openid-connect/token",
                        scopes = {
                                @OAuthScope(name = "address", description = "address scope"),
                                @OAuthScope(name = "email", description = "email scope")
                        }
                )
        )
)
public class CheckoutClientConfiguration {
    @Bean
    public InventoryServiceGrpc.InventoryServiceBlockingStub inventoryServiceBlockingStub(GrpcChannelFactory channelFactory) {
        return InventoryServiceGrpc.newBlockingStub(channelFactory.createChannel("inventory"));
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity httpSecurity) {
        httpSecurity.csrf(ServerHttpSecurity.CsrfSpec::disable);
        httpSecurity.authorizeExchange(authz -> {
            authz.pathMatchers("/openapi/**").permitAll();
            authz.pathMatchers("/checkout/openapi/**").permitAll();
            authz.pathMatchers("/webjars/**").permitAll();
            authz.pathMatchers("/openapi/v3/api-docs").permitAll();
            authz.pathMatchers("/eureka/**").permitAll();
            authz.pathMatchers("/config/**").permitAll();
            authz.pathMatchers("/actuator/**", "/oauth2/**", "/token").permitAll();
            authz.anyExchange().authenticated();
        });
        httpSecurity.oauth2ResourceServer(authz -> authz.jwt(Customizer.withDefaults()));
        return httpSecurity.build();
    }
}
