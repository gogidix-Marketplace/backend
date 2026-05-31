package com.gogidix.globalbusinessmanagement.countryingestion.application.dto;

import com.gogidix.globalbusinessmanagement.countryingestion.application.dto.ValidationErrorDto;
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
class ValidationErrorDtoTest {

        @Test
    void testBuilder() {
        ValidationErrorDto dto = ValidationErrorDto.builder()
                        .id("test-id")
            .errorId("test-errorId")
            .batchId("test-batchId")
            .recordId("test-recordId")
            .countryCode("test-countryCode")
            .errorLevel(ValidationErrorDto.ErrorLevelDto.CRITICAL)
            .errorCode("test-errorCode")
            .errorMessage("test-errorMessage")
            .detailedMessage("test-detailedMessage")
            .fieldName("test-fieldName")
            .fieldValue(null)
            .fieldType("test-fieldType")
            .errorType(ValidationErrorDto.ErrorTypeDto.MISSING_REQUIRED_FIELD)
            .rowNumber(42)
            .columnNumber(42)
            .errorTimestamp(LocalDateTime.of(2025,1,15,10,0))
            .status(ValidationErrorDto.ErrorStatusDto.OPEN)
            .resolvedDate(LocalDateTime.of(2025,1,15,10,0))
            .resolvedBy("test-resolvedBy")
            .resolutionNotes("test-resolutionNotes")
            .correctedValue("test-correctedValue")
            .autoCorrected(true)
            .correctionRuleApplied("test-correctionRuleApplied")
            .suppress(true)
            .suppressedBy("test-suppressedBy")
            .suppressedDate(LocalDateTime.of(2025,1,15,10,0))
            .suppressionReason("test-suppressionReason")
            .assignedTo("test-assignedTo")
            .priority(42)
            .occurrences(42)
            .organizationId("test-organizationId")
            .tenantId("test-tenantId")
            .context(Collections.emptyMap())
            .suggestion("test-suggestion")
            .validationRule("test-validationRule")
            .build();
        assertNotNull(dto);
        assertEquals("test-id", dto.getId());
        assertEquals("test-errorId", dto.getErrorId());
        assertEquals("test-batchId", dto.getBatchId());
        assertEquals("test-recordId", dto.getRecordId());
        assertEquals("test-countryCode", dto.getCountryCode());
        assertEquals(ValidationErrorDto.ErrorLevelDto.CRITICAL, dto.getErrorLevel());
        assertEquals("test-errorCode", dto.getErrorCode());
        assertEquals("test-errorMessage", dto.getErrorMessage());
        assertEquals("test-detailedMessage", dto.getDetailedMessage());
        assertEquals("test-fieldName", dto.getFieldName());
        assertEquals("test-fieldType", dto.getFieldType());
        assertEquals(ValidationErrorDto.ErrorTypeDto.MISSING_REQUIRED_FIELD, dto.getErrorType());
        assertEquals(42, dto.getRowNumber());
        assertEquals(42, dto.getColumnNumber());
        assertEquals(ValidationErrorDto.ErrorStatusDto.OPEN, dto.getStatus());
        assertEquals("test-resolvedBy", dto.getResolvedBy());
        assertEquals("test-resolutionNotes", dto.getResolutionNotes());
        assertEquals("test-correctedValue", dto.getCorrectedValue());
        assertTrue(dto.getAutoCorrected());
        assertEquals("test-correctionRuleApplied", dto.getCorrectionRuleApplied());
        assertTrue(dto.getSuppress());
        assertEquals("test-suppressedBy", dto.getSuppressedBy());
        assertEquals("test-suppressionReason", dto.getSuppressionReason());
        assertEquals("test-assignedTo", dto.getAssignedTo());
        assertEquals(42, dto.getPriority());
        assertEquals(42, dto.getOccurrences());
        assertEquals("test-organizationId", dto.getOrganizationId());
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-suggestion", dto.getSuggestion());
        assertEquals("test-validationRule", dto.getValidationRule());
    }

