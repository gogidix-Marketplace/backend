package com.gogidix.dashboard.gateway.chart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Main application class for Chart Service.
 *
 * <p>This service provides chart data and metrics for the centralized dashboard,
 * including data aggregation, caching, and real-time updates.</p>
 *
 * <p>Features:</p>
 * <ul>
 *   <li>Chart data aggregation and processing</li>
 *   <li>Time-series data management</li>
 *   <li>Caching for performance</li>
 *   <li>Tenant isolation</li>
 *   <li>Real-time data updates via WebSocket</li>
 * </ul>
 */
@SpringBootApplication
@EnableJpaAuditing
@EnableCaching
@EnableScheduling
public class ChartServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChartServiceApplication.class, args);
    }
}
