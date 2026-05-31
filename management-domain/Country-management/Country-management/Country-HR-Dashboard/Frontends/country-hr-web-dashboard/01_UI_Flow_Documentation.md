# COUNTRY-HR-DASHBOARD - UI FLOW DOCUMENTATION

**Domain:** Business Domain
**Application:** Country HR Web Dashboard
**Version:** 1.0
**Date:** 2026-02-08
**Path:** `Business-domain/Country-HR-Dashboard/Frontends/country-hr-web-dashboard/`

---

## TABLE OF CONTENTS

1. [Domain Context](#domain-context)
2. [Application Entry Flows](#application-entry-flows)
3. [Authentication Flows](#authentication-flows)
4. [Dashboard Navigation Flows](#dashboard-navigation-flows)
5. [Feature-Specific Flows](#feature-specific-flows)
6. [Data Integration Flows](#data-integration-flows)
7. [Reporting Flows](#reporting-flows)
8. [Navigation State Diagram](#navigation-state-diagram)

---

## 1. DOMAIN CONTEXT

### 1.1 Ecosystem Architecture

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                         GOGIDIX ECOSYSTEM ARCHITECTURE                   │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│  ┌─────────────────────────────────────────────────────────────────────┐   │
│  │                    MANAGEMENT DOMAIN                                │   │
│  │  ┌──────────────────────────────────────────────────────────────┐   │   │
│  │  │  HQ-Human-Resource Management Dashboard                       │   │   │
│  │  │  • Global HR oversight across ALL countries                   │   │   │
│  │  │  • Receives reports from each country-level HR manager        │   │   │
│  │  │  • Multi-country view, strategic HR decisions                 │   │   │
│  │  └──────────────────────────────────────────────────────────────┘   │   │
│  └─────────────────────────────────────────────────────────────────────┘   │
│                                    ↕ REPORTS TO                            │
│  ┌─────────────────────────────────────────────────────────────────────┐   │
│  │                    BUSINESS DOMAIN                                 │   │
│  │  ┌──────────────────────────────────────────────────────────────┐   │   │
│  │  │  Country-HR-Dashboard (THIS DOCUMENTATION)                    │   │   │
│  │  │  • Country-level HR operations                                │   │   │
│  │  │  • Each Country HR Manager assigned to ONE country            │   │   │
│  │  │  • Manages employees within assigned country                   │   │   │
│  │  │  • Reports HR data to HQ                                      │   │   │
│  │  └──────────────────────────────────────────────────────────────┘   │   │
│  └─────────────────────────────────────────────────────────────────────┘   │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

### 1.2 Business Domain HR Scope

```
Business Domain - Country-HR-Dashboard
│
├── EMPLOYEE MANAGEMENT
│   ├── Employee Directory
│   ├── Employee Onboarding
│   ├── Employee Offboarding
│   ├── Employee Transfers
│   └── Employee Records
│
├── PAYROLL MANAGEMENT
│   ├── Payroll Processing
│   ├── Salary Administration
│   ├── Benefits Administration
│   ├── Tax & Compliance
│   └── Payslip Management
│
├── RECRUITMENT (ATS)
│   ├── Job Postings
│   ├── Applicant Tracking
│   ├── Interview Scheduling
│   ├── Offer Management
│   └── Onboarding Workflow
│
├── PERFORMANCE MANAGEMENT
│   ├── Goal Setting (OKRs/KPIs)
│   ├── Performance Reviews
│   ├── 360-Degree Feedback
│   ├── Improvement Plans
│   └── Promotion Recommendations
│
├── TRAINING & DEVELOPMENT
│   ├── Training Programs
│   ├── Course Management
│   ├── Employee Enrollments
│   ├── Progress Tracking
│   └── Certification Tracking
│
└── COMPLIANCE & AUDIT
    ├── Attendance Tracking
    ├── Leave Management
    ├── Policy Compliance
    ├── Audit Logs
    └── Regulatory Reporting
```

### 1.3 User Roles & Entry Points

```
┌─────────────────────────────────────────────────────────────────────┐
│                     COUNTRY HR DASHBOARD USERS                      │
├─────────────────────────────────────────────────────────────────────┤
│                                                                      │
│   ┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐ │
│   │ Country HR      │    │ HR Manager      │    │ HR Specialist   │ │
│   │ Manager         │    │ (Department)    │    │                 │ │
│   │                 │    │                 │    │                 │ │
│   │ Assigned to:    │    │ Assigned to:    │    │ Assigned to:    │ │
│   │ ONE country     │    │ ONE country     │    │ ONE country     │
│   │                 │    │                 │    │                 │ │
│   │ Full HR         │    │ Department HR   │    │ Functional HR   │ │
│   │ operations      │    │ operations      │    │ operations      │ │
│   └─────────────────┘    └─────────────────┘    └─────────────────┘ │
│         │                       │                       │              │
│         └───────────────────────┴───────────────────────┘              │
│                                 │                                      │
│                         ┌───────▼───────┐                                │
│                         │ Country HR    │                                │
│                         │ Web Dashboard │                                │
│                         └───────────────┘                                │
│                                                                      │
│   ❌ REMOVED: Multi-country access                                    │
│   ✅ Each manager assigned to ONE operational country only            │
│                                                                      │
└─────────────────────────────────────────────────────────────────────┘
```

### 1.4 Dashboard Structure

```
Country HR Web Dashboard
│
├── Overview
│   ├── HR Health Score
│   ├── Employee Statistics
│   ├── Cross-Department HR Metrics
│   ├── Active Alerts
│   └── Pending Approvals
│
├── Employees
│   ├── Employee Directory
│   ├── Onboarding Queue
│   ├── Offboarding Process
│   ├── Employee Transfers
│   └── Employee Records
│
├── Payroll
│   ├── Payroll Overview
│   ├── Salary Administration
│   ├── Benefits Management
│   ├── Tax & Compliance
│   └── Payslip Distribution
│
├── Recruitment
│   ├── Job Postings
│   ├── Applicant Tracking
│   ├── Interview Scheduling
│   ├── Offer Management
│   └── Hiring Pipeline
│
├── Performance
│   ├── Goal Setting (OKRs/KPIs)
│   ├── Performance Reviews
│   ├── 360-Degree Feedback
│   ├── Improvement Plans
│   └── Promotion Cycle
│
├── Training
│   ├── Training Programs
│   ├── Course Catalog
│   ├── Employee Enrollments
│   ├── Progress Tracking
│   └── Certifications
│
├── Leave & Attendance
│   ├── Leave Management
│   ├── Attendance Tracking
│   ├── Timesheet Approval
│   ├── Leave Balance
│   └── Attendance Reports
│
├── Compliance
│   ├── Policy Management
│   ├── Compliance Monitoring
│   ├── Audit Logs
│   ├── Regulatory Reports
│   └── Risk Assessment
│
└── Settings
    ├── Profile Settings
    ├── Country Configuration
    ├── Notification Settings
    └── Audit Logs
```

---

## 2. APPLICATION ENTRY FLOWS

### 2.1 Main Entry Flow

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                         LOAD ASSIGNED COUNTRY CONTEXT                      │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│  User: Sarah Okon (s.okon@gogidix.com)                                    │
│  Role: Country HR Manager                                                  │
│  Assigned Country: Nigeria (NGA)                                           │
│  ❌ Cannot access other countries' HR operations                           │
│                                                                              │
│    ┌─────────────────────────────────────────────────────────────────┐      │
│    │              LOADING NIGERIA HR OPERATIONS                       │      │
│    │  ┌─────────────────────────────────────────────────────────────┐   │
│    │  │  🇳🇬 NIGERIA                                                  │   │
│    │  │  Country HR Dashboard                                       │   │
│    │  │  ┌─────────────────────────────────────────────────────────┐ │   │
│    │  │  │  347 Active Employees                                   │ │   │
│    │  │  │  • 320 Full-time Staff                                  │ │   │
│    │  │  │  • 27 Contract Staff                                    │ │   │
│    │  │  │                                                         │ │   │
│    │  │  │  HR Health Score: 94/100 ●                             │ │   │
│    │  │  │  Onboarding: 5  │  Leave Requests: 12  │  Issues: 2     │ │   │
│    │  │  └─────────────────────────────────────────────────────────┘ │   │
│    │  │  Loading country-specific HR data...                        │   │
│    │  └─────────────────────────────────────────────────────────────┘   │
│    │                                                                   │
│    │  [Note: You are viewing Nigeria HR operations only.              │      │
│    │         To access other countries, contact Management.]          │      │
│    └─────────────────────────────────────────────────────────────────┘      │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
      │
      ▼
┌─────────────────────────────────────────┐
│ Navigate to Role Dashboard              │
│ Based on user role:                     │
│ • Country HR Manager → Full HR ops     │
│ • HR Manager (Dept) → Department HR    │
│ • HR Specialist → Functional area      │
└─────────────────────────────────────────┘
```

### 2.2 Assigned Country Context Flow

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                    ASSIGNED COUNTRY CONTEXT LOADING                        │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│  User logs in successfully                                                  │
│      │                                                                      │
│      ▼                                                                      │
│  ┌─────────────────────────────────────────────────────────────────────┐    │
│  │  API: POST /api/v1/country-hr/auth/login                           │    │
│  │  Request: { email, password, rememberMe }                         │    │
│  │  Response: {                                                       │    │
│  │    token,                                                          │    │
│  │    user,                                                           │    │
│  │    role,                                                           │    │
│  │    assignedCountry: {                                              │    │
│  │      countryCode: "NGA",                                           │    │
│  │      countryName: "Nigeria",                                       │    │
│  │      currency: "NGN",                                              │    │
│  │      flag: "🇳🇬",                                                  │    │
│  │      activeEmployees: 347                                          │    │
│  │    }                                                               │    │
│  │  }                                                                 │    │
│  └─────────────────────────────────────────────────────────────────────┘    │
│                              │                                              │
│                              ▼                                              │
│  ┌─────────────────────────────────────────────────────────────────────┐    │
│  │  Store:                                                            │    │
│  │  • localStorage.setItem('token', token)                            │    │
│  │  • localStorage.setItem('user', user)                              │    │
│  │  • localStorage.setItem('assignedCountry', assignedCountry)        │    │
│  │  • store.setUser(user)                                             │    │
│  │  • store.setAssignedCountry(assignedCountry)                       │    │
│  └─────────────────────────────────────────────────────────────────────┘    │
│                              │                                              │
│                              ▼                                              │
│  ┌─────────────────────────────────────────────────────────────────────┐    │
│  │  Load Nigeria HR Dashboard Data                                   │    │
│  │  • GET /api/v1/country-hr/dashboard/overview                       │    │
│  │  • GET /api/v1/country-hr/employees                                │    │
│  │  • GET /api/v1/country-hr/payroll/summary                          │    │
│  │  • GET /api/v1/country-hr/recruitment/active                       │    │
│  │  • GET /api/v1/country-hr/leave/pending                            │    │
│  └─────────────────────────────────────────────────────────────────────┘    │
│                              │                                              │
│                              ▼                                              │
│  ┌─────────────────────────────────────────────────────────────────────┐    │
│  │  Navigate to /overview                                             │    │
│  │  Display Nigeria HR operations dashboard                          │    │
│  └─────────────────────────────────────────────────────────────────────┘    │
│                                                                              │
│  ⚠️ Note: Country HR managers are assigned to ONE country only.          │
│     No country switching is available.                                    │
│     To manage a different country, contact Management Domain.             │
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
│                    ┌─────────────────┐                                     │
│                    │   GOGIDIX       │                                     │
│                    │   Country HR    │                                     │
│                    │   Platform      │                                     │
│                    └─────────────────┘                                     │
│                                                                              │
│                    ┌─────────────────────────────┐                           │
│                    │  Email / Username           │                           │
│                    └─────────────────────────────┘                           │
│                                                                              │
│                    ┌─────────────────────────────┐                           │
│                    │  Password        [👁️]      │                           │
│                    └─────────────────────────────┘                           │
│                                                                              │
│                    [ ] Remember me                                          │
│                    Forgot password?                                         │
│                                                                              │
│                    ┌─────────────────────────────┐                           │
│                    │     SIGN IN                │                           │
│                    └─────────────────────────────┘                           │
│                                                                              │
│                    OR                                                       │
│                                                                              │
│                    [ SSO with Microsoft ]                                    │
│                    [ SSO with Google ]                                       │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
      │
      │ User enters credentials and clicks Sign In
      ▼
┌─────────────────────────────────────────────────────────────────────────────┐
│                      VALIDATE CREDENTIALS                                  │
│  ┌──────────────────────────────────────────────────────────────────────┐  │
│  │  POST /api/v1/country-hr/auth/login                                  │  │
│  │  Request: { email, password, rememberMe }                            │  │
│  │  Response: { token, user, role, assignedCountry }                    │  │
│  └──────────────────────────────────────────────────────────────────────┘  │
└────┬──────────────────────────────────────────────────────────────────┬─────┘
     │ VALID                                                              │ INVALID
     ▼                                                                    ▼
┌─────────────────┐                                            ┌─────────────────┐
│  Store Token    │                                            │  Show Error:    │
│  Load User Data │                                            │  "Invalid       │
│  Load Assigned  │                                            │   credentials"  │
│  Country        │                                            │  Allow retry    │
│  Navigate to    │                                            └─────────────────┘
│  HR Dashboard   │
└─────────────────┘
```

### 3.2 Logout Flow

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                       LOGOUT INITIATED                                      │
│  Trigger: User clicks logout from user menu                                 │
└─────┬───────────────────────────────────────────────────────────────────────┘
      │
      ▼
┌─────────────────────────────────────────────────────────────────────────────┐
│                      CONFIRM LOGOUT                                        │
│  ┌──────────────────────────────────────────────────────────────────────┐  │
│  │  Are you sure you want to logout?                                    │  │
│  │                                                                      │  │
│  │  [Cancel]  [Logout]                                                 │  │
│  └──────────────────────────────────────────────────────────────────────┘  │
└─────┬───────────────────────────────────────────────────────────────────────┘
      │                          │
      │ Cancel                   │ Confirm Logout
      ▼                          ▼
┌─────────────────┐      ┌─────────────────────────────────────────────────────┐
│  Return to      │      │              CLEAR SESSION                            │
│  Dashboard      │      │  • Call POST /api/v1/country-hr/auth/logout          │
│                 │      │  • Clear localStorage (token, user, assignedCountry)  │
│                 │      │  • Clear sessionStorage                              │
│                 │      │  • Clear cookies                                     │
│                 │      │  • Close WebSocket connections                       │
│                 │      │  • Navigate to Login screen                          │
│                 │      └─────────────────────────────────────────────────────┘
└─────────────────┘
```

---

## 4. DASHBOARD NAVIGATION FLOWS

### 4.1 Overview Dashboard Navigation Flow

```
                    ┌─────────────────┐
                    │ COUNTRY HR      │
                    │ DASHBOARD       │
                    │    (Home)       │
                    └────────┬────────┘
                             │
        ┌────────────────────┼────────────────────┐
        │                    │                    │
        ▼                    ▼                    ▼
┌───────────────┐    ┌───────────────┐    ┌───────────────┐
│ Sidebar Nav   │    │ Top Nav       │    │ Main Content  │
│               │    │               │    │               │
│ • Overview    │    │ Country Flag  │    │ HR Health     │
│ • Employees   │    │ Notification  │    │ Employee Stats │
│ • Payroll     │    │ User Menu     │    │ Payroll Summ  │
│ • Recruitment │    │ Connection    │    │ Active Alerts │
│ • Performance │    │               │    │ Pending Approv │
│ • Training    │    │               │    │               │
│ • Leave       │    │               │    │               │
│ • Compliance  │    │               │    │               │
│ • Reports     │    │               │    │               │
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
        │  2. Click Stat Card → Drill down to detail view     │
        │  3. Click Alert Item → Open alert detail           │
        │  4. Click Approval → Open approval form            │
        │  5. Click Country Flag → Show assigned country info │
        │  6. Click User Menu → Profile, Settings, Logout    │
        └──────────────────────────────────────────────────────┘
```

### 4.2 Subdomain Navigation Flow

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                    HR SUBDOMAIN NAVIGATION                                 │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│  User clicks on sidebar navigation item                                     │
│      │                                                                      │
│      ├── Employees → /employees                                             │
│      │   ├── Employee Directory                                            │
│      │   ├── Onboarding Queue                                              │
│      │   ├── Offboarding Process                                           │
│      │   └── Employee Records                                              │
│      │                                                                     │
│      ├── Payroll → /payroll                                                │
│      │   ├── Payroll Overview                                              │
│      │   ├── Salary Administration                                          │
│      │   ├── Benefits Management                                           │
│      │   └── Tax & Compliance                                              │
│      │                                                                     │
│      ├── Recruitment → /recruitment                                        │
│      │   ├── Job Postings                                                  │
│      │   ├── Applicant Tracking                                            │
│      │   ├── Interview Scheduling                                          │
│      │   └── Hiring Pipeline                                               │
│      │                                                                     │
│      ├── Performance → /performance                                        │
│      │   ├── Goal Setting (OKRs)                                           │
│      │   ├── Performance Reviews                                           │
│      │   ├── 360-Degree Feedback                                           │
│      │   └── Improvement Plans                                              │
│      │                                                                     │
│      ├── Training → /training                                              │
│      │   ├── Training Programs                                             │
│      │   ├── Course Catalog                                                │
│      │   ├── Employee Enrollments                                          │
│      │   └── Progress Tracking                                              │
│      │                                                                     │
│      ├── Leave & Attendance → /leave                                       │
│      │   ├── Leave Management                                              │
│      │   ├── Attendance Tracking                                           │
│      │   ├── Timesheet Approval                                            │
│      │   └── Leave Balance                                                 │
│      │                                                                     │
│      └── Compliance → /compliance                                          │
│          ├── Policy Management                                             │
│          ├── Compliance Monitoring                                         │
│          ├── Audit Logs                                                    │
│          └── Regulatory Reports                                             │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

### 4.3 Role-Based Navigation Flow

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                    ROLE-BASED ACCESS CONTROL                               │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│  ┌─────────────────────────────────────────────────────────────────────┐   │
│  │  COUNTRY HR MANAGER                                                │   │
│  │  Full access to all HR functions within assigned country            │   │
│  │  • Employees (Full CRUD)                                           │   │
│  │  • Payroll (Approve & Process)                                     │   │
│  │  • Recruitment (Full ATS)                                          │   │
│  │  • Performance (Full management)                                   │   │
│  │  • Training (Full management)                                      │   │
│  │  • Leave (Approve & Manage)                                        │   │
│  │  • Compliance (Full access)                                        │   │
│  │  • Reports (All reports)                                           │   │
│  └─────────────────────────────────────────────────────────────────────┘   │
│                                                                              │
│  ┌─────────────────────────────────────────────────────────────────────┐   │
│  │  HR MANAGER (DEPARTMENT)                                           │   │
│  │  Limited to department-specific HR functions                       │   │
│  │  • Department Employees (CRUD)                                    │   │
│  │  • Department Payroll (View & Recommend)                           │   │
│  │  • Department Recruitment (Full ATS)                               │   │
│  │  • Department Performance (Full management)                        │   │
│  │  • Department Training (View & Enroll)                             │   │
│  │  • Department Leave (Approve)                                      │   │
│  │  • Reports (Department reports only)                               │   │
│  └─────────────────────────────────────────────────────────────────────┘   │
│                                                                              │
│  ┌─────────────────────────────────────────────────────────────────────┐   │
│  │  HR SPECIALIST                                                     │   │
│  │  Functional area access only                                      │   │
│  │  • Employees (Read-only, Edit specific fields)                    │   │
│  │  • Payroll (Read-only)                                            │   │
│  │  • Recruitment (ATS management only)                              │   │
│  │  • Performance (Read-only)                                        │   │
│  │  • Training (Enrollment management)                               │   │
│  │  • Leave (Read-only)                                              │   │
│  │  • Compliance (Read-only)                                         │   │
│  │  • Reports (Limited reports)                                      │   │
│  └─────────────────────────────────────────────────────────────────────┘   │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## 5. FEATURE-SPECIFIC FLOWS

### 5.1 Employee Management Flow

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                    EMPLOYEE MANAGEMENT FLOW                                │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│  User clicks "Employees" in sidebar                                         │
│      │                                                                      │
│      ▼                                                                      │
│  ┌─────────────────────────────────────────────────────────────────────┐    │
│  │  EMPLOYEE DIRECTORY                                                │    │
│  │  ┌─────────────────────────────────────────────────────────────┐   │    │
│  │  │ [+ Add Employee]  [Import]  [Departments]  [Export]        │   │    │
│  │  └─────────────────────────────────────────────────────────────┘   │    │
│  │                                                                   │    │
│  │  ┌─────────────────────────────────────────────────────────────┐   │    │
│  │  │ FILTERS: [All Departments ▼] [All Status ▼] [Search...]     │   │    │
│  │  └─────────────────────────────────────────────────────────────┘   │    │
│  │                                                                   │    │
│  │  ┌─────────────────────────────────────────────────────────────┐   │    │
│  │  │ EMPLOYEES LIST (347 total)                                  │   │    │
│  │  │ [Employee cards/list with pagination]                       │   │    │
│  │  └─────────────────────────────────────────────────────────────┘   │    │
│  └─────────────────────────────────────────────────────────────────────┘    │
│                                                                              │
│  User Actions:                                                               │
│  ├── [+ Add Employee] → Opens employee creation form                        │
│  │   ├── Personal Information                                              │
│  │   ├── Employment Details                                                │
│  │   ├── Department Assignment                                              │
│  │   ├── Compensation                                                      │
│  │   ├── Benefits Selection                                                │
│  │   └── Document Upload                                                   │
│  │                                                                         │
│  ├── Click Employee → Opens employee detail view                           │
│  │   ├── Profile Information                                               │
│  │   ├── Employment History                                                │
│  │   ├── Compensation Details                                              │
│  │   ├── Benefits                                                          │
│  │   ├── Performance Records                                               │
│  │   ├── Training History                                                  │
│  │   ├── Leave Balance                                                     │
│  │   └── [Edit] [Terminate] [Transfer]                                     │
│  │                                                                         │
│  └── [Import] → Bulk employee import via CSV                                │
│      ├── Template download                                                  │
│      ├── File upload                                                       │
│      ├── Validation                                                        │
│      └── Bulk creation                                                     │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

### 5.2 Onboarding Flow

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                    EMPLOYEE ONBOARDING FLOW                                │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│  New employee record created or recruited                                   │
│      │                                                                      │
│      ▼                                                                      │
│  ┌─────────────────────────────────────────────────────────────────────┐    │
│  │  ONBOARDING CHECKLIST                                              │    │
│  │  ┌─────────────────────────────────────────────────────────────┐   │    │
│  │  │  Employee: Jane Doe (jane.doe@gogidix.com)                   │   │    │
│  │  │  Department: Sales | Start Date: 2026-02-15                  │   │    │
│  │  │  Progress: ████████░░ 60%                                    │   │    │
│  │  └─────────────────────────────────────────────────────────────┘   │    │
│  │                                                                   │    │
│  │  ┌─────────────────────────────────────────────────────────────┐   │    │
│  │  │ ONBOARDING TASKS                                             │   │    │
│  │  │  ☐ Employment Contract Signed                                │   │    │
│  │  │  ☑ Email Account Created                                     │   │    │
│  │  │  ☑ System Access Provisioned                                 │   │    │
│  │  │  ☐ Background Check Completed                                │   │    │
│  │  │  ☐ ID Badge Issued                                            │   │    │
│  │  │  ☐ Benefits Enrolled                                         │   │    │
│  │  │  ☐ Orientation Training Completed                            │   │    │
│  │  │  ☐ Desk/Equipment Assigned                                    │   │    │
│  │  └─────────────────────────────────────────────────────────────┘   │    │
│  │                                                                   │    │
│  │  [Mark Complete]  [Assign Task]  [View Documents]                │    │
│  └─────────────────────────────────────────────────────────────────────┘    │
│                                                                              │
│  All tasks completed → Employee status: ACTIVE                               │
│  Send welcome notification → Notify manager, IT, department                  │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

### 5.3 Payroll Processing Flow

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                    PAYROLL PROCESSING FLOW                                 │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│  User clicks "Payroll" → "Payroll Overview"                                 │
│      │                                                                      │
│      ▼                                                                      │
│  ┌─────────────────────────────────────────────────────────────────────┐    │
│  │  PAYROLL OVERVIEW                                                  │    │
│  │  ┌─────────────────────────────────────────────────────────────┐   │    │
│  │  │ Payroll Cycle: February 2026 | Status: Processing           │   │    │
│  │  │ [Run Payroll]  [View Previous]  [Pay Settings]              │   │    │
│  │  └─────────────────────────────────────────────────────────────┘   │    │
│  │                                                                   │    │
│  │  ┌───────────────┬───────────────┬───────────────┬─────────────┐  │    │
│  │  │ Total        │ Active       │ Net Pay      │ Processing  │  │    │
│  │  │ Employees    │ Payslips     │ (NGN)       │ Status      │  │    │
│  │  ├───────────────┼───────────────┼───────────────┼─────────────┤  │    │
│  │  │ 347          │ 320          │ ₦127,500,000 │ In Progress │  │    │
│  │  └───────────────┴───────────────┴───────────────┴─────────────┘  │    │
│  │                                                                   │    │
│  │  [View Payslips]  [Approve for Payment]  [Export Report]          │    │
│  └─────────────────────────────────────────────────────────────────────┘    │
│                                                                              │
│  [Run Payroll] →                                                            │
│  ┌─────────────────────────────────────────────────────────────────────┐    │
│  │  PAYROLL CALCULATION                                               │    │
│  │  1. Calculate gross pay for all employees                          │    │
│  │  2. Apply deductions (tax, pension, benefits)                      │    │
│  │  3. Calculate net pay                                               │    │
│  │  4. Generate payslips                                              │    │
│  │  5. Prepare payment file                                           │    │
│  │  6. Submit for approval                                            │    │
│  └─────────────────────────────────────────────────────────────────────┘    │
│                              │                                              │
│                              ▼                                              │
│  ┌─────────────────────────────────────────────────────────────────────┐    │
│  │  PAYROLL APPROVAL                                                  │    │
│  │  Requires approval from Country HR Manager                         │    │
│  │  [Review] [Approve] [Reject]                                       │    │
│  └─────────────────────────────────────────────────────────────────────┘    │
│                              │                                              │
│                              ▼                                              │
│  Approved → Payment processed → Payslips distributed → Notify employees    │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

### 5.4 Recruitment (ATS) Flow

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                    RECRUITMENT ATS FLOW                                    │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│  User clicks "Recruitment" → "Job Postings"                                 │
│      │                                                                      │
│      ▼                                                                      │
│  ┌─────────────────────────────────────────────────────────────────────┐    │
│  │  JOB POSTINGS                                                      │    │
│  │  ┌─────────────────────────────────────────────────────────────┐   │    │
│  │  │ [+ New Job Posting]  [Active Jobs]  [Closed Jobs]  [Templates]│   │    │
│  │  └─────────────────────────────────────────────────────────────┘   │    │
│  │                                                                   │    │
│  │  ┌─────────────────────────────────────────────────────────────┐   │    │
│  │  │ ACTIVE JOB POSTINGS (12 open positions)                     │   │    │
│  │  │ ┌───────────────────────────────────────────────────────┐  │   │    │
│  │  │ │ Sales Representative | Lagos | 45 applicants | [View→]│  │   │    │
│  │  │ │ HR Manager | Abuja | 23 applicants | [View→]         │  │   │    │
│  │  │ │ Software Engineer | Remote | 89 applicants | [View→] │  │   │    │
│  │  │ └───────────────────────────────────────────────────────┘  │   │    │
│  │  └─────────────────────────────────────────────────────────────┘   │    │
│  └─────────────────────────────────────────────────────────────────────┘    │
│                                                                              │
│  [New Job Posting] →                                                        │
│  ┌─────────────────────────────────────────────────────────────────────┐    │
│  │  CREATE JOB POSTING                                                 │    │
│  │  • Job Title                                                        │    │
│  │  • Department                                                       │    │
│  │  • Location                                                         │    │
│  │  • Employment Type (Full-time/Part-time/Contract)                   │    │
│  │  • Job Description                                                  │    │
│  │  • Requirements                                                     │    │
│  │  • Salary Range                                                     │    │
│  │  • Application Deadline                                              │    │
│  │  [Publish] [Save as Draft]                                          │    │
│  └─────────────────────────────────────────────────────────────────────┘    │
│                                                                              │
│  [View→] on job posting → Applicant Pipeline                                │
│  ┌─────────────────────────────────────────────────────────────────────┐    │
│  │  APPLICANT PIPELINE                                                │    │
│  │  New (89) → Screened (45) → Interview (23) → Offer (8) → Hired (5) │    │
│  │                                                                   │    │
│  │  Click applicant → View profile → Schedule interview → Send offer  │    │
│  └─────────────────────────────────────────────────────────────────────┘    │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

### 5.5 Performance Review Flow

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                    PERFORMANCE REVIEW FLOW                                 │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│  User clicks "Performance" → "Performance Reviews"                          │
│      │                                                                      │
│      ▼                                                                      │
│  ┌─────────────────────────────────────────────────────────────────────┐    │
│  │  PERFORMANCE REVIEWS                                                │    │
│  │  ┌─────────────────────────────────────────────────────────────┐   │    │
│  │  │ Cycle: Q4 2025 | Status: In Progress                          │   │    │
│  │  │ [+ Start Review]  [View Templates]  [Cycle Settings]          │   │    │
│  │  └─────────────────────────────────────────────────────────────┘   │    │
│  │                                                                   │    │
│  │  ┌─────────────────────────────────────────────────────────────┐   │    │
│  │  │ REVIEW PROGRESS                                                │   │    │
│  │  │  ┌────────────┐  ┌────────────┐  ┌────────────┐             │   │    │
│  │  │  │ Completed  │  │ In Review  │  │ Not Started│             │   │    │
│  │  │  │ 245        │  │ 52         │  │ 50         │             │   │    │
│  │  │  │ 71%        │  │ 15%        │  │ 14%        │             │   │    │
│  │  │  └────────────┘  └────────────┘  └────────────┘             │   │    │
│  │  └─────────────────────────────────────────────────────────────┘   │    │
│  │                                                                   │    │
│  │  [Self Reviews]  [Manager Reviews]  [360 Feedback]                │    │
│  └─────────────────────────────────────────────────────────────────────┘    │
│                                                                              │
│  Click employee → Start/View Review                                          │
│  ┌─────────────────────────────────────────────────────────────────────┐    │
│  │  PERFORMANCE REVIEW FORM                                           │    │
│  │  Employee: John Okon | Reviewer: Sarah Okon | Period: Q4 2025      │    │
│  │                                                                   │    │
│  │  ┌─────────────────────────────────────────────────────────────┐   │    │
│  │  │ 1. GOAL ACHIEVEMENT (Weight: 40%)                             │   │    │
│  │  │    • Exceeded expectations (5)                                │   │    │
│  │  │    • Met expectations (4)                                     │   │    │
│  │  │    • Below expectations (3)                                   │   │    │
│  │  │    • Comments: [________________]                             │   │    │
│  │  │                                                                 │   │    │
│  │  │ 2. COMPETENCIES (Weight: 30%)                                  │   │    │
│  │  │    • Leadership: [Rating]                                     │   │    │
│  │  │    • Communication: [Rating]                                  │   │    │
│  │  │    • Technical Skills: [Rating]                               │   │    │
│  │  │                                                                 │   │    │
│  │  │ 3. BEHAVIORS (Weight: 20%)                                    │   │    │
│  │  │    • Teamwork: [Rating]                                       │   │    │
│  │  │    • Innovation: [Rating]                                     │   │    │
│  │  │                                                                 │   │    │
│  │  │ 4. OVERALL RATING (Weight: 10%)                                │   │    │
│  │  │    ☐ Exceeds Expectations                                      │   │    │
│  │  │    ☐ Meets Expectations                                        │   │    │
│  │  │    ☐ Needs Improvement                                         │   │    │
│  │  │    ☐ Unsatisfactory                                            │   │    │
│  │  │                                                                 │   │    │
│  │  │ 5. DEVELOPMENT GOALS FOR NEXT PERIOD                           │   │    │
│  │  │    • [Add goal]                                                │   │    │
│  │  │                                                                 │   │    │
│  │  │ 6. RECOMMENDATIONS                                             │   │    │
│  │  │    ☐ Promotion                                                 │   │    │
│  │  │    ☐ Bonus                                                     │   │    │
│  │  │    ☐ Transfer                                                  │   │    │
│  │  │    ☐ Performance Improvement Plan                              │   │    │
│  │  │                                                                 │   │    │
│  │  │  [Save Draft]  [Submit]  [Schedule Discussion]                 │   │    │
│  │  └─────────────────────────────────────────────────────────────┘   │    │
│  └─────────────────────────────────────────────────────────────────────┘    │
│                                                                              │
│  Submit → Notify employee → Schedule review discussion → Finalize → Report  │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

### 5.6 Leave Management Flow

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                    LEAVE MANAGEMENT FLOW                                   │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│  User clicks "Leave & Attendance" → "Leave Management"                      │
│      │                                                                      │
│      ▼                                                                      │
│  ┌─────────────────────────────────────────────────────────────────────┐    │
│  │  LEAVE MANAGEMENT                                                   │    │
│  │  ┌─────────────────────────────────────────────────────────────┐   │    │
│  │  │ [Request Leave]  [My Team Leave]  [Leave Calendar]  [Policy] │   │    │
│  │  └─────────────────────────────────────────────────────────────┘   │    │
│  │                                                                   │    │
│  │  ┌─────────────────────────────────────────────────────────────┐   │    │
│  │  │ PENDING APPROVALS (12 requests)                              │   │    │
│  │  │ ┌───────────────────────────────────────────────────────┐  │   │    │
│  │  │ │ John O. | Annual Leave | Feb 10-15 | [Approve] [Reject]│  │   │    │
│  │  │ │ Mary A. | Sick Leave | Feb 8-9 | [Approve] [Reject]   │  │   │    │
│  │  │ └───────────────────────────────────────────────────────┘  │   │    │
│  │  └─────────────────────────────────────────────────────────────┘   │    │
│  │                                                                   │    │
│  │  ┌─────────────────────────────────────────────────────────────┐   │    │
│  │  │ TODAY'S ATTENDANCE                                            │   │    │
│  │  │  Present: 312 | On Leave: 23 | Absent: 5 | Remote: 7        │   │    │
│  │  └─────────────────────────────────────────────────────────────┘   │    │
│  └─────────────────────────────────────────────────────────────────────┘    │
│                                                                              │
│  [Approve/Reject] → Decision → Notify employee → Update leave balance        │
│                                                                              │
│  [Request Leave] →                                                          │
│  ┌─────────────────────────────────────────────────────────────────────┐    │
│  │  LEAVE REQUEST FORM                                                │    │
│  │  • Leave Type: Annual | Sick | Maternity | Paternity | Study      │    │
│  │  • Start Date                                                       │    │
│  │  • End Date                                                         │    │
│  │  • Reason                                                           │    │
│  │  • Attach Documents (if required)                                  │    │
│  │  • Check Leave Balance                                             │    │
│  │  [Submit]                                                          │    │
│  └─────────────────────────────────────────────────────────────────────┘    │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## 6. DATA INTEGRATION FLOWS

### 6.1 HR Data Aggregation Flow

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                    HR DATA AGGREGATION FLOW                                │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│  Country HR Dashboard → Sends data to HQ                                    │
│      │                                                                      │
│      ▼                                                                      │
│  ┌─────────────────────────────────────────────────────────────────────┐    │
│  │  DATA COLLECTION                                                    │    │
│  │  Collect all HR data for assigned country:                          │    │
│  │  • Employee headcount and demographics                              │    │
│  │  • Payroll summaries                                                │    │
│  │  • Recruitment metrics                                              │    │
│  │  • Performance review completion                                    │    │
│  │  • Training completion rates                                        │    │
│  │  • Leave balances and usage                                         │    │
│  │  • Compliance status                                                │    │
│  └─────────────────────────────────────────────────────────────────────┘    │
│                              │                                              │
│                              ▼                                              │
│  ┌─────────────────────────────────────────────────────────────────────┐    │
│  │  AGGREGATION                                                       │    │
│  │  • Calculate KPIs and metrics                                       │    │
│  │  • Generate summary reports                                         │    │
│  │  • Format data for HQ consumption                                   │    │
│  └─────────────────────────────────────────────────────────────────────┘    │
│                              │                                              │
│                              ▼                                              │
│  ┌─────────────────────────────────────────────────────────────────────┐    │
│  │  TRANSMISSION TO HQ                                                │    │
│  │  POST /api/v1/hq-hr/data/aggregation                               │    │
│  │  {                                                                 │    │
│  │    country: "NGA",                                                 │    │
│  │    period: "2026-02",                                              │    │
│  │    employeeCount: 347,                                             │    │
│  │    payrollAmount: 127500000,                                       │    │
│  │    hiredThisMonth: 8,                                              │    │
│  │    leftThisMonth: 3,                                               │    │
│  │    performanceReviewsCompleted: 245,                               │    │
│  │    trainingCompletionRate: 78,                                     │    │
│  │    leaveUtilization: 65,                                           │    │
│  │    complianceScore: 94                                             │    │
│  │  }                                                                 │    │
│  └─────────────────────────────────────────────────────────────────────┘    │
│                              │                                              │
│                              ▼                                              │
│  HQ HR Dashboard receives and displays Nigeria HR data                    │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

### 6.2 Real-Time Data Synchronization

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                    REAL-TIME DATA SYNC                                    │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│  WebSocket Connection: ws://api/country-hr/ws                               │
│                                                                              │
│  Subscriptions:                                                             │
│  ├── /topic/hr/employees → Employee changes (add, update, terminate)        │
│  ├── /topic/hr/payroll → Payroll status updates                            │
│  ├── /topic/hr/recruitment → New applicants, status changes                │
│  ├── /topic/hr/performance → Review submissions, approvals                 │
│  ├── /topic/hr/leave → Leave requests, approvals                           │
│  └── /topic/hr/compliance → Compliance alerts, notifications               │
│                                                                              │
│  Event Flow:                                                                │
│  ┌─────────────────────────────────────────────────────────────────────┐    │
│  │  Employee data changed (e.g., salary update)                        │    │
│  │  ↓                                                                  │    │
│  │  Kafka Event: hr.employee.updated                                   │    │
│  │  ↓                                                                  │    │
│  │  WebSocket Broadcast → Country HR Dashboard                          │    │
│  │  ↓                                                                  │    │
│  │  UI Update: Employee record reflects new salary                      │    │
│  │  ↓                                                                  │    │
│  │  Sync to HQ: Aggregated data includes salary change                  │    │
│  └─────────────────────────────────────────────────────────────────────┘    │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## 7. REPORTING FLOWS

### 7.1 HQ Reporting Flow

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                    HQ REPORTING FLOW                                       │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│  Country HR Dashboard → Generates Report → Sends to HQ                      │
│                                                                              │
│  Report Types:                                                              │
│  ├── Daily HR Summary                                                       │
│  │   └── Employee count, attendance, incidents                             │
│  │                                                                         │
│  ├── Weekly HR Report                                                       │
│  │   └── Hiring, terminations, payroll summary, training                   │
│  │                                                                         │
│  ├── Monthly HR Dashboard                                                   │
│  │   └── Full KPIs, trends, compliance, budget                             │
│  │                                                                         │
│  └── Ad-hoc Reports                                                         │
│      └── Custom reports generated on demand                                 │
│                                                                              │
│  Report Generation Flow:                                                     │
│  ┌─────────────────────────────────────────────────────────────────────┐    │
│  │  1. Collect Data from all HR modules                                │    │
│  │  2. Aggregate and calculate metrics                                  │    │
│  │  3. Generate visualizations (charts, graphs)                         │    │
│  │  4. Create summary with insights                                     │    │
│  │  5. Format report (PDF/Excel)                                        │    │
│  │  6. Submit to HQ HR Dashboard                                       │    │
│  └─────────────────────────────────────────────────────────────────────┘    │
│                              │                                              │
│                              ▼                                              │
│  ┌─────────────────────────────────────────────────────────────────────┐    │
│  │  HQ HR DASHBOARD                                                    │    │
│  │  Receives Nigeria HR Report                                         │    │
│  │  Displays Nigeria data in global HR overview                       │    │
│  │  Enables cross-country comparison                                  │    │
│  │  Supports strategic HR decisions                                   │    │
│  └─────────────────────────────────────────────────────────────────────┘    │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## 8. NAVIGATION STATE DIAGRAM

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                    COUNTRY HR DASHBOARD STATE MACHINE                      │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│  ┌──────────────┐                                                           │
│  │   LOGIN      │                                                           │
│  └──────┬───────┘                                                           │
│         │ success                                                            │
│         ▼                                                                    │
│  ┌──────────────┐   country_assigned   ┌──────────────┐                     │
│  │  LOADING     │ ───────────────────▶│  OVERVIEW    │◀──────────┐         │
│  │  COUNTRY     │                     │              │           │         │
│  └──────────────┘                     └──────┬───────┘           │         │
│         │                                    │                   │         │
│         │                                    │ nav              │ nav     │
│         │                                    ▼                   │         │
│         │                         ┌─────────────────┐            │         │
│         │                         │   EMPLOYEES     │            │         │
│         │                         │   ┌─ Directory   │            │         │
│         │                         │   ├─ Onboarding  │            │         │
│         │                         │   └─ Offboarding │            │         │
│         │                         └────────┬─────────┘            │         │
│         │                                 │                     │         │
│         │                                 nav                   │         │
│         │                                 ▼                     │         │
│         │                         ┌─────────────────┐            │         │
│         │                         │    PAYROLL      │            │         │
│         │                         │   ├─ Overview   │            │         │
│         │                         │   ├─ Salary     │            │         │
│         │                         │   ├─ Benefits   │            │         │
│         │                         │   └─ Compliance  │            │         │
│         │                         └────────┬─────────┘            │         │
│         │                                 │                     │         │
│         │                                 nav                   │         │
│         │                                 ▼                     │         │
│         │                         ┌─────────────────┐            │         │
│         │                         │  RECRUITMENT    │            │         │
│         │                         │   ├─ Jobs      │            │         │
│         │                         │   ├─ Applicants │            │         │
│         │                         │   └─ Pipeline   │            │         │
│         │                         └────────┬─────────┘            │         │
│         │                                 │                     │         │
│         │                                 nav                   │         │
│         │                                 ▼                     │         │
│         │                         ┌─────────────────┐            │         │
│         │                         │  PERFORMANCE    │            │         │
│         │                         │   ├─ Goals      │            │         │
│         │                         │   ├─ Reviews    │            │         │
│         │                         │   └─ 360 Feedback│            │         │
│         │                         └────────┬─────────┘            │         │
│         │                                 │                     │         │
│         │                                 nav                   │         │
│         │                                 ▼                     │         │
│         │                         ┌─────────────────┐            │         │
│         │                         │   TRAINING      │            │         │
│         │                         │   ├─ Programs  │            │         │
│         │                         │   ├─ Courses    │            │         │
│         │                         │   └─ Enrollments│            │         │
│         │                         └────────┬─────────┘            │         │
│         │                                 │                     │         │
│         │                                 nav                   │         │
│         │                                 ▼                     │         │
│         │                         ┌─────────────────┐            │         │
│         │                         │  LEAVE & ATTEND │            │         │
│         │                         │   ├─ Leave     │            │         │
│         │                         │   ├─ Attendance│            │         │
│         │                         │   └─ Timesheet │            │         │
│         │                         └────────┬─────────┘            │         │
│         │                                 │                     │         │
│         │                                 nav                   │         │
│         │                                 ▼                     │         │
│         │                         ┌─────────────────┐            │         │
│         │                         │   COMPLIANCE    │            │         │
│         │                         │   ├─ Policies   │            │         │
│         │                         │   ├─ Audit      │            │         │
│         │                         │   └─ Reports    │            │         │
│         │                         └────────┬─────────┘            │         │
│         │                                 │                     │         │
│         │                                 └─────────────────────┘         │
│         │                                                            │         │
│         └──────────────────────────────────────────────────────────┘         │
│                                                                      │         │
│  logout ──────────────────────────────────────────────────────────▶ LOGIN   │
│                                                                      │         │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

**End of UI Flow Documentation v1.0**

**Next:** [02_Wireframes_Documentation.md](./02_Wireframes_Documentation.md) - Visual wireframes for all pages
