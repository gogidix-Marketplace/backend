import * as React from 'react'
import { Card, CardContent, CardHeader, CardTitle } from '@shared/components/ui/card'
import { Badge } from '@shared/components/ui/badge'
import { Button } from '@shared/components/ui/button'
import { cn } from '@shared/utils/cn'
import {
  Sparkles,
  TrendingUp,
  AlertTriangle,
  Lightbulb,
  X,
  ChevronRight,
} from 'lucide-react'

/**
 * AI Insight Card - Purple-branded insight with confidence
 *
 * Features:
 * - Purple gradient header (#7C4DFF)
 * - AI glow effect
 * - Confidence bar
 * - Insight items with icons
 * - Dismissible with confirmation
 * - "Ask AI" chat button
 */

export type InsightType = 'opportunity' | 'risk' | 'anomaly' | 'prediction' | 'recommendation'

export interface AIInsight {
  id: string
  type: InsightType
  title: string
  description: string
  confidence: number // 0-100
  impact?: 'high' | 'medium' | 'low'
  actionable?: boolean
  actions?: Array<{
    label: string
    onClick: () => void
  }>
  metadata?: Record<string, string | number>
  timestamp?: Date
}

export interface AIInsightCardProps {
  insight: AIInsight
  onDismiss?: (id: string) => void
  onAskAI?: () => void
  showConfidence?: boolean
  variant?: 'default' | 'compact' | 'detailed'
  className?: string
}

const insightConfig = {
  opportunity: {
    icon: Lightbulb,
    iconBg: 'bg-amber-100 dark:bg-amber-900/20',
    iconColor: 'text-amber-600 dark:text-amber-400',
    gradient: 'from-amber-500 to-orange-500',
  },
  risk: {
    icon: AlertTriangle,
    iconBg: 'bg-red-100 dark:bg-red-900/20',
    iconColor: 'text-red-600 dark:text-red-400',
    gradient: 'from-red-500 to-rose-500',
  },
  anomaly: {
    icon: AlertTriangle,
    iconBg: 'bg-purple-100 dark:bg-purple-900/20',
    iconColor: 'text-purple-600 dark:text-purple-400',
    gradient: 'from-purple-500 to-pink-500',
  },
  prediction: {
    icon: TrendingUp,
    iconBg: 'bg-blue-100 dark:bg-blue-900/20',
    iconColor: 'text-blue-600 dark:text-blue-400',
    gradient: 'from-blue-500 to-cyan-500',
  },
  recommendation: {
    icon: Sparkles,
    iconBg: 'bg-purple-100 dark:bg-purple-900/20',
    iconColor: 'text-purple-600 dark:text-purple-400',
    gradient: 'from-purple-500 to-indigo-500',
  },
}

const aiGradient = 'from-[#7C4DFF] to-[#B388FF]'
const aiGlow = 'shadow-[0_0_20px_rgba(124,77,255,0.3)]'

