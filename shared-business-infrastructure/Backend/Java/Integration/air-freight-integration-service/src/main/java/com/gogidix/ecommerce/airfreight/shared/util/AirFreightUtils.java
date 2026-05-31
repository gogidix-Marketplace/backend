package com.gogidix.ecommerce.airfreight.shared.util;

import java.util.UUID;

public final class AirFreightUtils {
    private AirFreightUtils() {}
    public static String generateId() { return UUID.randomUUID().toString(); }
}
