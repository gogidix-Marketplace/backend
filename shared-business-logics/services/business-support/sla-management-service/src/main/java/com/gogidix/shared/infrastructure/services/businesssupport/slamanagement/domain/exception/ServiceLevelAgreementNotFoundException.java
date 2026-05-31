package com.gogidix.shared.infrastructure.services.businesssupport.slamanagement.domain.exception;
/**
 * Exception thrown when ServiceLevelAgreement is not found
 */
public class ServiceLevelAgreementNotFoundException extends RuntimeException {
    public ServiceLevelAgreementNotFoundException(String id) {
        super("Service Level Agreement not found with id: " + id);
    }
}
