# 🏗️ Shared Messaging Library Architecture

## 🎯 Architectural Overview

The Shared Messaging Library is built using **Hexagonal Architecture** (also known as Clean Architecture) to ensure clean separation of concerns, testability, and maintainability. This architecture enables the system to be database-agnostic, framework-agnostic, and easily testable.

### **Core Principles**
- **Dependency Inversion**: Core business logic does not depend on external frameworks
- **Single Responsibility**: Each component has one clear purpose
- **Open/Closed**: Open for extension, closed for modification  
- **Interface Segregation**: Focused interfaces for specific use cases
- **Testability**: Pure domain logic with easy mocking capabilities

## 🏛️ Hexagonal Architecture Layers

### **1. API Layer (External Interface)**
```
📨 MessageController
├── REST endpoints for message operations
├── Request/Response DTOs
├── MapStruct mapping between DTOs and domain objects
└── Input validation and error handling
```

**Responsibilities:**
- HTTP request/response handling
- Input validation and sanitization
- DTO to domain object mapping
- Error response formatting

### **2. Application Layer (Orchestration)**
```
🎭 Application Services
├── SharedMessagingService - Main business orchestration
├── MessageAsyncProcessor - Background processing
└── MessageTemplateService - Template processing
```

**Responsibilities:**
- Business workflow orchestration
- Use case implementation
- Transaction management
- Async processing coordination

### **3. Domain Layer (Business Logic)**
```
🏛️ Domain Core
├── Entities
│   └── Message - Core message entity with business rules
├── Value Objects
│   ├── MessageType (10 types)
│   ├── MessagePriority (4 levels)
│   ├── MessageStatus (12 states)
│   └── DeliveryMethod (14 channels)
├── Ports (Interfaces)
│   ├── MessageRepository
│   └── MessageDeliveryPort
└── Domain Services
    ├── Message lifecycle management
    ├── Retry logic with exponential backoff
    ├── Engagement tracking
    └── Cost calculation
```

**Responsibilities:**
- Core business rules and logic
- Domain model integrity
- Business rule validation
- State transitions

### **4. Infrastructure Layer (External Systems)**
```
🔧 Infrastructure
├── Database
│   └── MessageJpaRepository - PostgreSQL persistence
├── Caching
│   └── Redis integration for performance
├── Messaging
│   └── KafkaMessagePublisher - Event streaming
└── External Services
    ├── EmailNotificationAdapter
    ├── SmsNotificationAdapter
    └── Other delivery channel adapters
```

**Responsibilities:**
- Database persistence
- External service integration
- Infrastructure configuration
- Technical implementations

## 🎭 Design Patterns Implementation

### **1. Hexagonal Architecture Pattern**
- **Ports**: Interfaces defining contracts
- **Adapters**: Implementations of external interactions
- **Domain Core**: Business logic independent of infrastructure

### **2. Repository Pattern**
```java
// Port (Interface)
public interface MessageRepository {
    Message save(Message message);
    Optional<Message> findById(Long id);
    List<Message> findByStatus(MessageStatus status);
}

// Adapter (Implementation)
@Repository
public class MessageJpaRepository implements MessageRepository {
    // JPA implementation
}
```

### **3. Factory Pattern**
```java
@Component
public class MessageDeliveryFactory {
    public MessageDeliveryAdapter create(DeliveryMethod method) {
        return switch (method) {
            case EMAIL -> emailDeliveryAdapter;
            case SMS -> smsDeliveryAdapter;
            case PUSH -> pushDeliveryAdapter;
            // ... other delivery methods
        };
    }
}
```

### **4. Strategy Pattern**
```java
public interface MessageDeliveryStrategy {
    DeliveryResult deliver(Message message);
}

// Different strategies for each delivery method
@Component
public class EmailDeliveryStrategy implements MessageDeliveryStrategy {
    public DeliveryResult deliver(Message message) {
        // Email-specific delivery logic
    }
}
```

### **5. Observer Pattern (Event Publishing)**
```java
@EventListener
public class MessageEventListener {
    @Async
    public void handleMessageSent(MessageSentEvent event) {
        // React to message sent events
        updateStatistics(event);
        triggerFollowUpActions(event);
    }
}
```

### **6. Command Query Responsibility Segregation (CQRS)**
```java
// Commands (Write operations)
public class SendMessageCommand {
    // Command data and validation
}

// Queries (Read operations)
public class MessageSearchQuery {
    // Query parameters and filtering
}
```

