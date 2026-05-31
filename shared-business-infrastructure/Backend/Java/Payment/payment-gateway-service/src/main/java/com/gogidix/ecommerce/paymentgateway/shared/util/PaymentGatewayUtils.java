package com.gogidix.ecommerce.paymentgateway.shared.util;

import java.util.UUID;

public final class PaymentGatewayUtils {
    private PaymentGatewayUtils() {}
    public static String generateId() { return UUID.randomUUID().toString(); }
}
