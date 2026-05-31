# HQ SALES DASHBOARD - UI FLOW DOCUMENTATION

**Version:** 1.0
**Domain:** Management Domain
**Subdomain:** Sales-Departments (HQ)
**Frontend:** sales-web-dashboard
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

The HQ Sales Dashboard provides global sales oversight, aggregating data from all country sales dashboards across the organization. HQ sales leadership can monitor performance, compare regions, set global targets, and make strategic decisions based on comprehensive multi-country analytics.

### 1.2 Scope

**Managed at HQ Level:**
- Multi-country sales performance aggregation
- Global sales strategy and target setting
- Cross-country comparison and benchmarking
- Global sales team management (Country Directors)
- Worldwide customer insights and segmentation
- International territory and quota planning
- Global forecasting and trend analysis

**Data Sources:**
- All Country Sales Dashboards (Business Domain)
- Read-only aggregation of country-level data
- Real-time synchronization via event streaming

### 1.3 User Roles & Entry Points

┌─────────────────────────────────────────────────────────────────────┐
│                       HQ SALES DASHBOARD USERS                      │
├─────────────────────────────────────────────────────────────────────┤
│                                                                      │
│   ┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐ │
│   │ VP of Sales     │    │ Global Sales    │    │ HQ Sales        │ │
│   │ (Executive)     │    │ Director       │    │ Analyst         │ │
│   │                 │    │                 │    │                 │ │
│   │ All Countries   │    │ All Countries   │    │ All Countries   │ │
│   │ Full Access     │    │ Full Access     │    │ Read-Only       │ │
│   │                 │    │                 │    │                 │ │
│   └─────────────────┘    └─────────────────┘    └─────────────────┘ │
│                                                                      │
│   ┌─────────────────┐    ┌─────────────────┐                         │
│   │ Regional        │    │ Sales Ops       │                         │
│   │ Manager         │    │ Manager         │                         │
│   │                 │    │                 │                         │
│   │ Assigned        │    │ All Countries   │                         │
│   │ Regions Only    │    │ Ops Focus       │                         │
│   │                 │    │                 │                         │
│   └─────────────────┘    └─────────────────┘                         │
│                                                                      │
└─────────────────────────────────────────────────────────────────────┘

### 1.4 Role Permissions Matrix

| Module | VP Sales | Global Director | Regional Manager | Analyst | Ops Manager |
|--------|----------|-----------------|------------------|---------|-------------|
| Global Overview | Full | Full | Regions Only | Read | Read |
| Country Comparison | Full | Full | Regions Only | Read | Read |
| Sales Teams | Full | Full | Regions Only | Read | Full |
| Sales Team Partners | Full | Full | Regions Only | Read | All |
| Deals Pipeline | Full | Full | Regions Only | Read | All |
| Customers | Full | Full | Regions Only | Read | All |
| Performance | Full | Full | Regions Only | Read | All |
| Territories | Full | Full | Regions Only | Read | Full |
| Forecasting | Full | Full | Regions Only | Read | Full |
| Reports | Full | Full | Regions Only | Read | All |
| Settings | Full | Full | None | None | Full |

---

## 2. USER ROLES & PERMISSIONS

### 2.1 VP of Sales (Executive)

**Description:** Highest sales authority with global oversight

**Access Level:** Full access to all HQ Sales Dashboard features

**Key Capabilities:**
- View and analyze sales data across all countries
- Set global sales targets and strategies
- Approve major deals and discounts globally
- Manage Country Sales Directors
- Access all reports and analytics
- Configure global sales territories
- Set commission structures at global level
- Strategic decision-making authority

**Limitations:**
- Cannot modify country-level operational settings
- Cannot access other domains (Finance, HR, etc.)

### 2.2 Global Sales Director

**Description:** Manages sales operations across all countries

**Access Level:** Full access to HQ Sales Dashboard

**Key Capabilities:**
- Monitor all country sales performance
- Coordinate cross-country initiatives
- Manage global sales team (Country Directors)
- Set and adjust global quotas
- Approve large deals across countries
- Generate global sales reports
- Coordinate territory planning
- Manage global forecasting

**Limitations:**
- Cannot change executive-level strategies
- Cannot access non-sales domains

### 2.3 Regional Sales Manager

**Description:** Manages sales operations for assigned regions only

**Access Level:** Region-specific access

**Key Capabilities:**
- View performance for assigned countries/regions
- Monitor regional sales teams
- Approve regional deals
- Generate regional reports
- Coordinate regional quotas

**Limitations:**
- Can ONLY view data for assigned regions
- Cannot access other regions' data
- Cannot set global quotas

### 2.4 HQ Sales Analyst

**Description:** Analyzes global sales data and generates insights

**Access Level:** Read-only access for analytics

**Key Capabilities:**
- View all global sales data (read-only)
- Generate custom reports
- Create dashboards and visualizations
- Export data for analysis
- Access historical data across all countries

**Limitations:**
- Cannot modify any data
- Cannot approve or reject transactions
- Cannot assign quotas or territories

### 2.5 Sales Operations Manager

**Description:** Manages global sales operations and processes

**Access Level:** Operations focus with global visibility

**Key Capabilities:**
- Configure global sales processes
- Administer global territory assignments
- Set up commission structures
- Manage sales tools integrations
- Generate operational reports
- Monitor all pipelines globally

**Limitations:**
- Cannot modify team assignments (HR function)
- Cannot approve deals (sales manager role)

---

## 3. NAVIGATION ARCHITECTURE

### 3.1 Primary Navigation Structure

```
┌─────────────────────────────────────────────────────────────────────────┐
│                         HQ SALES DASHBOARD                              │
├─────────────────────────────────────────────────────────────────────────┤
│  🏠 Global  🌍 Countries  👥 Teams  🤝 Partners  💼 Pipeline  🤝 Customers │
│  📈 Performance  🎯 Territories  📡 Forecasting  📊 Reports  ⚙️ Settings│
└─────────────────────────────────────────────────────────────────────────┘
```

### 3.2 Navigation Hierarchy

