// User & Authentication Types
export type SalesUserRole =
  | 'SALES_DIRECTOR'
  | 'SALES_MANAGER'
  | 'SALES_REPRESENTATIVE'
  | 'SALES_ASSOCIATE'
  | 'REGIONAL_MANAGER'
  | 'COUNTRY_DIRECTOR'
  | 'VP_SALES'
  | 'SALES_ANALYST'
  | 'SALES_OPS_MANAGER'

export type Department =
  | 'sales'
  | 'executive'
  | 'finance'
  | 'human-resource'
  | 'customer-support'
  | 'system-administrator'
  | 'global-business-management'
  | 'digital-marketing'
  | 'foundation-services'

export interface SalesUser {
  id: string
  email: string
  firstName: string
  lastName: string
  displayName: string
  role: SalesUserRole
  department: Department
  avatar?: string
  permissions: string[]
  country?: string
  region?: string
  territory?: string[]
  targets?: SalesTargets
  isActive: boolean
  createdAt: string
  updatedAt: string
}

export interface SalesTargets {
  annualQuota: number
  monthlyQuota: number
  quarterlyQuota: number
  currentRevenue: number
  commissionRate: number
}

// Lead Types
export interface Lead {
  id: string
  company: string
  contactName: string
  email: string
  phone?: string
  title?: string
  source: LeadSource
  status: LeadStatus
  score: number
  estimatedValue: number
  assignedTo?: string
  territory?: string
  country: string
  tags: string[]
  lastActivity?: string
  createdAt: string
  updatedAt: string
}

export type LeadSource =
  | 'website'
  | 'referral'
  | 'partner'
  | 'trade_show'
  | 'cold_call'
  | 'email_campaign'
  | 'social_media'
  | 'other'

export type LeadStatus =
  | 'new'
  | 'contacted'
  | 'qualified'
  | 'unqualified'
  | 'converted'
  | 'lost'

// Opportunity/Deal Types
export interface Opportunity {
  id: string
  dealNumber: string
  accountName: string
  contactName: string
  value: number
  currency: string
  stage: OpportunityStage
  probability: number
  expectedCloseDate: string
  owner: string
  territory?: string
  country: string
  source: string
  type: DealType
  priority: DealPriority
  competitors?: string[]
  nextStep?: string
  description?: string
  products: OpportunityProduct[]
  activities: Activity[]
  createdAt: string
  updatedAt: string
}

export type OpportunityStage =
  | 'prospecting'
  | 'qualification'
  | 'proposal'
  | 'negotiation'
  | 'closed_won'
  | 'closed_lost'

export type DealType = 'new_business' | 'renewal' | 'upsell' | 'cross_sell'
export type DealPriority = 'low' | 'medium' | 'high' | 'urgent'

export interface OpportunityProduct {
  id: string
  name: string
  quantity: number
  unitPrice: number
  discount: number
  total: number
}

// Customer/Account Types
export interface Customer {
  id: string
  accountNumber: string
  name: string
  industry: string
  tier: CustomerTier
  status: CustomerStatus
  website?: string
  billingAddress?: Address
  shippingAddress?: Address
  contacts: Contact[]
  territory?: string
  country: string
  assignedTo?: string
  annualRevenue: number
  employeeCount?: number
  notes?: string
  tags: string[]
  acquisitions: number
  totalValue: number
  createdAt: string
  updatedAt: string
}

export type CustomerTier = 'enterprise' | 'mid_market' | 'small_business' | 'micro'
export type CustomerStatus = 'active' | 'inactive' | 'prospect' | 'churned'

export interface Address {
  street: string
  city: string
  state: string
  postalCode: string
  country: string
}

export interface Contact {
  id: string
  firstName: string
  lastName: string
  title: string
  email: string
  phone?: string
  mobile?: string
  isPrimary: boolean
  linkedIn?: string
}

// Sales Analytics Types
export interface SalesMetrics {
  revenue: {
    current: number
    target: number
    previous: number
    growth: number
  }
  pipeline: {
    total: number
    weighted: number
    count: number
  }
  deals: {
    closed: number
    inProgress: number
    won: number
    lost: number
  }
  forecast: {
    month: number
    quarter: number
    year: number
  }
  conversion: {
    leadToOpportunity: number
    opportunityToDeal: number
  }
}

