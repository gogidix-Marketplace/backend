package com.gogidix.globalbusinessmanagement.datavalidation.domain.dto;

import com.gogidix.globalbusinessmanagement.datavalidation.domain.dto.ValidationRequestDTO;
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
class ValidationRequestDTOTest {

        @Test
    void testBuilder() {
        ValidationRequestDTO dto = ValidationRequestDTO.builder()
                        .entityType("test-entityType")
            .entityId("test-entityId")
            .entityData("test-entityData")
            .context(Collections.emptyMap())
            .tenantId("test-tenantId")
            .validatedBy("test-validatedBy")
            .async(true)
            .build();
        assertNotNull(dto);
        assertEquals("test-entityType", dto.getEntityType());
        assertEquals("test-entityId", dto.getEntityId());
        assertEquals("test-entityData", dto.getEntityData());
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-validatedBy", dto.getValidatedBy());
        assertTrue(dto.isAsync());
    }

    @Test
    void testSettersAndGetters() {
        ValidationRequestDTO dto = new ValidationRequestDTO();
        dto.setEntityType("val-entityType");
        dto.setEntityId("val-entityId");
        dto.setEntityData("val-entityData");
        dto.setTenantId("val-tenantId");
        dto.setValidatedBy("val-validatedBy");
        dto.setAsync(true);
        assertEquals("val-entityType", dto.getEntityType());
        assertEquals("val-entityId", dto.getEntityId());
        assertEquals("val-entityData", dto.getEntityData());
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-validatedBy", dto.getValidatedBy());
        assertTrue(dto.isAsync());
    }

    @Test
    void testEqualsAndHashCode() {
        ValidationRequestDTO dto1 = ValidationRequestDTO.builder()
                        .entityType("test-entityType")
            .entityId("test-entityId")
            .entityData("test-entityData")
            .context(Collections.emptyMap())
            .tenantId("test-tenantId")
            .validatedBy("test-validatedBy")
            .async(true)
            .build();
        ValidationRequestDTO dto2 = ValidationRequestDTO.builder()
                        .entityType("test-entityType")
            .entityId("test-entityId")
            .entityData("test-entityData")
            .context(Collections.emptyMap())
            .tenantId("test-tenantId")
            .validatedBy("test-validatedBy")
            .async(true)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        ValidationRequestDTO dto = ValidationRequestDTO.builder()
                        .entityType("test-entityType")
            .entityId("test-entityId")
            .entityData("test-entityData")
            .context(Collections.emptyMap())
            .tenantId("test-tenantId")
            .validatedBy("test-validatedBy")
            .async(true)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}