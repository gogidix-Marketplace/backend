# HUMAN RESOURCE DEPARTMENT - UI FLOW DOCUMENTATION

**Domain:** Human Resource (Management Domain)
**Application:** HR Department Web Dashboard
**Version:** 1.0
**Date:** 2026-02-23
**Path:** `Human-resource/Frontends/Web/hr-web-dashboard/`

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
┌─────────────────────────────────────────────────────────────────────┐
│                     HR DEPARTMENT USERS                          │
├─────────────────────────────────────────────────────────────────────┤
│                                                                      │
│   ┌─────────┐    ┌─────────┐    ┌─────────┐    ┌─────────┐         │
│   │  CHRO   │    │ HR Dir  │    │Recruiter│    │ Payroll │         │
│   │         │    │         │    │         │    │Specialist│       │
│   │Chief    │    │Head of  │    │Talent   │    │Comp &   │         │
│   │Human    │    │Department│ │Acquisition│ │Benefits │         │
│   │Resource │    │Manager  │    │         │    │         │         │
│   └─────────┘    └─────────┘    └─────────┘    └─────────┘         │
│         │              │              │              │              │
│         └──────────────┴──────────────┴──────────────┘              │
│                            │                                         │
│                    ┌───────▼───────┐                                │
│                    │   HR Web      │                                │
│                    │   Dashboard   │                                │
│                    └───────────────┘                                │
└─────────────────────────────────────────────────────────────────────┘
```

### 1.2 Dashboard Structure

```
HR Department Web Dashboard
│
├── Overview (Workforce Analytics)
│   ├── Headcount Summary
│   ├── Growth & Turnover
│   ├── Diversity Metrics
│   ├── Pending Approvals
│   └── Recent Activities
│
├── Recruitment
│   ├── Open Positions
│   ├── Candidate Pipeline
│   ├── Job Requisitions
│   ├── Time-to-Hire Analytics
│   └── Cost-per-Hire Tracking
│
├── Onboarding
│   ├── New Hires Dashboard
│   ├── Onboarding Progress
│   ├── Task Checklists
│   └── First Week Retention
│
├── Performance
│   ├── Review Cycles
│   ├── Ratings Distribution
│   ├── Improvement Plans
│   ├── 360-Degree Feedback
│   └── Goals & OKRs
│
├── Payroll
│   ├── Salary Overview
│   ├── Multi-Country Payroll
│   ├── Bonus & Commissions
│   ├── Payroll Run Status
│   └── Compensation Analysis
│
├── Time & Attendance
│   ├── Leave Management
│   ├── Timesheet Review
│   ├── Overtime Tracking
│   ├── Attendance Dashboard
│   └── Shift Scheduling
│
├── Training
│   ├── Training Programs
│   ├── Certification Tracking
│   ├── Skills Gap Analysis
│   ├── Learning Progress
│   └── Training Budget
│
├── Engagement
│   ├── Survey Results
│   ├── eNPS Tracking
│   ├── Feedback Analysis
│   ├── Recognition Programs
│   └── Pulse Checks
│
├── Compliance
│   ├── Labor Law Monitoring
│   ├── Work Permit Tracking
│   ├── Visa Management
│   ├── Policy Compliance
│   └── Audit Reports
│
└── Approvals
    ├── Salary Changes
    ├── Promotions
    ├── Leave Requests
    ├── Expense Approvals
    └── Approval History
```

### 1.3 Data Flow Context

```
┌─────────────────────────────────────────────────────────────────────┐
│                    HR DATA FLOW CONTEXT                            │
├─────────────────────────────────────────────────────────────────────┤
│                                                                      │
│   ┌─────────────────────┐        ┌─────────────────────┐           │
│   │   Business Domain   │        │   HR Department     │           │
│   │                     │        │   Dashboard          │           │
│   │  Country-HR-        │  ───▶  │  (This Application) │           │
│   │  Dashboards         │  Agg   │                      │           │
│   │  (Nigeria, Kenya,   │  Data  │  ┌─────────────────────┐           │
│   │   S.Africa, etc.)   │        │  │ Reports to:         │           │
│   └─────────────────────┘        │  │ • COO Dashboard     │           │
│          ▲                       │  └─────────────────────┘           │
│          │                       │                                      │
│    Local HR data                                                   │
│    (Headcount, Payroll,            ┌─────────────────────┐         │
│     Recruitment, etc.)             │  Foundation Domain │         │
│                                    │  (Auth, Config,     │         │
│                                    │   Notifications)    │         │
│                                    └─────────────────────┘         │
└─────────────────────────────────────────────────────────────────────┘
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
┌─────────────────────────────────────────────────────────────┐
│                    CHECK AUTHENTICATION                     │
│  ┌──────────────────────────────────────────────────────┐  │
│  │  Is user already logged in?                          │  │
│  │  (Check: localStorage.authToken, cookie)             │  │
│  └──────────────────────────────────────────────────────┘  │
└─────┬──────────────────────────────────────────────────┬─────┘
      │ YES                                              │ NO
      ▼                                                  ▼
