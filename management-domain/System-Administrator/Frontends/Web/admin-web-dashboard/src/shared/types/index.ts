// Admin User Roles
export type AdminRole =
  | 'SYSTEM_ADMINISTRATOR'
  | 'SUPER_ADMIN'
  | 'GLOBAL_SECURITY_OFFICER'
  | 'DEVOPS_ENGINEER'
  | 'DOMAIN_ADMINISTRATOR'
  | 'IT_SUPPORT'
  | 'OPERATOR'
  | 'SECURITY_ANALYST'

export type Department =
  | 'system-administrator'
  | 'executive'
  | 'finance'
  | 'human-resource'
  | 'sales'
  | 'customer-support'
  | 'global-business-management'
  | 'digital-marketing'
  | 'foundation-services'

export interface AdminUser {
  id: string
  email: string
  firstName: string
  lastName: string
  displayName: string
  role: AdminRole
  department: Department
  avatar?: string
  permissions: Permission[]
  country?: string
  isActive: boolean
  lastLogin?: string
  createdAt: string
  updatedAt: string
}

export type Permission =
  | 'view:dashboard'
  | 'view:users'
  | 'create:users'
  | 'edit:users'
  | 'delete:users'
  | 'view:roles'
  | 'create:roles'
  | 'edit:roles'
  | 'delete:roles'
  | 'view:permissions'
  | 'manage:permissions'
  | 'view:access_requests'
  | 'approve:access_requests'
  | 'reject:access_requests'
  | 'view:incidents'
  | 'create:incidents'
  | 'assign:incidents'
  | 'resolve:incidents'
  | 'view:monitoring'
  | 'manage:monitoring'
  | 'view:security'
  | 'manage:security'
  | 'view:threats'
  | 'manage:threats'
  | 'view:vulnerabilities'
  | 'manage:vulnerabilities'
  | 'view:configuration'
  | 'edit:configuration'
  | 'view:deployments'
  | 'create:deployments'
  | 'rollback:deployments'
  | 'view:compliance'
  | 'manage:compliance'
  | 'view:audit_logs'
  | 'export:audit_logs'
  | 'provision:users'
  | 'deprovision:users'
  | 'view:maintenance'
  | 'schedule:maintenance'
  | 'manage:notifications'
  | 'view:reports'
  | 'create:reports'
  | 'manage:settings'

// System Health Types
export interface SystemHealth {
  id: string
  service: string
  status: 'healthy' | 'degraded' | 'down' | 'maintenance'
  uptime: number
  lastCheck: string
  metrics: {
    cpu: number
    memory: number
    disk: number
    network: number
  }
}

// Incident Types
export interface Incident {
  id: string
  title: string
  description: string
  severity: 'low' | 'medium' | 'high' | 'critical'
  status: 'open' | 'in_progress' | 'resolved' | 'closed'
  category: 'security' | 'performance' | 'availability' | 'data' | 'other'
  assignedTo?: string
  createdBy: string
  createdAt: string
  resolvedAt?: string
  resolution?: string
  affectedServices: string[]
  tags: string[]
}

// Access Request Types
export interface AccessRequest {
  id: string
  userId: string
  userName: string
  resource: string
  resourceType: 'system' | 'domain' | 'application' | 'data'
  accessType: 'read' | 'write' | 'admin' | 'owner'
  reason: string
  status: 'pending' | 'approved' | 'rejected' | 'expired'
  requestedAt: string
  expiresAt?: string
  reviewedBy?: string
  reviewedAt?: string
  reviewComment?: string
}

// Role & Permission Types
export interface Role {
  id: string
  name: string
  description: string
  permissions: Permission[]
  isSystemRole: boolean
  userCount: number
  createdAt: string
  updatedAt: string
}

export interface PermissionGroup {
  id: string
  name: string
  description: string
  permissions: Permission[]
}

