# MongoDB Database Setup - Quick Start Guide

**Management-Domain - Gogidix Ecosystem**
**Date:** 2026-01-29
**Status:** Ready for Phase 2

---

## ✅ MongoDB is Running

You have MongoDB installed and running with default settings:
- **Host:** localhost
- **Port:** 27017
- **Authentication:** None (default)
- **Data Directory:** `C:\Users\{username}\data\db`

---

## 🚀 Initialization Steps

### Step 1: Run the Initialization Script

Open MongoDB Shell (mongosh) or MongoDB Compass and run:

```bash
# Using mongosh (new MongoDB Shell)
mongosh < init-database.js

# OR using legacy mongo shell
mongo < init-database.js

# OR copy-paste the script content into mongosh
```

**Location:**
```
_Management-domain/_Foundation/database/initialization/init-database.js
```

**This will create:**
- 8 databases (one per domain)
- 19 collections with proper indexes
- All tenant isolation indexes

---

### Step 2: Verify Collections Created

Check that collections were created:

```bash
# In mongosh
use management_executive
show collections

# Should show:
# - approvals
# - alerts
# - kpi_metrics

use management_hr
show collections

# Should show:
# - employees
# - payroll_runs
# - leave_requests
```

---

### Step 3: Verify Indexes Created

Check that indexes were created:

```bash
# In mongosh
use management_executive
db.approvals.getIndexes()

# Should show:
# - _id_ (default)
# - tenant_entity_idx (compound tenant+id)
# - tenant_status_idx
# - tenant_approver_idx
```

---

## 📁 Configuration Files

### 1. Application Configuration

**File:** `application-mongodb.yml`

**Update your service's `application.yml` with:**
```yaml
spring:
  data:
    mongodb:
      host: localhost
      port: 27017
      database: management_executive  # Or domain-specific DB
```

### 2. Maven Dependencies

**Add to your service's `pom.xml`:**

```xml
<!-- Embedded MongoDB for fast tests -->
<dependency>
    <groupId>de.flapdoodle.embed</groupId>
    <artifactId>embed-mongo</artifactId>
    <version>4.11.0</version>
    <scope>test</scope>
</dependency>

<!-- Mongock for migrations -->
<dependency>
    <groupId>io.mongock</groupId>
    <artifactId>mongock-springboot</artifactId>
    <version>5.4.0</version>
</dependency>
```

---

## 🗄️ Database Structure

### Databases Created

| Database | Purpose |
|----------|---------|
| `management_executive` | Executive-Domain data |
| `management_hr` | Human-Resource data |
| `management_sales` | Sales-Departments data |
| `management_marketing` | Digital-Marketing data |
| `management_support` | Customer-Support data |
| `management_admin` | System-Administrator data |
| `management_business` | Global-Business-Management data |
| `management_finance` | Finance-Department data |
| `management_audit` | Audit logs (shared) |

### Collections & Indexes

**Every collection has:**
- ✅ `tenant_entity_idx` on `(tenantId, _id)` - **CRITICAL for tenant isolation**
- ✅ Additional indexes for common query patterns
- ✅ Audit logs have TTL index (1 year retention)

---

## 🧪 Testing Configuration

### Unit Tests (Fast)

Uses **Embedded MongoDB** - No external database needed!

```java
@SpringBootTest
@AutoConfigureEmbeddedMongo
public class EmployeeServiceTest {
    // Tests run against in-memory MongoDB
    // Super fast (~2 seconds startup)
    // No Docker required
}
```

### Integration Tests (Optional)

Can test against real MongoDB:

```java
@SpringBootTest
@ActiveProfiles("integration")
public class EmployeeIntegrationTest {
    // Tests run against your local MongoDB
    // Tests real queries and indexes
}
```

---

## 🔍 MongoDB Compass (Optional GUI)

**Download:** https://www.mongodb.com/try/download/compass

**Connect to:**
- Host: `localhost`
- Port: `27017`
- No authentication needed

**What you can do:**
- Browse databases and collections
- View documents
- Run queries
- Check indexes
- Monitor performance

---

## 📝 Next Steps

### 1. Run Initialization Script
```bash
mongosh < _Foundation/database/initialization/init-database.js
```

### 2. Verify Setup
```bash
mongosh
> show dbs
> use management_executive
> show collections
> db.approvals.getIndexes()
```

### 3. Start Phase 2 Development
All services can now:
- Connect to MongoDB
- Create/read/update/delete documents
- Use tenant isolation
- Run unit tests with Embedded MongoDB

---

## 🎯 Key Points

✅ **No Docker Required** - Everything runs natively
✅ **No H2 Confusion** - Using actual MongoDB in development
✅ **Fast Tests** - Embedded MongoDB for unit tests
✅ **Production-Ready** - Same database as production
✅ **Tenant Isolation** - All collections have tenant indexes

---

## ⚠️ Important Notes

### Development vs Production

**Development (Current Setup):**
- No authentication
- All databases on one server
- Auto-index creation enabled
- Embedded MongoDB for tests

**Production (Future):**
- Authentication enabled
- Replica set for high availability
- Sharding for large datasets
- Manual index creation
- Connection pooling configured

### Migration Strategy

- **Phase 1.5:** Manual initialization with scripts (current)
- **Phase 2:** Mongock for schema changes
- **Phase 3+:** Automated migrations via Mongock

---

## 📞 Troubleshooting

### MongoDB Not Running?

**Start MongoDB Service:**
```bash
# Windows Services
services.msc → MongoDB → Start

# OR command line (as Administrator)
net start MongoDB
```

### Can't Connect?

**Check MongoDB is listening:**
```bash
mongosh
# Should connect automatically to localhost:27017
```

### Collections Not Created?

**Re-run initialization script:**
```bash
mongosh < init-database.js
```

---

## ✅ Ready for Phase 2

Your MongoDB database is:
- ✅ Installed and running
- ✅ Initialized with collections
- ✅ Configured with indexes
- ✅ Ready for tenant isolation
- ✅ Setup for testing

**You can now start Phase 2 development!**
