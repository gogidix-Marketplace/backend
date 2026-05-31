# Deal Management Service - Architecture Documentation

## Overview

The Deal Management Service is a hexagonal architecture-based multi-tenant SaaS service responsible for managing sales deals/opportunities throughout their lifecycle from initial lead to closed-won or lost.

## Architecture Principles

The service follows **Hexagonal Architecture** (Ports and Adapters) with clear separation of concerns:

1. **Domain Layer**: Core business logic, entities (Deal, DealActivity, DealProduct, Competitor)
2. **Application Layer**: Use cases and application services
3. **Infrastructure Layer**: External integrations (MongoDB, Kafka)
4. **Interface Layer**: REST API endpoints

## Domain Model

### Deal Entity

The `Deal` entity represents a sales opportunity in the pipeline.

**Key Properties:**
- `dealId`: Unique identifier
- `tenantId`: Multi-tenant isolation
- `dealName`: Name of the deal
- `dealCode`: Auto-generated deal code (DL-XXXXXXXX)
- `stage`: Deal stage (LEAD, QUALIFIED, PROPOSAL, NEGOTIATION, VERBAL_COMMIT, CLOSED_WON, CLOSED_LOST)
- `probability`: Win probability (0-100%)
- `amount`: Deal value
- `weightedAmount`: Amount × Probability (for forecasting)
- `accountId`: Associated account
- `contactId`: Primary contact
- `ownerId`: Deal owner
- `status`: Deal status (OPEN, WON, LOST, ABANDONED, ON_HOLD)
- `expectedCloseDate`: Expected closing date
- `actualCloseDate`: Actual closing date

**Business Rules:**
- Weighted amount automatically calculated: amount × probability / 100
- Cannot modify closed deals
- Advancing to CLOSED_WON automatically marks as won
- Product changes recalculate deal amount
- Approval workflow for large deals

### DealProduct Entity

Products included in a deal:
- `productId`: Product identifier
- `productName`: Product name
- `quantity`: Quantity
- `unitPrice`: Price per unit
- `discount`: Discount amount
- `totalPrice`: Calculated total

### Competitor Entity

Competitors for this deal:
- `competitorId`: Competitor identifier
- `competitorName`: Competitor name
- `strength`: Their strengths
- `weakness`: Their weaknesses
- `threatLevel`: Threat level

## Application Services

### DealCommandService

Handles all write operations:
- `create()`: Create new deal
- `update()`: Update deal details
- `advanceStage()`: Move to next pipeline stage
- `regressStage()`: Move to previous stage
- `markAsWon()`: Close deal as won
- `markAsLost()`: Close deal as lost
- `addProduct()`: Add product to deal
- `removeProduct()`: Remove product
- `addCompetitor()`: Add competitor
- `requestApproval()`: Initiate approval workflow
- `approve()` / `rejectApproval()`: Approval decisions

### DealQueryService

Handles all read operations:
- `getById()`: Get deal by ID
- `getByOwner()`: Get deals by owner
- `getByStage()`: Filter by stage
- `getByAccount()`: Get deals for account
- `getPipelineValue()`: Calculate total pipeline value
- `getForecast()`: Weighted forecast calculation
- `search()`: Full-text search

## Infrastructure Components

### Persistence Layer

**MongoDB** Collections:
- `deals`: Deal documents
- `dealActivities`: Activity log
- `dealProducts`: Product line items
- `competitors`: Competitor information

**Indexes:**
- Compound index on (tenantId, dealId)
- Indexes on ownerId, accountId, stage, status

### Event Publishing

**Kafka Events:**
- DEAL_CREATED
- DEAL_STAGE_CHANGED
- DEAL_WON
- DEAL_LOST

## Multi-Tenancy

The service supports multi-tenancy through:
- `tenantId` field in all entities
- Request context interceptor
- Repository-level filtering

## Deal Stages

| Stage | Order | Default Probability | Description |
|-------|-------|-------------------|-------------|
| LEAD | 1 | 10% | Initial opportunity |
| QUALIFIED | 2 | 20% | Qualified opportunity |
| PROPOSAL | 3 | 40% | Proposal sent |
| NEGOTIATION | 4 | 60% | In negotiation |
| VERBAL_COMMIT | 5 | 80% | Verbal commitment received |
| CLOSED_WON | 6 | 100% | Deal won |
| CLOSED_LOST | 7 | 0% | Deal lost |

## Technology Stack

- **Language**: Java 17
- **Framework**: Spring Boot 3.1.5
- **Database**: MongoDB
- **Messaging**: Apache Kafka
- **API Documentation**: SpringDoc OpenAPI
- **Testing**: JUnit 5, Mockito, Testcontainers
