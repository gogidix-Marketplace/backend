package com.gogidix.shared.warehousing.spaceallocation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Space Allocation Service Application
 */
@SpringBootApplication(scanBasePackages = "com.gogidix.shared.warehousing.spaceallocation")
public class SpaceAllocationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpaceAllocationServiceApplication.class, args);
    }
}
