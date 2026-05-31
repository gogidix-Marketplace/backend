import { useEffect } from 'react'
import { useNavigate } from 'react-router-dom'
import { useFinanceStore } from '@store'
import { cn } from '@shared/utils/cn'
import {
  TrendingUp,
  TrendingDown,
  DollarSign,
  CreditCard,
  Wallet,
  Landmark,
  AlertTriangle,
  Brain,
  ArrowUpRight,
  CheckCircle,
  Clock,
  BarChart3,
} from 'lucide-react'

const COUNTRY_PERFORMANCE = [
  { name: 'Nigeria', flag: '🇳🇬', revenue: 8200000, growth: 12, healthScore: 94, status: 'on_track' },
  { name: 'Kenya', flag: '🇰🇪', revenue: 5100000, growth: 8, healthScore: 91, status: 'on_track' },
  { name: 'South Africa', flag: '🇿🇦', revenue: 4800000, growth: -3, healthScore: 78, status: 'at_risk' },
  { name: 'Ghana', flag: '🇬🇭', revenue: 3200000, growth: 15, healthScore: 96, status: 'on_track' },
  { name: 'Ireland', flag: '🇮🇪', revenue: 2900000, growth: 5, healthScore: 89, status: 'on_track' },
]

const PENDING_APPROVALS = [
  { id: 'BUD-0217-001', title: 'Nigeria Q2 Budget', amount: 2100000, country: '🇳🇬', priority: 'HIGH', due: 'Feb 20' },
  { id: 'BUD-0217-002', title: 'Kenya Marketing Expansion', amount: 500000, country: '🇰🇪', priority: 'MEDIUM', due: 'Feb 25' },
  { id: 'BUD-0217-003', title: 'SA Operations Increase', amount: 750000, country: '🇿🇦', priority: 'LOW', due: 'Mar 01' },
]

const AI_INSIGHTS = [
  { text: 'Revenue forecast: +13.4% next month', type: 'positive' },
  { text: 'Nigeria expansion opportunity identified', type: 'positive' },
  { text: 'Cash flow warning for Week 3', type: 'warning' },
  { text: 'S.Africa budget variance exceeds 20%', type: 'negative' },
]

const ALERTS = [
  { text: 'Budget approval required for Nigeria Q2', type: 'warning' },
  { text: 'Fraud alert flagged in Kenya operations', type: 'error' },
  { text: 'Tax report ready for review', type: 'info' },
]

function formatCurrency(value: number, currency: string = 'USD'): string {
  if (value >= 1000000) return `$${(value / 1000000).toFixed(1)}M`
  if (value >= 1000) return `$${(value / 1000).toFixed(0)}K`
  return `$${value.toLocaleString()}`
}

