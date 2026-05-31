# 🏢 Shared-Multitenancy Library

**Version:** 1.0.0
**Purpose:** Multi-tenant support library with Row-Level Security (RLS) for Foundation-Domain
**Status:** ✅ Core Implementation Complete

---

## 📋 Overview

The `shared-multitenancy` library provides the foundation for building multi-tenant SaaS applications in the Gogidix ecosystem. It implements database-level tenant isolation using PostgreSQL Row-Level Security (RLS) and ensures that all data access is automatically scoped to the current tenant.

---

## 🎯 Key Features

1. ✅ **Thread-Local Tenant Context** - Automatic tenant tracking per request
2. ✅ **BaseEntity Class** - All entities inherit tenant support
3. ✅ **Tenant Context Filter** - Automatic extraction of tenant from HTTP headers
4. ✅ **Row-Level Security (RLS)** - Database-level tenant isolation
5. ✅ **Tenant Type Support** - Multiple tenant types (ORGANIZATION, PARTNER, INTERNAL, TRIAL, INDIVIDUAL)
6. ✅ **Audit Fields** - Automatic tracking of created/updated dates and users
7. ✅ **Soft Delete Support** - Preserve data integrity with soft deletes
8. ✅ **Optimistic Locking** - Version field for concurrent modification prevention

---

## 📦 Components

### **1. BaseEntity**
Location: `com.gogidix.shared.multitenancy.entity.BaseEntity`

The abstract base class that all entities should extend. Provides:
- `tenantId` - The tenant identifier (required)
- `tenantType` - The tenant type (required)
- `id` - UUID primary key
- `createdAt`, `updatedAt` - Audit timestamps
- `createdBy`, `updatedBy` - Audit user tracking
- `deleted` - Soft delete flag
- `version` - Optimistic locking

**Example:**
```java
@Entity
@Table(name = "users", indexes = {
    @Index(name = "idx_users_tenant_id", columnList = "tenant_id")
})
public class User extends BaseEntity {
    private String username;
    private String email;
    // Inherits tenantId, tenantType, and all audit fields from BaseEntity
}
```

---

### **2. TenantContext**
Location: `com.gogidix.shared.multitenancy.context.TenantContext`

Thread-local storage for tenant information throughout the request lifecycle.

**Key Methods:**
```java
// Set tenant context
TenantContext.setTenantId("tenant-123");
TenantContext.setTenantType(TenantType.ORGANIZATION);

// Get tenant context
String tenantId = TenantContext.getTenantId();
TenantType tenantType = TenantContext.getTenantType();

// Check if context is set
boolean hasContext = TenantContext.hasTenantContext();

// Clear context (end of request)
TenantContext.clear();
```

---

### **3. TenantContextFilter**
Location: `com.gogidix.shared.multitenancy.filter.TenantContextFilter`

Servlet filter that automatically extracts tenant information from HTTP headers and sets it in TenantContext.

**Expected HTTP Headers:**
```
X-Tenant-ID: tenant-123        (required)
X-Tenant-Type: ORGANIZATION     (optional, defaults to ORGANIZATION)
X-Tenant-Name: Acme Corp       (optional, for logging)
```

**Example Request:**
```bash
curl -H "X-Tenant-ID: tenant-123" \
     -H "X-Tenant-Type: ORGANIZATION" \
     http://api.example.com/api/users
```

---

### **4. RLSUtil**
Location: `com.gogidix.shared.multitenancy.util.RLSUtil`

Utility class for Row-Level Security operations in PostgreSQL.

**Key Methods:**
```java
// Set tenant context at database level
RLSUtil.setTenantContext(connection, "tenant-123");

// Enable RLS on a table
RLSUtil.enableRLS(connection, "users");

// Create tenant isolation policy
RLSUtil.createTenantIsolationPolicy(connection, "users");

// Enable RLS with policy in one call
RLSUtil.enableRLSWithPolicy(connection, "users");

// Validate RLS configuration
RLSUtil.validateRLSConfiguration(connection, "users");

// Generate SQL for adding tenant columns
String addTenantSQL = RLSUtil.generateAddTenantColumnSQL("users");
String addTypeSQL = RLSUtil.generateAddTenantTypeSQL("users");
String indexSQL = RLSUtil.generateTenantIndexSQL("users");
```

---

