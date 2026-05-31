# 01 - UI Flow Documentation

## AI & Orchestration Monitoring Dashboard

---

## Table of Contents

1. [Overview](#1-overview)
2. [Navigation Structure](#2-navigation-structure)
3. [Authentication Flow](#3-authentication-flow)
4. [Authorization & Role-Based Access](#4-authorization--role-based-access)
5. [User Flows](#5-user-flows)
6. [Dashboard Navigation Patterns](#6-dashboard-navigation-patterns)
7. [Responsive Navigation](#7-responsive-navigation)
8. [State Management Flow](#8-state-management-flow)

---

## 1. Overview

The AI & Orchestration Monitoring Dashboard provides comprehensive visibility into 57 Foundation-domain services across AI Services, Orchestration Services, Transaction Orchestration, and Universal Tracking Services.

**Target Users:**
- System Administrators
- DevOps Engineers
- AI Engineers
- Platform Engineers
- Business Analysts
- Executives (Executive Summary view)

---

## 2. Navigation Structure

```
┌─────────────────────────────────────────────────────────────────┐
│  GOGIDIX [Logo]         AI & Orchestration Monitoring           │
├─────────────────────────────────────────────────────────────────┤
│  Search...              [Notifications] [User Menu] [Theme]     │
├──────────┬──────────────────────────────────────────────────────┤
│          │                                                      │
│ DASHBOARD│                   Main Content Area                  │
│          │                                                      │
│ ─────────┼──────────────────────────────────────────────────────│
│ Overview │                                                      │
│   • System Health               [Dashboard Content]            │
│   • Active Alerts                                               │
│   • Quick Metrics                                                │
│          │                                                      │
│ ─────────┼──────────────────────────────────────────────────────│
│ AI       │                                                      │
│ Services │                                                      │
│   • Service Catalog (48)                                         │
│   • Model Performance                                            │
│   • Inference Analytics                                          │
│   • Training Jobs                                                │
│   • Feature Store                                                │
│   • Experiments                                                  │
│          │                                                      │
│ ─────────┼──────────────────────────────────────────────────────│
│ Orchest- │                                                      │
│ ration   │                                                      │
│   • Active Workflows                                            │
│   • Audit Trail                                                  │
│   • Onboarding Tracker                                           │
│   • Status Broadcast                                             │
│          │                                                      │
│ ─────────┼──────────────────────────────────────────────────────│
│ Trans-   │                                                      │
│ actions  │                                                      │
│   • Saga Monitor                                                 │
│   • Event Log                                                    │
│   • State Machines                                               │
│   • Recovery Actions                                             │
│          │                                                      │
│ ─────────┼──────────────────────────────────────────────────────│
│ Tracking │                                                      │
│   • Real-time Metrics                                            │
│   • Performance Analytics                                        │
│   • Alert Rules                                                  │
│   • Historical Reports                                           │
│          │                                                      │
│ ─────────┼──────────────────────────────────────────────────────│
│ Settings │                                                      │
│   • Dashboard Config                                             │
│   • Alert Management                                             │
│   • User Preferences                                             │
│   • API Keys                                                     │
│          │                                                      │
└──────────┴──────────────────────────────────────────────────────┘
```

### Navigation Menu Hierarchy

```yaml
Root:
  Overview:
    - System Health
    - Active Alerts
    - Quick Metrics
    - Executive Summary (Executive role only)

  AI Services:
    - Service Catalog:
        - Core Services (12)
        - Management Services (12)
        - Specialized Services (12)
        - Advanced Services (12)
    - Model Performance:
        - Accuracy Metrics
        - Latency Analysis
        - Throughput Monitoring
        - Model Drift Detection
    - Inference Analytics:
        - Request/Response Patterns
        - Error Rates
        - Prediction Confidence
        - Feature Importance
    - Training Jobs:
        - Active Jobs
        - Job History
        - Hyperparameter Tuning
        - Model Registry
    - Feature Store:
        - Feature Groups
        - Data Quality
        - Freshness Metrics
        - Lineage Tracking
    - Experiments:
        - A/B Tests
        - Model Comparisons
        - Experiment History
        - Results Analysis

  Orchestration:
    - Active Workflows:
        - Running Workflows
        - Completed Workflows
        - Failed Workflows
        - Workflow Templates
    - Audit Trail:
        - Event Search
        - Timeline View
        - Change History
        - Compliance Reports
    - Onboarding Tracker:
        - New User Onboarding
        - Service Onboarding
        - Progress Details
        - Milestone Tracking
    - Status Broadcast:
        - Service Status Grid
        - Status History
        - Notification Rules
        - Status Subscriptions

  Transactions:
    - Saga Monitor:
        - Active Sagas
        - Saga History
        - Compensation Actions
        - Saga Analytics
    - Event Log:
        - Event Search
        - Event Replay
        - Event Visualization
        - Event Export
    - State Machines:
        - Active States
        - State Transitions
        - State History
        - State Diagrams
    - Recovery Actions:
        - Failed Transactions
        - Manual Recovery
        - Retry Policies
        - Rollback Operations

  Tracking:
    - Real-time Metrics:
        - Live Dashboard
        - Service Metrics
        - Custom Metrics
        - Metric Explorer
    - Performance Analytics:
        - Response Times
        - Throughput Analysis
        - Error Analysis
        - Capacity Planning
    - Alert Rules:
        - Rule Configuration
        - Alert History
        - Alert Routing
        - Alert Suppression
    - Historical Reports:
        - Scheduled Reports
        - Custom Reports
        - Report Templates
        - Data Export

  Settings:
    - Dashboard Configuration:
        - Layout Customization
        - Widget Management
        - Theme Settings
        - Preferences
    - Alert Management:
        - Alert Channels
        - Alert Escalation
        - Alert Scheduling
        - Alert Templates
    - User Management:
        - User List
        - Role Management
        - Permissions
        - Audit Log
    - API Keys:
        - Key Management
        - Key Rotation
        - Usage Analytics
        - Access Control
```

---

## 3. Authentication Flow

### 3.1 Login Sequence

```
┌─────────┐    ┌─────────┐    ┌─────────┐    ┌─────────┐    ┌─────────┐
│  User   │───▶│ Login   │───▶│ Auth    │───▶│ Session │───▶│Dashboard│
│         │    │ Page   │    │ Service │    │ Store  │    │  Home   │
└─────────┘    └─────────┘    └─────────┘    └─────────┘    └─────────┘
                    │              │              │              │
                   fail           fail          refresh        RBAC
                    │              │              │              │
                    ▼              ▼              ▼              ▼
                Error         Error         Login           Role-based
                Message       Message       Prompt          Navigation
```

### 3.2 Authentication Flow Steps

1. **Initial Access**
   - User navigates to dashboard URL
   - System checks for valid session token
   - If valid: Redirect to dashboard
   - If invalid: Show login page

2. **Login Page**
   - Email/Username input
   - Password input
   - "Remember me" option
   - "Forgot password" link
   - SSO option (if configured)
   - Submit button

3. **Authentication Request**
   - POST /api/v1/auth/login
   - Payload: `{ email, password, rememberMe }`
   - Response: `{ token, refreshToken, user, roles, permissions }`

4. **Session Creation**
   - Store JWT token in localStorage (or httpOnly cookie)
   - Store refresh token securely
   - Initialize user state in Zustand store
   - Set up auto-refresh mechanism

5. **Dashboard Access**
   - Redirect to Overview page
   - Load user permissions
   - Configure navigation based on roles
   - Establish WebSocket connection for real-time updates

### 3.3 Logout Flow

```
┌─────────┐    ┌─────────┐    ┌─────────┐    ┌─────────┐
│Dashboard│───▶│ Logout  │───▶│ Auth    │───▶│ Login   │
│  Page   │    │ Confirm │    │ Service │    │  Page   │
└─────────┘    └─────────┘    └─────────┘    └─────────┘
                    │              │              │
                  cancel          clear          clear
                    │              │              │
                    ▼              ▼              ▼
                Return        Clear Session    Redirect
                to Dashboard  from Server      to Login
```

---

## 4. Authorization & Role-Based Access

### 4.1 Role Definitions

| Role | Description | Permissions |
|------|-------------|-------------|
| **System Administrator** | Full system access | All permissions |
| **DevOps Engineer** | Operational management | Deploy, configure, manage services |
| **AI Engineer** | AI service management | AI models, training, experiments |
| **Platform Engineer** | Infrastructure management | Infrastructure, orchestration |
| **Business Analyst** | Reporting and analytics | Read-only reports and analytics |
| **Executive** | High-level oversight | Executive summary dashboards only |

### 4.2 Permission Matrix

| Feature | SysAdmin | DevOps | AI Engineer | Platform Eng | Analyst | Executive |
|---------|----------|--------|-------------|--------------|---------|-----------|
| System Health | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ |
| Service Management | ✓ | ✓ | - | - | - | - |
| AI Model Management | ✓ | - | ✓ | - | - | - |
| Training Jobs | ✓ | - | ✓ | - | View | - |
| Experiments | ✓ | - | ✓ | - | View | - |
| Workflow Management | ✓ | ✓ | - | ✓ | - | - |
| Transaction Recovery | ✓ | ✓ | - | ✓ | - | - |
| Alert Configuration | ✓ | ✓ | ✓ | ✓ | - | - |
| User Management | ✓ | - | - | - | - | - |
| API Keys | ✓ | ✓ | ✓ | ✓ | - | - |
| Reports | ✓ | ✓ | ✓ | ✓ | ✓ | Summary |

### 4.3 Access Control Flow

```
┌──────────────────────────────────────────────────────────────┐
│                    User Authentication                        │
│                  (Token-based, JWT)                           │
└───────────────────────────┬──────────────────────────────────┘
                            │
                            ▼
┌──────────────────────────────────────────────────────────────┐
│                    Role Extraction                            │
│            (from token, decoded on backend)                   │
└───────────────────────────┬──────────────────────────────────┘
                            │
                            ▼
┌──────────────────────────────────────────────────────────────┐
│                  Permission Loading                           │
│       (fetch permissions based on user roles)                 │
└───────────────────────────┬──────────────────────────────────┘
                            │
                            ▼
┌──────────────────────────────────────────────────────────────┐
│                  Navigation Filtering                         │
│       (show only authorized menu items)                       │
└───────────────────────────┬──────────────────────────────────┘
                            │
                            ▼
┌──────────────────────────────────────────────────────────────┐
│                   Component Guards                            │
│       (protect individual components/features)                │
└───────────────────────────┬──────────────────────────────────┘
                            │
                            ▼
┌──────────────────────────────────────────────────────────────┐
│                    API Request                                │
│   (include auth token, validate on backend)                   │
└──────────────────────────────────────────────────────────────┘
```

### 4.4 Route Protection Pattern

```typescript
// Protected Route Component
interface ProtectedRouteProps {
  children: React.ReactNode;
  requiredPermissions?: Permission[];
  requiredRoles?: Role[];
  fallback?: React.ReactNode;
}

// Usage in routing
<Route
  path="/ai-services/model-management"
  element={
    <ProtectedRoute
      requiredPermissions={['ai:model:manage']}
      requiredRoles={[Role.SystemAdmin, Role.AIEngineer]}
    >
      <ModelManagementPage />
    </ProtectedRoute>
  }
/>
```

---

## 5. User Flows

### 5.1 System Administrator - Monitoring Flow

```
Start: Login as System Administrator
  │
  ▼
Dashboard Overview
  │
  ├─▶ View System Health Status
  │     ├─▶ Overall Health: 98%
  │     ├─▶ Active Services: 55/57
  │     ├─▶ Degraded: 2 services
  │     └─▶ Down: 0 services
  │
  ├─▶ Review Active Alerts (3 new)
  │     ├─▶ Alert: High GPU usage on ai-inference-service
  │     ├─▶ Alert: Model drift detected in fraud-detection
  │     └─▶ Alert: Saga timeout in transaction-orchestration
  │
  ├─▶ Navigate to AI Services → Service Catalog
  │     ├─▶ Filter: Core Services
  │     ├─▶ Select: ai-inference-service
  │     ├─▶ View: Resource utilization
  │     ├─▶ Action: Scale up GPU allocation
  │     └─▶ Confirm scaling operation
  │
  ├─▶ Navigate to AI Services → Model Performance
  │     ├─▶ Select: fraud-detection-model
  │     ├─▶ View: Drift metrics (precision dropped 5%)
  │     ├─▶ Action: Trigger retraining
  │     └─▶ Schedule: Nightly retraining job
  │
  ├─▶ Navigate to Transactions → Saga Monitor
  │     ├─▶ View: Failed saga details
  │     ├─▶ Action: Manual compensation
  │     └─▶ Resolve: Transaction recovered
  │
  └─▶ Settings → Alert Management
        ├─▶ Update: GPU threshold alert
        └─▶ Configure: Email notifications for critical
```

### 5.2 AI Engineer - Model Management Flow

```
Start: Login as AI Engineer
  │
  ▼
AI Services → Training Jobs
  │
  ├─▶ View Active Training Jobs (3 running)
  │     ├─▶ Job: customer-segmentation-v2
  │     │     ├─▶ Progress: 67%
  │     │     ├─▶ ETA: 2h 15m
  │     │     └─▶ Metrics: Accuracy improving
  │     │
  │     ├─▶ Job: recommendation-retrain
  │     │     ├─▶ Progress: 23%
  │     │     ├─▶ ETA: 5h 30m
  │     │     └─▶ Status: Normal
  │     │
  │     └─▶ Job: nlp-sentiment-finetune
  │           ├─▶ Progress: 91%
  │           ├─▶ ETA: 15m
  │           └─▶ Status: Near completion
  │
  ├─▶ Navigate to Model Performance
  │     ├─▶ Select: recommendation-engine
  │     ├─▶ View: Performance metrics
  │     │     ├─▶ Accuracy: 94.2%
  │     │     ├─▶ Latency: 45ms (target: <50ms)
  │     │     └─▶ Throughput: 1,200 req/sec
  │     │
  │     └─▶ Check: Model drift indicators
  │           ├─▶ Drift score: 0.12 (acceptable)
  │           └─▶ Last retrained: 7 days ago
  │
  ├─▶ Navigate to Experiments
  │     ├─▶ Create new A/B test
  │     │     ├─▶ Name: "Model V3 vs V4"
  │     │     ├─▶ Model A: recommendation-v3
  │     │     ├─▶ Model B: recommendation-v4
  │     │     ├─▶ Traffic split: 50/50
  │     │     └─▶ Duration: 7 days
  │     │
  │     └─▶ Start experiment
  │
  └─▶ Navigate to Feature Store
        ├─▶ Browse: Feature groups
        ├─▶ Select: user-behavior-features
        ├─▶ View: Data quality metrics
        │     ├─▶ Completeness: 99.8%
        │     ├─▶ Freshness: 5 minutes old
        │     └─▶ Null count: 0.02%
        │
        └─▶ Export: Feature statistics
```

### 5.3 DevOps Engineer - Incident Response Flow

```
Start: Receive PagerDuty Alert
  │
  ▼
Login as DevOps Engineer
  │
  ▼
Navigate to: Overview → Active Alerts
  │
  ├─▶ Alert: ai-inference-service - High Error Rate
  │     ├─▶ Severity: Critical
  │     ├─▶ Error rate: 12.3% (baseline: <0.1%)
  │     ├─▶ Duration: 5 minutes
  │     └─▶ Affected endpoints: /predict, /batch-inference
  │
  ├─▶ Click: View Details
  │     ├─▶ Navigate to: AI Services → Service Catalog
  │     ├─▶ Select: ai-inference-service
  │     │
  │     ├─▶ Check: Service Health
  │     │     ├─▶ CPU: 89% (high)
  │     │     ├─▶ Memory: 72%
  │     │     ├─▶ GPU: 98% (critical)
  │     │     └─▶ Request queue: 2,345 pending
  │     │
  │     └─▶ Check: Recent Logs
  │           ├─▶ Error: CUDA out of memory
  │           └─▶ Pattern: Sudden spike 5 min ago
  │
  ├─▶ Navigate to: Tracking → Real-time Metrics
  │     ├─▶ View: Request volume spike
  │     ├─▶ Source: Internal service unexpected load
  │     └─▶ Action: Identify source service
  │
  ├─▶ Navigate to: Orchestration → Active Workflows
  │     ├─▶ Find: Problematic workflow
  │     ├─▶ Action: Pause workflow
  │     └─▶ Result: Load decreasing
  │
  ├─▶ Navigate to: AI Services → Service Catalog
  │     ├─▶ Select: ai-inference-service
  │     ├─▶ Action: Scale up instances
  │     ├─▶ Configuration: +2 GPU instances
  │     └─▶ Apply: Scaling operation
  │
  └─▶ Monitor: Recovery
        ├─▶ Watch: Error rate decreasing
        ├─▶ Watch: GPU utilization normalizing
        └─▶ Confirm: Incident resolved
```

### 5.4 Platform Engineer - Orchestration Debugging Flow

```
Start: Report of stuck workflow
  │
  ▼
Login as Platform Engineer
  │
  ▼
Navigate to: Orchestration → Active Workflows
  │
  ├─▶ Filter: Status = Stuck
  │     ├─▶ Result: 1 stuck workflow
  │     └─▶ Workflow ID: wf-onboarding-user-12345
  │
  ├─▶ Click: View Details
  │     ├─▶ Workflow: New User Onboarding
  │     ├─▶ Current Step: email-verification
  │     ├─▶ Stuck duration: 2 hours 15 min
  │     ├─▶ Steps: 5/8 completed
  │     │
  │     └─▶ View: Step Details
  │           ├─▶ Step: email-verification
  │           ├─▶ Status: Waiting
  │           ├─▶ Waiting for: External email service
  │           └─▶ Last update: 2 hours ago
  │
  ├─▶ Navigate to: Audit Trail
  │     ├─▶ Search: User ID 12345
  │     ├─▶ Timeline: Show all events
  │     │
  │     └─▶ Find: Email verification sent
  │           ├─▶ Time: 2 hours ago
  │           ├─▶ Status: Delivered
  │           ├─▶ Provider: SendGrid
  │           └─▶ No callback received
  │
  ├─▶ Navigate to: Status Broadcast
  │     ├─▶ Check: Email service status
  │     ├─▶ Result: All systems operational
  │     └─▶ Hypothesis: Webhook timeout
  │
  ├─▶ Navigate to: Transactions → State Machines
  │     ├─▶ Select: Onboarding state machine
  │     ├─▶ View: Current state diagram
  │     ├─▶ Current: EMAIL_VERIFICATION_PENDING
  │     │
  │     └─▶ Action: Force state transition
  │           ├─▶ New state: EMAIL_VERIFIED
  │           ├─▶ Reason: Manual override - timeout
  │           └─▶ Confirm: State change
  │
  └─▶ Navigate to: Orchestration → Active Workflows
        ├─▶ Select: wf-onboarding-user-12345
        ├─▶ Status: Resumed
        └─▶ Result: Workflow completed successfully
```

### 5.5 Business Analyst - Report Generation Flow

```
Start: Weekly report request
  │
  ▼
Login as Business Analyst
  │
  ▼
Navigate to: Tracking → Historical Reports
  │
  ├─▶ Create New Report
  │     ├─▶ Report Type: AI Service Performance
  │     ├─▶ Date Range: Last 7 days
  │     ├─▶ Services: All AI Services
  │     │
  │     └─▶ Metrics to include:
  │           ├─▶ Request volume
  │           ├─▶ Average latency
  │           ├─▶ Error rate
  │           ├─▶ Cost attribution
  │           └─▶ Top consumers
  │
  ├─▶ Generate Report
  │     ├─▶ Status: Processing
  │     ├─▶ Wait: 15 seconds
  │     └─▶ Result: Report ready
  │
  ├─▶ View Report
  │     ├─▶ Executive Summary
  │     │     ├─▶ Total requests: 12.4M
  │     │     ├─▶ Avg latency: 67ms
  │     │     ├─▶ Success rate: 99.7%
  │     │     └─▶ Cost: $4,280
  │     │
  │     ├─▶ Service Breakdown
  │     │     ├─▶ Top: ai-recommendation-service (3.2M req)
  │     │     ├─▶ Growth: ai-search-service (+45%)
  │     │     └─▶ Stable: ai-authentication-service
  │     │
  │     └─▶ Visualizations
  │           ├─▶ Line chart: Request trends
  │           ├─▶ Bar chart: Service comparison
  │           └─▶ Pie chart: Cost distribution
  │
  └─▶ Export Options
        ├─▶ Format: PDF
        ├─▶ Include: Raw data
        ├─▶ Schedule: Weekly recurring
        └─▶ Send: Email to stakeholders
```

### 5.6 Executive - Executive Summary Flow

```
Start: Login as Executive
  │
  ▼
Redirect to: Executive Summary (customized view)
  │
  ├─▶ Overall System Health
  │     ├─▶ Health Score: 97/100
  │     ├─▶ Status: Operational
  │     ├─▶ Trend: ↗ Improving
  │     └─▶ Last updated: Just now
  │
  ├─▶ Key Metrics (at a glance)
  │     ├─▶ AI Services Active: 55/57
  │     ├─▶ Daily AI Requests: 12.4M
  │     ├─▶ Avg Response Time: 67ms
  │     ├─▶ Workflows Today: 45,892
  │     └─▶ Transactions Today: 1.2M
  │
  ├─▶ Cost Summary
  │     ├─▶ This Month: $148,000
  │     ├─▶ vs Last Month: +5%
  │     ├─▶ Forecast: $155,000
  │     └─▶ Budget Utilization: 74%
  │
  ├─▶ Active Issues
  │     ├─▶ Critical: 0
  │     ├─▶ Warning: 2
  │     └─▶ Being addressed: Yes
  │
  ├─▶ AI Initiative Status
  │     ├─▶ Model Training: 3 active jobs
  │     ├─▶ Experiments: 2 A/B tests running
  │     ├─▶ New Models: 5 in development
  │     └─▶ Next Release: 2 weeks
  │
  └─▶ Quick Actions
        ├─▶ View detailed report
        ├─▶ Schedule briefing
        └─▶ Export monthly summary
```

---

## 6. Dashboard Navigation Patterns

### 6.1 Breadcrumb Navigation

```
Home > AI Services > Service Catalog > ai-inference-service

┌─────────────────────────────────────────────────────────────┐
│ Home ▸ AI Services ▸ Service Catalog ▸ ai-inference-service │
└─────────────────────────────────────────────────────────────┘
```

### 6.2 Tab Navigation (for sub-pages)

```
┌──────────────────────────────────────────────────────────────┐
│ [Overview] [Metrics] [Logs] [Configuration] [History]       │
├──────────────────────────────────────────────────────────────┤
│                                                              │
│                      Active Tab Content                      │
│                                                              │
└──────────────────────────────────────────────────────────────┘
```

### 6.3 Filter/Sort Navigation

```
┌──────────────────────────────────────────────────────────────┐
│ Filter: [Core ▼]  Status: [All ▼]  Sort: [Name ▲]  Search: []│
├──────────────────────────────────────────────────────────────┤
│                                                              │
│                      Filtered Results                        │
│                                                              │
└──────────────────────────────────────────────────────────────┘
```

### 6.4 Quick Navigation (Keyboard Shortcuts)

| Shortcut | Action |
|----------|--------|
| `Ctrl+K` | Command palette / Quick search |
| `Ctrl+G` | Go to Dashboard Overview |
| `Ctrl+A` | Go to AI Services |
| `Ctrl+O` | Go to Orchestration |
| `Ctrl+T` | Go to Transactions |
| `Ctrl+R` | Go to Tracking |
| `Ctrl+S` | Open Settings |
| `Esc` | Close modal/drawer |
| `Ctrl+/` | Show keyboard shortcuts |

---

## 7. Responsive Navigation

### 7.1 Desktop Layout (≥1024px)

```
┌────┬───────────────────────────────────────────────────────┐
│    │ Header: Logo | Search | Notifications | User | Theme│
│    ├───────────────────────────────────────────────────────┤
│ S  │                                                       │
│ i  │                                                       │
│ d  │                   Main Content                        │
│ e  │                                                       │
│ b  │                                                       │
│ a  │                                                       │
│ r  │                                                       │
│    │                                                       │
└────┴───────────────────────────────────────────────────────┘
```

### 7.2 Tablet Layout (768px - 1023px)

```
┌──────────────────────────────────────────────────────┐
│ Header: [☰] Logo | Search | Notifications | User    │
├──────────────────────────────────────────────────────┤
│                                                      │
│                   Main Content                       │
│                                                      │
│  (Sidebar collapsed to drawer, toggled with ☰)      │
│                                                      │
└──────────────────────────────────────────────────────┘
```

### 7.3 Mobile Layout (<768px)

```
┌─────────────────────────┐
│ [☰] Logo         [🔔][]│
├─────────────────────────┤
│                         │
│     Main Content        │
│                         │
│ (Bottom nav for         │
│  main sections)         │
│                         │
├─────────────────────────┤
│ [🏠][AI][⚙️][👤]        │
└─────────────────────────┘
```

---

## 8. State Management Flow

### 8.1 Global State Structure (Zustand)

```typescript
// Store Structure
interface DashboardStore {
  // Auth State
  auth: {
    user: User | null;
    token: string | null;
    isAuthenticated: boolean;
    permissions: Permission[];
  };

  // Navigation State
  navigation: {
    currentPath: string;
    sidebarCollapsed: boolean;
    breadcrumbs: Breadcrumb[];
  };

  // Dashboard State
  dashboard: {
    services: Service[];
    alerts: Alert[];
    metrics: MetricData[];
    filters: FilterState;
  };

  // Real-time State
  realtime: {
    connected: boolean;
    lastUpdate: Date;
    liveMetrics: Map<string, number>;
  };
}
```

### 8.2 Data Fetching Flow (TanStack Query)

```
Component Mount
      │
      ▼
useQuery Hook
      │
      ├─▶ Check Cache
      │     ├─▶ Fresh? → Return cached data
      │     └─▶ Stale? → Fetch from API
      │
      ├─▶ API Request
      │     ├─▶ Success → Update cache, return data
      │     └─▶ Error → Return error state
      │
      └─▶ Background Refetch
            └─▶ Auto-refetch on interval/focus/reconnect
```

### 8.3 Real-time Updates Flow

```
┌─────────────────┐
│ WebSocket Client │
└────────┬─────────┘
         │
         ▼
┌─────────────────┐
│  Connection     │─────▶ Connected? → Subscribe to channels
│   Manager       │─────▶ Disconnected? → Reconnect with backoff
└────────┬─────────┘
         │
         ▼
┌─────────────────┐
│  Message        │─────▶ Parse message
│  Handler        │─────▶ Validate schema
└────────┬─────────┘
         │
         ▼
┌─────────────────┐
│  State Update   │─────▶ Update Zustand store
│  Trigger        │─────▶ Invalidate related queries
└────────┬─────────┘
         │
         ▼
┌─────────────────┐
│  UI Re-render   │
└─────────────────┘
```

---

## 9. Error Handling Flows

### 9.1 Authentication Error Flow

```
API Request
      │
      ▼
Response: 401 Unauthorized
      │
      ├─▶ Clear stored tokens
      ├─▶ Redirect to Login
      └─▶ Show error message
```

### 9.2 Authorization Error Flow

```
Protected Access Request
      │
      ▼
Permission Check: FAILED
      │
      ├─▶ Log unauthorized attempt
      ├─▶ Show "Access Denied" UI
      ├─▶ Suggest contact admin
      └─▶ Redirect to safe page
```

### 9.3 Network Error Flow

```
API Request
      │
      ▼
Network Error
      │
      ├─▶ Show offline indicator
      ├─▶ Retry with exponential backoff
      ├─▶ Queue mutations for later
      └─▶ Show cached data if available
```

---

## 10. Loading States

### 10.1 Initial Load

```
┌─────────────────────────────────────────┐
│                                         │
│              [Spinner]                   │
│         Loading Dashboard...             │
│                                         │
└─────────────────────────────────────────┘
```

### 10.2 Skeleton Loading

```
┌─────────────────────────────────────────┐
│ ▓▓▓▓▓▓▓▓ ▓▓▓▓▓▓▓▓▓▓ ▓▓▓▓▓▓▓ ▓▓▓▓▓         │
│ ▓▓▓▓▓ ▓▓▓▓▓▓▓▓ ▓▓▓▓▓▓▓▓▓▓▓▓▓ ▓▓▓▓▓▓▓▓▓   │
│ ▓▓▓▓▓▓▓▓ ▓▓▓▓▓ ▓▓▓▓▓▓▓▓ ▓▓▓▓▓▓▓▓▓▓▓▓▓▓   │
└─────────────────────────────────────────┘
```

### 10.3 Progressive Loading

```
┌─────────────────────────────────────────┐
│ ✓ Header Loaded                         │
│ ✓ Navigation Loaded                    │
│ ⏳ Loading metrics...                   │
│ ⏳ Loading charts...                    │
└─────────────────────────────────────────┘
```

---

## Document Info

**Document Version:** 1.0
**Last Updated:** 2025-02-08
**Author:** Gogidix Architecture Team
**Related Documents:**
- 02_Wireframes_Documentation.md
- 03_Mock_Flow_Documentation.md
- 04_Page_By_Page_Flow_Documentation.md
