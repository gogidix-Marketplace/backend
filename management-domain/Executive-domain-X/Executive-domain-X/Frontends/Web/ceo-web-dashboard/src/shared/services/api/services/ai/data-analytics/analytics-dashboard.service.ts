/**
 * AI Analytics Dashboard Service
 *
 * Service: ai-analytics-dashboard-service (Port 7031)
 * Base Path: /api/v1/ai/dashboard
 *
 * Features:
 * - AI-powered dashboard widgets and visualizations
 * - Real-time analytics and insights
 * - Configurable dashboard layouts
 * - AI-generated alerts and notifications
 * - Model performance monitoring
 * - Predictive and prescriptive analytics
 */

import { analyticsDashboardApiClient } from '../../../client/axios-client'
import type { DateRange, TimePeriod } from '../../../client/types'

// ============================================================================
// Types
// ============================================================================

/**
 * Widget types supported by the dashboard
 */
export type WidgetType =
  | 'METRIC_CARD'           // Single KPI display
  | 'LINE_CHART'           // Time series line chart
  | 'BAR_CHART'            // Bar chart comparison
  | 'PIE_CHART'            // Distribution pie chart
  | 'AREA_CHART'           // Filled area chart
  | 'HEATMAP'              // Heat map visualization
  | 'TABLE'                // Data table
  | 'GAUGE'                // Gauge/meter display
  | 'FUNNEL'               // Funnel visualization
  | 'SCATTER_PLOT'         // Scatter plot
  | 'TREEMAP'              // Treemap hierarchy
  | 'GEO_MAP'              // Geographic map
  | 'WORD_CLOUD'           // Word cloud
  | 'SANKEY'               // Sankey diagram
  | 'RADAR'                // Radar chart
  | 'CUSTOM'               // Custom widget

/**
 * Alert severity levels
 */
export type AlertSeverity = 'INFO' | 'WARNING' | 'ERROR' | 'CRITICAL'

/**
 * Alert status
 */
export type AlertStatus = 'ACTIVE' | 'ACKNOWLEDGED' | 'RESOLVED' | 'DISMISSED'

/**
 * Insight confidence levels
 */
export type InsightConfidence = 'LOW' | 'MEDIUM' | 'HIGH' | 'VERY_HIGH'

/**
 * Insight categories
 */
export type InsightCategory =
  | 'REVENUE'
  | 'CUSTOMER'
  | 'OPERATION'
  | 'MARKETING'
  | 'PRODUCT'
  | 'FINANCIAL'
  | 'TECHNICAL'
  | 'STRATEGIC'

/**
 * AI Dashboard main response
 */
export interface AIDashboard {
  dashboardId: string
  name: string
  description?: string
  lastUpdated: string
  refreshInterval: number // in seconds

  // Summary metrics
  summary: DashboardSummary

  // Widgets configuration
  widgets: DashboardWidget[]

  // Current insights
  insights: AIInsight[]

  // Active alerts
  alerts: AIAlert[]

  // Performance metrics
  performance: ModelPerformance
}

/**
 * Dashboard summary metrics
 */
export interface DashboardSummary {
  totalWidgets: number
  activeAlerts: number
  pendingInsights: number
  lastDataRefresh: string
  dataFreshness: 'REALTIME' | 'NEAR_REALTIME' | 'BATCH' | 'STALE'
  overallHealth: 'EXCELLENT' | 'GOOD' | 'FAIR' | 'POOR'
}

/**
 * Dashboard widget configuration
 */
export interface DashboardWidget {
  widgetId: string
  name: string
  type: WidgetType
  title: string
  description?: string
  position: WidgetPosition
  size: WidgetSize
  enabled: boolean
  refreshInterval?: number
  dataSource: WidgetDataSource
  config: WidgetConfiguration
  lastUpdated: string
}

/**
 * Widget position on dashboard
 */
export interface WidgetPosition {
  row: number
  column: number
}

/**
 * Widget size (grid units)
 */
export interface WidgetSize {
  rows: number
  columns: number
}

/**
 * Widget data source
 */
export interface WidgetDataSource {
  type: 'API' | 'QUERY' | 'STREAM' | 'STATIC'
  endpoint?: string
  query?: string
  updateFrequency?: number
}

/**
 * Widget-specific configuration
 */
