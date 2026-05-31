# AI Services Architecture

## Overview

The AI Services domain provides a comprehensive suite of machine learning and artificial intelligence capabilities for the Gogidix Ecosystem. It follows a microservices architecture with both Spring Boot (Java) and FastAPI (Python) services.

## Architecture Diagram

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                              AI Gateway Service                              │
│                     (Spring Boot - API Gateway & Orchestration)              │
└─────────────────────────────────────────────────────────────────────────────┘
                                      │
                    ┌─────────────────┼─────────────────┐
                    ▼                 ▼                 ▼
        ┌───────────────────┐ ┌───────────────────┐ ┌───────────────────┐
        │  Java Services    │ │  Python ML Services│ │  Infrastructure   │
        │                   │ │                   │ │                   │
        │ • AI Gateway      │ │ • ML Training     │ │ • MongoDB         │
        │ • AI Orchestration│ │ • NLP             │ │ • Redis           │
        │ • AI Monitoring   │ │ • Computer Vision │ │ • Kafka           │
        │ • AI Testing      │ │ • Predictive       │ │ • Model Store     │
        │ • Workflow        │ │ • Anomaly Detect  │ │                   │
        │   Automation      │ │ • Recommendations  │ │                   │
        └───────────────────┘ └───────────────────┘ └───────────────────┘
```

## Services

### Core ML Services (Python FastAPI)

| Service | Port | Description |
|---------|------|-------------|
| ML Model Training Service | 8001 | Model training, evaluation, and deployment |
| NLP Service | 8002 | Sentiment analysis, chatbot, text classification |
| Predictive Analytics Service | 8003 | Demand forecasting, route optimization |
| Computer Vision Service | 8004 | OCR, object detection, face recognition |
| Anomaly Detection Service | 8005 | Fraud detection, system monitoring |
| Recommendation Service | 8006 | Collaborative filtering, content-based recommendations |

### Platform Services (Java Spring Boot)

| Service | Port | Description |
|---------|------|-------------|
| AI Gateway Service | 8080 | API gateway, load balancing, service discovery |
| AI Orchestration Service | 8081 | Workflow orchestration, pipeline management |
| AI Monitoring Service | 8082 | Health monitoring, metrics collection |
| AI Testing Service | 8083 | Model testing, validation, A/B testing |

## Technology Stack

### Backend (Java)
- **Framework**: Spring Boot 3.1.5
- **Language**: Java 17
- **Database**: MongoDB (via Spring Data MongoDB)
- **Cache**: Redis (via Spring Data Redis)
- **Messaging**: Apache Kafka
- **API Documentation**: SpringDoc OpenAPI

### ML Services (Python)
- **Framework**: FastAPI 0.109.0
- **Language**: Python 3.11
- **ML Libraries**:
  - scikit-learn 1.4.0
  - XGBoost 2.0.3
  - LightGBM 4.3.0
  - PyTorch 2.2.0 (optional)
- **Data Processing**: NumPy, Pandas
- **API Documentation**: FastAPI auto-generated docs

### Infrastructure
- **Message Queue**: Apache Kafka
- **Cache**: Redis 7
- **Database**: MongoDB 7
- **Container**: Docker, Docker Compose
- **Orchestration**: Kubernetes (optional)

## Design Patterns

### 1. Hexagonal Architecture
All services follow hexagonal architecture with clear separation of concerns:
- **Domain**: Core business logic
- **Application**: Use case orchestration
- **Infrastructure**: External integrations
- **Interfaces**: API controllers

### 2. Event-Driven Architecture
Services communicate via events through Kafka for:
- Model training completion
- Prediction requests
- Monitoring alerts
- Status updates

### 3. API Gateway Pattern
AI Gateway Service provides:
- Request routing
- Load balancing
- Rate limiting
- Authentication/authorization
- Response aggregation

### 4. Model Registry Pattern
Centralized model storage with:
- Versioning
- Metadata tracking
- Deployment status
- Performance metrics

## Data Flow

### Training Pipeline
```
User → Gateway → Training Service → ML Service → Model Store
                              ↓
                         Kafka Event
                              ↓
                    Inference Service
```

### Inference Pipeline
```
User → Gateway → Inference Service → Model Store → Model
                              ↓
                         Result → User
```

### Monitoring Pipeline
```
Services → Metrics → Monitoring Service → Alerts → Kafka → Dashboard
```

## Scalability

### Horizontal Scaling
- All services are stateless
- Docker containers for easy scaling
- Kubernetes readiness for orchestration
- Load balancing via AI Gateway

### Vertical Scaling
- Configurable resource allocation
- Model-specific resource requirements
- GPU support for deep learning models

## Security

### Authentication
- JWT-based authentication
- API key support
- OAuth 2.0 integration

### Authorization
- Role-based access control (RBAC)
- Tenant isolation
- Resource-level permissions

### Data Security
- Encryption at rest (MongoDB)
- Encryption in transit (TLS)
- PII redaction for sensitive data

## Monitoring & Observability

### Metrics
- Request/response times
- Error rates
- Model performance metrics
- Resource utilization

### Logging
- Structured JSON logging
- Centralized log aggregation
- Correlation IDs for tracing

### Tracing
- Distributed tracing support
- Request lifecycle tracking
- Performance bottleneck identification

## Deployment

### Development
```bash
docker-compose up -d
```

### Production
```bash
kubectl apply -f k8s/
```

### Environment Variables
See individual service `.env` files for configuration options.

## Extensibility

### Adding New Services
1. Create service directory
2. Implement hexagonal architecture
3. Add Dockerfile
4. Update docker-compose.yml
5. Register with AI Gateway

### Adding New Models
1. Train model via ML Training Service
2. Store in Model Registry
3. Deploy via Inference Service
4. Monitor via Monitoring Service

### Adding New Algorithms
1. Implement in ML Training Service
2. Add configuration options
3. Update documentation
4. Add tests
