# Docker Compose E-Commerce Deployment Guide

## Overview

This document provides comprehensive instructions for deploying the Gogidix E-Commerce Core microservices using Docker Compose.

## Prerequisites

- Docker Engine 20.10 or later
- Docker Compose 2.0 or later
- At least 8GB RAM available for Docker
- At least 20GB disk space available

## Quick Start

```bash
# Navigate to the Backend/Java directory
cd C:\Users\TEMP.LAPTOP-1QDBFFCA\Desktop\Gogidix-ecosystem\x-gogidix-domain\shared-business-infrastructure\shared-ecommerce-core\Backend\Java

# Start all services
docker-compose -f docker-compose-ecommerce.yml up -d

# View logs
docker-compose -f docker-compose-ecommerce.yml logs -f

# Check service status
docker-compose -f docker-compose-ecommerce.yml ps

# Stop all services
docker-compose -f docker-compose-ecommerce.yml down
```

## Infrastructure Services

### MongoDB Replica Set

- **Primary**: `mongodb-primary` on port 27017
- **Secondary-1**: `mongodb-secondary-1` on port 27018
- **Secondary-2**: `mongodb-secondary-2` on port 27019

Connection string:
```
mongodb://admin:admin123@localhost:27017,localhost:27018,localhost:27019/?authSource=admin&replicaSet=rs0
```

### Kafka

