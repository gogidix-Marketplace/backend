# GOGIDIX CORPORATE WEBSITE ADMIN - PAGE BY PAGE

**Version:** 1.0
**Domain:** Corporate Website
**Frontend:** corporate-website-admin
**Last Updated:** 2025-02-08

---

## PAGE TREE

```
Admin Dashboard
├── /dashboard
├── /content
│   ├── /pages
│   │   ├── /list
│   │   ├── /create
│   │   └── /:id/edit
│   ├── /blog
│   │   ├── /list
│   │   ├── /create
│   │   └── /:id/edit
│   └── /press
│       ├── /list
│       ├── /create
│       └── /:id/edit
├── /products
│   ├── /list
│   ├── /create
│   ├── /:id/edit
│   ├── /categories
│   └── /integrations
├── /careers
│   ├── /jobs
│   │   ├── /list
│   │   ├── /create
│   │   └── /:id/edit
│   └── /applications
│       ├── /list
│       └── /:id/detail
├── /developers
│   ├── /api-docs
│   └── /sdks
├── /leads
│   ├── /demo-requests
│   ├── /sales-inquiries
│   └── /support-tickets
├── /partners
│   ├── /programs
│   └── /applications
├── /analytics
│   ├── /overview
│   ├── /traffic
│   ├── /content
│   ├── /conversions
│   └── /seo
└── /settings
    ├── /general
    ├── /users
    ├── /workflows
    └── /integrations
```

---

## DASHBOARD FLOW

### Main Dashboard Entry

```
┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐
│ Admin Logs In   │────>│ Auth Verified  │────>│ Role Check      │
│                 │     │                │     │ Completed      │
└─────────────────┘     └─────────────────┘     └─────────────────┘
                                                        │
                        ┌───────────────────────────────┴───────────────────────────────┐
                        ▜                                                               ▼
                 ┌──────────────┐                                         ┌──────────────┐
                 │ Full Access   │                                         │ Limited       │
                 │ (Digital Mktg)│                                         │ Access        │
                 └──────┬───────┘                                         │ (Product/HR)  │
                        │                                                 └──────┬───────┘
                        ▼                                                        │
                 ┌──────────────┐                                              ▼
                 │ Dashboard     │                                     ┌──────────────┐
                 │ Loads        │                                     │ Restricted   │
                 │              │                                     │ View         │
                 └──────────────┘                                     └──────────────┘
```

### Dashboard Widget Actions

| Widget | Click Action | Navigation Target |
|--------|-------------|-------------------|
| Pages Count | View all pages | /content/pages/list |
| Blog Posts | View all posts | /content/blog/list |
| Careers | View jobs | /careers/jobs/list |
| Products | View products | /products/list |
| Pending Actions | View pending items | Context-specific |
| Recent Activity | View full activity log | /settings/audit-log |

---

## CONTENT PAGES FLOW

### Page Creation Flow

```
┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐
│ User Clicks     │────>│ Content Type   │────>│ Page Editor    │
│ "New Page"      │     │ Selection      │     │ Loads          │
└─────────────────┘     └─────────────────┘     └─────────────────┘
                                                        │
                        ┌───────────────────────────────┴───────────────────────────────┐
                        ▜                                                               ▼
                 ┌──────────────┐                                         ┌──────────────┐
                 │ Edit Page     │                                         │ Edit Blog     │
                 │              │                                         │ Post         │
                 └──────┬───────┘                                         └──────┬───────┘
                        │                                                        │
                        ▼                                                        │
                 ┌──────────────┐                                                 │
                 │ Add Content   │                                                 │
                 │ Blocks        │                                                 │
                 └──────┬───────┘                                                 │
                        │                                                        │
                        ▼                                                        │
                 ┌──────────────┐                                                 │
                 │ Configure     │                                                 │
                 │ SEO Settings   │                                                 │
                 └──────┬───────┘                                                 │
                        │                                                        │
                        ▼                                                        ▼
                 ┌──────────────┐                                         ┌──────────────┐
                 │ Save as Draft  │                                         │ Set Publish  │
                 │ or Submit for │                                         │ Date         │
                 │ Review        │                                         │              │
                 └──────┬───────┘                                         └──────┬───────┘
                        │                                                        │
                        ▼                                                        ▼
                 ┌──────────────┐                                         ┌──────────────┐
                 │ In Review     │                                         │ Scheduled/    │
                 │ Queue        │                                         │ Published     │
                 └──────┬───────┘                                         └──────────────┘
                        │
                        ▼
                 ┌──────────────┐
                 │ Published to  │
                 │ Website      │
                 └──────────────┘
```

