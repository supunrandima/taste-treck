package com.smartmenu.taste_trek;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Disable CSRF for stateless API operations (common practice)
                .csrf(AbstractHttpConfigurer::disable)
                // Configure CORS to use your custom WebConfig settings
                .cors(cors -> cors.configure(http))

                // Set session management to stateless
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Permit access to specific endpoints
                .authorizeHttpRequests(auth -> auth
                        // Allow POST for registration and login endpoints to be accessed by anyone
                        .requestMatchers("/api/v1/customer/registerCustomer", "/api/v1/customer/customerLogin").permitAll()
                        .requestMatchers("/api/v1/admin/registerAdmin", "/api/v1/admin/adminLogin").permitAll()
                        // You might also want to allow actuator endpoints for health checks
                        .requestMatchers("/", "/health").permitAll()
                        // Secure all other API endpoints
                        .requestMatchers("/api/**").authenticated()
                        .anyRequest().permitAll() // Fallback: allow all other unmapped requests
                );

        return http.build();
    }
}