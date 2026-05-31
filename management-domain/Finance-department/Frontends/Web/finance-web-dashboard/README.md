# Finance Web Dashboard - Documentation

## Overview

The Finance Web Dashboard is a React 18-based single-page application serving as the primary interface for finance personnel including accountants, CFOs, and financial analysts.

## Technology Stack

| Component | Technology | Version |
|-----------|------------|---------|
| Framework | React | 18.3.1 |
| Language | TypeScript | 5.3.3 |
| Build Tool | Vite | 5.x |
| State Management | Zustand | 4.5.0 |
| Data Fetching | @tanstack/react-query | 5.28.0 |
| Routing | React Router DOM | 6.22.0 |
| Validation | Zod | 3.22.4 |
| Date Utilities | date-fns | 3.3.1 |
| Testing | Vitest, Playwright | Latest |

## Application Structure

```
finance-web-dashboard/
├── accountant-dashboard/    # Accountant view
├── cfo-dashboard/          # CFO view
└── reports-dashboard/      # Reports view
```

## Key Features

### 1. Dashboard Views

#### Accountant Dashboard
- Invoice overview and management
- Payment tracking
- Vendor management
- Reconciliation tools

#### CFO Dashboard
- Financial summaries and KPIs
- Cash flow forecasting
- Budget vs actual analysis
- Multi-entity consolidation

#### Reports Dashboard
- Generate financial reports
- Export to PDF/Excel
- Schedule automated reports

### 2. Components Architecture

```
src/
├── application/          # Application startup
├── components/           # Reusable UI components
├── domain/              # Domain models
├── features/            # Feature modules
├── infrastructure/       # External integrations
├── presentation/         # Page components
├── shared/              # Shared utilities
└── main.tsx             # Application entry
```

### 3. State Management

**Zustand Stores:**
- `useAuthStore` - Authentication state
- `useInvoiceStore` - Invoice management
- `useVendorStore` - Vendor management
- `usePaymentStore` - Payment tracking
- `useDashboardStore` - Dashboard data
- `useReportStore` - Report generation

### 4. API Integration

All services communicate via REST API:
- Base URL: `http://{api-gateway}/api`
- Authentication: JWT tokens via interceptors
- Error handling: Global error boundary
- Loading states: React Query automatic caching

### 5. Build & Deployment

```bash
# Development
npm install
npm run dev

# Production Build
npm run build

# Preview Build
npm run preview

# Run Tests
npm test
npm run test:ui
npm run test:e2e

# Linting
npm run lint
npm run format
```

## Docker Deployment

### Development Container
```bash
docker build -t finance-dashboard:dev .
docker run -p 8080:8080 finance-dashboard:dev
```

### Production Container
```bash
docker build -t finance-dashboard:prod .
docker run -p 80:80 finance-dashboard:prod
```

### Health Endpoint
- `GET /` - Returns 200 if healthy

---

## Testing Strategy

### Unit Tests (Vitest)
- Component testing with React Testing Library
- Store testing with Zustand mocks
- Service layer mocking
- 80%+ coverage target

### E2E Tests (Playwright)
- Critical user workflows
- Cross-browser compatibility
- Performance testing
- Accessibility testing

---

## User Workflows

### 1. Invoice Management
1. Navigate to Invoices section
2. View list of invoices
3. Filter by status, vendor, date range
4. View invoice details
5. Approve/reject invoices
6. Schedule payments

### 2. Dashboard Monitoring
1. View financial summaries
2. Analyze cash flow trends
3. Review budget utilization
4. Monitor KPIs

### 3. Report Generation
1. Select report type
2. Set parameters (date range, entities)
3. Generate preview
4. Export to PDF/Excel
5. Schedule automated reports
