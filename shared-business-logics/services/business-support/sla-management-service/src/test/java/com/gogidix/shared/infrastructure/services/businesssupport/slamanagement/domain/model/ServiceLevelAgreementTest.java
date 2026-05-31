package com.gogidix.shared.infrastructure.services.businesssupport.slamanagement.domain.model;

import com.gogidix.shared.multitenancy.model.TenantId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for ServiceLevelAgreement domain model.
 */
@DisplayName("ServiceLevelAgreement Domain Model Tests")
class ServiceLevelAgreementTest {

    @Test
    @DisplayName("Should create SLA with parameterized constructor")
    void shouldCreateSLAWithParameterizedConstructor() {
        TenantId tenantId = TenantId.of("tenant-001");
        LocalDateTime validFrom = LocalDateTime.now();
        LocalDateTime validUntil = LocalDateTime.now().plusYears(1);

        ServiceLevelAgreement sla = new ServiceLevelAgreement(
                tenantId,
                "Premium SLA",
                "Premium service level agreement",
                "API",
                500.0,
                99.9,
                10,
                validFrom,
                validUntil
        );

        assertEquals(tenantId, sla.getTenantId());
        assertEquals("Premium SLA", sla.getName());
        assertEquals("Premium service level agreement", sla.getDescription());
        assertEquals("API", sla.getServiceType());
        assertEquals(500.0, sla.getResponseTimeThreshold());
        assertEquals(99.9, sla.getUptimePercentage());
        assertEquals(10, sla.getPenaltyPercentage());
        assertEquals(validFrom, sla.getValidFrom());
        assertEquals(validUntil, sla.getValidUntil());
        assertEquals("ACTIVE", sla.getStatus());
    }

    @Test
    @DisplayName("Should create SLA with protected no args constructor")
    void shouldCreateSLAWithNoArgsConstructor() {
        ServiceLevelAgreement sla = new ServiceLevelAgreement();

        assertNull(sla.getTenantId());
        assertNull(sla.getId());
        assertNull(sla.getName());
        assertNull(sla.getDescription());
        assertNull(sla.getServiceType());
        assertEquals(0.0, sla.getResponseTimeThreshold());
        assertEquals(0.0, sla.getUptimePercentage());
        assertEquals(0, sla.getPenaltyPercentage());
        assertNull(sla.getStatus());
        assertNull(sla.getCreatedAt());
        assertNull(sla.getUpdatedAt());
        assertNull(sla.getValidFrom());
        assertNull(sla.getValidUntil());
    }

    @Test
    @DisplayName("Should set and get all properties correctly")
    void shouldSetAndGetAllProperties() {
        ServiceLevelAgreement sla = new ServiceLevelAgreement();
        TenantId tenantId = TenantId.of("tenant-456");
        LocalDateTime now = LocalDateTime.now();

        sla.setTenantId(tenantId);
        sla.setId("sla-123");
        sla.setName("Basic SLA");
        sla.setDescription("Basic service level");
        sla.setServiceType("WEB");
        sla.setResponseTimeThreshold(1000.0);
        sla.setUptimePercentage(99.5);
        sla.setPenaltyPercentage(5);
        sla.setStatus("INACTIVE");
        sla.setCreatedAt(now);
        sla.setUpdatedAt(now);
        sla.setValidFrom(now);
        sla.setValidUntil(now.plusMonths(6));

        assertEquals(tenantId, sla.getTenantId());
        assertEquals("sla-123", sla.getId());
        assertEquals("Basic SLA", sla.getName());
        assertEquals("Basic service level", sla.getDescription());
        assertEquals("WEB", sla.getServiceType());
        assertEquals(1000.0, sla.getResponseTimeThreshold());
        assertEquals(99.5, sla.getUptimePercentage());
        assertEquals(5, sla.getPenaltyPercentage());
        assertEquals("INACTIVE", sla.getStatus());
        assertEquals(now, sla.getCreatedAt());
        assertEquals(now, sla.getUpdatedAt());
        assertEquals(now, sla.getValidFrom());
        assertEquals(now.plusMonths(6), sla.getValidUntil());
    }

