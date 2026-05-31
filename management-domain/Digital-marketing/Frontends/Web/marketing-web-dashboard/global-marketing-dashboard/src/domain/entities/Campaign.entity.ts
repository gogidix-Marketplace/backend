// Domain Entity: Campaign
// Represents a marketing campaign in the global marketing domain

export enum CampaignStatus {
  DRAFT = 'draft',
  SCHEDULED = 'scheduled',
  ACTIVE = 'active',
  PAUSED = 'paused',
  COMPLETED = 'completed',
  CANCELLED = 'cancelled'
}

export enum CampaignType {
  AWARENESS = 'awareness',
  CONSIDERATION = 'consideration',
  CONVERSION = 'conversion',
  RETENTION = 'retention'
}

export enum ChannelType {
  EMAIL = 'email',
  SOCIAL = 'social',
  SEARCH = 'search',
  DISPLAY = 'display',
  VIDEO = 'video',
  DIRECT_MAIL = 'direct_mail',
  EVENTS = 'events',
  OTHER = 'other'
}

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
  ctr: number; // Click-through rate
  cpc: number; // Cost per click
  cpa: number; // Cost per acquisition
  roas: number; // Return on ad spend
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
