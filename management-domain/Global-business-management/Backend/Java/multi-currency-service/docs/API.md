# Multi-Currency Service - API Documentation

## Base URL
```
http://localhost:8080/api/v1/currency
```

## Endpoints

### Currency Conversion

#### Convert Amount
```http
GET /api/v1/currency/convert?amount=100.00&from=USD&to=EUR
```

**Response**: `200 OK`
```json
{
  "amount": 100.00,
  "fromCurrency": "USD",
  "toCurrency": "EUR",
  "convertedAmount": 85.00,
  "rate": 0.85,
  "timestamp": "2024-01-01T12:00:00Z"
}
```

#### Convert as of Date
```http
GET /api/v1/currency/convert/historical?amount=100.00&from=USD&to=EUR&date=2024-01-01
```

#### Get Exchange Rate
```http
GET /api/v1/currency/rate?from=USD&to=EUR
```

**Response**: `200 OK`
```json
{
  "fromCurrency": "USD",
  "toCurrency": "EUR",
  "rate": 0.85,
  "effectiveDate": "2024-01-01T12:00:00Z",
  "source": "ECB"
}
```

#### Get All Exchange Rates
```http
GET /api/v1/currency/rates/all?base=USD
```

**Response**: `200 OK`
```json
{
  "baseCurrency": "USD",
  "rates": {
    "EUR": 0.85,
    "GBP": 0.73,
    "JPY": 110.50,
    "CAD": 1.25,
    "AUD": 1.35
  },
  "timestamp": "2024-01-01T12:00:00Z"
}
```

#### Batch Exchange Rates
```http
POST /api/v1/currency/rates/batch
Content-Type: application/json

["USD/EUR", "USD/GBP", "EUR/GBP"]
```

**Response**: `200 OK`
```json
{
  "USD/EUR": 0.85,
  "USD/GBP": 0.73,
  "EUR/GBP": 0.86
}
```

#### Convert to Multiple Currencies
```http
POST /api/v1/currency/convert/multiple
Content-Type: application/json

{
  "amount": 100.00,
  "fromCurrency": "USD",
  "toCurrencies": ["EUR", "GBP", "JPY"]
}
```

**Response**: `200 OK`
```json
{
  "EUR": 85.00,
  "GBP": 73.00,
  "JPY": 11050.00
}
```

#### Calculate Cross Rate
```http
GET /api/v1/currency/cross-rate?from=EUR&to=GBP&base=USD
```

### Exchange Rate Management

#### Create/Update Exchange Rate
```http
POST /api/v1/currency/rates
Content-Type: application/json

{
  "fromCurrency": "USD",
  "toCurrency": "EUR",
  "rate": 0.85,
  "source": "MANUAL",
  "status": "ACTIVE",
  "effectiveDate": "2024-01-01T12:00:00Z"
}
```

#### Get Exchange Rate Entity
```http
GET /api/v1/currency/rates/entity?from=USD&to=EUR
```

#### Get Historical Rates
```http
GET /api/v1/currency/rates/historical?from=USD&to=EUR&limit=30
```

### Currency Information

#### Check Currency Support
```http
GET /api/v1/currency/supported/{code}
```

**Response**: `200 OK`
```json
{
  "currencyCode": "USD",
  "supported": true
}
```

#### Get Supported Currencies
```http
GET /api/v1/currency/supported
```

**Response**: `200 OK`
```json
{
  "currencies": [
    {
      "code": "USD",
      "name": "US Dollar",
      "symbol": "$",
      "decimalPlaces": 2,
      "status": "ACTIVE"
    },
    {
      "code": "EUR",
      "name": "Euro",
      "symbol": "€",
      "decimalPlaces": 2,
      "status": "ACTIVE"
    }
  ]
}
```

### Cache Management

#### Invalidate Cache for Pair
```http
POST /api/v1/currency/cache/invalidate?from=USD&to=EUR
```

#### Invalidate All Cache
```http
POST /api/v1/currency/cache/invalidate-all
```

## Error Responses

### 400 Bad Request
```json
{
  "timestamp": "2024-01-01T00:00:00Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Invalid currency code: XXX"
}
```

### 404 Not Found
```json
{
  "timestamp": "2024-01-01T00:00:00Z",
  "status": 404,
  "error": "Not Found",
  "message": "No exchange rate found from USD to XXX"
}
```

## Data Types

### BigDecimal
All rates and amounts use `BigDecimal` with up to 6 decimal places.

### Currency Codes
ISO 4217 three-letter currency codes (e.g., USD, EUR, GBP).

### Instant
Timestamps in ISO 8601 format UTC.

## Rate Sources

- **ECB**: European Central Bank
- **FED**: Federal Reserve
- **BANK_OF_ENGLAND**: Bank of England
- **BLOOMBERG**: Bloomberg
- **REUTERS**: Reuters
- **XE**: XE.com
- **OANDA**: OANDA
- **MANUAL**: Manually entered
- **CALCULATED**: Calculated from other rates

## Response Headers

- **X-Rate-Source**: The source of the exchange rate
- **X-Rate-Timestamp**: When the rate was last updated
- **X-Cache-Hit**: Indicates if the response was served from cache
