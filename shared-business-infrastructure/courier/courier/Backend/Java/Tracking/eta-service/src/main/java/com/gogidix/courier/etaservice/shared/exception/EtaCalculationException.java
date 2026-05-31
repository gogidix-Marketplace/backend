package com.gogidix.courier.etaservice.shared.exception;

/**
 * Exception thrown when ETA calculation fails.
 */
public class EtaCalculationException extends BaseDomainException {

    private final String dispatchId;

    public EtaCalculationException(String message, String dispatchId) {
        super(message);
        this.dispatchId = dispatchId;
    }

    public EtaCalculationException(String message, String dispatchId, Throwable cause) {
        super(message, cause);
        this.dispatchId = dispatchId;
    }

    public String getDispatchId() {
        return dispatchId;
    }

    @Override
    protected String deriveErrorCode() {
        return "ETA_CALCULATION_ERROR";
    }
}
