# Global Sales Dashboard Service - Architecture Documentation

## Overview

The Global Sales Dashboard Service provides global sales analytics and executive-level dashboard data for the Sales Department.

## Domain Model

### GlobalMetric Entity

- `metricId`: Unique identifier
- `tenantId`: Multi-tenant isolation
- `period`: Time period
- `totalRevenue`: Total global revenue
- `totalDealsWon`: Total deals won
- `winRate': Overall win rate
- `averageDealSize': Average deal size
- `pipelineValue': Total pipeline value
- `forecast': Forecasted revenue
- `growthRate': Revenue growth rate

### RegionalBreakdown Entity

- `breakdownId`: Unique identifier
- `region': Geographic region
- `revenue': Revenue for region
- `contribution': % of total revenue
- `growth': Region growth rate
- `target': Regional target
- `achievement': % of target achieved

### ProductPerformance Entity

- `performanceId`: Unique identifier
- `productId': Product identifier
- `productName': Product name
- `revenue': Revenue from product
- `deals': Number of deals
- `growth': Growth rate
- `margin': Profit margin

## Application Services

### GlobalDashboardService

- `getGlobalMetrics()`: Get global metrics
- `getExecutiveSummary()`: Get executive summary
- `getRegionalComparison()`: Compare regions
- `getProductPerformance()`: Get product metrics
- `getTrendAnalysis()`: Get trend analysis

### ReportingService

- `generateExecutiveReport()`: Generate executive report
- `generateRegionalReport()`: Generate regional report
- `generateProductReport()`: Generate product report

## Technology Stack

- **Language**: Java 17
- **Framework**: Spring Boot 3.1.5
- **Database**: MongoDB
- **Messaging**: Apache Kafka
