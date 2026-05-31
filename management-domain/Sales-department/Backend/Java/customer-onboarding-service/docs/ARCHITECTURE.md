# Customer Onboarding Service - Architecture Documentation

## Overview

The Customer Onboarding Service manages the onboarding process for new customers, tracking progress through onboarding stages.

## Domain Model

### OnboardingPlan Entity

- `planId`: Unique identifier
- `tenantId`: Multi-tenant isolation
- `customerId`: Customer being onboarded
- `planType`: Type (STANDARD, ENTERPRISE, CUSTOM)
- `status`: Status (NOT_STARTED, IN_PROGRESS, COMPLETED, ON_HOLD, CANCELLED)
- `startDate`: Start date
- `expectedEndDate`: Expected completion
- `actualEndDate`: Actual completion date
- `assignedTo`: Onboarding specialist
- `progress`: Progress percentage (0-100)

### OnboardingTask Entity

- `taskId`: Unique identifier
- `planId`: Parent plan
- `taskName`: Task name
- `description`: Task description
- `dueDate`: Due date
- `status`: Status (PENDING, IN_PROGRESS, COMPLETED, SKIPPED)
- `assignedTo`: Task assignee
- `order`: Task order in sequence

### OnboardingMilestone Entity

- `milestoneId`: Unique identifier
- `planId`: Parent plan
- `milestoneName`: Milestone name
- `targetDate`: Target date
- `achievedDate': When achieved
- `status`: Status (PENDING, ACHIEVED, MISSED)

## Application Services

### OnboardingCommandService

- `createPlan()`: Create onboarding plan
- `updatePlan()`: Update plan details
- `addTask()`: Add task to plan
- `completeTask()`: Mark task complete
- `achieveMilestone()`: Mark milestone achieved
- `assignSpecialist()`: Assign onboarding specialist
- `cancelPlan()`: Cancel onboarding

### OnboardingQueryService

- `getPlanById()`: Get onboarding plan
- `getPlansByCustomer()`: Get customer's plans
- `getPlansBySpecialist()`: Get specialist's plans
- `getOverduePlans()`: Get plans past due date

## Technology Stack

- **Language**: Java 17
- **Framework**: Spring Boot 3.1.5
- **Database**: MongoDB
- **Messaging**: Apache Kafka
