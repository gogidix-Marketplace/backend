# HQ ADMIN DASHBOARD - UI FLOW

**Version:** 1.0
**Domain:** Management Domain
**Subdomain:** System-Administrator
**Frontend:** admin-web-dashboard
**Framework:** React 18 + Vite + TypeScript + Material UI v5.15+
**State Management:** Zustand
**Data Fetching:** TanStack Query
**Last Updated:** 2025-02-08

---

## OVERVIEW

HQ-level System Administration dashboard with centralized control over the entire Gogidix ecosystem. Provides user management, access control, system monitoring, security oversight, and audit logging across all domains and countries.

---

## NAVIGATION

```
Admin Dashboard
├── 🏠 Overview
├── 👥 Users & Access
│   ├── User Management
│   ├── Role Management
│   └── Permissions
├── 🔒 Security
│   ├── Security Events
│   ├── Threat Monitoring
│   └── Access Logs
├── 📊 Monitoring
│   ├── System Health
│   ├── Domain Status
│   └── Performance Metrics
├── 📋 Audit
│   ├── Activity Logs
│   └── Compliance Reports
└── ⚙️ Settings
    ├── System Configuration
    └── Notification Settings
```

---

## USER ROLES

### System Administrator Roles

| Role | Description | Access Level |
|------|-------------|--------------|
| SYSTEM_ADMINISTRATOR | Full system access, all permissions | Global |
| GLOBAL_SECURITY_OFFICER | Security oversight, user access management | Global |
| DEVOPS_ENGINEER | Deployment, monitoring, infrastructure | System |
| DOMAIN_ADMINISTRATOR | Domain-specific administration | Domain |
| IT_SUPPORT | Technical support, issue resolution | Read/Modify |

### Role Hierarchy

```
SYSTEM_ADMINISTRATOR (Root Access)
├── GLOBAL_SECURITY_OFFICER
│   ├── Domain Administrator (HR)
│   ├── Domain Administrator (Sales)
│   ├── Domain Administrator (Finance)
│   └── Domain Administrator (GBM/Support/Marketing)
├── DEVOPS_ENGINEER
│   └── IT_SUPPORT
└── IT_SUPPORT (Standalone)
```

---

## DATA FLOW ARCHITECTURE

```
┌─────────────────────────────────────────────────────────────────────────┐
│                        ADMIN DASHBOARD                                  │
├─────────────────────────────────────────────────────────────────────────┤
│                                                                         │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐   │
│  │   User      │  │  Security   │  │  System     │  │    Audit    │   │
│  │  Service    │  │  Service    │  │  Service    │  │  Service    │   │
│  └──────┬──────┘  └──────┬──────┘  └──────┬──────┘  └──────┬──────┘   │
│         │                │                │                │           │
│         └────────────────┴────────────────┴────────────────┘           │
│                                  │                                    │
│                                  ▼                                    │
│                    ┌───────────────────────┐                         │
│                    │    API Gateway        │                         │
│                    └───────────┬───────────┘                         │
│                                │                                    │
│            ┌───────────────────┼───────────────────┐                 │
│            ▼                   ▼                   ▼                 │
│    ┌───────────┐       ┌───────────┐       ┌───────────┐             │
│    │   Auth    │       │  Domain   │       │ Shared    │             │
│    │  Service  │       │ Services  │       │  Cores    │             │
│    └───────────┘       └───────────┘       └───────────┘             │
└─────────────────────────────────────────────────────────────────────────┘
```

---

## AUTHENTICATION FLOW

```
┌────────────────┐     ┌────────────────┐     ┌────────────────┐
│ User submits  │────>│ Auth Service   │────>│ Validate       │
│ credentials   │    │ validates      │     │ credentials    │
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
                   │ Generate    │           │ Return      │      │ Prompt      │
                   │ JWT Token   │           │ Error       │      │ for MFA     │
                   └──────┬──────┘           └─────────────┘      └──────┬──────┘
                          │                                             │
                          ▼                                             ▼
                   ┌─────────────┐                              ┌─────────────┐
                   │ Create      │                              │ Verify      │
                   │ Session     │                              │ MFA Code    │
                   └─────────────┘                              └──────┬──────┘
                                                                  │
                                                                  ▼
                                                           ┌─────────────┐
                                                           │ Generate    │
                                                           │ JWT Token   │
                                                           └─────────────┘
```