```
HQ Sales Dashboard
│
├── 🏠 Global Overview
│   ├── Worldwide Summary
│   ├── Country Performance
│   ├── Global Metrics
│   └── Executive Alerts
│
├── 🌍 Countries
│   ├── Country Comparison
│   ├── Country Detail
│   │   ├── Overview
│   │   ├── Sales Team
│   │   ├── Sales Team Partners
│   │   ├── Pipeline
│   │   ├── Performance
│   │   └── Reports
│   └── Country Rankings
│
├── 👥 Global Sales Teams
│   ├── Country Directors
│   ├── Regional Managers
│   ├── Performance Leaderboard
│   └── Commission Tracking
│
├── 🤝 Sales Team Partners
│   ├── Global Partners Overview
│   ├── Partner Applications
│   │   ├── Pending Approvals
│   │   ├── Approved Partners
│   │   └── Rejected Applications
│   ├── Partner Performance
│   │   ├── By Partner Type
│   │   ├── By Country
│   │   └── Leaderboard
│   ├── Commission Management
│   │   ├── Partner Referral Bonuses
│   │   ├── Customer Sales Commissions
│   │   ├── Tier Management
│   │   └── Payment Processing
│   ├── Territory Allocation
│   │   ├── Lead Distribution
│   │   ├── Territory Assignments
│   │   └── AI Lead Management
│   └── Partner Analytics
│       ├── Acquisition Metrics
│       ├── Activity Monitoring
│       └── Revenue Contribution
│
├── 💼 Global Pipeline
│   ├── Worldwide Pipeline View
│   ├── Major Deals Tracker
│   ├── Deal Approval Queue
│   └── Pipeline Analytics
│
├── 🤝 Global Customers
│   ├── Multi-National Accounts
│   ├── Customer Segmentation
│   ├── Global Account View
│   └── Customer Analytics
│
├── 📈 Global Performance
│   ├── Revenue Analytics
│   ├── Growth Metrics
│   ├── Market Share Analysis
│   ├── Product/Service Performance
│   └── Custom Reports
│
├── 🎯 Global Territories
│   ├── Territory Management
│   ├── Quota Administration
│   ├── Territory Performance
│   └── Boundary Planning
│
├── 📡 Global Forecasting
│   ├── Worldwide Revenue Forecast
│   ├── Regional Forecasts
│   ├── Trend Analysis
│   └── Predictive Analytics
│
├── 📊 Reports
│   ├── Executive Dashboards
│   ├── Country Reports
│   ├── Product Reports
│   ├── Sales Partners Reports
│   ├── Custom Report Builder
│   └── Scheduled Reports
│
└── ⚙️ Settings
    ├── Global Configuration
    ├── User Management
    ├── Role & Permissions
    ├── Commission Structure Configuration
    └── System Settings
```

### 3.3 Breadcrumb Navigation

```
Home > {Module} > {Country} > {Sub-module} > {Detail View}

Examples:
- Home > Global Overview
- Home > Countries > Nigeria > Overview
- Home > Countries > Nigeria > Sales Team
- Home > Global Pipeline > Major Deals
- Home > Reports > Executive Dashboard
```

---

## 4. UI FLOW DIAGRAMS

### 4.1 Authentication Flow

```
┌──────────────┐
│              │
│  User Visits │
│  HQ Sales    │
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
│  • If valid → Verify HQ Sales Dashboard access              │
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
│  • Retrieve user roles and permissions                     │
│  • Verify HQ Sales Dashboard access                        │
│  • Determine regional access (if applicable)               │
│  • Generate session token                                  │
└──────┬──────────────────────────────────────────────────────┘
       │
       ▼
┌─────────────────────────────────────────────────────────────┐
│                      Authenticated                          │
├─────────────────────────────────────────────────────────────┤
│  • Load user permissions for all countries (or assigned)   │
│  • Initialize WebSocket connection for real-time updates   │
│  • Load global sales data                                  │
│  • Redirect to Global Overview                             │
└──────┬──────────────────────────────────────────────────────┘
       │
       ▼
┌─────────────────────────────────────────────────────────────┐
│                    GLOBAL OVERVIEW                          │
│  • Display worldwide sales metrics                         │
│  • Show country-by-country breakdown                       │
│  • Load real-time data via WebSocket                       │
│  • Display user's permitted modules based on role          │
└─────────────────────────────────────────────────────────────┘
```

### 4.2 Global Overview Flow

