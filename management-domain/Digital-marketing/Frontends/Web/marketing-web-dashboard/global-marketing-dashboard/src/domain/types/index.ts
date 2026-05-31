// Domain Types for Global Marketing Dashboard
// Comprehensive TypeScript interfaces for all marketing entities

// User & Authentication Types
export type MarketingRole =
  | 'GLOBAL_CMO'
  | 'VP_MARKETING'
  | 'GLOBAL_MARKETING_DIRECTOR'
  | 'REGIONAL_MARKETING_MANAGER'
  | 'CAMPAIGN_MANAGER'
  | 'CONTENT_MANAGER'
  | 'SOCIAL_MEDIA_MANAGER'
  | 'EMAIL_MARKETING_MANAGER'
  | 'SEO_MANAGER'
  | 'MARKETING_ANALYST'
  | 'BRAND_MANAGER';

export type RegionScope =
  | 'GLOBAL'
  | 'NORTH_AMERICA'
  | 'EMEA'
  | 'APAC'
  | 'LATAM';

export interface Permission {
  id: string;
  name: string;
  resource: string;
  action: 'create' | 'read' | 'update' | 'delete' | 'approve' | 'publish';
  scope?: RegionScope;
}

export interface MarketingUser {
  id: string;
  email: string;
  firstName: string;
  lastName: string;
  avatar?: string;
  role: MarketingRole;
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
  user: MarketingUser;
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
    budget: number;
    spent: number;
    revenue: number;
    leads: number;
    conversions: number;
    impressions: number;
    clicks: number;
    roi: number;
    roas: number;
  };
  trend: 'up' | 'down' | 'neutral';
  status: 'on_track' | 'at_risk' | 'off_track';
}

export interface GlobalTopCampaign {
  id: string;
  name: string;
  country: CountryInfo;
  channel: string;
  revenue: number;
  budget: number;
  roi: number;
  leads: number;
  rank: number;
}

export interface GlobalAlert {
  id: string;
  type: 'info' | 'warning' | 'success' | 'error';
  category: 'campaign' | 'budget' | 'lead' | 'brand' | 'content' | 'system';
  title: string;
  message: string;
  affectedCountries?: CountryInfo[];
  severity: 'low' | 'medium' | 'high' | 'critical';
  actionUrl?: string;
  createdAt: Date;
  read: boolean;
}

export interface GlobalDashboardSummary {
  period: PeriodInfo;
  globalMetrics: {
    campaigns: GlobalMetric;
    budget: GlobalMetric;
    spent: GlobalMetric;
    revenue: GlobalMetric;
    leads: GlobalMetric;
    conversions: GlobalMetric;
    impressions: GlobalMetric;
    avgROAS: GlobalMetric;
    avgROI: GlobalMetric;
  };
  countriesSummary: CountrySummary[];
  topCampaigns: GlobalTopCampaign[];
  alerts: GlobalAlert[];
}

// Campaign Types
export type CampaignStatus =
  | 'draft'
  | 'scheduled'
  | 'active'
  | 'paused'
  | 'completed'
  | 'cancelled';

export type CampaignType =
  | 'awareness'
  | 'consideration'
  | 'conversion'
  | 'retention';

export type ChannelType =
  | 'email'
  | 'social'
  | 'search'
  | 'display'
  | 'video'
  | 'direct_mail'
  | 'events'
  | 'webinars'
  | 'content'
  | 'other';

export interface CountryTarget {
  countryCode: string;
  countryName: string;
  budget: number;
  targetAudience: string;
}

export interface CampaignMetrics {
  impressions: number;
  clicks: number;
  conversions: number;
  cost: number;
  revenue: number;
  leads: number;
  ctr: number;
  cpc: number;
  cpa: number;
  roas: number;
  engagement?: number;
}

