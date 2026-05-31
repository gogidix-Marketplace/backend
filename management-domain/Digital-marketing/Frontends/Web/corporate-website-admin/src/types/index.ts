// User & Authentication Types
export interface User {
  id: string;
  email: string;
  firstName: string;
  lastName: string;
  avatar?: string;
  role: UserRole;
  permissions: Permission[];
  department?: string;
  createdAt: string;
  updatedAt: string;
}

export type UserRole = 'admin' | 'editor' | 'author' | 'contributor' | 'viewer';

export type Permission =
  | 'content:read'
  | 'content:write'
  | 'content:publish'
  | 'content:delete'
  | 'products:read'
  | 'products:write'
  | 'users:read'
  | 'users:write'
  | 'settings:read'
  | 'settings:write'
  | 'analytics:read'
  | 'leads:read'
  | 'leads:write'
  | 'careers:read'
  | 'careers:write'
  | 'partners:read'
  | 'partners:write';

export interface AuthTokens {
  accessToken: string;
  refreshToken: string;
  expiresAt: number;
}

export interface LoginCredentials {
  email: string;
  password: string;
  rememberMe?: boolean;
}

export interface RegisterData {
  email: string;
  password: string;
  firstName: string;
  lastName: string;
}

// Content Types
export interface Page {
  id: string;
  title: string;
  slug: string;
  content: string;
  excerpt?: string;
  featuredImage?: MediaFile;
  status: ContentStatus;
  template: string;
  seo?: SeoData;
  author: User;
  publishedAt?: string;
  scheduledFor?: string;
  createdAt: string;
  updatedAt: string;
  meta?: Record<string, any>;
}

export interface BlogPost {
  id: string;
  title: string;
  slug: string;
  content: string;
  excerpt: string;
  featuredImage?: MediaFile;
  status: ContentStatus;
  category?: BlogCategory;
  tags: string[];
  author: User;
  seo?: SeoData;
  publishedAt?: string;
  scheduledFor?: string;
  createdAt: string;
  updatedAt: string;
  readTime?: number;
  viewCount?: number;
}

export interface PressRelease {
  id: string;
  title: string;
  slug: string;
  content: string;
  excerpt: string;
  featuredImage?: MediaFile;
  status: ContentStatus;
  releaseDate: string;
  contactInfo?: PressContact;
  seo?: SeoData;
  createdAt: string;
  updatedAt: string;
}

export interface Resource {
  id: string;
  title: string;
  slug: string;
  description: string;
  type: ResourceType;
  file?: MediaFile;
  externalLink?: string;
  thumbnail?: MediaFile;
  category: ResourceCategory;
  tags: string[];
  status: ContentStatus;
  featured: boolean;
  downloadCount?: number;
  createdAt: string;
  updatedAt: string;
}

export type ContentStatus = 'draft' | 'pending_review' | 'scheduled' | 'published' | 'archived';
export type ResourceType = 'ebook' | 'whitepaper' | 'case_study' | 'infographic' | 'video' | 'webinar' | 'template' | 'other';

export interface BlogCategory {
  id: string;
  name: string;
  slug: string;
  description?: string;
}

export interface ResourceCategory {
  id: string;
  name: string;
  slug: string;
  description?: string;
}

export interface SeoData {
  metaTitle?: string;
  metaDescription?: string;
  ogTitle?: string;
  ogDescription?: string;
  ogImage?: string;
  twitterCard?: string;
  keywords?: string[];
  canonicalUrl?: string;
  noIndex?: boolean;
}

export interface PressContact {
  name: string;
  email: string;
  phone?: string;
}

// Product Types
export interface Product {
  id: string;
  name: string;
  slug: string;
  description: string;
  longDescription?: string;
  logo?: MediaFile;
  images: MediaFile[];
  status: ContentStatus;
  category: ProductCategory;
  features: ProductFeature[];
  pricingPlans: PricingPlan[];
  integrations: Integration[];
  seo?: SeoData;
  featured: boolean;
  launchDate?: string;
  createdAt: string;
  updatedAt: string;
}

export interface ProductCategory {
  id: string;
  name: string;
  slug: string;
  description?: string;
  icon?: string;
  parentId?: string;
}

