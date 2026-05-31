// Domain Types for Country Sales Dashboard
// All TypeScript interfaces for country-level sales operations

// ============================================================
// USER & AUTHENTICATION TYPES
// ============================================================

export type CountrySalesRole =
  | 'COUNTRY_MANAGER'
  | 'REGIONAL_MANAGER'
  | 'TEAM_LEAD'
  | 'SALES_REPRESENTATIVE'
  | 'SALES_ANALYST'
  | 'PARTNER_COORDINATOR';

export type PermissionAction = 'create' | 'read' | 'update' | 'delete' | 'approve' | 'assign';

export interface Permission {
  id: string;
  name: string;
  resource: string;
  action: PermissionAction;
  scope?: string;
}

export interface CountrySalesUser {
  id: string;
  email: string;
  firstName: string;
  lastName: string;
  avatar?: string;
  role: CountrySalesRole;
  country: CountryInfo;
  assignedRegion?: string;
  assignedTerritory?: string;
  permissions: Permission[];
  managerId?: string;
  timezone: string;
  createdAt: Date;
  lastLoginAt: Date;
  isActive: boolean;
}

export interface LoginRequest {
  email: string;
  password: string;
  deviceId?: string;
  deviceName?: string;
}

export interface AuthResponse {
  user: CountrySalesUser;
  accessToken: string;
  refreshToken: string;
  expiresIn: number;
}

// ============================================================
// COUNTRY & LOCATION TYPES
// ============================================================

export interface CountryInfo {
  code: string;
  name: string;
  flag: string;
  currency: string;
  region: string;
  callingCode: string;
}

export interface Region {
  id: string;
  name: string;
  code: string;
  country: string;
  managerId?: string;
  managerName?: string;
  territories: Territory[];
}

export interface Territory {
  id: string;
  name: string;
  code: string;
  country: string;
  region?: string;
  assignedTo?: string[];
  quota?: number;
}

export interface Address {
  street: string;
  city: string;
  state: string;
  postalCode: string;
  country: string;
}

// ============================================================
// SALES TEAM TYPES
// ============================================================

export type TeamStatus = 'active' | 'inactive' | 'pending';

export interface SalesTeam {
  id: string;
  name: string;
  code: string;
  description?: string;
  country: CountryInfo;
  region?: Region;
  teamLeadId: string;
  teamLeadName: string;
  members: TeamMember[];
  status: TeamStatus;
  quota: {
    monthly: number;
    quarterly: number;
    annual: number;
  };
  metrics: {
    revenueThisMonth: number;
    revenueThisQuarter: number;
    quotaAttainment: number;
    dealsClosed: number;
    pipelineValue: number;
    winRate: number;
  };
  createdAt: Date;
  updatedAt: Date;
}

export interface TeamMember {
  userId: string;
  name: string;
  email: string;
  role: CountrySalesRole;
  avatar?: string;
  status: 'active' | 'inactive' | 'on_leave';
  individualQuota: number;
  revenueGenerated: number;
  attainment: number;
  joinedAt: Date;
}

export interface TeamPerformance {
  teamId: string;
  teamName: string;
  period: PeriodInfo;
  metrics: {
    revenue: number;
    quota: number;
    attainment: number;
    dealsClosed: number;
    avgDealSize: number;
    pipelineValue: number;
    activities: {
      calls: number;
      emails: number;
      meetings: number;
      demos: number;
    };
  };
  members: MemberPerformance[];
  ranking?: number;
  trend: 'up' | 'down' | 'neutral';
}

export interface MemberPerformance {
  userId: string;
  name: string;
  avatar?: string;
  role: CountrySalesRole;
  metrics: {
    revenue: number;
    quota: number;
    attainment: number;
    dealsClosed: number;
    activities: {
      calls: number;
      emails: number;
      meetings: number;
    };
  };
  ranking?: number;
}

// ============================================================
// PARTNER TYPES
// ============================================================

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

