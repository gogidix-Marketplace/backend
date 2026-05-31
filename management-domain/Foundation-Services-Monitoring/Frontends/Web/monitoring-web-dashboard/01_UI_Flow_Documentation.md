# MONITORING DASHBOARD - UI FLOW

**Version:** 1.0
**Domain:** Management Domain
**Subdomain:** Foundation-Services-Monitoring
**Frontend:** monitoring-web-dashboard
**Framework:** React 18 + Vite + TypeScript + Material UI v5.15+
**State Management:** Zustand
**Data Fetching:** TanStack Query
**Last Updated:** 2026-02-23

---

## OVERVIEW

Foundation Services Monitoring dashboard provides real-time visibility into the health and performance of all shared infrastructure services across the Gogidix ecosystem. Monitors API Gateway, Authentication Service, Notification Service, File Storage, Cache Services, Message Queues, and AI Orchestration services.

---

## NAVIGATION

```
Monitoring Dashboard
├── 🏠 Overview
├── 📊 Services
│   ├── API Gateway
│   ├── Auth Service
│   ├── Notification Service
│   ├── File Storage
│   ├── Cache Services
│   ├── Message Queues
│   └── AI Orchestration
├── 📈 Performance
│   ├── Response Times
│   ├── Throughput
│   ├── Error Rates
│   └── Resource Usage
├── 🚨 Alerts
│   ├── Active Alerts
│   ├── Alert History
│   └── Alert Rules
└── ⚙️ Settings
    ├── Notification Channels
    ├── Alert Thresholds
    └── Dashboard Config
```

---

## USER ROLES

### Monitoring Roles

| Role | Description | Access Level |
|------|-------------|--------------|
| DEVOPS_LEAD | Full monitoring access, alert configuration | Global |
| SRE_ENGINEER | Incident response, service management | Services |
| MONITORING_ANALYST | Read-only dashboards, report generation | Read Only |
| SERVICE_OWNER | Service-specific health and alerts | Service Only |

### Role Hierarchy

```
DEVOPS_LEAD (Full Access)
├── SRE_ENGINEER
│   ├── SERVICE_OWNER (API Gateway)
│   ├── SERVICE_OWNER (Auth Service)
│   ├── SERVICE_OWNER (Notification)
│   └── SERVICE_OWNER (Storage/Cache)
└── MONITORING_ANALYST (Read Only)
```

---

## DATA FLOW ARCHITECTURE

```
┌─────────────────────────────────────────────────────────────────────────┐
│                      MONITORING DASHBOARD                               │
├─────────────────────────────────────────────────────────────────────────┤
│                                                                         │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐   │
│  │  Metrics    │  │   Health    │  │   Alerts    │  │    Logs     │   │
│  │  Service    │  │  Service    │  │  Service    │  │  Service    │   │
│  └──────┬──────┘  └──────┬──────┘  └──────┬──────┘  └──────┬──────┘   │
│         │                │                │                │           │
│         └────────────────┴────────────────┴────────────────┘           │
│                                  │                                    │
│                                  ▼                                    │
│                    ┌───────────────────────┐                         │
│                    │  Metrics Collector    │                         │
│                    └───────────┬───────────┘                         │
│                                │                                    │
│            ┌───────────────────┼───────────────────┐                 │
│            ▼                   ▼                   ▼                 │
│    ┌───────────┐       ┌───────────┐       ┌───────────┐             │
│    │Prometheus │       │ Grafana   │       │ AlertMgr  │             │
│    └───────────┘       └───────────┘       └───────────┘             │
└─────────────────────────────────────────────────────────────────────────┘
```

---

## AUTHENTICATION FLOW

