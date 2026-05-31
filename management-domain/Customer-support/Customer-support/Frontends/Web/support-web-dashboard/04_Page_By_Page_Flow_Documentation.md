# HQ SUPPORT DASHBOARD - PAGE BY PAGE

**Version:** 1.0
**Domain:** Management Domain
**Subdomain:** Customer-support
**Last Updated:** 2025-02-08

---

## PAGE TREE

```
HQ Support Dashboard
├── /global-overview
├── /countries
├── /tickets
├── /teams
└── /reports
```

---

## GLOBAL OVERVIEW FLOW

### Dashboard Entry Flow

```
┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐
│ Login / Auth    │────>│ Role Check      │────>│ Global Overview │
│                 │     │ Support Role    │     │ Dashboard       │
└─────────────────┘     └─────────────────┘     └─────────────────┘
                                                        │
                        ┌───────────────────────────────┼───────────────────────────────┐
                        ▼                               ▼                               ▼
                 ┌──────────────┐               ┌──────────────┐               ┌──────────────┐
                 │ Ticket       │               │ Country      │               │ Team         │
                 │ Metrics      │               │ Performance  │               │ Performance  │
                 └──────────────┘               └──────────────┘               └──────────────┘
```

### Key Metrics Display

| Metric | Description | Data Source | Refresh Rate |
|--------|-------------|-------------|--------------|
| Total Tickets | All tickets across countries | Ticket Service | Real-time |
| Open Tickets | Tickets awaiting resolution | Ticket Service | Real-time |
| Resolved Today | Tickets resolved today | Ticket Service | Real-time |
| Avg Resolution Time | Mean time to resolve (hours) | Analytics Service | Hourly |
| Customer Satisfaction | Average CSAT rating (1-5) | Feedback Service | Real-time |
| SLA Compliance | % tickets meeting SLA | SLA Service | Real-time |

### Alert Indicators

| Alert Type | Trigger | Action |
|------------|---------|--------|
| SLA Breach | Resolution time exceeded | Escalate ticket |
| High Volume | Ticket count > threshold | Assign more agents |
| Low Satisfaction | CSAT < 3.5 | Review and retrain |
| Queue Buildup | Wait time > 15 min | Notify team lead |

---

## COUNTRY COMPARISON FLOW

### Country Selection Flow

```
┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐
│ Click Countries │────>│ Select Country  │────>│ View Country    │
│ Navigation      │     │ or Compare      │     │ Details         │
└─────────────────┘     └─────────────────┘     └─────────────────┘
                                                           │
                        ┌──────────────────────────────────┼──────────────────────────────────┐
                        ▼                                  ▼                                  ▼
                 ┌──────────────┐                  ┌──────────────┐                  ┌──────────────┐
                 │ Volume       │                  │ Performance  │                  │ SLA Status   │
                 │ Metrics      │                  │ Metrics      │                  │ & Breaches   │
                 └──────────────┘                  └──────────────┘                  └──────────────┘
```

### Country Comparison Metrics

| Category | Metrics | Visualization |
|----------|---------|---------------|
| Volume | Total, Open, Resolved, Escalated | Bar charts, Trend lines |
| Performance | Resolution time, First response, CSAT | Line charts, Heat maps |
| SLA | Compliance rate, Breaches, At-risk | Progress bars, Alerts |
| Agents | Active, Available, Utilization | Status cards, Gauges |

### Multi-Country Comparison View

