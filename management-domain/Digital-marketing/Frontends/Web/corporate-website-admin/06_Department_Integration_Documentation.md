# GOGIDIX CORPORATE WEBSITE ADMIN - DEPARTMENT INTEGRATION

**Version:** 1.0
**Domain:** Corporate Website
**Frontend:** corporate-website-admin
**Last Updated:** 2025-02-08

---

## TABLE OF CONTENTS

1. [Department Integration Overview](#department-integration-overview)
2. [HR Department Integration](#hr-department-integration)
3. [Product Team Integration](#product-team-integration)
4. [PR/Leadership Integration](#prleadership-integration)
5. [Integration APIs](#integration-apis)

---

## 1. DEPARTMENT INTEGRATION OVERVIEW

### Integration Architecture

```
┌────────────────────────────────────────────────────────────────────────────────────────────────┐
│                    CORPORATE WEBSITE DEPARTMENT INTEGRATION                                    │
├────────────────────────────────────────────────────────────────────────────────────────────────┤
│                                                                                                │
│  The corporate website allows multiple departments to contribute content while maintaining                 │
│  centralized control by Digital Marketing HQ.                                                      │
│                                                                                                │
│  ┌────────────────────────────────────────────────────────────────────────────────────────┐  │
│  │                         DEPARTMENT CONTRIBUTIONS                                            │  │
│  ├────────────────────────────────────────────────────────────────────────────────────────┤  │
│  │                                                                                         │  │
│  │  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐               │  │
│  │  │ HR           │  │ Product      │  │ PR/          │  │ Leadership   │               │  │
│  │  │ Department   │  │ Team         │  │ Communications│  │ Team         │               │  │
│  │  │              │  │              │  │              │  │              │               │  │
│  │  │ Job Postings │  │ Product      │  │ Press        │  │ Company      │               │  │
│  │  │ Application  │  │ Catalog      │  │ Releases    │  │ Statements   │               │  │
│  │  │ Management   │  │ Updates      │  │             │  │             │               │  │
│  │  └──────────────┘  └──────────────┘  └──────────────┘  └──────────────┘               │  │
│  │                                                                                         │  │
│  │                                   ▼                                           │  │
│  │  ┌────────────────────────────────────────────────────────────────────────────────┐  │
│  │  │                  DIGITAL MARKETING HQ - APPROVAL & PUBLISHING                       │  │
│  │  └────────────────────────────────────────────────────────────────────────────────┘  │
│  │                                                                                         │  │
│  └────────────────────────────────────────────────────────────────────────────────────────┘  │
│                                                                                                │
└────────────────────────────────────────────────────────────────────────────────────────────────┘
```

### Department Permissions Matrix

| Department | Contributes | Approval Required | Auto-Publish | Scope |
|-------------|-------------|------------------|--------------|-------|
| **HR** | Job postings, applications | No (for jobs) | Yes | Careers only |
| **Product** | Product catalog, pricing | Yes | No | Products only |
| **PR/Communications** | Press releases, announcements | Yes | No | Press only |
| **Leadership** | Company statements, vision | Yes | No | Company pages |
| **Digital Marketing** | All content | N/A | Yes | Full access |

---

## 2. HR DEPARTMENT INTEGRATION

### HR Workflow

```
┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐
│ HR Manager     │────>│ Logs into      │────>│ Accesses       │
│ Logs In        │     │ Admin         │     │ Careers Module │
│                │     │ Dashboard     │     │               │
└─────────────────┘     └─────────────────┘     └─────────────────┘
                                                        │
                        ┌───────────────────────────────┴───────────────────────────────┐
                        ▜                                                               ▼
                 ┌──────────────┐                                         ┌──────────────┐
                 │ Manage Job    │                                         │ Review       │
                 │ Postings     │                                         │ Applications │
                 └──────┬───────┘                                         └──────┬───────┘
                        │                                                      │
                        ▼                                                      ▼
                 ┌──────────────┐                                         ┌──────────────┐
                 │ Create/Edit   │                                         │ View         │
                 │ Job Details   │                                         │ Candidate    │
                 └──────┬───────┘                                         │ Pipeline     │
                        │                                                      │
                        ▼                                                      ▼
                 ┌──────────────┐                                         ┌──────────────┐
                 │ Add          │                                         │ Update       │
                 │ Requirements │                                         │ Status &     │
                 │ & Benefits   │                                         │ Provide      │
                 └──────┬───────┘                                         │ Feedback     │
                        │                                                      └──────┬───────┘
                        ▼                                                             │
                 ┌──────────────┐                                                    ▼
                 │ Submit to     │                                    ┌──────────────┐
                 │ Website      │                                    │ Changes Synced│
                 └──────────────┘                                    │ with HR DB   │
                        │                                                    └──────────────┘
                        ▼
                 ┌──────────────┐
                 │ Job Live on   │
                 │ Corporate    │
                 │ Website      │
                 └──────────────┘
```

### HR Integration Features

```
┌────────────────────────────────────────────────────────────────────────────────────────────────┐
│  HR CAREERS MODULE                                                                            │
├────────────────────────────────────────────────────────────────────────────────────────────────┤
│                                                                                                │
│  ┌────────────────────────────────────────────────────────────────────────────────────────┐  │
│  │  JOB POSTING MANAGEMENT                                                                  │  │
│  ├────────────────────────────────────────────────────────────────────────────────────────┤  │
│  │  • Create and edit job postings                                                         │  │
│  │  • Set job requirements (must-have/nice-to-have)                                         │  │
│  │  • Add benefits and compensation info                                                    │  │
│  │  • Set employment type, level, location                                                    │  │
│  │  • Schedule publish/unpublish dates                                                        │  │
│  │  • Clone existing postings for similar roles                                             │  │
│  │  • Bulk update multiple postings                                                          │  │
│  └────────────────────────────────────────────────────────────────────────────────────────┘  │
│                                                                                                │
│  ┌────────────────────────────────────────────────────────────────────────────────────────┐  │
│  │  APPLICATION MANAGEMENT                                                                 │  │
│  ├────────────────────────────────────────────────────────────────────────────────────────┤  │
│  │  • View all applications with filtering                                                     │  │
│  │  • Review resumes and cover letters                                                       │  │
│  │  • Add notes and ratings to candidates                                                    │  │
│  │  • Update application status and stage                                                     │  │
│  │  • Schedule and manage interviews                                                         │  │
│  │  • Send email notifications to candidates                                                │  │
│  │  • Export applications for ATS integration                                               │  │
│  └────────────────────────────────────────────────────────────────────────────────────────┘  │
│                                                                                                │
│  ┌────────────────────────────────────────────────────────────────────────────────────────┐  │
│  │  NOTIFICATIONS                                                                            │  │
│  ├────────────────────────────────────────────────────────────────────────────────────────┤  │
│  │  • Email alerts for new applications                                                     │  │
│  │  • Daily digest of application activity                                                  │  │
│  │  • In-app notifications for urgent items                                                    │  │
│  │  • Calendar reminders for scheduled reviews                                                │  │
│  └────────────────────────────────────────────────────────────────────────────────────────┘  │
│                                                                                                │
│  [Manage Jobs] [View Applications] [Settings]                                                     │
│                                                                                                │
└────────────────────────────────────────────────────────────────────────────────────────────────┘
```

### HR Data Synchronization

```typescript
// HR Sync Configuration
interface HRSyncConfig {
  enabled: boolean;
  syncDirection: 'bidirectional' | 'website-to-hr' | 'hr-to-website';
  syncFields: {
    jobPostings: boolean;
    applications: boolean;
    candidateStatus: boolean;
  };
  syncSchedule: {
    frequency: 'realtime' | 'hourly' | 'daily';
    batchSize: number;
  };
  endpoints: {
    hrSystem: string;
    atsIntegration?: string;
  };
}

// Sync Events
interface HRSyncEvent {
  eventType: 'job.created' | 'job.updated' | 'job.closed' | 'application.received';
  source: 'hr_system' | 'website_admin';
  timestamp: Date;
  data: {
    jobId?: string;
    applicationId?: string;
    syncStatus: 'pending' | 'success' | 'failed';
    errorMessage?: string;
  };
}
```

---

## 3. PRODUCT TEAM INTEGRATION

### Product Team Workflow

```
┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐
│ Product        │────>│ Logs into      │────>│ Accesses       │
│ Manager        │     │ Admin         │     │ Products       │
│ Logs In        │     │ Dashboard     │     │ Module        │
└─────────────────┘     └─────────────────┘     └─────────────────┘
                                                        │
                        ┌───────────────────────────────┴───────────────────────────────┐
                        ▜                                                               ▼
                 ┌──────────────┐                                         ┌──────────────┐
                 │ Manage        │                                         │ View Product │
                 │ Product       │                                         │ Performance │
                 │ Catalog      │                                         │ Analytics   │
                 └──────┬───────┘                                         └──────┬───────┘
                        │                                                      │
                        ▼                                                      ▼
                 ┌──────────────┐                                         ┌──────────────┐
                 │ Create/Edit    │                                         │ Lead        │
                 │ Products      │                                         │ Attribution  │
                 └──────┬───────┘                                         └──────┬───────┘
                        │                                                      │
                        ▼                                                      ▼
                 ┌──────────────┐                                         ┌──────────────┐
                 │ Update        │                                         │ Review      │
                 │ Features &    │                                         │ Product     │
                 │ Benefits      │                                         │ Inquiries   │
                 └──────┬───────┘                                         └──────┬───────┘
                        │                                                      │
                        ▼                                                      ▼
                 ┌──────────────┐                                         ┌──────────────┐
                 │ Configure    │                                         │ Connect     │
                 │ Pricing      │                                         │ Leads to    │
                 └──────┬───────┘                                         │ CRM         │
                        │                                                      └──────────────┘
                        ▼
                 ┌──────────────┐
                 │ Submit for    │
                 │ Publishing   │
                 └──────┬───────┘
                        │
                        ▼
                 ┌──────────────┐
                 │ Product Live │
                 │ on Website  │
                 └──────────────┘
```

### Product Integration Features

```
┌────────────────────────────────────────────────────────────────────────────────────────────────┐
│  PRODUCT CATALOG MODULE                                                                       │
├────────────────────────────────────────────────────────────────────────────────────────────────┤
│                                                                                                │
│  ┌────────────────────────────────────────────────────────────────────────────────────────┐  │
│  │  PRODUCT MANAGEMENT                                                                      │  │
│  ├────────────────────────────────────────────────────────────────────────────────────────┤  │
│  │  • Create and manage product listings                                                   │  │
│  │  • Define product categories and hierarchy                                               │  │
│  │  • Add features, benefits, and use cases                                                 │  │
│  │  • Upload product screenshots and videos                                                    │  │
│  │  • Configure pricing plans and tiers                                                        │  │
│  │  • Set product status (active, beta, coming-soon)                                           │  │
│  │  • Manage product availability by region                                                    │  │
│  └────────────────────────────────────────────────────────────────────────────────────────┘  │
│                                                                                                │
│  ┌────────────────────────────────────────────────────────────────────────────────────────┐  │
│  │  INTEGRATION MANAGEMENT                                                                  │  │
│  ├────────────────────────────────────────────────────────────────────────────────────────┤  │
│  │  • Add third-party integrations (Shopify, WooCommerce, etc.)                                 │  │
│  │  • Configure integration display and badges                                              │  │
│  │  • Set certification levels                                                              │  │
│  │  • Manage integration documentation                                                       │  │
│  │  • Track integration performance                                                          │  │
│  └────────────────────────────────────────────────────────────────────────────────────────┘  │
│                                                                                                │
│  ┌────────────────────────────────────────────────────────────────────────────────────────┐  │
│  │  LEAR INTEGRATION                                                                        │  │
│  ├────────────────────────────────────────────────────────────────────────────────────────┤  │
│  │  • View product-related demo requests                                                  │  │
│  │  • Connect demo requests to sales team                                                    │  │
│  │  • Track lead attribution from product pages                                             │  │
│  │  • Export lead data for CRM sync                                                        │  │
│  └────────────────────────────────────────────────────────────────────────────────────────┘  │
│                                                                                                │
│  [Manage Products] [Manage Integrations] [View Leads]                                               │
│                                                                                                │
└────────────────────────────────────────────────────────────────────────────────────────────────┘
```

---

## 4. PR/LEADERSHIP INTEGRATION

### Press Release Workflow

```
┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐
│ PR Manager     │────>│ Logs into      │────>│ Accesses       │
│ or Exec       │     │ Admin         │     │ Press Module   │
│ Logs In        │     │ Dashboard     │     │               │
└─────────────────┘     └─────────────────┘     └─────────────────┘
                                                        │
                        ┌───────────────────────────────┴───────────────────────────────┐
                        ▜                                                               ▼
                 ┌──────────────┐                                         ┌──────────────┐
                 │ Create/Edit   │                                         │ Review       │
                 │ Press        │                                         │ Submission  │
                 │ Release     │                                         │ Status      │
                 └──────┬───────┘                                         └──────┬───────┘
                        │                                                      │
                        ▼                                                      ▼
                 ┌──────────────┐                                         ┌──────────────┐
                 │ Add Press    │                                         │ Update       │
                 │ Contacts     │                                         │ Status      │
                 │ & Attachments│                                         │ & Provide   │
                 └──────┬───────┘                                         │ Feedback    │
                        │                                                      └──────┬───────┘
                        ▼                                                             │
                 ┌──────────────┐                                                    ▼
                 │ Set Publish  │                                    ┌──────────────┐
                 │ Date/Time   │                                    │ Notify       │
                 └──────┬───────┘                                    │ Stakeholders │
                        │                                             │ (C-Suite)    │
                        ▼                                             └──────────────┘
                 ┌──────────────┐
                 │ Submit for   │
                 │ Distribution │
                 └──────┬───────┘
                        │
                        ▼
                 ┌──────────────┐
                 │ Press Live   │
                 │ on Website  │
                 └──────┬───────┘
                        │
                        ▼
                 ┌──────────────┐
                 │ Wire        │
                 │ Service     │
                 │ Notified   │
                 └──────────────┘
```

### Leadership Statement Workflow

```
┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐
│ Executive      │────>│ Submits       │────>│ Digital       │
│ Provides       │     │ Statement    │     │ Marketing    │
│ Statement      │     │ via Email/    │     │ Team         │
│                │     │ Portal       │     │              │
└─────────────────┘     └─────────────────┘     └─────────────────┘
                                                        │
                        ┌───────────────────────────────┴───────────────────────────────┐
                        ▜                                                               ▼
                 ┌──────────────┐                                         ┌──────────────┐
                 │ Executive    │                                         │ Senior       │
                 │ Level       │                                         │ Leader      │
                 │ Statement   │                                         │ Statement   │
                 └──────┬───────┘                                         └──────┬───────┘
                        │                                                      │
                        ▼                                                      ▼
                 ┌──────────────┐                                         ┌──────────────┐
                 │ Review &     │                                         │ Review &     │
                 │ Refine       │                                         │ Translate   │
                 └──────┬───────┘                                         └──────┬───────┘
                        │                                                      │
                        └──────────────────────────────────────────────────────────────────┘
                                                      │
                                                      ▼
                                               ┌──────────────┐
                                               │ Add to       │
                                               │ Appropriate  │
                                               │ Page        │
                                               └──────┬───────┘
                                                      │
                                                      ▼
                                               ┌──────────────┐
                                               │ Publish to  │
                                               │ Website    │
                                               └──────────────┘
```

---

## 5. INTEGRATION APIS

### Department Integration Endpoints

```typescript
// HR Integration
interface HREndpoints {
  // Jobs sync
  'GET /api/hr/jobs': PaginatedResponse<JobPosting>;
  'POST /api/hr/jobs': JobPosting;
  'PUT /api/hr/jobs/:id': JobPosting;
  'POST /api/hr/jobs/:id/publish': JobPosting;

  // Applications sync
  'GET /api/hr/applications': PaginatedResponse<JobApplication>;
  'PUT /api/hr/applications/:id/status': JobApplication;

  // Notifications
  'POST /api/hr/notify/new-application': { applicationId: string };
  'GET /api/hr/stats/application-count': { count: number };
}

// Product Team Integration
interface ProductEndpoints {
  // Product catalog
  'GET /api/products': Product[];
  'POST /api/products': Product;
  'PUT /api/products/:id': Product;
  'POST /api/products/:id/publish': Product;

  // Integrations
  'GET /api/products/:id/integrations': ProductIntegration[];
  'POST /api/products/:id/integrations': ProductIntegration;

  // Leads
  'GET /api/products/leads': DemoRequest[];
  'POST /api/products/leads/assign': { leadId: string; assignedTo: string };
}

// PR/Leadership Integration
interface PREndpoints {
  // Press releases
  'GET /api/press': PressRelease[];
  'POST /api/press': PressRelease;
  'PUT /api/press/:id': PressRelease;
  'POST /api/press/:id/publish': PressRelease;

  // Statements
  'POST /api/statements/submit': Statement;
  'GET /api/statements/pending': Statement[];
  'PUT /api/statements/:id/approve': Statement;
}
```

### Webhook Events (Departments → Admin)

```typescript
// HR System → Admin
interface HRWebhookEvents {
  // Job sync events
  'hr.job.created': {
    jobId: string;
    title: string;
    department: string;
    location: string;
    createdAt: Date;
  };
  'hr.job.updated': {
    jobId: string;
    changes: Record<string, any>;
    updatedAt: Date;
  };
  'hr.job.closed': {
    jobId: string;
    reason: string;
    closedAt: Date;
  };
  'hr.application.received': {
    applicationId: string;
    jobId: string;
    applicant: ApplicantInfo;
    receivedAt: Date;
  };
}

// CRM/ATS → Admin (Lead Sync)
interface LeadSyncEvents {
  'lead.created': {
    leadId: string;
    type: 'demo' | 'sales' | 'partner';
    source: 'product_page' | 'other';
    data: LeadData;
  };
  'lead.updated': {
    leadId: string;
    status: LeadStatus;
    stage?: string;
  };
}
```

### Webhook Events (Admin → Departments)

```typescript
// Admin → HR System
interface AdminToHREvents {
  'admin.job.published': {
    jobId: string;
    publishedAt: Date;
    expiresAt: Date;
  };
  'admin.application.updated': {
    applicationId: string;
    status: ApplicationStatus;
    stage: ApplicationStage;
    note?: string;
  };
}

// Admin → CRM
interface AdminToCRMEvents {
  'admin.lead.qualified': {
    leadId: string;
    leadData: LeadData;
    qualifiedBy: string;
  };
  'admin.assigned': {
    leadId: string;
    assignedTo: string;
  };
}
```

---

## VERSION HISTORY

| Version | Date | Changes |
|---------|------|---------|
| 1.0 | 2025-02-08 | Initial Department Integration Documentation |

---

**Document End**
