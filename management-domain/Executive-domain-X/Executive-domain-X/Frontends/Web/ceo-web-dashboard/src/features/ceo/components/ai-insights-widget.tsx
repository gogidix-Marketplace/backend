import { Card, CardContent, CardHeader } from '@shared/components/ui/card'
import { Badge } from '@shared/components/ui/badge'
import { Button } from '@shared/components/ui/button'
import { cn } from '@shared/utils/cn'
import { Sparkles, ChevronRight, Lightbulb, AlertTriangle, TrendingUp } from 'lucide-react'
import * as React from 'react'
import { useAIInsights } from '@shared/services/api'

export type AIInsightType = 'opportunity' | 'risk' | 'prediction' | 'recommendation'

export type AIInsight = {
  id: string
  type: AIInsightType
  title: string
  description: string
  confidence: number
  impact: 'high' | 'medium' | 'low'
  actions?: Array<{
    label: string
    onClick: () => void
  }>
  timestamp: Date
}

// Fallback mock data for when API is unavailable
const fallbackInsights: AIInsight[] = [
  {
    id: '1',
    type: 'opportunity',
    title: 'Market Expansion Opportunity',
    description: 'Analysis indicates favorable conditions for expanding into South Africa with projected 25% ROI within 18 months.',
    confidence: 87,
    impact: 'high',
    actions: [
      { label: 'View Analysis', onClick: () => {} },
      { label: 'Schedule Review', onClick: () => {} },
    ],
    timestamp: new Date(Date.now() - 2 * 60 * 60 * 1000),
  },
  {
    id: '2',
    type: 'risk',
    title: 'Revenue at Risk - European Region',
    description: 'European region showing 12% decline in Q2 projections. Recommend immediate investigation.',
    confidence: 92,
    impact: 'high',
    actions: [
      { label: 'Investigate', onClick: () => {} },
      { label: 'View Report', onClick: () => {} },
    ],
    timestamp: new Date(Date.now() - 5 * 60 * 60 * 1000),
  },
  {
    id: '3',
    type: 'prediction',
    title: 'Q3 Revenue Forecast Update',
    description: 'Updated projections suggest Q3 revenue will exceed targets by 8-12% driven by strong performance.',
    confidence: 78,
    impact: 'medium',
    actions: [
      { label: 'View Forecast', onClick: () => {} },
    ],
    timestamp: new Date(Date.now() - 24 * 60 * 60 * 1000),
  },
]

/**
 * Transform API IntelligenceInsight to AIInsight format
 */
function transformApiInsight(apiInsight: any): AIInsight {
  // Map insight type
  const typeMap: Record<string, AIInsightType> = {
    opportunity: 'opportunity',
    risk: 'risk',
    warning: 'risk',
    prediction: 'prediction',
    forecast: 'prediction',
    recommendation: 'recommendation',
    suggestion: 'recommendation',
  }

  // Map impact
  const impactMap: Record<string, AIInsight['impact']> = {
    high: 'high',
    critical: 'high',
    medium: 'medium',
    moderate: 'medium',
    low: 'low',
    minor: 'low',
  }

  return {
    id: apiInsight.id || crypto.randomUUID?.() || Math.random().toString(36),
    type: typeMap[apiInsight.type?.toLowerCase()] || 'recommendation',
    title: apiInsight.title || apiInsight.heading || 'AI Insight',
    description: apiInsight.description || apiInsight.content || apiInsight.message || '',
    confidence: apiInsight.confidence ?? apiInsight.score ?? 75,
    impact: impactMap[apiInsight.impact?.toLowerCase()] || 'medium',
    timestamp: apiInsight.timestamp ? new Date(apiInsight.timestamp) : new Date(),
  }
}

const typeConfig = {
  opportunity: {
    icon: Lightbulb,
    bgLight: 'bg-amber-50 dark:bg-amber-950/20',
    iconBg: 'bg-amber-100 dark:bg-amber-900/30',
    iconColor: 'text-amber-600 dark:text-amber-400',
  },
  risk: {
    icon: AlertTriangle,
    bgLight: 'bg-red-50 dark:bg-red-950/20',
    iconBg: 'bg-red-100 dark:bg-red-900/30',
    iconColor: 'text-red-600 dark:text-red-400',
  },
  prediction: {
    icon: TrendingUp,
    bgLight: 'bg-blue-50 dark:bg-blue-950/20',
    iconBg: 'bg-blue-100 dark:bg-blue-900/30',
    iconColor: 'text-blue-600 dark:text-blue-400',
  },
  recommendation: {
    icon: Sparkles,
    bgLight: 'bg-purple-50 dark:bg-purple-950/20',
    iconBg: 'bg-purple-100 dark:bg-purple-900/30',
    iconColor: 'text-purple-600 dark:text-purple-400',
  },
}

export interface AIInsightsWidgetProps {
  onDismiss?: (id: string) => void
  onAskAI?: () => void
  maxVisible?: number
  className?: string
  /**
   * Force use of mock data (for testing/demos)
   * @default false - will try API first, fall back to mock on error
   */
  useMockData?: boolean
}