export interface Campaign {
  id: string;
  name: string;
  description: string;
  status: CampaignStatus;
  type: CampaignType;
  channels: ChannelType[];
  countries: CountryTarget[];
  budget: {
    total: number;
    spent: number;
    remaining: number;
    currency: string;
  };
  dates: {
    start: Date;
    end: Date;
    createdAt: Date;
    updatedAt: Date;
  };
  metrics: CampaignMetrics;
  owner: {
    id: string;
    name: string;
    email: string;
  };
  brand: {
    id: string;
    name: string;
  };
  tags: string[];
  objectives: string[];
  kpis: {
    name: string;
    target: number;
    current: number;
    unit: string;
  }[];
}

export interface CampaignCreateInput {
  name: string;
  description: string;
  type: CampaignType;
  channels: ChannelType[];
  countries: Omit<CountryTarget, 'countryName'>[];
  budgetTotal: number;
  startDate: Date;
  endDate: Date;
  brandId: string;
  objectives: string[];
  kpis: Array<{
    name: string;
    target: number;
    unit: string;
  }>;
}

export interface CampaignUpdateInput {
  name?: string;
  description?: string;
  status?: CampaignStatus;
  budgetTotal?: number;
  endDate?: Date;
  tags?: string[];
}

// Budget Types
export type BudgetStatus = 'draft' | 'approved' | 'active' | 'exceeded' | 'exhausted';

export interface BudgetAllocation {
  countryId: string;
  countryName: string;
  amount: number;
  spent: number;
  remaining: number;
  percentage: number;
}

export interface Budget {
  id: string;
  name: string;
  fiscalYear: number;
  quarter?: number;
  totalAmount: number;
  spentAmount: number;
  remainingAmount: number;
  currency: string;
  status: BudgetStatus;
  allocations: BudgetAllocation[];
  categories: {
    channel: string;
    amount: number;
    percentage: number;
  }[];
  approval: {
    requestedBy: string;
    requestedAt: Date;
    approvedBy?: string;
    approvedAt?: Date;
    status: 'pending' | 'approved' | 'rejected';
  };
  createdAt: Date;
  updatedAt: Date;
}

export interface BudgetCreateInput {
  name: string;
  fiscalYear: number;
  quarter?: number;
  totalAmount: number;
  currency: string;
  allocations: Omit<BudgetAllocation, 'countryName' | 'spent' | 'remaining' | 'percentage'>[];
  categories: Array<{
    channel: string;
    amount: number;
  }>;
}

// Lead Types
export type LeadStatus =
  | 'new'
  | 'contacted'
  | 'qualified'
  | 'proposal'
  | 'negotiation'
  | 'won'
  | 'lost';

export type LeadSource =
  | 'website'
  | 'email'
  | 'social_media'
  | 'search_engine'
  | 'referral'
  | 'event'
  | 'advertisement'
  | 'content'
  | 'webinar'
  | 'other';

export type LeadQuality = 'hot' | 'warm' | 'cold' | 'unqualified';

export interface Lead {
  id: string;
  firstName: string;
  lastName: string;
  email: string;
  phone?: string;
  company?: string;
  jobTitle?: string;
  status: LeadStatus;
  quality: LeadQuality;
  source: LeadSource;
  campaign: {
    id: string;
    name: string;
    channel: string;
  };
  country: {
    code: string;
    name: string;
  };
  assignedTo?: {
    id: string;
    name: string;
    email: string;
  };
  value: {
    estimated: number;
    actual?: number;
    currency: string;
  };
  score: number;
  activities: {
    type: string;
    description: string;
    timestamp: Date;
  }[];
  customFields: Record<string, string | number | boolean>;
  createdAt: Date;
  updatedAt: Date;
  convertedAt?: Date;
  handoffToSales?: {
    salesUserId: string;
    salesUserName: string;
    handedOffAt: Date;
    status: 'pending' | 'accepted' | 'declined';
  };
}

