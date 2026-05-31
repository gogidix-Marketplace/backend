# GLOBAL BUSINESS DASHBOARD - PAGE BY PAGE

**Version:** 1.0
**Domain:** Management Domain
**Subdomain:** Global-business-management
**Last Updated:** 2026-02-23

---

## PAGE TREE

```
Global Business Web Dashboard
│
├── (Public)
│   ├── /login                     → LoginPage
│   └── /forgot-password           → ForgotPasswordPage
│
├── (Protected - Auth Required)
│   ├── /                          → OverviewPage
│   ├── /overview                  → (default)
│   ├── /regions                   → RegionsPage
│   │   ├── /regions               → Region List
│   │   └── /regions/{id}          → RegionDetailPage
│   ├── /partners                  → PartnersPage
│   │   ├── /partners             → Partner List
│   │   ├── /partners/{id}        → PartnerDetailPage
│   │   └── /partners/register    → RegisterPartnerPage
│   ├── /pipeline                  → PipelinePage
│   │   ├── /pipeline/opportunities → Opportunities
│   │   └── /pipeline/{id}        → OpportunityDetailPage
│   ├── /intelligence              → IntelligencePage
│   │   ├── /intelligence/market   → Market Analysis
│   │   └── /intelligence/competitor → Competitive Intel
│   └── /settings                  → SettingsPage
│       ├── /settings/preferences   → Preferences
│       └── /settings/notifications → Notification Settings
```

---

## ROUTE GUARDS

```
User navigates to route
      │
      ▼
Check route requirements
      │
      ├─ Public route ──────────────────────────────────────────────┐
      │                                                           ▼
      │                                              Render page
      │
      ├─ Protected route ──────────────┐
      │                                  ▼
      │                     Check authentication
      │                           │
      │                ┌──────────┴──────────┐
      │                │                     │
      │           Authenticated        Not authenticated
      │                │                     │
      │                ▼                     ▼
      │        ┌───────────────┐    ┌───────────────┐
      │        │ Check GBM      │    │ Redirect to   │
      │        │ role scope    │    │ /login        │
      │        └───────┬───────┘    └───────────────┘
      │                │
      │       ┌────────┴────────┐
      │        │                │
      │    Has Access    No Access
      │        │                │
      │        ▼                ▼
      │  ┌───────────┐   ┌───────────────┐
      │  │ Render    │   │ Redirect to   │
      │  │ page      │   │ /unauthorized │
      │  └───────────┘   └───────────────┘
      │
      └─ Error route ──────────┐
                                ▼
                        ┌───────────────┐
                        │ Render error   │
                        │ page (404)     │
                        └───────────────┘
```

---

## AUTHENTICATION FLOWS

### Login Page Flow

```
ROUTE: /login
Entry Points:
  • User navigates to /login
  • User redirected due to unauthenticated access

Initial State:
  ┌─────────────────────────────────────────────────────────────────────┐
  │  GBM PORTAL LOGIN                                                  │
  │  ┌─────────────────────────────────────────────────────────────┐   │
  │  │ 🌍 GOGIDIX GLOBAL BUSINESS PORTAL                           │   │
  │  │                                                             │   │
  │  │  Email: [user@gogidix.com]                                  │   │
  │  │  Password: [•••••••••••]                                     │   │
  │  │  ☐ Remember my device                                       │   │
  │  │  [Sign In]                                                 │   │
  │  │  OR                                                        │   │
  │  │  [SSO with Microsoft]                                       │   │
  │  └─────────────────────────────────────────────────────────────┘   │
  └─────────────────────────────────────────────────────────────────────┘

User Action: Click [Sign In]
      │
      ├─ VALID CREDENTIALS ───────────────────────────────────────────┐
      │                                                                  │
      │                     API: POST /api/v1/gbm/auth/login             │
      │                     Response: { token, user, regionAccess }       │
      │                                                                  │
      │                     Store token, Load region scope              │
      │                     Navigate to / (Overview)                    │
      │                                                                  │
      └─ INVALID CREDENTIALS ──────────────────────────────────────────┐
                                                                         │
                                                                         ▼
                                                        Show error message
                                                        Allow retry
```

---

## OVERVIEW PAGE FLOW

