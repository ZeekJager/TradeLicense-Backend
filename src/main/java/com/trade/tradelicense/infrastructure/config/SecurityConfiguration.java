package com.trade.tradelicense.infrastructure.config;

import com.trade.tradelicense.infrastructure.security.TokenAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfiguration {
    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            TokenAuthenticationFilter tokenAuthenticationFilter
    ) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults())
                .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setStatus(401);
                            response.setContentType("application/json");
                            response.getWriter().write("{\"message\":\"Unauthorized\"}");
                        })
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            response.setStatus(403);
                            response.setContentType("application/json");
                            response.getWriter().write("{\"message\":\"Forbidden\"}");
                        }))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/auth/register").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/trade-licenses/verify/*").permitAll()
                        .requestMatchers("/", "/index.html", "/assets/**", "/static/**", "/h2-console/**").permitAll()
                        .requestMatchers("/api/auth/me", "/api/auth/logout").authenticated()
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/trade-license-applications/**").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/trade-licenses/**").hasAnyRole("CUSTOMER", "LICENSEE", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/trade-license-reviews/**").hasAnyRole("REVIEWER", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/trade-license-approvals/**").hasAnyRole("APPROVER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/trade-license-applications").hasAnyRole("CUSTOMER", "LICENSEE", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/trade-license-applications/*/submit").hasAnyRole("CUSTOMER", "LICENSEE", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/trade-license-applications/*/resubmit").hasAnyRole("CUSTOMER", "LICENSEE", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/trade-license-applications/*/cancel").hasAnyRole("CUSTOMER", "LICENSEE", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/trade-license-applications/*/documents/**").hasAnyRole("CUSTOMER", "LICENSEE", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/trade-license-applications/*/payment/**").hasAnyRole("CUSTOMER", "LICENSEE", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/trade-license-reviews/**").hasAnyRole("REVIEWER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/trade-license-approvals/**").hasAnyRole("APPROVER", "ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/trade-licenses/**").hasAnyRole("CUSTOMER", "LICENSEE", "ADMIN")
                        .anyRequest().authenticated())
                .addFilterBefore(tokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:5173", "http://localhost:4173", "http://localhost:8080"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type", "Accept"));
        configuration.setExposedHeaders(List.of("Content-Disposition"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
