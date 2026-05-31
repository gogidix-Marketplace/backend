# Lead Scoring Service

A NestJS microservice for dynamic lead scoring with multiple scoring models, custom rules, and AI-powered predictive scoring.

## Architecture

This service follows **Hexagonal Architecture** (also known as Ports and Adapters) with clear separation of concerns:

### Domain Layer
Contains the core business logic and entities:

- **Entities**: `LeadScore`, `ScoreModel`, `ScoreRule`, `ScoreAttribute`, `ScoreComponent`
- **Events**: `LeadScoredEvent`, `ScoreDecayedEvent`, `ScoreModelUpdatedEvent`, `ScoreModelActivatedEvent`, `LeadQualifiedEvent`, `ScoreThresholdReachedEvent`
- **Ports**: Repository interfaces (input) and external service interfaces (output)

### Application Layer
Contains use cases and business logic orchestration:

- **Commands**: `ScoreLeadCommand`, `BatchScoreLeadsCommand`, `CreateScoreModelCommand`, etc.
- **Queries**: `GetLeadScoreQuery`, `GetScoreStatisticsQuery`, etc.
- **Handlers**: CQRS command and query handlers
- **DTOs**: Request/Response data transfer objects
- **Services**: Application services (`LeadScoringService`, `ScoreModelService`, etc.)

### Infrastructure Layer
Contains technical implementations:

- **MongoDB**: Repository implementations and Mongoose schemas
- **Kafka**: Event publishing and lead data consumption
- **Security**: Multi-tenancy guards, interceptors, and request context
- **Scheduling**: Cron jobs for score decay

### Interface Layer
Contains API controllers and presentation logic:

- **REST Controllers**: Lead scores, score models, rules, and attributes
- **Guards/Interceptors**: Tenant validation and context management

## Features

### Multiple Scoring Models
- **Demographic Scoring**: Based on lead demographics (job title, industry, etc.)
- **Behavioral Scoring**: Based on lead activities and engagement
- **Predictive AI**: Machine learning-based scoring
- **Custom Models**: Tenant-specific scoring configurations

### Custom Scoring Rules
- Weight-based rule evaluation
- Multiple condition operators (equals, contains, between, regex, etc.)
- Rule categories for organization
- Priority-based rule execution

### Lead Quality Classification
- **Hot Leads (Grade A)**: Score >= 80
- **Warm Leads (Grade B)**: Score >= 60
- **Cool Leads (Grade C)**: Score >= 40
- **Cold Leads (Grade D)**: Score < 40

### Score History & Trends
- Historical score snapshots
- Trend analysis over time
- Score decay tracking

### Multi-Tenancy
- Tenant-scoped data isolation
- Tenant context propagation
- Tenant-specific configurations

## API Endpoints

### Lead Scoring
- `POST /api/v1/lead-scores/score` - Score a single lead
- `POST /api/v1/lead-scores/batch-score` - Batch score multiple leads
- `POST /api/v1/lead-scores/rescore/:leadId` - Rescore a lead
- `GET /api/v1/lead-scores/lead/:leadId` - Get lead score
- `GET /api/v1/lead-scores` - Get all scores for tenant
- `GET /api/v1/lead-scores/qualified` - Get qualified leads
- `GET /api/v1/lead-scores/statistics` - Get score statistics
- `GET /api/v1/lead-scores/trends` - Get score trends
- `GET /api/v1/lead-scores/history/:leadId` - Get score history

### Score Models
- `POST /api/v1/score-models` - Create a scoring model
- `PUT /api/v1/score-models/:id` - Update a scoring model
- `POST /api/v1/score-models/:id/activate` - Activate a model
- `POST /api/v1/score-models/:id/deactivate` - Deactivate a model
- `GET /api/v1/score-models/:id` - Get a scoring model
- `GET /api/v1/score-models` - List scoring models

### Score Rules
- `POST /api/v1/score-rules` - Create a scoring rule
- `GET /api/v1/score-rules/model/:scoreModelId` - Get rules for a model
- `PUT /api/v1/score-rules/:id` - Update a rule
- `DELETE /api/v1/score-rules/:id` - Delete a rule

### Score Attributes
- `POST /api/v1/score-attributes` - Create a scoring attribute
- `GET /api/v1/score-attributes` - List scoring attributes
- `PUT /api/v1/score-attributes/:id` - Update an attribute
- `DELETE /api/v1/score-attributes/:id` - Delete an attribute

## Environment Variables

```env
# Application
NODE_ENV=development
PORT=3001
CORS_ORIGIN=*

# MongoDB
MONGODB_URI=mongodb://localhost:27017/lead-scoring

# Kafka
KAFKA_BROKERS=localhost:9092
KAFKA_CLIENT_ID=lead-scoring-service
KAFKA_CONSUMER_GROUP_ID=lead-scoring-consumer
KAFKA_LEAD_SCORED_TOPIC=lead.scored
KAFKA_SCORE_MODEL_UPDATED_TOPIC=scoring.model.updated
KAFKA_LEAD_QUALIFIED_TOPIC=lead.qualified

# Multi-tenancy
TENANT_IDS=tenant1,tenant2,tenant3

# Score Decay
SCORE_DECAY_ENABLED=true
SCORE_DECAY_RATE=0.1
SCORE_DECAY_PERIOD_DAYS=30
```

## Running the Service

### Development
```bash
npm install
npm run start:dev
```

### Production
```bash
npm run build
npm run start:prod
```

### Testing
```bash
npm run test
npm run test:cov
```

## Database Schema

### LeadScore
- `leadId`: Reference to the lead
- `tenantId`: Tenant identifier
- `scoreModelId`: Reference to the scoring model
- `totalScore`: Calculated score (0-100)
- `grade`: Quality grade (A, B, C, D)
- `breakdown`: Score component breakdown
- `attributes`: Attribute contributions
- `variantId`: A/B test variant (if applicable)
- `decayApplied`: Total decay applied
- `createdAt`: Creation timestamp
- `updatedAt`: Last update timestamp

### ScoreModel
- `tenantId`: Tenant identifier
- `name`: Model name
- `description`: Model description
- `modelType`: Type (demographic, behavioral, predictive, custom)
- `status`: Model status (draft, active, paused, archived)
- `scoringConfig`: Configuration object
- `ruleIds`: Associated rule IDs
- `attributeIds`: Associated attribute IDs
- `variants`: A/B test variants
- `isDefault`: Whether this is the default model

### ScoreRule
- `tenantId`: Tenant identifier
- `scoreModelId`: Parent model ID
- `name`: Rule name
- `ruleType`: Rule type
- `conditions`: Rule conditions
- `baseScore`: Base score value
- `maxScore`: Maximum score value
- `priority`: Execution priority
- `category`: Rule category

## Event Publishing

The service publishes the following events to Kafka:

- `LeadScored` - When a lead is scored
- `ScoreDecayed` - When score decay is applied
- `ScoreModelUpdated` - When a model is updated
- `ScoreModelActivated` - When a model is activated
- `LeadQualified` - When a lead qualifies
- `ScoreThresholdReached` - When a score threshold is reached

## Multi-Tenancy

All operations are scoped to a tenant. The tenant ID is extracted from:
1. Header `x-tenant-id`
2. Query parameter `tenantId`
3. Body field `tenantId`
4. User context (if authenticated)

## Score Decay

Score decay is applied automatically:
- Daily: Full decay calculation
- Hourly: Light decay check (for active leads)

Decay formula:
```
decayedScore = currentScore * (1 - decayRate) ^ (daysSinceActivity / decayPeriodDays)
```
