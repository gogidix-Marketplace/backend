# Gogidix Executive Domain - UI/UX Design Documentation

**Version:** 3.0
**Created:** 2026-04-07
**Last Updated:** 2026-04-22
**Status:** Complete (Enhanced with Departmental + Business Revenue Integration)
**Domain:** Management-Domain / Executive-Domain-X

---

## Overview

This documentation provides comprehensive UI/UX design specifications for the Gogidix Executive Domain dashboards. It serves as the single source of truth for designers, developers, and stakeholders working on the executive suite.

### Executive Dashboards Covered

| Dashboard | Description | Status |
|-----------|-------------|--------|
| **CEO Dashboard** | Strategic oversight, cross-domain visibility | ✅ Documented |
| **CFO Dashboard** | Financial consolidation, budgeting, forecasting | ✅ Documented |
| **COO Dashboard** | Operational health, incident management, resources | ✅ Documented |
| **CTO Dashboard** | Technology oversight, infrastructure, roadmap | ✅ Documented |

---

## Document Structure

### 01. UI Design System
**File:** `01-ui-design-system.md`

Defines the foundational design elements used across all executive dashboards:

- **Design Principles:** Executive-First, Clarity, Authority, Insight, Efficiency
- **Color Palette:** Role-specific colors, semantic colors, department identity colors, business unit colors
- **Typography System:** Font families, type scale, usage guidelines
- **Spacing & Layout:** 8px grid, breakpoints, container widths
- **Component Standards:** KPI cards, status badges, department badges, approval queue
- **Motion & Animation:** Durations, easing, micro-interactions
- **Data Visualization:** Chart colors, metric formatting
- **Accessibility Standards:** WCAG 2.1 AA compliance, keyboard shortcuts
- **Departmental Integration:** 8 department colors, badges, selectors, KPI grids
- **Approval Workflow:** Queue component, detail modal, confirmation toast
- **Department Reporting:** Report cards, cross-department comparison, health grid
- **Business Unit Identity:** 8 business unit colors, badges, icons, selectors
- **Business Unit Revenue:** Revenue KPI grid, comparison chart, trend sparklines
- **Business Unit Service Health:** 272-service status grid, infrastructure summary cards

### 02. User Personas
**File:** `02-user-personas.md`

Detailed persona profiles for all primary and secondary users:

**Primary Personas:**
- **Strategic CEO - Sarah Mitchell:** Company-wide oversight, quick decisions
- **Financial CFO - Marcus Chen:** Multi-currency financials, forecasting
- **Operational COO - Amina Okonkwo:** Health monitoring, incident response
- **Technical CTO - Raj Patel:** Infrastructure visibility, technical debt

**Includes:**
- Demographics, goals, behaviors, frustrations
- Technical profiles, device preferences
- Key dashboard features used
- Daily journey maps
- Pain points and solutions

### 03. Wireframes & Mockups
**File:** `03-wireframes-mockups.md`

Visual specifications for all major screens and components:

**Layout Wireframes:**
- Desktop layout (1200px+)
- Tablet layout (900px - 1199px)
- Mobile layout (< 900px)

**Dashboard Mockups:**
- CEO Dashboard (KPI Overview, Crisis Center, Approvals)
- CFO Dashboard (Financial Overview, Budget vs Actual)
- COO Dashboard (Operational Health, Incident Management)
- CTO Dashboard (Technology Health, Service Architecture)
- CEO Revenue by Business Unit (8-unit revenue grid, trends)
- CFO P&L by Business Unit (8-unit P&L, settlements, commissions)
- COO Operations by Business Unit (8-unit operations, SLA, incidents)
- CTO Service Health by Business Unit (272-service grid, infrastructure)

**Component Mockups:**
- KPI Card (normal, at-risk, critical states)
- Crisis Alert Card (severity levels)
- Approval Card (request details)
- AI Insight Card (predictions, recommendations)

**Mobile Responsive Mockups:**
- Login screen
- Dashboard home
- Approval flow

### 04. Page Flows & Navigation
**File:** `04-page-flows-navigation.md`

Complete navigation architecture and user flows:

**Navigation Architecture:**
- Role-based navigation structure (with Departments menu item)
- Sidebar menus for each role (enhanced with department navigation)
- Role switching flow