```
┌────────────────┐     ┌────────────────┐     ┌────────────────┐
│ User submits   │────>│ Auth Service   │────>│ Validate       │
│ credentials    │    │ validates      │     │ credentials    │
└────────────────┘     └────────────────┘     └────────────────┘
                                                      │
                          ┌─────────────────────────┴─────────────────────┐
                          ▼                           ▼                   ▼
                   ┌─────────────┐           ┌─────────────┐      ┌─────────────┐
                   │ Valid       │           │ Invalid     │      │ MFA         │
                   │ Credentials │           │ Credentials │      │ Required    │
                   └──────┬──────┘           └──────┬──────┘      └──────┬──────┘
                          │                         │                    │
                          ▼                         ▼                    ▼
                   ┌─────────────┐           ┌─────────────┐      ┌─────────────┐
                   │ Check Role  │           │ Return      │      │ Prompt      │
                   │ Permissions │           │ Error       │      │ for MFA     │
                   └──────┬──────┘           └─────────────┘      └──────┬──────┘
                          │                                             │
                          ▼                                             ▼
                   ┌─────────────┐                              ┌─────────────┐
                   │ Load        │                              │ Verify      │
                   │ Dashboard   │                              │ MFA Code    │
                   └─────────────┘                              └──────┬──────┘
                                                                  │
                                                                  ▼
                                                           ┌─────────────┐
                                                           │ Load        │
                                                           │ Dashboard   │
                                                           └─────────────┘
```

---

## SERVICE MONITORING FLOW

```
┌────────────────┐     ┌────────────────┐     ┌────────────────┐
│ Dashboard      │────>│ Service List   │────>│ Select Service │
│ Loaded         │     │ Displayed      │     │ for Details   │
└────────────────┘     └────────────────┘     └────────────────┘
                                                      │
                      ┌─────────────────────────────┼─────────────────────────────┐
                      ▼                             ▼                             ▼
               ┌─────────────┐              ┌─────────────┐              ┌─────────────┐
               │ Health      │              │ Metrics     │              │ Recent      │
               │ Status      │              │ Charts      │              │ Incidents   │
               └──────┬──────┘              └──────┬──────┘              └──────┬──────┘
                      │                            │                            │
                      └────────────────────────────┴────────────────────────────┘
                                                       │
                                                       ▼
                                                ┌─────────────┐
                                                │ Take Action │
                                                │ (Restart/   │
                                                │  Scale/     │
                                                │  Investigate)│
                                                └─────────────┘
```

---

## ALERT MANAGEMENT FLOW

```
┌────────────────┐     ┌────────────────┐     ┌────────────────┐
│ Service Metric │────>│ Threshold     │────>│ Create Alert  │
│ Breached       │    │ Evaluation    │     │ Notification  │
└────────────────┘     └────────────────┘     └────────────────┘
                                                      │
                      ┌─────────────────────────────┴─────────────────────┐
                      ▼                                                   ▼
               ┌─────────────┐                                     ┌─────────────┐
               │ Severity:   │                                     │ Severity:   │
               │ INFO/WARNING│                                     │ ERROR/CRIT  │
               └──────┬──────┘                                     └──────┬──────┘
                      │                                                  │
                      ▼                                                  ▼
               ┌─────────────┐                                     ┌─────────────┐
               │ Log &       │                                     │ Immediate   │
               │ Display     │                                     │ Notify     │
               │ in Dashboard│                                     │ On-Call     │
               └─────────────┘                                     └──────┬──────┘
                                                                  │
                                                                  ▼
                                                           ┌─────────────┐
                                                           │ Auto-Create │
                                                           │ Incident    │
                                                           └─────────────┘
```

---

## REAL-TIME UPDATES

### WebSocket Events

| Event | Trigger | Action |
|-------|---------|--------|
| `service:status` | Service health change | Update status indicators |
| `metric:alert` | Threshold breach | Show alert notification |
| `incident:created` | New incident logged | Add to incidents list |
| `deployment:started` | Service deployment | Show deployment banner |
| `deployment:completed` | Deployment finished | Update version info |

---

## SESSION MANAGEMENT

| Setting | Value |
|---------|-------|
| Session Duration | 12 hours (operations extended) |
| Idle Timeout | 60 minutes (extended for monitoring) |
| Refresh Window | 30 minutes before expiry |
| Auto-refresh | Dashboard metrics auto-refresh every 30s |
| Concurrent Sessions | Max 3 per user |

---

## VERSION HISTORY

| Version | Date | Changes |
|---------|------|---------|
| 1.0 | 2026-02-23 | Initial UI Flow Documentation |

---

**Document End**