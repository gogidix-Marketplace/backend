# 🏗️ **HEXAGONAL ARCHITECTURE TEMPLATE GUIDE FOR AGENT B**

## 📋 **OVERVIEW**

This shared-audit library serves as the **definitive hexagonal architecture template** for all Agent B management services. It demonstrates the complete implementation of hexagonal architecture principles with rich domain models, clean ports, and proper layer separation.

## 🎯 **ARCHITECTURE STRUCTURE**

```
shared-audit/
├── domain/                 # CORE BUSINESS LOGIC (NO external dependencies)
│   ├── model/             # Domain entities with business logic
│   │   ├── AuditEvent.java           # Rich domain model
│   │   ├── AuditSearchCriteria.java  # Search value objects
│   │   ├── ComplianceReport.java     # Business entities  
│   │   └── AuditStatistics.java      # Analytics objects
│   ├── port/              # Domain interfaces (contracts)
│   │   ├── in/           # Input ports (use cases)
│   │   │   └── AuditUseCase.java
│   │   └── out/          # Output ports (dependencies) 
│   │       ├── AuditEventRepository.java
│   │       ├── AuditEventPublisher.java
│   │       └── ComplianceReporter.java
│   ├── service/          # Domain services (business rules)
│   │   └── AuditDomainService.java
│   └── exception/        # Domain-specific exceptions
│       └── AuditException.java
├── application/           # APPLICATION ORCHESTRATION
│   ├── port/
│   │   ├── in/           # Application input ports  
│   │   └── out/          # Application output ports
│   └── service/          # Application services
│       └── AuditApplicationService.java
└── adapter/              # INFRASTRUCTURE ADAPTERS
    ├── in/               # Inbound adapters
    │   ├── web/         # REST controllers
    │   └── messaging/   # Event listeners
    └── out/             # Outbound adapters
        ├── persistence/ # Database adapters
        ├── messaging/   # Event publishers
        └── external/    # External service clients
```

## 🔥 **DOMAIN LAYER PRINCIPLES**

### ✅ **DO's - Domain Layer**
- ✅ Rich domain models with business logic methods
- ✅ Pure domain logic - no infrastructure imports
- ✅ Immutable value objects where appropriate  
- ✅ Domain events for cross-domain communication
- ✅ Business rule validation in domain entities
- ✅ Factory methods for complex object creation
- ✅ Domain-specific exceptions

### ❌ **DON'Ts - Domain Layer**
- ❌ NO Spring, JPA, Jackson annotations in domain models
- ❌ NO database, web, or external service imports
- ❌ NO infrastructure dependencies
- ❌ NO framework-specific code
- ❌ NO getters/setters without business purpose

## 📊 **RICH DOMAIN MODEL EXAMPLE**

```java
// ✅ EXCELLENT - Rich Domain Model with Business Logic
public class AuditEvent {
    private String eventId;
    private String userId;
    private AuditEventType eventType;
    private LocalDateTime timestamp;
    private ComplianceType complianceType;
    
    // ✅ BUSINESS LOGIC METHODS
    public boolean isSecurityRelated() {
        return eventType.isSecurityEvent();
    }
    
    public boolean requiresCompliance() {
        return complianceType != ComplianceType.NONE;
    }
    
    public boolean isHighRisk() {
        return eventType.getRiskLevel() == RiskLevel.HIGH;
    }
    
    public boolean hasExpired(Duration retentionPeriod) {
        return timestamp.plus(retentionPeriod).isBefore(LocalDateTime.now());
    }
    
    public AuditEvent withEnhancedSecurity(SecurityContext context) {
        // Business logic for security enhancement
        return this.toBuilder()
            .securityLevel(SecurityLevel.ENHANCED)
            .securityContext(context)
            .build();
    }
}
```

## 🔌 **PORT DEFINITIONS**

### 🎯 **Input Ports (Use Cases)**
```java
public interface AuditUseCase {
    CompletableFuture<AuditResult> recordEvent(RecordEventCommand command);
    CompletableFuture<List<AuditEvent>> searchEvents(SearchCommand command);
    CompletableFuture<ComplianceReport> generateReport(ReportCommand command);
}
```

### 🔌 **Output Ports (Dependencies)**
```java
public interface AuditEventRepository {
    AuditEvent save(AuditEvent event);
    List<AuditEvent> findBySearchCriteria(AuditSearchCriteria criteria);
    boolean isAuditTrailComplete(LocalDateTime start, LocalDateTime end);
}
```

## 🚀 **IMPLEMENTATION CHECKLIST FOR AGENT B**

