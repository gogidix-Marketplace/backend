import { create } from 'zustand'
import { persist } from 'zustand/middleware'

interface User {
  id: string
  email: string
  name: string
  role: string
  avatar?: string
}

interface Notification {
  id: string
  title: string
  message: string
  type: 'info' | 'warning' | 'success' | 'error'
  read: boolean
  createdAt: string
}

interface JournalEntry {
  id: string
  date: string
  description: string
  debit: number
  credit: number
  account: string
  reference: string
  status: 'draft' | 'posted' | 'reviewed' | 'approved'
  createdBy: string
}

interface ReconciliationItem {
  id: string
  date: string
  description: string
  amount: number
  type: 'bank' | 'book'
  matched: boolean
  matchedWith?: string
  category?: string
}

interface Invoice {
  id: string
  vendorName: string
  invoiceNumber: string
  amount: number
  dueDate: string
  receivedDate: string
  status: 'pending' | 'approved' | 'rejected' | 'paid'
  description: string
  lineItems: { description: string; quantity: number; unitPrice: number; total: number }[]
  department?: string
}

interface Payment {
  id: string
  vendorName: string
  amount: number
  dueDate: string
  paymentDate?: string
  status: 'scheduled' | 'pending' | 'processing' | 'completed' | 'failed'
  method: 'wire' | 'ach' | 'check' | 'card'
  reference: string
  invoiceId?: string
}

interface Vendor {
  id: string
  name: string
  email: string
  phone: string
  address: string
  totalPaid: number
  outstandingBalance: number
  paymentTerms: string
  category: string
  status: 'active' | 'inactive' | 'on-hold'
  taxId: string
  lastPaymentDate: string
}

interface AccountantState {
  user: User | null
  isAuthenticated: boolean
  is2FAVerified: boolean
  sidebarCollapsed: boolean
  notifications: Notification[]
  journalEntries: JournalEntry[]
  reconciliation: { bank: ReconciliationItem[]; book: ReconciliationItem[] }
  invoices: Invoice[]
  payments: Payment[]
  vendors: Vendor[]
  loading: Record<string, boolean>

  login: (email: string, password: string) => boolean
  logout: () => void
  verify2FA: (code: string) => boolean
  toggleSidebar: () => void
  addNotification: (notification: Omit<Notification, 'id' | 'read' | 'createdAt'>) => void
  markNotificationRead: (id: string) => void
  clearNotifications: () => void
  loadJournalEntries: () => void
  loadReconciliation: () => void
  loadInvoices: () => void
  loadPayments: () => void
  loadVendors: () => void
}

const MOCK_USERS: Record<string, User & { password: string }> = {
  'accountant@gogidix.com': {
    id: 'usr-001',
    email: 'accountant@gogidix.com',
    name: 'David Wilson',
    role: 'Accountant',
    password: 'password123',
  },
  'senior-accountant@gogidix.com': {
    id: 'usr-002',
    email: 'senior-accountant@gogidix.com',
    name: 'Maria Santos',
    role: 'Senior Accountant',
    password: 'password123',
  },
}

