package jsl.group.microservices.integration.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.OAuthFlow;
import io.swagger.v3.oas.annotations.security.OAuthFlows;
import io.swagger.v3.oas.annotations.security.OAuthScope;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.servers.Server;
import jsl.group.commons.InventoryServiceGrpc;
import jsl.group.commons.ProductServiceGrpc;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.grpc.client.GrpcChannelFactory;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Configuration
@SecurityScheme(
        name = "security_authentication", type = SecuritySchemeType.OAUTH2,
        flows = @OAuthFlows(
                authorizationCode = @OAuthFlow(
                        authorizationUrl = "http://localhost:9092/realms/gateway/protocol/openid-connect/auth",
                        tokenUrl = "http://localhost:9092/realms/gateway/protocol/openid-connect/token",
                        scopes = {
                                @OAuthScope(name = "product:read", description = "read scope"),
                                @OAuthScope(name = "product:write", description = "write scope")
                        }
                )
        )
)
public class ChannelConfiguration {
    @Bean
    public ProductServiceGrpc.ProductServiceBlockingStub productChannel(GrpcChannelFactory channelFactory) {
        return ProductServiceGrpc.newBlockingStub(channelFactory.createChannel("product"));
    }

    @Bean
    public InventoryServiceGrpc.InventoryServiceBlockingStub inventoryChannel(GrpcChannelFactory channelFactory) {
        return InventoryServiceGrpc.newBlockingStub(channelFactory.createChannel("inventory"));
    }

    @Bean
    public RestTemplate restTemplate() { return new RestTemplate(); }

    @Bean
    public OpenAPI getOpenApiDocumentation() {
        return new OpenAPI()
                .info(new Info().title("apiTitle")
                        .description("apiDescription")
                        .version("apiVersion")
                        .contact(new Contact()
                                .name("apiContactName")
                                .url("apiContactUrl")
                                .email("apiContactEmail"))
                        .termsOfService("apiTermsOfService")
                        .license(new License()
                                .name("apiLicense")
                                .url("apiLicenseUrl")))
                .externalDocs(new ExternalDocumentation()
                        .description("apiExternalDocDesc")
                        .url("apiExternalDocUrl"))
                .servers(List.of(new Server().description("serverOneDescription").url("http://localhost:8080"), new Server().description("serverTwoDescription").url("serverTwoUrl")))
                .security(List.of(new SecurityRequirement().addList("securityName")));
    }
}
