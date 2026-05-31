// =============================================================================
// MongoDB Initialization Script
// Management-Domain - Gogidix Ecosystem
// =============================================================================
// This script initializes the MongoDB database for local development.
// Run this script in MongoDB Shell or mongosh after starting MongoDB.
//
// Usage:
//   mongosh < this_script.js
//   OR
//   mongo < this_script.js
// =============================================================================

print("=== MongoDB Initialization for Management-Domain ===");
print("");

// =============================================================================
// 1. CREATE DATABASES
// =============================================================================
// MongoDB creates databases automatically when you first write to them.
// Here we'll create collections to initialize the databases.

// Management-Domain databases
const execDb = db.getSiblingDB('management_executive');
const hrDb = db.getSiblingDB('management_hr');
const salesDb = db.getSiblingDB('management_sales');
const marketingDb = db.getSiblingDB('management_marketing');
const supportDb = db.getSiblingDB('management_support');
const adminDb = db.getSiblingDB('management_admin');
const businessDb = db.getSiblingDB('management_business');
const financeDb = db.getSiblingDB('management_finance');

print("✓ Databases initialized:");
print("  - management_executive");
print("  - management_hr");
print("  - management_sales");
print("  - management_marketing");
print("  - management_support");
print("  - management_admin");
print("  - management_business");
print("  - management_finance");
print("");

// =============================================================================
// 2. CREATE APPLICATION USER (Optional - for development, no auth by default)
// =============================================================================
// Uncomment below if you want to enable authentication

/*
adminDb.createUser({
  user: "management_app",
  pwd: "management123",
  roles: [
    { role: "readWrite", db: "management_executive" },
    { role: "readWrite", db: "management_hr" },
    { role: "readWrite", db: "management_sales" },
    { role: "readWrite", db: "management_marketing" },
    { role: "readWrite", db: "management_support" },
    { role: "readWrite", db: "management_admin" },
    { role: "readWrite", db: "management_business" },
    { role: "readWrite", db: "management_finance" }
  ]
});

print("✓ Application user created: management_app");
print("");
*/

// =============================================================================
// 3. INITIALIZE EXECUTIVE-DOMAIN COLLECTIONS
// =============================================================================

// Executive approvals collection
execDb.createCollection('approvals');
execDb.approvals.createIndex({ "tenantId": 1, "_id": 1 }, { name: "tenant_entity_idx" });
execDb.approvals.createIndex({ "tenantId": 1, "status": 1 }, { name: "tenant_status_idx" });
execDb.approvals.createIndex({ "tenantId": 1, "approver": 1, "createdAt": -1 }, { name: "tenant_approver_idx" });
print("✓ Executive-Domain: approvals collection created with indexes");

// Executive alerts collection
execDb.createCollection('alerts');
execDb.alerts.createIndex({ "tenantId": 1, "_id": 1 }, { name: "tenant_entity_idx" });
execDb.alerts.createIndex({ "tenantId": 1, "severity": 1, "createdAt": -1 }, { name: "tenant_severity_idx" });
execDb.alerts.createIndex({ "tenantId": 1, "status": 1 }, { name: "tenant_status_idx" });
print("✓ Executive-Domain: alerts collection created with indexes");

// KPI metrics collection
execDb.createCollection('kpi_metrics');
execDb.kpi_metrics.createIndex({ "tenantId": 1, "domain": 1, "period": -1 }, { name: "tenant_domain_period_idx" });
execDb.kpi_metrics.createIndex({ "tenantId": 1, "metricName": 1, "timestamp": -1 }, { name: "tenant_metric_time_idx" });
print("✓ Executive-Domain: kpi_metrics collection created with indexes");

// =============================================================================
// 4. INITIALIZE HUMAN-RESOURCE COLLECTIONS
// =============================================================================

// Employees collection
hrDb.createCollection('employees');
hrDb.employees.createIndex({ "tenantId": 1, "_id": 1 }, { name: "tenant_entity_idx" });
hrDb.employees.createIndex({ "tenantId": 1, "email": 1 }, { unique: true, name: "tenant_email_idx" });
hrDb.employees.createIndex({ "tenantId": 1, "country": 1 }, { name: "tenant_country_idx" });
hrDb.employees.createIndex({ "tenantId": 1, "department": 1 }, { name: "tenant_department_idx" });
hrDb.employees.createIndex({ "tenantId": 1, "status": 1 }, { name: "tenant_status_idx" });
print("✓ Human-Resource: employees collection created with indexes");

