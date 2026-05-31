# @gogidix/shared-ui

Shared UI component library for Gogidix Management Domain applications.

## Installation

This is a private library meant to be used within the Gogidix ecosystem. It should be installed as a local dependency:

```bash
# In your department's frontend project
pnpm add ../_Foundation/shared-ui-library
# or
npm install ../_Foundation/shared-ui-library
```

## Features

- **UI Components**: A comprehensive set of accessible, customizable components built on Radix UI
- **Layout Components**: Pre-built layout components for dashboards and portals
- **Chart Components**: Reusable chart components powered by Recharts
- **Data Components**: Tables, filters, pagination for data display
- **State Management**: Zustand stores for auth, UI state, and notifications
- **Utilities**: Common utility functions and helpers
- **TypeScript**: Full TypeScript support
- **Tailwind CSS**: Custom design system with executive theme

## Usage

### Setup

```tsx
// main.tsx
import { QueryClientProvider } from '@tanstack/react-query'
import { queryClient } from '@gogidix/shared-ui'
import App from './App'

ReactDOM.createRoot(document.getElementById('root')!).render(
  <QueryClientProvider client={queryClient}>
    <App />
  </QueryClientProvider>
)
```

### Components

```tsx
import { Button, Card, MetricCard, LineChart } from '@gogidix/shared-ui'

function MyComponent() {
  return (
    <div>
      <Button variant="executive">Click me</Button>
      <MetricCard
        title="Revenue"
        value={4200000}
        unit="currency"
        change={12.4}
        trend="up"
      />
    </div>
  )
}
```

### Authentication

```tsx
import { useAuth } from '@gogidix/shared-ui'

function ProtectedComponent() {
  const { user, isAuthenticated, hasPermission } = useAuth()

  if (!isAuthenticated) {
    return <Login />
  }

  if (!hasPermission('view:reports')) {
    return <AccessDenied />
  }

  return <Dashboard />
}
```

### Layouts

```tsx
import { DashboardLayout } from '@gogidix/shared-ui'

function App() {
  return (
    <DashboardLayout department="executive" user={user}>
      <YourPages />
    </DashboardLayout>
  )
}
```

## Mock Authentication

The library includes mock authentication for development. Use these credentials:

### Executive Domain
- CEO: `ceo@gogidix.com` / `password123`
- CFO: `cfo@gogidix.com` / `password123`
- COO: `coo@gogidix.com` / `password123`
- CTO: `cto@gogidix.com` / `password123`

### Other Departments
- Finance Manager: `finance-manager@gogidix.com` / `password123`
- HR Manager: `hr-manager@gogidix.com` / `password123`
- Sales Director: `sales-director@gogidix.com` / `password123`
- Support Lead: `support-lead@gogidix.com` / `password123`
- Admin: `admin@gogidix.com` / `password123`

## Design System

### Colors

- **Executive Blue**: `#0D47A1`
- **Executive Gold**: `#FFA000`
- **Success**: Green variants
- **Warning**: Yellow/Orange variants
- **Error**: Red variants

### Components

- Button (with variants: default, executive, gold, destructive, outline, ghost, link)
- Input, Textarea, Select
- Dialog, Dropdown Menu
- Table with sorting and filtering
- Tabs, Toast, Progress
- Avatar, Badge, Card

### Charts

- LineChart with area support
- BarChart (horizontal and vertical)
- PieChart with donut support
- MetricCard with trend indicators
- KPICard with progress tracking

## Development

```bash
# Install dependencies
pnpm install

# Run development mode
pnpm dev

# Run tests
pnpm test

# Build the library
pnpm build
```

## License

Internal use only for Gogidix Management Domain.
