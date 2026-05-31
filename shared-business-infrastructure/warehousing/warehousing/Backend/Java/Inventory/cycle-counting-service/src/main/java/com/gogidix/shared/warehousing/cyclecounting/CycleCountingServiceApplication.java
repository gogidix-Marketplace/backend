package com.gogidix.shared.warehousing.cyclecounting;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.gogidix.shared.warehousing.cyclecounting")
public class CycleCountingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CycleCountingServiceApplication.class, args);
    }
}
