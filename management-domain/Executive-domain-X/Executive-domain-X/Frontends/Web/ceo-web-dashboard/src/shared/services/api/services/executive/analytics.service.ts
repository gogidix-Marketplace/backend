/**
 * CEO Analytics Service API Client
 *
 * Connects to: ceo-analytics-service (Port 9001)
 * Base Path: /api/v1/analytics
 *
 * Endpoints:
 * - GET /dashboard - Get analytics dashboard overview
 * - GET /trending - Get trending KPIs
 * - GET /requiring-attention - Get KPIs requiring attention
 * - GET /time-series/{kpiName} - Get time series data for KPI
 * - GET /aggregate - Aggregate analytics by time period
 * - GET /export - Export analytics data
 */

import { executiveApiClient } from '../../client/axios-client'
import type {
  KPI,
  KPICategory,
  AnalyticsDataPoint,
  PaginatedResponse,
  DateRange,
  TimePeriod,
  ExportFormat,
} from '../../client/types'

// ============================================================================
// Types
// ============================================================================

export interface AnalyticsDashboardResponse {
  strategicHealthScore: number
  componentScores: {
    revenue: number
    growth: number
    customer: number
    operations: number
    innovation: number
  }
  overviewKPIs: KPI[]
  crossDomainPerformance: CrossDomainPerformance[]
  trendingKPIs: KPI[]
  alertsRequiringAttention: KPI[]
  lastUpdated: string
}

export interface CrossDomainPerformance {
  domain: string
  domainName: string
  revenue: number
  growth: number
  healthScore: number
  status: 'ON_TRACK' | 'AT_RISK' | 'BEHIND'
  trend: 'UP' | 'DOWN' | 'STABLE'
}

export interface AnalyticsResponse {
  id: string
  kpiCode: string
  kpiName: string
  category: KPICategory
  value: number
  target: number
  previousValue: number
  changePercentage: number
  healthScore: number
  status: 'ON_TRACK' | 'AT_RISK' | 'BEHIND' | 'AHEAD'
  trend: 'UP' | 'DOWN' | 'STABLE'
  sourceDomain: string
  analysisDate: string
  dimensions: Record<string, string>
  metadata?: Record<string, any>
}

export interface CreateAnalyticsRequest {
  kpiCode: string
  kpiName: string
  category: KPICategory
  value: number
  target: number
  sourceDomain: string
  analysisDate: string
  dimensions?: Record<string, string>
  metadata?: Record<string, any>
}

export interface UpdateAnalyticsRequest {
  value?: number
  target?: number
  metadata?: Record<string, any>
}

export interface AnalyticsExportRequest {
  format: ExportFormat
  startDate: string
  endDate: string
  categories?: KPICategory[]
}

export interface KPISeries {
  name: string
  kpiCode: string
  data: AnalyticsDataPoint[]
  unit: string
  color?: string
}

// ============================================================================
// Service Class
// ============================================================================

class AnalyticsService {
  private readonly basePath = '/api/v1/analytics'

  /**
   * Get analytics dashboard overview
   * This is the main endpoint for CEO dashboard overview page
   */
  async getDashboard(): Promise<AnalyticsDashboardResponse> {
    const response = await executiveApiClient.get<AnalyticsDashboardResponse>(
      `${this.basePath}/dashboard`
    )
    return response.data
  }

  /**
   * Get all analytics with pagination
   */
  async getAllAnalytics(params?: {
    page?: number
    size?: number
    sortBy?: string
    sortDirection?: 'asc' | 'desc'
  }): Promise<PaginatedResponse<AnalyticsResponse>> {
    const response = await executiveApiClient.get<PaginatedResponse<AnalyticsResponse>>(
      this.basePath,
      { params }
    )
    return response.data
  }

  /**
   * Get analytics by ID
   */
  async getAnalyticsById(id: string): Promise<AnalyticsResponse> {
    const response = await executiveApiClient.get<AnalyticsResponse>(
      `${this.basePath}/${id}`
    )
    return response.data
  }

  /**
   * Get analytics by category
   */
  async getAnalyticsByCategory(category: KPICategory): Promise<AnalyticsResponse[]> {
    const response = await executiveApiClient.get<AnalyticsResponse[]>(
      `${this.basePath}/category/${category}`
    )
    return response.data
  }

  /**
   * Get analytics by date range
   */
  async getAnalyticsByDateRange(dateRange: DateRange): Promise<AnalyticsResponse[]> {
    const response = await executiveApiClient.get<AnalyticsResponse[]>(
      `${this.basePath}/date-range`,
      { params: dateRange }
    )
    return response.data
  }

