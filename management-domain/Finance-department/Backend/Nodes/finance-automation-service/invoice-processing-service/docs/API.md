# Invoice Processing Service - API Documentation

## Base URL
```
http://{host}:3000/api/invoice-processing
```

---

## Invoice API

### Upload Invoice
```http
POST /invoices/upload
```

**Request:** `multipart/form-data`

| Field | Type | Required | Description |
|-------|------|----------|-------------|
| file | File | Yes | Invoice file (PDF, image) |
| vendorId | string | Yes | Vendor identifier |
| tenantId | string | Yes | Tenant identifier |
| organizationId | string | Yes | Organization identifier |
| autoProcess | boolean | No | Auto-process after upload (default: false) |

**Response (201 Created):**
```json
{
  "invoiceId": "inv-abc123",
  "status": "RECEIVED",
  "fileName": "invoice.pdf",
  "uploadedAt": "2024-01-15T10:00:00Z",
  "ocrProcessed": false
}
```

### Get Invoice
```http
GET /invoices/:invoiceId
```

**Response (200 OK):**
```json
{
  "invoiceId": "inv-abc123",
  "invoiceNumber": "INV-2024-001",
  "vendorId": "vendor-001",
  "vendorName": "Acme Supplies",
  "invoiceDate": "2024-01-15T00:00:00Z",
  "dueDate": "2024-02-15T00:00:00Z",
  "totalAmount": 5000.00,
  "outstandingAmount": 5000.00,
  "status": "RECEIVED",
  "items": [...],
  "attachments": [...]
}
```

### Validate Invoice
```http
POST /invoices/:invoiceId/validate
```

**Request Body:**
```json
{
  "validationLevel": "STRICT",
  "autoApprove": false
}
```

**Response (200 OK):**
```json
{
  "invoiceId": "inv-abc123",
  "validationResult": "PASSED",
  "validationDetails": {
    "vendorCheck": "PASSED",
    "poMatch": "MATCHED",
    "duplicateCheck": "CLEAN",
    "lineItemValidation": "PASSED"
  },
  "confidenceScore": 0.95
}
```

### Approve Invoice
```http
POST /invoices/:invoiceId/approve
```

**Request Body:**
```json
{
  "approvedBy": "user@example.com",
  "approvalLevel": "MANAGER",
  "notes": "Approved for payment"
}
```

### Reject Invoice
```http
POST /invoices/:invoiceId/reject
```

**Request Body:**
```json
{
  "rejectedBy": "user@example.com",
  "reason": "Invalid amount"
}
```

### Process Invoice
```http
POST /invoices/:invoiceId/process
```

**Response (200 OK):**
```json
{
  "invoiceId": "inv-abc123",
  "status": "PROCESSING",
  "processingStartedAt": "2024-01-15T10:05:00Z"
}
```

### Search Invoices
```http
GET /invoices/search
```

**Query Parameters:**
| Parameter | Type | Description |
|-----------|------|-------------|
| vendorId | string | Filter by vendor |
| status | string | Filter by status |
| fromDate | date | Filter from date |
| toDate | date | Filter to date |
| minAmount | number | Minimum amount |
| page | number | Page number |
| size | number | Page size |

---

## Webhook Events

The service emits webhook events for subscribed endpoints.

### Event Types

| Event | Description |
|-------|-------------|
| invoice.received | New invoice uploaded |
| invoice.validated | Validation completed |
| invoice.approved | Invoice approved |
| invoice.rejected | Invoice rejected |
| invoice.processed | Invoice processing complete |
| invoice.failed | Invoice processing failed |

### Webhook Configuration

**To configure webhooks:**
1. POST `/webhooks` - Register webhook URL
2. Provide authentication token
3. Subscribe to event types

---

## Error Responses

```json
{
  "statusCode": 400,
  "message": "Validation failed",
  "errors": [
    {
      "field": "invoiceNumber",
      "message": "Invoice number is required"
    }
  ]
}
```

| Status | Description |
|--------|-------------|
| 400 | Bad Request - Invalid input |
| 404 | Not Found - Invoice not found |
| 409 | Conflict - Duplicate invoice |
| 422 | Unprocessable Entity - Business rule violation |
| 500 | Internal Server Error |
| 503 | Service Unavailable - OCR service down |
