# 🔧 Shared Messaging Library Operations Guide

## 🎯 Overview

This operations guide provides comprehensive instructions for deploying, monitoring, maintaining, and troubleshooting the Shared Messaging Library in production environments.

**Service Information:**
- **Service Name**: shared-messaging
- **Port**: 8501
- **Namespace**: gogidix-infrastructure
- **Health Check**: `/api/messaging/actuator/health`

## 🚀 Deployment Operations

### **Prerequisites**
- Kubernetes cluster with sufficient resources
- PostgreSQL database instance
- Redis cache instance
- Kafka message broker
- Docker registry access

### **Environment Setup**
```bash
# Create namespace
kubectl create namespace gogidix-infrastructure

# Apply configurations
kubectl apply -f k8s/configmap.yaml
kubectl apply -f k8s/secrets.yaml
kubectl apply -f k8s/service-account.yaml

# Deploy application
kubectl apply -f k8s/deployment.yaml
```

### **Deployment Verification**
```bash
# Check pod status
kubectl get pods -n gogidix-infrastructure -l app=shared-messaging

# Verify service endpoints
kubectl get svc -n gogidix-infrastructure shared-messaging-service

# Check deployment rollout
kubectl rollout status deployment/shared-messaging -n gogidix-infrastructure
```

### **Environment-Specific Deployments**

#### **Development Environment**
```bash
# Deploy to development
kubectl apply -f k8s/ --namespace=gogidx-dev

# Port forward for local access
kubectl port-forward svc/shared-messaging-service 8501:8501 -n gogidix-dev
```

#### **Production Environment**
```bash
# Deploy with production profile
kubectl apply -f k8s/ --namespace=gogidix-prod

# Verify production readiness
./scripts/smoke-test.sh https://api.gogidix.com
```

## 📊 Monitoring Operations

### **Health Check Monitoring**
```bash
# Application health
curl -f https://api.gogidix.com/api/messaging/actuator/health

# Component-specific health checks
curl -f https://api.gogidix.com/api/messaging/actuator/health/db
curl -f https://api.gogidix.com/api/messaging/actuator/health/redis
curl -f https://api.gogidix.com/api/messaging/actuator/health/kafka
```

### **Metrics Collection**
```bash
# Prometheus metrics
curl https://api.gogidix.com/api/messaging/actuator/prometheus

# Application metrics
curl https://api.gogidix.com/api/messaging/actuator/metrics

# Custom business metrics
curl https://api.gogidix.com/api/messaging/messages/statistics
```

### **Log Monitoring**
```bash
# View application logs
kubectl logs -f deployment/shared-messaging -n gogidx-infrastructure

# Filter error logs
kubectl logs deployment/shared-messaging -n gogidix-infrastructure | grep ERROR

# Tail logs with timestamps
kubectl logs --timestamps=true -f deployment/shared-messaging -n gogidix-infrastructure
```

### **Performance Monitoring**

#### **Key Performance Indicators (KPIs)**
- **Message Throughput**: Messages processed per second
- **Delivery Success Rate**: Percentage of successfully delivered messages
- **Average Response Time**: API response latency
- **Error Rate**: Percentage of failed requests
- **Queue Depth**: Number of pending messages

#### **Performance Queries**
```bash
# Get message statistics
curl "https://api.gogidix.com/api/messaging/messages/statistics?period=1h"

# Check queue health
curl "https://api.gogidix.com/api/messaging/messages/health"

# Performance metrics
curl "https://api.gogidix.com/api/messaging/actuator/metrics/http.server.requests"
```

## 🔍 Alerting and Notifications

### **Critical Alerts**
- Application health check failures
- Database connectivity issues
- High error rates (>5%)
- Message queue backlog (>1000 messages)
- Memory usage >80%

### **Warning Alerts**
- Response time >1 second
- Delivery success rate <95%
- Cache hit rate <70%
- Disk usage >70%

### **Alert Configuration**
```yaml
# Prometheus AlertManager rules
groups:
- name: shared-messaging
  rules:
  - alert: SharedMessagingDown
    expr: up{job="shared-messaging"} == 0
    for: 5m
    labels:
      severity: critical
    annotations:
      summary: "Shared Messaging service is down"
      
  - alert: HighErrorRate
    expr: rate(http_requests_total{status=~"5.."}[5m]) > 0.05
    for: 2m
    labels:
      severity: warning
    annotations:
      summary: "High error rate in Shared Messaging"
```

## 🛠️ Troubleshooting Guide

### **Common Issues**

#### **Service Won't Start**

**Symptoms:**
- Pod in `CrashLoopBackOff` state
- Application health check fails
- Service not responding

