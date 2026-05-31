package com.gogidix.ecommerce.promotion.shared.util;

import java.util.UUID;

public final class PromotionUtils {
    private PromotionUtils() {}
    public static String generateId() { return UUID.randomUUID().toString(); }
}
