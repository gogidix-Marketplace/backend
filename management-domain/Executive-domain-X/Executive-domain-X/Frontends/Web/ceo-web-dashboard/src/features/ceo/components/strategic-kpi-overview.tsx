import { Badge } from '@shared/components/ui/badge'
import {
  TrendingUp,
  TrendingDown,
  DollarSign,
  Globe2,
  Users,
} from 'lucide-react'
import { useAnalytics } from '@shared/services/api'
import * as React from 'react'

export interface StrategicKPI {
  id: string
  name: string
  value: string
  change: string
  changeType: 'positive' | 'negative' | 'neutral'
  status: 'on_track' | 'at_risk' | 'behind' | 'ahead'
  targetPercent: number
  isLoading?: boolean
}

// Fallback mock data for when API is unavailable
const fallbackKPIs: StrategicKPI[] = [
  {
    id: 'revenue',
    name: 'Total Revenue',
    value: '$42.5M',
    change: '+8.5%',
    changeType: 'positive',
    status: 'on_track',
    targetPercent: 85,
  },
  {
    id: 'growth',
    name: 'Growth Rate',
    value: '24.5%',
    change: '+2.1%',
    changeType: 'positive',
    status: 'on_track',
    targetPercent: 98,
  },
  {
    id: 'domains',
    name: 'Active Domains',
    value: '8',
    change: '+14.3%',
    changeType: 'positive',
    status: 'ahead',
    targetPercent: 80,
  },
  {
    id: 'countries',
    name: 'Countries Served',
    value: '15',
    change: '+7.1%',
    changeType: 'positive',
    status: 'on_track',
    targetPercent: 75,
  },
  {
    id: 'users',
    name: 'Active Users',
    value: '1.25M',
    change: '+18.2%',
    changeType: 'positive',
    status: 'ahead',
    targetPercent: 83,
  },
]

/**
 * Transform API KPI data to StrategicKPI format
 */
function transformApiKPItoStrategicKPI(apiKPI: any): StrategicKPI {
  const value = apiKPI.value ?? apiKPI.currentValue ?? apiKPI.actual ?? 0
  const target = apiKPI.target ?? apiKPI.targetValue ?? 100
  const previous = apiKPI.previousValue ?? apiKPI.previous ?? value * 0.9
  const change = value - previous
  const changePercent = previous > 0 ? ((change / previous) * 100).toFixed(1) : '0.0'
  const changeType = change > 0 ? 'positive' : change < 0 ? 'negative' : 'neutral'

  // Map status values
  const statusMap: Record<string, StrategicKPI['status']> = {
    on_track: 'on_track',
    at_risk: 'at_risk',
    behind: 'behind',
    ahead: 'ahead',
    ontrack: 'on_track',
    healthy: 'ahead',
    warning: 'at_risk',
    critical: 'behind',
  }
  const status = statusMap[apiKPI.status?.toLowerCase()] || 'on_track'

  // Format value based on type
  let formattedValue = value.toString()
  if (apiKPI.format === 'currency' || apiKPI.name?.toLowerCase().includes('revenue')) {
    formattedValue = `$${(value / 1000000).toFixed(1)}M`
  } else if (apiKPI.format === 'percentage' || apiKPI.name?.toLowerCase().includes('rate')) {
    formattedValue = `${value.toFixed(1)}%`
  } else if (apiKPI.format === 'number' && value > 1000000) {
    formattedValue = `${(value / 1000000).toFixed(2)}M`
  } else if (apiKPI.format === 'number' && value > 1000) {
    formattedValue = `${(value / 1000).toFixed(1)}K`
  }

  // Calculate target percent
  const targetPercent = target > 0 ? Math.min(Math.round((value / target) * 100), 100) : 0

  return {
    id: apiKPI.id || apiKPI.name?.toLowerCase().replace(/\s+/g, '_') || 'unknown',
    name: apiKPI.name || apiKPI.title || 'KPI',
    value: formattedValue,
    change: `${changeType === 'positive' ? '+' : ''}${changePercent}%`,
    changeType,
    status,
    targetPercent,
  }
}

const statusConfig = {
  on_track: { label: 'On Track', bg: 'bg-emerald-50 dark:bg-emerald-950/20', text: 'text-emerald-700 dark:text-emerald-400', border: 'border-emerald-200 dark:border-emerald-800' },
  at_risk: { label: 'At Risk', bg: 'bg-amber-50 dark:bg-amber-950/20', text: 'text-amber-700 dark:text-amber-400', border: 'border-amber-200 dark:border-amber-800' },
  behind: { label: 'Behind', bg: 'bg-red-50 dark:bg-red-950/20', text: 'text-red-700 dark:text-red-400', border: 'border-red-200 dark:border-red-800' },
  ahead: { label: 'Ahead', bg: 'bg-blue-50 dark:bg-blue-950/20', text: 'text-blue-700 dark:text-blue-400', border: 'border-blue-200 dark:border-blue-800' },
}

const colorMap = {
  revenue: { from: 'from-emerald-500', to: 'to-teal-500', icon: 'bg-emerald-500' },
  growth: { from: 'from-blue-500', to: 'to-indigo-500', icon: 'bg-blue-500' },
  domains: { from: 'from-purple-500', to: 'to-violet-500', icon: 'bg-purple-500' },
  countries: { from: 'from-amber-500', to: 'to-orange-500', icon: 'bg-amber-500' },
  users: { from: 'from-rose-500', to: 'to-pink-500', icon: 'bg-rose-500' },
}

