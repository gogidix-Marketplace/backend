package com.gogidix.shared.warehousing.returns.domain.exception;

public class ReturnNotFoundException extends RuntimeException {

    public ReturnNotFoundException(String id) {
        super("Return not found: " + id);
    }

    public ReturnNotFoundException(String identifier, String tenantId) {
        super("Return not found: " + identifier + " for tenant: " + tenantId);
    }
}
