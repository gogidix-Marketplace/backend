# Revenue Tracking Service - Architecture Documentation

## Overview

The Revenue Tracking Service manages recognized revenue from closed deals and tracks revenue against targets.

## Domain Model

### Revenue Entity

- `revenueId`: Unique identifier
- `tenantId`: Multi-tenant isolation
- `dealId`: Source deal
- `amount`: Revenue amount
- `currency`: Currency code
- `recognizedDate`: When revenue was recognized
- `recognizedBy`: User who recognized revenue
- `period`: Accounting period
- `status`: Status (PENDING, RECOGNIZED, ADJUSTED, CANCELLED)
- `productRevenue`: Breakdown by product

## Application Services

### RevenueCommandService

- `recognizeRevenue()`: Recognize revenue from won deal
- `adjustRevenue()`: Adjust recognized revenue
- `cancelRevenue()`: Cancel revenue recognition
- `recognizeMilestone()`: Recognize milestone-based revenue

### RevenueQueryService

- `getRevenueById()`: Get revenue record
- `getRevenueByPeriod()`: Get revenue for period
- `getRevenueByTerritory()`: Get revenue by territory
- `getRevenueByUser()`: Get revenue by user
- `getRevenueTrend()`: Get revenue trend over time

## Technology Stack

- **Language**: Java 17
- **Framework**: Spring Boot 3.1.5
- **Database**: MongoDB
- **Messaging**: Apache Kafka
