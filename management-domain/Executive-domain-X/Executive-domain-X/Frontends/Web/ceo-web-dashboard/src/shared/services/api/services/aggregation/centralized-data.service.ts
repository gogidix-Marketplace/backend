/**
 * Centralized Data Aggregation Service API Client
 *
 * Connects to: centralized-data-aggregation-service (Port 9203)
 * Base Path: /api/v1/centralized
 *
 * Endpoints:
 * - GET /all - Get all centralized data
 * - GET /domain/{domain} - Get domain data
 * - GET /kpi/{kpiCode} - Get KPI from all sources
 * - GET /health - Get data sources health
 * - POST /refresh - Refresh data from sources
 */

import { centralizedDataAggregationApiClient } from '../../client/axios-client'
import type {
  PaginatedResponse,
  DateRange,
  DomainType,
  KPICategory,
  KPIStatus,
  KPITrend,
} from '../../client/types'

// ============================================================================
// Types
// ============================================================================

/**
 * Centralized data from all domains
 * Aggregated view of data across the entire ecosystem
 */
export interface CentralizedData {
  timestamp: string
  totalRecords: number
  domains: DomainCentralizedData[]
  summary: CentralizedSummary
  metadata: CentralizedMetadata
}

/**
 * Domain-specific centralized data
 */
export interface DomainCentralizedData {
  domain: DomainType
  domainName: string
  recordCount: number
  lastUpdated: string
  dataQuality: DataQuality
  kpis: KPIData[]
  metrics: MetricData[]
  trends: TrendData[]
}

/**
 * Data quality assessment
 */
export interface DataQuality {
  score: number // 0-100
  level: 'HIGH' | 'MEDIUM' | 'LOW'
  issues: QualityIssue[]
  lastAssessed: string
}

/**
 * Data quality issue
 */
export interface QualityIssue {
  id: string
  severity: 'INFO' | 'WARNING' | 'ERROR' | 'CRITICAL'
  category: 'COMPLETENESS' | 'ACCURACY' | 'CONSISTENCY' | 'TIMELINESS' | 'VALIDITY'
  description: string
  affectedRecords: number
  detectedAt: string
}

/**
 * KPI data from centralized aggregation
 */
export interface KPIData {
  code: string
  name: string
  description?: string
  category: KPICategory
  value: number
  target: number
  unit: string
  status: KPIStatus
  trend: KPITrend
  changePercentage: number
  previousValue: number
  sources: KPISource[]
  lastUpdated: string
}

/**
 * KPI source information
 */
export interface KPISource {
  domain: DomainType
  sourceService: string
  value: number
  contribution: number // percentage contribution to aggregated value
  lastSync: string
  status: 'SYNCED' | 'PENDING' | 'FAILED'
}

/**
 * Metric data point
 */
export interface MetricData {
  name: string
  value: number
  unit: string
  timestamp: string
  metadata?: Record<string, any>
}

/**
 * Trend data for analysis
 */
export interface TrendData {
  period: string
  value: number
  changePercentage: number
  direction: 'UP' | 'DOWN' | 'STABLE'
}

/**
 * Centralized summary across all domains
 */
export interface CentralizedSummary {
  totalDomains: number
  activeDomains: number
  totalKPIs: number
  averageDataQuality: number
  overallHealth: 'HEALTHY' | 'DEGRADED' | 'UNHEALTHY'
  lastSyncTime: string
  nextSyncTime: string
}

/**
 * Centralized metadata
 */
export interface CentralizedMetadata {
  generatedAt: string
  processingTimeMs: number
  aggregationMethod: AggregationMethod
  dateRange?: DateRange
  version: string
}

/**
 * Aggregation method
 */
export type AggregationMethod =
  | 'SUM'
  | 'AVERAGE'
  | 'WEIGHTED_AVERAGE'
  | 'MEDIAN'
  | 'MIN'
  | 'MAX'
  | 'LATEST'
  | 'CUSTOM'

/**
 * Domain-specific data response
 */
export interface DomainData {
  domain: DomainType
  domainName: string
  kpis: KPIData[]
  summary: DomainSummary
  sources: DomainSource[]
  lastUpdated: string
}

/**
 * Domain summary
 */
export interface DomainSummary {
  totalKPIs: number
  activeKPIs: number
  dataQualityScore: number
  lastSync: string
  status: 'ACTIVE' | 'INACTIVE' | 'DEGRADED'
}

