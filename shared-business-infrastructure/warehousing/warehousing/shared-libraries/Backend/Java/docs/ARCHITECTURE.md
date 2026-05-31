# Gogidix Shared Libraries - Architecture Documentation

## Overview

The Gogidix Shared Libraries provide a comprehensive set of reusable components for the Gogidix Social E-commerce Ecosystem. These libraries follow Hexagonal Architecture (Ports and Adapters) principles to ensure clean separation of concerns and maintainability.

## Architecture Principles

### 1. Hexagonal Architecture

The shared libraries implement Hexagonal Architecture with clear separation between:

- **Domain Layer**: Core business logic and entities
- **Application Layer**: Use cases and application services
- **Adapter Layer**: Input/Output adapters for external integration
- **Infrastructure Layer**: Technical implementations

### 2. Domain-Driven Design (DDD)

Each library follows DDD principles:

- **Value Objects**: Immutable objects representing domain concepts
- **Entities**: Objects with identity and lifecycle
- **Aggregates**: Clusters of domain objects
- **Domain Events**: Events representing significant business occurrences
- **Repositories**: Abstractions for data access

### 3. SOLID Principles

All components adhere to SOLID principles:

- **Single Responsibility**: Each class has one reason to change
- **Open/Closed**: Open for extension, closed for modification
- **Liskov Substitution**: Subtypes are substitutable for base types
- **Interface Segregation**: Clients depend only on interfaces they use
- **Dependency Inversion**: Depend on abstractions, not concretions

## Module Structure

```
Backend/Java/
├── core-libraries/
│   ├── shared-exceptions/      # Exception hierarchy
│   ├── shared-model/            # Domain models and DTOs
│   └── shared-validation/       # Validation logic
├── security-libraries/
│   ├── shared-audit/            # Audit trail and compliance
│   └── shared-security/         # Security and JWT handling
├── communication-libraries/
│   └── shared-messaging/        # Message handling and events
├── testing-libraries/
│   └── shared-testing/          # Test utilities
├── utility-libraries/
│   └── shared-utilities/        # Common utilities
└── shared-multitenancy/         # Multi-tenancy support
```

## Core Libraries

### shared-exceptions

Provides a clean exception hierarchy for the entire ecosystem:

```
BaseException (abstract)
├── AuthenticationException
├── AuthorizationException
├── BusinessException
│   ├── ResourceNotFoundException
│   └── ValidationException
├── DatabaseException
├── IntegrationException
├── ServiceUnavailableException
└── TechnicalException
```

**Key Features:**
- Rich error context (error code, HTTP status, timestamp)
- Support for adding contextual information
- Retryable and critical severity flags
- Category-based error classification

### shared-model

Contains domain models and value objects:

**Value Objects:**
- `Money`: Immutable monetary value with currency support
- `Address`: Physical address with validation and formatting
- `Email`: Email address with validation
- `PhoneNumber`: Phone number with international support

**Domain Models:**
- `User`: User entity with roles and permissions
- `Order`: Order entity with status tracking
- `Product`: Product entity with pricing

**Enums:**
- `UserRole`: User roles (ADMIN, USER, VENDOR, etc.)
- `OrderStatus`: Order lifecycle states
- `PaymentStatus`: Payment processing states
- `EntityStatus`: Generic entity states (ACTIVE, INACTIVE, DELETED)

### shared-validation

Provides validation patterns and rules:

**Validation Patterns:**
- `EmailValidationPattern`: RFC-compliant email validation
- `PhoneValidationPattern`: International phone validation (E.164)
- `CreditCardValidationPattern`: Payment card validation
- `SSNValidationPattern`: Social Security Number validation

**Features:**
- Multiple validation levels (LENIENT, BASIC, STRICT)
- Country-specific validation
- Detailed validation information with error messages

## Security Libraries

### shared-audit

Comprehensive audit trail and compliance tracking:

**Domain Models:**
- `AuditEvent`: Audit log entry with full context
- `AuditEventType`: Event categorization
- `ComplianceType`: Regulatory compliance frameworks
- `RiskLevel`: Risk assessment levels