    @Test
    @DisplayName("Should initialize status to ACTIVE in constructor")
    void shouldInitializeStatusToActiveInConstructor() {
        TenantId tenantId = TenantId.of("tenant-001");

        ServiceLevelAgreement sla = new ServiceLevelAgreement(
                tenantId,
                "Test SLA",
                "Description",
                "API",
                100.0,
                99.0,
                5,
                LocalDateTime.now(),
                LocalDateTime.now().plusMonths(1)
        );

        assertEquals("ACTIVE", sla.getStatus());
    }

    @Test
    @DisplayName("Should update status")
    void shouldUpdateStatus() {
        ServiceLevelAgreement sla = new ServiceLevelAgreement();
        sla.setStatus("ACTIVE");

        assertEquals("ACTIVE", sla.getStatus());

        sla.setStatus("EXPIRED");

        assertEquals("EXPIRED", sla.getStatus());
    }

    @Test
    @DisplayName("Should handle various status values")
    void shouldHandleVariousStatusValues() {
        ServiceLevelAgreement sla = new ServiceLevelAgreement();

        sla.setStatus("ACTIVE");
        assertEquals("ACTIVE", sla.getStatus());

        sla.setStatus("INACTIVE");
        assertEquals("INACTIVE", sla.getStatus());

        sla.setStatus("EXPIRED");
        assertEquals("EXPIRED", sla.getStatus());
    }

    @Test
    @DisplayName("Should set null tenant ID")
    void shouldSetNullTenantId() {
        ServiceLevelAgreement sla = new ServiceLevelAgreement();
        sla.setTenantId(null);

        assertNull(sla.getTenantId());
    }

    @Test
    @DisplayName("Should handle null optional fields")
    void shouldHandleNullOptionalFields() {
        ServiceLevelAgreement sla = new ServiceLevelAgreement(
                TenantId.of("tenant-001"),
                null,
                null,
                null,
                0.0,
                0.0,
                0,
                null,
                null
        );

        assertNull(sla.getName());
        assertNull(sla.getDescription());
        assertNull(sla.getServiceType());
        assertNull(sla.getValidFrom());
        assertNull(sla.getValidUntil());
    }

