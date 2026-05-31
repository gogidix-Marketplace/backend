# Accounts Payable Service - API Documentation

## Base URL
```
http://{host}:8080/api/accounts-payable
```

## Authentication
All API requests require:
- Header: `Authorization: Bearer {jwt_token}`
- Header: `X-Tenant-ID: {tenant_id}`

## Content Type
```
Content-Type: application/json
```

---

## Invoice API

### Create Invoice
```http
POST /invoices
```

**Request Body:**
```json
{
  "vendorId": "vendor-123",
  "vendorName": "Acme Supplies Inc.",
  "invoiceNumber": "INV-2024-001",
  "purchaseOrderNumber": "PO-12345",
  "invoiceDate": "2024-01-15",
  "dueDate": "2024-02-15",
  "amount": 5000.00,
  "currency": "USD",
  "description": "Monthly office supplies",
  "notes": "Please process by end of month",
  "internalReference": "REF-001",
  "department": "IT",
  "costCenter": "CC-001",
  "projectId": "PROJ-001",
  "lineItems": [
    {
      "lineItemId": "line-1",
      "description": "Office Chairs",
      "quantity": 10,
      "unitPrice": 250.00,
      "amount": 2500.00,
      "accountCode": "6000",
      "taxCode": "TAX-001"
    }
  ],
  "attachments": ["https://storage.example.com/invoice.pdf"],
  "tags": ["urgent", "recurring"],
  "requiresApproval": true,
  "glAccount": "6000",
  "taxCode": "TAX-001",
  "taxRate": 0.10,
  "taxIncluded": false,
  "discountValidUntil": "2024-01-30",
  "discountPercentage": 2.5,
  "paymentTerms": "Net 30"
}
```

**Response (201 Created):**
```json
{
  "id": "507f1f77bcf86cd799439011",
  "invoiceId": "inv-abc123",
  "tenantId": "tenant-001",
  "vendorId": "vendor-123",
  "vendorName": "Acme Supplies Inc.",
  "invoiceNumber": "INV-2024-001",
  "purchaseOrderNumber": "PO-12345",
  "invoiceDate": "2024-01-15",
  "dueDate": "2024-02-15",
  "receivedDate": "2024-01-15",
  "amount": 5000.00,
  "taxAmount": 500.00,
  "discountAmount": 125.00,
  "netAmount": 5375.00,
  "currency": "USD",
  "status": "DRAFT",
  "submittedBy": "user@example.com",
  "submittedAt": null,
  "approvedBy": null,
  "approvedAt": null,
  "rejectionReason": null,
  "paymentReference": null,
  "paidAt": null,
  "description": "Monthly office supplies",
  "notes": "Please process by end of month",
  "internalReference": "REF-001",
  "department": "IT",
  "costCenter": "CC-001",
  "projectId": "PROJ-001",
  "lineItems": [...],
  "attachments": ["https://storage.example.com/invoice.pdf"],
  "tags": ["urgent", "recurring"],
  "requiresApproval": true,
  "approvalLevel": "NONE",
  "glAccount": "6000",
  "taxCode": "TAX-001",
  "taxIncluded": false,
  "discountValidUntil": "2024-01-30",
  "discountPercentage": 2.5,
  "paymentTerms": "Net 30",
  "createdAt": "2024-01-15T10:00:00Z",
  "updatedAt": "2024-01-15T10:00:00Z",
  "isOverdue": false,
  "daysUntilDue": 31
}
```

### Get Invoice by ID
```http
GET /invoices/{invoiceId}
```

**Response (200 OK):** Same as Create Invoice response

### Get Invoice by Number
```http
GET /invoices/number/{invoiceNumber}
```

**Response (200 OK):** Same as Create Invoice response

### List All Invoices
```http
GET /invoices?page=0&size=20
```

**Query Parameters:**
| Parameter | Type | Default | Description |
|-----------|------|---------|-------------|
| page | integer | 0 | Page number |
| size | integer | 20 | Page size |

**Response (200 OK):**
```json
{
  "content": [...],
  "pageable": {...},
  "totalPages": 5,
  "totalElements": 100,
  "size": 20,
  "number": 0
}
```

### Get Invoices by Vendor
```http
GET /invoices/vendor/{vendorId}?page=0&size=20
```

### Get Invoices by Status
```http
GET /invoices/status/{status}?page=0&size=20
```

**Status Values:** `DRAFT`, `PENDING`, `APPROVED`, `PAID`, `CANCELLED`, `REJECTED`, `OVERDUE`, `PARTIALLY_PAID`

### Get Overdue Invoices
```http
GET /invoices/overdue?page=0&size=20
```

