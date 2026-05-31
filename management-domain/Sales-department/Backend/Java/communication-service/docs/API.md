# Communication Service - API Documentation

## Base URL

```
/api/v1/communication
```

## Authentication

All endpoints require Bearer token authentication with tenant context in JWT claims.

```
Authorization: Bearer <token>
X-Tenant-ID: <tenant-id>
```

## Messages API

### Create Message

```http
POST /messages
Content-Type: application/json

{
  "conversationId": "conv-123",
  "senderName": "John Doe",
  "recipients": [
    {
      "recipientId": "user-456",
      "recipientName": "Jane Smith",
      "recipientType": "USER",
      "emailAddress": "jane@example.com"
    }
  ],
  "channel": "EMAIL",
  "subject": "Meeting Follow-up",
  "content": "Thank you for the meeting...",
  "priority": 2,
  "scheduledAt": "2024-02-24T10:00:00Z"
}
```

**Response:** 201 Created
```json
{
  "id": "507f1f77bcf86cd799439011",
  "messageId": "msg-789",
  "tenantId": "tenant-123",
  "conversationId": "conv-123",
  "senderId": "user-123",
  "senderName": "John Doe",
  "channel": "EMAIL",
  "subject": "Meeting Follow-up",
  "content": "Thank you for the meeting...",
  "status": "DRAFT",
  "isRead": false,
  "priority": 2,
  "createdAt": "2024-02-23T10:00:00Z"
}
```

### Send Message

```http
POST /messages/{messageId}/send
```

**Response:** 200 OK
```json
{
  "id": "507f1f77bcf86cd799439011",
  "messageId": "msg-789",
  "status": "SENT",
  "sentAt": "2024-02-23T10:05:00Z",
  "externalMessageId": "EMAIL-a1b2c3d4"
}
```

### Mark Message as Read

```http
POST /messages/{messageId}/read
```

**Response:** 204 No Content

### Get Message by ID

```http
GET /messages/{messageId}
```

**Response:** 200 OK

### Get Messages by Conversation

```http
GET /messages/conversation/{conversationId}?page=0&size=20&sortBy=sentAt&sortDirection=asc
```

**Response:** 200 OK
```json
{
  "content": [...],
  "page": 0,
  "size": 20,
  "totalElements": 45,
  "totalPages": 3
}
```

### Get Unread Messages

```http
GET /messages/unread?page=0&size=20
```

### Search Messages

```http
GET /messages/search?searchTerm=follow-up&startDate=2024-02-01T00:00:00Z&endDate=2024-02-28T23:59:59Z&page=0&size=20
```

### Get Messages by Status

```http
GET /messages/status/SENT?page=0&size=20
```

### Get Messages by Sender

```http
GET /messages/sender/{senderId}?page=0&size=20
```

### Get Message Thread

```http
GET /messages/thread/{parentMessageId}
```

### Schedule Message

```http
POST /messages/{messageId}/schedule
Content-Type: application/json

{
  "scheduledAt": "2024-02-25T09:00:00Z"
}
```

### Add Attachment

```http
POST /messages/{messageId}/attachments
Content-Type: application/json

{
  "attachment": {
    "attachmentId": "att-123",
    "fileName": "proposal.pdf",
    "fileType": "application/pdf",
    "fileSize": 1048576,
    "fileUrl": "https://storage.example.com/proposal.pdf",
    "storageProvider": "S3"
  }
}
```

### Delete Message

```http
DELETE /messages/{messageId}
```

**Response:** 204 No Content

**Note:** Only DRAFT or FAILED messages can be deleted.

## Conversations API

### Create Conversation

```http
POST /conversations
Content-Type: application/json

{
  "title": "Sales Opportunity - ABC Corp",
  "description": "Discussion about Q2 contract",
  "type": "SALES_CONVERSATION",
  "participants": [
    {
      "participantId": "user-456",
      "participantName": "Jane Smith",
      "participantType": "USER",
      "role": "MEMBER"
    }
  ],
  "defaultChannel": "IN_APP",
  "relatedEntityType": "LEAD",
  "relatedEntityId": "lead-789"
}
```

**Response:** 201 Created

### Update Conversation

```http
PUT /conversations/{conversationId}
Content-Type: application/json

{
  "title": "Updated Title",
  "description": "Updated description",
  "assignedTo": "user-999",
  "priority": 3
}
```

