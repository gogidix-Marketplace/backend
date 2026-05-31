# HQ SUPPORT DASHBOARD - MOCK FLOW

**Version:** 1.0
**Domain:** Management Domain
**Subdomain:** Customer-support
**Frontend:** support-web-dashboard
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
Development:  https://support.hq.api.dev/v1
Staging:     https://support.hq.api.staging/v1
Production:  https://support.hq.api/v1
```

### 1.2 Authentication

All requests require authentication via Bearer token:

```typescript
const headers = {
  'Authorization': `Bearer ${accessToken}`,
  'Content-Type': 'application/json',
  'X-Country-Scope': getCountryScope(),
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
// Global Support Summary
interface GlobalSupportSummary {
  period: DateRange;
  globalMetrics: {
    totalTickets: number;
    openTickets: number;
    resolvedTickets: number;
    escalatedTickets: number;
    avgResolutionTime: number;
    customerSatisfaction: number;
    slaCompliance: number;
  };
  countriesSummary: CountrySupportSummary[];
  teamPerformance: TeamPerformanceMetrics;
  channelMetrics: ChannelMetrics;
}

interface CountrySupportSummary {
  country: CountryInfo;
  metrics: {
    openTickets: number;
    resolvedToday: number;
    avgResolutionTime: number;
    satisfaction: number;
    slaCompliance: number;
  };
  trend: 'up' | 'down' | 'neutral';
  status: 'excellent' | 'good' | 'fair' | 'poor';
}
```

### 2.2 Ticket Models

```typescript
// Support Ticket
interface GlobalSupportTicket {
  id: string;
  ticketNumber: string;
  subject: string;
  description: string;
  status: TicketStatus;
  priority: TicketPriority;
  category: SupportCategory;
  channel: TicketChannel;
  country: CountryInfo;
  customer: CustomerInfo;
  assignedAgent?: AgentInfo;
  assignedTeam?: string;
  sla: SLAInfo;
  createdAt: Date;
  resolvedAt?: Date;
  firstResponseAt?: Date;
  resolutionTime?: number;
  satisfactionRating?: number;
  tags: string[];
}

type TicketStatus =
  | 'NEW'
  | 'OPEN'
  | 'PENDING'
  | 'ESCALATED'
  | 'RESOLVED'
  | 'CLOSED';

type TicketPriority = 'CRITICAL' | 'HIGH' | 'MEDIUM' | 'LOW';

type SupportCategory =
  | 'TECHNICAL'
  | 'BILLING'
  | 'ACCOUNT'
  | 'PRODUCT'
  | 'FEATURE_REQUEST'
  | 'BUG_REPORT'
  | 'OTHER';

type TicketChannel =
  | 'EMAIL'
  | 'CHAT'
  | 'PHONE'
  | 'WEB_PORTAL'
  | 'MOBILE_APP'
  | 'SOCIAL_MEDIA';

interface CustomerInfo {
  id: string;
  name: string;
  email: string;
  phone?: string;
  accountType: string;
  tier: CustomerTier;
}

type CustomerTier = 'ENTERPRISE' | 'PREMIUM' | 'STANDARD' | 'BASIC';

interface AgentInfo {
  id: string;
  name: string;
  email: string;
  country: string;
  avatar?: string;
}

interface SLAInfo {
  policy: string;
  responseTarget: number; // minutes
  resolutionTarget: number; // hours
  responseDue: Date;
  resolutionDue: Date;
  responseStatus: 'met' | 'breached' | 'pending';
  resolutionStatus: 'met' | 'breached' | 'pending';
}
```

### 2.3 Team Performance Models

```typescript
// Support Team
interface SupportTeam {
  id: string;
  name: string;
  country: CountryInfo;
  type: TeamType;
  agents: AgentInfo[];
  teamLead?: AgentInfo;
  capacity: TeamCapacity;
  performance: TeamPerformance;
}

type TeamType = 'INBOUND' | 'OUTBOUND' | 'SPECIALIZED' | 'ESCALATION';

interface TeamCapacity {
  totalAgents: number;
  activeAgents: number;
  availableAgents: number;
  maxConcurrentChats: number;
  currentQueueSize: number;
}

interface TeamPerformance {
  period: string;
  ticketsResolved: number;
  avgResolutionTime: number;
  avgFirstResponseTime: number;
  customerSatisfaction: number;
  slaCompliance: number;
  agentUtilization: number;
}
```

### 2.4 Support Analytics Models

```typescript
// Support Analytics
interface SupportAnalytics {
  period: DateRange;
  ticketVolume: TicketVolumeMetrics;
  resolutionMetrics: ResolutionMetrics;
  satisfactionMetrics: SatisfactionMetrics;
  agentPerformance: AgentPerformance[];
  trendAnalysis: SupportTrend[];
  channelBreakdown: ChannelBreakdown[];
}

interface TicketVolumeMetrics {
  total: number;
  new: number;
  resolved: number;
  open: number;
  byCategory: Record<string, number>;
  byPriority: Record<TicketPriority, number>;
  byCountry: Record<string, number>;
}

interface ResolutionMetrics {
  avgResolutionTime: number;
  avgFirstResponseTime: number;
  slaBreachedCount: number;
  slaComplianceRate: number;
  withinSLAPercentage: number;
}

interface SatisfactionMetrics {
  avgRating: number;
  totalRatings: number;
  distribution: Record<number, number>; // 1-5 stars
  promoters: number;
  detractors: number;
  npsScore: number;
}
```

---

## 3. API ENDPOINTS

### 3.1 Global Overview Endpoints

```typescript
// GET /support/dashboard/global
interface GlobalDashboardEndpoint {
  GET: {
    query: {
      period?: 'today' | 'week' | 'month' | 'quarter';
      startDate?: Date;
      endDate?: Date;
    };
    response: GlobalSupportSummary;
  };
}

// GET /support/countries/summary
interface CountriesSummaryEndpoint {
  GET: {
    query: {
      period?: string;
    };
    response: CountrySupportSummary[];
  };
}
```

### 3.2 Ticket Endpoints

```typescript
// GET /support/tickets
interface TicketsListEndpoint {
  GET: {
    query: {
      status?: TicketStatus;
      priority?: TicketPriority;
      category?: SupportCategory;
      country?: string;
      assignedAgent?: string;
      page?: number;
      pageSize?: number;
      sortBy?: string;
      sortOrder?: 'asc' | 'desc';
    };
    response: PaginatedResponse<GlobalSupportTicket>;
  };
}

// GET /support/tickets/:id
interface TicketDetailEndpoint {
  GET: {
    params: { id: string };
    response: GlobalSupportTicket;
  };
}

// PUT /support/tickets/:id
interface UpdateTicketEndpoint {
  PUT: {
    params: { id: string };
    request: Partial<GlobalSupportTicket>;
    response: GlobalSupportTicket;
  };
}

// POST /support/tickets/:id/escalate
interface EscalateTicketEndpoint {
  POST: {
    params: { id: string };
    request: {
      reason: string;
      escalateTo: string;
    };
    response: GlobalSupportTicket;
  };
}

// POST /support/tickets/:id/resolve
interface ResolveTicketEndpoint {
  POST: {
    params: { id: string };
    request: {
      resolution: string;
      customerSatisfaction?: number;
    };
    response: GlobalSupportTicket;
  };
}
```

### 3.3 Team Management Endpoints

```typescript
// GET /support/teams
interface TeamsListEndpoint {
  GET: {
    query: {
      country?: string;
      type?: TeamType;
    };
    response: SupportTeam[];
  };
}

// GET /support/teams/:id/performance
interface TeamPerformanceEndpoint {
  GET: {
    params: { id: string };
    query: {
      period?: string;
    };
    response: TeamPerformance;
  };
}

// GET /support/agents/performance
interface AgentsPerformanceEndpoint {
  GET: {
    query: {
      period?: string;
      country?: string;
      limit?: number;
    };
    response: AgentPerformance[];
  };
}
```

### 3.4 Analytics Endpoints

```typescript
// GET /support/analytics
interface AnalyticsEndpoint {
  GET: {
    query: {
      period: string;
      groupBy?: 'country' | 'category' | 'channel' | 'agent';
      startDate?: Date;
      endDate?: Date;
    };
    response: SupportAnalytics;
  };
}

// GET /support/analytics/trends
interface TrendsEndpoint {
  GET: {
    query: {
      period: string;
      metric: 'volume' | 'resolution_time' | 'satisfaction' | 'sla';
    };
    response: SupportTrend[];
  };
}
```

### 3.5 SLA Monitoring Endpoints

```typescript
// GET /support/sla/status
interface SLAStatusEndpoint {
  GET: {
    query: {
      country?: string;
      atRiskOnly?: boolean;
    };
    response: SLAStatus[];
  };
}

// GET /support/sla/breaches
interface SLABreachesEndpoint {
  GET: {
    query: {
      period?: string;
      country?: string;
    };
    response: SLABreachReport;
  };
}
```

---

## 4. STATE MANAGEMENT

### 4.1 Store Structure (Zustand)

```typescript
interface HQSupportDashboardStore {
  // Auth state
  auth: {
    user: HQSupportUser | null;
    accessToken: string | null;
    isAuthenticated: boolean;
    permissions: Permission[];
    countryScope?: string[];
  };

  // Global dashboard state
  globalDashboard: {
    summary: GlobalSupportSummary | null;
    loading: boolean;
    error: string | null;
    selectedPeriod: string;
  };

  // Tickets state
  tickets: {
    items: GlobalSupportTicket[];
    selected: GlobalSupportTicket | null;
    filters: TicketFilters;
    pagination: PaginationInfo;
    loading: boolean;
    error: string | null;
  };

  // Teams state
  teams: {
    items: SupportTeam[];
    selected: SupportTeam | null;
    performance: TeamPerformance | null;
    loading: boolean;
    error: string | null;
  };

  // Analytics state
  analytics: {
    data: SupportAnalytics | null;
    trends: SupportTrend[];
    loading: boolean;
    error: string | null;
  };

  // SLA state
  sla: {
    status: SLAStatus[];
    breaches: SLABreachReport | null;
    loading: boolean;
    error: string | null;
  };

  // UI state
  ui: {
    sidebarOpen: boolean;
    selectedCountries: string[];
    selectedPeriod: string;
    theme: 'light' | 'dark';
  };
}
```

### 4.2 React Query Keys

```typescript
const queryKeys = {
  // Global dashboard
  globalDashboard: (period: string) => ['support', 'dashboard', period] as const,

  // Tickets
  tickets: (filters?: TicketFilters) => ['support', 'tickets', filters] as const,
  ticket: (id: string) => ['support', 'ticket', id] as const,

  // Teams
  teams: (country?: string) => ['support', 'teams', country] as const,
  team: (id: string) => ['support', 'team', id] as const,
  teamPerformance: (id: string, period: string) => ['support', 'team', id, 'performance', period] as const,

  // Analytics
  analytics: (period: string) => ['support', 'analytics', period] as const,
  trends: (metric: string, period: string) => ['support', 'trends', metric, period] as const,

  // SLA
  slaStatus: (country?: string) => ['support', 'sla', 'status', country] as const,
  slaBreaches: (period?: string) => ['support', 'sla', 'breaches', period] as const,
};
```

---

## 5. MOCK DATA EXAMPLES

### 5.1 Mock Global Support Summary

```json
{
  "period": {
    "start": "2025-02-01T00:00:00Z",
    "end": "2025-02-08T23:59:59Z",
    "type": "week"
  },
  "globalMetrics": {
    "totalTickets": 12450,
    "openTickets": 1823,
    "resolvedTickets": 9876,
    "escalatedTickets": 751,
    "avgResolutionTime": 4.2,
    "customerSatisfaction": 4.3,
    "slaCompliance": 94.5
  },
  "countriesSummary": [
    {
      "country": {
        "code": "NG",
        "name": "Nigeria",
        "flag": "🇳🇬"
      },
      "metrics": {
        "openTickets": 523,
        "resolvedToday": 412,
        "avgResolutionTime": 3.8,
        "satisfaction": 4.4,
        "slaCompliance": 95.2
      },
      "trend": "up",
      "status": "excellent"
    },
    {
      "country": {
        "code": "KE",
        "name": "Kenya",
        "flag": "🇰🇪"
      },
      "metrics": {
        "openTickets": 312,
        "resolvedToday": 289,
        "avgResolutionTime": 4.5,
        "satisfaction": 4.2,
        "slaCompliance": 93.8
      },
      "trend": "neutral",
      "status": "good"
    },
    {
      "country": {
        "code": "ZA",
        "name": "South Africa",
        "flag": "🇿🇦"
      },
      "metrics": {
        "openTickets": 445,
        "resolvedToday": 398,
        "avgResolutionTime": 4.1,
        "satisfaction": 4.5,
        "slaCompliance": 96.1
      },
      "trend": "up",
      "status": "excellent"
    }
  ],
  "teamPerformance": {
    "period": "2025-02-08",
    "totalAgents": 145,
    "activeAgents": 128,
    "avgTicketsPerAgent": 86,
    "topPerformer": {
      "id": "agt-001",
      "name": "Sarah Johnson",
      "country": "ZA",
      "ticketsResolved": 156,
      "avgRating": 4.8
    }
  },
  "channelMetrics": {
    "email": { "volume": 5234, "avgResponseTime": 120, "satisfaction": 4.1 },
    "chat": { "volume": 4123, "avgResponseTime": 2, "satisfaction": 4.5 },
    "phone": { "volume": 2098, "avgResponseTime": 180, "satisfaction": 4.3 },
    "webPortal": { "volume": 995, "avgResponseTime": 45, "satisfaction": 4.2 }
  }
}
```

### 5.2 Mock Support Tickets

```json
[
  {
    "id": "tkt-001",
    "ticketNumber": "SUP-2025-001234",
    "subject": "Unable to process payment",
    "description": "Customer unable to complete payment for order #12345",
    "status": "OPEN",
    "priority": "HIGH",
    "category": "BILLING",
    "channel": "CHAT",
    "country": {
      "code": "NG",
      "name": "Nigeria",
      "flag": "🇳🇬"
    },
    "customer": {
      "id": "cust-001",
      "name": "John Doe",
      "email": "john.doe@example.com",
      "accountType": "BUSINESS",
      "tier": "PREMIUM"
    },
    "assignedAgent": {
      "id": "agt-001",
      "name": "Sarah Johnson",
      "email": "sarah.j@gogidix.com",
      "country": "NG",
      "avatar": "https://cdn.company.com/avatars/sarah.jpg"
    },
    "assignedTeam": "NG-Billing-Specialists",
    "sla": {
      "policy": "PREMIUM_24HR",
      "responseTarget": 30,
      "resolutionTarget": 24,
      "responseDue": "2025-02-08T11:30:00Z",
      "resolutionDue": "2025-02-09T11:30:00Z",
      "responseStatus": "met",
      "resolutionStatus": "pending"
    },
    "createdAt": "2025-02-08T10:00:00Z",
    "firstResponseAt": "2025-02-08T10:15:00Z",
    "tags": ["payment", "urgent", "premium"],
    "satisfactionRating": null
  },
  {
    "id": "tkt-002",
    "ticketNumber": "SUP-2025-001235",
    "subject": "Feature request: Bulk export",
    "description": "Customer requests ability to export multiple reports at once",
    "status": "NEW",
    "priority": "MEDIUM",
    "category": "FEATURE_REQUEST",
    "channel": "EMAIL",
    "country": {
      "code": "KE",
      "name": "Kenya",
      "flag": "🇰🇪"
    },
    "customer": {
      "id": "cust-002",
      "name": "Jane Smith",
      "email": "jane.smith@company.co.ke",
      "accountType": "ENTERPRISE",
      "tier": "ENTERPRISE"
    },
    "sla": {
      "policy": "ENTERPRISE_4HR",
      "responseTarget": 240,
      "resolutionTarget": 48,
      "responseDue": "2025-02-08T14:00:00Z",
      "resolutionDue": "2025-02-10T10:00:00Z",
      "responseStatus": "pending",
      "resolutionStatus": "pending"
    },
    "createdAt": "2025-02-08T09:00:00Z",
    "tags": ["feature-request", "enterprise"],
    "satisfactionRating": null
  }
]
```

### 5.3 Mock Team Performance

```json
[
  {
    "id": "team-001",
    "name": "NG-Support-Team-A",
    "country": {
      "code": "NG",
      "name": "Nigeria",
      "flag": "🇳🇬"
    },
    "type": "INBOUND",
    "agents": [
      {
        "id": "agt-001",
        "name": "Sarah Johnson",
        "email": "sarah.j@gogidix.com",
        "country": "NG"
      },
      {
        "id": "agt-002",
        "name": "Michael Chen",
        "email": "michael.c@gogidix.com",
        "country": "NG"
      }
    ],
    "teamLead": {
      "id": "agt-001",
      "name": "Sarah Johnson",
      "email": "sarah.j@gogidix.com"
    },
    "capacity": {
      "totalAgents": 8,
      "activeAgents": 7,
      "availableAgents": 3,
      "maxConcurrentChats": 24,
      "currentQueueSize": 12
    },
    "performance": {
      "period": "2025-02-08",
      "ticketsResolved": 156,
      "avgResolutionTime": 3.5,
      "avgFirstResponseTime": 2.1,
      "customerSatisfaction": 4.6,
      "slaCompliance": 97.2,
      "agentUtilization": 87.5
    }
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
| SUP_AUTH_001 | 401 | Invalid or expired token |
| SUP_AUTH_002 | 403 | Insufficient permissions for country |
| SUP_VAL_001 | 400 | Invalid ticket data |
| SUP_VAL_002 | 400 | Invalid status transition |
| SUP_RES_001 | 404 | Ticket not found |
| SUP_RES_002 | 404 | Team not found |
| SUP_BIZ_001 | 400 | Cannot delete open ticket |
| SUP_BIZ_002 | 400 | SLA already breached |
| SUP_SLA_001 | 400 | Escalation limit reached |
| SUP_SRV_001 | 500 | Internal server error |
| SUP_SRV_002 | 503 | Service temporarily unavailable |

---

## VERSION HISTORY

| Version | Date | Changes |
|---------|------|---------|
| 1.0 | 2025-02-08 | Initial HQ Support Dashboard Mock Flow Documentation |

---

**Document End**
