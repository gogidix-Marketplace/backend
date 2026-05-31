# Infrastructure Lock Service - Business Use Cases

## Overview

The Infrastructure Lock Service provides distributed locking capabilities essential for maintaining data consistency and preventing race conditions in microservices architectures. This document outlines the key business use cases and real-world scenarios where the service adds value.

## Core Use Cases

### 1. Order Processing

**Problem**: Multiple order processing services might try to process the same order simultaneously, leading to duplicate shipments or incorrect inventory updates.

**Solution**: Use exclusive locks on order resources.

**Example Flow**:
```
1. Service A attempts to acquire lock on "order:12345"
2. Lock acquired successfully
3. Service A processes order (validate inventory, charge payment, schedule shipment)
4. Service A releases lock
5. Service B can now access the order (but it's already processed)
```

**Request**:
```json
{
  "tenantId": "ecommerce-tenant",
  "resourceKey": "order:12345",
  "holderId": "order-processing-service-1",
  "lockType": "EXCLUSIVE",
  "ttlSeconds": 600
}
```

**Benefits**:
- Prevents duplicate order processing
- Ensures inventory consistency
- Maintains order state integrity

### 2. Payment Processing

**Problem**: Multiple payment attempts on the same order could result in double-charging customers.

**Solution**: Exclusive locks on payment resources.

**Example Flow**:
```
1. Payment service acquires lock on "payment:order-12345"
2. Process payment through payment gateway
3. Update order status
4. Release lock
```

**Benefits**:
- Prevents duplicate charges
- Maintains payment ledger consistency
- Enables accurate reconciliation

### 3. Inventory Management

**Problem**: Concurrent inventory updates can lead to overselling products.

**Solution**: Use exclusive locks on SKU inventory during updates.

**Example Flow**:
```
1. Warehouse service acquires lock on "inventory:SKU-12345"
2. Read current inventory
3. Calculate new quantity
4. Update inventory
5. Release lock
```

**Benefits**:
- Prevents overselling
- Maintains accurate stock levels
- Enables real-time inventory reporting

### 4. User Profile Updates

**Problem**: Concurrent profile updates can result in data loss or inconsistent user information.

**Solution**: Use exclusive locks during profile updates.

**Request**:
```json
{
  "tenantId": "saas-tenant",
  "resourceKey": "user:789:profile",
  "holderId": "user-service-instance-1",
  "lockType": "EXCLUSIVE",
  "ttlSeconds": 30
}
```

**Benefits**:
- Prevents data loss from concurrent updates
- Maintains profile data consistency
- Enables audit trail accuracy

### 5. Document Collaboration

**Problem**: Multiple users editing a document simultaneously need synchronization.

**Solution**: Use exclusive write locks and shared read locks.

**Read Operations** (concurrent):
```json
{
  "resourceKey": "document:doc-123",
  "lockType": "SHARED",
  "ttlSeconds": 300
}
```

**Write Operations** (exclusive):
```json
{
  "resourceKey": "document:doc-123",
  "lockType": "WRITE",
  "ttlSeconds": 60
}
```

**Benefits**:
- Allows concurrent reads
- Prevents conflicting writes
- Maintains document version integrity

### 6. Rate Limiting & Quota Management

**Problem**: Prevent abuse of API resources while allowing fair access.

**Solution**: Use locks to track and enforce usage quotas.

**Example Flow**:
```
1. API gateway attempts lock on "quota:user-456:api-v1"
2. If locked: check current usage against quota
3. If within quota: allow request
4. If over quota: deny request
5. Update usage count
6. Release lock
```

**Benefits**:
- Fair resource allocation
- Prevents API abuse
- Enables tiered service plans

### 7. Scheduled Job Execution

**Problem**: Distributed schedulers might execute the same job multiple times.

**Solution**: Use locks to ensure single execution.

**Request**:
```json
{
  "resourceKey": "job:daily-report-generation",
  "holderId": "scheduler-instance-1",
  "lockType": "EXCLUSIVE",
  "ttlSeconds": 3600,
  "waitForLock": false
}
```

**Benefits**:
- Prevents duplicate job execution
- Enables distributed scheduling
- Supports job failover

### 8. Cache Invalidation

**Problem**: Multiple services might try to rebuild cached content simultaneously.

**Solution**: Use locks to coordinate cache rebuilds.

**Example Flow**:
```
1. Cache miss occurs
2. Service acquires lock on "cache:product-catalog:rebuild"
3. Only lock holder rebuilds cache
4. Other services wait and use stale data
5. Lock released when rebuild completes
```

**Benefits**:
- Prevents cache stampede
- Reduces database load
- Improves response times

### 9. Multi-Step Transaction Orchestration

**Problem**: Distributed transactions need coordination across multiple services.

**Solution**: Use locks to manage transaction state.

**Request**:
```json
{
  "tenantId": "banking-tenant",
  "resourceKey": "transaction:txn-789",
  "holderId": "transaction-orchestrator",
  "lockType": "EXCLUSIVE",
  "ttlSeconds": 300,
  "metadata": "{\"steps\": [\"reserve\", \"transfer\", \"confirm\"]}"
}
```

**Benefits**:
- Ensures transactional consistency
- Enables rollback mechanisms
- Supports saga pattern

### 10. Resource Pool Management

**Problem**: Manage limited resources (connections, licenses, etc.) across services.

**Solution**: Use locks to track resource allocation.

**Example Flow**:
```
1. Service requests license from pool
2. Acquire lock on "license:pool:premium"
3. Allocate available license
4. Record allocation
5. Release lock
```

**Benefits**:
- Prevents over-allocation
- Enables fair distribution
- Supports capacity planning

