// Domain Entity: Analytics
// Represents marketing analytics data

export interface ChannelMetrics {
  channel: string;
  impressions: number;
  clicks: number;
  cost: number;
  conversions: number;
  revenue: number;
  ctr: number;
  cpc: number;
  cpa: number;
  roas: number;
}

export interface TimeSeriesDataPoint {
  date: Date;
  value: number;
  label?: string;
}

export interface CountryPerformance {
  countryCode: string;
  countryName: string;
  metrics: {
    impressions: number;
    clicks: number;
    cost: number;
    conversions: number;
    revenue: number;
    leads: number;
  };
  roi: number;
  trend: 'up' | 'down' | 'stable';
}

export interface CampaignAttribution {
  campaignId: string;
  campaignName: string;
  channel: string;
  touchpoints: number;
  attributedConversions: number;
  attributedRevenue: number;
  firstTouchConversions: number;
  lastTouchConversions: number;
  linearConversions: number;
}

export interface FunnelStage {
  stage: string;
  count: number;
  percentage: number;
  dropOffRate: number;
}

export interface ConversionFunnel {
  stages: FunnelStage[];
  overallConversionRate: number;
}

export interface Alert {
  id: string;
  type: 'warning' | 'error' | 'info' | 'success';
  category: 'budget' | 'campaign' | 'performance' | 'system';
  title: string;
  message: string;
  severity: 'low' | 'medium' | 'high' | 'critical';
  entityType: string;
  entityId: string;
  entityName: string;
  isRead: boolean;
  createdAt: Date;
  resolvedAt?: Date;
  actionUrl?: string;
}

export interface MarketingReport {
  id: string;
  name: string;
  type: 'executive' | 'campaign' | 'channel' | 'country' | 'custom';
  period: {
    start: Date;
    end: Date;
  };
  summary: {
    totalSpend: number;
    totalRevenue: number;
    totalLeads: number;
    totalConversions: number;
    overallROI: number;
  };
  sections: Array<{
    title: string;
    data: unknown;
  }>;
  createdBy: {
    id: string;
    name: string;
  };
  createdAt: Date;
  format: 'pdf' | 'excel' | 'csv' | 'html';
}
