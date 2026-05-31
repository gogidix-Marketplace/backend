import * as React from 'react'
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card'
import { Progress } from '@/components/ui/progress'
import { cn } from '@/lib/utils'

export interface KPICardProps {
  title: string
  value: number
  target: number
  unit?: string
  description?: string
  status: 'on_track' | 'at_risk' | 'behind' | 'ahead'
  showTarget?: boolean
  className?: string
}

const statusConfig = {
  on_track: {
    color: 'bg-green-500',
    textColor: 'text-green-600 dark:text-green-400',
    label: 'On Track',
    progressVariant: 'default' as const,
  },
  at_risk: {
    color: 'bg-yellow-500',
    textColor: 'text-yellow-600 dark:text-yellow-400',
    label: 'At Risk',
    progressVariant: 'warning' as const,
  },
  behind: {
    color: 'bg-red-500',
    textColor: 'text-red-600 dark:text-red-400',
    label: 'Behind',
    progressVariant: 'destructive' as const,
  },
  ahead: {
    color: 'bg-blue-500',
    textColor: 'text-blue-600 dark:text-blue-400',
    label: 'Ahead',
    progressVariant: 'default' as const,
  },
}

export function KPICard({
  title,
  value,
  target,
  unit,
  description,
  status,
  showTarget = true,
  className,
}: KPICardProps) {
  const config = statusConfig[status]
  const percentage = Math.min((value / target) * 100, 100)
  const isOverTarget = value > target

  return (
    <Card className={cn('transition-all hover:shadow-md', className)}>
      <CardHeader className="pb-3">
        <div className="flex items-start justify-between">
          <CardTitle className="text-sm font-medium">{title}</CardTitle>
          <span className={cn('text-xs font-semibold', config.textColor)}>
            {config.label}
          </span>
        </div>
      </CardHeader>
      <CardContent className="space-y-3">
        <div className="flex items-baseline gap-2">
          <span className="text-2xl font-bold">
            {unit === 'currency' ? `$${value.toLocaleString()}` : `${value.toLocaleString()}${unit || ''}`}
          </span>
          {showTarget && (
            <span className="text-sm text-muted-foreground">
              of {unit === 'currency' ? `$${target.toLocaleString()}` : `${target.toLocaleString()}${unit || ''}`}
            </span>
          )}
        </div>

        <div className="space-y-1">
          <div className="flex justify-between text-xs text-muted-foreground">
            <span>Progress</span>
            <span>{isOverTarget ? '100%+' : `${percentage.toFixed(0)}%`}</span>
          </div>
          <Progress value={isOverTarget ? 100 : percentage} className="h-2" />
        </div>

        {description && (
          <p className="text-xs text-muted-foreground">{description}</p>
        )}
      </CardContent>
    </Card>
  )
}

// Add Progress component import
import { Progress as ProgressComponent } from '@/components/ui/progress'

// We need to create the Progress component first
