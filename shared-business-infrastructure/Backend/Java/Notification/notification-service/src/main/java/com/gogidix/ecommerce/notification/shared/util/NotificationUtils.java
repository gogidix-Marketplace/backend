package com.gogidix.ecommerce.notification.shared.util;

import java.util.UUID;

public final class NotificationUtils {
    private NotificationUtils() {}
    public static String generateId() { return UUID.randomUUID().toString(); }
}
