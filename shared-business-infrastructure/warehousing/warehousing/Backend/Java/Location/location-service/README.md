# location-service

Multi-tenant service for Gogidix ecosystem.

## Features

- Row-Level Security for tenant isolation
- Kafka event streaming
- PostgreSQL persistence
- Railway deployment ready

## Local Development

```bash
mvn spring-boot:run
```

## Build

```bash
mvn clean package
```

## Deploy to Railway

```bash
railway up
```

## API Endpoints

- GET /actuator/health - Health check
- GET /health - Service health

## Configuration

See `application.yml` for configuration options.
