package com.gogidix.ecommerce.reward.shared.util;

import java.util.UUID;

public final class RewardUtils {
    private RewardUtils() {}
    public static String generateId() { return UUID.randomUUID().toString(); }
}
