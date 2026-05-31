package com.gogidix.ecommerce.inventorysync.shared.util;

import java.util.UUID;

public final class InventorySyncUtils {
    private InventorySyncUtils() {}
    public static String generateId() { return UUID.randomUUID().toString(); }
}
