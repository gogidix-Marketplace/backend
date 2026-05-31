# Shared Warehousing Core - Business Use Cases

**Version:** 1.0.0
**Last Updated:** 2025-02-23
**Domain:** shared-warehousing-core

---

## Table of Contents

1. [Business Context](#business-context)
2. [User Personas](#user-personas)
3. [Core Use Cases](#core-use-cases)
4. [Business Processes](#business-processes)
5. [Service Interactions](#service-interactions)

---

## 1. Business Context

The Shared Warehousing Core platform serves multiple business models through a unified, multi-tenant warehousing and inventory management system.

### Supported Business Models

| Model | Description | Key Features |
|-------|-------------|--------------|
| **B2B Distribution** | Traditional wholesale distribution | Bulk storage, PO management, fulfillment |
| **B2C Fulfillment** | E-commerce order fulfillment | Same-day shipping, returns management |
| **Self-Storage** | Vendor-managed storage locations | Geofencing, vendor mobile app, capacity tracking |
| **3PL Services** | Third-party logistics operations | Multi-tenant isolation, billing per client |
| **Public Marketplace** | Direct-to-consumer storage rental | Public booking API, dynamic pricing |

---

## 2. User Personas

### 2.1 Primary Personas

| Persona | Role | Goals | Pain Points |
|---------|------|-------|-------------|
| **Warehouse Manager** | Operations oversight | Optimize space utilization, track inventory accuracy | Manual processes, lack of real-time visibility |
| **Warehouse Staff** | Daily operations | Efficient picking/packing, accurate counts | Poor routing, unclear instructions |
| **Tenant Admin** | Configure tenant rules | Set business rules, manage users | Complex setup, rigid configurations |
| **Vendor** | Self-storage customer | Manage inventory, track storage usage | Limited access, poor visibility |
| **Customer Service** | Handle inquiries | Check status, resolve issues | Disconnected systems, slow responses |
| **Business Analyst** | Reporting & insights | Access metrics, forecast demand | Delayed reports, limited analytics |

### 2.2 Secondary Personas

| Persona | Role | Goals |
|---------|------|-------|
| **System Administrator** | Platform operations | Maintain uptime, monitor performance |
| **Integration Developer** | API integration | Connect external systems, build automations |
| **Compliance Officer** | Regulatory compliance | Audit trails, security standards |

---

## 3. Core Use Cases

### UC-001: Inventory Management

**Actor:** Warehouse Manager, Inventory Clerk
**Priority:** High

#### Main Success Scenario

1. User creates new inventory item with SKU, quantity, location
2. System validates SKU uniqueness within tenant
3. System stores item in MongoDB with tenant isolation
4. System publishes `inventory.item.created` event
5. System updates availability indices

#### Extensions

| Condition | Action |
|-----------|--------|
| Duplicate SKU | Return 409 Conflict with error details |
| Invalid location | Reject with suggested valid locations |
| Quantity below reorder threshold | Create low stock alert |

#### Business Rules

- SKU must be unique per tenant
- All items must have a valid location
- Low stock threshold triggers automatic alerts
- Inventory changes create audit trail entries

---

### UC-002: Stock Allocation for Order

**Actor:** Fulfillment Service (system), Order Manager
**Priority:** Critical

#### Main Success Scenario

1. Order received from customer
2. Fulfillment service requests stock allocation
3. Stock service checks availability across locations
4. Stock service reserves quantity (creates reservation)
5. Stock service confirms allocation
6. Inventory service adjusts available quantities
7. System publishes `stock.allocated` event

#### Extensions

| Condition | Action |
|-----------|--------|
| Insufficient stock | Return partial allocation with backorder options |
| Multiple locations | Allocate based on picking optimization (FEFO) |
| Reservation timeout | Release reservation, notify fulfillment |

#### Business Rules

- Allocations expire after configurable timeout (default: 30 min)
- FEFO (First Expired First Out) for batched items
- Cannot allocate reserved or quarantined stock
- Multi-location allocation minimizes picking distance

---

### UC-003: Order Fulfillment Workflow

**Actor:** Picker, Packer, Shipping Coordinator
**Priority:** Critical

#### Main Success Scenario

**Picking Phase:**
1. System assigns picking task to available picker
2. System optimizes pick route through warehouse
3. Picker scans items at each location
4. System verifies quantity and barcode
5. System completes picking, publishes event

**Packing Phase:**
6. System assigns picked order to packing station
7. System recommends optimal box size
8. Packer packs items, adds packing materials
9. System generates shipping label
10. System completes packing, publishes event

**Shipping Phase:**
11. System selects best carrier based on rate/service
12. System transmits shipment to carrier
13. System generates tracking number
14. System updates order status to SHIPPED

#### Extensions

| Condition | Action |
|-----------|--------|
| Item not found at location | Initiate stock count, suggest alternate location |
| Damaged item discovered | Trigger quality check, remove from inventory |
| Carrier API unavailable | Queue shipment for retry, alert operations |

#### Business Rules

- Same-day orders must ship by cutoff time
- High-priority orders skip queue
- Quality hold pauses fulfillment
- Weight/size limits trigger special handling

---

### UC-004: Storage Space Booking

**Actor:** Public Customer, Tenant Admin
**Priority:** High

#### Main Success Scenario

1. Customer requests storage quote via public API
2. Pricing service calculates based on size, duration, features
3. Customer confirms booking with payment
4. Space service allocates available unit
5. Access service generates PIN code
6. System sends confirmation with access details
7. System publishes `space.booked` event

#### Extensions

| Condition | Action |
|-----------|--------|
| No availability | Offer alternatives, add to waitlist |
| Payment declined | Return payment error, hold allocation temporarily |
| Special requirements | Route to manual review |

#### Business Rules

- Overbooking not allowed (hard capacity limit)
- Dynamic pricing adjusts based on demand
- Climate-controlled units have premium pricing
- Minimum rental period applies (default: 30 days)

---

### UC-005: Vendor Self-Storage Management

**Actor:** Vendor (business customer)
**Priority:** High

#### Main Success Scenario

1. Vendor registers storage location with geofence
2. System validates location is within service area
3. Vendor adds inventory items via mobile app
4. System syncs inventory to central database
5. System tracks capacity utilization
6. Customer orders allocated to vendor location
7. Vendor fulfills from their storage
8. System updates inventory, calculates performance

#### Extensions

| Condition | Action |
|-----------|--------|
| Location outside geofence | Reject registration with map of valid area |
| Sync conflict | Resolve with last-write-wins, alert vendor |
| Capacity exceeded | Block new inventory, suggest consolidation |

#### Business Rules

- Vendors must pass compliance verification
- Performance metrics affect allocation priority
- Geofence prevents location spoofing
- Offline sync supported with conflict resolution

---

### UC-006: Receiving & Putaway

**Actor:** Receiving Clerk, Putaway Specialist
**Priority:** High

#### Main Success Scenario

**Receiving Phase:**
1. Supplier shipment arrives at dock
2. System records receipt against purchase order
3. System verifies quantities against ASN
4. System identifies discrepancies (damage, shortage)
5. Quality service initiates inspection if required
6. System generates GRN (Goods Receipt Note)

**Putaway Phase:**
7. System suggests optimal putaway locations
8. System generates putaway tasks
9. Worker scans items to locations
10. System verifies placement
11. System updates inventory quantities
12. System publishes `item.received` event

#### Extensions

| Condition | Action |
|-----------|--------|
| No purchase order found | Create receipt as "unplanned" |
| Quantity discrepancy | Flag for review, create adjustment record |
| Damaged items | Quarantine, initiate return process |

#### Business Rules

- Must putaway within 4 hours of receipt (SLA)
- Fast-moving items go to prime picking locations
- Heavy items go to lower shelves
- Hazardous materials require special zones

---

### UC-007: Returns Processing

**Actor:** Returns Clerk, Quality Inspector
**Priority:** Medium

#### Main Success Scenario

1. Customer initiates return via portal
2. System generates RMA (Return Merchandise Authorization)
3. Customer ships item back
4. Receiving receives returned item
5. Quality service inspects condition
6. Based on inspection:
   - Restock to inventory (sellable)
   - Refurbish/repair (minor defects)
   - Return to supplier (defective)
   - Dispose (unsellable)
7. System processes refund/exchange
8. System updates inventory

#### Extensions

| Condition | Action |
|-----------|--------|
| Return window expired | Check warranty, may charge restocking fee |
| No RMA found | Create exception record for manual review |
| Item not from inventory | Route to special handling |

#### Business Rules

- Return window: 30 days (configurable per tenant)
- Restocking fee: 10% (configurable)
- Refund within 3 business days of processing
- Supplier returns must meet defect threshold

---

### UC-008: Cycle Counting

**Actor:** Inventory Auditor
**Priority:** Medium

#### Main Success Scenario

1. System generates cycle count schedule (ABC analysis)
2. System assigns count tasks to auditors
3. Auditor counts items at location
4. System compares count to system records
5. If discrepancy found:
   - System creates adjustment record
   - System investigates root cause
   - System updates inventory
6. System records accuracy metrics

#### Extensions

| Condition | Action |
|-----------|--------|
| Significant discrepancy | Escalate to management, schedule full recount |
| Recurring discrepancies | Investigate potential theft or process issues |

#### Business Rules

- A items: Count monthly (high value/fast moving)
- B items: Count quarterly
- C items: Count annually
- Accuracy target: 98%+

---

### UC-009: Batch Expiration Management

**Actor:** Inventory Manager, Quality Inspector
**Priority:** High (for perishable goods)

#### Main Success Scenario

1. System receives items with batch/lot numbers
2. System records expiration dates
3. System tracks batch locations
4. System generates expiration alerts:
   - 60 days: Planning alert
   - 30 days: Action alert
   - 7 days: Critical alert
   - Expired: Quarantine automatically
5. Picking uses FEFO (First Expired First Out)
6. System generates expiration reports

#### Extensions

| Condition | Action |
|-----------|--------|
| No expiration on batch | Set default based on product category |
| Expired stock found | Prevent allocation, quarantine |

#### Business Rules

- Cannot ship expired items (hard stop)
- FEFO enforced at picking
- Expiration alerts sent to relevant roles
- Disposal workflow for expired items

---

### UC-010: Analytics & Reporting

**Actor:** Business Analyst, Operations Manager
**Priority:** Medium

#### Main Success Scenario

1. User requests report (inventory, fulfillment, utilization)
2. System aggregates data from MongoDB collections
3. System applies tenant filters
4. System generates report with visualizations
5. System exports to PDF/Excel if requested
6. System caches report for performance

#### Report Types

| Report | Description | Frequency |
|--------|-------------|------------|
| Inventory Turnover | Fast/slow moving items | Monthly |
| Fulfillment Metrics | Cycle time, efficiency | Daily/Weekly |
| Space Utilization | Occupancy, availability | Weekly |
| ABC Analysis | Revenue by item category | Quarterly |
| Stockout Report | Items out of stock | Weekly |
| Vendor Performance | Fill rate, quality | Monthly |

#### Business Rules

- Reports scoped to tenant (no cross-tenant data)
- Scheduled reports delivered via email
- Custom reports via query builder
- Historical data retained for 13 months

---

## 4. Business Processes

### BP-001: Order-to-Cash

```
Customer Order → Order Service → Inventory Check → Stock Allocation
       ↓
Fulfillment Service → Picking → Packing → Shipping → Carrier
       ↓
Order Confirmation → Tracking Update → Delivery Confirmation
       ↓
Invoicing → Payment Processing
```

### BP-002: Procure-to-Pay

```
Purchase Order → Supplier → ASN → Receiving → Inspection
       ↓
Putaway → Inventory Update → Quality Check → Supplier Payment
```

### BP-003: Storage Booking Lifecycle

```
Quote Request → Pricing Calculation → Booking → Payment
       ↓
Space Allocation → Access Setup → Customer Access
       ↓
Usage Tracking → Renewal/Extension → Vacate → Deposit Return
```

### BP-004: Return-to-Refund

```
Return Request → RMA Generation → Customer Ships → Receipt
       ↓
Quality Inspection → Disposition Decision → Inventory Update
       ↓
Refund Processing → Customer Notification
```

---

## 5. Service Interactions

### Inventory Flow

```
┌─────────────┐    ┌─────────────┐    ┌─────────────┐
│   Order     │───>│  Inventory  │───>│    Stock    │
│  Service    │    │    Core     │    │   Service   │
└─────────────┘    └─────────────┘    └─────────────┘
                          │                   │
                          ▼                   ▼
                   ┌─────────────┐    ┌─────────────┐
                   │  Location   │    │ Analytics   │
                   │   Service   │    │    Service  │
                   └─────────────┘    └─────────────┘
```

### Fulfillment Flow

```
┌─────────────┐    ┌─────────────┐    ┌─────────────┐
│   Order     │───>│Fulfillment  │───>│   Picking   │
│  Service    │    │    Core     │    │   Service   │
└─────────────┘    └─────────────┘    └─────────────┘
                          │                   │
                          ▼                   ▼
                   ┌─────────────┐    ┌─────────────┐
                   │   Packing   │    │  Shipping   │
                   │   Service   │    │   Service   │
                   └─────────────┘    └─────────────┘
                          │                   │
                          └─────────┬─────────┘
                                    ▼
                          ┌─────────────────────┐
                          │    Analytics        │
                          │    Service          │
                          └─────────────────────┘
```

### Storage Booking Flow

```
┌─────────────┐    ┌─────────────┐    ┌─────────────┐
│   Public    │───>│   Space     │───>│   Pricing   │
│   Booking   │    │   Service   │    │   Service   │
└─────────────┘    └─────────────┘    └─────────────┘
                          │                   │
                          ▼                   ▼
                   ┌─────────────┐    ┌─────────────┐
                   │   Access    │    │  Payment    │
                   │   Service   │    │  Webhook    │
                   └─────────────┘    └─────────────┘
```

---

## 6. Non-Functional Requirements

### Performance

| Requirement | Target |
|-------------|--------|
| API response time (P95) | < 500ms |
| Database query time | < 100ms |
| Event processing latency | < 1s |
| Concurrent users | 1000+ |

### Availability

| Requirement | Target |
|-------------|--------|
| Uptime | 99.9% |
| Recovery time | < 15 minutes |
| Data loss | None (replicated) |

### Scalability

| Requirement | Target |
|-------------|--------|
| Horizontal scaling | Auto (HPA) |
| Max tenants | 10,000+ |
| Max inventory items | 100M+ |
| Max orders/day | 1M+ |

### Security

| Requirement | Implementation |
|-------------|----------------|
| Tenant isolation | Row-level security |
| Data encryption | At rest + in transit |
| Authentication | JWT + OAuth 2.0 |
| Authorization | RBAC |

---

**Document Version:** 1.0.0
**Last Updated:** 2025-02-23
**Maintained By:** Product Team
