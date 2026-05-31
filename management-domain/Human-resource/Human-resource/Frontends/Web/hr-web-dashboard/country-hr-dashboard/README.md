# Country HR Dashboard

Gogidix HR Web Dashboard - Country HR Management Frontend Application

## Overview

This is the frontend dashboard for the Gogidix HR Management System, specifically designed for country-level HR operations. It provides a comprehensive interface for managing HR operations across multiple countries.

## Features

- **Authentication & Authorization**
  - Login with email/password
  - SSO integration (Microsoft, Google)
  - Multi-tenant support
  - Role-based access control

- **Dashboard Overview**
  - HR health score visualization
  - Workforce metrics by country
  - Global recruitment pipeline
  - Active alerts and notifications

- **Country Management**
  - View and compare HR metrics across countries
  - Detailed country-specific dashboards
  - Employee metrics and compliance status

- **Recruitment**
  - Global job postings management
  - Pipeline visualization
  - Recruitment analytics
  - Time-to-fill tracking

- **Payroll Management**
  - Global payroll overview
  - Country-specific payroll breakdown
  - Salary benchmarking

- **Performance & Development**
  - Performance review tracking
  - Training program management
  - Leadership development tracking

- **Analytics & Insights**
  - Workforce planning
  - Retention analysis
  - Diversity & inclusion metrics
  - AI-powered insights

- **Reports**
  - Report library
  - Custom report generation
  - Scheduled reports

- **Settings**
  - Profile management
  - Notification preferences
  - Security settings
  - Audit logs

## Tech Stack

- **Framework**: Next.js 14 (Pages Router)
- **Language**: TypeScript
- **Styling**: Tailwind CSS
- **State Management**: React Query (@tanstack/react-query)
- **Form Handling**: React Hook Form
- **HTTP Client**: Axios
- **Theming**: next-themes
- **Notifications**: react-hot-toast
- **Icons**: Heroicons, Lucide React

## Project Structure

```
country-hr-dashboard/
├── src/
│   ├── api/              # API client and TypeScript interfaces
│   │   └── hrApi.ts
│   ├── components/       # React components
│   │   ├── common/       # Shared components
│   │   ├── dashboard/    # Dashboard-specific components
│   │   └── layout/       # Layout components
│   ├── hooks/            # Custom React hooks
│   │   └── useAuth.ts
│   ├── lib/              # Utility functions
│   │   └── utils.ts
│   ├── pages/            # Next.js pages
│   │   ├── _app.tsx      # Root app wrapper
│   │   ├── _error.tsx    # Error page
│   │   ├── index.tsx     # Home/redirect page
│   │   ├── login.tsx     # Login page
│   │   ├── dashboard/    # Dashboard pages
│   │   ├── countries/    # Country pages
│   │   ├── recruitment/  # Recruitment pages
│   │   ├── payroll/      # Payroll pages
│   │   ├── performance/  # Performance pages
│   │   ├── analytics/    # Analytics pages
│   │   ├── reports/      # Reports pages
│   │   └── settings/     # Settings pages
│   ├── providers/        # Context providers
│   │   ├── AuthProvider.tsx
│   │   ├── DashboardProvider.tsx
│   │   └── ThemeProvider.tsx
│   └── styles/           # Global styles
│       └── globals.css
├── public/               # Static assets
├── .env.local.example    # Environment variables template
├── next.config.js        # Next.js configuration
├── tailwind.config.ts    # Tailwind CSS configuration
├── tsconfig.json         # TypeScript configuration
└── package.json          # Dependencies
```

## Getting Started

### Prerequisites

- Node.js 18+ installed
- npm or yarn package manager

### Installation

1. Install dependencies:
```bash
npm install
```

2. Create environment file:
```bash
cp .env.local.example .env.local
```

3. Configure environment variables in `.env.local`:
```env
NEXT_PUBLIC_HR_API_URL=http://localhost:8080
```

### Development

Run the development server:
```bash
npm run dev
```

Open [http://localhost:3000](http://localhost:3000) in your browser.

### Build

Create a production build:
```bash
npm run build
```

Start the production server:
```bash
npm start
```

## API Integration

The frontend communicates with the backend HR services via REST API. The API client is configured in `src/api/hrApi.ts`.

### API Endpoints

- `POST /api/v1/hq-hr/auth/login` - User authentication
- `GET /api/v1/hq-hr/dashboard/overview` - Dashboard summary
- `GET /api/v1/hq-hr/dashboard/countries` - Countries list
- `GET /api/v1/hq-hr/dashboard/countries/:code` - Country details
- `GET /api/v1/hq-hr/recruitment/global-jobs` - Global jobs
- `GET /api/v1/hq-hr/recruitment/global-pipeline` - Recruitment pipeline
- `GET /api/v1/hq-hr/payroll/global-summary` - Global payroll
- `GET /api/v1/hq-hr/performance/global-status` - Performance metrics
- `GET /api/v1/hq-hr/analytics/insights` - Analytics insights
- `GET /api/v1/hq-hr/reports/list` - Reports list
- `POST /api/v1/hq-hr/reports/generate` - Generate report
- `GET /api/v1/hq-hr/settings/profile` - User profile
- `PUT /api/v1/hq-hr/settings/profile` - Update profile

## Authentication

The application uses JWT-based authentication with multi-tenant support:
- Tokens are stored in localStorage
- Each API request includes:
  - `Authorization: Bearer <token>` header
  - `X-Tenant-ID: <tenant>` header

## Role-Based Access Control

The dashboard supports different access levels:
- **CHRO** - Full access to all features
- **HR VP** - Regional and global oversight
- **HR Director** - Country-level management
- **HR Analyst** - Read-only analytics access

## Styling

The project uses Tailwind CSS for styling with custom configurations in `tailwind.config.ts`. Global styles are defined in `src/styles/globals.css`.

## Browser Support

- Chrome (latest)
- Firefox (latest)
- Safari (latest)
- Edge (latest)

## License

Copyright © 2024 Gogidix. All rights reserved.