┌───────────────────┐                           ┌──────────────────┐
│  RESTORE SESSION  │                           │  LOGIN PAGE      │
│  • Load user data │                           │                  │
│  • Load role      │                           │  [See Section 3] │
│  • Load tenant    │                           └──────────────────┘
│  • Navigate to    │                                    │
│    dashboard      │                                    ▼
└─────────┬─────────┘                    ┌──────────────────────────────┐
          │                              │     HR LOGIN SCREEN          │
          ▼                              └──────────────────────────────┘
┌──────────────────────────────────────────────────────────────────────┐
│                        HR DASHBOARD                                │
│  Load HR-specific data based on user role:                         │
│  • CHRO → Global workforce view                                     │
│  • HR Director → Department/region view                            │
│  • Recruiter → Recruitment pipeline focus                          │
│  • Payroll Specialist → Compensation & payroll focus               │
└──────────────────────────────────────────────────────────────────────┘
```

### 2.2 Role-Based Dashboard Selection

```
┌─────────────────────────────────────────────────────────────────────┐
│                   HR ROLE SELECTION                                │
├─────────────────────────────────────────────────────────────────────┤
│                                                                      │
│  User has multiple HR roles?                                       │
│  ┌─────────────────────────────────────────────────────────────┐   │
│  │  Select your primary view for this session:                  │   │
│  │                                                               │   │
│  │  ┌───────────┐  ┌───────────┐  ┌───────────┐  ┌───────────┐│   │
│  │  │    👤     │  │    🎯     │  │    💰     │  │    📊     ││   │
│  │  │           │  │           │  │           │  │           ││   │
│  │  │ Workforce │  │Recruitment│  │ Payroll   │  │Performance││   │
│  │  │ Overview  │  │ Pipeline  │  │ & Benefits│  │ Management││   │
│  │  │           │  │           │  │           │  │           ││   │
│  │  └───────────┘  └───────────┘  └───────────┘  └───────────┘│   │
│  │                                                               │   │
│  │  [☐ Remember my preference]                                   │   │
│  └─────────────────────────────────────────────────────────────┘   │
│                                                                      │
└─────────────────────────────────────────────────────────────────────┘
```

---

## 3. AUTHENTICATION FLOWS

### 3.1 Login Flow

```
┌─────────────────────────────────────────────────────────────────────┐
│                          HR LOGIN SCREEN                           │
├─────────────────────────────────────────────────────────────────────┤
│                                                                      │
│                    ┌─────────────────┐                              │
│                    │   GOGIDIX       │                              │
│                    │   HR Platform   │                              │
│                    └─────────────────┘                              │
│                                                                      │
│                    ┌─────────────────────────────┐                   │
│                    │  Email / Username           │                   │
│                    └─────────────────────────────┘                   │
│                                                                      │
│                    ┌─────────────────────────────┐                   │
│                    │  Password        [👁️]      │                   │
│                    └─────────────────────────────┘                   │
│                                                                      │
│                    [ ] Remember me                                   │
│                    Forgot password?                                  │
│                                                                      │
│                    ┌─────────────────────────────┐                   │
│                    │     SIGN IN                │                   │
│                    └─────────────────────────────┘                   │
│                                                                      │
│                    OR                                               │
│                                                                      │
│                    [ SSO with Microsoft ]                            │
│                    [ SSO with Google ]                               │
│                                                                      │
└─────────────────────────────────────────────────────────────────────┘
      │
      │ User enters credentials and clicks Sign In
      ▼
┌─────────────────────────────────────────────────────────────────────┐
│                      VALIDATE CREDENTIALS                           │
│  ┌──────────────────────────────────────────────────────────────┐  │
│  │  POST /api/v1/hr/auth/login                                  │  │
│  │  Request: { email, password, department: "HR" }             │  │
│  │  Response: { token, user, hrRole, permissions, tenant }      │  │
│  └──────────────────────────────────────────────────────────────┘  │
└────┬──────────────────────────────────────────────────────────┬─────┘
     │ VALID                                                     │ INVALID
     ▼                                                           ▼
┌─────────────────┐                                     ┌─────────────────┐
│  Store Token    │                                     │  Show Error:    │
│  Load HR Data   │                                     │  "Invalid       │
│  Navigate to    │                                     │   credentials"  │
│  HR Dashboard   │                                     │  Allow retry    │
└─────────────────┘                                     └─────────────────┘
```

### 3.2 Logout Flow

```
┌─────────────────────────────────────────────────────────────────────┐
│                       LOGOUT INITIATED                              │
│  Trigger: User clicks logout from user menu                         │
└─────┬───────────────────────────────────────────────────────────────┘
      │
      ▼
┌─────────────────────────────────────────────────────────────────────┐
│                      CONFIRM LOGOUT                                │
│  ┌──────────────────────────────────────────────────────────────┐  │
│  │  Are you sure you want to logout?                            │  │
│  │  Unsaved changes may be lost.                                │  │
│  │                                                               │  │
│  │  [Cancel]  [Logout]                                           │  │
│  └──────────────────────────────────────────────────────────────┘  │
└─────┬───────────────────────────────────────────────────────────────┘
      │                          │
      │ Cancel                   │ Confirm Logout
      ▼                          ▼
