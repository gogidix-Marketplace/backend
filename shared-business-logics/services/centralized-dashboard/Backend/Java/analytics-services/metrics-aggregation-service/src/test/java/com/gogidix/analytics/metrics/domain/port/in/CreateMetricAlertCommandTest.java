package com.gogidix.analytics.metrics.domain.port.in;

import com.gogidix.analytics.metrics.domain.model.MetricAlert;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class CreateMetricAlertCommandTest {

    @Test
    void testBuilder() {
        CreateMetricAlertCommand cmd = CreateMetricAlertCommand.builder()
                .alertName("High CPU")
                .metricName("cpu.usage")
                .conditionType(MetricAlert.ConditionType.GREATER_THAN)
                .thresholdValue(new BigDecimal("90"))
                .evaluationWindowSeconds(300)
                .severity(MetricAlert.AlertSeverity.CRITICAL)
                .notificationChannels("email")
                .description("CPU alert")
                .cooldownSeconds(600)
                .createdBy("admin")
                .build();

        assertEquals("High CPU", cmd.getAlertName());
        assertEquals("cpu.usage", cmd.getMetricName());
        assertEquals(MetricAlert.ConditionType.GREATER_THAN, cmd.getConditionType());
        assertEquals(new BigDecimal("90"), cmd.getThresholdValue());
        assertEquals(300, cmd.getEvaluationWindowSeconds());
        assertEquals(MetricAlert.AlertSeverity.CRITICAL, cmd.getSeverity());
        assertEquals("email", cmd.getNotificationChannels());
        assertEquals("CPU alert", cmd.getDescription());
        assertEquals(600, cmd.getCooldownSeconds());
        assertEquals("admin", cmd.getCreatedBy());
    }

    @Test
    void testBuilderDefaults() {
        CreateMetricAlertCommand cmd = CreateMetricAlertCommand.builder()
                .alertName("a")
                .metricName("m")
                .conditionType(MetricAlert.ConditionType.EQUALS)
                .build();

        assertEquals(MetricAlert.AlertSeverity.WARNING, cmd.getSeverity());
    }

    @Test
    void testNoArgsConstructor() {
        CreateMetricAlertCommand cmd = new CreateMetricAlertCommand();
        assertNotNull(cmd);
        assertNull(cmd.getAlertName());
    }

    @Test
    void testAllArgsConstructor() {
        CreateMetricAlertCommand cmd = new CreateMetricAlertCommand(
                "n", "m", MetricAlert.ConditionType.LESS_THAN,
                BigDecimal.ONE, 60, MetricAlert.AlertSeverity.INFO, "ch", "d", 30, "u");

        assertEquals("n", cmd.getAlertName());
        assertEquals("m", cmd.getMetricName());
        assertEquals(MetricAlert.ConditionType.LESS_THAN, cmd.getConditionType());
        assertEquals(BigDecimal.ONE, cmd.getThresholdValue());
        assertEquals(60, cmd.getEvaluationWindowSeconds());
        assertEquals(MetricAlert.AlertSeverity.INFO, cmd.getSeverity());
        assertEquals("ch", cmd.getNotificationChannels());
        assertEquals("d", cmd.getDescription());
        assertEquals(30, cmd.getCooldownSeconds());
        assertEquals("u", cmd.getCreatedBy());
    }

    @Test
    void testSettersAndGetters() {
        CreateMetricAlertCommand cmd = new CreateMetricAlertCommand();
        cmd.setAlertName("name");
        cmd.setMetricName("metric");
        cmd.setConditionType(MetricAlert.ConditionType.NOT_EQUALS);
        cmd.setThresholdValue(BigDecimal.ZERO);
        cmd.setEvaluationWindowSeconds(10);
        cmd.setSeverity(MetricAlert.AlertSeverity.ERROR);
        cmd.setNotificationChannels("slack");
        cmd.setDescription("desc");
        cmd.setCooldownSeconds(120);
        cmd.setCreatedBy("user");

        assertEquals("name", cmd.getAlertName());
        assertEquals("metric", cmd.getMetricName());
        assertEquals(MetricAlert.ConditionType.NOT_EQUALS, cmd.getConditionType());
        assertEquals(BigDecimal.ZERO, cmd.getThresholdValue());
        assertEquals(10, cmd.getEvaluationWindowSeconds());
        assertEquals(MetricAlert.AlertSeverity.ERROR, cmd.getSeverity());
        assertEquals("slack", cmd.getNotificationChannels());
        assertEquals("desc", cmd.getDescription());
        assertEquals(120, cmd.getCooldownSeconds());
        assertEquals("user", cmd.getCreatedBy());
    }

    @Test
    void testEqualsAndHashCode() {
        CreateMetricAlertCommand c1 = CreateMetricAlertCommand.builder().alertName("a").build();
        CreateMetricAlertCommand c2 = CreateMetricAlertCommand.builder().alertName("a").build();
        assertEquals(c1, c2);
        assertEquals(c1.hashCode(), c2.hashCode());
    }

    @Test
    void testToString() {
        CreateMetricAlertCommand cmd = CreateMetricAlertCommand.builder().alertName("myAlert").build();
        assertNotNull(cmd.toString());
        assertTrue(cmd.toString().contains("myAlert"));
    }

    @Test
    void testEquals_sameReference() {
        CreateMetricAlertCommand cmd = new CreateMetricAlertCommand();
        assertEquals(cmd, cmd);
    }

    @Test
    void testEquals_null_returnsFalse() {
        CreateMetricAlertCommand cmd = new CreateMetricAlertCommand();
        assertNotEquals(null, cmd);
    }

    @Test
    void testEquals_differentType_returnsFalse() {
        CreateMetricAlertCommand cmd = new CreateMetricAlertCommand();
        assertNotEquals("not a command", cmd);
    }

    @Test
    void testEquals_fullyPopulatedEqual() {
        CreateMetricAlertCommand c1 = CreateMetricAlertCommand.builder()
                .alertName("High CPU").metricName("cpu.usage")
                .conditionType(MetricAlert.ConditionType.GREATER_THAN)
                .thresholdValue(new BigDecimal("90")).evaluationWindowSeconds(300)
                .severity(MetricAlert.AlertSeverity.CRITICAL)
                .notificationChannels("email").description("CPU alert")
                .cooldownSeconds(600).createdBy("admin")
                .build();
        CreateMetricAlertCommand c2 = CreateMetricAlertCommand.builder()
                .alertName("High CPU").metricName("cpu.usage")
                .conditionType(MetricAlert.ConditionType.GREATER_THAN)
                .thresholdValue(new BigDecimal("90")).evaluationWindowSeconds(300)
                .severity(MetricAlert.AlertSeverity.CRITICAL)
                .notificationChannels("email").description("CPU alert")
                .cooldownSeconds(600).createdBy("admin")
                .build();
        assertEquals(c1, c2);
        assertEquals(c1.hashCode(), c2.hashCode());
    }

    @Test
    void testEquals_differInAlertName() {
        CreateMetricAlertCommand c1 = CreateMetricAlertCommand.builder()
                .alertName("High CPU").metricName("cpu.usage")
                .conditionType(MetricAlert.ConditionType.GREATER_THAN)
                .thresholdValue(new BigDecimal("90")).evaluationWindowSeconds(300)
                .severity(MetricAlert.AlertSeverity.CRITICAL)
                .notificationChannels("email").description("CPU alert")
                .cooldownSeconds(600).createdBy("admin")
                .build();
        CreateMetricAlertCommand c2 = CreateMetricAlertCommand.builder()
                .alertName("Low CPU").metricName("cpu.usage")
                .conditionType(MetricAlert.ConditionType.GREATER_THAN)
                .thresholdValue(new BigDecimal("90")).evaluationWindowSeconds(300)
                .severity(MetricAlert.AlertSeverity.CRITICAL)
                .notificationChannels("email").description("CPU alert")
                .cooldownSeconds(600).createdBy("admin")
                .build();
        assertNotEquals(c1, c2);
    }

    @Test
    void testEquals_differInMetricName() {
        CreateMetricAlertCommand c1 = CreateMetricAlertCommand.builder()
                .alertName("Alert").metricName("cpu.usage")
                .conditionType(MetricAlert.ConditionType.GREATER_THAN)
                .thresholdValue(new BigDecimal("90")).evaluationWindowSeconds(300)
                .severity(MetricAlert.AlertSeverity.CRITICAL)
                .notificationChannels("email").description("CPU alert")
                .cooldownSeconds(600).createdBy("admin")
                .build();
        CreateMetricAlertCommand c2 = CreateMetricAlertCommand.builder()
                .alertName("Alert").metricName("mem.usage")
                .conditionType(MetricAlert.ConditionType.GREATER_THAN)
                .thresholdValue(new BigDecimal("90")).evaluationWindowSeconds(300)
                .severity(MetricAlert.AlertSeverity.CRITICAL)
                .notificationChannels("email").description("CPU alert")
                .cooldownSeconds(600).createdBy("admin")
                .build();
        assertNotEquals(c1, c2);
    }

    @Test
    void testEquals_differInConditionType() {
        CreateMetricAlertCommand c1 = CreateMetricAlertCommand.builder()
                .alertName("Alert").metricName("cpu")
                .conditionType(MetricAlert.ConditionType.GREATER_THAN)
                .thresholdValue(new BigDecimal("90")).evaluationWindowSeconds(300)
                .severity(MetricAlert.AlertSeverity.CRITICAL)
                .notificationChannels("email").description("CPU alert")
                .cooldownSeconds(600).createdBy("admin")
                .build();
        CreateMetricAlertCommand c2 = CreateMetricAlertCommand.builder()
                .alertName("Alert").metricName("cpu")
                .conditionType(MetricAlert.ConditionType.LESS_THAN)
                .thresholdValue(new BigDecimal("90")).evaluationWindowSeconds(300)
                .severity(MetricAlert.AlertSeverity.CRITICAL)
                .notificationChannels("email").description("CPU alert")
                .cooldownSeconds(600).createdBy("admin")
                .build();
        assertNotEquals(c1, c2);
    }

    @Test
    void testEquals_differInThresholdValue() {
        CreateMetricAlertCommand c1 = CreateMetricAlertCommand.builder()
                .alertName("Alert").metricName("cpu")
                .conditionType(MetricAlert.ConditionType.GREATER_THAN)
                .thresholdValue(new BigDecimal("90")).evaluationWindowSeconds(300)
                .severity(MetricAlert.AlertSeverity.CRITICAL)
                .notificationChannels("email").description("CPU alert")
                .cooldownSeconds(600).createdBy("admin")
                .build();
        CreateMetricAlertCommand c2 = CreateMetricAlertCommand.builder()
                .alertName("Alert").metricName("cpu")
                .conditionType(MetricAlert.ConditionType.GREATER_THAN)
                .thresholdValue(new BigDecimal("80")).evaluationWindowSeconds(300)
                .severity(MetricAlert.AlertSeverity.CRITICAL)
                .notificationChannels("email").description("CPU alert")
                .cooldownSeconds(600).createdBy("admin")
                .build();
        assertNotEquals(c1, c2);
    }

    @Test
    void testHashCode_fullyPopulated() {
        CreateMetricAlertCommand cmd = CreateMetricAlertCommand.builder()
                .alertName("High CPU").metricName("cpu.usage")
                .conditionType(MetricAlert.ConditionType.GREATER_THAN)
                .thresholdValue(new BigDecimal("90")).evaluationWindowSeconds(300)
                .severity(MetricAlert.AlertSeverity.CRITICAL)
                .notificationChannels("email").description("CPU alert")
                .cooldownSeconds(600).createdBy("admin")
                .build();
        int h1 = cmd.hashCode();
        cmd.setAlertName("Low CPU");
        int h2 = cmd.hashCode();
        assertNotEquals(h1, h2);
    }

    @Test
    void testEquals_oneNullOneSet_differ() {
        CreateMetricAlertCommand c1 = CreateMetricAlertCommand.builder()
                .alertName("Alert").metricName("cpu")
                .conditionType(MetricAlert.ConditionType.GREATER_THAN).build();
        CreateMetricAlertCommand c2 = CreateMetricAlertCommand.builder()
                .alertName("Alert").metricName("cpu")
                .conditionType(MetricAlert.ConditionType.GREATER_THAN)
                .thresholdValue(new BigDecimal("90")).build();
        assertNotEquals(c1, c2);
    }

    @Test
    void testEquals_differInCreatedBy() {
        CreateMetricAlertCommand c1 = CreateMetricAlertCommand.builder()
                .alertName("Alert").metricName("cpu")
                .conditionType(MetricAlert.ConditionType.GREATER_THAN)
                .thresholdValue(new BigDecimal("90")).evaluationWindowSeconds(300)
                .severity(MetricAlert.AlertSeverity.CRITICAL)
                .notificationChannels("email").description("CPU alert")
                .cooldownSeconds(600).createdBy("admin")
                .build();
        CreateMetricAlertCommand c2 = CreateMetricAlertCommand.builder()
                .alertName("Alert").metricName("cpu")
                .conditionType(MetricAlert.ConditionType.GREATER_THAN)
                .thresholdValue(new BigDecimal("90")).evaluationWindowSeconds(300)
                .severity(MetricAlert.AlertSeverity.CRITICAL)
                .notificationChannels("email").description("CPU alert")
                .cooldownSeconds(600).createdBy("user")
                .build();
        assertNotEquals(c1, c2);
    }
}
