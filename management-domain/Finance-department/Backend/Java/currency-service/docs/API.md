# Currency Service - API Documentation

## Base URL
```
http://{host}:8080/api/currency
```

---

## Currency API

### Create Currency
```http
POST /currencies
```

**Request Body:**
```json
{
  "currencyCode": "EUR",
  "name": "Euro",
  "symbol": "€",
  "decimalPlaces": 2,
  "isoNumericCode": "978",
  "countryCodes": ["DE", "FR", "IT", "ES", "NL"]
}
```

**Response (201 Created):**
```json
{
  "id": "507f1f77bcf86cd799439011",
  "currencyCode": "EUR",
  "name": "Euro",
  "symbol": "€",
  "decimalPlaces": 2,
  "isoNumericCode": "978",
  "status": "ACTIVE",
  "countryCodes": ["DE", "FR", "IT", "ES", "NL"],
  "createdAt": "2024-01-15T10:00:00Z"
}
```

### Get Currency
```http
GET /currencies/{currencyCode}
```

### List All Currencies
```http
GET /currencies?status=ACTIVE
```

### Update Currency
```http
PUT /currencies/{currencyCode}
```

**Request Body:**
```json
{
  "name": "Euro (EUR)",
  "symbol": "€",
  "decimalPlaces": 2,
  "isoNumericCode": "978"
}
```

---

## Exchange Rate API

### Update Exchange Rate
```http
POST /exchange-rates
```

**Request Body:**
```json
{
  "fromCurrency": "USD",
  "toCurrency": "EUR",
  "rate": 0.92,
  "source": "ECB",
  "validFrom": "2024-01-15T10:00:00Z"
}
```

**Response (201 Created):**
```json
{
  "id": "rate-abc123",
  "fromCurrency": "USD",
  "toCurrency": "EUR",
  "rate": 0.92,
  "inverseRate": 1.087,
  "source": "ECB",
  "validFrom": "2024-01-15T10:00:00Z"
}
```

### Get Exchange Rate
```http
GET /exchange-rates/{fromCurrency}/{toCurrency}
```

**Response (200 OK):**
```json
{
  "fromCurrency": "USD",
  "toCurrency": "EUR",
  "rate": 0.92,
  "inverseRate": 1.087,
  "validFrom": "2024-01-15T10:00:00Z"
}
```

---

## Currency Conversion API

### Convert Currency
```http
POST /conversions
```

**Request Body:**
```json
{
  "fromCurrency": "USD",
  "toCurrency": "EUR",
  "amount": 1000.00,
  "rateId": "rate-abc123"
}
```

**Response (201 Created):**
```json
{
  "conversionId": "conv-abc123",
  "fromCurrency": "USD",
  "toCurrency": "EUR",
  "fromAmount": 1000.00,
  "toAmount": 920.00,
  "rate": 0.92,
  "convertedAt": "2024-01-15T10:00:00Z"
}
```

### Calculate Conversion
```http
POST /conversions/calculate
```

**Request Body:**
```json
{
  "fromCurrency": "USD",
  "toCurrency": "EUR",
  "amount": 1000.00
}
```

**Response (200 OK):**
```json
{
  "fromCurrency": "USD",
  "toCurrency": "EUR",
  "amount": 1000.00,
  "convertedAmount": 920.00,
  "rate": 0.92,
  "rateValidAt": "2024-01-15T10:00:00Z"
}
```
