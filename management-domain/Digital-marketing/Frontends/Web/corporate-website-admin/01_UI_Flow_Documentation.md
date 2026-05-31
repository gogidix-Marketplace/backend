# GOGIDIX CORPORATE WEBSITE ADMIN - UI FLOW

**Version:** 1.0
**Domain:** Corporate Website
**Frontend:** corporate-website-admin
**Framework:** React 18 + Vite + TypeScript + Material UI v5.15+
**State Management:** Zustand
**Data Fetching:** TanStack Query
**Last Updated:** 2025-02-08

---

## OVERVIEW

Internal administrative dashboard for managing the Gogidix corporate website. Managed by Digital Marketing HQ with contributions from HR (job postings), Product (product catalog), Leadership (press releases), and other departments.

---

## NAVIGATION STRUCTURE

```
Admin Dashboard
├── Dashboard
├── Content Management
│   ├── Pages
│   ├── Blog
│   ├── Press Releases
│   └── Resources
├── Product Catalog
│   ├── Products
│   ├── Categories
│   ├── Features
│   ├── Pricing
│   └── Integrations
├── Developer Resources
│   ├── API Documentation
│   ├── SDKs
│   └── Code Examples
├── Careers
│   ├── Jobs
│   ├── Applications
│   └── Pipeline
├── Partners
│   ├── Programs
│   ├── Applications
│   └── Portal
├── Leads
│   ├── Demo Requests
│   ├── Sales Inquiries
│   └── Support Tickets
├── Analytics
│   ├── Site Analytics
│   ├── User Behavior
│   ├── Conversion Funnels
│   └── SEO Performance
├── Settings
│   ├── General
│   ├── Users & Permissions
│   ├── Workflows
│   └── Integrations
```

---

## AUTHENTICATION FLOW

```
┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐
│ Admin accesses  │────>│ Login Page      │────>│ Validate        │
│ Admin URL       │     │                │     │ Credentials     │
└─────────────────┘     └─────────────────┘     └─────────────────┘
                                                        │
                        ┌─────────────────────────────┴─────────────────────────────┐
                        ▜                                                           ▼
                 ┌──────────────┐                                         ┌──────────────┐
                 │ Valid        │                                         │ Invalid      │
                 │ Credentials │                                         │ Credentials  │
                 └──────┬───────┘                                         └──────┬───────┘
                        │                                                        │
                        ▼                                                        ▼
                 ┌──────────────┐                                         ┌──────────────┐
                 │ MFA Check    │                                         │ Show Error +  │
                 │ (if enabled)  │                                         │ Retry Option  │
                 └──────┬───────┘                                         └──────────────┘
                        │
                        ▼
                 ┌──────────────┐
                 │ Role Check    │
                 └──────┬───────┘
                        │
        ┌───────────────┼───────────────┐
        ▼               ▼               ▼
┌──────────────┐ ┌──────────────┐ ┌──────────────┐
│ Digital      │ │ Product      │ │ HR           │
│ Marketing    │ │ Team         │ │ Team         │
│ Full Access   │ │ Product      │ │ Careers only  │
│              │ │ Catalog only │ │              │
└──────┬───────┘ └──────┬───────┘ └──────┬───────┘
       │                │                │
       └────────────────┴────────────────┘
                        │
                        ▼
                 ┌──────────────┐
                 │ Admin        │
                 │ Dashboard    │
                 └──────────────┘
```

---

## USER ROLES & PERMISSIONS

### Role Definitions

| Role | Description | Department | Access Level |
|------|-------------|------------|--------------|
| **ADMIN** | Full system access | Digital Marketing HQ | Full |
| **CONTENT_EDITOR** | Content management | Digital Marketing | Content only |
| **PRODUCT_MANAGER** | Product catalog | Product Team | Products only |
| **HR_MANAGER** | Job postings & applications | HR | Careers only |
| **PR_MANAGER** | Press releases | Communications | Press only |
| **ANALYST** | Read-only analytics | Digital Marketing | Read-only |
| **DEVELOPER** | Dev resources | Engineering | Dev docs only |

### Permission Matrix

