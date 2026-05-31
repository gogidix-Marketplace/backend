// Monitoring Role Types
export type MonitoringRole =
  | 'DEVOPS_LEAD'
  | 'SRE_ENGINEER'
  | 'MONITORING_ANALYST'
  | 'SERVICE_OWNER'
  | 'ADMIN'

// Service Health Status
export type ServiceHealthStatus = 'healthy' | 'degraded' | 'critical' | 'unknown'

// Alert Severity
export type AlertSeverity = 'info' | 'warning' | 'error' | 'critical'

// Alert Status
export type AlertStatus = 'active' | 'acknowledged' | 'resolved' | 'dismissed'

// Service Types
export type ServiceType =
  | 'api_gateway'
  | 'auth_service'
  | 'notification_service'
  | 'file_storage'
  | 'cache_service'
  | 'message_queue'
  | 'ai_orchestration'
  | 'email_service'
  | 'database'
  | 'cdn'

// User Interface
export interface User {
  id: string
  email: string
  firstName: string
  lastName: string
  displayName: string
  role: MonitoringRole
  department: string
  avatar?: string
  permissions: string[]
  services?: ServiceType[] // For SERVICE_OWNER role
  isActive: boolean
  createdAt: string
  updatedAt: string
}

// Service Interface
export interface Service {
  id: string
  name: string
  type: ServiceType
  description: string
  status: ServiceHealthStatus
  version: string
  uptime: number
  responseTime: number
  errorRate: number
  throughput: number
  lastChecked: string
  region: string
  dependencies: string[]
  dependents: string[]
  endpoint?: string
  healthCheckUrl?: string
}

// Service Metrics
export interface ServiceMetrics {
  serviceId: string
  timestamp: string
  responseTime: number
  throughput: number
  errorRate: number
  cpuUsage: number
  memoryUsage: number
  diskUsage: number
  networkIn: number
  networkOut: number
  activeConnections: number
}

// Alert Interface
export interface Alert {
  id: string
  title: string
  description: string
  severity: AlertSeverity
  status: AlertStatus
  serviceId: string
  serviceName: string
  metric: string
  threshold: number
  currentValue: number
  triggeredAt: string
  acknowledgedAt?: string
  acknowledgedBy?: string
  resolvedAt?: string
  resolvedBy?: string
  assignedTo?: string
  incidentId?: string
  actions: AlertAction[]
}

export interface AlertAction {
  id: string
  type: 'acknowledged' | 'comment' | 'assigned' | 'escalated' | 'resolved'
  userId: string
  userName: string
  timestamp: string
  note?: string
}

// Alert Rule Interface
export interface AlertRule {
  id: string
  name: string
  description: string
  serviceId: string
  metric: string
  condition: 'greater_than' | 'less_than' | 'equals' | 'not_equals'
  threshold: number
  severity: AlertSeverity
  enabled: boolean
  notificationChannels: string[]
  cooldownMinutes: number
  createdAt: string
  updatedAt: string
  createdBy: string
}

// Incident Interface
export interface Incident {
  id: string
  title: string
  description: string
  severity: AlertSeverity
  status: 'open' | 'investigating' | 'monitoring' | 'resolved'
  serviceId: string
  assignedTo?: string
  createdBy: string
  createdAt: string
  resolvedAt?: string
  resolution?: string
  alerts: string[]
  updates: IncidentUpdate[]
}

export interface IncidentUpdate {
  id: string
  incidentId: string
  message: string
  userId: string
  userName: string
  timestamp: string
}

// Dependency Graph
export interface ServiceDependency {
  from: string
  to: string
  type: 'synchronous' | 'asynchronous'
  strength: number // 0-1, how critical the dependency is
}

// Performance Chart Data
export interface MetricDataPoint {
  timestamp: string
  value: number
}

export interface PerformanceChart {
  serviceId: string
  metric: string
  timeRange: string
  data: MetricDataPoint[]
  average: number
  min: number
  max: number
  p95: number
  p99: number
}

// Log Entry
export interface LogEntry {
  id: string
  serviceId: string
  timestamp: string
  level: 'debug' | 'info' | 'warn' | 'error'
  message: string
  metadata?: Record<string, unknown>
}

// Notification Channel
export interface NotificationChannel {
  id: string
  name: string
  type: 'email' | 'slack' | 'pagerduty' | 'webhook'
  enabled: boolean
  config: Record<string, unknown>
  createdAt: string
}

// Dashboard Stats
export interface DashboardStats {
  totalServices: number
  healthyServices: number
  degradedServices: number
  criticalServices: number
  unknownServices: number
  activeAlerts: number
  criticalAlerts: number
  averageResponseTime: number
  averageUptime: number
}

// Time Range Options
export type TimeRange = '1h' | '6h' | '24h' | '7d' | '30d' | '90d'

// Filter Options
export interface ServiceFilters {
  status?: ServiceHealthStatus[]
  type?: ServiceType[]
  region?: string[]
  search?: string
}

export interface AlertFilters {
  severity?: AlertSeverity[]
  status?: AlertStatus[]
  service?: string[]
  timeRange?: TimeRange
}
