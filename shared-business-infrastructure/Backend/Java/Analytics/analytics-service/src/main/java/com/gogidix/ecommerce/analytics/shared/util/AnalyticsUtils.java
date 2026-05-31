package com.gogidix.ecommerce.analytics.shared.util;

import java.util.UUID;

public final class AnalyticsUtils {
    private AnalyticsUtils() {}
    public static String generateId() { return UUID.randomUUID().toString(); }
}
