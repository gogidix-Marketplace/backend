# 🔧 SHARED UTILITIES SERVICE

## **Enterprise Utility Library for the GOGIDIX Ecosystem**

[![Build Status](https://gitlab.com/gogidix/shared-libraries/shared-utilities/badges/main/pipeline.svg)](https://gitlab.com/gogidix/shared-libraries/shared-utilities/-/pipelines)
[![Coverage Report](https://gitlab.com/gogidix/shared-libraries/shared-utilities/badges/main/coverage.svg)](https://gitlab.com/gogidix/shared-libraries/shared-utilities/-/coverage)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=gogidx-infrastructure_shared-utilities&metric=security_rating)](https://sonarcloud.io/summary/new_code?id=gogidx-infrastructure_shared-utilities)
[![Version](https://img.shields.io/badge/version-1.0.0-blue)](https://gitlab.com/gogidix/shared-libraries/shared-utilities/-/releases)

---

## 📋 **Table of Contents**

- [🎯 Service Overview](#-service-overview)
- [🚀 Quick Start](#-quick-start)  
- [📚 API Documentation](#-api-documentation)
- [🔧 Configuration](#-configuration)
- [🏃‍♂️ Development](#️-development)
- [🔒 Security](#-security)
- [🚀 Deployment](#-deployment)
- [📊 Monitoring](#-monitoring)
- [🤝 Contributing](#-contributing)
- [📞 Support](#-support)

---

## 🎯 **Service Overview**

The **Shared Utilities Service** is a foundational microservice within the GOGIDIX Social Commerce Ecosystem, providing comprehensive utility functions across all business domains. Built with Spring Boot 3.1.5 and Java 17, it delivers high-performance, enterprise-grade utility operations with advanced caching and security features.

### **🏗️ Architecture**
- **Pattern**: Hexagonal Architecture (Ports & Adapters)
- **Port**: 8707
- **Framework**: Spring Boot 3.1.5 + Java 17
- **Database**: PostgreSQL 15+ with HikariCP pooling
- **Cache**: Redis 7+ with multi-tier caching strategy
- **Messaging**: Kafka-based event streaming

### **🔧 Core Features**

#### **String Utilities**
- Text formatting, validation, and manipulation
- Regex pattern matching and replacement
- String sanitization and security filtering  
- Multi-language text processing support

#### **Date/Time Operations**
- Date calculations and formatting
- Timezone conversion and DST handling
- Business calendar operations
- Scheduling and time-based calculations

#### **JSON Processing**
- JSON serialization and deserialization
- Schema validation and transformation
- Data mapping and conversion operations
- Configuration and template processing

#### **File Operations**
- File upload, processing, and validation
- Document conversion and manipulation
- Security scanning and virus checking
- Image and media processing capabilities

#### **Data Validation**
- Input validation and sanitization
- Business rule validation and enforcement
- Data quality checks and constraint validation
- Comprehensive error reporting and handling

#### **Performance Optimization**
- Multi-tier caching strategy (95%+ hit rates)
- Asynchronous processing for heavy operations
- Connection pooling and resource management
- Auto-scaling and load balancing support

### **🏢 Ecosystem Integration**

The service seamlessly integrates with all GOGIDIX business domains:
- **Social Commerce**: Product validation, price formatting, content processing
- **Warehousing**: Inventory calculations, batch processing, data validation
- **Courier Services**: Address formatting, route calculations, tracking utilities
- **Haulage Logistics**: Load calculations, compliance validation, scheduling
- **Management Support**: Report generation, data analysis, executive dashboards
- **AI Services**: Data preprocessing, feature extraction, model validation

### **📊 Performance Specifications**

```ascii
┌─── PERFORMANCE METRICS ────────────────────────────────────────┐
│                                                                │
│ Response Times (P95):        Cache Performance:               │
│ ├─ String Operations: <10ms  ├─ L1 Cache Hit: <1ms          │
│ ├─ DateTime Ops: <5ms       ├─ L2 Cache Hit: <3ms          │
│ ├─ JSON Processing: <20ms    ├─ Database Query: <15ms       │
│ ├─ File Operations: <100ms   └─ Cache Hit Rate: 95%         │
│ └─ Validation: <15ms                                         │
│                                                                │
│ Throughput Capacity:         Scalability:                     │
│ ├─ String Utils: 15k req/s   ├─ Min Replicas: 2             │
│ ├─ DateTime: 20k req/s       ├─ Max Replicas: 10            │
│ ├─ JSON Utils: 10k req/s     ├─ Auto-scaling: CPU/Memory    │
│ ├─ File Utils: 5k req/s      └─ Target Utilization: 70%     │
│ └─ Validation: 18k req/s                                     │
└────────────────────────────────────────────────────────────────┘
```

---

## 🚀 **Quick Start**

### **Prerequisites**

```bash
# Required Software
Java 17 (OpenJDK or Eclipse Temurin)
Maven 3.9+
Docker 24.x+  
PostgreSQL 15+
Redis 7+

# Optional for Development
IntelliJ IDEA or Visual Studio Code
Postman or curl for API testing
GitLab CLI for repository operations
```

### **🏃‍♂️ Development Setup**

#### **1. Clone Repository**
```bash
# Clone the shared-utilities repository
git clone https://gitlab.com/gogidix/shared-libraries/shared-utilities.git
cd shared-utilities

# Verify Java and Maven versions
java -version   # Should show Java 17
mvn -version    # Should show Maven 3.9+
```

#### **2. Local Development with Docker Compose**
```bash
# Start complete development environment
docker-compose up -d

# Services started:
# ├─ PostgreSQL (port 5432) - Main database
# ├─ Redis (port 6379) - Cache and sessions
# ├─ PgAdmin (port 8080) - Database management UI
# └─ Redis Commander (port 8081) - Cache management UI

# Verify services are running
docker-compose ps
```

#### **3. Application Startup**
```bash
# Build and run the application
./mvnw clean compile
./mvnw spring-boot:run

# Application will start on port 8707
# Health check: http://localhost:8707/actuator/health
# API documentation: http://localhost:8707/swagger-ui.html
```

#### **4. Quick Verification**
```bash
# Test basic string utility
curl -X POST http://localhost:8707/api/utilities/string/format \
  -H "Content-Type: application/json" \
  -d '{"text": "hello world", "format": "UPPER_CASE"}'

# Expected response: {"result": "HELLO WORLD", "processed": true}

# Test health endpoints
curl http://localhost:8707/actuator/health/liveness
curl http://localhost:8707/actuator/health/readiness
```

### **🐳 Docker Development**

```bash
# Build Docker image
docker build -t shared-utilities:dev .

# Run with environment variables
docker run -d --name shared-utils-dev \
  -p 8707:8707 \
  -e SPRING_PROFILES_ACTIVE=docker \
  -e DB_HOST=host.docker.internal \
  -e REDIS_HOST=host.docker.internal \
  shared-utilities:dev

# View logs
docker logs -f shared-utils-dev
```

### **☸️ Kubernetes Development**

```bash
# Create development namespace
kubectl create namespace gogidix-dev

# Deploy to Kubernetes
kubectl apply -f k8s/ -n gogidix-dev

# Check deployment status
kubectl get pods -n gogidix-dev
kubectl logs -f deployment/shared-utilities -n gogidix-dev

# Port forward for local access
kubectl port-forward -n gogidix-dev service/shared-utilities 8707:8707
```

---

## 📚 **API Documentation**

### **🌐 Interactive Documentation**

- **Swagger UI**: [http://localhost:8707/swagger-ui.html](http://localhost:8707/swagger-ui.html)
- **OpenAPI Spec**: [http://localhost:8707/v3/api-docs](http://localhost:8707/v3/api-docs)
- **ReDoc**: [http://localhost:8707/redoc](http://localhost:8707/redoc)
- **Postman Collection**: [Download Collection](api-docs/postman-collection.json)

### **🔧 Core API Endpoints**

#### **String Utilities API**
```bash
# Base Path: /api/utilities/string

# Format text with various transformations
POST /api/utilities/string/format
Content-Type: application/json
{
  "text": "hello world example",
  "format": "CAMEL_CASE"
}
Response: {"result": "helloWorldExample", "processed": true}

# Validate text against patterns
POST /api/utilities/string/validate
{
  "text": "user@example.com",
  "pattern": "EMAIL",
  "strict": true
}
Response: {"valid": true, "pattern": "EMAIL", "score": 1.0}

# Sanitize text for security
POST /api/utilities/string/sanitize
{
  "text": "<script>alert('xss')</script>Clean content",
  "level": "STRICT",
  "preserveWhitespace": false
}
Response: {"result": "Clean content", "sanitized": true, "removedTags": ["script"]}

# Bulk text processing
POST /api/utilities/string/batch
{
  "operations": [
    {"text": "hello", "operation": "UPPER"},
    {"text": "WORLD", "operation": "LOWER"}
  ]
}
Response: {"results": ["HELLO", "world"], "processed": 2}
```

#### **Date/Time Utilities API**
```bash
# Base Path: /api/utilities/datetime

# Format dates with timezone conversion
POST /api/utilities/datetime/format
{
  "dateTime": "2025-08-14T10:30:00",
  "sourceTimezone": "UTC",
  "targetTimezone": "America/New_York",
  "format": "yyyy-MM-dd HH:mm:ss z"
}
Response: {"result": "2025-08-14 06:30:00 EDT", "timezone": "America/New_York"}

# Calculate business days between dates
POST /api/utilities/datetime/business-days
{
  "startDate": "2025-08-14",
  "endDate": "2025-08-21",
  "holidays": ["2025-08-15"],
  "weekends": ["SATURDAY", "SUNDAY"]
}
Response: {"businessDays": 4, "excludedDays": 3, "holidays": 1}

# Parse and validate date strings
POST /api/utilities/datetime/parse
{
  "dateString": "Aug 14, 2025 10:30 AM",
  "patterns": ["MMM dd, yyyy hh:mm a", "yyyy-MM-dd HH:mm:ss"],
  "timezone": "UTC"
}
Response: {"parsed": "2025-08-14T10:30:00Z", "pattern": "MMM dd, yyyy hh:mm a", "valid": true}
```

#### **JSON Utilities API**
```bash
# Base Path: /api/utilities/json

# Validate JSON against schema
POST /api/utilities/json/validate
{
  "jsonData": {"name": "John", "age": 30, "email": "john@example.com"},
  "schemaType": "USER_PROFILE",
  "strict": true
}
Response: {"valid": true, "schema": "USER_PROFILE", "errors": []}

# Transform JSON structure
POST /api/utilities/json/transform
{
  "sourceJson": {"firstName": "John", "lastName": "Doe", "age": 30},
  "mapping": {
    "fullName": "{firstName} {lastName}",
    "age": "age",
    "adult": "age >= 18"
  }
}
Response: {"result": {"fullName": "John Doe", "age": 30, "adult": true}, "transformed": true}

# Merge JSON objects with conflict resolution
POST /api/utilities/json/merge
{
  "baseJson": {"name": "John", "age": 25, "city": "NYC"},
  "mergeJson": {"age": 30, "country": "USA"},
  "strategy": "OVERWRITE"
}
Response: {"result": {"name": "John", "age": 30, "city": "NYC", "country": "USA"}, "conflicts": ["age"]}
```

#### **File Utilities API**
```bash
# Base Path: /api/utilities/file

# Upload and process file
POST /api/utilities/file/upload
Content-Type: multipart/form-data
Form Data:
  file: [binary file data]
  processType: "DOCUMENT"
  validateSecurity: true
  extractMetadata: true

Response: {
  "fileId": "f47ac10b-58cc-4372-a567-0e02b2c3d479",
  "originalName": "document.pdf",
  "size": 1048576,
  "type": "application/pdf",
  "secure": true,
  "metadata": {
    "pages": 10,
    "author": "John Doe",
    "created": "2025-08-14T10:30:00Z"
  }
}

# Convert file format
POST /api/utilities/file/convert
{
  "fileId": "f47ac10b-58cc-4372-a567-0e02b2c3d479",
  "targetFormat": "DOCX",
  "options": {
    "quality": "HIGH",
    "preserveFormatting": true
  }
}
Response: {"convertedFileId": "a23bc45d-67ef-8901-b234-56c78d9e0123", "format": "DOCX", "size": 987654}

# Validate file security
POST /api/utilities/file/security-scan
{
  "fileId": "f47ac10b-58cc-4372-a567-0e02b2c3d479",
  "scanLevel": "COMPREHENSIVE"
}
Response: {"secure": true, "threats": [], "scanTime": "2025-08-14T10:35:00Z", "level": "COMPREHENSIVE"}
```

#### **Validation Utilities API**
```bash
# Base Path: /api/utilities/validation

# Validate complex business rules
POST /api/utilities/validation/business-rules
{
  "data": {
    "customerType": "PREMIUM",
    "orderAmount": 1500.00,
    "discountPercent": 15,
    "region": "US"
  },
  "rules": ["CUSTOMER_DISCOUNT_ELIGIBILITY", "REGION_PRICING", "ORDER_MINIMUM"]
}
Response: {
  "valid": true,
  "results": [
    {"rule": "CUSTOMER_DISCOUNT_ELIGIBILITY", "passed": true},
    {"rule": "REGION_PRICING", "passed": true}, 
    {"rule": "ORDER_MINIMUM", "passed": true}
  ]
}

# Cross-field validation
POST /api/utilities/validation/cross-field
{
  "data": {
    "startDate": "2025-08-14",
    "endDate": "2025-08-21",
    "eventType": "CONFERENCE",
    "capacity": 500
  },
  "constraints": [
    {"field": "endDate", "dependsOn": "startDate", "rule": "AFTER"},
    {"field": "capacity", "dependsOn": "eventType", "rule": "APPROPRIATE_SIZE"}
  ]
}
Response: {"valid": true, "violations": [], "score": 1.0}
```

### **🔍 Advanced Query Parameters**

```bash
# Pagination for list endpoints
GET /api/utilities/string/history?page=0&size=20&sort=createdAt,desc

# Filtering and search
GET /api/utilities/validation/rules?category=BUSINESS&active=true&search=customer

# Response format control
GET /api/utilities/datetime/timezones?format=compact&locale=en_US

# Cache control
GET /api/utilities/json/schemas?cache=false&refresh=true
```

### **📊 Health and Monitoring Endpoints**

```bash
# Application health
GET /actuator/health                    # Overall health status
GET /actuator/health/liveness          # Kubernetes liveness probe
GET /actuator/health/readiness         # Kubernetes readiness probe

# Metrics and monitoring
GET /actuator/prometheus               # Prometheus metrics
GET /actuator/metrics                  # Application metrics
GET /actuator/info                     # Application information

# Cache and performance
GET /actuator/caches                   # Cache statistics
GET /actuator/threaddump              # Thread dump for debugging
GET /actuator/heapdump                # Heap dump for memory analysis
```

---

## 🔧 **Configuration**

### **📋 Environment Variables**

#### **Database Configuration**
```bash
# PostgreSQL Database Settings
DB_HOST=localhost                      # Database host
DB_PORT=5432                          # Database port
DB_NAME=gogidix_utilities             # Database name
DB_USERNAME=postgres                   # Database username
DB_PASSWORD=postgres                   # Database password
DB_MAX_POOL_SIZE=20                   # Maximum connection pool size
DB_MIN_IDLE=5                         # Minimum idle connections
```

#### **Redis Cache Configuration**
```bash
# Redis Cache Settings
REDIS_HOST=localhost                   # Redis host
REDIS_PORT=6379                       # Redis port
REDIS_PASSWORD=                       # Redis password (optional)
REDIS_DATABASE=0                      # Redis database number
REDIS_TIMEOUT=10000                   # Connection timeout (ms)
REDIS_MAX_ACTIVE=20                   # Maximum active connections
REDIS_MAX_IDLE=10                     # Maximum idle connections
```

#### **Application Configuration**
```bash
# Service Configuration
SERVER_PORT=8707                      # Application port
SPRING_PROFILES_ACTIVE=dev            # Active Spring profile
MANAGEMENT_PORT=8707                  # Management endpoints port

# Security Configuration
SECURITY_JWT_SECRET=your-jwt-secret   # JWT signing secret
SECURITY_JWT_EXPIRATION=86400         # JWT expiration (seconds)
SECURITY_CORS_ORIGINS=*               # CORS allowed origins

# Utility Configuration
UTILITY_FILE_MAX_SIZE=100MB           # Maximum file upload size
UTILITY_CACHE_TTL=3600               # Default cache TTL (seconds)
UTILITY_ASYNC_POOL_SIZE=10           # Async processing pool size
```

#### **Monitoring Configuration**
```bash
# Observability Settings
MANAGEMENT_METRICS_EXPORT_PROMETHEUS_ENABLED=true
MANAGEMENT_TRACING_ENABLED=true
MANAGEMENT_ZIPKIN_TRACING_ENDPOINT=http://jaeger:9411
LOGGING_LEVEL_COM_GOGIDIX=INFO
```

### **⚙️ Spring Profiles**

#### **Development Profile (dev)**
```yaml
# application-dev.yml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/gogidix_utilities_dev
    username: postgres
    password: postgres
  jpa:
    hibernate:
      ddl-auto: create-drop
    show-sql: true
  data:
    redis:
      host: localhost
      port: 6379
      database: 0
      
logging:
  level:
    com.gogidix: DEBUG
    org.springframework.web: DEBUG
```

#### **Test Profile (test)**
```yaml
# application-test.yml
spring:
  datasource:
    url: jdbc:h2:mem:testdb
    driver-class-name: org.h2.Driver
    username: sa
    password: password
  jpa:
    hibernate:
      ddl-auto: create-drop
  data:
    redis:
      host: localhost
      port: 6379
      database: 1

# Test-specific utilities configuration
gogidix:
  utilities:
    file:
      max-file-size: 10MB
    validation:
      strict-mode: false
```

#### **Production Profile (prod)**
```yaml
# application-prod.yml
spring:
  datasource:
    url: jdbc:postgresql://${DB_HOST}:${DB_PORT}/${DB_NAME}
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}
    hikari:
      maximum-pool-size: 20
      minimum-idle: 5
  jpa:
    hibernate:
      ddl-auto: validate
    show-sql: false
  data:
    redis:
      cluster:
        nodes: ${REDIS_CLUSTER_NODES}
      password: ${REDIS_PASSWORD}

management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics,prometheus
```

### **🐳 Docker Configuration**

#### **Docker Compose (Development)**
```yaml
# docker-compose.yml
version: '3.8'
services:
  shared-utilities:
    build: .
    ports:
      - "8707:8707"
    environment:
      - SPRING_PROFILES_ACTIVE=docker
      - DB_HOST=postgres
      - REDIS_HOST=redis
    depends_on:
      - postgres
      - redis

  postgres:
    image: postgres:15-alpine
    environment:
      POSTGRES_DB: gogidix_utilities
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: postgres
    ports:
      - "5432:5432"
    volumes:
      - postgres_data:/var/lib/postgresql/data

  redis:
    image: redis:7-alpine
    ports:
      - "6379:6379"
    command: redis-server --appendonly yes
    volumes:
      - redis_data:/data

volumes:
  postgres_data:
  redis_data:
```

#### **Production Docker Configuration**
```dockerfile
# Multi-stage production Dockerfile
FROM maven:3.9-eclipse-temurin-17 AS builder
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine AS runtime
RUN addgroup -g 1001 appgroup && adduser -u 1001 -G appgroup -D appuser
USER appuser
WORKDIR /app

COPY --from=builder /app/target/*.jar app.jar
COPY --chown=appuser:appgroup docker/entrypoint.sh ./

EXPOSE 8707
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:8707/actuator/health/liveness || exit 1

ENTRYPOINT ["./entrypoint.sh"]
```

### **☸️ Kubernetes Configuration**

#### **ConfigMap**
```yaml
# k8s/configmap.yaml
apiVersion: v1
kind: ConfigMap
metadata:
  name: shared-utilities-config
  namespace: gogidix-infrastructure
data:
  SPRING_PROFILES_ACTIVE: "kubernetes"
  SERVER_PORT: "8707"
  MANAGEMENT_PORT: "8707"
  DB_HOST: "postgres-shared-utilities.gogidix-infrastructure.svc.cluster.local"
  DB_PORT: "5432"
  DB_NAME: "gogidix_utilities"
  REDIS_HOST: "redis-shared-utilities.gogidix-infrastructure.svc.cluster.local"
  REDIS_PORT: "6379"
  REDIS_DATABASE: "2"
  UTILITY_CACHE_TTL: "3600"
  UTILITY_FILE_MAX_SIZE: "100MB"
  JAVA_OPTS: "-Xmx512m -Xms256m -XX:+UseG1GC"
```

#### **Secrets**
```yaml
# k8s/secrets.yaml
apiVersion: v1
kind: Secret
metadata:
  name: shared-utilities-secrets
  namespace: gogidix-infrastructure
type: Opaque
data:
  db.username: cG9zdGdyZXM=       # postgres
  db.password: cG9zdGdyZXM=       # postgres
  redis.password: cmVkaXMxMjM=    # redis123
  security.jwt.secret: eW91ci1qd3Qtc2VjcmV0LWtleQ==  # your-jwt-secret-key
```

---

## 🏃‍♂️ **Development**

### **📁 Project Structure**

```ascii
shared-utilities/
├── 📁 src/
│   ├── 📁 main/
│   │   ├── 📁 java/
│   │   │   └── 📁 com/gogidix/infrastructure/sharedlibraries/sharedutilities/
│   │   │       ├── 📁 api/                    # API Layer (Controllers & DTOs)
│   │   │       │   ├── 📁 controller/
│   │   │       │   │   ├── UtilityController.java
│   │   │       │   │   ├── HealthController.java
│   │   │       │   │   └── MetricsController.java
│   │   │       │   └── 📁 dto/
│   │   │       │       ├── StringOperationDto.java
│   │   │       │       ├── DateTimeOperationDto.java
│   │   │       │       ├── JsonOperationDto.java
│   │   │       │       ├── FileOperationDto.java
│   │   │       │       └── ValidationOperationDto.java
│   │   │       ├── 📁 application/            # Application Layer (Services)
│   │   │       │   ├── StringUtilsService.java
│   │   │       │   ├── DateTimeUtilsService.java
│   │   │       │   ├── JsonUtilsService.java
│   │   │       │   ├── FileUtilsService.java
│   │   │       │   ├── ValidationUtilsService.java
│   │   │       │   ├── CacheService.java
│   │   │       │   ├── AsyncUtilsService.java
│   │   │       │   ├── NotificationService.java
│   │   │       │   └── EventUtilsService.java
│   │   │       ├── 📁 domain/                 # Domain Layer (Business Logic)
│   │   │       │   ├── 📁 model/
│   │   │       │   │   ├── UtilityType.java
│   │   │       │   │   ├── ProcessingRule.java
│   │   │       │   │   ├── CachePolicy.java
│   │   │       │   │   ├── ValidationRule.java
│   │   │       │   │   └── ProcessingEvent.java
│   │   │       │   └── 📁 port/
│   │   │       │       ├── UtilityRepository.java
│   │   │       │       ├── CachePort.java
│   │   │       │       ├── FileStoragePort.java
│   │   │       │       ├── NotificationPort.java
│   │   │       │       └── EventPort.java
│   │   │       └── 📁 infrastructure/         # Infrastructure Layer (Adapters)
│   │   │           ├── 📁 adapter/
│   │   │           │   ├── UtilityRepositoryImpl.java
│   │   │           │   ├── RedisCacheAdapter.java
│   │   │           │   ├── FileSystemAdapter.java
│   │   │           │   ├── EmailNotificationAdapter.java
│   │   │           │   └── KafkaEventAdapter.java
│   │   │           └── 📁 config/
│   │   │               ├── SecurityConfig.java
│   │   │               ├── CacheConfig.java
│   │   │               ├── AsyncConfig.java
│   │   │               └── MonitoringConfig.java
│   │   └── 📁 resources/
│   │       ├── application.yml
│   │       ├── application-dev.yml
│   │       ├── application-test.yml
│   │       ├── application-prod.yml
│   │       ├── application-docker.yml
│   │       ├── application-kubernetes.yml
│   │       └── 📁 db/migration/
│   │           └── V1__Create_utility_tables.sql
│   └── 📁 test/                              # Test Suite
│       ├── 📁 java/
│       │   └── 📁 com/gogidix/.../sharedutilities/
│       │       ├── 📁 api/
│       │       │   └── controller/
│       │       │       ├── UtilityControllerTest.java
│       │       │       └── HealthControllerTest.java
│       │       ├── 📁 application/
│       │       │   ├── StringUtilsServiceTest.java
│       │       │   ├── DateTimeUtilsServiceTest.java
│       │       │   ├── JsonUtilsServiceTest.java
│       │       │   ├── FileUtilsServiceTest.java
│       │       │   └── ValidationUtilsServiceTest.java
│       │       ├── 📁 integration/
│       │       │   ├── UtilityIntegrationTest.java
│       │       │   ├── CacheIntegrationTest.java
│       │       │   └── DatabaseIntegrationTest.java
│       │       ├── 📁 performance/
│       │       │   ├── StringUtilsPerformanceTest.java
│       │       │   └── CachePerformanceTest.java
│       │       └── 📁 security/
│       │           ├── SecurityIntegrationTest.java
│       │           └── InputValidationSecurityTest.java
│       └── 📁 resources/
│           ├── application-test.yml
│           ├── 📁 testdata/
│           │   ├── sample-documents/
│           │   ├── test-json-schemas/
│           │   └── validation-rules/
│           └── 📁 fixtures/
├── 📁 k8s/                                   # Kubernetes Manifests
│   ├── deployment.yaml
│   ├── service.yaml
│   ├── configmap.yaml
│   ├── secrets.yaml
│   └── service-account.yaml
├── 📁 docker/                                # Docker Configuration
│   ├── entrypoint.sh
│   ├── health-check.sh
│   └── pgadmin-servers.json
├── 📁 api-docs/                              # API Documentation
│   ├── openapi.yaml
│   ├── postman-collection.json
│   └── examples/
├── 📁 scripts/                               # Development Scripts
│   ├── build.sh
│   ├── test.sh
│   ├── deploy.sh
│   └── smoke-test.sh
├── 📁 docs/                                  # Documentation
│   ├── README.md
│   ├── ARCHITECTURE_DIAGRAM.md
│   ├── 📁 setup/
│   ├── 📁 operations/
│   └── 📁 architecture/
├── Dockerfile
├── docker-compose.yml
├── .gitlab-ci.yml
├── pom.xml
└── README.md
```

### **🛠️ Development Commands**

#### **Build & Test**
```bash
# Clean and compile
./mvnw clean compile

# Run unit tests
./mvnw test

# Run integration tests
./mvnw test -P integration-test

# Run all tests with coverage
./mvnw test jacoco:report

# Build JAR file
./mvnw clean package

# Build and skip tests
./mvnw clean package -DskipTests

# Run specific test class
./mvnw test -Dtest=StringUtilsServiceTest

# Run specific test method
./mvnw test -Dtest=StringUtilsServiceTest#testFormatText
```

#### **Development Server**
```bash
# Start with dev profile
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev

# Start with custom port
./mvnw spring-boot:run -Dspring-boot.run.arguments=--server.port=8708

# Start with debug mode
./mvnw spring-boot:run -Dspring-boot.run.jvmArguments="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005"

# Hot reload development (requires spring-boot-devtools)
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev -Dspring.devtools.restart.enabled=true
```

#### **Docker Development**
```bash
# Build development image
docker build -t shared-utilities:dev .

# Run with docker-compose
docker-compose up -d

# View logs
docker-compose logs -f shared-utilities

# Restart service
docker-compose restart shared-utilities

# Clean up
docker-compose down -v
```

#### **Code Quality & Analysis**
```bash
# Run OWASP dependency check
./mvnw org.owasp:dependency-check-maven:check

# Run SonarQube analysis (requires SonarQube server)
./mvnw sonar:sonar \
  -Dsonar.projectKey=gogidx-infrastructure_shared-utilities \
  -Dsonar.host.url=http://localhost:9000 \
  -Dsonar.login=your-sonar-token

# Generate site documentation
./mvnw site

# Verify code formatting (if using Spotless)
./mvnw spotless:check

# Apply code formatting
./mvnw spotless:apply
```

### **🧪 Testing Strategy**

#### **Unit Tests (Jest)**
```java
// Example: StringUtilsServiceTest.java
@ExtendWith(MockitoExtension.class)
class StringUtilsServiceTest {
    
    @Mock
    private CacheService cacheService;
    
    @InjectMocks
    private StringUtilsService stringUtilsService;
    
    @Test
    @DisplayName("Should format text to upper case")
    void testFormatTextUpperCase() {
        // Given
        String input = "hello world";
        String format = "UPPER_CASE";
        
        // When
        String result = stringUtilsService.formatText(input, format);
        
        // Then
        assertThat(result).isEqualTo("HELLO WORLD");
        verify(cacheService).get(any(String.class));
    }
    
    @ParameterizedTest
    @ValueSource(strings = {"", "   ", "\t\n"})
    @DisplayName("Should handle empty or whitespace strings")
    void testHandleEmptyStrings(String input) {
        // When
        String result = stringUtilsService.formatText(input, "UPPER_CASE");
        
        // Then
        assertThat(result).isEmpty();
    }
}
```

#### **Integration Tests (TestContainers)**
```java
// Example: UtilityIntegrationTest.java
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application-test.yml")
@Testcontainers
class UtilityIntegrationTest {
    
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");
    
    @Container
    static GenericContainer<?> redis = new GenericContainer<>("redis:7-alpine")
            .withExposedPorts(6379);
    
    @Autowired
    private TestRestTemplate restTemplate;
    
    @Test
    @DisplayName("Should process string operations end-to-end")
    void testStringOperationEndToEnd() {
        // Given
        StringOperationDto request = StringOperationDto.builder()
                .text("hello world")
                .format("CAMEL_CASE")
                .build();
        
        // When
        ResponseEntity<StringOperationResponseDto> response = restTemplate.postForEntity(
                "/api/utilities/string/format", 
                request, 
                StringOperationResponseDto.class
        );
        
        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getResult()).isEqualTo("helloWorld");
        assertThat(response.getBody().isProcessed()).isTrue();
    }
}
```

#### **Performance Tests**
```java
// Example: StringUtilsPerformanceTest.java
@ExtendWith(MockitoExtension.class)
class StringUtilsPerformanceTest {
    
    @Test
    @DisplayName("Should handle 10k operations within performance threshold")
    void testBulkStringProcessingPerformance() {
        // Given
        List<String> testData = generateTestStrings(10_000);
        StringUtilsService service = new StringUtilsService();
        
        // When
        long startTime = System.currentTimeMillis();
        List<String> results = testData.stream()
                .map(text -> service.formatText(text, "UPPER_CASE"))
                .collect(Collectors.toList());
        long duration = System.currentTimeMillis() - startTime;
        
        // Then
        assertThat(results).hasSize(10_000);
        assertThat(duration).isLessThan(1000); // Should complete within 1 second
    }
}
```

### **📊 Code Quality Standards**

#### **Test Coverage Requirements**
- **Unit Tests**: >80% line coverage
- **Integration Tests**: >70% feature coverage
- **End-to-End Tests**: >90% critical path coverage
- **Performance Tests**: All utility operations benchmarked

#### **Code Style Guidelines**
- Follow Google Java Style Guide
- Use meaningful variable and method names
- Write self-documenting code with minimal comments
- Implement proper exception handling
- Follow SOLID principles and clean architecture

#### **Security Guidelines**
- Validate all inputs at API boundaries
- Sanitize data before processing
- Use parameterized queries for database operations
- Implement proper authentication and authorization
- Log security events for audit trails

---

## 🔒 **Security**

### **🛡️ Security Architecture**

The Shared Utilities Service implements comprehensive security measures across multiple layers to protect against common vulnerabilities and ensure data integrity.

#### **Authentication & Authorization**
```yaml
Security Implementation:
  Authentication:
    - JWT token validation
    - OAuth2 integration support
    - Session management with Redis
    - Multi-factor authentication ready
    
  Authorization:
    - Role-Based Access Control (RBAC)
    - Method-level security annotations
    - Resource-based permissions
    - API rate limiting per user/role
```

#### **Data Protection**
```yaml
Data Security:
  Input Validation:
    - Schema-based validation
    - SQL injection prevention
    - XSS protection
    - Command injection prevention
    
  Data Encryption:
    - TLS 1.3 for data in transit
    - AES-256 for sensitive data at rest
    - Database field-level encryption
    - Redis cache encryption
```

### **🔐 Security Configuration**

#### **JWT Authentication**
```java
// Example: JWT Configuration
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/actuator/health/**").permitAll()
                .requestMatchers("/api/utilities/public/**").permitAll()
                .requestMatchers("/api/utilities/admin/**").hasRole("ADMIN")
                .requestMatchers("/api/utilities/**").hasAnyRole("USER", "ADMIN")
                .anyRequest().authenticated()
            )
            .oauth2ResourceServer().jwt();
        
        return http.build();
    }
}
```

#### **Input Validation**
```java
// Example: Secure input validation
@RestController
@Validated
public class UtilityController {
    
    @PostMapping("/api/utilities/string/format")
    public ResponseEntity<StringOperationResponseDto> formatString(
            @Valid @RequestBody StringOperationDto request,
            Authentication authentication) {
        
        // Validate user permissions
        if (!hasPermission(authentication, "STRING_FORMAT")) {
            throw new AccessDeniedException("Insufficient permissions");
        }
        
        // Sanitize input
        String sanitizedText = securityService.sanitizeInput(request.getText());
        
        // Process with validation
        String result = stringUtilsService.formatText(sanitizedText, request.getFormat());
        
        return ResponseEntity.ok(StringOperationResponseDto.builder()
                .result(result)
                .processed(true)
                .build());
    }
}
```

### **🔍 Security Features**

#### **Input Sanitization**
```bash
# Text sanitization levels
POST /api/utilities/string/sanitize
{
  "text": "<script>alert('xss')</script>User content",
  "level": "STRICT",
  "allowedTags": [],
  "preserveWhitespace": false
}

Response: {
  "result": "User content",
  "sanitized": true,
  "removedElements": ["script"],
  "securityLevel": "STRICT"
}
```

#### **File Security Scanning**
```bash
# Comprehensive file security validation
POST /api/utilities/file/security-scan
{
  "fileId": "f47ac10b-58cc-4372-a567-0e02b2c3d479",
  "scanLevel": "COMPREHENSIVE",
  "checkMalware": true,
  "validateContent": true
}

Response: {
  "secure": true,
  "threats": [],
  "malwareDetected": false,
  "contentValid": true,
  "scanDuration": "2.3s",
  "timestamp": "2025-08-14T10:30:00Z"
}
```

#### **Audit Logging**
```yaml
# Security event logging configuration
logging:
  level:
    com.gogidix.security: DEBUG
  pattern:
    file: "%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level [%X{traceId},%X{spanId}] %logger{36} - %msg%n"
  
management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics,auditevents
```

### **🚨 Security Monitoring**

#### **Security Metrics**
```bash
# Security-related Prometheus metrics
GET /actuator/prometheus

# Sample metrics:
security_authentication_attempts_total{status="success"} 1250
security_authentication_attempts_total{status="failure"} 45
security_authorization_denied_total{endpoint="/api/utilities/admin"} 12
security_input_validation_failures_total{type="xss"} 8
security_file_scan_threats_total{type="malware"} 0
```

#### **Threat Detection**
```yaml
Security Monitoring:
  Real-time Alerts:
    - Failed authentication attempts (>10/min)
    - Authorization violations
    - Input validation failures
    - Suspicious file uploads
    - Rate limiting triggers
    
  Log Analysis:
    - Security event correlation
    - Anomaly detection
    - Threat intelligence integration
    - Compliance reporting
```

### **📋 Security Checklist**

```yaml
✅ Security Implementation Checklist:

Authentication & Authorization:
  ✅ JWT token validation implemented
  ✅ OAuth2 integration configured
  ✅ Role-based access control (RBAC)
  ✅ Method-level security annotations
  ✅ Session management with Redis
  
Input Validation:
  ✅ Schema-based request validation
  ✅ SQL injection prevention
  ✅ XSS protection with sanitization
  ✅ Command injection prevention
  ✅ File upload security scanning
  
Data Protection:
  ✅ TLS 1.3 encryption in transit
  ✅ AES-256 encryption at rest
  ✅ Database field-level encryption
  ✅ Redis cache encryption
  ✅ Secure key management with Vault
  
Monitoring & Compliance:
  ✅ Comprehensive audit logging
  ✅ Security metrics collection
  ✅ Real-time threat detection
  ✅ Compliance reporting (GDPR, CCPA)
  ✅ Vulnerability scanning (OWASP, Trivy)
```

---

## 🚀 **Deployment**

### **☸️ Kubernetes Production Deployment**

#### **Deployment Manifest**
```yaml
# k8s/deployment.yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: shared-utilities
  namespace: gogidix-infrastructure
  labels:
    app: shared-utilities
    component: utilities
    tier: infrastructure
spec:
  replicas: 3
  selector:
    matchLabels:
      app: shared-utilities
  template:
    metadata:
      labels:
        app: shared-utilities
        component: utilities
        tier: infrastructure
    spec:
      serviceAccountName: shared-utilities
      containers:
      - name: shared-utilities
        image: registry.gitlab.com/gogidix/shared-libraries/shared-utilities:1.0.0
        ports:
        - containerPort: 8707
          name: http
        env:
        - name: SPRING_PROFILES_ACTIVE
          value: "kubernetes"
        envFrom:
        - configMapRef:
            name: shared-utilities-config
        - secretRef:
            name: shared-utilities-secrets
        resources:
          requests:
            memory: "256Mi"
            cpu: "100m"
          limits:
            memory: "512Mi"
            cpu: "500m"
        livenessProbe:
          httpGet:
            path: /actuator/health/liveness
            port: http
          initialDelaySeconds: 60
          periodSeconds: 30
          timeoutSeconds: 10
          failureThreshold: 3
        readinessProbe:
          httpGet:
            path: /actuator/health/readiness
            port: http
          initialDelaySeconds: 30
          periodSeconds: 10
          timeoutSeconds: 5
          failureThreshold: 3
        startupProbe:
          httpGet:
            path: /actuator/health/liveness
            port: http
          initialDelaySeconds: 30
          periodSeconds: 10
          timeoutSeconds: 5
          failureThreshold: 30
        volumeMounts:
        - name: temp-storage
          mountPath: /tmp/utilities
        - name: logs
          mountPath: /app/logs
      volumes:
      - name: temp-storage
        emptyDir:
          sizeLimit: 1Gi
      - name: logs
        emptyDir: {}
```

#### **Service Configuration**
```yaml
# k8s/service.yaml
apiVersion: v1
kind: Service
metadata:
  name: shared-utilities
  namespace: gogidix-infrastructure
  labels:
    app: shared-utilities
    component: utilities
    tier: infrastructure
spec:
  selector:
    app: shared-utilities
  ports:
  - name: http
    port: 8707
    targetPort: 8707
    protocol: TCP
  type: ClusterIP
---
apiVersion: v1
kind: Service
metadata:
  name: shared-utilities-headless
  namespace: gogidix-infrastructure
spec:
  selector:
    app: shared-utilities
  ports:
  - name: http
    port: 8707
    targetPort: 8707
  clusterIP: None
```

#### **Horizontal Pod Autoscaler**
```yaml
# k8s/hpa.yaml
apiVersion: autoscaling/v2
kind: HorizontalPodAutoscaler
metadata:
  name: shared-utilities-hpa
  namespace: gogidix-infrastructure
spec:
  scaleTargetRef:
    apiVersion: apps/v1
    kind: Deployment
    name: shared-utilities
  minReplicas: 3
  maxReplicas: 10
  metrics:
  - type: Resource
    resource:
      name: cpu
      target:
        type: Utilization
        averageUtilization: 70
  - type: Resource
    resource:
      name: memory
      target:
        type: Utilization
        averageUtilization: 70
  - type: Pods
    pods:
      metric:
        name: http_requests_per_second
      target:
        type: AverageValue
        averageValue: "1k"
```

### **🐳 Docker Production Setup**

#### **Multi-Environment Docker Compose**
```yaml
# docker-compose.prod.yml
version: '3.8'
services:
  shared-utilities:
    image: registry.gitlab.com/gogidix/shared-libraries/shared-utilities:1.0.0
    restart: unless-stopped
    environment:
      - SPRING_PROFILES_ACTIVE=prod
      - JAVA_OPTS=-Xmx512m -Xms256m -XX:+UseG1GC
    env_file:
      - .env.prod
    ports:
      - "8707:8707"
    networks:
      - gogidix-network
    depends_on:
      postgres:
        condition: service_healthy
      redis:
        condition: service_healthy
    deploy:
      replicas: 3
      resources:
        limits:
          memory: 512M
          cpus: '0.5'
        reservations:
          memory: 256M
          cpus: '0.1'

  postgres:
    image: postgres:15-alpine
    restart: unless-stopped
    environment:
      POSTGRES_DB: ${DB_NAME}
      POSTGRES_USER: ${DB_USERNAME}
      POSTGRES_PASSWORD: ${DB_PASSWORD}
    volumes:
      - postgres_data:/var/lib/postgresql/data
      - ./postgres/postgresql.conf:/etc/postgresql/postgresql.conf
    networks:
      - gogidx-network
    healthcheck:
      test: ["CMD-SHELL", "pg_isready -U ${DB_USERNAME}"]
      interval: 30s
      timeout: 10s
      retries: 5
      start_period: 60s

  redis:
    image: redis:7-alpine
    restart: unless-stopped
    command: redis-server /usr/local/etc/redis/redis.conf
    volumes:
      - redis_data:/data
      - ./redis/redis.conf:/usr/local/etc/redis/redis.conf
    networks:
      - gogidix-network
    healthcheck:
      test: ["CMD", "redis-cli", "ping"]
      interval: 30s
      timeout: 10s
      retries: 5
      start_period: 30s

networks:
  gogidix-network:
    external: true

volumes:
  postgres_data:
  redis_data:
```

### **🚀 Deployment Scripts**

#### **Automated Deployment Script**
```bash
#!/bin/bash
# scripts/deploy.sh

set -e

ENVIRONMENT=${1:-dev}
NAMESPACE="gogidix-infrastructure"
VERSION=${2:-latest}

echo "🚀 Deploying Shared Utilities Service"
echo "Environment: $ENVIRONMENT"
echo "Version: $VERSION"
echo "Namespace: $NAMESPACE"

# Validate environment
if [[ ! "$ENVIRONMENT" =~ ^(dev|staging|prod)$ ]]; then
    echo "❌ Invalid environment. Must be dev, staging, or prod"
    exit 1
fi

# Create namespace if it doesn't exist
kubectl create namespace $NAMESPACE --dry-run=client -o yaml | kubectl apply -f -

# Apply ConfigMaps and Secrets
echo "📋 Applying configuration..."
envsubst < k8s/configmap.yaml | kubectl apply -f -
envsubst < k8s/secrets.yaml | kubectl apply -f -

# Apply service account and RBAC
echo "🔐 Setting up service account..."
kubectl apply -f k8s/service-account.yaml -n $NAMESPACE

# Apply services
echo "🌐 Creating services..."
kubectl apply -f k8s/service.yaml -n $NAMESPACE

# Update deployment with new image version
echo "🔄 Updating deployment..."
sed "s/:latest/:$VERSION/g" k8s/deployment.yaml | kubectl apply -f - -n $NAMESPACE

# Apply HPA
echo "📈 Setting up auto-scaling..."
kubectl apply -f k8s/hpa.yaml -n $NAMESPACE

# Wait for deployment to be ready
echo "⏳ Waiting for deployment to be ready..."
kubectl rollout status deployment/shared-utilities -n $NAMESPACE --timeout=300s

# Verify deployment
echo "✅ Verifying deployment..."
kubectl get pods -l app=shared-utilities -n $NAMESPACE
kubectl get svc -l app=shared-utilities -n $NAMESPACE

# Run health checks
echo "🏥 Running health checks..."
sleep 30
kubectl run temp-pod --rm -i --tty --restart=Never --image=curlimages/curl -- \
    curl -f http://shared-utilities.${NAMESPACE}.svc.cluster.local:8707/actuator/health

echo "🎉 Deployment completed successfully!"
```

#### **Rollback Script**
```bash
#!/bin/bash
# scripts/rollback.sh

NAMESPACE="gogidx-infrastructure"
REVISION=${1}

echo "🔄 Rolling back Shared Utilities Service"

if [ -z "$REVISION" ]; then
    echo "📋 Available rollout history:"
    kubectl rollout history deployment/shared-utilities -n $NAMESPACE
    echo "Usage: $0 <revision-number>"
    exit 1
fi

echo "⏪ Rolling back to revision $REVISION..."
kubectl rollout undo deployment/shared-utilities --to-revision=$REVISION -n $NAMESPACE

echo "⏳ Waiting for rollback to complete..."
kubectl rollout status deployment/shared-utilities -n $NAMESPACE --timeout=300s

echo "✅ Rollback completed successfully!"
kubectl get pods -l app=shared-utilities -n $NAMESPACE
```

### **🔍 Production Validation**

#### **Health Check Script**
```bash
#!/bin/bash
# docker/health-check.sh

set -e

HEALTH_ENDPOINT="${HEALTH_ENDPOINT:-http://localhost:8707/actuator/health}"
MAX_ATTEMPTS="${MAX_ATTEMPTS:-30}"
SLEEP_INTERVAL="${SLEEP_INTERVAL:-5}"

echo "🏥 Starting health check validation..."
echo "Endpoint: $HEALTH_ENDPOINT"
echo "Max attempts: $MAX_ATTEMPTS"
echo "Sleep interval: ${SLEEP_INTERVAL}s"

attempt=1
while [ $attempt -le $MAX_ATTEMPTS ]; do
    echo "Attempt $attempt/$MAX_ATTEMPTS: Checking application health..."
    
    if curl -f -s "$HEALTH_ENDPOINT" > /dev/null 2>&1; then
        echo "✅ Health check passed!"
        
        # Additional utility endpoint validation
        if curl -f -s "http://localhost:8707/api/utilities/health" > /dev/null 2>&1; then
            echo "✅ Utility endpoints accessible!"
            exit 0
        else
            echo "⚠️ Utility endpoints not ready, continuing..."
        fi
    else
        echo "❌ Health check failed (attempt $attempt/$MAX_ATTEMPTS)"
    fi
    
    if [ $attempt -eq $MAX_ATTEMPTS ]; then
        echo "❌ Health check failed after $MAX_ATTEMPTS attempts"
        echo "Fetching application logs for debugging..."
        kubectl logs -l app=shared-utilities --tail=50 2>/dev/null || echo "Could not fetch logs"
        exit 1
    fi
    
    sleep $SLEEP_INTERVAL
    ((attempt++))
done
```

### **📊 Deployment Monitoring**

#### **Deployment Metrics Dashboard**
```bash
# View deployment status
kubectl get deployments -n gogidix-infrastructure
kubectl get pods -l app=shared-utilities -n gogidix-infrastructure
kubectl get hpa -n gogidix-infrastructure

# Check resource usage
kubectl top pods -l app=shared-utilities -n gogidix-infrastructure
kubectl top nodes

# View application logs
kubectl logs -f deployment/shared-utilities -n gogidix-infrastructure

# Check service connectivity
kubectl exec -it deployment/shared-utilities -n gogidix-infrastructure -- \
    curl http://localhost:8707/actuator/health
```

#### **Performance Monitoring**
```bash
# Monitor key metrics
curl -s http://shared-utilities:8707/actuator/prometheus | grep -E "(http_requests_total|jvm_memory_used_bytes|cache_hits_total)"

# Database connection monitoring
kubectl exec -it postgres-pod -- psql -U postgres -d gogidix_utilities \
    -c "SELECT count(*) as active_connections FROM pg_stat_activity WHERE state = 'active';"

# Redis cache monitoring
kubectl exec -it redis-pod -- redis-cli INFO memory
kubectl exec -it redis-pod -- redis-cli INFO stats
```

---

## 📊 **Monitoring**

### **📈 Observability Stack**

The Shared Utilities Service provides comprehensive monitoring and observability through industry-standard tools and custom dashboards.

#### **Monitoring Architecture**
```ascii
┌─────────────────────────────────────────────────────────────────────────┐
│                      MONITORING & OBSERVABILITY                         │
└─────────────────────────────────────────────────────────────────────────┘

                              ┌─────────────────┐
                              │   GRAFANA       │
                              │   DASHBOARDS    │ ──── Executive Views
                              │ (Visualization) │      Developer Views
                              └─────────┬───────┘      Operations Views
                                        │
                                        ▼
                 ┌─────────────────────────────────────────┐
                 │            PROMETHEUS                   │
                 │         (Metrics Collection)            │ ──── Business Metrics
                 │  ┌─────────────┐  ┌─────────────┐       │      Technical Metrics
                 │  │ Application │  │Infrastructure│     │      Security Metrics
                 │  │   Metrics   │  │   Metrics     │     │      Performance Metrics
                 │  └─────────────┘  └─────────────┘       │
                 └─────────┬─────────────┬─────────────────┘
                           │             │
                           ▼             ▼
                 ┌─────────────────────────────────────────┐
                 │              JAEGER TRACING             │ ──── Request Tracing
                 │         (Distributed Tracing)           │      Service Mapping
                 │  ┌─────────────┐  ┌─────────────┐       │      Performance Analysis
                 │  │   Request   │  │   Service   │       │      Error Correlation
                 │  │   Tracing   │  │   Mapping   │       │
                 │  └─────────────┘  └─────────────┘       │
                 └─────────┬─────────────┬─────────────────┘
                           │             │
                           ▼             ▼
                 ┌─────────────────────────────────────────┐
                 │             ELK STACK                   │ ──── Centralized Logging
                 │          (Logging & Analysis)           │      Log Analysis
                 │  ┌─────────────┐  ┌─────────────┐       │      Error Tracking
                 │  │  Logstash   │  │    Kibana   │       │      Audit Trails
                 │  │ Processing  │  │    Analysis │       │
                 │  └─────────────┘  └─────────────┘       │
                 │  ┌─────────────┐                        │
                 │  │Elasticsearch│                        │
                 │  │   Storage   │                        │
                 │  └─────────────┘                        │
                 └─────────────────────────────────────────┘
```

### **📊 Key Metrics & Dashboards**

#### **Application Metrics**
```yaml
# Prometheus Metrics Configuration
management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics,prometheus
  metrics:
    export:
      prometheus:
        enabled: true
    distribution:
      percentiles-histogram:
        http.server.requests: true
      percentiles:
        http.server.requests: 0.50, 0.90, 0.95, 0.99
    tags:
      service: shared-utilities
      version: 1.0.0
      environment: production
```

#### **Business Metrics**
```bash
# Utility Operation Metrics
utility_operations_total{type="string",operation="format"} 125000
utility_operations_total{type="datetime",operation="convert"} 89000
utility_operations_total{type="json",operation="validate"} 156000
utility_operations_total{type="file",operation="process"} 45000
utility_operations_total{type="validation",operation="check"} 234000

# Performance Metrics
utility_operation_duration_seconds{type="string",quantile="0.5"} 0.008
utility_operation_duration_seconds{type="string",quantile="0.95"} 0.018
utility_operation_duration_seconds{type="datetime",quantile="0.95"} 0.012
utility_operation_duration_seconds{type="json",quantile="0.95"} 0.025

# Cache Performance Metrics
cache_hits_total{cache="string-utils",tier="l1"} 45000
cache_hits_total{cache="string-utils",tier="l2"} 8000
cache_misses_total{cache="string-utils",tier="l1"} 2000
cache_hit_ratio{cache="string-utils"} 0.956

# Error Metrics
utility_errors_total{type="validation",error="invalid_input"} 125
utility_errors_total{type="file",error="size_exceeded"} 45
utility_errors_total{type="security",error="malicious_content"} 8
```

#### **Infrastructure Metrics**
```bash
# JVM Metrics
jvm_memory_used_bytes{area="heap"} 402653184
jvm_memory_max_bytes{area="heap"} 536870912
jvm_gc_collection_seconds_total{gc="G1 Young Generation"} 12.45
jvm_threads_current 45
jvm_threads_daemon 28

# Database Metrics
hikaricp_connections_active{pool="shared-utilities"} 8
hikaricp_connections_idle{pool="shared-utilities"} 12
hikaricp_connections_pending{pool="shared-utilities"} 0
hikaricp_connections_timeout_total 0

# Redis Metrics
redis_connected_clients 15
redis_used_memory_bytes 67108864
redis_keyspace_hits_total 125000
redis_keyspace_misses_total 5000
redis_commands_processed_total 130000
```

### **📱 Grafana Dashboards**

#### **Executive Dashboard**
```json
{
  "dashboard": {
    "title": "Shared Utilities - Executive Overview",
    "panels": [
      {
        "title": "Service Health Overview",
        "type": "stat",
        "targets": [
          {
            "expr": "up{job=\"shared-utilities\"}",
            "legendFormat": "Service Availability"
          }
        ]
      },
      {
        "title": "Daily Operation Volume",
        "type": "graph",
        "targets": [
          {
            "expr": "increase(utility_operations_total[24h])",
            "legendFormat": "{{type}} operations"
          }
        ]
      },
      {
        "title": "Response Time Trends",
        "type": "graph",
        "targets": [
          {
            "expr": "histogram_quantile(0.95, rate(utility_operation_duration_seconds_bucket[5m]))",
            "legendFormat": "95th Percentile"
          }
        ]
      },
      {
        "title": "Error Rate",
        "type": "singlestat",
        "targets": [
          {
            "expr": "rate(utility_errors_total[5m]) / rate(utility_operations_total[5m]) * 100",
            "legendFormat": "Error Rate %"
          }
        ]
      }
    ]
  }
}
```

#### **Operations Dashboard**
```json
{
  "dashboard": {
    "title": "Shared Utilities - Operations",
    "panels": [
      {
        "title": "Pod Status",
        "type": "table",
        "targets": [
          {
            "expr": "kube_pod_info{pod=~\"shared-utilities-.*\"}",
            "format": "table"
          }
        ]
      },
      {
        "title": "Resource Utilization",
        "type": "graph",
        "targets": [
          {
            "expr": "rate(container_cpu_usage_seconds_total{pod=~\"shared-utilities-.*\"}[5m]) * 100",
            "legendFormat": "CPU Usage %"
          },
          {
            "expr": "container_memory_usage_bytes{pod=~\"shared-utilities-.*\"} / container_spec_memory_limit_bytes * 100",
            "legendFormat": "Memory Usage %"
          }
        ]
      },
      {
        "title": "Cache Performance",
        "type": "graph",
        "targets": [
          {
            "expr": "rate(cache_hits_total[5m])",
            "legendFormat": "Cache Hits/sec"
          },
          {
            "expr": "rate(cache_misses_total[5m])",
            "legendFormat": "Cache Misses/sec"
          }
        ]
      }
    ]
  }
}
```

#### **Developer Dashboard**
```json
{
  "dashboard": {
    "title": "Shared Utilities - Development",
    "panels": [
      {
        "title": "API Endpoint Performance",
        "type": "heatmap",
        "targets": [
          {
            "expr": "rate(http_server_requests_seconds_bucket{uri=~\"/api/utilities/.*\"}[5m])",
            "legendFormat": "{{uri}} {{method}}"
          }
        ]
      },
      {
        "title": "Error Details",
        "type": "table",
        "targets": [
          {
            "expr": "increase(utility_errors_total[1h])",
            "format": "table"
          }
        ]
      },
      {
        "title": "Database Query Performance",
        "type": "graph",
        "targets": [
          {
            "expr": "histogram_quantile(0.95, rate(spring_data_repository_invocations_seconds_bucket[5m]))",
            "legendFormat": "95th Percentile DB Query Time"
          }
        ]
      }
    ]
  }
}
```

### **🔔 Alerting Configuration**

#### **Critical Alerts**
```yaml
# prometheus-alerts.yml
groups:
- name: shared-utilities.critical
  rules:
  - alert: SharedUtilitiesServiceDown
    expr: up{job="shared-utilities"} == 0
    for: 1m
    labels:
      severity: critical
      service: shared-utilities
    annotations:
      summary: "Shared Utilities Service is down"
      description: "Shared Utilities Service has been down for more than 1 minute."
      
  - alert: SharedUtilitiesHighErrorRate
    expr: rate(utility_errors_total[5m]) / rate(utility_operations_total[5m]) > 0.05
    for: 2m
    labels:
      severity: critical
      service: shared-utilities
    annotations:
      summary: "High error rate in Shared Utilities Service"
      description: "Error rate is {{ $value | humanizePercentage }} over the last 5 minutes."
      
  - alert: SharedUtilitiesResponseTimeHigh
    expr: histogram_quantile(0.95, rate(utility_operation_duration_seconds_bucket[5m])) > 0.1
    for: 5m
    labels:
      severity: warning
      service: shared-utilities
    annotations:
      summary: "High response time in Shared Utilities Service"
      description: "95th percentile response time is {{ $value }}s over the last 5 minutes."
```

#### **Performance Alerts**
```yaml
- name: shared-utilities.performance
  rules:
  - alert: SharedUtilitiesCacheHitRateLow
    expr: cache_hit_ratio < 0.8
    for: 10m
    labels:
      severity: warning
      service: shared-utilities
    annotations:
      summary: "Low cache hit rate"
      description: "Cache hit rate is {{ $value | humanizePercentage }} for cache {{ $labels.cache }}."
      
  - alert: SharedUtilitiesMemoryUsageHigh
    expr: jvm_memory_used_bytes{area="heap"} / jvm_memory_max_bytes{area="heap"} > 0.9
    for: 5m
    labels:
      severity: warning
      service: shared-utilities
    annotations:
      summary: "High memory usage"
      description: "JVM heap memory usage is {{ $value | humanizePercentage }}."
```

### **📋 Monitoring Checklist**

```yaml
✅ Monitoring Implementation:

Metrics Collection:
  ✅ Prometheus metrics enabled
  ✅ Custom business metrics implemented
  ✅ JVM and application metrics
  ✅ Database and cache metrics
  ✅ Security and audit metrics
  
Visualization:
  ✅ Grafana dashboards configured
  ✅ Executive, operations, and developer views
  ✅ Real-time and historical data
  ✅ Mobile-responsive dashboards
  
Alerting:
  ✅ Critical alerts configured
  ✅ Performance threshold alerts
  ✅ Multi-channel notifications
  ✅ Alert escalation procedures
  
Logging:
  ✅ Structured JSON logging
  ✅ ELK stack integration
  ✅ Log correlation with traces
  ✅ Security event logging
  
Tracing:
  ✅ Distributed tracing enabled
  ✅ Service dependency mapping
  ✅ Performance bottleneck identification
  ✅ Error correlation across services
```

---

## 🤝 **Contributing**

### **👥 Development Team**

**Core Contributors:**
- **Lead Developer**: Infrastructure Team
- **Architecture Review**: Senior Architecture Team  
- **Security Review**: Security Engineering Team
- **DevOps Support**: Platform Engineering Team

### **🔄 Contribution Workflow**

#### **1. Development Setup**
```bash
# Fork the repository
git clone https://gitlab.com/your-username/shared-utilities.git
cd shared-utilities

# Create feature branch
git checkout -b feature/your-feature-name

# Set up development environment
./scripts/setup-dev-environment.sh
```

#### **2. Code Standards**
```yaml
Code Quality Requirements:
  - Follow Google Java Style Guide
  - Write comprehensive unit tests (>80% coverage)
  - Include integration tests for new features
  - Update documentation for API changes
  - Security review for sensitive changes
  
Commit Standards:
  - Use conventional commit format
  - Include issue/ticket references
  - Sign commits with GPG key
  - Write clear, descriptive messages
```

#### **3. Pull Request Process**
```bash
# Before submitting PR
./mvnw clean test                    # Run all tests
./mvnw verify                       # Run quality checks
./scripts/security-scan.sh          # Run security scan

# Submit merge request
git push origin feature/your-feature-name
# Create MR in GitLab with proper template
```

### **🧪 Testing Requirements**

```yaml
Testing Standards:
  Unit Tests:
    - Coverage: >80% line coverage
    - Framework: JUnit 5, Mockito
    - Naming: descriptive test method names
    - Documentation: clear test descriptions
    
  Integration Tests:
    - TestContainers for database/cache testing
    - End-to-end API testing
    - Performance regression testing
    - Security vulnerability testing
    
  Documentation:
    - Update API documentation
    - Include code examples
    - Update architecture diagrams
    - Review user guides
```

### **🔒 Security Guidelines**

```yaml
Security Review Process:
  Code Review:
    - Input validation review
    - Authentication/authorization checks
    - Data sanitization verification
    - SQL injection prevention
    
  Dependency Management:
    - OWASP dependency check
    - Regular security updates
    - Vulnerability assessment
    - License compliance check
    
  Secrets Management:
    - No secrets in code
    - Environment variable usage
    - Vault integration
    - Access control policies
```

---

## 📞 **Support**

### **🆘 Getting Help**

#### **Documentation Resources**
- **Service Documentation**: [README.md](README.md)
- **Architecture Guide**: [ARCHITECTURE_DIAGRAM.md](ARCHITECTURE_DIAGRAM.md)
- **API Documentation**: [Swagger UI](http://localhost:8707/swagger-ui.html)
- **Operations Guide**: [docs/operations/README.md](docs/operations/README.md)

#### **Development Support**
- **GitLab Issues**: [Create Issue](https://gitlab.com/gogidix/shared-libraries/shared-utilities/-/issues)
- **Development Chat**: #shared-utilities-dev (Slack)
- **Code Reviews**: Merge Request process
- **Architecture Discussions**: #architecture (Slack)

#### **Production Support**
- **Operations Team**: ops-team@gogidix.com
- **On-Call Support**: +1-555-GOGIDIX (24/7)
- **Incident Management**: [PagerDuty](https://gogidx.pagerduty.com)
- **Status Page**: [status.gogidix.com](https://status.gogidx.com)

### **🚨 Incident Response**

#### **Severity Levels**
```yaml
Severity Classification:
  CRITICAL (P0):
    - Service completely unavailable
    - Data loss or corruption
    - Security breach
    - Response: <15 minutes
    
  HIGH (P1):
    - Major functionality impaired
    - Performance severely degraded
    - Multiple users affected
    - Response: <1 hour
    
  MEDIUM (P2):
    - Minor functionality issues
    - Performance slightly degraded
    - Limited user impact
    - Response: <4 hours
    
  LOW (P3):
    - Feature requests
    - Documentation updates
    - Nice-to-have improvements
    - Response: <24 hours
```

#### **Escalation Process**
```bash
# Emergency Contact (24/7)
📞 On-Call Engineer: +1-555-GOGIDX-1
📧 ops-urgent@gogidix.com
💬 #incident-response (Slack)

# Business Hours Contact
📧 shared-utilities-team@gogidix.com
💬 #shared-utilities-support (Slack)
🎫 GitLab Issues (non-urgent)
```

### **📊 Service Level Agreements (SLAs)**

```yaml
Production SLAs:
  Availability:
    - Target: 99.9% uptime
    - Measurement: Monthly rolling average
    - Exclusions: Planned maintenance windows
    
  Performance:
    - API Response Time (P95): <20ms
    - API Response Time (P99): <100ms
    - Cache Hit Ratio: >95%
    
  Support Response:
    - Critical Issues: <15 minutes
    - High Priority: <1 hour  
    - Medium Priority: <4 hours
    - Low Priority: <24 hours
    
  Data Recovery:
    - Recovery Point Objective (RPO): <1 hour
    - Recovery Time Objective (RTO): <4 hours
    - Backup Frequency: Every 6 hours
    - Backup Retention: 30 days
```

### **📋 Troubleshooting Guides**

#### **Common Issues**

**Service Won't Start**
```bash
# Check logs
kubectl logs -f deployment/shared-utilities -n gogidx-infrastructure

# Common causes:
1. Database connection failure
   - Verify DB_HOST and DB_PORT environment variables
   - Check database credentials in secrets
   - Test database connectivity: kubectl exec -it postgres-pod -- pg_isready

2. Redis connection failure
   - Verify REDIS_HOST and REDIS_PORT environment variables
   - Check Redis service status: kubectl get svc redis
   - Test Redis connectivity: kubectl exec -it redis-pod -- redis-cli ping

3. Configuration issues
   - Verify ConfigMap and Secret mounting
   - Check environment variable values
   - Validate Spring profiles configuration
```

**High Response Times**
```bash
# Monitor performance metrics
curl http://shared-utilities:8707/actuator/prometheus | grep duration

# Common causes:
1. Database query performance
   - Check slow query logs
   - Monitor connection pool utilization
   - Review database index usage

2. Cache performance
   - Monitor cache hit rates
   - Check Redis memory usage
   - Review cache TTL settings

3. Resource constraints
   - Monitor CPU and memory usage
   - Check pod resource limits
   - Review auto-scaling configuration
```

**High Error Rates**
```bash
# Check error metrics
curl http://shared-utilities:8707/actuator/metrics/utility.errors.total

# Common causes:
1. Input validation failures
   - Review request validation logs
   - Check client request formats
   - Monitor security filtering

2. External service failures
   - Check downstream service health
   - Monitor circuit breaker status
   - Review timeout configurations

3. Resource exhaustion
   - Monitor database connection pool
   - Check memory heap usage
   - Review thread pool utilization
```

---

## 📄 **License & Legal**

### **📜 License Information**

```
Copyright (c) 2025 GOGIDIX Technologies Limited
All rights reserved.

This software and associated documentation files (the "Software") are
proprietary to GOGIDIX Technologies Limited and are protected by copyright
and other intellectual property laws.

Unauthorized copying, distribution, or modification of this Software,
via any medium, is strictly prohibited without the express written
permission of GOGIDX Technologies Limited.

For licensing inquiries, contact: legal@gogidix.com
```

### **🔒 Third-Party Dependencies**

**Major Open Source Components:**
- **Spring Boot**: Apache License 2.0
- **PostgreSQL**: PostgreSQL License
- **Redis**: BSD 3-Clause License
- **Apache Commons**: Apache License 2.0
- **Jackson**: Apache License 2.0

**Full dependency report**: `./mvnw project-info-reports:dependencies`

### **🏢 Enterprise Information**

**Company**: GOGIDX Technologies Limited  
**Service**: Shared Utilities Library  
**Version**: 1.0.0  
**Build Date**: August 14, 2025  
**Support**: enterprise-support@gogidx.com  

---

## 🏷️ **Version Information**

**Current Version**: 1.0.0  
**Release Date**: August 14, 2025  
**Git Commit**: [Latest Commit SHA]  
**Build ID**: [CI/CD Build Number]  

**Version History:**
- v1.0.0 - Initial production release with complete feature set
- v0.9.0 - Release candidate with performance optimizations
- v0.8.0 - Beta release with security enhancements
- v0.7.0 - Alpha release with core functionality

---

**📅 Document Updated**: August 14, 2025  
**📖 Documentation Version**: 1.0.0  
**✅ Status**: Production Ready  
**🔄 Next Review**: September 14, 2025

---