# COUNTRY SALES DASHBOARD - UI FLOW DOCUMENTATION

**Version:** 1.1
**Domain:** Business Domain
**Subdomain:** Country-Sales-Dashboard
**Frontend:** country-sales-web-dashboard
**Framework:** React 18 + Vite + TypeScript + Material UI v5.15+
**State Management:** Zustand
**Data Fetching:** TanStack Query
**Last Updated:** 2025-02-16

---

## TABLE OF CONTENTS

1. [Dashboard Overview](#1-dashboard-overview)
2. [User Roles & Permissions](#2-user-roles--permissions)
3. [Navigation Architecture](#3-navigation-architecture)
4. [UI Flow Diagrams](#4-ui-flow-diagrams)
5. [Module-Specific Flows](#5-module-specific-flows)
6. [Sales Team Partners Module](#6-sales-team-partners-module)
7. [Data Flow Architecture](#7-data-flow-architecture)
8. [Error Handling & States](#8-error-handling--states)

---

## 1. DASHBOARD OVERVIEW

### 1.1 Purpose

The Country Sales Dashboard provides sales operations management for a **single assigned country**. Each sales manager is assigned to exactly ONE country and manages all sales activities, sales team performance, customer relationships, and revenue targets within that country.

### 1.2 Scope

**Managed at Country Level:**
- Individual & Corporate Consumer Sales
- Sales Team Management (Country-based)
- Lead & Pipeline Management
- Deal & Order Management
- Sales Performance Tracking
- Customer Relationship Management (CRM)
- Territory & Quota Management
- Sales Forecasting

**Read-Only from Shared Cores:**
- Partner Data (Sellers, Couriers, Logistics Providers, etc.)
- Product/Service Catalogs
- Inventory Levels (for sales availability)

### 1.3 User Roles & Entry Points

┌─────────────────────────────────────────────────────────────────────┐
│                     COUNTRY SALES DASHBOARD USERS                   │
├─────────────────────────────────────────────────────────────────────┤
│                                                                      │
│   ┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐ │
│   │ Country Sales   │    │ Sales Manager   │    │ Sales Executive │ │
│   │ Director        │    │ (Department)    │    │ / Representative│ │
│   │                 │    │                 │    │                 │ │
│   │ Assigned to:    │    │ Assigned to:    │    │ Assigned to:    │ │
│   │ ONE country     │    │ ONE country     │    │ ONE country     │ │
│   │                 │    │                 │    │                 │ │
│   │ Full Access     │    │ Department      │    │ Personal        │ │
│   │ All Modules     │    │ Access          │    │ Access          │ │
│   └─────────────────┘    └─────────────────┘    └─────────────────┘ │
│                                                                      │
│   ┌─────────────────┐    ┌─────────────────┐                         │
│   │ Sales Operations│    │ Sales Analyst   │                         │
│   │ Manager         │    │                 │                         │
│   │                 │    │                 │                         │
│   │ Assigned to:    │    │ Assigned to:    │                         │
│   │ ONE country     │    │ ONE country     │                         │
│   │                 │    │                 │                         │
│   │ Ops Focus       │    │ Read-Only       │                         │
│   └─────────────────┘    └─────────────────┘                         │
│                                                                      │
└─────────────────────────────────────────────────────────────────────┘

### 1.4 Role Permissions Matrix

| Module | Director | Sales Manager | Executive | Ops Manager | Analyst |
|--------|----------|---------------|-----------|-------------|---------|
| Dashboard Overview | Full | Full | Read | Read | Read |
| Sales Team | Full | Department | Own | Department | Read |
| Sales Team Partners | Full | Department | Read | Department | Read |
| Leads & Pipeline | Full | Department | Own | All | Read |
| Deals & Orders | Full | Department | Own | All | Read |
| Customers | Full | Department | Assigned | All | Read |
| Performance | Full | Department | Own | Department | Read |
| Territory & Quota | Full | Department | Read | Full | Read |
| Forecasting | Full | Department | Read | Full | Read |
| Settings | Full | Department | None | None | None |

---

## 2. USER ROLES & PERMISSIONS

### 2.1 Country Sales Director

**Description:** Highest authority for sales operations in assigned country

**Access Level:** Full access to all modules and features within the country

**Key Capabilities:**
- Manage entire sales organization for the country
- Set and adjust sales targets and quotas
- Approve large deals and discounts
- Manage sales team structure and assignments
- Full access to all reports and analytics
- Configure territory assignments
- Approve commission structures

**Limitations:**
- Can ONLY view/manage data for assigned country
- Cannot modify system-level settings
- Cannot access other countries' data

### 2.2 Sales Manager (Department)

**Description:** Manages sales operations for a specific department or region within the country

**Access Level:** Department-level access

**Key Capabilities:**
- Manage sales team within department
- View department pipeline and deals
- Approve department-level deals
- Assign leads to team members
- View department performance reports
- Manage customer relationships for department

**Limitations:**
- Can ONLY view/manage department data
- Cannot access other departments
- Cannot modify quotas/targets (view only)

### 2.3 Sales Executive / Representative

**Description:** Individual contributor responsible for selling and customer relationships

**Access Level:** Personal access only

**Key Capabilities:**
- Manage assigned leads and opportunities
- Update personal pipeline
- Create and manage deals
- View personal performance metrics
- Manage assigned customer relationships
- Submit discount requests

**Limitations:**
- Can ONLY access own data
- Cannot view team member pipelines (unless shared)
- Cannot approve deals (requires manager)

### 2.4 Sales Operations Manager

**Description:** Manages sales operations, processes, and territory/quota administration

**Access Level:** Operations focus with cross-department visibility

**Key Capabilities:**
- Manage territory assignments
- Administer quota settings
- Configure sales processes
- View all pipelines and deals (operations view)
- Generate operational reports
- Manage sales tools integrations

**Limitations:**
- Cannot modify team assignments (HR function)
- Cannot approve deals (sales manager role)

### 2.5 Sales Analyst

**Description:** Analyzes sales data and generates insights

**Access Level:** Read-only access for analytics

**Key Capabilities:**
- View all sales data (read-only)
- Generate custom reports
- Create dashboards and visualizations
- Export data for analysis
- Access historical data

**Limitations:**
- Cannot modify any data
- Cannot approve or reject transactions
- Cannot assign leads or territories

---

## 3. NAVIGATION ARCHITECTURE

### 3.1 Primary Navigation Structure

```
┌─────────────────────────────────────────────────────────────────────────┐
│                         COUNTRY SALES DASHBOARD                        │
├─────────────────────────────────────────────────────────────────────────┤
│  🏠 Overview  👥 Sales Team  🤝 Partners  📊 Pipeline  💼 Deals  👥 Customers│
│  📈 Performance  🎯 Territory  📋 Orders  📩 Communications  ⚙️ Settings│
└─────────────────────────────────────────────────────────────────────────┘
```

### 3.2 Navigation Hierarchy

```
Country Sales Dashboard
│
├── 🏠 Dashboard Overview
│   ├── Sales Performance Summary
│   ├── Pipeline Overview
│   ├── Recent Activities
│   ├── Team Performance
│   └── Alerts & Notifications
│
├── 👥 Sales Team
│   ├── Team Directory
│   ├── Performance Tracker
│   ├── Workload Distribution
│   ├── Commission Tracker
│   └── Team Analytics
│
├── 🤝 Sales Team Partners
│   ├── Partners Overview
│   ├── Partner Applications
│   │   ├── Pending Reviews
│   │   ├── Approved Partners
│   │   └── Rejected Applications
│   ├── Partner Performance
│   │   ├── By Partner Type
│   │   ├── By Territory
│   │   └── Performance Reports
│   ├── Commission Management
│   │   ├── Referral Bonus Tracking
│   │   ├── Sales Commission Tracking
│   │   ├── Tier Progression
│   │   └── Payout Processing
│   ├── Territory Management
│   │   ├── Partner Assignments
│   │   ├── Lead Distribution
│   │   └── Capacity Planning
│   └── Partner Support
│       ├── Training Resources
│       ├── Performance Coaching
│       └── Issue Resolution
│
├── 📊 Leads & Pipeline
│   ├── Lead Management
│   ├── Pipeline View (Kanban)
│   ├── Lead Scoring
│   ├── Lead Assignment
│   └── Pipeline Analytics
│
├── 💼 Deals & Opportunities
│   ├── Active Deals
│   ├── Deal Pipeline
│   ├── Deal Management
│   ├── Approval Requests
│   └── Deal History
│
├── 🛒 Orders Management
│   ├── Order List
│   ├── Order Details
│   ├── Order Tracking
│   ├── Returns & Refunds
│   └── Bulk Orders
│
├── 🤝 Customer Management (CRM)
│   ├── Customer Directory
│   ├── Customer Profiles
│   ├── Account Management
│   ├── Interaction History
│   └── Customer Segmentation
│
├── 📈 Performance & Reports
│   ├── Sales Performance
│   ├── Team Performance
│   ├── Product/Service Sales
│   ├── Conversion Analytics
│   └── Custom Reports
│
├── 🎯 Territory & Quota
│   ├── Territory Management
│   ├── Quota Administration
│   ├── Territory Performance
│   └── Quota Tracking
│
├── 📡 Sales Forecasting
│   ├── Revenue Forecast
│   ├── Pipeline Forecast
│   ├── Trend Analysis
│   └── Predictive Analytics
│
├── 📩 Communications
│   ├── Email Templates
│   ├── Campaign Management
│   ├── Follow-up Reminders
│   └── Communication History
│
└── ⚙️ Settings
    ├── Profile Settings
    ├── Team Configuration
    ├── Territory Settings
    ├── Notification Preferences
    └── System Configuration
```

### 3.3 Breadcrumb Navigation

```
Home > {Module} > {Sub-module} > {Detail View}

Examples:
- Home > Dashboard > Overview
- Home > Pipeline > Leads > Lead Details
- Home > Deals > Active > Deal #12345
- Home > Customers > Directory > Customer Profile
- Home > Performance > Reports > Monthly Sales Report
```

---

## 4. UI FLOW DIAGRAMS

### 4.1 Authentication Flow

```
┌──────────────┐
│              │
│  User Visits │
│  Dashboard   │
│              │
└──────┬───────┘
       │
       ▼
┌─────────────────────────────────────────────────────────────┐
│                    Authentication Check                     │
├─────────────────────────────────────────────────────────────┤
│  • Check for valid session token                           │
│  • Verify token expiration                                  │
│  • If valid → Proceed to Dashboard                         │
│  • If invalid → Redirect to Login                          │
└──────┬──────────────────────────────────────────────────────┘
       │
       ▼
┌─────────────────────────────────────────────────────────────┐
│                      Not Authenticated                      │
├─────────────────────────────────────────────────────────────┤
│  ┌──────────────────────────────────────────────────────┐  │
│  │              LOGIN SCREEN                             │  │
│  │  • Email/Username input                               │  │
│  │  • Password input                                     │  │
│  │  • Country display (auto-detected from profile)       │  │
│  │  • "Forgot Password" link                             │  │
│  │  • Login button                                       │  │
│  └──────────────────────────────────────────────────────┘  │
└──────┬──────────────────────────────────────────────────────┘
       │
       ▼
┌─────────────────────────────────────────────────────────────┐
│                   Authentication Process                    │
├─────────────────────────────────────────────────────────────┤
│  • Validate credentials via API Gateway                     │
│  • Authentication Service verifies user                    │
│  • Retrieve user roles and assigned country                │
│  • Generate session token                                  │
│  • Store token securely                                    │
└──────┬──────────────────────────────────────────────────────┘
       │
       ▼
┌─────────────────────────────────────────────────────────────┐
│                      Authenticated                          │
├─────────────────────────────────────────────────────────────┤
│  • Verify user has Country Sales Dashboard access          │
│  • Load user permissions for assigned country only         │
│  • Initialize WebSocket connection for real-time updates   │
│  • Redirect to Dashboard Overview                          │
└──────┬──────────────────────────────────────────────────────┘
       │
       ▼
┌─────────────────────────────────────────────────────────────┐
│                    DASHBOARD OVERVIEW                       │
│  • Display sales metrics for assigned country              │
│  • Show user's permitted modules based on role             │
│  • Load real-time data via WebSocket                       │
└─────────────────────────────────────────────────────────────┘
```

### 4.2 Dashboard Overview Flow

```
┌───────────────────────────────────────────────────────────────┐
│                    DASHBOARD OVERVIEW                         │
├───────────────────────────────────────────────────────────────┤
│                                                                │
│  ┌─────────────────────────────────────────────────────────┐  │
│  │              KEY METRICS CARDS                          │  │
│  │  ┌───────────┐ ┌───────────┐ ┌───────────┐ ┌─────────┐  │  │
│  │  │ Total     │ │ Pipeline  │ │ Won This  │ │ Deals   │  │  │
│  │  │ Revenue   │ │ Value     │ │ Month     │ │ Closed  │  │  │
│  │  │ $1.2M     │ │ $3.5M     │ │ $450K     │ │ 127     │  │  │
│  │  │ +12%      │ │ +8%       │ │ +15%      │ │ +5      │  │  │
│  │  └───────────┘ └───────────┘ └───────────┘ └─────────┘  │  │
│  └─────────────────────────────────────────────────────────┘  │
│                                                                │
│  ┌─────────────────────────────────────────────────────────┐  │
│  │              SALES PERFORMANCE CHART                    │  │
│  │  Revenue vs Target | Quota Attainment | Trend Line      │  │
│  └─────────────────────────────────────────────────────────┘  │
│                                                                │
│  ┌───────────────────────┐ ┌───────────────────────────────┐  │
│  │   PIPELINE STATUS     │ │   TEAM PERFORMANCE            │  │
│  │   ┌─────────────┐     │ │   ┌─────────────────────┐     │  │
│  │   │ Prospecting │ 45%  │ │   │ Top Performers      │     │  │
│  │   │ Qualification│ 25% │ │   │ • John Doe - $120K │     │  │
│  │   │ Proposal    │ 18%  │ │   │ • Jane Smith - $98K│     │  │
│  │   │ Negotiation │ 10%  │ │   │ • Bob Wilson - $85K│     │  │
│  │   │ Closing     │ 2%   │ │   └─────────────────────┘     │  │
│  │   └─────────────┘     │ │                                 │  │
│  └───────────────────────┘ └───────────────────────────────┘  │
│                                                                │
│  ┌─────────────────────────────────────────────────────────┐  │
│  │              RECENT ACTIVITIES                          │  │
│  │  • New deal created: Enterprise Deal - $50K            │  │
│  │  • Deal moved to negotiation: Tech Corp - $25K         │  │
│  │  • Lead assigned to: Sarah Johnson                     │  │
│  │  • Order shipped: Order #12345                         │  │
│  └─────────────────────────────────────────────────────────┘  │
│                                                                │
└───────────────────────────────────────────────────────────────┘
```

### 4.3 Lead Management Flow

```
┌───────────────────────────────────────────────────────────────┐
│                    LEAD MANAGEMENT                            │
├───────────────────────────────────────────────────────────────┤
│                                                                │
│  ┌─────────────────────────────────────────────────────────┐  │
│  │              LEAD LIST VIEW                             │  │
│  │  ┌───────────────────────────────────────────────────┐  │  │
│  │  │  🔍 Search  │  Filter │  + Add Lead │  Import │   │  │  │
│  │  └───────────────────────────────────────────────────┘  │  │
│  │  ┌────────────────────────────────────────────────────┐ │  │
│  │  │ Lead │ Company │ Status │ Score │ Owner │ Value │   │ │  │
│  │  ├─────┼─────────┼────────┼───────┼───────┼───────┤   │  │
│  │  │ L1  │ Acme Inc│ New    │ 85    │ John  │ $50K  │   │  │
│  │  │ L2  │ TechCorp │ Qual  │ 72    │ Jane  │ $25K  │   │  │
│  │  │ L3  │ Global  │ Prop   │ 91    │ Bob   │ $100K │   │  │
│  │  └────────────────────────────────────────────────────┘ │  │
│  └─────────────────────────────────────────────────────────┘  │
│                            │                                    │
│                            ▼                                    │
│  ┌─────────────────────────────────────────────────────────┐  │
│  │              LEAD DETAIL VIEW                           │  │
│  │  ┌───────────────────────────────────────────────────┐  │  │
│  │  │  Lead Information                                   │  │  │
│  │  │  • Contact Details                                   │  │  │
│  │  │  • Company Information                               │  │  │
│  │  │  • Lead Source                                       │  │  │
│  │  │  • Lead Score & Qualification                        │  │  │
│  │  │  • Activity Timeline                                 │  │  │
│  │  │  • Notes & Comments                                  │  │
│  │  └───────────────────────────────────────────────────┘  │  │
│  │  ┌───────────────────────────────────────────────────┐  │  │
│  │  │  Actions:                                           │  │  │
│  │  │  [Convert to Deal] [Assign Owner] [Update Status]  │  │  │
│  │  │  [Schedule Follow-up] [Add Note] [Email]           │  │  │
│  │  └───────────────────────────────────────────────────┘  │  │
│  └─────────────────────────────────────────────────────────┘  │
│                            │                                    │
│                            ▼                                    │
│  ┌─────────────────────────────────────────────────────────┐  │
│  │              CONVERT TO DEAL FLOW                       │  │
│  │  1. Verify Lead Qualification                           │  │
│  │  2. Select Products/Services                            │  │
│  │  3. Set Initial Deal Value                              │  │
│  │  4. Assign Deal Owner                                   │  │
│  │  5. Create Opportunity Record                           │  │
│  │  6. Link to Customer Profile                            │  │
│  └─────────────────────────────────────────────────────────┘  │
│                                                                │
└───────────────────────────────────────────────────────────────┘
```

### 4.4 Deal Management Flow

```
┌───────────────────────────────────────────────────────────────┐
│                    DEAL MANAGEMENT                            │
├───────────────────────────────────────────────────────────────┤
│                                                                │
│  ┌─────────────────────────────────────────────────────────┐  │
│  │              DEAL PIPELINE VIEW (Kanban)                │  │
│  │  ┌───────────┐ ┌───────────┐ ┌───────────┐ ┌─────────┐  │  │
│  │  │Prospecting│ │Qualification│ │Proposal │ │Negotiate│  │  │
│  │  │    [5]    │ │    [8]     │ │   [12]   │ │   [6]   │  │  │
│  │  ├───────────┤ ├───────────┤ ├───────────┤ ├─────────┤  │  │
│  │  │ Deal A    │ │ Deal B    │ │ Deal C    │ │ Deal D  │  │  │
│  │  │ $25K      │ │ $50K      │ │ $100K     │ │ $75K    │  │  │
│  │  │ Acme Inc  │ │ TechCorp  │ │ Global    │ │ Prime   │  │  │
│  │  │ John      │ │ Jane      │ │ Bob       │ │ Sarah   │  │  │
│  │  │           │ │           │ │           │ │         │  │  │
│  │  │ Deal E    │ │ Deal F    │ │ Deal G    │ │ Deal H  │  │  │
│  │  │ $15K      │ │ $30K      │ │ $60K      │ │ $40K    │  │  │
│  │  │          ...│ │         ...│ │        ...│ │      ...│  │  │
│  │  └───────────┘ └───────────┘ └───────────┘ └─────────┘  │  │
│  │                                                             │  │
│  │  ┌───────────┐ ┌───────────┐ ┌───────────┐               │  │
│  │  │  Closing  │ │   Won     │ │   Lost    │               │  │
│  │  │    [3]    │ │   [45]    │ │   [12]    │               │  │
│  │  ├───────────┤ ├───────────┤ ├───────────┤               │  │
│  │  │ Deal I    │ │ Deal J    │ │ Deal K    │               │  │
│  │  │ $20K      │ │ $80K      │ │ $35K      │               │  │
│  │  │ MegaCorp  │ │ Elite Inc │ │ Small Co  │               │  │
│  │  │ Mike      │ │ John      │ │ Jane      │               │  │
│  │  └───────────┘ └───────────┘ └───────────┘               │  │
│  └─────────────────────────────────────────────────────────┘  │
│                            │                                    │
│                            ▼                                    │
│  ┌─────────────────────────────────────────────────────────┐  │
│  │              DEAL DETAIL VIEW                           │  │
│  │  ┌───────────────────────────────────────────────────┐  │  │
│  │  │  Deal Information                                   │  │  │
│  │  │  • Deal Name & Number                               │  │  │
│  │  │  • Customer/Account                                 │  │  │
│  │  │  • Deal Value & Currency                            │  │  │
│  │  │  • Stage & Probability                              │  │  │
│  │  │  • Expected Close Date                              │  │  │
│  │  │  • Products/Services Line Items                     │  │  │
│  │  │  • Discount Applied                                 │  │  │
│  │  │  • Commission Structure                             │  │  │
│  │  └───────────────────────────────────────────────────┘  │  │
│  │  ┌───────────────────────────────────────────────────┐  │  │
│  │  │  Actions:                                           │  │  │
│  │  │  [Move Stage] [Edit Deal] [Add Product]            │  │  │
│  │  │  [Request Approval] [Create Order] [Close Deal]    │  │  │
│  │  └───────────────────────────────────────────────────┘  │  │
│  └─────────────────────────────────────────────────────────┘  │
│                                                                │
└───────────────────────────────────────────────────────────────┘
```

### 4.5 Customer Management Flow

```
┌───────────────────────────────────────────────────────────────┐
│                    CUSTOMER MANAGEMENT (CRM)                  │
├───────────────────────────────────────────────────────────────┤
│                                                                │
│  ┌─────────────────────────────────────────────────────────┐  │
│  │              CUSTOMER DIRECTORY                         │  │
│  │  ┌───────────────────────────────────────────────────┐  │  │
│  │  │  🔍 Search  │  Filter │  + Add Customer │ Import │  │  │
│  │  └───────────────────────────────────────────────────┘  │  │
│  │  ┌────────────────────────────────────────────────────┐ │  │
│  │  │ Customer │ Type │ Industry │ Status │ Owner │ ARV │  │  │
│  │  ├─────────┼─────┼──────────┼────────┼───────┼─────┤  │  │
│  │  │ Acme Inc │ Corp│ Tech     │ Active │ John  │$120K│  │  │
│  │  │ TechCorp │ Corp│ Retail   │ Active │ Jane  │ $85K│  │  │
│  │  │ John Doe │ Ind │ N/A      │ Active │ Sarah │ $25K│  │  │
│  │  └────────────────────────────────────────────────────┘ │  │
│  └─────────────────────────────────────────────────────────┘  │
│                            │                                    │
│                            ▼                                    │
│  ┌─────────────────────────────────────────────────────────┐  │
│  │              CUSTOMER PROFILE VIEW                      │  │
│  │  ┌───────────────────────────────────────────────────┐  │  │
│  │  │  Customer Overview                                   │  │  │
│  │  │  • Company/Personal Information                      │  │  │
│  │  │  • Customer Type & Segment                           │  │  │
│  │  │  • Account Status & Tier                             │  │  │
│  │  │  • Assigned Sales Rep                                 │  │  │
│  │  └───────────────────────────────────────────────────┘  │  │
│  │  ┌───────────────────────────────────────────────────┐  │  │
│  │  │  Account Summary                                     │  │  │
│  │  │  • Total Lifetime Value                              │  │  │
│  │  │  • Annual Recurring Revenue                          │  │  │
│  │  │  • Total Orders/Deals                                │  │  │
│  │  │  • Average Order Value                               │  │  │
│  │  │  • Payment Terms & Credit Limit                      │  │  │
│  │  └───────────────────────────────────────────────────┘  │  │
│  │  ┌───────────────────────────────────────────────────┐  │  │
│  │  │  Related Records                                     │  │  │
│  │  │  • Associated Deals                                  │  │  │
│  │  │  • Order History                                     │  │  │
│  │  │  • Open Opportunities                                │  │  │
│  │  │  • Support Tickets                                   │  │  │
│  │  └───────────────────────────────────────────────────┘  │  │
│  │  ┌───────────────────────────────────────────────────┐  │  │
│  │  │  Interaction Timeline                                │  │  │
│  │  │  • All communications                                │  │  │
│  │  │  • Meetings and calls                                │  │  │
│  │  │  • Notes and activities                              │  │  │
│  │  │  • Document attachments                               │  │  │
│  │  └───────────────────────────────────────────────────┘  │  │
│  └─────────────────────────────────────────────────────────┘  │
│                                                                │
└───────────────────────────────────────────────────────────────┘
```

### 4.6 Order Management Flow

```
┌───────────────────────────────────────────────────────────────┐
│                    ORDER MANAGEMENT                           │
├───────────────────────────────────────────────────────────────┤
│                                                                │
│  ┌─────────────────────────────────────────────────────────┐  │
│  │              ORDER LIST VIEW                            │  │
│  │  ┌───────────────────────────────────────────────────┐  │  │
│  │  │  🔍 Search  │  Filter │  + Create Order │ Import │  │  │
│  │  └───────────────────────────────────────────────────┘  │  │
│  │  ┌────────────────────────────────────────────────────┐ │  │
│  │  │ Order # │ Customer │ Status │ Total │ Date │ Ship │  │  │
│  │  ├─────────┼─────────┼────────┼───────┼──────┼──────┤  │  │
│  │  │ SO-001  │ Acme Inc │ Pending│ $25K  │ 02/05│ ✓    │  │  │
│  │  │ SO-002  │ TechCorp │ Shipped│ $50K  │ 02/03│ ✓    │  │  │
│  │  │ SO-003  │ Global  │ Delivrd│ $100K │ 01/28│ ✓    │  │  │
│  │  │ SO-004  │ Prime   │ Cancel │ $15K  │ 02/01│ ✗    │  │  │
│  │  └────────────────────────────────────────────────────┘ │  │
│  └─────────────────────────────────────────────────────────┘  │
│                            │                                    │
│                            ▼                                    │
│  ┌─────────────────────────────────────────────────────────┐  │
│  │              ORDER DETAIL VIEW                          │  │
│  │  ┌───────────────────────────────────────────────────┐  │  │
│  │  │  Order Information                                   │  │  │
│  │  │  • Order Number & Date                               │  │  │
│  │  │  • Customer Details                                   │  │  │
│  │  │  • Order Status & Tracking                           │  │  │
│  │  │  • Shipping & Billing Address                        │  │  │
│  │  └───────────────────────────────────────────────────┘  │  │
│  │  ┌───────────────────────────────────────────────────┐  │  │
│  │  │  Line Items                                          │  │  │
│  │  │  • Product/Service                                   │  │  │
│  │  │  • Quantity & Unit Price                             │  │  │
│  │  │  • Discounts & Taxes                                 │  │  │
│  │  │  • Subtotal & Total                                  │  │  │
│  │  └───────────────────────────────────────────────────┘  │  │
│  │  ┌───────────────────────────────────────────────────┐  │  │
│  │  │  Actions:                                           │  │  │
│  │  │  [Edit Order] [Cancel] [Process Return]            │  │  │
│  │  │  [Generate Invoice] [Send Confirmation]            │  │  │
│  │  └───────────────────────────────────────────────────┘  │  │
│  └─────────────────────────────────────────────────────────┘  │
│                                                                │
└───────────────────────────────────────────────────────────────┘
```

---

## 5. MODULE-SIFIC FLOWS

### 5.1 Sales Team Management Flow

```
┌───────────────────────────────────────────────────────────────┐
│                    SALES TEAM MANAGEMENT                      │
├───────────────────────────────────────────────────────────────┤
│                                                                │
│  1. TEAM DIRECTORY                                            │
│     ├─ View all sales team members                           │
│     ├─ Filter by department/role                             │
│     ├─ Search by name/email                                  │
│     └─ View individual profiles                              │
│                                                                │
│  2. PERFORMANCE TRACKER                                       │
│     ├─ Individual performance metrics                        │
│     ├─ Quota attainment                                      │
│     ├─ Commission earned                                     │
│     ├─ Conversion rates                                      │
│     └─ Activity metrics (calls, emails, meetings)            │
│                                                                │
│  3. WORKLOAD DISTRIBUTION                                    │
│     ├─ Active deals per rep                                  │
│     ├─ Pipeline value distribution                          │
│     ├─ Lead assignment balance                               │
│     └─ Capacity planning                                     │
│                                                                │
│  4. COMMISSION TRACKER                                       │
│     ├─ Commission calculations                               │
│     ├─ Payment history                                       │
│     ├─ Pending commissions                                   │
│     └─ Commission structure rules                            │
│                                                                │
│  5. TEAM ANALYTICS                                            │
│     ├─ Team performance trends                               │
│     ├─ Comparative analysis                                  │
│     ├─ Attrition tracking                                    │
│     └─ Hiring needs                                          │
│                                                                │
└───────────────────────────────────────────────────────────────┘
```

### 5.2 Territory & Quota Management Flow

```
┌───────────────────────────────────────────────────────────────┐
│                    TERRITORY & QUOTA MANAGEMENT                │
├───────────────────────────────────────────────────────────────┤
│                                                                │
│  1. TERRITORY MANAGEMENT                                       │
│     ├─ Define geographic territories                          │
│     ├─ Assign territories to reps                             │
│     ├─ View territory performance                            │
│     ├─ Territory overlap management                          │
│     └─ Boundary adjustments                                   │
│                                                                │
│  2. QUOTA ADMINISTRATION                                      │
│     ├─ Set annual/quarterly/monthly quotas                   │
│     ├─ Assign quotas to individuals/teams                     │
│     ├─ Quota distribution rules                              │
│     ├─ Quota adjustment workflows                            │
│     └─ Quota approval process                                │
│                                                                │
│  3. TERRITORY PERFORMANCE                                     │
│     ├─ Territory vs quota comparison                         │
│     ├─ Geographic performance maps                           │
│     ├─ Territory growth trends                               │
│     └─ Under-performing territories                          │
│                                                                │
│  4. QUOTA TRACKING                                            │
│     ├─ Real-time quota attainment                            │
│     ├─ Quota vs actual analysis                              │
│     ├─ Forecast to quota                                     │
│     └─ Quota achievement recognition                         │
│                                                                │
└───────────────────────────────────────────────────────────────┘
```

### 5.3 Sales Forecasting Flow

```
┌───────────────────────────────────────────────────────────────┐
│                    SALES FORECASTING                          │
├───────────────────────────────────────────────────────────────┤
│                                                                │
│  1. REVENUE FORECAST                                          │
│     ├─ Weighted pipeline forecasting                         │
│     ├─ Historical trend analysis                             │
│     ├─ Seasonality adjustments                               │
│     ├─ Scenario planning (best/worst/likely)                 │
│     └─ Forecast accuracy tracking                            │
│                                                                │
│  2. PIPELINE FORECAST                                         │
│     ├─ Stage-based forecasting                               │
│     ├─ Velocity calculations                                 │
│     ├─ Deal slip predictions                                 │
│     ├─ New business forecast                                 │
│     └─ Renewal forecast                                      │
│                                                                │
│  3. TREND ANALYSIS                                            │
│     ├─ Revenue trends                                        │
│     ├─ Product/service mix trends                            │
│     ├─ Customer segment trends                               │
│     ├─ Win/loss analysis                                     │
│     └─ Competitive analysis                                  │
│                                                                │
│  4. PREDICTIVE ANALYTICS                                      │
│     ├─ Lead score predictions                                │
│     ├─ Deal win probability                                  │
│     ├─ Churn risk indicators                                 │
│     ├─ Next best action recommendations                       │
│     └─ AI-powered insights                                   │
│                                                                │
└───────────────────────────────────────────────────────────────┘
```

### 5.4 Communications & Campaign Management Flow

```
┌───────────────────────────────────────────────────────────────┐
│                    COMMUNICATIONS & CAMPAIGNS                  │
├───────────────────────────────────────────────────────────────┤
│                                                                │
│  1. EMAIL TEMPLATES                                            │
│     ├─ Template library                                       │
│     ├─ Custom template creation                               │
│     ├─ Dynamic field variables                                │
│     ├─ Template approval workflow                            │
│     └─ Template usage analytics                              │
│                                                                │
│  2. CAMPAIGN MANAGEMENT                                       │
│     ├─ Campaign creation & setup                              │
│     ├─ Target audience selection                              │
│     ├─ Campaign scheduling                                   │
│     ├─ Multi-channel campaigns                               │
│     └─ Budget tracking                                       │
│                                                                │
│  3. FOLLOW-UP REMINDERS                                       │
│     ├─ Automatic follow-up scheduling                        │
│     ├─ Task reminders                                        │
│     ├─ Next activity suggestions                             │
│     ├─ Escalation rules                                      │
│     └─ Reminder templates                                    │
│                                                                │
│ 4. COMMUNICATION HISTORY                                      │
│     ├─ Email integration                                      │
│     ├─ Call logging                                           │
│     ├─ Meeting notes                                         │
│     ├─ Activity timeline                                     │
│     └─ Communication analytics                               │
│                                                                │
└───────────────────────────────────────────────────────────────┘
```

---

## 6. SALES TEAM PARTNERS MODULE (NEW)

The Sales Team Partners module in the Country Sales Dashboard provides country-level management for Sales Partners, including application approvals, territory assignments, performance monitoring, and commission tracking.

### 6.1 Module Purpose

**Primary Functions:**
- Review and approve Sales Partner applications for the country
- Manage territory assignments and lead allocations
- Monitor partner performance at country level
- Track partner commissions and payouts
- Provide support and training for partners
- Coordinate with Management-Domain for partner onboarding

**Integration with Sales Team Partners App:**
- Country Sales Directors receive applications from sales-team-app
- Approved partners are routed to appropriate shared-business-infrastructure dashboards
- Commission data flows back to sales-team-app for partner visibility
- AI-generated leads are allocated based on partner territory assignments

### 6.2 Partner Application Review Flow

```
┌───────────────────────────────────────────────────────────────────┐
│          PARTNER APPLICATION REVIEW (Country Level)                   │
├───────────────────────────────────────────────────────────────────┤
│                                                                    │
│  [NEW APPLICATION FROM sales-team-app]                                 │
│         │                                                          │
│         ▼                                                          │
│  ┌─────────────────────────────────────────────────────────────┐   │
│  │  APPLICATION QUEUE (Pending Review)                            │   │
│  │  • Displays all pending applications for the country          │   │
│  │  • Filter by: Partner Type, Territory, Date Range               │   │
│  └─────────────────────────────────────────────────────────────┘   │
│         │                                                          │
│         ▼                                                          │
│  ┌─────────────────────────────────────────────────────────────┐   │
│  │  APPLICATION DETAIL                                          │   │
│  │  • Personal Information                                        │   │
│  │  • Sales Experience History                                    │   │
│  │  • Preferred Partner Types (up to 3)                          │   │
│  │  • Preferred Territory                                       │   │
│  │  • Bank Details                                               │   │
│  │  • Background Verification Status                             │   │
│  └─────────────────────────────────────────────────────────────┘   │
│         │                                                          │
│         ▼                                                          │
│  ┌─────────────────────────────────────────────────────────────┐   │
│  │  TERRITORY AVAILABILITY CHECK                                │   │
│  │  • Check requested territory capacity                           │   │
│  │  • Review current partner count in territory                    │   │
│  │  • Assess lead allocation availability                        │   │
│  │  • Recommend: Approve as-is OR Suggest alternative              │   │
│  └─────────────────────────────────────────────────────────────┘   │
│         │                                                          │
│         ▼                                                          │
│  ┌─────────────────────────────────────────────────────────────┐   │
│  │  DECISION                                                  │   │
│  │  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐      │   │
│  │  │   ✓ Approve  │  │   ✗ Reject   │  │   📧 Request   │      │   │
│  │  │              │  │              │  │   More Info    │      │   │
│  │  └──────────────┘  └──────────────┘  └──────────────┘      │   │
│  └─────────────────────────────────────────────────────────────┘   │
│         │                                                          │
│         ├─── [APPROVED] ──────────────────────────────────────┐    │
│         │                                                          │    │
│         ▼                                                          │    │
│  ┌─────────────────────────────────────────────────────────────┐   │    │
│  │  CREATE PARTNER ACCOUNT                                       │   │    │
│  │  • Generate Partner ID (SP-YYYY-XXXX)                        │   │    │
│  │  • Assign to Territory                                       │   │    │
│  │  • Set Initial Commission Tier (Bronze)                         │   │    │
│  │  │                                                            │   │    │
│  │  ┌───────────────────────────────────────────────────────┐  │   │    │
│  │  │ ROUTE TO APPROPRIATE SHARED-BUSINESS-INFRASTRUCTURE │  │   │    │
│  │  │ Partner Type │ Target Dashboard                             │  │   │    │
│  │  ├────────────┼──────────────────────────────────────┤  │   │    │
│  │  │ Courier     │ Courier-Partners-Dashboard             │  │   │    │
│  │  │ Haulage     │ Haulage-Partners-Dashboard             │  │   │    │
│  │  │ Warehouse   │ Warehouse-Partners-Dashboard           │  │   │    │
│  │  │ E-commerce  │ Ecommerce-Vendors-Dashboard            │  │   │    │
│  │  │ Air/Ocean   │ agents-dashboard (Air/Ocean)            │  │   │    │
│  │  │ Location   │ Location-Agents-Dashboard              │  │   │    │
│  │  │ Wholesale  │ wholesalers-dashboard                   │  │   │    │
│  │  │ Influencer │ Influencers-Dashboard                   │  │   │    │
│  │  └────────────┴──────────────────────────────────────┘  │   │    │
│  │  │                                                            │   │    │
│  │  │ Route to:                                                    │   │    │
│  │  │ • sales-web-dashboard (Management-Domain)                  │   │    │
│  │  │ • country-sales-web-dashboard (Business-Domain)             │   │    │
│  │  └───────────────────────────────────────────────────────┘  │   │    │
│  └─────────────────────────────────────────────────────────────┘   │    │
│         │                                                          │    │
│         ▼                                                          │    │
│  ┌─────────────────────────────────────────────────────────────┐   │    │
│  │  NOTIFY APPLICANT                                           │   │    │
│  │  • Push notification to sales-team-app                        │   │    │
│  │  • Email confirmation                                       │   │    │
│  │  • SMS notification with welcome message                       │   │    │
│  │  │                                                            │   │    │
│  │  ┌───────────────────────────────────────────────────────┐  │   │    │
│  │  │ Welcome to the Sales Team Partners program!               │  │   │    │
│  │  │ You've been approved as a Sales Partner for Nigeria.       │  │   │    │
│  │  │ Your Partner ID: SP-2025-XXXX                            │  │   │    │
│  │  │ Territory: North Lagos Zone                                 │  │   │    │
│  │  │ Log in to your sales-team-app to get started.            │  │   │    │
│  │  └───────────────────────────────────────────────────────┘  │   │    │
│  └─────────────────────────────────────────────────────────────┘   │    │
│         │                                                          │    │
│         └─── [REJECTED] ──────────────────────────────────────┐    │
│                                                                  │    │
│  ┌─────────────────────────────────────────────────────────────┐   │    │
│  │  NOTIFY APPLICANT OF REJECTION                               │   │    │
│  │  • Push notification                                        │   │    │
│  │  • Email with reason                                         │   │    │
│  │  │  • Option to reapply after 90 days                         │   │    │
│  └─────────────────────────────────────────────────────────────┘   │    │
│                                                                    │
└─────────────────────────────────────────────────────────────────────┘
```

### 6.3 Partner Performance Monitoring

```
┌───────────────────────────────────────────────────────────────────┐
│              PARTNER PERFORMANCE TRACKING (Country Level)               │
├───────────────────────────────────────────────────────────────────┤
│                                                                    │
│  1. PARTNER LEADERBOARD                                            │
│     ├─ Top 10 partners by commission earned                          │
│     ├─ Top partners by partner type                                  │
│     ├─ Partners by territory                                        │
│     └─ Month-over-month performance changes                           │
│                                                                    │
│  2. PERFORMANCE BY PARTNER TYPE                                     │
│     ├─ Courier Partners performance                                │
│     │   • Average deals/month                                       │
│     │   • Average commission/partner                               │
│     │   • Top performers                                            │
│     ├─ Haulage Partners performance                                 │
│     ├─ Warehouse Partners performance                               │
│     ├─ E-commerce Vendors performance                              │
│     ├─ Air/Ocean Agents performance                                 │
│     ├─ Location Agents performance                                 │
│     ├─ Wholesale Partners performance                               │
│     └─ Influencer Partners performance                                │
│                                                                    │
│  3. TERRITORY PERFORMANCE                                         │
│     ├─ Partner density by territory                                 │
│     ├─ Total commission by territory                               │
│     ├─ Lead conversion rate by territory                           │
│     ├─ Underperforming territories identification                     │
│     └─ Territory reallocation recommendations                        │
│                                                                    │
│  4. PARTNER ACTIVITY MONITORING                                   │
│     ├─ Login frequency to sales-team-app                           │
│     ├─ Lead response time                                          │
│     ├─ Partner onboarding activity                                │
│     ├─ Deal activity                                              │
│     └─ Communication activity                                       │
│                                                                    │
└─────────────────────────────────────────────────────────────────────┘
```

### 6.4 Commission Tracking for Partners

```
┌───────────────────────────────────────────────────────────────────┐
│              COMMISSION TRACKING (Country Level)                         │
├───────────────────────────────────────────────────────────────────┤
│                                                                    │
│  1. PARTNER REFERRAL BONUS TRACKING                               │
│     ├─ Track partner onboarding by partner                         │
│     ├─ Calculate referral bonuses based on partner type                 │
│     │   • Courier: 5% of registration fee                           │
│     │   • Haulage: 6% of registration fee                            │
│     │   • Warehouse: 7% of registration fee                          │
│     │   • E-commerce: 5% of registration fee                         │
│     │   • Air/Ocean: 8% of registration fee                          │
│     │   • Location Agent: 4% of registration fee                     │
│     │   • Wholesale: 6% of registration fee                          │
│     │   • Influencer: 3% of registration fee                         │
│     ├─ Bonus eligibility tracking (after first transaction)          │
│     └─ Payout processing                                            │
│                                                                    │
│  2. CUSTOMER SALES COMMISSION TRACKING                             │
│     ├─ Track deals closed by Sales Partners                       │
│     ├─ Calculate commissions based on:                              │
│     │   • Individual customers: 3-5% (tier-based)                    │
│     │   • Corporate customers: 5-10% (tier-based)                   │
│     ├─ Tier progression monitoring                                  │
│     │   • Bronze → Silver (₦1M in sales)                              │
│     │   • Silver → Gold (₦5M in sales)                                │
│     │   │   • Gold → Platinum (₦15M in sales)                          │
│     └─ Performance bonus tracking                                   │
│                                                                    │
│  3. PAYOUT PROCESSING                                              │
│     ├─ Monthly commission aggregation                              │
│     ├─ Validation of deal status                                    │
│     ├─ Payment file generation                                   │
│     ├─ Bank transfer initiation                                    │
│     └─ Payout confirmation                                           │
│                                                                    │
│  4. COMMISSION DISPUTE RESOLUTION                                  │
│     ├─ Dispute ticket creation                                    │
│     ├─ Investigation process                                       │
│     ├─ Adjustment processing                                      │
│     └─ Resolution notification                                    │
│                                                                    │
└─────────────────────────────────────────────────────────────────────┘
```

### 6.5 Territory Allocation Management

```
┌───────────────────────────────────────────────────────────────────┐
│              TERRITORY ALLOCATION (Country Level)                       │
├───────────────────────────────────────────────────────────────────┤
│                                                                    │
│  1. TERRITORY DEFINITION                                           │
│     ├─ Define geographic boundaries within country                 │
│     ├─ Sub-divide into zones (North, South, East, West)             │
│     ├─ Set partner capacity per territory                          │
│     ├─ Define lead allocation per territory                         │
│     └─ Territory map visualization                                  │
│                                                                    │
│  2. PARTNER-TERRITORY ASSIGNMENT                                │
│     ├─ Assign Sales Partners to territories                       │
│     ├─ View partner distribution map                             │
│     ├─ Reassign partners to new territories                       │
│     ├─ Partner territory change history                           │
│     └─ Territory capacity planning                                │
│                                                                    │
│  3. LEAD ALLOCATION                                               │
│     ├─ View AI-generated leads by territory                       │
│     ├─ Allocate leads to partners based on:                        │
│     │   • Partner tier (higher tier = priority)                    │
│     │   • Partner performance                                  │
│     │   • Partner capacity                                    │
│     │   • Lead-territory match                                   │
│     ├─ Bulk lead reassignment                                    │
│     ├─ Lead assignment rules configuration                        │
│     └─ Lead allocation performance tracking                      │
│                                                                    │
│  4. TERRITORY PERFORMANCE TRACKING                                │
│     ├─ Total revenue by territory                               │
│     ├─ Partner count vs territory performance                     │
│     ├─ Lead conversion rate by territory                           │
│     ├─ Territory optimization recommendations                        │
│     └─ Territory expansion planning                                 │
│                                                                    │
└─────────────────────────────────────────────────────────────────────┘
```

---

## 7. DATA FLOW ARCHITECTURE

### 6.1 Data Sources & Integration Points

```
┌─────────────────────────────────────────────────────────────────────┐
│                     COUNTRY SALES DASHBOARD DATA FLOW              │
├─────────────────────────────────────────────────────────────────────┤
│                                                                      │
│  ┌─────────────────────────────────────────────────────────────┐   │
│  │                    LOCAL DATA STORE                         │   │
│  │  (Country Sales Database - PostgreSQL)                      │   │
│  │  • Leads & Opportunities                                    │   │
│  │  • Deals & Pipeline                                         │   │
│  │  • Customer Relationships (CRM)                             │   │
│  │  • Sales Team Data                                          │   │
│  │  • Territory & Quota Assignments                            │   │
│  │  • Orders (linked to Order Management)                      │   │
│  │  • Communications History                                   │   │
│  └─────────────────────────────────────────────────────────────┘   │
│                                │                                    │
│                                ▼                                    │
│  ┌─────────────────────────────────────────────────────────────┐   │
│  │              COUNTRY SALES API LAYER                        │   │
│  │  (REST + GraphQL + WebSocket)                               │   │
│  └─────────────────────────────────────────────────────────────┘   │
│                                │                                    │
│         ┌──────────────────────┼──────────────────────┐            │
│         ▼                      ▼                      ▼            │
│  ┌──────────────┐    ┌──────────────────┐    ┌──────────────┐     │
│  │ SHARED CORES │    │  BUSINESS DOMAIN │    │   EXTERNAL   │     │
│  │   (Read-Only)│    │    SERVICES      │    │  INTEGRATIONS│     │
│  ├──────────────┤    ├──────────────────┤    ├──────────────┤     │
│  │ Seller Core  │    │ Order Svc       │    │ Email (SMTP) │     │
│  │ Courier Core │    │ Payment Svc     │    │ Calendar API │     │
│  │ Logistics    │    │ Inventory Svc   │    │ Video Conf  │     │
│  │ Provider Core│    │ Shipping Svc    │    │ SMS Gateway  │     │
│  │ Product Core │    │ Billing Svc     │    │             │     │
│  │ Warehouse    │    │                 │    │             │     │
│  │ Core         │    │                 │    │             │     │
│  └──────────────┘    └──────────────────┘    └──────────────┘     │
│                                                                      │
└─────────────────────────────────────────────────────────────────────┘
```

### 6.2 Real-Time Data Updates (WebSocket)

```
┌─────────────────────────────────────────────────────────────────────┐
│                    REAL-TIME UPDATE FLOW                           │
├─────────────────────────────────────────────────────────────────────┤
│                                                                      │
│  WebSocket Connection: wss://sales.country.{country}.api/v1/ws      │
│                                                                      │
│  Subscriptions:                                                     │
│  • /topic/leads/{leadId} - Lead updates                            │
│  • /topic/deals/{dealId} - Deal stage changes                      │
│  • /topic/pipeline - Pipeline overview updates                     │
│  • /topic/orders/{orderId} - Order status updates                  │
│  • /topic/team/{userId} - Team activity updates                    │
│  • /topic/quotas - Quota attainment updates                        │
│  • /topic/forecast - Forecast data refresh                         │
│                                                                      │
│  Message Format (STOMP):                                            │
│  {                                                                  │
│    "type": "DEAL_STAGE_CHANGED",                                   │
│    "dealId": "deal-123",                                           │
│    "oldStage": "PROPOSAL",                                         │
│    "newStage": "NEGOTIATION",                                      │
│    "timestamp": "2025-02-08T10:30:00Z"                            │
│  }                                                                  │
│                                                                      │
└─────────────────────────────────────────────────────────────────────┘
```

### 6.3 Data Flow to HQ Dashboard

```
┌─────────────────────────────────────────────────────────────────────┐
│                 DATA FLOW: COUNTRY SALES → HQ SALES               │
├─────────────────────────────────────────────────────────────────────┤
│                                                                      │
│  ┌─────────────────────────────────────────────────────────────┐   │
│  │              COUNTRY SALES DASHBOARD                        │   │
│  │  (Business Domain)                                          │   │
│  │  • Daily sales summary reports                              │   │
│  │  • Pipeline snapshots                                       │   │
│  │  • Deal closure events                                      │   │
│  │  • Team performance metrics                                 │   │
│  │  • Quota attainment data                                    │   │
│  └─────────────────────────────────────────────────────────────┘   │
│                                │                                    │
│                                │ Kafka Topics                       │
│                                │ (Sales Data Events)               │
│                                │                                    │
│  ┌─────────────────────────────────────────────────────────────┐   │
│  │              MANAGEMENT DOMAIN (HQ)                         │   │
│  │  HQ Sales Dashboard                                         │   │
│  │  • Aggregates data from all countries                       │   │
│  │  • Multi-country performance comparison                     │   │
│  │  • Global sales analytics                                   │   │
│  │  • Executive reporting                                      │   │
│  └─────────────────────────────────────────────────────────────┘   │
│                                                                      │
└─────────────────────────────────────────────────────────────────────┘
```

---

## 8. ERROR HANDLING & STATES

### 7.1 Loading States

| Component | Loading State | Description |
|-----------|---------------|-------------|
| Dashboard | Skeleton cards + progress bars | Metric cards loading |
| Tables | Circular progress indicator | Data fetching |
| Forms | Disabled inputs + spinner | Submission in progress |
| Charts | Skeleton chart placeholder | Chart data loading |
| Pipeline | Gray placeholder cards | Pipeline loading |

### 7.2 Error States

| Error Type | UI Response | Recovery Options |
|------------|-------------|------------------|
| Network Error | "Connection lost. Retrying..." banner | Auto-retry with exponential backoff |
| Auth Error | Redirect to login | Re-authenticate |
| Validation Error | Inline error messages | Fix input + resubmit |
| Not Found | 404 page with back button | Navigate elsewhere |
| Server Error | "Something went wrong" message | Retry button + support contact |

### 7.3 Empty States

| Scenario | Empty State Design |
|----------|-------------------|
| No Leads | "No leads yet. Click + Add Lead to get started." |
| No Deals | "No active deals. Your pipeline is empty." |
| No Customers | "No customers yet. Start adding customers." |
| No Orders | "No orders found. Orders will appear here." |
| No Results | "No results match your search criteria." |

---

## VERSION HISTORY

| Version | Date | Changes |
|---------|------|---------|
| 1.0 | 2025-02-08 | Initial Country Sales Dashboard UI Flow Documentation |

---

## NEXT STEPS

- Complete 02_Wireframes_Documentation.md (Visual mockups)
- Complete 03_Mock_Flow_Documentation.md (API endpoints, data models)
- Complete 04_Page_By_Page_Flow_Documentation.md (Detailed page flows)

---

**Document End**
