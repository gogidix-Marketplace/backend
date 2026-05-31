package com.gogidix.shared.utilities;

import org.junit.jupiter.api.Test;
import java.util.Map;
import static org.assertj.core.api.Assertions.*;

class JsonUtilsTest {

    @Test
    void shouldSerializeToJson() {
        TestObject obj = new TestObject("test", 123);
        String json = JsonUtils.toJson(obj);
        assertThat(json).contains("test").contains("123");
    }

    @Test
    void shouldDeserializeFromJson() {
        String json = "{\"name\":\"test\",\"value\":123}";
        TestObject obj = JsonUtils.fromJson(json, TestObject.class);
        assertThat(obj.name).isEqualTo("test");
        assertThat(obj.value).isEqualTo(123);
    }

    @Test
    void shouldValidateJson() {
        assertThat(JsonUtils.isValidJson("{\"valid\": true}")).isTrue();
        assertThat(JsonUtils.isValidJson("invalid json")).isFalse();
    }

    @Test
    void shouldThrowExceptionForInvalidSerialization() {
        assertThatThrownBy(() -> JsonUtils.toJson(new InvalidObject()))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Failed to serialize");
    }

    static class TestObject {
        public String name;
        public int value;
        
        public TestObject() {}
        public TestObject(String name, int value) {
            this.name = name;
            this.value = value;
        }
    }

    static class InvalidObject {
        public Object circular = this;
    }
}