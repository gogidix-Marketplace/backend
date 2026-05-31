# AI Services API Documentation

## Base URLs

| Environment | Base URL |
|-------------|----------|
| Local | http://localhost |
| Development | https://ai-dev.gogidix.com |
| Production | https://ai.gogidix.com |

## Authentication

Most endpoints require authentication via JWT token:

```http
Authorization: Bearer <token>
```

## Service Endpoints

### ML Model Training Service
**Port**: 8001
**Base Path**: `/api/v1`

#### Create Training Job
```http
POST /api/v1/training/jobs
Content-Type: application/json

{
  "model_type": "classification",
  "algorithm": "random_forest",
  "training_config": {
    "n_estimators": 100,
    "max_depth": 10
  },
  "data_source": {
    "type": "file",
    "file_path": "/data/training.csv",
    "format": "csv"
  },
  "feature_columns": ["feature1", "feature2"],
  "target_column": "target"
}
```

**Response**:
```json
{
  "job_id": "550e8400-e29b-41d4-a716-446655440000",
  "model_type": "classification",
  "algorithm": "random_forest",
  "status": "started",
  "created_at": "2026-02-25T10:00:00Z"
}
```

#### Get Training Status
```http
GET /api/v1/training/jobs/{job_id}
```

#### List Models
```http
GET /api/v1/models?skip=0&limit=100&model_type=classification
```

#### Make Prediction
```http
POST /api/v1/models/{model_id}/predict
Content-Type: application/json

{
  "features": [[1.0, 2.0, 3.0]],
  "return_probabilities": true
}
```

---

### NLP Service
**Port**: 8002
**Base Path**: `/api/v1`

#### Sentiment Analysis
```http
POST /api/v1/sentiment/analyze
Content-Type: application/json

{
  "texts": ["I love this product!", "This is terrible."],
  "language": "en",
  "model": "rule-based"
}
```

**Response**:
```json
{
  "results": [
    {
      "text": "I love this product!",
      "label": "positive",
      "score": 0.85,
      "confidence": 0.92,
      "probabilities": {
        "positive": 0.92,
        "negative": 0.03,
        "neutral": 0.05
      }
    }
  ],
  "language": "en",
  "model_used": "rule-based",
  "processed_at": "2026-02-25T10:00:00Z"
}
```

#### Text Classification
```http
POST /api/v1/classification/predict
Content-Type: application/json

{
  "texts": ["The laptop is fast and reliable"],
  "categories": ["electronics", "clothing", "food"]
}
```

#### Named Entity Recognition
```http
POST /api/v1/ner/extract
Content-Type: application/json

{
  "texts": ["John Smith works at Google in California"],
  "entity_types": ["PERSON", "ORG", "LOC"]
}
```

#### Chatbot Message
```http
POST /api/v1/chatbot/message
Content-Type: application/json

{
  "session_id": "session-123",
  "messages": [
    {"role": "user", "content": "Where is my order?"}
  ],
  "bot_type": "customer_service"
}
```

---

### Predictive Analytics Service
**Port**: 8003
**Base Path**: `/api/v1`

#### Generate Forecast
```http
POST /api/v1/forecast/generate
Content-Type: application/json

{
  "historical_data": [100, 105, 98, 102, 110],
  "forecast_horizon": 5,
  "frequency": "daily",
  "model": "moving_average"
}
```

#### Demand Forecast
```http
POST /api/v1/demand/forecast
Content-Type: application/json

{
  "product_id": "PROD-123",
  "location": "warehouse-1",
  "forecast_horizon_days": 30,
  "include_promotions": true,
  "include_seasonality": true
}
```

#### Route Optimization
```http
POST /api/v1/routes/optimize
Content-Type: application/json

{
  "depot": {"lat": 40.7128, "lng": -74.0060},
  "deliveries": [
    {
      "id": "DEL-001",
      "location": {"lat": 40.7306, "lng": -73.9352},
      "demand": 10
    }
  ],
  "vehicles": [
    {
      "id": "VEH-001",
      "capacity": 100
    }
  ]
}
```

---

### Computer Vision Service
**Port**: 8004
**Base Path**: `/api/v1`

#### OCR Text Extraction
```http
POST /api/v1/ocr/extract
Content-Type: multipart/form-data

file: <image_file>
language: eng
preprocess: true
```

