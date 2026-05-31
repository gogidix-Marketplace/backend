# HQ FINANCE DASHBOARD - MOCK FLOW DOCUMENTATION

**Domain:** Management Domain
**Application:** HQ Finance Web Dashboard
**Version:** 2.0
**Date:** 2026-02-17
**Path:** `Management-domain/Finance-department/Frontends/Web/finance-web-dashboard/`

---

## TABLE OF CONTENTS

1. [API Endpoint Specifications](#api-endpoint-specifications)
2. [Data Models](#data-models)
3. [Mock Data](#mock-data)
4. [WebSocket Real-Time Flows](#websocket-real-time-flows)
5. [Error Responses](#error-responses)
6. [State Management](#state-management)

---

## 1. API ENDPOINT SPECIFICATIONS

### 1.1 Authentication Endpoints

#### POST /api/v1/finance/auth/login
Authenticate user and retrieve finance scope

**Request:**
```typescript
interface LoginRequest {
  email: string;
  password: string;
  rememberMe?: boolean;
}
```

**Response (200 OK):**
```typescript
interface LoginResponse {
  token: string;
  refreshToken: string;
  expiresIn: number;  // seconds
  user: FinanceUser;
  financeScope: FinanceScope;
}
```

**Response (401 Unauthorized):**
```typescript
interface LoginErrorResponse {
  error: "INVALID_CREDENTIALS" | "ACCOUNT_LOCKED" | "ACCOUNT_DISABLED";
  message: string;
  attemptsRemaining?: number;
  lockoutUntil?: string;  // ISO 8601
}
```

#### POST /api/v1/finance/auth/logout
Invalidate current session

**Request Headers:**
```
Authorization: Bearer {token}
```

**Response (200 OK):**
```typescript
interface LogoutResponse {
  success: true;
  message: string;
}
```

#### GET /api/v1/finance/auth/me
Get current user and finance scope

**Response (200 OK):**
```typescript
interface MeResponse {
  user: FinanceUser;
  financeScope: FinanceScope;
  permissions: string[];
}
```

---

### 1.2 Dashboard Endpoints

#### GET /api/v1/finance/dashboard/global
Retrieve global financial summary for dashboard

**Query Parameters:**
```
periodStart: string (ISO 8601)
periodEnd: string (ISO 8601)
currency: string (default: USD)
includeCountries: boolean (default: true)
```

**Response (200 OK):**
```typescript
interface GlobalDashboardResponse {
  period: DateRange;
  financialHealthScore: number;
  globalMetrics: GlobalFinancialMetrics;
  countriesSummary: CountryFinanceSummary[];
  topPerformers: FinanceTopPerformer[];
  pendingApprovals: PendingApprovalCount;
  alerts: DashboardAlert[];
  currency: string;
  lastUpdated: string;  // ISO 8601
}
```

**Error Response (403 Forbidden):**
```typescript
interface ErrorResponse {
  code: "FIN_HQ_001";
  message: "Insufficient permissions for global view";
  requiredRole: "GLOBAL_VIEW";
}
```

#### GET /api/v1/finance/countries/summary
Retrieve financial summary for all countries in scope

**Query Parameters:**
```
periodStart: string (ISO 8601)
periodEnd: string (ISO 8601)
currency: string (default: USD)
sortBy: "revenue" | "expenses" | "netIncome" | "budgetAttainment"
sortOrder: "asc" | "desc"
```

**Response (200 OK):**
```typescript
interface CountriesSummaryResponse {
  countries: CountryFinanceSummary[];
  totalCount: number;
  currency: string;
  period: DateRange;
}
```

#### GET /api/v1/finance/countries/{countryCode}
Retrieve detailed financial data for a specific country

**Path Parameters:**
```
countryCode: string (e.g., "NGA", "KEN", "ZAF")
```

**Query Parameters:**
```
periodStart: string (ISO 8601)
periodEnd: string (ISO 8601)
currency: string (default: USD)
includeTransactions: boolean (default: false)
```

**Response (200 OK):**
```typescript
interface CountryDetailResponse {
  country: CountryInfo;
  financialHealthScore: number;
  metrics: CountryDetailedMetrics;
  revenueBreakdown: RevenueBreakdown;
  expensesBreakdown: ExpensesBreakdown;
  budgetVsActual: BudgetVsActualData[];
  trends: CountryTrends;
  aiInsights: AIInsight[];
  currency: string;
  period: DateRange;
}
```

**Error Response (403 Forbidden):**
```typescript
interface CountryAccessErrorResponse {
  code: "FIN_HQ_002";
  message: "Country not in assigned scope";
  countryCode: string;
  availableCountries: string[];
}
```

---

### 1.3 Revenue Endpoints

#### GET /api/v1/finance/revenue/consolidated
Retrieve consolidated revenue data across all countries

**Query Parameters:**
```
periodStart: string (ISO 8601)
periodEnd: string (ISO 8601)
currency: string (default: USD)
groupBy: "country" | "businessLine" | "currency" | "month"
```

**Response (200 OK):**
```typescript
interface ConsolidatedRevenueResponse {
  totalRevenue: number;
  revenueByCountry: RevenueByCountry[];
  revenueByBusinessLine: RevenueByBusinessLine[];
  revenueByCurrency: RevenueByCurrency[];
  revenueTrend: RevenueTrendData[];
  forecast: RevenueForecast;
  currency: string;
  period: DateRange;
}
```

#### GET /api/v1/finance/revenue/forecast
Retrieve AI-powered revenue forecast

**Query Parameters:**
```
periodStart: string (ISO 8601)
periodEnd: string (ISO 8601)
model: "lstm" | "arima" | "prophet" (default: lstm)
confidence: number (default: 95)
includeScenarios: boolean (default: true)
```

**Response (200 OK):**
```typescript
interface RevenueForecastResponse {
  forecast: RevenueForecast;
  modelInfo: ForecastModelInfo;
  scenarios: ForecastScenario[];
  drivers: ForecastDriver[];
  risks: ForecastRisk[];
  currency: string;
}
```

---

### 1.4 Budget Endpoints

#### GET /api/v1/finance/budgets/summary
Retrieve global budget summary

**Query Parameters:**
```
period: string (e.g., "Q1-2026", "2026")
currency: string (default: USD)
```

**Response (200 OK):**
```typescript
interface BudgetSummaryResponse {
  totalBudget: number;
  totalActual: number;
  totalVariance: number;
  budgetByCountry: BudgetByCountry[];
  budgetByCategory: BudgetByCategory[];
  budgetUtilization: BudgetUtilization;
  currency: string;
  period: string;
}
```

#### GET /api/v1/finance/budgets/pending-approvals
Retrieve pending budget approval requests

**Query Parameters:**
```
page: number (default: 1)
pageSize: number (default: 20)
status: "pending" | "approved" | "rejected" | "all"
priority: "high" | "medium" | "low" | "all"
```

**Response (200 OK):**
```typescript
interface PendingApprovalsResponse {
  approvals: BudgetApprovalRequest[];
  totalCount: number;
  highPriorityCount: number;
  page: number;
  pageSize: number;
}
```

#### POST /api/v1/finance/budgets/{requestId}/approve
Approve a budget request

**Request Body:**
```typescript
interface ApproveBudgetRequest {
  comments?: string;
  conditions?: string[];
}
```

**Response (200 OK):**
```typescript
interface ApproveBudgetResponse {
  success: boolean;
  approval: BudgetApprovalRequest;
  notificationSent: boolean;
}
```

#### POST /api/v1/finance/budgets/{requestId}/reject
Reject a budget request

**Request Body:**
```typescript
interface RejectBudgetRequest {
  reason: string;
  requiresChanges?: boolean;
  suggestedChanges?: string[];
}
```

**Response (200 OK):**
```typescript
interface RejectBudgetResponse {
  success: boolean;
  approval: BudgetApprovalRequest;
  notificationSent: boolean;
}
```

---

### 1.5 Treasury Endpoints

#### GET /api/v1/finance/treasury/position
Retrieve global cash position

**Query Parameters:**
```
currency: string (default: USD)
includeBreakdown: boolean (default: true)
```

**Response (200 OK):**
```typescript
interface TreasuryPositionResponse {
  totalCash: number;
  operatingCash: number;
  investmentCash: number;
  cashByCountry: CashByCountry[];
  utilizationRate: number;
  targetCash: number;
  currency: string;
  lastUpdated: string;
}
```

#### GET /api/v1/finance/treasury/forecast
Retrieve cash flow forecast (AI-powered)

**Query Parameters:**
```
days: number (default: 30)
currency: string (default: USD)
includeRecommendations: boolean (default: true)
```

**Response (200 OK):**
```typescript
interface CashFlowForecastResponse {
  forecast: CashFlowForecastData[];
  lowPoint: CashFlowLowPoint;
  recommendations: TreasuryRecommendation[];
  currency: string;
  periodDays: number;
}
```

#### GET /api/v1/finance/treasury/fx-exposure
Retrieve foreign exchange exposure

**Query Parameters:**
```
baseCurrency: string (default: USD)
includeHedges: boolean (default: true)
```

**Response (200 OK):**
```typescript
interface FXExposureResponse {
  totalExposure: number;
  exposureByCurrency: FXExposureByCurrency[];
  hedges: FXHedge[];
  netExposure: number;
  riskLevel: "low" | "medium" | "high";
  baseCurrency: string;
  lastUpdated: string;
}
```

---

### 1.6 Reports Endpoints

#### GET /api/v1/finance/reports/consolidated
Retrieve list of consolidated reports

**Query Parameters:**
```
page: number (default: 1)
pageSize: number (default: 20)
reportType: "pl" | "balance_sheet" | "cash_flow" | "tax" | "audit" | "all"
status: "scheduled" | "generating" | "completed" | "failed" | "all"
```

**Response (200 OK):**
```typescript
interface ReportsListResponse {
  reports: ReportInfo[];
  scheduledReports: ScheduledReportInfo[];
  totalCount: number;
  page: number;
  pageSize: number;
}
```

#### POST /api/v1/finance/reports/generate
Generate a new consolidated report

**Request Body:**
```typescript
interface GenerateReportRequest {
  reportType: "pl" | "balance_sheet" | "cash_flow" | "tax" | "audit" | "custom";
  name: string;
  periodStart: string;  // ISO 8601
  periodEnd: string;    // ISO 8601
  countries: string[];  // Country codes, empty for all
  currency: string;
  includeSections: ReportSection[];
  format: "pdf" | "excel" | "csv";
  includeAIInsights: boolean;
  includeCharts: boolean;
}
```

**Response (200 OK):**
```typescript
interface GenerateReportResponse {
  reportId: string;
  status: "generating";
  estimatedCompletionTime: string;  // ISO 8601
  webhookUrl?: string;
}
```

#### GET /api/v1/finance/reports/{reportId}
Retrieve report status and download links

**Response (200 OK):**
```typescript
interface ReportDetailResponse {
  report: ReportInfo;
  status: "generating" | "completed" | "failed";
  downloadUrls?: {
    pdf?: string;
    excel?: string;
    csv?: string;
  };
  error?: string;
}
```

---

### 1.7 AI Services Integration Endpoints

#### GET /api/v1/finance/ai/insights
Retrieve AI-generated financial insights

**Query Parameters:**
```
category: "all" | "revenue" | "expenses" | "fraud" | "treasury"
periodStart: string (ISO 8601)
periodEnd: string (ISO 8601)
confidenceThreshold: number (default: 0.7)
```

**Response (200 OK):**
```typescript
interface AIInsightsResponse {
  insights: AIInsight[];
  summary: {
    total: number;
    highPriority: number;
    mediumPriority: number;
    lowPriority: number;
  };
  lastGenerated: string;
  modelVersion: string;
}
```

#### GET /api/v1/finance/ai/fraud-alerts
Retrieve AI-detected fraud alerts

**Query Parameters:**
```
status: "open" | "investigating" | "resolved" | "dismissed" | "all"
riskLevel: "high" | "medium" | "low" | "all"
page: number (default: 1)
pageSize: number (default: 20)
```

**Response (200 OK):**
```typescript
interface FraudAlertsResponse {
  alerts: FraudAlert[];
  totalCount: number;
  highRiskCount: number;
  openInvestigations: number;
  page: number;
  pageSize: number;
}
```

#### POST /api/v1/finance/ai/fraud-alerts/{alertId}/assign
Assign fraud alert for investigation

**Request Body:**
```typescript
interface AssignFraudAlertRequest {
  assignee: string;  // User ID
  team?: string;      // Team ID
  priority?: "high" | "medium" | "low";
  notes?: string;
}
```

**Response (200 OK):**
```typescript
interface AssignFraudAlertResponse {
  success: boolean;
  alert: FraudAlert;
  notificationSent: boolean;
}
```

---

## 2. DATA MODELS

### 2.1 User & Authentication Models

```typescript
/**
 * Finance User with role and scope information
 */
interface FinanceUser {
  id: string;
  email: string;
  firstName: string;
  lastName: string;
  avatar?: string;
  roles: FinanceRole[];
  currentRole: FinanceRole;
  tenantId: string;
  tenantName: string;
  location: 'Ireland HQ' | string;
  preferences: UserPreferences;
  createdAt: string;
  lastLogin: string;
}

/**
 * Available finance roles
 */
type FinanceRole =
  | 'VP_FINANCE'           // CFO/VP Finance - Full access
  | 'GLOBAL_FINANCE_DIR'   // Global Finance Director
  | 'REGIONAL_FINANCE_MGR' // Regional Finance Manager
  | 'HQ_FINANCIAL_ANALYST' // HQ Financial Analyst (read-only)
  | 'FINANCE_OPS_MGR';     // Finance Operations Manager

/**
 * User's finance access scope
 */
interface FinanceScope {
  type: 'GLOBAL' | 'REGIONAL' | 'READ_ONLY';
  countries?: string[];     // For REGIONAL type
  regions?: string[];       // For REGIONAL type
  permissions: FinancePermission[];
  currencyPreferences: string[];
}

/**
 * Granular permissions
 */
type FinancePermission =
  | 'VIEW_ALL'
  | 'VIEW_ASSIGNED'
  | 'APPROVE_BUDGETS'
  | 'REVIEW_FRAUD_ALERTS'
  | 'EXPORT_REPORTS'
  | 'MANAGE_TREASURY'
  | 'VIEW_TAX_COMPLIANCE'
  | 'MANAGE_USERS';

/**
 * User preferences
 */
interface UserPreferences {
  theme: 'light' | 'dark' | 'auto';
  defaultCurrency: string;
  defaultPeriod: PeriodPreset;
  notifications: NotificationPreferences;
  dashboardLayout: DashboardLayoutConfig;
}

/**
 * Notification preferences
 */
interface NotificationPreferences {
  budgetApproval: boolean;
  fraudAlert: boolean;
  cashFlowWarning: boolean;
  reportReady: boolean;
  systemUpdates: boolean;
  emailDigest: 'daily' | 'weekly' | 'none';
}

/**
 * Period presets
 */
type PeriodPreset =
  | 'today'
  | 'this_week'
  | 'this_month'
  | 'this_quarter'
  | 'this_year'
  | 'last_30_days'
  | 'last_quarter'
  | 'ytd';

/**
 * Dashboard layout configuration
 */
interface DashboardLayoutConfig {
  cards: CardLayout[];
  cardOrder: string[];
  collapsedSections: string[];
}
```

### 2.2 Dashboard Models

```typescript
/**
 * Date range for queries
 */
interface DateRange {
  start: string;  // ISO 8601
  end: string;    // ISO 8601
  preset?: PeriodPreset;
}

/**
 * Global financial metrics
 */
interface GlobalFinancialMetrics {
  totalRevenue: number;
  totalExpenses: number;
  netIncome: number;
  totalCash: number;
  totalDebt: number;
  ebitda: number;
  profitMargin: number;
  revenueGrowth: number;    // percentage
  expenseGrowth: number;   // percentage
  operatingCashFlow: number;
  freeCashFlow: number;
}

/**
 * Country information
 */
interface CountryInfo {
  code: string;          // ISO 3166-1 alpha-3 (e.g., "NGA")
  name: string;
  flag: string;          // Emoji flag (e.g., "🇳🇬")
  currency: string;       // ISO 4217 (e.g., "NGN")
  region: string;         // e.g., "West Africa"
  timezone: string;       // IANA timezone (e.g., "Africa/Lagos")
}

/**
 * Country finance summary
 */
interface CountryFinanceSummary {
  country: CountryInfo;
  metrics: {
    revenue: number;
    expenses: number;
    netIncome: number;
    budgetAttainment: number;  // percentage
    arBalance: number;         // Accounts Receivable
    apBalance: number;         // Accounts Payable
  };
  trend: {
    revenue: 'up' | 'down' | 'neutral';
    expenses: 'up' | 'down' | 'neutral';
    netIncome: 'up' | 'down' | 'neutral';
  };
  healthScore: number;      // 0-100
  status: 'on_track' | 'at_risk' | 'off_track';
  lastUpdated: string;      // ISO 8601
}

/**
 * Top performer
 */
interface FinanceTopPerformer {
  country: CountryInfo;
  metric: 'revenue' | 'netIncome' | 'budgetAttainment';
  value: number;
  growth: number;  // percentage
}

/**
 * Dashboard alert
 */
interface DashboardAlert {
  id: string;
  type: 'budget_approval' | 'fraud_alert' | 'cash_warning' | 'compliance' | 'info';
  severity: 'high' | 'medium' | 'low';
  title: string;
  message: string;
  country?: CountryInfo;
  actionUrl?: string;
  createdAt: string;
  expiresAt?: string;
}

/**
 * Pending approval count
 */
interface PendingApprovalCount {
  total: number;
  highPriority: number;
  budgetApprovals: number;
  fraudAlerts: number;
}
```

### 2.3 Budget Models

```typescript
/**
 * Budget approval request
 */
interface BudgetApprovalRequest {
  id: string;
  type: 'budget_increase' | 'new_budget' | 'budget_transfer';
  priority: 'high' | 'medium' | 'low';
  status: 'pending' | 'approved' | 'rejected' | 'cancelled';

  // Request details
  country: CountryInfo;
  department: string;
  period: string;         // e.g., "Q2-2026"
  currency: string;

  // Budget amounts
  requestedAmount: number;
  currentAmount?: number;
  variance?: number;

  // Breakdown
  breakdown: BudgetCategoryAmount[];

  // Justification
  justification: string;
  requestedBy: RequesterInfo;
  requestedAt: string;

  // Approval info
  dueDate: string;
  approvedBy?: ApproverInfo;
  approvedAt?: string;
  approvalComments?: string;

  // AI Analysis
  aiAnalysis?: {
    riskScore: number;           // 0-100
    riskFactors: string[];
    roiPotential?: number;
    recommendation: 'approve' | 'review' | 'reject';
    confidence: number;         // 0-1
  };
}

/**
 * Budget category amount
 */
interface BudgetCategoryAmount {
  category: string;
  amount: number;
  percentage: number;
}

/**
 * Requester info
 */
interface RequesterInfo {
  id: string;
  name: string;
  email: string;
  role: string;
  avatar?: string;
}

/**
 * Approver info
 */
interface ApproverInfo {
  id: string;
  name: string;
  email: string;
  role: string;
  avatar?: string;
}

/**
 * Budget by country
 */
interface BudgetByCountry {
  country: CountryInfo;
  budget: number;
  actual: number;
  variance: number;
  variancePercent: number;
}

/**
 * Budget by category
 */
interface BudgetByCategory {
  category: string;
  budget: number;
  actual: number;
  variance: number;
  variancePercent: number;
}

/**
 * Budget utilization
 */
interface BudgetUtilization {
  overall: number;        // percentage
  byCountry: BudgetUtilizationByCountry[];
  byCategory: BudgetUtilizationByCategory[];
}

/**
 * Budget utilization by country
 */
interface BudgetUtilizationByCountry {
  country: CountryInfo;
  utilization: number;    // percentage
  remaining: number;
  status: 'under' | 'on_track' | 'over';
}
```

### 2.4 Treasury Models

```typescript
/**
 * Cash by country
 */
interface CashByCountry {
  country: CountryInfo;
  cash: number;
  operatingCash: number;
  investmentCash: number;
  utilizationRate: number;
  trend: 'up' | 'down' | 'neutral';
}

/**
 * Cash flow forecast data
 */
interface CashFlowForecastData {
  date: string;
  projectedInflow: number;
  projectedOutflow: number;
  netCashFlow: number;
  cumulativeCash: number;
  confidence: number;     // 0-1
}

/**
 * Cash flow low point
 */
interface CashFlowLowPoint {
  date: string;
  amount: number;
  daysFromNow: number;
  severity: 'low' | 'medium' | 'high';
}

/**
 * Treasury recommendation
 */
interface TreasuryRecommendation {
  id: string;
  type: 'transfer' | 'investment' | 'funding' | 'hedge';
  priority: 'high' | 'medium' | 'low';
  title: string;
  description: string;
  amount?: number;
  from?: string;         // Country or account
  to?: string;           // Country or account
  estimatedImpact?: number;
  confidence: number;     // 0-1
  validUntil: string;
}

/**
 * FX exposure by currency
 */
interface FXExposureByCurrency {
  currency: string;
  exposure: number;
  risk: 'low' | 'medium' | 'high';
  hedge: number;
  netExposure: number;
  spotRate: number;
  forwardRate?: number;
}

/**
 * FX hedge
 */
interface FXHedge {
  id: string;
  currency: string;
  type: 'forward' | 'option' | 'swap';
  amount: number;
  rate: number;
  maturityDate: string;
  counterparty: string;
  status: 'active' | 'expired' | 'settled';
}
```

### 2.5 AI Insights Models

```typescript
/**
 * AI-generated insight
 */
interface AIInsight {
  id: string;
  type: InsightType;
  category: 'revenue' | 'expenses' | 'fraud' | 'treasury' | 'compliance' | 'optimization';
  severity: 'high' | 'medium' | 'low';
  confidence: number;     // 0-1

  // Content
  title: string;
  summary: string;
  description: string;

  // Related entities
  countries?: CountryInfo[];
  period?: DateRange;

  // Metrics
  impact?: {
    metric: string;
    currentValue: number;
    projectedValue?: number;
    change?: number;
    changePercent?: number;
  };

  // Actionable recommendations
  recommendations: Recommendation[];

  // Metadata
  generatedAt: string;
  modelVersion: string;
  expiresAt: string;
}

/**
 * Insight types
 */
type InsightType =
  | 'forecast'
  | 'anomaly'
  | 'fraud_detection'
  | 'opportunity'
  | 'risk_warning'
  | 'compliance_issue'
  | 'optimization';

/**
 * Recommendation
 */
interface Recommendation {
  id: string;
  action: string;
  priority: 'high' | 'medium' | 'low';
  estimatedImpact?: string;
  effort: 'low' | 'medium' | 'high';
  status: 'pending' | 'in_progress' | 'completed' | 'dismissed';
}

/**
 * Fraud alert
 */
interface FraudAlert {
  id: string;
  country: CountryInfo;
  status: 'open' | 'investigating' | 'resolved' | 'dismissed';
  riskScore: number;      // 0-100
  confidence: number;     // 0-1

  // Anomaly details
  anomalyType: string;
  detectedAt: string;
  description: string;

  // Related transactions
  transactionCount: number;
  totalAmount: number;
  relatedTransactions: FraudTransaction[];

  // Risk factors
  riskFactors: RiskFactor[];

  // Assignment
  assignedTo?: string;
  assignedAt?: string;
  assignedBy?: string;

  // Resolution
  resolvedAt?: string;
  resolution?: string;
  falsePositive?: boolean;

  // AI metadata
  modelVersion: string;
  modelConfidence: number;
}

/**
 * Fraud transaction
 */
interface FraudTransaction {
  id: string;
  date: string;
  amount: number;
  currency: string;
  vendor: string;
  category: string;
  reference: string;
}

/**
 * Risk factor
 */
interface RiskFactor {
  factor: string;
  score: number;         // contribution to total risk score
  description: string;
}

/**
 * Revenue forecast
 */
interface RevenueForecast {
  period: DateRange;
  forecastedAmount: number;
  confidence: number;     // 0-1
  lowerBound: number;
  upperBound: number;
  growthRate: number;

  // Model info
  modelName: string;
  modelAccuracy: number;  // MAPE or similar
  trainingDataPoints: number;

  // Drivers
  drivers: ForecastDriver[];
  risks: ForecastRisk[];
}

/**
 * Forecast driver
 */
interface ForecastDriver {
  factor: string;
  impact: number;         // contribution to forecast
  direction: 'positive' | 'negative';
  description: string;
}

/**
 * Forecast risk
 */
interface ForecastRisk {
  risk: string;
  impact: number;
  probability: 'high' | 'medium' | 'low';
  mitigation?: string;
}

/**
 * Forecast scenario
 */
interface ForecastScenario {
  name: 'baseline' | 'optimistic' | 'pessimistic' | 'custom';
  forecast: number;
  confidence: number;
  assumptions: string[];
}

/**
 * Forecast model info
 */
interface ForecastModelInfo {
  name: string;
  type: 'lstm' | 'arima' | 'prophet' | 'ensemble';
  version: string;
  accuracy: number;       // MAPE
  lastTrained: string;
  trainingPeriod: DateRange;
}
```

### 2.6 Report Models

```typescript
/**
 * Report information
 */
interface ReportInfo {
  id: string;
  name: string;
  reportType: ReportType;
  status: ReportStatus;

  // Report configuration
  period: DateRange;
  countries: CountryInfo[];
  currency: string;

  // Output
  format: 'pdf' | 'excel' | 'csv';
  fileSize?: number;
  pageCount?: number;

  // Access
  createdBy: RequesterInfo;
  createdAt: string;
  expiresAt?: string;

  // Download
  downloadUrls?: {
    pdf?: string;
    excel?: string;
    csv?: string;
  };

  // AI features
  includeAIInsights: boolean;
  includeCharts: boolean;
}

/**
 * Report type
 */
type ReportType =
  | 'pl'                  // Profit & Loss
  | 'balance_sheet'       // Balance Sheet
  | 'cash_flow'           // Cash Flow Statement
  | 'budget_variance'     // Budget vs Actual
  | 'country_comparison'  // Multi-country comparison
  | 'tax'                 // Tax Summary
  | 'audit'               // Audit Report
  | 'custom';             // Custom report

/**
 * Report status
 */
type ReportStatus =
  | 'scheduled'
  | 'generating'
  | 'completed'
  | 'failed'
  | 'expired';

/**
 * Report section
 */
type ReportSection =
  | 'executive_summary'
  | 'revenue_analysis'
  | 'expense_analysis'
  | 'profit_loss'
  | 'balance_sheet'
  | 'cash_flow'
  | 'budget_variance'
  | 'country_breakdown'
  | 'ai_insights'
  | 'charts';

/**
 * Scheduled report info
 */
interface ScheduledReportInfo {
  id: string;
  name: string;
  reportType: ReportType;
  schedule: ReportSchedule;
  config: ReportConfig;
  active: boolean;
  nextRun: string;
  lastRun?: string;
  createdBy: RequesterInfo;
  recipients: string[];    // Email addresses
}

/**
 * Report schedule
 */
interface ReportSchedule {
  frequency: 'daily' | 'weekly' | 'monthly' | 'quarterly';
  dayOfWeek?: number;      // 0-6 (for weekly)
  dayOfMonth?: number;     // 1-31 (for monthly)
  time: string;            // HH:MM format
  timezone: string;
}

/**
 * Report configuration
 */
interface ReportConfig {
  period: PeriodPreset | 'custom';
  customPeriodStart?: string;
  customPeriodEnd?: string;
  countries: string[];     // Empty for all
  currency: string;
  sections: ReportSection[];
  format: 'pdf' | 'excel' | 'both';
  includeAIInsights: boolean;
  includeCharts: boolean;
}
```

---

## 3. MOCK DATA

### 3.1 Authentication Mock Data

```typescript
// Mock login response
const mockLoginResponse: LoginResponse = {
  token: "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.mock.token",
  refreshToken: "refresh.token.mock",
  expiresIn: 3600,
  user: {
    id: "usr-finance-001",
    email: "cfo@gogidix.com",
    firstName: "Sarah",
    lastName: "O'Connor",
    avatar: "https://api.dicebear.com/7.x/avataaars/svg?seed=Sarah",
    roles: ["VP_FINANCE"],
    currentRole: "VP_FINANCE",
    tenantId: "gogidix-hq",
    tenantName: "Gogidix HQ",
    location: "Ireland HQ",
    preferences: {
      theme: "light",
      defaultCurrency: "USD",
      defaultPeriod: "this_month",
      notifications: {
        budgetApproval: true,
        fraudAlert: true,
        cashFlowWarning: true,
        reportReady: true,
        systemUpdates: false,
        emailDigest: "daily"
      },
      dashboardLayout: {
        cards: [],
        cardOrder: ["revenue", "expenses", "netIncome", "cash", "debt", "alerts"],
        collapsedSections: []
      }
    },
    createdAt: "2024-01-15T10:30:00Z",
    lastLogin: "2026-02-17T08:15:00Z"
  },
  financeScope: {
    type: "GLOBAL",
    permissions: [
      "VIEW_ALL",
      "APPROVE_BUDGETS",
      "REVIEW_FRAUD_ALERTS",
      "EXPORT_REPORTS",
      "MANAGE_TREASURY",
      "VIEW_TAX_COMPLIANCE",
      "MANAGE_USERS"
    ],
    currencyPreferences: ["USD", "EUR", "GBP"]
  }
};

// Mock logout response
const mockLogoutResponse = {
  success: true,
  message: "Logged out successfully"
};

// Mock me response
const mockMeResponse: MeResponse = {
  user: mockLoginResponse.user,
  financeScope: mockLoginResponse.financeScope,
  permissions: mockLoginResponse.financeScope.permissions
};
```

### 3.2 Dashboard Mock Data

```typescript
// Mock global dashboard response
const mockGlobalDashboardResponse: GlobalDashboardResponse = {
  period: {
    start: "2026-02-01T00:00:00Z",
    end: "2026-02-29T23:59:59Z",
    preset: "this_month"
  },
  financialHealthScore: 87,
  globalMetrics: {
    totalRevenue: 42500000,
    totalExpenses: 32100000,
    netIncome: 10400000,
    totalCash: 15200000,
    totalDebt: 5100000,
    ebitda: 12800000,
    profitMargin: 24.5,
    revenueGrowth: 8.2,
    expenseGrowth: 3.1,
    operatingCashFlow: 9800000,
    freeCashFlow: 7500000
  },
  countriesSummary: [
    {
      country: {
        code: "NGA",
        name: "Nigeria",
        flag: "🇳🇬",
        currency: "NGN",
        region: "West Africa",
        timezone: "Africa/Lagos"
      },
      metrics: {
        revenue: 8200000,
        expenses: 6100000,
        netIncome: 2100000,
        budgetAttainment: 94,
        arBalance: 1800000,
        apBalance: 1200000
      },
      trend: {
        revenue: "up",
        expenses: "up",
        netIncome: "up"
      },
      healthScore: 94,
      status: "on_track",
      lastUpdated: "2026-02-17T09:00:00Z"
    },
    {
      country: {
        code: "KEN",
        name: "Kenya",
        flag: "🇰🇪",
        currency: "KES",
        region: "East Africa",
        timezone: "Africa/Nairobi"
      },
      metrics: {
        revenue: 5100000,
        expenses: 4200000,
        netIncome: 900000,
        budgetAttainment: 91,
        arBalance: 800000,
        apBalance: 600000
      },
      trend: {
        revenue: "up",
        expenses: "up",
        netIncome: "up"
      },
      healthScore: 91,
      status: "on_track",
      lastUpdated: "2026-02-17T08:45:00Z"
    },
    {
      country: {
        code: "ZAF",
        name: "South Africa",
        flag: "🇿🇦",
        currency: "ZAR",
        region: "Southern Africa",
        timezone: "Africa/Johannesburg"
      },
      metrics: {
        revenue: 4800000,
        expenses: 4500000,
        netIncome: 300000,
        budgetAttainment: 78,
        arBalance: 1200000,
        apBalance: 1500000
      },
      trend: {
        revenue: "down",
        expenses: "up",
        netIncome: "down"
      },
      healthScore: 78,
      status: "at_risk",
      lastUpdated: "2026-02-17T08:30:00Z"
    },
    {
      country: {
        code: "GHA",
        name: "Ghana",
        flag: "🇬🇭",
        currency: "GHS",
        region: "West Africa",
        timezone: "Africa/Accra"
      },
      metrics: {
        revenue: 3200000,
        expenses: 2800000,
        netIncome: 400000,
        budgetAttainment: 96,
        arBalance: 400000,
        apBalance: 300000
      },
      trend: {
        revenue: "up",
        expenses: "up",
        netIncome: "up"
      },
      healthScore: 96,
      status: "on_track",
      lastUpdated: "2026-02-17T09:15:00Z"
    },
    {
      country: {
        code: "IRL",
        name: "Ireland",
        flag: "🇮🇪",
        currency: "EUR",
        region: "Europe",
        timezone: "Europe/Dublin"
      },
      metrics: {
        revenue: 2900000,
        expenses: 2500000,
        netIncome: 400000,
        budgetAttainment: 89,
        arBalance: 300000,
        apBalance: 200000
      },
      trend: {
        revenue: "up",
        expenses: "up",
        netIncome: "up"
      },
      healthScore: 89,
      status: "on_track",
      lastUpdated: "2026-02-17T08:00:00Z"
    }
  ],
  topPerformers: [
    {
      country: {
        code: "GHA",
        name: "Ghana",
        flag: "🇬🇭",
        currency: "GHS",
        region: "West Africa",
        timezone: "Africa/Accra"
      },
      metric: "budgetAttainment",
      value: 96,
      growth: 22
    },
    {
      country: {
        code: "NGA",
        name: "Nigeria",
        flag: "🇳🇬",
        currency: "NGN",
        region: "West Africa",
        timezone: "Africa/Lagos"
      },
      metric: "revenue",
      value: 8200000,
      growth: 12
    },
    {
      country: {
        code: "NGA",
        name: "Nigeria",
        flag: "🇳🇬",
        currency: "NGN",
        region: "West Africa",
        timezone: "Africa/Lagos"
      },
      metric: "netIncome",
      value: 2100000,
      growth: 18
    }
  ],
  pendingApprovals: {
    total: 3,
    highPriority: 1,
    budgetApprovals: 2,
    fraudAlerts: 1
  },
  alerts: [
    {
      id: "alert-001",
      type: "budget_approval",
      severity: "high",
      title: "Nigeria Q2 budget approval required",
      message: "Budget request of $2.1M awaiting review for Nigeria operations",
      country: {
        code: "NGA",
        name: "Nigeria",
        flag: "🇳🇬",
        currency: "NGN",
        region: "West Africa",
        timezone: "Africa/Lagos"
      },
      actionUrl: "/budgets/approvals/BUD-2026-0217-001",
      createdAt: "2026-02-17T07:30:00Z",
      expiresAt: "2026-02-20T23:59:59Z"
    },
    {
      id: "alert-002",
      type: "fraud_alert",
      severity: "high",
      title: "Unusual expense pattern detected in Kenya",
      message: "AI detected 47 duplicate transactions to same vendor",
      country: {
        code: "KEN",
        name: "Kenya",
        flag: "🇰🇪",
        currency: "KES",
        region: "East Africa",
        timezone: "Africa/Nairobi"
      },
      actionUrl: "/alerts/fraud/FRAUD-2026-0217-001",
      createdAt: "2026-02-17T09:23:00Z",
      expiresAt: "2026-02-24T09:23:00Z"
    },
    {
      id: "alert-003",
      type: "cash_warning",
      severity: "medium",
      title: "Cash flow warning for Week 3",
      message: "Projected cash position may fall below $12M",
      actionUrl: "/treasury",
      createdAt: "2026-02-17T08:00:00Z",
      expiresAt: "2026-02-17T23:59:59Z"
    }
  ],
  currency: "USD",
  lastUpdated: "2026-02-17T09:30:00Z"
};
```

### 3.3 Budget Mock Data

```typescript
// Mock budget approval request
const mockBudgetApprovalRequest: BudgetApprovalRequest = {
  id: "BUD-2026-0217-001",
  type: "budget_increase",
  priority: "high",
  status: "pending",

  country: {
    code: "NGA",
    name: "Nigeria",
    flag: "🇳🇬",
    currency: "NGN",
    region: "West Africa",
    timezone: "Africa/Lagos"
  },
  department: "Nigeria Operations",
  period: "Q2-2026",
  currency: "NGN",

  requestedAmount: 2100000,
  currentAmount: 1800000,
  variance: 300000,

  breakdown: [
    {
      category: "Personnel",
      amount: 1200000,
      percentage: 57
    },
    {
      category: "Operations",
      amount: 600000,
      percentage: 29
    },
    {
      category: "Marketing",
      amount: 200000,
      percentage: 9
    },
    {
      category: "Capital",
      amount: 100000,
      percentage: 5
    }
  ],

  justification: "Q2 budget increase to support expansion into Lagos market. Additional sales team and marketing investment required to capture market opportunity.",

  requestedBy: {
    id: "usr-coutry-finance-nga",
    name: "Adebayo Okafor",
    email: "a.okafor@gogidix.com.ng",
    role: "Country Finance Manager - Nigeria",
    avatar: "https://api.dicebear.com/7.x/avataaars/svg?seed=Adebayo"
  },
  requestedAt: "2026-02-15T10:30:00Z",

  dueDate: "2026-02-20T23:59:59Z",

  aiAnalysis: {
    riskScore: 12,
    riskFactors: [
      "12% above recommended amount based on historical patterns",
      "Market uncertainty in Lagos region",
      "FX volatility risk for NGN"
    ],
    roiPotential: 450000,
    recommendation: "review",
    confidence: 0.78
  }
};

// Mock budget summary
const mockBudgetSummaryResponse: BudgetSummaryResponse = {
  totalBudget: 38000000,
  totalActual: 42500000,
  totalVariance: 4500000,
  budgetByCountry: [
    {
      country: {
        code: "NGA",
        name: "Nigeria",
        flag: "🇳🇬",
        currency: "NGN",
        region: "West Africa",
        timezone: "Africa/Lagos"
      },
      budget: 8500000,
      actual: 8200000,
      variance: -300000,
      variancePercent: 96
    },
    {
      country: {
        code: "KEN",
        name: "Kenya",
        flag: "🇰🇪",
        currency: "KES",
        region: "East Africa",
        timezone: "Africa/Nairobi"
      },
      budget: 5000000,
      actual: 5100000,
      variance: 100000,
      variancePercent: 102
    },
    {
      country: {
        code: "ZAF",
        name: "South Africa",
        flag: "🇿🇦",
        currency: "ZAR",
        region: "Southern Africa",
        timezone: "Africa/Johannesburg"
      },
      budget: 4200000,
      actual: 4800000,
      variance: 600000,
      variancePercent: 114
    },
    {
      country: {
        code: "GHA",
        name: "Ghana",
        flag: "🇬🇭",
        currency: "GHS",
        region: "West Africa",
        timezone: "Africa/Accra"
      },
      budget: 3000000,
      actual: 3200000,
      variance: 200000,
      variancePercent: 107
    },
    {
      country: {
        code: "IRL",
        name: "Ireland",
        flag: "🇮🇪",
        currency: "EUR",
        region: "Europe",
        timezone: "Europe/Dublin"
      },
      budget: 2800000,
      actual: 2900000,
      variance: 100000,
      variancePercent: 104
    }
  ],
  budgetByCategory: [
    {
      category: "Personnel",
      budget: 22000000,
      actual: 23500000,
      variance: 1500000,
      variancePercent: 107
    },
    {
      category: "Operations",
      budget: 8000000,
      actual: 8500000,
      variance: 500000,
      variancePercent: 106
    },
    {
      category: "Marketing",
      budget: 4000000,
      actual: 4200000,
      variance: 200000,
      variancePercent: 105
    },
    {
      category: "Capital",
      budget: 2000000,
      actual: 2300000,
      variance: 300000,
      variancePercent: 115
    },
    {
      category: "Admin",
      budget: 2000000,
      actual: 1800000,
      variance: -200000,
      variancePercent: 90
    }
  ],
  budgetUtilization: {
    overall: 112,
    byCountry: [
      {
        country: {
          code: "NGA",
          name: "Nigeria",
          flag: "🇳🇬",
          currency: "NGN",
          region: "West Africa",
          timezone: "Africa/Lagos"
        },
        utilization: 96,
        remaining: 300000,
        status: "under"
      },
      {
        country: {
          code: "KEN",
          name: "Kenya",
          flag: "🇰🇪",
          currency: "KES",
          region: "East Africa",
          timezone: "Africa/Nairobi"
        },
        utilization: 102,
        remaining: -100000,
        status: "over"
      },
      {
        country: {
          code: "ZAF",
          name: "South Africa",
          flag: "🇿🇦",
          currency: "ZAR",
          region: "Southern Africa",
          timezone: "Africa/Johannesburg"
        },
        utilization: 114,
        remaining: -600000,
        status: "over"
      },
      {
        country: {
          code: "GHA",
          name: "Ghana",
          flag: "🇬🇭",
          currency: "GHS",
          region: "West Africa",
          timezone: "Africa/Accra"
        },
        utilization: 107,
        remaining: -200000,
        status: "over"
      },
      {
        country: {
          code: "IRL",
          name: "Ireland",
          flag: "🇮🇪",
          currency: "EUR",
          region: "Europe",
          timezone: "Europe/Dublin"
        },
        utilization: 104,
        remaining: -100000,
        status: "over"
      }
    ],
    byCategory: []
  },
  currency: "USD",
  period: "Q1-2026"
};
```

### 3.4 Treasury Mock Data

```typescript
// Mock treasury position
const mockTreasuryPositionResponse: TreasuryPositionResponse = {
  totalCash: 15200000,
  operatingCash: 12500000,
  investmentCash: 2700000,
  cashByCountry: [
    {
      country: {
        code: "NGA",
        name: "Nigeria",
        flag: "🇳🇬",
        currency: "NGN",
        region: "West Africa",
        timezone: "Africa/Lagos"
      },
      cash: 4200000,
      operatingCash: 3800000,
      investmentCash: 400000,
      utilizationRate: 95,
      trend: "up"
    },
    {
      country: {
        code: "KEN",
        name: "Kenya",
        flag: "🇰🇪",
        currency: "KES",
        region: "East Africa",
        timezone: "Africa/Nairobi"
      },
      cash: 2800000,
      operatingCash: 2600000,
      investmentCash: 200000,
      utilizationRate: 88,
      trend: "neutral"
    },
    {
      country: {
        code: "ZAF",
        name: "South Africa",
        flag: "🇿🇦",
        currency: "ZAR",
        region: "Southern Africa",
        timezone: "Africa/Johannesburg"
      },
      cash: 2100000,
      operatingCash: 1900000,
      investmentCash: 200000,
      utilizationRate: 92,
      trend: "down"
    },
    {
      country: {
        code: "GHA",
        name: "Ghana",
        flag: "🇬🇭",
        currency: "GHS",
        region: "West Africa",
        timezone: "Africa/Accra"
      },
      cash: 1900000,
      operatingCash: 1700000,
      investmentCash: 200000,
      utilizationRate: 91,
      trend: "up"
    },
    {
      country: {
        code: "IRL",
        name: "Ireland",
        flag: "🇮🇪",
        currency: "EUR",
        region: "Europe",
        timezone: "Europe/Dublin"
      },
      cash: 1700000,
      operatingCash: 1500000,
      investmentCash: 200000,
      utilizationRate: 85,
      trend: "up"
    }
  ],
  utilizationRate: 85,
  targetCash: 18000000,
  currency: "USD",
  lastUpdated: "2026-02-17T09:00:00Z"
};

// Mock cash flow forecast
const mockCashFlowForecastResponse: CashFlowForecastResponse = {
  forecast: [
    {
      date: "2026-02-18",
      projectedInflow: 450000,
      projectedOutflow: 380000,
      netCashFlow: 70000,
      cumulativeCash: 15270000,
      confidence: 0.92
    },
    {
      date: "2026-02-19",
      projectedInflow: 420000,
      projectedOutflow: 390000,
      netCashFlow: 30000,
      cumulativeCash: 15300000,
      confidence: 0.90
    },
    // ... more days
    {
      date: "2026-03-01",
      projectedInflow: 380000,
      projectedOutflow: 520000,
      netCashFlow: -140000,
      cumulativeCash: 14100000,
      confidence: 0.85
    },
    {
      date: "2026-03-08",
      projectedInflow: 350000,
      projectedOutflow: 540000,
      netCashFlow: -190000,
      cumulativeCash: 11200000,
      confidence: 0.82
    }
  ],
  lowPoint: {
    date: "2026-03-08",
    amount: 11200000,
    daysFromNow: 19,
    severity: "medium"
  },
  recommendations: [
    {
      id: "rec-treasury-001",
      type: "transfer",
      priority: "medium",
      title: "Transfer funds from investments",
      description: "Transfer $2M from investment account to operating cash to maintain liquidity during Week 3",
      amount: 2000000,
      from: "Investment Account",
      to: "Operating Cash",
      estimatedImpact: 2000000,
      confidence: 0.88,
      validUntil: "2026-02-24T23:59:59Z"
    },
    {
      id: "rec-treasury-002",
      type: "funding",
      priority: "low",
      title: "Review Nigeria cash position",
      description: "Nigeria cash utilization at 95%, consider moving excess to investment",
      amount: 400000,
      from: "Nigeria Operating",
      to: "Investment Account",
      confidence: 0.75,
      validUntil: "2026-02-28T23:59:59Z"
    }
  ],
  currency: "USD",
  periodDays: 30
};

// Mock FX exposure
const mockFXExposureResponse: FXExposureResponse = {
  totalExposure: -3200000,
  exposureByCurrency: [
    {
      currency: "NGN",
      exposure: -3200000,
      risk: "high",
      hedge: 2500000,
      netExposure: -700000,
      spotRate: 1550,
      forwardRate: 1580
    },
    {
      currency: "KES",
      exposure: -1800000,
      risk: "medium",
      hedge: 1000000,
      netExposure: -800000,
      spotRate: 130,
      forwardRate: 132
    },
    {
      currency: "ZAR",
      exposure: -1200000,
      risk: "low",
      hedge: 1000000,
      netExposure: -200000,
      spotRate: 18.5,
      forwardRate: 18.8
    },
    {
      currency: "GHS",
      exposure: -900000,
      risk: "low",
      hedge: 700000,
      netExposure: -200000,
      spotRate: 12.5,
      forwardRate: 12.7
    },
    {
      currency: "EUR",
      exposure: 2100000,
      risk: "low",
      hedge: 0,
      netExposure: 2100000,
      spotRate: 1.08,
      forwardRate: null
    }
  ],
  hedges: [
    {
      id: "hedge-001",
      currency: "NGN",
      type: "forward",
      amount: 1500000,
      rate: 1580,
      maturityDate: "2026-03-31",
      counterparty: "Standard Bank",
      status: "active"
    },
    {
      id: "hedge-002",
      currency: "NGN",
      type: "forward",
      amount: 1000000,
      rate: 1560,
      maturityDate: "2026-04-30",
      counterparty: "Access Bank",
      status: "active"
    }
  ],
  netExposure: -3200000,
  riskLevel: "medium",
  baseCurrency: "USD",
  lastUpdated: "2026-02-17T09:00:00Z"
};
```

### 3.5 AI Insights Mock Data

```typescript
// Mock AI insights
const mockAIInsightsResponse: AIInsightsResponse = {
  insights: [
    {
      id: "insight-ai-001",
      type: "forecast",
      category: "revenue",
      severity: "low",
      confidence: 0.95,
      title: "Revenue forecast for March 2026",
      summary: "AI models project $48.2M revenue for March, a 13.4% increase from February",
      description: "LSTM neural network trained on 24 months of historical data predicts continued growth driven by Nigeria expansion and enterprise renewals. Confidence interval suggests 95% probability of revenue between $44.8M and $52.1M.",
      countries: [
        {
          code: "NGA",
          name: "Nigeria",
          flag: "🇳🇬",
          currency: "NGN",
          region: "West Africa",
          timezone: "Africa/Lagos"
        }
      ],
      period: {
        start: "2026-03-01T00:00:00Z",
        end: "2026-03-31T23:59:59Z"
      },
      impact: {
        metric: "revenue",
        currentValue: 42500000,
        projectedValue: 48200000,
        change: 5700000,
        changePercent: 13.4
      },
      recommendations: [
        {
          id: "rec-001",
          action: "Allocate additional inventory for Nigeria market",
          priority: "high",
          estimatedImpact: "+$800K additional revenue",
          effort: "medium",
          status: "pending"
        },
        {
          id: "rec-002",
          action: "Prepare enterprise renewal outreach for Q2",
          priority: "high",
          estimatedImpact: "+$1.2M revenue retention",
          effort: "low",
          status: "pending"
        }
      ],
      generatedAt: "2026-02-17T08:00:00Z",
      modelVersion: "lstm-v3.2",
      expiresAt: "2026-02-24T08:00:00Z"
    },
    {
      id: "insight-ai-002",
      type: "anomaly",
      category: "expenses",
      severity: "high",
      confidence: 0.87,
      title: "Unusual expense pattern detected in Kenya",
      summary: "47 duplicate transactions to same vendor within 3-day period",
      description: "AI anomaly detection identified unusual pattern: 47 transactions of exactly $45,000 each to 'Logistics Partner Ltd' processed within 72 hours. This represents $2.115M total and deviates significantly from historical patterns.",
      countries: [
        {
          code: "KEN",
          name: "Kenya",
          flag: "🇰🇪",
          currency: "KES",
          region: "East Africa",
          timezone: "Africa/Nairobi"
        }
      ],
      period: {
        start: "2026-02-14T00:00:00Z",
        end: "2026-02-17T09:23:00Z"
      },
      recommendations: [
        {
          id: "rec-fraud-001",
          action: "Review transactions with Kenya finance team",
          priority: "high",
          effort: "medium",
          status: "pending"
        },
        {
          id: "rec-fraud-002",
          action: "Contact vendor for clarification",
          priority: "high",
          effort: "low",
          status: "pending"
        }
      ],
      generatedAt: "2026-02-17T09:23:00Z",
      modelVersion: "isolation-forest-v2.1",
      expiresAt: "2026-02-18T09:23:00Z"
    },
    {
      id: "insight-ai-003",
      type: "risk_warning",
      category: "treasury",
      severity: "medium",
      confidence: 0.82,
      title: "Cash flow warning for Week 3",
      summary: "Projected cash position may fall below $12M during Week 3",
      description: "Based on current cash flow forecast, operating cash is projected to reach a low point of $11.2M during Week 3. This is below the recommended minimum of $12M. Recommend transferring funds from investments or delaying non-essential expenditures.",
      recommendations: [
        {
          id: "rec-treasury-001",
          action: "Transfer $2M from investments to operating cash",
          priority: "medium",
          estimatedImpact: "Maintain minimum liquidity threshold",
          effort: "low",
          status: "pending"
        }
      ],
      generatedAt: "2026-02-17T08:00:00Z",
      modelVersion: "lstm-cash-v1.5",
      expiresAt: "2026-02-18T08:00:00Z"
    }
  ],
  summary: {
    total: 3,
    highPriority: 1,
    mediumPriority: 1,
    lowPriority: 1
  },
  lastGenerated: "2026-02-17T09:30:00Z",
  modelVersion: "ensemble-v4.0"
};

// Mock fraud alerts
const mockFraudAlertsResponse: FraudAlertsResponse = {
  alerts: [
    {
      id: "FRAUD-2026-0217-001",
      country: {
        code: "KEN",
        name: "Kenya",
        flag: "🇰🇪",
        currency: "KES",
        region: "East Africa",
        timezone: "Africa/Nairobi"
      },
      status: "open",
      riskScore: 87,
      confidence: 0.87,
      anomalyType: "Unusual Expense Pattern",
      detectedAt: "2026-02-17T09:23:00Z",
      description: "47 duplicate transactions of $45,000 each to Logistics Partner Ltd within 3 days",
      transactionCount: 47,
      totalAmount: 2115000,
      relatedTransactions: [
        {
          id: "TX-001234",
          date: "2026-02-14T14:32:00Z",
          amount: 45000,
          currency: "USD",
          vendor: "Logistics Partner Ltd",
          category: "Operations",
          reference: "INV-2026-0214-001"
        },
        {
          id: "TX-001235",
          date: "2026-02-15T09:15:00Z",
          amount: 45000,
          currency: "USD",
          vendor: "Logistics Partner Ltd",
          category: "Operations",
          reference: "INV-2026-0215-003"
        },
        {
          id: "TX-001236",
          date: "2026-02-15T14:45:00Z",
          amount: 45000,
          currency: "USD",
          vendor: "Logistics Partner Ltd",
          category: "Operations",
          reference: "INV-2026-0215-007"
        }
      ],
      riskFactors: [
        {
          factor: "High transaction frequency",
          score: 35,
          description: "47 transactions to same vendor in 3 days is 234% above normal"
        },
        {
          factor: "Same vendor pattern",
          score: 22,
          description: "All transactions use identical amount ($45,000)"
        },
        {
          factor: "Large amounts",
          score: 15,
          description: "Individual amounts are 180% above category average"
        },
        {
          factor: "Off-cycle timing",
          score: 15,
          description: "Transactions processed outside normal business hours"
        }
      ],
      assignedTo: "usr-coutry-finance-ken",
      assignedAt: "2026-02-17T09:30:00Z",
      assignedBy: "usr-finance-001",
      modelVersion: "isolation-forest-v2.1",
      modelConfidence: 0.87
    }
  ],
  totalCount: 1,
  highRiskCount: 1,
  openInvestigations: 1,
  page: 1,
  pageSize: 20
};
```

### 3.6 Reports Mock Data

```typescript
// Mock reports list
const mockReportsListResponse: ReportsListResponse = {
  reports: [
    {
      id: "RPT-2026-0216-001",
      name: "Consolidated P&L February 2026",
      reportType: "pl",
      status: "completed",
      period: {
        start: "2026-02-01T00:00:00Z",
        end: "2026-02-29T23:59:59Z"
      },
      countries: [
        {
          code: "NGA",
          name: "Nigeria",
          flag: "🇳🇬",
          currency: "NGN",
          region: "West Africa",
          timezone: "Africa/Lagos"
        },
        {
          code: "KEN",
          name: "Kenya",
          flag: "🇰🇪",
          currency: "KES",
          region: "East Africa",
          timezone: "Africa/Nairobi"
        }
      ],
      currency: "USD",
      format: "pdf",
      fileSize: 2458624,
      pageCount: 18,
      createdBy: {
        id: "usr-finance-002",
        name: "System",
        email: "system@gogidix.com",
        role: "Finance Bot",
        avatar: "https://api.dicebear.com/7.x/bottts/svg?seed=System"
      },
      createdAt: "2026-02-16T10:00:00Z",
      expiresAt: "2026-05-17T10:00:00Z",
      downloadUrls: {
        pdf: "https://storage.gogidix.com/finance/reports/RPT-2026-0216-001.pdf",
        excel: "https://storage.gogidix.com/finance/reports/RPT-2026-0216-001.xlsx"
      },
      includeAIInsights: true,
      includeCharts: true
    }
  ],
  scheduledReports: [
    {
      id: "SCHED-001",
      name: "Monthly Consolidated P&L",
      reportType: "pl",
      schedule: {
        frequency: "monthly",
        dayOfMonth: 1,
        time: "10:00",
        timezone: "Africa/Lagos"
      },
      config: {
        period: "last_month",
        countries: [],
        currency: "USD",
        sections: ["executive_summary", "revenue_analysis", "expense_analysis", "profit_loss", "country_breakdown", "ai_insights", "charts"],
        format: "both",
        includeAIInsights: true,
        includeCharts: true
      },
      active: true,
      nextRun: "2026-03-01T10:00:00Z",
      lastRun: "2026-02-01T10:00:00Z",
      createdBy: {
        id: "usr-finance-001",
        name: "Sarah O'Connor",
        email: "cfo@gogidix.com",
        role: "VP Finance"
      },
      recipients: ["cfo@gogidix.com", "finance-director@gogidix.com"]
    },
    {
      id: "SCHED-002",
      name: "Global Cash Flow Statement",
      reportType: "cash_flow",
      schedule: {
        frequency: "weekly",
        dayOfWeek: 1,
        time: "09:00",
        timezone: "Africa/Lagos"
      },
      config: {
        period: "last_week",
        countries: [],
        currency: "USD",
        sections: ["executive_summary", "cash_flow", "charts"],
        format: "pdf",
        includeAIInsights: true,
        includeCharts: true
      },
      active: true,
      nextRun: "2026-02-24T09:00:00Z",
      lastRun: "2026-02-17T09:00:00Z",
      createdBy: {
        id: "usr-finance-003",
        name: "James Chen",
        email: "j.chen@gogidix.com",
        role: "Finance Operations Manager"
      },
      recipients: ["cfo@gogidix.com", "treasury@gogidix.com"]
    }
  ],
  totalCount: 4,
  page: 1,
  pageSize: 20
};
```

---

## 4. WEBSOCKET REAL-TIME FLOWS

### 4.1 WebSocket Connection Flow

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                    WEBSOCKET CONNECTION FLOW                              │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│  Client                                                                   │
│    │                                                                         │
│    │  1. After successful login, establish WebSocket connection            │
│    ▼                                                                         │
│  ┌─────────────────────────────────────────────────────────────────────┐   │
│  │  WS: wss://api.gogidix.com/v1/finance/ws                             │   │
│  │  Headers:                                                               │   │
│  │    Authorization: Bearer {token}                                      │   │
│  │    Tenant-Id: {tenantId}                                             │   │
│  └─────────────────────────────────────────────────────────────────────┘   │
│    │                                                                         │
│    │  2. Server authenticates and accepts connection                        │
│    ▼                                                                         │
│  ┌─────────────────────────────────────────────────────────────────────┐   │
│  │  Server → Client: {                                                   │   │
│  │    type: "connected",                                                │   │
│  │    connectionId: "conn_abc123",                                     │   │
│  │    timestamp: "2026-02-17T10:00:00Z"                               │   │
│  │  }                                                                  │   │
│  └─────────────────────────────────────────────────────────────────────┘   │
│    │                                                                         │
│    │  3. Client subscribes to channels                                      │
│    ▼                                                                         │
│  ┌─────────────────────────────────────────────────────────────────────┐   │
│  │  Client → Server: {                                                   │   │
│  │    type: "subscribe",                                                 │   │
│  │    channels: [                                                       │   │
│  │      "finance.updates",                                              │   │
│  │      "finance.budgets",                                             │   │
│  │      "finance.treasury",                                            │   │
│  │      "finance.alerts",                                              │   │
│  │      "finance.reports"                                              │   │
│  │    ]                                                                │   │
│  │  }                                                                  │   │
│  └─────────────────────────────────────────────────────────────────────┘   │
│    │                                                                         │
│    │  4. Server confirms subscriptions                                     │
│    ▼                                                                         │
│  ┌─────────────────────────────────────────────────────────────────────┐   │
│  │  Server → Client: {                                                   │   │
│  │    type: "subscribed",                                                │   │
│  │    channels: [                                                       │   │
│  │      "finance.updates",                                              │   │
│  │      "finance.budgets",                                             │   │
│  │      "finance.treasury",                                            │   │
│  │      "finance.alerts",                                              │   │
│  │      "finance.reports"                                              │   │
│  │    ]                                                                │   │
│  │  }                                                                  │   │
│  └─────────────────────────────────────────────────────────────────────┘   │
│                                                                              │
│  Connection established. Ready to receive real-time updates.                 │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

### 4.2 Real-Time Update Flows

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                    REAL-TIME UPDATE FLOWS                                 │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│  1. METRIC UPDATE                                                          │
│  ┌─────────────────────────────────────────────────────────────────────┐   │
│  │  Server → Client: {                                                   │   │
│  │    channel: "finance.updates",                                      │   │
│  │    type: "metric_update",                                           │   │
│  │    data: {                                                          │   │
│  │      metric: "revenue",                                            │   │
│  │      country: "NGA",                                                │   │
│  │      oldValue: 8100000,                                           │   │
│  │      newValue: 8200000,                                           │   │
│  │      change: 100000,                                               │   │
│  │      changePercent: 1.23,                                          │   │
│  │      timestamp: "2026-02-17T10:30:00Z"                           │   │
│  │    }                                                                │   │
│  │  }                                                                  │   │
│  └─────────────────────────────────────────────────────────────────────┘   │
│    │                                                                         │
│    │  Client updates UI:                                                    │
│    │  → Update KPI card value                                            │
│    │  → Update country card                                               │
│  │    → Update trend chart (append data point)                            │
│    │  → Show notification if significant change                            │
│    │                                                                         │
│  2. BUDGET APPROVAL REQUEST                                               │
│  ┌─────────────────────────────────────────────────────────────────────┐   │
│  │  Server → Client: {                                                   │   │
│  │    channel: "finance.budgets",                                      │   │
│  │    type: "approval_requested",                                       │   │
│  │    data: {                                                          │   │
│  │      requestId: "BUD-2026-0217-001",                               │   │
│  │      country: { name: "Nigeria", code: "NGA" },                     │   │
│  │      amount: 2100000,                                               │   │
│  │      priority: "high",                                              │   │
│  │      requestedBy: "Adebayo Okafor",                                 │   │
│  │      timestamp: "2026-02-17T10:30:00Z"                             │   │
│  │    }                                                                │   │
│  │  }                                                                  │   │
│  └─────────────────────────────────────────────────────────────────────┘   │
│    │                                                                         │
│    │  Client updates UI:                                                    │
│    │  → Update pending approval counter                                  │
│    │  → Add new alert to alerts panel                                    │
│    │  → Show toast notification                                           │
│    │  → Play notification sound (if enabled)                            │
│    │                                                                         │
│  3. FRAUD ALERT                                                            │
│  ┌─────────────────────────────────────────────────────────────────────┐   │
│  │  Server → Client: {                                                   │   │
│  │    channel: "finance.alerts",                                       │   │
│  │    type: "fraud_alert",                                             │   │
│  │    data: {                                                          │   │
│  │      alertId: "FRAUD-2026-0217-001",                                │   │
│  │      country: { name: "Kenya", code: "KEN" },                         │   │
│  │      riskScore: 87,                                                  │   │
│  │      confidence: 0.87,                                                │   │
│  │      severity: "high",                                               │   │
│  │      description: "Unusual expense pattern detected",                  │   │
│  │      timestamp: "2026-02-17T09:23:00Z"                             │   │
│  │    }                                                                │   │
│  │  }                                                                  │   │
│  └─────────────────────────────────────────────────────────────────────┘   │
│    │                                                                         │
│    │  Client updates UI:                                                    │
│    │  → Update alert counter                                             │
│    │  → Add high-priority alert to alerts panel                          │
│    │  → Show urgent toast notification                                   │
│    │  → Play alert sound (if enabled)                                   │
│    │                                                                         │
│  4. REPORT READY                                                           │
│  ┌─────────────────────────────────────────────────────────────────────┐   │
│  │  Server → Client: {                                                   │   │
│  │    channel: "finance.reports",                                      │   │
│  │    type: "report_ready",                                            │   │
│  │    data: {                                                          │   │
│  │      reportId: "RPT-2026-0217-001",                                 │   │
│  │      reportName: "Consolidated P&L February 2026",                   │   │
│  │      reportType: "pl",                                               │   │
│  │      format: "pdf",                                                   │   │
│  │      downloadUrl: "https://storage.gogidix.com/...",               │   │
│  │      timestamp: "2026-02-17T11:00:00Z"                             │   │
│  │    }                                                                │   │
│  │  }                                                                  │   │
│  └─────────────────────────────────────────────────────────────────────┘   │
│    │                                                                         │
│    │  Client updates UI:                                                    │
│    │  → Update scheduled report status                                    │
│    │  → Add notification to alerts panel                                 │
│  │    → Show toast notification                                           │
│    │                                                                         │
└─────────────────────────────────────────────────────────────────────────────┘
```

### 4.3 Client-Server Heartbeat

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                    HEARTBEAT / KEEP-ALIVE                                  │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│  Every 30 seconds:                                                          │
│                                                                              │
│  ┌─────────────────────────────────────────────────────────────────────┐   │
│  │  Client → Server: {                                                   │   │
│  │    type: "ping",                                                     │   │
│  │    timestamp: "2026-02-17T10:30:00Z"                               │   │
│  │  }                                                                  │   │
│  └─────────────────────────────────────────────────────────────────────┘   │
│    │                                                                         │
│    │  Server → Client: {                                                   │   │
│  │    type: "pong",                                                     │   │
│  │    timestamp: "2026-02-17T10:30:00Z",                              │   │
│  │    serverTime: "2026-02-17T10:30:01Z"                               │   │
│  │  }                                                                  │   │
│  │                                                                         │   │
│  If no pong received after 3 attempts → Reconnect with exponential backoff│
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## 5. ERROR RESPONSES

### 5.1 Error Response Format

```typescript
/**
 * Standard error response
 */
interface ErrorResponse {
  code: string;           // Unique error code (e.g., "FIN_HQ_001")
  message: string;         // Human-readable error message
  details?: string;        // Additional error details
  timestamp: string;       // ISO 8601 timestamp
  path: string;           // Request path that caused error
  requestId: string;     // Unique request ID for tracing
}

/**
 * Validation error response
 */
interface ValidationErrorResponse extends ErrorResponse {
  validationErrors?: ValidationError[];
}

/**
 * Single validation error
 */
interface ValidationError {
  field: string;
  message: string;
  rejectedValue: any;
}
```

### 5.2 Error Codes

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                         FINANCE ERROR CODES                                │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│  ┌─────────────────────────────────────────────────────────────────────┐ │
│  │ Code          │ Status │ Description                              │   │
│  ├─────────────────────────────────────────────────────────────────────┤ │
│  │ AUTH          │        │ Authentication Errors                        │   │
│  ├─────────────────────────────────────────────────────────────────────┤ │
│  │ FIN_HQ_AUTH_001│ 401   │ Invalid credentials                        │   │
│  │ FIN_HQ_AUTH_002│ 401   │ Account locked                            │   │
│  │ FIN_HQ_AUTH_003│ 401   │ Account disabled                         │   │
│  │ FIN_HQ_AUTH_004│ 401   │ Token expired                             │   │
│  │ FIN_HQ_AUTH_005│ 403   │ Invalid token                             │   │
│  │ FIN_HQ_AUTH_006│ 403   │ Insufficient permissions                   │   │
│  ├─────────────────────────────────────────────────────────────────────┤ │
│  │ SCOPE         │        │ Scope/Access Errors                       │   │
│  ├─────────────────────────────────────────────────────────────────────┤ │
│  │ FIN_HQ_001    │ 403   │ Country not in assigned scope             │   │
│  │ FIN_HQ_002    │ 403   │ Region not in assigned scope              │   │
│  │ FIN_HQ_003    │ 403   │ Global view access required               │   │
│  │ FIN_HQ_004    │ 403   │ Approval permission required               │   │
│  ├─────────────────────────────────────────────────────────────────────┤ │
│  │ VALIDATION    │        │ Validation Errors                          │   │
│  ├─────────────────────────────────────────────────────────────────────┤ │
│  │ FIN_HQ_VAL_001│ 400   │ Invalid date range                         │   │
│  │ FIN_HQ_VAL_002│ 400   │ Invalid currency code                       │   │
│  │ FIN_HQ_VAL_003│ 400   │ Invalid country code                       │   │
│  │ FIN_HQ_VAL_004│ 400   │ Invalid report type                        │   │
│  │ FIN_HQ_VAL_005│ 400   │ Invalid period format                      │   │
│  ├─────────────────────────────────────────────────────────────────────┤ │
│  │ RESOURCE      │        │ Resource Errors                           │   │
│  ├─────────────────────────────────────────────────────────────────────┤ │
│  │ FIN_HQ_RES_001│ 404   │ Country not found                         │   │
│  │ FIN_HQ_RES_002│ 404   │ Budget request not found                   │   │
│  │ FIN_HQ_RES_003│ 404   │ Report not found                          │   │
│  │ FIN_HQ_RES_004│ 404   │ Fraud alert not found                      │   │
│  ├─────────────────────────────────────────────────────────────────────┤ │
│  │ BUSINESS      │        │ Business Logic Errors                      │   │
│  ├─────────────────────────────────────────────────────────────────────┤ │
│  │ FIN_HQ_BUS_001│ 400   │ Budget already approved                     │   │
│  │ FIN_HQ_BUS_002│ 400   │ Budget expired for approval                │   │
│  │ FIN_HQ_BUS_003│ 400   │ Insufficient budget for transfer            │   │
│  │ FIN_HQ_BUS_004│ 400   │ Report generation already in progress     │   │
│  ├─────────────────────────────────────────────────────────────────────┤ │
│  │ SERVER        │        │ Server Errors                             │   │
│  ├─────────────────────────────────────────────────────────────────────┤ │
│  │ FIN_HQ_SRV_001│ 500   │ Data aggregation service unavailable        │   │
│  │ FIN_HQ_SRV_002│ 500   │ AI forecast service unavailable            │   │
│  │ FIN_HQ_SRV_003│ 500   │ Report generation service error            │   │
│  │ FIN_HQ_SRV_004│ 503   │ Service temporarily unavailable            │   │
│  │ FIN_HQ_SRV_005│ 504   │ Gateway timeout                           │   │
│  └─────────────────────────────────────────────────────────────────────┘   │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

### 5.3 Error Response Examples

```typescript
// 403 Forbidden - Country not in scope
const errorCountryNotInScope: ErrorResponse = {
  code: "FIN_HQ_001",
  message: "Country not in assigned scope",
  details: "Your account does not have access to financial data for South Africa (ZAF). Available countries: Nigeria (NGA), Kenya (KEN), Ghana (GHA), Ireland (IRL).",
  timestamp: "2026-02-17T10:30:00Z",
  path: "/api/v1/finance/countries/ZAF",
  requestId: "req_abc123"
};

// 404 Not Found - Budget request not found
const errorBudgetNotFound: ErrorResponse = {
  code: "FIN_HQ_RES_002",
  message: "Budget request not found",
  details: "The budget request BUD-2026-9999-999 does not exist or has been removed.",
  timestamp: "2026-02-17T10:30:00Z",
  path: "/api/v1/finance/budgets/BUD-2026-9999-999",
  requestId: "req_def456"
};

// 500 Server Error - Aggregation service down
const errorAggregationDown: ErrorResponse = {
  code: "FIN_HQ_SRV_001",
  message: "Unable to aggregate financial data at this time",
  details: "The data aggregation service is currently unavailable. Please try again later. If the problem persists, contact IT support.",
  timestamp: "2026-02-17T10:30:00Z",
  path: "/api/v1/finance/dashboard/global",
  requestId: "req_ghi789"
};

// 400 Validation Error - Invalid date range
const errorInvalidDateRange: ValidationErrorResponse = {
  code: "FIN_HQ_VAL_001",
  message: "Invalid date range",
  details: "Period start date must be before end date. Date range cannot exceed 12 months.",
  timestamp: "2026-02-17T10:30:00Z",
  path: "/api/v1/finance/revenue/consolidated",
  requestId: "req_jkl012",
  validationErrors: [
    {
      field: "periodStart",
      message: "Start date must be within the last 12 months",
      rejectedValue: "2020-01-01T00:00:00Z"
    },
    {
      field: "periodEnd",
      message: "End date cannot be in the future",
      rejectedValue: "2027-12-31T23:59:59Z"
    }
  ]
};
```

---

## 6. STATE MANAGEMENT

### 6.1 Zustand Store Structure

```typescript
/**
 * Root store state
 */
interface FinanceRootState {
  // Authentication & User
  auth: AuthState;

  // Dashboard data
  dashboard: DashboardState;

  // Countries data
  countries: CountriesState;

  // Budget data
  budgets: BudgetsState;

  // Treasury data
  treasury: TreasuryState;

  // Reports data
  reports: ReportsState;

  // AI Insights
  insights: InsightsState;

  // UI State
  ui: UIState;
}

/**
 * Authentication state
 */
interface AuthState {
  user: FinanceUser | null;
  financeScope: FinanceScope | null;
  token: string | null;
  isAuthenticated: boolean;
  isLoading: boolean;
  error: string | null;
  lastLoginAttempt: number;
}

/**
 * Dashboard state
 */
interface DashboardState {
  globalMetrics: GlobalFinancialMetrics | null;
  financialHealthScore: number;
  countriesSummary: CountryFinanceSummary[];
  topPerformers: FinanceTopPerformer[];
  alerts: DashboardAlert[];
  pendingApprovals: PendingApprovalCount;
  isLoading: boolean;
  error: string | null;
  lastUpdated: string | null;
}

/**
 * Countries state
 */
interface CountriesState {
  selectedCountry: string | null;
  countryDetails: Record<string, CountryDetailResponse>;
  comparisonData: CountryComparisonData | null;
  isLoading: boolean;
  error: string | null;
}

/**
 * Budgets state
 */
interface BudgetsState {
  summary: BudgetSummaryResponse | null;
  pendingApprovals: BudgetApprovalRequest[];
  budgetVsActual: BudgetVsActualData[] | null;
  isLoading: boolean;
  isApproving: boolean;
  error: string | null;
}

/**
 * Treasury state
 */
interface TreasuryState {
  position: TreasuryPositionResponse | null;
  forecast: CashFlowForecastResponse | null;
  fxExposure: FXExposureResponse | null;
  isLoading: boolean;
  error: string | null;
}

/**
 * Reports state
 */
interface ReportsState {
  reportsList: ReportInfo[];
  scheduledReports: ScheduledReportInfo[];
  generatingReports: Record<string, ReportGenerationStatus>;
  isLoading: boolean;
  isGenerating: boolean;
  error: string | null;
}

/**
 * AI Insights state
 */
interface InsightsState {
  insights: AIInsight[];
  fraudAlerts: FraudAlert[];
  forecasts: Record<string, RevenueForecast>;
  isLoading: boolean;
  error: string | null;
}

/**
 * UI state
 */
interface UIState {
  sidebarOpen: boolean;
  selectedPeriod: DateRange;
  selectedCurrency: string;
  selectedCountries: string[];
  filters: Record<string, any>;
  notifications: Notification[];
  modals: ModalState;
}

/**
 * Notification
 */
interface Notification {
  id: string;
  type: 'success' | 'error' | 'warning' | 'info';
  title: string;
  message: string;
  duration?: number;
  actionUrl?: string;
  read: boolean;
  timestamp: string;
}

/**
 * Modal state
 */
interface ModalState {
  budgetApproval: {
    open: boolean;
    requestId: string | null;
  };
  fraudAlert: {
    open: boolean;
    alertId: string | null;
  };
  reportBuilder: {
    open: boolean;
    step: number;
    config: Partial<GenerateReportRequest>;
  };
  countryDetail: {
    open: boolean;
    countryCode: string | null;
  };
}
```

### 6.2 Store Actions

```typescript
/**
 * Auth actions
 */
interface AuthActions {
  login(credentials: LoginRequest): Promise<LoginResponse>;
  logout(): Promise<void>;
  refreshToken(): Promise<void>;
  me(): Promise<MeResponse>;
  updateUserPreferences(preferences: Partial<UserPreferences>): Promise<void>;
}

/**
 * Dashboard actions
 */
interface DashboardActions {
  loadGlobalDashboard(period?: DateRange): Promise<void>;
  refreshMetrics(): Promise<void>;
  dismissAlert(alertId: string): void;
}

/**
 * Budget actions
 */
interface BudgetActions {
  loadBudgetSummary(period: string): Promise<void>;
  loadPendingApprovals(): Promise<void>;
  approveBudget(requestId: string, comments?: string): Promise<void>;
  rejectBudget(requestId: string, reason: string): Promise<void>;
}

/**
 * Treasury actions
 */
interface TreasuryActions {
  loadPosition(): Promise<void>;
  loadForecast(days?: number): Promise<void>;
  loadFXExposure(): Promise<void>;
  executeTransfer(from: string, to: string, amount: number): Promise<void>;
}

/**
 * Reports actions
 */
interface ReportActions {
  loadReports(filters?: ReportFilters): Promise<void>;
  generateReport(config: GenerateReportRequest): Promise<string>;
  downloadReport(reportId: string, format: string): void;
  scheduleReport(config: ScheduledReportConfig): Promise<void>;
}

/**
 * AI Insights actions
 */
interface InsightActions {
  loadInsights(category?: string): Promise<void>;
  loadFraudAlerts(): Promise<void>;
  assignFraudAlert(alertId: string, assignee: string): Promise<void>;
  dismissFraudAlert(alertId: string): Promise<void>;
}
```

---

**VERSION HISTORY**

| Version | Date | Changes |
|---------|------|---------|
| 1.0 | 2025-02-08 | Initial skeleton documentation |
| 2.0 | 2026-02-17 | Complete Mock Flow Documentation following Executive Dashboard standard |

---

**End of Mock Flow Documentation**

Next: [04_Page_By_Page_Flow_Documentation.md](#) - Route-level documentation
