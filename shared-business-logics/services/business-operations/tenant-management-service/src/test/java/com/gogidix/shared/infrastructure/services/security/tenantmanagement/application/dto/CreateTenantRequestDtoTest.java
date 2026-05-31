package com.gogidix.shared.infrastructure.services.security.tenantmanagement.application.dto;

import com.gogidix.shared.infrastructure.services.security.tenantmanagement.application.dto.request.CreateTenantRequestDto;
import com.gogidix.shared.infrastructure.services.security.tenantmanagement.domain.model.Tenant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for CreateTenantRequestDto.
 */
@DisplayName("CreateTenantRequestDto Tests")
class CreateTenantRequestDtoTest {

    @Test
    @DisplayName("Should create request with builder")
    void shouldCreateRequestWithBuilder() {
        Map<String, Object> settings = new HashMap<>();
        settings.put("key", "value");

        CreateTenantRequestDto dto = CreateTenantRequestDto.builder()
                .tenantId("tenant-001")
                .name("Test Tenant")
                .domain("test.com")
                .logoUrl("logo.png")
                .status(Tenant.TenantStatus.ACTIVE)
                .plan(Tenant.TenantPlan.PROFESSIONAL)
                .trialEndsAt(LocalDateTime.now())
                .settings(settings)
                .primaryContactEmail("admin@test.com")
                .primaryContactName("Admin")
                .maxUsers(100L)
                .maxStorageGB(50L)
                .build();

        assertEquals("tenant-001", dto.getTenantId());
        assertEquals("Test Tenant", dto.getName());
        assertEquals("test.com", dto.getDomain());
        assertEquals("logo.png", dto.getLogoUrl());
        assertEquals(Tenant.TenantStatus.ACTIVE, dto.getStatus());
        assertEquals(Tenant.TenantPlan.PROFESSIONAL, dto.getPlan());
        assertEquals("admin@test.com", dto.getPrimaryContactEmail());
        assertEquals("Admin", dto.getPrimaryContactName());
        assertEquals(100L, dto.getMaxUsers());
        assertEquals(50L, dto.getMaxStorageGB());
        assertEquals(settings, dto.getSettings());
    }

    @Test
    @DisplayName("Should create request with no args constructor")
    void shouldCreateRequestWithNoArgsConstructor() {
        CreateTenantRequestDto dto = new CreateTenantRequestDto();

        assertNull(dto.getTenantId());
        assertNull(dto.getName());
        assertNull(dto.getDomain());
        assertNull(dto.getLogoUrl());
        assertNull(dto.getStatus());
        assertNull(dto.getPlan());
        assertNull(dto.getTrialEndsAt());
        assertNull(dto.getSettings());
        assertNull(dto.getFeatures());
        assertNull(dto.getPrimaryContactEmail());
        assertNull(dto.getPrimaryContactName());
        assertNull(dto.getMaxUsers());
        assertNull(dto.getMaxStorageGB());
    }

    @Test
    @DisplayName("Should set and get all properties")
    void shouldSetAndGetAllProperties() {
        CreateTenantRequestDto dto = new CreateTenantRequestDto();
        Map<String, Object> settings = new HashMap<>();
        Map<String, Object> features = new HashMap<>();

        dto.setTenantId("tenant-123");
        dto.setName("New Tenant");
        dto.setDomain("new.com");
        dto.setLogoUrl("new-logo.png");
        dto.setStatus(Tenant.TenantStatus.TRIAL);
        dto.setPlan(Tenant.TenantPlan.FREE);
        dto.setTrialEndsAt(LocalDateTime.now());
        dto.setSettings(settings);
        dto.setFeatures(features);
        dto.setPrimaryContactEmail("contact@new.com");
        dto.setPrimaryContactName("Contact");
        dto.setMaxUsers(200L);
        dto.setMaxStorageGB(100L);

        assertEquals("tenant-123", dto.getTenantId());
        assertEquals("New Tenant", dto.getName());
        assertEquals("new.com", dto.getDomain());
        assertEquals("new-logo.png", dto.getLogoUrl());
        assertEquals(Tenant.TenantStatus.TRIAL, dto.getStatus());
        assertEquals(Tenant.TenantPlan.FREE, dto.getPlan());
        assertEquals(settings, dto.getSettings());
        assertEquals(features, dto.getFeatures());
        assertEquals("contact@new.com", dto.getPrimaryContactEmail());
        assertEquals("Contact", dto.getPrimaryContactName());
        assertEquals(200L, dto.getMaxUsers());
        assertEquals(100L, dto.getMaxStorageGB());
    }

    @Test
    @DisplayName("Should handle all args constructor")
    void shouldHandleAllArgsConstructor() {
        LocalDateTime trialEndsAt = LocalDateTime.now();
        Map<String, Object> settings = new HashMap<>();
        Map<String, Object> features = new HashMap<>();

        CreateTenantRequestDto dto = new CreateTenantRequestDto(
                "tenant-001",
                "Test Tenant",
                "test.com",
                "logo.png",
                Tenant.TenantStatus.ACTIVE,
                Tenant.TenantPlan.ENTERPRISE,
                trialEndsAt,
                settings,
                features,
                "admin@test.com",
                "Admin",
                100L,
                50L
        );

        assertEquals("tenant-001", dto.getTenantId());
        assertEquals("Test Tenant", dto.getName());
        assertEquals("test.com", dto.getDomain());
        assertEquals("logo.png", dto.getLogoUrl());
        assertEquals(Tenant.TenantStatus.ACTIVE, dto.getStatus());
        assertEquals(Tenant.TenantPlan.ENTERPRISE, dto.getPlan());
        assertEquals(trialEndsAt, dto.getTrialEndsAt());
        assertEquals(settings, dto.getSettings());
        assertEquals(features, dto.getFeatures());
        assertEquals("admin@test.com", dto.getPrimaryContactEmail());
        assertEquals("Admin", dto.getPrimaryContactName());
        assertEquals(100L, dto.getMaxUsers());
        assertEquals(50L, dto.getMaxStorageGB());
    }
}
