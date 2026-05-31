import * as React from 'react'
import { cn } from '@shared/utils/cn'
import { Popover, PopoverContent, PopoverTrigger } from '@shared/components/ui/popover'
import { Badge } from '@shared/components/ui/badge'
import { Sparkles, TrendingUp, TrendingDown, Minus } from 'lucide-react'

/**
 * AI Predictive Indicator - Hover prediction display
 *
 * Features:
 * - Shows on hover
 * - Predicted value with confidence
 * - Trend indicator
 * - Visual sparkle icon
 * - Purple AI theme
 */

export interface Prediction {
  value: number
  confidence: number // 0-100
  trend: 'up' | 'down' | 'neutral'
  timeframe?: string
  reasoning?: string
}

export interface AIPredictiveIndicatorProps {
  prediction: Prediction
  current?: number
  unit?: string
  formatValue?: (value: number) => string
  children: React.ReactElement
  showIcon?: boolean
  variant?: 'tooltip' | 'popover'
  className?: string
}

export function AIPredictiveIndicator({
  prediction,
  current,
  unit = '',
  formatValue = (v) => v.toString(),
  children,
  showIcon = true,
  variant = 'tooltip',
  className,
}: AIPredictiveIndicatorProps) {
  const [isOpen, setIsOpen] = React.useState(false)

  const TrendIcon = prediction.trend === 'up' ? TrendingUp : prediction.trend === 'down' ? TrendingDown : Minus

  const getConfidenceColor = (confidence: number) => {
    if (confidence >= 80) return 'text-green-600 dark:text-green-400'
    if (confidence >= 60) return 'text-amber-600 dark:text-amber-400'
    return 'text-red-600 dark:text-red-400'
  }

  const content = (
    <div className="space-y-3">
      <div className="flex items-center gap-2">
        <Sparkles className="h-4 w-4 text-purple-600 dark:text-purple-400" />
        <span className="font-semibold text-sm">AI Prediction</span>
      </div>

      <div className="space-y-2">
        <div className="flex items-baseline justify-between gap-4">
          <span className="text-sm text-muted-foreground">Predicted value</span>
          <div className="flex items-center gap-1">
            <TrendIcon className={cn('h-3 w-3', getConfidenceColor(prediction.confidence))} />
            <span className="text-lg font-bold">{formatValue(prediction.value)}{unit}</span>
          </div>
        </div>

        {current !== undefined && (
          <div className="flex items-baseline justify-between gap-4">
            <span className="text-sm text-muted-foreground">Current value</span>
            <span className="text-sm">{formatValue(current)}{unit}</span>
          </div>
        )}

        <div className="flex items-center justify-between gap-4">
          <span className="text-sm text-muted-foreground">Confidence</span>
          <div className="flex items-center gap-2">
            <div className="h-1.5 w-16 overflow-hidden rounded-full bg-slate-200 dark:bg-slate-700">
              <div
                className="h-full bg-gradient-to-r from-purple-500 to-indigo-500"
                style={{ width: `${prediction.confidence}%` }}
              />
            </div>
            <span className={cn('text-sm font-medium', getConfidenceColor(prediction.confidence))}>
              {prediction.confidence}%
            </span>
          </div>
        </div>

        {prediction.timeframe && (
          <div className="flex items-center justify-between gap-4">
            <span className="text-sm text-muted-foreground">Timeframe</span>
            <span className="text-sm">{prediction.timeframe}</span>
          </div>
        )}
      </div>

      {prediction.reasoning && (
        <div className="pt-2 border-t border-slate-200 dark:border-slate-700">
          <p className="text-xs text-muted-foreground">{prediction.reasoning}</p>
        </div>
      )}
    </div>
  )

  const trigger = React.cloneElement(children, {
    className: cn(
      'relative transition-all',
      isOpen && 'ring-2 ring-purple-500 ring-offset-2',
      children.props.className
    ),
  })

  if (variant === 'popover') {
    return (
      <Popover open={isOpen} onOpenChange={setIsOpen}>
        <PopoverTrigger asChild>{trigger}</PopoverTrigger>
        <PopoverContent className="w-72" align="start">
          {content}
        </PopoverContent>
      </Popover>
    )
  }

  // Tooltip variant
  return (
    <div
      className={cn('group relative inline-block', className)}
      onMouseEnter={() => setIsOpen(true)}
      onMouseLeave={() => setIsOpen(false)}
    >
      {trigger}
      {showIcon && (
        <Sparkles className="absolute -top-1 -right-1 h-3 w-3 text-purple-500 opacity-0 group-hover:opacity-100 transition-opacity" />
      )}

      {isOpen && (
        <div className="absolute z-50 p-3 mt-2 text-sm bg-white dark:bg-slate-900 border border-purple-200 dark:border-purple-800 rounded-lg shadow-lg w-64">
          {content}
        </div>
      )}
    </div>
  )
}

/**
 * AI Prediction Badge - Small badge showing prediction status
 */
export interface AIPredictionBadgeProps {
  prediction: Prediction
  formatValue?: (value: number) => string
  variant?: 'default' | 'compact'
  className?: string
}

export function AIPredictionBadge({
  prediction,
  formatValue = (v) => v.toString(),
  variant = 'default',
  className,
}: AIPredictionBadgeProps) {
  const TrendIcon = prediction.trend === 'up' ? TrendingUp : prediction.trend === 'down' ? TrendingDown : Minus

  const getConfidenceColor = (confidence: number) => {
    if (confidence >= 80) return 'bg-green-100 text-green-700 dark:bg-green-900/20 dark:text-green-400 border-green-200 dark:border-green-800'
    if (confidence >= 60) return 'bg-amber-100 text-amber-700 dark:bg-amber-900/20 dark:text-amber-400 border-amber-200 dark:border-amber-800'
    return 'bg-red-100 text-red-700 dark:bg-red-900/20 dark:text-red-400 border-red-200 dark:border-red-800'
  }

  if (variant === 'compact') {
    return (
      <Badge
        variant="outline"
        className={cn(
          'gap-1 bg-gradient-to-r from-purple-50 to-indigo-50 dark:from-purple-950/20 dark:to-indigo-950/20',
          'border-purple-200 dark:border-purple-800',
          className
        )}
      >
        <Sparkles className="h-3 w-3 text-purple-600 dark:text-purple-400" />
        <span className="text-xs">AI: {formatValue(prediction.value)}</span>
      </Badge>
    )
  }

  return (
    <Badge
      variant="outline"
      className={cn(
        'gap-1.5 px-2.5 py-1',
        getConfidenceColor(prediction.confidence),
        className
      )}
    >
      <Sparkles className="h-3 w-3" />
      <TrendIcon className="h-3 w-3" />
      <span className="font-medium">{formatValue(prediction.value)}</span>
      <span className="text-xs opacity-70">({prediction.confidence}%)</span>
    </Badge>
  )
}
