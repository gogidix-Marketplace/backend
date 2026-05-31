// User & Authentication Types for GBM
export type GBMUserRole =
  | 'GBM_ADMIN'
  | 'REGIONAL_MANAGER'
  | 'COUNTRY_ADMIN'
  | 'GBM_ANALYST'
  | 'GBM_VIEWER'
  | 'DATA_MANAGER'
  | 'REPORT_ADMIN'

export type Region =
  | 'north-america'
  | 'europe'
  | 'africa-north'
  | 'africa-west'
  | 'africa-east'
  | 'africa-south'
  | 'asia-pacific'
  | 'latin-america'
  | 'middle-east'

export type Country =
  | 'US'
  | 'CA'
  | 'UK'
  | 'NG'
  | 'KE'
  | 'ZA'
  | 'GH'
  | 'EG'
  | 'IN'
  | 'JP'
  | 'AU'
  | 'BR'
  | 'MX'
  | 'AE'
  | 'SA'

export interface GBMUser {
  id: string
  email: string
  firstName: string
  lastName: string
  displayName: string
  role: GBMUserRole
  department: 'global-business-management'
  avatar?: string
  permissions: string[]
  regions?: Region[]
  countries?: Country[]
  isActive: boolean
  createdAt: string
  updatedAt: string
}

// Regional & Country Data Types
export interface RegionalMetrics {
  id: string
  region: Region
  regionName: string
  countries: Country[]
  totalRevenue: number
  revenueTarget: number
  growthRate: number
  marketShare: number
  customerCount: number
  activeBusinesses: number
  status: 'on_track' | 'at_risk' | 'behind' | 'ahead'
  lastUpdated: string
}

export interface CountryMetrics {
  id: string
  country: Country
  countryName: string
  region: Region
  currency: string
  totalRevenue: number
  revenueTarget: number
  localCurrencyRevenue: number
  growthRate: number
  marketShare: number
  customerCount: number
  activeBusinesses: number
  status: 'on_track' | 'at_risk' | 'behind' | 'ahead'
  lastUpdated: string
}

// Currency Management Types
export interface Currency {
  code: string
  name: string
  symbol: string
  flag: string
  exchangeRate: number
  baseCurrency: 'USD'
  lastUpdated: string
  isActive: boolean
}

export interface CurrencyConversion {
  from: Currency
  to: Currency
  amount: number
  result: number
  rate: number
  timestamp: string
}

export interface ExchangeRateHistory {
  id: string
  currencyCode: string
  rate: number
  date: string
  baseCurrency: 'USD'
}

// Data Aggregation Types
export interface AggregationBatch {
  id: string
  name: string
  type: 'daily' | 'weekly' | 'monthly' | 'quarterly' | 'custom'
  status: 'pending' | 'running' | 'completed' | 'failed'
  sourceRegions: Region[]
  sourceCountries: Country[]
  dataPoints: number
  startedAt: string
  completedAt?: string
  error?: string
}

export interface RealTimeDataStream {
  id: string
  name: string
  source: Region | Country
  metrics: string[]
  updateFrequency: number // seconds
  isActive: boolean
  lastUpdate: string
}

// Report Types
export interface GBMReport {
  id: string
  name: string
  type: 'executive' | 'regional' | 'country' | 'currency' | 'custom'
  description: string
  createdBy: string
  createdAt: string
  format: 'pdf' | 'excel' | 'powerpoint' | 'csv'
  size: number
  downloadUrl: string
  schedule?: ReportSchedule
  parameters: ReportParameter[]
}

export interface ReportSchedule {
  frequency: 'daily' | 'weekly' | 'monthly' | 'quarterly'
  dayOfWeek?: number
  dayOfMonth?: number
  time: string
  recipients: string[]
  isActive: boolean
}

export interface ReportParameter {
  name: string
  value: string | number | boolean | string[]
  type: 'string' | 'number' | 'boolean' | 'array' | 'date' | 'region' | 'country'
}

export interface ReportTemplate {
  id: string
  name: string
  description: string
  category: 'executive' | 'operational' | 'financial' | 'custom'
  parameters: ReportParameter[]
  createdBy: string
  createdAt: string
  isPublic: boolean
}

// Business Intelligence Types
export interface DashboardWidget {
  id: string
  type: 'metric' | 'chart' | 'table' | 'map' | 'gauge' | 'trend'
  title: string
  position: { x: number; y: number; w: number; h: number }
  dataSource: string
  config: Record<string, unknown>
}

export interface GBMDashboard {
  id: string
  name: string
  description: string
  type: 'executive' | 'regional' | 'country' | 'custom'
  owner: string
  widgets: DashboardWidget[]
  filters: DashboardFilter[]
  isPublic: boolean
  createdAt: string
  updatedAt: string
}

export interface DashboardFilter {
  id: string
  name: string
  type: 'date' | 'region' | 'country' | 'currency' | 'multiselect'
  defaultValue: unknown
  options?: { label: string; value: string }[]
}

// Data Explorer Types
export interface DataQuery {
  id: string
  name: string
  description: string
  dimensions: string[]
  metrics: string[]
  filters: DataFilter[]
  sortBy: string[]
  limit: number
  createdAt: string
  createdBy: string
}

export interface DataFilter {
  field: string
  operator: 'equals' | 'contains' | 'greater_than' | 'less_than' | 'between' | 'in'
  value: unknown
}

// Export Types
export interface DataExport {
  id: string
  name: string
  type: 'revenue' | 'customers' | 'businesses' | 'regional' | 'custom'
  format: 'csv' | 'excel' | 'json' | 'pdf'
  status: 'pending' | 'processing' | 'completed' | 'failed'
  fileSize?: number
  downloadUrl?: string
  createdAt: string
  completedAt?: string
  createdBy: string
  parameters: ExportParameter[]
}

export interface ExportParameter {
  name: string
  value: unknown
}

// Localization Types
export interface Language {
  code: string
  name: string
  nativeName: string
  flag: string
  isActive: boolean
  completionPercentage: number
}

export interface LocalizationSettings {
  defaultLanguage: string
  supportedLanguages: string[]
  defaultCurrency: string
  supportedCurrencies: string[]
  dateFormat: string
  timeFormat: '12h' | '24h'
  numberFormat: 'US' | 'EU' | 'ISO'
}

// KPI Types for GBM
export interface GBMKPI {
  id: string
  name: string
  description: string
  category: 'revenue' | 'growth' | 'market' | 'operational' | 'custom'
  value: number
  target: number
  unit: string
  trend: 'up' | 'down' | 'neutral'
  trendValue: number
  status: 'on_track' | 'at_risk' | 'behind' | 'ahead'
  scope: 'global' | 'regional' | 'country'
  scopeValue?: Region | Country
  owner: string
  frequency: 'daily' | 'weekly' | 'monthly' | 'quarterly'
  lastUpdated: string
}

// Navigation Types
export interface GBMNavItem {
  title: string
  href: string
  icon: string
  badge?: number
  requiredPermission?: string
  children?: GBMNavItem[]
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

// Aggregation Status
export type AggregationStatus = 'idle' | 'running' | 'completed' | 'failed'