**Page Flows by Role:**
- CEO: Dashboard → KPI Detail → Domain Drill-down
- CEO: Approval flow (list → detail → confirm)
- CEO: Crisis management flow
- CEO: Strategy and Analytics flows
- CEO: Department overview → Department detail drill-down

- CFO: Budget management flow
- CFO: Forecast configuration flow
- CFO: Department financial drill-down

- COO: Incident management flow
- COO: Resource optimization flow
- COO: Department operations drill-down

- CTO: Infrastructure monitoring flow
- CTO: Roadmap planning flow
- CTO: Department technology drill-down

**Cross-Department Flows:**
- Cross-department approval flows (25 approval types)
- Department reporting navigation
- Department drill-down by executive role
- Route map for all department pages

**Business Unit Flows:**
- Business unit overview navigation (8 units)
- CEO business unit drill-down (revenue, volume, services per unit)
- CFO business unit drill-down (commission, payouts, P&L per unit)
- COO business unit drill-down (operations, SLA, incidents per unit)
- CTO business unit drill-down (service health, infrastructure per unit)
- Procurement approval flow ($5K/$25K/$100K/$100K+ thresholds)
- Business unit route map (12 new routes)
- Cross-reference: departments vs business units

**Cross-Cutting Flows:**
- Authentication (login → 2FA → role selection)
- Settings (profile, notifications, security)

**Interaction Patterns:**
- Click patterns summary
- Keyboard shortcuts
- Touch gestures (mobile)

### 05. Departmental Integration Mapping
**File:** `05-departmental-integration-mapping.md`

Complete mapping of all 8 Management-Domain departments to executive dashboards:

- **Digital Marketing** → CEO (ROI, brand), CFO (budget, CPL), COO (pipeline), CTO (health)
- **Customer Support** → CEO (CSAT, NPS), CFO (SLA penalties), COO (SLA, incidents), CTO (automation)
- **Global Business Management** → CEO (revenue, BI), CFO (currency, tax), COO (fulfillment), CTO (pipeline)
- **Human Resource** → CEO (headcount, eNPS), CFO (payroll), COO (compliance), CTO (adoption)
- **Sales** → CEO (revenue, pipeline), CFO (forecasts, commission), COO (velocity, territory), CTO (ML models)
- **System Administrator** → CEO (availability), CFO (infra cost), COO (MTTD/MTTR), CTO (security, compliance)
- **Finance** → CEO (P&L), CFO (all financials), COO (budget tracking), CTO (automation health)
- **Foundation Services Monitoring** → CEO (health score), CFO (cost), COO (SLA), CTO (AI metrics)

**Includes:**
- Cross-department approval flow matrix (25 types)
- Departmental reporting flow matrix (8 departments × 6 cadences)
- Executive approval authority matrix (RACI)
- Real-time data feed architecture (WebSocket channels)
- Executive notification rules by department (22+ rules)

### 06. Business Revenue Integration Mapping
**File:** `06-business-revenue-integration-mapping.md`

Complete mapping of all 8 revenue-generating business units from shared-business-infrastructure to executive dashboards:

- **Courier Services** → CEO (volume, satisfaction), CFO (commission, surge), COO (OTD, dispatch), CTO (20-service grid)
- **E-Commerce Platform** → CEO (GMV, vendors), CFO (payments, returns), COO (fulfillment, inventory), CTO (76-service grid)
- **Warehousing & Storage** → CEO (partners, occupancy), CFO (storage revenue), COO (utilization, pick/pack), CTO (43-service grid)
- **Air Freight** → CEO (bookings, routes), CFO (yield/kg, surcharges), COO (transit, customs), CTO (15-service grid)
- **Ocean Shipping** → CEO (FCL+LCL, vessels), CFO (revenue/TEU, BAF/CAF), COO (containers, reliability), CTO (18-service grid)
- **Haulage & Road Freight** → CEO (loads, carriers), CFO (revenue/km, margin), COO (OTD, empty miles), CTO (62-service grid)
- **Procurement** → CEO (spend, suppliers), CFO (budget variance, matching), COO (cycle times, OTD), CTO (27-service grid)
- **Admin & Partner Oversight** → CEO (partners, tiers), CFO (commissions, settlements), COO (onboarding, SLA), CTO (11-service grid)

