# Country Sales Dashboard - Backend Service

Spring Boot backend service for country-level sales operations management.

## Quick Start

```bash
# Build
mvn clean package

# Run
mvn spring-boot:run

# Run tests
mvn test

# Test with coverage
mvn verify
```

## API Endpoints

### Dashboard APIs
- `GET /api/sales/v1/dashboard/metrics` - Comprehensive sales metrics
- `GET /api/sales/v1/dashboard/summary` - Quick dashboard summary
- `GET /api/sales/v1/dashboard/pipeline/summary` - Pipeline statistics
- `GET /api/sales/v1/dashboard/customers/needs-followup` - Customers needing follow-up
- `GET /api/sales/v1/deals/overdue` - Overdue deals
- `GET /api/sales/v1/deals/followup` - Deals needing follow-up

### Customer APIs
- `GET /api/sales/v1/customers` - List customers (paginated)
- `POST /api/sales/v1/customers` - Create customer
- `GET /api/sales/v1/customers/{id}` - Get customer by ID
- `GET /api/sales/v1/customers/code/{code}` - Get customer by code
- `PUT /api/sales/v1/customers/{id}` - Update customer
- `DELETE /api/sales/v1/customers/{id}` - Delete customer (soft delete)
- `GET /api/sales/v1/customers/search?query={text}` - Search customers
- `GET /api/sales/v1/customers/status/{status}` - Filter by status
- `GET /api/sales/v1/customers/tier/{tier}` - Filter by tier

### Deal APIs
- `GET /api/sales/v1/deals` - List deals (paginated)
- `POST /api/sales/v1/deals` - Create deal
- `GET /api/sales/v1/deals/{id}` - Get deal by ID
- `PUT /api/sales/v1/deals/{id}` - Update deal
- `DELETE /api/sales/v1/deals/{id}` - Delete deal (soft delete)
- `GET /api/sales/v1/deals/stage/{stage}` - Filter by stage
- `GET /api/sales/v1/deals/owner/{ownerId}` - Filter by owner
- `GET /api/sales/v1/deals/customer/{customerId}` - Filter by customer

## Configuration

### Application Properties

```yaml
spring:
  data:
    mongodb:
      uri: mongodb://localhost:27017
      database: country_sales_dashboard
  kafka:
    bootstrap-servers: localhost:9092

country:
  code: US
  name: United States
  currency: USD

hq:
  sales:
    base-url: http://localhost:8081/api/sales-hq/v1
```

### Environment Variables

- `MONGODB_URI` - MongoDB connection string
- `SPRING_KAFKA_BOOTSTRAP_SERVERS` - Kafka bootstrap servers
- `COUNTRY_CODE` - ISO country code
- `JWT_SECRET` - JWT signing secret
- `HQ_SALES_BASE_URL` - HQ Sales Department API URL

## Domain Models

### Customer
- Tracks company information, tier, status
- Financial metrics (annual revenue, lifetime value)
- Account manager assignment

### Deal
- Pipeline stage management
- Value and probability tracking
- Weighted value calculation
- Customer and territory association

### Sales Team Member
- Sales role hierarchy
- Quota and performance tracking
- Territory assignment

### Territory
- Geographic/segment-based territories
- Target metrics
- Manager assignment

### Forecast
- Sales forecast by period
- Category-based (Conservative to Stretch)
- Approval workflow

### Sales Target
- HQ-assigned targets
- Progress tracking
- Period-based reporting

## Service Layer

### CustomerService
- CRUD operations for customers
- Tier management
- Follow-up tracking
- Kafka integration for updates

### DealService
- CRUD operations for deals
- Pipeline movement tracking
- Weighted value calculation
- Stage transitions

### SalesMetricsService
- Aggregated metrics calculation
- Dashboard summary
- Target progress tracking

## Kafka Integration

### Topics
- `sales.performance.country` - Performance snapshots
- `sales.pipeline.country` - Deal updates
- `sales.customer.country` - Customer updates
- `sales.forecast.country` - Forecasts
- `sales.targets.country` - Target progress

### Events
- PipelineUpdateEvent
- CustomerUpdateEvent
- ForecastUpdateEvent
- TargetUpdateEvent
- SalesPerformanceSnapshot

## HQ Integration

### HqSalesClient
- Circuit breaker pattern with Resilience4j
- Automatic retry with exponential backoff
- Fallback methods for resilience

### Capabilities
- Fetch sales targets from HQ
- Acknowledge target receipt
- Report target progress
- Get active campaigns
- Report sales performance

## Testing

```bash
# Unit tests
mvn test

# Integration tests
mvn verify

# Coverage report
mvn jacoco:report
```

### Test Structure
- Unit tests for all services
- Repository layer tests with embedded MongoDB
- Integration tests for controllers
- 80%+ coverage target

## Monitoring

### Actuator Endpoints
- `/actuator/health` - Health check
- `/actuator/metrics` - Application metrics
- `/actuator/prometheus` - Prometheus metrics

### Custom Metrics
- Sales pipeline value
- Deal conversion rates
- Customer acquisition metrics
- Target achievement percentages
