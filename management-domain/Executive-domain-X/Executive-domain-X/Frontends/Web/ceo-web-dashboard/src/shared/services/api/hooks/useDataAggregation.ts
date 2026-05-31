/**
 * React Hook: useDataAggregation
 *
 * Provides data aggregation hooks for cross-domain data, summaries,
 * real-time metrics, and centralized data access
 * Handles loading, error states, caching, and auto-refresh
 */

import { useState, useEffect, useCallback } from 'react'
import { dataAggregationService } from '../services/aggregation/data-aggregation.service'
import { metricsAggregationService } from '../services/aggregation/metrics-aggregation.service'
import { centralizedDataService } from '../services/aggregation/centralized-data.service'
import type {
  CrossDomainResponse,
  CrossDomainDataPoint,
  SummaryResponse,
  DomainSummary,
  RealTimeResponse,
  RealTimeMetrics,
  RealTimeDomainMetrics,
  AggregationRequest,
  AggregationStatus,
  CentralizedData,
  DomainData,
  KPISources,
  DataSourcesHealth,
  SourceStatus,
  RefreshRequest,
  RefreshResponse,
  RefreshStatusResponse,
} from '../services/aggregation'
import type {
  AggregatedKPI,
  KPITrendAnalysis,
  TrendingResponse,
  MetricComparison,
  DomainKPIsResponse,
  KPIBatchResponse,
  KPIBatchRequest,
} from '../services/aggregation/metrics-aggregation.service'
import type {
  DomainType,
  DateRange,
  KPICategory,
  PaginatedResponse,
} from '../client/types'

// ============================================================================
// Hook Result Types
// ============================================================================

interface UseCrossDomainDataResult {
  data: CrossDomainResponse | null
  loading: boolean
  error: string | null
  refetch: () => Promise<void>
}

interface UseCrossDomainSummaryResult {
  summary: SummaryResponse | null
  loading: boolean
  error: string | null
  refetch: () => Promise<void>
}

interface UseRealTimeMetricsResult {
  metrics: RealTimeMetrics | null
  loading: boolean
  error: string | null
  refetch: () => Promise<void>
  isRefreshing: boolean
}

interface UseDomainRealTimeMetricsResult {
  metrics: RealTimeDomainMetrics | null
  loading: boolean
  error: string | null
  refetch: () => Promise<void>
}

interface UseCentralizedDataResult {
  data: CentralizedData | null
  loading: boolean
  error: string | null
  refetch: () => Promise<void>
  refresh: (request?: RefreshRequest) => Promise<void>
}

interface UseDomainDataResult {
  data: DomainData | null
  loading: boolean
  error: string | null
  refetch: () => Promise<void>
}

interface UseKPISourcesResult {
  kpiSources: KPISources | null
  loading: boolean
  error: string | null
  refetch: () => Promise<void>
}

interface UseDataSourceHealthResult {
  health: DataSourcesHealth | null
  loading: boolean
  error: string | null
  refetch: () => Promise<void>
  isHealthy: boolean | null
}

interface UseAggregatedKPIResult {
  kpi: AggregatedKPI | null
  loading: boolean
  error: string | null
  refetch: () => Promise<void>
}

interface UseTrendingMetricsResult {
  trending: TrendingResponse | null
  loading: boolean
  error: string | null
  refetch: () => Promise<void>
}

interface UseMetricComparisonResult {
  comparison: MetricComparison | null
  loading: boolean
  error: string | null
  refetch: () => Promise<void>
}

interface UseAggregationRequestResult {
  request: AggregationRequest | null
  loading: boolean
  error: string | null
  refetch: () => Promise<void>
  cancel: () => Promise<void>
  poll: (options?: { interval?: number; maxAttempts?: number; onProgress?: (progress: number) => void }) => Promise<AggregationRequest>
}

interface UseKPIBatchResult {
  response: KPIBatchResponse | null
  loading: boolean
  error: string | null
  refetch: (request: KPIBatchRequest) => Promise<void>
}

