// Application Constants for Country Sales Dashboard

// ============================================================
// API Configuration
// ============================================================

export const API_CONFIG = {
  baseURL: import.meta.env.VITE_API_BASE_URL || 'https://sales.country.api/v1',
  timeout: 30000,
  retryAttempts: 3,
  retryDelay: 1000,
} as const;

// ============================================================
// WebSocket Configuration
// ============================================================

export const WS_CONFIG = {
  baseURL: import.meta.env.VITE_WS_BASE_URL || 'wss://sales.country.api/v1/ws',
  reconnectInterval: 5000,
  maxReconnectAttempts: 10,
  topics: {
    DASHBOARD: '/topic/country/dashboard',
    DEALS: '/topic/deals',
    LEADS: '/topic/leads',
    PARTNERS: '/topic/partners',
    TEAM_UPDATES: '/topic/teams',
  } as const,
} as const;

// ============================================================
// Storage Keys
// ============================================================

export const STORAGE_KEYS = {
  ACCESS_TOKEN: 'country_sales_access_token',
  REFRESH_TOKEN: 'country_sales_refresh_token',
  USER_DATA: 'country_sales_user',
  THEME: 'country_sales_theme',
  SIDEBAR_STATE: 'country_sales_sidebar',
  FILTERS: 'country_sales_filters_',
  COUNTRY_CODE: 'country_sales_country',
} as const;

// ============================================================
// Pagination
// ============================================================

export const PAGINATION = {
  DEFAULT_PAGE_SIZE: 20,
  PAGE_SIZES: [10, 20, 50, 100],
} as const;

// ============================================================
// Date Formats
// ============================================================

export const DATE_FORMATS = {
  DISPLAY: 'MMM d, yyyy',
  DISPLAY_WITH_TIME: 'MMM d, yyyy h:mm a',
  SHORT: 'MM/dd/yyyy',
  INPUT: 'yyyy-MM-dd',
  MONTH_YEAR: 'MMMM yyyy',
  QUARTER_YEAR: 'QQQ yyyy',
} as const;

// ============================================================
// Currency
// ============================================================

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
    TZS: 'TSh',
    RWF: 'RF',
    XOF: 'CFA',
    XAF: 'CFA',
  } as const,
} as const;

// ============================================================
// Country Flags (emoji)
// ============================================================

