package com.gogidix.globalbusinessmanagement.datavalidation.domain.dto;

import com.gogidix.globalbusinessmanagement.datavalidation.domain.dto.ValidationResultDTO;
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
class ValidationResultDTOTest {

        @Test
    void testBuilder() {
        ValidationResultDTO dto = ValidationResultDTO.builder()
                        .id("test-id")
            .validationId("test-validationId")
            .entityType("test-entityType")
            .entityId("test-entityId")
            .entityData("test-entityData")
            .ruleCode("test-ruleCode")
            .ruleName("test-ruleName")
            .passed(true)
            .fieldName("test-fieldName")
            .actualValue("test-actualValue")
            .expectedValue("test-expectedValue")
            .errorMessage("test-errorMessage")
            .localizedErrorMessages(Collections.emptyMap())
            .errorCode("test-errorCode")
            .context(Collections.emptyMap())
            .validatedBy("test-validatedBy")
            .executionTimeMs(42L)
            .batchId("test-batchId")
            .tenantId("test-tenantId")
            .metadata(Collections.emptyMap())
            .build();
        assertNotNull(dto);
        assertEquals("test-id", dto.getId());
        assertEquals("test-validationId", dto.getValidationId());
        assertEquals("test-entityType", dto.getEntityType());
        assertEquals("test-entityId", dto.getEntityId());
        assertEquals("test-entityData", dto.getEntityData());
        assertEquals("test-ruleCode", dto.getRuleCode());
        assertEquals("test-ruleName", dto.getRuleName());
        assertTrue(dto.isPassed());
        assertEquals("test-fieldName", dto.getFieldName());
        assertEquals("test-actualValue", dto.getActualValue());
        assertEquals("test-expectedValue", dto.getExpectedValue());
        assertEquals("test-errorMessage", dto.getErrorMessage());
        assertEquals("test-errorCode", dto.getErrorCode());
        assertEquals("test-validatedBy", dto.getValidatedBy());
        assertEquals(42L, dto.getExecutionTimeMs());
        assertEquals("test-batchId", dto.getBatchId());
        assertEquals("test-tenantId", dto.getTenantId());
    }

    @Test
    void testSettersAndGetters() {
        ValidationResultDTO dto = new ValidationResultDTO();
        dto.setId("val-id");
        dto.setValidationId("val-validationId");
        dto.setEntityType("val-entityType");
        dto.setEntityId("val-entityId");
        dto.setEntityData("val-entityData");
        dto.setRuleCode("val-ruleCode");
        dto.setRuleName("val-ruleName");
        dto.setPassed(true);
        dto.setFieldName("val-fieldName");
        dto.setActualValue("val-actualValue");
        dto.setExpectedValue("val-expectedValue");
        dto.setErrorMessage("val-errorMessage");
        dto.setErrorCode("val-errorCode");
        dto.setValidatedBy("val-validatedBy");
        dto.setBatchId("val-batchId");
        dto.setTenantId("val-tenantId");
        assertEquals("val-id", dto.getId());
        assertEquals("val-validationId", dto.getValidationId());
        assertEquals("val-entityType", dto.getEntityType());
        assertEquals("val-entityId", dto.getEntityId());
        assertEquals("val-entityData", dto.getEntityData());
        assertEquals("val-ruleCode", dto.getRuleCode());
        assertEquals("val-ruleName", dto.getRuleName());
        assertTrue(dto.isPassed());
        assertEquals("val-fieldName", dto.getFieldName());
        assertEquals("val-actualValue", dto.getActualValue());
        assertEquals("val-expectedValue", dto.getExpectedValue());
        assertEquals("val-errorMessage", dto.getErrorMessage());
        assertEquals("val-errorCode", dto.getErrorCode());
        assertEquals("val-validatedBy", dto.getValidatedBy());
        assertEquals("val-batchId", dto.getBatchId());
        assertEquals("val-tenantId", dto.getTenantId());
    }

    @Test
    void testEqualsAndHashCode() {
        ValidationResultDTO dto1 = ValidationResultDTO.builder()
                        .id("test-id")
            .validationId("test-validationId")
            .entityType("test-entityType")
            .entityId("test-entityId")
            .entityData("test-entityData")
            .ruleCode("test-ruleCode")
            .ruleName("test-ruleName")
            .passed(true)
            .fieldName("test-fieldName")
            .actualValue("test-actualValue")
            .expectedValue("test-expectedValue")
            .errorMessage("test-errorMessage")
            .localizedErrorMessages(Collections.emptyMap())
            .errorCode("test-errorCode")
            .context(Collections.emptyMap())
            .validatedBy("test-validatedBy")
            .executionTimeMs(42L)
            .batchId("test-batchId")
            .tenantId("test-tenantId")
            .metadata(Collections.emptyMap())
            .build();
        ValidationResultDTO dto2 = ValidationResultDTO.builder()
                        .id("test-id")
            .validationId("test-validationId")
            .entityType("test-entityType")
            .entityId("test-entityId")
            .entityData("test-entityData")
            .ruleCode("test-ruleCode")
            .ruleName("test-ruleName")
            .passed(true)
            .fieldName("test-fieldName")
            .actualValue("test-actualValue")
            .expectedValue("test-expectedValue")
            .errorMessage("test-errorMessage")
            .localizedErrorMessages(Collections.emptyMap())
            .errorCode("test-errorCode")
            .context(Collections.emptyMap())
            .validatedBy("test-validatedBy")
            .executionTimeMs(42L)
            .batchId("test-batchId")
            .tenantId("test-tenantId")
            .metadata(Collections.emptyMap())
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        ValidationResultDTO dto = ValidationResultDTO.builder()
                        .id("test-id")
            .validationId("test-validationId")
            .entityType("test-entityType")
            .entityId("test-entityId")
            .entityData("test-entityData")
            .ruleCode("test-ruleCode")
            .ruleName("test-ruleName")
            .passed(true)
            .fieldName("test-fieldName")
            .actualValue("test-actualValue")
            .expectedValue("test-expectedValue")
            .errorMessage("test-errorMessage")
            .localizedErrorMessages(Collections.emptyMap())
            .errorCode("test-errorCode")
            .context(Collections.emptyMap())
            .validatedBy("test-validatedBy")
            .executionTimeMs(42L)
            .batchId("test-batchId")
            .tenantId("test-tenantId")
            .metadata(Collections.emptyMap())
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}