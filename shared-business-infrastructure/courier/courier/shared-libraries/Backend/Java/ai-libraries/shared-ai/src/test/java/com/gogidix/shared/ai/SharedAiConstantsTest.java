package com.gogidix.shared.ai;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SharedAiConstantsTest {

    @Test
    void constantsHaveExpectedValues() {
        assertEquals("cargo.ai", SharedAiConstants.KAFKA_TOPIC_PREFIX);
        assertEquals("1536", SharedAiConstants.EMBEDDING_DIMENSION_SMALL);
        assertEquals("3072", SharedAiConstants.EMBEDDING_DIMENSION_LARGE);
        assertEquals(4096, SharedAiConstants.DEFAULT_MAX_TOKENS);
        assertEquals(0.7, SharedAiConstants.DEFAULT_TEMPERATURE);
    }
}
