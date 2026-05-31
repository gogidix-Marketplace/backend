package com.gogidix.finance.cashflow.shared.util;

public final class ValidationUtil {

    private ValidationUtil() {}

    public static void requireNonNull(Object value, String fieldName) {
        if (value == null) {
            throw new IllegalArgumentException(fieldName + " is required");
        }
    }

    public static void requireNonBlank(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " is required and cannot be blank");
        }
    }
}
