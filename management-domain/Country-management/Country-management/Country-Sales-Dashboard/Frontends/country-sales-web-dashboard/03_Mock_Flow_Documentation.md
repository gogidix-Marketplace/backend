# COUNTRY SALES DASHBOARD - MOCK FLOW DOCUMENTATION

**Version:** 1.1
**Domain:** Business Domain
**Subdomain:** Country-Sales-Dashboard
**Frontend:** country-sales-web-dashboard
**Framework:** React 18 + Vite + TypeScript + Material UI v5.15+
**State Management:** Zustand
**Data Fetching:** TanStack Query
**Last Updated:** 2025-02-16

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
Development:  https://sales.country.{country}.api.dev/v1
Staging:     https://sales.country.{country}.api.staging/v1
Production:  https://sales.country.{country}.api/v1

Example (Nigeria):
Development:  https://sales.country.ng.api.dev/v1
Production:  https://sales.country.ng.api/v1
```

### 1.2 Authentication

All requests require authentication via Bearer token:

```typescript
const headers = {
  'Authorization': `Bearer ${accessToken}`,
  'Content-Type': 'application/json',
  'X-Country-Code': 'NG',  // Auto-injected from user profile
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

interface PaginationInfo {
  page: number;
  pageSize: number;
  totalItems: number;
  totalPages: number;
  hasNext: boolean;
  hasPrevious: boolean;
}
```

---

## 2. DATA MODELS & TYPES

### 2.1 User & Authentication

```typescript
// Country Sales User
interface CountrySalesUser {
  id: string;
  email: string;
  firstName: string;
  lastName: string;
  avatar?: string;
  role: CountrySalesRole;
  assignedCountry: AssignedCountry;
  department?: string;
  location: string;
  permissions: Permission[];
  timezone: string;
  createdAt: Date;
  lastLoginAt: Date;
}

type CountrySalesRole =
  | 'COUNTRY_SALES_DIRECTOR'
  | 'SALES_MANAGER'
  | 'SALES_EXECUTIVE'
  | 'SALES_OPERATIONS_MANAGER'
  | 'SALES_ANALYST';

interface AssignedCountry {
  code: string;  // 'NG', 'KE', 'GH', etc.
  name: string;  // 'Nigeria', 'Kenya', 'Ghana'
  currency: string;  // 'NGN', 'KES', 'GHS'
  timezone: string;  // 'Africa/Lagos', 'Africa/Nairobi'
}

interface Permission {
  resource: string;
  actions: ('create' | 'read' | 'update' | 'delete' | 'approve')[];
}

// Auth Response
interface AuthResponse {
  user: CountrySalesUser;
  accessToken: string;
  refreshToken: string;
  expiresIn: number;  // seconds
}

// Login Request
interface LoginRequest {
  email: string;
  password: string;
  deviceId?: string;
  deviceName?: string;
}
```

### 2.2 Lead & Pipeline Models

```typescript
// Lead
interface Lead {
  id: string;
  leadNumber: string;  // 'L-1001'
  company: string;
  contact: {
    firstName: string;
    lastName: string;
    email: string;
    phone: string;
    title?: string;
  };
  status: LeadStatus;
  source: LeadSource;
  score: number;  // 0-100
  owner: SalesRep;
  territory?: Territory;
  estimatedValue: number;
  currency: string;
  probability: number;  // 0-100
  expectedCloseDate?: Date;
  stage: string;
  tags: string[];
  customFields: Record<string, any>;
  activities: Activity[];
  createdAt: Date;
  updatedAt: Date;
  convertedAt?: Date;
  convertedToDealId?: string;
}

type LeadStatus =
  | 'NEW'
  | 'CONTACTED'
  | 'QUALIFIED'
  | 'PROPOSAL'
  | 'NEGOTIATION'
  | 'CONVERTED'
  | 'LOST'
  | 'UNQUALIFIED';

type LeadSource =
  | 'WEBSITE'
  | 'REFERRAL'
  | 'EVENT'
  | 'OUTBOUND'
  | 'PARTNER'
  | 'ADVERTISEMENT'
  | 'SOCIAL_MEDIA'
  | 'OTHER';

// Pipeline Stage
interface PipelineStage {
  id: string;
  name: string;
  order: number;
  probability: number;  // Default probability for this stage
  daysInStage: number;  // Expected days
  color: string;
}

// Pipeline Summary
interface PipelineSummary {
  stage: string;
  count: number;
  totalValue: number;
  currency: string;
  weightedValue: number;
  deals: DealSummary[];
}
```

### 2.3 Deal Models

```typescript
// Deal
interface Deal {
  id: string;
  dealNumber: string;  // 'D-2501'
  name: string;
  companyId: string;
  contact: ContactInfo;
  status: DealStatus;
  stage: string;
  probability: number;  // 0-100
  value: number;
  discountAmount: number;
  discountPercentage: number;
  netValue: number;
  currency: string;
  owner: SalesRep;
  territory?: Territory;
  expectedCloseDate: Date;
  actualCloseDate?: Date;
  wonReason?: string;
  lostReason?: string;
  products: DealLineItem[];
  commission: CommissionInfo;
  createdAt: Date;
  updatedAt: Date;
  lastStageChangeAt: Date;
  daysInCurrentStage: number;
  activities: Activity[];
  competitorInfo?: CompetitorInfo[];
  nextSteps: string[];
}

type DealStatus = 'ACTIVE' | 'WON' | 'LOST' | 'ON_HOLD' | 'CANCELLED';

interface DealLineItem {
  id: string;
  productCode: string;
  productName: string;
  description: string;
  quantity: number;
  unitPrice: number;
  discount: number;
  tax: number;
  total: number;
}

interface CommissionInfo {
  rate: number;  // percentage
  amount: number;
  quotaCredit: number;
  eligibleRepIds: string[];
  paidAt?: Date;
}

interface CompetitorInfo {
  name: string;
  strengths: string[];
  weaknesses: string[];
  price?: number;
}

interface DealSummary {
  id: string;
  name: string;
  company: string;
  value: number;
  stage: string;
  probability: number;
  expectedCloseDate: Date;
  owner: {
    id: string;
    name: string;
  };
}

// Deal Approval Request
interface DealApprovalRequest {
  id: string;
  dealId: string;
  dealNumber: string;
  dealName: string;
  requestType: 'DISCOUNT' | 'EXTENSION' | 'SPECIAL_TERMS';
  currentValue: number;
  requestedValue: number;
  reason: string;
  requestedBy: SalesRep;
  requestedAt: Date;
  status: 'PENDING' | 'APPROVED' | 'REJECTED';
  approvedBy?: string;
  approvedAt?: Date;
  rejectionReason?: string;
}
```

### 2.4 Customer Models

```typescript
// Customer (CRM)
interface Customer {
  id: string;
  customerNumber: string;  // 'C-5001'
  type: 'INDIVIDUAL' | 'CORPORATE';
  status: 'ACTIVE' | 'INACTIVE' | 'SUSPENDED';
  tier: CustomerTier;

  // Individual fields
  individualInfo?: {
    firstName: string;
    lastName: string;
    dateOfBirth?: Date;
    nationalId?: string;
  };

  // Corporate fields
  corporateInfo?: {
    companyName: string;
    registrationNumber?: string;
    taxId?: string;
    industry: string;
    employeeCount?: number;
    website?: string;
    headquarters?: string;
  };

  // Common fields
  contact: {
    email: string;
    phone: string;
    address: Address;
    billingAddress?: Address;
    shippingAddress?: Address;
  };

  // Sales info
  accountOwner: SalesRep;
  territory?: Territory;
  source: string;
  tags: string[];

  // Metrics
  lifetimeValue: number;
  annualRecurringValue: number;
  totalPurchases: number;
  averageOrderValue: number;
  firstPurchaseDate?: Date;
  lastPurchaseDate?: Date;
  nextRenewalDate?: Date;

  // Terms
  paymentTerms: string;
  creditLimit: number;
  outstandingBalance: number;

  // Related data
  deals: DealSummary[];
  orders: OrderSummary[];
  supportTickets?: number;

  customFields: Record<string, any>;

  createdAt: Date;
  updatedAt: Date;
}

type CustomerTier = 'PLATINUM' | 'GOLD' | 'SILVER' | 'BRONZE';

interface Address {
  line1: string;
  line2?: string;
  city: string;
  state: string;
  postalCode: string;
  country: string;
}

interface OrderSummary {
  id: string;
  orderNumber: string;
  date: Date;
  total: number;
  status: string;
}
```

### 2.5 Order Models

```typescript
// Sales Order
interface SalesOrder {
  id: string;
  orderNumber: string;  // 'SO-2501'
  dealId?: string;
  customerId: string;
  customerName: string;
  status: OrderStatus;
  orderDate: Date;
  expectedDeliveryDate?: Date;
  actualDeliveryDate?: Date;

  // Items
  items: OrderLineItem[];
  subtotal: number;
  discountAmount: number;
  taxAmount: number;
  total: number;
  currency: string;

  // Shipping
  shippingAddress: Address;
  shippingMethod?: string;
  shippingCost: number;
  trackingNumber?: string;

  // Billing
  billingAddress: Address;
  paymentTerms: string;
  paymentStatus: 'PENDING' | 'PARTIAL' | 'PAID' | 'OVERDUE';
  paidAmount: number;

  // Owner
  salesRep: SalesRep;

  // Additional
  notes?: string;
  internalNotes?: string;
  attachments: Attachment[];

  createdAt: Date;
  updatedAt: Date;
  createdBy: string;
}

type OrderStatus =
  | 'PENDING'
  | 'CONFIRMED'
  | 'PROCESSING'
  | 'SHIPPED'
  | 'DELIVERED'
  | 'CANCELLED'
  | 'REFUNDED'
  | 'ON_HOLD';

interface OrderLineItem {
  id: string;
  productCode: string;
  productName: string;
  description: string;
  quantity: number;
  unitPrice: number;
  discount: number;
  tax: number;
  total: number;
}
```

### 2.6 Sales Team & Performance Models

```typescript
// Sales Representative
interface SalesRep {
  id: string;
  employeeId: string;
  firstName: string;
  lastName: string;
  email: string;
  phone: string;
  avatar?: string;
  role: CountrySalesRole;
  department?: string;
  territory?: Territory;
  managerId?: string;
  status: 'ACTIVE' | 'INACTIVE' | 'ON_LEAVE';
  hireDate: Date;
}

// Territory
interface Territory {
  id: string;
  name: string;
  code: string;
  type: 'REGION' | 'CITY' | 'STATE' | 'COUNTRY';
  parentTerritoryId?: string;
  assignedRepIds: string[];
  quota?: Quota;
  metrics: TerritoryMetrics;
}

interface TerritoryMetrics {
  currentRevenue: number;
  quotaAttainment: number;
  activeDeals: number;
  winRate: number;
  avgDealSize: number;
}

// Quota
interface Quota {
  id: string;
  name: string;
  description?: string;
  type: 'REVENUE' | 'DEALS' | 'UNITS';
  periodType: 'ANNUAL' | 'QUARTERLY' | 'MONTHLY';
  periodStart: Date;
  periodEnd: Date;
  targetAmount: number;
  currentAmount: number;
  attainmentPercentage: number;
  currency?: string;
  assignedTo: {
    entityType: 'INDIVIDUAL' | 'TEAM' | 'TERRITORY';
    entityId: string;
    entityName: string;
  };
  breakdown: QuotaBreakdown[];
}

interface QuotaBreakdown {
  period: string;  // 'Q1', 'Q2', 'January', etc.
  target: number;
  current: number;
  attainment: number;
  dueDate: Date;
}

// Performance Metrics
interface PerformanceMetrics {
  period: {
    start: Date;
    end: Date;
    type: 'DAILY' | 'WEEKLY' | 'MONTHLY' | 'QUARTERLY' | 'ANNUAL';
  };
  revenue: {
    total: number;
    target: number;
    attainment: number;
    change: number;
    currency: string;
  };
  pipeline: {
    value: number;
    count: number;
    weightedValue: number;
    change: number;
  };
  deals: {
    closed: number;
    won: number;
    lost: number;
    inProgress: number;
    winRate: number;
    avgSize: number;
    avgCycle: number;  // days
  };
  activities: {
    calls: number;
    emails: number;
    meetings: number;
    demos: number;
    proposals: number;
  };
}
```

### 2.7 Forecasting Models

```typescript
// Sales Forecast
interface SalesForecast {
  id: string;
  name: string;
  period: ForecastPeriod;
  scenario: ForecastScenario;
  generatedAt: Date;
  generatedBy: string;

  // Forecast values
  revenue: {
    forecast: number;
    bestCase: number;
    worstCase: number;
    currency: string;
  };

  // Pipeline contribution
  pipelineBreakdown: PipelineForecast[];
  historicalTrend: ForecastDataPoint[];
  accuracyMetrics: ForecastAccuracy[];
}

interface ForecastPeriod {
  type: 'QUARTER' | 'MONTH' | 'YEAR';
  startDate: Date;
  endDate: Date;
  label: string;  // 'Q1 2025', 'February 2025'
}

type ForecastScenario = 'BEST' | 'LIKELY' | 'WORST' | 'CUSTOM';

interface PipelineForecast {
  stage: string;
  count: number;
  value: number;
  weightedValue: number;
  probability: number;
  expectedCloseDate: Date;
}

interface ForecastDataPoint {
  period: string;
  actual?: number;
  forecast: number;
  variance?: number;
}

interface ForecastAccuracy {
  period: string;
  accuracy: number;  // percentage
  actualRevenue: number;
  forecastedRevenue: number;
  variance: number;
  variancePercentage: number;
}
```

### 2.8 Communication Models

```typescript
// Email Template
interface EmailTemplate {
  id: string;
  name: string;
  subject: string;
  body: string;
  category: string;
  variables: TemplateVariable[];
  isActive: boolean;
  createdBy: string;
  createdAt: Date;
  updatedAt: Date;
}

interface TemplateVariable {
  key: string;
  label: string;
  type: 'text' | 'date' | 'currency' | 'number' | 'boolean';
  defaultValue?: string;
}

// Campaign
interface Campaign {
  id: string;
  name: string;
  type: 'EMAIL' | 'SMS' | 'MULTI_CHANNEL';
  status: 'DRAFT' | 'SCHEDULED' | 'RUNNING' | 'COMPLETED' | 'CANCELLED';
  description?: string;

  // Targeting
  targetAudience: {
    type: 'SEGMENT' | 'LIST' | 'ALL';
    criteria?: AudienceCriteria;
    estimatedCount: number;
  };

  // Content
  templateId?: string;
  subject?: string;
  content?: string;

  // Scheduling
  scheduledStart?: Date;
  scheduledEnd?: Date;
  actualStart?: Date;
  actualEnd?: Date;

  // Budget (if applicable)
  budget?: number;

  // Metrics
  metrics: CampaignMetrics;

  createdBy: string;
  createdAt: Date;
  updatedAt: Date;
}

interface AudienceCriteria {
  customerType?: ('INDIVIDUAL' | 'CORPORATE')[];
  tier?: CustomerTier[];
  territory?: string[];
  purchaseHistory?: {
    minAmount?: number;
    maxAmount?: number;
    lastPurchaseAfter?: Date;
  };
  tags?: string[];
}

interface CampaignMetrics {
  sent: number;
  delivered: number;
  opened: number;
  clicked: number;
  bounced: number;
  unsubscribed: number;
  converted: number;
  conversionRate: number;
}

// Activity
interface Activity {
  id: string;
  type: ActivityType;
  subject: string;
  description?: string;
  entityType: 'LEAD' | 'DEAL' | 'CUSTOMER';
  entityId: string;
  entityName?: string;

  createdBy: string;
  createdAt: Date;

  // Type-specific data
  callData?: CallActivityData;
  emailData?: EmailActivityData;
  meetingData?: MeetingActivityData;
  noteData?: NoteActivityData;
}

type ActivityType =
  | 'CALL'
  | 'EMAIL'
  | 'MEETING'
  | 'NOTE'
  | 'TASK'
  | 'DEMO'
  | 'PROPOSAL'
  | 'STAGE_CHANGE'
  | 'ASSIGNMENT'
  | 'OTHER';

interface CallActivityData {
  duration: number;  // minutes
  outcome: 'CONNECTED' | 'NO_ANSWER' | 'VOICEMAIL' | 'CALL_BACK';
  notes?: string;
  recordingUrl?: string;
}

interface EmailActivityData {
  to: string[];
  cc?: string[];
  subject: string;
  body: string;
  opened?: boolean;
  clicked?: boolean;
  replied?: boolean;
}

interface MeetingActivityData {
  startTime: Date;
  endTime: Date;
  location?: string;
  attendees: string[];
  agenda?: string;
  outcome?: string;
}

interface NoteActivityData {
  content: string;
  isPrivate: boolean;
  attachments?: Attachment[];
}
```

### 2.9 Common Models

```typescript
// Attachment
interface Attachment {
  id: string;
  name: string;
  type: string;
  size: number;
  url: string;
  uploadedBy: string;
  uploadedAt: Date;
}

// Notification
interface Notification {
  id: string;
  userId: string;
  type: NotificationType;
  title: string;
  message: string;
  actionUrl?: string;
  read: boolean;
  createdAt: Date;
}

type NotificationType =
  | 'DEAL_ASSIGNED'
  | 'DEAL_STAGE_CHANGED'
  | 'DEAL_WON'
  | 'DEAL_LOST'
  | 'LEAD_ASSIGNED'
  | 'LEAD_CONVERTED'
  | 'ORDER_STATUS_CHANGED'
  | 'APPROVAL_REQUESTED'
  | 'APPROVAL_APPROVED'
  | 'APPROVAL_REJECTED'
  | 'QUOTA_ALERT'
  | 'FORECAST_UPDATE'
  | 'SYSTEM';

// Dashboard Summary
interface DashboardSummary {
  period: {
    start: Date;
    end: Date;
    type: string;
  };
  metrics: {
    revenue: MetricData;
    pipeline: MetricData;
    dealsClosed: MetricData;
    avgDealSize: MetricData;
  };
  performance: PerformanceData;
  topPerformers: TopPerformer[];
  recentDeals: DealSummary[];
  recentLeads: LeadSummary[];
  alerts: Alert[];
}

interface MetricData {
  value: number;
  label: string;
  change: number;
  changeType: 'increase' | 'decrease' | 'neutral';
  previousPeriod: number;
}

interface PerformanceData {
  quotaAttainment: number;
  winRate: number;
  avgDealCycle: number;
  pipelineVelocity: number;
}

interface TopPerformer {
  userId: string;
  name: string;
  avatar?: string;
  department?: string;
  revenue: number;
  quota: number;
  attainment: number;
  dealsClosed: number;
}

interface LeadSummary {
  id: string;
  company: string;
  contact: string;
  score: number;
  status: string;
  createdAt: Date;
}

interface Alert {
  id: string;
  type: 'WARNING' | 'INFO' | 'SUCCESS' | 'ERROR';
  title: string;
  message: string;
  actionUrl?: string;
  createdAt: Date;
}
```

### 2.10 Sales Team Partners Models

```typescript
// Sales Team Partner (Country-level view)
interface CountrySalesTeamPartner {
  id: string;
  partnerNumber: string;  // 'P-2001'
  userId: string;
  user: {
    firstName: string;
    lastName: string;
    email: string;
    phone: string;
    avatar?: string;
  };
  partnerType: PartnerType;
  territory?: Territory;
  status: PartnerStatus;
  tier: PartnerTier;

  // Performance metrics
  partnersRecruited: number;
  customersAcquired: number;
  pipelineValue: number;
  revenueGenerated: number;
  commissionEarned: number;
  referralBonusEarned: number;

  // AI Lead assignments
  assignedLeads: number;
  convertedLeads: number;
  leadConversionRate: number;

  // Approval info
  applicationId?: string;
  approvedAt?: Date;
  approvedBy?: string;

  createdAt: Date;
  updatedAt: Date;
  lastActivityAt?: Date;
}

type PartnerType =
  | 'COURIER'
  | 'HAULAGE'
  | 'WAREHOUSE'
  | 'ECOMMERCE'
  | 'AIR_OCEAN'
  | 'LOCATION_AGENT'
  | 'WHOLESALE'
  | 'INFLUENCER';

type PartnerStatus =
  | 'PENDING'
  | 'ACTIVE'
  | 'SUSPENDED'
  | 'TERMINATED';

type PartnerTier =
  | 'BRONZE'
  | 'SILVER'
  | 'GOLD'
  | 'PLATINUM';

// Partner Application (Country-level review)
interface CountryPartnerApplication {
  id: string;
  applicationNumber: string;  // 'PA-1001'
  applicant: {
    firstName: string;
    lastName: string;
    email: string;
    phone: string;
    address: Address;
  };
  partnerType: PartnerType;
  preferredTerritory?: string;

  // Supporting documents
  documents: ApplicationDocument[];

  // Country-level review
  countryReview: {
    status: 'PENDING' | 'APPROVED' | 'REJECTED' | 'ESCALATED';
    reviewedBy?: string;
    reviewedAt?: Date;
    reviewNotes?: string;
    rejectionReason?: string;
    territoryAssigned?: string;
  };

  // Background check
  backgroundCheck: {
    status: 'PENDING' | 'CLEARED' | 'FLAGGED';
    completedAt?: Date;
    notes?: string;
  };

  referredBy?: string;

  createdAt: Date;
  updatedAt: Date;
}

interface ApplicationDocument {
  id: string;
  type: 'ID_DOCUMENT' | 'PROOF_OF_ADDRESS' | 'BUSINESS_REG' | 'TAX_CERT' | 'OTHER';
  name: string;
  url: string;
  uploadedAt: Date;
  verified: boolean;
}

// Partner Commission (Country-level)
interface CountryPartnerCommission {
  id: string;
  partnerId: string;
  period: CommissionPeriod;

  // Referral bonus breakdown
  referralBonus: {
    partnersReferred: number;
    totalBonus: number;
    paidBonus: number;
    pendingBonus: number;
    breakdown: ReferralBonusBreakdown[];
  };

  // Sales commission breakdown
  salesCommission: {
    customersAcquired: number;
    totalRevenue: number;
    totalCommission: number;
    paidCommission: number;
    pendingCommission: number;
    breakdown: SalesCommissionBreakdown[];
  };

  // Payment info
  paymentInfo: {
    bankName?: string;
    accountNumber?: string;
    lastPaymentDate?: Date;
    nextPaymentDate?: Date;
  };
}

interface ReferralBonusBreakdown {
  referredPartnerId: string;
  referredPartnerName: string;
  bonusAmount: number;
  paidAt?: Date;
}

interface SalesCommissionBreakdown {
  customerId: string;
  customerName: string;
  dealValue: number;
  commissionRate: number;
  commissionAmount: number;
  dealClosedAt: Date;
  paidAt?: Date;
}

interface CommissionPeriod {
  type: 'MONTHLY' | 'QUARTERLY';
  start: Date;
  end: Date;
  label: string;
}

// Partner Territory Assignment
interface CountryPartnerTerritory {
  id: string;
  partnerId: string;
  territoryId: string;
  territory: Territory;
  exclusive: boolean;

  // Capacity
  capacity: {
    maxPartners: number;
    maxCustomers: number;
    currentPartners: number;
    currentCustomers: number;
  };

  // Lead allocation
  leadAllocation: {
    enabled: boolean;
    maxDailyLeads: number;
    currentDailyLeads: number;
  };

  assignedAt: Date;
  assignedBy: string;
  active: boolean;
}

// Partners Summary (Country-level)
interface CountryPartnersSummary {
  period: PeriodInfo;
  totalPartners: number;
  activePartners: number;
  pendingApplications: number;

  byTerritory: TerritoryPartnersSummary[];
  byPartnerType: PartnerTypeSummary[];
  byTier: PartnerTierSummary[];

  topPerformers: TopPartnerPerformer[];
  commissionStats: CommissionStats;
  territoryCapacity: TerritoryCapacity[];
}

interface TerritoryPartnersSummary {
  territory: Territory;
  totalPartners: number;
  activePartners: number;
  pipelineValue: number;
  commissionEarned: number;
}

interface PartnerTypeSummary {
  partnerType: PartnerType;
  count: number;
  pipelineValue: number;
  revenueGenerated: number;
  avgCommission: number;
}

interface PartnerTierSummary {
  tier: PartnerTier;
  count: number;
  revenueThreshold: number;
}

interface TopPartnerPerformer {
  partnerId: string;
  name: string;
  partnerType: PartnerType;
  tier: PartnerTier;
  territory: Territory;
  pipelineValue: number;
  commissionEarned: number;
  rank: number;
}

interface CommissionStats {
  totalEarned: number;
  totalPaid: number;
  totalPending: number;
  avgPayoutTime: number;
}

interface TerritoryCapacity {
  territory: Territory;
  maxPartners: number;
  currentPartners: number;
  availableSlots: number;
}
```

---

## 3. API ENDPOINTS

### 3.1 Authentication Endpoints

```typescript
// POST /auth/login
// Login and receive access token
interface LoginEndpoint {
  POST: {
    request: LoginRequest;
    response: AuthResponse;
  };
}

// POST /auth/refresh
// Refresh access token
interface RefreshEndpoint {
  POST: {
    request: { refreshToken: string };
    response: { accessToken: string; expiresIn: number };
  };
}

// POST /auth/logout
// Logout and invalidate token
interface LogoutEndpoint {
  POST: {
    request: {};
    response: { success: boolean };
  };
}

// GET /auth/me
// Get current user info
interface MeEndpoint {
  GET: {
    response: CountrySalesUser;
  };
}
```

### 3.2 Dashboard Endpoints

```typescript
// GET /dashboard/summary
// Get dashboard overview
interface DashboardSummaryEndpoint {
  GET: {
    query: {
      period?: 'today' | 'week' | 'month' | 'quarter' | 'year';
      startDate?: Date;
      endDate?: Date;
    };
    response: DashboardSummary;
  };
}

// GET /dashboard/metrics
// Get specific metrics
interface DashboardMetricsEndpoint {
  GET: {
    query: {
      metrics: string[];
      period?: string;
      compare?: boolean;
    };
    response: Record<string, MetricData>;
  };
}
```

### 3.3 Lead Endpoints

```typescript
// GET /leads
// List leads with pagination
interface LeadsListEndpoint {
  GET: {
    query: {
      page?: number;
      pageSize?: number;
      sortBy?: string;
      sortOrder?: 'asc' | 'desc';
      search?: string;
      status?: LeadStatus[];
      source?: LeadSource[];
      owner?: string;
      scoreMin?: number;
      scoreMax?: number;
      dateFrom?: Date;
      dateTo?: Date;
    };
    response: PaginatedResponse<Lead>;
  };
}

// GET /leads/:id
// Get lead details
interface LeadDetailEndpoint {
  GET: {
    params: { id: string };
    response: Lead;
  };
}

// POST /leads
// Create new lead
interface LeadCreateEndpoint {
  POST: {
    request: Partial<Lead>;
    response: Lead;
  };
}

// PUT /leads/:id
// Update lead
interface LeadUpdateEndpoint {
  PUT: {
    params: { id: string };
    request: Partial<Lead>;
    response: Lead;
  };
}

// DELETE /leads/:id
// Delete lead
interface LeadDeleteEndpoint {
  DELETE: {
    params: { id: string };
    response: { success: boolean };
  };
}

// POST /leads/:id/convert
// Convert lead to deal
interface LeadConvertEndpoint {
  POST: {
    params: { id: string };
    request: {
      dealName: string;
      products: DealLineItem[];
      expectedCloseDate: Date;
      ownerId?: string;
    };
    response: { lead: Lead; deal: Deal };
  };
}

// POST /leads/:id/assign
// Assign lead to sales rep
interface LeadAssignEndpoint {
  POST: {
    params: { id: string };
    request: { ownerId: string };
    response: Lead;
  };
}

// GET /leads/pipeline
// Get pipeline summary
interface PipelineSummaryEndpoint {
  GET: {
    query: {
      owner?: string;
      territory?: string;
    };
    response: PipelineSummary[];
  };
}
```

### 3.4 Deal Endpoints

```typescript
// GET /deals
// List deals with pagination
interface DealsListEndpoint {
  GET: {
    query: {
      page?: number;
      pageSize?: number;
      sortBy?: string;
      sortOrder?: 'asc' | 'desc';
      search?: string;
      status?: DealStatus[];
      stage?: string[];
      owner?: string;
      valueMin?: number;
      valueMax?: number;
      closeDateFrom?: Date;
      closeDateTo?: Date;
    };
    response: PaginatedResponse<Deal>;
  };
}

// GET /deals/:id
// Get deal details
interface DealDetailEndpoint {
  GET: {
    params: { id: string };
    response: Deal;
  };
}

// POST /deals
// Create new deal
interface DealCreateEndpoint {
  POST: {
    request: Partial<Deal>;
    response: Deal;
  };
}

// PUT /deals/:id
// Update deal
interface DealUpdateEndpoint {
  PUT: {
    params: { id: string };
    request: Partial<Deal>;
    response: Deal;
  };
}

// DELETE /deals/:id
// Delete deal
interface DealDeleteEndpoint {
  DELETE: {
    params: { id: string };
    response: { success: boolean };
  };
}

// PUT /deals/:id/stage
// Update deal stage
interface DealStageUpdateEndpoint {
  PUT: {
    params: { id: string };
    request: {
      stage: string;
      probability?: number;
      reason?: string;
    };
    response: Deal;
  };
}

// POST /deals/:id/close
// Close deal (won/lost)
interface DealCloseEndpoint {
  POST: {
    params: { id: string };
    request: {
      outcome: 'WON' | 'LOST';
      reason?: string;
      actualCloseDate?: Date;
      finalValue?: number;
    };
    response: Deal;
  };
}

// POST /deals/:id/approval
// Request deal approval
interface DealApprovalRequestEndpoint {
  POST: {
    params: { id: string };
    request: {
      requestType: 'DISCOUNT' | 'EXTENSION' | 'SPECIAL_TERMS';
      reason: string;
    };
    response: DealApprovalRequest;
  };
}
```

### 3.5 Customer Endpoints

```typescript
// GET /customers
// List customers with pagination
interface CustomersListEndpoint {
  GET: {
    query: {
      page?: number;
      pageSize?: number;
      sortBy?: string;
      sortOrder?: 'asc' | 'desc';
      search?: string;
      type?: ('INDIVIDUAL' | 'CORPORATE')[];
      tier?: CustomerTier[];
      status?: ('ACTIVE' | 'INACTIVE' | 'SUSPENDED')[];
      owner?: string;
      territory?: string;
      industry?: string[];
    };
    response: PaginatedResponse<Customer>;
  };
}

// GET /customers/:id
// Get customer details
interface CustomerDetailEndpoint {
  GET: {
    params: { id: string };
    response: Customer;
  };
}

// POST /customers
// Create new customer
interface CustomerCreateEndpoint {
  POST: {
    request: Partial<Customer>;
    response: Customer;
  };
}

// PUT /customers/:id
// Update customer
interface CustomerUpdateEndpoint {
  PUT: {
    params: { id: string };
    request: Partial<Customer>;
    response: Customer;
  };
}

// DELETE /customers/:id
// Delete customer
interface CustomerDeleteEndpoint {
  DELETE: {
    params: { id: string };
    response: { success: boolean };
  };
}

// GET /customers/:id/deals
// Get customer deals
interface CustomerDealsEndpoint {
  GET: {
    params: { id: string };
    query: {
      status?: DealStatus[];
    };
    response: DealSummary[];
  };
}

// GET /customers/:id/orders
// Get customer orders
interface CustomerOrdersEndpoint {
  GET: {
    params: { id: string };
    query: {
      limit?: number;
    };
    response: OrderSummary[];
  };
}

// GET /customers/:id/activities
// Get customer activity timeline
interface CustomerActivitiesEndpoint {
  GET: {
    params: { id: string };
    query: {
      limit?: number;
    };
    response: Activity[];
  };
}
```

### 3.6 Order Endpoints

```typescript
// GET /orders
// List orders with pagination
interface OrdersListEndpoint {
  GET: {
    query: {
      page?: number;
      pageSize?: number;
      sortBy?: string;
      sortOrder?: 'asc' | 'desc';
      search?: string;
      status?: OrderStatus[];
      customer?: string;
      dateFrom?: Date;
      dateTo?: Date;
    };
    response: PaginatedResponse<SalesOrder>;
  };
}

// GET /orders/:id
// Get order details
interface OrderDetailEndpoint {
  GET: {
    params: { id: string };
    response: SalesOrder;
  };
}

// POST /orders
// Create new order
interface OrderCreateEndpoint {
  POST: {
    request: Partial<SalesOrder>;
    response: SalesOrder;
  };
}

// PUT /orders/:id
// Update order
interface OrderUpdateEndpoint {
  PUT: {
    params: { id: string };
    request: Partial<SalesOrder>;
    response: SalesOrder;
  };
}

// PUT /orders/:id/status
// Update order status
interface OrderStatusUpdateEndpoint {
  PUT: {
    params: { id: string };
    request: { status: OrderStatus; reason?: string };
    response: SalesOrder;
  };
}

// POST /orders/:id/cancel
// Cancel order
interface OrderCancelEndpoint {
  POST: {
    params: { id: string };
    request: { reason?: string };
    response: SalesOrder;
  };
}
```

### 3.7 Sales Team Endpoints

```typescript
// GET /team
// List sales team members
interface TeamListEndpoint {
  GET: {
    query: {
      department?: string;
      status?: ('ACTIVE' | 'INACTIVE' | 'ON_LEAVE')[];
      territory?: string;
    };
    response: SalesRep[];
  };
}

// GET /team/:id
// Get team member details
interface TeamMemberDetailEndpoint {
  GET: {
    params: { id: string };
    response: SalesRep & { performance: PerformanceMetrics };
  };
}

// GET /team/:id/performance
// Get team member performance
interface TeamMemberPerformanceEndpoint {
  GET: {
    params: { id: string };
    query: {
      period?: string;
      startDate?: Date;
      endDate?: Date;
    };
    response: PerformanceMetrics;
  };
}

// GET /team/:id/commission
// Get team member commission
interface TeamMemberCommissionEndpoint {
  GET: {
    params: { id: string };
    query: {
      period?: string;
    };
    response: {
      totalEarned: number;
      pending: number;
      paid: number;
      transactions: CommissionTransaction[];
    };
  };
}
```

### 3.8 Territory & Quota Endpoints

```typescript
// GET /territories
// List territories
interface TerritoriesListEndpoint {
  GET: {
    query: {
      type?: string;
      parent?: string;
    };
    response: Territory[];
  };
}

// GET /territories/:id
// Get territory details
interface TerritoryDetailEndpoint {
  GET: {
    params: { id: string };
    response: Territory;
  };
}

// POST /territories
// Create territory
interface TerritoryCreateEndpoint {
  POST: {
    request: Partial<Territory>;
    response: Territory;
  };
}

// PUT /territories/:id
// Update territory
interface TerritoryUpdateEndpoint {
  PUT: {
    params: { id: string };
    request: Partial<Territory>;
    response: Territory;
  };
}

// GET /quotas
// List quotas
interface QuotasListEndpoint {
  GET: {
    query: {
      period?: string;
      assignee?: string;
      type?: ('REVENUE' | 'DEALS' | 'UNITS')[];
    };
    response: Quota[];
  };
}

// GET /quotas/:id
// Get quota details
interface QuotaDetailEndpoint {
  GET: {
    params: { id: string };
    response: Quota;
  };
}

// POST /quotas
// Create quota
interface QuotaCreateEndpoint {
  POST: {
    request: Partial<Quota>;
    response: Quota;
  };
}

// PUT /quotas/:id
// Update quota
interface QuotaUpdateEndpoint {
  PUT: {
    params: { id: string };
    request: Partial<Quota>;
    response: Quota;
  };
}
```

### 3.9 Forecasting Endpoints

```typescript
// GET /forecasts
// List forecasts
interface ForecastsListEndpoint {
  GET: {
    query: {
      period?: string;
      scenario?: ForecastScenario;
    };
    response: SalesForecast[];
  };
}

// POST /forecasts
// Generate new forecast
interface ForecastGenerateEndpoint {
  POST: {
    request: {
      period: ForecastPeriod;
      scenario?: ForecastScenario;
      options?: {
        includeBestCase?: boolean;
        includeWorstCase?: boolean;
        useHistoricalTrends?: boolean;
      };
    };
    response: SalesForecast;
  };
}

// GET /forecasts/:id
// Get forecast details
interface ForecastDetailEndpoint {
  GET: {
    params: { id: string };
    response: SalesForecast;
  };
}
```

### 3.10 Communication Endpoints

```typescript
// GET /communications/templates
// List email templates
interface TemplatesListEndpoint {
  GET: {
    query: {
      category?: string;
      isActive?: boolean;
    };
    response: EmailTemplate[];
  };
}

// POST /communications/templates
// Create email template
interface TemplateCreateEndpoint {
  POST: {
    request: Partial<EmailTemplate>;
    response: EmailTemplate;
  };
}

// GET /communications/campaigns
// List campaigns
interface CampaignsListEndpoint {
  GET: {
    query: {
      status?: string;
      type?: string;
    };
    response: Campaign[];
  };
}

// POST /communications/campaigns
// Create campaign
interface CampaignCreateEndpoint {
  POST: {
    request: Partial<Campaign>;
    response: Campaign;
  };
}

// POST /communications/send
// Send ad-hoc communication
interface SendCommunicationEndpoint {
  POST: {
    request: {
      type: 'EMAIL' | 'SMS';
      recipients: string[];
      templateId?: string;
      subject?: string;
      body?: string;
      variables?: Record<string, any>;
    };
    response: { sent: number; failed: number };
  };
}

// GET /activities
// List activities
interface ActivitiesListEndpoint {
  GET: {
    query: {
      entityType?: 'LEAD' | 'DEAL' | 'CUSTOMER';
      entityId?: string;
      type?: ActivityType[];
      limit?: number;
    };
    response: Activity[];
  };
}

// POST /activities
// Log activity
interface ActivityCreateEndpoint {
  POST: {
    request: Partial<Activity>;
    response: Activity;
  };
}
```

### 3.11 Sales Team Partners Endpoints

```typescript
// Sales Team Partner (Country-level view)
interface CountrySalesTeamPartner {
  id: string;
  partnerNumber: string;  // 'P-2001'
  userId: string;
  user: {
    firstName: string;
    lastName: string;
    email: string;
    phone: string;
    avatar?: string;
  };
  partnerType: PartnerType;
  territory?: Territory;
  status: PartnerStatus;
  tier: PartnerTier;

  // Performance metrics
  partnersRecruited: number;
  customersAcquired: number;
  pipelineValue: number;
  revenueGenerated: number;
  commissionEarned: number;
  referralBonusEarned: number;

  // AI Lead assignments
  assignedLeads: number;
  convertedLeads: number;
  leadConversionRate: number;

  // Approval info
  applicationId?: string;
  approvedAt?: Date;
  approvedBy?: string;

  createdAt: Date;
  updatedAt: Date;
  lastActivityAt?: Date;
}

type PartnerType =
  | 'COURIER'
  | 'HAULAGE'
  | 'WAREHOUSE'
  | 'ECOMMERCE'
  | 'AIR_OCEAN'
  | 'LOCATION_AGENT'
  | 'WHOLESALE'
  | 'INFLUENCER';

type PartnerStatus =
  | 'PENDING'
  | 'ACTIVE'
  | 'SUSPENDED'
  | 'TERMINATED';

type PartnerTier =
  | 'BRONZE'
  | 'SILVER'
  | 'GOLD'
  | 'PLATINUM';

// Partner Application (Country-level review)
interface CountryPartnerApplication {
  id: string;
  applicationNumber: string;  // 'PA-1001'
  applicant: {
    firstName: string;
    lastName: string;
    email: string;
    phone: string;
    address: Address;
  };
  partnerType: PartnerType;
  preferredTerritory?: string;

  // Supporting documents
  documents: ApplicationDocument[];

  // Country-level review
  countryReview: {
    status: 'PENDING' | 'APPROVED' | 'REJECTED' | 'ESCALATED';
    reviewedBy?: string;
    reviewedAt?: Date;
    reviewNotes?: string;
    rejectionReason?: string;
    territoryAssigned?: string;
  };

  // Background check
  backgroundCheck: {
    status: 'PENDING' | 'CLEARED' | 'FLAGGED';
    completedAt?: Date;
    notes?: string;
  };

  referredBy?: string;

  createdAt: Date;
  updatedAt: Date;
}

interface ApplicationDocument {
  id: string;
  type: 'ID_DOCUMENT' | 'PROOF_OF_ADDRESS' | 'BUSINESS_REG' | 'TAX_CERT' | 'OTHER';
  name: string;
  url: string;
  uploadedAt: Date;
  verified: boolean;
}

// Partner Commission (Country-level)
interface CountryPartnerCommission {
  id: string;
  partnerId: string;
  period: CommissionPeriod;

  // Referral bonus breakdown
  referralBonus: {
    partnersReferred: number;
    totalBonus: number;
    paidBonus: number;
    pendingBonus: number;
    breakdown: ReferralBonusBreakdown[];
  };

  // Sales commission breakdown
  salesCommission: {
    customersAcquired: number;
    totalRevenue: number;
    totalCommission: number;
    paidCommission: number;
    pendingCommission: number;
    breakdown: SalesCommissionBreakdown[];
  };

  // Payment info
  paymentInfo: {
    bankName?: string;
    accountNumber?: string;
    lastPaymentDate?: Date;
    nextPaymentDate?: Date;
  };
}

interface ReferralBonusBreakdown {
  referredPartnerId: string;
  referredPartnerName: string;
  bonusAmount: number;
  paidAt?: Date;
}

interface SalesCommissionBreakdown {
  customerId: string;
  customerName: string;
  dealValue: number;
  commissionRate: number;
  commissionAmount: number;
  dealClosedAt: Date;
  paidAt?: Date;
}

interface CommissionPeriod {
  type: 'MONTHLY' | 'QUARTERLY';
  start: Date;
  end: Date;
  label: string;
}

// Partner Territory Assignment
interface CountryPartnerTerritory {
  id: string;
  partnerId: string;
  territoryId: string;
  territory: Territory;
  exclusive: boolean;

  // Capacity
  capacity: {
    maxPartners: number;
    maxCustomers: number;
    currentPartners: number;
    currentCustomers: number;
  };

  // Lead allocation
  leadAllocation: {
    enabled: boolean;
    maxDailyLeads: number;
    currentDailyLeads: number;
  };

  assignedAt: Date;
  assignedBy: string;
  active: boolean;
}

// Partners Summary (Country-level)
interface CountryPartnersSummary {
  period: PeriodInfo;
  totalPartners: number;
  activePartners: number;
  pendingApplications: number;

  byTerritory: TerritoryPartnersSummary[];
  byPartnerType: PartnerTypeSummary[];
  byTier: PartnerTierSummary[];

  topPerformers: TopPartnerPerformer[];
  commissionStats: CommissionStats;
  territoryCapacity: TerritoryCapacity[];
}

interface TerritoryPartnersSummary {
  territory: Territory;
  totalPartners: number;
  activePartners: number;
  pipelineValue: number;
  commissionEarned: number;
}

interface PartnerTypeSummary {
  partnerType: PartnerType;
  count: number;
  pipelineValue: number;
  revenueGenerated: number;
  avgCommission: number;
}

interface PartnerTierSummary {
  tier: PartnerTier;
  count: number;
  revenueThreshold: number;
}

interface TopPartnerPerformer {
  partnerId: string;
  name: string;
  partnerType: PartnerType;
  tier: PartnerTier;
  territory: Territory;
  pipelineValue: number;
  commissionEarned: number;
  rank: number;
}

interface CommissionStats {
  totalEarned: number;
  totalPaid: number;
  totalPending: number;
  avgPayoutTime: number;
}

interface TerritoryCapacity {
  territory: Territory;
  maxPartners: number;
  currentPartners: number;
  availableSlots: number;
}
```

---

## 4. STATE MANAGEMENT

### 4.1 Store Structure (Zustand)

```typescript
// Main store structure
interface SalesDashboardStore {
  // Auth state
  auth: {
    user: CountrySalesUser | null;
    accessToken: string | null;
    isAuthenticated: boolean;
    permissions: Permission[];
  };

  // Dashboard state
  dashboard: {
    summary: DashboardSummary | null;
    loading: boolean;
    error: string | null;
    selectedPeriod: string;
  };

  // Leads state
  leads: {
    items: Lead[];
    selected: Lead | null;
    filters: LeadFilters;
    pagination: PaginationState;
    loading: boolean;
    error: string | null;
  };

  // Deals state
  deals: {
    items: Deal[];
    selected: Deal | null;
    pipelineStages: PipelineStage[];
    filters: DealFilters;
    pagination: PaginationState;
    loading: boolean;
    error: string | null;
  };

  // Customers state
  customers: {
    items: Customer[];
    selected: Customer | null;
    filters: CustomerFilters;
    pagination: PaginationState;
    loading: boolean;
    error: string | null;
  };

  // Orders state
  orders: {
    items: SalesOrder[];
    selected: SalesOrder | null;
    filters: OrderFilters;
    pagination: PaginationState;
    loading: boolean;
    error: string | null;
  };

  // Team state
  team: {
    members: SalesRep[];
    selected: SalesRep | null;
    performance: Map<string, PerformanceMetrics>;
    loading: boolean;
    error: string | null;
  };

  // Territories state
  territories: {
    items: Territory[];
    selected: Territory | null;
    quotas: Quota[];
    loading: boolean;
    error: string | null;
  };

  // Notifications state
  notifications: {
    items: Notification[];
    unreadCount: number;
  };

  // UI state
  ui: {
    sidebarOpen: boolean;
    selectedCountry: AssignedCountry;
    theme: 'light' | 'dark';
    language: string;
  };
}
```

### 4.2 Store Actions

```typescript
interface SalesDashboardActions {
  // Auth actions
  login: (credentials: LoginRequest) => Promise<void>;
  logout: () => void;
  refreshToken: () => Promise<void>;

  // Dashboard actions
  loadDashboardSummary: (period?: string) => Promise<void>;
  setSelectedPeriod: (period: string) => void;

  // Lead actions
  loadLeads: (filters?: LeadFilters) => Promise<void>;
  selectLead: (lead: Lead | null) => void;
  createLead: (data: Partial<Lead>) => Promise<Lead>;
  updateLead: (id: string, data: Partial<Lead>) => Promise<void>;
  deleteLead: (id: string) => Promise<void>;
  convertLeadToDeal: (id: string, dealData: any) => Promise<Deal>;
  assignLead: (id: string, ownerId: string) => Promise<void>;

  // Deal actions
  loadDeals: (filters?: DealFilters) => Promise<void>;
  selectDeal: (deal: Deal | null) => void;
  createDeal: (data: Partial<Deal>) => Promise<Deal>;
  updateDeal: (id: string, data: Partial<Deal>) => Promise<void>;
  updateDealStage: (id: string, stage: string) => Promise<void>;
  closeDeal: (id: string, outcome: 'WON' | 'LOST', reason?: string) => Promise<void>;
  deleteDeal: (id: string) => Promise<void>;

  // Customer actions
  loadCustomers: (filters?: CustomerFilters) => Promise<void>;
  selectCustomer: (customer: Customer | null) => void;
  createCustomer: (data: Partial<Customer>) => Promise<Customer>;
  updateCustomer: (id: string, data: Partial<Customer>) => Promise<void>;
  deleteCustomer: (id: string) => Promise<void>;

  // Order actions
  loadOrders: (filters?: OrderFilters) => Promise<void>;
  selectOrder: (order: SalesOrder | null) => void;
  createOrder: (data: Partial<SalesOrder>) => Promise<SalesOrder>;
  updateOrder: (id: string, data: Partial<SalesOrder>) => Promise<void>;
  cancelOrder: (id: string, reason?: string) => Promise<void>;

  // Team actions
  loadTeam: () => Promise<void>;
  loadMemberPerformance: (memberId: string) => Promise<void>;

  // Territory actions
  loadTerritories: () => Promise<void>;
  loadQuotas: () => Promise<void>;

  // Notification actions
  loadNotifications: () => Promise<void>;
  markNotificationRead: (id: string) => void;
  markAllNotificationsRead: () => void;

  // UI actions
  toggleSidebar: () => void;
  setTheme: (theme: 'light' | 'dark') => void;
}
```

### 4.3 React Query Keys

```typescript
// Query keys for React Query
const queryKeys = {
  // Auth
  auth: ['auth'] as const,
  me: ['me'] as const,

  // Dashboard
  dashboard: (period: string) => ['dashboard', period] as const,

  // Leads
  leads: (filters?: LeadFilters) => ['leads', filters] as const,
  lead: (id: string) => ['lead', id] as const,
  pipeline: (filters?: any) => ['pipeline', filters] as const,

  // Deals
  deals: (filters?: DealFilters) => ['deals', filters] as const,
  deal: (id: string) => ['deal', id] as const,

  // Customers
  customers: (filters?: CustomerFilters) => ['customers', filters] as const,
  customer: (id: string) => ['customer', id] as const,
  customerDeals: (id: string) => ['customer', id, 'deals'] as const,
  customerOrders: (id: string) => ['customer', id, 'orders'] as const,

  // Orders
  orders: (filters?: OrderFilters) => ['orders', filters] as const,
  order: (id: string) => ['order', id] as const,

  // Team
  team: () => ['team'] as const,
  teamMember: (id: string) => ['team', id] as const,
  memberPerformance: (id: string, period: string) => ['team', id, 'performance', period] as const,

  // Territories
  territories: () => ['territories'] as const,
  territory: (id: string) => ['territory', id] as const,
  quotas: () => ['quotas'] as const,

  // Forecasts
  forecasts: () => ['forecasts'] as const,
  forecast: (id: string) => ['forecast', id] as const,

  // Communications
  templates: () => ['templates'] as const,
  campaigns: () => ['campaigns'] as const,
  activities: (entityType: string, entityId: string) => ['activities', entityType, entityId] as const,

  // Notifications
  notifications: () => ['notifications'] as const,
};
```

---

## 5. MOCK DATA EXAMPLES

### 5.1 Mock User

```json
{
  "id": "usr-sales-001",
  "email": "john.doe@company.ng",
  "firstName": "John",
  "lastName": "Doe",
  "avatar": "https://cdn.company.com/avatars/john-doe.jpg",
  "role": "COUNTRY_SALES_DIRECTOR",
  "assignedCountry": {
    "code": "NG",
    "name": "Nigeria",
    "currency": "NGN",
    "timezone": "Africa/Lagos"
  },
  "department": "Enterprise Sales",
  "location": "Lagos",
  "permissions": [
    {
      "resource": "leads",
      "actions": ["create", "read", "update", "delete"]
    },
    {
      "resource": "deals",
      "actions": ["create", "read", "update", "delete", "approve"]
    },
    {
      "resource": "customers",
      "actions": ["create", "read", "update", "delete"]
    },
    {
      "resource": "quotas",
      "actions": ["create", "read", "update", "delete"]
    }
  ],
  "timezone": "Africa/Lagos",
  "createdAt": "2023-01-15T00:00:00Z",
  "lastLoginAt": "2025-02-08T08:30:00Z"
}
```

### 5.2 Mock Dashboard Summary

```json
{
  "period": {
    "start": "2025-02-01T00:00:00Z",
    "end": "2025-02-08T23:59:59Z",
    "type": "month"
  },
  "metrics": {
    "revenue": {
      "value": 1200000,
      "label": "₦1.2M",
      "change": 12,
      "changeType": "increase",
      "previousPeriod": 1071428
    },
    "pipeline": {
      "value": 3500000,
      "label": "₦3.5M",
      "change": 8,
      "changeType": "increase",
      "previousPeriod": 3240740
    },
    "dealsClosed": {
      "value": 127,
      "label": "127",
      "change": 15,
      "changeType": "increase",
      "previousPeriod": 110
    },
    "avgDealSize": {
      "value": 45000,
      "label": "₦45K",
      "change": -3,
      "changeType": "decrease",
      "previousPeriod": 46391
    }
  },
  "performance": {
    "quotaAttainment": 95,
    "winRate": 32,
    "avgDealCycle": 28,
    "pipelineVelocity": 45000
  },
  "topPerformers": [
    {
      "userId": "usr-001",
      "name": "John Doe",
      "avatar": "https://cdn.company.com/avatars/john.jpg",
      "department": "Enterprise",
      "revenue": 250000,
      "quota": 200000,
      "attainment": 125,
      "dealsClosed": 15
    },
    {
      "userId": "usr-002",
      "name": "Jane Smith",
      "avatar": "https://cdn.company.com/avatars/jane.jpg",
      "department": "SMB",
      "revenue": 180000,
      "quota": 150000,
      "attainment": 120,
      "dealsClosed": 22
    },
    {
      "userId": "usr-003",
      "name": "Bob Johnson",
      "avatar": "https://cdn.company.com/avatars/bob.jpg",
      "department": "Retail",
      "revenue": 145000,
      "quota": 150000,
      "attainment": 97,
      "dealsClosed": 28
    }
  ],
  "recentDeals": [
    {
      "id": "deal-001",
      "name": "Enterprise Software License",
      "company": "Acme Industries",
      "value": 250000,
      "stage": "Negotiation",
      "probability": 75,
      "expectedCloseDate": "2025-02-15T00:00:00Z",
      "owner": {
        "id": "usr-001",
        "name": "John Doe"
      }
    },
    {
      "id": "deal-002",
      "name": "Annual Support Package",
      "company": "TechCorp Nigeria",
      "value": 180000,
      "stage": "Proposal",
      "probability": 50,
      "expectedCloseDate": "2025-02-20T00:00:00Z",
      "owner": {
        "id": "usr-002",
        "name": "Jane Smith"
      }
    }
  ],
  "alerts": [
    {
      "id": "alert-001",
      "type": "WARNING",
      "title": "Deals at Risk",
      "message": "5 deals are approaching their close date with low probability",
      "actionUrl": "/deals?status=at-risk",
      "createdAt": "2025-02-08T09:00:00Z"
    },
    {
      "id": "alert-002",
      "type": "INFO",
      "title": "Pending Approvals",
      "message": "3 deal approvals awaiting your review",
      "actionUrl": "/approvals",
      "createdAt": "2025-02-08T08:30:00Z"
    }
  ]
}
```

### 5.3 Mock Lead

```json
{
  "id": "lead-001",
  "leadNumber": "L-1001",
  "company": "Acme Industries Nigeria",
  "contact": {
    "firstName": "John",
    "lastName": "Smith",
    "email": "john.smith@acme-ng.com",
    "phone": "+234-801-234-5678",
    "title": "Procurement Manager"
  },
  "status": "QUALIFIED",
  "source": "WEBSITE",
  "score": 85,
  "owner": {
    "id": "usr-001",
    "firstName": "John",
    "lastName": "Doe",
    "email": "john.doe@company.ng",
    "avatar": "https://cdn.company.com/avatars/john-doe.jpg"
  },
  "territory": {
    "id": "territory-sw-001",
    "name": "South West",
    "code": "SW"
  },
  "estimatedValue": 50000,
  "currency": "NGN",
  "probability": 30,
  "expectedCloseDate": "2025-03-15T00:00:00Z",
  "stage": "Qualified",
  "tags": ["enterprise", "hot-lead", "software"],
  "customFields": {
    "companySize": "250-500",
    "industry": "Technology",
    "budgetConfirmed": true
  },
  "activities": [
    {
      "id": "act-001",
      "type": "CALL",
      "subject": "Discovery call completed",
      "description": "Discussed requirements and budget",
      "entityType": "LEAD",
      "entityId": "lead-001",
      "createdBy": "usr-001",
      "createdAt": "2025-02-07T14:30:00Z",
      "callData": {
        "duration": 25,
        "outcome": "CONNECTED",
        "notes": "Very interested in enterprise solution"
      }
    }
  ],
  "createdAt": "2025-02-01T10:00:00Z",
  "updatedAt": "2025-02-07T14:30:00Z"
}
```

### 5.4 Mock Deal

```json
{
  "id": "deal-001",
  "dealNumber": "D-2501",
  "name": "Enterprise Software License",
  "companyId": "cust-001",
  "contact": {
    "firstName": "John",
    "lastName": "Smith",
    "email": "john.smith@acme-ng.com",
    "phone": "+234-801-234-5678",
    "title": "Procurement Manager"
  },
  "status": "ACTIVE",
  "stage": "Negotiation",
  "probability": 75,
  "value": 250000,
  "discountAmount": 0,
  "discountPercentage": 0,
  "netValue": 250000,
  "currency": "NGN",
  "owner": {
    "id": "usr-001",
    "firstName": "John",
    "lastName": "Doe",
    "email": "john.doe@company.ng"
  },
  "territory": {
    "id": "territory-sw-001",
    "name": "South West",
    "code": "SW"
  },
  "expectedCloseDate": "2025-02-15T00:00:00Z",
  "products": [
    {
      "id": "line-001",
      "productCode": "SW-ENT-001",
      "productName": "Enterprise Software License",
      "description": "Annual enterprise license for 100 users",
      "quantity": 1,
      "unitPrice": 200000,
      "discount": 0,
      "tax": 15000,
      "total": 215000
    },
    {
      "id": "line-002",
      "productCode": "SV-IMP-001",
      "productName": "Implementation Service",
      "description": "Setup and training (20 hours)",
      "quantity": 1,
      "unitPrice": 50000,
      "discount": 0,
      "tax": 3750,
      "total": 53750
    }
  ],
  "commission": {
    "rate": 10,
    "amount": 25000,
    "quotaCredit": 250000,
    "eligibleRepIds": ["usr-001"]
  },
  "createdAt": "2025-02-01T10:00:00Z",
  "updatedAt": "2025-02-08T10:30:00Z",
  "lastStageChangeAt": "2025-02-05T14:00:00Z",
  "daysInCurrentStage": 3,
  "activities": [],
  "nextSteps": [
    "Submit revised proposal with discount options",
    "Schedule follow-up meeting for Feb 10",
    "Get legal review of contract terms"
  ]
}
```

### 5.5 Mock Customer

```json
{
  "id": "cust-001",
  "customerNumber": "C-5001",
  "type": "CORPORATE",
  "status": "ACTIVE",
  "tier": "PLATINUM",
  "corporateInfo": {
    "companyName": "Acme Industries Nigeria",
    "registrationNumber": "RC-123456",
    "taxId": "TIN-789012",
    "industry": "Technology",
    "employeeCount": 350,
    "website": "www.acme-ng.com",
    "headquarters": "Lagos, Nigeria"
  },
  "contact": {
    "email": "contact@acme-ng.com",
    "phone": "+234-1-234-5678",
    "address": {
      "line1": "Plot 123, Adetokunbo Ademola Street",
      "line2": "Victoria Island",
      "city": "Lagos",
      "state": "Lagos",
      "postalCode": "101241",
      "country": "Nigeria"
    },
    "billingAddress": {
      "line1": "Plot 123, Adetokunbo Ademola Street",
      "line2": "Victoria Island",
      "city": "Lagos",
      "state": "Lagos",
      "postalCode": "101241",
      "country": "Nigeria"
    },
    "shippingAddress": {
      "line1": "Plot 123, Adetokunbo Ademola Street",
      "line2": "Victoria Island",
      "city": "Lagos",
      "state": "Lagos",
      "postalCode": "101241",
      "country": "Nigeria"
    }
  },
  "accountOwner": {
    "id": "usr-001",
    "firstName": "John",
    "lastName": "Doe"
  },
  "territory": {
    "id": "territory-sw-001",
    "name": "South West"
  },
  "source": "WEBSITE",
  "tags": ["enterprise", "technology", "repeat-customer"],
  "lifetimeValue": 850000,
  "annualRecurringValue": 250000,
  "totalPurchases": 12,
  "averageOrderValue": 70833,
  "firstPurchaseDate": "2023-01-15T00:00:00Z",
  "lastPurchaseDate": "2025-02-01T00:00:00Z",
  "nextRenewalDate": "2025-03-01T00:00:00Z",
  "paymentTerms": "Net 30",
  "creditLimit": 500000,
  "outstandingBalance": 0,
  "deals": [],
  "orders": [],
  "customFields": {
    "decisionMaker": "John Smith",
    "accountManagerNotes": "Key account - prioritize support",
    "preferredContactMethod": "email"
  },
  "createdAt": "2023-01-15T00:00:00Z",
  "updatedAt": "2025-02-08T10:00:00Z"
}
```

### 5.6 Mock Sales Order

```json
{
  "id": "order-001",
  "orderNumber": "SO-2501",
  "dealId": "deal-001",
  "customerId": "cust-001",
  "customerName": "Acme Industries Nigeria",
  "status": "CONFIRMED",
  "orderDate": "2025-02-08T10:00:00Z",
  "expectedDeliveryDate": "2025-02-15T00:00:00Z",
  "items": [
    {
      "id": "order-line-001",
      "productCode": "SW-ENT-001",
      "productName": "Enterprise Software License",
      "description": "Annual enterprise license for 100 users",
      "quantity": 1,
      "unitPrice": 200000,
      "discount": 0,
      "tax": 15000,
      "total": 215000
    },
    {
      "id": "order-line-002",
      "productCode": "SV-IMP-001",
      "productName": "Implementation Service",
      "description": "Setup and training (20 hours)",
      "quantity": 1,
      "unitPrice": 50000,
      "discount": 0,
      "tax": 3750,
      "total": 53750
    }
  ],
  "subtotal": 250000,
  "discountAmount": 0,
  "taxAmount": 18750,
  "total": 268750,
  "currency": "NGN",
  "shippingAddress": {
    "line1": "Plot 123, Adetokunbo Ademola Street",
    "line2": "Victoria Island",
    "city": "Lagos",
    "state": "Lagos",
    "postalCode": "101241",
    "country": "Nigeria"
  },
  "shippingMethod": "Standard",
  "shippingCost": 0,
  "trackingNumber": null,
  "billingAddress": {
    "line1": "Plot 123, Adetokunbo Ademola Street",
    "line2": "Victoria Island",
    "city": "Lagos",
    "state": "Lagos",
    "postalCode": "101241",
    "country": "Nigeria"
  },
  "paymentTerms": "Net 30",
  "paymentStatus": "PENDING",
  "paidAmount": 0,
  "salesRep": {
    "id": "usr-001",
    "firstName": "John",
    "lastName": "Doe"
  },
  "notes": "Please deliver by February 15",
  "attachments": [],
  "createdAt": "2025-02-08T10:00:00Z",
  "updatedAt": "2025-02-08T10:00:00Z",
  "createdBy": "usr-001"
}
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
| AUTH_001 | 401 | Invalid or expired token |
| AUTH_002 | 403 | Insufficient permissions |
| AUTH_003 | 401 | Invalid credentials |
| VAL_001 | 400 | Validation error |
| VAL_002 | 400 | Missing required field |
| VAL_003 | 400 | Invalid format |
| RES_001 | 404 | Resource not found |
| RES_002 | 409 | Resource already exists |
| RES_003 | 409 | Resource conflict |
| SRV_001 | 500 | Internal server error |
| SRV_002 | 503 | Service unavailable |

### 6.3 Error Response Examples

```json
{
  "success": false,
  "error": {
    "code": "AUTH_001",
    "message": "Invalid or expired access token",
    "timestamp": "2025-02-08T10:30:00Z",
    "requestId": "req-abc123"
  }
}

{
  "success": false,
  "error": {
    "code": "VAL_001",
    "message": "Validation failed",
    "details": {
      "errors": [
        {
          "field": "email",
          "message": "Email is required"
        },
        {
          "field": "estimatedValue",
          "message": "Value must be greater than 0"
        }
      ]
    },
    "timestamp": "2025-02-08T10:30:00Z",
    "requestId": "req-abc123"
  }
}
```

---

## VERSION HISTORY

| Version | Date | Changes |
|---------|------|---------|
| 1.0 | 2025-02-08 | Initial Country Sales Dashboard Mock Flow Documentation |

---

## NEXT STEPS

- Complete 04_Page_By_Page_Flow_Documentation.md (Detailed page flows)

---

**Document End**
