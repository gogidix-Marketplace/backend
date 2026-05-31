package com.gogidix.ecommerce.sms.infrastructure.config;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.IOException;

@Configuration
@ConditionalOnWebApplication
public class WebConfig implements WebMvcConfigurer {

    @Bean
    public Filter tenantFilter() {
        return new Filter() {
            @Override
            public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
                    throws IOException, ServletException {
                HttpServletRequest httpRequest = (HttpServletRequest) request;
                String tenantId = httpRequest.getHeader("X-Tenant-ID");
                if (tenantId != null) {
                    request.setAttribute("tenantId", tenantId);
                }
                chain.doFilter(request, response);
            }
        };
    }
}