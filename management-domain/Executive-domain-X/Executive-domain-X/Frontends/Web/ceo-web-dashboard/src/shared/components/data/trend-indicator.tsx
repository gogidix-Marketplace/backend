import { cn } from '@shared/utils/cn'
import { TrendingUp, TrendingDown, Minus, LucideIcon } from 'lucide-react'

/**
 * TrendIndicator - Up/down/neutral trend with icon
 *
 * Features:
 * - Icon-based trend display
 * - Percentage change display
 * - Color coding
 * - Size variants
 * - Compact mode
 */

export interface TrendIndicatorProps {
  value: number // Percentage change
  label?: string
  size?: 'sm' | 'md' | 'lg'
  variant?: 'default' | 'subtle' | 'vibrant'
  showIcon?: boolean
  showSign?: boolean
  icon?: {
    up: LucideIcon
    down: LucideIcon
    neutral: LucideIcon
  }
  className?: string
}

const sizeClasses = {
  sm: { icon: 'h-3 w-3', text: 'text-xs' },
  md: { icon: 'h-4 w-4', text: 'text-sm' },
  lg: { icon: 'h-5 w-5', text: 'text-base' },
}

const variantClasses = {
  default: {
    positive: 'text-green-600 dark:text-green-400',
    negative: 'text-red-600 dark:text-red-400',
    neutral: 'text-muted-foreground',
  },
  subtle: {
    positive: 'text-green-600/70 dark:text-green-400/70',
    negative: 'text-red-600/70 dark:text-red-400/70',
    neutral: 'text-muted-foreground/70',
  },
  vibrant: {
    positive: 'text-emerald-500 dark:text-emerald-400',
    negative: 'text-rose-500 dark:text-rose-400',
    neutral: 'text-slate-500 dark:text-slate-400',
  },
}

export function TrendIndicator({
  value,
  label,
  size = 'md',
  variant = 'default',
  showIcon = true,
  showSign = true,
  icon: { up: UpIcon, down: DownIcon, neutral: NeutralIcon } = {
    up: TrendingUp,
    down: TrendingDown,
    neutral: Minus,
  },
  className,
}: TrendIndicatorProps) {
  const sizeClass = sizeClasses[size]
  const variantClass = variantClasses[variant]
  const trend = value > 0 ? 'positive' : value < 0 ? 'negative' : 'neutral'
  const TrendIcon = trend === 'positive' ? UpIcon : trend === 'negative' ? DownIcon : NeutralIcon
  const displayValue = Math.abs(value)

  return (
    <span className={cn('inline-flex items-center gap-1', sizeClass.text, className)}>
      {showIcon && (
        <TrendIcon className={cn(sizeClass.icon, variantClass[trend])} strokeWidth={2.5} />
      )}
      <span className={cn('font-medium tabular-nums', variantClass[trend])}>
        {showSign && trend !== 'neutral' ? (trend === 'positive' ? '+' : '-') : ''}
        {displayValue}%
      </span>
      {label && <span className="text-muted-foreground ml-1">{label}</span>}
    </span>
  )
}

/**
 * MiniSparkTrend - Small trend with inline sparkline
 */
export interface MiniSparkTrendProps {
  data: number[]
  value: number
  size?: 'sm' | 'md'
}

export function MiniSparkTrend({ data, value, size = 'sm' }: MiniSparkTrendProps) {
  const height = size === 'sm' ? 16 : 24
  const width = data.length * 4

  const max = Math.max(...data)
  const min = Math.min(...data)
  const range = max - min || 1

  const points = data
    .map((v, i) => {
      const x = i * 4
      const y = height - ((v - min) / range) * height
      return `${x},${y}`
    })
    .join(' ')

  const trend = value > 0 ? 'positive' : value < 0 ? 'negative' : 'neutral'
  const colorClass =
    trend === 'positive'
      ? 'text-green-500'
      : trend === 'negative'
      ? 'text-red-500'
      : 'text-muted-foreground'

  return (
    <span className="inline-flex items-center gap-2">
      <svg width={width} height={height} className={colorClass}>
        <polyline
          fill="none"
          stroke="currentColor"
          strokeWidth="2"
          points={points}
          strokeLinecap="round"
          strokeLinejoin="round"
        />
      </svg>
      <TrendIndicator value={value} size="sm" />
    </span>
  )
}
