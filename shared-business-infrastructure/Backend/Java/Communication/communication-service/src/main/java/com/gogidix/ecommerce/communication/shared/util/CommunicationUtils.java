package com.gogidix.ecommerce.communication.shared.util;

import java.util.UUID;

public final class CommunicationUtils {
    private CommunicationUtils() {}
    public static String generateId() { return UUID.randomUUID().toString(); }
}
