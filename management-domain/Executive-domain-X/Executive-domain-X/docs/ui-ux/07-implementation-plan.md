# Gogidix Executive Domain - Implementation Plan

**Version:** 1.0
**Last Updated:** 2026-04-22
**Status:** Active
**Ports:** CEO=3011 | CFO=3012 | COO=3013 | CTO=3014

---

## Table of Contents

1. [Architecture Decision](#1-architecture-decision)
2. [Current State Assessment](#2-current-state-assessment)
3. [Port Configuration](#3-port-configuration)
4. [Phase 0: Project Setup & Fixes](#4-phase-0-project-setup--fixes)
5. [Phase 1: Type System & Mock Data Foundation](#5-phase-1-type-system--mock-data-foundation)
6. [Phase 2: Shared Components Library](#6-phase-2-shared-components-library)
7. [Phase 3: CEO Dashboard Enhancement](#7-phase-3-ceo-dashboard-enhancement)
8. [Phase 4: CFO Dashboard Implementation](#8-phase-4-cfo-dashboard-implementation)
9. [Phase 5: COO Dashboard Implementation](#9-phase-5-coo-dashboard-implementation)
10. [Phase 6: CTO Dashboard Implementation](#10-phase-6-cto-dashboard-implementation)
11. [Phase 7: Integration & Polish](#11-phase-7-integration--polish)
12. [File Tree Target State](#12-file-tree-target-state)
13. [Execution Order & Dependencies](#13-execution-order--dependencies)
14. [Testing Strategy](#14-testing-strategy)

---

## 1. Architecture Decision

### Decision: Single Codebase, Multi-Role App

The CEO dashboard (`ceo-web-dashboard/`) already contains a **multi-role router** that detects the current role based on the port number. This eliminates code duplication across 4 separate projects.

**Implementation approach:**
- **One source code** in `ceo-web-dashboard/` (canonical implementation)
- **4 dev server scripts** in `package.json` — each starts on the designated port
- **Port detection** in routing determines which role's UI to render
- **Role-aware components** receive `role` prop and adapt accordingly

### Why not 4 separate apps?

| Factor | Single Codebase | 4 Separate Apps |
|---|---|---|
| Code duplication | Zero | Massive (layouts, auth, UI components) |
| Shared components | Naturally shared | Must copy or use monorepo |
| Maintenance | One place | 4 places |
| Bug fixes | Fix once | Fix 4 times |
| Build process | One build | 4 builds |
| Test coverage | One test suite | 4 test suites |

The other 3 dashboard directories (`cfo-web-dashboard/`, `coo-web-dashboard/`, `cto-web-dashboard/`) will be **archived** with a README pointing to the canonical implementation.

### Tech Stack (from existing CEO dashboard)

| Technology | Version | Purpose |
|---|---|---|
| React | 18.3 | UI Framework |
| TypeScript | 5.4 | Type Safety |
| Vite | 5.1 | Build Tool |
| Zustand | 4.5 | State Management |
| TanStack Query | 5.28 | Data Fetching |
| React Router | 6.22 | Routing |
| Recharts | 2.12 | Charts |
| Radix UI | Latest | Component Primitives |
| Framer Motion | 12.38 | Animations |
| Lucide React | 0.344 | Icons |
| Axios | 1.6 | HTTP Client |
| Tailwind Merge | 3.5 | Class merging |

**Note:** The CEO dashboard uses CSS modules and inline styles (no Tailwind CSS config). We will add Tailwind CSS v3 for utility classes during Phase 0.

---

## 2. Current State Assessment

### CEO Dashboard (Port 3011) — 70% Complete

**Working:**
- Multi-role routing with port detection
- Role-aware sidebar with business domain navigation
- Role-themed header with gradient backgrounds
- Authentication (login, forgot-password, 2FA)
- Shared UI component library (16 components)
- WebSocket infrastructure
- API service layer (aggregation, AI/BI, executive)
- All core pages: overview, strategy, analytics, approvals, reports, settings

**Missing:**
- Departments page (`/departments`)
- Business Units page (`/business-units`)
- Department detail pages
- Business unit detail pages
- Business unit revenue components
- Department integration components
- Tailwind CSS setup
- Role-specific page content for CFO/COO/CTO
- Cross-department approval dashboard
- Business unit service health grid

### CFO/COO/CTO Dashboards — BROKEN/COPIES

| Dashboard | Port | State | Action |
|---|---|---|---|
| CFO | 3014 (wrong) | Copy-pasted from CEO, wrong branding, wrong port | Archive |
| COO | 3012 (wrong) | Skeleton — cannot build | Archive |
| CTO | 3013 (wrong) | Copy-pasted from CEO, wrong branding, wrong port | Archive |

All 3 will be archived. Their functionality is handled by the CEO dashboard's multi-role router.

---

## 3. Port Configuration

### Target Port Assignments

| Role | Port | Dev Script | Route Prefix |
|---|---|---|---|
| **CEO** | 3011 | `npm run dev:ceo` | `/` |
| **CFO** | 3012 | `npm run dev:cfo` | `/cfo` |
| **COO** | 3013 | `npm run dev:coo` | `/coo` |
| **CTO** | 3014 | `npm run dev:cto` | `/cto` |
| **All** | — | `npm run dev:all` | Starts all 4 concurrently |

### package.json Script Updates

```json
{
  "scripts": {
    "dev": "vite --port 3011",
    "dev:ceo": "vite --port 3011",
    "dev:cfo": "vite --port 3012",
    "dev:coo": "vite --port 3013",
    "dev:cto": "vite --port 3014",
    "dev:all": "concurrently \"npm run dev:ceo\" \"npm run dev:cfo\" \"npm run dev:coo\" \"npm run dev:cto\"",
    "build": "tsc && vite build",
    "build:ceo": "VITE_ROLE=CEO tsc && vite build --outDir dist/ceo",
    "build:cfo": "VITE_ROLE=CFO tsc && vite build --outDir dist/cfo",
    "build:coo": "VITE_ROLE=COO tsc && vite build --outDir dist/coo",
    "build:cto": "VITE_ROLE=CTO tsc && vite build --outDir dist/cto",
    "preview": "vite preview",
    "lint": "eslint .",
    "test": "vitest",
    "typecheck": "tsc --noEmit"
  }
}
```

### New Dependencies to Add

```json
{
  "devDependencies": {
    "tailwindcss": "^3.5.0",
    "postcss": "^8.4.35",
    "autoprefixer": "^10.4.17",
    "concurrently": "^8.2.2"
  }
}
```

---

## 4. Phase 0: Project Setup & Fixes

### Estimated Time: 1 hour

### Tasks

#### 0.1 Add Tailwind CSS
- Create `tailwind.config.js` with executive color palette from design system
- Create `postcss.config.js`
- Update `src/shared/styles/globals.css` with Tailwind directives
- Verify existing classes still work

#### 0.2 Fix Port Detection in Routing
- File: `src/presentation/routing/index.tsx`
- Current: Already correct (3011=CEO, 3012=CFO, 3013=COO, 3014=CTO)
- No changes needed — port detection already matches target ports

#### 0.3 Update package.json
- Add `dev:ceo`, `dev:cfo`, `dev:coo`, `dev:cto`, `dev:all` scripts
- Add `tailwindcss`, `postcss`, `autoprefixer`, `concurrently` dev dependencies
- Run `npm install`

#### 0.4 Update vite.config.ts
- Ensure port defaults to 3011
- Add `VITE_ROLE` env variable support for builds

#### 0.5 Archive Other Dashboard Projects
- Add `README.md` to each of `cfo-web-dashboard/`, `coo-web-dashboard/`, `cto-web-dashboard/`
- State: "This project has been consolidated into `../ceo-web-dashboard/` which serves all 4 roles via port-based routing. Run `npm run dev:cfo` (or coo/cto) from the CEO dashboard."

#### 0.6 Verify Existing Functionality
- Run `npm run dev:ceo` on port 3011 — verify CEO dashboard loads
- Run `npm run dev:cfo` on port 3012 — verify CFO routes work
- Run `npm run dev:coo` on port 3013 — verify COO routes work
- Run `npm run dev:cto` on port 3014 — verify CTO routes work

---

## 5. Phase 1: Type System & Mock Data Foundation

### Estimated Time: 2 hours

### Tasks

#### 1.1 Extend Type System

**File:** `src/shared/types/index.ts`

Add these new types:

```typescript
// Business Unit Types
export type BusinessUnitSlug =
  | 'courier' | 'ecommerce' | 'warehousing' | 'air-freight'
  | 'ocean-shipping' | 'haulage' | 'procurement' | 'admin-core'

export interface BusinessUnit {
  id: BusinessUnitSlug
  name: string
  icon: string
  color: string
  serviceCount: number
  healthyServices: number
  revenue: number
  revenueGrowth: number
  revenueModel: string
  primaryRegion: string
}

export interface BusinessUnitRevenue {
  unitId: BusinessUnitSlug
  revenue: number
  growth: number
  margin: number
  opex: number
  netProfit: number
  currency: string
  period: string
}

// Department Types (enhanced)
export interface DepartmentHealth {
  departmentId: string
  name: string
  icon: string
  color: string
  healthScore: number
  serviceCount: number
  healthyServices: number
  alertCount: number
  criticalCount: number
  pendingApprovals: number
}

export interface DepartmentBudget {
  departmentId: string
  allocated: number
  spent: number
  remaining: number
  percentUsed: number
  currency: string
}

// Approval Types (enhanced)
export interface CrossDepartmentApproval extends Approval {
  sourceDepartment: string
  targetRole: UserRole
  approvalChain: ApprovalChainStep[]
  justification: string
  attachments: Attachment[]
}

export interface ApprovalChainStep {
  role: UserRole
  status: 'pending' | 'approved' | 'rejected' | 'awaiting'
  user?: string
  timestamp?: string
  comment?: string
}

export interface Attachment {
  id: string
  name: string
  type: string
  size: number
  url: string
}

// Service Health Types
export interface ServiceHealth {
  name: string
  port: number
  status: 'healthy' | 'degraded' | 'offline'
  uptime: number
  responseTime: number
  cpu: number
  memory: number
  region: string
  businessUnit?: BusinessUnitSlug
  department?: string
}

// WebSocket Message Types
export interface WSMessage<T = unknown> {
  type: string
  channel: string
  payload: T
  timestamp: string
}

// Dashboard Filter Types
export interface DashboardFilters {
  period: 'today' | 'week' | 'month' | 'quarter' | 'year'
  currency: 'USD' | 'EUR' | 'GBP' | 'NGN'
  businessUnit?: BusinessUnitSlug | 'all'
  department?: string | 'all'
  region?: string | 'all'
}
```

#### 1.2 Create Mock Data Files

Create these files with comprehensive mock data:

| File | Content |
|---|---|
| `src/shared/data/business-units.ts` | 8 business unit objects with revenue, growth, services |
| `src/shared/data/departments.ts` | 8 department health objects with metrics |
| `src/shared/data/service-health.ts` | 272 service health entries across 8 units |
| `src/shared/data/approvals.ts` | 25+ cross-department approval objects |
| `src/shared/data/revenue.ts` | Revenue data by unit, region, period (12 months) |
| `src/shared/data/financials.ts` | P&L data, budget vs actual, settlement pipeline |
| `src/shared/data/operations.ts` | Operational KPIs, incidents, SLA compliance |
| `src/shared/data/technology.ts` | Infrastructure status, deployment pipeline, security |

Each mock data file should export typed arrays and objects that match the wireframes exactly.

---

## 6. Phase 2: Shared Components Library

### Estimated Time: 3 hours

### Tasks

Create new shared components in `src/shared/components/`:

#### 2.1 Business Unit Components

| Component File | Props | Description |
|---|---|---|
| `business-unit/BusinessUnitBadge.tsx` | `unit: BusinessUnitSlug, size?: 'sm' \| 'md'` | Colored badge with icon + name |
| `business-unit/BusinessUnitSelector.tsx` | `value, onChange, includeAll?` | Dropdown filter for business units |
| `business-unit/BusinessUnitRevenueGrid.tsx` | `period, currency` | 8-card grid showing revenue per unit |
| `business-unit/BusinessUnitRevenueChart.tsx` | `period, sortBy` | Horizontal bar chart comparing unit revenue |
| `business-unit/BusinessUnitTrendSparklines.tsx` | `period, unitIds?` | Sparkline rows for each unit |
| `business-unit/BusinessUnitServiceHealthGrid.tsx` | `unitId?` | 272-service dot grid (filterable by unit) |
| `business-unit/BusinessUnitInfraSummary.tsx` | `unitId: BusinessUnitSlug` | Infrastructure summary card for one unit |

#### 2.2 Department Integration Components

| Component File | Props | Description |
|---|---|---|
| `department/DepartmentBadge.tsx` | `dept: string, size?` | Department badge with color |
| `department/DepartmentHealthGrid.tsx` | `role` | 8-card grid showing department health |
| `department/DepartmentBudgetComparison.tsx` | `period, currency` | Budget utilization bars for all depts |
| `department/DepartmentReportCard.tsx` | `deptId, period` | Full report card for one department |

#### 2.3 Approval Components

| Component File | Props | Description |
|---|---|---|
| `approval/CrossDepartmentApprovalQueue.tsx` | `role, filter?` | Unified approval inbox with dept badges |
| `approval/ApprovalDetailModal.tsx` | `approval, open, onClose, onAction` | Full approval detail with chain, attachments |
| `approval/ApprovalConfirmationToast.tsx` | `approval, action, visible, onUndo` | Success/error toast with undo |

#### 2.4 Enhanced KPI Components

| Component File | Props | Description |
|---|---|---|
| `kpi/RoleAwareKPIGrid.tsx` | `role, period` | Shows role-specific KPI cards |
| `kpi/BusinessUnitKPIBreakdown.tsx` | `unitId, role` | KPI cards filtered to one business unit |
| `kpi/DepartmentKPIBreakdown.tsx` | `deptId, role` | KPI cards filtered to one department |

#### 2.5 Navigation Components

| Component File | Props | Description |
|---|---|---|
| `navigation/BusinessDomainNav.tsx` | (upgrade existing sidebar section) | Expand business domains section to include all 8 units |
| `navigation/DepartmentNav.tsx` | `role` | Department navigation section in sidebar |
| `navigation/DashboardSwitcher.tsx` | `currentRole` | Role switcher buttons (already partially exists) |

---

## 7. Phase 3: CEO Dashboard Enhancement

### Estimated Time: 3 hours

### Tasks

#### 3.1 Add New Routes

**File:** `src/presentation/routing/index.tsx`

Add to CEO route tree:
```
/departments                           → DepartmentsOverviewPage
/departments/:deptId                   → DepartmentDetailPage
/departments/:deptId/budget            → DepartmentBudgetPage
/departments/:deptId/approvals         → DepartmentApprovalsPage
/business-units                        → BusinessUnitsPage
/business-units/:unitId                → BusinessUnitDetailPage
/business-units/:unitId/revenue        → BusinessUnitRevenuePage
/business-units/:unitId/operations     → BusinessUnitOperationsPage
/business-units/:unitId/services       → BusinessUnitServicesPage
/business-units/comparison             → BusinessUnitComparisonPage
```

#### 3.2 Update Sidebar Navigation

**File:** `src/shared/components/layout/sidebar.tsx`

Add to CEO nav items:
- `Departments` → `/departments` (icon: Building2)
- `Business Units` → `/business-units` (icon: Briefcase)

Keep existing "Business Domains" collapsible section for quick domain access.

#### 3.3 Create CEO-Specific Pages

| Page File | Route | Content |
|---|---|---|
| `src/pages/departments.tsx` | `/departments` | 8 department health cards |
| `src/pages/department-detail.tsx` | `/departments/:deptId` | Department tabs: Overview, KPIs, Budget, Reports, Approvals, Services |
| `src/pages/business-units.tsx` | `/business-units` | 8 business unit revenue cards |
| `src/pages/business-unit-detail.tsx` | `/business-units/:unitId` | Unit tabs: Overview, Revenue, Operations, Services, Reports |
| `src/pages/business-unit-comparison.tsx` | `/business-units/comparison` | Cross-unit ranking, metrics comparison, trend chart |

#### 3.4 Enhance Existing CEO Overview Page

**File:** `src/pages/overview.tsx`

Add below existing content:
- Business Unit Revenue Summary row (8 mini cards)
- Department Health Summary row (8 mini cards)
- Cross-unit revenue trend chart

---

## 8. Phase 4: CFO Dashboard Implementation

### Estimated Time: 4 hours

### Tasks

#### 4.1 Update CFO Routes

**File:** `src/presentation/routing/index.tsx`

Add to CFO route tree:
```
/cfo/departments                       → DepartmentsOverviewPage (CFO view)
/cfo/departments/:deptId               → DepartmentDetailPage (CFO: budget focus)
/cfo/business-units                    → BusinessUnitsPage (CFO: revenue focus)
/cfo/business-units/:unitId            → BusinessUnitDetailPage (CFO: P&L view)
/cfo/business-units/:unitId/revenue    → BusinessUnitRevenuePage
/cfo/business-units/financial-summary  → FinancialSummaryPage
/cfo/approvals                         → CrossDeptApprovalPage (CFO role)
```

#### 4.2 Update Sidebar for CFO

**File:** `src/shared/components/layout/dashboard-layout.tsx`

Add to CFO nav items:
- `Departments` → `/cfo/departments`
- `Business Units` → `/cfo/business-units`

#### 4.3 Create CFO-Specific Pages

The existing CFO feature pages in `src/features/cfo/pages/` need complete rewrites.

| Page File | Route | Content |
|---|---|---|
| `src/features/cfo/pages/overview.tsx` | `/cfo` | Financial Overview KPIs, P&L by unit, settlement pipeline, pending approvals |
| `src/features/cfo/pages/budget.tsx` | `/cfo/budget` | Department budget vs actual (8 depts), drill-down per dept |
| `src/features/cfo/pages/financials.tsx` | `/cfo/financials` | Revenue/expense tracking chart, multi-region financials, currency exposure |
| `src/features/cfo/pages/forecast.tsx` | `/cfo/forecast` | Revenue forecast with confidence bands, adjustable assumptions |
| `src/features/cfo/pages/compliance.tsx` | `/cfo/compliance` | Tax status, audit trail, regulatory compliance grid |
| `src/features/cfo/pages/financial-summary.tsx` | `/cfo/business-units/financial-summary` | Cross-unit P&L table, revenue mix, settlements, commissions |

#### 4.4 CFO Dashboard Switcher

Remove the hardcoded role switcher from CEO overview. Add it to the header component as a dropdown.

---

## 9. Phase 5: COO Dashboard Implementation

### Estimated Time: 4 hours

### Tasks

#### 5.1 Update COO Routes

**File:** `src/presentation/routing/index.tsx`

Add to COO route tree:
```
/coo/departments                       → DepartmentsOverviewPage (COO view)
/coo/departments/:deptId               → DepartmentDetailPage (COO: operations focus)
/coo/business-units                    → BusinessUnitsPage (COO: operations focus)
/coo/business-units/:unitId            → BusinessUnitDetailPage (COO: operations view)
/coo/business-units/operations-overview → OperationsOverviewPage
/coo/approvals                         → CrossDeptApprovalPage (COO role)
```

#### 5.2 Update Sidebar for COO

Add to COO nav items:
- `Departments` → `/coo/departments`
- `Business Units` → `/coo/business-units`

#### 5.3 Create COO-Specific Pages

The existing COO feature pages in `src/features/coo/pages/` need complete rewrites.

| Page File | Route | Content |
|---|---|---|
| `src/features/coo/pages/overview.tsx` | `/coo` | Operational health score, 8-unit operations grid, live map, active incidents, volume snapshot |
| `src/features/coo/pages/operations.tsx` | `/coo/operations` | Performance benchmarking, resource allocation, AI process insights |
| `src/features/coo/pages/incidents.tsx` | `/coo/incidents` | Incident list with severity filters, detail modal, timeline, reassign |
| `src/features/coo/pages/resources.tsx` | `/coo/resources` | Department utilization bars, optimization suggestions, capacity planning |
| `src/features/coo/pages/operations-overview.tsx` | `/coo/business-units/operations-overview` | Cross-unit operations map, SLA compliance gauges, capacity, incidents |

---

## 10. Phase 6: CTO Dashboard Implementation

### Estimated Time: 4 hours

### Tasks

#### 6.1 Update CTO Routes

**File:** `src/presentation/routing/index.tsx`

Add to CTO route tree:
```
/cto/departments                       → DepartmentsOverviewPage (CTO view)
/cto/departments/:deptId               → DepartmentDetailPage (CTO: technology focus)
/cto/business-units                    → BusinessUnitsPage (CTO: service health focus)
/cto/business-units/:unitId            → BusinessUnitDetailPage (CTO: services view)
/cto/business-units/service-health     → FullServiceHealthPage
/cto/business-units/infrastructure     → InfrastructureSummaryPage
/cto/approvals                         → CrossDeptApprovalPage (CTO role)
```

#### 6.2 Update Sidebar for CTO

Add to CTO nav items:
- `Departments` → `/cto/departments`
- `Business Units` → `/cto/business-units`

#### 6.3 Create CTO-Specific Pages

The existing CTO feature pages in `src/features/cto/pages/` need complete rewrites.

| Page File | Route | Content |
|---|---|---|
| `src/features/cto/pages/overview.tsx` | `/cto` | Technology health score, 8-unit service grid, deployment pipeline, pending approvals |
| `src/features/cto/pages/infrastructure.tsx` | `/cto/infrastructure` | Infrastructure health (servers, DBs, APIs, networks), service status grid, filter/search |
| `src/features/cto/pages/engineering.tsx` | `/cto/engineering` | Build success rate, deploy frequency, lead time, PR count, test coverage |
| `src/features/cto/pages/security.tsx` | `/cto/security` | Compliance status (GDPR, SOC2, ISO, OWASP), security posture by dept |
| `src/features/cto/pages/roadmap.tsx` | `/cto/roadmap` | Innovation roadmap (quarterly view), initiative cards, progress tracking |
| `src/features/cto/pages/users.tsx` | `/cto/users` | User management, access levels, session tracking |
| `src/features/cto/pages/service-health.tsx` | `/cto/business-units/service-health` | Full 272-service health grid with filtering |
| `src/features/cto/pages/infrastructure-summary.tsx` | `/cto/business-units/infrastructure` | Infrastructure components (Kafka, MongoDB, PostgreSQL, Redis, RabbitMQ, ES) |

---

## 11. Phase 7: Integration & Polish

### Estimated Time: 2 hours

### Tasks

#### 7.1 Cross-Department Approval Dashboard
- Create unified approval page accessible by all roles
- Filter by department, business unit, priority, status
- Support bulk approve/reject/delegate

#### 7.2 Role Switching Enhancement
- Add role switcher to header (not just overview page)
- Remember last role via localStorage
- Smooth transition between roles (same codebase, different port)

#### 7.3 Mobile Responsive
- Ensure all new pages work on mobile (< 900px)
- Bottom navigation for core pages
- Collapsible sidebar

#### 7.4 Loading States
- Skeleton loading for all data-dependent components
- Shimmer animation for KPI cards
- Skeleton charts for loading states

#### 7.5 Error Handling
- Error boundaries per page
- Retry buttons for failed API calls
- Graceful fallback when mock data service is unavailable

---

## 12. File Tree Target State

```
ceo-web-dashboard/
├── package.json                         (updated with 4 dev scripts + tailwind)
├── vite.config.ts                       (port 3011 default)
├── tailwind.config.js                   (NEW - executive color palette)
├── postcss.config.js                    (NEW)
├── tsconfig.json                        (updated with new paths)
├── index.html
│
├── src/
│   ├── main.tsx                         (updated - add Tailwind imports)
│   │
│   ├── presentation/
│   │   ├── App.tsx
│   │   ├── routing/
│   │   │   └── index.tsx                (updated - all routes for all 4 roles)
│   │   └── styles/
│   │       └── global/index.css         (updated - add Tailwind directives)
│   │
│   ├── pages/
│   │   ├── index.ts                     (updated exports)
│   │   ├── overview.tsx                 (enhanced with dept + BU summaries)
│   │   ├── strategy.tsx                 (CEO)
│   │   ├── analytics.tsx                (CEO)
│   │   ├── approvals.tsx                (enhanced - cross-dept)
│   │   ├── reports.tsx                  (enhanced - dept + BU reports)
│   │   ├── settings.tsx                 (all roles)
│   │   ├── login.tsx
│   │   ├── forgot-password.tsx
│   │   ├── two-factor-auth.tsx
│   │   ├── departments.tsx              (NEW - dept overview)
│   │   ├── department-detail.tsx        (NEW - dept drill-down)
│   │   ├── business-units.tsx           (NEW - BU overview)
│   │   ├── business-unit-detail.tsx     (NEW - BU drill-down)
│   │   ├── business-unit-comparison.tsx (NEW - cross-BU comparison)
│   │   └── domain-overview-page.tsx     (existing - legacy domain view)
│   │
│   ├── features/
│   │   ├── auth/                        (existing - login, 2FA, onboarding)
│   │   ├── ceo/
│   │   │   ├── components/             (existing - 8 components)
│   │   │   └── pages/                  (existing - CEO-specific feature pages)
│   │   ├── cfo/
│   │   │   └── pages/
│   │   │       ├── overview.tsx        (REWRITE - P&L, settlements, commission)
│   │   │       ├── budget.tsx          (REWRITE - dept budget vs actual)
│   │   │       ├── financials.tsx      (REWRITE - revenue/expense tracking)
│   │   │       ├── forecast.tsx        (REWRITE - projections with confidence)
│   │   │       ├── compliance.tsx      (REWRITE - tax, audit, regulatory)
│   │   │       ├── reports.tsx         (REWRITE - financial reports)
│   │   │       └── financial-summary.tsx (NEW - cross-unit P&L)
│   │   ├── coo/
│   │   │   └── pages/
│   │   │       ├── overview.tsx        (REWRITE - health score, 8-unit ops)
│   │   │       ├── operations.tsx      (REWRITE - performance, resources)
│   │   │       ├── incidents.tsx       (REWRITE - incident management)
│   │   │       ├── resources.tsx       (REWRITE - utilization, capacity)
│   │   │       ├── reports.tsx         (REWRITE - operational reports)
│   │   │       ├── approvals.tsx       (REWRITE - COO approvals)
│   │   │       └── operations-overview.tsx (NEW - cross-unit ops)
│   │   ├── cto/
│   │   │   └── pages/
│   │   │       ├── overview.tsx        (REWRITE - tech health, 8-unit grid)
│   │   │       ├── infrastructure.tsx  (REWRITE - servers, DBs, APIs)
│   │   │       ├── engineering.tsx     (REWRITE - pipeline metrics)
│   │   │       ├── security.tsx        (REWRITE - compliance posture)
│   │   │       ├── roadmap.tsx         (REWRITE - innovation initiatives)
│   │   │       ├── users.tsx           (REWRITE - user management)
│   │   │       ├── reports.tsx         (REWRITE - tech reports)
│   │   │       ├── approvals.tsx       (REWRITE - CTO approvals)
│   │   │       ├── service-health.tsx  (NEW - 272-service grid)
│   │   │       └── infrastructure-summary.tsx (NEW - infra components)
│   │   └── dashboard/                  (existing - placeholder)
│   │
│   ├── shared/
│   │   ├── components/
│   │   │   ├── ui/                     (existing - 16 base components)
│   │   │   ├── layout/
│   │   │   │   ├── sidebar.tsx         (updated - add Depts + BU nav items)
│   │   │   │   ├── header.tsx          (updated - add role switcher)
│   │   │   │   ├── dashboard-layout.tsx (updated - new nav items per role)
│   │   │   │   └── index.ts
│   │   │   ├── business-unit/          (NEW - 7 components)
│   │   │   ├── department/             (NEW - 4 components)
│   │   │   ├── approval/               (NEW - 3 components)
│   │   │   ├── kpi/                    (NEW - 3 enhanced components)
│   │   │   └── navigation/             (NEW - 3 components)
│   │   │
│   │   ├── data/                       (NEW - mock data)
│   │   │   ├── business-units.ts
│   │   │   ├── departments.ts
│   │   │   ├── service-health.ts
│   │   │   ├── approvals.ts
│   │   │   ├── revenue.ts
│   │   │   ├── financials.ts
│   │   │   ├── operations.ts
│   │   │   └── technology.ts
│   │   │
│   │   ├── services/api/               (existing - API client)
│   │   ├── stores/
│   │   │   └── authStore.ts            (updated - all 4 role users)
│   │   ├── types/
│   │   │   └── index.ts                (updated - new types)
│   │   ├── utils/
│   │   │   └── cn.ts
│   │   ├── styles/
│   │   │   └── globals.css             (updated - Tailwind directives)
│   │   └── index.ts
│   │
│   ├── infrastructure/
│   │   ├── auth/                       (existing)
│   │   └── websocket/                  (existing)
│   │
│   └── application/ports/out/          (existing)
│
├── cfo-web-dashboard/                   (ARCHIVED - README only)
├── coo-web-dashboard/                   (ARCHIVED - README only)
└── cto-web-dashboard/                   (ARCHIVED - README only)
```

---

## 13. Execution Order & Dependencies

```
Phase 0: Setup & Fixes
   │
   ├── 0.1 Add Tailwind CSS ─────────────────┐
   ├── 0.2 Verify port detection             │
   ├── 0.3 Update package.json scripts       ├──→ Verify all 4 ports work
   ├── 0.4 Update vite.config.ts             │
   ├── 0.5 Archive old dashboards ───────────┘
   └── 0.6 Smoke test
         │
         ▼
Phase 1: Types & Mock Data
   │
   ├── 1.1 Extend type system ───────────────┐
   └── 1.2 Create mock data files (8 files)──┘
         │
         ▼
Phase 2: Shared Components
   │
   ├── 2.1 Business unit components (7) ─────┐
   ├── 2.2 Department components (4)          │
   ├── 2.3 Approval components (3)            ├──→ All components ready
   ├── 2.4 Enhanced KPI components (3)        │
   └── 2.5 Navigation components (3) ─────────┘
         │
         ▼
Phase 3: CEO Enhancement ← depends on Phase 2
   │
   ├── 3.1 Add new CEO routes
   ├── 3.2 Update CEO sidebar
   ├── 3.3 Create 5 new CEO pages
   └── 3.4 Enhance CEO overview
         │
         ▼
Phase 4: CFO Implementation ← depends on Phase 2
   │
   ├── 4.1 Add CFO routes
   ├── 4.2 Update CFO sidebar
   ├── 4.3 Rewrite 6 CFO pages
   └── 4.4 Create financial summary page
         │
         ▼
Phase 5: COO Implementation ← depends on Phase 2
   │
   ├── 5.1 Add COO routes
   ├── 5.2 Update COO sidebar
   ├── 5.3 Rewrite 6 COO pages
   └── 5.4 Create operations overview page
         │
         ▼
Phase 6: CTO Implementation ← depends on Phase 2
   │
   ├── 6.1 Add CTO routes
   ├── 6.2 Update CTO sidebar
   ├── 6.3 Rewrite 8 CTO pages
   └── 6.4 Create service health + infra pages
         │
         ▼
Phase 7: Integration & Polish ← depends on Phase 3-6
   │
   ├── 7.1 Cross-dept approval dashboard
   ├── 7.2 Role switching enhancement
   ├── 7.3 Mobile responsive
   ├── 7.4 Loading states
   └── 7.5 Error handling
```

### Total Estimated Effort

| Phase | Hours | Description |
|---|---|---|
| Phase 0 | 1h | Setup, Tailwind, ports, archive |
| Phase 1 | 2h | Types + mock data |
| Phase 2 | 3h | 20 shared components |
| Phase 3 | 3h | CEO enhancement (5 pages) |
| Phase 4 | 4h | CFO implementation (7 pages) |
| Phase 5 | 4h | COO implementation (7 pages) |
| Phase 6 | 4h | CTO implementation (10 pages) |
| Phase 7 | 2h | Polish & integration |
| **Total** | **23h** | |

---

## 14. Testing Strategy

### Per-Phase Verification

| Phase | Verification | Command |
|---|---|---|
| 0 | All 4 ports serve the app | `npm run dev:all` |
| 1 | TypeScript compiles | `npm run typecheck` |
| 2 | Components render in isolation | `npm run test` |
| 3 | CEO dashboard fully navigable | Manual test on :3011 |
| 4 | CFO dashboard fully navigable | Manual test on :3012 |
| 5 | COO dashboard fully navigable | Manual test on :3013 |
| 6 | CTO dashboard fully navigable | Manual test on :3014 |
| 7 | Cross-role navigation works | Manual test all 4 ports |

### Build Verification

```bash
npm run typecheck    # TypeScript errors = 0
npm run lint         # ESLint errors = 0
npm run build        # Clean build
npm run test         # All tests pass
```

### Manual Test Checklist

- [ ] Port 3011: CEO dashboard loads, all nav items work, dept + BU pages render
- [ ] Port 3012: CFO dashboard loads, financial pages show P&L data
- [ ] Port 3013: COO dashboard loads, operations pages show health data
- [ ] Port 3014: CTO dashboard loads, service grid shows 272 services
- [ ] Login flow works on all 4 ports
- [ ] 2FA works for CEO user
- [ ] Role switcher changes theme correctly
- [ ] Mobile responsive on all pages
- [ ] No console errors

---

**Document End: Implementation Plan v1.0**