const MOCK_JOURNAL_ENTRIES: JournalEntry[] = [
  { id: 'JE-2024-001', date: '2024-12-15', description: 'Office supplies purchase', debit: 2450.00, credit: 0, account: '6100 - Office Supplies', reference: 'PO-8842', status: 'posted', createdBy: 'David Wilson' },
  { id: 'JE-2024-002', date: '2024-12-15', description: 'Accounts payable - Office supplies', debit: 0, credit: 2450.00, account: '2000 - Accounts Payable', reference: 'PO-8842', status: 'posted', createdBy: 'David Wilson' },
  { id: 'JE-2024-003', date: '2024-12-14', description: 'Monthly payroll - December', debit: 185000.00, credit: 0, account: '6200 - Salaries & Wages', reference: 'PR-122024', status: 'approved', createdBy: 'Maria Santos' },
  { id: 'JE-2024-004', date: '2024-12-14', description: 'Payroll tax withholding', debit: 0, credit: 42500.00, account: '2100 - Payroll Liabilities', reference: 'PR-122024', status: 'approved', createdBy: 'Maria Santos' },
  { id: 'JE-2024-005', date: '2024-12-13', description: 'Customer payment - Invoice #4521', debit: 15750.00, credit: 0, account: '1000 - Cash', reference: 'REC-4521', status: 'reviewed', createdBy: 'David Wilson' },
  { id: 'JE-2024-006', date: '2024-12-13', description: 'Revenue recognition - Invoice #4521', debit: 0, credit: 15750.00, account: '4000 - Revenue', reference: 'REC-4521', status: 'reviewed', createdBy: 'David Wilson' },
  { id: 'JE-2024-007', date: '2024-12-12', description: 'Equipment depreciation - December', debit: 3200.00, credit: 0, account: '6300 - Depreciation Expense', reference: 'DEP-122024', status: 'draft', createdBy: 'David Wilson' },
  { id: 'JE-2024-008', date: '2024-12-12', description: 'Accumulated depreciation', debit: 0, credit: 3200.00, account: '1500 - Accum. Depreciation', reference: 'DEP-122024', status: 'draft', createdBy: 'David Wilson' },
  { id: 'JE-2024-009', date: '2024-12-11', description: 'Rent expense - December', debit: 8500.00, credit: 0, account: '6400 - Rent Expense', reference: 'RENT-122024', status: 'posted', createdBy: 'Maria Santos' },
  { id: 'JE-2024-010', date: '2024-12-11', description: 'Prepaid rent utilized', debit: 0, credit: 8500.00, account: '1300 - Prepaid Expenses', reference: 'RENT-122024', status: 'posted', createdBy: 'Maria Santos' },
]

const MOCK_RECONCILIATION = {
  bank: [
    { id: 'BNK-001', date: '2024-12-14', description: 'Wire Transfer - Client Payment', amount: 45000.00, type: 'bank' as const, matched: true, matchedWith: 'BK-001', category: 'Revenue' },
    { id: 'BNK-002', date: '2024-12-13', description: 'ACH - Payroll Processing', amount: -142500.00, type: 'bank' as const, matched: true, matchedWith: 'BK-002', category: 'Payroll' },
    { id: 'BNK-003', date: '2024-12-12', description: 'Check #4521 - Vendor Payment', amount: -8750.00, type: 'bank' as const, matched: false, category: 'Vendor' },
    { id: 'BNK-004', date: '2024-12-11', description: 'Deposit - Customer Collection', amount: 12350.00, type: 'bank' as const, matched: false, category: 'Revenue' },
    { id: 'BNK-005', date: '2024-12-10', description: 'Bank Fee - Monthly Service', amount: -125.00, type: 'bank' as const, matched: true, matchedWith: 'BK-005', category: 'Fees' },
    { id: 'BNK-006', date: '2024-12-09', description: 'Wire Transfer - Vendor', amount: -22000.00, type: 'bank' as const, matched: true, matchedWith: 'BK-006', category: 'Vendor' },
  ],
  book: [
    { id: 'BK-001', date: '2024-12-14', description: 'Client Payment Received - INV-8820', amount: 45000.00, type: 'book' as const, matched: true, matchedWith: 'BNK-001' },
    { id: 'BK-002', date: '2024-12-13', description: 'Payroll Disbursement - Dec Cycle', amount: -142500.00, type: 'book' as const, matched: true, matchedWith: 'BNK-002' },
    { id: 'BK-003', date: '2024-12-12', description: 'Vendor Payment - Acme Corp', amount: -8750.00, type: 'book' as const, matched: false },
    { id: 'BK-004', date: '2024-12-10', description: 'Monthly Bank Service Charge', amount: -125.00, type: 'book' as const, matched: true, matchedWith: 'BNK-005' },
    { id: 'BK-005', date: '2024-12-09', description: 'Wire to GlobalTech Solutions', amount: -22000.00, type: 'book' as const, matched: true, matchedWith: 'BNK-006' },
    { id: 'BK-006', date: '2024-12-08', description: 'Interest Income', amount: 340.00, type: 'book' as const, matched: false },
  ],
}