```
ROUTE: / (default: /overview)
Entry Points:
  • User successfully logs in
  • User clicks logo/brand
  • User clicks "Overview" in sidebar

Initial Load:
  API CALLS (Parallel):
  • GET /gbm/dashboard/overview
  • GET /gbm/regions/performance
  • GET /gbm/pipeline/summary
  • GET /gbm/partners/summary

Initial State:
  ┌─────────────────────────────────────────────────────────────────────┐
  │  GLOBAL BUSINESS OVERVIEW                                          │
  │  ┌─────────────────────────────────────────────────────────────┐   │
  │  │ GBM │ Regions │ Partners │ Pipeline │ Settings ⚙️             │   │
  │  └─────────────────────────────────────────────────────────────┘   │
  │                                                                   │
  │  ┌─────────────────────────────────────────────────────────────┐   │
  │  │ BUSINESS HEALTH SCORE                         [Full Report] │   │
  │  │  Score: 85/100  |  Trend: +5 from last quarter                  │   │
  │  └─────────────────────────────────────────────────────────────┘   │
  │                                                                   │
  │  ┌────────┐ ┌────────┐ ┌────────┐ ┌────────┐ ┌────────┐ ┌────────┐│  │
  │  │Regions │ │Active  │ │Pipeline│ │Partners│ │Deals   │ │Target  ││  │
  │  │   4    │ │Deals   │ │ Value  │ │        │ │Closed  │ │Achieve ││  │
  │  │ ●●●●●  │ │  45    │ │ $12.5M │ │  128   │ │   23   │ │  82%   ││  │
  │  └────────┘ └────────┘ └────────┘ └────────┘ └────────┘ └────────┘│  │
  │                                                                   │
  │  ┌─────────────────────────────────────────────────────────────┐   │
  │  │ REGIONAL PERFORMANCE                                          │   │
  │  │  [Region cards with revenue, growth, status]                   │   │
  │  └─────────────────────────────────────────────────────────────┘   │
  │                                                                   │
  │  ┌─────────────────────────────────────────────────────────────┐   │
  │  │ PIPELINE SNAPSHOT                                               │   │
  │  │  [Funnel visualization with stage counts and values]            │   │
  │  └─────────────────────────────────────────────────────────────┘   │
  │                                                                   │
  │  ┌─────────────────────────────────────────────────────────────┐   │
  │  │ PARTNER HIGHLIGHTS                                              │   │
  │  │  [Top partners, new registrations, performance metrics]         │   │
  │  └─────────────────────────────────────────────────────────────┘   │
  └─────────────────────────────────────────────────────────────────────┘

Page Interactions:

┌─────────────────────────────────────────────────────────────────────────┐
│ Interaction 1: Click Region Card                                    │
├─────────────────────────────────────────────────────────────────────────┤
│ User clicks Europe region card                                       │
│      ▼                                                                 │
│ Navigate to /regions/reg_europe                                       │
│ Load region detail page with country breakdown, partners, pipeline    │
└─────────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────────────┐
│ Interaction 2: View Pipeline                                        │
├─────────────────────────────────────────────────────────────────────────┤
│ User clicks pipeline snapshot                                        │
│      ▼                                                                 │
│ Navigate to /pipeline                                                 │
│ Load full pipeline with all opportunities and stage controls           │
└─────────────────────────────────────────────────────────────────────────┘
```

---

## REGIONS PAGE FLOWS

### Region List Flow

```
ROUTE: /regions
Entry Points:
  • User clicks "Regions" in sidebar

Initial State:
  ┌─────────────────────────────────────────────────────────────────────┐
  │  ALL REGIONS                                                       │
  │  Filters: [Status: All ▼] [Search regions...]                        │
  │                                                                   │
  │  ┌─────────────────────────────────────────────────────────────┐   │
  │  │  🇪🇺 EUROPE                           $8.2M    +15%    [View]  │   │
  │  │  7 countries | 45 partners | 92% target achievement        │   │
  │  ├─────────────────────────────────────────────────────────────┤   │
  │  │  🌍 AFRICA                           $3.5M    +22%    [View]  │   │
  │  │  5 countries | 32 partners | 78% target achievement        │   │
  │  ├─────────────────────────────────────────────────────────────┤   │
  │  │  🇺🇸 AMERICAS                        $4.1M    +8%     [View]  │   │
  │  │  4 countries | 28 partners | 88% target achievement        │   │
  │  ├─────────────────────────────────────────────────────────────┤   │
  │  │  🌏 ASIA PACIFIC                    $2.8M    +18%    [View]  │   │
  │  │  6 countries | 23 partners | 85% target achievement        │   │
  │  └─────────────────────────────────────────────────────────────┘   │
  └─────────────────────────────────────────────────────────────────────┘
```

