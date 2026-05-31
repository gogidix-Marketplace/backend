import { Badge } from '@shared/components/ui/badge'
import {
  TrendingUp,
  TrendingDown,
  DollarSign,
  TrendingDown as TrendDownIcon,
  ArrowUpDown,
  Wallet,
} from 'lucide-react'

export interface FinancialKPI {
  id: string
  name: string
  value: string
  change: string
  changeType: 'positive' | 'negative' | 'neutral'
  status: 'on_track' | 'at_risk' | 'behind' | 'ahead'
  targetPercent: number
  icon: React.ReactNode
}

const mockKPIs: FinancialKPI[] = [
  {
    id: 'revenue',
    name: 'Total Revenue',
    value: '$42.5M',
    change: '+8.5%',
    changeType: 'positive',
    status: 'on_track',
    targetPercent: 85,
    icon: <DollarSign className="h-4 w-4" />,
  },
  {
    id: 'growth',
    name: 'Revenue Growth',
    value: '12.3%',
    change: '+2.1%',
    changeType: 'positive',
    status: 'on_track',
    targetPercent: 95,
    icon: <TrendingUp className="h-4 w-4" />,
  },
  {
    id: 'expenses',
    name: 'Expenses vs Budget',
    value: '$28.2M',
    change: '-3.2%',
    changeType: 'positive',
    status: 'ahead',
    targetPercent: 88,
    icon: <Wallet className="h-4 w-4" />,
  },
  {
    id: 'margin',
    name: 'Profit Margin',
    value: '33.6%',
    change: '+1.8%',
    changeType: 'positive',
    status: 'on_track',
    targetPercent: 92,
    icon: <ArrowUpDown className="h-4 w-4" />,
  },
  {
    id: 'cashflow',
    name: 'Cash Flow',
    value: '$8.4M',
    change: '+15.2%',
    changeType: 'positive',
    status: 'ahead',
    targetPercent: 105,
    icon: <TrendDownIcon className="h-4 w-4" />,
  },
]

const statusConfig = {
  on_track: { label: 'On Track', bg: 'bg-emerald-50 dark:bg-emerald-950/20', text: 'text-emerald-700 dark:text-emerald-400', border: 'border-emerald-200 dark:border-emerald-800' },
  at_risk: { label: 'At Risk', bg: 'bg-amber-50 dark:bg-amber-950/20', text: 'text-amber-700 dark:text-amber-400', border: 'border-amber-200 dark:border-amber-800' },
  behind: { label: 'Behind', bg: 'bg-red-50 dark:bg-red-950/20', text: 'text-red-700 dark:text-red-400', border: 'border-red-200 dark:border-red-800' },
  ahead: { label: 'Ahead', bg: 'bg-blue-50 dark:bg-blue-950/20', text: 'text-blue-700 dark:text-blue-400', border: 'border-blue-200 dark:border-blue-800' },
}

const colorMap = {
  revenue: { from: 'from-emerald-500', to: 'to-teal-500', icon: 'bg-emerald-500' },
  growth: { from: 'from-blue-500', to: 'to-indigo-500', icon: 'bg-blue-500' },
  expenses: { from: 'from-violet-500', to: 'to-purple-500', icon: 'bg-violet-500' },
  margin: { from: 'from-cyan-500', to: 'to-sky-500', icon: 'bg-cyan-500' },
  cashflow: { from: 'from-amber-500', to: 'to-orange-500', icon: 'bg-amber-500' },
}