### Content Approval Flow

```
┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐
│ Content        │────>│ Review Queue   │────>│ Reviewer       │
│ Submitted      │     │                │     │ Assigned       │
└─────────────────┘     └─────────────────┘     └─────────────────┘
                                                        │
                      ┌─────────────────────────────┬─────────────────────────────┐
                      ▼                             ▼                             ▼
               ┌──────────────┐             ┌──────────────┐             ┌──────────────┐
               │ Quick View    │             │ Full Edit     │             │ Reject       │
               │ & Approve     │             │ Request      │             │ with Feedback│
               └──────┬───────┘             └──────┬───────┘             └──────┬───────┘
                      │                              │                              │
                      ▼                              ▼                              ▼
               ┌──────────────┐             ┌──────────────┐             ┌──────────────┐
               │ Auto-Notify   │             │ Changes      │             │ Notify       │
               │ Creator       │             │ Requested    │             │ Creator      │
               └──────┬───────┘             └──────┬───────┘             └──────┬───────┘
                      │                              │                              │
                      └──────────────────────────────┴──────────────────────────────┘
                                                      │
                                                      ▼
                                               ┌──────────────┐
                                               │ Content Live │
                                               └──────────────┘
```

---

## PRODUCT CATALOG FLOW

### Product Management Flow

```
┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐
│ Product Manager │────>│ Product List   │────>│ Select Action   │
│ Accesses       │     │                │     │                │
│ Products       │     │                │     │                │
└─────────────────┘     └─────────────────┘     └─────────────────┘
                                                        │
        ┌───────────────────────────────────────────────┴───────────────────────────────┐
        ▜                               ▼                               ▼                               ▼
┌──────────────┐               ┌──────────────┐               ┌──────────────┐               ┌──────────────┐
│ Add New      │               │ Edit         │               │ Update       │               │ Manage      │
│ Product      │               │ Existing     │               │ Pricing      │               │ Integrations │
│              │               │ Product      │               │              │               │              │
└──────┬───────┘               └──────┬───────┘               └──────┬───────┘               └──────┬───────┘
       │                              │                              │                              │
       ▼                              ▼                              ▼                              ▼
┌──────────────┐               ┌──────────────┐               ┌──────────────┐               ┌──────────────┐
│ Enter Basic  │               │ Modify       │               │ Adjust Plans  │               │ Add/Edit     │
│ Info         │               │ Details      │               │ & Tiers      │               │ Integration  │
│              │               │              │               │              │               │              │
└──────┬───────┘               └──────┬───────┘               └──────┬───────┘               └──────┬───────┘
       │                              │                              │                              │
       ▼                              ▼                              ▼                              ▼
┌──────────────┐               ┌──────────────┐               ┌──────────────┐               ┌──────────────┐
│ Add Features │               │ Update Media │               │ Publish      │               │ Save        │
│ & Benefits   │               │ & Assets     │               │ Changes      │               │ Integration  │
└──────┬───────┘               └──────┬───────┘               └──────┬───────┘               └──────┬───────┘
       │                              │                              │                              │
       ▼                              ▼                              ▼                              ▼
┌──────────────┐               ┌──────────────┐               ┌──────────────┐               ┌──────────────┐
│ Configure    │               │ Update SEO   │               │ Notify       │               │ Integration  │
│ Display      │               │ Metadata     │               │ Stakeholders │               │ Live        │
│ Settings     │               │              │               │              │               │              │
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

### Product Categories Management

```
┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐
│ User Clicks    │────>│ Categories     │────>│ Category List  │
│ Categories     │     │                │     │                │
└─────────────────┘     └─────────────────┘     └─────────────────┘
                                                        │
                      ┌───────────────────────────────┴───────────────────────────────┐
                      ▜                                                               ▼
               ┌──────────────┐                                         ┌──────────────┐
               │ Reorder       │                                         │ Edit Category│
               │ Categories    │                                         │ Details      │
               └──────┬───────┘                                         └──────┬───────┘
                      │                                                        │
                      ▼                                                        ▼
               ┌──────────────┐                                         ┌──────────────┐
               │ Save Order   │                                         │ Update Name, │
               └──────┬───────┘                                         │ Icon,        │
                      │                                                │ Description  │
                      ▼                                                └──────┬───────┘
               ┌──────────────┐                                                 │
               │ Category     │                                                 ▼
               │ Structure    │                                         ┌──────────────┐
               │ Updated     │                                         │ Changes Live │
               └──────────────┘                                         └──────────────┘
