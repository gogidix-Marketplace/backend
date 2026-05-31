# API Documentation - Warehousing Domain

## Overview

This document provides comprehensive API documentation for all warehousing domain services. All services follow RESTful principles and use JSON for request/response bodies.

## Base URLs

| Environment | Base URL |
|-------------|----------|
| Local | `http://localhost:8080` |
| Staging | `https://staging-api.warehousing.gogidix.com` |
| Production | `https://api.warehousing.gogidix.com` |

## Common Headers

| Header | Description | Example |
|--------|-------------|---------|
| `X-Tenant-ID` | Tenant identifier (required for all requests) | `tenant-001` |
| `Authorization` | Bearer token for authentication | `Bearer eyJhbGc...` |
| `X-Request-ID` | Request tracing ID | `req-12345` |
| `Content-Type` | Request content type | `application/json` |

## Common Response Codes

| Code | Description |
|------|-------------|
| 200 | Success |
| 201 | Created |
| 204 | No Content |
| 400 | Bad Request |
| 401 | Unauthorized |
| 403 | Forbidden |
| 404 | Not Found |
| 409 | Conflict |
| 422 | Unprocessable Entity |
| 429 | Rate Limit Exceeded |
| 500 | Internal Server Error |

---

## Inventory Services

### Inventory Core Service

Base Path: `/api/v1/inventory`

#### Items

##### Create Item

```http
POST /api/v1/inventory/items
```

**Request Body:**
```json
{
  "sku": "ITEM-001",
  "name": "Product Name",
  "description": "Product description",
  "quantity": 100,
  "unitOfMeasure": "EA",
  "locationId": "ZONE-A-AISLE-01-SHELF-01-BIN-01",
  "reorderThreshold": 20,
  "reorderQuantity": 100,
  "attributes": {
    "category": "ELECTRONICS",
    "brand": "BrandName",
    "weight": 1.5,
    "weightUnit": "KG"
  }
}
```

**Response:** `201 Created`
```json
{
  "id": "507f1f77bcf86cd799439011",
  "sku": "ITEM-001",
  "name": "Product Name",
  "quantity": 100,
  "tenantId": "tenant-001",
  "createdAt": "2024-01-15T10:30:00Z",
  "updatedAt": "2024-01-15T10:30:00Z"
}
```

##### List Items

```http
GET /api/v1/inventory/items?page=0&size=20&sort=createdAt,desc
```

**Query Parameters:**
| Parameter | Type | Description |
|-----------|------|-------------|
| page | integer | Page number (default: 0) |
| size | integer | Page size (default: 20, max: 100) |
| sort | string | Sort field and direction |
| category | string | Filter by category |
| lowStock | boolean | Filter for items below reorder threshold |

**Response:** `200 OK`
```json
{
  "content": [
    {
      "id": "507f1f77bcf86cd799439011",
      "sku": "ITEM-001",
      "name": "Product Name",
      "quantity": 100
    }
  ],
  "page": 0,
  "size": 20,
  "totalElements": 150,
  "totalPages": 8
}
```

##### Get Item by ID

```http
GET /api/v1/inventory/items/{itemId}
```

**Response:** `200 OK`
```json
{
  "id": "507f1f77bcf86cd799439011",
  "sku": "ITEM-001",
  "name": "Product Name",
  "description": "Product description",
  "quantity": 100,
  "unitOfMeasure": "EA",
  "locationId": "ZONE-A-AISLE-01-SHELF-01-BIN-01",
  "tenantId": "tenant-001"
}
```

##### Update Item

```http
PUT /api/v1/inventory/items/{itemId}
```

**Request Body:** (partial update supported)
```json
{
  "name": "Updated Product Name",
  "description": "Updated description",
  "reorderThreshold": 30
}
```

**Response:** `200 OK`

##### Delete Item

```http
DELETE /api/v1/inventory/items/{itemId}
```

**Response:** `204 No Content`

#### Inventory Adjustments

##### Adjust Quantity

```http
POST /api/v1/inventory/adjust
```