function FinancialKPICard({ kpi, onClick }: { kpi: FinancialKPI; onClick?: () => void }) {
  const status = statusConfig[kpi.status]
  const colors = colorMap[kpi.id as keyof typeof colorMap] || colorMap.revenue

  return (
    <button
      onClick={onClick}
      className="group w-full text-left transition-all hover:-translate-y-1"
    >
      <div className="bg-white dark:bg-slate-800 rounded-xl border border-slate-200 dark:border-slate-700 p-4 sm:p-5 h-full shadow-sm hover:shadow-lg">
        {/* Status Badge & Icon */}
        <div className="flex items-start justify-between mb-4">
          <div className={`flex h-9 w-9 sm:h-10 sm:w-10 items-center justify-center rounded-lg bg-gradient-to-br ${colors.from} ${colors.to} shadow-md`}>
            <div className="text-white">
              {kpi.icon}
            </div>
          </div>
          <Badge className={`${status.bg} ${status.text} ${status.border} text-xs font-medium`}>
            {status.label}
          </Badge>
        </div>

        {/* KPI Name */}
        <h3 className="text-xs sm:text-sm font-medium text-slate-500 dark:text-slate-400 uppercase tracking-wide mb-1">
          {kpi.name}
        </h3>

        {/* Value */}
        <p className="text-2xl sm:text-3xl font-bold text-slate-900 dark:text-white mb-2">
          {kpi.value}
        </p>

        {/* Change & Target */}
        <div className="flex items-center justify-between">
          <div className={`flex items-center gap-1 text-sm font-medium ${kpi.changeType === 'positive' ? 'text-emerald-600 dark:text-emerald-400' : kpi.changeType === 'negative' ? 'text-red-600 dark:text-red-400' : 'text-slate-500'}`}>
            {kpi.changeType === 'positive' && <TrendingUp className="h-3.5 w-3.5" />}
            {kpi.changeType === 'negative' && <TrendingDown className="h-3.5 w-3.5" />}
            {kpi.change}
          </div>
          <span className="text-xs text-slate-500 dark:text-slate-400">
            {kpi.targetPercent}% of target
          </span>
        </div>

        {/* Progress Bar */}
        <div className="mt-3 h-1.5 bg-slate-100 dark:bg-slate-700 rounded-full overflow-hidden">
          <div
            className={`h-full bg-gradient-to-r ${colors.from} ${colors.to} rounded-full transition-all duration-500`}
            style={{ width: `${Math.min(kpi.targetPercent, 100)}%` }}
          />
        </div>
      </div>
    </button>
  )
}

export interface FinancialOverviewProps {
  kpis?: FinancialKPI[]
  onKPIClick?: (kpiId: string) => void
  className?: string
}

export function FinancialOverview({
  kpis = mockKPIs,
  onKPIClick,
  className,
}: FinancialOverviewProps) {
  return (
    <div className={className}>
      {/* Header */}
      <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-3 mb-5">
        <div>
          <h2 className="text-xl sm:text-2xl font-bold text-slate-900 dark:text-white">Financial Overview</h2>
          <p className="text-sm text-slate-500 dark:text-slate-400 mt-1">
            Real-time financial metrics across all regions
          </p>
        </div>
        <div className="flex items-center gap-2">
          <select className="text-sm border border-slate-300 dark:border-slate-600 rounded-lg px-3 py-1.5 bg-white dark:bg-slate-800 text-slate-900 dark:text-slate-100">
            <option>Q2 2026</option>
            <option>Q1 2026</option>
            <option>Q4 2025</option>
          </select>
          <select className="text-sm border border-slate-300 dark:border-slate-600 rounded-lg px-3 py-1.5 bg-white dark:bg-slate-800 text-slate-900 dark:text-slate-100">
            <option value="USD">USD $</option>
            <option value="EUR">EUR €</option>
            <option value="NGN">NGN ₦</option>
            <option value="KES">KES KSh</option>
            <option value="ZAR">ZAR R</option>
          </select>
        </div>
      </div>

      {/* KPI Cards Grid */}
      <div className="grid gap-3 sm:gap-4 grid-cols-2 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-5">
        {kpis.map((kpi) => (
          <FinancialKPICard
            key={kpi.id}
            kpi={kpi}
            onClick={() => onKPIClick?.(kpi.id)}
          />
        ))}
      </div>
    </div>
  )
}
