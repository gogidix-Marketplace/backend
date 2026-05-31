# Country Marketing Dashboard - API Documentation

## Overview

The Country Marketing Dashboard API provides endpoints for managing marketing operations at the country level, including campaigns, leads, events, budgets, and ROI analytics. All data is synced with the HQ Marketing Dashboard.

**Base URL:** `http://localhost:8080/api/v1`

**Authentication:** Headers-based authentication
```
X-Tenant-Id: <tenant-id>
X-Country: <country-code> (NG, KE, GH, ZA, CI)
X-User-Id: <user-id>
```

## Table of Contents

- [Campaigns](#campaigns)
- [Leads](#leads)
- [Dashboard](#dashboard)
- [Events](#events)
- [Budget](#budget)
- [Brand Tracking](#brand-tracking)
- [ROI Analytics](#roi-analytics)

---

## Campaigns

### Get All Campaigns

```http
GET /api/v1/campaigns?page=0&size=20&sortBy=createdAt&sortDirection=DESC
```

**Query Parameters:**
| Parameter | Type | Default | Description |
|-----------|------|---------|-------------|
| page | integer | 0 | Page number |
| size | integer | 20 | Page size |
| sortBy | string | createdAt | Sort field |
| sortDirection | ASC/DESC | DESC | Sort direction |

**Response (200 OK):**
```json
{
  "success": true,
  "message": null,
  "data": {
    "campaigns": [
      {
        "id": "cmp-123",
        "name": "Q1 Digital Campaign",
        "type": "DIGITAL",
        "status": "ACTIVE",
        "budget": 50000.00,
        "spent": 12500.00,
        "remainingBudget": 37500.00,
        "country": "NG",
        "channels": ["FACEBOOK", "GOOGLE"],
        "startDate": "2026-01-01T00:00:00Z",
        "endDate": "2026-03-31T23:59:59Z",
        "objective": "AWARENESS",
        "metrics": {
          "impressions": 150000,
          "clicks": 8500,
          "conversions": 340,
          "ctr": 5.67,
          "roas": 2.8
        },
        "createdAt": "2026-01-01T10:00:00Z"
      }
    ],
    "totalCount": 25,
    "page": 0,
    "pageSize": 20,
    "totalPages": 2
  }
}
```

### Create Campaign

```http
POST /api/v1/campaigns
Content-Type: application/json

{
  "name": "Q1 Digital Campaign",
  "type": "DIGITAL",
  "budget": 50000.00,
  "country": "NG",
  "startDate": "2026-01-01T00:00:00Z",
  "endDate": "2026-03-31T23:59:59Z",
  "channels": ["FACEBOOK", "GOOGLE"],
  "objective": "AWARENESS"
}
```

**Response (201 Created):**
```json
{
  "success": true,
  "message": "Resource created successfully",
  "data": {
    "id": "cmp-123",
    "name": "Q1 Digital Campaign",
    "status": "DRAFT",
    ...
  }
}
```

### Get Campaign by ID

```http
GET /api/v1/campaigns/{id}
```

### Update Campaign

```http
PUT /api/v1/campaigns/{id}
Content-Type: application/json

{
  "name": "Updated Campaign Name",
  "status": "ACTIVE",
  "budget": 60000.00
}
```

### Launch Campaign

```http
POST /api/v1/campaigns/{id}/launch
```

### Pause Campaign

```http
POST /api/v1/campaigns/{id}/pause
```

### Record Campaign Spend

```http
POST /api/v1/campaigns/{id}/spend?amount=1500.00
```

### Delete Campaign

```http
DELETE /api/v1/campaigns/{id}
```

### Get Active Campaigns

```http
GET /api/v1/campaigns/active
```

### Get Campaign Statistics

```http
GET /api/v1/campaigns/statistics/summary
```

**Response:**
```json
{
  "success": true,
  "data": {
    "totalCampaigns": 25,
    "activeCampaigns": 8,
    "pausedCampaigns": 2,
    "completedCampaigns": 15,
    "totalBudget": 500000.00,
    "totalSpent": 285000.00,
    "totalRemaining": 215000.00
  }
}
```

---

## Leads

### Get All Leads

```http
GET /api/v1/leads?page=0&size=20
```

**Response (200 OK):**
```json
{
  "success": true,
  "data": {
    "leads": [
      {
        "id": "lead-123",
        "leadNumber": "NG-20260201-000001",
        "firstName": "John",
        "lastName": "Doe",
        "email": "john.doe@company.com",
        "company": "Acme Corp",
        "status": "NEW",
        "source": "WEBSITE",
        "leadScore": "A",
        "estimatedValue": 25000.00,
        "probability": 75,
        "assignedTo": "user-456",
        "createdAt": "2026-02-01T10:00:00Z"
      }
    ],
    "totalCount": 150,
    "page": 0,
    "pageSize": 20,
    "totalPages": 8
  }
}
```

### Create Lead

```http
POST /api/v1/leads
Content-Type: application/json

{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@company.com",
  "phone": "+234 123 456 7890",
  "company": "Acme Corp",
  "country": "NG",
  "source": "WEBSITE",
  "productInterest": "Logistics Platform",
  "estimatedValue": 25000.00
}
```

### Get Lead by ID

```http
GET /api/v1/leads/{id}
```

### Update Lead Status

```http
PATCH /api/v1/leads/{id}/status
Content-Type: application/json

{
  "status": "CONTACTED",
  "notes": "Initial contact made via phone"
}
```

**Valid Status Values:** `NEW`, `CONTACTED`, `QUALIFIED`, `PROPOSAL`, `NEGOTIATION`, `WON`, `LOST`, `UNRESPONSIVE`

### Assign Lead

```http
POST /api/v1/leads/{id}/assign
Content-Type: application/json

{
  "assignedTo": "user-456",
  "assignedToName": "Jane Smith",
  "notes": "Assigning for follow-up"
}
```

### Record Engagement

```http
POST /api/v1/leads/{id}/engagement?engagementType=EMAIL_OPEN
```

**Valid Engagement Types:** `EMAIL_OPEN`, `EMAIL_CLICK`, `WEBSITE_VISIT`

### Get Hot Leads

```http
GET /api/v1/leads/hot
```

### Get Lead Statistics

```http
GET /api/v1/leads/statistics/summary
```

**Response:**
```json
{
  "success": true,
  "data": {
    "totalLeads": 500,
    "newLeads": 75,
    "contactedLeads": 125,
    "qualifiedLeads": 85,
    "convertedLeads": 45,
    "lostLeads": 30,
    "hotLeads": 60,
    "totalPipelineValue": 3750000.00,
    "conversionRate": 9.0
  }
}
```

---

## Dashboard

### Get Quick Stats

```http
GET /api/v1/dashboard/quick-stats
```

**Response:**
```json
{
  "success": true,
  "data": {
    "activeCampaigns": 8,
    "newLeads": 45,
    "spendThisPeriod": 28500.00,
    "revenueThisPeriod": 125000.00,
    "upcomingEvents": 3,
    "averageROI": 145.5,
    "budgetUtilization": 57.0,
    "pendingTasks": 12
  }
}
```

### Get Full Dashboard Data

```http
GET /api/v1/dashboard?periodStart=2026-01-01T00:00:00Z&periodEnd=2026-01-31T23:59:59Z
```

**Response:**
```json
{
  "success": true,
  "data": {
    "country": "NG",
    "periodStart": "2026-01-01T00:00:00Z",
    "periodEnd": "2026-01-31T23:59:59Z",
    "overview": {
      "totalCampaigns": 25,
      "activeCampaigns": 8,
      "totalLeads": 500,
      "convertedLeads": 45,
      "totalRevenue": 125000.00
    },
    "campaignMetrics": { ... },
    "leadMetrics": { ... },
    "budgetMetrics": { ... },
    "roiMetrics": { ... }
  }
}
```

### Get Alerts

```http
GET /api/v1/dashboard/alerts
```

**Response:**
```json
{
  "success": true,
  "data": [
    {
      "id": "alert-123",
      "type": "BUDGET_OVER",
      "severity": "CRITICAL",
      "title": "Campaign Over Budget",
      "message": "Campaign 'Q1 Digital' has exceeded its budget",
      "entity": "Campaign",
      "entityId": "cmp-123",
      "createdAt": "2026-02-01T10:00:00Z",
      "acknowledged": false
    }
  ]
}
```

---

## Events

### Get All Events

```http
GET /api/v1/events?page=0&size=20
```

### Get Upcoming Events

```http
GET /api/v1/events/upcoming
```

### Create Event

```http
POST /api/v1/events
Content-Type: application/json

{
  "name": "Product Launch Webinar",
  "type": "WEBINAR",
  "eventDate": "2026-03-15T14:00:00Z",
  "maxAttendees": 500,
  "isVirtual": true,
  "virtualEventUrl": "https://zoom.us/j/123456"
}
```

### Register for Event

```http
POST /api/v1/events/{id}/register
Content-Type: application/json

{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@company.com"
}
```

---

## Common Error Responses

### 400 Bad Request
```json
{
  "success": false,
  "errorCode": "VALIDATION_ERROR",
  "message": "Validation failed for field 'email'",
  "errors": [
    {
      "field": "email",
      "message": "Email must be valid"
    }
  ]
}
```

### 404 Not Found
```json
{
  "success": false,
  "errorCode": "NOT_FOUND",
  "message": "Resource not found with id: xyz"
}
```

### 500 Internal Server Error
```json
{
  "success": false,
  "errorCode": "INTERNAL_ERROR",
  "message": "An unexpected error occurred"
}
```

---

## Integration with HQ

### Data Sync Flow

1. **Country Dashboard → HQ (via Kafka)**
   - Campaign created/updated
   - Lead captured
   - ROI analytics calculated
   - Budget updates

2. **HQ → Country Dashboard (via REST API)**
   - Campaign directives
   - Brand guidelines
   - Global campaigns to localize

### Kafka Topics

| Topic | Purpose |
|-------|---------|
| `marketing.campaign.data` | Campaign events |
| `marketing.lead.data` | Lead events |
| `marketing.analytics.data` | ROI analytics |
| `marketing.budget.data` | Budget updates |
| `marketing.sync.data` | Full sync data |

---

## Rate Limiting

- **Default:** 1000 requests per hour per tenant
- **Burst:** 100 requests per minute

Rate limit headers:
```
X-RateLimit-Limit: 1000
X-RateLimit-Remaining: 950
X-RateLimit-Reset: 1705305600
```

---

## Interactive Documentation

Swagger UI available at: `http://localhost:8080/swagger-ui.html`

API Docs available at: `http://localhost:8080/api-docs`
