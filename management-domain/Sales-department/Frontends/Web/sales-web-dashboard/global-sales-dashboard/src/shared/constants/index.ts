// Application Constants

// API Configuration
export const API_CONFIG = {
  baseURL: import.meta.env.VITE_API_BASE_URL || 'https://sales.hq.api/v1',
  timeout: 30000,
  retryAttempts: 3,
  retryDelay: 1000,
} as const;

// WebSocket Configuration
export const WS_CONFIG = {
  baseURL: import.meta.env.VITE_WS_BASE_URL || 'wss://sales.hq.api/v1/ws',
  reconnectInterval: 5000,
  maxReconnectAttempts: 10,
  topics: {
    GLOBAL_DASHBOARD: '/topic/global/dashboard',
    COUNTRY_UPDATES: '/topic/countries/',
    MAJOR_DEALS: '/topic/deals/major',
    DEALS_WON: '/topic/deals/won',
    QUOTAS: '/topic/quotas',
    FORECAST: '/topic/forecast',
  } as const,
} as const;

// Storage Keys
export const STORAGE_KEYS = {
  ACCESS_TOKEN: 'hq_sales_access_token',
  REFRESH_TOKEN: 'hq_sales_refresh_token',
  USER_DATA: 'hq_sales_user',
  THEME: 'hq_sales_theme',
  SIDEBAR_STATE: 'hq_sales_sidebar',
  FILTERS: 'hq_sales_filters_',
} as const;

// Pagination
export const PAGINATION = {
  DEFAULT_PAGE_SIZE: 20,
  PAGE_SIZES: [10, 20, 50, 100],
} as const;

// Date Formats
export const DATE_FORMATS = {
  DISPLAY: 'MMM d, yyyy',
  DISPLAY_WITH_TIME: 'MMM d, yyyy h:mm a',
  SHORT: 'MM/dd/yyyy',
  INPUT: 'yyyy-MM-dd',
  MONTH_YEAR: 'MMMM yyyy',
  QUARTER_YEAR: 'QQQ yyyy',
} as const;

// Currency
export const CURRENCY = {
  DEFAULT: 'USD',
  SYMBOLS: {
    USD: '$',
    NGN: '\u20A6',
    KES: 'KSh',
    GHS: 'GH\u20B5',
    ZAR: 'R',
    ETB: 'Br',
    UGX: 'UGX',
  } as const,
} as const;

// Country Flags (emoji)
export const COUNTRY_FLAGS = {
  NG: '\uD83C\uDDF3\uD83C\uDDEC',
  KE: '\uD83C\uDDF0\uD83C\uDDEA',
  GH: '\uD83C\uDDEC\uD83C\uDDED',
  ZA: '\uD83C\uDDFF\uD83C\uDDE6',
  ET: '\uD83C\uDDEA\uD83C\uDDF9',
  UG: '\uD83C\uDDFA\uD83C\uDDEC',
  TZ: '\uD83C\uDDF9\uD83C\uDDFF',
  RW: '\uD83C\uDDF7\uD83C\uDDFC',
  CI: '\uD83C\uDDE8\uD83C\uDDEE',
  SN: '\uD83C\uDDF8\uD83C\uDDF7',
  ML: '\uD83C\uDDF2\uD83C\uDDFC',
  BF: '\uD83C\uDDE7\uD83C\uDDEB',
  NE: '\uD83C\uDDF3\uD83C\uDDEA',
  CM: '\uD83C\uDDE8\uD83C\uDDF2',
} as const;