```
┌───────────────────────────────────────────────────────────────────┐
│                    HQ GLOBAL OVERVIEW                             │
├───────────────────────────────────────────────────────────────────┤
│                                                                    │
│  ┌─────────────────────────────────────────────────────────────┐  │
│  │              GLOBAL KEY METRICS CARDS                        │  │
│  │  ┌───────────┐ ┌───────────┐ ┌───────────┐ ┌─────────┐    │  │
│  │  │ Global    │ │ Worldwide │ │ Deals     │ │ Active │    │  │
│  │  │ Revenue   │ │ Pipeline  │ │ Closed    │ │ Deals  │    │  │
│  │  │           │ │ Value     │ │ This Month│ │        │    │  │
│  │  │   $8.5M   │ │   $25M    │ │   1,245   │ │ 4,892  │    │  │
│  │  │   ┌──────┐│ │   ┌──────┐│ │   ┌──────┐│ │ ┌──────┐│    │  │
│  │  │   │ +15% ││ │   │ +12% ││ │   │ +18% ││ │ │ +8%  ││    │  │
│  │  │   └──────┘│ │   └──────┘│ │   └──────┘│ │ └──────┘│    │  │
│  │  └───────────┘ └───────────┘ └───────────┘ └─────────┘    │  │
│  └─────────────────────────────────────────────────────────────┘  │
│                                                                    │
│  ┌─────────────────────────────────────────────────────────────┐  │
│  │              COUNTRY PERFORMANCE GRID                       │  │
│  │  ┌─────────────┐ ┌─────────────┐ ┌─────────────┐             │  │
│  │  │ 🇳🇬 Nigeria  │ │ 🇰🇪 Kenya    │ │ 🇬🇭 Ghana    │             │  │
│  │  │ Revenue:    │ │ Revenue:    │ │ Revenue:    │             │  │
│  │  │ $1.2M       │ │ $950K       │ │ $780K       │             │  │
│  │  │ Quota:      │ │ Quota:      │ │ Quota:      │             │  │
│  │  │ 95% ████████│ │ 102% ██████ │ │ 88% █████  │             │  │
│  │  │ [+View]     │ │ [+View]     │ │ [+View]     │             │  │
│  │  └─────────────┘ └─────────────┘ └─────────────┘             │  │
│  │  ┌─────────────┐ ┌─────────────┐ ┌─────────────┐             │  │
│  │  │ 🇿🇦 S. Africa│ │ 🇪🇹 Ethiopia  │ │ 🇺🇬 Uganda   │             │  │
│  │  │ Revenue:    │ │ Revenue:    │ │ Revenue:    │             │  │
│  │  │ $2.1M       │ │ $650K       │ │ $420K       │             │  │
│  │  │ Quota:      │ │ Quota:      │ │ Quota:      │             │  │
│  │  │ 105% ██████ │ │ 92% █████   │ │ 95% █████  │             │  │
│  │  │ [+View]     │ │ [+View]     │ │ [+View]     │             │  │
│  │  └─────────────┘ └─────────────┘ └─────────────┘             │  │
│  │                         [+ View All Countries]              │  │
│  └─────────────────────────────────────────────────────────────┘  │
│                                                                    │
│  ┌───────────────────────────────────────┐ ┌─────────────────────┐│
│  │         GLOBAL REVENUE CHART           │ │   TOP PERFORMING    ││
│  │  Revenue by Country (Bar Chart)        │ │   COUNTRIES         ││
│  │  ┌─────────────────────────────────┐  │ └─────────────────────┘│
│  │  │ Nigeria    ████████████ $1.2M    │  │                         │
│  │  │ S. Africa  ████████████████ $2.1M │  │ ┌─────────────────────┐│
│  │  │ Kenya      ██████████ $950K      │  │ │   MAJOR DEALS       ││
│  │  │ Ghana      ████████ $780K       │  │ │   Tracker           ││
│  │  │ Ethiopia   ██████ $650K         │  │ └─────────────────────┘│
│  │  └─────────────────────────────────┘  │                         │
│  └───────────────────────────────────────┘                         │
│                                                                    │
└─────────────────────────────────────────────────────────────────────┘
```

### 4.3 Country Comparison Flow

```
┌───────────────────────────────────────────────────────────────────┐
│                    COUNTRY COMPARISON                             │
├───────────────────────────────────────────────────────────────────┤
│                                                                    │
│  ┌─────────────────────────────────────────────────────────────┐  │
│  │  COMPARISON METRICS                                           │  │
│  │  [Select Countries] 🇳🇬 🇰🇪 🇬🇭 🇿🇦                             │  │
│  │  Period: [This Month ▼]  │  Compare: [Previous Period ▼]       │  │
│  └─────────────────────────────────────────────────────────────┘  │
│                                                                    │
│  ┌─────────────────────────────────────────────────────────────┐  │
│  │              COMPARISON TABLE                                 │  │
│  │  ┌───────────────────────────────────────────────────────────┐ │  │
│  │  │ Metric          │ NG    │ KE    │ GH    │ ZA    │ Trend   │ │  │
│  │  ├───────────────────────────────────────────────────────────┤ │  │
│  │  │ Revenue         │ $1.2M │ $950K │ $780K │ $2.1M │ 📈      │ │  │
│  │  │ Quota Attainment│ 95%   │ 102%  │ 88%   │ 105%  │ 📈      │ │  │
│  │  │ Deals Closed    │ 245   │ 189   │ 156   │ 312   │ 📈      │ │  │
│  │  │ Pipeline Value  │ $3.5M │ $2.8M │ $2.1M │ $4.2M │ 📈      │ │  │
│  │  │ Win Rate        │ 32%   │ 35%   │ 28%   │ 38%   │ ➡️       │ │  │
│  │  │ Avg Deal Size   │ $4.9K │ $5.0K │ $5.0K │ $6.7K │ 📈      │ │  │
│  │  │ Team Size       │ 25    │ 18    │ 15    │ 32    │ ➡️       │ │  │
│  │  └───────────────────────────────────────────────────────────┘ │  │
│  └─────────────────────────────────────────────────────────────┘  │
│                                                                    │
│  ┌─────────────────────────────────────────────────────────────┐  │
│  │              PERFORMANCE COMPARISON CHARTS                    │  │
│  │  ┌─────────────────────────────┐ ┌─────────────────────────┐ │  │
│  │  │ Revenue by Country         │ │ Quota Attainment        │ │  │
│  │  │ (Grouped Bar Chart)        │ │ (Grouped Bar Chart)     │ │  │
│  │  └─────────────────────────────┘ └─────────────────────────┘ │  │
│  │  ┌─────────────────────────────┐ ┌─────────────────────────┐ │  │
│  │  │ Deals Closed Comparison    │ │ Win Rate Comparison     │ │  │
│  │  │ (Grouped Bar Chart)        │ │ (Grouped Bar Chart)     │ │  │
│  │  └─────────────────────────────┘ └─────────────────────────┘ │  │
│  └─────────────────────────────────────────────────────────────┘  │
│                                                                    │
│  ┌─────────────────────────────────────────────────────────────┐  │
│  │              RANKINGS                                        │  │
│  │  ┌───────────────────────────────────────────────────────────┐ │  │
│  │  │ By Revenue:  1. 🥇 S. Africa  2. 🥈 Nigeria  3. 🥉 Kenya   │ │  │
│  │  │ By Growth:   1. 🥇 Kenya      2. 🥈 Nigeria  3. 🥉 Ghana   │ │  │
│  │  │ By Quota:    1. 🥇 S. Africa  2. 🥈 Kenya    3. 🥉 Nigeria │ │  │
│  │  └───────────────────────────────────────────────────────────┘ │  │
│  └─────────────────────────────────────────────────────────────┘  │
│                                                                    │
└─────────────────────────────────────────────────────────────────────┘
```

### 4.4 Country Detail Flow

