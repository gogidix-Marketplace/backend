import { Card, CardContent } from '@shared/components/ui/card'
import { Badge } from '@shared/components/ui/badge'

/**
 * Technical KPI Overview for CTO Dashboard
 */

export interface TechnicalKPI {
  id: string
  name: string
  value: string
  change: string
  changeType: 'positive' | 'negative' | 'neutral'
  status: 'healthy' | 'warning' | 'critical' | 'optimal'
  targetPercent: number
  icon: React.ReactNode
  color: string
}

interface TechnicalKPIOverviewProps {
  kpis?: TechnicalKPI[]
  onKPIClick?: (kpiId: string) => void
}

const defaultKPIs: TechnicalKPI[] = [
  {
    id: 'uptime',
    name: 'System Uptime',
    value: '99.95%',
    change: '+0.02%',
    changeType: 'positive',
    status: 'optimal',
    targetPercent: 100,
    icon: (
      <svg className="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M5 12h14M5 12a2 2 0 01-2-2V6a2 2 0 012-2h14a2 2 0 012 2v4a2 2 0 01-2 2M5 12a2 2 0 00-2 2v4a2 2 0 002 2h14a2 2 0 002-2v-4a2 2 0 00-2-2m-2-4h.01M17 16h.01" />
      </svg>
    ),
    color: 'from-emerald-500 to-teal-500',
  },
  {
    id: 'latency',
    name: 'Avg Latency',
    value: '45ms',
    change: '-12%',
    changeType: 'positive',
    status: 'optimal',
    targetPercent: 90,
    icon: (
      <svg className="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M13 10V3L4 14h7v7l9-11h-7z" />
      </svg>
    ),
    color: 'from-blue-500 to-indigo-500',
  },
  {
    id: 'errors',
    name: 'Error Rate',
    value: '0.02%',
    change: '-35%',
    changeType: 'positive',
    status: 'healthy',
    targetPercent: 98,
    icon: (
      <svg className="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.532-3L5.667 3H4a2 2 0 00-2 2v12c0 1.1.9 2 2 2h12a2 2 0 002-2V5a2 2 0 00-2-2h-1.464a1 1 0 01-.768-.364z" />
      </svg>
    ),
    color: 'from-purple-500 to-violet-500',
  },
  {
    id: 'deployments',
    name: 'Deployments',
    value: '147',
    change: '+18',
    changeType: 'positive',
    status: 'healthy',
    targetPercent: 85,
    icon: (
      <svg className="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M7 16a4 4 0 01-.88-7.903A5 5 0 1115.9 6L16 6a5 5 0 011 9.9M15 13l-3-3m0 0l-3 3m3-3v12" />
      </svg>
    ),
    color: 'from-amber-500 to-orange-500',
  },
  {
    id: 'coverage',
    name: 'Test Coverage',
    value: '87.3%',
    change: '+2.4%',
    changeType: 'positive',
    status: 'healthy',
    targetPercent: 87,
    icon: (
      <svg className="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z" />
      </svg>
    ),
    color: 'from-rose-500 to-pink-500',
  },
]

export function TechnicalKPIOverview({ kpis = defaultKPIs, onKPIClick }: TechnicalKPIOverviewProps) {
  const getStatusColor = (status: TechnicalKPI['status']) => {
    switch (status) {
      case 'healthy':
        return 'bg-emerald-100 dark:bg-emerald-900/30 text-emerald-700 dark:text-emerald-400 border-emerald-200 dark:border-emerald-800'
      case 'optimal':
        return 'bg-blue-100 dark:bg-blue-900/30 text-blue-700 dark:text-blue-400 border-blue-200 dark:border-blue-800'
      case 'warning':
        return 'bg-amber-100 dark:bg-amber-900/30 text-amber-700 dark:text-amber-400 border-amber-200 dark:border-amber-800'
      case 'critical':
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
                {kpi.status === 'healthy' && 'Healthy'}
                {kpi.status === 'optimal' && 'Optimal'}
                {kpi.status === 'warning' && 'Warning'}
                {kpi.status === 'critical' && 'Critical'}
              </Badge>
            </div>
          </CardContent>
        </Card>
      ))}
    </div>
  )
}
