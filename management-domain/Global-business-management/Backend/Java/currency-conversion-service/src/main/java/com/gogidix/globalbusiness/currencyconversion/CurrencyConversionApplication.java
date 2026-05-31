package com.gogidix.globalbusiness.currencyconversion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Main application class for Currency Conversion Service.
 * Handles real-time currency conversion and exchange rate management.
 */
@SpringBootApplication(scanBasePackages = {
    "com.gogidix.globalbusiness.currencyconversion"
})
@EnableMongoAuditing
@EnableScheduling
public class CurrencyConversionApplication {

    public static void main(String[] args) {
        SpringApplication.run(CurrencyConversionApplication.class, args);
    }
}
