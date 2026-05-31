/**
 * React Hook: useApprovals
 *
 * Provides approval data from CEO Approval Service
 * Handles loading, error states, caching, and optimistic updates
 */

import { useState, useEffect, useCallback, useRef } from 'react'
import { approvalService } from '../services/executive/approval.service'
import type {
  ApprovalQueueItem,
  ApprovalQueueResponse,
  ApprovalQueueFilters,
  ApprovalHistoryItem,
  ApprovalHistoryResponse,
  PendingApprovalsSummary,
  WorkflowStatus,
  ApprovalStatus,
  ApprovalType,
  ApprovalPriority,
  ApproveRequest,
  RejectRequest,
  DelegateRequest,
  ApprovalTypeConfig,
  BulkActionRequest,
  BulkActionResponse,
} from '../services/executive/approval.service'

// ============================================================================
// Types
// ============================================================================

interface UseApprovalQueueResult {
  data: ApprovalQueueResponse | null
  items: ApprovalQueueItem[]
  loading: boolean
  error: string | null
  refetch: () => Promise<void>
  summary: {
    total: number
    pending: number
    approved: number
    rejected: number
    delegated: number
  } | null
}

interface UsePendingApprovalsResult {
  data: PendingApprovalsSummary | null
  total: number
  critical: number
  high: number
  medium: number
  low: number
  overdue: number
  byType: Record<ApprovalType, number>
  loading: boolean
  error: string | null
  refetch: () => Promise<void>
}

interface UseApprovalResult {
  data: ApprovalQueueItem | null
  loading: boolean
  error: string | null
  refetch: () => Promise<void>
}

interface UseApprovalHistoryResult {
  data: ApprovalHistoryResponse | null
  items: ApprovalHistoryItem[]
  loading: boolean
  error: string | null
  refetch: () => Promise<void>
  summary: {
    total: number
    approved: number
    rejected: number
    delegated: number
    totalAmount?: number
  } | null
}

interface UseWorkflowStatusResult {
  data: WorkflowStatus | null
  loading: boolean
  error: string | null
  refetch: () => Promise<void>
  progress: number
}

interface UseApprovalActionsResult {
  approve: (id: string, request: ApproveRequest) => Promise<ApprovalQueueItem | null>
  reject: (id: string, request: RejectRequest) => Promise<ApprovalQueueItem | null>
  delegate: (id: string, request: DelegateRequest) => Promise<ApprovalQueueItem | null>
  cancel: (id: string, reason: string) => Promise<ApprovalQueueItem | null>
  addComment: (id: string, comment: string) => Promise<ApprovalQueueItem | null>
  loading: boolean
  error: string | null
  clearError: () => void
}

interface UseBulkActionsResult {
  bulkAction: (request: BulkActionRequest) => Promise<BulkActionResponse | null>
  loading: boolean
  error: string | null
  clearError: () => void
}

interface UseApprovalTypesResult {
  types: ApprovalTypeConfig[]
  loading: boolean
  error: string | null
  refetch: () => Promise<void>
}

interface UseUrgentApprovalsResult {
  items: ApprovalQueueItem[]
  loading: boolean
  error: string | null
  refetch: () => Promise<void>
  count: number
}

interface UseMyApprovalsResult {
  data: ApprovalQueueResponse | null
  items: ApprovalQueueItem[]
  loading: boolean
  error: string | null
  refetch: () => Promise<void>
}

interface UseApprovalSearchResult {
  results: ApprovalQueueItem[]
  loading: boolean
  error: string | null
  totalCount: number
}

// ============================================================================
// Hook: Approval Queue
// ============================================================================