```
┌───────────────────────────────────────────────────────────────────┐
│                    COUNTRY DETAIL VIEW                           │
├───────────────────────────────────────────────────────────────────┤
│                                                                    │
│  ┌─────────────────────────────────────────────────────────────┐  │
│  │  [< Countries]  │  🇳🇬 Nigeria  │  Country Sales Dashboard      │  │
│  │  ACTIONS: [View Full Dashboard] [Export Report] [Contact]     │  │
│  └─────────────────────────────────────────────────────────────┘  │
│                                                                    │
│  ┌─────────────────────────────────────────────────────────────┐  │
│  │                       SUMMARY                                │  │
│  │  ┌─────────────────────────────────────┐ ┌─────────────────┐  │  │
│  │  │  COUNTRY METRICS                     │ │  SALES TEAM     │  │  │
│  │  │  • Revenue: $1.2M (95% of quota)     │ │  • Director:    │  │  │
│  │  │  • Pipeline: $3.5M                   │ │    John Doe     │  │  │
│  │  │  • Deals Closed: 245                 │ │  • Team Size: 25│  │  │
│  │  │  • Win Rate: 32%                     │ │  • Departments: │  │  │
│  │  │  • Growth: +12% vs last month         │ │    Enterprise   │  │  │
│  │  └─────────────────────────────────────┘ └─────────────────┘  │  │
│  └─────────────────────────────────────────────────────────────┘  │
│                                                                    │
│  ┌─────────────────────────────────────────────────────────────┐  │
│  │              PERFORMANCE TRENDS                               │  │
│  │  ┌─────────────────────────────────────────────────────────┐  │  │
│  │  │  Revenue Trend (Line Chart - 6 months)                   │  │  │
│  │  │  ┌─────────────────────────────────────────────────────┐│  │  │
│  │  │  │     ┌───┐                                              ││  │  │
│  │  │  │   ┌─┘   └─┐   Monthly Revenue vs Target                ││  │  │
│  │  │  │ ┌─┘       └─┐                                          ││  │  │
│  │  │  └─┘           └─                                        ││  │  │
│  │  │  Sep Oct Nov Dec Jan Feb                                  ││  │  │
│  │  └─────────────────────────────────────────────────────────────┘│  │  │
│  │  └─────────────────────────────────────────────────────────┘  │  │
│  └─────────────────────────────────────────────────────────────┘  │
│                                                                    │
│  ┌─────────────────────────────────────────────────────────────┐  │
│  │              TOP PERFORMERS IN COUNTRY                        │  │
│  │  ┌───────────────────────────────────────────────────────────┐ │  │
│  │  │ Rank │ Name          │ Department  │ Revenue │ Attainment│ │  │
│  │  ├───────────────────────────────────────────────────────────┤ │  │
│  │  │ 🥇   │ John Doe      │ Enterprise  │ $250K   │ 125%      │ │  │
│  │  │ 🥈   │ Jane Smith    │ SMB         │ $180K   │ 120%      │ │  │
│  │  │ 🥉   │ Bob Johnson  │ Retail      │ $145K   │ 97%       │ │  │
│  │  └───────────────────────────────────────────────────────────┘ │  │
│  └─────────────────────────────────────────────────────────────┘  │
│                                                                    │
│  ┌─────────────────────────────────────────────────────────────┐  │
│  │              MAJOR DEALS                                     │  │
│  │  ┌───────────────────────────────────────────────────────────┐ │  │
│  │  │ Deal #    │ Company      │ Value   │ Stage   │ Owner      │ │  │
│  │  ├───────────────────────────────────────────────────────────┤ │  │
│  │  │ D-2501    │ TechCorp NG  │ $250K   │ Negot   │ John D.    │ │  │
│  │  │ D-2502    │ Global Co    │ $500K   │ Prop    │ Sarah W.   │ │  │
│  │  │ D-2503    │ Prime Ltd    │ $180K   │ Qual    │ Jane S.    │ │  │
│  │  └───────────────────────────────────────────────────────────┘ │  │
│  └─────────────────────────────────────────────────────────────┘  │
│                                                                    │
└─────────────────────────────────────────────────────────────────────┘
```

### 4.5 Global Pipeline Flow

```
┌───────────────────────────────────────────────────────────────────┐
│                    GLOBAL PIPELINE VIEW                           │
├───────────────────────────────────────────────────────────────────┤
│                                                                    │
│  ┌─────────────────────────────────────────────────────────────┐  │
│  │  FILTERS                                                       │  │
│  │  [All Countries ▼] [All Stages ▼] [Value > $100K] [Major Deals]│  │
│  └─────────────────────────────────────────────────────────────┘  │
│                                                                    │
│  ┌─────────────────────────────────────────────────────────────┐  │
│  │              PIPELINE SUMMARY BY COUNTRY                      │  │
│  │  ┌───────────────────────────────────────────────────────────┐ │  │
│  │  │ Country │ Pipeline │ Deals │ Weighted │ Avg Size │ Trend │ │  │
│  │  ├───────────────────────────────────────────────────────────┤ │  │
│  │  │ 🇳🇬 NG   │ $3.5M    │ 287   │ $1.8M    │ $12.2K   │ 📈    │ │  │
│  │  │ 🇰🇪 KE   │ $2.8M    │ 198   │ $1.4M    │ $14.1K   │ 📈    │ │  │
│  │  │ 🇬🇭 GH   │ $2.1M    │ 156   │ $1.0M    │ $13.5K   │ ➡️    │ │  │
│  │  │ 🇿🇦 ZA   │ $4.2M    │ 342   │ $2.5M    │ $12.3K   │ 📈    │ │  │
│  │  │ TOTAL   │ $12.6M   │ 983   │ $6.7M    │ $12.8K   │ 📈    │ │  │
│  │  └───────────────────────────────────────────────────────────┘ │  │
│  └─────────────────────────────────────────────────────────────┘  │
│                                                                    │
│  ┌─────────────────────────────────────────────────────────────┐  │
│  │              MAJOR DEALS TRACKER (> $50K)                     │  │
│  │  ┌───────────────────────────────────────────────────────────┐ │  │
│  │  │ Deal #  │ Company     │ Country│ Value │ Stage │ Close   │ │  │
│  │  ├───────────────────────────────────────────────────────────┤ │  │
│  │  │ D-5001  │ Global Corp │ 🇳🇬 NG │ $500K │ Negot │ Feb 15 │ │  │
│  │  │ D-5002  │ TechPrime   │ 🇿🇦 ZA │ $350K │ Prop  │ Feb 20 │ │  │
│  │  │ D-5003  │ MegaTrade   │ 🇰🇪 KE │ $280K │ Negot │ Feb 18 │ │  │
│  │  │ D-5004  │ PrimeGroup  │ 🇳🇬 NG │ $250K │ Prop  │ Feb 25 │ │  │
│  │  │ D-5005  │ EliteCorp   │ 🇿🇦 ZA │ $200K │ Qual  │ Mar 01 │ │  │
│  │  └───────────────────────────────────────────────────────────┘ │  │
│  └─────────────────────────────────────────────────────────────┘  │
│                                                                    │
│  ┌─────────────────────────────────────────────────────────────┐  │
│  │              PIPELINE BY STAGE                                │  │
│  │  ┌───────────────────────────────────────────────────────────┐ │  │
│  │  │ Stage       │ Count │ Value   │ % of Pipe │ Weighted      │ │  │
│  │  ├───────────────────────────────────────────────────────────┤ │  │
│  │  │ New         │ 156   │ $780K   │ 6%        │ $0           │ │  │
│  │  │ Qualified   │ 287   │ $2.8M   │ 22%       │ $560K        │ │  │
│  │  │ Proposal    │ 245   │ $4.2M   │ 33%       │ $2.1M        │ │  │
│  │  │ Negotiating │ 198   │ $3.5M   │ 28%       │ $2.6M        │ │  │
│  │  │ Closing     │ 97    │ $1.4M   │ 11%       │ $1.3M        │ │  │
│  │  └───────────────────────────────────────────────────────────┘ │  │
│  └─────────────────────────────────────────────────────────────┘  │
│                                                                    │
└─────────────────────────────────────────────────────────────────────┘
```

