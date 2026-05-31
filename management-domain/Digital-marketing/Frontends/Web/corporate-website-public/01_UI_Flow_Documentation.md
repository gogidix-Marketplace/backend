# GOGIDIX CORPORATE WEBSITE - UI FLOW

**Version:** 1.0
**Domain:** Corporate Website
**Frontend:** corporate-website-public
**Framework:** Next.js 14 + React 18 + TypeScript + Tailwind CSS + Framer Motion
**State Management:** Zustand + React Context
**Data Fetching:** TanStack Query + SWR
**Last Updated:** 2025-02-08

---

## OVERVIEW

World-class corporate website serving as the public face of Gogidix - a global logistics and e-commerce platform. Showcases product portfolios, SaaS offerings, enterprise solutions, developer platform, and partner programs. Designed to Apple/Google/Amazon standards with exceptional UX, accessibility, and performance.

---

## NAVIGATION STRUCTURE

```
Corporate Website
├── Home
├── Products
│   ├── Logistics Platform
│   │   ├── Courier Management
│   │   ├── Warehouse Management
│   │   ├── Haulage Management
│   │   ├── Air Freight
│   │   ├── Ocean Shipping
│   │   └── Fleet Tracking
│   ├── E-commerce Platform
│   │   ├── Marketplace
│   │   ├── Social Commerce
│   │   ├── Booking Engine
│   │   ├── Payment Gateway
│   │   └── Order Management
│   ├── Procurement Platform
│   │   ├── Corporate Procurement
│   │   ├── Wholesale Management
│   │   ├── Vendor Management
│   │   └── Procurement Analytics
│   ├── Business Operations
│   │   ├── Admin Cloud
│   │   ├── Finance Cloud
│   │   ├── HR Cloud
│   │   ├── Sales Cloud
│   │   ├── Marketing Cloud
│   │   ├── Support Cloud
│   │   └── GBM Cloud
│   ├── Enterprise Management
│   │   ├── Executive Dashboard
│   │   ├── Global Business Management
│   │   ├── Corporate Finance
│   │   ├── Corporate HR
│   │   ├── Global Sales
│   │   ├── Global Support
│   │   ├── Global Marketing
│   │   └── Systems Management
│   └── Infrastructure
│       ├── Platform
│       ├── AI Services
│       ├── Orchestration
│       ├── Tracking Services
│       ├── Transaction Management
│       └── Auth Infrastructure
├── Solutions
│   ├── By Industry
│   ├── By Company Size
│   ├── By Region
│   └── Case Studies
├── Developers
│   ├── API Reference
│   ├── SDKs
│   ├── Webhooks
│   ├── Sandbox
│   ├── Integration Guides
│   └── Status
├── Partners
│   ├── White-Label Program
│   ├── Technology Partners
│   ├── System Integrators
│   └── Marketplace Partners
├── Company
│   ├── About Us
│   ├── Leadership
│   ├── Careers
│   ├── Press
│   └── Contact
├── Resources
│   ├── Documentation
│   ├── Blog
│   ├── Webinars
│   └── Security
└── [Login]
```

---

## USER FLOW ARCHITECTURE

```
┌─────────────────────────────────────────────────────────────────────────────────┐
│                         USER FLOW - CORPORATE WEBSITE                            │
├─────────────────────────────────────────────────────────────────────────────────┤
│                                                                                  │
│  ┌────────────┐    ┌────────────┐    ┌────────────┐    ┌────────────┐          │
│  │  Visitor   │───>│  Landing   │───>│  Explore   │───>│  Convert   │          │
│  │  Arrives   │    │  on Home   │    │  Products  │    │  to Lead   │          │
│  └────────────┘    └────────────┘    └────────────┘    └────────────┘          │
│       │                  │                  │                  │                │
│       │                  │                  ▼                  ▼                │
│       │                  │        ┌──────────────┐    ┌──────────────┐         │
│       │                  │        │  Product     │    │  Request     │         │
│       │                  │        │  Details     │    │  Demo/       │         │
│       │                  │        │  Page        │    │  Contact     │         │
│       │                  │        └──────────────┘    └──────────────┘         │
│       │                  │                                                      │
│       │                  └─────────────────────────────────────────────────┐   │
│       │                                                                    │   │
│       ▼                                                                    ▼   │
│  ┌────────────┐    ┌────────────┐    ┌────────────┐    ┌────────────┐         │
│  │  Developer │───>│  API       │───>│  Sandbox   │───>│  Start      │         │
│  │  Flow      │    │  Docs      │    │  Access    │    │  Building   │         │
│  └────────────┘    └────────────┘    └────────────┘    └────────────┘         │
│                                                                                  │
│  ┌────────────┐    ┌────────────┐    ┌────────────┐    ┌────────────┐         │
│  │  Partner   │───>│  Partner   │───>│  Apply     │───>│  Become     │         │
│  │  Flow      │    │  Program   │    │  Form       │    │  Partner    │         │
│  └────────────┘    └────────────┘    └────────────┘    └────────────┘         │
│                                                                                  │
│  ┌────────────┐    ┌────────────┐    ┌────────────┐    ┌────────────┐         │
│  │  Career    │───>│  Open      │───>│  Apply     │───>│  Submit     │         │
│  │  Seeker    │    │  Positions │    │  Online     │    │  Application│         │
│  └────────────┘    └────────────┘    └────────────┘    └────────────┘         │
└─────────────────────────────────────────────────────────────────────────────────┘
```