### ✅ **Sprint Integration Checklist**
- [ ] Copy hexagonal structure to your service
- [ ] Create rich domain models with business logic (minimum 15 methods)
- [ ] Define clean input/output ports
- [ ] Implement application services for orchestration  
- [ ] Create adapters for infrastructure concerns
- [ ] Add comprehensive validation in domain layer
- [ ] Include domain events for cross-service communication
- [ ] Add business rule engines where applicable
- [ ] Implement factory patterns for complex objects
- [ ] Create domain-specific exceptions

### 📋 **Quality Gates**
- ✅ Domain layer has ZERO infrastructure dependencies
- ✅ Business logic concentrated in domain models
- ✅ All external access through ports
- ✅ Rich domain models (15+ business methods minimum)
- ✅ Proper separation of concerns across layers
- ✅ Comprehensive unit tests for business logic
- ✅ Integration tests for ports and adapters

## 🎯 **AGENT B INTEGRATION EXAMPLES**

### 📊 **Example 1: Business Analytics Service**
```java
// Domain Model
public class AnalyticsReport {
    public boolean isRealtimeData() { /* business logic */ }
    public double getAccuracyScore() { /* calculation */ }
    public boolean requiresRefresh() { /* business rules */ }
}

// Input Port  
public interface AnalyticsUseCase {
    CompletableFuture<AnalyticsReport> generateReport(ReportCommand cmd);
}

// Output Port
public interface DataRepository {
    AnalyticsData findByDateRange(DateRange range);
}
```

### 🏢 **Example 2: HR Management Service**
```java
// Domain Model
public class Employee {
    public boolean isEligibleForPromotion() { /* business logic */ }
    public SalaryBand calculateSalaryBand() { /* calculation */ }
    public boolean requiresPerformanceReview() { /* business rules */ }
}
```

## 📚 **SHARED LIBRARIES INTEGRATION**

### 🔧 **Available Shared Libraries for Agent B**
- `shared-audit` - Audit trail and compliance tracking
- `shared-security` - JWT, OAuth2, RBAC integration  
- `shared-messaging` - Kafka events and messaging
- `shared-model` - Common domain entities
- `shared-utilities` - Common business functions
- `shared-exceptions` - Standardized error handling
- `shared-validation` - Business rule validation
- `shared-testing` - Test utilities and fixtures

### 📦 **Maven Dependency Example**
```xml
<dependency>
    <groupId>com.gogidix.shared</groupId>
    <artifactId>shared-audit</artifactId>
    <version>1.0.0</version>
</dependency>
```

## 🔗 **CROSS-SERVICE INTEGRATION**

### 🔐 **Agent A Foundation Integration**
- **Authentication**: Integrate with `auth-service:8502` for JWT validation
- **Audit Logging**: Use `audit-log-management-service:8516` for compliance
- **Configuration**: Connect to `config-server:8888` for dynamic config
- **Service Discovery**: Register with `service-registry:8761` 
- **Monitoring**: Send metrics to `monitoring-service:8507`

### 📊 **Example Integration Code**
```java
@Service
public class HRApplicationService implements HRUseCase {
    
    // Agent A Integration via Ports
    private final AuthenticationPort authPort;        // -> auth-service  
    private final AuditPort auditPort;               // -> audit-log-management
    private final ConfigurationPort configPort;      // -> config-server
    
    @Override
    public CompletableFuture<Employee> createEmployee(CreateEmployeeCommand cmd) {
        // Authenticate via Agent A auth-service
        authPort.validateToken(cmd.getToken());
        
        // Business logic in domain
        Employee employee = Employee.create(cmd.getEmployeeData());
        
        // Audit via Agent A audit-log-management  
        auditPort.recordEvent(AuditEvent.employeeCreated(employee));
        
        return CompletableFuture.completedFuture(employee);
    }
}
```

## 🎯 **SUCCESS CRITERIA**

### ✅ **Domain Quality Metrics**
- Minimum 15 business logic methods per rich domain model
- Zero infrastructure dependencies in domain layer
- 90%+ test coverage on business logic
- All business rules encoded in domain layer
- Clean separation between layers

### 📈 **Integration Success Metrics** 
- Successful integration with all 5 Agent A foundation services
- Proper audit trail integration for compliance
- JWT authentication working across services
- Configuration management operational
- Service discovery and health checks active

## 📞 **SUPPORT & COORDINATION**

**Agent A Foundation Lead**: Available for architecture guidance and integration support
**Documentation Updates**: All changes tracked in CLAUDE.md
**Integration Issues**: Escalate via CLAUDE.md coordination protocols

---

**🏗️ Template Version**: 1.0.0  
**🎯 For**: Agent B Management Domain Services  
**📅 Created**: Sprint 4 (Weeks 7-8)  
**✅ Status**: Production Ready Template