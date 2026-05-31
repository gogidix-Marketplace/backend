// User & Authentication Types
export type UserRole =
  | 'CEO'
  | 'ACTING_CEO'
  | 'CFO'
  | 'COO'
  | 'CTO'
  | 'PRESIDENT'
  | 'VP'
  | 'DIRECTOR'
  | 'MANAGER'
  | 'SUPERVISOR'
  | 'EMPLOYEE'
  | 'CONTRACTOR'
  | 'ADMIN'
  | 'SUPER_ADMIN'

export type Department =
  | 'executive'
  | 'finance'
  | 'human-resource'
  | 'sales'
  | 'customer-support'
  | 'system-administrator'
  | 'global-business-management'
  | 'digital-marketing'
  | 'foundation-services'
  | 'operations'
  | 'technology'

export interface User {
  id: string
  email: string
  firstName: string
  lastName: string
  displayName: string
  role: UserRole
  department: Department
  avatar?: string
  permissions: string[]
  country?: string
  isActive: boolean
  createdAt: string
  updatedAt: string
}

// KPI & Metrics Types
export interface KPI {
  id: string
  name: string
  description: string
  value: number
  target: number
  unit: string
  trend: 'up' | 'down' | 'neutral'
  trendValue: number
  status: 'on_track' | 'at_risk' | 'behind' | 'ahead'
  category: string
  owner: string
  frequency: 'daily' | 'weekly' | 'monthly' | 'quarterly'
  lastUpdated: string
}

export interface MetricCardProps {
  title: string
  value: string | number
  change?: number
  changeType?: 'increase' | 'decrease' | 'neutral'
  unit?: string
  trend?: 'up' | 'down' | 'neutral'
  loading?: boolean
}

// Approval Types
export interface Approval {
  id: string
  type: 'budget' | 'hiring' | 'initiative' | 'contract' | 'other'
  title: string
  description: string
  requestedBy: string
  requestDate: string
  amount?: number
  department: Department
  status: 'pending' | 'approved' | 'rejected' | 'delegated'
  priority: 'low' | 'medium' | 'high' | 'urgent'
  dueDate?: string
}

// Report Types
export interface Report {
  id: string
  name: string
  type: 'executive' | 'board' | 'financial' | 'operational' | 'custom'
  description: string
  generatedBy: string
  generatedAt: string
  format: 'pdf' | 'excel' | 'powerpoint'
  size: number
  downloadUrl: string
}

// OKR Types
export interface OKR {
  id: string
  objective: string
  keyResults: KeyResult[]
  owner: string
  period: string
  progress: number
  status: 'on_track' | 'at_risk' | 'behind'
  department?: string
}

export interface KeyResult {
  id: string
  title: string
  description: string
  targetValue: number
  currentValue: number
  unit: string
  dueDate: string
}

// Navigation Types
export interface NavItem {
  title: string
  href: string
  icon?: string
  badge?: number
  children?: NavItem[]
}

// Chart Data Types
export interface ChartDataPoint {
  name: string
  value: number
  [key: string]: string | number
}

export interface SeriesData {
  name: string
  data: Array<{ name: string; value: number }>
  color?: string
}

export type BusinessUnitSlug = 'courier' | 'ecommerce' | 'warehousing' | 'air-freight' | 'ocean-shipping' | 'haulage' | 'procurement' | 'admin-core'

export type DepartmentSlug = 'digital-marketing' | 'customer-support' | 'global-business-management' | 'human-resource' | 'sales' | 'system-administrator' | 'finance' | 'foundation-services'

export interface BusinessUnit {
  id: BusinessUnitSlug
  name: string
  icon: string
  color: string
  serviceCount: number
  healthyServices: number
  revenue: number
  revenueGrowth: number
  revenueModel: string
  primaryRegion: string
}

export interface BusinessUnitRevenue {
  unitId: BusinessUnitSlug
  revenue: number
  growth: number
  margin: number
  opex: number
  netProfit: number
  currency: string
  period: string
}

export interface DepartmentHealth {
  departmentId: DepartmentSlug
  name: string
  icon: string
  color: string
  healthScore: number
  serviceCount: number
  healthyServices: number
  alertCount: number
  criticalCount: number
  pendingApprovals: number
  keyMetric: string
  keyMetricValue: string
  budgetAllocated: number
  budgetSpent: number
  budgetPercentUsed: number
}

export interface ServiceHealthStatus {
  name: string
  port: number
  status: 'healthy' | 'degraded' | 'offline'
  uptime: number
  responseTime: number
  cpu: number
  memory: number
  region: string
  businessUnit?: BusinessUnitSlug
  department?: DepartmentSlug
}

export interface ApprovalChainStep {
  role: UserRole
  status: 'pending' | 'approved' | 'rejected' | 'awaiting'
  userName?: string
  timestamp?: string
  comment?: string
}

export interface DashboardFilters {
  period: 'today' | 'week' | 'month' | 'quarter' | 'year'
  currency: 'USD' | 'EUR' | 'GBP' | 'NGN'
  businessUnit?: BusinessUnitSlug | 'all'
  department?: DepartmentSlug | 'all'
  region?: string | 'all'
}