**Diagnosis:**
```bash
# Check pod events
kubectl describe pod -l app=shared-messaging -n gogidx-infrastructure

# Review application logs
kubectl logs deployment/shared-messaging -n gogidix-infrastructure --previous

# Verify configuration
kubectl get configmap shared-messaging-config -n gogidix-infrastructure -o yaml
```

**Resolution:**
1. Verify database connectivity
2. Check Redis availability
3. Validate Kafka broker connectivity
4. Review resource limits and requests
5. Check secret configurations

#### **Database Connection Issues**

**Symptoms:**
- Database health check failing
- Connection timeout errors
- SQL exceptions in logs

**Diagnosis:**
```bash
# Test database connectivity
kubectl run test-db --rm -i --tty --image=postgres:15 -- bash
psql -h postgres-service -U postgres -d gogidix_messaging

# Check connection pool metrics
curl "https://api.gogidix.com/api/messaging/actuator/metrics/hikaricp.connections"
```

**Resolution:**
1. Verify database credentials in secrets
2. Check database server availability
3. Validate connection pool configuration
4. Review network policies
5. Check database resource utilization

#### **Message Delivery Failures**

**Symptoms:**
- Messages stuck in `PENDING` status
- High number of `FAILED` messages
- Delivery timeout errors

**Diagnosis:**
```bash
# Check message statistics
curl "https://api.gogidix.com/api/messaging/messages/statistics"

# Review failed messages
curl "https://api.gogidix.com/api/messaging/messages?status=FAILED&limit=10"

# Check Kafka connectivity
kubectl logs deployment/shared-messaging -n gogidix-infrastructure | grep -i kafka
```

**Resolution:**
1. Verify external API credentials
2. Check rate limiting configuration
3. Review retry logic settings
4. Validate external service availability
5. Check network connectivity to delivery providers

#### **High Memory Usage**

**Symptoms:**
- Pod memory usage >80%
- OutOfMemoryError in logs
- Pod restarts due to memory limits

**Diagnosis:**
```bash
# Check pod resource usage
kubectl top pod -l app=shared-messaging -n gogidix-infrastructure

# Review JVM metrics
curl "https://api.gogidix.com/api/messaging/actuator/metrics/jvm.memory.used"

# Check memory configuration
kubectl describe pod -l app=shared-messaging -n gogidix-infrastructure
```

**Resolution:**
1. Increase memory limits in deployment
2. Tune JVM heap settings
3. Review cache configuration
4. Check for memory leaks
5. Optimize database connection pool

#### **Performance Degradation**

**Symptoms:**
- High response times
- Increased CPU usage
- Database query slowness

**Diagnosis:**
```bash
# Check response time metrics
curl "https://api.gogidix.com/api/messaging/actuator/metrics/http.server.requests"

# Review database performance
kubectl exec -it postgres-pod -- psql -U postgres -c "SELECT * FROM pg_stat_activity;"

# Check cache performance
redis-cli -h redis-service info stats
```

**Resolution:**
1. Scale horizontally by increasing replicas
2. Optimize database queries and indexes
3. Tune cache configuration
4. Review async processing settings
5. Check for resource contention

## 📈 Scaling Operations

### **Horizontal Scaling**
```bash
# Scale deployment replicas
kubectl scale deployment shared-messaging --replicas=5 -n gogidix-infrastructure

# Configure Horizontal Pod Autoscaler
kubectl apply -f - <<EOF
apiVersion: autoscaling/v2
kind: HorizontalPodAutoscaler
metadata:
  name: shared-messaging-hpa
  namespace: gogidix-infrastructure
spec:
  scaleTargetRef:
    apiVersion: apps/v1
    kind: Deployment
    name: shared-messaging
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
EOF
```

### **Vertical Scaling**
```bash
# Update resource limits
kubectl patch deployment shared-messaging -n gogidix-infrastructure -p '
{
  "spec": {
    "template": {
      "spec": {
        "containers": [{
          "name": "shared-messaging",
          "resources": {
            "limits": {
              "cpu": "1000m",
              "memory": "1Gi"
            },
            "requests": {
              "cpu": "500m",
              "memory": "512Mi"
            }
          }
        }]
      }
    }
  }
}'
```

## 🔄 Backup and Recovery

### **Database Backup**
```bash
# Create database backup
kubectl exec -it postgres-pod -- pg_dump -U postgres gogidix_messaging > backup-$(date +%Y%m%d).sql

# Automated backup script
#!/bin/bash
DATE=$(date +%Y%m%d_%H%M%S)
kubectl exec postgres-pod -- pg_dump -U postgres gogidx_messaging | \
  gzip > backups/shared-messaging-${DATE}.sql.gz
```

