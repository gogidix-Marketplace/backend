package com.gogidix.sales.analytics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Main Application class for Sales Analytics Service
 *
 * This service provides comprehensive sales analytics capabilities including:
 * - Sales performance analytics and metrics
 * - Team and individual sales rep performance tracking
 * - Conversion rate analysis
 * - Sales cycle analytics
 * - Pipeline velocity tracking
 * - Win/loss analysis
 * - Revenue forecasting analytics
 * - Custom report generation
 * - Dashboard widgets
 * - Real-time metrics calculation
 * - Multi-tenancy support
 * - Domain events for metric updates
 *
 * Architecture: Hexagonal (Ports and Adapters)
 * - Domain: Entities, Value Objects, Events, Repository Ports
 * - Application: Services, DTOs
 * - Infrastructure: MongoDB Repositories, Kafka Event Publishing
 * - Interfaces: REST Controllers
 */
@SpringBootApplication
@EnableKafka
@EnableAsync
@EnableScheduling
@EnableCaching
public class SalesAnalyticsApplication {

    public static void main(String[] args) {
        SpringApplication.run(SalesAnalyticsApplication.class, args);
    }
}
