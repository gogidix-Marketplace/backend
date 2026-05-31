package com.gogidix.aiservices.aicustomerengagementservice.infrastructure.metrics;

import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.*;
import static org.assertj.core.api.Assertions.*;

class CustomerEngagementMetricsGenTest {
    private CustomerEngagementMetrics metrics;

    @BeforeEach
    void setup() { metrics = new CustomerEngagementMetrics(new SimpleMeterRegistry()); }

    @Test
    void incrementEngagementTotal() { metrics.incrementEngagementTotal(); }

    @Test
    void incrementEngagementSuccess() { metrics.incrementEngagementSuccess(); }

    @Test
    void incrementEngagementFailure() { metrics.incrementEngagementFailure(); }

    @Test
    void incrementCampaignsCreated() { metrics.incrementCampaignsCreated(); }

    @Test
    void incrementMessagesSent() { metrics.incrementMessagesSent(); }

    @Test
    void incrementInteractionsRecorded() { metrics.incrementInteractionsRecorded(); }

    @Test
    void recordEngagementTime() { metrics.recordEngagementTime(100); }

    @Test
    void stopEngagementTimer() { var s = metrics.startEngagementTimer(); metrics.stopEngagementTimer(s); }

    @Test
    void stopCampaignCreationTimer() { var s = metrics.startCampaignCreationTimer(); metrics.stopCampaignCreationTimer(s); }

    @Test
    void stopMessageDeliveryTimer() { var s = metrics.startMessageDeliveryTimer(); metrics.stopMessageDeliveryTimer(s); }

    @Test
    void stopMlPredictionTimer() { var s = metrics.startMlPredictionTimer(); metrics.stopMlPredictionTimer(s); }

    @Test
    void getEngagementLatencyP95() { assertThat(metrics.getEngagementLatencyP95()).isNotNull(); }

    @Test
    void getMlPredictionLatencyP95() { assertThat(metrics.getMlPredictionLatencyP95()).isNotNull(); }

    @Test
    void getEngagementLatencyP99() { assertThat(metrics.getEngagementLatencyP99()).isNotNull(); }

    @Test
    void getErrorRate() { assertThat(metrics.getErrorRate()).isNotNull(); }

    @Test
    void getMeterRegistry() { assertThat(metrics.getMeterRegistry()).isNotNull(); }

}