// Partner Types
export const PARTNER_TYPES = {
  COURIER: { label: 'Courier', icon: '\uD83D\uDE9A', color: '#3B82F6' },
  HAULAGE: { label: 'Haulage', icon: '\uD83D\uDE9B', color: '#10B981' },
  WAREHOUSE: { label: 'Warehouse', icon: '\uD83D\uDCE6', color: '#F59E0B' },
  ECOMMERCE: { label: 'E-commerce', icon: '\uD83D\uDED2', color: '#8B5CF6' },
  AIR_OCEAN: { label: 'Air/Ocean', icon: '\u2708\uFE0F', color: '#06B6D4' },
  LOCATION_AGENT: { label: 'Location Agent', icon: '\uD83D\uDCCD', color: '#EC4899' },
  WHOLESALE: { label: 'Wholesale', icon: '\uD83D\uDCE6', color: '#F97316' },
  INFLUENCER: { label: 'Influencer', icon: '\uD83D\uDCF1', color: '#14B8A6' },
} as const;

// Partner Tiers
export const PARTNER_TIERS = {
  BRONZE: { label: 'Bronze', color: '#CD7F32', threshold: 0 },
  SILVER: { label: 'Silver', color: '#C0C0C0', threshold: 500000 },
  GOLD: { label: 'Gold', color: '#FFD700', threshold: 2000000 },
  PLATINUM: { label: 'Platinum', color: '#E5E4E2', threshold: 5000000 },
} as const;

// Alert Types
export const ALERT_TYPES = {
  info: { color: '#3B82F6', icon: '\u2139\uFE0F' },
  warning: { color: '#F59E0B', icon: '\u26A0\uFE0F' },
  success: { color: '#10B981', icon: '\u2705' },
  error: { color: '#EF4444', icon: '\u274C' },
} as const;

// Pipeline Stages
export const PIPELINE_STAGES = {
  NEW: { label: 'New', order: 1, color: '#9CA3AF' },
  QUALIFIED: { label: 'Qualified', order: 2, color: '#3B82F6' },
  PROPOSAL: { label: 'Proposal', order: 3, color: '#8B5CF6' },
  NEGOTIATING: { label: 'Negotiating', order: 4, color: '#F59E0B' },
  CLOSING: { label: 'Closing', order: 5, color: '#10B981' },
  WON: { label: 'Won', order: 6, color: '#059669' },
  LOST: { label: 'Lost', order: 7, color: '#EF4444' },
} as const;

// Roles
export const USER_ROLES = {
  VP_SALES: { label: 'VP of Sales', level: 100 },
  GLOBAL_SALES_DIRECTOR: { label: 'Global Sales Director', level: 80 },
  REGIONAL_SALES_MANAGER: { label: 'Regional Sales Manager', level: 60 },
  HQ_SALES_ANALYST: { label: 'HQ Sales Analyst', level: 40 },
  SALES_OPERATIONS_MANAGER: { label: 'Sales Operations Manager', level: 70 },
} as const;

// Report Types
export const REPORT_TYPES = {
  EXECUTIVE_DASHBOARD: { label: 'Executive Dashboard', icon: '\uD83D\uDCCA' },
  COUNTRY_COMPARISON: { label: 'Country Comparison', icon: '\uD83C\uDDF0' },
  PIPELINE_ANALYSIS: { label: 'Pipeline Analysis', icon: '\uD83D\uDCCA' },
  PERFORMANCE_REPORT: { label: 'Performance Report', icon: '\uD83D\uDCC8' },
  FORECAST_REPORT: { label: 'Forecast Report', icon: '\uD83D\uDCC8' },
  PRODUCT_REPORT: { label: 'Product Report', icon: '\uD83D\uDCE6' },
  TEAM_REPORT: { label: 'Team Report', icon: '\uD83D\uDC65' },
  CUSTOM: { label: 'Custom Report', icon: '\u270D\uFE0F' },
} as const;

// Theme Colors
export const THEME_COLORS = {
  primary: '#2563EB',
  secondary: '#7C3AED',
  success: '#10B981',
  warning: '#F59E0B',
  error: '#EF4444',
  info: '#3B82F6',
  gray: {
    50: '#F9FAFB',
    100: '#F3F4F6',
    200: '#E5E7EB',
    300: '#D1D5DB',
    400: '#9CA3AF',
    500: '#6B7280',
    600: '#4B5563',
    700: '#374151',
    800: '#1F2937',
    900: '#111827',
  },
} as const;

