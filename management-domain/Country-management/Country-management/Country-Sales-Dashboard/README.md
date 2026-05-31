# Country Sales Dashboard

A production-ready country-level sales operations dashboard for managing customers, deals, pipeline, territories, and sales performance reporting to HQ.

## Overview

The Country Sales Dashboard handles all sales operations at the country level and reports to the Sales Department at HQ. It provides comprehensive tools for:

- **Sales Pipeline Tracking** - Track deals through all stages of the sales process
- **Revenue Tracking** - Monitor country revenue, targets, and forecasting
- **Customer Management** - Manage country customer relationships and tiers
- **Deal Management** - Track opportunities, win rates, and deal velocity
- **Sales Team Management** - Monitor sales team performance and quotas
- **Territory Management** - Manage geographical and segment-based territories
- **Sales Forecasting** - Create and manage sales forecasts
- **HQ Integration** - Report performance to HQ via API and Kafka

## Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                     Frontend (Next.js)                        │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐  ┌──────────┐    │
│  │ Dashboard│  │ Pipeline │  │Customers │  │  Deals   │    │
│  └──────────┘  └──────────┘  └──────────┘  └──────────┘    │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│              Backend (Spring Boot)                           │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐        │
│  │ Controllers │  │  Services   │  │Repositories │        │
│  └─────────────┘  └─────────────┘  └─────────────┘        │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐        │
│  │    DTOs     │  │   Models    │  │   Kafka     │        │
│  └─────────────┘  └─────────────┘  └─────────────┘        │
└─────────────────────────────────────────────────────────────┘
                              │
                    ┌─────────┴─────────┐
                    ▼                   ▼
            ┌──────────┐          ┌──────────┐
            │ MongoDB  │          │  Kafka   │
            └──────────┘          └──────────┘
```

## Quick Start

### Prerequisites

- Java 21+
- Node.js 20+
- MongoDB 7.0+
- Kafka 3.7+ (for HQ integration)

### Using Docker Compose (Recommended)

```bash
# Start all services
docker-compose up -d

# View logs
docker-compose logs -f

# Stop services
docker-compose down
```

### Local Development

#### Backend

```bash
cd backend

# Run tests
mvn test

# Start application
mvn spring-boot:run

# Build for production
mvn clean package -DskipTests
```

#### Frontend

```bash
cd frontend

# Install dependencies
npm install

# Run development server
npm run dev

# Run tests
npm test

# Build for production
npm run build
```

## Configuration

### Backend Environment Variables

| Variable | Description | Default |
|----------|-------------|---------|
| `SPRING_PROFILES_ACTIVE` | Environment profile | `dev` |
| `MONGODB_URI` | MongoDB connection string | `mongodb://localhost:27017` |
| `SPRING_KAFKA_BOOTSTRAP_SERVERS` | Kafka servers | `localhost:9092` |
| `COUNTRY_CODE` | Country code for this instance | `US` |
| `JWT_SECRET` | JWT signing secret | - |
| `HQ_SALES_BASE_URL` | HQ Sales API URL | - |

### Frontend Environment Variables

| Variable | Description | Default |
|----------|-------------|---------|
| `NEXT_PUBLIC_API_URL` | Backend API URL | `http://localhost:8080/api/sales/v1` |
| `NEXT_PUBLIC_COUNTRY_CODE` | Country code | `US` |

## API Documentation

Once the backend is running, visit:
- Swagger UI: `http://localhost:8080/api/sales/v1/swagger-ui.html`
- OpenAPI Docs: `http://localhost:8080/api/sales/v1/api-docs`

### Main Endpoints

#### Dashboard
- `GET /dashboard/metrics` - Get comprehensive sales metrics
- `GET /dashboard/summary` - Get dashboard summary
- `GET /dashboard/pipeline/summary` - Get pipeline statistics

#### Customers
- `GET /customers` - List all customers
- `POST /customers` - Create new customer
- `GET /customers/{id}` - Get customer by ID
- `PUT /customers/{id}` - Update customer
- `DELETE /customers/{id}` - Delete customer

#### Deals
- `GET /deals` - List all deals
- `POST /deals` - Create new deal
- `GET /deals/{id}` - Get deal by ID
- `PUT /deals/{id}` - Update deal
- `DELETE /deals/{id}` - Delete deal
- `GET /deals/stage/{stage}` - Get deals by stage

## Kafka Integration with HQ

The dashboard sends sales data to HQ via Kafka topics:

| Topic | Purpose |
|-------|---------|
| `sales.performance.country` | Sales performance snapshots |
| `sales.pipeline.country` | Pipeline and deal updates |
| `sales.customer.country` | Customer updates |
| `sales.forecast.country` | Forecast submissions |
| `sales.targets.country` | Target progress updates |

## Data Models

### Customer
- Customer code, company name, industry
- Status (Active, Inactive, Prospect, Churned)
- Tier (Bronze, Silver, Gold, Platinum, Diamond)
- Annual revenue, lifetime value
- Account manager assignment

### Deal
- Deal code, title, customer
- Stage (Prospecting to Closed Won/Lost)
- Priority, value, weighted value
- Probability, expected close date
- Owner, territory assignment

### Sales Team Member
- Employee ID, name, role
- Quota, YTD revenue, win rate
- Territory assignment

### Territory
- Territory code, name, type
- Geographic or segment boundaries
- Target accounts, metrics

### Forecast
- Forecast category (Conservative to Stretch)
- Period, committed forecast
- Approval workflow

### Sales Target
- HQ-assigned targets
- Progress tracking
- Period-based reporting

## Testing

### Backend Tests
```bash
cd backend
mvn test
mvn verify # with coverage
```

### Frontend Tests
```bash
cd frontend
npm test
npm run test:coverage
```

## Deployment

### Build Docker Images

```bash
# Backend
docker build -t country-sales-backend ./backend

# Frontend
docker build -t country-sales-frontend ./frontend
```

### Kubernetes (Example)

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: country-sales-backend
spec:
  replicas: 3
  selector:
    matchLabels:
      app: country-sales-backend
  template:
    metadata:
      labels:
        app: country-sales-backend
    spec:
      containers:
      - name: backend
        image: country-sales-backend:latest
        ports:
        - containerPort: 8080
        env:
        - name: COUNTRY_CODE
          value: "US"
        - name: SPRING_PROFILES_ACTIVE
          value: "prod"
```

## Directory Structure

```
Country-Sales-Dashboard/
├── backend/
│   ├── src/main/java/com/gogidix/countrysales/
│   │   ├── application/     # DTOs, config, exceptions, client
│   │   ├── domain/          # Models, enums, repositories, services
│   │   ├── infrastructure/  # Kafka, persistence
│   │   └── interfaces/      # REST controllers
│   ├── src/main/resources/
│   │   └── application.yml
│   └── src/test/            # Unit and integration tests
├── frontend/
│   ├── src/
│   │   ├── app/             # Next.js pages
│   │   ├── components/      # React components
│   │   ├── services/        # API services
│   │   ├── lib/             # Utilities
│   │   └── types/           # TypeScript types
│   └── public/
└── docker-compose.yml
```

## Support

For issues and questions:
- GitHub Issues: [Gogidix Ecosystem](https://github.com/gogidix)
- Email: dev@gogidix.com

## License

Proprietary - (C) 2024 Gogidix. All rights reserved.
