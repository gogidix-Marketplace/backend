# 🔐 **JWT/OAuth2 INTEGRATION GUIDE FOR AGENT B**

## 📋 **OVERVIEW**

This shared-security library serves as the **definitive JWT and OAuth2 integration template** for all Agent B management services. It provides comprehensive security domain models, JWT validation, OAuth2 flows, and RBAC implementation following hexagonal architecture principles.

## 🎯 **SECURITY ARCHITECTURE STRUCTURE**

```
shared-security/
├── domain/                 # CORE SECURITY LOGIC (NO external dependencies)
│   ├── model/             # Security domain entities with business logic
│   │   ├── SecurityToken.java           # Rich JWT/OAuth2 token model (30+ methods)
│   │   ├── JwtSecurityContext.java      # JWT-specific security context (25+ methods)
│   │   ├── OAuth2AuthorizationCode.java # OAuth2 authorization code flow (20+ methods)
│   │   ├── SecurityLevel.java           # Security level enum with business rules
│   │   ├── TokenType.java               # Token type classifications
│   │   └── TokenStatus.java             # Token status lifecycle
│   ├── port/              # Security interfaces (contracts)
│   │   ├── in/           # Input ports (security use cases)
│   │   │   ├── TokenValidationUseCase.java
│   │   │   ├── OAuth2AuthorizationUseCase.java
│   │   │   └── JwtSecurityUseCase.java
│   │   └── out/          # Output ports (security dependencies) 
│   │       ├── TokenRepository.java
│   │       ├── SecurityEventPublisher.java
│   │       └── AuthorizationCodeRepository.java
│   └── service/          # Security domain services (business rules)
│       ├── JwtValidationService.java
│       ├── OAuth2SecurityService.java
│       └── RbacSecurityService.java
└── adapter/              # INFRASTRUCTURE ADAPTERS
    ├── in/               # Inbound adapters
    │   ├── web/         # REST security controllers
    │   └── messaging/   # Security event listeners
    └── out/             # Outbound adapters
        ├── persistence/ # Security database adapters
        ├── messaging/   # Security event publishers
        └── external/    # External auth service clients
```

## 🔥 **RICH DOMAIN MODEL EXAMPLES**

### ✅ **SecurityToken - JWT/OAuth2 Token Management**

```java
// ✅ EXCELLENT - Rich Security Token with 30+ Business Logic Methods
SecurityToken jwtToken = SecurityToken.createJwtToken(
    "user123", "john.doe", 
    Set.of("ADMIN", "USER"), 
    Set.of("READ_USERS", "WRITE_USERS"),
    Duration.ofHours(8)
);

// Business rule validation
if (jwtToken.isValid() && jwtToken.hasRole("ADMIN")) {
    if (jwtToken.requiresMfaForOperation("DELETE_ACCOUNT")) {
        // Require additional MFA verification
    }
}

// Security score calculation
int securityScore = jwtToken.getSecurityScore(); // 0-100 score
boolean needsAudit = jwtToken.requiresAuditLogging();

// Token lifecycle management
SecurityToken refreshedToken = jwtToken.refresh(Duration.ofHours(8));
SecurityToken revokedToken = jwtToken.revoke("Security violation");
```

### 🎯 **JwtSecurityContext - JWT-Specific Security Context**

```java
// ✅ EXCELLENT - JWT Security Context with 25+ Business Logic Methods
JwtSecurityContext jwtContext = JwtSecurityContext.builder()
    .subject("user123")
    .issuer("gogidx-auth-service")
    .addRole("MANAGER")
    .addPermission("MANAGE_TEAM")
    .addScope("read:profile")
    .expiresInHours(8)
    .securityLevel(SecurityLevel.HIGH)
    .multiFactorAuthenticated(true)
    .build();

// JWT validation and business rules
if (jwtContext.isValid() && !jwtContext.isExpired()) {
    if (jwtContext.supportsOperation("TEAM_OPERATION", 
                                   Set.of("MANAGER"), 
                                   Set.of("MANAGE_TEAM"))) {
        // Execute operation
    }
}

// Security features
Map<String, Object> claims = jwtContext.getAllClaims();
Set<String> authorities = jwtContext.getCombinedAuthorities();
boolean requiresAudit = jwtContext.requiresAuditLogging();
```

### 🔐 **OAuth2AuthorizationCode - OAuth2 Authorization Code Flow**

