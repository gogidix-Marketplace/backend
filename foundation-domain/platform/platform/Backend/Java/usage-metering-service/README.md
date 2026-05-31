# Usage Metering Service

Resource usage tracking, quota management, and billing analytics service for the Gogidix platform.

## Features

- **Real-Time Usage Collection**: High-performance ingestion of usage events (10K+ events/sec)
- **Metric Aggregation**: Hourly, daily, and monthly aggregations
- **Quota Management**: Soft limits (alerts) and hard limits (blocking)
- **Quota Enforcement**: Real-time quota checking with configurable actions
- **Alert System**: Multi-channel notifications when thresholds are exceeded
- **Usage Analytics**: Trends, forecasting, and cost analysis
- **Billing Integration**: Usage data preparation for subscription billing
- **Multi-Tenancy**: Complete tenant isolation with Row-Level Security

## Quick Start

### Prerequisites

- Java 17+
- Maven 3.9+
- PostgreSQL 14+
- Redis 7+
- Kafka 3+

### Local Development

1. Navigate to the service:
```bash
cd usage-metering-service
```

2. Build the application:
```bash
mvn clean install
```

3. Run with Spring Boot:
```bash
mvn spring-boot:run
```

4. Or use Docker Compose:
```bash
docker-compose up -d
```

The service will be available at: `http://localhost:8403/usage-metering`

## API Documentation

### Swagger UI

Interactive API documentation available at:
```
http://localhost:8403/usage-metering/swagger-ui.html
```

## Architecture

### Technology Stack

- **Framework**: Spring Boot 3.1.5
- **Language**: Java 17
- **Database**: PostgreSQL 14 with RLS
- **Cache**: Redis 7
- **Messaging**: Kafka 3
- **Build**: Maven 3.9

### Domain Models

- **MetricDefinition**: Catalog of trackable metrics
- **UsageRecord**: Raw usage events (high-volume table)
- **UsageAggregate**: Aggregated metrics by period
- **QuotaDefinition**: Quota rules and limits
- **QuotaUsage**: Current usage tracking
- **QuotaAlert**: Alert notifications

## API Endpoints

### Metric Recording

- `POST /api/v1/usage-metering/records` - Record usage (batch)
- `POST /api/v1/usage-metering/records/realtime` - Record real-time event
- `GET /api/v1/usage-metering/records` - Query usage records

### Aggregates

- `GET /api/v1/usage-metering/aggregates` - Query aggregated usage
- `GET /api/v1/usage-metering/aggregates/tenant/{tenantId}` - Get tenant aggregates
- `GET /api/v1/usage-metering/aggregates/metric/{metric}` - Get metric aggregates
- `POST /api/v1/usage-metering/aggregates/recalculate` - Recalculate aggregates

### Quotas

- `POST /api/v1/usage-metering/quotas` - Create quota definition
- `GET /api/v1/usage-metering/quotas` - List quota definitions
- `GET /api/v1/usage-metering/quotas/{id}` - Get quota definition
- `GET /api/v1/usage-metering/quotas/{id}/usage` - Get current usage
- `POST /api/v1/usage-metering/quotas/{id}/reset` - Reset quota usage

### Alerts

- `GET /api/v1/usage-metering/alerts` - List alerts
- `GET /api/v1/usage-metering/alerts/{id}` - Get alert details
- `POST /api/v1/usage-metering/alerts/{id}/acknowledge` - Acknowledge alert

### Analytics

- `GET /api/v1/usage-metering/analytics/usage-trends` - Get usage trends
- `GET /api/v1/usage-metering/analytics/top-consumers` - Get top consumers
- `GET /api/v1/usage-metering/analytics/cost-analysis` - Get cost analysis
- `GET /api/v1/usage-metering/analytics/forecast` - Get usage forecast

## Configuration

### Environment Variables

