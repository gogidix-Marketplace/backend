# GOGIDIX Shared Exceptions Service

## Enterprise Exception Management for Distributed Systems

[![Build Status](https://gitlab.com/gogidix/shared-libraries/shared-exceptions/badges/main/pipeline.svg)](https://gitlab.com/gogidix/shared-libraries/shared-exceptions/-/pipelines)
[![Coverage Report](https://gitlab.com/gogidix/shared-libraries/shared-exceptions/badges/main/coverage.svg)](https://gitlab.com/gogidix/shared-libraries/shared-exceptions/-/jobs)
[![Security Scan](https://img.shields.io/badge/security-trivy%20%7C%20snyk-brightgreen)](https://gitlab.com/gogidix/shared-libraries/shared-exceptions/-/security/vulnerability_report)
[![Version](https://img.shields.io/badge/version-1.0.0-blue.svg)](https://gitlab.com/gogidix/shared-libraries/shared-exceptions/-/tags)

**Centralized exception management service providing enterprise-grade error handling, analytics, and monitoring capabilities for the GOGIDIX ecosystem.**

---

## 🎯 **Overview**

The **Shared Exceptions Service** is a core infrastructure component that provides centralized exception logging, analysis, and monitoring for all GOGIDIX microservices. Built with enterprise-grade reliability and scalability, it offers real-time exception tracking, intelligent categorization, and comprehensive analytics.

### **Key Features**

- 🔍 **Centralized Exception Logging** - Single point for all ecosystem exceptions
- 📊 **Real-time Analytics** - Exception trends, patterns, and insights
- 🚨 **Intelligent Alerting** - Automated escalation based on severity
- 🔒 **Enterprise Security** - JWT authentication and role-based access
- ⚡ **High Performance** - <100ms response times, 10,000+ exceptions/minute
- 📈 **Comprehensive Monitoring** - Prometheus metrics, Jaeger tracing
- 🏗️ **Cloud-Native Architecture** - Kubernetes-ready with auto-scaling
- 🔄 **Event-Driven Integration** - Kafka-based cross-service communication

---

## 🏗️ **Architecture**

### **Service Architecture**
```
Port: 8702
Framework: Spring Boot 3.1.5 + Java 17
Architecture: Hexagonal/Clean Architecture
Database: PostgreSQL with Redis caching
Messaging: Apache Kafka event streaming
```

### **Core Components**
- **Exception Controller** - REST API endpoints for exception management
- **Exception Service** - Business logic for categorization and analysis
- **Analytics Service** - Statistical analysis and trend detection
- **Event Service** - Kafka-based event publishing and handling
- **Cache Service** - Redis-based performance optimization

For detailed architecture information, see [ARCHITECTURE_DIAGRAM.md](./ARCHITECTURE_DIAGRAM.md).

---

## 🚀 **Quick Start**

### **Prerequisites**
- Java 17+
- Docker & Docker Compose
- PostgreSQL 15+
- Redis 7+
- Apache Kafka 3.0+

### **Local Development Setup**

1. **Clone and Build**
   ```bash
   git clone https://gitlab.com/gogidix/shared-libraries/shared-exceptions.git
   cd shared-exceptions
   ./mvnw clean compile
   ```

2. **Start Infrastructure**
   ```bash
   # Start all dependencies
   ./scripts/docker-compose-dev.sh up postgres redis kafka
   
   # Verify services are healthy
   ./scripts/docker-compose-dev.sh health
   ```

3. **Run the Service**
   ```bash
   # Development mode
   ./mvnw spring-boot:run -Dspring-boot.run.profiles=development
   
   # Or with Docker
   ./scripts/docker-build.sh --test
   ./scripts/docker-compose-dev.sh up shared-exceptions
   ```

4. **Verify Installation**
   ```bash
   # Health check
   curl http://localhost:8702/api/v1/actuator/health
   
   # API documentation
   open http://localhost:8702/swagger-ui/index.html
   ```

### **Docker Compose (Recommended)**
```bash
# Complete development environment
docker-compose up -d

# Check all services
docker-compose ps

# View logs
docker-compose logs -f shared-exceptions
```

---

## 📚 **API Documentation**

### **Core Endpoints**

#### **Exception Management**
```bash
# Log a new exception
POST /api/v1/exceptions
{
  "message": "Database connection timeout",
  "serviceName": "social-commerce",
  "errorCode": "DB_TIMEOUT_001",
  "severity": "HIGH",
  "stackTrace": "...",
  "metadata": {
    "userId": "12345",
    "transactionId": "tx-abc123"
  }
}

# Get exception by ID
GET /api/v1/exceptions/{id}

# List exceptions with filtering
GET /api/v1/exceptions?serviceName=social-commerce&severity=HIGH&limit=50

# Update exception status
PUT /api/v1/exceptions/{id}
{
  "status": "RESOLVED",
  "resolution": "Database connection pool increased"
}

# Get exception statistics
GET /api/v1/exceptions/stats?period=24h&groupBy=severity
```

#### **Health & Monitoring**
```bash
# Service health
GET /actuator/health

# Prometheus metrics
GET /actuator/prometheus

# Service information
GET /actuator/info
```

### **Interactive API Documentation**
- **Swagger UI**: http://localhost:8702/swagger-ui/index.html
- **ReDoc**: http://localhost:8702/redoc
- **OpenAPI Spec**: http://localhost:8702/api/v1/api-docs

---

## 🔧 **Configuration**

### **Environment Variables**
```bash
# Database Configuration
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/gogidix_shared_exceptions
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=secret

# Redis Configuration
SPRING_REDIS_HOST=localhost
SPRING_REDIS_PORT=6379
SPRING_REDIS_DATABASE=2

# Kafka Configuration
SPRING_KAFKA_BOOTSTRAP_SERVERS=localhost:9092

# Security Configuration
JWT_ISSUER_URI=https://auth.gogidix.com
JWT_JWK_SET_URI=https://auth.gogidix.com/.well-known/jwks.json

# Monitoring Configuration
OTLP_ENDPOINT=http://jaeger:4318/v1/traces
MANAGEMENT_ENDPOINTS_WEB_EXPOSURE_INCLUDE=health,info,prometheus,metrics
```

### **Profiles**
- **development** - Local development with H2 database
- **docker** - Docker environment with external dependencies
- **test** - Testing with test containers
- **production** - Production configuration with security enabled

### **Custom Configuration**
See [application.yml](src/main/resources/application.yml) for complete configuration options.

---

## 🏃‍♂️ **Development**

### **Project Structure**
```
shared-exceptions/
├── src/main/java/com/gogidix/exceptions/
│   ├── api/                 # REST Controllers and DTOs
│   ├── application/         # Application Services
│   ├── domain/              # Domain Entities and Logic
│   └── infrastructure/      # Infrastructure Components
├── src/main/resources/
│   ├── application.yml      # Configuration
│   └── db/migration/        # Database migrations
├── docker/                  # Docker configuration files
├── scripts/                 # Development and deployment scripts
├── k8s/                     # Kubernetes manifests
└── docs/                    # Additional documentation
```

### **Development Commands**
```bash
# Run tests
./mvnw test

# Generate test coverage
./mvnw test jacoco:report

# Code quality checks
./mvnw checkstyle:check spotbugs:check

# Build JAR
./mvnw package

# Build Docker image
./scripts/docker-build.sh

# Run security scans
./mvnw dependency-check:check
docker run --rm -v $(pwd):/workspace aquasec/trivy:latest fs /workspace
```

### **Code Quality Standards**
- **Test Coverage**: >80% line coverage required
- **Code Style**: Google Java Style Guide with Checkstyle
- **Security**: SpotBugs and OWASP dependency checks
- **Documentation**: Javadoc for all public APIs
- **Formatting**: Automatic formatting with google-java-format

---

## 🔒 **Security**

### **Authentication & Authorization**
- **JWT Tokens**: RS256 signed tokens with issuer validation
- **Role-Based Access**: Scoped permissions for different operations
- **API Rate Limiting**: Per-client and global rate limiting
- **Request Validation**: Comprehensive input validation and sanitization

### **Security Features**
- **HTTPS Only**: TLS 1.3 encryption for all communications
- **CORS Protection**: Configurable CORS policies
- **Security Headers**: OWASP recommended security headers
- **Audit Logging**: Complete audit trail for all operations
- **Secrets Management**: Integration with HashiCorp Vault
- **Vulnerability Scanning**: Automated Trivy and Snyk scans

### **Security Configuration**
```yaml
gogidix:
  security:
    jwt:
      issuer-uri: ${JWT_ISSUER_URI:https://auth.gogidix.com}
      jwk-set-uri: ${JWT_JWK_SET_URI:https://auth.gogidix.com/.well-known/jwks.json}
    cors:
      allowed-origins: ${CORS_ALLOWED_ORIGINS:https://app.gogidix.com}
      allowed-methods: ${CORS_ALLOWED_METHODS:GET,POST,PUT,DELETE}
    rate-limiting:
      enabled: true
      requests-per-minute: 1000
```

---

## 🚀 **Deployment**

### **Production Deployment**

#### **Kubernetes (Recommended)**
```bash
# Apply Kubernetes manifests
kubectl apply -f k8s/

# Verify deployment
kubectl get pods -l app=shared-exceptions
kubectl get services shared-exceptions

# Check service health
kubectl exec -it deployment/shared-exceptions -- curl localhost:8702/actuator/health
```

#### **Docker Compose**
```bash
# Production deployment
docker-compose -f docker-compose.prod.yml up -d

# Scale service
docker-compose up -d --scale shared-exceptions=3
```

### **Environment Requirements**

#### **Production Resources**
- **CPU**: 2 cores minimum, 4 cores recommended
- **Memory**: 4GB minimum, 8GB recommended
- **Storage**: 100GB minimum with auto-scaling
- **Network**: Stable network connectivity to PostgreSQL, Redis, Kafka

#### **Database Requirements**
- **PostgreSQL 15+**: Primary database with 2 read replicas
- **Redis Cluster**: 3-node cluster for caching and sessions
- **Kafka Cluster**: 3-broker cluster for event streaming

### **High Availability Setup**
- **Pod Replicas**: 5 replicas across 3 availability zones
- **Load Balancing**: NGINX ingress with health checks
- **Database HA**: PostgreSQL streaming replication
- **Cache HA**: Redis Cluster with automatic failover
- **Message HA**: Kafka with replication factor 3

---

## 📊 **Monitoring & Observability**

### **Metrics & Dashboards**
- **Prometheus Metrics**: Custom business metrics and JVM metrics
- **Grafana Dashboards**: Pre-built dashboards for service monitoring
- **Application Metrics**: Exception counts, resolution times, service health
- **Infrastructure Metrics**: CPU, memory, database performance

### **Distributed Tracing**
- **Jaeger Integration**: End-to-end request tracing
- **Correlation IDs**: Request correlation across microservices
- **Performance Analysis**: Query performance and bottleneck identification
- **Dependency Mapping**: Service dependency visualization

### **Logging & Alerting**
- **Structured Logging**: JSON logging with correlation IDs
- **Centralized Logs**: ELK stack integration for log aggregation
- **Custom Alerts**: Configurable alerts via Alertmanager
- **Incident Response**: Integration with PagerDuty and Slack

### **Health Checks**
- **Liveness Probe**: `/actuator/health/liveness`
- **Readiness Probe**: `/actuator/health/readiness`
- **Custom Health Indicators**: Database, Redis, Kafka connectivity
- **Dependency Health**: External service health monitoring

---

## 🧪 **Testing**

### **Test Strategy**
```bash
# Unit tests
./mvnw test

# Integration tests
./mvnw integration-test

# End-to-end tests
./mvnw test -Dtest=**/*E2ETest

# Performance tests
./mvnw test -Dtest=**/*PerformanceTest

# Security tests
./mvnw test -Dtest=**/*SecurityTest
```

### **Test Coverage**
- **Unit Tests**: >90% coverage for business logic
- **Integration Tests**: Database, cache, and messaging integration
- **Contract Tests**: API contract validation with Spring Cloud Contract
- **Performance Tests**: Load testing with JMeter
- **Security Tests**: Authentication, authorization, and input validation

### **Test Environment**
- **TestContainers**: Isolated test environment with Docker
- **Test Profiles**: Dedicated test configuration
- **Mock Services**: Wiremock for external service mocking
- **Test Data**: Flyway migrations for test data setup

---

## 🔄 **CI/CD Pipeline**

### **GitLab CI/CD**
The service includes a comprehensive CI/CD pipeline with 6 stages:

1. **Validate** - Code quality, Dockerfile linting, YAML validation
2. **Build** - Maven compilation and Docker image building
3. **Test** - Unit tests, integration tests, performance tests
4. **Security** - Container scanning, secrets detection, license compliance
5. **Package** - JAR packaging and Docker image tagging
6. **Deploy** - Multi-environment deployment (dev, staging, production)

### **Pipeline Features**
- **Parallel Execution**: Jobs run in parallel for faster builds
- **Caching Strategy**: Maven and Docker layer caching
- **Security Scanning**: Trivy, TruffleHog, OWASP dependency checks
- **Multi-Environment**: Automated deployment to 3 environments
- **Manual Gates**: Production deployments require manual approval
- **Rollback Support**: Easy rollback through version management

See [.gitlab-ci.yml](.gitlab-ci.yml) for complete pipeline configuration.

---

## 📖 **Additional Documentation**

### **Architecture & Design**
- [Architecture Diagram](./ARCHITECTURE_DIAGRAM.md) - Detailed architecture documentation
- [API Documentation](./docs/api/) - Complete API reference
- [Database Schema](./docs/database/) - Database design and migrations
- [Security Guide](./docs/security/) - Security implementation details

### **Operations & Troubleshooting**
- [Troubleshooting Guide](./docs/troubleshooting/) - Common issues and solutions
- [Performance Tuning](./docs/performance/) - Optimization recommendations
- [Disaster Recovery](./docs/disaster-recovery/) - Backup and recovery procedures
- [Monitoring Runbook](./docs/monitoring/) - Monitoring and alerting guide

### **Development & Contributing**
- [Development Guide](./docs/development/) - Development setup and guidelines
- [Contributing Guidelines](./CONTRIBUTING.md) - Code contribution process
- [Code of Conduct](./CODE_OF_CONDUCT.md) - Community standards
- [Changelog](./CHANGELOG.md) - Version history and changes

---

## ⚡ **Performance Characteristics**

### **Benchmarks**
- **Throughput**: 10,000 exceptions/minute (167 TPS) peak capacity
- **Response Time**: <100ms average API response time
- **Availability**: 99.9% uptime SLA
- **Concurrency**: 1,000 simultaneous connections supported
- **Cache Hit Ratio**: >85% for frequently accessed data

### **Resource Usage**
- **Memory**: 1.5-2.5GB typical usage, 4GB limit
- **CPU**: 0.5-1.0 cores typical usage, 2.0 cores limit  
- **Storage**: Auto-scaling from 50GB initial allocation
- **Network**: <100Mbps typical throughput
- **Database Connections**: 10-30 connection pool size

### **Scaling Characteristics**
- **Horizontal Scaling**: Auto-scaling 2-10 pod replicas
- **Load Distribution**: NGINX ingress load balancing
- **Database Scaling**: Read replicas for analytics workload
- **Cache Scaling**: Redis cluster horizontal scaling
- **Event Processing**: Kafka partition-based parallel processing

---

## 🤝 **Contributing**

We welcome contributions to improve the Shared Exceptions Service! Please read our [Contributing Guidelines](./CONTRIBUTING.md) for details on:

- Code of conduct and community standards
- Development setup and coding standards
- Pull request process and review criteria
- Issue reporting and feature requests
- Testing requirements and quality gates

### **Quick Contribution Guide**
1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

---

## 📞 **Support & Community**

### **Getting Help**
- **Documentation**: Start with this README and [Architecture Guide](./ARCHITECTURE_DIAGRAM.md)
- **Issue Tracker**: [GitLab Issues](https://gitlab.com/gogidix/shared-libraries/shared-exceptions/-/issues)
- **Slack Channel**: `#shared-exceptions` in GOGIDIX Workspace
- **Email Support**: support@gogidix.com

### **Reporting Issues**
- **Bug Reports**: Use the bug report template with reproduction steps
- **Feature Requests**: Use the feature request template with use cases
- **Security Issues**: Email security@gogidix.com (do not use public issues)
- **Performance Issues**: Include profiling data and environment details

### **Community Resources**
- **Wiki**: [Service Wiki](https://gitlab.com/gogidix/shared-libraries/shared-exceptions/-/wikis/home)
- **Discussions**: [GitLab Discussions](https://gitlab.com/gogidix/shared-libraries/shared-exceptions/-/issues)
- **Release Notes**: [GitLab Releases](https://gitlab.com/gogidix/shared-libraries/shared-exceptions/-/releases)
- **Roadmap**: [Project Roadmap](https://gitlab.com/gogidix/shared-libraries/shared-exceptions/-/boards)

---

## 📄 **License & Legal**

### **License**
This project is licensed under the **MIT License** - see the [LICENSE](./LICENSE) file for details.

### **Copyright**
Copyright © 2025 Gogidix Technologies Limited. All rights reserved.

### **Third-Party Licenses**
- Spring Boot: Apache License 2.0
- PostgreSQL: PostgreSQL License
- Redis: Redis Source Available License 2.0 (RSALv2)
- Apache Kafka: Apache License 2.0

See [THIRD_PARTY_LICENSES.md](./docs/legal/THIRD_PARTY_LICENSES.md) for complete third-party license information.

---

## 📊 **Project Status**

### **Current Version**
- **Version**: 1.0.0
- **Release Date**: 2025-08-13
- **Stability**: Production Ready ✅
- **Support Status**: Active Development & Support

### **Compatibility**
- **Java**: 17, 21 (LTS versions)
- **Spring Boot**: 3.1.5, 3.2.x
- **PostgreSQL**: 15, 16
- **Redis**: 7.0, 7.2
- **Kafka**: 3.0, 3.1, 3.2

### **Roadmap Highlights**
- **Q1 2025**: Machine learning-based exception categorization
- **Q2 2025**: Advanced analytics and predictive insights
- **Q3 2025**: Multi-tenant architecture support
- **Q4 2025**: GraphQL API and enhanced integrations

---

## 🙏 **Acknowledgments**

Special thanks to:
- **GOGIDIX Engineering Team** - Core architecture and development
- **Spring Boot Team** - Excellent framework foundation
- **Apache Software Foundation** - Kafka and other essential tools
- **PostgreSQL Global Development Group** - Robust database platform
- **Redis Community** - High-performance caching solution
- **Kubernetes Community** - Cloud-native orchestration platform

---

**🚀 Ready to get started? Follow the [Quick Start](#-quick-start) guide above!**

For questions, support, or contributions, please refer to our [Support & Community](#-support--community) section.

---

<div align="center">

**Built with ❤️ by the GOGIDIX Engineering Team**

[Website](https://gogidix.com) • [Documentation](https://docs.gogidix.com) • [Support](mailto:support@gogidix.com)

</div>