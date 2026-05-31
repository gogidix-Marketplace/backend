// ============================================
// FINANCE DEPARTMENT - SHARED TYPES
// ============================================

// User & Auth Types (from shared-ui-library)
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
  country: CountryCode
  isActive: boolean
  createdAt: string
  updatedAt: string
}

export type UserRole =
  | 'CEO'
  | 'CFO'
  | 'COO'
  | 'CTO'
  | 'DIRECTOR'
  | 'MANAGER'
  | 'SUPERVISOR'
  | 'EMPLOYEE'
  | 'SUPER_ADMIN'
  | 'FINANCE_MANAGER'
  | 'ACCOUNTANT'
  | 'CONTROLLER'

export type Department =
  | 'executive'
  | 'finance'
  | 'human-resource'
  | 'sales'
  | 'digital-marketing'
  | 'customer-support'
  | 'system-administrator'
  | 'global-business-management'

export type Permission =
  | 'view:all'
  | 'view:department'
  | 'view:country'
  | 'approve:budget'
  | 'approve:expense'
  | 'approve:invoice'
  | 'view:reports'
  | 'create:reports'
  | 'manage:settings'
  | 'submit:expense'
  | 'create:budget'
  | 'edit:budget'
  | 'delete:budget'
  | 'manage:users'

export type CountryCode =
  | 'US'
  | 'GB'
  | 'NG'
  | 'KE'
  | 'ZA'
  | 'GH'
  | 'IE'
  | 'EG'
  | 'Other'

// Navigation Types
export interface NavItem {
  label: string
  path: string
  icon?: string
  badge?: number
  children?: NavItem[]
}

export interface SidebarSection {
  title: string
  items: NavItem[]
}

// Chart Types
export interface ChartDataPoint {
  label: string
  value: number
  color?: string
}

export interface TimeSeriesData {
  date: string
  value: number
  label?: string
}

// Notification Types
export interface Notification {
  id: string
  type: 'info' | 'success' | 'warning' | 'error'
  title: string
  message: string
  timestamp: string
  read: boolean
  actionUrl?: string
}

// API State Types
export interface ApiState<T> {
  data: T | null
  loading: boolean
  error: string | null
  lastUpdated: string | null
}

// Form Types
export interface FormField {
  name: string
  label: string
  type: 'text' | 'number' | 'email' | 'password' | 'select' | 'multiselect' | 'date' | 'daterange' | 'textarea' | 'file' | 'checkbox'
  placeholder?: string
  required?: boolean
  options?: SelectOption[]
  validation?: ValidationRule[]
  defaultValue?: unknown
}

export interface SelectOption {
  value: string | number
  label: string
  disabled?: boolean
}

export interface ValidationRule {
  type: 'required' | 'min' | 'max' | 'minLength' | 'maxLength' | 'pattern' | 'email' | 'custom'
  value?: unknown
  message: string
}

// Table Types
export interface TableColumn<T> {
  id: string
  header: string
  accessor: keyof T | ((row: T) => unknown)
  cell?: (value: unknown, row: T) => React.ReactNode
  sortable?: boolean
  filterable?: boolean
  width?: number | string
}

// Filter Types
export interface FilterOption {
  label: string
  value: string
  count?: number
}

export interface ActiveFilter {
  field: string
  operator: 'eq' | 'ne' | 'gt' | 'lt' | 'gte' | 'lte' | 'contains' | 'in'
  value: unknown
}

// Export to file types
export type ExportFormat = 'pdf' | 'xlsx' | 'csv'
export type ReportPeriod = 'today' | 'yesterday' | 'this_week' | 'last_week' | 'this_month' | 'last_month' | 'this_quarter' | 'last_quarter' | 'this_year' | 'last_year' | 'custom'

export interface ExportOptions {
  format: ExportFormat
  period: ReportPeriod
  dateRange?: { start: string; end: string }
  includeCharts?: boolean
  includeDetails?: boolean
}

// Currency formatter
export function formatCurrency(amount: number, currency: string): string {
  return new Intl.NumberFormat('en-US', {
    style: 'currency',
    currency: currency,
    minimumFractionDigits: 2,
    maximumFractionDigits: 2,
  }).format(amount)
}

// Date formatter
export function formatDate(date: string | Date, format: 'short' | 'long' | 'time' = 'short'): string {
  const dateObj = typeof date === 'string' ? new Date(date) : date

  if (format === 'short') {
    return new Intl.DateTimeFormat('en-US', { month: 'short', day: 'numeric', year: 'numeric' }).format(dateObj)
  } else if (format === 'long') {
    return new Intl.DateTimeFormat('en-US', { weekday: 'long', month: 'long', day: 'numeric', year: 'numeric' }).format(dateObj)
  } else {
    return new Intl.DateTimeFormat('en-US', { hour: 'numeric', minute: '2-digit' }).format(dateObj)
  }
}

// Number formatter
export function formatNumber(value: number, decimals: number = 0): string {
  return new Intl.NumberFormat('en-US', {
    minimumFractionDigits: decimals,
    maximumFractionDigits: decimals,
  }).format(value)
}

// Percentage formatter
export function formatPercentage(value: number, decimals: number = 1): string {
  return `${value >= 0 ? '+' : ''}${value.toFixed(decimals)}%`
}

import React from 'react'
