# GLOBAL BUSINESS DASHBOARD - UI FLOW

**Version:** 1.0
**Domain:** Management Domain
**Subdomain:** Global-business-management
**Frontend:** global-business-web-dashboard
**Framework:** React 18 + Vite + TypeScript + Ant Design v5.15+
**State Management:** Zustand
**Data Fetching:** TanStack Query
**Last Updated:** 2026-02-23

---

## OVERVIEW

Global Business Management (GBM) Dashboard provides strategic oversight of international operations, cross-border business development, regional performance, and global partnership management across all operating countries.

---

## NAVIGATION

```
Global Business Dashboard
├── 🏠 Overview
├── 🌍 Regions
│   ├── Europe
│   ├── Africa
│   ├── Americas
│   └── Asia Pacific
├── 💼 Partners
│   ├── Channel Partners
│   ├── Strategic Alliances
│   └── Partner Performance
├── 📈 Business Development
│   ├── Opportunities
│   ├── Pipeline
│   └── Deals
├── 📊 Analytics
│   ├── Regional Performance
│   ├── Market Analysis
│   └── Competitive Intelligence
└── ⚙️ Settings
    ├── Preferences
    └── Notifications
```

---

## USER ROLES

### GBM Roles

| Role | Description | Access Level |
|------|-------------|--------------|
| GLOBAL_BUSINESS_DIRECTOR | Full strategic oversight | Global |
| REGIONAL_DIRECTOR | Regional management | Region-specific |
| BUSINESS_DEVELOPMENT_MANAGER | Deal management, pipeline | Global/Regional |
| PARTNER_MANAGER | Partner relationships | Partners only |
| GBM_ANALYST | Read-only analytics | Read Only |

### Role Hierarchy

```
GLOBAL_BUSINESS_DIRECTOR (Full Access)
├── REGIONAL_DIRECTOR (Europe)
│   ├── BUSINESS_DEVELOPMENT_MANAGER
│   └── PARTNER_MANAGER
├── REGIONAL_DIRECTOR (Africa)
│   ├── BUSINESS_DEVELOPMENT_MANAGER
│   └── PARTNER_MANAGER
├── REGIONAL_DIRECTOR (Americas)
│   └── ...
├── REGIONAL_DIRECTOR (Asia Pacific)
│   └── ...
└── GBM_ANALYST (Read Only)
```

---

## DATA FLOW ARCHITECTURE

```
┌─────────────────────────────────────────────────────────────────────────┐
│                      GBM DASHBOARD                                    │
├─────────────────────────────────────────────────────────────────────────┤
│                                                                         │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐   │
│  │  Regional   │  │  Partner    │  │  Business   │  │  Market     │   │
│  │  Service    │  │  Service    │  │  Dev Service │  │  Intel      │   │
│  └──────┬──────┘  └──────┬──────┘  └──────┬──────┘  └──────┬──────┘   │
│         │                │                │                │           │
│         └────────────────┴────────────────┴────────────────┘           │
│                                  │                                    │
│                                  ▼                                    │
│                    ┌───────────────────────┐                         │
│                    │  GBM API Gateway       │                         │
│                    └───────────┬───────────┘                         │
│                                │                                    │
│            ┌───────────────────┼───────────────────┐                 │
│            ▼                   ▼                   ▼                 │
│    ┌───────────┐       ┌───────────┐       ┌───────────┐             │
│    │ Country   │       │ Partner   │       │ Shared    │             │
│    │ Services  │       │ Services  │       │ Cores     │             │
│    └───────────┘       └───────────┘       └───────────┘             │
└─────────────────────────────────────────────────────────────────────────┘
```

---

## AUTHENTICATION FLOW

```
┌────────────────┐     ┌────────────────┐     ┌────────────────┐
│ User submits   │────>│ Auth Service   │────>│ Validate GBM   │
│ credentials    │    │ validates      │     │ Role Access    │
└────────────────┘     └────────────────┘     └────────────────┘
                                                      │
                          ┌─────────────────────────┴─────────────────────┐
                          ▼                           ▼                   ▼
                   ┌─────────────┐           ┌─────────────┐      ┌─────────────┐
                   │ Valid GBM   │           │ Invalid     │      │ MFA         │
                   │ Credentials │           │ Credentials │      │ Required    │
                   └──────┬──────┘           └──────┬──────┘      └──────┬──────┘
                          │                         │                    │
                          ▼                         ▼                    ▼
                   ┌─────────────┐           ┌─────────────┐      ┌─────────────┐
                   │ Load Region │           │ Return      │      │ Prompt      │
                   │ Scope       │           │ Error       │      │ for MFA     │
                   └──────┬──────┘           └─────────────┘      └──────┬──────┘
                          │                                             │
                          ▼                                             ▼
                   ┌─────────────┐                              ┌─────────────┐
                   │ Load GBM    │                              │ Verify      │
                   │ Dashboard   │                              │ MFA Code    │
                   └─────────────┘                              └──────┬──────┘
                                                                  │
                                                                  ▼
                                                           ┌─────────────┐
                                                           │ Load GBM    │
                                                           │ Dashboard   │
                                                           └─────────────┘
```

