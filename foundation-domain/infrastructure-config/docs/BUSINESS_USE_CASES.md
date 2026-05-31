# Infrastructure Config Service - Business Use Cases

## Executive Summary

The Infrastructure Config Service provides centralized configuration management for the Gogidix ecosystem, enabling teams to manage application configurations, feature flags, and secrets securely and efficiently across all environments.

## Target Users

| Role | Needs | Primary Features |
|------|-------|------------------|
| **Platform Engineers** | Manage service configurations across environments | Environment-specific configs, versioning, rollback |
| **Product Managers** | Control feature rollouts without deployments | Feature flags, percentage rollouts, A/B testing |
| **Developers** | Access configuration values in applications | Typed values, caching, SDK integration |
| **DevOps Engineers** | Manage secrets and credentials | Encrypted storage, rotation, access control |
| **Security Team** | Audit and compliance | Change history, access logging, encryption |

---

## Use Case 1: Environment-Specific Configuration

### Problem
Development teams need different configuration values for different environments (dev, staging, prod). Managing these in code or multiple files creates complexity and risk of accidental prod changes.

### Solution
The service provides environment-scoped configuration properties.

### Workflow
1. Platform engineer creates configuration for dev environment:
```json
POST /api/v1/configurations
{
  "tenantId": "gogidix",
  "environment": "DEV",
  "key": "database.timeout",
  "value": "5000",
  "valueType": "INTEGER"
}
```

2. Same configuration created for prod with different value:
```json
POST /api/v1/configurations
{
  "tenantId": "gogidix",
  "environment": "PROD",
  "key": "database.timeout",
  "value": "30000",
  "valueType": "INTEGER"
}
```

3. Applications request configuration by specifying their environment:
```java
int timeout = configService.getValue("gogidix", "database.timeout",
    Environment.PROD, Integer.class);
```

### Business Value
- Reduces configuration errors between environments
- Enables safe configuration updates without code changes
- Provides audit trail of all configuration changes

---

## Use Case 2: Progressive Feature Rollout

### Problem
Product teams need to release new features gradually to monitor performance and gather user feedback, rather than big-bang releases.

### Solution
Feature flags with percentage-based rollout strategy.

### Workflow
1. Product manager creates feature flag at 10% rollout:
```json
POST /api/v1/feature-flags
{
  "flagKey": "new-checkout-flow",
  "isEnabled": true,
  "rolloutStrategy": "PERCENTAGE",
  "rolloutPercentage": 10
}
```

2. Monitor metrics and gradually increase:
```json
PUT /api/v1/feature-flags/{id}
{
  "rolloutPercentage": 25
}
```

3. Eventually enable for all users:
```json
PUT /api/v1/feature-flags/{id}
{
  "rolloutPercentage": 100
}
```

4. After successful rollout, clean up old code paths

### Business Value
- Reduces risk of new feature releases
- Enables quick rollback if issues detected
- Provides data-driven feature adoption

---

## Use Case 3: Premium Feature Gates

### Problem
Business needs to restrict certain features to premium-tier customers.

### Solution
Feature flags with conditional rollout based on user attributes.

### Workflow
1. Create feature flag with conditions:
```json
POST /api/v1/feature-flags
{
  "flagKey": "advanced-analytics",
  "isEnabled": true,
  "rolloutStrategy": "CONDITIONAL",
  "conditions": [
    {
      "type": "ATTRIBUTE",
      "attribute": "tier",
      "operator": "IN",
      "values": ["premium", "enterprise"]
    }
  ]
}
```

2. Application evaluates flag with user context:
```json
POST /api/v1/feature-flags/evaluate
{
  "flagKey": "advanced-analytics",
  "userId": "user-123",
  "context": {
    "tier": "premium",
    "role": "user"
  }
}
```

Response:
```json
{
  "enabled": true,
  "reason": "FLAG_ENABLED"
}
```

### Business Value
- Enables tiered pricing models
- Easy to modify feature access rules
- Self-service feature management

---

## Use Case 4: A/B Testing

### Problem
Product team wants to test two different implementations to measure which performs better.

### Solution
Feature flags with AB_TEST rollout strategy.

### Workflow
1. Create A/B test flag:
```json
POST /api/v1/feature-flags
{
  "flagKey": "checkout-button-color",
  "isEnabled": true,
  "rolloutStrategy": "AB_TEST",
  "rolloutPercentage": 50,
  "isSticky": true
}
```

2. Application evaluates and shows variant:
```java
FeatureFlagEvaluation eval = flagService.evaluate(
    tenantId, "checkout-button-color", userId, context);

if (eval.isEnabled()) {
    // Show variant A (new blue button)
} else {
    // Show variant B (current green button)
}
```

3. Track metrics by variant in analytics platform

### Business Value
- Data-driven product decisions
- Reduced risk of UI/UX changes
- Statistical validation of improvements

---

## Use Case 5: Secret Management

### Problem
Applications need access to sensitive credentials (API keys, database passwords) without hardcoding them or storing in plain text.

### Solution
Encrypted secret storage with access control.

### Workflow
1. DevOps engineer creates secret:
```json
POST /api/v1/secrets
{
  "secretKey": "stripe-api-key",
  "name": "Stripe API Key",
  "secretValue": "sk_live_xxxxx",
  "secretType": "API_KEY",
  "rotationIntervalDays": 90,
  "accessControlList": ["payment-service", "billing-service"]
}
```

2. Application retrieves secret at runtime:
```java
String apiKey = secretService.getSecretValue(
    "gogidix", "stripe-api-key", "payment-service");
```