**Request Body:**
```json
{
  "itemId": "507f1f77bcf86cd799439011",
  "quantity": 50,
  "reason": "STOCK_IN",
  "reference": "PO-12345",
  "notes": "Initial stock receipt"
}
```

**Reason Values:** `STOCK_IN`, `STOCK_OUT`, `SALE`, `RETURN`, `DAMAGED`, `ADJUSTMENT`, `COUNT`

**Response:** `200 OK`
```json
{
  "itemId": "507f1f77bcf86cd799439011",
  "previousQuantity": 100,
  "newQuantity": 150,
  "adjustment": 50,
  "transactionId": "txn-001"
}
```

#### Stock Operations

##### Check Availability

```http
GET /api/v1/inventory/availability?sku=ITEM-001&quantity=50
```

**Response:** `200 OK`
```json
{
  "sku": "ITEM-001",
  "available": true,
  "availableQuantity": 100,
  "requestedQuantity": 50,
  "locations": [
    {
      "locationId": "ZONE-A-AISLE-01-SHELF-01-BIN-01",
      "availableQuantity": 100
    }
  ]
}
```

##### Allocate Stock

```http
POST /api/v1/inventory/allocate
```

**Request Body:**
```json
{
  "orderId": "ORDER-001",
  "items": [
    {
      "sku": "ITEM-001",
      "quantity": 50
    }
  ]
}
```

**Response:** `200 OK`
```json
{
  "orderId": "ORDER-001",
  "allocations": [
    {
      "sku": "ITEM-001",
      "allocatedQuantity": 50,
      "locationId": "ZONE-A-AISLE-01-SHELF-01-BIN-01",
      "reservationId": "res-001"
    }
  ]
}
```

#### Queries

##### Low Stock Items

```http
GET /api/v1/inventory/items/low-stock?threshold=20
```

**Response:** `200 OK`
```json
[
  {
    "id": "507f1f77bcf86cd799439011",
    "sku": "ITEM-002",
    "quantity": 15,
    "reorderThreshold": 20
  }
]
```

##### Audit Trail

```http
GET /api/v1/inventory/audit?itemId=507f1f77bcf86cd799439011
```

**Response:** `200 OK`
```json
{
  "records": [
    {
      "transactionId": "txn-001",
      "itemId": "507f1f77bcf86cd799439011",
      "type": "ADJUSTMENT",
      "previousQuantity": 100,
      "newQuantity": 150,
      "reason": "STOCK_IN",
      "performedBy": "user-001",
      "performedAt": "2024-01-15T10:30:00Z"
    }
  ]
}
```

### Stock Service

Base Path: `/api/v1/stock`

#### Reserve Stock

```http
POST /api/v1/stock/reserve
```

**Request Body:**
```json
{
  "orderId": "ORDER-001",
  "items": [
    {
      "sku": "ITEM-001",
      "quantity": 10,
      "durationMinutes": 30
    }
  ]
}
```

**Response:** `200 OK`
```json
{
  "reservationId": "res-001",
  "expiresAt": "2024-01-15T11:00:00Z"
}
```

#### Release Stock

```http
POST /api/v1/stock/release
```

**Request Body:**
```json
{
  "reservationId": "res-001"
}
```

**Response:** `200 OK`

#### Transfer Stock

```http
POST /api/v1/stock/transfer
```

**Request Body:**
```json
{
  "itemId": "507f1f77bcf86cd799439011",
  "quantity": 50,
  "fromLocation": "ZONE-A-AISLE-01-SHELF-01-BIN-01",
  "toLocation": "ZONE-B-AISLE-02-SHELF-03-BIN-05",
  "reason": "REPLENISHMENT"
}
```

**Response:** `200 OK`

### Location Service

Base Path: `/api/v1/location`

#### Locations

##### Create Location

```http
POST /api/v1/location/locations
```

**Request Body:**
```json
{
  "code": "ZONE-A-AISLE-01-SHELF-01-BIN-01",
  "type": "PICKING",
  "zoneId": "ZONE-A",
  "warehouseId": "WH-001",
  "capacity": 1000,
  "coordinates": {
    "zone": "A",
    "aisle": "01",
    "shelf": "01",
    "bin": "01"
  }
}
```

