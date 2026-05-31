package com.gogidix.ecommerce.coupon.shared.util;

import java.util.UUID;

public final class CouponUtils {
    private CouponUtils() {}
    public static String generateId() { return UUID.randomUUID().toString(); }
}
