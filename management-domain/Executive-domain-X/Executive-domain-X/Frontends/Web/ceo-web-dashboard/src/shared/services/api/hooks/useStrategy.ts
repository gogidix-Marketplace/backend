/**
 * React Hook: useStrategy
 *
 * Provides strategy data from CEO Strategy Service
 * Handles loading, error states, and caching for Strategic KPIs, OKRs, and Initiatives
 */

import { useState, useEffect, useCallback } from 'react'
import { strategyService } from '../services/executive/strategy.service'
import type {
  StrategicKPI,
  OKR,
  Initiative,
  StrategyDashboardResponse,
  KPICategory,
  InitiativeStatus,
  ObjectivePeriod,
  KeyResult,
  InitiativeRisk,
  Milestone,
  StrategyInsights,
  PerformanceTrends,
  OKRSummary,
  InitiativeSummary,
  CreateStrategicKPIRequest,
  UpdateStrategicKPIRequest,
  CreateOKRRequest,
  UpdateOKRRequest,
  CreateInitiativeRequest,
  UpdateInitiativeRequest,
} from '../services/executive/strategy.service'
import type { PaginatedResponse } from '../client/types'

// ============================================================================
// Types
// ============================================================================

interface UseStrategicKPIsResult {
  kpis: StrategicKPI[]
  loading: boolean
  error: string | null
  totalCount: number
  refetch: () => Promise<void>
}

interface UseStrategicKPIResult {
  kpi: StrategicKPI | null
  loading: boolean
  error: string | null
  refetch: () => Promise<void>
}

interface UseOKRsResult {
  okrs: OKR[]
  loading: boolean
  error: string | null
  totalCount: number
  refetch: () => Promise<void>
}

interface UseOKRResult {
  okr: OKR | null
  loading: boolean
  error: string | null
  refetch: () => Promise<void>
}

interface UseInitiativesResult {
  initiatives: Initiative[]
  loading: boolean
  error: string | null
  totalCount: number
  refetch: () => Promise<void>
}

interface UseInitiativeResult {
  initiative: Initiative | null
  loading: boolean
  error: string | null
  refetch: () => Promise<void>
}

interface UseStrategyDashboardResult {
  dashboard: StrategyDashboardResponse | null
  loading: boolean
  error: string | null
  refetch: () => Promise<void>
}

interface UseStrategyInsightsResult {
  insights: StrategyInsights | null
  loading: boolean
  error: string | null
  refetch: () => Promise<void>
}

interface UsePerformanceTrendsResult {
  trends: PerformanceTrends | null
  loading: boolean
  error: string | null
  refetch: () => Promise<void>
}

interface UseCreateStrategicKPIResult {
  create: (data: CreateStrategicKPIRequest) => Promise<StrategicKPI | null>
  loading: boolean
  error: string | null
}

interface UseUpdateStrategicKPIResult {
  update: (id: string, data: UpdateStrategicKPIRequest) => Promise<StrategicKPI | null>
  loading: boolean
  error: string | null
}

interface UseDeleteStrategicKPIResult {
  remove: (id: string) => Promise<boolean>
  loading: boolean
  error: string | null
}

interface UseCreateOKRResult {
  create: (data: CreateOKRRequest) => Promise<OKR | null>
  loading: boolean
  error: string | null
}

interface UseUpdateOKRResult {
  update: (id: string, data: UpdateOKRRequest) => Promise<OKR | null>
  loading: boolean
  error: string | null
}

interface UseDeleteOKRResult {
  remove: (id: string) => Promise<boolean>
  loading: boolean
  error: string | null
}

interface UseCreateInitiativeResult {
  create: (data: CreateInitiativeRequest) => Promise<Initiative | null>
  loading: boolean
  error: string | null
}

interface UseUpdateInitiativeResult {
  update: (id: string, data: UpdateInitiativeRequest) => Promise<Initiative | null>
  loading: boolean
  error: string | null
}

interface UseDeleteInitiativeResult {
  remove: (id: string) => Promise<boolean>
  loading: boolean
  error: string | null
}

// ============================================================================
// Hook: Strategic KPIs
// ============================================================================