export interface WidgetConfiguration {
  // Chart/display options
  showLegend?: boolean
  showLabels?: boolean
  showGrid?: boolean
  colorScheme?: string[]
  xAxisLabel?: string
  yAxisLabel?: string

  // Data options
  aggregation?: 'SUM' | 'AVG' | 'COUNT' | 'MIN' | 'MAX' | 'MEDIAN'
  groupBy?: string
  filters?: Record<string, any>
  timeRange?: DateRange

  // Display thresholds
  thresholds?: WidgetThreshold[]

  // Custom options per widget type
  customOptions?: Record<string, any>
}

/**
 * Widget value thresholds for visual indicators
 */
export interface WidgetThreshold {
  label: string
  value: number
  color: string
  operator: 'GREATER_THAN' | 'LESS_THAN' | 'EQUALS' | 'BETWEEN'
}

/**
 * AI-generated insight
 */
export interface AIInsight {
  insightId: string
  title: string
  description: string
  category: InsightCategory
  confidence: InsightConfidence
  confidenceScore: number // 0-100
  generatedAt: string
  validUntil?: string

  // Insight details
  impact: 'HIGH' | 'MEDIUM' | 'LOW'
  priority: number
  actionable: boolean

  // Related data
  metrics: InsightMetric[]
  recommendations?: string[]
  relatedInsights?: string[] // insight IDs

  // Source information
  dataSource: string
  modelVersion?: string
}

/**
 * Insight metric reference
 */
export interface InsightMetric {
  name: string
  value: number
  change?: number
  changePercent?: number
  period?: string
}

/**
 * AI Alert
 */
export interface AIAlert {
  alertId: string
  title: string
  message: string
  severity: AlertSeverity
  status: AlertStatus
  createdAt: string
  acknowledgedAt?: string
  resolvedAt?: string

  // Alert details
  category: InsightCategory
  source: string
  affectedEntities?: string[]

  // Threshold/rule info
  condition?: string
  threshold?: number
  actualValue?: number

  // Actions
  actionable: boolean
  actions?: AlertAction[]
}

/**
 * Available actions for an alert
 */
export interface AlertAction {
  actionId: string
  label: string
  type: 'LINK' | 'API' | 'WORKFLOW' | 'CUSTOM'
  target?: string
  payload?: Record<string, any>
}

/**
 * Model performance metrics
 */
export interface ModelPerformance {
  modelId: string
  modelName: string
  modelType: string
  version: string

  // Accuracy metrics
  accuracy: number
  precision?: number
  recall?: number
  f1Score?: number

  // Prediction metrics
  mae?: number // Mean Absolute Error
  mse?: number // Mean Squared Error
  rmse?: number // Root Mean Square Error
  mape?: number // Mean Absolute Percentage Error

  // Operational metrics
  avgPredictionTime: number // in milliseconds
  totalPredictions: number
  successRate: number

  // Data quality
  trainingDataPoints: number
  lastTrainingDate: string
  dataDriftScore?: number

  // Model health
  healthStatus: 'HEALTHY' | 'DEGRADED' | 'CRITICAL'
  lastEvaluated: string
}

/**
 * Dashboard configuration request
 */
export interface DashboardConfiguration {
  dashboardId?: string
  name: string
  description?: string
  widgets: WidgetConfiguration[]
  layout?: DashboardLayout
  refreshInterval?: number
}

/**
 * Dashboard layout configuration
 */
export interface DashboardLayout {
  columns: number
  rows?: number
  gap?: number
}

/**
 * Get dashboard request parameters
 */
export interface GetDashboardRequest {
  dateRange?: DateRange
  includeWidgets?: boolean
  includeInsights?: boolean
  includeAlerts?: boolean
  includePerformance?: boolean
  widgetTypes?: WidgetType[]
}

/**
 * Get widgets request parameters
 */
export interface GetWidgetsRequest {
  types?: WidgetType[]
  enabledOnly?: boolean
}

/**
 * Get insights request parameters
 */
export interface GetInsightsRequest {
  categories?: InsightCategory[]
  confidence?: InsightConfidence[]
  startDate?: string
  endDate?: string
  limit?: number
  offset?: number
}

/**
 * Get alerts request parameters
 */
export interface GetAlertsRequest {
  severity?: AlertSeverity[]
  status?: AlertStatus[]
  categories?: InsightCategory[]
  startDate?: string
  endDate?: string
}

/**
 * Get performance request parameters
 */
