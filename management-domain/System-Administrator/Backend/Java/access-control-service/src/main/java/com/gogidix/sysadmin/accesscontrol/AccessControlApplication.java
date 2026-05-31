package com.gogidix.sysadmin.accesscontrol;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main application class for Access Control Service.
 * This service manages access policies, permissions, and role-based access control.
 */
@SpringBootApplication
public class AccessControlApplication {

    public static void main(String[] args) {
        SpringApplication.run(AccessControlApplication.class, args);
    }
}
