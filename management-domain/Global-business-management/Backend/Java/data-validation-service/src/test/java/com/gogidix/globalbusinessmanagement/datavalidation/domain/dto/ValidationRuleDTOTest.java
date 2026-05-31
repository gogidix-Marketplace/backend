package com.gogidix.globalbusinessmanagement.datavalidation.domain.dto;

import com.gogidix.globalbusinessmanagement.datavalidation.domain.dto.ValidationRuleDTO;
import java.math.BigDecimal;
import java.time.*;
import java.util.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ValidationRuleDTOTest {

        @Test
    void testBuilder() {
        ValidationRuleDTO dto = ValidationRuleDTO.builder()
                        .id("test-id")
            .name("test-name")
            .code("test-code")
            .description("test-description")
            .entityType("test-entityType")
            .fieldName("test-fieldName")
            .jsonPath("test-jsonPath")
            .value("test-value")
            .allowedValues(Collections.emptyList())
            .parameters(Collections.emptyMap())
            .enabled(true)
            .priority(42)
            .errorMessageTemplate("test-errorMessageTemplate")
            .localizedErrorMessages(Collections.emptyMap())
            .version(42)
            .createdBy("test-createdBy")
            .tags(Collections.emptyList())
            .requiresContext(true)
            .requiredContextFields(Collections.emptyList())
            .build();
        assertNotNull(dto);
        assertEquals("test-id", dto.getId());
        assertEquals("test-name", dto.getName());
        assertEquals("test-code", dto.getCode());
        assertEquals("test-description", dto.getDescription());
        assertEquals("test-entityType", dto.getEntityType());
        assertEquals("test-fieldName", dto.getFieldName());
        assertEquals("test-jsonPath", dto.getJsonPath());
        assertEquals("test-value", dto.getValue());
        assertTrue(dto.isEnabled());
        assertEquals(42, dto.getPriority());
        assertEquals("test-errorMessageTemplate", dto.getErrorMessageTemplate());
        assertEquals(42, dto.getVersion());
        assertEquals("test-createdBy", dto.getCreatedBy());
        assertTrue(dto.isRequiresContext());
    }

    @Test
    void testSettersAndGetters() {
        ValidationRuleDTO dto = new ValidationRuleDTO();
        dto.setId("val-id");
        dto.setName("val-name");
        dto.setCode("val-code");
        dto.setDescription("val-description");
        dto.setEntityType("val-entityType");
        dto.setFieldName("val-fieldName");
        dto.setJsonPath("val-jsonPath");
        dto.setValue("val-value");
        dto.setEnabled(true);
        dto.setPriority(99);
        dto.setErrorMessageTemplate("val-errorMessageTemplate");
        dto.setVersion(99);
        dto.setCreatedBy("val-createdBy");
        dto.setRequiresContext(true);
        assertEquals("val-id", dto.getId());
        assertEquals("val-name", dto.getName());
        assertEquals("val-code", dto.getCode());
        assertEquals("val-description", dto.getDescription());
        assertEquals("val-entityType", dto.getEntityType());
        assertEquals("val-fieldName", dto.getFieldName());
        assertEquals("val-jsonPath", dto.getJsonPath());
        assertEquals("val-value", dto.getValue());
        assertTrue(dto.isEnabled());
        assertEquals(99, dto.getPriority());
        assertEquals("val-errorMessageTemplate", dto.getErrorMessageTemplate());
        assertEquals(99, dto.getVersion());
        assertEquals("val-createdBy", dto.getCreatedBy());
        assertTrue(dto.isRequiresContext());
    }

    @Test
    void testEqualsAndHashCode() {
        ValidationRuleDTO dto1 = ValidationRuleDTO.builder()
                        .id("test-id")
            .name("test-name")
            .code("test-code")
            .description("test-description")
            .entityType("test-entityType")
            .fieldName("test-fieldName")
            .jsonPath("test-jsonPath")
            .value("test-value")
            .allowedValues(Collections.emptyList())
            .parameters(Collections.emptyMap())
            .enabled(true)
            .priority(42)
            .errorMessageTemplate("test-errorMessageTemplate")
            .localizedErrorMessages(Collections.emptyMap())
            .version(42)
            .createdBy("test-createdBy")
            .tags(Collections.emptyList())
            .requiresContext(true)
            .requiredContextFields(Collections.emptyList())
            .build();
        ValidationRuleDTO dto2 = ValidationRuleDTO.builder()
                        .id("test-id")
            .name("test-name")
            .code("test-code")
            .description("test-description")
            .entityType("test-entityType")
            .fieldName("test-fieldName")
            .jsonPath("test-jsonPath")
            .value("test-value")
            .allowedValues(Collections.emptyList())
            .parameters(Collections.emptyMap())
            .enabled(true)
            .priority(42)
            .errorMessageTemplate("test-errorMessageTemplate")
            .localizedErrorMessages(Collections.emptyMap())
            .version(42)
            .createdBy("test-createdBy")
            .tags(Collections.emptyList())
            .requiresContext(true)
            .requiredContextFields(Collections.emptyList())
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        ValidationRuleDTO dto = ValidationRuleDTO.builder()
                        .id("test-id")
            .name("test-name")
            .code("test-code")
            .description("test-description")
            .entityType("test-entityType")
            .fieldName("test-fieldName")
            .jsonPath("test-jsonPath")
            .value("test-value")
            .allowedValues(Collections.emptyList())
            .parameters(Collections.emptyMap())
            .enabled(true)
            .priority(42)
            .errorMessageTemplate("test-errorMessageTemplate")
            .localizedErrorMessages(Collections.emptyMap())
            .version(42)
            .createdBy("test-createdBy")
            .tags(Collections.emptyList())
            .requiresContext(true)
            .requiredContextFields(Collections.emptyList())
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}