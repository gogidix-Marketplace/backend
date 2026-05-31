# GOGIDIX CORPORATE WEBSITE ADMIN - PRODUCT CATALOG

**Version:** 1.0
**Domain:** Corporate Website
**Frontend:** corporate-website-admin
**Last Updated:** 2025-02-08

---

## TABLE OF CONTENTS

1. [Product Catalog Overview](#product-catalog-overview)
2. [Product Management](#product-management)
3. [Category Management](#category-management)
4. [Pricing Management](#pricing-management)
5. [Integration Management](#integration-management)

---

## 1. PRODUCT CATALOG OVERVIEW

### Catalog Architecture

```
┌────────────────────────────────────────────────────────────────────────────────────────────────┐
│                        PRODUCT CATALOG STRUCTURE                                              │
├────────────────────────────────────────────────────────────────────────────────────────────────┤
│                                                                                                │
│  ┌────────────────────────────────────────────────────────────────────────────────────────┐  │
│  │                        PRODUCT CATEGORIES                                                │  │
│  ├────────────────────────────────────────────────────────────────────────────────────────┤  │
│  │  Logistics Platform              │  E-commerce Platform          │  Procurement Platform       │  │
│  │  ├─ Courier Management           │  ├─ Marketplace                │  ├─ Corporate Procurement   │  │
│  │  ├─ Warehouse Management         │  ├─ Social Commerce            │  ├─ Wholesale Management     │  │
│  │  ├─ Haulage Management           │  ├─ Booking Engine             │  └─ Vendor Management         │  │
│  │  ├─ Air Freight                 │  ├─ Payment Gateway            │  └───────────────────────────┘  │
│  │  ├─ Ocean Shipping               │  ├─ Order Management          │                                 │  │
│  │  └─ Fleet Tracking              │  └─────────────────────────────┘                                 │  │
│  └────────────────────────────────────────────────────────────────────────────────────────┘  │
│                                                                                                │
│  ┌────────────────────────────────────────────────────────────────────────────────────────┐  │
│  │                        BUSINESS OPERATIONS                                                │  │
│  ├────────────────────────────────────────────────────────────────────────────────────────┤  │
│  │  ├─ Admin Cloud                  │  ├─ Finance Cloud               │  ├─ HR Cloud                   │  │
│  │  ├─ Sales Cloud                  │  ├─ Operations Cloud           │  ├─ Marketing Cloud             │  │
│  │  ├─ Support Cloud                │  └─────────────────────────────┘                                 │  │
│  │  └─ GBM Cloud                   │                                                            │  │
│  └────────────────────────────────────────────────────────────────────────────────────────┘  │
│                                                                                                │
│  ┌────────────────────────────────────────────────────────────────────────────────────────┐  │
│  │                        ENTERPRISE SOLUTIONS                                               │  │
│  ├────────────────────────────────────────────────────────────────────────────────────────┤  │
│  │  ├─ Executive Dashboard           │  ├─ Global Business Management │  ├─ Corporate Finance           │  │
│  │  ├─ Corporate HR                 │  ├─ Global Sales                │  └───────────────────────────────┘  │  │
│  │  ├─ Global Operations             │                                                            │  │
│  │  ├─ Global Support               │                                                            │  │
│  │  └─ Systems Management          │                                                            │  │
│  └────────────────────────────────────────────────────────────────────────────────────────┘  │
│                                                                                                │
│  ┌────────────────────────────────────────────────────────────────────────────────────────┐  │
│  │                        INFRASTRUCTURE                                                   │  │
│  ├────────────────────────────────────────────────────────────────────────────────────────┤  │
│  │  ├─ Platform                     │  ├─ AI Services                 │  ├─ Orchestration               │  │
│  │  ├─ API Gateway                  │  ├─ Tracking Services           │  └─ Transaction Management     │  │
│  │  └─ Auth Infrastructure          │                                                            │  │
│  └────────────────────────────────────────────────────────────────────────────────────────┘  │
│                                                                                                │
└────────────────────────────────────────────────────────────────────────────────────────────────┘
```

### Product Hierarchy

```
Product
├─ id: string
├─ slug: string
├─ name: string
├─ category: ProductCategory
├─ subcategory?: string
├─ type: ProductType
├─ status: ProductStatus
├─ content
│  ├─ tagline
│  ├─ description
│  ├─ longDescription
│  └─ story
├─ features: ProductFeature[]
├─ benefits: Benefit[]
├─ useCases: UseCase[]
├─ pricing?: ProductPricing
├─ integrations: ProductIntegration[]
├─ media
│  ├─ icon: string
│  ├─ images: ProductImage[]
│  ├─ videos: ProductVideo[]
│  └─ documents: Document[]
├─ seo: SEOData
└─ analytics: ProductAnalytics
```

---

## 2. PRODUCT MANAGEMENT

### Product Creation Flow

```
┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐
│ Product        │────>│ Create/Edit     │────>│ Basic Info     │
│ Manager Clicks │     │ Product       │     │ Tab           │
│ "New Product"  │     │               │     │               │
└─────────────────┘     └─────────────────┘     └─────────────────┘
                                                        │
                        │
                        ▼
                 ┌─────────────────────────────┐
                 │ 1. Basic Information            │
                 │    • Name                       │
                 │    • Slug (auto-generated)       │
                 │    • Category & Subcategory      │
                 │    • Type                       │
                 │    • Status                     │
                 │    [Continue →]                 │
                 └─────────────────────────────┘
                        │
                        ▼
                 ┌─────────────────────────────┐
                 │ 2. Content Description         │
                 │    • Tagline                   │
                 │    • Short description          │
                 │    • Long description          │
                 │    • Key highlights            │
                 │    [Continue →]                 │
                 └─────────────────────────────┘
                        │
                        ▼
                 ┌─────────────────────────────┐
                 │ 3. Features & Benefits         │
                 │    • Add features (name, icon)    │
                 │    • Add benefits with metrics    │
                 │    • [Save & Add Another]          │
                 │    [Continue →]                 │
                 └─────────────────────────────┘
                        │
                        ▼
                 ┌─────────────────────────────┐
                 │ 4. Use Cases                  │
                 │    • Industry                  │
                 │    • Company Size              │
                 │    • Outcomes                  │
                 │    • [Save & Add Another]          │
                 │    [Continue →]                 │
                 └─────────────────────────────�
                        │
                        ▼
                 ┌─────────────────────────────┐
                 │ 5. Media                      │
                 │    • Upload icon                 │
                 │    • Upload screenshots           │
                 │    │    Upload videos            │
                 │    │    Add documents             │
                 │    [Continue →]                 │
                 └─────────────────────────────┘
                        │
                        ▼
                 ┌─────────────────────────────┐
                 │ 6. Pricing (if applicable)    │
                 │    • Add pricing plans            │
                 │    • Set tiers and features        │
                 │    • Configure billing             │
                 │    [Continue →]                 │
                 └─────────────────────────────┘
                        │
                        ▼
                 ┌─────────────────────────────┐
                 │ 7. SEO                       │
                 │    • Meta title                 │
                 │    • Meta description          │
                 │    • Keywords                   │
                 │    • OG image                   │
                 │    [Continue →]                 │
                 └─────────────────────────────�
                        │
                        ▼
                 ┌─────────────────────────────┐
                 │ 8. Review & Publish            │
                 │    • Review all sections         │
                 │    • Save as draft                │
                 │    │    Submit for approval       │
                 │    │    Schedule publish          │
                 │    │    Publish now               │
                 │    └─────────────────────────────┘
```

### Product Data Model

```typescript
interface ProductAdmin {
  // Basic Info
  id: string;
  slug: string;
  name: string;
  tagline: string;
  category: ProductCategory;
  subcategory?: string;
  type: ProductType;
  status: ProductStatus;

  // Content
  description: string;
  longDescription: string;

  // Features
  features: {
    id: string;
    name: string;
    description: string;
    icon: string;
    available: boolean;
    availableSince?: string;
  }[];

  // Benefits
  benefits: {
    id: string;
    title: string;
    description: string;
    icon: string;
    metric?: string;
    metricDescription?: string;
  }[];

  // Use Cases
  useCases: {
    id: string;
    title: string;
    description: string;
    industry: string;
    companySize: string[];
    outcomes: string[];
  }[];

  // Media
  icon: string;
  images: {
    id: string;
    url: string;
    alt: string;
    type: 'screenshot' | 'diagram' | 'photo';
    order: number;
  }[];
  videos: {
    id: string;
    url: string;
    thumbnail: string;
    title: string;
    duration: number;
  }[];

  // Pricing
  pricing?: {
    plans: PricingPlan[];
    hasFreeTier: boolean;
    customPricingAvailable: boolean;
  };

  // Integrations
  integrations: {
    id: string;
    name: string;
    description: string;
    logo: string;
    category: string;
    certificationLevel?: 'basic' | 'certified' | 'premium';
    documentationUrl?: string;
  }[];

  // Case Studies
  caseStudies: string[];

  // SEO
  seo: SEOData;

  // Publishing
  publishedAt?: Date;
  publishedBy: string;
  createdAt: Date;
  updatedAt: Date;
}

type ProductCategory =
  | 'logistics'
  | 'ecommerce'
  | 'procurement'
  | 'business-ops'
  | 'enterprise'
  | 'infrastructure';

type ProductType = 'saas' | 'white-label' | 'api' | 'managed-service';
type ProductStatus = 'active' | 'beta' | 'coming-soon' | 'deprecated';
```

---

## 3. CATEGORY MANAGEMENT

### Category Management Flow

```
┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐
│ Admin Accesses  │────>│ Product       │────>│ Categories     │
│ Products       │     │ Management   │     │               │
│ Module        │     │               │     │               │
└─────────────────┘     └─────────────────┘     └─────────────────┘
                                                        │
                        ┌───────────────────────────────┴───────────────────────────────┐
                        ▜                                                               ▼
                 ┌──────────────┐                                         ┌──────────────┐
                 │ View/Edit     │                                         │ Reorder      │
                 │ Category     │                                         │ Categories   │
                 │ Details      │                                         │             │
                 └──────┬───────┘                                         └──────────────┘
                        │                                                      │
                        ▼                                                      │
                 ┌──────────────┐                                               │
                 │ Add New      │                                               │ Drag & Drop   │
                 │ Category    │                                               │ to Reorder   │
                 └──────┬───────┘                                               │
                        │                                                      │
                        ▼                                                      │
                 ┌──────────────┐                                               │
                 │ Set          │                                               │ Save Order   │
                 │ Display      │                                               │             │
                 │ Settings    │                                               │             │
                 │ (Icon, Name, │                                               │             │
                 │  Color)      │                                               │             │
                 └──────┬───────┘                                               │
                        │                                                      │
                        ▼                                                      ▼
                 ┌──────────────┐                                         ┌──────────────┐
                 │ Category     │                                         │ Category    │
                 │ Live on     │                                         │ Structure   │
                 │ Website     │                                         │ Updated     │
                 └──────────────┘                                         └──────────────┘
```

### Category Data Model

```typescript
interface ProductCategory {
  id: string;
  slug: string;
  name: string;
  description: string;
  icon: string;
  color: string; // For UI theming
  order: number;
  parentId?: string; // For nested categories

  // Products count
  productCount: number;

  // Display settings
  displayIn: 'all' | 'public' | 'admin-only';
  featured: boolean;

  // SEO
  seo: {
    name: string;
    description: string;
    keywords: string[];
  };

  // Children (for nested categories)
  children?: ProductCategory[];
}
```

---

## 4. PRICING MANAGEMENT

### Pricing Plan Configuration

```
┌────────────────────────────────────────────────────────────────────────────────────────────────┐
│  Pricing Plan Configuration                                                                 │
├────────────────────────────────────────────────────────────────────────────────────────────────┤
│                                                                                                │
│  ┌────────────────────────────────────────────────────────────────────────────────────────┐  │
│  │ PLAN DETAILS                                                                               │  │
│  ├────────────────────────────────────────────────────────────────────────────────────────┤  │
│  │  Plan Name:        [Professional                                     ]                           │  │
│  │  Plan Description: [For growing teams needing more resources...                          ]                           │  │
│  │  Popular Plan:     ☑ Mark as popular option                                            │  │
│  │                                                                                           │  │
│  │  PRICING                                                                                   │  │
│  │  Price:            [$199                                               ]                           │  │
│  │  Currency:         [USD ▼]                                           │  │
│  │  Period:           [Month ▼]                                          │  │
│  │  Display Price:    [$199/month                                         ]                           │  │
│  │  Custom Pricing:   ☐ Available (Contact us for quote)                                    │  │
│  │                                                                                           │  │
│  └────────────────────────────────────────────────────────────────────────────────────────┘  │
│                                                                                                │
│  ┌────────────────────────────────────────────────────────────────────────────────────────┐  │
│  │ FEATURES (Check to include in plan)                                                    │  │
│  ├────────────────────────────────────────────────────────────────────────────────────────┤  │
│  │  ☑ Shipments up to 5,000/month                                                        │  │
│  │  ☑ 10 users included                                                                   │  │
│  │  ☑ Advanced analytics dashboard                                                         │  │
│  │  ☐ API access (Enterprise only)                                                        │  │
│  │  ☐ Priority support                                                                     │  │
│  │  ☐ Custom integrations                                                                 │  │
│  │                                                                                           │  │
│  │  [+ Add Feature]                                                                              │  │
│  │                                                                                           │  │
│  └────────────────────────────────────────────────────────────────────────────────────────┘  │
│                                                                                                │
│  ┌────────────────────────────────────────────────────────────────────────────────────────┐  │
│  │ FEATURE DETAILS (for each feature)                                                       │  │
│  ├────────────────────────────────────────────────────────────────────────────────────────┤  │
│  │  Feature:          Shipments up to 5,000/month                                            │  │
│  │  Description:      Process up to 5,000 shipments per month                           │  │
│  │  Value Prop:       Scale your operations without limits                                    │  │
│  │  Limit Type:       🔄 Soft limit (notify at 80%)                              │  │
│  │  Fine Print:       Additional shipments billed at $0.50 each                              │  │
│  └────────────────────────────────────────────────────────────────────────────────────────┘  │
│                                                                                                │
│  [Save Plan] [Preview] [Publish to Website]                                                       │
│                                                                                                │
└────────────────────────────────────────────────────────────────────────────────────────────────┘
```

### Pricing Comparison Table

```
┌────────────────────────────────────────────────────────────────────────────────────────────────┐
│  Pricing Comparison Table Configuration                                                          │
├────────────────────────────────────────────────────────────────────────────────────────────────┤
│                                                                                                │
│  Display Mode: ◉ Cards   ○ Table   ○ Toggle                                                   │
│                                                                                                │
│  ┌────────────────────────────────────────────────────────────────────────────────────────┐  │
│  │  COLUMNS                                                                                    │  │
│  ├────────────────────────────────────────────────────────────────────────────────────────┤  │
│  │  Feature            │  Essential Features   │  Premium Features   │  Enterprise      │  │
│  │  Show Features:     ☑ All features across all plans                               │  │
│  │  Hide Common:       ☐ Hide features common to all plans                              │  │
│  │                                                                                           │  │
│  └────────────────────────────────────────────────────────────────────────────────────────┘  │
│                                                                                                │
│  ┌────────────────────────────────────────────────────────────────────────────────────────┐  │
│  │  PLANS TO SHOW                                                                          │  │
│  ├────────────────────────────────────────────────────────────────────────────────────────┤  │
│  │  ☑ Starter       ☑ Professional       ☐ Enterprise                                   │  │
│  │  ☐ Free Trial    ☐ Beta Plan         ☑ Custom (Contact Us)                        │  │
│  │                                                                                           │  │
│  │  Drag to reorder plans:                                                                 │  │
│  │  [Starter] [Professional] [Enterprise] [Free Trial] [Beta]                              │  │
│  │                                                                                           │  │
│  └────────────────────────────────────────────────────────────────────────────────────────┘  │
│                                                                                                │
│  ┌────────────────────────────────────────────────────────────────────────────────────────┐  │
│  │  CALL TO ACTION                                                                         │  │
│  ├────────────────────────────────────────────────────────────────────────────────────────┤  │
│  │  Primary CTA:        [Start Free Trial →]                                             │  │
│  │  Secondary CTA:      [Talk to Sales →]                                               │  │  │
│  │  Enterprise CTA:      [Contact for Quote →]                                            │  │
│  │                                                                                           │  │
│  └────────────────────────────────────────────────────────────────────────────────────────┘  │
│                                                                                                │
│  [Save Configuration]                                                                        │
│                                                                                                │
└────────────────────────────────────────────────────────────────────────────────────────────────┘
```

---

## 5. INTEGRATION MANAGEMENT

### Integration Listing

```
┌────────────────────────────────────────────────────────────────────────────────────────────────┐
│  Product Integrations                                [+ Add Integration] [Manage Categories]    │
├────────────────────────────────────────────────────────────────────────────────────────────────┤
│                                                                                                │
│  Product: [Courier Management Cloud ▼]       Category: [All ▼]    Search: [__________] [🔍]    │
│                                                                                                │
│  ┌────────────────────────────────────────────────────────────────────────────────────────┐  │
│  │ Integration                     │ Type      │ Category     │ Status │ Actions          │  │
│  ├────────────────────────────────────────────────────────────────────────────────────────┤  │
│  │  [Shopify Logo]        Shopify   │ E-commerce│ Certified    │ Active   │ [⋮]             │  │
│  │  Connects with Shopify for seamless order syncing and automated shipping labels                     │  │
│  │                                                                                          │  │
│  │  [WooCommerce Logo]     WooCommerce│ E-commerce│ Basic       │ Active   │ [⋮]             │  │
│  │  Integration with WooCommerce stores for inventory sync and order fulfillment                     │  │
│  │                                                                                          │  │
│  │  [Salesforce Logo]     Salesforce│ CRM       │ Certified    │ Active   │ [⋮]             │  │
│  │  Sync customer data and sales pipeline information                                        │  │
│  │                                                                                          │  │
│  │  [Slack Logo]         Slack     │ Comms     │ Basic       │ Active   │ [⋮]             │  │
│  │  Notifications and updates delivered directly to Slack channels                            │  │
│  │                                                                                          │  │
│  │  [Zapier Logo]         Zapier    │ Automation│ Certified    │ Active   │ [⋮]             │  │
│  │  Connect 3000+ apps through Zapier automation platform                                  │  │
│  │                                                                                          │  │
│  └────────────────────────────────────────────────────────────────────────────────────────┘  │
│                                                                                                │
│  Showing 1-6 of 45 integrations                                                          Load More              │
│                                                                                                │
└────────────────────────────────────────────────────────────────────────────────────────────────┘
```

### Integration Detail Management

```
┌────────────────────────────────────────────────────────────────────────────────────────────────┐
│  Integration Details                      [← Back]                      [Delete Integration]             │
├────────────────────────────────────────────────────────────────────────────────────────────────┤
│                                                                                                │
│  ┌────────────────────────────────────────────────────────────────────────────────────────┐  │
│  │  BASIC INFO                                                                               │  │
│  ├────────────────────────────────────────────────────────────────────────────────────────┤  │
│  │  Name:           Shopify Integration                                                      │  │
│  │  Logo:           [shopify-logo.png                              ] [Upload...]        │  │
│  │  Description:    [Seamless integration with Shopify stores for...]                           │  │
│  │  Website:        [https://www.shopify.com/                      ]                      │  │
│  │  Category:       [E-commerce ▼]                                                            │  │
│  │  Certification:   [Certified ▼]                                                            │  │
│  │  Status:         [Active ▼]                                                              │  │
│  └────────────────────────────────────────────────────────────────────────────────────────┘  │
│                                                                                                │
│  ┌────────────────────────────────────────────────────────────────────────────────────────┐  │
│  │  INTEGRATION DETAILS                                                                      │  │
│  ├────────────────────────────────────────────────────────────────────────────────────────┤  │
│  │  Integration Type:   [Plugin ▼]                                                           │  │
│  │  API Version:        v2.1                                                                 │  │
│  │  Documentation URL:  [https://docs.gogidix.com/shopify-integration              ]                     │  │
│  │  Setup Guide URL:   [https://docs.gogidix.com/shopify-setup                     ]                     │  │
│  │                                                                                           │  │
│  │  Supported Features:                                                                    │  │
│  │  ☑ Order sync (Create orders in Gogidix when placed in Shopify)                           │  │
│  │  ☑ Inventory sync (Sync product inventory)                                               │  │
│  │  ☑ Shipment tracking (Send tracking updates to Shopify)                                      │  │
│  │  ☐ Customer sync (Sync customer data)                                                   │  │
│  │                                                                                           │  │
│  └────────────────────────────────────────────────────────────────────────────────────────┘  │
│                                                                                                │
│  ┌────────────────────────────────────────────────────────────────────────────────────────┐  │
│  │  USAGE STATISTICS                                                                          │  │
│  ├────────────────────────────────────────────────────────────────────────────────────────┤  │
│  │  Total Connections:  1,245                                                             │  │
│  │  Active Connections: 1,180                                                             │  │
│  │  API Calls (30 days): 45,678                                                           │  │
│  │  Last Error: 7 days ago                                                               │  │
│  │                                                                                           │  │
│  └────────────────────────────────────────────────────────────────────────────────────────┘  │
│                                                                                                │
│  ┌────────────────────────────────────────────────────────────────────────────────────────┐  │
│  │  LEAD ATTRIBUTION                                                                        │  │
│  ├────────────────────────────────────────────────────────────────────────────────────────┤  │
│  │  Leads from product page in last 30 days: 234                                         │  │
│  │  Qualified leads: 45                                                                      │  │
│  │  Conversion rate: 19.2%                                                                 │  │
│  │                                                                                           │  │
│  └────────────────────────────────────────────────────────────────────────────────────────┘  │
│                                                                                                │
│  [Save Changes] [View Integration Docs] [Test Connection]                                          │
│                                                                                                │
└────────────────────────────────────────────────────────────────────────────────────────────────┘
```

---

## VERSION HISTORY

| Version | Date | Changes |
|---------|------|---------|
| 1.0 | 2025-02-08 | Initial Product Catalog Documentation |

---

**Document End**
