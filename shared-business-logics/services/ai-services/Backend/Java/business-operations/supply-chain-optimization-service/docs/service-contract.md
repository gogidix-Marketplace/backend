# Supply Chain Optimization Service - Service Contract

## Service Responsibility
AI-powered optimization of supply chain operations.

## Core Functionality
- Demand forecasting
- Inventory optimization
- Route optimization
- Supplier analysis

## API Contracts

### 1. Optimize Inventory
**Endpoint:** `POST /api/v1/supply-chain/inventory/optimize`

**Input:**
```json
{
  "products": [{"sku": "string", "currentStock": "integer"}],
  "forecastPeriod": "integer (days)",
  "constraints": {"warehouseCapacity": "integer"}
}
```

**Output:**
```json
{
  "optimizationId": "string (UUID)",
  "recommendations": [{
    "sku": "string",
    "action": "RESTOCK|REDUCE|MAINTAIN",
    "quantity": "integer",
    "reason": "string"
  }],
  "projectedSavings": "float"
}
```

### 2. Optimize Routes
**Endpoint:** `POST /api/v1/supply-chain/routes/optimize`

## Business Rules
- Forecast horizon: 1-90 days
- Min order quantity: configurable
- Safety stock: auto-calculated

## Error Conditions
- Invalid product data (400)
