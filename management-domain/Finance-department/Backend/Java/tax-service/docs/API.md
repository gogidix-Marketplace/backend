# Tax Service - API Documentation

## Base URL
```
/api/v1
```

## Tax Calculation API

### Calculate Tax
```http
POST /tax/calculate
Content-Type: application/json

{
  "transactionType": "SALE",
  "transactionDate": "2024-02-15",
  "amount": 1000.00,
  "currency": "USD",
  "jurisdiction": "CA",
  "taxCode": "STANDARD",
  "exempt": false,
  "customerTaxId": "12345"
}
```

Response:
```json
{
  "taxAmount": 82.50,
  "taxRate": 0.0825,
  "jurisdictions": [
    {
      "name": "State",
      "taxAmount": 62.50,
      "rate": 0.0625
    },
    {
      "name": "County",
      "taxAmount": 15.00,
      "rate": 0.0150
    },
    {
      "name": "City",
      "taxAmount": 5.00,
      "rate": 0.0050
    }
  ]
}
```

### Batch Calculate
```http
POST /tax/calculate/batch
Content-Type: application/json

{
  "transactions": [...]
}
```

## Tax Returns API

### Create Return
```http
POST /tax/returns
Content-Type: application/json

{
  "taxAuthorityId": "auth-001",
  "returnType": "SALES_TAX",
  "periodStart": "2024-01-01",
  "periodEnd": "2024-01-31"
}
```

### File Return
```http
PUT /tax/returns/{returnId}/file
Content-Type: application/json

{
  "filingDate": "2024-02-20",
  "confirmationNumber": "FILE-12345"
}
```

### Record Payment
```http
POST /tax/returns/{returnId}/pay
Content-Type: application/json

{
  "paymentDate": "2024-02-20",
  "amount": 5000.00,
  "paymentMethod": "ACH",
  "reference": "PAY-12345"
}
```
