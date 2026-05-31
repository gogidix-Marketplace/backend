# Gogidix Corporate Website

A modern, enterprise-grade corporate website built with Next.js 14, featuring multi-language support, responsive design, and comprehensive content pages.

## Tech Stack

- **Framework**: Next.js 14 (App Router)
- **Language**: TypeScript 5.3
- **Styling**: Tailwind CSS 3.4
- **Animations**: Framer Motion 11
- **State Management**: Zustand 4.4
- **Data Fetching**: TanStack Query 5.17
- **Forms**: React Hook Form 7.49 + Zod 3.22
- **i18n**: next-intl
- **UI Components**: Radix UI primitives
- **Testing**: React Testing Library + Jest

## Features

### Pages
- **Home**: Hero section, products showcase, features, CTAs
- **Products**: 6 product pages (Logistics, E-commerce, Procurement, Business Ops, Enterprise, Infrastructure)
- **Solutions**: By Industry, Company Size, Region, Case Studies
- **Developers**: API Reference, SDKs, Webhooks, Sandbox environment
- **Partners**: White-Label, Technology Partners, System Integrators
- **Company**: About Us, Leadership, Careers, Press, Contact
- **Resources**: Documentation, Blog, Webinars, Security

### Features
- Global search (Cmd/Ctrl + K)
- Multi-language support (EN, FR, ES, PT, AR)
- Region selector
- Fully responsive design
- SEO optimized with next-seo
- Cookie consent banner
- Contact forms with validation
- Newsletter signup
- Dark mode support

## Getting Started

### Prerequisites

- Node.js 18.0 or higher
- npm 9.0 or higher

### Installation

1. Clone the repository and navigate to the project directory:
```bash
cd corporate-website-public
```

2. Install dependencies:
```bash
npm install
```

3. Copy environment variables:
```bash
cp .env.example .env.local
```

4. Update `.env.local` with your configuration:
```env
NEXT_PUBLIC_SITE_URL=http://localhost:3000
NEXT_PUBLIC_API_URL=https://api.gogidix.com
```

### Development

Start the development server:
```bash
npm run dev
```

Open [http://localhost:3000](http://localhost:3000) in your browser.

### Build

Build for production:
```bash
npm run build
```

Start the production server:
```bash
npm start
```

## Project Structure

```
corporate-website-public/
├── src/
│   ├── app/                 # Next.js App Router
│   │   ├── [locale]/        # Localized routes
│   │   │   ├── page.tsx     # Home page
│   │   │   ├── products/    # Product pages
│   │   │   ├── solutions/   # Solution pages
│   │   │   ├── developers/  # Developer portal
│   │   │   ├── partners/    # Partner pages
│   │   │   ├── company/     # Company pages
│   │   │   ├── resources/   # Resource pages
│   │   │   └── layout.tsx   # Locale layout
│   │   ├── globals.css      # Global styles
│   │   └── layout.tsx       # Root layout
│   ├── components/          # React components
│   │   ├── ui/              # Base UI components
│   │   ├── layout/          # Layout components (Header, Footer)
│   │   ├── home/            # Home page components
│   │   ├── products/        # Product components
│   │   ├── solutions/       # Solution components
│   │   ├── developers/      # Developer components
│   │   ├── partners/        # Partner components
│   │   ├── company/         # Company components
│   │   ├── resources/       # Resource components
│   │   ├── contact/         # Contact components
│   │   └── providers/       # Context providers
│   ├── lib/                 # Utility functions
│   │   └── utils.ts
│   ├── stores/              # Zustand stores
│   │   └── ui-store.ts
│   ├── hooks/               # Custom hooks
│   ├── api/                 # API client & endpoints
│   ├── i18n/                # Internationalization
│   │   ├── routing.ts
│   │   └── request.ts
│   └── types/               # TypeScript types
├── messages/                # Translation files
│   ├── en.json
│   ├── fr.json
│   ├── es.json
│   ├── pt.json
│   └── ar.json
├── public/                  # Static assets
├── .env.example             # Environment template
├── next.config.js           # Next.js configuration
├── tailwind.config.ts       # Tailwind configuration
├── tsconfig.json            # TypeScript configuration
├── Dockerfile               # Docker configuration
└── docker-compose.yml       # Docker Compose configuration
```

## Available Scripts

| Script | Description |
|--------|-------------|
| `npm run dev` | Start development server |
| `npm run build` | Build for production |
| `npm start` | Start production server |
| `npm run lint` | Run ESLint |
| `npm run type-check` | Run TypeScript type check |
| `npm run format` | Format code with Prettier |
| `npm test` | Run tests |
| `npm run test:coverage` | Run tests with coverage |

## Docker Deployment

Build and run with Docker:

```bash
docker build -t gogidix-website .
docker run -p 3000:3000 gogidix-website
```

Or use Docker Compose:

```bash
docker-compose up -d
```

## Adding a New Language

1. Create a new translation file in `messages/`:
```bash
cp messages/en.json messages/de.json
```

2. Add the locale to `src/i18n/routing.ts`:
```typescript
locales: ['en', 'fr', 'es', 'pt', 'ar', 'de'],
```

3. Update `src/lib/constants.ts` LOCALES array.

## Adding a New Page

1. Create a new route in `src/app/[locale]/your-page/`
2. Add page.tsx with your component
3. Add translations to `messages/en.json`
4. Link from navigation in `src/components/layout/header.tsx`

## Testing

Run tests in watch mode:
```bash
npm run test:watch
```

Generate coverage report:
```bash
npm run test:coverage
```

## Performance

- Image optimization with Next.js Image component
- Static generation where possible
- Code splitting with dynamic imports
- Edge runtime for API routes
- CDN deployment ready

## Security

- CSRF protection
- XSS prevention
- Content Security Policy headers
- HTTPS enforcement in production
- Environment variable protection

## License

Copyright (c) 2024 Gogidix. All rights reserved.
