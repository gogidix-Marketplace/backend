// Domain Types for HQ Sales Dashboard
// All TypeScript interfaces from Mock Flow Documentation

// User & Authentication Types
export type HQSalesRole =
  | 'VP_SALES'
  | 'GLOBAL_SALES_DIRECTOR'
  | 'REGIONAL_SALES_MANAGER'
  | 'HQ_SALES_ANALYST'
  | 'SALES_OPERATIONS_MANAGER';

export type RegionScope =
  | 'GLOBAL'
  | 'WEST_AFRICA'
  | 'EAST_AFRICA'
  | 'SOUTHERN_AFRICA'
  | 'NORTH_AFRICA';

export interface Permission {
  id: string;
  name: string;
  resource: string;
  action: 'create' | 'read' | 'update' | 'delete' | 'approve';
  scope?: RegionScope;
}

export interface HQSalesUser {
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

export interface LoginRequest {
  email: string;
  password: string;
  deviceId?: string;
  deviceName?: string;
}

export interface AuthResponse {
  user: HQSalesUser;
  accessToken: string;
  refreshToken: string;
  expiresIn: number;
}

// Country & Region Types
export interface CountryInfo {
  code: string;
  name: string;
  flag: string;
  currency: string;
  region: string;
  directorId: string;
  directorName: string;
}

export interface Address {
  street: string;
  city: string;
  state: string;
  postalCode: string;
  country: string;
}

// Global Dashboard Types
export interface PeriodInfo {
  start: Date;
  end: Date;
  type: 'daily' | 'weekly' | 'monthly' | 'quarterly' | 'yearly';
}

export interface GlobalMetric {
  value: number;
  label: string;
  change: number;
  changeType: 'increase' | 'decrease' | 'neutral';
  target?: number;
  attainment?: number;
  currency?: string;
}

export interface CountrySummary {
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

export interface GlobalTopPerformer {
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

export interface MajorDeal {
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
  isMajor: boolean;
}

export interface GlobalAlert {
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

export interface ForecastSummary {
  period: string;
  forecast: number;
  bestCase: number;
  worstCase: number;
  weightedPipeline: number;
  currency: string;
  accuracy?: number;
}

export interface GlobalDashboardSummary {
  period: PeriodInfo;
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

// Country Comparison Types
export interface ComparisonValue {
  value: number;
  display: string;
  rank?: number;
  vsTarget?: number;
  vsPrevious?: number;
}

export interface ComparisonMetric {
  key: string;
  label: string;
  format: 'currency' | 'percentage' | 'number' | 'text';
  values: Record<string, ComparisonValue>;
  trend?: Record<string, 'up' | 'down' | 'neutral'>;
}

export interface CountryRanking {
  rank: number;
  country: CountryInfo;
  value: number;
  display: string;
  change?: number;
}

export interface ComparisonRanking {
  category: string;
  rankings: CountryRanking[];
}

export interface CountryComparison {
  countries: CountryInfo[];
  metrics: ComparisonMetric[];
  rankings: ComparisonRanking[];
  period: PeriodInfo;
}

// Pipeline Types
export interface PipelineStageSummary {
  stage: string;
  count: number;
  totalValue: number;
  weightedValue: number;
  percentage: number;
  avgDaysInStage: number;
  conversionRate?: number;
}

export interface CountryPipelineSummary {
  country: CountryInfo;
  totalValue: number;
  dealCount: number;
  weightedValue: number;
  avgDealSize: number;
  stageDistribution: Record<string, number>;
  trend: 'up' | 'down' | 'neutral';
}

export interface PipelineVelocity {
  avgDaysInStage: Record<string, number>;
  overallCycle: number;
  cycleTarget: number;
  cycleVariance: number;
}

export interface GlobalPipelineSummary {
  totalValue: number;
  totalDeals: number;
  weightedValue: number;
  avgDealSize: number;
  byCountry: CountryPipelineSummary[];
  byStage: PipelineStageSummary[];
  majorDeals: MajorDeal[];
  velocity: PipelineVelocity;
}

// Performance Types
export interface PerformanceSummary {
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

export interface ProductCountryMetric {
  revenue: number;
  growth: number;
  attainment: number;
}

export interface ProductPerformance {
  productCode: string;
  productName: string;
  category: string;
  revenue: number;
  percentage: number;
  growth: number;
  targetAttainment: number;
  byCountry: Record<string, ProductCountryMetric>;
}

export interface PerformanceTrend {
  period: string;
  revenue: number;
  target: number;
  attainment: number;
  dealsClosed: number;
  winRate: number;
}

export interface GlobalPerformanceData {
  period: PeriodInfo;
  summary: PerformanceSummary;
  countryRankings: CountryRanking[];
  topPerformers: GlobalTopPerformer[];
  productPerformance: ProductPerformance[];
  trendAnalysis: PerformanceTrend[];
}

// Forecasting Types
export type ForecastScenario = 'likely' | 'best' | 'worst' | 'custom';

export interface ForecastPeriod {
  type: 'monthly' | 'quarterly' | 'yearly';
  start: Date;
  end: Date;
  label: string;
}

export interface CountryForecast {
  country: CountryInfo;
  forecast: number;
  bestCase: number;
  worstCase: number;
  pipeline: number;
  contribution: number;
  trend: 'up' | 'down' | 'neutral';
  riskLevel: 'low' | 'medium' | 'high';
}

export interface StageForecast {
  stage: string;
  count: number;
  value: number;
  weightedValue: number;
  probability: number;
  contribution: number;
}

export interface ForecastAccuracy {
  period: string;
  accuracy: number;
  variance: number;
  variancePercentage: number;
  actualRevenue: number;
  forecastedRevenue: number;
}

export interface ForecastTrend {
  period: string;
  actual?: number;
  forecast: number;
  bestCase?: number;
  worstCase?: number;
  scenario: ForecastScenario;
}

export interface GlobalSalesForecast {
  id: string;
  name: string;
  period: ForecastPeriod;
  scenario: ForecastScenario;
  generatedAt: Date;
  generatedBy: string;
  global: {
    forecast: number;
    bestCase: number;
    worstCase: number;
    currency: string;
    confidence: number;
  };
  byCountry: CountryForecast[];
  byStage: StageForecast[];
  accuracyMetrics: ForecastAccuracy[];
  trends: ForecastTrend[];
}

// Reports Types
export type ReportType =
  | 'EXECUTIVE_DASHBOARD'
  | 'COUNTRY_COMPARISON'
  | 'PIPELINE_ANALYSIS'
  | 'PERFORMANCE_REPORT'
  | 'FORECAST_REPORT'
  | 'PRODUCT_REPORT'
  | 'TEAM_REPORT'
  | 'CUSTOM';

export type ReportCategory =
  | 'EXECUTIVE'
  | 'COUNTRY'
  | 'PIPELINE'
  | 'PERFORMANCE'
  | 'FORECAST'
  | 'PRODUCT'
  | 'TEAM';

export interface ReportVisualization {
  type: 'line' | 'bar' | 'pie' | 'table' | 'number';
  title: string;
  dataSource: string;
  config: any;
}

export interface ReportConfig {
  metrics: string[];
  countries?: string[];
  period: PeriodInfo;
  filters?: Record<string, any>;
  visualizations: ReportVisualization[];
  exportFormat: 'PDF' | 'EXCEL' | 'CSV';
}

export interface ReportSchedule {
  frequency: 'daily' | 'weekly' | 'monthly' | 'quarterly';
  dayOfWeek?: number;
  dayOfMonth?: number;
  time: string;
  recipients: string[];
  active: boolean;
  nextRunAt?: Date;
}

export interface Report {
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

export interface GeneratedReport {
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

// Sales Team Partners Types
export type PartnerType =
  | 'COURIER'
  | 'HAULAGE'
  | 'WAREHOUSE'
  | 'ECOMMERCE'
  | 'AIR_OCEAN'
  | 'LOCATION_AGENT'
  | 'WHOLESALE'
  | 'INFLUENCER';

export type PartnerStatus =
  | 'PENDING'
  | 'ACTIVE'
  | 'SUSPENDED'
  | 'TERMINATED';

export type PartnerTier =
  | 'BRONZE'
  | 'SILVER'
  | 'GOLD'
  | 'PLATINUM';

export type ApplicationStatus =
  | 'PENDING'
  | 'UNDER_REVIEW'
  | 'APPROVED'
  | 'REJECTED'
  | 'ON_HOLD';

export interface Territory {
  id: string;
  name: string;
  code: string;
  country: string;
  region?: string;
}

export interface TierProgression {
  currentTier: PartnerTier;
  currentTierRevenue: number;
  nextTier?: PartnerTier;
  nextTierRevenue?: number;
  revenueToNextTier: number;
  estimatedTimeToNextTier?: number;
}

export interface PartnerLeadAssignment {
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

export interface SalesTeamPartner {
  id: string;
  partnerNumber: string;
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
  partnersRecruited: number;
  customersAcquired: number;
  pipelineValue: number;
  revenueGenerated: number;
  commissionEarned: number;
  referralBonusEarned: number;
  tierProgression: TierProgression;
  leadConversionRate: number;
  avgDealSize: number;
  activeDeals: number;
  assignedLeads: PartnerLeadAssignment[];
  applicationId?: string;
  approvedAt?: Date;
  approvedBy?: string;
  createdAt: Date;
  updatedAt: Date;
  lastActivityAt?: Date;
}

export interface ApplicationDocument {
  id: string;
  type: 'ID_DOCUMENT' | 'PROOF_OF_ADDRESS' | 'BUSINESS_REG' | 'TAX_CERT' | 'OTHER';
  name: string;
  url: string;
  uploadedAt: Date;
  verified: boolean;
  verifiedAt?: Date;
}

export interface PartnerApplication {
  id: string;
  applicationNumber: string;
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
  documents: ApplicationDocument[];
  backgroundCheck: {
    status: 'PENDING' | 'CLEARED' | 'FLAGGED';
    completedAt?: Date;
    notes?: string;
  };
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

export interface ReferralBonusBreakdown {
  referredPartnerId: string;
  referredPartnerName: string;
  referredPartnerType: PartnerType;
  bonusAmount: number;
  paidAt?: Date;
}

export interface SalesCommissionBreakdown {
  customerId: string;
  customerName: string;
  dealValue: number;
  commissionRate: number;
  commissionAmount: number;
  dealClosedAt: Date;
  paidAt?: Date;
}

export interface CommissionPeriod {
  type: 'MONTHLY' | 'QUARTERLY' | 'ANNUAL';
  start: Date;
  end: Date;
  label: string;
}

export interface PartnerCommission {
  id: string;
  partnerId: string;
  partner: {
    name: string;
    partnerType: PartnerType;
    tier: PartnerTier;
  };
  referralBonus: {
    partnersReferred: number;
    totalBonus: number;
    paidBonus: number;
    pendingBonus: number;
    breakdown: ReferralBonusBreakdown[];
  };
  salesCommission: {
    customersAcquired: number;
    totalRevenue: number;
    totalCommission: number;
    paidCommission: number;
    pendingCommission: number;
    breakdown: SalesCommissionBreakdown[];
  };
  tierBonus?: {
    achievedTier: PartnerTier;
    bonusAmount: number;
    paidAt?: Date;
  };
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

export interface PartnerTerritoryAssignment {
  id: string;
  partnerId: string;
  partner: {
    name: string;
    partnerType: PartnerType;
    tier: PartnerTier;
  };
  assignedCountry: CountryInfo;
  assignedTerritories: string[];
  exclusive: boolean;
  capacity: {
    maxPartners: number;
    maxCustomers: number;
    currentPartners: number;
    currentCustomers: number;
    availableCapacity: boolean;
  };
  leadAllocation: {
    enabled: boolean;
    maxDailyLeads: number;
    currentDailyLeads: number;
    aiMatchScore: number;
  };
  assignedAt: Date;
  assignedBy: string;
  active: boolean;
}

export interface CountryPartnersSummary {
  country: CountryInfo;
  totalPartners: number;
  activePartners: number;
  pipelineValue: number;
  commissionPaid: number;
  tierBreakdown: Record<PartnerTier, number>;
}

export interface PartnerTypeSummary {
  partnerType: PartnerType;
  count: number;
  pipelineValue: number;
  revenueGenerated: number;
  avgCommission: number;
}

export interface PartnerTierSummary {
  tier: PartnerTier;
  count: number;
  revenueThreshold: number;
  partnersAtTier: number;
}

export interface TopPartnerPerformer {
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

export interface CommissionStats {
  totalEarned: number;
  totalPaid: number;
  totalPending: number;
  avgPayoutTime: number;
}

export interface GlobalPartnersSummary {
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

// Customer Types
export interface CustomerSegment {
  id: string;
  name: string;
  description: string;
  criteria: Record<string, any>;
}

export interface GlobalCustomer {
  id: string;
  name: string;
  logo?: string;
  industry: string;
  segment: CustomerSegment;
  countries: CountryInfo[];
  totalRevenue: number;
  dealsCount: number;
  avgDealSize: number;
  status: 'active' | 'at_risk' | 'churned';
  accountOwner: {
    id: string;
    name: string;
    country: string;
  };
  createdAt: Date;
  lastDealAt?: Date;
}

export interface CustomerAnalytics {
  totalCustomers: number;
  activeCustomers: number;
  atRiskCustomers: number;
  churnedCustomers: number;
  totalRevenue: number;
  avgRevenuePerCustomer: number;
  bySegment: Record<string, number>;
  byCountry: Record<string, number>;
  retentionRate: number;
  acquisitionRate: number;
}

// Territory Types
export interface TerritoryQuota {
  territoryId: string;
  territoryName: string;
  quota: number;
  attainment: number;
  assignedTo: {
    id: string;
    name: string;
    type: 'individual' | 'team';
  }[];
}

export interface TerritoryPerformance {
  territory: Territory;
  quota: TerritoryQuota;
  actualRevenue: number;
  attainment: number;
  pipelineValue: number;
  dealsClosed: number;
  teamSize: number;
  trend: 'up' | 'down' | 'neutral';
}

// Pagination Types
export interface PaginationInfo {
  page: number;
  pageSize: number;
  totalItems: number;
  totalPages: number;
  hasNext: boolean;
  hasPrevious: boolean;
}

export interface ApiResponse<T> {
  success: boolean;
  data: T;
  message?: string;
  errors?: ValidationError[];
  pagination?: PaginationInfo;
  timestamp: string;
  requestId: string;
}

export interface ValidationError {
  field: string;
  message: string;
  code: string;
}

export interface ErrorResponse {
  success: false;
  error: {
    code: string;
    message: string;
    details?: any;
    timestamp: string;
    requestId: string;
  };
}

// Partner Filters
export interface PartnerFilters {
  page?: number;
  pageSize?: number;
  sortBy?: string;
  sortOrder?: 'asc' | 'desc';
  search?: string;
  country?: string[];
  partnerType?: PartnerType[];
  status?: PartnerStatus[];
  tier?: PartnerTier[];
}

// Territory
export interface Territory {
  id: string;
  name: string;
  code: string;
  country: string;
  region?: string;
}
