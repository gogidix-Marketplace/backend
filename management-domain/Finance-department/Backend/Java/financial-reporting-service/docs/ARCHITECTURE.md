# Financial Reporting Service - Architecture Documentation

## Overview

The Financial Reporting Service is responsible for generating various financial reports including Balance Sheet, Income Statement, Cash Flow Statement, and custom reports. It aggregates data from the General Ledger and other financial services to produce accurate, compliant financial statements.

## Architecture

### Technology Stack
- **Framework**: Spring Boot 3.1.5
- **Language**: Java 17
- **Database**: MongoDB
- **Message Broker**: Apache Kafka
- **Cache**: Redis

### Domain Models

#### Report
- Represents a generated financial report
- Supports multiple report types (Balance Sheet, Income Statement, etc.)
- Handles multi-currency consolidation
- Tracks report generation status and versioning

#### ReportTemplate
- Defines the structure of custom reports
- Supports row/column definitions
- Handles formulas and calculations

#### ReportSchedule
- Manages automated report generation
- Supports various schedules (daily, weekly, monthly, quarterly)
- Handles distribution to stakeholders

## API Endpoints

### Report Generation
- `POST /api/v1/reports/generate` - Generate a new report
- `GET /api/v1/reports/{reportId}` - Get report by ID
- `GET /api/v1/reports/{reportId}/download` - Download report PDF/Excel

### Report Templates
- `POST /api/v1/reports/templates` - Create report template
- `GET /api/v1/reports/templates` - List all templates
- `PUT /api/v1/reports/templates/{templateId}` - Update template

### Scheduling
- `POST /api/v1/reports/schedule` - Schedule report generation
- `GET /api/v1/reports/schedules` - List all schedules
- `DELETE /api/v1/reports/schedules/{scheduleId}` - Cancel schedule

## Data Model

### Reports Collection
```javascript
{
  reportId: "string",
  tenantId: "string",
  reportType: "BALANCE_SHEET",
  periodStart: ISODate,
  periodEnd: ISODate,
  currency: "USD",
  status: "GENERATED",
  generatedAt: ISODate,
  data: {...}
}
```

## Event Integration

### Consumed Events
- JournalEntryPosted - Update balances
- AccountCreated - Add new accounts to reports
- PeriodClosed - Trigger report generation

### Published Events
- ReportGenerated - Notify downstream systems
- ReportFailed - Alert on generation failures

## Multi-Tenancy

All reports are tenant-isolated with tenant_id filtering on all queries.
