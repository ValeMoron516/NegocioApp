package com.desarrolloweb.NegocioApp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class PasswordConfig {

    // Encoder de contraseñas compartido por toda la app (se inyecta donde haga falta).
    // BCrypt con el costo por defecto (10): genera un salt aleatorio por cada hash.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