┌─────────────────┐      ┌─────────────────────────────────────────────┐
│  Return to      │      │              CLEAR SESSION                   │
│  Dashboard      │      │  • Call POST /api/v1/hr/auth/logout         │
│                 │      │  • Clear localStorage (token, user, tenant)  │
│                 │      │  • Clear sessionStorage                     │
│                 │      │  • Clear cookies                            │
│                 │      │  • Close WebSocket connections              │
│                 │      │  • Navigate to Login screen                 │
│                 │      └─────────────────────────────────────────────┘
└─────────────────┘
```

---

## 4. DASHBOARD NAVIGATION FLOWS

### 4.1 HR Overview Navigation Flow

```
                    ┌─────────────────┐
                    │  HR DASHBOARD  │
                    │    (Home)      │
                    └────────┬────────┘
                             │
        ┌────────────────────┼────────────────────┐
        │                    │                    │
        ▼                    ▼                    ▼
┌───────────────┐    ┌───────────────┐    ┌───────────────┐
│ Sidebar Nav   │    │ Top Nav       │    │ Main Content  │
│               │    │               │    │               │
│ • Overview    │    │ Country/Reg   │    │ Workforce KPI │
│ • Recruitment │    │ Location Ind  │    │ Cards         │
│ • Onboarding  │    │ Notif Bell    │    │ Charts        │
│ • Performance │    │ User Menu     │    │ Pending       │
│ • Payroll     │    │ Data Refresh  │    │ Approvals     │
│ • Time & Att  │    │ Connection    │    │ Quick Actions │
│ • Training    │    │               │    │               │
│ • Engagement  │    │               │    │               │
│ • Compliance  │    │               │    │               │
│ • Approvals   │    │               │    │               │
│ • Settings    │    │               │    │               │
└───────────────┘    └───────────────┘    └───────────────┘
        │                    │                    │
        │                    │                    │
        └────────────────────┼────────────────────┘
                             │
                             ▼
        ┌──────────────────────────────────────────────────────┐
        │              NAVIGATION OPTIONS                       │
        ├──────────────────────────────────────────────────────┤
        │  1. Click Sidebar Item → Navigate to Section        │
        │  2. Click KPI Card → Drill down to detail view       │
        │  3. Click Employee → Open employee profile           │
        │  4. Click Pending Approval → Open approval form      │
        │  5. Click Country Selector → Switch region view      │
        │  6. Click Quick Action → Initiate action             │
        └──────────────────────────────────────────────────────┘
```

### 4.2 KPI Drill-Down Flow (Workforce Overview)

```
┌─────────────────────────────────────────────────────────────────────┐
│                  HR DASHBOARD - WORKFORCE OVERVIEW                  │
│  ┌─────────┐ ┌─────────┐ ┌─────────┐ ┌─────────┐ ┌─────────┐      │
│  │Headcount│ │ Growth  │ │Turnover │ │Diversity│ │Engage-  │      │
│  │ 2,847   │ │  +5.2%  │ │  8.5%   │ │  62%   │ │ ment 72%│      │
│  │  ▲ +42  │ │  ▲ 1.1% │ │  ▼ 0.3% │ │  ▲ 3%  │ │  ▲ 2%  │      │
│  └─────────┘ └─────────┘ └─────────┘ └─────────┘ └─────────┘      │
│      ↑                                                                  │
│      │ User clicks Headcount KPI                                        │
│      ▼                                                                  │
└─────────────────────────────────────────────────────────────────────┘
                            │
                            ▼
┌─────────────────────────────────────────────────────────────────────┐
│                      HEADCOUNT DETAIL MODAL                         │
├─────────────────────────────────────────────────────────────────────┤
│  ┌─────────────────────────────────────────────────────────────┐   │
│  │  👥 HEADCOUNT DETAILS                                       │   │
│  │                                                               │   │
│  │  Total Headcount:        2,847                                │   │
│  │  Growth this month:      +42 employees                        │   │
│  │  Growth rate:           +1.5%                                 │   │
│  │  Period:                February 2026                         │   │
│  │  Status:                ● ABOVE TARGET                        │   │
│  │                                                               │   │
│  │  ┌─────────────────────────────────────────────────────┐   │   │
│  │  │  Headcount Trend (Last 30 Days)                     │   │   │
│  │  │  [Line Chart showing upward trend]                  │   │   │
│  │  └─────────────────────────────────────────────────────┘   │   │
│  │                                                               │   │
│  │  ┌───────────────┐ ┌───────────────┐ ┌───────────────┐     │   │
│  │  │ By Department │ │ By Region     │ │ By Country    │     │   │
│  │  ├───────────────┤ ├───────────────┤ ├───────────────┤     │   │
│  │  │ Engineering  │ │ Europe: 45%   │ │ Ireland: 18%  │     │   │
│  │  │ 847          │ │ Africa: 32%   │ │ Nigeria: 22% │     │   │
│  │  │ Sales: 725   │ │ Americas: 18% │ │ S.Africa: 15% │     │   │
│  │  │ Operations: 523│ │ Asia: 5%      │ │ Kenya: 8%    │     │   │
│  │  │ Support: 412  │ │               │ │ Others: 37%   │     │   │
│  │  │ Admin: 340    │ │               │ │               │     │   │
│  │  └───────────────┘ └───────────────┘ └───────────────┘     │   │
│  │                                                               │   │
│  │  New Hires (This Month):  59                                 │   │
│  │  Departures:                 17                                 │   │
│  │  Net Change:                +42                                 │   │
│  │                                                               │   │
│  │  [View All Employees] [Export Data] [View Full Report]        │   │
│  └─────────────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────────────┘
```

### 4.3 Country/Region Navigation Flow

```
┌─────────────────────────────────────────────────────────────────────┐
│              HR DASHBOARD - COUNTRY/REGION SELECTION                │
│  ┌───────────────────────────────────────────────────────────────┐ │
│  │  Location: [Global (All Countries) ▼]                         │ │
│  └───────────────────────────────────────────────────────────────┘ │
│                              ↑                                        │
│                              │ User clicks dropdown                   │
│                              ▼                                        │
└─────────────────────────────────────────────────────────────────────┘
                                        │
