# Vendor Services Documentation

**Last Updated**: 2026-04-25

---

## Overview

The Vendor module provides comprehensive services for vendor management, dropshipping, onboarding, dashboard, and analytics.

---

## Services

| Service | Port | Description |
|---------|------|-------------|
| vendor-service | 8336 | Core vendor management |
| dropship-service | 8337 | Dropshipping operations |
| vendor-dashboard-service | 8338 | Dashboard metrics and reports |
| onboarding-service | 8339 | Vendor onboarding workflow |
| vendor-analytics-service | 8340 | Analytics and reporting |

---

## Vendor Service (8336)

### Domain Models
- **Vendor** - Vendor entity with business info, verification status, settings

### Key Endpoints
- `POST /api/v1/vendors` - Create vendor
- `GET /api/v1/vendors/{vendorId}` - Get vendor
- `PUT /api/v1/vendors/{vendorId}` - Update vendor
- `PATCH /api/v1/vendors/{vendorId}/status` - Update status
- `PATCH /api/v1/vendors/{vendorId}/verification` - Update verification status

---

## Dropship Service (8337)

### Domain Models
- **DropshipOrder** - Dropship order management
- **DropshipSupplier** - Supplier management

### Key Endpoints
- `POST /api/v1/dropship/orders` - Create dropship order
- `POST /api/v1/dropship/orders/{id}/process` - Process order
- `POST /api/v1/dropship/suppliers` - Create supplier
- `POST /api/v1/dropship/suppliers/{id}/enable-auto` - Enable auto-order

---

## Vendor Dashboard Service (8338)

### Domain Models
- **DashboardMetrics** - Sales, order, product, inventory metrics
- **VendorReport** - Report generation and management
- **VendorAlert** - Alert management

### Key Endpoints
- `GET /api/v1/vendor-dashboard/metrics/{vendorId}` - Get dashboard metrics
- `POST /api/v1/vendor-dashboard/metrics/{vendorId}/calculate` - Calculate metrics
- `POST /api/v1/vendor-dashboard/reports` - Create report
- `GET /api/v1/vendor-dashboard/alerts` - Get alerts

---

## Onboarding Service (8339)

### Domain Models
- **OnboardingSession** - Onboarding session tracking
- **OnboardingTask** - Onboarding task management

### Key Endpoints
- `POST /api/v1/onboarding/sessions` - Create onboarding session
- `POST /api/v1/onboarding/sessions/{id}/advance` - Advance step
- `POST /api/v1/onboarding/sessions/{id}/complete` - Complete onboarding
- `POST /api/v1/onboarding/tasks` - Create task

---

## Vendor Analytics Service (8340)

### Domain Models
- **VendorAnalytics** - Detailed analytics data
- **AnalyticsSummary** - Aggregated summaries

### Key Endpoints
- `GET /api/v1/vendor-analytics/analytics` - Get analytics
- `GET /api/v1/vendor-analytics/realtime` - Get real-time metrics
- `POST /api/v1/vendor-analytics/summaries/generate` - Generate summary

---

## API Conventions

### Multi-Tenancy
All endpoints require `X-Tenant-ID` header for tenant isolation.

### Response Format
- Success: 200 OK with response body
- Not Found: 404 Not Found
- Validation Error: 400 Bad Request
- Server Error: 500 Internal Server Error

---

## Data Flow

```
Client Request → Gateway → X-Tenant-ID Validation → Service → MongoDB
```

---

## Integration Points

- **vendor-service** ↔ **vendor-dashboard-service** - Metrics aggregation
- **vendor-service** ↔ **onboarding-service** - Vendor profile creation
- **dropship-service** ↔ **fulfillment-service** - Order fulfillment
- **vendor-analytics-service** ↔ **all services** - Data aggregation

---

## Security Notes

1. All vendor data is isolated by tenant ID
2. Verification required before vendors can operate
3. Audit logging for all sensitive operations
4. Rate limiting applied per vendor
