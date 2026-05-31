package com.gogidix.ecommerce.paymentmethod.shared.util;

import java.util.UUID;

public final class PaymentMethodUtils {

    public static String generateId() {
        return UUID.randomUUID().toString();
    }

    public static boolean isValidId(String id) {
        return id != null && !id.isBlank();
    }

    public static String sanitize(String input) {
        if (input == null) return null;
        return input.trim();
    }

    private PaymentMethodUtils() {
    }
}