| Variable | Description | Default |
|----------|-------------|---------|
| `DATABASE_URL` | PostgreSQL JDBC URL | `jdbc:postgresql://localhost:5432/usage_metering` |
| `REDIS_HOST` | Redis host | `localhost` |
| `KAFKA_BOOTSTRAP_SERVERS` | Kafka servers | `localhost:9092` |
| `SERVER_PORT` | Service port | `8403` |

### Application Properties

```yaml
usage:
  metering:
    batch-size: 100
    aggregation-interval-minutes: 60
    retention-days: 90
    real-time-ingestion: true

  quotas:
    enforcement-enabled: true
    soft-limit-action: ALERT
    hard-limit-action: BLOCK
```

## Deployment

### Using Docker

```bash
# Build image
docker build -t usage-metering-service:latest .

# Run container
docker run -p 8403:8403 \
  -e DATABASE_URL=jdbc:postgresql://localhost:5432/usage_metering \
  usage-metering-service:latest
```

### Railway Deployment

```bash
# Install Railway CLI
npm install -g @railway/cli

# Login and deploy
railway login
railway init
railway up
```

## Usage Recording

### Record Usage Event

```bash
curl -X POST http://localhost:8403/usage-metering/api/v1/usage-metering/records \
  -H "Content-Type: application/json" \
  -H "X-Tenant-ID: tenant-123" \
  -d '{
    "metric_name": "api_calls",
    "quantity": 1,
    "unit": "calls",
    "service_name": "api-gateway",
    "user_id": "user-123"
  }'
```

### Batch Recording

```bash
curl -X POST http://localhost:8403/usage-metering/api/v1/usage-metering/records/batch \
  -H "Content-Type: application/json" \
  -H "X-Tenant-ID: tenant-123" \
  -d '{
    "records": [
      {"metric_name": "api_calls", "quantity": 5},
      {"metric_name": "storage_gb", "quantity": 1.5}
    ]
  }'
```

## Quota Management

### Create Quota

```bash
curl -X POST http://localhost:8403/usage-metering/api/v1/usage-metering/quotas \
  -H "Content-Type: application/json" \
  -H "X-Tenant-ID: tenant-123" \
  -d '{
    "quota_name": "API Calls Daily Limit",
    "metric_name": "api_calls",
    "hard_limit": 10000,
    "quota_period": "DAILY",
    "hard_limit_action": "BLOCK"
  }'
```

### Check Quota Usage

```bash
curl http://localhost:8403/usage-metering/api/v1/usage-metering/quotas/{id}/usage \
  -H "X-Tenant-ID: tenant-123"
```

## Testing

```bash
# Unit tests
mvn test

# Integration tests
mvn verify -P integration-test

# Coverage report
mvn jacoco:report
```

## Monitoring

### Health Check
```
GET /usage-metering/actuator/health
```

### Metrics
```
GET /usage-metering/actuator/metrics
GET /usage-metering/actuator/prometheus
```

### Custom Metrics

- `usage_records_received_total`: Total records ingested
- `usage_aggregation_duration_seconds`: Time to aggregate
- `quota_checks_total`: Total quota checks performed
- `quota_exceeded_total`: Times quota was exceeded

## Performance

### High-Volume Ingestion

- **Batch Size**: 100 records per request
- **Throughput**: 10,000+ records/second
- **Latency**: < 50ms p95 for recording
- **Aggregation**: Runs every 60 minutes

### Optimization Strategies

1. **Partitioning**: Partition usage_records by month
2. **Indexing**: Composite indexes on tenant_id + event_time
3. **Caching**: Redis for hot quota data
4. **Batch Processing**: Kafka Streams for real-time aggregation

## Contributing

1. Create feature branch: `git checkout -b feature/your-feature`
2. Commit changes: `git commit -am 'Add feature'`
3. Push branch: `git push origin feature/your-feature`
4. Create merge request

## License

Copyright © 2025 Gogidix Platform Team. All rights reserved.
