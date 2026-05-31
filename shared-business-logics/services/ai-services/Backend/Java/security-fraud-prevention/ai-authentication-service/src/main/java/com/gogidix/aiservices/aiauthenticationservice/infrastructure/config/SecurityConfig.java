package com.gogidix.aiservices.aiauthenticationservice.infrastructure.config;

import com.gogidix.aiservices.aiauthenticationservice.domain.port.out.PasswordEncoderPort;
import com.gogidix.aiservices.aiauthenticationservice.domain.port.out.TokenPolicy;
import com.gogidix.aiservices.aiauthenticationservice.infrastructure.adapter.JwtTokenAdapter;
import com.gogidix.aiservices.aiauthenticationservice.infrastructure.adapter.PasswordEncoderAdapter;
import com.gogidix.aiservices.aiauthenticationservice.infrastructure.persistence.InMemoryAuthenticationRepository;
import com.gogidix.aiservices.aiauthenticationservice.domain.port.out.AuthenticationRepository;
import com.gogidix.aiservices.aiauthenticationservice.domain.port.out.EventPublisherPort;
import com.gogidix.aiservices.aiauthenticationservice.infrastructure.messaging.EventPublisherImpl;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SecurityConfig {

    @Bean
    public TokenPolicy tokenPolicy(JwtProperties properties) {
        return new JwtTokenAdapter(properties);
    }

    @Bean
    public PasswordEncoderPort passwordEncoder() {
        return new PasswordEncoderAdapter();
    }

    @Bean
    public AuthenticationRepository authenticationRepository() {
        return new InMemoryAuthenticationRepository();
    }

    @Bean
    public EventPublisherPort eventPublisher(ApplicationEventPublisher publisher) {
        return new EventPublisherImpl(publisher);
    }
}
