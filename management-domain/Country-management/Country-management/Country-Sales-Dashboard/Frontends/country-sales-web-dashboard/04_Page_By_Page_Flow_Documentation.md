# COUNTRY SALES DASHBOARD - PAGE BY PAGE FLOW DOCUMENTATION

**Version:** 1.0
**Domain:** Business Domain
**Subdomain:** Country-Sales-Dashboard
**Frontend:** country-sales-web-dashboard
**Framework:** React 18 + Vite + TypeScript + Material UI v5.15+
**State Management:** Zustand
**Data Fetching:** TanStack Query
**Last Updated:** 2025-02-08

---

## TABLE OF CONTENTS

1. [Page Tree Structure](#1-page-tree-structure)
2. [Authentication Flows](#2-authentication-flows)
3. [Dashboard Flows](#3-dashboard-flows)
4. [Sales Team Flows](#4-sales-team-flows)
5. [Leads & Pipeline Flows](#5-leads--pipeline-flows)
6. [Deals Flows](#6-deals-flows)
7. [Orders Flows](#7-orders-flows)
8. [Customer Flows](#8-customer-flows)
9. [Performance Flows](#9-performance-flows)
10. [Territory & Quota Flows](#10-territory--quota-flows)
11. [Forecasting Flows](#11-forecasting-flows)
12. [Communications Flows](#12-communications-flows)
13. [Settings Flows](#13-settings-flows)
14. [Permission Matrix](#14-permission-matrix)

---

## 1. PAGE TREE STRUCTURE

```
Country Sales Dashboard
│
├── /login
│   ├── Login Page
│   └── Forgot Password Page
│
├── / (protected)
│   ├── /dashboard (Overview)
│   │   ├── Summary View
│   │   ├── Performance Charts
│   │   └── Quick Actions
│   │
│   ├── /team
│   │   ├── Team Directory
│   │   ├── /:memberId (Member Profile)
│   │   ├── /performance
│   │   ├── /commission
│   │   └── /workload
│   │
│   ├── /leads
│   │   ├── List View
│   │   ├── /pipeline (Kanban View)
│   │   ├── /scoring
│   │   ├── /:leadId (Lead Detail)
│   │   └── /new (Create Lead)
│   │
│   ├── /deals
│   │   ├── List View
│   │   ├── /:dealId (Deal Detail)
│   │   ├── /new (Create Deal)
│   │   ├── /approvals
│   │   └── /:dealId/stage (Stage Change)
│   │
│   ├── /orders
│   │   ├── List View
│   │   ├── /:orderId (Order Detail)
│   │   └── /new (Create Order)
│   │
│   ├── /customers
│   │   ├── List View
│   │   ├── /:customerId (Customer Profile)
│   │   ├── /:customerId/deals
│   │   ├── /:customerId/orders
│   │   ├── /:customerId/activities
│   │   └── /new (Create Customer)
│   │
│   ├── /performance
│   │   ├── Sales Performance
│   │   ├── Team Performance
│   │   ├── /reports
│   │   └── /analytics
│   │
│   ├── /territory
│   │   ├── Territory Management
│   │   ├── /:territoryId (Territory Detail)
│   │   └── /quotas
│   │
│   ├── /forecasting
│   │   ├── Revenue Forecast
│   │   ├── Pipeline Forecast
│   │   ├── /trends
│   │   └── /accuracy
│   │
│   ├── /communications
│   │   ├── Email Templates
│   │   ├── /campaigns
│   │   ├── /:campaignId (Campaign Detail)
│   │   └── /history
│   │
│   └── /settings
│       ├── Profile Settings
│       ├── Team Configuration
│       ├── /territories
│       ├── /notifications
│       └── /stages (Pipeline Configuration)
```

---

## 2. AUTHENTICATION FLOWS

### 2.1 Login Flow

```
┌─────────────────────────────────────────────────────────────────────────────────┐
│                            LOGIN FLOW                                           │
├─────────────────────────────────────────────────────────────────────────────────┤
│                                                                                 │
│  USER ACTION                         SYSTEM RESPONSE                             │
│  ────────────                        ────────────────                           │
│  1. Navigate to dashboard URL       → Check for valid token                     │
│                                      → If valid: redirect to dashboard          │
│                                      → If invalid: show login page              │
│                                                                                 │
│  2. Enter email and password         → Validate input format                    │
│                                      → Show loading state on button             │
│                                                                                 │
│  3. Click "Login" button             → POST /auth/login                         │
│                                      → Authenticate credentials                │
│                                      → Retrieve user profile & country          │
│                                      → Store access token                      │
│                                      → Initialize WebSocket connection         │
│                                      → Redirect to dashboard                   │
│                                                                                 │
│  SUCCESS FLOW:                                                                   │
│  ┌───────────────────────────────────────────────────────────────────────────┐  │
│  │  Login Form                                                    [Login]     │  │
│  │  ┌────────────────────────────────────────────────────────────────────┐  │  │
│  │  │  Email:               [john.doe@company.ng                ]        │  │  │
│  │  │  Password:            [•••••••••••••••••••••••••            ]        │  │  │
│  │  │                       [Forgot password?]                         │  │  │
│  │  │  Country assigned:    🇳🇬 Nigeria (auto-detected)               │  │  │
│  │  │                                                                  │  │  │
│  │  │                                              [Cancel]  [Login]  │  │  │
│  │  └────────────────────────────────────────────────────────────────────┘  │  │
│  └───────────────────────────────────────────────────────────────────────────┘  │
│                                      │                                           │
│                                      ▼                                           │
│  ┌───────────────────────────────────────────────────────────────────────────┐  │
│  │  Logging in...                                                              │  │
│  │  ┌────────────────────────────────────────────────────────────────────┐  │  │
│  │  │                                                                        │  │  │
│  │  │                        [Circular Progress]                            │  │  │
│  │  │                        Authenticating...                              │  │  │
│  │  │                                                                        │  │  │
│  │  └────────────────────────────────────────────────────────────────────┘  │  │
│  └───────────────────────────────────────────────────────────────────────────┘  │
│                                      │                                           │
│                                      ▼                                           │
│  ┌───────────────────────────────────────────────────────────────────────────┐  │
│  │  Dashboard Overview                                                          │  │
│  │  ┌────────────────────────────────────────────────────────────────────┐  │  │
│  │  │  Welcome, John Doe                                                   │  │  │
│  │  │  Country Sales Director - Nigeria                                    │  │  │
│  │  │                                                                  │  │  │
│  │  │  [Metrics...]                                                       │  │  │
│  │  └────────────────────────────────────────────────────────────────────┘  │  │
│  └───────────────────────────────────────────────────────────────────────────┘  │
│                                                                                 │
│  ERROR FLOW:                                                                    │
│  ┌───────────────────────────────────────────────────────────────────────────┐  │
│  │  Login Form                                                    [Login]     │  │
│  │  ┌────────────────────────────────────────────────────────────────────┐  │  │
│  │  │  Email:               [john.doe@company.ng                ]        │  │  │
│  │  │  Password:            [•••••••••••••••••••••••••            ]        │  │  │
│  │  │                                                                  │  │  │
│  │  │  ❌ Invalid credentials. Please check your email and password.  │  │  │
│  │  │                       [Forgot password?]                         │  │  │
│  │  │                                                                  │  │  │
│  │  │                                              [Cancel]  [Login]  │  │  │
│  │  └────────────────────────────────────────────────────────────────────┘  │  │
│  └───────────────────────────────────────────────────────────────────────────┘  │
│                                                                                 │
└─────────────────────────────────────────────────────────────────────────────────┘
```

### 2.2 Logout Flow

```
┌─────────────────────────────────────────────────────────────────────────────────┐
│                            LOGOUT FLOW                                          │
├─────────────────────────────────────────────────────────────────────────────────┤
│                                                                                 │
│  USER ACTION                         SYSTEM RESPONSE                             │
│  ────────────                        ────────────────                           │
│  1. Click user avatar in header      → Open dropdown menu                       │
│                                                                                 │
│  2. Click "Logout"                   → Show confirmation dialog                  │
│                                      → "Are you sure you want to logout?"       │
│                                                                                 │
│  3. Confirm logout                   → POST /auth/logout                        │
│                                      → Invalidate token on server               │
│                                      → Clear local storage                      │
│                                      → Close WebSocket connection              │
│                                      → Redirect to login page                  │
│                                                                                 │
└─────────────────────────────────────────────────────────────────────────────────┘
```

---

## 3. DASHBOARD FLOWS

### 3.1 Dashboard Overview Flow

```
┌─────────────────────────────────────────────────────────────────────────────────┐
│                        DASHBOARD OVERVIEW FLOW                                  │
├─────────────────────────────────────────────────────────────────────────────────┤
│                                                                                 │
│  ENTRY: User logs in → Dashboard Overview                                       │
│                                                                                 │
│  PAGE COMPONENTS:                                                               │
│  ┌───────────────────────────────────────────────────────────────────────────┐  │
│  │  HEADER: Country Sales Dashboard > Overview                               │  │
│  │  FILTERS: [Time Range Selector] [Country: Nigeria (read-only)]            │  │
│  │  ───────────────────────────────────────────────────────────────────────  │  │
│  │  KEY METRICS CARDS (4):                                                    │  │
│  │  • Total Revenue      • Pipeline Value   • Deals Closed  • Avg Deal Size │  │
│  │  ───────────────────────────────────────────────────────────────────────  │  │
│  │  CHARTS SECTION:                                                           │  │
│  │  • Revenue vs Target Chart      • Pipeline Donut Chart                     │  │
│  │  ───────────────────────────────────────────────────────────────────────  │  │
│  │  DATA TABLES:                                                              │  │
│  │  • Top Performers Table         • Recent Deals                             │  │
│  │  • Active Opportunities          • Lead Sources Chart                      │  │
│  │  ───────────────────────────────────────────────────────────────────────  │  │
│  │  ALERTS SECTION:                                                          │  │
│  │  • Deals at risk  • Pending approvals  • Quota alerts                      │  │
│  └───────────────────────────────────────────────────────────────────────────┘  │
│                                                                                 │
│  USER ACTIONS:                                                                  │
│  ────────────                                                                   │
│  1. Change time range                  → Reload all data with new period        │
│                                        → Update charts and metrics              │
│                                                                                 │
│  2. Click metric card                  → Navigate to detailed view              │
│                                        • Total Revenue → /performance           │
│                                        • Pipeline Value → /leads/pipeline       │
│                                        • Deals Closed → /deals?status=won        │
│                                        • Avg Deal Size → /performance           │
│                                                                                 │
│  3. Click top performer                 → Open member profile modal               │
│                                        → Show performance details               │
│                                        → Option to navigate to /team/:memberId  │
│                                                                                 │
│  4. Click recent deal                   → Navigate to /deals/:dealId             │
│                                                                                 │
│  5. Click alert                        → Navigate to relevant page               │
│                                        • Deals at risk → /deals?status=at-risk  │
│                                        • Pending approvals → /deals/approvals   │
│                                        • Quota alerts → /territory/quotas       │
│                                                                                 │
│  6. Real-time updates (WebSocket)     → Auto-refresh metrics every 30 seconds   │
│                                        → Update pipeline values live            │
│                                        → Show toast notifications for events     │
│                                                                                 │
└─────────────────────────────────────────────────────────────────────────────────┘
```

### 3.2 Dashboard Refresh Flow

```
┌─────────────────────────────────────────────────────────────────────────────────┐
│                        DASHBOARD REFRESH FLOW                                   │
├─────────────────────────────────────────────────────────────────────────────────┤
│                                                                                 │
│  TRIGGERS:                                                                     │
│  • Page load (initial)                                                         │
│  • Time range change                                                           │
│  • Manual refresh (pull-to-refresh or button)                                  │
│  • WebSocket update notification                                               │
│  • Return from another page                                                    │
│                                                                                 │
│  FLOW:                                                                         │
│  ┌───────────────────────────────────────────────────────────────────────────┐  │
│  │  1. Check permissions                                                     │  │
│  │     └─ Verify user has access to dashboard                                │  │
│  │         └─ If not authorized: redirect to /unauthorized                   │  │
│  │                                                                          │  │
│  │  2. Show loading skeleton                                                  │  │
│  │     └─ Display placeholder UI for metrics and charts                       │  │
│  │                                                                          │  │
│  │  3. Fetch dashboard summary (GET /dashboard/summary)                      │  │
│  │     ├─ Query: { period: selectedPeriod }                                  │  │
│  │     └─ Response: DashboardSummary                                         │  │
│  │                                                                          │  │
│  │  4. Fetch top performers (GET /team?limit=5&sortBy=revenue)              │  │
│  │     └─ Response: SalesRep[] with performance metrics                      │  │
│  │                                                                          │  │
│  │  5. Fetch recent deals (GET /deals?limit=10&status=ACTIVE)               │  │
│  │     └─ Response: Deal[]                                                  │  │
│  │                                                                          │  │
│  │  6. Fetch pipeline summary (GET /leads/pipeline)                         │  │
│  │     └─ Response: PipelineSummary[]                                       │  │
│  │                                                                          │  │
│  │  7. Fetch alerts (GET /notifications?unread=true)                        │  │
│  │     └─ Response: Notification[]                                          │  │
│  │                                                                          │  │
│  │  8. Update UI with fetched data                                           │  │
│  │     ├─ Populate metric cards                                              │  │
│  │     ├─ Render charts                                                      │  │
│  │     ├─ Fill tables                                                        │  │
│  │     └─ Show alert badges                                                  │  │
│  │                                                                          │  │
│  │  9. Subscribe to WebSocket for real-time updates                          │  │
│  │     └─ Topics: /topic/dashboard, /topic/deals, /topic/leads              │  │
│  │                                                                          │  │
│  │  10. Handle errors                                                         │  │
│  │      ├─ Network error: Show retry banner, auto-retry                      │  │
│  │      ├─ Auth error: Redirect to login                                     │  │
│  │      └─ Server error: Show error state, offer refresh button              │  │
│  │                                                                          │  │
│  └───────────────────────────────────────────────────────────────────────────┘  │
│                                                                                 │
└─────────────────────────────────────────────────────────────────────────────────┘
```

---

## 4. SALES TEAM FLOWS

### 4.1 Team Directory Flow

```
┌─────────────────────────────────────────────────────────────────────────────────┐
│                           TEAM DIRECTORY FLOW                                   │
├─────────────────────────────────────────────────────────────────────────────────┤
│                                                                                 │
│  NAVIGATION: /team                                                              │
│                                                                                 │
│  PAGE COMPONENTS:                                                               │
│  ┌───────────────────────────────────────────────────────────────────────────┐  │
│  │  HEADER: Sales Team > Directory                                           │  │
│  │  FILTERS: [Department] [Status] [Territory] [Search]                      │  │
│  │  ACTIONS: [+ Add Team Member] [Export]                                    │  │
│  │  ───────────────────────────────────────────────────────────────────────  │  │
│  │  TEAM LIST (Table or Card View):                                           │  │
│  │  ┌────────────────────────────────────────────────────────────────────┐  │  │
│  │  │ Avatar │ Name          │ Department  │ Territory │ Revenue│ Quota │  │  │
│  │  │        │ Email         │ Status      │           │        │ Attain│  │  │
│  │  ├────────────────────────────────────────────────────────────────────┤  │  │
│  │  │ [Photo] │ John Doe      │ Enterprise  │ SW        │ ₦250K  │ 125%  │  │  │
│  │  │        │ john@...      │ Active      │           │        │       │  │  │
│  │  ├────────────────────────────────────────────────────────────────────┤  │  │
│  │  │ [Photo] │ Jane Smith    │ SMB         │ SE        │ ₦180K  │ 120%  │  │  │
│  │  │        │ jane@...      │ Active      │           │        │       │  │  │
│  │  └────────────────────────────────────────────────────────────────────┘  │  │
│  └───────────────────────────────────────────────────────────────────────────┘  │
│                                                                                 │
│  USER ACTIONS:                                                                  │
│  ────────────                                                                   │
│  1. Click team member row             → Navigate to /team/:memberId             │
│                                                                                 │
│  2. Apply filters                     → Update table with filtered results       │
│                                        • Filter by department                   │
│                                        • Filter by status (active/inactive)      │
│                                        • Filter by territory                    │
│                                        • Search by name/email                   │
│                                                                                 │
│  3. Click [+ Add Team Member]         → Open create team member modal          │
│                                        • Requires admin permissions             │
│                                        • Route to HR system for creation        │
│                                        • Assign to department/territory        │
│                                                                                 │
│  4. Export team list                  → Generate CSV/Excel file                 │
│                                        • Include visible columns                │
│                                        • Respect applied filters                │
│                                                                                 │
└─────────────────────────────────────────────────────────────────────────────────┘
```

### 4.2 Team Member Profile Flow

```
┌─────────────────────────────────────────────────────────────────────────────────┐
│                       TEAM MEMBER PROFILE FLOW                                  │
├─────────────────────────────────────────────────────────────────────────────────┤
│                                                                                 │
│  NAVIGATION: /team/:memberId                                                     │
│                                                                                 │
│  PAGE COMPONENTS:                                                               │
│  ┌───────────────────────────────────────────────────────────────────────────┐  │
│  │  HEADER: [< Back to Team]  │  Member Name  │  [Edit] [Email] [Call]       │  │
│  │  ───────────────────────────────────────────────────────────────────────  │  │
│  │  ┌─────────────────────────────┐ ┌──────────────────────────────────────┐  │  │
│  │  │  MEMBER INFO                │ │  PERFORMANCE SUMMARY               │  │
│  │  │  • Name, Photo, Title       │ │  • Revenue vs Quota                 │  │
│  │  │  • Department, Territory    │ │  • Deals Closed                     │  │
│  │  │  • Email, Phone             │ │  • Win Rate                         │  │
│  │  │  • Hire Date, Status        │ │  • Avg Deal Size                    │  │
│  │  └─────────────────────────────┘ └──────────────────────────────────────┘  │  │
│  │  ───────────────────────────────────────────────────────────────────────  │  │
│  │  PERFORMANCE CHARTS:                                                        │  │
│  │  • Revenue Trend (Line Chart)                                              │  │
│  │  • Deals by Stage (Funnel Chart)                                           │  │
│  │  • Activity Metrics (Bar Chart)                                             │  │
│  │  ───────────────────────────────────────────────────────────────────────  │  │
│  │  TABS:                                                                      │  │
│  │  [Overview] [Deals] [Leads] [Commission] [Activities]                      │  │
│  │  ───────────────────────────────────────────────────────────────────────  │  │
│  │  TAB CONTENT (based on selection):                                         │  │
│  │  • Overview: Performance summary, recent activity                          │  │
│  │  • Deals: List of deals owned by member                                    │  │
│  │  • Leads: List of leads assigned to member                                 │  │
│  │  • Commission: Commission history, pending payments                        │  │
│  │  • Activities: Timeline of all activities                                  │  │
│  └───────────────────────────────────────────────────────────────────────────┘  │
│                                                                                 │
│  USER ACTIONS:                                                                  │
│  ────────────                                                                   │
│  1. Click [Email]                     → Open email client with member address   │
│                                                                                 │
│  2. Click [Call]                      → Initiate call (if integrated)            │
│                                                                                 │
│  3. Click [Edit] (admin only)        → Open edit member modal                  │
│                                        • Update department, territory           │
│                                        • Change status                         │
│                                        • Assign quota                           │
│                                                                                 │
│  4. Click tab                         → Switch tab content                      │
│                                        • Lazy load data for each tab            │
│                                                                                 │
│  5. Click deal in list                → Navigate to /deals/:dealId             │
│                                                                                 │
│  6. Change period selector            → Reload performance data for new period  │
│                                                                                 │
└─────────────────────────────────────────────────────────────────────────────────┘
```

---

## 5. LEADS & PIPELINE FLOWS

### 5.1 Lead List Flow

```
┌─────────────────────────────────────────────────────────────────────────────────┐
│                            LEAD LIST FLOW                                      │
├─────────────────────────────────────────────────────────────────────────────────┤
│                                                                                 │
│  NAVIGATION: /leads                                                             │
│                                                                                 │
│  PAGE COMPONENTS:                                                               │
│  ┌───────────────────────────────────────────────────────────────────────────┐  │
│  │  HEADER: Sales Team > Leads & Pipeline > List                             │  │
│  │  TABS: [📋 List View] [📊 Pipeline View] [🎯 Scoring]                     │  │
│  │  FILTERS:                                                                  │  │
│  │  ┌────────────────────────────────────────────────────────────────────┐  │  │
│  │  │ 🔍 Search  │ Status: [All ▼]  │ Source: [All ▼]  │ Score: [All ▼]  │  │  │
│  │  └────────────────────────────────────────────────────────────────────┘  │  │
│  │  ACTIONS: [+ Add Lead] [Import] [Export]                                 │  │
│  │  ───────────────────────────────────────────────────────────────────────  │  │
│  │  LEADS TABLE:                                                              │  │
│  │  ┌────────────────────────────────────────────────────────────────────┐  │  │
│  │  │ ☐ │ Lead # │ Company │ Contact │ Status │ Score │ Owner │ Created │  │  │
│  │  ├────────────────────────────────────────────────────────────────────┤  │  │
│  │  │ ☐ │ L-1001  │ Acme    │ John S. │ New    │ 85 🟢 │ John  │ Feb 8  │  │  │
│  │  │ ☐ │ L-1002  │ TechCorp│ Jane O. │ Qual   │ 72 🟡 │ Jane  │ Feb 7  │  │  │
│  │  │ ☐ │ L-1003  │ Global  │ Emma A. │ Prop   │ 91 🟢 │ Bob   │ Feb 5  │  │  │
│  │  └────────────────────────────────────────────────────────────────────┘  │  │
│  └───────────────────────────────────────────────────────────────────────────┘  │
│                                                                                 │
│  USER ACTIONS:                                                                  │
│  ────────────                                                                   │
│  1. Click [+ Add Lead]                → Open create lead modal/drawer          │
│                                        • Enter company and contact info        │
│                                        • Select source and owner                │
│                                        • Save → Create lead + redirect          │
│                                                                                 │
│  2. Click lead row                   → Navigate to /leads/:leadId              │
│                                                                                 │
│  3. Click checkbox                   → Enable bulk actions                      │
│                                        • [Convert] [Assign] [Export] [Delete]   │
│                                                                                 │
│  4. Apply filters                   → Update table with filtered leads          │
│                                        • Status, source, owner, score range    │
│                                        • Text search across fields             │
│                                                                                 │
│  5. Click column header              → Sort by column                           │
│                                        • Toggle ascending/descending           │
│                                                                                 │
│  6. Change page size                 → Update pagination                        │
│                                        • Options: 25, 50, 100, 200             │
│                                                                                 │
└─────────────────────────────────────────────────────────────────────────────────┘
```

### 5.2 Lead Detail Flow

```
┌─────────────────────────────────────────────────────────────────────────────────┐
│                            LEAD DETAIL FLOW                                     │
├─────────────────────────────────────────────────────────────────────────────────┤
│                                                                                 │
│  NAVIGATION: /leads/:leadId                                                      │
│                                                                                 │
│  PAGE COMPONENTS:                                                               │
│  ┌───────────────────────────────────────────────────────────────────────────┐  │
│  │  HEADER: [< Back to Leads]  │  Lead #L-1001  │  Status: Qualified  │     │  │
│  │  ACTIONS: [Convert to Deal] [Assign] [Update Status] [Email] [Delete]      │  │
│  │  ───────────────────────────────────────────────────────────────────────  │  │
│  │  ┌─────────────────────────────────────┐ ┌──────────────────────────────┐  │  │
│  │  │  LEAD INFORMATION                   │ │  LEAD SCORE & QUALIFICATION  │  │
│  │  │  • Company: Acme Industries         │ │  Score: 85/100 🟢           │  │
│  │  │  • Contact: John Smith              │ │  Qualification: High        │  │  │
│  │  │  • Email: john@acme.com             │ │  Readiness: 72%             │  │
│  │  │  • Phone: +234-XXX-XXXX             │ │  BANT Score: Qualified      │  │
│  │  │  • Source: Website                 │ │  [Recalculate Score]        │  │
│  │  │  • Owner: John Doe                 │ └──────────────────────────────┘  │  │
│  │  │  • Created: Feb 1, 2025            │                                   │  │
│  │  │  • Last Updated: Feb 8, 2025        │                                   │  │
│  │  └─────────────────────────────────────┘                                   │  │
│  │  ───────────────────────────────────────────────────────────────────────  │  │
│  │  TABS: [Overview] [Activities] [Notes] [Email History]                     │  │
│  │  ───────────────────────────────────────────────────────────────────────  │  │
│  │  TAB CONTENT:                                                               │  │
│  │  • Overview: Lead details, custom fields, conversion info                  │  │
│  │  • Activities: Timeline of all interactions                                │  │
│  │  • Notes: Internal notes and comments                                      │  │
│  │  • Email History: Email communications                                     │  │
│  │  ───────────────────────────────────────────────────────────────────────  │  │
│  │  QUICK ACTIONS:                                                             │  │
│  │  [+ Log Call] [+ Log Email] [+ Schedule Meeting] [+ Add Note]              │  │
│  └───────────────────────────────────────────────────────────────────────────┘  │
│                                                                                 │
│  USER ACTIONS:                                                                  │
│  ────────────                                                                   │
│  1. Click [Convert to Deal]          → Open convert to deal modal              │
│                                        • Pre-fill with lead data               │
│                                        • Select products/services              │
│                                        • Set deal value and owner              │
│                                        • Save → Create deal + update lead      │
│                                                                                 │
│  2. Click [Assign]                   → Open assign modal                        │
│                                        • Select new owner from team list        │
│                                        • Add assignment notes                  │
│                                        • Save → Update lead + notify owner     │
│                                                                                 │
│  3. Click [Email]                     → Open email composer                     │
│                                        • Pre-fill recipient email              │
│                                        • Select from template library          │
│                                        • Send → Log as activity                │
│                                                                                 │
│  4. Click [+ Log Call]                → Open log call modal                     │
│                                        • Record call outcome                   │
│                                        • Add notes and duration                │
│                                        • Save → Create activity record         │
│                                                                                 │
│  5. Change tab                        → Switch tab content                      │
│                                        • Lazy load activities/notes            │
│                                                                                 │
└─────────────────────────────────────────────────────────────────────────────────┘
```

### 5.3 Convert Lead to Deal Flow

```
┌─────────────────────────────────────────────────────────────────────────────────┐
│                        CONVERT LEAD TO DEAL FLOW                                 │
├─────────────────────────────────────────────────────────────────────────────────┤
│                                                                                 │
│  TRIGGER: From Lead Detail → Click [Convert to Deal]                            │
│                                                                                 │
│  MODAL STEPS:                                                                   │
│  ┌───────────────────────────────────────────────────────────────────────────┐  │
│  │  STEP 1: Verify Information                                                │  │
│  │  ┌────────────────────────────────────────────────────────────────────┐  │  │
│  │  │  Pre-filled from Lead:                                              │  │
│  │  │  • Company: Acme Industries                                         │  │
│  │  │  • Contact: John Smith                                               │  │
│  │  │  • Estimated Value: ₦50,000                                          │  │
│  │  │  • Source: Website                                                   │  │
│  │  │  • Owner: John Doe                                                   │  │
│  │  │                                                                      │  │  │
│  │  │  [Edit Information]  [Next: Products & Services →]                   │  │
│  │  └────────────────────────────────────────────────────────────────────┘  │  │
│  │                                                                          │  │
│  │  STEP 2: Products & Services                                             │  │
│  │  ┌────────────────────────────────────────────────────────────────────┐  │  │
│  │  │  Add Products/Services to this Deal:                                 │  │
│  │  │  ┌────────────────────────────────────────────────────────────────┤│  │  │
│  │  │  │ [+ Add Line Item]                                               ││  │  │
│  │  │  │ ┌─────────────────────────────────────────────────────────────┐ ││  │  │
│  │  │  │ │ Product  │ Description         │ Qty  │ Unit Price │ Total   │ ││  │  │
│  │  │  │ ├──────────┼────────────────────┼──────┼────────────┼─────────┤ ││  │  │
│  │  │  │ │ [Select] │                     │  1   │ ₦0         │ ₦0     │ ││  │  │
│  │  │  │ └──────────┴────────────────────┴──────┴────────────┴─────────┘ ││  │  │
│  │  │  │                                                                 ││  │  │
│  │  │  │  Subtotal: ₦0                                                    ││  │  │
│  │  │  │  Discount: ₦0                                                    ││  │  │
│  │  │  │  Tax: ₦0                                                         ││  │  │
│  │  │  │  Total: ₦0                                                       ││  │  │
│  │  │  └────────────────────────────────────────────────────────────────┘│  │  │
│  │  │                                                                      │  │  │
│  │  │  [← Back]  [Next: Deal Details →]                                   │  │  │
│  │  └────────────────────────────────────────────────────────────────────┘  │  │
│  │                                                                          │  │
│  │  STEP 3: Deal Details                                                    │  │
│  │  ┌────────────────────────────────────────────────────────────────────┐  │  │
│  │  │  Deal Name: [Enterprise Software License                            │  │
│  │  │  Deal Stage: [Qualification ▼]                                      │  │
│  │  │  Probability: [30%]                                                  │  │
│  │  │  Expected Close Date: [2025-03-15]                                  │  │
│  │  │  Territory: [South West ▼]                                          │  │
│  │  │  Deal Owner: [John Doe ▼]                                           │  │
│  │  │  Commission Split: [100% to John Doe ▼]                             │  │
│  │  │  Tags: [Add tags...]                                                │  │
│  │  │  Notes: [Additional notes...]                                       │  │
│  │  │                                                                      │  │  │
│  │  │  [← Back]  [Create Deal]                                            │  │  │
│  │  └────────────────────────────────────────────────────────────────────┘  │  │
│  └───────────────────────────────────────────────────────────────────────────┘  │
│                                                                                 │
│  FLOW AFTER CREATION:                                                          │
│  1. POST /leads/:id/convert                                                    │
│  2. Response: { lead: Lead, deal: Deal }                                      │
│  3. Update lead status to 'CONVERTED'                                         │
│  4. Link lead.convertedToDealId = deal.id                                     │
│  5. Show success toast: "Lead converted to deal successfully"                │
│  6. Redirect to /deals/:dealId                                                │
│  7. Optionally create customer if not exists                                 │
│                                                                                 │
└─────────────────────────────────────────────────────────────────────────────────┘
```

### 5.4 Pipeline Kanban View Flow

```
┌─────────────────────────────────────────────────────────────────────────────────┐
│                          PIPELINE KANBAN VIEW FLOW                              │
├─────────────────────────────────────────────────────────────────────────────────┤
│                                                                                 │
│  NAVIGATION: /leads/pipeline                                                    │
│                                                                                 │
│  PAGE COMPONENTS:                                                               │
│  ┌───────────────────────────────────────────────────────────────────────────┐  │
│  │  HEADER: Sales Team > Leads & Pipeline > Pipeline View                    │  │
│  │  FILTERS: [Owner: All ▼] [Source: All ▼] [Territory: All ▼]               │  │
│  │  SUMMARY: Total Pipeline Value: ₦3.5M | 89 deals                          │  │
│  │  ───────────────────────────────────────────────────────────────────────  │  │
│  │  KANBAN BOARD (Horizontal Scroll):                                          │  │
│  │  ┌─────────────┐ ┌─────────────┐ ┌─────────────┐ ┌─────────────┐           │  │
│  │  │  NEW [45]   │ │QUALIFIED[25]│ │ PROPOSAL[18]│ │NEGOTIATE[10]│           │  │
│  │  │ Value:₦450K │ │ Value:₦750K │ │ Value:₦1.2M │ │ Value:₦1.1M │           │  │
│  │  ├─────────────┤ ├─────────────┤ ├─────────────┤ ├─────────────┤           │  │
│  │  │ ┌─────────┐ │ │ ┌─────────┐ │ │ ┌─────────┐ │ │ ┌─────────┐ │           │  │
│  │  │ │Acme Inc │ │ │ │TechCorp │ │ │ │Global   │ │ │ │Prime    │ │           │  │
│  │  │ │₦50K     │ │ │ │₦75K     │ │ │ │₦150K    │ │ │ │₦250K   │ │           │  │
│  │  │ │John D.  │ │ │ │Jane S.  │ │ │ │Bob J.   │ │ │ │Chioma O.│ │           │  │
│  │  │ │Score:85 │ │ │ │Score:72 │ │ │ │Score:91 │ │ │ │Score:88 │ │           │  │
│  │  │ │[→]      │ │ │ │[→]      │ │ │ │[→]      │ │ │ │[→]      │ │           │  │
│  │  │ └─────────┘ │ │ └─────────┘ │ │ └─────────┘ │ │ └─────────┘ │           │  │
│  │  │ ┌─────────┐ │ │ ┌─────────┐ │ │ ┌─────────┐ │ │ ┌─────────┐ │           │  │
│  │  │ │Mega    │ │ │ │First    │ │ │ │Dangote │ │ │ │MTN NG   │ │           │  │
│  │  │ │Corp    │ │ │ │Bank     │ │ │ │Group    │ │ │ │         │ │           │  │
│  │  │ │₦35K    │ │ │ │₦60K     │ │ │ │₦200K   │ │ │ │₦180K   │ │           │  │
│  │  │ │Bob J.  │ │ │ │Sarah W. │ │ │ │John D.  │ │ │ │Jane S.  │ │           │  │
│  │  │ │Score:68│ │ │ │Score:82 │ │ │ │Score:94 │ │ │ │Score:76 │ │           │  │
│  │  │ │[→]      │ │ │ │[→]      │ │ │ │[→]      │ │ │ │[→]      │ │           │  │
│  │  │ └─────────┘ │ │ └─────────┘ │ │ └─────────┘ │ │ └─────────┘ │           │  │
│  │  │ [+2 more]  │ │ [+1 more]  │ │ [+3 more]  │ │ [+1 more]  │ │           │  │
│  │  └─────────────┘ └─────────────┘ └─────────────┘ └─────────────┘           │  │
│  │  ┌─────────────┐ ┌─────────────┐                                              │  │
│  │  │ CLOSING [3] │ │  WON [127]  │                                              │  │
│  │  │ Value:₦350K │ │ Value:₦1.2M │                                              │  │
│  │  └─────────────┘ └─────────────┘                                              │  │
│  └───────────────────────────────────────────────────────────────────────────┘  │
│                                                                                 │
│  USER ACTIONS:                                                                  │
│  ────────────                                                                   │
│  1. Click card                       → Open lead/deal detail modal            │
│                                        • Quick view of details                  │
│                                        • Option to navigate to full page       │
│                                                                                 │
│  2. Drag card to another stage         → Move lead/deal to new stage            │
│                                        • PUT /deals/:id/stage                   │
│                                        • Update probability automatically       │
│                                        • Show success toast                     │
│                                        • Broadcast via WebSocket                │
│                                                                                 │
│  3. Click [→] on card                 → Quick advance to next stage             │
│                                        • Same as drag action                    │
│                                                                                 │
│  4. Apply filters                    → Filter cards shown in each stage         │
│                                        • Filter by owner, source, territory     │
│                                        • Update summary metrics                 │
│                                                                                 │
│  5. Click stage header                → Filter by stage + show stage details     │
│                                        • Stage metrics (count, value)           │
│                                        • Average days in stage                  │
│                                        • Conversion rate to next stage          │
│                                                                                 │
└─────────────────────────────────────────────────────────────────────────────────┘
```

---

## 6. DEALS FLOWS

### 6.1 Deals List Flow

```
┌─────────────────────────────────────────────────────────────────────────────────┐
│                              DEALS LIST FLOW                                    │
├─────────────────────────────────────────────────────────────────────────────────┤
│                                                                                 │
│  NAVIGATION: /deals                                                             │
│                                                                                 │
│  PAGE COMPONENTS:                                                               │
│  ┌───────────────────────────────────────────────────────────────────────────┐  │
│  │  HEADER: Sales Team > Deals                                               │  │
│  │  TABS: [💼 Active Deals] [✅ Closed Won] [❌ Closed Lost] [⏸️ On Hold]      │  │
│  │  FILTERS:                                                                  │  │
│  │  ┌────────────────────────────────────────────────────────────────────┐  │  │
│  │  │ 🔍 Search  │ Stage: [All ▼]  │ Owner: [All ▼]  │ Value: [All ▼]     │  │  │
│  │  └────────────────────────────────────────────────────────────────────┘  │  │
│  │  ACTIONS: [+ New Deal] [Import] [Export]                                 │  │
│  │  ───────────────────────────────────────────────────────────────────────  │  │
│  │  DEALS TABLE:                                                              │  │
│  │  ┌────────────────────────────────────────────────────────────────────┐  │  │
│  │  │ Deal # │ Company │ Value │ Stage │ Prob. │ Owner │ Close │ Actions│  │  │
│  │  ├────────────────────────────────────────────────────────────────────┤  │  │
│  │  │ D-2501 │ Acme    │ ₦250K │ Negot  │ 75%   │ John  │ Feb 15│ [View]│  │  │
│  │  │ D-2502 │ TechCorp│ ₦180K │ Prop   │ 50%   │ Jane  │ Feb 20│ [View]│  │  │
│  │  │ D-2503 │ Global  │ ₦500K │ Negot  │ 80%   │ Bob   │ Feb 12│ [View]│  │  │
│  │  └────────────────────────────────────────────────────────────────────┘  │  │
│  └───────────────────────────────────────────────────────────────────────────┘  │
│                                                                                 │
│  USER ACTIONS:                                                                  │
│  ────────────                                                                   │
│  1. Click [+ New Deal]                → Open create deal modal                  │
│                                        • Select or create customer              │
│                                        • Add products/services                  │
│                                        • Set deal value and owner               │
│                                        • Save → Create deal + redirect          │
│                                                                                 │
│  2. Click deal row                   → Navigate to /deals/:dealId              │
│                                                                                 │
│  3. Click tab                        → Filter deals by status                    │
│                                        • Active, Won, Lost, On Hold            │
│                                                                                 │
│  4. Apply filters                    → Update table with filtered deals          │
│                                        • Stage, owner, value range             │
│                                        • Text search across fields             │
│                                                                                 │
│  5. Change sorting                   → Sort by any column                       │
│                                        • Toggle ascending/descending           │
│                                                                                 │
└─────────────────────────────────────────────────────────────────────────────────┘
```

### 6.2 Deal Detail Flow

```
┌─────────────────────────────────────────────────────────────────────────────────┐
│                              DEAL DETAIL FLOW                                   │
├─────────────────────────────────────────────────────────────────────────────────┤
│                                                                                 │
│  NAVIGATION: /deals/:dealId                                                      │
│                                                                                 │
│  PAGE COMPONENTS:                                                               │
│  ┌───────────────────────────────────────────────────────────────────────────┐  │
│  │  HEADER: [< Back to Deals]  │  Deal #D-2501  │  Stage: Negotiating 75%    │  │
│  │  ACTIONS: [✏️ Edit] [📄 PDF] [Move Stage] [Close Won] [Close Lost]           │  │
│  │  ───────────────────────────────────────────────────────────────────────  │  │
│  │  ┌─────────────────────────────────────┐ ┌──────────────────────────────┐  │  │
│  │  │  DEAL OVERVIEW                      │ │  DEAL STAGE                   │  │
│  │  │  • Company: Acme Industries         │ │  Current: Negotiation         │  │
│  │  │  • Contact: John Smith              │ │  Probability: 75%              │  │
│  │  │  • Value: ₦250,000                  │ │  Expected Close: Feb 15        │  │
│  │  │  • Discount: 0%                     │ │  Days in Stage: 5              │  │
│  │  │  • Net Value: ₦250,000              │ │  [████████████░░░░] 75%        │  │
│  │  │  • Owner: John Doe                  │ │  [Advance Stage]               │  │
│  │  │  • Created: Feb 1, 2025             │ │  [Put on Hold]                 │  │
│  │  └─────────────────────────────────────┘ └──────────────────────────────┘  │  │
│  │  ───────────────────────────────────────────────────────────────────────  │  │
│  │  PRODUCTS & SERVICES TABLE:                                            │  │
│  │  ┌────────────────────────────────────────────────────────────────────┐  │  │
│  │  │ Product        │ Description     │ Qty │ Unit Price │ Total │ Actions│  │  │
│  │  ├────────────────┼─────────────────┼─────┼────────────┼───────┼────────┤  │  │
│  │  │ Software Lic. │ Enterprise Plan │  1  │ ₦200,000   │₦200K  │ [Edit] │  │  │
│  │  │ Implementation│ Setup & Train. │  1  │ ₦50,000    │ ₦50K  │ [Edit] │  │  │
│  │  └────────────────┴─────────────────┴─────┴────────────┴───────┴────────┘  │  │
│  │  [+ Add Line Item]                                                      │  │
│  │  Subtotal: ₦250,000  Discount: ₦0  Tax: ₦18,750  TOTAL: ₦268,750          │  │
│  │  ───────────────────────────────────────────────────────────────────────  │  │
│  │  COMMISSION INFO:                                                          │  │
│  │  Rate: 10%  │  Amount: ₦25,000  │  Quota Credit: ₦250,000  │  [View Structure]│  │
│  │  ───────────────────────────────────────────────────────────────────────  │  │
│  │  TABS: [Activities] [Competitors] [Documents] [Email History]             │  │
│  │  ───────────────────────────────────────────────────────────────────────  │  │
│  │  QUICK ACTIONS:                                                             │  │
│  │  [+ Log Activity] [Send Quote] [Schedule Follow-up] [Request Approval]    │  │
│  └───────────────────────────────────────────────────────────────────────────┘  │
│                                                                                 │
│  USER ACTIONS:                                                                  │
│  ────────────                                                                   │
│  1. Click [Move Stage]                → Open stage change modal                 │
│                                        • Select new stage                       │
│                                        • Update probability                    │
│                                        • Add reason notes                      │
│                                        • Save → Update deal + log activity      │
│                                                                                 │
│  2. Click [Close Won]                 → Open close deal modal                   │
│                                        • Enter final value                      │
│                                        • Select won reason                      │
│                                        • Save → Mark as won + update metrics    │
│                                                                                 │
│  3. Click [Close Lost]                → Open close deal modal                   │
│                                        • Select lost reason                     │
│                                        • Add competitor info                    │
│                                        • Save → Mark as lost + update metrics   │
│                                                                                 │
│  4. Edit products                     → Add/edit/remove line items               │
│                                        • Auto-recalculate totals                │
│                                        • Update commission calculation         │
│                                                                                 │
│  5. Click [Request Approval]          → Create approval request                 │
│                                        • For discounts above threshold          │
│                                        • For special terms                      │
│                                        • Route to manager for approval         │
│                                                                                 │
└─────────────────────────────────────────────────────────────────────────────────┘
```

### 6.3 Deal Stage Change Flow

```
┌─────────────────────────────────────────────────────────────────────────────────┐
│                          DEAL STAGE CHANGE FLOW                                  │
├─────────────────────────────────────────────────────────────────────────────────┤
│                                                                                 │
│  TRIGGER: From Deal Detail → Click [Move Stage] or Drag in Pipeline            │
│                                                                                 │
│  MODAL:                                                                         │
│  ┌───────────────────────────────────────────────────────────────────────────┐  │
│  │  Change Deal Stage                                                          │  │
│  │  ┌────────────────────────────────────────────────────────────────────┐  │  │
│  │  │  Deal: D-2501 - Enterprise Software License                        │  │  │
│  │  │  Current Stage: Negotiation (75%)                                   │  │  │
│  │  │                                                                      │  │  │
│  │  │  Select New Stage:                                                  │  │  │
│  │  │  ┌────────────────────────────────────────────────────────────────┤│  │  │
│  │  │  │ ○ New (0%)                                                      ││  │  │
│  │  │  │ ○ Qualified (20%)                                               ││  │  │
│  │  │  │ ○ Proposal (50%)                                                ││  │  │
│  │  │  │ ● Negotiation (75%)  ← Current                                   ││  │  │
│  │  │  │ ○ Closing (90%)                                                 ││  │  │
│  │  │  │ ○ Won (100%)                                                    ││  │  │
│  │  │  │ ○ Lost (0%)                                                     ││  │  │
│  │  │  └────────────────────────────────────────────────────────────────┘│  │  │
│  │  │                                                                      │  │  │
│  │  │  Probability: [75%] (auto-updated based on stage)                    │  │  │
│  │  │  Expected Close Date: [2025-02-15]                                   │  │  │
│  │  │                                                                      │  │  │
│  │  │  Reason for stage change (optional):                                 │  │  │
│  │  │  ┌────────────────────────────────────────────────────────────────┐  │  │
│  │  │  │ Customer approved terms. Ready to finalize contract.           │  │  │
│  │  │  └────────────────────────────────────────────────────────────────┘  │  │
│  │  │                                                                      │  │  │
│  │  │  Notify owner (John Doe): [✓]                                       │  │  │
│  │  │  Notify deal followers: [✓]                                         │  │  │
│  │  │                                                                      │  │  │
│  │  │                                               [Cancel]  [Move Stage] │  │  │
│  │  └────────────────────────────────────────────────────────────────────┘  │  │
│  └───────────────────────────────────────────────────────────────────────────┘  │
│                                                                                 │
│  FLOW AFTER SUBMISSION:                                                         │
│  1. PUT /deals/:id/stage                                                        │
│  2. Response: Updated Deal                                                      │
│  3. Create activity record for stage change                                     │
│  4. Send notifications to owner and followers                                   │
│  5. Broadcast via WebSocket: /topic/deals/:dealId                               │
│  6. Show success toast: "Deal moved to Closing"                                 │
│  7. Refresh deal detail view                                                    │
│  8. Update pipeline view for all viewers                                        │
│                                                                                 │
└─────────────────────────────────────────────────────────────────────────────────┘
```

### 6.4 Deal Approval Flow

```
┌─────────────────────────────────────────────────────────────────────────────────┐
│                          DEAL APPROVAL FLOW                                      │
├─────────────────────────────────────────────────────────────────────────────────┤
│                                                                                 │
│  SCENARIO: Sales rep requests approval for discount above threshold              │
│                                                                                 │
│  REQUESTER FLOW (Sales Rep):                                                     │
│  ┌───────────────────────────────────────────────────────────────────────────┐  │
│  │  1. From Deal Detail → Click [Request Approval]                            │  │
│  │  2. Open Request Approval Modal:                                           │  │
│  │     ┌────────────────────────────────────────────────────────────────────┐  │  │
│  │     │  Request Deal Approval                                             │  │  │
│  │     │  Deal: D-2501 - Enterprise Software License                        │  │  │
│  │     │                                                                      │  │  │
│  │     │  Request Type: [Discount ▼]                                         │  │  │
│  │     │  • Discount                                                       │  │  │
│  │     │  • Extension                                                      │  │  │
│  │     │  • Special Terms                                                  │  │  │
│  │     │                                                                      │  │  │
│  │     │  Current Value: ₦250,000                                            │  │  │
│  │     │  Requested Value: ₦225,000 (10% discount)                           │  │  │
│  │     │  Discount: 10%                                                      │  │  │
│  │     │                                                                      │  │  │
│  │     │  Reason:                                                             │  │  │
│  │     │  ┌────────────────────────────────────────────────────────────────┐  │  │
│  │     │  │ Customer requested discount for multi-year commitment.         │  │  │
│  │     │  │ Competitive situation with XYZ Corp.                           │  │  │
│  │     │  └────────────────────────────────────────────────────────────────┘  │  │
│  │     │                                                                      │  │  │
│  │     │  Submit to: [Sales Director ▼]                                      │  │  │
│  │     │                                                                      │  │  │
│  │     │                                               [Cancel]  [Submit]   │  │  │
│  │     └────────────────────────────────────────────────────────────────────┘  │  │
│  │  3. POST /deals/:id/approval                                                 │
│  │  4. Show success toast                                                      │
│  │  5. Deal status shows "Pending Approval"                                    │  │
│  └───────────────────────────────────────────────────────────────────────────┘  │
│                                                                                 │
│  APPROVER FLOW (Sales Director):                                                 │
│  ┌───────────────────────────────────────────────────────────────────────────┐  │
│  │  1. Receive notification: "Approval requested for Deal D-2501"            │  │
│  │  2. Navigate to /deals/approvals                                          │  │
│  │  3. View approval request:                                                  │  │
│  │     ┌────────────────────────────────────────────────────────────────────┐  │  │
│  │     │  Approval Requests                                                   │  │  │
│  │     │  ┌────────────────────────────────────────────────────────────────┤│  │  │
│  │     │  │ Request #A-001                                                   ││  │  │
│  │     │  │ Deal: D-2501 - Enterprise Software License                      ││  │  │
│  │     │  │ Type: Discount Request                                           ││  │  │
│  │     │  │ Current: ₦250,000 → Requested: ₦225,000 (10% off)                ││  │  │
│  │     │  │ Requested by: John Doe                                           ││  │  │
│  │     │  │ Reason: Customer requested discount for multi-year...           ││  │  │
│  │     │  │ Requested: Feb 8, 2025                                            ││  │  │
│  │     │  │                                                                    ││  │  │
│  │     │  │ [View Deal]  [Approve]  [Reject]                                  ││  │  │
│  │     │  └────────────────────────────────────────────────────────────────┘│  │  │
│  │     │                                                                      │  │  │
│  │     │  [Approve] Actions:                                                  │  │
│  │     │  ┌────────────────────────────────────────────────────────────────┐  │  │
│  │     │  │  Approve this request?                                           │  │  │
│  │     │  │  Add notes (optional):                                          │  │  │
│  │     │  │  ┌────────────────────────────────────────────────────────────┐  │  │
│  │     │  │  │ Approved. Ensure multi-year commitment is secured.         │  │  │
│  │     │  │  └────────────────────────────────────────────────────────────┘  │  │  │
│  │     │  │                                               [Cancel]  [Approve] │  │  │
│  │     │  └────────────────────────────────────────────────────────────────┘  │  │
│  │     │                                                                      │  │  │
│  │     │  [Reject] Actions:                                                   │  │  │
│  │     │  ┌────────────────────────────────────────────────────────────────┐  │  │
│  │     │  │  Reject this request?                                            │  │  │
│  │     │  │  Reason (required):                                              │  │  │
│  │     │  │  ┌────────────────────────────────────────────────────────────┐  │  │
│  │     │  │  │ Discount exceeds approval authority.                      │  │  │
│  │     │  │  │ Please escalate to VP of Sales.                            │  │  │
│  │     │  │  └────────────────────────────────────────────────────────────┘  │  │
│  │     │  │                                               [Cancel]  [Reject] │  │  │
│  │     │  └────────────────────────────────────────────────────────────────┘  │  │
│  │     └────────────────────────────────────────────────────────────────────┘  │  │
│  │  4. POST /approvals/:id/approve or /approvals/:id/reject                      │
│  │  5. Notify requester of outcome                                            │
│  └───────────────────────────────────────────────────────────────────────────┘  │
│                                                                                 │
│  RESULT:                                                                        │
│  • If approved: Deal discount applied, deal can proceed to closing             │
│  • If rejected: Original value maintained, requester notified                  │
│                                                                                 │
└─────────────────────────────────────────────────────────────────────────────────┘
```

---

## 7. ORDERS FLOWS

### 7.1 Orders List Flow

```
┌─────────────────────────────────────────────────────────────────────────────────┐
│                              ORDERS LIST FLOW                                    │
├─────────────────────────────────────────────────────────────────────────────────┤
│                                                                                 │
│  NAVIGATION: /orders                                                             │
│                                                                                 │
│  PAGE COMPONENTS:                                                               │
│  ┌───────────────────────────────────────────────────────────────────────────┐  │
│  │  HEADER: Sales Team > Orders                                               │  │
│  │  TABS: [📦 All Orders] [⏳ Pending] [🚚 Shipped] [✅ Delivered] [❌ Cancelled]│  │
│  │  FILTERS:                                                                  │  │
│  │  ┌────────────────────────────────────────────────────────────────────┐  │  │
│  │  │ 🔍 Search  │ Status: [All ▼]  │ Customer: [All ▼]  │ Date: [All ▼]    │  │  │
│  │  └────────────────────────────────────────────────────────────────────┘  │  │
│  │  ACTIONS: [+ Create Order] [Import] [Export]                               │  │
│  │  ───────────────────────────────────────────────────────────────────────  │  │
│  │  ORDERS TABLE:                                                             │  │
│  │  ┌────────────────────────────────────────────────────────────────────┐  │  │
│  │  │ Order # │ Customer │ Status │ Total │ Date │ Ship │ Owner │ Actions│  │  │
│  │  ├────────────────────────────────────────────────────────────────────┤  │  │
│  │  │ SO-2501 │ Acme     │Shipped │₦268K │Feb 8 │ ✓    │ John  │ [View] │  │  │
│  │  │ SO-2500 │ TechCorp │Pending │₦180K │Feb 7 │ ⏳   │ Jane  │ [View] │  │  │
│  │  │ SO-2498 │ Global   │Delivered│₦540K │Feb 5 │ ✓    │ Bob   │ [View] │  │  │
│  │  └────────────────────────────────────────────────────────────────────┘  │  │
│  └───────────────────────────────────────────────────────────────────────────┘  │
│                                                                                 │
│  USER ACTIONS:                                                                  │
│  ────────────                                                                   │
│  1. Click [+ Create Order]           → Open create order modal                  │
│                                        • Select customer (or create new)        │
│                                        • Copy from deal (optional)              │
│                                        • Add products/services                  │
│                                        • Set shipping/billing addresses          │
│                                        • Save → Create order + redirect         │
│                                                                                 │
│  2. Click order row                   → Navigate to /orders/:orderId            │
│                                                                                 │
│  3. Click tab                        → Filter orders by status                  │
│                                        • Pending, Shipped, Delivered, Cancelled │
│                                                                                 │
│  4. Apply filters                    → Update table with filtered orders         │
│                                        • Status, customer, date range          │
│                                        • Text search across fields             │
│                                                                                 │
└─────────────────────────────────────────────────────────────────────────────────┘
```

### 7.2 Order Detail Flow

```
┌─────────────────────────────────────────────────────────────────────────────────┐
│                              ORDER DETAIL FLOW                                   │
├─────────────────────────────────────────────────────────────────────────────────┤
│                                                                                 │
│  NAVIGATION: /orders/:orderId                                                    │
│                                                                                 │
│  PAGE COMPONENTS:                                                               │
│  ┌───────────────────────────────────────────────────────────────────────────┐  │
│  │  HEADER: [< Back to Orders]  │  Order #SO-2501  │  Status: Shipped 🟢      │  │
│  │  ACTIONS: [✏️ Edit] [📄 PDF] [Send Invoice] [Cancel Order]                    │  │
│  │  ───────────────────────────────────────────────────────────────────────  │  │
│  │  ┌─────────────────────────────────────┐ ┌──────────────────────────────┐  │  │
│  │  │  ORDER INFORMATION                  │ │  SHIPPING INFO                │  │
│  │  │  • Order #: SO-2501                  │ │  • Method: Standard            │  │
│  │  │  • Customer: Acme Industries         │ │  • Tracking #: 1Z999AA1...     │  │
│  │  │  • Order Date: Feb 8, 2025           │ │  • Expected: Feb 15            │  │
│  │  │  • Total: ₦268,750                   │ │  • Actual: Feb 14              │  │
│  │  │  • Currency: NGN                     │ │  • Carrier: DHL                │  │
│  │  │  • Owner: John Doe                   │ │  [Track Package]              │  │
│  │  └─────────────────────────────────────┘ └──────────────────────────────┘  │  │
│  │  ───────────────────────────────────────────────────────────────────────  │  │
│  │  LINE ITEMS:                                                               │  │
│  │  ┌────────────────────────────────────────────────────────────────────┐  │  │
│  │  │ Product          │ Description        │ Qty │ Unit Price │ Total    │  │  │
│  │  ├─────────────────┼────────────────────┼─────┼────────────┼─────────┤  │  │
│  │  │ Software License │ Enterprise Plan   │  1  │ ₦200,000   │ ₦200,000│  │  │
│  │  │ Implementation   │ Setup & Training  │  1  │ ₦50,000    │  ₦50,000│  │  │
│  │  ├─────────────────┴────────────────────┴─────┴────────────┴─────────┤  │  │
│  │  │                                                  Subtotal: ₦250,000│  │  │
│  │  │                                                  Discount: ₦0     │  │  │
│  │  │                                                  Tax: ₦18,750     │  │  │
│  │  │                                                  TOTAL: ₦268,750  │  │  │
│  │  └────────────────────────────────────────────────────────────────────┘  │  │
│  │  ───────────────────────────────────────────────────────────────────────  │  │
│  │  BILLING & SHIPPING:                                                       │  │
│  │  ┌─────────────────────────────────────┐ ┌──────────────────────────────┐  │  │
│  │  │  BILLING ADDRESS                    │ │  SHIPPING ADDRESS             │  │
│  │  │  Acme Industries                   │ │  Acme Industries             │  │  │
│  │  │  Plot 123, Adetokunbo Ademola St.  │ │  Plot 123, Adetokunbo Ademola │  │  │
│  │  │  Victoria Island, Lagos             │ │  Victoria Island, Lagos       │  │  │
│  │  │  Nigeria, 101241                    │ │  Nigeria, 101241              │  │  │
│  │  │                                      │ │                                │  │
│  │  │  Payment Terms: Net 30               │ │  [Edit Address]              │  │
│  │  │  Payment Status: Pending             │ │                                │  │  │
│  │  │  Paid: ₦0                            │ │                                │  │
│  │  └─────────────────────────────────────┘ └──────────────────────────────┘  │  │
│  │  ───────────────────────────────────────────────────────────────────────  │  │
│  │  TABS: [Activities] [Documents] [Email History] [Related Deals]             │  │
│  │  ───────────────────────────────────────────────────────────────────────  │  │
│  │  QUICK ACTIONS:                                                             │  │
│  │  [Record Payment] [Send Reminder] [Generate Invoice] [Upload Document]     │  │
│  └───────────────────────────────────────────────────────────────────────────┘  │
│                                                                                 │
│  USER ACTIONS:                                                                  │
│  ────────────                                                                   │
│  1. Click [Track Package]            → Open carrier tracking (new tab)          │
│                                                                                 │
│  2. Click [Send Invoice]             → Generate and email invoice PDF           │
│                                        • Select email template                  │
│                                        • Send → Log as activity                │
│                                                                                 │
│  3. Click [Record Payment]            → Open payment recording modal            │
│                                        • Enter payment amount                  │
│                                        • Select payment method                 │
│                                        • Update payment status                 │
│                                                                                 │
│  4. Click [Cancel Order]              → Confirm cancellation                    │
│                                        • Select cancellation reason            │
│                                        • Process refund if applicable          │
│                                        • Update inventory                      │
│                                                                                 │
└─────────────────────────────────────────────────────────────────────────────────┘
```

---

## 8. CUSTOMER FLOWS

### 8.1 Customer Directory Flow

```
┌─────────────────────────────────────────────────────────────────────────────────┐
│                           CUSTOMER DIRECTORY FLOW                                 │
├─────────────────────────────────────────────────────────────────────────────────┤
│                                                                                 │
│  NAVIGATION: /customers                                                          │
│                                                                                 │
│  PAGE COMPONENTS:                                                               │
│  ┌───────────────────────────────────────────────────────────────────────────┐  │
│  │  HEADER: Sales Team > Customers                                            │  │
│  │  TABS: [🤝 All Customers] [🏢 Corporate] [👤 Individual] [⭐ Active]        │  │
│  │  FILTERS:                                                                  │  │
│  │  ┌────────────────────────────────────────────────────────────────────┐  │  │
│  │  │ 🔍 Search  │ Type: [All ▼]  │ Tier: [All ▼]  │ Industry: [All ▼]    │  │  │
│  │  └────────────────────────────────────────────────────────────────────┘  │  │
│  │  ACTIONS: [+ Add Customer] [Import] [Export]                               │  │
│  │  ───────────────────────────────────────────────────────────────────────  │  │
│  │  CUSTOMERS TABLE:                                                           │  │
│  │  ┌────────────────────────────────────────────────────────────────────┐  │  │
│  │  │ Customer │ Type │ Industry │ Status │ Tier │ Owner │ ARV   │ Purch. │  │  │
│  │  ├────────────────────────────────────────────────────────────────────┤  │  │
│  │  │ 🏢 Acme   │ Corp│ Tech     │ Active │ Plat │ John  │ ₦250K │ 12    │  │  │
│  │  │ Industries│    │          │   🟢   │      │       │ /year  │       │  │  │
│  │  ├────────────────────────────────────────────────────────────────────┤  │  │
│  │  │ 🏢 Tech   │ Corp│ Retail   │ Active │ Gold │ Jane  │ ₦180K │ 8     │  │  │
│  │  │ Nigeria  │    │          │   🟢   │      │       │ /year  │       │  │  │
│  │  ├────────────────────────────────────────────────────────────────────┤  │  │
│  │  │ 👤 John   │ Ind │ N/A      │ Active │ Silv │ Sarah │ ₦45K  │ 3     │  │  │
│  │  │ Okafor   │    │          │   🟢   │      │       │ total  │       │  │  │
│  │  └────────────────────────────────────────────────────────────────────┘  │  │
│  └───────────────────────────────────────────────────────────────────────────┘  │
│                                                                                 │
│  USER ACTIONS:                                                                  │
│  ────────────                                                                   │
│  1. Click customer row               → Navigate to /customers/:customerId    │
│                                                                                 │
│  2. Click [+ Add Customer]           → Open create customer modal              │
│                                        • Select customer type (Corp/Ind)       │
│                                        • Enter customer information            │
│                                        • Assign owner and territory            │
│                                        • Set payment terms and credit limit    │
│                                        • Save → Create customer + redirect     │
│                                                                                 │
│  3. Click tab                        → Filter customers by tab criteria          │
│                                        • All, Corporate, Individual, Active    │
│                                                                                 │
│  4. Apply filters                    → Update table with filtered customers      │
│                                        • Type, tier, industry, owner           │
│                                        • Text search across fields             │
│                                                                                 │
└─────────────────────────────────────────────────────────────────────────────────┘
```

### 8.2 Customer Profile Flow

```
┌─────────────────────────────────────────────────────────────────────────────────┐
│                            CUSTOMER PROFILE FLOW                                  │
├─────────────────────────────────────────────────────────────────────────────────┤
│                                                                                 │
│  NAVIGATION: /customers/:customerId                                              │
│                                                                                 │
│  PAGE COMPONENTS:                                                               │
│  ┌───────────────────────────────────────────────────────────────────────────┐  │
│  │  HEADER: [< Back]  │  Acme Industries  │  Status: Active 🟢  │ [Edit]...   │  │
│  │  ───────────────────────────────────────────────────────────────────────  │  │
│  │  ┌─────────────────────────────────────┐ ┌──────────────────────────────┐  │  │
│  │  │  CUSTOMER OVERVIEW                  │ │  ACCOUNT SUMMARY              │  │
│  │  │  • Type: Corporate                  │ │  • Customer Tier: Platinum    │  │
│  │  │  • Industry: Technology             │ │  • Customer Since: Jan 2023   │  │
│  │  │  • Website: acme-ng.com              │ │  • Lifetime Value: ₦850K      │  │
│  │  │  • Employees: 250-500                │ │  • Annual Recurring: ₦250K    │  │
│  │  │  • Account Owner: John Doe           │ │  • Total Purchases: 12        │  │
│  │  │  • Territory: South West             │ │  • Avg Order Value: ₦70.8K     │  │
│  │  │  • Payment Terms: Net 30             │ │  • Payment Terms: Net 30       │  │
│  │  │  • Credit Limit: ₦500,000            │ │  • Credit Limit: ₦500,000     │  │
│  │  │  • Outstanding Balance: ₦0           │ │  • Outstanding: ₦0            │  │
│  │  └─────────────────────────────────────┘ └──────────────────────────────┘  │  │
│  │  ───────────────────────────────────────────────────────────────────────  │  │
│  │  TABS: [Overview] [Deals] [Orders] [Activities] [Documents]                 │  │
│  │  ───────────────────────────────────────────────────────────────────────  │  │
│  │  TAB CONTENT (based on selection):                                         │  │
│  │  • Overview: Customer details, contacts, metrics, custom fields            │  │
│  │  • Deals: Active and closed deals, create new deal from customer           │  │
│  │  • Orders: Order history, create new order from customer                   │  │
│  │  • Activities: Timeline of all interactions with customer                   │  │
│  │  • Documents: Uploaded contracts, proposals, invoices                      │  │
│  │  ───────────────────────────────────────────────────────────────────────  │  │
│  │  QUICK ACTIONS:                                                             │  │
│  │  [+ Create Deal] [+ Create Order] [Log Call] [Send Email] [Schedule Visit]  │  │
│  └───────────────────────────────────────────────────────────────────────────┘  │
│                                                                                 │
│  USER ACTIONS:                                                                  │
│  ────────────                                                                   │
│  1. Click [+ Create Deal]             → Open create deal modal                  │
│                                        • Pre-fill customer information          │
│                                        • Add products/services                  │
│                                        • Save → Create deal linked to customer  │
│                                                                                 │
│  2. Click [+ Create Order]            → Open create order modal                 │
│                                        • Pre-fill customer and billing info     │
│                                        • Add products/services                  │
│                                        • Save → Create order linked to customer │
│                                                                                 │
│  3. Click [Send Email]                → Open email composer                     │
│                                        • Pre-fill recipient                     │
│                                        • Select from template library          │
│                                        • Send → Log as customer activity        │
│                                                                                 │
│  4. Click deal in Deals tab           → Navigate to /deals/:dealId             │
│                                                                                 │
│  5. Click order in Orders tab         → Navigate to /orders/:orderId           │
│                                                                                 │
│  6. Click activity in timeline       → Open activity detail                    │
│                                        • View full notes/attachments            │
│                                        • Edit if owned by current user          │
│                                                                                 │
└─────────────────────────────────────────────────────────────────────────────────┘
```

---

## 9. PERFORMANCE FLOWS

### 9.1 Sales Performance Flow

```
┌─────────────────────────────────────────────────────────────────────────────────┐
│                           SALES PERFORMANCE FLOW                                  │
├─────────────────────────────────────────────────────────────────────────────────┤
│                                                                                 │
│  NAVIGATION: /performance/sales                                                  │
│                                                                                 │
│  PAGE COMPONENTS:                                                               │
│  ┌───────────────────────────────────────────────────────────────────────────┐  │
│  │  HEADER: Performance > Sales Performance                                   │  │
│  │  CONTROLS: [Period: This Month ▼] [Compare: Previous Period ▼] [Export]    │  │
│  │  ───────────────────────────────────────────────────────────────────────  │  │
│  │  SUMMARY CARDS:                                                            │  │
│  │  ┌─────────────┐ ┌─────────────┐ ┌─────────────┐ ┌─────────────┐           │  │
│  │  │ Total       │ │ Pipeline    │ │ Deals       │ │ Win Rate    │           │  │
│  │  │ Revenue     │ │ Value       │ │ Closed      │ │             │           │  │
│  │  │ ₦1.2M       │ │ ₦3.5M       │ │ 127         │ │ 32%         │           │  │
│  │  │ +12% vs     │ │ +8% vs      │ │ +15 vs      │ │ +3% vs      │           │  │
│  │  │ last month  │ │ last month  │ │ last month  │ │ last month  │           │  │
│  │  └─────────────┘ └─────────────┘ └─────────────┘ └─────────────┘           │  │
│  │  ───────────────────────────────────────────────────────────────────────  │  │
│  │  CHARTS:                                                                  │  │
│  │  ┌─────────────────────────────────────┐ ┌───────────────────────────────┐ │  │
│  │  │  Revenue Trend (Line Chart)          │ │  Deals by Stage (Funnel)      │ │  │
│  │  │  Revenue vs Target                   │ │  Conversion rates              │ │  │
│  │  └─────────────────────────────────────┘ └───────────────────────────────┘ │  │
│  │  ┌─────────────────────────────────────┐ ┌───────────────────────────────┐ │  │
│  │  │  Revenue by Territory (Bar Chart)    │ │  Top Products (Pie Chart)     │ │  │
│  │  │  Territory comparison                │ │  Product mix breakdown         │ │  │
│  │  └─────────────────────────────────────┘ └───────────────────────────────┘ │  │
│  │  ───────────────────────────────────────────────────────────────────────  │  │
│  │  TEAM PERFORMANCE TABLE:                                                 │  │
│  │  ┌────────────────────────────────────────────────────────────────────┐  │  │
│  │  │ Member   │ Dept  │ Territory │ Revenue │ Quota │ Attain │ Deals │ Win%│  │  │
│  │  ├────────────────────────────────────────────────────────────────────┤  │  │
│  │  │ John Doe │ Ent   │ SW        │ ₦250K  │₦200K  │ 125%   │ 15    │ 38% │  │  │
│  │  │ Jane Smith│ SMB   │ SE        │ ₦180K  │₦150K  │ 120%   │ 22    │ 35% │  │  │
│  │  │ Bob J.   │ Retail│ NW        │ ₦145K  │₦150K  │  97%   │ 28    │ 30% │  │  │
│  │  └────────────────────────────────────────────────────────────────────┘  │  │
│  └───────────────────────────────────────────────────────────────────────────┘  │
│                                                                                 │
│  USER ACTIONS:                                                                  │
│  ────────────                                                                   │
│  1. Change period selector            → Reload all data for new period          │
│                                        • Update charts and tables                │
│                                                                                 │
│  2. Enable comparison                → Show period-over-period comparison        │
│                                        • Display variance percentages            │
│                                                                                 │
│  3. Click team member row             → Navigate to /team/:memberId             │
│                                        • View detailed performance              │
│                                                                                 │
│  4. Click chart segment               → Drill down into segment                  │
│                                        • Filter data by selection               │
│                                                                                 │
│  5. Click [Export]                    → Generate PDF/Excel report                │
│                                        • Include all charts and tables           │
│                                                                                 │
└─────────────────────────────────────────────────────────────────────────────────┘
```

### 9.2 Team Performance Flow

```
┌─────────────────────────────────────────────────────────────────────────────────┐
│                           TEAM PERFORMANCE FLOW                                   │
├─────────────────────────────────────────────────────────────────────────────────┤
│                                                                                 │
│  NAVIGATION: /performance/team                                                  │
│                                                                                 │
│  PAGE COMPONENTS:                                                               │
│  ┌───────────────────────────────────────────────────────────────────────────┐  │
│  │  HEADER: Performance > Team Performance                                    │  │
│  │  CONTROLS: [Period: This Month ▼] [Department: All ▼] [Territory: All ▼]  │  │
│  │  ───────────────────────────────────────────────────────────────────────  │  │
│  │  SUMMARY METRICS:                                                          │  │
│  │  ┌─────────────┐ ┌─────────────┐ ┌─────────────┐ ┌─────────────┐           │  │
│  │  │ Team        │ │ Quota       │ │ Avg Deal    │ │ Team Win    │           │  │
│  │  │ Revenue     │ │ Attainment │ │ Size        │ │ Rate        │           │  │
│  │  │ ₦1.2M       │ │ 95%        │ │ ₦48K        │ │ 32%         │           │  │
│  │  └─────────────┘ └─────────────┘ └─────────────┘ └─────────────┘           │  │
│  │  ───────────────────────────────────────────────────────────────────────  │  │
│  │  TEAM PERFORMANCE CHART:                                                  │  │
│  │  ┌────────────────────────────────────────────────────────────────────┐  │  │
│  │  │  Revenue by Team Member (Horizontal Bar Chart)                        │  │
│  │  │  ┌────────────────────────────────────────────────────────────────┤  │  │
│  │  │  │ John Doe   ████████████████████████ ₦250K (125% of quota)        │  │  │
│  │  │  │ Jane Smith ████████████████████     ₦180K (120% of quota)        │  │  │
│  │  │  │ Bob J.     ████████████████         ₦145K (97% of quota)         │  │  │
│  │  │  │ Sarah W.   ██████████████           ₦135K (68% of quota)         │  │  │
│  │  │  └────────────────────────────────────────────────────────────────┘  │  │
│  │  └────────────────────────────────────────────────────────────────────┘  │  │
│  │  ───────────────────────────────────────────────────────────────────────  │  │
│  │  ACTIVITY METRICS:                                                         │  │
│  │  ┌────────────────────────────────────────────────────────────────────┐  │  │
│  │  │  Total Calls: 1,245  │  Total Emails: 856  │  Meetings: 234           │  │
│  │  │  Demos: 178          │  Proposals: 89       │  Activities: 2,514       │  │
│  │  └────────────────────────────────────────────────────────────────────┘  │  │
│  │  ───────────────────────────────────────────────────────────────────────  │  │
│  │  TEAM MEMBERS LIST (with performance):                                     │  │
│  │  [Same as Team Directory but with additional performance columns]         │  │
│  └───────────────────────────────────────────────────────────────────────────┘  │
│                                                                                 │
│  USER ACTIONS:                                                                  │
│  ────────────                                                                   │
│  1. Apply filters                    → Filter by department/territory           │
│                                        • Update metrics and charts              │
│                                                                                 │
│  2. Click team member               → Navigate to /team/:memberId             │
│                                        • View detailed performance              │
│                                                                                 │
│  3. Hover over chart bars           → Show detailed tooltips                    │
│                                        • Revenue, quota, attainment             │
│                                        • Deals closed, win rate                │
│                                                                                 │
└─────────────────────────────────────────────────────────────────────────────────┘
```

---

## 10. TERRITORY & QUOTA FLOWS

### 10.1 Territory Management Flow

```
┌─────────────────────────────────────────────────────────────────────────────────┐
│                        TERRITORY MANAGEMENT FLOW                                  │
├─────────────────────────────────────────────────────────────────────────────────┤
│                                                                                 │
│  NAVIGATION: /territory                                                          │
│                                                                                 │
│  PAGE COMPONENTS:                                                               │
│  ┌───────────────────────────────────────────────────────────────────────────┐  │
│  │  HEADER: Territory & Quota > Territory Management                         │  │
│  │  ACTIONS: [+ Add Territory] [Configure Stages]                             │  │
│  │  ───────────────────────────────────────────────────────────────────────  │  │
│  │  TERRITORY OVERVIEW MAP:                                                   │  │
│  │  ┌────────────────────────────────────────────────────────────────────┐  │  │
│  │  │                 [NIGERIA - REGIONAL MAP]                           │  │  │
│  │  │  • Interactive map showing all territories                          │  │  │
│  │  │  • Color-coded by quota attainment                                   │  │  │
│  │  │  • Click territory to view details                                    │  │  │
│  │  └────────────────────────────────────────────────────────────────────┘  │  │
│  │  ───────────────────────────────────────────────────────────────────────  │  │
│  │  TERRITORY LIST:                                                            │  │
│  │  ┌────────────────────────────────────────────────────────────────────┐  │  │
│  │  │ Territory │ Owner     │ Revenue │ Quota │ Attain │ Deals │ Actions│  │  │
│  │  ├────────────────────────────────────────────────────────────────────┤  │  │
│  │  │ South West│ John Doe  │ ₦350K  │₦300K  │ 117%   │ 45    │ [View] │  │  │
│  │  │ South East│ Jane Smith│ ₦280K  │₦300K  │  93%   │ 38    │ [View] │  │  │
│  │  │ North Ctr │ Sarah W.  │ ₦220K  │₦250K  │  88%   │ 32    │ [View] │  │  │
│  │  │ North West│ Bob J.    │ ₦180K  │₦200K  │  90%   │ 28    │ [View] │  │  │
│  │  └────────────────────────────────────────────────────────────────────┘  │  │
│  └───────────────────────────────────────────────────────────────────────────┘  │
│                                                                                 │
│  USER ACTIONS:                                                                  │
│  ────────────                                                                   │
│  1. Click [+ Add Territory]          → Open create territory modal              │
│                                        • Enter territory name and code          │
│                                        • Define boundaries                     │
│                                        • Assign owner(s)                       │
│                                        • Set initial quota                     │
│                                        • Save → Create territory               │
│                                                                                 │
│  2. Click territory in list/map       → Navigate to /territory/:territoryId   │
│                                        • View territory details                 │
│                                        • View assigned reps                     │
│                                        • View performance metrics               │
│                                                                                 │
│  3. Click [View] actions             → Open territory detail drawer             │
│                                        • Quick view of metrics                  │
│                                        • Edit quota                            │
│                                        • Reassign owner                        │
│                                                                                 │
└─────────────────────────────────────────────────────────────────────────────────┘
```

### 10.2 Quota Management Flow

```
┌─────────────────────────────────────────────────────────────────────────────────┐
│                           QUOTA MANAGEMENT FLOW                                   │
├─────────────────────────────────────────────────────────────────────────────────┤
│                                                                                 │
│  NAVIGATION: /territory/quotas                                                   │
│                                                                                 │
│  PAGE COMPONENTS:                                                               │
│  ┌───────────────────────────────────────────────────────────────────────────┐  │
│  │  HEADER: Territory & Quota > Quota Management                             │  │
│  │  CONTROLS: [Period: 2025 ▼] [Type: Revenue ▼] [+ Set Quota]                │  │
│  │  ───────────────────────────────────────────────────────────────────────  │  │
│  │  QUOTA SUMMARY:                                                            │  │
│  │  ┌────────────────────────────────────────────────────────────────────┐  │  │
│  │  │ Total Team Quota: ₦1.5M  │  Current Attainment: ₦1.425M (95%)         │  │  │
│  │  │ Remaining: ₦75,000     │  Days Left: 21                               │  │  │
│  │  └────────────────────────────────────────────────────────────────────┘  │  │
│  │  ───────────────────────────────────────────────────────────────────────  │  │
│  │  QUOTA ASSIGNMENTS TABLE:                                                 │  │
│  │  ┌────────────────────────────────────────────────────────────────────┐  │  │
│  │  │ Assignee │ Type   │ Annual │ Q1    │ Q2    │ Q3    │ Q4    │ Actions│  │  │
│  │  ├────────────────────────────────────────────────────────────────────┤  │  │
│  │  │ John Doe │ Individual│₦300K │₦75K │₦75K │₦75K │₦75K │ [Edit]│  │  │
│  │  │          │        │ 117%   │105% │122% │118% │123% │       │  │  │
│  │  ├────────────────────────────────────────────────────────────────────┤  │  │
│  │  │ Jane S.  │ Individual│₦300K │₦75K │₦75K │₦75K │₦75K │ [Edit]│  │  │
│  │  │          │        │ 93%    │ 88% │ 95% │ 92% │ 97% │       │  │  │
│  │  ├────────────────────────────────────────────────────────────────────┤  │  │
│  │  │ SW Team  │ Team   │₦600K  │₦150K│₦150K│₦150K│₦150K│ [Edit]│  │  │
│  │  │          │        │ 110%   │102% │115% │108% │115% │       │  │  │
│  │  └────────────────────────────────────────────────────────────────────┘  │  │
│  │  ───────────────────────────────────────────────────────────────────────  │  │
│  │  QUOTA TRACKING CHART:                                                     │  │
│  │  ┌────────────────────────────────────────────────────────────────────┐  │  │
│  │  │  Quota Attainment by Assignee (Horizontal Bar Chart)                  │  │
│  │  │  ┌────────────────────────────────────────────────────────────────┤  │  │
│  │  │  │ John Doe   [████████████████████████] 117%                     │  │  │
│  │  │  │ Jane Smith [████████████████████░░░]  93%                      │  │  │
│  │  │  │ Bob J.     [██████████████████░░░░░]  88%                      │  │  │
│  │  │  └────────────────────────────────────────────────────────────────┘  │  │
│  │  └────────────────────────────────────────────────────────────────────┘  │  │
│  └───────────────────────────────────────────────────────────────────────────┘  │
│                                                                                 │
│  USER ACTIONS:                                                                  │
│  ────────────                                                                   │
│  1. Click [+ Set Quota]               → Open create quota modal                  │
│                                        • Select assignee (individual/team)      │
│                                        • Set quota amount                      │
│                                        • Choose period (annual/quarterly)      │
│                                        • Set quarterly breakdown                │
│                                        • Save → Create quota                    │
│                                                                                 │
│  2. Click [Edit]                     → Open edit quota modal                    │
│                                        • Adjust quota amounts                   │
│                                        • Modify breakdown                      │
│                                        • Add adjustment reason                 │
│                                        • Save → Update quota                    │
│                                                                                 │
│  3. Click assignee row               → Navigate to assignee detail              │
│                                        • Individual → /team/:memberId           │
│                                        • Team → /performance/team               │
│                                                                                 │
└─────────────────────────────────────────────────────────────────────────────────┘
```

---

## 11. FORECASTING FLOWS

### 11.1 Revenue Forecast Flow

```
┌─────────────────────────────────────────────────────────────────────────────────┐
│                          REVENUE FORECAST FLOW                                    │
├─────────────────────────────────────────────────────────────────────────────────┤
│                                                                                 │
│  NAVIGATION: /forecasting/revenue                                                │
│                                                                                 │
│  PAGE COMPONENTS:                                                               │
│  ┌───────────────────────────────────────────────────────────────────────────┐  │
│  │  HEADER: Forecasting > Revenue Forecast                                    │  │
│  │  CONTROLS: [Period: Q1 2025 ▼] [Scenario: Likely ▼] [Generate Forecast]    │  │
│  │  ───────────────────────────────────────────────────────────────────────  │  │
│  │  FORECAST SUMMARY:                                                         │  │
│  │  ┌─────────────┐ ┌─────────────┐ ┌─────────────┐ ┌─────────────┐           │  │
│  │  │ Forecast    │ │ Weighted    │ │ Best Case   │ │ Worst Case  │           │  │
│  │  │ Revenue     │ │ Pipeline    │ │             │ │             │           │  │
│  │  │ ₦1.5M       │ │ ₦3.5M       │ │ ₦1.8M      │ │ ₦1.2M      │           │  │
│  │  │ Q1 2025     │ │ Available   │ │ +20%       │ │ -20%       │           │  │
│  │  └─────────────┘ └─────────────┘ └─────────────┘ └─────────────┘           │  │
│  │  ───────────────────────────────────────────────────────────────────────  │  │
│  │  FORECAST CHART:                                                          │  │
│  │  ┌────────────────────────────────────────────────────────────────────┐  │  │
│  │  │  Revenue Forecast Trend (Line Chart with Scenarios)                   │  │  │
│  │  │  ┌────────────────────────────────────────────────────────────────┤  │  │
│  │  │  │                                                                  │  │  │
│  │  │  │  ₦2M ┤    [Best Case] ──────────────────────                     │  │  │
│  │  │  │      \         ┌────────────  ₦1.8M ┤                            │  │  │
│  │  │  │       \     ┌─┘             [Forecast] ─────────                   │  │  │
│  │  │  │        \   ─┤               ₦1.5M ┤                              │  │  │
│  │  │  │         \─┘                                                         │  │  │
│  │  │  │  [Worst Case] ───────────                                              │  │  │
│  │  │  │                                                                  │  │  │
│  │  │  └──────────────────────────────────────────────────────────────────┤  │  │
│  │  │         Jan    Feb    Mar    Apr    May    Jun                        │  │  │
│  │  └────────────────────────────────────────────────────────────────────┘  │  │
│  │  ───────────────────────────────────────────────────────────────────────  │  │
│  │  FORECAST BY STAGE:                                                       │  │
│  │  ┌────────────────────────────────────────────────────────────────────┐  │  │
│  │  │ Stage        │ Count │ Value    │ Weighted │ % of Forecast │          │  │
│  │  ├────────────────────────────────────────────────────────────────────┤  │  │
│  │  │ Negotiating │ 10    │ ₦825K   │ ₦825K   │ 55%           │          │  │
│  │  │ Proposal    │ 18    │ ₦525K   │ ₦263K   │ 18%           │          │  │
│  │  │ Qualified   │ 25    │ ₦225K   │ ₦45K    │  3%           │          │  │
│  │  │ Total       │ 53    │ ₦1.575M │ ₦1.133M │               │          │  │
│  │  └────────────────────────────────────────────────────────────────────┘  │  │
│  │  ───────────────────────────────────────────────────────────────────────  │  │
│  │  FORECAST ACCURACY:                                                       │  │
│  │  ┌────────────────────────────────────────────────────────────────────┐  │  │
│  │  │  Last 3 Months Accuracy: 94.2%                                       │  │  │
│  │  │  • Feb: 95.1%  │  Jan: 93.8%  │  Dec: 93.7%                         │  │  │
│  │  └────────────────────────────────────────────────────────────────────┘  │  │
│  └───────────────────────────────────────────────────────────────────────────┘  │
│                                                                                 │
│  USER ACTIONS:                                                                  │
│  ────────────                                                                   │
│  1. Change period selector            → Regenerate forecast for new period      │
│                                                                                 │
│  2. Change scenario                  → Update chart with selected scenario       │
│                                        • Best, Likely, Worst, Custom            │
│                                                                                 │
│  3. Click [Generate Forecast]         → Open forecast options modal             │
│                                        • Include/exclude best/worst cases        │
│                                        • Use historical trends                 │
│                                        • Select forecast model                  │
│                                        • Generate → Show loading + update        │
│                                                                                 │
│  4. Click stage row                   → Filter deals by stage                    │
│                                        • Navigate to filtered deals list        │
│                                                                                 │
└─────────────────────────────────────────────────────────────────────────────────┘
```

---

## 12. COMMUNICATIONS FLOWS

### 12.1 Email Templates Flow

```
┌─────────────────────────────────────────────────────────────────────────────────┐
│                          EMAIL TEMPLATES FLOW                                     │
├─────────────────────────────────────────────────────────────────────────────────┤
│                                                                                 │
│  NAVIGATION: /communications/templates                                           │
│                                                                                 │
│  PAGE COMPONENTS:                                                               │
│  ┌───────────────────────────────────────────────────────────────────────────┐  │
│  │  HEADER: Communications > Email Templates                                  │  │
│  │  ACTIONS: [+ New Template] [Import] [Sync with Email Provider]             │  │
│  │  ───────────────────────────────────────────────────────────────────────  │  │
│  │  TEMPLATES LIST:                                                           │  │
│  │  ┌────────────────────────────────────────────────────────────────────┐  │  │
│  │  │ Template Name │ Category │ Subject         │ Last Used │ Actions  │  │  │
│  │  ├────────────────────────────────────────────────────────────────────┤  │  │
│  │  │ Lead Follow-up │ Leads  │ {Company} - Next │ Today     │ [Use]    │  │  │
│  │  │               │         │ Steps          │ 12 times  │ [Edit]   │  │  │
│  │  ├────────────────────────────────────────────────────────────────────┤  │  │
│  │  │ Proposal Sent │ Deals  │ Proposal for     │ Yesterday │ [Use]    │  │  │
│  │  │               │         │ {Deal Name}     │ 45 times  │ [Edit]   │  │  │
│  │  ├────────────────────────────────────────────────────────────────────┤  │  │
│  │  │ Meeting Req   │ General│ Meeting Request  │ 3 days    │ [Use]    │  │  │
│  │  │               │         │                 │ ago       │ [Edit]   │  │  │
│  │  └────────────────────────────────────────────────────────────────────┘  │  │
│  └───────────────────────────────────────────────────────────────────────────┘  │
│                                                                                 │
│  CREATE/EDIT TEMPLATE MODAL:                                                    │
│  ┌───────────────────────────────────────────────────────────────────────────┐  │
│  │  Email Template                                                            │  │
│  │  ┌────────────────────────────────────────────────────────────────────┐  │  │
│  │  │  Template Name: [Follow-up after demo                                 │  │  │
│  │  │  Category: [Deals ▼]                                                  │  │  │
│  │  │  Subject: [{Company}] - Re: Our discussion about {Product}          │  │  │
│  │  │                                                                      │  │  │
│  │  │  Body:                                                                │  │  │
│  │  │  ┌────────────────────────────────────────────────────────────────┐  │  │
│  │  │  │ Hi {FirstName},                                                │  │  │
│  │  │  │                                                                │  │  │
│  │  │  │ Thank you for taking the time to review our {Product}...      │  │  │
│  │  │  │                                                                │  │  │
│  │  │  │ [Rich text editor with formatting options]                   │  │  │
│  │  │  │                                                                │  │  │
│  │  │  │ Available variables: {FirstName}, {LastName}, {Company},     │  │  │
│  │  │  │ {Product}, {DealValue}, {MyName}, {MyTitle}...               │  │  │
│  │  │  └────────────────────────────────────────────────────────────────┘  │  │  │
│  │  │                                                                      │  │  │
│  │  │  Variables:                                                           │  │  │
│  │  │  [+ Add Variable] [Manage Variables]                                  │  │  │
│  │  │                                                                      │  │  │
│  │  │  Is Active: [✓]                                                       │  │  │
│  │  │                                                                      │  │  │
│  │  │                                               [Cancel]  [Save]       │  │  │
│  │  └────────────────────────────────────────────────────────────────────┘  │  │
│  └───────────────────────────────────────────────────────────────────────────┘  │
│                                                                                 │
│  USER ACTIONS:                                                                  │
│  ────────────                                                                   │
│  1. Click [+ New Template]           → Open create template modal               │
│                                                                                 │
│  2. Click [Use]                      → Open send email modal with template       │
│                                        • Pre-fill subject and body               │
│                                        • Replace variables with actual data     │
│                                        • Select recipients                      │
│                                        • Send → Log as activity                │
│                                                                                 │
│  3. Click [Edit]                     → Open edit template modal                  │
│                                        • Modify template content                │
│                                        • Update variables                      │
│                                        • Save → Update template                 │
│                                                                                 │
└─────────────────────────────────────────────────────────────────────────────────┘
```

---

## 13. SETTINGS FLOWS

### 13.1 Profile Settings Flow

```
┌─────────────────────────────────────────────────────────────────────────────────┐
│                           PROFILE SETTINGS FLOW                                   │
├─────────────────────────────────────────────────────────────────────────────────┤
│                                                                                 │
│  NAVIGATION: /settings/profile                                                   │
│                                                                                 │
│  PAGE COMPONENTS:                                                               │
│  ┌───────────────────────────────────────────────────────────────────────────┐  │
│  │  HEADER: Settings > Profile                                                │  │
│  │  TABS: [Profile] [Security] [Notifications] [Preferences]                   │  │
│  │  ───────────────────────────────────────────────────────────────────────  │  │
│  │  PROFILE TAB:                                                              │  │
│  │  ┌────────────────────────────────────────────────────────────────────┐  │  │
│  │  │  Personal Information                                                 │  │  │
│  │  │  ┌────────────────────────────────────────────────────────────────┤  │  │
│  │  │  │ Avatar: [Upload] [Remove]                              │  │  │
│  │  │  │                                              [Current Photo]   │  │  │
│  │  │  │ First Name: [John                                   ]             │  │  │
│  │  │  │ Last Name: [Doe                                     ]             │  │  │
│  │  │  │ Email: [john.doe@company.ng (read-only)       ]             │  │  │
│  │  │  │ Phone: [+234-XXX-XXXX                             ]             │  │  │
│  │  │  │ Department: [Enterprise Sales                    ▼]             │  │  │
│  │  │  │ Title: [Country Sales Director                   ▼]             │  │  │
│  │  │  │ Timezone: [Africa/Lagos                           ▼]             │  │  │
│  │  │  │                                                                  │  │  │
│  │  │  │                                                  [Save Changes] │  │  │
│  │  │  └────────────────────────────────────────────────────────────────┘  │  │
│  │  └────────────────────────────────────────────────────────────────────┘  │  │
│  │  ───────────────────────────────────────────────────────────────────────  │  │
│  │  SECURITY TAB:                                                             │  │
│  │  ┌────────────────────────────────────────────────────────────────────┐  │  │
│  │  │  Change Password                                                       │  │  │
│  │  │  ┌────────────────────────────────────────────────────────────────┤  │  │
│  │  │  │ Current Password: [•••••••••••••••••••••••••             ]             │  │  │
│  │  │  │ New Password: [•••••••••••••••••••••••••               ]             │  │  │
│  │  │  │ Confirm Password: [•••••••••••••••••••••••••            ]             │  │  │
│  │  │  │                                                                  │  │  │
│  │  │  │                                                  [Update Password]│  │  │
│  │  │  └────────────────────────────────────────────────────────────────┘  │  │
│  │  │                                                                      │  │  │
│  │  │  Two-Factor Authentication: [Enable]                                 │  │  │
│  │  │  Active Sessions:                                                    │  │  │
│  │  │  ┌────────────────────────────────────────────────────────────────┤  │  │
│  │  │  │ Device │ Location │ Last Active │ Actions                     │  │  │
│  │  │  │ Chrome │ Lagos    │ Now         │ Current                    │  │  │
│  │  │  │ Mobile │ Lagos    │ 2 hours ago │ [Revoke]                  │  │  │
│  │  │  └────────────────────────────────────────────────────────────────┘  │  │
│  │  └────────────────────────────────────────────────────────────────────┘  │  │
│  │  ───────────────────────────────────────────────────────────────────────  │  │
│  │  NOTIFICATIONS TAB:                                                       │  │
│  │  ┌────────────────────────────────────────────────────────────────────┐  │  │
│  │  │  Email Notifications:          Push Notifications:                   │  │  │
│  │  │  [✓] Deal assignments            [✓] Deal assignments               │  │  │
│  │  │  [✓] Deal stage changes         [✓] Deal stage changes            │  │  │
│  │  │  [✓] Lead assignments           [✓] Lead assignments              │  │  │
│  │  │  [✓] Approval requests          [✓] Approval requests             │  │  │
│  │  │  [ ] Weekly summary              [ ] Weekly summary                 │  │
│  │  │  [✓] Quota alerts               [✓] Quota alerts                  │  │  │
│  │  │                                                                      │  │  │
│  │  │                                                  [Save Preferences]  │  │  │
│  │  └────────────────────────────────────────────────────────────────────┘  │  │
│  └───────────────────────────────────────────────────────────────────────────┘  │
│                                                                                 │
│  USER ACTIONS:                                                                  │
│  ────────────                                                                   │
│  1. Update personal information      → PUT /users/me                            │
│                                        • Update profile fields                 │
│                                        • Refresh user data in store             │
│                                        • Show success toast                    │
│                                                                                 │
│  2. Change password                 → PUT /users/me/password                   │
│                                        • Validate current password             │
│                                        • Update password                       │
│                                        • Re-authenticate required               │
│                                                                                 │
│  3. Toggle notifications           → PUT /users/me/notifications              │
│                                        • Update notification preferences        │
│                                        • Subscribe/unsubscribe from topics      │
│                                                                                 │
│  4. Revoke session                   → DELETE /users/me/sessions/:id            │
│                                        • Invalidate session token               │
│                                        • Remove from active sessions            │
│                                                                                 │
└─────────────────────────────────────────────────────────────────────────────────┘
```

---

## 14. PERMISSION MATRIX

### 14.1 Page Access by Role

| Page | Director | Sales Manager | Executive | Ops Manager | Analyst |
|------|----------|---------------|-----------|-------------|---------|
| /dashboard | Read/Write | Read/Write | Read | Read | Read |
| /team | Full | Department | Own | Full | Read |
| /team/:memberId | Full | Department | Own | Full | Read |
| /leads | Full | Department | Own | All | Read |
| /leads/:leadId | Full | Department | Own | All | Read |
| /deals | Full | Department | Own | All | Read |
| /deals/:dealId | Full | Department | Own | All | Read |
| /deals/approvals | Approve | None | None | None | None |
| /orders | Full | Department | Own | All | Read |
| /customers | Full | Department | Assigned | All | Read |
| /performance | Full | Department | Own | Department | Read |
| /territory | Full | View | View | Full | Read |
| /territory/quotas | Full | View | View | Full | Read |
| /forecasting | Full | Department | None | Full | Read |
| /communications | Full | Department | None | All | Read |
| /settings/profile | Own | Own | Own | Own | Own |

### 14.2 Action Permissions by Role

| Action | Director | Sales Manager | Executive | Ops Manager | Analyst |
|--------|----------|---------------|-----------|-------------|---------|
| Create Lead | ✓ | ✓ | ✓ | ✓ | ✗ |
| Delete Lead | ✓ | Department | Own | ✗ | ✗ |
| Convert Lead to Deal | ✓ | ✓ | ✓ | ✗ | ✗ |
| Create Deal | ✓ | ✓ | ✓ | ✗ | ✗ |
| Delete Deal | ✓ | Department | Own | ✗ | ✗ |
| Close Deal (Won/Lost) | ✓ | Department | Own | ✗ | ✗ |
| Move Deal Stage | ✓ | Department | Own | ✗ | ✗ |
| Approve Deal | ✓ | ✗ | ✗ | ✗ | ✗ |
| Create Order | ✓ | ✓ | ✓ | ✓ | ✗ |
| Cancel Order | ✓ | Department | Own | ✗ | ✗ |
| Create Customer | ✓ | ✓ | ✓ | ✓ | ✗ |
| Delete Customer | ✓ | Department | ✗ | ✗ | ✗ |
| Set Quota | ✓ | ✗ | ✗ | ✓ | ✗ |
| Assign Territory | ✓ | ✗ | ✗ | ✓ | ✗ |
| View Commission | Own | Department | Own | Full | ✗ |
| Edit Pipeline Stages | ✓ | ✗ | ✗ | ✗ | ✗ |
| Export Data | ✓ | ✓ | Own | ✓ | ✓ |

---

## SUMMARY

### Country Sales Dashboard Documentation Complete

**Business Domain - Country-Sales-Dashboard (4 files)** ✓
- 01_UI_Flow_Documentation.md ✓
- 02_Wireframes_Documentation.md ✓
- 03_Mock_Flow_Documentation.md ✓
- 04_Page_By_Page_Flow_Documentation.md ✓

---

## NEXT STEPS

**Management Domain - Sales Department (HQ Sales Dashboard):**
- Create 4 documentation files for HQ Sales Dashboard
- Multi-country view and aggregation
- Global sales oversight and reporting

**Remaining Management Domain Documentation:**
2. Finance-department (Finance) - 4 files needed
3. Global-business-management (GBM) - 4 files needed
4. Customer-support (Support) - 4 files needed
5. Digital-marketing (Marketing) - 4 files needed
6. System-Administrator (Admin) - 4 files needed

**Total Remaining: 24 documentation files (1 HQ Sales + 5 other domains × 4 files each)**

---

**Document End**
