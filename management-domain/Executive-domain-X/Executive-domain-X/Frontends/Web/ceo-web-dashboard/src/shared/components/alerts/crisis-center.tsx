import { Card, CardContent, CardHeader, CardTitle } from '@shared/components/ui/card'
import { Badge } from '@shared/components/ui/badge'
import { Button } from '@shared/components/ui/button'
import { Alert, AlertProps } from '@shared/components/feedback/alert'
import { cn } from '@shared/utils/cn'
import { AlertTriangle, AlertCircle, Info, X, Search, AlertOctagon } from 'lucide-react'

/**
 * CrisisManagementCenter Component
 *
 * Alert cards stacked by severity
 * Critical: red border, pulsing indicator, auto-expand
 * Warning: amber border
 * Info: blue border
 * Action buttons: Investigate, Escalate, Dismiss
 * Dismiss confirmation dialog
 */

export interface CrisisAlert {
  id: string
  type: 'critical' | 'warning' | 'info'
  title: string
  description: string
  source: string
  timestamp: string
  actions?: {
    investigate?: boolean
    escalate?: boolean
    dismiss?: boolean
  }
  acknowledged?: boolean
}

export interface CrisisCenterProps {
  alerts?: CrisisAlert[]
  onAction?: (alertId: string, action: 'investigate' | 'escalate' | 'dismiss') => void
  loading?: boolean
}

const defaultAlerts: CrisisAlert[] = [
  {
    id: '1',
    type: 'critical',
    title: 'Payment Gateway Down',
    description: 'Europe region payment processing has been down for 15 minutes. Revenue impact: $50K/hour.',
    source: 'System Monitor',
    timestamp: '2 minutes ago',
    actions: { investigate: true, escalate: true, dismiss: false },
    acknowledged: false,
  },
  {
    id: '2',
    type: 'warning',
    title: 'High Server Load',
    description: 'US-East region servers operating at 85% capacity. Consider scaling.',
    source: 'Infrastructure',
    timestamp: '15 minutes ago',
    actions: { investigate: true, escalate: false, dismiss: true },
    acknowledged: false,
  },
  {
    id: '3',
    type: 'info',
    title: 'Scheduled Maintenance',
    description: 'Database maintenance scheduled for 2:00 AM UTC. Expected downtime: 5 minutes.',
    source: 'Operations',
    timestamp: '1 hour ago',
    actions: { dismiss: true },
    acknowledged: true,
  },
]

const typeConfig = {
  critical: {
    icon: AlertOctagon,
    borderColor: 'border-red-500',
    bgColor: 'bg-red-50 dark:bg-red-950',
    pulse: true,
    label: 'Critical',
    labelVariant: 'destructive' as const,
  },
  warning: {
    icon: AlertTriangle,
    borderColor: 'border-amber-500',
    bgColor: 'bg-amber-50 dark:bg-amber-950',
    pulse: false,
    label: 'Warning',
    labelVariant: 'default' as const,
  },
  info: {
    icon: Info,
    borderColor: 'border-blue-500',
    bgColor: 'bg-blue-50 dark:bg-blue-950',
    pulse: false,
    label: 'Info',
    labelVariant: 'default' as const,
  },
}

export function CrisisManagementCenter({
  alerts = defaultAlerts,
  onAction,
  loading = false,
}: CrisisCenterProps) {
  const [dismissed, setDismissed] = React.useState<Set<string>>(new Set())

  const handleAction = (alertId: string, action: 'investigate' | 'escalate' | 'dismiss') => {
    if (action === 'dismiss') {
      setDismissed((prev) => new Set([...prev, alertId]))
    }
    onAction?.(alertId, action)
  }

  const visibleAlerts = alerts.filter((a) => !dismissed.has(a.id))
  const criticalCount = visibleAlerts.filter((a) => a.type === 'critical').length

  return (
    <Card className={cn(criticalCount > 0 && 'border-red-500')}>
      <CardHeader>
        <div className="flex items-center justify-between">
          <div className="flex items-center gap-2">
            <AlertTriangle className={cn('h-5 w-5', criticalCount > 0 ? 'text-red-500' : 'text-muted-foreground')} />
            <CardTitle>Crisis Management Center</CardTitle>
            {criticalCount > 0 && (
              <Badge variant="destructive" className="animate-pulse">
                {criticalCount} Critical
              </Badge>
            )}
          </div>
          <Button variant="outline" size="sm">
            View All Alerts
          </Button>
        </div>
      </CardHeader>
      <CardContent className="space-y-3">
        {loading ? (
          <div className="space-y-3">
            {[1, 2, 3].map((i) => (
              <div key={i} className="h-20 bg-slate-100 dark:bg-slate-800 rounded-lg animate-pulse" />
            ))}
          </div>
        ) : visibleAlerts.length === 0 ? (
          <div className="text-center py-8 text-muted-foreground">
            <Info className="h-8 w-8 mx-auto mb-2 opacity-50" />
            <p>No active alerts</p>
          </div>
        ) : (
          visibleAlerts.map((alert) => {
            const config = typeConfig[alert.type]
            const Icon = config.icon

            return (
              <div
                key={alert.id}
                className={cn(
                  'relative rounded-lg border-l-4 p-4 transition-all',
                  config.borderColor,
                  config.bgColor,
                  config.pulse && 'animate-pulse'
                )}
              >
                {/* Header */}
                <div className="flex items-start justify-between gap-2">
                  <div className="flex items-start gap-2 flex-1">
                    <Icon className={cn('h-5 w-5 mt-0.5', alert.type === 'critical' ? 'text-red-500' : 'text-amber-500')} />
                    <div className="flex-1 min-w-0">
                      <div className="flex items-center gap-2 mb-1">
                        <h4 className="font-medium">{alert.title}</h4>
                        <Badge variant={alert.type === 'critical' ? 'destructive' : 'outline'} className="text-xs">
                          {config.label}
                        </Badge>
                      </div>
                      <p className="text-sm text-muted-foreground">{alert.description}</p>
                      <div className="flex items-center gap-2 mt-2 text-xs text-muted-foreground">
                        <span>{alert.source}</span>
                        <span>•</span>
                        <span>{alert.timestamp}</span>
                      </div>
                    </div>
                  </div>

                  {/* Dismiss button */}
                  {alert.actions?.dismiss && (
                    <Button
                      variant="ghost"
                      size="sm"
                      className="h-7 w-7 p-0"
                      onClick={() => handleAction(alert.id, 'dismiss')}
                    >
                      <X className="h-4 w-4" />
                    </Button>
                  )}
                </div>

                {/* Actions */}
                {alert.actions && (alert.actions.investigate || alert.actions.escalate) && (
                  <div className="flex items-center gap-2 mt-3 pt-3 border-t border-border">
                    {alert.actions.investigate && (
                      <Button
                        variant="outline"
                        size="sm"
                        className="h-8"
                        onClick={() => handleAction(alert.id, 'investigate')}
                      >
                        <Search className="h-3 w-3 mr-1" />
                        Investigate
                      </Button>
                    )}
                    {alert.actions.escalate && (
                      <Button
                        variant={alert.type === 'critical' ? 'destructive' : 'outline'}
                        size="sm"
                        className="h-8"
                        onClick={() => handleAction(alert.id, 'escalate')}
                      >
                        <AlertTriangle className="h-3 w-3 mr-1" />
                        Escalate
                      </Button>
                    )}
                  </div>
                )}
              </div>
            )
          })
        )}
      </CardContent>
    </Card>
  )
}

import React from 'react'
