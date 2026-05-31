# MONITORING DASHBOARD - PAGE BY PAGE

**Version:** 1.0
**Domain:** Management Domain
**Subdomain:** Foundation-Services-Monitoring
**Last Updated:** 2026-02-23

---

## PAGE TREE

```
Monitoring Web Dashboard
│
├── (Public)
│   ├── /login                     → LoginPage
│   └── /forgot-password           → ForgotPasswordPage
│
├── (Protected - Auth Required)
│   ├── /                          → OverviewPage
│   ├── /overview                  → (default)
│   ├── /services                  → ServicesListPage
│   │   ├── /services              → Service Grid
│   │   └── /services/{id}         → ServiceDetailPage
│   ├── /alerts                    → AlertsPage
│   │   ├── /alerts                → Active Alerts
│   │   ├── /alerts/history        → AlertHistory
│   │   └── /alerts/rules          → AlertRules
│   ├── /performance               → PerformancePage
│   │   ├── /performance/response-time → ResponseTimeCharts
│   │   ├── /performance/throughput     → ThroughputCharts
│   │   └── /performance/resources       → ResourceUsage
│   └── /settings                  → SettingsPage
│       ├── /settings/notifications    → NotificationChannels
│       └── /settings/thresholds       → AlertThresholds
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
  │  MONITORING PORTAL LOGIN                                           │
  │  ┌─────────────────────────────────────────────────────────────┐   │
  │  │  📊 GOGIDIX MONITORING PORTAL                                │   │
  │  │                                                             │   │
  │  │  Email/Username: [_____________________________]           │   │
  │  │  Password:        [_____________________________] [👁️]    │   │
  │  │  ☐ Remember me                                             │   │
  │  │  [Sign In]                                                 │   │
  │  │  OR                                                        │   │
  │  │  [SSO with Microsoft]                                       │   │
  │  └─────────────────────────────────────────────────────────────┘   │
  └─────────────────────────────────────────────────────────────────────┘

User Action: Click [Sign In]
      │
      ├─ VALID CREDENTIALS ───────────────────────────────────────────┐
      │                                                                  │
      │                     API: POST /api/v1/monitoring/auth/login    │
      │                     Response: { token, user, role, permissions } │
      │                                                                  │
      │                     Store token, Load user data                │
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
  • GET /monitoring/dashboard/overview
  • GET /monitoring/services/health
  • GET /monitoring/alerts/active

Initial State:
  ┌─────────────────────────────────────────────────────────────────────┐
  │  MONITORING OVERVIEW                                              │
  │  ┌─────────────────────────────────────────────────────────────┐   │
  │  │ MONITORING │ Services │ Alerts │ Performance │ Settings ⚙️  │   │
  │  └─────────────────────────────────────────────────────────────┘   │
  │                                                                   │
  │  ┌─────────────────────────────────────────────────────────────┐   │
  │  │ SERVICES HEALTH STATUS                                        │   │
  │  │  ┌────────┐ ┌────────┐ ┌────────┐ ┌────────┐ ┌────────┐  │   │
  │  │  │ Total  │  │ Healthy│  │Degraded│  │Critical│  │Unknown │  │   │
  │  │  │   12   │  │   9    │  │   2    │  │   1    │  │   0    │  │   │
  │  │  └────────┘ └────────┘ └────────┘ └────────┘ └────────┘  │   │
  │  └─────────────────────────────────────────────────────────────┘   │
  │                                                                   │
  │  ┌─────────────────────────────────────────────────────────────┐   │
  │  │ SERVICE HEALTH GRID                                           │   │
  │  │  [Service cards with status, response time, uptime]           │   │
  │  └─────────────────────────────────────────────────────────────┘   │
  │                                                                   │
  │  ┌─────────────────────────────────────────────────────────────┐   │
  │  │ ACTIVE ALERTS                                                  │   │
  │  │  [Alert cards with severity, service, message, actions]        │   │
  │  └─────────────────────────────────────────────────────────────┘   │
  └─────────────────────────────────────────────────────────────────────┘

Page Interactions:

┌─────────────────────────────────────────────────────────────────────────┐
│ Interaction 1: Click Service Card                                    │
├─────────────────────────────────────────────────────────────────────────┤
│ User clicks API Gateway service card                                  │
│      ▼                                                                 │
│ Navigate to /services/svc_api_gateway                                 │
│ Load service detail page with metrics, logs, incidents                │
└─────────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────────────┐
│ Interaction 2: Click Alert                                            │
├─────────────────────────────────────────────────────────────────────────┤
│ User clicks critical alert                                            │
│      ▼                                                                 │
│ Open Alert Detail Modal                                               │
│ Actions: Acknowledge, Investigate, Create Incident, Dismiss           │
└─────────────────────────────────────────────────────────────────────────┘
```

---