/**
 * Domain data source
 */
export interface DomainSource {
  serviceName: string
  endpoint: string
  status: 'CONNECTED' | 'DISCONNECTED' | 'DEGRADED'
  lastSync: string
  recordCount: number
}

/**
 * KPI data from all sources
 */
export interface KPISources {
  kpiCode: string
  kpiName: string
  category: KPICategory
  sources: KPIDetailedSource[]
  aggregatedValue: number
  aggregationMethod: AggregationMethod
  lastUpdated: string
}

/**
 * Detailed KPI source information
 */
export interface KPIDetailedSource {
  domain: DomainType
  serviceName: string
  endpoint: string
  value: number
  unit: string
  weight: number // for weighted average aggregation
  lastSync: string
  status: 'SYNCED' | 'PENDING' | 'FAILED'
  error?: string
  metadata?: Record<string, any>
}

/**
 * Data sources health status
 */
export interface DataSourcesHealth {
  timestamp: string
  overallStatus: 'HEALTHY' | 'DEGRADED' | 'UNHEALTHY'
  totalSources: number
  healthySources: number
  degradedSources: number
  unhealthySources: number
  sources: SourceStatus[]
  recommendations: HealthRecommendation[]
}

/**
 * Individual source status
 */
export interface SourceStatus {
  id: string
  domain: DomainType
  serviceName: string
  endpoint: string
  status: 'HEALTHY' | 'DEGRADED' | 'UNHEALTHY' | 'UNKNOWN'
  responseTime?: number // milliseconds
  lastCheck: string
  uptime: number // percentage
  lastSyncTime?: string
  errorCount: number
  lastError?: SourceError
}

/**
 * Source error details
 */
export interface SourceError {
  code: string
  message: string
  timestamp: string
  resolved?: boolean
}

/**
 * Health recommendation
 */
export interface HealthRecommendation {
  priority: 'LOW' | 'MEDIUM' | 'HIGH' | 'CRITICAL'
  category: 'PERFORMANCE' | 'RELIABILITY' | 'DATA_QUALITY' | 'SYNC'
  recommendation: string
  affectedSources: string[]
}

/**
 * Refresh request
 */
export interface RefreshRequest {
  sources?: string[] // specific sources to refresh, empty means all
  force?: boolean // bypass cache and force refresh
  domains?: DomainType[] // specific domains to refresh
  kpis?: string[] // specific KPIs to refresh
}

/**
 * Refresh response
 */
export interface RefreshResponse {
  refreshId: string
  status: 'INITIATED' | 'IN_PROGRESS' | 'COMPLETED' | 'FAILED'
  initiatedAt: string
  estimatedCompletion?: string
  sourcesToRefresh: RefreshSource[]
  progress: RefreshProgress
}

/**
 * Source to refresh
 */
export interface RefreshSource {
  sourceId: string
  domain: DomainType
  serviceName: string
  status: 'PENDING' | 'IN_PROGRESS' | 'COMPLETED' | 'FAILED'
  startedAt?: string
  completedAt?: string
  recordsProcessed?: number
  error?: string
}

/**
 * Refresh progress information
 */
export interface RefreshProgress {
  totalSources: number
  completedSources: number
  failedSources: number
  percentage: number // 0-100
  currentStep: string
  estimatedTimeRemaining?: string
}

/**
 * Refresh status response
 */
export interface RefreshStatusResponse extends RefreshResponse {
  completedAt?: string
  totalRecordsProcessed: number
  errors: RefreshError[]
}

/**
 * Refresh error details
 */
export interface RefreshError {
  sourceId: string
  error: string
  timestamp: string
}

// ============================================================================
// Service Class
// ============================================================================

class CentralizedDataService {
  private readonly basePath = '/api/v1/centralized'

  // ========================================================================
  // Data Retrieval Endpoints
  // ========================================================================

  /**
   * Get all centralized data
   * Retrieves aggregated data from all domains
   *
   * @param params - Optional query parameters
   * @returns Promise<CentralizedData>
   */
  async getAllCentralizedData(params?: {
    domains?: DomainType[]
    kpis?: string[]
    startDate?: string
    endDate?: string
    includeTrends?: boolean
    aggregationMethod?: AggregationMethod
  }): Promise<CentralizedData> {
    const response = await centralizedDataAggregationApiClient.get<CentralizedData>(
      `${this.basePath}/all`,
      { params }
    )
    return response.data
  }

