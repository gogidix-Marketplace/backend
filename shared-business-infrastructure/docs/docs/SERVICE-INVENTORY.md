# Ecommerce Domain — Service Inventory

## Service Count: 65+ microservices + 1 API gateway + 7 frontends

---

## API Gateway

| Name | Port | Tech |
|------|------|------|
| ecommerce-api-gateway | 8091 | Spring Cloud Gateway, WebFlux, Redis, JWT |

---

## Backend Services (by group)

### Catalog (6)
| Service | Package | Description | Status |
|---------|---------|-------------|--------|
| category-service | com.gogidix.cargo.ecommerce.catalog | Product category CRUD, hierarchy | Scaffolded |
| catalog-service | com.gogidix.cargo.ecommerce.catalog | Product catalog, search index | Scaffolded |
| variant-service | com.gogidix.cargo.ecommerce.catalog | Product variant management | Scaffolded |
| pricing-service | com.gogidix.cargo.ecommerce.catalog | Dynamic pricing engine | Scaffolded |
| bundle-service | com.gogidix.cargo.ecommerce.catalog | Product bundles and combos | Scaffolded |
| inventory-sync-service | com.gogidix.cargo.ecommerce.catalog | Real-time inventory sync | Scaffolded |

### Order (6)
| Service | Package | Description | Status |
|---------|---------|-------------|--------|
| order-service | com.gogidix.cargo.ecommerce.order | Order lifecycle (create → complete) | Scaffolded |
| cart-service | com.gogidix.cargo.ecommerce.order | Shopping cart management | Scaffolded |
| checkout-service | com.gogidix.cargo.ecommerce.order | Checkout orchestration | Scaffolded |
| payment-service | com.gogidix.cargo.ecommerce.order | Payment processing | Scaffolded |
| returns-service | com.gogidix.cargo.ecommerce.order | Returns and refunds | Scaffolded |
| exchange-service | com.gogidix.cargo.ecommerce.order | Product exchanges | Scaffolded |

### Fulfillment (11)
| Service | Package | Description | Status |
|---------|---------|-------------|--------|
| fulfillment-orchestrator-service | com.gogidix.cargo.ecommerce.fulfillment | Cross-mode fulfillment orchestration | Scaffolded |
| fulfillment-service | com.gogidix.cargo.ecommerce.fulfillment | Fulfillment core logic | Scaffolded |
| delivery-service | com.gogidix.cargo.ecommerce.fulfillment | Delivery scheduling and tracking | Scaffolded |
| shipping-service | com.gogidix.cargo.ecommerce.fulfillment | Shipping rate calculation | Scaffolded |
| order-tracking-service | com.gogidix.cargo.ecommerce.fulfillment | Order tracking events | Scaffolded |
| realtime-tracking-service | com.gogidix.cargo.ecommerce.fulfillment | Real-time GPS tracking | Scaffolded |
| courier-integration-service | com.gogidix.cargo.ecommerce.fulfillment | Courier network API | Scaffolded |
| haulage-integration-service | com.gogidix.cargo.ecommerce.fulfillment | Haulage partner API | Scaffolded |
| warehouse-integration-service | com.gogidix.cargo.ecommerce.fulfillment | Warehouse management API | Scaffolded |
| air-freight-integration-service | com.gogidix.cargo.ecommerce.fulfillment | Air freight API | Scaffolded |
| ocean-shipping-integration-service | com.gogidix.cargo.ecommerce.fulfillment | Ocean freight API | Scaffolded |

### Vendor (5)
| Service | Package | Description | Status |
|---------|---------|-------------|--------|
| vendor-service | com.gogidix.cargo.ecommerce.vendor | Vendor CRUD, profile | Scaffolded |
| onboarding-service | com.gogidix.cargo.ecommerce.vendor | Vendor registration, KYB | Scaffolded |
| vendor-dashboard-service | com.gogidix.cargo.ecommerce.vendor | Vendor dashboard data aggregation | Scaffolded |
| vendor-analytics-service | com.gogidix.cargo.ecommerce.vendor | Performance analytics | Scaffolded |
| dropship-service | com.gogidix.cargo.ecommerce.vendor | Dropshipping management | Scaffolded |

### Wholesaler (4)
| Service | Package | Description | Status |
|---------|---------|-------------|--------|
| wholesaler-service | com.gogidix.cargo.ecommerce.wholesaler | Wholesaler profiles | Scaffolded |
| wholesaler-dashboard-service | com.gogidix.cargo.ecommerce.wholesaler | Dashboard aggregation | Scaffolded |
| bulk-pricing-service | com.gogidix.cargo.ecommerce.wholesaler | Volume pricing tiers | Scaffolded |
| contract-management-service | com.gogidix.cargo.ecommerce.wholesaler | Wholesale contracts | Scaffolded |

### Marketplace (5)
| Service | Package | Description | Status |
|---------|---------|-------------|--------|
| marketplace-service | com.gogidix.cargo.ecommerce.marketplace | Marketplace core | Scaffolded |
| marketplace-search-service | com.gogidix.cargo.ecommerce.marketplace | Elasticsearch integration | Scaffolded |
| commission-service | com.gogidix.cargo.ecommerce.marketplace | Commission calculation | Scaffolded |
| sme-marketplace-service | com.gogidix.cargo.ecommerce.marketplace | SME marketplace features | Scaffolded |
| wholesale-marketplace-service | com.gogidix.cargo.ecommerce.marketplace | Wholesale marketplace | Scaffolded |

