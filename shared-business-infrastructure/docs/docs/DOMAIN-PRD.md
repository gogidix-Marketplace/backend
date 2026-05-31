# Ecommerce Domain — Product Requirements Document

## 1. Domain Overview

**Domain:** Ecommerce Marketplace
**Business Group:** E-commerce-business-group
**Package:** `com.gogidix.cargo.ecommerce`
**API Gateway Port:** 8091
**Repository:** `cargoNexus-africa/CargoNexus-Ecommerce-Domain`

CargoNexus Ecommerce is a multi-vendor marketplace enabling vendors to list, sell, and fulfill products (petroleum accessories, dry goods, consumer products) to customers across Nigeria, Ghana, and Kenya.

## 2. Actors & User Roles

| Actor | Frontend | Description |
|-------|----------|-------------|
| Consumer/Customer | marketplace-web, marketplace-mobile | Browses, searches, orders products; tracks deliveries; manages wishlist |
| Vendor | vendor-portal, vendor-app | Lists products, manages inventory, processes orders, chooses fulfillment |
| Vendor POS Operator | vendor-pos-app | In-store POS transactions (card, USSD, QR, NFC, cash) |
| Wholesaler | wholesaler-portal, wholesaler-app | Bulk ordering, contract management, volume pricing |

## 3. Frontend Applications (7)

| App | Package | Type | Pages/Screens |
|-----|---------|------|---------------|
| marketplace-web | @cargonexus/marketplace-web | Web (Next.js) | 10 pages |
| marketplace-mobile | @cargonexus/marketplace-mobile | Mobile (Expo) | 10 screens |
| vendor-portal | @cargonexus/vendor-portal | Web (Next.js) | 13 pages |
| vendor-app | @cargonexus/vendor-app | Mobile (Expo) | 12 screens |
| vendor-pos-app | @cargonexus/vendor-pos-app | Mobile (Expo) | 17 screens |
| wholesaler-portal | @cargonexus/wholesaler-portal | Web (Next.js) | 10 pages |
| wholesaler-app | @cargonexus/wholesaler-app | Mobile (Expo) | 9 screens |

## 4. Backend Services (65+)

### 4.1 Catalog (6 services)
- `category-service` — Product category hierarchy
- `catalog-service` — Product catalog management
- `variant-service` — Product variants (size, color, etc.)
- `pricing-service` — Dynamic pricing rules
- `bundle-service` — Product bundles and combos
- `inventory-sync-service` — Real-time inventory sync

### 4.2 Order (6 services)
- `order-service` — Order lifecycle management
- `cart-service` — Shopping cart
- `checkout-service` — Checkout flow orchestration
- `payment-service` — Payment processing
- `returns-service` — Returns and refunds
- `exchange-service` — Product exchanges

### 4.3 Fulfillment (10 services)
- `fulfillment-orchestrator-service` — Orchestrate fulfillment across modes
- `fulfillment-service` — Fulfillment core logic
- `delivery-service` — Delivery management
- `shipping-service` — Shipping rate calculation
- `order-tracking-service` — Order tracking
- `realtime-tracking-service` — Real-time GPS tracking
- `courier-integration-service` — Courier network integration
- `haulage-integration-service` — Haulage partner integration
- `warehouse-integration-service` — Warehouse integration
- `air-freight-integration-service` — Air freight
- `ocean-shipping-integration-service` — Ocean shipping

### 4.4 Vendor (5 services)
- `vendor-service` — Vendor profile management
- `onboarding-service` — Vendor registration and KYB
- `vendor-dashboard-service` — Vendor dashboard data
- `vendor-analytics-service` — Vendor performance analytics
- `dropship-service` — Dropshipping management

### 4.5 Wholesaler (4 services)
- `wholesaler-service` — Wholesaler profile management
- `wholesaler-dashboard-service` — Wholesaler dashboard
- `bulk-pricing-service` — Volume-based pricing tiers
- `contract-management-service` — Wholesale contracts

### 4.6 Marketplace (5 services)
- `marketplace-service` — Marketplace core
- `marketplace-search-service` — Product search (Elasticsearch)
- `commission-service` — Marketplace commission calculation
- `sme-marketplace-service` — SME marketplace features
- `wholesale-marketplace-service` — Wholesale marketplace

