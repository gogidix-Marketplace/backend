/**
 * Data Aggregation Service API Client
 *
 * Connects to: data-aggregation-service (Port 9201)
 * Base Path: /api/v1/aggregation
 *
 * Endpoints:
 * - GET /cross-domain - Get cross-domain aggregated data
 * - GET /summary - Get cross-domain summary
 * - GET /real-time - Get real-time metrics
 * - POST /requests - Create aggregation request
 * - GET /requests/{id} - Get aggregation request status
 * - DELETE /requests/{id} - Cancel aggregation request
 * - GET /health - Health check
 */

import { aggregationApiClient } from '../../client/axios-client'
import type {
  PaginatedResponse,
  DateRange,
  DomainType,
  KPICategory,
  AnalyticsDataPoint,
} from '../../client/types'

// ============================================================================
// Types
// ============================================================================

/**
 * Cross-domain aggregated data point
 * Represents a single data point aggregated from multiple domains
 */
export interface CrossDomainDataPoint {
  id: string
  timestamp: string
  domains: string[]
  metricName: string
  metricValue: number
  metricUnit: string
  aggregationMethod: 'SUM' | 'AVG' | 'MIN' | 'MAX' | 'COUNT'
  sourceData: SourceDataPoint[]
  metadata?: Record<string, any>
}

/**
 * Source data point contributing to aggregation
 */
export interface SourceDataPoint {
  domain: string
  sourceService: string
  originalValue: number
  timestamp: string
  dataQuality: 'HIGH' | 'MEDIUM' | 'LOW'
}

/**
 * Domain summary for cross-domain overview
 */
export interface DomainSummary {
  domain: DomainType
  domainName: string
  totalRecords: number
  lastUpdated: string
  dataQualityScore: number // 0-100
  status: 'ACTIVE' | 'INACTIVE' | 'DEGRADED'
  keyMetrics: DomainMetric[]
  trends: DomainTrend[]
}

/**
 * Key metric for a domain
 */
export interface DomainMetric {
  name: string
  value: number
  unit: string
  changePercentage: number
  trend: 'UP' | 'DOWN' | 'STABLE'
}

/**
 * Trend data for a domain
 */
export interface DomainTrend {
  period: string
  value: number
  changePercentage: number
}

/**
 * Real-time metrics from all domains
 */
export interface RealTimeMetrics {
  timestamp: string
  refreshInterval: number // milliseconds
  domains: RealTimeDomainMetrics[]
  alerts: RealTimeAlert[]
  systemHealth: SystemHealth
}

/**
 * Real-time metrics for a specific domain
 */
export interface RealTimeDomainMetrics {
  domain: DomainType
  domainName: string
  status: 'OPERATIONAL' | 'DEGRADED' | 'DOWN'
  metrics: RealTimeMetric[]
  lastUpdated: string
}

/**
 * Single real-time metric
 */
export interface RealTimeMetric {
  name: string
  currentValue: number
  previousValue: number
  changePercentage: number
  unit: string
  threshold?: {
    warning: number
    critical: number
  }
  status: 'NORMAL' | 'WARNING' | 'CRITICAL'
}

/**
 * Real-time alert
 */
export interface RealTimeAlert {
  id: string
  severity: 'INFO' | 'WARNING' | 'ERROR' | 'CRITICAL'
  domain: DomainType
  message: string
  metricName: string
  currentValue: number
  threshold: number
  triggeredAt: string
}

/**
 * System health overview
 */
export interface SystemHealth {
  overallStatus: 'HEALTHY' | 'DEGRADED' | 'UNHEALTHY'
  totalDomains: number
  operationalDomains: number
  degradedDomains: number
  downDomains: number
  uptime: number // percentage
}

/**
 * Create aggregation request
 */
