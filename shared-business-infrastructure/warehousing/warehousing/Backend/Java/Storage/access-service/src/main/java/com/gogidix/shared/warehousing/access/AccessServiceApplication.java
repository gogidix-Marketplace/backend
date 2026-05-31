package com.gogidix.shared.warehousing.access;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Access Service Application
 */
@SpringBootApplication(scanBasePackages = "com.gogidix.shared.warehousing.access")
public class AccessServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AccessServiceApplication.class, args);
    }
}