// Payroll collection
hrDb.createCollection('payroll_runs');
hrDb.payroll_runs.createIndex({ "tenantId": 1, "_id": 1 }, { name: "tenant_entity_idx" });
hrDb.payroll_runs.createIndex({ "tenantId": 1, "period": -1 }, { name: "tenant_period_idx" });
hrDb.payroll_runs.createIndex({ "tenantId": 1, "country": 1, "status": 1 }, { name: "tenant_country_status_idx" });
print("✓ Human-Resource: payroll_runs collection created with indexes");

// Leave requests collection
hrDb.createCollection('leave_requests');
hrDb.leave_requests.createIndex({ "tenantId": 1, "_id": 1 }, { name: "tenant_entity_idx" });
hrDb.leave_requests.createIndex({ "tenantId": 1, "employeeId": 1, "startDate": -1 }, { name: "tenant_employee_date_idx" });
hrDb.leave_requests.createIndex({ "tenantId": 1, "status": 1 }, { name: "tenant_status_idx" });
print("✓ Human-Resource: leave_requests collection created with indexes");

// =============================================================================
// 5. INITIALIZE SALES-DEPARTMENTS COLLECTIONS
// =============================================================================

// Leads collection
salesDb.createCollection('leads');
salesDb.leads.createIndex({ "tenantId": 1, "_id": 1 }, { name: "tenant_entity_idx" });
salesDb.leads.createIndex({ "tenantId": 1, "email": 1 }, { name: "tenant_email_idx" });
salesDb.leads.createIndex({ "tenantId": 1, "status": 1, "score": -1 }, { name: "tenant_status_score_idx" });
salesDb.leads.createIndex({ "tenantId": 1, "assignedTo": 1 }, { name: "tenant_assigned_idx" });
print("✓ Sales-Departments: leads collection created with indexes");

// Deals collection
salesDb.createCollection('deals');
salesDb.deals.createIndex({ "tenantId": 1, "_id": 1 }, { name: "tenant_entity_idx" });
salesDb.deals.createIndex({ "tenantId": 1, "stage": 1, "expectedCloseDate": 1 }, { name: "tenant_stage_date_idx" });
salesDb.deals.createIndex({ "tenantId": 1, "leadId": 1 }, { name: "tenant_lead_idx" });
print("✓ Sales-Departments: deals collection created with indexes");

// Customers collection
salesDb.createCollection('customers');
salesDb.customers.createIndex({ "tenantId": 1, "_id": 1 }, { name: "tenant_entity_idx" });
salesDb.customers.createIndex({ "tenantId": 1, "companyName": 1 }, { name: "tenant_company_idx" });
salesDb.customers.createIndex({ "tenantId": 1, "country": 1 }, { name: "tenant_country_idx" });
print("✓ Sales-Departments: customers collection created with indexes");

// =============================================================================
// 6. INITIALIZE DIGITAL-MARKETING COLLECTIONS
// =============================================================================

marketingDb.createCollection('campaigns');
marketingDb.campaigns.createIndex({ "tenantId": 1, "_id": 1 }, { name: "tenant_entity_idx" });
marketingDb.campaigns.createIndex({ "tenantId": 1, "status": 1, "startDate": -1 }, { name: "tenant_status_date_idx" });
print("✓ Digital-Marketing: campaigns collection created with indexes");

marketingDb.createCollection('leads');
marketingDb.leads.createIndex({ "tenantId": 1, "_id": 1 }, { name: "tenant_entity_idx" });
marketingDb.leads.createIndex({ "tenantId": 1, "campaignId": 1 }, { name: "tenant_campaign_idx" });
print("✓ Digital-Marketing: leads collection created with indexes");

// =============================================================================
// 7. INITIALIZE CUSTOMER-SUPPORT COLLECTIONS
// =============================================================================

supportDb.createCollection('tickets');
supportDb.tickets.createIndex({ "tenantId": 1, "_id": 1 }, { name: "tenant_entity_idx" });
supportDb.tickets.createIndex({ "tenantId": 1, "customerId": 1, "status": 1, "createdAt": -1 }, { name: "tenant_customer_status_idx" });
supportDb.tickets.createIndex({ "tenantId": 1, "priority": 1, "status": 1 }, { name: "tenant_priority_status_idx" });
supportDb.tickets.createIndex({ "tenantId": 1, "assignedTo": 1 }, { name: "tenant_assigned_idx" });
print("✓ Customer-Support: tickets collection created with indexes");

// =============================================================================
// 8. INITIALIZE SYSTEM-ADMINISTRATOR COLLECTIONS
// =============================================================================

adminDb.createCollection('incidents');
adminDb.incidents.createIndex({ "tenantId": 1, "_id": 1 }, { name: "tenant_entity_idx" });
adminDb.incidents.createIndex({ "tenantId": 1, "severity": 1, "status": 1, "createdAt": -1 }, { name: "tenant_severity_status_idx" });
adminDb.incidents.createIndex({ "service": 1, "status": 1 }, { name: "service_status_idx" });
print("✓ System-Administrator: incidents collection created with indexes");

