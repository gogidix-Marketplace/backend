# =============================================================================
# README - Run MongoDB Initialization
# Management-Domain - Database Setup
# =============================================================================

## Quick Start

### Option 1: Using mongosh (MongoDB Shell)

Open PowerShell or Command Prompt and navigate to:
```
cd C:\Users\TEMP.LAPTOP-1QDBFFCA\Desktop\Gogidix-ecosystem\x-gogidix-domain\Management-domain\_Foundation\database\initialization
```

Run:
```bash
mongosh init-database.js
```

### Option 2: Using PowerShell Script

```powershell
cd C:\Users\TEMP.LAPTOP-1QDBFFCA\Desktop\Gogidix-ecosystem\x-gogidix-domain\Management-domain\_Foundation\database\initialization
.\Initialize-MongoDB.ps1
```

### Option 3: Using MongoDB Compass

1. Open MongoDB Compass
2. Connect to: `mongodb://localhost:27017`
3. Click on "Mongosh" button (opens shell)
4. Copy and paste the contents of `init-database.js`
5. Press Enter

---

## What Gets Created

**8 Databases:**
- management_executive
- management_hr
- management_sales
- management_marketing
- management_support
- management_admin
- management_business
- management_finance
- management_audit

**19 Collections with Indexes:**
- Executive: approvals, alerts, kpi_metrics
- HR: employees, payroll_runs, leave_requests
- Sales: leads, deals, customers
- Marketing: campaigns, leads
- Support: tickets
- Admin: incidents, users
- Global-Business: ingestion_logs, regional_metrics
- Finance: invoices, budgets
- Audit: audit_logs

---

## Verification Steps

After initialization, verify with mongosh:

```bash
# List all databases
mongosh
> show dbs

# Check executive domain
> use management_executive
> show collections
> db.approvals.getIndexes()

# Check HR domain
> use management_hr
> show collections
> db.employees.getIndexes()

# Exit
> exit
```

---

## Troubleshooting

### "mongosh not found"
Install MongoDB Shell from: https://www.mongodb.com/try/download/shell

### Or use legacy mongo shell:
```bash
mongo < init-database.js
```

### "Connection refused"
Make sure MongoDB service is running:
- Open Windows Services (services.msc)
- Find "MongoDB"
- Start the service

---

## Status

Once initialization completes successfully, you'll see:
```
=== MongoDB Initialization Complete ===
MongoDB is ready for Phase 2 development!
===========================================
```

Then you can proceed with Phase 2 implementation!
