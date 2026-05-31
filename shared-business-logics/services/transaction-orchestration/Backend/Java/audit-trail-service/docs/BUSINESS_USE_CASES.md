# Audit Trail Service - Business Use Cases

## Overview

The Audit Trail Service is a critical component of the Gogidix ecosystem, providing comprehensive audit logging for compliance, security, and operational visibility. This document outlines the key business use cases and how the service addresses them.

## Business Use Cases

### 1. Regulatory Compliance

#### Use Case Description

Financial institutions are required to maintain comprehensive audit trails for all financial transactions under regulations such as:

- **SOX (Sarbanes-Oxley Act)**: Requires internal controls and financial reporting accuracy
- **PCI DSS**: Requires tracking access to cardholder data
- **GDPR**: Requires tracking data processing activities
- **MiFID II**: Requires comprehensive transaction reporting
- **AML/KYC**: Requires tracking customer identification and transaction monitoring

#### Solution

The Audit Trail Service provides:

- **Immutable Records**: Once created, audit logs cannot be modified
- **Complete Transaction History**: Every action on every transaction is recorded
- **Actor Attribution**: All actions are attributed to specific users or services
- **Timestamp Precision**: Accurate timestamps for regulatory reporting
- **Data Retention**: Support for long-term data retention policies

#### Example Scenario

```
1. Customer initiates a $10,000 transfer
2. Audit log created: CREATE Transfer, actor: customer-001
3. Risk assessment service evaluates transfer
4. Audit log created: EXECUTE RiskAssessment, actor: risk-service
5. Compliance officer reviews flagged transaction
6. Audit log created: READ Transfer, actor: officer-123
7. Transfer approved and executed
8. Audit log created: UPDATE Transfer.status=APPROVED, actor: system
```

#### Business Value

- **Audit Readiness**: Immediate availability of complete audit records
- **Reduced Fines**: Comprehensive logging reduces regulatory violation risks
- **Faster Audits**: Structured data enables automated audit report generation

---

### 2. Security Incident Investigation

#### Use Case Description

When security incidents occur (unauthorized access, data breach, suspicious activity), security teams need to reconstruct the exact sequence of events to:

- Determine the scope of the incident
- Identify affected data and users
- Understand the attacker's actions
- Prevent future incidents

#### Solution

The Audit Trail Service supports security investigations through:

- **Comprehensive Logging**: All access to sensitive data is logged
- **Correlation ID Tracking**: Trace related events across services
- **IP Address and User Agent**: Capture source of requests
- **Session Tracking**: Link events to user sessions
- **Failures Logged**: Both successful and failed attempts are recorded

#### Example Scenario

```
1. Alert triggered: Unusual login pattern detected
2. Investigator queries audit logs for actorId: user-suspicious-001
3. Results show:
   - 50 failed login attempts (status: FAILURE)
   - 1 successful login from new IP (status: SUCCESS, ipAddress: 203.0.113.42)
   - Access to customer records: 500 records viewed (action: READ)
   - Data export initiated (action: CREATE, entityType: DataExport)
4. Investigator uses correlationId to trace all related events
5. Security team can now respond appropriately
```

#### Business Value

- **Faster Investigation**: Reduced mean time to investigate (MTTI)
- **Accurate Assessment**: Complete picture of what happened
- **Evidence Collection**: Audit logs serve as legal evidence
- **Incident Response**: Targeted response based on accurate information

---

### 3. Transaction Dispute Resolution

#### Use Case Description

Customers may dispute transactions, claiming:

- "I didn't authorize this transaction"
- "The amount is wrong"
- "I never received this service"

Resolving disputes requires reconstructing the exact sequence of events.

#### Solution

The Audit Trail Service enables dispute resolution through:

- **Entity History**: Complete history of each transaction
- **State Changes**: Before/after states captured for updates
- **Changed Fields**: Specific fields modified in each update
- **Multi-Service Tracing**: Track events across all involved services

#### Example Scenario

```
Customer disputes: "I didn't change this payment amount from $100 to $500"

Investigation:
1. Query audit logs for entity: Payment, entityId: pay-12345
2. Results show:
   - CREATE Payment, amount: $100, actor: customer-001
   - READ Payment, actor: customer-001
   - UPDATE Payment, amount: $500, actor: user-002
   - Changed fields: ["amount"]
   - Old state: {"amount":100}
   - New state: {"amount":500}
   - IP address: 203.0.113.42 (different from customer's usual IP)
3. Evidence shows user-002 (customer's spouse) made the change
4. Dispute resolved
```

#### Business Value