```
┌─────────────────────────────────────────────────────────────────┐
│                    Country Comparison Matrix                    │
├────────────┬──────────┬──────────┬──────────┬──────────┬─────────┤
│ Country    │ Tickets  │ Resolved │ Avg Time │ CSAT     │ SLA %   │
├────────────┼──────────┼──────────┼──────────┼──────────┼─────────┤
│ 🇳🇬 Nigeria │ 1,245    │ 982      │ 3.8h     │ 4.4 ★★★★ │ 95.2%   │
│ 🇰🇪 Kenya   │ 876      │ 723      │ 4.5h     │ 4.2 ★★★★ │ 93.8%   │
│ 🇿🇦 S.Africa│ 1,102    │ 945      │ 4.1h     │ 4.5 ★★★★★│ 96.1%   │
│ 🇬🇭 Ghana   │ 534      │ 489      │ 4.2h     │ 4.1 ★★★★ │ 92.5%   │
│ 🇺🇬 Uganda  │ 412      │ 367      │ 4.8h     │ 3.9 ★★★  │ 89.3%   │
└────────────┴──────────┴──────────┴──────────┴──────────┴─────────┘
```

---

## TICKET MANAGEMENT FLOW

### Ticket List View Flow

```
┌──────────────┐     ┌──────────────┐     ┌──────────────┐     ┌──────────────┐
│ Click Tickets │────>│ Apply Filters │────>│ Sort/        │────>│ Paginate     │
│ Navigation   │     │ (Status,     │     │ Search       │     │ Results      │
│              │     │ Priority,    │     │              │     │              │
│              │     │ Country)     │     │              │     │              │
└──────────────┘     └──────────────┘     └──────────────┘     └──────────────┘
                                                                          │
                                                                          ▼
                                                                   ┌──────────────┐
                                                                   │ Select       │
                                                                   │ Ticket       │
                                                                   └──────────────┘
```

### Ticket Detail View Flow

```
┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐
│ Select Ticket   │────>│ View Full       │────>│ Available       │
│ from List       │     │ Ticket Details  │     │ Actions         │
└─────────────────┘     └─────────────────┘     └─────────────────┘
                                                        │
                        ┌───────────────────────────────┼───────────────────────────────┐
                        ▼                               ▼                               ▼
                 ┌──────────────┐               ┌──────────────┐               ┌──────────────┐
                 │ Assign       │               │ Update       │               │ Escalate/    │
                 │ Agent/Team   │               │ Status       │               │ Resolve      │
                 └──────────────┘               └──────────────┘               └──────────────┘
```

### Ticket Status Flow

```
┌──────────────┐     ┌──────────────┐     ┌──────────────┐     ┌──────────────┐
│ NEW          │────>│ OPEN         │────>│ PENDING      │────>│ RESOLVED     │
│              │     │              │     │              │     │              │
└──────────────┘     └──────────────┘     └──────────────┘     └──────────────┘
                           │                     │                     │
                           ▼                     ▼                     ▼
                    ┌──────────────┐    ┌──────────────┐    ┌──────────────┐
                    │ ESCALATED    │    │ CUSTOMER     │    │ CLOSED       │
                    │              │    │ RESPONSE     │    │              │
                    └──────────────┘    └──────────────┘    └──────────────┘
```

### Ticket Actions

| Action | Description | Permissions |
|--------|-------------|-------------|
| View | View ticket details | All support roles |
| Assign | Assign to agent/team | Team Lead, Manager |
| Reassign | Change assignment | Team Lead, Manager |
| Update Status | Change ticket status | All support roles |
| Add Note | Add internal note | All support roles |
| Respond | Send customer response | Assigned agent |
| Escalate | Escalate to higher tier | All support roles |
| Resolve | Mark as resolved | Assigned agent |
| Close | Close ticket | Assigned agent, Manager |

---

## TEAM MANAGEMENT FLOW

### Team List View

```
┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐
│ Click Teams     │────>│ Filter by       │────>│ View Team       │
│ Navigation      │     │ Country/Type    │     │ List            │
└─────────────────┘     └─────────────────┘     └─────────────────┘
                                                          │
                        ┌─────────────────────────────────┼─────────────────────────────────┐
                        ▼                                 ▼                                 ▼
                 ┌──────────────┐                 ┌──────────────┐                 ┌──────────────┐
                 │ Capacity     │                 │ Performance  │                 │ Agent        │
                 │ Status       │                 │ Metrics      │                 │ List         │
                 └──────────────┘                 └──────────────┘                 └──────────────┘
```

