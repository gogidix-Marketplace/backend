# HQ MARKETING DASHBOARD - WIREFRAMES

**Version:** 1.0
**Domain:** Management Domain
**Subdomain:** Digital-marketing
**Last Updated:** 2025-02-08

---

## TABLE OF CONTENTS

1. [Design System](#design-system)
2. [Page Wireframes](#page-wireframes)
3. [Components](#components)
4. [Responsive Design](#responsive-design)

---

## DESIGN SYSTEM

### Color Palette

| Color | Hex | Usage |
|-------|-----|-------|
| Primary Blue | #1976D2 | Primary actions, navigation |
| Secondary Purple | #7B1FA2 | Campaign highlights |
| Success Green | #43A047 | Positive metrics, ROI |
| Warning Orange | #FB8C00 | Alerts, warnings |
| Error Red | #E53935 | Negative metrics, budget exceeded |
| Neutral Gray | #616161 | Text, borders |
| Background | #FAFAFA | Page background |

### Typography

| Element | Font | Size | Weight |
|---------|------|------|--------|
| Page Title | Roboto | 32px | 700 |
| Section Title | Roboto | 24px | 600 |
| Card Title | Roboto | 18px | 500 |
| Body Text | Roboto | 14px | 400 |
| Caption | Roboto | 12px | 400 |

### Spacing

| Scale | Value | Usage |
|-------|-------|-------|
| XS | 4px | Small gaps |
| SM | 8px | Card padding |
| MD | 16px | Section spacing |
| LG | 24px | Component spacing |
| XL | 32px | Page margins |

---

## PAGE WIREFRAMES

### Global Overview

```
+--------------------------------------------------------------------------+
|  ☰  HQ Marketing Dashboard          Global Overview    📅 Feb 2025    👤   |
+--------------------------------------------------------------------------+
|                                                                          |
|  +----------+  +----------+  +----------+  +----------+  +----------+    |
|  | Active   |  | Total    |  | Budget   |  | Avg      |  | ROI      |    |
|  | Campaigns|  | Spend    |  | Used     |  | CTR      |  |          |    |
|  +----------+  +----------+  +----------+  +----------+  +----------+    |
|  |    67    |  | $7.84M   |  |  62.7%   |  |  4.0%    |  |  3.2x   |    |
|  |   +12%   |  |  +8%     |  |   +5%    |  |  +0.3%   |  |  +0.4x  |    |
|  +----------+  +----------+  +----------+  +----------+  +----------+    |
|                                                                          |
|  +------------------------------------------------------------------+   |
|  |                       Campaign Performance by Country              |   |
|  +------------------------------------------------------------------+   |
|  |  🇳🇬 Nigeria  │ $2.1M │ 3.5x │ 🇰🇪 Kenya │ $1.45M │ 3.1x │            |   |
|  |  🇿🇦 S.Africa │ $1.35M│ 2.8x │ 🇬🇭 Ghana │ $0.98M │ 2.9x │  View All  |   |
|  +------------------------------------------------------------------+   |
|                                                                          |
|  +---------------------------+  +-------------------------------------+   |
|  |    Channel Performance    |  |        Top Performing Ads          |   |
|  +---------------------------+  +-------------------------------------+   |
|  |  [Bar Chart]              |  |  1. Spring Sale - NG              |   |
|  |  Google: $3.2M (41%)      |  |  2. Brand Awareness - ZA          |   |
|  |  Meta:   $2.8M (36%)      |  |  3. Product Launch - KE           |   |
|  |  LinkedIn: $1.2M (15%)    |  |  4. Holiday Promo - GH            |   |
|  |  Other:   $0.6M (8%)      |  |                                    |   |
|  +---------------------------+  +-------------------------------------+   |
|                                                                          |
+--------------------------------------------------------------------------+
```

### Campaign Comparison

```
+--------------------------------------------------------------------------+
|  ☰  HQ Marketing Dashboard          Campaign Comparison    🔍 📊        |
+--------------------------------------------------------------------------+
|                                                                          |
|  Filters: [Country: All ▼] [Channel: All ▼] [Status: Active ▼]          |
|                                                                          |
|  +------------------------------------------------------------------+   |
|  |                        Campaign Comparison Matrix                 |   |
|  +------------------------------------------------------------------+   |
|  |  Campaign        │ Country │ Budget  │ Spend   │ ROI    │ Status |   |
|  +-----------------+---------+---------+---------+--------+--------+   |
|  |  Q1 Global Brand │ Multi   │ $2.0M   │ $1.2M   │ 3.2x   │ Active |   |
|  |  Social Q1      │ NG, KE  │ $0.5M   │ $0.32M  │ 4.1x   │ Active |   |
|  |  Email Feb      │ DE, FR  │ $0.15M  │ $0.145M │ 5.2x   │ Done   |   |
|  |  Spring Launch  │ US, JP  │ $0.8M   │ $0      │ -      │ Sched  |   |
|  +------------------------------------------------------------------+   |
|                                                                          |
|  +----------------------------------------------------+  +------------+   |
|  |              Campaign Performance Trends          |  |   ROI by    |   |
|  |  [Multi-line Chart - Spend vs Revenue by Month]   |  |  Country   |   |
|  +----------------------------------------------------+  | [Pie Chart]|   |
|                                                         |            |   |
|                                                         +------------+   |
|                                                                          |
+--------------------------------------------------------------------------+
```

### Country Analysis

```
+--------------------------------------------------------------------------+
|  ☰  HQ Marketing Dashboard          Country Analysis       🇳🇬 Country   |
+--------------------------------------------------------------------------+
|                                                                          |
|  Select Country: [Nigeria ▼]                                             |
|                                                                          |
|  +----------+  +----------+  +----------+  +----------+                   |
|  | Active   |  | Total    |  | Budget   |  | ROI      |                   |
|  | Campaigns|  | Spend    |  | Remaining│  |          |                   |
|  +----------+  +----------+  +----------+  +----------+                   |
|  |    12    |  | $2.1M    |  | $0.4M    |  |  3.5x   |                   |
|  +----------+  +----------+  +----------+  +----------+                   |
|                                                                          |
|  +-----------------------------------------------------------+           |
|  |                    Audience Demographics                  |           |
|  +-----------------------------------------------------------+           |
|  |  Age: 25-34 (35%), 35-44 (30%), 18-24 (20%)               |           |
|  |  Gender: Male (52%), Female (48%)                         |           |
|  |  Interests: Tech, Finance, Business                       |           |
|  +-----------------------------------------------------------+           |
|                                                                          |
|  +-----------------------+  +---------------------------------------+    |
|  |   Campaign List      |  |        Performance Timeline        |    |
|  +-----------------------+  +---------------------------------------+    |
|  |  Q1 Brand ($500K)    |  |  [Line chart - Daily performance]    |    |
|  |  Social ($320K)      |  |                                       |    |
|  |  Search ($450K)      |  |                                       |    |
|  |  Display ($280K)     |  |                                       |    |
|  |  Email ($180K)       |  |                                       |    |
|  +-----------------------+  +---------------------------------------+    |
|                                                                          |
+--------------------------------------------------------------------------+
```

### Audience Segmentation

```
+--------------------------------------------------------------------------+
|  ☰  HQ Marketing Dashboard          Audience Segmentation    👥          |
+--------------------------------------------------------------------------+
|                                                                          |
|  +-----------------------------------------------------------+           |
|  |                      Segment Overview                     |           |
|  +-----------------------------------------------------------+           |
|  |  Total Segments: 24  │ Total Reach: 8.5M  │ Avg Engagement: 4.2% |   |
|  +-----------------------------------------------------------+           |
|                                                                          |
|  +------------------------------------------------------------------+   |
|  |                         Segments List                          |   |
|  +------------------------------------------------------------------+   |
|  |  Segment           │ Size   │ Engagement │ CTR    │ Created    |    |
|  +-------------------+--------+------------+--------+------------+    |
|  |  Young Prospects  │ 1.2M   │ 5.2%       │ 4.8%   │ 2025-01-15 |    |
|  |  Business Decision│ 890K   │ 3.8%       │ 3.2%   │ 2025-01-10 |    |
|  |  Premium Buyers   │ 456K   │ 6.1%       │ 5.5%   │ 2024-12-20 |    |
|  |  Re-engagement    │ 2.1M   │ 2.4%       │ 2.1%   │ 2024-12-01 |    |
|  +------------------------------------------------------------------+   |
|                                                                          |
|  +---------------------------+  +-------------------------------------+   |
|  |    Segment Demographics    |  |         Lookalike Audiences       |   |
|  |  [Radar Chart]             |  |  1. Similar to Premium Buyers   |   |
|  |                           |  |  2. Similar to Converted Users  |   |
|  +---------------------------+  +-------------------------------------+   |
|                                                                          |
+--------------------------------------------------------------------------+
```

---

## COMPONENTS

### MetricCard

```
+------------------+
│  📊 Total Spend   │
│  ────────────────│
│      $7.84M      │
│      ▲ 8%        │
│  vs last month   │
+------------------+
```

### CampaignStatusBadge

| Status | Style |
|--------|-------|
| Active | Green pill with dot |
| Scheduled | Blue pill |
| Paused | Orange pill |
| Completed | Gray pill |
| Cancelled | Red pill |

### ChannelIcon

| Channel | Icon | Color |
|---------|------|-------|
| Google | 🔍 Google | Blue |
| Meta | 👥 Meta | Purple |
| LinkedIn | 💼 LinkedIn | Navy |
| Email | ✉️ Email | Orange |
| Display | 🖼️ Display | Pink |

### FilterPanel

```
+--------------------------------------+
|  🔍 Search Campaigns...              |
|                                      |
|  Country:                             |
|  ☑ Nigeria  ☑ Kenya  ☑ Ghana        |
|  ☐ South Africa                      |
|                                      |
|  Status:                              |
|  ☑ Active  ☐ Scheduled               |
|  ☐ Completed                         |
|                                      |
|  Date Range:                          |
|  [Last 30 Days ▼]                    |
|                                      |
|           [Apply Filters] [Reset]    |
+--------------------------------------+
```

---

## RESPONSIVE DESIGN

### Breakpoints

| Device | Min Width | Layout |
|--------|-----------|--------|
| Mobile | 320px | Single column, stacked cards |
| Tablet | 768px | 2-column dashboard, collapsible sidebar |
| Desktop | 1024px | 3-4 column dashboard, fixed sidebar |
| Large | 1440px+ | Full dashboard, expanded tables |

### Mobile Adaptations

**Navigation:** Bottom tab bar instead of sidebar
**Tables:** Horizontal scroll with card view toggle
**Charts:** Simplified, tap for details
**Filters:** Modal overlay instead of inline

### Tablet Adaptations

**Navigation:** Collapsible sidebar with hamburger
**Cards:** 2-column grid
**Tables:** Swipe actions for row operations
**Charts:** Medium detail, touch interactive

---

## VERSION HISTORY

| Version | Date | Changes |
|---------|------|---------|
| 1.0 | 2025-02-08 | Initial |

---

**Document End**
