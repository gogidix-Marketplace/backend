# Deployment Guide - Warehousing Domain

## Overview

This guide covers deployment of the Shared Warehousing Core domain across different environments, including local development, Docker Compose, Kubernetes, and Helm charts.

## Table of Contents

- [Prerequisites](#prerequisites)
- [Local Development Setup](#local-development-setup)
- [Docker Compose Deployment](#docker-compose-deployment)
- [Kubernetes Deployment](#kubernetes-deployment)
- [Helm Deployment](#helm-deployment)
- [Environment Configuration](#environment-configuration)
- [Rolling Updates](#rolling-updates)
- [Troubleshooting](#troubleshooting)

---

## Prerequisites

### Local Development

| Tool | Version |
|------|---------|
| Java JDK | 17+ |
| Maven | 3.9+ |
| Node.js | 20.x |
| Docker | 24.x |
| Docker Compose | 2.x |

### Kubernetes Deployment

| Tool | Version |
|------|---------|
| kubectl | 1.28+ |
| Helm | 3.12+ |
| Kubernetes Cluster | 1.25+ |

---

## Local Development Setup

### 1. Clone Repository

```bash
cd /path/to/Gogidix-ecosystem/x-gogidix-domain/shared-business-infrastructure/shared-warehousing-core
```

### 2. Start Infrastructure Services

```bash
cd Backend/Java
docker-compose -f docker-compose-warehousing.yml up -d mongodb kafka redis
```

### 3. Build and Run Java Services

#### Using Maven

```bash
# Build all services
cd Backend/Java
mvn clean install

# Run specific service
cd Inventory/inventory-core-service
mvn spring-boot:run
```

#### Service Ports

| Service | Port | Management Port |
|---------|------|-----------------|
| inventory-core-service | 8081 | 8211 |
| fulfillment-core-service | 8085 | 8285 |
| space-service | 8083 | 8283 |
| pricing-service | 8084 | 8284 |
| public-booking-service | 8090 | 8290 |

### 4. Run Frontend Applications

```bash
# Partners Dashboard
cd Frontends/Web/partners-dashboard
npm install
npm run dev

# Private Storage Dashboard
cd Frontends/Web/private-storage-dashboard
npm install
npm run dev
```

| Application | Port |
|-------------|------|
| Partners Dashboard | 3001 |
| Private Storage Dashboard | 3002 |

### 5. Verify Services

```bash
# Health check
curl http://localhost:8081/actuator/health
curl http://localhost:8085/actuator/health

# API test
curl -H "X-Tenant-ID: test-tenant" http://localhost:8081/api/v1/inventory/items
```

---

## Docker Compose Deployment

### Quick Start

```bash
cd Backend/Java
docker-compose -f docker-compose-warehousing.yml up -d
```

### Docker Compose File Structure

The `docker-compose-warehousing.yml` includes:

```yaml
version: '3.8'

services:
  # Infrastructure
  mongodb:
    image: mongo:6.0
    ports: ["27017:27017"]
    environment:
      MONGO_INITDB_DATABASE: shared_warehousing
    volumes:
      - mongodb-data:/data/db

  kafka:
    image: bitnami/kafka:3.5
    ports: ["9092:9092"]
    environment:
      KAFKA_CFG_ZOOKEEPER_CONNECT: zookeeper:2181
      KAFKA_CFG_AUTO_CREATE_TOPICS_ENABLE: true

  redis:
    image: redis:7-alpine
    ports: ["6379:6379"]

  # Backend Services
  inventory-core-service:
    build: ./Inventory/inventory-core-service
    ports: ["8081:8081"]
    depends_on: [mongodb, kafka, redis]
    environment:
      SPRING_DATA_MONGODB_URI: mongodb://mongodb:27017/shared_warehousing
      KAFKA_BOOTSTRAP_SERVERS: kafka:9092
      SPRING_REDIS_HOST: redis

  fulfillment-core-service:
    build: ./Fulfillment/fulfillment-core-service
    ports: ["8085:8085"]
    depends_on: [mongodb, kafka, redis]

  # Frontend Applications
  partners-dashboard:
    build: ../../Frontends/Web/partners-dashboard
    ports: ["3001:3000"]
```

### Commands

```bash
# Start all services
docker-compose -f docker-compose-warehousing.yml up -d

# View logs
docker-compose -f docker-compose-warehousing.yml logs -f inventory-core-service

# Stop all services
docker-compose -f docker-compose-warehousing.yml down

# Stop and remove volumes
docker-compose -f docker-compose-warehousing.yml down -v
```

---

## Kubernetes Deployment

### Namespace Structure

```bash
# Create namespaces
kubectl create namespace warehousing-core
kubectl create namespace warehousing-staging
kubectl create namespace warehousing-warehousing
```

### Apply Base Configuration

```bash
# Apply namespace, secrets, configmaps
kubectl apply -f k8s/warehousing-core/base/

# Apply services
kubectl apply -f k8s/warehousing-core/services/
```

### Kustomization

```bash
# Development overlay
kubectl apply -k k8s/warehousing-core/overlays/development/

# Staging overlay
kubectl apply -k k8s/warehousing-core/overlays/staging/

# Production overlay
kubectl apply -k k8s/warehousing-core/overlays/production/
```

### Service Deployment Example

```yaml
# k8s/warehousing-core/services/inventory-core-service/deployment.yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: inventory-core-service
  namespace: warehousing-core
spec:
  replicas: 3
  selector:
    matchLabels:
      app: inventory-core-service
  template:
    metadata:
      labels:
        app: inventory-core-service
        version: v1
    spec:
      containers:
      - name: inventory-core-service
        image: gogidix/inventory-core-service:1.0.0
        ports:
        - containerPort: 8081
          name: http
        - containerPort: 8211
          name: management
        env:
        - name: SPRING_PROFILES_ACTIVE
          value: "production"
        - name: SPRING_DATA_MONGODB_URI
          valueFrom:
            secretKeyRef:
              name: warehousing-secrets
              key: mongodb-uri
        resources:
          requests:
            memory: "256Mi"
            cpu: "250m"
          limits:
            memory: "512Mi"
            cpu: "500m"
        livenessProbe:
          httpGet:
            path: /actuator/health/liveness
            port: management
          initialDelaySeconds: 60
          periodSeconds: 15
        readinessProbe:
          httpGet:
            path: /actuator/health/readiness
            port: management
          initialDelaySeconds: 30
          periodSeconds: 10
```

### Deploy All Services

```bash
# Deploy all services using the kustomization
cd k8s/warehousing-core
kubectl apply -f services/
```

### Verify Deployment

```bash
# Check pods
kubectl get pods -n warehousing-core

# Check services
kubectl get svc -n warehousing-core

# Check logs
kubectl logs -f deployment/inventory-core-service -n warehousing-core

# Port forward for local testing
kubectl port-forward svc/inventory-core-service 8081:8081 -n warehousing-core
```

---

## Helm Deployment

### Install Helm Chart

```bash
# Create values file for environment
cat > values-production.yaml << EOF
image:
  repository: gogidix
  pullPolicy: Always
  tag: "1.0.0"

resources:
  inventory:
    replicas: 3
    cpu:
      request: 250m
      limit: 500m
    memory:
      request: 256Mi
      limit: 512Mi

mongodb:
  enabled: true
  uri: mongodb://mongodb-0.mongodb-headless:27017/shared_warehousing

kafka:
  enabled: true
  bootstrapServers: kafka-0.kafka-headless:9092
EOF

# Install chart
helm install warehousing ./helm/warehousing-core \
  -f values-production.yaml \
  -n warehousing-core \
  --create-namespace
```

### Upgrade Deployment

```bash
helm upgrade warehousing ./helm/warehousing-core \
  -f values-production.yaml \
  -n warehousing-core
```

### Rollback

```bash
# List revisions
helm history warehousing -n warehousing-core

# Rollback to previous revision
helm rollback warehousing 1 -n warehousing-core
```

---

## Environment Configuration

### Environment Variables

| Variable | Description | Default |
|----------|-------------|---------|
| `SPRING_PROFILES_ACTIVE` | Spring profile | `dev` |
| `SERVER_PORT` | HTTP port | `8080` |
| `MANAGEMENT_SERVER_PORT` | Management port | `8081` |
| `SPRING_DATA_MONGODB_URI` | MongoDB URI | `mongodb://localhost:27017` |
| `KAFKA_BOOTSTRAP_SERVERS` | Kafka brokers | `localhost:9092` |
| `SPRING_REDIS_HOST` | Redis host | `localhost` |
| `SPRING_REDIS_PORT` | Redis port | `6379` |
| `EUREKA_CLIENT_SERVICEURL_DEFAULTZONE` | Eureka server | `http://localhost:8761` |

### Secrets Management

#### Kubernetes Secrets

```bash
# Create secret for MongoDB
kubectl create secret generic warehousing-secrets \
  --from-literal=mongodb-uri="mongodb://user:pass@mongodb:27017/db" \
  --from-literal=mongodb-username="user" \
  --from-literal=mongodb-password="pass" \
  -n warehousing-core

# Create secret for Redis
kubectl create secret generic redis-secrets \
  --from-literal=redis-password="redis-password" \
  -n warehousing-core
```

### ConfigMap Example

```yaml
apiVersion: v1
kind: ConfigMap
metadata:
  name: warehousing-config
  namespace: warehousing-core
data:
  SPRING_PROFILES_ACTIVE: "production"
  KAFKA_BOOTSTRAP_SERVERS: "kafka-0.kafka-headless:9092"
  REDIS_HOST: "redis-master"
  REDIS_PORT: "6379"
  EUREKA_CLIENT_SERVICEURL_DEFAULTZONE: "http://eureka:8761/eureka"
```

---

## Rolling Updates

### Strategy

All deployments use RollingUpdate strategy:

```yaml
strategy:
  type: RollingUpdate
  rollingUpdate:
    maxSurge: 1
    maxUnavailable: 0
```

### Execute Rolling Update

```bash
# Update image tag
kubectl set image deployment/inventory-core-service \
  inventory-core-service=gogidix/inventory-core-service:1.1.0 \
  -n warehousing-core

# Watch rollout status
kubectl rollout status deployment/inventory-core-service -n warehousing-core

# Check revision history
kubectl rollout history deployment/inventory-core-service -n warehousing-core
```

### Canary Deployment

```bash
# Create canary deployment
kubectl apply -f k8s/warehousing-core/services/inventory-core-service/canary-deployment.yaml

# Split traffic using Istio
kubectl apply -f k8s/warehousing-core/services/inventory-core-service/virtual-service.yaml
```

---

## Troubleshooting

### Common Issues

#### Service Not Starting

```bash
# Check pod status
kubectl get pods -n warehousing-core

# Describe pod for events
kubectl describe pod inventory-core-service-xxx -n warehousing-core

# Check logs
kubectl logs inventory-core-service-xxx -n warehousing-core
```

#### Database Connection Issues

```bash
# Check MongoDB is reachable
kubectl exec -it inventory-core-service-xxx -n warehousing-core -- \
  curl http://mongodb:27017

# Check connection string
kubectl get configmap warehousing-config -n warehousing-core -o yaml
```

#### Kafka Issues

```bash
# Check Kafka topics
kubectl exec -it kafka-0 -n warehousing-core -- \
  kafka-topics.sh --list --bootstrap-server localhost:9092

# Check consumer lag
kubectl exec -it inventory-core-service-xxx -n warehousing-core -- \
  kafka-consumer-groups.sh --bootstrap-server kafka:9092 --describe --group inventory-service-group
```

### Health Check Scripts

```bash
#!/bin/bash
# health-check.sh

SERVICES=(
  "inventory-core-service"
  "fulfillment-core-service"
  "space-service"
  "pricing-service"
)

NAMESPACE="warehousing-core"

for service in "${SERVICES[@]}"; do
  echo "Checking $service..."
  kubectl get pods -n $NAMESPACE -l app=$service
  kubectl rollout status deployment/$service -n $NAMESPACE
done

# Check external endpoints
for service in "${SERVICES[@]}"; do
  echo "Health check for $service..."
  kubectl run curl-test --image=curlimages/curl --rm -i --restart=Never -- \
    curl -f http://$service:8211/actuator/health
done
```

### Log Aggregation

```bash
# Stream logs from all services
kubectl logs -f -l app=inventory-core-service -n warehousing-core --all-containers=true

# Get logs from specific time range
kubectl logs inventory-core-service-xxx -n warehousing-core \
  --since-time=2024-01-15T00:00:00Z \
  --until-time=2024-01-15T23:59:59Z
```

---

## Scaling

### Horizontal Pod Autoscaler

```yaml
apiVersion: autoscaling/v2
kind: HorizontalPodAutoscaler
metadata:
  name: inventory-core-service-hpa
  namespace: warehousing-core
spec:
  scaleTargetRef:
    apiVersion: apps/v1
    kind: Deployment
    name: inventory-core-service
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
        averageUtilization: 80
```

### Manual Scaling

```bash
# Scale up
kubectl scale deployment inventory-core-service --replicas=5 -n warehousing-core

# Scale down
kubectl scale deployment inventory-core-service --replicas=2 -n warehousing-core
```

---

## Monitoring Setup

### Prometheus ServiceMonitors

ServiceMonitors are automatically deployed with services:

```bash
kubectl apply -f k8s/warehousing-base/monitoring/servicemonitor.yaml
```

### Grafana Dashboards

Import dashboards from monitoring directory:

```bash
kubectl create configmap grafana-dashboards \
  --from-file=k8s/warehousing-base/monitoring/grafana-dashboards/ \
  -n monitoring
```

---

## Backup and Restore

### MongoDB Backup

```bash
# Create backup
kubectl exec -it mongodb-0 -n warehousing-core -- \
  mongodump --archive=/backup/mongodb-$(date +%Y%m%d).gz

# Copy backup locally
kubectl cp warehousing-core/mongodb-0:/backup/mongodb-20240115.gz ./backup.gz

# Restore from backup
kubectl cp ./backup.gz warehousing-core/mongodb-0:/backup/restore.gz
kubectl exec -it mongodb-0 -n warehousing-core -- \
  mongorestore --archive=/backup/restore.gz
```

---

## Disaster Recovery

### Restore Procedure

1. **Restore Infrastructure**
```bash
kubectl apply -f k8s/warehousing-core/base/
```

2. **Restore Secrets**
```bash
kubectl apply -f backups/secrets.yaml
```

3. **Restore Data**
```bash
# Follow MongoDB backup/restore procedure
```

4. **Verify Services**
```bash
kubectl get pods -n warehousing-core
kubectl run test-pod --image=curlimages/curl --rm -it --restart=Never -- \
  curl http://inventory-core-service:8211/actuator/health
```