export interface CreateAggregationRequest {
  name: string
  description?: string
  domains: DomainType[]
  metrics: string[]
  dateRange: DateRange
  aggregationMethod: 'SUM' | 'AVG' | 'MIN' | 'MAX' | 'COUNT'
  groupBy?: string[]
  filters?: Record<string, any>
  notifyOnComplete?: boolean
  notificationEmail?: string
}

/**
 * Aggregation request entity
 */
export interface AggregationRequest {
  id: string
  name: string
  description?: string
  domains: DomainType[]
  metrics: string[]
  dateRange: DateRange
  aggregationMethod: 'SUM' | 'AVG' | 'MIN' | 'MAX' | 'COUNT'
  groupBy?: string[]
  filters?: Record<string, any>
  status: AggregationStatus
  progress: number // 0-100
  createdBy: string
  createdAt: string
  startedAt?: string
  completedAt?: string
  resultUrl?: string
  error?: string
}

/**
 * Aggregation request status
 */
export type AggregationStatus =
  | 'PENDING'
  | 'IN_PROGRESS'
  | 'COMPLETED'
  | 'FAILED'
  | 'CANCELLED'

/**
 * Cross-domain response
 */
export interface CrossDomainResponse {
  dataPoints: CrossDomainDataPoint[]
  totalRecords: number
  aggregationMetadata: AggregationMetadata
}

/**
 * Aggregation metadata
 */
export interface AggregationMetadata {
  domainsIncluded: string[]
  aggregationPeriod: DateRange
  dataQualityScore: number // 0-100
  generatedAt: string
  processingTimeMs: number
}

/**
 * Summary response
 */
export interface SummaryResponse {
  domains: DomainSummary[]
  overallSummary: OverallSummary
  generatedAt: string
}

/**
 * Overall summary across all domains
 */
export interface OverallSummary {
  totalDomains: number
  totalRecords: number
  averageDataQuality: number
  activeDomains: number
  degradedDomains: number
  inactiveDomains: number
}

/**
 * Real-time response
 */
export interface RealTimeResponse {
  metrics: RealTimeMetrics
  lastRefresh: string
  nextRefresh: string
}

/**
 * Aggregation requests list response
 */
export interface AggregationRequestsResponse extends PaginatedResponse<AggregationRequest> {}

// ============================================================================
// Service Class
// ============================================================================

class DataAggregationService {
  private readonly basePath = '/api/v1/aggregation'

  // ========================================================================
  // Cross-Domain Data Endpoints
  // ========================================================================

  /**
   * Get cross-domain aggregated data
   * Retrieves aggregated data from multiple domains based on filters
   *
   * @param params - Query parameters for filtering
   * @returns Promise<CrossDomainResponse>
   */
  async getCrossDomainData(params?: {
    domains?: DomainType[]
    metrics?: string[]
    startDate?: string
    endDate?: string
    aggregationMethod?: 'SUM' | 'AVG' | 'MIN' | 'MAX' | 'COUNT'
    groupBy?: string[]
    page?: number
    size?: number
  }): Promise<CrossDomainResponse> {
    const response = await aggregationApiClient.get<CrossDomainResponse>(
      `${this.basePath}/cross-domain`,
      { params }
    )
    return response.data
  }

  /**
   * Get cross-domain summary
   * Provides a summary overview of all domains
   *
   * @param params - Optional filters for domains
   * @returns Promise<SummaryResponse>
   */
  async getCrossDomainSummary(params?: {
    domains?: DomainType[]
    includeTrends?: boolean
    trendDays?: number
  }): Promise<SummaryResponse> {
    const response = await aggregationApiClient.get<SummaryResponse>(
      `${this.basePath}/summary`,
      { params }
    )
    return response.data
  }

  /**
   * Get real-time metrics
   * Fetches current real-time metrics from all domains
   *
   * @param domains - Optional filter for specific domains
   * @returns Promise<RealTimeResponse>
   */
  async getRealTimeMetrics(domains?: DomainType[]): Promise<RealTimeResponse> {
    const response = await aggregationApiClient.get<RealTimeResponse>(
      `${this.basePath}/real-time`,
      { params: domains ? { domains: domains.join(',') } : undefined }
    )
    return response.data
  }

