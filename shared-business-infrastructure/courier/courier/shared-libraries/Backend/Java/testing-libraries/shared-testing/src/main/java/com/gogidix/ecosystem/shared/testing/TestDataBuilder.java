package com.gogidix.ecosystem.shared.testing;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class TestDataBuilder {
    
    public static String randomString(int length) {
        return UUID.randomUUID().toString().substring(0, Math.min(length, 36));
    }
    
    public static String randomEmail() {
        return randomString(8) + "@example.com";
    }
    
    public static Long randomId() {
        return ThreadLocalRandom.current().nextLong(1, 1000000);
    }
    
    public static LocalDateTime randomDateTime() {
        return LocalDateTime.now().minusDays(ThreadLocalRandom.current().nextInt(0, 365));
    }
    
    public static <T> T randomFromList(List<T> items) {
        return items.get(ThreadLocalRandom.current().nextInt(items.size()));
    }
    
    public static Map<String, Object> randomMetadata() {
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("key1", randomString(10));
        metadata.put("key2", randomId());
        metadata.put("key3", randomDateTime());
        return metadata;
    }
}