```

---

## CAREERS FLOW

### HR Department Job Posting Flow

```
┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐
│ HR Manager     │────>│ Careers Module  │────>│ Create Job     │
│ Logs In       │     │                │     │ Posting        │
└─────────────────┘     └─────────────────┘     └─────────────────┘
                                                        │
                        ┌─────────────────────────────────────────────────────────────────────┐
                        ▜
                 ┌──────────────┐
                 │ Enter Job     │
                 │ Details       │
                 └──────┬───────┘
                        │
                        ▼
                 ┌──────────────┐
                 │ Set Job       │
                 │ Requirements  │
                 │ (Must Have/   │
                 │  Nice to Have)│
                 └──────┬───────┘
                        │
                        ▼
                 ┌──────────────┐
                 │ Add Benefits  │
                 │ & Salary      │
                 │ Range         │
                 └──────┬───────┘
                        │
                        ▼
                 ┌──────────────┐
                 │ Set Publish   │
                 │ Date /        │
                 │ Expiry Date   │
                 └──────┬───────┘
                        │
                        ▼
                 ┌──────────────┐
                 │ Submit for    │
                 │ Publishing    │
                 └──────┬───────┘
                        │
                        ▼
                 ┌──────────────┐
                 │ Digital       │
                 │ Marketing     │
                 │ Notified      │
                 │ for Review    │
                 └──────┬───────┘
                        │
                        ▼
                 ┌──────────────┐
                 │ Job Goes Live │
                 │ on Website    │
                 └──────────────┘
```

### Application Management Flow

```
┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐
│ New Application│────>│ Application    │────>│ HR Team Views  │
│ Arrives       │     │ Dashboard      │     │ Application     │
└─────────────────┘     └─────────────────┘     └─────────────────┘
                                                        │
                      ┌───────────────────────────────┴───────────────────────────────┐
                      ▜                                                               ▼
               ┌──────────────┐                                         ┌──────────────┐
               │ Quick Screen  │                                         │ Full Review   │
               └──────┬───────┘                                         └──────┬───────┘
                      │                                                      │
                      ▼                                                      │
               ┌──────────────┐                                           ▼
               │ Update       │                                   ┌──────────────┐
               │ Status       │                                   │ Move to Next  │
               └──────┬───────┘                                   │ Stage        │
                      │                                           └──────┬───────┘
                      ▼                                                      │
               ┌──────────────┐                                               │
               │ Notify       │                                               ▼
               │ Candidate    │                                   ┌──────────────┐
               └──────────────┘                                   │ Schedule     │
                                                                  │ Interview    │
                                                                  └──────┬───────┘
                                                                       │
                                                                       ▼
                                                               ┌──────────────┐
                                                               │ Interview     │
                                                               │ Process      │
                                                               └──────┬───────┘
                                                                       │
                                                                       ▼
                                                               ┌──────────────┐
                                                               │ Decision &    │
                                                               │ Notify       │
                                                               └──────────────┘
