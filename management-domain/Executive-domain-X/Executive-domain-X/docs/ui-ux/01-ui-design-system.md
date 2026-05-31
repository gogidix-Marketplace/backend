# Gogidix Executive Domain - UI Design System

**Version:** 4.0
**Last Updated:** 2026-04-22
**Status:** Active
**Design Philosophy:** Executive Excellence - Clarity, Authority, Insight

---

## Table of Contents

1. [Design Principles](#design-principles)
2. [Color Palette](#color-palette)
3. [Typography System](#typography-system)
4. [Spacing & Layout](#spacing--layout)
5. [Component Standards](#component-standards)
6. [Motion & Animation](#motion--animation)
7. [Data Visualization](#data-visualization)
8. [Accessibility Standards](#accessibility-standards)
9. [Departmental Integration Components](#9-departmental-integration-components)
10. [Approval Workflow Components](#10-approval-workflow-components)
11. [Department Reporting Components](#11-department-reporting-components)
12. [Business Unit Identity Components](#12-business-unit-identity-components)
13. [Business Unit Revenue Components](#13-business-unit-revenue-components)
14. [Business Unit Service Health Components](#14-business-unit-service-health-components)

---

## 1. Design Principles

### Executive-First Design Philosophy

Our design system is built for C-level executives who need to make critical decisions quickly. Every element serves the principles of:

| Principle | Description | Implementation |
|-----------|-------------|----------------|
| **Clarity** | Information must be instantly comprehensible | High contrast, clear hierarchy, minimal cognitive load |
| **Authority** | Convey trust and professional excellence | Refined typography, premium color palette, consistent spacing |
| **Insight** | Surface what matters most | Progressive disclosure, smart defaults, AI-assisted highlighting |
| **Efficiency** | Reduce clicks, increase decisions | Keyboard navigation, bulk actions, intelligent defaults |
| **Context** | Always show where you are and what you're seeing | Breadcrumbs, domain indicators, location badges |

### Anti-Patterns to Avoid

- **Generic AI aesthetics:** No purple gradients on white backgrounds
- **Overwhelming dashboards:** Avoid data density without hierarchy
- **Click-heavy workflows:** Minimize steps to key actions
- **Ambiguous status:** Always use clear, color-coded status indicators
- **Hidden information:** Use progressive disclosure, don't bury critical data

---

## 2. Color Palette

### Executive Theme Colors

```css
/* Primary - Authority Blue */
--color-primary-50:  #E3F2FD;
--color-primary-100: #BBDEFB;
--color-primary-200: #90CAF9;
--color-primary-300: #64B5F6;
--color-primary-400: #42A5F5;
--color-primary-500: #2196F3;
--color-primary-600: #1E88E5;
--color-primary-700: #1976D2;
--color-primary-800: #1565C0;
--color-primary-900: #0D47A1;
--color-primary-950: #0A1929;

/* Role-Specific Accents */
--color-ceo-accent:   #FFA000;  /* Strategic Gold */
--color-cfo-accent:   #FF6B00;  /* Financial Orange */
--color-coo-accent:   #00BCD4;  /* Operational Cyan */
--color-cto-accent:   #7C4DFF;  /* Technology Purple */
```

### Semantic Colors

```css
/* Status Colors */
--color-success:  #10B981;  /* Green - On track, healthy */
--color-warning:  #F59E0B;  /* Amber - At risk, attention */
--color-danger:   #EF4444;  /* Red - Critical, behind */
--color-info:     #3B82F6;  /* Blue - Informational */
--color-neutral:  #6B7280;  /* Gray - Pending, unknown */
```

### Background System

```css
/* Surface Colors */
--color-bg-primary:   #FFFFFF;     /* Main content */
--color-bg-secondary: #F8FAFC;     /* Cards, panels */
--color-bg-tertiary:  #F1F5F9;     /* Nested sections */
--color-bg-elevated:  #0D47A1;     /* Header, primary actions */
```

### Gradient System

```css
/* Header Gradients by Role */
--gradient-ceo: linear-gradient(135deg, #0D47A1 0%, #1565C0 100%);
--gradient-cfo: linear-gradient(135deg, #0D47A1 0%, #FFA000 100%);
--gradient-coo: linear-gradient(135deg, #0D47A1 0%, #00BCD4 100%);
--gradient-cto: linear-gradient(135deg, #0D47A1 0%, #7C4DFF 100%);

/* AI/Intelligence Accent */
--gradient-ai: linear-gradient(135deg, #7C4DFF 0%, #00E5FF 100%);
```

### Dark Mode (Future)

```css
[data-theme="dark"] {
  --color-bg-primary:   #0F172A;
  --color-bg-secondary: #1E293B;
  --color-bg-tertiary:  #334155;
}
```

---

## 3. Typography System

### Font Families

```css
/* Primary Font - Inter (body text) */
--font-sans: 'Inter', -apple-system, BlinkMacSystemFont, 'Segoe UI', sans-serif;

/* Display Font - Space Grotesk (headings, numbers) */
--font-display: 'Space Grotesk', 'Inter', sans-serif;

/* Mono Font - JetBrains Mono (code, data) */
--font-mono: 'JetBrains Mono', 'Fira Code', monospace;
```

### Type Scale

```css
/* Display */
--text-display-2xl: 4.5rem/1.1;    /* 72px - Hero titles */
--text-display-xl:   3.75rem/1.1;  /* 60px - Page titles */
--text-display-lg:   3rem/1.2;     /* 48px - Section headers */

/* Headings */
--text-h1: 2.5rem/1.2;     /* 40px - Main page title */
--text-h2: 2rem/1.3;       /* 32px - Section title */
--text-h3: 1.5rem/1.4;     /* 24px - Subsection */
--text-h4: 1.25rem/1.5;    /* 20px - Card title */
--text-h5: 1.125rem/1.5;   /* 18px - Label */
--text-h6: 1rem/1.5;       /* 16px - Small label */

/* Body */
--text-body-lg: 1.125rem/1.6;   /* 18px - Lead text */
--text-body:   1rem/1.6;        /* 16px - Default */
--text-body-sm: 0.875rem/1.6;   /* 14px - Secondary */

/* Captions */
--text-caption: 0.75rem/1.5;    /* 12px - Labels, hints */
--text-overline: 0.625rem/1.5;  /* 10px - Tags */
```

### Font Weights

```css
--font-light:    300;
--font-regular:  400;
--font-medium:   500;
--font-semibold: 600;
--font-bold:     700;
```

### Typography Usage Guidelines

| Context | Font Family | Size | Weight | Example |
|---------|-------------|------|--------|---------|
| Page Title | Display | h1 (40px) | Bold | CEO Dashboard |
| Section Header | Display | h2 (32px) | Semibold | Strategic KPIs |
| Card Title | Sans | h4 (20px) | Medium | Revenue |
| Metric Value | Display | 36-48px | Bold | $42.5M |
| Body Text | Sans | body (16px) | Regular | Description text |
| Data Labels | Sans | caption (12px) | Medium | Label |

---

## 4. Spacing & Layout

### Spacing Scale (8px Grid)

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
--spacing-20:  5rem;     80px */
--spacing-24:  6rem;     96px */
```

### Container Widths

```css
--container-xs:  20rem;   /* 320px - Mobile */
--container-sm:  24rem;   /* 384px - Small */
--container-md:  28rem;   /* 448px - Medium */
--container-lg:  32rem;   /* 512px - Large */
--container-xl:  36rem;   /* 576px - XLarge */
--container-2xl: 42rem;   /* 672px - 2XLarge */
--container-full: 100%;   /* Full width */
```

### Breakpoint System

| Breakpoint | Min Width | Max Width | Target Devices |
|------------|-----------|-----------|----------------|
| `xs` | 0px | 599px | Mobile portrait |
| `sm` | 600px | 899px | Mobile landscape |
| `md` | 900px | 1199px | Tablet portrait |
| `lg` | 1200px | 1535px | Desktop |
| `xl` | 1536px | ∞ | Large desktop |

### Layout Grid

```css
/* Dashboard Grid - 12 columns */
--grid-columns: 12;
--grid-gap: 1.5rem;  /* 24px */

/* Sidebar */
--sidebar-width: 16rem;      /* 256px - Expanded */
--sidebar-width-collapsed: 4rem;  /* 64px - Collapsed */

/* Header */
--header-height: 4rem;  /* 64px */
```

---

## 5. Component Standards

### KPI Card

```
┌─────────────────────────────────┐
│  [Icon]  Revenue          [⋮]   │
│                          $42.5M │
│                    ▲ 8%  ● On   │
│                    ████████░░   │
└─────────────────────────────────┘
```

**Specs:**
- Min width: 180px
- Padding: 16px
- Border radius: 12px
- Shadow: sm (0 1px 3px rgba(0,0,0,0.1))
- Background: white

### Status Badge

```
  ● On Track    ● At Risk    ● Critical
  Green pill   Amber pill   Red pill
```

**Specs:**
- Height: 24px
- Padding: 4px 12px
- Border radius: 12px
- Font size: 12px
- Font weight: 500

### Button Styles

| Variant | Background | Text | Hover | Usage |
|---------|------------|------|-------|-------|
| Primary | Brand gradient | White | Lighten | Primary actions |
| Secondary | White/Gray | Brand | Border | Secondary actions |
| Ghost | Transparent | Brand | Background | Tertiary |
| Danger | Red | White | Darken | Destructive |

---

## 6. Motion & Animation

### Animation Duration

```css
--duration-instant: 100ms;
--duration-fast:    150ms;
--duration-normal:  200ms;
--duration-slow:    300ms;
--duration-slower:  500ms;
```

### Easing Functions

```css
--ease-out: cubic-bezier(0.16, 1, 0.3, 1);
--ease-in-out: cubic-bezier(0.4, 0, 0.2, 1);
--ease-spring: cubic-bezier(0.34, 1.56, 0.64, 1);
```

### Animation Standards

| Trigger | Animation | Duration | Easing |
|---------|-----------|----------|--------|
| Page load | Staggered fade-in | 300ms | Stagger 50ms |
| Card hover | Subtle lift | 200ms | ease-out |
| Button press | Scale down | 100ms | ease-in-out |
| Modal open | Scale + fade | 300ms | ease-spring |
| Sidebar toggle | Slide | 300ms | ease-in-out |

### Micro-interactions

- **Hover:** Cards lift 4px, shadow increases
- **Focus:** 2px brand color outline
- **Active:** Scale 0.98
- **Loading:** Skeleton with shimmer animation
- **Success:** Green checkmark animation
- **Error:** Shake animation

---

## 7. Data Visualization

### Chart Colors

```css
/* Primary Chart Palette */
--chart-color-1: #0D47A1;  /* Blue */
--chart-color-2: #FFA000;  /* Gold */
--chart-color-3: #10B981;  /* Green */
--chart-color-4: #7C4DFF;  /* Purple */
--chart-color-5: #EF4444;  /* Red */
--chart-color-6: #00BCD4;  /* Cyan */
--chart-color-7: #F59E0B;  /* Orange */
--chart-color-8: #6366F1;  /* Indigo */
```

### Chart Guidelines

| Chart Type | Use For | Max Data Points |
|------------|---------|-----------------|
| Line | Trends over time | 12-24 |
| Bar | Comparisons | 8-12 |
| Area | Volume + trends | 12-24 |
| Donut | Proportions | 3-6 |
| Sparkline | Mini trends | 7-30 |

### Metric Formatting

| Type | Format | Example |
|------|--------|---------|
| Currency | $X.XXB | $42.5M |
| Percentage | X.X% | 12.3% |
| Count | X,XXX | 2,847 |
| Ratio | X:Y | 3:1 |
| Duration | Xh Ym | 2h 15m |

---

## 8. Accessibility Standards

### WCAG 2.1 AA Compliance

- **Color Contrast:** Minimum 4.5:1 for text, 3:1 for large text
- **Touch Targets:** Minimum 44x44px
- **Keyboard Navigation:** All features accessible via keyboard
- **Screen Reader:** Semantic HTML, ARIA labels
- **Focus Indicators:** Visible 2px outline

### Screen Reader Support

```html
<!-- Good Example -->
<button aria-label="View revenue details">
  <RevenueIcon />
</button>

<!-- KPI Card with Screen Reader Support -->
<div role="region" aria-label="Revenue KPI">
  <h2>Revenue</h2>
  <p aria-label="42.5 million dollars, up 8 percent from last month">$42.5M</p>
</div>
```

### Keyboard Shortcuts

| Shortcut | Action |
|----------|--------|
| `Cmd/Ctrl + K` | Command palette |
| `Cmd/Ctrl + /` | Keyboard shortcuts help |
| `Esc` | Close modal/drawer |
| `← →` | Navigate dashboard tabs |
| `Enter` | Select focused item |

---

## 9. Departmental Integration Components

### Department Color System

```css
/* Department Identity Colors */
--color-dept-marketing:    #E91E63;  /* Pink - Digital Marketing */
--color-dept-support:      #00BCD4;  /* Cyan - Customer Support */
--color-dept-business:     #4CAF50;  /* Green - Global Business Mgmt */
--color-dept-hr:           #FF9800;  /* Orange - Human Resource */
--color-dept-sales:        #2196F3;  /* Blue - Sales Department */
--color-dept-sysadmin:     #9C27B0;  /* Purple - System Administrator */
--color-dept-finance:      #FF5722;  /* Deep Orange - Finance Department */
--color-dept-foundation:   #607D8B;  /* Blue Grey - Foundation Monitoring */
```

### Department Badge Component

```
┌─────────────────────────────────┐
│  [Icon]  Digital Marketing  ●   │
└─────────────────────────────────┘
```

**Specs:**
- Height: 28px
- Padding: 4px 12px
- Border radius: 14px
- Font size: 12px
- Font weight: 600
- Left border: 3px department color
- Background: department color at 10% opacity
- Icon: department-specific (see mapping below)

| Department | Icon | Color | CSS Class |
|---|---|---|---|
| Digital Marketing | `Megaphone` | `#E91E63` | `.dept-marketing` |
| Customer Support | `Headphones` | `#00BCD4` | `.dept-support` |
| Global Business Mgmt | `Globe` | `#4CAF50` | `.dept-business` |
| Human Resource | `Users` | `#FF9800` | `.dept-hr` |
| Sales | `TrendingUp` | `#2196F3` | `.dept-sales` |
| System Administrator | `Shield` | `#9C27B0` | `.dept-sysadmin` |
| Finance | `DollarSign` | `#FF5722` | `.dept-finance` |
| Foundation Monitoring | `Activity` | `#607D8B` | `.dept-foundation` |

### Department Selector Dropdown

```
┌──────────────────────────────────────┐
│  Filter by Department          [▼]   │
├──────────────────────────────────────┤
│  ● All Departments                   │
│  ─────────────────────────────────── │
│  [Megaphone]  Digital Marketing      │
│  [Headphones] Customer Support       │
│  [Globe]      Global Business Mgmt   │
│  [Users]      Human Resource         │
│  [TrendingUp] Sales                  │
│  [Shield]     System Administrator   │
│  [DollarSign] Finance                │
│  [Activity]   Foundation Monitoring  │
└──────────────────────────────────────┘
```

**Specs:**
- Dropdown width: 280px
- Item height: 40px
- Multi-select: No (single department filter)
- Search: Yes (type to filter)
- Active state: Department color border + checkmark

### Department KPI Grid Component

```
┌─────────────────────────────────────────────────────────────────────────────────┐
│  DEPARTMENT PERFORMANCE OVERVIEW                              [Filter ▼]      │
│  ┌──────────────┐ ┌──────────────┐ ┌──────────────┐ ┌──────────────┐          │
│  │ [Megaphone]   │ │ [Headphones] │ │ [Globe]      │ │ [Users]      │          │
│  │ Marketing    │ │ Support      │ │ Business     │ │ HR           │          │
│  │              │ │              │ │              │ │              │          │
│  │ ROI: 340%    │ │ CSAT: 4.2    │ │ Rev: $42.5M  │ │ HC: 2,847    │          │
│  │ ▲ 12%  ● OK │ │ ▲ 0.3  ● OK  │ │ ▲ 8%   ● OK  │ │ ▲ 5%   ● OK  │          │
│  │ 3 pending    │ │ 2 SLA alerts │ │ 1 data issue │ │ 0 critical   │          │
│  └──────────────┘ └──────────────┘ └──────────────┘ └──────────────┘          │
│  ┌──────────────┐ ┌──────────────┐ ┌──────────────┐ ┌──────────────┐          │
│  │ [TrendingUp]  │ │ [Shield]     │ │ [DollarSign] │ │ [Activity]   │          │
│  │ Sales        │ │ SysAdmin     │ │ Finance      │ │ Foundation   │          │
│  │              │ │              │ │              │ │              │          │
│  │ Rev: $38.2M  │ │ Uptime: 99.9%│ │ P&L: +$7.8M │ │ Health: 97   │          │
│  │ ▲ 15%  ● OK │ │ ▲ 0.1  ● OK  │ │ ▼ 2%   ● OK  │ │ ▲ 3   ● OK  │          │
│  │ 4 deals      │ │ 1 deploy     │ │ 5 invoices   │ │ 2 alerts     │          │
│  └──────────────┘ └──────────────┘ └──────────────┘ └──────────────┘          │
└─────────────────────────────────────────────────────────────────────────────────┘
```

**Specs:**
- Grid: 4 columns on desktop, 2 on tablet, 1 on mobile
- Card min width: 200px
- Card padding: 16px
- Border left: 4px department color
- Hover: lift 4px + shadow increase
- Click: drills into department detail

---

## 10. Approval Workflow Components

### Approval Queue Component

```
┌─────────────────────────────────────────────────────────────────────────────┐
│  PENDING APPROVALS (7)                              [Filter: All ▼]       │
│  ─────────────────────────────────────────────────────────────────────────  │
│                                                                             │
│  ┌─ 🔴 HIGH PRIORITY ──────────────────────────────────────────────────┐   │
│  │  [DollarSign] Budget Increase Request                               │   │
│  │  Finance → CFO                            Due: Today                 │   │
│  │  Amount: EUR 250,000 | Dept: Digital Marketing                      │   │
│  │  Requested: 2 hours ago by J. Smith (Marketing Director)            │   │
│  │  [Quick Approve ✓] [Review Details →] [Delegate →] [Reject ✕]      │   │
│  └─────────────────────────────────────────────────────────────────────┘   │
│                                                                             │
│  ┌─ 🟡 NORMAL ─────────────────────────────────────────────────────────┐   │
│  │  [Shield] Production Deployment                                     │   │
│  │  SysAdmin → CTO                           Due: Tomorrow             │   │
│  │  Service: payment-gateway v2.4.0 | Region: All                      │   │
│  │  Requested: 4 hours ago by DevOps Lead                              │   │
│  │  [Quick Approve ✓] [Review Details →] [Delegate →] [Reject ✕]      │   │
│  └─────────────────────────────────────────────────────────────────────┘   │
│                                                                             │
│  ┌─ 🟡 NORMAL ─────────────────────────────────────────────────────────┐   │
│  │  [Users] Executive Hire Approval                                    │   │
│  │  HR → CEO                                Due: 3 days               │   │
│  │  Position: VP Sales, Africa | Package: EUR 180K                     │   │
│  │  Requested: 1 day ago by HR Director                                │   │
│  │  [Quick Approve ✓] [Review Details →] [Delegate →] [Reject ✕]      │   │
│  └─────────────────────────────────────────────────────────────────────┘   │
│                                                                             │
│  ─────────────────────────────────────────────────────────────────────────  │
│  [View All 7 →]                    [Bulk Approve] [Export List]            │
└─────────────────────────────────────────────────────────────────────────────┘
```

**Specs:**
- Queue padding: 16px
- Card border-radius: 8px
- Priority indicator: left border (4px)
  - Red (#EF4444) for HIGH
  - Amber (#F59E0B) for NORMAL
  - Green (#10B981) for LOW
- Department badge inline
- Quick Approve: primary button, 1-click
- Review Details: opens modal with full context
- Delegate: opens team member selector
- Reject: opens reason modal

### Approval Detail Modal

```
┌─────────────────────────────────────────────────────────────────────────────┐
│  ┌───────────────────────────────────────────────────────────────────────┐  │
│  │  Budget Increase Request                                       [✕]  │  │
│  ├───────────────────────────────────────────────────────────────────────┤  │
│  │                                                                       │  │
│  │  ┌─────────────────┐  ┌──────────────────┐  ┌──────────────────┐    │  │
│  │  │ [DollarSign]    │  │ Priority: 🔴 HIGH │  │ Status: PENDING  │    │  │
│  │  │ Finance Dept    │  │ Due: Today        │  │ ID: APR-2026-042 │    │  │
│  │  └─────────────────┘  └──────────────────┘  └──────────────────┘    │  │
│  │                                                                       │  │
│  │  REQUEST DETAILS                                                      │  │
│  │  ─────────────────────────────                                        │  │
│  │  Type:        Budget Increase                                         │  │
│  │  Department:  Digital Marketing                                       │  │
│  │  Amount:      EUR 250,000                                             │  │
│  │  Current:     EUR 2,500,000                                           │  │
│  │  Proposed:    EUR 2,750,000 (+10%)                                    │  │
│  │  Requester:   James Smith, Marketing Director                         │  │
│  │  Submitted:   Apr 22, 2026 at 10:30 AM                               │  │
│  │                                                                       │  │
│  │  JUSTIFICATION                                                        │  │
│  │  ─────────────                                                        │  │
│  │  "Q2 Africa expansion requires additional ad spend for               │  │
│  │   Nigeria and Kenya market entry campaigns. Projected ROI            │  │
│  │   is 340% based on Q1 performance."                                  │  │
│  │                                                                       │  │
│  │  BUDGET BREAKDOWN                                                     │  │
│  │  ┌─────────────────────────────────────────────────────────────┐    │  │
│  │  │  Digital Ads (Nigeria)      EUR 120,000                    │    │  │
│  │  │  Digital Ads (Kenya)         EUR  80,000                    │    │  │
│  │  │  Local Events               EUR  30,000                    │    │  │
│  │  │  Content Creation           EUR  20,000                    │    │  │
│  │  │  ─────────────────────────────────────────                  │    │  │
│  │  │  TOTAL                      EUR 250,000                    │    │  │
│  │  └─────────────────────────────────────────────────────────────┘    │  │
│  │                                                                       │  │
│  │  APPROVAL CHAIN                                                       │  │
│  │  ─────────────                                                        │  │
│  │  ✅ Marketing VP (Auto-approved - within delegation)                  │  │
│  │  ⏳ CFO (Pending - You)                                              │  │
│  │  ○  CEO (Awaiting CFO decision)                                      │  │
│  │                                                                       │  │
│  │  ATTACHMENTS                                                          │  │
│  │  ───────────                                                          │  │
│  │  📄 ROI_Analysis_Q1_2026.pdf                                         │  │
│  │  📊 Africa_Market_Research.xlsx                                      │  │
│  │                                                                       │  │
│  │  ┌──────────────────────────────────────────────────────────────┐   │  │
│  │  │  Add Comment:                                                │   │  │
│  │  │  [__________________________________________________________] │   │  │
│  │  └──────────────────────────────────────────────────────────────┘   │  │
│  │                                                                       │  │
│  │  [Request Changes] [Delegate →] [Reject ✕] [Approve ✓]              │  │
│  └───────────────────────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────────────────────┘
```

**Specs:**
- Modal max width: 640px
- Modal padding: 24px
- Section headers: h5 (18px) semibold
- Section spacing: 16px gap
- Approval chain: vertical stepper with icons
- Attachments: clickable file cards
- Comment: textarea with 500 char limit
- Buttons: fixed bottom bar

### Approval Confirmation Toast

```
┌──────────────────────────────────────────────────┐
│  ✓ Approved                                       │
│  Budget increase for Digital Marketing (EUR 250K) │
│  Next: CEO review pending                         │
│                                          [Undo]   │
└──────────────────────────────────────────────────┘
```

**Specs:**
- Position: bottom-right
- Duration: 5 seconds (auto-dismiss)
- Background: success green
- Undo: available for 5 seconds

---

## 11. Department Reporting Components

### Department Report Card

```
┌─────────────────────────────────────────────────────────────────────────────┐
│  ┌──────────┐  DIGITAL MARKETING — Monthly Report          [Download PDF]  │
│  │Megaphone │  April 2026 | Generated: Apr 22, 2026                       │
│  └──────────┘                                                               │
│  ─────────────────────────────────────────────────────────────────────────  │
│                                                                             │
│  KEY METRICS                                                                │
│  ┌──────────┐ ┌──────────┐ ┌──────────┐ ┌──────────┐                     │
│  │ Leads    │ │ CPL      │ │ ROI      │ │ Spend    │                     │
│  │ 4,231    │ │ $32.50   │ │ 340%     │ │ $2.05M   │                     │
│  │ ▲ 18%    │ │ ▼ 12%    │ │ ▲ 45%    │ │ 82% used │                     │
│  └──────────┘ └──────────┘ └──────────┘ └──────────┘                     │
│                                                                             │
│  INSIGHTS                                                                   │
│  💡 Africa campaigns showing 3x ROI vs. Europe                             │
│  ⚠️  Email open rate declining (-8% MoM) — review subject lines            │
│  🔴 Social media engagement dropped in Kenya market                         │
│                                                                             │
│  BUDGET STATUS                                                              │
│  Allocated: $2.5M  |  Spent: $2.05M  |  Remaining: $450K                   │
│  ████████████████████░░░░░░░░░░░  82%                                       │
│                                                                             │
│  REPORTS TO:                                                                │
│  CEO: Marketing ROI, Market penetration, Brand health                       │
│  CFO: Budget utilization, Cost attribution, Revenue impact                  │
│  COO: Campaign execution, Content pipeline, Regional ops                    │
│                                                                             │
│  [View Full Report →] [Schedule Review Meeting]                             │
└─────────────────────────────────────────────────────────────────────────────┘
```

### Cross-Department Comparison Component

```
┌─────────────────────────────────────────────────────────────────────────────┐
│  DEPARTMENT COMPARISON                          Period: [Q1 2026 ▼]         │
│  ─────────────────────────────────────────────────────────────────────────  │
│                                                                             │
│  Budget Utilization                                                         │
│  Marketing     ████████████████████░░░░░  82%    $450K remaining           │
│  Support       ██████████████████████░░░  92%    $210K remaining           │
│  Business      █████████████████░░░░░░░░  68%    $1.2M remaining           │
│  HR            ████████████████████░░░░░  85%    $315K remaining           │
│  Sales         ████████████████████░░░░░  88%    $480K remaining           │
│  SysAdmin      █████████████████░░░░░░░░  72%    $1.1M remaining           │
│  Finance       ████████████████████░░░░░  80%    $420K remaining           │
│  Foundation    ██████████████████████░░░  90%    $150K remaining           │
│                                                                             │
│  Headcount                                                                  │
│  Marketing     45 staff     ▲ 5 this quarter                               │
│  Support       123 staff    ▲ 8 this quarter                               │
│  Business      28 staff     ▬ Stable                                       │
│  HR            47 staff     ▲ 2 this quarter                               │
│  Sales         89 staff     ▲ 12 this quarter                              │
│  SysAdmin      34 staff     ▬ Stable                                       │
│  Finance       52 staff     ▼ 3 this quarter                               │
│  Foundation    18 staff     ▲ 4 this quarter                               │
│                                                                             │
│  [Export Comparison] [Customize Metrics]                                    │
└─────────────────────────────────────────────────────────────────────────────┘
```

### Department Health Status Grid

```
┌─────────────────────────────────────────────────────────────────────────────┐
│  DEPARTMENT HEALTH                                      [Refresh] [Alerts]  │
│  ─────────────────────────────────────────────────────────────────────────  │
│                                                                             │
│  ┌───────────────────┐  ┌───────────────────┐  ┌───────────────────┐      │
│  │ [Megaphone]       │  │ [Headphones]      │  │ [Globe]           │      │
│  │ Marketing    ● OK │  │ Support      ● OK │  │ Business     ● OK │      │
│  │                   │  │                   │  │                   │      │
│  │ 20 services       │  │ 15 services       │  │ 18 services       │      │
│  │ 3 alerts  0 crit  │  │ 2 alerts  0 crit  │  │ 1 alert   0 crit  │      │
│  │ 3 pending approv  │  │ 5 SLA pending     │  │ Data: healthy     │      │
│  │ [View →]          │  │ [View →]          │  │ [View →]          │      │
│  └───────────────────┘  └───────────────────┘  └───────────────────┘      │
│                                                                             │
│  ┌───────────────────┐  ┌───────────────────┐  ┌───────────────────┐      │
│  │ [Users]           │  │ [TrendingUp]      │  │ [Shield]          │      │
│  │ HR           ● OK │  │ Sales        ● OK │  │ SysAdmin    ⚠️ ATN │      │
│  │                   │  │                   │  │                   │      │
│  │ 15 services       │  │ 15 services       │  │ 17 services       │      │
│  │ 0 alerts  0 crit  │  │ 0 alerts  0 crit  │  │ 4 alerts  1 crit  │      │
│  │ 2 pending approv  │  │ 4 deals pending   │  │ 1 deploy pending  │      │
│  │ [View →]          │  │ [View →]          │  │ [View →]          │      │
│  └───────────────────┘  └───────────────────┘  └───────────────────┘      │
│                                                                             │
│  ┌───────────────────┐  ┌───────────────────┐                             │
│  │ [DollarSign]      │  │ [Activity]        │                             │
│  │ Finance      ● OK │  │ Foundation   ● OK │                             │
│  │                   │  │                   │                             │
│  │ 21 services       │  │ 3 services        │                             │
│  │ 2 alerts  0 crit  │  │ 1 alert   0 crit  │                             │
│  │ 5 invoices pend   │  │ 57 AI svc monitrd │                             │
│  │ [View →]          │  │ [View →]          │                             │
│  └───────────────────┘  └───────────────────┘                             │
│                                                                             │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

---

## 12. Business Unit Identity Components

### Business Unit Color System

```css
/* Business Unit Identity Colors (distinct from department colors) */
--color-bu-courier:       #0EA5E9;  /* Sky Blue - Courier Delivery */
--color-bu-ecommerce:     #8B5CF6;  /* Violet - E-Commerce Marketplace */
--color-bu-warehousing:   #F59E0B;  /* Amber - Warehousing & Storage */
--color-bu-airfreight:    #06B6D4;  /* Teal - Air Freight */
--color-bu-ocean:         #1E40AF;  /* Navy - Ocean Shipping */
--color-bu-haulage:       #DC2626;  /* Red - Haulage & Road Freight */
--color-bu-procurement:   #059669;  /* Emerald - Procurement */
--color-bu-admin:         #475569;  /* Slate - Admin & Partner Oversight */
```

### Business Unit Badge Component

```
┌─────────────────────────────────┐
│  [Icon]  Courier Services   ●   │
└─────────────────────────────────┘
```

**Specs:**
- Height: 28px
- Padding: 4px 12px
- Border radius: 14px
- Font size: 12px
- Font weight: 600
- Left border: 3px business unit color
- Background: business unit color at 10% opacity
- Icon: business unit-specific (see mapping below)

| Business Unit | Icon | Color | CSS Class | Services |
|---|---|---|---|---|
| Courier Services | `Truck` | `#0EA5E9` | `.bu-courier` | 20 |
| E-Commerce Platform | `ShoppingCart` | `#8B5CF6` | `.bu-ecommerce` | 76 |
| Warehousing & Storage | `Warehouse` | `#F59E0B` | `.bu-warehousing` | 43 |
| Air Freight | `Plane` | `#06B6D4` | `.bu-airfreight` | 15 |
| Ocean Shipping | `Ship` | `#1E40AF` | `.bu-ocean` | 18 |
| Haulage & Road Freight | `Route` | `#DC2626` | `.bu-haulage` | 62 |
| Procurement | `ClipboardList` | `#059669` | `.bu-procurement` | 27 |
| Admin & Oversight | `Building2` | `#475569` | `.bu-admin` | 11 |

### Business Unit Selector Dropdown

```
┌──────────────────────────────────────┐
│  Filter by Business Unit        [▼]  │
├──────────────────────────────────────┤
│  ● All Business Units                │
│  ─────────────────────────────────── │
│  [Truck]         Courier Services    │
│  [ShoppingCart]  E-Commerce Platform │
│  [Warehouse]     Warehousing & Store │
│  [Plane]         Air Freight         │
│  [Ship]          Ocean Shipping      │
│  [Route]         Haulage & Road      │
│  [ClipboardList] Procurement         │
│  [Building2]     Admin & Oversight   │
└──────────────────────────────────────┘
```

**Specs:**
- Dropdown width: 300px
- Item height: 40px
- Multi-select: No (single unit filter)
- Search: Yes (type to filter)
- Active state: Business unit color border + checkmark
- Revenue indicator: Show mini revenue next to each unit

---

## 13. Business Unit Revenue Components

### Business Unit Revenue KPI Grid

```
┌─────────────────────────────────────────────────────────────────────────────────┐
│  REVENUE BY BUSINESS UNIT                              [Filter ▼] [Period▼]  │
│  ┌──────────────┐ ┌──────────────┐ ┌──────────────┐ ┌──────────────┐          │
│  │ [Truck]       │ │[ShoppingCart]│ │ [Warehouse]  │ │ [Plane]      │          │
│  │ Courier      │ │ E-Commerce   │ │ Warehousing  │ │ Air Freight  │          │
│  │              │ │              │ │              │ │              │          │
│  │ Rev: $8.2M   │ │ GMV: $24.5M  │ │ Rev: $5.1M   │ │ Rev: $3.8M   │          │
│  │ ▲ 12%  ● OK │ │ ▲ 22%  ● OK │ │ ▲ 8%   ● OK │ │ ▲ 5%   ● OK │          │
│  │ 20 services  │ │ 76 services  │ │ 43 services  │ │ 15 services  │          │
│  └──────────────┘ └──────────────┘ └──────────────┘ └──────────────┘          │
│  ┌──────────────┐ ┌──────────────┐ ┌──────────────┐ ┌──────────────┐          │
│  │ [Ship]        │ │ [Route]      │ │[ClipboardList]│ │ [Building2]  │          │
│  │ Ocean        │ │ Haulage      │ │ Procurement  │ │ Admin Core   │          │
│  │              │ │              │ │              │ │              │          │
│  │ Rev: $4.2M   │ │ Rev: $12.8M  │ │ Spend: $18M  │ │ Partners:245 │          │
│  │ ▲ 3%   ● OK │ │ ▲ 18%  ● OK │ │ ▼ 5%   ● OK │ │ ▲ 10   ● OK │          │
│  │ 18 services  │ │ 62 services  │ │ 27 services  │ │ 11 services  │          │
│  └──────────────┘ └──────────────┘ └──────────────┘ └──────────────┘          │
│                                                                                  │
│  TOTAL: 272 services | Combined Revenue: $58.6M | ▲ 12.3% YoY                  │
└─────────────────────────────────────────────────────────────────────────────────┘
```

**Specs:**
- Grid: 4 columns on desktop, 2 on tablet, 1 on mobile
- Card min width: 200px
- Card padding: 16px
- Border left: 4px business unit color
- Hover: lift 4px + shadow increase
- Click: drills into business unit revenue detail
- Revenue format: automatic currency with abbreviation

### Business Unit Revenue Comparison Chart Component

```
┌─────────────────────────────────────────────────────────────────────────────┐
│  REVENUE COMPARISON BY BUSINESS UNIT          Period: [Q1 2026 ▼]            │
│  ┌──────────────────────────────────────────────────────────────────────┐   │
│  │                                                                      │   │
│  │  15M█│                                                               │   │
│  │     ││ [Haulage]  ████████████████████░░░░░  $12.8M                  │   │
│  │  10M█│ [E-Comm]   ██████████████████░░░░░░░  $9.8M (commission)      │   │
│  │     ││ [Courier]  ████████████████░░░░░░░░░░  $8.2M                  │   │
│  │   5M█│ [Warehouse]████████████░░░░░░░░░░░░░░  $5.1M                  │   │
│  │     ││ [Ocean]    █████████░░░░░░░░░░░░░░░░░  $4.2M                  │   │
│  │     ││ [Air]      ████████░░░░░░░░░░░░░░░░░░  $3.8M                  │   │
│  │     ││ [Procure]  ████████░░░░░░░░░░░░░░░░░░  $3.2M (savings)        │   │
│  │     ││ [Admin]    ████░░░░░░░░░░░░░░░░░░░░░░  $1.5M                  │   │
│  │     └───────────────────────────────────────────────────────────────│   │
│  └──────────────────────────────────────────────────────────────────────┘   │
│  [View Trend →] [Export CSV] [Compare Periods]                               │
└─────────────────────────────────────────────────────────────────────────────┘
```

**Specs:**
- Chart type: Horizontal bar chart
- Color: Each bar uses corresponding business unit color
- Max units displayed: 8
- Hover: Show exact value, YoY change, service count
- Click: Navigate to business unit detail page
- Sorting: By revenue (default), alphabetical, or growth rate

### Business Unit Revenue Trend Sparklines

```
┌──────────────────────────────────────────────────────────────────────────────┐
│  REVENUE TRENDS — 30 Day                              [View All Charts →]  │
│                                                                               │
│  [Truck] Courier        $8.2M  ▲12%   ▁▂▃▄▅▆▇██▇▆▅▄▃▂▁▂▃▄▅▆▇██            │
│  [ShoppingCart] E-Comm  $9.8M  ▲22%   ▁▁▂▃▄▅▆▇████▇▆▅▄▃▂▁▂▃▄▅▆▇           │
│  [Warehouse] Storage    $5.1M  ▲ 8%   ▅▅▆▆▇▇██▇▇▆▆▅▅▆▆▇▇████             │
│  [Plane] Air Freight    $3.8M  ▲ 5%   ▄▄▄▅▅▆▆▇▇██▇▆▅▄▃▃▄▅▆▇██            │
│  [Ship] Ocean           $4.2M  ▲ 3%   ▅▅▅▅▆▆▆▇▇████▇▇▆▆▅▅▅▅              │
│  [Route] Haulage       $12.8M  ▲18%   ▁▂▃▃▄▅▆▇████▇▆▅▄▃▂▂▃▄▅▆▇           │
│  [ClipboardList] Proc   $3.2M  ▼ 5%   ██▇▆▅▄▃▂▁▁▂▃▄▅▆▇██▇▆▅▄             │
│  [Building2] Admin      $1.5M  ▲10%   ▃▃▄▄▅▅▆▆▇▇████▇▇▆▆▅▅▄             │
│                                                                               │
└──────────────────────────────────────────────────────────────────────────────┘
```

**Specs:**
- Sparkline width: 200px
- Sparkline height: 24px
- Color: Business unit color
- Hover: Show data point values
- Period selector: 7d, 30d, 90d, 1y

---

## 14. Business Unit Service Health Components

### Business Unit Service Health Status Grid

```
┌─────────────────────────────────────────────────────────────────────────────┐
│  SERVICE HEALTH — ALL BUSINESS UNITS                        [Refresh] [↺]  │
│  ─────────────────────────────────────────────────────────────────────────  │
│                                                                              │
│  [Truck] Courier Services (20)                                          ●   │
│  ● ● ● ● ● ● ● ● ● ● ● ● ● ● ● ● ● ● ● ●                               │
│  20/20 operational | 0 degraded                                             │
│                                                                              │
│  [ShoppingCart] E-Commerce (76)                                         ●   │
│  ● ● ● ● ● ● ● ● ● ● ● ● ● ● ● ● ● ● ● ● ● ● ● ● ● ● ● ● ● ●         │
│  ● ● ● ● ● ● ● ● ● ● ● ● ● ● ● ● ● ● ● ● ● ● ● ● ● ● ● ● ● ●         │
│  ● ● ● ● ● ● ● ● ● ● ● ● ● ● ● 🔴                                       │
│  75/76 operational | 1 degraded (search-service)                            │
│                                                                              │
│  [Warehouse] Warehousing (43)                                           ●   │
│  ● ● ● ● ● ● ● ● ● ● ● ● ● ● ● ● ● ● ● ● ● ● ● ● ● ● ● ● ●           │
│  ● ● ● ● ● ● ● ● ● ● ● ● ●                                             │
│  43/43 operational | 0 degraded                                             │
│                                                                              │
│  [Plane] Air Freight (15)                                               ●   │
│  ● ● ● ● ● ● ● ● ● ● ● ● ● ● ●                                          │
│  15/15 operational | 0 degraded                                             │
│                                                                              │
│  [Ship] Ocean Shipping (18)                                             ●   │
│  ● ● ● ● ● ● ● ● ● ● ● ● ● ● ● ● ● ●                                    │
│  18/18 operational | 0 degraded                                             │
│                                                                              │
│  [Route] Haulage (62)                                                   ●   │
│  ● ● ● ● ● ● ● ● ● ● ● ● ● ● ● ● ● ● ● ● ● ● ● ● ● ● ● ● ●           │
│  ● ● ● ● ● ● ● ● ● ● ● ● ● ● ● ● ● ● ● ● ● ● ● ● ● ● ● ● ●           │
│  62/62 operational | 0 degraded                                             │
│                                                                              │
│  [ClipboardList] Procurement (27)                                       ●   │
│  ● ● ● ● ● ● ● ● ● ● ● ● ● ● ● ● ● ● ● ● ● ● ● ● ● ●                  │
│  27/27 operational | 0 degraded                                             │
│                                                                              │
│  [Building2] Admin Core (11)                                            ●   │
│  ● ● ● ● ● ● ● ● ● ● ●                                                   │
│  11/11 operational | 0 degraded                                             │
│                                                                              │
│  TOTAL: 272/273 services operational | 99.6% | 1 degraded                   │
│  Click any unit header → drill into service detail page                     │
└─────────────────────────────────────────────────────────────────────────────┘
```

**Specs:**
- Each dot represents 1 microservice
- Dot color: Green (#10B981) = healthy, Red (#EF4444) = degraded, Gray (#6B7280) = offline
- Dot size: 8px × 8px
- Dot spacing: 4px gap
- Row width: full container width, wrapping
- Unit header: icon + name + count + overall status badge
- Click header: navigates to unit's service detail page
- Click individual degraded dot: opens service incident detail modal
- Tooltip on hover: service name, port, status, response time

### Business Unit Infrastructure Summary Card

```
┌─────────────────────────────────────────────────────────────────────────────┐
│  [Truck] COURIER SERVICES — Infrastructure Summary                     [→]  │
│  ─────────────────────────────────────────────────────────────────────────  │
│                                                                              │
│  Services: 20/20 ●   |   Uptime: 99.97%   |   P95 Latency: 142ms          │
│  ████████████████████████████████████████████████████████████░░  99.97%     │
│                                                                              │
│  Top Services by Load:                                                       │
│  dispatch-core (8101)    ████████████████░░░░  78% CPU    ● Healthy         │
│  gps-tracking (8109)     ██████████████░░░░░░  65% CPU    ● Healthy         │
│  routing (8104)          ██████████████████░░  82% CPU    ● Healthy         │
│  commission (8115)       ████████████░░░░░░░░  52% CPU    ● Healthy         │
│  dynamic-pricing (8114)  ██████████████░░░░░░  61% CPU    ● Healthy         │
│                                                                              │
│  Kafka Throughput: 4,200 events/min | Redis Hit Rate: 98.5%                │
│  MongoDB Ops: 12,400/sec | Active Connections: 847                          │
│                                                                              │
│  [View All 20 Services →] [View Logs] [View Metrics Dashboard →]           │
└─────────────────────────────────────────────────────────────────────────────┘
```

**Specs:**
- Card padding: 20px
- Border left: 4px business unit color
- Uptime bar: full width, colored green (>99.9%), amber (99-99.9%), red (<99%)
- Service rows: max 5 displayed, expandable
- Click service name: opens service detail in CTO dashboard
- Click [→] header button: navigates to full unit infrastructure page

---

**Document End: UI Design System v4.0**