interface UseRefreshStatusResult {
  refreshStatus: RefreshStatusResponse | null
  loading: boolean
  error: string | null
  refetch: () => Promise<void>
  isComplete: boolean
}

// ============================================================================
// Hook: Cross-Domain Data
// ============================================================================

/**
 * Get cross-domain aggregated data
 * Retrieves aggregated data from multiple domains
 */
export function useCrossDomainData(params?: {
  domains?: DomainType[]
  metrics?: string[]
  startDate?: string
  endDate?: string
  aggregationMethod?: 'SUM' | 'AVG' | 'MIN' | 'MAX' | 'COUNT'
  groupBy?: string[]
  page?: number
  size?: number
}, refetchInterval?: number): UseCrossDomainDataResult {
  const [data, setData] = useState<CrossDomainResponse | null>(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  const fetchData = useCallback(async () => {
    setLoading(true)
    setError(null)
    try {
      const response = await dataAggregationService.getCrossDomainData(params)
      setData(response)
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to fetch cross-domain data')
      console.error('Error fetching cross-domain data:', err)
    } finally {
      setLoading(false)
    }
  }, [params])

  useEffect(() => {
    fetchData()

    if (refetchInterval) {
      const interval = setInterval(fetchData, refetchInterval)
      return () => clearInterval(interval)
    }
  }, [fetchData, refetchInterval])

  return { data, loading, error, refetch: fetchData }
}

// ============================================================================
// Hook: Cross-Domain Summary
// ============================================================================

/**
 * Get cross-domain summary
 * Provides a summary overview of all domains
 */
export function useCrossDomainSummary(params?: {
  domains?: DomainType[]
  includeTrends?: boolean
  trendDays?: number
}, refetchInterval?: number): UseCrossDomainSummaryResult {
  const [summary, setSummary] = useState<SummaryResponse | null>(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  const fetchData = useCallback(async () => {
    setLoading(true)
    setError(null)
    try {
      const response = await dataAggregationService.getCrossDomainSummary(params)
      setSummary(response)
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to fetch cross-domain summary')
      console.error('Error fetching cross-domain summary:', err)
    } finally {
      setLoading(false)
    }
  }, [params])

  useEffect(() => {
    fetchData()

    if (refetchInterval) {
      const interval = setInterval(fetchData, refetchInterval)
      return () => clearInterval(interval)
    }
  }, [fetchData, refetchInterval])

  return { summary, loading, error, refetch: fetchData }
}

// ============================================================================
// Hook: Real-Time Metrics
// ============================================================================

/**
 * Get real-time metrics
 * Fetches current real-time metrics from all domains with auto-refresh
 */
export function useRealTimeMetrics(
  domains?: DomainType[],
  refreshInterval = 30000 // Default 30 seconds
): UseRealTimeMetricsResult {
  const [metrics, setMetrics] = useState<RealTimeMetrics | null>(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)
  const [isRefreshing, setIsRefreshing] = useState(false)

  const fetchData = useCallback(async () => {
    if (isRefreshing) return // Prevent overlapping refreshes

    setIsRefreshing(true)
    if (!metrics) setLoading(true)
    setError(null)
    try {
      const response = await dataAggregationService.getRealTimeMetrics(domains)
      setMetrics(response.metrics)
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to fetch real-time metrics')
      console.error('Error fetching real-time metrics:', err)
    } finally {
      setLoading(false)
      setIsRefreshing(false)
    }
  }, [domains, metrics, isRefreshing])

  useEffect(() => {
    fetchData()

    if (refreshInterval > 0) {
      const interval = setInterval(fetchData, refreshInterval)
      return () => clearInterval(interval)
    }
  }, [fetchData, refreshInterval])

  return { metrics, loading, error, refetch: fetchData, isRefreshing }
}

// ============================================================================
// Hook: Domain Real-Time Metrics
// ============================================================================

/**
 * Get domain-specific real-time metrics
 * Fetches current real-time metrics for a specific domain
 */
export function useDomainRealTimeMetrics(
  domain: DomainType,
  refreshInterval?: number
): UseDomainRealTimeMetricsResult {
  const [metrics, setMetrics] = useState<RealTimeDomainMetrics | null>(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  const fetchData = useCallback(async () => {
    setLoading(true)
    setError(null)
    try {
      const response = await dataAggregationService.getDomainRealTimeMetrics(domain)
      setMetrics(response)
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to fetch domain metrics')
      console.error('Error fetching domain real-time metrics:', err)
    } finally {
      setLoading(false)
    }
  }, [domain])

  useEffect(() => {
    fetchData()

    if (refreshInterval) {
      const interval = setInterval(fetchData, refreshInterval)
      return () => clearInterval(interval)
    }
  }, [fetchData, refreshInterval])

  return { metrics, loading, error, refetch: fetchData }
}

// ============================================================================
// Hook: Centralized Data
// ============================================================================

/**
 * Get all centralized data
 * Retrieves aggregated data from all domains
 */
export function useCentralizedData(params?: {
  domains?: DomainType[]
  kpis?: string[]
  startDate?: string
  endDate?: string
  includeTrends?: boolean
}, refetchInterval?: number): UseCentralizedDataResult {
  const [data, setData] = useState<CentralizedData | null>(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)
  const [isRefreshing, setIsRefreshing] = useState(false)

  const fetchData = useCallback(async () => {
    setLoading(true)
    setError(null)
    try {
      const response = await centralizedDataService.getAllCentralizedData(params)
      setData(response)
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to fetch centralized data')
      console.error('Error fetching centralized data:', err)
    } finally {
      setLoading(false)
    }
  }, [params])

  const refresh = useCallback(async (request?: RefreshRequest) => {
    setIsRefreshing(true)
    setError(null)
    try {
      const refreshResponse = await centralizedDataService.refreshData(request)
      // Poll for completion if initiated
      if (refreshResponse.status === 'INITIATED' || refreshResponse.status === 'IN_PROGRESS') {
        await centralizedDataService.pollRefreshStatus(refreshResponse.refreshId)
      }
      // Refetch data after refresh completes
      await fetchData()
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to refresh data')
      console.error('Error refreshing data:', err)
    } finally {
      setIsRefreshing(false)
    }
  }, [fetchData])

  useEffect(() => {
    fetchData()

    if (refetchInterval) {
      const interval = setInterval(fetchData, refetchInterval)
      return () => clearInterval(interval)
    }
  }, [fetchData, refetchInterval])

  return { data, loading, error, refetch: fetchData, refresh }
}

// ============================================================================
// Hook: Domain Data
// ============================================================================

/**
 * Get domain-specific centralized data
 */
export function useDomainData(
  domain: DomainType,
  params?: {
    kpis?: string[]
    includeSources?: boolean
    includeTrends?: boolean
  }
): UseDomainDataResult {
  const [data, setData] = useState<DomainData | null>(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  const fetchData = useCallback(async () => {
    setLoading(true)
    setError(null)
    try {
      const response = await centralizedDataService.getDomainData(domain, params)
      setData(response)
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to fetch domain data')
      console.error('Error fetching domain data:', err)
    } finally {
      setLoading(false)
    }
  }, [domain, params])

  useEffect(() => {
    fetchData()
  }, [fetchData])

  return { data, loading, error, refetch: fetchData }
}

// ============================================================================
// Hook: KPI Sources
// ============================================================================

/**
 * Get KPI data from all sources
 */
export function useKPISources(
  kpiCode: string,
  params?: {
    domains?: DomainType[]
    includeMetadata?: boolean
    startDate?: string
    endDate?: string
  }
): UseKPISourcesResult {
  const [kpiSources, setKpiSources] = useState<KPISources | null>(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  const fetchData = useCallback(async () => {
    if (!kpiCode) return

    setLoading(true)
    setError(null)
    try {
      const response = await centralizedDataService.getKPISources(kpiCode, params)
      setKpiSources(response)
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to fetch KPI sources')
      console.error('Error fetching KPI sources:', err)
    } finally {
      setLoading(false)
    }
  }, [kpiCode, params])

  useEffect(() => {
    fetchData()
  }, [fetchData])

  return { kpiSources, loading, error, refetch: fetchData }
}

// ============================================================================
// Hook: Data Source Health
// ============================================================================

/**
 * Get data source health status
 * Monitors health of all data sources with auto-refresh
 */
export function useDataSourceHealth(
  params?: {
    domains?: DomainType[]
    includeDetails?: boolean
  },
  healthCheckInterval = 60000 // Default 1 minute
): UseDataSourceHealthResult {
  const [health, setHealth] = useState<DataSourcesHealth | null>(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  const fetchData = useCallback(async () => {
    setLoading(true)
    setError(null)
    try {
      const response = await centralizedDataService.getDataSourcesHealth(params)
      setHealth(response)
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to fetch data source health')
      console.error('Error fetching data source health:', err)
    } finally {
      setLoading(false)
    }
  }, [params])

  useEffect(() => {
    fetchData()

    if (healthCheckInterval > 0) {
      const interval = setInterval(fetchData, healthCheckInterval)
      return () => clearInterval(interval)
    }
  }, [fetchData, healthCheckInterval])

  const isHealthy = health?.overallStatus === 'HEALTHY' ? true :
                   health?.overallStatus === 'UNHEALTHY' ? false :
                   null

  return { health, loading, error, refetch: fetchData, isHealthy }
}

// ============================================================================
// Hook: Aggregated KPI
// ============================================================================

/**
 * Get aggregated KPI data by code
 */
export function useAggregatedKPI(
  kpiCode: string,
  options?: {
    includeSources?: boolean
    includeHistorical?: boolean
    historicalDays?: number
  }
): UseAggregatedKPIResult {
  const [kpi, setKpi] = useState<AggregatedKPI | null>(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  const fetchData = useCallback(async () => {
    if (!kpiCode) return

    setLoading(true)
    setError(null)
    try {
      const response = await metricsAggregationService.getKPIByCode(kpiCode, options)
      setKpi(response)
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to fetch KPI')
      console.error('Error fetching aggregated KPI:', err)
    } finally {
      setLoading(false)
    }
  }, [kpiCode, options])

  useEffect(() => {
    fetchData()
  }, [fetchData])

  return { kpi, loading, error, refetch: fetchData }
}

// ============================================================================
// Hook: KPI Batch
// ============================================================================

/**
 * Get multiple KPIs in a single batch request
 */
export function useKPIBatch(
  request: KPIBatchRequest | null
): UseKPIBatchResult {
  const [response, setResponse] = useState<KPIBatchResponse | null>(null)
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState<string | null>(null)

  const fetchData = useCallback(async (req: KPIBatchRequest) => {
    setLoading(true)
    setError(null)
    try {
      const res = await metricsAggregationService.getKPIsBatch(req)
      setResponse(res)
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to fetch KPIs')
      console.error('Error fetching KPI batch:', err)
    } finally {
      setLoading(false)
    }
  }, [])

  useEffect(() => {
    if (request) {
      fetchData(request)
    }
  }, [request, fetchData])

  return { response, loading, error, refetch: fetchData }
}

// ============================================================================
// Hook: Domain KPIs
// ============================================================================

/**
 * Get all KPIs for a specific domain
 */
export function useDomainKPIs(
  domain: string,
  params?: {
    category?: KPICategory
    status?: string
    page?: number
    size?: number
  }
): DomainKPIsResponse | null {
  const [data, setData] = useState<DomainKPIsResponse | null>(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  const fetchData = useCallback(async () => {
    if (!domain) return

    setLoading(true)
    setError(null)
    try {
      const response = await metricsAggregationService.getDomainKPIs(domain, params)
      setData(response)
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to fetch domain KPIs')
      console.error('Error fetching domain KPIs:', err)
    } finally {
      setLoading(false)
    }
  }, [domain, params])

  useEffect(() => {
    fetchData()
  }, [fetchData])

  return { data, loading, error, refetch: fetchData }
}

// ============================================================================
// Hook: Trending Metrics
// ============================================================================

/**
 * Get trending metrics
 * Returns metrics with significant upward or downward trends
 */
export function useTrendingMetrics(
  request?: {
    category?: KPICategory
    domain?: string
    period?: {
      start: string
      end: string
    }
    threshold?: number
    limit?: number
  },
  refetchInterval?: number
): UseTrendingMetricsResult {
  const [trending, setTrending] = useState<TrendingResponse | null>(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  const fetchData = useCallback(async () => {
    setLoading(true)
    setError(null)
    try {
      const response = await metricsAggregationService.getTrendingMetrics(request)
      setTrending(response)
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to fetch trending metrics')
      console.error('Error fetching trending metrics:', err)
    } finally {
      setLoading(false)
    }
  }, [request])

  useEffect(() => {
    fetchData()

    if (refetchInterval) {
      const interval = setInterval(fetchData, refetchInterval)
      return () => clearInterval(interval)
    }
  }, [fetchData, refetchInterval])

  return { trending, loading, error, refetch: fetchData }
}

// ============================================================================
// Hook: KPI Trend
// ============================================================================

/**
 * Get trend data for a specific KPI
 */
export function useKPITrend(
  kpiCode: string,
  params?: {
    period?: 'DAILY' | 'WEEKLY' | 'MONTHLY' | 'QUARTERLY'
    startDate?: string
    endDate?: string
    points?: number
  }
): { trend: KPITrendAnalysis | null; loading: boolean; error: string | null } {
  const [trend, setTrend] = useState<KPITrendAnalysis | null>(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  const fetchData = useCallback(async () => {
    if (!kpiCode) return

    setLoading(true)
    setError(null)
    try {
      const response = await metricsAggregationService.getKPITrend(kpiCode, params)
      setTrend(response)
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to fetch KPI trend')
      console.error('Error fetching KPI trend:', err)
    } finally {
      setLoading(false)
    }
  }, [kpiCode, params])

  useEffect(() => {
    fetchData()
  }, [fetchData])

  return { trend, loading, error }
}

// ============================================================================
// Hook: Metric Comparison
// ============================================================================

/**
 * Compare metrics across domains, regions, or time periods
 */
export function useMetricComparison(
  request: {
    metricCode: string
    comparisonType: 'DOMAIN' | 'REGION' | 'PERIOD' | 'TARGET' | 'CUSTOM'
    entities?: string[]
    dateRange: DateRange
    includeInsights?: boolean
    groupBy?: string
  } | null
): UseMetricComparisonResult {
  const [comparison, setComparison] = useState<MetricComparison | null>(null)
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState<string | null>(null)

  const fetchData = useCallback(async () => {
    if (!request) return

    setLoading(true)
    setError(null)
    try {
      const response = await metricsAggregationService.compareMetrics(request)
      setComparison(response)
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to fetch comparison')
      console.error('Error fetching metric comparison:', err)
    } finally {
      setLoading(false)
    }
  }, [request])

  useEffect(() => {
    fetchData()
  }, [fetchData])

  return { comparison, loading, error, refetch: fetchData }
}

// ============================================================================
// Hook: Aggregation Request
// ============================================================================

/**
 * Manage an aggregation request
 * Creates, polls, and cancels aggregation requests
 */
export function useAggregationRequest(
  initialRequestId?: string
): UseAggregationRequestResult {
  const [request, setRequest] = useState<AggregationRequest | null>(null)
  const [loading, setLoading] = useState(!!initialRequestId)
  const [error, setError] = useState<string | null>(null)

  const fetchData = useCallback(async (id: string) => {
    setLoading(true)
    setError(null)
    try {
      const response = await dataAggregationService.getAggregationRequest(id)
      setRequest(response)
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to fetch request status')
      console.error('Error fetching aggregation request:', err)
    } finally {
      setLoading(false)
    }
  }, [])

  const cancel = useCallback(async () => {
    if (!request?.id) return

    setLoading(true)
    setError(null)
    try {
      await dataAggregationService.cancelAggregationRequest(request.id)
      setRequest((prev) => prev ? { ...prev, status: 'CANCELLED' } : null)
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to cancel request')
      console.error('Error canceling aggregation request:', err)
    } finally {
      setLoading(false)
    }
  }, [request])

  const poll = useCallback(async (options?: {
    interval?: number
    maxAttempts?: number
    onProgress?: (progress: number) => void
  }) => {
    if (!request?.id) {
      throw new Error('No request ID to poll')
    }

    setLoading(true)
    try {
      const result = await dataAggregationService.pollRequestStatus(request.id, options)
      setRequest(result)
      return result
    } finally {
      setLoading(false)
    }
  }, [request])

  useEffect(() => {
    if (initialRequestId) {
      fetchData(initialRequestId)
    }
  }, [initialRequestId, fetchData])

  return { request, loading, error, refetch: () => request?.id ? fetchData(request.id) : Promise.resolve(), cancel, poll }
}

// ============================================================================
// Hook: Aggregation Requests List
// ============================================================================

/**
 * Get all aggregation requests with pagination
 */
export function useAggregationRequests(params?: {
  page?: number
  size?: number
  status?: AggregationStatus
  sortBy?: string
  sortDirection?: 'asc' | 'desc'
}) {
  const [requests, setRequests] = useState<PaginatedResponse<AggregationRequest> | null>(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  const fetchData = useCallback(async () => {
    setLoading(true)
    setError(null)
    try {
      const response = await dataAggregationService.getAggregationRequests(params)
      setRequests(response)
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to fetch requests')
      console.error('Error fetching aggregation requests:', err)
    } finally {
      setLoading(false)
    }
  }, [params])

  useEffect(() => {
    fetchData()
  }, [fetchData])

  return { requests, loading, error, refetch: fetchData }
}

// ============================================================================
// Hook: Refresh Status
// ============================================================================

/**
 * Monitor data refresh operation status
 */
export function useRefreshStatus(
  refreshId?: string,
  pollInterval = 2000
): UseRefreshStatusResult {
  const [refreshStatus, setRefreshStatus] = useState<RefreshStatusResponse | null>(null)
  const [loading, setLoading] = useState(!!refreshId)
  const [error, setError] = useState<string | null>(null)

  const fetchData = useCallback(async () => {
    if (!refreshId) return

    setLoading(true)
    setError(null)
    try {
      const response = await centralizedDataService.getRefreshStatus(refreshId)
      setRefreshStatus(response)
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to fetch refresh status')
      console.error('Error fetching refresh status:', err)
    } finally {
      setLoading(false)
    }
  }, [refreshId])

  useEffect(() => {
    if (!refreshId) return

    fetchData()

    // Auto-poll if not complete
    const interval = setInterval(() => {
      if (refreshStatus?.status === 'COMPLETED' ||
          refreshStatus?.status === 'FAILED') {
        clearInterval(interval)
        return
      }
      fetchData()
    }, pollInterval)

    return () => clearInterval(interval)
  }, [refreshId, fetchData, pollInterval, refreshStatus?.status])

  const isComplete = refreshStatus?.status === 'COMPLETED' ||
                    refreshStatus?.status === 'FAILED'

  return { refreshStatus, loading, error, refetch: fetchData, isComplete }
}

// ============================================================================
// Hook: Available Metrics
// ============================================================================

/**
 * Get available metrics for aggregation
 */
export function useAvailableMetrics(domains?: DomainType[]) {
  const [metrics, setMetrics] = useState<string[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  useEffect(() => {
    const fetchMetrics = async () => {
      setLoading(true)
      setError(null)
      try {
        const response = await dataAggregationService.getAvailableMetrics(domains)
        setMetrics(response)
      } catch (err) {
        setError(err instanceof Error ? err.message : 'Failed to fetch available metrics')
        console.error('Error fetching available metrics:', err)
      } finally {
        setLoading(false)
      }
    }

    fetchMetrics()
  }, [domains])

  return { metrics, loading, error }
}

// ============================================================================
// Hook: Aggregation Capabilities
// ============================================================================

/**
 * Get aggregation capabilities
 */
export function useAggregationCapabilities() {
  const [capabilities, setCapabilities] = useState<any>(null)
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    const fetchCapabilities = async () => {
      setLoading(true)
      try {
        const response = await dataAggregationService.getAggregationCapabilities()
        setCapabilities(response)
      } finally {
        setLoading(false)
      }
    }

    fetchCapabilities()
  }, [])

  return { capabilities, loading }
}

// ============================================================================
// Hook: Health Check
// ============================================================================

/**
 * Health check for data aggregation service
 */
export function useDataAggregationHealth(checkInterval = 30000) {
  const [isHealthy, setIsHealthy] = useState<boolean | null>(null)
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    const checkHealth = async () => {
      setLoading(true)
      try {
        await dataAggregationService.healthCheck()
        setIsHealthy(true)
      } catch {
        setIsHealthy(false)
      } finally {
        setLoading(false)
      }
    }

    checkHealth()

    if (checkInterval > 0) {
      const interval = setInterval(checkHealth, checkInterval)
      return () => clearInterval(interval)
    }
  }, [checkInterval])

  return { isHealthy, loading }
}

// ============================================================================
// Utility Hooks
// ============================================================================

/**
 * Hook to get formatted data quality info
 */
export function useDataQualityInfo(score: number) {
  return {
    color: score >= 80 ? '#4CAF50' : score >= 60 ? '#FF9800' : '#F44336',
    label: score >= 80 ? 'High' : score >= 60 ? 'Medium' : 'Low',
    score
  }
}

/**
 * Hook to get health status color
 */
export function useHealthColor(status: 'HEALTHY' | 'DEGRADED' | 'UNHEALTHY' | 'UNKNOWN' | null) {
  if (!status) return '#9E9E9E'

  const colors: Record<string, string> = {
    HEALTHY: '#4CAF50',
    DEGRADED: '#FF9800',
    UNHEALTHY: '#F44336',
    UNKNOWN: '#9E9E9E',
  }
  return colors[status] || '#9E9E9E'
}

/**
 * Hook to get trend color and icon
 */
export function useTrendDisplay(trend: string | null) {
  const getTrendIcon = (t: string): string => {
    const icons: Record<string, string> = {
      UP: '↑',
      UPWARD: '↑',
      DOWN: '↓',
      DOWNWARD: '↓',
      STABLE: '→',
      VOLATILE: '↝',
    }
    return icons[t] || '→'
  }

  const getTrendColor = (t: string): string => {
    const colors: Record<string, string> = {
      UP: '#4CAF50',
      UPWARD: '#4CAF50',
      DOWN: '#F44336',
      DOWNWARD: '#F44336',
      STABLE: '#9E9E9E',
      VOLATILE: '#FF9800',
    }
    return colors[t] || '#9E9E9E'
  }

  return {
    icon: trend ? getTrendIcon(trend) : '→',
    color: trend ? getTrendColor(trend) : '#9E9E9E'
  }
}

// ============================================================================
// Export Types for Components
// ============================================================================

export type {
  // Data Aggregation Types
  CrossDomainResponse,
  CrossDomainDataPoint,
  SummaryResponse,
  DomainSummary,
  RealTimeResponse,
  RealTimeMetrics,
  RealTimeDomainMetrics,
  RealTimeMetric,
  RealTimeAlert,
  SystemHealth,
  AggregationRequest,
  AggregationStatus,
  CreateAggregationRequest,

  // Metrics Aggregation Types
  AggregatedKPI,
  KPITrendAnalysis,
  TrendDataPoint,
  TrendingResponse,
  MetricComparison,
  ComparisonType,
  ComparisonResult,
  ComparisonInsight,
  KPIBatchRequest,
  KPIBatchResponse,
  DomainKPIsResponse,

  // Centralized Data Types
  CentralizedData,
  DomainData,
  DomainCentralizedData,
  KPIData,
  KPISource,
  KPISources,
  DataQuality,
  QualityIssue,
  MetricData,
  TrendData,
  CentralizedSummary,
  DataSourcesHealth,
  SourceStatus,
  SourceError,
  HealthRecommendation,
  RefreshRequest,
  RefreshResponse,
  RefreshStatusResponse,
  RefreshProgress,
  AggregationMethod,
  AggregationCapabilities,
  KPICapability,
}
