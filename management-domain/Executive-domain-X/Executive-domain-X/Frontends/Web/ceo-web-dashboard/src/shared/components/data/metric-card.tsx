import { Card, CardContent } from '@shared/components/ui/card'
import { cn } from '@shared/utils/cn'
import { TrendingUp, TrendingDown, Minus, LucideIcon } from 'lucide-react'

/**
 * MetricCard - Single metric with trend
 *
 * Simpler version of KPICard for single metric display
 */

export interface MetricCardProps {
  label: string
  value: string | number
  unit?: string
  change?: number
  changeLabel?: string
  trend?: 'up' | 'down' | 'neutral'
  icon?: LucideIcon
  color?: 'blue' | 'green' | 'red' | 'amber' | 'purple' | 'default'
  loading?: boolean
  onClick?: () => void
}

const colorClasses = {
  blue: 'bg-blue-50 dark:bg-blue-950 border-blue-200 dark:border-blue-800',
  green: 'bg-green-50 dark:bg-green-950 border-green-200 dark:border-green-800',
  red: 'bg-red-50 dark:bg-red-950 border-red-200 dark:border-red-800',
  amber: 'bg-amber-50 dark:bg-amber-950 border-amber-200 dark:border-amber-800',
  purple: 'bg-purple-50 dark:bg-purple-950 border-purple-200 dark:border-purple-800',
  default: 'bg-slate-50 dark:bg-slate-900 border-slate-200 dark:border-slate-800',
}

const iconColorClasses = {
  blue: 'text-blue-600 dark:text-blue-400',
  green: 'text-green-600 dark:text-green-400',
  red: 'text-red-600 dark:text-red-400',
  amber: 'text-amber-600 dark:text-amber-400',
  purple: 'text-purple-600 dark:text-purple-400',
  default: 'text-slate-600 dark:text-slate-400',
}

export function MetricCard({
  label,
  value,
  unit,
  change,
  changeLabel,
  trend = 'neutral',
  icon: Icon,
  color = 'default',
  loading = false,
  onClick,
}: MetricCardProps) {
  const TrendIcon = trend === 'up' ? TrendingUp : trend === 'down' ? TrendingDown : Minus

  if (loading) {
    return (
      <Card className="animate-pulse">
        <CardContent className="p-4">
          <div className="h-4 w-20 bg-slate-200 dark:bg-slate-700 rounded mb-2" />
          <div className="h-8 w-24 bg-slate-200 dark:bg-slate-700 rounded" />
        </CardContent>
      </Card>
    )
  }

  return (
    <Card
      className={cn(
        'transition-all hover:shadow-md',
        colorClasses[color],
        onClick && 'cursor-pointer hover:scale-[1.02]'
      )}
      onClick={onClick}
    >
      <CardContent className="p-4">
        <div className="flex items-center justify-between">
          <div className="space-y-1">
            <p className="text-sm font-medium text-muted-foreground">{label}</p>
            <div className="flex items-baseline gap-1">
              <span className="text-2xl font-bold">{value}</span>
              {unit && <span className="text-sm text-muted-foreground">{unit}</span>}
            </div>
            {change !== undefined && (
              <div className="flex items-center gap-1 text-sm">
                <TrendIcon
                  className={cn(
                    'h-3 w-3',
                    trend === 'up' ? 'text-green-600' : trend === 'down' ? 'text-red-600' : 'text-gray-500'
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
                {changeLabel && <span className="text-muted-foreground">{changeLabel}</span>}
              </div>
            )}
          </div>
          {Icon && (
            <div className={cn('h-10 w-10 rounded-full flex items-center justify-center', iconColorClasses[color])}>
              <Icon className="h-5 w-5" />
            </div>
          )}
        </div>
      </CardContent>
    </Card>
  )
}