    @Test
    void testSettersAndGetters() {
        ValidationErrorDto dto = new ValidationErrorDto();
        dto.setId("val-id");
        dto.setErrorId("val-errorId");
        dto.setBatchId("val-batchId");
        dto.setRecordId("val-recordId");
        dto.setCountryCode("val-countryCode");
        dto.setErrorLevel(ValidationErrorDto.ErrorLevelDto.CRITICAL);
        dto.setErrorCode("val-errorCode");
        dto.setErrorMessage("val-errorMessage");
        dto.setDetailedMessage("val-detailedMessage");
        dto.setFieldName("val-fieldName");
        dto.setFieldType("val-fieldType");
        dto.setErrorType(ValidationErrorDto.ErrorTypeDto.MISSING_REQUIRED_FIELD);
        dto.setRowNumber(99);
        dto.setColumnNumber(99);
        dto.setStatus(ValidationErrorDto.ErrorStatusDto.OPEN);
        dto.setResolvedBy("val-resolvedBy");
        dto.setResolutionNotes("val-resolutionNotes");
        dto.setCorrectedValue("val-correctedValue");
        dto.setAutoCorrected(true);
        dto.setCorrectionRuleApplied("val-correctionRuleApplied");
        dto.setSuppress(true);
        dto.setSuppressedBy("val-suppressedBy");
        dto.setSuppressionReason("val-suppressionReason");
        dto.setAssignedTo("val-assignedTo");
        dto.setPriority(99);
        dto.setOccurrences(99);
        dto.setOrganizationId("val-organizationId");
        dto.setTenantId("val-tenantId");
        dto.setSuggestion("val-suggestion");
        dto.setValidationRule("val-validationRule");
        assertEquals("val-id", dto.getId());
        assertEquals("val-errorId", dto.getErrorId());
        assertEquals("val-batchId", dto.getBatchId());
        assertEquals("val-recordId", dto.getRecordId());
        assertEquals("val-countryCode", dto.getCountryCode());
        assertEquals(ValidationErrorDto.ErrorLevelDto.CRITICAL, dto.getErrorLevel());
        assertEquals("val-errorCode", dto.getErrorCode());
        assertEquals("val-errorMessage", dto.getErrorMessage());
        assertEquals("val-detailedMessage", dto.getDetailedMessage());
        assertEquals("val-fieldName", dto.getFieldName());
        assertEquals("val-fieldType", dto.getFieldType());
        assertEquals(ValidationErrorDto.ErrorTypeDto.MISSING_REQUIRED_FIELD, dto.getErrorType());
        assertEquals(99, dto.getRowNumber());
        assertEquals(99, dto.getColumnNumber());
        assertEquals(ValidationErrorDto.ErrorStatusDto.OPEN, dto.getStatus());
        assertEquals("val-resolvedBy", dto.getResolvedBy());
        assertEquals("val-resolutionNotes", dto.getResolutionNotes());
        assertEquals("val-correctedValue", dto.getCorrectedValue());
        assertTrue(dto.getAutoCorrected());
        assertEquals("val-correctionRuleApplied", dto.getCorrectionRuleApplied());
        assertTrue(dto.getSuppress());
        assertEquals("val-suppressedBy", dto.getSuppressedBy());
        assertEquals("val-suppressionReason", dto.getSuppressionReason());
        assertEquals("val-assignedTo", dto.getAssignedTo());
        assertEquals(99, dto.getPriority());
        assertEquals(99, dto.getOccurrences());
        assertEquals("val-organizationId", dto.getOrganizationId());
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-suggestion", dto.getSuggestion());
        assertEquals("val-validationRule", dto.getValidationRule());
    }