### Add Participant

```http
POST /conversations/{conversationId}/participants
Content-Type: application/json

{
  "participant": {
    "participantId": "user-789",
    "participantName": "Bob Johnson",
    "participantType": "USER",
    "role": "MEMBER"
  }
}
```

### Remove Participant

```http
DELETE /conversations/{conversationId}/participants/{participantId}
```

### Mark Conversation as Read

```http
POST /conversations/{conversationId}/read
```

### Archive Conversation

```http
POST /conversations/{conversationId}/archive
```

### Resolve Conversation

```http
POST /conversations/{conversationId}/resolve
Content-Type: application/json

{
  "resolutionNotes": "Customer requirements clarified"
}
```

### Assign Conversation

```http
POST /conversations/{conversationId}/assign
Content-Type: application/json

{
  "assignedTo": "sales-team-lead"
}
```

### Add Tag

```http
POST /conversations/{conversationId}/tags
Content-Type: application/json

{
  "tag": "priority-high"
}
```

### Set SLA Deadline

```http
POST /conversations/{conversationId}/sla
Content-Type: application/json

{
  "deadline": "2024-02-25T17:00:00Z"
}
```

### Get Conversation by ID

```http
GET /conversations/{conversationId}
```

### Get All Conversations for Tenant

```http
GET /conversations?page=0&size=20&status=ACTIVE
```

### Get Conversations by Type

```http
GET /conversations/type/{type}?page=0&size=20
```

### Get Conversations by Participant

```http
GET /conversations/participant/{participantId}?page=0&size=20
```

### Get Conversations by Owner

```http
GET /conversations/owner/{ownerId}?page=0&size=20
```

## Error Responses

### 400 Bad Request

```json
{
  "timestamp": "2024-02-23T10:00:00Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "errors": [
    {
      "field": "recipients",
      "message": "At least one recipient is required"
    }
  ]
}
```

### 404 Not Found

```json
{
  "timestamp": "2024-02-23T10:00:00Z",
  "status": 404,
  "error": "Not Found",
  "message": "Message not found with id: msg-999"
}
```

### 409 Conflict

```json
{
  "timestamp": "2024-02-23T10:00:00Z",
  "status": 409,
  "error": "Conflict",
  "message": "Maximum participants (100) reached"
}
```

### 500 Internal Server Error

```json
{
  "timestamp": "2024-02-23T10:00:00Z",
  "status": 500,
  "error": "Internal Server Error",
  "message": "An unexpected error occurred"
}
```

## Enums

### ChannelType

- `EMAIL` - Email communication
- `SMS` - SMS messages
- `IN_APP` - In-app notifications
- `WHATSAPP` - WhatsApp messages
- `PUSH_NOTIFICATION` - Mobile push notifications

### MessageStatus

- `DRAFT` - Message draft
- `SCHEDULED` - Scheduled for future sending
- `SENDING` - Currently being sent
- `SENT` - Successfully sent
- `DELIVERED` - Delivered to recipient
- `FAILED` - Send failed
- `BOUNCED` - Bounced by recipient
- `READ` - Read by recipient
- `ARCHIVED` - Archived message

### ConversationType

- `DIRECT` - Direct 1-on-1 conversation
- `GROUP` - Group conversation
- `CHANNEL` - Public channel
- `SUPPORT_TICKET` - Support ticket conversation
- `SALES_CONVERSATION` - Sales-related conversation
- `MARKETING_CAMPAIGN` - Marketing campaign conversation

### ConversationStatus

- `ACTIVE` - Active conversation
- `ARCHIVED` - Archived conversation
- `CLOSED` - Closed conversation
- `RESOLVED` - Resolved conversation
- `ON_HOLD` - On hold conversation
- `PENDING` - Pending conversation

## Pagination

All list endpoints support pagination:

- `page` - Page number (default: 0)
- `size` - Page size (default: 20, max: 100)
- `sortBy` - Sort field
- `sortDirection` - asc or desc

## Rate Limiting

- 100 requests per minute per tenant
- 1000 requests per hour per tenant

Rate limit headers are included in responses:

```
X-RateLimit-Limit: 100
X-RateLimit-Remaining: 95
X-RateLimit-Reset: 1708707600
```
