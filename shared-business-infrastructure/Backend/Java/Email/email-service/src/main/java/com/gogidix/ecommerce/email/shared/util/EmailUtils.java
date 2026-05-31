package com.gogidix.ecommerce.email.shared.util;

import java.util.UUID;

public final class EmailUtils {

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

    private EmailUtils() {
    }
}