package com.prodnretail.inventory_gen.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // ✅ Disable CSRF for APIs like POST, PUT, DELETE
                .authorizeHttpRequests(requests -> requests
                        .anyRequest().permitAll()); // Allow all requests without auth (dev only)

        return http.build();
    }
}