## Industry-Specific Use Cases

### E-Commerce

| Use Case | Resource Key | Lock Type |
|----------|--------------|-----------|
| Order processing | `order:{orderId}` | EXCLUSIVE |
| Cart updates | `cart:{userId}` | EXCLUSIVE |
| Inventory adjustment | `inventory:{skuId}` | EXCLUSIVE |
| Price updates | `price:{skuId}` | WRITE |
| Product catalog reads | `catalog:*` | READ |

### Financial Services

| Use Case | Resource Key | Lock Type |
|----------|--------------|-----------|
| Account balance updates | `account:{accountId}:balance` | EXCLUSIVE |
| Transaction processing | `transaction:{txnId}` | EXCLUSIVE |
| Fund transfers | `transfer:{transferId}` | EXCLUSIVE |
| Compliance reporting | `report:{reportId}` | EXCLUSIVE |
| Market data reads | `market:{symbol}` | READ |

### Healthcare

| Use Case | Resource Key | Lock Type |
|----------|--------------|-----------|
| Patient record updates | `patient:{patientId}:record` | EXCLUSIVE |
| Appointment scheduling | `appointment:{slotId}` | EXCLUSIVE |
| Prescription processing | `prescription:{rxId}` | EXCLUSIVE |
| Medical device commands | `device:{deviceId}` | EXCLUSIVE |
| Lab result access | `lab:{resultId}` | SHARED |

### SaaS Platforms

| Use Case | Resource Key | Lock Type |
|----------|--------------|-----------|
| Tenant configuration | `tenant:{tenantId}:config` | WRITE |
| User provisioning | `user:{userId}:provision` | EXCLUSIVE |
| Subscription changes | `subscription:{subId}` | EXCLUSIVE |
| Billing runs | `billing:{period}` | EXCLUSIVE |
| Analytics reads | `analytics:{tenantId}` | READ |

## Lock Type Selection Guide

### When to Use EXCLUSIVE

- Critical state changes
- Financial transactions
- Inventory modifications
- User data updates
- Job execution

### When to Use SHARED

- Report generation
- Batch processing reads
- Data export operations
- Analytics queries

### When to Use WRITE

- Database write operations
- Cache updates
- Message publishing
- State synchronization

### When to Use READ

- Reporting queries
- Data synchronization
- Cache warming
- Analytics processing

## TTL Selection Guidelines

| Operation Type | Recommended TTL | Rationale |
|----------------|-----------------|-----------|
| Quick updates | 30-60 seconds | Fast operations, quick recovery if crashed |
| Standard operations | 300 seconds (5 min) | Balanced timeout |
| Long-running jobs | 3600 seconds (1 hour) | Batch jobs, report generation |
| Critical operations | 60 seconds + auto-extend | Safety with renewal capability |

## Error Handling Best Practices

### 1. Always Check Acquisition Result

```java
LockAcquisitionResult result = lockService.acquireLock(request);
if (!result.isAcquired()) {
    if (result.isTimedOut()) {
        // Handle timeout - maybe queue for later
    } else {
        // Handle immediate failure - resource locked
    }
}
```

### 2. Always Release in Finally Block

```java
try {
    result = lockService.acquireLock(request);
    if (result.isAcquired()) {
        // Perform work
    }
} finally {
    if (result != null && result.isAcquired()) {
        lockService.releaseLock(...);
    }
}
```

### 3. Handle Lock Extension

```java
while (doingLongWork()) {
    if (lock.getRemainingTtlSeconds() < 60) {
        boolean extended = lockService.extendLock(...);
        if (!extended) {
            // Handle failure - maybe abort work
            break;
        }
    }
    // Continue work
}
```

## Monitoring Recommendations

### Key Metrics to Track

1. **Lock Acquisition Rate**: Throughput indicator
2. **Average Acquisition Time**: Performance indicator
3. **Failed Acquisition Rate**: Contention indicator
4. **Average Hold Time**: Usage pattern indicator
5. **Active Locks per Tenant**: Capacity planning

### Alert Thresholds

| Metric | Warning | Critical |
|--------|---------|----------|
| Failed acquisition rate | > 5% | > 15% |
| Average acquisition time | > 100ms | > 500ms |
| Active locks (per tenant) | > 1000 | > 5000 |
| Expired locks (per hour) | > 50 | > 200 |

## Integration Patterns

### Pattern 1: Try-Lock-Or-Fail

Quick attempt without waiting:

```java
LockRequest request = LockRequest.essential(tenant, resource, holder)
    .waitForLock(false)
    .build();
```

### Pattern 2: Try-Lock-With-Timeout

Wait up to a limit:

```java
LockRequest request = LockRequest.essential(tenant, resource, holder)
    .waitForLock(true)
    .waitTimeSeconds(30)
    .build();
```

### Pattern 3: Lock-With-Renewal

For long operations:

```java
// Initial lock with short TTL
LockRequest request = LockRequest.essential(tenant, resource, holder)
    .ttlSeconds(60)
    .build();

// Periodically extend while working
while (moreWorkToDo()) {
    lockService.extendLock(..., 60);
    // Do work
}
```

## Best Practices Summary

1. **Always use tenant-specific resource keys** for multi-tenancy
2. **Set appropriate TTLs** based on operation duration
3. **Always release locks** in finally blocks
4. **Handle acquisition failures** gracefully
5. **Monitor lock metrics** for performance insights
6. **Use shared locks** for read-heavy workloads
7. **Document lock usage** in code comments
8. **Test lock scenarios** including failures
9. **Plan for deadlocks** with timeout strategies
10. **Use descriptive holder IDs** for debugging
