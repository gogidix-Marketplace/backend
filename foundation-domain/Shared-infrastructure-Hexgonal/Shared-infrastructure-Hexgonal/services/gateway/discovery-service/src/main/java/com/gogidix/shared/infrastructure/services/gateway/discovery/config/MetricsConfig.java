package com.gogidix.shared.infrastructure.services.gateway.discovery.config;

import com.gogidix.shared.infrastructure.services.gateway.discovery.application.port.in.ServiceRegistryQueryPort;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.jvm.JvmMemoryMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmThreadMetrics;
import io.micrometer.core.instrument.binder.system.ProcessorMetrics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for custom metrics collection in Discovery Service.
 */
@Configuration
public class MetricsConfig {

    @Autowired(required = false)
    private MeterRegistry meterRegistry;

    @Autowired(required = false)
    private ServiceRegistryQueryPort registryQueryPort;

    /**
     * Bind JVM metrics to Micrometer.
     */
    @Bean
    public JvmMemoryMetrics jvmMemoryMetrics() {
        return new JvmMemoryMetrics();
    }

    /**
     * Bind JVM thread metrics.
     */
    @Bean
    public JvmThreadMetrics jvmThreadMetrics() {
        return new JvmThreadMetrics();
    }

    /**
     * Bind processor metrics.
     */
    @Bean
    public ProcessorMetrics processorMetrics() {
        return new ProcessorMetrics();
    }

    /**
     * Register custom Eureka registry metrics on application ready.
     */
    @org.springframework.context.event.EventListener({ApplicationReadyEvent.class})
    public void bindRegistryMetrics() {
        if (meterRegistry != null && registryQueryPort != null) {
            // Number of registered applications
            Gauge.builder("eureka.registry.applications.count", registryQueryPort, port -> port.getApplicationCount())
                  .description("Number of applications registered in Eureka registry")
                  .register(meterRegistry);

            // Number of registered instances
            Gauge.builder("eureka.registry.instances.count", registryQueryPort, ServiceRegistryQueryPort::getInstanceCount)
                  .description("Total number of instances registered in Eureka registry")
                  .register(meterRegistry);

            // Self-preservation mode status
            Gauge.builder("eureka.self.preservation.enabled", registryQueryPort,
                    port -> port.isSelfPreservationEnabled() ? 1 : 0)
                  .description("Whether self-preservation mode is enabled (1=yes, 0=no)")
                  .register(meterRegistry);
        }
    }
}
