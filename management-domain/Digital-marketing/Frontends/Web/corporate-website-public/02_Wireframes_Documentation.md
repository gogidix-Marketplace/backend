# GOGIDIX CORPORATE WEBSITE - WIREFRAMES

**Version:** 1.0
**Domain:** Corporate Website
**Frontend:** corporate-website-public
**Last Updated:** 2025-02-08

---

## TABLE OF CONTENTS

1. [Design System](#design-system)
2. [Page Wireframes](#page-wireframes)
3. [Components](#components)
4. [Responsive Design](#responsive-design)

---

## DESIGN SYSTEM

### Color Palette

| Color | Hex | CSS Variable | Usage |
|-------|-----|--------------|-------|
| Primary Black | #0A0A0A | `--color-primary` | Primary backgrounds, text |
| Pure White | #FFFFFF | `--color-white` | Backgrounds, text on dark |
| Accent Blue | #0066FF | `--color-accent` | CTAs, links, highlights |
| Accent Purple | #7C3AED | `--color-accent-alt` | Gradients, accents |
| Success Green | #10B981 | `--color-success` | Success states |
| Warning Amber | #F59E0B | `--color-warning` | Warnings |
| Error Red | #EF4444 | `--color-error` | Errors |
| Neutral Grays | #FAFAFA to #71717A | `--color-gray-*` | Borders, muted text |

### Typography

| Element | Font Family | Size | Weight | Line Height |
|---------|-------------|------|--------|-------------|
| Display Hero | Inter/San Francisco | 56-72px | 700-800 | 1.1 |
| H1 Heading | Inter/San Francisco | 48px | 700 | 1.2 |
| H2 Heading | Inter/San Francisco | 36px | 600 | 1.3 |
| H3 Heading | Inter/San Francisco | 28px | 600 | 1.4 |
| H4 Heading | Inter/San Francisco | 24px | 600 | 1.4 |
| Body Large | Inter/San Francisco | 18px | 400 | 1.6 |
| Body Base | Inter/San Francisco | 16px | 400 | 1.6 |
| Body Small | Inter/San Francisco | 14px | 400 | 1.5 |
| Caption | Inter/San Francisco | 12px | 400 | 1.4 |
| Code | JetBrains Mono | 14-16px | 400 | 1.5 |

### Spacing Scale

| Token | Value | Usage |
|-------|-------|-------|
| --space-0 | 0px | None |
| --space-1 | 4px | Tight gaps |
| --space-2 | 8px | Small gaps |
| --space-3 | 12px | Compact spacing |
| --space-4 | 16px | Default spacing |
| --space-5 | 20px | Comfortable spacing |
| --space-6 | 24px | Section spacing |
| --space-8 | 32px | Large spacing |
| --space-10 | 40px | XL spacing |
| --space-12 | 48px | XXL spacing |
| --space-16 | 64px | Hero spacing |
| --space-20 | 80px | Section margins |

### Border Radius

| Token | Value | Usage |
|-------|-------|-------|
| --radius-none | 0px | Sharp edges |
| --radius-sm | 4px | Small elements |
| --radius-md | 8px | Cards, buttons |
| --radius-lg | 12px | Large cards |
| --radius-xl | 16px | Hero cards |
| --radius-full | 9999px | Pills, badges |

### Shadows

| Token | Value | Usage |
|-------|-------|-------|
| --shadow-sm | 0 1px 2px rgba(0,0,0,0.05) | Subtle elevation |
| --shadow-md | 0 4px 6px rgba(0,0,0,0.07) | Cards, buttons |
| --shadow-lg | 0 10px 15px rgba(0,0,0,0.1) | Dropdowns, modals |
| --shadow-xl | 0 20px 25px rgba(0,0,0,0.15) | Large modals |
| --shadow-2xl | 0 25px 50px rgba(0,0,0,0.25) | Hero elements |

### Animation

| Property | Value | Usage |
|----------|-------|-------|
| --duration-fast | 150ms | Hover states |
| --duration-base | 200ms | Default transitions |
| --duration-slow | 300ms | Page elements |
| --duration-slower | 500ms | Section reveals |
| --easing-default | cubic-bezier(0.4, 0, 0.2, 1) | Standard easing |
| --easing-in | cubic-bezier(0.4, 0, 1, 1) | Enter animations |
| --easing-out | cubic-bezier(0, 0, 0.2, 1) | Exit animations |
| --easing-bounce | cubic-bezier(0.68, -0.55, 0.265, 1.55) | Playful interactions |

---

## PAGE WIREFRAMES

### Homepage

```
┌────────────────────────────────────────────────────────────────────────────────────────────────┐
│  [Gogidix]  Products ▼  Solutions ▼  Developers  Partners  Company ▼           [Login] [Get Start]│
└────────────────────────────────────────────────────────────────────────────────────────────────┘
┌────────────────────────────────────────────────────────────────────────────────────────────────┐
│                                                                                                │
│                     THE GLOBAL LOGISTICS & E-COMMERCE PLATFORM                                  │
│                                                                                                │
│            Powering businesses across 50+ countries with unified infrastructure                │
│                                                                                                │
│                              [Start Building Free] [Watch Demo]                                │
│                                                                                                │
│               ▶ Trusted by 10,000+ businesses worldwide                                         │
│                                                                                                │
└────────────────────────────────────────────────────────────────────────────────────────────────┘
┌────────────────────────────────────────────────────────────────────────────────────────────────┐
│                                                                                                │
│  ┌─────────────────┐  ┌─────────────────┐  ┌─────────────────┐  ┌─────────────────┐            │
│  │  [📦 Icon]       │  │  [🚚 Icon]       │  │  [🏪 Icon]       │  │  [📊 Icon]       │            │
│  │                 │  │                 │  │                 │  │                 │            │
│  │  Logistics      │  │  E-commerce     │  │  Procurement    │  │  Business       │            │
│  │  Platform       │  │  Platform       │  │  Platform       │  │  Operations     │            │
│  │                 │  │                 │  │                 │  │                 │            │
│  │  End-to-end     │  │  Multi-vendor   │  │  B2B sourcing   │  │  Complete suite │            │
│  │  shipping       │  │  marketplace    │  │  & procurement  │  │  for running    │            │
│  │                 │  │                 │  │                 │  │  your business  │            │
│  │  [Explore →]    │  │  [Explore →]    │  │  [Explore →]    │  │  [Explore →]    │            │
│  └─────────────────┘  └─────────────────┘  └─────────────────┘  └─────────────────┘            │
│                                                                                                │
└────────────────────────────────────────────────────────────────────────────────────────────────┘
┌────────────────────────────────────────────────────────────────────────────────────────────────┐
│                                                                                                │
│                                    TRUSTED BY LEADERS                                          │
│                                                                                                │
│     [Company Logo]    [Company Logo]    [Company Logo]    [Company Logo]    [Company Logo]    │
│                                                                                                │
└────────────────────────────────────────────────────────────────────────────────────────────────┘
┌────────────────────────────────────────────────────────────────────────────────────────────────┐
│                                                                                                │
│  ┌─────────────────────────────┐                                         ┌────────────────┐  │
│  │                             │                                         │   4.9/5        │  │
│  │     POWERFUL INFRASTRUCTURE  │                                         │   ★★★★★       │  │
│  │                             │                                         │                │  │
│  │  • 50+ Countries            │                                         │  G2 Reviews    │  │
│  │  • 99.99% Uptime            │                                         │  2,500+ reviews│  │
│  │  • 15 Shared Service Cores  │                                         │                │  │
│  │  • Enterprise-grade         │                                         │  [Read Reviews]│  │
│  │    Security                 │                                         └────────────────┘  │
│  │                             │                                                              │
│  │  [Learn About Infrastructure]                                                              │
│  └─────────────────────────────┘                                                              │
│                                                                                                │
└────────────────────────────────────────────────────────────────────────────────────────────────┘
┌────────────────────────────────────────────────────────────────────────────────────────────────┐
│                                                                                                │
│                           DEVELOPER-FIRST PLATFORM                                            │
│                                                                                                │
│     ┌─────────────────┐       ┌─────────────────┐       ┌─────────────────┐                   │
│     │  📚             │       │  🔌             │       │  ⚡             │                   │
│     │                 │       │                 │       │                 │                   │
│     │  Comprehensive  │       │  REST & GraphQL │       │  Real-time      │                   │
│     │  Documentation  │       │  APIs           │       │  Webhooks       │                   │
│     │                 │       │                 │       │                 │                   │
│     │  Full API       │       │  Official SDKs  │       │  Event-driven   │                   │
│     │  Reference      │       │  for all langs  │       │  architecture   │                   │
│     └─────────────────┘       └─────────────────┘       └─────────────────┘                   │
│                                                                                                │
│                                        [Start Building →]                                      │
│                                                                                                │
└────────────────────────────────────────────────────────────────────────────────────────────────┘
┌────────────────────────────────────────────────────────────────────────────────────────────────┐
│                                                                                                │
│                              CUSTOMER SUCCESS STORIES                                          │
│                                                                                                │
│  ┌─────────────────────────────┐  ┌─────────────────────────────────────────┐                 │
│  │  [Customer Photo]           │  │  "Gogidix transformed our operations.    │                 │
│  │                             │  │   We reduced shipping times by 60% and    │                 │
│  │  Sarah Johnson              │  │   cut operational costs significantly."   │                 │
│  │  CEO, LogiTech Africa       │  │                                          │                 │
│  │                             │  │  — Sarah Johnson, CEO                      │                 │
│  │  [Read Case Study →]        │  │                                          │                 │
│  └─────────────────────────────┘  └─────────────────────────────────────────┘                 │
│                                                                                                │
└────────────────────────────────────────────────────────────────────────────────────────────────┘
┌────────────────────────────────────────────────────────────────────────────────────────────────┐
│                                                                                                │
│                              READY TO GET STARTED?                                             │
│                                                                                                │
│                    Join thousands of businesses building on Gogidix                             │
│                                                                                                │
│                              [Start Free] [Talk to Sales]                                      │
│                                                                                                │
└────────────────────────────────────────────────────────────────────────────────────────────────┘
┌────────────────────────────────────────────────────────────────────────────────────────────────┐
│  Products │ Solutions │ Developers │ Partners │ Company │ Resources │ © 2025 Gogidix          │
│  [Social Icons]                                                                  Privacy │ Terms │
└────────────────────────────────────────────────────────────────────────────────────────────────┘
```

### Products Overview Page

```
┌────────────────────────────────────────────────────────────────────────────────────────────────┐
│  [Gogidix]  Products ▼  Solutions ▼  Developers  Partners  Company ▼           [Login] [Get Start]│
└────────────────────────────────────────────────────────────────────────────────────────────────┘
┌────────────────────────────────────────────────────────────────────────────────────────────────┐
│                                                                                                │
│                                     PRODUCTS                                                    │
│                                                                                                │
│              Complete platform for logistics, e-commerce, and business operations               │
│                                                                                                │
└────────────────────────────────────────────────────────────────────────────────────────────────┘
┌────────────────────────────────────────────────────────────────────────────────────────────────┐
│                                                                                                │
│  ┌────────────────────────────────────────────────────────────────────────────────────────┐   │
│  │  FILTERS                                    ▼                                            │   │
│  ├────────────────────────────────────────────────────────────────────────────────────────┤   │
│  │  Category: [All Categories ▼]                                                  [Clear]  │   │
│  │  Platform: [All Platforms ▼]                                                            │   │
│  │  Use Case: [All Use Cases ▼]                                                            │   │
│  └────────────────────────────────────────────────────────────────────────────────────────┘   │
│                                                                                                │
│  ┌────────────────────────────────────────────────────────────────────────────────────────┐   │
│  │  LOGISTICS PLATFORM                                                                      │   │
│  ├────────────────────────────────────────────────────────────────────────────────────────┤   │
│  │                                                                                          │   │
│  │  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐  │   │
│  │  │    [📦]     │  │    [🏭]     │  │    [🚚]     │  │    [✈️]     │  │    [🚢]     │  │   │
│  │  │  Courier    │  │  Warehouse  │  │  Haulage    │  │  Air Freight│  │  Ocean Ship │  │   │
│  │  │  Management │  │  Management │  │  Management │  │             │  │             │  │   │
│  │  │             │  │             │  │             │  │             │  │             │  │   │
│  │  │  25+        │  │  30+        │  │  20+        │  │  Full       │  │  Full       │  │   │
│  │  │  Services   │  │  Services   │  │  Services   │  │  Suite      │  │  Suite      │  │   │
│  │  └─────────────┘  └─────────────┘  └─────────────┘  └─────────────┘  └─────────────┘  │   │
│  │                                                                                          │   │
│  └────────────────────────────────────────────────────────────────────────────────────────┘   │
│                                                                                                │
│  ┌────────────────────────────────────────────────────────────────────────────────────────┐   │
│  │  E-COMMERCE PLATFORM                                                                      │   │
│  ├────────────────────────────────────────────────────────────────────────────────────────┤   │
│  │                                                                                          │   │
│  │  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐  │   │
│  │  │    [🛒]     │  │    [📱]     │  │    [📅]     │  │    [💳]     │  │    [📦]     │  │   │
│  │  │ Marketplace │  │  Social     │  │  Booking    │  │  Payment    │  │  Order      │  │   │
│  │  │             │  │  Commerce   │  │  Engine     │  │  Gateway    │  │  Management │  │   │
│  │  │  Multi-     │  │  Social     │  │  Real-time  │  │  Multiple   │  │  Full       │  │   │
│  │  │  vendor     │  │  selling    │  │  booking    │  │  providers  │  │  fulfillment│  │   │
│  │  └─────────────┘  └─────────────┘  └─────────────┘  └─────────────┘  └─────────────┘  │   │
│  │                                                                                          │   │
│  └────────────────────────────────────────────────────────────────────────────────────────┘   │
│                                                                                                │
│  ┌────────────────────────────────────────────────────────────────────────────────────────┐   │
│  │  PROCUREMENT PLATFORM                                                                     │   │
│  ├────────────────────────────────────────────────────────────────────────────────────────┤   │
│  │                                                                                          │   │
│  │  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐                                      │   │
│  │  │    [🏢]     │  │    [📊]     │  │    [🤝]     │                                      │   │
│  │  │ Corporate   │  │  Wholesale  │  │  Vendor     │                                      │   │
│  │  │ Procurement │  │  Management │  │  Management │                                      │   │
│  │  │  B2B        │  │  Bulk       │  │  Supplier   │                                      │   │
│  │  │  sourcing   │  │  operations │  │  management │                                      │   │
│  │  └─────────────┘  └─────────────┘  └─────────────┘                                      │   │
│  │                                                                                          │   │
│  └────────────────────────────────────────────────────────────────────────────────────────┘   │
│                                                                                                │
│  ┌────────────────────────────────────────────────────────────────────────────────────────┐   │
│  │  BUSINESS OPERATIONS                                                                     │   │
│  ├────────────────────────────────────────────────────────────────────────────────────────┤   │
│  │                                                                                          │   │
│  │  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐  │   │
│  │  │    [👤]     │  │    [💰]     │  │    [👥]     │  │    [📈]     │  │    [🎯]     │  │   │
│  │  │ Admin      │  │  Finance    │  │  HR         │  │  Sales      │  │  Marketing  │  │   │
│  │  │ Cloud      │  │  Cloud      │  │  Cloud      │  │  Cloud      │  │  Cloud      │  │   │
│  │  │  Country    │  │  Financial  │  │  Human      │  │  CRM &      │  │  Campaign   │  │   │
│  │  │  admin      │  │  management │  │  resources  │  │  sales      │  │  management │  │   │
│  │  └─────────────┘  └─────────────┘  └─────────────┘  └─────────────┘  └─────────────┘  │   │
│  │  ┌─────────────┐  ┌─────────────┘                                                      │   │
│  │  │    [💬]     │  │    [📊]     │                                                      │   │
│  │  │ Support     │  │  GBM        │                                                      │   │
│  │  │ Cloud      │  │  Cloud      │                                                      │   │
│  │  │  Customer   │  │  Global     │                                                      │   │
│  │  │  service   │  │  business   │                                                      │   │
│  │  └─────────────┘  └─────────────┘                                                      │   │
│  │                                                                                          │   │
│  └────────────────────────────────────────────────────────────────────────────────────────┘   │
│                                                                                                │
│  ┌────────────────────────────────────────────────────────────────────────────────────────┐   │
│  │  ENTERPRISE MANAGEMENT                                                                   │   │
│  ├────────────────────────────────────────────────────────────────────────────────────────┤   │
│  │                                                                                          │   │
│  │  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐  │   │
│  │  │    [🎩]     │  │    [🌍]     │  │    [💼]     │  │    [👔]     │  │    [📊]     │  │   │
│  │  │ Executive  │  │  Global     │  │  Corporate  │  │  Corporate  │  │  Global     │  │   │
│  │  │ Dashboard  │  │  Business   │  │  Finance    │  │  HR         │  │  Sales      │  │   │
│  │  │  C-suite   │  │  Management │  │  Multi-     │  │  Multi-     │  │  Multi-     │  │   │
│  │  │  analytics │  │  Strategy   │  │  entity     │  │  country    │  │  region     │  │   │
│  │  └─────────────┘  └─────────────┘  └─────────────┘  └─────────────┘  └─────────────┘  │   │
│  │  ┌─────────────┐  ┌─────────────┘  ┌─────────────┘                                      │   │
│  │  │    [🎧]     │  │    [📢]     │  │    [⚙️]     │                                      │   │
│  │  │ Global     │  │  Global     │  │  Systems    │                                      │   │
│  │  │ Support    │  │  Marketing  │  │  Management │                                      │   │
│  │  │  Multi-    │  │  Multi-     │  │  IT         │                                      │   │
│  │  │  region    │  │  country    │  │  infra      │                                      │   │
│  │  └─────────────┘  └─────────────┘  └─────────────┘                                      │   │
│  │                                                                                          │   │
│  └────────────────────────────────────────────────────────────────────────────────────────┘   │
│                                                                                                │
│  ┌────────────────────────────────────────────────────────────────────────────────────────┐   │
│  │  INFRASTRUCTURE                                                                          │   │
│  ├────────────────────────────────────────────────────────────────────────────────────────┤   │
│  │                                                                                          │   │
│  │  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐  │   │
│  │  │    [🔧]     │  │    [🤖]     │  │    [⚙️]     │  │    [📍]     │  │    [🔄]     │  │   │
│  │  │ Platform   │  │  AI         │  │  Orchest-   │  │  Universal  │  │  Trans-     │  │   │
│  │  │            │  │  Services   │  │  ration     │  │  Tracking   │  │  action     │  │   │
│  │  │  Core      │  │  ML &       │  │  Workflow   │  │  Real-time  │  │  Distributed│  │   │
│  │  │  platform  │  │  predictive │  │  automa-    │  │  tracking   │  │  transactions│  │   │
│  │  │            │  │  analytics  │  │  tion       │  │             │  │             │  │   │
│  │  └─────────────┘  └─────────────┘  └─────────────┘  └─────────────┘  └─────────────┘  │   │
│  │  ┌─────────────┘                                                                          │   │
│  │  │    [🔐]                                                                               │   │
│  │  │  Auth Infra                                                                          │   │
│  │  │  Identity & Access                                                                    │   │
│  │  │  OAuth, SSO, MFA                                                                      │   │
│  │  └─────────────────────────────────────────────────────────────────────────────────────┘   │
│  │                                                                                          │   │
│  └────────────────────────────────────────────────────────────────────────────────────────┘   │
│                                                                                                │
└────────────────────────────────────────────────────────────────────────────────────────────────┘
```

### Product Detail Page (Courier Management Example)

```
┌────────────────────────────────────────────────────────────────────────────────────────────────┐
│  [← Products]  Courier Management Cloud                    [View Pricing] [Start Free Trial]   │
├────────────────────────────────────────────────────────────────────────────────────────────────┤
│  Overview    Features    Pricing    Documentation    Case Studies    Contact                  │
└────────────────────────────────────────────────────────────────────────────────────────────────┘
┌────────────────────────────────────────────────────────────────────────────────────────────────┐
│                                                                                                │
│  ┌─────────────────────────────────────────┐  ┌─────────────────────────────────────────┐    │
│  │                                         │  │                                         │    │
│  │      COURIER MANAGEMENT CLOUD           │  │        GET STARTED IN MINUTES           │    │
│  │                                         │  │                                         │    │
│  │  Complete courier operations platform   │  │  [Email]                              │    │
│  │  with 25+ integrated services           │  │                                         │    │
│  │                                         │  │  [Start Free Trial →]                │    │
│  │  • Package delivery & routing          │  │                                         │    │
│  │  • Real-time tracking                  │  │  or [Contact Sales]                    │    │
│  │  • Driver management                   │  │                                         │    │
│  │  • Automated dispatch                  │  │                                         │    │
│  │                                         │  │  ✓ No credit card required             │    │
│  └─────────────────────────────────────────┘  └─────────────────────────────────────────┘    │
│                                                                                                │
└────────────────────────────────────────────────────────────────────────────────────────────────┘
┌────────────────────────────────────────────────────────────────────────────────────────────────┐
│                                                                                                │
│                                    KEY FEATURES                                                │
│                                                                                                │
│  ┌───────────────────┐  ┌───────────────────┐  ┌───────────────────┐  ┌───────────────────┐  │
│  │  [📦 Icon]        │  │  [📍 Icon]        │  │  [🚚 Icon]        │  │  [📊 Icon]        │  │
│  │                   │  │                   │  │                   │  │                   │  │
│  │  Package          │  │  Real-time        │  │  Smart            │  │  Analytics        │  │
│  │  Management       │  │  Tracking         │  │  Routing          │  │  Dashboard        │  │
│  │                   │  │                   │  │                   │  │                   │  │
│  │  End-to-end       │  │  GPS tracking     │  │  AI-powered       │  │  Comprehensive    │  │
│  │  package lifecycle│  │  for every        │  │  route            │  │  reports and      │  │
│  │  tracking         │  │  shipment         │  │  optimization     │  │  insights         │  │
│  │                   │  │                   │  │                   │  │                   │  │
│  │  [Learn more →]   │  │  [Learn more →]   │  │  [Learn more →]   │  │  [Learn more →]   │  │
│  └───────────────────┘  └───────────────────┘  └───────────────────┘  └───────────────────┘  │
│                                                                                                │
└────────────────────────────────────────────────────────────────────────────────────────────────┘
┌────────────────────────────────────────────────────────────────────────────────────────────────┐
│                                                                                                │
│                                    API OVERVIEW                                                │
│                                                                                                │
│  ┌────────────────────────────────────────────────────────────────────────────────────────┐   │
│  │  // Create a new shipment                                                               │   │
│  │  POST /v1/courier/shipments                                                             │   │
│  │                                                                                          │   │
│  │  {                                                                                        │   │
│  │    "origin": { "address": "123 Main St", "city": "Lagos", "country": "NG" },           │   │
│  │    "destination": { "address": "456 Oak Ave", "city": "Accra", "country": "GH" },      │   │
│  │    "package": { "weight": 2.5, "dimensions": { "length": 30, "width": 20, "height": 15 } }│   │
│  │  }                                                                                        │   │
│  │                                                                                          │   │
│  │  // Response                                                                             │   │
│  │  { "shipmentId": "SHP-123456", "trackingNumber": "GG-7890123456", "eta": "2025-02-10" }│   │
│  └────────────────────────────────────────────────────────────────────────────────────────┘   │
│                                                                                                │
│                                       [View Full API Docs →]                                  │
│                                                                                                │
└────────────────────────────────────────────────────────────────────────────────────────────────┘
┌────────────────────────────────────────────────────────────────────────────────────────────────┐
│                                                                                                │
│                                    PRICING                                                     │
│                                                                                                │
│  ┌─────────────────────┐  ┌─────────────────────┐  ┌─────────────────────┐                   │
│  │    STARTER          │  │    PROFESSIONAL     │  │    ENTERPRISE       │                   │
│  │                     │  │                     │  │                     │                   │
│  │    $49/month        │  │    $199/month       │  │    Custom           │                   │
│  │                     │  │                     │  │                     │                   │
│  │  • 500 shipments/   │  │  • 5,000 shipments/ │  │  • Unlimited       │                   │
│  │    month            │  │    month            │  │    shipments       │                   │
│  │  • 2 users          │  │  • 10 users         │  │  • Unlimited users │                   │
│  │  • Basic analytics  │  │  • Advanced reports │  │  • Custom reports  │                   │
│  │  • Email support    │  │  • Priority support │  │  • Dedicated       │                   │
│  │                     │  │  • API access       │  │    support         │                   │
│  │  [Get Started]      │  │  [Get Started]      │  │  [Contact Sales]    │                   │
│  └─────────────────────┘  └─────────────────────┘  └─────────────────────┘                   │
│                                                                                                │
└────────────────────────────────────────────────────────────────────────────────────────────────┘
┌────────────────────────────────────────────────────────────────────────────────────────────────┐
│                                                                                                │
│                                    CASE STUDIES                                                │
│                                                                                                │
│  ┌─────────────────────────────┐  ┌─────────────────────────────────────────┐                 │
│  │  [Company Logo]             │  │  "We scaled from 100 to 10,000 daily    │                 │
│  │                             │  │   shipments in 6 months with Gogidix."   │                 │
│  │  SwiftDeliver               │  │                                          │                 │
│  │  Last-Mile Delivery, NG     │  │  — Emmanuel Okon, Operations Director    │                 │
│  │                             │  │                                          │                 │
│  │  [Read Story →]             │  │  ─────────────────────────────────────    │                 │
│  │                             │  │  • 60% faster delivery times              │                 │
│  │                             │  │  • 40% reduction in operational costs     │                 │
│  │                             │  │  • 99.8% on-time delivery rate            │                 │
│  └─────────────────────────────┘  └─────────────────────────────────────────┘                 │
│                                                                                                │
└────────────────────────────────────────────────────────────────────────────────────────────────┘
```

### Developer Portal Page

```
┌────────────────────────────────────────────────────────────────────────────────────────────────┐
│  [Gogidix]  Products ▼  Solutions ▼  Developers ▼  Partners  Company ▼     [Login] [Get Start]│
└────────────────────────────────────────────────────────────────────────────────────────────────┘
┌────────────────────────────────────────────────────────────────────────────────────────────────┐
│                                                                                                │
│                                   DEVELOPER PORTAL                                             │
│                                                                                                │
│                     Build powerful integrations with Gogidix APIs                               │
│                                                                                                │
│                              [Get API Key] [Read Docs] [Join Discord]                          │
│                                                                                                │
└────────────────────────────────────────────────────────────────────────────────────────────────┘
┌────────────────────────────────────────────────────────────────────────────────────────────────┐
│                                                                                                │
│  ┌─────────────────────────────────────────┐  ┌─────────────────────────────────────────┐    │
│  │                                         │  │                                         │    │
│  │        QUICK START GUIDE                │  │        SANDBOX ACCESS                   │    │
│  │                                         │  │                                         │    │
│  │  1. Get your API keys                   │  │  Test your integrations in our safe     │    │
│  │  2. Choose your SDK                     │  │  sandbox environment                   │    │
│  │  3. Make your first API call            │  │                                         │    │
│  │  4. Explore webhooks                    │  │  [Access Sandbox →]                    │    │
│  │                                         │  │                                         │    │
│  │  [Start Building →]                    │  │  Sandbox Key: sbk_test_abc123...        │    │
│  │                                         │  │                                         │    │
│  └─────────────────────────────────────────┘  └─────────────────────────────────────────┘    │
│                                                                                                │
└────────────────────────────────────────────────────────────────────────────────────────────────┘
┌────────────────────────────────────────────────────────────────────────────────────────────────┐
│                                                                                                │
│                                    API PRODUCTS                                                │
│                                                                                                │
│  ┌────────────────────────────────────────────────────────────────────────────────────────┐   │
│  │  COURIER API                           WAREHOUSE API                                   │   │
│  │  ──────────────────────────────────────────────────────────────────────────────────    │   │
│  │  POST /v1/courier/shipments          POST /v1/warehouse/inventory                    │   │
│  │  GET  /v1/courier/shipments/:id       GET  /v1/warehouse/items                       │   │
│  │  GET  /v1/courier/tracking/:number    POST /v1/warehouse/transfer                    │   │
│  │  POST /v1/courier/cancel/:id          GET  /v1/warehouse/locations                   │   │
│  │                                         [Explore →]                                    │   │
│  │  [Explore →]                                                                          │   │
│  └────────────────────────────────────────────────────────────────────────────────────────┘   │
│                                                                                                │
│  ┌────────────────────────────────────────────────────────────────────────────────────────┐   │
│  │  E-COMMERCE API                        PROCUREMENT API                                  │   │
│  │  ──────────────────────────────────────────────────────────────────────────────────    │   │
│  │  POST /v1/ecommerce/orders           POST /v1/procurement/requests                   │   │
│  │  GET  /v1/ecommerce/products          GET  /v1/procurement/vendors                   │   │
│  │  POST /v1/ecommerce/payments          POST /v1/procurement/purchase-orders           │   │
│  │  GET  /v1/ecommerce/customers         GET  /v1/procurement/analytics                 │   │
│  │                                         [Explore →]                                    │   │
│  │  [Explore →]                                                                          │   │
│  └────────────────────────────────────────────────────────────────────────────────────────┘   │
│                                                                                                │
│  ┌────────────────────────────────────────────────────────────────────────────────────────┐   │
│  │  BUSINESS OPERATIONS API              ENTERPRISE API                                   │   │
│  │  ──────────────────────────────────────────────────────────────────────────────────    │   │
│  │  POST /v1/admin/users                POST /v1/executive/dashboard                     │   │
│  │  GET  /v1/finance/reports             GET  /v1/global/analytics                       │   │
│  │  POST /v1/hr/employees                POST /v1/corporate/reports                       │   │
│  │  GET  /v1/sales/opportunities         GET  /v1/systems/status                         │   │
│  │                                         [Explore →]                                    │   │
│  │  [Explore →]                                                                          │   │
│  └────────────────────────────────────────────────────────────────────────────────────────┘   │
│                                                                                                │
└────────────────────────────────────────────────────────────────────────────────────────────────┘
┌────────────────────────────────────────────────────────────────────────────────────────────────┐
│                                                                                                │
│                                    OFFICIAL SDKS                                               │
│                                                                                                │
│  ┌───────────────────┐  ┌───────────────────┐  ┌───────────────────┐  ┌───────────────────┐  │
│  │  [Node.js Icon]   │  │  [Python Icon]    │  │  [PHP Icon]       │  │  [Go Icon]        │  │
│  │                   │  │                   │  │                   │  │                   │  │
│  │  Node.js SDK      │  │  Python SDK       │  │  PHP SDK          │  │  Go SDK           │  │
│  │                   │  │                   │  │                   │  │                   │  │
│  │  @gogidix/sdk     │  │  gogidix-python   │  │  gogidix/php      │  │  github.com/...   │  │
│  │                   │  │                   │  │                   │  │                   │  │
│  │  [npm install →]  │  │  [pip install →]  │  │  [composer →]     │  │  [go get →]       │  │
│  └───────────────────┘  └───────────────────┘  └───────────────────┘  └───────────────────┘  │
│                                                                                                │
│  ┌───────────────────┐  ┌───────────────────┐  ┌───────────────────┐                          │
│  │  [Java Icon]      │  │  [.NET Icon]      │  │  [Ruby Icon]      │                          │
│  │                   │  │                   │  │                   │                          │
│  │  Java SDK         │  │  .NET SDK         │  │  Ruby SDK         │                          │
│  │                   │  │                   │  │                   │                          │
│  │  [Maven →]        │  │  [NuGet →]        │  │  [Gem →]          │                          │
│  └───────────────────┘  └───────────────────┘  └───────────────────┘                          │
│                                                                                                │
└────────────────────────────────────────────────────────────────────────────────────────────────┘
┌────────────────────────────────────────────────────────────────────────────────────────────────┐
│                                                                                                │
│                                    SYSTEM STATUS                                               │
│                                                                                                │
│  ┌────────────────────────────────────────────────────────────────────────────────────────┐   │
│  │  All Systems Operational                                                                  │   │
│  │  ✓ Courier API    ✓ Warehouse API    ✓ E-commerce API    ✓ Payment Gateway             │   │
│  │  Uptime: 99.99%    Last incident: 7 days ago    [View Status Page →]                     │   │
│  └────────────────────────────────────────────────────────────────────────────────────────┘   │
│                                                                                                │
└────────────────────────────────────────────────────────────────────────────────────────────────┘
```

---

## COMPONENTS

### Button

```typescript
interface ButtonProps {
  variant: 'primary' | 'secondary' | 'outline' | 'ghost' | 'link';
  size: 'sm' | 'md' | 'lg' | 'xl';
  disabled?: boolean;
  loading?: boolean;
  iconLeft?: ReactNode;
  iconRight?: ReactNode;
  fullWidth?: boolean;
}
```

**Variants:**
- Primary: Solid accent blue background
- Secondary: Gray background
- Outline: Transparent with border
- Ghost: Transparent, hover background
- Link: Text-only with underline

### Card

```typescript
interface CardProps {
  variant: 'default' | 'elevated' | 'outlined' | 'flat';
  padding: 'none' | 'sm' | 'md' | 'lg' | 'xl';
  interactive?: boolean;
  header?: ReactNode;
  footer?: ReactNode;
}
```

### Badge

```typescript
interface BadgeProps {
  variant: 'default' | 'success' | 'warning' | 'error' | 'info';
  size: 'sm' | 'md' | 'lg';
  dot?: boolean;
}
```

### Modal

```typescript
interface ModalProps {
  size: 'sm' | 'md' | 'lg' | 'xl' | 'full';
  position: 'center' | 'top' | 'bottom';
  closable?: boolean;
  backdrop?: boolean;
}
```

### Navigation

**Breadcrumb:**
```
Home > Products > Logistics > Courier Management
```

**Tabs:**
```
[Overview] [Features] [Pricing] [Documentation]
```

**Pagination:**
```
[<] [1] [2] [3] ... [10] [>]
```

### Form Components

**Input:**
- Text input with floating label
- Error state validation
- Character counter
- Clear button

**Select:**
- Single select
- Multi select with chips
- Searchable dropdown
- Custom option rendering

**Checkbox/Radio:**
- Custom styled controls
- Group validation
- Indeterminate state

---

## RESPONSIVE DESIGN

### Breakpoints

| Breakpoint | Min Width | Max Width | Columns | Gutter |
|------------|-----------|-----------|---------|--------|
| Mobile XS | 320px | 479px | 1 | 16px |
| Mobile SM | 480px | 639px | 1 | 16px |
| Mobile MD | 640px | 767px | 1 | 16px |
| Tablet | 768px | 1023px | 2 | 24px |
| Desktop SM | 1024px | 1279px | 3 | 24px |
| Desktop MD | 1280px | 1439px | 4 | 24px |
| Desktop LG | 1440px | 1919px | 4 | 32px |
| Wide | 1920px+ | - | 4-6 | 32px |

### Mobile Adaptations

**Navigation:**
- Hamburger menu with slide-in drawer
- Bottom tab bar for key sections
- Sticky CTA buttons

**Content:**
- Single column layout
- Stacked cards
- Full-width images
- Simplified tables (horizontal scroll)

**Interactions:**
- Larger touch targets (44px min)
- Swipe gestures for carousels
- Pull-to-refresh on data pages

### Tablet Adaptations

**Navigation:**
- Collapsible sidebar
- Larger tap targets
- Split-screen views where appropriate

**Content:**
- 2-column grids
- Expandable cards
- Enhanced tables with sorting

### Desktop Adaptations

**Navigation:**
- Full header with mega menus
- Sidebar navigation for docs
- Quick search always visible

**Content:**
- Multi-column layouts
- Hover interactions
- Rich animations and transitions

---

## VERSION HISTORY

| Version | Date | Changes |
|---------|------|---------|
| 1.0 | 2025-02-08 | Initial Wireframes Documentation |

---

**Document End**
