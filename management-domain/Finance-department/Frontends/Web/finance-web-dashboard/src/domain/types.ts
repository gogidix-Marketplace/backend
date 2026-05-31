// ============================================
// FINANCE DEPARTMENT - DOMAIN TYPES
// ============================================

// Core Finance Types
export type CurrencyCode = 'USD' | 'EUR' | 'GBP' | 'NGN' | 'KES' | 'ZAR' | 'GHS' | 'EGP'

export type BudgetStatus = 'draft' | 'pending' | 'approved' | 'active' | 'exceeded' | 'closed'
export type ExpenseStatus = 'draft' | 'pending' | 'approved' | 'rejected' | 'paid' | 'reimbursed'
export type InvoiceStatus = 'draft' | 'sent' | 'viewed' | 'approved' | 'paid' | 'overdue' | 'cancelled'
export type PaymentStatus = 'pending' | 'processing' | 'completed' | 'failed' | 'refunded'
export type ApprovalStatus = 'pending' | 'approved' | 'rejected' | 'cancelled'

export type ExpenseCategory =
  | 'travel'
  | 'meals'
  | 'office_supplies'
  | 'software'
  | 'equipment'
  | 'training'
  | 'marketing'
  | 'utilities'
  | 'rent'
  | 'insurance'
  | 'other'

export type InvoiceType = 'accounts_receivable' | 'accounts_payable'

// Money type with currency support
export interface Money {
  amount: number
  currency: CurrencyCode
  formatted: string
}

// Budget Types
export interface Budget {
  id: string
  name: string
  code: string
  department: string
  category?: string
  fiscalYear: number
  period: 'Q1' | 'Q2' | 'Q3' | 'Q4' | 'H1' | 'H2' | 'annual'
  allocated: Money
  spent: Money
  remaining: Money
  variancePercentage: number
  status: BudgetStatus
  managerId: string
  managerName: string
  description?: string
  tags: string[]
  createdAt: string
  updatedAt: string
  approvedAt?: string
  approvedBy?: string
}

export interface BudgetLineItem {
  id: string
  budgetId: string
  category: string
  description: string
  allocated: Money
  spent: Money
  remaining: Money
  variancePercentage: number
}

// Expense Types
export interface Expense {
  id: string
  title: string
  description?: string
  category: ExpenseCategory
  amount: Money
  submittedBy: string
  submittedByName: string
  submittedByDepartment: string
  department: string
  date: string
  status: ExpenseStatus
  receiptUrl?: string
  receiptFileName?: string
  projectCode?: string
  taxAmount?: Money
  totalAmount: Money
  reimbursable: boolean
  reimbursedAmount?: Money
  reimbursedDate?: string
  approvedBy?: string
  approvedByName?: string
  approvedAt?: string
  rejectionReason?: string
  createdAt: string
  updatedAt: string
}

export interface ExpenseReport {
  id: string
  title: string
  submittedBy: string
  submittedByName: string
  department: string
  period: { start: string; end: string }
  expenses: Expense[]
  totalAmount: Money
  status: ExpenseStatus
  submittedAt: string
  approvedAt?: string
  approvedBy?: string
  rejectionReason?: string
}

// Invoice Types
export interface Invoice {
  id: string
  invoiceNumber: string
  type: InvoiceType
  vendorId?: string
  vendorName?: string
  customerId?: string
  customerName?: string
  amount: Money
  taxAmount: Money
  totalAmount: Money
  currency: CurrencyCode
  issueDate: string
  dueDate: string
  paidDate?: string
  status: InvoiceStatus
  lineItems: InvoiceLineItem[]
  purchaseOrder?: string
  notes?: string
  attachmentUrl?: string
  createdAt: string
  updatedAt: string
  approvedBy?: string
  approvedAt?: string
}

export interface InvoiceLineItem {
  id: string
  description: string
  quantity: number
  unitPrice: Money
  amount: Money
  taxRate?: number
  taxAmount?: Money
}

// Payment Types
export interface Payment {
  id: string
  type: 'incoming' | 'outgoing'
  amount: Money
  currency: CurrencyCode
  method: 'bank_transfer' | 'credit_card' | 'debit_card' | 'check' | 'cash' | 'other'
  status: PaymentStatus
  reference: string
  description: string
  invoiceId?: string
  invoiceNumber?: string
  vendorId?: string
  vendorName?: string
  customerId?: string
  customerName?: string
  processedAt?: string
  scheduledFor?: string
  metadata?: Record<string, unknown>
  createdAt: string
  updatedAt: string
}