// Deployment Types
export interface Deployment {
  id: string
  name: string
  version: string
  environment: 'development' | 'staging' | 'production'
  status: 'pending' | 'deploying' | 'success' | 'failed' | 'rolled_back'
  deployedBy: string
  deployedAt?: string
  rollbackVersion?: string
  changes: string[]
  affectedServices: string[]
  duration?: number
}

// Security Types
export interface SecurityThreat {
  id: string
  type: 'malware' | 'phishing' | 'ddos' | 'intrusion' | 'data_breach' | 'other'
  severity: 'low' | 'medium' | 'high' | 'critical'
  status: 'detected' | 'investigating' | 'mitigated' | 'false_positive'
  source: string
  target: string
  description: string
  detectedAt: string
  resolvedAt?: string
  actions: string[]
}

export interface Vulnerability {
  id: string
  cveId?: string
  title: string
  severity: 'low' | 'medium' | 'high' | 'critical'
  status: 'open' | 'in_progress' | 'patched' | 'ignored'
  affectedComponent: string
  description: string
  discoveredAt: string
  dueDate?: string
  patchedAt?: string
}

// Monitoring Types
export interface MetricData {
  timestamp: string
  value: number
}

export interface MonitoringMetric {
  id: string
  name: string
  category: 'cpu' | 'memory' | 'network' | 'storage' | 'custom'
  current: number
  average: number
  peak: number
  unit: string
  threshold: number
  status: 'normal' | 'warning' | 'critical'
  history: MetricData[]
}

// Audit Types
export interface AuditLog {
  id: string
  action: string
  actor: string
  actorId: string
  target?: string
  targetId?: string
  category: 'auth' | 'user' | 'permission' | 'system' | 'security' | 'data'
  details: Record<string, unknown>
  ipAddress: string
  userAgent: string
  timestamp: string
  result: 'success' | 'failure'
}

// Compliance Types
export interface ComplianceReport {
  id: string
  name: string
  framework: 'SOC2' | 'ISO27001' | 'GDPR' | 'HIPAA' | 'PCI_DSS'
  status: 'compliant' | 'non_compliant' | 'pending_review'
  lastAudit: string
  nextAudit: string
  score: number
  findings: ComplianceFinding[]
}

export interface ComplianceFinding {
  id: string
  severity: 'low' | 'medium' | 'high' | 'critical'
  description: string
  status: 'open' | 'in_progress' | 'resolved'
  dueDate?: string
}

// User Provisioning Types
export interface ProvisioningRequest {
  id: string
  type: 'onboarding' | 'offboarding' | 'transfer' | 'modification'
  employeeId: string
  employeeName: string
  department: Department
  role: AdminRole
  status: 'pending' | 'in_progress' | 'completed' | 'failed'
  requestedBy: string
  requestedAt: string
  completedAt?: string
  tasks: ProvisioningTask[]
}

export interface ProvisioningTask {
  id: string
  name: string
  description: string
  status: 'pending' | 'in_progress' | 'completed' | 'failed'
  completedAt?: string
}

// Configuration Types
export interface SystemConfiguration {
  id: string
  category: string
  key: string
  value: string
  description: string
  isSecret: boolean
  environment: 'all' | 'development' | 'staging' | 'production'
  updatedAt: string
  updatedBy: string
}

// Maintenance Types
export interface MaintenanceWindow {
  id: string
  title: string
  description: string
  startAt: string
  endAt: string
  status: 'scheduled' | 'in_progress' | 'completed' | 'cancelled'
  affectedServices: string[]
  impact: 'none' | 'low' | 'medium' | 'high'
  createdBy: string
  notificationsSent: boolean
}

// Stats & Dashboard Types
export interface DashboardStats {
  systemHealth: number
  activeIncidents: number
  pendingRequests: number
  activeUsers: number
  cpuUsage: number
  memoryUsage: number
  diskUsage: number
  networkUsage: number
  threatsDetected: number
  vulnerabilities: number
  uptime: number
}
