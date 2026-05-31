# Notification Service - Architecture Documentation

## Overview

The Notification Service manages notifications for the Sales Department, including in-app notifications, email digests, and alert management.

## Domain Model

### Notification Entity

- `notificationId`: Unique identifier
- `tenantId`: Multi-tenant isolation
- `recipientId`: Recipient user
- `type`: Type (INFO, WARNING, ERROR, SUCCESS)
- `category`: Category (DEAL_UPDATE, LEAD_ASSIGNED, TASK_DUE, MENTION, SYSTEM)
- `title`: Notification title
- `message`: Notification message
- `actionLink`: Link to related resource
- `isRead`: Read status
- `readAt`: When read
- `expiresAt`: Expiration time
- `priority`: Priority (LOW, NORMAL, HIGH, URGENT)

### NotificationPreference Entity

- `preferenceId`: Unique identifier
- `userId`: User preferences
- `channel`: Channel (IN_APP, EMAIL, SMS, PUSH)
- `category`: Notification category
- `enabled`: Is enabled
- `frequency`: Frequency (IMMEDIATE, HOURLY, DAILY, WEEKLY)

### NotificationTemplate Entity

- `templateId`: Unique identifier
- `templateName`: Template name
- `category`: Template category
- `subject`: Message subject
- `body`: Message body with variables
- `variables`: Template variables

## Application Services

### NotificationCommandService

- `sendNotification()`: Send notification to user
- `sendBulkNotification()`: Send to multiple users
- `markAsRead()`: Mark notification as read
- `markAllAsRead()`: Mark all user's notifications as read
- `dismissNotification()`: Dismiss notification

### NotificationQueryService

- `getNotifications()`: Get user's notifications
- `getUnreadCount()`: Get unread count
- `getPreferences()`: Get user preferences
- `getTemplates()`: Get available templates

### PreferenceCommandService

- `updatePreferences()`: Update user preferences
- `enableChannel()`: Enable notification channel
- `disableChannel()`: Disable notification channel

## Technology Stack

- **Language**: Java 17
- **Framework**: Spring Boot 3.1.5
- **Database**: MongoDB, Redis
- **Messaging**: Apache Kafka, Email Service
