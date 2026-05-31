package com.gogidix.ecommerce.pushnotification.shared.util;

import java.util.UUID;

public final class PushNotificationUtils {
    private PushNotificationUtils() {}
    public static String generateId() { return UUID.randomUUID().toString(); }
}
