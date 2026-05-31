package com.gogidix.courier.discountservice.application.dto;

import java.math.BigDecimal;

public record ValidationResponse(
        boolean valid,
        BigDecimal discountAmount,
        BigDecimal finalAmount,
        String rejectionReason,
        String code
) {
    public static ValidationResponse valid(BigDecimal discountAmount, BigDecimal finalAmount, String code) {
        return new ValidationResponse(true, discountAmount, finalAmount, null, code);
    }

    public static ValidationResponse invalid(String rejectionReason, String code) {
        return new ValidationResponse(false, BigDecimal.ZERO, BigDecimal.ZERO, rejectionReason, code);
    }
}