  /**
   * Get domain-specific real-time metrics
   *
   * @param domain - The domain to get metrics for
   * @returns Promise<RealTimeDomainMetrics>
   */
  async getDomainRealTimeMetrics(domain: DomainType): Promise<RealTimeDomainMetrics> {
    const response = await aggregationApiClient.get<RealTimeDomainMetrics>(
      `${this.basePath}/real-time/${domain}`
    )
    return response.data
  }

  // ========================================================================
  // Aggregation Request Endpoints
  // ========================================================================

  /**
   * Create aggregation request
   * Creates a new asynchronous aggregation request
   *
   * @param request - The aggregation request details
   * @returns Promise<AggregationRequest>
   */
  async createAggregationRequest(
    request: CreateAggregationRequest
  ): Promise<AggregationRequest> {
    const response = await aggregationApiClient.post<AggregationRequest>(
      `${this.basePath}/requests`,
      request
    )
    return response.data
  }

  /**
   * Get aggregation request status
   * Retrieves the current status of an aggregation request
   *
   * @param id - The aggregation request ID
   * @returns Promise<AggregationRequest>
   */
  async getAggregationRequest(id: string): Promise<AggregationRequest> {
    const response = await aggregationApiClient.get<AggregationRequest>(
      `${this.basePath}/requests/${id}`
    )
    return response.data
  }

  /**
   * Get all aggregation requests
   * Lists all aggregation requests with pagination
   *
   * @param params - Pagination and filter parameters
   * @returns Promise<AggregationRequestsResponse>
   */
  async getAggregationRequests(params?: {
    page?: number
    size?: number
    status?: AggregationStatus
    sortBy?: string
    sortDirection?: 'asc' | 'desc'
  }): Promise<AggregationRequestsResponse> {
    const response = await aggregationApiClient.get<AggregationRequestsResponse>(
      `${this.basePath}/requests`,
      { params }
    )
    return response.data
  }

  /**
   * Cancel aggregation request
   * Cancels a pending or in-progress aggregation request
   *
   * @param id - The aggregation request ID
   * @returns Promise<void>
   */
  async cancelAggregationRequest(id: string): Promise<void> {
    await aggregationApiClient.delete(`${this.basePath}/requests/${id}`)
  }

  /**
   * Get aggregation request results
   * Downloads the results of a completed aggregation request
   *
   * @param id - The aggregation request ID
   * @returns Promise<any>
   */
  async getAggregationResults(id: string): Promise<any> {
    const response = await aggregationApiClient.get<any>(
      `${this.basePath}/requests/${id}/results`
    )
    return response.data
  }

  // ========================================================================
  // Additional Utility Endpoints
  // ========================================================================

  /**
   * Get available metrics for aggregation
   * Returns list of metrics that can be aggregated
   *
   * @param domains - Optional filter for specific domains
   * @returns Promise<string[]>
   */
  async getAvailableMetrics(domains?: DomainType[]): Promise<string[]> {
    const response = await aggregationApiClient.get<string[]>(
      `${this.basePath}/metrics`,
      { params: domains ? { domains: domains.join(',') } : undefined }
    )
    return response.data
  }

  /**
   * Get aggregation capabilities
   * Returns information about supported aggregation operations
   *
   * @returns Promise<AggregationCapabilities>
   */
  async getAggregationCapabilities(): Promise<AggregationCapabilities> {
    const response = await aggregationApiClient.get<AggregationCapabilities>(
      `${this.basePath}/capabilities`
    )
    return response.data
  }

  /**
   * Health check
   * Checks the health of the aggregation service
   *
   * @returns Promise<HealthStatus>
   */
  async healthCheck(): Promise<HealthStatus> {
    const response = await aggregationApiClient.get<HealthStatus>(
      `${this.basePath}/health`
    )
    return response.data
  }

