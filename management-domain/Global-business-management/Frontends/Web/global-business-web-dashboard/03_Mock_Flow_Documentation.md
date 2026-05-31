# GLOBAL BUSINESS DASHBOARD - MOCK FLOW

**Version:** 1.0
**Last Updated:** 2026-02-23

---

## API ENDPOINTS

```
GET  /gbm/dashboard/overview
GET  /gbm/regions/performance
GET  /gbm/regions/{regionId}/details
GET  /gbm/partners/list
GET  /gbm/partners/{partnerId}/performance
POST /gbm/partners/register
GET  /gbm/pipeline/opportunities
POST /gbm/pipeline/opportunities
PUT  /gbm/pipeline/opportunities/{id}/stage
GET  /gbm/analytics/market-intelligence
```

---

## DATA MODELS

```typescript
interface GBMUser {
  id: string;
  email: string;
  firstName: string;
  lastName: string;
  title: string;
  role: 'GLOBAL_BUSINESS_DIRECTOR' | 'REGIONAL_DIRECTOR' | 'BUSINESS_DEVELOPMENT_MANAGER' | 'PARTNER_MANAGER' | 'GBM_ANALYST';
  regionAccess?: string[];
}

interface RegionPerformance {
  regionId: string;
  regionName: string;
  regionCode: string;
  revenue: number;
  growth: number;
  targetAchievement: number;
  status: 'on_track' | 'caution' | 'off_track';
  countries: CountryPerformance[];
  activePartners: number;
  pipelineValue: number;
  teamSize: number;
}

interface CountryPerformance {
  countryCode: string;
  countryName: string;
  revenue: number;
  growth: number;
  targetAchievement: number;
  status: 'on_track' | 'caution' | 'off_track';
}

interface Partner {
  id: string;
  name: string;
  type: 'CHANNEL' | 'STRATEGIC' | 'DISTRIBUTION' | 'RESELLER';
  tier: 'PLATINUM' | 'GOLD' | 'SILVER' | 'BRONZE';
  region: string;
  revenueYTD: number;
  status: 'ACTIVE' | 'INACTIVE' | 'ONBOARDING';
  joinedDate: Date;
  performanceScore: number;
  dealsClosed: number;
  pipelineValue: number;
  contactInfo: PartnerContact;
}

interface Opportunity {
  id: string;
  title: string;
  accountName: string;
  type: 'ENTERPRISE' | 'PARTNERSHIP' | 'CHANNEL' | 'STRATEGIC';
  value: number;
  stage: 'PROSPECTING' | 'QUALIFYING' | 'PROPOSAL' | 'NEGOTIATING' | 'CLOSING' | 'WON' | 'LOST';
  probability: number;
  region: string;
  owner: string;
  ownerId: string;
  dueDate: Date;
  lastActivity: Date;
  nextAction: string;
}

interface MarketIntelligence {
  id: string;
  type: 'COMPETITOR' | 'MARKET_TREND' | 'OPPORTUNITY' | 'THREAT';
  title: string;
  description: string;
  region: string;
  severity: 'INFO' | 'WARNING' | 'OPPORTUNITY';
  source: string;
  createdAt: Date;
}
```

---

## MOCK DATA

### Overview Mock

```typescript
const mockGBMOverview = {
  businessHealth: 85,
  healthTrend: 5,
  regions: [
    {
      regionId: 'reg_europe',
      regionName: 'Europe',
      regionCode: 'EU',
      revenue: 8200000,
      growth: 15,
      targetAchievement: 92,
      status: 'on_track',
      activePartners: 45,
      pipelineValue: 3500000,
      teamSize: 23
    },
    {
      regionId: 'reg_africa',
      regionName: 'Africa',
      regionCode: 'AF',
      revenue: 3500000,
      growth: 22,
      targetAchievement: 78,
      status: 'caution',
      activePartners: 32,
      pipelineValue: 2800000,
      teamSize: 18
    },
    {
      regionId: 'reg_americas',
      regionName: 'Americas',
      regionCode: 'AM',
      revenue: 4100000,
      growth: 8,
      targetAchievement: 88,
      status: 'on_track',
      activePartners: 28,
      pipelineValue: 3200000,
      teamSize: 15
    },
    {
      regionId: 'reg_asia_pacific',
      regionName: 'Asia Pacific',
      regionCode: 'APAC',
      revenue: 2800000,
      growth: 18,
      targetAchievement: 85,
      status: 'on_track',
      activePartners: 23,
      pipelineValue: 3050000,
      teamSize: 12
    }
  ],
  pipeline: {
    prospecting: 12,
    qualifying: 18,
    proposal: 15,
    negotiating: 8,
    closing: 5,
    totalValue: 12500000
  },
  partners: {
    total: 128,
    active: 112,
    onboarding: 16,
    newThisMonth: 5
  },
  deals: {
    closed: 23,
    value: 4200000,
    upFromLastMonth: 3
  },
  targetAchievement: 82
};
```

