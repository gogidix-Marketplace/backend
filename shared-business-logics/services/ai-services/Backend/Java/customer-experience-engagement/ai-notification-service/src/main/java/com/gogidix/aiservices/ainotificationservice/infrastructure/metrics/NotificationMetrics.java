package com.gogidix.aiservices.ainotificationservice.infrastructure.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Micrometer metrics for AI Notification Service.
 * Tracks key performance indicators for SLO monitoring.
 */
@Component
public class NotificationMetrics {

    private final MeterRegistry meterRegistry;

    // Counters
    private final Counter notificationTotalCounter;
    private final Counter notificationSuccessCounter;
    private final Counter notificationFailureCounter;
    private final Counter notificationCancelledCounter;
    private final Counter emailNotificationCounter;
    private final Counter smsNotificationCounter;
    private final Counter pushNotificationCounter;
    private final Counter batchProcessingCounter;

    // Timers
    private final Timer sendNotificationTimer;
    private final Timer batchProcessingTimer;
    private final Timer deliveryTimer;

    public NotificationMetrics(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;

        // Initialize counters
        this.notificationTotalCounter = Counter.builder("notification.send.total")
                .description("Total number of notification send requests")
                .tag("service", "ai-notification")
                .register(meterRegistry);

        this.notificationSuccessCounter = Counter.builder("notification.send.success")
                .description("Number of successfully sent notifications")
                .tag("service", "ai-notification")
                .register(meterRegistry);

        this.notificationFailureCounter = Counter.builder("notification.send.failure")
                .description("Number of failed notification send attempts")
                .tag("service", "ai-notification")
                .register(meterRegistry);

        this.notificationCancelledCounter = Counter.builder("notification.cancelled")
                .description("Number of cancelled notifications")
                .tag("service", "ai-notification")
                .register(meterRegistry);

        this.emailNotificationCounter = Counter.builder("notification.email.total")
                .description("Number of email notifications sent")
                .tag("service", "ai-notification")
                .register(meterRegistry);

        this.smsNotificationCounter = Counter.builder("notification.sms.total")
                .description("Number of SMS notifications sent")
                .tag("service", "ai-notification")
                .register(meterRegistry);

        this.pushNotificationCounter = Counter.builder("notification.push.total")
                .description("Number of push notifications sent")
                .tag("service", "ai-notification")
                .register(meterRegistry);

        this.batchProcessingCounter = Counter.builder("notification.batch.total")
                .description("Number of batch processing runs")
                .tag("service", "ai-notification")
                .register(meterRegistry);

        // Initialize timers
        this.sendNotificationTimer = Timer.builder("notification.send.duration")
                .description("Notification send processing time")
                .tag("service", "ai-notification")
                .publishPercentiles(0.5, 0.95, 0.99)
                .publishPercentileHistogram()
                .register(meterRegistry);

        this.batchProcessingTimer = Timer.builder("notification.batch.duration")
                .description("Batch processing time")
                .tag("service", "ai-notification")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);

        this.deliveryTimer = Timer.builder("notification.delivery.duration")
                .description("Notification delivery time")
                .tag("service", "ai-notification")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);
    }

    // Counter methods
    public void incrementNotificationTotal() {
        notificationTotalCounter.increment();
    }

    public void incrementNotificationSuccess() {
        notificationSuccessCounter.increment();
    }

    public void incrementNotificationFailure() {
        notificationFailureCounter.increment();
    }

    public void incrementNotificationCancelled() {
        notificationCancelledCounter.increment();
    }

    public void incrementEmailNotification() {
        emailNotificationCounter.increment();
    }

    public void incrementSmsNotification() {
        smsNotificationCounter.increment();
    }

    public void incrementPushNotification() {
        pushNotificationCounter.increment();
    }

    public void incrementBatchProcessing() {
        batchProcessingCounter.increment();
    }

    // Timer methods
    public void recordSendNotificationTime(long durationMs) {
        sendNotificationTimer.record(durationMs, TimeUnit.MILLISECONDS);
    }

    public Timer.Sample startSendNotificationTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopSendNotificationTimer(Timer.Sample sample) {
        sample.stop(sendNotificationTimer);
    }

    public Timer.Sample startBatchProcessingTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopBatchProcessingTimer(Timer.Sample sample) {
        sample.stop(batchProcessingTimer);
    }

    public void recordDeliveryTime(long durationMs) {
        deliveryTimer.record(durationMs, TimeUnit.MILLISECONDS);
    }

    public Timer.Sample startDeliveryTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopDeliveryTimer(Timer.Sample sample) {
        sample.stop(deliveryTimer);
    }

    // SLO compliance methods
    public double getSendNotificationLatencyP95() {
        return sendNotificationTimer.percentile(0.95, TimeUnit.MILLISECONDS);
    }

    public double getSendNotificationLatencyP99() {
        return sendNotificationTimer.percentile(0.99, TimeUnit.MILLISECONDS);
    }

    public double getBatchProcessingLatencyP95() {
        return batchProcessingTimer.percentile(0.95, TimeUnit.MILLISECONDS);
    }

    public double getBatchProcessingLatencyP99() {
        return batchProcessingTimer.percentile(0.99, TimeUnit.MILLISECONDS);
    }

    public double getErrorRate() {
        long total = (long) notificationTotalCounter.count();
        long failures = (long) notificationFailureCounter.count();
        return total > 0 ? (double) failures / total : 0.0;
    }

    public double getSuccessRate() {
        long total = (long) notificationTotalCounter.count();
        long successes = (long) notificationSuccessCounter.count();
        return total > 0 ? (double) successes / total : 0.0;
    }

    public MeterRegistry getMeterRegistry() {
        return meterRegistry;
    }
}