---

## 5. MODULE-SPECIFIC FLOWS

### 5.1 Global Sales Teams Management Flow

```
┌───────────────────────────────────────────────────────────────────┐
│                    GLOBAL SALES TEAMS                             │
├───────────────────────────────────────────────────────────────────┤
│                                                                    │
│  1. COUNTRY DIRECTORS                                               │
│     ├─ View all Country Sales Directors                            │
│     ├─ Compare director performance across countries               │
│     ├─ Director performance rankings                               │
│     └─ Director quota attainment tracking                          │
│                                                                    │
│  2. REGIONAL MANAGERS                                               │
│     ├─ View regional managers by country                           │
│     ├─ Regional performance comparison                             │
│     ├─ Cross-regional performance benchmarking                     │
│     └─ Regional quota tracking                                     │
│                                                                    │
│  3. PERFORMANCE LEADERBOARD                                         │
│     ├─ Global sales performer rankings                            │
│     ├─ Top performers by country                                  │
│     ├─ Top performers by department                               │
│     └─ Month-over-month performance changes                        │
│                                                                    │
│  4. COMMISSION TRACKING                                             │
│     ├─ Global commission payouts                                  │
│     ├─ Commission by country                                      │
│     ├─ Pending commission approvals                               │
│     └─ Commission structure effectiveness                          │
│                                                                    │
│  5. TEAM ANALYTICS                                                 │
│     ├─ Team size vs performance correlation                        │
│     ├─ Optimal team composition analysis                           │
│     ├─ Hiring needs by country                                    │
│     └─ Talent distribution across regions                          │
│                                                                    │
└─────────────────────────────────────────────────────────────────────┘
```

### 5.2 Global Forecasting Flow

```
┌───────────────────────────────────────────────────────────────────┐
│                    GLOBAL FORECASTING                             │
├───────────────────────────────────────────────────────────────────┤
│                                                                    │
│  1. WORLDWIDE REVENUE FORECAST                                     │
│     ├─ Aggregate forecast from all countries                      │
│     ├─ Best/worst/likely case scenarios                           │
│     ├─ Seasonality adjustments                                    │
│     ├─ Currency impact analysis                                   │
│     └─ Forecast accuracy tracking                                 │
│                                                                    │
│  2. REGIONAL FORECASTS                                             │
│     ├─ Country-by-country forecast breakdown                      │
│     ├─ Regional contribution to global total                      │
│     ├─ Emerging market projections                                │
│     └─ Developed market projections                               │
│                                                                    │
│  3. TREND ANALYSIS                                                  │
│     ├─ Global revenue trends (quarterly, annually)                │
│     ├─ Market share trends                                        │
│     ├─ Product/service mix trends                                 │
│     ├─ Customer segment trends                                    │
│     └─ Competitive analysis                                       │
│                                                                    │
│  4. PREDICTIVE ANALYTICS                                           │
│     ├─ AI-powered revenue predictions                             │
│     ├─ Market opportunity identification                          │
│     ├─ Risk indicators across regions                             │
│     ├─ Growth recommendations                                      │
│     └─ Investment prioritization                                  │
│                                                                    │
└─────────────────────────────────────────────────────────────────────┘
```

### 5.3 Global Reports Flow

```
┌───────────────────────────────────────────────────────────────────┐
│                    GLOBAL REPORTS                                 │
├───────────────────────────────────────────────────────────────────┤
│                                                                    │
│  1. EXECUTIVE DASHBOARDS                                           │
│     ├─ C-level sales overview                                     │
│     ├─ Board reporting                                            │
│     ├─ Investor relations metrics                                 │
│     └─ Real-time executive KPIs                                   │
│                                                                    │
│  2. COUNTRY REPORTS                                                │
│     ├─ Individual country deep-dive                               │
│     ├─ Country comparison reports                                │
│     ├─ Regional summary reports                                   │
│     └─ Market entry analysis                                      │
│                                                                    │
│  3. PRODUCT/SERVICE REPORTS                                        │
│     ├─ Global product performance                                 │
│     ├─ Product mix by country                                     │
│     ├─ Service performance metrics                                │
│     └─ Cross-selling analysis                                     │
│                                                                    │
│  4. CUSTOMER REPORTS                                               │
│     ├─ Global customer segmentation                               │
│     ├─ Multi-national account reports                             │
│     ├─ Customer lifetime value analysis                           │
│     └─ Churn analysis by region                                   │
│                                                                    │
│  5. CUSTOM REPORT BUILDER                                          │
│     ├─ Drag-and-drop report builder                               │
│     ├─ Custom metric definitions                                  │
│     ├─ Scheduled report generation                                │
│     └─ Automated distribution                                      │
│                                                                    │
└─────────────────────────────────────────────────────────────────────┘
```

