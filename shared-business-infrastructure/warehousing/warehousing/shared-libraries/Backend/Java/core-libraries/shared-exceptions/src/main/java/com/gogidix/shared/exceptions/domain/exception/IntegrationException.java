package com.gogidix.shared.exceptions.domain.exception;

/**
 * Integration exception for external service failures
 */
public class IntegrationException extends TechnicalException {
    
    private final String serviceName;
    
    public IntegrationException(String serviceName, String message) {
        super(String.format("Integration error with %s: %s", serviceName, message), 
              "INTEGRATION_ERROR", 503);
        this.serviceName = serviceName;
        addContext("serviceName", serviceName);
    }
    
    public IntegrationException(String serviceName, String message, Throwable cause) {
        super(String.format("Integration error with %s: %s", serviceName, message), 
              cause, "INTEGRATION_ERROR", 503);
        this.serviceName = serviceName;
        addContext("serviceName", serviceName);
    }
    
    public String getServiceName() {
        return serviceName;
    }
}