┌─────────────────────────────────────────────────────────────────────┐
│                      COUNTRY SELECTOR                               │
│  ┌─────────────────────────────────────────────────────────────┐   │
│  │  Select Region/Country:                                      │   │
│  │                                                               │   │
│  │  ┌─────────────────────────────────────────────────────────┐ │   │
│  │  │ 🌍 Global (All Countries)         2,847 employees       │ │   │
│  │  ├─────────────────────────────────────────────────────────┤ │   │
│  │  │ 🇪🇺 Europe                                                │ │   │
│  │  │   🇮🇪 Ireland                          512 employees      │ │   │
│  │  │   🇬🇧 UK                               245 employees      │ │   │
│  │  ├─────────────────────────────────────────────────────────┤ │   │
│  │  │ 🌍 Africa                                                │ │   │
│  │  │   🇳🇬 Nigeria                          626 employees      │ │   │
│  │  │   🇿🇦 South Africa                     228 employees      │ │ │   │
│  │  │   🇰🇪 Kenya                            191 employees      │ │   │
│  │  ├─────────────────────────────────────────────────────────┤ │   │
│  │  │ 🌍 Americas                                              │ │   │
│  │  │   🇺🇸 USA                              513 employees      │ │   │
│  │  │   🇨🇦 Canada                           245 employees      │ │   │
│  │  └─────────────────────────────────────────────────────────┘ │   │
│  └─────────────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────────────┘
                              │
                              │ User selects Nigeria
                              ▼
┌─────────────────────────────────────────────────────────────────────┐
│              HR DASHBOARD - NIGERIA VIEW                            │
│  ┌───────────────────────────────────────────────────────────────┐ │
│  │  Location: [Nigeria ▼]                                       │ │
│  └───────────────────────────────────────────────────────────────┘ │
│                                                                       │
│  ┌─────────────────────────────────────────────────────────────┐    │
│  │  NIGERIA WORKFORCE OVERVIEW                      [Full Report]│    │
│  │  ┌────────┐ ┌────────┐ ┌────────┐ ┌────────┐ ┌────────┐  │    │
│  │  │Headcount│ │ Growth │ │Turnover│ │Diversity│ │Engage- │  │    │
│  │  │  626   │ │ +6.8%  │ │ 10.2%  │ │  58%   │ │ ment 68%│  │    │
│  │  │  ▲ +18 │ │ ▲ 2.3% │ │ ▲ 1.1% │ │ ▲ 5%   │ │ ▲ 3%   │  │    │
│  │  └────────┘ └────────┘ └────────┘ └────────┘ └────────┘  │    │
│  └─────────────────────────────────────────────────────────────┘    │
│                                                                       │
│  Nigeria-specific KPIs, charts, and data displayed...              │
└─────────────────────────────────────────────────────────────────────┘
```

---

## 5. FEATURE-SPECIFIC FLOWS

### 5.1 Recruitment Pipeline Flow

```
┌─────────────────────────────────────────────────────────────────────┐
│                HR DASHBOARD - RECRUITMENT PAGE                      │
│  ┌─────────────────────────────────────────────────────────────┐   │
│  │  RECRUITMENT OVERVIEW                                        │   │
│  │  ┌────────┐ ┌────────┐ ┌────────┐ ┌────────┐ ┌────────┐  │   │
│  │  │  Open  │ │Pipeline│ │Time to │ │Cost per│ │Offer   │  │   │
│  │  │ Positions│ │Candidates│ │Hire   │ │Hire   │ │Accept  │  │   │
│  │  │   47   │ │   312  │ │  28d   │ │  €4,200│ │  85%   │  │   │
│  │  │  ▲ +5  │ │  ▲ +42 │ │ ▼ -3d  │ │ ▼ -8%  │ │ ▲ 5%   │  │   │
│  │  └────────┘ └────────┘ └────────┘ └────────┘ └────────┘  │   │
│  └─────────────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────────────┘
                              ↑
                              │ User clicks "Pipeline Candidates"
                              ▼