export function useStrategicKPIs(params?: {
  category?: KPICategory
  status?: string
  period?: ObjectivePeriod
  year?: number
  department?: string
  page?: number
  size?: number
  sortBy?: string
  sortDirection?: 'asc' | 'desc'
}, refetchInterval?: number): UseStrategicKPIsResult {
  const [kpis, setKpis] = useState<StrategicKPI[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)
  const [totalCount, setTotalCount] = useState(0)

  const fetchData = useCallback(async () => {
    setLoading(true)
    setError(null)
    try {
      const response = await strategyService.getAllStrategicKPIs(params)
      setKpis(response.content)
      setTotalCount(response.totalElements)
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to fetch strategic KPIs')
      console.error('Error fetching strategic KPIs:', err)
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

  return { kpis, loading, error, totalCount, refetch: fetchData }
}

export function useStrategicKPI(id: string): UseStrategicKPIResult {
  const [kpi, setKpi] = useState<StrategicKPI | null>(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  const fetchData = useCallback(async () => {
    if (!id) return

    setLoading(true)
    setError(null)
    try {
      const response = await strategyService.getStrategicKPIById(id)
      setKpi(response)
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to fetch strategic KPI')
      console.error('Error fetching strategic KPI:', err)
    } finally {
      setLoading(false)
    }
  }, [id])

  useEffect(() => {
    fetchData()
  }, [fetchData])

  return { kpi, loading, error, refetch: fetchData }
}

// ============================================================================
// Hook: OKRs
// ============================================================================

export function useOKRs(params?: {
  period?: ObjectivePeriod
  year?: number
  department?: string
  owner?: string
  status?: string
  page?: number
  size?: number
  sortBy?: string
  sortDirection?: 'asc' | 'desc'
}, refetchInterval?: number): UseOKRsResult {
  const [okrs, setOkrs] = useState<OKR[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)
  const [totalCount, setTotalCount] = useState(0)

  const fetchData = useCallback(async () => {
    setLoading(true)
    setError(null)
    try {
      const response = await strategyService.getAllOKRs(params)
      setOkrs(response.content)
      setTotalCount(response.totalElements)
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to fetch OKRs')
      console.error('Error fetching OKRs:', err)
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

  return { okrs, loading, error, totalCount, refetch: fetchData }
}

export function useOKR(id: string): UseOKRResult {
  const [okr, setOkr] = useState<OKR | null>(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  const fetchData = useCallback(async () => {
    if (!id) return

    setLoading(true)
    setError(null)
    try {
      const response = await strategyService.getOKRById(id)
      setOkr(response)
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to fetch OKR')
      console.error('Error fetching OKR:', err)
    } finally {
      setLoading(false)
    }
  }, [id])

  useEffect(() => {
    fetchData()
  }, [fetchData])

  return { okr, loading, error, refetch: fetchData }
}

// ============================================================================
// Hook: Initiatives
// ============================================================================

export function useInitiatives(params?: {
  status?: InitiativeStatus
  priority?: string
  department?: string
  owner?: string
  page?: number
  size?: number
  sortBy?: string
  sortDirection?: 'asc' | 'desc'
}, refetchInterval?: number): UseInitiativesResult {
  const [initiatives, setInitiatives] = useState<Initiative[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)
  const [totalCount, setTotalCount] = useState(0)

  const fetchData = useCallback(async () => {
    setLoading(true)
    setError(null)
    try {
      const response = await strategyService.getAllInitiatives(params)
      setInitiatives(response.content)
      setTotalCount(response.totalElements)
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to fetch initiatives')
      console.error('Error fetching initiatives:', err)
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

  return { initiatives, loading, error, totalCount, refetch: fetchData }
}

export function useInitiative(id: string): UseInitiativeResult {
  const [initiative, setInitiative] = useState<Initiative | null>(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  const fetchData = useCallback(async () => {
    if (!id) return

    setLoading(true)
    setError(null)
    try {
      const response = await strategyService.getInitiativeById(id)
      setInitiative(response)
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to fetch initiative')
      console.error('Error fetching initiative:', err)
    } finally {
      setLoading(false)
    }
  }, [id])

  useEffect(() => {
    fetchData()
  }, [fetchData])

  return { initiative, loading, error, refetch: fetchData }
}

// ============================================================================
// Hook: Strategy Dashboard
// ============================================================================

export function useStrategyDashboard(
  period?: ObjectivePeriod,
  year?: number,
  refetchInterval?: number
): UseStrategyDashboardResult {
  const [dashboard, setDashboard] = useState<StrategyDashboardResponse | null>(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  const fetchData = useCallback(async () => {
    setLoading(true)
    setError(null)
    try {
      const response = await strategyService.getDashboard(period, year)
      setDashboard(response)
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to fetch strategy dashboard')
      console.error('Error fetching strategy dashboard:', err)
    } finally {
      setLoading(false)
    }
  }, [period, year])

  useEffect(() => {
    fetchData()

    if (refetchInterval) {
      const interval = setInterval(fetchData, refetchInterval)
      return () => clearInterval(interval)
    }
  }, [fetchData, refetchInterval])

  return { dashboard, loading, error, refetch: fetchData }
}

// ============================================================================
// Hook: Strategy Insights
// ============================================================================

export function useStrategyInsights(dateRange?: {
  startDate: string
  endDate: string
}, refetchInterval?: number): UseStrategyInsightsResult {
  const [insights, setInsights] = useState<StrategyInsights | null>(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  const fetchData = useCallback(async () => {
    setLoading(true)
    setError(null)
    try {
      const response = await strategyService.getExecutionInsights(dateRange)
      setInsights(response)
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to fetch strategy insights')
      console.error('Error fetching strategy insights:', err)
    } finally {
      setLoading(false)
    }
  }, [dateRange])

  useEffect(() => {
    fetchData()

    if (refetchInterval) {
      const interval = setInterval(fetchData, refetchInterval)
      return () => clearInterval(interval)
    }
  }, [fetchData, refetchInterval])

  return { insights, loading, error, refetch: fetchData }
}

// ============================================================================
// Hook: Performance Trends
// ============================================================================

export function usePerformanceTrends(params?: {
  period?: ObjectivePeriod
  year?: number
  type?: 'kpis' | 'okrs' | 'initiatives' | 'all'
}, refetchInterval?: number): UsePerformanceTrendsResult {
  const [trends, setTrends] = useState<PerformanceTrends | null>(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  const fetchData = useCallback(async () => {
    setLoading(true)
    setError(null)
    try {
      const response = await strategyService.getPerformanceTrends(params)
      setTrends(response)
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to fetch performance trends')
      console.error('Error fetching performance trends:', err)
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

  return { trends, loading, error, refetch: fetchData }
}

// ============================================================================
// Hook: KPI Value Update
// ============================================================================

export function useKPIValueUpdate() {
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState<string | null>(null)

  const updateValue = useCallback(async (id: string, currentValue: number, notes?: string) => {
    setLoading(true)
    setError(null)
    try {
      const result = await strategyService.updateKPIValue(id, currentValue, notes)
      return result
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to update KPI value')
      return null
    } finally {
      setLoading(false)
    }
  }, [])

  return { updateValue, loading, error }
}

// ============================================================================
// Hook: Key Result Progress Update
// ============================================================================

export function useKeyResultProgressUpdate() {
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState<string | null>(null)

  const updateProgress = useCallback(async (
    okrId: string,
    keyResultId: string,
    currentValue: number,
    notes?: string
  ) => {
    setLoading(true)
    setError(null)
    try {
      const result = await strategyService.updateKeyResultProgress(okrId, keyResultId, currentValue, notes)
      return result
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to update key result progress')
      return null
    } finally {
      setLoading(false)
    }
  }, [])

  return { updateProgress, loading, error }
}

// ============================================================================
// Hook: Initiative Status Update
// ============================================================================

export function useInitiativeStatusUpdate() {
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState<string | null>(null)

  const updateStatus = useCallback(async (id: string, status: InitiativeStatus, reason?: string) => {
    setLoading(true)
    setError(null)
    try {
      const result = await strategyService.updateInitiativeStatus(id, status, reason)
      return result
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to update initiative status')
      return null
    } finally {
      setLoading(false)
    }
  }, [])

  return { updateStatus, loading, error }
}

// ============================================================================
// Hook: Milestone Management
// ============================================================================

export function useMilestoneManagement(initiativeId: string) {
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState<string | null>(null)

  const addMilestone = useCallback(async (milestone: Omit<Milestone, 'id' | 'initiativeId'>) => {
    setLoading(true)
    setError(null)
    try {
      const result = await strategyService.addMilestone(initiativeId, milestone)
      return result
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to add milestone')
      return null
    } finally {
      setLoading(false)
    }
  }, [initiativeId])

  const updateMilestone = useCallback(async (
    milestoneId: string,
    updates: Partial<Pick<Milestone, 'title' | 'description' | 'dueDate' | 'status'>>
  ) => {
    setLoading(true)
    setError(null)
    try {
      const result = await strategyService.updateMilestone(initiativeId, milestoneId, updates)
      return result
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to update milestone')
      return null
    } finally {
      setLoading(false)
    }
  }, [initiativeId])

  const completeMilestone = useCallback(async (milestoneId: string) => {
    setLoading(true)
    setError(null)
    try {
      const result = await strategyService.completeMilestone(initiativeId, milestoneId)
      return result
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to complete milestone')
      return null
    } finally {
      setLoading(false)
    }
  }, [initiativeId])

  const deleteMilestone = useCallback(async (milestoneId: string) => {
    setLoading(true)
    setError(null)
    try {
      await strategyService.deleteMilestone(initiativeId, milestoneId)
      return true
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to delete milestone')
      return false
    } finally {
      setLoading(false)
    }
  }, [initiativeId])

  return { addMilestone, updateMilestone, completeMilestone, deleteMilestone, loading, error }
}

// ============================================================================
// Hook: Risk Management
// ============================================================================

export function useRiskManagement(initiativeId: string) {
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState<string | null>(null)

  const addRisk = useCallback(async (risk: Omit<InitiativeRisk, 'id' | 'initiativeId' | 'identifiedAt' | 'updatedAt'>) => {
    setLoading(true)
    setError(null)
    try {
      const result = await strategyService.addRisk(initiativeId, risk)
      return result
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to add risk')
      return null
    } finally {
      setLoading(false)
    }
  }, [initiativeId])

  const updateRisk = useCallback(async (
    riskId: string,
    updates: Partial<Pick<InitiativeRisk, 'likelihood' | 'impact' | 'mitigation' | 'status' | 'owner'>>
  ) => {
    setLoading(true)
    setError(null)
    try {
      const result = await strategyService.updateRisk(initiativeId, riskId, updates)
      return result
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to update risk')
      return null
    } finally {
      setLoading(false)
    }
  }, [initiativeId])

  const deleteRisk = useCallback(async (riskId: string) => {
    setLoading(true)
    setError(null)
    try {
      await strategyService.deleteRisk(initiativeId, riskId)
      return true
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to delete risk')
      return false
    } finally {
      setLoading(false)
    }
  }, [initiativeId])

  return { addRisk, updateRisk, deleteRisk, loading, error }
}

// ============================================================================
// Hook: Create Strategic KPI
// ============================================================================

export function useCreateStrategicKPI(): UseCreateStrategicKPIResult {
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState<string | null>(null)

  const create = useCallback(async (data: CreateStrategicKPIRequest) => {
    setLoading(true)
    setError(null)
    try {
      const result = await strategyService.createStrategicKPI(data)
      return result
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to create strategic KPI')
      return null
    } finally {
      setLoading(false)
    }
  }, [])

  return { create, loading, error }
}

// ============================================================================
// Hook: Update Strategic KPI
// ============================================================================

export function useUpdateStrategicKPI(): UseUpdateStrategicKPIResult {
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState<string | null>(null)

  const update = useCallback(async (id: string, data: UpdateStrategicKPIRequest) => {
    setLoading(true)
    setError(null)
    try {
      const result = await strategyService.updateStrategicKPI(id, data)
      return result
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to update strategic KPI')
      return null
    } finally {
      setLoading(false)
    }
  }, [])

  return { update, loading, error }
}

// ============================================================================
// Hook: Delete Strategic KPI
// ============================================================================

export function useDeleteStrategicKPI(): UseDeleteStrategicKPIResult {
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState<string | null>(null)

  const remove = useCallback(async (id: string) => {
    setLoading(true)
    setError(null)
    try {
      await strategyService.deleteStrategicKPI(id)
      return true
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to delete strategic KPI')
      return false
    } finally {
      setLoading(false)
    }
  }, [])

  return { remove, loading, error }
}

// ============================================================================
// Hook: Create OKR
// ============================================================================

export function useCreateOKR(): UseCreateOKRResult {
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState<string | null>(null)

  const create = useCallback(async (data: CreateOKRRequest) => {
    setLoading(true)
    setError(null)
    try {
      const result = await strategyService.createOKR(data)
      return result
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to create OKR')
      return null
    } finally {
      setLoading(false)
    }
  }, [])

  return { create, loading, error }
}

// ============================================================================
// Hook: Update OKR
// ============================================================================

export function useUpdateOKR(): UseUpdateOKRResult {
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState<string | null>(null)

  const update = useCallback(async (id: string, data: UpdateOKRRequest) => {
    setLoading(true)
    setError(null)
    try {
      const result = await strategyService.updateOKR(id, data)
      return result
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to update OKR')
      return null
    } finally {
      setLoading(false)
    }
  }, [])

  return { update, loading, error }
}

// ============================================================================
// Hook: Delete OKR
// ============================================================================

export function useDeleteOKR(): UseDeleteOKRResult {
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState<string | null>(null)

  const remove = useCallback(async (id: string) => {
    setLoading(true)
    setError(null)
    try {
      await strategyService.deleteOKR(id)
      return true
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to delete OKR')
      return false
    } finally {
      setLoading(false)
    }
  }, [])

  return { remove, loading, error }
}

// ============================================================================
// Hook: Create Initiative
// ============================================================================

export function useCreateInitiative(): UseCreateInitiativeResult {
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState<string | null>(null)

  const create = useCallback(async (data: CreateInitiativeRequest) => {
    setLoading(true)
    setError(null)
    try {
      const result = await strategyService.createInitiative(data)
      return result
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to create initiative')
      return null
    } finally {
      setLoading(false)
    }
  }, [])

  return { create, loading, error }
}

// ============================================================================
// Hook: Update Initiative
// ============================================================================

export function useUpdateInitiative(): UseUpdateInitiativeResult {
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState<string | null>(null)

  const update = useCallback(async (id: string, data: UpdateInitiativeRequest) => {
    setLoading(true)
    setError(null)
    try {
      const result = await strategyService.updateInitiative(id, data)
      return result
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to update initiative')
      return null
    } finally {
      setLoading(false)
    }
  }, [])

  return { update, loading, error }
}

// ============================================================================
// Hook: Delete Initiative
// ============================================================================

export function useDeleteInitiative(): UseDeleteInitiativeResult {
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState<string | null>(null)

  const remove = useCallback(async (id: string) => {
    setLoading(true)
    setError(null)
    try {
      await strategyService.deleteInitiative(id)
      return true
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to delete initiative')
      return false
    } finally {
      setLoading(false)
    }
  }, [])

  return { remove, loading, error }
}

// ============================================================================
// Hook: Strategy Health Check
// ============================================================================

export function useStrategyHealth(refetchInterval?: number) {
  const [isHealthy, setIsHealthy] = useState<boolean | null>(null)
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    const checkHealth = async () => {
      setLoading(true)
      try {
        await strategyService.healthCheck()
        setIsHealthy(true)
      } catch {
        setIsHealthy(false)
      } finally {
        setLoading(false)
      }
    }

    checkHealth()

    if (refetchInterval) {
      const interval = setInterval(checkHealth, refetchInterval)
      return () => clearInterval(interval)
    }
  }, [refetchInterval])

  return { isHealthy, loading }
}

// ============================================================================
// Export Types for Components
// ============================================================================

export type {
  // Core Types
  StrategicKPI,
  OKR,
  Initiative,
  KeyResult,
  KeyResultMilestone,
  Milestone,
  InitiativeRisk,
  OKRSummary,
  InitiativeSummary,
  StrategyDashboardResponse,
  StrategyInsights,
  PerformanceTrends,
  KPITrend,
  OKRTrend,
  InitiativeTrend,
  MonthlyProgress,
  Recommendation,
  // Request/Response Types
  CreateStrategicKPIRequest,
  UpdateStrategicKPIRequest,
  CreateOKRRequest,
  UpdateOKRRequest,
  CreateInitiativeRequest,
  UpdateInitiativeRequest,
  // Hook Result Types
  UseStrategicKPIsResult,
  UseStrategicKPIResult,
  UseOKRsResult,
  UseOKRResult,
  UseInitiativesResult,
  UseInitiativeResult,
  UseStrategyDashboardResult,
  UseStrategyInsightsResult,
  UsePerformanceTrendsResult,
  UseCreateStrategicKPIResult,
  UseUpdateStrategicKPIResult,
  UseDeleteStrategicKPIResult,
  UseCreateOKRResult,
  UseUpdateOKRResult,
  UseDeleteOKRResult,
  UseCreateInitiativeResult,
  UseUpdateInitiativeResult,
  UseDeleteInitiativeResult,
}
