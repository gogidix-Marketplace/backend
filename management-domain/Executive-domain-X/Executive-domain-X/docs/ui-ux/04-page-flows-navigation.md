# Gogidix Executive Domain - Page Flows & Navigation

**Version:** 3.0
**Last Updated:** 2026-04-22
**Status:** Active

---

## Table of Contents

1. [Navigation Architecture](#navigation-architecture)
2. [CEO Page Flows](#ceo-page-flows)
3. [CFO Page Flows](#cfo-page-flows)
4. [COO Page Flows](#coo-page-flows)
5. [CTO Page Flows](#cto-page-flows)
6. [Cross-Cutting Flows](#cross-cutting-flows)
7. [Interaction Patterns](#interaction-patterns)
8. [Cross-Department Approval Flows](#8-cross-department-approval-flows)
9. [Department Reporting Navigation Flows](#9-department-reporting-navigation-flows)
10. [Department Drill-Down Navigation Flows](#10-department-drill-down-navigation-flows)
11. [Business Unit Navigation](#11-business-unit-navigation)
12. [CEO Business Unit Drill-Down Flows](#12-ceo-business-unit-drill-down-flows)
13. [CFO Business Unit Drill-Down Flows](#13-cfo-business-unit-drill-down-flows)
14. [COO Business Unit Drill-Down Flows](#14-coo-business-unit-drill-down-flows)
15. [CTO Business Unit Drill-Down Flows](#15-cto-business-unit-drill-down-flows)
16. [Business Unit Route Map](#16-business-unit-route-map)

---

## 1. Navigation Architecture

### Role-Based Navigation Structure

```
                    ┌───────────────────┐
                    │   Login Page      │
                    │ (with 2FA)        │
                    └────────┬──────────┘
                             │
                ┌────────────┴────────────┐
                │    Role Selection       │
                └────────────┬────────────┘
                             │
        ┌────────────────────┼────────────────────┐
        │                    │                    │
        ▼                    ▼                    ▼
┌───────────┐        ┌───────────┐        ┌───────────┐
│    CEO    │        │   CFO     │        │   COO     │        CTO
│  Suite    │        │  Suite    │        │  Suite    │      Suite
└───────────┘        └───────────┘        └───────────┘
     │                    │                    │
     └────────────────────┴────────────────────┘
                          │
                ┌─────────┴─────────┐
                │  Shared Services │
                │  (Auth, API, WS) │
                └──────────────────┘
```

### Navigation Menus by Role

#### CEO Navigation

```
┌─────────────────────┐
│ CEO Suite Navigation│
├─────────────────────┤
│ 📊 Overview         │ → /ceo (Dashboard)
│ 🏢 Departments     │ → /ceo/departments
│ 💼 Business Units  │ → /ceo/business-units
│ 🎯 Strategy         │ → /ceo/strategy
│ 📈 Analytics        │ → /ceo/analytics
│ ✅ Approvals        │ → /ceo/approvals
│ 📄 Reports          │ → /ceo/reports
│ ⚙️ Settings         │ → /ceo/settings
└─────────────────────┘
```

#### CFO Navigation

```
┌─────────────────────┐
│ CFO Suite Navigation│
├─────────────────────┤
│ 📊 Overview         │ → /cfo (Dashboard)
│ 🏢 Departments     │ → /cfo/departments
│ 💼 Business Units  │ → /cfo/business-units
│ 💰 Budget           │ → /cfo/budget
│ 📉 Financials       │ → /cfo/financials
│ 📈 Forecast         │ → /cfo/forecast
│ 🛡️ Compliance       │ → /cfo/compliance
│ 📄 Reports          │ → /cfo/reports
│ ✅ Approvals        │ → /cfo/approvals
│ ⚙️ Settings         │ → /cfo/settings
└─────────────────────┘
```

#### COO Navigation

```
┌─────────────────────┐
│ COO Suite Navigation│
├─────────────────────┤
│ 📊 Overview         │ → /coo (Dashboard)
│ 🏢 Departments     │ → /coo/departments
│ 💼 Business Units  │ → /coo/business-units
│ ⚡ Operations       │ → /coo/operations
│ 🚨 Incidents        │ → /coo/incidents
│ 👥 Resources        │ → /coo/resources
│ 📄 Reports          │ → /coo/reports
│ ✅ Approvals        │ → /coo/approvals
│ ⚙️ Settings         │ → /coo/settings
└─────────────────────┘
```

#### CTO Navigation

```
┌─────────────────────┐
│ CTO Suite Navigation│
├─────────────────────┤
│ 📊 Overview         │ → /cto (Dashboard)
│ 🏢 Departments     │ → /cto/departments
│ 💼 Business Units  │ → /cto/business-units
│ 🏗️ Infrastructure   │ → /cto/infrastructure
│ 👨‍💻 Engineering      │ → /cto/engineering
│ 🔒 Security         │ → /cto/security
│ 🗺️ Roadmap          │ → /cto/roadmap
│ 👥 Users            │ → /cto/users
│ 📄 Reports          │ → /cto/reports
│ ✅ Approvals        │ → /cto/approvals
│ ⚙️ Settings         │ → /cto/settings
└─────────────────────┘
```

---

## 2. CEO Page Flows

### CEO Dashboard → KPI Detail Flow

```
CEO Dashboard (Overview)
       │
       │  User clicks on Revenue KPI Card
       ▼
┌─────────────────────────────────────┐
│  Revenue KPI Detail Modal           │
│  ─────────────────────────────────  │
│  • View 7-day trend chart           │
│  • Breakdown by domain              │
│  • Breakdown by region              │
│  • Compare to previous periods      │
│  • Set alert threshold              │
│  • Export data                      │
│                                     │
│  Actions:                           │
│  • [Drill Down to Domain]           │
│  • [Set Alert]                      │
│  • [Export CSV/PDF]                 │
│  • [Close]                          │
└─────────────────────────────────────┘
       │
       │  User clicks "Drill Down to Executive Domain"
       ▼
┌─────────────────────────────────────┐
│  Executive Domain Detail Page       │
│  ─────────────────────────────────  │
│  • Domain overview metrics          │
│  • Service health                   │
│  • Active incidents                 │
│  • Team performance                 │
│  • Recent activities                │
│                                     │
│  [Back to Dashboard] [View Domain]  │
└─────────────────────────────────────┘
```

### CEO Approval Flow

```
CEO Dashboard (Overview)
       │
       │  User clicks Pending Approvals (5)
       ▼
┌─────────────────────────────────────┐
│  Approvals Page                     │
│  ─────────────────────────────────  │
│  Filter: [All ▼] [Budget] [Hire]   │
│  Sort: [Urgency ▼] [Date] [Type]   │
│                                     │
│  ┌───────────────────────────────┐ │
│  │ 💰 Budget: Marketing Q2       │ │
│  │ €250,000 | Nigeria | Urgent   │ │
│  │ [Quick View] [Approve] [Skip] │ │
│  └───────────────────────────────┘ │
│                                     │
│  ┌───────────────────────────────┐ │
│  │ 👤 Hire: CTO Position         │ │
│  │ Executive | High             │ │
│  │ [Quick View] [Approve] [Skip] │ │
│  └───────────────────────────────┘ │
│  ... more items ...                │
└─────────────────────────────────────┘
       │
       │  User clicks "Quick View" on Budget item
       ▼
┌─────────────────────────────────────┐
│  Approval Detail Modal              │
│  ─────────────────────────────────  │
│  Budget Request: Marketing Q2      │
│  Amount: €250,000                   │
│  Region: Nigeria                    │
│  Requester: Marketing Director     │
│  Justification: [Full text]        │
│                                     │
│  Budget Breakdown:                  │
│  • Digital Ads: €150,000            │
│  • Events: €75,000                  │
│  • Content: €25,000                 │
│                                     │
│  Attachments: [PDF] [Spreadsheet]   │
│                                     │
│  [Add Comment] [Reject] [Approve ✓]│
└─────────────────────────────────────┘
       │
       │  User clicks "Approve"
       ▼
┌─────────────────────────────────────┐
│  Approval Confirmation              │
│  ─────────────────────────────────  │
│  ✓ Approval Successful              │
│                                     │
│  The budget has been approved.      │
│  Notifications sent to:             │
│  • Marketing Director               │
│  • CFO                              │
│                                     │
│  [View Another] [Back to Dashboard] │
└─────────────────────────────────────┘
```

### CEO Crisis Management Flow

```
CEO Dashboard (Overview)
       │
       │  User sees Crisis Center with 2 active alerts
       ▼
┌─────────────────────────────────────┐
│  Crisis Center Expanded             │
│  ─────────────────────────────────  │
│  🔴 CRITICAL (2)  ⚠️ WARNING (1)   │
│                                     │
│  ┌───────────────────────────────┐ │
│  │ 🔴 Payment Gateway Down       │ │
│  │ Nigeria | 45 min ago         │ │
│  │ Impact: High | 23% revenue   │ │
│  │ Assigned: sysadmin-ng         │ │
│  │ ETA: 15 min                  │ │
│  │                             │ │
│  │ [Investigate] [Escalate]     │ │
│  └───────────────────────────────┘ │
│                                     │
│  ┌───────────────────────────────┐ │
│  │ ⚠️ HR Onboarding Backlog      │ │
│  │ Ireland | 2 hrs ago          │ │
│  │ Impact: Medium               │ │
│  │                             │ │
│  │ [Review] [Delegate]          │ │
│  └───────────────────────────────┘ │
└─────────────────────────────────────┘
       │
       │  User clicks "Investigate" on Payment Gateway
       ▼
┌─────────────────────────────────────┐
│  Incident Detail Page               │
│  ─────────────────────────────────  │
│  Payment Gateway Incident           │
│  Severity: P1 - Critical            │
│  Status: Investigating             │
│                                     │
│  Timeline:                          │
│  • 14:32 - Detected (automatic)     │
│  • 14:35 - Assigned to sysadmin-ng  │
│  • 14:40 - Investigation started    │
│  • 14:45 - ETA: 15 min             │
│                                     │
│  Impact:                            │
│  • 23% of payments failing          │
│  • Nigeria region affected          │
│  • Estimated revenue loss: €12K/hr │
│                                     │
│  Related Systems:                   │
│  • Payment Service: ● 0%           │
│  • Notification Service: ● 100%    │
│                                     │
│  [Escalate to P0] [Contact Team]    │
│  [Monitor] [Subscribe to Updates]   │
└─────────────────────────────────────┘
```

### CEO Strategy Page Flow

```
CEO Navigation → Strategy
       │
       ▼
┌─────────────────────────────────────┐
│  Strategy Page                      │
│  ─────────────────────────────────  │
│  ┌───────────────────────────────┐ │
│  │ Strategic Initiatives          │ │
│  │ ─────────────────────────────  │ │
│  │                               │ │
│  │ ┌───────────────────────────┐ │ │
│  │ │ 🎯 Africa Expansion       │ │ │
│  │ │ Q2 2026 | 60% Complete    │ │ │
│  │ │ ████████████░░░░░░░░░░░  │ │ │
│  │ │ Owner: CEO                │ │ │
│  │ │ [View →] [Update]         │ │ │
│  │ └───────────────────────────┘ │ │
│  │                               │ │
│  │ ┌───────────────────────────┐ │ │
│  │ │ 🚀 Digital Transformation │ │ │
│  │ │ Q3 2026 | 35% Complete    │ │ │
│  │ │ ████████░░░░░░░░░░░░░░░  │ │ │
│  │ │ Owner: CTO                │ │ │
│  │ │ [View →] [Update]         │ │ │
│  │ └───────────────────────────┘ │ │
│  └───────────────────────────────┘ │
│                                     │
│  ┌───────────────────────────────┐ │
│  │ KPI Tracking                   │ │
│  │ ─────────────────────────────  │ │
│  │ Revenue Growth: ✓ On Track     │ │
│  │ Market Share: ▲ Ahead          │ │
│  │ Customer Satisfaction: ● Risk  │ │
│  └───────────────────────────────┘ │
│                                     │
│  [Create New Initiative]           │
└─────────────────────────────────────┘
       │
       │  User clicks "View" on Africa Expansion
       ▼
┌─────────────────────────────────────┐
│  Initiative Detail Page             │
│  ─────────────────────────────────  │
│  🎯 Africa Expansion Initiative     │
│  ─────────────────────────────────  │
│  Target: Expand to 5 African markets│
│  Timeline: Q2 2026                  │
│  Budget: €2.5M                      │
│  Progress: 60%                      │
│                                     │
│  Milestones:                        │
│  ✓ Market Research Complete         │
│  ✓ Legal Setup Complete             │
│  → Nigeria Launch (In Progress)     │
│  ⏳ Kenya Launch (Pending)           │
│  ⏳ South Africa Launch (Pending)   │
│                                     │
│  Team:                              │
│  • Owner: CEO                       │
│  • Sponsor: CFO                     │
│  • Members: 8                       │
│                                     │
│  Risks:                             │
│  ⚠️ Currency volatility             │
│  ⚠️ Regulatory delays               │
│                                     │
│  [Update Progress] [Add Milestone]   │
│  [Report Issue] [Back to Strategy]  │
└─────────────────────────────────────┘
```

### CEO Analytics Page Flow

```
CEO Navigation → Analytics
       │
       ▼
┌─────────────────────────────────────┐
│  Analytics Page                     │
│  ─────────────────────────────────  │
│  Time Range: [Last 30 days ▼]       │
│  Compare: [Previous Period ▼]       │
│  Domain: [All Domains ▼]            │
│                                     │
│  ┌───────────────────────────────┐ │
│  │ Revenue Trend                 │ │
│  │ [Line Chart]                  │ │
│  └───────────────────────────────┘ │
│                                     │
│  ┌───────────────────────────────────┐ │
│  │ Domain Performance Comparison      │ │
│  │ [Bar Chart - Side by side]         │ │
│  └───────────────────────────────────┘ │
│                                     │
│  ┌───────────────────────────────┐ │
│  │ Regional Heat Map             │ │
│  │ [Interactive Map]              │ │
│  └───────────────────────────────┘ │
│                                     │
│  [Custom Report] [Export Dashboard] │
└─────────────────────────────────────┘
       │
       │  User clicks on a data point in chart
       ▼
┌─────────────────────────────────────┐
│  Data Point Detail Modal            │
│  ─────────────────────────────────  │
│  Revenue: March 15, 2026            │
│  Value: $1.42M                      │
│  Change: +12.3% vs previous         │
│                                     │
│  Top Contributors:                  │
│  • Executive: $520K                 │
│  • Business: $380K                  │
│  • Public: $220K                    │
│                                     │
│  Notable Events:                    │
│  • New customer signup: Acme Corp   │
│  • Campaign launch: Spring Sale     │
│                                     │
│  [View Transaction Detail] [Close]  │
└─────────────────────────────────────┘
```

---

## 3. CFO Page Flows

### CFO Dashboard → Budget Detail Flow

```
CFO Dashboard (Overview)
       │
       │  User clicks Budget vs Actual widget
       ▼
┌─────────────────────────────────────┐
│  Budget Management Page             │
│  ─────────────────────────────────  │
│  Period: [Q1 2026 ▼]                │
│  Currency: [EUR € ▼]                │
│                                     │
│  Overall Budget Status:              │
│  Total Budget: €42.5M                │
│  Spent: €38.2M (89.9%)              │
│  Remaining: €4.3M                   │
│                                     │
│  ┌───────────────────────────────┐ │
│  │ Department Budgets            │ │
│  │ ─────────────────────────────  │ │
│  │                               │ │
│  │ Marketing  €2.5M  ████████░░  │ │
│  │ 82% used  €450K remaining     │ │
│  │                               │ │
│  │ Operations  €8.2M  ████████░░ │ │
│  │ 95% used  €410K remaining     │ │
│  │                               │ │
│  │ Technology  €3.8M  ████████░░ │ │
│  │ 78% used  €836K remaining     │ │
│  │                               │ │
│  │ HR  €2.1M  ████████░░         │ │
│  │ 88% used  €252K remaining     │ │
│  └───────────────────────────────┘ │
│                                     │
│  [Request Budget Change]            │
└─────────────────────────────────────┘
       │
       │  User clicks Marketing budget
       ▼
┌─────────────────────────────────────┐
│  Department Budget Detail           │
│  ─────────────────────────────────  │
│  Marketing Department - Q1 2026     │
│  Total Budget: €2,500,000            │
│  Spent: €2,050,000 (82%)             │
│  Remaining: €450,000                │
│                                     │
│  Budget Breakdown:                  │
│  ┌───────────────────────────────┐ │
│  │ Digital Ads     €1.5M  95%   │ │
│  │ ████████████████████░░░░░    │ │
│  └───────────────────────────────┘ │
│  ┌───────────────────────────────┐ │
│  │ Events         €0.75M  70%   │ │
│  │ ████████████████░░░░░░░░░░░  │ │
│  └───────────────────────────────┘ │
│  ┌───────────────────────────────┐ │
│  │ Content        €0.25M  60%   │ │
│  │ ████████████░░░░░░░░░░░░░░░  │ │
│  └───────────────────────────────┘ │
│                                     │
│  Transactions (Last 10):            │
│  • Mar 28: Google Ads €45,000      │
│  • Mar 27: Event Venue €25,000     │
│  • Mar 26: Content Agency €12,000  │
│  ...                                │
│                                     │
│  [View All Transactions]            │
│  [Request Adjustment] [Export]      │
└─────────────────────────────────────┘
```

### CFO Forecast Flow

```
CFO Navigation → Forecast
       │
       ▼
┌─────────────────────────────────────┐
│  Financial Forecast Page             │
│  ─────────────────────────────────  │
│  Forecast Period: Q2 2026            │
│  Confidence Level: [95% ▼]           │
│                                     │
│  ┌───────────────────────────────┐ │
│  │ Revenue Forecast               │ │
│  │                               │ │
│  │ Projected: €48.2M             │ │
│  │ Range: €45.8M - €50.6M        │ │
│  │                               │ │
│  │ [Chart with confidence band]  │ │
│  └───────────────────────────────┘ │
│                                     │
│  Key Drivers:                        │
│  • Africa growth: +18%              │
│  • Enterprise sales: +12%           │
│  • New product launch: +8%          │
│                                     │
│  Risk Factors:                       │
│  ⚠️ Currency volatility (Nigeria)   │
│  ⚠️ Supply chain delays              │
│                                     │
│  [Adjust Assumptions] [Export]       │
└─────────────────────────────────────┘
       │
       │  User clicks "Adjust Assumptions"
       ▼
┌─────────────────────────────────────┐
│  Forecast Assumptions Modal         │
│  ─────────────────────────────────  │
│  Adjust forecast assumptions to     │
│  see impact on projections.         │
│                                     │
│  Growth Rate:           [12.3 %]    │
│  Churn Rate:           [2.1 %]      │
│  Market Expansion:     [5.0 %]      │
│  Currency Impact:      [-1.5 %]     │
│                                     │
│  Updated Projection: €48.2M         │
│  Range: €45.8M - €50.6M              │
│                                     │
│  [Reset] [Apply] [Save Scenario]     │
└─────────────────────────────────────┘
```

---

## 4. COO Page Flows

### COO Incident Management Flow

```
COO Navigation → Incidents
       │
       ▼
┌─────────────────────────────────────┐
│  Incident Management Page            │
│  ─────────────────────────────────  │
│  Filter: [All ▼] [Open] [Assigned]  │
│  Sort: [Severity ▼] [Time]           │
│                                     │
│  ┌───────────────────────────────┐ │
│  │ 🔴 P1 Database Latency        │ │
│  │ Nigeria | 25 min ago         │ │
│  │ Assigned: sysadmin-ng         │ │
│  │ ETA: 15 min                  │ │
│  │ [View] [Assign] [Escalate]    │ │
│  └───────────────────────────────┘ │
│                                     │
│  ┌───────────────────────────────┐ │
│  │ ⚠️  P2 Payment Queue          │ │
│  │ S. Africa | 1 hr ago         │ │
│  │ Assigned: ops-sa             │ │
│  │ [View] [Assign] [Monitor]     │ │
│  └───────────────────────────────┘ │
│                                     │
│  [Create New Incident]              │
└─────────────────────────────────────┘
       │
       │  User clicks "View" on P1 incident
       ▼
┌─────────────────────────────────────┐
│  Incident Detail Page               │
│  ─────────────────────────────────  │
│  🔴 P1: Database Latency            │
│  ─────────────────────────────────  │
│  Location: Nigeria Production       │
│  Started: 25 minutes ago            │
│  Status: Investigating             │
│  Assignee: sysadmin-ng              │
│  ETA: 15 minutes                    │
│                                     │
│  Impact:                            │
│  • Users affected: 2,347            │
│  • Response time: +2.5s             │
│  • Error rate: 23%                  │
│                                     │
│  Timeline:                          │
│  • 14:32 - Alert triggered           │
│  • 14:35 - Auto-scaling initiated   │
│  • 14:38 - Assigned to sysadmin-ng  │
│  • 14:42 - Investigation in progress│
│                                     │
│  Communications:                    │
│  • Email sent to affected users     │
│  • Status page updated              │
│                                     │
│  Related Incidents:                  │
│  • #1234 - Cache issues (Resolved)  │
│                                     │
│  [Update Status] [Reassign]          │
│  [Escalate to P0] [Notify Users]     │
│  [Add Note] [Resolve]                │
└─────────────────────────────────────┘
       │
       │  User clicks "Reassign"
       ▼
┌─────────────────────────────────────┐
│  Reassign Incident Modal            │
│  ─────────────────────────────────  │
│  Reassign P1: Database Latency       │
│                                     │
│  Current Assignee: sysadmin-ng       │
│                                     │
│  New Assignee:                      │
│  ┌───────────────────────────────┐ │
│  │ Search team members...        │ │
│  │ ─────────────────────────────  │ │
│  │ db-admin-ke  ● Available      │ │
│  │ db-admin-ng ⚠️ Busy           │ │
│  │ ops-lead     ● Available      │ │
│  └───────────────────────────────┘ │
│                                     │
│  Priority: [P1 - Critical ▼]         │
│                                     │
│  Comment: [________________]         │
│                                     │
│  [Cancel] [Reassign]                │
└─────────────────────────────────────┘
```

### COO Resource Optimization Flow

```
COO Navigation → Resources
       │
       ▼
┌─────────────────────────────────────┐
│  Resource Allocation Page            │
│  ─────────────────────────────────  │
│  Department Utilization              │
│                                     │
│  ┌───────────────────────────────┐ │
│  │ Engineering  78%              │ │
│  │ ████████░░                   │ │
│  │ Capacity: 50 | Used: 39       │ │
│  │ [Optimize]                   │ │
│  └───────────────────────────────┘ │
│                                     │
│  ┌───────────────────────────────┐ │
│  │ Operations  85%               │ │
│  │ ████████░░                   │ │
│  │ Capacity: 120 | Used: 102     │ │
│  │ [Optimize]                   │ │
│  └───────────────────────────────┘ │
│                                     │
│  ┌───────────────────────────────┐ │
│  │ Support  92%                  │ │
│  │ ████████░░                   │ │
│  │ Capacity: 25 | Used: 23       │ │
│  │ [Optimize]                   │ │
│  └───────────────────────────────┘ │
│                                     │
│  AI Suggestion:                     │
│  💡 Support team at 92% capacity.  │
│     Consider 2 temporary hires     │
│     or redistribute load.          │
│     [View Analysis] [Dismiss]      │
└─────────────────────────────────────┘
       │
       │  User clicks "Optimize" on Engineering
       ▼
┌─────────────────────────────────────┐
│  Resource Optimization Panel        │
│  ─────────────────────────────────  │
│  Engineering Department             │
│  Current: 78% utilized (39/50)      │
│                                     │
│  Active Projects:                   │
│  • Platform Redesign (12 people)    │
│  • Mobile App V2 (10 people)         │
│  • API Gateway (8 people)           │
│  • Bug Fixes (9 people)             │
│                                     │
│  Available Team Members:            │
│  • Sarah Chen (Full Stack)          │
│  • Mike Johnson (Backend)           │
│  • Lisa Park (Frontend)             │
│                                     │
│  Optimization Options:              │
│  1. Reallocate 2 from Bug Fixes     │
│     to Mobile App V2                │
│  2. Move API Gateway to QA          │
│     (3 resources freed)             │
│  3. Request 2 additional hires     │
│                                     │
│  [Apply Option 1] [Custom Plan]     │
└─────────────────────────────────────┘
```

---

## 5. CTO Page Flows

### CTO Infrastructure Flow

```
CTO Navigation → Infrastructure
       │
       ▼
┌─────────────────────────────────────┐
│  Infrastructure Dashboard           │
│  ─────────────────────────────────  │
│  Region: [All Regions ▼]            │
│  Environment: [Production ▼]        │
│                                     │
│  ┌───────────────────────────────┐ │
│  │ Infrastructure Health          │ │
│  │                               │ │
│  │ Servers      99.95%  ●        │ │
│  │ Databases    98.00%  ●        │ │
│  │ APIs         98.50%  ●        │ │
│  │ Networks     99.80%  ●        │ │
│  └───────────────────────────────┘ │
│                                     │
│  ┌───────────────────────────────┐ │
│  │ Service Status                 │ │
│  │                               │ │
│  │ ●●●●●●●●●●●●●●●●●●●●●●●●●●●│ │
│  │ 52 services                   │ │
│  │ 50 operational                │ │
│  │ 2 degraded                   │ │
│  └───────────────────────────────┘ │
│                                     │
│  [View All Services] [Deployments]  │
└─────────────────────────────────────┘
       │
       │  User clicks "View All Services"
       ▼
┌─────────────────────────────────────┐
│  Service List Page                  │
│  ─────────────────────────────────  │
│  Filter: [All ▼] [Operational]      │
│  Search: [________________]         │
│                                     │
│  ┌───────────────────────────────┐ │
│  │ ceo-analytics-service         │ │
│  │ ● Operational 99.9%  Ireland  │ │
│  │ CPU: 45% | Memory: 62%        │ │
│  │ [View] [Restart] [Scale]      │ │
│  └───────────────────────────────┘ │
│                                     │
│  ┌───────────────────────────────┐ │
│  │ payment-gateway-service      │ │
│  │ 🔴 Degraded 0.0%  Nigeria    │ │
│  │ CPU: 95% | Memory: 98%       │ │
│  │ [View] [Logs] [Restart]       │ │
│  └───────────────────────────────┘ │
│  ... more services ...              │
└─────────────────────────────────────┘
       │
       │  User clicks "View" on degraded service
       ▼
┌─────────────────────────────────────┐
│  Service Detail Page                │
│  ─────────────────────────────────  │
│  payment-gateway-service            │
│  Status: 🔴 Degraded                │
│  Region: Nigeria                    │
│  Version: 2.3.1                     │
│                                     │
│  Metrics:                           │
│  • CPU: 95% (⚠️ High)               │
│  • Memory: 98% (⚠️ High)            │
│  • Response Time: 2.5s (🔴 Slow)    │
│  • Error Rate: 23% (🔴 High)        │
│                                     │
│  Recent Events:                     │
│  • 14:32 - Latency spike detected    │
│  • 14:35 - Auto-scaling triggered   │
│  • 14:38 - Capacity reached         │
│                                     │
│  [View Logs] [Scale Up] [Rollback]  │
│  [Restart Service]                  │
└─────────────────────────────────────┘
```

### CTO Roadmap Flow

```
CTO Navigation → Roadmap
       │
       ▼
┌─────────────────────────────────────┐
│  Innovation Roadmap Page            │
│  ─────────────────────────────────  │
│  View: [Quarter ▼] [Timeline] [Kanban]│
│                                     │
│  Q2 2026                            │
│  ┌───────────────────────────────┐ │
│  │ 🤖 AI Integration Phase 2     │ │
│  │ ████████████████░░░░░░░░░░░  │ │
│  │ 60% Complete | Owner: CTO     │ │
│  │                               │ │
│  │ Tasks:                        │ │
│  │ ✓ Design Complete            │ │
│  │ ✓ API Development            │ │
│  │ → Model Training (In Prog)   │ │
│  │ ⏳ Integration               │ │
│  │ ⏳ Testing                   │ │
│  │                               │ │
│  │ [View Details] [Update]       │ │
│  └───────────────────────────────┘ │
│                                     │
│  ┌───────────────────────────────┐ │
│  │ 🔄 Microservices Migration    │ │
│  │ ████████████████████████░░░░  │ │
│  │ 85% Complete | Owner: Lead Arch│ │
│  │                               │ │
│  │ Tasks:                        │ │
│  │ ✓ Auth Service Migrated      │ │
│  │ ✓ Payment Service Migrated   │ │
│  │ → Analytics (In Progress)    │ │
│  │ ⏳ Reporting                 │ │
│  │                               │ │
│  │ [View Details] [Update]       │ │
│  └───────────────────────────────┘ │
│                                     │
│  [Add Initiative] [Export Roadmap]  │
└─────────────────────────────────────┘
       │
       │  User clicks "View Details" on AI Integration
       ▼
┌─────────────────────────────────────┐
│  Initiative Detail Page             │
│  ─────────────────────────────────  │
│  🤖 AI Integration Phase 2          │
│  ─────────────────────────────────  │
│  Target: Deploy AI-powered insights │
│  Timeline: Q2 2026                  │
│  Budget: €500K                      │
│  Progress: 60%                      │
│                                     │
│  Deliverables:                      │
│  ✓ Predictive Analytics Engine      │
│  ✓ Anomaly Detection System         │
│  → NLP Query Interface              │
│  ⏳ Automated Reporting             │
│                                     │
│  Team:                              │
│  • Owner: CTO                       │
│  • Lead: AI Engineer                │
│  • Team: 8 engineers                │
│                                     │
│  Blockers:                          │
│  ⚠️ GPU availability in production   │
│  ⚠️ Model training data quality     │
│                                     │
│  Dependencies:                      │
│  • Data Platform V2 (Complete)      │
│  • API Gateway (In Progress)        │
│                                     │
│  [Update Progress] [Report Blocker]  │
│  [Add Dependency] [Back to Roadmap]  │
└─────────────────────────────────────┘
```

---

## 6. Cross-Cutting Flows

### Authentication Flow

```
┌─────────────────────────────────────┐
│  Login Page                         │
│  ─────────────────────────────────  │
│         ┌─────────┐                │
│         │    G    │                │
│         └─────────┘                │
│        Gogidix Executive           │
│                                   │
│  ┌─────────────────────────────┐  │
│  │ Email                       │  │
│  │ ─────────────────────────── │  │
│  └─────────────────────────────┘  │
│                                   │
│  ┌─────────────────────────────┐  │
│  │ Password                    │  │
│  │ ─────────────────────────── │  │
│  └─────────────────────────────┘  │
│                                   │
│  [  Log In  ]  [Forgot Password?] │
│                                   │
│  Or enter 2FA code                │
│  ┌─────────────────────────────┐  │
│  │ [6-digit code]              │  │
│  └─────────────────────────────┘  │
│                                   │
└─────────────────────────────────────┘
       │
       │  User enters credentials and clicks Log In
       ▼
┌─────────────────────────────────────┐
│  Two-Factor Authentication          │
│  ─────────────────────────────────  │
│  Enter the 6-digit code from your   │
│  authenticator app.                │
│                                   │
│  ┌─────────────────────────────┐  │
│  │ [ _ _ _ _ _ _ ]            │  │
│  └─────────────────────────────┘  │
│                                   │
│  Code expires in 4:32              │
│                                   │
│  [Verify] [Resend Code]            │
└─────────────────────────────────────┘
       │
       │  2FA Successful
       ▼
┌─────────────────────────────────────┐
│  Role Selection                     │
│  ─────────────────────────────────  │
│  Welcome, Sarah Mitchell!           │
│                                   │
│  Select your dashboard:            │
│                                   │
│  ┌─────────────────────────────┐  │
│  │ 📊 CEO Dashboard            │  │
│  │    Strategic oversight      │  │
│  └─────────────────────────────┘  │
│                                   │
│  ┌─────────────────────────────┐  │
│  │ 💰 CFO Dashboard            │  │
│  │    Financial management     │  │
│  └─────────────────────────────┘  │
│                                   │
│  ┌─────────────────────────────┐  │
│  │ ⚡ COO Dashboard            │  │
│  │    Operations oversight     │  │
│  └─────────────────────────────┘  │
│                                   │
│  ┌─────────────────────────────┐  │
│  │ 🖥️  CTO Dashboard           │  │
│  │    Technology oversight     │  │
│  └─────────────────────────────┘  │
│                                   │
│  Default: CEO Dashboard            │
│  [Continue to Dashboard]           │
└─────────────────────────────────────┘
       │
       │  User selects CEO Dashboard
       ▼
┌─────────────────────────────────────┐
│  CEO Dashboard (Main)               │
│  (As shown in previous flows)       │
└─────────────────────────────────────┘
```

### Settings Flow (All Roles)

```
Any Dashboard → Settings
       │
       ▼
┌─────────────────────────────────────┐
│  Settings Page                      │
│  ─────────────────────────────────  │
│  ┌───────────────────────────────┐ │
│  │ Navigation:                   │ │
│  │ • Profile                     │ │
│  │ • Preferences                 │ │
│  │ • Notifications              │ │
│  │ • Security                    │ │
│  │ • Integrations                │ │
│  └───────────────────────────────┘ │
│                                     │
│  ┌───────────────────────────────┐ │
│  │ Profile Settings              │ │
│  │ ─────────────────────────────  │ │
│  │                               │ │
│  │  ┌─────────────────────────┐  │ │
│  │  │ [Avatar]                │  │ │
│  │  │ [Change Photo]           │  │ │
│  │  └─────────────────────────┘  │ │
│  │                               │ │
│  │  Full Name: [Sarah Mitchell]  │ │
│  │  Email: [sarah@gogidix.com]   │ │
│  │  Phone: [+353 1 234 5678]     │ │
│  │  Timezone: [(UTC+0) Dublin ▼] │ │
│  │  Language: [English ▼]        │ │
│  │                               │ │
│  │  [Save Changes]               │ │
│  └───────────────────────────────┘ │
└─────────────────────────────────────┘
       │
       │  User clicks Notifications
       ▼
┌─────────────────────────────────────┐
│  Notification Settings              │
│  ─────────────────────────────────  │
│  Email Notifications:               │
│  ☑ Critical alerts                  │
│  ☑ Approval requests                │
│  ☐ Daily summary                    │
│  ☑ Weekly reports                   │
│                                     │
│  Push Notifications:                │
│  ☑ Critical alerts                  │
│  ☑ Approval requests                │
│  ☐ KPI updates                     │
│                                     │
│  Alert Thresholds:                  │
│  • Revenue: Notify if change > 10% │
│  • Incident: Notify all P1/P2      │
│  • Budget: Notify if > 90% used    │
│                                     │
│  [Save Changes]                     │
└─────────────────────────────────────┘
```

---

## 7. Interaction Patterns

### Click Patterns Summary

| Action | Primary | Secondary | Result |
|--------|---------|-----------|--------|
| **View KPI Details** | Click KPI card | Hover + click | Modal with drill-down |
| **Approve Request** | Click approve button | Swipe right (mobile) | Success toast |
| **View Crisis** | Click crisis card | Click bell icon | Crisis detail page |
| **Navigate** | Click sidebar item | Type Cmd+K | Navigate to page |
| **Switch Role** | Click role tab | Profile menu | Reload dashboard |
| **Export Data** | Click export button | Right-click | Download file |
| **Set Alert** | Click set alert | Long press | Alert configuration |

### Keyboard Shortcuts

| Shortcut | Action | Availability |
|----------|--------|--------------|
| `Cmd/Ctrl + K` | Open command palette | All pages |
| `Cmd/Ctrl + /` | Show keyboard shortcuts | All pages |
| `Cmd/Ctrl + B` | Toggle sidebar | Dashboard pages |
| `Cmd/Ctrl + F` | Focus search | All pages |
| `Esc` | Close modal/drawer | Modals |
| `← →` | Navigate tabs | Dashboard |
| `↑ ↓` | Navigate list | Lists/tables |
| `Enter` | Select/submit | Forms |
| `Space` | Expand/collapse | Expandable items |
| `1-4` | Switch role (CEO/CFO/COO/CTO) | Dashboard |

### Touch Gestures (Mobile)

| Gesture | Action | Availability |
|---------|--------|--------------|
| **Swipe Left** | Approve item | Approvals list |
| **Swipe Right** | Reject/skip item | Approvals list |
| **Pull Down** | Refresh dashboard | Dashboard |
| **Long Press** | Context menu | Lists, cards |
| **Pinch** | Zoom chart | Charts |
| **Double Tap** | Expand card | Cards |
| **Swipe from Left** | Open sidebar | All pages |

---

## 8. Cross-Department Approval Flows

### Budget Increase Approval Flow (Marketing → CFO → CEO)

```
Marketing Dashboard
       │
       │  James Smith submits budget increase (EUR 250K)
       ▼
┌─────────────────────────────────────┐
│  Executive Approval Queue (CFO)     │
│  ─────────────────────────────────  │
│  [Megaphone] Marketing Budget       │
│  EUR 250,000 increase | Africa      │
│  Priority: HIGH | Due: 3 days       │
│                                     │
│  [Quick Approve ✓]                  │
│  [Review Details →]                 │
│  [Delegate →]                       │
│  [Reject ✕]                         │
└─────────────────────────────────────┘
       │
       │  CFO clicks "Quick Approve"
       ▼
┌─────────────────────────────────────┐
│  Confirmation Toast                 │
│  ✓ CFO approved Marketing budget   │
│  Next: CEO review required          │
│  [Undo - 5s]                        │
└─────────────────────────────────────┘
       │
       │  Notification pushed to CEO
       ▼
┌─────────────────────────────────────┐
│  Executive Approval Queue (CEO)     │
│  ─────────────────────────────────  │
│  [Megaphone] Marketing Budget       │
│  EUR 250,000 increase | Africa      │
│  ✅ CFO Approved by Marcus Chen     │
│  Priority: HIGH | Due: 2 days       │
│                                     │
│  [Quick Approve ✓]                  │
│  [Review Details →]                 │
│  [Request Changes]                  │
│  [Reject ✕]                         │
└─────────────────────────────────────┘
       │
       │  CEO clicks "Quick Approve"
       ▼
┌─────────────────────────────────────┐
│  Approval Complete                  │
│  ─────────────────────────────────  │
│  ✓ Budget approved                  │
│  EUR 2.75M total marketing budget   │
│                                     │
│  Notifications sent:                │
│  • James Smith (Marketing)          │
│  • Marcus Chen (CFO)               │
│  • Amina Okonkwo (COO)             │
│                                     │
│  [View Updated Budget] [Close]      │
└─────────────────────────────────────┘
```

### Security Incident Escalation Flow (SysAdmin → CTO → CEO)

```
SysAdmin Monitoring Dashboard
       │
       │  CRITICAL security event detected
       ▼
┌─────────────────────────────────────┐
│  Push Notification (CTO)            │
│  ─────────────────────────────────  │
│  🔴 SECURITY ALERT                  │
│  Possible data breach - Nigeria     │
│  Detected: 2 min ago                │
│  Severity: CRITICAL                 │
│  [View →] [Acknowledge]             │
└─────────────────────────────────────┘
       │
       │  CTO acknowledges + escalates
       ▼
┌─────────────────────────────────────┐
│  CEO Crisis Alert                   │
│  ─────────────────────────────────  │
│  🔴 CRITICAL SECURITY INCIDENT      │
│  Possible data breach - Nigeria     │
│  CTO: Raj Patel investigating       │
│  Impact: 2,347 customers affected   │
│                                     │
│  [View Incident →]                  │
│  [Escalate to Board →]              │
│  [Schedule Emergency Meeting]        │
└─────────────────────────────────────┘
       │
       │  Resolution
       ▼
┌─────────────────────────────────────┐
│  Resolution Notification            │
│  ─────────────────────────────────  │
│  ✓ Incident resolved                │
│  False positive - no data breach    │
│  Resolved by: Priya Sharma          │
│  Time to resolve: 45 minutes        │
│                                     │
│  [View Post-Mortem →] [Close]       │
└─────────────────────────────────────┘
```

### Deal Discount Approval Flow (Sales → CEO + CFO)

```
Sales Dashboard
       │
       │  David Okafor submits deal >15% discount
       ▼
┌─────────────────────────────────────┐
│  Approval Queue (CEO + CFO)         │
│  ─────────────────────────────────  │
│  [TrendingUp] Deal Discount         │
│  18% on EUR 1.2M deal              │
│  Customer: Acme Corp | Region: KE   │
│  Requires: CEO AND CFO approval     │
│                                     │
│  Approval Status:                   │
│  ⏳ CFO: Pending                    │
│  ○  CEO: Awaiting CFO              │
│                                     │
│  [CFO: Approve ✓] [Review →]        │
└─────────────────────────────────────┘
       │
       │  CFO approves → CEO gets notified
       ▼
┌─────────────────────────────────────┐
│  Approval Queue (CEO)               │
│  ─────────────────────────────────  │
│  [TrendingUp] Deal Discount         │
│  ✅ CFO Approved (Marcus Chen)       │
│  ⏳ CEO: Your turn                  │
│                                     │
│  [CEO: Approve ✓] [Reject ✕]        │
└─────────────────────────────────────┘
       │
       │  Both approved
       ▼
┌─────────────────────────────────────┐
│  ✓ Deal discount approved           │
│  18% discount on EUR 1.2M           │
│  Notification sent to David Okafor  │
└─────────────────────────────────────┘
```

---

## 9. Department Reporting Navigation Flows

### Department Overview Navigation (All Roles)

```
Executive Dashboard (Overview)
       │
       │  Click "Departments" in sidebar
       ▼
┌─────────────────────────────────────┐
│  Departments Page                   │
│  ─────────────────────────────────  │
│  [All] [Marketing] [Support]        │
│  [Business] [HR] [Sales]            │
│  [SysAdmin] [Finance] [Foundation]  │
│                                     │
│  8 department cards (grid)          │
│  Each showing:                      │
│  • Health status                    │
│  • Key metric                       │
│  • Alert count                      │
│  • Pending items                    │
│                                     │
│  [Click any department →]           │
└─────────────────────────────────────┘
       │
       │  Click "Digital Marketing"
       ▼
┌─────────────────────────────────────┐
│  Department Detail: Marketing       │
│  ─────────────────────────────────  │
│  Tabs:                              │
│  [Overview] [KPIs] [Budget]         │
│  [Reports] [Approvals] [Services]   │
│                                     │
│  Overview:                          │
│  • ROI: 340% ▲ 12%                 │
│  • Leads: 4,231 ▲ 18%              │
│  • CPL: $32.50 ▼ 12%               │
│  • Spend: $2.05M (82% used)         │
│  • 3 pending approvals              │
│  • 0 critical alerts                │
│                                     │
│  [Back to Departments] [Export]      │
└─────────────────────────────────────┘
       │
       │  Click "Budget" tab
       ▼
┌─────────────────────────────────────┐
│  Marketing Budget Detail            │
│  ─────────────────────────────────  │
│  Allocated: $2.5M                   │
│  Spent: $2.05M (82%)               │
│  Remaining: $450K                   │
│                                     │
│  Breakdown:                         │
│  • Digital Ads: $1.5M (95% used)    │
│  • Events: $0.75M (70% used)        │
│  • Content: $0.25M (60% used)       │
│                                     │
│  Trending:                          │
│  [Budget vs Spend chart]            │
│                                     │
│  [Request Increase] [Export]         │
└─────────────────────────────────────┘
```

### Cross-Department Report Navigation

```
Executive Dashboard → Reports
       │
       ▼
┌─────────────────────────────────────┐
│  Reports Page                       │
│  ─────────────────────────────────  │
│  Report Types:                      │
│  [Executive Summary]                │
│  [Department Comparison]            │
│  [Financial Package]                │
│  [Operational Review]               │
│  [Technology Health]                │
│  [Custom Report Builder]            │
│                                     │
│  Scheduled Reports:                 │
│  • Morning Brief (Daily 6:30 AM)    │
│  • Weekly Business Review (Mon)     │
│  • Monthly Financial Package (5th)  │
│                                     │
│  [Generate New Report →]            │
└─────────────────────────────────────┘
       │
       │  Click "Department Comparison"
       ▼
┌─────────────────────────────────────┐
│  Department Comparison Report       │
│  ─────────────────────────────────  │
│  Period: [Q1 2026 ▼]               │
│  Departments: [All ▼] (multi)       │
│  Metrics: [Budget] [Headcount]      │
│           [Revenue] [Performance]   │
│                                     │
│  [Side-by-side comparison table]    │
│  [Bar charts by department]         │
│  [Trend lines]                      │
│                                     │
│  [Download PDF] [Share] [Schedule]  │
└─────────────────────────────────────┘
```

---

## 10. Department Drill-Down Navigation Flows

### CEO Department Drill-Down

```
CEO Dashboard
       │
       │  Click Marketing KPI Card
       ▼
CEO/Departments/Digital-Marketing/Overview
       │
       ├─→ /ceo/departments/marketing/kpis     (KPI details)
       ├─→ /ceo/departments/marketing/budget    (Budget details)
       ├─→ /ceo/departments/marketing/campaigns (Campaign list)
       └─→ /ceo/departments/marketing/insights  (AI insights)
```

### CFO Department Financial Drill-Down

```
CFO Dashboard
       │
       │  Click Finance Department Card
       ▼
CFO/Departments/Finance/Overview
       │
       ├─→ /cfo/departments/finance/budget      (Budget management)
       ├─→ /cfo/departments/finance/invoices     (AP/AR queue)
       ├─→ /cfo/departments/finance/tax          (Tax compliance)
       ├─→ /cfo/departments/finance/cashflow     (Cashflow statement)
       ├─→ /cfo/departments/finance/consolidation (Multi-entity)
       └─→ /cfo/departments/finance/compliance   (Financial compliance)
```

### COO Department Operations Drill-Down

```
COO Dashboard
       │
       │  Click any Department Health Card
       ▼
COO/Departments/{Department}/Overview
       │
       ├─→ /coo/departments/{dept}/operations   (Operational metrics)
       ├─→ /coo/departments/{dept}/incidents     (Active incidents)
       ├─→ /coo/departments/{dept}/resources     (Resource allocation)
       ├─→ /coo/departments/{dept}/compliance    (Operational compliance)
       └─→ /coo/departments/{dept}/performance   (Performance trends)
```

### CTO Department Technology Drill-Down

```
CTO Dashboard
       │
       │  Click any Department Service Health
       ▼
CTO/Departments/{Department}/Overview
       │
       ├─→ /cto/departments/{dept}/services      (Service list & health)
       ├─→ /cto/departments/{dept}/security       (Security posture)
       ├─→ /cto/departments/{dept}/deployments    (Deployment history)
       ├─→ /cto/departments/{dept}/infrastructure (Infra metrics)
       └─→ /cto/departments/{dept}/compliance     (Tech compliance)
```

### Department Route Map

| Route | Accessible By | Content |
|---|---|---|
| `/{role}/departments` | CEO, CFO, COO, CTO | 8 department cards |
| `/{role}/departments/{dept}` | CEO, CFO, COO, CTO | Department overview (role-filtered) |
| `/{role}/departments/{dept}/approvals` | CEO, CFO, COO, CTO | Pending approvals from this dept |
| `/{role}/departments/{dept}/reports` | CEO, CFO, COO, CTO | Department reports |
| `/{role}/approvals` | CEO, CFO, COO, CTO | All approvals (cross-dept) |
| `/{role}/approvals/{id}` | CEO, CFO, COO, CTO | Approval detail modal |
| `/{role}/reports/department-comparison` | CEO, CFO, COO, CTO | Cross-dept comparison |
| `/{role}/reports/generate` | CEO, CFO, COO, CTO | Report builder |

---

## 11. Business Unit Navigation

### Business Units Overview Page (All Roles)

```
Executive Dashboard (Overview)
       │
       │  Click "Business Units" in sidebar
       ▼
┌─────────────────────────────────────┐
│  Business Units Page                │
│  ─────────────────────────────────  │
│  [All] [Courier] [E-Commerce]       │
│  [Warehouse] [Air] [Ocean]          │
│  [Haulage] [Procurement] [Admin]    │
│                                     │
│  8 business unit cards (grid)       │
│  Each showing:                      │
│  • Revenue / GMV / Spend            │
│  • Growth rate                      │
│  • Service health (X/Y healthy)     │
│  • Key operational KPI              │
│  • Alert count                      │
│                                     │
│  [Click any business unit →]        │
└─────────────────────────────────────┘
       │
       │  Click "Courier Services"
       ▼
┌─────────────────────────────────────┐
│  Business Unit Detail: Courier      │
│  ─────────────────────────────────  │
│  Tabs (role-filtered):              │
│  [Overview] [Revenue] [Operations]  │
│  [Services] [Reports] [Approvals]   │
│                                     │
│  Overview (CEO view):               │
│  • Revenue: $8.2M ▲ 12%            │
│  • Deliveries: 3,842 today          │
│  • Active drivers: 345              │
│  • OTD rate: 94%                    │
│  • Services: 20/20 healthy          │
│  • Platform commission: 15%         │
│                                     │
│  [Back to Business Units] [Export]  │
└─────────────────────────────────────┘
```

### Business Unit Selector Navigation Pattern

```
Any Dashboard Page
       │
       │  Click Business Unit filter in header
       ▼
┌─────────────────────────────────────┐
│  Business Unit Quick Switch         │
│  ─────────────────────────────────  │
│  [Truck]         Courier      $8.2M │
│  [ShoppingCart]  E-Commerce  $24.5M │
│  [Warehouse]     Storage     $5.1M  │
│  [Plane]         Air         $3.8M  │
│  [Ship]          Ocean       $4.2M  │
│  [Route]         Haulage    $12.8M  │
│  [Clipboard]     Procurement $18M   │
│  [Building]      Admin       $1.5M  │
│                                     │
│  [All Business Units]               │
└─────────────────────────────────────┘
       │
       │  Select a unit → filters all
       │  dashboard widgets to that unit
       ▼
  Dashboard filtered to selected unit
```

---

## 12. CEO Business Unit Drill-Down Flows

### CEO → Courier Revenue Detail

```
CEO Dashboard → Business Units → Courier
       │
       ▼
/ceo/business-units/courier
       │
       ├─→ /ceo/business-units/courier/revenue      (Revenue breakdown, commission trend)
       ├─→ /ceo/business-units/courier/volume        (Delivery volume, geographic map)
       ├─→ /ceo/business-units/courier/drivers       (Active drivers, partner network)
       ├─→ /ceo/business-units/courier/satisfaction  (CSAT, reviews, complaint trend)
       └─→ /ceo/business-units/courier/services      (20-service health overview)
```

### CEO → E-Commerce Marketplace Detail

```
CEO Dashboard → Business Units → E-Commerce
       │
       ▼
/ceo/business-units/ecommerce
       │
       ├─→ /ceo/business-units/ecommerce/gmv         (GMV trend, top categories, vendor growth)
       ├─→ /ceo/business-units/ecommerce/vendors      (Active vendors, onboarding pipeline)
       ├─→ /ceo/business-units/ecommerce/marketplace  (Listings, categories, search trends)
       ├─→ /ceo/business-units/ecommerce/customers    (Customer base, growth, retention)
       └─→ /ceo/business-units/ecommerce/services     (76-service health overview)
```

### CEO → Warehousing Detail

```
CEO Dashboard → Business Units → Warehousing
       │
       ▼
/ceo/business-units/warehousing
       │
       ├─→ /ceo/business-units/warehousing/partners   (Partner count, facility map)
       ├─→ /ceo/business-units/warehousing/occupancy  (Occupancy rates, unit availability)
       ├─→ /ceo/business-units/warehousing/revenue     (Storage + fulfillment revenue)
       ├─→ /ceo/business-units/warehousing/fulfillment (Orders processed, speed)
       └─→ /ceo/business-units/warehousing/services    (43-service health overview)
```

### CEO → Air Freight / Ocean / Haulage / Procurement / Admin Drill-Downs

```
/ceo/business-units/air-freight
       ├─→ .../bookings   (Booking volume, service tier mix)
       ├─→ .../routes      (Active routes, coverage map)
       ├─→ .../customs     (Clearance rate, pipeline)
       └─→ .../services    (15-service health)

/ceo/business-units/ocean-shipping
       ├─→ .../bookings    (FCL + LCL volume)
       ├─→ .../vessels     (Live vessel positions, schedule)
       ├─→ .../containers  (Container fleet status)
       └─→ .../services    (18-service health)

/ceo/business-units/haulage
       ├─→ .../loads       (Load volume, FCL/LCL/Bulk/Special)
       ├─→ .../carriers    (Carrier network, coverage)
       ├─→ .../fleet       (Fleet utilization, live map)
       └─→ .../services    (62-service health)

/ceo/business-units/procurement
       ├─→ .../spend       (Total spend, category breakdown)
       ├─→ .../suppliers   (Supplier network, ratings)
       ├─→ .../savings     (Cost savings achieved)
       └─→ .../services    (27-service health)

/ceo/business-units/admin-core
       ├─→ .../partners    (Partner count, tier distribution)
       ├─→ .../compliance  (KYC status, risk scores)
       ├─→ .../settlements (Settlement pipeline)
       └─→ .../services    (11-service health)
```

---

## 13. CFO Business Unit Drill-Down Flows

### CFO → Courier Financial Detail

```
CFO Dashboard → Business Units → Courier
       │
       ▼
/cfo/business-units/courier
       │
       ├─→ /cfo/business-units/courier/commission   (15% commission revenue, trend)
       ├─→ /cfo/business-units/courier/surge          (Surge revenue uplift, multiplier stats)
       ├─→ /cfo/business-units/courier/payouts        (Driver + partner payout liability)
       ├─→ /cfo/business-units/courier/pricing        (Revenue/km, discount impact)
       └─→ /cfo/business-units/courier/reconciliation (Payment reconciliation status)
```

### CFO → E-Commerce Financial Detail

```
CFO Dashboard → Business Units → E-Commerce
       │
       ▼
/cfo/business-units/ecommerce
       │
       ├─→ /cfo/business-units/ecommerce/commissions   (Marketplace commission by tier)
       ├─→ /cfo/business-units/ecommerce/payments       (Payment volume, gateway fees)
       ├─→ /cfo/business-units/ecommerce/returns        (Refund rate, return cost impact)
       ├─→ /cfo/business-units/ecommerce/vendor-payouts (Vendor payout queue, liability)
       └─→ /cfo/business-units/ecommerce/gift-cards     (Gift card liability, float)
```

### CFO → All Units Financial Summary Flow

```
CFO Dashboard → Business Units
       │
       │  Click "Financial Summary"
       ▼
/cfo/business-units/financial-summary
       │
       ├─→ P&L by unit (8 units)
       ├─→ Revenue mix trend (quarterly)
       ├─→ Settlement pipeline (all units)
       ├─→ Commission tracker (all units)
       ├─→ Budget vs actual (cross-unit)
       └─→ Currency exposure (multi-unit)
```

### CFO → Procurement Approval Flow

```
CFO receives procurement approval > $100K
       │
       ▼
┌─────────────────────────────────────┐
│  CFO Approval Queue                 │
│  ─────────────────────────────────  │
│  [Clipboard] Procurement PO         │
│  Amount: $125,000 | Vendor: Acme    │
│  Dept: IT Infrastructure            │
│  3-way match: ✅ Verified           │
│  Budget: $500K allocated            │
│  Remaining after: $375K             │
│                                     │
│  Approval Chain:                    │
│  ✅ Dept Manager (auto < $5K)       │
│  ✅ Procurement Mgr ($5-25K)        │
│  ✅ Finance Director ($25-100K)     │
│  ⏳ CFO (>$100K) — You              │
│                                     │
│  [Approve ✓] [Reject ✕] [Review →] │
└─────────────────────────────────────┘
```

---

## 14. COO Business Unit Drill-Down Flows

### COO → Courier Operations Detail

```
COO Dashboard → Business Units → Courier
       │
       ▼
/coo/business-units/courier
       │
       ├─→ /coo/business-units/courier/dispatches    (Live dispatch grid, real-time WS)
       ├─→ /coo/business-units/courier/performance    (OTD rate, avg delivery time)
       ├─→ /coo/business-units/courier/drivers        (Driver availability, assignment speed)
       ├─→ /coo/business-units/courier/routing        (Route optimization, savings)
       └─→ /coo/business-units/courier/incidents      (Courier-specific incidents)
```

### COO → E-Commerce Fulfillment Detail

```
COO Dashboard → Business Units → E-Commerce
       │
       ▼
/coo/business-units/ecommerce
       │
       ├─→ /coo/business-units/ecommerce/fulfillment  (Fulfillment rate, ship time)
       ├─→ /coo/business-units/ecommerce/inventory     (Inventory health, stock alerts)
       ├─→ /coo/business-units/ecommerce/onboarding    (Vendor onboarding pipeline)
       ├─→ /coo/business-units/ecommerce/carriers      (Carrier integration status)
       └─→ /coo/business-units/ecommerce/returns       (Return processing speed)
```

### COO → Cross-Unit Operations Dashboard Flow

```
COO Dashboard → Business Units
       │
       │  Click "Operations Overview"
       ▼
/coo/business-units/operations-overview
       │
       ├─→ Live operations map (all units combined)
       ├─→ SLA compliance by unit (8 gauges)
       ├─→ Active incidents across all units
       ├─→ Volume snapshot (today's numbers)
       └─→ Capacity utilization (all units)
```

---

## 15. CTO Business Unit Drill-Down Flows

### CTO → Full Service Health Grid

```
CTO Dashboard → Business Units
       │
       │  Click "Service Health Grid"
       ▼
/cto/business-units/service-health
       │
       ├─→ 272-service status grid (all 8 units)
       ├─→ Filter by unit, health status, region
       ├─→ Sort by name, CPU, memory, latency
       └─→ Click any service → service detail page
```

### CTO → E-Commerce Service Detail

```
CTO Dashboard → Business Units → E-Commerce
       │
       ▼
/cto/business-units/ecommerce
       │
       ├─→ /cto/business-units/ecommerce/services      (76-service grid with details)
       ├─→ /cto/business-units/ecommerce/elasticsearch  (ES index health, search perf)
       ├─→ /cto/business-units/ecommerce/kafka          (Event flow, consumer lag)
       ├─→ /cto/business-units/ecommerce/throughput     (Peak order throughput)
       └─→ /cto/business-units/ecommerce/deployments    (Deployment history, pipeline)
```

### CTO → Haulage Service Detail

```
CTO Dashboard → Business Units → Haulage
       │
       ▼
/cto/business-units/haulage
       │
       ├─→ /cto/business-units/haulage/services       (62-service grid)
       ├─→ /cto/business-units/haulage/gps             (GPS tracking latency)
       ├─→ /cto/business-units/haulage/iot             (IoT sensor pipeline)
       ├─→ /cto/business-units/haulage/eld             (ELD integration health)
       └─→ /cto/business-units/haulage/kafka           (6 event type streams)
```

### CTO → Infrastructure by Unit Flow

```
CTO Dashboard → Business Units → Infrastructure Summary
       │
       ▼
/cto/business-units/infrastructure
       │
       ├─→ Kafka clusters (3 clusters across units)
       ├─→ MongoDB clusters (4 clusters)
       ├─→ PostgreSQL instances (2)
       ├─→ Redis clusters (5)
       ├─→ RabbitMQ cluster (1 - Air Freight)
       ├─→ Elasticsearch cluster (1 - E-Commerce)
       └─→ Total: 16 infrastructure components
```

---

## 16. Business Unit Route Map

### Complete Route Table

| Route | Accessible By | Content |
|---|---|---|
| `/{role}/business-units` | CEO, CFO, COO, CTO | 8 business unit cards (role-filtered metrics) |
| `/{role}/business-units/{unit}` | CEO, CFO, COO, CTO | Unit overview (role-filtered) |
| `/{role}/business-units/{unit}/revenue` | CEO, CFO | Revenue breakdown, trend, P&L |
| `/{role}/business-units/{unit}/operations` | CEO, COO | Operational KPIs, live data |
| `/{role}/business-units/{unit}/services` | CEO, CFO, COO, CTO | Service health grid for unit |
| `/{role}/business-units/{unit}/reports` | CEO, CFO, COO, CTO | Unit-specific reports |
| `/{role}/business-units/{unit}/approvals` | CEO, CFO, COO, CTO | Pending approvals from this unit |
| `/cfo/business-units/financial-summary` | CFO | Cross-unit P&L, revenue mix, settlements |
| `/coo/business-units/operations-overview` | COO | Cross-unit operations map, SLA, incidents |
| `/cto/business-units/service-health` | CTO | Full 272-service health grid |
| `/cto/business-units/infrastructure` | CTO | Infrastructure summary (16 components) |
| `/{role}/business-units/comparison` | CEO, CFO, COO, CTO | Cross-unit comparison report |

### Business Unit Slug Mapping

| Business Unit | Slug | Services |
|---|---|---|
| Courier Services | `courier` | 20 |
| E-Commerce Platform | `ecommerce` | 76 |
| Warehousing & Storage | `warehousing` | 43 |
| Air Freight | `air-freight` | 15 |
| Ocean Shipping | `ocean-shipping` | 18 |
| Haulage & Road Freight | `haulage` | 62 |
| Procurement | `procurement` | 27 |
| Admin & Partner Oversight | `admin-core` | 11 |

### Cross-Reference: Departments vs Business Units

| Navigation | Route Prefix | Content Source |
|---|---|---|
| Departments | `/{role}/departments/{dept}` | Management-Domain HQ services |
| Business Units | `/{role}/business-units/{unit}` | shared-business-infrastructure services |
| Combined | `/{role}/overview` | Both departments + business units aggregated |

---

**Document End: Page Flows & Navigation v3.0**
