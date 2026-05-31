/**
 * React Hook: useAnalytics
 *
 * Provides analytics data from CEO Analytics Service
 * Handles loading, error states, and caching
 */

import { useState, useEffect, useCallback } from 'react'
import { analyticsService } from '../services/executive/analytics.service'
import type {
  AnalyticsDashboardResponse,
  AnalyticsResponse,
  KPICategory,
  TimePeriod,
  DateRange,
} from '../client/types'

// ============================================================================
// Types
// ============================================================================

interface UseAnalyticsResult {
  data: AnalyticsDashboardResponse | null
  loading: boolean
  error: string | null
  refetch: () => Promise<void>
}

interface UseAnalyticsKPIsResult {
  kpis: AnalyticsResponse[]
  loading: boolean
  error: string | null
  refetch: () => Promise<void>
}

interface UseTrendingKPIsResult {
  trendingKPIs: AnalyticsResponse[]
  loading: boolean
  error: string | null
  refetch: () => Promise<void>
}

interface UseTimeSeriesResult {
  timeSeries: AnalyticsResponse[]
  loading: boolean
  error: string | null
  refetch: (kpiName: string, days?: number) => Promise<void>
}

interface UseRequiringAttentionResult {
  alerts: AnalyticsResponse[]
  loading: boolean
  error: string | null
  refetch: () => Promise<void>
}

// ============================================================================
// Hook: Dashboard Analytics
// ============================================================================

