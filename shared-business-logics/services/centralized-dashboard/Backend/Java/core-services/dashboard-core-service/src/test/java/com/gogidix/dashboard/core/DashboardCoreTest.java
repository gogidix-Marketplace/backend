package com.gogidix.dashboard.core;

import com.gogidix.dashboard.core.application.dto.request.CreateKPIRequestDto;
import com.gogidix.dashboard.core.application.dto.request.RecordKPIValueRequestDto;
import com.gogidix.dashboard.core.application.dto.request.UpdateKPIRequestDto;
import com.gogidix.dashboard.core.application.dto.response.KPIResponseDto;
import com.gogidix.dashboard.core.application.dto.response.PagedResponseDto;
import com.gogidix.dashboard.core.domain.model.*;
import com.gogidix.dashboard.core.domain.model.DashboardWidget.WidgetType;
import com.gogidix.dashboard.core.interfaces.rest.HealthController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Dashboard Core Service Tests")
class DashboardCoreTest {

    @Nested
    @DisplayName("DashboardKPI Tests")
    class DashboardKPITest {

        @Test
        @DisplayName("Should update value and set trend UP")
        void shouldUpdateValueUp() {
            DashboardKPI kpi = DashboardKPI.builder()
                    .currentValue(50.0).build();
            kpi.updateValue(60.0);
            assertEquals(60.0, kpi.getCurrentValue());
            assertEquals(50.0, kpi.getPreviousValue());
            assertEquals("UP", kpi.getTrend());
            assertNotNull(kpi.getLastCalculatedAt());
        }

        @Test
        @DisplayName("Should update value and set trend DOWN")
        void shouldUpdateValueDown() {
            DashboardKPI kpi = DashboardKPI.builder()
                    .currentValue(50.0).build();
            kpi.updateValue(40.0);
            assertEquals("DOWN", kpi.getTrend());
        }

        @Test
        @DisplayName("Should update value and set trend STABLE")
        void shouldUpdateValueStable() {
            DashboardKPI kpi = DashboardKPI.builder()
                    .currentValue(50.0).build();
            kpi.updateValue(50.0);
            assertEquals("STABLE", kpi.getTrend());
        }

        @Test
        @DisplayName("Should detect above threshold")
        void shouldDetectAboveThreshold() {
            DashboardKPI kpi = DashboardKPI.builder()
                    .currentValue(100.0).thresholdCritical(80.0).build();
            assertTrue(kpi.isAboveThreshold());
            assertFalse(kpi.isBelowThreshold());
        }

        @Test
        @DisplayName("Should detect below threshold")
        void shouldDetectBelowThreshold() {
            DashboardKPI kpi = DashboardKPI.builder()
                    .currentValue(50.0).thresholdCritical(80.0).build();
            assertTrue(kpi.isBelowThreshold());
            assertFalse(kpi.isAboveThreshold());
        }

        @Test
        @DisplayName("Should detect warning level")
        void shouldDetectWarningLevel() {
            DashboardKPI kpi = DashboardKPI.builder()
                    .currentValue(75.0).thresholdWarning(70.0).build();
            assertTrue(kpi.isAtWarningLevel());
        }

        @Test
        @DisplayName("Should calculate progress")
        void shouldCalculateProgress() {
            DashboardKPI kpi = DashboardKPI.builder()
                    .currentValue(75.0).targetValue(100.0).build();
            assertEquals(75.0, kpi.calculateProgress());
        }

        @Test
        @DisplayName("Should return null progress for no target")
        void shouldReturnNullProgressForNoTarget() {
            DashboardKPI kpi = DashboardKPI.builder()
                    .currentValue(75.0).build();
            assertNull(kpi.calculateProgress());
        }

        @Test
        @DisplayName("Should return null progress for zero target")
        void shouldReturnNullProgressForZeroTarget() {
            DashboardKPI kpi = DashboardKPI.builder()
                    .currentValue(75.0).targetValue(0.0).build();
            assertNull(kpi.calculateProgress());
        }

        @Test
        @DisplayName("Should add historical value")
        void shouldAddHistoricalValue() {
            DashboardKPI kpi = DashboardKPI.builder().build();
            KPIValue value = KPIValue.builder().value(42.0).build();
            kpi.addHistoricalValue(value);
            assertEquals(1, kpi.getHistoricalValues().size());
            assertEquals(kpi, value.getKpi());
        }

        @Test
        @DisplayName("Should add target")
        void shouldAddTarget() {
            DashboardKPI kpi = DashboardKPI.builder().build();
            KPITarget target = KPITarget.builder().targetValue(100.0).build();
            kpi.addTarget(target);
            assertEquals(1, kpi.getTargets().size());
        }

        @Test
        @DisplayName("Should use builder defaults")
        void shouldUseBuilderDefaults() {
            DashboardKPI kpi = DashboardKPI.builder().build();
            assertTrue(kpi.getIsActive());
            assertFalse(kpi.getIsRealTime());
        }
    }

    @Nested
    @DisplayName("KPITarget Tests")
    class KPITargetTest {