### 4.7 Payment (3 services)
- `payment-gateway-service` — Payment gateway abstraction
- `payment-method-service` — Payment method management
- `payment-service` — Payment processing

### 4.8 Influencer (5 services)
- `influencer-service` — Influencer management
- `influencer-dashboard-service` — Influencer dashboard data
- `affiliate-service` — Affiliate link tracking
- `commission-service` — Influencer commission
- `social-integration-service` — Social media integration

### 4.9 Supporting Services
- `analytics-service` — Business analytics
- `communication-service` — Communication hub
- `customer-service` — Customer profiles
- `discount-service` — Discount rules engine
- `loyalty-service` — Loyalty program
- `reward-service` — Rewards management
- `gift-card-service` — Gift card system
- `store-credit-service` — Store credit
- `notification-service` — Notification hub
- `email-service` — Email sending
- `push-notification-service` — Push notifications
- `sms-service` — SMS gateway
- `search-service` — Global search
- `wishlist-service` — Wishlist management
- `tenant-config-service` — Multi-tenant config
- `tenant-hierarchy-service` — Tenant hierarchy
- `promotion-service` — Promotional campaigns
- `coupon-service` — Coupon management
- `pricing-service` — Dynamic pricing

### 4.10 Public API (3 services)
- `public-cart-service` — Public cart API
- `public-catalog-service` — Public catalog API
- `public-search-service` — Public search API

### 4.11 Procurement (6 services)
- `requisition-service` — Purchase requisitions
- `approval-workflow-service` — Approval chains
- `budget-service` — Budget tracking
- `reconciliation-service` — Procurement reconciliation
- `branch-dashboard-service` — Branch procurement view
- `hq-dashboard-service` — HQ procurement view

## 5. Revenue Streams

| Channel | Model | Target |
|---------|-------|--------|
| Vendor commission | 5-15% per sale | All vendors |
| Influencer commission | 2-5% of attributed sales | Influencers |
| Courier commission | 3-8% per delivery | Courier partners |
| Warehousing fee | ₦/sqm/month | Storage vendors |
| Transaction fee | 1.5% per transaction | All transactions |
| POS transaction fee | 1.5-2.5% | In-store vendors |
| POS hardware rental | ₦5K/month | POS terminal users |
| POS premium app | ₦5K/month | Premium POS features |

## 6. Cross-Domain Dependencies

| Dependency | Domain | Via |
|-----------|--------|-----|
| Payment processing | Banking | Kafka + REST (gateway) |
| Courier dispatch | Courier | Kafka events |
| Influencer tracking | Influencer | Kafka events |
| Warehouse management | Warehousing (shared) | REST (gateway) |
| Haulage | Haulage (shared) | REST (gateway) |
| User auth | Admin (shared) | JWT tokens |

## 7. Kafka Topics

| Topic | Producer | Consumer |
|-------|----------|----------|
| `cargo.ecommerce.order.created` | order-service | vendor-portal, notification-service |
| `cargo.ecommerce.order.fulfillment.started` | fulfillment-service | marketplace-web, order-tracking |
| `cargo.ecommerce.order.completed` | fulfillment-service | marketplace, vendor, banking |
| `cargo.ecommerce.payment.processed` | payment-service | order-service, banking |
| `cargo.ecommerce.vendor.onboarded` | onboarding-service | vendor-dashboard, notification |
| `cargo.ecommerce.inventory.updated` | inventory-service | catalog-service, vendor-portal |
| `cargo.ecommerce.courier.requested` | fulfillment-service | courier domain |
| `cargo.ecommerce.commission.earned` | commission-service | banking, influencer |

## 8. Multi-Tenancy

- `tenantId` on every entity
- Country-level data isolation (NG, GH, KE)
- HQ tenant sees aggregate data across countries
- Tenant config managed by `tenant-config-service`

## 9. Success Metrics

| Metric | Target |
|--------|--------|
| Vendor onboarding | 500 vendors in 6 months |
| Monthly active consumers | 50K in 12 months |
| Order completion rate | >95% |
| Average delivery time | <48 hours (urban) |
| POS adoption | 30% of vendors within 9 months |