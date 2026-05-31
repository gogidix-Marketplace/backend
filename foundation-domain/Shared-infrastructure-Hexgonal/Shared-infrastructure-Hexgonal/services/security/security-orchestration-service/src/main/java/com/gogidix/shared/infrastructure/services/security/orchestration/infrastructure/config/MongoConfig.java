package com.gogidix.shared.infrastructure.services.security.orchestration.infrastructure.config;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
@Configuration
@EnableMongoRepositories(basePackages = "com.gogidix.shared.advanced_security.securityorchestration.infrastructure.persistence")
@EnableMongoAuditing
public class MongoConfig {}
