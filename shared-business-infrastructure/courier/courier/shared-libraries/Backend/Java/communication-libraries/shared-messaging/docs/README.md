# 📨 Shared Messaging Library Documentation

## 🎯 Overview
The **Shared Messaging Library** is a comprehensive, enterprise-grade messaging service that provides unified messaging capabilities across the GOGIDIX ecosystem. Built with Spring Boot 3.1.5 and Java 17+, it offers multi-channel message delivery, template processing, and advanced queue management.

**Version**: 1.0.0  
**Service Port**: 8501  
**API Base Path**: `/api/messaging`

## 🚀 Quick Links
- [Architecture Documentation](./architecture/README.md)
- [Setup Guide](./setup/README.md)
- [Operations Guide](./operations/README.md)
- [API Documentation](../api-docs/openapi.yaml)
- [Development Phases](../)
  - [Phase 1: Hexagonal Architecture](../PHASE_1_HEXAGONAL_ARCHITECTURE_COMPLETE.md)
  - [Phase 2: Infrastructure Configuration](../PHASE_2_INFRASTRUCTURE_CONFIGURATION_COMPLETE.md)
  - [Phase 3: Containerization](../PHASE_3_CONTAINERIZATION_COMPLETE.md)
  - [Phase 4: CI/CD Pipeline](../PHASE_4_CICD_PIPELINE_COMPLETE.md)

## 🌟 Key Features

### **Core Messaging Capabilities**
- ✅ **Multi-Channel Delivery** - Email, SMS, Push, Slack, Teams, WhatsApp
- ✅ **Template Processing** - Dynamic content with variable substitution
- ✅ **Bulk Operations** - Efficient bulk message handling
- ✅ **Message Scheduling** - Future delivery scheduling
- ✅ **Retry Logic** - Intelligent retry with exponential backoff
- ✅ **Rate Limiting** - Configurable rate limits per channel

### **Advanced Features**
- ✅ **Engagement Tracking** - Open and click tracking
- ✅ **Priority Handling** - 4-tier priority system with SLAs
- ✅ **Status Management** - 12-state lifecycle tracking
- ✅ **Cost Tracking** - Delivery cost calculation and billing
- ✅ **Search & Analytics** - Complex search with 30+ filters
- ✅ **Health Monitoring** - System health and queue monitoring

### **Technical Features**
- ✅ **Hexagonal Architecture** - Clean separation of concerns
- ✅ **Event Streaming** - Kafka-based event publishing
- ✅ **Distributed Caching** - Redis for performance optimization
- ✅ **Database Persistence** - PostgreSQL with optimized indexing
- ✅ **API Documentation** - OpenAPI 3.0 with Swagger UI
- ✅ **Internationalization** - Multi-language support (5 languages)

## 🏗️ Architecture Overview

### **Hexagonal Architecture Layers**
```
┌─────────────────────────────────────────┐
│          API Layer (REST)               │
│  Controllers, DTOs, Mappers             │
├─────────────────────────────────────────┤
│       Application Layer                 │
│  Services, Use Cases, Processors        │
├─────────────────────────────────────────┤
│         Domain Layer                    │
│  Entities, Value Objects, Ports         │
├─────────────────────────────────────────┤
│      Infrastructure Layer               │
│  Database, Cache, Message Broker        │
└─────────────────────────────────────────┘
```

### **Technology Stack**
- **Framework**: Spring Boot 3.1.5
- **Language**: Java 17+
- **Database**: PostgreSQL 15
- **Cache**: Redis 7
- **Message Broker**: Apache Kafka
- **Container**: Docker with Alpine Linux
- **Orchestration**: Kubernetes
- **CI/CD**: GitLab CI

## 📊 API Endpoints

### **Message Management**
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/messages` | Create new message |
| GET | `/messages/{id}` | Get message by ID |
| GET | `/messages` | List messages with filters |
| PUT | `/messages/{id}` | Update message |
| DELETE | `/messages/{id}` | Delete message |
| POST | `/messages/bulk` | Bulk message creation |
| POST | `/messages/{id}/retry` | Retry failed message |
| POST | `/messages/{id}/cancel` | Cancel scheduled message |

### **Message Analytics**
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/messages/statistics` | Get messaging statistics |
| GET | `/messages/{id}/tracking` | Get message tracking info |
| GET | `/messages/search` | Advanced message search |
| GET | `/messages/health` | Queue health status |

### **Management Endpoints**
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/actuator/health` | Service health check |
| GET | `/actuator/metrics` | Service metrics |
| GET | `/actuator/prometheus` | Prometheus metrics |
| GET | `/swagger-ui.html` | API documentation UI |

## 🔧 Configuration

### **Environment Variables**
```bash
# Database Configuration
DB_HOST=postgres
DB_PORT=5432
DB_USERNAME=postgres
DB_PASSWORD=postgres

# Redis Configuration
REDIS_HOST=redis
REDIS_PORT=6379
REDIS_PASSWORD=redis123

# Kafka Configuration
KAFKA_BOOTSTRAP_SERVERS=kafka:9092

