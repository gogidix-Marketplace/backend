import * as React from 'react'
import { ArrowUp, ArrowDown, Minus } from 'lucide-react'
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card'
import { Badge } from '@/components/ui/badge'
import { cn, formatNumber, formatCurrency } from '@/lib/utils'

export interface MetricCardProps {
  title: string
  value: number | string
  unit?: string
  change?: number
  changeType?: 'increase' | 'decrease' | 'neutral'
  icon?: React.ReactNode
  description?: string
  trend?: 'up' | 'down' | 'neutral'
  loading?: boolean
  className?: string
  variant?: 'default' | 'executive' | 'success' | 'warning' | 'danger'
}

export function MetricCard({
  title,
  value,
  unit,
  change,
  changeType,
  icon,
  description,
  trend,
  loading = false,
  className,
  variant = 'default',
}: MetricCardProps) {
  // Auto-determine trend from change if not provided
  const displayTrend = trend || (change ? (change > 0 ? 'up' : change < 0 ? 'down' : 'neutral') : 'neutral')
  const displayChangeType = changeType || (change ? (change > 0 ? 'increase' : change < 0 ? 'decrease' : 'neutral') : 'neutral')

  const variantStyles = {
    default: 'bg-card text-card-foreground border-border',
    executive: 'bg-gradient-to-br from-[#0D47A1] to-[#1565C0] text-white border-[#0D47A1]',
    success: 'bg-gradient-to-br from-green-500 to-green-600 text-white border-green-500',
    warning: 'bg-gradient-to-br from-yellow-500 to-orange-500 text-white border-yellow-500',
    danger: 'bg-gradient-to-br from-red-500 to-red-600 text-white border-red-500',
  }

  const getTrendIcon = () => {
    switch (displayTrend) {
      case 'up':
        return <ArrowUp className="h-4 w-4" />
      case 'down':
        return <ArrowDown className="h-4 w-4" />
      default:
        return <Minus className="h-4 w-4" />
    }
  }

  const formatValue = () => {
    if (typeof value === 'string') return value
    if (unit === 'currency') return formatCurrency(value)
    if (unit === 'percentage') return `${value.toFixed(1)}%`
    if (unit) return `${formatNumber(value)} ${unit}`
    return formatNumber(value)
  }

  if (loading) {
    return (
      <Card className={cn('animate-pulse', className)}>
        <CardHeader className="space-y-2 pb-2">
          <div className="h-4 w-24 bg-muted rounded" />
          <div className="h-8 w-32 bg-muted rounded" />
        </CardHeader>
        <CardContent>
          <div className="h-4 w-full bg-muted rounded" />
        </CardContent>
      </Card>
    )
  }

  return (
    <Card className={cn('relative overflow-hidden transition-all hover:shadow-lg', variantStyles[variant], className)}>
      <CardHeader className="flex flex-row items-center justify-between pb-2">
        <CardTitle className={cn('text-sm font-medium', variant === 'default' && 'text-muted-foreground')}>
          {title}
        </CardTitle>
        {icon && (
          <div className={cn('rounded-lg p-2', variant === 'default' && 'bg-muted/50')}>
            {icon}
          </div>
        )}
      </CardHeader>
      <CardContent>
        <div className="flex items-baseline gap-2">
          <div className="text-2xl font-bold tracking-tight">
            {formatValue()}
          </div>
          {change !== undefined && (
            <Badge
              variant={displayChangeType === 'increase' ? 'success' : displayChangeType === 'decrease' ? 'destructive' : 'secondary'}
              className="gap-1"
            >
              {getTrendIcon()}
              {Math.abs(change).toFixed(1)}%
            </Badge>
          )}
        </div>
        {description && (
          <p className={cn('mt-1 text-xs', variant === 'default' ? 'text-muted-foreground' : 'text-white/80')}>
            {description}
          </p>
        )}
      </CardContent>
    </Card>
  )
}
