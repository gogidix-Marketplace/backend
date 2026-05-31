// ============================================
// FINANCE PORTAL - ZUSTAND STORE
// ============================================

import { create } from 'zustand'
import { persist } from 'zustand/middleware'

// Types
export interface Employee {
  id: string
  email: string
  firstName: string
  lastName: string
  displayName: string
  department: string
  employeeId: string
  managerId?: string
  country: string
}

export interface Expense {
  id?: string
  title: string
  description: string
  category: ExpenseCategory
  amount: number
  currency: string
  date: string
  receipt?: File
  receiptUrl?: string
  projectCode?: string
  reimbursable: boolean
  status: 'draft' | 'pending' | 'approved' | 'rejected' | 'paid'
}

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

interface AuthState {
  employee: Employee | null
  isAuthenticated: boolean
  is2FAVerified: boolean
  login: (email: string, password: string) => Promise<void>
  verify2FA: (code: string) => boolean
  logout: () => void
}

interface PortalState {
  myExpenses: Expense[]
  loading: boolean
  error: string | null
  submitExpense: (expense: Omit<Expense, 'id'>) => Promise<void>
  loadMyExpenses: () => Promise<void>
}

// Mock Employee Users
const MOCK_EMPLOYEES: Record<string, Employee> = {
  'john.davis@gogidix.com': {
    id: 'usr-emp-001',
    email: 'john.davis@gogidix.com',
    firstName: 'John',
    lastName: 'Davis',
    displayName: 'John Davis',
    department: 'Sales',
    employeeId: 'EMP-001',
    managerId: 'usr-mgr-001',
    country: 'GB',
  },
  'amanda.peters@gogidix.com': {
    id: 'usr-emp-002',
    email: 'amanda.peters@gogidix.com',
    firstName: 'Amanda',
    lastName: 'Peters',
    displayName: 'Amanda Peters',
    department: 'Digital Marketing',
    employeeId: 'EMP-002',
    managerId: 'usr-mgr-002',
    country: 'NG',
  },
  'david.wilson@gogidix.com': {
    id: 'usr-emp-003',
    email: 'david.wilson@gogidix.com',
    firstName: 'David',
    lastName: 'Wilson',
    displayName: 'David Wilson',
    department: 'IT',
    employeeId: 'EMP-003',
    managerId: 'usr-mgr-003',
    country: 'US',
  },
}

const MOCK_PASSWORD = 'password123'

const MOCK_EXPENSES: Expense[] = [
  {
    id: '1',
    title: 'Client Travel - London',
    description: 'Flight and accommodation for client meeting',
    category: 'travel',
    amount: 1250,
    currency: 'GBP',
    date: '2025-03-10',
    receiptUrl: '/receipts/travel-1.pdf',
    reimbursable: true,
    status: 'pending',
  },
  {
    id: '2',
    title: 'Software License',
    description: 'Monthly subscription for design tool',
    category: 'software',
    amount: 49.99,
    currency: 'USD',
    date: '2025-03-08',
    reimbursable: false,
    status: 'approved',
  },
  {
    id: '3',
    title: 'Team Lunch',
    description: 'Lunch with team members',
    category: 'meals',
    amount: 85.50,
    currency: 'USD',
    date: '2025-03-05',
    reimbursable: false,
    status: 'paid',
  },
]

// Auth Store
export const useAuthStore = create<AuthState>()(
  persist(
    (set, _get) => ({
      employee: null,
      isAuthenticated: false,
      is2FAVerified: false,

      login: async (email: string, password: string) => {
        await new Promise((resolve) => setTimeout(resolve, 500))

        const mockEmployee = MOCK_EMPLOYEES[email.toLowerCase()]

        if (!mockEmployee) {
          throw new Error('Invalid credentials')
        }

        if (password !== MOCK_PASSWORD) {
          throw new Error('Invalid credentials')
        }

        set({ employee: mockEmployee, isAuthenticated: true })
      },

      logout: () => {
        set({ employee: null, isAuthenticated: false, is2FAVerified: false })
      },

      verify2FA: (code: string) => {
        if (code === '123456' || code === '000000') {
          set({ is2FAVerified: true })
          return true
        }
        return false
      },
    }),
    {
      name: 'gogidix-finance-portal-auth-storage',
    }
  )
)

// Portal Store
export const usePortalStore = create<PortalState>()((set, _get) => ({
  myExpenses: [],
  loading: false,
  error: null,

  submitExpense: async (expense) => {
    set({ loading: true, error: null })
    try {
      await new Promise((resolve) => setTimeout(resolve, 1000))

      const newExpense: Expense = {
        ...expense,
        id: Date.now().toString(),
        status: 'pending',
      }

      set((state) => ({
        myExpenses: [newExpense, ...state.myExpenses],
        loading: false,
      }))
    } catch (error) {
      set({ error: 'Failed to submit expense', loading: false })
      throw error
    }
  },

  loadMyExpenses: async () => {
    set({ loading: true, error: null })
    try {
      await new Promise((resolve) => setTimeout(resolve, 500))
      set({ myExpenses: MOCK_EXPENSES, loading: false })
    } catch (error) {
      set({ error: 'Failed to load expenses', loading: false })
    }
  },
}))
