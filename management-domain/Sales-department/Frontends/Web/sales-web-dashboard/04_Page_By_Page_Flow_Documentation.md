# HQ SALES DASHBOARD - PAGE BY PAGE FLOW DOCUMENTATION

**Version:** 1.1
**Domain:** Management Domain
**Subdomain:** Sales-Departments (HQ)
**Frontend:** sales-web-dashboard
**Framework:** React 18 + Vite + TypeScript + Material UI v5.15+
**State Management:** Zustand
**Data Fetching:** TanStack Query
**Last Updated:** 2025-02-16

---

## TABLE OF CONTENTS

1. [Page Tree Structure](#1-page-tree-structure)
2. [Authentication Flows](#2-authentication-flows)
3. [Global Overview Flows](#3-global-overview-flows)
4. [Country Comparison Flows](#4-country-comparison-flows)
5. [Country Detail Flows](#5-country-detail-flows)
6. [Global Pipeline Flows](#6-global-pipeline-flows)
7. [Sales Team Partners Flows](#7-sales-team-partners-flows)
8. [Performance Flows](#8-performance-flows)
9. [Forecasting Flows](#9-forecasting-flows)
10. [Reports Flows](#10-reports-flows)
11. [Settings Flows](#11-settings-flows)
12. [Permission Matrix](#12-permission-matrix)

---

## 1. PAGE TREE STRUCTURE

```
HQ Sales Dashboard
│
├── /login
│   ├── Login Page
│   └── Forgot Password Page
│
├── / (protected)
│   ├── /global-overview (Dashboard)
│   │   ├── Worldwide Summary
│   │   ├── Country Performance Grid
│   │   ├── Executive Metrics
│   │   └── Alerts
│   │
│   ├── /countries
│   │   ├── Comparison View
│   │   ├── /:code (Country Detail)
│   │   │   ├── Overview
│   │   │   ├── Sales Team
│   │   │   ├── Pipeline
│   │   │   ├── Performance
│   │   │   └── Reports
│   │   └── /rankings
│   │
│   ├── /teams (Global Sales Teams)
│   │   ├── Country Directors
│   │   ├── Regional Managers
│   │   ├── Performance Leaderboard
│   │   └── /:memberId (Member Profile)
│   │
│   ├── /partners (Sales Team Partners) (NEW)
│   │   ├── Global Partners Overview
│   │   ├── /applications (Partner Applications)
│   │   │   ├── Pending Approvals
│   │   │   ├── /review/:applicationId (Review Application)
│   │   │   ├── Approved Partners
│   │   │   └── Rejected Applications
│   │   ├── /performance (Partner Performance)
│   │   │   ├── By Partner Type
│   │   │   ├── By Country
│   │   │   └── /leaderboard
│   │   ├── /commission (Commission Management)
│   │   │   ├── Partner Referral Bonuses
│   │   │   ├── Customer Sales Commissions
│   │   │   ├── Tier Management
│   │   │   ├── /payouts (Payment Processing)
│   │   │   └── /:transactionId (Transaction Detail)
│   │   ├── /territory (Territory Allocation)
│   │   │   ├── Lead Distribution
│   │   │   ├── Territory Assignments
│   │   │   └── AI Lead Management
│   │   └── /analytics (Partner Analytics)
│   │       ├── Acquisition Metrics
│   │       ├── Activity Monitoring
│   │       └── Revenue Contribution
│   │
│   ├── /pipeline (Global Pipeline)
│   │   ├── Worldwide Pipeline View
│   │   ├── Major Deals Tracker
│   │   ├── Pipeline Analytics
│   │   └── /:dealId (Deal Detail)
│   │
│   ├── /customers (Global Customers)
│   │   ├── Multi-National Accounts
│   │   ├── Customer Segmentation
│   │   ├── Global Account View
│   │   └── /:customerId (Customer Profile)
│   │
│   ├── /performance
│   │   ├── Revenue Analytics
│   │   ├── Team Performance
│   │   ├── Product Performance
│   │   └── /reports
│   │
│   ├── /forecasting
│   │   ├── Revenue Forecast
│   │   ├── Pipeline Forecast
│   │   ├── /trends
│   │   └── /accuracy
│   │
│   ├── /reports
│   │   ├── Executive Dashboards
│   │   ├── Country Reports
│   │   ├── Product Reports
│   │   ├── /:reportId
│   │   └── /create
│   │
│   └── /settings
│       ├── Profile Settings
│       ├── Country Configuration
│       ├── Commission Structures
│       └── System Configuration
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
│  1. Navigate to HQ dashboard URL   → Check for valid token                     │
│                                      → If valid: verify HQ access                  │
│                                      → If invalid: show login page              │
│                                                                                 │
│  2. Enter email and password         → Validate input format                    │
│                                      → Show loading state on button             │
│                                                                                 │
│  3. Click "Login" button             → POST /auth/login                         │
│                                      → Authenticate credentials                │
│                                      → Retrieve user profile & role              │
│                                      → Verify HQ Sales Dashboard access         │
│                                      → Determine region scope (if applicable)   │
│                                      → Store access token                      │
│                                      → Redirect to Global Overview            │
│                                                                                 │
│  SUCCESS FLOW:                                                                   │
│  ┌───────────────────────────────────────────────────────────────────────────┐  │
│  │  Login Form                                                    [Login]     │  │
│  │  ┌────────────────────────────────────────────────────────────────────┐  │  │
│  │  │  Email:               [john.doe@company.com                ]        │  │  │
│  │  │  Password:            [•••••••••••••••••••••••••            ]        │  │  │
│  │  │                       [Forgot password?]                         │  │  │
│  │  │                                                                  │  │  │
│  │  │  Your region scope will be auto-detected based on your role      │  │  │
│  │  │                                                                  │  │  │
│  │  │                                              [Cancel]  [Login]  │  │  │
│  │  └────────────────────────────────────────────────────────────────────┘  │  │
│  └───────────────────────────────────────────────────────────────────────────┘  │
│                                      │                                           │
│                                      ▼                                           │
│  ┌───────────────────────────────────────────────────────────────────────────┐  │
│  │  Verifying access...                                                         │  │
│  └───────────────────────────────────────────────────────────────────────────┘  │
│                                      │                                           │
│                                      ▼                                           │
│  ┌───────────────────────────────────────────────────────────────────────────┐  │
│  │  HQ Global Overview                                                          │  │
│  │  ┌────────────────────────────────────────────────────────────────────┐  │  │
│  │  │  Welcome, John Doe                                                   │  │  │
│  │  │  VP of Sales                                                         │  │  │
│  │  │  Region Scope: Global                                                  │  │  │
│  │  │                                                                  │  │  │
│  │  │  [Global Metrics...]                                               │  │  │
│  │  └────────────────────────────────────────────────────────────────────┘  │  │
│  └───────────────────────────────────────────────────────────────────────────┘  │
│                                                                                 │
│  REGIONAL MANAGER FLOW (Different login outcome):                               │
│  ┌───────────────────────────────────────────────────────────────────────────┐  │
│  │  West Africa Overview                                                      │  │
│  │  ┌────────────────────────────────────────────────────────────────────┐  │  │
│  │  │  Welcome, Jane Smith                                                  │  │  │
│  │  │  Regional Sales Manager                                              │  │  │
│  │  │  Region Scope: West Africa (Nigeria, Ghana, etc.)                  │  │  │
│  │  │                                                                  │  │  │
│  │  │  [West Africa Metrics...]                                          │  │  │
│  │  └────────────────────────────────────────────────────────────────────┘  │  │
│  └───────────────────────────────────────────────────────────────────────────┘  │
│                                                                                 │
└─────────────────────────────────────────────────────────────────────────────────┘
```

---

## 3. GLOBAL OVERVIEW FLOWS

### 3.1 Dashboard Load Flow

```
┌─────────────────────────────────────────────────────────────────────────────────┐
│                        GLOBAL OVERVIEW LOAD FLOW                              │
├─────────────────────────────────────────────────────────────────────────────────┤
│                                                                                 │
│  ENTRY: User logs in → Global Overview                                        │
│                                                                                 │
│  PAGE COMPONENTS:                                                               │
│  ┌───────────────────────────────────────────────────────────────────────────┐  │
│  │  HEADER: HQ Sales Dashboard > Global Overview                            │  │
│  │  FILTERS: [Period Selector] [Compare: Previous Period]                    │  │
│  │  ───────────────────────────────────────────────────────────────────────  │  │
│  │  GLOBAL KEY METRICS CARDS (6):                                           │  │
│  │  • Global Revenue      • Worldwide Pipeline   • Deals Closed            │  │
│  │  • Active Deals        • Win Rate             • Avg Deal Size           │  │
│  │  ───────────────────────────────────────────────────────────────────────  │  │
│  │  COUNTRY PERFORMANCE GRID:                                               │  │
│  │  ┌─────────┐ ┌─────────┐ ┌─────────┐ ┌─────────┐ ┌─────────┐          │  │
│  │  │ 🇳🇬 NG  │ │ 🇰🇪 KE  │ │ 🇬🇭 GH  │ │ 🇿🇦 ZA  │ │ [+ More]│          │  │
│  │  │ Revenue │ │ Revenue │ │ Revenue │ │ Revenue │ │          │          │  │
│  │  │ Quota % │ │ Quota % │ │ Quota % │ │ Quota % │ │          │          │  │
│  │  │ [View]  │ │ [View]  │ │ [View]  │ │ [View]  │ │          │          │  │
│  │  └─────────┘ └─────────┘ └─────────┘ └─────────┘ └─────────┘          │  │
│  │  ───────────────────────────────────────────────────────────────────────  │  │
│  │  CHARTS:                                                                  │  │
│  │  • Global Revenue Chart (by country)                                   │  │
│  │  • Revenue Trend (6 months)                                             │  │
│  │  • Top Performing Countries                                               │  │
│  │  ───────────────────────────────────────────────────────────────────────  │  │
│  │  EXECUTIVE ALERTS:                                                         │  │
│  │  • Countries below quota                                                  │  │
│  │  • Major deals closing                                                    │  │
│  │  • Forecast updates                                                       │  │
│  └───────────────────────────────────────────────────────────────────────────┘  │
│                                                                                 │
│  LOAD SEQUENCE:                                                                │
│  ┌───────────────────────────────────────────────────────────────────────────┐  │
│  │  1. Check permissions                                                     │  │
│  │     └─ Verify user has HQ Sales Dashboard access                        │  │
│  │         └─ Determine region scope (Global or Regional)                  │  │
│  │                                                                          │  │
│  │  2. Show loading skeleton                                                  │  │
│  │     └─ Display placeholder UI for metrics and charts                       │  │
│  │                                                                          │  │
│  │  3. Fetch global summary (GET /dashboard/global)                         │  │
│  │     ├─ Query: { period: selectedPeriod }                                │  │
│  │     └─ Response: GlobalDashboardSummary                                 │  │
│  │                                                                          │  │
│  │  4. Fetch country summaries                                               │  │
│  │     └─ Response: CountrySummary[]                                       │  │
│  │                                                                          │  │
│  │  5. Fetch top performers                                                 │  │
│  │     └─ Response: GlobalTopPerformer[]                                   │  │
│  │                                                                          │  │
│  │  6. Update UI with fetched data                                           │  │
│  │     ├─ Populate metric cards                                              │  │
│  │     ├─ Render country grid                                                 │  │
│  │     ├─ Render charts                                                      │  │
│  │     └─ Show alerts                                                        │  │
│  │                                                                          │  │
│  │  7. Subscribe to WebSocket for real-time updates                          │  │
│  │     └─ Topics: /topic/global/dashboard, /topic/deals/major             │  │
│  │                                                                          │  │
│  │  8. Handle errors                                                         │  │
│  │     ├─ Network error: Show retry banner                                  │  │
│  │     ├─ Auth error: Redirect to login                                     │  │
│  │     └─ Partial data error: Show warning, display available data          │  │
│  │                                                                          │  │
│  └───────────────────────────────────────────────────────────────────────────┘  │
│                                                                                 │
└─────────────────────────────────────────────────────────────────────────────────┘
```

### 3.2 Metric Card Click Flow

```
┌─────────────────────────────────────────────────────────────────────────────────┐
│                        METRIC CARD INTERACTION                               │
├─────────────────────────────────────────────────────────────────────────────────┤
│                                                                                 │
│  USER ACTION: Click on Global Revenue metric card                            │
│                                                                                 │
│  FLOW:                                                                          │
│  ┌───────────────────────────────────────────────────────────────────────────┐  │
│  │  1. User clicks "Global Revenue" card                                    │  │
│  │      └─ Shows dropdown with drill-down options                            │  │
│  │                                                                          │  │
│  │  2. User selects option:                                                 │  │
│  │     • "View by Country" → Navigate to /countries with revenue sort        │  │
│  │     • "View Trend" → Open revenue trend modal                            │  │
│  │     • "View Forecast" → Navigate to /forecasting/revenue                │  │
│  │     • "Export Report" → Generate revenue report PDF                      │  │
│  │                                                                          │  │
│  │  3. For "View Trend" modal:                                             │  │
│  │     ┌────────────────────────────────────────────────────────────────────┐  │  │
│  │     │  Global Revenue Trend (Last 6 Months)                            │  │
│  │     │  ┌────────────────────────────────────────────────────────────────┤  │  │
│  │     │  │                                                            │  │  │
│  │     │  │  $10M ┤    ┌───┐                                              │  │  │
│  │     │  │   ┌─┘   └─┐    ┌────  Monthly Global Revenue                 │  │  │
│  │     │  │ ┌─┘       └─┐   │                                        │  │  │
│  │     │  └─┘           └─┘ └                                        │  │  │
│  │     │  │  Sep Oct Nov Dec Jan Feb                                   │  │  │
│  │     │  │  ━━━━━━━━━━━━━━━━━━ Target                                │  │  │
│  │     │  └────────────────────────────────────────────────────────────────┤  │  │
│  │     │                                                                  │  │  │
│  │     │  [Close] [Download Chart] [Add to Report]                         │  │  │
│  │     └────────────────────────────────────────────────────────────────────┘  │  │
│  │                                                                          │  │
│  └───────────────────────────────────────────────────────────────────────────┘  │
│                                                                                 │
│  SIMILAR FLOWS for other metric cards:                                         │
│  • Pipeline Value → Navigate to /pipeline                                   │
│  • Deals Closed → Navigate to /deals?status=won                              │
│  • Active Deals → Navigate to /pipeline                                     │
│  • Win Rate → Navigate to /performance/win-rate                             │
│  • Avg Deal Size → Navigate to /performance/deal-size                        │
│                                                                                 │
└─────────────────────────────────────────────────────────────────────────────────┘
```

---

## 4. COUNTRY COMPARISON FLOWS

### 4.1 Country Selection Flow

```
┌─────────────────────────────────────────────────────────────────────────────────┐
│                        COUNTRY COMPARISON FLOW                               │
├─────────────────────────────────────────────────────────────────────────────────┤
│                                                                                 │
│  NAVIGATION: /countries/compare                                                │
│                                                                                 │
│  PAGE COMPONENTS:                                                               │
│  ┌───────────────────────────────────────────────────────────────────────────┐  │
│  │  HEADER: HQ Sales Dashboard > Countries > Comparison                     │  │
│  │  COUNTRY SELECTOR:                                                          │  │
│  │  ┌────────────────────────────────────────────────────────────────────────┐  │  │
│  │  │ [Select Countries to Compare]                                       │  │  │
│  │  │ [☑] 🇳🇬 Nigeria  [☑] 🇰🇪 Kenya  [☑] 🇬🇭 Ghana  [☑] 🇿🇦 S. Africa     │  │  │
│  │  │ [+ Add More Countries]                                                │  │  │
│  │  └────────────────────────────────────────────────────────────────────────┘  │  │
│  │  PERIOD SELECTOR: [This Month ▼]  COMPARE: [Previous Period ▼]           │  │
│  │  ACTIONS: [Generate Report] [Export] [Save as Template]                   │  │
│  │  ───────────────────────────────────────────────────────────────────────  │  │
│  │  COMPARISON TABLE:                                                          │  │
│  │  ┌────────────────────────────────────────────────────────────────────────┐  │  │
│  │  │ Metric      │ 🇳🇬 NG │ 🇰🇪 KE │ 🇬🇭 GH │ 🇿🇦 ZA │ Trend │          │  │  │
│  │  ├────────────────────────────────────────────────────────────────────────┤  │  │
│  │  │ Revenue     │ $1.2M │ $950K │ $780K│ $2.1M│ 📈    │          │  │  │
│  │  │ Quota Att.  │ 95%   │ 102%  │ 88%  │ 105% │        │          │  │  │
│  │  │ Deals       │ 245   │ 189   │ 156  │ 312  │ 📈    │          │  │  │
│  │  │ Win Rate    │ 32%   │ 35%   │ 28%  │ 38%  │        │          │  │  │
│  │  └────────────────────────────────────────────────────────────────────────┘  │  │
│  │  COMPARISON CHARTS:                                                          │  │
│  │  • Revenue Comparison (Grouped Bar)                                       │  │
│  │  • Quota Attainment Comparison                                             │  │
│  │  • Deals Closed Comparison                                                 │  │
│  │  RANKINGS:                                                                  │  │
│  │  • By Revenue, By Growth, By Quota, By Win Rate                            │  │
│  └───────────────────────────────────────────────────────────────────────────┘  │
│                                                                                 │
│  USER ACTIONS:                                                                  │
│  ────────────                                                                   │
│  1. Select/deselect countries          → Update comparison table and charts    │
│                                          • Re-fetch comparison data               │
│                                          • Update visualizations                │
│                                                                                 │
│  2. Click country checkbox             → Add/remove from comparison            │
│                                          • Minimum 2 countries required          │
│                                          • Maximum 8 countries for display      │
│                                                                                 │
│  3. Change period selector            → Re-fetch data for new period          │
│                                          • Update all tables and charts          │
│                                                                                 │
│  4. Enable "Compare to Previous"       → Show variance columns                 │
│                                          • Display period-over-period change      │
│                                                                                 │
│  5. Click "Generate Report"            → Create downloadable comparison report │
│                                          • PDF/Excel export                     │
│                                                                                 │
│  6. Click metric in table             → Sort by metric                       │
│                                          • Toggle ascending/descending         │
│                                                                                 │
└─────────────────────────────────────────────────────────────────────────────────┘
```

### 4.2 Country Detail Navigation Flow

```
┌─────────────────────────────────────────────────────────────────────────────────┐
│                        COUNTRY DETAIL NAVIGATION                             │
├─────────────────────────────────────────────────────────────────────────────────┤
│                                                                                 │
│  FROM: Comparison page or Global Overview → Click country card                │
│                                                                                 │
│  NAVIGATION: /countries/:code                                                    │
│                                                                                 │
│  USER ACTIONS:                                                                  │
│  ────────────                                                                   │
│  1. Click country card in overview       → Navigate to /countries/:code       │
│  2. Click [View] in comparison table   → Navigate to /countries/:code       │
│  3. Click country name in rankings      → Navigate to /countries/:code       │
│                                                                                 │
│  COUNTRY DETAIL PAGE:                                                          │
│  ┌───────────────────────────────────────────────────────────────────────────┐  │
│  │  [< All Countries]  │  🇳🇬 Nigeria  │  Director: John Doe               │  │
│  │  ACTIONS: [View Full Dashboard] [Download Report] [Contact Director]      │  │
│  │  ───────────────────────────────────────────────────────────────────────  │  │
│  │  SUMMARY:                                                                  │  │
│  │  • Revenue, Quota, Deals, Win Rate, Growth                              │  │
│  │  ───────────────────────────────────────────────────────────────────────  │  │
│  │  PERFORMANCE TRENDS:                                                       │  │
│  │  • Revenue trend chart (6 months)                                        │  │
│  │  ───────────────────────────────────────────────────────────────────────  │  │
│  │  TOP PERFORMERS:                                                           │  │
│  │  • Top 5 performers in country                                           │  │
│  │  ───────────────────────────────────────────────────────────────────────  │  │
│  │  MAJOR DEALS:                                                              │  │
│  │  • Deals > $50K in country                                               │  │
│  │  ───────────────────────────────────────────────────────────────────────  │  │
│  │  PRODUCT/ SERVICE BREAKDOWN:                                             │  │
│  │  • Revenue by product/service                                            │  │
│  └───────────────────────────────────────────────────────────────────────────┘  │
│                                                                                 │
│  DRILL-DOWN OPTIONS:                                                            │
│  • [View Full Dashboard] → Opens country sales dashboard in new tab          │
│  • [Download Report] → Generates detailed country report PDF                 │
│  • [Contact Director] → Opens email to country director                      │
│                                                                                 │
│  QUICK ACTIONS:                                                                │
│  • Click performer name → Navigate to /teams/:memberId                     │
│  • Click deal → Navigate to deal detail (via country API)                   │
│  • Click product → Show product performance breakdown                        │
│                                                                                 │
└─────────────────────────────────────────────────────────────────────────────────┘
```

---

## 5. COUNTRY DETAIL FLOWS

### 5.1 Country Overview Flow

```
┌─────────────────────────────────────────────────────────────────────────────────┐
│                        COUNTRY DETAIL PAGE                                  │
├─────────────────────────────────────────────────────────────────────────────────┤
│                                                                                 │
│  TRIGGER: User navigates to /countries/NG (Nigeria)                           │
│                                                                                 │
│  LOAD SEQUENCE:                                                                │
│  ┌───────────────────────────────────────────────────────────────────────────┐  │
│  │  1. Verify user has access to requested country                           │  │
│  │     ├─ Global users: All countries                                       │  │
│  │     └─ Regional users: Countries in their scope only                    │  │
│  │                                                                          │  │
│  │  2. If access denied:                                                     │  │
│  │     └─ Show error: "You don't have access to this country"              │  │
│  │         └─ Suggest available countries                                  │  │
│  │                                                                          │  │
│  │  3. Fetch country detail (GET /countries/:code)                           │  │
│  │     ├─ Country info                                                       │  │
│  │     ├─ Summary metrics                                                    │  │
│  │     ├─ Top performers                                                    │  │
│  │     ├─ Major deals                                                       │  │
│  │     └─ Performance trends                                               │  │
│  │                                                                          │  │
│  │  4. Display country detail page                                           │  │
│  │                                                                          │  │
│  │  5. Subscribe to country-specific WebSocket updates                        │  │
│  │     └─ /topic/countries/{code}                                           │  │
│  │                                                                          │  │
│  └───────────────────────────────────────────────────────────────────────────┘  │
│                                                                                 │
│  PAGE ACTIONS:                                                                  │
│  ────────────                                                                   │
│  1. [View Full Dashboard]             → Opens country dashboard in new tab   │
│                                                • Requires separate login token   │
│                                                • Redirects to country-specific    │
│                                                • sales domain URL               │
│                                                                                 │
│  2. [Download Report]                  → Generate PDF country report         │
│                                                • Include all metrics          │
│                                                • Include top performers       │
│                                                • Include major deals           │
│                                                • Include trend analysis         │
│                                                • Auto-download on generation    │
│                                                                                 │
│  3. [Contact Director]                 → Open email composer               │
│                                                • Pre-fill: director email      │
│                                                • Subject: Country performance    │
│                                                • Include summary in body        │
│                                                                                 │
│  4. Click performer                   → Navigate to /teams/:memberId       │
│                                                → Show performer profile         │
│                                                • Performance metrics           │
│                                                • Commission info               │
│                                                • Activity timeline            │
│                                                                                 │
│  5. Click deal                        → Navigate to deal detail            │
│                                                • Via country API proxy          │
│                                                • Show deal context              │
│                                                • Country, stage, value         │
│                                                                                 │
└─────────────────────────────────────────────────────────────────────────────────┘
```

---

## 6. GLOBAL PIPELINE FLOWS

### 6.1 Pipeline Overview Flow

```
┌─────────────────────────────────────────────────────────────────────────────────┐
│                        GLOBAL PIPELINE OVERVIEW                               │
├─────────────────────────────────────────────────────────────────────────────────┤
│                                                                                 │
│  NAVIGATION: /pipeline                                                           │
│                                                                                 │
│  PAGE COMPONENTS:                                                               │
│  ┌───────────────────────────────────────────────────────────────────────────┐  │
│  │  HEADER: HQ Sales Dashboard > Global Pipeline                             │  │
│  │  FILTERS:                                                                  │  │
│  │  [All Countries ▼] [All Stages ▼] [Value > $50K] [Major Deals Only]        │  │
│  │  ───────────────────────────────────────────────────────────────────────  │  │
│  │  PIPELINE SUMMARY BY COUNTRY:                                              │  │
│  │  ┌────────────────────────────────────────────────────────────────────────┐  │  │
│  │  │ Country │ Pipeline │ Deals │ Weighted │ Avg Size │ Stage│ Trend │       │  │  │
│  │  ├────────────────────────────────────────────────────────────────────────┤  │  │
│  │  │ 🇳🇬 NG   │ $3.5M    │ 287   │ $1.8M   │ $12.2K  │ View │ 📈   │[View]│  │  │
│  │  │ 🇰🇪 KE   │ $2.8M    │ 198   │ $1.4M   │ $14.1K  │ View │ 📈   │[View]│  │  │
│  │  │ 🇬🇭 GH   │ $2.1M    │ 156   │ $1.0M   │ $13.5K  │ View │ ➡️    │[View]│  │  │
│  │  │ 🇿🇦 ZA   │ $4.2M    │ 342   │ $2.5M   │ $12.3K  │ View │ 📈   │[View]│  │  │
│  │  │ TOTAL  │ $12.6M   │ 983   │ $6.7M   │ $12.8K  │ View │ 📈   │       │  │  │
│  │  └────────────────────────────────────────────────────────────────────────┘  │  │
│  │  ───────────────────────────────────────────────────────────────────────  │  │
│  │  MAJOR DEALS TRACKER:                                                        │  │
│  │  • Deals > $50K across all countries                                       │  │
│  │  • Sortable by value, stage, close date                                    │  │
│  │  ───────────────────────────────────────────────────────────────────────  │  │
│  │  PIPELINE BY STAGE:                                                         │  │
│  │  • Stage breakdown with count, value, weighted value                        │  │
│  │  • Average days in stage                                                    │  │
│  │  ───────────────────────────────────────────────────────────────────────  │  │
│  │  PIPELINE VELOCITY:                                                         │  │
│  │  • Average days in each stage                                               │  │
│  │  • Overall cycle time vs target                                            │  │
│  └───────────────────────────────────────────────────────────────────────────┘  │
│                                                                                 │
│  USER ACTIONS:                                                                  │
│  ────────────                                                                   │
│  1. Apply filters                    → Update pipeline display            │
│                                          • Filter by country                 │
│                                          • Filter by stage                   │
│                                          • Filter by minimum deal value      │
│                                          • Show major deals only             │
│                                                                                 │
│  2. Click [View] on country           → Navigate to /countries/:code      │
│                                          • Filter to pipeline view           │
│                                                                                 │
│  3. Click major deal                  → Open deal detail modal           │
│                                          • Show deal context                │
│                                          • Country, stage, probability       │
│                                          • Owner information                │
│                                          • Option to view in country dash    │
│                                                                                 │
│  4. Click stage in summary          → Filter deals by stage              │
│                                          • Show all deals in stage           │
│                                          • Breakdown by country              │
│                                                                                 │
│  5. View stage distribution         → Show stage distribution chart    │
│                                          • Donut or funnel chart              │
│                                          • Drill down into each stage         │
│                                                                                 │
└─────────────────────────────────────────────────────────────────────────────────┘
```

---

## 7. SALES TEAM PARTNERS FLOWS (NEW)

### 7.1 Sales Team Partners Overview Flow

```
┌─────────────────────────────────────────────────────────────────────────────────┐
│                      GLOBAL SALES TEAM PARTNERS OVERVIEW                         │
├─────────────────────────────────────────────────────────────────────────────────┤
│                                                                                 │
│  NAVIGATION: /partners                                                           │
│                                                                                 │
│  PAGE COMPONENTS:                                                               │
│  ┌───────────────────────────────────────────────────────────────────────────┐  │
│  │  HEADER: HQ Sales Dashboard > Sales Team Partners                             │  │
│  │  CONTROLS: [Region: All ▼] [Partner Type: All ▼] [Period: This Month ▼]     │  │
│  │  ───────────────────────────────────────────────────────────────────────  │  │
│  │  SUMMARY CARDS:                                                            │  │
│  │  ┌──────────────┐ ┌──────────────┐ ┌──────────────┐ ┌──────────────┐     │  │
│  │  │ Total Active │ │ Pending Apps │ │ This Month   │ │ Commission   │     │  │
│  │  │ Partners     │ │              │ │ Commissions   │ │ Paid Out     │     │  │
│  │  │     1,245     │ │      18       │ │     ₦8.5M     │ │     ₦6.2M     │     │  │
│  │  │   ┌──────┐    │ │   ┌──────┐    │ │   ┌──────┐    │ │   ┌──────┐    │     │  │
│  │  │   │ +12% │    │ │   │ ┌───┐ │    │ │   │ +8%  │    │ │   │ +15% │    │     │  │
│  │  │   └──────┘    │ │   │ │ 3 │ │    │ │   └──────┘    │ │   └──────┘    │     │  │
│  │  └──────────────┘ └──────────────┘ └──────────────┘ └──────────────┘     │  │
│  │  ───────────────────────────────────────────────────────────────────────  │  │
│  │  PARTNERS BY TYPE:                                                          │  │
│  │  ┌────────────────────────────────────────────────────────────────────┐ │  │
│  │  │ 🚚 Courier      │ 📦 Warehouse  │ 🛒 E-commerce  │ ✈️ Air/Ocean │    │  │
│  │  │     245        │      189      │      312      │      98       │    │  │
│  │  └────────────────────────────────────────────────────────────────────┘ │  │
│  │  ┌────────────────────────────────────────────────────────────────────┐ │  │
│  │  │ 🚛 Haulage      │ 📍 Location   │ 📦 Wholesale   │ 📱 Influencer │    │  │
│  │  │     156        │      134      │      87       │      124      │    │  │
│  │  └────────────────────────────────────────────────────────────────────┘ │  │
│  │  ───────────────────────────────────────────────────────────────────────  │  │
│  │  PARTNERS BY COUNTRY:                                                        │  │
│  │  ┌────────────────────────────────────────────────────────────────────┐ │  │
│  │  │ Country      │ Partners │ Apps │ Commission  │ Top Performer        │    │  │
│  │  ├────────────────────────────────────────────────────────────────────┤ │  │
│  │  │ 🇳🇬 Nigeria    │   342    │   6   │  ₦2.8M      │ John Okafor          │    │  │
│  │  │ 🇰🇪 Kenya      │   198    │   3   │  ₦1.5M      │ Sarah Wanjiku        │    │  │
│  │  │ 🇬🇭 Ghana      │   145    │   4   │  ₦980K       │ Kwame Mensah         │    │  │
│  │  │ 🇿🇦 S. Africa  │   278    │   5   │  ₦2.1M      │ Johan van der Berg   │    │  │
│  │  └────────────────────────────────────────────────────────────────────┘ │  │
│  │  ───────────────────────────────────────────────────────────────────────  │  │
│  │  TIER DISTRIBUTION:                                                          │  │
│  │  ┌────────────────────────────────────────────────────────────────────┐ │  │
│  │  │ Platinum (15%)      │ Gold (10%)        │ Silver (7.5%)       │    │  │
│  │  │      45            │      234           │      567             │    │  │
│  │  │   ████████████████   │   ████████████████████████████████████   │    │  │
│  │  │                      │                                          │    │  │
│  │  │ Bronze (5%)         │ Total                                   │    │  │
│  │  │      399            │      1,245                               │    │  │
│  │  │   ████████████████████████████████████████████████████████   │    │  │
│  │  └────────────────────────────────────────────────────────────────────┘ │  │
│  └───────────────────────────────────────────────────────────────────────────┘  │
│                                                                                 │
└─────────────────────────────────────────────────────────────────────────────┘
```

### 7.2 Partner Application Review Flow

```
┌─────────────────────────────────────────────────────────────────────────────────┐
│                        PARTNER APPLICATION REVIEW                                │
├─────────────────────────────────────────────────────────────────────────────────┤
│                                                                                 │
│  NAVIGATION: /partners/applications/pending                                │
│                                                                                 │
│  PAGE COMPONENTS:                                                               │
│  ┌───────────────────────────────────────────────────────────────────────────┐  │
│  │  HEADER: Sales Team Partners > Applications > Pending Approvals           │  │
│  │  FILTERS: [Partner Type: All ▼] [Country: All ▼] [Date Range]                │  │
│  │  SEARCH: [Search by name, email, or application ID...]                        │  │
│  │  ───────────────────────────────────────────────────────────────────────  │  │
│  │  APPLICATIONS TABLE:                                                        │  │
│  │  ┌────────────────────────────────────────────────────────────────────┐  │  │
│  │  │ App ID      │ Name            │ Type      │ Country │ Territory │ Age │ Action│  │  │
│  │  ├────────────────────────────────────────────────────────────────────┤  │  │
│  │  │ APP-2025-01│ John Okafor     │ Courier   │ Nigeria │ North    │ 2d  │[Review]│  │  │
│  │  │ APP-2025-02│ Sarah Wanjiku   │ Warehouse │ Kenya   │ West     │ 1d  │[Review]│  │  │
│  │  │ APP-2025-03│ Kwame Mensah    │ E-commerce│ Ghana   │ Accra   │ 3h  │[Review]│  │  │
│  │  │ APP-2025-04│ Johan vdB       │ Haulage  │ S.Africa│ Gauteng │ 5h  │[Review]│  │  │
│  │  └────────────────────────────────────────────────────────────────────┘  │  │
│  └───────────────────────────────────────────────────────────────────────────┘  │
│                                                                                 │
│  [TAP REVIEW] → Application Detail Page                                        │
│                                                                                 │
└─────────────────────────────────────────────────────────────────────────────┘
```

### 7.3 Partner Application Detail Flow

```
┌─────────────────────────────────────────────────────────────────────────────────┐
│                      PARTNER APPLICATION DETAIL                                │
├─────────────────────────────────────────────────────────────────────────────────┤
│                                                                                 │
│  APPLICATION: APP-2025-001                                                   │
│  STATUS: Pending Review                                                        │
│                                                                                 │
│  ┌─────────────────────────────────────────────────────────────────────────┐  │
│  │  PERSONAL INFORMATION                                                        │  │
│  │  ┌─────────────────────────────────────────────────────────────────────┐  │  │
│  │  │ Full Name:           John Okafor                                     │  │  │
│  │  │ Email:               john.okafor@email.com                          │  │  │
│  │  │ Phone:               +234 801 234 5678                              │  │  │
│  │  │ Date of Birth:       1985-03-15                                     │  │  │
│  │  │ National ID:         12345678901                                    │  │  │
│  │  │ Address:             123 Adetokunbo Ademola St, VI, Lagos        │  │  │
│  │  └─────────────────────────────────────────────────────────────────────┘  │  │
│  └─────────────────────────────────────────────────────────────────────────┘  │
│                                                                                 │
│  ┌─────────────────────────────────────────────────────────────────────────┐  │
│  │  SALES EXPERIENCE                                                            │  │
│  │  ┌─────────────────────────────────────────────────────────────────────┐  │  │
│  │  │ Years of Experience:  8 years                                         │  │  │
│  │  │ Industries:            Retail, E-commerce, Logistics               │  │  │
│  │  │ Monthly Sales Volume:  ₦2,000,000                                  │  │  │
│  │  │ Current Employment:   Self-employed                                │  │  │
│  │  └─────────────────────────────────────────────────────────────────────┘  │  │
│  └─────────────────────────────────────────────────────────────────────────┘  │
│                                                                                 │
│  ┌─────────────────────────────────────────────────────────────────────────┐  │
│  │  PREFERENCES                                                                  │  │
│  │  ┌─────────────────────────────────────────────────────────────────────┐  │  │
│  │  │ Partner Types:          ✓ Courier   ✓ Warehouse   ✓ E-commerce      │  │  │
│  │  │ Customer Types:        ✓ Individual ✓ Corporate                     │  │  │
│  │  │ Preferred Territory:   North Lagos Zone                              │  │  │
│  │  └─────────────────────────────────────────────────────────────────────┘  │  │
│  └─────────────────────────────────────────────────────────────────────────┘  │
│                                                                                 │
│  ┌─────────────────────────────────────────────────────────────────────────┐  │
│  │  BANK INFORMATION (FOR COMMISSION)                                           │  │
│  │  ┌─────────────────────────────────────────────────────────────────────┐  │  │
│  │  │ Bank:                 GTBank                                          │  │  │
│  │  │ Account Number:        1234567890                                     │  │  │
│  │  │ Account Name:          John Okafor                                     │  │  │
│  │  │ BVN:                  12345678901 (✓ Verified)                          │  │  │
│  │  └─────────────────────────────────────────────────────────────────────┘  │  │
│  └─────────────────────────────────────────────────────────────────────────┘  │
│                                                                                 │
│  ┌─────────────────────────────────────────────────────────────────────────┐  │
│  │  BACKGROUND VERIFICATION                                                    │  │
│  │  ┌─────────────────────────────────────────────────────────────────────┐  │  │
│  │  │ BVN Validation:       ✓ Verified                                     │  │  │
│  │  │ National ID:          ✓ Verified                                     │  │  │
│  │  │ Employment Check:     ✓ Verified                                     │  │  │
│  │  │ References:           ⏳ Pending (2 of 3)                              │  │  │
│  │  │ Overall Status:       ⚠️ Awaiting All Verifications                   │  │  │
│  │  └─────────────────────────────────────────────────────────────────────┘  │  │
│  └─────────────────────────────────────────────────────────────────────────┘  │
│                                                                                 │
│  ┌─────────────────────────────────────────────────────────────────────────┐  │
│  │  TERRITORY ALIGNMENT CHECK                                                  │  │
│  │  ┌─────────────────────────────────────────────────────────────────────┐  │  │
│  │  │ Requested Territory:  North Lagos Zone                             │  │  │
│  │  │ Current Capacity:      45 partners (Max: 50)                      │  │  │
│  │  │ Lead Allocation:       50 leads/month                             │  │  │
│  │  │ Recommendation:        ✓ Approve for requested territory           │  │  │
│  │  │ Alternative:            If full, assign to South Lagos Zone      │  │  │
│  │  └─────────────────────────────────────────────────────────────────────┘  │  │
│  └─────────────────────────────────────────────────────────────────────────┘  │
│                                                                                 │
│  ┌─────────────────────────────────────────────────────────────────────────┐  │
│  │  ACTIONS                                                                      │  │
│  │  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐      │  │
│  │  │   ✓ APPROVE  │  │   ✗ REJECT   │  │ 📧 Request    │  │ 📋 Put on     │      │  │
│  │  │              │  │              │  │   More Info   │  │   Hold       │      │  │
│  │  └──────────────┘  └──────────────┘  └──────────────┘  └──────────────┘      │  │
│  └─────────────────────────────────────────────────────────────────────────┘  │
│                                                                                 │
│  [APPROVE] → Confirmation Modal → Partner Account Created → Notified                 │
│                                                                                 │
└─────────────────────────────────────────────────────────────────────────────┘
```

### 7.4 Commission Management Flow

```
┌─────────────────────────────────────────────────────────────────────────────────┐
│                        COMMISSION MANAGEMENT                                    │
├─────────────────────────────────────────────────────────────────────────────────┤
│                                                                                 │
│  NAVIGATION: /partners/commission                                               │
│                                                                                 │
│  PAGE COMPONENTS:                                                               │
│  ┌───────────────────────────────────────────────────────────────────────────┐  │
│  │  HEADER: Sales Team Partners > Commission Management                       │  │
│  │  TABS: [Overview] [Partner Referrals] [Customer Sales] [Tier Management]     │  │
│  │  ───────────────────────────────────────────────────────────────────────  │  │
│  │  COMMISSION SUMMARY                                                           │  │
│  │  ┌────────────────────────────────────────────────────────────────────┐  │  │
│  │  │ This Month Payout:         ₦6,250,000                              │  │  │
│  │  │ Pending Commissions:       ₦1,850,000                              │  │  │
│  │  │ Active Partners:          1,245                                   │  │  │
│  │  │ Average per Partner:        ₦5,020                                 │  │  │
│  │  └────────────────────────────────────────────────────────────────────┘  │  │
│  │  ───────────────────────────────────────────────────────────────────────  │  │
│  │  COMMISSION BY TYPE                                                           │  │
│  │  ┌────────────────────────────────────────────────────────────────────┐  │  │
│  │  │ Partner Referral Bonuses     │ Customer Sales Commissions          │  │  │
│  │  │                              │                                       │  │  │
│  │  │ ₦2,125,000                   │ ₦4,125,000                          │  │  │
│  │  │ 34%                          │ 66%                                  │  │  │
│  │  │ 245 partners                 │ 1,245 partners                        │  │  │
│  │  └────────────────────────────────────────────────────────────────────┘  │  │
│  │  ───────────────────────────────────────────────────────────────────────  │  │
│  │  PAYOUT SCHEDULE                                                              │  │
│  │  ┌────────────────────────────────────────────────────────────────────┐  │  │
│  │  │ Next Payout:              March 1, 2025                             │  │  │
│  │  │ Processing Status:        Calculating commissions                   │  │  │
│  │  │ Estimated Payouts:        1,245 partners                           │  │  │
│  │  │ Bank Transfer Status:      Pending                                   │  │  │
│  │  └────────────────────────────────────────────────────────────────────┘  │  │
│  └───────────────────────────────────────────────────────────────────────────┘  │
│                                                                                 │
└─────────────────────────────────────────────────────────────────────────────┘
```

### 7.5 Territory Allocation Management Flow

```
┌─────────────────────────────────────────────────────────────────────────────────┐
│                     TERRITORY ALLOCATION MANAGEMENT                              │
├─────────────────────────────────────────────────────────────────────────────────┤
│                                                                                 │
│  NAVIGATION: /partners/territory                                               │
│                                                                                 │
│  PAGE COMPONENTS:                                                               │
│  ┌───────────────────────────────────────────────────────────────────────────┐  │
│  │  HEADER: Sales Team Partners > Territory Allocation                         │  │
│  │  CONTROLS: [Country: All ▼] [Region: All ▼] [View: Map ▼]                    │  │
│  │  ───────────────────────────────────────────────────────────────────────  │  │
│  │  TERRITORY MAP                                                                │  │
│  │  ┌────────────────────────────────────────────────────────────────────┐  │  │
│  │  │                    [Interactive Map View]                             │  │  │
│  │  │  • Colored by partner density                                          │  │  │
│  │  │  • Lead allocation indicators                                          │  │  │
│  │  │  • Territory boundaries                                                 │  │  │
│  │  │  • Partner location pins                                               │  │  │
│  │  └────────────────────────────────────────────────────────────────────┘  │  │
│  │  ───────────────────────────────────────────────────────────────────────  │  │
│  │  TERRITORY SUMMARY TABLE                                                    │  │
│  │  ┌────────────────────────────────────────────────────────────────────┐  │  │
│  │  │ Territory │ Partners │ Lead Alloc │ Utilization │ Avg Lead │  │  │
│  │  │           │ Capacity │            │              │ Score     │  │  │
│  │  ├────────────────────────────────────────────────────────────────────┤  │  │
│  │  │ North     │   45/50  │    2,250   │     90%      │    72     │  │  │
│  │  │ Lagos     │   ▓▓▓▓▓▓  │   ▓▓▓▓▓▓▓▓▓▓▓▓▓│   ▓▓▓▓▓▓▓▓▓▓▓│  ▓▓▓▓▓▓▓▓ │  │  │
│  │  │ South     │   38/50  │    1,900   │     76%      │    68     │  │  │
│  │  │ Lagos     │   ▓▓▓▓▓▓  │   ▓▓▓▓▓▓▓▓▓▓▓▓│   ▓▓▓▓▓▓▓▓▓▓▓│  ▓▓▓▓▓▓▓▓ │  │  │
│  │  │ East      │   28/50  │    1,400   │     56%      │    64     │  │  │
│  │  │ Nigeria   │   ▓▓▓▓▓   │   ▓▓▓▓▓▓▓▓▓▓▓▓│   ▓▓▓▓▓▓▓▓▓▓▓│  ▓▓▓▓▓▓▓▓ │  │  │
│  │  │ West      │   34/50  │    1,700   │     68%      │    70     │  │  │
│  │  │ Kenya     │   ▓▓▓▓▓▓   │   ▓▓▓▓▓▓▓▓▓▓▓▓│   ▓▓▓▓▓▓▓▓▓▓▓│  ▓▓▓▓▓▓▓▓ │  │  │
│  │  └────────────────────────────────────────────────────────────────────┘  │  │
│  │  ───────────────────────────────────────────────────────────────────────  │  │
│  │  AI LEAD GENERATION STATUS                                                    │  │
│  │  ┌────────────────────────────────────────────────────────────────────┐  │  │
│  │  │ Total Leads Generated:    15,450 this month                        │  │  │
│  │  │ Leads Assigned:           15,423 (99.8%)                           │  │  │
│  │  │ Lead Quality Score:        71 average                              │  │  │
│  │  │ Conversion Rate:          85%                                    │  │  │
│  │  └────────────────────────────────────────────────────────────────────┘  │  │
│  │  ───────────────────────────────────────────────────────────────────────  │  │
│  │  ACTIONS: [Adjust Allocation] [Reassign Leads] [View Partner List]        │  │
│  └───────────────────────────────────────────────────────────────────────────┘  │
│                                                                                 │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## 8. PERFORMANCE FLOWS

### 8.1 Global Performance Flow

```
┌─────────────────────────────────────────────────────────────────────────────────┐
│                        GLOBAL PERFORMANCE PAGE                                │
├─────────────────────────────────────────────────────────────────────────────────┤
│                                                                                 │
│  NAVIGATION: /performance                                                        │
│                                                                                 │
│  PAGE COMPONENTS:                                                               │
│  ┌───────────────────────────────────────────────────────────────────────────┐  │
│  │  HEADER: HQ Sales Dashboard > Performance                                 │  │
│  │  CONTROLS: [Period: Q1 2025 ▼] [View By: Country ▼] [Department ▼]         │  │
│  │  ───────────────────────────────────────────────────────────────────────  │  │
│  │  PERFORMANCE SUMMARY CARDS:                                               │  │
│  │  ┌──────────────┐ ┌──────────────┐ ┌──────────────┐ ┌──────────────┐     │  │
│  │  │ Global       │ │ Worldwide    │ │ Total        │ │ Average      │     │  │
│  │  │ Revenue      │ │ Quota        │ │ Deals        │ │ Win Rate     │     │  │
│  │  │ Attainment   │ │ Attainment   │ │ Closed       │ │              │     │  │
│  │  └──────────────┘ └──────────────┘ └──────────────┘ └──────────────┘     │  │
│  │  ───────────────────────────────────────────────────────────────────────  │  │
│  │  COUNTRY PERFORMANCE RANKING:                                             │  │
│  │  ┌────────────────────────────────────────────────────────────────────────┐  │  │
│  │  │ Rank │ Country │ Revenue │ Quota │ Attain │ Deals │ Win% │ Trend │[View]│  │  │
│  │  ├────────────────────────────────────────────────────────────────────────┤  │  │
│  │  │ 🥇   │🇿🇦 ZA    │ $2.1M   │ $2.0M │ 105%   │ 312   │ 38%  │ 📈   │[View]│  │  │
│  │  │ 🥈   │🇰🇪 KE    │ $950K   │ $900K │ 106%   │ 189   │ 35%  │ 📈   │[View]│  │  │
│  │  │ 🥉   │🇳🇬 NG    │ $1.2M   │ $1.3M │  92%   │ 245   │ 32%  │ ➡️   │[View]│  │  │
│  │  └────────────────────────────────────────────────────────────────────────┘  │  │
│  │  ───────────────────────────────────────────────────────────────────────  │  │
│  │  PERFORMANCE CHARTS:                                                       │  │
│  │  • Revenue vs Quota by Country                                               │  │
│  │  • Win Rate by Country                                                      │  │
│  │  • Deals Closed by Country                                                   │  │
│  │  • Revenue Trend (Quarterly)                                                 │  │
│  │  ───────────────────────────────────────────────────────────────────────  │  │
│  │  TOP GLOBAL PERFORMERS:                                                    │  │
│  │  ┌────────────────────────────────────────────────────────────────────────┐  │  │
│  │  │ Rank │ Name          │ Country│ Dept  │ Revenue │ Quota │ Attain│[View]│  │  │
│  │  ├────────────────────────────────────────────────────────────────────────┤  │  │
│  │  │ 🥇   │ Sarah Williams│🇿🇦 ZA  │ Ent   │ $280K   │ $200K │ 140% │[View]│  │  │
│  │  │ 🥈   │ John Doe     │🇳🇬 NG  │ Ent   │ $250K   │ $200K │ 125% │[View]│  │  │
│  │  │ 🥉   │ Jane Smith   │🇰🇪 KE   │ SMB   │ $180K   │ $150K │ 120% │[View]│  │  │
│  │  └────────────────────────────────────────────────────────────────────────┘  │  │
│  └───────────────────────────────────────────────────────────────────────────┘  │
│                                                                                 │
│  USER ACTIONS:                                                                  │
│  ────────────                                                                   │
│  1. Change period selector            → Reload performance data           │
│                                          • Update rankings                │
│                                          • Update charts                   │
│                                                                                 │
│  2. Change "View By" selector         → Group data by selection            │
│                                          • Country                       │
│                                          • Department                    │
│                                          • Product                       │
│                                          • Territory                     │
│                                                                                 │
│  3. Click country ranking row          → Navigate to /countries/:code      │
│                                          • Filter to performance view      │
│                                                                                 │
│  4. Click performer                   → Navigate to /teams/:memberId       │
│                                          • Show detailed performance       │
│                                                                                 │
│  5. Click export button               → Generate performance report       │
│                                          • Include all metrics             │
│                                          • Include rankings               │
│                                          • Include charts                  │
│                                                                                 │
└─────────────────────────────────────────────────────────────────────────────────┘
```

---

## 8. FORECASTING FLOWS

### 8.1 Forecast Generation Flow

```
┌─────────────────────────────────────────────────────────────────────────────────┐
│                        FORECAST GENERATION FLOW                               │
├─────────────────────────────────────────────────────────────────────────────────┤
│                                                                                 │
│  NAVIGATION: /forecasting                                                        │
│                                                                                 │
│  TRIGGER: User clicks "Generate New Forecast"                                  │
│                                                                                 │
│  MODAL FLOW:                                                                    │
│  ┌───────────────────────────────────────────────────────────────────────────┐  │
│  │  Generate Sales Forecast                                                   │  │
│  │  ┌────────────────────────────────────────────────────────────────────┐  │  │
│  │  │  Forecast Period:                                                    │  │  │
│  │  │  [Q1 2025 ▼]                                                        │  │  │
│  │  │                                                                    │  │  │
│  │  │  Scenario:                                                          │  │  │
│  │  │  ○ Likely (default)                                                │  │  │
│  │  │  ○ Best Case                                                       │  │  │
│  │  │  ○ Worst Case                                                      │  │  │
│  │  │  ○ Custom                                                          │  │  │
│  │  │                                                                    │  │  │
│  │  │  Options:                                                           │  │  │
│  │  │  [✓] Include best case analysis                                    │  │  │
│  │  │  [✓] Include worst case analysis                                   │  │  │
│  │  │  [✓] Use historical trends                                        │  │  │
│  │  │  [✓] Include country breakdown                                    │  │  │
│  │  │                                                                    │  │  │
│  │  │  Countries to include:                                              │  │  │
│  │  │  [☑] 🇳🇬 Nigeria  [☑] 🇰🇪 Kenya  [☑] 🇬🇭 Ghana  [☑] 🇿🇦 S. Africa      │  │  │
│  │  │  [+ Select All]  [Clear All]                                       │  │  │
│  │  │                                                                    │  │  │
│  │  │  Advanced options: [Show/Hide]                                     │  │  │
│  │  │  • Forecast model selection                                         │  │  │
│  │  │  • Seasonality adjustments                                         │  │  │
│  │  │  • Currency conversion                                               │  │  │
│  │  │                                                                    │  │  │
│  │  │                                               [Cancel]  [Generate]   │  │  │
│  │  └────────────────────────────────────────────────────────────────────┘  │  │
│  └───────────────────────────────────────────────────────────────────────────┘  │
│                                                                                 │
│  GENERATION PROCESS:                                                            │
│  ┌───────────────────────────────────────────────────────────────────────────┐  │
│  │  1. POST /forecasting/generate                                            │  │
│  │  2. Show loading state: "Generating forecast..."                          │  │
│  │  3. Backend processes:                                                      │  │
│  │     • Aggregates pipeline data from all countries                       │  │
│  │     • Applies forecast model                                            │  │
│  │     • Calculates best/worst/likely scenarios                             │  │
│  │     • Generates country-level breakdowns                                │  │
│  │     • Calculates accuracy metrics                                         │  │
│  │  4. Response: GlobalSalesForecast                                        │  │
│  │  5. Display forecast results                                              │  │
│  │     └─ Navigate to /forecasting with new forecast                      │  │
│  └───────────────────────────────────────────────────────────────────────────┘  │
│                                                                                 │
│  FORECAST RESULT DISPLAY:                                                       │
│  ┌───────────────────────────────────────────────────────────────────────────┐  │
│  │  Forecast Summary                                                          │  │
│  │  • Forecast: $10.5M                                                       │  │
│  │  • Best Case: $12.8M (+22%)                                               │  │
│  │  • Worst Case: $8.2M (-22%)                                               │  │
│  │  • Weighted Pipeline: $25M                                                 │  │
│  │  • Accuracy: 94.2%                                                        │  │
│  │  ───────────────────────────────────────────────────────────────────────  │  │
│  │  Forecast by Country                                                         │  │
│  │  • Country breakdown with forecast/best/worst                           │  │
│  │  ───────────────────────────────────────────────────────────────────────  │  │
│  │  Forecast by Stage                                                           │  │
│  │  • Stage contribution to forecast                                         │  │
│  │  ───────────────────────────────────────────────────────────────────────  │  │
│  │  Forecast Trend Chart                                                        │  │
│  │  • Visual representation over time                                          │  │
│  └───────────────────────────────────────────────────────────────────────────┘  │
│                                                                                 │
│  ACTIONS:                                                                      │
│  • [Download Forecast] → Export to Excel/PDF                                │
│  • [Share with Team] → Email forecast to stakeholders                       │
│  • [Set as Baseline] → Use this forecast for tracking                       │
│  • [Generate Variance Report] → Compare actual vs forecast                    │
│                                                                                 │
└─────────────────────────────────────────────────────────────────────────────────┘
```

---

## 9. REPORTS FLOWS

### 9.1 Report Generation Flow

```
┌─────────────────────────────────────────────────────────────────────────────────┐
│                        REPORT GENERATION FLOW                                  │
├─────────────────────────────────────────────────────────────────────────────────┤
│                                                                                 │
│  NAVIGATION: /reports                                                           │
│                                                                                 │
│  OPTIONS FOR GENERATING REPORTS:                                               │
│                                                                                 │
│  1. QUICK REPORT TEMPLATES                                                     │
│     ┌───────────────────────────────────────────────────────────────────────┐  │
│     │ Executive Summary → Generate automatically                              │  │
│     │ Country Comparison → Select countries → Generate                      │  │
│     │ Pipeline Analysis → Generate with current data                       │  │
│     │ Performance Report → Select period → Generate                         │  │
│     └───────────────────────────────────────────────────────────────────────┘  │
│                                                                                 │
│  2. CUSTOM REPORT BUILDER                                                     │
│     ┌───────────────────────────────────────────────────────────────────────┐  │
│     │ Step 1: Report Configuration                                           │  │
│     │ ┌────────────────────────────────────────────────────────────────────┐  │
│     │ │ Report Name: [Monthly Executive Summary                            │  │
│     │ │ Report Type: [Executive Dashboard ▼]                              │  │
│     │ │                                                                    │  │
│     │ │ Select Metrics:                                                    │  │
│     │ │ [☑] Revenue  [☑] Pipeline  [☑] Deals  [☑] Win Rate               │  │
│     │ │ [☑] Quota Attainment  [☑] Forecast  [☑] Top Performers        │  │
│     │ │                                                                    │  │
│     │ │ Select Countries:                                                   │  │
│     │ │ [☑] All Countries  [☑] Top 5 by Revenue                            │  │
│     │ │                                                                    │  │
│     │ │ Period: [This Month ▼]                                             │  │
│     │ │                                                                    │  │
│     │ │ Visualizations:                                                    │  │
│     │ │ [☑] Charts  [☑] Tables  [☑] Summary Cards                          │  │
│     │ │                                                                    │  │
│     │ │ Export Format: [PDF ▼]                                            │  │
│     │ │                                                                    │  │
│     │ │                                          [← Back]  [Next →]         │  │
│     │ └────────────────────────────────────────────────────────────────────┘  │
│     │                                                                    │  │
│     │ Step 2: Review & Generate                                            │  │
│     │ ┌────────────────────────────────────────────────────────────────────┐  │
│     │ │ Report Preview:                                                    │  │
│     │ │ ┌────────────────────────────────────────────────────────────────┤  │
│     │ │ │ Report: Monthly Executive Summary                              │  │
│     │ │ │ Type: Executive Dashboard                                      │  │
│     │ │ │ Period: February 2025                                          │  │
│     │ │ │ Countries: All (8)                                             │  │
│     │ │ │ Estimated pages: 12                                            │  │
│     │ │ │ Estimated size: 2.5 MB                                         │  │
│     │ │ └────────────────────────────────────────────────────────────────┤  │
│     │ │                                                                    │  │
│     │ │ Schedule (optional):                                               │  │
│     │ │ [☑] Run once only  ○ Schedule recurring                           │  │
│     │ │                                                                    │  │
│     │ │                                          [← Back]  [Generate]      │  │
│     │ └────────────────────────────────────────────────────────────────────┘  │
│     └───────────────────────────────────────────────────────────────────────┘  │
│                                                                                 │
│  3. GENERATION PROCESS                                                          │
│     ┌───────────────────────────────────────────────────────────────────────┐  │
│     │  POST /reports/:id/generate                                         │  │
│     │ → Response: { reportId: "rep-001", status: "generating" }          │  │
│     │ → Show progress: "Generating report..."                             │  │
│     │ → WebSocket updates on progress                                      │  │
│     │ → On complete: Download file + show success                         │  │
│     └───────────────────────────────────────────────────────────────────────┘  │
│                                                                                 │
│  4. SCHEDULED REPORTS                                                           │
│     ┌───────────────────────────────────────────────────────────────────────┐  │
│     │ Configure recurring reports:                                         │  │
│     │ • Frequency: Daily, Weekly, Monthly, Quarterly                     │  │
│     │ • Recipients: Email distribution list                              │  │
│     │ • Format: PDF attachment or link                                   │  │
│     │ • Active: Enable/disable scheduled reports                           │  │
│     └───────────────────────────────────────────────────────────────────────┘  │
│                                                                                 │
└─────────────────────────────────────────────────────────────────────────────────┘
```

---

## 10. SETTINGS FLOWS

### 10.1 Country Configuration Flow

```
┌─────────────────────────────────────────────────────────────────────────────────┐
│                        COUNTRY CONFIGURATION FLOW                             │
├─────────────────────────────────────────────────────────────────────────────────┤
│                                                                                 │
│  NAVIGATION: /settings/countries                                               │
│                                                                                 │
│  PAGE:                                                                          │
│  ┌───────────────────────────────────────────────────────────────────────────┐  │
│  │  HEADER: Settings > Country Configuration                                 │  │
│  │  [+ Add Country]                                                          │  │
│  │  ───────────────────────────────────────────────────────────────────────  │  │
│  │  ACTIVE COUNTRIES:                                                         │  │
│  │  ┌────────────────────────────────────────────────────────────────────────┐  │  │
│  │  │ Country │ Status  │ Currency │ Quota │ Director │ Actions │          │  │  │
│  │  ├────────────────────────────────────────────────────────────────────────┤  │  │
│  │  │ 🇳🇬 Nigeria│ Active │ NGN     │ $1.3M │ John D. │[Edit][Remove]│          │  │
│  │  │ 🇰🇪 Kenya │ Active │ KES     │ $950K │ Jane S. │[Edit][Remove]│          │  │
│  │  │ 🇬🇭 Ghana │ Active │ GHS     │ $800K │ Bob J.  │[Edit][Remove]│          │  │
│  │  │ 🇿🇦 S.Afric│ Active │ ZAR     │ $2.1M │ Sarah W.│[Edit][Remove]│          │  │
│  │  └────────────────────────────────────────────────────────────────────────┘  │  │
│  └───────────────────────────────────────────────────────────────────────────┘  │
│                                                                                 │
│  ADD COUNTRY FLOW:                                                              │
│  ┌───────────────────────────────────────────────────────────────────────────┐  │
│  │  1. Click [+ Add Country]                                                   │  │
│  │  2. Select from available countries (not yet configured)                    │  │
│  │  3. Configure country:                                                      │  │
│  │     • Country name and code                                                │  │
│  │     • Local currency                                                       │  │
│  │     • Revenue quota                                                        │  │
│  │     • Assign country director                                             │  │
│  │     • Regional assignment                                                 │  │
│  │  4. Save → Create country configuration                                   │  │
│  │     → Email notification to country director                             │  │
│  │     → Country appears in global dashboard                               │  │
│  └───────────────────────────────────────────────────────────────────────────┘  │
│                                                                                 │
│  EDIT COUNTRY FLOW:                                                             │
│  ┌───────────────────────────────────────────────────────────────────────────┐  │
│  │  1. Click [Edit] on country row                                          │  │
│  │  2. Update configuration:                                                   │  │
│  │     • Adjust quota                                                        │  │
│  │     • Change director                                                     │  │
│  │     • Modify regional assignment                                         │  │
│  │     • Update status (Active/Inactive)                                   │  │
│  │  3. Save → Update country configuration                                   │  │
│  │     → Notify director of changes                                        │  │
│  │     → Update dashboard calculations                                       │  │
│  └───────────────────────────────────────────────────────────────────────────┘  │
│                                                                                 │
└─────────────────────────────────────────────────────────────────────────────────┘
```

---

## 11. PERMISSION MATRIX

### 11.1 Page Access by Role

| Page | VP Sales | Global Director | Regional Manager | Analyst | Ops Manager |
|------|----------|-----------------|------------------|---------|-------------|
| /global-overview | Full | Full | Regions Only | Read | Read |
| /countries | Full | Full | Regions Only | Read | Read |
| /countries/compare | Full | Full | Regions Only | Read | Read |
| /countries/:code | Full | Full | Regions Only | Read | Read |
| /pipeline | Full | Full | Regions Only | Read | All |
| /pipeline/major-deals | Full | Full | Regions Only | Read | All |
| /performance | Full | Full | Regions Only | Read | All |
| /forecasting | Full | Full | Regions Only | Read | Full |
| /reports | Full | Full | Regions Only | Read | All |
| /settings | Full | Full | None | None | Full |

### 11.2 Action Permissions by Role

| Action | VP Sales | Global Director | Regional Manager | Analyst | Ops Manager |
|--------|----------|-----------------|------------------|---------|-------------|
| View all countries | ✓ | ✓ | Assigned only | Read | Read |
| View global metrics | ✓ | ✓ | Aggregated | Read | Read |
| Compare countries | ✓ | ✓ | Assigned only | Read | Read |
| Generate forecast | ✓ | ✓ | Assigned only | Read | ✓ |
| Approve major deals | ✓ | ✓ | Assigned only | ✗ | ✗ |
| Configure countries | ✓ | ✓ | ✗ | ✗ | ✓ |
| Set quotas | ✓ | ✓ | ✗ | ✗ | ✓ |
| Generate reports | ✓ | ✓ | Assigned only | ✓ | ✓ |
| Schedule reports | ✓ | ✓ | ✗ | ✓ | ✓ |
| Edit commission structure | ✓ | ✓ | ✗ | ✗ | ✓ |
| View commission data | Own | All | Team | Read | All |

---

## SUMMARY

### Sales Domain Documentation Complete

**Business Domain - Country-Sales-Dashboard (4 files)** ✓
- 01_UI_Flow_Documentation.md ✓
- 02_Wireframes_Documentation.md ✓
- 03_Mock_Flow_Documentation.md ✓
- 04_Page_By_Page_Flow_Documentation.md ✓

**Management Domain - HQ Sales Dashboard (4 files)** ✓
- 01_UI_Flow_Documentation.md ✓
- 02_Wireframes_Documentation.md ✓
- 03_Mock_Flow_Documentation.md ✓
- 04_Page_By_Page_Flow_Documentation.md ✓

**Total: 12 documentation files completed for Sales Domain**

---

## NEXT STEPS

**Remaining Management Domain Documentation:**
2. Finance-department (Finance) - 4 files needed
3. Global-business-management (GBM) - 4 files needed
4. Customer-support (Support) - 4 files needed
5. Digital-marketing (Marketing) - 4 files needed
6. System-Administrator (Admin) - 4 files needed

**Total Remaining: 24 documentation files (6 domains × 4 files each)**

---

**Document End**
