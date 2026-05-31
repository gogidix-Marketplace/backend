# GOGIDIX MARKET - BACKEND DOMAINS REFERENCE

**Version:** 1.0
**Date:** May 30, 2026
**Status:** EXISTING CODE READY TO CLONE

---

## OVERVIEW

The backend is organized into 5 core domains + shared infrastructure. All domains are production-ready and will be cloned for this project.

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                         BACKEND DOMAIN ARCHITECTURE                         │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│  ┌──────────────────────────────────────────────────────────────────────┐  │
│  │                        FOUNDATION DOMAIN                               │  │
│  │                    (Shared Infrastructure - No Business Logic)         │  │
│  └──────────────────────────────────────────────────────────────────────┘  │
│                              ↕ Provides infra to                           │
│  ┌──────────────────────────────────────────────────────────────────────┐  │
│  │                      SHARED BUSINESS INFRASTRUCTURE                     │  │
│  │                       (API, Auth, Messaging, etc.)                        │  │
│  └──────────────────────────────────────────────────────────────────────┘  │
│                              ↕ Shared services for                           │
│  ┌──────────────┬──────────────┬──────────────────┬────────────────────┐ │
│  ↓              ↓              ↓                  ↓                    ↓  │
│  ┌────────────┐ ┌────────────┐ ┌────────────────┐ ┌────────────────┐  │
│  │  SHARED    │ │  BUSINESS   │ │   MANAGEMENT   │ │    COURIER     │  │
│  │ BUSINESS   │ │ORCHESTRATOR │ │    DOMAIN      │ │ SERVICES       │  │
│  │  LOGICS    │ │   DOMAIN    │ │ (9 Departments)│ │  DOMAIN        │  │
│  └────────────┘ └────────────┘ └────────────────┘ └────────────────┘  │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## 1. FOUNDATION DOMAIN

**Purpose:** Pure infrastructure services with NO business logic

**Location:** `backend/foundation-domain/`

### Sub-Domains

| Sub-Domain | Description | Status |
|------------|-------------|--------|
| `multi-tenancy` | Tenant isolation, data segregation | ✓ Ready |
| `security` | Authentication, RBAC, encryption | ✓ Ready |
| `event-bus` | Message broker integration (Kafka/RabbitMQ) | ✓ Ready |
| `notification` | Email, SMS, Push notification service | ✓ Ready |
| `logging` | Structured logging, log aggregation | ✓ Ready |
| `monitoring` | Health checks, metrics, alerts | ✓ Ready |
| `cache` | Redis integration, caching strategies | ✓ Ready |
| `database` | DB connection pooling, migrations | ✓ Ready |
| `config` | Configuration management, secrets | ✓ Ready |

### Key Services

```typescript
// Multi-Tenancy
TenantService.getCurrentTenant()
TenantService.isTenantActive(tenantId)
TenantService.getTenantConfig(tenantId)

// Security
AuthService.authenticate(credentials)
AuthService.authorize(user, permission)
AuthService.generateToken(user)

// Event Bus
EventBus.publish(event)
EventBus.subscribe(pattern, handler)
EventBus.createTopic(name)

// Notification
NotificationService.sendEmail(to, subject, body)
NotificationService.sendSMS(phone, message)
NotificationService.sendPush(userId, payload)
```

---

## 2. MANAGEMENT DOMAIN

**Purpose:** 9 operational departments to manage platform operations

**Location:** `backend/management-domain/`

### Departments

| Department | Description | Key Functions |
|------------|-------------|---------------|
| `operations` | Day-to-day operations | Order monitoring, incident response |
| `finance` | Financial operations | Revenue tracking, payouts, reconciliation |
| `analytics` | Business intelligence | Reports, dashboards, insights |
| `audit` | Compliance & audit | Activity logging, compliance checks |
| `admin` | Platform administration | User management, system config |
| `compliance` | Regulatory compliance | KYC verification, document validation |
| `support` | Customer support | Ticket management, helpdesk |
| `reporting` | Report generation | Scheduled reports, exports |
| `security-admin` | Security operations | Fraud detection, security alerts |

### API Endpoints