3. Automatic rotation before expiration:
```json
POST /api/v1/secrets/{id}/rotate
{
  "newSecretValue": "sk_live_yyyyy",
  "changeReason": "Quarterly rotation"
}
```

### Business Value
- Eliminates hardcoded credentials
- Automated secret rotation
- Audit trail of all secret access

---

## Use Case 6: Configuration Rollback

### Problem
A configuration change caused issues in production and needs to be reverted quickly.

### Solution
Version history with one-click rollback.

### Workflow
1. View version history:
```http
GET /api/v1/configurations/{configId}/versions
```

2. Identify the stable version (e.g., version 5)

3. Rollback to that version:
```http
POST /api/v1/configurations/{configId}/rollback/5
```

4. System creates new version with restored values

### Business Value
- Reduces MTTR (Mean Time To Recovery)
- Enables safe experimentation
- Maintains audit trail even after rollback

---

## Use Case 7: Gradual Migration

### Problem
Migrating from an old service to a new service requires gradually shifting traffic.

### Solution
Feature flags with gradual rollout strategy.

### Workflow
1. Create gradual rollout flag:
```json
POST /api/v1/feature-flags
{
  "flagKey": "use-new-inventory-service",
  "isEnabled": true,
  "rolloutStrategy": "GRADUAL",
  "rolloutPercentage": 100,
  "isSticky": true
}
```

2. Application checks flag and routes traffic accordingly:
```java
boolean useNew = flagService.evaluate(...).isEnabled();
if (useNew) {
    inventoryService = newInventoryService;
} else {
    inventoryService = legacyInventoryService;
}
```

3. Gradually increase percentage over time
4. Complete migration and remove old code

### Business Value
- Enables zero-downtime migrations
- Easy rollback if issues occur
- Controlled traffic shifting

---

## Use Case 8: Kill Switch

### Problem
A critical bug is discovered in a feature that needs to be disabled immediately across all environments.

### Solution
Global feature flag toggle.

### Workflow
1. Product manager toggles off the feature:
```http
PUT /api/v1/feature-flags/{tenantId}/{flagKey}/toggle?enabled=false
```

2. All applications see the change within cache TTL (1 minute max)

3. Fix the issue
4. Toggle back on when ready:
```http
PUT /api/v1/feature-flags/{tenantId}/{flagKey}/toggle?enabled=true
```

### Business Value
- Instant mitigation of critical issues
- No deployment required
- Reduces production incident impact

---

## Use Case 9: Compliance Auditing

### Problem
Security auditors need to see who changed what configuration and when.

### Solution
Complete audit trail via ConfigVersion records.

### Workflow
1. Query changes by time range:
```http
GET /api/v1/configurations/versions?changedAfter=2025-01-01&changedBefore=2025-02-01
```

2. Query changes by user:
```http
GET /api/v1/configurations/versions?changedBy=user-123
```

3. Export audit report for compliance

### Business Value
- Simplifies compliance audits
- Accountability for changes
- Security incident investigation support

---

## Use Case 10: Multi-Region Configuration

### Problem
Same application deployed in multiple regions needs region-specific configuration.

### Solution
Tag-based configuration organization.

### Workflow
1. Create region-specific configurations with tags:
```json
POST /api/v1/configurations
{
  "key": "cdn.endpoint",
  "value": "cdn.us-east.example.com",
  "tags": ["us-east", "production"]
}
```

2. Query configurations by tags:
```http
GET /api/v1/configurations?tags=us-east,production
```

3. Application loads appropriate configuration based on its region

### Business Value
- Simplifies multi-region deployments
- Consistent configuration structure
- Region-specific optimization

---

## Integration Examples

### Spring Boot Application

```java
@Configuration
public class AppConfig {

    @Bean
    public ConfigClient configClient(ConfigService configService) {
        return configService.forTenant("gogidix")
            .withEnvironment(Environment.PROD);
    }

    @Bean
    public SomeService someService(ConfigClient config) {
        int timeout = config.getInt("service.timeout", 5000);
        return new SomeService(timeout);
    }
}
```

### Feature Flag Check

```java
@Service
public class CheckoutService {

    @Autowired
    private FeatureFlagClient flagClient;

    public void processCheckout(Order order) {
        if (flagClient.isEnabled("new-checkout-flow", order.getUserId())) {
            processNewFlow(order);
        } else {
            processLegacyFlow(order);
        }
    }
}
```

### Secret Access

```java
@Component
public class PaymentProcessor {

    @Value("${stripe.api.key.secret-id}")
    private String stripeKeyId;

    public ChargeResult charge(ChargeRequest request) {
        String apiKey = secretClient.getSecret(stripeKeyId);
        return stripeClient.charge(apiKey, request);
    }
}
```

---

## Metrics and KPIs

### Operational Metrics
- Configuration requests per second
- Cache hit/miss ratio
- Average API latency
- Feature flag evaluation rate
- Secret access rate

### Business Metrics
- Number of active feature flags
- Configuration change frequency
- Secret rotation compliance
- Rollback frequency
- Time from flag creation to 100% rollout

---

## Pricing Tiers

| Feature | Free | Pro | Enterprise |
|---------|------|-----|------------|
| Configurations | 1,000 | 50,000 | Unlimited |
| Feature Flags | 100 | 5,000 | Unlimited |
| Secrets | 100 | 1,000 | Unlimited |
| Version History | 30 days | 90 days | 1 year |
| SSO | - | ✅ | ✅ |
| RBAC | Basic | Advanced | Advanced |
| SLA | 99% | 99.9% | 99.99% |
