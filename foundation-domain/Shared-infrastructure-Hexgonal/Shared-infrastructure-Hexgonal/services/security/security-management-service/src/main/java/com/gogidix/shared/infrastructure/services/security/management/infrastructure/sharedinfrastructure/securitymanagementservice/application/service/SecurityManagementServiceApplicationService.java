package com.gogidix.shared.infrastructure.services.security.management.infrastructure.sharedinfrastructure.securitymanagementservice.application.service;

import com.gogidix.shared.infrastructure.services.security.management.domain.service.SecurityManagementDomainService;

/**
 * Application service for security-management-service operations.
 * Orchestrates business workflows and coordinates between domain and infrastructure layers.
 */
public class SecurityManagementServiceApplicationService {
    
    private final SecurityManagementDomainService domainService;

    public SecurityManagementServiceApplicationService(SecurityManagementDomainService domainService) {
        this.domainService = domainService;
    }
    
    /**
     * Process business operations for security-management-service.
     * This is a template method that should be implemented based on specific business requirements.
     *
     * @param input the operation input
     * @return operation result
     */
    public String processOperation(String input) {
        if (input == null || input.trim().isEmpty()) {
            throw new IllegalArgumentException("Input cannot be null or empty");
        }
        
        // Delegate to domain service for business logic
        return domainService.processBusinessLogic(input);
    }
}
