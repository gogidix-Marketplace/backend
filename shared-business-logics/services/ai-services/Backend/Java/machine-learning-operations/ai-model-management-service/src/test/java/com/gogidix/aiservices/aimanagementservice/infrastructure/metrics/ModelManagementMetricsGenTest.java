package com.gogidix.aiservices.aimanagementservice.infrastructure.metrics;

import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.*;
import static org.assertj.core.api.Assertions.*;

class ModelManagementMetricsGenTest {

    private ModelManagementMetrics metrics;

    @BeforeEach
    void setup() {
        metrics = new ModelManagementMetrics(new SimpleMeterRegistry());
    }

    @Test
    void incrementModelRegistrationTotal() {
        metrics.incrementModelRegistrationTotal();
    }

    @Test
    void incrementModelRegistrationSuccess() {
        metrics.incrementModelRegistrationSuccess();
    }

    @Test
    void incrementModelRegistrationFailure() {
        metrics.incrementModelRegistrationFailure();
    }

    @Test
    void incrementModelLoaded() {
        metrics.incrementModelLoaded();
    }

    @Test
    void incrementModelUnloaded() {
        metrics.incrementModelUnloaded();
    }

    @Test
    void incrementModelDeployed() {
        metrics.incrementModelDeployed();
    }

    @Test
    void incrementModelRetired() {
        metrics.incrementModelRetired();
    }

    @Test
    void recordModelRegistrationTime() {
        metrics.recordModelRegistrationTime(100);
    }

    @Test
    void recordModelLoadingTime() {
        metrics.recordModelLoadingTime(50);
    }

    @Test
    void recordModelDeploymentTime() {
        metrics.recordModelDeploymentTime(75);
    }

    @Test
    void stopModelRegistrationTimer() {
        var sample = metrics.startModelRegistrationTimer();
        metrics.stopModelRegistrationTimer(sample);
    }

    @Test
    void stopModelLoadingTimer() {
        var sample = metrics.startModelLoadingTimer();
        metrics.stopModelLoadingTimer(sample);
    }

    @Test
    void stopModelDeploymentTimer() {
        var sample = metrics.startModelDeploymentTimer();
        metrics.stopModelDeploymentTimer(sample);
    }

    @Test
    void sloMethods() {
        assertThat(metrics.getModelRegistrationLatencyP95()).isGreaterThanOrEqualTo(0);
        assertThat(metrics.getModelRegistrationLatencyP99()).isGreaterThanOrEqualTo(0);
        assertThat(metrics.getErrorRate()).isGreaterThanOrEqualTo(0);
    }

    @Test
    void getMeterRegistry() {
        assertThat(metrics.getMeterRegistry()).isNotNull();
    }
}
