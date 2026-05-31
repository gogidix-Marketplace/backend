# FIELD SALES REPRESENTATIVE APP - MOCK FLOW DOCUMENTATION

**Version:** 1.0
**Domain:** Business Domain
**Subdomain:** Country-Sales-Dashboard
**Frontend:** field-sales-rep-app (Mobile)
**Date:** 2026-03-08

---

## TABLE OF CONTENTS

1. [Mock Data Overview](#mock-data-overview)
2. [API Flow Diagrams](#api-flow-diagrams)
3. [Mock Data Definitions](#mock-data-definitions)
4. [State Management Flow](#state-management-flow)
5. [Offline Data Sync](#offline-data-sync)
6. [Location Services Flow](#location-services-flow)

---

## 1. MOCK DATA OVERVIEW

### 1.1 Data Flow Architecture

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                      FIELD SALES REP APP DATA FLOW                          │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│  ┌─────────────┐     ┌─────────────┐     ┌─────────────┐                 │
│  │   Mobile    │────▶│   API       │────▶│  Backend    │                 │
│  │   App       │◀────│  Gateway    │◀────│  Services   │                 │
│  │  (React     │     │            │     │             │                 │
│  │   Native)   │     └─────┬──────┘     └─────┬───────┘                 │
│  └──────┬──────┘           │                   │                          │
│         │                  ▼                   ▼                          │
│  ┌─────┴─────┐     ┌─────────────┐     ┌─────────────┐                 │
│  │  Redux     │     │   AsyncStorage│    │  MongoDB    │                 │
│  │  Store     │     │   (Offline)   │    │  Database   │                 │
│  └─────┬──────┘     └─────────────┘     └─────┬───────┘                 │
│        │                                   │                          │
│        ▼                                   ▼                          │
│  ┌─────────────────────────────────────────────────────────────┐    │
│  │                    Location Services                        │    │
│  │  • GPS tracking                                                   │    │
│  │  • Geofencing                                                     │    │
│  │  • Check-in/Check-out                                             │    │
│  └─────────────────────────────────────────────────────────────┘    │
│                              │                                          │
│                              ▼                                          │
│  ┌─────────────────────────────────────────────────────────────┐    │
│  │                    Country Sales Dashboard API                 │    │
│  │  POST /api/v1/sales/leads/create                                │    │
│  │  POST /api/v1/sales/partners/register                           │    │
│  │  POST /api/v1/sales/orders/create                              │    │
│  │  POST /api/v1/sales/checkin                                    │    │
│  │  GET  /api/v1/sales/performance                               │    │
│  └─────────────────────────────────────────────────────────────┘    │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

### 1.2 API Endpoints Summary

| Endpoint | Method | Purpose | Response Type |
|----------|--------|---------|---------------|
| **Authentication** |
| `/api/v1/sales/auth/login` | POST | Authenticate sales rep | `{ token, user, territory }` |
| `/api/v1/sales/auth/refresh` | POST | Refresh JWT token | `{ token }` |
| `/api/v1/sales/auth/logout` | POST | Logout | `{ success }` |
| **Routes & Visits** |
| `/api/v1/sales/routes/today` | GET | Get today's route | `RouteDTO` |
| `/api/v1/sales/routes/optimize` | POST | Optimize route | `OptimizedRouteDTO` |
| `/api/v1/sales/checkin` | POST | Check in at location | `CheckinDTO` |
| `/api/v1/sales/checkout` | POST | Check out from location | `CheckoutDTO` |
| `/api/v1/sales/visits/history` | GET | Get visit history | `Visit[]` |
| **Partners** |
| `/api/v1/sales/partners/directory` | GET | Get partners in territory | `PartnerSummary[]` |
| `/api/v1/sales/partners/{id}` | GET | Get partner details | `PartnerDetailDTO` |
| `/api/v1/sales/partners/register` | POST | Register new partner | `PartnerApplicationDTO` |
| `/api/v1/sales/partners/applications` | GET | Get pending applications | `PartnerApplication[]` |
| `/api/v1/sales/partners/{id}/status` | GET | Get application status | `ApplicationStatusDTO` |
| **Leads** |
| `/api/v1/sales/leads/mine` | GET | Get my leads | `Lead[]` |
| `/api/v1/sales/leads/create` | POST | Create new lead | `LeadDTO` |
| `/api/v1/sales/leads/{id}` | GET | Get lead details | `LeadDetailDTO` |
| `/api/v1/sales/leads/{id}` | PUT | Update lead | `LeadDTO` |
| `/api/v1/sales/leads/convert` | POST | Convert lead to opportunity | `OpportunityDTO` |
| **Orders** |
| `/api/v1/sales/products/catalog` | GET | Get product catalog | `Product[]` |
| `/api/v1/sales/orders/create` | POST | Create new order | `OrderDTO` |
| `/api/v1/sales/orders/mine` | GET | Get my orders | `Order[]` |
| `/api/v1/sales/orders/{id}` | GET | Get order details | `OrderDetailDTO` |
| `/api/v1/sales/orders/{id}/track` | GET | Track order | `OrderTrackingDTO` |
| **Customers** |
| `/api/v1/sales/customers/directory` | GET | Get customers in territory | `Customer[]` |
| `/api/v1/sales/customers/{id}` | GET | Get customer details | `CustomerDetailDTO` |
| `/api/v1/sales/customers/{id}/visits` | GET | Get customer visit history | `Visit[]` |
| **Performance** |
| `/api/v1/sales/performance/summary` | GET | Get performance summary | `PerformanceSummaryDTO` |
| `/api/v1/sales/performance/commission` | GET | Get commission data | `CommissionDTO` |
| `/api/v1/sales/performance/leaderboard` | GET | Get leaderboard | `LeaderboardDTO` |
| **Expenses** |
| `/api/v1/sales/expenses/create` | POST | Submit expense | `ExpenseDTO` |
| `/api/v1/sales/expenses/mine` | GET | Get my expenses | `Expense[]` |
| `/api/v1/sales/expenses/pending` | GET | Get pending reimbursements | `Expense[]` |
| **Communications** |
| `/api/v1/sales/notifications` | GET | Get notifications | `Notification[]` |
| `/api/v1/sales/messages` | GET | Get messages | `Message[]` |
| `/api/v1/sales/announcements` | GET | Get announcements | `Announcement[]` |

---

## 2. API FLOW DIAGRAMS

### 2.1 App Initialization Flow

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                        APP INITIALIZATION FLOW                            │
└─────────────────────────────────────────────────────────────────────────────┘

App launched
      │
      ▼
┌─────────────────────────────────────────┐
│ Check AsyncStorage for Auth Token        │
│ • Token exists?                        │
│ • Token valid?                          │
└─────────────────────────────────────────┘
      │
      ├─ Valid Token ──────────────────────────────────────────────────────┐
      │                                                                     │
      ▼                                                                     │
┌─────────────────────────────────────────┐                                 │
│ Load User Data from Redux Persist          │                                 │
│ • User profile                          │                                 │
│ • Territory assignment                  │                                 │
│ • Permissions                          │                                 │
└─────────────────────────────────────────┘                                 │
      │                                                                     │
      ▼                                                                     │
┌─────────────────────────────────────────┐                                 │
│ Fetch Initial Data (Parallel)             │                                 │
│ • GET /routes/today                     │                                 │
│ • GET /performance/summary               │                                 │
│ • GET /notifications (unread count)      │                                 │
│ • GET /announcements                    │                                 │
└─────────────────────────────────────────┘                                 │
      │                                                                     │
      ▼                                                                     │
┌─────────────────────────────────────────┐                                 │
│ Populate Redux Store                       │                                 │
│ • setRouteData(route)                   │                                 │
│ • setPerformanceData(performance)        │                                 │
│ • setNotifications(notifications)       │                                 │
└─────────────────────────────────────────┘                                 │
      │                                                                     │
      ▼                                                                     │
│ Navigate to Home Screen ────────────────────────────────────────────────────│
                                                                            │
      └─ No Token / Invalid Token ──────────────────────────────────────────┤
                                                                           │
      ▼                                                                     │
┌─────────────────────────────────────────┐                                 │
│ Show Login Screen                        │                                 │
│ • Email / Employee ID                   │                                 │
│ • Password / PIN                         │                                 │
│ • Biometric option (if enabled)         │                                 │
│ • SSO option (if configured)            │                                 │
└─────────────────────────────────────────┘                                 │
                                                                            │
└───────────────────────────────────────────────────────────────────────────┘
```

### 2.2 Check-in Flow with Location Verification

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                        CHECK-IN WITH LOCATION VERIFICATION                   │
└─────────────────────────────────────────────────────────────────────────────┘

User taps "Check In" at customer/partner location
      │
      ▼
┌─────────────────────────────────────────┐
│ Get Current GPS Location                 │
│ • latitude, longitude                   │
│ • accuracy (meters)                     │
└─────────────────────────────────────────┘
      │
      ▼
┌─────────────────────────────────────────┐
│ Geofence Verification                    │
│ POST /api/v1/sales/checkin              │
│ {                                        │
│   salesRepId: "SR-NGA-001",            │
│   location: {                          │
│     latitude: 6.5244,                  │
│     longitude: 3.3792,                 │
│     accuracy: 15.5                      │
│   },                                   │
│   customerId: "CUST-1234",             │
│   partnerId: "PART-5678",               │
│   visitPurpose: "SALES_CALL"           │
│ }                                        │
└─────────────────────────────────────────┘
      │
      ▼
┌─────────────────────────────────────────┐
│ Backend Verification                     │
│ • Get customer/partner location           │
│ • Calculate distance from GPS            │
│ • Within tolerance? (100m default)       │
│ • Check if already checked in today       │
└─────────────────────────────────────────┘
      │
      ├─ Within Tolerance ───────────────────────────────────────────────┐
      │                                                                     │
      ▼                                                                     │
┌─────────────────────────────────────────┐                                 │
│ Check-in Successful                       │                                 │
│ {                                        │                                 │
│   success: true,                        │                                 │
│   checkinId: "CHK-20260308-001",       │                                 │
│   timestamp: "2026-03-08T09:15:23Z",  │                                 │
│   location: {                          │                                 │
│     verified: true,                    │                                 │
│     distance: 15.2                     │                                 │
│   }                                     │                                 │
│ }                                        │                                 │
└─────────────────────────────────────────┘                                 │
      │                                                                     │
      ▼                                                                     │
│ Start Visit Timer ───────────────────────────────────────────────────────│
      • Display visit duration                                     │
      • Enable visit actions (notes, photos, orders)                        │
                                                                            │
      └─ Outside Tolerance ────────────────────────────────────────────────┤
                                                                           │
      ▼                                                                     │
│ Verification Failed                       │
│ {                                        │
│   success: false,                       │
│   error: "LOCATION_MISMATCH",            │
│   message: "You appear to be 250m away │  │
│            from the registered address",│  │
│   currentLocation: {                    │  │
│     latitude: 6.5267,                  │  │
│     longitude: 3.3811                  │  │
│   },                                   │  │
│   expectedLocation: {                   │  │
│     latitude: 6.5244,                  │  │
│     longitude: 3.3792                  │  │
│   }                                     │  │
│ }                                        │  │
│                                         │  │
│ Show Options:                           │  │
│ • [Override] - Manager approval only    │  │
│ • [Update Location] - Add new address    │  │
│ • [Cancel]                             │  │
│                                         │  │
└───────────────────────────────────────────────────────────────────────────┘
```

### 2.3 Partner Onboarding Flow

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                         PARTNER ONBOARDING FLOW                             │
└─────────────────────────────────────────────────────────────────────────────┘

User completes partner onboarding form
      │
      ▼
┌─────────────────────────────────────────┐
│ Submit Partner Application                │
│ POST /api/v1/sales/partners/register     │
│ {                                        │
│   partnerType: "COURIER_SME",          │
│   businessName: "Express Deliveries",  │
│   businessInfo: {                      │
│     registrationNumber: "RC123456",   │
│     businessType: "LLC",              │
│     contactPerson: "John Doe",       │
│     phone: "+2348012345678",          │
│     email: "john@expressdel.com",     │
│     address: {                       │
│       street: "123 Commerce St",      │
│       city: "Lagos",                  │
│       state: "Lagos Island",          │
│       country: "Nigeria",             │
│       latitude: 6.5244,               │
│       longitude: 3.3792               │
│     }                                   │
│   },                                   │
│   services: ["SAME_DAY", "STANDARD"],│
│   documents: [                         │
│     {                                  │
│       type: "BUSINESS_REGISTRATION",│
│       url: "https://s3.../rc.pdf", │
│       size: 245678                     │
│     }                                  │
│   ],                                   │
│   photos: [                            │
│     {                                  │
│       type: "BUSINESS_PREMISES",   │
│       url: "https://s3.../photo1.jpg"│
│       geoTag: {                        │
│         latitude: 6.5244,              │
│         longitude: 3.3792              │
│       }                                 │
│     }                                  │
│   ]                                     │
│ }                                        │
└─────────────────────────────────────────┘
      │
      ▼
┌─────────────────────────────────────────┐
│ Backend Processing                       │
│ • Validate business data                  │
│ • Verify document uploads               │
│ • Store in database                     │
│ • Trigger KYC process                   │
│ • Assign to Country Sales Manager       │
│ • Generate application reference         │
└─────────────────────────────────────────┘
      │
      ▼
┌─────────────────────────────────────────┐
│ Application Submitted                     │
│ {                                        │
│   success: true,                        │
│   applicationId: "APP-20260308-001",  │
│   referenceNumber: "REF-EXP-001",     │
│   status: "PENDING_REVIEW",            │
│   estimatedProcessingTime: "2-3 days", │
│   assignedTo: "Sarah Okon"             │
│ }                                        │
└─────────────────────────────────────────┘
      │
      ▼
┌─────────────────────────────────────────┐
│ Store Locally for Tracking                │
│ Redux Store:                           │
│ • Add to pending applications            │
│ • Set status to "PENDING_REVIEW"        │
│ AsyncStorage:                           │
│ • Save offline for tracking             │
└─────────────────────────────────────────┘
      │
      ▼
┌─────────────────────────────────────────┐
│ Show Confirmation to User                  │
│ "Partner Application Submitted!"          │
│ • Reference: REF-EXP-001               │
│ • Processing time: 2-3 business days     │
│ • You'll be notified when approved        │
│ [Track Status] [Add Another Partner]      │
└─────────────────────────────────────────┘
```

---

## 3. MOCK DATA DEFINITIONS

### 3.1 TypeScript Interfaces

```typescript
// ============================================
// CORE TYPES
// ============================================

interface SalesRepUser {
  id: string;
  employeeId: string;
  firstName: string;
  lastName: string;
  email: string;
  phone: string;
  avatar?: string;
  role: SalesRepRole;
  territory: Territory;
  targets: SalesTargets;
  permissions: SalesRepPermission[];
  settings: UserSettings;
}

type SalesRepRole =
  | 'SALES_EXECUTIVE'
  | 'SALES_REPRESENTATIVE'
  | 'FIELD_SALES_AGENT';

interface Territory {
  id: string;
  name: string;
  type: 'CITY' | 'REGION' | 'POSTAL_CODES';
  code: string;
  country: string;
  bounds?: TerritoryBounds;
  partnersCount: number;
  customersCount: number;
}

interface TerritoryBounds {
  northeast: { latitude: number; longitude: number };
  northwest: { latitude: number; longitude: number };
  southeast: { latitude: number; longitude: number };
  southwest: { latitude: number; longitude: number };
}

interface SalesTargets {
  monthly: RevenueTarget;
  partners: PartnerTarget;
  leads: LeadTarget;
  orders: OrderTarget;
}

interface RevenueTarget {
  target: number;
  currency: string;
  achieved: number;
  percentAchieved: number;
  trend: number;
}

interface PartnerTarget {
  target: number;
  achieved: number;
  percentAchieved: number;
  trend: number;
}

interface LeadTarget {
  target: number;
  achieved: number;
  qualified: number;
  percentAchieved: number;
}

interface OrderTarget {
  target: number;
  achieved: number;
  value: number;
  percentAchieved: number;
}

interface SalesRepPermission {
  resource: string;
  actions: ('view' | 'create' | 'edit' | 'delete' | 'approve' | 'submit')[];
}

interface UserSettings {
  notifications: NotificationSettings;
  preferences: AppPreferences;
  offline: OfflineSettings;
}

// ============================================
// ROUTE & VISIT TYPES
// ============================================

interface RouteDTO {
  routeId: string;
  date: string;
  salesRepId: string;
  status: 'PLANNED' | 'IN_PROGRESS' | 'COMPLETED';
  stops: RouteStop[];
  totalStops: number;
  completedStops: number;
  estimatedDuration: number;
  actualDuration?: number;
  distance: number;
  optimized: boolean;
}

interface RouteStop {
  stopId: string;
  sequence: number;
  customer?: CustomerSummary;
  partner?: PartnerSummary;
  type: StopType;
  address: Address;
  purpose: VisitPurpose;
  scheduledTime: string;
  estimatedArrival?: string;
  estimatedDuration?: number;
  status: StopStatus;
  checkIn?: CheckIn;
  checkOut?: CheckOut;
  notes?: VisitNote[];
  tasks?: VisitTask[];
  metadata?: Record<string, any>;
}

type StopType =
  | 'SALES_CALL'
  | 'PARTNER_REVIEW'
  | 'DELIVERY'
  | 'PICKUP'
  | 'PRODUCT_PRESENTATION'
  | 'CONTRACT_SIGNING'
  | 'FOLLOW_UP'
  | 'TRAINING';

type VisitPurpose =
  | 'NEW_SALES'
  | 'RELATIONSHIP_BUILDING'
  | 'PRODUCT_PRESENTATION'
  | 'CLOSING_DEAL'
  | 'DELIVERY'
  | 'ISSUE_RESOLUTION'
  | 'TRAINING'
  | 'REVIEW';

type StopStatus =
  | 'PENDING'
  | 'IN_PROGRESS'
  | 'COMPLETED'
  | 'SKIPPED';

interface CheckIn {
  checkInId: string;
  timestamp: string;
  location: GeoLocation;
  verified: boolean;
  distanceFromTarget?: number;
  note?: string;
}

interface CheckOut {
  checkOutId: string;
  timestamp: string;
  location?: GeoLocation;
  duration: number; // minutes
  finalNotes?: string;
  nextAction?: string;
}

interface VisitNote {
  noteId: string;
  timestamp: string;
  type: 'TEXT' | 'VOICE' | 'PHOTO';
  content: string;
  author: string;
  attachments?: string[];
}

interface VisitTask {
  taskId: string;
  title: string;
  description?: string;
  status: 'PENDING' | 'IN_PROGRESS' | 'COMPLETED';
  due?: string;
  completedAt?: string;
}

interface CustomerSummary {
  customerId: string;
  name: string;
  type: 'CUSTOMER' | 'PARTNER';
  address: Address;
  phone?: string;
  email?: string;
}

interface PartnerSummary {
  partnerId: string;
  name: string;
  type: PartnerType;
  tier: PartnerTier;
  address: Address;
  phone?: string;
  email?: string;
}

type PartnerType =
  | 'COURIER_PARTNER'
  | 'HAULAGE_PARTNER'
  | 'WAREHOUSE_PARTNER'
  | 'ECOMMERCE_VENDOR'
  | 'LOGISTICS_PROVIDER';

type PartnerTier =
  | 'BRONZE'
  | 'SILVER'
  | 'GOLD'
  | 'PLATINUM';

// ============================================
// PARTNER TYPES
// ============================================

interface PartnerDetailDTO {
  partnerId: string;
  businessName: string;
  partnerType: PartnerType;
  tier: PartnerTier;
  status: PartnerStatus;
  registrationNumber?: string;
  businessType?: string;
  contactInfo: ContactInfo;
  address: Address;
  services: PartnerService[];
  territory: Territory;
  salesRepId: string;
  salesRepName: string;
  assignedDate: string;
  performanceMetrics?: PartnerPerformance;
  commissionTier?: number;
  documents: PartnerDocument[];
  photos: PartnerPhoto[];
  createdAt: string;
  updatedAt: string;
}

type PartnerStatus =
  | 'ACTIVE'
  | 'PENDING'
  | 'SUSPENDED'
  | 'TERMINATED';

interface ContactInfo {
  primaryContact: string;
  phone: string;
  email: string;
  secondaryContact?: string;
  secondaryPhone?: string;
  secondaryEmail?: string;
}

interface PartnerService {
  serviceId: string;
  serviceName: string;
  serviceCategory: string;
  status: 'ACTIVE' | 'INACTIVE';
}

interface PartnerPerformance {
  ordersThisMonth: number;
  orderValueThisMonth: number;
  totalOrders: number;
  totalValue: number;
  avgRating: number;
  lastOrderDate: string;
}

interface PartnerDocument {
  documentId: string;
  type: DocumentType;
  name: string;
  url: string;
  size: number;
  status: 'PENDING' | 'VERIFIED' | 'REJECTED';
  uploadedAt: string;
  verifiedAt?: string;
  rejectionReason?: string;
}

type DocumentType =
  | 'BUSINESS_REGISTRATION'
  | 'TAX_CERTIFICATE'
  | 'INSURANCE_CERTIFICATE'
  | 'ID_DOCUMENT'
  | 'PROOF_OF_ADDRESS'
  | 'OPERATING_LICENSE'
  | 'OTHER';

interface PartnerPhoto {
  photoId: string;
  type: PhotoType;
  url: string;
  geoTag?: GeoLocation;
  capturedAt: string;
  capturedBy: string;
  description?: string;
}

type PhotoType =
  | 'BUSINESS_PREMISES'
  | 'FLEET_VEHICLES'
  | 'WAREHOUSE_FACILITY'
  | 'EQUIPMENT'
  | 'TEAM'
  | 'OTHER';

// ============================================
// PARTNER APPLICATION TYPES
// ============================================

interface PartnerApplicationDTO {
  applicationId: string;
  referenceNumber: string;
  partnerType: PartnerType;
  businessInfo: PartnerBusinessInfo;
  servicesOffered: string[];
  documents: ApplicationDocument[];
  photos: ApplicationPhoto[];
  status: ApplicationStatus;
  submittedBy: string;
  submittedAt: string;
  reviewedBy?: string;
  reviewedAt?: string;
  approvedAt?: string;
  rejectedReason?: string;
  assignedTerritory?: Territory;
  estimatedCommission?: number;
}

interface PartnerBusinessInfo {
  businessName: string;
  registrationNumber: string;
  businessType: string;
  contactPerson: string;
  phone: string;
  email: string;
  address: Address;
  numberOfEmployees?: number;
  fleetSize?: number;
  warehouseCapacity?: number;
  operationalAreas?: string[];
}

interface ApplicationDocument {
  documentId: string;
  type: DocumentType;
  fileName: string;
  fileUrl: string;
  fileSize: number;
  uploadedAt: string;
}

interface ApplicationPhoto {
  photoId: string;
  type: PhotoType;
  fileUrl: string;
  geoLocation?: GeoLocation;
  capturedAt: string;
  description?: string;
}

type ApplicationStatus =
  | 'DRAFT'
  | 'SUBMITTED'
  | 'UNDER_REVIEW'
  | 'KYC_PENDING'
  | 'APPROVED'
  | 'REJECTED'
  | 'ADDITIONAL_INFO_REQUIRED';

// ============================================
// LEAD TYPES
// ============================================

interface LeadDTO {
  leadId: string;
  source: LeadSource;
  sourceDetails?: string;
  status: LeadStatus;
  priority: LeadPriority;
  score: LeadScore;
  contactInfo: LeadContactInfo;
  companyInfo?: CompanyInfo;
  interests: ProductInterest[];
  estimatedValue: number;
  currency: string;
  purchaseTimeline: PurchaseTimeline;
  decisionMaker?: string;
  competition?: string;
  territory: Territory;
  assignedTo: string;
  assignedAt: string;
  followUpActions: FollowUpAction[];
  notes: LeadNote[];
  convertedTo?: string;
  convertedAt?: string;
  createdAt: string;
  updatedAt: string;
}

interface LeadContactInfo {
  name: string;
  phone: string;
  email?: string;
  company?: string;
  title?: string;
}

interface CompanyInfo {
  name: string;
  industry?: string;
  size?: CompanySize;
  address?: Address;
  website?: string;
}

type CompanySize =
  | 'STARTUP'
  | 'SMALL'
  | 'MEDIUM'
  | 'LARGE'
  | 'ENTERPRISE';

interface ProductInterest {
  category: string;
  product: string;
  specificInterest?: string;
  budget?: number;
}

type PurchaseTimeline =
  | 'IMMEDIATE'
  | 'WITHIN_1_MONTH'
  | '1_3_MONTHS'
  | '3_6_MONTHS'
  | '6_12_MONTHS'
  | 'NEXT_YEAR';

interface LeadSource =
  | 'COLD_CALL'
  | 'FIELD_VISIT'
  | 'REFERRAL'
  | 'EVENT'
  | 'WEBSITE_INQUIRY'
  | 'PARTNER_REFERRAL'
  | 'ADVERTISEMENT'
  | 'OTHER';

type LeadStatus =
  | 'NEW'
  | 'CONTACTED'
  | 'QUALIFIED'
  | 'PROPOSAL'
  | 'NEGOTIATION'
  | 'WON'
  | 'LOST'
  | 'UNQUALIFIED';

type LeadPriority =
  | 'HOT'
  | 'WARM'
  | 'COLD';

type LeadScore =
  | 'A'
  | 'B'
  | 'C'
  | 'D';

interface FollowUpAction {
  actionId: string;
  type: FollowUpType;
  scheduledDate: string;
  scheduledTime?: string;
  status: 'PENDING' | 'COMPLETED' | 'MISSED';
  outcome?: string;
  notes?: string;
  completedAt?: string;
}

type FollowUpType =
  | 'CALL'
  | 'EMAIL'
  | 'VISIT'
  | 'DEMO'
  | 'PROPOSAL';

interface LeadNote {
  noteId: string;
  timestamp: string;
  author: string;
  content: string;
  type: 'NOTE' | 'CALL_LOG' | 'EMAIL_LOG';
  attachments?: string[];
}

// ============================================
// ORDER TYPES
// ============================================

interface OrderDTO {
  orderId: string;
  orderNumber: string;
  customerId: string;
  customerName: string;
  salesRepId: string;
  salesRepName: string;
  status: OrderStatus;
  items: OrderItem[];
  pricing: OrderPricing;
  delivery: OrderDelivery;
  payment: OrderPayment;
  documentUrls: OrderDocuments;
  signature?: OrderSignature;
  createdAt: string;
  updatedAt: string;
  estimatedDeliveryDate?: string;
}

type OrderStatus =
  | 'DRAFT'
  | 'SUBMITTED'
  | 'CONFIRMED'
  | 'PROCESSING'
  | 'READY'
  | 'SHIPPED'
  | 'DELIVERED'
  | 'CANCELLED'
  | 'RETURNED';

interface OrderItem {
  itemId: string;
  productId: string;
  productName: string;
  category: string;
  quantity: number;
  unitPrice: number;
  discount?: number;
  taxRate: number;
  lineTotal: number;
}

interface OrderPricing {
  currency: string;
  subtotal: number;
  discountAmount: number;
  taxAmount: number;
  shippingCost?: number;
  total: number;

  breakdown: PricingBreakdown[];
}

interface PricingBreakdown {
  type: 'DISCOUNT' | 'TAX' | 'SHIPPING';
  amount: number;
  percentage?: number;
  description: string;
}

interface OrderDelivery {
  type: 'PICKUP' | 'DELIVERY' | 'DIGITAL';
  address?: Address;
  date?: string;
  instructions?: string;
  trackingNumber?: string;
  status: 'PENDING' | 'PROCESSING' | 'READY' | 'IN_TRANSIT' | 'DELIVERED';
}

interface OrderPayment {
  terms: PaymentTerms;
  method: PaymentMethod;
  status: PaymentStatus;
  paidDate?: string;
  dueDate?: string;
  amount: number;
}

type PaymentTerms =
  | 'CASH_ON_DELIVERY'
  | 'NET_15'
  | 'NET_30'
  | 'NET_60'
  | 'CUSTOM';

type PaymentMethod =
  | 'BANK_TRANSFER'
  | 'CASH'
  | 'MOBILE_MONEY'
  | 'CARD'
  | 'POS';

type PaymentStatus =
  | 'PENDING'
  | 'PARTIAL'
  | 'PAID'
  | 'OVERDUE';

interface OrderDocuments {
  invoice?: string;
  receipt?: string;
  deliveryNote?: string;
  terms?: string;
}

interface OrderSignature {
  signatureData: string;
  capturedAt: string;
  capturedBy: string;
  ipAddress?: string;
  gpsLocation?: GeoLocation;
}

// ============================================
// PRODUCT TYPES
// ============================================

interface Product {
  productId: string;
  name: string;
  category: ProductCategory;
  subcategory?: string;
  description: string;
  pricing: ProductPricing;
  availability: ProductAvailability;
  images: ProductImage[];
  specifications?: ProductSpecs;
  tags: string[];
  isActive: boolean;
  createdAt: string;
  updatedAt: string;
}

type ProductCategory =
  | 'LOGISTICS'
  | 'WAREHOUSING'
  | 'INVENTORY'
  | 'E_COMMERCE';

interface ProductPricing {
  currency: string;
  basePrice: number;
  unit: string;
  priceTiers?: PriceTier[];
  discountApplicable: boolean;
}

interface PriceTier {
  minQuantity: number;
  maxQuantity: number;
  pricePerUnit: number;
}

interface ProductAvailability {
  inStock: boolean;
  quantity?: number;
  stockLocation?: string;
  leadTimeDays?: number;
  backorderable: boolean;
}

interface ProductImage {
  url: string;
  type: 'PRIMARY' | 'GALLERY';
  alt?: string;
}

// ============================================
// PERFORMANCE TYPES
// ============================================

interface PerformanceSummaryDTO {
  period: PerformancePeriod;
  revenue: RevenuePerformance;
  partners: PartnerPerformance;
  leads: LeadsPerformance;
  orders: OrdersPerformance;
  targets: TargetAchievement;
  commission: CommissionSummary;
  rank?: LeaderboardRank;
}

interface PerformancePeriod {
  type: 'DAILY' | 'WEEKLY' | 'MONTHLY' | 'QUARTERLY';
  startDate: string;
  endDate: string;
}

interface RevenuePerformance {
  target: number;
  achieved: number;
  percentAchieved: number;
  trend: number;
  breakdown: RevenueBreakdown[];
}

interface RevenueBreakdown {
  date: string;
  amount: number;
  cumulative: number;
  target: number;
}

interface PartnerPerformance {
  target: number;
  achieved: number;
  percentAchieved: number;
  newPartners: number;
  tierUpgrades: number;
}

interface LeadsPerformance {
  target: number;
  achieved: number;
  percentAchieved: number;
  qualified: number;
  conversionRate: number;
}

interface OrdersPerformance {
  target: number;
  achieved: number;
  percentAchieved: number;
  orderCount: number;
  averageOrderValue: number;
}

interface TargetAchievement {
  overall: TargetStatus;
  revenue: TargetStatus;
  partners: TargetStatus;
  leads: TargetStatus;
  orders: TargetStatus;
}

type TargetStatus =
  | 'AHEAD'
  | 'ON_TRACK'
  'AT_RISK'
  | 'BEHIND';

interface CommissionSummary {
  totalEarned: number;
  pending: number;
  paid: number;
  currency: string;
  breakdown: CommissionBreakdown[];
  nextPayoutDate?: string;
  currentTier: number;
}

interface CommissionBreakdown {
  period: string;
  directSales: number;
  partnerReferrals: number;
  leadBonuses: number;
  tierBonus: number;
  total: number;
}

interface LeaderboardRank {
  rank: number;
  totalReps: number;
  position: LeaderboardPosition;
}

// ============================================
// EXPENSE TYPES
// ============================================

interface ExpenseDTO {
  expenseId: string;
  salesRepId: string;
  type: ExpenseType;
  category: ExpenseCategory;
  amount: number;
  currency: string;
  date: string;
  description: string;
  relatedCustomerId?: string;
  relatedPartnerId?: string;
  receiptUrl?: string;
  receiptNumber?: string;
  mileage?: MileageInfo;
  status: ExpenseStatus;
  submittedAt: string;
  approvedAt?: string;
  reimbursedAt?: string;
  rejectionReason?: string;
}

type ExpenseType =
  | 'TRAVEL'
  | 'MEALS'
  | 'ACCOMMODATION'
  | 'CLIENT_ENTERTAINMENT'
  | 'SUPPLIES'
  | 'COMMUNICATION'
  | 'TRAINING'
  | 'OTHER';

type ExpenseCategory =
  | 'MILEAGE'
  | 'FUEL'
  | 'ACCOMMODATION'
  | 'MEAL'
  | 'TRANSPORTATION'
  | 'COMMUNICATION'
  | 'OFFICE_SUPPLIES'
  | 'CLIENT_ENTERTAINMENT';

interface MileageInfo {
  startLocation: GeoLocation;
  endLocation: GeoLocation;
  distance: number; // km
  rate: number; // per km
  calculatedAmount: number;
}

type ExpenseStatus =
  | 'PENDING'
  | 'UNDER_REVIEW'
  | 'APPROVED'
  | 'REJECTED'
  'REIMBURSED';

// ============================================
// NOTIFICATION TYPES
// ============================================

interface Notification {
  notificationId: string;
  type: NotificationType;
  title: string;
  message: string;
  priority: 'LOW' | 'MEDIUM' | 'HIGH' | 'URGENT';
  actionRequired: boolean;
  actionUrl?: string;
  metadata?: Record<string, any>;
  read: boolean;
  timestamp: string;
  expiresAt?: string;
}

type NotificationType =
  | 'PARTNER_APPLICATION_APPROVED'
  | 'PARTNER_APPLICATION_REJECTED'
  | 'PARTNER_APPLICATION_REQUIRES_INFO'
  | 'ORDER_CONFIRMED'
  | 'ORDER_SHIPPED'
  | 'LEAD_ASSIGNED'
  | 'COMMISSION_PAID'
  | 'EXPENSE_APPROVED'
  | 'EXPENSE_REJECTED'
  | 'ROUTE_ASSIGNED'
  | 'TARGET_ACHIEVED'
  | 'PERFORMANCE_REVIEW'
  | 'TRAINING_ASSIGNED'
  | 'ANNOUNCEMENT'
  | 'SYSTEM';

// ============================================
// COMMON TYPES
// ============================================

interface Address {
  street?: string;
  city?: string;
  state?: string;
  postalCode?: string;
  country?: string;
  latitude?: number;
  longitude?: number;
}

interface GeoLocation {
  latitude: number;
  longitude: number;
  accuracy?: number;
  timestamp?: string;
}

// ============================================
// OFFLINE SYNC TYPES
// ============================================

interface SyncableAction {
  actionId: string;
  type: SyncableActionType;
  endpoint: string;
  method: 'GET' | 'POST' | 'PUT' | 'DELETE';
  payload?: any;
  timestamp: string;
  synced: boolean;
  retryCount: number;
  priority: number;
}

type SyncableActionType =
  | 'CHECK_IN'
  | 'CHECK_OUT'
  | 'CREATE_LEAD'
  | 'UPDATE_LEAD'
  | 'CREATE_ORDER'
  | 'SUBMIT_EXPENSE'
  | 'REGISTER_PARTNER'
  | 'UPDATE_LOCATION';

interface SyncStatus {
  lastSyncAt?: string;
  pendingActions: number;
  lastSyncStatus: 'SUCCESS' | 'FAILED' | 'NEVER';
  errorMessage?: string;
}
```

---

## 4. STATE MANAGEMENT FLOW

### 4.1 Redux Store Structure

```typescript
import { combineReducers, configureStore } from '@reduxjs/toolkit';
import { persistStore, persistReducer } from 'redux-persist';

// ============================================
// SLICES
// ============================================

// Auth Slice
interface AuthState {
  user: SalesRepUser | null;
  token: string | null;
  isAuthenticated: boolean;
  isLoading: boolean;
  error: string | null;
}

// Route Slice
interface RouteState {
  todayRoute: RouteDTO | null;
  plannedRoutes: RouteDTO[];
  currentStop: RouteStop | null;
  visitStartTime: string | null;
  navigationMode: boolean;
}

// Partner Slice
interface PartnerState {
  partners: PartnerSummary[];
  partnersDict: Record<string, PartnerDetailDTO>;
  applications: PartnerApplicationDTO[];
  loading: boolean;
  error: string | null;
}

// Lead Slice
interface LeadState {
  leads: LeadDTO[];
  selectedLead: LeadDTO | null;
  loading: boolean;
  error: string | null;
}

// Order Slice
interface OrderState {
  cart: CartItem[];
  orders: OrderDTO[];
  selectedOrder: OrderDTO | null;
  loading: boolean;
  error: string | null;
}

// Performance Slice
interface PerformanceState {
  summary: PerformanceSummaryDTO | null;
  commission: CommissionSummary | null;
  leaderboard: LeaderboardRank[] | null;
  loading: boolean;
  error: string | null;
}

// Offline Sync Slice
interface OfflineState {
  isOnline: boolean;
  pendingActions: SyncableAction[];
  lastSyncAt: string | null;
  syncStatus: SyncStatus;
  syncInProgress: boolean;
}

// ============================================
// ROOT REDUCER
// ============================================

const rootReducer = combineReducers({
  auth: persistReducer(authReducer, authPersistConfig),
  routes: routesReducer,
  partners: partnersReducer,
  leads: leadsReducer,
  orders: ordersReducer,
  performance: performanceReducer,
  offline: offlineReducer,
});

// ============================================
// STORE CONFIGURATION
// ============================================

const store = configureStore({
  reducer: rootReducer,
  middleware: (getDefaultMiddleware) =>
    getDefaultMiddleware({
      serializableCheck: false,
    immutableCheck: false,
    }),
});

// ============================================
// ROOT STATE TYPE
// ============================================

type RootState = ReturnType<typeof store.getState>;
```

---

## 5. OFFLINE DATA SYNC

### 5.1 Offline Data Flow

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                         OFFLINE DATA SYNC FLOW                             │
└─────────────────────────────────────────────────────────────────────────────┘

Network Unavailable
      │
      ▼
┌─────────────────────────────────────────┐
│ Detect Offline Mode                       │
│ • Connection listener fires               │
│ • Set offline mode flag                   │
│ • Show indicator in UI                   │
└─────────────────────────────────────────┘
      │
      ├─ User Creates Lead ──────────────────────────────────────────────────┐
      │                                                                   │
      ▼                                                                   │
┌─────────────────────────────────────────┐                                 │
│ Create Lead Action                       │                                 │
│ {                                        │                                 │
│   actionId: uuid-v4(),                  │                                 │
│   type: 'CREATE_LEAD',                │                                 │
│   endpoint: '/api/v1/sales/leads/create',│                                 │
│   method: 'POST',                      │                                 │
│   payload: { leadData },                │                                 │
│   timestamp: now(),                    │                                 │
│   synced: false,                       │                                 │
│   priority: 1                         │                                 │
│ }                                        │                                 │
└─────────────────────────────────────────┘                                 │
      │                                                                   │
      ▼                                                                   │
┌─────────────────────────────────────────┐                                 │
│ Store in Redux & AsyncStorage            │                                 │
│ • Add to offline pending actions          │                                 │
│ • Store in AsyncStorage for persistence   │                                 │
│ • Update UI to show "Saved Offline"       │                                 │
└─────────────────────────────────────────┘                                 │
      │                                                                   │
      ▼                                                                   │
│ ┌─────────────────────────────────┐                                 │
│ │ Optimistic UI Update                  │                                 │
│ │ • Add lead to local leads list        │                                 │
│ │ • Show success indicator             │                                 │
│ │ • Mark with "Sync pending" badge     │                                 │
│ └─────────────────────────────────┘                                 │
                                                                            │
└───────────────────────────────────────────────────────────────────────────┘

Network Restored
      │
      ▼
┌─────────────────────────────────────────┐
│ Background Sync Initiated                │
│ • Get all pending actions                │
│ • Sort by priority                       │
│ └─────────────────────────────────┘   │
      │                                       │
      ▼                                       │
┌─────────────────────────────────────────┐ │
│ Process Each Pending Action             │ │
│ ┌─────────────────────────────────┐   │ │
│ │ POST /api/v1/sales/leads/create    │   │ │
│ │ { leadData }                          │   │ │
│ │    │                                 │   │ │
│ │    ├─ Success ─────────────────────│  │ │
│ │    │                                 │   │ │
│ │    ▼                                 │   │ │
│ │  ┌─────────────────────────────┐│   │ │
│  │ {                                ││   │ │
│ │  │   leadId: "LED-123"            ││   │ │
│  │ │   synced: true                ││   │ │
│  │ │ }                                ││   │ │
│  │ └─────────────────────────────┘│   │ │
│ │                                   │   │ │
│ │ └─────────────────────────────┘│   │ │
│ │                                   │   │ │
│ └─────────────────────────────────┘   │ │
│                                   │   │ │
│ ├─ Error ─────────────────────────│   │ │
│ │                                   │   │ │
│ ▼                                   │   │ │
│ Keep in pending with retry count + 1  │   │ │
└─────────────────────────────────────────┘   │
                                            │
└─────────────────────────────────────────────┘
```

### 5.2 Conflict Resolution

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                        CONFLICT RESOLUTION                                  │
└─────────────────────────────────────────────────────────────────────────────┘

Sync Error: Lead LED-123 already exists on server
      │
      ▼
┌─────────────────────────────────────────┐
│ Conflict Detected                        │
│ • Local: New lead created offline       │
│ • Server: Lead exists with same data     │
└─────────────────────────────────────────┘
      │
      ▼
┌─────────────────────────────────────────┐
│ Resolution Strategy                       │
│ 1. Check timestamps                     │
│ 2. Check who has more recent data       │
│ 3. Server data wins (usually)           │
│ 4. Update local with server data         │
│ 5. Notify user of conflict              │
└─────────────────────────────────────────┘
      │
      ▼
┌─────────────────────────────────────────┐
│ User Notification                       │
│ "Lead already exists.                  │
│  Local data merged with server."       │
│  [View Details] [Dismiss]              │
└─────────────────────────────────────────┘
```

---

## 6. LOCATION SERVICES FLOW

### 6.1 GPS Tracking Flow

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                        GPS TRACKING FLOW                                   │
└─────────────────────────────────────────────────────────────────────────────┘

App requests location permission
      │
      ▼
┌─────────────────────────────────────────┐
│ Start Location Service                  │
│ • Request permission (if not granted)     │
│ • Set up location listener               │
│ • Configure update interval (30s)         │
└─────────────────────────────────────────┘
      │
      ▼
┌─────────────────────────────────────────┐
│ Location Update Listener                 │
│ navigator.geolocation.watchPosition(     │
│   {                                    │
│     enableHighAccuracy: true,           │
│     distanceFilter: 10,                │
│     timeout: 10000                     │
│   },                                   │
│   (location) => {                       │
│     // Handle location updates          │
│   }                                   │
│ )                                       │
└─────────────────────────────────────────┘
      │
      ▼
┌─────────────────────────────────────────┐
│ On Location Update                       │
│ • Update Redux store                     │
│ • If significant movement:               │
│   • Update current location in state     │
│   • Check nearby stops                   │
│   • Send background update to server    │
│ • If near scheduled stop:                │
│   • Notify user: "Near ABC Logistics"   │
└─────────────────────────────────────────┘
      │
      ▼
┌─────────────────────────────────────────┐
│ Background Location Update               │
│ POST /api/v1/sales/location/update      │
│ {                                        │
│   salesRepId: "SR-NGA-001",            │
│   location: {                          │
│     latitude: 6.5245,                  │
│     longitude: 3.3793,                 │
│     accuracy: 12.3                     │
│   },                                   │
│   timestamp: "2026-03-08T09:15:30Z"  │
│ }                                        │
└─────────────────────────────────────────┘
```

### 6.2 Geofencing Flow

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                        GEOFENCING FLOW                                     │
└─────────────────────────────────────────────────────────────────────────────┘

Sales rep enters customer/partner vicinity
      │
      ▼
┌─────────────────────────────────────────┐
│ Geofence Check                          │
│ • Is location within geofence?         │
│ • Calculate distance from center         │
│ • Determine if inside or outside         │
└─────────────────────────────────────────┘
      │
      ├─ Entered Geofence ────────────────────────────────────────────────────┐
      │                                                                   │
      ▼                                                                   │
┌─────────────────────────────────────────┐                                 │
│ Geofence Entry Event                    │                                 │
│ • Trigger: Enter geofence radius         │
│ • Send notification: "Near ABC Logistics"│                                 │
│ • Show prompt: "Check in now?"           │                                 │
│ • Auto-check in option (if enabled)     │                                 │
└─────────────────────────────────────────┘                                 │
                                                                            │
      └─ Exited Geofence ───────────────────────────────────────────────────────│
                                                                           │
      ▼                                                                     │
┌─────────────────────────────────────────┐                                 │
│ Geofence Exit Event                     │                                 │
│ • Trigger: Exit geofence radius          │                                 │
│ • Update status in Redux                 │                                 │
│ │
└─────────────────────────────────────────┘                                 │
                                                                            │
└───────────────────────────────────────────────────────────────────────────┘
```

---

**End of Mock Flow Documentation v1.0**

**Next:** [04_Page_By_Page_Flow_Documentation.md](./04_Page_By_Page_Flow_Documentation.md) - Detailed page flows
