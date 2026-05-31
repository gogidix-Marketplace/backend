# 🔍 Shared Audit Library

> **Enterprise-grade audit logging and compliance tracking library for the Gogidix Social E-commerce Ecosystem**

[![Build Status](https://gitlab.com/gogidix/shared-audit/badges/main/pipeline.svg)](https://gitlab.com/gogidix/shared-audit/-/pipelines)
[![Coverage](https://gitlab.com/gogidix/shared-audit/badges/main/coverage.svg)](https://gitlab.com/gogidix/shared-audit/-/commits/main)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=gogidix_shared-audit&metric=security_rating)](https://sonarcloud.io/dashboard?id=gogidix_shared-audit)
[![Maintainability](https://sonarcloud.io/api/project_badges/measure?project=gogidix_shared-audit&metric=sqale_rating)](https://sonarcloud.io/dashboard?id=gogidix_shared-audit)

## 📋 **Overview**

The Shared Audit Library provides comprehensive audit logging, compliance tracking, and security monitoring capabilities across all services in the Gogidix ecosystem. Built with hexagonal architecture principles, it offers enterprise-grade features including real-time threat detection, compliance reporting (PCI DSS, GDPR, SOX), and tamper-proof audit trails.

### 🎯 **Key Features**

- **🔍 Comprehensive Audit Logging** - Track all user and system activities across 9 business domains
- **🛡️ Real-time Security Monitoring** - Suspicious pattern detection with automated escalation
- **📊 Compliance Reporting** - PCI DSS, GDPR, SOX compliance with automated report generation
- **⚡ High Performance** - Async processing with Kafka event streaming and Redis caching
- **🔒 Enterprise Security** - OAuth2 + JWT authentication with audit trail integrity
- **📈 Advanced Analytics** - ML-powered anomaly detection and executive dashboards

## 🚀 **Quick Start**

### Prerequisites

- Java 17+
- Maven 3.9+
- Docker & Docker Compose
- PostgreSQL 15+
- Redis 7+
- Apache Kafka 2.8+

### 🐳 **Docker Development Setup**

```bash
# Clone the repository
git clone https://gitlab.com/gogidix/shared-audit.git
cd shared-audit

# Start the complete infrastructure
docker-compose up -d

# Build and run the service
mvn spring-boot:run -Dspring-boot.run.profiles=development
```

The service will be available at `http://localhost:8300`

### 📦 **Maven Dependency**

Add to your `pom.xml`:

```xml
<dependency>
    <groupId>com.gogidix</groupId>
    <artifactId>shared-audit</artifactId>
    <version>1.0.0</version>
</dependency>
```

### 🔧 **Basic Usage**

```java
@Autowired
private SharedAuditService auditService;

// Record a simple audit event
AuditEventCreationRequest request = AuditEventCreationRequest.builder()
    .userId("user123")
    .sessionId("session456")
    .eventType(AuditEventType.USER_LOGIN)
    .domain(BusinessDomain.SOCIAL_COMMERCE)
    .action("LOGIN_ATTEMPT")
    .resource("authentication_service")
    .result(AuditResult.SUCCESS)
    .description("User successfully logged in")
    .ipAddress("192.168.1.100")
    .build();

CompletableFuture<AuditEvent> auditEvent = auditService.recordAuditEvent(request);
```

## Core Components

### AuditEvent
Central audit event model with comprehensive metadata:

```java
@Entity
@Table(name = "audit_events")
public class AuditEvent {
    private String eventId;
    private String eventType;
    private String action;
    private String resource;
    private String resourceId;
    private String userId;
    private String sessionId;
    private LocalDateTime timestamp;
    private String ipAddress;
    private String userAgent;
    private Object beforeState;
    private Object afterState;
    private Map<String, Object> metadata;
    private AuditStatus status;
    private String description;
}
```

### AuditService
Core service for manual audit logging:

```java
@Component
public class AuditService {
    
    public void logEvent(AuditEvent event) {
        // Log audit event
    }
    
    public void logUserAction(String action, String resource, Object data) {
        // Log user action with context
    }
    
    public void logDataChange(String resource, String resourceId, 
                             Object before, Object after) {
        // Log data changes
    }
    
    public List<AuditEvent> findEventsByUser(String userId, 
                                           LocalDateTime from, 
                                           LocalDateTime to) {
        // Query audit events
    }
}
```

### AuditableAnnotation
Method-level annotation for declarative auditing:

```java
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Auditable {
    String action();
    String resource() default "";
    String description() default "";
    boolean includeParameters() default true;
    boolean includeReturnValue() default false;
    String[] excludeFields() default {};
}
```

## Configuration Options

### Database Storage Configuration

```yaml
exalt:
  audit:
    storage:
      type: database
      datasource:
        url: jdbc:postgresql://localhost:5432/audit_db
        username: audit_user
        password: ${AUDIT_DB_PASSWORD}
        driver-class-name: org.postgresql.Driver
      jpa:
        hibernate:
          ddl-auto: validate
        properties:
          hibernate:
            dialect: org.hibernate.dialect.PostgreSQLDialect
```

### Kafka Integration

```yaml
exalt:
  audit:
    storage:
      type: kafka
      kafka:
        bootstrap-servers: localhost:9092
        topic: audit-events
        producer:
          key-serializer: org.apache.kafka.common.serialization.StringSerializer
          value-serializer: org.springframework.kafka.support.serializer.JsonSerializer
```

### RabbitMQ Integration

```yaml
exalt:
  audit:
    storage:
      type: rabbitmq
      rabbitmq:
        host: localhost
        port: 5672
        username: guest
        password: guest
        exchange: audit.exchange
        routing-key: audit.events
```

## Advanced Usage

### Custom Audit Event Types

```java
public enum AuditEventType {
    USER_LOGIN,
    USER_LOGOUT,
    USER_REGISTRATION,
    PASSWORD_CHANGE,
    PERMISSION_GRANTED,
    PERMISSION_REVOKED,
    ORDER_CREATED,
    ORDER_UPDATED,
    ORDER_CANCELLED,
    PAYMENT_PROCESSED,
    INVENTORY_ADJUSTED,
    PRODUCT_CREATED,
    PRODUCT_UPDATED,
    SYSTEM_STARTUP,
    SYSTEM_SHUTDOWN,
    CONFIGURATION_CHANGED,
    DATA_EXPORT,
    DATA_IMPORT
}
```

### Custom Audit Context

```java
@Component
public class CustomAuditContext implements AuditContextProvider {
    
    @Override
    public AuditContext getCurrentContext() {
        return AuditContext.builder()
            .userId(getCurrentUserId())
            .sessionId(getCurrentSessionId())
            .ipAddress(getCurrentIpAddress())
            .userAgent(getCurrentUserAgent())
            .organizationId(getCurrentOrganizationId())
            .tenantId(getCurrentTenantId())
            .build();
    }
}
```

### Audit Event Filtering

```java
@Configuration
public class AuditConfiguration {
    
    @Bean
    public AuditEventFilter auditEventFilter() {
        return AuditEventFilter.builder()
            .excludeActions("HEALTH_CHECK", "METRICS_REQUEST")
            .excludeUsers("system", "monitoring")
            .excludeResources("actuator/**")
            .includeOnlyFailures(false)
            .sensitiveDataMasking(true)
            .build();
    }
}
```

## Compliance Features

### GDPR Compliance
- **Data Anonymization**: Automatic PII masking in audit logs
- **Right to Erasure**: Soft delete with data retention policies
- **Data Portability**: Export audit data in standard formats

### SOX Compliance
- **Immutable Audit Trail**: Cryptographic integrity verification
- **Access Controls**: Role-based access to audit data
- **Retention Policies**: Configurable retention periods

### PCI DSS Compliance
- **Secure Storage**: Encrypted audit data storage
- **Access Monitoring**: Track access to sensitive audit information
- **Log Protection**: Tamper-evident audit logs

## Monitoring and Alerting

### Metrics
- Audit events per second
- Storage utilization
- Event processing latency
- Failed audit attempts

### Health Checks
- Database connectivity
- Message queue health
- Storage capacity
- Event processing status

### Alerts
- Audit storage failures
- Unusual activity patterns
- Compliance violations
- System intrusions

## Testing

### Unit Tests
```bash
mvn test
```

### Integration Tests
```bash
mvn verify
```

### Performance Tests
```bash
mvn test -Pperformance
```

## API Documentation

Full API documentation is available at:
- **OpenAPI/Swagger**: `/swagger-ui.html` (when included in web application)
- **JavaDoc**: Available in the `target/site/apidocs` directory after building

## Contributing

1. **Fork** the repository
2. **Create** a feature branch (`git checkout -b feature/audit-enhancement`)
3. **Commit** your changes (`git commit -am 'Add audit enhancement'`)
4. **Push** to the branch (`git push origin feature/audit-enhancement`)
5. **Create** a Pull Request

## Support

### Documentation
- [Architecture Guide](docs/architecture/audit-architecture.md)
- [Integration Guide](docs/operations/integration-guide.md)
- [Troubleshooting](docs/operations/troubleshooting.md)

### Community
- **Issues**: [GitHub Issues](https://github.com/exalt-application/social-ecommerce-ecosystem/issues)
- **Discussions**: [GitHub Discussions](https://github.com/exalt-application/social-ecommerce-ecosystem/discussions)
- **Support**: [support@exaltapplication.com](mailto:support@exaltapplication.com)

## License

This project is licensed under the **MIT License** - see the [LICENSE](../../LICENSE) file for details.

## Changelog

### v1.0.0-SNAPSHOT
- Initial release
- Core audit functionality
- Database and messaging integration
- Spring Boot auto-configuration
- Compliance features

---

**Exalt Application Limited** - Building the future of social e-commerce  
**Website**: [https://exaltapplication.com](https://exaltapplication.com)