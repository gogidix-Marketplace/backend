import * as React from 'react'
import { cn } from '@shared/utils/cn'
import { Badge } from '@shared/components/ui/badge'
import { Popover, PopoverContent, PopoverTrigger } from '@shared/components/ui/popover'
import {
  AlertTriangle,
  AlertCircle,
  Info,
  Sparkles,
  TrendingUp,
  TrendingDown,
} from 'lucide-react'

/**
 * AI Anomaly Badge - Anomaly indicator for tables and lists
 *
 * Features:
 * - Shows AI-detected anomalies
 * - Severity levels (info, warning, critical)
 * - Detailed explanation on hover/click
 * - Confidence indicator
 * - Action suggestions
 */

export type AnomalySeverity = 'info' | 'warning' | 'critical'

export interface Anomaly {
  id: string
  type: string
  severity: AnomalySeverity
  description: string
  value: number
  expected?: number
  variance?: number
  confidence: number
  detectedAt: Date
  suggestions?: string[]
}

export interface AIAnomalyBadgeProps {
  anomaly: Anomaly
  variant?: 'badge' | 'dot' | 'icon'
  showConfidence?: boolean
  onClick?: () => void
  className?: string
}

const severityConfig = {
  info: {
    icon: Info,
    bgColor: 'bg-blue-100 dark:bg-blue-900/20',
    textColor: 'text-blue-700 dark:text-blue-400',
    borderColor: 'border-blue-200 dark:border-blue-800',
    iconColor: 'text-blue-600 dark:text-blue-400',
  },
  warning: {
    icon: AlertTriangle,
    bgColor: 'bg-amber-100 dark:bg-amber-900/20',
    textColor: 'text-amber-700 dark:text-amber-400',
    borderColor: 'border-amber-200 dark:border-amber-800',
    iconColor: 'text-amber-600 dark:text-amber-400',
  },
  critical: {
    icon: AlertCircle,
    bgColor: 'bg-red-100 dark:bg-red-900/20',
    textColor: 'text-red-700 dark:text-red-400',
    borderColor: 'border-red-200 dark:border-red-800',
    iconColor: 'text-red-600 dark:text-red-400',
  },
}

export function AIAnomalyBadge({
  anomaly,
  variant = 'badge',
  showConfidence = true,
  onClick,
  className,
}: AIAnomalyBadgeProps) {
  const config = severityConfig[anomaly.severity]
  const Icon = config.icon

  if (variant === 'dot') {
    return (
      <div
        className={cn(
          'relative h-2 w-2 rounded-full',
          anomaly.severity === 'critical' && 'animate-pulse',
          config.bgColor,
          className
        )}
        title={anomaly.description}
      />
    )
  }

  if (variant === 'icon') {
    return (
      <div
        className={cn(
          'inline-flex h-5 w-5 items-center justify-center rounded',
          config.bgColor,
          onClick && 'cursor-pointer hover:opacity-80',
          className
        )}
        onClick={onClick}
        title={anomaly.description}
      >
        <Icon className={cn('h-3 w-3', config.iconColor)} />
      </div>
    )
  }

  return (
    <Popover>
      <PopoverTrigger asChild>
        <Badge
          variant="outline"
          className={cn(
            'gap-1.5 cursor-pointer hover:opacity-80 transition-opacity',
            config.bgColor,
            config.textColor,
            config.borderColor,
            className
          )}
        >
          <Sparkles className="h-3 w-3" />
          <Icon className={cn('h-3 w-3', config.iconColor)} />
          <span className="font-medium">{anomaly.type}</span>
          {showConfidence && (
            <span className="text-xs opacity-70">{anomaly.confidence}%</span>
          )}
        </Badge>
      </PopoverTrigger>
      <PopoverContent className="w-80" align="start">
        <AnomalyDetails anomaly={anomaly} />
      </PopoverContent>
    </Popover>
  )
}

/**
 * AnomalyDetails - Detailed view of an anomaly
 */