---

## REGIONAL OVERVIEW FLOW

```
┌────────────────┐     ┌────────────────┐     ┌────────────────┐
│ Dashboard      │────>│ Load Regional  │────>│ Display Region │
│ Loaded         │     │ Performance    │     │ Cards         │
└────────────────┘     └────────────────┘     └────────────────┘
                                                      │
                      ┌─────────────────────────────┼─────────────────────────────┐
                      ▼                             ▼                             ▼
               ┌─────────────┐              ┌─────────────┐              ┌─────────────┐
               │ Europe      │              │ Africa      │              │ Americas    │
               │ Performance │              │ Performance │              │ Performance │
               └──────┬──────┘              └──────┬──────┘              └──────┬──────┘
                      │                            │                            │
                      └────────────────────────────┴────────────────────────────┘
                                                       │
                                                       ▼
                                                ┌─────────────┐
                                                │ Select      │
                                                │ Region for  │
                                                │ Detail View │
                                                └─────────────┘
```

---

## PARTNER MANAGEMENT FLOW

```
┌────────────────┐     ┌────────────────┐     ┌────────────────┐
│ Click Partner  │────>│ Load Partner   │────>│ Display       │
│ Management     │     │ List           │     │ Partner Cards │
└────────────────┘     └────────────────┘     └────────────────┘
                                                      │
                      ┌─────────────────────────────┴─────────────────────┐
                      ▼                                                   ▼
               ┌─────────────┐                                     ┌─────────────┐
               │ Active      │                                     │ New         │
               │ Partners    │                                     │ Partner     │
               └──────┬──────┘                                     └──────┬──────┘
                      │                                                   │
                      ▼                                                   ▼
               ┌─────────────┐                                     ┌─────────────┐
               │ Partner     │                                     │ Partner     │
               │ Details     │                                     │ Onboarding  │
               │ Performance │                                     │ Workflow    │
               └─────────────┘                                     └─────────────┘
```

---

## DEAL PIPELINE FLOW

```
┌────────────────┐     ┌────────────────┐     ┌────────────────┐
│ Click Business │────>│ Load Deal      │────>│ Display       │
│ Development   │     │ Pipeline       │     │ Pipeline View │
└────────────────┘     └────────────────┘     └────────────────┘
                                                      │
                      ┌─────────────────────────────┴─────────────────────┐
                      ▼                             ▼                             ▼
               ┌─────────────┐              ┌─────────────┐              ┌─────────────┐
               │ Prospecting │              │ Qualifying  │              │ Closing     │
               │ Phase       │              │ Phase       │              │ Phase       │
               └──────┬──────┘              └──────┬──────┘              └──────┬──────┘
                      │                            │                            │
                      └────────────────────────────┴────────────────────────────┘
                                                       │
                                                       ▼
                                                ┌─────────────┐
                                                │ Update Deal │
                                                │ Status      │
                                                └─────────────┘
```

---

## REAL-TIME UPDATES

### WebSocket Events

| Event | Trigger | Action |
|-------|---------|--------|
| `deal:created` | New deal added to pipeline | Add to pipeline list |
| `deal:stage_changed` | Deal moved to new stage | Update pipeline view |
| `partner:registered` | New partner onboarded | Add to partners list |
| `region:target_met` | Regional target achieved | Show celebration notification |
| `alert:competitor` | Competitor activity detected | Show intelligence alert |

---

## SESSION MANAGEMENT

| Setting | Value |
|---------|-------|
| Session Duration | 10 hours (extended) |
| Idle Timeout | 60 minutes |
| Refresh Window | 30 minutes before expiry |
| Concurrent Sessions | Max 3 per user |
| Auto-refresh | Dashboard data every 60 seconds |

---

## VERSION HISTORY

| Version | Date | Changes |
|---------|------|---------|
| 1.0 | 2026-02-23 | Initial UI Flow Documentation |

---

**Document End**
