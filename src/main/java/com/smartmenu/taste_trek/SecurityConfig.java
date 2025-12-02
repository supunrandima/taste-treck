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
                        // 1. Allow POST for registration and customer login endpoints (public access)
                        .requestMatchers("/api/v1/customer/registerCustomer", "/api/v1/customer/customerLogin").permitAll()

                        // 2. Allow POST for admin registration and login (public access)
                        .requestMatchers("/api/v1/admin/registerAdmin", "/api/v1/admin/adminLogin").permitAll()

                        .requestMatchers("/api/v1/admin/menu/allItems", "/api/v1/admin/menu/newMenuItem", "/api/v1/admin/menu/{itemId}").permitAll()

                        .requestMatchers("/api/v1/admin/categories/allCategories", "/api/v1/admin/categories/newCategory", "/api/v1/admin/categories/{categoryId}").permitAll()

                        .requestMatchers("/api/v1/admin/menu/**").permitAll()
                        .requestMatchers("/api/v1/admin/categories/**").permitAll()
                        .requestMatchers("/api/v1/staff/**").permitAll()

                        // 3. Allow Actuator endpoints and root path
                        .requestMatchers("/", "/health").permitAll()

                        // FIX: Secure ALL remaining admin routes and any other API path starting with /api/
                        .requestMatchers("/api/v1/admin/**").authenticated() // Now protects Menu, Category, and Admin CRUD operations

                        // Fallback security for other authenticated APIs
                        .requestMatchers("/api/**").authenticated()

                        // Fallback: allow all other unmapped requests (e.g. static files, favicon)
                        .anyRequest().permitAll()
                );

        return http.build();
    }
}