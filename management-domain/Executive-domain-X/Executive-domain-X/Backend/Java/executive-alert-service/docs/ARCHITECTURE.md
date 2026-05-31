# Executive-dashboard-service - Architecture Documentation

## Overview
The Executive-dashboard-service is a Spring Boot microservice for the Gogidix Management Domain Executive module.

## Technology Stack
- **Framework**: Spring Boot 3.1.5
- **Language**: Java 17
- **Database**: MongoDB
- **Cache**: Redis
- **Message Broker**: Apache Kafka

## Architecture Layers
- API Layer: REST Controllers
- Application Layer: Service Classes
- Domain Layer: Domain Models and Business Logic
- Infrastructure Layer: External Integrations

## Key Features
- Multi-tenant architecture
- Event-driven communication via Kafka
- Redis caching for performance
- MongoDB for persistence

## Deployment
Dockerized deployment with health checks on port 808X.