| Permission | ADMIN | CONTENT_EDITOR | PRODUCT_MANAGER | HR_MANAGER | PR_MANAGER | ANALYST | DEVELOPER |
|------------|-------|---------------|----------------|------------|------------|---------|-----------|
| View Dashboard | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ |
| Edit Pages | ✓ | ✓ | ✗ | ✗ | ✗ | ✗ | ✗ |
| Publish Content | ✓ | ✓ | ✗ | ✗ | ✗ | ✗ | ✗ |
| Manage Products | ✓ | ✗ | ✓ | ✗ | ✗ | ✗ | ✗ |
| Manage Pricing | ✓ | ✗ | ✓ | ✗ | ✗ | ✗ | ✗ |
| Manage Jobs | ✓ | ✗ | ✗ | ✓ | ✗ | ✗ | ✗ |
| Manage Applications | ✓ | ✗ | ✗ | ✓ | ✗ | ✗ | ✗ |
| Manage Press | ✓ | ✗ | ✗ | ✗ | ✓ | ✗ | ✗ |
| Manage API Docs | ✓ | ✗ | ✗ | ✗ | ✗ | ✗ | ✓ |
| View Analytics | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ | ✗ |
| Manage Users | ✓ | ✗ | ✗ | ✗ | ✗ | ✗ | ✗ |
| Manage Settings | ✓ | ✗ | ✗ | ✗ | ✗ | ✗ | ✗ |

---

## CONTENT MANAGEMENT FLOW

### Content Creation Workflow

```
┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐
│ User clicks     │────>│ Content Type   │────>│ Content Editor  │
│ "New Content"  │     │ Selection      │     │ Loaded         │
└─────────────────┘     └─────────────────┘     └─────────────────┘
                                                        │
                        ┌───────────────────────────────┼───────────────────────────────┐
                        ▜                               ▼                               ▼
                 ┌──────────────┐               ┌──────────────┐               ┌──────────────┐
                 │ Create Page  │               │ Create Blog   │               │ Create Press  │
                 │              │               │ Post         │               │ Release      │
                 └──────┬───────┘               └──────┬───────┘               └──────┬───────┘
                        │                              │                              │
                        ▼                              ▼                              ▼
                 ┌──────────────┐               ┌──────────────┐               ┌──────────────┐
                 │ Edit Content │               │ Edit Post    │               │ Edit Release │
                 └──────┬───────┘               └──────┬───────┘               └──────┬───────┘
                        │                              │                              │
                        ▼                              ▼                              ▼
                 ┌──────────────┐               ┌──────────────┐               ┌──────────────┐
                 │ Add Media    │               │ Set          │               │ Set          │
                 │ & Assets     │               │ Categories   │               │ Publish Date │
                 └──────┬───────┘               └──────┬───────┘               └──────┬───────┘
                        │                              │                              │
                        ▼                              ▼                              ▼
                 ┌──────────────┐               ┌──────────────┐               ┌──────────────┐
                 │ Set SEO      │               │ Set Featured  │               │ Add Contact  │
                 │ Metadata     │               │ Image        │               │ Info         │
                 └──────┬───────┘               └──────┬───────┘               └──────┬───────┘
                        │                              │                              │
                        ▼                              ▼                              ▼
                 ┌──────────────┐               ┌──────────────┐               ┌──────────────┐
                 │ Submit for   │               │ Save as      │               │ Schedule/    │
                 │ Review       │               │ Draft        │               │ Publish      │
                 └──────┬───────┘               └──────┬───────┘               └──────┬───────┘
                        │                              │                              │
                        ▼                              ▼                              ▼
                 ┌──────────────┐               ┌──────────────┐               ┌──────────────┐
                 │ Approval     │               │ Direct       │               │ Published to  │
                 │ Workflow     │               │ Publish (if  │               │ Website      │
                 │              │               │ authorized)  │               │              │
                 └──────────────┘               └──────────────┘               └──────────────┘
```

### Approval Workflow

```
┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐
│ Content        │────>│ Review Queue   │────>│ Reviewer       │
│ Submitted      │     │                │     │ Assigned       │
└─────────────────┘     └─────────────────┘     └─────────────────┘
                                                        │
                      ┌───────────────────────────────┴───────────────────────────────┐
                      ▜                                                               ▼
               ┌──────────────┐                                         ┌──────────────┐
               │ Approved     │                                         │ Rejected     │
               │              │                                         │              │
               └──────┬───────┘                                         └──────┬───────┘
                      │                                                        │
                      ▼                                                        ▼
               ┌──────────────┐                                         ┌──────────────┐
               │ Scheduled/   │                                         │ Returned to  │
               │ Published    │                                         │ Creator with │
               │              │                                         │ Feedback     │
               └──────────────┘                                         └──────────────┘
```

