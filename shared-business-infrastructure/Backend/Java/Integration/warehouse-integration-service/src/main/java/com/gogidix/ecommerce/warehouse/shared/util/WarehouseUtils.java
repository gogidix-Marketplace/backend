package com.gogidix.ecommerce.warehouse.shared.util;

import java.util.UUID;

public final class WarehouseUtils {
    private WarehouseUtils() {}
    public static String generateId() { return UUID.randomUUID().toString(); }
}
