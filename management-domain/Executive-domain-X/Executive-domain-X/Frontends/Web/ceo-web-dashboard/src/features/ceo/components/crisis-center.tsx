import { Card, CardContent, CardHeader, CardTitle } from '@shared/components/ui/card'
import { Badge } from '@shared/components/ui/badge'
import { Button } from '@shared/components/ui/button'
import { cn } from '@shared/utils/cn'
import { CheckCircle2, AlertTriangle, AlertOctagon } from 'lucide-react'
import * as React from 'react'
import { useRequiringAttention } from '@shared/services/api'

export type AlertSeverity = 'critical' | 'warning' | 'info'
export type AlertStatus = 'active' | 'investigating' | 'resolved' | 'dismissed'

export interface CrisisAlert {
  id: string
  title: string
  description: string
  severity: AlertSeverity
  status: AlertStatus
  domain: string
  region?: string
  affectedKPIs: string[]
  createdAt: Date
  updatedAt: Date
  assignee?: string
}

// Fallback mock data for when API is unavailable
const fallbackAlerts: CrisisAlert[] = [
  {
    id: '1',
    title: 'European Region Revenue Decline',
    description: 'European region showing 12% decline in Q2 projections.',
    severity: 'critical',
    status: 'active',
    domain: 'Public',
    region: 'Europe',
    affectedKPIs: ['Revenue', 'Growth'],
    createdAt: new Date(Date.now() - 2 * 60 * 60 * 1000),
    updatedAt: new Date(Date.now() - 30 * 60 * 1000),
  },
  {
    id: '2',
    title: 'Foundation Domain API Latency',
    description: 'API response times increased by 200ms following deployment.',
    severity: 'warning',
    status: 'investigating',
    domain: 'Foundation',
    affectedKPIs: ['Performance'],
    createdAt: new Date(Date.now() - 4 * 60 * 60 * 1000),
    updatedAt: new Date(Date.now() - 1 * 60 * 60 * 1000),
    assignee: 'CTO Office',
  },
  {
    id: '3',
    title: 'New Market Expansion Opportunity',
    description: 'Analysis indicates favorable conditions for South Africa expansion.',
    severity: 'info',
    status: 'active',
    domain: 'Business',
    region: 'Africa',
    affectedKPIs: ['Growth', 'Countries'],
    createdAt: new Date(Date.now() - 6 * 60 * 60 * 1000),
    updatedAt: new Date(Date.now() - 6 * 60 * 60 * 1000),
  },
]

const severityConfig = {
  critical: {
    icon: AlertOctagon,
    borderColor: 'border-red-500',
    bgLight: 'bg-red-50 dark:bg-red-950/20',
    textColor: 'text-red-700 dark:text-red-400',
    pulse: true,
  },
  warning: {
    icon: AlertTriangle,
    borderColor: 'border-amber-500',
    bgLight: 'bg-amber-50 dark:bg-amber-950/20',
    textColor: 'text-amber-700 dark:text-amber-400',
    pulse: false,
  },
  info: {
    icon: CheckCircle2,
    borderColor: 'border-blue-500',
    bgLight: 'bg-blue-50 dark:bg-blue-950/20',
    textColor: 'text-blue-700 dark:text-blue-400',
    pulse: false,
  },
}

/**
 * Transform API KPI/alert data to CrisisAlert format
 */
function transformApiAlert(apiKPI: any): CrisisAlert {
  const value = apiKPI.value ?? apiKPI.currentValue ?? 0
  const target = apiKPI.target ?? value * 1.1
  const variance = ((value - target) / target) * 100

  // Determine severity based on variance and status
  let severity: AlertSeverity = 'info'
  if (apiKPI.status === 'behind' || apiKPI.status === 'critical') {
    severity = variance < -10 ? 'critical' : 'warning'
  } else if (apiKPI.status === 'at_risk') {
    severity = 'warning'
  }

  // Map status
  const statusMap: Record<string, AlertStatus> = {
    active: 'active',
    investigating: 'investigating',
    resolved: 'resolved',
    dismissed: 'dismissed',
    on_track: 'active',
    at_risk: 'active',
    behind: 'active',
    ahead: 'active',
  }

  return {
    id: apiKPI.id || crypto.randomUUID?.() || Math.random().toString(36),
    title: apiKPI.title || apiKPI.name || 'Alert',
    description: apiKPI.description || apiKPI.message || `KPI ${apiKPI.status}: ${variance.toFixed(1)}% variance from target.`,
    severity,
    status: statusMap[apiKPI.status?.toLowerCase()] || 'active',
    domain: apiKPI.domain || apiKPI.category || 'Unknown',
    region: apiKPI.region,
    affectedKPIs: apiKPI.kpis || apiKPI.affectedMetrics || [apiKPI.name || 'KPI'],
    createdAt: apiKPI.createdAt ? new Date(apiKPI.createdAt) : new Date(),
    updatedAt: apiKPI.updatedAt ? new Date(apiKPI.updatedAt) : new Date(),
    assignee: apiKPI.assignee || apiKPI.owner,
  }
}

export interface CrisisManagementCenterProps {
  onDismiss?: (id: string) => void
  maxVisible?: number
  className?: string
  /**
   * Force use of mock data (for testing/demos)
   * @default false - will try API first, fall back to mock on error
   */
  useMockData?: boolean
}

