import { create } from 'zustand'
import { persist } from 'zustand/middleware'

export interface User {
  email: string
  name: string
  role: 'FINANCIAL_ANALYST' | 'REPORT_ADMIN'
  avatar?: string
}

export interface ReportTemplate {
  id: string
  name: string
  description: string
  icon: string
  category: 'financial' | 'operational' | 'compliance' | 'custom'
  lastUsed: string
  sections: string[]
}

export interface ScheduledReport {
  id: string
  reportName: string
  templateId: string
  schedule: 'Daily' | 'Weekly' | 'Monthly' | 'Quarterly'
  nextRun: string
  recipients: string[]
  status: 'Active' | 'Paused' | 'Error'
  format: 'PDF' | 'Excel' | 'CSV'
  lastRun: string
}

export interface ReportHistory {
  id: string
  name: string
  type: string
  period: string
  generatedDate: string
  format: 'PDF' | 'Excel' | 'CSV'
  size: string
  status: 'Completed' | 'Failed' | 'Processing'
  generatedBy: string
  downloadUrl: string
}

export interface GenerationState {
  step: number
  isGenerating: boolean
  progress: number
  selectedTemplate: string | null
  reportName: string
  period: string
  countries: string[]
  sections: string[]
  format: 'PDF' | 'Excel' | 'CSV'
  result: ReportHistory | null
}

const MOCK_USERS: Record<string, User> = {
  'analyst@gogidix.com': {
    email: 'analyst@gogidix.com',
    name: 'Emily Chen',
    role: 'FINANCIAL_ANALYST',
  },
  'report-admin@gogidix.com': {
    email: 'report-admin@gogidix.com',
    name: 'James Murphy',
    role: 'REPORT_ADMIN',
  },
}

const MOCK_TEMPLATES: ReportTemplate[] = [
  {
    id: 'tpl-001',
    name: 'P&L Consolidated',
    description: 'Consolidated profit and loss statement across all subsidiaries with multi-currency support.',
    icon: 'TrendingUp',
    category: 'financial',
    lastUsed: '2026-04-20',
    sections: ['Revenue', 'COGS', 'Operating Expenses', 'Net Income', 'EBITDA'],
  },
  {
    id: 'tpl-002',
    name: 'Balance Sheet',
    description: 'Complete balance sheet report with assets, liabilities, and equity breakdowns.',
    icon: 'Scale',
    category: 'financial',
    lastUsed: '2026-04-18',
    sections: ['Current Assets', 'Fixed Assets', 'Current Liabilities', 'Long-term Debt', 'Equity'],
  },
  {
    id: 'tpl-003',
    name: 'Cash Flow Statement',
    description: 'Detailed cash flow analysis covering operating, investing, and financing activities.',
    icon: 'DollarSign',
    category: 'financial',
    lastUsed: '2026-04-15',
    sections: ['Operating Activities', 'Investing Activities', 'Financing Activities', 'Net Cash Flow'],
  },
  {
    id: 'tpl-004',
    name: 'Budget Variance',
    description: 'Comparison report between budgeted and actual figures with variance analysis.',
    icon: 'BarChart3',
    category: 'operational',
    lastUsed: '2026-04-22',
    sections: ['Revenue Variance', 'Expense Variance', 'Margin Analysis', 'Forecast Adjustment'],
  },
  {
    id: 'tpl-005',
    name: 'Country Comparison',
    description: 'Side-by-side financial comparison across different country operations.',
    icon: 'Globe',
    category: 'operational',
    lastUsed: '2026-04-10',
    sections: ['Revenue by Country', 'Cost Comparison', 'Tax Rates', 'Currency Impact'],
  },
  {
    id: 'tpl-006',
    name: 'Audit Report',
    description: 'Comprehensive audit trail report with transaction details and compliance checks.',
    icon: 'Shield',
    category: 'compliance',
    lastUsed: '2026-04-05',
    sections: ['Audit Summary', 'Findings', 'Recommendations', 'Action Items'],
  },
  {
    id: 'tpl-007',
    name: 'Tax Summary',
    description: 'Tax obligations summary across all jurisdictions with filing status tracking.',
    icon: 'FileText',
    category: 'compliance',
    lastUsed: '2026-04-12',
    sections: ['Corporate Tax', 'VAT/GST', 'Withholding Tax', 'Transfer Pricing'],
  },
  {
    id: 'tpl-008',
    name: 'Custom Report',
    description: 'Build your own report by selecting sections, metrics, and visualization options.',
    icon: 'Palette',
    category: 'custom',
    lastUsed: '2026-04-21',
    sections: ['Custom Section 1', 'Custom Section 2', 'Custom Section 3'],
  },
]