- **Kafka Broker**: Port 9092 (internal), 9093 (external)
- **Zookeeper**: Port 2181
- **Kafka UI**: Port 8080 (http://localhost:8080)

### Eureka Server

- **Service Discovery**: Port 8761
- **Dashboard**: http://localhost:8761

## Microservices Port Mapping

### Core E-Commerce Services

| Service | Port | Description |
|---------|------|-------------|
| cart-service | 8306 | Shopping cart management |
| order-service | 8050 | Order processing |
| checkout-service | 8051 | Checkout flow orchestration |
| payment-service | 8052 | Payment processing |
| payment-gateway-service | 8520 | Payment gateway integration |
| payment-method-service | 8521 | Payment method management |
| catalog-service | 8053 | Product catalog |
| category-service | 8057 | Product categories |
| bundle-service | 8075 | Product bundles |
| variant-service | 8076 | Product variants |
| pricing-service | 8066 | Dynamic pricing |

### Customer & Influencer Services

| Service | Port | Description |
|---------|------|-------------|
| customer-service | 8056 | Customer management |
| customer-profile-service | 8560 | Customer profiles |
| influencer-service | 8054 | Influencer management |
| commission-service | 8055 | Commission calculations |
| affiliate-service | 8540 | Affiliate program |
| influencer-dashboard-service | 8541 | Influencer dashboard |

### Fulfillment & Delivery

| Service | Port | Description |
|---------|------|-------------|
| fulfillment-service | 8058 | Order fulfillment |
| fulfillment-orchestrator-service | 8580 | Fulfillment orchestration |
| delivery-service | 8059 | Delivery management |
| shipping-service | 8581 | Shipping management |
| realtime-tracking-service | 8060 | Real-time package tracking |
| order-tracking-service | 8582 | Order tracking |

### Vendor & Wholesaler

| Service | Port | Description |
|---------|------|-------------|
| vendor-service | 8062 | Vendor management |
| vendor-dashboard-service | 8590 | Vendor dashboard |
| vendor-analytics-service | 8591 | Vendor analytics |
| onboarding-service | 8592 | Vendor onboarding |
| dropship-service | 8593 | Dropship management |
| wholesaler-service | 8063 | Wholesaler management |
| wholesaler-dashboard-service | 8594 | Wholesaler dashboard |
| bulk-pricing-service | 8595 | Bulk pricing |
| contract-management-service | 8596 | Contract management |

### Marketing & Promotions

| Service | Port | Description |
|---------|------|-------------|
| discount-service | 8064 | Discount management |
| promotion-service | 8065 | Promotion campaigns |
| coupon-service | 8078 | Coupon management |

### Communication & Notifications

| Service | Port | Description |
|---------|------|-------------|
| email-service | 8067 | Email notifications |
| sms-service | 8068 | SMS notifications |
| push-notification-service | 8069 | Push notifications |
| notification-service | 8070 | Notification aggregator |
| communication-service | 8600 | Communication hub |

### Marketplace

| Service | Port | Description |
|---------|------|-------------|
| marketplace-service | 8071 | Main marketplace |
| marketplace-search-service | 8601 | Marketplace search |
| sme-marketplace-service | 8602 | SME marketplace |
| wholesale-marketplace-service | 8603 | Wholesale marketplace |

### Search & Discovery

| Service | Port | Description |
|---------|------|-------------|
| search-service | 8072 | Product search |
| wishlist-service | 8073 | Customer wishlists |
| inventory-service | 8074 | Inventory management |
| inventory-sync-service | 8610 | Inventory synchronization |

### Loyalty & Rewards

| Service | Port | Description |
|---------|------|-------------|
| loyalty-service | 8077 | Loyalty program |
| reward-service | 8620 | Reward management |
| gift-card-service | 8621 | Gift card management |
| store-credit-service | 8622 | Store credit |

### Order Management

| Service | Port | Description |
|---------|------|-------------|
| exchange-service | 8080 | Order exchanges |
| returns-service | 8081 | Returns management |

### Public API Services

| Service | Port | Description |
|---------|------|-------------|
| public-cart-service | 8082 | Public cart API |
| public-catalog-service | 8083 | Public catalog API |
| public-search-service | 8084 | Public search API |

### Tenant Management

| Service | Port | Description |
|---------|------|-------------|
| tenant-config-service | 8085 | Tenant configuration |
| tenant-hierarchy-service | 8086 | Tenant hierarchy |

### AI & Automation

| Service | Port | Description |
|---------|------|-------------|
| ai-listing-service | 8500 | AI-powered listing |
| bulk-listing-service | 8501 | Bulk listing operations |
| social-integration-service | 8502 | Social media integration |

### Procurement

| Service | Port | Description |
|---------|------|-------------|
| requisition-service | 8630 | Purchase requisitions |
| budget-service | 8631 | Budget management |
| approval-workflow-service | 8632 | Approval workflows |
| reconciliation-service | 8633 | Payment reconciliation |
| hq-dashboard-service | 8634 | HQ dashboard |
| branch-dashboard-service | 8635 | Branch dashboard |

### Integration Services

| Service | Port | Description |
|---------|------|-------------|
| air-freight-integration-service | 8640 | Air freight integration |
| ocean-shipping-integration-service | 8641 | Ocean shipping integration |
| courier-integration-service | 8642 | Courier integration |
| haulage-integration-service | 8643 | Haulage integration |
| warehouse-integration-service | 8644 | Warehouse integration |

### Analytics

| Service | Port | Description |
|---------|------|-------------|
| analytics-service | 8061 | Business analytics |

## Health Checks

All services include health checks. Check service health:

```bash
# Check all services
docker-compose -f docker-compose-ecommerce.yml ps

# Check specific service logs
docker-compose -f docker-compose-ecommerce.yml logs cart-service

# Check health endpoint
curl http://localhost:8306/actuator/health
```

## Environment Variables

### Common Environment Variables

All services use these common environment variables:

- `SPRING_PROFILES_ACTIVE`: Spring profile (docker)
- `SPRING_APPLICATION_NAME`: Service name
- `SERVER_PORT`: Service port
- `MONGODB_URI`: MongoDB connection string with replica set
- `KAFKA_BROKERS`: Kafka broker addresses
- `EUREKA_SERVER_URL`: Eureka server URL
- `EUREKA_CLIENT_SERVICE_URL_DEFAULTZONE`: Service discovery URL
- `MANAGEMENT_ENDPOINTS_WEB_EXPOSURE_INCLUDE`: Management endpoints

### Service-Specific URLs

Services communicate via internal service names. For example:
- `CART_SERVICE_URL: http://cart-service:8306`
- `ORDER_SERVICE_URL: http://order-service:8050`
- `CATALOG_SERVICE_URL: http://catalog-service:8053`

## Networks

All services communicate via the `ecommerce-network` Docker network:

- **Network Name**: ecommerce-network
- **Subnet**: 172.28.0.0/16
- **Driver**: bridge

## Volumes

Persistent data is stored in Docker volumes:

### MongoDB Volumes
- `mongodb-primary-data`: Primary node data
- `mongodb-secondary-1-data`: Secondary node 1 data
- `mongodb-secondary-2-data`: Secondary node 2 data
- `mongodb-config`: MongoDB configuration

### Kafka Volumes
- `kafka-data`: Kafka data
- `zookeeper-data`: Zookeeper data
- `zookeeper-logs`: Zookeeper logs

## Troubleshooting

### Service Not Starting

```bash
# Check logs
docker-compose -f docker-compose-ecommerce.yml logs <service-name>

# Remove and recreate
docker-compose -f docker-compose-ecommerce.yml down
docker-compose -f docker-compose-ecommerce.yml up -d
```

### MongoDB Connection Issues

```bash
# Check MongoDB status
docker exec mongodb-primary mongosh -u admin -p admin123 --authenticationDatabase admin --eval 'rs.status()'

# Reinitialize replica set
docker exec mongodb-init bash /mongodb-init.sh
```

### Kafka Connection Issues

```bash
# Check Kafka logs
docker-compose -f docker-compose-ecommerce.yml logs kafka

# Verify Zookeeper
docker-compose -f docker-compose-ecommerce.yml logs zookeeper
```

### Memory Issues

If services are failing due to memory:

1. Increase Docker memory limit in Docker Desktop settings
2. Reduce the number of running services
3. Adjust JVM settings in service environment variables

## Scaling Services

To scale a service (e.g., cart-service):

```bash
docker-compose -f docker-compose-ecommerce.yml up -d --scale cart-service=3
```

Note: MongoDB replica set and Kafka should not be scaled.

## Monitoring

### Service Discovery

View all registered services in Eureka:
```
http://localhost:8761
```

### Kafka Topics

View and manage Kafka topics:
```
http://localhost:8080
```

### Service Health

Check individual service health:
```
http://localhost:<port>/actuator/health
```

## Production Considerations

1. **Security**: Change default passwords in production
2. **TLS**: Enable TLS for all service communications
3. **Secrets Management**: Use Docker secrets or external vault
4. **Resource Limits**: Set appropriate CPU and memory limits
5. **Backup**: Implement regular MongoDB backups
6. **Monitoring**: Add centralized logging (ELK, Splunk)
7. **Metrics**: Configure Prometheus exporters
8. **Tracing**: Add distributed tracing (Jaeger, Zipkin)

## Clean Up

```bash
# Stop and remove all containers
docker-compose -f docker-compose-ecommerce.yml down

# Remove volumes (WARNING: This deletes all data)
docker-compose -f docker-compose-ecommerce.yml down -v

# Remove orphaned containers
docker-compose -f docker-compose-ecommerce.yml down --remove-orphans
```

## Support

For issues or questions, refer to:
- Project documentation in the shared-ecommerce-core directory
- Service-specific README files in each service directory
- Docker logs for troubleshooting
