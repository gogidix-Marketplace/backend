# Territory Management Service - Architecture Documentation

## Overview

The Territory Management Service manages sales territories, assignments, and quotas for the Sales Department.

## Domain Model

### Territory Entity

- `territoryId`: Unique identifier
- `tenantId`: Multi-tenant isolation
- `territoryName`: Territory name
- `territoryType`: Type (GEOGRAPHIC, INDUSTRY, PRODUCT, CUSTOMER_SIZE)
- `boundary`: Geographic boundary (for geo territories)
- `description`: Territory description
- `isActive`: Active status
- `parentTerritoryId`: For hierarchical territories

### TerritoryAssignment Entity

- `assignmentId`: Unique identifier
- `territoryId`: Assigned territory
- `userId`: Assigned user
- `assignmentType`: Type (PRIMARY, SHARED, SPLIT)
- `effectiveDate`: When assignment starts
- `expiryDate`: When assignment ends
- `percentage`: Split percentage for shared territories

### Quota Entity

- `quotaId`: Unique identifier
- `territoryId`: Associated territory
- `userId`: User quota (if user-specific)
- `period`: Quota period (MONTHLY, QUARTERLY, YEARLY)
- `amount`: Quota amount
- `currency`: Currency code
- `type`: Type (REVENUE, DEALS, ACTIVITIES)

## Application Services

### TerritoryCommandService

- `createTerritory()`: Create new territory
- `updateTerritory()`: Update territory details
- `deleteTerritory()`: Delete territory
- `assignUser()`: Assign user to territory
- `unassignUser()`: Remove user assignment

### TerritoryQueryService

- `getTerritoryById()`: Get territory details
- `getTerritoriesByUser()`: Get user's territories
- `getUsersByTerritory()`: Get users in territory
- `checkOverlap()`: Check for territory overlaps

### QuotaCommandService

- `setQuota()`: Set quota for territory/user
- `adjustQuota()`: Adjust existing quota
- `distributeQuota()`: Distribute quota among team

### QuotaQueryService

- `getQuota()`: Get quota details
- `getQuotaProgress()`: Get quota vs actual progress
- `getTeamQuotaStatus()`: Get team quota overview

## Technology Stack

- **Language**: Java 17
- **Framework**: Spring Boot 3.1.5
- **Database**: MongoDB, Redis
- **Messaging**: Apache Kafka
- **API Documentation**: SpringDoc OpenAPI
