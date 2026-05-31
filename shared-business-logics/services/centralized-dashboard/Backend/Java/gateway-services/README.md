# Gateway Services

Backend microservices for the Centralized Dashboard.

## Services

| Service | Port | Description |
|---------|------|-------------|
| api-gateway-service | 8907 | BFF (Backend for Frontend) gateway |
| websocket-gateway-service | 8908 | Real-time WebSocket gateway |
| chart-service | 8909 | Chart data provider |

## Prerequisites

- Java 17
- Maven 3.9+
- PostgreSQL 15+
- Redis 7+

## Building

```bash
# Build all services
mvn clean install

# Build individual service
cd api-gateway-service
mvn clean package
```

## Running Locally

### Using Docker Compose

```bash
docker-compose up -d
```

### Manual Setup

1. Start PostgreSQL and Redis
2. Run each service:

```bash
# API Gateway
cd api-gateway-service
mvn spring-boot:run

# WebSocket Gateway
cd websocket-gateway-service
mvn spring-boot:run

# Chart Service
cd chart-service
mvn spring-boot:run
```

## API Documentation

- API Gateway: http://localhost:8907/swagger-ui.html
- WebSocket Gateway: http://localhost:8908/swagger-ui.html
- Chart Service: http://localhost:8909/swagger-ui.html

## Configuration

Each service can be configured via environment variables or application.yml:

| Variable | Description | Default |
|----------|-------------|---------|
| DATABASE_URL | PostgreSQL JDBC URL | jdbc:postgresql://localhost:5432/{service} |
| DATABASE_USERNAME | Database username | postgres |
| DATABASE_PASSWORD | Database password | postgres |
| REDIS_HOST | Redis host | localhost |
| REDIS_PORT | Redis port | 6379 |
| SERVER_PORT | Service port | {service specific} |