### 5.4 Sales Team Partners Management Flow (NEW)

```
┌───────────────────────────────────────────────────────────────────┐
│              SALES TEAM PARTNERS - GLOBAL MANAGEMENT              │
├───────────────────────────────────────────────────────────────────┤
│                                                                    │
│  1. GLOBAL PARTNERS OVERVIEW                                       │
│     ├─ Total active Sales Partners by country                       │
│     ├─ Partner distribution by type (8 types)                      │
│     ├─ Partner tier distribution (Bronze, Silver, Gold, Platinum)  │
│     ├─ Global partner acquisition metrics                          │
│     └─ Cross-country partner performance comparison                │
│                                                                    │
│  2. PARTNER APPLICATIONS MANAGEMENT                                │
│     ├─ Pending applications queue (global view)                    │
│     ├─ Application approval workflow                                │
│     │   ├─ Review application details                              │
│     │   ├─ Verify territory alignment                              │
│     │   ├─ Check background verification status                    │
│     │   ├─ Approve/Reject applications                             │
│     │   └─ Route to appropriate partner dashboard                   │
│     ├─ Approved partners monitoring                                 │
│     └─ Rejected applications tracking                               │
│                                                                    │
│  3. PARTNER PERFORMANCE TRACKING                                    │
│     ├─ Partner leaderboard by commission earned                     │
│     ├─ Partner performance by partner type                          │
│     │   ├─ Courier Partners performance                             │
│     │   ├─ Haulage Partners performance                             │
│     │   ├─ Warehouse Partners performance                           │
│     │   ├─ E-commerce Vendors performance                          │
│     │   ├─ Air/Ocean Agents performance                            │
│     │   ├─ Location Agents performance                            │
│     │   ├─ Wholesale Partners performance                          │
│     │   └─ Influencer Partners performance                         │
│     ├─ Top performing partners by country                           │
│     └─ Partners requiring intervention                              │
│                                                                    │
│  4. COMMISSION MANAGEMENT                                           │
│     ├─ Global commission payout summary                             │
│     ├─ Partner referral bonus tracking                              │
│     ├─ Customer sales commission tracking                          │
│     ├─ Tier progression monitoring                                  │
│     ├─ Payout processing and approval                               │
│     └─ Commission dispute resolution                                │
│                                                                    │
│  5. TERRITORY ALLOCATION MANAGEMENT                                  │
│     ├─ AI lead generation overview                                   │
│     ├─ Lead distribution by territory                               │
│     ├─ Territory capacity planning                                  │
│     ├─ Lead allocation adjustments                                   │
│     └─ Partner-territory reallocation                               │
│                                                                    │
│  6. PARTNER ANALYTICS                                               │
│     ├─ Partner acquisition trends                                   │
│     ├─ Partner activity monitoring                                  │
│     ├─ Revenue contribution by partners                             │
│     ├─ Partner churn analysis                                       │
│     └─ ROI per partner by type                                      │
│                                                                    │
└─────────────────────────────────────────────────────────────────────┘
```

### 5.5 Sales Team Partner Application Approval Flow (NEW)

```
┌───────────────────────────────────────────────────────────────────┐
│          PARTNER APPLICATION APPROVAL WORKFLOW                      │
├───────────────────────────────────────────────────────────────────┤
│                                                                    │
│  [NEW APPLICATION]                                                  │
│         │                                                            │
│         ▼                                                            │
│  ┌─────────────────────────────────────────────────────────────┐   │
│  │  APPLICATION QUEUE                                            │   │
│  │  • New partner applications from sales-team-app              │   │
│  │  • Sorted by: Date, Type, Territory                           │   │
│  │  • Filter by: Partner Type, Country, Territory               │   │
│  └─────────────────────────────────────────────────────────────┘   │
│         │                                                            │
│         ▼                                                            │
│  ┌─────────────────────────────────────────────────────────────┐   │
│  │  REVIEW APPLICATION                                           │   │
│  │  • Applicant Information                                     │   │
│  │  • Sales Experience History                                   │   │
│  │  • Preferred Partner Types                                    │   │
│  │  • Preferred Territory                                        │   │
│  │  • Bank Details (for commission)                              │   │
│  └─────────────────────────────────────────────────────────────┘   │
│         │                                                            │
│         ▼                                                            │
│  ┌─────────────────────────────────────────────────────────────┐   │
│  │  BACKGROUND VERIFICATION STATUS                                │   │
│  │  • BVN Validation: ✓                                          │   │
│  │  • National ID: ✓                                             │   │
│  │  • Employment Check: ✓                                        │   │
│  │  • References: Pending                                        │   │
│  └─────────────────────────────────────────────────────────────┘   │
│         │                                                            │
│         ▼                                                            │
│  ┌─────────────────────────────────────────────────────────────┐   │
│  │  TERRITORY ALIGNMENT CHECK                                    │   │
│  │  • Is preferred territory available?                          │   │
│  │  • Current partner capacity in territory                       │   │
│  │  • Lead allocation availability                               │   │
│  │  • Recommend: Same territory OR Alternative                    │   │
│  └─────────────────────────────────────────────────────────────┘   │
│         │                                                            │
│         ▼                                                            │
│  ┌─────────────────────────────────────────────────────────────┐   │
│  │  DECISION                                                      │   │
│  │  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐      │   │
│  │  │   APPROVE    │  │    REJECT    │  │   REQUEST    │      │   │
│  │  │              │  │              │  │   MORE INFO  │      │   │
│  │  └──────────────┘  └──────────────┘  └──────────────┘      │   │
│  └─────────────────────────────────────────────────────────────┘   │
│         │                                                            │
│         ├─── [APPROVED] ───────────────────────────────────────┐    │
│         │                                                          │    │
│         ▼                                                          │    │
│  ┌─────────────────────────────────────────────────────────────┐   │    │
│  │  CREATE PARTNER ACCOUNT                                     │   │    │
│  │  • Generate Partner ID (SP-YYYY-XXXX)                       │   │    │
│  │  • Assign Territory                                        │   │    │
│  │  • Set Initial Tier (Bronze)                                 │   │    │
│  │  • Configure Lead Allocation                                │   │    │
│  │  • Send Welcome Notification                                 │   │    │
│  └─────────────────────────────────────────────────────────────┘   │    │
│         │                                                          │    │
│         ▼                                                          │    │
│  ┌─────────────────────────────────────────────────────────────┐   │    │
│  │  ROUTE TO COUNTRY SALES DASHBOARD                            │   │    │
│  │  • For territory management                                  │   │    │
│  │  • For lead distribution                                    │   │    │
│  │  • For local performance monitoring                         │   │    │
│  └─────────────────────────────────────────────────────────────┘   │    │
│         │                                                          │    │
│         ▼                                                          │    │
│  ┌─────────────────────────────────────────────────────────────┐   │    │
│  │  NOTIFY APPLICANT                                           │   │    │
│  │  • Push notification to sales-team-app                      │   │    │
│  │  • Email confirmation                                       │   │    │
│  │  • Next steps instructions                                  │   │    │
│  └─────────────────────────────────────────────────────────────┘   │    │
│         │                                                          │    │
│         └─── [REJECTED] ──────────────────────────────────────┐    │
│                                                                  │    │
│  ┌─────────────────────────────────────────────────────────────┐   │    │
│  │  NOTIFY APPLICANT OF REJECTION                              │   │    │
│  │  • Push notification                                        │   │    │
│  │  • Email with reason                                        │   │    │
│  │  • Option to reapply after 90 days                          │   │    │
│  └─────────────────────────────────────────────────────────────┘   │    │
│                                                                    │
└─────────────────────────────────────────────────────────────────────┘
```

