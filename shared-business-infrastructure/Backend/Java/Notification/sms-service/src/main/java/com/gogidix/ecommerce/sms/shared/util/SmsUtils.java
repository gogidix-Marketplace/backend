package com.gogidix.ecommerce.sms.shared.util;

import java.util.UUID;

public final class SmsUtils {
    private SmsUtils() {}
    public static String generateId() { return UUID.randomUUID().toString(); }
}
