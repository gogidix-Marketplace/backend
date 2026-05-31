// ============================================
// FINANCE DEPARTMENT - ZUSTAND STORE
// ============================================

import { create } from 'zustand'
import { persist } from 'zustand/middleware'
import type {
  User,
  Budget,
  Expense,
  Invoice,
  Payment,
  ApprovalRequest,
  FinanceDashboardData,
  BudgetFilters,
  ExpenseFilters,
  InvoiceFilters,
  PaginatedResponse,
} from '@domain'

interface AuthState {
  user: User | null
  isAuthenticated: boolean
  is2FAVerified: boolean
  login: (email: string, password: string) => Promise<void>
  verify2FA: (code: string) => boolean
  logout: () => void
  hasPermission: (permission: string) => boolean
  hasRole: (role: string) => boolean
}

interface FinanceState {
  // Dashboard Data
  dashboardData: FinanceDashboardData | null
  dashboardLoading: boolean
  dashboardError: string | null

  // Budgets
  budgets: Budget[]
  budgetsLoading: boolean
  budgetsError: string | null
  budgetsPagination: {
    total: number
    page: number
    pageSize: number
    totalPages: number
  } | null
  budgetFilters: BudgetFilters

  // Expenses
  expenses: Expense[]
  expensesLoading: boolean
  expensesError: string | null
  expensesPagination: {
    total: number
    page: number
    pageSize: number
    totalPages: number
  } | null
  expenseFilters: ExpenseFilters
  selectedExpense: Expense | null

  // Invoices
  invoices: Invoice[]
  invoicesLoading: boolean
  invoicesError: string | null
  invoicesPagination: {
    total: number
    page: number
    pageSize: number
    totalPages: number
  } | null
  invoiceFilters: InvoiceFilters
  selectedInvoice: Invoice | null

  // Approvals
  approvals: ApprovalRequest[]
  approvalsLoading: boolean
  approvalsError: string | null

  // Payments
  payments: Payment[]
  paymentsLoading: boolean
  paymentsError: string | null

  // UI State
  sidebarCollapsed: boolean
  activeTab: string
  notifications: Array<{ id: string; message: string; type: 'info' | 'success' | 'warning' | 'error' }>

  // Actions
  loadDashboardData: () => Promise<void>
  loadBudgets: (page?: number, filters?: BudgetFilters) => Promise<void>
  loadExpenses: (page?: number, filters?: ExpenseFilters) => Promise<void>
  loadInvoices: (page?: number, filters?: InvoiceFilters) => Promise<void>
  loadApprovals: () => Promise<void>
  loadPayments: () => Promise<void>
  setBudgetFilters: (filters: BudgetFilters) => void
  setExpenseFilters: (filters: ExpenseFilters) => void
  setInvoiceFilters: (filters: InvoiceFilters) => void
  setSelectedExpense: (expense: Expense | null) => void
  setSelectedInvoice: (invoice: Invoice | null) => void
  approveRequest: (id: string, comment?: string) => Promise<void>
  rejectRequest: (id: string, reason: string) => Promise<void>
  toggleSidebar: () => void
  setActiveTab: (tab: string) => void
  addNotification: (notification: Omit<Notification, 'id' | 'read' | 'timestamp'>) => void
  removeNotification: (id: string) => void
  clearNotifications: () => void
}

// Mock Finance Users
const MOCK_FINANCE_USERS: Record<string, User> = {
  'finance-manager@gogidix.com': {
    id: 'usr-fin-002',
    email: 'finance-manager@gogidix.com',
    firstName: 'Amanda',
    lastName: 'Peters',
    displayName: 'Amanda Peters',
    role: 'FINANCE_MANAGER',
    department: 'finance',
    avatar: '',
    permissions: [
      'view:department',
      'view:country',
      'approve:budget',
      'approve:expense',
      'approve:invoice',
      'view:reports',
      'create:reports',
      'create:budget',
      'edit:budget',
    ],
    country: 'GB',
    isActive: true,
    createdAt: '2024-01-01T00:00:00Z',
    updatedAt: '2024-01-01T00:00:00Z',
  },
  'accountant@gogidix.com': {
    id: 'usr-fin-001',
    email: 'accountant@gogidix.com',
    firstName: 'David',
    lastName: 'Wilson',
    displayName: 'David Wilson',
    role: 'ACCOUNTANT',
    department: 'finance',
    avatar: '',
    permissions: [
      'view:department',
      'view:reports',
      'submit:expense',
    ],
    country: 'NG',
    isActive: true,
    createdAt: '2024-01-01T00:00:00Z',
    updatedAt: '2024-01-01T00:00:00Z',
  },
  'cfo@gogidix.com': {
    id: 'usr-exec-002',
    email: 'cfo@gogidix.com',
    firstName: 'Sarah',
    lastName: 'Mitchell',
    displayName: 'Sarah Mitchell',
    role: 'CFO',
    department: 'executive',
    avatar: '',
    permissions: [
      'view:all',
      'view:department',
      'view:country',
      'approve:budget',
      'approve:expense',
      'approve:invoice',
      'view:reports',
      'create:reports',
      'manage:settings',
    ],
    country: 'US',
    isActive: true,
    createdAt: '2024-01-01T00:00:00Z',
    updatedAt: '2024-01-01T00:00:00Z',
  },
}