```java
// ✅ EXCELLENT - OAuth2 Authorization Code with 20+ Business Logic Methods
OAuth2AuthorizationCode authCode = OAuth2AuthorizationCode.createPkceCode(
    "client123", "user456", "https://app.gogidx.com/callback",
    Set.of("read:profile", "write:data"),
    "codeChallenge", "S256",
    Duration.ofMinutes(10)
);

// OAuth2 validation and security
if (authCode.isValidForExchange("client123", 
                               "https://app.gogidx.com/callback", 
                               "codeVerifier", 
                               "stateToken")) {
    // Exchange code for token
    OAuth2AuthorizationCode usedCode = authCode.markAsUsed();
}

// PKCE validation
boolean pkceValid = authCode.validatePkceCodeVerifier("originalCodeVerifier");
int securityScore = authCode.getSecurityScore(); // Enhanced by PKCE
```

## 🔌 **SECURITY PORT DEFINITIONS**

### 🎯 **Input Ports (Security Use Cases)**
```java
public interface TokenValidationUseCase {
    CompletableFuture<ValidationResult> validateJwtToken(String token);
    CompletableFuture<SecurityContext> extractSecurityContext(String token);
    CompletableFuture<Boolean> hasPermission(String token, String permission);
}

public interface OAuth2AuthorizationUseCase {
    CompletableFuture<AuthorizationCode> generateAuthorizationCode(AuthorizeRequest request);
    CompletableFuture<AccessToken> exchangeCodeForToken(TokenExchangeRequest request);
    CompletableFuture<Boolean> validateAuthorizationCode(String code, String clientId);
}

public interface JwtSecurityUseCase {
    CompletableFuture<String> generateJwtToken(SecurityContext context);
    CompletableFuture<Boolean> isTokenExpired(String token);
    CompletableFuture<SecurityContext> refreshSecurityContext(String refreshToken);
}
```

### 🔌 **Output Ports (Security Dependencies)**
```java
public interface TokenRepository {
    SecurityToken save(SecurityToken token);
    Optional<SecurityToken> findByTokenId(String tokenId);
    List<SecurityToken> findActiveTokensByUserId(String userId);
    void revokeTokensByUserId(String userId);
    void cleanExpiredTokens();
}

public interface AuthorizationCodeRepository {
    OAuth2AuthorizationCode save(OAuth2AuthorizationCode code);
    Optional<OAuth2AuthorizationCode> findByCode(String code);
    void markAsUsed(String code);
    void deleteExpiredCodes();
}

public interface SecurityEventPublisher {
    void publishTokenValidated(TokenValidatedEvent event);
    void publishTokenRevoked(TokenRevokedEvent event);
    void publishSecurityViolation(SecurityViolationEvent event);
}
```

## 🚀 **AGENT B INTEGRATION EXAMPLES**

### 📊 **Example 1: Business Analytics Service JWT Integration**
```java
@Service
public class AnalyticsApplicationService implements AnalyticsUseCase {
    
    private final TokenValidationUseCase tokenValidator;
    private final SecurityEventPublisher securityPublisher;
    
    @Override
    public CompletableFuture<AnalyticsReport> generateReport(GenerateReportCommand cmd) {
        return tokenValidator.validateJwtToken(cmd.getAuthToken())
            .thenCompose(validationResult -> {
                if (!validationResult.isValid()) {
                    throw new SecurityException("Invalid token");
                }
                
                SecurityToken token = validationResult.getToken();
                
                // Check permissions using rich domain logic
                if (!token.hasPermission("READ_ANALYTICS") || 
                    !token.hasAnyRole(Set.of("ANALYST", "MANAGER"))) {
                    throw new AccessDeniedException("Insufficient permissions");
                }
                
                // Check if sensitive operation requires MFA
                if (cmd.isHighSensitivity() && 
                    token.requiresMfaForOperation("ACCESS_SENSITIVE_DATA")) {
                    throw new MfaRequiredException("MFA required for sensitive analytics");
                }
                
                // Log security event if required
                if (token.requiresAuditLogging()) {
                    securityPublisher.publishTokenValidated(
                        new TokenValidatedEvent(token.getTokenId(), "ANALYTICS_ACCESS")
                    );
                }
                
                // Business logic execution
                AnalyticsReport report = generateAnalyticsReport(cmd.getParameters());
                return CompletableFuture.completedFuture(report);
            });
    }
}
```