function AnomalyDetails({ anomaly }: { anomaly: Anomaly }) {
  const config = severityConfig[anomaly.severity]
  const Icon = config.icon

  const getVarianceIcon = () => {
    if (!anomaly.expected) return null
    const variance = ((anomaly.value - anomaly.expected) / anomaly.expected) * 100
    if (variance > 0) {
      return <TrendingUp className="h-4 w-4 text-red-600 dark:text-red-400" />
    }
    return <TrendingDown className="h-4 w-4 text-green-600 dark:text-green-400" />
  }

  return (
    <div className="space-y-3">
      {/* Header */}
      <div className="flex items-start gap-3">
        <div className={cn('flex h-8 w-8 items-center justify-center rounded-full', config.bgColor)}>
          <Icon className={cn('h-4 w-4', config.iconColor)} />
        </div>
        <div className="flex-1">
          <h4 className="font-semibold text-sm">{anomaly.type}</h4>
          <p className="text-xs text-muted-foreground">
            Detected {new Date(anomaly.detectedAt).toLocaleString()}
          </p>
        </div>
      </div>

      {/* Description */}
      <p className="text-sm">{anomaly.description}</p>

      {/* Values */}
      <div className="space-y-2 rounded-lg bg-slate-50 dark:bg-slate-800 p-3">
        <div className="flex items-center justify-between">
          <span className="text-sm text-muted-foreground">Actual value</span>
          <span className="font-mono text-sm font-medium">{anomaly.value}</span>
        </div>
        {anomaly.expected && (
          <div className="flex items-center justify-between">
            <span className="text-sm text-muted-foreground">Expected value</span>
            <span className="font-mono text-sm">{anomaly.expected}</span>
          </div>
        )}
        {anomaly.variance && (
          <div className="flex items-center justify-between">
            <span className="text-sm text-muted-foreground">Variance</span>
            <div className="flex items-center gap-1">
              {getVarianceIcon()}
              <span className="font-mono text-sm font-medium">{anomaly.variance}%</span>
            </div>
          </div>
        )}
      </div>

      {/* Confidence */}
      <div className="flex items-center justify-between">
        <span className="text-sm text-muted-foreground">AI Confidence</span>
        <div className="flex items-center gap-2">
          <div className="h-1.5 w-20 overflow-hidden rounded-full bg-slate-200 dark:bg-slate-700">
            <div
              className="h-full bg-gradient-to-r from-purple-500 to-indigo-500"
              style={{ width: `${anomaly.confidence}%` }}
            />
          </div>
          <span className="text-sm font-medium">{anomaly.confidence}%</span>
        </div>
      </div>

      {/* Suggestions */}
      {anomaly.suggestions && anomaly.suggestions.length > 0 && (
        <div className="pt-2 border-t border-slate-200 dark:border-slate-700">
          <p className="text-xs font-medium text-muted-foreground mb-2">Suggested actions:</p>
          <ul className="space-y-1">
            {anomaly.suggestions.map((suggestion, i) => (
              <li key={i} className="text-xs flex items-start gap-2">
                <span className="text-purple-600 dark:text-purple-400">•</span>
                <span>{suggestion}</span>
              </li>
            ))}
          </ul>
        </div>
      )}
    </div>
  )
}

/**
 * AnomalyCell - Table cell with anomaly indicator
 */
export interface AnomalyCellProps {
  value: number
  anomalies?: Anomaly[]
  formatValue?: (value: number) => string
  className?: string
}

export function AnomalyCell({
  value,
  anomalies = [],
  formatValue = (v) => v.toString(),
  className,
}: AnomalyCellProps) {
  const hasAnomalies = anomalies.length > 0
  const criticalAnomalies = anomalies.filter((a) => a.severity === 'critical')

  return (
    <div className={cn('flex items-center gap-2', className)}>
      <span className={cn('font-medium', hasAnomalies && 'text-muted-foreground')}>
        {formatValue(value)}
      </span>
      {anomalies.length > 0 && (
        <div className="flex -space-x-1">
          {anomalies.slice(0, 3).map((anomaly) => (
            <AIAnomalyBadge
              key={anomaly.id}
              anomaly={anomaly}
              variant="icon"
              className="border-2 border-background"
            />
          ))}
          {anomalies.length > 3 && (
            <Badge
              variant="outline"
              className="h-5 w-5 p-0 flex items-center justify-center text-xs rounded-full"
            >
              +{anomalies.length - 3}
            </Badge>
          )}
        </div>
      )}
    </div>
  )
}
