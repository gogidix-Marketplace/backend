/**
 * Shared API Types
 * Common types used across all API services
 */

// ============================================================================
// Pagination
// ============================================================================

export interface PaginationParams {
  page?: number
  size?: number
  sortBy?: string
  sortDirection?: 'asc' | 'desc'
}

export interface PaginatedResponse<T> {
  content: T[]
  totalElements: number
  totalPages: number
  size: number
  number: number
  first: boolean
  last: boolean
  empty: boolean
}

// ============================================================================
// Date/Time
// ============================================================================

export type DateRange = {
  startDate: string // ISO 8601 format
  endDate: string // ISO 8601 format
}

export type TimePeriod = 'hourly' | 'daily' | 'weekly' | 'monthly' | 'quarterly' | 'yearly'

// ============================================================================
// Common Response Types
// ============================================================================

export interface ApiResponse<T> {
  data: T
  message?: string
  timestamp: string
}

export interface ApiError {
  timestamp: string
  status: number
  error: string
  message: string
  path: string
  validationErrors?: Record<string, string>
}

// ============================================================================
// KPI Types
// ============================================================================

export type KPICategory =
  | 'REVENUE'
  | 'GROWTH'
  | 'CUSTOMER'
  | 'OPERATIONS'
  | 'FINANCIAL'
  | 'TECHNOLOGY'
  | 'COMPLIANCE'
  | 'INNOVATION'

export type KPIStatus = 'ON_TRACK' | 'AT_RISK' | 'BEHIND' | 'AHEAD'

export type KPITrend = 'UP' | 'DOWN' | 'STABLE' | 'VOLATILE'

export interface KPI {
  id: string
  code: string
  name: string
  description?: string
  category: KPICategory
  value: number
  target: number
  unit: string
  status: KPIStatus
  trend: KPITrend
  changePercentage: number
  previousValue: number
  healthScore: number // 0-100
  lastUpdated: string
  sourceDomain?: string
}

export interface KPIGroup {
  category: KPICategory
  kpis: KPI[]
  overallHealthScore: number
}

// ============================================================================
// Domain Types
// ============================================================================

export type DomainType =
  | 'EXECUTIVE'
  | 'ECOMMERCE'
  | 'COURIER'
  | 'WAREHOUSING'
  | 'AIR_FREIGHT'
  | 'OCEAN_SHIPPING'
  | 'HAULAGE'
  | 'PROCUREMENT'
  | 'ADMIN'
  | 'FINANCE'
  | 'AI'

export interface Domain {
  id: string
  code: string
  name: string
  type: DomainType
  status: 'ACTIVE' | 'INACTIVE' | 'DEGRADED'
  healthScore: number
}

// ============================================================================
// Country/Region Types
// ============================================================================

export interface Country {
  id: string
  code: string // ISO 3166-1 alpha-2
  name: string
  region: string
  status: 'ACTIVE' | 'INACTIVE'
}

export interface Region {
  id: string
  code: string
  name: string
  countries: Country[]
}

// ============================================================================
// Executive Types
// ============================================================================

export type ExecutiveRole = 'CEO' | 'CFO' | 'COO' | 'CTO' | 'ACTING_CEO'

export interface Executive {
  id: string
  userId: string
  role: ExecutiveRole
  department: string
  permissions: string[]
}

// ============================================================================
// Dashboard Types
// ============================================================================

export interface DashboardWidget {
  id: string
  type: string
  title: string
  position: { x: number; y: number; w: number; h: number }
  config: Record<string, any>
  dataSource: string
}

export interface DashboardConfig {
  id: string
  name: string
  role: ExecutiveRole
  widgets: DashboardWidget[]
  layout: string
}

// ============================================================================
// Analytics Types
// ============================================================================

export interface AnalyticsDataPoint {
  timestamp: string
  value: number
  metadata?: Record<string, any>
}

export interface AnalyticsSeries {
  name: string
  data: AnalyticsDataPoint[]
  color?: string
}

export interface AnalyticsChart {
  type: 'line' | 'bar' | 'pie' | 'donut' | 'area' | 'heatmap'
  title: string
  series: AnalyticsSeries[]
  config?: Record<string, any>
}

// ============================================================================
// Approval Types
// ============================================================================

export type ApprovalStatus = 'PENDING' | 'APPROVED' | 'REJECTED' | 'DELEGATED'

export type ApprovalType = 'BUDGET' | 'HIRING' | 'INITIATIVE' | 'EXPENSE' | 'STRATEGY'

export interface Approval {
  id: string
  type: ApprovalType
  title: string
  description: string
  requester: {
    id: string
    name: string
    department: string
  }
  requestedAt: string
  urgency: 'LOW' | 'MEDIUM' | 'HIGH' | 'CRITICAL'
  status: ApprovalStatus
  amount?: number
  currency?: string
  documents?: string[]
  comments?: Comment[]
  workflowStep?: string
}

export interface Comment {
  id: string
  userId: string
  userName: string
  content: string
  createdAt: string
}

// ============================================================================
// Alert Types
// ============================================================================

export type AlertSeverity = 'INFO' | 'WARNING' | 'ERROR' | 'CRITICAL'

export type AlertCategory = 'SYSTEM' | 'BUSINESS' | 'COMPLIANCE' | 'SECURITY' | 'OPERATIONAL'

export interface Alert {
  id: string
  title: string
  description: string
  severity: AlertSeverity
  category: AlertCategory
  source: string
  createdAt: string
  acknowledged: boolean
  acknowledgedBy?: string
  acknowledgedAt?: string
  actionUrl?: string
  metadata?: Record<string, any>
}

// ============================================================================
// Report Types
// ============================================================================

export type ReportFormat = 'PDF' | 'EXCEL' | 'CSV' | 'JSON'

