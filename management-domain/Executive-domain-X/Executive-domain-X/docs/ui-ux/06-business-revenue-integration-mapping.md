# Gogidix Executive Domain - Business Revenue Integration Mapping

**Version:** 1.0
**Last Updated:** 2026-04-22
**Status:** Active
**Purpose:** Map all revenue-generating business units from shared-business-infrastructure to CEO, CFO, COO, CTO executive dashboards

---

## Table of Contents

1. [Business Revenue Architecture Overview](#1-business-revenue-architecture-overview)
2. [Courier Services (shared-courier-core)](#2-courier-services-shared-courier-core)
3. [E-Commerce Platform (shared-ecommerce-core)](#3-e-commerce-platform-shared-ecommerce-core)
4. [Warehousing & Storage (shared-warehousing-core)](#4-warehousing--storage-shared-warehousing-core)
5. [Air Freight (shared-air-freight-core)](#5-air-freight-shared-air-freight-core)
6. [Ocean Shipping (shared-ocean-shipping-core)](#6-ocean-shipping-shared-ocean-shipping-core)
7. [Haulage & Road Freight (shared-haulage-core)](#7-haulage--road-freight-shared-haulage-core)
8. [Procurement (shared-procurement-core)](#8-procurement-shared-procurement-core)
9. [Admin & Partner Oversight (shared-admin-core)](#9-admin--partner-oversight-shared-admin-core)
10. [Consolidated Revenue Dashboard Matrix](#10-consolidated-revenue-dashboard-matrix)
11. [Business Unit KPI Targets for Executive Dashboards](#11-business-unit-kpi-targets-for-executive-dashboards)
12. [Revenue Flow Architecture](#12-revenue-flow-architecture)

---

## 1. Business Revenue Architecture Overview

### Revenue-Generating Business Units

```
┌─────────────────────────────────────────────────────────────────────────────────┐
│                    SHARED-BUSINESS-INFRASTRUCTURE                                │
│                    (Revenue Generation Layer)                                    │
│                                                                                  │
│  ┌──────────┐ ┌──────────┐ ┌──────────┐ ┌──────────┐ ┌──────────┐           │
│  │ Courier  │ │E-Commerce│ │Warehouse │ │ Air      │ │ Ocean    │           │
│  │ Delivery │ │ Platform │ │ Storage  │ │ Freight  │ │ Shipping │           │
│  │          │ │          │ │          │ │          │ │          │           │
│  │ 20 svc   │ │ 76 svc   │ │ 43 svc   │ │ 15 svc   │ │ 18 svc   │           │
│  │ 4 UI     │ │ 4 UI     │ │ 4 UI     │ │ 3 UI     │ │ 3 UI     │           │
│  └──────────┘ └──────────┘ └──────────┘ └──────────┘ └──────────┘           │
│                                                                                  │
│  ┌──────────┐ ┌──────────┐ ┌──────────┐                                      │
│  │ Haulage  │ │Procurement│ │ Admin    │                                      │
│  │ Road Frt │ │ B2B      │ │ Oversight│                                      │
│  │          │ │          │ │          │                                      │
│  │ 62 svc   │ │ 27 svc   │ │ 11 svc   │                                      │
│  │ 4 UI     │ │ 2 UI     │ │ 2 UI     │                                      │
│  └──────────┘ └──────────┘ └──────────┘                                      │
│                                                                                  │
│  TOTAL: 272 microservices | 26 frontend applications | 8 business units       │
└─────────────────────────────────────────────────────────────────────────────────┘
         │
         │ Revenue & operational data feeds
         ▼
┌─────────────────────────────────────────────────────────────────────────────────┐
│                    EXECUTIVE-DOMAIN                                              │
│  CEO Dashboard  |  CFO Dashboard  |  COO Dashboard  |  CTO Dashboard          │
└─────────────────────────────────────────────────────────────────────────────────┘
```

### Business Unit Scale Summary

| Business Unit | Services | Frontends | Revenue Model | Primary Region |
|---|---|---|---|---|
| **Courier** | 20 Java | 2 Web + 2 Mobile | 15% platform commission | Global (multi-tenant) |
| **E-Commerce** | 76 Java | 3 Web + 1 Mobile | Marketplace commissions, vendor fees | Africa / International |
| **Warehousing** | 37 Java + 6 Node | 2 Web + 2 Mobile | Storage fees, fulfillment fees | Nigeria (expanding) |
| **Air Freight** | 15 Java | 1 Web + 2 Mobile | Freight fees, surcharges | International (JFK, LAX, etc.) |
| **Ocean Shipping** | 18 Java | 1 Web + 2 Mobile | FCL/LCL freight, BAF/CAF | International (CNSHA, USLAX) |
| **Haulage** | 58 Java + 4 Node | 2 Web + 2 Mobile | Per-km/per-kg freight | North America, Europe |
| **Procurement** | 27 Java | 2 Web | Cost savings, volume discounts | Global (multi-country) |
| **Admin Core** | 11 Java | 2 Web | Cross-domain oversight | Global HQ |

---

## 2. Courier Services (shared-courier-core)

### Business Profile

| Attribute | Value |
|---|---|
| **Platform Type** | SME Courier Aggregation |
| **Total Services** | 20 Java microservices |
| **Frontends** | Dispatcher Dashboard, Partners Dashboard, Customer Tracking App, Driver App |
| **Ports** | 8100-8119 |
| **Tech Stack** | Spring Boot 3.1.5, Java 17, MongoDB, Kafka, Redis, OSRM |
| **Certification** | PRODUCTION READY (95%) |

### Revenue Model

| Stream | Mechanism | Rate |
|---|---|---|
| **Platform Commission** | % of every delivery fee | 15% |
| **Driver Share** | Delivery fee | 70% |
| **Partner Share** | Fleet commission | 15% |
| **Surge Pricing** | Dynamic demand multiplier | Up to 3.0x |
| **Cancellation Fee** | Driver assigned, not picked up | 50% of order |
| **Failed Delivery** | Charge per failed attempt | $5.00 |
| **Minimum Base Price** | Per order floor | $5.00 |

### CEO Data Points

| Metric | Source Service | Refresh | Component |
|---|---|---|---|
| Total deliveries completed | dispatch-core-service (8101) | Real-time (WS) | Hero KPI |
| Platform revenue (15% commission) | commission-service (8115) | 5 min | Revenue KPI |
| Active drivers/partners | driver-pool-service (8105) | 1 min | Headcount Card |
| Delivery volume trend | dispatch-core-service | 15 min | Line Chart |
| Customer satisfaction | performance-service (8107) | Daily | Score Gauge |
| Geographic coverage map | gps-tracking-service (8109) | Real-time (WS) | Live Map |

### CFO Data Points

| Metric | Source Service | Refresh | Component |
|---|---|---|---|
| Commission revenue | commission-service (8115) | 5 min | Revenue KPI |
| Surge revenue uplift | dynamic-pricing-service (8114) | 15 min | Surge Chart |
| Cancellation fee income | dispatch-core-service | 15 min | Fee KPI |
| Failed delivery charges | dispatch-core-service | 15 min | Charge Card |
| Discount/promo impact | discount-service (8113) | 15 min | Discount Table |
| Payout liability (driver+partner) | commission-service | Daily | Liability Gauge |
| Revenue per km trend | pricing-engine-service (8112) | Hourly | Revenue/Km Chart |

### COO Data Points

| Metric | Source Service | Refresh | Component |
|---|---|---|---|
| Active dispatches | dispatch-core-service (8101) | Real-time (WS) | Live Dispatch Grid |
| On-time delivery rate | performance-service (8107) | 15 min | OTD Gauge (>90%) |
| Average delivery time | eta-service (8108) | 5 min | Time KPI (<45 min) |
| First-attempt success | performance-service | 15 min | Success Gauge (>85%) |
| Driver availability | availability-service (8106) | 30 sec | Availability Bar |
| Route optimization savings | routing-service (8104) | Hourly | Savings Card |
| Assignment time | assignment-service (8102) | Real-time (WS) | Speed KPI (<2 min) |
| Fleet load balance | load-balancing-service (8103) | 5 min | Balance Chart |

### CTO Data Points

| Metric | Source Service | Refresh | Component |
|---|---|---|---|
| All 20 services health | All services `/actuator/health` | 30 sec | 20-Service Grid |
| GPS tracking latency | gps-tracking-service (8109) | 1 min | Latency Card (<5s) |
| OSRM routing engine status | routing-service (8104) | 30 sec | Engine Status |
| Public API rate limiting | public-booking-service (8117) | 1 min | Rate Limit Chart |
| Kafka event throughput | All services | 30 sec | Event Stream |

---

## 3. E-Commerce Platform (shared-ecommerce-core)

### Business Profile

| Attribute | Value |
|---|---|
| **Platform Type** | Multi-sided E-Commerce Marketplace |
| **Total Services** | 76 Java microservices (26 domain modules) |
| **Frontends** | Vendors Dashboard, Influencer Dashboard, Wholesalers Dashboard, Warehouse App |
| **Tech Stack** | Spring Boot 3.1.5, Java 17, MongoDB, PostgreSQL, Kafka, Redis, Elasticsearch |
| **Certification** | PRODUCTION READY (82% test coverage) |

### Revenue Model

| Stream | Mechanism | Enabling Service |
|---|---|---|
| **Marketplace Commission** | Per-transaction fee | commission-service (Marketplace) |
| **Vendor Fees** | Onboarding, listing, featured placement | vendor-service, onboarding-service |
| **Wholesale Transaction Fees** | Bulk order processing | wholesaler-service, bulk-pricing-service |
| **SME Marketplace Subscriptions** | Platform access | sme-marketplace-service |
| **Influencer/Affiliate Revenue** | Revenue share from referrals | influencer-service, affiliate-service |
| **Dropshipping Margins** | Product margin | dropship-service |
| **Promotional Placement** | Sponsored listings | promotion-service, coupon-service |
| **Loyalty/Gift Card Float** | Unused balance | loyalty-service, gift-card-service |

### CEO Data Points

| Metric | Source Service | Refresh | Component |
|---|---|---|---|
| Total GMV (Gross Merchandise Value) | order-service (8304) | 5 min | Hero GMV KPI |
| Active vendors | vendor-service | 15 min | Vendor Count Card |
| Active marketplace listings | marketplace-service | 15 min | Listings Counter |
| Order volume trend | order-service | 5 min | Order Trend Chart |
| Top categories | catalog-service | Hourly | Category Donut |
| Influencer campaign ROI | influencer-service | Daily | ROI Table |
| Customer base growth | customer-service | Daily | Growth Sparkline |

### CFO Data Points

| Metric | Source Service | Refresh | Component |
|---|---|---|---|
| Marketplace commission revenue | commission-service | 5 min | Commission Revenue KPI |
| Payment volume | payment-service | 5 min | Payment Volume Chart |
| Refund/return rate | returns-service | 15 min | Return Rate Gauge |
| Gift card liability | gift-card-service | Daily | Liability Card |
| Vendor payout pending | payment-gateway-service | 15 min | Payout Queue |
| Discount/promo cost impact | discount-service | 15 min | Cost Impact Table |
| Revenue by marketplace tier | marketplace-service | Hourly | Tier Revenue Bars |

### COO Data Points

| Metric | Source Service | Refresh | Component |
|---|---|---|---|
| Fulfillment rate | fulfillment-service | 5 min | Fulfillment Gauge |
| Order-to-ship time | shipping-service | 15 min | Speed KPI |
| Inventory health | inventory-service | 5 min | Inventory Grid |
| Vendor onboarding queue | onboarding-service | 15 min | Onboarding Pipeline |
| Return processing time | returns-service | 15 min | Returns Speed Card |
| Carrier integration status | courier-integration-service | 30 sec | Carrier Status Grid |

### CTO Data Points

| Metric | Source Service | Refresh | Component |
|---|---|---|---|
| 76 services health grid | All services `/actuator/health` | 30 sec | 76-Service Grid |
| Elasticsearch index health | search-service | 1 min | Search Index Card |
| Kafka event flow | All services | 30 sec | Event Stream |
| Peak order throughput | order-service | 5 min | Throughput Chart |
| E-commerce site performance | Public API services | 1 min | Performance Cards |

---

## 4. Warehousing & Storage (shared-warehousing-core)

### Business Profile

| Attribute | Value |
|---|---|
| **Platform Type** | Warehouse Aggregation (3-sided: Partners, Vendors, Public Storage) |
| **Total Services** | 37 Java + 6 Node.js = 43 microservices |
| **Frontends** | Partners Dashboard, Private Storage Dashboard, Warehouse Staff App, Vendor App |
| **Tech Stack** | Spring Boot 3.1.5, Java 17, MongoDB, Kafka, Redis, NestJS |
| **Certification** | PRODUCTION CERTIFIED (93%) |
| **Primary Region** | Nigeria (expanding to Africa) |

### Revenue Model

| Stream | Rate (Example) |
|---|---|
| **Storage Fee** (per cubic meter/month) | Small: N1,500; Medium: N2,000; Large: N2,500 |
| **Fulfillment Fee** (per order) | Standard: N500; Express: N750 |
| **Picking Fee** (per item) | Configurable per warehouse |
| **Packing Fee** (per order) | Configurable per warehouse |
| **Returns Processing** | N300 per return |
| **Self-Storage Rental** (monthly) | Small: N15,000; Medium: N25,000; Large: N40,000 |
| **Climate Control Surcharge** | +20% |
| **24/7 Access Surcharge** | +15% |
| **Insurance Coverage** | N500/month |

### CEO Data Points

| Metric | Source Service | Refresh | Component |
|---|---|---|---|
| Total warehouse partners | tenant-config-service (8111) | 15 min | Partner Count |
| Total storage units | space-service (8091) | 15 min | Unit Count Card |
| Occupancy rate | space-allocation-service (8092) | 5 min | Occupancy Gauge (83%) |
| Monthly storage revenue | pricing-service (8093) | Daily | Revenue KPI |
| Fulfillment orders processed | fulfillment-core-service (8099) | 5 min | Order Volume |
| Self-storage bookings | public-booking-service (8112) | 15 min | Booking Trend |

### CFO Data Points

| Metric | Source Service | Refresh | Component |
|---|---|---|---|
| Storage revenue by facility | pricing-service (8093) | Daily | Revenue Table |
| Fulfillment fee income | fulfillment-core-service (8099) | 15 min | Fee Income KPI |
| Ancillary revenue (surcharges) | pricing-service | Daily | Surcharge Breakdown |
| Partner payout liability | warehouse-analytics-service (8117) | Daily | Payout Card |
| Revenue per sq meter | warehouse-analytics-service | Daily | Revenue/SqM Chart |
| Occupancy vs. revenue correlation | reporting-service (8118) | Daily | Correlation Chart |

### COO Data Points

| Metric | Source Service | Refresh | Component |
|---|---|---|---|
| Warehouse utilization (all facilities) | warehouse-analytics-service (8117) | 5 min | Utilization Heat Map |
| Picking efficiency | picking-service (8100) | 15 min | Pick Rate Card |
| Packing throughput | packing-service (8101) | 15 min | Pack Rate Card |
| Inbound receiving volume | receiving-service (8105) | 15 min | Receiving Grid |
| Inventory accuracy | cycle-counting-service (8089) | Hourly | Accuracy Gauge |
| Returns processing queue | returns-service (8103) | 15 min | Returns Queue |
| Expiring inventory alerts | expiration-service (8087) | Hourly | Expiry Alert Cards |

### CTO Data Points

| Metric | Source Service | Refresh | Component |
|---|---|---|---|
| 43 services health | All services | 30 sec | 43-Service Grid |
| Order throughput (target: 1200/min) | fulfillment-core-service | 1 min | Throughput Gauge |
| API response time (P95: 380ms) | All services | 1 min | Latency Chart |
| MongoDB cluster health | All services | 30 sec | DB Health Cards |
| Kafka event processing (450ms) | All services | 1 min | Event Flow |

---

## 5. Air Freight (shared-air-freight-core)

### Business Profile

| Attribute | Value |
|---|---|
| **Platform Type** | Air Freight Aggregation |
| **Total Services** | 15 Java microservices |
| **Frontends** | Agents Dashboard (Web), Driver App (Mobile), Agent Location App (Mobile) |
| **Tech Stack** | Spring Boot 3.1.5, Java 17, MongoDB, RabbitMQ, Redis |
| **Ports** | 8080-8094 |
| **Coverage** | International (JFK, LAX, ORD + global customs) |

### Revenue Model

| Stream | Mechanism |
|---|---|
| **Freight Shipping** | Distance x weight/volume x service level (Standard/Express/Priority 1.5x) |
| **Fuel Surcharge** | e.g., 5% of base price |
| **Security Surcharge** | Flat per shipment |
| **Dangerous Goods Surcharge** | +25% premium |
| **Customs Brokerage** | Electronic filing fees |
| **Ground Transportation** | First-mile + last-mile fees |
| **Platform SaaS** | Multi-tenant licensing |

### CEO Data Points

| Metric | Source Service | Refresh | Component |
|---|---|---|---|
| Total air freight bookings | air-booking-service (8080) | 15 min | Booking Volume KPI |
| Revenue by service tier | air-quote-service (8081) | Hourly | Tier Revenue Bars |
| Active flight routes | flight-schedule-service (8085) | 15 min | Route Map |
| Shipment completion rate | air-shipment-service (8083) | 15 min | Completion Gauge |
| Customs clearance rate | customs-declaration-service (8089) | Daily | Clearance Score |

### CFO Data Points

| Metric | Source Service | Refresh | Component |
|---|---|---|---|
| Total freight revenue | air-quote-service (8081) | 15 min | Revenue KPI |
| Revenue per kg (yield) | air-shipment-service | Hourly | Yield Chart |
| Surcharge revenue breakdown | air-quote-service | Daily | Surcharge Donut |
| Customs brokerage fees | customs-declaration-service | Daily | Fee Income Card |
| Cancellation fee income | air-booking-service | Daily | Cancellation KPI |
| Margin by service level | air-quote-service | Daily | Margin Table |

### COO Data Points

| Metric | Source Service | Refresh | Component |
|---|---|---|---|
| Active shipments in transit | air-tracking-service (8084) | Real-time (WS) | Transit Grid |
| On-time delivery rate (>95%) | air-tracking-service | 15 min | OTD Gauge |
| On-time pickup rate (>98%) | air-tracking-service | 15 min | OTP Gauge |
| Capacity utilization (>80%) | cargo-space-service (8087) | 15 min | Capacity Bars |
| Flight status alerts | flight-status-service (8086) | Real-time (WS) | Flight Alert Feed |
| Average customs clearance time | customs-declaration-service | Daily | Clearance Time KPI (<24h) |

### CTO Data Points

| Metric | Source Service | Refresh | Component |
|---|---|---|---|
| 15 services health | All services | 30 sec | 15-Service Grid |
| Quote generation response (<3s) | air-quote-service | 1 min | Quote Speed Gauge |
| Booking creation response (<5s) | air-booking-service | 1 min | Booking Speed Gauge |
| Tracking update frequency | air-tracking-service | 1 min | Tracking Frequency |
| Airline adapter sync status | airline-adapter-service (8090) | 5 min | Adapter Status |
| RabbitMQ message throughput | All services | 30 sec | Message Flow |

---

## 6. Ocean Shipping (shared-ocean-shipping-core)

### Business Profile

| Attribute | Value |
|---|---|
| **Platform Type** | Ocean Shipping Aggregation |
| **Total Services** | 18 Java microservices |
| **Frontends** | Ocean Dashboard (Web), Agent App (Mobile), Driver App (Mobile) |
| **Tech Stack** | Spring Boot 3.1.5, Java 17, MongoDB, Kafka, Eureka |
| **Ports** | 8060-8075 |
| **Coverage** | International (CNSHA, USLAX, global service loops) |

### Revenue Model

| Stream | Mechanism |
|---|---|
| **FCL Freight** | Per-container pricing |
| **LCL Freight** | Per-CBM/per-ton pricing |
| **Surcharges** | BAF (fuel), CAF (currency), Peak Season, War Risk |
| **Customs Brokerage** | Import/export declaration fees |
| **Documentation Fees** | BL issuance, e-signing, amendments |
| **Space Reservation** | Vessel space holding fees |
| **Cancellation Fees** | Tiered: Free (>7d), 50% (3-7d), None (<3d) |
| **Platform SaaS** | Multi-tenant licensing |

### CEO Data Points

| Metric | Source Service | Refresh | Component |
|---|---|---|---|
| Total FCL + LCL bookings | ocean-booking-service (8061) | 15 min | Booking Volume KPI |
| Revenue by container type | ocean-quote-service (8062) | Hourly | FCL/LCL Revenue Split |
| Active vessel routes | vessel-schedule-service (8067) | 15 min | Route Map |
| Container fleet status | container-service (8064) | 5 min | Container Status Grid |
| BL processing volume | bl-service (8060) | Daily | BL Volume Card |

### CFO Data Points

| Metric | Source Service | Refresh | Component |
|---|---|---|---|
| Total freight revenue (FCL + LCL) | ocean-quote-service (8062) | 15 min | Revenue Hero KPI |
| Average revenue per TEU/FEU | ocean-quote-service | Daily | Revenue/TEU Chart |
| Surcharge revenue (BAF, CAF, WRS) | ocean-quote-service | Daily | Surcharge Breakdown |
| Customs brokerage income | customs-declaration-service (8066) | Daily | Customs Fee Card |
| Cancellation fee revenue | ocean-booking-service | Daily | Cancellation KPI |
| Quote-to-booking conversion | ocean-quote-service → ocean-booking-service | Daily | Conversion Funnel |

### COO Data Points

| Metric | Source Service | Refresh | Component |
|---|---|---|---|
| Containers in transit | ocean-tracking-service (8072) | Real-time (WS) | Transit Container Grid |
| Vessel positions (AIS) | vessel-tracking-service (8068) | Real-time (WS) | Live Vessel Map |
| Schedule reliability (ETA vs ATA) | vessel-schedule-service | 15 min | Reliability Gauge |
| Container utilization | container-space-service (8065) | 15 min | Stowage Chart |
| Customs clearance pipeline | customs-declaration-service | 15 min | Customs Pipeline |
| Port operations status | container-service | 5 min | Port Ops Grid |

### CTO Data Points

| Metric | Source Service | Refresh | Component |
|---|---|---|---|
| 18 services health | All services | 30 sec | 18-Service Grid |
| Kafka event flow (6 event types) | All services | 30 sec | Event Stream |
| Shipping line adapter status | shipping-line-adapter-service (8070) | 5 min | Adapter Status |
| Eureka service discovery | All services | 30 sec | Service Registry |
| Public API rate limiting | public-booking-service (8073) | 1 min | API Rate Card |

---

## 7. Haulage & Road Freight (shared-haulage-core)

### Business Profile

| Attribute | Value |
|---|---|
| **Platform Type** | Long-Distance Freight Aggregation |
| **Total Services** | 58 Java + 4 Node.js = 62 microservices (12 domain groups) |
| **Frontends** | Haulage Dashboard, Partners Dashboard, Carrier App, Driver App |
| **Tech Stack** | Spring Boot 3.1.5, Java 17, MongoDB, Kafka, Redis, NestJS |
| **Coverage** | North America, Europe (CMR), Cross-border |

### Revenue Model

| Stream | Mechanism |
|---|---|
| **Distance-based Freight** | pricePerKm x distanceKm |
| **Weight Surcharges** | pricePerKg x weightKg (tiered) |
| **Volume Charges** | pricePerCubicMeter x volume |
| **Fuel Surcharge** | % of subtotal |
| **Express Premium** | Urgency multiplier (EXPRESS > STANDARD) |
| **Load-type Premiums** | FCL, LCL, Bulk, Specialized (hazardous, oversized) |
| **Partner Commissions** | Carrier commission + payout management |
| **API Subscription** | Free / Basic / Professional / Enterprise tiers |

### CEO Data Points

| Metric | Source Service | Refresh | Component |
|---|---|---|---|
| Total loads dispatched | load-planning-service | 15 min | Load Volume KPI |
| Revenue by load type | pricing-engine-service | Hourly | FCL/LCL/Bulk/Special Revenue |
| Active carrier network | carrier-service | 15 min | Carrier Network Card |
| Fleet utilization (>80%) | utilization-service | 15 min | Utilization Gauge |
| Geographic coverage map | gps-tracking-service | Real-time (WS) | Live Fleet Map |
| Backhaul optimization savings | backhaul-service | Daily | Savings Card |

### CFO Data Points

| Metric | Source Service | Refresh | Component |
|---|---|---|---|
| Total freight revenue | pricing-engine-service | 15 min | Revenue Hero KPI |
| Revenue per km/mile | distance-service | Daily | Revenue/Km Chart |
| Fuel cost impact | fuel-service | Daily | Fuel Cost Chart |
| Partner commission liability | commission-service | Daily | Commission Liability |
| Express vs Standard margin | urgency-service | Daily | Margin Comparison |
| Net profit margin (>15%) | profitability-service | Daily | Profit Margin Gauge |
| Deadhead cost (<10% target) | utilization-service | Daily | Deadhead Card |

### COO Data Points

| Metric | Source Service | Refresh | Component |
|---|---|---|---|
| Active loads in transit | status-service | Real-time (WS) | Transit Load Grid |
| On-time delivery rate (>95%) | status-service | 15 min | OTD Gauge |
| ETA accuracy (<30 min dev) | eta-service | 5 min | ETA Accuracy Card |
| Route optimization (>15% savings) | route-optimization-service | Daily | Optimization Savings |
| POD availability (<15 min) | pod-service | 15 min | POD Speed Card |
| Empty miles % (<15% target) | utilization-service | Daily | Empty Miles Gauge |
| Temperature compliance | temperature-service | 5 min | Temp Alert Feed |

### CTO Data Points

| Metric | Source Service | Refresh | Component |
|---|---|---|---|
| 62 services health | All services | 30 sec | 62-Service Grid |
| GPS tracking latency | gps-tracking-service | 1 min | GPS Latency Card |
| IoT sensor pipeline | iot-sensor-service | 1 min | IoT Pipeline Status |
| ELD integration health | eld-integration-service | 5 min | ELD Status |
| Kafka throughput (6 event types) | All services | 30 sec | Event Stream |

---

## 8. Procurement (shared-procurement-core)

### Business Profile

| Attribute | Value |
|---|---|
| **Platform Type** | B2B Procurement Platform (Internal Cost Center) |
| **Total Services** | 27 Java microservices (8 domain groups) |
| **Frontends** | Procurement Dashboard, Supplier Portal |
| **Tech Stack** | Spring Boot 3.1.5, Java 17, MongoDB, PostgreSQL, Kafka |
| **Coverage** | Multi-country (US, international) |

### Revenue / Cost Savings Model

| Lever | Mechanism | Target |
|---|---|---|
| **Volume Aggregation** | Consolidated purchasing across branches | Better pricing |
| **Budget Control** | Real-time commit/actualize/release | Prevent overspending |
| **Three-Way Matching** | PO qty = Receipt qty = Invoice qty (+/-5%) | Eliminate overpayment |
| **Supplier Performance** | Data-driven sourcing | Preferred supplier programs |
| **Contract Leverage** | Blanket purchase agreements | Volume discounts |
| **Approval Efficiency** | Threshold-based auto-routing | Faster cycle time |

### Approval Thresholds

| Amount | Required Approver |
|---|---|
| < $5,000 | Department Manager |
| $5,000 - $25,000 | Procurement Manager |
| $25,000 - $100,000 | Finance Director |
| > $100,000 | CFO |

### CEO Data Points

| Metric | Source Service | Refresh | Component |
|---|---|---|---|
| Total procurement spend | spend-analytics-service | Daily | Spend Hero KPI |
| Active supplier network | supplier-service (8083) | 15 min | Supplier Count |
| Cost savings achieved | spend-analytics-service | Weekly | Savings Card |
| Contract coverage rate | contract-service | Weekly | Coverage Gauge |
| Preferred supplier % | vendor-rating-service | Weekly | Preferred % Card |

### CFO Data Points

| Metric | Source Service | Refresh | Component |
|---|---|---|---|
| Budget vs. actual variance | budget-reporting-service | Daily | Variance Chart |
| Spend by category/department | spend-analytics-service | Daily | Spend Breakdown |
| Budget utilization % | budget-service (8084) | 15 min | Budget Util Grid |
| PO commitment tracking | purchase-order-service (8082) | 15 min | Commitment Table |
| Invoice matching rate | invoice-matching-service | Daily | Match Rate Gauge |
| Overdue orders (count + value) | purchase-order-service | Daily | Overdue Alert |

### COO Data Points

| Metric | Source Service | Refresh | Component |
|---|---|---|---|
| PO cycle time | procurement-reporting-service | Daily | Cycle Time Chart |
| Approval cycle time | approval-engine-service (8085) | 15 min | Approval Speed Card |
| Goods receipt cycle time | receiving-service | Daily | Receipt Speed KPI |
| Supplier on-time delivery (>95%) | vendor-performance-service | Weekly | OTD Gauge |
| Requisition to PO conversion | procurement-engine-service | Daily | Conversion Funnel |
| Approval SLA compliance | approval-engine-service | Daily | SLA Compliance % |

### CTO Data Points

| Metric | Source Service | Refresh | Component |
|---|---|---|---|
| 27 services health | All services | 30 sec | 27-Service Grid |
| Kafka procurement events | All services | 30 sec | Event Stream |
| ERP integration status | erp-integration-service | 5 min | ERP Sync Status |
| Bank integration status | bank-integration-service | 5 min | Bank Connection |
| Accounting sync health | accounting-integration-service | 5 min | Accounting Sync |

---

## 9. Admin & Partner Oversight (shared-admin-core)

### Business Profile

| Attribute | Value |
|---|---|
| **Platform Type** | Cross-Domain Administrative Services |
| **Total Services** | 11 Java microservices |
| **Frontends** | Country Admin Dashboard, Management HQ Dashboard |
| **Tech Stack** | Spring Boot 3.1.5, Java 17, PostgreSQL, Kafka |
| **Ports** | 8501-8511 |

### Revenue Impact (Indirect)

| Lever | Mechanism |
|---|---|
| **Partner Commission Engine** | Tier-based rates (Bronze/Silver/Gold/Platinum) |
| **Settlement Processing** | Batch settlement, approval workflows, bank payments |
| **KYC/Compliance** | Risk scoring, document verification |
| **Cross-Domain Analytics** | Revenue comparison across all 7 business units |
| **HQ Oversight** | Global executive summary across entire ecosystem |

### CEO Data Points

| Metric | Source Service | Refresh | Component |
|---|---|---|---|
| Total active partners (all domains) | partner-management-service (8503) | 15 min | Partner Count Card |
| Partner tier distribution | partner-management-service | Daily | Tier Distribution Donut |
| KYC compliance rate | partner-kyc-service (8502) | Daily | KYC Compliance Gauge |
| Cross-domain revenue comparison | cross-domain-analytics-service (8508) | 15 min | Revenue Comparison Chart |
| Global executive summary | hq-oversight-service (8511) | 5 min | Executive Summary Card |

### CFO Data Points

| Metric | Source Service | Refresh | Component |
|---|---|---|---|
| Total commission (all domains) | commission-engine-service (8505) | 5 min | Commission KPI |
| Settlement batch status | settlement-service (8506) | 15 min | Settlement Pipeline |
| Payout pending (all partners) | settlement-service | 15 min | Payout Liability |
| Revenue by partner domain | cross-domain-analytics-service | Hourly | Domain Revenue Bars |
| Settlement reconciliation | settlement-service | Daily | Reconciliation Status |

### COO Data Points

| Metric | Source Service | Refresh | Component |
|---|---|---|---|
| Partner onboarding pipeline | partner-registration-service (8501) | 15 min | Onboarding Queue |
| Domain assignment status | partner-domain-assignment (8504) | 15 min | Domain Assignment Grid |
| Regional hub performance | regional-hub-management (8509) | 15 min | Hub Performance Cards |
| Service level compliance | service-level-monitoring (8510) | 5 min | SLA Compliance Grid |
| Incident management | service-level-monitoring | Real-time (WS) | Incident Feed |

### CTO Data Points

| Metric | Source Service | Refresh | Component |
|---|---|---|---|
| 11 services health | All services | 30 sec | 11-Service Grid |
| PostgreSQL cluster status | All services | 30 sec | DB Health |
| Cross-domain data sync | hq-oversight-service | 5 min | Sync Status |
| KYC service performance | partner-kyc-service | 1 min | KYC Pipeline Health |

---

## 10. Consolidated Revenue Dashboard Matrix

### All Business Units → Executive Role Mapping

| Business Unit | CEO | CFO | COO | CTO |
|---|---|---|---|---|
| **Courier** | Volume, Satisfaction, Growth | Commission, Surge revenue, Payouts | On-time rate, Dispatch efficiency, Driver avail | 20-service grid, GPS latency, OSRM |
| **E-Commerce** | GMV, Vendors, Marketplace growth | Commission, Payment vol, Returns | Fulfillment rate, Inventory, Onboarding | 76-service grid, ES, Kafka |
| **Warehousing** | Partners, Occupancy, Revenue | Storage revenue, Fulfillment fees, Surcharges | Utilization, Pick/pack speed, Expiry alerts | 43-service grid, Throughput |
| **Air Freight** | Bookings, Routes, Clearance | Yield/kg, Surcharges, Margins | Transit, OTP, Capacity utilization | 15-service grid, Quote speed |
| **Ocean Shipping** | FCL+LCL, Vessel routes, Containers | Revenue/TEU, BAF/CAF, Conversion | Containers in transit, Schedule reliability | 18-service grid, AIS tracking |
| **Haulage** | Loads, Carrier network, Fleet util | Revenue/km, Fuel cost, Profit margin | OTD, ETA accuracy, Empty miles | 62-service grid, IoT, ELD |
| **Procurement** | Total spend, Suppliers, Savings | Budget variance, Spend analysis, Matching | Cycle times, Supplier OTD, Approvals | 27-service grid, ERP/Bank sync |
| **Admin Core** | Partners, Tiers, KYC compliance | Commissions, Settlements, Reconciliation | Onboarding, Hub perf, SLA compliance | 11-service grid, Cross-domain sync |

### Total Executive KPI Count by Role

| Role | Business Unit KPIs | Department KPIs (from doc 05) | Total Data Points |
|---|---|---|---|
| **CEO** | 38 | 22 | **60** |
| **CFO** | 44 | 26 | **70** |
| **COO** | 44 | 30 | **74** |
| **CTO** | 34 | 20 | **54** |
| **Total** | **160** | **98** | **258** |

---

## 11. Business Unit KPI Targets for Executive Dashboards

### Revenue KPI Targets (CFO Dashboard)

| Business Unit | Revenue KPI | Target | Timeframe |
|---|---|---|---|
| **Courier** | Platform commission revenue | 15% of delivery fees | Monthly |
| **E-Commerce** | GMV growth rate | >20% YoY | Quarterly |
| **Warehousing** | Occupancy rate | >85% | Monthly |
| **Air Freight** | Revenue per kg (yield) | Industry benchmark | Monthly |
| **Ocean Shipping** | Revenue per TEU | Industry benchmark | Monthly |
| **Haulage** | Net profit margin | >15% | Quarterly |
| **Procurement** | Cost savings vs. budget | >10% annually | Quarterly |
| **Admin Core** | Partner commission accuracy | 100% | Monthly |

### Operational KPI Targets (COO Dashboard)

| Business Unit | Operational KPI | Target |
|---|---|---|
| **Courier** | On-time delivery rate | >90% |
| **Courier** | Assignment time | <2 minutes |
| **E-Commerce** | Fulfillment rate | >95% |
| **Warehousing** | Inventory accuracy | >99% |
| **Warehousing** | Order throughput | >1,200/min |
| **Air Freight** | On-time delivery | >95% |
| **Air Freight** | Customs clearance time | <24 hours |
| **Ocean Shipping** | Schedule reliability (ETA vs ATA) | >85% |
| **Haulage** | On-time delivery rate | >95% |
| **Haulage** | Empty miles percentage | <15% |
| **Procurement** | PO cycle time | <48 hours |
| **Procurement** | Three-way match rate | >98% |

### Technology KPI Targets (CTO Dashboard)

| Business Unit | Total Services | Uptime Target | P95 Latency |
|---|---|---|---|
| **Courier** | 20 | 99.9% | <200ms |
| **E-Commerce** | 76 | 99.9% | <500ms |
| **Warehousing** | 43 | 99.9% | <380ms |
| **Air Freight** | 15 | 99.9% | <3s (quotes) |
| **Ocean Shipping** | 18 | 99.9% | <500ms |
| **Haulage** | 62 | 99.9% | <500ms |
| **Procurement** | 27 | 99.9% | <500ms |
| **Admin Core** | 11 | 99.9% | <500ms |
| **TOTAL** | **272** | | |

---

## 12. Revenue Flow Architecture

### Data Aggregation Pipeline (Business Units → Executives)

```
8 Revenue Business Units (272 microservices)
    │
    ├── Kafka Topics (per business unit)
    │   ├── courier.events (dispatch, tracking, commission)
    │   ├── ecommerce.events (orders, payments, fulfillment)
    │   ├── warehouse.events (inventory, fulfillment, storage)
    │   ├── airfreight.events (bookings, shipments, customs)
    │   ├── ocean.events (bookings, containers, vessels)
    │   ├── haulage.events (loads, tracking, pricing)
    │   ├── procurement.events (POs, budgets, approvals)
    │   └── admin.events (partners, settlements, KYC)
    │
    ▼
Admin Core: Cross-Domain Analytics Service (8508)
    │
    ├── Aggregates revenue across all 8 business units
    ├── Calculates cross-unit KPIs
    ├── Generates executive summaries
    │
    ▼
Executive Command Service (Node.js)
    │
    ├── Role-based filtering (CEO/CFO/COO/CTO)
    ├── Revenue consolidation and currency normalization
    ├── AI-powered anomaly detection
    │
    ▼
Executive Dashboard Frontends
    ├── CEO Dashboard → Revenue by business unit, Growth, Strategic view
    ├── CFO Dashboard → Financial consolidation, P&L by unit, Budget tracking
    ├── COO Dashboard → Operational health by unit, SLA compliance
    └── CTO Dashboard → 272-service grid, Performance by unit
```

### WebSocket Channels (Business Unit Data)

```css
/* CEO - Revenue Stream Channels */
ws://executive-domain/ws/topic/ceo/courier-revenue
ws://executive-domain/ws/topic/ceo/ecommerce-gmv
ws://executive-domain/ws/topic/ceo/warehouse-occupancy
ws://executive-domain/ws/topic/ceo/airfreight-bookings
ws://executive-domain/ws/topic/ceo/ocean-shipments
ws://executive-domain/ws/topic/ceo/haulage-loads
ws://executive-domain/ws/topic/ceo/procurement-spend

/* CFO - Financial Stream Channels */
ws://executive-domain/ws/topic/cfo/courier-commission
ws://executive-domain/ws/topic/cfo/ecommerce-payments
ws://executive-domain/ws/topic/cfo/warehouse-revenue
ws://executive-domain/ws/topic/cfo/airfreight-yield
ws://executive-domain/ws/topic/cfo/ocean-revenue-per-teu
ws://executive-domain/ws/topic/cfo/haulage-margin
ws://executive-domain/ws/topic/cfo/procurement-budget-variance
ws://executive-domain/ws/topic/cfo/partner-settlements

/* COO - Operational Stream Channels */
ws://executive-domain/ws/topic/coo/courier-dispatches
ws://executive-domain/ws/topic/coo/ecommerce-fulfillment
ws://executive-domain/ws/topic/coo/warehouse-operations
ws://executive-domain/ws/topic/coo/airfreight-transit
ws://executive-domain/ws/topic/coo/ocean-containers
ws://executive-domain/ws/topic/coo/haulage-fleet
ws://executive-domain/ws/topic/coo/procurement-approvals

/* CTO - Infrastructure Stream Channels */
ws://executive-domain/ws/topic/cto/courier-services-health
ws://executive-domain/ws/topic/cto/ecommerce-services-health
ws://executive-domain/ws/topic/cto/warehouse-services-health
ws://executive-domain/ws/topic/cto/airfreight-services-health
ws://executive-domain/ws/topic/cto/ocean-services-health
ws://executive-domain/ws/topic/cto/haulage-services-health
ws://executive-domain/ws/topic/cto/procurement-services-health
ws://executive-domain/ws/topic/cto/admin-services-health
```

---

**Document End: Business Revenue Integration Mapping v1.0**