### Region Detail Flow

```
ROUTE: /regions/{id}
Entry Points:
  • User clicks region from list
  • User navigates directly

Initial State:
  ┌─────────────────────────────────────────────────────────────────────┐
  │  🇪🇺 EUROPE REGION                                  [Export] [Settings]│
  │  [← Back to Regions]                                             │
  │                                                                   │
  │  ┌─────────────────────────────────────────────────────────────┐   │
  │  │ Revenue: $8.2M  |  Growth: +15%  |  Target: 92%               │   │
  │  │ Countries: 7  |  Partners: 45  |  Team: 23                     │   │
  │  └─────────────────────────────────────────────────────────────┘   │
  │                                                                   │
  │  ┌─────────────────────────────────────────────────────────────┐   │
  │  │ COUNTRY PERFORMANCE                    [+ New Opportunity]  │   │
  │  │  🇬🇧 UK    $3.2M ●●●●● 92%    🇮🇪 Ireland $2.1M ●●●●● 88%     │   │
  │  │  🇩🇪 Germany $1.5M ●●●●○ 78%    🇫🇷 France $0.8M ●●●○○ 65%     │   │
  │  └─────────────────────────────────────────────────────────────┘   │
  │                                                                   │
  │  ┌─────────────────────────────────────────────────────────────┐   │
  │  │ REGIONAL PIPELINE                                                │   │
  │  │  Prospecting: 8  |  Qualifying: 12  |  Proposal: 10            │   │
  │  │  Total Value: $3.5M                                          │   │
  │  └─────────────────────────────────────────────────────────────┘   │
  │                                                                   │
  │  ┌─────────────────────────────────────────────────────────────┐   │
  │  │ TOP PARTNERS IN REGION                                          │   │
  │  │  [Partner cards with performance metrics]                        │   │
  │  └─────────────────────────────────────────────────────────────┘   │
  └─────────────────────────────────────────────────────────────────────┘

Page Interactions:

┌─────────────────────────────────────────────────────────────────────────┐
│ Interaction 1: Add New Opportunity                                  │
├─────────────────────────────────────────────────────────────────────────┤
│ User clicks [+ New Opportunity]                                     │
│      ▼                                                                 │
│ Open Opportunity Creation Modal                                     │
│ • Form: Account, Type, Value, Estimated Close, Owner                 │
│ • Save → POST /gbm/pipeline/opportunities                           │
│ • Add to pipeline, show success toast                              │
└─────────────────────────────────────────────────────────────────────────┘
```

---

## PARTNERS PAGE FLOWS

```
ROUTE: /partners
Entry Points:
  • User clicks "Partners" in sidebar

Initial State:
  ┌─────────────────────────────────────────────────────────────────────┐
  │  PARTNER MANAGEMENT                                  [+ New Partner]│
  │  Filters: [Region: All ▼] [Type: All ▼] [Tier: All ▼]               │
  │                                                                   │
  │  ┌─────────────────────────────────────────────────────────────┐   │
  │  │ ACTIVE PARTNERS (112)                                         │   │
  │  │  🏢 TechVentures Global        Platinum €2.5M     [Details]   │   │
  │  │  🏢 African Business Partners    Gold     $1.2M     [Details]   │   │
  │  │  🏢 APAC Connections            Silver   $800K      [Details]   │   │
  │  │  [Load More Partners]                                           │   │
  │  └─────────────────────────────────────────────────────────────┘   │
  │                                                                   │
  │  ┌─────────────────────────────────────────────────────────────┐   │
  │  │ PARTNER PERFORMANCE SUMMARY                                     │   │
  │  │  • Total Partners: 128 (Active: 112, Onboarding: 16)            │   │
  │  │  • Revenue through Partners: $8.5M (Q1 2026)                    │   │
  │  │  • Top Region: Europe (45 partners, €2.5M)                       │   │
  │  └─────────────────────────────────────────────────────────────┘   │
  └─────────────────────────────────────────────────────────────────────┘

Page Interactions:

┌─────────────────────────────────────────────────────────────────────────┐
│ Interaction 1: Register New Partner                                 │
├─────────────────────────────────────────────────────────────────────────┤
│ User clicks [+ New Partner]                                          │
│      ▼                                                                 │
│ Open Partner Registration Wizard                                     │
│ Step 1: Basic Info (Name, Type, Region, Contact)                      │
│ Step 2: Tier & Agreement                                             │
│ Step 3: Onboarding Checklist                                        │
│      │                                                                 │
│ └─ Submit → POST /gbm/partners/register                              │
│             • Create partner record                                  │
│             • Initiate onboarding                                   │
│             • Notify partner team                                    │
└─────────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────────────┐
│ Interaction 2: View Partner Performance                              │
├─────────────────────────────────────────────────────────────────────────┤
│ User clicks [Details] on partner                                     │
│      ▼                                                                 │
│ Navigate to /partners/{id}                                           │
│ Load partner detail with performance history, deals, contacts         │
└─────────────────────────────────────────────────────────────────────────┘
```