┌─────────────────────────────────────────────────────────────────────┐
│                  CANDIDATE PIPELINE DETAIL                          │
├─────────────────────────────────────────────────────────────────────┤
│  ┌─────────────────────────────────────────────────────────────┐   │
│  │  CANDIDATE PIPELINE                             [Add Stage]  │   │
│  │                                                               │   │
│  │  Applied ──▶ Screening ──▶ Interview ──▶ Offer ──▶ Hired   │   │
│  │   [89]       [65]          [78]         [42]      [38]       │   │
│  │    ▼+12       ▼-8          ▼+5         ▼-3       ▼+2        │   │
│  │                                                               │   │
│  │  ┌─────────────────────────────────────────────────────┐    │   │
│  │  │ Filter: [All Stages ▼] [All Positions ▼] [Search]  │    │   │
│  │  └─────────────────────────────────────────────────────┘    │   │
│  │                                                               │   │
│  │  ┌─────────────────────────────────────────────────────────┐ │   │
│  │  │ 👤 Sarah Johnson           [Applied]  2 days ago       │ │   │
│  │  │    Senior Developer - Nigeria                        │ │   │
│  │  │    [View Profile] [Move to Screening] [Reject]        │ │   │
│  │  ├─────────────────────────────────────────────────────────┤ │   │
│  │  │ 👤 Michael Chen            [Interview] 5 days ago      │ │   │
│  │  │    Product Manager - Ireland                         │ │   │
│  │  │    [View Profile] [Schedule Interview] [Send Offer]   │ │   │
│  │  ├─────────────────────────────────────────────────────────┤ │   │
│  │  │ 👤 Amanda Williams         [Offer] 1 day ago           │ │   │
│  │  │    HR Director - Kenya                                │ │   │
│  │  │    [View Profile] [Follow Up] [Withdraw Offer]        │ │   │
│  │  └─────────────────────────────────────────────────────────┘ │   │
│  │                                                               │   │
│  │  [Load More Candidates]                                      │   │
│  └─────────────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────────────┘
```

### 5.2 Performance Review Flow

```
┌─────────────────────────────────────────────────────────────────────┐
│              HR DASHBOARD - PERFORMANCE MANAGEMENT                  │
│  ┌─────────────────────────────────────────────────────────────┐   │
│  │  PERFORMANCE REVIEW CYCLE: Q1 2026              [Start New]  │   │
│  │  Progress: ████████░░ 78% complete                           │   │
│  │  Due: March 31, 2026                                         │   │
│  └─────────────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────────────┘
                              ↑
                              │ User clicks "Start New Review"
                              ▼
┌─────────────────────────────────────────────────────────────────────┐
│                  CREATE PERFORMANCE REVIEW                          │
├─────────────────────────────────────────────────────────────────────┤
│  ┌─────────────────────────────────────────────────────────────┐   │
│  │  📝 NEW PERFORMANCE REVIEW                                   │   │
│  │                                                               │   │
│  │  Review Type: [Annual Review ▼]                               │   │
│  │  Review Cycle: [Q1 2026 ▼]                                   │   │
│  │                                                               │   │
│  │  Select Employees:                                           │   │
│  │  ┌─────────────────────────────────────────────────────────┐ │   │
│  │  │ ☑ John Smith - Engineering - Ireland                   │ │   │
│  │  │ ☑ Jane Doe - Sales - Nigeria                            │ │   │
│  │  │ ☑ Mike Johnson - Operations - Kenya                     │ │   │
│  │  │ [+ Add more employees]                                  │ │   │
│  │  └─────────────────────────────────────────────────────────┘ │   │
│  │                                                               │   │
│  │  Review Template: [Standard Performance Review ▼]            │   │
│  │                                                               │   │
│  │  Self-Assessment Due: [March 15, 2026]                       │   │
│  │  Manager Review Due: [March 31, 2026]                        │   │
│  │                                                               │   │
│  │  Email notifications to: ☑ Employees ☑ Managers             │   │
│  │                                                               │   │
│  │  [Cancel] [Create as Draft] [Start Review Cycle]              │   │
│  └─────────────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────────────┘
                              │
                              │ Click "Start Review Cycle"
                              ▼
┌─────────────────────────────────────────────────────────────────────┐
│                  REVIEW CYCLE INITIATED                             │
│  ┌─────────────────────────────────────────────────────────────┐   │
│  │  ✓ Review Cycle Started                                      │   │
│  │                                                               │   │
│  │  3 employees have been notified to begin self-assessment.   │   │
│  │  Managers will be notified after self-assessments complete.  │   │
│  │                                                               │   │
│  │  [View Progress Dashboard] [Notify Additional Managers]      │   │
│  │  [Close]                                                     │   │
│  └─────────────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────────────┘
```

### 5.3 Leave Approval Flow

```
┌─────────────────────────────────────────────────────────────────────┐
│              HR DASHBOARD - PENDING APPROVALS                       │
│  ┌─────────────────────────────────────────────────────────────┐   │
│  │  PENDING APPROVALS (12)                          [View All]  │   │
│  ─────────────────────────                                   │   │
│  │  • Leave Request: John Smith - 5 days Annual     [Review]   │   │
│  │  • Salary Change: Jane Doe - Promotion         [Review]   │   │
│  │  • Bonus Request: Mike Johnson - Q1 Target    [Review]   │   │
│  │  • Timesheet: Operations Team - Week 8        [Review]   │   │
│  └─────────────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────────────┘
                              ↑
                              │ User clicks [Review] on Leave Request
                              ▼