## 🔄 Data Flow Architecture

### **Message Creation Flow**
```
HTTP Request → Controller → DTO → Mapper → Domain Entity → 
Application Service → Domain Service → Repository → Database
                 ↓
Event Publisher → Kafka → External Systems
```

### **Message Delivery Flow**
```
Scheduled Job → MessageAsyncProcessor → Message Repository →
Domain Entity → DeliveryPort → DeliveryAdapter → External API
                                    ↓
Status Update → Repository → Event Publishing → Monitoring
```

## 🌐 Integration Architecture

### **Event-Driven Integration**
```yaml
Event Types:
├── message.created - New message created
├── message.sent - Message successfully delivered
├── message.failed - Message delivery failed
├── message.retry - Message queued for retry
├── message.cancelled - Message cancelled
└── message.engagement - User engagement tracked
```

### **External System Integration**
```yaml
Delivery Channels:
├── Email Services
│   ├── SMTP (Internal)
│   ├── SendGrid (External)
│   └── AWS SES (Cloud)
├── SMS Services
│   ├── Twilio
│   └── AWS SNS
├── Push Notifications
│   ├── Firebase
│   └── APNs
├── Chat Platforms
│   ├── Slack
│   ├── Microsoft Teams
│   └── WhatsApp Business
└── Social Media
    ├── Twitter
    └── LinkedIn
```

### **Database Schema Design**
```sql
-- Core message table
CREATE TABLE messages (
    id BIGSERIAL PRIMARY KEY,
    content TEXT NOT NULL,
    message_type VARCHAR(50) NOT NULL,
    priority VARCHAR(20) NOT NULL,
    status VARCHAR(50) NOT NULL,
    delivery_method VARCHAR(50) NOT NULL,
    scheduled_delivery_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Message recipients (many-to-many)
CREATE TABLE message_recipients (
    id BIGSERIAL PRIMARY KEY,
    message_id BIGINT REFERENCES messages(id),
    recipient VARCHAR(255) NOT NULL,
    delivery_status VARCHAR(50)
);

-- Message metadata (flexible key-value)
CREATE TABLE message_metadata (
    id BIGSERIAL PRIMARY KEY,
    message_id BIGINT REFERENCES messages(id),
    metadata_key VARCHAR(255) NOT NULL,
    metadata_value TEXT
);

-- Delivery tracking
CREATE TABLE delivery_history (
    id BIGSERIAL PRIMARY KEY,
    message_id BIGINT REFERENCES messages(id),
    attempt_number INTEGER NOT NULL,
    delivery_status VARCHAR(50) NOT NULL,
    error_message TEXT,
    delivered_at TIMESTAMP,
    cost DECIMAL(10,4),
    currency VARCHAR(3)
);
```

## 🔒 Security Architecture

### **Authentication & Authorization**
- **Spring Security Integration**: Role-based access control
- **JWT Token Support**: Stateless authentication
- **API Key Authentication**: External system integration
- **Rate Limiting**: Per-user and per-API limits

### **Data Security**
- **Encryption at Rest**: Database-level encryption
- **Encryption in Transit**: HTTPS/TLS for all communications
- **Sensitive Data Masking**: PII protection in logs
- **Secret Management**: External configuration for credentials

### **Input Validation**
- **DTO Validation**: Bean Validation (JSR 303) annotations
- **Domain Validation**: Business rule validation in domain layer
- **SQL Injection Prevention**: Parameterized queries
- **XSS Prevention**: Input sanitization

## ⚡ Performance Architecture

### **Caching Strategy**
```yaml
Cache Layers:
├── Application Cache (Redis)
│   ├── Message templates (TTL: 1 hour)
│   ├── User preferences (TTL: 30 minutes)
│   └── Delivery statistics (TTL: 5 minutes)
├── Database Query Cache
│   └── Frequently accessed message data
└── HTTP Response Cache
    └── Static content and API responses
```

### **Async Processing**
- **Background Jobs**: Message delivery processing
- **Queue Management**: Priority-based message queuing
- **Retry Logic**: Exponential backoff with jitter
- **Circuit Breakers**: Resilience4j integration
- **Bulk Operations**: Efficient batch processing

