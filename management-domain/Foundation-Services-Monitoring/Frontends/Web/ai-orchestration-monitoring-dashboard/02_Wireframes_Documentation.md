# 02 - Wireframes Documentation

## AI & Orchestration Monitoring Dashboard

---

## Table of Contents

1. [Design System](#1-design-system)
2. [Layout Specifications](#2-layout-specifications)
3. [Component Library](#3-component-library)
4. [Page Wireframes](#4-page-wireframes)
5. [Responsive Design](#5-responsive-design)
6. [Accessibility Considerations](#6-accessibility-considerations)

---

## 1. Design System

### 1.1 Color Palette

#### Primary Colors

```css
--primary-50:  #EEF2FF;
--primary-100: #E0E7FF;
--primary-200: #C7D2FE;
--primary-300: #A5B4FC;
--primary-400: #818CF8;
--primary-500: #6366F1;  /* Primary brand color */
--primary-600: #4F46E5;
--primary-700: #4338CA;
--primary-800: #3730A3;
--primary-900: #312E81;
```

#### Status Colors

```css
/* Success - Healthy/Operational */
--success-50:  #F0FDF4;
--success-500: #22C55E;
--success-700: #15803D;

/* Warning - Degraded/Attention needed */
--warning-50:  #FEFCE8;
--warning-500: #EAB308;
--warning-700: #A16207;

/* Error - Critical/Down */
--error-50:    #FEF2F2;
--error-500:   #EF4444;
--error-700:   #B91C1C;

/* Info - Neutral/Informational */
--info-50:     #F0F9FF;
--info-500:    #0EA5E9;
--info-700:    #0369A1;
```

#### Neutral Colors

```css
--gray-50:    #F9FAFB;
--gray-100:   #F3F4F6;
--gray-200:   #E5E7EB;
--gray-300:   #D1D5DB;
--gray-400:   #9CA3AF;
--gray-500:   #6B7280;
--gray-600:   #4B5563;
--gray-700:   #374151;
--gray-800:   #1F2937;
--gray-900:   #111827;
```

#### Dark Mode Overrides

```css
@media (prefers-color-scheme: dark) {
  --bg-primary:   #0F172A;
  --bg-secondary: #1E293B;
  --bg-tertiary:  #334155;
  --text-primary: #F1F5F9;
  --text-secondary: #94A3B8;
  --border-color: #334155;
}
```

### 1.2 Typography

#### Font Families

```css
--font-sans: 'Inter', -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto,
             'Helvetica Neue', Arial, sans-serif;
--font-mono: 'JetBrains Mono', 'Fira Code', Consolas, Monaco, monospace;
```

#### Type Scale

```css
/* Text Sizes */
--text-xs:    0.75rem;    /* 12px */
--text-sm:    0.875rem;   /* 14px */
--text-base:  1rem;       /* 16px */
--text-lg:    1.125rem;   /* 18px */
--text-xl:    1.25rem;    /* 20px */
--text-2xl:   1.5rem;     /* 24px */
--text-3xl:   1.875rem;   30px */
--text-4xl:   2.25rem;    /* 36px */

/* Font Weights */
--font-normal:  400;
--font-medium:  500;
--font-semibold: 600;
--font-bold:    700;

/* Line Heights */
--leading-tight:   1.25;
--leading-normal:  1.5;
--leading-relaxed: 1.75;
```

#### Typography Hierarchy

```
H1 Page Title       text-4xl font-bold - Dashboard Overview
H2 Section Header   text-2xl font-semibold - AI Services
H3 Subsection       text-xl font-medium - Model Performance
H4 Card Title       text-lg font-semibold - Accuracy Metrics
Body Large          text-base - Main content
Body Small          text-sm - Secondary info
Caption             text-xs - Labels, metadata
Code/Mono           text-sm font-mono - IDs, timestamps
```

### 1.3 Spacing Scale

```css
--spacing-0:   0;
--spacing-1:   0.25rem;  /* 4px */
--spacing-2:   0.5rem;   /* 8px */
--spacing-3:   0.75rem;  /* 12px */
--spacing-4:   1rem;     /* 16px */
--spacing-5:   1.25rem;  /* 20px */
--spacing-6:   1.5rem;   /* 24px */
--spacing-8:   2rem;     /* 32px */
--spacing-10:  2.5rem;   /* 40px */
--spacing-12:  3rem;     /* 48px */
--spacing-16:  4rem;     /* 64px */
--spacing-20:  5rem;     /* 80px */
```

### 1.4 Border Radius

```css
--radius-none:  0;
--radius-sm:    0.125rem;  /* 2px */
--radius-md:    0.375rem;  /* 6px */
--radius-lg:    0.5rem;    /* 8px */
--radius-xl:    1rem;      /* 16px */
--radius-2xl:   1.5rem;    /* 24px */
--radius-full:  9999px;
```

### 1.5 Shadows

```css
--shadow-xs:   0 1px 2px 0 rgb(0 0 0 / 0.05);
--shadow-sm:   0 1px 3px 0 rgb(0 0 0 / 0.1);
--shadow-md:   0 4px 6px -1px rgb(0 0 0 / 0.1);
--shadow-lg:   0 10px 15px -3px rgb(0 0 0 / 0.1);
--shadow-xl:   0 20px 25px -5px rgb(0 0 0 / 0.1);
```

---

## 2. Layout Specifications

### 2.1 Overall Layout Structure

```
┌──────────────────────────────────────────────────────────────────────┐
│  HEADER (64px)                                                        │
│  ├─ Logo (40px) ├─ Search ├─ Breadcrumb ├─ Spacer ├─ Notify ├─ User│
├──────┬───────────────────────────────────────────────────────────────┤
│      │                                                                │
│ SIDE-│                           MAIN CONTENT                         │
│ BAR  │                                                               │
│(256px│                                                               │
│      │                                                               │
│ col- │                                                               │
│lapsed│                                                               │
│ to   │                                                               │
│ 64px)│                                                               │
│      │                                                               │
│      │                                                               │
│      │                                                               │
└──────┴───────────────────────────────────────────────────────────────┘

Dimensions:
- Header: 64px height, fixed
- Sidebar: 256px width (expanded), 64px width (collapsed)
- Main Content: Remaining width, 100vh - 64px height
- Content Padding: 24px
- Card Gap: 16px
```

### 2.2 Header Layout

```
┌────────────────────────────────────────────────────────────────────┐
│ ┌────┐ ┌─────────────────────┐              ┌────────────────────┐ │
│ │Logo│ │  Search services... │   [Breadcrumb] │ 🔔  John Doe ▼   │ │
│ └────┘ └─────────────────────┘              └────────────────────┘ │
└────────────────────────────────────────────────────────────────────┘

    40px      240px                          200px
```

### 2.3 Sidebar Layout (Expanded)

```
┌──────────────┐
│   GOGIDIX    │  Logo area (48px)
│   ────────   │  ═══════════════════════════
│              │
│ 🏠 Overview  │  Menu item (40px height)
│              │
│ ───────────  │  Section divider
│              │
│ 🤖 AI        │  Section header (32px)
│   Services   │
│   • Catalog  │  Submenu items
│   • Models   │
│   • Train    │
│              │
│ ───────────  │
│              │
│ ⚙️ Orchestr. │
│   • Workflows│
│   • Audit    │
│              │
│ ───────────  │
│              │
│ 🔄 Trans.    │
│              │
│ ───────────  │
│              │
│ 📊 Tracking  │
│              │
│ ───────────  │
│              │
│ ⚙️ Settings  │
│              │
└──────────────┘
```

### 2.4 Sidebar Layout (Collapsed)

```
┌──────────┐
│    🏠    │  Icon only (40px)
│          │
│    🤖    │
│          │
│    ⚙️    │
│          │
│    🔄    │
│          │
│    📊    │
│          │
│    ⚙️    │
│          │
└──────────┘
```

### 2.5 Main Content Grid

```
┌───────────────────────────────────────────────────────────────────┐
│ Page Title                                                    [🔃] │
│ Subtitle / Breadcrumbs                                            │
├───────────────────────────────────────────────────────────────────┤
│                                                                   │
│  ┌────────────┐  ┌────────────┐  ┌────────────┐  ┌────────────┐ │
│  │   Card 1   │  │   Card 2   │  │   Card 3   │  │   Card 4   │ │
│  │  (25%)     │  │  (25%)     │  │  (25%)     │  │  (25%)     │ │
│  └────────────┘  └────────────┘  └────────────┘  └────────────┘ │
│                                                                   │
│  ┌────────────────────────┐  ┌──────────────────────────────────┐│
│  │   Card 5               │  │   Card 6                         ││
│  │   (50%)                │  │   (50%)                          ││
│  └────────────────────────┘  └──────────────────────────────────┘│
│                                                                   │
│  ┌─────────────────────────────────────────────────────────────────┐│
│  │   Card 7                                                        ││
│  │   (100%)                                                       ││
│  └─────────────────────────────────────────────────────────────────┘│
│                                                                   │
└───────────────────────────────────────────────────────────────────┘

Grid System:
- 12-column grid
- Column gap: 16px
- Row gap: 16px
- Card span: 3, 4, 6, 8, or 12 columns
```

---

## 3. Component Library

### 3.1 Status Badge

```
┌─────────────┐
│ ● Operational│  Small, pill-shaped
└─────────────┘

Variants:
┌────────────┬─────────────┬──────────────┬─────────────┐
│ ●Healthy   │ ●Degraded  │ ●Down        │ ●Unknown   │
│ bg-success │ bg-warning │ bg-error     │ bg-gray    │
└────────────┴─────────────┴──────────────┴─────────────┘

Sizes:
┌──────────┐ ┌───────────┐ ┌─────────────┐
│ Small    │ │ Medium    │ │ Large       │
│ text-xs  │ │ text-sm   │ │ text-base   │
│ h-5      │ │ h-6       │ │ h-7         │
└──────────┘ └───────────┘ └─────────────┘
```

### 3.2 Metric Card

```
┌────────────────────────────────────┐
│ Metric Title                  [⠇] │
├────────────────────────────────────┤
│                                    │
│     98.7%                          │
│     ────────                        │
│   value text-3xl                    │
│                                    │
│  ↗ +2.3% from last week            │
│  text-sm text-gray-500             │
│                                    │
│     ┌──────────────────────────┐  │
│     │                          │  │
│     │   Mini Sparkline Chart   │  │
│     │                          │  │
│     └──────────────────────────┘  │
│                                    │
└────────────────────────────────────┘
     width: 100%
     height: auto (min 160px)
```

### 3.3 Service Card

```
┌──────────────────────────────────────────────┐
│ ┌────┐ ai-inference-service        [●●●] [⋮] │
│ │    │ Inference Engine v2.1.0                │
│ │ 🤖 │                                       │
│ └────┘                                       │
│                                              │
│ Status:    ● Operational                     │
│ Requests:  1.2M/day                          │
│ Latency:   45ms avg                         │
│ Error:     0.02%                             │
│                                              │
│ ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━ 89%│
│ GPU Utilization                             │
│                                              │
└──────────────────────────────────────────────┘
```

### 3.4 Alert Card

```
┌────────────────────────────────────────────────────────────┐
│ [🔴] CRITICAL               ai-inference-service   5m ago  │
├────────────────────────────────────────────────────────────┤
│ High GPU utilization detected                              │
│                                                            │
│ GPU usage has exceeded 90% threshold for 5 minutes.       │
│ Current: 98% | Threshold: 90%                              │
│                                                            │
│ [View Details] [Acknowledge] [Resolve]                    │
└────────────────────────────────────────────────────────────┘

Severity Variants:
🔴 Critical - Red border/bg
🟡 Warning  - Yellow border/bg
🔵 Info     - Blue border/bg
```

### 3.5 Data Table

```
┌─────────────────────────────────────────────────────────────────────────┐
│ Service Name            Status      Requests/s    Latency    Actions   │
├─────────────────────────────────────────────────────────────────────────┤
│ ai-inference-service    ● Healthy   1,234         45ms       [⋮]       │
│ ai-analytics-service    ● Healthy   567           89ms       [⋮]       │
│ ai-training-service     ● Healthy   12            N/A        [⋮]       │
│ ai-gateway-service      ● Healthy   2,456         23ms       [⋮]       │
│ ai-model-service        🟡 Warning  890           156ms      [⋮]       │
├─────────────────────────────────────────────────────────────────────────┤
│                    1 - 10 of 48                    [◀] 1 [▶]          │
└─────────────────────────────────────────────────────────────────────────┘
```

### 3.6 Action Menu (Dropdown)

```
                      ┌─────────────────────┐
                      │ View Details        │
                      │ View Logs           │
                      │ View Metrics        │
                      ├─────────────────────┤
                      │ Edit Configuration  │
                      │ Restart Service     │
                      │ Scale Up/Down       │
                      ├─────────────────────┤
                      │ Copy Service ID     │
                      │ Copy URL            │
                      └─────────────────────┘
```

### 3.7 Filter Bar

```
┌─────────────────────────────────────────────────────────────────────────┐
│ Filters                                                                  │
├─────────────────────────────────────────────────────────────────────────┤
│ Category: [All Services ▼]    Status: [All ▼]    Search: [        ]   │
│ Sort by: [Name ▲ ▼]           Clear All                               │
└─────────────────────────────────────────────────────────────────────────┘
```

### 3.8 Progress Bar

```
┌────────────────────────────────────────────┐
│ Training Job: customer-segmentation-v2     │
│                                            │
│ Progress: 67%                              │
│ ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━  │
│                                            │
│ Started: 2 hours ago                       │
│ ETA: 1 hour 15 minutes                     │
│                                            │
└────────────────────────────────────────────┘
```

### 3.9 Modal

```
┌─────────────────────────────────────────────────────────────────┐
│ ┌─────────────────────────────────────────────────────────────┐ │
│ │                    Configure Service                        │ │
│ │                                                        [✕] │ │
│ ├─────────────────────────────────────────────────────────────┤ │
│ │                                                             │ │
│ │ Service: ai-inference-service                              │ │
│ │                                                             │ │
│ │ Configuration                                              │ │
│ │ ┌─────────────────────────────────────────────────────┐   │ │
│ │ │ GPU Instances: [2]                               │   │ │
│ │ │ Memory Limit:   [16 GB]                          │   │ │
│ │ │ Request Limit:  [1000]                           │   │ │
│ │ │ Auto-scale:     [✓] Enabled                     │   │ │
│ │ └─────────────────────────────────────────────────────┘   │ │
│ │                                                             │ │
│ ├─────────────────────────────────────────────────────────────┤ │
│ │                     [Cancel]  [Save Changes]                │ │
│ └─────────────────────────────────────────────────────────────┘ │
└─────────────────────────────────────────────────────────────────┘
```

### 3.10 Toast Notification

```
┌────────────────────────────────────────┐
│ ✓ Service scaled successfully          │
│ ai-inference-service → 4 instances     │
│                                [Dismiss]│
└────────────────────────────────────────┘

Variants:
✓ Success - Green
⚠ Warning  - Yellow
✕ Error    - Red
ℹ Info     - Blue
```

### 3.11 Chart Container

```
┌──────────────────────────────────────────────────────────────┐
│ Request Volume over Time                            [📥][⚙️] │
├──────────────────────────────────────────────────────────────┤
│  2M ┌─┐                                                         │
│     │ │     ┌─┐                            ┌────┐              │
│  1M │ └─────┘ │        ┌────┐            ┌─┘    │             │
│     │         └───┐   ┌──┘    └───┐   ┌───┘      │             │
│  0  └─────────────┴───┴───────────┴───┴──────────┴─  Time    │
│     00:00      06:00      12:00      18:00      24:00        │
├──────────────────────────────────────────────────────────────┤
│ ━━━━ ai-inference    ━━━━ ai-analytics    ━━━━ ai-gateway   │
└──────────────────────────────────────────────────────────────┘
```

### 3.12 Timeline (Audit Trail)

```
┌──────────────────────────────────────────────────────────────┐
│ Timeline: User ID 12345                                      │
├──────────────────────────────────────────────────────────────┤
│                                                              │
│  ● Today, 14:32                                              │
│    Email verified                                            │
│    User completed email verification                        │
│                                                              │
│  ● Today, 14:30                                              │
│    Email sent                                                │
│    Verification email sent to user@example.com              │
│                                                              │
│  ● Today, 14:28                                              │
│    Account created                                           │
│    New user account created                                  │
│                                                              │
└──────────────────────────────────────────────────────────────┘
```

### 3.13 Workflow Visualization

```
┌──────────────────────────────────────────────────────────────┐
│ Workflow: New User Onboarding                                │
│                     ID: wf-onboarding-12345                   │
├──────────────────────────────────────────────────────────────┤
│                                                              │
│  ┌──────────┐    ┌──────────┐    ┌──────────┐    ┌────────┐ │
│  │Create    │───▶│Send      │───▶│Verify    │───▶│Complete││
│  │Account   │    │Email     │    │Email     │    │Setup   ││
│  │          │    │          │    │          │    │        ││
│  │  ✓ Done  │    │  ✓ Done  │    │  ⏳ Pending│    │       ││
│  └──────────┘    └──────────┘    └──────────┘    └────────┘ │
│                                                              │
│  Progress: 2/4 steps completed                               │
│  Started: 2 minutes ago                                      │
│  ETA: 3 minutes                                              │
│                                                              │
└──────────────────────────────────────────────────────────────┘
```

### 3.14 State Machine Diagram

```
┌──────────────────────────────────────────────────────────────┐
│ State Machine: Order Processing                              │
├──────────────────────────────────────────────────────────────┤
│                                                              │
│        ┌──────────┐        ┌──────────┐        ┌──────────┐ │
│        │ CREATED  │───────▶│ PENDING  │───────▶│ APPROVED │ │
│        └──────────┘        └────┬─────┘        └────┬─────┘ │
│                              │                    │         │
│                              ▼                    ▼         │
│                        ┌──────────┐        ┌──────────┐    │
│                        │ REJECTED │        │ SHIPPED  │    │
│                        └──────────┘        └────┬─────┘    │
│                                                   │         │
│                                                   ▼         │
│                                            ┌──────────┐   │
│                                            │DELIVERED │   │
│                                            └──────────┘   │
│                                                              │
│  Current State: ● APPROVED                                   │
│  Transitions: 4 total                                        │
│  Last transition: 5 minutes ago                              │
│                                                              │
└──────────────────────────────────────────────────────────────┘
```

---

## 4. Page Wireframes

### 4.1 Login Page

```
┌────────────────────────────────────────────────────────────────────┐
│                                                                    │
│                                                                    │
│                                                                    │
│                    ┌──────────────────────────────┐               │
│                    │                              │               │
│                    │         ┌────┐               │               │
│                    │         │    │               │               │
│                    │         │ 🏔️ │               │               │
│                    │         │    │               │               │
│                    │         └────┘               │               │
│                    │                              │               │
│                    │       Gogidix Ecosystem      │               │
│                    │    AI & Orchestration        │               │
│                    │         Monitoring           │               │
│                    │                              │               │
│                    │  ┌────────────────────────┐  │               │
│                    │  │ Email or Username      │  │               │
│                    │  └────────────────────────┘  │               │
│                    │                              │               │
│                    │  ┌────────────────────────┐  │               │
│                    │  │ Password           [👁] │  │               │
│                    │  └────────────────────────┘  │               │
│                    │                              │               │
│                    │  [✓] Remember me    Forgot?  │               │
│                    │                              │               │
│                    │  ┌────────────────────────┐  │               │
│                    │  │    Sign In             │  │               │
│                    │  └────────────────────────┘  │               │
│                    │                              │               │
│                    │         Or sign in with       │               │
│                    │    [SSO]    [Google]         │               │
│                    │                              │               │
│                    └──────────────────────────────┘               │
│                                                                    │
│                                                                    │
└────────────────────────────────────────────────────────────────────┘
```

### 4.2 Dashboard Overview Page

```
┌────────────────────────────────────────────────────────────────────┐
│ Gogidix [Logo] │ Search... │ [🔔] [John Doe ▼]                    │
├──────┬─────────────────────────────────────────────────────────────┤
│      │ Dashboard Overview                                   [🔃] │
│      │ Real-time monitoring of all Foundation services            │
│      ├─────────────────────────────────────────────────────────────┤
│      │ ┌────────────┐ ┌────────────┐ ┌────────────┐ ┌───────────┐│
│      │ │   Health   │ │  Services  │ │   Alerts   │ │  Throughput││
│      │ │             │ │             │ │             │ │           ││
│      │ │    97%      │ │   55/57     │ │    3 New    │ │  12.4K/s  ││
│      │ │   ↗ +2%     │ │   2 Degrade │ │   1 Critical│ │  ↗ +8%    ││
│      │ │ ┌───────┐   │ │ ┌───────┐   │ │ ┌───────┐   │ │ ┌─────┐   ││
│      │ │ │ Chart │   │ │ │ Pie   │   │ │ │ List  │   │ │ │Line │   ││
│      │ │ └───────┘   │ │ └───────┘   │ │ └───────┘   │ │ └─────┘   ││
│      │ └────────────┘ └────────────┘ └────────────┘ └───────────┘│
│      │                                                             │
│      │ ┌────────────────────────────────────────────────────────┐ │
│      │ │ Service Health Matrix                                  │ │
│      │ ├────────────────────────────────────────────────────────┤ │
│      │ │ AI Services        │ ████░ 46/48  Healthy              │ │
│      │ │ Orchestration       │ █████  5/5   Healthy              │ │
│      │ │ Transaction Orch.   │ █████  3/3   Healthy              │ │
│      │ │ Universal Tracking  │ █████  1/1   Healthy              │ │
│      │ └────────────────────────────────────────────────────────┘ │
│      │                                                             │
│      │ ┌─────────────────────────────┐ ┌────────────────────────┐ │
│      │ │ Active Workflows            │ │ Recent Transactions    │ │
│      │ ├─────────────────────────────┤ ├────────────────────────┤ │
│      │ │ wf-onboarding-12345  Running│ │ saga-order-456789 Active│ │
│      │ │ wf-training-001      Active │ │ saga-payment-123456 Done │ │
│      │ │ wf-approval-789      Active │ │ saga-shipping-234567 Act.│ │
│      │ │ [View All 245]             │ │ [View All 1.2K]        │ │
│      │ └─────────────────────────────┘ └────────────────────────┘ │
│      │                                                             │
│      │ ┌────────────────────────────────────────────────────────┐ │
│      │ │ Real-time Metrics Stream                               │ │
│      │ ├────────────────────────────────────────────────────────┤ │
│      │ │ 14:32:15 ai-inference    45ms  ✓ Success               │ │
│      │ │ 14:32:15 ai-analytics    89ms  ✓ Success               │ │
│      │ │ 14:32:14 ai-gateway      23ms  ✓ Success               │ │
│      │ │ 14:32:14 ai-inference    52ms  ✓ Success               │ │
│      │ │ 14:32:13 ai-training     N/A   ⏳ Processing            │ │
│      │ └────────────────────────────────────────────────────────┘ │
│      │                                                             │
└──────┴─────────────────────────────────────────────────────────────┘
```

### 4.3 AI Services - Service Catalog Page

```
┌────────────────────────────────────────────────────────────────────┐
│ Gogidix │ Search services... │ [🔔] [John]                         │
├──────┬─────────────────────────────────────────────────────────────┤
│ AI   │ AI Services › Service Catalog                         [🔃] │
│ Serv │                                                             │
│      │ ┌─────────────────────────────────────────────────────────┐│
│      │ │ Filters: [Core ▼] [All Status ▼] Sort: [Name ▲]        ││
│      │ └─────────────────────────────────────────────────────────┘│
│      │                                                             │
│      │ ┌────────────────────────────────────────────────────────┐ │
│      │ │ ┌────┐ ai-analytics-service                    [⋮]    │ │
│      │ │ │ 📊 │ Analytics & Reporting Engine v1.2.0            │ │
│      │ │ │    │                                              │ │
│      │ │ └────┘ ● Healthy  892 req/s  67ms  0.01% errors      │ │
│      │ │ ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━ │ │
│      │ │ CPU: 45%  Memory: 2.1GB  GPU: N/A                     │ │
│      │ └────────────────────────────────────────────────────────┘ │
│      │                                                             │
│      │ ┌────────────────────────────────────────────────────────┐ │
│      │ │ ┌────┐ ai-inference-service                   [⋮]    │ │
│      │ │ │ 🤖 │ AI Inference Engine v2.1.0                    │ │
│      │ │ │    │                                              │ │
│      │ │ └────┘ ● Healthy  1,234 req/s  45ms  0.02% errors    │ │
│      │ │ ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━ │ │
│      │ CPU: 72%  Memory: 4.5GB  GPU: 89%                       │ │
│      │ └────────────────────────────────────────────────────────┘ │
│      │                                                             │
│      │ ┌────────────────────────────────────────────────────────┐ │
│      │ │ ┌────┐ ai-model-training-service                [⋮]    │ │
│      │ │ │ 🧠 │ Model Training Service v3.0.1                 │ │
│      │ │ │    │                                              │ │
│      │ │ └────┘ 🟡 Degraded  Active: 3 jobs  ETA: 2h 15m      │ │
│      │ │ ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━ │ │
│      │ CPU: 95%  Memory: 15.2GB  GPU: 98% (high utilization)   │ │
│      │ └────────────────────────────────────────────────────────┘ │
│      │                                                             │
│      │                             1-10 of 48    [◀] 1 2 3 4 5 [▶] │
│      │                                                             │
└──────┴─────────────────────────────────────────────────────────────┘
```

### 4.4 AI Services - Service Detail Page

```
┌────────────────────────────────────────────────────────────────────┐
│ Gogidix │ Search... │ [🔔] [John]                                 │
├──────┬─────────────────────────────────────────────────────────────┤
│      │ AI Services › Catalog › ai-inference-service         [🔃] │
│      │                                                             │
│      │ ┌────┐ ai-inference-service                    [Edit][Restart]│
│      │ │ 🤖 │ AI Inference Engine v2.1.0                           │
│      │ │    │ Last deployed: 2 days ago by john.doe               │
│      │ └────┘                                                     │
│      │ ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━│
│      │ ● Operational  Uptime: 99.97%  Region: us-east-1            │
│      │                                                             │
│      │ ┌─────────────┐ ┌─────────────┐ ┌─────────────┐           │
│      │ │  Requests   │ │   Latency   │ │   Errors    │           │
│      │ │   1.2M/day  │ │    45ms     │ │    0.02%    │           │
│      │ │   ↗ +5%     │ │   ↘ -3ms    │ │   ✓ Stable  │           │
│      │ │ ┌─────────┐ │ │ ┌─────────┐ │ │ ┌─────────┐ │           │
│      │ │ │ Sparkline│ │ │ Sparkline│ │ │ Sparkline│ │           │
│      │ │ └─────────┘ │ │ └─────────┘ │ │ └─────────┘ │           │
│      │ └─────────────┘ └─────────────┘ └─────────────┘           │
│      │                                                             │
│      │ ┌────────────────────────────────────────────────────────┐ │
│      │ │ [Overview] [Metrics] [Logs] [Models] [Configuration]   │ │
│      │ ├────────────────────────────────────────────────────────┤ │
│      │ │                                                         │ │
│      │ │ Request Volume (24h)                                    │ │
│      │ │ 2K ┌─┐                                                  │ │
│      │ │    │ │    ┌────┐                   ┌───┐               │ │
│      │ │ 1K │ └────┘    │    ┌─────┐       ┌─┘   └──┐          │ │
│      │ │ 0  └────────────┴────┴────────┴───┴─────────┴── Time   │ │
│      │ │                                                         │ │
│      │ └────────────────────────────────────────────────────────┘ │
│      │                                                             │
│      │ ┌─────────────────────────────┐ ┌─────────────────────────┐│
│      │ │ Resource Utilization        │ │ Active Models           ││
│      │ ├─────────────────────────────┤ ├─────────────────────────┤│
│      │ │ CPU     ████████░░  72%     │ │ fraud-detection-v3      ││
│      │ │ Memory  ██████░░░░  45%     │ │ recommendation-engine-v2 ││
│      │ │ GPU     ██████████  89%     │ │ sentiment-analysis-v1   ││
│      │ │ Storage ████░░░░░░  32%     │ │ + 5 more               ││
│      │ └─────────────────────────────┘ └─────────────────────────┘│
│      │                                                             │
│      │ ┌────────────────────────────────────────────────────────┐ │
│      │ │ Recent Alerts                                           │ │
│      │ ├────────────────────────────────────────────────────────┤ │
│      │ │ [🟡] 2h ago  GPU usage high                             │ │
│      │ │ [✓]  5h ago  Auto-scaled to 4 instances                 │ │
│      │ │ [✓]  1d ago  Model v3 deployed successfully             │ │
│      │ └────────────────────────────────────────────────────────┘ │
│      │                                                             │
└──────┴─────────────────────────────────────────────────────────────┘
```

### 4.5 Orchestration - Active Workflows Page

```
┌────────────────────────────────────────────────────────────────────┐
│ Gogidix │ Search... │ [🔔] [John]                                 │
├──────┬─────────────────────────────────────────────────────────────┤
│ Orch │ Orchestration › Active Workflows                      [🔃] │
│      │                                                             │
│      │ ┌─────────────────────────────────────────────────────────┐│
│      │ │ Status: [All ▼]  Type: [All ▼]  Search: [          ]   ││
│      │ └─────────────────────────────────────────────────────────┘│
│      │                                                             │
│      │ ┌────────────────────────────────────────────────────────┐ │
│      │ │ wf-onboarding-user-12345                     [Running]││
│      │ │ ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━  ││
│      │ │ Type: User Onboarding  Started: 5 min ago  ETA: 2 min   ││
│      │ │ ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━  ││
│      │ │ ┌────┐ ┌────┐ ┌────┐ ┌────┐ ┌────┐ ┌────┐ ┌────┐     ││
│      │ │ │Create│→│Send │→│Verify│→│Setup│→│Complete│      ││
│      │ │ │Acct │ │Email│ │Email│ │Prof │ │      │      ││
│      │ │ │ ✓  │ │ ✓  │ │ ⏳  │ │    │ │    │ │    │     ││
│      │ │ └────┘ └────┘ └────┘ └────┘ └────┘ └────┘ └────┘     ││
│      │ │ 3/7 steps completed  Progress: ━━━━━━━░░░░ 43%         ││
│      │ │ [View Details] [Pause] [Cancel]                        ││
│      │ └────────────────────────────────────────────────────────┘ │
│      │                                                             │
│      │ ┌────────────────────────────────────────────────────────┐ │
│      │ │ wf-training-model-456                        [Running]││
│      │ │ ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━  ││
│      │ │ Type: Model Training  Started: 2h ago  ETA: 45min       ││
│      │ │ ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━  ││
│      │ │ ┌─────┐ ┌─────┐ ┌─────┐ ┌─────┐ ┌─────┐                ││
│      │ │ │Data │→│Train│→│Valid│→│Deploy│→│Notify│              ││
│      │ │ │Prep │ │Model│ │ate  │ │     │ │     │                ││
│      │ │ │ ✓  │ │ ⏳  │ │    │ │    │ │    │                ││
│      │ │ └─────┘ └─────┘ └─────┘ └─────┘ └─────┘                ││
│      │ │ 2/5 steps completed  Progress: ━━━━━━━░░░░░ 40%        ││
│      │ │ Current Epoch: 234/500  Accuracy: 87.3%                 ││
│      │ │ [View Details] [Pause] [Cancel]                        ││
│      │ └────────────────────────────────────────────────────────┘ │
│      │                                                             │
│      │ ┌────────────────────────────────────────────────────────┐ │
│      │ │ wf-order-processing-789                      [Failed] ││
│      │ │ ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━  ││
│      │ │ Type: Order Processing  Started: 1h ago  Failed: 5m   ││
│      │ │ ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━  ││
│      │ │ ┌────┐ ┌────┐ ┌────┐ ┌────┐ ┌────┐                    ││
│      │ │ │Create│→│Validate│→│Process│→│Ship│→│Complete│       ││
│      │ │ │Order│ │Payment│ │Order│ │    │ │        │           ││
│      │ │ │ ✓  │ │ ✓  │ │ ✗  │ │    │ │        │           ││
│      │ │ └────┘ └────┘ └────┘ └────┘ └────┘                    ││
│      │ │ 2/5 steps completed  Failed at: Process Order          ││
│      │ │ Error: Payment gateway timeout                         ││
│      │ │ [View Error] [Retry] [Manual Recovery]                 ││
│      │ └────────────────────────────────────────────────────────┘ │
│      │                                                             │
│      │                    1-10 of 245      [◀] 1 2 3 ... 25 [▶]   │
│      │                                                             │
└──────┴─────────────────────────────────────────────────────────────┘
```

### 4.6 Transactions - Saga Monitor Page

```
┌────────────────────────────────────────────────────────────────────┐
│ Gogidix │ Search... │ [🔔] [John]                                 │
├──────┬─────────────────────────────────────────────────────────────┤
│ Trans│ Transactions › Saga Monitor                           [🔃] │
│      │                                                             │
│      │ ┌─────────────────────────────────────────────────────────┐│
│      │ │ Status: [All ▼]  Search: [                    ]        ││
│      │ └─────────────────────────────────────────────────────────┘│
│      │                                                             │
│      │ ┌────────────────────────────────────────────────────────┐ │
│      │ │ saga-order-456789                             [Active] ││
│      │ │ ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━  ││
│      │ │ Order Processing Saga  Started: 2 min ago              ││
│      │ │ ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━  ││
│      │ │                                                         ││
│      │ │ ┌──────────┐    ┌──────────┐    ┌──────────┐          ││
│      │ │ │Create    │───▶│Validate  │───▶│Process   │          ││
│      │ │ │Order     │    │Payment   │    │Order     │          ││
│      │ │ │  ✓ Done  │    │  ✓ Done  │    │  ⏳ Pending│          ││
│      │ │ └──────────┘    └──────────┘    └──────────┘          ││
│      │ │                                     │                  ││
│      │ │                                     ▼                  ││
│      │ │                            ┌──────────┐    ┌──────────┐││
│      │ │                            │Ship      │───▶│Complete  │││
│      │ │                            │          │    │          │││
│      │ │                            │          │    │          │││
│      │ │                            └──────────┘    └──────────┘││
│      │ │                                                         ││
│      │ │ Current Action: Process Order                           ││
│      │ │ Participant: order-processing-service                  ││
│      │ │ [View Events] [Force Complete] [Trigger Compensation]  ││
│      │ └────────────────────────────────────────────────────────┘ │
│      │                                                             │
│      │ ┌────────────────────────────────────────────────────────┐ │
│      │ │ saga-payment-123456                           [Done]   ││
│      │ │ ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━  ││
│      │ │ Payment Processing Saga  Completed: 5 min ago          ││
│      │ │ ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━  ││
│      │ │ All 5 steps completed successfully                     ││
│      │ │ Duration: 2.3 seconds                                  ││
│      │ │ [View Details] [Replay Events]                         ││
│      │ └────────────────────────────────────────────────────────┘ │
│      │                                                             │
│      │ ┌────────────────────────────────────────────────────────┐ │
│      │ │ saga-refund-789012                            [Failed] ││
│      │ │ ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━  ││
│      │ │ Refund Processing Saga  Failed: 15 min ago             ││
│      │ │ ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━  ││
│      │ │ Failed at: Process Refund (Step 3/5)                   ││
│      │ │ Error: External payment API timeout                    ││
│      │ │                                                         ││
│      │ │ Compensating Actions Triggered:                         ││
│      │ │ • ✓ Lock released                                      ││
│      │ │ • ⏳ Transaction rollback in progress                   ││
│      │ │ • Notification pending                                 ││
│      │ │                                                         ││
│      │ │ [View Error] [Retry Compensation] [Manual Recovery]    ││
│      │ └────────────────────────────────────────────────────────┘ │
│      │                                                             │
└──────┴─────────────────────────────────────────────────────────────┘
```

### 4.7 Tracking - Real-time Metrics Page

```
┌────────────────────────────────────────────────────────────────────┐
│ Gogidix │ Search... │ [🔔] [John]                                 │
├──────┬─────────────────────────────────────────────────────────────┤
│ Track│ Tracking › Real-time Metrics                          [🔃] │
│      │                                                             │
│      │ ┌─────────────────────────────────────────────────────────┐│
│      │ │ Time Range: [Last 1h ▼]  Refresh: [5s ▼]  ● Live       ││
│      │ └─────────────────────────────────────────────────────────┘│
│      │                                                             │
│      │ ┌────────────────────────────────────────────────────────┐ │
│      │ │ Request Rate (requests/second)                          │ │
│      │ │ 15K ┌─┐                                                  │ │
│      │ │     │ │    ┌──┐      ┌─┐                              │ │
│      │ │ 10K │ └────┘  │    ┌─┘ └───┐   ┌────┐                 │ │
│      │ │  5K │         └───┘        └───┘    └───┐             │ │
│      │ │  0  └─────────────────────────────────────┴─ Time     │ │
│      │ │ 14:00   14:15   14:30   14:45   15:00   15:15         │ │
│      │ └────────────────────────────────────────────────────────┘ │
│      │                                                             │
│      │ ┌──────────────────────┐ ┌──────────────────────────────┐ │
│      │ │ P50 Latency          │ │ Error Rate                   │ │
│      │ │ ┌────────────────────┤ │                              │ │
│      │ │ │45ms ┃               │ │ 0.02%                        │ │
│      │ │ │40ms ┃ ████          │ │ ━━━━━━━━━━━━━━━━━━━━━━━━━━ │ │
│      │ │ │35ms ┃ ██████        │ │                              │ │
│      │ │ │30ms ┃ ████████      │ │ Current: ✓ Within threshold │ │
│      │ │ │25ms ┃██████████     │ │ SLO: <0.1%                   │ │
│      │ │ └────────────────────┤ │                              │ │
│      │ └──────────────────────┘ └──────────────────────────────┘ │
│      │                                                             │
│      │ ┌────────────────────────────────────────────────────────┐ │
│      │ │ Throughput by Service                                   │ │
│      │ ├────────────────────────────────────────────────────────┤ │
│      │ │ ai-inference     ████████████████████  1,234/s         │ │
│      │ │ ai-analytics     ████████████░░░░░░░░░    892/s         │ │
│      │ │ ai-gateway       ██████████████░░░░░░░  1,156/s         │ │
│      │ │ ai-recommend     ████████░░░░░░░░░░░░░    567/s         │ │
│      │ │ ai-nlp          ██████░░░░░░░░░░░░░░░    345/s         │ │
│      │ └────────────────────────────────────────────────────────┘ │
│      │                                                             │
│      │ ┌────────────────────────────────────────────────────────┐ │
│      │ │ Live Log Stream                                         │ │
│      │ ├────────────────────────────────────────────────────────┤ │
│      │ │ 14:32:15.678 [ai-inference] 200 OK  45ms  /predict      │ │
│      │ │ 14:32:15.681 [ai-analytics]   200 OK  89ms  /aggregate   │ │
│      │ │ 14:32:15.690 [ai-gateway]     200 OK  23ms  /route       │ │
│      │ │ 14:32:15.712 [ai-inference]  200 OK  52ms  /batch        │ │
│      │ │ 14:32:15.745 [ai-nlp]         200 OK 156ms  /sentiment   │ │
│      │ │ 14:32:15.789 [ai-inference]  200 OK  41ms  /predict      │ │
│      │ │ 14:32:15.823 [ai-recommend]  200 OK  67ms  /suggest      │ │
│      │ └────────────────────────────────────────────────────────┘ │
│      │                                                             │
└──────┴─────────────────────────────────────────────────────────────┘
```

---

## 5. Responsive Design

### 5.1 Breakpoints

```css
/* Mobile First Approach */
--screen-xs:  320px;   /* Very small phones */
--screen-sm:  640px;   /* Small phones, landscape */
--screen-md:  768px;   /* Tablets */
--screen-lg:  1024px;  /* Small laptops, large tablets */
--screen-xl:  1280px;  /* Desktops */
--screen-2xl: 1536px;  /* Large desktops */
```

### 5.2 Responsive Grid

```
Mobile (320px+):
┌────────────┐
│  100%      │  Single column, stacked
│  width     │
└────────────┘

Tablet (768px+):
┌────────────┬────────────┐
│    50%     │    50%     │  2 columns
└────────────┴────────────┘

Desktop (1024px+):
┌────┬──────────┬──────────┬──────────┐
│256 │   25%    │   25%    │   25%    │  Sidebar + 3 cols
│    │          │          │          │
└────┴──────────┴──────────┴──────────┘

Large Desktop (1280px+):
┌────┬─────────┬─────────┬─────────┬─────────┐
│256 │  ~25%   │  ~25%   │  ~25%   │  ~25%   │
│    │         │         │         │         │
└────┴─────────┴─────────┴─────────┴─────────┘
```

---

## 6. Accessibility Considerations

### 6.1 Color Contrast Ratios

| Combination | Ratio | WCAG Level |
|-------------|-------|------------|
| Primary 500 on White | 4.7:1 | AA |
| Primary 700 on White | 7.8:1 | AAA |
| Success 500 on White | 4.6:1 | AA |
| Error 500 on White | 4.5:1 | AA |
| Gray 800 on White | 12.6:1 | AAA |

### 6.2 Focus Indicators

```
┌────────────────────┐
│ [Tab Stop]         │ ← 2px solid primary-500
│                    │   3px offset
└────────────────────┘
```

### 6.3 Screen Reader Support

- Proper heading hierarchy (H1 → H2 → H3)
- ARIA labels for icon-only buttons
- Live regions for dynamic content
- Semantic HTML elements
- Alt text for all images

---

## Document Info

**Document Version:** 1.0
**Last Updated:** 2025-02-08
**Author:** Gogidix Architecture Team
**Related Documents:**
- 01_UI_Flow_Documentation.md
- 03_Mock_Flow_Documentation.md
- 04_Page_By_Page_Flow_Documentation.md