┌─────────────────────────────────────────────────────────────────────┐
│                    LEAVE REQUEST DETAIL                            │
├─────────────────────────────────────────────────────────────────────┤
│  ┌─────────────────────────────────────────────────────────────┐   │
│  │  🏖️ LEAVE REQUEST                                          │   │
│  │                                                               │   │
│  │  Request ID: LR-2026-0223-089                                │   │
│  │  Type: Annual Leave                                         │   │
│  │  Priority: Normal                                            │   │
│  │  Submitted: Feb 22, 2026 | Due: Feb 25, 2026                  │   │
│  │                                                               │   │
│  │  ┌────────┐                                                  │   │
│  │  │  JS    │  John Smith                                       │   │
│  │  └────────┘  Senior Software Engineer                          │   │
│  │               Engineering - Ireland                          │   │
│  │               Manager: Sarah Johnson                          │   │
│  │                                                               │   │
│  │  Leave Details:                                              │   │
│  │  ┌─────────────────────────────────────────────────────────┐ │   │
│  │  │  Start Date:     March 10, 2026                         │ │   │
│  │  │  End Date:       March 14, 2026                         │ │   │
│  │  │  Duration:       5 working days                         │ │   │
│  │  │  Return to Work: March 15, 2026                         │ │   │
│  │  │  Reason:         Family vacation                         │ │   │
│  │  │  Balance:        18 days remaining / 25 annual          │ │   │
│  │  └─────────────────────────────────────────────────────────┘ │   │
│  │                                                               │   │
│  │  Coverage Plan:                                              │   │
│  │  • Tasks delegated to: Michael Chen                          │   │
│  │  • On-call backup: Maria Rodriguez                           │   │
│  │                                                               │   │
│  │  Manager Recommendation:                                      │   │
│  │  ┌─────────────────────────────────────────────────────────┐ │   │
│  │  │ "Approved - no critical deliverables during period"     │ │   │
│  │  │ - Sarah Johnson                                          │ │   │
│  │  └─────────────────────────────────────────────────────────┘ │   │
│  │                                                               │   │
│  │  Add your comments (optional):                               │   │
│  │  ┌─────────────────────────────────────────────────────────┐ │   │
│  │  │                                                           │ │   │
│  │  └─────────────────────────────────────────────────────────┘ │   │
│  │                                                               │   │
│  │  [Reject] [Request Changes] [Approve]                         │   │
│  └─────────────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────────────┘
                              │
                              │ User clicks [Approve]
                              ▼
┌─────────────────────────────────────────────────────────────────────┐
│                      APPROVAL CONFIRMATION                          │
│  ┌─────────────────────────────────────────────────────────────┐   │
│  │  ✓ Leave Approved                                            │   │
│  │                                                               │   │
│  │  John Smith's leave request for March 10-14 has been         │   │
│  │  approved. He will be notified via email.                   │   │
│  │                                                               │   │
│  │  Calendar updated | Payroll notified | Coverage confirmed    │   │
│  │                                                               │   │
│  │  [View Calendar] [Close]                                      │   │
│  └─────────────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────────────┘
```

### 5.4 Payroll Processing Flow

```
┌─────────────────────────────────────────────────────────────────────┐
│              HR DASHBOARD - PAYROLL PAGE                            │
│  ┌─────────────────────────────────────────────────────────────┐   │
│  │  PAYROLL OVERVIEW                              [Run Payroll] │   │
│  │  ┌────────┐ ┌────────┐ ┌────────┐ ┌────────┐ ┌────────┐  │   │
│  │  │Total   │ │Gross   │ │Net     │ │Bonus   │ │Next    │  │   │
│  │  │Payroll │ │Pay     │ │Pay     │ │Paid    │ │Run     │  │   │
│  │  │€1.2M   │ │€1.5M   │ │€950K   │ │€85K   │ │5 days  │  │   │
│  │  │ Feb    │ │ Feb    │ │ Feb    │ │ YTD   │ │        │  │   │
│  └────────┘ └────────┘ └────────┘ └────────┘ └────────┘  │   │
│  └─────────────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────────────┘
                              ↑
                              │ User clicks "Run Payroll"
                              ▼
┌─────────────────────────────────────────────────────────────────────┐
│                    PAYROLL RUN WIZARD                              │
├─────────────────────────────────────────────────────────────────────┤
│  ┌─────────────────────────────────────────────────────────────┐   │
│  │  💰 PAYROLL RUN - FEBRUARY 2026                              │   │
│  │                                                               │   │
│  │  Step 1 of 5: Select Payroll Period                          │   │
│  │  ┌─────────────────────────────────────────────────────────┐ │   │
│  │  │  Pay Period: [February 1-28, 2026 ▼]                    │ │   │
│  │  │  Pay Date: [February 28, 2026 ▼]                        │ │   │
│  │  │                                                           │ │   │
│  │  │  Include:                                                │ │   │
│  │  │  ☑ All active employees                                  │ │   │
│  │  │  ☑ New hires (prorated)                                  │ │   │
│  │  │  ☑ Terminations (final pay)                              │ │ │   │
│  │  │  ☑ Bonus payments                                        │ │   │
│  │  │  ☑ Commission adjustments                                │ │ │   │
│  │  │                                                           │ │   │
│  │  │  Countries:                                              │ │   │
│  │  │  ☑ Ireland (€)  ☑ Nigeria (₦)  ☑ Kenya (KSh)           │ │   │
│  │  │  ☑ S. Africa (R)  ☑ USA ($)                             │ │   │
│  │  └─────────────────────────────────────────────────────────┘ │   │
│  │                                                               │   │
│  │  [Cancel]                                    [Next: Review ▶]│   │
│  └─────────────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────────────┘
                              │
                              │ Click Next
                              ▼