export function useApprovalQueue(
  filters?: ApprovalQueueFilters,
  params?: {
    page?: number
    size?: number
    sortBy?: string
    sortDirection?: 'asc' | 'desc'
  },
  refetchInterval?: number
): UseApprovalQueueResult {
  const [data, setData] = useState<ApprovalQueueResponse | null>(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  const fetchData = useCallback(async () => {
    setLoading(true)
    setError(null)
    try {
      const response = await approvalService.getApprovalQueue(filters, params)
      setData(response)
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to fetch approval queue')
      console.error('Error fetching approval queue:', err)
    } finally {
      setLoading(false)
    }
  }, [filters, params])

  useEffect(() => {
    fetchData()

    if (refetchInterval) {
      const interval = setInterval(fetchData, refetchInterval)
      return () => clearInterval(interval)
    }
  }, [fetchData, refetchInterval])

  return {
    data,
    items: data?.content || [],
    loading,
    error,
    refetch: fetchData,
    summary: data?.summary || null,
  }
}

// ============================================================================
// Hook: Pending Approvals Count
// ============================================================================

export function usePendingApprovals(
  refetchInterval?: number
): UsePendingApprovalsResult {
  const [data, setData] = useState<PendingApprovalsSummary | null>(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  const fetchData = useCallback(async () => {
    setLoading(true)
    setError(null)
    try {
      const response = await approvalService.getPendingApprovalsCount()
      setData(response)
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to fetch pending approvals')
      console.error('Error fetching pending approvals:', err)
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

  return {
    data,
    total: data?.total || 0,
    critical: data?.critical || 0,
    high: data?.high || 0,
    medium: data?.medium || 0,
    low: data?.low || 0,
    overdue: data?.overdue || 0,
    byType: data?.byType || ({} as Record<ApprovalType, number>),
    loading,
    error,
    refetch: fetchData,
  }
}

// ============================================================================
// Hook: Single Approval Details
// ============================================================================

export function useApproval(id: string): UseApprovalResult {
  const [data, setData] = useState<ApprovalQueueItem | null>(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  const fetchData = useCallback(async () => {
    if (!id) return

    setLoading(true)
    setError(null)
    try {
      const response = await approvalService.getApprovalById(id)
      setData(response)
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to fetch approval details')
      console.error('Error fetching approval details:', err)
    } finally {
      setLoading(false)
    }
  }, [id])

  useEffect(() => {
    fetchData()
  }, [fetchData])

  return { data, loading, error, refetch: fetchData }
}

// ============================================================================
// Hook: Approval History
// ============================================================================

export function useApprovalHistory(
  filters?: {
    type?: ApprovalType
    status?: ApprovalStatus
    startDate?: string
    endDate?: string
    processedBy?: string
  },
  params?: {
    page?: number
    size?: number
    sortBy?: string
    sortDirection?: 'asc' | 'desc'
  }
): UseApprovalHistoryResult {
  const [data, setData] = useState<ApprovalHistoryResponse | null>(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  const fetchData = useCallback(async () => {
    setLoading(true)
    setError(null)
    try {
      const response = await approvalService.getApprovalHistory(filters, params)
      setData(response)
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to fetch approval history')
      console.error('Error fetching approval history:', err)
    } finally {
      setLoading(false)
    }
  }, [filters, params])

  useEffect(() => {
    fetchData()
  }, [fetchData])

  return {
    data,
    items: data?.content || [],
    loading,
    error,
    refetch: fetchData,
    summary: data?.summary || null,
  }
}

// ============================================================================
// Hook: Workflow Status
// ============================================================================

export function useWorkflowStatus(id: string): UseWorkflowStatusResult {
  const [data, setData] = useState<WorkflowStatus | null>(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  const fetchData = useCallback(async () => {
    if (!id) return

    setLoading(true)
    setError(null)
    try {
      const response = await approvalService.getWorkflowStatus(id)
      setData(response)
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to fetch workflow status')
      console.error('Error fetching workflow status:', err)
    } finally {
      setLoading(false)
    }
  }, [id])

  useEffect(() => {
    fetchData()
  }, [fetchData])

  return {
    data,
    loading,
    error,
    refetch: fetchData,
    progress: data?.progress || 0,
  }
}

// ============================================================================
// Hook: Approval Actions (with optimistic updates)
// ============================================================================

export function useApprovalActions(
  onActionSuccess?: (action: 'approve' | 'reject' | 'delegate' | 'cancel' | 'comment', approval: ApprovalQueueItem) => void,
  onActionError?: (action: string, error: string) => void
): UseApprovalActionsResult {
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState<string | null>(null)

  const approve = useCallback(async (id: string, request: ApproveRequest): Promise<ApprovalQueueItem | null> => {
    setLoading(true)
    setError(null)
    try {
      const result = await approvalService.approveRequest(id, request)
      onActionSuccess?.('approve', result)
      return result
    } catch (err) {
      const errorMsg = err instanceof Error ? err.message : 'Failed to approve request'
      setError(errorMsg)
      onActionError?.('approve', errorMsg)
      return null
    } finally {
      setLoading(false)
    }
  }, [onActionSuccess, onActionError])

  const reject = useCallback(async (id: string, request: RejectRequest): Promise<ApprovalQueueItem | null> => {
    setLoading(true)
    setError(null)
    try {
      const result = await approvalService.rejectRequest(id, request)
      onActionSuccess?.('reject', result)
      return result
    } catch (err) {
      const errorMsg = err instanceof Error ? err.message : 'Failed to reject request'
      setError(errorMsg)
      onActionError?.('reject', errorMsg)
      return null
    } finally {
      setLoading(false)
    }
  }, [onActionSuccess, onActionError])

  const delegate = useCallback(async (id: string, request: DelegateRequest): Promise<ApprovalQueueItem | null> => {
    setLoading(true)
    setError(null)
    try {
      const result = await approvalService.delegateRequest(id, request)
      onActionSuccess?.('delegate', result)
      return result
    } catch (err) {
      const errorMsg = err instanceof Error ? err.message : 'Failed to delegate request'
      setError(errorMsg)
      onActionError?.('delegate', errorMsg)
      return null
    } finally {
      setLoading(false)
    }
  }, [onActionSuccess, onActionError])

  const cancel = useCallback(async (id: string, reason: string): Promise<ApprovalQueueItem | null> => {
    setLoading(true)
    setError(null)
    try {
      const result = await approvalService.cancelApproval(id, reason)
      onActionSuccess?.('cancel', result)
      return result
    } catch (err) {
      const errorMsg = err instanceof Error ? err.message : 'Failed to cancel request'
      setError(errorMsg)
      onActionError?.('cancel', errorMsg)
      return null
    } finally {
      setLoading(false)
    }
  }, [onActionSuccess, onActionError])

  const addComment = useCallback(async (id: string, comment: string): Promise<ApprovalQueueItem | null> => {
    setLoading(true)
    setError(null)
    try {
      const result = await approvalService.addComment(id, comment)
      onActionSuccess?.('comment', result)
      return result
    } catch (err) {
      const errorMsg = err instanceof Error ? err.message : 'Failed to add comment'
      setError(errorMsg)
      onActionError?.('comment', errorMsg)
      return null
    } finally {
      setLoading(false)
    }
  }, [onActionSuccess, onActionError])

  const clearError = useCallback(() => {
    setError(null)
  }, [])

  return { approve, reject, delegate, cancel, addComment, loading, error, clearError }
}

// ============================================================================
// Hook: Bulk Actions
// ============================================================================

export function useBulkActions(
  onActionSuccess?: (response: BulkActionResponse) => void,
  onActionError?: (error: string) => void
): UseBulkActionsResult {
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState<string | null>(null)

  const bulkAction = useCallback(async (request: BulkActionRequest): Promise<BulkActionResponse | null> => {
    setLoading(true)
    setError(null)
    try {
      const result = await approvalService.bulkAction(request)
      onActionSuccess?.(result)
      return result
    } catch (err) {
      const errorMsg = err instanceof Error ? err.message : 'Bulk action failed'
      setError(errorMsg)
      onActionError?.(errorMsg)
      return null
    } finally {
      setLoading(false)
    }
  }, [onActionSuccess, onActionError])

  const clearError = useCallback(() => {
    setError(null)
  }, [])

  return { bulkAction, loading, error, clearError }
}

// ============================================================================
// Hook: Approval Types
// ============================================================================

export function useApprovalTypes(): UseApprovalTypesResult {
  const [types, setTypes] = useState<ApprovalTypeConfig[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  const fetchData = useCallback(async () => {
    setLoading(true)
    setError(null)
    try {
      const response = await approvalService.getApprovalTypes()
      setTypes(response)
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to fetch approval types')
    } finally {
      setLoading(false)
    }
  }, [])

  useEffect(() => {
    fetchData()
  }, [fetchData])

  return { types, loading, error, refetch: fetchData }
}

// ============================================================================
// Hook: Urgent Approvals
// ============================================================================

export function useUrgentApprovals(
  params?: {
    page?: number
    size?: number
    sortBy?: string
    sortDirection?: 'asc' | 'desc'
  },
  refetchInterval?: number
): UseUrgentApprovalsResult {
  const [items, setItems] = useState<ApprovalQueueItem[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  const fetchData = useCallback(async () => {
    setLoading(true)
    setError(null)
    try {
      const response = await approvalService.getUrgentApprovals(params)
      setItems(response.content)
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to fetch urgent approvals')
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

  return { items, loading, error, refetch: fetchData, count: items.length }
}

// ============================================================================
// Hook: My Approvals (requested by current user)
// ============================================================================

export function useMyApprovals(
  params?: {
    page?: number
    size?: number
    sortBy?: string
    sortDirection?: 'asc' | 'desc'
  }
): UseMyApprovalsResult {
  const [data, setData] = useState<ApprovalQueueResponse | null>(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  const fetchData = useCallback(async () => {
    setLoading(true)
    setError(null)
    try {
      const response = await approvalService.getMyApprovals(params)
      setData(response)
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to fetch my approvals')
    } finally {
      setLoading(false)
    }
  }, [params])

  useEffect(() => {
    fetchData()
  }, [fetchData])

  return {
    data,
    items: data?.content || [],
    loading,
    error,
    refetch: fetchData,
  }
}

// ============================================================================
// Hook: Approval Search
// ============================================================================

export function useApprovalSearch(
  searchTerm: string,
  params?: {
    page?: number
    size?: number
    sortBy?: string
    sortDirection?: 'asc' | 'desc'
  },
  debounceMs = 300
): UseApprovalSearchResult {
  const [results, setResults] = useState<ApprovalQueueItem[]>([])
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState<string | null>(null)
  const [totalCount, setTotalCount] = useState(0)
  const debounceRef = useRef<NodeJS.Timeout>()

  useEffect(() => {
    if (!searchTerm || searchTerm.length < 2) {
      setResults([])
      setTotalCount(0)
      return
    }

    if (debounceRef.current) {
      clearTimeout(debounceRef.current)
    }

    debounceRef.current = setTimeout(async () => {
      setLoading(true)
      setError(null)
      try {
        const response = await approvalService.searchApprovals(searchTerm, params)
        setResults(response.content)
        setTotalCount(response.totalElements)
      } catch (err) {
        setError(err instanceof Error ? err.message : 'Search failed')
      } finally {
        setLoading(false)
      }
    }, debounceMs)

    return () => {
      if (debounceRef.current) {
        clearTimeout(debounceRef.current)
      }
    }
  }, [searchTerm, params, debounceMs])

  return { results, loading, error, totalCount }
}

// ============================================================================
// Hook: Approvals by Status
// ============================================================================

export function useApprovalsByStatus(
  status: ApprovalStatus,
  params?: {
    page?: number
    size?: number
    sortBy?: string
    sortDirection?: 'asc' | 'desc'
  }
): UseApprovalQueueResult {
  const [data, setData] = useState<ApprovalQueueResponse | null>(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  const fetchData = useCallback(async () => {
    setLoading(true)
    setError(null)
    try {
      const response = await approvalService.getApprovalsByStatus(status, params)
      setData(response)
    } catch (err) {
      setError(err instanceof Error ? err.message : `Failed to fetch ${status.toLowerCase()} approvals`)
    } finally {
      setLoading(false)
    }
  }, [status, params])

  useEffect(() => {
    fetchData()
  }, [fetchData])

  return {
    data,
    items: data?.content || [],
    loading,
    error,
    refetch: fetchData,
    summary: data?.summary || null,
  }
}

// ============================================================================
// Hook: Approvals by Type
// ============================================================================

export function useApprovalsByType(
  type: ApprovalType,
  params?: {
    page?: number
    size?: number
    sortBy?: string
    sortDirection?: 'asc' | 'desc'
  }
): UseApprovalQueueResult {
  const [data, setData] = useState<ApprovalQueueResponse | null>(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  const fetchData = useCallback(async () => {
    setLoading(true)
    setError(null)
    try {
      const response = await approvalService.getApprovalsByType(type, params)
      setData(response)
    } catch (err) {
      setError(err instanceof Error ? err.message : `Failed to fetch ${type.toLowerCase()} approvals`)
    } finally {
      setLoading(false)
    }
  }, [type, params])

  useEffect(() => {
    fetchData()
  }, [fetchData])

  return {
    data,
    items: data?.content || [],
    loading,
    error,
    refetch: fetchData,
    summary: data?.summary || null,
  }
}

// ============================================================================
// Hook: Approvals by Priority
// ============================================================================

export function useApprovalsByPriority(
  priority: ApprovalPriority,
  params?: {
    page?: number
    size?: number
    sortBy?: string
    sortDirection?: 'asc' | 'desc'
  }
): UseApprovalQueueResult {
  const filters = { priority }
  return useApprovalQueue(filters, params)
}

// ============================================================================
// Hook: Approval Health Check
// ============================================================================

export function useApprovalHealth(refetchInterval = 30000) {
  const [isHealthy, setIsHealthy] = useState<boolean | null>(null)
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    const checkHealth = async () => {
      setLoading(true)
      try {
        await approvalService.healthCheck()
        setIsHealthy(true)
      } catch {
        setIsHealthy(false)
      } finally {
        setLoading(false)
      }
    }

    checkHealth()

    if (refetchInterval > 0) {
      const interval = setInterval(checkHealth, refetchInterval)
      return () => clearInterval(interval)
    }
  }, [refetchInterval])

  return { isHealthy, loading }
}

// ============================================================================
// Hook: Create New Approval
// ============================================================================

export function useCreateApproval(
  onSuccess?: (approval: ApprovalQueueItem) => void,
  onError?: (error: string) => void
) {
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState<string | null>(null)

  const createApproval = useCallback(async (
    type: ApprovalType,
    title: string,
    description: string,
    options?: {
      amount?: number
      currency?: string
      dueDate?: string
      priority?: ApprovalPriority
      attachments?: string[]
      metadata?: Record<string, any>
    }
  ): Promise<ApprovalQueueItem | null> => {
    setLoading(true)
    setError(null)
    try {
      const result = await approvalService.createApproval({
        type,
        title,
        description,
        ...options,
      })
      onSuccess?.(result)
      return result
    } catch (err) {
      const errorMsg = err instanceof Error ? err.message : 'Failed to create approval'
      setError(errorMsg)
      onError?.(errorMsg)
      return null
    } finally {
      setLoading(false)
    }
  }, [onSuccess, onError])

  const clearError = useCallback(() => {
    setError(null)
  }, [])

  return { createApproval, loading, error, clearError }
}

// ============================================================================
// Hook: Update Approval
// ============================================================================

export function useUpdateApproval(
  onSuccess?: (approval: ApprovalQueueItem) => void,
  onError?: (error: string) => void
) {
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState<string | null>(null)

  const updateApproval = useCallback(async (
    id: string,
    updates: Partial<{
      type: ApprovalType
      title: string
      description: string
      amount: number
      currency: string
      dueDate: string
      priority: ApprovalPriority
      attachments: string[]
      metadata: Record<string, any>
    }>
  ): Promise<ApprovalQueueItem | null> => {
    setLoading(true)
    setError(null)
    try {
      const result = await approvalService.updateApproval(id, updates)
      onSuccess?.(result)
      return result
    } catch (err) {
      const errorMsg = err instanceof Error ? err.message : 'Failed to update approval'
      setError(errorMsg)
      onError?.(errorMsg)
      return null
    } finally {
      setLoading(false)
    }
  }, [onSuccess, onError])

  const clearError = useCallback(() => {
    setError(null)
  }, [])

  return { updateApproval, loading, error, clearError }
}

// ============================================================================
// Export All Types for Components
// ============================================================================

export type {
  // Service types
  ApprovalQueueItem,
  ApprovalQueueResponse,
  ApprovalQueueFilters,
  ApprovalHistoryItem,
  ApprovalHistoryResponse,
  PendingApprovalsSummary,
  WorkflowStatus,
  ApprovalStatus,
  ApprovalType,
  ApprovalPriority,
  ApproveRequest,
  RejectRequest,
  DelegateRequest,
  ApprovalTypeConfig,
  BulkActionRequest,
  BulkActionResponse,
  // Hook result types
  UseApprovalQueueResult,
  UsePendingApprovalsResult,
  UseApprovalResult,
  UseApprovalHistoryResult,
  UseWorkflowStatusResult,
  UseApprovalActionsResult,
  UseBulkActionsResult,
  UseApprovalTypesResult,
  UseUrgentApprovalsResult,
  UseMyApprovalsResult,
  UseApprovalSearchResult,
}