export function AIInsightCard({
  insight,
  onDismiss,
  onAskAI,
  showConfidence = true,
  variant = 'default',
  className,
}: AIInsightCardProps) {
  const config = insightConfig[insight.type]
  const Icon = config.icon

  const getImpactBadge = () => {
    if (!insight.impact) return null
    const impactColors = {
      high: 'bg-red-100 text-red-700 dark:bg-red-900/20 dark:text-red-400',
      medium: 'bg-amber-100 text-amber-700 dark:bg-amber-900/20 dark:text-amber-400',
      low: 'bg-green-100 text-green-700 dark:bg-green-900/20 dark:text-green-400',
    }
    return (
      <Badge variant="outline" className={impactColors[insight.impact]}>
        {insight.impact} impact
      </Badge>
    )
  }

  if (variant === 'compact') {
    return (
      <Card className={cn('ai-gradient bg-gradient-to-r from-purple-50 to-indigo-50 dark:from-purple-950/20 dark:to-indigo-950/20 border-purple-200 dark:border-purple-800', className)}>
        <CardContent className="p-3">
          <div className="flex items-start gap-3">
            <div className={cn('flex h-8 w-8 items-center justify-center rounded-full', config.iconBg)}>
              <Icon className={cn('h-4 w-4', config.iconColor)} />
            </div>
            <div className="flex-1 min-w-0">
              <p className="text-sm font-medium truncate">{insight.title}</p>
              <p className="text-xs text-muted-foreground line-clamp-1">{insight.description}</p>
            </div>
            {showConfidence && (
              <div className="flex items-center gap-1">
                <div className="h-1.5 w-12 overflow-hidden rounded-full bg-slate-200 dark:bg-slate-700">
                  <div
                    className="h-full bg-gradient-to-r from-purple-500 to-indigo-500"
                    style={{ width: `${insight.confidence}%` }}
                  />
                </div>
                <span className="text-xs text-muted-foreground w-8 text-right">{insight.confidence}%</span>
              </div>
            )}
          </div>
        </CardContent>
      </Card>
    )
  }

  return (
    <Card
      className={cn(
        'ai-glow overflow-hidden',
        'bg-gradient-to-br from-white to-purple-50/50 dark:from-slate-900 dark:to-purple-950/20',
        'border-purple-200 dark:border-purple-800',
        className
      )}
    >
      {/* Gradient Header */}
      <div className={cn('h-1.5 w-full bg-gradient-to-r', aiGradient)} />

      <CardHeader className="pb-3">
        <div className="flex items-start justify-between">
          <div className="flex items-start gap-3 flex-1">
            <div className={cn('flex h-10 w-10 items-center justify-center rounded-full', config.iconBg)}>
              <Icon className={cn('h-5 w-5', config.iconColor)} />
            </div>
            <div className="flex-1 space-y-1">
              <div className="flex items-center gap-2">
                <CardTitle className="text-base">{insight.title}</CardTitle>
                {getImpactBadge()}
                {insight.actionable && (
                  <Badge variant="outline" className="text-xs bg-purple-100 dark:bg-purple-900/20 text-purple-700 dark:text-purple-400 border-purple-200 dark:border-purple-800">
                    Actionable
                  </Badge>
                )}
              </div>
              <p className="text-sm text-muted-foreground">{insight.description}</p>
            </div>
          </div>

          {onDismiss && (
            <Button
              variant="ghost"
              size="sm"
              onClick={() => onDismiss(insight.id)}
              className="h-8 w-8 p-0 text-muted-foreground hover:text-destructive"
            >
              <X className="h-4 w-4" />
            </Button>
          )}
        </div>

        {/* Confidence Bar */}
        {showConfidence && (
          <div className="mt-3 space-y-1">
            <div className="flex items-center justify-between text-xs">
              <span className="text-muted-foreground">AI Confidence</span>
              <span className="font-medium">{insight.confidence}%</span>
            </div>
            <div className="h-2 w-full overflow-hidden rounded-full bg-slate-200 dark:bg-slate-700">
              <div
                className={cn('h-full bg-gradient-to-r transition-all duration-500', aiGradient)}
                style={{ width: `${insight.confidence}%` }}
              />
            </div>
          </div>
        )}
      </CardHeader>

      {/* Actions */}
      {(insight.actions || onAskAI) && (
        <CardContent className="pt-0 space-y-2">
          {insight.actions && (
            <div className="flex flex-wrap gap-2">
              {insight.actions.map((action, i) => (
                <Button
                  key={i}
                  variant="outline"
                  size="sm"
                  onClick={action.onClick}
                  className="border-purple-200 dark:border-purple-800 hover:bg-purple-50 dark:hover:bg-purple-950/30"
                >
                  {action.label}
                  <ChevronRight className="ml-1 h-3 w-3" />
                </Button>
              ))}
            </div>
          )}

          {onAskAI && (
            <Button
              variant="ghost"
              size="sm"
              onClick={onAskAI}
              className="w-full text-purple-600 dark:text-purple-400 hover:bg-purple-50 dark:hover:bg-purple-950/30 justify-start"
            >
              <Sparkles className="mr-2 h-4 w-4" />
              Ask AI for more details
            </Button>
          )}

          {insight.timestamp && (
            <p className="text-xs text-muted-foreground">
              {new Date(insight.timestamp).toLocaleString()}
            </p>
          )}
        </CardContent>
      )}
    </Card>
  )
}

/**
 * AIInsightList - List of AI insights
 */
export interface AIInsightListProps {
  insights: AIInsight[]
  onDismiss?: (id: string) => void
  onAskAI?: () => void
  maxVisible?: number
  className?: string
}

export function AIInsightList({
  insights,
  onDismiss,
  onAskAI,
  maxVisible,
  className,
}: AIInsightListProps) {
  const visible = maxVisible ? insights.slice(0, maxVisible) : insights

  if (visible.length === 0) {
    return (
      <div className={cn('text-center py-8', className)}>
        <div className="mx-auto flex h-12 w-12 items-center justify-center rounded-full bg-purple-100 dark:bg-purple-900/20 mb-3">
          <Sparkles className="h-6 w-6 text-purple-600 dark:text-purple-400" />
        </div>
        <p className="text-sm text-muted-foreground">No AI insights available</p>
      </div>
    )
  }

  return (
    <div className={cn('space-y-4', className)}>
      {visible.map((insight) => (
        <AIInsightCard
          key={insight.id}
          insight={insight}
          onDismiss={onDismiss}
          onAskAI={onAskAI}
        />
      ))}
    </div>
  )
}
