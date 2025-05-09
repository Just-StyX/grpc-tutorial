package jsl.group.microservices.cloud.gateway_service.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableWebFluxSecurity
public class GatewayConfiguration {
    @Bean
    @LoadBalanced
    public WebClient.Builder loadBalancedWebClientBuilder() { return WebClient.builder(); }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity httpSecurity) {
        httpSecurity.cors(corsSpec -> corsSpec.configurationSource(corsConfigurationSource()));
        httpSecurity.csrf(ServerHttpSecurity.CsrfSpec::disable);
        httpSecurity.authorizeExchange(authz -> {
            authz.pathMatchers("/openapi/**").permitAll();
            authz.pathMatchers("/product/openapi/**").permitAll();
            authz.pathMatchers("/checkout/openapi/**").permitAll();
            authz.pathMatchers("/webjars/**").permitAll();
            authz.pathMatchers("/openapi/v3/api-docs").permitAll();
            authz.pathMatchers("/eureka/**").permitAll();
            authz.pathMatchers("/config/**").permitAll();
            authz.pathMatchers("/actuator/**", "/oauth2/**", "/token").permitAll();
            authz.anyExchange().authenticated();
        });
        httpSecurity.oauth2Client(Customizer.withDefaults());
        httpSecurity.oauth2Login(Customizer.withDefaults());
        httpSecurity.oauth2ResourceServer(authz -> {
            authz.jwt(Customizer.withDefaults())
                    .jwt(token -> token.jwtAuthenticationConverter(KeycloakJwtAuthenticationConverter.getInstance()));
        });
        return httpSecurity.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setAllowedOrigins(Collections.singletonList("http://localhost:4200"));
        corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH"));
        corsConfiguration.setAllowedHeaders(Arrays.asList(HttpHeaders.AUTHORIZATION, HttpHeaders.ORIGIN, HttpHeaders.CONTENT_TYPE, HttpHeaders.ACCEPT));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }

    @Bean
    public CorsWebFilter corsWebFilter() { return new CorsWebFilter(corsConfigurationSource()); }

//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
//        httpSecurity.csrf(AbstractHttpConfigurer::disable);
//        httpSecurity.authorizeHttpRequests(authz -> {
//            authz.requestMatchers("/openapi/**").permitAll();
//            authz.requestMatchers("/webjars/**").permitAll();
//            authz.requestMatchers("/openapi/v3/api-docs").permitAll();
//            authz.requestMatchers("/eureka/**").permitAll();
//            authz.requestMatchers("/config/**").permitAll();
//            authz.requestMatchers("/actuator/**", "/oauth2/**", "/token").permitAll();
//            authz.anyRequest().authenticated();
//        });
//        httpSecurity.oauth2Client(Customizer.withDefaults());
//        httpSecurity.oauth2Login(Customizer.withDefaults());
//        httpSecurity.oauth2ResourceServer(authz -> {
//            authz.jwt(token -> token.jwtAuthenticationConverter(KeycloakJwtAuthenticationConverter.getInstance()));
//        });
//        return httpSecurity.build();
//    }
//
//    @Bean
//    public CorsFilter corsFilter() {
//        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        final CorsConfiguration corsConfiguration = new CorsConfiguration();
//        corsConfiguration.setAllowCredentials(true);
//        corsConfiguration.setAllowedOrigins(Collections.singletonList("http://localhost:4200"));
//        corsConfiguration.setAllowedHeaders(Arrays.asList(HttpHeaders.ORIGIN, HttpHeaders.CONTENT_TYPE, HttpHeaders.ACCEPT, HttpHeaders.AUTHORIZATION));
//        corsConfiguration.setAllowedMethods(Arrays.asList("GET", "PUT", "POST", "PATCH", "DELETE"));
//        source.registerCorsConfiguration("/**", corsConfiguration);
//        return new CorsFilter(source);
//    }
}
