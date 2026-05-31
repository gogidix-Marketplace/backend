package com.gogidix.shared.ai.domain.model;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PromptTemplateTest {

    @Test
    void resolve_replacesVariables() {
        PromptTemplate pt = PromptTemplate.builder()
                .name("test")
                .template("Hello {{name}}, your order {{orderId}} is ready.")
                .build();
        String result = pt.resolve(Map.of("name", "Alice", "orderId", "123"));
        assertEquals("Hello Alice, your order 123 is ready.", result);
    }

    @Test
    void resolve_usesDefaultValues() {
        PromptTemplate pt = PromptTemplate.builder()
                .name("test")
                .template("Hello {{name}} from {{city}}")
                .defaultValues(Map.of("city", "Lagos"))
                .build();
        assertEquals("Hello Alice from Lagos", pt.resolve(Map.of("name", "Alice")));
    }

    @Test
    void resolve_keepsUnresolvedPlaceholders() {
        PromptTemplate pt = PromptTemplate.builder()
                .name("test")
                .template("{{a}} {{b}}")
                .build();
        assertEquals("{{a}} {{b}}", pt.resolve(Map.of()));
    }

    @Test
    void builder_defaultValues() {
        PromptTemplate pt = PromptTemplate.builder().name("t").template("c").build();
        assertEquals(Map.of(), pt.getDefaultValues());
        assertEquals(4096, pt.getMaxTokens());
        assertEquals(0.7, pt.getTemperature());
    }
}
