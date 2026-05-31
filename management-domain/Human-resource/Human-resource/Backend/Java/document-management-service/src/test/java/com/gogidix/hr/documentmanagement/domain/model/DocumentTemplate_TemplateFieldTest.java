package com.gogidix.hr.documentmanagement.domain.model;

import com.gogidix.hr.documentmanagement.domain.model.DocumentTemplate;
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
class DocumentTemplate_TemplateFieldTest {

        @Test
    void testBuilder() {
        DocumentTemplate.TemplateField dto = DocumentTemplate.TemplateField.builder()
                        .fieldId("test-fieldId")
            .fieldName("test-fieldName")
            .fieldType("test-fieldType")
            .label("test-label")
            .placeholder("test-placeholder")
            .required(true)
            .defaultValue("test-defaultValue")
            .options(Collections.emptyList())
            .order(42)
            .validation("test-validation")
            .helpText("test-helpText")
            .maxLength(42)
            .readOnly(true)
            .build();
        assertNotNull(dto);
        assertEquals("test-fieldId", dto.getFieldId());
        assertEquals("test-fieldName", dto.getFieldName());
        assertEquals("test-fieldType", dto.getFieldType());
        assertEquals("test-label", dto.getLabel());
        assertEquals("test-placeholder", dto.getPlaceholder());
        assertTrue(dto.getRequired());
        assertEquals("test-defaultValue", dto.getDefaultValue());
        assertEquals(42, dto.getOrder());
        assertEquals("test-validation", dto.getValidation());
        assertEquals("test-helpText", dto.getHelpText());
        assertEquals(42, dto.getMaxLength());
        assertTrue(dto.getReadOnly());
    }

    @Test
    void testSettersAndGetters() {
        DocumentTemplate.TemplateField dto = new DocumentTemplate.TemplateField();
        dto.setFieldId("val-fieldId");
        dto.setFieldName("val-fieldName");
        dto.setFieldType("val-fieldType");
        dto.setLabel("val-label");
        dto.setPlaceholder("val-placeholder");
        dto.setRequired(true);
        dto.setDefaultValue("val-defaultValue");
        dto.setOrder(99);
        dto.setValidation("val-validation");
        dto.setHelpText("val-helpText");
        dto.setMaxLength(99);
        dto.setReadOnly(true);
        assertEquals("val-fieldId", dto.getFieldId());
        assertEquals("val-fieldName", dto.getFieldName());
        assertEquals("val-fieldType", dto.getFieldType());
        assertEquals("val-label", dto.getLabel());
        assertEquals("val-placeholder", dto.getPlaceholder());
        assertTrue(dto.getRequired());
        assertEquals("val-defaultValue", dto.getDefaultValue());
        assertEquals(99, dto.getOrder());
        assertEquals("val-validation", dto.getValidation());
        assertEquals("val-helpText", dto.getHelpText());
        assertEquals(99, dto.getMaxLength());
        assertTrue(dto.getReadOnly());
    }

    @Test
    void testEqualsAndHashCode() {
        DocumentTemplate.TemplateField dto1 = DocumentTemplate.TemplateField.builder()
                        .fieldId("test-fieldId")
            .fieldName("test-fieldName")
            .fieldType("test-fieldType")
            .label("test-label")
            .placeholder("test-placeholder")
            .required(true)
            .defaultValue("test-defaultValue")
            .options(Collections.emptyList())
            .order(42)
            .validation("test-validation")
            .helpText("test-helpText")
            .maxLength(42)
            .readOnly(true)
            .build();
        DocumentTemplate.TemplateField dto2 = DocumentTemplate.TemplateField.builder()
                        .fieldId("test-fieldId")
            .fieldName("test-fieldName")
            .fieldType("test-fieldType")
            .label("test-label")
            .placeholder("test-placeholder")
            .required(true)
            .defaultValue("test-defaultValue")
            .options(Collections.emptyList())
            .order(42)
            .validation("test-validation")
            .helpText("test-helpText")
            .maxLength(42)
            .readOnly(true)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        DocumentTemplate.TemplateField dto = DocumentTemplate.TemplateField.builder()
                        .fieldId("test-fieldId")
            .fieldName("test-fieldName")
            .fieldType("test-fieldType")
            .label("test-label")
            .placeholder("test-placeholder")
            .required(true)
            .defaultValue("test-defaultValue")
            .options(Collections.emptyList())
            .order(42)
            .validation("test-validation")
            .helpText("test-helpText")
            .maxLength(42)
            .readOnly(true)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}