export interface LeadMetrics {
  total: number;
  new: number;
  contacted: number;
  qualified: number;
  converted: number;
  lost: number;
  conversionRate: number;
  averageValue: number;
  totalValue: number;
  bySource: Record<LeadSource, number>;
  byCountry: Record<string, number>;
  byCampaign: Record<string, number>;
  byQuality: Record<LeadQuality, number>;
}

// Brand Types
export type BrandStatus = 'active' | 'archived' | 'draft';

export interface BrandAsset {
  id: string;
  name: string;
  type: 'logo' | 'image' | 'video' | 'document' | 'template';
  url: string;
  thumbnailUrl?: string;
  size: number;
  format: string;
  tags: string[];
  uploadedBy: string;
  uploadedAt: Date;
}

export interface BrandGuideline {
  id: string;
  category: string;
  title: string;
  content: string;
  attachments: string[];
  version: string;
  lastUpdated: Date;
  updatedBy: string;
}

export interface Brand {
  id: string;
  name: string;
  description: string;
  logo: string;
  status: BrandStatus;
  colors: {
    primary: string;
    secondary: string;
    accent: string;
    neutral: string[];
  };
  typography: {
    primary: string;
    secondary: string;
    headings: string;
  };
  tone: string[];
  taglines: string[];
  assets: BrandAsset[];
  guidelines: BrandGuideline[];
  regions: string[];
  createdAt: Date;
  updatedAt: Date;
}

export interface BrandTemplate {
  id: string;
  brandId: string;
  name: string;
  type: 'email' | 'social' | 'document' | 'presentation' | 'banner';
  thumbnail: string;
  fileUrl: string;
  category: string;
  tags: string[];
  usage: number;
  createdAt: Date;
}

// Content Types
export type ContentStatus = 'draft' | 'pending_approval' | 'approved' | 'scheduled' | 'published' | 'archived';

export type ContentType =
  | 'blog_post'
  | 'social_post'
  | 'email'
  | 'video'
  | 'infographic'
  | 'whitepaper'
  | 'case_study'
  | 'ebook'
  | 'webinar'
  | 'podcast'
  | 'press_release'
  | 'landing_page'
  | 'other';

export interface Content {
  id: string;
  title: string;
  type: ContentType;
  status: ContentStatus;
  excerpt: string;
  content: string;
  author: {
    id: string;
    name: string;
    avatar?: string;
  };
  brandId: string;
  campaignIds: string[];
  tags: string[];
  categories: string[];
  seo: {
    title: string;
    description: string;
    keywords: string[];
    slug: string;
  };
  publishing: {
    publishAt?: Date;
    publishedAt?: Date;
    channels: string[];
    countries: string[];
  };
  metrics: {
    views: number;
    clicks: number;
    shares: number;
    comments: number;
    downloads: number;
    leads: number;
    engagementRate: number;
  };
  approval: {
    requestedBy?: string;
    requestedAt?: Date;
    approvedBy?: string;
    approvedAt?: Date;
    status: 'none' | 'pending' | 'approved' | 'rejected';
    rejectionReason?: string;
  };
  createdAt: Date;
  updatedAt: Date;
}

export interface ContentCalendar {
  date: Date;
  items: {
    contentId: string;
    title: string;
    type: ContentType;
    status: ContentStatus;
    channels: string[];
    countries: string[];
  }[];
}

// Social Media Types
export type SocialPlatform = 'facebook' | 'instagram' | 'twitter' | 'linkedin' | 'tiktok' | 'youtube' | 'pinterest';

export type SocialPostStatus = 'draft' | 'scheduled' | 'published' | 'failed';

export interface SocialPost {
  id: string;
  content: string;
  platform: SocialPlatform;
  status: SocialPostStatus;
  media: {
    type: 'image' | 'video' | 'document' | 'link';
    url: string;
    thumbnailUrl?: string;
  }[];
  campaignId?: string;
  contentId?: string;
  scheduledFor?: Date;
  publishedAt?: Date;
  metrics: {
    impressions: number;
    reach: number;
    engagement: number;
    likes: number;
    comments: number;
    shares: number;
    clicks: number;
    saves: number;
  };
  country: string;
  createdBy: string;
  createdAt: Date;
  updatedAt: Date;
}

