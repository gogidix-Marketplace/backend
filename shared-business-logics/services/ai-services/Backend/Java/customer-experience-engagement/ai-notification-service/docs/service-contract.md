# AI Notification Service - Service Contract

## Service Responsibility
Intelligent notification delivery and preference management.

## Core Functionality
- Notification delivery
- Preference management
- Smart scheduling
- Channel optimization

## API Contracts

### 1. Send Notification
**Endpoint:** `POST /api/v1/notifications/send`

**Input:**
```json
{
  "userId": "string",
  "channels": ["EMAIL|SMS|PUSH|IN_APP"],
  "template": "string",
  "data": {},
  "priority": "HIGH|MEDIUM|LOW",
  "scheduleAt": "datetime (optional)"
}
```

**Output:**
```json
{
  "notificationId": "string (UUID)",
  "status": "QUEUED|SENT|DELIVERED|FAILED",
  "scheduledFor": "datetime"
}
```

### 2. Update Preferences
**Endpoint:** `PUT /api/v1/notifications/preferences`

## Business Rules
- Quiet hours: respected
- Rate limiting: per channel
- Max batch size: 1000

## Error Conditions
- User not found (404)
- Invalid channel (400)
