package com.gogidix.ecommerce.customer.shared.util;

import java.util.UUID;

public final class CustomerUtils {
    private CustomerUtils() {}
    public static String generateId() { return UUID.randomUUID().toString(); }
}