**Location Types:** `PICKING`, `STORAGE`, `STAGING`, `RECEIVING`, `SHIPPING`

**Response:** `201 Created`

##### Get Location Hierarchy

```http
GET /api/v1/location/hierarchy?warehouseId=WH-001
```

**Response:** `200 OK`
```json
{
  "warehouseId": "WH-001",
  "zones": [
    {
      "id": "ZONE-A",
      "name": "Zone A",
      "aisles": [
        {
          "id": "AISLE-01",
          "shelves": [
            {
              "id": "SHELF-01",
              "bins": ["BIN-01", "BIN-02"]
            }
          ]
        }
      ]
    }
  ]
}
```

---

## Fulfillment Services

### Fulfillment Core Service

Base Path: `/api/v1/fulfillment`

#### Orders

##### Create Order

```http
POST /api/v1/fulfillment/orders
```

**Request Body:**
```json
{
  "orderNumber": "ORDER-001",
  "customer": {
    "id": "customer-001",
    "name": "John Doe",
    "email": "john@example.com",
    "phone": "+1234567890"
  },
  "items": [
    {
      "sku": "ITEM-001",
      "quantity": 10,
      "price": 29.99
    }
  ],
  "shipping": {
    "method": "STANDARD",
    "address": {
      "name": "John Doe",
      "street": "123 Main St",
      "city": "Anytown",
      "state": "CA",
      "zip": "12345",
      "country": "USA"
    }
  },
  "priority": "NORMAL"
}
```

**Shipping Methods:** `STANDARD`, `EXPRESS`, `OVERNIGHT`, `SAME_DAY`

**Priority Levels:** `LOW`, `NORMAL`, `HIGH`, `URGENT`

**Response:** `201 Created`
```json
{
  "id": "507f1f77bcf86cd799439011",
  "orderNumber": "ORDER-001",
  "status": "PENDING",
  "createdAt": "2024-01-15T10:30:00Z"
}
```

**Order Statuses:** `PENDING`, `PICKING`, `PACKED`, `SHIPPED`, `DELIVERED`, `CANCELLED`, `RETURNED`

##### Start Fulfillment

```http
POST /api/v1/fulfillment/orders/{orderId}/start
```

**Response:** `200 OK`

##### Get Order Status

```http
GET /api/v1/fulfillment/orders/{orderId}/status
```

**Response:** `200 OK`
```json
{
  "orderId": "507f1f77bcf86cd799439011",
  "status": "PICKING",
  "timeline": [
    {
      "status": "PENDING",
      "timestamp": "2024-01-15T10:30:00Z",
      "performedBy": "system"
    },
    {
      "status": "PICKING",
      "timestamp": "2024-01-15T10:35:00Z",
      "performedBy": "picker-001"
    }
  ]
}
```

#### Picking

##### Assign Picking Task

```http
POST /api/v1/fulfillment/picking/assign
```

**Request Body:**
```json
{
  "orderId": "ORDER-001",
  "pickerId": "PICKER-001",
  "zone": "ZONE-A"
}
```

**Response:** `200 OK`
```json
{
  "taskId": "task-001",
  "orderId": "ORDER-001",
  "pickerId": "PICKER-001",
  "status": "ASSIGNED",
  "items": [
    {
      "sku": "ITEM-001",
      "quantity": 10,
      "location": "ZONE-A-AISLE-01-SHELF-01-BIN-01"
    }
  ]
}
```

##### Complete Picking

```http
POST /api/v1/fulfillment/picking/complete
```

**Request Body:**
```json
{
  "orderId": "ORDER-001",
  "pickerId": "PICKER-001",
  "items": [
    {
      "sku": "ITEM-001",
      "quantityPicked": 10,
      "locationId": "ZONE-A-AISLE-01-SHELF-01-BIN-01",
      "barcodeScanned": true
    }
  ],
  "notes": "All items picked successfully"
}
```