### Get Invoices Pending Approval
```http
GET /invoices/pending-approval?page=0&size=20
```

### Get Invoices by Department
```http
GET /invoices/department/{department}?page=0&size=20
```

### Update Invoice
```http
PUT /invoices/{invoiceId}
```

**Request Body:**
```json
{
  "description": "Updated description",
  "amount": 5500.00,
  "dueDate": "2024-02-20",
  "notes": "Updated notes",
  "internalReference": "REF-002",
  "department": "Finance",
  "costCenter": "CC-002",
  "projectId": "PROJ-002",
  "tags": ["urgent", "updated"],
  "attachments": ["https://storage.example.com/new-invoice.pdf"],
  "taxRate": 0.12,
  "discountPercentage": 5.0,
  "discountValidUntil": "2024-01-25"
}
```

**Note:** Only DRAFT invoices can be updated.

### Submit Invoice for Approval
```http
POST /invoices/{invoiceId}/submit
```

**Response (202 Accepted):** Empty body

### Approve Invoice
```http
POST /invoices/{invoiceId}/approve
```

**Request Body:**
```json
{
  "approvalLevel": "FINANCE"
}
```

**Approval Levels:** `MANAGER`, `FINANCE`, `EXECUTIVE`

**Response (200 OK):** Empty body

### Reject Invoice
```http
POST /invoices/{invoiceId}/reject
```

**Request Body:**
```json
{
  "reason": "Incorrect amount - should be $4500 not $5000"
}
```

**Response (200 OK):** Empty body

### Mark Invoice as Paid
```http
POST /invoices/{invoiceId}/pay
```

**Request Body:**
```json
{
  "paymentReference": "PAY-2024-001"
}
```

**Response (200 OK):** Empty body

### Mark Invoice as Partially Paid
```http
POST /invoices/{invoiceId}/partially-pay
```

**Request Body:**
```json
{
  "paymentReference": "PAY-2024-002",
  "amountPaid": 2500.00
}
```

**Response (200 OK):** Empty body

### Cancel Invoice
```http
POST /invoices/{invoiceId}/cancel
```

**Request Body:**
```json
{
  "reason": "Duplicate invoice"
}
```

**Response (200 OK):** Empty body

### Add Line Item
```http
POST /invoices/{invoiceId}/line-items
```

**Request Body:**
```json
{
  "lineItem": {
    "lineItemId": "line-2",
    "description": "Office Desks",
    "quantity": 5,
    "unitPrice": 500.00,
    "amount": 2500.00,
    "accountCode": "6000",
    "taxCode": "TAX-001"
  }
}
```

### Remove Line Item
```http
DELETE /invoices/{invoiceId}/line-items/{lineItemId}
```

### Add Attachment
```http
POST /invoices/{invoiceId}/attachments
```

**Request Body:**
```json
{
  "attachmentUrl": "https://storage.example.com/additional-doc.pdf"
}
```

### Calculate Tax
```http
POST /invoices/{invoiceId}/calculate-tax
```

**Request Body:**
```json
{
  "taxRate": 0.10
}
```

### Calculate Discount
```http
POST /invoices/{invoiceId}/calculate-discount
```

**Request Body:**
```json
{
  "discountPercentage": 5.0
}
```

### Delete Invoice
```http
DELETE /invoices/{invoiceId}
```

**Note:** Only DRAFT or REJECTED invoices can be deleted.

**Response (204 No Content):** Empty body

---

## Vendor API

### Create Vendor
```http
POST /vendors
```

**Request Body:**
```json
{
  "vendorCode": "VENDOR-001",
  "vendorName": "Acme Supplies Inc.",
  "vendorType": "CORPORATION",
  "taxId": "12-3456789",
  "currency": "USD",
  "paymentDays": 30,
  "paymentTerms": "Net 30",
  "contactPerson": "John Doe",
  "email": "john@acme.com",
  "phone": "+1-555-0123",
  "website": "https://acme.com",
  "billingAddress": {
    "street": "123 Business Ave",
    "city": "Business City",
    "state": "BC",
    "postalCode": "12345",
    "country": "USA",
    "addressLine1": "Suite 100",
    "addressLine2": "Floor 5"
  },
  "shippingAddress": {...},
  "bankAccountNumber": "123456789",
  "bankRoutingNumber": "987654321",
  "bankName": "First National Bank",
  "bankAccountType": "Checking",
  "creditLimit": "2024-12-31",
  "notes": "Preferred vendor",
  "tags": ["priority", "reliable"],
  "parentVendorId": null,
  "discountPercentage": 5.0,
  "validFrom": "2024-01-01",
  "validUntil": "2024-12-31"
}
```