    @Test
    void testEqualsAndHashCode() {
        ValidationErrorDto dto1 = ValidationErrorDto.builder()
                        .id("test-id")
            .errorId("test-errorId")
            .batchId("test-batchId")
            .recordId("test-recordId")
            .countryCode("test-countryCode")
            .errorLevel(ValidationErrorDto.ErrorLevelDto.CRITICAL)
            .errorCode("test-errorCode")
            .errorMessage("test-errorMessage")
            .detailedMessage("test-detailedMessage")
            .fieldName("test-fieldName")
            .fieldValue(null)
            .fieldType("test-fieldType")
            .errorType(ValidationErrorDto.ErrorTypeDto.MISSING_REQUIRED_FIELD)
            .rowNumber(42)
            .columnNumber(42)
            .errorTimestamp(LocalDateTime.of(2025,1,15,10,0))
            .status(ValidationErrorDto.ErrorStatusDto.OPEN)
            .resolvedDate(LocalDateTime.of(2025,1,15,10,0))
            .resolvedBy("test-resolvedBy")
            .resolutionNotes("test-resolutionNotes")
            .correctedValue("test-correctedValue")
            .autoCorrected(true)
            .correctionRuleApplied("test-correctionRuleApplied")
            .suppress(true)
            .suppressedBy("test-suppressedBy")
            .suppressedDate(LocalDateTime.of(2025,1,15,10,0))
            .suppressionReason("test-suppressionReason")
            .assignedTo("test-assignedTo")
            .priority(42)
            .occurrences(42)
            .organizationId("test-organizationId")
            .tenantId("test-tenantId")
            .context(Collections.emptyMap())
            .suggestion("test-suggestion")
            .validationRule("test-validationRule")
            .build();
        ValidationErrorDto dto2 = ValidationErrorDto.builder()
                        .id("test-id")
            .errorId("test-errorId")
            .batchId("test-batchId")
            .recordId("test-recordId")
            .countryCode("test-countryCode")
            .errorLevel(ValidationErrorDto.ErrorLevelDto.CRITICAL)
            .errorCode("test-errorCode")
            .errorMessage("test-errorMessage")
            .detailedMessage("test-detailedMessage")
            .fieldName("test-fieldName")
            .fieldValue(null)
            .fieldType("test-fieldType")
            .errorType(ValidationErrorDto.ErrorTypeDto.MISSING_REQUIRED_FIELD)
            .rowNumber(42)
            .columnNumber(42)
            .errorTimestamp(LocalDateTime.of(2025,1,15,10,0))
            .status(ValidationErrorDto.ErrorStatusDto.OPEN)
            .resolvedDate(LocalDateTime.of(2025,1,15,10,0))
            .resolvedBy("test-resolvedBy")
            .resolutionNotes("test-resolutionNotes")
            .correctedValue("test-correctedValue")
            .autoCorrected(true)
            .correctionRuleApplied("test-correctionRuleApplied")
            .suppress(true)
            .suppressedBy("test-suppressedBy")
            .suppressedDate(LocalDateTime.of(2025,1,15,10,0))
            .suppressionReason("test-suppressionReason")
            .assignedTo("test-assignedTo")
            .priority(42)
            .occurrences(42)
            .organizationId("test-organizationId")
            .tenantId("test-tenantId")
            .context(Collections.emptyMap())
            .suggestion("test-suggestion")
            .validationRule("test-validationRule")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        ValidationErrorDto dto = ValidationErrorDto.builder()
                        .id("test-id")
            .errorId("test-errorId")
            .batchId("test-batchId")
            .recordId("test-recordId")
            .countryCode("test-countryCode")
            .errorLevel(ValidationErrorDto.ErrorLevelDto.CRITICAL)
            .errorCode("test-errorCode")
            .errorMessage("test-errorMessage")
            .detailedMessage("test-detailedMessage")
            .fieldName("test-fieldName")
            .fieldValue(null)
            .fieldType("test-fieldType")
            .errorType(ValidationErrorDto.ErrorTypeDto.MISSING_REQUIRED_FIELD)
            .rowNumber(42)
            .columnNumber(42)
            .errorTimestamp(LocalDateTime.of(2025,1,15,10,0))
            .status(ValidationErrorDto.ErrorStatusDto.OPEN)
            .resolvedDate(LocalDateTime.of(2025,1,15,10,0))
            .resolvedBy("test-resolvedBy")
            .resolutionNotes("test-resolutionNotes")
            .correctedValue("test-correctedValue")
            .autoCorrected(true)
            .correctionRuleApplied("test-correctionRuleApplied")
            .suppress(true)
            .suppressedBy("test-suppressedBy")
            .suppressedDate(LocalDateTime.of(2025,1,15,10,0))
            .suppressionReason("test-suppressionReason")
            .assignedTo("test-assignedTo")
            .priority(42)
            .occurrences(42)
            .organizationId("test-organizationId")
            .tenantId("test-tenantId")
            .context(Collections.emptyMap())
            .suggestion("test-suggestion")
            .validationRule("test-validationRule")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}