const MOCK_INVOICES: Invoice[] = [
  {
    id: 'INV-2024-101', vendorName: 'Acme Corp', invoiceNumber: 'ACM-8842', amount: 12450.00, dueDate: '2024-12-30', receivedDate: '2024-12-10', status: 'pending', description: 'Q4 Software Licenses',
    lineItems: [{ description: 'Enterprise License - 50 seats', quantity: 50, unitPrice: 200, total: 10000 }, { description: 'Premium Support Add-on', quantity: 1, unitPrice: 2450, total: 2450 }],
    department: 'IT',
  },
  {
    id: 'INV-2024-102', vendorName: 'GlobalTech Solutions', invoiceNumber: 'GTS-7721', amount: 35000.00, dueDate: '2024-12-25', receivedDate: '2024-12-08', status: 'approved', description: 'Cloud Infrastructure Services',
    lineItems: [{ description: 'AWS Hosting - Monthly', quantity: 1, unitPrice: 22000, total: 22000 }, { description: 'CDN Services', quantity: 1, unitPrice: 8000, total: 8000 }, { description: 'Monitoring & Alerting', quantity: 1, unitPrice: 5000, total: 5000 }],
    department: 'Engineering',
  },
  {
    id: 'INV-2024-103', vendorName: 'Metro Office Supply', invoiceNumber: 'MOS-3310', amount: 2890.00, dueDate: '2025-01-15', receivedDate: '2024-12-12', status: 'pending', description: 'Office Furniture & Supplies',
    lineItems: [{ description: 'Standing Desks (x5)', quantity: 5, unitPrice: 450, total: 2250 }, { description: 'Ergonomic Chairs (x2)', quantity: 2, unitPrice: 320, total: 640 }],
    department: 'Operations',
  },
  {
    id: 'INV-2024-104', vendorName: 'Precision Marketing Inc', invoiceNumber: 'PMI-5502', amount: 18750.00, dueDate: '2024-12-28', receivedDate: '2024-12-05', status: 'paid', description: 'Holiday Campaign - Digital Ads',
    lineItems: [{ description: 'Google Ads Spend', quantity: 1, unitPrice: 10000, total: 10000 }, { description: 'Social Media Campaign', quantity: 1, unitPrice: 6500, total: 6500 }, { description: 'Creative Design Fee', quantity: 1, unitPrice: 2250, total: 2250 }],
    department: 'Marketing',
  },
  {
    id: 'INV-2024-105', vendorName: 'SecureIT Services', invoiceNumber: 'SIT-2201', amount: 7500.00, dueDate: '2025-01-05', receivedDate: '2024-12-14', status: 'pending', description: 'Annual Security Audit',
    lineItems: [{ description: 'Penetration Testing', quantity: 1, unitPrice: 5000, total: 5000 }, { description: 'Compliance Assessment', quantity: 1, unitPrice: 2500, total: 2500 }],
    department: 'IT',
  },
  {
    id: 'INV-2024-106', vendorName: 'CleanPro Services', invoiceNumber: 'CPS-1190', amount: 4200.00, dueDate: '2025-01-01', receivedDate: '2024-12-13', status: 'approved', description: 'Monthly Janitorial Services',
    lineItems: [{ description: 'Office Cleaning - Dec', quantity: 1, unitPrice: 2800, total: 2800 }, { description: 'Deep Clean - Kitchen', quantity: 1, unitPrice: 1400, total: 1400 }],
    department: 'Facilities',
  },
  {
    id: 'INV-2024-107', vendorName: 'Acme Corp', invoiceNumber: 'ACM-8890', amount: 6800.00, dueDate: '2024-12-20', receivedDate: '2024-12-01', status: 'rejected', description: 'Duplicate charge - Consulting',
    lineItems: [{ description: 'Consulting Hours', quantity: 40, unitPrice: 170, total: 6800 }],
    department: 'IT',
  },
]

