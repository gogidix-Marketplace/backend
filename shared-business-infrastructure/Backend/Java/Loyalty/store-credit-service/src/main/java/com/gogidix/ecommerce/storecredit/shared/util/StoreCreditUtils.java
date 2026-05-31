package com.gogidix.ecommerce.storecredit.shared.util;

import java.util.UUID;

public final class StoreCreditUtils {
    private StoreCreditUtils() {}
    public static String generateId() { return UUID.randomUUID().toString(); }
}