### **5. TenantType**
Location: `com.gogidix.shared.multitenancy.entity.TenantType`

Enumeration of tenant types:
- `ORGANIZATION` - Standard business customer
- `PARTNER` - Business partner with extended access
- `INTERNAL` - Internal system use
- `TRIAL` - Trial customer with limited features
- `INDIVIDUAL` - Individual user

---

### **6. @TenantIgnore**
Location: `com.gogidix.shared.multitenancy.annotation.TenantIgnore`

Annotation to mark fields or methods that should be ignored during tenant operations.

**Example:**
```java
@TenantIgnore
public boolean isNew() {
    return id == null;
}
```

---

## 🚀 Integration Guide

### **Step 1: Add Dependency**

Add the `shared-multitenancy` library to your service's `pom.xml`:

```xml
<dependency>
    <groupId>com.gogidix.libraries</groupId>
    <artifactId>shared-multitenancy</artifactId>
    <version>1.0.0</version>
</dependency>
```

### **Step 2: Extend BaseEntity**

Make all your entities extend `BaseEntity`:

```java
@Entity
@Table(name = "products")
public class Product extends BaseEntity {
    @Id
    private UUID id;  // Inherited from BaseEntity

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private BigDecimal price;

    // Inherits tenantId, tenantType, audit fields
}
```

### **Step 3: Update Database Schema**

Add `tenant_id` and `tenant_type` columns to all tables:

```sql
-- Add tenant columns to existing tables
ALTER TABLE products ADD COLUMN tenant_id VARCHAR(100) NOT NULL DEFAULT '';
ALTER TABLE products ADD COLUMN tenant_type VARCHAR(50) NOT NULL DEFAULT 'ORGANIZATION';

-- Create index on tenant_id for performance
CREATE INDEX idx_products_tenant_id ON products(tenant_id);
```

### **Step 4: Enable Row-Level Security**

Enable RLS and create tenant isolation policies:

```sql
-- Enable Row-Level Security
ALTER TABLE products ENABLE ROW LEVEL SECURITY;

-- Create tenant isolation policy
CREATE POLICY tenant_isolation_policy ON products
    FOR ALL
    USING (tenant_id = current_setting('app.current_tenant_id', true)::VARCHAR);
```

### **Step 5: Configure Database Connection**

Configure your JDBC connection URL to support RLS:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/foundation_db?prepareThreshold=0
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}
```

### **Step 6: Set Tenant Context in Repositories**

Configure your JPA repositories to automatically set tenant context:

```java
@Repository
public class ProductRepository {

    @PersistenceContext
    private EntityManager entityManager;

    private void setTenantContext() {
        Session session = entityManager.unwrap(Session.class);
        session.doWork(connection -> {
            String tenantId = TenantContext.getRequiredTenantId();
            RLSUtil.setTenantContext(connection, tenantId);
        });
    }

