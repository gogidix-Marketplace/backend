# Gogidix Shared Libraries - API Documentation

## Overview

This document provides comprehensive API documentation for all Gogidix Shared Libraries. Each library exposes well-defined interfaces for integration.

## Table of Contents

- [Exceptions API](#exceptions-api)
- [Model API](#model-api)
- [Validation API](#validation-api)
- [Audit API](#audit-api)
- [Security API](#security-api)
- [Messaging API](#messaging-api)
- [Utilities API](#utilities-api)

---

## Exceptions API

### BaseException

The root exception class providing common functionality for all Gogidix exceptions.

```java
public abstract class BaseException extends RuntimeException {
    private final String errorCode;
    private final int httpStatus;
    private final LocalDateTime timestamp;
    private final Map<String, Object> context;
    private final String category;

    // Methods
    public void addContext(String key, Object value);
    public boolean isCritical();
    public boolean isRetryable();
}
```

#### Usage Example

```java
throw new ResourceNotFoundException("User", "123");
// Adds context automatically: {resourceType: "User", resourceId: "123"}

try {
    // code
} catch (BaseException e) {
    if (e.isCritical()) {
        alertOpsTeam(e);
    }
    if (e.isRetryable()) {
        retryOperation();
    }
}
```

### Exception Hierarchy

```
BaseException
├── AuthenticationException (401)
├── AuthorizationException (403)
├── BusinessException (400)
│   ├── ResourceNotFoundException (404)
│   └── ValidationException (400)
├── DatabaseException (500)
├── IntegrationException (500)
├── ServiceUnavailableException (503)
└── TechnicalException (500)
```

---

## Model API

### Money Value Object

Immutable monetary value with precise decimal arithmetic.

```java
public final class Money {
    public static final Money ZERO = new Money(BigDecimal.ZERO, "USD");

    // Constructors
    public Money(BigDecimal amount, String currencyCode);
    public Money(double amount, String currencyCode);
    public Money(long amountInCents, String currencyCode);
    public Money(String amount, String currencyCode);

    // Arithmetic Operations
    public Money add(Money other);
    public Money subtract(Money other);
    public Money multiply(double factor);
    public Money divide(double factor);
    public Money abs();
    public Money negate();

    // Comparison
    public boolean isPositive();
    public boolean isNegative();
    public boolean isZero();
    public boolean isGreaterThan(Money other);
    public boolean isLessThan(Money other);

    // Conversion
    public Money convertTo(String targetCurrency, BigDecimal exchangeRate);
    public double percentageOf(Money total);
    public Money applyPercentage(double percentage);

    // Formatting
    public String format();              // "$100.50"
    public String formatWithCode();      // "100.50 USD"
}
```

#### Usage Examples

```java
// Creating Money instances
Money price = Money.of(99.99, "USD");
Money tax = Money.ofMinor(899, "USD");  // $8.99

// Operations
Money total = price.add(tax);
Money discount = total.applyPercentage(10); // 10% discount
Money finalPrice = total.subtract(discount);

// Comparison
if (finalPrice.isGreaterThan(Money.of(100, "USD"))) {
    // Apply free shipping
}

// Currency conversion
Money inEur = price.convertTo("EUR", new BigDecimal("0.85"));
```

### Address Value Object

Physical address with validation and formatting.

```java
public final class Address {
    // Builder pattern
    public static AddressBuilder builder();
    public static AddressBuilder usAddress();
    public static AddressBuilder internationalAddress(String country);

    // Validation
    public boolean isComplete();
    public boolean isPostalCodeValid();
    public boolean isDeliverable();
    public boolean isInternationalAddress();

    // Formatting
    public String getFullAddress();
    public String getCityStatePostal();
    public Address normalizeForShipping();

    // Comparison
    public boolean isEquivalentTo(Address other);
    public Double getDistanceTo(Address other); // kilometers
}
```

#### Usage Examples

```java
// Creating addresses
Address usAddress = Address.usAddress()
    .street1("123 Main St")
    .city("New York")
    .state("NY")
    .postalCode("10001")
    .type(AddressType.RESIDENTIAL)
    .build();

Address intlAddress = Address.internationalAddress("UK")
    .street1("Baker Street")
    .city("London")
    .postalCode("NW1 6XE")
    .build();

// Validation
if (address.isComplete() && address.isPostalCodeValid()) {
    // Address is deliverable
}

// Formatting for shipping labels
Address formatted = address.normalizeForShipping();
```

---

## Validation API

### Email Validation

```java
public class EmailValidationPattern {
    // Basic validation
    public static boolean isValid(String email);
    public static boolean isValid(String email, EmailValidationLevel level);

    // Detailed validation info
    public static EmailValidationInfo getValidationInfo(String email);

    // Validation levels: LENIENT, BASIC, STRICT
}
```

#### Usage Examples

```java
// Basic validation
if (EmailValidationPattern.isValid("user@example.com")) {
    // Valid email
}

// Strict validation
if (EmailValidationPattern.isValid(email, EmailValidationLevel.STRICT)) {
    // RFC-compliant email
}

// Get detailed info
EmailValidationInfo info = EmailValidationPattern.getValidationInfo(email);
if (!info.isValid()) {
    System.out.println("Error: " + info.getMessage());
}
```

### Phone Validation

```java
public class PhoneValidationPattern {
    // Basic validation
    public static boolean isValid(String phoneNumber);
    public static boolean isValid(String phone, PhoneValidationLevel level);

    // Country-specific validation
    public static boolean isValidForCountry(String phone, String countryCode);

    // Formatting
    public static String formatInternational(String phone);
    public static String formatForDisplay(String phone, String countryCode);

    // Validation info
    public static PhoneValidationInfo getValidationInfo(String phone);
}
```

#### Usage Examples

```java
// Validate international number
if (PhoneValidationPattern.isValid("+14155552671", PhoneValidationLevel.INTERNATIONAL)) {
    // Valid E.164 format
}

// Country-specific validation
if (PhoneValidationPattern.isValidForCountry("+14155551234", "US")) {
    // Valid US number
}

// Format for display
String display = PhoneValidationPattern.formatForDisplay("4155552671", "US");
// Returns: "(415) 555-2671"
```

---

## Audit API

### AuditEvent Domain Model

```java
@Entity
public class AuditEvent {
    private Long id;
    private String eventId;
    private String userId;
    private String sessionId;
    private LocalDateTime timestamp;
    private AuditEventType eventType;
    private BusinessDomain domain;
    private String action;
    private String resource;
    private String resourceId;
    private AuditResult result;
    private String description;
    private String ipAddress;
    private String userAgent;
    private String correlationId;
    private ComplianceType complianceType;
    private String riskScore;

    // Business logic methods
    public boolean isCompliantEvent();
    public boolean requiresSecurityEscalation();
    public String calculateSeverity();
    public boolean isSuspiciousPattern();
    public ComplianceReport generateComplianceReport();
    public AuditTrailEntry createTrailEntry();
    public boolean isFinancialEvent();
}
```

#### AuditEvent Types

- `LOGIN_ATTEMPT`: User login events
- `LOGOUT`: User logout events
- `DATA_ACCESS`: Data read operations
- `DATA_MODIFICATION`: Data write operations
- `FINANCIAL_TRANSACTION`: Payment/transaction events
- `SECURITY_EVENT`: Security-related events
- `ACCESS_DENIED`: Failed authorization
- `API_CALL`: REST API invocations

#### Business Domains

- `IDENTITY`: User identity management
- `PAYMENTS`: Payment processing
- `ORDERS`: Order management
- `PRODUCTS`: Product catalog
- `COMMISSIONS`: Commission calculations
- `SECURITY`: Security operations
- `BILLING`: Billing operations

#### Usage Example

```java
AuditEvent event = AuditEvent.builder()
    .eventId(UUID.randomUUID().toString())
    .userId(userId)
    .sessionId(sessionId)
    .timestamp(LocalDateTime.now())
    .eventType(AuditEventType.DATA_ACCESS)
    .domain(BusinessDomain.IDENTITY)
    .action("READ")
    .resource("/api/users/" + userId)
    .result(AuditResult.SUCCESS)
    .description("User profile accessed")
    .ipAddress(request.getRemoteAddr())
    .userAgent(request.getHeader("User-Agent"))
    .complianceType(ComplianceType.GDPR)
    .riskScore("LOW")
    .build();

// Check if escalation needed
if (event.requiresSecurityEscalation()) {
    securityTeam.alert(event);
}

// Generate compliance report
ComplianceReport report = event.generateComplianceReport();
```

---

## Security API

### JwtTokenProvider

JWT token generation and validation.

```java
@Component
public class JwtTokenProvider {
    // Token generation
    public String generateToken(Authentication authentication);
    public String generateRefreshToken(Authentication authentication);

    // Token parsing
    public String getUserIdFromToken(String token);
    public String getUsernameFromToken(String token);
    public List<String> getAuthoritiesFromToken(String token);
    public Date getExpirationDateFromToken(String token);

    // Token validation
    public boolean validateToken(String token);
    public boolean isTokenExpired(String token);
    public boolean isRefreshToken(String token);
    public boolean canTokenBeRefreshed(String token);

    // Token refresh
    public String refreshToken(String token);
}
```

#### Usage Example

```java
@Autowired
private JwtTokenProvider tokenProvider;

// Generate token
String token = tokenProvider.generateToken(authentication);

// Validate token
if (tokenProvider.validateToken(token)) {
    String userId = tokenProvider.getUserIdFromToken(token);
    // Process request
}

// Refresh token
if (tokenProvider.canTokenBeRefreshed(token)) {
    String newToken = tokenProvider.refreshToken(token);
}
```

### JwtAuthenticationFilter

Filter for JWT authentication in Spring Security.

```java
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                   HttpServletResponse response,
                                   FilterChain filterChain);
}
```

---

## Messaging API

### Message Domain Model

```java
public class Message {
    private String id;
    private String payload;
    private MessageType type;
    private MessagePriority priority;
    private MessageStatus status;
    private LocalDateTime timestamp;
    private String correlationId;
    private Map<String, Object> headers;
}
```

#### Message Types

- `DOMAIN_EVENT`: Domain events
- `COMMAND`: Command messages
- `QUERY`: Query messages
- `EVENT`: Event notifications
- `REQUEST`: Request messages
- `RESPONSE`: Response messages

#### Message Priorities

- `CRITICAL`: Highest priority
- `HIGH`: High priority
- `NORMAL`: Normal priority (default)
- `LOW`: Low priority

### MessagePublisher

Message publishing abstraction.

```java
public interface MessagePublisher {
    void publish(Message message);
    void publish(String topic, Message message);
    CompletableFuture<Void> publishAsync(Message message);
}
```

#### Usage Example

```java
@Autowired
private MessagePublisher messagePublisher;

Message event = Message.builder()
    .type(MessageType.DOMAIN_EVENT)
    .priority(MessagePriority.HIGH)
    .payload(eventPayload)
    .correlationId(correlationId)
    .build();

messagePublisher.publish("user-events", event);
```

---

## Utilities API

### DateTimeUtilityService

Date and time operations.

```java
public class DateTimeUtilityService {
    public LocalDateTime convertToTimeZone(LocalDateTime dateTime, String zoneId);
    public String format(LocalDateTime dateTime, String format);
    public LocalDateTime parse(String dateTimeString, String format);
    public long getDaysBetween(LocalDateTime start, LocalDateTime end);
    public boolean isBusinessDay(LocalDateTime date);
}
```

### JsonProcessingService

JSON serialization and deserialization.

```java
public class JsonProcessingService {
    public String toJson(Object object);
    public <T> T fromJson(String json, Class<T> clazz);
    public <T> T fromJson(String json, TypeReference<T> typeRef);
    public String toPrettyJson(Object object);
}
```

---

## Error Response Format

All API errors follow a consistent format:

```json
{
    "timestamp": "2024-01-15T10:30:00Z",
    "status": 404,
    "error": "RESOURCE_NOT_FOUND",
    "message": "User not found with id: 123",
    "path": "/api/users/123",
    "context": {
        "resourceType": "User",
        "resourceId": "123"
    }
}
```

---

## Pagination

List endpoints support pagination:

```json
{
    "data": [...],
    "pagination": {
        "page": 1,
        "size": 20,
        "totalElements": 100,
        "totalPages": 5,
        "hasNext": true,
        "hasPrevious": false
    }
}
```

---

## Rate Limiting

API endpoints may be rate limited:

```http
X-RateLimit-Limit: 1000
X-RateLimit-Remaining: 999
X-RateLimit-Reset: 1642252800
```

When rate limited, the API returns:

```http
HTTP/1.1 429 Too Many Requests
X-RateLimit-Limit: 1000
X-RateLimit-Remaining: 0
X-RateLimit-Reset: 1642252800
Retry-After: 60
```

---

## Versioning

API versioning is done via URL path:

```
/api/v1/resource
/api/v2/resource
```

---

## CORS Configuration

Cross-Origin Resource Sharing is configurable:

```yaml
gogidix:
  cors:
    allowed-origins: "https://app.gogidix.com,https://admin.gogidix.com"
    allowed-methods: "GET,POST,PUT,DELETE,OPTIONS"
    allowed-headers: "*"
    allow-credentials: true
    max-age: 3600
```