export interface ProductFeature {
  id: string;
  productId: string;
  name: string;
  description: string;
  icon?: string;
  highlighted: boolean;
  order: number;
}

export interface PricingPlan {
  id: string;
  productId: string;
  name: string;
  description?: string;
  price: number;
  currency: string;
  billingCycle: BillingCycle;
  features: string[];
  highlighted: boolean;
  order: number;
}

export type BillingCycle = 'monthly' | 'quarterly' | 'annual' | 'custom';

export interface Integration {
  id: string;
  name: string;
  slug: string;
  description: string;
  logo?: MediaFile;
  category: IntegrationCategory;
  status: IntegrationStatus;
  documentationUrl?: string;
  partner?: Partner;
}

export type IntegrationCategory = 'crm' | 'analytics' | 'communication' | 'productivity' | 'ecommerce' | 'other';
export type IntegrationStatus = 'native' | 'partner' | 'community' | 'beta';

// Developer Types
export interface ApiDocumentation {
  id: string;
  title: string;
  slug: string;
  content: string;
  version: string;
  baseUrl: string;
  endpoints: ApiEndpoint[];
  status: ContentStatus;
  lastUpdated: string;
  order: number;
}

export interface ApiEndpoint {
  id: string;
  method: 'GET' | 'POST' | 'PUT' | 'PATCH' | 'DELETE';
  path: string;
  summary: string;
  description?: string;
  parameters?: ApiParameter[];
  requestBody?: ApiRequestBody;
  responses: ApiResponse[];
  authentication: boolean;
}

export interface ApiParameter {
  name: string;
  type: string;
  required: boolean;
  description?: string;
  location: 'query' | 'path' | 'header';
}

export interface ApiRequestBody {
  contentType: string;
  schema: any;
  required: boolean;
}

export interface ApiResponse {
  statusCode: number;
  description: string;
  schema?: any;
}

export interface Sdk {
  id: string;
  name: string;
  language: string;
  version: string;
  description: string;
  icon?: string;
  downloadUrl?: string;
  documentationUrl?: string;
  githubUrl?: string;
  status: 'stable' | 'beta' | 'alpha' | 'deprecated';
}

export interface CodeExample {
  id: string;
  title: string;
  slug: string;
  description: string;
  language: string;
  code: string;
  category: string;
  tags: string[];
  apiEndpoint?: string;
  sdk?: string;
  order: number;
}

// Career Types
export interface Job {
  id: string;
  title: string;
  slug: string;
  description: string;
  responsibilities: string[];
  requirements: string[];
  benefits: string[];
  location: JobLocation;
  type: EmploymentType;
  department: string;
  experience?: ExperienceLevel;
  salary?: SalaryRange;
  status: JobStatus;
  featured: boolean;
  applicationCount?: number;
  publishedAt?: string;
  expiresAt?: string;
  createdAt: string;
  updatedAt: string;
}

export type JobLocation = { type: 'remote' } | { type: 'onsite'; location: string } | { type: 'hybrid'; location: string };
export type EmploymentType = 'full_time' | 'part_time' | 'contract' | 'internship';
export type ExperienceLevel = 'entry' | 'mid' | 'senior' | 'lead' | 'executive';
export type JobStatus = 'draft' | 'open' | 'closed' | 'on_hold' | 'archived';

export interface SalaryRange {
  min: number;
  max: number;
  currency: string;
  period: 'hourly' | 'monthly' | 'yearly';
}

export interface JobApplication {
  id: string;
  jobId: string;
  job?: Job;
  applicant: Applicant;
  coverLetter?: string;
  resume?: MediaFile;
  portfolio?: string;
  linkedIn?: string;
  github?: string;
  status: ApplicationStatus;
  stage: ApplicationStage;
  source?: string;
  appliedAt: string;
  updatedAt: string;
  notes?: ApplicationNote[];
}

export interface Applicant {
  firstName: string;
  lastName: string;
  email: string;
  phone?: string;
  avatar?: MediaFile;
}

