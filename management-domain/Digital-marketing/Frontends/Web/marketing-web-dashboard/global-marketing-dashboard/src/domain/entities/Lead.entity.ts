// Domain Entity: Lead
// Represents a lead generated from marketing activities

export enum LeadStatus {
  NEW = 'new',
  CONTACTED = 'contacted',
  QUALIFIED = 'qualified',
  PROPOSAL = 'proposal',
  NEGOTIATION = 'negotiation',
  WON = 'won',
  LOST = 'lost'
}

export enum LeadSource {
  WEBSITE = 'website',
  EMAIL = 'email',
  SOCIAL_MEDIA = 'social_media',
  SEARCH_ENGINE = 'search_engine',
  REFERRAL = 'referral',
  EVENT = 'event',
  ADVERTISEMENT = 'advertisement',
  CONTENT = 'content',
  OTHER = 'other'
}

export enum LeadQuality {
  HOT = 'hot',
  WARM = 'warm',
  COLD = 'cold',
  UNQUALIFIED = 'unqualified'
}

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
}

export interface LeadMetrics {
  total: number;
  new: number;
  qualified: number;
  converted: number;
  conversionRate: number;
  averageValue: number;
  totalValue: number;
  bySource: Record<LeadSource, number>;
  byCountry: Record<string, number>;
  byCampaign: Record<string, number>;
}
