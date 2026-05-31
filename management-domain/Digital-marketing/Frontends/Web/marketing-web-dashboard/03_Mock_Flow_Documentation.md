# HQ MARKETING DASHBOARD - MOCK FLOW

**Version:** 1.0
**Last Updated:** 2025-02-08

---

## API ENDPOINTS

```
GET /marketing/dashboard/global
GET /marketing/countries/compare
GET /marketing/campaigns/performance
```

---

## DATA MODELS

```typescript
interface GlobalMarketingSummary {
  totalCampaigns: number;
  activeCampaigns: number;
  totalBudget: number;
  totalSpent: number;
  avgROI: number;
  countriesSummary: CountryMarketingSummary[];
}

interface CountryMarketingSummary {
  countryCode: string;
  countryName: string;
  campaigns: number;
  budget: number;
  spent: number;
  roi: number;
  conversionRate: number;
}

interface MarketingCampaign {
  id: string;
  name: string;
  type: 'DIGITAL' | 'PRINT' | 'EVENT' | 'SOCIAL' | 'EMAIL';
  status: 'DRAFT' | 'SCHEDULED' | 'ACTIVE' | 'PAUSED' | 'COMPLETED';
  countries: string[];
  budget: number;
  spent: number;
  startDate: Date;
  endDate: Date;
  roi?: number;
}
```

---

## MOCK DATA

### Global Marketing Summary Mock

```typescript
const mockGlobalMarketingSummary: GlobalMarketingSummary = {
  totalCampaigns: 145,
  activeCampaigns: 67,
  totalBudget: 12500000,
  totalSpent: 7840000,
  avgROI: 3.2,
  countriesSummary: [
    {
      countryCode: 'US',
      countryName: 'United States',
      campaigns: 32,
      budget: 3500000,
      spent: 2100000,
      roi: 3.5,
      conversionRate: 2.8
    },
    {
      countryCode: 'UK',
      countryName: 'United Kingdom',
      campaigns: 24,
      budget: 2200000,
      spent: 1450000,
      roi: 3.1,
      conversionRate: 2.5
    },
    {
      countryCode: 'DE',
      countryName: 'Germany',
      campaigns: 18,
      budget: 1800000,
      spent: 1120000,
      roi: 2.9,
      conversionRate: 2.3
    },
    {
      countryCode: 'FR',
      countryName: 'France',
      campaigns: 15,
      budget: 1500000,
      spent: 980000,
      roi: 2.8,
      conversionRate: 2.2
    },
    {
      countryCode: 'JP',
      countryName: 'Japan',
      campaigns: 20,
      budget: 2000000,
      spent: 1350000,
      roi: 3.4,
      conversionRate: 2.7
    },
    {
      countryCode: 'AU',
      countryName: 'Australia',
      campaigns: 12,
      budget: 800000,
      spent: 520000,
      roi: 3.0,
      conversionRate: 2.4
    },
    {
      countryCode: 'CA',
      countryName: 'Canada',
      campaigns: 14,
      budget: 700000,
      spent: 316000,
      roi: 2.7,
      conversionRate: 2.1
    }
  ]
};
```

### Campaign Performance Mock

```typescript
const mockCampaignPerformance: MarketingCampaign[] = [
  {
    id: 'camp_global_001',
    name: 'Q1 Global Brand Awareness',
    type: 'DIGITAL',
    status: 'ACTIVE',
    countries: ['US', 'UK', 'DE', 'FR', 'JP'],
    budget: 2000000,
    spent: 1200000,
    startDate: new Date('2025-01-01'),
    endDate: new Date('2025-03-31'),
    roi: 3.2
  },
  {
    id: 'camp_social_001',
    name: 'Social Media Engagement Q1',
    type: 'SOCIAL',
    status: 'ACTIVE',
    countries: ['US', 'CA', 'AU'],
    budget: 500000,
    spent: 320000,
    startDate: new Date('2025-01-15'),
    endDate: new Date('2025-04-15'),
    roi: 4.1
  },
  {
    id: 'camp_email_001',
    name: 'Newsletter Campaign - February',
    type: 'EMAIL',
    status: 'COMPLETED',
    countries: ['DE', 'FR', 'UK'],
    budget: 150000,
    spent: 145000,
    startDate: new Date('2025-02-01'),
    endDate: new Date('2025-02-28'),
    roi: 5.2
  },
  {
    id: 'camp_event_001',
    name: 'Spring Launch Events',
    type: 'EVENT',
    status: 'SCHEDULED',
    countries: ['US', 'JP'],
    budget: 800000,
    spent: 0,
    startDate: new Date('2025-03-15'),
    endDate: new Date('2025-03-30'),
    roi: undefined
  }
];
```

### Analytics Mock Data

```typescript
const mockMarketingAnalytics = {
  period: 'Q1 2025',
  metrics: {
    impressions: 45000000,
    clicks: 1800000,
    conversions: 45000,
    ctr: 4.0, // Click-through rate
    cpc: 2.15, // Cost per click
    cpa: 86.22, // Cost per acquisition
    roas: 3.2 // Return on ad spend
  },
  byChannel: [
    { channel: 'Google Ads', budget: 2500000, spent: 1650000, roi: 3.5, conversions: 18500 },
    { channel: 'Facebook', budget: 1800000, spent: 1200000, roi: 3.8, conversions: 14200 },
    { channel: 'LinkedIn', budget: 1200000, spent: 780000, roi: 2.9, conversions: 5200 },
    { channel: 'Instagram', budget: 1500000, spent: 980000, roi: 4.2, conversions: 11800 },
    { channel: 'Email', budget: 400000, spent: 350000, roi: 5.1, conversions: 4800 }
  ],
  trends: [
    { date: '2025-01-01', spend: 85000, conversions: 1450 },
    { date: '2025-01-08', spend: 92000, conversions: 1680 },
    { date: '2025-01-15', spend: 88000, conversions: 1520 },
    { date: '2025-01-22', spend: 95000, conversions: 1750 },
    { date: '2025-01-29', spend: 105000, conversions: 1920 },
    { date: '2025-02-05', spend: 98000, conversions: 1810 },
    { date: '2025-02-08', spend: 102000, conversions: 1890 }
  ]
};
```

---

**Document End**