### Team Performance Monitoring

| Metric | Description | Target |
|--------|-------------|--------|
| Tickets Resolved | Total resolved in period | > 80/agent/day |
| Avg Resolution Time | Mean resolution time | < 4 hours |
| First Response | Time to first response | < 30 minutes |
| CSAT Score | Customer satisfaction | > 4.0 |
| SLA Compliance | % meeting SLA | > 95% |
| Agent Utilization | % active time | 70-85% |

### Agent Capacity Indicators

| Status | Color | Meaning |
|--------|-------|---------|
| Available | Green | Ready for assignments |
| Busy | Yellow | Handling active tickets |
| Unavailable | Red | Not accepting new tickets |
| Offline | Gray | Not logged in |

---

## SLA MONITORING FLOW

### SLA Dashboard Flow

```
┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐
│ SLA Status      │────>│ Filter by       │────>│ View SLA        │
│ View            │     │ Country/Policy  │     │ Details         │
└─────────────────┘     └─────────────────┘     └─────────────────┘
                                                          │
                        ┌─────────────────────────────────┼─────────────────────────────────┐
                        ▼                                 ▼                                 ▼
                 ┌──────────────┐                 ┌──────────────┐                 ┌──────────────┐
                 │ At Risk      │                 │ Breached     │                 │ Compliance   │
                 │ Tickets      │                 │ Tickets      │                 │ Report       │
                 └──────────────┘                 └──────────────┘                 └──────────────┘
```

### SLA Policy Tiers

| Tier | Response Target | Resolution Target | Customer Type |
|------|-----------------|-------------------|---------------|
| Enterprise | 15 minutes | 4 hours | Enterprise accounts |
| Premium | 30 minutes | 24 hours | Premium accounts |
| Standard | 2 hours | 48 hours | Standard accounts |
| Basic | 4 hours | 72 hours | Basic accounts |

### SLA Breach Handling

```
┌──────────────┐     ┌──────────────┐     ┌──────────────┐     ┌──────────────┐
│ SLA Warning  │────>│ Notify Team  │────>│ Escalate if  │────>│ Log Breach   │
│ Triggered     │     │ Lead         │     │ Critical     │     │ & Report     │
└──────────────┘     └──────────────┘     └──────────────┘     └──────────────┘
```

---

## PERMISSION MATRIX

### Role-Based Access

| Permission | GLOBAL_DIR | REGIONAL_MGR | TEAM_LEAD | AGENT | ANALYST |
|------------|------------|--------------|-----------|-------|---------|
| View Global Dashboard | ✓ | ✓ | ✓ | ✓ | ✓ |
| View All Countries | ✓ | Region | Country | Country | ✓ |
| Create Tickets | ✓ | ✓ | ✓ | ✓ | ✗ |
| Assign Tickets | ✓ | ✓ | Team only | Self only | ✗ |
| Reassign Tickets | ✓ | ✓ | Team only | ✗ | ✗ |
| Escalate Tickets | ✓ | ✓ | ✓ | ✓ | ✗ |
| Resolve Tickets | ✓ | ✓ | ✓ | ✓ | ✗ |
| View Team Performance | ✓ | Region | Team | Self | ✓ |
| Manage Teams | ✓ | Region | ✗ | ✗ | ✗ |
| View SLA Breaches | ✓ | ✓ | ✓ | ✓ | ✓ |
| Export Reports | ✓ | ✓ | ✓ | ✗ | ✓ |
| Manage SLA Policies | ✓ | ✗ | ✗ | ✗ | ✗ |

### Country Access Levels

| Level | Description | Countries Access |
|-------|-------------|------------------|
| Global | All countries, full oversight | All |
| Regional | Specific region countries | Region-specific |
| Country | Single country only | Assigned country |
| ReadOnly | View only, no modifications | All or Assigned |

---

## SUMMARY

**Customer-Support (8 files)** ✓

**Document End**