### 🏢 **Example 2: HR Management Service OAuth2 Integration**
```java
@Service
public class HRApplicationService implements HRUseCase {
    
    private final OAuth2AuthorizationUseCase oauth2Service;
    private final TokenValidationUseCase tokenValidator;
    
    @Override
    public CompletableFuture<Employee> createEmployee(CreateEmployeeCommand cmd) {
        return tokenValidator.extractSecurityContext(cmd.getOAuth2Token())
            .thenCompose(securityContext -> {
                JwtSecurityContext jwtContext = (JwtSecurityContext) securityContext;
                
                // OAuth2 scope validation
                if (!jwtContext.hasScope("write:employees")) {
                    throw new InsufficientScopeException("Missing write:employees scope");
                }
                
                // Business role validation
                if (!jwtContext.hasAnyRole(Set.of("HR_ADMIN", "MANAGER"))) {
                    throw new AccessDeniedException("Insufficient role privileges");
                }
                
                // Security level check for sensitive operations
                if (jwtContext.getSecurityLevel().getLevel() < SecurityLevel.HIGH.getLevel()) {
                    throw new SecurityException("High security level required for employee creation");
                }
                
                // Domain business logic
                Employee employee = Employee.create(cmd.getEmployeeData());
                
                return CompletableFuture.completedFuture(employee);
            });
    }
}
```

### 💰 **Example 3: Financial Service High Security Integration**
```java
@Service
public class FinancialApplicationService implements FinancialUseCase {
    
    private final TokenValidationUseCase tokenValidator;
    private final SecurityEventPublisher securityPublisher;
    
    @Override
    public CompletableFuture<TransferResult> transferFunds(TransferFundsCommand cmd) {
        return tokenValidator.validateJwtToken(cmd.getAuthToken())
            .thenCompose(validationResult -> {
                SecurityToken token = validationResult.getToken();
                
                // High security validation for financial operations
                if (!token.isHighSecurity()) {
                    throw new SecurityException("High security token required for financial operations");
                }
                
                // MFA requirement for financial transactions
                if (token.requiresMfaForOperation("TRANSFER_FUNDS")) {
                    throw new MfaRequiredException("MFA required for fund transfers");
                }
                
                // Amount-based security checks
                if (cmd.getAmount().compareTo(BigDecimal.valueOf(10000)) > 0 && 
                    !token.hasRole("FINANCIAL_ADMIN")) {
                    throw new AccessDeniedException("Admin role required for high-value transfers");
                }
                
                // Device and location validation
                if (!token.isValidForDevice(cmd.getDeviceId(), cmd.getIpAddress())) {
                    securityPublisher.publishSecurityViolation(
                        new SecurityViolationEvent(token.getTokenId(), "DEVICE_MISMATCH", 
                                                  "Financial operation from unrecognized device")
                    );
                    throw new SecurityException("Device validation failed");
                }
                
                // Enhanced audit logging for financial operations
                securityPublisher.publishTokenValidated(
                    new TokenValidatedEvent(token.getTokenId(), "FINANCIAL_TRANSFER", 
                                          Map.of("amount", cmd.getAmount(), 
                                                "security_score", token.getSecurityScore()))
                );
                
                // Execute financial business logic
                TransferResult result = executeTransfer(cmd);
                return CompletableFuture.completedFuture(result);
            });
    }
}
```

## 🔗 **AGENT A FOUNDATION INTEGRATION**

### 🔐 **Foundation Security Services Integration**
```java
// Integration with Agent A Auth Service
@Component
public class AuthServiceIntegrationAdapter implements SecurityEventPublisher {
    
    private final AuthServiceClient authServiceClient; // Agent A auth-service:8502
    private final AuditServiceClient auditClient;      // Agent A audit-log-management:8516
    
    @Override
    public void publishTokenValidated(TokenValidatedEvent event) {
        // Send to Agent A audit logging
        auditClient.recordSecurityEvent(AuditEvent.builder()
            .eventType(AuditEventType.SECURITY_ACCESS)
            .domain(BusinessDomain.SECURITY)
            .action("TOKEN_VALIDATED")
            .result(AuditResult.SUCCESS)
            .userId(event.getUserId())
            .resourceId(event.getTokenId())
            .additionalData("operation", event.getOperation())
            .build());
    }
    
    @Override
    public void publishSecurityViolation(SecurityViolationEvent event) {
        // High-priority security alert to Agent A monitoring
        auditClient.recordSecurityEvent(AuditEvent.builder()
            .eventType(AuditEventType.SECURITY_VIOLATION)
            .domain(BusinessDomain.SECURITY)
            .action("SECURITY_VIOLATION")
            .result(AuditResult.FAILURE)
            .securityLevel(SecurityLevel.CRITICAL)
            .reason(event.getViolationType())
            .build());
    }
}
```

## 📊 **SECURITY BEST PRACTICES**

