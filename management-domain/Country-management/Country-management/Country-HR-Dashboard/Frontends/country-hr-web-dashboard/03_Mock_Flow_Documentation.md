# COUNTRY-HR-DASHBOARD - MOCK FLOW DOCUMENTATION

**Domain:** Business Domain
**Application:** Country HR Web Dashboard
**Version:** 1.0
**Date:** 2026-02-08
**Path:** `Business-domain/Country-HR-Dashboard/Frontends/country-hr-web-dashboard/`

---

## TABLE OF CONTENTS

1. [Mock Data Overview](#mock-data-overview)
2. [API Flow Diagrams](#api-flow-diagrams)
3. [Mock Data Definitions](#mock-data-definitions)
4. [State Management Flow](#state-management-flow)
5. [WebSocket Real-Time Flow](#websocket-real-time-flow)
6. [Error Handling Flow](#error-handling-flow)

---

## 1. MOCK DATA OVERVIEW

### 1.1 Data Flow Architecture

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                      COUNTRY-HR-DASHBOARD DATA FLOW                       │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│  ┌─────────────┐     ┌─────────────┐     ┌─────────────┐                 │
│  │   Browser   │────▶│   API       │────▶│  Backend    │                 │
│  │   (React)   │◀────│  Gateway    │◀────│  Services   │                 │
│  └──────┬──────┘     └──────┬──────┘     └──────┬──────┘                 │
│         │                   │                   │                          │
│         │                   │                   │                          │
│         ▼                   ▼                   ▼                          │
│  ┌─────────────┐     ┌─────────────┐     ┌─────────────┐                 │
│  │  Zustand    │     │    Redis    │     │  MongoDB    │                 │
│  │  Store      │     │   Cache     │     │  Database   │                 │
│  └─────────────┘     └──────┬──────┘     └──────┬──────┘                 │
│         │                   │                   │                          │
│         │                   ▼                   ▼                          │
│         │            ┌─────────────┐     ┌─────────────┐                 │
│         │            │    Kafka    │────▶│  WebSocket  │                 │
│         │            │   Events    │     │  Broadcast  │                 │
│         │            └─────────────┘     └─────────────┘                 │
│         │                   │                   │                          │
│         └───────────────────┴───────────────────┘                          │
│                              ▲                                             │
│                              │                                             │
│                    Real-time updates to UI                                 │
│                                                                              │
│  ┌─────────────────────────────────────────────────────────────────────┐   │
│  │                      INTEGRATION LAYER                              │   │
│  │  Data flows to HQ HR Dashboard (Management Domain)                 │   │
│  └─────────────────────────────────────────────────────────────────────┘   │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

### 1.2 API Endpoints Summary

| Endpoint | Method | Purpose | Response Type |
|----------|--------|---------|---------------|
| **Authentication** |
| `/api/v1/country-hr/auth/login` | POST | Authenticate user | `{ token, user, role, assignedCountry }` |
| `/api/v1/country-hr/auth/logout` | POST | Logout user | `{ success }` |
| `/api/v1/country-hr/auth/me` | GET | Get current user | `{ user, role, permissions }` |
| **Dashboard** |
| `/api/v1/country-hr/dashboard/overview` | GET | Get dashboard overview | `DashboardOverviewDTO` |
| **Employee Management** |
| `/api/v1/country-hr/employees` | GET | Get all employees | `EmployeeSummary` |
| `/api/v1/country-hr/employees/{id}` | GET | Get employee details | `EmployeeDetail` |
| `/api/v1/country-hr/employees` | POST | Create employee | `{ success, employeeId }` |
| `/api/v1/country-hr/employees/{id}` | PUT | Update employee | `{ success }` |
| `/api/v1/country-hr/employees/{id}/terminate` | POST | Terminate employee | `{ success, exitDate }` |
| `/api/v1/country-hr/employees/onboarding` | GET | Get onboarding queue | `OnboardingTask[]` |
| `/api/v1/country-hr/employees/onboarding/{id}/complete` | POST | Complete onboarding task | `{ success }` |
| **Payroll Management** |
| `/api/v1/country-hr/payroll/summary` | GET | Get payroll summary | `PayrollSummary` |
| `/api/v1/country-hr/payroll/run` | POST | Initiate payroll run | `{ success, runId }` |
| `/api/v1/country-hr/payroll/payslips` | GET | Get payslips | `Payslip[]` |
| `/api/v1/country-hr/payroll/payslips/{id}` | GET | Get payslip detail | `PayslipDetail` |
| `/api/v1/country-hr/payroll/payslips/distribute` | POST | Distribute payslips | `{ success, sent }` |
| `/api/v1/country-hr/payroll/salary` | GET | Get salary data | `SalaryData[]` |
| `/api/v1/country-hr/payroll/salary/{id}` | PUT | Update salary | `{ success }` |
| **Recruitment** |
| `/api/v1/country-hr/recruitment/jobs` | GET | Get job postings | `JobPosting[]` |
| `/api/v1/country-hr/recruitment/jobs` | POST | Create job posting | `{ success, jobId }` |
| `/api/v1/country-hr/recruitment/jobs/{id}` | PUT | Update job posting | `{ success }` |
| `/api/v1/country-hr/recruitment/applicants` | GET | Get applicants | `Applicant[]` |
| `/api/v1/country-hr/recruitment/applicants/{id}` | GET | Get applicant detail | `ApplicantDetail` |
| `/api/v1/country-hr/recruitment/applicants/{id}/stage` | PUT | Update applicant stage | `{ success }` |
| `/api/v1/country-hr/recruitment/interviews` | GET | Get interviews | `Interview[]` |
| `/api/v1/country-hr/recruitment/interviews` | POST | Schedule interview | `{ success, interviewId }` |
| **Performance Management** |
| `/api/v1/country-hr/performance/reviews` | GET | Get performance reviews | `PerformanceReview[]` |
| `/api/v1/country-hr/performance/reviews/{id}` | GET | Get review detail | `PerformanceReviewDetail` |
| `/api/v1/country-hr/performance/reviews` | POST | Create review | `{ success, reviewId }` |
| `/api/v1/country-hr/performance/reviews/{id}` | PUT | Update review | `{ success }` |
| `/api/v1/country-hr/performance/reviews/{id}/submit` | POST | Submit review | `{ success }` |
| `/api/v1/country-hr/performance/goals` | GET | Get goals | `Goal[]` |
| `/api/v1/country-hr/performance/goals` | POST | Create goal | `{ success, goalId }` |
| `/api/v1/country-hr/performance/goals/{id}` | PUT | Update goal progress | `{ success }` |
| **Training & Development** |
| `/api/v1/country-hr/training/programs` | GET | Get training programs | `TrainingProgram[]` |
| `/api/v1/country-hr/training/programs` | POST | Create program | `{ success, programId }` |
| `/api/v1/country-hr/training/enrollments` | GET | Get enrollments | `Enrollment[]` |
| `/api/v1/country-hr/training/enroll` | POST | Enroll employee | `{ success }` |
| `/api/v1/country-hr/training/progress/{id}` | PUT | Update progress | `{ success }` |
| `/api/v1/country-hr/training/certifications` | GET | Get certifications | `Certification[]` |
| **Leave & Attendance** |
| `/api/v1/country-hr/leave/requests` | GET | Get leave requests | `LeaveRequest[]` |
| `/api/v1/country-hr/leave/requests` | POST | Create leave request | `{ success, requestId }` |
| `/api/v1/country-hr/leave/requests/{id}/approve` | POST | Approve leave | `{ success }` |
| `/api/v1/country-hr/leave/requests/{id}/reject` | POST | Reject leave | `{ success }` |
| `/api/v1/country-hr/leave/balance` | GET | Get leave balances | `LeaveBalance[]` |
| `/api/v1/country-hr/attendance/today` | GET | Get today's attendance | `AttendanceSummary` |
| `/api/v1/country-hr/attendance/timesheet` | GET | Get timesheets | `Timesheet[]` |
| `/api/v1/country-hr/attendance/timesheet/{id}/approve` | POST | Approve timesheet | `{ success }` |
| **Compliance** |
| `/api/v1/country-hr/compliance/status` | GET | Get compliance status | `ComplianceStatus` |
| `/api/v1/country-hr/compliance/policies` | GET | Get policies | `Policy[]` |
| `/api/v1/country-hr/compliance/acknowledgments` | GET | Get acknowledgments | `Acknowledgment[]` |
| `/api/v1/country-hr/compliance/audit-log` | GET | Get audit logs | `AuditLogEntry[]` |
| **Reporting to HQ** |
| `/api/v1/country-hr/reports/daily` | POST | Submit daily report | `{ success }` |
| `/api/v1/country-hr/reports/weekly` | POST | Submit weekly report | `{ success }` |
| `/api/v1/country-hr/reports/monthly` | POST | Submit monthly report | `{ success }` |

---

## 2. API FLOW DIAGRAMS

### 2.1 Initial Load Flow

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                         INITIAL APPLICATION LOAD                           │
└─────────────────────────────────────────────────────────────────────────────┘

User opens app
      │
      ▼
┌─────────────────────┐
│ Check localStorage │
│ for authToken      │
└─────┬──────────────┘
      │
      ├─ Token exists ──▶ ┌─────────────────────┐
      │                   │ Validate token with │
      │                   │ GET /api/v1/auth/me│
      │                   └──────┬──┘
      │                          │
      │                   ┌──────┴──────────┐
      │                   │                 │
      │               Valid             Invalid
      │                   │                 │
      │                   ▼                 ▼
      │            ┌──────────┐    ┌──────────┐
      │            │ Load user│    │ Redirect │
      │            │ data &   │    │ to login │
      │            │ country  │    └──────────┘
      │            └─────┬────┘
      │                  │
      ▼                  ▼
┌─────────────────────────────────────────┐
│ Load Dashboard Data (Parallel)          │
├─────────────────────────────────────────┤
│  1. GET /api/v1/country-hr/dashboard/overview│
│  2. GET /api/v1/country-hr/employees     │
│  3. GET /api/v1/country-hr/payroll/summary│
│  4. GET /api/v1/country-hr/recruitment/active│
│  5. GET /api/v1/country-hr/leave/pending│
│  6. GET /api/v1/country-hr/compliance/status│
└─────────────────────────────────────────┘
      │
      ▼
┌─────────────────────────────────────────┐
│ Populate Zustand Store                  │
├─────────────────────────────────────────┤
│  • setDashboardData(data)              │
│  • setEmployees(employees)              │
│  • setPayrollSummary(payroll)           │
│  • setRecruitmentData(recruitment)      │
│  • setLeaveRequests(requests)           │
│  • setComplianceStatus(compliance)      │
└─────────────────────────────────────────┘
      │
      ▼
┌─────────────────────────────────────────┐
│ Render Dashboard                       │
│  • Show loading skeletons during load  │
│  • Show error state on failure         │
│  • Show data on success                │
└─────────────────────────────────────────┘
      │
      ▼
┌─────────────────────────────────────────┐
│ Establish WebSocket Connection          │
│  • Subscribe to employee updates       │
│  • Subscribe to payroll status updates │
│  • Subscribe to recruitment updates    │
│  • Subscribe to leave request updates │
│  • Subscribe to compliance alerts      │
└─────────────────────────────────────────┘
```

### 2.2 Employee Data Flow

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                    EMPLOYEE DATA FLOW                                     │
└─────────────────────────────────────────────────────────────────────────────┘

User clicks "Employees" in sidebar
      │
      ▼
┌─────────────────────────────────────────┐
│ Load Employee Directory                   │
│  GET /api/v1/country-hr/employees        │
│  Query: page=1, limit=50, country=NGA    │
└─────────────────────────────────────────┘
      │
      ▼
┌─────────────────────────────────────────┐
│ Display Employee List                    │
│  • Employee cards with key info          │
│  • Pagination controls                  │
│  • Filter options                       │
│  • Search functionality                 │
└─────────────────────────────────────────┘
      │
      ▼
User clicks on employee
      │
      ▼
┌─────────────────────────────────────────┐
│ Load Employee Detail                     │
│  GET /api/v1/country-hr/employees/{id}  │
└─────────────────────────────────────────┘
      │
      ▼
┌─────────────────────────────────────────┐
│ Display Employee Detail                 │
│  • Profile information                  │
│  • Employment details                  │
│  • Compensation & benefits              │
│  • Performance history                 │
│  • Training records                    │
│  • Leave balance                       │
└─────────────────────────────────────────┘
```

### 2.3 Payroll Processing Flow

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                    PAYROLL PROCESSING FLOW                                │
└─────────────────────────────────────────────────────────────────────────────┘

User clicks "Run Payroll"
      │
      ▼
┌─────────────────────────────────────────┐
│ Initiate Payroll Run                     │
│  POST /api/v1/country-hr/payroll/run    │
│  Request: { period, country }           │
└─────────────────────────────────────────┘
      │
      ▼
┌─────────────────────────────────────────┐
│ Backend Processing                      │
│  1. Fetch all active employees          │
│  2. Calculate gross pay                 │
│  3. Apply deductions                    │
│  4. Calculate net pay                   │
│  5. Generate payslips                   │
│  6. Prepare payment file                │
└─────────────────────────────────────────┘
      │
      ▼
┌─────────────────────────────────────────┐
│ Update Progress via WebSocket            │
│  ws://api/country-hr/ws/payroll/{runId} │
│  • Percentage complete                  │
│  • Current step                         │
│  • Status updates                       │
└─────────────────────────────────────────┘
      │
      ▼
┌─────────────────────────────────────────┐
│ Payroll Ready for Approval               │
│  • Display summary                      │
│  • Show payslips preview                │
│  • [Approve] [Reject] buttons           │
└─────────────────────────────────────────┘
      │
      ├─ Click Approve ──▶ POST /api/v1/country-hr/payroll/{runId}/approve
      │                      • Process payments
      │                      • Distribute payslips
      │                      • Update employee records
      │                      • Submit report to HQ
      │
      └─ Click Reject ───▶ POST /api/v1/country-hr/payroll/{runId}/reject
                          • Cancel payroll run
                          • Notify HR team
```

### 2.4 HQ Reporting Flow

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                    HQ REPORTING FLOW                                     │
└─────────────────────────────────────────────────────────────────────────────┘

Scheduled report time (daily/weekly/monthly)
      │
      ▼
┌─────────────────────────────────────────┐
│ Collect HR Data                         │
│  • Employee headcount                   │
│  • Payroll summaries                    │
│  • Recruitment metrics                  │
│  • Performance data                     │
│  • Training completion                  │
│  • Leave utilization                    │
│  • Compliance status                    │
└─────────────────────────────────────────┘
      │
      ▼
┌─────────────────────────────────────────┐
│ Aggregate and Calculate                 │
│  • KPIs and metrics                     │
│  • Trends and comparisons               │
│  • Visualizations                       │
└─────────────────────────────────────────┘
      │
      ▼
┌─────────────────────────────────────────┐
│ Generate Report                         │
│  POST /api/v1/country-hr/reports/daily  │
│  POST /api/v1/country-hr/reports/weekly │
│  POST /api/v1/country-hr/reports/monthly│
└─────────────────────────────────────────┘
      │
      ▼
┌─────────────────────────────────────────┐
│ Submit to HQ HR Dashboard               │
│  POST /api/v1/hq-hr/data/aggregation    │
│  Request: {                             │
│    country: "NGA",                     │
│    period: "2026-02",                  │
│    employeeCount: 347,                 │
│    payrollAmount: 127500000,           │
│    hiredThisMonth: 8,                  │
│    leftThisMonth: 3,                   │
│    performanceReviewsCompleted: 245,   │
│    trainingCompletionRate: 78,         │
│    leaveUtilization: 65,               │
│    complianceScore: 94                 │
│  }                                     │
└─────────────────────────────────────────┘
```

---

## 3. MOCK DATA DEFINITIONS

### 3.1 TypeScript Interfaces

```typescript
// ============================================
// CORE TYPES
// ============================================

interface CountryHRUser {
  id: string;
  email: string;
  firstName: string;
  lastName: string;
  avatar?: string;
  role: CountryHRRole;
  assignedCountry: AssignedCountry;
  department?: string;
  location: string;
}

type CountryHRRole =
  | 'COUNTRY_HR_MANAGER'
  | 'HR_MANAGER'
  | 'HR_SPECIALIST';

interface AssignedCountry {
  countryCode: string;
  countryName: string;
  currency: string;
  flag: string;
  activeEmployees: number;
}

// ============================================
// DASHBOARD TYPES
// ============================================

interface DashboardOverviewDTO {
  country: string;
  hrHealth: HRHealthScore;
  employeeStats: EmployeeStats;
  payrollSummary: PayrollSummary;
  recruitmentMetrics: RecruitmentMetrics;
  performanceStats: PerformanceStats;
  trainingMetrics: TrainingMetrics;
  leaveStats: LeaveStats;
  complianceStatus: ComplianceStatus;
  activeAlerts: HRAlert[];
}

interface HRHealthScore {
  score: number;
  trend: number;
  status: 'HEALTHY' | 'WARNING' | 'CRITICAL';
  breakdown: {
    staffing: number;
    payroll: number;
    performance: number;
    training: number;
    compliance: number;
  };
}

interface EmployeeStats {
  totalEmployees: number;
  activeThisMonth: number;
  onLeaveToday: number;
  newHiresThisMonth: number;
  terminationsThisMonth: number;
  departmentBreakdown: DepartmentStats[];
}

interface DepartmentStats {
  department: string;
  count: number;
  percentage: number;
}

interface PayrollSummary {
  period: string;
  status: 'PROCESSING' | 'READY' | 'COMPLETED' | 'FAILED';
  totalEmployees: number;
  activePayslips: number;
  grossPay: number;
  netPay: number;
  currency: string;
  payDate: Date;
}

// ============================================
// EMPLOYEE TYPES
// ============================================

interface EmployeeSummary {
  totalEmployees: number;
  activeEmployees: number;
  onLeave: number;
  newHiresThisMonth: number;
  departmentBreakdown: DepartmentStats[];
}

interface Employee {
  id: string;
  employeeId: string;
  firstName: string;
  lastName: string;
  fullName: string;
  email: string;
  phone: string;
  avatar?: string;
  status: EmployeeStatus;
  employmentType: EmploymentType;
  department: Department;
  location: EmployeeLocation;
  workLocation: WorkLocation;
  manager?: string;
  managerId?: string;
  startDate: Date;
  endDate?: Date;
}

type EmployeeStatus = 'ACTIVE' | 'ON_LEAVE' | 'SUSPENDED' | 'TERMINATED';
type EmploymentType = 'FULL_TIME' | 'PART_TIME' | 'CONTRACT' | 'INTERN';

type Department =
  | 'EXECUTIVE'
  | 'SALES'
  | 'OPERATIONS'
  | 'FINANCE'
  | 'HR'
  | 'TECHNOLOGY'
  | 'MARKETING'
  | 'CUSTOMER_SUPPORT';

interface EmployeeLocation {
  country: string;
  state: string;
  city: string;
  address: string;
}

type WorkLocation = 'ONSITE' | 'REMOTE' | 'HYBRID';

interface EmployeeDetail {
  employee: Employee;
  employment: EmploymentDetails;
  compensation: Compensation;
  benefits: Benefits;
  performance: PerformanceSummary;
  training: TrainingSummary;
  leaveBalance: LeaveBalanceSummary;
  documents: EmployeeDocument[];
}

interface EmploymentDetails {
  jobTitle: string;
  department: Department;
  employmentType: EmploymentType;
  startDate: Date;
  endDate?: Date;
  reportingTo: string;
  reportingToId: string;
  workLocation: WorkLocation;
  workLocationDetail: string;
}

interface Compensation {
  salary: number;
  currency: string;
  payFrequency: 'MONTHLY' | 'BI_WEEKLY' | 'WEEKLY';
  lastReviewDate: Date;
  nextReviewDate: Date;
  salaryHistory: SalaryChange[];
}

interface Benefits {
  healthInsurance: boolean;
  pension: boolean;
  hmoPlan: boolean;
  lifeInsurance: boolean;
  stockOptions: boolean;
  otherBenefits: string[];
}

interface PerformanceSummary {
  overallRating: number;
  lastReviewDate: Date;
  lastReviewScore: number;
  goalsCompleted: number;
  goalsTotal: number;
  reviewHistory: PerformanceReview[];
}

interface TrainingSummary {
  coursesCompleted: number;
  coursesInProgress: number;
  certifications: Certification[];
  enrolledPrograms: TrainingEnrollment[];
}

interface LeaveBalanceSummary {
  annualLeave: {
    used: number;
    balance: number;
    total: number;
  };
  sickLeave: {
    used: number;
    balance: number;
    total: number;
  };
  otherLeave: LeaveTypeBalance[];
}

// ============================================
// PAYROLL TYPES
// ============================================

interface Payslip {
  id: string;
  employeeId: string;
  employeeName: string;
  period: string;
  grossPay: number;
  deductions: Deductions;
  netPay: number;
  currency: string;
  status: 'GENERATED' | 'SENT' | 'FAILED';
  generatedDate: Date;
  sentDate?: Date;
}

interface Deductions {
  tax: number;
  pension: number;
  healthInsurance: number;
  lifeInsurance: number;
  other: number;
  total: number;
}

interface SalaryData {
  employeeId: string;
  salary: number;
  currency: string;
  effectiveDate: Date;
  reason?: string;
}

// ============================================
// RECRUITMENT TYPES
// ============================================

interface JobPosting {
  id: string;
  title: string;
  department: Department;
  location: string;
  employmentType: EmploymentType;
  status: 'DRAFT' | 'ACTIVE' | 'PAUSED' | 'CLOSED';
  postedDate: Date;
  deadline: Date;
  applicantCount: number;
  newApplicants: number;
  pipeline: ApplicantPipeline;
}

interface ApplicantPipeline {
  new: number;
  screened: number;
  interview: number;
  offer: number;
  hired: number;
  rejected: number;
}

interface Applicant {
  id: string;
  name: string;
  email: string;
  phone: string;
  jobId: string;
  jobTitle: string;
  stage: ApplicantStage;
  rating: number;
  matchScore: number;
  source: ApplicantSource;
  appliedDate: Date;
  status: ApplicantStatus;
}

type ApplicantStage =
  | 'NEW'
  | 'SCREENED'
  | 'INTERVIEW'
  | 'OFFER'
  | 'HIRED'
  | 'REJECTED';

type ApplicantSource =
  | 'LINKEDIN'
  | 'WEBSITE'
  | 'REFERRAL'
  | 'AGENCY'
  | 'OTHER';

interface Interview {
  id: string;
  applicantId: string;
  applicantName: string;
  jobId: string;
  jobTitle: string;
  type: InterviewType;
  scheduledDate: Date;
  duration: number;
  interviewer: string;
  interviewerId: string;
  status: InterviewStatus;
  feedback?: string;
  rating?: number;
}

type InterviewType = 'PHONE' | 'VIDEO' | 'ONSITE' | 'TECHNICAL';
type InterviewStatus = 'SCHEDULED' | 'COMPLETED' | 'CANCELLED' | 'NO_SHOW';

// ============================================
// PERFORMANCE TYPES
// ============================================

interface PerformanceReview {
  id: string;
  employeeId: string;
  employeeName: string;
  reviewerId: string;
  reviewerName: string;
  period: string;
  status: ReviewStatus;
  overallRating?: number;
  goalAchievement?: number;
  submissionDate?: Date;
  completionDate?: Date;
}

type ReviewStatus = 'PENDING' | 'IN_PROGRESS' | 'SUBMITTED' | 'COMPLETED';

interface PerformanceReviewDetail {
  review: PerformanceReview;
  goals: ReviewGoal[];
  competencies: CompetencyRating[];
  overallRating: number;
  recommendations: Recommendation[];
  developmentGoals: DevelopmentGoal[];
  comments: string;
}

interface ReviewGoal {
  id: string;
  title: string;
  description: string;
  weight: number;
  target: string;
  achievement: number;
  rating: number;
}

interface CompetencyRating {
  name: string;
  rating: number;
  weight: number;
}

interface Recommendation {
  type: 'PROMOTION' | 'BONUS' | 'TRANSFER' | 'PIP';
  approved: boolean;
}

interface DevelopmentGoal {
  title: string;
  description: string;
  targetDate: Date;
  status: 'ACTIVE' | 'COMPLETED' | 'CANCELLED';
}

// ============================================
// TRAINING TYPES
// ============================================

interface TrainingProgram {
  id: string;
  title: string;
  description: string;
  type: TrainingType;
  format: TrainingFormat;
  status: 'DRAFT' | 'ACTIVE' | 'COMPLETED' | 'CANCELLED';
  startDate: Date;
  endDate: Date;
  capacity: number;
  enrolled: number;
  completed: number;
  instructor: string;
  location?: string;
}

type TrainingType = 'ONBOARDING' | 'TECHNICAL' | 'LEADERSHIP' | 'COMPLIANCE' | 'SAFETY';
type TrainingFormat = 'ONLINE' | 'IN_PERSON' | 'HYBRID' | 'SELF_PACED';

interface TrainingEnrollment {
  id: string;
  employeeId: string;
  employeeName: string;
  programId: string;
  programTitle: string;
  enrollmentDate: Date;
  progress: number;
  status: 'ENROLLED' | 'IN_PROGRESS' | 'COMPLETED' | 'DROPPED';
  completionDate?: Date;
  certificateUrl?: string;
}

interface Certification {
  id: string;
  name: string;
  issuedBy: string;
  issueDate: Date;
  expiryDate?: Date;
  status: 'ACTIVE' | 'EXPIRED' | 'PENDING';
  certificateUrl?: string;
}

// ============================================
// LEAVE TYPES
// ============================================

interface LeaveRequest {
  id: string;
  employeeId: string;
  employeeName: string;
  type: LeaveType;
  startDate: Date;
  endDate: Date;
  days: number;
  reason: string;
  status: LeaveStatus;
  requestedDate: Date;
  approvedBy?: string;
  approvedDate?: Date;
}

type LeaveType =
  | 'ANNUAL'
  | 'SICK'
  | 'MATERNITY'
  | 'PATERNITY'
  | 'STUDY'
  | 'COMPASSIONATE'
  | 'UNPAID';

type LeaveStatus = 'PENDING' | 'APPROVED' | 'REJECTED' | 'CANCELLED';

interface LeaveBalance {
  employeeId: string;
  annualLeave: number;
  sickLeave: number;
  maternityLeave: number;
  paternityLeave: number;
  studyLeave: number;
  usedThisYear: LeaveUsage[];
}

interface LeaveUsage {
  type: LeaveType;
  used: number;
  remaining: number;
}

interface AttendanceSummary {
  date: Date;
  present: number;
  onLeave: number;
  absent: number;
  remote: number;
  total: number;
  attendanceRate: number;
}

// ============================================
// COMPLIANCE TYPES
// ============================================

interface ComplianceStatus {
  overallScore: number;
  status: 'COMPLIANT' | 'REVIEW' | 'NON_COMPLIANT';
  categories: ComplianceCategory[];
  pendingAcknowledgments: number;
  expiringDocuments: number;
}

interface ComplianceCategory {
  name: string;
  status: 'CLEAR' | 'REVIEW' | 'ISSUE';
  lastAudit: Date;
  nextAudit: Date;
}

interface Policy {
  id: string;
  title: string;
  version: string;
  effectiveDate: Date;
  category: string;
  acknowledgementRequired: boolean;
  status: 'ACTIVE' | 'DRAFT' | 'RETIRED';
}

interface Acknowledgment {
  id: string;
  policyId: string;
  policyTitle: string;
  employeeId: string;
  employeeName: string;
  status: 'PENDING' | 'ACKNOWLEDGED';
  dueDate: Date;
  acknowledgedDate?: Date;
}

interface AuditLogEntry {
  id: string;
  timestamp: Date;
  userId: string;
  userName: string;
  action: string;
  category: AuditCategory;
  details: string;
  ipAddress?: string;
}

type AuditCategory =
  | 'EMPLOYEE'
  | 'PAYROLL'
  | 'RECRUITMENT'
  | 'PERFORMANCE'
  | 'TRAINING'
  | 'LEAVE'
  | 'COMPLIANCE'
  | 'SYSTEM';

// ============================================
// ALERT TYPES
// ============================================

interface HRAlert {
  id: string;
  type: HRAlertType;
  severity: 'INFO' | 'WARNING' | 'URGENT' | 'CRITICAL';
  title: string;
  description: string;
  affectedEmployees?: number;
  actionRequired: boolean;
  dueDate?: Date;
  createdAt: Date;
}

type HRAlertType =
  | 'ONBOARDING_PENDING'
  | 'PROBATION_ENDING'
  | 'CERTIFICATION_EXPIRING'
  | 'POLICY_ACKNOWLEDGMENT'
  | 'PAYROLL_ISSUE'
  | 'COMPLIANCE_ISSUE'
  | 'PERFORMANCE_REVIEW_DUE'
  | 'TRAINING_OVERDUE';
```

---

## 4. STATE MANAGEMENT FLOW

### 4.1 Zustand Store Structure

```typescript
import { create } from 'zustand';
import { devtools, persist } from 'zustand/middleware';

interface CountryHRStore {
  // User State
  user: CountryHRUser | null;
  setUser: (user: CountryHRUser) => void;
  clearUser: () => void;

  // Assigned Country
  assignedCountry: AssignedCountry | null;
  setAssignedCountry: (country: AssignedCountry) => void;

  // Dashboard Data
  dashboardData: DashboardOverviewDTO | null;
  setDashboardData: (data: DashboardOverviewDTO) => void;

  // Employees
  employees: Employee[];
  setEmployees: (employees: Employee[]) => void;
  addEmployee: (employee: Employee) => void;
  updateEmployee: (id: string, updates: Partial<Employee>) => void;
  removeEmployee: (id: string) => void;

  // Payroll
  payrollSummary: PayrollSummary | null;
  payslips: Payslip[];
  setPayrollSummary: (summary: PayrollSummary) => void;
  setPayslips: (payslips: Payslip[]) => void;

  // Recruitment
  jobPostings: JobPosting[];
  applicants: Applicant[];
  setJobPostings: (jobs: JobPosting[]) => void;
  setApplicants: (applicants: Applicant[]) => void;

  // Performance
  performanceReviews: PerformanceReview[];
  setPerformanceReviews: (reviews: PerformanceReview[]) => void;

  // Training
  trainingPrograms: TrainingProgram[];
  enrollments: TrainingEnrollment[];
  setTrainingPrograms: (programs: TrainingProgram[]) => void;
  setEnrollments: (enrollments: TrainingEnrollment[]) => void;

  // Leave
  leaveRequests: LeaveRequest[];
  setLeaveRequests: (requests: LeaveRequest[]) => void;
  updateLeaveRequest: (id: string, status: LeaveStatus) => void;

  // Compliance
  complianceStatus: ComplianceStatus | null;
  setComplianceStatus: (status: ComplianceStatus) => void;

  // Loading States
  isLoading: boolean;
  setLoading: (loading: boolean) => void;

  // Error State
  error: string | null;
  setError: (error: string | null) => void;
}

export const useCountryHRStore = create<CountryHRStore>()(
  devtools(
    persist(
      (set) => ({
        // Initial State
        user: null,
        assignedCountry: null,
        dashboardData: null,
        employees: [],
        payrollSummary: null,
        payslips: [],
        jobPostings: [],
        applicants: [],
        performanceReviews: [],
        trainingPrograms: [],
        enrollments: [],
        leaveRequests: [],
        complianceStatus: null,
        isLoading: false,
        error: null,

        // User Actions
        setUser: (user) => set({ user }),
        clearUser: () => set({ user: null, assignedCountry: null }),

        // Country Actions
        setAssignedCountry: (country) => set({ assignedCountry: country }),

        // Dashboard Actions
        setDashboardData: (data) => set({ dashboardData: data }),

        // Employee Actions
        setEmployees: (employees) => set({ employees }),
        addEmployee: (employee) => set((state) => ({
          employees: [...state.employees, employee]
        })),
        updateEmployee: (id, updates) => set((state) => ({
          employees: state.employees.map(emp =>
            emp.id === id ? { ...emp, ...updates } : emp
          )
        })),
        removeEmployee: (id) => set((state) => ({
          employees: state.employees.filter(emp => emp.id !== id)
        })),

        // Payroll Actions
        setPayrollSummary: (summary) => set({ payrollSummary: summary }),
        setPayslips: (payslips) => set({ payslips }),

        // Recruitment Actions
        setJobPostings: (jobs) => set({ jobPostings: jobs }),
        setApplicants: (applicants) => set({ applicants: applicants }),

        // Performance Actions
        setPerformanceReviews: (reviews) => set({ performanceReviews: reviews }),

        // Training Actions
        setTrainingPrograms: (programs) => set({ trainingPrograms: programs }),
        setEnrollments: (enrollments) => set({ enrollments: enrollments }),

        // Leave Actions
        setLeaveRequests: (requests) => set({ leaveRequests: requests }),
        updateLeaveRequest: (id, status) => set((state) => ({
          leaveRequests: state.leaveRequests.map(req =>
            req.id === id ? { ...req, status } : req
          )
        })),

        // Compliance Actions
        setComplianceStatus: (status) => set({ complianceStatus: status }),

        // UI State Actions
        setLoading: (loading) => set({ isLoading: loading }),
        setError: (error) => set({ error }),
      }),
      {
        name: 'country-hr-storage',
        partialize: (state) => ({
          user: state.user,
          assignedCountry: state.assignedCountry,
        }),
      }
    )
  )
);
```

---

## 5. WEBSOCKET REAL-TIME FLOW

### 5.1 WebSocket Connection

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                    WEBSOCKET CONNECTION SETUP                            │
└─────────────────────────────────────────────────────────────────────────────┘

After successful authentication
      │
      ▼
┌─────────────────────────────────────────┐
│ Establish WebSocket Connection            │
│ const ws = new WebSocket(                │
│   `ws://api/country-hr/ws?token=${token}` │
│ );                                        │
└─────────────────────────────────────────┘
      │
      ▼
┌─────────────────────────────────────────┐
│ Subscribe to Topics                      │
│ ws.send(JSON.stringify({                │
│   action: 'SUBSCRIBE',                  │
│   topics: [                             │
│     '/topic/employees',                 │
│     '/topic/payroll',                   │
│     '/topic/recruitment',               │
│     '/topic/performance',               │
│     '/topic/leave',                     │
│     '/topic/compliance'                 │
│   ]                                     │
│ }));                                    │
└─────────────────────────────────────────┘
```

### 5.2 Real-Time Event Handling

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                    REAL-TIME EVENT HANDLING                              │
└─────────────────────────────────────────────────────────────────────────────┘

WebSocket message received
      │
      ▼
┌─────────────────────────────────────────┐
│ Parse Message                            │
│ {                                        │
│   topic: '/topic/employees',            │
│   event: 'employee.updated',            │
│   data: { ... }                         │
│ }                                        │
└─────────────────────────────────────────┘
      │
      ▼
┌─────────────────────────────────────────┐
│ Route to Handler                         │
│ switch(message.topic) {                 │
│   case '/topic/employees':              │
│     handleEmployeeEvent(message.data)   │
│     break                                │
│   case '/topic/payroll':                │
│     handlePayrollEvent(message.data)    │
│     break                                │
│   case '/topic/recruitment':            │
│     handleRecruitmentEvent(message.data)│
│     break                                │
│   case '/topic/performance':            │
│     handlePerformanceEvent(message.data)│
│     break                                │
│   case '/topic/leave':                  │
│     handleLeaveEvent(message.data)      │
│     break                                │
│   case '/topic/compliance':             │
│     handleComplianceEvent(message.data) │
│     break                                │
│ }                                        │
└─────────────────────────────────────────┘
      │
      ▼
┌─────────────────────────────────────────┐
│ Update Zustand Store                     │
│ • Update relevant state                 │
│ • Trigger UI re-render                  │
│ • Show notification if needed           │
└─────────────────────────────────────────┘
```

---

## 6. ERROR HANDLING FLOW

### 6.1 Error Response Structure

```typescript
interface APIError {
  success: false;
  error: {
    code: string;
    message: string;
    details?: any;
    timestamp: Date;
  };
}

type ErrorCode =
  | 'AUTH_FAILED'
  | 'UNAUTHORIZED'
  | 'FORBIDDEN'
  | 'NOT_FOUND'
  | 'VALIDATION_ERROR'
  | 'DUPLICATE_RECORD'
  | 'PAYROLL_PROCESSING'
  | 'COMPLIANCE_VIOLATION'
  | 'SERVER_ERROR';
```

### 6.2 Error Handling Flow

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                    ERROR HANDLING FLOW                                   │
└─────────────────────────────────────────────────────────────────────────────┘

API Call Failed
      │
      ▼
┌─────────────────────────────────────────┐
│ Check Error Type                         │
│ switch(error.response?.status) {        │
│   case 401:                             │
│     // Unauthorized - Clear token       │
│     clearUser()                         │
│     navigate('/login')                  │
│     break                                │
│   case 403:                             │
│     // Forbidden - Show permission error│
│     setError("You don't have permission")│
│     break                                │
│   case 404:                             │
│     // Not Found - Show not found       │
│     setError("Resource not found")       │
│     break                                │
│   case 409:                             │
│     // Conflict - Duplicate record      │
│     setError(error.response.data.message)│
│     break                                │
│   case 422:                             │
│     // Validation Error                 │
│     showValidationErrors(error.data)     │
│     break                                │
│   default:                               │
│     // Server Error                     │
│     setError("Something went wrong")     │
│ }                                        │
└─────────────────────────────────────────┘
```

---

**End of Mock Flow Documentation v1.0**

**Next:** [04_Page_By_Page_Flow_Documentation.md](./04_Page_By_Page_Flow_Documentation.md) - Detailed page flows
