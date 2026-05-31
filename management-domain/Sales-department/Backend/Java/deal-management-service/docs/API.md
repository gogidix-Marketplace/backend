# Deal Management Service - API Documentation

## Base URL

```
/api/v1/deals
```

## Authentication

All endpoints require Bearer token authentication with tenant context.

## Deals API

### Create Deal

```http
POST /deals
Content-Type: application/json

{
  "dealName": "Enterprise Software License",
  "accountId": "acc-123",
  "accountName": "Acme Corp",
  "contactId": "contact-456",
  "contactName": "John Doe",
  "amount": 150000.00,
  "currency": "USD",
  "stage": "LEAD",
  "ownerId": "user-789",
  "expectedCloseDate": "2024-06-30",
  "priority": "HIGH",
  "description": "Enterprise software licensing deal"
}
```

**Response:** 201 Created

### Update Deal

```http
PUT /deals/{dealId}
```

### Advance Deal Stage

```http
POST /deals/{dealId}/advance-stage
Content-Type: application/json

{
  "notes": "Customer interested in proposal"
}
```

### Regress Deal Stage

```http
POST /deals/{dealId}/regress-stage
Content-Type: application/json

{
  "targetStage": "QUALIFIED",
  "reason": "Customer needs more time"
}
```

### Mark Deal as Won

```http
POST /deals/{dealId}/won
Content-Type: application/json

{
  "finalAmount": 145000.00,
  "notes": "Closed with discount"
}
```

### Mark Deal as Lost

```http
POST /deals/{dealId}/lost
Content-Type: application/json

{
  "lossReason": "PRICE",
  "lossDetails": "Competitor was 20% cheaper"
}
```

### Get Deal by ID

```http
GET /deals/{dealId}
```

### Get All Deals for Tenant

```http
GET /deals?page=0&size=20&status=OPEN
```

### Get Deals by Stage

```http
GET /deals/stage/{stage}?page=0&size=20
```

### Get Deals by Owner

```http
GET /deals/owner/{ownerId}?page=0&size=20
```

### Get Pipeline Value

```http
GET /deals/pipeline/value
```

**Response:** 200 OK
```json
{
  "totalPipeline": 2500000.00,
  "weightedPipeline": 850000.00,
  "byStage": {
    "LEAD": 500000.00,
    "QUALIFIED": 750000.00,
    "PROPOSAL": 1000000.00,
    "NEGOTIATION": 250000.00
  }
}
```

## Deal Products API

### Add Product to Deal

```http
POST /deals/{dealId}/products
Content-Type: application/json

{
  "productId": "prod-123",
  "productName": "Software License",
  "quantity": 10,
  "unitPrice": 15000.00,
  "discount": 0
}
```

### Remove Product from Deal

```http
DELETE /deals/{dealId}/products/{productId}
```

## Deal Activities API

### Add Activity

```http
POST /deals/{dealId}/activities
Content-Type: application/json

{
  "activityType": "CALL",
  "subject": "Discovery call",
  "description": "Discussed customer requirements",
  "duration": 30
}
```

### Get Deal Activities

```http
GET /deals/{dealId}/activities
```

## Competitors API

### Add Competitor

```http
POST /deals/{dealId}/competitors
Content-Type: application/json

{
  "competitorName": "Competitor Inc",
  "strength": "Lower price",
  "weakness": "Fewer features",
  "threatLevel": "HIGH"
}
```

## Approval API

### Request Approval

```http
POST /deals/{dealId}/request-approval
```

### Approve Deal

```http
POST /deals/{dealId}/approve
```

### Reject Deal

```http
POST /deals/{dealId}/reject
Content-Type: application/json

{
  "reason": "Discount too high"
}
```

## Enums

### DealStage
- LEAD
- QUALIFIED
- PROPOSAL
- NEGOTIATION
- VERBAL_COMMIT
- CLOSED_WON
- CLOSED_LOST

### DealPriority
- LOW
- MEDIUM
- HIGH
- CRITICAL

### DealStatus
- OPEN
- WON
- LOST
- ABANDONED
- ON_HOLD

### ApprovalStatus
- NOT_REQUIRED
- PENDING
- APPROVED
- REJECTED