    @Test
    @DisplayName("Should maintain tenant ID reference")
    void shouldMaintainTenantIdReference() {
        TenantId tenantId = TenantId.of("tenant-789");
        ServiceLevelAgreement sla = new ServiceLevelAgreement(
                tenantId,
                "SLA",
                "Desc",
                "API",
                100.0,
                99.0,
                5,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        // Same reference
        assertSame(tenantId, sla.getTenantId());
    }

    @Test
    @DisplayName("Should handle edge case values")
    void shouldHandleEdgeCaseValues() {
        ServiceLevelAgreement sla = new ServiceLevelAgreement();

        sla.setResponseTimeThreshold(0.0);
        sla.setUptimePercentage(0.0);
        sla.setPenaltyPercentage(0);

        assertEquals(0.0, sla.getResponseTimeThreshold());
        assertEquals(0.0, sla.getUptimePercentage());
        assertEquals(0, sla.getPenaltyPercentage());

        sla.setResponseTimeThreshold(Double.MAX_VALUE);
        sla.setUptimePercentage(100.0);
        sla.setPenaltyPercentage(100);

        assertEquals(Double.MAX_VALUE, sla.getResponseTimeThreshold());
        assertEquals(100.0, sla.getUptimePercentage());
        assertEquals(100, sla.getPenaltyPercentage());
    }

    @Test
    @DisplayName("Should update timestamp fields")
    void shouldUpdateTimestampFields() {
        ServiceLevelAgreement sla = new ServiceLevelAgreement();
        LocalDateTime initialTime = LocalDateTime.now().minusDays(1);
        LocalDateTime updatedTime = LocalDateTime.now();

        sla.setCreatedAt(initialTime);
        sla.setUpdatedAt(updatedTime);

        assertEquals(initialTime, sla.getCreatedAt());
        assertEquals(updatedTime, sla.getUpdatedAt());

        // Update again
        LocalDateTime newTime = LocalDateTime.now().plusHours(1);
        sla.setUpdatedAt(newTime);
        assertEquals(newTime, sla.getUpdatedAt());
        assertEquals(initialTime, sla.getCreatedAt());
    }

    @Test
    @DisplayName("Should handle date range correctly")
    void shouldHandleDateRangeCorrectly() {
        LocalDateTime now = LocalDateTime.now();
        ServiceLevelAgreement sla = new ServiceLevelAgreement(
                TenantId.of("tenant-001"),
                "SLA",
                "Desc",
                "API",
                100.0,
                99.0,
                5,
                now,
                now.plusYears(1)
        );

        assertTrue(sla.getValidFrom().isBefore(sla.getValidUntil()));
        assertEquals(365, java.time.temporal.ChronoUnit.DAYS.between(sla.getValidFrom(), sla.getValidUntil()));
    }

    @Test
    @DisplayName("Should create multiple SLAs independently")
    void shouldCreateMultipleSLAsIndependently() {
        TenantId tenant1 = TenantId.of("tenant-001");
        TenantId tenant2 = TenantId.of("tenant-002");

        ServiceLevelAgreement sla1 = new ServiceLevelAgreement(
                tenant1,
                "SLA 1",
                "Description 1",
                "API",
                100.0,
                99.0,
                5,
                LocalDateTime.now(),
                LocalDateTime.now().plusMonths(12)
        );

        ServiceLevelAgreement sla2 = new ServiceLevelAgreement(
                tenant2,
                "SLA 2",
                "Description 2",
                "WEB",
                200.0,
                99.9,
                10,
                LocalDateTime.now(),
                LocalDateTime.now().plusMonths(24)
        );

        assertEquals("tenant-001", sla1.getTenantId().getValue());
        assertEquals("tenant-002", sla2.getTenantId().getValue());
        assertEquals("SLA 1", sla1.getName());
        assertEquals("SLA 2", sla2.getName());
        assertEquals("API", sla1.getServiceType());
        assertEquals("WEB", sla2.getServiceType());
        assertEquals(100.0, sla1.getResponseTimeThreshold());
        assertEquals(200.0, sla2.getResponseTimeThreshold());
    }

    @Test
    @DisplayName("Should handle fractional response time threshold")
    void shouldHandleFractionalResponseTimeThreshold() {
        ServiceLevelAgreement sla = new ServiceLevelAgreement();
        sla.setResponseTimeThreshold(123.456);

        assertEquals(123.456, sla.getResponseTimeThreshold(), 0.001);
    }

    @Test
    @DisplayName("Should handle fractional uptime percentage")
    void shouldHandleFractionalUptimePercentage() {
        ServiceLevelAgreement sla = new ServiceLevelAgreement();
        sla.setUptimePercentage(99.99);

        assertEquals(99.99, sla.getUptimePercentage(), 0.001);
    }

    @Test
    @DisplayName("Should set valid from and until independently")
    void shouldSetValidFromAndUntilIndependently() {
        ServiceLevelAgreement sla = new ServiceLevelAgreement();
        LocalDateTime validFrom = LocalDateTime.of(2024, 1, 1, 0, 0);
        LocalDateTime validUntil = LocalDateTime.of(2024, 12, 31, 23, 59);

        sla.setValidFrom(validFrom);
        sla.setValidUntil(validUntil);

        assertEquals(validFrom, sla.getValidFrom());
        assertEquals(validUntil, sla.getValidUntil());
    }
}
