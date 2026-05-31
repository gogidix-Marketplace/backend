package com.gogidix.analytics.metrics.domain.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class MetricAlertTest {

    @Test
    void testBuilder() {
        MetricAlert alert = MetricAlert.builder()
                .id("alert-1")
                .alertName("High CPU")
                .metricName("cpu.usage")
                .conditionType(MetricAlert.ConditionType.GREATER_THAN)
                .thresholdValue(new BigDecimal("90.0"))
                .evaluationWindowSeconds(300)
                .severity(MetricAlert.AlertSeverity.CRITICAL)
                .status(MetricAlert.AlertStatus.ACTIVE)
                .enabled(true)
                .notificationChannels("email,slack")
                .description("CPU exceeds 90%")
                .tenantId("tenant-1")
                .lastTriggeredAt(null)
                .triggerCount(0)
                .cooldownSeconds(600)
                .createdBy("admin")
                .build();

        assertEquals("alert-1", alert.getId());
        assertEquals("High CPU", alert.getAlertName());
        assertEquals("cpu.usage", alert.getMetricName());
        assertEquals(MetricAlert.ConditionType.GREATER_THAN, alert.getConditionType());
        assertEquals(new BigDecimal("90.0"), alert.getThresholdValue());
        assertEquals(300, alert.getEvaluationWindowSeconds());
        assertEquals(MetricAlert.AlertSeverity.CRITICAL, alert.getSeverity());
        assertEquals(MetricAlert.AlertStatus.ACTIVE, alert.getStatus());
        assertTrue(alert.getEnabled());
        assertEquals("email,slack", alert.getNotificationChannels());
        assertEquals("CPU exceeds 90%", alert.getDescription());
        assertEquals("tenant-1", alert.getTenantId());
        assertNull(alert.getLastTriggeredAt());
        assertEquals(0, alert.getTriggerCount());
        assertEquals(600, alert.getCooldownSeconds());
        assertEquals("admin", alert.getCreatedBy());
    }

    @Test
    void testBuilderDefaults() {
        MetricAlert alert = MetricAlert.builder()
                .alertName("test")
                .metricName("test")
                .conditionType(MetricAlert.ConditionType.GREATER_THAN)
                .tenantId("t1")
                .build();

        assertEquals(MetricAlert.AlertSeverity.WARNING, alert.getSeverity());
        assertEquals(MetricAlert.AlertStatus.ACTIVE, alert.getStatus());
        assertTrue(alert.getEnabled());
        assertEquals(0, alert.getTriggerCount());
        assertEquals(300, alert.getCooldownSeconds());
    }

    @Test
    void testNoArgsConstructor() {
        MetricAlert alert = new MetricAlert();
        assertNotNull(alert);
        assertNull(alert.getId());
        assertNull(alert.getAlertName());
    }

    @Test
    void testAllArgsConstructor() {
        LocalDateTime now = LocalDateTime.now();
        MetricAlert alert = new MetricAlert(
                "id", "name", "metric", MetricAlert.ConditionType.LESS_THAN,
                BigDecimal.ONE, 60, MetricAlert.AlertSeverity.ERROR, MetricAlert.AlertStatus.PAUSED,
                false, "channels", "desc", "t1", now, 5, 120, "user", now, now);

        assertEquals("id", alert.getId());
        assertEquals("name", alert.getAlertName());
        assertEquals("metric", alert.getMetricName());
        assertEquals(MetricAlert.ConditionType.LESS_THAN, alert.getConditionType());
        assertEquals(BigDecimal.ONE, alert.getThresholdValue());
        assertEquals(60, alert.getEvaluationWindowSeconds());
        assertEquals(MetricAlert.AlertSeverity.ERROR, alert.getSeverity());
        assertEquals(MetricAlert.AlertStatus.PAUSED, alert.getStatus());
        assertFalse(alert.getEnabled());
        assertEquals("channels", alert.getNotificationChannels());
        assertEquals("desc", alert.getDescription());
        assertEquals("t1", alert.getTenantId());
        assertEquals(now, alert.getLastTriggeredAt());
        assertEquals(5, alert.getTriggerCount());
        assertEquals(120, alert.getCooldownSeconds());
        assertEquals("user", alert.getCreatedBy());
    }

    @Test
    void testSettersAndGetters() {
        MetricAlert alert = new MetricAlert();
        LocalDateTime now = LocalDateTime.now();

        alert.setId("new-id");
        alert.setAlertName("New Alert");
        alert.setMetricName("new.metric");
        alert.setConditionType(MetricAlert.ConditionType.EQUALS);
        alert.setThresholdValue(BigDecimal.ZERO);
        alert.setEvaluationWindowSeconds(30);
        alert.setSeverity(MetricAlert.AlertSeverity.INFO);
        alert.setStatus(MetricAlert.AlertStatus.TRIGGERED);
        alert.setEnabled(false);
        alert.setNotificationChannels("webhook");
        alert.setDescription("new desc");
        alert.setTenantId("t2");
        alert.setLastTriggeredAt(now);
        alert.setTriggerCount(10);
        alert.setCooldownSeconds(60);
        alert.setCreatedBy("user2");
        alert.setCreatedAt(now);
        alert.setUpdatedAt(now);

        assertEquals("new-id", alert.getId());
        assertEquals("New Alert", alert.getAlertName());
        assertEquals("new.metric", alert.getMetricName());
        assertEquals(MetricAlert.ConditionType.EQUALS, alert.getConditionType());
        assertEquals(BigDecimal.ZERO, alert.getThresholdValue());
        assertEquals(30, alert.getEvaluationWindowSeconds());
        assertEquals(MetricAlert.AlertSeverity.INFO, alert.getSeverity());
        assertEquals(MetricAlert.AlertStatus.TRIGGERED, alert.getStatus());
        assertFalse(alert.getEnabled());
        assertEquals("webhook", alert.getNotificationChannels());
        assertEquals("new desc", alert.getDescription());
        assertEquals("t2", alert.getTenantId());
        assertEquals(now, alert.getLastTriggeredAt());
        assertEquals(10, alert.getTriggerCount());
        assertEquals(60, alert.getCooldownSeconds());
        assertEquals("user2", alert.getCreatedBy());
    }

    @Test
    void testOnCreateSetsTimestampsAndId() {
        MetricAlert alert = MetricAlert.builder()
                .alertName("test")
                .metricName("test")
                .conditionType(MetricAlert.ConditionType.GREATER_THAN)
                .tenantId("t1")
                .build();

        assertNull(alert.getId());
        alert.onCreate();

        assertNotNull(alert.getCreatedAt());
        assertNotNull(alert.getUpdatedAt());
        assertNotNull(alert.getId());
    }

    @Test
    void testOnCreatePreservesExistingId() {
        MetricAlert alert = MetricAlert.builder()
                .id("custom-id")
                .alertName("test")
                .build();
        alert.onCreate();
        assertEquals("custom-id", alert.getId());
    }

    @Test
    void testOnUpdate() {
        MetricAlert alert = new MetricAlert();
        alert.setUpdatedAt(LocalDateTime.of(2020, 1, 1, 0, 0));
        alert.onUpdate();
        assertNotNull(alert.getUpdatedAt());
    }

    @Test
    void testIsCooldownPeriodWhenNeverTriggered() {
        MetricAlert alert = MetricAlert.builder()
                .cooldownSeconds(300)
                .build();
        assertFalse(alert.isCooldownPeriod());
    }

    @Test
    void testIsCooldownPeriodWhenRecentlyTriggered() {
        MetricAlert alert = MetricAlert.builder()
                .lastTriggeredAt(LocalDateTime.now())
                .cooldownSeconds(300)
                .build();
        assertTrue(alert.isCooldownPeriod());
    }

    @Test
    void testIsCooldownPeriodWhenExpired() {
        MetricAlert alert = MetricAlert.builder()
                .lastTriggeredAt(LocalDateTime.now().minusSeconds(600))
                .cooldownSeconds(300)
                .build();
        assertFalse(alert.isCooldownPeriod());
    }

    @Test
    void testMarkAsTriggered() {
        MetricAlert alert = MetricAlert.builder()
                .triggerCount(0)
                .build();

        assertNull(alert.getLastTriggeredAt());

        alert.markAsTriggered();

        assertNotNull(alert.getLastTriggeredAt());
        assertEquals(1, alert.getTriggerCount());

        alert.markAsTriggered();
        assertEquals(2, alert.getTriggerCount());
    }

    @Test
    void testConditionTypeEnumValues() {
        assertEquals(8, MetricAlert.ConditionType.values().length);
        assertNotNull(MetricAlert.ConditionType.valueOf("GREATER_THAN"));
        assertNotNull(MetricAlert.ConditionType.valueOf("LESS_THAN"));
        assertNotNull(MetricAlert.ConditionType.valueOf("EQUALS"));
        assertNotNull(MetricAlert.ConditionType.valueOf("NOT_EQUALS"));
        assertNotNull(MetricAlert.ConditionType.valueOf("GREATER_THAN_OR_EQUAL"));
        assertNotNull(MetricAlert.ConditionType.valueOf("LESS_THAN_OR_EQUAL"));
        assertNotNull(MetricAlert.ConditionType.valueOf("RATE_INCREASES_BY"));
        assertNotNull(MetricAlert.ConditionType.valueOf("RATE_DECREASES_BY"));
    }

    @Test
    void testAlertSeverityEnumValues() {
        assertEquals(4, MetricAlert.AlertSeverity.values().length);
        assertNotNull(MetricAlert.AlertSeverity.valueOf("INFO"));
        assertNotNull(MetricAlert.AlertSeverity.valueOf("WARNING"));
        assertNotNull(MetricAlert.AlertSeverity.valueOf("ERROR"));
        assertNotNull(MetricAlert.AlertSeverity.valueOf("CRITICAL"));
    }

    @Test
    void testAlertStatusEnumValues() {
        assertEquals(5, MetricAlert.AlertStatus.values().length);
        assertNotNull(MetricAlert.AlertStatus.valueOf("ACTIVE"));
        assertNotNull(MetricAlert.AlertStatus.valueOf("PAUSED"));
        assertNotNull(MetricAlert.AlertStatus.valueOf("TRIGGERED"));
        assertNotNull(MetricAlert.AlertStatus.valueOf("RESOLVED"));
        assertNotNull(MetricAlert.AlertStatus.valueOf("DISABLED"));
    }

    @Test
    void testEqualsAndHashCode() {
        MetricAlert a1 = MetricAlert.builder().id("x").alertName("n").build();
        MetricAlert a2 = MetricAlert.builder().id("x").alertName("n").build();
        assertEquals(a1, a2);
        assertEquals(a1.hashCode(), a2.hashCode());
    }

    @Test
    void testToString() {
        MetricAlert alert = MetricAlert.builder().alertName("myAlert").build();
        assertNotNull(alert.toString());
        assertTrue(alert.toString().contains("myAlert"));
    }

    @Test
    void testEquals_sameReference() {
        MetricAlert alert = new MetricAlert();
        assertEquals(alert, alert);
    }

    @Test
    void testEquals_null_returnsFalse() {
        MetricAlert alert = new MetricAlert();
        assertNotEquals(null, alert);
    }

    @Test
    void testEquals_differentType_returnsFalse() {
        MetricAlert alert = new MetricAlert();
        assertNotEquals("not an alert", alert);
    }

    @Test
    void testEquals_fullyPopulatedEqual() {
        LocalDateTime now = LocalDateTime.now();
        MetricAlert a1 = MetricAlert.builder()
                .id("a1").alertName("High CPU").metricName("cpu.usage")
                .conditionType(MetricAlert.ConditionType.GREATER_THAN)
                .thresholdValue(new BigDecimal("90")).evaluationWindowSeconds(300)
                .severity(MetricAlert.AlertSeverity.CRITICAL).status(MetricAlert.AlertStatus.ACTIVE)
                .enabled(true).notificationChannels("email").description("CPU alert")
                .tenantId("t1").lastTriggeredAt(now).triggerCount(5)
                .cooldownSeconds(600).createdBy("admin").createdAt(now).updatedAt(now)
                .build();
        MetricAlert a2 = MetricAlert.builder()
                .id("a1").alertName("High CPU").metricName("cpu.usage")
                .conditionType(MetricAlert.ConditionType.GREATER_THAN)
                .thresholdValue(new BigDecimal("90")).evaluationWindowSeconds(300)
                .severity(MetricAlert.AlertSeverity.CRITICAL).status(MetricAlert.AlertStatus.ACTIVE)
                .enabled(true).notificationChannels("email").description("CPU alert")
                .tenantId("t1").lastTriggeredAt(now).triggerCount(5)
                .cooldownSeconds(600).createdBy("admin").createdAt(now).updatedAt(now)
                .build();
        assertEquals(a1, a2);
        assertEquals(a1.hashCode(), a2.hashCode());
    }

    @Test
    void testEquals_fullyPopulatedDifferInId() {
        LocalDateTime now = LocalDateTime.now();
        MetricAlert a1 = MetricAlert.builder()
                .id("a1").alertName("High CPU").metricName("cpu.usage")
                .conditionType(MetricAlert.ConditionType.GREATER_THAN)
                .thresholdValue(new BigDecimal("90")).evaluationWindowSeconds(300)
                .severity(MetricAlert.AlertSeverity.CRITICAL).status(MetricAlert.AlertStatus.ACTIVE)
                .enabled(true).notificationChannels("email").description("CPU alert")
                .tenantId("t1").lastTriggeredAt(now).triggerCount(5)
                .cooldownSeconds(600).createdBy("admin").createdAt(now).updatedAt(now)
                .build();
        MetricAlert a2 = MetricAlert.builder()
                .id("a2").alertName("High CPU").metricName("cpu.usage")
                .conditionType(MetricAlert.ConditionType.GREATER_THAN)
                .thresholdValue(new BigDecimal("90")).evaluationWindowSeconds(300)
                .severity(MetricAlert.AlertSeverity.CRITICAL).status(MetricAlert.AlertStatus.ACTIVE)
                .enabled(true).notificationChannels("email").description("CPU alert")
                .tenantId("t1").lastTriggeredAt(now).triggerCount(5)
                .cooldownSeconds(600).createdBy("admin").createdAt(now).updatedAt(now)
                .build();
        assertNotEquals(a1, a2);
    }

    @Test
    void testEquals_differInAlertName() {
        LocalDateTime now = LocalDateTime.now();
        MetricAlert a1 = MetricAlert.builder()
                .id("a1").alertName("High CPU").metricName("cpu.usage")
                .conditionType(MetricAlert.ConditionType.GREATER_THAN)
                .thresholdValue(new BigDecimal("90")).evaluationWindowSeconds(300)
                .severity(MetricAlert.AlertSeverity.CRITICAL).status(MetricAlert.AlertStatus.ACTIVE)
                .enabled(true).notificationChannels("email").description("CPU alert")
                .tenantId("t1").lastTriggeredAt(now).triggerCount(5)
                .cooldownSeconds(600).createdBy("admin").createdAt(now).updatedAt(now)
                .build();
        MetricAlert a2 = MetricAlert.builder()
                .id("a1").alertName("Low CPU").metricName("cpu.usage")
                .conditionType(MetricAlert.ConditionType.GREATER_THAN)
                .thresholdValue(new BigDecimal("90")).evaluationWindowSeconds(300)
                .severity(MetricAlert.AlertSeverity.CRITICAL).status(MetricAlert.AlertStatus.ACTIVE)
                .enabled(true).notificationChannels("email").description("CPU alert")
                .tenantId("t1").lastTriggeredAt(now).triggerCount(5)
                .cooldownSeconds(600).createdBy("admin").createdAt(now).updatedAt(now)
                .build();
        assertNotEquals(a1, a2);
    }

    @Test
    void testEquals_differInMetricName() {
        LocalDateTime now = LocalDateTime.now();
        MetricAlert a1 = MetricAlert.builder()
                .id("a1").alertName("High CPU").metricName("cpu.usage")
                .conditionType(MetricAlert.ConditionType.GREATER_THAN)
                .thresholdValue(new BigDecimal("90")).evaluationWindowSeconds(300)
                .severity(MetricAlert.AlertSeverity.CRITICAL).status(MetricAlert.AlertStatus.ACTIVE)
                .enabled(true).notificationChannels("email").description("CPU alert")
                .tenantId("t1").lastTriggeredAt(now).triggerCount(5)
                .cooldownSeconds(600).createdBy("admin").createdAt(now).updatedAt(now)
                .build();
        MetricAlert a2 = MetricAlert.builder()
                .id("a1").alertName("High CPU").metricName("mem.usage")
                .conditionType(MetricAlert.ConditionType.GREATER_THAN)
                .thresholdValue(new BigDecimal("90")).evaluationWindowSeconds(300)
                .severity(MetricAlert.AlertSeverity.CRITICAL).status(MetricAlert.AlertStatus.ACTIVE)
                .enabled(true).notificationChannels("email").description("CPU alert")
                .tenantId("t1").lastTriggeredAt(now).triggerCount(5)
                .cooldownSeconds(600).createdBy("admin").createdAt(now).updatedAt(now)
                .build();
        assertNotEquals(a1, a2);
    }

    @Test
    void testEquals_differInConditionType() {
        LocalDateTime now = LocalDateTime.now();
        MetricAlert a1 = MetricAlert.builder()
                .id("a1").alertName("High CPU").metricName("cpu.usage")
                .conditionType(MetricAlert.ConditionType.GREATER_THAN)
                .thresholdValue(new BigDecimal("90")).evaluationWindowSeconds(300)
                .severity(MetricAlert.AlertSeverity.CRITICAL).status(MetricAlert.AlertStatus.ACTIVE)
                .enabled(true).notificationChannels("email").description("CPU alert")
                .tenantId("t1").lastTriggeredAt(now).triggerCount(5)
                .cooldownSeconds(600).createdBy("admin").createdAt(now).updatedAt(now)
                .build();
        MetricAlert a2 = MetricAlert.builder()
                .id("a1").alertName("High CPU").metricName("cpu.usage")
                .conditionType(MetricAlert.ConditionType.LESS_THAN)
                .thresholdValue(new BigDecimal("90")).evaluationWindowSeconds(300)
                .severity(MetricAlert.AlertSeverity.CRITICAL).status(MetricAlert.AlertStatus.ACTIVE)
                .enabled(true).notificationChannels("email").description("CPU alert")
                .tenantId("t1").lastTriggeredAt(now).triggerCount(5)
                .cooldownSeconds(600).createdBy("admin").createdAt(now).updatedAt(now)
                .build();
        assertNotEquals(a1, a2);
    }

    @Test
    void testEquals_differInTenantId() {
        LocalDateTime now = LocalDateTime.now();
        MetricAlert a1 = MetricAlert.builder()
                .id("a1").alertName("High CPU").metricName("cpu.usage")
                .conditionType(MetricAlert.ConditionType.GREATER_THAN)
                .thresholdValue(new BigDecimal("90")).evaluationWindowSeconds(300)
                .severity(MetricAlert.AlertSeverity.CRITICAL).status(MetricAlert.AlertStatus.ACTIVE)
                .enabled(true).notificationChannels("email").description("CPU alert")
                .tenantId("t1").lastTriggeredAt(now).triggerCount(5)
                .cooldownSeconds(600).createdBy("admin").createdAt(now).updatedAt(now)
                .build();
        MetricAlert a2 = MetricAlert.builder()
                .id("a1").alertName("High CPU").metricName("cpu.usage")
                .conditionType(MetricAlert.ConditionType.GREATER_THAN)
                .thresholdValue(new BigDecimal("90")).evaluationWindowSeconds(300)
                .severity(MetricAlert.AlertSeverity.CRITICAL).status(MetricAlert.AlertStatus.ACTIVE)
                .enabled(true).notificationChannels("email").description("CPU alert")
                .tenantId("t2").lastTriggeredAt(now).triggerCount(5)
                .cooldownSeconds(600).createdBy("admin").createdAt(now).updatedAt(now)
                .build();
        assertNotEquals(a1, a2);
    }

    @Test
    void testHashCode_fullyPopulated() {
        LocalDateTime now = LocalDateTime.now();
        MetricAlert alert = MetricAlert.builder()
                .id("a1").alertName("High CPU").metricName("cpu.usage")
                .conditionType(MetricAlert.ConditionType.GREATER_THAN)
                .thresholdValue(new BigDecimal("90")).evaluationWindowSeconds(300)
                .severity(MetricAlert.AlertSeverity.CRITICAL).status(MetricAlert.AlertStatus.ACTIVE)
                .enabled(true).notificationChannels("email").description("CPU alert")
                .tenantId("t1").lastTriggeredAt(now).triggerCount(5)
                .cooldownSeconds(600).createdBy("admin").createdAt(now).updatedAt(now)
                .build();
        int h1 = alert.hashCode();
        alert.setAlertName("Low CPU");
        int h2 = alert.hashCode();
        assertNotEquals(h1, h2);
    }

    @Test
    void testEquals_oneNullOneSet_differ() {
        MetricAlert a1 = MetricAlert.builder().id("x").alertName("n").build();
        MetricAlert a2 = MetricAlert.builder().id("x").alertName("n")
                .metricName("cpu").build();
        assertNotEquals(a1, a2);
    }

    @Test
    void testEquals_differInThresholdValue() {
        LocalDateTime now = LocalDateTime.now();
        MetricAlert a1 = MetricAlert.builder()
                .id("a1").alertName("High CPU").metricName("cpu.usage")
                .conditionType(MetricAlert.ConditionType.GREATER_THAN)
                .thresholdValue(new BigDecimal("90")).evaluationWindowSeconds(300)
                .severity(MetricAlert.AlertSeverity.CRITICAL).status(MetricAlert.AlertStatus.ACTIVE)
                .enabled(true).notificationChannels("email").description("CPU alert")
                .tenantId("t1").lastTriggeredAt(now).triggerCount(5)
                .cooldownSeconds(600).createdBy("admin").createdAt(now).updatedAt(now)
                .build();
        MetricAlert a2 = MetricAlert.builder()
                .id("a1").alertName("High CPU").metricName("cpu.usage")
                .conditionType(MetricAlert.ConditionType.GREATER_THAN)
                .thresholdValue(new BigDecimal("80")).evaluationWindowSeconds(300)
                .severity(MetricAlert.AlertSeverity.CRITICAL).status(MetricAlert.AlertStatus.ACTIVE)
                .enabled(true).notificationChannels("email").description("CPU alert")
                .tenantId("t1").lastTriggeredAt(now).triggerCount(5)
                .cooldownSeconds(600).createdBy("admin").createdAt(now).updatedAt(now)
                .build();
        assertNotEquals(a1, a2);
    }

    @Test
    void testEquals_differInEnabled() {
        LocalDateTime now = LocalDateTime.now();
        MetricAlert a1 = MetricAlert.builder()
                .id("a1").alertName("High CPU").metricName("cpu.usage")
                .conditionType(MetricAlert.ConditionType.GREATER_THAN)
                .thresholdValue(new BigDecimal("90")).evaluationWindowSeconds(300)
                .severity(MetricAlert.AlertSeverity.CRITICAL).status(MetricAlert.AlertStatus.ACTIVE)
                .enabled(true).notificationChannels("email").description("CPU alert")
                .tenantId("t1").lastTriggeredAt(now).triggerCount(5)
                .cooldownSeconds(600).createdBy("admin").createdAt(now).updatedAt(now)
                .build();
        MetricAlert a2 = MetricAlert.builder()
                .id("a1").alertName("High CPU").metricName("cpu.usage")
                .conditionType(MetricAlert.ConditionType.GREATER_THAN)
                .thresholdValue(new BigDecimal("90")).evaluationWindowSeconds(300)
                .severity(MetricAlert.AlertSeverity.CRITICAL).status(MetricAlert.AlertStatus.ACTIVE)
                .enabled(false).notificationChannels("email").description("CPU alert")
                .tenantId("t1").lastTriggeredAt(now).triggerCount(5)
                .cooldownSeconds(600).createdBy("admin").createdAt(now).updatedAt(now)
                .build();
        assertNotEquals(a1, a2);
    }
}