- **Faster Resolution**: Reduce dispute resolution time from days to minutes
- **Cost Reduction**: Fewer manual investigations
- **Customer Satisfaction**: Transparent evidence builds trust
- **Fraud Prevention**: Deters fraud when users know actions are logged

---

### 4. Operational Monitoring and Analytics

#### Use Case Description

Operations teams need to monitor system health and identify issues:

- Service performance degradation
- Error rate increases
- Unusual patterns of activity
- Capacity planning

#### Solution

The Audit Trail Service supports operations through:

- **Real-time Logging**: Events published to Kafka for real-time monitoring
- **Severity Levels**: Critical events flagged for immediate attention
- **Categorization**: Events categorized for targeted monitoring
- **Aggregation**: Support for aggregating metrics from audit logs

#### Example Scenario

```
Monitoring Dashboard Alert: "Error rate spike in Payment Service"

Investigation:
1. Query audit logs for severity: ERROR, entityType: Payment
2. Results show 500 failures in last hour
3. Group by errorMessage:
   - "Payment gateway timeout": 450 (90%)
   - "Insufficient funds": 40 (8%)
   - Other: 10 (2%)
4. Correlation IDs show all timeouts share same pattern
5. Root cause identified: Payment gateway issue
6. Operations team routes traffic to backup gateway
```

#### Business Value

- **Proactive Issue Detection**: Identify issues before customers are impacted
- **Faster Resolution**: Root cause analysis with complete event history
- **Capacity Planning**: Trend analysis informs infrastructure decisions
- **SLA Monitoring**: Verify service level agreements are met

---

### 5. Multi-Tenant Data Isolation

#### Use Case Description

In a SaaS platform, multiple customers (tenants) share infrastructure but must have complete data isolation:

- Each tenant must only see their own audit logs
- Tenant-specific retention policies
- Tenant-specific compliance requirements

#### Solution

The Audit Trail Service enforces multi-tenancy through:

- **Tenant ID Required**: All audit logs must include tenantId
- **Tenant Filtering**: All queries automatically filter by tenant
- **Tenant Indexing**: Database indexes optimize tenant-scoped queries
- **Tenant Isolation**: No cross-tenant data leakage

#### Example Scenario

```
Tenant A (Financial Institution) requires:
- 7-year data retention
- Immediate access to all logs
- Compliance with financial regulations

Tenant B (Startup) requires:
- 1-year data retention
- Cost-effective storage
- Basic audit capabilities

Both tenants use the same Audit Trail Service:
- Each query automatically scoped to tenantId
- Tenant A's retention policy runs independently
- Tenant B's logs are archived after 1 year
- Complete isolation maintained
```

#### Business Value

- **Cost Efficiency**: Shared infrastructure reduces costs
- **Regulatory Compliance**: Each tenant can meet their specific requirements
- **Scalability**: Easy to add new tenants without infrastructure changes
- **Data Security**: Strong isolation prevents data leakage

---

### 6. Distributed Transaction Tracing

#### Use Case Description

In microservices architecture, a single business transaction spans multiple services:

- Customer places order
- Inventory service checks stock
- Payment service processes payment
- Shipping service schedules delivery
- Notification service sends confirmation

When issues occur, tracing the flow across services is essential.

#### Solution

The Audit Trail Service enables distributed tracing through:

- **Correlation ID**: All related events share the same correlationId
- **Cross-Service Logging**: Each service logs its actions
- **Service Identification**: ActorType identifies when services act
- **Event Sequencing**: Timestamps show order of events

#### Example Scenario

```
Customer complains: "My order was charged but not confirmed"

Tracing the transaction:
1. Query audit logs by correlationId: corr-order-12345
2. Results show complete flow:
   - [10:00:00] OrderService: CREATE Order (status: PENDING)
   - [10:00:01] InventoryService: READ Product (status: SUCCESS)
   - [10:00:02] InventoryService: UPDATE Stock (status: SUCCESS)
   - [10:00:03] PaymentService: CREATE Payment (status: SUCCESS)
   - [10:00:04] PaymentService: UPDATE Payment.status=CAPTURED (status: SUCCESS)
   - [10:00:05] ShippingService: CREATE Shipment (status: FAILED)
   - [10:00:05] ShippingService: UPDATE Order.status=FAILED (error: "No shipping method selected")
3. Root cause: Shipping service failed due to missing information
4. Customer service can now provide accurate status and resolution
```

#### Business Value

- **Faster Problem Resolution**: Complete trace across services
- **Improved Customer Service**: Accurate status information
- **System Debugging**: Identify problematic services
- **Performance Analysis**: Find bottlenecks in transaction flow