export interface GetPerformanceRequest {
  modelId?: string
  includeHistory?: boolean
  period?: TimePeriod
}

// ============================================================================
// Service Class
// ============================================================================

class AnalyticsDashboardService {
  private readonly basePath = '/api/v1/ai/dashboard'

  /**
   * Get AI analytics dashboard
   * Retrieves the complete dashboard with all components
   *
   * @param request - Optional parameters to filter dashboard content
   * @returns Complete AI dashboard with widgets, insights, alerts, and performance
   */
  async getDashboard(request?: GetDashboardRequest): Promise<AIDashboard> {
    const response = await analyticsDashboardApiClient.get<AIDashboard>(
      `${this.basePath}`,
      { params: request }
    )
    return response.data
  }

  /**
   * Get available dashboard widgets
   * Retrieves all configured widgets for the dashboard
   *
   * @param request - Optional filters for widget types and status
   * @returns List of dashboard widgets
   */
  async getWidgets(request?: GetWidgetsRequest): Promise<DashboardWidget[]> {
    const response = await analyticsDashboardApiClient.get<DashboardWidget[]>(
      `${this.basePath}/widgets`,
      { params: request }
    )
    return response.data
  }

  /**
   * Get a specific widget by ID
   *
   * @param widgetId - The widget ID
   * @returns The widget details
   */
  async getWidget(widgetId: string): Promise<DashboardWidget> {
    const response = await analyticsDashboardApiClient.get<DashboardWidget>(
      `${this.basePath}/widgets/${widgetId}`
    )
    return response.data
  }

  /**
   * Get AI-generated insights
   * Retrieves insights generated by AI models
   *
   * @param request - Optional filters for insights
   * @returns List of AI insights
   */
  async getInsights(request?: GetInsightsRequest): Promise<AIInsight[]> {
    const response = await analyticsDashboardApiClient.get<AIInsight[]>(
      `${this.basePath}/insights`,
      { params: request }
    )
    return response.data
  }

  /**
   * Get a specific insight by ID
   *
   * @param insightId - The insight ID
   * @returns The insight details
   */
  async getInsight(insightId: string): Promise<AIInsight> {
    const response = await analyticsDashboardApiClient.get<AIInsight>(
      `${this.basePath}/insights/${insightId}`
    )
    return response.data
  }

  /**
   * Get AI alerts
   * Retrieves alerts generated by AI monitoring
   *
   * @param request - Optional filters for alerts
   * @returns List of AI alerts
   */
  async getAlerts(request?: GetAlertsRequest): Promise<AIAlert[]> {
    const response = await analyticsDashboardApiClient.get<AIAlert[]>(
      `${this.basePath}/alerts`,
      { params: request }
    )
    return response.data
  }

  /**
   * Acknowledge an alert
   *
   * @param alertId - The alert ID to acknowledge
   * @returns Updated alert
   */
  async acknowledgeAlert(alertId: string): Promise<AIAlert> {
    const response = await analyticsDashboardApiClient.post<AIAlert>(
      `${this.basePath}/alerts/${alertId}/acknowledge`
    )
    return response.data
  }

  /**
   * Dismiss an alert
   *
   * @param alertId - The alert ID to dismiss
   * @returns Updated alert
   */
  async dismissAlert(alertId: string): Promise<AIAlert> {
    const response = await analyticsDashboardApiClient.post<AIAlert>(
      `${this.basePath}/alerts/${alertId}/dismiss`
    )
    return response.data
  }

  /**
   * Get model performance metrics
   * Retrieves performance metrics for AI models
   *
   * @param request - Optional filters for performance data
   * @returns Model performance metrics
   */
  async getPerformance(request?: GetPerformanceRequest): Promise<ModelPerformance[]> {
    const response = await analyticsDashboardApiClient.get<ModelPerformance[]>(
      `${this.basePath}/performance`,
      { params: request }
    )
    return response.data
  }

  /**
   * Get performance for a specific model
   *
   * @param modelId - The model ID
   * @returns Model performance details
   */
  async getModelPerformance(modelId: string): Promise<ModelPerformance> {
    const response = await analyticsDashboardApiClient.get<ModelPerformance>(
      `${this.basePath}/performance/${modelId}`
    )
    return response.data
  }

