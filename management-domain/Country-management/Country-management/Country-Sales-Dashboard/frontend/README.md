# Country Sales Dashboard - Frontend

Next.js 14 React application for the Country Sales Dashboard.

## Quick Start

```bash
# Install dependencies
npm install

# Development server
npm run dev

# Build for production
npm run build

# Start production server
npm start

# Run tests
npm test

# Run tests with coverage
npm run test:coverage
```

## Project Structure

```
src/
├── app/                 # Next.js app router pages
│   ├── dashboard/       # Dashboard pages
│   ├── layout.tsx       # Root layout
│   └── page.tsx         # Home page
├── components/          # React components
│   ├── charts/          # Chart components (Recharts)
│   ├── dashboard/       # Dashboard-specific components
│   ├── layouts/         # Layout components
│   ├── tables/          # Table components
│   └── ui/              # UI primitives
├── services/            # API service layer
│   └── api.ts           # API client
├── lib/                 # Utilities
│   └── utils.ts         # Helper functions
└── types/               # TypeScript types
    └── index.ts         # Type definitions
```

## Features

### Dashboard
- Real-time KPI cards
- Pipeline by stage chart
- Revenue trend chart
- Target progress visualization
- Overdue deals alerts

### Pipeline Management
- Visual pipeline representation
- Drag-and-drop stage transitions
- Deal value and probability tracking
- Filter by stage, owner, customer

### Customer Management
- Customer list with search and filters
- Customer tier management
- Follow-up tracking
- Customer lifetime value tracking

### Deal Management
- Deal creation and editing
- Stage advancement
- Value and probability updates
- Customer association

## Components

### MetricCard
Display KPIs with trend indicators.

```tsx
<MetricCard
  title="Total Revenue"
  value={formatCurrency(100000)}
  trend="+12.5%"
  trendUp={true}
  icon="dollar-sign"
/>
```

### PipelineChart
Bar chart showing deals by stage.

```tsx
<PipelineChart />
```

### RevenueChart
Line chart showing revenue trend.

```tsx
<RevenueChart />
```

### DealsTable
Sortable table of deals.

```tsx
<DealsTable deals={deals} />
```

## API Service

```typescript
import { salesApi } from '@/services/api'

// Get dashboard summary
const summary = await salesApi.getDashboardSummary('US')

// Get sales metrics
const metrics = await salesApi.getSalesMetrics('US', '2024-01-15')

// Get deals
const deals = await salesApi.getDeals({ page: 0, size: 20 })

// Create deal
const newDeal = await salesApi.createDeal({
  title: 'New Deal',
  value: 100000,
  customerId: 'customer1'
})
```

## State Management

Uses React Query for server state:

```tsx
const { data, isLoading, error } = useQuery({
  queryKey: ['dashboard-summary', 'US'],
  queryFn: () => salesApi.getDashboardSummary('US'),
  refetchInterval: 60000, // Refresh every minute
})
```

## Styling

Built with Tailwind CSS and follows a design system with CSS variables for theming:

```css
--primary: 221.2 83.2% 53.3%;
--secondary: 210 40% 96.1%;
--muted: 210 40% 96.1%;
--accent: 210 40% 96.1%;
```

## Environment Variables

```env
NEXT_PUBLIC_API_URL=http://localhost:8080/api/sales/v1
NEXT_PUBLIC_COUNTRY_CODE=US
```

## Testing

```bash
# Run tests
npm test

# Watch mode
npm run test:watch

# Coverage
npm run test:coverage
```

### Example Test

```tsx
import { render, screen } from '@testing-library/react'
import DashboardPage from '@/app/dashboard/page'

describe('DashboardPage', () => {
  it('renders dashboard metrics', async () => {
    render(<DashboardPage />)
    expect(await screen.findByText('Sales Dashboard')).toBeInTheDocument()
  })
})
```

## Deployment

### Docker

```bash
docker build -t country-sales-frontend .
docker run -p 3000:3000 country-sales-frontend
```

### Vercel

Push to your Git repository and connect to Vercel for automatic deployments.

### Standalone

```bash
npm run build
npm start
```

## Browser Support

- Chrome (last 2 versions)
- Firefox (last 2 versions)
- Safari (last 2 versions)
- Edge (last 2 versions)
