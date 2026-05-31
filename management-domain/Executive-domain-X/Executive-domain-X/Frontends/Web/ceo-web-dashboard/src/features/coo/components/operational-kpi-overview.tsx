import { Card, CardContent } from '@shared/components/ui/card'
import { Badge } from '@shared/components/ui/badge'

/**
 * Operational KPI Overview for COO Dashboard
 */

export interface OperationalKPI {
  id: string
  name: string
  value: string
  change: string
  changeType: 'positive' | 'negative' | 'neutral'
  status: 'on_track' | 'at_risk' | 'behind' | 'ahead'
  targetPercent: number
  icon: React.ReactNode
  color: string
}

interface OperationalKPIOverviewProps {
  kpis: OperationalKPI[]
  onKPIClick?: (kpiId: string) => void
}

const defaultKPIs: OperationalKPI[] = [
  {
    id: 'efficiency',
    name: 'Operational Efficiency',
    value: '94.2%',
    change: '+2.3%',
    changeType: 'positive',
    status: 'on_track',
    targetPercent: 94,
    icon: (
      <svg className="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M13 10V3L4 14h7v7l9-11h-7z" />
      </svg>
    ),
    color: 'from-emerald-500 to-teal-500',
  },
  {
    id: 'quality',
    name: 'Quality Score',
    value: '96.8%',
    change: '+1.1%',
    changeType: 'positive',
    status: 'ahead',
    targetPercent: 97,
    icon: (
      <svg className="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 12l2 2 4-4M7.835 4.697a3.42 3.42 0 001.946-.806 3.42 3.42 0 014.438 0 3.42 3.42 0 001.946.806 3.42 3.42 0 013.138 3.138 3.42 3.42 0 00.806 1.946 3.42 3.42 0 010 4.438 3.42 3.42 0 00-.806 1.946 3.42 3.42 0 01-3.138 3.138 3.42 3.42 0 00-1.946.806 3.42 3.42 0 01-4.438 0 3.42 3.42 0 00-1.946-.806 3.42 3.42 0 01-3.138-3.138 3.42 3.42 0 00-.806-1.946 3.42 3.42 0 010-4.438 3.42 3.42 0 00.806-1.946 3.42 3.42 0 013.138-3.138z" />
      </svg>
    ),
    color: 'from-blue-500 to-indigo-500',
  },
  {
    id: 'throughput',
    name: 'Daily Throughput',
    value: '12.4K',
    change: '+8.7%',
    changeType: 'positive',
    status: 'ahead',
    targetPercent: 105,
    icon: (
      <svg className="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M4 4v5h.582m15.356 2A8.001 8.001 0 004.582 9m0 0H9m11 11v-5h-.581m0 0a8.003 8.003 0 01-15.357-2m15.357 2H15" />
      </svg>
    ),
    color: 'from-purple-500 to-violet-500',
  },
  {
    id: 'uptime',
    name: 'System Uptime',
    value: '99.9%',
    change: '+0.1%',
    changeType: 'positive',
    status: 'on_track',
    targetPercent: 100,
    icon: (
      <svg className="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M5 12h14M5 12a2 2 0 01-2-2V6a2 2 0 012-2h14a2 2 0 012 2v4a2 2 0 01-2 2M5 12a2 2 0 00-2 2v4a2 2 0 002 2h14a2 2 0 002-2v-4a2 2 0 00-2-2m-2-4h.01M17 16h.01" />
      </svg>
    ),
    color: 'from-amber-500 to-orange-500',
  },
  {
    id: 'response',
    name: 'Avg Response Time',
    value: '2.3h',
    change: '-15%',
    changeType: 'positive',
    status: 'on_track',
    targetPercent: 85,
    icon: (
      <svg className="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z" />
      </svg>
    ),
    color: 'from-rose-500 to-pink-500',
  },
]

export function OperationalKPIOverview({ kpis = defaultKPIs, onKPIClick }: OperationalKPIOverviewProps) {
  const getStatusColor = (status: OperationalKPI['status']) => {
    switch (status) {
      case 'on_track':
        return 'bg-emerald-100 dark:bg-emerald-900/30 text-emerald-700 dark:text-emerald-400 border-emerald-200 dark:border-emerald-800'
      case 'ahead':
        return 'bg-blue-100 dark:bg-blue-900/30 text-blue-700 dark:text-blue-400 border-blue-200 dark:border-blue-800'
      case 'at_risk':
        return 'bg-amber-100 dark:bg-amber-900/30 text-amber-700 dark:text-amber-400 border-amber-200 dark:border-amber-800'
      case 'behind':
        return 'bg-red-100 dark:bg-red-900/30 text-red-700 dark:text-red-400 border-red-200 dark:border-red-800'
    }
  }

  return (
    <div className="grid grid-cols-2 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-5 gap-3 sm:gap-4">
      {kpis.map((kpi) => (
        <Card
          key={kpi.id}
          className="group cursor-pointer hover:shadow-lg transition-all duration-300 border-l-4 overflow-hidden"
          onClick={() => onKPIClick?.(kpi.id)}
          style={{ borderLeftColor: kpi.color.split(' ')[1] }}
        >
          <CardContent className="p-4">
            {/* Icon with gradient background */}
            <div className={`flex h-10 w-10 items-center justify-center rounded-lg bg-gradient-to-br ${kpi.color} mb-3 group-hover:scale-110 transition-transform`}>
              <div className="text-white">{kpi.icon}</div>
            </div>

            {/* KPI Name */}
            <p className="text-xs text-slate-500 dark:text-slate-400 font-medium mb-1">
              {kpi.name}
            </p>

            {/* KPI Value */}
            <p className="text-xl sm:text-2xl font-bold text-slate-900 dark:text-white mb-1">
              {kpi.value}
            </p>

            {/* Progress Bar */}
            <div className="h-1.5 bg-slate-100 dark:bg-slate-800 rounded-full overflow-hidden mb-2">
              <div
                className={`h-full bg-gradient-to-r ${kpi.color} transition-all duration-500`}
                style={{ width: `${Math.min(100, kpi.targetPercent)}%` }}
              />
            </div>

            {/* Change and Status */}
            <div className="flex items-center justify-between">
              <span
                className={`text-xs font-medium ${
                  kpi.changeType === 'positive'
                    ? 'text-emerald-600 dark:text-emerald-400'
                    : kpi.changeType === 'negative'
                    ? 'text-red-600 dark:text-red-400'
                    : 'text-slate-500'
                }`}
              >
                {kpi.change}
              </span>
              <Badge variant="secondary" className={`text-[10px] px-1.5 py-0 ${getStatusColor(kpi.status)}`}>
                {kpi.status === 'on_track' && 'On Track'}
                {kpi.status === 'ahead' && 'Ahead'}
                {kpi.status === 'at_risk' && 'At Risk'}
                {kpi.status === 'behind' && 'Behind'}
              </Badge>
            </div>
          </CardContent>
        </Card>
      ))}
    </div>
  )
}
