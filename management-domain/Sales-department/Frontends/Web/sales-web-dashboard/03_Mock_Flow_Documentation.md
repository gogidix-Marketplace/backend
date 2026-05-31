# HQ SALES DASHBOARD - MOCK FLOW DOCUMENTATION

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

1. [API Architecture](#1-api-architecture)
2. [Data Models & Types](#2-data-models--types)
3. [API Endpoints](#3-api-endpoints)
4. [State Management](#4-state-management)
5. [Mock Data Examples](#5-mock-data-examples)
6. [Error Responses](#6-error-responses)

---

## 1. API ARCHITECTURE

### 1.1 Base URLs

```
Development:  https://sales.hq.api.dev/v1
Staging:     https://sales.hq.api.staging/v1
Production:  https://sales.hq.api/v1
```

### 1.2 Authentication

All requests require authentication via Bearer token:

```typescript
const headers = {
  'Authorization': `Bearer ${accessToken}`,
  'Content-Type': 'application/json',
  'X-Region-Scope': getRegionScope(), // 'GLOBAL' or specific region code
  'X-Request-ID': generateRequestId(),
};
```

### 1.3 Response Format

```typescript
interface ApiResponse<T> {
  success: boolean;
  data: T;
  message?: string;
  errors?: ValidationError[];
  pagination?: PaginationInfo;
  timestamp: string;
  requestId: string;
}

interface PaginationInfo {
  page: number;
  pageSize: number;
  totalItems: number;
  totalPages: number;
  hasNext: boolean;
  hasPrevious: boolean;
}
```

---

## 2. DATA MODELS & TYPES

### 2.1 User & Authentication

```typescript
// HQ Sales User
interface HQSalesUser {
  id: string;
  email: string;
  firstName: string;
  lastName: string;
  avatar?: string;
  role: HQSalesRole;
  regionScope?: RegionScope;
  permissions: Permission[];
  timezone: string;
  createdAt: Date;
  lastLoginAt: Date;
}

type HQSalesRole =
  | 'VP_SALES'
  | 'GLOBAL_SALES_DIRECTOR'
  | 'REGIONAL_SALES_MANAGER'
  | 'HQ_SALES_ANALYST'
  | 'SALES_OPERATIONS_MANAGER';

type RegionScope =
  | 'GLOBAL'
  | 'WEST_AFRICA'
  | 'EAST_AFRICA'
  | 'SOUTHERN_AFRICA'
  | 'NORTH_AFRICA';

// Auth Response
interface AuthResponse {
  user: HQSalesUser;
  accessToken: string;
  refreshToken: string;
  expiresIn: number;
}

// Login Request
interface LoginRequest {
  email: string;
  password: string;
  deviceId?: string;
  deviceName?: string;
}
```

### 2.2 Global Dashboard Models

```typescript
// Global Dashboard Summary
interface GlobalDashboardSummary {
  period: {
    start: Date;
    end: Date;
    type: 'daily' | 'weekly' | 'monthly' | 'quarterly' | 'yearly';
  };
  globalMetrics: {
    revenue: GlobalMetric;
    pipelineValue: GlobalMetric;
    dealsClosed: GlobalMetric;
    activeDeals: GlobalMetric;
    winRate: GlobalMetric;
    avgDealSize: GlobalMetric;
  };
  countriesSummary: CountrySummary[];
  topPerformers: GlobalTopPerformer[];
  majorDeals: MajorDeal[];
  alerts: GlobalAlert[];
  forecastSummary: ForecastSummary;
}

interface GlobalMetric {
  value: number;
  label: string;
  change: number;
  changeType: 'increase' | 'decrease' | 'neutral';
  target?: number;
  attainment?: number;
  currency?: string;
}

// Country Summary
interface CountrySummary {
  country: CountryInfo;
  metrics: {
    revenue: number;
    quota: number;
    attainment: number;
    dealsClosed: number;
    pipelineValue: number;
    winRate: number;
    growth: number;
  };
  trend: 'up' | 'down' | 'neutral';
  status: 'on_track' | 'at_risk' | 'off_track';
}

interface CountryInfo {
  code: string;  // 'NG', 'KE', 'GH', 'ZA', etc.
  name: string;  // 'Nigeria', 'Kenya', etc.
  flag: string;  // 🇳🇬, 🇰🇪, etc.
  currency: string;  // 'NGN', 'KES', etc.
  region: string;  // 'West Africa', 'East Africa', etc.
  directorId: string;
  directorName: string;
}

// Global Top Performer
interface GlobalTopPerformer {
  userId: string;
  name: string;
  avatar?: string;
  country: CountryInfo;
  department: string;
  revenue: number;
  quota: number;
  attainment: number;
  dealsClosed: number;
  rank: number;
}

// Major Deal
interface MajorDeal {
  id: string;
  dealNumber: string;
  name: string;
  company: string;
  country: CountryInfo;
  value: number;
  currency: string;
  stage: string;
  probability: number;
  expectedCloseDate: Date;
  owner: {
    id: string;
    name: string;
    country: string;
  };
  isMajor: boolean;  // Value > $100K
}

// Global Alert
interface GlobalAlert {
  id: string;
  type: 'info' | 'warning' | 'success' | 'error';
  category: 'quota' | 'deal' | 'forecast' | 'team' | 'system';
  title: string;
  message: string;
  affectedCountries?: CountryInfo[];
  severity: 'low' | 'medium' | 'high' | 'critical';
  actionUrl?: string;
  createdAt: Date;
}

// Forecast Summary
interface ForecastSummary {
  period: string;
  forecast: number;
  bestCase: number;
  worstCase: number;
  weightedPipeline: number;
  currency: string;
  accuracy?: number;
}
```

### 2.3 Country Comparison Models

```typescript
// Country Comparison Data
interface CountryComparison {
  countries: CountryInfo[];
  metrics: ComparisonMetric[];
  rankings: ComparisonRanking[];
  period: PeriodInfo;
}

interface ComparisonMetric {
  key: string;
  label: string;
  format: 'currency' | 'percentage' | 'number' | 'text';
  values: Record<string, ComparisonValue>;
  trend?: Record<string, 'up' | 'down' | 'neutral'>;
}

interface ComparisonValue {
  value: number;
  display: string;
  rank?: number;
  vsTarget?: number;
  vsPrevious?: number;
}

interface ComparisonRanking {
  category: string;
  rankings: CountryRanking[];
}

interface CountryRanking {
  rank: number;
  country: CountryInfo;
  value: number;
  display: string;
  change?: number;
}
```

### 2.4 Global Pipeline Models

```typescript
// Global Pipeline Summary
interface GlobalPipelineSummary {
  totalValue: number;
  totalDeals: number;
  weightedValue: number;
  avgDealSize: number;
  byCountry: CountryPipelineSummary[];
  byStage: PipelineStageSummary[];
  majorDeals: MajorDeal[];
  velocity: PipelineVelocity;
}

interface CountryPipelineSummary {
  country: CountryInfo;
  totalValue: number;
  dealCount: number;
  weightedValue: number;
  avgDealSize: number;
  stageDistribution: Record<string, number>;
  trend: 'up' | 'down' | 'neutral';
}

interface PipelineStageSummary {
  stage: string;
  count: number;
  totalValue: number;
  weightedValue: number;
  percentage: number;
  avgDaysInStage: number;
  conversionRate?: number;
}

interface PipelineVelocity {
  avgDaysInStage: Record<string, number>;
  overallCycle: number;
  cycleTarget: number;
  cycleVariance: number;
}
```

### 2.5 Global Performance Models

```typescript
// Global Performance Data
interface GlobalPerformanceData {
  period: PeriodInfo;
  summary: PerformanceSummary;
  countryRankings: CountryRanking[];
  topPerformers: GlobalTopPerformer[];
  productPerformance: ProductPerformance[];
  trendAnalysis: PerformanceTrend[];
}

interface PerformanceSummary {
  globalRevenue: {
    actual: number;
    target: number;
    attainment: number;
    change: number;
  };
  globalQuota: {
    assigned: number;
    achieved: number;
    attainment: number;
    change: number;
  };
  totalDeals: {
    closed: number;
    change: number;
  };
  avgWinRate: {
    value: number;
    change: number;
  };
}

interface ProductPerformance {
  productCode: string;
  productName: string;
  category: string;
  revenue: number;
  percentage: number;
  growth: number;
  targetAttainment: number;
  byCountry: Record<string, ProductCountryMetric>;
}

interface ProductCountryMetric {
  revenue: number;
  growth: number;
  attainment: number;
}

interface PerformanceTrend {
  period: string;
  revenue: number;
  target: number;
  attainment: number;
  dealsClosed: number;
  winRate: number;
}
```

### 2.6 Global Forecasting Models

```typescript
// Global Sales Forecast
interface GlobalSalesForecast {
  id: string;
  name: string;
  period: ForecastPeriod;
  scenario: ForecastScenario;
  generatedAt: Date;
  generatedBy: string;

  // Global values
  global: {
    forecast: number;
    bestCase: number;
    worstCase: number;
    currency: string;
    confidence: number;
  };

  // Country breakdown
  byCountry: CountryForecast[];

  // Stage breakdown
  byStage: StageForecast[];

  // Historical accuracy
  accuracyMetrics: ForecastAccuracy[];

  // Trend analysis
  trends: ForecastTrend[];
}

interface CountryForecast {
  country: CountryInfo;
  forecast: number;
  bestCase: number;
  worstCase: number;
  pipeline: number;
  contribution: number;  // % of global total
  trend: 'up' | 'down' | 'neutral';
  riskLevel: 'low' | 'medium' | 'high';
}

interface StageForecast {
  stage: string;
  count: number;
  value: number;
  weightedValue: number;
  probability: number;
  contribution: number;  // % of total forecast
}

interface ForecastAccuracy {
  period: string;
  accuracy: number;
  variance: number;
  variancePercentage: number;
  actualRevenue: number;
  forecastedRevenue: number;
}

interface ForecastTrend {
  period: string;
  actual?: number;
  forecast: number;
  bestCase?: number;
  worstCase?: number;
  scenario: ForecastScenario;
}
```

### 2.7 Global Reports Models

```typescript
// Report Definition
interface Report {
  id: string;
  name: string;
  description: string;
  type: ReportType;
  category: ReportCategory;
  createdBy: string;
  createdAt: Date;
  lastRunAt?: Date;
  schedule?: ReportSchedule;
  config: ReportConfig;
}

type ReportType =
  | 'EXECUTIVE_DASHBOARD'
  | 'COUNTRY_COMPARISON'
  | 'PIPELINE_ANALYSIS'
  | 'PERFORMANCE_REPORT'
  | 'FORECAST_REPORT'
  | 'PRODUCT_REPORT'
  | 'TEAM_REPORT'
  | 'CUSTOM';

type ReportCategory =
  | 'EXECUTIVE'
  | 'COUNTRY'
  | 'PIPELINE'
  | 'PERFORMANCE'
  | 'FORECAST'
  | 'PRODUCT'
  | 'TEAM';

interface ReportConfig {
  metrics: string[];
  countries?: string[];
  period: PeriodInfo;
  filters?: Record<string, any>;
  visualizations: ReportVisualization[];
  exportFormat: 'PDF' | 'EXCEL' | 'CSV';
}

interface ReportVisualization {
  type: 'line' | 'bar' | 'pie' | 'table' | 'number';
  title: string;
  dataSource: string;
  config: any;
}

interface ReportSchedule {
  frequency: 'daily' | 'weekly' | 'monthly' | 'quarterly';
  dayOfWeek?: number;
  dayOfMonth?: number;
  time: string;
  recipients: string[];
  active: boolean;
  nextRunAt?: Date;
}

// Generated Report
interface GeneratedReport {
  id: string;
  reportId: string;
  reportName: string;
  generatedBy: string;
  generatedAt: Date;
  period: PeriodInfo;
  status: 'pending' | 'generating' | 'completed' | 'failed';
  fileUrl?: string;
  expiresAt?: Date;
}
```

---

### 2.8 Sales Team Partners Models

```typescript
// Sales Team Partner
interface SalesTeamPartner {
  id: string;
  partnerNumber: string;  // 'P-2001'
  userId: string;
  user: {
    firstName: string;
    lastName: string;
    email: string;
    phone: string;
    avatar?: string;
  };
  partnerType: PartnerType;
  assignedCountry: CountryInfo;
  territory?: Territory;
  status: PartnerStatus;
  tier: PartnerTier;

  // Metrics
  partnersRecruited: number;
  customersAcquired: number;
  pipelineValue: number;
  revenueGenerated: number;
  commissionEarned: number;
  referralBonusEarned: number;

  // Performance
  tierProgression: TierProgression;
  leadConversionRate: number;
  avgDealSize: number;
  activeDeals: number;

  // AI Lead assignments
  assignedLeads: PartnerLeadAssignment[];

  // Approval
  applicationId?: string;
  approvedAt?: Date;
  approvedBy?: string;

  createdAt: Date;
  updatedAt: Date;
  lastActivityAt?: Date;
}

type PartnerType =
  | 'COURIER'
  | 'HAULAGE'
  | 'WAREHOUSE'
  | 'ECOMMERCE'
  | 'AIR_OCEAN'
  | 'LOCATION_AGENT'
  | 'WHOLESALE'
  | 'INFLUENCER';

type PartnerStatus =
  | 'PENDING'
  | 'ACTIVE'
  | 'SUSPENDED'
  | 'TERMINATED';

type PartnerTier =
  | 'BRONZE'
  | 'SILVER'
  | 'GOLD'
  | 'PLATINUM';

interface TierProgression {
  currentTier: PartnerTier;
  currentTierRevenue: number;
  nextTier?: PartnerTier;
  nextTierRevenue?: number;
  revenueToNextTier: number;
  estimatedTimeToNextTier?: number;  // days
}

interface PartnerLeadAssignment {
  id: string;
  leadId: string;
  lead: {
    companyName: string;
    contact: string;
    value: number;
    territory: string;
  };
  score: number;
  assignedAt: Date;
  status: 'NEW' | 'CONTACTED' | 'CONVERTED' | 'LOST';
  convertedToCustomer?: boolean;
  customerValue?: number;
}

// Partner Application
interface PartnerApplication {
  id: string;
  applicationNumber: string;  // 'PA-1001'
  applicant: {
    firstName: string;
    lastName: string;
    email: string;
    phone: string;
    dateOfBirth?: Date;
    nationalId?: string;
    address: Address;
  };
  partnerType: PartnerType;
  preferredCountry: CountryInfo;
  preferredTerritory?: string;

  // Supporting documents
  documents: ApplicationDocument[];

  // Background check
  backgroundCheck: {
    status: 'PENDING' | 'CLEARED' | 'FLAGGED';
    completedAt?: Date;
    notes?: string;
  };

  // Referral
  referredBy?: {
    partnerId?: string;
    userId?: string;
    name: string;
  };

  status: ApplicationStatus;
  reviewNotes?: string;
  reviewedBy?: string;
  reviewedAt?: Date;
  rejectionReason?: string;

  createdAt: Date;
  updatedAt: Date;
}

type ApplicationStatus =
  | 'PENDING'
  | 'UNDER_REVIEW'
  | 'APPROVED'
  | 'REJECTED'
  | 'ON_HOLD';

interface ApplicationDocument {
  id: string;
  type: 'ID_DOCUMENT' | 'PROOF_OF_ADDRESS' | 'BUSINESS_REG' | 'TAX_CERT' | 'OTHER';
  name: string;
  url: string;
  uploadedAt: Date;
  verified: boolean;
  verifiedAt?: Date;
}

// Partner Commission
interface PartnerCommission {
  id: string;
  partnerId: string;
  partner: {
    name: string;
    partnerType: PartnerType;
    tier: PartnerTier;
  };

  // Referral bonus
  referralBonus: {
    partnersReferred: number;
    totalBonus: number;
    paidBonus: number;
    pendingBonus: number;
    breakdown: ReferralBonusBreakdown[];
  };

  // Sales commission
  salesCommission: {
    customersAcquired: number;
    totalRevenue: number;
    totalCommission: number;
    paidCommission: number;
    pendingCommission: number;
    breakdown: SalesCommissionBreakdown[];
  };

  // Tier progression bonus
  tierBonus?: {
    achievedTier: PartnerTier;
    bonusAmount: number;
    paidAt?: Date;
  };

  // Payment status
  paymentSummary: {
    totalEarned: number;
    totalPaid: number;
    totalPending: number;
    nextPayoutDate?: Date;
    nextPayoutAmount?: number;
  };

  period: CommissionPeriod;
  generatedAt: Date;
}

interface ReferralBonusBreakdown {
  referredPartnerId: string;
  referredPartnerName: string;
  referredPartnerType: PartnerType;
  bonusAmount: number;
  paidAt?: Date;
}

interface SalesCommissionBreakdown {
  customerId: string;
  customerName: string;
  dealValue: number;
  commissionRate: number;
  commissionAmount: number;
  dealClosedAt: Date;
  paidAt?: Date;
}

interface CommissionPeriod {
  type: 'MONTHLY' | 'QUARTERLY' | 'ANNUAL';
  start: Date;
  end: Date;
  label: string;
}

// Partner Territory Assignment
interface PartnerTerritoryAssignment {
  id: string;
  partnerId: string;
  partner: {
    name: string;
    partnerType: PartnerType;
    tier: PartnerTier;
  };
  assignedCountry: CountryInfo;
  assignedTerritories: string[];
  exclusive: boolean;  // Whether territory is exclusive to this partner

  // Capacity
  capacity: {
    maxPartners: number;
    maxCustomers: number;
    currentPartners: number;
    currentCustomers: number;
    availableCapacity: boolean;
  };

  // Lead allocation
  leadAllocation: {
    enabled: boolean;
    maxDailyLeads: number;
    currentDailyLeads: number;
    aiMatchScore: number;  // Partner's quality score for lead matching
  };

  assignedAt: Date;
  assignedBy: string;
  active: boolean;
}

// Global Partners Summary
interface GlobalPartnersSummary {
  period: PeriodInfo;
  totalPartners: number;
  activePartners: number;
  pendingApplications: number;

  byCountry: CountryPartnersSummary[];
  byPartnerType: PartnerTypeSummary[];
  byTier: PartnerTierSummary[];

  topPerformers: TopPartnerPerformer[];
  recentApplications: PartnerApplication[];
  commissionStats: CommissionStats;
}

interface CountryPartnersSummary {
  country: CountryInfo;
  totalPartners: number;
  activePartners: number;
  pipelineValue: number;
  commissionPaid: number;
  tierBreakdown: Record<PartnerTier, number>;
}

interface PartnerTypeSummary {
  partnerType: PartnerType;
  count: number;
  pipelineValue: number;
  revenueGenerated: number;
  avgCommission: number;
}

interface PartnerTierSummary {
  tier: PartnerTier;
  count: number;
  revenueThreshold: number;
  partnersAtTier: number;
}

interface TopPartnerPerformer {
  partnerId: string;
  name: string;
  partnerType: PartnerType;
  tier: PartnerTier;
  country: CountryInfo;
  pipelineValue: number;
  commissionEarned: number;
  partnersRecruited: number;
  customersAcquired: number;
  rank: number;
}

interface CommissionStats {
  totalEarned: number;
  totalPaid: number;
  totalPending: number;
  avgPayoutTime: number;  // days
}
```

---

## 3. API ENDPOINTS

### 3.1 Authentication Endpoints

```typescript
// POST /auth/login
interface LoginEndpoint {
  POST: {
    request: LoginRequest;
    response: AuthResponse;
  };
}

// POST /auth/refresh
interface RefreshEndpoint {
  POST: {
    request: { refreshToken: string };
    response: { accessToken: string; expiresIn: number };
  };
}

// POST /auth/logout
interface LogoutEndpoint {
  POST: {
    request: {};
    response: { success: boolean };
  };
}

// GET /auth/me
interface MeEndpoint {
  GET: {
    response: HQSalesUser;
  };
}
```

### 3.2 Global Overview Endpoints

```typescript
// GET /dashboard/global
interface GlobalDashboardEndpoint {
  GET: {
    query: {
      period?: 'today' | 'week' | 'month' | 'quarter' | 'year';
      startDate?: Date;
      endDate?: Date;
      countries?: string[];  // Filter by specific countries
    };
    response: GlobalDashboardSummary;
  };
}

// GET /dashboard/countries
interface CountriesSummaryEndpoint {
  GET: {
    query: {
      period?: string;
      region?: string;
    };
    response: CountrySummary[];
  };
}
```

### 3.3 Country Comparison Endpoints

```typescript
// GET /countries/compare
interface CountryCompareEndpoint {
  GET: {
    query: {
      countries: string[];  // Required: ['NG', 'KE', 'GH']
      period?: string;
      metrics?: string[];  // ['revenue', 'quota', 'deals', etc.]
    };
    response: CountryComparison;
  };
}

// GET /countries/:code
interface CountryDetailEndpoint {
  GET: {
    params: { code: string };
    query: {
      period?: string;
    };
    response: {
      country: CountryInfo;
      summary: CountrySummary;
      metrics: PerformanceMetrics;
      topPerformers: GlobalTopPerformer[];
      majorDeals: MajorDeal[];
      trend: PerformanceTrend[];
    };
  };
}

// GET /countries/rankings
interface CountryRankingsEndpoint {
  GET: {
    query: {
      metric: 'revenue' | 'quota_attainment' | 'deals_closed' | 'win_rate';
      period?: string;
    };
    response: CountryRanking[];
  };
}
```

### 3.4 Global Pipeline Endpoints

```typescript
// GET /pipeline/global
interface GlobalPipelineEndpoint {
  GET: {
    query: {
      countries?: string[];
      minDealValue?: number;
      majorDealsOnly?: boolean;
    };
    response: GlobalPipelineSummary;
  };
}

// GET /pipeline/major-deals
interface MajorDealsEndpoint {
  GET: {
    query: {
      minValue?: number;
      countries?: string[];
      stages?: string[];
      limit?: number;
    };
    response: MajorDeal[];
  };
}

// GET /pipeline/by-stage
interface PipelineByStageEndpoint {
  GET: {
    query: {
      country?: string;
    };
    response: PipelineStageSummary[];
  };
}
```

### 3.5 Global Performance Endpoints

```typescript
// GET /performance/global
interface GlobalPerformanceEndpoint {
  GET: {
    query: {
      period: string;
      groupBy?: 'country' | 'department' | 'product';
    };
    response: GlobalPerformanceData;
  };
}

// GET /performance/top-performers
interface TopPerformersEndpoint {
  GET: {
    query: {
      period?: string;
      limit?: number;
      country?: string;
      department?: string;
    };
    response: GlobalTopPerformer[];
  };
}

// GET /performance/products
interface ProductPerformanceEndpoint {
  GET: {
    query: {
      period?: string;
      country?: string;
    };
    response: ProductPerformance[];
  };
}
```

### 3.6 Global Forecasting Endpoints

```typescript
// GET /forecasting/global
interface GlobalForecastEndpoint {
  GET: {
    query: {
      period?: string;
      scenario?: ForecastScenario;
    };
    response: GlobalSalesForecast;
  };
}

// POST /forecasting/generate
interface GenerateForecastEndpoint {
  POST: {
    request: {
      period: ForecastPeriod;
      scenario?: ForecastScenario;
      options?: {
        includeBestCase?: boolean;
        includeWorstCase?: boolean;
        useHistoricalTrends?: boolean;
        countries?: string[];
      };
    };
    response: GlobalSalesForecast;
  };
}

// GET /forecasting/accuracy
interface ForecastAccuracyEndpoint {
  GET: {
    query: {
      periods?: number;  // Number of past periods to analyze
    };
    response: ForecastAccuracy[];
  };
}
```

### 3.7 Reports Endpoints

```typescript
// GET /reports
interface ReportsListEndpoint {
  GET: {
    query: {
      type?: ReportType;
      category?: ReportCategory;
    };
    response: Report[];
  };
}

// POST /reports
interface CreateReportEndpoint {
  POST: {
    request: Partial<Report>;
    response: Report;
  };
}

// POST /reports/:id/generate
interface GenerateReportEndpoint {
  POST: {
    params: { id: string };
    request: {
      period: PeriodInfo;
      format?: 'PDF' | 'EXCEL' | 'CSV';
    };
    response: GeneratedReport;
  };
}

// GET /reports/:id/history
interface ReportHistoryEndpoint {
  GET: {
    params: { id: string };
    query: {
      limit?: number;
    };
    response: GeneratedReport[];
  };
}
```

### 3.8 Sales Team Partners Endpoints

```typescript
// GET /partners/summary
// Get global partners summary
interface PartnersSummaryEndpoint {
  GET: {
    query: {
      period?: string;
      country?: string;
    };
    response: GlobalPartnersSummary;
  };
}

// GET /partners
// List partners with pagination
interface PartnersListEndpoint {
  GET: {
    query: {
      page?: number;
      pageSize?: number;
      sortBy?: string;
      sortOrder?: 'asc' | 'desc';
      search?: string;
      country?: string[];
      partnerType?: PartnerType[];
      status?: PartnerStatus[];
      tier?: PartnerTier[];
    };
    response: PaginatedResponse<SalesTeamPartner>;
  };
}

// GET /partners/:id
// Get partner details
interface PartnerDetailEndpoint {
  GET: {
    params: { id: string };
    response: SalesTeamPartner;
  };
}

// GET /partners/applications
// List partner applications
interface PartnerApplicationsEndpoint {
  GET: {
    query: {
      page?: number;
      pageSize?: number;
      status?: ApplicationStatus[];
      country?: string[];
      partnerType?: PartnerType[];
    };
    response: PaginatedResponse<PartnerApplication>;
  };
}

// GET /partners/applications/:id
// Get application details
interface PartnerApplicationDetailEndpoint {
  GET: {
    params: { id: string };
    response: PartnerApplication;
  };
}

// PUT /partners/applications/:id/review
// Review and approve/reject application
interface PartnerApplicationReviewEndpoint {
  PUT: {
    params: { id: string };
    request: {
      action: 'APPROVE' | 'REJECT' | 'REQUEST_INFO';
      notes?: string;
      rejectionReason?: string;
      assignTerritory?: string;
    };
    response: PartnerApplication;
  };
}

// GET /partners/performance
// Get partners performance data
interface PartnersPerformanceEndpoint {
  GET: {
    query: {
      period?: string;
      country?: string;
      partnerType?: PartnerType;
      groupBy?: 'country' | 'type' | 'tier';
      limit?: number;
    };
    response: {
      summary: PartnerPerformanceSummary;
      topPerformers: TopPartnerPerformer[];
      byCountry: CountryPartnersSummary[];
      byType: PartnerTypeSummary[];
      byTier: PartnerTierSummary[];
    };
  };
}

// GET /partners/commission
// Get partners commission data
interface PartnersCommissionEndpoint {
  GET: {
    query: {
      period?: string;
      partnerId?: string;
      status?: 'PENDING' | 'PAID' | 'ALL';
    };
    response: {
      summary: CommissionStats;
      breakdown: PartnerCommission[];
    };
  };
}

// POST /partners/commission/process
// Process pending commission payments
interface ProcessCommissionEndpoint {
  POST: {
    request: {
      partnerIds?: string[];
      period: CommissionPeriod;
    };
    response: {
      processed: number;
      totalAmount: number;
      transactions: string[];
    };
  };
}

// GET /partners/territory
// Get territory assignments
interface PartnersTerritoryEndpoint {
  GET: {
    query: {
      country?: string;
      partnerId?: string;
    };
    response: PartnerTerritoryAssignment[];
  };
}

// POST /partners/territory/assign
// Assign territory to partner
interface AssignTerritoryEndpoint {
  POST: {
    request: {
      partnerId: string;
      country: string;
      territories: string[];
      exclusive?: boolean;
    };
    response: PartnerTerritoryAssignment;
  };
}

// GET /partners/analytics
// Get partner analytics
interface PartnersAnalyticsEndpoint {
  GET: {
    query: {
      period?: string;
      country?: string;
      metrics?: string[];
    };
    response: {
      recruitment: PartnerRecruitmentAnalytics;
      performance: PartnerPerformanceAnalytics;
      commission: PartnerCommissionAnalytics;
      trends: AnalyticsTrend[];
    };
  };
}
```

---

## 4. STATE MANAGEMENT

### 4.1 Store Structure (Zustand)

```typescript
interface HQSalesDashboardStore {
  // Auth state
  auth: {
    user: HQSalesUser | null;
    accessToken: string | null;
    isAuthenticated: boolean;
    permissions: Permission[];
    regionScope?: RegionScope;
  };

  // Global dashboard state
  globalDashboard: {
    summary: GlobalDashboardSummary | null;
    loading: boolean;
    error: string | null;
    selectedPeriod: string;
  };

  // Countries state
  countries: {
    summaries: CountrySummary[];
    selected: CountryInfo[];
    comparison: CountryComparison | null;
    loading: boolean;
    error: string | null;
  };

  // Pipeline state
  pipeline: {
    summary: GlobalPipelineSummary | null;
    majorDeals: MajorDeal[];
    filters: PipelineFilters;
    loading: boolean;
    error: string | null;
  };

  // Performance state
  performance: {
    data: GlobalPerformanceData | null;
    topPerformers: GlobalTopPerformer[];
    products: ProductPerformance[];
    loading: boolean;
    error: string | null;
  };

  // Forecasting state
  forecasting: {
    currentForecast: GlobalSalesForecast | null;
    scenarios: Record<string, GlobalSalesForecast>;
    loading: boolean;
    error: string | null;
  };

  // Reports state
  reports: {
    templates: Report[];
    generated: GeneratedReport[];
    loading: boolean;
    error: string | null;
  };

  // Partners state
  partners: {
    summary: GlobalPartnersSummary | null;
    items: SalesTeamPartner[];
    applications: PartnerApplication[];
    selectedApplication: PartnerApplication | null;
    performance: PartnerPerformanceSummary | null;
    commission: PartnerCommission | null;
    territory: PartnerTerritoryAssignment[];
    filters: PartnerFilters;
    pagination: PaginationState;
    loading: boolean;
    error: string | null;
  };

  // UI state
  ui: {
    sidebarOpen: boolean;
    selectedCountries: string[];
    selectedPeriod: string;
    selectedRegion: RegionScope;
    theme: 'light' | 'dark';
  };
}
```

### 4.2 Store Actions

```typescript
interface HQSalesDashboardActions {
  // Auth actions
  login: (credentials: LoginRequest) => Promise<void>;
  logout: () => void;
  refreshToken: () => Promise<void>;

  // Global dashboard actions
  loadGlobalSummary: (period?: string) => Promise<void>;
  setSelectedPeriod: (period: string) => void;

  // Country actions
  loadCountrySummaries: () => Promise<void>;
  selectCountries: (countries: string[]) => void;
  loadCountryComparison: (countries: string[]) => Promise<void>;
  loadCountryDetail: (code: string) => Promise<void>;

  // Pipeline actions
  loadGlobalPipeline: (filters?: PipelineFilters) => Promise<void>;
  loadMajorDeals: (minValue?: number) => Promise<void>;

  // Performance actions
  loadGlobalPerformance: (period?: string) => Promise<void>;
  loadTopPerformers: (limit?: number) => Promise<void>;
  loadProductPerformance: () => Promise<void>;

  // Forecasting actions
  loadForecast: (scenario?: ForecastScenario) => Promise<void>;
  generateForecast: (options: ForecastOptions) => Promise<void>;

  // Report actions
  loadReports: () => Promise<void>;
  generateReport: (reportId: string, config: ReportConfig) => Promise<void>;

  // Partner actions
  loadPartnersSummary: (period?: string) => Promise<void>;
  loadPartners: (filters?: PartnerFilters) => Promise<void>;
  loadPartnerApplications: (status?: ApplicationStatus[]) => Promise<void>;
  reviewPartnerApplication: (id: string, action: 'APPROVE' | 'REJECT', data?: any) => Promise<void>;
  loadPartnersPerformance: (period?: string) => Promise<void>;
  loadPartnersCommission: (period?: string) => Promise<void>;
  processPartnerCommission: (partnerIds: string[], period: CommissionPeriod) => Promise<void>;
  loadPartnersTerritory: (country?: string) => Promise<void>;
  assignPartnerTerritory: (partnerId: string, data: TerritoryAssignment) => Promise<void>;

  // UI actions
  toggleSidebar: () => void;
  setTheme: (theme: 'light' | 'dark') => void;
  setSelectedRegion: (region: RegionScope) => void;
}
```

### 4.3 React Query Keys

```typescript
const queryKeys = {
  // Auth
  auth: ['auth'] as const,
  me: ['me'] as const,

  // Global dashboard
  globalDashboard: (period: string) => ['dashboard', 'global', period] as const,
  countrySummaries: () => ['countries', 'summaries'] as const,

  // Countries
  countries: (filters?: string[]) => ['countries', filters] as const,
  country: (code: string) => ['country', code] as const,
  countryComparison: (countries: string[]) => ['countries', 'compare', countries] as const,
  countryRankings: (metric: string) => ['countries', 'rankings', metric] as const,

  // Pipeline
  globalPipeline: (filters?: PipelineFilters) => ['pipeline', 'global', filters] as const,
  majorDeals: (minValue?: number) => ['pipeline', 'major-deals', minValue] as const,
  pipelineByStage: (country?: string) => ['pipeline', 'by-stage', country] as const,

  // Performance
  globalPerformance: (period: string) => ['performance', 'global', period] as const,
  topPerformers: (limit?: number) => ['performance', 'top-performers', limit] as const,
  productPerformance: (country?: string) => ['performance', 'products', country] as const,

  // Forecasting
  forecast: (scenario?: string) => ['forecast', scenario] as const,
  forecastAccuracy: (periods?: number) => ['forecast', 'accuracy', periods] as const,

  // Reports
  reports: () => ['reports'] as const,
  reportHistory: (id: string) => ['reports', id, 'history'] as const,

  // Partners
  partnersSummary: (period?: string) => ['partners', 'summary', period] as const,
  partners: (filters?: PartnerFilters) => ['partners', filters] as const,
  partner: (id: string) => ['partner', id] as const,
  partnerApplications: (status?: ApplicationStatus[]) => ['partners', 'applications', status] as const,
  partnerApplication: (id: string) => ['partners', 'applications', id] as const,
  partnersPerformance: (period?: string) => ['partners', 'performance', period] as const,
  partnersCommission: (period?: string) => ['partners', 'commission', period] as const,
  partnersTerritory: (country?: string) => ['partners', 'territory', country] as const,
  partnersAnalytics: (period?: string) => ['partners', 'analytics', period] as const,

  // Notifications
  notifications: () => ['notifications'] as const,
};
```

---

## 5. MOCK DATA EXAMPLES

### 5.1 Mock Global Dashboard Summary

```json
{
  "period": {
    "start": "2025-02-01T00:00:00Z",
    "end": "2025-02-08T23:59:59Z",
    "type": "month"
  },
  "globalMetrics": {
    "revenue": {
      "value": 8500000,
      "label": "$8.5M",
      "change": 15,
      "changeType": "increase",
      "target": 8500000,
      "attainment": 100,
      "currency": "USD"
    },
    "pipelineValue": {
      "value": 25000000,
      "label": "$25M",
      "change": 12,
      "changeType": "increase",
      "currency": "USD"
    },
    "dealsClosed": {
      "value": 1245,
      "label": "1,245",
      "change": 18,
      "changeType": "increase"
    },
    "activeDeals": {
      "value": 4892,
      "label": "4,892",
      "change": 8,
      "changeType": "increase"
    },
    "winRate": {
      "value": 34,
      "label": "34%",
      "change": 2,
      "changeType": "increase"
    },
    "avgDealSize": {
      "value": 6827,
      "label": "$6,827",
      "change": -3,
      "changeType": "decrease",
      "currency": "USD"
    }
  },
  "countriesSummary": [
    {
      "country": {
        "code": "NG",
        "name": "Nigeria",
        "flag": "🇳🇬",
        "currency": "NGN",
        "region": "West Africa",
        "directorId": "usr-001",
        "directorName": "John Doe"
      },
      "metrics": {
        "revenue": 1200000,
        "quota": 1300000,
        "attainment": 92,
        "dealsClosed": 245,
        "pipelineValue": 3500000,
        "winRate": 32,
        "growth": 12
      },
      "trend": "up",
      "status": "on_track"
    },
    {
      "country": {
        "code": "KE",
        "name": "Kenya",
        "flag": "🇰🇪",
        "currency": "KES",
        "region": "East Africa",
        "directorId": "usr-002",
        "directorName": "Jane Smith"
      },
      "metrics": {
        "revenue": 950000,
        "quota": 900000,
        "attainment": 106,
        "dealsClosed": 189,
        "pipelineValue": 2800000,
        "winRate": 35,
        "growth": 15
      },
      "trend": "up",
      "status": "on_track"
    },
    {
      "country": {
        "code": "ZA",
        "name": "South Africa",
        "flag": "🇿🇦",
        "currency": "ZAR",
        "region": "Southern Africa",
        "directorId": "usr-003",
        "directorName": "Sarah Williams"
      },
      "metrics": {
        "revenue": 2100000,
        "quota": 2000000,
        "attainment": 105,
        "dealsClosed": 312,
        "pipelineValue": 4200000,
        "winRate": 38,
        "growth": 18
      },
      "trend": "up",
      "status": "on_track"
    }
  ],
  "topPerformers": [
    {
      "userId": "usr-101",
      "name": "Sarah Williams",
      "avatar": "https://cdn.company.com/avatars/sarah.jpg",
      "country": {
        "code": "ZA",
        "name": "South Africa",
        "flag": "🇿🇦"
      },
      "department": "Enterprise",
      "revenue": 280000,
      "quota": 200000,
      "attainment": 140,
      "dealsClosed": 35,
      "rank": 1
    },
    {
      "userId": "usr-001",
      "name": "John Doe",
      "avatar": "https://cdn.company.com/avatars/john.jpg",
      "country": {
        "code": "NG",
        "name": "Nigeria",
        "flag": "🇳🇬"
      },
      "department": "Enterprise",
      "revenue": 250000,
      "quota": 200000,
      "attainment": 125,
      "dealsClosed": 28,
      "rank": 2
    }
  ],
  "majorDeals": [
    {
      "id": "deal-5001",
      "dealNumber": "D-5001",
      "name": "Global Corporation Enterprise Deal",
      "company": "Global Corporation Nigeria",
      "country": {
        "code": "NG",
        "name": "Nigeria",
        "flag": "🇳🇬",
        "currency": "NGN"
      },
      "value": 500000,
      "currency": "USD",
      "stage": "Negotiation",
      "probability": 75,
      "expectedCloseDate": "2025-02-15T00:00:00Z",
      "owner": {
        "id": "usr-001",
        "name": "John Doe",
        "country": "NG"
      },
      "isMajor": true
    }
  ],
  "alerts": [
    {
      "id": "alert-001",
      "type": "warning",
      "category": "quota",
      "title": "Countries Below Quota",
      "message": "3 countries are below 80% quota attainment",
      "affectedCountries": [
        { "code": "GH", "name": "Ghana", "flag": "🇬🇭" },
        { "code": "ET", "name": "Ethiopia", "flag": "🇪🇹" },
        { "code": "UG", "name": "Uganda", "flag": "🇺🇬" }
      ],
      "severity": "medium",
      "actionUrl": "/countries?filter=below_quota",
      "createdAt": "2025-02-08T09:00:00Z"
    },
    {
      "id": "alert-002",
      "type": "info",
      "category": "deal",
      "title": "Major Deals Closing",
      "message": "12 major deals expected to close this week",
      "severity": "low",
      "actionUrl": "/pipeline?filter=major_deals",
      "createdAt": "2025-02-08T08:30:00Z"
    }
  ],
  "forecastSummary": {
    "period": "Q1 2025",
    "forecast": 10500000,
    "bestCase": 12800000,
    "worstCase": 8200000,
    "weightedPipeline": 25000000,
    "currency": "USD",
    "accuracy": 94.2
  }
}
```

### 5.2 Mock Country Comparison

```json
{
  "countries": [
    { "code": "NG", "name": "Nigeria", "flag": "🇳🇬" },
    { "code": "KE", "name": "Kenya", "flag": "🇰🇪" },
    { "code": "GH", "name": "Ghana", "flag": "🇬🇭" },
    { "code": "ZA", "name": "South Africa", "flag": "🇿🇦" }
  ],
  "metrics": [
    {
      "key": "revenue",
      "label": "Revenue",
      "format": "currency",
      "values": {
        "NG": { "value": 1200000, "display": "$1.2M", "rank": 2, "vsTarget": 92, "vsPrevious": 12 },
        "KE": { "value": 950000, "display": "$950K", "rank": 3, "vsTarget": 106, "vsPrevious": 15 },
        "GH": { "value": 780000, "display": "$780K", "rank": 4, "vsTarget": 88, "vsPrevious": 8 },
        "ZA": { "value": 2100000, "display": "$2.1M", "rank": 1, "vsTarget": 105, "vsPrevious": 18 }
      },
      "trend": {
        "NG": "up",
        "KE": "up",
        "GH": "up",
        "ZA": "up"
      }
    },
    {
      "key": "quotaAttainment",
      "label": "Quota Attainment",
      "format": "percentage",
      "values": {
        "NG": { "value": 92, "display": "92%", "rank": 3 },
        "KE": { "value": 106, "display": "106%", "rank": 2 },
        "GH": { "value": 88, "display": "88%", "rank": 4 },
        "ZA": { "value": 105, "display": "105%", "rank": 1 }
      }
    },
    {
      "key": "dealsClosed",
      "label": "Deals Closed",
      "format": "number",
      "values": {
        "NG": { "value": 245, "display": "245", "rank": 2 },
        "KE": { "value": 189, "display": "189", "rank": 3 },
        "GH": { "value": 156, "display": "156", "rank": 4 },
        "ZA": { "value": 312, "display": "312", "rank": 1 }
      }
    },
    {
      "key": "winRate",
      "label": "Win Rate",
      "format": "percentage",
      "values": {
        "NG": { "value": 32, "display": "32%", "rank": 3 },
        "KE": { "value": 35, "display": "35%", "rank": 2 },
        "GH": { "value": 28, "display": "28%", "rank": 4 },
        "ZA": { "value": 38, "display": "38%", "rank": 1 }
      }
    }
  ],
  "rankings": [
    {
      "category": "revenue",
      "rankings": [
        { "rank": 1, "country": { "code": "ZA", "name": "South Africa", "flag": "🇿🇦" }, "value": 2100000, "display": "$2.1M", "change": 18 },
        { "rank": 2, "country": { "code": "NG", "name": "Nigeria", "flag": "🇳🇬" }, "value": 1200000, "display": "$1.2M", "change": 12 },
        { "rank": 3, "country": { "code": "KE", "name": "Kenya", "flag": "🇰🇪" }, "value": 950000, "display": "$950K", "change": 15 }
      ]
    },
    {
      "category": "growth",
      "rankings": [
        { "rank": 1, "country": { "code": "KE", "name": "Kenya", "flag": "🇰🇪" }, "value": 15, "display": "+15%" },
        { "rank": 2, "country": { "code": "NG", "name": "Nigeria", "flag": "🇳🇬" }, "value": 12, "display": "+12%" },
        { "rank": 3, "country": { "code": "GH", "name": "Ghana", "flag": "🇬🇭" }, "value": 8, "display": "+8%" }
      ]
    }
  ],
  "period": {
    "start": "2025-02-01T00:00:00Z",
    "end": "2025-02-08T23:59:59Z",
    "type": "month"
  }
}
```

### 5.3 Mock Global Pipeline Summary

```json
{
  "totalValue": 26000000,
  "totalDeals": 983,
  "weightedValue": 6700000,
  "avgDealSize": 26449,
  "byCountry": [
    {
      "country": {
        "code": "NG",
        "name": "Nigeria",
        "flag": "🇳🇬",
        "currency": "NGN",
        "region": "West Africa"
      },
      "totalValue": 3500000,
      "dealCount": 287,
      "weightedValue": 1800000,
      "avgDealSize": 12194,
      "stageDistribution": {
        "New": 45,
        "Qualified": 68,
        "Proposal": 52,
        "Negotiating": 62,
        "Closing": 60
      },
      "trend": "up"
    },
    {
      "country": {
        "code": "ZA",
        "name": "South Africa",
        "flag": "🇿🇦",
        "currency": "ZAR",
        "region": "Southern Africa"
      },
      "totalValue": 4200000,
      "dealCount": 342,
      "weightedValue": 2500000,
      "avgDealSize": 12281,
      "stageDistribution": {
        "New": 48,
        "Qual": 89,
        "Prop": 72,
        "Neg": 78,
        "Close": 55
      },
      "trend": "up"
    }
  ],
  "byStage": [
    {
      "stage": "New",
      "count": 156,
      "totalValue": 780000,
      "weightedValue": 0,
      "percentage": 6,
      "avgDaysInStage": 0
    },
    {
      "stage": "Qualified",
      "count": 287,
      "totalValue": 2800000,
      "weightedValue": 560000,
      "percentage": 22,
      "avgDaysInStage": 18
    },
    {
      "stage": "Proposal",
      "count": 245,
      "totalValue": 4200000,
      "weightedValue": 2100000,
      "percentage": 33,
      "avgDaysInStage": 35
    },
    {
      "stage": "Negotiating",
      "count": 198,
      "totalValue": 3500000,
      "weightedValue": 2600000,
      "percentage": 28,
      "avgDaysInStage": 15
    },
    {
      "stage": "Closing",
      "count": 97,
      "totalValue": 1400000,
      "weightedValue": 1300000,
      "percentage": 11,
      "avgDaysInStage": 5
    }
  ],
  "majorDeals": [
    {
      "id": "deal-5001",
      "dealNumber": "D-5001",
      "name": "Global Corp Enterprise Deal",
      "company": "Global Corporation Nigeria",
      "country": {
        "code": "NG",
        "name": "Nigeria",
        "flag": "🇳🇬",
        "currency": "NGN"
      },
      "value": 500000,
      "currency": "USD",
      "stage": "Negotiation",
      "probability": 75,
      "expectedCloseDate": "2025-02-15T00:00:00Z",
      "owner": {
        "id": "usr-001",
        "name": "John Doe",
        "country": "NG"
      },
      "isMajor": true
    },
    {
      "id": "deal-5002",
      "dealNumber": "D-5002",
      "name": "TechPrime Africa Deal",
      "company": "TechPrime South Africa",
      "country": {
        "code": "ZA",
        "name": "South Africa",
        "flag": "🇿🇦",
        "currency": "ZAR"
      },
      "value": 350000,
      "currency": "USD",
      "stage": "Proposal",
      "probability": 50,
      "expectedCloseDate": "2025-02-20T00:00:00Z",
      "owner": {
        "id": "usr-102",
        "name": "Sarah Williams",
        "country": "ZA"
      },
      "isMajor": true
    }
  ],
  "velocity": {
    "avgDaysInStage": {
      "New": 0,
      "Qualified": 18,
      "Proposal": 35,
      "Negotiating": 15,
      "Closing": 5
    },
    "overallCycle": 96,
    "cycleTarget": 90,
    "cycleVariance": 6
  }
}
```

---

## 6. ERROR RESPONSES

### 6.1 Error Response Format

```typescript
interface ErrorResponse {
  success: false;
  error: {
    code: string;
    message: string;
    details?: any;
    timestamp: string;
    requestId: string;
  };
}
```

### 6.2 Common Error Codes

| Error Code | HTTP Status | Description |
|------------|-------------|-------------|
| AUTH_001 | 401 | Invalid or expired token |
| AUTH_002 | 403 | Insufficient permissions for region/country |
| AUTH_003 | 401 | Invalid credentials |
| VAL_001 | 400 | Validation error |
| VAL_002 | 400 | Missing required field |
| VAL_003 | 400 | Invalid format |
| RES_001 | 404 | Resource not found |
| RES_002 | 404 | Country not found in scope |
| RES_003 | 409 | Resource conflict |
| SRV_001 | 500 | Internal server error |
| SRV_002 | 503 | Service unavailable |
| AGG_001 | 500 | Aggregation error |
| AGG_002 | 503 | Country data unavailable |

### 6.3 Error Response Examples

```json
{
  "success": false,
  "error": {
    "code": "RES_002",
    "message": "Country not found in user's scope",
    "details": {
      "requestedCountry": "NG",
      "availableCountries": ["ZA", "KE", "GH"],
      "regionScope": "SOUTHERN_AFRICA"
    },
    "timestamp": "2025-02-08T10:30:00Z",
    "requestId": "req-abc123"
  }
}
```

---

## VERSION HISTORY

| Version | Date | Changes |
|---------|------|---------|
| 1.0 | 2025-02-08 | Initial HQ Sales Dashboard Mock Flow Documentation |
| 1.1 | 2025-02-16 | Added Sales Team Partners module with partner models, application endpoints, commission management, territory allocation, and analytics |

---

## NEXT STEPS

- Complete 04_Page_By_Page_Flow_Documentation.md (Detailed page flows)

---

**Document End**
