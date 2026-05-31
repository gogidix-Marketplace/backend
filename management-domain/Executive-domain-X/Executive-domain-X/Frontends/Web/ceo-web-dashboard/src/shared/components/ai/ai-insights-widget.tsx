import { Card, CardContent, CardHeader } from '@shared/components/ui/card'
import { Badge } from '@shared/components/ui/badge'
import { Button } from '@shared/components/ui/button'
import { cn } from '@shared/utils/cn'
import { Sparkles, MessageSquare, X, TrendingUp, AlertTriangle, Lightbulb, Zap } from 'lucide-react'
import { useState } from 'react'

/**
 * AIInsightsWidget Component
 *
 * Purple gradient card header (#7C4DFF)
 * Insight items with icons and confidence bars
 * "Ask AI" chat button (opens floating widget)
 * Dismissible with confirmation
 */

export type InsightType = 'opportunity' | 'risk' | 'prediction' | 'anomaly' | 'recommendation'

export interface AIInsight {
  id: string
  type: InsightType
  title: string
  description: string
  confidence: number // 0-100
  source: string
  timestamp: string
  actionable?: boolean
  actionLabel?: string
}

export interface AIInsightsWidgetProps {
  insights?: AIInsight[]
  onInsightClick?: (insight: AIInsight) => void
  onDismiss?: () => void
  onAskAI?: () => void
  loading?: boolean
}

const defaultInsights: AIInsight[] = [
  {
    id: '1',
    type: 'opportunity',
    title: 'European Market Expansion',
    description: 'Analysis suggests 23% growth potential in DACH region. Current market share: 12%.',
    confidence: 87,
    source: 'Market Analysis',
    timestamp: '2 hours ago',
    actionable: true,
    actionLabel: 'View Report',
  },
  {
    id: '2',
    type: 'risk',
    title: 'Revenue at Risk',
    description: 'Customer churn forecast increased by 8% in enterprise segment. Key factor: pricing.',
    confidence: 72,
    source: 'Predictive Model',
    timestamp: '4 hours ago',
    actionable: true,
    actionLabel: 'Take Action',
  },
  {
    id: '3',
    type: 'anomaly',
    title: 'Unusual Traffic Pattern',
    description: 'Kenya region showing 340% traffic spike. Investigating: potential bot activity or viral campaign.',
    confidence: 65,
    source: 'Anomaly Detection',
    timestamp: '6 hours ago',
    actionable: true,
    actionLabel: 'Investigate',
  },
]

const typeConfig = {
  opportunity: {
    icon: TrendingUp,
    color: 'text-green-600 dark:text-green-400',
    bgColor: 'bg-green-100 dark:bg-green-900/20',
    label: 'Opportunity',
  },
  risk: {
    icon: AlertTriangle,
    color: 'text-red-600 dark:text-red-400',
    bgColor: 'bg-red-100 dark:bg-red-900/20',
    label: 'Risk',
  },
  prediction: {
    icon: Sparkles,
    color: 'text-purple-600 dark:text-purple-400',
    bgColor: 'bg-purple-100 dark:bg-purple-900/20',
    label: 'Prediction',
  },
  anomaly: {
    icon: Zap,
    color: 'text-amber-600 dark:text-amber-400',
    bgColor: 'bg-amber-100 dark:bg-amber-900/20',
    label: 'Anomaly',
  },
  recommendation: {
    icon: Lightbulb,
    color: 'text-blue-600 dark:text-blue-400',
    bgColor: 'bg-blue-100 dark:bg-blue-900/20',
    label: 'Recommendation',
  },
}

