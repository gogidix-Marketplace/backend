package com.gogidix.aiservices.aiprediction;

import com.gogidix.aiservices.aiprediction.infrastructure.PredictionRepositoryImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableDiscoveryClient
public class AiPredictionServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AiPredictionServiceApplication.class, args);
    }

    @Bean
    public PredictionRepositoryImpl initializeRepository(PredictionRepositoryImpl repository) {
        repository.initializeDefaultModels();
        return repository;
    }
}
