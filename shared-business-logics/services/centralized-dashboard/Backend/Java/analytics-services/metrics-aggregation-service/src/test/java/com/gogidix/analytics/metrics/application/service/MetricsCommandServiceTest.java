package com.gogidix.analytics.metrics.application.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gogidix.analytics.metrics.domain.model.MetricAlert;
import com.gogidix.analytics.metrics.domain.model.MetricDataPoint;
import com.gogidix.analytics.metrics.domain.port.in.CreateMetricAlertCommand;
import com.gogidix.analytics.metrics.domain.port.in.IngestMetricCommand;
import com.gogidix.analytics.metrics.domain.port.out.MetricEventPublisher;
import com.gogidix.analytics.metrics.domain.repository.MetricAlertRepository;
import com.gogidix.analytics.metrics.domain.repository.MetricDataPointRepository;
import com.gogidix.shared.audit.service.AuditService;
import com.gogidix.shared.exceptions.NotFoundException;
import com.gogidix.shared.exceptions.ValidationException;
import com.gogidix.shared.security.context.RequestContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MetricsCommandServiceTest {

    @Mock
    private MetricDataPointRepository metricRepository;

    @Mock
    private MetricAlertRepository alertRepository;

    @Mock
    private MetricEventPublisher eventPublisher;

    @Mock
    private AuditService auditService;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private MetricsCommandService service;

    private static final String TENANT_ID = "tenant-1";

    private IngestMetricCommand createIngestCommand() {
        return IngestMetricCommand.builder()
                .metricName("cpu.usage")
                .metricType(MetricDataPoint.MetricType.GAUGE)
                .metricValue(new BigDecimal("85.5"))
                .unit("percent")
                .timestamp(LocalDateTime.now())
                .sourceService("order-service")
                .tags(Map.of("env", "prod"))
                .dimensions(Map.of("region", "us-east"))
                .aggregationLevel("raw")
                .build();
    }

    private MetricAlert createAlert(String id, String tenantId) {
        return MetricAlert.builder()
                .id(id)
                .alertName("High CPU")
                .metricName("cpu.usage")
                .conditionType(MetricAlert.ConditionType.GREATER_THAN)
                .thresholdValue(new BigDecimal("90"))
                .severity(MetricAlert.AlertSeverity.CRITICAL)
                .status(MetricAlert.AlertStatus.ACTIVE)
                .enabled(true)
                .tenantId(tenantId)
                .cooldownSeconds(300)
                .triggerCount(0)
                .build();
    }

    @Test
    void ingestMetric_savesAndPublishesEvent() throws Exception {
        IngestMetricCommand cmd = createIngestCommand();
        when(objectMapper.writeValueAsString(any())).thenReturn("{\"env\":\"prod\"}");

        try (MockedStatic<RequestContext> mocked = mockStatic(RequestContext.class)) {
            mocked.when(RequestContext::getTenantIdFromThreadLocal).thenReturn(TENANT_ID);
            when(metricRepository.save(any(MetricDataPoint.class))).thenAnswer(inv -> inv.getArgument(0));
            when(alertRepository.findByTenantIdAndMetricName(eq(TENANT_ID), eq("cpu.usage")))
                    .thenReturn(Collections.emptyList());

            MetricDataPoint result = service.ingestMetric(cmd);

            assertNotNull(result);
            assertEquals("cpu.usage", result.getMetricName());
            assertEquals(TENANT_ID, result.getTenantId());
            verify(eventPublisher).publishMetricIngested(result);
            verify(metricRepository).save(any(MetricDataPoint.class));
        }
    }

    @Test
    void ingestMetric_nullTimestamp_defaultsToNow() throws Exception {
        IngestMetricCommand cmd = IngestMetricCommand.builder()
                .metricName("test")
                .metricType(MetricDataPoint.MetricType.COUNTER)
                .metricValue(BigDecimal.ONE)
                .sourceService("svc")
                .timestamp(null)
                .build();

        try (MockedStatic<RequestContext> mocked = mockStatic(RequestContext.class)) {
            mocked.when(RequestContext::getTenantIdFromThreadLocal).thenReturn(TENANT_ID);
            when(metricRepository.save(any(MetricDataPoint.class))).thenAnswer(inv -> inv.getArgument(0));
            when(alertRepository.findByTenantIdAndMetricName(any(), any())).thenReturn(Collections.emptyList());

            MetricDataPoint result = service.ingestMetric(cmd);
            assertNotNull(result.getTimestamp());
        }
    }

    @Test
    void ingestMetric_nullTags_skipsConversion() {
        IngestMetricCommand cmd = IngestMetricCommand.builder()
                .metricName("test")
                .metricType(MetricDataPoint.MetricType.COUNTER)
                .metricValue(BigDecimal.ONE)
                .sourceService("svc")
                .tags(null)
                .dimensions(null)
                .build();

        try (MockedStatic<RequestContext> mocked = mockStatic(RequestContext.class)) {
            mocked.when(RequestContext::getTenantIdFromThreadLocal).thenReturn(TENANT_ID);
            when(metricRepository.save(any(MetricDataPoint.class))).thenAnswer(inv -> inv.getArgument(0));
            when(alertRepository.findByTenantIdAndMetricName(any(), any())).thenReturn(Collections.emptyList());

            MetricDataPoint result = service.ingestMetric(cmd);
            assertNull(result.getTags());
            assertNull(result.getDimensions());
        }
    }

    @Test
    void ingestMetric_noTenantId_throwsValidationException() {
        IngestMetricCommand cmd = createIngestCommand();

        try (MockedStatic<RequestContext> mocked = mockStatic(RequestContext.class)) {
            mocked.when(RequestContext::getTenantIdFromThreadLocal).thenReturn(null);

            assertThrows(ValidationException.class, () -> service.ingestMetric(cmd));
        }
    }

    @Test
    void ingestMetric_jsonProcessingException_returnsNull() throws Exception {
        IngestMetricCommand cmd = IngestMetricCommand.builder()
                .metricName("test")
                .metricType(MetricDataPoint.MetricType.COUNTER)
                .metricValue(BigDecimal.ONE)
                .sourceService("svc")
                .tags(Map.of("k", "v"))
                .build();
        when(objectMapper.writeValueAsString(any())).thenThrow(new JsonProcessingException("err") {});

        try (MockedStatic<RequestContext> mocked = mockStatic(RequestContext.class)) {
            mocked.when(RequestContext::getTenantIdFromThreadLocal).thenReturn(TENANT_ID);
            when(metricRepository.save(any(MetricDataPoint.class))).thenAnswer(inv -> inv.getArgument(0));
            when(alertRepository.findByTenantIdAndMetricName(any(), any())).thenReturn(Collections.emptyList());

            MetricDataPoint result = service.ingestMetric(cmd);
            assertNull(result.getTags());
        }
    }

    @Test
    void ingestMetricsBatch_savesAll() throws Exception {
        IngestMetricCommand cmd = createIngestCommand();
        when(objectMapper.writeValueAsString(any())).thenReturn("{}");

        try (MockedStatic<RequestContext> mocked = mockStatic(RequestContext.class)) {
            mocked.when(RequestContext::getTenantIdFromThreadLocal).thenReturn(TENANT_ID);
            when(metricRepository.saveAll(anyList())).thenAnswer(inv -> inv.getArgument(0));

            List<MetricDataPoint> result = service.ingestMetricsBatch(List.of(cmd));

            assertEquals(1, result.size());
            verify(eventPublisher).publishMetricIngested(any());
        }
    }

    @Test
    void ingestMetricsBatch_noTenantId_throwsValidationException() {
        try (MockedStatic<RequestContext> mocked = mockStatic(RequestContext.class)) {
            mocked.when(RequestContext::getTenantIdFromThreadLocal).thenReturn(null);

            assertThrows(ValidationException.class,
                    () -> service.ingestMetricsBatch(List.of(createIngestCommand())));
        }
    }

    @Test
    void createAlert_savesAndAudits() {
        CreateMetricAlertCommand cmd = CreateMetricAlertCommand.builder()
                .alertName("High CPU")
                .metricName("cpu.usage")
                .conditionType(MetricAlert.ConditionType.GREATER_THAN)
                .thresholdValue(new BigDecimal("90"))
                .severity(MetricAlert.AlertSeverity.CRITICAL)
                .notificationChannels("email")
                .description("CPU alert")
                .createdBy("admin")
                .build();

        try (MockedStatic<RequestContext> mocked = mockStatic(RequestContext.class)) {
            mocked.when(RequestContext::getTenantIdFromThreadLocal).thenReturn(TENANT_ID);
            when(alertRepository.save(any(MetricAlert.class))).thenAnswer(inv -> inv.getArgument(0));

            MetricAlert result = service.createAlert(cmd);

            assertNotNull(result);
            assertEquals("High CPU", result.getAlertName());
            assertEquals(TENANT_ID, result.getTenantId());
            assertTrue(result.getEnabled());
            assertEquals(MetricAlert.AlertStatus.ACTIVE, result.getStatus());
            verify(auditService).logEvent(eq("METRIC_ALERT_CREATED"), eq("MetricAlert"), any(), anyString());
        }
    }

    @Test
    void createAlert_nullCooldown_defaultsTo300() {
        CreateMetricAlertCommand cmd = CreateMetricAlertCommand.builder()
                .alertName("test").metricName("m")
                .conditionType(MetricAlert.ConditionType.EQUALS)
                .cooldownSeconds(null)
                .build();

        try (MockedStatic<RequestContext> mocked = mockStatic(RequestContext.class)) {
            mocked.when(RequestContext::getTenantIdFromThreadLocal).thenReturn(TENANT_ID);
            when(alertRepository.save(any(MetricAlert.class))).thenAnswer(inv -> inv.getArgument(0));

            MetricAlert result = service.createAlert(cmd);
            assertEquals(300, result.getCooldownSeconds());
        }
    }

    @Test
    void createAlert_customCooldown() {
        CreateMetricAlertCommand cmd = CreateMetricAlertCommand.builder()
                .alertName("test").metricName("m")
                .conditionType(MetricAlert.ConditionType.EQUALS)
                .cooldownSeconds(600)
                .build();

        try (MockedStatic<RequestContext> mocked = mockStatic(RequestContext.class)) {
            mocked.when(RequestContext::getTenantIdFromThreadLocal).thenReturn(TENANT_ID);
            when(alertRepository.save(any(MetricAlert.class))).thenAnswer(inv -> inv.getArgument(0));

            MetricAlert result = service.createAlert(cmd);
            assertEquals(600, result.getCooldownSeconds());
        }
    }

    @Test
    void createAlert_noTenantId_throwsValidationException() {
        CreateMetricAlertCommand cmd = CreateMetricAlertCommand.builder()
                .alertName("a").metricName("m")
                .conditionType(MetricAlert.ConditionType.EQUALS)
                .build();

        try (MockedStatic<RequestContext> mocked = mockStatic(RequestContext.class)) {
            mocked.when(RequestContext::getTenantIdFromThreadLocal).thenReturn(null);

            assertThrows(ValidationException.class, () -> service.createAlert(cmd));
        }
    }

    @Test
    void updateAlert_updatesAndAudits() {
        CreateMetricAlertCommand cmd = CreateMetricAlertCommand.builder()
                .alertName("Updated")
                .metricName("mem")
                .conditionType(MetricAlert.ConditionType.LESS_THAN)
                .thresholdValue(BigDecimal.ONE)
                .evaluationWindowSeconds(60)
                .severity(MetricAlert.AlertSeverity.INFO)
                .notificationChannels("slack")
                .description("updated")
                .cooldownSeconds(120)
                .build();

        MetricAlert existing = createAlert("a1", TENANT_ID);

        try (MockedStatic<RequestContext> mocked = mockStatic(RequestContext.class)) {
            mocked.when(RequestContext::getTenantIdFromThreadLocal).thenReturn(TENANT_ID);
            when(alertRepository.findById("a1")).thenReturn(Optional.of(existing));
            when(alertRepository.save(any(MetricAlert.class))).thenAnswer(inv -> inv.getArgument(0));

            MetricAlert result = service.updateAlert("a1", cmd);

            assertEquals("Updated", result.getAlertName());
            assertEquals("mem", result.getMetricName());
            assertEquals(MetricAlert.ConditionType.LESS_THAN, result.getConditionType());
            assertEquals(120, result.getCooldownSeconds());
            verify(auditService).logEvent(eq("METRIC_ALERT_UPDATED"), eq("MetricAlert"), any(), anyString());
        }
    }

    @Test
    void updateAlert_nullCooldown_keepsExisting() {
        CreateMetricAlertCommand cmd = CreateMetricAlertCommand.builder()
                .alertName("Updated").metricName("mem")
                .conditionType(MetricAlert.ConditionType.EQUALS)
                .cooldownSeconds(null)
                .build();

        MetricAlert existing = createAlert("a1", TENANT_ID);

        try (MockedStatic<RequestContext> mocked = mockStatic(RequestContext.class)) {
            mocked.when(RequestContext::getTenantIdFromThreadLocal).thenReturn(TENANT_ID);
            when(alertRepository.findById("a1")).thenReturn(Optional.of(existing));
            when(alertRepository.save(any(MetricAlert.class))).thenAnswer(inv -> inv.getArgument(0));

            MetricAlert result = service.updateAlert("a1", cmd);
            assertEquals(300, result.getCooldownSeconds());
        }
    }

    @Test
    void updateAlert_wrongTenant_throwsValidationException() {
        CreateMetricAlertCommand cmd = CreateMetricAlertCommand.builder()
                .alertName("a").metricName("m")
                .conditionType(MetricAlert.ConditionType.EQUALS)
                .build();

        try (MockedStatic<RequestContext> mocked = mockStatic(RequestContext.class)) {
            mocked.when(RequestContext::getTenantIdFromThreadLocal).thenReturn(TENANT_ID);
            when(alertRepository.findById("a1")).thenReturn(Optional.of(createAlert("a1", "other")));

            assertThrows(ValidationException.class, () -> service.updateAlert("a1", cmd));
        }
    }

    @Test
    void updateAlert_notFound_throwsNotFoundException() {
        CreateMetricAlertCommand cmd = CreateMetricAlertCommand.builder()
                .alertName("a").metricName("m")
                .conditionType(MetricAlert.ConditionType.EQUALS)
                .build();

        try (MockedStatic<RequestContext> mocked = mockStatic(RequestContext.class)) {
            mocked.when(RequestContext::getTenantIdFromThreadLocal).thenReturn(TENANT_ID);
            when(alertRepository.findById("missing")).thenReturn(Optional.empty());

            assertThrows(NotFoundException.class, () -> service.updateAlert("missing", cmd));
        }
    }

    @Test
    void updateAlert_noTenantId_throwsValidationException() {
        CreateMetricAlertCommand cmd = CreateMetricAlertCommand.builder()
                .alertName("a").metricName("m")
                .conditionType(MetricAlert.ConditionType.EQUALS)
                .build();

        try (MockedStatic<RequestContext> mocked = mockStatic(RequestContext.class)) {
            mocked.when(RequestContext::getTenantIdFromThreadLocal).thenReturn(null);

            assertThrows(ValidationException.class, () -> service.updateAlert("a1", cmd));
        }
    }

    @Test
    void deleteAlert_deletesAndAudits() {
        MetricAlert alert = createAlert("a1", TENANT_ID);

        try (MockedStatic<RequestContext> mocked = mockStatic(RequestContext.class)) {
            mocked.when(RequestContext::getTenantIdFromThreadLocal).thenReturn(TENANT_ID);
            when(alertRepository.findById("a1")).thenReturn(Optional.of(alert));

            service.deleteAlert("a1");

            verify(alertRepository).delete(alert);
            verify(auditService).logEvent(eq("METRIC_ALERT_DELETED"), eq("MetricAlert"), eq("a1"), anyString());
        }
    }

    @Test
    void deleteAlert_wrongTenant_throwsValidationException() {
        try (MockedStatic<RequestContext> mocked = mockStatic(RequestContext.class)) {
            mocked.when(RequestContext::getTenantIdFromThreadLocal).thenReturn(TENANT_ID);
            when(alertRepository.findById("a1")).thenReturn(Optional.of(createAlert("a1", "other")));

            assertThrows(ValidationException.class, () -> service.deleteAlert("a1"));
        }
    }

    @Test
    void deleteAlert_notFound_throwsNotFoundException() {
        try (MockedStatic<RequestContext> mocked = mockStatic(RequestContext.class)) {
            mocked.when(RequestContext::getTenantIdFromThreadLocal).thenReturn(TENANT_ID);
            when(alertRepository.findById("missing")).thenReturn(Optional.empty());

            assertThrows(NotFoundException.class, () -> service.deleteAlert("missing"));
        }
    }

    @Test
    void deleteAlert_noTenantId_throwsValidationException() {
        try (MockedStatic<RequestContext> mocked = mockStatic(RequestContext.class)) {
            mocked.when(RequestContext::getTenantIdFromThreadLocal).thenReturn(null);

            assertThrows(ValidationException.class, () -> service.deleteAlert("a1"));
        }
    }

    @Test
    void toggleAlert_enable_setsActiveStatus() {
        MetricAlert alert = createAlert("a1", TENANT_ID);
        alert.setEnabled(false);
        alert.setStatus(MetricAlert.AlertStatus.PAUSED);

        try (MockedStatic<RequestContext> mocked = mockStatic(RequestContext.class)) {
            mocked.when(RequestContext::getTenantIdFromThreadLocal).thenReturn(TENANT_ID);
            when(alertRepository.findById("a1")).thenReturn(Optional.of(alert));
            when(alertRepository.save(any(MetricAlert.class))).thenAnswer(inv -> inv.getArgument(0));

            MetricAlert result = service.toggleAlert("a1", true);

            assertTrue(result.getEnabled());
            assertEquals(MetricAlert.AlertStatus.ACTIVE, result.getStatus());
            verify(auditService).logEvent(eq("METRIC_ALERT_TOGGLED"), eq("MetricAlert"), eq("a1"), contains("Enabled"));
        }
    }

    @Test
    void toggleAlert_disable_setsPausedStatus() {
        MetricAlert alert = createAlert("a1", TENANT_ID);

        try (MockedStatic<RequestContext> mocked = mockStatic(RequestContext.class)) {
            mocked.when(RequestContext::getTenantIdFromThreadLocal).thenReturn(TENANT_ID);
            when(alertRepository.findById("a1")).thenReturn(Optional.of(alert));
            when(alertRepository.save(any(MetricAlert.class))).thenAnswer(inv -> inv.getArgument(0));

            MetricAlert result = service.toggleAlert("a1", false);

            assertFalse(result.getEnabled());
            assertEquals(MetricAlert.AlertStatus.PAUSED, result.getStatus());
            verify(auditService).logEvent(eq("METRIC_ALERT_TOGGLED"), eq("MetricAlert"), eq("a1"), contains("Disabled"));
        }
    }

    @Test
    void toggleAlert_wrongTenant_throwsValidationException() {
        try (MockedStatic<RequestContext> mocked = mockStatic(RequestContext.class)) {
            mocked.when(RequestContext::getTenantIdFromThreadLocal).thenReturn(TENANT_ID);
            when(alertRepository.findById("a1")).thenReturn(Optional.of(createAlert("a1", "other")));

            assertThrows(ValidationException.class, () -> service.toggleAlert("a1", true));
        }
    }

    @Test
    void toggleAlert_notFound_throwsNotFoundException() {
        try (MockedStatic<RequestContext> mocked = mockStatic(RequestContext.class)) {
            mocked.when(RequestContext::getTenantIdFromThreadLocal).thenReturn(TENANT_ID);
            when(alertRepository.findById("missing")).thenReturn(Optional.empty());

            assertThrows(NotFoundException.class, () -> service.toggleAlert("missing", true));
        }
    }

    @Test
    void toggleAlert_noTenantId_throwsValidationException() {
        try (MockedStatic<RequestContext> mocked = mockStatic(RequestContext.class)) {
            mocked.when(RequestContext::getTenantIdFromThreadLocal).thenReturn(null);

            assertThrows(ValidationException.class, () -> service.toggleAlert("a1", true));
        }
    }

    @Test
    void checkAlertsForMetric_triggersAlertWhenConditionMet() {
        MetricDataPoint dp = MetricDataPoint.builder()
                .metricName("cpu.usage")
                .metricValue(new BigDecimal("95"))
                .tenantId(TENANT_ID)
                .build();

        MetricAlert alert = createAlert("a1", TENANT_ID);

        try (MockedStatic<RequestContext> ignored = mockStatic(RequestContext.class)) {
            when(alertRepository.findByTenantIdAndMetricName(TENANT_ID, "cpu.usage"))
                    .thenReturn(List.of(alert));
            when(alertRepository.save(any(MetricAlert.class))).thenAnswer(inv -> inv.getArgument(0));

            service.checkAlertsForMetric(dp);

            verify(eventPublisher).publishAlertTriggered(any(), contains("cpu.usage"));
        }
    }

    @Test
    void checkAlertsForMetric_skipsDisabledAlerts() {
        MetricDataPoint dp = MetricDataPoint.builder()
                .metricName("cpu.usage")
                .metricValue(new BigDecimal("95"))
                .tenantId(TENANT_ID)
                .build();

        MetricAlert alert = createAlert("a1", TENANT_ID);
        alert.setEnabled(false);

        try (MockedStatic<RequestContext> ignored = mockStatic(RequestContext.class)) {
            when(alertRepository.findByTenantIdAndMetricName(TENANT_ID, "cpu.usage"))
                    .thenReturn(List.of(alert));

            service.checkAlertsForMetric(dp);

            verify(eventPublisher, never()).publishAlertTriggered(any(), anyString());
        }
    }

    @Test
    void checkAlertsForMetric_skipsCooldownAlerts() {
        MetricDataPoint dp = MetricDataPoint.builder()
                .metricName("cpu.usage")
                .metricValue(new BigDecimal("95"))
                .tenantId(TENANT_ID)
                .build();

        MetricAlert alert = createAlert("a1", TENANT_ID);
        alert.setLastTriggeredAt(LocalDateTime.now());

        try (MockedStatic<RequestContext> ignored = mockStatic(RequestContext.class)) {
            when(alertRepository.findByTenantIdAndMetricName(TENANT_ID, "cpu.usage"))
                    .thenReturn(List.of(alert));

            service.checkAlertsForMetric(dp);

            verify(eventPublisher, never()).publishAlertTriggered(any(), anyString());
        }
    }

    @Test
    void checkAlertsForMetric_noAlertsFound() {
        MetricDataPoint dp = MetricDataPoint.builder()
                .metricName("cpu.usage")
                .metricValue(new BigDecimal("95"))
                .tenantId(TENANT_ID)
                .build();

        try (MockedStatic<RequestContext> ignored = mockStatic(RequestContext.class)) {
            when(alertRepository.findByTenantIdAndMetricName(TENANT_ID, "cpu.usage"))
                    .thenReturn(Collections.emptyList());

            service.checkAlertsForMetric(dp);

            verify(alertRepository, never()).save(any());
        }
    }

    @Test
    void checkAlertsForMetric_valueNotTriggering_doesNotTrigger() {
        MetricDataPoint dp = MetricDataPoint.builder()
                .metricName("cpu.usage")
                .metricValue(new BigDecimal("50"))
                .tenantId(TENANT_ID)
                .build();

        MetricAlert alert = createAlert("a1", TENANT_ID);

        try (MockedStatic<RequestContext> ignored = mockStatic(RequestContext.class)) {
            when(alertRepository.findByTenantIdAndMetricName(TENANT_ID, "cpu.usage"))
                    .thenReturn(List.of(alert));

            service.checkAlertsForMetric(dp);

            verify(eventPublisher, never()).publishAlertTriggered(any(), anyString());
        }
    }

    @Test
    void checkAlertsForMetric_nullThreshold_doesNotTrigger() {
        MetricDataPoint dp = MetricDataPoint.builder()
                .metricName("cpu.usage")
                .metricValue(new BigDecimal("999"))
                .tenantId(TENANT_ID)
                .build();

        MetricAlert alert = createAlert("a1", TENANT_ID);
        alert.setThresholdValue(null);

        try (MockedStatic<RequestContext> ignored = mockStatic(RequestContext.class)) {
            when(alertRepository.findByTenantIdAndMetricName(TENANT_ID, "cpu.usage"))
                    .thenReturn(List.of(alert));

            service.checkAlertsForMetric(dp);

            verify(eventPublisher, never()).publishAlertTriggered(any(), anyString());
        }
    }

    @Test
    void evaluateAlert_allConditionTypes() {
        MetricAlert lessThan = MetricAlert.builder()
                .conditionType(MetricAlert.ConditionType.LESS_THAN)
                .thresholdValue(new BigDecimal("50")).build();

        MetricAlert equals = MetricAlert.builder()
                .conditionType(MetricAlert.ConditionType.EQUALS)
                .thresholdValue(new BigDecimal("50")).build();

        MetricAlert notEquals = MetricAlert.builder()
                .conditionType(MetricAlert.ConditionType.NOT_EQUALS)
                .thresholdValue(new BigDecimal("50")).build();

        MetricAlert gte = MetricAlert.builder()
                .conditionType(MetricAlert.ConditionType.GREATER_THAN_OR_EQUAL)
                .thresholdValue(new BigDecimal("50")).build();

        MetricAlert lte = MetricAlert.builder()
                .conditionType(MetricAlert.ConditionType.LESS_THAN_OR_EQUAL)
                .thresholdValue(new BigDecimal("50")).build();

        MetricDataPoint dp50 = MetricDataPoint.builder()
                .metricValue(new BigDecimal("50")).tenantId(TENANT_ID).build();
        MetricDataPoint dp100 = MetricDataPoint.builder()
                .metricValue(new BigDecimal("100")).tenantId(TENANT_ID).build();

        try (MockedStatic<RequestContext> ignored = mockStatic(RequestContext.class)) {
            when(alertRepository.findByTenantIdAndMetricName(any(), any())).thenReturn(List.of(lessThan, equals, notEquals, gte, lte));
            when(alertRepository.save(any(MetricAlert.class))).thenAnswer(inv -> inv.getArgument(0));

            service.checkAlertsForMetric(dp100);
            service.checkAlertsForMetric(dp50);
        }
    }

    @Test
    void ingestMetric_emptyTags_skipsConversion() {
        IngestMetricCommand cmd = IngestMetricCommand.builder()
                .metricName("test")
                .metricType(MetricDataPoint.MetricType.COUNTER)
                .metricValue(BigDecimal.ONE)
                .sourceService("svc")
                .tags(Collections.emptyMap())
                .dimensions(Collections.emptyMap())
                .build();

        try (MockedStatic<RequestContext> mocked = mockStatic(RequestContext.class)) {
            mocked.when(RequestContext::getTenantIdFromThreadLocal).thenReturn(TENANT_ID);
            when(metricRepository.save(any(MetricDataPoint.class))).thenAnswer(inv -> inv.getArgument(0));
            when(alertRepository.findByTenantIdAndMetricName(any(), any())).thenReturn(Collections.emptyList());

            MetricDataPoint result = service.ingestMetric(cmd);
            assertNull(result.getTags());
            assertNull(result.getDimensions());
        }
    }

    @Test
    void ingestMetricsBatch_nullTimestamp_defaultsToNow() {
        IngestMetricCommand cmd = IngestMetricCommand.builder()
                .metricName("test")
                .metricType(MetricDataPoint.MetricType.COUNTER)
                .metricValue(BigDecimal.ONE)
                .sourceService("svc")
                .timestamp(null)
                .build();

        try (MockedStatic<RequestContext> mocked = mockStatic(RequestContext.class)) {
            mocked.when(RequestContext::getTenantIdFromThreadLocal).thenReturn(TENANT_ID);
            when(metricRepository.saveAll(anyList())).thenAnswer(inv -> inv.getArgument(0));

            List<MetricDataPoint> result = service.ingestMetricsBatch(List.of(cmd));
            assertNotNull(result.get(0).getTimestamp());
        }
    }

    @Test
    void ingestMetricsBatch_emptyTagsAndDimensions() {
        IngestMetricCommand cmd = IngestMetricCommand.builder()
                .metricName("test")
                .metricType(MetricDataPoint.MetricType.COUNTER)
                .metricValue(BigDecimal.ONE)
                .sourceService("svc")
                .tags(Collections.emptyMap())
                .dimensions(Collections.emptyMap())
                .build();

        try (MockedStatic<RequestContext> mocked = mockStatic(RequestContext.class)) {
            mocked.when(RequestContext::getTenantIdFromThreadLocal).thenReturn(TENANT_ID);
            when(metricRepository.saveAll(anyList())).thenAnswer(inv -> inv.getArgument(0));

            List<MetricDataPoint> result = service.ingestMetricsBatch(List.of(cmd));
            assertNull(result.get(0).getTags());
            assertNull(result.get(0).getDimensions());
        }
    }

    @Test
    void ingestMetricsBatch_nullTagsAndDimensions() {
        IngestMetricCommand cmd = IngestMetricCommand.builder()
                .metricName("test")
                .metricType(MetricDataPoint.MetricType.COUNTER)
                .metricValue(BigDecimal.ONE)
                .sourceService("svc")
                .tags(null)
                .dimensions(null)
                .build();

        try (MockedStatic<RequestContext> mocked = mockStatic(RequestContext.class)) {
            mocked.when(RequestContext::getTenantIdFromThreadLocal).thenReturn(TENANT_ID);
            when(metricRepository.saveAll(anyList())).thenAnswer(inv -> inv.getArgument(0));

            List<MetricDataPoint> result = service.ingestMetricsBatch(List.of(cmd));
            assertNull(result.get(0).getTags());
            assertNull(result.get(0).getDimensions());
        }
    }

    @Test
    void ingestMetric_dimensionsJsonProcessingException_returnsNullDimensions() throws Exception {
        IngestMetricCommand cmd = IngestMetricCommand.builder()
                .metricName("test")
                .metricType(MetricDataPoint.MetricType.COUNTER)
                .metricValue(BigDecimal.ONE)
                .sourceService("svc")
                .tags(null)
                .dimensions(Map.of("key", "value"))
                .build();

        when(objectMapper.writeValueAsString(any())).thenThrow(new JsonProcessingException("err") {});

        try (MockedStatic<RequestContext> mocked = mockStatic(RequestContext.class)) {
            mocked.when(RequestContext::getTenantIdFromThreadLocal).thenReturn(TENANT_ID);
            when(metricRepository.save(any(MetricDataPoint.class))).thenAnswer(inv -> inv.getArgument(0));
            when(alertRepository.findByTenantIdAndMetricName(any(), any())).thenReturn(Collections.emptyList());

            MetricDataPoint result = service.ingestMetric(cmd);
            assertNull(result.getDimensions());
        }
    }

    @Test
    void ingestMetric_tagsAndDimensionsBothSerialize() throws Exception {
        IngestMetricCommand cmd = IngestMetricCommand.builder()
                .metricName("test")
                .metricType(MetricDataPoint.MetricType.COUNTER)
                .metricValue(BigDecimal.ONE)
                .sourceService("svc")
                .tags(Map.of("env", "prod"))
                .dimensions(Map.of("region", "us-east"))
                .build();

        when(objectMapper.writeValueAsString(any())).thenReturn("{\"json\":true}");

        try (MockedStatic<RequestContext> mocked = mockStatic(RequestContext.class)) {
            mocked.when(RequestContext::getTenantIdFromThreadLocal).thenReturn(TENANT_ID);
            when(metricRepository.save(any(MetricDataPoint.class))).thenAnswer(inv -> inv.getArgument(0));
            when(alertRepository.findByTenantIdAndMetricName(any(), any())).thenReturn(Collections.emptyList());

            MetricDataPoint result = service.ingestMetric(cmd);
            assertEquals("{\"json\":true}", result.getTags());
            assertEquals("{\"json\":true}", result.getDimensions());
        }
    }

    @Test
    void checkAlertsForMetric_rateIncreasesBy_doesNotTrigger() {
        MetricDataPoint dp = MetricDataPoint.builder()
                .metricName("cpu.usage")
                .metricValue(new BigDecimal("95"))
                .tenantId(TENANT_ID)
                .build();

        MetricAlert alert = MetricAlert.builder()
                .id("a1")
                .alertName("Rate Increase")
                .metricName("cpu.usage")
                .conditionType(MetricAlert.ConditionType.RATE_INCREASES_BY)
                .thresholdValue(new BigDecimal("50"))
                .enabled(true)
                .tenantId(TENANT_ID)
                .cooldownSeconds(300)
                .triggerCount(0)
                .build();

        try (MockedStatic<RequestContext> ignored = mockStatic(RequestContext.class)) {
            when(alertRepository.findByTenantIdAndMetricName(TENANT_ID, "cpu.usage"))
                    .thenReturn(List.of(alert));

            service.checkAlertsForMetric(dp);

            verify(eventPublisher, never()).publishAlertTriggered(any(), anyString());
            verify(alertRepository, never()).save(any());
        }
    }

    @Test
    void checkAlertsForMetric_rateDecreasesBy_doesNotTrigger() {
        MetricDataPoint dp = MetricDataPoint.builder()
                .metricName("cpu.usage")
                .metricValue(new BigDecimal("95"))
                .tenantId(TENANT_ID)
                .build();

        MetricAlert alert = MetricAlert.builder()
                .id("a1")
                .alertName("Rate Decrease")
                .metricName("cpu.usage")
                .conditionType(MetricAlert.ConditionType.RATE_DECREASES_BY)
                .thresholdValue(new BigDecimal("50"))
                .enabled(true)
                .tenantId(TENANT_ID)
                .cooldownSeconds(300)
                .triggerCount(0)
                .build();

        try (MockedStatic<RequestContext> ignored = mockStatic(RequestContext.class)) {
            when(alertRepository.findByTenantIdAndMetricName(TENANT_ID, "cpu.usage"))
                    .thenReturn(List.of(alert));

            service.checkAlertsForMetric(dp);

            verify(eventPublisher, never()).publishAlertTriggered(any(), anyString());
            verify(alertRepository, never()).save(any());
        }
    }

    @Test
    void checkAlertsForMetric_multipleAlerts_partialTrigger() {
        MetricDataPoint dp = MetricDataPoint.builder()
                .metricName("cpu.usage")
                .metricValue(new BigDecimal("95"))
                .tenantId(TENANT_ID)
                .build();

        MetricAlert alert1 = createAlert("a1", TENANT_ID);
        MetricAlert alert2 = MetricAlert.builder()
                .id("a2")
                .alertName("Disabled Alert")
                .metricName("cpu.usage")
                .conditionType(MetricAlert.ConditionType.GREATER_THAN)
                .thresholdValue(new BigDecimal("90"))
                .enabled(false)
                .tenantId(TENANT_ID)
                .cooldownSeconds(300)
                .triggerCount(0)
                .build();
        MetricAlert alert3 = MetricAlert.builder()
                .id("a3")
                .alertName("Low CPU")
                .metricName("cpu.usage")
                .conditionType(MetricAlert.ConditionType.LESS_THAN)
                .thresholdValue(new BigDecimal("50"))
                .enabled(true)
                .tenantId(TENANT_ID)
                .cooldownSeconds(300)
                .triggerCount(0)
                .build();

        try (MockedStatic<RequestContext> ignored = mockStatic(RequestContext.class)) {
            when(alertRepository.findByTenantIdAndMetricName(TENANT_ID, "cpu.usage"))
                    .thenReturn(List.of(alert1, alert2, alert3));
            when(alertRepository.save(any(MetricAlert.class))).thenAnswer(inv -> inv.getArgument(0));

            service.checkAlertsForMetric(dp);

            verify(eventPublisher, times(1)).publishAlertTriggered(any(), contains("cpu.usage"));
            verify(alertRepository, times(1)).save(any());
        }
    }

    @Test
    void checkAlertsForMetric_greaterThanOrEqual_triggered() {
        MetricDataPoint dp = MetricDataPoint.builder()
                .metricName("cpu.usage")
                .metricValue(new BigDecimal("90"))
                .tenantId(TENANT_ID)
                .build();

        MetricAlert alert = MetricAlert.builder()
                .id("a1")
                .metricName("cpu.usage")
                .conditionType(MetricAlert.ConditionType.GREATER_THAN_OR_EQUAL)
                .thresholdValue(new BigDecimal("90"))
                .enabled(true)
                .tenantId(TENANT_ID)
                .cooldownSeconds(300)
                .triggerCount(0)
                .build();

        try (MockedStatic<RequestContext> ignored = mockStatic(RequestContext.class)) {
            when(alertRepository.findByTenantIdAndMetricName(TENANT_ID, "cpu.usage"))
                    .thenReturn(List.of(alert));
            when(alertRepository.save(any(MetricAlert.class))).thenAnswer(inv -> inv.getArgument(0));

            service.checkAlertsForMetric(dp);

            verify(eventPublisher).publishAlertTriggered(any(), anyString());
        }
    }

    @Test
    void checkAlertsForMetric_lessThanOrEqual_triggered() {
        MetricDataPoint dp = MetricDataPoint.builder()
                .metricName("mem.usage")
                .metricValue(new BigDecimal("10"))
                .tenantId(TENANT_ID)
                .build();

        MetricAlert alert = MetricAlert.builder()
                .id("a1")
                .metricName("mem.usage")
                .conditionType(MetricAlert.ConditionType.LESS_THAN_OR_EQUAL)
                .thresholdValue(new BigDecimal("10"))
                .enabled(true)
                .tenantId(TENANT_ID)
                .cooldownSeconds(300)
                .triggerCount(0)
                .build();

        try (MockedStatic<RequestContext> ignored = mockStatic(RequestContext.class)) {
            when(alertRepository.findByTenantIdAndMetricName(TENANT_ID, "mem.usage"))
                    .thenReturn(List.of(alert));
            when(alertRepository.save(any(MetricAlert.class))).thenAnswer(inv -> inv.getArgument(0));

            service.checkAlertsForMetric(dp);

            verify(eventPublisher).publishAlertTriggered(any(), anyString());
        }
    }

    @Test
    void checkAlertsForMetric_equals_triggered() {
        MetricDataPoint dp = MetricDataPoint.builder()
                .metricName("status")
                .metricValue(new BigDecimal("0"))
                .tenantId(TENANT_ID)
                .build();

        MetricAlert alert = MetricAlert.builder()
                .id("a1")
                .metricName("status")
                .conditionType(MetricAlert.ConditionType.EQUALS)
                .thresholdValue(new BigDecimal("0"))
                .enabled(true)
                .tenantId(TENANT_ID)
                .cooldownSeconds(300)
                .triggerCount(0)
                .build();

        try (MockedStatic<RequestContext> ignored = mockStatic(RequestContext.class)) {
            when(alertRepository.findByTenantIdAndMetricName(TENANT_ID, "status"))
                    .thenReturn(List.of(alert));
            when(alertRepository.save(any(MetricAlert.class))).thenAnswer(inv -> inv.getArgument(0));

            service.checkAlertsForMetric(dp);

            verify(eventPublisher).publishAlertTriggered(any(), anyString());
        }
    }

    @Test
    void checkAlertsForMetric_notEquals_triggered() {
        MetricDataPoint dp = MetricDataPoint.builder()
                .metricName("health")
                .metricValue(new BigDecimal("1"))
                .tenantId(TENANT_ID)
                .build();

        MetricAlert alert = MetricAlert.builder()
                .id("a1")
                .metricName("health")
                .conditionType(MetricAlert.ConditionType.NOT_EQUALS)
                .thresholdValue(new BigDecimal("0"))
                .enabled(true)
                .tenantId(TENANT_ID)
                .cooldownSeconds(300)
                .triggerCount(0)
                .build();

        try (MockedStatic<RequestContext> ignored = mockStatic(RequestContext.class)) {
            when(alertRepository.findByTenantIdAndMetricName(TENANT_ID, "health"))
                    .thenReturn(List.of(alert));
            when(alertRepository.save(any(MetricAlert.class))).thenAnswer(inv -> inv.getArgument(0));

            service.checkAlertsForMetric(dp);

            verify(eventPublisher).publishAlertTriggered(any(), anyString());
        }
    }

    @Test
    void ingestMetricsBatch_multipleCommandsWithMixedTimestamps() {
        IngestMetricCommand cmd1 = IngestMetricCommand.builder()
                .metricName("m1")
                .metricType(MetricDataPoint.MetricType.COUNTER)
                .metricValue(BigDecimal.ONE)
                .sourceService("svc")
                .timestamp(LocalDateTime.now())
                .build();
        IngestMetricCommand cmd2 = IngestMetricCommand.builder()
                .metricName("m2")
                .metricType(MetricDataPoint.MetricType.GAUGE)
                .metricValue(BigDecimal.TEN)
                .sourceService("svc")
                .timestamp(null)
                .build();

        try (MockedStatic<RequestContext> mocked = mockStatic(RequestContext.class)) {
            mocked.when(RequestContext::getTenantIdFromThreadLocal).thenReturn(TENANT_ID);
            when(metricRepository.saveAll(anyList())).thenAnswer(inv -> inv.getArgument(0));

            List<MetricDataPoint> result = service.ingestMetricsBatch(List.of(cmd1, cmd2));

            assertEquals(2, result.size());
            assertNotNull(result.get(0).getTimestamp());
            assertNotNull(result.get(1).getTimestamp());
            verify(eventPublisher, times(2)).publishMetricIngested(any());
        }
    }

    @Test
    void ingestMetricsBatch_setsTenantIdOnAll() {
        IngestMetricCommand cmd1 = IngestMetricCommand.builder()
                .metricName("m1")
                .metricType(MetricDataPoint.MetricType.COUNTER)
                .metricValue(BigDecimal.ONE)
                .sourceService("svc")
                .build();
        IngestMetricCommand cmd2 = IngestMetricCommand.builder()
                .metricName("m2")
                .metricType(MetricDataPoint.MetricType.GAUGE)
                .metricValue(BigDecimal.TEN)
                .sourceService("svc2")
                .build();

        try (MockedStatic<RequestContext> mocked = mockStatic(RequestContext.class)) {
            mocked.when(RequestContext::getTenantIdFromThreadLocal).thenReturn(TENANT_ID);
            when(metricRepository.saveAll(anyList())).thenAnswer(inv -> inv.getArgument(0));

            List<MetricDataPoint> result = service.ingestMetricsBatch(List.of(cmd1, cmd2));

            result.forEach(dp -> assertEquals(TENANT_ID, dp.getTenantId()));
        }
    }
}
