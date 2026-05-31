# HQ GBM DASHBOARD - MOCK FLOW

**Version:** 1.0
**Domain:** Management Domain
**Subdomain:** Global-business-management
**Frontend:** gbm-web-dashboard
**Framework:** React 18 + Vite + TypeScript + Material UI v5.15+
**State Management:** Zustand
**Data Fetching:** TanStack Query
**Last Updated:** 2025-02-08

---

## TABLE OF CONTENTS

1. [API Architecture](#1-api-architecture)
2. [Data Models & Types](#2-data-models--types)
3. [API Endpoints](#3-api-endpoints)
4. [State Management](#4-state-management)
5. [Mock Data Examples](#5-mock-data-examples)
6. [Error Responses](#6-error-responses)

---

## 1. API ARCHITECTURE

### 1.1 Base URLs

```
Development:  https://gbm.hq.api.dev/v1
Staging:     https://gbm.hq.api.staging/v1
Production:  https://gbm.hq.api/v1
```

### 1.2 Authentication

All requests require authentication via Bearer token:

```typescript
const headers = {
  'Authorization': `Bearer ${accessToken}`,
  'Content-Type': 'application/json',
  'X-Market-Scope': getMarketScope(),
  'X-Request-ID': generateRequestId(),
};
```

### 1.3 Response Format

```typescript
interface ApiResponse<T> {
  success: boolean;
  data: T;
  message?: string;
  errors?: ValidationError[];
  pagination?: PaginationInfo;
  timestamp: string;
  requestId: string;
}
```

---

## 2. DATA MODELS & TYPES

### 2.1 Core Models

```typescript
// Global GBM Summary
interface GlobalGBMSummary {
  period: DateRange;
  totalMarkets: number;
  activeMarkets: number;
  emergingMarkets: number;
  activePartnerships: number;
  strategicInitiatives: number;
  businessDevelopment: BDMetrics;
  marketExpansion: MarketExpansionMetrics;
}

interface BDMetrics {
  totalOpportunities: number;
  pipelineValue: number;
  activeDeals: number;
  thisQuarter: {
    dealsOpened: number;
    dealsClosed: number;
    revenueGenerated: number;
  };
}

interface MarketExpansionMetrics {
  targetMarkets: number;
  enteredMarkets: number;
  researchInProgress: number;
  totalInvestment: number;
}
```

### 2.2 Partnership Models

```typescript
// Global Partnership
interface GlobalPartnership {
  id: string;
  name: string;
  type: PartnershipType;
  tier: PartnershipTier;
  countries: CountryInfo[];
  status: PartnershipStatus;
  startDate: Date;
  endDate?: Date;
  totalValue: number;
  currency: string;
  performance: PartnershipPerformance;
}

type PartnershipType =
  | 'STRATEGIC_ALLIANCE'
  | 'JOINT_VENTURE'
  | 'DISTRIBUTION'
  | 'RESELLER'
  | 'TECHNOLOGY'
  | 'CO_BRANDING';

type PartnershipTier = 'PLATINUM' | 'GOLD' | 'SILVER' | 'BRONZE';

type PartnershipStatus =
  | 'ACTIVE'
  | 'PENDING'
  | 'SUSPENDED'
  | 'TERMINATED'
  | 'RENEWAL_DUE';

interface PartnershipPerformance {
  revenueGenerated: number;
  revenueTarget: number;
  attainment: number;
  dealsCoSold: number;
  customerSatisfaction: number;
  trend: 'up' | 'down' | 'neutral';
}
```

### 2.3 Strategic Initiative Models

```typescript
// Strategic Initiative
interface StrategicInitiative {
  id: string;
  name: string;
  description: string;
  category: InitiativeCategory;
  priority: InitiativePriority;
  status: InitiativeStatus;
  owner: {
    id: string;
    name: string;
    role: string;
  };
  targetMarkets: string[];
  budget: number;
  spent: number;
  startDate: Date;
  targetEndDate: Date;
  progress: number;
  milestones: InitiativeMilestone[];
  kpis: InitiativeKPI[];
}

type InitiativeCategory =
  | 'MARKET_EXPANSION'
  | 'PRODUCT_LAUNCH'
  | 'PARTNERSHIP'
  | 'DIGITAL_TRANSFORMATION'
  | 'OPERATIONAL_EXCELLENCE';

type InitiativePriority = 'CRITICAL' | 'HIGH' | 'MEDIUM' | 'LOW';

type InitiativeStatus =
  | 'PROPOSED'
  | 'APPROVED'
  | 'IN_PROGRESS'
  | 'ON_HOLD'
  | 'COMPLETED'
  | 'CANCELLED';

interface InitiativeMilestone {
  id: string;
  name: string;
  targetDate: Date;
  actualDate?: Date;
  status: 'pending' | 'completed' | 'overdue';
}

interface InitiativeKPI {
  id: string;
  name: string;
  currentValue: number;
  targetValue: number;
  unit: string;
}
```

### 2.4 Business Development Models

```typescript
// Business Opportunity
interface BusinessOpportunity {
  id: string;
  title: string;
  type: OpportunityType;
  stage: OpportunityStage;
  priority: OpportunityPriority;
  market: string;
  country?: CountryInfo;
  estimatedValue: number;
  currency: string;
  probability: number;
  expectedCloseDate: Date;
  owner: {
    id: string;
    name: string;
  };
  description: string;
  nextSteps: string[];
  competitors: string[];
  createdAt: Date;
  updatedAt: Date;
}

type OpportunityType =
  | 'MARKET_ENTRY'
  | 'PARTNERSHIP'
  | 'ACQUISITION'
  | 'PRODUCT_EXPANSION'
  | 'STRATEGIC_INVESTMENT';

type OpportunityStage =
  | 'IDENTIFICATION'
  | 'QUALIFICATION'
  | 'ANALYSIS'
  | 'PROPOSAL'
  | 'NEGOTIATION'
  | 'CLOSING'
  | 'WON'
  | 'LOST';

type OpportunityPriority = 'CRITICAL' | 'HIGH' | 'MEDIUM' | 'LOW';
```

### 2.5 Market Analysis Models

```typescript
// Market Analysis
interface MarketAnalysis {
  market: string;
  country: CountryInfo;
  marketSize: number;
  growthRate: number;
  competitiveness: number;
  entryBarrier: EntryBarrier;
  opportunityScore: number;
  targetSegments: MarketSegment[];
  competitorAnalysis: CompetitorInfo[];
  swotAnalysis: SWOTAnalysis;
  recommendation: string;
}

type EntryBarrier =
  | 'LOW'
  | 'MEDIUM'
  | 'HIGH'
  | 'VERY_HIGH';

interface MarketSegment {
  name: string;
  size: number;
  growthRate: number;
  accessibility: number;
}

interface CompetitorInfo {
  name: string;
  marketShare: number;
  strengths: string[];
  weaknesses: string[];
}

interface SWOTAnalysis {
  strengths: string[];
  weaknesses: string[];
  opportunities: string[];
  threats: string[];
}
```

---

## 3. API ENDPOINTS

### 3.1 Global Overview Endpoints

```typescript
// GET /gbm/dashboard/global
interface GlobalDashboardEndpoint {
  GET: {
    query: {
      period?: 'today' | 'week' | 'month' | 'quarter' | 'year';
      startDate?: Date;
      endDate?: Date;
    };
    response: GlobalGBMSummary;
  };
}

// GET /gbm/markets/summary
interface MarketsSummaryEndpoint {
  GET: {
    query: {
      region?: string;
    };
    response: MarketSummary[];
  };
}
```

### 3.2 Partnership Endpoints

```typescript
// GET /gbm/partnerships
interface PartnershipsListEndpoint {
  GET: {
    query: {
      type?: PartnershipType;
      tier?: PartnershipTier;
      status?: PartnershipStatus;
      countries?: string[];
    };
    response: GlobalPartnership[];
  };
}

// GET /gbm/partnerships/:id
interface PartnershipDetailEndpoint {
  GET: {
    params: { id: string };
    response: GlobalPartnership;
  };
}

// POST /gbm/partnerships
interface CreatePartnershipEndpoint {
  POST: {
    request: Partial<GlobalPartnership>;
    response: GlobalPartnership;
  };
}

// PUT /gbm/partnerships/:id
interface UpdatePartnershipEndpoint {
  PUT: {
    params: { id: string };
    request: Partial<GlobalPartnership>;
    response: GlobalPartnership;
  };
}
```

### 3.3 Strategic Initiative Endpoints

```typescript
// GET /gbm/initiatives
interface InitiativesListEndpoint {
  GET: {
    query: {
      category?: InitiativeCategory;
      status?: InitiativeStatus;
      priority?: InitiativePriority;
    };
    response: StrategicInitiative[];
  };
}

// GET /gbm/initiatives/:id
interface InitiativeDetailEndpoint {
  GET: {
    params: { id: string };
    response: StrategicInitiative;
  };
}

// POST /gbm/initiatives
interface CreateInitiativeEndpoint {
  POST: {
    request: Partial<StrategicInitiative>;
    response: StrategicInitiative;
  };
}

// PUT /gbm/initiatives/:id
interface UpdateInitiativeEndpoint {
  PUT: {
    params: { id: string };
    request: Partial<StrategicInitiative>;
    response: StrategicInitiative;
  };
}

// POST /gbm/initiatives/:id/milestones
interface AddMilestoneEndpoint {
  POST: {
    params: { id: string };
    request: InitiativeMilestone;
    response: InitiativeMilestone;
  };
}
```

### 3.4 Business Development Endpoints

```typescript
// GET /gbm/opportunities
interface OpportunitiesListEndpoint {
  GET: {
    query: {
      type?: OpportunityType;
      stage?: OpportunityStage;
      priority?: OpportunityPriority;
      market?: string;
      country?: string;
    };
    response: BusinessOpportunity[];
  };
}

// GET /gbm/opportunities/:id
interface OpportunityDetailEndpoint {
  GET: {
    params: { id: string };
    response: BusinessOpportunity;
  };
}

// POST /gbm/opportunities
interface CreateOpportunityEndpoint {
  POST: {
    request: Partial<BusinessOpportunity>;
    response: BusinessOpportunity;
  };
}

// PUT /gbm/opportunities/:id
interface UpdateOpportunityEndpoint {
  PUT: {
    params: { id: string };
    request: Partial<BusinessOpportunity>;
    response: BusinessOpportunity;
  };
}

// POST /gbm/opportunities/:id/convert
interface ConvertOpportunityEndpoint {
  POST: {
    params: { id: string };
    request: {
      convertTo: 'partnership' | 'initiative' | 'project';
    };
    response: { success: boolean; newId: string };
  };
}
```

### 3.5 Market Analysis Endpoints

```typescript
// GET /gbm/markets/analysis
interface MarketAnalysisEndpoint {
  GET: {
    query: {
      market: string;
      country?: string;
    };
    response: MarketAnalysis;
  };
}

// GET /gbm/markets/compare
interface MarketCompareEndpoint {
  GET: {
    query: {
      markets: string[]; // ['Nigeria', 'Kenya', 'Ghana']
    };
    response: MarketComparison;
  };
}
```

---

## 4. STATE MANAGEMENT

### 4.1 Store Structure (Zustand)

```typescript
interface HQGBMDashboardStore {
  // Auth state
  auth: {
    user: HQGBMUser | null;
    accessToken: string | null;
    isAuthenticated: boolean;
    permissions: Permission[];
  };

  // Global dashboard state
  globalDashboard: {
    summary: GlobalGBMSummary | null;
    loading: boolean;
    error: string | null;
    selectedPeriod: string;
  };

  // Partnerships state
  partnerships: {
    items: GlobalPartnership[];
    selected: GlobalPartnership | null;
    filters: PartnershipFilters;
    loading: boolean;
    error: string | null;
  };

  // Initiatives state
  initiatives: {
    items: StrategicInitiative[];
    selected: StrategicInitiative | null;
    filters: InitiativeFilters;
    loading: boolean;
    error: string | null;
  };

  // Opportunities state
  opportunities: {
    items: BusinessOpportunity[];
    selected: BusinessOpportunity | null;
    pipeline: OpportunityPipeline;
    loading: boolean;
    error: string | null;
  };

  // Markets state
  markets: {
    analyses: Record<string, MarketAnalysis>;
    comparisons: MarketComparison | null;
    loading: boolean;
    error: string | null;
  };

  // UI state
  ui: {
    sidebarOpen: boolean;
    selectedMarkets: string[];
    selectedPeriod: string;
    theme: 'light' | 'dark';
  };
}
```

### 4.2 React Query Keys

```typescript
const queryKeys = {
  // Global dashboard
  globalDashboard: (period: string) => ['gbm', 'dashboard', period] as const,

  // Partnerships
  partnerships: (filters?: PartnershipFilters) => ['gbm', 'partnerships', filters] as const,
  partnership: (id: string) => ['gbm', 'partnership', id] as const,

  // Initiatives
  initiatives: (filters?: InitiativeFilters) => ['gbm', 'initiatives', filters] as const,
  initiative: (id: string) => ['gbm', 'initiative', id] as const,

  // Opportunities
  opportunities: (filters?: OpportunityFilters) => ['gbm', 'opportunities', filters] as const,
  opportunity: (id: string) => ['gbm', 'opportunity', id] as const,

  // Markets
  marketAnalysis: (market: string) => ['gbm', 'market', market] as const,
  marketComparison: (markets: string[]) => ['gbm', 'markets', 'compare', markets] as const,
};
```

---

## 5. MOCK DATA EXAMPLES

### 5.1 Mock Global GBM Summary

```json
{
  "period": {
    "start": "2025-02-01T00:00:00Z",
    "end": "2025-02-08T23:59:59Z",
    "type": "month"
  },
  "totalMarkets": 12,
  "activeMarkets": 8,
  "emergingMarkets": 4,
  "activePartnerships": 45,
  "strategicInitiatives": 18,
  "businessDevelopment": {
    "totalOpportunities": 128,
    "pipelineValue": 45000000,
    "activeDeals": 67,
    "thisQuarter": {
      "dealsOpened": 24,
      "dealsClosed": 12,
      "revenueGenerated": 8500000
    }
  },
  "marketExpansion": {
    "targetMarkets": 6,
    "enteredMarkets": 2,
    "researchInProgress": 3,
    "totalInvestment": 12000000
  }
}
```

### 5.2 Mock Partnerships

```json
[
  {
    "id": "prt-001",
    "name": "TechCorp Global Alliance",
    "type": "STRATEGIC_ALLIANCE",
    "tier": "PLATINUM",
    "countries": [
      { "code": "NG", "name": "Nigeria", "flag": "🇳🇬" },
      { "code": "KE", "name": "Kenya", "flag": "🇰🇪" },
      { "code": "ZA", "name": "South Africa", "flag": "🇿🇦" }
    ],
    "status": "ACTIVE",
    "startDate": "2024-01-15T00:00:00Z",
    "endDate": "2027-01-15T00:00:00Z",
    "totalValue": 5000000,
    "currency": "USD",
    "performance": {
      "revenueGenerated": 2150000,
      "revenueTarget": 2000000,
      "attainment": 107.5,
      "dealsCoSold": 45,
      "customerSatisfaction": 4.5,
      "trend": "up"
    }
  },
  {
    "id": "prt-002",
    "name": "LocalConnect Distribution",
    "type": "DISTRIBUTION",
    "tier": "GOLD",
    "countries": [
      { "code": "GH", "name": "Ghana", "flag": "🇬🇭" },
      { "code": "CI", "name": "Ivory Coast", "flag": "🇨🇮" }
    ],
    "status": "ACTIVE",
    "startDate": "2024-06-01T00:00:00Z",
    "totalValue": 1200000,
    "currency": "USD",
    "performance": {
      "revenueGenerated": 780000,
      "revenueTarget": 800000,
      "attainment": 97.5,
      "dealsCoSold": 28,
      "customerSatisfaction": 4.2,
      "trend": "neutral"
    }
  }
]
```

### 5.3 Mock Strategic Initiatives

```json
[
  {
    "id": "ini-001",
    "name": "West Africa Market Expansion 2025",
    "description": "Expand operations into 3 new West African markets",
    "category": "MARKET_EXPANSION",
    "priority": "HIGH",
    "status": "IN_PROGRESS",
    "owner": {
      "id": "usr-001",
      "name": "John Doe",
      "role": "VP Business Development"
    },
    "targetMarkets": ["Senegal", "Cameroon", "Benin"],
    "budget": 2500000,
    "spent": 1250000,
    "startDate": "2025-01-01T00:00:00Z",
    "targetEndDate": "2025-12-31T23:59:59Z",
    "progress": 50,
    "milestones": [
      {
        "id": "mil-001",
        "name": "Market Research Complete",
        "targetDate": "2025-03-31T00:00:00Z",
        "actualDate": "2025-03-15T00:00:00Z",
        "status": "completed"
      },
      {
        "id": "mil-002",
        "name": "Local Team Hired",
        "targetDate": "2025-06-30T00:00:00Z",
        "status": "pending"
      }
    ],
    "kpis": [
      {
        "id": "kpi-001",
        "name": "Markets Entered",
        "currentValue": 1,
        "targetValue": 3,
        "unit": "markets"
      },
      {
        "id": "kpi-002",
        "name": "Revenue Generated",
        "currentValue": 250000,
        "targetValue": 1000000,
        "unit": "USD"
      }
    ]
  }
]
```

### 5.4 Mock Business Opportunities

```json
[
  {
    "id": "opp-001",
    "title": "East Africa Telecom Partnership",
    "type": "PARTNERSHIP",
    "stage": "PROPOSAL",
    "priority": "HIGH",
    "market": "Telecommunications",
    "country": {
      "code": "TZ",
      "name": "Tanzania",
      "flag": "🇹🇿"
    },
    "estimatedValue": 2500000,
    "currency": "USD",
    "probability": 60,
    "expectedCloseDate": "2025-04-30T00:00:00Z",
    "owner": {
      "id": "usr-002",
      "name": "Jane Smith"
    },
    "description": "Strategic partnership with leading telecom provider for distribution services",
    "nextSteps": [
      "Submit final proposal",
      "Schedule executive meeting",
      "Prepare legal documentation"
    ],
    "competitors": ["Competitor A", "Competitor B"],
    "createdAt": "2025-01-15T00:00:00Z",
    "updatedAt": "2025-02-05T00:00:00Z"
  },
  {
    "id": "opp-002",
    "title": "Fintech Market Entry - Ethiopia",
    "type": "MARKET_ENTRY",
    "stage": "ANALYSIS",
    "priority": "CRITICAL",
    "market": "Financial Services",
    "country": {
      "code": "ET",
      "name": "Ethiopia",
      "flag": "🇪🇹"
    },
    "estimatedValue": 5000000,
    "currency": "USD",
    "probability": 35,
    "expectedCloseDate": "2025-06-30T00:00:00Z",
    "owner": {
      "id": "usr-001",
      "name": "John Doe"
    },
    "description": "Market entry opportunity following regulatory liberalization",
    "nextSteps": [
      "Complete regulatory analysis",
      "Identify local partners",
      "Feasibility study"
    ],
    "competitors": ["Regional Bank X", "International Fintech Y"],
    "createdAt": "2025-02-01T00:00:00Z",
    "updatedAt": "2025-02-07T00:00:00Z"
  }
]
```

---

## 6. ERROR RESPONSES

### 6.1 Error Response Format

```typescript
interface ErrorResponse {
  success: false;
  error: {
    code: string;
    message: string;
    details?: any;
    timestamp: string;
    requestId: string;
  };
}
```

### 6.2 Common Error Codes

| Error Code | HTTP Status | Description |
|------------|-------------|-------------|
| GBM_AUTH_001 | 401 | Invalid or expired token |
| GBM_AUTH_002 | 403 | Insufficient permissions for market |
| GBM_VAL_001 | 400 | Invalid partnership data |
| GBM_VAL_002 | 400 | Invalid initiative dates |
| GBM_RES_001 | 404 | Partnership not found |
| GBM_RES_002 | 404 | Initiative not found |
| GBM_RES_003 | 404 | Opportunity not found |
| GBM_BIZ_001 | 400 | Cannot delete active partnership |
| GBM_BIZ_002 | 400 | Initiative budget exceeded |
| GBM_SRV_001 | 500 | Internal server error |
| GBM_SRV_002 | 503 | Market data unavailable |

---

## VERSION HISTORY

| Version | Date | Changes |
|---------|------|---------|
| 1.0 | 2025-02-08 | Initial HQ GBM Dashboard Mock Flow Documentation |

---

**Document End**
