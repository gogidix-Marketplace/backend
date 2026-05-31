# GOGIDIX CORPORATE WEBSITE - DEVELOPER PORTAL

**Version:** 1.0
**Domain:** Corporate Website
**Frontend:** corporate-website-public
**Last Updated:** 2025-02-08

---

## TABLE OF CONTENTS

1. [Developer Portal Overview](#developer-portal-overview)
2. [API Documentation Structure](#api-documentation-structure)
3. [SDK Documentation](#sdk-documentation)
4. [Sandbox Environment](#sandbox-environment)
5. [Developer Resources](#developer-resources)

---

## 1. DEVELOPER PORTAL OVERVIEW

### Portal Objectives

| Objective | Description | Target Audience |
|-----------|-------------|-----------------|
| **Onboarding** | Get developers started quickly | New developers |
| **Reference** | Complete API documentation | All developers |
| **Testing** | Sandbox for safe experimentation | Developers evaluating APIs |
| **Support** | Community and help resources | All developers |

### Developer Journey Flow

```
┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐
│ Developer       │────>│ Lands on       │────>│ Quick Start     │
│ Discovers APIs  │     │ Developer Portal│     │ Guide          │
└─────────────────┘     └─────────────────┘     └─────────────────┘
                                                        │
                        ┌───────────────────────────────┴───────────────────────────────┐
                        ▼                                                               ▼
                 ┌──────────────┐                                               ┌──────────────┐
                 │ Get Sandbox  │                                               │ Read API     │
                 │ Credentials  │                                               │ Reference    │
                 └──────┬───────┘                                               └──────┬───────┘
                        │                                                              │
                        ▼                                                              │
                 ┌──────────────┐                                                     │
                 │ Download SDK  │                                                     │
                 │ or Use cURL   │                                                     │
                 └──────┬───────┘                                                     │
                        │                                                              │
                        ▼                                                              │
                 ┌──────────────┐                                                     │
                 │ Make First    │                                                     │
                 │ API Call      │                                                     │
                 └──────┬───────┘                                                     │
                        │                                                              │
                        ▼                                                              │
                 ┌──────────────┐                                                     │
                 │ Explore       │←────────────────────────────────────────────────────┘
                 │ Full Features │
                 └──────┬───────┘
                        │
                        ▼
                 ┌──────────────┐
                 │ Go to        │
                 │ Production   │
                 └──────────────┘
```

### Developer Portal Navigation

```
Developer Portal Navigation:
┌────────────────────────────────────────────────────────────────────────────────────────┐
│  [Gogidix Developers]  Docs ▼  Guides ▼  SDKs ▼  Community ▼        [Sandbox] [Login]   │
└────────────────────────────────────────────────────────────────────────────────────────┘

Sidebar Navigation:
┌────────────────────────────┐
│  Getting Started            │
│  ├─ Quick Start             │
│  ├─ Authentication          │
│  ├─ Your First API Call     │
│  └─ Error Handling          │
│                            │
│  API Reference              │
│  ├─ Courier API            │
│  ├─ Warehouse API           │
│  ├─ E-commerce API          │
│  ├─ Business Operations API │
│  └─ Enterprise API          │
│                            │
│  SDKs                       │
│  ├─ JavaScript/Node.js      │
│  ├─ Python                  │
│  ├─ PHP                     │
│  ├─ Go                      │
│  ├─ Java                    │
│  └─ .NET                    │
│                            │
│  Guides                     │
│  ├─ Webhooks                │
│  ├─ Rate Limits             │
│  ├─ Best Practices          │
│  └─ Migration Guides        │
│                            │
│  Resources                  │
│  ├─ Sandbox                 │
│  ├─ Status                  │
│  ├─ Changelog               │
│  └─ Support                 │
└────────────────────────────┘
```

---

## 2. API DOCUMENTATION STRUCTURE

### API Reference Page Layout

```
┌────────────────────────────────────────────────────────────────────────────────────────────────┐
│  [← Developers]  Courier API v1.0                                          [Run in Sandbox]    │
├────────────────────────────────────────────────────────────────────────────────────────────────┤
│                                                                                                │
│  COURIER API                                                                                   │
│  Complete courier operations API with 25+ integrated services                                  │
│                                                                                                │
│  [Base URL] https://api.gogidix.com/courier/v1                                                 │
│                                                                                                │
│  ┌────────────────────────────────────────────────────────────────────────────────────────┐   │
│  │  Authentication                                                                         │   │
│  │  ──────────────────────────────────────────────────────────────────────────────────── │   │
│  │  All API requests require authentication using an API key in the header:                │   │
│  │                                                                                         │   │
│  │  Authorization: Bearer YOUR_API_KEY                                                      │   │
│  │                                                                                         │   │
│  │  [Get API Key]                                                                         │   │
│  └────────────────────────────────────────────────────────────────────────────────────────┘   │
│                                                                                                │
│  ┌────────────────────────────────────────────────────────────────────────────────────────┐   │
│  │  Rate Limiting                                                                          │   │
│  │  ──────────────────────────────────────────────────────────────────────────────────── │   │
│  │  • Free Tier: 100 requests/minute                                                       │   │
│  │  • Professional: 1,000 requests/minute                                                   │   │
│  │  • Enterprise: Custom limits                                                            │   │
│  │                                                                                         │   │
│  │  Rate limit headers are included in every response:                                     │   │
│  │  X-RateLimit-Limit: 1000                                                                │   │
│  │  X-RateLimit-Remaining: 999                                                              │   │
│  │  X-RateLimit-Reset: 1644326400                                                           │   │
│  └────────────────────────────────────────────────────────────────────────────────────────┘   │
│                                                                                                │
│  ENDPOINTS                                                                                     │
│  ┌────────────────────────────────────────────────────────────────────────────────────────┐   │
│  │  SHIPMENTS                                                                              │   │
│  │  ──────────────────────────────────────────────────────────────────────────────────── │   │
│  │                                                                                         │   │
│  │  POST   /shipments              Create a new shipment                                    │   │
│  │  GET    /shipments              List all shipments                                       │   │
│  │  GET    /shipments/:id          Get shipment details                                    │   │
│  │  PATCH  /shipments/:id          Update shipment                                         │   │
│  │  DELETE /shipments/:id          Cancel shipment                                         │   │
│  │  POST   /shipments/:id/track    Get real-time tracking                                  │   │
│  └────────────────────────────────────────────────────────────────────────────────────────┘   │
│                                                                                                │
│  ┌────────────────────────────────────────────────────────────────────────────────────────┐   │
│  │  DRIVERS                                                                                │   │
│  │  ──────────────────────────────────────────────────────────────────────────────────── │   │
│  │                                                                                         │   │
│  │  GET    /drivers                List all drivers                                        │   │
│  │  GET    /drivers/:id            Get driver details                                      │   │
│  │  PATCH  /drivers/:id            Update driver status                                    │   │
│  │  GET    /drivers/:id/location   Get driver location                                     │   │
│  └────────────────────────────────────────────────────────────────────────────────────────┘   │
│                                                                                                │
│  ┌────────────────────────────────────────────────────────────────────────────────────────┐   │
│  │  ROUTES                                                                                 │   │
│  │  ──────────────────────────────────────────────────────────────────────────────────── │   │
│  │                                                                                         │   │
│  │  POST   /routes/optimize        Calculate optimal route                                  │   │
│  │  GET    /routes/:id             Get route details                                       │   │
│  │  POST   /routes/:id/assign      Assign route to driver                                  │   │
│  └────────────────────────────────────────────────────────────────────────────────────────┘   │
│                                                                                                │
│  [View All Endpoints →]                                                                      │
│                                                                                                │
└────────────────────────────────────────────────────────────────────────────────────────────────┘
```

### Endpoint Detail Page

```
┌────────────────────────────────────────────────────────────────────────────────────────────────┐
│  [← Courier API]  POST /shipyards                                            [Run in Sandbox]  │
├────────────────────────────────────────────────────────────────────────────────────────────────┤
│                                                                                                │
│  Create a new shipment                                                                         │
│  ──────────────────────────────────────────────────────────────────────────────────────────── │
│  Creates a new shipment with package details, origin, destination, and optional services.     │
│                                                                                                │
│  ┌────────────────────────────────────────────────────────────────────────────────────────┐   │
│  │  Request                                                                               │   │
│  ├────────────────────────────────────────────────────────────────────────────────────────┤   │
│  │                                                                                        │   │
│  │  POST /courier/v1/shipments                                                           │   │
│  │                                                                                        │   │
│  │  Headers:                                                                              │   │
│  │  Authorization: Bearer {api_key}                                                       │   │
│  │  Content-Type: application/json                                                       │   │
│  │  Idempotency-Key: {unique_key}  (optional)                                             │   │
│  │                                                                                        │   │
│  │  Body:                                                                                 │   │
│  │  {                                                                                     │   │
│  │    "origin": {                                                                        │   │
│  │      "address": "123 Main Street",                                                     │   │
│  │      "city": "Lagos",                                                                  │   │
│  │      "state": "Lagos",                                                                │   │
│  │      "country": "NG",                                                                 │   │
│  │      "postalCode": "100001",                                                          │   │
│  │      "coordinates": {                                                                 │   │
│  │        "latitude": 6.5244,                                                            │   │
│  │        "longitude": 3.3792                                                            │   │
│  │      }                                                                                │   │
│  │    },                                                                                 │   │
│  │    "destination": {                                                                   │   │
│  │      "address": "456 Oak Avenue",                                                     │   │
│  │      "city": "Accra",                                                                 │   │
│  │      "country": "GH",                                                                 │   │
│  │      "postalCode": "00233"                                                            │   │
│  │    },                                                                                 │   │
│  │    "package": {                                                                        │   │
│  │      "weight": {                                                                      │   │
│  │        "value": 2.5,                                                                  │   │
│  │        "unit": "kg"                                                                   │   │
│  │      },                                                                               │   │
│  │      "dimensions": {                                                                  │   │
│  │        "length": 30,                                                                  │   │
│  │        "width": 20,                                                                   │   │
│  │        "height": 15,                                                                  │   │
│  │        "unit": "cm"                                                                   │   │
│  │      },                                                                               │   │
│  │      "description": "Electronics - Fragile",                                          │   │
│  │      "value": {                                                                       │   │
│  │        "amount": 50000,                                                               │   │
│  │        "currency": "NGN"                                                              │   │
│  │      },                                                                               │   │
│  │      "items": [                                                                       │   │
│  │        {                                                                              │   │
│  │          "description": "Laptop",                                                     │   │
│  │          "quantity": 1,                                                               │   │
│  │          "weight": 2.0                                                                │   │
│  │        }                                                                              │   │
│  │      ]                                                                                │   │
│  │    },                                                                                 │   │
│  │    "services": [                                                                      │   │
│  │      "signature_required",                                                            │   │
│  │      "insurance"                                                                     │   │
│  │    ],                                                                                 │   │
│  │    "recipient": {                                                                     │   │
│  │      "name": "Jane Smith",                                                            │   │
│  │      "phone": "+233201234567",                                                        │   │
│  │      "email": "jane.smith@example.com",                                              │   │
│  │      "notify": true                                                                  │   │
│  │    },                                                                                 │   │
│  │    "metadata": {                                                                      │   │
│  │      "reference": "ORDER-12345",                                                      │   │
│  │      "customer_id": "CUST-001"                                                        │   │
│  │    }                                                                                  │   │
│  │  }                                                                                    │   │
│  └────────────────────────────────────────────────────────────────────────────────────────┘   │
│                                                                                                │
│  ┌────────────────────────────────────────────────────────────────────────────────────────┐   │
│  │  Response                                                                              │   │
│  ├────────────────────────────────────────────────────────────────────────────────────────┤   │
│  │                                                                                        │   │
│  │  201 Created                                                                          │   │
│  │                                                                                        │   │
│  │  {                                                                                     │   │
│  │    "id": "shp_abc123xyz456",                                                           │   │
│  │    "trackingNumber": "GG-7890123456",                                                  │   │
│  │    "status": "created",                                                                │   │
│  │    "createdAt": "2025-02-08T10:00:00Z",                                                │   │
│  │    "estimatedDelivery": "2025-02-10T18:00:00Z",                                         │   │
│  │    "cost": {                                                                           │   │
│  │      "amount": 2500,                                                                   │   │
│  │      "currency": "NGN",                                                                │   │
│  │      "breakdown": [                                                                    │   │
│  │        { "type": "base_fare", "amount": 1500 },                                       │   │
│  │        { "type": "weight_fee", "amount": 500 },                                         │   │
│  │        { "type": "distance_fee", "amount": 300 },                                       │   │
│  │        { "type": "insurance", "amount": 200 }                                          │   │
│  │      ]                                                                                 │   │
│  │    },                                                                                  │   │
│  │    "trackingUrl": "https://track.gogidix.com/GG-7890123456"                           │   │
│  │  }                                                                                     │   │
│  └────────────────────────────────────────────────────────────────────────────────────────┘   │
│                                                                                                │
│  ┌────────────────────────────────────────────────────────────────────────────────────────┐   │
│  │  Error Responses                                                                       │   │
│  ├────────────────────────────────────────────────────────────────────────────────────────┤   │
│  │                                                                                        │   │
│  │  400 Bad Request                                                                       │   │
│  │  {                                                                                     │   │
│  │    "error": {                                                                          │   │
│  │      "code": "INVALID_ADDRESS",                                                        │   │
│  │      "message": "The origin address could not be verified",                            │   │
│  │      "details": {                                                                      │   │
│  │        "field": "origin.address"                                                       │   │
│  │      }                                                                                │   │
│  │    }                                                                                  │   │
│  │  }                                                                                     │   │
│  └────────────────────────────────────────────────────────────────────────────────────────┘   │
│                                                                                                │
│  [Try in Sandbox] [Copy as cURL] [View in Postman]                                         │
│                                                                                                │
└────────────────────────────────────────────────────────────────────────────────────────────────┘
```

---

## 3. SDK DOCUMENTATION

### SDK Reference Page

```
┌────────────────────────────────────────────────────────────────────────────────────────────────┐
│  [← Developers]  JavaScript/Node.js SDK                                          [npm Package]  │
├────────────────────────────────────────────────────────────────────────────────────────────────┤
│                                                                                                │
│  @gogidix/sdk                                                                                  │
│  ──────────────────────────────────────────────────────────────────────────────────────────── │
│  Official JavaScript/Node.js SDK for Gogidix APIs                                              │
│                                                                                                │
│  Version: 2.1.0 • License: MIT • Repository: github.com/gogidix/js-sdk                        │
│                                                                                                │
│  ┌────────────────────────────────────────────────────────────────────────────────────────┐   │
│  │  Installation                                                                          │   │
│  ├────────────────────────────────────────────────────────────────────────────────────────┤   │
│  │                                                                                        │   │
│  │  # npm                                                                                 │   │
│  │  npm install @gogidix/sdk                                                             │   │
│  │                                                                                        │   │
│  │  # yarn                                                                                │   │
│  │  yarn add @gogidix/sdk                                                                │   │
│  │                                                                                        │   │
│  │  # pnpm                                                                                │   │
│  │  pnpm add @gogidix/sdk                                                                │   │
│  └────────────────────────────────────────────────────────────────────────────────────────┘   │
│                                                                                                │
│  ┌────────────────────────────────────────────────────────────────────────────────────────┐   │
│  │  Quick Start                                                                           │   │
│  ├────────────────────────────────────────────────────────────────────────────────────────┤   │
│  │                                                                                        │   │
│  │  import { Gogidix } from '@gogidix/sdk';                                              │   │
│  │                                                                                        │   │
│  │  const client = new Gogidix({                                                          │   │
│  │    apiKey: process.env.GOGIDIX_API_KEY,                                               │   │
│  │    environment: 'sandbox'  // or 'production'                                          │   │
│  │  });                                                                                   │   │
│  │                                                                                        │   │
│  │  // Create a shipment                                                                  │   │
│  │  const shipment = await client.courier.shipments.create({                              │   │
│  │    origin: {                                                                            │   │
│  │      address: '123 Main St',                                                           │   │
│  │      city: 'Lagos',                                                                    │   │
│  │      country: 'NG'                                                                     │   │
│  │    },                                                                                   │   │
│  │    destination: {                                                                       │   │
│  │      address: '456 Oak Ave',                                                           │   │
│  │      city: 'Accra',                                                                    │   │
│  │      country: 'GH'                                                                     │   │
│  │    },                                                                                   │   │
│  │    package: {                                                                           │   │
│  │      weight: 2.5,                                                                      │   │
│  │      dimensions: { length: 30, width: 20, height: 15 }                                  │   │
│  │    }                                                                                    │   │
│  │  });                                                                                    │   │
│  │                                                                                        │   │
│  │  console.log(shipment.trackingNumber);  // GG-7890123456                               │   │
│  └────────────────────────────────────────────────────────────────────────────────────────┘   │
│                                                                                                │
│  AVAILABLE SERVICES                                                                            │
│  ┌────────────────────────────────────────────────────────────────────────────────────────┐   │
│  │  client.courier       - Courier management API                                          │   │
│  │  client.warehouse     - Warehouse management API                                        │   │
│  │  client.ecommerce     - E-commerce API                                                   │   │
│  │  client.procurement   - Procurement API                                                   │   │
│  │  client.business      - Business operations API                                           │   │
│  │  client.enterprise    - Enterprise management API                                         │   │
│  └────────────────────────────────────────────────────────────────────────────────────────┘   │
│                                                                                                │
│  [Full Documentation] [API Reference] [Examples] [Changelog]                                  │
│                                                                                                │
└────────────────────────────────────────────────────────────────────────────────────────────────┘
```

### SDK Examples

```
┌────────────────────────────────────────────────────────────────────────────────────────────────┐
│  SDK Examples                                                                                 │
├────────────────────────────────────────────────────────────────────────────────────────────────┤
│                                                                                                │
│  ┌────────────────────────────────────────────────────────────────────────────────────────┐   │
│  │  Authentication                                                                         │   │
│  ├────────────────────────────────────────────────────────────────────────────────────────┤   │
│  │                                                                                        │   │
│  │  import { Gogidix } from '@gogidix/sdk';                                              │   │
│  │                                                                                        │   │
│  │  // Initialize with API key                                                             │   │
│  │  const client = new Gogidix({                                                          │   │
│  │    apiKey: 'your_api_key_here'                                                         │   │
│  │  });                                                                                   │   │
│  │                                                                                        │   │
│  │  // Use different environments                                                          │   │
│  │  const sandboxClient = new Gogidix({                                                   │   │
│  │    apiKey: 'sandbox_key',                                                              │   │
│  │    environment: 'sandbox'                                                              │   │
│  │  });                                                                                   │   │
│  │                                                                                        │   │
│  │  const prodClient = new Gogidix({                                                      │   │
│  │    apiKey: 'prod_key',                                                                │   │
│  │    environment: 'production'                                                           │   │
│  │  });                                                                                   │   │
│  └────────────────────────────────────────────────────────────────────────────────────────┘   │
│                                                                                                │
│  ┌────────────────────────────────────────────────────────────────────────────────────────┐   │
│  │  Webhooks                                                                               │   │
│  ├────────────────────────────────────────────────────────────────────────────────────────┤   │
│  │                                                                                        │   │
│  │  import { Gogidix, verifyWebhookSignature } from '@gogidix/sdk';                      │   │
│  │                                                                                        │   │
│  │  const client = new Gogidix({ apiKey: process.env.WEBHOOK_KEY });                     │   │
│  │                                                                                        │   │
│  │  // Express.js example                                                                 │   │
│  │  app.post('/webhooks', (req, res) => {                                                │   │
│  │    const signature = req.headers['x-gogidix-signature'];                               │   │
│  │                                                                                        │   │
│  │    try {                                                                                │   │
│  │      const event = verifyWebhookSignature(req.body, signature);                         │   │
│  │                                                                                        │   │
│  │      switch (event.type) {                                                             │   │
│  │        case 'shipment.delivered':                                                      │   │
│  │          await handleDelivery(event.data);                                             │   │
│  │          break;                                                                        │   │
│  │        case 'shipment.exception':                                                      │   │
│  │          await handleException(event.data);                                            │   │
│  │          break;                                                                        │   │
│  │      }                                                                                  │   │
│  │                                                                                        │   │
│  │      res.sendStatus(200);                                                             │   │
│  │    } catch (err) {                                                                     │   │
│  │      console.error('Webhook verification failed:', err);                               │   │
│  │      res.sendStatus(401);                                                             │   │
│  │    }                                                                                    │   │
│  │  });                                                                                    │   │
│  └────────────────────────────────────────────────────────────────────────────────────────┘   │
│                                                                                                │
│  [View All Examples →]                                                                       │
│                                                                                                │
└────────────────────────────────────────────────────────────────────────────────────────────────┘
```

---

## 4. SANDBOX ENVIRONMENT

### Sandbox Access Page

```
┌────────────────────────────────────────────────────────────────────────────────────────────────┐
│  [← Developers]  Sandbox Environment                                                            │
├────────────────────────────────────────────────────────────────────────────────────────────────┤
│                                                                                                │
│  SANDBOX ENVIRONMENT                                                                          │
│  ──────────────────────────────────────────────────────────────────────────────────────────── │
│  Test your integrations safely with our sandbox environment. No real transactions, no        │
│  charges, full API access.                                                                   │
│                                                                                                │
│  ┌────────────────────────────────────────────────────────────────────────────────────────┐   │
│  │  Your Sandbox Account                                                                   │   │
│  ├────────────────────────────────────────────────────────────────────────────────────────┤   │
│  │                                                                                        │   │
│  │  API Key: sbk_test_abc123def456ghi789                                                  [Regenerate]    │   │
│  │  API Secret: ********************************************    [Show] [Regenerate]    │   │
│  │                                                                                        │   │
│  │  Sandbox URL: https://api-sandbox.gogidix.com                                          │   │
│  │                                                                                        │   │
│  │  Test Credits: 1,000 / 1,000 remaining                                                  │   │
│  │  Reset Date: 2025-03-08                                                                  │   │
│  │                                                                                        │   │
│  │  [Request More Credits]                                                                │   │
│  └────────────────────────────────────────────────────────────────────────────────────────┘   │
│                                                                                                │
│  ┌────────────────────────────────────────────────────────────────────────────────────────┐   │
│  │  Quick Test                                                                             │   │
│  ├────────────────────────────────────────────────────────────────────────────────────────┤   │
│  │                                                                                        │   │
│  │  Test an API endpoint right from your browser:                                         │   │
│  │                                                                                        │   │
│  │  Endpoint: [POST /courier/v1/shipments ▼]                                              │   │
│  │                                                                                        │   │
│  │  [Send Test Request]                                                                   │   │
│  │                                                                                        │   │
│  │  Last Response:                                                                        │   │
│  │  {                                                                                     │   │
│  │    "id": "shp_test_123",                                                               │   │
│  │    "status": "created",                                                                │   │
│  │    "trackingNumber": "GG-TEST-001"                                                     │   │
│  │  }                                                                                     │   │
│  └────────────────────────────────────────────────────────────────────────────────────────┘   │
│                                                                                                │
│  ┌────────────────────────────────────────────────────────────────────────────────────────┐   │
│  │  Sandbox Features                                                                      │   │
│  ├────────────────────────────────────────────────────────────────────────────────────────┤   │
│  │  ✓ Full API access                                                                    │   │
│  │  ✓ Simulated responses (realistic data)                                              │   │
│  │  ✓ Error testing (trigger specific errors)                                           │   │
│  │  ✓ Webhook testing (test your endpoints)                                              │   │
│  │  ✓ Rate limit testing                                                                 │   │
│  │  ✓ No real transactions or charges                                                    │   │
│  └────────────────────────────────────────────────────────────────────────────────────────┘   │
│                                                                                                │
│  [View Sandbox Docs] [Test Webhooks] [Error Simulator]                                      │
│                                                                                                │
└────────────────────────────────────────────────────────────────────────────────────────────────┘
```

### Sandbox Error Simulator

```
┌────────────────────────────────────────────────────────────────────────────────────────────────┐
│  Error Simulator                                                                              │
│  ──────────────────────────────────────────────────────────────────────────────────────────── │
│  Test how your application handles different error scenarios                                 │
│                                                                                                │
│  ┌────────────────────────────────────────────────────────────────────────────────────────┐   │
│  │  Error Type to Simulate:                                                                │   │
│  │  ┌──────────────────────────────────────────────────────────────────────────────────┐ │   │
│  │  │ [4xx Errors ▼]                                                  [5xx Errors ▼] │ │   │
│  │  │                                                                  │                │ │   │
│  │  │  • 400 Bad Request                                              │                │ │   │
│  │  │  • 401 Unauthorized                                            │                │ │   │
│  │  │  • 403 Forbidden                                               │                │ │   │
│  │  │  • 404 Not Found                                               │                │ │   │
│  │  │  • 409 Conflict                                                │                │ │   │
│  │  │  • 422 Unprocessable Entity                                   │                │ │   │
│  │  │  • 429 Rate Limit Exceeded                                     │                │ │   │
│  │  │                                                                  │                │ │   │
│  │  │  Specific Error Code: [INVALID_ADDRESS ▼]                       │                │ │   │
│  │  └──────────────────────────────────────────────────────────────────────────────────┘ │   │
│  │                                                                                        │   │
│  │  Request to Test:                                                                       │   │
│  │  POST /courier/v1/shipments                                                            │   │
│  │                                                                                        │   │
│  │  [Simulate Error]                                                                      │   │
│  │                                                                                        │   │
│  │  Response:                                                                             │   │
│  │  {                                                                                     │   │
│  │    "error": {                                                                          │   │
│  │      "code": "INVALID_ADDRESS",                                                        │   │
│  │      "message": "The origin address could not be verified",                            │   │
│  │      "requestId": "req_test_123"                                                       │   │
│  │      "timestamp": "2025-02-08T10:00:00Z"                                               │   │
│  │    }                                                                                   │   │
│  │  }                                                                                     │   │
│  └────────────────────────────────────────────────────────────────────────────────────────┘   │
│                                                                                                │
└────────────────────────────────────────────────────────────────────────────────────────────────┘
```

---

## 5. DEVELOPER RESOURCES

### Status Page

```
┌────────────────────────────────────────────────────────────────────────────────────────────────┐
│  System Status                                   [Subscribe to Updates] [RSS Feed]           │
├────────────────────────────────────────────────────────────────────────────────────────────────┤
│                                                                                                │
│  ┌────────────────────────────────────────────────────────────────────────────────────────┐   │
│  │  ✓ All Systems Operational                                                            │   │
│  │  Last updated: 2 minutes ago                                                            │   │
│  └────────────────────────────────────────────────────────────────────────────────────────┘   │
│                                                                                                │
│  API SERVICES                                                                                  │
│  ┌────────────────────────────────────────────────────────────────────────────────────────┐   │
│  │  Courier API          │  ✓ Operational   │  99.99% uptime   │  Last: 7 days ago      │   │
│  │  Warehouse API        │  ✓ Operational   │  99.98% uptime   │  Last: 3 days ago      │   │
│  │  E-commerce API       │  ✓ Operational   │  99.95% uptime   │  1 hour ago            │   │
│  │  Business Ops API    │  ✓ Operational   │  99.97% uptime   │  Last: 5 days ago      │   │
│  │  Enterprise API      │  ✓ Operational   │  99.96% uptime   │  Last: 2 days ago      │   │
│  └────────────────────────────────────────────────────────────────────────────────────────┘   │
│                                                                                                │
│  INFRASTRUCTURE                                                                                │
│  ┌────────────────────────────────────────────────────────────────────────────────────────┐   │
│  │  API Gateway          │  ✓ Operational   │  100% uptime     │                        │   │
│  │  Webhook Service     │  ✓ Operational   │  99.99% uptime   │                        │   │
│  │  Auth Service        │  ✓ Operational   │  100% uptime     │                        │   │
│  │  Sandbox Environment │  ✓ Operational   │  100% uptime     │                        │   │
│  └────────────────────────────────────────────────────────────────────────────────────────┘   │
│                                                                                                │
│  SCHEDULED MAINTENANCE                                                                         │
│  ┌────────────────────────────────────────────────────────────────────────────────────────┐   │
│  │  📅 Courier API Maintenance                                                             │   │
│  │  Scheduled: Feb 15, 2025, 02:00-02:30 UTC                                              │   │
│  │  Expected impact: Brief downtime, requests will queue                                  │   │
│  │  [Add to Calendar]                                                                     │   │
│  └────────────────────────────────────────────────────────────────────────────────────────┘   │
│                                                                                                │
│  INCIDENT HISTORY (30 days)                                                                    │
│  ┌────────────────────────────────────────────────────────────────────────────────────────┐   │
│  │  Feb 5, 2025     │ E-commerce API │ Degraded Performance │ 23 minutes │ Resolved      │   │
│  └────────────────────────────────────────────────────────────────────────────────────────┘   │
│                                                                                                │
│  [status.gogidix.com] [Subscribe to Updates]                                                  │
│                                                                                                │
└────────────────────────────────────────────────────────────────────────────────────────────────┘
```

### Changelog

```
┌────────────────────────────────────────────────────────────────────────────────────────────────┐
│  Changelog                                                   [RSS Feed] [Subscribe to Email]  │
├────────────────────────────────────────────────────────────────────────────────────────────────┤
│                                                                                                │
│  ┌────────────────────────────────────────────────────────────────────────────────────────┐   │
│  │  February 8, 2025                                                    [v2.1.0]        │   │
│  │  ──────────────────────────────────────────────────────────────────────────────────── │   │
│  │                                                                                        │   │
│  │  New                                                                                   │   │
│  │  • Added batch shipment creation endpoint (POST /courier/v1/shipments/batch)           │   │
│  │  • Added predictive ETA based on traffic and weather data                              │   │
│  │  • Added web dashboard for tracking sandbox usage                                      │   │
│  │                                                                                        │   │
│  │  Improved                                                                              │   │
│  │  • Reduced API response time by 40% for warehouse endpoints                            │   │
│  │  • Enhanced error messages with actionable suggestions                                 │   │
│  │  • Updated SDKs with better TypeScript types                                           │   │
│  │                                                                                        │   │
│  │  Fixed                                                                                 │   │
│  │  • Fixed issue with timezone handling in scheduling APIs                               │   │
│  │  • Fixed webhook retry logic for certain edge cases                                    │   │
│  │                                                                                        │   │
│  └────────────────────────────────────────────────────────────────────────────────────────┘   │
│                                                                                                │
│  ┌────────────────────────────────────────────────────────────────────────────────────────┐   │
│  │  January 25, 2025                                                  [v2.0.5]        │   │
│  │  ──────────────────────────────────────────────────────────────────────────────────── │   │
│  │                                                                                        │   │
│  │  New                                                                                   │   │
│  │  • Added Python SDK v1.0                                                              │   │
│  │  • Added Go SDK v1.0                                                                  │   │
│  │                                                                                        │   │
│  │  [View Full Changelog →]                                                              │   │
│  └────────────────────────────────────────────────────────────────────────────────────────┘   │
│                                                                                                │
└────────────────────────────────────────────────────────────────────────────────────────────────┘
```

---

## VERSION HISTORY

| Version | Date | Changes |
|---------|------|---------|
| 1.0 | 2025-02-08 | Initial Developer Portal Documentation |

---

**Document End**
