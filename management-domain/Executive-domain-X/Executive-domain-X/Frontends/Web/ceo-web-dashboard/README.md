# CEO Web Dashboard | Gogidix Executive Suite

CEO Executive Dashboard for Gogidix Management Suite with Hexagonal Architecture.

## Features

- **Dashboard**: Executive overview with key metrics and KPIs
- **Analytics**: Business intelligence and data visualization
- **Approvals**: CEO approval workflow management
- **Strategy**: Strategic planning and tracking
- **Reports**: Executive reporting and summaries

## Architecture

This application follows **Hexagonal Architecture** (Ports and Adapters):

```
src/
├── domain/          # Core business logic (framework independent)
├── application/     # Application services and orchestration
├── infrastructure/  # External dependencies (API, storage, state)
├── presentation/    # UI components, pages, styles
├── shared/          # Cross-cutting concerns
└── features/        # Feature-based modules
```

## Tech Stack

- **Framework**: React 18 + TypeScript
- **Build Tool**: Vite
- **State Management**: Zustand
- **Data Fetching**: TanStack Query
- **Routing**: React Router v6
- **Charts**: Recharts
- **UI Components**: Radix UI

## Getting Started

\`\`\`bash
# Install dependencies
npm install

# Start development server
npm run dev

# Build for production
npm run build

# Preview production build
npm run preview
\`\`\`

## Development

- Port: 3000
- Open: http://localhost:3000

## License

Copyright © 2026 Gogidix
