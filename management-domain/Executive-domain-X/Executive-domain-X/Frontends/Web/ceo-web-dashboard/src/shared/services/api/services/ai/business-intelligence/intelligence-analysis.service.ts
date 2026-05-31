/**
 * AI Intelligence Analysis Service
 *
 * Service: intelligence-analysis-service (Port 7007)
 * Base Path: /api/v1/ai/intelligence
 *
 * Features:
 * - Business intelligence insights generation
 * - Intelligence-driven recommendations
 * - Trend analysis and pattern detection
 * - Alert management for critical insights
 * - Multi-source intelligence aggregation
 */

import { aiApiClient } from '../../../client/axios-client'
import type { TimePeriod, DateRange } from '../../../client/types'

// ============================================================================
// Types
// ============================================================================

/**
 * Insight types categorize the nature of the intelligence
 */
export type InsightType =
  | 'PERFORMANCE'
  | 'OPPORTUNITY'
  | 'RISK'
  | 'ANOMALY'
  | 'CORRELATION'
  | 'PREDICTION'
  | 'COMPETITIVE'
  | 'MARKET'

/**
 * Priority levels for insights
 */
export type InsightPriority = 'CRITICAL' | 'HIGH' | 'MEDIUM' | 'LOW'

/**
 * Source of the intelligence data
 */
export type InsightSource =
  | 'SALES'
  | 'MARKETING'
  | 'FINANCE'
  | 'OPERATIONS'
  | 'CUSTOMER'
  | 'COMPETITOR'
  | 'MARKET'
  | 'EXTERNAL'
  | 'AGGREGATED'

/**
 * Trend direction for intelligence data
 */
export type TrendDirection = 'UPWARD' | 'DOWNWARD' | 'STABLE' | 'VOLATILE'

/**
 * Alert severity levels
 */
export type AlertSeverity = 'CRITICAL' | 'WARNING' | 'INFO'

/**
 * Alert status
 */
export type AlertStatus = 'ACTIVE' | 'ACKNOWLEDGED' | 'RESOLVED' | 'DISMISSED'

/**
 * Business intelligence insight
 */
export interface IntelligenceInsight {
  id: string
  type: InsightType
  priority: InsightPriority
  title: string
  description: string
  summary: string

  // Source information
  source: InsightSource
  sourceDetails?: {
    domain?: string
    service?: string
    dataSource?: string
  }

  // Time context
  detectedAt: string
  period?: DateRange
  timeToImpact?: number // Days until impact

  // Metrics and data
  metrics: {
    currentValue: number
    previousValue?: number
    change?: number
    changePercent?: number
    threshold?: number
    unit?: string
  }

  // Confidence and reliability
  confidence: number // 0-100
  reliability: 'HIGH' | 'MEDIUM' | 'LOW'
  sampleSize?: number

  // Context and analysis
  context?: {
    factors?: string[]
    relatedMetrics?: string[]
    assumptions?: string[]
  }

  // Recommendations
  recommendations?: string[]

  // Status
  status: 'NEW' | 'REVIEWED' | 'ACTIONED' | 'ARCHIVED'

  // Metadata
  tags?: string[]
  category?: string
}

/**
 * Intelligence trend data
 */
export interface InsightTrend {
  id: string
  name: string
  description?: string
  direction: TrendDirection
  strength: number // 0-100, how strong the trend is

  // Trend data
  dataPoints: {
    period: string
    value: number
    movingAverage?: number
  }[]

  // Analysis
  significance: 'HIGH' | 'MEDIUM' | 'LOW'
  seasonality?: {
    detected: boolean
    pattern?: string
    period?: number
  }

  // Projection
  projection?: {
    shortTerm: string // Description
    longTerm: string // Description
    confidence: number
  }

  // Related insights
  relatedInsightIds?: string[]
}

/**
 * Intelligence alert
 */
export interface IntelligenceAlert {
  id: string
  severity: AlertSeverity
  status: AlertStatus
  title: string
  message: string

  // Associated insight
  insightId?: string
  insight?: IntelligenceInsight

  // Timestamps
  triggeredAt: string
  acknowledgedAt?: string
  resolvedAt?: string