---

### 7. Data Privacy and GDPR Compliance

#### Use Case Description

Under GDPR, individuals have the right to:

- Access their personal data
- Request deletion of their data (Right to be Forgotten)
- Know how their data is being processed

#### Solution

The Audit Trail Service supports GDPR compliance through:

- **Data Access**: Complete history of data access
- **Processing Records**: Audit trail of all data processing activities
- **Consent Tracking**: Records of consent changes
- **Deletion Audit**: Even when data is deleted, the deletion is logged

#### Example Scenario

```
Customer submits GDPR Data Access Request

1. Query audit logs by actorId: customer-001
2. Return complete history of:
   - When their data was accessed
   - Who accessed their data
   - What changes were made
   - What data was processed

Customer submits Right to be Forgotten request:

1. Process data deletion across all services
2. Create audit log: DELETE CustomerData, actor: gdpr-process
3. Keep audit log (with anonymized data) for compliance
4. Return confirmation of deletion
```

#### Business Value

- **Compliance**: Meet GDPR requirements efficiently
- **Customer Trust**: Transparency builds trust
- **Audit Readiness**: Demonstrate compliance to regulators
- **Automated Responses**: Automated response to GDPR requests

---

## Metrics and KPIs

### Key Performance Indicators

| KPI | Description | Target |
|-----|-------------|--------|
| Audit Log Creation Latency | Time to log an event | < 50ms p95 |
| Query Response Time | Time to retrieve logs | < 200ms p95 |
| Data Retention Compliance | % of logs retained per policy | 100% |
| Search Accuracy | % of relevant logs returned | > 99% |
| System Availability | Service uptime | > 99.9% |

### Business Metrics

| Metric | Description |
|--------|-------------|
| Logs per Day | Total audit logs created |
| Unique Actors | Number of distinct users/services |
| Critical Events | Number of CRITICAL severity events |
| Failed Transactions | Number of FAILURE status events |
| Cross-Tenant Access | Number of potential data isolation issues (should be 0) |

---

## Integration Examples

### 1. Payment Service Integration

```java
// Payment Service creates audit log when payment is processed
CreateAuditLogRequestDto auditRequest = CreateAuditLogRequestDto.builder()
    .tenantId(payment.getTenantId())
    .entityType("Payment")
    .entityId(payment.getId())
    .action("PROCESS")
    .actorId("payment-service")
    .actorType("SERVICE")
    .severity("INFO")
    .status("SUCCESS")
    .oldState("{\"status\":\"PENDING\"}")
    .newState("{\"status\":\"COMPLETED\"}")
    .correlationId(payment.getCorrelationId())
    .businessContext("{\"amount\":" + payment.getAmount() + "}")
    .build();

auditServiceClient.createAuditLog(auditRequest);
```

### 2. User Authentication Logging

```java
// Auth service logs all authentication attempts
CreateAuditLogRequestDto authAudit = CreateAuditLogRequestDto.builder()
    .tenantId(tenantId)
    .entityType("UserSession")
    .entityId(sessionId)
    .action(authResult.isSuccess() ? "LOGIN" : "LOGIN_FAILED")
    .actorId(userId)
    .actorType("USER")
    .ipAddress(request.getRemoteAddr())
    .userAgent(request.getHeader("User-Agent"))
    .severity(authResult.isSuccess() ? "INFO" : "WARNING")
    .status(authResult.isSuccess() ? "SUCCESS" : "FAILURE")
    .category("AUTHENTICATION")
    .description(authResult.getMessage())
    .build();

auditServiceClient.createAuditLog(authAudit);
```

---

## Data Retention Policies

### Default Retention Rules

| Severity | Retention Period |
|----------|------------------|
| CRITICAL | 7 years |
| ERROR | 2 years |
| WARNING | 1 year |
| INFO | 6 months |

### Regulatory Retention

| Regulation | Requirement | Service Support |
|------------|-------------|-----------------|
| SOX | 7 years | Yes |
| PCI DSS | 1 year | Yes |
| MiFID II | 5 years | Yes |
| AML | 5 years | Yes |

---

## Future Use Cases

### Planned Enhancements

1. **AI-Powered Anomaly Detection**: ML models analyze audit patterns
2. **Real-time Alerting**: Immediate notification of suspicious activities
3. **Blockchain Integration**: Immutable ledger for critical logs
4. **Compliance Reporting**: Automated regulatory report generation
5. **Data Lineage**: Track data flow through the entire ecosystem