┌─────────────────────────────────────────────────────────────────────┐
│                    PAYROLL RUN - REVIEW                             │
├─────────────────────────────────────────────────────────────────────┤
│  ┌─────────────────────────────────────────────────────────────┐   │
│  │  Step 2 of 5: Review Payroll Data                            │   │
│  │                                                               │   │
│  │  Summary:                                                     │   │
│  │  • Total Employees: 2,847                                     │   │
│  │  • New Hires: 12 (prorated first payroll)                     │   │
│  │  • Terminations: 3 (final pay)                                │   │
│  │  • Changes: 45 (salary, bonus, tax)                          │   │
│  │                                                               │   │
│  │  ┌─────────────────────────────────────────────────────────┐ │   │
│  │  │  Items Requiring Attention:                              │ │   │
│  │  │  ⚠️ 3 employees missing tax details                      │ │   │
│  │  │  ⚠️ 5 employees with insufficient leave balance          │ │   │
│  │  │  ⚠️ 2 salary changes awaiting verification              │ │   │
│  │  │  [Review Issues]                                         │ │   │
│  │  └─────────────────────────────────────────────────────────┘ │   │
│  │                                                               │   │
│  │  Total Gross Pay: €1,523,450                                  │   │
│  │  Total Deductions: €573,283                                   │   │
│  │  Total Net Pay: €950,167                                      │   │
│  │                                                               │   │
│  │  [◀ Back]  [Resolve Issues]            [Next: Confirm ▶]     │   │
│  └─────────────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────────────┘
```

### 5.5 Training Program Assignment Flow

```
┌─────────────────────────────────────────────────────────────────────┐
│              HR DASHBOARD - TRAINING PAGE                            │
│  ┌─────────────────────────────────────────────────────────────┐   │
│  │  TRAINING OVERVIEW                               [Add Program]│   │
│  │  ┌────────┐ ┌────────┐ ┌────────┐ ┌────────┐ ┌────────┐  │   │
│  │  │Active  │ │Enrolled│ │Complete│ │Skills  │ │Training│  │   │
│  │  │Programs│ │Employees│ │Rate   │ │Gap    │ │Budget  │  │   │
│  │  │   18   │ │ 1,245 │ │  87%   │ │  15%   │ │€45K   │  │   │
│  │  │   +3   │ │  +85  │ │ ▲ 5%   │ │ ▼ 2%   │ │€12K rem│  │   │
│  │  └────────┘ └────────┘ └────────┘ └────────┘ └────────┘  │   │
│  └─────────────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────────────┘
                              ↑
                              │ User clicks "Add Program"
                              ▼
┌─────────────────────────────────────────────────────────────────────┐
│                  CREATE TRAINING PROGRAM                            │
├─────────────────────────────────────────────────────────────────────┤
│  ┌─────────────────────────────────────────────────────────────┐   │
│  │  📚 NEW TRAINING PROGRAM                                     │   │
│  │                                                               │   │
│  │  Program Name: [________________________________]           │   │
│  │                                                               │   │
│  │  Program Type: [Technical Training ▼]                          │   │
│  │  ┌─────────────────────────────────────────────────────────┐ │   │
│  │  │  • Technical Training                                    │ │   │
│  │  │  • Leadership Development                              │ │   │
│  │  │  • Compliance Training                                 │ │   │
│  │  │  • Onboarding                                        │ │   │
│  │  │  • Skills Workshop                                    │ │   │
│  │  └─────────────────────────────────────────────────────────┘ │   │
│  │                                                               │   │
│  │  Description:                                                 │   │
│  │  ┌─────────────────────────────────────────────────────────┐ │   │
│  │  │                                                           │ │   │
│  │  └─────────────────────────────────────────────────────────┘ │   │
│  │                                                               │   │
│  │  Duration: [4 weeks ▼]  Estimated Cost: [€15,000]             │   │
│ │                                                               │   │
│  │  Assign To:                                                  │   │
│  │  ☑ Engineering  ☑ New Hires  ☐ All Employees                │   │
│  │                                                               │   │
│  │  Target Skills (select all that apply):                       │   │
│  │  ☐ Python  ☑ Cloud Services  ☐ Leadership  ☑ Security      │   │
│  │                                                               │   │
│  │  Start Date: [March 1, 2026 ▼]                                │   │
│  │                                                               │   │
│  │  [Cancel] [Save as Draft] [Publish & Enroll]                  │   │
│  └─────────────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────────────┘
```

---

## 6. EXIT FLOWS

### 6.1 Session Timeout Flow

```
┌─────────────────────────────────────────────────────────────────────┐
│                    SESSION TIMEOUT WARNING                         │
│  (Auto-triggered after 25 minutes of inactivity)                  │
│  ┌─────────────────────────────────────────────────────────────┐   │
│  │  ⏰ Session Expiring Soon                                   │   │
│  │                                                               │   │
│  │  Your session will expire in 5 minutes.                     │   │
│  │  Unsaved changes may be lost.                               │   │
│  │                                                               │   │
│  │  [Stay Signed In] [Sign Out Now]                            │   │
│  └─────────────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────────────┘
         │                                    │
         │ No action (5 min)                 │ Click "Stay Signed In"
         ▼                                    ▼
