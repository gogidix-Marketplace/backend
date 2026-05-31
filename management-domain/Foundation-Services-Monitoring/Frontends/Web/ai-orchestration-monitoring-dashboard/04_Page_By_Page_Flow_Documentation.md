# 04 - Page by Page Flow Documentation

## AI & Orchestration Monitoring Dashboard

---

## Table of Contents

1. [Page Tree Structure](#1-page-tree-structure)
2. [Navigation Flows](#2-navigation-flows)
3. [User Journey Maps](#3-user-journey-maps)
4. [Page Flows](#4-page-flows)
5. [Error Handling Flows](#5-error-handling-flows)
6. [Loading States](#6-loading-states)

---

## 1. Page Tree Structure

```
AI & Orchestration Monitoring Dashboard
│
├── /login
│   ├── Login Page
│   ├── Forgot Password
│   └── Reset Password
│
├── / (authenticated)
│   │
│   ├── /overview
│   │   ├── Dashboard Overview
│   │   └── Executive Summary (Executive role only)
│   │
│   ├── /ai-services
│   │   ├── /ai-services/catalog
│   │   │   ├── Service List (all 48 AI services)
│   │   │   └── /ai-services/catalog/:serviceId
│   │   │       ├── Service Overview
│   │   │       ├── Service Metrics
│   │   │       ├── Service Logs
│   │   │       ├── Service Models
│   │   │       └── Service Configuration
│   │   │
│   │   ├── /ai-services/models
│   │   │   ├── Model List
│   │   │   └── /ai-services/models/:modelId
│   │   │       ├── Model Overview
│   │   │       ├── Model Performance
│   │   │       ├── Model Drift
│   │   │       └── Model Retraining
│   │   │
│   │   ├── /ai-services/training
│   │   │   ├── Training Jobs List
│   │   │   ├── /ai-services/training/:jobId
│   │   │   │   ├── Job Overview
│   │   │   │   ├── Job Metrics
│   │   │   │   └── Job Logs
│   │   │   └── /ai-services/training/new
│   │   │       └── Create Training Job
│   │   │
│   │   ├── /ai-services/experiments
│   │   │   ├── Experiments List
│   │   │   ├── /ai-services/experiments/:experimentId
│   │   │   │   ├── Experiment Overview
│   │   │   │   ├── Experiment Results
│   │   │   │   └── Experiment Configuration
│   │   │   └── /ai-services/experiments/new
│   │   │       └── Create Experiment
│   │   │
│   │   └── /ai-services/feature-store
│   │       ├── Feature Groups
│   │       ├── /ai-services/feature-store/:groupId
│   │       │   ├── Feature Group Details
│   │       │   ├── Data Quality
│   │       │   └── Lineage
│   │       └── Feature Statistics
│   │
│   ├── /orchestration
│   │   ├── /orchestration/workflows
│   │   │   ├── Active Workflows
│   │   │   ├── Completed Workflows
│   │   │   ├── Failed Workflows
│   │   │   └── /orchestration/workflows/:workflowId
│   │   │       ├── Workflow Overview
│   │   │       ├── Workflow Steps
│   │   │       ├── Workflow History
│   │   │       └── Workflow Configuration
│   │   │
│   │   ├── /orchestration/audit-trail
│   │   │   ├── Audit Log Search
│   │   │   ├── Timeline View
│   │   │   ├── Change History
│   │   │   └── /orchestration/audit-trail/:logId
│   │   │       └── Audit Log Details
│   │   │
│   │   ├── /orchestration/onboarding
│   │   │   ├── User Onboarding Tracker
│   │   │   ├── Service Onboarding Tracker
│   │   │   └── /orchestration/onboarding/:trackerId
│   │   │       └── Onboarding Details
│   │   │
│   │   └── /orchestration/status
│   │       ├── Service Status Grid
│   │       ├── Status History
│   │       └── Status Subscriptions
│   │
│   ├── /transactions
│   │   ├── /transactions/sagas
│   │   │   ├── Active Sagas
│   │   │   ├── Saga History
│   │   │   └── /transactions/sagas/:sagaId
│   │   │       ├── Saga Overview
│   │   │       ├── Saga Events
│   │   │       ├── Saga Visualization
│   │   │       └── Saga Actions
│   │   │
│   │   ├── /transactions/event-log
│   │   │   ├── Event Search
│   │   │   ├── Event Visualization
│   │   │   ├── Event Replay
│   │   │   └── /transactions/event-log/:eventId
│   │   │       └── Event Details
│   │   │
│   │   ├── /transactions/state-machines
│   │   │   ├── State Machines List
│   │   │   ├── /transactions/state-machines/:smId
│   │   │   │   ├── State Machine Overview
│   │   │   │   ├── Current State
│   │   │   │   ├── State Diagram
│   │   │   │   └── Transition History
│   │   │   └── State Templates
│   │   │
│   │   └── /transactions/recovery
│   │       ├── Failed Transactions
│   │       ├── Manual Recovery Queue
│   │       ├── Recovery Actions
│   │       └── Recovery History
│   │
│   ├── /tracking
│   │   ├── /tracking/metrics
│   │   │   ├── Real-time Dashboard
│   │   │   ├── Metric Explorer
│   │   │   ├── Custom Metrics
│   │   │   └── /tracking/metrics/:metricId
│   │   │       └── Metric Details
│   │   │
│   │   ├── /tracking/performance
│   │   │   ├── Response Time Analysis
│   │   │   ├── Throughput Analysis
│   │   │   ├── Error Analysis
│   │   │   └── Capacity Planning
│   │   │
│   │   ├── /tracking/alerts
│   │   │   ├── Alert Rules List
│   │   │   ├── Alert History
│   │   │   ├── Alert Routing
│   │   │   ├── /tracking/alerts/:ruleId
│   │   │   │   └── Alert Rule Configuration
│   │   │   └── /tracking/alerts/new
│   │   │       └── Create Alert Rule
│   │   │
│   │   └── /tracking/reports
│   │       ├── Scheduled Reports
│   │       ├── Custom Reports
│   │       ├── Report Templates
│   │       ├── /tracking/reports/:reportId
│   │       │   └── Report Details
│   │       └── /tracking/reports/new
│   │           └── Create Report
│   │
│   └── /settings
│       ├── /settings/dashboard
│       │   ├── Layout Customization
│       │   ├── Widget Management
│       │   ├── Theme Settings
│       │   └── Preferences
│       │
│       ├── /settings/alerts
│       │   ├── Notification Channels
│       │   ├── Alert Escalation
│       │   ├── Alert Scheduling
│       │   └── Alert Templates
│       │
│       ├── /settings/users
│       │   ├── User List
│       │   ├── Roles and Permissions
│       │   ├── /settings/users/:userId
│       │   │   └── User Details
│       │   └── /settings/users/new
│       │       └── Create User
│       │
│       ├── /settings/api-keys
│       │   ├── API Keys List
│       │   ├── Usage Analytics
│       │   ├── /settings/api-keys/:keyId
│       │   │   └── API Key Details
│       │   └── /settings/api-keys/new
│       │       └── Create API Key
│       │
│       └── /settings/account
│           ├── Profile Settings
│           ├── Security Settings
│           └── Activity Log
```

---

## 2. Navigation Flows

### 2.1 Primary Navigation Flow

```
┌─────────────┐
│   Login     │
└──────┬──────┘
       │
       ▼
┌──────────────────────────────────────────────────────────┐
│                    Dashboard Overview                      │
│  ┌────────┐  ┌────────┐  ┌────────┐  ┌────────┐         │
│  │ Health │  │Metrics │  │ Alerts │  │ Reports│         │
│  └────────┘  └────────┘  └────────┘  └────────┘         │
└───┬───────────┬───────────┬───────────┬──────────┬───────┘
    │           │           │           │          │
    ▼           ▼           ▼           ▼          ▼
┌───────┐  ┌───────┐  ┌───────┐  ┌───────┐  ┌─────────┐
│   AI  │  │ Orch. │  │Trans. │  │Track. │  │Settings │
│Services│  │       │  │       │  │       │  │         │
└───┬───┘  └───┬───┘  └───┬───┘  └───┬───┘  └────┬────┘
    │          │          │          │          │
    ▼          ▼          ▼          ▼          ▼
┌───────┐  ┌───────┐  ┌───────┐  ┌───────┐  ┌─────────┐
│Catalog│  │Workfl. │  │ Sagas │  │Metrics│  │Dashboard│
│Models │  │Audit   │  │Events │  │Alerts │  │Users    │
│Training│  │Onboard│  │State  │  │Reports│  │API Keys │
│Exp.   │  │Status  │  │Recovery│       │  │Account  │
└───────┘  └───────┘  └───────┘  └───────┘  └─────────┘
```

### 2.2 Breadcrumb Navigation Flow

``Dashboard Overview > AI Services > Service Catalog > ai-inference-service

Path:
/overview → /ai-services → /ai-services/catalog → /ai-services/catalog/svc-ai-inference-001

Breadcrumb Navigation:
┌─────────────────────────────────────────────────────────────┐
│ Home ▸ AI Services ▸ Service Catalog ▸ ai-inference-service │
└─────────────────────────────────────────────────────────────┘
```

### 2.3 Deep Link Navigation Flow

```
External Link:
https://dashboard.gogidix.com/ai-services/catalog/svc-ai-inference-001?tab=metrics

Flow:
1. User not authenticated → Redirect to /login
2. After login → Redirect to intended URL
3. User authenticated → Load service details
4. Open Metrics tab by default (from query param)
```

---

## 3. User Journey Maps

### 3.1 System Administrator - Daily Monitoring Journey

```
Start: 9:00 AM Daily Check-in
  │
  ▼
┌────────────────────────────────────────────────────────────┐
│ 1. Dashboard Overview                                      │
│    - Review health score (target: >95%)                    │
│    - Check active alerts (address critical first)          │
│    - Scan quick metrics for anomalies                     │
└───────────────────────────┬────────────────────────────────┘
                            │ Anomaly Detected
                            ▼
┌────────────────────────────────────────────────────────────┐
│ 2. AI Services → Service Catalog                           │
│    - Navigate to problematic service                       │
│    - Review detailed metrics and logs                      │
│    - Take corrective action (scale, restart, etc.)         │
└───────────────────────────┬────────────────────────────────┘
                            │
                            ▼
┌────────────────────────────────────────────────────────────┐
│ 3. Orchestration → Active Workflows                        │
│    - Check for stuck or failed workflows                   │
│    - Review workflow execution times                       │
│    - Address any bottlenecks                               │
└───────────────────────────┬────────────────────────────────┘
                            │
                            ▼
┌────────────────────────────────────────────────────────────┐
│ 4. Transactions → Saga Monitor                             │
│    - Review active sagas                                   │
│    - Check for compensation actions                        │
│    - Monitor transaction health                            │
└───────────────────────────┬────────────────────────────────┘
                            │
                            ▼
┌────────────────────────────────────────────────────────────┐
│ 5. Tracking → Real-time Metrics                            │
│    - Monitor live metrics stream                           │
│    - Verify system stability                              │
│    - Review capacity planning                             │
└───────────────────────────┬────────────────────────────────┘
                            │
                            ▼
┌────────────────────────────────────────────────────────────┐
│ 6. Settings → Alert Management                             │
│    - Review and update alert rules                         │
│    - Adjust thresholds if needed                           │
│    - Configure notifications for off-hours                 │
└────────────────────────────────────────────────────────────┘
```

### 3.2 AI Engineer - Model Development Journey

```
Start: New Model Development Request
  │
  ▼
┌────────────────────────────────────────────────────────────┐
│ 1. AI Services → Feature Store                             │
│    - Browse available feature groups                      │
│    - Review feature statistics and quality                │
│    - Select features for new model                        │
└───────────────────────────┬────────────────────────────────┘
                            │
                            ▼
┌────────────────────────────────────────────────────────────┐
│ 2. AI Services → Training Jobs                            │
│    - Create new training job                              │
│    - Configure hyperparameters                            │
│    - Start training                                       │
└───────────────────────────┬────────────────────────────────┘
                            │
                            ▼
┌────────────────────────────────────────────────────────────┐
│ 3. Monitor Training Progress                               │
│    - Watch training metrics in real-time                   │
│    - Review logs for issues                               │
│    - Wait for completion                                  │
└───────────────────────────┬────────────────────────────────┘
                            │ Training Complete
                            ▼
┌────────────────────────────────────────────────────────────┐
│ 4. AI Services → Models                                    │
│    - Review new model performance                         │
│    - Compare with baseline model                          │
│    - Check for overfitting/underfitting                   │
└───────────────────────────┬────────────────────────────────┘
                            │ Model Approved
                            ▼
┌────────────────────────────────────────────────────────────┐
│ 5. AI Services → Experiments                               │
│    - Create A/B test experiment                           │
│    - Configure traffic split                              │
│    - Start experiment                                     │
└───────────────────────────┬────────────────────────────────┘
                            │
                            ▼
┌────────────────────────────────────────────────────────────┐
│ 6. Monitor Experiment Results                              │
│    - Track experiment metrics                             │
│    - Review confidence intervals                          │
│    - Declare winner when significant                      │
└───────────────────────────┬────────────────────────────────┘
                            │ Winner Declared
                            ▼
┌────────────────────────────────────────────────────────────┐
│ 7. Deploy New Model                                        │
│    - Gradually roll out to production                     │
│    - Monitor performance post-deployment                  │
│    - Retire old model                                     │
└────────────────────────────────────────────────────────────┘
```

### 3.3 DevOps Engineer - Incident Response Journey

```
Start: PagerDuty Alert Received
  │
  ▼
┌────────────────────────────────────────────────────────────┐
│ 1. Quick Access Alert Notification                         │
│    - Read alert details                                   │
│    - Click direct link to affected service                │
└───────────────────────────┬────────────────────────────────┘
                            │
                            ▼
┌────────────────────────────────────────────────────────────┐
│ 2. Service Detail Page                                     │
│    - Review current metrics and status                    │
│    - Check recent logs for errors                         │
│    - Identify root cause                                  │
└───────────────────────────┬────────────────────────────────┘
                            │ Root Cause Identified
                            ▼
┌────────────────────────────────────────────────────────────┐
│ 3. Take Immediate Action                                   │
│    - Scale up service to handle load                      │
│    - OR Roll back recent deployment                       │
│    - OR Restart affected instances                        │
└───────────────────────────┬────────────────────────────────┘
                            │
                            ▼
┌────────────────────────────────────────────────────────────┐
│ 4. Monitor Recovery                                        │
│    - Watch metrics normalize                              │
│    - Verify error rates decrease                          │
│    - Confirm service is healthy                           │
└───────────────────────────┬────────────────────────────────┘
                            │
                            ▼
┌────────────────────────────────────────────────────────────┐
│ 5. Post-Incident Actions                                   │
│    - Resolve alert in dashboard                           │
│    - Document incident in audit trail                     │
│    - Create follow-up task for root fix                   │
└────────────────────────────────────────────────────────────┘
```

---

## 4. Page Flows

### 4.1 Login Flow

```
┌────────────────────────────────────────────────────────────┐
│                        Login Page                          │
├────────────────────────────────────────────────────────────┤
│                                                            │
│                    ┌──────────┐                           │
│                    │   Logo   │                           │
│                    └──────────┘                           │
│                                                            │
│                 AI & Orchestration                         │
│                    Monitoring                              │
│                                                            │
│  ┌────────────────────────────────────────────────┐      │
│  │  Email or Username                             │      │
│  └────────────────────────────────────────────────┘      │
│                                                            │
│  ┌────────────────────────────────────────────────┐      │
│  │  Password                                 [👁] │      │
│  └────────────────────────────────────────────────┘      │
│                                                            │
│  [✓] Remember me        Forgot password?                 │
│                                                            │
│  ┌────────────────────────────────────────────────┐      │
│  │              Sign In                            │      │
│  └────────────────────────────────────────────────┘      │
│                                                            │
│            Or sign in with  [SSO]    [Google]             │
│                                                            │
└────────────────────────────────────────────────────────────┘
              │
              │ Enter credentials and submit
              ▼
┌────────────────────────────────────────────────────────────┐
│                      Validation                            │
├────────────────────────────────────────────────────────────┤
│  ┌─────────────────┐    ┌─────────────────┐              │
│  │ Valid Credentials│    │ Invalid Creds   │              │
│  └────────┬────────┘    └────────┬────────┘              │
│           │                      │                         │
│           ▼                      ▼                         │
│  ┌─────────────────┐    ┌─────────────────┐              │
│  │ Generate Token  │    │ Show Error Msg  │              │
│  │ Store Session   │    │ Stay on Login   │              │
│  └────────┬────────┘    └─────────────────┘              │
│           │                                                  │
│           ▼                                                  │
│  ┌─────────────────┐                                      │
│  │ Redirect to      │                                      │
│  │ Dashboard        │                                      │
│  └─────────────────┘                                      │
└────────────────────────────────────────────────────────────┘
```

### 4.2 Dashboard Overview Flow

```
┌────────────────────────────────────────────────────────────┐
│                    Dashboard Overview                       │
├────────────────────────────────────────────────────────────┤
│                                                            │
│  ┌────────────┐ ┌────────────┐ ┌────────────┐            │
│  │   Health   │ │  Services  │ │   Alerts   │            │
│  │   Score    │ │  Summary   │ │  Summary   │            │
│  │   Click    │ │   Click    │ │   Click    │            │
│  └────────────┘ └────────────┘ └────────────┘            │
│         │              │              │                   │
│         ▼              ▼              ▼                   │
│  ┌────────────────────────────────────────┐              │
│  │    Filter services by health           │              │
│  │    Navigate to service details         │              │
│  │    Navigate to alert details           │              │
│  └────────────────────────────────────────┘              │
│                                                            │
│  ┌────────────────────────────────────────────────┐      │
│  │         Service Health Matrix                  │      │
│  │    Click on category to see services           │      │
│  └────────────────────────────────────────────────┘      │
│                        │                                   │
│                        ▼                                   │
│           ┌────────────────────────┐                      │
│           │ AI Services Category   │                      │
│           │ (Filtered view)        │                      │
│           └────────────────────────┘                      │
│                                                            │
│  ┌─────────────────────────┐ ┌──────────────────┐        │
│  │  Active Workflows       │ │ Recent Trans.    │        │
│  │  [View All]             │ │ [View All]       │        │
│  └─────────────────────────┘ └──────────────────┘        │
│          │                             │                  │
│          ▼                             ▼                  │
│  ┌──────────────┐          ┌──────────────────┐           │
│  │ Workflow     │          │ Saga Monitor     │           │
│  │ List Page    │          │ List Page        │           │
│  └──────────────┘          └──────────────────┘           │
│                                                            │
└────────────────────────────────────────────────────────────┘
```

### 4.3 Service Catalog Flow

```
┌────────────────────────────────────────────────────────────┐
│                 AI Services Catalog                         │
├────────────────────────────────────────────────────────────┤
│  Filters: [Core ▼] [Healthy ▼] Sort: [Name ▲]              │
│                                                            │
│  ┌────────────────────────────────────────────────┐       │
│  │ ┌────┐ ai-inference-service            [⋮]    │       │
│  │ │ 🤖 │ Click to view details              │       │
│  │ └────┘                                    │       │
│  └────────────────────────────────────────────────┘       │
│                        │                                   │
│                        ▼                                   │
│  ┌──────────────────────────────────────────────────┐     │
│  │        Service Detail Page                        │     │
│  │ ┌────┐ ai-inference-service               [Edit] │     │
│  │ │ 🤖 │ AI Inference Engine v2.1.0          [Restart]│ │ │
│  │ └────┘                                         │     │
│  │ ● Operational  Uptime: 99.97%                   │     │
│  ├──────────────────────────────────────────────────┤     │
│  │ [Overview] [Metrics] [Logs] [Models] [Config]    │     │
│  ├──────────────────────────────────────────────────┤     │
│  │ ┌──────────┐ ┌──────────┐ ┌──────────┐         │     │
│  │ │ Requests │ │  Latency │ │  Errors  │         │     │
│  │ └──────────┘ └──────────┘ └──────────┘         │     │
│  │                                                  │     │
│  │  Request Volume Chart                            │     │
│  │                                                  │     │
│  │ ┌──────────────────┐ ┌──────────────────┐       │     │
│  │ │Resource Utiliz.  │ │ Active Models    │       │     │
│  │ └──────────────────┘ └──────────────────┘       │     │
│  │                                                  │     │
│  │ Recent Alerts                                   │     │
│  └──────────────────────────────────────────────────┘     │
│                                                            │
│  From Service Detail:                                      │
│  │                                                          │
│  ├─▶ Metrics Tab → View detailed metrics, charts          │
│  ├─▶ Logs Tab → View and search service logs              │
│  ├─▶ Models Tab → View deployed models                     │
│  └─▶ Config Tab → Edit service configuration              │
│                                                            │
└────────────────────────────────────────────────────────────┘
```

### 4.4 Workflow Management Flow

```
┌────────────────────────────────────────────────────────────┐
│                 Active Workflows Page                       │
├────────────────────────────────────────────────────────────┤
│  Status: [All ▼]  Type: [All ▼]  Search: [            ]   │
│                                                            │
│  ┌────────────────────────────────────────────────┐       │
│  │ wf-onboarding-user-12345               [Running]│      │
│  │ ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━ 43%   │      │
│  │ Click to view details →                      │      │
│  └────────────────────────────────────────────────┘       │
│                        │                                   │
│                        ▼                                   │
│  ┌──────────────────────────────────────────────────┐     │
│  │         Workflow Detail Page                      │     │
│  │ wf-onboarding-user-12345               [Running]  │     │
│  │ ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━ 43%     │     │
│  │ Type: User Onboarding  Started: 5 min ago         │     │
│  ├──────────────────────────────────────────────────┤     │
│  │ ┌────┐ ┌────┐ ┌────┐ ┌────┐ ┌────┐ ┌────┐      │     │
│  │ │Create│→│Send │→│Verify│→│Setup│→│Complete│     │     │
│  │ │Acct │ │Email│ │Email│ │Prof │ │        │     │     │
│  │ │ ✓  │ │ ✓  │ │ ⏳  │ │    │ │        │     │     │
│  │ └────┘ └────┘ └────┘ └────┘ └────┘ └────┘      │     │
│  │                                                  │     │
│  │ Click on step to view details →                  │     │
│  ├──────────────────────────────────────────────────┤     │
│  │ Actions:                                          │     │
│  │  [View Details] [Pause] [Cancel] [Retry Failed]  │     │
│  └──────────────────────────────────────────────────┘     │
│                                                            │
│  Available Actions by Status:                              │
│  ┌─────────────────────────────────────────────────┐       │
│  │ Running    │ Pause, Cancel, View Step Details  │       │
│  │ Paused     │ Resume, Cancel                    │       │
│  │ Failed     │ View Error, Retry, Manual Recovery│       │
│  │ Completed  │ View Details, View History        │       │
│  └─────────────────────────────────────────────────┘       │
│                                                            │
└────────────────────────────────────────────────────────────┘
```

### 4.5 Saga Monitor Flow

```
┌────────────────────────────────────────────────────────────┐
│                    Saga Monitor Page                        │
├────────────────────────────────────────────────────────────┤
│  Status: [All ▼]  Search: [                           ]   │
│                                                            │
│  ┌────────────────────────────────────────────────┐       │
│  │ saga-order-456789                    [Active]   │      │
│  │ Click to view details →                      │      │
│  └────────────────────────────────────────────────┘       │
│                        │                                   │
│                        ▼                                   │
│  ┌──────────────────────────────────────────────────┐     │
│  │            Saga Detail Page                       │     │
│  │ saga-order-456789                        [Active] │     │
│  │ ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━          │     │
│  │ Order Processing Saga  Started: 2 min ago          │     │
│  ├──────────────────────────────────────────────────┤     │
│  │ ┌──────────┐    ┌──────────┐    ┌──────────┐    │     │
│  │ │Create    │───▶│Validate  │───▶│Process   │    │     │
│  │ │Order     │    │Payment   │    │Order     │    │     │
│  │ │  ✓ Done  │    │  ✓ Done  │    │  ⏳ Pending│    │     │
│  │ └──────────┘    └──────────┘    └──────────┘    │     │
│  │                                     │              │     │
│  │                                     ▼              │     │
│  │                            ┌──────────┐           │     │
│  │                            │Ship      │           │     │
│  │                            │          │           │     │
│  │                            └──────────┘           │     │
│  ├──────────────────────────────────────────────────┤     │
│  │ [Events] [History] [Actions]                       │     │
│  ├──────────────────────────────────────────────────┤     │
│  │ Current Action: Process Order                       │     │
│  │ Participant: order-processing-service              │     │
│  │                                                    │     │
│  │ Actions:                                           │     │
│  │  [View Events] [Force Complete] [Trigger Comp.]   │     │
│  └──────────────────────────────────────────────────┘     │
│                                                            │
│  For Failed Sagas:                                        │
│  ┌──────────────────────────────────────────────────┐     │
│  │ saga-refund-789012                    [Failed]   │     │
│  │ ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━          │     │
│  │ Failed at: Process Refund (Step 3/5)              │     │
│  │ Error: External payment API timeout               │     │
│  │                                                    │     │
│  │ Compensating Actions Triggered:                   │     │
│  │  • ✓ Lock released                                │     │
│  │  • ⏳ Transaction rollback in progress             │     │
│  │                                                    │     │
│  │ [View Error] [Retry Compensation] [Manual Recovery]│     │
│  └──────────────────────────────────────────────────┘     │
│                                                            │
└────────────────────────────────────────────────────────────┘
```

### 4.6 Alert Management Flow

```
┌────────────────────────────────────────────────────────────┐
│                    Alert Navigation                         │
├────────────────────────────────────────────────────────────┤
│                                                            │
│  From Dashboard Overview:                                  │
│  ┌────────────┐                                          │
│  │ 3 New      │ Click                                   │
│  │ Alerts     │────────────────────────┐                 │
│  └────────────┘                          │                 │
│                                         ▼                 │
│  ┌────────────────────────────────────────────────────┐   │
│  │                  Active Alerts Page                │   │
│  ├────────────────────────────────────────────────────┤   │
│  │ ┌──────────────────────────────────────────────┐   │   │
│  │ │ [🔴] CRITICAL  ai-inference-service   5m ago │   │   │
│  │ │ High GPU utilization detected               │   │   │
│  │ │ [View] [Acknowledge] [Resolve]              │   │   │
│  │ └──────────────────────────────────────────────┘   │   │
│  │                      │ Click [View]                │   │
│  │                      ▼                             │   │
│  │  ┌────────────────────────────────────────────┐   │   │
│  │  │           Alert Detail Modal               │   │   │
│  │  ├────────────────────────────────────────────┤   │   │
│  │  │ Alert: High GPU utilization                │   │   │
│  │  │ Severity: CRITICAL                         │   │   │
│  │  │ Service: ai-inference-service              │   │   │
│  │  │ Triggered: 5 minutes ago                   │   │   │
│  │  │                                            │   │   │
│  │  │ Current Value: 98%                         │   │   │
│  │  │ Threshold: 90%                             │   │   │
│  │  │                                            │   │   │
│  │  │ [Navigate to Service]                     │   │   │
│  │  │ [Acknowledge] [Snooze] [Resolve]          │   │   │
│  │  └────────────────────────────────────────────┘   │   │
│  └────────────────────────────────────────────────────┘   │
│                                                            │
│  From Service Detail Page:                                │
│  ┌──────────────────────────────────────────────────┐    │
│  │ Recent Alerts                                    │    │
│  │ [🟡] 2h ago  GPU usage high    Click →          │    │
│  └──────────────────────────────────────────────────┘    │
│                        │                                   │
│                        ▼                                   │
│           Opens Alert Detail Modal                        │
│                                                            │
│  Acknowledge Flow:                                        │
│  ┌──────────┐     ┌──────────┐     ┌──────────┐         │
│  │ Alert    │ →  │ Confirm  │ →  │ Alert    │         │
│  │ Active   │     │ Action   │     │ Acknowl. │         │
│  └──────────┘     └──────────┘     └──────────┘         │
│                                                            │
│  Resolve Flow:                                            │
│  ┌──────────┐     ┌──────────┐     ┌──────────┐         │
│  │ Alert    │ →  │ Enter    │ →  │ Alert    │         │
│  │ Acknowl. │     │ Note     │     │ Resolved │         │
│  └──────────┘     └──────────┘     └──────────┘         │
│                                                            │
└────────────────────────────────────────────────────────────┘
```

### 4.7 Report Generation Flow

```
┌────────────────────────────────────────────────────────────┐
│                 Tracking → Reports                          │
├────────────────────────────────────────────────────────────┤
│                                                            │
│  ┌────────────────────────────────────────────────┐       │
│  │  [+ Create New Report]           Click          │       │
│  └────────────────────────────────────────────────┘       │
│                        │                                   │
│                        ▼                                   │
│  ┌──────────────────────────────────────────────────┐     │
│  │            Create Report Modal                   │     │
│  ├──────────────────────────────────────────────────┤     │
│  │ Report Name: [________________]                  │     │
│  │ Report Type:  [Service Performance ▼]            │     │
│  │ Format:       [PDF ▼]                            │     │
│  │                                                  │     │
│  │ Date Range:                                      │     │
│  │  From: [2025-02-01]  To: [2025-02-08]           │     │
│  │                                                  │     │
│  │ Services: (Multi-select)                         │     │
│  │  ☑ ai-inference-service                         │     │
│  │  ☑ ai-analytics-service                         │     │
│  │  ☐ ai-training-service                          │     │
│  │                                                  │     │
│  │ Metrics:                                        │     │
│  │  ☑ Request Volume                              │     │
│  │  ☑ Latency                                     │     │
│  │  ☑ Error Rate                                  │     │
│  │  ☐ GPU Utilization                             │     │
│  │                                                  │     │
│  │ Include: ☑ Charts  ☑ Raw Data                  │     │
│  │                                                  │     │
│  │ Schedule: ☐ Send weekly on Monday               │     │
│  │                                                  │     │
│  │           [Cancel]  [Generate Report]            │     │
│  └──────────────────────────────────────────────────┘     │
│                        │ Click [Generate Report]          │
│                        ▼                                   │
│  ┌──────────────────────────────────────────────────┐     │
│  │              Report Generation Progress          │     │
│  │  ⏳ Generating report...                        │     │
│  │  ████████████░░░░░░░░ 50%                       │     │
│  │  Estimated time: 30 seconds                     │     │
│  └──────────────────────────────────────────────────┘     │
│                        │                                   │
│                        ▼                                   │
│  ┌──────────────────────────────────────────────────┐     │
│  │              Report Ready                        │     │
│  │  ✓ Report generated successfully                │     │
│  │                                                  │     │
│  │  [Download PDF]  [View in Browser]               │     │
│  │  [Share Link]   [Schedule Another]               │     │
│  └──────────────────────────────────────────────────┘     │
│                                                            │
└────────────────────────────────────────────────────────────┘
```

---

## 5. Error Handling Flows

### 5.1 401 Unauthorized Flow

```
┌────────────────────────────────────────────────────────────┐
│                    API Request                              │
│                         │                                   │
│                         ▼                                   │
│              Response: 401 Unauthorized                     │
│                         │                                   │
│                         ▼                                   │
┌────────────────────────────────────────────────────────────┐
│  Clear stored tokens and session                           │
│  Show notification: "Session expired. Please log in again."│
│  Redirect to /login                                        │
│  Store intended URL for post-login redirect                │
└────────────────────────────────────────────────────────────┘
```

### 5.2 403 Forbidden Flow

```
┌────────────────────────────────────────────────────────────┐
│              Protected Page Access Request                 │
│                         │                                   │
│                         ▼                                   │
│              Permission Check: FAILED                       │
│                         │                                   │
│                         ▼                                   │
┌────────────────────────────────────────────────────────────┐
│  Log unauthorized access attempt                            │
│  Show Access Denied page:                                   │
│   ┌────────────────────────────────────────────────┐       │
│   │           🔒 Access Denied                     │       │
│   │                                                │       │
│   │  You don't have permission to view this page.  │       │
│   │                                                │       │
│   │  Requested: AI Model Management                 │       │
│   │  Required: ai_model:manage                      │       │
│   │  Your Roles: DevOps Engineer                   │       │
│   │                                                │       │
│   │  [Go Back]  [Contact Admin]                    │       │
│   └────────────────────────────────────────────────┘       │
└────────────────────────────────────────────────────────────┘
```

### 5.3 404 Not Found Flow

```
┌────────────────────────────────────────────────────────────┐
│                    Invalid URL Access                       │
│                         │                                   │
│                         ▼                                   │
│              Response: 404 Not Found                        │
│                         │                                   │
│                         ▼                                   │
┌────────────────────────────────────────────────────────────┐
│  Show Not Found page:                                      │
│   ┌────────────────────────────────────────────────┐       │
│   │           📄 Page Not Found                    │       │
│   │                                                │       │
│   │  The page you're looking for doesn't exist.    │       │
│   │                                                │       │
│   │  [Go to Dashboard]  [Go Back]  [Report Issue]  │       │
│   └────────────────────────────────────────────────┘       │
└────────────────────────────────────────────────────────────┘
```

### 5.4 500 Server Error Flow

```
┌────────────────────────────────────────────────────────────┐
│                    API Request                              │
│                         │                                   │
│                         ▼                                   │
│              Response: 500 Server Error                     │
│                         │                                   │
│                         ▼                                   │
┌────────────────────────────────────────────────────────────┐
│  Show error notification                                    │
│  Log error to tracking service                              │
│  Option to retry request                                    │
│   ┌────────────────────────────────────────────────┐       │
│   │           ⚠️ Something went wrong              │       │
│   │                                                │       │
│   │  We encountered an error processing your       │       │
│   │  request. Please try again.                    │       │
│   │                                                │       │
│   │  Error ID: ERR-12345                           │       │
│   │                                                │       │
│   │  [Retry]  [Go to Dashboard]  [Report Issue]    │       │
│   └────────────────────────────────────────────────┘       │
└────────────────────────────────────────────────────────────┘
```

---

## 6. Loading States

### 6.1 Initial Page Load

```
┌────────────────────────────────────────────────────────────┐
│ Gogidix │ Search... │ [🔔] [John]                           │
├────────────────────────────────────────────────────────────┤
│                                                            │
│  ┌────────────────────────────────────────────────┐       │
│  │                                                │       │
│  │           ⏳ Loading Dashboard...              │       │
│  │                                                │       │
│  │           Fetching services...                 │       │
│  │           Loading metrics...                  │       │
│  │           Checking alerts...                  │       │
│  │                                                │       │
│  │                                                │       │
│  └────────────────────────────────────────────────┘       │
│                                                            │
└────────────────────────────────────────────────────────────┘
```

### 6.2 Skeleton Loading

```
┌────────────────────────────────────────────────────────────┐
│ Gogidix │ Search... │ [🔔] [John]                           │
├────────────────────────────────────────────────────────────┤
│                                                            │
│  ┌────────────┐ ┌────────────┐ ┌────────────┐            │
│  │ ▓▓▓▓▓▓▓▓▓▓  │ ▓▓▓▓▓▓▓▓▓▓  │ ▓▓▓▓▓▓▓▓▓▓  │            │
│  │ ▓▓▓▓▓▓▓▓▓▓  │ ▓▓▓▓▓▓▓▓▓▓  │ ▓▓▓▓▓▓▓▓▓▓  │            │
│  └────────────┘ └────────────┘ └────────────┘            │
│                                                            │
│  ┌────────────────────────────────────────────────┐       │
│  │ ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓  │       │
│  │ ▓▓▓▓▓▓▓▓ ▓▓▓▓▓▓▓▓ ▓▓▓▓▓▓▓▓ ▓▓▓▓▓▓▓▓          │       │
│  └────────────────────────────────────────────────┘       │
│                                                            │
│  ┌──────────────────────┐ ┌────────────────────────────┐  │
│  │ ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓  │ ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓   │  │
│  │ ▓▓▓▓▓▓ ▓▓▓▓▓▓▓▓▓▓▓▓  │ ▓▓▓▓▓▓▓ ▓▓▓▓▓▓▓▓▓▓▓▓▓▓    │  │
│  └──────────────────────┘ └────────────────────────────┘  │
│                                                            │
└────────────────────────────────────────────────────────────┘
```

### 6.3 Progressive Loading

```
┌────────────────────────────────────────────────────────────┐
│ Dashboard Overview                                          │
├────────────────────────────────────────────────────────────┤
│  ✓ Header loaded                                           │
│  ✓ Navigation loaded                                       │
│  ⏳ Loading metrics...                                     │
│  ⏳ Loading charts...                                      │
│  ⏳ Loading alerts...                                      │
└────────────────────────────────────────────────────────────┘
```

---

## Document Info

**Document Version:** 1.0
**Last Updated:** 2025-02-08
**Author:** Gogidix Architecture Team
**Related Documents:**
- 01_UI_Flow_Documentation.md
- 02_Wireframes_Documentation.md
- 03_Mock_Flow_Documentation.md