export function FinanceDashboardPage() {
  const { dashboardData, dashboardLoading, loadDashboardData } = useFinanceStore()
  const navigate = useNavigate()

  useEffect(() => {
    loadDashboardData()
  }, [loadDashboardData])

  if (dashboardLoading || !dashboardData) {
    return (
      <div className="flex items-center justify-center h-64">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600" />
      </div>
    )
  }

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-2xl font-bold text-slate-900">Global Overview</h1>
          <p className="text-slate-500">HQ Finance Dashboard - All countries</p>
        </div>
        <div className="flex items-center gap-3">
          <select className="px-3 py-2 border border-slate-200 rounded-lg text-sm">
            <option>February 2026</option>
            <option>January 2026</option>
            <option>Q1 2026</option>
            <option>YTD 2026</option>
          </select>
          <select className="px-3 py-2 border border-slate-200 rounded-lg text-sm">
            <option>USD</option>
            <option>EUR</option>
            <option>GBP</option>
          </select>
        </div>
      </div>

      <div className="bg-white rounded-xl border border-slate-200 p-6">
        <div className="flex items-center justify-between">
          <div className="flex items-center gap-6">
            <div className="text-center">
              <div className="w-20 h-20 rounded-full border-4 border-emerald-500 flex items-center justify-center">
                <span className="text-2xl font-bold text-emerald-600">87</span>
              </div>
              <p className="text-xs text-slate-500 mt-1">Health Score</p>
            </div>
            <div>
              <p className="text-sm text-slate-500">Global Financial Health</p>
              <p className="text-emerald-600 text-sm font-medium mt-1 flex items-center gap-1">
                <TrendingUp size={14} />
                +5 points from last month
              </p>
              <p className="text-xs text-slate-400 mt-1">All KPIs on track · 2 alerts requiring review</p>
            </div>
          </div>
          <button className="text-sm text-blue-600 hover:text-blue-700 font-medium">View Details</button>
        </div>
      </div>

      <div className="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-6 gap-4">
        {[
          { label: 'Total Revenue', value: '$42.5M', trend: '+8.2%', trendUp: true, status: 'on_track', icon: DollarSign, color: 'blue' },
          { label: 'Total Expenses', value: '$32.1M', trend: '-3.1%', trendUp: false, status: 'on_track', icon: CreditCard, color: 'amber' },
          { label: 'Net Income', value: '$10.4M', trend: '+12.5%', trendUp: true, status: 'on_track', icon: Wallet, color: 'emerald' },
          { label: 'Total Cash', value: '$15.2M', trend: '94% utilized', trendUp: true, status: 'on_track', icon: Landmark, color: 'blue' },
          { label: 'Total Debt', value: '$5.1M', trend: 'Within limits', trendUp: true, status: 'on_track', icon: CreditCard, color: 'purple' },
          { label: 'Alerts', value: '3', trend: 'Pending', trendUp: false, status: 'warning', icon: AlertTriangle, color: 'amber' },
        ].map((kpi) => {
          const Icon = kpi.icon
          const colorMap: Record<string, string> = {
            blue: 'bg-blue-100 text-blue-600',
            amber: 'bg-amber-100 text-amber-600',
            emerald: 'bg-emerald-100 text-emerald-600',
            purple: 'bg-purple-100 text-purple-600',
          }
          return (
            <div
              key={kpi.label}
              className="bg-white rounded-xl border border-slate-200 p-4 hover:shadow-md transition-shadow cursor-pointer"
              onClick={() => {
                if (kpi.label === 'Total Revenue') navigate('/revenue')
                else if (kpi.label === 'Total Cash') navigate('/treasury')
                else if (kpi.label === 'Alerts') navigate('/tax-compliance')
              }}
            >
              <div className="flex items-center justify-between mb-2">
                <div className={cn('w-8 h-8 rounded-lg flex items-center justify-center', colorMap[kpi.color])}>
                  <Icon size={16} />
                </div>
                <span className={cn(
                  'w-2 h-2 rounded-full',
                  kpi.status === 'on_track' ? 'bg-emerald-500' : 'bg-amber-500'
                )} />
              </div>
              <p className="text-lg font-bold text-slate-900">{kpi.value}</p>
              <p className="text-xs text-slate-500 mt-1">{kpi.label}</p>
              <p className={cn(
                'text-xs font-medium mt-1 flex items-center gap-1',
                kpi.trendUp ? 'text-emerald-600' : 'text-amber-600'
              )}>
                {kpi.trendUp && <TrendingUp size={10} />}
                {!kpi.trendUp && kpi.label === 'Total Expenses' && <TrendingDown size={10} />}
                {kpi.trend}
              </p>
            </div>
          )
        })}
      </div>

      <div className="bg-white rounded-xl border border-slate-200 p-6">
        <div className="flex items-center justify-between mb-4">
          <h3 className="font-semibold text-slate-900">Country Financial Performance</h3>
          <button
            onClick={() => navigate('/countries')}
            className="text-sm text-blue-600 hover:text-blue-700 font-medium flex items-center gap-1"
          >
            View All Countries <ArrowUpRight size={14} />
          </button>
        </div>
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-5 gap-3">
          {COUNTRY_PERFORMANCE.map((country) => (
            <div
              key={country.name}
              className="border border-slate-200 rounded-lg p-3 hover:shadow-sm transition-shadow cursor-pointer"
              onClick={() => navigate('/countries')}
            >
              <div className="flex items-center justify-between mb-2">
                <div className="flex items-center gap-1.5">
                  <span className="text-lg">{country.flag}</span>
                  <span className="text-sm font-medium text-slate-900">{country.name}</span>
                </div>
                <span className={cn(
                  'w-2 h-2 rounded-full',
                  country.status === 'on_track' ? 'bg-emerald-500' : 'bg-amber-500'
                )} />
              </div>
              <p className="text-lg font-bold text-slate-900">{formatCurrency(country.revenue)}</p>
              <div className="flex items-center justify-between mt-1">
                <span className={cn('text-xs font-medium flex items-center gap-1',
                  country.growth >= 0 ? 'text-emerald-600' : 'text-red-600'
                )}>
                  {country.growth >= 0 ? <TrendingUp size={10} /> : <TrendingDown size={10} />}
                  {country.growth >= 0 ? '+' : ''}{country.growth}%
                </span>
                <span className="text-xs text-slate-500">{country.healthScore}%</span>
              </div>
            </div>
          ))}
        </div>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        <div className="bg-white rounded-xl border border-slate-200 p-6">
          <h3 className="font-semibold text-slate-900 mb-4">Consolidated Revenue Trend</h3>
          <div className="space-y-3">
            {[
              { month: 'Oct 2025', value: 37500000 },
              { month: 'Nov 2025', value: 39000000 },
              { month: 'Dec 2025', value: 40500000 },
              { month: 'Jan 2026', value: 41500000 },
              { month: 'Feb 2026', value: 42500000 },
            ].map(item => (
              <div key={item.month} className="flex items-center gap-3">
                <span className="text-xs text-slate-500 w-20">{item.month}</span>
                <div className="flex-1 bg-slate-100 rounded-full h-4">
                  <div
                    className="bg-blue-500 h-4 rounded-full"
                    style={{ width: `${(item.value / 50000000) * 100}%` }}
                  />
                </div>
                <span className="text-xs font-medium text-slate-600 w-16 text-right">{formatCurrency(item.value)}</span>
              </div>
            ))}
          </div>
        </div>

        <div className="space-y-6">
          <div className="bg-white rounded-xl border border-slate-200 p-6">
            <div className="flex items-center justify-between mb-3">
              <h3 className="font-semibold text-slate-900 flex items-center gap-2">
                <Brain size={18} className="text-purple-500" />
                AI Forecasts
              </h3>
              <button className="text-xs text-blue-600 font-medium">View Details</button>
            </div>
            <div className="bg-purple-50 rounded-lg p-3 mb-3">
              <p className="text-sm font-medium text-purple-900">March 2026 Forecast: $48.2M</p>
              <div className="flex items-center gap-2 mt-1">
                <TrendingUp size={14} className="text-purple-600" />
                <span className="text-xs text-purple-700 font-medium">+13.4% growth (95% confidence)</span>
              </div>
            </div>
            <div className="space-y-2">
              {AI_INSIGHTS.map((insight, i) => (
                <div key={i} className="flex items-start gap-2">
                  <span className={cn('w-1.5 h-1.5 rounded-full mt-1.5',
                    insight.type === 'positive' ? 'bg-emerald-500' :
                    insight.type === 'warning' ? 'bg-amber-500' : 'bg-red-500'
                  )} />
                  <span className="text-xs text-slate-600">{insight.text}</span>
                </div>
              ))}
            </div>
          </div>
        </div>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        <div className="bg-white rounded-xl border border-slate-200 p-6">
          <div className="flex items-center justify-between mb-4">
            <h3 className="font-semibold text-slate-900">Pending Approvals</h3>
            <span className="text-sm text-blue-600 font-medium">{PENDING_APPROVALS.length} pending</span>
          </div>
          <div className="space-y-3">
            {PENDING_APPROVALS.map((approval) => (
              <div key={approval.id} className="flex items-center justify-between p-3 rounded-lg border border-slate-100 hover:bg-slate-50">
                <div className="flex items-center gap-3">
                  <span className="text-lg">{approval.country}</span>
                  <div>
                    <p className="text-sm font-medium text-slate-900">{approval.title}</p>
                    <p className="text-xs text-slate-500">{approval.id} · Due: {approval.due}</p>
                  </div>
                </div>
                <div className="flex items-center gap-3">
                  <span className="text-sm font-semibold text-slate-900">{formatCurrency(approval.amount)}</span>
                  <span className={cn('text-xs font-medium px-2 py-0.5 rounded-full',
                    approval.priority === 'HIGH' ? 'bg-red-100 text-red-700' :
                    approval.priority === 'MEDIUM' ? 'bg-amber-100 text-amber-700' :
                    'bg-slate-100 text-slate-600'
                  )}>
                    {approval.priority}
                  </span>
                  <button className="text-blue-600 hover:text-blue-700 text-xs font-medium">Review</button>
                </div>
              </div>
            ))}
          </div>
          <button className="mt-3 w-full py-2 text-sm text-blue-600 hover:bg-blue-50 rounded-lg transition-colors font-medium">
            View All Approvals
          </button>
        </div>

        <div className="bg-white rounded-xl border border-slate-200 p-6">
          <div className="flex items-center justify-between mb-4">
            <h3 className="font-semibold text-slate-900">Executive Alerts</h3>
            <span className="text-sm text-blue-600 font-medium">View All</span>
          </div>
          <div className="space-y-3">
            {ALERTS.map((alert, i) => (
              <div key={i} className={cn(
                'flex items-start gap-3 p-3 rounded-lg border',
                alert.type === 'warning' && 'bg-amber-50 border-amber-200',
                alert.type === 'error' && 'bg-red-50 border-red-200',
                alert.type === 'info' && 'bg-blue-50 border-blue-200'
              )}>
                {alert.type === 'warning' && <AlertTriangle size={16} className="text-amber-600 mt-0.5" />}
                {alert.type === 'error' && <AlertTriangle size={16} className="text-red-600 mt-0.5" />}
                {alert.type === 'info' && <CheckCircle size={16} className="text-blue-600 mt-0.5" />}
                <div className="flex-1">
                  <p className="text-sm text-slate-900">{alert.text}</p>
                </div>
                <button className="text-xs text-blue-600 font-medium whitespace-nowrap">View</button>
              </div>
            ))}
          </div>

          <div className="mt-4 pt-4 border-t border-slate-200">
            <div className="flex items-center justify-between mb-2">
              <span className="text-sm font-medium text-slate-700">Budget Utilization</span>
              <span className="text-sm font-semibold text-slate-900">72.5%</span>
            </div>
            <div className="w-full bg-slate-200 rounded-full h-2.5">
              <div className="bg-emerald-500 h-2.5 rounded-full" style={{ width: '72.5%' }} />
            </div>
            <p className="text-xs text-slate-500 mt-1">Overall budget used this period</p>
          </div>
        </div>
      </div>
    </div>
  )
}