**Includes:**
- Consolidated revenue dashboard matrix (8 units × 4 roles)
- Business unit KPI targets (revenue, operational, technology)
- Revenue flow architecture (Kafka → Admin Core → Executive Command → Dashboard)
- WebSocket channels per business unit per role (28 channels)
- Total: 272 microservices across 8 business units
- Total: 258 executive data points (160 business unit + 98 department)

---

## Tech Stack

### Frontend Technologies

| Technology | Purpose | Version |
|------------|---------|---------|
| **React** | UI Framework | 18.3+ |
| **TypeScript** | Type Safety | 5.4+ |
| **Vite** | Build Tool | 5.1+ |
| **Zustand** | State Management | 4.5+ |
| **TanStack Query** | Data Fetching | 5.28+ |
| **React Router** | Routing | 6.22+ |
| **Recharts** | Charts | 2.12+ |
| **Radix UI** | Component Primitives | Latest |
| **Framer Motion** | Animations | 12.38+ |
| **Lucide React** | Icons | 0.344+ |
| **Tailwind CSS** | Styling | 3.5+ |

### Backend Integration

| Service Type | Base URL | Authentication |
|-------------|----------|----------------|
| **Java Services** | `/api/v1` | JWT + Bearer |
| **Node.js Services** | `/api/v2` | JWT + Bearer |
| **WebSocket** | `/ws` | STOMP over SockJS |

---

## Design Philosophy

### Executive Excellence

Our design system is built for C-level executives who need to make critical decisions quickly.

| Principle | Implementation |
|-----------|----------------|
| **Clarity** | High contrast, clear hierarchy, minimal cognitive load |
| **Authority** | Refined typography, premium colors, consistent spacing |
| **Insight** | Progressive disclosure, smart defaults, AI highlighting |
| **Efficiency** | Keyboard navigation, bulk actions, intelligent defaults |
| **Context** | Breadcrumbs, domain indicators, location badges |

### Avoid These Anti-Patterns

- ❌ Generic AI aesthetics (purple gradients on white)
- ❌ Overwhelming dashboards (data density without hierarchy)
- ❌ Click-heavy workflows (minimize steps to key actions)
- ❌ Ambiguous status (use clear, color-coded indicators)
- ❌ Hidden information (progressive disclosure, not burial)

---

## Key Metrics & Targets

### Performance Targets

| Metric | Target | Priority |
|--------|--------|----------|
| Initial Load | < 2s | P0 |
| Dashboard Render | < 1s | P0 |
| KPI Card Click | < 200ms | P1 |
| Chart Render | < 500ms | P1 |
| Page Transition | < 300ms | P1 |
| WebSocket Latency | < 100ms | P0 |

### Accessibility Targets

| Standard | Target | Status |
|----------|--------|--------|
| WCAG 2.1 AA | 100% compliance | In Progress |
| Keyboard Navigation | All features | In Progress |
| Screen Reader | Full support | In Progress |
| Touch Targets | 44x44px minimum | Complete |
| Color Contrast | 4.5:1 minimum | Complete |

---

## Usage Guidelines

### For Designers

1. Start with the **Design System** document to understand foundations
2. Review **User Personas** to understand who you're designing for
3. Use **Wireframes** as layout templates
4. Reference **Page Flows** for interaction patterns

### For Developers

1. **Design System:** Implement tokens, components, utilities first
2. **Wireframes:** Build layout components matching mockups
3. **Page Flows:** Implement navigation and routing
4. **Integration:** Connect to backend services (Java/Node.js)

### For Product Managers

1. **User Personas:** Validate features against user needs
2. **Page Flows:** Review workflows for completeness
3. **Wireframes:** Sign off on visual designs
4. **Design System:** Ensure consistency across features

---

## Implementation Status

### Phase 1: Foundation (Complete)

- [x] Design System documentation
- [x] User Personas defined
- [x] Wireframes created
- [x] Page flows documented
- [x] Component specifications

### Phase 1.5: Departmental Integration (Complete)

- [x] All 8 departments explored and analyzed
- [x] Department-to-executive reporting flows mapped
- [x] Approval workflows mapped (25 types)
- [x] Department stakeholder personas defined (8 personas)
- [x] Department design system components specified
- [x] Cross-department wireframes created
- [x] Cross-department page flows documented
- [x] Real-time data feed architecture defined
- [x] Executive notification rules defined