```

---

## LEADS FLOW

### Lead Management Flow

```
┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐
│ Website Form   │────>│ Lead Captured  │────>│ Lead Assigned   │
│ Submitted      │     │ in System      │     │ to Sales/      │
└─────────────────┘     └─────────────────┘     │ Partner Mgr   │
                                         └─────────────────┘
                                                        │
                      ┌───────────────────────────────┴───────────────────────────────┐
                      ▜                                                               ▼
               ┌──────────────┐                                         ┌──────────────┐
               │ Auto-Response │                                         │ Manual        │
               │ Sent          │                                         │ Assignment    │
               └──────┬───────┘                                         └──────┬───────┘
                      │                                                      │
                      ▼                                                      ▼
               ┌──────────────┐                                         ┌──────────────┐
               │ Follow-up     │                                         │ Sales/        │
               │ Scheduled    │                                         │ Partner       │
               └──────┬───────┘                                         │ Contact Made  │
                      │                                                      └──────┬───────┘
                      ▼                                                             │
               ┌──────────────┐                                               ▼
               │ Regular       │                                     ┌──────────────┐
               │ Touch Points  │                                     │ Track in     │
               └──────┬───────┘                                     │ CRM          │
                      │                                              └──────┬───────┘
                      ▼                                                     │
               ┌──────────────┐                                               │
               │ Demo/Meeting │                                               │
               │ Completed    │                                               │
               └──────┬───────┘                                               │
                      │                                                      │
                      ▼                                                      ▼
               ┌──────────────┐                                         ┌──────────────┐
               │ Proposal Sent │                                         │ Converted to  │
               └──────┬───────┘                                         │ Customer     │
                      │                                                      └──────────────┘
                      ▼
               ┌──────────────┐
               │ Opportunity   │
               │ Won/Lost      │
               └──────────────┘
```

### Lead Status Flow

```
┌──────────────┐     ┌──────────────┐     ┌──────────────┐     ┌──────────────┐
│     NEW      │────>│  CONTACTED   │────>│  QUALIFIED   │────>│  PROPOSAL    │
│              │     │              │     │              │     │              │
└──────────────┘     └──────────────┘     └──────────────┘     └──────────────┘
                           │                     │                     │
                           ▼                     ▼                     ▼
                    ┌──────────────┐     ┌──────────────┐     ┌──────────────┐
                    │ DEMO         │     │ NEGOTIATION  │     │ WON          │
                    │ SCHEDULED    │     │              │     │              │
                    └──────────────┘     └──────────────┘     └──────────────┘
                           │                     │                     │
                           └─────────────────────┴─────────────────────┘
                                                 │
                                                 ▼
                                          ┌──────────────┐
                                          │ LOST         │
                                          │              │
                                          └──────────────┘
```

---

## ANALYTICS FLOW

### Analytics Dashboard Flow

```
┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐
│ User Accesses   │────>│ Analytics      │────>│ Select Report  │
│ Analytics       │     │ Dashboard      │     │ Period        │
└─────────────────┘     └─────────────────┘     └─────────────────┘
                                                        │
                        ┌───────────────────────────────┴───────────────────────────────┐
                        ▜                                                               ▼
                 ┌──────────────┐                                         ┌──────────────┐
                 │ Site Traffic   │                                         │ Conversion    │
                 │ Report        │                                         │ Funnels      │
                 └──────┬───────┘                                         └──────┬───────┘
                        │                                                      │
                        ▼                                                      ▼
                 ┌──────────────┐                                         ┌──────────────┐
                 │ View Visitors │                                         │ View Funnel   │
                 │ Sources,      │                                         │ Steps, Drop-  │
                 │ Pages,        │                                         │ off Metrics  │
                 │ Demographics  │                                         │              │
                 └──────┬───────┘                                         └──────┬───────┘
                        │                                                      │
                        ▼                                                      ▼
                 ┌──────────────┐                                         ┌──────────────┐
                 │ Export Data   │                                         │ Identify     │
                 │ for Reports   │                                         │ Bottlenecks  │
                 └──────┬───────┘                                         └──────┬───────┘
                        │                                                      │
                        └──────────────────────────────┬──────────────────────────────┘
                                                      │
                                                      ▼
                                               ┌──────────────┐
                                               │ Data Used for │
                                               │ Decision     │
                                               │ Making      │
                                               └──────────────┘
