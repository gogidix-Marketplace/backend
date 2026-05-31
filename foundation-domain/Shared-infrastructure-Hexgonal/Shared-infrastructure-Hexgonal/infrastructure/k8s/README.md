# Foundation Domain Infrastructure - Kubernetes Manifests

This directory contains all Kubernetes manifests for deploying Foundation Domain infrastructure components.

## Deployment Order

1. **Prerequisites**: Create namespace and policies
   ```bash
   kubectl apply -f namespace.yaml
   kubectl apply -f network-policies.yaml
   kubectl apply -f resource-quotas.yaml
   ```

2. **Core Infrastructure**: Deploy databases and messaging
   ```bash
   # MongoDB (3 replicas)
   kubectl apply -f ../mongodb/k8s/mongodb.yaml

   # PostgreSQL (2 replicas)
   kubectl apply -f ../postgresql/k8s/postgresql.yaml

   # Zookeeper (3 replicas) - Required for Kafka
   kubectl apply -f ../kafka/k8s/kafka.yaml

   # Redis (3 replicas + Sentinel)
   kubectl apply -f ../redis/k8s/redis.yaml
   ```

## Components

| Component | Replicas | Storage | Purpose |
|-----------|-----------|---------|---------|
| MongoDB | 3 | 20Gi per replica | Document database for users, sessions, tenants, audit logs |
| PostgreSQL | 2 | 30Gi per replica | Relational database for subscriptions, invoices, payments |
| Zookeeper | 3 | 10Gi per replica | Coordination service for Kafka |
| Kafka | 3 | 20Gi per replica | Event streaming platform |
| Redis | 3 | 5Gi per replica | Caching layer with Sentinel HA |

## Access Points

| Service | Type | Internal URL | External Port |
|---------|------|--------------|---------------|
| mongodb | Headless | mongodb.infrastructure.svc.cluster.local | 27017 |
| postgresql | Headless | postgresql.infrastructure.svc.cluster.local | 5432 |
| zookeeper | Headless | zookeeper.infrastructure.svc.cluster.local | 2181 |
| kafka-bootstrap | ClusterIP | kafka-bootstrap.infrastructure.svc.cluster.local | 9092 |
| redis-cluster | ClusterIP | redis-cluster.infrastructure.svc.cluster.local | 6379 |
| redis-sentinel-cluster | ClusterIP | redis-sentinel-cluster.infrastructure.svc.cluster.local | 26379 |

## Security Notes

1. **Secrets**: All passwords must be changed from defaults before production use
2. **Network Policies**: Only traffic within namespace and from gateway is allowed
3. **Pod Disruption Budgets**: Ensures minimum availability during maintenance
4. **Resource Limits**: Prevents resource exhaustion

## Connection Strings

### MongoDB
```
mongodb://<username>:<password>@mongodb-0.mongodb.infrastructure.svc.cluster.local:27017,mongodb-1.mongodb.infrastructure.svc.cluster.local:27017,mongodb-2.mongodb.infrastructure.svc.cluster.local:27017/<database>?replicaSet=rs0
```

### PostgreSQL
```
jdbc:postgresql://postgresql.infrastructure.svc.cluster.local:5432/<database>
```

### Kafka
```
spring.kafka.bootstrap-servers=kafka-bootstrap.infrastructure.svc.cluster.local:9092
```

### Redis
```
spring.redis.host=redis-sentinel-cluster.infrastructure.svc.cluster.local
spring.redis.port=26379
spring.redis.sentinel.master=mymaster
```

## Monitoring

- Redis metrics available on port 9121 via redis-exporter
- Configure Prometheus ServiceMonitor for automatic scraping
- Configure Grafana dashboards for monitoring

## Backup Strategy

- MongoDB: Configured with replica set, backup from secondary
- PostgreSQL: WAL archiving enabled, streaming replication
- Kafka: Topic retention configured, periodic backup via MirrorMaker
- Redis: RDB snapshots + AOF for durability