const MOCK_PAYMENTS: Payment[] = [
  { id: 'PAY-2024-201', vendorName: 'GlobalTech Solutions', amount: 35000.00, dueDate: '2024-12-25', status: 'pending', method: 'wire', reference: 'WR-88421', invoiceId: 'INV-2024-102' },
  { id: 'PAY-2024-202', vendorName: 'CleanPro Services', amount: 4200.00, dueDate: '2025-01-01', status: 'scheduled', method: 'ach', reference: 'ACH-33210', invoiceId: 'INV-2024-106' },
  { id: 'PAY-2024-203', vendorName: 'Precision Marketing Inc', amount: 18750.00, dueDate: '2024-12-28', paymentDate: '2024-12-15', status: 'completed', method: 'wire', reference: 'WR-88419', invoiceId: 'INV-2024-104' },
  { id: 'PAY-2024-204', vendorName: 'Metro Office Supply', amount: 2890.00, dueDate: '2025-01-15', status: 'scheduled', method: 'check', reference: 'CHK-55220', invoiceId: 'INV-2024-103' },
  { id: 'PAY-2024-205', vendorName: 'SecureIT Services', amount: 7500.00, dueDate: '2025-01-05', status: 'pending', method: 'ach', reference: 'ACH-33215', invoiceId: 'INV-2024-105' },
  { id: 'PAY-2024-206', vendorName: 'Acme Corp', amount: 12450.00, dueDate: '2024-12-30', status: 'pending', method: 'wire', reference: 'WR-88430', invoiceId: 'INV-2024-101' },
  { id: 'PAY-2024-207', vendorName: 'CloudNet Partners', amount: 15200.00, dueDate: '2024-12-22', paymentDate: '2024-12-20', status: 'completed', method: 'wire', reference: 'WR-88415', invoiceId: 'INV-2024-098' },
  { id: 'PAY-2024-208', vendorName: 'Office Max Pro', amount: 1200.00, dueDate: '2024-12-18', paymentDate: '2024-12-17', status: 'completed', method: 'card', reference: 'CC-99210', invoiceId: 'INV-2024-095' },
]

const MOCK_VENDORS: Vendor[] = [
  { id: 'VND-001', name: 'Acme Corp', email: 'billing@acmecorp.com', phone: '(555) 123-4567', address: '100 Industrial Way, Tech City, CA 94000', totalPaid: 245800.00, outstandingBalance: 12450.00, paymentTerms: 'Net 30', category: 'Technology', status: 'active', taxId: 'XX-XXX1234', lastPaymentDate: '2024-11-28' },
  { id: 'VND-002', name: 'GlobalTech Solutions', email: 'ap@globaltech.com', phone: '(555) 234-5678', address: '250 Cloud Ave, Server Park, TX 75000', totalPaid: 412000.00, outstandingBalance: 35000.00, paymentTerms: 'Net 15', category: 'Cloud Services', status: 'active', taxId: 'XX-XXX5678', lastPaymentDate: '2024-12-10' },
  { id: 'VND-003', name: 'Metro Office Supply', email: 'orders@metrooffice.com', phone: '(555) 345-6789', address: '789 Commerce St, Business Park, IL 60000', totalPaid: 45600.00, outstandingBalance: 2890.00, paymentTerms: 'Net 45', category: 'Office Supplies', status: 'active', taxId: 'XX-XXX9012', lastPaymentDate: '2024-11-15' },
  { id: 'VND-004', name: 'Precision Marketing Inc', email: 'finance@precmkt.com', phone: '(555) 456-7890', address: '500 Ad Blvd, Creative District, NY 10000', totalPaid: 189500.00, outstandingBalance: 0, paymentTerms: 'Net 30', category: 'Marketing', status: 'active', taxId: 'XX-XXX3456', lastPaymentDate: '2024-12-15' },
  { id: 'VND-005', name: 'SecureIT Services', email: 'billing@secureit.com', phone: '(555) 567-8901', address: '321 Security Lane, Cyber Town, VA 22000', totalPaid: 62000.00, outstandingBalance: 7500.00, paymentTerms: 'Net 30', category: 'Security', status: 'active', taxId: 'XX-XXX7890', lastPaymentDate: '2024-10-30' },
  { id: 'VND-006', name: 'CleanPro Services', email: 'invoices@cleanpro.com', phone: '(555) 678-9012', address: '45 Clean St, Service Area, OH 43000', totalPaid: 52400.00, outstandingBalance: 4200.00, paymentTerms: 'Net 15', category: 'Facilities', status: 'active', taxId: 'XX-XXX2345', lastPaymentDate: '2024-12-01' },
  { id: 'VND-007', name: 'TechParts Unlimited', email: 'ap@techparts.com', phone: '(555) 789-0123', address: '888 Hardware Hwy, Component City, OR 97000', totalPaid: 12400.00, outstandingBalance: 0, paymentTerms: 'Net 60', category: 'Hardware', status: 'inactive', taxId: 'XX-XXX6789', lastPaymentDate: '2024-06-15' },
]

