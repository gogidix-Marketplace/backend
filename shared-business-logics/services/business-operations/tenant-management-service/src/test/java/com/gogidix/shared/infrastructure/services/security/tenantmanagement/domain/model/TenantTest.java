package com.gogidix.shared.infrastructure.services.security.tenantmanagement.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tenant Domain Model Tests")
class TenantTest {

    @Nested
    @DisplayName("Builder and Fields")
    class BuilderTests {

        @Test
        void shouldBuildWithAllFields() {
            Tenant tenant = Tenant.builder()
                .id("1").tenantId("t1").name("Test").domain("test.com")
                .logoUrl("logo.png").status(Tenant.TenantStatus.ACTIVE)
                .plan(Tenant.TenantPlan.PROFESSIONAL)
                .trialEndsAt(LocalDateTime.now().plusDays(14))
                .settings(Map.of("key", "val"))
                .features(Map.of("f1", true))
                .primaryContactEmail("admin@test.com")
                .primaryContactName("Admin")
                .maxUsers(100).maxStorageGB(50)
                .createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now())
                .build();

            assertEquals("1", tenant.getId());
            assertEquals("t1", tenant.getTenantId());
            assertEquals("Test", tenant.getName());
            assertEquals("test.com", tenant.getDomain());
            assertEquals(Tenant.TenantStatus.ACTIVE, tenant.getStatus());
            assertEquals(Tenant.TenantPlan.PROFESSIONAL, tenant.getPlan());
            assertEquals(100, tenant.getMaxUsers());
            assertEquals(50, tenant.getMaxStorageGB());
        }

        @Test
        void shouldUseNoArgsConstructor() {
            Tenant tenant = new Tenant();
            assertNotNull(tenant);
            assertNull(tenant.getTenantId());
        }

        @Test
        void shouldUseAllArgsConstructor() {
            Tenant tenant = new Tenant("1", "t1", "Test", "test.com", "logo",
                Tenant.TenantStatus.ACTIVE, Tenant.TenantPlan.FREE, null, null, null,
                "e@t.com", "Admin", 10, 5, null, null);
            assertEquals("t1", tenant.getTenantId());
        }
    }

    @Nested
    @DisplayName("isActive")
    class IsActiveTests {

        @Test
        void shouldBeActiveForActiveStatus() {
            Tenant t = Tenant.builder().status(Tenant.TenantStatus.ACTIVE).build();
            assertTrue(t.isActive());
        }

        @Test
        void shouldBeActiveForTrialWithFutureExpiry() {
            Tenant t = Tenant.builder()
                .status(Tenant.TenantStatus.TRIAL)
                .trialEndsAt(LocalDateTime.now().plusDays(7))
                .build();
            assertTrue(t.isActive());
        }

        @Test
        void shouldNotBeActiveForExpiredTrial() {
            Tenant t = Tenant.builder()
                .status(Tenant.TenantStatus.TRIAL)
                .trialEndsAt(LocalDateTime.now().minusDays(1))
                .build();
            assertFalse(t.isActive());
        }

        @Test
        void shouldNotBeActiveForSuspended() {
            Tenant t = Tenant.builder().status(Tenant.TenantStatus.SUSPENDED).build();
            assertFalse(t.isActive());
        }

        @Test
        void shouldNotBeActiveForInactive() {
            Tenant t = Tenant.builder().status(Tenant.TenantStatus.INACTIVE).build();
            assertFalse(t.isActive());
        }
    }

    @Nested
    @DisplayName("canAddUsers")
    class CanAddUsersTests {

        @Test
        void shouldAllowWhenBelowMax() {
            Tenant t = Tenant.builder().maxUsers(10).build();
            assertTrue(t.canAddUsers(5));
        }

        @Test
        void shouldNotAllowWhenAtMax() {
            Tenant t = Tenant.builder().maxUsers(10).build();
            assertFalse(t.canAddUsers(10));
        }

        @Test
        void shouldAllowWhenMaxIsZero() {
            Tenant t = Tenant.builder().maxUsers(0).build();
            assertTrue(t.canAddUsers(999));
        }
    }

    @Nested
    @DisplayName("Equality and ToString")
    class EqualityTests {

        @Test
        void shouldBeEqualWithSameFields() {
            Tenant t1 = Tenant.builder().id("1").tenantId("t1").build();
            Tenant t2 = Tenant.builder().id("1").tenantId("t1").build();
            assertEquals(t1, t2);
            assertEquals(t1.hashCode(), t2.hashCode());
        }

        @Test
        void shouldNotBeEqualWithDifferentFields() {
            Tenant t1 = Tenant.builder().id("1").build();
            Tenant t2 = Tenant.builder().id("2").build();
            assertNotEquals(t1, t2);
        }

        @Test
        void shouldNotBeEqualToNull() {
            Tenant t = Tenant.builder().id("1").build();
            assertNotEquals(null, t);
        }

        @Test
        void shouldBeEqualToSelf() {
            Tenant t = Tenant.builder().build();
            assertEquals(t, t);
        }

        @Test
        void shouldHaveToString() {
            Tenant t = Tenant.builder().tenantId("t1").name("Test").build();
            assertNotNull(t.toString());
        }
    }

    @Nested
    @DisplayName("Enums")
    class EnumTests {

        @Test
        void tenantStatusShouldHaveAllValues() {
            assertEquals(5, Tenant.TenantStatus.values().length);
        }

        @Test
        void tenantPlanShouldHaveAllValues() {
            assertEquals(5, Tenant.TenantPlan.values().length);
        }
    }
}
