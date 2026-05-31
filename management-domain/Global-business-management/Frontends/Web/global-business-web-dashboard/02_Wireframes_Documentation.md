# GLOBAL BUSINESS DASHBOARD - WIREFRAMES

**Version:** 1.0
**Last Updated:** 2026-02-23

---

## DESIGN SYSTEM

### Color Palette

| Color | Hex | Usage |
|-------|-----|-------|
| Primary Blue | #1565C0 | Primary actions, navigation |
| Growth Green | #2E7D32 | Positive growth, targets met |
| Warning Orange | #F57C00 | Caution, below target |
| Critical Red | #C62828 | Decline, critical issues |
| World Blue | #0277BD | Regional highlights |
| Partner Purple | #7B1FA2 | Partner-related items |
| Background | #FAFAFA | Page background |
| Surface White | #FFFFFF | Card, panel background |

### Typography

| Element | Font | Size | Weight |
|---------|------|------|--------|
| Page Title | Montserrat | 28px | 700 |
| Section Title | Montserrat | 20px | 600 |
| Card Title | Montserrat | 16px | 500 |
| Body Text | Open Sans | 14px | 400 |
| Data Labels | Roboto Mono | 12px | 500 |

---

## PAGES

### Overview Page

```
+--------------------------------------------------------------------------+
|  ☰  GLOBAL BUSINESS DASHBOARD      Overview    📅 Feb 2026    👤     |
+--------------------------------------------------------------------------+
|                                                                          |
|  ┌─────────────────────────────────────────────────────────────────────┐  │
|  │ GLOBAL BUSINESS HEALTH                      [📊 Full Report]      │  │
|  │  ┌───────────────────────┐ ┌─────────────────────────────────────┐ │  │
|  │  │         85            │ │    ▲ 5 points from last quarter       │  │
|  │  │        ●●●●○          │ │    3 regions above target              │  │
|  │  │   Business Health      │ │    2 regions require attention        │  │
|  │  └───────────────────────┘ └─────────────────────────────────────┘ │  │
  │  └─────────────────────────────────────────────────────────────────────┘  │
|                                                                          |
|  ┌────────┐ ┌────────┐ ┌────────┐ ┌────────┐ ┌────────┐ ┌────────┐  │
|  │Regions │ │Active  │ │Pipeline│ │Partners│ │Deals   │ │Target  │  │
|  │        │ │Deals   │ │Value   │ │        │ │Closed  │ │Achieve │  │
|  │   4    │ │   45   │ │$12.5M  │ │   128  │ │   23   │ │  82%   │  │
|  │ ●●●●●  │ │ ▲ +8   │ │ ▲ $2M  │ │ ▲ +5   │ │ ▲ +3   │ │ ●●●●○  │  │
|  └────────┘ └────────┘ └────────┘ └────────┘ └────────┘ └────────┘  │
|                                                                          |
|  +------------------------------------------------------------------+   │
|  |                       REGIONAL PERFORMANCE                          │   │
|  +------------------------------------------------------------------+   │
|  │  ┌─────────────┐ ┌─────────────┐ ┌─────────────┐ ┌─────────────┐│  │
|  │  │ 🇪🇺 Europe   │ │ 🌍 Africa   │ │ 🇺🇸 Americas │ │ 🌏 Asia     ││  │
|  │  │ 92% ●●●●●●  │ │ 78% ●●●●○○  │ │ 88% ●●●●●○  │ │ 85% ●●●●●○  ││  │
|  │  │ $8.2M       │ │ $3.5M       │ │ $4.1M       │ │ $2.8M       ││  │
|  │  │ +15%        │ │ +22%        │ │ +8%         │ │ +18%        ││  │
|  │  │ [View]      │ │ [View]      │ │ [View]      │ │ [View]      ││  │
|  │  └─────────────┘ └─────────────┘ └─────────────┘ └─────────────┘│  │
|  +------------------------------------------------------------------+   │
|                                                                          |
|  +---------------------------+  +-------------------------------------+   │
|  │   PIPELINE OVERVIEW        │  │        PARTNER HIGHLIGHTS           │   │
|  +---------------------------+  +-------------------------------------+   │
|  │  Prospecting: 12 deals      │  │ 🏆 Partner of the Month:            │   │
|  │  Qualifying: 18 deals       │  │    TechVentures Global             │   │
|  │  Proposal: 15 deals         │  │    $2.5M closed this quarter        │   │
|  │  Negotiating: 8 deals       │  │                                     │   │
|  │  Closing: 5 deals           │  │ 📈 Top Performer:                   │   │
|  │  [View Full Pipeline]       │  │    Sarah Johnson (Europe)           │   │
|  +---------------------------+  +-------------------------------------+   │
|                                                                          |
+--------------------------------------------------------------------------+
```

