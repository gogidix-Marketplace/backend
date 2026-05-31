/**
 * Metrics Aggregation Service API Client
 *
 * Connects to: metrics-aggregation-service (Port 9202)
 * Base Path: /api/v1/metrics
 *
 * Endpoints:
 * - GET /kpi/{kpiCode} - Get aggregated KPI data
 * - GET /kpi/batch - Get multiple KPIs (batch)
 * - GET /domains/{domain}/kpi - Get KPIs by domain
 * - GET /trending - Get trending metrics
 * - GET /comparison - Compare metrics across domains/regions
 */

import { metricsAggregationApiClient } from '../../client/axios-client'
import type {
  KPI,
  KPICategory,
  KPITrend as KPITrendType,
  KPIStatus,
  DateRange,
  DomainType,
  PaginatedResponse,
  ApiResponse,
} from '../../client/types'

// Re-export the KPITrend type for convenience
export type KPITrend = KPITrendType

// ============================================================================
// Types
// ============================================================================

/**
 * Aggregated KPI with source information
 */
export interface AggregatedKPI extends KPI {
  sources: KPISource[]
  aggregationMethod: AggregationMethod
  lastAggregated: string
  dataFreshness: 'FRESH' | 'STALE' | 'EXPIRED'
}

/**
 * KPI data source information
 */
export interface KPISource {
  domain: string
  domainName: string
  serviceName: string
  value: number
  weight: number
  lastUpdated: string
  status: 'ACTIVE' | 'INACTIVE' | 'DEGRADED'
}

/**
 * Aggregation methods
 */
export type AggregationMethod =
  | 'SUM'
  | 'AVERAGE'
  | 'WEIGHTED_AVERAGE'
  | 'MEDIAN'
  | 'MAX'
  | 'MIN'
  | 'COUNT'
  | 'LATEST'

/**
 * KPI Trend Analysis data
 */
export interface KPITrendAnalysis {
  kpiCode: string
  kpiName: string
  category: KPICategory
  trendDirection: 'UPWARD' | 'DOWNWARD' | 'STABLE' | 'VOLATILE'
  changePercentage: number
  period: {
    start: string
    end: string
    type: 'DAILY' | 'WEEKLY' | 'MONTHLY' | 'QUARTERLY'
  }
  dataPoints: TrendDataPoint[]
  prediction?: {
    direction: 'UPWARD' | 'DOWNWARD' | 'STABLE'
    confidence: number
    nextPeriodValue?: number
  }
}

/**
 * Single trend data point
 */
export interface TrendDataPoint {
  timestamp: string
  value: number
  target?: number
  metadata?: Record<string, any>
}

/**
 * Metric comparison result
 */
export interface MetricComparison {
  comparisonId: string
  metricCode: string
  metricName: string
  category: KPICategory
  comparisonType: ComparisonType
  period: DateRange
  results: ComparisonResult[]
  insights: ComparisonInsight[]
  generatedAt: string
}

/**
 * Comparison types
 */
export type ComparisonType =
  | 'DOMAIN'
  | 'REGION'
  | 'PERIOD'
  | 'TARGET'
  | 'CUSTOM'

/**
 * Comparison result for a single entity
 */
export interface ComparisonResult {
  entity: string
  entityName: string
  entityType: 'DOMAIN' | 'REGION' | 'COUNTRY'
  value: number
  target?: number
  variance?: number
  variancePercentage?: number
  rank: number
  status: KPIStatus
  trend: KPITrend
  changeFromPrevious?: number
}

/**
 * Comparison insight
 */
export interface ComparisonInsight {
  type: 'OPPORTUNITY' | 'RISK' | 'OUTLIER' | 'TREND' | 'BEST_PRACTICE'
  title: string
  description: string
  entity: string
  impact: 'HIGH' | 'MEDIUM' | 'LOW'
  actionable: boolean
  recommendation?: string
}

/**
 * Batch KPI request
 */
export interface KPIBatchRequest {
  kpiCodes: string[]
  includeSources?: boolean
  includeHistorical?: boolean
  historicalDays?: number
  dateRange?: DateRange
}

