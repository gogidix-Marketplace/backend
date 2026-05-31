package com.gogidix.ecommerce.giftcard.shared.util;

import java.util.UUID;

public final class GiftCardUtils {
    private GiftCardUtils() {}
    public static String generateId() { return UUID.randomUUID().toString(); }
}
