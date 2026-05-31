package com.gogidix.dashboard.gateway.api.application.dto.response;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class DtoTests {

    @Nested
    @DisplayName("AggregatedDashboardDataDto tests")
    class AggregatedDashboardDataDtoTests {

        @Test
        void builder_works() {
            AggregatedDashboardDataDto dto = AggregatedDashboardDataDto.builder()
                    .sagaStatistics(Map.of("total", 100))
                    .chartData(Map.of("charts", 5))
                    .serviceHealth(Map.of())
                    .timestamp("2024-01-01T00:00:00")
                    .tenantId("t1")
                    .build();
            assertEquals(100, dto.getSagaStatistics().get("total"));
            assertEquals("t1", dto.getTenantId());
        }

        @Test
        void noArgsAndAllArgs() {
            AggregatedDashboardDataDto dto = new AggregatedDashboardDataDto();
            assertNotNull(dto);
            AggregatedDashboardDataDto dto2 = new AggregatedDashboardDataDto(
                    Map.of("s", 1), Map.of("c", 2), Map.of("m", 3),
                    Map.of("o", 4), Map.of("a", 5), Map.of(), "ts", "t1");
            assertEquals("t1", dto2.getTenantId());
            assertEquals(1, dto2.getSagaStatistics().get("s"));
            assertEquals(2, dto2.getChartData().get("c"));
            assertEquals(3, dto2.getMonitoringData().get("m"));
            assertEquals(4, dto2.getOnboardingData().get("o"));
            assertEquals(5, dto2.getAuditData().get("a"));
        }

        @Test
        void setters_work() {
            AggregatedDashboardDataDto dto = new AggregatedDashboardDataDto();
            dto.setSagaStatistics(Map.of("k", "v"));
            dto.setChartData(Map.of("c", "d"));
            dto.setMonitoringData(Map.of("m", "n"));
            dto.setOnboardingData(Map.of("o", "p"));
            dto.setAuditData(Map.of("a", "b"));
            dto.setServiceHealth(Map.of());
            dto.setTimestamp("ts");
            dto.setTenantId("new");
            assertEquals("v", dto.getSagaStatistics().get("k"));
            assertEquals("d", dto.getChartData().get("c"));
            assertEquals("n", dto.getMonitoringData().get("m"));
            assertEquals("p", dto.getOnboardingData().get("o"));
            assertEquals("b", dto.getAuditData().get("a"));
            assertEquals("ts", dto.getTimestamp());
            assertEquals("new", dto.getTenantId());
        }

        @Test
        void equals_sameInstance_returnsTrue() {
            AggregatedDashboardDataDto dto = new AggregatedDashboardDataDto();
            assertEquals(dto, dto);
        }

        @Test
        void equals_null_returnsFalse() {
            assertNotEquals(null, new AggregatedDashboardDataDto());
        }

        @Test
        void equals_differentType_returnsFalse() {
            assertNotEquals("string", new AggregatedDashboardDataDto());
        }

        @Test
        void equals_canEqualFalse_returnsFalse() {
            AggregatedDashboardDataDto original = createFullyPopulatedAggDto();
            AggregatedDashboardDataDto sub = new AggregatedDashboardDataDto() {
                @Override
                protected boolean canEqual(Object other) {
                    return false;
                }
            };
            assertNotEquals(original, sub);
        }

        @Test
        void equals_bothEmpty_returnsTrue() {
            assertEquals(new AggregatedDashboardDataDto(), new AggregatedDashboardDataDto());
        }

        @Test
        void equals_bothPopulatedIdentical_returnsTrue() {
            assertEquals(createFullyPopulatedAggDto(), createFullyPopulatedAggDto());
        }

        @Test
        void equals_emptyVsPopulated_returnsFalse() {
            assertNotEquals(new AggregatedDashboardDataDto(), createFullyPopulatedAggDto());
        }

        @Test
        void equals_populatedVsEmpty_returnsFalse() {
            assertNotEquals(createFullyPopulatedAggDto(), new AggregatedDashboardDataDto());
        }

        @Test
        void equals_differentSagaStatistics() {
            AggregatedDashboardDataDto dto1 = createFullyPopulatedAggDto();
            AggregatedDashboardDataDto dto2 = createFullyPopulatedAggDto();
            dto2.setSagaStatistics(Map.of("different", 1));
            assertNotEquals(dto1, dto2);
        }

        @Test
        void equals_differentChartData() {
            AggregatedDashboardDataDto dto1 = createFullyPopulatedAggDto();
            AggregatedDashboardDataDto dto2 = createFullyPopulatedAggDto();
            dto2.setChartData(Map.of("different", 1));
            assertNotEquals(dto1, dto2);
        }

        @Test
        void equals_differentMonitoringData() {
            AggregatedDashboardDataDto dto1 = createFullyPopulatedAggDto();
            AggregatedDashboardDataDto dto2 = createFullyPopulatedAggDto();
            dto2.setMonitoringData(null);
            assertNotEquals(dto1, dto2);
        }

        @Test
        void equals_differentOnboardingData() {
            AggregatedDashboardDataDto dto1 = createFullyPopulatedAggDto();
            AggregatedDashboardDataDto dto2 = createFullyPopulatedAggDto();
            dto2.setOnboardingData(null);
            assertNotEquals(dto1, dto2);
        }

        @Test
        void equals_differentAuditData() {
            AggregatedDashboardDataDto dto1 = createFullyPopulatedAggDto();
            AggregatedDashboardDataDto dto2 = createFullyPopulatedAggDto();
            dto2.setAuditData(null);
            assertNotEquals(dto1, dto2);
        }

        @Test
        void equals_differentServiceHealth() {
            AggregatedDashboardDataDto dto1 = createFullyPopulatedAggDto();
            AggregatedDashboardDataDto dto2 = createFullyPopulatedAggDto();
            dto2.setServiceHealth(null);
            assertNotEquals(dto1, dto2);
        }

        @Test
        void equals_differentTimestamp() {
            AggregatedDashboardDataDto dto1 = createFullyPopulatedAggDto();
            AggregatedDashboardDataDto dto2 = createFullyPopulatedAggDto();
            dto2.setTimestamp("different");
            assertNotEquals(dto1, dto2);
        }

        @Test
        void equals_differentTenantId() {
            AggregatedDashboardDataDto dto1 = createFullyPopulatedAggDto();
            AggregatedDashboardDataDto dto2 = createFullyPopulatedAggDto();
            dto2.setTenantId("different");
            assertNotEquals(dto1, dto2);
        }

        @Test
        void hashCode_empty_returnsConsistent() {
            AggregatedDashboardDataDto dto = new AggregatedDashboardDataDto();
            assertEquals(dto.hashCode(), dto.hashCode());
        }

        @Test
        void hashCode_populated_returnsConsistent() {
            AggregatedDashboardDataDto dto = createFullyPopulatedAggDto();
            assertEquals(dto.hashCode(), dto.hashCode());
        }

        @Test
        void hashCode_emptyAndPopulated_different() {
            assertNotEquals(new AggregatedDashboardDataDto().hashCode(), createFullyPopulatedAggDto().hashCode());
        }

        @Test
        void toString_empty_returnsString() {
            assertNotNull(new AggregatedDashboardDataDto().toString());
        }

        @Test
        void toString_populated_containsClassName() {
            String str = createFullyPopulatedAggDto().toString();
            assertNotNull(str);
            assertTrue(str.contains("AggregatedDashboardDataDto"));
        }

        private AggregatedDashboardDataDto createFullyPopulatedAggDto() {
            return AggregatedDashboardDataDto.builder()
                    .sagaStatistics(Map.of("total", 100))
                    .chartData(Map.of("charts", 5))
                    .monitoringData(Map.of("cpu", 80))
                    .onboardingData(Map.of("pending", 10))
                    .auditData(Map.of("events", 500))
                    .serviceHealth(Map.of("svc", ServiceHealthDto.builder().serviceName("svc").build()))
                    .timestamp("2024-01-01T00:00:00")
                    .tenantId("t1")
                    .build();
        }
    }

    @Nested
    @DisplayName("ServiceHealthDto tests")
    class ServiceHealthDtoTests {

        @Test
        void builder_works() {
            ServiceHealthDto dto = ServiceHealthDto.builder()
                    .serviceName("svc")
                    .status("UP")
                    .baseUrl("http://localhost")
                    .lastChecked(LocalDateTime.now())
                    .responseTimeMs(50L)
                    .retryCount(3)
                    .details(Map.of("key", "val"))
                    .build();
            assertEquals("svc", dto.getServiceName());
            assertEquals("UP", dto.getStatus());
            assertEquals(50L, dto.getResponseTimeMs());
        }

        @Test
        void noArgsAndAllArgs() {
            ServiceHealthDto dto = new ServiceHealthDto();
            assertNotNull(dto);
            LocalDateTime now = LocalDateTime.now();
            ServiceHealthDto dto2 = new ServiceHealthDto("svc", "UP", "url", now, 1L, 3, Map.of());
            assertEquals("svc", dto2.getServiceName());
            assertEquals("UP", dto2.getStatus());
            assertEquals("url", dto2.getBaseUrl());
            assertEquals(now, dto2.getLastChecked());
            assertEquals(1L, dto2.getResponseTimeMs());
            assertEquals(3, dto2.getRetryCount());
        }

        @Test
        void setters_work() {
            ServiceHealthDto dto = new ServiceHealthDto();
            dto.setServiceName("new-svc");
            dto.setStatus("DOWN");
            dto.setBaseUrl("http://new");
            dto.setLastChecked(LocalDateTime.now());
            dto.setResponseTimeMs(99L);
            dto.setRetryCount(5);
            dto.setDetails(Map.of("k", "v"));
            assertEquals("new-svc", dto.getServiceName());
            assertEquals("DOWN", dto.getStatus());
            assertEquals("http://new", dto.getBaseUrl());
            assertEquals(99L, dto.getResponseTimeMs());
            assertEquals(5, dto.getRetryCount());
            assertEquals("v", dto.getDetails().get("k"));
        }

        @Test
        void statusEnum_hasAllValues() {
            assertEquals(4, ServiceHealthDto.Status.values().length);
            assertNotNull(ServiceHealthDto.Status.valueOf("UP"));
            assertNotNull(ServiceHealthDto.Status.valueOf("DOWN"));
            assertNotNull(ServiceHealthDto.Status.valueOf("DEGRADED"));
            assertNotNull(ServiceHealthDto.Status.valueOf("UNKNOWN"));
        }

        @Test
        void equals_sameInstance_returnsTrue() {
            ServiceHealthDto dto = new ServiceHealthDto();
            assertEquals(dto, dto);
        }

        @Test
        void equals_null_returnsFalse() {
            assertNotEquals(null, new ServiceHealthDto());
        }

        @Test
        void equals_differentType_returnsFalse() {
            assertNotEquals("string", new ServiceHealthDto());
        }

        @Test
        void equals_canEqualFalse_returnsFalse() {
            ServiceHealthDto original = createFullyPopulatedHealthDto();
            ServiceHealthDto sub = new ServiceHealthDto() {
                @Override
                protected boolean canEqual(Object other) {
                    return false;
                }
            };
            assertNotEquals(original, sub);
        }

        @Test
        void equals_bothEmpty_returnsTrue() {
            assertEquals(new ServiceHealthDto(), new ServiceHealthDto());
        }

        @Test
        void equals_bothPopulatedIdentical_returnsTrue() {
            assertEquals(createFullyPopulatedHealthDto(), createFullyPopulatedHealthDto());
        }

        @Test
        void equals_emptyVsPopulated_returnsFalse() {
            assertNotEquals(new ServiceHealthDto(), createFullyPopulatedHealthDto());
        }

        @Test
        void equals_populatedVsEmpty_returnsFalse() {
            assertNotEquals(createFullyPopulatedHealthDto(), new ServiceHealthDto());
        }

        @Test
        void equals_differentServiceName() {
            ServiceHealthDto dto1 = createFullyPopulatedHealthDto();
            ServiceHealthDto dto2 = createFullyPopulatedHealthDto();
            dto2.setServiceName("different");
            assertNotEquals(dto1, dto2);
        }

        @Test
        void equals_differentStatus() {
            ServiceHealthDto dto1 = createFullyPopulatedHealthDto();
            ServiceHealthDto dto2 = createFullyPopulatedHealthDto();
            dto2.setStatus("DOWN");
            assertNotEquals(dto1, dto2);
        }

        @Test
        void equals_differentBaseUrl() {
            ServiceHealthDto dto1 = createFullyPopulatedHealthDto();
            ServiceHealthDto dto2 = createFullyPopulatedHealthDto();
            dto2.setBaseUrl("http://different");
            assertNotEquals(dto1, dto2);
        }

        @Test
        void equals_differentLastChecked() {
            ServiceHealthDto dto1 = createFullyPopulatedHealthDto();
            ServiceHealthDto dto2 = createFullyPopulatedHealthDto();
            dto2.setLastChecked(LocalDateTime.of(2000, 1, 1, 0, 0));
            assertNotEquals(dto1, dto2);
        }

        @Test
        void equals_differentResponseTimeMs() {
            ServiceHealthDto dto1 = createFullyPopulatedHealthDto();
            ServiceHealthDto dto2 = createFullyPopulatedHealthDto();
            dto2.setResponseTimeMs(999L);
            assertNotEquals(dto1, dto2);
        }

        @Test
        void equals_differentRetryCount() {
            ServiceHealthDto dto1 = createFullyPopulatedHealthDto();
            ServiceHealthDto dto2 = createFullyPopulatedHealthDto();
            dto2.setRetryCount(99);
            assertNotEquals(dto1, dto2);
        }

        @Test
        void equals_differentDetails() {
            ServiceHealthDto dto1 = createFullyPopulatedHealthDto();
            ServiceHealthDto dto2 = createFullyPopulatedHealthDto();
            dto2.setDetails(Map.of("different", 1));
            assertNotEquals(dto1, dto2);
        }

        @Test
        void hashCode_empty_returnsConsistent() {
            ServiceHealthDto dto = new ServiceHealthDto();
            assertEquals(dto.hashCode(), dto.hashCode());
        }

        @Test
        void hashCode_populated_returnsConsistent() {
            ServiceHealthDto dto = createFullyPopulatedHealthDto();
            assertEquals(dto.hashCode(), dto.hashCode());
        }

        @Test
        void hashCode_emptyAndPopulated_different() {
            assertNotEquals(new ServiceHealthDto().hashCode(), createFullyPopulatedHealthDto().hashCode());
        }

        @Test
        void toString_empty_returnsString() {
            assertNotNull(new ServiceHealthDto().toString());
        }

        @Test
        void toString_populated_containsClassName() {
            String str = createFullyPopulatedHealthDto().toString();
            assertNotNull(str);
            assertTrue(str.contains("ServiceHealthDto"));
        }

        private ServiceHealthDto createFullyPopulatedHealthDto() {
            return ServiceHealthDto.builder()
                    .serviceName("svc")
                    .status("UP")
                    .baseUrl("http://localhost:8080")
                    .lastChecked(LocalDateTime.of(2024, 1, 1, 0, 0))
                    .responseTimeMs(50L)
                    .retryCount(3)
                    .details(Map.of("key", "val"))
                    .build();
        }
    }

    @Nested
    @DisplayName("AggregateResponseDto tests")
    class AggregateResponseDtoTests {

        @Test
        void builder_works() {
            AggregateResponseDto dto = AggregateResponseDto.builder()
                    .endpoint("/api/test")
                    .serviceName("svc")
                    .data(Map.of("result", "ok"))
                    .timestamp(LocalDateTime.now())
                    .durationMs(100L)
                    .correlationId("corr-1")
                    .tenantId("t1")
                    .success(true)
                    .cached(false)
                    .statusCode(200)
                    .build();
            assertEquals("/api/test", dto.getEndpoint());
            assertTrue(dto.getSuccess());
            assertEquals(200, dto.getStatusCode());
        }

        @Test
        void defaults_cachedIsFalse() {
            AggregateResponseDto dto = AggregateResponseDto.builder().build();
            assertFalse(dto.getCached());
        }

        @Test
        void defaults_successIsTrue() {
            AggregateResponseDto dto = AggregateResponseDto.builder().build();
            assertTrue(dto.getSuccess());
        }

        @Test
        void noArgsAndAllArgs() {
            AggregateResponseDto dto = new AggregateResponseDto();
            assertNotNull(dto);
            LocalDateTime now = LocalDateTime.now();
            AggregateResponseDto dto2 = new AggregateResponseDto(
                    "/ep", "svc", "data", Map.of("m", 1), now, 1L, "corr", "t1",
                    false, true, "err", 500);
            assertEquals("/ep", dto2.getEndpoint());
            assertEquals("svc", dto2.getServiceName());
            assertEquals("data", dto2.getData());
            assertEquals(1, dto2.getMetadata().get("m"));
            assertEquals(now, dto2.getTimestamp());
            assertEquals(1L, dto2.getDurationMs());
            assertEquals("corr", dto2.getCorrelationId());
            assertEquals("t1", dto2.getTenantId());
            assertFalse(dto2.getCached());
            assertTrue(dto2.getSuccess());
            assertEquals("err", dto2.getError());
            assertEquals(500, dto2.getStatusCode());
        }

        @Test
        void setters_work() {
            AggregateResponseDto dto = new AggregateResponseDto();
            dto.setEndpoint("/new");
            dto.setServiceName("new-svc");
            dto.setData("data");
            dto.setMetadata(Map.of("k", "v"));
            dto.setTimestamp(LocalDateTime.now());
            dto.setDurationMs(200L);
            dto.setCorrelationId("corr-2");
            dto.setTenantId("t2");
            dto.setCached(true);
            dto.setSuccess(false);
            dto.setError("failed");
            dto.setStatusCode(500);
            assertEquals("/new", dto.getEndpoint());
            assertEquals("new-svc", dto.getServiceName());
            assertFalse(dto.getSuccess());
            assertEquals("failed", dto.getError());
            assertEquals(500, dto.getStatusCode());
        }

        @Test
        void equals_sameInstance_returnsTrue() {
            AggregateResponseDto dto = new AggregateResponseDto();
            assertEquals(dto, dto);
        }

        @Test
        void equals_null_returnsFalse() {
            assertNotEquals(null, new AggregateResponseDto());
        }

        @Test
        void equals_differentType_returnsFalse() {
            assertNotEquals("string", new AggregateResponseDto());
        }

        @Test
        void equals_canEqualFalse_returnsFalse() {
            AggregateResponseDto original = createFullyPopulatedResponseDto();
            AggregateResponseDto sub = new AggregateResponseDto() {
                @Override
                protected boolean canEqual(Object other) {
                    return false;
                }
            };
            assertNotEquals(original, sub);
        }

        @Test
        void equals_bothEmpty_returnsTrue() {
            assertEquals(new AggregateResponseDto(), new AggregateResponseDto());
        }

        @Test
        void equals_bothPopulatedIdentical_returnsTrue() {
            assertEquals(createFullyPopulatedResponseDto(), createFullyPopulatedResponseDto());
        }

        @Test
        void equals_emptyVsPopulated_returnsFalse() {
            assertNotEquals(new AggregateResponseDto(), createFullyPopulatedResponseDto());
        }

        @Test
        void equals_populatedVsEmpty_returnsFalse() {
            assertNotEquals(createFullyPopulatedResponseDto(), new AggregateResponseDto());
        }

        @Test
        void equals_differentEndpoint() {
            AggregateResponseDto dto1 = createFullyPopulatedResponseDto();
            AggregateResponseDto dto2 = createFullyPopulatedResponseDto();
            dto2.setEndpoint("/different");
            assertNotEquals(dto1, dto2);
        }

        @Test
        void equals_differentServiceName() {
            AggregateResponseDto dto1 = createFullyPopulatedResponseDto();
            AggregateResponseDto dto2 = createFullyPopulatedResponseDto();
            dto2.setServiceName("different");
            assertNotEquals(dto1, dto2);
        }

        @Test
        void equals_differentData() {
            AggregateResponseDto dto1 = createFullyPopulatedResponseDto();
            AggregateResponseDto dto2 = createFullyPopulatedResponseDto();
            dto2.setData("different");
            assertNotEquals(dto1, dto2);
        }

        @Test
        void equals_differentMetadata() {
            AggregateResponseDto dto1 = createFullyPopulatedResponseDto();
            AggregateResponseDto dto2 = createFullyPopulatedResponseDto();
            dto2.setMetadata(Map.of("different", 1));
            assertNotEquals(dto1, dto2);
        }

        @Test
        void equals_differentTimestamp() {
            AggregateResponseDto dto1 = createFullyPopulatedResponseDto();
            AggregateResponseDto dto2 = createFullyPopulatedResponseDto();
            dto2.setTimestamp(LocalDateTime.of(2000, 1, 1, 0, 0));
            assertNotEquals(dto1, dto2);
        }

        @Test
        void equals_differentDurationMs() {
            AggregateResponseDto dto1 = createFullyPopulatedResponseDto();
            AggregateResponseDto dto2 = createFullyPopulatedResponseDto();
            dto2.setDurationMs(999L);
            assertNotEquals(dto1, dto2);
        }

        @Test
        void equals_differentCorrelationId() {
            AggregateResponseDto dto1 = createFullyPopulatedResponseDto();
            AggregateResponseDto dto2 = createFullyPopulatedResponseDto();
            dto2.setCorrelationId("different");
            assertNotEquals(dto1, dto2);
        }

        @Test
        void equals_differentTenantId() {
            AggregateResponseDto dto1 = createFullyPopulatedResponseDto();
            AggregateResponseDto dto2 = createFullyPopulatedResponseDto();
            dto2.setTenantId("different");
            assertNotEquals(dto1, dto2);
        }

        @Test
        void equals_differentCached() {
            AggregateResponseDto dto1 = createFullyPopulatedResponseDto();
            AggregateResponseDto dto2 = createFullyPopulatedResponseDto();
            dto2.setCached(true);
            assertNotEquals(dto1, dto2);
        }

        @Test
        void equals_differentSuccess() {
            AggregateResponseDto dto1 = createFullyPopulatedResponseDto();
            AggregateResponseDto dto2 = createFullyPopulatedResponseDto();
            dto2.setSuccess(false);
            assertNotEquals(dto1, dto2);
        }

        @Test
        void equals_differentError() {
            AggregateResponseDto dto1 = createFullyPopulatedResponseDto();
            AggregateResponseDto dto2 = createFullyPopulatedResponseDto();
            dto2.setError("different");
            assertNotEquals(dto1, dto2);
        }

        @Test
        void equals_differentStatusCode() {
            AggregateResponseDto dto1 = createFullyPopulatedResponseDto();
            AggregateResponseDto dto2 = createFullyPopulatedResponseDto();
            dto2.setStatusCode(404);
            assertNotEquals(dto1, dto2);
        }

        @Test
        void hashCode_empty_returnsConsistent() {
            AggregateResponseDto dto = new AggregateResponseDto();
            assertEquals(dto.hashCode(), dto.hashCode());
        }

        @Test
        void hashCode_populated_returnsConsistent() {
            AggregateResponseDto dto = createFullyPopulatedResponseDto();
            assertEquals(dto.hashCode(), dto.hashCode());
        }

        @Test
        void hashCode_emptyAndPopulated_different() {
            assertNotEquals(new AggregateResponseDto().hashCode(), createFullyPopulatedResponseDto().hashCode());
        }

        @Test
        void toString_empty_returnsString() {
            assertNotNull(new AggregateResponseDto().toString());
        }

        @Test
        void toString_populated_containsClassName() {
            String str = createFullyPopulatedResponseDto().toString();
            assertNotNull(str);
            assertTrue(str.contains("AggregateResponseDto"));
        }

        private AggregateResponseDto createFullyPopulatedResponseDto() {
            return AggregateResponseDto.builder()
                    .endpoint("/api/test")
                    .serviceName("svc")
                    .data(Map.of("result", "ok"))
                    .metadata(Map.of("key", "val"))
                    .timestamp(LocalDateTime.of(2024, 1, 1, 0, 0))
                    .durationMs(100L)
                    .correlationId("corr-1")
                    .tenantId("t1")
                    .cached(false)
                    .success(true)
                    .error(null)
                    .statusCode(200)
                    .build();
        }
    }
}