adminDb.createCollection('users');
adminDb.users.createIndex({ "tenantId": 1, "_id": 1 }, { name: "tenant_entity_idx" });
adminDb.users.createIndex({ "tenantId": 1, "username": 1 }, { unique: true, name: "tenant_username_idx" });
adminDb.users.createIndex({ "tenantId": 1, "email": 1 }, { unique: true, name: "tenant_email_idx" });
print("✓ System-Administrator: users collection created with indexes");

// =============================================================================
// 9. INITIALIZE GLOBAL-BUSINESS-MANAGEMENT COLLECTIONS
// =============================================================================

businessDb.createCollection('ingestion_logs');
businessDb.ingestion_logs.createIndex({ "tenantId": 1, "source": 1, "ingestedAt": -1 }, { name: "tenant_source_time_idx" });
businessDb.ingestion_logs.createIndex({ "tenantId": 1, "status": 1 }, { name: "tenant_status_idx" });
print("✓ Global-Business-Management: ingestion_logs collection created with indexes");

businessDb.createCollection('regional_metrics');
businessDb.regional_metrics.createIndex({ "tenantId": 1, "region": 1, "period": -1 }, { name: "tenant_region_period_idx" });
print("✓ Global-Business-Management: regional_metrics collection created with indexes");

// =============================================================================
// 10. INITIALIZE FINANCE-DEPARTMENT COLLECTIONS
// =============================================================================

financeDb.createCollection('invoices');
financeDb.invoices.createIndex({ "tenantId": 1, "_id": 1 }, { name: "tenant_entity_idx" });
financeDb.invoices.createIndex({ "tenantId": 1, "invoiceNumber": 1 }, { unique: true, name: "tenant_invoice_idx" });
financeDb.invoices.createIndex({ "tenantId": 1, "vendorId": 1, "dueDate": 1 }, { name: "tenant_vendor_date_idx" });
financeDb.invoices.createIndex({ "tenantId": 1, "status": 1 }, { name: "tenant_status_idx" });
print("✓ Finance-Department: invoices collection created with indexes");

financeDb.createCollection('budgets');
financeDb.budgets.createIndex({ "tenantId": 1, "_id": 1 }, { name: "tenant_entity_idx" });
financeDb.budgets.createIndex({ "tenantId": 1, "fiscalYear": 1, "department": 1 }, { unique: true, name: "tenant_year_dept_idx" });
print("✓ Finance-Department: budgets collection created with indexes");

// =============================================================================
// 11. CREATE AUDIT LOG COLLECTION (Shared across all domains)
// =============================================================================

const auditDb = db.getSiblingDB('management_audit');
auditDb.createCollection('audit_logs');
auditDb.audit_logs.createIndex({ "tenantId": 1, "timestamp": -1 }, { name: "tenant_time_idx" });
auditDb.audit_logs.createIndex({ "tenantId": 1, "userId": 1, "timestamp": -1 }, { name: "tenant_user_time_idx" });
auditDb.audit_logs.createIndex({ "tenantId": 1, "entity": 1, "entityId": 1, "timestamp": -1 }, { name: "tenant_entity_idx" });
auditDb.audit_logs.createIndex({ "timestamp": 1 }, { name: "timestamp_idx" }); // For cleanup
print("✓ Audit: audit_logs collection created with indexes");

// =============================================================================
// 12. CREATE TTL INDEX FOR AUDIT LOGS (Auto-delete after 1 year)
// =============================================================================

auditDb.audit_logs.createIndex(
    { "timestamp": 1 },
    {
        name: "ttl_idx",
        expireAfterSeconds: 31536000, // 365 days in seconds
        background: true
    }
);
print("✓ Audit: TTL index created (1 year retention)");

// =============================================================================
// COMPLETE
// =============================================================================

print("");
print("=== MongoDB Initialization Complete ===");
print("");
print("Database Summary:");
print("  Executive-Domain: 3 collections");
print("  Human-Resource: 3 collections");
print("  Sales-Departments: 3 collections");
print("  Digital-Marketing: 2 collections");
print("  Customer-Support: 1 collection");
print("  System-Administrator: 2 collections");
print("  Global-Business-Management: 2 collections");
print("  Finance-Department: 2 collections");
print("  Audit (shared): 1 collection");
print("");
print("Total: 19 collections created");
print("All collections have tenant isolation indexes");
print("");
print("MongoDB is ready for Phase 2 development!");
print("===========================================");
