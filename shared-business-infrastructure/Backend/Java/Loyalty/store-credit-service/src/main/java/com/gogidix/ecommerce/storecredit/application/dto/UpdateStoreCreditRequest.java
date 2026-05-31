package com.gogidix.ecommerce.storecredit.application.dto;
import java.math.BigDecimal;
public record UpdateStoreCreditRequest(
    String name,
    String description,
    String type,
    java.math.BigDecimal creditAmount,
    java.math.BigDecimal remainingBalance,
    String reason,
    Boolean isActive
) {}