**Response:** `200 OK`

#### Packing

##### Start Packing

```http
POST /api/v1/fulfillment/packing/start
```

**Request Body:**
```json
{
  "orderId": "ORDER-001",
  "packerId": "PACKER-001"
}
```

**Response:** `200 OK`

##### Complete Packing

```http
POST /api/v1/fulfillment/packing/complete
```

**Request Body:**
```json
{
  "orderId": "ORDER-001",
  "packerId": "PACKER-001",
  "boxes": [
    {
      "boxType": "STANDARD",
      "weight": 5.5,
      "dimensions": {
        "length": 40,
        "width": 30,
        "height": 20,
        "unit": "CM"
      },
      "items": [
        {
          "sku": "ITEM-001",
          "quantity": 10
        }
      ]
    }
  ]
}
```

**Box Types:** `ENVELOPE`, `SMALL`, `STANDARD`, `LARGE`, `PALLET`

**Response:** `200 OK`

---

## Storage Services

### Space Service

Base Path: `/api/v1/storage`

#### Spaces

##### Create Space

```http
POST /api/v1/storage/spaces
```

**Request Body:**
```json
{
  "spaceCode": "SPACE-001",
  "spaceType": "CLIMATE_CONTROLLED",
  "sizeCategory": "MEDIUM",
  "dimensions": {
    "length": 10,
    "width": 8,
    "height": 8,
    "unit": "FT"
  },
  "capacity": 640,
  "warehouseId": "WH-001",
  "zoneId": "ZONE-CC-A"
}
```

**Space Types:** `STANDARD`, `CLIMATE_CONTROLLED`, `SECURITY`, `VEHICLE`, `OUTDOOR`

**Size Categories:** `SMALL`, `MEDIUM`, `LARGE`, `XL`, `CUSTOM`

**Response:** `201 Created`

##### Check Availability

```http
POST /api/v1/storage/availability/check
```

**Request Body:**
```json
{
  "spaceType": "CLIMATE_CONTROLLED",
  "sizeCategory": "MEDIUM",
  "startDate": "2024-02-01",
  "duration": 30,
  "durationUnit": "DAYS"
}
```

**Response:** `200 OK`
```json
{
  "available": true,
  "totalAvailable": 15,
  "availableSpaces": [
    {
      "spaceId": "space-001",
      "spaceCode": "SPACE-001",
      "price": 299.99,
      "currency": "USD"
    }
  ]
}
```

#### Allocation

##### Allocate Space

```http
POST /api/v1/storage/allocate
```

**Request Body:**
```json
{
  "spaceId": "space-001",
  "customerId": "customer-001",
  "startDate": "2024-02-01",
  "duration": 90,
  "durationUnit": "DAYS"
}
```

**Response:** `200 OK`
```json
{
  "bookingId": "booking-001",
  "spaceId": "space-001",
  "startDate": "2024-02-01",
  "endDate": "2024-05-01",
  "accessCode": "1234"
}
```

### Pricing Service

Base Path: `/api/v1/pricing`

#### Calculate Price

```http
POST /api/v1/pricing/calculate
```

**Request Body:**
```json
{
  "spaceType": "CLIMATE_CONTROLLED",
  "sizeCategory": "MEDIUM",
  "duration": 90,
  "durationUnit": "DAYS",
  "features": ["CLIMATE_CONTROLLED", "SECURITY_24_7"]
}
```

**Response:** `200 OK`
```json
{
  "basePrice": 8997.00,
  "discounts": [
    {
      "type": "LONG_TERM",
      "amount": 899.70,
      "percentage": 10
    }
  ],
  "featureCharges": [
    {
      "feature": "CLIMATE_CONTROLLED",
      "charge": 899.70
    }
  ],
  "totalPrice": 9096.30,
  "currency": "USD",
  "priceBreakdown": [
    {
      "period": "months",
      "count": 3,
      "amount": 3032.10
    }
  ]
}
```

#### Get Quote

```http
POST /api/v1/pricing/quote
```

