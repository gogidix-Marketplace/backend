# Country Sales Dashboard Service - Architecture Documentation

## Overview

The Country Sales Dashboard Service provides country-specific sales analytics and dashboard data for the Sales Department.

## Domain Model

### CountryMetric Entity

- `metricId`: Unique identifier
- `tenantId`: Multi-tenant isolation
- `countryCode`: ISO country code
- `countryName`: Country name
- `period`: Time period
- `revenue`: Revenue generated
- `dealsWon`: Deals won count
- `dealsLost`: Deals lost count
- `pipelineValue`: Total pipeline value
- `activitiesCompleted`: Activities completed
- `conversionRate': Lead to deal conversion rate

### CountryTerritory Entity

- `territoryId`: Unique identifier
- `countryCode`: Associated country
- `regionName`: Region within country
- `managerId`: Territory manager
- `targetAmount`: Sales target
- `actualAmount`: Actual sales
- `achievementPercentage`: Target achievement %

## Application Services

### CountryDashboardService

- `getCountryMetrics()`: Get metrics for country
- `getCountryComparison()`: Compare countries
- `getTopPerformers()`: Get top performing countries
- `getCountryTrend()`: Get trend over time

### RegionDashboardService

- `getRegionMetrics()`: Get metrics for region
- `getRegionalBreakdown()`: Break down by region
- `getManagerPerformance()`: Manager performance metrics

## Technology Stack

- **Language**: Java 17
- **Framework**: Spring Boot 3.1.5
- **Database**: MongoDB
- **Messaging**: Apache Kafka