---

## PIPELINE PAGE FLOWS

```
ROUTE: /pipeline
Entry Points:
  • User clicks "Business Development" in sidebar
  • User clicks "Pipeline" from overview

Initial State:
  ┌─────────────────────────────────────────────────────────────────────┐
  │  DEAL PIPELINE                                                      │
  │  ┌─────────────────────────────────────────────────────────────┐   │
  │  │ PIPELINE OVERVIEW                                  [+ New Deal]│   │
  │  │  ┌────────┐ ┌────────┐ ┌────────┐ ┌────────┐ ┌────────┐  │   │
  │  │  │Prospect│ │Qualify │ │Proposal│ │Negotiat│ │Closing │  │   │
  │  │  │   12   │ │   18   │ │   15   │ │    8   │ │    5   │  │   │
  │  │  │ $1.2M   │ │ $3.5M   │ │ $4.2M   │ │ $2.8M   │ │ $0.8M   │  │   │
  │  │  └────────┘ └────────┘ └────────┘ └────────┘ └────────┘  │   │
  │  │  Total Pipeline Value: $12.5M                                 │   │
  │  └─────────────────────────────────────────────────────────────┘   │
  └─────────────────────────────────────────────────────────────────────┘

Page Interactions:

┌─────────────────────────────────────────────────────────────────────────┐
│ Interaction 1: Update Deal Stage                                    │
├─────────────────────────────────────────────────────────────────────────┤
│ User drags deal from Qualifying to Proposal                          │
│      ▼                                                                 │
│ PUT /gbm/pipeline/opportunities/{id}/stage                           │
│ • Update stage                                                      │
│ • Update probability                                                │
│ • Log activity                                                    │
│ • Refresh pipeline view                                           │
└─────────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────────────┐
│ Interaction 2: Create New Deal                                       │
├─────────────────────────────────────────────────────────────────────────┤
│ User clicks [+ New Deal]                                             │
│      ▼                                                                 │
│ Open Deal Creation Form                                              │
│ • Account, Type, Value, Region, Owner                                │
│ • Save → POST /gbm/pipeline/opportunities                            │
│ • Add to pipeline                                                   │
└─────────────────────────────────────────────────────────────────────────┘
```

---

## NAVIGATION REFERENCE

| From | To | Method |
|------|-----|--------|
| Anywhere | Login | Redirect (unauthenticated) |
| Login | Overview | Auth success |
| Overview | Regions | Click sidebar |
| Overview | Partners | Click sidebar |
| Overview | Pipeline | Click sidebar |
| Regions | Region Detail | Click region card |
| Partners | Partner Detail | Click partner |
| Pipeline | Opportunity Detail | Click opportunity |
| Detail | Back | Back button |

---

## PERMISSION MATRIX

### Role-Based Access

| Permission | GLOBAL_DIR | REGIONAL_DIR | BDM | PARTNER_MGR | ANALYST |
|------------|------------|--------------|-----|-------------|---------|
| View All Regions | ✓ | Assigned only | ✓ | ✓ | ✓ |
| View Region Details | ✓ | Assigned only | ✓ | ✓ | ✓ |
| Create Opportunities | ✓ | ✓ | ✓ | ✗ | ✗ |
| Edit Any Opportunity | ✓ | Region only | Own only | ✗ | ✗ |
| Register Partners | ✓ | ✓ | ✓ | ✓ | ✗ |
| View Partner Performance | ✓ | ✓ | ✓ | ✓ | ✓ |
| Export Reports | ✓ | ✓ | ✓ | ✗ | ✓ |
| View Market Intelligence | ✓ | ✓ | ✓ | ✗ | ✓ |

---

**Document End**