export type ReportFrequency = 'DAILY' | 'WEEKLY' | 'MONTHLY' | 'QUARTERLY' | 'YEARLY' | 'ON_DEMAND'

export interface Report {
  id: string
  name: string
  description: string
  type: string
  format: ReportFormat
  frequency: ReportFrequency
  lastGenerated: string
  nextScheduled?: string
  status: 'READY' | 'GENERATING' | 'FAILED'
  createdBy: string
}

// ============================================================================
// AI Types
// ============================================================================

export type AIModelType = 'PREDICTION' | 'CLASSIFICATION' | 'CLUSTERING' | 'NLP' | 'ANOMALY_DETECTION'

export type AIConfidence = 'LOW' | 'MEDIUM' | 'HIGH'

export interface AIInsight {
  id: string
  type: string
  title: string
  description: string
  confidence: AIConfidence
  confidenceScore: number // 0-100
  category: string
  generatedAt: string
  modelName: string
  recommendations?: string[]
  impact: 'LOW' | 'MEDIUM' | 'HIGH'
  actionable: boolean
}

export interface AIPrediction {
  id: string
  model: string
  modelType: AIModelType
  targetVariable: string
  predictedValue: number
  confidenceInterval: {
    lower: number
    upper: number
  }
  confidence: number // 0-100
  features: Record<string, number>
  generatedAt: string
}

export interface AIAnomaly {
  id: string
  type: string
  description: string
  severity: 'LOW' | 'MEDIUM' | 'HIGH'
  detectedAt: string
  value: number
  expectedRange: {
    min: number
    max: number
  }
  deviation: number // percentage deviation
  confidence: number // 0-100
}

// ============================================================================
// Strategy Types
// ============================================================================

export type InitiativeStatus = 'PLANNED' | 'IN_PROGRESS' | 'COMPLETE' | 'DELAYED' | 'CANCELLED'

export type ObjectivePeriod = 'Q1' | 'Q2' | 'Q3' | 'Q4' | 'H1' | 'H2' | 'ANNUAL'

export interface Objective {
  id: string
  title: string
  description: string
  period: ObjectivePeriod
  year: number
  owner: string
  department: string
  status: InitiativeStatus
  progress: number // 0-100
  keyResults: KeyResult[]
  initiatives: Initiative[]
  startDate: string
  endDate: string
}

export interface KeyResult {
  id: string
  title: string
  description: string
  targetValue: number
  currentValue: number
  unit: string
  progress: number // 0-100
  status: 'ON_TRACK' | 'AT_RISK' | 'BEHIND'
  dueDate: string
}

export interface Initiative {
  id: string
  name: string
  description: string
  status: InitiativeStatus
  priority: 'LOW' | 'MEDIUM' | 'HIGH' | 'CRITICAL'
  owner: string
  budget?: number
  progress: number // 0-100
  startDate: string
  endDate: string
  milestones: Milestone[]
}

export interface Milestone {
  id: string
  title: string
  description: string
  dueDate: string
  status: 'PENDING' | 'COMPLETE' | 'OVERDUE'
}

// ============================================================================
// Financial Types
// ============================================================================

export type Currency = 'USD' | 'EUR' | 'GBP' | 'NGN' | 'KES' | 'ZAR' | 'GHS'

export interface Money {
  amount: number
  currency: Currency
}

export interface Budget {
  id: string
  category: string
  department: string
  period: string
  budgetedAmount: Money
  actualAmount: Money
  variance: Money
  variancePercentage: number
  status: 'UNDER' | 'ON_TRACK' | 'OVER'
}

// ============================================================================
// Operations Types
// ============================================================================

export type ServiceStatus = 'OPERATIONAL' | 'DEGRADED' | 'DOWN'

export interface ServiceHealth {
  name: string
  status: ServiceStatus
  uptime: number // percentage
  lastIncident?: string
  responseTime?: number // milliseconds
}

export interface Incident {
  id: string
  title: string
  description: string
  severity: 'P1' | 'P2' | 'P3' | 'P4'
  status: 'OPEN' | 'IN_PROGRESS' | 'RESOLVED' | 'CLOSED'
  createdAt: string
  resolvedAt?: string
  assignee?: string
  service: string
}

// ============================================================================
// Technology Types
// ============================================================================

export interface TechMetric {
  name: string
  value: number
  unit: string
  status: 'HEALTHY' | 'WARNING' | 'CRITICAL'
  threshold: {
    warning: number
    critical: number
  }
}

export interface TechnicalDebt {
  id: string
  title: string
  description: string
  severity: 'LOW' | 'MEDIUM' | 'HIGH' | 'CRITICAL'
  estimatedRemediationTime: string
  category: string
  service: string
}

// ============================================================================
// Filter Types
// ============================================================================

export interface FilterOption {
  label: string
  value: string
}

export interface Filter {
  key: string
  label: string
  options: FilterOption[]
  selected: string[]
  type: 'select' | 'multiselect' | 'date' | 'daterange'
}

// ============================================================================
// User Types
// ============================================================================

export interface User {
  id: string
  email: string
  firstName: string
  lastName: string
  displayName: string
  role: ExecutiveRole
  department: string
  avatar?: string
  permissions: string[]
  isActive: boolean
  createdAt: string
  updatedAt: string
}

// ============================================================================
// Export Types
// ============================================================================

export type ExportFormat = 'CSV' | 'EXCEL' | 'PDF' | 'JSON'

export interface ExportRequest {
  format: ExportFormat
  startDate: string
  endDate: string
  filters?: Record<string, any>
}

export interface ExportResponse {
  exportId: string
  status: 'PENDING' | 'COMPLETE' | 'FAILED'
  downloadUrl?: string
  expiresAt?: string
}