---

## MAIN USER FLOWS

### 1. Product Discovery Flow

```
┌────────────────┐     ┌────────────────┐     ┌────────────────┐
│ User lands on  │────>│ Browses        │────>│ Selects        │
│ Homepage       │     │ Products menu   │     │ Category       │
└────────────────┘     └────────────────┘     └────────────────┘
                                                      │
                      ┌─────────────────────────────┼─────────────────────────────┐
                      ▼                             ▼                             ▼
               ┌─────────────┐              ┌─────────────┐              ┌─────────────┐
               │ Logistics   │              │ E-commerce  │              │ Enterprise  │
               │ Products    │              │ Products    │              │ Solutions  │
               └──────┬──────┘              └──────┬──────┘              └──────┬──────┘
                      │                            │                            │
                      ▼                            ▼                            ▼
               ┌─────────────┐              ┌─────────────┐              ┌─────────────┐
               │ View        │              │ View        │              │ View        │
               │ Product     │              │ Product     │              │ Product     │
               │ Details     │              │ Details     │              │ Details     │
               └──────┬──────┘              └──────┬──────┘              └──────┬──────┘
                      │                            │                            │
                      ▼                            ▼                            ▼
               ┌─────────────┐              ┌─────────────┐              ┌─────────────┐
               │ Request     │              │ Start Free  │              │ Contact     │
               │ Demo        │              │ Trial       │              │ Sales       │
               └─────────────┘              └─────────────┘              └─────────────┘
```

### 2. Developer Onboarding Flow

```
┌────────────────┐     ┌────────────────┐     ┌────────────────┐
│ Developer      │────>│ Visits         │────>│ Explores       │
│ lands on site  │     │ Developers     │     │ API Reference  │
└────────────────┘     └────────────────┘     └────────────────┘
                                                      │
                      ┌─────────────────────────────┼─────────────────────────────┐
                      ▼                             ▼                             ▼
               ┌─────────────┐              ┌─────────────┐              ┌─────────────┐
               │ Quick Start │              │ Full API    │              │ Download   │
               │ Guide       │              │ Reference   │              │ SDKs       │
               └──────┬──────┘              └──────┬──────┘              └──────┬──────┘
                      │                            │                            │
                      ▼                            ▼                            ▼
               ┌─────────────┐              ┌─────────────┐              ┌─────────────┐
               │ Get Sandbox │              │ Generate    │              │ Join       │
               │ Credentials │              │ API Key     │              │ Discord/    │
               │             │              │             │              │ Community  │
               └──────┬──────┘              └──────┬──────┘              └──────┬──────┘
                      │                            │                            │
                      └────────────────────────────┴────────────────────────────┘
                                                      │
                                                      ▼
                                               ┌─────────────┐
                                               │ Make First  │
                                               │ API Call    │
                                               └─────────────┘
```

### 3. Partner Application Flow

