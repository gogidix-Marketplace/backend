package com.gogidix.customersupport.globalsupportdashboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Main Application class for Global Support Dashboard Service
 *
 * This service provides global-level analytics and dashboard functionality
 * for the Customer Support domain, including:
 * - Global support metrics aggregation
 * - Multi-region performance tracking
 * - Agent productivity analytics
 * - Ticket volume and resolution trends
 * - SLA compliance monitoring
 * - Customer satisfaction scores (CSAT)
 *
 * @port 8100
 * @author Gogidix
 * @version 1.0.0
 */
@SpringBootApplication
public class GlobalSupportDashboardServiceApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(GlobalSupportDashboardServiceApplication.class);
        application.run(args);
    }

    /**
     * CORS Configuration for cross-origin requests
     */
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**")
                        .allowedOrigins("*")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
                        .allowedHeaders("*")
                        .maxAge(3600);
            }
        };
    }
}
