/**
 * AI Components
 *
 * Production-grade AI-branded components with purple gradient theme (#7C4DFF)
 * for displaying AI insights, predictions, and anomalies
 */

// AI Insight Card
export { AIInsightCard, AIInsightList } from './ai-insight-card'
export type { AIInsight, AIInsightCardProps, AIInsightListProps, InsightType } from './ai-insight-card'

// AI Chat Widget
export { AIChatWidget } from './ai-chat-widget'
export type { ChatMessage, AIChatWidgetProps } from './ai-chat-widget'

// AI Confidence Bar
export { AIConfidenceBar, AIConfidenceScore } from './ai-confidence-bar'
export type { AIConfidenceBarProps, AIConfidenceScoreProps } from './ai-confidence-bar'

// AI Predictive Indicator
export { AIPredictiveIndicator, AIPredictionBadge } from './ai-predictive-indicator'
export type { Prediction, AIPredictiveIndicatorProps, AIPredictionBadgeProps } from './ai-predictive-indicator'

// AI Anomaly Badge
export { AIAnomalyBadge, AnomalyCell } from './ai-anomaly-badge'
export type { Anomaly, AnomalySeverity, AIAnomalyBadgeProps, AnomalyCellProps } from './ai-anomaly-badge'