## SERVICES PAGE FLOWS

### Service List Flow

```
ROUTE: /services
Entry Points:
  • User clicks "Services" in sidebar

Initial State:
  ┌─────────────────────────────────────────────────────────────────────┐
  │  ALL SERVICES                                                      │
  │  Filters: [Status: All ▼] [Search services...]                     │
  │                                                                   │
  │  ┌─────────────────────────────────────────────────────────────┐   │
  │  │  🟢 API Gateway          99.99%  |  45ms    |  [View Details] │   │
  │  │  🟢 Auth Service        99.95%  |  32ms    |  [View Details] │   │
  │  │  🟡 Notification Service 98.50%  |  450ms ⚠️|  [View Details] │   │
  │  │  🟢 File Storage        99.90%  |  120ms   |  [View Details] │   │
  │  │  🟢 Cache Service        99.98%  |  8ms     |  [View Details] │   │
  │  │  🟢 Message Queue        99.92%  |  15ms    |  [View Details] │   │
  │  │  🔴 AI Orchestration     94.20%  |  DOWN ❌ |  [View Details] │   │
  │  │  🟢 Email Service        99.85%  |  234ms   |  [View Details] │   │
  │  └─────────────────────────────────────────────────────────────┘   │
  └─────────────────────────────────────────────────────────────────────┘
```

### Service Detail Flow

```
ROUTE: /services/{id}
Entry Points:
  • User clicks service from list
  • User navigates directly

Initial State:
  ┌─────────────────────────────────────────────────────────────────────┐
  │  API GATEWAY SERVICE                              [Export] [Restart]│
  │  [← Back to Services]                                             │
  │                                                                   │
  │  ┌─────────────────────────────────────────────────────────────┐   │
  │  │  🟢 OPERATIONAL  |  Uptime: 99.99%  |  Last Incident: 7d ago │   │
  │  └─────────────────────────────────────────────────────────────┘   │
  │                                                                   │
  │  ┌─────────────────────────────────────────────────────────────┐   │
  │  │  RESPONSE TIME (24 HOURS)                                    │   │
  │  │  [Interactive line chart]                                     │   │
  │  └─────────────────────────────────────────────────────────────┘   │
  │                                                                   │
  │  ┌───────────────────────────────┬─────────────────────────────────┐│
  │  │  Request Distribution           │  Error Analysis                ││
  │  │  2xx: 98.5% ████████████        │  Total: 12 (24h)               ││
  │  │  4xx: 1.2% ██                  │  4xx: 10  |  5xx: 2            ││
  │  │  5xx: 0.3% █                   │  Top: 429 Rate Limit           ││
  │  └───────────────────────────────┴─────────────────────────────────┘│
  │                                                                   │
  │  ┌─────────────────────────────────────────────────────────────┐   │
  │  │ RECENT LOGS (Live)                                            │   │
  │  │  [Auto-updating log stream]                                   │   │
  │  └─────────────────────────────────────────────────────────────┘   │
  └─────────────────────────────────────────────────────────────────────┘

Page Interactions:

┌─────────────────────────────────────────────────────────────────────────┐
│ Interaction 1: Restart Service                                       │
├─────────────────────────────────────────────────────────────────────────┤
│ User clicks [Restart]                                                 │
│      ▼                                                                 │
│ Show confirmation dialog                                              │
│ "Restart API Gateway service?"                                        │
│ [Cancel] [Confirm Restart]                                            │
│      │                                                                 │
│      └─ Click Confirm ──▶ POST /monitoring/services/{id}/restart      │
│                          • Show "Restarting..." state                   │
│                          • Poll for status update                      │
│                          • Show success toast when complete            │
└─────────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────────────┐
│ Interaction 2: Change Time Range                                      │
├─────────────────────────────────────────────────────────────────────────┤
│ User changes time range selector from 24h to 7d                         │
│      ▼                                                                 │
│ Refresh metrics with new range                                         │
│ Update all charts with historical data                                 │
└─────────────────────────────────────────────────────────────────────────┘
```

---

## ALERTS PAGE FLOWS

### Active Alerts Flow