**Request Body:**
```json
{
  "spaceType": "STANDARD",
  "sizeCategory": "LARGE",
  "duration": 180,
  "durationUnit": "DAYS",
  "customerTier": "PREMIUM"
}
```

**Response:** `200 OK`
```json
{
  "quoteId": "quote-001",
  "validUntil": "2024-01-22T10:30:00Z",
  "totalPrice": 17994.00,
  "currency": "USD",
  "customerDiscount": 15
}
```

### Access Service

Base Path: `/api/v1/access`

#### Access Codes

##### Generate Access Code

```http
POST /api/v1/access/generate
```

**Request Body:**
```json
{
  "bookingId": "booking-001",
  "accessType": "TEMPORARY",
  "validFrom": "2024-02-01T08:00:00Z",
  "validUntil": "2024-02-01T20:00:00Z",
  "pinType": "4_DIGIT"
}
```

**Access Types:** `PERMANENT`, `TEMPORARY`, `ONE_TIME`

**Response:** `200 OK`
```json
{
  "accessCode": "1234",
  "bookingId": "booking-001",
  "validFrom": "2024-02-01T08:00:00Z",
  "validUntil": "2024-02-01T20:00:00Z"
}
```

##### Validate Access Code

```http
POST /api/v1/access/validate
```

**Request Body:**
```json
{
  "bookingId": "booking-001",
  "accessCode": "1234",
  "timestamp": "2024-02-01T10:00:00Z"
}
```

**Response:** `200 OK`
```json
{
  "valid": true,
  "grantAccess": true,
  "bookingId": "booking-001",
  "spaceId": "space-001"
}
```

##### Access Log

```http
POST /api/v1/access/log
```

**Request Body:**
```json
{
  "bookingId": "booking-001",
  "eventType": "ENTRY",
  "timestamp": "2024-02-01T10:00:00Z",
  "method": "PIN_CODE",
  "success": true
}
```

**Event Types:** `ENTRY`, `EXIT`, `FAILED_ATTEMPT`

**Response:** `201 Created`

---

## Public API Services

### Public Booking Service

Base Path: `/api/v1/public/booking`

#### Book Storage

```http
POST /api/v1/public/booking/book
```

**Request Body:**
```json
{
  "spaceType": "STANDARD",
  "sizeCategory": "MEDIUM",
  "startDate": "2024-02-01",
  "duration": 90,
  "durationUnit": "DAYS",
  "customer": {
    "name": "John Doe",
    "email": "john@example.com",
    "phone": "+1234567890"
  },
  "paymentInfo": {
    "method": "CREDIT_CARD",
    "token": "tok_1234567890"
  }
}
```

**Response:** `201 Created`
```json
{
  "bookingId": "booking-001",
  "spaceCode": "SPACE-001",
  "accessCode": "1234",
  "totalPrice": 8997.00,
  "currency": "USD",
  "status": "CONFIRMED"
}
```

### Public Availability Service

Base Path: `/api/v1/public/availability`

#### Check Availability

```http
GET /api/v1/public/availability?spaceType=STANDARD&sizeCategory=MEDIUM&startDate=2024-02-01&duration=30
```

**Response:** `200 OK`
```json
{
  "available": true,
  "spaces": [
    {
      "spaceCode": "SPACE-001",
      "warehouse": {
        "id": "WH-001",
        "name": "Downtown Warehouse",
        "address": {
          "street": "123 Warehouse St",
          "city": "Anytown",
          "state": "CA",
          "zip": "12345"
        }
      },
      "price": 299.99,
      "currency": "USD"
    }
  ]
}
```

### Public Pricing Service

Base Path: `/api/v1/public/pricing`

#### Get Price Estimate

```http
GET /api/v1/public/pricing/estimate?spaceType=CLIMATE_CONTROLLED&sizeCategory=MEDIUM&duration=90
```

**Response:** `200 OK`
```json
{
  "estimatedPrice": 9096.30,
  "currency": "USD",
  "breakdown": {
    "dailyRate": 99.99,
    "duration": 90,
    "basePrice": 8999.10,
    "fees": 97.20
  }
}
```

