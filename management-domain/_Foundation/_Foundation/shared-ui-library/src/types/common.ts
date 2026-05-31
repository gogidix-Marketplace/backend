/**
 * Common type definitions for the Gogidix Management Domain
 */

// ============================================
// User & Authentication Types
// ============================================

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

export interface User {
  id: string
  email: string
  firstName: string
  lastName: string
  displayName: string
  role: UserRole
  department: Department
  avatar?: string
  permissions: Permission[]
  country?: string
  isActive: boolean
  createdAt: string
  updatedAt: string
}

export type Permission =
  | 'view:all'
  | 'view:department'
  | 'view:country'
  | 'approve:budget'
  | 'approve:hiring'
  | 'approve:initiative'
  | 'create:kpi'
  | 'edit:kpi'
  | 'delete:kpi'
  | 'view:reports'
  | 'create:reports'
  | 'manage:users'
  | 'manage:settings'
  | 'view:audit_logs'

// ============================================
// API Response Types
// ============================================

export interface ApiResponse<T> {
  data: T
  success: boolean
  message?: string
  errors?: Record<string, string[]>
}

export interface ApiError {
  message: string
  code: string
  statusCode: number
  details?: Record<string, unknown>
}

export interface PaginationParams {
  page: number
  limit: number
  sortBy?: string
  sortOrder?: 'asc' | 'desc'
}

export interface PaginatedResponse<T> {
  data: T[]
  pagination: {
    page: number
    limit: number
    total: number
    totalPages: number
    hasNext: boolean
    hasPrev: boolean
  }
}

// ============================================
// Common Domain Types
// ============================================

export type Status = 'pending' | 'in_progress' | 'completed' | 'cancelled' | 'failed'
export type Priority = 'low' | 'medium' | 'high' | 'urgent'

export interface BaseEntity {
  id: string
  createdAt: string
  updatedAt: string
  createdBy?: string
  updatedBy?: string
}

export interface Timestamps {
  createdAt: string
  updatedAt: string
  deletedAt?: string
}

// ============================================
// KPI & Metrics Types
// ============================================

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
  icon?: React.ReactNode
  trend?: 'up' | 'down' | 'neutral'
  loading?: boolean
}

// ============================================
// Filter & Table Types
// ============================================

export interface FilterOption {
  label: string
  value: string
}

export interface FilterState {
  search: string
  status?: string[]
  dateRange?: [Date, Date]
  [key: string]: unknown
}

export interface ColumnDef<T> {
  id: string
  header: string
  accessorKey?: keyof T | string
  cell?: (props: { row: { getValue: (key: string) => unknown; original: T } }) => React.ReactNode
  sortable?: boolean
  filterable?: boolean
  width?: number
}

// ============================================
// Notification Types
// ============================================

export type NotificationType = 'info' | 'success' | 'warning' | 'error'

export interface Notification {
  id: string
  type: NotificationType
  title: string
  message: string
  timestamp: string
  read: boolean
  actionUrl?: string
}

// ============================================
// Theme & UI Types
// ============================================

export type Theme = 'light' | 'dark' | 'system'

export interface ToastProps {
  id?: string
  title: string
  description?: string
  variant?: 'default' | 'destructive' | 'success' | 'warning'
  duration?: number
}

// ============================================
// Country/Region Types
// ============================================

export type CountryCode =
  | 'NG' // Nigeria
  | 'KE' // Kenya
  | 'GH' // Ghana
  | 'ZA' // South Africa
  | 'IE' // Ireland
  | 'GB' // United Kingdom
  | 'US' // United States
  | 'CA' // Canada
  | 'IN' // India
  | 'PH' // Philippines

export interface Country {
  code: CountryCode
  name: string
  flag: string
  currency: string
  locale: string
}

export const COUNTRIES: Country[] = [
  { code: 'NG', name: 'Nigeria', flag: '🇳🇬', currency: 'NGN', locale: 'en-NG' },
  { code: 'KE', name: 'Kenya', flag: '🇰🇪', currency: 'KES', locale: 'en-KE' },
  { code: 'GH', name: 'Ghana', flag: '🇬🇭', currency: 'GHS', locale: 'en-GH' },
  { code: 'ZA', name: 'South Africa', flag: '🇿🇦', currency: 'ZAR', locale: 'en-ZA' },
  { code: 'IE', name: 'Ireland', flag: '🇮🇪', currency: 'EUR', locale: 'en-IE' },
  { code: 'GB', name: 'United Kingdom', flag: '🇬🇧', currency: 'GBP', locale: 'en-GB' },
  { code: 'US', name: 'United States', flag: '🇺🇸', currency: 'USD', locale: 'en-US' },
]