```
ROUTE: /alerts
Entry Points:
  • User clicks "Alerts" in sidebar
  • User clicks alert count badge

Initial State:
  ┌─────────────────────────────────────────────────────────────────────┐
  │  ACTIVE ALERTS                                     [Configure Rules]│
  │  Filters: [Severity: All ▼] [Service: All ▼] [Status: Active ▼]   │
  │                                                                   │
  │  ┌─────────────────────────────────────────────────────────────┐   │
  │  │ 🔴 CRITICAL | AI Orchestration        5 min ago    [⋯]       │   │
  │  │ Service is not responding. Health checks failing.          │   │
  │  │ [Acknowledge] [Investigate] [Create Incident]               │   │
  │  ├─────────────────────────────────────────────────────────────┤   │
  │  │ 🟡 WARNING | Notification Service     23 min ago   [⋯]       │   │
  │  │ Response time exceeded: 450ms (threshold: 200ms)            │   │
  │  │ [Acknowledge] [Investigate] [Dismiss]                         │   │
  │  ├─────────────────────────────────────────────────────────────┤   │
  │  │ 🟡 WARNING | Cache Service Memory      1 hour ago   [⋯]       │   │
  │  │ Memory usage at 82% (threshold: 80%)                        │   │
  │  │ [Acknowledge] [Investigate] [Dismiss]                         │   │
  │  └─────────────────────────────────────────────────────────────┘   │
  └─────────────────────────────────────────────────────────────────────┘

Page Interactions:

┌─────────────────────────────────────────────────────────────────────────┐
│ Interaction 1: Acknowledge Alert                                     │
├─────────────────────────────────────────────────────────────────────────┤
│ User clicks [Acknowledge]                                             │
│      ▼                                                                 │
│ POST /monitoring/alerts/{id}/acknowledge                               │
│ Update alert status to ACKNOWLEDGED                                   │
│ Show success toast                                                     │
│ Remove from active view (if filter excludes acknowledged)              │
└─────────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────────────┐
│ Interaction 2: Create Incident                                        │
├─────────────────────────────────────────────────────────────────────────┤
│ User clicks [Create Incident]                                          │
│      ▼                                                                 │
│ Open Incident Creation Modal                                          │
│ • Pre-fill with alert details                                         │
│ • Assign to team/member                                               │
│ • Set priority                                                        │
│ • Submit → Create incident, link alert, notify team                   │
└─────────────────────────────────────────────────────────────────────────┘
```

---

## SETTINGS PAGE FLOWS

```
ROUTE: /settings
Entry Points:
  • User clicks "Settings" in sidebar

Initial State:
  ┌─────────────────────────────────────────────────────────────────────┐
  │  MONITORING SETTINGS                                               │
  │  TABS: [Notification Channels] [Alert Thresholds] [Dashboard Config]│
  │                                                                   │
  │  NOTIFICATION CHANNELS TAB                                         │
  │  ┌─────────────────────────────────────────────────────────────┐   │
  │  │ Email Notifications                                           │   │
  │  │  ☑ Send critical alerts immediately                          │   │
  │  │  ☑ Send daily summary at 8:00 AM                             │   │
  │  │  Recipients: [ops@gogidix.com] [Add more]                    │   │
  │  └─────────────────────────────────────────────────────────────┘   │
  │                                                                   │
  │  ┌─────────────────────────────────────────────────────────────┐   │
  │  │ Slack Integration                                             │   │
  │  │  Webhook URL: [https://hooks.slack.com/...]                 │   │
  │  │  Channel: #ops-alerts                                         │   │
  │  │  ☑ Enabled                                                   │   │
  │  │  [Test Connection] [Save]                                     │   │
  │  └─────────────────────────────────────────────────────────────┘   │
  │                                                                   │
  │  ┌─────────────────────────────────────────────────────────────┐   │
  │  │ PagerDuty Integration                                         │   │
  │  │  API Key: [••••••••••••]                                      │   │
  │  │  Service Key: [••••••••••••]                                   │   │
  │  │  ☑ Enabled for critical only                                 │   │
  │  │  [Test Connection] [Save]                                     │   │
  │  └─────────────────────────────────────────────────────────────┘   │
  └─────────────────────────────────────────────────────────────────────┘
```

---

## NAVIGATION REFERENCE

| From | To | Method |
|------|-----|--------|
| Anywhere | Login | Redirect (unauthenticated) |
| Login | Overview | Auth success |
| Overview | Services | Click sidebar |
| Overview | Alerts | Click sidebar |
| Services | Service Detail | Click service card |
| Service Detail | Services | Back button |
| Alerts | Alert Rules | Click Configure Rules |
| Anywhere | Settings | Click sidebar |

---

## PERMISSION MATRIX

### Role-Based Access

| Permission | DEVOPS_LEAD | SRE_ENGINEER | MONITORING_ANALYST | SERVICE_OWNER |
|------------|-------------|--------------|-------------------|----------------|
| View All Services | ✓ | ✓ | ✓ | Own only |
| View Service Metrics | ✓ | ✓ | ✓ | Own only |
| Acknowledge Alerts | ✓ | ✓ | ✗ | Own only |
| Create/Modify Alert Rules | ✓ | ✗ | ✗ | ✗ |
| Restart Services | ✓ | Partial | ✗ | Own only |
| Configure Notifications | ✓ | ✗ | ✗ | ✗ |
| Export Reports | ✓ | ✓ | ✓ | ✓ |
| Access Logs | ✓ | ✓ | ✗ | Own only |

---

**Document End**