        @Test
        @DisplayName("Should calculate progress percentage")
        void shouldCalculateProgress() {
            KPITarget target = KPITarget.builder().targetValue(100.0).build();
            assertEquals(75.0, target.getProgressPercentage(75.0));
        }

        @Test
        @DisplayName("Should return null for no target")
        void shouldReturnNullForNoTarget() {
            KPITarget target = KPITarget.builder().build();
            assertNull(target.getProgressPercentage(75.0));
        }

        @Test
        @DisplayName("Should detect target met")
        void shouldDetectTargetMet() {
            KPITarget target = KPITarget.builder().targetValue(100.0).build();
            assertTrue(target.isTargetMet(120.0));
            assertTrue(target.isTargetMet(100.0));
            assertFalse(target.isTargetMet(90.0));
        }

        @Test
        @DisplayName("Should detect below minimum")
        void shouldDetectBelowMinimum() {
            KPITarget target = KPITarget.builder().minimumAcceptable(50.0).build();
            assertTrue(target.isBelowMinimum(30.0));
            assertFalse(target.isBelowMinimum(60.0));
        }
    }

    @Nested
    @DisplayName("SourceDomain Tests")
    class SourceDomainTest {

        @Test
        @DisplayName("Should have display names")
        void shouldHaveDisplayNames() {
            assertEquals("Courier Service", SourceDomain.COURIER_SERVICE.getDisplayName());
            assertEquals("Warehouse", SourceDomain.WAREHOUSE.getDisplayName());
            assertNotNull(SourceDomain.AGGREGATED.getDescription());
        }

        @Test
        @DisplayName("Should have all domains")
        void shouldHaveAllDomains() {
            assertTrue(SourceDomain.values().length >= 15);
        }
    }

    @Nested
    @DisplayName("DashboardWidget Tests")
    class DashboardWidgetTest {

        @Test
        @DisplayName("Should build widget")
        void shouldBuildWidget() {
            DashboardWidget w = DashboardWidget.builder()
                    .name("Sales Chart").widgetType(WidgetType.LINE_CHART)
                    .positionX(0).positionY(0).build();
            assertEquals("Sales Chart", w.getName());
            assertEquals(WidgetType.LINE_CHART, w.getWidgetType());
        }

        @Test
        @DisplayName("Should have defaults")
        void shouldHaveDefaults() {
            DashboardWidget w = DashboardWidget.builder().build();
            assertEquals(0, w.getPositionX());
            assertEquals(0, w.getPositionY());
            assertEquals(4, w.getWidth());
            assertEquals(3, w.getHeight());
            assertTrue(w.getIsRefreshable());
            assertTrue(w.getIsVisible());
        }

        @Test
        @DisplayName("Should have widget types")
        void shouldHaveWidgetTypes() {
            assertTrue(WidgetType.values().length >= 12);
        }
    }

    @Nested
    @DisplayName("DTO Tests")
    class DtoTest {

        @Test
        @DisplayName("Should build CreateKPIRequestDto")
        void shouldBuildCreateRequest() {
            CreateKPIRequestDto dto = new CreateKPIRequestDto();
            dto.setName("Revenue");
            dto.setCode("REV_001");
            dto.setCategory("finance");
            assertEquals("Revenue", dto.getName());
        }

        @Test
        @DisplayName("Should build RecordKPIValueRequestDto")
        void shouldBuildRecordValueRequest() {
            RecordKPIValueRequestDto dto = new RecordKPIValueRequestDto();
            dto.setValue(42.5);
            assertEquals(42.5, dto.getValue());
        }

        @Test
        @DisplayName("Should build UpdateKPIRequestDto")
        void shouldBuildUpdateRequest() {
            UpdateKPIRequestDto dto = new UpdateKPIRequestDto();
            dto.setName("Updated");
            assertEquals("Updated", dto.getName());
        }

        @Test
        @DisplayName("Should build KPIResponseDto")
        void shouldBuildResponse() {
            KPIResponseDto dto = new KPIResponseDto();
            dto.setId(UUID.randomUUID());
            dto.setName("KPI");
            assertNotNull(dto.getId());
        }

        @Test
        @DisplayName("Should build PagedResponseDto")
        void shouldBuildPagedResponse() {
            PagedResponseDto<Object> dto = new PagedResponseDto<>();
            dto.setTotalElements(100L);
            assertEquals(100L, dto.getTotalElements());
        }
    }

    @Nested
    @DisplayName("HealthController Tests")
    class HealthControllerTest {

        private final HealthController controller = new HealthController(Optional.empty());

        @Test
        @DisplayName("Should return health status")
        void shouldReturnHealth() {
            ResponseEntity<Map<String, Object>> resp = controller.health();
            assertEquals(HttpStatus.OK, resp.getStatusCode());
            assertEquals("UP", resp.getBody().get("status"));
        }

        @Test
        @DisplayName("Should return info")
        void shouldReturnInfo() {
            ResponseEntity<Map<String, Object>> resp = controller.info();
            assertEquals(HttpStatus.OK, resp.getStatusCode());
            assertEquals("Dashboard Core Service", resp.getBody().get("name"));
        }
    }
}
