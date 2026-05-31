# HQ GBM DASHBOARD - PAGE BY PAGE

**Version:** 1.0
**Domain:** Management Domain
**Subdomain:** Global-business-management
**Last Updated:** 2025-02-08

---

## PAGE TREE

```
HQ GBM Dashboard
├── /global-overview
├── /markets
├── /partnerships
├── /initiatives
├── /opportunities
└── /reports
```

---

## GLOBAL OVERVIEW FLOW

### Dashboard Entry Flow

```
┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐
│ Login / Auth    │────>│ Role Check      │────>│ Global Overview │
│                 │     │ GBM Role        │     │ Dashboard       │
└─────────────────┘     └─────────────────┘     └─────────────────┘
                                                        │
                        ┌───────────────────────────────┼───────────────────────────────┐
                        ▼                               ▼                               ▼
                 ┌──────────────┐               ┌──────────────┐               ┌──────────────┐
                 │ Markets      │               │ Partnerships │               │ Initiatives  │
                 │ Summary      │               │ Overview     │               │ Tracker     │
                 └──────────────┘               └──────────────┘               └──────────────┘
```

### Key Metrics Display

| Metric | Description | Data Source | Refresh Rate |
|--------|-------------|-------------|--------------|
| Total Markets | All markets being tracked | Market Service | Real-time |
| Active Markets | Markets with active operations | Market Service | Real-time |
| Partnerships | Active strategic partnerships | Partnership Service | Hourly |
| Initiatives | Strategic initiatives in progress | Initiative Service | Real-time |
| Pipeline Value | Total BD opportunity value | Opportunity Service | Real-time |

### Filter Flow

```
┌──────────────┐     ┌──────────────┐     ┌──────────────┐     ┌──────────────┐
│ Select Date  │────>│ Select       │────>│ Select       │────>│ Apply        │
│ Range        │     │ Regions      │     │ Categories   │     │ Filters      │
└──────────────┘     └──────────────┘     └──────────────┘     └──────────────┘
                                                                          │
                                                                          ▼
                                                                   ┌──────────────┐
                                                                   │ Update       │
                                                                   │ Dashboard    │
                                                                   └──────────────┘
```

---

## MARKETS COMPARISON FLOW

### Market Analysis Entry

```
┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐
│ Click Markets   │────>│ Select Market   │────>│ View Market     │
│ Navigation      │     │ or Compare      │     │ Analysis        │
└─────────────────┘     └─────────────────┘     └─────────────────┘
                                                           │
                        ┌──────────────────────────────────┼──────────────────────────────────┐
                        ▼                                  ▼                                  ▼
                 ┌──────────────┐                  ┌──────────────┐                  ┌──────────────┐
                 │ Market Size  │                  │ Competition  │                  │ Entry        │
                 │ & Growth     │                  │ Analysis     │                  │ Barriers     │
                 └──────────────┘                  └──────────────┘                  └──────────────┘
```

### Market Comparison Flow

```
┌──────────────┐     ┌──────────────┐     ┌──────────────┐     ┌──────────────┐
│ Select       │────>│ Choose       │────>│ Select       │────>│ Generate     │
│ Compare Mode │     │ Markets (2+) │     │ Metrics      │     │ Comparison   │
└──────────────┘     └──────────────┘     └──────────────┘     └──────────────┘
                                                                          │
                                                                          ▼
                                                                   ┌──────────────┐
                                                                   │ Side-by-Side │
                                                                   │ View         │
                                                                   └──────────────┘
```

### Market Analysis Metrics

| Category | Metrics | Visualization |
|----------|---------|---------------|
| Market Size | TAM, SAM, SOM | Pie charts, Funnel |
| Growth | CAGR, YoY growth | Line charts, Bar graphs |
| Competition | Market share, Concentration | Bar charts, Heat maps |
| Entry Barriers | Regulatory, Capital, Distribution | Radar charts |
| SWOT | Strengths, Weaknesses, Opportunities, Threats | Quadrant view |

---

## PARTNERSHIPS MANAGEMENT FLOW

### Partnership Discovery Flow

```
┌──────────────┐     ┌──────────────┐     ┌──────────────┐     ┌──────────────┐
│ Click "Add   │────>│ Search/      │────>│ Evaluate     │────>│ Create       │
│ Partnership" │     │ Browse       │     │ Potential    │     │ Record       │
└──────────────┘     └──────────────┘     └──────────────┘     └──────────────┘
                                                                          │
                                                                          ▼
                                                                   ┌──────────────┐
                                                                   │ Assign       │
                                                                   │ Owner        │
                                                                   └──────────────┘
```

### Partnership Lifecycle Flow

```
┌──────────────┐     ┌──────────────┐     ┌──────────────┐     ┌──────────────┐
│ PROPOSED     │────>│ UNDER REVIEW │────>│ ACTIVE       │────>│ RENEWAL DUE  │
│              │     │              │     │              │     │              │
└──────────────┘     └──────────────┘     └──────────────┘     └──────────────┘
                           │                     │
                           ▼                     ▼
                    ┌──────────────┐    ┌──────────────┐
                    │ REJECTED     │    │ SUSPENDED    │
                    └──────────────┘    │ TERMINATED   │
                                        └──────────────┘
```

### Partnership Performance Tracking

| Stage | Key Metrics | Alert Thresholds |
|-------|-------------|------------------|
| Active | Revenue, Deal count, Satisfaction | Revenue < 80% target |
| Review | Evaluation score, Risk assessment | Score < 3/5 |
| Renewal | ROI, Strategic value, Future potential | ROI < 1.0x |