  /**
   * Get analytics by KPI name
   */
  async getAnalyticsByKpiName(kpiName: string): Promise<AnalyticsResponse[]> {
    const response = await executiveApiClient.get<AnalyticsResponse[]>(
      `${this.basePath}/kpi/${encodeURIComponent(kpiName)}`
    )
    return response.data
  }

  /**
   * Get analytics by source domain
   */
  async getAnalyticsBySourceDomain(sourceDomain: string): Promise<AnalyticsResponse[]> {
    const response = await executiveApiClient.get<AnalyticsResponse[]>(
      `${this.basePath}/source/${encodeURIComponent(sourceDomain)}`
    )
    return response.data
  }

  /**
   * Search analytics
   */
  async searchAnalytics(searchTerm: string, params?: {
    page?: number
    size?: number
  }): Promise<PaginatedResponse<AnalyticsResponse>> {
    const response = await executiveApiClient.get<PaginatedResponse<AnalyticsResponse>>(
      `${this.basePath}/search`,
      { params: { searchTerm, ...params } }
    )
    return response.data
  }

  /**
   * Get analytics requiring attention
   * These are KPIs with low health score or alerts
   */
  async getAnalyticsRequiringAttention(): Promise<AnalyticsResponse[]> {
    const response = await executiveApiClient.get<AnalyticsResponse[]>(
      `${this.basePath}/requiring-attention`
    )
    return response.data
  }

  /**
   * Get trending KPIs
   * Returns KPIs with significant trends (up or down)
   */
  async getTrendingKPIs(): Promise<AnalyticsResponse[]> {
    const response = await executiveApiClient.get<AnalyticsResponse[]>(
      `${this.basePath}/trending`
    )
    return response.data
  }

  /**
   * Get time series data for a specific KPI
   * Useful for charts showing KPI over time
   */
  async getTimeSeriesData(kpiName: string, days = 30): Promise<AnalyticsResponse[]> {
    const response = await executiveApiClient.get<AnalyticsResponse[]>(
      `${this.basePath}/time-series/${encodeURIComponent(kpiName)}`,
      { params: { days } }
    )
    return response.data
  }

  /**
   * Aggregate analytics by time period
   */
  async aggregateAnalytics(
    timePeriod: TimePeriod,
    dateRange: DateRange
  ): Promise<AnalyticsResponse[]> {
    const response = await executiveApiClient.get<AnalyticsResponse[]>(
      `${this.basePath}/aggregate`,
      { params: { timePeriod, ...dateRange } }
    )
    return response.data
  }

  /**
   * Create new analytics record
   */
  async createAnalytics(request: CreateAnalyticsRequest): Promise<AnalyticsResponse> {
    const response = await executiveApiClient.post<AnalyticsResponse>(
      this.basePath,
      request
    )
    return response.data
  }

  /**
   * Update analytics record
   */
  async updateAnalytics(
    id: string,
    request: UpdateAnalyticsRequest
  ): Promise<AnalyticsResponse> {
    const response = await executiveApiClient.put<AnalyticsResponse>(
      `${this.basePath}/${id}`,
      request
    )
    return response.data
  }

  /**
   * Delete analytics record (soft delete)
   */
  async deleteAnalytics(id: string): Promise<void> {
    await executiveApiClient.delete(`${this.basePath}/${id}`)
  }

  /**
   * Export analytics data
   */
  async exportAnalytics(request: AnalyticsExportRequest): Promise<Blob> {
    const response = await executiveApiClient.get(
      `${this.basePath}/export`,
      {
        params: {
          format: request.format.toLowerCase(),
          startDate: request.startDate,
          endDate: request.endDate,
        },
        responseType: 'blob',
      }
    )
    return response.data
  }

  /**
   * Health check
   */
  async healthCheck(): Promise<string> {
    const response = await executiveApiClient.get<string>(`${this.basePath}/health`)
    return response.data
  }

  // ========================================================================
  // Helper Methods
  // ========================================================================

  /**
   * Get strategic health score color
   */
  getHealthScoreColor(score: number): string {
    if (score >= 80) return '#4CAF50' // Green
    if (score >= 60) return '#FF9800' // Orange
    return '#F44336' // Red
  }

  /**
   * Get status color
   */
  getStatusColor(status: string): string {
    const colors: Record<string, string> = {
      ON_TRACK: '#4CAF50',
      AHEAD: '#2196F3',
      AT_RISK: '#FF9800',
      BEHIND: '#F44336',
    }
    return colors[status] || '#9E9E9E'
  }

  /**
   * Get trend icon
   */
  getTrendIcon(trend: string): string {
    const icons: Record<string, string> = {
      UP: '↑',
      DOWN: '↓',
      STABLE: '→',
      VOLATILE: '↝',
    }
    return icons[trend] || '→'
  }
}

// Export singleton instance
export const analyticsService = new AnalyticsService()

// Export type for use in components
export type { AnalyticsService }
