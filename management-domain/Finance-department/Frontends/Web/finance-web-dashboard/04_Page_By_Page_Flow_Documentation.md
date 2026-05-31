# HQ FINANCE DASHBOARD - PAGE BY PAGE FLOW

**Version:** 1.0
**Domain:** Management Domain
**Subdomain:** Finance-Department
**Last Updated:** 2025-02-08

---

## PAGE TREE

```
HQ Finance Dashboard
├── /login
├── / (protected)
│   ├── /global-overview
│   ├── /countries
│   │   ├── /compare
│   │   └── /:code
│   ├── /revenue
│   ├── /expenses
│   ├── /budgets-forecasts
│   ├── /consolidated-reports
│   ├── /treasury
│   ├── /tax-compliance
│   └── /settings
```

---

## AUTHENTICATION FLOW

```
Login → Verify HQ Finance access → Determine region scope → Load dashboard
Regional managers: See assigned countries only
Global users: See all countries
```

---

## GLOBAL OVERVIEW FLOWS

### Load Sequence
1. Verify permissions
2. Show loading skeleton
3. Fetch GET /finance/dashboard/global
4. Fetch country summaries
5. Display aggregated metrics
6. Subscribe to WebSocket

### Metric Interaction
- Click metric → Drill down to detail page
- Export button → Generate consolidated report

---

## COUNTRY COMPARISON FLOWS

### Comparison View
- Select countries to compare
- Choose metrics to display
- Toggle period comparison
- Export comparison report

### Country Detail
- Click country card → Navigate to /countries/:code
- View full financial breakdown
- Drill down to transactions
- Export country report

---

## REPORT FLOWS

### Consolidated Reports
- P&L by country
- Balance sheet consolidation
- Cash flow statement
- Custom report builder

### Report Generation
1. Select report type
2. Choose countries to include
3. Select period
4. Choose format (PDF/Excel)
5. Generate and download

---

## PERMISSION MATRIX

| Page | VP Finance | Director | Regional | Analyst | Ops |
|------|-----------|----------|----------|---------|-----|
| Global Overview | Full | Full | Regions | Read | Read |
| Countries | Full | Full | Regions | Read | Read |
| Consolidated Reports | Full | Full | Regions | Read | All |
| Treasury | Full | Full | Regions | ✗ | Full |
| Tax & Compliance | Full | Full | Regions | Read | All |

---

## SUMMARY

### Finance Domain Complete

**Country-Finance-Dashboard (4 files)** ✓
**HQ Finance Dashboard (4 files)** ✓

**Total: 8 files for Finance Domain**

---

**Document End**
