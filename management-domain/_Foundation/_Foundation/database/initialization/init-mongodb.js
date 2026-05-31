// =============================================================================
// MongoDB Initialization Script - Node.js Version
// Management-Domain - Gogidix Ecosystem
//
// Run: node init-mongodb.js
// =============================================================================

const { MongoClient } = require('mongodb');

const url = 'mongodb://localhost:27017';
const client = new MongoClient(url);

async function initializeDatabases() {
    try {
        console.log('===========================================');
        console.log('MongoDB Initialization - Management Domain');
        console.log('===========================================');
        console.log('');
        console.log('Connecting to MongoDB...');

        await client.connect();
        console.log('✓ Connected to MongoDB');
        console.log('');

        const db = client.db('admin');
        const result = await db.admin().listDatabases();
        console.log('Current databases:');
        result.databases.forEach(d => console.log(`  - ${d.name}`));
        console.log('');

        // 1. Create Executive Domain Database
        console.log('Creating Executive Domain collections...');
        let execDb = client.db('management_executive');

        await execDb.collection('approvals').createIndex({ "tenantId": 1, "_id": 1 }, { name: "tenant_entity_idx" });
        await execDb.collection('approvals').createIndex({ "tenantId": 1, "status": 1 }, { name: "tenant_status_idx" });
        await execDb.collection('approvals').createIndex({ "tenantId": 1, "approverId": 1, "createdAt": -1 }, { name: "tenant_approver_idx" });
        await execDb.collection('approvals').createIndex({ "tenantId": 1, "approvalType": 1, "status": 1 }, { name: "tenant_type_status_idx" });

        await execDb.collection('alerts').createIndex({ "tenantId": 1, "_id": 1 }, { name: "tenant_entity_idx" });
        await execDb.collection('alerts').createIndex({ "tenantId": 1, "severity": 1, "resolved": 1 }, { name: "tenant_alert_idx" });
        await execDb.collection('alerts').createIndex({ "tenantId": 1, "createdAt": -1 }, { name: "tenant_created_idx" });

        await execDb.collection('kpi_metrics').createIndex({ "tenantId": 1, "_id": 1 }, { name: "tenant_entity_idx" });
        await execDb.collection('kpi_metrics').createIndex({ "tenantId": 1, "category": 1, "period": -1 }, { name: "tenant_category_period_idx" });
        await execDb.collection('kpi_metrics').createIndex({ "tenantId": 1, "executiveLevel": 1, "category": 1 }, { name: "tenant_executive_category_idx" });

        console.log('✓ Executive Domain: 3 collections with indexes');

        // 2. Create HR Database
        console.log('Creating HR Domain collections...');
        let hrDb = client.db('management_hr');

        await hrDb.collection('employees').createIndex({ "tenantId": 1, "_id": 1 }, { name: "tenant_entity_idx" });
        await hrDb.collection('employees').createIndex({ "tenantId": 1, "email": 1 }, { unique: true, name: "tenant_email_idx" });
        await hrDb.collection('employees').createIndex({ "tenantId": 1, "department": 1, "status": 1 }, { name: "tenant_dept_status_idx" });

        await hrDb.collection('payroll_runs').createIndex({ "tenantId": 1, "_id": 1 }, { name: "tenant_entity_idx" });
        await hrDb.collection('payroll_runs').createIndex({ "tenantId": 1, "runDate": -1 }, { name: "tenant_date_idx" });

        await hrDb.collection('leave_requests').createIndex({ "tenantId": 1, "_id": 1 }, { name: "tenant_entity_idx" });
        await hrDb.collection('leave_requests').createIndex({ "tenantId": 1, "employeeId": 1, "status": 1 }, { name: "tenant_employee_status_idx" });

        console.log('✓ HR Domain: 3 collections with indexes');

        // 3. Create Sales Database
        console.log('Creating Sales Domain collections...');
        let salesDb = client.db('management_sales');

        await salesDb.collection('leads').createIndex({ "tenantId": 1, "_id": 1 }, { name: "tenant_entity_idx" });
        await salesDb.collection('leads').createIndex({ "tenantId": 1, "email": 1 }, { name: "tenant_email_idx" });
        await salesDb.collection('leads').createIndex({ "tenantId": 1, "status": 1, "score": -1 }, { name: "tenant_status_score_idx" });

        await salesDb.collection('deals').createIndex({ "tenantId": 1, "_id": 1 }, { name: "tenant_entity_idx" });
        await salesDb.collection('deals').createIndex({ "tenantId": 1, "stage": 1, "createdAt": -1 }, { name: "tenant_stage_idx" });
        await salesDb.collection('deals').createIndex({ "tenantId": 1, "salesRepId": 1, "status": 1 }, { name: "tenant_rep_status_idx" });

        await salesDb.collection('customers').createIndex({ "tenantId": 1, "_id": 1 }, { name: "tenant_entity_idx" });
        await salesDb.collection('customers').createIndex({ "tenantId": 1, "email": 1 }, { unique: true, name: "tenant_email_idx" });

        console.log('✓ Sales Domain: 3 collections with indexes');

        // 4. Create Marketing Database
        console.log('Creating Marketing Domain collections...');
        let mktDb = client.db('management_marketing');

        await mktDb.collection('campaigns').createIndex({ "tenantId": 1, "_id": 1 }, { name: "tenant_entity_idx" });
        await mktDb.collection('campaigns').createIndex({ "tenantId": 1, "status": 1, "startDate": -1 }, { name: "tenant_status_date_idx" });

        await mktDb.collection('leads_generated').createIndex({ "tenantId": 1, "_id": 1 }, { name: "tenant_entity_idx" });
        await mktDb.collection('leads_generated').createIndex({ "tenantId": 1, "campaignId": 1, "createdAt": -1 }, { name: "tenant_campaign_idx" });

        console.log('✓ Marketing Domain: 2 collections with indexes');

        // 5. Create Support Database
        console.log('Creating Support Domain collections...');
        let supportDb = client.db('management_support');

        await supportDb.collection('tickets').createIndex({ "tenantId": 1, "_id": 1 }, { name: "tenant_entity_idx" });
        await supportDb.collection('tickets').createIndex({ "tenantId": 1, "status": 1, "priority": 1 }, { name: "tenant_status_priority_idx" });
        await supportDb.collection('tickets').createIndex({ "tenantId": 1, "assignedTo": 1, "status": 1 }, { name: "tenant_assignee_idx" });

        await supportDb.collection('knowledge_articles').createIndex({ "tenantId": 1, "_id": 1 }, { name: "tenant_entity_idx" });
        await supportDb.collection('knowledge_articles').createIndex({ "tenantId": 1, "category": 1, "published": 1 }, { name: "tenant_category_idx" });

        console.log('✓ Support Domain: 2 collections with indexes');

        // 6. Create System Admin Database
        console.log('Creating System Admin Domain collections...');
        let adminDb = client.db('management_admin');

        await adminDb.collection('incidents').createIndex({ "tenantId": 1, "_id": 1 }, { name: "tenant_entity_idx" });
        await adminDb.collection('incidents').createIndex({ "tenantId": 1, "severity": 1, "status": 1 }, { name: "tenant_severity_status_idx" });
        await adminDb.collection('incidents').createIndex({ "tenantId": 1, "createdAt": -1 }, { name: "tenant_created_idx" });

        await adminDb.collection('users').createIndex({ "tenantId": 1, "_id": 1 }, { name: "tenant_entity_idx" });
        await adminDb.collection('users').createIndex({ "tenantId": 1, "username": 1 }, { unique: true, name: "tenant_username_idx" });
        await adminDb.collection('users').createIndex({ "tenantId": 1, "email": 1 }, { unique: true, name: "tenant_email_idx" });

        console.log('✓ System Admin Domain: 2 collections with indexes');

        // 7. Create Global Business Database
        console.log('Creating Global Business Domain collections...');
        let bizDb = client.db('management_business');

        await bizDb.collection('country_data').createIndex({ "tenantId": 1, "_id": 1 }, { name: "tenant_entity_idx" });
        await bizDb.collection('country_data').createIndex({ "tenantId": 1, "countryCode": 1 }, { unique: true, name: "tenant_country_idx" });

        await bizDb.collection('reports').createIndex({ "tenantId": 1, "_id": 1 }, { name: "tenant_entity_idx" });
        await bizDb.collection('reports').createIndex({ "tenantId": 1, "reportType": 1, "generatedAt": -1 }, { name: "tenant_type_date_idx" });

        console.log('✓ Global Business Domain: 2 collections with indexes');

        // 8. Create Finance Database
        console.log('Creating Finance Domain collections...');
        let financeDb = client.db('management_finance');

        await financeDb.collection('invoices').createIndex({ "tenantId": 1, "_id": 1 }, { name: "tenant_entity_idx" });
        await financeDb.collection('invoices').createIndex({ "tenantId": 1, "invoiceNumber": 1 }, { unique: true, name: "tenant_invoice_idx" });
        await financeDb.collection('invoices').createIndex({ "tenantId": 1, "status": 1, "dueDate": 1 }, { name: "tenant_status_date_idx" });

        await financeDb.collection('transactions').createIndex({ "tenantId": 1, "_id": 1 }, { name: "tenant_entity_idx" });
        await financeDb.collection('transactions').createIndex({ "tenantId": 1, "transactionDate": -1 }, { name: "tenant_date_idx" });

        await financeDb.collection('budgets').createIndex({ "tenantId": 1, "_id": 1 }, { name: "tenant_entity_idx" });
        await financeDb.collection('budgets').createIndex({ "tenantId": 1, "fiscalYear": 1, "category": 1 }, { name: "tenant_year_category_idx" });

        console.log('✓ Finance Domain: 3 collections with indexes');

        // 9. Create Audit Database (Shared)
        console.log('Creating Audit Database collections...');
        let auditDb = client.db('management_audit');

        await auditDb.collection('audit_logs').createIndex({ "tenantId": 1, "_id": 1 }, { name: "tenant_entity_idx" });
        // TTL index must be on a single field
        await auditDb.collection('audit_logs').createIndex({ "createdAt": 1 }, { name: "created_at_ttl_idx", expireAfterSeconds: 31536000 }); // 1 year TTL

        console.log('✓ Audit Database: 1 collection with TTL index');

        console.log('');
        console.log('===========================================');
        console.log('✓ MongoDB Initialization Complete!');
        console.log('===========================================');
        console.log('');
        console.log('Databases created:');
        console.log('  - management_executive (3 collections)');
        console.log('  - management_hr (3 collections)');
        console.log('  - management_sales (3 collections)');
        console.log('  - management_marketing (2 collections)');
        console.log('  - management_support (2 collections)');
        console.log('  - management_admin (2 collections)');
        console.log('  - management_business (2 collections)');
        console.log('  - management_finance (3 collections)');
        console.log('  - management_audit (1 collection)');
        console.log('');
        console.log('Total: 9 databases, 21 collections');
        console.log('');
        console.log('All collections have tenant isolation indexes!');
        console.log('');
        console.log('Refresh MongoDB Compass to see the new databases.');
        console.log('===========================================');

    } catch (error) {
        console.error('Error initializing MongoDB:', error);
        throw error;
    } finally {
        await client.close();
        console.log('');
        console.log('Connection closed.');
    }
}

// Run initialization
initializeDatabases().catch(console.error);