/**
 * Batch KPI response
 */
export interface KPIBatchResponse {
  results: AggregatedKPI[]
  notFound: string[]
  timestamp: string
}

/**
 * Comparison request
 */
export interface ComparisonRequest {
  metricCode: string
  comparisonType: ComparisonType
  entities?: string[] // Domains, regions, or countries to compare
  dateRange: DateRange
  includeInsights?: boolean
  groupBy?: string
}

/**
 * Domain KPIs response
 */
export interface DomainKPIsResponse {
  domain: string
  domainName: string
  kpis: AggregatedKPI[]
  summary: {
    totalKPIs: number
    onTrack: number
    atRisk: number
    behind: number
    overallHealthScore: number
  }
  lastUpdated: string
}

/**
 * Trending metrics request
 */
export interface TrendingRequest {
  category?: KPICategory
  domain?: string
  period?: {
    start: string
    end: string
  }
  threshold?: number // Minimum change percentage to be considered trending
  limit?: number
}

/**
 * Trending metrics response
 */
export interface TrendingResponse {
  trendingUp: KPITrendAnalysis[]
  trendingDown: KPITrendAnalysis[]
  stable: KPITrendAnalysis[]
  volatile: KPITrendAnalysis[]
  timestamp: string
}

// ============================================================================
// Service Class
// ============================================================================

/**
 * Metrics Aggregation Service
 * Handles aggregation and retrieval of metrics from multiple domains
 */
class MetricsAggregationService {
  private readonly client = metricsAggregationApiClient
  private readonly basePath = '/api/v1/metrics'

  // ========================================================================
  // KPI Endpoints
  // ========================================================================

  /**
   * Get aggregated KPI data by KPI code
   * @param kpiCode - The KPI code to retrieve
   * @param options - Optional parameters
   */
  async getKPIByCode(
    kpiCode: string,
    options?: {
      includeSources?: boolean
      includeHistorical?: boolean
      historicalDays?: number
    }
  ): Promise<AggregatedKPI> {
    const response = await this.client.get<AggregatedKPI>(
      `${this.basePath}/kpi/${encodeURIComponent(kpiCode)}`,
      { params: options }
    )
    return response.data
  }

  /**
   * Get multiple KPIs in a single batch request
   * @param request - Batch request with KPI codes
   */
  async getKPIsBatch(request: KPIBatchRequest): Promise<KPIBatchResponse> {
    const response = await this.client.post<KPIBatchResponse>(
      `${this.basePath}/kpi/batch`,
      request
    )
    return response.data
  }

  /**
   * Get all KPIs for a specific domain
   * @param domain - Domain code
   * @param params - Optional query parameters
   */
  async getDomainKPIs(
    domain: string,
    params?: {
      category?: KPICategory
      status?: KPIStatus
      page?: number
      size?: number
    }
  ): Promise<DomainKPIsResponse> {
    const response = await this.client.get<DomainKPIsResponse>(
      `${this.basePath}/domains/${encodeURIComponent(domain)}/kpi`,
      { params }
    )
    return response.data
  }

  /**
   * Get KPIs by category across all domains
   * @param category - KPI category
   * @param params - Optional query parameters
   */
  async getKPIsByCategory(
    category: KPICategory,
    params?: {
      page?: number
      size?: number
      sortBy?: string
    }
  ): Promise<PaginatedResponse<AggregatedKPI>> {
    const response = await this.client.get<PaginatedResponse<AggregatedKPI>>(
      `${this.basePath}/kpi/category/${category}`,
      { params }
    )
    return response.data
  }

  /**
   * Search KPIs by name or description
   * @param searchTerm - Search term
   * @param params - Optional query parameters
   */
  async searchKPIs(
    searchTerm: string,
    params?: {
      category?: KPICategory
      domain?: string
      page?: number
      size?: number
    }
  ): Promise<PaginatedResponse<AggregatedKPI>> {
    const response = await this.client.get<PaginatedResponse<AggregatedKPI>>(
      `${this.basePath}/kpi/search`,
      { params: { searchTerm, ...params } }
    )
    return response.data
  }