### **Database Optimization**
```sql
-- Optimized indexes for common queries
CREATE INDEX idx_messages_status_created ON messages(status, created_at);
CREATE INDEX idx_messages_scheduled_delivery ON messages(scheduled_delivery_at) 
WHERE status = 'SCHEDULED';
CREATE INDEX idx_delivery_history_message_attempt ON delivery_history(message_id, attempt_number);
```

## 📊 Monitoring & Observability Architecture

### **Application Metrics**
- **Business Metrics**: Message counts, delivery rates, error rates
- **Technical Metrics**: Response times, throughput, error rates
- **Infrastructure Metrics**: CPU, memory, disk usage
- **Custom Metrics**: Domain-specific KPIs

### **Distributed Tracing**
- **Correlation IDs**: Request tracing across services
- **Span Tracking**: Operation-level performance monitoring
- **Error Tracking**: Exception tracking and alerting
- **Performance Profiling**: Method-level performance analysis

### **Health Checks**
```yaml
Health Check Layers:
├── Application Health
│   ├── Basic application status
│   ├── Database connectivity
│   ├── Redis connectivity
│   └── Kafka connectivity
├── Business Health
│   ├── Message queue depths
│   ├── Delivery success rates
│   └── System throughput
└── Infrastructure Health
    ├── Memory usage
    ├── CPU utilization
    └── Disk space
```

## 🧪 Testing Architecture

### **Testing Strategy**
```yaml
Test Pyramid:
├── Unit Tests (80%)
│   ├── Domain logic testing
│   ├── Service layer testing
│   └── Utility function testing
├── Integration Tests (15%)
│   ├── Database integration
│   ├── External API integration
│   └── Message queue integration
└── End-to-End Tests (5%)
    ├── Full workflow testing
    ├── Performance testing
    └── Security testing
```

### **Test Infrastructure**
- **TestContainers**: Real database and message broker testing
- **WireMock**: External service mocking
- **Test Profiles**: Isolated test configurations
- **Test Data Builders**: Clean test data creation

## 🚀 Deployment Architecture

### **Containerization**
- **Multi-stage Docker builds**: Optimized image size
- **Security hardening**: Non-root user, minimal attack surface
- **Health checks**: Container health monitoring
- **Resource limits**: CPU and memory constraints

### **Kubernetes Deployment**
- **Pod management**: Horizontal Pod Autoscaling (HPA)
- **Service discovery**: Internal service communication
- **Configuration management**: ConfigMaps and Secrets
- **Rolling deployments**: Zero-downtime updates

### **CI/CD Pipeline**
```yaml
Pipeline Stages:
├── Code Quality (Validation)
├── Testing (Unit + Integration)
├── Security Scanning
├── Build & Package
├── Container Creation
└── Deployment (Dev → Staging → Prod)
```

## 📈 Scalability Architecture

### **Horizontal Scaling**
- **Stateless Design**: No server-side session state
- **Load Balancing**: Kubernetes service load balancing
- **Database Scaling**: Read replicas and connection pooling
- **Cache Scaling**: Redis cluster configuration

### **Vertical Scaling**
- **Resource Optimization**: JVM tuning and memory management
- **Connection Pooling**: Database connection optimization
- **Async Processing**: Non-blocking I/O operations
- **Batch Processing**: Efficient bulk operations

---

## 🎯 Architecture Decision Records (ADRs)

### **ADR-001: Hexagonal Architecture**
**Decision**: Adopt Hexagonal Architecture pattern  
**Rationale**: Ensures testability, maintainability, and technology independence  
**Consequences**: Increased initial complexity, better long-term maintainability

### **ADR-002: PostgreSQL as Primary Database**
**Decision**: Use PostgreSQL for message persistence  
**Rationale**: ACID compliance, JSON support, performance, and reliability  
**Consequences**: Strong consistency, potential scaling challenges

### **ADR-003: Redis for Caching**
**Decision**: Use Redis for distributed caching  
**Rationale**: High performance, data structure variety, persistence options  
**Consequences**: Additional infrastructure dependency, improved performance

### **ADR-004: Apache Kafka for Event Streaming**
**Decision**: Use Kafka for event publishing and messaging  
**Rationale**: High throughput, durability, scalability  
**Consequences**: Operational complexity, excellent event streaming capabilities

---

**Last Updated**: 2025-08-14  
**Document Version**: 1.0  
**Review Schedule**: Quarterly