const MOCK_NOTIFICATIONS: Notification[] = [
  { id: 'n1', title: 'Invoice Approval Required', message: 'Invoice INV-2024-101 from Acme Corp needs your approval.', type: 'warning', read: false, createdAt: '2024-12-15T09:30:00Z' },
  { id: 'n2', title: 'Reconciliation Alert', message: '3 unmatched transactions found in December bank reconciliation.', type: 'info', read: false, createdAt: '2024-12-15T08:15:00Z' },
  { id: 'n3', title: 'Payment Processed', message: 'Payment PAY-2024-203 to Precision Marketing completed successfully.', type: 'success', read: true, createdAt: '2024-12-14T16:45:00Z' },
  { id: 'n4', title: 'Due Date Reminder', message: 'Payment to GlobalTech Solutions ($35,000) is due in 5 days.', type: 'warning', read: false, createdAt: '2024-12-14T07:00:00Z' },
  { id: 'n5', title: 'Journal Entry Posted', message: 'JE-2024-001 has been posted by David Wilson.', type: 'info', read: true, createdAt: '2024-12-13T14:20:00Z' },
]

export const useAccountantStore = create<AccountantState>()(
  persist(
    (set, get) => ({
      user: null,
      isAuthenticated: false,
      is2FAVerified: false,
      sidebarCollapsed: false,
      notifications: MOCK_NOTIFICATIONS,
      journalEntries: [],
      reconciliation: { bank: [], book: [] },
      invoices: [],
      payments: [],
      vendors: [],
      loading: {},

      login: (email: string, password: string) => {
        const mockUser = MOCK_USERS[email]
        if (mockUser && mockUser.password === password) {
          set({
            user: { id: mockUser.id, email: mockUser.email, name: mockUser.name, role: mockUser.role },
            isAuthenticated: true,
          })
          return true
        }
        return false
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

      toggleSidebar: () => {
        set((state) => ({ sidebarCollapsed: !state.sidebarCollapsed }))
      },

      addNotification: (notification) => {
        const newNotification: Notification = {
          ...notification,
          id: `n${Date.now()}`,
          read: false,
          createdAt: new Date().toISOString(),
        }
        set((state) => ({ notifications: [newNotification, ...state.notifications] }))
      },

      markNotificationRead: (id) => {
        set((state) => ({
          notifications: state.notifications.map((n) => (n.id === id ? { ...n, read: true } : n)),
        }))
      },

      clearNotifications: () => {
        set({ notifications: [] })
      },

      loadJournalEntries: () => {
        set((state) => ({ loading: { ...state.loading, journalEntries: true } }))
        setTimeout(() => {
          set((state) => ({ journalEntries: MOCK_JOURNAL_ENTRIES, loading: { ...state.loading, journalEntries: false } }))
        }, 600)
      },

      loadReconciliation: () => {
        set((state) => ({ loading: { ...state.loading, reconciliation: true } }))
        setTimeout(() => {
          set((state) => ({ reconciliation: MOCK_RECONCILIATION, loading: { ...state.loading, reconciliation: false } }))
        }, 600)
      },

      loadInvoices: () => {
        set((state) => ({ loading: { ...state.loading, invoices: true } }))
        setTimeout(() => {
          set((state) => ({ invoices: MOCK_INVOICES, loading: { ...state.loading, invoices: false } }))
        }, 600)
      },

      loadPayments: () => {
        set((state) => ({ loading: { ...state.loading, payments: true } }))
        setTimeout(() => {
          set((state) => ({ payments: MOCK_PAYMENTS, loading: { ...state.loading, payments: false } }))
        }, 600)
      },

      loadVendors: () => {
        set((state) => ({ loading: { ...state.loading, vendors: true } }))
        setTimeout(() => {
          set((state) => ({ vendors: MOCK_VENDORS, loading: { ...state.loading, vendors: false } }))
        }, 600)
      },
    }),
    {
      name: 'gogidix-accountant-auth',
      partialize: (state) => ({ user: state.user, isAuthenticated: state.isAuthenticated, is2FAVerified: state.is2FAVerified }),
    }
  )
)
