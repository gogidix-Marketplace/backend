# Warehousing Domain - Architecture Documentation

## Overview

The Shared Warehousing Core domain provides flexible, multi-tenant warehousing and inventory management capabilities for the Gogidix ecosystem. It serves diverse business needs including B2B distribution, B2C self-storage, and managed warehousing through a unified platform.

## Table of Contents

- [System Architecture](#system-architecture)
- [Domain-Driven Design Layers](#domain-driven-design-layers)
- [Service Categories](#service-categories)
- [Service Interactions](#service-interactions)
- [Data Models](#data-models)
- [Technology Stack](#technology-stack)
- [Deployment Architecture](#deployment-architecture)

## System Architecture

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                           PUBLIC API GATEWAY                                │
│              (Kong / AWS API Gateway / Azure API Gateway)                   │
└─────────────────────────────────────────────────────────────────────────────┘
                                      │
                                      ▼
┌─────────────────────────────────────────────────────────────────────────────┐
│                           FRONTEND APPLICATIONS                             │
│  ┌──────────────────────┐  ┌──────────────────────┐  ┌─────────────────┐  │
│  │  Partners Dashboard  │  │ Private Storage      │  │  Public Market  │  │
│  │    (Next.js Web)     │  │    Dashboard         │  │     Place       │  │
│  │                      │  │    (Next.js Web)     │  │                 │  │
│  └──────────────────────┘  └──────────────────────┘  └─────────────────┘  │
│  ┌──────────────────────┐  ┌──────────────────────┐                        │
│  │  Warehouse Staff     │  │  Vendor Self Storage │                        │
│  │    App (React Native)│  │    App (React Native)│                        │
│  └──────────────────────┘  └──────────────────────┘                        │
└─────────────────────────────────────────────────────────────────────────────┘
                                      │
                                      ▼
┌─────────────────────────────────────────────────────────────────────────────┐
│                        SERVICE MESH (Istio / Linkerd)                        │
└─────────────────────────────────────────────────────────────────────────────┘
                                      │
              ┌───────────────────────┼───────────────────────┐
              ▼                       ▼                       ▼
┌─────────────────────┐  ┌─────────────────────┐  ┌─────────────────────┐
│   INVENTORY DOMAIN  │  │  FULFILLMENT DOMAIN │  │   STORAGE DOMAIN    │
│ ┌─────────────────┐ │  │ ┌─────────────────┐ │  │ ┌─────────────────┐ │
│ │inventory-core   │ │  │ │fulfillment-core │ │  │ │space-service    │ │
│ │stock-service    │ │  │ │picking-service  │ │  │ │pricing-service  │ │
│ │location-service │ │  │ │packing-service  │ │  │ │access-service   │ │
│ │serialization    │ │  │ │shipping-service │ │  │ │bin-service      │ │
│ │batch-service    │ │  │ │returns-service  │ │  │ │shelf-service    │ │
│ │expiration       │ │  │ │quality-service  │ │  │ │zone-service     │ │
│ │reorder-service  │ │  │ └─────────────────┘ │  │ │warehouse-config │ │
│ │cycle-counting   │ │  │                     │  │ └─────────────────┘ │
│ │availability     │ │  │                     │  │                     │
│ └─────────────────┘ │  │                     │  │                     │
└─────────────────────┘  └─────────────────────┘  └─────────────────────┘
              │                       │                       │
              └───────────────────────┼───────────────────────┘
                                      ▼
┌─────────────────────────────────────────────────────────────────────────────┐
│                         INFRASTRUCTURE SERVICES                              │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐   │
│  │   MongoDB    │  │    Kafka     │  │    Redis     │  │   Prometheus │   │
│  │   Cluster    │  │   Cluster    │  │   Cluster    │  │   + Grafana  │   │
│  └──────────────┘  └──────────────┘  └──────────────┘  └──────────────┘   │
└─────────────────────────────────────────────────────────────────────────────┘
```

## Domain-Driven Design Layers

Each service follows the Domain-Driven Design (DDD) layered architecture:

```
service-name/
├── src/main/java/com/gogidix/shared/warehousing/
│   ├── ServiceNameApplication.java          # Application entry point
│   │
│   ├── interfaces/                          # Interface Layer
│   │   └── rest/                            # REST Controllers
│   │       ├── InventoryController.java
│   │       ├── dto/                         # Request/Response DTOs
│   │       └── mappers/                     # DTO Mappers
│   │
│   ├── application/                         # Application Layer
│   │   ├── command/                         # Commands (Input DTOs)
│   │   ├── query/                           # Queries (Input DTOs)
│   │   ├── service/                         # Application Services
│   │   │   ├── InventoryService.java
│   │   │   └── StockAllocationService.java
│   │   └── handlers/                        # Command/Query Handlers
│   │
│   ├── domain/                              # Domain Layer
│   │   ├── entity/                          # Domain Entities (@Document)
│   │   │   ├── InventoryItem.java
│   │   │   └── StockMovement.java
│   │   ├── repository/                      # Repository Interfaces
│   │   │   └── InventoryItemRepository.java
│   │   ├── service/                         # Domain Services
│   │   │   └── DomainEventService.java
│   │   ├── events/                          # Domain Events
│   │   │   └── InventoryAdjustedEvent.java
│   │   └── valueobject/                     # Value Objects
│   │       ├── Quantity.java
│   │       └── Location.java
│   │
│   └── infrastructure/                      # Infrastructure Layer
│       ├── persistence/                     # MongoDB Implementation
│       │   └── mongo/
│       │       └── InventoryItemRepositoryImpl.java
│       ├── messaging/                       # Kafka Producers/Consumers
│       │   ├── producers/
│       │   │   └── InventoryEventProducer.java
│       │   └── consumers/
│       │       └── FulfillmentEventConsumer.java
│       └── config/                          # Configuration
│           ├── MongoDBConfig.java
│           └── KafkaConfig.java
│
└── src/test/java/                           # Tests
    ├── unit/                                # Unit Tests
    ├── integration/                         # Integration Tests
    └── e2e/                                 # End-to-End Tests
```

### Layer Responsibilities

| Layer | Responsibility | Dependencies |
|-------|---------------|--------------|
| **Interface** | Handle HTTP requests, validation, response formatting | Application |
| **Application** | Orchestrate use cases, command/query handling | Domain, Interface |
| **Domain** | Core business logic, entities, domain events | None (independent) |
| **Infrastructure** | External concerns (DB, messaging, APIs) | Domain, Application |

## Service Categories

### 1. Inventory Services (8 services)

| Service | Description | Key Features |
|---------|-------------|--------------|
| `inventory-core-service` | Core inventory management | Multi-tenant, CRUD, stock tracking |
| `stock-service` | Stock allocation and reservation | Real-time allocation, FIFO/FEFO |
| `location-service` | Storage location management | Hierarchy, capacity, geospatial |
| `self-storage-service` | Vendor location management | Geofencing, compliance tracking |
| `serialization-service` | Serialized item tracking | Individual item lifecycle, warranty |
| `batch-service` | Batch/lot management | Expiry tracking, FEFO picking |
| `expiration-service` | Expiration monitoring | Alerts, quarantine automation |
| `reorder-service` | Automated reordering | EOQ calculation, PO generation |

### 2. Storage Services (8 services)

| Service | Description | Key Features |
|---------|-------------|--------------|
| `space-service` | Storage space allocation | Availability, reservation |
| `space-allocation-service` | Optimization algorithms | Slot optimization, consolidation |
| `pricing-service` | Dynamic pricing engine | Size/duration-based, seasonal |
| `access-service` | Access control management | PIN codes, logging, time-based |
| `bin-service` | Bin management | Capacity tracking, assignment |
| `shelf-service` | Shelf management | Configuration, utilization |
| `zone-service` | Zone management | Classification, routing |
| `warehouse-config-service` | Warehouse configuration | Layout, rules, settings |

### 3. Fulfillment Services (6 services)

| Service | Description | Key Features |
|---------|-------------|--------------|
| `fulfillment-core-service` | Order orchestration | Workflow management, prioritization |
| `picking-service` | Picking optimization | Wave/batch picking, routing |
| `packing-service` | Packing station management | Box selection, labeling |
| `shipping-service` | Carrier integration | Rate shopping, label generation |
| `returns-service` | Returns processing | RMA, restocking, refunds |
| `quality-service` | Quality control | Inspections, defect tracking |

### 4. Inbound Services (3 services)

| Service | Description | Key Features |
|---------|-------------|--------------|
| `receiving-service` | Goods receiving | Dock scheduling, unloading |
| `receipt-service` | Receipt processing | GRN generation, discrepancy handling |
| `putaway-service` | Put-away optimization | Location suggestions, verification |

### 5. Outbound Services (3 services)

| Service | Description | Key Features |
|---------|-------------|--------------|
| `order-service` | Order processing | Order lifecycle, prioritization |
| `carrier-service` | Carrier management | Rate cards, performance tracking |
| `label-service` | Label generation | Carrier-specific formats |

### 6. Public API Services (3 services)

| Service | Description | Key Features |
|---------|-------------|--------------|
| `public-booking-service` | Storage booking | Availability checking, reservations |
| `public-availability-service` | Public availability API | Real-time availability display |
| `public-pricing-service` | Public pricing calculator | Quote generation |

### 7. Analytics Services (4 services)

| Service | Description | Key Features |
|---------|-------------|--------------|
| `inventory-analytics-service` | Inventory analytics | Turnover, ABC analysis |
| `fulfillment-analytics-service` | Fulfillment metrics | Efficiency, SLA compliance |
| `warehouse-analytics-service` | Warehouse metrics | Utilization, productivity |
| `reporting-service` | Custom reports | Scheduled reports, export |

### 8. Tenant Services (1 service)

| Service | Description | Key Features |
|---------|-------------|--------------|
| `tenant-config-service` | Multi-tenant configuration | Tenant settings, rules engine |

### 9. Vendor Services (1 service)

| Service | Description | Key Features |
|---------|-------------|--------------|
| `vendor-sync-service` | Mobile app synchronization | Offline sync, conflict resolution |

## Service Interactions

### Event-Driven Communication

Services communicate via Kafka topics:

```
┌────────────────────────────────────────────────────────────────────────────┐
│                              KAFKA TOPICS                                 │
├────────────────────────────────────────────────────────────────────────────┤
│  inventory.adjusted        →  stock, fulfillment, analytics                │
│  stock.allocated           →  fulfillment, inventory                        │
│  stock.reserved            →  fulfillment, inventory                        │
│  order.created             →  inventory, fulfillment, picking              │
│  picking.completed         →  packing, fulfillment                          │
│  packing.completed         →  shipping, fulfillment                        │
│  shipment.created          →  order, fulfillment, tracking                  │
│  item.received             →  inventory, putaway                            │
│  space.allocated           →  pricing, access, analytics                   │
│  tenant.config.changed     →  all services (broadcast)                      │
└────────────────────────────────────────────────────────────────────────────┘
```

### Synchronous API Calls

Key synchronous dependencies:

```
fulfillment-core → inventory-core (stock availability check)
fulfillment-core → location-service (pick location lookup)
shipping-service → carrier-service (carrier selection)
public-booking → space-service (space reservation)
public-booking → pricing-service (price calculation)
```

## Data Models

### Multi-Tenancy Pattern

All MongoDB collections include a `tenantId` field for isolation:

```java
@Document(collection = "inventory_items")
@CompoundIndex(def = "{'tenantId': 1, 'sku': 1}", unique = true)
public class InventoryItem {
    @Id
    private String id;

    @Indexed
    private String tenantId;  // Multi-tenant isolation

    @Indexed
    private String sku;

    private Integer quantity;
    private String locationId;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
```

### Key Collections

| Collection | Index Pattern | Description |
|------------|---------------|-------------|
| `inventory_items` | `(tenantId, sku)` unique | Inventory records |
| `stock_movements` | `(tenantId, itemId, timestamp)` | Transaction log |
| `storage_spaces` | `(tenantId, spaceCode)` unique | Storage units |
| `fulfillment_orders` | `(tenantId, orderNumber)` unique | Orders |
| `tenants` | `tenantId` unique | Tenant configuration |

## Technology Stack

### Backend Technologies

| Component | Technology | Version |
|-----------|-----------|---------|
| Language | Java | 17 |
| Framework | Spring Boot | 3.1.5 |
| Database | MongoDB | 6.0+ |
| Messaging | Apache Kafka | 3.5.0 |
| Cache | Redis | 7.0+ |
| API Docs | OpenAPI/Springdoc | - |
| Build Tool | Maven | 3.9+ |
| JVM Metrics | Micrometer + Prometheus | - |

### Frontend Technologies

| Application | Framework | Key Libraries |
|-------------|-----------|---------------|
| Partners Dashboard | Next.js 14 | Ant Design, React Query |
| Private Storage Dashboard | Next.js 14 | Tailwind CSS, Socket.io |
| Warehouse Staff App | React Native + Expo | Barcode Scanner, Redux |
| Vendor App | React Native + Expo | Camera, Maps, Secure Store |

### Infrastructure

| Component | Technology |
|-----------|-----------|
| Container Runtime | Docker |
| Orchestration | Kubernetes |
| Service Mesh | Istio |
| Ingress | NGINX / Kong |
| Monitoring | Prometheus + Grafana |
| Logging | ELK Stack |
| Tracing | Jaeger |
| Secrets | Vault / K8s Secrets |

## Deployment Architecture

### Kubernetes Namespace Structure

```
warehousing-core/                # Production
├── Services (30+ deployments)
├── ConfigMaps
├── Secrets
└── ServiceMonitors

warehousing-staging/             # Staging
└── (same structure)

warehousing-dev/                 # Development
└── (same structure)
```

### Service Resource Allocation

| Service Tier | CPU Request | CPU Limit | Memory Request | Memory Limit | Replicas |
|--------------|-------------|-----------|----------------|--------------|----------|
| Core Services | 250m | 500m | 256Mi | 512Mi | 3 |
| Analytics | 500m | 1000m | 512Mi | 1Gi | 2 |
| Public API | 500m | 1000m | 512Mi | 1Gi | 3 |

### Scaling Strategy

- **Horizontal Pod Autoscaler**: CPU-based (70% target)
- **Cluster Autoscaler**: Node-based scaling
- **Circuit Breakers**: Resilience4j pattern
- **Rate Limiting**: Per-tenant and per-API-key

## Security Architecture

### Authentication & Authorization

```
┌─────────────────────┐
│   Identity Provider │  (Keycloak / Auth0)
└─────────────────────┘
           │
           ▼
┌─────────────────────┐
│   API Gateway       │  (JWT Validation)
└─────────────────────┘
           │
           ▼
┌─────────────────────┐
│   Service Layer     │  (X-Tenant-ID header)
└─────────────────────┘
           │
           ▼
┌─────────────────────┐
│   Data Layer        │  (Row-Level Security)
└─────────────────────┘
```

### Network Security

- mTLS between services (Istio)
- Network policies restricting pod-to-pod communication
- API rate limiting per tenant
- Web Application Firewall (WAF) at edge

## Observability

### Metrics Collection

Each service exposes:
- JVM metrics (Micrometer)
- HTTP request metrics
- Business metrics (orders, picks, shipments)
- Custom domain metrics

### Logging

- Structured JSON logging
- Correlation IDs for request tracing
- Centralized log aggregation (ELK)

### Tracing

- Distributed tracing with Jaeger
- Trace context propagation
- Service dependency graph

## Disaster Recovery

### Backup Strategy

- MongoDB daily snapshots
- Config version control (Git)
- Infrastructure as Code

### High Availability

- Multi-region deployment
- Database replication sets
- Kafka cluster replication
- Graceful degradation patterns