export interface RegionalMetrics {
  country: string
  countryCode: string
  revenue: number
  target: number
  attainment: number
  growth: number
  pipeline: number
  dealsClosed: number
  teamSize: number
}

// Territory Types
export interface Territory {
  id: string
  name: string
  type: TerritoryType
  country: string
  region?: string
  description?: string
  assignedTo: string[]
  leads: number
  opportunities: number
  revenue: number
  target: number
  boundaries?: TerritoryBoundary[]
  createdAt: string
  updatedAt: string
}

export type TerritoryType = 'geographic' | 'industry' | 'company_size' | 'hybrid'

export interface TerritoryBoundary {
  type: string
  coordinates: any[]
}

// Commission Types
export interface Commission {
  id: string
  salesRepId: string
  salesRepName: string
  period: string
  type: CommissionType
  salesAmount: number
  commissionRate: number
  commissionAmount: number
  deals: number
  status: CommissionStatus
  paidDate?: string
  calculatedAt: string
}

export type CommissionType = 'direct_sales' | 'override' | 'bonus' | 'partner_referral'
export type CommissionStatus = 'pending' | 'approved' | 'paid' | 'declined'

// Forecast Types
export interface Forecast {
  id: string
  period: ForecastPeriod
  year: number
  month?: number
  quarter?: number
  territory?: string
  country?: string
  scenario: ForecastScenario
  predictedRevenue: number
  confidence: number
  pipelineContribution: number
  committedDeals: number
  bestCase: number
  worstCase: number
  createdAt: string
  updatedAt: string
}

export type ForecastPeriod = 'month' | 'quarter' | 'year'
export type ForecastScenario = 'commit' | 'upside' | 'downside' | 'baseline'

// Activity/Communication Types
export interface Activity {
  id: string
  type: ActivityType
  subject: string
  description?: string
  regarding?: string
  regardingType?: 'lead' | 'opportunity' | 'customer'
  assignedTo: string
  status: ActivityStatus
  priority: 'low' | 'medium' | 'high'
  startDate: string
  endDate?: string
  duration?: number
  location?: string
  notes?: string
  participants?: string[]
  outcome?: string
  createdAt: string
  updatedAt: string
}

export type ActivityType =
  | 'call'
  | 'email'
  | 'meeting'
  | 'presentation'
  | 'demo'
  | 'site_visit'
  | 'follow_up'
  | 'task'
  | 'note'

export type ActivityStatus = 'scheduled' | 'in_progress' | 'completed' | 'cancelled'

// Partner Types
export interface SalesPartner {
  id: string
  partnerNumber: string
  name: string
  type: PartnerType
  status: PartnerStatus
  tier: PartnerTier
  country: string
  region?: string
  contactPerson: string
  email: string
  phone?: string
  commissionRate: number
  leadsGenerated: number
  dealsClosed: number
  totalRevenue: number
  territory?: string[]
  specialties: string[]
  joinedAt: string
  lastActivity: string
}

export type PartnerType = 'referrer' | 'reseller' | 'system_integrator' | 'consultant'
export type PartnerStatus = 'pending' | 'active' | 'suspended' | 'terminated'
export type PartnerTier = 'bronze' | 'silver' | 'gold' | 'platinum'

// Product Types
export interface Product {
  id: string
  sku: string
  name: string
  category: string
  type: 'product' | 'service'
  price: number
  currency: string
  commissionRate: number
  active: boolean
}

// Navigation Types
export interface NavItem {
  title: string
  href: string
  icon?: string
  badge?: number
  children?: NavItem[]
  permission?: string
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

// Filter Types
export interface SalesFilters {
  dateRange?: {
    from: string
    to: string
  }
  countries?: string[]
  territories?: string[]
  salesReps?: string[]
  stages?: OpportunityStage[]
  leadSources?: LeadSource[]
  valueRange?: {
    min: number
    max: number
  }
}
