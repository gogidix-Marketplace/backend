# MongoDB Initialization - Manual Steps

**Since mongosh is not accessible from the command line, please follow these manual steps:**

---

## Step 1: Open MongoDB Shell

**Option A: Using MongoDB Compass (if installed)**
1. Open MongoDB Compass
2. Connect to: `mongodb://localhost:27017`
3. Click on "MongoDB Shell" button (or press Ctrl+`)

**Option B: Using Command Prompt**
1. Open Command Prompt as Administrator
2. Navigate to MongoDB bin directory:
   ```
   cd "C:\Program Files\MongoDB\Server\7.0\bin"
   ```
3. Run: `mongosh`

**Option C: Using Windows Terminal**
1. Open Windows Terminal
2. Run: `mongosh`

---

## Step 2: Copy and Run This Script

Once you have the MongoDB shell open (you'll see `>` prompt), copy and paste the following:

```javascript
// =============================================================================
// MongoDB Initialization Script
// Management-Domain - Gogidix Ecosystem
// =============================================================================

print("===========================================");
print("MongoDB Initialization - Management Domain");
print("===========================================");
print("");

// 1. Create Executive Domain Database
print("Creating Executive Domain collections...");
db = db.getSiblingDB('management_executive');

db.approvals.createIndex({ "tenantId": 1, "_id": 1 }, { name: "tenant_entity_idx" });
db.approvals.createIndex({ "tenantId": 1, "status": 1 }, { name: "tenant_status_idx" });
db.approvals.createIndex({ "tenantId": 1, "approverId": 1, "createdAt": -1 }, { name: "tenant_approver_idx" });
db.approvals.createIndex({ "tenantId": 1, "approvalType": 1, "status": 1 }, { name: "tenant_type_status_idx" });

db.alerts.createIndex({ "tenantId": 1, "_id": 1 }, { name: "tenant_entity_idx" });
db.alerts.createIndex({ "tenantId": 1, "severity": 1, "resolved": 1 }, { name: "tenant_alert_idx" });
db.alerts.createIndex({ "tenantId": 1, "createdAt": -1 }, { name: "tenant_created_idx" });

db.kpi_metrics.createIndex({ "tenantId": 1, "_id": 1 }, { name: "tenant_entity_idx" });
db.kpi_metrics.createIndex({ "tenantId": 1, "category": 1, "period": -1 }, { name: "tenant_category_period_idx" });
db.kpi_metrics.createIndex({ "tenantId": 1, "executiveLevel": 1, "category": 1 }, { name: "tenant_executive_category_idx" });

print("✓ Executive Domain: 3 collections created with indexes");

// 2. Create HR Database
print("Creating HR Domain collections...");
db = db.getSiblingDB('management_hr');

db.employees.createIndex({ "tenantId": 1, "_id": 1 }, { name: "tenant_entity_idx" });
db.employees.createIndex({ "tenantId": 1, "email": 1 }, { unique: true, name: "tenant_email_idx" });
db.employees.createIndex({ "tenantId": 1, "department": 1, "status": 1 }, { name: "tenant_dept_status_idx" });

db.payroll_runs.createIndex({ "tenantId": 1, "_id": 1 }, { name: "tenant_entity_idx" });
db.payroll_runs.createIndex({ "tenantId": 1, "runDate": -1 }, { name: "tenant_date_idx" });

db.leave_requests.createIndex({ "tenantId": 1, "_id": 1 }, { name: "tenant_entity_idx" });
db.leave_requests.createIndex({ "tenantId": 1, "employeeId": 1, "status": 1 }, { name: "tenant_employee_status_idx" });

print("✓ HR Domain: 3 collections created with indexes");

// 3. Create Sales Database
print("Creating Sales Domain collections...");
db = db.getSiblingDB('management_sales');

db.leads.createIndex({ "tenantId": 1, "_id": 1 }, { name: "tenant_entity_idx" });
db.leads.createIndex({ "tenantId": 1, "email": 1 }, { name: "tenant_email_idx" });
db.leads.createIndex({ "tenantId": 1, "status": 1, "score": -1 }, { name: "tenant_status_score_idx" });

db.deals.createIndex({ "tenantId": 1, "_id": 1 }, { name: "tenant_entity_idx" });
db.deals.createIndex({ "tenantId": 1, "stage": 1, "createdAt": -1 }, { name: "tenant_stage_idx" });
db.deals.createIndex({ "tenantId": 1, "salesRepId": 1, "status": 1 }, { name: "tenant_rep_status_idx" });

db.customers.createIndex({ "tenantId": 1, "_id": 1 }, { name: "tenant_entity_idx" });
db.customers.createIndex({ "tenantId": 1, "email": 1 }, { unique: true, name: "tenant_email_idx" });

print("✓ Sales Domain: 3 collections created with indexes");

// 4. Create Marketing Database
print("Creating Marketing Domain collections...");
db = db.getSiblingDB('management_marketing');

db.campaigns.createIndex({ "tenantId": 1, "_id": 1 }, { name: "tenant_entity_idx" });
db.campaigns.createIndex({ "tenantId": 1, "status": 1, "startDate": -1 }, { name: "tenant_status_date_idx" });

db.leads_generated.createIndex({ "tenantId": 1, "_id": 1 }, { name: "tenant_entity_idx" });
db.leads_generated.createIndex({ "tenantId": 1, "campaignId": 1, "createdAt": -1 }, { name: "tenant_campaign_idx" });

print("✓ Marketing Domain: 2 collections created with indexes");

// 5. Create Support Database
print("Creating Support Domain collections...");
db = db.getSiblingDB('management_support');

db.tickets.createIndex({ "tenantId": 1, "_id": 1 }, { name: "tenant_entity_idx" });
db.tickets.createIndex({ "tenantId": 1, "status": 1, "priority": 1 }, { name: "tenant_status_priority_idx" });
db.tickets.createIndex({ "tenantId": 1, "assignedTo": 1, "status": 1 }, { name: "tenant_assignee_idx" });

db.knowledge_articles.createIndex({ "tenantId": 1, "_id": 1 }, { name: "tenant_entity_idx" });
db.knowledge_articles.createIndex({ "tenantId": 1, "category": 1, "published": 1 }, { name: "tenant_category_idx" });

print("✓ Support Domain: 2 collections created with indexes");

// 6. Create System Admin Database
print("Creating System Admin Domain collections...");
db = db.getSiblingDB('management_admin');

db.incidents.createIndex({ "tenantId": 1, "_id": 1 }, { name: "tenant_entity_idx" });
db.incidents.createIndex({ "tenantId": 1, "severity": 1, "status": 1 }, { name: "tenant_severity_status_idx" });
db.incidents.createIndex({ "tenantId": 1, "createdAt": -1 }, { name: "tenant_created_idx" });

db.users.createIndex({ "tenantId": 1, "_id": 1 }, { name: "tenant_entity_idx" });
db.users.createIndex({ "tenantId": 1, "username": 1 }, { unique: true, name: "tenant_username_idx" });
db.users.createIndex({ "tenantId": 1, "email": 1 }, { unique: true, name: "tenant_email_idx" });

print("✓ System Admin Domain: 2 collections created with indexes");

// 7. Create Global Business Database
print("Creating Global Business Domain collections...");
db = db.getSiblingDB('management_business');

db.country_data.createIndex({ "tenantId": 1, "_id": 1 }, { name: "tenant_entity_idx" });
db.country_data.createIndex({ "tenantId": 1, "countryCode": 1 }, { unique: true, name: "tenant_country_idx" });

db.reports.createIndex({ "tenantId": 1, "_id": 1 }, { name: "tenant_entity_idx" });
db.reports.createIndex({ "tenantId": 1, "reportType": 1, "generatedAt": -1 }, { name: "tenant_type_date_idx" });

print("✓ Global Business Domain: 2 collections created with indexes");

// 8. Create Finance Database
print("Creating Finance Domain collections...");
db = db.getSiblingDB('management_finance');

db.invoices.createIndex({ "tenantId": 1, "_id": 1 }, { name: "tenant_entity_idx" });
db.invoices.createIndex({ "tenantId": 1, "invoiceNumber": 1 }, { unique: true, name: "tenant_invoice_idx" });
db.invoices.createIndex({ "tenantId": 1, "status": 1, "dueDate": 1 }, { name: "tenant_status_date_idx" });

db.transactions.createIndex({ "tenantId": 1, "_id": 1 }, { name: "tenant_entity_idx" });
db.transactions.createIndex({ "tenantId": 1, "transactionDate": -1 }, { name: "tenant_date_idx" });

db.budgets.createIndex({ "tenantId": 1, "_id": 1 }, { name: "tenant_entity_idx" });
db.budgets.createIndex({ "tenantId": 1, "fiscalYear": 1, "category": 1 }, { name: "tenant_year_category_idx" });

print("✓ Finance Domain: 3 collections created with indexes");

// 9. Create Audit Database (Shared)
print("Creating Audit Database collections...");
db = db.getSiblingDB('management_audit');

db.audit_logs.createIndex({ "tenantId": 1, "_id": 1 }, { name: "tenant_entity_idx" });
db.audit_logs.createIndex({ "tenantId": 1, "createdAt": 1 }, { name: "tenant_created_idx", expireAfterSeconds: 31536000 }); // 1 year TTL

print("✓ Audit Database: 1 collection created with TTL index");

// Summary
print("");
print("===========================================");
print("✓ MongoDB Initialization Complete!");
print("===========================================");
print("");
print("Databases created:");
print("  - management_executive (3 collections)");
print("  - management_hr (3 collections)");
print("  - management_sales (3 collections)");
print("  - management_marketing (2 collections)");
print("  - management_support (2 collections)");
print("  - management_admin (2 collections)");
print("  - management_business (2 collections)");
print("  - management_finance (3 collections)");
print("  - management_audit (1 collection)");
print("");
print("Total: 8 databases, 21 collections");
print("");
print("All collections have tenant isolation indexes!");
print("");
print("Next steps:");
print("  1. Verify databases: show dbs");
print("  2. View collections: use management_executive; show collections");
print("  3. Check indexes: db.approvals.getIndexes()");
print("  4. Start Phase 2 development!");
print("===========================================");
```

---

## Step 3: Verify Initialization

After running the script, verify everything was created:

```javascript
// List all databases
show dbs

// You should see:
// - management_executive
// - management_hr
// - management_sales
// - management_marketing
// - management_support
// - management_admin
// - management_business
// - management_finance
// - management_audit

// Switch to a database and view collections
use management_executive
show collections

// View indexes on a collection
db.approvals.getIndexes()
```

---

## Step 4: You're Ready!

Once you've verified the databases and collections exist, you're ready to start Phase 2 development!

All services will automatically connect to these databases using the configuration in `application-mongodb.yml`.

---

## Need Help?

If you encounter any issues:
1. Ensure MongoDB is running (check Windows Services)
2. Try connecting with MongoDB Compass if available
3. Check the MongoDB logs in: `C:\Users\{username}\mongodb\log\`