  // Alert details
  condition: {
    metric: string
    operator: 'GREATER_THAN' | 'LESS_THAN' | 'EQUALS' | 'CHANGED_BY'
    threshold: number
    currentValue: number
  }

  // Actions
  suggestedActions?: string[]
  actionsTaken?: string[]

  // Assignment
  assignedTo?: string
  dueDate?: string

  // Metadata
  category?: string
  tags?: string[]
}

/**
 * Intelligence recommendation
 */
export interface IntelligenceRecommendation {
  id: string
  title: string
  description: string

  // Type and priority
  type: 'STRATEGIC' | 'TACTICAL' | 'OPERATIONAL'
  priority: InsightPriority
  category: string

  // Rationale
  rationale: string
  basedOnInsightIds: string[]
  insights?: IntelligenceInsight[]

  // Expected impact
  expectedImpact: {
    area: string
    description: string
    estimatedValue?: number
    confidence: number
  }

  // Implementation
  effort: 'LOW' | 'MEDIUM' | 'HIGH'
  timeframe: string
  dependencies?: string[]

  // Status
  status: 'PENDING' | 'IN_PROGRESS' | 'IMPLEMENTED' | 'REJECTED' | 'DEFERRED'

  // Timestamps
  createdAt: string
  expiresAt?: string
  implementedAt?: string

  // Metadata
  tags?: string[]
}

/**
 * Request for running intelligence analysis
 */
export interface AnalysisRequest {
  // Analysis scope
  domains?: string[] // Domains to analyze
  sources?: InsightSource[] // Data sources to include
  timeRange?: DateRange

  // Analysis options
  includeHistorical?: boolean
  lookbackPeriod?: number // Days
  confidenceThreshold?: number // Minimum confidence level

  // Filters
  types?: InsightType[]
  priorities?: InsightPriority[]
  categories?: string[]

  // Output options
  includeRecommendations?: boolean
  maxResults?: number
}

/**
 * Response from intelligence analysis
 */
export interface AnalysisResponse {
  analysisId: string
  generatedAt: string
  timeRange: DateRange
  sourcesAnalyzed: InsightSource[]

  // Summary
  summary: {
    totalInsights: number
    criticalCount: number
    highPriorityCount: number
    newInsights: number
    trendsDetected: number
  }

  // Results
  insights: IntelligenceInsight[]
  trends: InsightTrend[]
  recommendations: IntelligenceRecommendation[]

  // Metadata
  analysisDuration: number // Milliseconds
  dataPointsAnalyzed: number
  confidence: number
}

/**
 * Query parameters for insights
 */
export interface InsightsQuery {
  type?: InsightType
  priority?: InsightPriority
  source?: InsightSource
  status?: string
  startDate?: string
  endDate?: string
  minConfidence?: number
  limit?: number
  offset?: number
  sortBy?: 'detectedAt' | 'priority' | 'confidence'
  sortOrder?: 'asc' | 'desc'
}

/**
 * Query parameters for alerts
 */
export interface AlertsQuery {
  severity?: AlertSeverity
  status?: AlertStatus
  category?: string
  startDate?: string
  endDate?: string
  assignedTo?: string
  limit?: number
  offset?: number
}

/**
 * Query parameters for trends
 */
export interface TrendsQuery {
  source?: InsightSource
  direction?: TrendDirection
  minSignificance?: 'HIGH' | 'MEDIUM' | 'LOW'
  limit?: number
}

// ============================================================================
// Service Class
// ============================================================================

class IntelligenceAnalysisService {
  private readonly basePath = '/api/v1/ai/intelligence'

  /**
   * Get all business intelligence insights
   * Supports filtering and pagination
   */
  async getInsights(query?: InsightsQuery): Promise<{
    data: IntelligenceInsight[]
    total: number
    limit: number
    offset: number
  }> {
    const response = await aiApiClient.get<{
      data: IntelligenceInsight[]
      total: number
      limit: number
      offset: number
    }>(`${this.basePath}/insights`, { params: query })
    return response.data
  }

  /**
   * Get a specific insight by ID
   */
  async getInsightById(id: string): Promise<IntelligenceInsight> {
    const response = await aiApiClient.get<IntelligenceInsight>(
      `${this.basePath}/insights/${id}`
    )
    return response.data
  }

