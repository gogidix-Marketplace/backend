package com.gogidix.shared.warehousing.tenant;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Tenant Config Service Application
 *
 * Manages multi-tenant configurations for shared warehousing core
 */
@SpringBootApplication
public class TenantConfigServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(TenantConfigServiceApplication.class, args);
    }
}
