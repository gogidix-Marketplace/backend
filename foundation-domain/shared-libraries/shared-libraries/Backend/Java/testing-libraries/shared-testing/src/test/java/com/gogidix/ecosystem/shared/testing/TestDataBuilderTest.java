package com.gogidix.ecosystem.shared.testing;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Map;
import static org.assertj.core.api.Assertions.*;

class TestDataBuilderTest {

    @Test
    void shouldGenerateRandomString() {
        String result = TestDataBuilder.randomString(10);
        assertThat(result).hasSize(10);
    }

    @Test
    void shouldGenerateRandomEmail() {
        String email = TestDataBuilder.randomEmail();
        assertThat(email).contains("@example.com");
    }

    @Test
    void shouldGenerateRandomId() {
        Long id = TestDataBuilder.randomId();
        assertThat(id).isBetween(1L, 1000000L);
    }

    @Test
    void shouldGenerateRandomDateTime() {
        LocalDateTime dateTime = TestDataBuilder.randomDateTime();
        assertThat(dateTime).isBefore(LocalDateTime.now());
    }

    @Test
    void shouldSelectRandomFromList() {
        String result = TestDataBuilder.randomFromList(Arrays.asList("A", "B", "C"));
        assertThat(result).isIn("A", "B", "C");
    }

    @Test
    void shouldGenerateRandomMetadata() {
        Map<String, Object> metadata = TestDataBuilder.randomMetadata();
        assertThat(metadata).hasSize(3);
        assertThat(metadata).containsKeys("key1", "key2", "key3");
    }
}