export type ApplicationStatus = 'new' | 'reviewing' | 'shortlisted' | 'interviewed' | 'offered' | 'hired' | 'rejected' | 'withdrawn';
export type ApplicationStage = 'applied' | 'screening' | 'interview' | 'assessment' | 'offer' | 'hired' | 'rejected';

export interface ApplicationNote {
  id: string;
  content: string;
  author: User;
  createdAt: string;
}

// Partner Types
export interface PartnerProgram {
  id: string;
  name: string;
  slug: string;
  description: string;
  type: PartnerType;
  benefits: string[];
  requirements: string[];
  commissionStructure?: string;
  applicationForm?: any;
  status: ContentStatus;
  featured: boolean;
  order: number;
  createdAt: string;
  updatedAt: string;
}

export type PartnerType = 'referral' | 'reseller' | 'technology' | 'strategic' | 'affiliate';

export interface Partner {
  id: string;
  name: string;
  slug: string;
  logo?: MediaFile;
  description?: string;
  website?: string;
  program?: PartnerProgram;
  type: PartnerType;
  tier: PartnerTier;
  status: PartnerStatus;
  contactInfo?: PartnerContact;
  createdAt: string;
  updatedAt: string;
}

export type PartnerTier = 'bronze' | 'silver' | 'gold' | 'platinum';
export type PartnerStatus = 'pending' | 'active' | 'inactive' | 'suspended';

export interface PartnerContact {
  primaryName: string;
  primaryEmail: string;
  primaryPhone?: string;
  secondaryName?: string;
  secondaryEmail?: string;
}

export interface PartnerApplication {
  id: string;
  programId: string;
  program?: PartnerProgram;
  companyName: string;
  contactName: string;
  email: string;
  phone?: string;
  website?: string;
  description?: string;
  businessModel?: string;
  status: PartnerApplicationStatus;
  appliedAt: string;
  reviewedAt?: string;
  reviewedBy?: User;
  notes?: string;
}

export type PartnerApplicationStatus = 'pending' | 'approved' | 'rejected' | 'more_info_needed';

// Lead Types
export interface DemoRequest {
  id: string;
  firstName: string;
  lastName: string;
  email: string;
  phone?: string;
  company?: string;
  companySize?: string;
  industry?: string;
  jobTitle?: string;
  country?: string;
  interests: string[];
  message?: string;
  preferredDate?: string;
  preferredTime?: string;
  status: LeadStatus;
  source?: string;
  assignedTo?: User;
  score?: number;
  createdAt: string;
  updatedAt: string;
}

export interface SalesInquiry {
  id: string;
  firstName: string;
  lastName: string;
  email: string;
  phone?: string;
  company?: string;
  inquiryType: InquiryType;
  subject: string;
  message: string;
  budget?: string;
  timeline?: string;
  status: LeadStatus;
  source?: string;
  assignedTo?: User;
  priority: Priority;
  createdAt: string;
  updatedAt: string;
}

export interface SupportTicket {
  id: string;
  ticketNumber: string;
  customerId?: string;
  customerName: string;
  customerEmail: string;
  category: SupportCategory;
  subject: string;
  description: string;
  priority: Priority;
  status: TicketStatus;
  assignedTo?: User;
  resolution?: string;
  resolvedAt?: string;
  firstResponseAt?: string;
  slaDueAt?: string;
  attachments: MediaFile[];
  source: string;
  createdAt: string;
  updatedAt: string;
}

export type LeadStatus = 'new' | 'contacted' | 'qualified' | 'converted' | 'lost' | 'archived';
export type InquiryType = 'sales' | 'pricing' | 'enterprise' | 'partnership' | 'support' | 'other';
export type SupportCategory = 'technical' | 'billing' | 'feature_request' | 'bug_report' | 'other';
export type TicketStatus = 'open' | 'in_progress' | 'waiting_on_customer' | 'resolved' | 'closed';
export type Priority = 'low' | 'normal' | 'high' | 'urgent';

// Analytics Types
export interface AnalyticsData {
  period: AnalyticsPeriod;
  metrics: AnalyticsMetrics;
  visitors: VisitorMetrics;
  content: ContentMetrics;
  conversions: ConversionMetrics;
  seo: SeoMetrics;
}

export type AnalyticsPeriod = 'today' | 'yesterday' | 'last_7_days' | 'last_30_days' | 'last_90_days' | 'custom';

