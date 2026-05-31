package com.gogidix.shared.infrastructure.services.businesssupport.slamanagement.application.mapper;

import com.gogidix.shared.infrastructure.core.tenancy.model.TenantId;
import com.gogidix.shared.infrastructure.services.businesssupport.slamanagement.application.dto.request.CreateServiceLevelAgreementRequestDto;
import com.gogidix.shared.infrastructure.services.businesssupport.slamanagement.application.dto.request.UpdateServiceLevelAgreementRequestDto;
import com.gogidix.shared.infrastructure.services.businesssupport.slamanagement.application.dto.response.ServiceLevelAgreementResponseDto;
import com.gogidix.shared.infrastructure.services.businesssupport.slamanagement.domain.model.ServiceLevelAgreement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ServiceLevelAgreementMapper Tests")
class ServiceLevelAgreementMapperTest {

    private ServiceLevelAgreementMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new ServiceLevelAgreementMapper();
    }

    @Test
    @DisplayName("Should map CreateDto to entity")
    void shouldMapCreateDtoToEntity() {
        LocalDateTime now = LocalDateTime.now();
        CreateServiceLevelAgreementRequestDto dto = new CreateServiceLevelAgreementRequestDto(
                "Test SLA", "Desc", "API", 200.0, 99.9, 5, now, now.plusYears(1));

        ServiceLevelAgreement entity = mapper.toEntity(dto, "tenant-1");

        assertNotNull(entity);
        assertEquals("Test SLA", entity.getName());
        assertEquals("Desc", entity.getDescription());
        assertEquals("API", entity.getServiceType());
        assertEquals(200.0, entity.getResponseTimeThreshold());
        assertEquals(99.9, entity.getUptimePercentage());
        assertEquals(5, entity.getPenaltyPercentage());
        assertEquals("tenant-1", entity.getTenantId().getValue());
        assertEquals("ACTIVE", entity.getStatus());
    }

    @Test
    @DisplayName("Should map entity to response DTO")
    void shouldMapEntityToResponseDto() {
        LocalDateTime now = LocalDateTime.now();
        ServiceLevelAgreement entity = new ServiceLevelAgreement(
                TenantId.of("tenant-1"), "SLA", "Desc", "API",
                200.0, 99.9, 5, now, now.plusYears(1));
        entity.setId("sla-1");
        entity.setCreatedAt(now);
        entity.setUpdatedAt(now);

        ServiceLevelAgreementResponseDto dto = mapper.toResponseDto(entity);

        assertNotNull(dto);
        assertEquals("sla-1", dto.id());
        assertEquals("tenant-1", dto.tenantId());
        assertEquals("SLA", dto.name());
        assertEquals("Desc", dto.description());
        assertEquals("API", dto.serviceType());
        assertEquals(200.0, dto.responseTimeThreshold());
        assertEquals(99.9, dto.uptimePercentage());
        assertEquals(5, dto.penaltyPercentage());
        assertEquals("ACTIVE", dto.status());
    }

    @Test
    @DisplayName("Should handle null tenantId in toResponseDto")
    void shouldHandleNullTenantIdInToResponseDto() {
        ServiceLevelAgreement entity = new ServiceLevelAgreement(
                null, "SLA", "Desc", "API", 100.0, 99.0, 5,
                LocalDateTime.now(), LocalDateTime.now().plusYears(1));
        entity.setId("sla-1");

        ServiceLevelAgreementResponseDto dto = mapper.toResponseDto(entity);

        assertNull(dto.tenantId());
    }

    @Test
    @DisplayName("Should update entity from UpdateDto")
    void shouldUpdateEntityFromUpdateDto() {
        LocalDateTime now = LocalDateTime.now();
        ServiceLevelAgreement entity = new ServiceLevelAgreement(
                TenantId.of("t1"), "Old", "OldDesc", "API",
                100.0, 99.0, 5, now, now.plusYears(1));

        UpdateServiceLevelAgreementRequestDto dto = new UpdateServiceLevelAgreementRequestDto(
                "New", "NewDesc", "WEB", 200.0, 99.9, 10,
                now.plusDays(1), now.plusYears(2), "INACTIVE");

        mapper.updateEntity(entity, dto);

        assertEquals("New", entity.getName());
        assertEquals("NewDesc", entity.getDescription());
        assertEquals("WEB", entity.getServiceType());
        assertEquals(200.0, entity.getResponseTimeThreshold());
        assertEquals(99.9, entity.getUptimePercentage());
        assertEquals(10, entity.getPenaltyPercentage());
        assertEquals("INACTIVE", entity.getStatus());
        assertEquals(now.plusDays(1), entity.getValidFrom());
        assertEquals(now.plusYears(2), entity.getValidUntil());
    }

    @Test
    @DisplayName("Should not update null validFrom in updateEntity")
    void shouldNotUpdateNullValidFrom() {
        LocalDateTime original = LocalDateTime.now();
        ServiceLevelAgreement entity = new ServiceLevelAgreement(
                TenantId.of("t1"), "SLA", "Desc", "API",
                100.0, 99.0, 5, original, original.plusYears(1));

        UpdateServiceLevelAgreementRequestDto dto = new UpdateServiceLevelAgreementRequestDto(
                "SLA", "Desc", "API", 100.0, 99.0, 5,
                null, null, null);

        mapper.updateEntity(entity, dto);

        assertEquals(original, entity.getValidFrom());
        assertEquals(original.plusYears(1), entity.getValidUntil());
        assertEquals("ACTIVE", entity.getStatus());
    }
}
