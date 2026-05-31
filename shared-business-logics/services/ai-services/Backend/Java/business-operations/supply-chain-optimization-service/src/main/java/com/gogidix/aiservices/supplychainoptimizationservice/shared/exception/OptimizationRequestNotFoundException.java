package com.gogidix.aiservices.supplychainoptimizationservice.shared.exception;

public class OptimizationRequestNotFoundException extends SupplyChainException {
    public OptimizationRequestNotFoundException(String requestId) {
        super("Optimization request not found: " + requestId);
    }
}