### 5.6 Sales Team Partners - AI Lead Management Flow (NEW)

```
┌───────────────────────────────────────────────────────────────────┐
│              SALES TEAM PARTNERS - AI LEAD MANAGEMENT             │
├───────────────────────────────────────────────────────────────────┤
│                                                                    │
│  1. AI LEAD GENERATION OVERVIEW                                    │
│     ├─ Total leads generated by AI service                         │
│     ├─ Lead distribution by country                                 │
│     ├─ Lead distribution by territory                               │
│     ├─ Lead quality metrics (AI scores)                            │
│     └─ Lead conversion rates                                        │
│                                                                    │
│  2. LEAD ALLOCATION VIEW                                          │
│     ├─ Territory-wise lead allocation                              │
│     ├─ Partner capacity in each territory                          │
│     ├─ Unassigned leads pool                                       │
│     ├─ Lead assignment recommendations                             │
│     └─ Bulk lead reassignment tool                                 │
│                                                                    │
│  3. LEAD QUALITY MONITORING                                        │
│     ├─ AI score distribution trends                                │
│     ├─ Conversion rate by score range                             │
│     ├─ Lead source effectiveness                                   │
│     ├─ Territory performance by lead quality                     │
│     └─ AI accuracy tracking                                       │
│                                                                    │
│  4. PARTNER PERFORMANCE BY LEADS                                   │
│     ├─ Leads assigned per partner                                 │
│     ├─ Lead response time by partner                               │
│     ├─ Conversion rate by partner                                 │
│     ├─ Top performers in lead conversion                           │
│     └─ Partners requiring additional training                     │
│                                                                    │
└─────────────────────────────────────────────────────────────────────┘
```

---

## 6. DATA FLOW ARCHITECTURE

### 6.1 Data Sources & Integration Points

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                     HQ SALES DASHBOARD DATA FLOW                            │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│  ┌─────────────────────────────────────────────────────────────────────┐   │
│  │                    BUSINESS DOMAIN                                  │   │
│  │  Country Sales Dashboards (Source of Truth)                         │   │
│  │  ┌──────────────────────────────────────────────────────────────┐   │   │
│  │  │ Nigeria Sales │ Kenya Sales │ Ghana Sales │ S. Africa Sales...│   │   │
│  │  │ Dashboard     │ Dashboard   │ Dashboard   │ Dashboard        │   │   │
│  │  └──────────────────────────────────────────────────────────────┘   │   │
│  └─────────────────────────────────────────────────────────────────────┘   │
│                                │                                            │
│                                │ Data Replication (Scheduled + Real-time) │
│                                ▼                                            │
│  ┌─────────────────────────────────────────────────────────────────────┐   │
│  │                    MANAGEMENT DOMAIN                                │   │
│  │  HQ Sales Dashboard (Data Aggregation Layer)                        │   │
│  │  ┌──────────────────────────────────────────────────────────────┐   │   │
│  │  │ Global Sales Data Warehouse (Read-Only Aggregates)            │   │   │
│  │  │ • Daily summary reports from each country                     │   │   │
│  │  │ • Real-time deal events via WebSocket                         │   │   │
│  │  │ • Aggregated metrics and KPIs                                │   │   │
│  │  │ • Cross-country analytics                                   │   │   │
│  │  └──────────────────────────────────────────────────────────────┘   │   │
│  └─────────────────────────────────────────────────────────────────────┘   │
│                                │                                            │
│                                ▼                                            │
│  ┌─────────────────────────────────────────────────────────────────────┐   │
│  │                    HQ SALES API LAYER                               │   │
│  │  (REST + GraphQL + WebSocket)                                      │   │
│  └─────────────────────────────────────────────────────────────────────┘   │
│                                │                                            │
│                                ▼                                            │
│  ┌─────────────────────────────────────────────────────────────────────┐   │
│  │                    HQ SALES DASHBOARD FRONTEND                       │   │
│  │  (React Application)                                                │   │
│  └─────────────────────────────────────────────────────────────────────┘   │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