**Vendor Types:** `INDIVIDUAL`, `CORPORATION`, `PARTNERSHIP`, `LLC`, `NON_PROFIT`, `GOVERNMENT`, `FOREIGN_ENTITY`

**Response (201 Created):**
```json
{
  "id": "507f1f77bcf86cd799439011",
  "vendorId": "vendor-abc123",
  "tenantId": "tenant-001",
  "vendorCode": "VENDOR-001",
  "vendorName": "Acme Supplies Inc.",
  "vendorType": "CORPORATION",
  "taxId": "12-3456789",
  "currency": "USD",
  "paymentTerms": "Net 30",
  "paymentDays": 30,
  "contactPerson": "John Doe",
  "email": "john@acme.com",
  "phone": "+1-555-0123",
  "website": "https://acme.com",
  "billingAddress": {...},
  "shippingAddress": {...},
  "status": "PENDING_APPROVAL",
  "createdBy": "admin@example.com",
  "activatedAt": null,
  "deactivatedAt": null,
  "deactivationReason": null,
  "bankAccountNumber": "****5678",
  "bankRoutingNumber": "987654321",
  "bankName": "First National Bank",
  "bankAccountType": "Checking",
  "creditLimit": "2024-12-31",
  "notes": "Preferred vendor",
  "tags": ["priority", "reliable"],
  "parentVendorId": null,
  "isPreferredVendor": false,
  "discountPercentage": 5.0,
  "validFrom": "2024-01-01",
  "validUntil": "2024-12-31",
  "createdAt": "2024-01-15T10:00:00Z",
  "updatedAt": "2024-01-15T10:00:00Z",
  "canReceivePayment": false,
  "isActive": false
}
```

### Get Vendor by ID
```http
GET /vendors/{vendorId}
```

### Get Vendor by Code
```http
GET /vendors/code/{vendorCode}
```

### List All Vendors
```http
GET /vendors?page=0&size=20
```

### Get Vendors by Status
```http
GET /vendors/status/{status}?page=0&size=20
```

**Status Values:** `ACTIVE`, `INACTIVE`, `PENDING_APPROVAL`, `SUSPENDED`, `BLACKLISTED`

### Get Preferred Vendors
```http
GET /vendors/preferred
```

### Search Vendors
```http
GET /vendors/search?query={searchTerm}&page=0&size=20
```

### Update Vendor
```http
PUT /vendors/{vendorId}
```

**Request Body:**
```json
{
  "vendorName": "Updated Acme Supplies Inc.",
  "contactPerson": "Jane Doe",
  "email": "jane@acme.com",
  "phone": "+1-555-0987",
  "billingAddress": {...},
  "shippingAddress": {...},
  "notes": "Updated notes",
  "website": "https://acme-updated.com"
}
```

### Activate Vendor
```http
POST /vendors/{vendorId}/activate
```

### Deactivate Vendor
```http
POST /vendors/{vendorId}/deactivate
```

**Request Body:**
```json
{
  "reason": "Business closed"
}
```

### Suspend Vendor
```http
POST /vendors/{vendorId}/suspend
```

**Request Body:**
```json
{
  "reason": "Payment issues"
}
```

### Blacklist Vendor
```http
POST /vendors/{vendorId}/blacklist
```

**Request Body:**
```json
{
  "reason": "Fraud detected"
}
```

### Set Preferred Status
```http
PUT /vendors/{vendorId}/preferred
```

**Request Body:**
```json
{
  "preferred": true
}
```

### Update Payment Terms
```http
PUT /vendors/{vendorId}/payment-terms
```

**Request Body:**
```json
{
  "paymentTerms": "Net 60",
  "paymentDays": 60
}
```

### Update Bank Information
```http
PUT /vendors/{vendorId}/bank-info
```

**Request Body:**
```json
{
  "bankAccountNumber": "987654321",
  "bankRoutingNumber": "123456789",
  "bankName": "Second National Bank",
  "bankAccountType": "Savings"
}
```

### Add Tag
```http
POST /vendors/{vendorId}/tags
```

**Request Body:**
```json
{
  "tag": "international"
}
```

### Remove Tag
```http
DELETE /vendors/{vendorId}/tags/{tag}
```

### Delete Vendor
```http
DELETE /vendors/{vendorId}
```

**Note:** Only inactive vendors can be deleted.

---

## Payment API

### Create Payment
```http
POST /payments
```