  /**
   * Get domain-specific data
   * Retrieves centralized data for a specific domain
   *
   * @param domain - The domain to get data for
   * @param params - Optional query parameters
   * @returns Promise<DomainData>
   */
  async getDomainData(
    domain: DomainType,
    params?: {
      kpis?: string[]
      includeSources?: boolean
      includeTrends?: boolean
    }
  ): Promise<DomainData> {
    const response = await centralizedDataAggregationApiClient.get<DomainData>(
      `${this.basePath}/domain/${domain}`,
      { params }
    )
    return response.data
  }

  /**
   * Get KPI data from all sources
   * Retrieves a specific KPI aggregated from all domain sources
   *
   * @param kpiCode - The KPI code to retrieve
   * @param params - Optional query parameters
   * @returns Promise<KPISources>
   */
  async getKPISources(
    kpiCode: string,
    params?: {
      domains?: DomainType[]
      includeMetadata?: boolean
      startDate?: string
      endDate?: string
    }
  ): Promise<KPISources> {
    const response = await centralizedDataAggregationApiClient.get<KPISources>(
      `${this.basePath}/kpi/${kpiCode}`,
      { params }
    )
    return response.data
  }

  /**
   * Get multiple KPIs from all sources
   *
   * @param kpiCodes - Array of KPI codes to retrieve
   * @param params - Optional query parameters
   * @returns Promise<KPISources[]>
   */
  async getMultipleKPISources(
    kpiCodes: string[],
    params?: {
      domains?: DomainType[]
      includeMetadata?: boolean
    }
  ): Promise<KPISources[]> {
    const response = await centralizedDataAggregationApiClient.get<KPISources[]>(
      `${this.basePath}/kpi/batch`,
      {
        params: {
          ...params,
          kpiCodes: kpiCodes.join(','),
        },
      }
    )
    return response.data
  }

  // ========================================================================
  // Health and Monitoring Endpoints
  // ========================================================================

  /**
   * Get data sources health
   * Retrieves health status of all data sources
   *
   * @param params - Optional filters
   * @returns Promise<DataSourcesHealth>
   */
  async getDataSourcesHealth(params?: {
    domains?: DomainType[]
    includeDetails?: boolean
  }): Promise<DataSourcesHealth> {
    const response = await centralizedDataAggregationApiClient.get<DataSourcesHealth>(
      `${this.basePath}/health`,
      { params }
    )
    return response.data
  }

  /**
   * Get specific source health
   *
   * @param sourceId - The source ID to check
   * @returns Promise<SourceStatus>
   */
  async getSourceHealth(sourceId: string): Promise<SourceStatus> {
    const response = await centralizedDataAggregationApiClient.get<SourceStatus>(
      `${this.basePath}/health/source/${sourceId}`
    )
    return response.data
  }

  /**
   * Get domain sources health
   *
   * @param domain - The domain to check sources for
   * @returns Promise<SourceStatus[]>
   */
  async getDomainSourceHealth(domain: DomainType): Promise<SourceStatus[]> {
    const response = await centralizedDataAggregationApiClient.get<SourceStatus[]>(
      `${this.basePath}/health/domain/${domain}`
    )
    return response.data
  }

  // ========================================================================
  // Refresh Endpoints
  // ========================================================================

  /**
   * Refresh data from sources
   * Initiates a refresh of data from all or specific sources
   *
   * @param request - Refresh request parameters
   * @returns Promise<RefreshResponse>
   */
  async refreshData(request: RefreshRequest = {}): Promise<RefreshResponse> {
    const response = await centralizedDataAggregationApiClient.post<RefreshResponse>(
      `${this.basePath}/refresh`,
      request
    )
    return response.data
  }

  /**
   * Get refresh status
   * Checks the status of an ongoing refresh operation
   *
   * @param refreshId - The refresh operation ID
   * @returns Promise<RefreshStatusResponse>
   */
  async getRefreshStatus(refreshId: string): Promise<RefreshStatusResponse> {
    const response = await centralizedDataAggregationApiClient.get<RefreshStatusResponse>(
      `${this.basePath}/refresh/${refreshId}`
    )
    return response.data
  }

