package com.gogidix.cargo.eventdriven.infrastructure.serialization;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class JsonEventSerializerTest {
    private JsonEventSerializer serializer;

    @BeforeEach
    void setUp() { serializer = new JsonEventSerializer(); }

    @Test
    void shouldSerializeObject() {
        String json = serializer.serialize(new TestDto("hello", 42));
        assertNotNull(json);
        assertTrue(json.contains("hello"));
        assertTrue(json.contains("42"));
    }

    @Test
    void shouldDeserializeObject() {
        String json = "{\"name\":\"world\",\"value\":99}";
        TestDto result = serializer.deserialize(json, TestDto.class);
        assertEquals("world", result.getName());
        assertEquals(99, result.getValue());
    }

    @Test
    void shouldThrowOnInvalidJson() {
        assertThrows(RuntimeException.class, () -> serializer.deserialize("invalid", TestDto.class));
    }

    static class TestDto {
        private String name;
        private int value;
        public TestDto() {}
        public TestDto(String name, int value) { this.name = name; this.value = value; }
        public String getName() { return name; }
        public int getValue() { return value; }
    }
}