┌──────────────────────────┐      ┌────────────────────────────┐
│   SESSION EXPIRED        │      │   SESSION EXTENDED         │
│  ┌────────────────────┐  │      │  ┌────────────────────┐    │
│  │  Your session has  │  │      │  │ Session extended.  │    │
│  │  expired.          │  │      │  │ You can continue.  │    │
│  │                    │  │      │  └────────────────────┘    │
│  │  [Sign In Again]   │  │      └────────────────────────────┘    │
│  └────────────────────┘  │
└──────────────────────────┘
```

### 6.2 Browser Close Flow

```
┌─────────────────────────────────────────────────────────────────────┐
│                    USER CLOSES BROWSER TAB                          │
│  (Detected by beforeunload event)                                  │
└─────┬───────────────────────────────────────────────────────────────┘
      │
      ▼
┌─────────────────────────────────────────────────────────────────────┐
│                      UNSAVED CHANGES CHECK                         │
│  ┌──────────────────────────────────────────────────────┐  │
│  │  Are there unsaved changes?                                 │  │
│  └─────┬───────────────────────────────────────────┬─────┘
        │ YES                                               │ NO
        ▼                                                   ▼
┌───────────────────────┐                   ┌───────────────────────────┐
│  SHOW WARNING:        │                   │  CLOSE GRACEFULLY:       │
│  "You have unsaved    │                   │  • Close WebSocket       │
│   changes. Stay on    │                   │  • Clear session flags    │
│   page?"              │                   │  • Allow browser close    │
│  [Leave] [Stay]       │                   └───────────────────────────┘
└───────────────────────┘
```

### 6.3 Logout and Data Sync Flow

```
┌─────────────────────────────────────────────────────────────────────┐
│              USER CLICKS LOGOUT                                    │
└─────┬───────────────────────────────────────────────────────────────┘
      │
      ▼
┌─────────────────────────────────────────────────────────────────────┐
│              SYNC PENDING CHANGES                                  │
│  ┌─────────────────────────────────────────────────────────────┐   │
│  │  Checking for unsaved changes...                             │   │
│  └─────────────────────────────────────────────────────────────┘   │
└─────┬───────────────────────────────────────────────────────────────┘
      │
      ├─ No changes ─────────────────────────────────┐
      │                                              ▼
      │                              ┌─────────────────────────────────────┐
      │                              │ Proceed with logout                │
      │                              │ • Clear local state                │
      │                              │ • Close WebSocket                 │
      │                              │ • Navigate to login               │
      │                              └─────────────────────────────────────┘
      │
      └─ Unsaved changes ──▶ ┌─────────────────────────────────────┐
                                │ Save changes before logout?       │
                                │ [Cancel] [Save & Logout] [Logout] │
                                └─────────────────────────────────────┘
```

---

## 7. NAVIGATION STATE DIAGRAM

```
                        ┌─────────────┐
                        │   Login     │
                        └──────┬──────┘
                               │
                    ┌──────────┴──────────┐
                    │                     │
                Authenticated         Not Authenticated
                    │                     │
                    ▼                     ▼
        ┌───────────────────┐    ┌──────────────┐
        │  HR Dashboard     │    │  Login Page  │
        │  (Overview)       │    └──────────────┘
        └─────────┬─────────┘
                  │
        ┌─────────┴─────────┐
        │                   │
    Full Data           Loading
    Available           Required
        │                   │
        ▼                   ▼
┌─────────────────────────────────────────────────────┐
│              HR DASHBOARD SECTIONS                  │
│  ┌─────────┐  ┌─────────┐  ┌─────────┐  ┌─────────┐│
│  │Overview │ │Recruit │ │Onboard │ │Perform  ││
│  └────┬────┘  └────┬────┘  └────┬────┘  └────┬────┘│
│       │            │            │            │       │
├───────┼────────────┼────────────┼────────────┼───────┤
│       │            │            │            │       │
│  ┌────┴────┐  ┌───┴────┐  ┌───┴────┐  ┌───┴────┐│
│  │Payroll  │  │Time     │  │Training│  │Engage  ││
│  └────┬────┘  │& Attend│  └────┬───┘  └────┬───┘│
│       │       └────┬────┘       │            │      │
├───────┼────────────┼─────────────┼────────────┼───────┤
│       │            │             │            │       │
│  ┌────┴────┐  ┌───┴─────────────┴────────────┘───┴───┐  │
│  │Comply   │  │         Approvals                │  │
│  └────┬────┘  └───────────────────────────────────┘  │
│       │                            │                   │
│       └───────────────────────────────────────────┘
                              │
              ┌──────────────┼──────────────┐
              ▼              ▼              ▼
        ┌─────────┐   ┌─────────┐   ┌─────────┐
        │  Back   │ │Navigate │ │  Logout │
        │         │ │ to other│ │         │
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

Next: [02_Wireframes_Documentation.md](#) - Visual wireframes for each screen
