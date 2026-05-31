# COUNTRY-HR-DASHBOARD - PAGE BY PAGE FLOW DOCUMENTATION

**Domain:** Business Domain
**Application:** Country HR Web Dashboard
**Version:** 1.0
**Date:** 2026-02-08
**Path:** `Business-domain/Country-HR-Dashboard/Frontends/country-hr-web-dashboard/`

---

## TABLE OF CONTENTS

1. [Page Flow Overview](#page-flow-overview)
2. [Authentication Flows](#authentication-flows)
3. [Overview Dashboard Page Flows](#overview-dashboard-page-flows)
4. [Employee Management Page Flows](#employee-management-page-flows)
5. [Payroll Management Page Flows](#payroll-management-page-flows)
6. [Recruitment Page Flows](#recruitment-page-flows)
7. [Performance Management Page Flows](#performance-management-page-flows)
8. [Training Page Flows](#training-page-flows)
9. [Leave & Attendance Page Flows](#leave--attendance-page-flows)
10. [Compliance Page Flows](#compliance-page-flows)
11. [Settings Page Flows](#settings-page-flows)
12. [Navigation Reference](#navigation-reference)

---

## 1. PAGE FLOW OVERVIEW

### 1.1 Application Page Tree

```
Country HR Web Dashboard
│
├── (Public)
│   ├── /login                          → LoginPage
│   └── /forgot-password                → ForgotPasswordPage
│
├── (Protected - Auth Required)
│   ├── /overview                       → OverviewDashboardPage
│   │
│   ├── /employees                      → EmployeeDirectoryPage
│   │   ├── /employees/add               → AddEmployeePage
│   │   ├── /employees/import            → BulkImportPage
│   │   ├── /employees/onboarding        → OnboardingQueuePage
│   │   ├── /employees/{id}              → EmployeeDetailPage
│   │   └── /employees/{id}/edit         → EditEmployeePage
│   │
│   ├── /payroll                        → PayrollOverviewPage
│   │   ├── /payroll/run                 → RunPayrollPage
│   │   ├── /payroll/payslips            → PayslipsPage
│   │   ├── /payroll/payslips/{id}       → PayslipDetailPage
│   │   ├── /payroll/salary              → SalaryAdministrationPage
│   │   └── /payroll/benefits            → BenefitsPage
│   │
│   ├── /recruitment                    → RecruitmentPage
│   │   ├── /recruitment/jobs            → JobPostingsPage
│   │   ├── /recruitment/jobs/add        → CreateJobPage
│   │   ├── /recruitment/jobs/{id}       → JobDetailPage
│   │   ├── /recruitment/applicants       → ApplicantsPage
│   │   ├── /recruitment/applicants/{id}  → ApplicantDetailPage
│   │   └── /recruitment/interviews       → InterviewsPage
│   │
│   ├── /performance                    → PerformancePage
│   │   ├── /performance/reviews          → PerformanceReviewsPage
│   │   ├── /performance/reviews/{id}     → ReviewDetailPage
│   │   ├── /performance/goals            → GoalsPage
│   │   ├── /performance/goals/add        → CreateGoalPage
│   │   └── /performance/cycles           → ReviewCyclesPage
│   │
│   ├── /training                       → TrainingPage
│   │   ├── /training/programs            → ProgramsPage
│   │   ├── /training/programs/add        → CreateProgramPage
│   │   ├── /training/programs/{id}       → ProgramDetailPage
│   │   ├── /training/enrollments         → EnrollmentsPage
│   │   ├── /training/certifications      → CertificationsPage
│   │   └── /training/catalog            → CourseCatalogPage
│   │
│   ├── /leave                          → LeaveManagementPage
│   │   ├── /leave/requests               → LeaveRequestsPage
│   │   ├── /leave/request               → CreateLeavePage
│   │   ├── /leave/balance                → LeaveBalancePage
│   │   ├── /leave/calendar               → LeaveCalendarPage
│   │   └── /attendance                  → AttendancePage
│   │
│   ├── /compliance                     → CompliancePage
│   │   ├── /compliance/status            → ComplianceStatusPage
│   │   ├── /compliance/policies          → PoliciesPage
│   │   ├── /compliance/audit             → AuditLogsPage
│   │   └── /compliance/reports           → ComplianceReportsPage
│   │
│   ├── /reports                        → ReportsPage
│   │   └── /reports/{id}                  → ReportViewPage
│   │
│   └── /settings                       → SettingsPage
│       ├── /settings/profile            → ProfileSettingsPage
│       ├── /settings/notifications      → NotificationSettingsPage
│       └── /settings/audit              → AuditLogsPage
```

### 1.2 Route Guards

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                         ROUTE GUARD FLOW                                   │
└─────────────────────────────────────────────────────────────────────────────┘

User navigates to route
      │
      ▼
┌─────────────────────────────────────────┐
│ Check route requirements                 │
│ • public: no auth required              │
│ • protected: auth required              │
│ • role: specific HR role required      │
│ • country: country context required     │
└─────────────────────────────────────────┘
      │
      ├─ Public route ────────────────────────────────────────┐
      │                                                           ▼
      │                                              ┌───────────┐
      │                                              │ Render page│
      │                                              └───────────┘
      │
      ├─ Protected route ──────────────┐
      │                                  ▼
      │                     ┌─────────────────────────────────────┐
      │                     │ Check authentication               │
      │                     │ (localStorage.token)              │
      │                     └─────┬─────────────────────────────┘
      │                           │
      │                ┌──────────┴──────────┐
      │                │                     │
      │           Authenticated         Not authenticated
      │                │                     │
      │                ▼                     ▼
      │        ┌───────────────┐    ┌───────────────┐
      │        │ Check country │    │ Redirect to   │
      │        └───────┬───────┘    │ /login        │
      │                │             └───────────────┘
      │       ┌────────┴────────┐
      │       │                 │
      │   Country Set     No Country
      │       │                 │
      │       ▼                 ▼
      │  ┌───────────┐   ┌───────────────┐
      │  │ Render    │   │ Load assigned │
      │  │ page      │   │ country &     │
      │  └───────────┘   │ render        │
      │                     └───────────────┘
      │
      └─ Error route ──────────┐
                                ▼
                        ┌───────────────┐
                        │ Render error   │
                        │ page (404)     │
                        └───────────────┘
```

---

## 2. AUTHENTICATION FLOWS

### 2.1 Login Page Flow

```
ROUTE: /login
┌─────────────────────────────────────────────────────────────────────────────┐
│                         LOGIN PAGE FLOW                                   │
└─────────────────────────────────────────────────────────────────────────────┘

Entry Points:
  • User navigates to /login
  • User redirected due to unauthenticated access
  • User clicks "Sign Out" (redirected here)

Initial State:
  ┌─────────────────────────────────────────────────────────────────────┐
  │  Login Form                                                        │
  │  ┌─────────────────────────────────────────────────────────────┐   │
  │  │  Email/Username: [_____________________________]           │   │
  │  │  Password:        [_____________________________] [👁️]    │   │
  │  │  ☐ Remember me                                             │   │
  │  │  [Sign In]                                                 │   │
  │  │  OR                                                        │   │
  │  │  [SSO with Microsoft] [SSO with Google]                     │   │
  │  │  Forgot password?                                          │   │
  │  └─────────────────────────────────────────────────────────────┘   │
  └─────────────────────────────────────────────────────────────────────┘

User Action: Click [Sign In]
      │
      ├─ VALID CREDENTIALS ───────────────────────────────────────────────────────┐
      │                                                                             │
      │                     ┌─────────────────────────────────────────────┐        │
      │                     │ API: POST /api/v1/country-hr/auth/login  │        │
      │                     │ Request: { email, password, rememberMe }  │        │
      │                     │ Response: { token, user, role, assignedCountry } │        │
      │                     └─────────────────────────────────────────────┘        │
      │                                       │                                      │
      │                                       ▼                                      │
      │                     ┌─────────────────────────────────────────────┐        │
      │                     │ Store:                                    │        │
      │                     │ • localStorage.setItem('token', token)     │        │
      │                     │ • localStorage.setItem('user', user)       │        │
      │                     │ • localStorage.setItem('assignedCountry',   │        │
      │                     │   assignedCountry)                          │        │
      │                     │ • store.setUser(user)                     │        │
      │                     │ • store.setAssignedCountry(assignedCountry) │        │
      │                     └─────────────────────────────────────────────┘        │
      │                                       │                                      │
      │                                       ▼                                      │
      │                     ┌─────────────────────────────────────────────┐        │
      │                     │ Load assigned country HR dashboard              │        │
      │                     └─────────────────────────────────────────────┘        │
      │                                       │                                      │
      │                                       ▼                                      │
      │                     ┌─────────────────────────────────────────────┐        │
      │                     │ Navigate to /overview                       │        │
      │                     │ Load country-specific HR data                  │        │
      │                     └─────────────────────────────────────────────┘        │
      │                                                                             │
      └─ INVALID CREDENTIALS ──────────────────────────────────────────────────────┘
                                                                                    │
                                                                                    ▼
                                                                      ┌─────────────────────────────────────┐
                                                                      │ Show error message                 │
                                                                      │ "Invalid email or password"         │
                                                                      │ Highlight input fields in red       │
                                                                      │ Allow retry                         │
                                                                      └─────────────────────────────────────┘

User Action: Click SSO with Microsoft/Google
      │
      ▼
┌─────────────────────────────────────────┐
│ Redirect to OAuth provider             │
│ window.location.href = ssoProviderUrl  │
└─────────────────────────────────────────┘
      │
      ▼ (After OAuth callback)
┌─────────────────────────────────────────┐
│ Handle OAuth callback                  │
│ Extract token from URL params           │
│ Store token and user data              │
│ Navigate to dashboard                  │
└─────────────────────────────────────────┘
```

---

## 3. OVERVIEW DASHBOARD PAGE FLOWS

### 3.1 Overview Dashboard Page Flow

```
ROUTE: /overview
┌─────────────────────────────────────────────────────────────────────────────┐
│                         OVERVIEW DASHBOARD PAGE FLOW                      │
└─────────────────────────────────────────────────────────────────────────────┘

Entry Points:
  • User logs in (auto-redirected to assigned country's overview)
  • User clicks "Overview" in sidebar
  • User clicks logo/brand
  • User navigates to /overview directly

Initial Load:
  ┌─────────────────────────────────────────────────────────────────────┐
  │  API CALLS (Parallel)                                              │
  │  • GET /api/v1/country-hr/dashboard/overview                        │
  │  • GET /api/v1/country-hr/employees                                  │
  │  • GET /api/v1/country-hr/payroll/summary                            │
  │  • GET /api/v1/country-hr/recruitment/active                         │
  │  • GET /api/v1/country-hr/leave/pending                              │
  │  • GET /api/v1/country-hr/compliance/status                          │
  └─────────────────────────────────────────────────────────────────────┘
                              │
                              ▼
  ┌─────────────────────────────────────────────────────────────────────┐
  │  COUNTRY HR OVERVIEW DASHBOARD                                    │
  │  ┌─────────────────────────────────────────────────────────────┐   │
  │  │ Overview │ Employees │ Payroll │ Recruit │ Perform │ Training │   │
  │  └─────────────────────────────────────────────────────────────┘   │
  │                                                                   │
  │  ┌─────────────────────────────────────────────────────────────┐   │
  │  │  HR HEALTH SCORE                                         [Details] │   │
  │  │  [Score: 94/100] [Trend: +2 from last month]                   │   │
  │  │  Active Employees: 347 │ Onboarding: 5 │ Leave Requests: 12     │   │
  │  └─────────────────────────────────────────────────────────────┘   │
  │                                                                   │
  │  ┌─────────────────────────────────────────────────────────────┐   │
  │  │  QUICK STATS                                                    │   │
  │  │  ┌───────────┐ ┌───────────┐ ┌───────────┐ ┌───────────┐   │   │
  │  │  │ Total     │ │ On Leave  │ │ New Hires │ │ Open      │   │   │
  │  │  │ Employees │ │ Today     │ │ This Month│ │ Positions │   │   │
  │  │  │   347     │ │    23     │ │     8     │ │    12     │   │   │
  │  │  └───────────┘ └───────────┘ └───────────┘ └───────────┘   │   │
  │  └─────────────────────────────────────────────────────────────┘   │
  │                                                                   │
  │  ┌─────────────────────────────────────────────────────────────┐   │
  │  │  ACTIVE ALERTS (5)                                [View All →] │   │
  │  └─────────────────────────────────────────────────────────────┘   │
  └─────────────────────────────────────────────────────────────────────┘

Page Interactions:

┌─────────────────────────────────────────────────────────────────────────┐
│ Interaction 1: Click Stat Card                                       │
├─────────────────────────────────────────────────────────────────────────┤
│ User clicks stat card (e.g., Total Employees)                           │
│      ▼                                                                 │
│ ┌───────────────────────────────────────────────────────────────────┐ │
│ │ Navigate to /employees                                             │ │
│ │ • Load employee directory                                          │ │
│ │ • Show all employees                                               │ │
│ │ • Apply filters for current stats                                  │ │
│ └───────────────────────────────────────────────────────────────────┘ │
└─────────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────────────┐
│ Interaction 2: Click Alert Item                                      │
├─────────────────────────────────────────────────────────────────────────┤
│ User clicks alert in Active Alerts                                     │
│      ▼                                                                 │
│ ┌───────────────────────────────────────────────────────────────────┐ │
│ │ Open Alert Detail Modal/Page                                       │ │
│ │ • Display full alert details                                      │ │
│ │ • Show affected employees                                         │ │
│ │ • User can: Acknowledge, Dismiss, Escalate                        │ │
│ └───────────────────────────────────────────────────────────────────┘ │
└─────────────────────────────────────────────────────────────────────────┘
```

---

## 4. EMPLOYEE MANAGEMENT PAGE FLOWS

### 4.1 Employee Directory Page Flow

```
ROUTE: /employees
┌─────────────────────────────────────────────────────────────────────────────┐
│                         EMPLOYEE DIRECTORY PAGE FLOW                      │
└─────────────────────────────────────────────────────────────────────────────┘

Entry Points:
  • User clicks "Employees" in sidebar
  • User navigates to /employees directly

Initial Load:
  ┌─────────────────────────────────────────────────────────────────────┐
  │  API CALL: GET /api/v1/country-hr/employees                           │
  │  Query Params: page=1, limit=50, country=NGA                         │
  └─────────────────────────────────────────────────────────────────────┘
                              │
                              ▼
  ┌─────────────────────────────────────────────────────────────────────┐
  │  EMPLOYEE DIRECTORY                                                │
  │  ┌─────────────────────────────────────────────────────────────┐   │
  │  │ [+ Add Employee]  [Bulk Import]  [Departments]  [Export ▼]  │   │
  │  └─────────────────────────────────────────────────────────────┘   │
  │                                                                   │
  │  ┌─────────────────────────────────────────────────────────────┐   │
  │  │ FILTERS: [All Departments ▼] [All Status ▼] [Search...]     │   │
  │  └─────────────────────────────────────────────────────────────┘   │
  │                                                                   │
  │  ┌─────────────────────────────────────────────────────────────┐   │
  │  │ EMPLOYEES LIST (347 total)                                    │   │
  │  [Employee cards/list with pagination]                             │   │
  │  └─────────────────────────────────────────────────────────────┘   │
  └─────────────────────────────────────────────────────────────────────┘

Page Interactions:

┌─────────────────────────────────────────────────────────────────────────┐
│ Interaction 1: Add New Employee                                         │
├─────────────────────────────────────────────────────────────────────────┤
│ User clicks [+ Add Employee]                                          │
│      ▼                                                                 │
│ ┌───────────────────────────────────────────────────────────────────┐ │
│ │ Navigate to /employees/add                                        │ │
│ │ • Multi-step form: Personal → Employment → Compensation → Benefits│ │
│ │ • Document upload                                                 │ │
│ │ • Submit creates employee record                                  │ │
│ │ • Returns to directory with success notification                   │ │
│ └───────────────────────────────────────────────────────────────────┘ │
└─────────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────────────┐
│ Interaction 2: View Employee Detail                                     │
├─────────────────────────────────────────────────────────────────────────┤
│ User clicks on employee card/name                                       │
│      ▼                                                                 │
│ ┌───────────────────────────────────────────────────────────────────┐ │
│ │ Navigate to /employees/{id}                                        │ │
│ └───────────────────────────────────────────────────────────────────┘ │
│      │                                                                 │
│      ▼                                                                 │
│ See Employee Detail Page Flow (section 4.2)                             │
└─────────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────────────┐
│ Interaction 3: Filter Employees                                        │
├─────────────────────────────────────────────────────────────────────────┤
│ User applies filters (e.g., Department → Sales, Status → Active)          │
│      ▼                                                                 │
│ ┌───────────────────────────────────────────────────────────────────┐ │
│ │ Filter Employees List                                               │ │
│ │ • Call GET /api/v1/country-hr/employees?department=SALES&status=ACTIVE│ │
│ │ • Update list with filtered results                                │ │
│ │ • Update URL params                                               │ │
│ └───────────────────────────────────────────────────────────────────┘ │
└─────────────────────────────────────────────────────────────────────────┘
```

### 4.2 Employee Detail Page Flow

```
ROUTE: /employees/{id}
┌─────────────────────────────────────────────────────────────────────────────┐
│                         EMPLOYEE DETAIL PAGE FLOW                         │
└─────────────────────────────────────────────────────────────────────────────┘

Entry Points:
  • User clicks employee from directory
  • User navigates to /employees/{id} directly

Initial Load:
  ┌─────────────────────────────────────────────────────────────────────┐
  │  API CALL: GET /api/v1/country-hr/employees/{id}                      │
  └─────────────────────────────────────────────────────────────────────┘
                              │
                              ▼
  ┌─────────────────────────────────────────────────────────────────────┐
  │  EMPLOYEE DETAIL PAGE                                              │
  │  [← Back to Directory]                       [Edit] [More ▼]       │
  │                                                                   │
  │  ┌─────────────────────────────────────────────────────────────┐   │
  │  │ EMPLOYEE PROFILE                                                 │   │
  │  │  Name, Photo, ID, Email, Phone, Location                       │   │
  │  │  Status, Department, Manager                                    │   │
  │  └─────────────────────────────────────────────────────────────┘   │
  │                                                                   │
  │  ┌─────────────────────────────────────────────────────────────┐   │
  │  │ TABS: Overview │ Employment │ Performance │ Training │ Benefits│   │
  │  │       Documents │ Leave │ Activity │ Notes                            │   │
  │  └─────────────────────────────────────────────────────────────┘   │
  │                                                                   │
  │  [Tab content displays selected tab information]                    │
  └─────────────────────────────────────────────────────────────────────┘

Page Interactions:

┌─────────────────────────────────────────────────────────────────────────┐
│ Interaction 1: Edit Employee                                         │
├─────────────────────────────────────────────────────────────────────────┤
│ User clicks [Edit] button                                              │
│      ▼                                                                 │
│ ┌───────────────────────────────────────────────────────────────────┐ │
│ │ Navigate to /employees/{id}/edit                                   │ │
│ │ • Pre-fill form with existing employee data                       │ │
│ │ • Allow modifications                                              │ │
│ │ • Submit updates employee record                                  │ │
│ │ • Returns to detail view                                          │ │
│ └───────────────────────────────────────────────────────────────────┘ │
└─────────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────────────┐
│ Interaction 2: Terminate Employee                                     │
├─────────────────────────────────────────────────────────────────────────┤
│ User clicks [More ▼] → Terminate                                      │
│      ▼                                                                 │
│ ┌───────────────────────────────────────────────────────────────────┐ │
│ │ Open Termination Confirmation Modal                                 │ │
│ │  • Select termination reason                                      │ │
│ │  • Enter last working date                                        │ │
│ │  • Add notes                                                      │ │
│ │  • Calculate final pay                                            │ │
│ │  • [Cancel] [Confirm Termination]                                 │ │
│ └───────────────────────────────────────────────────────────────────┘ │
│      │                                                                 │
│      └─ Click Confirm ─▶ API Call: POST /api/v1/country-hr/employees/{id}/terminate│
│                      • Update employee status to TERMINATED          │
│                      • Process final pay                            │
│                      • Send notifications                            │
│                      • Update directory                              │
└─────────────────────────────────────────────────────────────────────────┘
```

---

## 5. PAYROLL MANAGEMENT PAGE FLOWS

### 5.1 Payroll Overview Page Flow

```
ROUTE: /payroll
┌─────────────────────────────────────────────────────────────────────────────┐
│                         PAYROLL OVERVIEW PAGE FLOW                        │
└─────────────────────────────────────────────────────────────────────────────┘

Entry Points:
  • User clicks "Payroll" in sidebar
  • User navigates to /payroll directly

Initial Load:
  ┌─────────────────────────────────────────────────────────────────────┐
  │  API CALL: GET /api/v1/country-hr/payroll/summary                      │
  └─────────────────────────────────────────────────────────────────────┘
                              │
                              ▼
  ┌─────────────────────────────────────────────────────────────────────┐
  │  PAYROLL OVERVIEW                                                  │
  │  ┌─────────────────────────────────────────────────────────────┐   │
  │  │ PAYROLL CYCLE: February 2026                        [Run...]│   │
  │  └─────────────────────────────────────────────────────────────┘   │
  │                                                                   │
  │  ┌─────────────────────────────────────────────────────────────┐   │
  │  │ PAYROLL SUMMARY                                                  │   │
  │  │ Total Employees | Active Payslips | Net Pay | Status            │   │
  │  └─────────────────────────────────────────────────────────────┘   │
  │                                                                   │
  │  [Run Payroll] [View Payslips] [Salary Admin] [Benefits]             │
  └─────────────────────────────────────────────────────────────────────┘

Page Interactions:

┌─────────────────────────────────────────────────────────────────────────┐
│ Interaction 1: Run Payroll                                            │
├─────────────────────────────────────────────────────────────────────────┤
│ User clicks [Run Payroll]                                             │
│      ▼                                                                 │
│ ┌───────────────────────────────────────────────────────────────────┐ │
│ │ Open Payroll Run Configuration                                    │ │
│ │  • Select period                                                 │ │
│ │  • Confirm employees to include                                  │ │
│ │  • Review calculated amounts                                     │ │
│ │  • [Cancel] [Start Payroll Run]                                  │ │
│ └───────────────────────────────────────────────────────────────────┘ │
│      │                                                                 │
│      └─ Click Start ─▶ API Call: POST /api/v1/country-hr/payroll/run │
│                      • Initiate payroll calculation                │
│                      • Show progress via WebSocket                   │
│                      • Notify when ready for approval                │
└─────────────────────────────────────────────────────────────────────────┘
```

### 5.2 Payslips Page Flow

```
ROUTE: /payroll/payslips
┌─────────────────────────────────────────────────────────────────────────────┐
│                         PAYSLIPS PAGE FLOW                                 │
└─────────────────────────────────────────────────────────────────────────────┘

Entry Points:
  • User clicks "Payslips" from Payroll menu
  • User navigates to /payroll/payslips directly

Initial Load:
  ┌─────────────────────────────────────────────────────────────────────┐
  │  API CALL: GET /api/v1/country-hr/payroll/payslips                      │
  │  Query Params: period=2026-02                                         │
  └─────────────────────────────────────────────────────────────────────┘
                              │
                              ▼
  ┌─────────────────────────────────────────────────────────────────────┐
  │  PAYSLIPS - February 2026                                [Distribute All]│
  │  Generated: 320 | Distributed: 285 | Pending: 35                     │
  │                                                                   │
  │  [Payslips list with employee name, amount, status, actions]      │
  └─────────────────────────────────────────────────────────────────────┘

Page Interactions:

┌─────────────────────────────────────────────────────────────────────────┐
│ Interaction 1: Distribute Payslip                                     │
├─────────────────────────────────────────────────────────────────────────┤
│ User clicks [Send] or [Distribute All]                                │
│      ▼                                                                 │
│ ┌───────────────────────────────────────────────────────────────────┐ │
│ │ API Call: POST /api/v1/country-hr/payroll/payslips/distribute     │ │
│ │  • Generate payslip PDF                                          │ │
│ │  • Send email to employee                                        │ │
│ │  • Update status to SENT                                         │ │
│ │  • Show success notification                                      │ │
│ └───────────────────────────────────────────────────────────────────┘ │
└─────────────────────────────────────────────────────────────────────────┘
```

---

## 6. RECRUITMENT PAGE FLOWS

### 6.1 Job Postings Page Flow

```
ROUTE: /recruitment/jobs
┌─────────────────────────────────────────────────────────────────────────────┐
│                         JOB POSTINGS PAGE FLOW                             │
└─────────────────────────────────────────────────────────────────────────────┘

Entry Points:
  • User clicks "Recruitment" → "Jobs"
  • User navigates to /recruitment/jobs directly

Initial Load:
  ┌─────────────────────────────────────────────────────────────────────┐
  │  API CALL: GET /api/v1/country-hr/recruitment/jobs                      │
  │  Query Params: status=ACTIVE                                           │
  └─────────────────────────────────────────────────────────────────────┘
                              │
                              ▼
  ┌─────────────────────────────────────────────────────────────────────┐
  │  JOB POSTINGS                                                      │
  │  [+ New Job Posting]  [Active: 12]  [Closed: 45]  [Templates]     │
  │                                                                   │
  │  [Active jobs list with applicant counts and actions]                │
  └─────────────────────────────────────────────────────────────────────┘

Page Interactions:

┌─────────────────────────────────────────────────────────────────────────┐
│ Interaction 1: Create New Job Posting                                 │
├─────────────────────────────────────────────────────────────────────────┤
│ User clicks [+ New Job Posting]                                       │
│      ▼                                                                 │
│ ┌───────────────────────────────────────────────────────────────────┐ │
│ │ Navigate to /recruitment/jobs/add                                 │ │
│ │ • Job title, department, location                                 │ │
│ │ • Employment type, description, requirements                       │ │
│ │ • Salary range, application deadline                               │ │
│ │ • [Save as Draft] [Publish]                                        │ │
│ └───────────────────────────────────────────────────────────────────┘ │
└─────────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────────────┐
│ Interaction 2: View Applicant Pipeline                                 │
├─────────────────────────────────────────────────────────────────────────┤
│ User clicks [View Pipeline →] on job posting                           │
│      ▼                                                                 │
│ ┌───────────────────────────────────────────────────────────────────┐ │
│ │ Navigate to /recruitment/applicants?jobId={id}                    │ │
│ │ • Show applicant pipeline for selected job                        │ │
│ │ • Display applicants by stage                                      │ │
│ │ • Allow stage transitions                                         │ │
│ └───────────────────────────────────────────────────────────────────┘ │
└─────────────────────────────────────────────────────────────────────────┘
```

---

## 7. PERFORMANCE MANAGEMENT PAGE FLOWS

### 7.1 Performance Reviews Page Flow

```
ROUTE: /performance/reviews
┌─────────────────────────────────────────────────────────────────────────────┐
│                         PERFORMANCE REVIEWS PAGE FLOW                      │
└─────────────────────────────────────────────────────────────────────────────┘

Entry Points:
  • User clicks "Performance" → "Reviews"
  • User navigates to /performance/reviews directly

Initial Load:
  ┌─────────────────────────────────────────────────────────────────────┐
  │  API CALL: GET /api/v1/country-hr/performance/reviews                   │
  │  Query Params: cycle=Q4-2025                                          │
  └─────────────────────────────────────────────────────────────────────┘
                              │
                              ▼
  ┌─────────────────────────────────────────────────────────────────────┐
  │  PERFORMANCE REVIEWS - Q4 2025                                      │
  │  Status: In Progress | Due: Feb 28, 2026                            │
  │  [Start Review]                                                      │
  │                                                                   │
  │  [Review progress: 71% complete]                                     │
  │  [My reviews list with status]                                      │
  └─────────────────────────────────────────────────────────────────────┘

Page Interactions:

┌─────────────────────────────────────────────────────────────────────────┐
│ Interaction 1: Start/Complete Review                                   │
├─────────────────────────────────────────────────────────────────────────┤
│ User clicks on employee in review list                                 │
│      ▼                                                                 │
│ ┌───────────────────────────────────────────────────────────────────┐ │
│ │ Navigate to /performance/reviews/{id}                             │ │
│ │ • Display employee information                                      │ │
│ │ • Show goal achievement ratings                                     │ │
│ │ • Competency ratings                                               │ │
│ │ • Overall rating                                                   │ │
│ │ • Recommendations                                                  │ │
│ │ • Development goals                                                │ │
│ │ • [Save Draft] [Submit] [Schedule Discussion]                      │ │
│ └───────────────────────────────────────────────────────────────────┘ │
└─────────────────────────────────────────────────────────────────────────┘
```

---

## 8. TRAINING PAGE FLOWS

### 8.1 Training Programs Page Flow

```
ROUTE: /training/programs
┌─────────────────────────────────────────────────────────────────────────────┐
│                         TRAINING PROGRAMS PAGE FLOW                        │
└─────────────────────────────────────────────────────────────────────────────┘

Entry Points:
  • User clicks "Training" → "Programs"
  • User navigates to /training/programs directly

Initial Load:
  ┌─────────────────────────────────────────────────────────────────────┐
  │  API CALL: GET /api/v1/country-hr/training/programs                    │
  │  Query Params: status=ACTIVE                                          │
  └─────────────────────────────────────────────────────────────────────┘
                              │
                              ▼
  ┌─────────────────────────────────────────────────────────────────────┐
  │  TRAINING PROGRAMS                                                 │
  │  [+ Create Program]  [Active: 8]  [Upcoming: 5]  [Archived]        │
  │                                                                   │
  │  [Active programs list with enrollment and completion stats]         │
  └─────────────────────────────────────────────────────────────────────┘

Page Interactions:

┌─────────────────────────────────────────────────────────────────────────┐
│ Interaction 1: Enroll Employee in Program                              │
├─────────────────────────────────────────────────────────────────────────┤
│ User clicks on program → [Enroll Employees]                           │
│      ▼                                                                 │
│ ┌───────────────────────────────────────────────────────────────────┐ │
│ │ Open Enrollment Modal                                             │ │
│ │  • Search employees                                               │ │
│ │  • Select employees to enroll                                     │ │
│ │  • Set enrollment deadline                                        │ │
│ │  • [Cancel] [Enroll]                                             │ │
│ └───────────────────────────────────────────────────────────────────┘ │
│      │                                                                 │
│      └─ Click Enroll ─▶ API Call: POST /api/v1/country-hr/training/enroll│
│                      • Enroll selected employees                     │
│                      • Send enrollment notifications               │
│                      • Update program enrollment count              │
└─────────────────────────────────────────────────────────────────────────┘
```

---

## 9. LEAVE & ATTENDANCE PAGE FLOWS

### 9.1 Leave Management Page Flow

```
ROUTE: /leave
┌─────────────────────────────────────────────────────────────────────────────┐
│                         LEAVE MANAGEMENT PAGE FLOW                          │
└─────────────────────────────────────────────────────────────────────────────┘

Entry Points:
  • User clicks "Leave & Attendance" → "Leave Management"
  • User navigates to /leave directly

Initial Load:
  ┌─────────────────────────────────────────────────────────────────────┐
  │  API CALLS (Parallel)                                               │
  │  • GET /api/v1/country-hr/leave/requests                            │
  │  • GET /api/v1/country-hr/leave/balance                             │
  │  • GET /api/v1/country-hr/attendance/today                          │
  └─────────────────────────────────────────────────────────────────────┘
                              │
                              ▼
  ┌─────────────────────────────────────────────────────────────────────┐
  │  LEAVE MANAGEMENT                                                   │
  │  [+ Request Leave]  [My Team]  [Calendar View]  [Leave Policy]     │
  │                                                                   │
  │  [Pending approvals (12 requests)]                                   │
  │  [Today's attendance summary]                                        │
  └─────────────────────────────────────────────────────────────────────┘

Page Interactions:

┌─────────────────────────────────────────────────────────────────────────┐
│ Interaction 1: Approve/Reject Leave Request                            │
├─────────────────────────────────────────────────────────────────────────┤
│ User clicks [Approve] or [Reject] on leave request                     │
│      ▼                                                                 │
│ ┌───────────────────────────────────────────────────────────────────┐ │
│ │ Immediate Action (Approve/Reject)                                │ │
│ │  • Approve: Update status to APPROVED, update leave balance      │ │
│ │  • Reject: Open rejection reason modal, then update status         │ │
│ │  • Send notification to employee                                   │ │
│ │  • Refresh list                                                   │ │
│ └───────────────────────────────────────────────────────────────────┘ │
└─────────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────────────┐
│ Interaction 2: Create Leave Request                                   │
├─────────────────────────────────────────────────────────────────────────┤
│ User clicks [+ Request Leave]                                         │
│      ▼                                                                 │
│ ┌───────────────────────────────────────────────────────────────────┐ │
│ │ Open Leave Request Form                                           │ │
│ │  • Select leave type                                              │ │
│ │  • Select start and end dates                                     │ │
│ │  • Enter reason                                                   │ │
│ │  • Check leave balance                                            │ │
│ │  • Upload supporting documents (if required)                      │ │
│ │  • [Cancel] [Submit]                                              │ │
│ └───────────────────────────────────────────────────────────────────┘ │
│      │                                                                 │
│      └─ Click Submit ─▶ API Call: POST /api/v1/country-hr/leave/requests│
│                      • Create leave request                         │
│                      • Submit for approval                           │
│                      • Notify manager                               │
└─────────────────────────────────────────────────────────────────────────┘
```

---

## 10. COMPLIANCE PAGE FLOWS

### 10.1 Compliance Status Page Flow

```
ROUTE: /compliance
┌─────────────────────────────────────────────────────────────────────────────┐
│                         COMPLIANCE STATUS PAGE FLOW                        │
└─────────────────────────────────────────────────────────────────────────────┘

Entry Points:
  • User clicks "Compliance" in sidebar
  • User navigates to /compliance directly

Initial Load:
  ┌─────────────────────────────────────────────────────────────────────┐
  │  API CALL: GET /api/v1/country-hr/compliance/status                    │
  └─────────────────────────────────────────────────────────────────────┘
                              │
                              ▼
  ┌─────────────────────────────────────────────────────────────────────┐
  │  COMPLIANCE STATUS                                                  │
  │  [Compliance Health Score: 94/100]                                 │
  │                                                                   │
  │  [Compliance categories with status]                                │
  │  [Policy acknowledgments pending]                                   │
  │  [Active alerts]                                                    │
  └─────────────────────────────────────────────────────────────────────┘

Page Interactions:

┌─────────────────────────────────────────────────────────────────────────┐
│ Interaction 1: View Audit Logs                                        │
├─────────────────────────────────────────────────────────────────────────┤
│ User clicks "Audit Logs"                                               │
│      ▼                                                                 │
│ ┌───────────────────────────────────────────────────────────────────┐ │
│ │ Navigate to /compliance/audit                                     │ │
│ │ • Display audit log entries                                       │ │
│ │ • Filter by user, category, date range                            │ │
│ │ • Export audit trail                                              │ │
│ └───────────────────────────────────────────────────────────────────┘ │
└─────────────────────────────────────────────────────────────────────────┘
```

---

## 11. SETTINGS PAGE FLOWS

### 11.1 Settings Page Flow

```
ROUTE: /settings
┌─────────────────────────────────────────────────────────────────────────────┐
│                         SETTINGS PAGE FLOW                                 │
└─────────────────────────────────────────────────────────────────────────────┘

Entry Points:
  • User clicks "Settings" in sidebar
  • User navigates to /settings directly

Initial Load:
  ┌─────────────────────────────────────────────────────────────────────┐
  │  SETTINGS                                                          │
  │  [Profile] [Country Config] [Notifications] [Audit Logs] [Help]   │
  │                                                                   │
  │  [Profile settings form with user information]                      │
  │  [Notification preferences]                                        │
  └─────────────────────────────────────────────────────────────────────┘

Page Interactions:

┌─────────────────────────────────────────────────────────────────────────┐
│ Interaction 1: Update Profile                                         │
├─────────────────────────────────────────────────────────────────────────┤
│ User updates profile information                                       │
│      ▼                                                                 │
│ ┌───────────────────────────────────────────────────────────────────┐ │
│ │ API Call: PUT /api/v1/country-hr/settings/profile                 │ │
│ │  • Update user profile                                            │ │
│ │  • Update store                                                  │ │
│ │  • Show success notification                                      │ │
│ └───────────────────────────────────────────────────────────────────┘ │
└─────────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────────────┐
│ Interaction 2: Change Password                                        │
├─────────────────────────────────────────────────────────────────────────┤
│ User clicks "Change Password"                                          │
│      ▼                                                                 │
│ ┌───────────────────────────────────────────────────────────────────┐ │
│ │ Open Change Password Modal                                         │ │
│ │  • Enter current password                                        │ │
│ │  • Enter new password                                            │ │
│ │  • Confirm new password                                          │ │
│ │  • [Cancel] [Update Password]                                    │ │
│ └───────────────────────────────────────────────────────────────────┘ │
│      │                                                                 │
│      └─ Click Update ─▶ API Call: POST /api/v1/country-hr/auth/change-password│
│                      • Validate current password                    │
│                      • Update password                              │
│                      • Re-authenticate or logout                      │
└─────────────────────────────────────────────────────────────────────────┘
```

---

## 12. NAVIGATION REFERENCE

### 12.1 Permission Matrix

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                         PERMISSION MATRIX                                  │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│  Feature                    │ Country HR Manager │ HR Manager │ HR Specialist│
│  ├────────────────────────────┼──────────────────┼────────────┼──────────────┤
│  │ Overview Dashboard         │ Full View         │ Full View    │ Read Only    │
│  │ Employees - CRUD           │ ✅                │ Dept Only   │ Read Only    │
│  │ Employees - Terminate      │ ✅                │ ❌          │ ❌           │
│  │ Payroll - Process          │ ✅                │ ❌          │ ❌           │
│  │ Payroll - View             │ ✅                │ ✅          │ ✅           │
│  │ Payroll - Approve          │ ✅                │ Recommend  │ ❌           │
│  │ Recruitment - Full Access   │ ✅                │ ✅          │ ATS Only     │
│  │ Performance - Reviews      │ ✅                │ ✅          │ Read Only    │
│  │ Performance - Goals        │ ✅                │ ✅          │ Read Only    │
│  │ Training - Manage          │ ✅                │ View Only   │ Enroll Only  │
│  │ Leave - Approve            │ ✅                │ ✅          │ Read Only    │
│  │ Leave - Request            │ ✅                │ ✅          │ ✅           │
│  │ Compliance - Full Access    │ ✅                │ Read Only   │ Read Only    │
│  │ Reports - All               │ ✅                │ Dept Only   │ Limited      │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

**End of Page By Page Flow Documentation v1.0**

**Country-HR-Dashboard Documentation Complete - 4/4 files**

This completes the Country-HR-Dashboard documentation. The HQ HR Dashboard documentation will be created in the Management Domain.
