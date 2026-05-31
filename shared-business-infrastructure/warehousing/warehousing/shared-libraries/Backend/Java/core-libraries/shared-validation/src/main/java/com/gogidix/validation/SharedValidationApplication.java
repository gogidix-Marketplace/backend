package com.gogidix.validation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;

/**
 * Shared Validation Application for the Gogidix Social E-commerce Ecosystem.
 * Implements hexagonal architecture with comprehensive validation services.
 * 
 * Features:
 * - Hexagonal architecture implementation (Domain-driven design)
 * - Advanced validation rules and constraints engine
 * - Email, phone number, credit card, and SSN validation patterns
 * - Validation contexts and conditional validation
 * - Cross-field validation and business rule validation
 * - Validation groups and rule orchestration
 * - REST API for validation operations
 * - Real-time validation statistics and monitoring
 * - Multi-language validation messages
 * - Custom validator implementations with extensive patterns
 * - Validation result aggregation and reporting
 * 
 * Architecture Layers:
 * - Domain: ValidationRule, ValidationResult, ValidationContext, ValidationPatterns
 * - Application: ValidationUseCase, ValidationService
 * - Adapter: ValidationController (REST API)
 * 
 * @author Gogidix Development Team
 * @since 2.0.0 (Hexagonal Architecture)
 * @version 2.0.0
 */
@SpringBootApplication(exclude = {FlywayAutoConfiguration.class}, scanBasePackages = "com.gogidix")
public class SharedValidationApplication {

    public static void main(String[] args) {
        SpringApplication.run(SharedValidationApplication.class, args);
    }
}