  /**
   * Configure dashboard widgets
   * Updates the dashboard configuration with new widgets and layout
   *
   * @param configuration - Dashboard configuration
   * @returns Updated dashboard
   */
  async configureDashboard(configuration: DashboardConfiguration): Promise<AIDashboard> {
    const response = await analyticsDashboardApiClient.post<AIDashboard>(
      `${this.basePath}/configure`,
      configuration
    )
    return response.data
  }

  /**
   * Add a widget to the dashboard
   *
   * @param widget - Widget configuration
   * @returns Created widget
   */
  async addWidget(widget: Omit<DashboardWidget, 'widgetId' | 'lastUpdated'>): Promise<DashboardWidget> {
    const response = await analyticsDashboardApiClient.post<DashboardWidget>(
      `${this.basePath}/widgets`,
      widget
    )
    return response.data
  }

  /**
   * Update a widget configuration
   *
   * @param widgetId - The widget ID to update
   * @param widget - Updated widget configuration
   * @returns Updated widget
   */
  async updateWidget(
    widgetId: string,
    widget: Partial<DashboardWidget>
  ): Promise<DashboardWidget> {
    const response = await analyticsDashboardApiClient.put<DashboardWidget>(
      `${this.basePath}/widgets/${widgetId}`,
      widget
    )
    return response.data
  }

  /**
   * Remove a widget from the dashboard
   *
   * @param widgetId - The widget ID to remove
   * @returns Confirmation of deletion
   */
  async removeWidget(widgetId: string): Promise<{ success: boolean; message: string }> {
    const response = await analyticsDashboardApiClient.delete<{
      success: boolean
      message: string
    }>(`${this.basePath}/widgets/${widgetId}`)
    return response.data
  }

  /**
   * Refresh dashboard data
   * Forces a refresh of all dashboard data
   *
   * @returns Refreshed dashboard
   */
  async refreshDashboard(): Promise<AIDashboard> {
    const response = await analyticsDashboardApiClient.post<AIDashboard>(
      `${this.basePath}/refresh`
    )
    return response.data
  }

  /**
   * Export dashboard configuration
   *
   * @param format - Export format ('JSON' | 'CSV' | 'PDF')
   * @returns Exported dashboard data
   */
  async exportDashboard(format: 'JSON' | 'CSV' | 'PDF' = 'JSON'): Promise<Blob> {
    const response = await analyticsDashboardApiClient.get(
      `${this.basePath}/export`,
      {
        params: { format },
        responseType: 'blob'
      }
    )
    return response.data
  }

  // ========================================================================
  // Helper Methods
  // ========================================================================

  /**
   * Get alert severity color
   *
   * @param severity - Alert severity level
   * @returns Hex color code
   */
  getAlertSeverityColor(severity: AlertSeverity): string {
    const colors: Record<AlertSeverity, string> = {
      INFO: '#2196F3',
      WARNING: '#FF9800',
      ERROR: '#F44336',
      CRITICAL: '#D32F2F'
    }
    return colors[severity]
  }

  /**
   * Get insight confidence color
   *
   * @param confidence - Insight confidence level
   * @returns Hex color code
   */
  getInsightConfidenceColor(confidence: InsightConfidence): string {
    const colors: Record<InsightConfidence, string> = {
      LOW: '#F44336',
      MEDIUM: '#FF9800',
      HIGH: '#4CAF50',
      VERY_HIGH: '#2E7D32'
    }
    return colors[confidence]
  }

  /**
   * Get model health status color
   *
   * @param status - Model health status
   * @returns Hex color code
   */
  getModelHealthColor(status: 'HEALTHY' | 'DEGRADED' | 'CRITICAL'): string {
    const colors: Record<string, string> = {
      HEALTHY: '#4CAF50',
      DEGRADED: '#FF9800',
      CRITICAL: '#F44336'
    }
    return colors[status]
  }

  /**
   * Format widget type for display
   *
   * @param type - Widget type
   * @returns Formatted display name
   */
  formatWidgetType(type: WidgetType): string {
    return type
      .split('_')
      .map(word => word.charAt(0) + word.slice(1).toLowerCase())
      .join(' ')
  }

  /**
   * Calculate widget grid position
   *
   * @param index - Widget index in list
   * @param columns - Number of columns in layout
   * @returns Widget position (row, column)
   */
  calculateWidgetPosition(index: number, columns = 4): WidgetPosition {
    return {
      row: Math.floor(index / columns),
      column: index % columns
    }
  }
}

// Export singleton instance
export const analyticsDashboardService = new AnalyticsDashboardService()
