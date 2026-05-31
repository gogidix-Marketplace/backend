package com.gogidix.shared.ai;

public final class SharedAiConstants {

    public static final String KAFKA_TOPIC_PREFIX = "cargo.ai";
    public static final String EMBEDDING_DIMENSION_SMALL = "1536";
    public static final String EMBEDDING_DIMENSION_LARGE = "3072";
    public static final int DEFAULT_MAX_TOKENS = 4096;
    public static final double DEFAULT_TEMPERATURE = 0.7;

    private SharedAiConstants() {}
}