const MOCK_PASSWORD = 'password123'

// Mock Dashboard Data
const MOCK_DASHBOARD_DATA: FinanceDashboardData = {
  kpis: [
    { id: '1', name: 'Total Revenue', description: 'Revenue for current month', value: 1284500, previousValue: 1187500, trend: 'up', status: 'on_track', unit: 'USD', currency: 'USD', period: 'Mar 2025', lastUpdated: new Date().toISOString() },
    { id: '2', name: 'Total Expenses', description: 'Expenses for current month', value: 843200, previousValue: 818000, trend: 'up', status: 'warning', unit: 'USD', currency: 'USD', period: 'Mar 2025', lastUpdated: new Date().toISOString() },
    { id: '3', name: 'Net Income', description: 'Net profit for current month', value: 441300, previousValue: 369500, trend: 'up', status: 'on_track', unit: 'USD', currency: 'USD', period: 'Mar 2025', lastUpdated: new Date().toISOString() },
    { id: '4', name: 'Cash Balance', description: 'Available cash', value: 215800, previousValue: 205200, trend: 'up', status: 'on_track', unit: 'USD', currency: 'USD', period: 'Mar 2025', lastUpdated: new Date().toISOString() },
    { id: '5', name: 'Pending Approvals', description: 'Awaiting approval', value: 12, trend: 'neutral', status: 'warning', period: 'Mar 2025', lastUpdated: new Date().toISOString() },
    { id: '6', name: 'Overdue Invoices', description: 'Invoices past due', value: 5, previousValue: 8, trend: 'down', status: 'on_track', period: 'Mar 2025', lastUpdated: new Date().toISOString() },
  ],
  totalRevenue: { amount: 1284500, currency: 'USD', formatted: '$1,284,500.00' },
  totalExpenses: { amount: 843200, currency: 'USD', formatted: '$843,200.00' },
  netIncome: { amount: 441300, currency: 'USD', formatted: '$441,300.00' },
  cashBalance: { amount: 215800, currency: 'USD', formatted: '$215,800.00' },
  pendingApprovals: 12,
  overdueInvoices: 5,
  budgetUtilization: 72.5,
  accountsReceivable: { amount: 325000, currency: 'USD', formatted: '$325,000.00' },
  accountsPayable: { amount: 185000, currency: 'USD', formatted: '$185,000.00' },
  recentTransactions: [],
  upcomingPayments: [],
  budgetAlerts: [],
}

// Auth Store
export const useAuthStore = create<AuthState>()(
  persist(
    (set, get) => ({
      user: null,
      isAuthenticated: false,
      is2FAVerified: false,

      login: async (email: string, password: string) => {
        await new Promise((resolve) => setTimeout(resolve, 500))

        const mockUser = MOCK_FINANCE_USERS[email.toLowerCase()]

        if (!mockUser) {
          throw new Error('Invalid credentials')
        }

        if (password !== MOCK_PASSWORD) {
          throw new Error('Invalid credentials')
        }

        if (!mockUser.isActive) {
          throw new Error('Account is inactive')
        }

        set({ user: mockUser, isAuthenticated: true })
      },

      logout: () => {
        set({ user: null, isAuthenticated: false, is2FAVerified: false })
      },

      verify2FA: (code: string) => {
        if (code === '123456' || code === '000000') {
          set({ is2FAVerified: true })
          return true
        }
        return false
      },

      hasPermission: (permission: string) => {
        const { user } = get()
        return user?.permissions.includes(permission as any) ?? false
      },

      hasRole: (role: string) => {
        const { user } = get()
        return user?.role === role
      },
    }),
    {
      name: 'gogidix-finance-auth-storage',
    }
  )
)

