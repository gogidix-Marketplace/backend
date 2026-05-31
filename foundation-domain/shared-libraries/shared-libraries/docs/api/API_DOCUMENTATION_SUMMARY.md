# Shared Libraries - API Documentation Summary

**Module:** gogidix-foundation-shared-libraries  
**Version:** 1.0.0  
**Last Updated:** 2025-10-26

---

## API-Enabled Libraries

The following libraries expose REST APIs:

1. **shared-security** - Authentication & Authorization APIs
2. **shared-audit** - Audit Logging APIs
3. **shared-messaging** - Message Management APIs
4. **shared-utilities** - Utility Function APIs
5. **shared-validation** - Validation Service APIs

---

## 1. Shared Security APIs

### Base URL
```
/api/security
```

### Authentication Endpoints

#### POST /auth/login
**Description:** Authenticate user and receive JWT token

**Request:**
```json
{
  "username": "john.doe",
  "password": "SecurePassword123!"
}
```

**Response (200 OK):**
```json
{
  "token": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refreshToken": "refresh_token_here",
  "expiresIn": 3600,
  "tokenType": "Bearer",
  "user": {
    "id": "user-123",
    "username": "john.doe",
    "roles": ["USER", "MANAGER"]
  }
}
```

**Error Responses:**
- `401 Unauthorized` - Invalid credentials
- `423 Locked` - Account locked
- `429 Too Many Requests` - Rate limit exceeded

---

#### POST /auth/refresh
**Description:** Refresh JWT token using refresh token

**Request:**
```json
{
  "refreshToken": "refresh_token_here"
}
```

**Response (200 OK):**
```json
{
  "token": "new_jwt_token",
  "expiresIn": 3600
}
```

---

#### POST /auth/logout
**Description:** Invalidate JWT token and end session

**Request:**
```
Authorization: Bearer {token}
```

**Response (200 OK):**
```json
{
  "message": "Successfully logged out"
}
```

---

### User Management Endpoints

#### GET /users/{id}
**Description:** Get user by ID

**Response (200 OK):**
```json
{
  "id": "user-123",
  "username": "john.doe",
  "email": "john.doe@example.com",
  "roles": ["USER", "MANAGER"],
  "status": "ACTIVE",
  "createdAt": "2025-01-15T10:30:00Z"
}
```

---

#### POST /users
**Description:** Create new user

**Request:**
```json
{
  "username": "jane.smith",
  "email": "jane.smith@example.com",
  "password": "SecurePass123!",
  "roles": ["USER"]
}
```

**Response (201 Created):**
```json
{
  "id": "user-456",
  "username": "jane.smith",
  "email": "jane.smith@example.com",
  "roles": ["USER"],
  "status": "ACTIVE"
}
```

---

### Role Management Endpoints

#### GET /roles
**Description:** List all roles

**Response (200 OK):**
```json
[
  {
    "id": "role-1",
    "name": "ADMIN",
    "permissions": ["users:read", "users:write", "system:admin"]
  },
  {
    "id": "role-2",
    "name": "USER",
    "permissions": ["profile:read", "profile:write"]
  }
]
```

---

## 2. Shared Audit APIs

### Base URL
```
/api/audit
```

#### POST /audit/events
**Description:** Create audit event

**Request:**
```json
{
  "eventType": "USER_LOGIN",
  "userId": "user-123",
  "resource": "authentication",
  "action": "login",
  "status": "SUCCESS",
  "metadata": {
    "ipAddress": "192.168.1.100",
    "userAgent": "Mozilla/5.0"
  }
}
```

**Response (201 Created):**
```json
{
  "id": "audit-789",
  "eventType": "USER_LOGIN",
  "timestamp": "2025-10-26T10:30:00Z",
  "status": "RECORDED"
}
```

---

#### GET /audit/events
**Description:** Query audit events

**Query Parameters:**
- `userId` - Filter by user ID
- `eventType` - Filter by event type
- `startDate` - Start date (ISO 8601)
- `endDate` - End date (ISO 8601)
- `page` - Page number (default: 0)
- `size` - Page size (default: 20)

**Response (200 OK):**
```json
{
  "content": [
    {
      "id": "audit-789",
      "eventType": "USER_LOGIN",
      "userId": "user-123",
      "timestamp": "2025-10-26T10:30:00Z",
      "action": "login",
      "status": "SUCCESS"
    }
  ],
  "totalElements": 150,
  "totalPages": 8,
  "page": 0,
  "size": 20
}
```

---

## 3. Shared Messaging APIs

### Base URL
```
/api/messaging
```

#### POST /messages
**Description:** Create and send message