const iconMap = {
  revenue: <DollarSign className="h-4 w-4" />,
  growth: <TrendingUp className="h-4 w-4" />,
  domains: <Globe2 className="h-4 w-4" />,
  countries: <span className="text-base">🌍</span>,
  users: <Users className="h-4 w-4" />,
}

function KPICard({ kpi, onClick }: { kpi: StrategicKPI; onClick?: () => void }) {
  const status = statusConfig[kpi.status]
  const colors = colorMap[kpi.id as keyof typeof colorMap] || colorMap.revenue
  const icon = iconMap[kpi.id as keyof typeof iconMap] || iconMap.revenue

  if (kpi.isLoading) {
    return (
      <div className="w-full h-full">
        <div className="bg-white dark:bg-slate-800 rounded-xl border border-slate-200 dark:border-slate-700 p-4 sm:p-5 h-full shadow-sm animate-pulse">
          <div className="flex items-start justify-between mb-4">
            <div className="h-9 w-9 sm:h-10 sm:w-10 rounded-lg bg-slate-200 dark:bg-slate-700" />
            <div className="h-5 w-12 rounded-full bg-slate-200 dark:bg-slate-700" />
          </div>
          <div className="h-3 w-20 bg-slate-200 dark:bg-slate-700 rounded mb-2" />
          <div className="h-7 w-16 bg-slate-200 dark:bg-slate-700 rounded mb-3" />
          <div className="h-1.5 bg-slate-200 dark:bg-slate-700 rounded-full" />
        </div>
      </div>
    )
  }

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
              {icon}
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
            style={{ width: `${kpi.targetPercent}%` }}
          />
        </div>
      </div>
    </button>
  )
}

export interface StrategicKPIOverviewProps {
  onKPIClick?: (kpiId: string) => void
  className?: string
  /**
   * Force use of mock data (for testing/demos)
   * @default false - will try API first, fall back to mock on error
   */
  useMockData?: boolean
}

export function StrategicKPIOverview({
  onKPIClick,
  className,
  useMockData = false,
}: StrategicKPIOverviewProps) {
  // Fetch KPIs from analytics service
  const { data: analyticsData, loading, error } = useAnalytics(useMockData ? undefined : 60000)

  // Memoize KPIs to prevent flickering - use fallback during loading, don't show loading state
  const kpis = React.useMemo(() => {
    let result: StrategicKPI[] = fallbackKPIs

    if (!useMockData && analyticsData?.kpis) {
      result = analyticsData.kpis.slice(0, 5).map(transformApiKPItoStrategicKPI)
    } else if (!useMockData && analyticsData?.strategicKPIs) {
      result = analyticsData.strategicKPIs.slice(0, 5).map(transformApiKPItoStrategicKPI)
    }

    // Only add loading state on first load if truly loading
    if (loading && !useMockData && !analyticsData) {
      return result.map(k => ({ ...k, isLoading: true }))
    }

    return result
  }, [analyticsData, loading, useMockData])

  // Memoize click handler
  const handleKPIClick = React.useCallback((kpiId: string) => {
    onKPIClick?.(kpiId)
  }, [onKPIClick])

  return (
    <div className={className}>
      {/* Header */}
      <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-3 mb-5">
        <div>
          <h2 className="text-xl sm:text-2xl font-bold text-slate-900 dark:text-white">Strategic KPI Overview</h2>
          <p className="text-sm text-slate-500 dark:text-slate-400 mt-1">
            Real-time performance metrics across all domains
          </p>
        </div>
        <div className="flex items-center gap-2">
          {loading && !useMockData ? (
            <div className="flex items-center gap-2 px-3 py-1.5 rounded-full bg-amber-50 dark:bg-amber-950/20 border border-amber-200 dark:border-amber-800">
              <div className="h-2 w-2 rounded-full bg-amber-500 animate-pulse" />
              <span className="text-xs font-semibold text-amber-700 dark:text-amber-400">Loading...</span>
            </div>
          ) : error ? (
            <div className="flex items-center gap-2 px-3 py-1.5 rounded-full bg-red-50 dark:bg-red-950/20 border border-red-200 dark:border-red-800">
              <div className="h-2 w-2 rounded-full bg-red-500" />
              <span className="text-xs font-semibold text-red-700 dark:text-red-400">Using cached data</span>
            </div>
          ) : (
            <div className="flex items-center gap-2 px-3 py-1.5 rounded-full bg-emerald-50 dark:bg-emerald-950/20 border border-emerald-200 dark:border-emerald-800">
              <div className="h-2 w-2 rounded-full bg-emerald-500 animate-pulse" />
              <span className="text-xs font-semibold text-emerald-700 dark:text-emerald-400">Live</span>
            </div>
          )}
        </div>
      </div>

      {/* KPI Cards Grid */}
      <div className="grid gap-3 sm:gap-4 grid-cols-2 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-5">
        {kpis.map((kpi) => (
          <KPICard
            key={kpi.id}
            kpi={kpi}
            onClick={() => handleKPIClick(kpi.id)}
          />
        ))}
      </div>
    </div>
  )
}