### Phase 1.6: Business Revenue Integration (Complete)

- [x] All 8 revenue business units explored and analyzed (272 services)
- [x] Business unit revenue models documented
- [x] CEO/CFO/COO/CTO data points mapped per unit (160 total)
- [x] Business unit KPI targets defined (revenue, operational, technology)
- [x] Revenue flow architecture defined (Kafka → Admin Core → Executive)
- [x] Business unit identity components (8 colors, badges, icons)
- [x] Business unit revenue KPI grid component
- [x] Business unit revenue comparison chart component
- [x] Business unit trend sparkline component
- [x] Business unit service health status grid (272 services)
- [x] Business unit infrastructure summary card
- [x] CEO revenue by business unit dashboard mockup
- [x] CFO P&L by business unit dashboard mockup
- [x] COO operations by business unit dashboard mockup
- [x] CTO service health by business unit dashboard mockup
- [x] Unified business unit revenue comparison mockup
- [x] Business Units navigation added to all 4 role sidebars
- [x] Business unit drill-down flows for all 4 roles
- [x] Business unit route map (12 new routes + slug mapping)
- [x] Procurement approval flow ($5K/$25K/$100K/$100K+ thresholds)
- [x] Cross-reference table: departments vs business units

### Phase 2: Implementation (In Progress 🚧)

- [x] Project scaffolding (Vite + React + TS)
- [x] Shared component library
- [x] Layout components (Header, Sidebar)
- [x] Authentication flow
- [ ] CEO Dashboard pages
- [ ] CFO Dashboard pages
- [ ] COO Dashboard pages
- [ ] CTO Dashboard pages

### Phase 3: Polish (Pending ⏳)

- [ ] Animations and transitions
- [ ] Mobile responsive refinement
- [ ] Accessibility audit
- [ ] Performance optimization
- [ ] User testing
- [ ] Documentation updates

---

## File Locations

### Documentation
```
Management-domain/docs/ui-ux/
├── README.md (this file)
├── 01-ui-design-system.md (v4.0 - includes department + business unit components)
├── 02-user-personas.md (v2.0 - includes 8 department stakeholder personas)
├── 03-wireframes-mockups.md (v3.0 - includes dept dashboards + business unit revenue views)
├── 04-page-flows-navigation.md (v3.0 - includes dept flows + business unit drill-downs)
├── 05-departmental-integration-mapping.md (8-dept integration map)
└── 06-business-revenue-integration-mapping.md (8 business unit revenue map)
```

### Implementation Plan
**File:** `07-implementation-plan.md`

Production-ready implementation plan for all 4 executive dashboards:
- **Architecture:** Single codebase (ceo-web-dashboard) serving all 4 roles via port-based routing
- **Ports:** CEO=3011 | CFO=3012 | COO=3013 | CTO=3014
- **8 phases:** Setup → Types → Components → CEO → CFO → COO → CTO → Polish
- **~23 hours** estimated implementation time
- **20+ new shared components**, **29 new/rewritten pages**, **8 mock data files**

```

### Frontend Implementations
```
Management-domain/Executive-domain-X/Frontends/Web/
├── ceo-web-dashboard/
├── cfo-web-dashboard/
├── coo-web-dashboard/
└── cto-web-dashboard/
```

### Backend Services
```
Management-domain/Executive-domain-X/Backend/
├── Java/
│   ├── ceo-analytics-service/
│   ├── ceo-approval-service/
│   ├── ceo-strategy-service/
│   ├── cfo-financial-consolidation-service/
│   ├── coo-operations-service/
│   ├── cto-technology-oversight-service/
│   ├── executive-alert-service/
│   ├── executive-approval-workflow-service/
│   ├── executive-audit-service/
│   └── executive-dashboard-service/
└── Nodes/
    ├── executive-command-service/
    ├── executive-query-service/
    ├── executive-realtime-service/
    ├── kafka-consumer-service/
    └── websocket-service/
```

---

## Quick Reference

### Color Quick Reference

```css
/* Primary - Executive Blue */
--color-primary: #0D47A1;

