package com.gogidix.ecommerce.wishlist.shared.util;

import java.util.UUID;

public final class WishlistUtils {
    private WishlistUtils() {}
    public static String generateId() { return UUID.randomUUID().toString(); }
}