---

## SESSION MANAGEMENT

| Setting | Value |
|---------|-------|
| Session Duration | 8 hours |
| Idle Timeout | 30 minutes |
| Refresh Window | 15 minutes before expiry |
| Concurrent Sessions | Max 2 per user |
| MFA | Optional per user |

---

## USER MANAGEMENT FLOW

```
┌────────────────┐     ┌────────────────┐     ┌────────────────┐
│ Admin clicks   │────>│ User List      │────>│ Select User    │
│ "User          │     │ Loaded         │     │ Action         │
│ Management"    │     │                │     │                │
└────────────────┘     └────────────────┘     └────────────────┘
                                                      │
                      ┌─────────────────────────────┼─────────────────────────────┐
                      ▼                             ▼                             ▼
               ┌─────────────┐              ┌─────────────┐              ┌─────────────┐
               │ Create New  │              │ Edit User   │              │ Deactivate  │
               │ User        │              │            │              │ User        │
               └──────┬──────┘              └──────┬──────┘              └──────┬──────┘
                      │                            │                            │
                      ▼                            ▼                            ▼
               ┌─────────────┐              ┌─────────────┐              ┌─────────────┐
               │ Enter User  │              │ Update      │              │ Confirm     │
               │ Details     │              │ Fields      │              │ Action      │
               └──────┬──────┘              └──────┬──────┘              └──────┬──────┘
                      │                            │                            │
                      ▼                            ▼                            ▼
               ┌─────────────┐              ┌─────────────┐              ┌─────────────┐
               │ Assign      │              │ Save        │              │ Revoke      │
               │ Roles       │              │ Changes     │              │ Access      │
               └──────┬──────┘              └──────┬──────┘              └──────┬──────┘
                      │                            │                            │
                      └────────────────────────────┴────────────────────────────┘
                                                       │
                                                       ▼
                                                ┌─────────────┐
                                                │ Log Audit   │
                                                │ Entry       │
                                                └─────────────┘
```

---

## SECURITY INCIDENT FLOW

```
┌────────────────┐     ┌────────────────┐     ┌────────────────┐
│ Security Event│────>│ Severity       │────>│ Create        │
│ Detected      │    │ Analysis       │     │ Incident      │
│               │     │                │     │ Ticket        │
└────────────────┘     └────────────────┘     └────────────────┘
                                                      │
                      ┌─────────────────────────────┴─────────────────────┐
                      ▼                                                   ▼
               ┌─────────────┐                                     ┌─────────────┐
               │ LOW/MEDIUM  │                                     │ HIGH/CRITICAL│
               │ Priority   │                                     │ Priority    │
               └──────┬──────┘                                     └──────┬──────┘
                      │                                                  │
                      ▼                                                  ▼
               ┌─────────────┐                                     ┌─────────────┐
               │ Route to    │                                     │ Immediate   │
               │ Security    │                                     │ Escalation  │
               │ Officer     │                                     │ to Execs    │
               └──────┬──────┘                                     └──────┬──────┘
                      │                                                  │
                      ▼                                                  │
               ┌─────────────┐                                          │
               │ Monitor     │                                          │
               │ Resolution  │                                          │
               └─────────────┘                                          │
                                                                          ▼
                                                                   ┌─────────────┐
                                                                   │ Auto-Notify │
                                                                   │ Stakeholders│
                                                                   └─────────────┘
```

---

## REAL-TIME UPDATES

### WebSocket Events

| Event | Trigger | Action |
|-------|---------|--------|
| `user:created` | New user added | Refresh user list |
| `security:incident` | Security event | Show notification |
| `system:alert` | System issue | Display alert banner |
| `audit:entry` | Audit log created | Update audit feed |
| `domain:status` | Domain status change | Update health indicators |

---

## VERSION HISTORY

| Version | Date | Changes |
|---------|------|---------|
| 1.0 | 2025-02-08 | Initial UI Flow Documentation |

---

**Document End**