**Request Body:**
```json
{
  "vendorId": "vendor-123",
  "vendorName": "Acme Supplies Inc.",
  "invoiceIds": ["inv-001", "inv-002"],
  "amount": 7500.00,
  "currency": "USD",
  "paymentMethod": "BANK_TRANSFER",
  "paymentDate": "2024-01-20",
  "scheduledDate": "2024-01-25",
  "description": "Payment for invoices INV-001 and INV-002",
  "notes": "Process via standard transfer",
  "bankAccountNumber": "123456789",
  "bankRoutingNumber": "987654321",
  "checkNumber": null,
  "batchId": "BATCH-2024-001",
  "approvalReference": "APPR-001",
  "feeAmount": 25.00,
  "exchangeRate": null,
  "originalCurrency": null,
  "originalAmount": null,
  "attachmentUrl": "https://storage.example.com/payment-doc.pdf"
}
```

**Payment Methods:** `BANK_TRANSFER`, `CHECK`, `WIRE_TRANSFER`, `ACH`, `CREDIT_CARD`, `DEBIT_CARD`, `ELECTRONIC_FUNDS_TRANSFER`, `STANDING_ORDER`

**Response (201 Created):**
```json
{
  "id": "507f1f77bcf86cd799439011",
  "paymentId": "pay-abc123",
  "tenantId": "tenant-001",
  "vendorId": "vendor-123",
  "vendorName": "Acme Supplies Inc.",
  "invoiceId": "inv-001",
  "invoiceNumber": "INV-2024-001",
  "amount": 7500.00,
  "currency": "USD",
  "status": "PENDING",
  "paymentMethod": "BANK_TRANSFER",
  "paymentReference": null,
  "paymentDate": "2024-01-20",
  "scheduledDate": "2024-01-25",
  "processedAt": null,
  "processedBy": null,
  "bankAccountNumber": "****5678",
  "bankRoutingNumber": "987654321",
  "checkNumber": null,
  "transactionReference": null,
  "description": "Payment for invoices",
  "notes": "Process via standard transfer",
  "batchId": "BATCH-2024-001",
  "approvalReference": "APPR-001",
  "rejectionReason": null,
  "cancelledAt": null,
  "cancelledBy": null,
  "cancellationReason": null,
  "invoiceIds": ["inv-001", "inv-002"],
  "allocations": [],
  "feeAmount": 25.00,
  "exchangeRate": null,
  "originalCurrency": null,
  "originalAmount": null,
  "attachmentUrl": "https://storage.example.com/payment-doc.pdf",
  "createdBy": "user@example.com",
  "createdAt": "2024-01-15T10:00:00Z",
  "updatedAt": "2024-01-15T10:00:00Z",
  "totalAmount": 7525.00,
  "canProcess": true,
  "canCancel": true
}
```

### Get Payment by ID
```http
GET /payments/{paymentId}
```

### List All Payments
```http
GET /payments?page=0&size=20
```

### Get Payments by Vendor
```http
GET /payments/vendor/{vendorId}?page=0&size=20
```

### Get Payments by Status
```http
GET /payments/status/{status}?page=0&size=20
```

**Status Values:** `PENDING`, `SCHEDULED`, `PROCESSING`, `COMPLETED`, `FAILED`, `CANCELLED`, `REVERSED`

### Get Scheduled Payments
```http
GET /payments/scheduled?fromDate=2024-01-01&toDate=2024-01-31
```

### Get Payments Requiring Action
```http
GET /payments/requiring-action
```

### Schedule Payment
```http
POST /payments/{paymentId}/schedule
```

**Request Body:**
```json
{
  "scheduledDate": "2024-01-25"
}
```

### Process Payment
```http
POST /payments/{paymentId}/process
```

**Request Body:**
```json
{
  "paymentReference": "REF-2024-001"
}
```

### Complete Payment
```http
POST /payments/{paymentId}/complete
```

**Request Body:**
```json
{
  "transactionReference": "TXN-2024-001"
}
```

### Fail Payment
```http
POST /payments/{paymentId}/fail
```

**Request Body:**
```json
{
  "reason": "Insufficient funds"
}
```

### Cancel Payment
```http
POST /payments/{paymentId}/cancel
```

**Request Body:**
```json
{
  "reason": "Duplicate payment"
}
```

### Reverse Payment
```http
POST /payments/{paymentId}/reverse
```

**Request Body:**
```json
{
  "reason": "Vendor returned funds"
}
```

### Add Allocation
```http
POST /payments/{paymentId}/allocations
```

**Request Body:**
```json
{
  "invoiceId": "inv-001",
  "invoiceNumber": "INV-2024-001",
  "amount": 2500.00
}
```

### Set Payment Method Details
```http
PUT /payments/{paymentId}/payment-method
```

**Request Body:**
```json
{
  "bankAccountNumber": "123456789",
  "bankRoutingNumber": "987654321",
  "checkNumber": "CHK-12345"
}
```