### 6.2 Real-Time Data Updates (WebSocket)

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                    REAL-TIME UPDATE FLOW                                   │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│  WebSocket Connection: wss://sales.hq.api/v1/ws                             │
│                                                                              │
│  Subscriptions (Aggregated from all countries):                             │
│  • /topic/global/dashboard - Global metrics updates                         │
│  • /topic/countries/{code} - Country-specific updates                       │
│  • /topic/deals/major - Major deal changes (> $100K)                        │
│  • /topic/deals/won - Deal closure events (all deals)                       │
│  • /topic/quotas - Global quota attainment updates                          │
│  • /topic/forecast - Forecast data refresh                                 │
│                                                                              │
│  Message Format (STOMP):                                                     │
│  {                                                                          │
│    "type": "DEAL_WON",                                                       │
│    "dealId": "D-5001",                                                      │
│    "country": "NG",                                                         │
│    "value": 500000,                                                         │
│    "timestamp": "2025-02-08T10:30:00Z"                                    │
│  }                                                                          │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

### 6.3 Data Aggregation Flow

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                 DATA AGGREGATION: COUNTRIES → HQ                           │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│  DAILY AGGREGATION (Scheduled):                                              │
│  ┌─────────────────────────────────────────────────────────────────────┐   │
│  │ Each Country Sales Dashboard → HQ Data Warehouse                     │   │
│  │                                                                      │   │
│  │ Data Points:                                                        │   │
│  │ • Total revenue (by country, by product, by segment)                │   │
│  │ • Pipeline value (by stage, by owner)                               │   │
│  │ • Deals closed (count, value, by stage transition)                  │   │
│  │ • Team performance (by individual, by department)                   │   │
│  │ • Quota attainment (by individual, by territory, by country)        │   │
│  │ • Customer metrics (new, active, churned)                           │   │
│  │                                                                      │   │
│  │ Process:                                                            │   │
│  │ 1. Each country dashboard publishes daily summary                    │   │
│  │ 2. HQ aggregator processes and stores in data warehouse             │   │
│  │ 3. Pre-computed aggregations available for dashboard queries        │   │
│  │ 4. Historical data maintained for trend analysis                    │   │
│  └─────────────────────────────────────────────────────────────────────┘   │
│                                                                              │
│  REAL-TIME EVENTS (WebSocket):                                               │
│  ┌─────────────────────────────────────────────────────────────────────┐   │
│  │ Event Types Broadcast to HQ:                                         │   │
│  │ • Deal stage changes (especially for major deals)                   │   │
│  │ • Deal closures (won/lost)                                         │   │
│  │ • Quota attainment milestones (>80%, >100%)                        │   │
│  │ • New major opportunities (>$100K)                                 │   │
│  │ • Alerts and escalations                                            │   │
│  └─────────────────────────────────────────────────────────────────────┘   │
│                                                                              │
│  ON-DEMAND QUERIES:                                                          │
│  ┌─────────────────────────────────────────────────────────────────────┐   │
│  │ HQ Dashboard → Country APIs (Read-Only)                             │   │
│  │ • Drill-down queries for specific country details                   │   │
│  │ • Cross-country comparison queries                                  │   │
│  │ • Custom report generation                                          │   │
│  └─────────────────────────────────────────────────────────────────────┘   │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## 7. ERROR HANDLING & STATES

### 7.1 Loading States

| Component | Loading State | Description |
|-----------|---------------|-------------|
| Global Overview | Skeleton cards + progress bars | Metric cards loading |
| Country Grid | Shimmer effect for each card | Country data loading |
| Comparison Table | Skeleton rows | Table data loading |
| Charts | Circular progress + placeholder | Chart data loading |
| Pipeline | Gray placeholder stages | Pipeline loading |

### 7.2 Error States

| Error Type | UI Response | Recovery Options |
|------------|-------------|------------------|
| Network Error | "Connection lost. Retrying..." banner | Auto-retry with exponential backoff |
| Auth Error | Redirect to login | Re-authenticate |
| Country API Error | Partial data display + warning | Retry country data fetch |
| Aggregation Error | "Some data unavailable" message | Display available data, retry failed |
| Server Error | "Something went wrong" message | Retry button + support contact |

### 7.3 Empty States

| Scenario | Empty State Design |
|----------|-------------------|
| No Countries | "No countries configured. Contact administrator." |
| No Deals | "No deals found across all countries." |
| No Comparison Data | "Select countries to compare performance." |
| No Reports | "No reports available. Generate your first report." |

---

## VERSION HISTORY

| Version | Date | Changes |
|---------|------|---------|
| 1.0 | 2025-02-08 | Initial HQ Sales Dashboard UI Flow Documentation |
| 1.1 | 2025-02-16 | Added Sales Team Partners module with global partner management, application approvals, commission tracking, and AI lead management |

---

## SUMMARY

### Sales Domain Documentation Complete

**Business Domain - Country-Sales-Dashboard (4 files)** ✓
- 01_UI_Flow_Documentation.md ✓
- 02_Wireframes_Documentation.md ✓
- 03_Mock_Flow_Documentation.md ✓
- 04_Page_By_Page_Flow_Documentation.md ✓

**Management Domain - HQ Sales Dashboard (1 of 4 files)** ✓
- 01_UI_Flow_Documentation.md ✓
- 02_Wireframes_Documentation.md (pending)
- 03_Mock_Flow_Documentation.md (pending)
- 04_Page_By_Page_Flow_Documentation.md (pending)

---

## NEXT STEPS

- Complete 02_Wireframes_Documentation.md (Visual mockups)
- Complete 03_Mock_Flow_Documentation.md (API endpoints, data models)
- Complete 04_Page_By_Page_Flow_Documentation.md (Detailed page flows)

**Remaining Management Domain Documentation:**
2. Finance-department (Finance) - 4 files needed
3. Global-business-management (GBM) - 4 files needed
4. Customer-support (Support) - 4 files needed
5. Digital-marketing (Marketing) - 4 files needed
6. System-Administrator (Admin) - 4 files needed

**Total Remaining: 27 documentation files (3 HQ Sales + 5 other domains × 4 files each)**

---

**Document End**
