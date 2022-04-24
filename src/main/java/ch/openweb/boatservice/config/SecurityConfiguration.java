package ch.openweb.boatservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoders;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfiguration {

  @Value("${spring.security.oauth2.resourceserver.jwk.issuer-uri}")
  private String issuerUri;

  @Bean
  SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
    // @formatter:off
    http
      .authorizeExchange((authorize) -> authorize.anyExchange().authenticated())
      .oauth2ResourceServer(ServerHttpSecurity.OAuth2ResourceServerSpec::jwt);
    // @formatter:on
    return http.build();
  }

  @Bean
  public ReactiveJwtDecoder jwtDecoder() {
    return ReactiveJwtDecoders.fromOidcIssuerLocation(issuerUri);
  }

}