  /**
   * Run new intelligence analysis
   * Generates insights based on current data
   */
  async runAnalysis(request: AnalysisRequest): Promise<AnalysisResponse> {
    const response = await aiApiClient.post<AnalysisResponse>(
      `${this.basePath}/analyze`,
      request
    )
    return response.data
  }

  /**
   * Get quick analysis with default parameters
   * Convenience method for dashboard widgets
   */
  async getQuickAnalysis(
    lookbackDays: number = 30
  ): Promise<AnalysisResponse> {
    return this.runAnalysis({
      lookbackPeriod: lookbackDays,
      includeHistorical: true,
      includeRecommendations: true,
      confidenceThreshold: 70,
      maxResults: 20,
    })
  }

  /**
   * Get intelligence trends
   * Analyzes trends across various metrics
   */
  async getTrends(query?: TrendsQuery): Promise<InsightTrend[]> {
    const response = await aiApiClient.get<InsightTrend[]>(
      `${this.basePath}/trends`,
      { params: query }
    )
    return response.data
  }

  /**
   * Get trends by source
   */
  async getTrendsBySource(source: InsightSource): Promise<InsightTrend[]> {
    return this.getTrends({ source })
  }

  /**
   * Get intelligence-driven recommendations
   */
  async getRecommendations(filters?: {
    type?: 'STRATEGIC' | 'TACTICAL' | 'OPERATIONAL'
    priority?: InsightPriority
    status?: string
    category?: string
    limit?: number
  }): Promise<IntelligenceRecommendation[]> {
    const response = await aiApiClient.get<IntelligenceRecommendation[]>(
      `${this.basePath}/recommendations`,
      { params: filters }
    )
    return response.data
  }

  /**
   * Get recommendations by priority
   */
  async getRecommendationsByPriority(
    priority: InsightPriority
  ): Promise<IntelligenceRecommendation[]> {
    return this.getRecommendations({ priority })
  }

  /**
   * Get intelligence alerts
   */
  async getAlerts(query?: AlertsQuery): Promise<{
    data: IntelligenceAlert[]
    total: number
    criticalCount: number
    warningCount: number
    infoCount: number
  }> {
    const response = await aiApiClient.get<{
      data: IntelligenceAlert[]
      total: number
      criticalCount: number
      warningCount: number
      infoCount: number
    }>(`${this.basePath}/alerts`, { params: query })
    return response.data
  }

  /**
   * Get active alerts only
   */
  async getActiveAlerts(): Promise<IntelligenceAlert[]> {
    const response = await this.getAlerts({ status: 'ACTIVE' })
    return response.data
  }

  /**
   * Acknowledge an alert
   */
  async acknowledgeAlert(
    alertId: string,
    comment?: string
  ): Promise<IntelligenceAlert> {
    const response = await aiApiClient.post<IntelligenceAlert>(
      `${this.basePath}/alerts/${alertId}/acknowledge`,
      { comment }
    )
    return response.data
  }

  /**
   * Resolve an alert
   */
  async resolveAlert(
    alertId: string,
    resolution: string
  ): Promise<IntelligenceAlert> {
    const response = await aiApiClient.post<IntelligenceAlert>(
      `${this.basePath}/alerts/${alertId}/resolve`,
      { resolution }
    )
    return response.data
  }

  /**
   * Dismiss an alert
   */
  async dismissAlert(alertId: string): Promise<void> {
    await aiApiClient.post(`${this.basePath}/alerts/${alertId}/dismiss`)
  }

  /**
   * Update insight status
   */
  async updateInsightStatus(
    insightId: string,
    status: 'NEW' | 'REVIEWED' | 'ACTIONED' | 'ARCHIVED'
  ): Promise<IntelligenceInsight> {
    const response = await aiApiClient.patch<IntelligenceInsight>(
      `${this.basePath}/insights/${insightId}/status`,
      { status }
    )
    return response.data
  }

  /**
   * Update recommendation status
   */
  async updateRecommendationStatus(
    recommendationId: string,
    status: 'PENDING' | 'IN_PROGRESS' | 'IMPLEMENTED' | 'REJECTED' | 'DEFERRED',
    notes?: string
  ): Promise<IntelligenceRecommendation> {
    const response = await aiApiClient.patch<IntelligenceRecommendation>(
      `${this.basePath}/recommendations/${recommendationId}/status`,
      { status, notes }
    )
    return response.data
  }

