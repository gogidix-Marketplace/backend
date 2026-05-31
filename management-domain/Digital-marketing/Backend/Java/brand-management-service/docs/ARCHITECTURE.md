# Brand Management Service - Architecture Documentation

## Overview

The Brand Management Service is a core component of the Digital Marketing Domain within the Gogidix Ecosystem. It provides comprehensive brand asset management, brand guidelines enforcement, and brand consistency monitoring across all marketing channels.

## Table of Contents

- [Service Purpose](#service-purpose)
- [Architecture Principles](#architecture-principles)
- [System Architecture](#system-architecture)
- [Domain Model](#domain-model)
- [Technology Stack](#technology-stack)
- [Multi-Tenancy](#multi-tenancy)
- [Data Flow](#data-flow)
- [Integration Points](#integration-points)
- [Security](#security)

---

## Service Purpose

The Brand Management Service is responsible for:

1. **Brand Asset Management**: Centralized storage and management of brand assets (logos, images, videos, templates, etc.)
2. **Brand Guidelines**: Creation and distribution of brand usage guidelines
3. **Asset Approval Workflow**: Review and approval processes for brand assets
4. **Usage Tracking**: Monitoring where and how brand assets are used
5. **Brand Consistency**: Ensuring consistent brand representation across channels

## Architecture Principles

- **Domain-Driven Design**: Clear domain boundaries with ubiquitous language
- **Multi-Tenancy First**: Complete tenant isolation at all levels
- **Asset Storage Abstraction**: Support for multiple storage backends
- **Event-Driven**: Asynchronous processing for heavy operations
- **API-First**: RESTful APIs with comprehensive documentation

## System Architecture

### High-Level Architecture

```mermaid
graph TB
    subgraph "Presentation Layer"
        API[REST API]
        Docs[Swagger UI]
    end

    subgraph "Application Layer"
        AssetService[Asset Service]
        GuidelineService[Guideline Service]
        ApprovalService[Approval Service]
    end

    subgraph "Domain Layer"
        Asset[Brand Asset]
        Guideline[Brand Guideline]
        Repository[Asset Repository]
    end

    subgraph "Infrastructure Layer"
        Mongo[MongoDB]
        Storage[S3/Storage]
        Cache[Redis Cache]
    end

    subgraph "External Services"
        CDN[CDN]
        ImageProc[Image Processing]
    end

    API --> AssetService
    API --> GuidelineService
    AssetService --> Asset
    GuidelineService --> Guideline
    Asset --> Repository
    Repository --> Mongo
    AssetService --> Storage
    AssetService --> Cache
    Storage --> CDN
```

## Domain Model

### BrandAsset

Core entity representing a brand asset.

```mermaid
classDiagram
    class BrandAsset {
        +String id
        +String tenantId
        +String name
        +String type
        +String url
        +String category
        +List~String~ tags
        +String status
        +String version
        +Long fileSize
        +String uploadedBy
        +Boolean isActive
        +Boolean isPublic
        +approve(approver)
        +archive()
        +addTag(tag)
        +addUsage(context, reference)
    }

    class BrandAssetUsage {
        +String context
        +String reference
        +Instant usedAt
    }

    BrandAsset "1" *-- "*" BrandAssetUsage : tracks
```

### BrandGuideline

Entity representing brand usage guidelines.

```mermaid
classDiagram
    class BrandGuideline {
        +String id
        +String tenantId
        +String name
        +String category
        +String status
        +String version
        +List~GuidelineRule~ rules
        +List~GuidelineExample~ examples
        +approve(approver)
        +addRule(title, description)
        +addExample(title, url, isCorrect)
    }

    class GuidelineRule {
        +String title
        +String description
    }

    class GuidelineExample {
        +String title
        +String imageUrl
        +boolean isCorrect
    }

    BrandGuideline "1" *-- "*" GuidelineRule : contains
    BrandGuideline "1" *-- "*" GuidelineExample : illustrates
```

## Technology Stack

- **Spring Boot 3.x**: Application framework
- **Spring Data MongoDB**: Data persistence
- **MongoDB**: Document database
- **Lombok**: Code generation
- **MapStruct**: DTO mapping

## Multi-Tenancy

All data is isolated by tenant ID through:
- Document-level `tenantId` field
- Compound indexes including tenant
- Repository-level filtering
- Request context-based tenant resolution

## Data Flow

### Asset Upload Flow

```mermaid
sequenceDiagram
    participant User as User
    participant API as REST API
    participant Service as Asset Service
    participant Storage as S3/Storage
    participant DB as MongoDB

    User->>API: Upload Asset
    API->>Service: Process Upload
    Service->>Storage: Store File
    Storage-->>Service: File URL
    Service->>DB: Save Asset Metadata
    DB-->>Service: Asset Created
    Service->>User: Return Asset
```

## Integration Points

- **Campaign Management Service**: Provides assets for campaigns
- **Content Management Service**: Enforces brand guidelines
- **Email Marketing Service**: Supplies email templates
- **Social Media Service**: Provides social media assets
- **Analytics Service**: Tracks asset usage metrics

## Security

- JWT-based authentication
- Role-based access control
- Tenant data isolation
- Asset access control (public/private)
- Audit logging for all mutations