```
┌────────────────┐     ┌────────────────┐     ┌────────────────┐
│ Business       │────>│ Visits Partners │────>│ Selects        │
│ visitor        │     │ page           │     │ Program Type   │
└────────────────┘     └────────────────┘     └────────────────┘
                                                      │
                      ┌─────────────────────────────┼─────────────────────────────┐
                      ▼                             ▼                             ▼
               ┌─────────────┐              ┌─────────────┐              ┌─────────────┐
               │ White-Label │              │ Technology  │              │ System      │
               │ Reseller    │              │ Partner     │              │ Integrator  │
               └──────┬──────┘              └──────┬──────┘              └──────┬──────┘
                      │                            │                            │
                      ▼                            ▼                            ▼
               ┌─────────────┐              ┌─────────────┐              ┌─────────────┐
               │ Completes   │              │ Submits     │              │ Schedules   │
               │ Application │              │ Integration │              │ Meeting     │
               │ Form        │              │ Proposal    │              │             │
               └──────┬──────┘              └──────┬──────┘              └──────┬──────┘
                      │                            │                            │
                      ▼                            ▼                            ▼
               ┌─────────────┐              ┌─────────────┐              ┌─────────────┐
               │ Automated   │              │ Manual      │              │ Partner    │
               │ Review      │              │ Review      │              │ Manager     │
               │ Follow-up   │              │ Follow-up   │              │ Contact     │
               └─────────────┘              └─────────────┘              └─────────────┘
```

### 4. Career Application Flow

```
┌────────────────┐     ┌────────────────┐     ┌────────────────┐
│ Job seeker     │────>│ Visits Careers │────>│ Browses Open   │
│ lands on site  │     │ page           │     │ Positions      │
└────────────────┘     └────────────────┘     └────────────────┘
                                                      │
                      ┌─────────────────────────────┼─────────────────────────────┐
                      ▼                             ▼                             ▼
               ┌─────────────┐              ┌─────────────┐              ┌─────────────┐
               │ Filter by   │              │ View Job    │              │ Learn about │
               │ Category/   │              │ Details     │              │ Company    │
               │ Location    │              │             │              │ Culture    │
               └──────┬──────┘              └──────┬──────┘              └──────┬──────┘
                      │                            │                            │
                      ▼                            ▼                            ▼
               ┌─────────────┐              ┌─────────────┐              ┌─────────────┐
               │ Select      │              │ Upload      │              │ Submit      │
               │ Position    │              │ Resume/CV   │              │ Application │
               └──────┬──────┘              └──────┬──────┘              └──────┬──────┘
                      │                            │                            │
                      ▼                            ▼                            ▼
               ┌─────────────┐              ┌─────────────┐              ┌─────────────┐
               │ Application │              │ Auto        │              │ Recruiter   │
               │ Confirmation│              │ Confirmation│              │ Follow-up   │
               │ Email       │              │ Email       │              │             │
               └─────────────┘              └─────────────┘              └─────────────┘
```

---

## GLOBAL NAVIGATION PATTERNS

### Header Navigation

```
┌────────────────────────────────────────────────────────────────────────────────────┐
│  [Gogidix Logo]  Products ▼  Solutions ▼  Developers  Partners  Company ▼  [Login] │
│                                                                     [Get Started]  │
└────────────────────────────────────────────────────────────────────────────────────┘
```

**Behavior:**
- Sticky header on scroll (transforms to compact version)
- Mega menus for Products and Solutions
- Quick search accessible via Cmd/Ctrl + K
- Language selector (EN, FR, ES, PT, AR)
- Region selector for localized content

### Footer Navigation

```
┌────────────────────────────────────────────────────────────────────────────────────┐
│  Products    Solutions    Developers    Partners    Company    Resources           │
│  Logistics   By Industry  API Reference White-Label  About Us   Documentation      │
│  E-commerce  By Size      SDKs          Technology   Leadership Blog               │
│  Procurement By Region    Webhooks      System Int.  Careers    Webinars           │
│  Enterprise               Sandbox       Marketplace  Press      Security           │
│  Infrastructure           Status                     Contact                       │
│                                                                                    │
│  [Twitter] [LinkedIn] [GitHub] [YouTube] [Discord]                                │
│                                                                                    │
│  © 2025 Gogidix. Privacy Policy | Terms of Service | Cookie Settings             │
└────────────────────────────────────────────────────────────────────────────────────┘
```

---

## RESPONSIVE NAVIGATION

### Mobile Navigation (< 768px)

```
┌────────────────────────┐
│  [☰] Gogidix    [Lang] │
└────────────────────────┘
         │
         ▼ (Tap hamburger)
┌────────────────────────┐
│  ✕ Close               │
├────────────────────────┤
│  Home                  │
│  Products            ▶ │
│  Solutions           ▶ │
│  Developers          ▶ │
│  Partners            ▶ │
│  Company             ▶ │
│  Resources           ▶ │
│                        │
│  ─────────────────     │
│  Login                 │
│  Get Started           │
└────────────────────────┘
```

