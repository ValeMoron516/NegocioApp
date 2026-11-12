package com.desarrolloweb.NegocioApp.config;

import java.util.Base64;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import com.nimbusds.jose.jwk.source.ImmutableSecret;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {
    @Bean
    public SecretKey jwtSecret(@Value("${NEGOCIOAPP_JWT_SECRET:}") String encoded) {
        if (encoded == null || encoded.isBlank()) {
            throw new IllegalStateException("Configure NEGOCIOAPP_JWT_SECRET con 32+ bytes aleatorios Base64. Consultar INSTRUCCIONES_AUTH.md");
        }
        byte[] key;
        try { key = Base64.getDecoder().decode(encoded); }
        catch (IllegalArgumentException ex) { throw new IllegalStateException("NEGOCIOAPP_JWT_SECRET debe ser Base64", ex); }
        if (key.length < 32) throw new IllegalStateException("NEGOCIOAPP_JWT_SECRET debe contener 32+ bytes aleatorios");
        return new SecretKeySpec(key, "HmacSHA256");
    }

    @Bean public JwtEncoder jwtEncoder(SecretKey jwtSecret) {
        return new NimbusJwtEncoder(new ImmutableSecret<>(jwtSecret));
    }
    @Bean public JwtDecoder jwtDecoder(SecretKey jwtSecret) {
        return NimbusJwtDecoder.withSecretKey(jwtSecret).macAlgorithm(MacAlgorithm.HS256).build();
    }
    @Bean public SecurityFilterChain apiSecurity(HttpSecurity http) throws Exception {
        http
            // Bearer tokens are provided explicitly, not automatically via cookies.
            .csrf(AbstractHttpConfigurer::disable)
            .cors(AbstractHttpConfigurer::disable) // Astro dev proxy ensures same-origin API calls.
            .httpBasic(AbstractHttpConfigurer::disable)
            .formLogin(AbstractHttpConfigurer::disable)
            .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .exceptionHandling(ex -> ex.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
            .authorizeHttpRequests(a -> a
                .requestMatchers(HttpMethod.POST,"/auth/login","/auth/registro").permitAll()
                .requestMatchers(HttpMethod.GET, "/negocio/mios").authenticated()
                .requestMatchers(HttpMethod.GET, "/productos", "/productos/**",
                    "/negocio/*", "/negocio/buscarPorNombre",
                    "/negocio/buscarPorProducto", "/categorias", "/categorias/**",
                    "/valoraciones", "/valoraciones/promedio",
                    "/valoraciones-negocio", "/valoraciones-negocio/*",
                    "/valoraciones-negocio/negocios/*/valoraciones",
                    "/valoraciones-negocio/estrellas/*"
                ).permitAll()
                .anyRequest().authenticated())
            .oauth2ResourceServer(o -> o.jwt(Customizer.withDefaults()));
        return http.build();
    }
}
