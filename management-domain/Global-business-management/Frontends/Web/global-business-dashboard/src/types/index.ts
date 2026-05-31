// Business Metrics Types
export interface KPIMetric {
  id: string;
  name: string;
  value: number;
  previousValue: number;
  change: number;
  changeType: 'increase' | 'decrease' | 'neutral';
  unit: string;
  trend: number[];
  target?: number;
  category: KPICategory;
  lastUpdated: string;
}

export type KPICategory =
  | 'revenue'
  | 'profit'
  | 'growth'
  | 'customer'
  | 'operational'
  | 'financial'
  | 'compliance'
  | 'satisfaction';

// Regional Data Types
export interface Region {
  id: string;
  name: string;
  code: string;
  countries: Country[];
  manager: string;
  headquarters: string;
  timezone: string;
}

export interface Country {
  id: string;
  name: string;
  code: string;
  regionId: string;
  currency: string;
  language: string;
  timezone: string;
  flag: string;
  population: number;
  gdp: number;
}

export interface RegionalMetrics {
  regionId: string;
  regionName: string;
  revenue: number;
  revenueShare: number;
  profit: number;
  growth: number;
  customers: number;
  satisfaction: number;
  marketPenetration: number;
  performance: PerformanceRating;
  trend: TrendData[];
}

export type PerformanceRating = 'excellent' | 'good' | 'average' | 'below-average' | 'poor';

export interface TrendData {
  period: string;
  value: number;
  target?: number;
}

// Financial Data Types
export interface FinancialReport {
  id: string;
  period: string;
  type: ReportType;
  status: ReportStatus;
  revenue: FinancialBreakdown;
  expenses: FinancialBreakdown;
  profit: number;
  profitMargin: number;
  metrics: FinancialMetrics;
  generatedAt: string;
  createdBy: string;
}

export type ReportType = 'quarterly' | 'annual' | 'monthly' | 'custom';
export type ReportStatus = 'draft' | 'pending' | 'approved' | 'published';

export interface FinancialBreakdown {
  total: number;
  byCategory: CategoryBreakdown[];
  byRegion: RegionalBreakdown[];
}

export interface CategoryBreakdown {
  category: string;
  amount: number;
  percentage: number;
  change: number;
}

export interface RegionalBreakdown {
  region: string;
  amount: number;
  percentage: number;
  change: number;
  currency: string;
  convertedAmount: number;
}

export interface FinancialMetrics {
  ebitda: number;
  ebitdaMargin: number;
  operatingCashFlow: number;
  freeCashFlow: number;
  debtToEquity: number;
  currentRatio: number;
  quickRatio: number;
  returnOnAssets: number;
  returnOnEquity: number;
}

// Customer Data Types
export interface CustomerMetrics {
  total: number;
  new: number;
  churned: number;
  retained: number;
  churnRate: number;
  retentionRate: number;
  acquisitionCost: number;
  lifetimeValue: number;
  segments: CustomerSegment[];
}

export interface CustomerSegment {
  segment: string;
  count: number;
  revenue: number;
  growth: number;
  satisfaction: number;
}

// Operational Metrics
export interface OperationalMetrics {
  efficiency: number;
  productivity: number;
  utilization: number;
  throughput: number;
  quality: number;
  onTimeDelivery: number;
  inventoryTurnover: number;
  orderFulfillmentTime: number;
}

// Compliance & Risk
export interface ComplianceMetrics {
  overallScore: number;
  regulations: RegulationStatus[];
  audits: Audit[];
  incidents: Incident[];
  trainingsCompleted: number;
  trainingsPending: number;
}

export interface RegulationStatus {
  id: string;
  name: string;
  category: string;
  status: 'compliant' | 'non-compliant' | 'pending' | 'exempt';
  lastReview: string;
  nextReview: string;
  severity: 'high' | 'medium' | 'low';
}

