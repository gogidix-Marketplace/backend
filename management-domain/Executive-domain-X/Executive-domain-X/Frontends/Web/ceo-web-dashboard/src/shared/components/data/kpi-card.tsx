import { Card, CardContent, CardHeader, CardTitle } from '@shared/components/ui/card'
import { Badge } from '@shared/components/ui/badge'
import { cn } from '@shared/utils/cn'
import { TrendingUp, TrendingDown, Minus } from 'lucide-react'

/**
 * KPICard - Reusable KPI display with sparkline
 *
 * Features:
 * - Icon, label, value, change indicator
 * - Status dot with color coding
 * - Mini sparkline chart
 * - Click handler for drill-down
 * - Real-time update support
 */

export interface KPICardProps {
  title: string
  value: string | number
  unit?: string
  target?: string | number
  change?: number
  trend?: 'up' | 'down' | 'neutral'
  status?: 'on_track' | 'at_risk' | 'behind' | 'ahead'
  icon?: React.ReactNode
  sparkline?: number[]
  onClick?: () => void
  loading?: boolean
  size?: 'default' | 'compact' | 'large'
}

const statusConfig = {
  on_track: { label: 'On Track', color: 'bg-green-500' },
  at_risk: { label: 'At Risk', color: 'bg-yellow-500' },
  behind: { label: 'Behind', color: 'bg-red-500' },
  ahead: { label: 'Ahead', color: 'bg-blue-500' },
}

const sizeConfig = {
  default: { title: 'text-sm', value: 'text-2xl' },
  compact: { title: 'text-xs', value: 'text-lg' },
  large: { title: 'text-base', value: 'text-4xl' },
}

export function KPICard({
  title,
  value,
  unit,
  target,
  change,
  trend = 'neutral',
  status = 'on_track',
  icon,
  sparkline,
  onClick,
  loading = false,
  size = 'default',
}: KPICardProps) {
  const TrendIcon = trend === 'up' ? TrendingUp : trend === 'down' ? TrendingDown : Minus
  const progress = target ? Math.min((Number(value) / Number(target)) * 100, 100) : null

  if (loading) {
    return (
      <Card className="animate-pulse">
        <CardHeader className="pb-2">
          <div className="h-4 w-24 bg-slate-200 dark:bg-slate-700 rounded" />
        </CardHeader>
        <CardContent>
          <div className="h-8 w-32 bg-slate-200 dark:bg-slate-700 rounded mb-2" />
          <div className="h-2 w-full bg-slate-200 dark:bg-slate-700 rounded" />
        </CardContent>
      </Card>
    )
  }

  return (
    <Card
      className={cn(
        'transition-all hover:shadow-md',
        onClick && 'cursor-pointer hover:border-primary/50'
      )}
      onClick={onClick}
    >
      <CardHeader className="pb-3">
        <div className="flex items-start justify-between">
          <div className="flex items-center gap-2">
            {icon && <div className="text-muted-foreground">{icon}</div>}
            <CardTitle className={cn(sizeConfig[size].title, 'font-medium text-muted-foreground')}>
              {title}
            </CardTitle>
          </div>
          {status && (
            <Badge
              variant={status === 'on_track' || status === 'ahead' ? 'success' : 'warning'}
              className="text-xs"
            >
              {statusConfig[status].label}
            </Badge>
          )}
        </div>
      </CardHeader>
      <CardContent className="space-y-3">
        <div className="flex items-baseline gap-2">
          <span className={cn('font-bold tracking-tight', sizeConfig[size].value)}>
            {value}
          </span>
          {unit && (
            <span className="text-sm text-muted-foreground">{unit}</span>
          )}
          {target && (
            <span className="text-sm text-muted-foreground">
              of {target}
              {unit}
            </span>
          )}
        </div>

        {change !== undefined && (
          <div className="flex items-center gap-2 text-sm">
            <TrendIcon
              className={cn(
                'h-4 w-4',
                trend === 'up' ? 'text-green-500' : trend === 'down' ? 'text-red-500' : 'text-gray-500'
              )}
            />
            <span
              className={cn(
                change > 0 ? 'text-green-600 dark:text-green-400' : 'text-red-600 dark:text-red-400'
              )}
            >
              {change > 0 ? '+' : ''}
              {change}%
            </span>
            <span className="text-muted-foreground">from last period</span>
          </div>
        )}

        {(progress !== null || sparkline) && (
          <div className="space-y-2">
            {progress !== null && (
              <div className="space-y-1">
                <div className="flex justify-between text-xs text-muted-foreground">
                  <span>Progress</span>
                  <span>{Math.round(progress)}%</span>
                </div>
                <div className="h-2 w-full overflow-hidden rounded-full bg-slate-200 dark:bg-slate-700">
                  <div
                    className={cn('h-full transition-all', statusConfig[status].color)}
                    style={{ width: `${Math.min(progress, 100)}%` }}
                  />
                </div>
              </div>
            )}

            {sparkline && sparkline.length > 0 && (
              <div className="h-8 w-full">
                <svg viewBox={`0 0 ${sparkline.length} 20`} className="h-full w-full" preserveAspectRatio="none">
                  <polyline
                    fill="none"
                    stroke="currentColor"
                    strokeWidth="2"
                    points={sparkline
                      .map((v, i) => {
                        const max = Math.max(...sparkline)
                        const min = Math.min(...sparkline)
                        const range = max - min || 1
                        const y = 18 - ((v - min) / range) * 16
                        return `${i},${y}`
                      })
                      .join(' ')}
                    className={cn(
                      trend === 'up' ? 'text-green-500' : trend === 'down' ? 'text-red-500' : 'text-primary'
                    )}
                  />
                </svg>
              </div>
            )}
          </div>
        )}
      </CardContent>
    </Card>
  )
}
