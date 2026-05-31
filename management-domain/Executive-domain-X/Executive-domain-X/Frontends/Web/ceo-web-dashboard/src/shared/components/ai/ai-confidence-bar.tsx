import * as React from 'react'
import { cn } from '@shared/utils/cn'
import { Tooltip, TooltipContent, TooltipTrigger, TooltipProvider } from '@shared/components/ui/tooltip'

/**
 * AI Confidence Bar - Confidence visualization for AI predictions
 *
 * Features:
 * - Gradient bar (purple to indigo)
 * - Percentage display
 * - Tooltip with details
 * - Animated on mount
 * - Size variants
 */

export interface AIConfidenceBarProps {
  confidence: number // 0-100
  size?: 'sm' | 'md' | 'lg'
  showLabel?: boolean
  showPercentage?: boolean
  animated?: boolean
  variant?: 'default' | 'compact' | 'inline'
  tooltip?: string
  className?: string
}

const sizeConfig = {
  sm: { bar: 'h-1', text: 'text-xs' },
  md: { bar: 'h-2', text: 'text-sm' },
  lg: { bar: 'h-3', text: 'text-base' },
}

const aiGradient = 'from-purple-500 via-indigo-500 to-purple-600'

export function AIConfidenceBar({
  confidence,
  size = 'md',
  showLabel = true,
  showPercentage = true,
  animated = true,
  variant = 'default',
  tooltip,
  className,
}: AIConfidenceBarProps) {
  const [displayConfidence, setDisplayConfidence] = React.useState(0)

  React.useEffect(() => {
    if (!animated) {
      setDisplayConfidence(confidence)
      return
    }

    const duration = 1000
    const steps = 60
    const increment = confidence / steps
    const interval = duration / steps

    let current = 0
    const timer = setInterval(() => {
      current += increment
      if (current >= confidence) {
        setDisplayConfidence(confidence)
        clearInterval(timer)
      } else {
        setDisplayConfidence(current)
      }
    }, interval)

    return () => clearInterval(timer)
  }, [confidence, animated])

  const sizeClasses = sizeConfig[size]

  const getConfidenceColor = (value: number) => {
    if (value >= 80) return 'text-green-600 dark:text-green-400'
    if (value >= 60) return 'text-amber-600 dark:text-amber-400'
    return 'text-red-600 dark:text-red-400'
  }

  const bar = (
    <div className="flex items-center gap-2">
      {showLabel && (
        <span className={cn('text-muted-foreground whitespace-nowrap', sizeClasses.text)}>
          Confidence
        </span>
      )}
      <div className="flex-1 flex items-center gap-2">
        <div
          className={cn(
            'flex-1 overflow-hidden rounded-full bg-slate-200 dark:bg-slate-700',
            sizeClasses.bar
          )}
        >
          <div
            className={cn(
              'h-full bg-gradient-to-r transition-all duration-500',
              aiGradient
            )}
            style={{ width: `${displayConfidence}%` }}
          />
        </div>
        {showPercentage && (
          <span
            className={cn(
              'font-medium tabular-nums min-w-[3ch] text-right',
              sizeClasses.text,
              getConfidenceColor(displayConfidence)
            )}
          >
            {Math.round(displayConfidence)}%
          </span>
        )}
      </div>
    </div>
  )

  if (tooltip) {
    return (
      <TooltipProvider>
        <Tooltip>
          <TooltipTrigger asChild>
            <div className={cn('cursor-help', className)}>{bar}</div>
          </TooltipTrigger>
          <TooltipContent>
            <p className="max-w-xs">{tooltip}</p>
          </TooltipContent>
        </Tooltip>
      </TooltipProvider>
    )
  }

  return <div className={className}>{bar}</div>
}

/**
 * AI Confidence Score - Circular variant
 */
export interface AIConfidenceScoreProps {
  confidence: number // 0-100
  size?: number
  strokeWidth?: number
  showValue?: boolean
  animated?: boolean
  className?: string
}

export function AIConfidenceScore({
  confidence,
  size = 60,
  strokeWidth = 6,
  showValue = true,
  animated = true,
  className,
}: AIConfidenceScoreProps) {
  const [displayConfidence, setDisplayConfidence] = React.useState(0)

  React.useEffect(() => {
    if (!animated) {
      setDisplayConfidence(confidence)
      return
    }

    const duration = 1000
    const steps = 60
    const increment = confidence / steps
    const interval = duration / steps

    let current = 0
    const timer = setInterval(() => {
      current += increment
      if (current >= confidence) {
        setDisplayConfidence(confidence)
        clearInterval(timer)
      } else {
        setDisplayConfidence(current)
      }
    }, interval)

    return () => clearInterval(timer)
  }, [confidence, animated])

  const radius = (size - strokeWidth) / 2
  const circumference = 2 * Math.PI * radius
  const strokeDashoffset = circumference - (displayConfidence / 100) * circumference
  const center = size / 2

  const getConfidenceColor = (value: number) => {
    if (value >= 80) return 'hsl(var(--success))'
    if (value >= 60) return 'hsl(var(--warning))'
    return 'hsl(var(--destructive))'
  }

  return (
    <div className={cn('inline-flex relative', className)}>
      <svg width={size} height={size} className="transform -rotate-90" viewBox={`0 0 ${size} ${size}`}>
        {/* Background circle */}
        <circle
          cx={center}
          cy={center}
          r={radius}
          fill="none"
          stroke="hsl(var(--muted))"
          strokeWidth={strokeWidth}
          strokeLinecap="round"
        />
        {/* Progress circle */}
        <circle
          cx={center}
          cy={center}
          r={radius}
          fill="none"
          stroke="url(#ai-gradient)"
          strokeWidth={strokeWidth}
          strokeLinecap="round"
          strokeDasharray={circumference}
          strokeDashoffset={strokeDashoffset}
          style={{
            transition: animated ? 'stroke-dashoffset 1s ease-out' : undefined,
          }}
        />
        {/* Gradient definition */}
        <defs>
          <linearGradient id="ai-gradient" x1="0%" y1="0%" x2="100%" y2="0%">
            <stop offset="0%" stopColor="#7C4DFF" />
            <stop offset="100%" stopColor="#B388FF" />
          </linearGradient>
        </defs>
      </svg>

      {showValue && (
        <div
          className="absolute inset-0 flex items-center justify-center"
          style={{ color: getConfidenceColor(displayConfidence) }}
        >
          <span className="text-sm font-bold tabular-nums">{Math.round(displayConfidence)}</span>
        </div>
      )}
    </div>
  )
}