### ✅ **JWT Security Implementation Checklist**
- [ ] JWT tokens have reasonable expiration times (≤8 hours for access tokens)
- [ ] Refresh tokens have longer expiration (≤30 days) and are securely stored
- [ ] MFA enforcement for high-security operations
- [ ] Device fingerprinting for high-security contexts
- [ ] IP address restrictions for sensitive operations
- [ ] Comprehensive audit logging for security events
- [ ] Token revocation capabilities
- [ ] Security score calculation and monitoring

### 🔒 **OAuth2 Security Implementation Checklist**
- [ ] PKCE (Proof Key for Code Exchange) enabled for public clients
- [ ] State parameter validation for CSRF protection
- [ ] Authorization codes expire quickly (≤10 minutes)
- [ ] Redirect URI strict validation
- [ ] Scope limitation and validation
- [ ] Client authentication for confidential clients
- [ ] Comprehensive security event logging

### 🛡️ **RBAC (Role-Based Access Control) Checklist**
- [ ] Fine-grained permissions beyond role-based access
- [ ] Role hierarchy and inheritance support
- [ ] Dynamic permission evaluation
- [ ] Context-aware access control (device, location, time)
- [ ] Principle of least privilege enforcement
- [ ] Regular access reviews and auditing

## 🎯 **SECURITY METRICS & MONITORING**

### 📈 **Security Score Calculation**
```java
// Automatic security score calculation
int securityScore = securityToken.getSecurityScore();

// Score components:
// - Security Level (15 points per level)
// - Multi-Factor Authentication (+20 points)
// - Device Restrictions (+10 points each)
// - IP Restrictions (+10 points)
// - Short Expiration (+5-15 points)
// - OAuth2 Scopes (+5 points)
// Maximum: 100 points

if (securityScore >= 90) {
    // Maximum security - allow all operations
} else if (securityScore >= 75) {
    // High security - restrict sensitive operations
} else if (securityScore >= 50) {
    // Standard security - basic restrictions
} else {
    // Low security - require additional verification
}
```

### 🔔 **Security Event Monitoring**
- **Token Validation Events**: Track all successful/failed validations
- **Security Violations**: Monitor device mismatches, location anomalies
- **MFA Requirements**: Track MFA enforcement and compliance
- **High-Value Operations**: Enhanced monitoring for financial/sensitive operations
- **Token Lifecycle**: Monitor token creation, refresh, and revocation

## 📚 **INTEGRATION WITH SHARED LIBRARIES**

### 🔧 **Available Security Features**
- `shared-audit` - Automatic security event auditing
- `shared-validation` - Security policy validation
- `shared-exceptions` - Standardized security exceptions
- `shared-utilities` - Security utility functions

### 📦 **Maven Dependency Example**
```xml
<dependency>
    <groupId>com.gogidx.shared</groupId>
    <artifactId>shared-security</artifactId>
    <version>1.0.0</version>
</dependency>
```

## 🚨 **SECURITY ERROR HANDLING**

### 🛡️ **Standardized Security Exceptions**
```java
// JWT-specific exceptions
throw new JwtValidationException("Invalid JWT signature");
throw new JwtExpiredException("JWT token has expired");
throw new InsufficientPermissionsException("Missing required permission: " + permission);

// OAuth2-specific exceptions
throw new InvalidAuthorizationCodeException("Authorization code is invalid or expired");
throw new InvalidClientException("Client authentication failed");
throw new InsufficientScopeException("Missing required OAuth2 scope: " + scope);

// Security policy exceptions
throw new MfaRequiredException("Multi-factor authentication required for this operation");
throw new SecurityLevelInsufficientException("Higher security level required");
throw new DeviceValidationException("Operation not allowed from unrecognized device");
```

## 🎯 **SUCCESS CRITERIA**

### ✅ **Security Implementation Quality Metrics**
- Minimum 20+ business logic methods per security domain model
- Zero infrastructure dependencies in domain layer
- 95%+ test coverage on security business logic
- All security rules encoded in domain layer
- Comprehensive JWT and OAuth2 support

### 📈 **Security Integration Success Metrics** 
- Successful JWT validation across all Agent B services
- OAuth2 authorization code flow operational
- RBAC permissions working correctly
- MFA enforcement operational where required
- Security audit trails integrated with Agent A audit service

## 📞 **SECURITY SUPPORT & ESCALATION**

**Agent A Foundation Lead**: Available for security architecture guidance  
**Security Issues**: Escalate immediately via CLAUDE.md protocols  
**Integration Problems**: Use shared security templates and examples above  

---

**🔐 Template Version**: 1.0.0  
**🎯 For**: Agent B JWT/OAuth2 Integration  
**📅 Created**: Sprint 4 (Weeks 7-8)  
**✅ Status**: Production Ready Security Framework