package com.gogidix.ecommerce.loyalty.shared.util;

import java.util.UUID;

public final class LoyaltyUtils {
    private LoyaltyUtils() {}
    public static String generateId() { return UUID.randomUUID().toString(); }
}