---

## Analytics Services

### Inventory Analytics Service

Base Path: `/api/v1/analytics/inventory`

#### Get Turnover Report

```http
GET /api/v1/analytics/inventory/turnover?period=LAST_30_DAYS
```

**Response:** `200 OK`
```json
{
  "period": "LAST_30_DAYS",
  "turnoverRate": 4.5,
  "items": [
    {
      "sku": "ITEM-001",
      "name": "Product Name",
      "turnoverRate": 6.2,
      "averageStock": 500,
      "sold": 3100
    }
  ]
}
```

#### Get ABC Analysis

```http
GET /api/v1/analytics/inventory/abc-analysis
```

**Response:** `200 OK`
```json
{
  "categories": {
    "A": {
      "itemCount": 150,
      "revenuePercentage": 75,
      "items": ["ITEM-001", "ITEM-002"]
    },
    "B": {
      "itemCount": 300,
      "revenuePercentage": 20,
      "items": ["ITEM-003", "ITEM-004"]
    },
    "C": {
      "itemCount": 550,
      "revenuePercentage": 5,
      "items": ["ITEM-005"]
    }
  }
}
```

### Fulfillment Analytics Service

Base Path: `/api/v1/analytics/fulfillment`

#### Get Performance Metrics

```http
GET /api/v1/analytics/fulfillment/performance?period=LAST_7_DAYS
```

**Response:** `200 OK`
```json
{
  "period": "LAST_7_DAYS",
  "metrics": {
    "totalOrders": 1250,
    "fulfilledOrders": 1180,
    "fulfillmentRate": 94.4,
    "averageCycleTime": {
      "hours": 8.5,
      "target": 12
    },
    "pickingEfficiency": {
      "itemsPerHour": 95,
      "target": 80
    },
    "packingEfficiency": {
      "ordersPerHour": 45,
      "target": 40
    }
  }
}
```

---

## Health & Monitoring Endpoints

All services expose actuator endpoints:

### Health Check

```http
GET /actuator/health
```

**Response:** `200 OK`
```json
{
  "status": "UP",
  "components": {
    "db": {
      "status": "UP",
      "details": {
        "database": "MongoDB",
        "validationQuery": "OK"
      }
    },
    "kafka": {
      "status": "UP"
    }
  }
}
```

### Metrics (Prometheus)

```http
GET /actuator/prometheus
```

Returns Prometheus-compatible metrics.

### Info

```http
GET /actuator/info
```

**Response:** `200 OK`
```json
{
  "app": {
    "name": "inventory-core-service",
    "version": "1.0.0"
  }
}
```

---

## Error Response Format

All error responses follow this format:

```json
{
  "timestamp": "2024-01-15T10:30:00Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "path": "/api/v1/inventory/items",
  "details": [
    {
      "field": "sku",
      "message": "SKU is required"
    }
  ],
  "requestId": "req-12345"
}
```

---

## Rate Limits

| Tier | Requests/Minute | Burst |
|------|-----------------|-------|
| Free | 60 | 10 |
| Standard | 600 | 100 |
| Premium | 6000 | 1000 |

Rate limit headers are included in responses:
```
X-RateLimit-Limit: 600
X-RateLimit-Remaining: 599
X-RateLimit-Reset: 1642258800
```

---

## Webhooks

Services support webhook notifications for events:

### Configure Webhook

```http
POST /api/v1/webhooks
```

**Request Body:**
```json
{
  "url": "https://your-domain.com/webhooks",
  "events": ["order.created", "order.shipped"],
  "secret": "webhook-secret-key"
}
```

**Webhook Signature:** `X-Signature` header contains HMAC SHA256 signature.

### Webhook Payload

```json
{
  "eventId": "evt-001",
  "eventType": "order.created",
  "timestamp": "2024-01-15T10:30:00Z",
  "data": {
    "orderId": "ORDER-001",
    "orderNumber": "ORDER-001",
    "status": "PENDING"
  }
}
```