export function useAnalytics(refetchInterval?: number): UseAnalyticsResult {
  const [data, setData] = useState<AnalyticsDashboardResponse | null>(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  const fetchData = useCallback(async () => {
    setLoading(true)
    setError(null)
    try {
      const response = await analyticsService.getDashboard()
      setData(response)
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to fetch analytics')
      console.error('Error fetching analytics dashboard:', err)
    } finally {
      setLoading(false)
    }
  }, [])

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
// Hook: Analytics by Category
// ============================================================================

export function useAnalyticsByCategory(category: KPICategory): UseAnalyticsKPIsResult {
  const [kpis, setKpis] = useState<AnalyticsResponse[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  const fetchData = useCallback(async () => {
    setLoading(true)
    setError(null)
    try {
      const response = await analyticsService.getAnalyticsByCategory(category)
      setKpis(response)
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to fetch KPIs')
    } finally {
      setLoading(false)
    }
  }, [category])

  useEffect(() => {
    fetchData()
  }, [fetchData])

  return { kpis, loading, error, refetch: fetchData }
}

// ============================================================================
// Hook: Trending KPIs
// ============================================================================

export function useTrendingKPIs(): UseTrendingKPIsResult {
  const [trendingKPIs, setTrendingKPIs] = useState<AnalyticsResponse[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  const fetchData = useCallback(async () => {
    setLoading(true)
    setError(null)
    try {
      const response = await analyticsService.getTrendingKPIs()
      setTrendingKPIs(response)
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to fetch trending KPIs')
    } finally {
      setLoading(false)
    }
  }, [])

  useEffect(() => {
    fetchData()
  }, [fetchData])

  return { trendingKPIs, loading, error, refetch: fetchData }
}

// ============================================================================
// Hook: Time Series Data
// ============================================================================

export function useTimeSeriesData(initialKpiName?: string, initialDays = 30): UseTimeSeriesResult {
  const [timeSeries, setTimeSeries] = useState<AnalyticsResponse[]>([])
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState<string | null>(null)

  const fetchData = useCallback(async (kpiName?: string, days = 30) => {
    if (!kpiName) return

    setLoading(true)
    setError(null)
    try {
      const response = await analyticsService.getTimeSeriesData(kpiName, days)
      setTimeSeries(response)
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to fetch time series data')
    } finally {
      setLoading(false)
    }
  }, [])

  useEffect(() => {
    if (initialKpiName) {
      fetchData(initialKpiName, initialDays)
    }
  }, [initialKpiName, initialDays, fetchData])

  return { timeSeries, loading, error, refetch: fetchData }
}

// ============================================================================
// Hook: KPIs Requiring Attention
// ============================================================================

export function useRequiringAttention(refetchInterval?: number): UseRequiringAttentionResult {
  const [alerts, setAlerts] = useState<AnalyticsResponse[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  const fetchData = useCallback(async () => {
    setLoading(true)
    setError(null)
    try {
      const response = await analyticsService.getAnalyticsRequiringAttention()
      setAlerts(response)
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to fetch alerts')
    } finally {
      setLoading(false)
    }
  }, [])

  useEffect(() => {
    fetchData()

    if (refetchInterval) {
      const interval = setInterval(fetchData, refetchInterval)
      return () => clearInterval(interval)
    }
  }, [fetchData, refetchInterval])

  return { alerts, loading, error, refetch: fetchData }
}

// ============================================================================
// Hook: Analytics by Date Range
// ============================================================================

export function useAnalyticsByDateRange(dateRange?: DateRange): UseAnalyticsKPIsResult {
  const [kpis, setKpis] = useState<AnalyticsResponse[]>([])
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState<string | null>(null)

  const fetchData = useCallback(async () => {
    if (!dateRange) return

    setLoading(true)
    setError(null)
    try {
      const response = await analyticsService.getAnalyticsByDateRange(dateRange)
      setKpis(response)
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to fetch analytics')
    } finally {
      setLoading(false)
    }
  }, [dateRange])

  useEffect(() => {
    fetchData()
  }, [fetchData])

  return { kpis, loading, error, refetch: fetchData }
}

// ============================================================================
// Hook: Aggregated Analytics
// ============================================================================

export function useAggregatedAnalytics(
  timePeriod?: TimePeriod,
  dateRange?: DateRange
): UseAnalyticsKPIsResult {
  const [kpis, setKpis] = useState<AnalyticsResponse[]>([])
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState<string | null>(null)

  const fetchData = useCallback(async () => {
    if (!timePeriod || !dateRange) return

    setLoading(true)
    setError(null)
    try {
      const response = await analyticsService.aggregateAnalytics(timePeriod, dateRange)
      setKpis(response)
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to fetch aggregated analytics')
    } finally {
      setLoading(false)
    }
  }, [timePeriod, dateRange])

  useEffect(() => {
    fetchData()
  }, [fetchData])

  return { kpis, loading, error, refetch: fetchData }
}

// ============================================================================
// Hook: Search Analytics
// ============================================================================

export function useAnalyticsSearch(searchTerm: string, params?: {
  page?: number
  size?: number
}) {
  const [results, setResults] = useState<AnalyticsResponse[]>([])
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState<string | null>(null)
  const [totalCount, setTotalCount] = useState(0)

  useEffect(() => {
    if (!searchTerm || searchTerm.length < 2) {
      setResults([])
      setTotalCount(0)
      return
    }

    const debounceTimer = setTimeout(async () => {
      setLoading(true)
      setError(null)
      try {
        const response = await analyticsService.searchAnalytics(searchTerm, params)
        setResults(response.content)
        setTotalCount(response.totalElements)
      } catch (err) {
        setError(err instanceof Error ? err.message : 'Search failed')
      } finally {
        setLoading(false)
      }
    }, 300)

    return () => clearTimeout(debounceTimer)
  }, [searchTerm, params])

  return { results, loading, error, totalCount }
}

// ============================================================================
// Hook: Export Analytics
// ============================================================================

export function useAnalyticsExport() {
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState<string | null>(null)

  const exportData = useCallback(async (
    format: 'CSV' | 'EXCEL' | 'PDF' | 'JSON',
    startDate: string,
    endDate: string,
    filename?: string
  ) => {
    setLoading(true)
    setError(null)

    try {
      const blob = await analyticsService.exportAnalytics({
        format,
        startDate,
        endDate,
      })

      // Create download link
      const url = window.URL.createObjectURL(blob)
      const link = document.createElement('a')
      link.href = url
      link.download = filename || `analytics-export-${startDate}-to-${endDate}.${format.toLowerCase()}`
      document.body.appendChild(link)
      link.click()
      document.body.removeChild(link)
      window.URL.revokeObjectURL(url)

      return true
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Export failed')
      return false
    } finally {
      setLoading(false)
    }
  }, [])

  return { exportData, loading, error }
}

// ============================================================================
// Hook: Analytics Health Check
// ============================================================================

export function useAnalyticsHealth() {
  const [isHealthy, setIsHealthy] = useState<boolean | null>(null)
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    const checkHealth = async () => {
      setLoading(true)
      try {
        await analyticsService.healthCheck()
        setIsHealthy(true)
      } catch {
        setIsHealthy(false)
      } finally {
        setLoading(false)
      }
    }

    checkHealth()

    // Recheck every 30 seconds
    const interval = setInterval(checkHealth, 30000)
    return () => clearInterval(interval)
  }, [])

  return { isHealthy, loading }
}