export interface CountryPartner {
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
  assignedTerritory?: Territory;
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
  approvedAt?: Date;
  approvedBy?: string;
  createdAt: Date;
  updatedAt: Date;
  lastActivityAt?: Date;
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

export interface ApplicationDocument {
  id: string;
  type: 'ID_DOCUMENT' | 'PROOF_OF_ADDRESS' | 'BUSINESS_REG' | 'TAX_CERT' | 'OTHER';
  name: string;
  url: string;
  uploadedAt: Date;
  verified: boolean;
  verifiedAt?: Date;
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
  };
  salesCommission: {
    customersAcquired: number;
    totalRevenue: number;
    totalCommission: number;
    paidCommission: number;
    pendingCommission: number;
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

export interface CommissionPeriod {
  type: 'MONTHLY' | 'QUARTERLY' | 'ANNUAL';
  start: Date;
  end: Date;
  label: string;
}

// ============================================================
// PIPELINE & DEAL TYPES
// ============================================================

export type DealStage =
  | 'PROSPECTING'
  | 'QUALIFICATION'
  | 'PROPOSAL'
  | 'NEGOTIATION'
  | 'CLOSING'
  | 'WON'
  | 'LOST';

export type DealPriority = 'low' | 'medium' | 'high' | 'urgent';

export interface Deal {
  id: string;
  dealNumber: string;
  name: string;
  accountId: string;
  accountName: string;
  contactId?: string;
  contactName?: string;
  value: number;
  currency: string;
  stage: DealStage;
  probability: number;
  expectedCloseDate: Date;
  actualCloseDate?: Date;
  owner: {
    id: string;
    name: string;
    team?: string;
  };
  priority: DealPriority;
  source: string;
  campaign?: string;
  products: DealProduct[];
  competitors?: string[];
  nextStep?: string;
  lastActivityAt?: Date;
  createdAt: Date;
  updatedAt: Date;
  lostReason?: string;
  tags?: string[];
}

export interface DealProduct {
  id: string;
  name: string;
  code: string;
  quantity: number;
  unitPrice: number;
  discount: number;
  totalPrice: number;
}

export interface CountryPipeline {
  totalValue: number;
  totalDeals: number;
  weightedValue: number;
  avgDealSize: number;
  byStage: PipelineStageSummary[];
  byOwner: PipelineOwnerSummary[];
  byTeam: PipelineTeamSummary[];
  velocity: PipelineVelocity;
  trending: {
    up: number;
    down: number;
    neutral: number;
  };
}

export interface PipelineStageSummary {
  stage: DealStage;
  count: number;
  totalValue: number;
  weightedValue: number;
  percentage: number;
  avgDaysInStage: number;
  conversionRate?: number;
}

export interface PipelineOwnerSummary {
  ownerId: string;
  ownerName: string;
  team?: string;
  dealCount: number;
  totalValue: number;
  weightedValue: number;
  quotaAttainment: number;
}

export interface PipelineTeamSummary {
  teamId: string;
  teamName: string;
  dealCount: number;
  totalValue: number;
  weightedValue: number;
  memberCount: number;
  avgPerMember: number;
}

export interface PipelineVelocity {
  avgDaysInStage: Record<string, number>;
  overallCycle: number;
  cycleTarget: number;
  cycleVariance: number;
}

// ============================================================
// LEAD TYPES
// ============================================================

export type LeadStatus =
  | 'NEW'
  | 'CONTACTED'
  | 'QUALIFIED'
  | 'CONVERTED'
  | 'UNQUALIFIED'
  | 'LOST';

export type LeadSource =
  | 'WEBSITE'
  | 'REFERRAL'
  | 'PARTNER'
  | 'EVENT'
  | 'COLD_CALL'
  | 'EMAIL_CAMPAIGN'
  | 'SOCIAL_MEDIA'
  | 'ADVERTISEMENT'
  | 'OTHER';

export type LeadRating = 'hot' | 'warm' | 'cold';

export interface Lead {
  id: string;
  leadNumber: string;
  firstName: string;
  lastName: string;
  email?: string;
  phone?: string;
  company?: string;
  title?: string;
  industry?: string;
  status: LeadStatus;
  source: LeadSource;
  rating: LeadRating;
  estimatedValue: number;
  currency: string;
  assignedTo?: string;
  assignedToName?: string;
  assignedTeam?: string;
  territory?: string;
  tags: string[];
  notes?: string;
  lastContactedAt?: Date;
  nextFollowUp?: Date;
  convertedToDeal?: boolean;
  dealId?: string;
  activities: LeadActivity[];
  createdAt: Date;
  updatedAt: Date;
}

export interface LeadActivity {
  id: string;
  type: 'call' | 'email' | 'meeting' | 'note' | 'task';
  subject: string;
  description?: string;
  createdBy: string;
  createdByName: string;
  createdAt: Date;
}

export interface LeadAssignment {
  leadId: string;
  assignedTo: string;
  assignedToName: string;
  assignedBy: string;
  assignedByName: string;
  assignedAt: Date;
  score: number;
  reason?: string;
}

// ============================================================
// CUSTOMER TYPES
// ============================================================

export type CustomerStatus = 'active' | 'at_risk' | 'churned' | 'prospect';

export type CustomerTier = 'enterprise' | 'mid_market' | 'small_business';

export interface Customer {
  id: string;
  customerNumber: string;
  name: string;
  logo?: string;
  industry: string;
  tier: CustomerTier;
  status: CustomerStatus;
  address: Address;
  contacts: CustomerContact[];
  accountOwner: {
    id: string;
    name: string;
    email: string;
  };
  teamOwner?: {
    id: string;
    name: string;
  };
  metrics: {
    totalRevenue: number;
    dealsCount: number;
    avgDealSize: number;
    ltv: number;
    firstPurchaseDate: Date;
    lastPurchaseDate: Date;
    nextRenewalDate?: Date;
  };
  territory?: string;
  tags: string[];
  notes?: string;
  createdAt: Date;
  updatedAt: Date;
}

export interface CustomerContact {
  id: string;
  firstName: string;
  lastName: string;
  title: string;
  email: string;
  phone?: string;
  isPrimary: boolean;
}

export interface CustomerAnalytics {
  totalCustomers: number;
  activeCustomers: number;
  atRiskCustomers: number;
  churnedCustomers: number;
  totalRevenue: number;
  avgRevenuePerCustomer: number;
  byIndustry: Record<string, number>;
  byTier: Record<string, number>;
  retentionRate: number;
  acquisitionRate: number;
  churnRate: number;
}

// ============================================================
// PERFORMANCE TYPES
// ============================================================

export interface PeriodInfo {
  start: Date;
  end: Date;
  type: 'daily' | 'weekly' | 'monthly' | 'quarterly' | 'yearly';
  label: string;
}

export interface CountryMetric {
  value: number;
  label: string;
  change: number;
  changeType: 'increase' | 'decrease' | 'neutral';
  target?: number;
  attainment?: number;
  currency?: string;
}

export interface CountryDashboardSummary {
  period: PeriodInfo;
  country: CountryInfo;
  metrics: {
    revenue: CountryMetric;
    pipelineValue: CountryMetric;
    dealsClosed: CountryMetric;
    activeDeals: CountryMetric;
    winRate: CountryMetric;
    avgDealSize: CountryMetric;
    newLeads: CountryMetric;
    partners: CountryMetric;
  };
  teamRankings: TeamRanking[];
  topPerformers: TopPerformer[];
  majorDeals: Deal[];
  alerts: CountryAlert[];
  forecastSummary: ForecastSummary;
}

export interface TeamRanking {
  rank: number;
  teamId: string;
  teamName: string;
  teamLead: string;
  revenue: number;
  attainment: number;
  dealsClosed: number;
  trend: 'up' | 'down' | 'neutral';
}

export interface TopPerformer {
  userId: string;
  name: string;
  avatar?: string;
  role: CountrySalesRole;
  team: string;
  revenue: number;
  quota: number;
  attainment: number;
  dealsClosed: number;
  rank: number;
  activities: {
    calls: number;
    emails: number;
    meetings: number;
  };
}

export interface CountryAlert {
  id: string;
  type: 'info' | 'warning' | 'success' | 'error';
  category: 'quota' | 'deal' | 'forecast' | 'team' | 'partner' | 'system';
  title: string;
  message: string;
  severity: 'low' | 'medium' | 'high' | 'critical';
  actionUrl?: string;
  createdAt: Date;
  isRead: boolean;
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

export interface PerformanceData {
  period: PeriodInfo;
  country: CountryInfo;
  summary: {
    revenue: {
      actual: number;
      target: number;
      attainment: number;
      change: number;
    };
    quota: {
      assigned: number;
      achieved: number;
      attainment: number;
      change: number;
    };
    deals: {
      closed: number;
      inPipeline: number;
      won: number;
      lost: number;
      change: number;
    };
    winRate: {
      value: number;
      change: number;
    };
  };
  teamComparison: TeamPerformance[];
  individualRankings: IndividualRanking[];
  trendAnalysis: PerformanceTrend[];
  productBreakdown: ProductPerformance[];
}

export interface IndividualRanking {
  rank: number;
  userId: string;
  name: string;
  avatar?: string;
  role: CountrySalesRole;
  team: string;
  revenue: number;
  attainment: number;
  dealsClosed: number;
  activities: {
    calls: number;
    emails: number;
    meetings: number;
  };
}

export interface PerformanceTrend {
  period: string;
  revenue: number;
  target: number;
  attainment: number;
  dealsClosed: number;
  winRate: number;
}

export interface ProductPerformance {
  productCode: string;
  productName: string;
  category: string;
  revenue: number;
  percentage: number;
  growth: number;
  targetAttainment: number;
  unitsSold: number;
}

// ============================================================
// TERRITORY TYPES
// ============================================================

export interface TerritoryAssignment {
  territoryId: string;
  territoryName: string;
  assignedTo: {
    id: string;
    name: string;
    type: 'individual' | 'team';
  }[];
  quota: number;
  attainment: number;
  pipelineValue: number;
  dealsClosed: number;
  leads: number;
}

export interface TerritoryManagement {
  territories: Territory[];
  assignments: TerritoryAssignment[];
  unassignedAreas: string[];
  capacityAnalysis: CapacityAnalysis[];
}

export interface CapacityAnalysis {
  territoryId: string;
  territoryName: string;
  currentCapacity: number;
  maxCapacity: number;
  utilizationPercent: number;
  recommendation: 'expand' | 'maintain' | 'reduce';
}

// ============================================================
// REPORT TYPES
// ============================================================

export type CountryReportType =
  | 'SALES_PERFORMANCE'
  | 'TEAM_PERFORMANCE'
  | 'PIPELINE_ANALYSIS'
  | 'PARTNER_PERFORMANCE'
  | 'CUSTOMER_ANALYSIS'
  | 'TERRITORY_REPORT'
  | 'LEAD_CONVERSION'
  | 'FORECAST_REPORT'
  | 'ACTIVITY_REPORT'
  | 'CUSTOM';

export type ReportCategory =
  | 'SALES'
  | 'TEAM'
  | 'PIPELINE'
  | 'PARTNER'
  | 'CUSTOMER'
  | 'TERRITORY'
  | 'FORECAST';

export interface CountryReport {
  id: string;
  name: string;
  description: string;
  type: CountryReportType;
  category: ReportCategory;
  createdBy: string;
  createdAt: Date;
  lastRunAt?: Date;
  schedule?: ReportSchedule;
  config: ReportConfig;
}

export interface ReportConfig {
  metrics: string[];
  teams?: string[];
  members?: string[];
  period: PeriodInfo;
  filters?: Record<string, any>;
  exportFormat: 'PDF' | 'EXCEL' | 'CSV';
}

export interface ReportSchedule {
  frequency: 'daily' | 'weekly' | 'monthly';
  dayOfWeek?: number;
  dayOfMonth?: number;
  time: string;
  recipients: string[];
  active: boolean;
  nextRunAt?: Date;
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

// ============================================================
// SETTINGS TYPES
// ============================================================

export interface CountrySettings {
  country: CountryInfo;
  businessHours: BusinessHours;
  quotas: QuotaSettings;
  commission: CommissionSettings;
  pipeline: PipelineSettings;
  notifications: NotificationSettings;
  integrations: IntegrationSettings[];
  updatedAt: Date;
  updatedBy: string;
}

export interface BusinessHours {
  timezone: string;
  workingDays: number[];
  workingHours: {
    start: string;
    end: string;
  };
}

export interface QuotaSettings {
  defaultIndividualQuota: number;
  defaultTeamQuota: number;
  quotaPeriod: 'monthly' | 'quarterly';
  autoAdjustQuota: boolean;
}

export interface CommissionSettings {
  enabled: boolean;
  baseRate: number;
  tierBonusEnabled: boolean;
  tiers: CommissionTier[];
  payoutFrequency: 'MONTHLY' | 'QUARTERLY';
  payoutDayOfMonth: number;
}

export interface CommissionTier {
  tier: PartnerTier;
  rate: number;
  minRevenue: number;
}

export interface PipelineSettings {
  stages: DealStage[];
  defaultStage: DealStage;
  autoAdvanceDays: number;
  probabilityPerStage: Record<string, number>;
}

export interface NotificationSettings {
  email: boolean;
  push: boolean;
  sms: boolean;
  alerts: {
    dealStalled: boolean;
    dealWon: boolean;
    dealLost: boolean;
    quotaAtRisk: boolean;
    newLead: boolean;
    partnerApproval: boolean;
  };
}

export interface IntegrationSettings {
  type: string;
  name: string;
  enabled: boolean;
  config: Record<string, any>;
}

// ============================================================
// COMMON TYPES
// ============================================================

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

// Filters
export interface DealFilters {
  page?: number;
  pageSize?: number;
  sortBy?: string;
  sortOrder?: 'asc' | 'desc';
  search?: string;
  stage?: DealStage[];
  owner?: string[];
  team?: string[];
  status?: DealStage[];
  valueMin?: number;
  valueMax?: number;
  closeDateFrom?: Date;
  closeDateTo?: Date;
}

export interface LeadFilters {
  page?: number;
  pageSize?: number;
  sortBy?: string;
  sortOrder?: 'asc' | 'desc';
  search?: string;
  status?: LeadStatus[];
  source?: LeadSource[];
  rating?: LeadRating[];
  assignedTo?: string[];
  team?: string[];
  territory?: string[];
}

export interface PartnerFilters {
  page?: number;
  pageSize?: number;
  sortBy?: string;
  sortOrder?: 'asc' | 'desc';
  search?: string;
  partnerType?: PartnerType[];
  status?: PartnerStatus[];
  tier?: PartnerTier[];
  territory?: string[];
}

export interface CustomerFilters {
  page?: number;
  pageSize?: number;
  sortBy?: string;
  sortOrder?: 'asc' | 'desc';
  search?: string;
  status?: CustomerStatus[];
  tier?: CustomerTier[];
  industry?: string[];
  territory?: string[];
}

export interface TeamFilters {
  page?: number;
  pageSize?: number;
  sortBy?: string;
  sortOrder?: 'asc' | 'desc';
  search?: string;
  status?: TeamStatus[];
  region?: string[];
  territory?: string[];
}
