// Domain Types for Country Marketing Dashboard
// Country-specific marketing types

// User & Authentication Types
export type CountryMarketingRole =
  | 'COUNTRY_MARKETING_MANAGER'
  | 'LOCAL_CAMPAIGN_MANAGER'
  | 'CONTENT_CREATOR'
  | 'SOCIAL_MEDIA_SPECIALIST'
  | 'EMAIL_SPECIALIST'
  | 'SEO_SPECIALIST'
  | 'MARKETING_COORDINATOR';

export interface CountryUser {
  id: string;
  email: string;
  firstName: string;
  lastName: string;
  avatar?: string;
  role: CountryMarketingRole;
  country: {
    code: string;
    name: string;
    flag: string;
    currency: string;
  };
  permissions: string[];
  timezone: string;
  createdAt: Date;
  lastLoginAt: Date;
}

// Local Campaign Types
export type LocalCampaignStatus = 'draft' | 'pending_approval' | 'approved' | 'active' | 'paused' | 'completed' | 'cancelled';

export type LocalCampaignType = 'local_brand_awareness' | 'local_promotion' | 'event_marketing' | 'local_content' | 'referral_program';

export interface LocalCampaign {
  id: string;
  name: string;
  description: string;
  status: LocalCampaignStatus;
  type: LocalCampaignType;
  channels: string[];
  budget: {
    total: number;
    spent: number;
    remaining: number;
    currency: string;
    approvedAt?: Date;
  };
  dates: {
    start: Date;
    end: Date;
    createdAt: Date;
    updatedAt: Date;
  };
  metrics: {
    impressions: number;
    clicks: number;
    conversions: number;
    cost: number;
    revenue: number;
    leads: number;
    ctr: number;
    cpa: number;
    roas: number;
  };
  owner: {
    id: string;
    name: string;
    email: string;
  };
  country: {
    code: string;
    name: string;
  };
  targetAudience: string;
  objectives: string[];
  tags: string[];
  globalCampaignId?: string;
}

// Local Budget Types
export interface LocalBudget {
  id: string;
  name: string;
  fiscalYear: number;
  month?: number;
  totalAmount: number;
  spentAmount: number;
  remainingAmount: number;
  currency: string;
  status: 'pending' | 'approved' | 'active' | 'exhausted';
  categories: {
    category: string;
    amount: number;
    percentage: number;
  }[];
  requestStatus: 'none' | 'pending' | 'approved' | 'rejected';
  rejectionReason?: string;
  createdAt: Date;
  updatedAt: Date;
}

// Local Content Types
export type LocalContentStatus = 'draft' | 'pending_approval' | 'approved' 'scheduled' | 'published';

export interface LocalContent {
  id: string;
  title: string;
  type: 'blog_post' | 'social_post' | 'email' | 'event_announcement' | 'local_news';
  status: LocalContentStatus;
  content: string;
  excerpt: string;
  author: {
    id: string;
    name: email: };
  country: {
    code: string;
    name: string;
  };
  language?: string;
  publishing: {
    scheduledFor?: Date;
    publishedAt?: Date;
    channels: string[];
  };
  metrics: {
    views: number;
    clicks: number;
    shares: number;
    likes: number;
    leads: number;
  };
  approval: {
    requestedBy?: string;
    requestedAt?: Date;
    approvedBy?: string;
    approvedAt?: Date;
    status: 'none' | 'pending' | 'approved' | 'rejected';
  };
  createdAt: Date;
  updatedAt: Date;
}

// Social Media Post Types
export interface LocalSocialPost {
  id: string;
  content: string;
  platform: 'facebook' | 'instagram' | 'twitter' | 'linkedin' | 'tiktok';
  status: 'draft' | 'scheduled' | 'published' | 'failed';
  media: {
    type: 'image' | 'video';
    url: string;
  }[];
  scheduledFor?: Date;
  publishedAt?: Date;
  metrics: {
    impressions: number;
    engagement: number;
    likes: number;
    comments: number;
    shares: number;
    clicks: number;
  };
  country: string;
  createdBy: string;
  createdAt: Date;
  updatedAt: Date;
}

