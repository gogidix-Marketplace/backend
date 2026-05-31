package com.gogidix.globalbusinessmanagement.countryingestion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Main application class for Country Ingestion Service.
 * Handles ingestion of country data from various sources.
 */
@SpringBootApplication(scanBasePackages = {
    "com.gogidix.globalbusiness.countryingestion",
    "com.gogidix.globalbusinessmanagement.countryingestion"
})
@EnableMongoAuditing
@EnableKafka
@EnableAsync
@EnableScheduling
public class CountryIngestionApplication {

    public static void main(String[] args) {
        SpringApplication.run(CountryIngestionApplication.class, args);
    }
}
