# COO Web Dashboard — Archived

This directory has been archived. All COO dashboard functionality has been consolidated into the single multi-role executive dashboard.

**Active project:** `../ceo-web-dashboard/`

## How to run COO dashboard

```bash
cd ../ceo-web-dashboard
npm run dev:coo
```

The COO dashboard runs on **port 3013** with role auto-detection via the unified routing system.

## Reason for archival

The CFO, COO, and CTO dashboards were initially separate projects but shared 95% of their codebase. They have been consolidated into `ceo-web-dashboard/` which serves all 4 executive roles (CEO, CFO, COO, CTO) via port-based routing detection.
