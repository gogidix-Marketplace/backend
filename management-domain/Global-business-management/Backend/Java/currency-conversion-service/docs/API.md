# Currency Conversion Service - API Documentation

## Base URL
```
http://localhost:8080/api/v1/currency
```

## Convert Currency
```
GET /api/v1/currency/convert?amount=100&from=USD&to=EUR
```

**Response:**
```json
{
  "amount": 100.00,
  "from": "USD",
  "to": "EUR",
  "result": 92.50,
  "rate": 0.925,
  "timestamp": "2024-01-23T12:00:00Z"
}
```

## Get Exchange Rate
```
GET /api/v1/currency/rates?from=USD&to=EUR
```

## Get All Rates
```
GET /api/v1/currency/rates?base=USD
```

## Historical Rates
```
GET /api/v1/currency/rates/historical?date=2024-01-01&base=USD
```
