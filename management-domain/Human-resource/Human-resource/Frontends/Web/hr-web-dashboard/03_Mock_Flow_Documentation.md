# HQ-HR-DASHBOARD - MOCK FLOW DOCUMENTATION

**Domain:** Management Domain (Human Resource)
**Application:** HQ HR Web Dashboard
**Version:** 1.0
**Date:** 2026-02-08
**Path:** `Management-domain/Human-resource/Frontends/Web/hr-web-dashboard/`

---

## TABLE OF CONTENTS

1. [Mock Data Overview](#mock-data-overview)
2. [API Flow Diagrams](#api-flow-diagrams)
3. [Mock Data Definitions](#mock-data-definitions)
4. [State Management Flow](#state-management-flow)
5. [WebSocket Real-Time Flow](#websocket-real-time-flow)
6. [Cross-Country Data Aggregation](#cross-country-data-aggregation)

---

## 1. MOCK DATA OVERVIEW

### 1.1 Data Flow Architecture

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                      HQ-HR-DASHBOARD DATA FLOW                             │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│  ┌─────────────┐     ┌─────────────┐     ┌─────────────┐                 │
│  │   Browser   │────▶│   API       │────▶│  Backend    │                 │
│  │   (React)   │◀────│  Gateway    │◀────│  Services   │                 │
│  └──────┬──────┘     └──────┬──────┘     └──────┬──────┘                 │
│         │                   │                   │                          │
│         │                   ▼                   ▼                          │
│  ┌─────────────┐     ┌─────────────┐     ┌─────────────┐                 │
│  │  Zustand    │     │    Redis    │     │  MongoDB    │                 │
│  │  Store      │     │   Cache     │     │  Database   │                 │
│  └──────┬──────┘     └──────┬──────┘     └──────┬──────┘                 │
│         │                   │                   │                          │
│         ▼                   ▼                   ▼                          │
│  ┌─────────────────────────────────────────────────────────────┐    │
│  │                    Kafka Events                            │    │
│  │  ┌───────────────────────────────────────────────────────┐   │    │
│  │  │ /topic/hr/nigeria → Nigeria HR data                   │   │    │
│  │  │ /topic/hr/kenya → Kenya HR data                         │   │    │
│  │  │ /topic/hr/south-africa → South Africa HR data        │   │    │
│  │  │ /topic/hr/ireland → Ireland HR data                    │   │    │
│  │  └───────────────────────────────────────────────────────┘   │    │
│  └─────────────────────────────────────────────────────────────┘    │
│                              │                                          │                    │
│                              ▼                                          │                    │
│  ┌─────────────────────────────────────────────────────────────┐    │
│  │  WebSocket Broadcast → HQ Dashboard Updates                 │    │
│  └─────────────────────────────────────────────────────────────┘    │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

### 1.2 API Endpoints Summary

| Endpoint | Method | Purpose | Response Type |
|----------|--------|---------|---------------|
| **Authentication** |
| `/api/v1/hq-hr/auth/login` | POST | Authenticate user | `{ token, user, role, accessibleCountries }` |
| `/api/v1/hq-hr/auth/logout` | POST | Logout user | `{ success }` |
| `/api/v1/hq-hr/auth/me` | GET | Get current user | `{ user, role, permissions }` |
| **Global Dashboard** |
| `/api/v1/hq-hr/dashboard/overview` | GET | Get global dashboard | `GlobalDashboardOverviewDTO` |
| `/api/v1/hq-hr/dashboard/countries` | GET | Get all countries summary | `CountrySummary[]` |
| **Country Data** |
| `/api/v1/hq-hr/countries/{countryCode}` | GET | Get country detail | `CountryDetailDTO` |
| `/api/v1/hq-hr/countries/{countryCode}/employees` | GET | Get country employees | `CountryEmployeeData` |
| `/api/v1/hq-hr/countries/{countryCode}/payroll` | GET | Get country payroll | `CountryPayrollData` |
| `/api/v1/hq-hr/countries/{countryCode}/recruitment` | GET | Get country recruitment | `CountryRecruitmentData` |
| `/api/v1/hq-hr/countries/{countryCode}/performance` | GET | Get country performance | `CountryPerformanceData` |
| **Global Recruitment** |
| `/api/v1/hq-hr/recruitment/global-jobs` | GET | Get all global job postings | `GlobalJobPosting[]` |
| `/api/v1/hq-hr/recruitment/global-pipeline` | GET | Get global applicant pipeline | `GlobalPipelineDTO` |
| `/api/v1/hq-hr/recruitment/metrics` | GET | Get recruitment metrics by country | `RecruitmentMetrics[]` |
| **Global Payroll** |
| `/api/v1/hq-hr/payroll/global-summary` | GET | Get global payroll summary | `GlobalPayrollSummary` |
| `/api/v1/hq-hr/payroll/by-country` | GET | Get payroll breakdown by country | `PayrollByCountry[]` |
| `/api/v1/hq-hr/payroll/salary-benchmark` | GET | Get salary benchmarks | `SalaryBenchmarkDTO` |
| **Global Performance** |
| `/api/v1/hq-hr/performance/global-status` | GET | Get global performance status | `GlobalPerformanceStatus` |
| `/api/v1/hq-hr/performance/benchmarks` | GET | Get country performance benchmarks | `PerformanceBenchmark[]` |
| `/api/v1/hq-hr/performance/succession` | GET | Get succession pipeline | `SuccessionPipelineDTO` |
| **Analytics** |
| `/api/v1/hq-hr/analytics/workforce-planning` | GET | Get workforce planning data | `WorkforcePlanningDTO` |
| `/api/v1/hq-hr/analytics/retention` | GET | Get retention analysis | `RetentionAnalysisDTO` |
| `/api/v1/hq-hr/analytics/diversity` | GET | Get diversity metrics | `DiversityMetricsDTO` |
| **Reports** |
| `/api/v1/hq-hr/reports/generate` | POST | Generate global report | `{ success, reportUrl }` |
| `/api/v1/hq-hr/reports/schedule` | POST | Schedule report | `{ success, scheduleId }` |
| `/api/v1/hq-hr/reports/list` | GET | Get reports list | `Report[]` |

---

## 2. API FLOW DIAGRAMS

### 2.1 Global Dashboard Load Flow

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                    GLOBAL DASHBOARD LOAD                               │
└─────────────────────────────────────────────────────────────────────────────┘

User opens HQ HR Dashboard → Navigates to /overview
      │
      ▼
┌─────────────────────────────────────────────────────────────────────┐
│  API CALLS (Parallel)                                                  │
│  • GET /api/v1/hq-hr/dashboard/overview                             │
│  • GET /api/v1/hq-hr/dashboard/countries                             │
│  • GET /api/v1/hq-hr/recruitment/global-pipeline                      │
│  • GET /api/v1/hq-hr/payroll/global-summary                          │
│  • GET /api/v1/hq-hr/performance/global-status                        │
└─────────────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────────────┐
│  AGGREGATE DATA                                                       │
│  • Combine data from all countries                                   │
│  • Calculate global metrics                                           │
│  • Compute comparisons                                               │
│  • Generate insights                                                  │
└─────────────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────────────┐
│  POPULATE ZUSTAND STORE                                                │
│  • setGlobalDashboardData(data)                                     │
│  • setCountriesSummary(countries)                                   │
│  • setGlobalRecruitment(recruitment)                                │
│  • setGlobalPayroll(payroll)                                      │
│  • setGlobalPerformance(performance)                                │
└─────────────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────────────┐
│  RENDER GLOBAL OVERVIEW                                               │
│  • Display global health score                                         │
│  • Show country comparison cards                                       │
│  • Display global metrics                                               │
│  • Show active alerts                                                  │
└─────────────────────────────────────────────────────────────────────┘
```

---

## 3. MOCK DATA DEFINITIONS

### 3.1 TypeScript Interfaces

```typescript
// ============================================
// CORE TYPES
// ============================================

interface HQHRUser {
  id: string;
  email: string;
  firstName: string;
  lastName: string;
  avatar?: string;
  role: HQHRRole;
  permissions: GlobalHRPermission[];
  accessibleCountries: string[];
}

type HQHRRole =
  | 'CHIEF_HR_OFFICER'
  | 'HR_VICE_PRESIDENT'
  | 'HR_DIRECTOR'
  | 'HR_ANALYST';

interface GlobalHRPermission {
  resource: string;
  actions: ('view' | 'approve' | 'edit' | 'delete' | 'export')[];
}

// ============================================
// GLOBAL DASHBOARD TYPES
// ============================================

interface GlobalDashboardOverviewDTO {
  totalWorkforce: number;
  hrHealthScore: number;
  healthScoreTrend: number;
  countriesSummary: CountrySummary[];
  globalRecruitment: GlobalRecruitmentSummary;
  globalPayroll: GlobalPayrollSummary;
  globalPerformance: GlobalPerformanceSummary;
  activeAlerts: GlobalAlert[];
  lastUpdated: Date;
}

interface CountrySummary {
  countryCode: string;
  countryName: string;
  flag: string;
  staffCount: number;
  staffTrend: number;
  healthScore: number;
  healthScoreTrend: number;
  payrollAmount: number;
  currency: string;
  performanceCompletion: number;
  trainingCompletion: number;
  newHires: number;
}

interface GlobalRecruitmentSummary {
  totalJobs: number;
  totalApplicants: number;
  globalPipeline: GlobalPipeline;
  hiresThisMonth: number;
  averageTimeToFill: number;
  averageCostPerHire: number;
  byCountry: RecruitmentByCountry[];
}

interface GlobalPipeline {
  new: number;
  screened: number;
  interview: number;
  offer: number;
  hired: number;
  rejected: number;
}

interface RecruitmentByCountry {
  countryCode: string;
  countryName: string;
  activeJobs: number;
  applicants: number;
  hires: number;
  timeToFill: number;
}

interface GlobalPayrollSummary {
  totalWorkforce: number;
  monthlyPayroll: number;
  currency: string;
  byCountry: PayrollByCountry[];
  status: 'ALL_COMPLETE' | 'IN_PROGRESS' | 'PARTIAL';
  lastProcessed: Date;
}

interface PayrollByCountry {
  countryCode: string;
  countryName: string;
  staffCount: number;
  payrollAmount: number;
  currency: string;
  status: 'PROCESSED' | 'PROCESSING' | 'PENDING';
}

interface GlobalPerformanceSummary {
  totalReviews: number;
  completedReviews: number;
  completionRate: number;
  averageRating: number;
  byCountry: PerformanceByCountry[];
}

interface PerformanceByCountry {
  countryCode: string;
  countryName: string;
  targetReviews: number;
  completedReviews: number;
  completionRate: number;
  averageRating: number;
}

// ============================================
// COUNTRY DETAIL TYPES
// ============================================

interface CountryDetailDTO {
  country: CountrySummary;
  employees: CountryEmployeeData;
  payroll: CountryPayrollData;
  recruitment: CountryRecruitmentData;
  performance: CountryPerformanceData;
  training: CountryTrainingData;
  compliance: CountryComplianceData;
  lastUpdated: Date;
}

interface CountryEmployeeData {
  headcount: number;
  newHires: number;
  terminations: number;
  departmentBreakdown: DepartmentHeadcount[];
  demographics: Demographics;
}

interface DepartmentHeadcount {
  department: string;
  count: number;
  percentage: number;
}

interface Demographics {
  genderDistribution: {
    male: number;
    female: number;
    other: number;
  };
  ageDistribution: {
    under25: number;
    age25to34: number;
    age35to44: number;
    age45to54: number;
    over55: number;
  };
}

interface CountryPayrollData {
  monthlyPayroll: number;
  averageSalary: number;
  payrollTrend: PayrollTrendPoint[];
  benefitsCost: number;
}

interface CountryRecruitmentData {
  activeJobs: number;
  totalApplicants: number;
  pipelineStages: PipelineStageCount[];
  timeToFill: number;
  costPerHire: number;
}

interface CountryPerformanceData {
  reviewsTarget: number;
  reviewsCompleted: number;
  completionRate: number;
  averageRating: number;
  goalAchievementRate: number;
}

interface CountryTrainingData {
  programsActive: number;
  enrollments: number;
  completionRate: number;
  certifications: number;
  trainingBudget: number;
}

interface CountryComplianceData {
  overallScore: number;
  policyAcknowledgments: number;
  pendingAcknowledgments: number;
  expiringCertifications: number;
  auditIssues: number;
}

// ============================================
// GLOBAL RECRUITMENT TYPES
// ============================================

interface GlobalJobPosting {
  id: string;
  title: string;
  countries: string[]; // Countries where job is posted
  department: Department;
  location: string;
  employmentType: EmploymentType;
  status: 'ACTIVE' | 'PAUSED' | 'CLOSED';
  totalApplicants: number;
  totalHires: number;
  postedDate: Date;
}

interface GlobalApplicant {
  id: string;
  name: string;
  countryPreference: string[];
  jobId: string;
  jobTitle: string;
  stage: ApplicantStage;
  rating: number;
  source: ApplicantSource;
  appliedDate: Date;
}

// ============================================
// GLOBAL ANALYTICS TYPES
// ============================================

interface WorkforcePlanningDTO {
  currentHeadcount: number;
  projectedGrowth: number;
  hiringNeeds: HiringNeedByCountry[];
  skillsGaps: SkillsGap[];
  recommendations: WorkforceRecommendation[];
}

interface HiringNeedByCountry {
  countryCode: string;
  countryName: string;
  department: string;
  positionsNeeded: number;
  priority: 'HIGH' | 'MEDIUM' | 'LOW';
  targetDate: Date;
}

interface SkillsGap {
  skill: string;
  gapCount: number;
  affectedCountries: string[];
  recommendedTraining: string;
}

interface WorkforceRecommendation {
  type: 'HIRING' | 'TRAINING' | 'REALLOCATION';
  priority: 'HIGH' | 'MEDIUM' | 'LOW';
  description: string;
  affectedCountries: string[];
  estimatedCost: number;
}

interface RetentionAnalysisDTO {
  overallRetentionRate: number;
  retentionByCountry: RetentionByCountry[];
  riskFactors: RetentionRiskFactor[];
  recommendations: RetentionRecommendation[];
}

interface RetentionByCountry {
  countryCode: string;
  countryName: string;
  retentionRate: number;
  trend: number;
  atRiskEmployees: number;
}

interface RetentionRiskFactor {
  factor: string;
  description: string;
  affectedEmployees: number;
  mitigation: string;
}

interface DiversityMetricsDTO {
  overallDiversityScore: number;
  genderDistribution: GenderDistribution;
  regionalDistribution: RegionalDistribution;
  departmentalDiversity: DepartmentalDiversity[];
  leadershipDiversity: LeadershipDiversity;
  inclusionMetrics: InclusionMetrics;
}

interface GenderDistribution {
  executive: GenderBreakdown;
  management: GenderBreakdown;
  overall: GenderBreakdown;
}

interface GenderBreakdown {
  male: number;
  female: number;
  other: number;
}

interface InclusionMetrics {
  inclusionScore: number;
  employeeSatisfaction: number;
  promotionRate: number;
  trainingParticipation: number;
}
```

---

## 4. STATE MANAGEMENT FLOW

### 4.1 Zustand Store Structure

```typescript
import { create } from 'zustand';
import { devtools, persist } from 'zustand/middleware';

interface HQHRStore {
  // User State
  user: HQHRUser | null;
  setUser: (user: HQHRUser) => void;
  clearUser: () => void;

  // Global Dashboard Data
  globalDashboardData: GlobalDashboardOverviewDTO | null;
  setGlobalDashboardData: (data: GlobalDashboardOverviewDTO) => void;

  // Countries Data
  countriesSummary: CountrySummary[];
  setCountriesSummary: (countries: CountrySummary[]) => void;

  // Selected Country
  selectedCountry: string | null;
  setSelectedCountry: (countryCode: string) => void;

  // Country Detail Data
  countryDetail: CountryDetailDTO | null;
  setCountryDetail: (detail: CountryDetailDTO) => void;

  // Global Recruitment
  globalRecruitment: GlobalRecruitmentSummary | null;
  setGlobalRecruitment: (data: GlobalRecruitmentSummary) => void;

  // Global Payroll
  globalPayroll: GlobalPayrollSummary | null;
  setGlobalPayroll: (data: GlobalPayrollSummary) => void;

  // Global Performance
  globalPerformance: GlobalPerformanceSummary | null;
  setGlobalPerformance: (data: GlobalPerformanceSummary) => void;

  // Loading States
  isLoading: boolean;
  setLoading: (loading: boolean) => void;

  // Error State
  error: string | null;
  setError: (error: string | null) => void;
}

export const useHQHRStore = create<HQHRStore>()(
  devtools(
    persist(
      (set) => ({
        // Initial State
        user: null,
        globalDashboardData: null,
        countriesSummary: [],
        selectedCountry: null,
        countryDetail: null,
        globalRecruitment: null,
        globalPayroll: null,
        globalPerformance: null,
        isLoading: false,
        error: null,

        // User Actions
        setUser: (user) => set({ user }),
        clearUser: () => set({ user: null }),

        // Dashboard Actions
        setGlobalDashboardData: (data) => set({ globalDashboardData: data }),
        setCountriesSummary: (countries) => set({ countriesSummary: countries }),

        // Country Selection
        setSelectedCountry: (countryCode) => set({ selectedCountry: countryCode }),
        setCountryDetail: (detail) => set({ countryDetail: detail }),

        // Recruitment Actions
        setGlobalRecruitment: (data) => set({ globalRecruitment: data }),

        // Payroll Actions
        setGlobalPayroll: (data) => set({ globalPayroll: data }),

        // Performance Actions
        setGlobalPerformance: (data) => set({ globalPerformance: data }),

        // UI State Actions
        setLoading: (loading) => set({ isLoading: loading }),
        setError: (error) => set({ error }),
      }),
      {
        name: 'hq-hr-storage',
        partialize: (state) => ({
          user: state.user,
        }),
      }
    )
  )
);
```

---

## 5. WEBSOCKET REAL-TIME FLOW

### 5.1 WebSocket Connection for Multi-Country Data

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                    WEBSOCKET CONNECTION - HQ HR                             │
└─────────────────────────────────────────────────────────────────────────────┘

After successful authentication
      │
      ▼
┌─────────────────────────────────────────┐
│ Establish WebSocket Connection            │
│ const ws = new WebSocket(                │
│   `ws://api/hq-hr/ws?token=${token}` │
│ );                                        │
└─────────────────────────────────────────┘
      │
      ▼
┌─────────────────────────────────────────┐
│ Subscribe to Country Topics               │
│ ws.send(JSON.stringify({                │
│   action: 'SUBSCRIBE',                │
│   topics: [                           │
│     '/topic/hr/nigeria',               │
│     '/topic/hr/kenya',                 │
│     '/topic/hr/south-africa',         │
│     '/topic/hr/ireland',              │
│     '/topic/hr/global'                 │
│   ]                                   │
│ }));                                   │
└─────────────────────────────────────────┘
```

### 5.2 Real-Time Event Handling

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                    MULTI-COUNTRY EVENT HANDLING                           │
└─────────────────────────────────────────────────────────────────────────────┘

Country event received (e.g., Nigeria: Employee hired)
      │
      ▼
┌─────────────────────────────────────────┐
│ Parse Message                            │
│ {                                        │
│   topic: '/topic/hr/nigeria',            │
│   event: 'employee.hired',              │
│   data: {                              │
│     country: 'NGA',                    │
│     employeeCount: 348                  │
│   }                                     │
│ }                                        │
└─────────────────────────────────────────┘
      │
      ▼
┌─────────────────────────────────────────┐
│ Update Global Metrics                     │
│ • Update Nigeria country card             │
│ • Recalculate global totals              │
│ • Update global headcount                 │
│ • Trigger UI refresh                      │
│ • Show notification (if applicable)       │
└─────────────────────────────────────────┘
```

---

## 6. CROSS-COUNTRY DATA AGGREGATION

### 6.1 Data Collection Flow

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                    DATA COLLECTION FROM COUNTRIES                        │
└─────────────────────────────────────────────────────────────────────────────┘

Scheduled data collection (daily/weekly/monthly)
      │
      ▼
┌─────────────────────────────────────────────────────────────────────┐
│  COLLECT DATA FROM ALL COUNTRY HR DASHBOARDS                       │
│  ┌───────────────────────────────────────────────────────────────┐  │  │
│  │ Nigeria → POST /api/v1/hq-hr/data/aggregation                    │  │  │
│  │   Employee: 347, Payroll: ₦127.5M, Hires: 8, etc.             │  │  │
│  └───────────────────────────────────────────────────────────────┘  │  │
│  ┌───────────────────────────────────────────────────────────────┐  │  │
│  │ Kenya → POST /api/v1/hq-hr/data/aggregation                       │  │  │
│  │   Employee: 234, Payroll: ₦89.2M, Hires: 5, etc.                │  │  │
│  └───────────────────────────────────────────────────────────────┘  │  │
│  ┌───────────────────────────────────────────────────────────────┐  │  │
│  │ South Africa → POST /api/v1/hq-hr/data/aggregation               │  │  │
│  │   Employee: 289, Payroll: ₦105.3M, Hires: 6, etc.            │  │  │
│  └───────────────────────────────────────────────────────────────┘  │  │
│  ┌───────────────────────────────────────────────────────────────┐  │  │
│  │ Ireland → POST /api/v1/hq-hr/data/aggregation                  │  │  │
│  │   Employee: 156, Payroll: ₦103M, Hires: 4, etc.                   │  │  │
│  └───────────────────────────────────────────────────────────────┘  │  │
└─────────────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────────────┐
│  AGGREGATE & CALCULATE                                                │
│  • Sum totals across all countries                                     │
│  • Calculate averages                                                  │
│  • Compute country comparisons                                         │
│  • Identify trends                                                     │
│  • Generate insights                                                    │
└─────────────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────────────┐
│  UPDATE HQ HR DASHBOARD                                                │
│  • Refresh country comparison cards                                     │
│  • Update global metrics                                                │
│  • Display aggregated data                                              │
│  • Show comparison charts                                               │
└─────────────────────────────────────────────────────────────────────┘
```

---

**End of Mock Flow Documentation v1.0**

**Next:** [04_Page_By_Page_Flow_Documentation.md](./04_Page_By_Page_Flow_Documentation.md) - Detailed page flows