**Request:**
```json
{
  "messageType": "EMAIL",
  "priority": "HIGH",
  "subject": "Important Notification",
  "content": "Your order has been shipped.",
  "recipients": ["user@example.com"],
  "metadata": {
    "orderId": "order-123",
    "trackingNumber": "TRACK123"
  }
}
```

**Response (201 Created):**
```json
{
  "id": "msg-456",
  "messageType": "EMAIL",
  "status": "QUEUED",
  "createdAt": "2025-10-26T10:30:00Z",
  "estimatedDelivery": "2025-10-26T10:31:00Z"
}
```

---

#### GET /messages/{id}
**Description:** Get message by ID

**Response (200 OK):**
```json
{
  "id": "msg-456",
  "messageType": "EMAIL",
  "status": "DELIVERED",
  "subject": "Important Notification",
  "recipients": ["user@example.com"],
  "sentAt": "2025-10-26T10:30:45Z",
  "deliveredAt": "2025-10-26T10:31:12Z"
}
```

---

## 4. Shared Utilities APIs

### Base URL
```
/api/utilities
```

#### POST /utilities/date/format
**Description:** Format date with specified pattern

**Request:**
```json
{
  "date": "2025-10-26T10:30:00Z",
  "pattern": "yyyy-MM-dd HH:mm:ss",
  "timezone": "America/New_York"
}
```

**Response (200 OK):**
```json
{
  "formatted": "2025-10-26 06:30:00",
  "timezone": "America/New_York"
}
```

---

#### POST /utilities/json/validate
**Description:** Validate JSON against schema

**Request:**
```json
{
  "json": "{\"name\":\"John\",\"age\":30}",
  "schema": {
    "type": "object",
    "properties": {
      "name": {"type": "string"},
      "age": {"type": "number"}
    },
    "required": ["name", "age"]
  }
}
```

**Response (200 OK):**
```json
{
  "valid": true,
  "errors": []
}
```

---

## 5. Shared Validation APIs

### Base URL
```
/api/validation
```

#### POST /validation/email
**Description:** Validate email address

**Request:**
```json
{
  "email": "john.doe@example.com"
}
```

**Response (200 OK):**
```json
{
  "valid": true,
  "email": "john.doe@example.com",
  "normalized": "john.doe@example.com",
  "domain": "example.com"
}
```

---

#### POST /validation/phone
**Description:** Validate phone number

**Request:**
```json
{
  "phone": "+1-555-123-4567",
  "country": "US"
}
```

**Response (200 OK):**
```json
{
  "valid": true,
  "formatted": "+1 (555) 123-4567",
  "country": "US",
  "type": "MOBILE"
}
```

---

## Common Response Codes

| Code | Meaning | Description |
|------|---------|-------------|
| 200 | OK | Request succeeded |
| 201 | Created | Resource created |
| 204 | No Content | Request succeeded, no content |
| 400 | Bad Request | Invalid request parameters |
| 401 | Unauthorized | Authentication required |
| 403 | Forbidden | Insufficient permissions |
| 404 | Not Found | Resource not found |
| 409 | Conflict | Resource conflict |
| 422 | Unprocessable Entity | Validation failed |
| 429 | Too Many Requests | Rate limit exceeded |
| 500 | Internal Server Error | Server error |
| 503 | Service Unavailable | Service temporarily unavailable |

---

## Authentication

All API endpoints (except `/auth/login` and `/auth/register`) require authentication.

### Bearer Token Authentication
```
Authorization: Bearer {jwt_token}
```

### Example Request
```bash
curl -X GET \
  https://api.gogidix.com/api/users/123 \
  -H 'Authorization: Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9...'
```

---

## Rate Limiting

- **Default Limit**: 100 requests per minute per user
- **Header**: `X-RateLimit-Remaining`
- **Reset Header**: `X-RateLimit-Reset`

**429 Response:**
```json
{
  "error": "Rate limit exceeded",
  "retryAfter": 60
}
```

---

## Pagination

List endpoints support pagination:

**Query Parameters:**
- `page` - Page number (0-indexed)
- `size` - Page size (default: 20, max: 100)
- `sort` - Sort field and direction (e.g., `createdAt,desc`)

**Response:**
```json
{
  "content": [...],
  "totalElements": 150,
  "totalPages": 8,
  "page": 0,
  "size": 20
}
```

---

## Error Response Format

```json
{
  "timestamp": "2025-10-26T10:30:00Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "path": "/api/users",
  "errors": [
    {
      "field": "email",
      "message": "Invalid email format"
    }
  ]
}
```

---

## Swagger/OpenAPI Documentation

Interactive API documentation available at:
```
/swagger-ui.html
/api-docs
```

---

**Status:** ✅ API Documentation Complete  
**Coverage:** 5/9 libraries with REST APIs  
**Last Updated:** 2025-10-26
