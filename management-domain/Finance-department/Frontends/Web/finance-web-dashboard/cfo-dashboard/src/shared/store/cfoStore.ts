import { create } from 'zustand'
import { persist } from 'zustand/middleware'

export interface User {
  email: string
  name: string
  role: string
  avatar?: string
}

export interface StrategicMetric {
  label: string
  value: string
  change: number
  changeLabel: string
  icon: string
}

export interface ApprovalItem {
  id: string
  type: 'budget' | 'expense' | 'investment' | 'vendor'
  title: string
  amount: number
  currency: string
  country: string
  requestor: string
  department: string
  date: string
  priority: 'high' | 'medium' | 'low'
  status: 'pending' | 'approved' | 'rejected' | 'changes_requested'
  description: string
}

export interface RiskItem {
  id: string
  name: string
  category: string
  severity: 'critical' | 'high' | 'medium' | 'low'
  likelihood: number
  impact: number
  status: 'active' | 'mitigated' | 'monitoring'
  owner: string
  description: string
}

export interface StrategyGoal {
  id: string
  title: string
  progress: number
  target: string
  deadline: string
  owner: string
  status: 'on_track' | 'at_risk' | 'behind'
}

export interface MAPipelineItem {
  id: string
  company: string
  sector: string
  stage: 'due_diligence' | 'negotiation' | 'letter_of_intent' | 'closed'
  value: number
  currency: string
  expectedClose: string
}

export interface InvestorMetric {
  label: string
  value: string
  change: number
  changeLabel: string
}

export interface BoardMeeting {
  id: string
  title: string
  date: string
  time: string
  attendees: string
  status: 'upcoming' | 'completed' | 'cancelled'
}

export interface Communication {
  id: string
  type: 'email' | 'call' | 'meeting' | 'report'
  contact: string
  subject: string
  date: string
  summary: string
}

interface AuthState {
  user: User | null
  isAuthenticated: boolean
  is2FAVerified: boolean
  isLoading: boolean
  error: string | null
  login: (email: string, password: string) => Promise<boolean>
  verify2FA: (code: string) => boolean
  logout: () => void
  clearError: () => void
}

interface StrategicState {
  metrics: StrategicMetric[]
  financialHealthScore: number
  revenueTrend: { month: string; value: number }[]
  strategicPriorities: { name: string; progress: number }[]
  aiInsights: string[]
  isLoadingMetrics: boolean
  loadStrategicData: () => Promise<void>
}

interface ApprovalsState {
  approvals: ApprovalItem[]
  isLoadingApprovals: boolean
  loadApprovals: () => Promise<void>
  approveItem: (id: string) => void
  rejectItem: (id: string) => void
  requestChanges: (id: string) => void
}

interface RiskState {
  risks: RiskItem[]
  fxExposure: { currency: string; exposure: number; change: number }[]
  complianceScore: number
  isLoadingRisks: boolean
  loadRiskData: () => Promise<void>
}

interface StrategyState {
  goals: StrategyGoal[]
  maPipeline: MAPipelineItem[]
  capitalAllocation: { category: string; amount: number; percentage: number }[]
  growthTargets: { region: string; target: number; actual: number }[]
  aiRecommendations: string[]
  isLoadingStrategy: boolean
  loadStrategyData: () => Promise<void>
}

interface InvestorState {
  investorMetrics: InvestorMetric[]
  shareholders: { name: string; percentage: number }[]
  communications: Communication[]
  boardMeetings: BoardMeeting[]
  isLoadingInvestor: boolean
  loadInvestorData: () => Promise<void>
}

type CfoStore = AuthState & StrategicState & ApprovalsState & RiskState & StrategyState & InvestorState

const MOCK_USER: User = {
  email: 'cfo@gogidix.com',
  name: 'Sarah Mitchell',
  role: 'Chief Financial Officer',
}

const delay = (ms: number) => new Promise(resolve => setTimeout(resolve, ms))