export function AIInsightsWidget({
  insights = defaultInsights,
  onInsightClick,
  onDismiss,
  onAskAI,
  loading = false,
}: AIInsightsWidgetProps) {
  const [dismissed, setDismissed] = useState(false)

  if (dismissed) return null

  return (
    <Card className="border-purple-200 dark:border-purple-800 overflow-hidden">
      {/* Purple Gradient Header */}
      <div className="bg-gradient-to-r from-[#7C4DFF] to-[#B388FF] px-6 py-4">
        <div className="flex items-center justify-between">
          <div className="flex items-center gap-2 text-white">
            <Sparkles className="h-5 w-5" />
            <h3 className="font-semibold">AI Insights</h3>
            <Badge variant="secondary" className="bg-white/20 text-white hover:bg-white/30 border-0">
              {insights.length}
            </Badge>
          </div>
          <div className="flex items-center gap-2">
            <Button
              variant="ghost"
              size="sm"
              className="h-7 text-white hover:bg-white/20"
              onClick={() => {
                setDismissed(true)
                onDismiss?.()
              }}
            >
              <X className="h-4 w-4" />
            </Button>
          </div>
        </div>
      </div>

      <CardContent className="p-4 space-y-3">
        {loading ? (
          <div className="space-y-3">
            {[1, 2, 3].map((i) => (
              <div key={i} className="h-16 bg-slate-100 dark:bg-slate-800 rounded-lg animate-pulse" />
            ))}
          </div>
        ) : (
          insights.map((insight) => {
            const config = typeConfig[insight.type]
            const Icon = config.icon

            return (
              <div
                key={insight.id}
                className="p-3 rounded-lg border hover:border-purple-300 dark:hover:border-purple-700 hover:bg-purple-50/50 dark:hover:bg-purple-950/20 transition-colors cursor-pointer"
                onClick={() => onInsightClick?.(insight)}
              >
                <div className="flex items-start gap-3">
                  <div className={cn('flex h-8 w-8 items-center justify-center rounded-full', config.bgColor)}>
                    <Icon className={cn('h-4 w-4', config.color)} />
                  </div>
                  <div className="flex-1 min-w-0">
                    <div className="flex items-center gap-2 mb-1">
                      <h4 className="font-medium text-sm">{insight.title}</h4>
                      <Badge variant="outline" className="text-xs h-5">
                        {config.label}
                      </Badge>
                    </div>
                    <p className="text-xs text-muted-foreground line-clamp-2">{insight.description}</p>

                    {/* Confidence Bar */}
                    <div className="mt-2">
                      <div className="flex items-center justify-between text-xs text-muted-foreground mb-1">
                        <span>Confidence</span>
                        <span>{insight.confidence}%</span>
                      </div>
                      <div className="h-1.5 w-full overflow-hidden rounded-full bg-slate-200 dark:bg-slate-700">
                        <div
                          className="h-full bg-gradient-to-r from-purple-500 to-pink-500"
                          style={{ width: `${insight.confidence}%` }}
                        />
                      </div>
                    </div>

                    {/* Footer */}
                    <div className="flex items-center justify-between mt-2">
                      <span className="text-xs text-muted-foreground">
                        {insight.source} • {insight.timestamp}
                      </span>
                      {insight.actionable && (
                        <Button variant="ghost" size="sm" className="h-6 text-xs text-purple-600 hover:text-purple-700">
                          {insight.actionLabel}
                        </Button>
                      )}
                    </div>
                  </div>
                </div>
              </div>
            )
          })
        )}

        {/* Ask AI Button */}
        <Button
          variant="outline"
          className="w-full border-purple-200 text-purple-700 hover:bg-purple-50 hover:text-purple-800"
          onClick={onAskAI}
        >
          <MessageSquare className="h-4 w-4 mr-2" />
          Ask AI
        </Button>
      </CardContent>
    </Card>
  )
}

/**
 * AI Chat Button - Floating button for quick AI access
 */
export function AIChatButton({ onClick }: { onClick?: () => void }) {
  return (
    <button
      onClick={onClick}
      className="fixed bottom-6 right-6 h-14 w-14 rounded-full bg-gradient-to-br from-[#7C4DFF] to-[#B388FF] text-white shadow-lg hover:shadow-xl hover:scale-110 transition-all flex items-center justify-center z-50"
      aria-label="Ask AI"
    >
      <MessageSquare className="h-6 w-6" />
      <span className="absolute -top-1 -right-1 h-4 w-4 rounded-full bg-red-500 border-2 border-background animate-pulse" />
    </button>
  )
}
