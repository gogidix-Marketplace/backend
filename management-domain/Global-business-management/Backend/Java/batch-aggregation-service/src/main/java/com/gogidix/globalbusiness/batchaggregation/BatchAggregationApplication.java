package com.gogidix.globalbusiness.batchaggregation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Main application class for Batch Aggregation Service.
 * This service handles batch aggregation of business data across regions and countries.
 *
 * <p>Key responsibilities:
 * <ul>
 *   <li>Aggregating business metrics from multiple data sources</li>
 *   <li>Processing scheduled batch jobs</li>
 *   <li>Computing regional and global summaries</li>
 *   <li>Storing aggregated results for dashboard consumption</li>
 * </ul>
 *
 * @author Gogidix
 * @version 1.0.0
 * @since 2024
 */
@SpringBootApplication(scanBasePackages = {
    "com.gogidix.globalbusiness.batchaggregation",
    "com.gogidix.globalbusinessmanagement"
})
@EnableMongoAuditing
@EnableScheduling
public class BatchAggregationApplication {

    /**
     * Main entry point for the Batch Aggregation Service.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(BatchAggregationApplication.class, args);
    }
}
