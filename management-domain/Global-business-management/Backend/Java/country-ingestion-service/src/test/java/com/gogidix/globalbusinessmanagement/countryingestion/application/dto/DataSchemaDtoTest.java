package com.gogidix.globalbusinessmanagement.countryingestion.application.dto;

import com.gogidix.globalbusinessmanagement.countryingestion.application.dto.DataSchemaDto;
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
class DataSchemaDtoTest {

        @Test
    void testBuilder() {
        DataSchemaDto dto = DataSchemaDto.builder()
                        .id("test-id")
            .schemaId("test-schemaId")
            .schemaName("test-schemaName")
            .description("test-description")
            .version("test-version")
            .active(true)
            .schemaType(DataSchemaDto.SchemaTypeDto.COUNTRY_DATA)
            .targetEntity("test-targetEntity")
            .fields(Collections.emptyList())
            .fieldMappings(Collections.emptyMap())
            .strictValidation(true)
            .allowUnknownFields(true)
            .stopOnFirstError(true)
            .createdBy("test-createdBy")
            .createdDate(LocalDateTime.of(2025,1,15,10,0))
            .lastModifiedBy("test-lastModifiedBy")
            .lastModifiedDate(LocalDateTime.of(2025,1,15,10,0))
            .organizationId("test-organizationId")
            .tenantId("test-tenantId")
            .metadata(Collections.emptyMap())
            .usageCount(42)
            .build();
        assertNotNull(dto);
        assertEquals("test-id", dto.getId());
        assertEquals("test-schemaId", dto.getSchemaId());
        assertEquals("test-schemaName", dto.getSchemaName());
        assertEquals("test-description", dto.getDescription());
        assertEquals("test-version", dto.getVersion());
        assertTrue(dto.getActive());
        assertEquals(DataSchemaDto.SchemaTypeDto.COUNTRY_DATA, dto.getSchemaType());
        assertEquals("test-targetEntity", dto.getTargetEntity());
        assertTrue(dto.getStrictValidation());
        assertTrue(dto.getAllowUnknownFields());
        assertTrue(dto.getStopOnFirstError());
        assertEquals("test-createdBy", dto.getCreatedBy());
        assertEquals("test-lastModifiedBy", dto.getLastModifiedBy());
        assertEquals("test-organizationId", dto.getOrganizationId());
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals(42, dto.getUsageCount());
    }

    @Test
    void testSettersAndGetters() {
        DataSchemaDto dto = new DataSchemaDto();
        dto.setId("val-id");
        dto.setSchemaId("val-schemaId");
        dto.setSchemaName("val-schemaName");
        dto.setDescription("val-description");
        dto.setVersion("val-version");
        dto.setActive(true);
        dto.setSchemaType(DataSchemaDto.SchemaTypeDto.COUNTRY_DATA);
        dto.setTargetEntity("val-targetEntity");
        dto.setStrictValidation(true);
        dto.setAllowUnknownFields(true);
        dto.setStopOnFirstError(true);
        dto.setCreatedBy("val-createdBy");
        dto.setLastModifiedBy("val-lastModifiedBy");
        dto.setOrganizationId("val-organizationId");
        dto.setTenantId("val-tenantId");
        dto.setUsageCount(99);
        assertEquals("val-id", dto.getId());
        assertEquals("val-schemaId", dto.getSchemaId());
        assertEquals("val-schemaName", dto.getSchemaName());
        assertEquals("val-description", dto.getDescription());
        assertEquals("val-version", dto.getVersion());
        assertTrue(dto.getActive());
        assertEquals(DataSchemaDto.SchemaTypeDto.COUNTRY_DATA, dto.getSchemaType());
        assertEquals("val-targetEntity", dto.getTargetEntity());
        assertTrue(dto.getStrictValidation());
        assertTrue(dto.getAllowUnknownFields());
        assertTrue(dto.getStopOnFirstError());
        assertEquals("val-createdBy", dto.getCreatedBy());
        assertEquals("val-lastModifiedBy", dto.getLastModifiedBy());
        assertEquals("val-organizationId", dto.getOrganizationId());
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals(99, dto.getUsageCount());
    }

    @Test
    void testEqualsAndHashCode() {
        DataSchemaDto dto1 = DataSchemaDto.builder()
                        .id("test-id")
            .schemaId("test-schemaId")
            .schemaName("test-schemaName")
            .description("test-description")
            .version("test-version")
            .active(true)
            .schemaType(DataSchemaDto.SchemaTypeDto.COUNTRY_DATA)
            .targetEntity("test-targetEntity")
            .fields(Collections.emptyList())
            .fieldMappings(Collections.emptyMap())
            .strictValidation(true)
            .allowUnknownFields(true)
            .stopOnFirstError(true)
            .createdBy("test-createdBy")
            .createdDate(LocalDateTime.of(2025,1,15,10,0))
            .lastModifiedBy("test-lastModifiedBy")
            .lastModifiedDate(LocalDateTime.of(2025,1,15,10,0))
            .organizationId("test-organizationId")
            .tenantId("test-tenantId")
            .metadata(Collections.emptyMap())
            .usageCount(42)
            .build();
        DataSchemaDto dto2 = DataSchemaDto.builder()
                        .id("test-id")
            .schemaId("test-schemaId")
            .schemaName("test-schemaName")
            .description("test-description")
            .version("test-version")
            .active(true)
            .schemaType(DataSchemaDto.SchemaTypeDto.COUNTRY_DATA)
            .targetEntity("test-targetEntity")
            .fields(Collections.emptyList())
            .fieldMappings(Collections.emptyMap())
            .strictValidation(true)
            .allowUnknownFields(true)
            .stopOnFirstError(true)
            .createdBy("test-createdBy")
            .createdDate(LocalDateTime.of(2025,1,15,10,0))
            .lastModifiedBy("test-lastModifiedBy")
            .lastModifiedDate(LocalDateTime.of(2025,1,15,10,0))
            .organizationId("test-organizationId")
            .tenantId("test-tenantId")
            .metadata(Collections.emptyMap())
            .usageCount(42)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        DataSchemaDto dto = DataSchemaDto.builder()
                        .id("test-id")
            .schemaId("test-schemaId")
            .schemaName("test-schemaName")
            .description("test-description")
            .version("test-version")
            .active(true)
            .schemaType(DataSchemaDto.SchemaTypeDto.COUNTRY_DATA)
            .targetEntity("test-targetEntity")
            .fields(Collections.emptyList())
            .fieldMappings(Collections.emptyMap())
            .strictValidation(true)
            .allowUnknownFields(true)
            .stopOnFirstError(true)
            .createdBy("test-createdBy")
            .createdDate(LocalDateTime.of(2025,1,15,10,0))
            .lastModifiedBy("test-lastModifiedBy")
            .lastModifiedDate(LocalDateTime.of(2025,1,15,10,0))
            .organizationId("test-organizationId")
            .tenantId("test-tenantId")
            .metadata(Collections.emptyMap())
            .usageCount(42)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}