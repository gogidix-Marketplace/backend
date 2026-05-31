package com.gogidix.ecommerce.paymentmethod.shared.util;

import java.util.UUID;

public final class PaymentMethodUtils {
    private PaymentMethodUtils() {}
    public static String generateId() { return UUID.randomUUID().toString(); }
}