  // ========================================================================
  // Helper Methods
  // ========================================================================

  /**
   * Poll aggregation request status until completion
   *
   * @param id - The aggregation request ID
   * @param options - Polling options
   * @returns Promise<AggregationRequest>
   */
  async pollRequestStatus(
    id: string,
    options: {
      interval?: number // milliseconds, default 2000
      maxAttempts?: number // default 30
      onProgress?: (progress: number) => void
    } = {}
  ): Promise<AggregationRequest> {
    const { interval = 2000, maxAttempts = 30, onProgress } = options

    for (let attempt = 0; attempt < maxAttempts; attempt++) {
      const request = await this.getAggregationRequest(id)

      if (onProgress) {
        onProgress(request.progress)
      }

      if (request.status === 'COMPLETED' || request.status === 'FAILED' || request.status === 'CANCELLED') {
        return request
      }

      await this.sleep(interval)
    }

    throw new Error('Aggregation request polling timed out')
  }

  /**
   * Get status color for display
   *
   * @param status - The aggregation status
   * @returns string - CSS color code
   */
  getStatusColor(status: AggregationStatus): string {
    const colors: Record<AggregationStatus, string> = {
      PENDING: '#FFA726',
      IN_PROGRESS: '#42A5F5',
      COMPLETED: '#66BB6A',
      FAILED: '#EF5350',
      CANCELLED: '#9E9E9E',
    }
    return colors[status] || '#9E9E9E'
  }

  /**
   * Get system health color
   *
   * @param status - The system health status
   * @returns string - CSS color code
   */
  getHealthColor(status: 'HEALTHY' | 'DEGRADED' | 'UNHEALTHY'): string {
    const colors: Record<string, string> = {
      HEALTHY: '#4CAF50',
      DEGRADED: '#FF9800',
      UNHEALTHY: '#F44336',
    }
    return colors[status] || '#9E9E9E'
  }

  /**
   * Format data quality score
   *
   * @param score - Data quality score (0-100)
   * @returns Object with color and label
   */
  getDataQualityInfo(score: number): { color: string; label: string } {
    if (score >= 80) {
      return { color: '#4CAF50', label: 'High' }
    }
    if (score >= 60) {
      return { color: '#FF9800', label: 'Medium' }
    }
    return { color: '#F44336', label: 'Low' }
  }

  /**
   * Sleep utility for polling
   */
  private sleep(ms: number): Promise<void> {
    return new Promise((resolve) => setTimeout(resolve, ms))
  }
}

// ============================================================================
// Additional Types
// ============================================================================

/**
 * Aggregation capabilities
 */
export interface AggregationCapabilities {
  supportedDomains: DomainType[]
  supportedMetrics: MetricCapability[]
  supportedMethods: AggregationMethod[]
  maxDateRangeDays: number
  maxGroupByFields: number
}

/**
 * Metric capability
 */
export interface MetricCapability {
  name: string
  category: KPICategory
  unit: string
  supportedMethods: AggregationMethod[]
  availableForDomains: DomainType[]
}

/**
 * Aggregation method details
 */
export interface AggregationMethod {
  method: 'SUM' | 'AVG' | 'MIN' | 'MAX' | 'COUNT'
  description: string
  applicableTypes: ('numeric' | 'categorical')[]
}

/**
 * Health status
 */
export interface HealthStatus {
  status: 'UP' | 'DOWN' | 'DEGRADED'
  timestamp: string
  version: string
  dependencies: DependencyHealth[]
}

/**
 * Dependency health
 */
export interface DependencyHealth {
  service: string
  status: 'UP' | 'DOWN' | 'DEGRADED'
  latency?: number
}

// Export singleton instance
export const dataAggregationService = new DataAggregationService()

// Export type for use in components
export type { DataAggregationService }