export function CrisisManagementCenter({
  onDismiss,
  maxVisible = 5,
  className,
  useMockData = false,
}: CrisisManagementCenterProps) {
  // Fetch KPIs requiring attention from API
  const { alerts, loading } = useRequiringAttention(useMockData ? undefined : 60000)

  // Memoize alerts data to prevent flickering
  const alertsData = React.useMemo(() => {
    if (!useMockData && alerts.length > 0) {
      // Transform KPIs requiring attention to alerts
      return alerts
        .filter((kpi: any) => kpi.status === 'behind' || kpi.status === 'at_risk' || kpi.status === 'critical')
        .slice(0, maxVisible)
        .map(transformApiAlert)
    }
    return fallbackAlerts
  }, [alerts, useMockData, maxVisible])

  // Memoize computed values
  const visibleAlerts = React.useMemo(() => alertsData.slice(0, maxVisible), [alertsData, maxVisible])
  const activeCount = React.useMemo(() => alertsData.filter((a) => a.status === 'active').length, [alertsData])
  const criticalCount = React.useMemo(() => alertsData.filter((a) => a.severity === 'critical').length, [alertsData])

  const [expandedAlert, setExpandedAlert] = React.useState<string | null>(
    fallbackAlerts.find((a) => a.severity === 'critical')?.id || null
  )

  // Update expanded alert when alerts data changes
  React.useEffect(() => {
    const criticalAlert = alertsData.find((a) => a.severity === 'critical')
    if (criticalAlert && !expandedAlert) {
      setExpandedAlert(criticalAlert.id)
    }
  }, [alertsData, expandedAlert])

  const formatTimeAgo = (date: Date) => {
    const seconds = Math.floor((Date.now() - date.getTime()) / 1000)
    if (seconds < 60) return `${seconds}s ago`
    const minutes = Math.floor(seconds / 60)
    if (minutes < 60) return `${minutes}m ago`
    const hours = Math.floor(minutes / 60)
    if (hours < 24) return `${hours}h ago`
    return `${Math.floor(hours / 24)}d ago`
  }

  return (
    <Card className={className}>
      <CardHeader className="pb-3">
        <div className="flex items-center justify-between">
          <div>
            <CardTitle className="text-lg">Crisis Management Center</CardTitle>
            <p className="text-sm text-muted-foreground">
              {loading && !useMockData
                ? 'Loading alerts...'
                : `${activeCount} active alert${activeCount !== 1 ? 's' : ''} requiring attention`
              }
            </p>
          </div>
          {criticalCount > 0 && (
            <Badge variant="destructive" className="animate-pulse text-xs">
              {criticalCount} Critical
            </Badge>
          )}
        </div>
      </CardHeader>

      <CardContent className="space-y-2">
        {loading && !useMockData ? (
          <div className="text-center py-8 text-muted-foreground">
            <div className="animate-pulse">Loading alerts...</div>
          </div>
        ) : visibleAlerts.length === 0 ? (
          <div className="text-center py-8 text-muted-foreground">
            <CheckCircle2 className="h-10 w-10 mx-auto mb-2 text-emerald-500" />
            <p className="text-sm">All systems operational</p>
          </div>
        ) : (
          visibleAlerts.map((alert) => {
            const config = severityConfig[alert.severity]
            const Icon = config.icon
            const isExpanded = expandedAlert === alert.id

            return (
              <div
                key={alert.id}
                className={cn(
                  'rounded-lg border-l-4 transition-all cursor-pointer hover:shadow-md',
                  config.borderColor,
                  config.bgLight,
                  isExpanded && 'shadow-md'
                )}
                onClick={() => setExpandedAlert(isExpanded ? null : alert.id)}
              >
                <div className={cn('p-3', isExpanded && 'pb-3')}>
                  <div className="flex items-start gap-3">
                    <div className={cn('flex h-8 w-8 flex-shrink-0 items-center justify-center rounded-full', config.pulse && 'animate-pulse')}>
                      <Icon className={cn('h-4 w-4', config.textColor)} />
                    </div>

                    <div className="flex-1 min-w-0">
                      <div className="flex items-start justify-between gap-2 mb-1">
                        <h4 className="font-semibold text-sm truncate">{alert.title}</h4>
                        <span className="text-xs text-muted-foreground whitespace-nowrap">
                          {formatTimeAgo(alert.updatedAt)}
                        </span>
                      </div>

                      <p className={cn('text-sm', isExpanded ? 'block' : 'line-clamp-1')}>
                        {alert.description}
                      </p>

                      {isExpanded && (
                        <div className="mt-3 flex flex-wrap gap-2">
                          <Badge variant="outline" className="text-xs">
                            {alert.domain}
                          </Badge>
                          {alert.region && (
                            <Badge variant="outline" className="text-xs">
                              {alert.region}
                            </Badge>
                          )}
                          {alert.assignee && (
                            <Badge variant="secondary" className="text-xs">
                              {alert.assignee}
                            </Badge>
                          )}
                          <Button
                            variant="outline"
                            size="sm"
                            className="h-7 text-xs ml-auto"
                            onClick={(e) => {
                              e.stopPropagation()
                              onDismiss?.(alert.id)
                            }}
                          >
                            Dismiss
                          </Button>
                        </div>
                      )}
                    </div>
                  </div>
                </div>
              </div>
            )
          })
        )}
      </CardContent>
    </Card>
  )
}