// Email Marketing Types
export interface LocalEmailCampaign {
  id: string;
  name: string;
  subject: string;
  preheader: string;
  fromName: string;
  fromEmail: string;
  type: 'newsletter' | 'promotion' | 'event_invite' | 'announcement';
  status: 'draft' | 'scheduled' | 'sent' | 'failed';
  listId: string;
  listName: string;
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
    openRate: number;
    clickRate: number;
    bounceRate: number;
  };
  country: string;
  createdBy: string;
  createdAt: Date;
  updatedAt: Date;
}

// Lead Types
export interface LocalLead {
  id: string;
  firstName: string;
  lastName: string;
  email: string;
  phone?: string;
  company?: string;
  jobTitle?: string;
  status: 'new' | 'contacted' | 'qualified' | 'proposal' | 'closed' | 'lost';
  source: string;
  campaign?: string;
  score: number;
  value: {
    estimated: number;
    actual?: number;
    currency: string;
  };
  assignedTo?: {
    id: string;
    name: string;
  };
  activities: {
    type: string;
    description: string;
    timestamp: Date;
  }[];
  country: string;
  handoffToSales?: {
    salesUserId: string;
    salesUserName: string;
    handedOffAt: Date;
    status: 'pending' | 'accepted' | 'declined';
  };
  createdAt: Date;
  updatedAt: Date;
}

// SEO Types
export interface LocalSEOKeyword {
  id: string;
  keyword: string;
  volume: number;
  currentRank: number;
  previousRank: number;
  targetRank: number;
  country: string;
  url?: string;
  trackedSince: Date;
}

export interface LocalBacklink {
  id: string;
  sourceUrl: string;
  sourceDomain: string;
  domainAuthority: number;
  anchorText: string;
  follow: boolean;
  status: 'active' | 'lost';
  discoveredAt: Date;
  lostAt?: Date;
}

// Analytics Types
export interface CountryMetrics {
  period: {
    start: Date;
    end: Date;
  };
  country: {
    code: string;
    name: string;
  };
  summary: {
    budget: number;
    spent: number;
    revenue: number;
    leads: number;
    conversions: number;
    roi: number;
  };
  topCampaigns: Array<{
    id: string;
    name: string;
    revenue: number;
    roi: number;
    leads: number;
  }>;
}

// Report Types
export interface LocalReport {
  id: string;
  name: string;
  type: 'performance' | 'campaign_summary' | 'lead_report' | 'budget_report';
  format: 'PDF' | 'EXCEL' | 'CSV';
  generatedAt: Date;
  fileUrl?: string;
  createdBy: string;
  status: 'pending' | 'generating' | 'completed' | 'failed';
}

// Settings Types
export interface LocalSettings {
  userId: string;
  country: {
    code: string;
    name: string;
    currency: string;
  };
  preferences: {
    language: string;
    timezone: string;
    dateFormat: string;
  };
  notifications: {
    email: boolean;
    push: boolean;
    campaignAlerts: boolean;
    budgetAlerts: boolean;
    weeklyReport: boolean;
  };
  dashboard: {
    defaultView: string;
    hiddenWidgets: string[];
  };
}

// Pagination & API
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
  pagination?: PaginationInfo;
  timestamp: string;
}

// Filters
export interface LocalCampaignFilters {
  page?: number;
  pageSize?: number;
  search?: string;
  status?: LocalCampaignStatus[];
  type?: LocalCampaignType[];
  dateRange?: {
    start: Date;
    end: Date;
  };
}

export interface LocalLeadFilters {
  page?: number;
  pageSize?: number;
  search?: string;
  status?: string[];
  source?: string[];
  score?: string[];
  dateRange?: {
    start: Date;
    end: Date;
  };
}