**Behavior:**
- Full-screen slide-in menu from right
- Expandable sub-menus with chevron indicators
- Swipe to close gesture
- Backdrop blur overlay

---

## MICROSITE NAVIGATION

### Product Landing Navigation

```
┌────────────────────────────────────────────────────────────────────────────────────┐
│  [← Back to Products]  Courier Management Cloud  [View Pricing] [Start Free Trial] │
├────────────────────────────────────────────────────────────────────────────────────┤
│  Overview    Features    Pricing    Documentation    Case Studies    Contact       │
└────────────────────────────────────────────────────────────────────────────────────┘
```

---

## CROSS-SITE NAVIGATION

### Breadcrumb Pattern

```
Home > Products > Logistics Platform > Courier Management > Features
```

### Related Products Navigation

```
┌────────────────────────────────────────────────────────────────────────────────────┐
│                                                                                    │
│  RELATED PRODUCTS                                                                  │
│  ─────────────────────────────────────────────────────────────────────────────    │
│                                                                                    │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐               │
│  │    [Icon]   │  │    [Icon]   │  │    [Icon]   │  │    [Icon]   │               │
│  │  Warehouse  │  │   Haulage   │  │  Air Freight│  │Fleet Track │               │
│  │ Management  │  │ Management  │  │             │  │             │               │
│  └─────────────┘  └─────────────┘  └─────────────┘  └─────────────┘               │
└────────────────────────────────────────────────────────────────────────────────────┘
```

---

## SEARCH FUNCTIONALITY

### Global Search (Cmd/Ctrl + K)

```
┌────────────────────────────────────────────────────────────────────────────────────┐
│  🔍 Search products, docs, APIs, resources...                [ESC]                 │
├────────────────────────────────────────────────────────────────────────────────────┤
│                                                                                    │
│  Products                              Solutions                                  │
│  ─────────────────────────────────────────────────────────────────────────────    │
│  • Courier Management Cloud         • Logistics Solutions                        │
│  • Warehouse Management Cloud       • E-commerce Solutions                       │
│  • Haulage Management Cloud         • Enterprise Solutions                       │
│                                                                                    │
│  API Reference                        Developers                                   │
│  ─────────────────────────────────────────────────────────────────────────────    │
│  • POST /v1/courier/create          • Quick Start Guide                           │
│  • GET /v1/warehouse/inventory      • SDKs                                       │
│                                                                                    │
│  [↑↓ Navigate] [↵ Open] [⌘K]                                           [Clear]    │
└────────────────────────────────────────────────────────────────────────────────────┘
```

---

## ACCESSIBILITY NAVIGATION

### Keyboard Navigation

| Key | Action |
|-----|--------|
| Tab | Navigate focusable elements |
| Shift + Tab | Reverse navigation |
| Enter/Space | Activate focused element |
| Escape | Close modals, menus, exit search |
| Arrow Keys | Navigate within menus, lists |
| Home/End | Jump to start/end of lists |
| Cmd/Ctrl + K | Open global search |
| Cmd/Ctrl + / | Open keyboard shortcuts help |

### Skip Links

```html
<a href="#main-content" class="sr-only focus:not-sr-only">
  Skip to main content
</a>
<a href="#navigation" class="sr-only focus:not-sr-only">
  Skip to navigation
</a>
```

---

## INTERNATIONALIZATION

### Supported Languages

| Code | Language | RTL | Status |
|------|----------|-----|--------|
| en | English | ✗ | ✓ Full |
| fr | French | ✗ | ✓ Full |
| es | Spanish | ✗ | ✓ Full |
| pt | Portuguese | ✗ | ✓ Full |
| ar | Arabic | ✓ | ✓ Full |

### Region-Specific Content

| Region | Currency | Date Format | Available Products |
|--------|----------|-------------|-------------------|
| NG (Nigeria) | NGN | DD/MM/YYYY | Full Suite |
| KE (Kenya) | KES | DD/MM/YYYY | Full Suite |
| GH (Ghana) | GHS | DD/MM/YYYY | Full Suite |
| ZA (South Africa) | ZAR | YYYY/MM/DD | Full Suite |
| US (United States) | USD | MM/DD/YYYY | Enterprise Only |

---

## VERSION HISTORY

| Version | Date | Changes |
|---------|------|---------|
| 1.0 | 2025-02-08 | Initial UI Flow Documentation |

---

**Document End**