**Response**:
```json
{
  "text": "Extracted text from image",
  "confidence": 0.95,
  "language": "eng",
  "words": [...],
  "lines": [...],
  "processed_at": "2026-02-25T10:00:00Z"
}
```

#### Document Analysis
```http
POST /api/v1/ocr/document
Content-Type: multipart/form-data

file: <document_image>
doc_type: invoice
```

#### Image Classification
```http
POST /api/v1/classification/classify
Content-Type: multipart/form-data

file: <image_file>
top_k: 5
```

#### Object Detection
```http
POST /api/v1/detection/detect
Content-Type: multipart/form-data

file: <image_file>
confidence_threshold: 0.5
```

#### Face Detection
```http
POST /api/v1/faces/detect
Content-Type: multipart/form-data

file: <image_file>
return_landmarks: true
```

---

### Anomaly Detection Service
**Port**: 8005
**Base Path**: `/api/v1`

#### Fraud Detection
```http
POST /api/v1/fraud/detect
Content-Type: application/json

{
  "transaction": {
    "id": "TXN-12345",
    "user_id": "USER-001",
    "amount": 5000,
    "currency": "USD",
    "merchant": "Test Store",
    "location": {"country": "US"}
  },
  "user_history": [
    {"amount": 50, "merchant": "Store A"},
    {"amount": 75, "merchant": "Store B"}
  ]
}
```

**Response**:
```json
{
  "is_fraudulent": false,
  "fraud_score": 0.35,
  "risk_level": "medium",
  "reasons": ["High transaction amount: 5000"],
  "alert_triggered": true,
  "model_version": "fraud_detection_v1.0",
  "processed_at": "2026-02-25T10:00:00Z"
}
```

#### Anomaly Detection
```http
POST /api/v1/anomalies/detect
Content-Type: application/json

{
  "data": [10, 12, 11, 13, 50, 11, 12],
  "algorithm": "zscore",
  "threshold": 3.0
}
```

---

### Recommendation Service
**Port**: 8006
**Base Path**: `/api/v1`

#### Get Recommendations
```http
POST /api/v1/recommendations
Content-Type: application/json

{
  "user_id": "USER-001",
  "algorithm": "hybrid",
  "count": 10,
  "context": {
    "category": "electronics",
    "session_id": "session-123"
  }
}
```

**Response**:
```json
{
  "user_id": "USER-001",
  "recommendations": [
    {
      "item_id": "ITEM-001",
      "score": 0.92,
      "reason": "hybrid_recommendation"
    }
  ],
  "algorithm": "hybrid",
  "generated_at": "2026-02-25T10:00:00Z"
}
```

#### Get Similar Items
```http
GET /api/v1/recommendations/items/{item_id}/similar?count=10
```

#### User Similarity
```http
POST /api/v1/similarity/users
Content-Type: application/json

{
  "user_id1": "USER-001",
  "user_id2": "USER-002"
}
```

---

## Error Responses

All endpoints follow standard error response format:

```json
{
  "error": {
    "code": "VALIDATION_ERROR",
    "message": "Invalid input data",
    "details": {...}
  },
  "timestamp": "2026-02-25T10:00:00Z"
}
```

### Common Error Codes

| Code | HTTP Status | Description |
|------|-------------|-------------|
| VALIDATION_ERROR | 400 | Invalid request parameters |
| UNAUTHORIZED | 401 | Missing or invalid authentication |
| FORBIDDEN | 403 | Insufficient permissions |
| NOT_FOUND | 404 | Resource not found |
| CONFLICT | 409 | Resource conflict |
| RATE_LIMIT_EXCEEDED | 429 | Too many requests |
| INTERNAL_ERROR | 500 | Internal server error |

## Rate Limiting

| Tier | Requests | Window |
|------|----------|--------|
| Free | 100 | 1 hour |
| Standard | 1000 | 1 hour |
| Premium | 10000 | 1 hour |

## Pagination

List endpoints support pagination:

```http
GET /api/v1/models?skip=0&limit=100
```

| Parameter | Type | Default | Max |
|-----------|------|---------|-----|
| skip | integer | 0 | - |
| limit | integer | 100 | 1000 |

## API Documentation (Interactive)

- ML Training: http://localhost:8001/docs
- NLP: http://localhost:8002/docs
- Predictive Analytics: http://localhost:8003/docs
- Computer Vision: http://localhost:8004/docs
- Anomaly Detection: http://localhost:8005/docs
- Recommendations: http://localhost:8006/docs
