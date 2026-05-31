// ============================================
// FINANCE DEPARTMENT - SHARED TYPES
// ============================================

export type FinanceRole = 'CFO' | 'Finance Manager' | 'Accountant' | 'Accounts Payable' | 'Accounts Receivable' | 'Financial Analyst';

export interface FinanceUser {
  id: string;
  email: string;
  firstName: string;
  lastName: string;
  role: FinanceRole;
  accessibleCountries: string[];
  defaultCurrency: string;
  permissions: FinancePermission[];
}

export type FinancePermission =
  | 'VIEW_DASHBOARD'
  | 'VIEW_AP'
  | 'CREATE_AP'
  | 'APPROVE_AP'
  | 'VIEW_AR'
  | 'CREATE_AR'
  | 'MANAGE_BUDGET'
  | 'APPROVE_BUDGET'
  | 'GENERATE_REPORTS'
  | 'MANAGE_CURRENCY'
  | 'VIEW_COMPLIANCE'
  | 'MANAGE_TAX';

// ============================================
// KPI TYPES
// ============================================

export interface FinancialKPI {
  id: string;
  name: string;
  currentValue: number;
  targetValue: number;
  unit: string;
  currency?: string;
  trend: number;
  status: 'on_track' | 'at_risk' | 'behind' | 'ahead';
  category: 'revenue' | 'expense' | 'profit' | 'cash' | 'efficiency';
}

export interface FinancialHealthScore {
  overall: number;
  revenue: number;
  expenses: number;
  profitability: number;
  cashFlow: number;
  efficiency: number;
  lastCalculated: Date;
}

// ============================================
// ACCOUNTS PAYABLE TYPES
// ============================================

export interface Vendor {
  id: string;
  name: string;
  code: string;
  email: string;
  phone: string;
  country: string;
  currency: string;
  paymentTerms: number;
  taxId: string;
  status: 'active' | 'inactive' | 'blocked';
  totalPurchased: number;
  outstandingBalance: number;
}

export interface APInvoice {
  id: string;
  invoiceNumber: string;
  vendorId: string;
  vendorName: string;
  invoiceDate: Date;
  dueDate: Date;
  currency: string;
  subtotal: number;
  tax: number;
  total: number;
  status: 'draft' | 'pending_approval' | 'approved' | 'scheduled' | 'paid' | 'rejected';
  approvalStatus?: 'pending' | 'approved' | 'rejected';
  lineItems: APLineItem[];
  attachments: Attachment[];
  createdBy: string;
  createdAt: Date;
  approvedBy?: string;
  approvedAt?: Date;
  paymentScheduled?: Date;
  paidAt?: Date;
}

export interface APLineItem {
  id: string;
  description: string;
  quantity: number;
  unitPrice: number;
  taxRate: number;
  total: number;
  accountCode: string;
}

export interface Payment {
  id: string;
  paymentNumber: string;
  vendorId: string;
  vendorName: string;
  currency: string;
  total: number;
  method: 'bank_transfer' | 'check' | 'card' | 'wire';
  status: 'pending' | 'processing' | 'completed' | 'failed';
  scheduledDate: Date;
  processedAt?: Date;
  invoiceIds: string[];
  reference?: string;
}

// ============================================
// ACCOUNTS RECEIVABLE TYPES
// ============================================

export interface Customer {
  id: string;
  name: string;
  code: string;
  email: string;
  phone: string;
  country: string;
  currency: string;
  paymentTerms: number;
  taxId: string;
  status: 'active' | 'inactive' | 'blocked';
  totalInvoiced: number;
  outstandingBalance: number;
  creditLimit: number;
}

export interface ARInvoice {
  id: string;
  invoiceNumber: string;
  customerId: string;
  customerName: string;
  invoiceDate: Date;
  dueDate: Date;
  currency: string;
  subtotal: number;
  tax: number;
  total: number;
  status: 'draft' | 'sent' | 'viewed' | 'partial' | 'paid' | 'overdue' | 'void';
  lineItems: ARLineItem[];
  attachments: Attachment[];
  createdBy: string;
  createdAt: Date;
  paidAmount?: number;
  paidAt?: Date;
}

export interface ARLineItem {
  id: string;
  description: string;
  quantity: number;
  unitPrice: number;
  taxRate: number;
  total: number;
  revenueAccount: string;
}

export interface AgingBucket {
  bucket: 'current' | '1_30' | '31_60' | '61_90' | '90_plus';
  label: string;
  amount: number;
  count: number;
  percentage: number;
}

// ============================================
// BUDGET TYPES
// ============================================

export interface Budget {
  id: string;
  name: string;
  department: string;
  category: string;
  fiscalYear: number;
  currency: string;
  totalAmount: number;
  spentAmount: number;
  committedAmount: number;
  remainingAmount: number;
  variancePercent: number;
  status: 'draft' | 'submitted' | 'approved' | 'active' | 'closed';
  periods: BudgetPeriod[];
  createdBy: string;
  createdAt: Date;
  approvedBy?: string;
  approvedAt?: Date;
}

export interface BudgetPeriod {
  period: string;
  periodType: 'monthly' | 'quarterly';
  budgeted: number;
  actual: number;
  variance: number;
  variancePercent: number;
}

// ============================================
// CASH FLOW TYPES
// ============================================

export interface CashFlowItem {
  id: string;
  type: 'inflow' | 'outflow';
  category: string;
  description: string;
  amount: number;
  currency: string;
  date: Date;
  status: 'projected' | 'confirmed';
  reference?: string;
}

export interface CashFlowPosition {
  currency: string;
  openingBalance: number;
  inflows: number;
  outflows: number;
  closingBalance: number;
  change: number;
  changePercent: number;
}

// ============================================
// CURRENCY TYPES
// ============================================

export interface CurrencyRate {
  from: string;
  to: string;
  rate: number;
  inverseRate: number;
  lastUpdated: Date;
}

export interface Currency {
  code: string;
  name: string;
  symbol: string;
  flag: string;
  enabled: boolean;
  isDefault: boolean;
}

// ============================================
// REPORT TYPES
// ============================================

export interface Report {
  id: string;
  name: string;
  type: 'p_and_l' | 'balance_sheet' | 'cash_flow' | 'budget_variance' | 'aging' | 'custom';
  format: 'pdf' | 'excel' | 'csv';
  status: 'generating' | 'ready' | 'failed';
  generatedBy: string;
  generatedAt?: Date;
  url?: string;
  parameters: ReportParameters;
}

export interface ReportParameters {
  periodStart: Date;
  periodEnd: Date;
  countries: string[];
  departments?: string[];
  includeComparisons?: boolean;
  includeCharts?: boolean;
}

// ============================================
// SHARED TYPES
// ============================================

export interface Attachment {
  id: string;
  name: string;
  type: string;
  size: number;
  url: string;
  uploadedAt: Date;
}

export interface Country {
  code: string;
  name: string;
  currency: string;
  flag: string;
  timezone: string;
}
