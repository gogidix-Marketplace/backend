package com.gogidix.sysadmin.audit.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "com.gogidix.sysadmin.audit.domain.repository")
@EnableMongoAuditing
public class MongoConfig {
}
