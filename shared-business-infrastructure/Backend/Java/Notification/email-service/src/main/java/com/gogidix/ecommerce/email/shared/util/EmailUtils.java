package com.gogidix.ecommerce.email.shared.util;

import java.util.UUID;

public final class EmailUtils {
    private EmailUtils() {}
    public static String generateId() { return UUID.randomUUID().toString(); }
}
