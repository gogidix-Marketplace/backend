# 03 - Mock Flow Documentation

## AI & Orchestration Monitoring Dashboard

---

## Table of Contents

1. [API Architecture](#1-api-architecture)
2. [Data Models](#2-data-models)
3. [API Endpoints](#3-api-endpoints)
4. [Mock Data Examples](#4-mock-data-examples)
5. [WebSocket Events](#5-websocket-events)
6. [State Management](#6-state-management)
7. [Error Handling](#7-error-handling)

---

## 1. API Architecture

### 1.1 Gateway Architecture

```
┌─────────────────────────────────────────────────────────────────────┐
│                         AI Monitoring Dashboard                      │
│                         (Frontend - React)                           │
└─────────────────────────────────────┬───────────────────────────────┘
                                      │
                                      ▼
┌─────────────────────────────────────────────────────────────────────┐
│                    API Gateway (api-gateway-service)                │
│                         Port: 8907                                   │
├─────────────────────────────────────────────────────────────────────┤
│  • Authentication & Authorization                                    │
│  • Request Routing                                                   │
│  • Rate Limiting                                                     │
│  • Response Aggregation                                              │
└───────┬─────────────────────┬────────────────┬──────────────────────┘
        │                     │                │
        ▼                     ▼                ▼
┌──────────────────┐  ┌──────────────┐  ┌──────────────────┐
│  AI Services     │  │ Orchestration│  │ Transaction      │
│  Gateway         │  │ Services     │  │ Orchestration    │
│  Port: 9001      │  │ Port: 9002   │  │ Port: 9003       │
└──────────────────┘  └──────────────┘  └──────────────────┘
        │                     │                │
        ▼                     ▼                ▼
┌──────────────────┐  ┌──────────────┐  ┌──────────────────┐
│ 48 AI Services   │  │ 5 Orch. Svc  │  │ 3 Trans. Svc     │
│ (9001-9048)      │  │ (9100-9104)  │  │ (9200-9202)      │
└──────────────────┘  └──────────────┘  └──────────────────┘
                                              │
                                              ▼
                                    ┌──────────────────┐
                                    │  Universal       │
                                    │  Tracking        │
                                    │  Port: 9300      │
                                    └──────────────────┘
```

### 1.2 Base URL Configuration

```typescript
// Environment-based configuration
const API_CONFIG = {
  development: {
    baseURL: 'http://localhost:8907/api/v1',
    wsURL: 'ws://localhost:8908/ws'
  },
  staging: {
    baseURL: 'https://api-staging.gogidix.com/api/v1',
    wsURL: 'wss://ws-staging.gogidix.com/ws'
  },
  production: {
    baseURL: 'https://api.gogidix.com/api/v1',
    wsURL: 'wss://ws.gogidix.com/ws'
  }
};
```

### 1.3 Request Flow

```
Client Request
      │
      ▼
┌─────────────────┐
│  Authentication │──▶ No Token ─▶ 401 Unauthorized
│     Check       │
└────────┬────────┘
         │ Token Present
         ▼
┌─────────────────┐
│  Authorization  │──▶ No Permission ─▶ 403 Forbidden
│     Check       │
└────────┬────────┘
         │ Authorized
         ▼
┌─────────────────┐
│   Rate Limit    │──▶ Exceeded ─▶ 429 Too Many Requests
│     Check       │
└────────┬────────┘
         │ Within Limit
         ▼
┌─────────────────┐
│  Route Request  │
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│  Backend Service│
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│  Response Cache │
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│  Format Response│
└────────┬────────┘
         │
         ▼
    Client Response
```

---

## 2. Data Models

### 2.1 Core Types

```typescript
// ========================
// Base Types
// ========================

interface BaseEntity {
  id: string;
  createdAt: Date;
  updatedAt: Date;
  createdBy?: string;
  updatedBy?: string;
}

interface Timestamped {
  timestamp: Date;
}

interface ResponseMeta {
  page: number;
  pageSize: number;
  totalCount: number;
  hasMore: boolean;
}

// ========================
// Authentication Models
// ========================

interface User extends BaseEntity {
  email: string;
  name: string;
  avatar?: string;
  role: UserRole;
  permissions: Permission[];
  department?: string;
  lastLoginAt?: Date;
}

enum UserRole {
  SYSTEM_ADMIN = 'system_admin',
  DEVOPS_ENGINEER = 'devops_engineer',
  AI_ENGINEER = 'ai_engineer',
  PLATFORM_ENGINEER = 'platform_engineer',
  BUSINESS_ANALYST = 'business_analyst',
  EXECUTIVE = 'executive'
}

enum Permission {
  // Dashboard permissions
  DASHBOARD_VIEW = 'dashboard:view',
  DASHBOARD_EXPORT = 'dashboard:export',

  // AI Services permissions
  AI_SERVICE_VIEW = 'ai_service:view',
  AI_SERVICE_MANAGE = 'ai_service:manage',
  AI_MODEL_VIEW = 'ai_model:view',
  AI_MODEL_MANAGE = 'ai_model:manage',
  AI_TRAINING_VIEW = 'ai_training:view',
  AI_TRAINING_MANAGE = 'ai_training:manage',
  AI_EXPERIMENT_VIEW = 'ai_experiment:view',
  AI_EXPERIMENT_MANAGE = 'ai_experiment:manage',

  // Orchestration permissions
  ORCHESTRATION_VIEW = 'orchestration:view',
  ORCHESTRATION_MANAGE = 'orchestration:manage',
  AUDIT_TRAIL_VIEW = 'audit_trail:view',
  WORKFLOW_MANAGE = 'workflow:manage',

  // Transaction permissions
  TRANSACTION_VIEW = 'transaction:view',
  TRANSACTION_MANAGE = 'transaction:manage',
  SAGA_VIEW = 'saga:view',
  SAGA_MANAGE = 'saga:manage',

  // Tracking permissions
  TRACKING_VIEW = 'tracking:view',
  METRICS_VIEW = 'metrics:view',
  ALERT_MANAGE = 'alert:manage',
  REPORT_VIEW = 'report:view',
  REPORT_CREATE = 'report:create',

  // Admin permissions
  USER_MANAGE = 'user:manage',
  SETTINGS_MANAGE = 'settings:manage',
  API_KEY_MANAGE = 'api_key:manage'
}

interface AuthTokens {
  accessToken: string;
  refreshToken: string;
  expiresIn: number;
}

interface LoginRequest {
  email: string;
  password: string;
  rememberMe?: boolean;
}

interface LoginResponse {
  user: User;
  tokens: AuthTokens;
}

// ========================
// Service Models
// ========================

enum ServiceCategory {
  AI_CORE = 'ai_core',
  AI_MANAGEMENT = 'ai_management',
  AI_SPECIALIZED = 'ai_specialized',
  AI_ADVANCED = 'ai_advanced',
  ORCHESTRATION = 'orchestration',
  TRANSACTION = 'transaction',
  TRACKING = 'tracking'
}

enum ServiceStatus {
  HEALTHY = 'healthy',
  DEGRADED = 'degraded',
  DOWN = 'down',
  STARTING = 'starting',
  STOPPING = 'stopping',
  UNKNOWN = 'unknown'
}

interface Service extends BaseEntity {
  name: string;
  displayName: string;
  category: ServiceCategory;
  status: ServiceStatus;
  version: string;
  description: string;
  documentationUrl?: string;
  repositoryUrl?: string;
  endpoints: ServiceEndpoint[];
  healthCheckUrl: string;
  metrics: ServiceMetrics;
  configuration: ServiceConfiguration;
  dependencies: string[];
}

interface ServiceEndpoint {
  path: string;
  method: 'GET' | 'POST' | 'PUT' | 'DELETE' | 'PATCH';
  description: string;
  authenticated: boolean;
  rateLimit?: number;
}

interface ServiceMetrics {
  requestsPerSecond: number;
  averageLatency: number;
  errorRate: number;
  p50Latency: number;
  p95Latency: number;
  p99Latency: number;
  uptimePercentage: number;
  lastRestartAt?: Date;
}

interface ServiceConfiguration {
  cpu: {
    limit: number;
    request: number;
    utilization: number;
  };
  memory: {
    limit: number;  // in MB
    request: number;
    utilization: number;
  };
  gpu?: {
    count: number;
    utilization: number;
    memory: number;
  };
  replicas: {
    current: number;
    min: number;
    max: number;
  };
  autoScaling: {
    enabled: boolean;
    targetCPU?: number;
    targetMemory?: number;
  };
}

// ========================
// Alert Models
// ========================

enum AlertSeverity {
  CRITICAL = 'critical',
  WARNING = 'warning',
  INFO = 'info',
  SUCCESS = 'success'
}

enum AlertStatus {
  ACTIVE = 'active',
  ACKNOWLEDGED = 'acknowledged',
  RESOLVED = 'resolved',
  SNOOZED = 'snoozed'
}

interface Alert extends BaseEntity {
  id: string;
  severity: AlertSeverity;
  status: AlertStatus;
  title: string;
  description: string;
  serviceId: string;
  serviceName: string;
  metric: string;
  threshold: number;
  currentValue: number;
  triggeredAt: Date;
  acknowledgedAt?: Date;
  resolvedAt?: Date;
  acknowledgedBy?: string;
  resolvedBy?: string;
  metadata: Record<string, unknown>;
}

// ========================
// AI Service Models
// ========================

interface Model extends BaseEntity {
  id: string;
  name: string;
  version: string;
  serviceId: string;
  type: ModelType;
  framework: ModelFramework;
  status: ModelStatus;
  performance: ModelPerformance;
  training: ModelTrainingInfo;
  deployment: ModelDeployment;
  drift: ModelDriftInfo;
  features: string[];
}

enum ModelType {
  CLASSIFICATION = 'classification',
  REGRESSION = 'regression',
  CLUSTERING = 'clustering',
  RECOMMENDATION = 'recommendation',
  NLP = 'nlp',
  COMPUTER_VISION = 'computer_vision',
  ANOMALY_DETECTION = 'anomaly_detection',
  TIME_SERIES = 'time_series',
  CUSTOM = 'custom'
}

enum ModelFramework {
  TENSORFLOW = 'tensorflow',
  PYTORCH = 'pytorch',
  SCIKIT_LEARN = 'scikit_learn',
  XGBOOST = 'xgboost',
  ONNX = 'onnx',
  CUSTOM = 'custom'
}

enum ModelStatus {
  TRAINING = 'training',
  TRAINED = 'trained',
  DEPLOYING = 'deploying',
  DEPLOYED = 'deployed',
  RETIRED = 'retired',
  FAILED = 'failed'
}

interface ModelPerformance {
  accuracy?: number;
  precision?: number;
  recall?: number;
  f1Score?: number;
  auc?: number;
  mae?: number;
  mse?: number;
  rmse?: number;
  latency: number;
  throughput: number;
  lastEvaluatedAt: Date;
}

interface ModelTrainingInfo {
  startedAt?: Date;
  completedAt?: Date;
  duration?: number;
  dataset: string;
  hyperparameters: Record<string, unknown>;
  metrics: Record<string, number>;
}

interface ModelDeployment {
  environment: 'development' | 'staging' | 'production';
  endpoint: string;
  version: string;
  deployedAt: Date;
  scaledToZero: boolean;
}

interface ModelDriftInfo {
  detected: boolean;
  score: number;
  lastCheckedAt: Date;
  featuresDrifted: string[];
}

interface TrainingJob extends BaseEntity {
  id: string;
  modelId: string;
  modelName: string;
  status: 'pending' | 'running' | 'completed' | 'failed' | 'cancelled';
  progress: number;
  startedAt: Date;
  completedAt?: Date;
  estimatedCompletionAt?: Date;
  config: TrainingConfig;
  metrics: TrainingMetrics;
  logs: TrainingLogEntry[];
}

interface TrainingConfig {
  algorithm: string;
  hyperparameters: Record<string, unknown>;
  dataset: {
    name: string;
    version: string;
    size: number;
  };
  resources: {
    cpu: number;
    memory: number;
    gpu?: number;
  };
}

interface TrainingMetrics {
  loss: number;
  accuracy?: number;
  validationLoss: number;
  validationAccuracy?: number;
  epochs: number;
  currentEpoch: number;
}

interface TrainingLogEntry {
  timestamp: Date;
  level: 'info' | 'warning' | 'error';
  message: string;
}

interface Experiment extends BaseEntity {
  id: string;
  name: string;
  description: string;
  status: 'draft' | 'running' | 'completed' | 'archived';
  type: 'ab_test' | 'multivariate' | 'canary';
  models: ExperimentModel[];
  trafficSplit: Record<string, number>;
  metrics: ExperimentMetrics;
  startDate?: Date;
  endDate?: Date;
  duration?: number;
}

interface ExperimentModel {
  modelId: string;
  modelName: string;
  version: string;
  isControl: boolean;
}

interface ExperimentMetrics {
  primaryMetric: string;
  results: Record<string, ExperimentMetricResult>;
  winner?: string;
  confidence: number;
}

interface ExperimentMetricResult {
  modelId: string;
  value: number;
  change: number;
  changePercent: number;
  isSignificant: boolean;
}

// ========================
// Orchestration Models
// ========================

interface Workflow extends BaseEntity {
  id: string;
  name: string;
  type: WorkflowType;
  status: WorkflowStatus;
  definition: WorkflowDefinition;
  execution: WorkflowExecution;
  steps: WorkflowStep[];
  input: Record<string, unknown>;
  output?: Record<string, unknown>;
  error?: WorkflowError;
}

enum WorkflowType {
  USER_ONBOARDING = 'user_onboarding',
  SERVICE_ONBOARDING = 'service_onboarding',
  MODEL_TRAINING = 'model_training',
  ORDER_PROCESSING = 'order_processing',
  PAYMENT_PROCESSING = 'payment_processing',
  DATA_PIPELINE = 'data_pipeline',
  CUSTOM = 'custom'
}

enum WorkflowStatus {
  PENDING = 'pending',
  RUNNING = 'running',
  PAUSED = 'paused',
  COMPLETED = 'completed',
  FAILED = 'failed',
  CANCELLED = 'cancelled',
  ROLLED_BACK = 'rolled_back'
}

interface WorkflowDefinition {
  id: string;
  version: string;
  description: string;
  stepsCount: number;
  estimatedDuration?: number;
}

interface WorkflowExecution {
  startedAt: Date;
  completedAt?: Date;
  duration?: number;
  currentStep: number;
  completedSteps: number;
  progress: number;
  retryCount: number;
}

interface WorkflowStep {
  id: string;
  name: string;
  type: string;
  status: StepStatus;
  startedAt?: Date;
  completedAt?: Date;
  duration?: number;
  input?: Record<string, unknown>;
  output?: Record<string, unknown>;
  error?: string;
}

enum StepStatus {
  PENDING = 'pending',
  RUNNING = 'running',
  COMPLETED = 'completed',
  SKIPPED = 'skipped',
  FAILED = 'failed',
  RETRYING = 'retrying'
}

interface WorkflowError {
  code: string;
  message: string;
  stepId?: string;
  stackTrace?: string;
  occurredAt: Date;
}

interface AuditLog extends BaseEntity {
  id: string;
  timestamp: Date;
  userId?: string;
  serviceId?: string;
  workflowId?: string;
  action: AuditAction;
  resourceType: string;
  resourceId: string;
  changes?: AuditChange[];
  metadata: Record<string, unknown>;
  ipAddress?: string;
  userAgent?: string;
}

enum AuditAction {
  CREATE = 'create',
  READ = 'read',
  UPDATE = 'update',
  DELETE = 'delete',
  START = 'start',
  STOP = 'stop',
  DEPLOY = 'deploy',
  SCALE = 'scale',
  RESTART = 'restart'
}

interface AuditChange {
  field: string;
  oldValue: unknown;
  newValue: unknown;
}

// ========================
// Transaction Models
// ========================

interface Saga extends BaseEntity {
  id: string;
  type: SagaType;
  status: SagaStatus;
  definition: SagaDefinition;
  execution: SagaExecution;
  steps: SagaStep[];
  compensations: CompensationAction[];
  currentStep: number;
  input: Record<string, unknown>;
  output?: Record<string, unknown>;
}

enum SagaType {
  ORDER_PROCESSING = 'order_processing',
  PAYMENT_PROCESSING = 'payment_processing',
  REFUND_PROCESSING = 'refund_processing',
  INVENTORY_RESERVATION = 'inventory_reservation',
  SHIPMENT_ORCHESTRATION = 'shipment_orchestration',
  CUSTOM = 'custom'
}

enum SagaStatus {
  PENDING = 'pending',
  RUNNING = 'running',
  COMPLETED = 'completed',
  FAILED = 'failed',
  COMPENSATING = 'compensating',
  COMPENSATED = 'compensated',
  ABORTED = 'aborted'
}

interface SagaDefinition {
  id: string;
  name: string;
  version: string;
  steps: SagaStepDefinition[];
}

interface SagaExecution {
  startedAt: Date;
  completedAt?: Date;
  duration?: number;
  retryCount: number;
}

interface SagaStepDefinition {
  id: string;
  name: string;
  service: string;
  action: string;
  timeout: number;
  retryPolicy: RetryPolicy;
}

interface RetryPolicy {
  maxAttempts: number;
  backoffMs: number;
  maxBackoffMs: number;
}

interface SagaStep {
  id: string;
  definition: SagaStepDefinition;
  status: SagaStepStatus;
  startedAt?: Date;
  completedAt?: Date;
  duration?: number;
  attempt: number;
  output?: Record<string, unknown>;
  error?: string;
}

enum SagaStepStatus {
  PENDING = 'pending',
  RUNNING = 'running',
  COMPLETED = 'completed',
  FAILED = 'failed',
  SKIPPED = 'skipped'
}

interface CompensationAction {
  stepId: string;
  stepName: string;
  status: 'pending' | 'running' | 'completed' | 'failed';
  startedAt?: Date;
  completedAt?: Date;
  error?: string;
}

interface StateMachine extends BaseEntity {
  id: string;
  name: string;
  type: string;
  currentState: State;
  states: StateDefinition[];
  transitions: StateTransition[];
  history: StateTransitionHistory[];
}

interface State {
  id: string;
  name: string;
  enteredAt: Date;
}

interface StateDefinition {
  id: string;
  name: string;
  type: 'initial' | 'normal' | 'final' | 'choice' | 'parallel';
  actions?: string[];
}

interface StateTransition {
  from: string;
  to: string;
  event: string;
  condition?: string;
  actions?: string[];
}

interface StateTransitionHistory {
  from: string;
  to: string;
  event: string;
  timestamp: Date;
  triggeredBy?: string;
}

// ========================
// Tracking Models
// ========================

interface MetricDataPoint {
  timestamp: Date;
  value: number;
  labels?: Record<string, string>;
}

interface TimeSeriesData {
  metric: string;
  datapoints: MetricDataPoint[];
  aggregation: 'avg' | 'sum' | 'min' | 'max' | 'count';
  interval: number;
}

interface ServiceMetricsSummary {
  serviceId: string;
  serviceName: string;
  requestRate: number;
  errorRate: number;
  latency: LatencyMetrics;
  throughput: number;
  resource: ResourceMetrics;
}

interface LatencyMetrics {
  p50: number;
  p75: number;
  p90: number;
  p95: number;
  p99: number;
  max: number;
  avg: number;
}

interface ResourceMetrics {
  cpuPercent: number;
  memoryBytes: number;
  memoryPercent: number;
  diskBytes: number;
  diskPercent: number;
  gpuPercent?: number;
  gpuMemoryBytes?: number;
}

interface AlertRule extends BaseEntity {
  id: string;
  name: string;
  description: string;
  enabled: boolean;
  severity: AlertSeverity;
  condition: AlertCondition;
  actions: AlertAction[];
  cooldown: number;
  lastTriggered?: Date;
  triggerCount: number;
}

interface AlertCondition {
  metric: string;
  operator: 'gt' | 'lt' | 'eq' | 'ne' | 'gte' | 'lte';
  threshold: number;
  duration: number;
  aggregation?: 'avg' | 'sum' | 'min' | 'max';
}

interface AlertAction {
  type: 'email' | 'webhook' | 'slack' | 'pagerduty';
  config: Record<string, unknown>;
}

interface Report extends BaseEntity {
  id: string;
  name: string;
  type: ReportType;
  format: 'pdf' | 'csv' | 'json';
  status: 'pending' | 'generating' | 'completed' | 'failed';
  config: ReportConfig;
  result?: {
    url: string;
    size: number;
    expiresAt: Date;
  };
  scheduledAt?: Date;
}

enum ReportType {
  SERVICE_PERFORMANCE = 'service_performance',
  AI_MODEL_PERFORMANCE = 'ai_model_performance',
  WORKFLOW_SUMMARY = 'workflow_summary',
  TRANSACTION_SUMMARY = 'transaction_summary',
  CUSTOM = 'custom'
}

interface ReportConfig {
  dateRange: {
    start: Date;
    end: Date;
  };
  services?: string[];
  metrics: string[];
  includeCharts: boolean;
  includeRawData: boolean;
}
```

---

## 3. API Endpoints

### 3.1 Authentication Endpoints

```typescript
// POST /auth/login
interface LoginRequest {
  email: string;
  password: string;
  rememberMe?: boolean;
}

interface LoginResponse {
  user: User;
  tokens: AuthTokens;
}

// POST /auth/refresh
interface RefreshTokenRequest {
  refreshToken: string;
}

interface RefreshTokenResponse {
  accessToken: string;
  expiresIn: number;
}

// POST /auth/logout
interface LogoutRequest {
  refreshToken: string;
}

// GET /auth/me
interface GetMeResponse {
  user: User;
  permissions: Permission[];
}

// POST /auth/forgot-password
interface ForgotPasswordRequest {
  email: string;
}

// POST /auth/reset-password
interface ResetPasswordRequest {
  token: string;
  newPassword: string;
}
```

### 3.2 Dashboard Endpoints

```typescript
// GET /dashboard/overview
interface DashboardOverviewResponse {
  health: {
    score: number;
    totalServices: number;
    healthyServices: number;
    degradedServices: number;
    downServices: number;
  };
  metrics: {
    requestsPerSecond: number;
    averageLatency: number;
    errorRate: number;
    activeWorkflows: number;
    activeTransactions: number;
  };
  alerts: Alert[];
  topServices: Service[];
  trends: {
    requests: TimeSeriesData;
    latency: TimeSeriesData;
    errors: TimeSeriesData;
  };
}

// GET /dashboard/executive-summary
interface ExecutiveSummaryResponse {
  overview: DashboardOverviewResponse['health'];
  costs: {
    today: number;
    thisMonth: number;
    lastMonth: number;
    forecast: number;
    budget: number;
  };
  initiatives: {
    trainingJobs: { active: number; queued: number };
    experiments: { active: number; planned: number };
    newModels: number;
  };
  issues: {
    critical: number;
    warning: number;
  };
}
```

### 3.3 AI Services Endpoints

```typescript
// GET /ai-services
interface GetAIServicesRequest {
  page?: number;
  pageSize?: number;
  category?: ServiceCategory;
  status?: ServiceStatus;
  search?: string;
}

interface GetAIServicesResponse {
  services: Service[];
  meta: ResponseMeta;
}

// GET /ai-services/:serviceId
interface GetAIServiceResponse {
  service: Service;
  metrics: ServiceMetrics;
  alerts: Alert[];
  recentLogs: LogEntry[];
}

// GET /ai-services/:serviceId/metrics
interface GetServiceMetricsRequest {
  from: Date;
  to: Date;
  interval: number;
  metrics: string[];
}

interface GetServiceMetricsResponse {
  serviceId: string;
  metrics: Record<string, TimeSeriesData>;
}

// PUT /ai-services/:serviceId/config
interface UpdateServiceConfigRequest {
  cpu?: { limit?: number; request?: number };
  memory?: { limit?: number; request?: number };
  replicas?: { current?: number; min?: number; max?: number };
  autoScaling?: {
    enabled?: boolean;
    targetCPU?: number;
    targetMemory?: number;
  };
}

// POST /ai-services/:serviceId/restart
interface RestartServiceResponse {
  serviceId: string;
  status: 'restarting';
  estimatedDowntime: number;
}

// GET /ai-services/:serviceId/logs
interface GetServiceLogsRequest {
  from?: Date;
  to?: Date;
  level?: 'debug' | 'info' | 'warn' | 'error';
  limit?: number;
  search?: string;
}

interface GetServiceLogsResponse {
  logs: LogEntry[];
  hasMore: boolean;
}

interface LogEntry {
  timestamp: Date;
  level: string;
  message: string;
  context?: Record<string, unknown>;
}
```

### 3.4 AI Models Endpoints

```typescript
// GET /ai-models
interface GetAIModelsRequest {
  page?: number;
  pageSize?: number;
  serviceId?: string;
  type?: ModelType;
  status?: ModelStatus;
}

interface GetAIModelsResponse {
  models: Model[];
  meta: ResponseMeta;
}

// GET /ai-models/:modelId
interface GetAIModelResponse {
  model: Model;
  performance: ModelPerformance;
  drift: ModelDriftInfo;
  predictions: {
    last24h: number;
    avgLatency: number;
  };
}

// GET /ai-models/:modelId/performance
interface GetModelPerformanceRequest {
  from: Date;
  to: Date;
}

interface GetModelPerformanceResponse {
  modelId: string;
  metrics: {
    accuracy: TimeSeriesData;
    precision: TimeSeriesData;
    recall: TimeSeriesData;
    latency: TimeSeriesData;
  };
}

// POST /ai-models/:modelId/retrain
interface RetrainModelRequest {
  dataset?: string;
  hyperparameters?: Record<string, unknown>;
  priority?: 'low' | 'normal' | 'high';
}

interface RetrainModelResponse {
  modelId: string;
  trainingJobId: string;
  status: 'scheduled' | 'started';
  estimatedCompletionAt?: Date;
}
```

### 3.5 Training Jobs Endpoints

```typescript
// GET /training-jobs
interface GetTrainingJobsRequest {
  page?: number;
  pageSize?: number;
  status?: string;
  modelId?: string;
}

interface GetTrainingJobsResponse {
  jobs: TrainingJob[];
  meta: ResponseMeta;
}

// GET /training-jobs/:jobId
interface GetTrainingJobResponse {
  job: TrainingJob;
  logs: TrainingLogEntry[];
}

// POST /training-jobs/:jobId/cancel
interface CancelTrainingJobResponse {
  jobId: string;
  status: 'cancelled';
}

// GET /training-jobs/:jobId/logs
interface GetTrainingLogsResponse {
  logs: TrainingLogEntry[];
  hasMore: boolean;
}
```

### 3.6 Experiments Endpoints

```typescript
// GET /experiments
interface GetExperimentsRequest {
  page?: number;
  pageSize?: number;
  status?: string;
}

interface GetExperimentsResponse {
  experiments: Experiment[];
  meta: ResponseMeta;
}

// POST /experiments
interface CreateExperimentRequest {
  name: string;
  description: string;
  type: 'ab_test' | 'multivariate' | 'canary';
  models: {
    modelId: string;
    isControl: boolean;
    trafficPercentage: number;
  }[];
  duration: number;
  primaryMetric: string;
}

interface CreateExperimentResponse {
  experiment: Experiment;
}

// POST /experiments/:experimentId/start
interface StartExperimentResponse {
  experimentId: string;
  status: 'running';
  startedAt: Date;
}

// POST /experiments/:experimentId/stop
interface StopExperimentResponse {
  experimentId: string;
  status: 'completed';
  completedAt: Date;
  winner?: string;
}

// GET /experiments/:experimentId/results
interface GetExperimentResultsResponse {
  experiment: Experiment;
  metrics: ExperimentMetrics;
  confidence: number;
  recommendation: string;
}
```

### 3.7 Orchestration Endpoints

```typescript
// GET /workflows
interface GetWorkflowsRequest {
  page?: number;
  pageSize?: number;
  type?: WorkflowType;
  status?: WorkflowStatus;
  search?: string;
}

interface GetWorkflowsResponse {
  workflows: Workflow[];
  meta: ResponseMeta;
}

// GET /workflows/:workflowId
interface GetWorkflowResponse {
  workflow: Workflow;
  history: AuditLog[];
}

// POST /workflows/:workflowId/pause
interface PauseWorkflowResponse {
  workflowId: string;
  status: 'paused';
}

// POST /workflows/:workflowId/resume
interface ResumeWorkflowResponse {
  workflowId: string;
  status: 'running';
}

// POST /workflows/:workflowId/cancel
interface CancelWorkflowResponse {
  workflowId: string;
  status: 'cancelled';
}

// GET /workflows/:workflowId/steps/:stepId
interface GetWorkflowStepResponse {
  step: WorkflowStep;
  input: Record<string, unknown>;
  output?: Record<string, unknown>;
  logs: LogEntry[];
}

// POST /workflows/:workflowId/steps/:stepId/retry
interface RetryWorkflowStepResponse {
  stepId: string;
  status: 'retrying';
}
```

### 3.8 Audit Trail Endpoints

```typescript
// GET /audit-trail
interface GetAuditLogsRequest {
  page?: number;
  pageSize?: number;
  userId?: string;
  serviceId?: string;
  workflowId?: string;
  action?: AuditAction;
  from?: Date;
  to?: Date;
}

interface GetAuditLogsResponse {
  logs: AuditLog[];
  meta: ResponseMeta;
}

// GET /audit-trail/:logId
interface GetAuditLogResponse {
  log: AuditLog;
  changes: AuditChange[];
}
```

### 3.9 Transaction Endpoints

```typescript
// GET /sagas
interface GetSagasRequest {
  page?: number;
  pageSize?: number;
  type?: SagaType;
  status?: SagaStatus;
  search?: string;
}

interface GetSagasResponse {
  sagas: Saga[];
  meta: ResponseMeta;
}

// GET /sagas/:sagaId
interface GetSagaResponse {
  saga: Saga;
  events: Event[];
}

// GET /sagas/:sagaId/events
interface GetSagaEventsResponse {
  events: Event[];
  hasMore: boolean;
}

interface Event {
  id: string;
  sagaId: string;
  type: string;
  data: Record<string, unknown>;
  timestamp: Date;
}

// POST /sagas/:sagaId/force-complete
interface ForceCompleteSagaResponse {
  sagaId: string;
  status: 'completed';
  completedAt: Date;
}

// POST /sagas/:sagaId/compensate
interface TriggerCompensationResponse {
  sagaId: string;
  status: 'compensating';
}
```

### 3.10 State Machine Endpoints

```typescript
// GET /state-machines
interface GetStateMachinesRequest {
  page?: number;
  pageSize?: number;
  type?: string;
}

interface GetStateMachinesResponse {
  stateMachines: StateMachine[];
  meta: ResponseMeta;
}

// GET /state-machines/:smId
interface GetStateMachineResponse {
  stateMachine: StateMachine;
  currentState: State;
  availableTransitions: StateTransition[];
}

// POST /state-machines/:smId/transition
interface TriggerStateTransitionRequest {
  event: string;
  data?: Record<string, unknown>;
}

interface TriggerStateTransitionResponse {
  stateMachineId: string;
  previousState: string;
  newState: string;
  transitionedAt: Date;
}
```

### 3.11 Tracking & Metrics Endpoints

```typescript
// GET /metrics/realtime
interface GetRealTimeMetricsResponse {
  timestamp: Date;
  services: ServiceMetricsSummary[];
  system: {
    cpuPercent: number;
    memoryPercent: number;
    diskPercent: number;
  };
}

// GET /metrics/historical
interface GetHistoricalMetricsRequest {
  from: Date;
  to: Date;
  interval: number;
  services?: string[];
  metrics: string[];
}

interface GetHistoricalMetricsResponse {
  metrics: Record<string, TimeSeriesData>;
}

// GET /metrics/service/:serviceId
interface GetServiceMetricsResponse {
  serviceId: string;
  metrics: {
    requests: TimeSeriesData;
    latency: TimeSeriesData;
    errors: TimeSeriesData;
  };
  summary: LatencyMetrics;
}
```

### 3.12 Alerts Endpoints

```typescript
// GET /alerts
interface GetAlertsRequest {
  page?: number;
  pageSize?: number;
  severity?: AlertSeverity;
  status?: AlertStatus;
  serviceId?: string;
}

interface GetAlertsResponse {
  alerts: Alert[];
  meta: ResponseMeta;
}

// POST /alerts/:alertId/acknowledge
interface AcknowledgeAlertRequest {
  note?: string;
}

interface AcknowledgeAlertResponse {
  alertId: string;
  status: 'acknowledged';
  acknowledgedAt: Date;
  acknowledgedBy: string;
}

// POST /alerts/:alertId/resolve
interface ResolveAlertResponse {
  alertId: string;
  status: 'resolved';
  resolvedAt: Date;
  resolvedBy: string;
}

// POST /alerts/:alertId/snooze
interface SnoozeAlertRequest {
  duration: number;
}

interface SnoozeAlertResponse {
  alertId: string;
  status: 'snoozed';
  snoozedUntil: Date;
}

// GET /alert-rules
interface GetAlertRulesResponse {
  rules: AlertRule[];
}

// POST /alert-rules
interface CreateAlertRuleRequest {
  name: string;
  description: string;
  severity: AlertSeverity;
  condition: AlertCondition;
  actions: AlertAction[];
  cooldown: number;
}

interface CreateAlertRuleResponse {
  rule: AlertRule;
}

// PUT /alert-rules/:ruleId
interface UpdateAlertRuleResponse {
  rule: AlertRule;
}

// DELETE /alert-rules/:ruleId
interface DeleteAlertRuleResponse {
  ruleId: string;
  deleted: boolean;
}
```

### 3.13 Reports Endpoints

```typescript
// GET /reports
interface GetReportsRequest {
  page?: number;
  pageSize?: number;
  type?: ReportType;
}

interface GetReportsResponse {
  reports: Report[];
  meta: ResponseMeta;
}

// POST /reports
interface CreateReportRequest {
  name: string;
  type: ReportType;
  format: 'pdf' | 'csv' | 'json';
  config: ReportConfig;
  schedule?: {
    frequency: 'daily' | 'weekly' | 'monthly';
    time?: string;
  };
}

interface CreateReportResponse {
  report: Report;
}

// POST /reports/:reportId/generate
interface GenerateReportResponse {
  reportId: string;
  status: 'generating';
  estimatedCompletionAt?: Date;
}

// GET /reports/:reportId/download
interface DownloadReportResponse {
  url: string;
  expiresAt: Date;
}
```

### 3.14 Settings Endpoints

```typescript
// GET /settings
interface GetSettingsResponse {
  dashboard: {
    theme: 'light' | 'dark' | 'auto';
    refreshInterval: number;
    defaultTimeRange: number;
  };
  notifications: {
    email: boolean;
    push: boolean;
    slack: boolean;
  };
}

// PUT /settings
interface UpdateSettingsRequest {
  dashboard?: {
    theme?: 'light' | 'dark' | 'auto';
    refreshInterval?: number;
    defaultTimeRange?: number;
  };
  notifications?: {
    email?: boolean;
    push?: boolean;
    slack?: boolean;
  };
}

// GET /users
interface GetUsersResponse {
  users: User[];
}

// POST /users
interface CreateUserRequest {
  email: string;
  name: string;
  role: UserRole;
  permissions: Permission[];
}

interface CreateUserResponse {
  user: User;
}

// DELETE /users/:userId
interface DeleteUserResponse {
  userId: string;
  deleted: boolean;
}

// GET /api-keys
interface GetAPIKeysResponse {
  keys: APIKey[];
}

interface APIKey {
  id: string;
  name: string;
  key: string;
  createdAt: Date;
  expiresAt?: Date;
  lastUsed?: Date;
  scopes: string[];
}

// POST /api-keys
interface CreateAPIKeyRequest {
  name: string;
  scopes: string[];
  expiresAt?: Date;
}

interface CreateAPIKeyResponse {
  key: APIKey;
  plainTextKey: string;
}

// DELETE /api-keys/:keyId
interface DeleteAPIKeyResponse {
  keyId: string;
  deleted: boolean;
}
```

---

## 4. Mock Data Examples

### 4.1 Mock Services Data

```json
{
  "services": [
    {
      "id": "svc-ai-inference-001",
      "name": "ai-inference-service",
      "displayName": "AI Inference Service",
      "category": "ai_core",
      "status": "healthy",
      "version": "2.1.0",
      "description": "Real-time inference engine for ML models",
      "documentationUrl": "https://docs.gogidix.com/ai/inference",
      "endpoints": [
        { "path": "/predict", "method": "POST", "description": "Single prediction", "authenticated": true },
        { "path": "/batch-inference", "method": "POST", "description": "Batch predictions", "authenticated": true },
        { "path": "/health", "method": "GET", "description": "Health check", "authenticated": false }
      ],
      "healthCheckUrl": "http://ai-inference-service:9001/health",
      "metrics": {
        "requestsPerSecond": 1234,
        "averageLatency": 45,
        "errorRate": 0.0002,
        "p50Latency": 38,
        "p95Latency": 67,
        "p99Latency": 123,
        "uptimePercentage": 99.97,
        "lastRestartAt": "2025-02-06T10:30:00Z"
      },
      "configuration": {
        "cpu": { "limit": 2000, "request": 1000, "utilization": 72 },
        "memory": { "limit": 8192, "request": 4096, "utilization": 55 },
        "gpu": { "count": 2, "utilization": 89, "memory": 16384 },
        "replicas": { "current": 4, "min": 2, "max": 8 },
        "autoScaling": { "enabled": true, "targetCPU": 70, "targetMemory": 80 }
      },
      "dependencies": ["ai-gateway-service", "ai-model-service", "redis", "mongodb"],
      "createdAt": "2025-01-15T08:00:00Z",
      "updatedAt": "2025-02-08T10:30:00Z"
    },
    {
      "id": "svc-ai-analytics-002",
      "name": "ai-analytics-service",
      "displayName": "AI Analytics Service",
      "category": "ai_core",
      "status": "healthy",
      "version": "1.2.0",
      "description": "Data analytics and reporting service",
      "metrics": {
        "requestsPerSecond": 892,
        "averageLatency": 67,
        "errorRate": 0.0001,
        "p50Latency": 58,
        "p95Latency": 89,
        "p99Latency": 156,
        "uptimePercentage": 99.95
      },
      "configuration": {
        "cpu": { "limit": 1000, "request": 500, "utilization": 45 },
        "memory": { "limit": 4096, "request": 2048, "utilization": 52 },
        "replicas": { "current": 3, "min": 2, "max": 6 },
        "autoScaling": { "enabled": true }
      }
    },
    {
      "id": "svc-ai-training-003",
      "name": "ai-model-training-service",
      "displayName": "AI Model Training Service",
      "category": "ai_management",
      "status": "degraded",
      "version": "3.0.1",
      "description": "Model training and hyperparameter optimization",
      "metrics": {
        "requestsPerSecond": 12,
        "averageLatency": 0,
        "errorRate": 0,
        "uptimePercentage": 98.5
      },
      "configuration": {
        "cpu": { "limit": 8000, "request": 4000, "utilization": 95 },
        "memory": { "limit": 32768, "request": 16384, "utilization": 85 },
        "gpu": { "count": 4, "utilization": 98, "memory": 32768 },
        "replicas": { "current": 2, "min": 1, "max": 4 },
        "autoScaling": { "enabled": false }
      }
    }
  ]
}
```

### 4.2 Mock Alerts Data

```json
{
  "alerts": [
    {
      "id": "alt-001",
      "severity": "critical",
      "status": "active",
      "title": "High GPU utilization on ai-inference-service",
      "description": "GPU usage has exceeded 90% threshold for 5 minutes.",
      "serviceId": "svc-ai-inference-001",
      "serviceName": "ai-inference-service",
      "metric": "gpu_utilization",
      "threshold": 90,
      "currentValue": 98,
      "triggeredAt": "2025-02-08T14:25:00Z",
      "metadata": {
        "instance": "ai-inference-3",
        "region": "us-east-1",
        "gpuId": "gpu-0"
      }
    },
    {
      "id": "alt-002",
      "severity": "warning",
      "status": "active",
      "title": "Model drift detected in fraud-detection model",
      "description": "Model precision has dropped by 5% in the last 24 hours.",
      "serviceId": "svc-ai-inference-001",
      "serviceName": "ai-inference-service",
      "metric": "model_precision",
      "threshold": 0.90,
      "currentValue": 0.85,
      "triggeredAt": "2025-02-08T13:00:00Z",
      "metadata": {
        "modelId": "model-fraud-detection-v3",
        "baseline": 0.90,
        "current": 0.85
      }
    },
    {
      "id": "alt-003",
      "severity": "critical",
      "status": "active",
      "title": "Saga timeout in transaction-orchestration",
      "description": "Order processing saga has exceeded timeout threshold.",
      "serviceId": "svc-tx-orchestration-001",
      "serviceName": "workflow-orchestration-service",
      "metric": "saga_duration",
      "threshold": 300,
      "currentValue": 345,
      "triggeredAt": "2025-02-08T14:20:00Z",
      "metadata": {
        "sagaId": "saga-order-456789",
        "timeout": 300
      }
    }
  ]
}
```

### 4.3 Mock Workflow Data

```json
{
  "workflows": [
    {
      "id": "wf-onboarding-user-12345",
      "name": "New User Onboarding",
      "type": "user_onboarding",
      "status": "running",
      "definition": {
        "id": "def-onboarding-v1",
        "version": "1.0.0",
        "description": "Standard user onboarding workflow",
        "stepsCount": 7,
        "estimatedDuration": 300
      },
      "execution": {
        "startedAt": "2025-02-08T14:25:00Z",
        "currentStep": 3,
        "completedSteps": 2,
        "progress": 43,
        "retryCount": 0
      },
      "steps": [
        {
          "id": "step-create-account",
          "name": "Create Account",
          "type": "task",
          "status": "completed",
          "startedAt": "2025-02-08T14:25:00Z",
          "completedAt": "2025-02-08T14:25:05Z",
          "duration": 5
        },
        {
          "id": "step-send-email",
          "name": "Send Verification Email",
          "type": "task",
          "status": "completed",
          "startedAt": "2025-02-08T14:25:05Z",
          "completedAt": "2025-02-08T14:25:08Z",
          "duration": 3
        },
        {
          "id": "step-verify-email",
          "name": "Verify Email",
          "type": "wait",
          "status": "running",
          "startedAt": "2025-02-08T14:25:08Z"
        },
        {
          "id": "step-setup-profile",
          "name": "Setup User Profile",
          "type": "task",
          "status": "pending"
        },
        {
          "id": "step-send-welcome",
          "name": "Send Welcome Email",
          "type": "task",
          "status": "pending"
        },
        {
          "id": "step-notify-admin",
          "name": "Notify Admin",
          "type": "task",
          "status": "pending"
        },
        {
          "id": "step-complete",
          "name": "Complete Onboarding",
          "type": "task",
          "status": "pending"
        }
      ],
      "input": {
        "userId": "user-12345",
        "email": "newuser@example.com",
        "name": "New User",
        "role": "customer"
      }
    }
  ]
}
```

### 4.4 Mock Saga Data

```json
{
  "sagas": [
    {
      "id": "saga-order-456789",
      "type": "order_processing",
      "status": "running",
      "definition": {
        "id": "saga-def-order-v1",
        "name": "Order Processing Saga",
        "version": "1.0.0",
        "steps": [
          {
            "id": "step-create-order",
            "name": "Create Order",
            "service": "order-service",
            "action": "createOrder",
            "timeout": 30,
            "retryPolicy": { "maxAttempts": 3, "backoffMs": 1000, "maxBackoffMs": 10000 }
          },
          {
            "id": "step-validate-payment",
            "name": "Validate Payment",
            "service": "payment-service",
            "action": "validatePayment",
            "timeout": 60,
            "retryPolicy": { "maxAttempts": 3, "backoffMs": 1000, "maxBackoffMs": 10000 }
          },
          {
            "id": "step-process-payment",
            "name": "Process Payment",
            "service": "payment-service",
            "action": "processPayment",
            "timeout": 60,
            "retryPolicy": { "maxAttempts": 3, "backoffMs": 1000, "maxBackoffMs": 10000 }
          },
          {
            "id": "step-reserve-inventory",
            "name": "Reserve Inventory",
            "service": "inventory-service",
            "action": "reserveInventory",
            "timeout": 45,
            "retryPolicy": { "maxAttempts": 3, "backoffMs": 1000, "maxBackoffMs": 10000 }
          },
          {
            "id": "step-ship-order",
            "name": "Ship Order",
            "service": "shipping-service",
            "action": "createShipment",
            "timeout": 90,
            "retryPolicy": { "maxAttempts": 3, "backoffMs": 1000, "maxBackoffMs": 10000 }
          }
        ]
      },
      "execution": {
        "startedAt": "2025-02-08T14:20:00Z",
        "retryCount": 0
      },
      "currentStep": 2,
      "steps": [
        {
          "id": "step-create-order",
          "definition": { "name": "Create Order" },
          "status": "completed",
          "startedAt": "2025-02-08T14:20:00Z",
          "completedAt": "2025-02-08T14:20:02Z",
          "duration": 2,
          "attempt": 1,
          "output": { "orderId": "ORD-456789" }
        },
        {
          "id": "step-validate-payment",
          "definition": { "name": "Validate Payment" },
          "status": "completed",
          "startedAt": "2025-02-08T14:20:02Z",
          "completedAt": "2025-02-08T14:20:05Z",
          "duration": 3,
          "attempt": 1,
          "output": { "valid": true, "paymentMethod": "card" }
        },
        {
          "id": "step-process-payment",
          "definition": { "name": "Process Payment" },
          "status": "running",
          "startedAt": "2025-02-08T14:20:05Z",
          "attempt": 1
        },
        {
          "id": "step-reserve-inventory",
          "definition": { "name": "Reserve Inventory" },
          "status": "pending"
        },
        {
          "id": "step-ship-order",
          "definition": { "name": "Ship Order" },
          "status": "pending"
        }
      ],
      "compensations": [],
      "input": {
        "userId": "user-12345",
        "items": [
          { "productId": "prod-001", "quantity": 2, "price": 99.99 },
          { "productId": "prod-002", "quantity": 1, "price": 149.99 }
        ],
        "shippingAddress": {
          "street": "123 Main St",
          "city": "San Francisco",
          "state": "CA",
          "zip": "94102",
          "country": "US"
        }
      }
    }
  ]
}
```

---

## 5. WebSocket Events

### 5.1 Connection Setup

```typescript
// WebSocket Connection
const ws = new WebSocket('ws://localhost:8908/ws');

// Authentication
ws.send(JSON.stringify({
  type: 'auth',
  token: 'your-jwt-token'
}));

// Subscribe to channels
ws.send(JSON.stringify({
  type: 'subscribe',
  channels: [
    'services:*',
    'alerts:*',
    'workflows:*',
    'metrics:realtime'
  ]
}));
```

### 5.2 Event Types

```typescript
// Service Health Update
interface ServiceHealthEvent {
  type: 'service_health';
  serviceId: string;
  serviceName: string;
  status: ServiceStatus;
  timestamp: Date;
}

// Metric Update
interface MetricUpdateEvent {
  type: 'metric_update';
  metric: string;
  serviceId: string;
  value: number;
  timestamp: Date;
}

// Alert Triggered
interface AlertTriggeredEvent {
  type: 'alert_triggered';
  alert: Alert;
  timestamp: Date;
}

// Alert Resolved
interface AlertResolvedEvent {
  type: 'alert_resolved';
  alertId: string;
  resolvedBy: string;
  timestamp: Date;
}

// Workflow Status Change
interface WorkflowStatusEvent {
  type: 'workflow_status';
  workflowId: string;
  status: WorkflowStatus;
  previousStatus: WorkflowStatus;
  timestamp: Date;
}

// Workflow Step Update
interface WorkflowStepEvent {
  type: 'workflow_step';
  workflowId: string;
  stepId: string;
  stepName: string;
  status: StepStatus;
  timestamp: Date;
}

// Training Job Update
interface TrainingJobEvent {
  type: 'training_job';
  jobId: string;
  modelId: string;
  status: string;
  progress: number;
  metrics: TrainingMetrics;
  timestamp: Date;
}

// Saga Status Change
interface SagaStatusEvent {
  type: 'saga_status';
  sagaId: string;
  status: SagaStatus;
  timestamp: Date;
}

// State Transition
interface StateTransitionEvent {
  type: 'state_transition';
  stateMachineId: string;
  previousState: string;
  newState: string;
  event: string;
  timestamp: Date;
}
```

### 5.3 Event Examples

```json
{
  "type": "service_health",
  "serviceId": "svc-ai-inference-001",
  "serviceName": "ai-inference-service",
  "status": "degraded",
  "timestamp": "2025-02-08T14:30:00Z"
}

{
  "type": "metric_update",
  "metric": "requests_per_second",
  "serviceId": "svc-ai-inference-001",
  "value": 1567,
  "timestamp": "2025-02-08T14:30:05Z"
}

{
  "type": "alert_triggered",
  "alert": {
    "id": "alt-004",
    "severity": "warning",
    "title": "High latency on ai-inference-service",
    "serviceId": "svc-ai-inference-001"
  },
  "timestamp": "2025-02-08T14:30:10Z"
}

{
  "type": "workflow_step",
  "workflowId": "wf-onboarding-user-12345",
  "stepId": "step-verify-email",
  "stepName": "Verify Email",
  "status": "completed",
  "timestamp": "2025-02-08T14:30:15Z"
}
```

---

## 6. State Management

### 6.1 Zustand Store Structure

```typescript
interface DashboardStore {
  // Auth State
  auth: {
    user: User | null;
    token: string | null;
    isAuthenticated: boolean;
    permissions: Permission[];
    login: (email: string, password: string) => Promise<void>;
    logout: () => Promise<void>;
    refresh: () => Promise<void>;
  };

  // UI State
  ui: {
    sidebarCollapsed: boolean;
    theme: 'light' | 'dark' | 'auto';
    notifications: Notification[];
    addNotification: (notification: Notification) => void;
    removeNotification: (id: string) => void;
    toggleSidebar: () => void;
    setTheme: (theme: 'light' | 'dark' | 'auto') => void;
  };

  // Services State
  services: {
    items: Map<string, Service>;
    filters: ServiceFilters;
    selectedService: Service | null;
    setServices: (services: Service[]) => void;
    updateService: (serviceId: string, updates: Partial<Service>) => void;
    setFilters: (filters: ServiceFilters) => void;
    selectService: (serviceId: string | null) => void;
  };

  // Alerts State
  alerts: {
    items: Alert[];
    unreadCount: number;
    addAlert: (alert: Alert) => void;
    acknowledgeAlert: (alertId: string) => Promise<void>;
    resolveAlert: (alertId: string) => Promise<void>;
  };

  // Realtime State
  realtime: {
    connected: boolean;
    lastUpdate: Date | null;
    metrics: Map<string, number>;
    setConnected: (connected: boolean) => void;
    updateMetric: (key: string, value: number) => void;
  };
}
```

---

## 7. Error Handling

### 7.1 Error Response Format

```typescript
interface APIError {
  error: {
    code: string;
    message: string;
    details?: Record<string, unknown>;
    stackTrace?: string;
    requestId: string;
    timestamp: Date;
  };
}

// Common Error Codes
enum ErrorCode {
  // Authentication (4xx)
  UNAUTHORIZED = 'UNAUTHORIZED',
  TOKEN_EXPIRED = 'TOKEN_EXPIRED',
  INVALID_CREDENTIALS = 'INVALID_CREDENTIALS',
  FORBIDDEN = 'FORBIDDEN',

  // Client Errors (4xx)
  VALIDATION_ERROR = 'VALIDATION_ERROR',
  NOT_FOUND = 'NOT_FOUND',
  CONFLICT = 'CONFLICT',
  RATE_LIMIT_EXCEEDED = 'RATE_LIMIT_EXCEEDED',

  // Server Errors (5xx)
  INTERNAL_ERROR = 'INTERNAL_ERROR',
  SERVICE_UNAVAILABLE = 'SERVICE_UNAVAILABLE',
  GATEWAY_TIMEOUT = 'GATEWAY_TIMEOUT'
}
```

### 7.2 Error Handling Flow

```typescript
// TanStack Query Error Handling
const queryClient = new QueryClient({
  queryCache: new QueryCache({
    onError: (error) => {
      if (error.statusCode === 401) {
        // Redirect to login
        window.location.href = '/login';
      }
    }
  }),
  mutationCache: new MutationCache({
    onError: (error) => {
      // Show toast notification
      toast.error(error.message);
    }
  })
});
```

---

## Document Info

**Document Version:** 1.0
**Last Updated:** 2025-02-08
**Author:** Gogidix Architecture Team
**Related Documents:**
- 01_UI_Flow_Documentation.md
- 02_Wireframes_Documentation.md
- 04_Page_By_Page_Flow_Documentation.md
