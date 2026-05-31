# HQ FINANCE DASHBOARD - UI FLOW DOCUMENTATION

**Domain:** Management Domain
**Application:** HQ Finance Web Dashboard
**Version:** 1.0
**Date:** 2026-02-17
**Path:** `Management-domain/Finance-department/Frontends/Web/finance-web-dashboard/`

---

## TABLE OF CONTENTS

1. [Overview](#overview)
2. [Application Entry Flows](#application-entry-flows)
3. [Authentication Flows](#authentication-flows)
4. [Dashboard Navigation Flows](#dashboard-navigation-flows)
5. [Feature-Specific Flows](#feature-specific-flows)
6. [Exit Flows](#exit-flows)

---

## 1. OVERVIEW

### 1.1 User Roles & Entry Points

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                     HQ FINANCE DASHBOARD USERS                          │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│   ┌───────────────┐    ┌───────────────┐    ┌───────────────┐           │
│   │ VP Finance /  │    │  Global       │    │  Regional     │           │
│   │      CFO      │    │  Finance      │    │  Finance      │           │
│   │               │    │  Director     │    │  Manager      │           │
│   │ Executive     │    │               │    │               │           │
│   │ Access        │    │ Global Access  │    │ Region Access │           │
│   └───────────────┘    └───────────────┘    └───────────────┘           │
│         │                      │                      │                      │
│         └──────────────────────┴──────────────────────┘                      │
│                                │                                             │
│                        ┌───────▼───────┐                                    │
│                        │  HQ Finance    │                                    │
│                        │  Dashboard     │                                    │
│                        └───────────────┘                                    │
│                                                                              │
│   ┌───────────────┐    ┌───────────────┐    ┌───────────────┐           │
│   │   HQ          │    │   Finance     │    │  Finance      │           │
│   │  Financial    │    │  Operations   │    │   Analyst    │           │
│   │  Analyst      │    │  Manager      │    │               │           │
│   └───────────────┘    └───────────────┘    └───────────────┘           │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

### 1.2 Dashboard Structure

```
HQ Finance Web Dashboard
│
├── Global Overview
│   ├── Financial Health Score
│   ├── Global KPIs (Revenue, Expenses, Net Income, Cash, Debt)
│   ├── Country Performance Grid
│   ├── Consolidated P&L Summary
│   ├── Executive Alerts
│   └── Pending Approvals
│
├── Countries
│   ├── Multi-Country Comparison
│   ├── Country Detail View
│   ├── Regional Performance
│   └── Currency Exchange Rates
│
├── Global Revenue
│   ├── Revenue by Country
│   ├── Revenue Trends
│   ├── Revenue Forecasts (AI-Powered)
│   ├── Revenue by Business Line
│   └── AR Aging Analysis
│
├── Global Expenses
│   ├── Expense by Country
│   ├── OpEx vs CapEx
│   ├── Expense Categories
│   ├── AP Aging Analysis
│   └── Cost Optimization Insights (AI-Powered)
│
├── Budgets & Forecasts
│   ├── Global Budget Overview
│   ├── Budget vs Actual
│   ├── Budget Allocation
│   ├── Variance Analysis
│   └── AI Forecasts
│
├── Consolidated Reports
│   ├── P&L Consolidation
│   ├── Balance Sheet
│   ├── Cash Flow Statement
│   ├── Custom Report Builder
│   └── Report Scheduler
│
├── Global Treasury
│   ├── Cash Position
│   ├── Cash Flow Forecast
│   ├── Banking Partners
│   ├── Inter-Company Transfers
│   └── FX Exposure Management
│
├── Tax & Compliance
│   ├── Global Tax Summary
│   ├── Tax by Country
│   ├── Compliance Status
│   ├── Audit Readiness
│   └── Regulatory Filings
│
└── Settings
    ├── User Management
    ├── Role Permissions
    ├── Notification Settings
    └── Audit Logs
```

### 1.3 Ecosystem Context

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                      FINANCE ECOSYSTEM ARCHITECTURE                      │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│  ┌─────────────────────────────────────────────────────────────────────┐   │
│  │                    MANAGEMENT DOMAIN                                 │   │
│  │  ┌──────────────────────────────────────────────────────────────┐   │   │
│  │  │  HQ FINANCE DASHBOARD (THIS DOCUMENTATION)                     │   │   │
│  │  │  • Global financial oversight across ALL countries            │   │   │
│  │  │  • Receives reports from each Country Finance Manager           │   │   │
│  │  │  • Multi-country view, strategic financial decisions          │   │   │
│  │  │  • AI-powered forecasting and anomaly detection               │   │   │
│  │  └──────────────────────────────────────────────────────────────┘   │   │
│  └─────────────────────────────────────────────────────────────────────┘   │
│                                    ↕ REPORTS TO / GUIDES                   │
│  ┌─────────────────────────────────────────────────────────────────────┐   │
│  │                    BUSINESS DOMAIN                                 │   │
│  │  ┌──────────────────────────────────────────────────────────────┐   │   │
│  │  │  Country-Finance-Dashboard (Each Country)                     │   │   │
│  │  │  • Country-level financial operations                         │   │   │
│  │  │  • Each Country Finance Manager assigned to ONE country       │   │   │
│  │  │  • Reports financial data to HQ                               │   │   │
│  │  │  • Nigeria • Kenya • South Africa • Ghana • Ireland • etc.    │   │   │
│  │  └──────────────────────────────────────────────────────────────┘   │   │
│  └─────────────────────────────────────────────────────────────────────┘   │
│                                                                              │
│  ┌─────────────────────────────────────────────────────────────────────┐   │
│  │                    FOUNDATION DOMAIN                               │   │
│  │  • AI Services: Forecasting, Anomaly Detection, Fraud Prevention  │   │
│  │  • Data Aggregation Services                                        │   │
│  │  • Currency Exchange Rate Services                                 │   │
│  └─────────────────────────────────────────────────────────────────────┘   │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## 2. APPLICATION ENTRY FLOWS

### 2.1 Main Entry Flow

```
┌──────────┐
│   User   │
│  Opens   │
│   App    │
└─────┬────┘
      │
      ▼
┌─────────────────────────────────────────────────────────────────────────────┐
│                    CHECK AUTHENTICATION                                     │
│  ┌─────────────────────────────────────────────────────────────────────┐  │
│  │  Is user already logged in?                                         │  │
│  │  (Check: localStorage.authToken, cookie)                            │  │
│  └─────────────────────────────────────────────────────────────────────┘  │
└─────┬──────────────────────────────────────────────────────────────────┬─────┘
      │ YES                                                                │ NO
      ▼                                                                    ▼
┌───────────────────┐                                            ┌──────────────────┐
│  RESTORE SESSION  │                                            │  LOGIN PAGE      │
│  • Load user data │                                            │                  │
│  • Load role      │                                            │  [See Section 3] │
│  • Load scope     │                                            └──────────────────┘
│  • Navigate to    │                                                       │
│    dashboard      │                                                       │
└─────────┬─────────┘                                                       │
          │                                                                 │
          ▼                                                                 ▼
┌──────────────────────────────────────────────────────────────────────────────┐
│                        LOAD FINANCE CONTEXT                                 │
│  ┌────────────────────────────────────────────────────────────────────────┐ │
│  │  Determine user's finance access scope:                              │ │
│  │                                                                      │ │
│  │  • VP Finance / CFO → Global access (ALL countries)                  │ │
│  │  • Global Finance Director → Global access (ALL countries)          │ │
│  │  • Regional Finance Manager → Regional access (assigned regions)    │ │
│  │  • HQ Financial Analyst → Read-only global access                   │ │
│  │  • Finance Operations Manager → Operational access                  │ │
│  └────────────────────────────────────────────────────────────────────────┘ │
└─────────────────────────────────────────────────────────────────────────────┘
      │
      ▼
┌─────────────────────────────────────────┐
│ Navigate to Dashboard                  │
│ Based on user role and scope           │
└─────────────────────────────────────────┘
```

### 2.2 Scope Determination Flow

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                    FINANCE SCOPE DETERMINATION                            │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│  User authenticated successfully                                            │
│      │                                                                      │
│      ▼                                                                      │
│  ┌─────────────────────────────────────────────────────────────────────┐    │
│  │  API: GET /api/v1/finance/auth/me                                   │    │
│  │  Response: {                                                         │    │
│  │    user,                                                              │    │
│  │    role,                                                              │    │
│  │    financeScope: {                                                   │    │
│  │      type: "GLOBAL" | "REGIONAL" | "READ_ONLY",                       │    │
│  │      countries?: ["NGA", "KEN", "ZAF", ...],  // For Regional        │    │
│  │      regions?: ["West Africa", "East Africa", ...],                 │    │
│  │      permissions: ["VIEW_ALL", "APPROVE", "EXPORT", ...]            │    │
│  │    }                                                                 │    │
│  │  }                                                                   │    │
│  └─────────────────────────────────────────────────────────────────────┘    │
│                              │                                              │
│                              ▼                                              │
│  ┌─────────────────────────────────────────────────────────────────────┐    │
│  │  Store:                                                              │    │
│  │  • store.setUser(user)                                               │    │
│  │  • store.setFinanceScope(financeScope)                               │    │
│  └─────────────────────────────────────────────────────────────────────┘    │
│                              │                                              │
│                              ▼                                              │
│  ┌─────────────────────────────────────────────────────────────────────┐    │
│  │  Load Finance Dashboard Data                                        │    │
│  │  • GET /api/v1/finance/dashboard/global                            │    │
│  │  • GET /api/v1/finance/countries/summary                            │    │
│  │  • GET /api/v1/finance/budgets/summary                              │    │
│  │  • GET /api/v1/finance/treasury/position                            │    │
│  │  • GET /api/v1/finance/alerts/pending                               │    │
│  └─────────────────────────────────────────────────────────────────────┘    │
│                              │                                              │
│                              ▼                                              │
│  ┌─────────────────────────────────────────────────────────────────────┐    │
│  │  Render Dashboard                                                   │    │
│  │  • Apply scope filter to data                                       │    │
│  │  • Show/hide features based on permissions                          │    │
│  │  • Display country list according to scope                         │    │
│  └─────────────────────────────────────────────────────────────────────┘    │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## 3. AUTHENTICATION FLOWS

### 3.1 Login Flow

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                          LOGIN SCREEN                                     │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│                    ┌─────────────────┐                                    │
│                    │   GOGIDIX       │                                    │
│                    │   Finance       │                                    │
│                    │   Dashboard     │                                    │
│                    └─────────────────┘                                    │
│                                                                              │
│                    ┌─────────────────────────────┐                          │
│                    │  Email / Username          │                          │
│                    └─────────────────────────────┘                          │
│                                                                              │
│                    ┌─────────────────────────────┐                          │
│                    │  Password        [👁️]      │                          │
│                    └─────────────────────────────┘                          │
│                                                                              │
│                    [ ] Remember me                                         │
│                    Forgot password?                                        │
│                                                                              │
│                    ┌─────────────────────────────┐                          │
│                    │     SIGN IN                │                          │
│                    └─────────────────────────────┘                          │
│                                                                              │
│                    OR                                                       │
│                                                                              │
│                    [ SSO with Microsoft ]                                   │
│                    [ SSO with Google ]                                      │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
      │
      │ User enters credentials and clicks Sign In
      ▼
┌─────────────────────────────────────────────────────────────────────────────┐
│                      VALIDATE CREDENTIALS                                 │
│  ┌─────────────────────────────────────────────────────────────────────┐  │
│  │  POST /api/v1/finance/auth/login                                   │  │
│  │  Request: { email, password }                                      │  │
│  │  Response: { token, user, role, financeScope }                     │  │
│  └─────────────────────────────────────────────────────────────────────┘  │
└────┬──────────────────────────────────────────────────────────────────┬─────┘
     │ VALID                                                              │ INVALID
     ▼                                                                    ▼
┌─────────────────┐                                          ┌─────────────────┐
│  Store Token    │                                          │  Show Error:    │
│  Load User Data │                                          │  "Invalid       │
│  Load Scope     │                                          │   credentials"  │
│  Navigate to    │                                          │  Allow retry    │
│  Dashboard      │                                          └─────────────────┘
└─────────────────┘
```

### 3.2 Logout Flow

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                       LOGOUT INITIATED                                    │
│  Trigger: User clicks logout from user menu                               │
└─────┬───────────────────────────────────────────────────────────────────────┘
      │
      ▼
┌─────────────────────────────────────────────────────────────────────────────┐
│                      CONFIRM LOGOUT                                       │
│  ┌─────────────────────────────────────────────────────────────────────┐  │
│  │  Are you sure you want to logout?                                 │  │
│  │  [Cancel]  [Logout]                                                │  │
│  └─────────────────────────────────────────────────────────────────────┘  │
└─────┬───────────────────────────────────────────────────────────────────────┘
      │                          │
      │ Cancel                   │ Confirm Logout
      ▼                          ▼
┌─────────────────┐      ┌─────────────────────────────────────────────┐
│  Return to      │      │              CLEAR SESSION                   │
│  Dashboard      │      │  • Call POST /api/v1/finance/auth/logout     │
│                 │      │  • Clear localStorage (token, user, scope)   │
│                 │      │  • Clear sessionStorage                     │
│                 │      │  • Clear cookies                            │
│                 │      │  • Close WebSocket connections              │
│                 │      │  • Navigate to Login screen                 │
│                 │      └─────────────────────────────────────────────┘
└─────────────────┘
```

---

## 4. DASHBOARD NAVIGATION FLOWS

### 4.1 Global Overview Navigation Flow

```
                    ┌─────────────────┐
                    │ GLOBAL OVERVIEW │
                    │    (Home)       │
                    └────────┬────────┘
                             │
        ┌────────────────────┼────────────────────┐
        │                    │                    │
        ▼                    ▼                    ▼
┌───────────────┐    ┌───────────────┐    ┌───────────────┐
│ Sidebar Nav   │    │ Top Nav       │    │ Main Content  │
│               │    │               │    │               │
│ • Overview    │    │ Scope Filter  │    │ Financial KPI │
│ • Countries   │    │ Currency      │    │ Cards         │
│ • Revenue     │    │ Period Select │    │ Country Grid  │
│ • Expenses    │    │ Notif Bell    │    │ P&L Summary   │
│ • Budgets     │    │ User Menu     │    │ Alerts        │
│ • Reports     │    │ Connection    │    │ Approvals     │
│ • Treasury    │    │               │    │               │
│ • Tax         │    │               │    │               │
│ • Settings    │    │               │    │               │
└───────────────┘    └───────────────┘    └───────────────┘
        │                    │                    │
        │                    │                    │
        └────────────────────┼────────────────────┘
                             │
                             ▼
        ┌──────────────────────────────────────────────────────────────┐
        │              NAVIGATION OPTIONS                               │
        ├──────────────────────────────────────────────────────────────┤
        │  1. Click Sidebar Item → Navigate to Section              │
        │  2. Click KPI Card → Drill down to detail view               │
        │  3. Click Country Card → Open country detail view           │
        │  4. Click Alert → Open alert modal                          │
        │  5. Change Currency → Convert all monetary values          │
        │  6. Change Period → Refresh all data with new time range    │
        └──────────────────────────────────────────────────────────────┘
```

### 4.2 Financial KPI Drill-Down Flow

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                  GLOBAL OVERVIEW - FINANCIAL KPIs                         │
│  ┌────────┐ ┌────────┐ ┌────────┐ ┌────────┐ ┌────────┐ ┌────────┐      │
│  │        │ │        │ │        │ │        │ │        │ │        │      │
│  │  💰    │ │  💵    │ │  💳    │ │  🏦    │ │  📊    │ │  ⚠️    │      │
│  │        │ │        │ │        │ │        │ │        │ │        │      │
│  │Revenue │ │Expenses│ │Net Inc.│  Cash   │  Debt   │ Alerts │      │
│  │ $42.5M │ │ $32.1M │ │ $10.4M │ │ $15.2M │ │  $5.1M │ │   3    │      │
│  │  ▲ 8%  │ │  ▼ 3%  │ │  ▲ 12% │ │ ● OK   │ │  ● OK  │ │  View   │      │
│  └────────┘ └────────┘ └────────┘ └────────┘ └────────┘ └────────┘      │
└─────────────────────────────────────────────────────────────────────────────┘
                              ↑
                              │ User clicks Revenue KPI
                              ▼
┌─────────────────────────────────────────────────────────────────────────────┐
│                      REVENUE KPI DETAIL MODAL                            │
├─────────────────────────────────────────────────────────────────────────────┤
│  ┌─────────────────────────────────────────────────────────────────────┐   │
│  │  💰 GLOBAL REVENUE DETAILS                                         │   │
│  │                                                               [×] │   │
│  │  ─────────────────────────────────────────────────────────────   │   │
│  │  Current:        $42,500,000                                       │   │
│  │  Target:         $40,000,000                                       │   │
│  │  Variance:       +$2,500,000 (+6.25%) ●                            │   │
│  │  Period:         February 2026                                     │   │
│  │  Status:         ● ON TRACK                                       │   │
│  │                                                                       │   │
│  │  ┌─────────────────────────────────────────────────────────────┐   │   │
│  │  │  Revenue Trend (Last 30 Days)                      [Full ▼] │   │   │
│  │  │  ┌───────────────────────────────────────────────────────┐ │   │   │
│  │  │  │ $43M│ ┌─                                                 │ │   │   │
│  │  │  │ $42M│   ┌─                                             │ │   │   │
│  │  │  │ $41M│     ──────────                                    │ │   │   │
│  │  │  │     └────────────────────────────────                 │ │   │   │
│  │  │  │        Feb 1     Feb 8     Feb 15                     │ │   │   │
│  │  │  └───────────────────────────────────────────────────────┘ │   │   │
│  │  └─────────────────────────────────────────────────────────────┘   │   │
│  │                                                                       │   │
│  │  ┌───────────────┐ ┌───────────────┐ ┌───────────────┐             │   │
│  │  │ By Country    │ │ By Business   │ │ By Currency   │             │   │
│  │  ├───────────────┤ ├───────────────┤ ├───────────────┤             │   │
│  │  │ Nigeria $8.2M │ │ E-comm $15.1M  │ │ EUR $18.5M    │             │   │
│  │  │ Kenya  $5.1M  │ │ Logistics $12M │ │ GBP $8.2M     │             │   │
│  │  │ SA      $4.8M  │ │ Services $10M  │ │ USD $8.1M     │             │   │
│  │  │ Others  $24.4M │ │ Others $5.4M   │ │ Others $7.7M   │             │   │
│  │  └───────────────┘ └───────────────┘ └───────────────┘             │   │
│  │                                                                       │   │
│  │  Drivers:                                                             │   │
│  │  • Enterprise sales growth: +$1.8M                                 │   │
│  │  • New customer acquisition: +$1.2M                                │   │
│  │  • Seasonal demand increase: +$800K                                 │   │
│  │                                                                       │   │
│  │  AI Forecast: $48.2M next month (95% confidence)                     │   │
│  │                                                                       │   │
│  │  [Set Alert] [View Full Report] [Export Data] [Close]                │   │
│  └─────────────────────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────────────────────┘
```

### 4.3 Country Comparison Navigation Flow

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                  GLOBAL OVERVIEW - COUNTRY GRID                            │
│  ┌─────────────────────────────────────────────────────────────────────┐   │
│  │  🌍 COUNTRY FINANCIAL PERFORMANCE                                 │   │
│  │  ┌───────────────────────────────────────────────────────────────┐ │   │
│  │  │ ┌─────────┐ ┌─────────┐ ┌─────────┐ ┌─────────┐ ┌─────────┐ │ │   │
│  │  │ │  🇳🇬    │ │  🇰🇪    │ │  🇿🇦    │ │  🇬🇭    │ │  🇮🇪    │ │ │   │
│  │  │ │Nigeria  │ │ Kenya   │ │S.Africa│ │ Ghana   │ │ Ireland │ │ │   │
│  │  │ │         │ │         │ │         │ │         │ │         │ │ │   │
│  │  │ │ $8.2M   │ │ $5.1M   │ │ $4.8M   │ │ $3.2M   │ │ $2.9M   │ │ │   │
│  │  │ │ ▲ 12%   │ │ ▲ 8%    │ │ ▼ 3%    │ │ ▲ 15%   │ │ ▲ 5%    │ │ │   │
│  │  │ │  ● OK   │ │  ● OK   │ │  ⚠️     │ │  ● OK   │ │  ● OK   │ │ │   │
│  │  │ │ 94%     │ │ 91%     │ │ 78%     │ │ 96%     │ │ 89%     │ │ │   │
│  │  │ │[View]   │ │[View]   │ │[View]   │ │[View]   │ │[View]   │ │ │   │
│  │  │ └─────────┘ └─────────┘ └─────────┘ └─────────┘ └─────────┘ │ │   │
│  │  └───────────────────────────────────────────────────────────────┘ │   │
│  │  [View All Countries]                                                │   │
│  └─────────────────────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────────────────────┘
                              ↑
                              │ User clicks [View] on Nigeria
                              ▼
┌─────────────────────────────────────────────────────────────────────────────┐
│                    COUNTRY DETAIL VIEW - NIGERIA                          │
├─────────────────────────────────────────────────────────────────────────────┤
│  ┌─────────────────────────────────────────────────────────────────────┐   │
│  │  🇳🇬 NIGERIA FINANCIAL DASHBOARD                                     │   │
│  │  ───────────────────────────────────────────────────────────────   │   │
│  │  [← Back to Global Overview]                           [Export Report]│   │
│  └─────────────────────────────────────────────────────────────────────┘   │
│                                                                           │
│  ┌─────────────────────────────────────────────────────────────────────┐   │
│  │  FINANCIAL HEALTH SCORE                            [📊 View Details]  │   │
│  │  ┌─────────────────────────┐ ┌─────────────────────────────────┐   │   │
│  │  │         94              │ │    ▲ 3 points from last month     │   │   │
│  │  │        ●●●●●●●●●         │ │    All financial metrics on track     │   │   │
│  │  │     Financial Health       │ │    2 alerts requiring attention     │   │   │
│  │  │                         │ │                                 │   │   │
│  │  └─────────────────────────┘ └─────────────────────────────────┘   │   │
│  └─────────────────────────────────────────────────────────────────────┘   │
│                                                                           │
│  ┌─────────────────────────────────────────────────────────────────────┐   │
│  │  KEY FINANCIAL METRICS                                                 │   │
│  │  ┌────────┐ ┌────────┐ ┌────────┐ ┌────────┐ ┌────────┐ ┌────────┐  │   │
│  │  │ Revenue│ │Expense │ │Net Inc.│  AR    │  AP    │Budget  │  │   │
│  │  │ $8.2M  │ │ $6.1M  │ │ $2.1M  │ $1.8M  │ $1.2M  │ 96%    │  │   │
│  │  │  ▲ 12% │ │  ▲ 5%  │ │  ▲ 18% │ │ ▼ 8%  │ │ ● OK  │ │ ● OK  │  │   │
│  │  └────────┘ └────────┘ └────────┘ └────────┘ └────────┘ └────────┘  │   │
│  └─────────────────────────────────────────────────────────────────────┘   │
│                                                                           │
│  ┌───────────────────────────────────────┬─────────────────────────────────┐│
│  │  REVENUE BREAKDOWN                     │  AI INSIGHTS                   ││
│  │  ┌─────────────────────────────────┐ │  ┌───────────────────────────┐││
│  │  │ E-commerce: $4.5M (55%)          │ │  │ 📊 Revenue Forecast       │││
│  │  │ Logistics: $2.8M (34%)           │ │  │  +15% growth expected     │││
│  │  │ Services: $900K (11%)           │ │  │  (95% confidence)         │││
│  │  └─────────────────────────────────┘ │  │                          │││
│  │  [View Details]                      │  │ ⚠️ AR Aging Alert        │││
│  │                                      │  │  5 invoices >90 days      │││
│  │                                      │  │  [View]                  │││
│  │                                      │  └───────────────────────────┘││
│  └───────────────────────────────────────┴─────────────────────────────────┘│
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## 5. FEATURE-SPECIFIC FLOWS

### 5.1 Budget Approval Flow

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                 BUDGETS & FORECASTS - PENDING APPROVALS                    │
│  ┌─────────────────────────────────────────────────────────────────────┐   │
│  │  PENDING BUDGET APPROVALS (3)                                     │   │
│  │  ─────────────────────────────────                                 │   │
│  │  • Nigeria Q2 Budget: $2.1M                 [Review]              │   │
│  │  • Kenya Marketing Expansion: $500K         [Review]              │   │
│  │  • SA Operations Increase: $750K           [Review]              │   │
│  └─────────────────────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────────────────────┘
                              ↑
                              │ User clicks [Review]
                              ▼
┌─────────────────────────────────────────────────────────────────────────────┐
│                    BUDGET APPROVAL MODAL                                │
├─────────────────────────────────────────────────────────────────────────────┤
│  ┌─────────────────────────────────────────────────────────────────────┐   │
│  │  💰 BUDGET APPROVAL REQUEST                                      │   │
│  │                                                               [×] │   │
│  │  ─────────────────────────────────────────────────────────────   │   │
│  │  Request ID: BUD-2026-0217-001                                    │   │
│  │  Type: Budget Approval  |  Priority: HIGH  |  Due: Feb 20, 2026    │   │
│  │                                                                       │   │
│  │  Requested By: Adebayo Okafor (Nigeria Finance Manager)            │   │
│  │  Department: Nigeria Operations                                    │   │
│  │                                                                       │   │
│  │  Budget Request Details:                                           │   │
│  │  ┌─────────────────────────────────────────────────────────────┐   │   │
│  │  │  Quarter: Q2 2026                                              │   │   │
│  │  │  Total Amount: $2,100,000                                     │   │   │
│  │  │  Currency: NGN                                                 │   │   │
│  │  │                                                              │   │   │
│  │  │  Budget Breakdown:                                           │   │   │
│  │  │  ┌──────────────────┬──────────────────┐                    │   │   │
│  │  │  │ Personnel        │ $1,200,000       │                    │   │   │
│  │  │  │ ████████░░       │                  │                    │   │   │
│  │  │  ├──────────────────┼──────────────────┤                    │   │   │
│  │  │  │ Operations       │ $600,000         │                    │   │   │
│  │  │  │ ██████░░░░       │                  │                    │   │   │
│  │  │  ├──────────────────┼──────────────────┤                    │   │   │
│  │  │  │ Marketing        │ $200,000         │                    │   │   │
│  │  │  │ ███░░░░░░░       │                  │                    │   │   │
│  │  │  ├──────────────────┼──────────────────┤                    │   │   │
│  │  │  │ Capital          │ $100,000         │                    │   │   │
│  │  │  │ ██░░░░░░░░       │                  │                    │   │   │
│  │  │  └──────────────────┴──────────────────┘                    │   │   │
│  │  └─────────────────────────────────────────────────────────────┘   │   │
│  │                                                                       │   │
│  │  Justification:                                                      │   │
│  │  "Q2 budget increase to support expansion into Lagos market.       │   │
│  │   Additional sales team and marketing investment required."         │   │
│  │                                                                       │   │
│  │  AI Budget Analysis:                                                 │   │
│  │  ⚠️ Risk: 12% over recommended based on historical patterns         │   │
│  │  ✅ ROI Potential: +$450,000 additional revenue expected             │   │
│  │                                                                       │   │
│  │  Add your comments (optional):                                       │   │
│  │  ┌─────────────────────────────────────────────────────────────┐   │   │
│  │  │                                                             │   │   │
│  │  └─────────────────────────────────────────────────────────────┘   │   │
│  │                                                                       │   │
│  │                    [Reject] [Request Changes] [Approve]              │   │
│  └─────────────────────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────────────────────┘
```

### 5.2 AI-Powered Forecast Flow

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                   AI-POWERED FORECASTING                                  │
│  ┌─────────────────────────────────────────────────────────────────────┐   │
│  │  📊 AI REVENUE FORECAST                             [Configure ▼]   │   │
│  │  ───────────────────────────────────────────────────────────────   │   │
│  │  ┌───────────────────────────────────────────────────────────────┐ │   │
│  │  │  $48.2M Projected Revenue for March 2026                      │ │   │
│  │  │  ████████████████░░ 95% Confidence                            │ │   │
│  │  │  ↑ +13.4% from February                                       │ │   │
│  │  │                                                               │ │   │
│  │  │  ┌─────────────────────────────────────────────────────────┐ │ │   │
│  │  │  │ $50M│ ┌─                                                  │ │ │   │
│  │  │  │ $45M│   ┌─                                                │ │ │   │
│  │  │  │ $40M│     ───────── Actual vs Forecast                    │ │ │   │
│  │  │  │     └────────────────────────────────                     │ │ │   │
│  │  │  │        Jan     Feb     Mar     Apr     May               │ │ │   │
│  │  │  └─────────────────────────────────────────────────────────┘ │ │   │
│  │  │                                                               │ │   │
│  │  │  Key Drivers:                                                  │ │   │
│  │  │  • Nigeria expansion: +$800K                                 │   │
│  │  │  • Enterprise renewals: +$1.2M                               │   │   │
│  │  │  • Seasonal demand: +$500K                                   │   │   │
│  │  │                                                               │ │   │
│  │  │  Risks:                                                       │ │   │
│  │  │  • FX volatility (NGN): -$300K potential impact             │   │   │
│  │  │  • Competition in Kenya: -$200K                              │   │   │
│  │  │                                                               │ │   │
│  │  │  [View Detailed Analysis] [Set Alert] [Export]               │ │   │
│  │  └───────────────────────────────────────────────────────────────┘ │   │
│  └─────────────────────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────────────────────┘
                              ↑
                              │ User clicks [View Detailed Analysis]
                              ▼
┌─────────────────────────────────────────────────────────────────────────────┐
│                    AI FORECAST DETAIL VIEW                              │
├─────────────────────────────────────────────────────────────────────────────┤
│  ┌─────────────────────────────────────────────────────────────────────┐   │
│  │  📊 AI REVENUE FORECAST - DETAILED ANALYSIS                      │   │
│  │                                                               [×] │   │
│  │  ─────────────────────────────────────────────────────────────   │   │
│  │                                                                       │   │
│  │  Forecast Model: LSTM Neural Network                               │   │
│  │  Training Data: 24 months historical data                         │   │
│  │  Accuracy: 94.2% MAPE                                              │   │
│  │                                                                       │   │
│  │  ┌─────────────────────────────────────────────────────────────┐   │   │
│  │  │  SCENARIO ANALYSIS                                          │   │   │
│  │  │  ┌───────────────────────────────────────────────────────┐ │   │   │
│  │  │  │ Scenario           │ Forecast │ Confidence │ Impact │ │   │   │
│  │  │  ├───────────────────────────────────────────────────────┤ │   │   │
│  │  │  │ Baseline          │ $48.2M   │ 95%        │ —      │ │   │   │
│  │  │  │ Optimistic        │ $52.1M   │ 75%        │ +$3.9M │ │   │   │
│  │  │  │ Pessimistic        │ $44.8M   │ 82%        │ -$3.4M │ │   │   │
│  │  │  │ No Expansion      │ $45.5M   │ 91%        │ -$2.7M │ │   │   │
│  │  │  └───────────────────────────────────────────────────────┘ │   │   │
│  │  └─────────────────────────────────────────────────────────────┘   │   │
│  │                                                                       │   │
│  │  ┌─────────────────────────────────────────────────────────────┐   │   │
│  │  │  COUNTRY-BREAKDOWN                                           │   │   │
│  │  │  ┌───────────────────────────────────────────────────────┐ │   │   │
│  │  │  │ Country    │ Current │ Forecast │ Growth    │   │   │
│  │  │  ├───────────────────────────────────────────────────────┤ │   │   │
│  │  │  │ Nigeria    │ $8.2M   │ $9.5M    │ +16%      │ │   │   │
│  │  │  │ Kenya      │ $5.1M   │ $5.8M    │ +14%      │ │   │   │
│  │  │  │ S.Africa   │ $4.8M   │ $4.9M    │ +2%       │ │   │   │
│  │  │  │ Ghana      │ $3.2M   │ $3.8M    │ +19%      │ │   │   │
│  │  │  │ Others     │ $21.2M  │ $24.2M   │ +14%      │ │   │   │
│  │  │  └───────────────────────────────────────────────────────┘ │   │   │
│  │  └─────────────────────────────────────────────────────────────┘   │   │
│  │                                                                       │   │
│  │  [Run Custom Scenario] [Export Model] [Schedule Recurring] [Close]    │   │
│  └─────────────────────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────────────────────┘
```

### 5.3 Consolidated Report Generation Flow

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                   CONSOLIDATED REPORTS                                   │
│  ┌─────────────────────────────────────────────────────────────────────┐   │
│  │  [+ Create New Report]                                        │   │
│  └─────────────────────────────────────────────────────────────────────┘   │
│                                                                           │
│  ┌─────────────────────────────────────────────────────────────────────┐   │
│  │  SCHEDULED REPORTS (4 active)                                    │   │
│  │  ┌─────────────────────────────────────────────────────────────┐ │   │
│  │  │ 📊 Monthly Consolidated P&L                                   │ │   │
│  │  │    1st of month at 10:00 AM | Next: Mar 1, 2026             │ │   │
│  │  │    [Edit] [Disable] [Run Now]                               │ │   │
│  │  ├─────────────────────────────────────────────────────────────┤ │   │
│  │  │ 💰 Global Cash Flow Statement                               │ │   │
│  │  │    Weekly on Mondays at 9:00 AM | Next: Feb 24, 2026         │   │
│  │  │    [Edit] [Disable] [Run Now]                               │ │   │
│  │  └─────────────────────────────────────────────────────────────┘ │   │
│  └─────────────────────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────────────────────┘
                              ↑
                              │ User clicks [+ Create New Report]
                              ▼
┌─────────────────────────────────────────────────────────────────────────────┐
│                    REPORT BUILDER WIZARD                                 │
├─────────────────────────────────────────────────────────────────────────────┤
│  ┌─────────────────────────────────────────────────────────────────────┐   │
│  │  📊 CREATE CONSOLIDATED REPORT                                   │   │
│  │                                                               [×] │   │
│  │  ─────────────────────────────────────────────────────────────   │   │
│  │  Step 1 of 4: Select Report Type                                 │   │
│  │  ┌─────────────────────────────────────────────────────────────┐ │   │
│  │  │                                                             │ │   │
│  │  │  ┌─────────┐  ┌─────────┐  ┌─────────┐  ┌─────────┐   │ │   │
│  │  │  │         │  │         │  │         │  │         │   │ │   │
│  │  │  │  📊     │  │  💰     │  │  🏦     │  │  📋     │   │ │   │
│  │  │  │         │  │         │  │         │  │         │   │ │   │
│  │  │  │P&L      │  │Cash Flow│  │Balance  │  │Custom   │   │ │   │
│  │  │  │Report   │  │Statement│  │Sheet    │  │Report   │   │ │   │
│  │  │  └─────────┘  └─────────┘  └─────────┘  └─────────┘   │ │   │
│  │  │                                                             │ │   │
│  │  │  ┌─────────┐  ┌─────────┐  ┌─────────┐  ┌─────────┐   │ │   │
│  │  │  │  📈     │  │  🌍     │  │  🔍     │  │  📄     │   │ │   │
│  │  │  │Budget   │  │Country  │  │Audit    │  │Tax      │   │ │   │
│  │  │  │Variance │  │Comparison│  │Report   │  │Report   │   │ │   │
│  │  │  └─────────┘  └─────────┘  └─────────┘  └─────────┘   │ │   │
│  │  │                                                             │ │   │
│  │  └─────────────────────────────────────────────────────────────┘ │   │
│  │                                                                   │   │
│  │  [Cancel]                                          [Next: Configure]│   │
│  └─────────────────────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────────────────────┘
                              │
                              │ Select P&L Report → Click Next
                              ▼
┌─────────────────────────────────────────────────────────────────────────────┐
│                    REPORT BUILDER - CONFIGURE                            │
├─────────────────────────────────────────────────────────────────────────────┤
│  Step 2 of 4: Configure Report                                          │
│                                                                           │
│  Report Name: [Consolidated P&L February 2026                    ]       │
│                                                                           │
│  Time Range: [January 1, 2026 - February 29, 2026 ▼]                  │
│                                                                           │
│  Include Countries:                                                     │
│  ☑ Nigeria  ☑ Kenya  ☑ South Africa  ☑ Ghana  ☑ Ireland               │
│  ☑ All Countries                                                       │
│                                                                           │
│  Include Sections:                                                      │
│  ☑ Revenue by Country                                                  │
│  ☑ Cost of Goods Sold                                                 │
│  ☑ Operating Expenses                                                 │
│  ☑ Net Income by Country                                              │
│  ☑ Currency Translation (Base: USD)                                  │
│  ☐ Executive Commentary                                               │
│  ☐ AI Insights                                                       │
│                                                                           │
│  [Cancel] [Back]                                              [Next: Format]│
└─────────────────────────────────────────────────────────────────────────────┘
                              │
                              │ Click Next
                              ▼
┌─────────────────────────────────────────────────────────────────────────────┐
│                    REPORT BUILDER - FORMAT                                │
├─────────────────────────────────────────────────────────────────────────────┤
│  Step 3 of 4: Choose Format                                             │
│                                                                           │
│  Output Format:                                                         │
│  ┌─────────┐  ┌─────────┐  ┌─────────┐  ┌─────────┐                      │
│  │    📄   │  │   📊   │  │  📧    │  │   🔗   │                      │
│  │         │  │         │  │         │  │         │                      │
│  │   PDF   │  │  Excel  │  │  Email  │   │   Link  │                      │
│  └─────────┘  └─────────┘  └─────────┘  └─────────┘                      │
│                                                                           │
│  Include:                                                               │
│  ☑ Charts and visualizations                                          │
│  ☑ Country breakdown tables                                          │
│  ☐ AI insights and forecasts                                           │
│  ☑ Executive summary                                                  │
│  ☑ Company branding                                                   │
│  ☑ Comparison with previous period                                    │
│                                                                           │
│  [Cancel] [Back]                                              [Generate]│
└─────────────────────────────────────────────────────────────────────────────┘
                              │
                              │ Click Generate
                              ▼
┌─────────────────────────────────────────────────────────────────────────────┐
│                    REPORT GENERATED                                       │
│                                                                           │
│  ✅ Report Generated Successfully                                        │
│                                                                           │
│  Consolidated P&L February 2026                                        │
│  Generated: Feb 17, 2026 | 18 pages | PDF                              │
│                                                                           │
│  ┌─────────────────────────────────────────────────────────────┐        │
│  │  📄 Preview: [First page preview]                          │        │
│  └─────────────────────────────────────────────────────────────┘        │
│                                                                           │
│  [📥 Download PDF] [📧 Email Report] [🔗 Copy Link]                       │
│  [📅 Schedule Recurring] [✨ Save as Template] [Generate Another]         │
│                                                                           │
└─────────────────────────────────────────────────────────────────────────────┘
```

### 5.4 Treasury Management Flow

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                    GLOBAL TREASURY                                        │
│  ┌─────────────────────────────────────────────────────────────────────┐   │
│  │  🏦 GLOBAL CASH POSITION                                         │   │
│  │  ───────────────────────────────────────────────────────────────   │   │
│  │  ┌───────────────────────────────────────────────────────────────┐ │   │
│  │  │  Total Cash Position: $15,200,000                             │ │   │
│  │  │  ████████████████████████████░░░░ 85% of Target            │ │   │
│  │  │                                                               │ │   │
│  │  │  Operating Cash: $12,500,000    • Investment: $2,700,000     │ │   │
│  │  │  ● 94% Utilized                • Available                 │ │   │
│  │  └───────────────────────────────────────────────────────────────┘ │   │
│  └─────────────────────────────────────────────────────────────────────┘   │
│                                                                           │
│  ┌───────────────────────────────────────────────┬─────────────────────────┐│
│  │  CASH POSITION BY COUNTRY                    │  FX EXPOSURE            ││
│  │  ┌────────────────────────────────────────┐  │  ┌───────────────────┐ ││
│  │  │ Nigeria    $4,200,000 ● 95%          │  │  │ NGN Exposure      │ ││
│  │  │ Kenya      $2,800,000 ● 88%          │  │  │ -$3.2M           │ ││
│  │  │ S.Africa   $2,100,000 ● 92%          │  │  │ KES Exposure      │ ││
│  │  │ Ghana      $1,900,000 ● 91%          │  │  │ -$1.8M           │ ││
│  │  │ Others     $4,200,000 ● 82%          │  │  │ [View All →]     │ ││
│  │  └────────────────────────────────────────┘  │  └───────────────────┘ ││
│  └───────────────────────────────────────────────┴─────────────────────────┘│
│                                                                           │
│  ┌─────────────────────────────────────────────────────────────────────┐   │
│  │  📊 AI CASH FLOW FORECAST                                         │   │
│  │  ┌───────────────────────────────────────────────────────────────┐ │   │
│  │  │  Next 30 Days Forecast                                        │ │   │
│  │  │  ┌─────────────────────────────────────────────────────────┐ │ │   │
│  │  │  │ $20M│ ┌─                                                    │ │ │   │
│  │  │  │ $15M│   ┌─                                                  │ │ │   │
│  │  │  │ $10M│     ───── ──────────────────                        │ │ │   │
│  │  │  │     └────────────────────────────────                     │ │ │   │
│  │  │  │        Week 1    Week 2    Week 3    Week 4              │ │ │   │
│  │  │  └─────────────────────────────────────────────────────────┘ │ │   │
│  │  │                                                               │ │   │
│  │  │  ⚠️ Cash Low Point: Week 3 - $11.2M                          │ │   │
│  │  │  💡 Recommendation: Transfer $2M from investments              │ │   │
│  │  └───────────────────────────────────────────────────────────────┘ │   │
│  └─────────────────────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────────────────────┘
```

### 5.5 Fraud Alert Flow

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                    AI FRAUD DETECTION ALERT                               │
│  ┌─────────────────────────────────────────────────────────────────────┐   │
│  │  ⚠️ FRAUD ALERT                                                   │   │
│  │  ───────────────────────────────────────────────────────────────   │   │
│  │  Anomaly detected in Kenya expense transactions                      │   │
│  │  Confidence: 87% | Risk Level: HIGH                                │   │
│  │  [Investigate] [Dismiss] [Assign]                                 │   │
│  └─────────────────────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────────────────────┘
                              ↑
                              │ User clicks [Investigate]
                              ▼
┌─────────────────────────────────────────────────────────────────────────────┐
│                    FRAUD DETAIL MODAL                                   │
├─────────────────────────────────────────────────────────────────────────────┤
│  ┌─────────────────────────────────────────────────────────────────────┐   │
│  │  ⚠️ FRAUD ANALYSIS DETAIL                                        │   │
│  │                                                               [×] │   │
│  │  ─────────────────────────────────────────────────────────────   │   │
│  │  Alert ID: FRAUD-2026-0217-001                                   │   │
│  │  Country: Kenya                                                   │   │
│  │  Detected: Feb 17, 2026 09:23 UTC                                │   │
│  │                                                                       │   │
│  │  Anomaly Details:                                                    │   │
│  │  ┌─────────────────────────────────────────────────────────────┐   │   │
│  │  │  Type: Unusual Expense Pattern                                │   │   │
│  │  │  • 47 duplicate transactions detected                    │   │   │
│  │  │  • Same vendor: "Logistics Partner Ltd"                   │   │   │
│  │  │  • Amount: $45,000 each (total: $2,115,000)            │   │   │
│  │  │  • Time window: 3 days                                     │   │   │
│  │  │  • Unusual: High frequency for single vendor                │   │   │
│  │  └─────────────────────────────────────────────────────────────┘   │   │
│  │                                                                       │   │
│  │  AI Analysis:                                                        │   │
│  │  ┌─────────────────────────────────────────────────────────────┐   │   │
│  │  │  Risk Score: 87/100                                          │   │   │
│  │  │  ████████████████████████████████░░░░░                     │   │   │
│  │  │                                                               │   │   │
│  │  │  Risk Factors:                                                  │   │   │
│  │  │  • High transaction frequency: +35 risk points             │   │   │
│  │  │  • Same vendor pattern: +22 risk points                    │   │   │
│  │  │  • Large amounts: +15 risk points                           │   │   │
│  │  │  • Off-cycle timing: +15 risk points                        │   │   │
│  │  │                                                               │   │   │
│  │  │  Recommended Action:                                          │   │   │
│  │  │  Flag for review by Kenya Finance Manager                   │   │   │
│  │  └─────────────────────────────────────────────────────────────┘   │   │
│  │                                                                       │   │
│  │  Related Transactions:                                              │   │
│  │  ┌─────────────────────────────────────────────────────────────┐   │   │
│  │  │  TX ID        │ Date       │ Amount    │ Vendor         │   │   │
│  │  ├─────────────────────────────────────────────────────────────┤   │   │
│  │  │  TX-001234   │ Feb 14     │ $45,000   │ Logistics...   │   │   │
│  │  │  TX-001235   │ Feb 15     │ $45,000   │ Logistics...   │   │   │
│  │  │  TX-001236   │ Feb 15     │ $45,000   │ Logistics...   │   │   │
│  │  │  ... 44 more │ ...        │ ...       │ ...           │   │   │
│  │  └─────────────────────────────────────────────────────────────┘   │   │
│  │                                                                       │   │
│  │  [View All Transactions] [Assign to Kenya Team] [Mark as Resolved]     │   │
│  └─────────────────────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## 6. EXIT FLOWS

### 6.1 Session Timeout Flow

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                    SESSION TIMEOUT WARNING                                 │
│  (Auto-triggered after 25 minutes of inactivity)                          │
│  ┌─────────────────────────────────────────────────────────────────────┐   │
│  │  ⏰ Session Expiring Soon                                         │   │
│  │                                                               [×] │   │
│  │  ───────────────────────────────────────────────────────────────   │   │
│  │  Your session will expire in 5 minutes.                            │   │
│  │  Unsaved changes may be lost.                                       │   │
│  │  [Stay Signed In] [Sign Out Now]                                  │   │
│  └─────────────────────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────────────────────┘
         │                                    │
         │ No action (5 min)                 │ Click "Stay Signed In"
         ▼                                    ▼
┌──────────────────────────┐      ┌────────────────────────────┐
│   SESSION EXPIRED        │      │   SESSION EXTENDED         │
│  ┌────────────────────┐  │      │  ┌────────────────────┐    │
│  │  Your session has  │  │      │  │ Session extended.  │    │
│  │  expired.          │  │      │  │ You can continue.  │    │
│  │                    │  │      │  └────────────────────┘    │
│  │  [Sign In Again]   │  │      └────────────────────────────┘
│  └────────────────────┘  │
└──────────────────────────┘
```

### 6.2 Browser Close Flow

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                    USER CLOSES BROWSER TAB                                │
│  (Detected by beforeunload event)                                         │
└─────┬───────────────────────────────────────────────────────────────────────┘
      │
      ▼
┌─────────────────────────────────────────────────────────────────────────────┐
│                      UNSAVED CHANGES CHECK                               │
│  ┌─────────────────────────────────────────────────────────────────────┐   │
│  │  Are there unsaved changes?                                       │   │
│  └─────┬───────────────────────────────────────────────────────────┬─────┘
        │ YES                                                            │ NO
        ▼                                                               ▼
┌───────────────────────┘                           ┌───────────────────────────┐
│  SHOW WARNING:        │                           │  CLOSE GRACEFULLY:       │
│  "You have unsaved    │                           │  • Close WebSocket       │
│   changes. Stay on    │                           │  • Clear session flags    │
│   page?"              │                           │  • Allow browser close    │
│  [Leave] [Stay]       │                           └───────────────────────────┘
└───────────────────────┘
```

---

## 7. NAVIGATION STATE DIAGRAM

```
                    ┌─────────────┐
                    │   Login     │
                    └──────┬──────┘
                           │
               ┌──────────┴──────────┐
               │                      │
          Authenticated         Not Authenticated
               │                      ▼
               ▼              ┌──────────────┐
    ┌───────────────────┐    │  Login Page  │
    │  Determine Scope  │    └──────────────┘
    └─────────┬─────────┘
              │
      ┌─────────┴────────┐
      │                  │
  Global Scope      Regional Scope
      │                  │
      ▼                  ▼
┌─────────────┐   ┌─────────────┐
│   Global     │   │   Regional  │
│  Dashboard   │   │  Dashboard  │
└──────┬──────┘   └──────┬──────┘
       │                 │
       └────────┬────────┘
                ▼
    ┌─────────────────────────────┐
    │     Finance Dashboard       │
    │  (Global/Regional View)    │
    └──────┬──────────────────────┘
           │
           ├──────────┬──────────┬──────────┬──────────┐
           ▼          ▼          ▼          ▼          ▼
     ┌─────────┐ ┌─────────┐ ┌─────────┐ ┌─────────┐ ┌─────────┐
     │Overview │ │Countries│ │ Revenue │ │Expenses │ │Budgets  │
     └────┬────┘ └────┬────┘ └────┬────┘ └────┬────┘ └────┬────┘
          │           │           │           │           │
          └───────────┴───────────┴───────────┴───────────┘
                              │
                              ▼
                    ┌─────────────────┐
                    │   Any Page       │
                    └────────┬────────┘
                             │
               ┌──────────────┼──────────────┐
               ▼              ▼              ▼
        ┌─────────┐   ┌─────────┐   ┌─────────┐
        │  Back   │   │Navigate │   │ Logout │
        │         │   │ to other│   │         │
          └────┬────┘   └────┬────┘   └────┬────┘
               │              │              │
               └──────────────┴──────────────┘
                              │
                              ▼
                        ┌─────────┐
                        │ Login   │
                        └─────────┘
```

---

**End of UI Flow Documentation**

**Version History:**
| Version | Date | Changes |
|---------|------|---------|
| 1.0 | 2025-02-08 | Initial skeleton documentation |
| 2.0 | 2026-02-17 | Complete UI Flow Documentation following Executive Dashboard standard |

Next: [02_Wireframes_Documentation.md](#) - Visual wireframes for each screen