# Application Configuration
SPRING_PROFILES_ACTIVE=docker
SERVER_PORT=8501
```

### **Application Profiles**
- **default** - Development configuration
- **docker** - Docker container configuration
- **kubernetes** - Kubernetes deployment configuration
- **test** - Testing configuration with H2 database
- **prod** - Production configuration

## 🚀 Quick Start

### **Using Docker Compose**
```bash
# Start all services
./scripts/docker-run.sh up

# View logs
./scripts/docker-run.sh logs

# Stop services
./scripts/docker-run.sh down
```

### **Using Maven**
```bash
# Build the project
mvn clean package

# Run tests
mvn test

# Run application
mvn spring-boot:run
```

### **Using Docker**
```bash
# Build image
docker build -t gogidix/shared-messaging:latest .

# Run container
docker run -p 8501:8501 gogidix/shared-messaging:latest
```

## 📝 Usage Examples

### **Java Integration**
```java
import com.gogidix.infrastructure.sharedlibraries.sharedmessaging.api.dto.*;
import com.gogidix.infrastructure.sharedlibraries.sharedmessaging.domain.*;

// Create a message
CreateMessageDTO message = CreateMessageDTO.builder()
    .content("Welcome to GOGIDIX!")
    .messageType(MessageType.TRANSACTIONAL)
    .priority(MessagePriority.HIGH)
    .recipients(List.of("user@example.com"))
    .deliveryMethod(DeliveryMethod.EMAIL)
    .build();

// Send message
MessageDTO result = messagingService.sendMessage(message);
```

### **REST API Example**
```bash
# Create a message
curl -X POST http://localhost:8501/api/messaging/messages \
  -H "Content-Type: application/json" \
  -d '{
    "content": "Welcome to GOGIDIX!",
    "messageType": "TRANSACTIONAL",
    "priority": "HIGH",
    "recipients": ["user@example.com"],
    "deliveryMethod": "EMAIL"
  }'

# Get message status
curl http://localhost:8501/api/messaging/messages/{messageId}
```

## 🧪 Testing

### **Running Tests**
```bash
# Unit tests
mvn test

# Integration tests
mvn verify -Pintegration-test

# Smoke tests
./scripts/smoke-test.sh

# Health checks
./docker/health-check.sh
```

### **Test Coverage**
- Unit Tests: > 80% coverage
- Integration Tests: Database, Redis, Kafka
- Smoke Tests: API functionality validation
- Performance Tests: Load and stress testing

## 📦 Deployment

### **Kubernetes Deployment**
```bash
# Apply configurations
kubectl apply -f k8s/

# Check deployment status
kubectl get pods -n gogidix-infrastructure

# View logs
kubectl logs -f deployment/shared-messaging -n gogidix-infrastructure
```

### **CI/CD Pipeline**
The service uses GitLab CI/CD with 6 stages:
1. **Validate** - Code quality and dependencies
2. **Test** - Unit and integration tests
3. **Security** - Vulnerability scanning
4. **Build** - Package application
5. **Package** - Docker image creation
6. **Deploy** - Environment deployment

## 🔍 Monitoring

### **Health Checks**
- **Application Health**: `/actuator/health`
- **Database Health**: `/actuator/health/db`
- **Redis Health**: `/actuator/health/redis`
- **Kafka Health**: `/actuator/health/kafka`

### **Metrics**
- **Prometheus Metrics**: `/actuator/prometheus`
- **Application Metrics**: `/actuator/metrics`
- **Custom Business Metrics**: Message counts, delivery rates, error rates

### **Logging**
- Structured JSON logging
- Correlation IDs for request tracing
- Log levels: ERROR, WARN, INFO, DEBUG
- Centralized logging with ELK stack

## 🛠️ Troubleshooting

### **Common Issues**

#### **Service Won't Start**
```bash
# Check logs
docker logs shared-messaging

# Verify dependencies
./docker/health-check.sh dependencies
```

#### **Database Connection Issues**
```bash
# Check PostgreSQL status
docker-compose ps postgres

# Test connection
psql -h localhost -U postgres -d gogidix_messaging
```

#### **Message Delivery Failures**
```bash
# Check Kafka status
docker-compose ps kafka

# View message queue
kafka-console-consumer --bootstrap-server localhost:9092 --topic messages
```

## 📚 Additional Resources

### **Documentation**
- [Spring Boot Documentation](https://docs.spring.io/spring-boot/docs/3.1.5/reference/)
- [Docker Documentation](https://docs.docker.com/)
- [Kubernetes Documentation](https://kubernetes.io/docs/)
- [Apache Kafka Documentation](https://kafka.apache.org/documentation/)

### **Support**
- **GitHub Issues**: Report bugs and request features
- **Slack Channel**: #shared-messaging
- **Email**: messaging-team@gogidix.com

## 📄 License
Copyright © 2025 GOGIDIX Technologies. All rights reserved.

---
**Last Updated**: 2025-08-14  
**Maintained By**: GOGIDIX Infrastructure Team