### **Configuration Backup**
```bash
# Backup Kubernetes configurations
kubectl get configmap,secret,deployment,service -n gogidix-infrastructure -o yaml > \
  backup/shared-messaging-k8s-${DATE}.yaml
```

### **Recovery Procedures**
```bash
# Database recovery
kubectl exec -i postgres-pod -- psql -U postgres -d gogidix_messaging < backup-20250814.sql

# Configuration recovery
kubectl apply -f backup/shared-messaging-k8s-20250814.yaml
```

## 🔒 Security Operations

### **Security Monitoring**
```bash
# Check for security vulnerabilities
trivy image gogidx/shared-messaging:latest

# Review security events
kubectl get events -n gogidix-infrastructure --field-selector type=Warning

# Audit API access
kubectl logs deployment/shared-messaging -n gogidix-infrastructure | grep -i "unauthorized\|forbidden"
```

### **Security Hardening**
```bash
# Update base image
docker pull eclipse-temurin:17-jre-alpine
docker build -t gogidix/shared-messaging:latest .

# Review security policies
kubectl get networkpolicies -n gogidix-infrastructure
kubectl get podsecuritypolicies
```

### **Certificate Management**
```bash
# Check certificate expiry
kubectl describe secret shared-messaging-tls -n gogidix-infrastructure

# Renew certificates
cert-manager renew shared-messaging-cert
```

## 📋 Maintenance Operations

### **Regular Maintenance Tasks**

#### **Daily Tasks**
- Monitor application health and metrics
- Review error logs and alerts
- Check resource utilization
- Verify backup completion

#### **Weekly Tasks**
- Analyze performance trends
- Review security vulnerabilities
- Update documentation
- Cleanup old log files

#### **Monthly Tasks**
- Security patching and updates
- Capacity planning review
- Disaster recovery testing
- Performance optimization review

### **Update Procedures**
```bash
# Rolling update deployment
kubectl set image deployment/shared-messaging \
  shared-messaging=gogidix/shared-messaging:v1.0.1 \
  -n gogidix-infrastructure

# Monitor rollout
kubectl rollout status deployment/shared-messaging -n gogidix-infrastructure

# Rollback if needed
kubectl rollout undo deployment/shared-messaging -n gogidix-infrastructure
```

### **Version Management**
```bash
# Tag releases
git tag -a v1.0.1 -m "Release version 1.0.1"
docker tag gogidix/shared-messaging:latest gogidix/shared-messaging:v1.0.1

# Deploy specific version
kubectl set image deployment/shared-messaging \
  shared-messaging=gogidx/shared-messaging:v1.0.1 \
  -n gogidix-infrastructure
```

## 📞 Support and Escalation

### **Support Contacts**
- **Level 1 Support**: operations-team@gogidix.com
- **Level 2 Support**: platform-team@gogidix.com
- **Level 3 Support**: architecture-team@gogidix.com

### **Escalation Procedures**
1. **P1 (Critical)**: Service completely down - Immediate escalation
2. **P2 (High)**: Significant performance degradation - 1 hour response
3. **P3 (Medium)**: Minor issues - 4 hour response
4. **P4 (Low)**: Enhancement requests - 24 hour response

### **Emergency Response**
```bash
# Emergency rollback
kubectl rollout undo deployment/shared-messaging -n gogidx-infrastructure

# Emergency scaling
kubectl scale deployment shared-messaging --replicas=10 -n gogidix-infrastructure

# Emergency maintenance mode
kubectl patch deployment shared-messaging -n gogidix-infrastructure -p '
{
  "spec": {
    "template": {
      "metadata": {
        "annotations": {
          "maintenance.gogidix.com/mode": "enabled"
        }
      }
    }
  }
}'
```

## 📚 Additional Resources

### **Documentation Links**
- [Architecture Guide](../architecture/README.md)
- [Setup Guide](../setup/README.md)
- [API Documentation](../../api-docs/openapi.yaml)
- [Troubleshooting FAQ](./troubleshooting-faq.md)

### **External Resources**
- [Spring Boot Actuator Documentation](https://docs.spring.io/spring-boot/docs/3.1.5/reference/actuator/)
- [Kubernetes Operations Guide](https://kubernetes.io/docs/concepts/cluster-administration/)
- [Prometheus Monitoring](https://prometheus.io/docs/introduction/overview/)

### **Tools and Scripts**
- Health check scripts: `./docker/health-check.sh`
- Smoke tests: `./scripts/smoke-test.sh`
- Deployment scripts: `./scripts/deploy.sh`
- Monitoring dashboards: Available in Grafana

---

**Last Updated**: 2025-08-14  
**Document Version**: 1.0  
**Maintained By**: GOGIDIX Platform Operations Team
