# Accounts Receivable Service - API Documentation

## Base URL
```
http://{host}:8080/api/accounts-receivable
```

---

## Customer API

### Create Customer
```http
POST /customers
```

**Request Body:**
```json
{
  "customerCode": "CUST-001",
  "customerName": "Acme Corporation",
  "customerType": "BUSINESS",
  "currency": "USD",
  "email": "contact@acme.com",
  "phone": "+1-555-0123",
  "website": "https://acme.com",
  "taxId": "12-3456789",
  "billingAddressLine1": "123 Main St",
  "billingCity": "Business City",
  "billingState": "BC",
  "billingPostalCode": "12345",
  "billingCountry": "USA",
  "shippingAddressLine1": "456 Shipping Ave",
  "shippingCity": "Ship City",
  "shippingState": "SC",
  "shippingPostalCode": "54321",
  "shippingCountry": "USA",
  "paymentTerms": "Net 30",
  "creditLimit": 10000,
  "creditDays": 30,
  "salesRepresentative": "John Smith",
  "industry": "Technology",
  "sendElectronicInvoices": true,
  "invoiceDeliveryEmail": "invoices@acme.com"
}
```

**Customer Types:** `INDIVIDUAL`, `BUSINESS`, `GOVERNMENT`, `NON_PROFIT`, `RESALE`, `INTERNATIONAL`

**Response (201 Created):**
```json
{
  "customerId": "cust-abc123",
  "customerCode": "CUST-001",
  "customerName": "Acme Corporation",
  "status": "ACTIVE",
  "creditLimit": 10000,
  "availableCredit": 10000,
  "outstandingBalance": 0.00,
  "collectionStage": "CURRENT"
}
```

### Get Customer
```http
GET /customers/{customerId}
```

### Update Customer Credit
```http
PUT /customers/{customerId}/credit
```

**Request Body:**
```json
{
  "creditLimit": 15000,
  "creditDays": 45
}
```

### Suspend Customer
```http
POST /customers/{customerId}/suspend
```

**Request Body:**
```json
{
  "reason": "Non-payment"
}
```

---

## Invoice API

### Generate Invoice
```http
POST /invoices
```

**Request Body:**
```json
{
  "customerId": "cust-001",
  "invoiceNumber": "INV-2024-001",
  "invoiceDate": "2024-01-15",
  "dueDate": "2024-02-15",
  "amount": 5000.00,
  "currency": "USD",
  "lineItems": [
    {
      "description": "Services rendered",
      "quantity": 100,
      "unitPrice": 50.00,
      "amount": 5000.00
    }
  ],
  "taxRate": 0.10,
  "notes": "Payment due within 30 days"
}
```

**Response (201 Created):**
```json
{
  "invoiceId": "inv-abc123",
  "invoiceNumber": "INV-2024-001",
  "customerId": "cust-001",
  "customerName": "Acme Corporation",
  "amount": 5000.00,
  "taxAmount": 500.00,
  "totalAmount": 5500.00,
  "status": "PENDING",
  "dueDate": "2024-02-15",
  "daysUntilDue": 31
}
```

### Apply Payment to Invoice
```http
POST /invoices/{invoiceId}/apply-payment
```

**Request Body:**
```json
{
  "paymentId": "pay-001",
  "amount": 2000.00
}
```

### Get Overdue Invoices
```http
GET /invoices/overdue
```

---

## Payment API

### Record Payment
```http
POST /payments
```

**Request Body:**
```json
{
  "customerId": "cust-001",
  "amount": 2500.00,
  "currency": "USD",
  "paymentMethod": "BANK_TRANSFER",
  "paymentDate": "2024-01-20",
  "reference": "REF-2024-001",
  "invoiceIds": ["inv-001", "inv-002"]
}
```

**Response (201 Created):**
```json
{
  "paymentId": "pay-abc123",
  "customerId": "cust-001",
  "amount": 2500.00,
  "paymentMethod": "BANK_TRANSFER",
  "status": "APPLIED",
  "createdAt": "2024-01-20T10:00:00Z"
}
```

### Get Payment
```http
GET /payments/{paymentId}
```

---

## Error Responses

```json
{
  "timestamp": "2024-01-15T10:00:00Z",
  "status": 404,
  "error": "Not Found",
  "message": "Customer with id 'cust-123' not found"
}
```