const MOCK_SCHEDULED_REPORTS: ScheduledReport[] = [
  {
    id: 'sch-001',
    reportName: 'Daily Revenue Summary',
    templateId: 'tpl-001',
    schedule: 'Daily',
    nextRun: '2026-04-24T08:00:00Z',
    recipients: ['cfo@gogidix.com', 'finance-team@gogidix.com'],
    status: 'Active',
    format: 'PDF',
    lastRun: '2026-04-23T08:00:00Z',
  },
  {
    id: 'sch-002',
    reportName: 'Weekly P&L Consolidated',
    templateId: 'tpl-001',
    schedule: 'Weekly',
    nextRun: '2026-04-28T06:00:00Z',
    recipients: ['cfo@gogidix.com', 'analyst@gogidix.com'],
    status: 'Active',
    format: 'Excel',
    lastRun: '2026-04-21T06:00:00Z',
  },
  {
    id: 'sch-003',
    reportName: 'Monthly Balance Sheet',
    templateId: 'tpl-002',
    schedule: 'Monthly',
    nextRun: '2026-05-01T07:00:00Z',
    recipients: ['cfo@gogidix.com', 'board@gogidix.com'],
    status: 'Active',
    format: 'PDF',
    lastRun: '2026-04-01T07:00:00Z',
  },
  {
    id: 'sch-004',
    reportName: 'Quarterly Audit Summary',
    templateId: 'tpl-006',
    schedule: 'Quarterly',
    nextRun: '2026-07-01T09:00:00Z',
    recipients: ['audit@gogidix.com', 'compliance@gogidix.com'],
    status: 'Active',
    format: 'PDF',
    lastRun: '2026-01-01T09:00:00Z',
  },
  {
    id: 'sch-005',
    reportName: 'Monthly Tax Report',
    templateId: 'tpl-007',
    schedule: 'Monthly',
    nextRun: '2026-05-05T10:00:00Z',
    recipients: ['tax@gogidix.com'],
    status: 'Paused',
    format: 'Excel',
    lastRun: '2026-03-05T10:00:00Z',
  },
  {
    id: 'sch-006',
    reportName: 'Weekly Country Comparison',
    templateId: 'tpl-005',
    schedule: 'Weekly',
    nextRun: '2026-04-25T06:00:00Z',
    recipients: ['analyst@gogidix.com', 'regional-managers@gogidix.com'],
    status: 'Error',
    format: 'CSV',
    lastRun: '2026-04-18T06:00:00Z',
  },
]

const MOCK_HISTORY: ReportHistory[] = [
  { id: 'rep-001', name: 'P&L Q1 2026 Consolidated', type: 'P&L Consolidated', period: 'Q1 2026', generatedDate: '2026-04-22T14:30:00Z', format: 'PDF', size: '2.4 MB', status: 'Completed', generatedBy: 'Emily Chen', downloadUrl: '#' },
  { id: 'rep-002', name: 'Balance Sheet March 2026', type: 'Balance Sheet', period: 'Mar 2026', generatedDate: '2026-04-20T09:15:00Z', format: 'Excel', size: '1.8 MB', status: 'Completed', generatedBy: 'James Murphy', downloadUrl: '#' },
  { id: 'rep-003', name: 'Cash Flow Q1 2026', type: 'Cash Flow Statement', period: 'Q1 2026', generatedDate: '2026-04-19T16:45:00Z', format: 'PDF', size: '3.1 MB', status: 'Completed', generatedBy: 'Emily Chen', downloadUrl: '#' },
  { id: 'rep-004', name: 'Budget Variance March 2026', type: 'Budget Variance', period: 'Mar 2026', generatedDate: '2026-04-18T11:20:00Z', format: 'Excel', size: '1.2 MB', status: 'Completed', generatedBy: 'Emily Chen', downloadUrl: '#' },
  { id: 'rep-005', name: 'Country Comparison Q1 2026', type: 'Country Comparison', period: 'Q1 2026', generatedDate: '2026-04-17T08:30:00Z', format: 'PDF', size: '4.5 MB', status: 'Completed', generatedBy: 'James Murphy', downloadUrl: '#' },
  { id: 'rep-006', name: 'Annual Audit Report 2025', type: 'Audit Report', period: 'FY 2025', generatedDate: '2026-04-15T13:00:00Z', format: 'PDF', size: '8.2 MB', status: 'Completed', generatedBy: 'James Murphy', downloadUrl: '#' },
  { id: 'rep-007', name: 'Tax Summary Q1 2026', type: 'Tax Summary', period: 'Q1 2026', generatedDate: '2026-04-14T10:45:00Z', format: 'Excel', size: '1.5 MB', status: 'Completed', generatedBy: 'Emily Chen', downloadUrl: '#' },
  { id: 'rep-008', name: 'Custom Revenue Analysis', type: 'Custom Report', period: 'Q1 2026', generatedDate: '2026-04-13T15:30:00Z', format: 'PDF', size: '2.0 MB', status: 'Completed', generatedBy: 'Emily Chen', downloadUrl: '#' },
  { id: 'rep-009', name: 'P&L February 2026', type: 'P&L Consolidated', period: 'Feb 2026', generatedDate: '2026-04-10T09:00:00Z', format: 'PDF', size: '1.9 MB', status: 'Completed', generatedBy: 'James Murphy', downloadUrl: '#' },
  { id: 'rep-010', name: 'Balance Sheet February 2026', type: 'Balance Sheet', period: 'Feb 2026', generatedDate: '2026-04-08T14:20:00Z', format: 'Excel', size: '1.7 MB', status: 'Completed', generatedBy: 'Emily Chen', downloadUrl: '#' },
  { id: 'rep-011', name: 'Cash Flow March 2026', type: 'Cash Flow Statement', period: 'Mar 2026', generatedDate: '2026-04-07T11:15:00Z', format: 'CSV', size: '0.8 MB', status: 'Completed', generatedBy: 'Emily Chen', downloadUrl: '#' },
  { id: 'rep-012', name: 'Budget Variance Q1 2026', type: 'Budget Variance', period: 'Q1 2026', generatedDate: '2026-04-05T08:45:00Z', format: 'Excel', size: '1.3 MB', status: 'Failed', generatedBy: 'James Murphy', downloadUrl: '#' },
  { id: 'rep-013', name: 'Country Comparison Mar 2026', type: 'Country Comparison', period: 'Mar 2026', generatedDate: '2026-04-03T16:00:00Z', format: 'PDF', size: '3.8 MB', status: 'Completed', generatedBy: 'Emily Chen', downloadUrl: '#' },
  { id: 'rep-014', name: 'Tax Summary March 2026', type: 'Tax Summary', period: 'Mar 2026', generatedDate: '2026-04-02T10:30:00Z', format: 'Excel', size: '1.1 MB', status: 'Completed', generatedBy: 'James Murphy', downloadUrl: '#' },
  { id: 'rep-015', name: 'Daily Revenue Apr 22', type: 'P&L Consolidated', period: 'Apr 22, 2026', generatedDate: '2026-04-22T23:59:00Z', format: 'PDF', size: '0.5 MB', status: 'Processing', generatedBy: 'System', downloadUrl: '#' },
  { id: 'rep-016', name: 'Weekly Cash Position W16', type: 'Cash Flow Statement', period: 'Week 16, 2026', generatedDate: '2026-04-21T18:00:00Z', format: 'PDF', size: '0.9 MB', status: 'Completed', generatedBy: 'System', downloadUrl: '#' },
]

