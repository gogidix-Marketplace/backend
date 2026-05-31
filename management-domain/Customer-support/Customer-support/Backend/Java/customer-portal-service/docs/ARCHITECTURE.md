# Customer Portal Service - Architecture Documentation

## Overview

The Customer Portal Service is a Spring Boot microservice that provides self-service capabilities for customers to manage their profiles and view their ticket history within the Customer Support domain.

## Technology Stack

- **Framework**: Spring Boot 3.2.0
- **Java Version**: 17
- **Database**: MongoDB
- **Build Tool**: Maven
- **API Documentation**: OpenAPI 3.0 (SpringDoc)

## Architecture Pattern

The service follows **Hexagonal Architecture** (Ports and Adapters):

```mermaid
graph TB
    subgraph "Interface Layer"
        Controller[CustomerPortalController]
    end

    subgraph "Application Layer"
        Service[CustomerPortalService]
        Mapper1[CustomerProfileMapper]
        Mapper2[TicketHistoryMapper]
    end

    subgraph "Domain Layer"
        Model1[CustomerProfile]
        Model2[TicketHistory]
        Repo1[CustomerProfileRepository]
        Repo2[TicketHistoryRepository]
    end

    subgraph "Infrastructure Layer"
        MongoConfig[MongoConfig]
        WebConfig[WebConfig]
    end

    subgraph "Shared Layer"
        RequestContext[RequestContext]
        RequestContextHolder[RequestContextHolder]
    end

    Controller --> Service
    Service --> Mapper1
    Service --> Mapper2
    Service --> Repo1
    Service --> Repo2
    Service --> RequestContextHolder
```

## Layer Structure

### 1. Interface Layer (`interfaces.rest`)

**CustomerPortalController**
- REST API endpoints for customer profile management
- Ticket history query endpoints
- Request validation

### 2. Application Layer (`application`)

#### Service (`application.service`)
- **CustomerPortalService**: Business logic for profiles and ticket history

#### Mapper (`application.mapper`)
- **CustomerProfileMapper**: Profile DTO to Entity mapping
- **TicketHistoryMapper**: Ticket history DTO to Entity mapping

### 3. Domain Layer (`domain`)

#### Models (`domain.model`)
- **CustomerProfile**: Customer profile entity with preferences
- **TicketHistory**: Historical ticket data
- **BaseEntity**: Common entity fields

#### Repositories (`domain.repository`)
- **CustomerProfileRepository**: Profile data access
- **TicketHistoryRepository**: Ticket history data access

## Data Model

### CustomerProfile

```mermaid
erDiagram
    CustomerProfile ||--o| Address : contains
    CustomerProfile ||--o| CustomerPreferences : contains
    CustomerProfile {
        string id PK
        string tenantId FK
        string customerId UK
        string userId
        string firstName
        string lastName
        string email
        string phone
        string secondaryPhone
        string companyName
        string companyId
        string customerType
        string tier
        string preferredLanguage
        string timezone
        string country
        list communicationChannels
        list tags
        map customFields
        boolean isActive
        instant lastLoginAt
        instant accountCreatedAt
        instant createdAt
        instant updatedAt
    }

    Address {
        string street
        string city
        string state
        string postalCode
        string country
    }

    CustomerPreferences {
        boolean emailNotifications
        boolean smsNotifications
        boolean phoneNotifications
        boolean pushNotifications
        string preferredContactMethod
        map notificationPreferences
    }
```

### TicketHistory

```mermaid
erDiagram
    TicketHistory ||--o| AttachmentInfo : contains
    TicketHistory {
        string id PK
        string customerId FK
        string ticketId
        string ticketNumber
        string title
        string description
        string status
        string priority
        string category
        string channel
        instant createdAt
        instant resolvedAt
        instant closedAt
        string assignedAgentId
        string assignedAgentName
        int satisfactionRating
        string feedback
        string resolutionNotes
        list tags
        list attachments
    }

    AttachmentInfo {
        string fileName
        string fileUrl
        instant uploadedAt
    }
```

## Multi-Tenancy

The service supports multi-tenancy through tenant-scoped data isolation:

1. **Tenant Context**: `RequestContextHolder` maintains the current tenant ID
2. **Data Isolation**: All profile queries filter by tenant ID
3. **Security**: Cross-tenant data access is prevented

## API Flow

```mermaid
sequenceDiagram
    participant Client
    participant Controller
    participant Service
    participant Repository
    participant MongoDB

    Client->>Controller: HTTP Request
    Controller->>Service: Business method call
    Service->>RequestContextHolder: Get tenant ID
    Service->>Repository: Query with tenant filter
    Repository->>MongoDB: MongoDB query
    MongoDB-->>Repository: Result
    Repository-->>Service: Domain entities
    Service->>Service: Map to DTOs
    Service-->>Controller: Response DTOs
    Controller-->>Client: HTTP Response
```

## Key Features

1. **Customer Profile Management**
   - Create, update, delete customer profiles
   - Profile lookup by customer ID or profile ID
   - Support for custom fields and tags

2. **Ticket History**
   - Complete ticket history for customers
   - Ticket count queries with status filtering
   - Chronological history ordering

3. **Customer Preferences**
   - Notification preferences
   - Communication channel preferences
   - Preferred contact method

## Security Considerations

1. **Tenant Isolation**: All queries scoped to tenant ID
2. **Input Validation**: Bean validation on all request DTOs
3. **Email Validation**: Email format validation
4. **Data Privacy**: Sensitive data handling compliance

## Testing Strategy

### Unit Tests
- Service layer tests with mocked repositories
- Mapper tests for DTO conversions
- Domain model tests

### Test Coverage Goal
- Minimum 80% code coverage
- 100% coverage for critical business logic