### Set Fee
```http
PUT /payments/{paymentId}/fee
```

**Request Body:**
```json
{
  "feeAmount": 25.00
}
```

### Set Currency Conversion
```http
PUT /payments/{paymentId}/currency-conversion
```

**Request Body:**
```json
{
  "exchangeRate": "1.08",
  "originalCurrency": "EUR",
  "originalAmount": 6500.00
}
```

### Delete Payment
```http
DELETE /payments/{paymentId}
```

**Note:** Only pending or scheduled payments can be deleted.

---

## Summary API

### Get Accounts Payable Summary
```http
GET /summary?fromDate=2024-01-01&toDate=2024-01-31
```

**Query Parameters:**
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| fromDate | date | Yes | Start date for summary period |
| toDate | date | Yes | End date for summary period |

**Response (200 OK):**
```json
{
  "totalInvoices": 150,
  "pendingInvoices": 25,
  "approvedInvoices": 75,
  "paidInvoices": 45,
  "overdueInvoices": 5,
  "totalInvoiceAmount": 375000.00,
  "pendingAmount": 62500.00,
  "approvedAmount": 187500.00,
  "paidAmount": 112500.00,
  "overdueAmount": 12500.00,
  "totalPayments": 120,
  "pendingPayments": 10,
  "scheduledPayments": 15,
  "processingPayments": 5,
  "completedPayments": 85,
  "failedPayments": 5,
  "totalPaymentAmount": 350000.00,
  "pendingPaymentAmount": 30000.00,
  "completedPaymentAmount": 297500.00,
  "failedPaymentAmount": 12500.00,
  "totalVendors": 80,
  "activeVendors": 65,
  "pendingVendors": 10,
  "inactiveVendors": 5,
  "averagePaymentDays": 32,
  "discountsTaken": 5000.00,
  "discountsLost": 2500.00
}
```

---

## Error Responses

### Error Response Format
```json
{
  "timestamp": "2024-01-15T10:00:00Z",
  "status": 404,
  "error": "Not Found",
  "message": "Invoice with id 'inv-123' not found",
  "path": "/api/accounts-payable/invoices/inv-123",
  "details": {}
}
```

### Common HTTP Status Codes

| Status | Description |
|--------|-------------|
| 200 | OK - Request successful |
| 201 | Created - Resource created successfully |
| 202 | Accepted - Request accepted for processing |
| 204 | No Content - Request successful with no response body |
| 400 | Bad Request - Invalid request data |
| 401 | Unauthorized - Authentication required |
| 403 | Forbidden - Insufficient permissions |
| 404 | Not Found - Resource not found |
| 409 | Conflict - Resource already exists |
| 422 | Unprocessable Entity - Business logic validation failed |
| 500 | Internal Server Error - Server error |

---

## Pagination

All list endpoints support pagination:

**Response Headers:**
```
X-Total-Count: 150
X-Total-Pages: 8
X-Page-Number: 0
X-Page-Size: 20
```

**Request Parameters:**
- `page`: Page number (0-based)
- `size`: Page size (default: 20, max: 100)
- `sort`: Sort field and direction (e.g., `createdAt,desc`)

---

## Rate Limiting

API requests are rate limited:

| Tier | Requests per Minute |
|------|---------------------|
| Free | 60 |
| Standard | 600 |
| Premium | 6000 |

**Rate Limit Headers:**
```
X-RateLimit-Limit: 600
X-RateLimit-Remaining: 595
X-RateLimit-Reset: 1642252800
```

---

## Webhook Events

The service publishes webhook events for subscribed endpoints:

### Event Types

| Event | Description |
|-------|-------------|
| invoice.created | New invoice created |
| invoice.submitted | Invoice submitted for approval |
| invoice.approved | Invoice approved |
| invoice.rejected | Invoice rejected |
| invoice.paid | Invoice marked as paid |
| invoice.overdue | Invoice marked as overdue |
| vendor.registered | New vendor registered |
| vendor.activated | Vendor activated |
| payment.created | New payment created |
| payment.scheduled | Payment scheduled |
| payment.processing | Payment processing started |
| payment.completed | Payment completed |
| payment.failed | Payment failed |

### Webhook Signature

Webhook requests include a signature header:

```
X-Webhook-Signature: sha256=<signature>
```

Verify the signature using your webhook secret:

```java
Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
SecretKeySpec secret_key = new SecretKeySpec(webhookSecret.getBytes(), "HmacSHA256");
sha256_HMAC.init(secret_key);
byte[] signature = sha256_HMAC.doFinal(payload.getBytes());
```