### Partners Mock

```typescript
const mockPartners: Partner[] = [
  {
    id: 'prt_001',
    name: 'TechVentures Global',
    type: 'STRATEGIC',
    tier: 'PLATINUM',
    region: 'Europe',
    revenueYTD: 2500000,
    status: 'ACTIVE',
    joinedDate: new Date('2019-03-15T00:00:00Z'),
    performanceScore: 92,
    dealsClosed: 12,
    pipelineValue: 850000,
    contactInfo: {
      primary: 'James Wilson',
      email: 'j.wilson@techventures.com',
      phone: '+44 20 1234 5678'
    }
  },
  {
    id: 'prt_002',
    name: 'African Business Partners',
    type: 'DISTRIBUTION',
    tier: 'GOLD',
    region: 'Africa',
    revenueYTD: 1200000,
    status: 'ACTIVE',
    joinedDate: new Date('2021-06-01T00:00:00Z'),
    performanceScore: 78,
    dealsClosed: 8,
    pipelineValue: 450000,
    contactInfo: {
      primary: 'Amara Okafor',
      email: 'a.okafor@abp-africa.com',
      phone: '+234 1 234 5678'
    }
  },
  {
    id: 'prt_003',
    name: 'Asia Pacific Connections',
    type: 'CHANNEL',
    tier: 'SILVER',
    region: 'Asia Pacific',
    revenueYTD: 800000,
    status: 'ACTIVE',
    joinedDate: new Date('2022-01-15T00:00:00Z'),
    performanceScore: 72,
    dealsClosed: 5,
    pipelineValue: 320000,
    contactInfo: {
      primary: 'Lin Wei',
      email: 'lin.wei@apc-partners.com',
      phone: '+86 21 1234 5678'
    }
  }
];
```

### Pipeline Mock

```typescript
const mockOpportunities: Opportunity[] = [
  {
    id: 'opp_001',
    title: 'Enterprise Solutions - TechCorp Europe',
    accountName: 'TechCorp Europe Ltd',
    type: 'ENTERPRISE',
    value: 500000,
    stage: 'QUALIFYING',
    probability: 60,
    region: 'Europe',
    owner: 'Sarah Johnson',
    ownerId: 'usr_sarah_j',
    dueDate: new Date('2026-03-31T00:00:00Z'),
    lastActivity: new Date('2026-02-21T14:30:00Z'),
    nextAction: 'Schedule product demo'
  },
  {
    id: 'opp_002',
    title: 'Strategic Partnership - African Logistics Alliance',
    accountName: 'African Logistics Alliance',
    type: 'PARTNERSHIP',
    value: 250000,
    stage: 'PROPOSAL',
    probability: 75,
    region: 'Africa',
    owner: 'Michael Chen',
    ownerId: 'usr_mike_c',
    dueDate: new Date('2026-02-28T00:00:00Z'),
    lastActivity: new Date('2026-02-22T10:15:00Z'),
    nextAction: 'Follow up on contract review'
  },
  {
    id: 'opp_003',
    title: 'Channel Distribution - APAC Network',
    accountName: 'APAC Distribution Network Pte Ltd',
    type: 'CHANNEL',
    value: 180000,
    stage: 'CLOSING',
    probability: 90,
    region: 'Asia Pacific',
    owner: 'Lisa Wang',
    ownerId: 'usr_lisa_w',
    dueDate: new Date('2026-02-23T00:00:00Z'),
    lastActivity: new Date('2026-02-23T09:00:00Z'),
    nextAction: 'Final signature'
  }
];
```

