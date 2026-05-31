package com.gogidix.ecommerce.discount.shared.util;

import java.util.UUID;

public final class DiscountUtils {
    private DiscountUtils() {}
    public static String generateId() { return UUID.randomUUID().toString(); }
}
