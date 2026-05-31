# Kafka Ingestion Service - API Documentation

## Overview
This service primarily consumes Kafka events rather than exposing REST APIs. However, it provides management endpoints for monitoring and administration.

## Base URL
```
http://localhost:8080/api/v1/kafka
```

## Management Endpoints

### Consumer Status

#### Get Consumer Status
```http
GET /api/v1/kafka/consumer/status
```

**Response**: `200 OK`
```json
{
  "status": "RUNNING",
  "consumers": [
    {
      "topic": "orders.v1",
      "partition": 0,
      "lag": 125,
      "consumerId": "global-business-ingestion-0",
      "host": "kafka-ingestion-service-1"
    }
  ],
  "lastPoll": "2024-01-01T12:00:00Z"
}
```

#### Get Consumer Metrics
```http
GET /api/v1/kafka/consumer/metrics
```

**Response**: `200 OK`
```json
{
  "recordsConsumed": 15000,
  "recordsProcessed": 14985,
  "recordsFailed": 15,
  "processingRatePerSecond": 250,
  "avgProcessingTimeMs": 45,
  "period": "PT1M"
}
```

### Dead Letter Queue

#### Get DLQ Statistics
```http
GET /api/v1/kafka/dlq/stats
```

**Response**: `200 OK`
```json
{
  "orders.dlq": {
    "size": 25,
    "lastFailedAt": "2024-01-01T11:55:00Z"
  },
  "payments.dlq": {
    "size": 5,
    "lastFailedAt": "2024-01-01T11:50:00Z"
  }
}
```

#### Retry DLQ Messages
```http
POST /api/v1/kafka/dlq/{topic}/retry
```

**Request Body**:
```json
{
  "maxMessages": 100,
  "filter": "error.type == 'TransientException'"
}
```

### Health Check

#### Service Health
```http
GET /api/v1/kafka/health
```

**Response**: `200 OK`
```json
{
  "status": "UP",
  "kafkaConnection": "UP",
  "mongoConnection": "UP",
  "consumers": {
    "orders.v1": "ACTIVE",
    "customers.v1": "ACTIVE",
    "payments.v1": "ACTIVE"
  }
}
```

## Event Schemas

### OrderCreated Event
```json
{
  "eventType": "OrderCreated",
  "eventId": "uuid",
  "timestamp": "2024-01-01T12:00:00Z",
  "aggregateId": "order-123",
  "data": {
    "orderId": "order-123",
    "customerId": "customer-456",
    "regionCode": "NA",
    "countryCode": "US",
    "currency": "USD",
    "totalAmount": 150.00,
    "items": [
      {
        "sku": "PROD-001",
        "quantity": 2,
        "unitPrice": 75.00
      }
    ]
  }
}
```

### CustomerRegistered Event
```json
{
  "eventType": "CustomerRegistered",
  "eventId": "uuid",
  "timestamp": "2024-01-01T12:00:00Z",
  "aggregateId": "customer-456",
  "data": {
    "customerId": "customer-456",
    "email": "customer@example.com",
    "regionCode": "NA",
    "countryCode": "US",
    "registrationDate": "2024-01-01T12:00:00Z"
  }
}
```

## Error Responses

### 503 Service Unavailable
```json
{
  "timestamp": "2024-01-01T00:00:00Z",
  "status": 503,
  "error": "Service Unavailable",
  "message": "Kafka broker connection failed"
}
```

### 500 Internal Server Error
```json
{
  "timestamp": "2024-01-01T00:00:00Z",
  "status": 500,
  "error": "Internal Server Error",
  "message": "Event processing failed"
}
```

## Monitoring

### Metrics Exposed
- **kafka.consumer.records.consumed.total**: Total records consumed
- **kafka.consumer.records.failed.total**: Total processing failures
- **kafka.consumer.lag.max**: Maximum consumer lag across partitions
- **kafka.consumer.processing.time**: Event processing time

### Alert Thresholds
- **Consumer Lag**: > 10000 records
- **Error Rate**: > 1% of total records
- **DLQ Size**: > 1000 messages
- **Processing Time**: > 5000ms p95
