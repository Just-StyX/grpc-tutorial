package jsl.group.microservices.core.product.config;

import io.grpc.Metadata;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.grpc.server.GlobalServerInterceptor;
import org.springframework.grpc.server.security.AuthenticationProcessInterceptor;
import org.springframework.grpc.server.security.GrpcSecurity;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;

@Configuration
@Import(AuthenticationConfiguration.class)
public class ProductSecurityConfiguration {
    public static final Metadata.Key<String> USER_KEY = Metadata.Key.of("X-USER", Metadata.ASCII_STRING_MARSHALLER);

    @Bean
    @GlobalServerInterceptor
    public AuthenticationProcessInterceptor authenticationProcessInterceptor(GrpcSecurity grpcSecurity) throws Exception {
        grpcSecurity.authorizeRequests(requests -> {
//                    requests.methods("ProductService/createProduct").authenticated()
//                            .methods("ProductService/getProduct").authenticated()
//                            .methods("ProductService/deleteProduct").authenticated()
//                            .methods("ProductService/updateProduct").authenticated()
//                            .methods("grpc.*/*").permitAll()
//                            .allRequests().denyAll();
                    requests.allRequests().permitAll();
                })
                .oauth2ResourceServer((resourceServer) -> resourceServer.jwt(Customizer.withDefaults()));
        return grpcSecurity.build();
    }
}
