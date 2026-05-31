# Log Aggregation Service - Architecture

## Overview

The Log Aggregation Service is a Node.js microservice that collects, parses, and stores logs from various sources with Elasticsearch integration for powerful search and analytics.

## Architecture Diagram

```mermaid
graph TB
    subgraph "Collection Layer"
        COLLECTOR[Log Collection Service]
        PARSER[Log Parser Service]
    end

    subgraph "Storage Layer"
        ES[Elasticsearch Service]
        RETENTION[Log Retention Service]
    end

    subgraph "API Layer"
        API[Log Controller]
        SOURCE[Log Source Controller]
    end

    subgraph "Data Layer"
        MONGO[(MongoDB)]
    end

    subgraph "Log Sources"
        FILES[Log Files]
        SYSLOG[Syslog]
        K8S[Kubernetes Logs]
        APP[Application Logs]
    end

    FILES --> COLLECTOR
    SYSLOG --> COLLECTOR
    K8S --> COLLECTOR
    APP --> COLLECTOR
    COLLECTOR --> PARSER
    PARSER --> ES
    API --> ES
    SOURCE --> MONGO
    RETENTION --> ES
```

## Components

### Controllers
- **LogController**: Query and retrieve logs
- **LogSourceController**: Manage log sources

### Services
- **LogCollectionService**: Collect logs from sources
- **LogParserService**: Parse various log formats
- **ElasticsearchService**: Store and search logs
- **LogRetentionService**: Apply retention policies

## Domain Models

### LogSource
- Source type (file, syslog, kubernetes)
- Connection details
- Parse format
- Collection schedule

### LogAlertRule
- Alert conditions
- Notification settings
- Rule status

## Supported Log Formats

- JSON
- Syslog
- Common Log Format (CLF)
- Combined Log Format
- Custom patterns

## Technology Stack

- **Runtime**: Node.js 18+
- **Framework**: Express.js
- **Database**: MongoDB
- **Search**: Elasticsearch
- **Language**: TypeScript
- **Testing**: Jest
