package com.gogidix.aiservices.ainotificationservice.infrastructure.metrics;

import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.*;
import static org.assertj.core.api.Assertions.*;

class NotificationMetricsGenTest {
    private NotificationMetrics metrics;

    @BeforeEach
    void setup() { metrics = new NotificationMetrics(new SimpleMeterRegistry()); }

    @Test
    void incrementNotificationTotal() { metrics.incrementNotificationTotal(); }

    @Test
    void incrementNotificationSuccess() { metrics.incrementNotificationSuccess(); }

    @Test
    void incrementNotificationFailure() { metrics.incrementNotificationFailure(); }

    @Test
    void incrementNotificationCancelled() { metrics.incrementNotificationCancelled(); }

    @Test
    void incrementEmailNotification() { metrics.incrementEmailNotification(); }

    @Test
    void incrementSmsNotification() { metrics.incrementSmsNotification(); }

    @Test
    void incrementPushNotification() { metrics.incrementPushNotification(); }

    @Test
    void incrementBatchProcessing() { metrics.incrementBatchProcessing(); }

    @Test
    void recordSendNotificationTime() { metrics.recordSendNotificationTime(100); }

    @Test
    void recordDeliveryTime() { metrics.recordDeliveryTime(100); }

    @Test
    void stopSendNotificationTimer() { var s = metrics.startSendNotificationTimer(); metrics.stopSendNotificationTimer(s); }

    @Test
    void stopBatchProcessingTimer() { var s = metrics.startBatchProcessingTimer(); metrics.stopBatchProcessingTimer(s); }

    @Test
    void stopDeliveryTimer() { var s = metrics.startDeliveryTimer(); metrics.stopDeliveryTimer(s); }

    @Test
    void getSendNotificationLatencyP95() { assertThat(metrics.getSendNotificationLatencyP95()).isNotNull(); }

    @Test
    void getBatchProcessingLatencyP95() { assertThat(metrics.getBatchProcessingLatencyP95()).isNotNull(); }

    @Test
    void getSendNotificationLatencyP99() { assertThat(metrics.getSendNotificationLatencyP99()).isNotNull(); }

    @Test
    void getBatchProcessingLatencyP99() { assertThat(metrics.getBatchProcessingLatencyP99()).isNotNull(); }

    @Test
    void getErrorRate() { assertThat(metrics.getErrorRate()).isNotNull(); }

    @Test
    void getMeterRegistry() { assertThat(metrics.getMeterRegistry()).isNotNull(); }

}
