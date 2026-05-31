package com.gogidix.ecommerce.oceanshipping.shared.util;

import java.util.UUID;

public final class OceanShippingUtils {
    private OceanShippingUtils() {}
    public static String generateId() { return UUID.randomUUID().toString(); }
}