**Features:**
- Compliance reporting (GDPR, PCI-DSS, SOX, HIPAA)
- Security event detection and escalation
- Audit trail generation
- Data retention reporting

### shared-security

Security utilities for authentication and authorization:

**Components:**
- `JwtTokenProvider`: JWT token generation and validation
- `JwtAuthenticationFilter`: Request authentication filter
- `SecurityToken`: Domain model for security tokens
- `OAuth2AuthorizationCode`: OAuth2 support

**Features:**
- JWT access and refresh tokens
- Token validation and refresh logic
- Security context management
- Role-based access control support

## Communication Libraries

### shared-messaging

Message handling and event-driven communication:

**Domain Models:**
- `Message`: Message entity with priority and status
- `MessageType`: Message categorization
- `MessagePriority`: Priority levels
- `MessageStatus`: Tracking states

**Adapters:**
- `KafkaAdapter`: Kafka message broker integration
- `MessagePublisher`: Publishing abstraction
- `MessageController`: REST endpoints for messaging

## Testing Libraries

### shared-testing

Test utilities and helpers:

**Components:**
- `TestContainerConfig`: Test container configuration
- `TestDataBuilder`: Test data factory
- `TestSpecification`: Test definition DSL
- `TestStep`: Test step definitions

## Utility Libraries

### shared-utilities

Common utilities for the ecosystem:

**Services:**
- `DateTimeUtilityService`: Date/time operations
- `JsonProcessingService`: JSON serialization/deserialization
- `CacheAdapter`: Caching abstraction
- `UtilityOperationService`: Async operation handling

**Features:**
- Date/time zone handling
- JSON processing with Jackson
- Caching with TTL support
- Async processing with callbacks

## Multi-Tenancy Support

### shared-multitenancy

Multi-tenant architecture support:

**Components:**
- Tenant identification and resolution
- Tenant-specific data isolation
- Tenant configuration management
- Cross-tenant operations

## Design Patterns Used

1. **Factory Pattern**: For creating domain objects
2. **Builder Pattern**: For complex object construction
3. **Strategy Pattern**: For validation strategies
4. **Template Method**: For common processing flows
5. **Observer Pattern**: For domain events
6. **Repository Pattern**: For data access abstraction
7. **Adapter Pattern**: For external integration

## Dependency Management

All libraries use Maven for dependency management:

```xml
<parent>
    <groupId>com.gogidix.shared</groupId>
    <artifactId>shared-libraries-parent</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</parent>
```

**Key Dependencies:**
- Spring Boot 3.1.5
- Spring Cloud 2022.0.4
- Lombok 1.18.30
- MapStruct 1.5.5.Final
- JJWT 0.11.5
- Jackson 2.15.2

## Build and Test

### Building All Modules

```bash
mvn clean install
```

### Running Tests

```bash
mvn test
```

### Code Coverage

```bash
mvn jacoco:report
```

Target coverage: 80%+

## Quality Standards

### Code Quality Tools

- **JaCoCo**: Code coverage (target: 80%+)
- **SonarQube**: Code quality analysis
- **SpotBugs**: Bug detection
- **Checkstyle**: Style checking

### Coding Standards

1. All classes have Javadoc comments
2. Methods follow `@throws`, `@param`, `@return` conventions
3. Package-private methods have explanatory comments
4. Complex logic has inline comments
5. Error messages are descriptive

## Deployment Considerations

### Versioning

- Semantic versioning (MAJOR.MINOR.PATCH)
- SNAPSHOT versions for development
- Release versions for production

### Compatibility

- Java 17 minimum
- Spring Boot 3.x
- Maven 3.8+

### Configuration

All libraries support external configuration via:

- Application properties/YAML
- Environment variables
- Configuration servers (Spring Cloud Config)

## Security Considerations

1. All secrets are externalized
2. JWT tokens use secure signing keys (256-bit minimum)
3. Sensitive data is encrypted at rest
4. Audit trails capture all security events
5. Dependencies are regularly updated for vulnerabilities

## Future Enhancements

1. Additional validation patterns
2. More security token types
3. Enhanced caching strategies
4. Additional messaging adapters (RabbitMQ, Redis)
5. GraphQL support for shared-model
6. Reactive programming support
