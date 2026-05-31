package com.gogidix.globalbusiness.localization;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

/**
 * Main application class for Localization Service.
 */
@SpringBootApplication(scanBasePackages = {
    "com.gogidix.globalbusiness.localization"
})
@EnableMongoAuditing
public class LocalizationApplication {

    public static void main(String[] args) {
        SpringApplication.run(LocalizationApplication.class, args);
    }
}
