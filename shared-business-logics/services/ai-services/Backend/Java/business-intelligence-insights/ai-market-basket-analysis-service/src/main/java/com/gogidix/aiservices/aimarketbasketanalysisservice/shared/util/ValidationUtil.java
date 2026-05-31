package com.gogidix.aiservices.aimarketbasketanalysisservice.shared.util;

import java.util.regex.Pattern;

/**
 * Utility class for ai-market-basketation-service.
 */
public class ValidationUtil {

    private static final Pattern EMAIL_PATTERN =
        Pattern.compile("^[A-Za-z0-9+._%+-]+@[A-Za-z0-9.-]+\\\\.[A-Za-z]{2,}\\$");

    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }

    public static boolean isNotEmpty(String str) {
        return str != null && !str.trim().isEmpty();
    }

    public static boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }
}