### Market Intelligence Mock

```typescript
const mockMarketIntelligence: MarketIntelligence[] = [
  {
    id: 'intel_001',
    type: 'COMPETITOR',
    title: 'Competitor expanding in Nigeria',
    description: 'Major competitor TechGlobal Inc. announced plans to enter Nigerian market with $5M investment',
    region: 'Africa',
    severity: 'WARNING',
    source: 'Market Research Team',
    createdAt: new Date('2026-02-23T08:30:00Z')
  },
  {
    id: 'intel_002',
    type: 'OPPORTUNITY',
    title: 'Government digital transformation initiative',
    description: 'Kenya government launching $50M digital transformation program - potential for enterprise solutions',
    region: 'Africa',
    severity: 'OPPORTUNITY',
    source: 'Partner Network',
    createdAt: new Date('2026-02-22T16:45:00Z')
  },
  {
    id: 'intel_003',
    type: 'MARKET_TREND',
    title: 'Cloud adoption accelerating in Europe',
    description: 'Enterprise cloud adoption in Europe grew 35% YoY - opportunities for migration services',
    region: 'Europe',
    severity: 'INFO',
    source: 'Industry Analysis',
    createdAt: new Date('2026-02-21T11:20:00Z')
  }
];
```

---

## STATE MANAGEMENT

### Zustand Store Structure

```typescript
interface GBMStore {
  // State
  user: GBMUser | null;
  overview: GBMOverview | null;
  regions: RegionPerformance[];
  selectedRegion: string | null;
  partners: Partner[];
  pipeline: Opportunity[];
  opportunities: Opportunity[];
  intelligence: MarketIntelligence[];

  // Actions
  setUser: (user: GBMUser) => void;
  loadOverview: () => Promise<void>;
  loadRegions: () => Promise<void>;
  selectRegion: (regionId: string) => void;
  loadPartners: () => Promise<void>;
  loadPipeline: () => Promise<void>;
  createOpportunity: (opportunity: Partial<Opportunity>) => Promise<void>;
  updateOpportunityStage: (id: string, stage: string) => Promise<void>;
  loadIntelligence: () => Promise<void>;
  refreshData: () => Promise<void>;
}
```

---

## WEBSOCKET EVENTS

### Incoming Events

```typescript
// New opportunity created
interface OpportunityCreatedEvent {
  type: 'opportunity.created';
  data: Opportunity;
}

// Deal stage changed
interface StageChangedEvent {
  type: 'opportunity.stage_changed';
  data: {
    opportunityId: string;
    oldStage: string;
    newStage: string;
    changedBy: string;
  };
}

// Partner registered
interface PartnerRegisteredEvent {
  type: 'partner.registered';
  data: Partner;
}

// Target achieved
interface TargetAchievedEvent {
  type: 'region.target_achieved';
  data: {
    regionId: string;
    target: string;
    achievement: number;
  };
}

// New market intelligence
interface IntelligenceEvent {
  type: 'intelligence.created';
  data: MarketIntelligence;
}
```

---

## ERROR HANDLING

### Standard Error Format

```typescript
interface GBMApiError {
  success: false;
  error: {
    code: string;
    message: string;
    details?: any;
    timestamp: Date;
    requestId: string;
  };
}
```

### Common Error Codes

| Code | HTTP Status | Description | Retryable |
|------|-------------|-------------|-----------|
| `GBM_001` | 401 | Invalid authentication token | No |
| `GBM_002` | 403 | Insufficient region access | No |
| `GBM_003` | 404 | Partner not found | No |
| `GBM_004` | 400 | Invalid opportunity data | No |
| `GBM_005` | 500 | Regional service unavailable | Yes |
| `GBM_006` | 503 | Partner service down | Yes |

---

## VERSION HISTORY

| Version | Date | Changes |
|---------|------|---------|
| 1.0 | 2026-02-23 | Initial Mock Flow Documentation |

---

**Document End**
