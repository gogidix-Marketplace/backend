package com.gogidix.ecommerce.sms.shared.util;

import java.util.UUID;

public final class SmsUtils {

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

    private SmsUtils() {
    }
}