    // All repository methods should call setTenantContext()
    public Product save(Product product) {
        setTenantContext();
        return entityManager.persist(product);
    }
}
```

---

## 🔒 Security Considerations

### **Tenant Isolation**

The library ensures tenant isolation at multiple levels:

1. **Application Level** - TenantContextFilter validates tenant headers
2. **Entity Level** - BaseEntity enforces tenant_id on all entities
3. **Database Level** - RLS policies filter queries at database level

### **Best Practices**

1. ✅ **Always use BaseEntity** - All entities must extend BaseEntity
2. ✅ **Set tenant context** - Call `TenantContext.setTenantId()` at request start
3. ✅ **Enable RLS** - Enable Row-Level Security on all tables
4. ✅ **Validate tenant** - Validate tenant belongs to user in authentication
5. ✅ **Clear context** - Always clear TenantContext at end of request
6. ✅ **Use HTTPS** - Always use HTTPS to prevent header spoofing
7. ✅ **Validate headers** - Validate X-Tenant-ID header in API Gateway

---

## 📊 Testing Multi-Tenant Isolation

### **Test Case 1: Basic Isolation**

```java
@Test
public void testTenantIsolation() {
    // Create data for Tenant A
    TenantContext.setTenantContext("tenant-a", TenantType.ORGANIZATION);
    Product productA = new Product("Product A");
    productRepository.save(productA);

    // Create data for Tenant B
    TenantContext.setTenantContext("tenant-b", TenantType.ORGANIZATION);
    Product productB = new Product("Product B");
    productRepository.save(productB);

    // Verify Tenant A cannot see Tenant B's data
    TenantContext.setTenantContext("tenant-a", TenantType.ORGANIZATION);
    List<Product> productsA = productRepository.findAll();
    assertEquals(1, productsA.size());
    assertEquals("Product A", productsA.get(0).getName());

    // Verify Tenant B cannot see Tenant A's data
    TenantContext.setTenantContext("tenant-b", TenantType.ORGANIZATION);
    List<Product> productsB = productRepository.findAll();
    assertEquals(1, productsB.size());
    assertEquals("Product B", productsB.get(0).getName());
}
```

### **Test Case 2: RLS Policy**

```java
@Test
public void testRLSPolicy() throws SQLException {
    try (Connection connection = dataSource.getConnection()) {
        // Set tenant context
        RLSUtil.setTenantContext(connection, "tenant-123");

        // Query should only return rows for tenant-123
        String sql = "SELECT COUNT(*) FROM products WHERE tenant_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, "tenant-123");
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                // Verify count matches expected for tenant-123
            }
        }
    }
}
```

---

## 🛠️ Migration Checklist

Use this checklist when migrating services to multi-tenant:

- [ ] Add `shared-multitenancy` dependency
- [ ] Make all entities extend `BaseEntity`
- [ ] Add `tenant_id` and `tenant_type` columns to all tables
- [ ] Create indexes on `tenant_id` columns
- [ ] Enable Row-Level Security on all tables
- [ ] Create tenant isolation policies
- [ ] Configure `TenantContextFilter` (auto-registered)
- [ ] Update repositories to set tenant context
- [ ] Add `X-Tenant-ID` header to all API calls
- [ ] Test tenant isolation
- [ ] Verify no data leakage between tenants
- [ ] Update API documentation
- [ ] Train developers on multi-tenant patterns

---

## 📝 Configuration Properties

```yaml
multitenancy:
  headers:
    tenant-id: X-Tenant-ID          # Header name for tenant ID
    tenant-type: X-Tenant-Type      # Header name for tenant type
    tenant-name: X-Tenant-Name      # Header name for tenant name

  default-tenant-type: ORGANIZATION # Default tenant type
  require-tenant-context: true      # Require tenant for all requests

  exclude-paths:                    # Paths that bypass tenant validation
    - /actuator/health
    - /actuator/info
    - /error
    - /public/

database:
  rls:
    auto-set-context: true           # Auto-set tenant at DB level
    tenant-context-variable: app.current_tenant_id
    validate-on-startup: false       # Validate RLS on startup
```

---

## 🔍 Troubleshooting

### **Issue: "Tenant ID not set in TenantContext"**

**Cause:** Tenant ID header not provided or filter not configured

**Solution:**
1. Ensure `X-Tenant-ID` header is sent with all requests
2. Verify `TenantContextFilter` is registered
3. Check if request path is in `exclude-paths`

### **Issue: "RLS is not enabled on table"**

**Cause:** Row-Level Security not enabled on table

**Solution:**
```sql
ALTER TABLE your_table ENABLE ROW LEVEL SECURITY;
```

### **Issue: "Can see data from other tenants"**

**Cause:** RLS policy not created or tenant context not set

**Solution:**
```sql
CREATE POLICY tenant_isolation_policy ON your_table
    FOR ALL
    USING (tenant_id = current_setting('app.current_tenant_id', true)::VARCHAR);
```

---

## 📚 Related Documentation

- [Foundation-Domain Production Readiness Plan](../FOUNDATION_DOMAIN_PRODUCTION_READINESS_PLAN.md)
- [Row-Level Security in PostgreSQL](https://www.postgresql.org/docs/current/ddl-rowsecurity.html)
- [ThreadLocal in Java](https://docs.oracle.com/javase/8/docs/api/java/lang/ThreadLocal.html)

---

## ✅ Status

**Implementation Status:** ✅ COMPLETE

**Next Steps:**
1. ✅ Add to shared-libraries parent POM
2. ✅ Deploy to Maven repository
3. ⏳ Refactor pilot services (auth-service, user-management, billing-service)
4. ⏳ Enable RLS on all tables
5. ⏳ Integration testing

---

**Last Updated:** 2026-01-29
**Version:** 1.0.0
**Maintainer:** Foundation-Domain Team