export const useCfoStore = create<CfoStore>()(
  persist(
    (set, get) => ({
      user: null,
      isAuthenticated: false,
      is2FAVerified: false,
      isLoading: false,
      error: null,

      login: async (email: string, password: string) => {
        set({ isLoading: true, error: null })
        await delay(800)
        if (email === 'cfo@gogidix.com' && password === 'password123') {
          set({ user: MOCK_USER, isAuthenticated: true, isLoading: false })
          return true
        }
        set({ error: 'Invalid credentials', isLoading: false })
        return false
      },

      logout: () => {
        set({ user: null, isAuthenticated: false, is2FAVerified: false, error: null })
      },

      clearError: () => set({ error: null }),

      verify2FA: (code: string) => {
        if (code === '123456' || code === '000000') {
          set({ is2FAVerified: true })
          return true
        }
        return false
      },

      metrics: [],
      financialHealthScore: 0,
      revenueTrend: [],
      strategicPriorities: [],
      aiInsights: [],
      isLoadingMetrics: false,

      loadStrategicData: async () => {
        set({ isLoadingMetrics: true })
        await delay(600)
        set({
          metrics: [
            { label: 'Total Revenue', value: '$2.84B', change: 12.4, changeLabel: 'vs last quarter', icon: 'DollarSign' },
            { label: 'Net Income', value: '$428M', change: 8.7, changeLabel: 'vs last quarter', icon: 'TrendingUp' },
            { label: 'EBITDA', value: '$712M', change: 15.2, changeLabel: 'vs last quarter', icon: 'BarChart3' },
            { label: 'Cash Position', value: '$1.23B', change: -2.1, changeLabel: 'vs last quarter', icon: 'Wallet' },
            { label: 'Debt-to-Equity', value: '0.42', change: -5.3, changeLabel: 'improved', icon: 'Scale' },
            { label: 'ROI', value: '18.6%', change: 3.2, changeLabel: 'vs last quarter', icon: 'Target' },
          ],
          financialHealthScore: 87,
          revenueTrend: [
            { month: 'Jul', value: 680 },
            { month: 'Aug', value: 720 },
            { month: 'Sep', value: 695 },
            { month: 'Oct', value: 780 },
            { month: 'Nov', value: 810 },
            { month: 'Dec', value: 850 },
            { month: 'Jan', value: 890 },
            { month: 'Feb', value: 920 },
            { month: 'Mar', value: 945 },
            { month: 'Apr', value: 960 },
            { month: 'May', value: 990 },
            { month: 'Jun', value: 1020 },
          ],
          strategicPriorities: [
            { name: 'APAC Market Expansion', progress: 72 },
            { name: 'Digital Transformation', progress: 85 },
            { name: 'Cost Optimization Program', progress: 63 },
            { name: 'ESG Compliance', progress: 91 },
            { name: 'Talent Acquisition', progress: 54 },
          ],
          aiInsights: [
            'Revenue growth in APAC region is outpacing projections by 14%. Consider accelerating investment allocation.',
            'Cash flow pattern suggests potential shortfall in Q3. Recommend increasing credit facility by $50M.',
            'Vendor consolidation opportunity identified in European operations — estimated savings of $12M annually.',
            'Currency hedging strategy is underperforming. Recommend rebalancing EUR/USD positions.',
            'M&A target synergy estimates for TechVista acquisition may be overstated by 15-20%.',
          ],
          isLoadingMetrics: false,
        })
      },

      approvals: [],
      isLoadingApprovals: false,

      loadApprovals: async () => {
        set({ isLoadingApprovals: true })
        await delay(500)
        set({
          approvals: [
            { id: 'APR-001', type: 'budget', title: 'Q3 Marketing Budget Increase', amount: 2500000, currency: 'USD', country: 'United States', requestor: 'James Wilson', department: 'Marketing', date: '2026-04-22', priority: 'high', status: 'pending', description: 'Request for additional marketing budget to support product launch campaign in North America.' },
            { id: 'APR-002', type: 'investment', title: 'Cloud Infrastructure Upgrade', amount: 4800000, currency: 'USD', country: 'Germany', requestor: 'Lisa Chen', department: 'Technology', date: '2026-04-21', priority: 'high', status: 'pending', description: 'Major cloud infrastructure upgrade to support growing user base and new AI features.' },
            { id: 'APR-003', type: 'expense', title: 'Executive Team Offsite', amount: 185000, currency: 'EUR', country: 'France', requestor: 'David Park', department: 'Operations', date: '2026-04-20', priority: 'medium', status: 'pending', description: 'Annual executive leadership team offsite for strategic planning and team alignment.' },
            { id: 'APR-004', type: 'vendor', title: 'Deloitte Audit Services Renewal', amount: 1200000, currency: 'USD', country: 'United Kingdom', requestor: 'Emma Roberts', department: 'Finance', date: '2026-04-19', priority: 'medium', status: 'pending', description: 'Annual audit services contract renewal with Deloitte for FY2027.' },
            { id: 'APR-005', type: 'investment', title: 'TechVista Acquisition Due Diligence', amount: 15000000, currency: 'USD', country: 'Singapore', requestor: 'Michael Torres', department: 'Corporate Development', date: '2026-04-18', priority: 'high', status: 'pending', description: 'Funding approval for due diligence phase of TechVista Systems acquisition.' },
            { id: 'APR-006', type: 'budget', title: 'New Office Setup - Tokyo', amount: 3200000, currency: 'JPY', country: 'Japan', requestor: 'Yuki Tanaka', department: 'Real Estate', date: '2026-04-17', priority: 'medium', status: 'pending', description: 'Budget allocation for new Tokyo office including lease, renovation, and equipment.' },
            { id: 'APR-007', type: 'expense', title: 'Annual Insurance Premium', amount: 890000, currency: 'USD', country: 'United States', requestor: 'Rachel Kim', department: 'Risk Management', date: '2026-04-16', priority: 'low', status: 'pending', description: 'Annual corporate insurance premium payment for comprehensive coverage.' },
            { id: 'APR-008', type: 'vendor', title: 'AWS Enterprise Agreement', amount: 5600000, currency: 'USD', country: 'United States', requestor: 'Alex Kumar', department: 'Technology', date: '2026-04-15', priority: 'high', status: 'pending', description: 'Three-year enterprise agreement with AWS for cloud services with committed spend.' },
            { id: 'APR-009', type: 'budget', title: 'R&D Lab Expansion', amount: 7500000, currency: 'USD', country: 'South Korea', requestor: 'Dr. Soo-Jin Lee', department: 'Research & Development', date: '2026-04-14', priority: 'high', status: 'pending', description: 'Capital budget for expanding R&D laboratory facilities in Seoul.' },
            { id: 'APR-010', type: 'expense', title: 'Employee Wellness Program', amount: 340000, currency: 'USD', country: 'Canada', requestor: 'Maria Santos', department: 'Human Resources', date: '2026-04-13', priority: 'low', status: 'pending', description: 'Annual employee wellness and mental health program expansion.' },
          ],
          isLoadingApprovals: false,
        })
      },

      approveItem: (id: string) => {
        set(state => ({
          approvals: state.approvals.map(a => a.id === id ? { ...a, status: 'approved' as const } : a),
        }))
      },

      rejectItem: (id: string) => {
        set(state => ({
          approvals: state.approvals.map(a => a.id === id ? { ...a, status: 'rejected' as const } : a),
        }))
      },

      requestChanges: (id: string) => {
        set(state => ({
          approvals: state.approvals.map(a => a.id === id ? { ...a, status: 'changes_requested' as const } : a),
        }))
      },

      risks: [],
      fxExposure: [],
      complianceScore: 0,
      isLoadingRisks: false,

      loadRiskData: async () => {
        set({ isLoadingRisks: true })
        await delay(600)
        set({
          risks: [
            { id: 'RSK-001', name: 'Currency Volatility (EUR/USD)', category: 'Market Risk', severity: 'high', likelihood: 4, impact: 5, status: 'active', owner: 'Treasury Team', description: 'Significant EUR/USD exposure with potential $45M impact on Q3 earnings.' },
            { id: 'RSK-002', name: 'Supply Chain Disruption', category: 'Operational Risk', severity: 'high', likelihood: 3, impact: 4, status: 'monitoring', owner: 'Operations', description: 'Ongoing semiconductor shortage affecting product delivery timelines.' },
            { id: 'RSK-003', name: 'Regulatory Changes - GDPR', category: 'Compliance Risk', severity: 'medium', likelihood: 3, impact: 3, status: 'active', owner: 'Legal & Compliance', description: 'New EU data protection amendments requiring system modifications by Q4.' },
            { id: 'RSK-004', name: 'Interest Rate Increases', category: 'Market Risk', severity: 'medium', likelihood: 4, impact: 3, status: 'monitoring', owner: 'Treasury Team', description: 'Rising interest rates affecting variable rate debt portfolio.' },
            { id: 'RSK-005', name: 'Key Personnel Departure', category: 'Strategic Risk', severity: 'medium', likelihood: 2, impact: 4, status: 'active', owner: 'HR & Executive', description: 'Potential departure of key technical leadership could impact product roadmap.' },
            { id: 'RSK-006', name: 'Cybersecurity Breach', category: 'Technology Risk', severity: 'critical', likelihood: 2, impact: 5, status: 'active', owner: 'CISO', description: 'Increasing sophistication of cyber threats targeting financial data.' },
            { id: 'RSK-007', name: 'Competitive Pressure - Pricing', category: 'Strategic Risk', severity: 'medium', likelihood: 4, impact: 3, status: 'monitoring', owner: 'Strategy Team', description: 'New market entrants driving pricing pressure in core segments.' },
            { id: 'RSK-008', name: 'Tax Regulation Changes', category: 'Compliance Risk', severity: 'low', likelihood: 3, impact: 2, status: 'monitoring', owner: 'Tax Department', description: 'Potential changes to international tax treaties affecting transfer pricing.' },
            { id: 'RSK-009', name: 'Climate/ESG Risk', category: 'ESG Risk', severity: 'medium', likelihood: 3, impact: 4, status: 'active', owner: 'Sustainability Team', description: 'Increasing stakeholder expectations on carbon neutrality commitments.' },
            { id: 'RSK-010', name: 'M&A Integration Risk', category: 'Strategic Risk', severity: 'high', likelihood: 3, impact: 5, status: 'active', owner: 'Corp Development', description: 'Integration challenges with pending TechVista acquisition could delay synergies.' },
          ],
          fxExposure: [
            { currency: 'EUR/USD', exposure: 285000000, change: -2.4 },
            { currency: 'GBP/USD', exposure: 142000000, change: 1.1 },
            { currency: 'JPY/USD', exposure: 98000000, change: -3.8 },
            { currency: 'CNY/USD', exposure: 67000000, change: 0.5 },
            { currency: 'KRW/USD', exposure: 45000000, change: -1.2 },
          ],
          complianceScore: 94,
          isLoadingRisks: false,
        })
      },

      goals: [],
      maPipeline: [],
      capitalAllocation: [],
      growthTargets: [],
      aiRecommendations: [],
      isLoadingStrategy: false,

      loadStrategyData: async () => {
        set({ isLoadingStrategy: true })
        await delay(700)
        set({
          goals: [
            { id: 'SG-001', title: 'Achieve $3B Revenue Target', progress: 78, target: '$3.0B by FY2027', deadline: '2027-03-31', owner: 'Revenue Team', status: 'on_track' },
            { id: 'SG-002', title: 'Improve Operating Margin to 25%', progress: 65, target: '25% operating margin', deadline: '2027-06-30', owner: 'Operations', status: 'at_risk' },
            { id: 'SG-003', title: 'Complete APAC Expansion Phase 2', progress: 42, target: '5 new markets', deadline: '2027-09-30', owner: 'International', status: 'on_track' },
            { id: 'SG-004', title: 'Reduce Debt-to-Equity to 0.35', progress: 81, target: '0.35 ratio', deadline: '2027-03-31', owner: 'Treasury', status: 'on_track' },
            { id: 'SG-005', title: 'Launch Sustainability Fund', progress: 33, target: '$200M fund', deadline: '2027-12-31', owner: 'ESG Committee', status: 'behind' },
            { id: 'SG-006', title: 'Digitize 90% of Financial Processes', progress: 88, target: '90% automation', deadline: '2026-12-31', owner: 'Digital Finance', status: 'on_track' },
          ],
          maPipeline: [
            { id: 'MA-001', company: 'TechVista Systems', sector: 'Enterprise Software', stage: 'due_diligence', value: 450000000, currency: 'USD', expectedClose: '2026-Q4' },
            { id: 'MA-002', company: 'DataFlow Analytics', sector: 'Data Analytics', stage: 'negotiation', value: 120000000, currency: 'USD', expectedClose: '2026-Q3' },
            { id: 'MA-003', company: 'GreenEnergy Solutions', sector: 'CleanTech', stage: 'letter_of_intent', value: 85000000, currency: 'USD', expectedClose: '2026-Q3' },
            { id: 'MA-004', company: 'PaySecure Gateway', sector: 'FinTech', stage: 'due_diligence', value: 200000000, currency: 'USD', expectedClose: '2027-Q1' },
          ],
          capitalAllocation: [
            { category: 'Organic Growth', amount: 850000000, percentage: 34 },
            { category: 'M&A', amount: 620000000, percentage: 25 },
            { category: 'R&D', amount: 450000000, percentage: 18 },
            { category: 'Debt Reduction', amount: 300000000, percentage: 12 },
            { category: 'Shareholder Returns', amount: 200000000, percentage: 8 },
            { category: 'Reserves', amount: 80000000, percentage: 3 },
          ],
          growthTargets: [
            { region: 'North America', target: 8, actual: 9.2 },
            { region: 'Europe', target: 12, actual: 10.8 },
            { region: 'Asia Pacific', target: 20, actual: 24.6 },
            { region: 'Latin America', target: 15, actual: 13.1 },
            { region: 'Middle East & Africa', target: 18, actual: 19.5 },
          ],
          aiRecommendations: [
            'Based on current trajectory, APAC region will exceed growth target by 23%. Recommend reallocating $15M from LATAM marketing to APAC operations.',
            'TechVista acquisition valuation appears 12% above fair value. Negotiate price reduction or seek additional synergy commitments.',
            'Operating margin improvement is lagging. Suggest accelerating cloud migration to reduce infrastructure costs by $8M/quarter.',
            'Debt reduction ahead of schedule — consider redirecting $50M to M&A fund for PaySecure Gateway opportunity.',
            'ESG fund launch timeline at risk. Recommend forming dedicated taskforce with quarterly milestone reviews.',
          ],
          isLoadingStrategy: false,
        })
      },

      investorMetrics: [],
      shareholders: [],
      communications: [],
      boardMeetings: [],
      isLoadingInvestor: false,

      loadInvestorData: async () => {
        set({ isLoadingInvestor: true })
        await delay(500)
        set({
          investorMetrics: [
            { label: 'Company Valuation', value: '$18.4B', change: 14.2, changeLabel: 'vs last round' },
            { label: 'Revenue Run Rate', value: '$2.84B', change: 12.4, changeLabel: 'annualized' },
            { label: 'Burn Rate', value: '$12.3M/mo', change: -8.5, changeLabel: 'improved' },
            { label: 'Runway', value: '48 months', change: 6, changeLabel: 'extended' },
          ],
          shareholders: [
            { name: 'Institutional Investors', percentage: 42 },
            { name: 'Founder & Executive Team', percentage: 18 },
            { name: 'Venture Capital', percentage: 15 },
            { name: 'Private Equity', percentage: 12 },
            { name: 'Employee Stock Options', percentage: 8 },
            { name: 'Public Float', percentage: 5 },
          ],
          communications: [
            { id: 'COM-001', type: 'email', contact: 'BlackRock Investments', subject: 'Q1 2026 Performance Update', date: '2026-04-20', summary: 'Shared preliminary Q1 results showing strong revenue growth and margin improvement.' },
            { id: 'COM-002', type: 'call', contact: 'Sequoia Capital', subject: 'APAC Expansion Strategy Discussion', date: '2026-04-18', summary: 'Discussed go-to-market strategy for Southeast Asian markets and resource requirements.' },
            { id: 'COM-003', type: 'meeting', contact: 'Board Audit Committee', subject: 'FY2026 Audit Planning', date: '2026-04-15', summary: 'Reviewed audit scope, timeline, and key focus areas for annual audit.' },
            { id: 'COM-004', type: 'report', contact: 'All Shareholders', subject: 'Monthly Investor Report - March 2026', date: '2026-04-10', summary: 'Distributed comprehensive monthly report with financials, KPIs, and strategic updates.' },
            { id: 'COM-005', type: 'call', contact: 'Goldman Sachs', subject: 'Debt Refinancing Options', date: '2026-04-08', summary: 'Explored refinancing opportunities for $500M debt facility maturing in 2027.' },
            { id: 'COM-006', type: 'meeting', contact: 'ESG Advisory Board', subject: 'Sustainability Report Review', date: '2026-04-05', summary: 'Reviewed annual sustainability report data and strategic ESG commitments.' },
          ],
          boardMeetings: [
            { id: 'BM-001', title: 'Q1 2026 Board Review', date: '2026-05-15', time: '09:00 AM EST', attendees: 'Full Board (12 members)', status: 'upcoming' },
            { id: 'BM-002', title: 'Audit Committee Meeting', date: '2026-05-22', time: '02:00 PM EST', attendees: 'Audit Committee (4 members)', status: 'upcoming' },
            { id: 'BM-003', title: 'Strategy Session - M&A Pipeline', date: '2026-06-05', time: '10:00 AM EST', attendees: 'Executive Committee (6 members)', status: 'upcoming' },
            { id: 'BM-004', title: 'Q4 2025 Board Review', date: '2026-01-20', time: '09:00 AM EST', attendees: 'Full Board (12 members)', status: 'completed' },
          ],
          isLoadingInvestor: false,
        })
      },
    }),
    {
      name: 'gogidix-cfo-auth',
      partialize: (state) => ({
        user: state.user,
        isAuthenticated: state.isAuthenticated,
        is2FAVerified: state.is2FAVerified,
      }),
    }
  )
)