---

## STRATEGIC INITIATIVES FLOW

### Initiative Creation Flow

```
┌──────────────┐     ┌──────────────┐     ┌──────────────┐     ┌──────────────┐
│ Click "New   │────>│ Define       │────>│ Set Budget   │────>│ Assign       │
│ Initiative"  │     │ Scope        │     │ & Timeline   │     │ Owner        │
└──────────────┘     └──────────────┘     └──────────────┘     └──────────────┘
                                                                          │
                                                                          ▼
                                                                   ┌──────────────┐
                                                                   │ Create       │
                                                                   │ Milestones   │
                                                                   └──────────────┘
```

### Initiative Status Flow

```
┌──────────────┐     ┌──────────────┐     ┌──────────────┐     ┌──────────────┐
│ PROPOSED     │────>│ APPROVED     │────>│ IN PROGRESS  │────>│ COMPLETED    │
│              │     │              │     │              │     │              │
└──────────────┘     └──────────────┘     └──────────────┘     └──────────────┘
                           │                     │
                           ▼                     ▼
                    ┌──────────────┐    ┌──────────────┐
                    │ REJECTED     │    │ ON HOLD      │
                    └──────────────┘    │ CANCELLED    │
                                        └──────────────┘
```

### Milestone Management Flow

```
┌──────────────┐     ┌──────────────┐     ┌──────────────┐     ┌──────────────┐
│ View         │────>│ Check Status  │────>│ Update       │────>│ Log          │
│ Initiative   │     │              │     │ Progress     │     │ Completion   │
└──────────────┘     └──────────────┘     └──────────────┘     └──────────────┘
```

---

## BUSINESS OPPORTUNITIES FLOW

### Opportunity Creation Flow

```
┌──────────────┐     ┌──────────────┐     ┌──────────────┐     ┌──────────────┐
│ Click "New   │────>│ Select       │────>│ Define       │────>│ Estimate     │
│ Opportunity" │     │ Type         │     │ Scope        │     │ Value        │
└──────────────┘     └──────────────┘     └──────────────┘     └──────────────┘
                                                                          │
                                                                          ▼
                                                                   ┌──────────────┐
                                                                   │ Assign       │
                                                                   │ Owner        │
                                                                   └──────────────┘
```

### Opportunity Pipeline Flow

```
┌──────────────┐     ┌──────────────┐     ┌──────────────┐     ┌──────────────┐
│ IDENTIFIED   │────>│ QUALIFIED    │────>│ ANALYSIS     │────>│ PROPOSAL     │
│              │     │              │     │              │     │              │
└──────────────┘     └──────────────┘     └──────────────┘     └──────────────┘
                                                           │
                                                           ▼
                                                    ┌──────────────┐     ┌──────────────┐
                                                    │ NEGOTIATION  │────>│ CLOSING      │
                                                    └──────────────┘     └──────────────┘
                                                                                │
                                                                    ┌───────────┴───────────┐
                                                                    ▼                       ▼
                                                              ┌──────────────┐       ┌──────────────┐
                                                              │ WON          │       │ LOST         │
                                                              └──────────────┘       └──────────────┘
```

### Opportunity Conversion Flow

```
┌──────────────┐     ┌──────────────┐     ┌──────────────┐     ┌──────────────┐
│ Opportunity  │────>│ Click        │────>│ Select       │────>│ Create       │
│ Won          │     │ "Convert"    │     │ Target Type  │     │ New Entity   │
└──────────────┘     └──────────────┘     └──────────────┘     └──────────────┘
                                                                      │
                        ┌─────────────────────────────────────────────┼─────────────────────────────────────┐
                        ▼                                               ▼                                     ▼
                 ┌──────────────┐                               ┌──────────────┐                       ┌──────────────┐
                 │ Partnership  │                               │ Initiative   │                       │ Project      │
                 │ Created      │                               │ Created      │                       │ Created      │
                 └──────────────┘                               └──────────────┘                       └──────────────┘
```

---

## PERMISSION MATRIX

### Role-Based Access

| Permission | VP_GBM | GLOBAL_GBM_DIR | REGIONAL_MGR | BD_MANAGER | ANALYST |
|------------|--------|----------------|--------------|------------|---------|
| View Global Dashboard | ✓ | ✓ | ✓ | ✓ | ✓ |
| Compare Markets | ✓ | ✓ | ✓ | ✓ | ✓ |
| Create Partnerships | ✓ | ✓ | Partial | ✗ | ✗ |
| Edit Partnerships | ✓ | ✓ | Own only | Own only | ✗ |
| Delete Partnerships | ✓ | ✗ | ✗ | ✗ | ✗ |
| Create Initiatives | ✓ | ✓ | ✗ | ✗ | ✗ |
| Edit Initiatives | ✓ | ✓ | Own only | Own only | ✗ |
| Create Opportunities | ✓ | ✓ | ✓ | ✓ | ✗ |
| Convert Opportunities | ✓ | ✓ | ✗ | ✗ | ✗ |
| Export Reports | ✓ | ✓ | ✓ | ✓ | ✓ |
| Manage Market Data | ✓ | Partial | ✗ | ✗ | ✗ |

### Market Access Levels

| Level | Description | Markets Access |
|-------|-------------|----------------|
| Global | All markets, full oversight | All |
| Regional | Specific regional markets | Region-specific |
| Local | Single market only | Assigned market |
| ReadOnly | View only, no modifications | All or Assigned |

---

## SUMMARY

**Global-business-management (GBM) (8 files)** ✓

**Document End**
