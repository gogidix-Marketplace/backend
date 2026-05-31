package com.gogidix.dashboard.gateway.api.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ServiceRegistryTest {

    @Nested
    @DisplayName("Construction tests")
    class ConstructionTests {
        @Test
        void noArgsConstructor_createsInstance() {
            ServiceRegistry sr = new ServiceRegistry();
            assertNotNull(sr);
            assertTrue(sr.getEnabled());
            assertEquals(5000, sr.getTimeoutMs());
            assertEquals(3, sr.getRetryCount());
        }

        @Test
        void builder_createsInstance() {
            ServiceRegistry sr = createFullyPopulatedServiceRegistry();
            assertEquals(1L, sr.getId());
            assertEquals("test-service", sr.getServiceName());
            assertEquals("http://localhost:8080", sr.getBaseUrl());
            assertEquals("/actuator/health", sr.getHealthCheckUrl());
            assertEquals("tenant-1", sr.getTenantId());
            assertTrue(sr.getEnabled());
            assertEquals(3000, sr.getTimeoutMs());
            assertEquals(5, sr.getRetryCount());
            assertEquals("Test service", sr.getDescription());
        }

        @Test
        void allArgsConstructor_works() {
            LocalDateTime now = LocalDateTime.now();
            ServiceRegistry sr = new ServiceRegistry(1L, "svc", "url", "hc", "t", false, 1000, 1, "desc", now, now);
            assertEquals(1L, sr.getId());
            assertEquals("svc", sr.getServiceName());
            assertEquals("url", sr.getBaseUrl());
            assertEquals("hc", sr.getHealthCheckUrl());
            assertEquals("t", sr.getTenantId());
            assertFalse(sr.getEnabled());
            assertEquals(1000, sr.getTimeoutMs());
            assertEquals(1, sr.getRetryCount());
            assertEquals("desc", sr.getDescription());
            assertEquals(now, sr.getCreatedAt());
            assertEquals(now, sr.getUpdatedAt());
        }

        @Test
        void builderDefaults_enabledIsTrue() {
            assertTrue(ServiceRegistry.builder().build().getEnabled());
        }

        @Test
        void builderDefaults_timeoutMsIs5000() {
            assertEquals(5000, ServiceRegistry.builder().build().getTimeoutMs());
        }

        @Test
        void builderDefaults_retryCountIs3() {
            assertEquals(3, ServiceRegistry.builder().build().getRetryCount());
        }
    }

    @Nested
    @DisplayName("Setter tests")
    class SetterTests {
        @Test
        void setters_work() {
            ServiceRegistry sr = new ServiceRegistry();
            sr.setId(2L);
            sr.setServiceName("new-svc");
            sr.setBaseUrl("http://new-url");
            sr.setHealthCheckUrl("/health");
            sr.setTenantId("new-tenant");
            sr.setEnabled(false);
            sr.setTimeoutMs(2000);
            sr.setRetryCount(10);
            sr.setDescription("Updated");
            sr.setCreatedAt(LocalDateTime.now());
            sr.setUpdatedAt(LocalDateTime.now());
            assertEquals(2L, sr.getId());
            assertEquals("new-svc", sr.getServiceName());
            assertEquals("http://new-url", sr.getBaseUrl());
            assertEquals("/health", sr.getHealthCheckUrl());
            assertEquals("new-tenant", sr.getTenantId());
            assertFalse(sr.getEnabled());
            assertEquals(2000, sr.getTimeoutMs());
            assertEquals(10, sr.getRetryCount());
            assertEquals("Updated", sr.getDescription());
        }
    }

    @Nested
    @DisplayName("equals tests")
    class EqualsTests {
        @Test
        void equals_sameInstance_returnsTrue() {
            ServiceRegistry sr = new ServiceRegistry();
            assertEquals(sr, sr);
        }

        @Test
        void equals_null_returnsFalse() {
            assertNotEquals(null, new ServiceRegistry());
        }

        @Test
        void equals_differentType_returnsFalse() {
            assertNotEquals("string", new ServiceRegistry());
        }

        @Test
        void equals_canEqualFalse_returnsFalse() {
            ServiceRegistry original = createFullyPopulatedServiceRegistry();
            ServiceRegistry sub = new ServiceRegistry() {
                @Override
                protected boolean canEqual(Object other) {
                    return false;
                }
            };
            assertNotEquals(original, sub);
        }

        @Test
        void equals_bothEmpty_returnsTrue() {
            assertEquals(new ServiceRegistry(), new ServiceRegistry());
        }

        @Test
        void equals_bothPopulatedIdentical_returnsTrue() {
            assertEquals(createFullyPopulatedServiceRegistry(), createFullyPopulatedServiceRegistry());
        }

        @Test
        void equals_emptyVsPopulated_returnsFalse() {
            assertNotEquals(new ServiceRegistry(), createFullyPopulatedServiceRegistry());
        }

        @Test
        void equals_populatedVsEmpty_returnsFalse() {
            assertNotEquals(createFullyPopulatedServiceRegistry(), new ServiceRegistry());
        }

        @Test
        void equals_differentId() {
            ServiceRegistry sr1 = createFullyPopulatedServiceRegistry();
            ServiceRegistry sr2 = createFullyPopulatedServiceRegistry();
            sr2.setId(99L);
            assertNotEquals(sr1, sr2);
        }

        @Test
        void equals_differentServiceName() {
            ServiceRegistry sr1 = createFullyPopulatedServiceRegistry();
            ServiceRegistry sr2 = createFullyPopulatedServiceRegistry();
            sr2.setServiceName("different");
            assertNotEquals(sr1, sr2);
        }

        @Test
        void equals_differentBaseUrl() {
            ServiceRegistry sr1 = createFullyPopulatedServiceRegistry();
            ServiceRegistry sr2 = createFullyPopulatedServiceRegistry();
            sr2.setBaseUrl("http://different");
            assertNotEquals(sr1, sr2);
        }

        @Test
        void equals_differentHealthCheckUrl() {
            ServiceRegistry sr1 = createFullyPopulatedServiceRegistry();
            ServiceRegistry sr2 = createFullyPopulatedServiceRegistry();
            sr2.setHealthCheckUrl(null);
            assertNotEquals(sr1, sr2);
        }

        @Test
        void equals_differentTenantId() {
            ServiceRegistry sr1 = createFullyPopulatedServiceRegistry();
            ServiceRegistry sr2 = createFullyPopulatedServiceRegistry();
            sr2.setTenantId("different");
            assertNotEquals(sr1, sr2);
        }

        @Test
        void equals_differentEnabled() {
            ServiceRegistry sr1 = createFullyPopulatedServiceRegistry();
            ServiceRegistry sr2 = createFullyPopulatedServiceRegistry();
            sr2.setEnabled(false);
            assertNotEquals(sr1, sr2);
        }

        @Test
        void equals_differentTimeoutMs() {
            ServiceRegistry sr1 = createFullyPopulatedServiceRegistry();
            ServiceRegistry sr2 = createFullyPopulatedServiceRegistry();
            sr2.setTimeoutMs(9999);
            assertNotEquals(sr1, sr2);
        }

        @Test
        void equals_differentRetryCount() {
            ServiceRegistry sr1 = createFullyPopulatedServiceRegistry();
            ServiceRegistry sr2 = createFullyPopulatedServiceRegistry();
            sr2.setRetryCount(99);
            assertNotEquals(sr1, sr2);
        }

        @Test
        void equals_differentDescription() {
            ServiceRegistry sr1 = createFullyPopulatedServiceRegistry();
            ServiceRegistry sr2 = createFullyPopulatedServiceRegistry();
            sr2.setDescription("different");
            assertNotEquals(sr1, sr2);
        }

        @Test
        void equals_differentCreatedAt() {
            ServiceRegistry sr1 = createFullyPopulatedServiceRegistry();
            ServiceRegistry sr2 = createFullyPopulatedServiceRegistry();
            sr2.setCreatedAt(LocalDateTime.of(2000, 1, 1, 0, 0));
            assertNotEquals(sr1, sr2);
        }

        @Test
        void equals_differentUpdatedAt() {
            ServiceRegistry sr1 = createFullyPopulatedServiceRegistry();
            ServiceRegistry sr2 = createFullyPopulatedServiceRegistry();
            sr2.setUpdatedAt(LocalDateTime.of(2000, 1, 1, 0, 0));
            assertNotEquals(sr1, sr2);
        }
    }

    @Nested
    @DisplayName("hashCode tests")
    class HashCodeTests {
        @Test
        void hashCode_empty_returnsConsistent() {
            ServiceRegistry sr = new ServiceRegistry();
            assertEquals(sr.hashCode(), sr.hashCode());
        }

        @Test
        void hashCode_populated_returnsConsistent() {
            ServiceRegistry sr = createFullyPopulatedServiceRegistry();
            assertEquals(sr.hashCode(), sr.hashCode());
        }

        @Test
        void hashCode_emptyAndPopulated_different() {
            assertNotEquals(new ServiceRegistry().hashCode(), createFullyPopulatedServiceRegistry().hashCode());
        }
    }

    @Nested
    @DisplayName("toString tests")
    class ToStringTests {
        @Test
        void toString_empty_returnsString() {
            assertNotNull(new ServiceRegistry().toString());
        }

        @Test
        void toString_populated_containsClassName() {
            String str = createFullyPopulatedServiceRegistry().toString();
            assertNotNull(str);
            assertTrue(str.contains("ServiceRegistry"));
        }
    }

    private ServiceRegistry createFullyPopulatedServiceRegistry() {
        LocalDateTime fixedDate = LocalDateTime.of(2024, 1, 1, 0, 0);
        return ServiceRegistry.builder()
                .id(1L)
                .serviceName("test-service")
                .baseUrl("http://localhost:8080")
                .healthCheckUrl("/actuator/health")
                .tenantId("tenant-1")
                .enabled(true)
                .timeoutMs(3000)
                .retryCount(5)
                .description("Test service")
                .createdAt(fixedDate)
                .updatedAt(fixedDate)
                .build();
    }
}