export interface SocialAccount {
  id: string;
  platform: SocialPlatform;
  accountName: string;
  accountId: string;
  profileImage: string;
  followers: number;
  verified: boolean;
  connected: boolean;
  lastSync: Date;
  country: string;
}

// Email Types
export type EmailStatus = 'draft' | 'scheduled' | 'sending' | 'sent' | 'failed';

export type EmailType = 'campaign' | 'automation' | 'transactional' | 'one_off';

export interface EmailList {
  id: string;
  name: string;
  description: string;
  subscriberCount: number;
  tags: string[];
  country?: string;
  createdAt: Date;
  updatedAt: Date;
}

export interface EmailCampaign {
  id: string;
  name: string;
  subject: string;
  previewText: string;
  fromName: string;
  fromEmail: string;
  type: EmailType;
  status: EmailStatus;
  listId: string;
  listName: string;
  templateId?: string;
  content: {
    html: string;
    text: string;
  };
  scheduledFor?: Date;
  sentAt?: Date;
  metrics: {
    sent: number;
    delivered: number;
    opened: number;
    clicked: number;
    bounced: number;
    unsubscribed: number;
    complained: number;
    openRate: number;
    clickRate: number;
    bounceRate: number;
    unsubscribeRate: number;
  };
  country: string;
  createdBy: string;
  createdAt: Date;
  updatedAt: Date;
}

export interface EmailAutomation {
  id: string;
  name: string;
  description: string;
  trigger: {
    type: 'webhook' | 'form_submit' | 'link_click' | 'email_open' | 'list_added' | 'date_based';
    config: Record<string, unknown>;
  };
  steps: Array<{
    id: string;
    type: 'email' | 'delay' | 'condition' | 'action';
    order: number;
    config: Record<string, unknown>;
  }>;
  status: 'active' | 'paused' | 'draft';
  metrics: {
    enrolled: number;
    completed: number;
    inProgress: number;
    conversionRate: number;
  };
  createdAt: Date;
  updatedAt: Date;
}

// SEO Types
export type KeywordDifficulty = 'low' | 'medium' | 'high' | 'very_high';

export interface Keyword {
  id: string;
  keyword: string;
  volume: number;
  difficulty: KeywordDifficulty;
  cpc: number;
  currentRanking: number;
  previousRanking: number;
  change: number;
  country: string;
  url?: string;
  trackedSince: Date;
}

export interface Backlink {
  id: string;
  sourceUrl: string;
  sourceDomain: string;
  sourceDomainAuthority: number;
  targetUrl: string;
  anchorText: string;
  follow: boolean;
  discoveredAt: Date;
  lostAt?: Date;
  status: 'active' | 'lost';
}

export interface SEOAudit {
  id: string;
  url: string;
  pageType: string;
  overallScore: number;
  checks: Array<{
    category: string;
    name: string;
    status: 'pass' | 'warning' | 'fail';
    impact: 'high' | 'medium' | 'low';
    description: string;
  }>;
  auditedAt: Date;
}

export interface SEOMetrics {
  organicTraffic: number;
  organicTrafficChange: number;
  organicKeywords: number;
  organicKeywordsChange: number;
  avgPosition: number;
  backlinks: number;
  backlinksChange: number;
  domainAuthority: number;
  pageAuthority: number;
}

// Analytics Types
export type ChartType = 'line' | 'bar' | 'pie' | 'funnel' | 'scatter' | 'heatmap' | 'table';

export interface MetricDataPoint {
  date: Date;
  value: number;
  label?: string;
  metadata?: Record<string, unknown>;
}

