# Global HR Dashboard

Gogidix Global HR Dashboard - HQ Level Multi-Country HR Management System

## Overview

The Global HR Dashboard is the headquarters-level management interface that aggregates data from all Country HR Dashboards. It provides strategic oversight and enables data-driven decisions across the entire global HR operations.

## Key Features

### Global Overview Dashboard
- **Worldwide HR Health Score** - Aggregated health score across all countries
- **Total Headcount** - Employee count across all operations
- **Total Countries** - Number of countries with active operations
- **Growth Rate** - Year-over-year workforce growth percentage
- **Regional Summary** - HR metrics by geographic region (Africa, Europe, Asia, Americas)

### Country Comparison
- **Multi-Country View** - Side-by-side comparison of all countries
- **Filtering & Sorting** - By region, headcount, health score, or growth
- **Detailed Metrics**:
  - Headcount and growth trends
  - Health score per country
  - Payroll in local currency and USD
  - Performance ratings
  - Training completion rates
  - Compliance status

### Global Talent Acquisition
- **Worldwide Job Postings** - All active positions across countries
- **Global Pipeline** - Recruitment funnel visualization (Applied → Screened → Interview → Offer → Hired)
- **By-Country Recruitment** - Applicant and hire metrics by country
- **Key Metrics**:
  - Total active jobs
  - Total applicants
  - Time to fill (average)
  - Cost per hire
  - Conversion rates

### Global Compensation & Benefits
- **Worldwide Payroll Summary** - Total payroll with currency breakdown
- **Payroll by Country** - Local currency amounts with USD conversion
- **Payroll Status Tracking** - Processed, processing, pending status
- **Currency Analysis** - Payroll variance by country
- **Monthly Trends** - Payroll evolution over time

### Global Performance & Development
- **Performance Review Status** - Completion rates across countries
- **Training Programs** - Global training completion metrics
- **Performance by Country** - Ratings and participation rates
- **Performance Distribution** - Rating breakdown (1-5 scale)

### Analytics & Insights
- **Workforce Trends** - 12-month headcount trends with hires/departures
- **Retention Analysis** - Country-by-country retention rates
- **Diversity & Inclusion** - Gender and regional diversity metrics
- **Leadership Diversity** - Diversity at different organizational levels
- **Predictive Analytics** - AI-powered insights and recommendations

### Reports
- **Report Library** - Pre-built reports for different categories
- **Scheduled Reports** - Automated report generation and delivery
- **Report Categories**:
  - Workforce
  - Recruitment
  - Payroll
  - Performance
  - Compliance
  - Executive summaries

### Settings & Administration
- **Profile Management** - User profile and preferences
- **Notifications** - Configurable alert preferences
- **Security** - Password management and session controls
- **User Administration** - Manage user access and permissions
- **Country Management** - Add/modify country configurations

## Data Flow Architecture

```
┌─────────────────────────────────────────────────────┐
│                 COUNTRY HR DASHBOARDS                  │
│  (Nigeria, Kenya, South Africa, Ireland, etc.)        │
│  ┌─────────────────────────────────────────────────┐  │
│  │  Country-Level Data:                               │  │
│  │  • Employee headcount                            │  │
│  │  • Local payroll amounts                           │  │
│  │  • Recruitment pipeline                            │  │
│  │  • Performance metrics                             │  │
│  │  • Compliance status                              │  │
│  └─────────────────────────────────────────────────┘  │
│                         │                                   │
│                         │ Reports To                         │
│                         ▼                                   │
└─────────────────────────────────────────────────────┘
                          │
                          ▼
┌─────────────────────────────────────────────────────┐
│              GLOBAL HR DASHBOARD (HQ)                  │
│  ┌─────────────────────────────────────────────────┐  │
│  │  Data Aggregation Layer:                         │  │
│  │  • Fetches from all country APIs                   │  │
│  │  • Currency conversion to USD                     │  │
│  │  • Calculates global metrics                       │  │
│  │  • Identifies trends and patterns                 │  │
│  └─────────────────────────────────────────────────┘  │
│  ┌─────────────────────────────────────────────────┐  │
│  │  Global Views:                                    │  │
│  │  • Worldwide health score                          │  │
│  │  • Cross-country comparisons                      │  │
│  │  • Regional rollups                                │  │
│  │  • Strategic insights                               │  │
│  └─────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────┘
                          │
                          ▼
                    Reports to Executive Leadership
```

## User Roles & Permissions

| Role | Description | Access Level |
|------|-------------|-------------|
| **CHRO** | Chief Human Resources Officer | Full global access, all features |
| **HR VP** | HR Vice President | Multi-region oversight, strategic analytics |
| **HR Director** | HR Director | Regional oversight, country comparisons |
| **HR Analyst** | HR Analyst | Read-only access to reports and analytics |

## API Integration

### Base URL
```
/api/v1/global-hr/
```

### Key Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/auth/login` | User authentication |
| GET | `/dashboard/overview` | Global dashboard summary |
| GET | `/countries/comparison` | Country comparison data |
| GET | `/regions/summary` | Regional summary |
| GET | `/recruitment/summary` | Global recruitment stats |
| GET | `/payroll/summary` | Global payroll summary |
| GET | `/performance/summary` | Global performance metrics |
| GET | `/insights` | AI-powered insights |
| GET | `/analytics/workforce-trends` | Workforce trends |
| GET | `/analytics/diversity` | Diversity metrics |
| GET | `/analytics/retention` | Retention analysis |
| GET | `/reports` | Available reports |
| POST | `/reports/:id/generate` | Generate report |

## Installation & Setup

```bash
# Install dependencies
npm install

# Set up environment variables
cp .env.local.example .env.local

# Run development server
npm run dev
```

## Build & Deploy

```bash
# Create production build
npm run build

# Start production server
npm start
```

## Technologies Used

- **Framework**: Next.js 14
- **Language**: TypeScript
- **Styling**: Tailwind CSS
- **State Management**: React Query (@tanstack/react-query)
- **Forms**: React Hook Form
- **HTTP Client**: Axios
- **Theming**: next-themes
- **Charts**: Recharts
- **Icons**: Heroicons, Lucide React

## License

Copyright © 2024 Gogidix. All rights reserved.