export interface AnalyticsMetrics {
  pageViews: number;
  uniqueVisitors: number;
  sessions: number;
  bounceRate: number;
  avgSessionDuration: number;
  pagesPerSession: number;
}

export interface VisitorMetrics {
  newVsReturning: { new: number; returning: number };
  topCountries: Array<{ country: string; visitors: number }>;
  topSources: Array<{ source: string; visitors: number }>;
  topDevices: Array<{ device: string; visitors: number }>;
}

export interface ContentMetrics {
  topPages: Array<{ page: string; views: number; avgDuration: number }>;
  topBlogPosts: Array<{ title: string; slug: string; views: number }>;
  topResources: Array<{ title: string; slug: string; downloads: number }>;
}

export interface ConversionMetrics {
  totalConversions: number;
  conversionRate: number;
  demoRequests: number;
  trialSignups: number;
  contactSubmissions: number;
  funnels: ConversionFunnel[];
}

export interface ConversionFunnel {
  name: string;
  steps: FunnelStep[];
  overallConversionRate: number;
}

export interface FunnelStep {
  name: string;
  count: number;
  dropOffCount: number;
  conversionRate: number;
}

export interface SeoMetrics {
  organicTraffic: number;
  organicKeywords: number;
  avgPosition: number;
  topKeywords: Array<{ keyword: string; position: number; clicks: number; impressions: number }>;
  backlinks: number;
  domainAuthority: number;
  pageSpeedScore: number;
}

// Settings Types
export interface GeneralSettings {
  siteName: string;
  siteDescription: string;
  siteUrl: string;
  logo?: MediaFile;
  favicon?: MediaFile;
  defaultSeoImage?: MediaFile;
  socialLinks: SocialLinks;
  contactInfo: ContactInfo;
}

export interface SocialLinks {
  twitter?: string;
  linkedin?: string;
  facebook?: string;
  instagram?: string;
  youtube?: string;
  github?: string;
}

export interface ContactInfo {
  email: string;
  phone?: string;
  address?: string;
  supportEmail?: string;
  salesEmail?: string;
}

export interface Workflow {
  id: string;
  name: string;
  description?: string;
  type: WorkflowType;
  triggers: WorkflowTrigger[];
  actions: WorkflowAction[];
  conditions?: WorkflowCondition[];
  active: boolean;
  createdAt: string;
  updatedAt: string;
}

export type WorkflowType = 'content_approval' | 'lead_assignment' | 'partner_application' | 'job_application' | 'custom';

export interface WorkflowTrigger {
  type: 'status_change' | 'form_submission' | 'time_based' | 'manual';
  config: any;
}

export interface WorkflowAction {
  type: 'send_email' | 'assign_user' | 'update_status' | 'create_task' | 'webhook';
  config: any;
}

export interface WorkflowCondition {
  field: string;
  operator: string;
  value: any;
}

export interface SystemIntegration {
  id: string;
  name: string;
  type: IntegrationProviderType;
  description?: string;
  config: any;
  active: boolean;
  lastSync?: string;
}

export type IntegrationProviderType = 'analytics' | 'crm' | 'email' | 'cms' | 'storage' | 'other';

// Media Types
export interface MediaFile {
  id: string;
  name: string;
  filename: string;
  url: string;
  thumbnailUrl?: string;
  mimeType: string;
  size: number;
  width?: number;
  height?: number;
  alt?: string;
  caption?: string;
  folder?: string;
  tags: string[];
  uploadedBy: User;
  createdAt: string;
}

// API Response Types
export interface ApiResponse<T = any> {
  success: boolean;
  data?: T;
  error?: ApiError;
  message?: string;
  pagination?: PaginationInfo;
}

export interface ApiError {
  code: string;
  message: string;
  details?: any;
}

export interface PaginationInfo {
  page: number;
  limit: number;
  total: number;
  totalPages: number;
  hasNext: boolean;
  hasPrev: boolean;
}

export interface ListParams {
  page?: number;
  limit?: number;
  search?: string;
  sort?: string;
  order?: 'asc' | 'desc';
  filters?: Record<string, any>;
}