// Finance Store
export const useFinanceStore = create<FinanceState>()((set, get) => ({
  // Initial State
  dashboardData: null,
  dashboardLoading: false,
  dashboardError: null,

  budgets: [],
  budgetsLoading: false,
  budgetsError: null,
  budgetsPagination: null,
  budgetFilters: {},

  expenses: [],
  expensesLoading: false,
  expensesError: null,
  expensesPagination: null,
  expenseFilters: {},
  selectedExpense: null,

  invoices: [],
  invoicesLoading: false,
  invoicesError: null,
  invoicesPagination: null,
  invoiceFilters: {},
  selectedInvoice: null,

  approvals: [],
  approvalsLoading: false,
  approvalsError: null,

  payments: [],
  paymentsLoading: false,
  paymentsError: null,

  sidebarCollapsed: false,
  activeTab: 'dashboard',
  notifications: [],

  // Actions
  loadDashboardData: async () => {
    set({ dashboardLoading: true, dashboardError: null })
    try {
      await new Promise((resolve) => setTimeout(resolve, 500))
      set({ dashboardData: MOCK_DASHBOARD_DATA, dashboardLoading: false })
    } catch (error) {
      set({ dashboardError: 'Failed to load dashboard data', dashboardLoading: false })
    }
  },

  loadBudgets: async (page = 1, filters) => {
    set({ budgetsLoading: true, budgetsError: null })
    try {
      await new Promise((resolve) => setTimeout(resolve, 500))
      // Mock data - in production, this would call the API
      set({
        budgets: [],
        budgetsLoading: false,
        budgetFilters: filters || get().budgetFilters,
        budgetsPagination: { total: 0, page, pageSize: 20, totalPages: 0 },
      })
    } catch (error) {
      set({ budgetsError: 'Failed to load budgets', budgetsLoading: false })
    }
  },

  loadExpenses: async (page = 1, filters) => {
    set({ expensesLoading: true, expensesError: null })
    try {
      await new Promise((resolve) => setTimeout(resolve, 500))
      set({
        expenses: [],
        expensesLoading: false,
        expenseFilters: filters || get().expenseFilters,
        expensesPagination: { total: 0, page, pageSize: 20, totalPages: 0 },
      })
    } catch (error) {
      set({ expensesError: 'Failed to load expenses', expensesLoading: false })
    }
  },

  loadInvoices: async (page = 1, filters) => {
    set({ invoicesLoading: true, invoicesError: null })
    try {
      await new Promise((resolve) => setTimeout(resolve, 500))
      set({
        invoices: [],
        invoicesLoading: false,
        invoiceFilters: filters || get().invoiceFilters,
        invoicesPagination: { total: 0, page, pageSize: 20, totalPages: 0 },
      })
    } catch (error) {
      set({ invoicesError: 'Failed to load invoices', invoicesLoading: false })
    }
  },

  loadApprovals: async () => {
    set({ approvalsLoading: true, approvalsError: null })
    try {
      await new Promise((resolve) => setTimeout(resolve, 500))
      set({ approvals: [], approvalsLoading: false })
    } catch (error) {
      set({ approvalsError: 'Failed to load approvals', approvalsLoading: false })
    }
  },

  loadPayments: async () => {
    set({ paymentsLoading: true, paymentsError: null })
    try {
      await new Promise((resolve) => setTimeout(resolve, 500))
      set({ payments: [], paymentsLoading: false })
    } catch (error) {
      set({ paymentsError: 'Failed to load payments', paymentsLoading: false })
    }
  },

  setBudgetFilters: (filters) => set({ budgetFilters: filters }),
  setExpenseFilters: (filters) => set({ expenseFilters: filters }),
  setInvoiceFilters: (filters) => set({ invoiceFilters: filters }),
  setSelectedExpense: (expense) => set({ selectedExpense: expense }),
  setSelectedInvoice: (invoice) => set({ selectedInvoice: invoice }),

  approveRequest: async (id: string, comment?: string) => {
    try {
      await new Promise((resolve) => setTimeout(resolve, 500))
      set((state) => ({
        approvals: state.approvals.map((a) =>
          a.id === id ? { ...a, status: 'approved' as const } : a
        ),
      }))
      get().addNotification({ message: 'Request approved successfully', type: 'success' })
    } catch (error) {
      get().addNotification({ message: 'Failed to approve request', type: 'error' })
    }
  },

  rejectRequest: async (id: string, reason: string) => {
    try {
      await new Promise((resolve) => setTimeout(resolve, 500))
      set((state) => ({
        approvals: state.approvals.map((a) =>
          a.id === id ? { ...a, status: 'rejected' as const } : a
        ),
      }))
      get().addNotification({ message: 'Request rejected', type: 'warning' })
    } catch (error) {
      get().addNotification({ message: 'Failed to reject request', type: 'error' })
    }
  },

  toggleSidebar: () => set((state) => ({ sidebarCollapsed: !state.sidebarCollapsed })),
  setActiveTab: (tab) => set({ activeTab: tab }),

  addNotification: (notification) => set((state) => ({
    notifications: [
      ...state.notifications,
      { ...notification, id: Date.now().toString() },
    ],
  })),

  removeNotification: (id) => set((state) => ({
    notifications: state.notifications.filter((n) => n.id !== id),
  })),

  clearNotifications: () => set({ notifications: [] }),
}))