```
Operations:
  GET    /api/operations/dashboard
  GET    /api/operations/incidents
  POST   /api/operations/incidents/{id}/resolve

Finance:
  GET    /api/finance/revenue
  GET    /api/finance/payouts
  POST   /api/finance/payouts/process

Analytics:
  GET    /api/analytics/reports/{id}
  POST   /api/analytics/reports
  GET    /api/analytics/metrics

Audit:
  GET    /api/audit/logs
  GET    /api/audit/compliance

Admin:
  GET    /api/admin/users
  POST   /api/admin/users
  PUT    /api/admin/users/{id}/suspend

Compliance:
  GET    /api/compliance/kyc/{id}
  POST   /api/compliance/verify
  GET    /api/compliance/documents

Support:
  GET    /api/support/tickets
  POST   /api/support/tickets
  PUT    /api/support/tickets/{id}

Reporting:
  GET    /api/reports/scheduled
  POST   /api/reports/generate

Security-Admin:
  GET    /api/security-admin/alerts
  POST   /api/security-admin/investigations
```

---

## 3. SHARED BUSINESS LOGICS

**Purpose:** Reusable business logic across multiple domains

**Location:** `backend/shared-business-logics/`

### Business Logic Modules

| Module | Description | Key Operations |
|--------|-------------|-----------------|
| `product-catalog` | Product management | CRUD, categorization, search |
| `inventory` | Stock management | Movement tracking, reservations |
| `pricing` | Price calculation | Discounts, dynamic pricing |
| `orders` | Order lifecycle | State management, workflows |
| `customer` | Customer data | Profiles, preferences, segments |
| `payment` | Payment processing | Transactions, refunds, reconciliation |
| `address` | Address management | Validation, formatting, geocoding |
| `location` | Location services | Geocoding, distance calculation |

### Service Interfaces

```typescript
// Product Catalog
ProductService.create(product)
ProductService.update(id, changes)
ProductService.delete(id)
ProductService.search(query)
ProductService.getCategories()

// Inventory
InventoryService.getStock(productId, location)
InventoryService.addMovement(movement)
InventoryService.reserve(orderId)
InventoryService.release(orderId)

// Pricing
PricingService.calculatePrice(productId, quantity)
PricingService.applyDiscount(productId, discount)
PricingService.getDynamicPrice(productId)

// Orders
OrderService.create(order)
OrderService.updateStatus(id, status)
OrderService.getState(id)
OrderService.cancel(id)

// Customer
CustomerService.getProfile(id)
CustomerService.updatePreferences(id, prefs)
CustomerService.getSegment(id)

// Payment
PaymentService.charge(paymentMethod, amount)
PaymentService.refund(transactionId, amount)
PaymentService.reconcile(startDate, endDate)
```

---

## 4. BUSINESS ORCHESTRATOR DOMAIN

**Purpose:** Workflow orchestration and state machine management

**Location:** `backend/business-orchestrator-domain/`

### Components

| Component | Description | Use Cases |
|-----------|-------------|------------|
| `workflows` | Workflow definitions | Order fulfillment, approval flows |
| `state-machines` | State machine engine | Order states, offer states, job states |
| `approvals` | Approval workflows | Vendor approval, spending limits |
| `task-scheduling` | Scheduled tasks | Batch jobs, periodic processes |
| `rule-engine` | Business rules engine | Routing rules, pricing rules |

### Workflow Examples

```typescript
// Order Fulfillment Workflow
Workflow: OrderFulfillment
  Steps:
    - ValidateInventory
    - ReserveStock
    - ProcessPayment
    - AssignCourier
    - UpdateStatus
    - NotifyCustomer
  Transitions:
    - OnPaymentFailed → CancelOrder
    - OnOutOfStock → NotifyVendor

// Negotiation State Machine
StateMachine: OfferNegotiation
  States: [SUBMITTED, PENDING_VENDOR, ACCEPTED, REJECTED, EXPIRED]
  Transitions:
    SUBMITTED → PENDING_VENDOR (when offer >= min)
    SUBMITTED → AUTO_REJECTED (when offer < min)
    PENDING_VENDOR → ACCEPTED (on vendor accept)
    PENDING_VENDOR → REJECTED (on vendor reject)
    PENDING_VENDOR → EXPIRED (after 24h)

// Approval Workflow
Workflow: VendorApproval
  Steps:
    - SubmitApplication
    - DocumentVerification
    - BackgroundCheck
    - ComplianceReview
    - FinalApproval
  Approvers: [Admin, Compliance, Operations]
```

---

## 5. SHARED BUSINESS INFRASTRUCTURE

**Purpose:** Shared technical infrastructure for business services

**Location:** `backend/shared-business-infrastructure/`

### Infrastructure Components

| Component | Description | Technologies |
|-----------|-------------|--------------|
| `api-gateway` | API gateway, routing, rate limiting | Kong/Nginx |
| `auth` | Authentication, authorization | OAuth2, JWT |
| `messaging` | Message queue integration | RabbitMQ, Kafka |
| `rate-limiter` | Rate limiting strategies | Redis, Token bucket |
| `storage` | File storage, CDN | S3, CloudFront |
| `validation` | Request/response validation | Joi, Zod |
| `error-handling` | Standardized error handling | Custom error classes |
| `file-handling` | File upload/download processing | Multer, GridFS |

