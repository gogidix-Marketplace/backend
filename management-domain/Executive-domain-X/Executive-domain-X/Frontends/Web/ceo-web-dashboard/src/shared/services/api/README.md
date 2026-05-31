# Executive Domain API Integration Layer

**Path:** `src/shared/services/api/`

## Overview

This directory contains all API client services for connecting the Executive Domain frontend to backend services. All clients use TypeScript with proper type definitions matching backend DTOs.

## Architecture

```
api/
├── client/                    # Base API client configuration
│   ├── axios-client.ts       # Axios instance with interceptors
│   ├── api-config.ts         # API endpoints configuration
│   └── types.ts              # Shared API types
├── services/                 # Service-specific API clients
│   ├── executive/            # Executive Backend services
│   │   ├── analytics.service.ts
│   │   ├── approval.service.ts
│   │   ├── strategy.service.ts
│   │   ├── financial.service.ts
│   │   ├── operations.service.ts
│   │   ├── technology.service.ts
│   │   ├── alert.service.ts
│   │   ├── audit.service.ts
│   │   └── dashboard.service.ts
│   ├── aggregation/          # Data Aggregation services
│   │   ├── data-aggregation.service.ts
│   │   ├── metrics-aggregation.service.ts
│   │   └── centralized-data.service.ts
│   ├── business/             # Shared Business Infrastructure
│   │   ├── ecommerce/        # E-commerce services (81 services)
│   │   ├── admin/            # Admin core services (11 services)
│   │   ├── courier/          # Courier services
│   │   └── warehousing/      # Warehousing services
│   ├── ai/                   # AI Services (44+ services)
│   │   ├── business-intelligence/
│   │   │   ├── churn-prediction.service.ts
│   │   │   ├── customer-segmentation.service.ts
│   │   │   ├── market-basket-analysis.service.ts
│   │   │   ├── product-recommendation.service.ts
│   │   │   ├── sales-forecasting.service.ts
│   │   │   ├── user-profiling.service.ts
│   │   │   ├── intelligence-analysis.service.ts
│   │   │   └── research-intelligence.service.ts
│   │   ├── customer-experience/
│   │   │   ├── chatbot.service.ts
│   │   │   ├── content-generation.service.ts
│   │   │   ├── customer-engagement.service.ts
│   │   │   ├── customer-feedback.service.ts
│   │   │   ├── email-optimization.service.ts
│   │   │   ├── notification.service.ts
│   │   │   ├── personalization.service.ts
│   │   │   ├── recommendation-engine.service.ts
│   │   │   ├── search.service.ts
│   │   │   ├── sentiment-analysis.service.ts
│   │   │   └── translation.service.ts
│   │   ├── data-analytics/
│   │   │   ├── analytics-dashboard.service.ts
│   │   │   ├── data-processing.service.ts
│   │   │   ├── data-validation.service.ts
│   │   │   ├── prediction.service.ts
│   │   │   ├── reporting.service.ts
│   │   │   ├── predictive-analytics.service.ts
│   │   │   └── time-series-forecasting.service.ts
│   │   ├── content-processing/
│   │   │   ├── document-classification.service.ts
│   │   │   ├── document-extraction.service.ts
│   │   │   ├── ocr.service.ts
│   │   │   ├── summarization.service.ts
│   │   │   └── nlp-processing.service.ts
│   │   └── security/
│   │       ├── fraud-detection.service.ts
│   │       └── security-analysis.service.ts
│   └── foundation/           # Foundation services
│       ├── auth.service.ts
│       ├── discovery.service.ts
│       └── config.service.ts
├── hooks/                    # React hooks for API calls
│   ├── useAnalytics.ts
│   ├── useApprovals.ts
│   ├── useStrategy.ts
│   ├── useFinancial.ts
│   ├── useOperations.ts
│   ├── useTechnology.ts
│   ├── useAI.ts
│   └── useRealtime.ts
├── websocket/                # WebSocket clients
│   ├── ws-client.ts
│   ├── ws-manager.ts
│   └── types.ts
└── mock/                     # Mock data for development
    ├── analytics.mock.ts
    ├── approvals.mock.ts
    └── strategy.mock.ts
```

## Service Endpoints Mapping

### Executive Backend Services (Port Range: 90xx-91xx)

| Service | Port | Base Path | Purpose |
|---------|------|-----------|---------|
| ceo-analytics-service | 9001 | /api/v1/analytics | CEO analytics data |
| ceo-approval-service | 9002 | /api/v1/approvals | CEO approval workflow |
| ceo-strategy-service | 9003 | /api/v1/strategy | CEO strategy & OKR |
| cfo-financial-consolidation | 9011 | /api/v1/financial | CFO financial data |
| coo-operations-service | 9021 | /api/v1/operations | COO operations data |
| cto-technology-oversight | 9031 | /api/v1/technology | CTO technology metrics |
| executive-alert-service | 9041 | /api/v1/alerts | Alert notifications |
| executive-audit-service | 9042 | /api/v1/audit | Audit trail |

