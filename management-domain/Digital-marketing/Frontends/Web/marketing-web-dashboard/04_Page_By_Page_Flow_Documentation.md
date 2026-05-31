# HQ MARKETING DASHBOARD - PAGE BY PAGE

**Version:** 1.0
**Last Updated:** 2025-02-08

---

## PAGE TREE

```
HQ Marketing Dashboard
├── /global-overview
├── /countries
├── /campaigns
└── /reports
```

---

## GLOBAL OVERVIEW FLOW

### Dashboard Entry Flow

```
┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐
│ Login / Auth    │────>│ Role Check      │────>│ Global Overview │
│                 │     │ Marketing Role  │     │ Dashboard       │
└─────────────────┘     └─────────────────┘     └─────────────────┘
                                                        │
                     ┌──────────────────────────────────┼──────────────────────────────────┐
                     ▼                                  ▼                                  ▼
              ┌──────────────┐                  ┌──────────────┐                  ┌──────────────┐
              │ Campaign     │                  │ Performance  │                  │ Budget       │
              │ Summary      │                  │ Metrics      │                  │ Overview     │
              └──────────────┘                  └──────────────┘                  └──────────────┘
```

### Global Metrics Display

| Metric | Description | Data Source | Refresh Rate |
|--------|-------------|-------------|--------------|
| Total Campaigns | Active campaigns across all countries | Campaign Service | Real-time |
| Global ROI | Aggregate return on investment | Analytics Service | Hourly |
| Total Budget | Combined marketing budget | Finance Service | Daily |
| Spend vs Budget | Budget utilization percentage | Finance Service | Daily |
| Active Countries | Countries with active campaigns | Location Service | Real-time |

### Filter Flow

```
┌──────────────┐     ┌──────────────┐     ┌──────────────┐     ┌──────────────┐
│ Select Date  │────>│ Select       │────>│ Select       │────>│ Apply        │
│ Range        │     │ Countries    │     │ Campaign     │     │ Filters      │
└──────────────┘     └──────────────┘     └──────────────┘     └──────────────┘
                                                                          │
                                                                          ▼
                                                                   ┌──────────────┐
                                                                   │ Update       │
                                                                   │ Dashboard    │
                                                                   └──────────────┘
```

---

## COUNTRY COMPARISON FLOW

### Comparison Dashboard Flow

```
┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐
│ Select Compare  │────>│ Choose          │────>│ Comparison      │
│ Countries       │     │ Metrics         │     │ View            │
└─────────────────┘     └─────────────────┘     └─────────────────┘
                                                       │
                        ┌──────────────────────────────┼──────────────────────────────┐
                        ▼                              ▼                              ▼
                 ┌──────────────┐              ┌──────────────┐              ┌──────────────┐
                 │ Performance  │              │ Budget       │              │ Campaign     │
                 │ Comparison   │              │ Comparison   │              │ Comparison   │
                 └──────────────┘              └──────────────┘              └──────────────┘
```

### Country Selection

| Selection Type | Max Selection | Description |
|----------------|---------------|-------------|
| Single | 1 | View detailed country metrics |
| Compare | 2-4 | Side-by-side comparison view |
| Aggregate | All | Global summary with country breakdown |

### Comparison Metrics

| Category | Metrics | Visualization |
|----------|---------|---------------|
| Performance | ROI, Conversion Rate, CTR, Engagement Rate | Bar charts, Line graphs |
| Budget | Total Spend, Budget Remaining, Cost per Lead | Pie charts, Progress bars |
| Audience | Reach, Impressions, New vs Returning | Heat maps, Funnel charts |
| Campaign | Active, Paused, Completed, Scheduled | Status cards, Timeline |

---

## CAMPAIGN MANAGEMENT FLOW

### Create Campaign Flow

```
┌──────────────┐     ┌──────────────┐     ┌──────────────┐     ┌──────────────┐
│ Click "New   │────>│ Select       │────>│ Define       │────>│ Set Budget   │
│ Campaign"    │     │ Countries    │     │ Objectives   │     │ & Schedule   │
└──────────────┘     └──────────────┘     └──────────────┘     └──────────────┘
                                                                          │
                                                                          ▼
                                                                   ┌──────────────┐
                                                                   │ Configure   │
                                                                   │ Targeting   │
                                                                   └──────────────┘
```

### Campaign Status Flow

```
┌──────────────┐     ┌──────────────┐     ┌──────────────┐     ┌──────────────┐
│ DRAFT        │────>│ SCHEDULED    │────>│ ACTIVE       │────>│ COMPLETED   │
│              │     │              │     │              │     │              │
└──────────────┘     └──────────────┘     └──────────────┘     └──────────────┘
                           │                     │
                           ▼                     ▼
                      ┌──────────────┐    ┌──────────────┐
                      │ CANCELLED    │    │ PAUSED       │
                      └──────────────┘    └──────────────┘
```

---

## PERMISSION MATRIX

### Role-Based Access

| Permission | CMO | MARKETING_DIR | COUNTRY_MGR | CAMPAIGN_MGR | ANALYST |
|------------|-----|---------------|-------------|--------------|---------|
| View Global Dashboard | ✓ | ✓ | ✓ | ✓ | ✓ |
| Compare Countries | ✓ | ✓ | ✓ | ✓ | ✓ |
| Create Global Campaign | ✓ | ✓ | ✗ | ✗ | ✗ |
| Create Country Campaign | ✓ | ✓ | ✓ | ✓ | ✗ |
| Edit Campaign Budget | ✓ | ✓ | ✓ | Partial | ✗ |
| Approve Campaign | ✓ | ✓ | ✗ | ✗ | ✗ |
| Delete Campaign | ✓ | ✗ | ✗ | ✗ | ✗ |
| Export Reports | ✓ | ✓ | ✓ | ✓ | ✓ |
| Manage Audiences | ✓ | ✓ | Partial | ✗ | ✗ |

### Country Access Levels

| Level | Description | Countries Access |
|-------|-------------|------------------|
| Global | All countries, full oversight | All |
| Regional | Specific region countries | Region-specific |
| Country | Single country only | Assigned country |
| ReadOnly | View only, no modifications | All or Assigned |

---

## SUMMARY

**Digital-Marketing (8 files)** ✓

**Document End**