### Middleware Stack

```typescript
// API Gateway Pipeline
Request →
  CorsMiddleware →
  AuthMiddleware →
  RateLimitMiddleware →
  TenantMiddleware →
  ValidationMiddleware →
  RequestLogging →
  Controller →
  Response →
  ErrorHandler →
Response
```

---

## 6. SHARED LAYERS

### Shared Data Access
`backend/shared-data-access/`
- Repository base classes
- ORM configurations
- Database session management
- Query builders

### Shared Utilities
`backend/shared-utilities/`
- Date/time helpers
- String manipulation
- Encryption utilities
- HTTP client wrapper
- Retry logic

### Shared Types
`backend/shared-types/`
- Common TypeScript types
- Interface definitions
- Enum definitions
- DTO base classes

### Shared Constants
`backend/shared-constants/`
- Application constants
- Error codes
- Status codes
- Configuration keys

---

## 7. API ROUTING STRUCTURE

```
/api/v1/
├── /foundation/
│   ├── /tenants
│   ├── /auth
│   └── /notifications
├── /management/
│   ├── /operations
│   ├── /finance
│   ├── /analytics
│   └── ...
├── /vendor/
│   ├── /products
│   ├── /inventory
│   ├── /orders
│   └── /couriers
├── /marketplace/
│   ├── /products
│   ├── /vendors
│   ├── /cart
│   └── /checkout
├── /courier/
│   ├── /jobs
│   ├── /location
│   └── /earnings
└── /independent-rider/
    ├── /jobs
    ├── /earnings
    └── /profile
```

---

## 8. DEPLOYMENT ARCHITECTURE

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                              DEPLOYMENT                                      │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│  ┌──────────────────────────────────────────────────────────────────────┐  │
│  │                        API GATEWAY                                      │  │
│  │                    (Kong/Nginx + Load Balancer)                        │  │
│  └──────────────────────────────────────────────────────────────────────┘  │
│                                    │                                        │
│              ┌─────────────────────┴─────────────────────┐                 │
│              │                                       │                    │
│  ┌───────────────────────┐             ┌───────────────────────────┐   │
│  │   Foundation Domain   │             │   Business Domains         │   │
│  │   Services            │             │   (Vendor, Marketplace,     │   │
│  │   - Auth              │             │    Courier, etc.)           │   │
│  │   - Multi-tenancy      │             │                             │   │
│  │   - Event Bus         │             │                             │   │
│  │   - Notifications     │             │                             │   │
│  └───────────────────────┘             └───────────────────────────┘   │
│              │                                       │                    │
│  ┌───────────────────────┐             ┌───────────────────────────┐   │
│  │   Shared Business     │             │   Management Domain        │   │
│  │   Infrastructure      │             │   (Operations, Finance,     │   │
│  │   - API Gateway       │             │    Analytics, etc.)         │   │
│  │   - Rate Limiting     │             │                             │   │
│  │   - Validation        │             │                             │   │
│  └───────────────────────┘             └───────────────────────────┘   │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## 9. GETTING STARTED WITH CODE CLONE

### Clone Commands

```bash
# Clone foundation domain
git clone <repo-url> backend/foundation-domain

# Clone management domain
git clone <repo-url> backend/management-domain

# Clone shared business logics
git clone <repo-url> backend/shared-business-logics

# Clone business orchestrator
git clone <repo-url> backend/business-orchestrator-domain

# Clone shared business infrastructure
git clone <repo-url> backend/shared-business-infrastructure

# Clone shared layers
git clone <repo-url> backend/shared-data-access
git clone <repo-url> backend/shared-utilities
git clone <repo-url> backend/shared-types
git clone <repo-url> backend/shared-constants
```

---

## APPENDIX A: DOMAIN DEPENDENCIES

```
Foundation Domain
  ↓ (no dependencies - pure infrastructure)

Shared Business Infrastructure
  ↓ depends on → Foundation Domain

Shared Business Logics
  ↓ depends on → Foundation Domain, Shared Business Infrastructure

Business Orchestrator Domain
  ↓ depends on → Foundation Domain, Shared Business Logics

Management Domain
  ↓ depends on → Foundation Domain, Shared Business Logics

Application Domains (Vendor, Marketplace, Courier)
  ↓ depends on → All shared domains
```

---

**END OF DOCUMENT**