  // ========================================================================
  // Trending Endpoints
  // ========================================================================

  /**
   * Get trending metrics
   * Returns metrics with significant upward or downward trends
   * @param request - Trending request parameters
   */
  async getTrendingMetrics(request?: TrendingRequest): Promise<TrendingResponse> {
    const response = await this.client.get<TrendingResponse>(
      `${this.basePath}/trending`,
      { params: request }
    )
    return response.data
  }

  /**
   * Get trend data for a specific KPI
   * @param kpiCode - KPI code
   * @param params - Trend parameters
   */
  async getKPITrend(
    kpiCode: string,
    params?: {
      period?: 'DAILY' | 'WEEKLY' | 'MONTHLY' | 'QUARTERLY'
      startDate?: string
      endDate?: string
      points?: number
    }
  ): Promise<KPITrendAnalysis> {
    const response = await this.client.get<KPITrendAnalysis>(
      `${this.basePath}/trending/${encodeURIComponent(kpiCode)}`,
      { params }
    )
    return response.data
  }

  // ========================================================================
  // Comparison Endpoints
  // ========================================================================

  /**
   * Compare metrics across domains, regions, or time periods
   * @param request - Comparison request
   */
  async compareMetrics(request: ComparisonRequest): Promise<MetricComparison> {
    const response = await this.client.post<MetricComparison>(
      `${this.basePath}/comparison`,
      request
    )
    return response.data
  }

  /**
   * Compare a single metric across domains
   * @param metricCode - Metric code
   * @param domains - List of domains to compare
   * @param dateRange - Date range for comparison
   */
  async compareAcrossDomains(
    metricCode: string,
    domains: string[],
    dateRange: DateRange
  ): Promise<MetricComparison> {
    return this.compareMetrics({
      metricCode,
      comparisonType: 'DOMAIN',
      entities: domains,
      dateRange,
      includeInsights: true,
    })
  }

  /**
   * Compare a single metric across regions
   * @param metricCode - Metric code
   * @param regions - List of regions to compare
   * @param dateRange - Date range for comparison
   */
  async compareAcrossRegions(
    metricCode: string,
    regions: string[],
    dateRange: DateRange
  ): Promise<MetricComparison> {
    return this.compareMetrics({
      metricCode,
      comparisonType: 'REGION',
      entities: regions,
      dateRange,
      includeInsights: true,
    })
  }

  /**
   * Compare a metric against its target over time
   * @param metricCode - Metric code
   * @param dateRange - Date range for comparison
   */
  async compareWithTarget(
    metricCode: string,
    dateRange: DateRange
  ): Promise<MetricComparison> {
    return this.compareMetrics({
      metricCode,
      comparisonType: 'TARGET',
      dateRange,
      includeInsights: true,
    })
  }

  // ========================================================================
  // Aggregation Endpoints
  // ========================================================================

  /**
   * Trigger manual aggregation for specific KPIs
   * @param kpiCodes - KPI codes to aggregate
   */
  async triggerAggregation(kpiCodes: string[]): Promise<ApiResponse<{ success: string[]; failed: string[] }>> {
    const response = await this.client.post<ApiResponse<{ success: string[]; failed: string[] }>>(
      `${this.basePath}/aggregate`,
      { kpiCodes }
    )
    return response.data
  }

  /**
   * Get aggregation status for KPIs
   * @param kpiCodes - KPI codes to check
   */
  async getAggregationStatus(kpiCodes: string[]): Promise<
    ApiResponse<
      Array<{
        kpiCode: string
        status: 'PENDING' | 'IN_PROGRESS' | 'COMPLETED' | 'FAILED'
        lastAggregated: string
        nextAggregation: string
      }>
    >
  > {
    const response = await this.client.get<
      ApiResponse<
        Array<{
          kpiCode: string
          status: 'PENDING' | 'IN_PROGRESS' | 'COMPLETED' | 'FAILED'
          lastAggregated: string
          nextAggregation: string
        }>
      >
    >(`${this.basePath}/aggregate/status`, {
      params: { kpiCodes: kpiCodes.join(',') },
    })
    return response.data
  }