---

## PRODUCT CATALOG FLOW

### Product Management Flow

```
┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐
│ Product Manager │────>│ Products List  │────>│ Select Action   │
│ Accesses       │     │                │     │                │
│ Product Catalog│     │                │     │                │
└─────────────────┘     └─────────────────┘     └─────────────────┘
                                                        │
        ┌───────────────────────────────────────────────┴───────────────────────────────┐
        ▜                               ▼                               ▼                               ▼
┌──────────────┐               ┌──────────────┐               ┌──────────────┐               ┌──────────────┐
│ Add New      │               │ Edit         │               │ Update       │               │ Archive      │
│ Product      │               │ Existing     │               │ Pricing      │               │ Product      │
│              │               │ Product      │               │              │               │              │
└──────┬───────┘               └──────┬───────┘               └──────┬───────┘               └──────┬───────┘
       │                              │                              │                              │
       ▼                              ▼                              ▼                              ▼
┌──────────────┐               ┌──────────────┐               ┌──────────────┐               ┌──────────────┐
│ Enter Product│               │ Modify       │               │ Adjust Plans │               │ Confirm &    │
│ Details      │               │ Details      │               │ & Tiers      │               │ Reason       │
│              │               │              │               │              │               │              │
└──────┬───────┘               └──────┬───────┘               └──────┬───────┘               └──────┬───────┘
       │                              │                              │                              │
       ▼                              ▼                              ▼                              ▼
┌──────────────┐               ┌──────────────┐               ┌──────────────┐               ┌──────────────┐
│ Add Features │               │ Update       │               │ Publish      │               │ Product      │
│ & Benefits   │               │ Features     │               │ Changes      │               │ Hidden       │
│              │               │              │               │              │               │              │
└──────┬───────┘               └──────┬───────┘               └──────┬───────┘               └──────┬───────┘
       │                              │                              │                              │
       ▼                              ▼                              ▼                              ▼
┌──────────────┐               ┌──────────────┐               ┌──────────────┐               ┌──────────────┐
│ Upload       │               │ Update Media │               │ Notify       │               │ End         │
│ Screenshots  │               │ & Assets     │               │ Stakeholders │               │ Workflow     │
│              │               │              │               │              │               │              │
└──────┬───────┘               └──────┬───────┘               └──────┬───────┘               └──────┬───────┘
       │                              │                              │                              │
       └──────────────────────────────┴──────────────────────────────┴──────────────────────────────┘
                                                      │
                                                      ▼
                                               ┌──────────────┐
                                               │ Product Live │
                                               │ on Website   │
                                               └──────────────┘
```

---

## CAREERS MANAGEMENT FLOW

### HR Department Contribution Flow

```
┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐
│ HR Manager     │────>│ Careers Module  │────>│ Select Action   │
│ Logs In       │     │ Access         │     │                │
└─────────────────┘     └─────────────────┘     └─────────────────┘
                                                        │
        ┌───────────────────────────────────────────────┴───────────────────────────────┐
        ▜                               ▼                               ▼
┌──────────────┐               ┌──────────────┐               ┌──────────────┐
│ Create Job   │               │ Edit Job     │               │ Close/       │
│ Posting      │               │ Posting     │               │ Expire Job   │
│              │               │             │               │              │
└──────┬───────┘               └──────┬───────┘               └──────┬───────┘
       │                              │                              │
       ▼                              ▼                              ▼
┌──────────────┐               ┌──────────────┐               ┌──────────────┐
│ Fill Job     │               │ Update       │               │ Set Closed   │
│ Details      │               │ Details      │               │ Reason      │
│              │               │              │               │              │
└──────┬───────┘               └──────┬───────┘               └──────┬───────┘
       │                              │                              │
       ▼                              ▼                              ▼
┌──────────────┐               ┌──────────────┐               ┌──────────────┐
│ Set          │               │ Add/Edit     │               │ Job Removed  │
│ Requirements │               │ Requirements │               │ from Site    │
│              │               │              │               │              │
└──────┬───────┘               └──────┬───────┘               └──────┬───────┘
       │                              │                              │
       ▼                              ▼                              ▼
┌──────────────┐               ┌──────────────┐               ┌──────────────┐
│ Upload Job   │               │ Update       │               │ Applicants   │
│ Description  │               │ Benefits    │               │ Notified     │
│              │               │              │               │              │
└──────┬───────┘               └──────┬───────┘               └──────┬───────┘
       │                              │                              │
       ▼                              ▼                              ▼
┌──────────────┐               ┌──────────────┐               ┌──────────────┐
│ Submit for   │               │ Save Changes │               │ End          │
│ Publishing   │               │              │               │ Workflow     │
│              │               │              │               │              │
└──────┬───────┘               └──────┬───────┘               └──────┬───────┘
       │                              │                              │
       ▼                              ▼                              │
┌──────────────┐               ┌──────────────┐                      │
│ Digital      │               │ Job Updated  │                      │
│ Marketing    │               │ on Website   │                      │
│ Auto-Notified │               │              │                      │
│              │               │              │                      │
└──────────────┘               └──────────────┘                      │
                                                                │
       ┌────────────────────────────────────────────────────────┘
       │
       ▼
┌──────────────┐
│ Job Live on  │
│ Corporate    │
│ Website      │
└──────────────┘
```