export interface ChannelMetrics {
  channel: string;
  impressions: number;
  clicks: number;
  conversions: number;
  cost: number;
  revenue: number;
  roi: number;
  roas: number;
}

export interface AttributionModel {
  type: 'first_touch' | 'last_touch' | 'linear' | 'time_decay' | 'position_based';
  touchpoints: Array<{
    channel: string;
    touchpoint: string;
    credit: number;
    revenue: number;
  }>;
}

export interface FunnelStage {
  name: string;
  count: number;
  dropoff: number;
  dropoffRate: number;
  conversionRate: number;
}

export interface ConversionFunnel {
  stages: FunnelStage[];
  overallConversionRate: number;
}

// Report Types
export type ReportType =
  | 'CAMPAIGN_PERFORMANCE'
  | 'CHANNEL_ANALYSIS'
  | 'LEAD_GENERATION'
  | 'ROI_ANALYSIS'
  | 'BUDGET_UTILIZATION'
  | 'SOCIAL_MEDIA'
  | 'EMAIL_MARKETING'
  | 'SEO_PERFORMANCE'
  | 'CONTENT_PERFORMANCE'
  | 'EXECUTIVE_SUMMARY'
  | 'CUSTOM';

export type ReportFormat = 'PDF' | 'EXCEL' | 'CSV' | 'JSON';

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
  format: ReportFormat;
  createdBy: string;
  createdAt: Date;
  lastRunAt?: Date;
  schedule?: ReportSchedule;
  config: {
    period: PeriodInfo;
    filters: Record<string, unknown>;
    metrics: string[];
    countries?: string[];
    campaigns?: string[];
    visualizations: Array<{
      type: ChartType;
      title: string;
      dataSource: string;
    }>;
  };
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
  format: ReportFormat;
  expiresAt?: Date;
  error?: string;
}

// Settings Types
export interface UserSettings {
  userId: string;
  preferences: {
    theme: 'light' | 'dark' | 'system';
    language: string;
    timezone: string;
    currency: string;
    dateFormat: string;
  };
  notifications: {
    email: boolean;
    push: boolean;
    campaignAlerts: boolean;
    budgetAlerts: boolean;
    leadAlerts: boolean;
    weeklyDigest: boolean;
  };
  dashboard: {
    defaultView: string;
    hiddenWidgets: string[];
    widgetOrder: string[];
  };
  updatedAt: Date;
}

export interface Integration {
  id: string;
  name: string;
  type: string;
  logo: string;
  status: 'connected' | 'disconnected' | 'error';
  configured: boolean;
  lastSync?: Date;
  config: Record<string, unknown>;
}

export interface TeamMember {
  id: string;
  user: MarketingUser;
  role: string;
  permissions: string[];
  countries: string[];
  channels: string[];
  addedAt: Date;
  addedBy: string;
}

// Pagination & API Types
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
    details?: unknown;
    timestamp: string;
    requestId: string;
  };
}

// Filter Types
export interface CampaignFilters {
  page?: number;
  pageSize?: number;
  sortBy?: string;
  sortOrder?: 'asc' | 'desc';
  search?: string;
  status?: CampaignStatus[];
  type?: CampaignType[];
  channel?: ChannelType[];
  country?: string[];
  dateRange?: {
    start: Date;
    end: Date;
  };
}

export interface LeadFilters {
  page?: number;
  pageSize?: number;
  sortBy?: string;
  sortOrder?: 'asc' | 'desc';
  search?: string;
  status?: LeadStatus[];
  quality?: LeadQuality[];
  source?: LeadSource[];
  country?: string[];
  dateRange?: {
    start: Date;
    end: Date;
  };
}

export interface ContentFilters {
  page?: number;
  pageSize?: number;
  sortBy?: string;
  sortOrder?: 'asc' | 'desc';
  search?: string;
  status?: ContentStatus[];
  type?: ContentType[];
  country?: string[];
  author?: string[];
  dateRange?: {
    start: Date;
    end: Date;
  };
}