  // ========================================================================
  // Health & Utility Endpoints
  // ========================================================================

  /**
   * Health check for metrics aggregation service
   */
  async healthCheck(): Promise<ApiResponse<{ status: string; timestamp: string }>> {
    const response = await this.client.get<ApiResponse<{ status: string; timestamp: string }>>(
      `${this.basePath}/health`
    )
    return response.data
  }

  /**
   * Get service metadata and supported KPIs
   */
  async getMetadata(): Promise<
    ApiResponse<{
      version: string
      supportedKPIs: Array<{
        code: string
        name: string
        category: KPICategory
        unit: string
        aggregationMethod: AggregationMethod
      }>
      supportedDomains: DomainType[]
    }>
  > {
    const response = await this.client.get<
      ApiResponse<{
        version: string
        supportedKPIs: Array<{
          code: string
          name: string
          category: KPICategory
          unit: string
          aggregationMethod: AggregationMethod
        }>
        supportedDomains: DomainType[]
      }>
    >(`${this.basePath}/metadata`)
    return response.data
  }

  // ========================================================================
  // Helper Methods
  // ========================================================================

  /**
   * Get trend icon based on trend direction
   */
  getTrendIcon(trend: KPITrend | string): string {
    const icons: Record<string, string> = {
      UP: '↑',
      UPWARD: '↑',
      DOWN: '↓',
      DOWNWARD: '↓',
      STABLE: '→',
      VOLATILE: '↝',
    }
    return icons[trend as string] || '→'
  }

  /**
   * Get color for trend direction
   */
  getTrendColor(trend: KPITrend | string): string {
    const colors: Record<string, string> = {
      UP: '#4CAF50',
      UPWARD: '#4CAF50',
      DOWN: '#F44336',
      DOWNWARD: '#F44336',
      STABLE: '#9E9E9E',
      VOLATILE: '#FF9800',
    }
    return colors[trend as string] || '#9E9E9E'
  }

  /**
   * Get status color
   */
  getStatusColor(status: KPIStatus | string): string {
    const colors: Record<string, string> = {
      ON_TRACK: '#4CAF50',
      AHEAD: '#2196F3',
      AT_RISK: '#FF9800',
      BEHIND: '#F44336',
    }
    return colors[status as string] || '#9E9E9E'
  }

  /**
   * Get data freshness color
   */
  getFreshnessColor(freshness: string): string {
    const colors: Record<string, string> = {
      FRESH: '#4CAF50',
      STALE: '#FF9800',
      EXPIRED: '#F44336',
    }
    return colors[freshness] || '#9E9E9E'
  }

  /**
   * Calculate percentage formatted string
   */
  formatPercentage(value: number, decimals = 1): string {
    const formatted = value.toFixed(decimals)
    const sign = value > 0 ? '+' : ''
    return `${sign}${formatted}%`
  }

  /**
   * Format value with unit
   */
  formatValue(value: number, unit: string): string {
    // Handle currency units
    if (unit.startsWith('$') || unit === 'USD' || unit === 'EUR' || unit === 'GBP') {
      return new Intl.NumberFormat('en-US', {
        style: 'currency',
        currency: unit.replace('$', 'USD') as any,
      }).format(value)
    }

    // Handle percentage
    if (unit === '%') {
      return `${value.toFixed(1)}%`
    }

    // Handle large numbers with K/M/B suffixes
    if (value >= 1000000000) {
      return `${(value / 1000000000).toFixed(1)}B ${unit}`
    }
    if (value >= 1000000) {
      return `${(value / 1000000).toFixed(1)}M ${unit}`
    }
    if (value >= 1000) {
      return `${(value / 1000).toFixed(1)}K ${unit}`
    }

    return `${value.toLocaleString()} ${unit}`
  }
}

// Export singleton instance
export const metricsAggregationService = new MetricsAggregationService()

// Export type for use in components
export type { MetricsAggregationService }