// Approval Types
export interface ApprovalRequest {
  id: string
  type: 'budget' | 'expense' | 'invoice' | 'payment'
  typeId: string
  title: string
  description: string
  amount: Money
  requestedBy: string
  requestedByName: string
  requestedAt: string
  currentApprover: string
  approvalLevel: number
  totalLevels: number
  status: ApprovalStatus
  comments: ApprovalComment[]
  priority: 'low' | 'medium' | 'high' | 'urgent'
  department: string
  dueDate?: string
}

export interface ApprovalComment {
  id: string
  userId: string
  userName: string
  comment: string
  timestamp: string
}

// Financial Report Types
export interface FinancialReport {
  id: string
  name: string
  type: 'profit_loss' | 'balance_sheet' | 'cash_flow' | 'budget_variance' | 'expense_summary' | 'custom'
  period: { start: string; end: string }
  department?: string
  currency: CurrencyCode
  generatedAt: string
  generatedBy: string
  status: 'generating' | 'ready' | 'failed'
  fileUrl?: string
  fileSize?: number
  scheduled?: boolean
  schedule?: string
}

export interface ProfitLossStatement {
  revenue: {
    total: Money
    breakdown: RevenueItem[]
  }
  expenses: {
    total: Money
    breakdown: ExpenseItem[]
  }
  grossProfit: Money
  operatingIncome: Money
  netIncome: Money
  margin: {
    gross: number
    operating: number
    net: number
  }
}

export interface RevenueItem {
  category: string
  amount: Money
  percentage: number
}

export interface ExpenseItem {
  category: string
  amount: Money
  percentage: number
}

// KPI Types
export interface FinanceKPI {
  id: string
  name: string
  description: string
  value: number | string
  previousValue?: number | string
  trend: 'up' | 'down' | 'neutral'
  status: 'on_track' | 'warning' | 'critical'
  target?: number
  unit?: string
  currency?: CurrencyCode
  period: string
  lastUpdated: string
}

export interface FinanceDashboardData {
  kpis: FinanceKPI[]
  totalRevenue: Money
  totalExpenses: Money
  netIncome: Money
  cashBalance: Money
  pendingApprovals: number
  overdueInvoices: number
  budgetUtilization: number
  accountsReceivable: Money
  accountsPayable: Money
  recentTransactions: Transaction[]
  upcomingPayments: Payment[]
  budgetAlerts: BudgetAlert[]
}

export interface Transaction {
  id: string
  type: 'income' | 'expense'
  description: string
  amount: Money
  category: string
  date: string
  status: string
  reference?: string
}

export interface BudgetAlert {
  id: string
  budgetId: string
  budgetName: string
  type: 'exceeded' | 'warning' | 'info'
  message: string
  threshold: number
  current: number
  createdAt: string
}

// Currency Rates
export interface CurrencyRate {
  from: CurrencyCode
  to: CurrencyCode
  rate: number
  timestamp: string
}

// Filter Types
export interface BudgetFilters {
  department?: string
  status?: BudgetStatus
  fiscalYear?: number
  period?: string
  manager?: string
  search?: string
}

export interface ExpenseFilters {
  department?: string
  category?: ExpenseCategory
  status?: ExpenseStatus
  submittedBy?: string
  dateFrom?: string
  dateTo?: string
  amountFrom?: number
  amountTo?: number
  reimbursable?: boolean
  search?: string
}

export interface InvoiceFilters {
  type?: InvoiceType
  status?: InvoiceStatus
  vendor?: string
  customer?: string
  dateFrom?: string
  dateTo?: string
  amountFrom?: number
  amountTo?: number
  overdue?: boolean
  search?: string
}

// Pagination
export interface PaginatedResponse<T> {
  data: T[]
  total: number
  page: number
  pageSize: number
  totalPages: number
  hasNext: boolean
  hasPrevious: boolean
}

// API Response Types
export interface ApiResponse<T> {
  success: boolean
  data?: T
  error?: {
    code: string
    message: string
    details?: Record<string, unknown>
  }
  meta?: {
    timestamp: string
    requestId: string
  }
}
