package com.gogidix.shared.infrastructure.services.storage.filestorage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication(scanBasePackages = {
        "com.gogidix.shared.infrastructure.services.storage.filestorage",
        "com.gogidix.shared.infrastructure.core.tenancy",
        "com.gogidix.shared"
})
@EnableMongoAuditing
public class FileStorageServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(FileStorageServiceApplication.class, args);
    }
}
