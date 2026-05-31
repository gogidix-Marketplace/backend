// ============================================
// FINANCE DEPARTMENT - KPI CARD COMPONENT
// ============================================

import { ArrowUp, ArrowUpRight, ArrowDown, ArrowDownRight, Minus } from 'lucide-react'
import { cn, formatCurrency, formatNumber, formatPercentage } from '@shared/utils/cn'

interface KPICardProps {
  title: string
  value: string | number
  previousValue?: string | number
  trend?: 'up' | 'down' | 'neutral'
  status?: 'on_track' | 'warning' | 'critical'
  unit?: string
  currency?: string
  description?: string
  className?: string
  onClick?: () => void
}

export function KPICard({
  title,
  value,
  previousValue,
  trend = 'neutral',
  status = 'on_track',
  unit,
  currency,
  description,
  className,
  onClick,
}: KPICardProps) {
  const formatValue = (val: string | number) => {
    if (typeof val === 'number') {
      if (currency) return formatCurrency(val, currency)
      if (unit) return formatNumber(val, 0) + unit
      return formatNumber(val)
    }
    return val
  }

  const getTrendIcon = () => {
    switch (trend) {
      case 'up':
        return <ArrowUp size={16} />
      case 'down':
        return <ArrowDown size={16} />
      default:
        return <Minus size={16} />
    }
  }

  const getStatusColor = () => {
    switch (status) {
      case 'on_track':
        return 'bg-emerald-50 border-emerald-200'
      case 'warning':
        return 'bg-amber-50 border-amber-200'
      case 'critical':
        return 'bg-red-50 border-red-200'
      default:
        return 'bg-white border-slate-200'
    }
  }

  const getTrendColor = () => {
    switch (trend) {
      case 'up':
        return 'text-emerald-600'
      case 'down':
        return 'text-red-600'
      default:
        return 'text-slate-500'
    }
  }

  const calculateChange = () => {
    if (typeof value === 'number' && typeof previousValue === 'number') {
      const change = ((value - previousValue) / previousValue) * 100
      return formatPercentage(change)
    }
    return null
  }

  return (
    <div
      onClick={onClick}
      className={cn(
        'rounded-xl border p-6 transition-all',
        getStatusColor(),
        onClick && 'cursor-pointer hover:shadow-md',
        className
      )}
    >
      <div className="flex items-start justify-between">
        <div className="flex-1">
          <p className="text-sm font-medium text-slate-600">{title}</p>
          <p className="text-2xl font-bold text-slate-900 mt-1">
            {formatValue(value)}
          </p>
          {description && (
            <p className="text-xs text-slate-500 mt-1">{description}</p>
          )}
        </div>

        {(trend !== 'neutral' || previousValue !== undefined) && (
          <div className={cn('flex items-center gap-1 text-sm font-medium', getTrendColor())}>
            {getTrendIcon()}
            {calculateChange && (
              <span>{calculateChange()}</span>
            )}
          </div>
        )}
      </div>
    </div>
  )
}