interface ReportsState {
  user: User | null
  isAuthenticated: boolean
  is2FAVerified: boolean

  templates: ReportTemplate[]
  scheduledReports: ScheduledReport[]
  reportsHistory: ReportHistory[]
  generation: GenerationState

  notification: string | null

  login: (email: string, password: string) => boolean
  logout: () => void
  verify2FA: (code: string) => boolean
  setNotification: (msg: string | null) => void

  startGeneration: () => void
  setGenerationStep: (step: number) => void
  setGenerationField: <K extends keyof GenerationState>(key: K, value: GenerationState[K]) => void
  completeGeneration: (report: ReportHistory) => void
  resetGeneration: () => void

  toggleScheduleStatus: (id: string) => void
  deleteSchedule: (id: string) => void
}

export const useReportsStore = create<ReportsState>()(
  persist(
    (set, get) => ({
      user: null,
      isAuthenticated: false,
      is2FAVerified: false,

      templates: MOCK_TEMPLATES,
      scheduledReports: MOCK_SCHEDULED_REPORTS,
      reportsHistory: MOCK_HISTORY,
      generation: {
        step: 1,
        isGenerating: false,
        progress: 0,
        selectedTemplate: null,
        reportName: '',
        period: '',
        countries: [],
        sections: [],
        format: 'PDF',
        result: null,
      },

      notification: null,

      login: (email: string, _password: string) => {
        const user = MOCK_USERS[email]
        if (user) {
          set({ user, isAuthenticated: true })
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

      setNotification: (msg) => set({ notification: msg }),

      startGeneration: () => {
        set({
          generation: {
            ...get().generation,
            isGenerating: true,
            progress: 0,
          },
        })
      },

      setGenerationStep: (step) => {
        set({
          generation: { ...get().generation, step },
        })
      },

      setGenerationField: (key, value) => {
        set({
          generation: { ...get().generation, [key]: value },
        })
      },

      completeGeneration: (report) => {
        set((state) => ({
          reportsHistory: [report, ...state.reportsHistory],
          generation: {
            step: 4,
            isGenerating: false,
            progress: 100,
            selectedTemplate: null,
            reportName: '',
            period: '',
            countries: [],
            sections: [],
            format: 'PDF',
            result: report,
          },
        }))
      },

      resetGeneration: () => {
        set({
          generation: {
            step: 1,
            isGenerating: false,
            progress: 0,
            selectedTemplate: null,
            reportName: '',
            period: '',
            countries: [],
            sections: [],
            format: 'PDF',
            result: null,
          },
        })
      },

      toggleScheduleStatus: (id) => {
        set((state) => ({
          scheduledReports: state.scheduledReports.map((s) =>
            s.id === id
              ? { ...s, status: s.status === 'Active' ? 'Paused' : 'Active' }
              : s
          ),
        }))
      },

      deleteSchedule: (id) => {
        set((state) => ({
          scheduledReports: state.scheduledReports.filter((s) => s.id !== id),
        }))
      },
    }),
    {
      name: 'gogidix-reports-auth',
      partialize: (state) => ({
        user: state.user,
        isAuthenticated: state.isAuthenticated,
        is2FAVerified: state.is2FAVerified,
      }),
    }
  )
)
