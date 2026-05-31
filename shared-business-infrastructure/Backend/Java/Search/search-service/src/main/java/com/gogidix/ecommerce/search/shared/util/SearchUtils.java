package com.gogidix.ecommerce.search.shared.util;

import java.util.UUID;

public final class SearchUtils {
    private SearchUtils() {}
    public static String generateId() { return UUID.randomUUID().toString(); }
}