  /**
   * Cancel refresh operation
   *
   * @param refreshId - The refresh operation ID to cancel
   * @returns Promise<void>
   */
  async cancelRefresh(refreshId: string): Promise<void> {
    await centralizedDataAggregationApiClient.delete(`${this.basePath}/refresh/${refreshId}`)
  }

  // ========================================================================
  // Configuration and Capabilities Endpoints
  // ========================================================================

  /**
   * Get available KPIs
   * Returns list of KPIs available in centralized aggregation
   *
   * @param params - Optional filters
   * @returns Promise<string[]>
   */
  async getAvailableKPIs(params?: {
    categories?: KPICategory[]
    domains?: DomainType[]
  }): Promise<string[]> {
    const response = await centralizedDataAggregationApiClient.get<string[]>(
      `${this.basePath}/kpi`,
      { params }
    )
    return response.data
  }

  /**
   * Get supported domains
   * Returns list of domains supported by centralized aggregation
   *
   * @returns Promise<DomainType[]>
   */
  async getSupportedDomains(): Promise<DomainType[]> {
    const response = await centralizedDataAggregationApiClient.get<DomainType[]>(
      `${this.basePath}/domains`
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
    const response = await centralizedDataAggregationApiClient.get<AggregationCapabilities>(
      `${this.basePath}/capabilities`
    )
    return response.data
  }

  // ========================================================================
  // Helper Methods
  // ========================================================================

  /**
   * Poll refresh status until completion
   *
   * @param refreshId - The refresh operation ID
   * @param options - Polling options
   * @returns Promise<RefreshStatusResponse>
   */
  async pollRefreshStatus(
    refreshId: string,
    options: {
      interval?: number // milliseconds, default 2000
      maxAttempts?: number // default 30
      onProgress?: (progress: RefreshProgress) => void
    } = {}
  ): Promise<RefreshStatusResponse> {
    const { interval = 2000, maxAttempts = 30, onProgress } = options

    for (let attempt = 0; attempt < maxAttempts; attempt++) {
      const status = await this.getRefreshStatus(refreshId)

      if (onProgress) {
        onProgress(status.progress)
      }

      if (
        status.status === 'COMPLETED' ||
        status.status === 'FAILED'
      ) {
        return status
      }

      await this.sleep(interval)
    }

    throw new Error('Refresh polling timed out')
  }

  /**
   * Get status color for display
   *
   * @param status - The health status
   * @returns string - CSS color code
   */
  getHealthColor(status: 'HEALTHY' | 'DEGRADED' | 'UNHEALTHY' | 'UNKNOWN'): string {
    const colors: Record<string, string> = {
      HEALTHY: '#4CAF50',
      DEGRADED: '#FF9800',
      UNHEALTHY: '#F44336',
      UNKNOWN: '#9E9E9E',
    }
    return colors[status] || '#9E9E9E'
  }

  /**
   * Get KPI status color for display
   *
   * @param status - The KPI status
   * @returns string - CSS color code
   */
  getKPIStatusColor(status: KPIStatus): string {
    const colors: Record<KPIStatus, string> = {
      ON_TRACK: '#4CAF50',
      AT_RISK: '#FF9800',
      BEHIND: '#F44336',
      AHEAD: '#2196F3',
    }
    return colors[status] || '#9E9E9E'
  }

  /**
   * Get data quality info
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
   * Format aggregation method for display
   *
   * @param method - The aggregation method
   * @returns string - Display name
   */
  formatAggregationMethod(method: AggregationMethod): string {
    const displayNames: Record<AggregationMethod, string> = {
      SUM: 'Sum',
      AVERAGE: 'Average',
      WEIGHTED_AVERAGE: 'Weighted Average',
      MEDIAN: 'Median',
      MIN: 'Minimum',
      MAX: 'Maximum',
      LATEST: 'Latest Value',
      CUSTOM: 'Custom',
    }
    return displayNames[method] || method
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
  supportedKPIs: KPICapability[]
  supportedMethods: AggregationMethod[]
  maxDateRangeDays: number
  refreshInterval: number // minimum seconds between refreshes
  cacheEnabled: boolean
}

/**
 * KPI capability
 */
export interface KPICapability {
  code: string
  name: string
  category: KPICategory
  unit: string
  supportedMethods: AggregationMethod[]
  availableForDomains: DomainType[]
  defaultAggregationMethod: AggregationMethod
}

// Export singleton instance
export const centralizedDataService = new CentralizedDataService()

// Export type for use in components
export type { CentralizedDataService }