export function AIInsightsWidget({
  onDismiss,
  onAskAI,
  maxVisible = 3,
  className,
  useMockData = false,
}: AIInsightsWidgetProps) {
  const [dismissed, setDismissed] = React.useState<Set<string>>(new Set())

  // Fetch AI insights from API
  const { insights, loading } = useAIInsights(
    'intelligence-analysis',
    useMockData ? undefined : { limit: 10 }
  )

  // Memoize transformed insights to prevent flickering
  const insightsData = React.useMemo(() => {
    if (!useMockData && insights.length > 0) {
      return insights.map(transformApiInsight)
    }
    return fallbackInsights
  }, [insights, useMockData])

  // Memoize visible insights
  const visibleInsights = React.useMemo(() => {
    return insightsData
      .filter((i) => !dismissed.has(i.id))
      .slice(0, maxVisible)
  }, [insightsData, dismissed, maxVisible])

  // Memoize handler
  const handleDismiss = React.useCallback((id: string) => {
    setDismissed((prev) => new Set([...prev, id]))
    onDismiss?.(id)
  }, [onDismiss])

  return (
    <Card
      className={cn(
        'bg-gradient-to-br from-white to-purple-50/50 dark:from-slate-900 dark:to-purple-950/20 border-purple-200 dark:border-purple-800',
        className
      )}
    >
      <CardHeader className="relative pb-3">
        <div className="absolute top-0 left-0 right-0 h-1 bg-gradient-to-r from-[#7C4DFF] to-[#B388FF]" />
        <div className="flex items-center justify-between pt-2">
          <div className="flex items-center gap-2">
            <div className="flex h-7 w-7 items-center justify-center rounded-full bg-gradient-to-r from-purple-600 to-indigo-600">
              <Sparkles className="h-3.5 w-3.5 text-white" />
            </div>
            <div>
              <h3 className="text-sm font-semibold">AI Insights</h3>
              <p className="text-xs text-muted-foreground">
                {loading && !useMockData
                  ? 'Loading insights...'
                  : `${insightsData.filter((i) => !dismissed.has(i.id)).length} insights available`
                }
              </p>
            </div>
          </div>
          <Badge className="bg-purple-100 dark:bg-purple-900/20 text-purple-700 dark:text-purple-400 border-purple-200 dark:border-purple-800 text-[10px]">
            Beta
          </Badge>
        </div>
      </CardHeader>

      <CardContent className="space-y-2">
        {loading && !useMockData ? (
          <div className="text-center py-6">
            <Sparkles className="h-7 w-7 mx-auto mb-2 text-purple-400 animate-pulse" />
            <p className="text-sm text-muted-foreground">Loading AI insights...</p>
          </div>
        ) : visibleInsights.length === 0 ? (
          <div className="text-center py-6">
            <Sparkles className="h-7 w-7 mx-auto mb-2 text-purple-400" />
            <p className="text-sm text-muted-foreground">No insights to display</p>
          </div>
        ) : (
          <>
            {visibleInsights.map((insight) => {
              const config = typeConfig[insight.type]
              const Icon = config.icon

              return (
                <div
                  key={insight.id}
                  className={`group relative rounded-lg border border-purple-100 dark:border-purple-900/30 ${config.bgLight} p-3 transition-colors hover:border-purple-300 dark:hover:border-purple-700`}
                >
                  <button
                    onClick={() => handleDismiss(insight.id)}
                    className="absolute top-2 right-2 opacity-0 group-hover:opacity-100 transition-opacity p-1 hover:bg-purple-200 dark:hover:bg-purple-800 rounded"
                  >
                    <svg className="h-3 w-3 text-muted-foreground" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                      <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M6 18L18 6M6 6l12 12" />
                    </svg>
                  </button>

                  <div className="flex items-start gap-2.5 pr-6">
                    <div className={`flex h-7 w-7 flex-shrink-0 items-center justify-center rounded-full ${config.iconBg}`}>
                      <Icon className={`h-3.5 w-3.5 ${config.iconColor}`} />
                    </div>

                    <div className="flex-1 min-w-0 space-y-1.5">
                      <p className="text-xs font-medium line-clamp-1">{insight.title}</p>
                      <p className="text-[10px] sm:text-xs text-muted-foreground line-clamp-2">
                        {insight.description}
                      </p>

                      {/* Confidence Bar */}
                      <div className="flex items-center gap-2">
                        <div className="flex-1 h-1 overflow-hidden rounded-full bg-slate-200 dark:bg-slate-700">
                          <div
                            className="h-full bg-gradient-to-r from-purple-500 to-indigo-500 rounded-full"
                            style={{ width: `${insight.confidence}%` }}
                          />
                        </div>
                        <span className="text-[10px] text-muted-foreground w-8 text-right">
                          {insight.confidence}%
                        </span>
                      </div>

                      {/* Actions */}
                      {insight.actions && (
                        <div className="flex gap-1">
                          {insight.actions.map((action, i) => (
                            <Button
                              key={i}
                              variant="ghost"
                              size="sm"
                              className="h-6 px-2 text-[10px] text-purple-600 dark:text-purple-400 hover:bg-purple-100 dark:hover:bg-purple-900/30"
                              onClick={action.onClick}
                            >
                              {action.label}
                              <ChevronRight className="ml-0.5 h-3 w-3" />
                            </Button>
                          ))}
                        </div>
                      )}
                    </div>
                  </div>
                </div>
              )
            })}

            {/* Ask AI Button */}
            {onAskAI && (
              <Button
                variant="outline"
                className="w-full border-purple-200 dark:border-purple-800 text-purple-600 dark:text-purple-400 hover:bg-purple-50 dark:hover:bg-purple-950/30 justify-start text-sm"
                onClick={onAskAI}
              >
                <Sparkles className="mr-2 h-4 w-4" />
                Ask AI about your data
              </Button>
            )}
          </>
        )}
      </CardContent>
    </Card>
  )
}