export interface Audit {
  id: string;
  type: string;
  status: 'scheduled' | 'in-progress' | 'completed';
  date: string;
  auditor: string;
  findings: number;
  critical: number;
}

export interface Incident {
  id: string;
  type: string;
  severity: 'critical' | 'high' | 'medium' | 'low';
  status: 'open' | 'investigating' | 'resolved' | 'closed';
  reportedAt: string;
  resolvedAt?: string;
  description: string;
}

// Chart Data Types
export interface ChartData {
  name: string;
  value: number;
  [key: string]: string | number | undefined;
}

export interface TimeSeriesData {
  period: string;
  [key: string]: string | number | undefined;
}

export interface ComparisonData {
  category: string;
  current: number;
  previous: number;
  target?: number;
}

// Export Types
export type ExportFormat = 'csv' | 'excel' | 'pdf' | 'json';
export type ExportScope = 'current' | 'period' | 'all';

export interface ExportOptions {
  format: ExportFormat;
  scope: ExportScope;
  includeCharts: boolean;
  includeRawData: boolean;
  dateRange?: DateRange;
}

export interface DateRange {
  start: string;
  end: string;
}

// API Response Types
export interface ApiResponse<T> {
  success: boolean;
  data: T;
  message?: string;
  error?: string;
  pagination?: PaginationInfo;
}

export interface PaginationInfo {
  page: number;
  pageSize: number;
  total: number;
  totalPages: number;
  hasNext: boolean;
  hasPrevious: boolean;
}

// Filter & Sort Types
export interface FilterOptions {
  search?: string;
  regions?: string[];
  countries?: string[];
  dateRange?: DateRange;
  categories?: KPICategory[];
  status?: ReportStatus[];
}

export interface SortOptions {
  field: string;
  direction: 'asc' | 'desc';
}

// User & Auth Types
export interface User {
  id: string;
  name: string;
  email: string;
  role: UserRole;
  region?: string;
  country?: string;
  permissions: Permission[];
  avatar?: string;
}

export type UserRole =
  | 'global-admin'
  | 'regional-admin'
  | 'country-admin'
  | 'regional-manager'
  | 'country-manager'
  | 'analyst'
  | 'viewer';

export type Permission =
  | 'view-all'
  | 'view-regional'
  | 'view-country'
  | 'edit-all'
  | 'edit-regional'
  | 'edit-country'
  | 'export-data'
  | 'generate-reports'
  | 'manage-users'
  | 'approve-reports';

// Notification Types
export interface Notification {
  id: string;
  type: NotificationType;
  title: string;
  message: string;
  severity: 'info' | 'success' | 'warning' | 'error';
  read: boolean;
  createdAt: string;
  actionUrl?: string;
}

export type NotificationType =
  | 'report-ready'
  | 'deadline-reminder'
  | 'compliance-alert'
  | 'system-update'
  | 'mention'
  | 'approval-required';

// Settings Types
export interface DashboardSettings {
  theme: 'light' | 'dark' | 'system';
  language: string;
  currency: string;
  defaultRegion?: string;
  defaultCountry?: string;
  notifications: NotificationSettings;
  display: DisplaySettings;
}

export interface NotificationSettings {
  email: boolean;
  push: boolean;
  reportReady: boolean;
  deadlineReminder: boolean;
  complianceAlert: boolean;
}

export interface DisplaySettings {
  compactMode: boolean;
  showTrends: boolean;
  showTargets: boolean;
  chartAnimations: boolean;
  itemsPerPage: number;
}

// Currency Types
export interface CurrencyRate {
  from: string;
  to: string;
  rate: number;
  lastUpdated: string;
}

// Form Types
export interface ReportFormData {
  name: string;
  description: string;
  type: ReportType;
  period: string;
  regions: string[];
  includeCharts: boolean;
  includeRawData: boolean;
  format: ExportFormat;
}

export interface UserFormData {
  name: string;
  email: string;
  role: UserRole;
  region?: string;
  country?: string;
  permissions: Permission[];
}
