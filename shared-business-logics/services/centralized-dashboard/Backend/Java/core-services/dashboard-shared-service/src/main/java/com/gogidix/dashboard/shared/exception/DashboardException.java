package com.gogidix.dashboard.shared.exception;

import com.gogidix.dashboard.shared.constants.DashboardConstants;
import lombok.Getter;

import java.util.Map;

/**
 * Base exception for all dashboard services.
 */
@Getter
public class DashboardException extends RuntimeException {

    private final String errorCode;

    private final Map<String, Object> metadata;

    public DashboardException(String message) {
        super(message);
        this.errorCode = DashboardConstants.ERROR_INVALID_INPUT;
        this.metadata = null;
    }

    public DashboardException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
        this.metadata = null;
    }

    public DashboardException(String message, String errorCode, Map<String, Object> metadata) {
        super(message);
        this.errorCode = errorCode;
        this.metadata = metadata;
    }

    public DashboardException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = DashboardConstants.ERROR_INVALID_INPUT;
        this.metadata = null;
    }

    public DashboardException(String message, String errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.metadata = null;
    }

    /**
     * Create exception for tenant not found
     */
    public static DashboardException tenantNotFound(String tenantId) {
        return new DashboardException(
                String.format("Tenant not found: %s", tenantId),
                DashboardConstants.ERROR_TENANT_NOT_FOUND
        );
    }

    /**
     * Create exception for invalid tenant
     */
    public static DashboardException invalidTenant(String tenantId) {
        return new DashboardException(
                String.format("Invalid tenant: %s", tenantId),
                DashboardConstants.ERROR_TENANT_INVALID
        );
    }

    /**
     * Create exception for KPI not found
     */
    public static DashboardException kpiNotFound(String kpiId) {
        return new DashboardException(
                String.format("KPI not found: %s", kpiId),
                DashboardConstants.ERROR_KPI_NOT_FOUND
        );
    }

    /**
     * Create exception for KPI already exists
     */
    public static DashboardException kpiExists(String code) {
        return new DashboardException(
                String.format("KPI with code already exists: %s", code),
                DashboardConstants.ERROR_KPI_EXISTS
        );
    }

    /**
     * Create exception for calculation failed
     */
    public static DashboardException calculationFailed(String kpiCode, Throwable cause) {
        return new DashboardException(
                String.format("KPI calculation failed: %s", kpiCode),
                DashboardConstants.ERROR_CALCULATION_FAILED,
                cause
        );
    }

    /**
     * Create exception for data source error
     */
    public static DashboardException dataSourceError(String source, String message) {
        return new DashboardException(
                String.format("Data source error from %s: %s", source, message),
                DashboardConstants.ERROR_DATA_SOURCE_ERROR
        );
    }
}