```

---

## SETTINGS FLOW

### User Management Flow

```
┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐
│ Admin Accesses │────>│ Users &        │────>│ Select Action  │
│ Settings       │     │ Permissions    │     │                │
└─────────────────┘     └─────────────────┘     └─────────────────┘
                                                        │
                      ┌───────────────────────────────┴───────────────────────────────┐
                      ▜                                                               ▼
               ┌──────────────┐                                         ┌──────────────┐
               │ Add New User  │                                         │ Edit Existing │
               └──────┬───────┘                                         │ User         │
                      │                                                  └──────┬───────┘
                      ▼                                                         │
               ┌──────────────┐                                                ▼
               │ Enter User    │                                    ┌──────────────┐
               │ Details       │                                    │ Change Role  │
               │              │                                    │ or Remove    │
               └──────┬───────┘                                    └──────────────┘
                      │
                      ▼
               ┌──────────────┐
               │ Assign Role   │
               │ & Permissions│
               └──────┬───────┘
                      │
                      ▼
               ┌──────────────┐
               │ User Created/ │
               │ Updated      │
               └──────────────┘
```

### Workflow Configuration Flow

```
┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐
│ Admin Accesses │────>│ Workflow       │────>│ Select Content │
│ Settings       │     │ Settings       │     │ Type          │
└─────────────────┘     └─────────────────┘     └─────────────────┘
                                                        │
                      ┌───────────────────────────────┴───────────────────────────────┐
                      ▜                                                               ▼
               ┌──────────────┐                                         ┌──────────────┐
               │ Create/Modify │                                         │ Configure    │
               │ Approval      │                                         │ Notification │
               │ Workflow     │                                         │ Settings     │
               └──────┬───────┘                                         └──────┬───────┘
                      │                                                      │
                      ▼                                                      ▼
               ┌──────────────┐                                         ┌──────────────┐
               │ Set Approvers │                                         │ Enable/      │
               │ (by Role)    │                                         │ Disable Auto │
               └──────┬───────┘                                         │ Publish      │
                      │                                                      └──────┬───────┘
                      ▼                                                             │
               ┌──────────────┐                                                ▼
               │ Configure    │                                    ┌──────────────┐
               │ Stages       │                                    │ Workflow     │
               │ & Rules      │                                    │ Active       │
               └──────┬───────┘                                    └──────────────┘
                      │
                      ▼
               ┌──────────────┐
               │ Workflow     │
               │ Saved       │
               └──────────────┘
```

---

## PERMISSION MATRIX

### Department Access Matrix

| Page/Section | Digital Marketing | Product Team | HR | PR | Leadership |
|--------------|-------------------|--------------|-----|----|------------|
| Dashboard | ✓ Full | ✓ Limited | ✓ Limited | ✓ Limited | ✓ View Only |
| Content - Pages | ✓ Full | ✗ | ✗ | ✓ Full | ✓ View |
| Content - Blog | ✓ Full | ✗ | ✗ | ✗ | ✓ View |
| Content - Press | ✓ Full | ✗ | ✗ | ✓ Full | ✓ View |
| Products | ✓ View | ✓ Full | ✗ | ✗ | ✓ View |
| Product Pricing | ✓ Full | ✓ Full | ✗ | ✗ | ✓ Approve |
| Careers - Jobs | ✓ View | ✗ | ✓ Full | ✗ | ✓ View |
| Careers - Applications | ✓ View | ✗ | ✓ Full | ✗ | ✓ View |
| Developer Resources | ✓ View | ✓ View | ✗ | ✗ | ✓ View |
| Leads | ✓ Full | ✓ View | ✗ | ✓ View | ✓ View |
| Partners | ✓ Full | ✗ | ✗ | ✓ Full | ✓ View |
| Analytics | ✓ Full | ✓ View | ✓ View | ✓ View | ✓ View |
| Settings - General | ✓ Full | ✗ | ✗ | ✗ | ✗ |
| Settings - Users | ✓ Full | ✗ | ✗ | ✗ | ✗ |

---

## VERSION HISTORY

| Version | Date | Changes |
|---------|------|---------|
| 1.0 | 2025-02-08 | Initial Page By Page Documentation |

---

**Document End**