### Payment (3)
| Service | Package | Description | Status |
|---------|---------|-------------|--------|
| payment-gateway-service | com.gogidix.cargo.ecommerce.payment | Payment gateway abstraction | Scaffolded |
| payment-method-service | com.gogidix.cargo.ecommerce.payment | Payment method management | Scaffolded |
| payment-service | com.gogidix.cargo.ecommerce.payment | Payment processing | Scaffolded |

### Influencer (5)
| Service | Package | Description | Status |
|---------|---------|-------------|--------|
| influencer-service | com.gogidix.cargo.ecommerce.influencer | Influencer management | Scaffolded |
| influencer-dashboard-service | com.gogidix.cargo.ecommerce.influencer | Dashboard data | Scaffolded |
| affiliate-service | com.gogidix.cargo.ecommerce.influencer | Affiliate link tracking | Scaffolded |
| commission-service | com.gogidix.cargo.ecommerce.influencer | Influencer commission | Scaffolded |
| social-integration-service | com.gogidix.cargo.ecommerce.influencer | Social media API | Scaffolded |

### Notification (4)
| Service | Package | Description | Status |
|---------|---------|-------------|--------|
| notification-service | com.gogidix.cargo.ecommerce.notification | Notification hub | Scaffolded |
| email-service | com.gogidix.cargo.ecommerce.notification | Email gateway | Scaffolded |
| push-notification-service | com.gogidix.cargo.ecommerce.notification | Push (Firebase/APNs) | Scaffolded |
| sms-service | com.gogidix.cargo.ecommerce.notification | SMS gateway (Twilio/Africa's Talking) | Scaffolded |

### Loyalty (4)
| Service | Package | Description | Status |
|---------|---------|-------------|--------|
| loyalty-service | com.gogidix.cargo.ecommerce.loyalty | Loyalty program engine | Scaffolded |
| reward-service | com.gogidix.cargo.ecommerce.loyalty | Rewards management | Scaffolded |
| gift-card-service | com.gogidix.cargo.ecommerce.loyalty | Gift card system | Scaffolded |
| store-credit-service | com.gogidix.cargo.ecommerce.loyalty | Store credit | Scaffolded |

### Supporting (8)
| Service | Package | Description | Status |
|---------|---------|-------------|--------|
| analytics-service | com.gogidix.cargo.ecommerce.analytics | Business analytics | Scaffolded |
| communication-service | com.gogidix.cargo.ecommerce.communication | Communication hub | Scaffolded |
| customer-service | com.gogidix.cargo.ecommerce.customer | Customer profiles | Scaffolded |
| discount-service | com.gogidix.cargo.ecommerce.discount | Discount engine | Scaffolded |
| search-service | com.gogidix.cargo.ecommerce.search | Global search | Scaffolded |
| wishlist-service | com.gogidix.cargo.ecommerce.wishlist | Wishlist CRUD | Scaffolded |
| promotion-service | com.gogidix.cargo.ecommerce.promotion | Promotional campaigns | Scaffolded |
| coupon-service | com.gogidix.cargo.ecommerce.promotion | Coupon management | Scaffolded |

### Tenant (2)
| Service | Package | Description | Status |
|---------|---------|-------------|--------|
| tenant-config-service | com.gogidix.cargo.ecommerce.tenant | Multi-tenant config | Scaffolded |
| tenant-hierarchy-service | com.gogidix.cargo.ecommerce.tenant | Tenant hierarchy (HQ → Country) | Scaffolded |

### Public API (3)
| Service | Package | Description | Status |
|---------|---------|-------------|--------|
| public-cart-service | com.gogidix.cargo.ecommerce.publicapi | Public cart endpoint | Scaffolded |
| public-catalog-service | com.gogidix.cargo.ecommerce.publicapi | Public catalog endpoint | Scaffolded |
| public-search-service | com.gogidix.cargo.ecommerce.publicapi | Public search endpoint | Scaffolded |

### Procurement (6)
| Service | Package | Description | Status |
|---------|---------|-------------|--------|
| requisition-service | com.gogidix.cargo.ecommerce.procurement | Purchase requisitions | Scaffolded |
| approval-workflow-service | com.gogidix.cargo.ecommerce.procurement | Approval chains | Scaffolded |
| budget-service | com.gogidix.cargo.ecommerce.procurement | Budget tracking | Scaffolded |
| reconciliation-service | com.gogidix.cargo.ecommerce.procurement | Procurement reconciliation | Scaffolded |
| branch-dashboard-service | com.gogidix.cargo.ecommerce.procurement | Branch procurement view | Scaffolded |
| hq-dashboard-service | com.gogidix.cargo.ecommerce.procurement | HQ procurement view | Scaffolded |

---

## Frontend Applications (7)

| App | Package | Type | Framework | Port |
|-----|---------|------|-----------|------|
| marketplace-web | @cargonexus/marketplace-web | Web | Next.js 14.2.3 | 3000 |
| marketplace-mobile | @cargonexus/marketplace-mobile | Mobile | Expo ~51.0.0 | — |
| vendor-portal | @cargonexus/vendor-portal | Web | Next.js 14.2.3 | 3001 |
| vendor-app | @cargonexus/vendor-app | Mobile | Expo ~51.0.0 | — |
| vendor-pos-app | @cargonexus/vendor-pos-app | Mobile | Expo ~51.0.0 | — |
| wholesaler-portal | @cargonexus/wholesaler-portal | Web | Next.js 14.2.3 | 3002 |
| wholesaler-app | @cargonexus/wholesaler-app | Mobile | Expo ~51.0.0 | — |