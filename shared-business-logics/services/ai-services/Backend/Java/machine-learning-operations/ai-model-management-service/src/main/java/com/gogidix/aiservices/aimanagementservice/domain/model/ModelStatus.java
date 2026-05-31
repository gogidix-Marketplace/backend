package com.gogidix.aiservices.aimanagementservice.domain.model;

/**
 * Enumeration of model lifecycle status.
 */
public enum ModelStatus {
    REGISTERED,
    LOADING,
    LOADED,
    DEPLOYING,
    DEPLOYED,
    UNDEPLOYING,
    UNDEPLOYED,
    ARCHIVED,
    ERROR
}
