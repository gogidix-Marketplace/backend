package com.gogidix.shared.ai.application.service;

import com.gogidix.shared.ai.domain.model.PromptTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PromptTemplateServiceTest {

    private PromptTemplateService service;

    @BeforeEach
    void setUp() {
        service = new PromptTemplateService();
    }

    @Test
    void registerTemplate_andResolve() {
        PromptTemplate template = PromptTemplate.builder()
                .name("greeting")
                .template("Hello {{name}}, welcome to {{place}}!")
                .build();
        service.registerTemplate(template);

        String result = service.resolve("greeting", Map.of("name", "Alice", "place", "Lagos"));
        assertEquals("Hello Alice, welcome to Lagos!", result);
    }

    @Test
    void resolve_usesDefaultValues() {
        PromptTemplate template = PromptTemplate.builder()
                .name("farewell")
                .template("Goodbye {{name}}, see you at {{place}}!")
                .defaultValues(Map.of("place", "CargoNexus"))
                .build();
        service.registerTemplate(template);

        String result = service.resolve("farewell", Map.of("name", "Bob"));
        assertEquals("Goodbye Bob, see you at CargoNexus!", result);
    }

    @Test
    void resolve_overridesDefaultsWithProvidedValues() {
        PromptTemplate template = PromptTemplate.builder()
                .name("test")
                .template("{{a}} {{b}}")
                .defaultValues(Map.of("a", "default-a", "b", "default-b"))
                .build();
        service.registerTemplate(template);

        String result = service.resolve("test", Map.of("a", "override-a"));
        assertEquals("override-a default-b", result);
    }

    @Test
    void resolve_throwsForMissingTemplate() {
        assertThrows(IllegalArgumentException.class, () -> service.resolve("nonexistent", Map.of()));
    }

    @Test
    void getTemplate_returnsRegisteredTemplate() {
        PromptTemplate template = PromptTemplate.builder().name("t1").template("content").build();
        service.registerTemplate(template);

        assertEquals(template, service.getTemplate("t1"));
    }

    @Test
    void getTemplate_throwsForMissing() {
        assertThrows(IllegalArgumentException.class, () -> service.getTemplate("missing"));
    }

    @Test
    void removeTemplate_removesSuccessfully() {
        service.registerTemplate(PromptTemplate.builder().name("temp").template("x").build());
        assertTrue(service.hasTemplate("temp"));
        service.removeTemplate("temp");
        assertFalse(service.hasTemplate("temp"));
    }

    @Test
    void getAllTemplates_returnsAllRegistered() {
        service.registerTemplate(PromptTemplate.builder().name("a").template("a").build());
        service.registerTemplate(PromptTemplate.builder().name("b").template("b").build());
        assertEquals(2, service.getAllTemplates().size());
    }
}
