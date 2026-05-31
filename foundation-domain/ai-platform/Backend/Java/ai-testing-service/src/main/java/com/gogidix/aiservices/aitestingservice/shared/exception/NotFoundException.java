package com.gogidix.aiservices.aitestingservice.shared.exception;
public class NotFoundException extends BaseDomainException {
    public NotFoundException(String resourceType, String resourceId) {
        super(String.format("%s with id '%s' was not found", resourceType, resourceId));
    }
    @Override protected String deriveErrorCode() { return "RESOURCE_NOT_FOUND"; }
}