  /**
   * Get intelligence summary
   * High-level overview of intelligence state
   */
  async getIntelligenceSummary(): Promise<{
    totalInsights: number
    activeAlerts: number
    pendingRecommendations: number
    criticalIssues: number
    trendsDetected: number
    lastAnalysisTime: string
    healthScore: number // 0-100
  }> {
    const response = await aiApiClient.get(
      `${this.basePath}/summary`
    )
    return response.data
  }

  /**
   * Get insight categories
   */
  async getInsightCategories(): Promise<{
    category: string
    count: number
    lastUpdated: string
  }[]> {
    const response = await aiApiClient.get(
      `${this.basePath}/categories`
    )
    return response.data
  }

  // ========================================================================
  // Helper Methods
  // ========================================================================

  /**
   * Get color for insight priority
   */
  getPriorityColor(priority: InsightPriority): string {
    switch (priority) {
      case 'CRITICAL':
        return '#F44336' // Red
      case 'HIGH':
        return '#FF9800' // Orange
      case 'MEDIUM':
        return '#2196F3' // Blue
      case 'LOW':
        return '#4CAF50' // Green
      default:
        return '#9E9E9E' // Gray
    }
  }

  /**
   * Get color for insight type
   */
  getInsightTypeColor(type: InsightType): string {
    const colors: Record<InsightType, string> = {
      PERFORMANCE: '#2196F3',
      OPPORTUNITY: '#4CAF50',
      RISK: '#F44336',
      ANOMALY: '#9C27B0',
      CORRELATION: '#FF9800',
      PREDICTION: '#00BCD4',
      COMPETITIVE: '#E91E63',
      MARKET: '#3F51B5',
    }
    return colors[type] || '#9E9E9E'
  }

  /**
   * Get color for trend direction
   */
  getTrendDirectionColor(direction: TrendDirection): string {
    switch (direction) {
      case 'UPWARD':
        return '#4CAF50' // Green
      case 'DOWNWARD':
        return '#F44336' // Red
      case 'STABLE':
        return '#9E9E9E' // Gray
      case 'VOLATILE':
        return '#FF9800' // Orange
      default:
        return '#9E9E9E'
    }
  }

  /**
   * Get color for alert severity
   */
  getAlertSeverityColor(severity: AlertSeverity): string {
    switch (severity) {
      case 'CRITICAL':
        return '#F44336' // Red
      case 'WARNING':
        return '#FF9800' // Orange
      case 'INFO':
        return '#2196F3' // Blue
      default:
        return '#9E9E9E'
    }
  }

  /**
   * Get icon for insight type
   */
  getInsightTypeIcon(type: InsightType): string {
    const icons: Record<InsightType, string> = {
      PERFORMANCE: 'trending_up',
      OPPORTUNITY: 'lightbulb',
      RISK: 'warning',
      ANOMALY: 'error',
      CORRELATION: 'hub',
      PREDICTION: 'analytics',
      COMPETITIVE: 'groups',
      MARKET: 'public',
    }
    return icons[type] || 'insights'
  }

  /**
   * Format confidence level
   */
  formatConfidence(confidence: number): string {
    if (confidence >= 90) return 'Very High'
    if (confidence >= 75) return 'High'
    if (confidence >= 50) return 'Medium'
    return 'Low'
  }

  /**
   * Check if insight requires immediate action
   */
  requiresImmediateAction(insight: IntelligenceInsight): boolean {
    return (
      insight.priority === 'CRITICAL' ||
      (insight.priority === 'HIGH' && insight.confidence >= 80)
    )
  }

  /**
   * Calculate insight age in days
   */
  getInsightAge(insight: IntelligenceInsight): number {
    const detected = new Date(insight.detectedAt)
    const now = new Date()
    const diffTime = Math.abs(now.getTime() - detected.getTime())
    return Math.ceil(diffTime / (1000 * 60 * 60 * 24))
  }
}

// Export singleton instance
export const intelligenceAnalysisService = new IntelligenceAnalysisService()