/* Role Accents */
--color-ceo: #FFA000;  /* Gold */
--color-cfo: #FF6B00;  /* Orange */
--color-coo: #00BCD4;  /* Cyan */
--color-cto: #7C4DFF;  /* Purple */

/* Status */
--color-success: #10B981;  /* Green */
--color-warning: #F59E0B;  /* Amber */
--color-danger: #EF4444;   /* Red */
```

### Component Quick Reference

| Component | Props | Usage |
|-----------|-------|-------|
| `KPICard` | value, change, status, trend | Display metrics |
| `CrisisAlert` | severity, title, actions | Show critical issues |
| `ApprovalCard` | type, amount, requester | Approval requests |
| `AIInsight` | type, content, confidence | AI predictions |

---

## Changelog

### v1.0 - 2026-04-07

**Added:**
- Complete UI Design System documentation
- 4 detailed User Personas (CEO, CFO, COO, CTO)
- Comprehensive Wireframes and Mockups
- Complete Page Flows and Navigation documentation
- Quick reference guide

### v2.0 - 2026-04-22

**Added (Departmental Integration Enhancement):**
- NEW: `05-departmental-integration-mapping.md` — Complete 8-department mapping
- 8 department stakeholder personas (Marketing, Support, Business, HR, Sales, SysAdmin, Finance, Foundation)
- Department identity color system and badge components
- Approval Queue, Approval Detail Modal, and Confirmation Toast components
- Department KPI Grid, Health Status Grid, and Report Card components
- Cross-department comparison and budget utilization components
- CEO/CFO/COO/CTO dashboard mockups with department integration views
- 25 cross-department approval flow types with full routing
- Department reporting flow matrix (8 departments × 6 cadences)
- Executive approval authority matrix (RACI by category)
- Real-time WebSocket data feed architecture per role
- 22+ executive notification rules by department
- Department drill-down navigation routes for all 4 roles
- Departments menu item added to all 4 role navigation sidebars

**Next Release:**
- Add animation specifications
- Add component code examples
- Add integration testing guide
- Add end-to-end testing checklist

### v3.0 - 2026-04-22

**Added (Business Revenue Integration Enhancement):**
- NEW: `06-business-revenue-integration-mapping.md` — Complete 8 business unit revenue mapping (272 services)
- Business unit identity color system (8 distinct colors) and badge components
- Business unit selector dropdown with revenue indicator
- Business unit revenue KPI grid (8 units, 4-column layout)
- Business unit revenue comparison chart (horizontal bar)
- Business unit revenue trend sparklines (30-day)
- Business unit service health status grid (272 services)
- Business unit infrastructure summary card
- CEO revenue by business unit dashboard mockup
- CFO P&L by business unit dashboard mockup (8-unit P&L table, settlements, commissions)
- COO operations by business unit dashboard mockup (8-unit operations, SLA, incidents)
- CTO service health by business unit dashboard mockup (272-service grid, infrastructure)
- Unified business unit revenue comparison mockup (ranking, metrics, trends)
- Business Units navigation item added to all 4 role sidebars
- CEO business unit drill-down flows (8 units, revenue/volume/services per unit)
- CFO business unit drill-down flows (8 units, commission/payouts/P&L per unit)
- COO business unit drill-down flows (8 units, operations/SLA/incidents per unit)
- CTO business unit drill-down flows (8 units, service health/infrastructure per unit)
- Procurement approval flow with 4-tier thresholds ($5K/$25K/$100K/$100K+)
- Business unit route map (12 new routes + slug mapping for 8 units)
- Cross-reference table: departments vs business units navigation
- Consolidated revenue dashboard matrix (8 units × 4 roles)
- Business unit KPI targets (revenue, operational, technology — 8 units)
- Revenue flow architecture (Kafka → Admin Core → Executive Command → Dashboard)
- WebSocket channels per business unit per role (28 channels)

**Next Release:**
- Frontend implementation of all 4 executive dashboards
- Component code examples with React + TypeScript
- Integration testing guide
- End-to-end testing checklist

---

## Contact & Support

**Documentation Maintainer:** UI/UX Team
**Technical Lead:** Frontend Engineering
**Last Review:** 2026-04-07

For questions or suggestions about this documentation, please contact the UI/UX team or create an issue in the project repository.

---

**End of UI/UX Documentation Index**
