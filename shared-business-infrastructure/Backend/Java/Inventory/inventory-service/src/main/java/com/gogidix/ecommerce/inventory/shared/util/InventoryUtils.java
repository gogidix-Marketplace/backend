package com.gogidix.ecommerce.inventory.shared.util;

import java.util.UUID;

public final class InventoryUtils {
    private InventoryUtils() {}
    public static String generateId() { return UUID.randomUUID().toString(); }
}