### Regional Detail Page

```
+--------------------------------------------------------------------------+
|  ☰  ← Back           EUROPE REGION                  [Export] [Settings]   |
+--------------------------------------------------------------------------+
|                                                                          |
|  ┌─────────────────────────────────────────────────────────────────────┐  │
|  │ 🇪🇺 EUROPE OVERVIEW                                                │  │
|  │  Revenue: $8.2M  |  Growth: +15%  |  Target Achievement: 92%       │  │
|  │  Countries: 7  |  Active Partners: 45  |  Team Size: 23              │  │
|  └─────────────────────────────────────────────────────────────────────┘  │
|                                                                          |
|  ┌─────────────────────────────────────────────────────────────┬───────┐│  │
|  │  COUNTRY PERFORMANCE                    NEW OPPORTUNITY    [+ Add]││  │
|  ├─────────────────────────────────────────────────────────────┼───────┤│  │
|  │  🇬🇧 UK          $3.2M    ●●●●● 92%     Enterprise Deal    ││  │
│  │  🇮🇪 Ireland     $2.1M    ●●●●● 88%     Tech Expansion     ││  │
│  │  🇩🇪 Germany     $1.5M    ●●●●○ 78%     Partnership Renew  ││  │
│  │  🇫🇷 France      $0.8M    ●●●○○ 65%     Market Entry       ││  │
│  │  🇳🇱 Netherlands $0.4M    ●●●●○ 85%     Channel Expansion   ││  │
│  │  🇧🇪 Belgium     $0.1M    ●●○○○ 42%     Lead Generation    ││  │
│  │  🇦🇹 Austria     $0.1M    ●●○○○ 38%     Brand Awareness    ││  │
│  └─────────────────────────────────────────────────────────────────────┘│  │
|                                                                          |
|  ┌─────────────────────────────────────────────────────────────┐   │  │
|  │                    DEAL PIPELINE                             │   │  │
|  │  ┌─────────────────────────────────────────────────────────┐ │   │  │
│  │  │ PROSPECTING (12)  │ QUALIFYING (18) │ PROPOSAL (15)     │ │   │  │
│  │  │ ████░░░░░░░░░░░░  │ ████████████░░░░ │ ████████░░░░░░░░ │ │   │  │
│  │  └─────────────────────────────────────────────────────────┘ │   │  │
│  │  Total Pipeline Value: $8.5M  |  Avg Deal Size: $185K          │   │  │
│  └─────────────────────────────────────────────────────────────────┘   │
|                                                                          |
+--------------------------------------------------------------------------+
```

### Partners Page

```
+--------------------------------------------------------------------------+
|  ☰  GLOBAL BUSINESS DASHBOARD      Partners        [+ New Partner]      |
+--------------------------------------------------------------------------+
|                                                                          |
|  Filters: [Region: All ▼] [Type: All ▼] [Status: Active ▼]             │
|                                                                          |
|  ┌─────────────────────────────────────────────────────────────────────┐  │
|  │ ACTIVE PARTNERS (128)                                              │  │
|  │  ┌───────────────────────────────────────────────────────────────┐ │  │
│  │  │ 🏢 TechVentures Global              Platinum    €2.5M YTD     │ │  │
│  │  │    Europe | Enterprise Solutions | Active since 2019        │ │  │
│  │  │    [View Details] [Contact] [Performance Report]              │ │  │
│  │  ├───────────────────────────────────────────────────────────────┤ │  │
│  │  │ 🏢 African Business Partners          Gold        $1.2M YTD     │ │  │
│  │  │    Africa | Distribution | Active since 2021                  │ │  │
│  │  │    [View Details] [Contact] [Performance Report]              │ │  │
│  │  ├───────────────────────────────────────────────────────────────┤ │  │
│  │  │ 🏢 Asia Pacific Connections            Silver      $800K YTD    │ │  │
│  │  │    Asia Pacific | Channel Partners | Active since 2022        │ │  │
│  │  │    [View Details] [Contact] [Performance Report]              │ │  │
│  │  └───────────────────────────────────────────────────────────────┘ │  │
│  │  [Load More Partners]                                               │  │
│  └─────────────────────────────────────────────────────────────────────┘  │
|                                                                          |
|  ┌─────────────────────────────────────────────────────────────────────┐  │
│  │ PARTNER PERFORMANCE SUMMARY                                         │  │
|  │  • Total Partners: 128 (Active: 112, Onboarding: 16)                 │  │
│  │  • Revenue through Partners: $8.5M (Q1 2026)                        │  │
|  │  • Top Performing Region: Europe (45 partners, €2.5M)               │  │
│  │  • Average Partner Score: 78/100                                    │  │
│  └─────────────────────────────────────────────────────────────────────┘  │
|                                                                          |
+--------------------------------------------------------------------------+
```