export const COUNTRY_FLAGS = {
  NG: '\uD83C\uDDF3\uD83C\uDDEC',
  KE: '\uD83C\uDDF0\uD83C\uDDEA',
  GH: '\uD83C\uDDEC\uD83C\uDDED',
  ZA: '\uD83C\uDDFF\uD83D\uDDE6',
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

// ============================================================
// Partner Types
// ============================================================

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

// ============================================================
// Partner Tiers
// ============================================================

export const PARTNER_TIERS = {
  BRONZE: { label: 'Bronze', color: '#CD7F32', threshold: 0 },
  SILVER: { label: 'Silver', color: '#C0C0C0', threshold: 100000 },
  GOLD: { label: 'Gold', color: '#FFD700', threshold: 500000 },
  PLATINUM: { label: 'Platinum', color: '#E5E4E2', threshold: 1000000 },
} as const;

// ============================================================
// Deal Stages
// ============================================================

export const DEAL_STAGES = {
  PROSPECTING: { label: 'Prospecting', order: 1, color: '#9CA3AF', probability: 10 },
  QUALIFICATION: { label: 'Qualification', order: 2, color: '#3B82F6', probability: 25 },
  PROPOSAL: { label: 'Proposal', order: 3, color: '#8B5CF6', probability: 50 },
  NEGOTIATION: { label: 'Negotiation', order: 4, color: '#F59E0B', probability: 75 },
  CLOSING: { label: 'Closing', order: 5, color: '#10B981', probability: 90 },
  WON: { label: 'Won', order: 6, color: '#059669', probability: 100 },
  LOST: { label: 'Lost', order: 7, color: '#EF4444', probability: 0 },
} as const;

// ============================================================
// Lead Status
// ============================================================

export const LEAD_STATUS = {
  NEW: { label: 'New', color: '#3B82F6' },
  CONTACTED: { label: 'Contacted', color: '#8B5CF6' },
  QUALIFIED: { label: 'Qualified', color: '#10B981' },
  CONVERTED: { label: 'Converted', color: '#059669' },
  UNQUALIFIED: { label: 'Unqualified', color: '#F59E0B' },
  LOST: { label: 'Lost', color: '#EF4444' },
} as const;

// ============================================================
// Lead Sources
// ============================================================

export const LEAD_SOURCES = {
  WEBSITE: { label: 'Website', icon: '\uD83C\uDF0F' },
  REFERRAL: { label: 'Referral', icon: '\uD83D\uDC65' },
  PARTNER: { label: 'Partner', icon: '\uD83E\uDD1D' },
  EVENT: { label: 'Event', icon: '\uD83D\uDDD2' },
  COLD_CALL: { label: 'Cold Call', icon: '\uD83D\uDCDE' },
  EMAIL_CAMPAIGN: { label: 'Email Campaign', icon: '\u2709' },
  SOCIAL_MEDIA: { label: 'Social Media', icon: '\uD83D\uDCF1' },
  ADVERTISEMENT: { label: 'Advertisement', icon: '\uD83D\uDCE6' },
  OTHER: { label: 'Other', icon: '\u2026' },
} as const;

// ============================================================
// Lead Ratings
// ============================================================

export const LEAD_RATINGS = {
  hot: { label: 'Hot', color: '#EF4444', icon: '\uD83D\uDD25' },
  warm: { label: 'Warm', color: '#F59E0B', icon: '\u2600' },
  cold: { label: 'Cold', color: '#3B82F6', icon: '\u2744' },
} as const;

// ============================================================
// Customer Status
// ============================================================

export const CUSTOMER_STATUS = {
  active: { label: 'Active', color: '#10B981' },
  at_risk: { label: 'At Risk', color: '#F59E0B' },
  churned: { label: 'Churned', color: '#EF4444' },
  prospect: { label: 'Prospect', color: '#3B82F6' },
} as const;

// ============================================================
// Customer Tiers
// ============================================================

export const CUSTOMER_TIERS = {
  enterprise: { label: 'Enterprise', color: '#7C3AED' },
  mid_market: { label: 'Mid-Market', color: '#3B82F6' },
  small_business: { label: 'Small Business', color: '#10B981' },
} as const;

// ============================================================
// Sales Roles
// ============================================================

export const SALES_ROLES = {
  COUNTRY_MANAGER: { label: 'Country Manager', level: 100 },
  REGIONAL_MANAGER: { label: 'Regional Manager', level: 80 },
  TEAM_LEAD: { label: 'Team Lead', level: 60 },
  SALES_REPRESENTATIVE: { label: 'Sales Representative', level: 40 },
  SALES_ANALYST: { label: 'Sales Analyst', level: 30 },
  PARTNER_COORDINATOR: { label: 'Partner Coordinator', level: 50 },
} as const;

// ============================================================
// Report Types
// ============================================================

export const REPORT_TYPES = {
  SALES_PERFORMANCE: { label: 'Sales Performance', icon: '\uD83D\uDCCA' },
  TEAM_PERFORMANCE: { label: 'Team Performance', icon: '\uD83D\uDC65' },
  PIPELINE_ANALYSIS: { label: 'Pipeline Analysis', icon: '\uD83D\uDCC8' },
  PARTNER_PERFORMANCE: { label: 'Partner Performance', icon: '\uD83E\uDD1D' },
  CUSTOMER_ANALYSIS: { label: 'Customer Analysis', icon: '\uD83D\uDC65' },
  TERRITORY_REPORT: { label: 'Territory Report', icon: '\uD83C\uDFAF' },
  LEAD_CONVERSION: { label: 'Lead Conversion', icon: '\u2708' },
  FORECAST_REPORT: { label: 'Forecast Report', icon: '\uD83D\uDCC8' },
  ACTIVITY_REPORT: { label: 'Activity Report', icon: '\uD83D\uDD52' },
  CUSTOM: { label: 'Custom Report', icon: '\u270D\uFE0F' },
} as const;

// ============================================================
// Theme Colors
// ============================================================

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

// ============================================================
// Chart Colors
// ============================================================

export const CHART_COLORS = [
  '#2563EB', '#7C3AED', '#10B981', '#F59E0B', '#EF4444',
  '#06B6D4', '#8B5CF6', '#EC4899', '#F97316', '#14B8A6',
] as const;

// ============================================================
// Navigation Items
// ============================================================

export const NAV_ITEMS = [
  { id: 'overview', label: 'Dashboard', path: '/', icon: '\uD83C\uDFE0' },
  { id: 'teams', label: 'Sales Teams', path: '/teams', icon: '\uD83D\uDC65' },
  { id: 'partners', label: 'Partners', path: '/partners', icon: '\uD83E\uDD1D' },
  { id: 'pipeline', label: 'Pipeline', path: '/pipeline', icon: '\uD83D\uDCC8' },
  { id: 'customers', label: 'Customers', path: '/customers', icon: '\uD83D\uDC65' },
  { id: 'performance', label: 'Performance', path: '/performance', icon: '\uD83D\uDCC8' },
  { id: 'territories', label: 'Territories', path: '/territories', icon: '\uD83C\uDFAF' },
  { id: 'leads', label: 'Leads', path: '/leads', icon: '\u2708' },
  { id: 'deals', label: 'Deals', path: '/deals', icon: '\uD83D\uDCC8' },
  { id: 'reports', label: 'Reports', path: '/reports', icon: '\uD83D\uDCCA' },
  { id: 'settings', label: 'Settings', path: '/settings', icon: '\u2699\uFE0F' },
] as const;

// ============================================================
// Time Periods
// ============================================================

export const TIME_PERIODS = [
  { value: 'today', label: 'Today' },
  { value: 'week', label: 'This Week' },
  { value: 'month', label: 'This Month' },
  { value: 'quarter', label: 'This Quarter' },
  { value: 'year', label: 'This Year' },
  { value: 'custom', label: 'Custom Range' },
] as const;

// ============================================================
// Export Formats
// ============================================================

export const EXPORT_FORMATS = ['PDF', 'EXCEL', 'CSV'] as const;

// ============================================================
// Error Codes
// ============================================================

export const ERROR_CODES = {
  AUTH_001: 'Invalid or expired token',
  AUTH_002: 'Insufficient permissions',
  AUTH_003: 'Invalid credentials',
  AUTH_004: 'Account inactive',
  VAL_001: 'Validation error',
  VAL_002: 'Missing required field',
  VAL_003: 'Invalid format',
  RES_001: 'Resource not found',
  RES_002: 'Resource already exists',
  RES_003: 'Resource conflict',
  SRV_001: 'Internal server error',
  SRV_002: 'Service unavailable',
} as const;
