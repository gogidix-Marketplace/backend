package com.gogidix.corporatecms.application.config;

import com.gogidix.corporatecms.application.security.JwtAuthenticationFilter;
import com.gogidix.corporatecms.application.security.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

/**
 * Security configuration for the application.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserDetailsServiceImpl userDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    private static final String[] PUBLIC_ENDPOINTS = {
            "/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/v3/api-docs/**",
            "/actuator/**",
            "/api/cms/v1/auth/**"
    };

    private static final String[] READ_ENDPOINTS = {
            "/api/cms/v1/content/published/**",
            "/api/cms/v1/products/published/**",
            "/api/cms/v1/jobs/open/**",
            "/api/cms/v1/media/download/**"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(PUBLIC_ENDPOINTS).permitAll()
                        .requestMatchers(HttpMethod.GET, READ_ENDPOINTS).permitAll()
                        .requestMatchers("/api/cms/v1/analytics/**").hasRole("ADMIN")
                        .requestMatchers("/api/cms/v1/users/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/cms/v1/content/**").hasAnyAuthority("CONTENT_WRITE", "CONTENT_APPROVE")
                        .requestMatchers(HttpMethod.PUT, "/api/cms/v1/content/**").hasAnyAuthority("CONTENT_WRITE", "CONTENT_APPROVE")
                        .requestMatchers(HttpMethod.DELETE, "/api/cms/v1/content/**").hasAuthority("CONTENT_DELETE")
                        .requestMatchers(HttpMethod.POST, "/api/cms/v1/media/**").hasAuthority("MEDIA_MANAGE")
                        .requestMatchers(HttpMethod.PUT, "/api/cms/v1/media/**").hasAuthority("MEDIA_MANAGE")
                        .requestMatchers(HttpMethod.DELETE, "/api/cms/v1/media/**").hasAuthority("MEDIA_MANAGE")
                        .requestMatchers("/api/cms/v1/products/**").hasAuthority("PRODUCT_MANAGE")
                        .requestMatchers("/api/cms/v1/jobs/**").hasAuthority("CAREER_MANAGE")
                        .requestMatchers("/api/cms/v1/leads/**").hasAuthority("LEAD_MANAGE")
                        .anyRequest().authenticated()
                )
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setExposedHeaders(Arrays.asList("Authorization", "Content-Type"));
        configuration.setAllowCredentials(false);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
