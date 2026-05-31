package com.gogidix.ecommerce.storecredit.application.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateStoreCreditRequest(
    @NotBlank String name,
    String description,
    String type,
    java.math.BigDecimal creditAmount,
    java.math.BigDecimal remainingBalance,
    String reason
) {}