### Data Aggregation Services (Port Range: 92xx)

| Service | Port | Base Path | Purpose |
|---------|------|-----------|---------|
| data-aggregation-service | 9201 | /api/v1/aggregation | Cross-domain aggregation |
| metrics-aggregation-service | 9202 | /api/v1/metrics | KPI metrics aggregation |
| centralized-data-aggregation | 9203 | /api/v1/centralized | Unified data layer |

### Shared Business Infrastructure (Port Range: 80xx-85xx)

| Domain | Services | Port Range | Base Path |
|--------|----------|------------|-----------|
| shared-ecommerce-core | 81 services | 8001-8081 | /api/v1/ecommerce/* |
| shared-admin-core | 11 services | 8501-8511 | /api/v1/admin/* |
| shared-courier-core | ~10 services | 8101-8110 | /api/v1/courier/* |
| shared-warehousing-core | ~8 services | 8201-8208 | /api/v1/warehouse/* |

### AI Services (Port Range: 70xx-79xx)

| AI Category | Services | Port Range | Base Path |
|-------------|----------|------------|-----------|
| business-intelligence | 8 services | 7001-7008 | /api/v1/ai/bi/* |
| customer-experience | 13 services | 7011-7023 | /api/v1/ai/cx/* |
| data-analytics | 7 services | 7031-7037 | /api/v1/ai/analytics/* |
| content-processing | 7 services | 7041-7047 | /api/v1/ai/content/* |
| ml-operations | 6 services | 7051-7056 | /api/v1/ai/ml/* |
| security-fraud | 3 services | 7061-7063 | /api/v1/ai/security/* |

## Usage Examples

### Using the Analytics Service

```typescript
import { analyticsService } from '@shared/services/api/services/executive/analytics.service'

// Get CEO analytics dashboard
const dashboard = await analyticsService.getDashboard()

// Get trending KPIs
const trendingKPIs = await analyticsService.getTrendingKPIs()

// Get analytics requiring attention
const alerts = await analyticsService.getRequiringAttention()
```

### Using AI Services

```typescript
import { salesForecastingService } from '@shared/services/api/services/ai/business-intelligence/sales-forecasting.service'
import { sentimentAnalysisService } from '@shared/services/api/services/ai/customer-experience/sentiment-analysis.service'

// Get AI sales forecast
const forecast = await salesForecastingService.getForecast({
  period: 'quarterly',
  horizon: 4
})

// Analyze customer sentiment
const sentiment = await sentimentAnalysisService.analyze({
  source: 'customer-feedback',
  timeframe: '7d'
})
```

### Using React Hooks

```typescript
import { useAnalytics } from '@shared/services/api/hooks/useAnalytics'
import { useAIInsights } from '@shared/services/api/hooks/useAI'

function CEOOverview() {
  const { data: dashboard, loading, error } = useAnalytics()
  const { insights } = useAIInsights('sales-forecasting')

  // Render dashboard with AI insights
}
```

## Configuration

### Environment Variables

```env
# API Base URLs
VITE_API_GATEWAY_URL=http://localhost:8080
VITE_EXECUTIVE_API_URL=http://localhost:9001
VITE_AGGREGATION_API_URL=http://localhost:9201
VITE_AI_API_URL=http://localhost:7001
VITE_BUSINESS_API_URL=http://localhost:8001

# WebSocket
VITE_WS_URL=ws://localhost:9090

# Authentication
VITE_AUTH_ISSUER=http://localhost:8080/auth
VITE_AUTH_CLIENT_ID=executive-dashboard
```

## Authentication Flow

All API calls use JWT tokens obtained from the authentication service:

1. Login → Receive JWT access token
2. Store token in Zustand authStore
3. Axios interceptor adds `Authorization: Bearer <token>` to all requests
4. Token refresh handled automatically before expiry

## Error Handling

All API clients follow consistent error handling:

- **400 Bad Request**: Validation error - display field errors
- **401 Unauthorized**: Token expired - trigger refresh
- **403 Forbidden**: Insufficient permissions - show access denied
- **404 Not Found**: Resource not found - show not found message
- **500 Internal Server Error**: Server error - show generic error message

## Mock Data Strategy

During development, mock data is used when:
1. Backend services are not available
2. `VITE_USE_MOCK_DATA=true` environment variable is set
3. Service returns error and fallback is enabled

Mock data files are in `api/mock/` directory and match real API response structure.