---

## LEAD MANAGEMENT FLOW

### Lead Handling Flow

```
┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐
│ Website Form   │────>│ Lead Captured  │────>│ Lead Routing    │
│ Submitted      │     │ in System      │     │                │
└─────────────────┘     └─────────────────┘     └─────────────────┘
                                                        │
                      ┌───────────────────────────────┴───────────────────────────────┐
                      ▜                                                               ▼
               ┌──────────────┐                                         ┌──────────────┐
               │ Demo Request │                                         │ Sales/       │
               │              │                                         │ Partner      │
               │              │                                         │ Inquiry      │
               └──────┬───────┘                                         └──────┬───────┘
                      │                                                        │
                      ▼                                                        ▼
               ┌──────────────┐                                         ┌──────────────┐
               │ Auto-        │                                         │ Sales Team   │
               │ Response     │                                         │ Notified     │
               │ Sent         │                                         │              │
               └──────┬───────┘                                         └──────┬───────┘
                      │                                                        │
                      ▼                                                        ▼
               ┌──────────────┐                                         ┌──────────────┐
               │ Sales        │                                         │ Assigned to   │
               │ Team Notified│                                         │ Partner      │
               │              │                                         │ Manager      │
               └──────┬───────┘                                         └──────┬───────┘
                      │                                                        │
                      ▼                                                        ▼
               ┌──────────────┐                                         ┌──────────────┐
               │ Follow-up    │                                         │ Partner      │
               │ Scheduled    │                                         │ Contact Made  │
               └──────┬───────┘                                         └──────┬───────┘
                      │                                                        │
                      └───────────────────────────────┬───────────────────────────────┘
                                                        │
                                                        ▼
                                                 ┌──────────────┐
                                                 │ Lead Added to │
                                                 │ CRM for       │
                                                 │ Tracking      │
                                                 └──────────────┘
```

---

## ANALYTICS FLOW

### Analytics Dashboard Flow

```
┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐
│ User Accesses  │────>│ Analytics      │────>│ Select Report  │
│ Analytics      │     │ Dashboard      │     │ Type          │
└─────────────────┘     └─────────────────┘     └─────────────────┘
                                                        │
        ┌───────────────────────────────────────────────┴───────────────────────────────┐
        ▜                               ▼                               ▼
┌──────────────┐               ┌──────────────┐               ┌──────────────┐
│ Site         │               │ User         │               │ Conversion   │
│ Analytics    │               │ Behavior    │               │ Funnels      │
│              │               │             │               │              │
└──────┬───────┘               └──────┬───────┘               └──────┬───────┘
       │                              │                              │
       ▼                              ▼                              ▼
┌──────────────┐               ┌──────────────┐               ┌──────────────┐
│ View Page    │               │ View User    │               │ View Funnel   │
│ Views, Traffic│               │ Journeys,    │               │ Steps, Drop-  │
│ Sources      │               │ Flow        │               │ off          │
└──────┬───────┘               └──────┬───────┘               └──────┬───────┘
       │                              │                              │
       ▼                              ▼                              ▼
┌──────────────┐               ┌──────────────┐               ┌──────────────┐
│ Export       │               │ Segment     │               │ Identify     │
│ Report       │               │ Users       │               │ Bottlenecks  │
└──────────────┘               └──────────────┘               └──────────────┘
```

---

## VERSION HISTORY

| Version | Date | Changes |
|---------|------|---------|
| 1.0 | 2025-02-08 | Initial UI Flow Documentation |

---

**Document End**
