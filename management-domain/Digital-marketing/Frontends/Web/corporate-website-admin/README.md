# Gogidix Corporate Website Admin

A comprehensive React-based admin dashboard for managing the Gogidix corporate website content, products, and analytics.

## Tech Stack

- **React 18** - UI library
- **Vite 5** - Build tool and dev server
- **TypeScript 5.3** - Type safety
- **Material UI 5.15** - Component library
- **Zustand 4.4** - State management
- **TanStack Query 5.17** - Data fetching and caching
- **React Hook Form 7.49** - Form handling
- **Zod 3.22** - Schema validation
- **TipTap 2.1** - Rich text editor
- **Recharts 2.10** - Analytics charts

## Features

### Content Management
- Pages management with templates
- Blog posts with categories and tags
- Press releases
- Resources (ebooks, whitepapers, case studies)

### Product Catalog
- Products with categories and features
- Pricing plans management
- Integrations management

### Developer Resources
- API documentation editor
- SDK management
- Code examples repository

### Careers
- Job postings management
- Application tracking
- Hiring pipeline view

### Partners
- Partner programs
- Partner applications
- Partner portal

### Leads Management
- Demo requests
- Sales inquiries
- Support tickets

### Analytics
- Site traffic overview
- User behavior analytics
- Conversion funnels
- SEO performance tracking

### Settings
- General site settings
- User management with role-based access
- Workflow automation
- Third-party integrations

## Getting Started

### Prerequisites

- Node.js 18+
- npm 9+

### Installation

1. Clone the repository:
```bash
git clone https://github.com/gogidix/corporate-website-admin.git
cd corporate-website-admin
```

2. Install dependencies:
```bash
npm install
```

3. Create environment file:
```bash
cp .env.example .env
```

4. Configure environment variables:
```env
VITE_API_URL=http://localhost:4000
VITE_API_TIMEOUT=30000
VITE_APP_NAME=Gogidix Corporate Admin
VITE_APP_URL=http://localhost:3001
```

### Development

Run the development server:
```bash
npm run dev
```

The app will be available at http://localhost:3001

### Build

Build for production:
```bash
npm run build
```

Preview the production build:
```bash
npm run preview
```

### Testing

Run unit tests:
```bash
npm test
```

Run tests with UI:
```bash
npm run test:ui
```

Run tests with coverage:
```bash
npm run test:coverage
```

### Type Checking

Check TypeScript types:
```bash
npm run type-check
```

### Linting

Run ESLint:
```bash
npm run lint
```

## Docker

Build and run with Docker:

```bash
docker build -t corporate-website-admin .
docker run -p 3000:80 corporate-website-admin
```

Or use Docker Compose:

```bash
docker-compose up -d
```

## Project Structure

```
src/
├── components/          # Reusable components
│   ├── auth/           # Authentication components
│   ├── common/         # Shared UI components
│   ├── dashboard/      # Dashboard-specific components
│   ├── editor/         # Rich text editor
│   └── media/          # Media library
├── contexts/           # React contexts
├── hooks/              # Custom hooks
├── layouts/            # Layout components
├── pages/              # Page components
│   ├── auth/           # Authentication pages
│   ├── analytics/      # Analytics pages
│   ├── careers/        # Career pages
│   ├── content/        # Content management pages
│   ├── developer/      # Developer resources pages
│   ├── leads/          # Lead management pages
│   ├── partners/       # Partner pages
│   ├── products/       # Product pages
│   └── settings/       # Settings pages
├── services/           # API services
├── stores/             # Zustand stores
├── types/              # TypeScript types
├── utils/              # Utility functions
├── App.tsx             # Root component
├── main.tsx            # Entry point
└── theme.ts            # Material UI theme
```

## Authentication

The application uses JWT-based authentication. Users must log in to access the admin dashboard.

Default credentials for development:
- Email: admin@gogidix.com
- Password: (set during initial setup)

## Role-Based Access Control

The application supports the following roles:

- **Admin** - Full access to all features
- **Editor** - Can create and edit content
- **Author** - Can write blog posts
- **Contributor** - Can submit content for review
- **Viewer** - Read-only access

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Open a Pull Request

## License

Copyright (c) 2024 Gogidix. All rights reserved.