### Business Development Page

```
+--------------------------------------------------------------------------+
|  ☰  GLOBAL BUSINESS DASHBOARD      Business Dev         [+ New Deal]     |
+--------------------------------------------------------------------------+
|                                                                          |
|  ┌─────────────────────────────────────────────────────────────────────┐  │
│  │ PIPELINE OVERVIEW                                                  │  │
│  │  ┌────────┐ ┌────────┐ ┌────────┐ ┌────────┐ ┌────────┐          │  │
│  │  │Prospect│ │Qualify │ │Proposal│ │Negotiat│ │Closing │          │  │
│  │  │   12   │ │   18   │ │   15   │ │    8   │ │    5   │          │  │
│  │  │$1.2M   │ │$3.5M   │ │$4.2M   │ │$2.8M   │ │$0.8M   │          │  │
│  │  └────────┘ └────────┘ └────────┘ └────────┘ └────────┘          │  │
│  └─────────────────────────────────────────────────────────────────────┘  │
|                                                                          |
|  ┌─────────────────────────────────────────────────────────────────────┐  │
│  │ RECENT OPPORTUNITIES                                               │  │
│  │  ┌───────────────────────────────────────────────────────────────┐ │  │
│  │  │ 🏢 ENTERPRISE - TechCorp Europe                       NEW      │ │  │
│  │  │    Value: $500K | Region: Europe | Stage: Qualifying         │ │  │
│  │  │    Owner: Sarah Johnson | Probability: 60% | Due: Mar 2026   │ │  │
│  │  │    [View Details] [Update Stage] [Add Note]                   │ │  │
│  │  ├───────────────────────────────────────────────────────────────┤ │  │
│  │  │ 🏢 PARTNERSHIP - African Logistics Alliance        PROPOSAL  │ │  │
│  │  │    Value: $250K | Region: Africa | Stage: Proposal          │ │  │
│  │  │    Owner: Michael Chen | Probability: 75% | Due: Feb 2026   │ │  │
│  │  │    [View Details] [Update Stage] [Add Note]                   │ │  │
│  │  ├───────────────────────────────────────────────────────────────┤ │  │
│  │  │ 🏢 CHANNEL - APAC Distribution Network              CLOSING   │ │  │
│  │  │    Value: $180K | Region: Asia Pacific | Stage: Closing     │ │  │
│  │  │    Owner: Lisa Wang | Probability: 90% | Due: Today         │ │  │
│  │  │    [View Details] [Update Stage] [Add Note]                   │ │  │
│  │  └───────────────────────────────────────────────────────────────┘ │  │
│  │  [View All Opportunities]                                            │  │
│  └─────────────────────────────────────────────────────────────────────┘  │
|                                                                          |
+--------------------------------------------------------------------------+
```

---

## COMPONENTS

### RegionCard

```
+------------------+
│  🇪🇺 Europe       │
│  ───────────────  │
│  $8.2M revenue    │
│  +15% growth      │
│  92% on track     │
│  [View Details]   │
+------------------+
```

### PartnerCard

```
┌────────────────────────────────────────────────────────────┐
│ 🏢 TechVentures Global              Platinum    €2.5M YTD  │
│                                                         │
│ Europe | Enterprise Solutions | Active since 2019      │
│                                                         │
│ Performance: ●●●●● 92/100                               │
│ Deals Closed: 12 | Pipeline: 8                           │
│                                                         │
│ [View Details] [Contact] [Performance Report]            │
└────────────────────────────────────────────────────────────┘
```

### PipelineCard

```
┌────────────────────────────────────────────────────────────┐
│ 🏢 TechCorp Europe                           $500K    60%   │
│                                                         │
│ Stage: Qualifying | Region: Europe | Owner: S. Johnson   │
│                                                         │
│ Last Activity: 2 days ago                                │
│ Next Action: Schedule demo                               │
│                                                         │
│ [View Details] [Update] [Add Note]                        │
└────────────────────────────────────────────────────────────┘
```

---

## RESPONSIVE DESIGN

### Breakpoints

| Device | Min Width | Layout |
|--------|-----------|--------|
| Mobile | 320px | Single column, vertical cards |
| Tablet | 768px | 2-column grid, collapsible sidebar |
| Desktop | 1024px | 3-4 column dashboard, fixed sidebar |
| Large Desktop | 1440px+ | Full dashboard, expanded views |

### Mobile Adaptations

- **Navigation:** Bottom tab bar
- **Region Cards:** Horizontal scroll, swipe for actions
- **Pipeline:** Compact list view
- **Partners:** Card-based with tap for details

---

## VERSION HISTORY

| Version | Date | Changes |
|---------|------|---------|
| 1.0 | 2026-02-23 | Initial Wireframes Documentation |

---

**Document End**