// Chart Colors
export const CHART_COLORS = [
  '#2563EB', '#7C3AED', '#10B981', '#F59E0B', '#EF4444',
  '#06B6D4', '#8B5CF6', '#EC4899', '#F97316', '#14B8A6',
] as const;

// Navigation Items
export const NAV_ITEMS = [
  { id: 'overview', label: 'Global Overview', path: '/', icon: '\uD83C\uDFE0' },
  { id: 'countries', label: 'Countries', path: '/countries', icon: '\uD83C\uDDF0' },
  { id: 'teams', label: 'Sales Teams', path: '/teams', icon: '\uD83D\uDC65' },
  { id: 'partners', label: 'Partners', path: '/partners', icon: '\uD83E\uDD1D' },
  { id: 'pipeline', label: 'Pipeline', path: '/pipeline', icon: '\uD83D\uDCC8' },
  { id: 'customers', label: 'Customers', path: '/customers', icon: '\uD83D\uDC65' },
  { id: 'performance', label: 'Performance', path: '/performance', icon: '\uD83D\uDCC8' },
  { id: 'territories', label: 'Territories', path: '/territories', icon: '\uD83C\uDFAF' },
  { id: 'forecasting', label: 'Forecasting', path: '/forecasting', icon: '\uD83D\uDCC8' },
  { id: 'reports', label: 'Reports', path: '/reports', icon: '\uD83D\uDCCA' },
  { id: 'settings', label: 'Settings', path: '/settings', icon: '\u2699\uFE0F' },
] as const;

// Sub-navigation items
export const PARTNER_SUB_ITEMS = [
  { id: 'partners-overview', label: 'Overview', path: '/partners' },
  { id: 'applications', label: 'Applications', path: '/partners/applications' },
  { id: 'performance', label: 'Performance', path: '/partners/performance' },
  { id: 'commission', label: 'Commission', path: '/partners/commission' },
  { id: 'territory', label: 'Territory', path: '/partners/territory' },
  { id: 'analytics', label: 'Analytics', path: '/partners/analytics' },
] as const;

// Time Periods
export const TIME_PERIODS = [
  { value: 'today', label: 'Today' },
  { value: 'week', label: 'This Week' },
  { value: 'month', label: 'This Month' },
  { value: 'quarter', label: 'This Quarter' },
  { value: 'year', label: 'This Year' },
  { value: 'custom', label: 'Custom Range' },
] as const;

// Export Formats
export const EXPORT_FORMATS = ['PDF', 'EXCEL', 'CSV'] as const;

// Commission Payout Frequencies
export const PAYOUT_FREQUENCIES = [
  { value: 'MONTHLY', label: 'Monthly' },
  { value: 'QUARTERLY', label: 'Quarterly' },
  { value: 'ANNUAL', label: 'Annually' },
] as const;

// Commission Tiers
export const COMMISSION_TIERS = {
  BRONZE: { rate: 0.05, label: 'Bronze (5%)' },
  SILVER: { rate: 0.075, label: 'Silver (7.5%)' },
  GOLD: { rate: 0.10, label: 'Gold (10%)' },
  PLATINUM: { rate: 0.125, label: 'Platinum (12.5%)' },
} as const;

// Error Codes
export const ERROR_CODES = {
  AUTH_001: 'Invalid or expired token',
  AUTH_002: 'Insufficient permissions',
  AUTH_003: 'Invalid credentials',
  VAL_001: 'Validation error',
  VAL_002: 'Missing required field',
  VAL_003: 'Invalid format',
  RES_001: 'Resource not found',
  RES_002: 'Country not in scope',
  RES_003: 'Resource conflict',
  SRV_001: 'Internal server error',
  SRV_002: 'Service unavailable',
  AGG_001: 'Aggregation error',
  AGG_002: 'Country data unavailable',
} as const;
