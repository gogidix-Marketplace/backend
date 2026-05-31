import { useEffect } from 'react'
import {
  DollarSign,
  TrendingUp,
  BarChart3,
  Wallet,
  Scale,
  Target,
  ArrowUpRight,
  ArrowDownRight,
  Sparkles,
  ExternalLink,
  Activity,
  Loader2,
} from 'lucide-react'
import { useCfoStore } from '@shared/store'
import { cn } from '@shared/utils'

const iconMap: Record<string, React.ElementType> = {
  DollarSign,
  TrendingUp,
  BarChart3,
  Wallet,
  Scale,
  Target,
}

export default function StrategicDashboardPage() {
  const {
    metrics,
    financialHealthScore,
    revenueTrend,
    strategicPriorities,
    aiInsights,
    isLoadingMetrics,
    loadStrategicData,
  } = useCfoStore()

  useEffect(() => {
    loadStrategicData()
  }, [loadStrategicData])

  if (isLoadingMetrics) {
    return (
      <div className="flex items-center justify-center h-96">
        <Loader2 className="w-8 h-8 text-indigo-500 animate-spin" />
      </div>
    )
  }

  const maxRevenue = Math.max(...revenueTrend.map((r) => r.value))

  return (
    <div className="space-y-6 max-w-7xl">
      <div>
        <h1 className="text-2xl font-bold text-gray-900">Strategic Dashboard</h1>
        <p className="text-sm text-gray-500 mt-1">Executive overview of Gogidix financial performance</p>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-5">
        {metrics.map((metric) => {
          const Icon = iconMap[metric.icon] || DollarSign
          const isPositive = metric.change > 0
          return (
            <div
              key={metric.label}
              className="bg-white rounded-xl border border-gray-200 p-6 hover:shadow-lg transition-shadow"
            >
              <div className="flex items-start justify-between mb-4">
                <div className="w-10 h-10 rounded-lg bg-indigo-50 flex items-center justify-center">
                  <Icon className="w-5 h-5 text-indigo-600" />
                </div>
                <div
                  className={cn(
                    'flex items-center gap-1 text-xs font-medium px-2 py-1 rounded-full',
                    isPositive
                      ? 'text-emerald-700 bg-emerald-50'
                      : 'text-red-700 bg-red-50'
                  )}
                >
                  {isPositive ? (
                    <ArrowUpRight className="w-3 h-3" />
                  ) : (
                    <ArrowDownRight className="w-3 h-3" />
                  )}
                  {Math.abs(metric.change)}%
                </div>
              </div>
              <p className="text-sm text-gray-500 mb-1">{metric.label}</p>
              <p className="text-2xl font-bold text-gray-900">{metric.value}</p>
              <p className="text-xs text-gray-400 mt-1">{metric.changeLabel}</p>
            </div>
          )
        })}
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-5">
        <div className="bg-white rounded-xl border border-gray-200 p-6">
          <h3 className="text-sm font-semibold text-gray-700 mb-4 flex items-center gap-2">
            <Activity className="w-4 h-4 text-indigo-600" />
            Financial Health Score
          </h3>
          <div className="flex items-center justify-center mb-4">
            <div className="relative w-40 h-40">
              <svg className="w-40 h-40 -rotate-90" viewBox="0 0 120 120">
                <circle cx="60" cy="60" r="52" fill="none" stroke="#E5E7EB" strokeWidth="10" />
                <circle
                  cx="60"
                  cy="60"
                  r="52"
                  fill="none"
                  stroke="#6366F1"
                  strokeWidth="10"
                  strokeLinecap="round"
                  strokeDasharray={`${(financialHealthScore / 100) * 327} 327`}
                />
              </svg>
              <div className="absolute inset-0 flex flex-col items-center justify-center">
                <span className="text-3xl font-bold text-gray-900">{financialHealthScore}</span>
                <span className="text-xs text-gray-500">out of 100</span>
              </div>
            </div>
          </div>
          <p className="text-xs text-center text-gray-500">
            Based on 24 financial indicators
          </p>
        </div>

        <div className="bg-white rounded-xl border border-gray-200 p-6">
          <h3 className="text-sm font-semibold text-gray-700 mb-4">Revenue Trend ($M)</h3>
          <div className="space-y-2">
            {revenueTrend.map((item) => (
              <div key={item.month} className="flex items-center gap-3">
                <span className="text-xs text-gray-500 w-8">{item.month}</span>
                <div className="flex-1 bg-gray-100 rounded-full h-4 overflow-hidden">
                  <div
                    className="h-full bg-gradient-to-r from-indigo-500 to-violet-500 rounded-full transition-all duration-500"
                    style={{ width: `${(item.value / maxRevenue) * 100}%` }}
                  />
                </div>
                <span className="text-xs font-medium text-gray-700 w-8 text-right">{item.value}</span>
              </div>
            ))}
          </div>
        </div>

        <div className="bg-white rounded-xl border border-gray-200 p-6">
          <h3 className="text-sm font-semibold text-gray-700 mb-4">Strategic Priorities</h3>
          <div className="space-y-4">
            {strategicPriorities.map((item) => (
              <div key={item.name}>
                <div className="flex items-center justify-between mb-1">
                  <span className="text-xs text-gray-600">{item.name}</span>
                  <span className="text-xs font-semibold text-indigo-600">{item.progress}%</span>
                </div>
                <div className="w-full bg-gray-100 rounded-full h-2">
                  <div
                    className={cn(
                      'h-full rounded-full transition-all duration-500',
                      item.progress >= 80
                        ? 'bg-emerald-500'
                        : item.progress >= 60
                        ? 'bg-indigo-500'
                        : 'bg-amber-500'
                    )}
                    style={{ width: `${item.progress}%` }}
                  />
                </div>
              </div>
            ))}
          </div>
        </div>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-2 gap-5">
        <div className="bg-gradient-to-br from-indigo-600 to-violet-700 rounded-xl p-6 text-white">
          <div className="flex items-center gap-2 mb-4">
            <Sparkles className="w-5 h-5" />
            <h3 className="text-sm font-semibold">AI Insights</h3>
          </div>
          <div className="space-y-3">
            {aiInsights.map((insight, i) => (
              <div key={i} className="flex gap-3 text-sm">
                <div className="w-1.5 h-1.5 rounded-full bg-indigo-200 mt-2 flex-shrink-0" />
                <p className="text-indigo-100">{insight}</p>
              </div>
            ))}
          </div>
        </div>

        <div className="bg-white rounded-xl border border-gray-200 p-6">
          <h3 className="text-sm font-semibold text-gray-700 mb-4">Quick Links</h3>
          <div className="grid grid-cols-1 gap-3">
            {[
              { label: 'HQ Finance Dashboard', url: 'http://localhost:3016', desc: 'Headquarters financial overview' },
              { label: 'Accountant Dashboard', url: 'http://localhost:3018', desc: 'Accounting & bookkeeping' },
              { label: 'Reports Dashboard', url: 'http://localhost:3020', desc: 'Financial reports & analytics' },
            ].map((link) => (
              <a
                key={link.url}
                href={link.url}
                target="_blank"
                rel="noopener noreferrer"
                className="flex items-center justify-between p-3 rounded-lg border border-gray-200 hover:border-indigo-300 hover:bg-indigo-50 transition-colors group"
              >
                <div>
                  <p className="text-sm font-medium text-gray-800">{link.label}</p>
                  <p className="text-xs text-gray-500">{link.desc}</p>
                </div>
                <ExternalLink className="w-4 h-4 text-gray-400 group-hover:text-indigo-600" />
              </a>
            ))}
          </div>
        </div>
      </div>
    </div>
  )
}
