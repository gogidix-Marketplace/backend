package com.gogidix.ecommerce.payment.shared.util;

import java.util.UUID;

public final class PaymentUtils {
    private PaymentUtils() {}
    public static String generateId() { return UUID.randomUUID().toString(); }
}
