/**
 * CEO Approval Service API Client
 *
 * Connects to: ceo-approval-service (Port 9002)
 * Base Path: /api/v1/approvals
 *
 * Endpoints:
 * - GET /queue - Get approval queue (with filters)
 * - GET /queue/{id} - Get specific approval request details
 * - POST /queue/{id}/approve - Approve a request
 * - POST /queue/{id}/reject - Reject a request
 * - POST /queue/{id}/delegate - Delegate a request
 * - GET /history - Get approval history
 * - GET /pending - Get pending approvals count
 * - GET /workflow/{id}/status - Get workflow status for approval
 * - GET /types - Get available approval types
 */

import { executiveApprovalApiClient } from '../../client/axios-client'
import type {
  PaginatedResponse,
  DateRange,
  ApprovalStatus,
  ApprovalType,
  Comment,
} from '../../client/types'

// ============================================================================
// Types
// ============================================================================

/**
 * Approval priority levels
 */
export type ApprovalPriority = 'LOW' | 'MEDIUM' | 'HIGH' | 'CRITICAL'

/**
 * Urgency levels for approvals
 */
export type ApprovalUrgency = 'LOW' | 'MEDIUM' | 'HIGH' | 'CRITICAL'

/**
 * Workflow step status
 */
export type WorkflowStepStatus = 'PENDING' | 'IN_PROGRESS' | 'COMPLETED' | 'SKIPPED' | 'FAILED'

/**
 * Workflow status
 */
export type WorkflowStatusType = 'ACTIVE' | 'COMPLETED' | 'CANCELLED' | 'ON_HOLD'

/**
 * Approval requester information
 */
export interface ApprovalRequester {
  id: string
  name: string
  email: string
  department: string
  role: string
}

/**
 * Approval queue item
 */
export interface ApprovalQueueItem {
  id: string
  type: ApprovalType
  title: string
  description: string
  requester: ApprovalRequester
  requestedAt: string
  urgency: ApprovalUrgency
  priority: ApprovalPriority
  status: ApprovalStatus
  amount?: number
  currency?: string
  dueDate?: string
  workflowStep?: string
  attachments?: Attachment[]
  comments?: Comment[]
  metadata?: Record<string, any>
}

/**
 * Attachment for approval requests
 */
export interface Attachment {
  id: string
  name: string
  type: string
  size: number
  url: string
  uploadedAt: string
}

/**
 * Approval queue response with pagination
 */
export interface ApprovalQueueResponse extends PaginatedResponse<ApprovalQueueItem> {
  summary: {
    total: number
    pending: number
    approved: number
    rejected: number
    delegated: number
  }
}

/**
 * Approval queue filters
 */
export interface ApprovalQueueFilters {
  status?: ApprovalStatus
  type?: ApprovalType
  priority?: ApprovalPriority
  urgency?: ApprovalUrgency
  startDate?: string
  endDate?: string
  requesterId?: string
  department?: string
  search?: string
}

/**
 * Approve request payload
 */
export interface ApproveRequest {
  comments?: string
  attachments?: string[]
  conditions?: string[]
}

/**
 * Reject request payload
 */
export interface RejectRequest {
  reason: string
  comments?: string
  allowResubmit?: boolean
}

/**
 * Delegate request payload
 */
export interface DelegateRequest {
  delegateTo: string
  delegateToName: string
  reason?: string
  comments?: string
}

/**
 * Approval history item
 */
export interface ApprovalHistoryItem {
  id: string
  approvalId: string
  type: ApprovalType
  title: string
  requester: ApprovalRequester
  requestedAt: string
  processedAt: string
  status: ApprovalStatus
  processedBy?: {
    id: string
    name: string
    role: string
  }
  amount?: number
  currency?: string
  comments?: string
  reason?: string
  delegatedTo?: string
}

/**
 * Approval history response with pagination
 */
export interface ApprovalHistoryResponse extends PaginatedResponse<ApprovalHistoryItem> {
  summary: {
    total: number
    approved: number
    rejected: number
    delegated: number
    totalAmount?: number
  }
}

/**
 * Pending approvals count summary
 */
export interface PendingApprovalsSummary {
  total: number
  critical: number
  high: number
  medium: number
  low: number
  overdue: number
  byType: Record<ApprovalType, number>
}

/**
 * Workflow step information
 */
export interface WorkflowStep {
  id: string
  name: string
  description?: string
  status: WorkflowStepStatus
  assignee?: {
    id: string
    name: string
    role: string
  }
  startedAt?: string
  completedAt?: string
  comments?: string[]
}

/**
 * Workflow status information
 */
export interface WorkflowStatus {
  id: string
  approvalId: string
  name: string
  status: WorkflowStatusType
  currentStep: string
  steps: WorkflowStep[]
  startedAt: string
  completedAt?: string
  estimatedCompletion?: string
  progress: number // 0-100
}

/**
 * Available approval type configuration
 */
export interface ApprovalTypeConfig {
  type: ApprovalType
  name: string
  description: string
  requiresAmount: boolean
  requiresDocuments: boolean
  requiresJustification: boolean
  autoApprovalThreshold?: number
  escalationRules?: EscalationRule[]
  allowedRoles: string[]
}

/**
 * Escalation rule for approval timeouts
 */
export interface EscalationRule {
  level: number
  escalateTo: string
  escalateAfterHours: number
  notifyRoles: string[]
}

/**
 * Create approval request payload
 */
export interface CreateApprovalRequest {
  type: ApprovalType
  title: string
  description: string
  amount?: number
  currency?: string
  dueDate?: string
  priority?: ApprovalPriority
  attachments?: string[]
  metadata?: Record<string, any>
}

/**
 * Bulk action request
 */
export interface BulkActionRequest {
  approvalIds: string[]
  action: 'approve' | 'reject' | 'delegate'
  reason?: string
  comments?: string
  delegateTo?: string
}

/**
 * Bulk action response
 */
export interface BulkActionResponse {
  success: string[]
  failed: Array<{
    id: string
    error: string
  }>
  totalCount: number
  successCount: number
  failedCount: number
}

// ============================================================================
// Service Class
// ============================================================================

class ApprovalService {
  private readonly basePath = '/api/v1/approvals'

  /**
   * Get approval queue with filters
   *
   * @param filters - Optional filters for status, type, priority, dates, etc.
   * @param params - Pagination parameters
   * @returns Paginated list of approval queue items
   */
  async getApprovalQueue(
    filters?: ApprovalQueueFilters,
    params?: {
      page?: number
      size?: number
      sortBy?: string
      sortDirection?: 'asc' | 'desc'
    }
  ): Promise<ApprovalQueueResponse> {
    const response = await executiveApprovalApiClient.get<ApprovalQueueResponse>(
      `${this.basePath}/queue`,
      {
        params: {
          ...filters,
          ...params,
        },
      }
    )
    return response.data
  }

  /**
   * Get approval queue by status
   *
   * @param status - Approval status to filter by
   * @param params - Pagination parameters
   * @returns Paginated list of approvals with the specified status
   */
  async getApprovalsByStatus(
    status: ApprovalStatus,
    params?: {
      page?: number
      size?: number
      sortBy?: string
      sortDirection?: 'asc' | 'desc'
    }
  ): Promise<ApprovalQueueResponse> {
    return this.getApprovalQueue({ status }, params)
  }

  /**
   * Get approval queue by type
   *
   * @param type - Approval type to filter by
   * @param params - Pagination parameters
   * @returns Paginated list of approvals of the specified type
   */
  async getApprovalsByType(
    type: ApprovalType,
    params?: {
      page?: number
      size?: number
      sortBy?: string
      sortDirection?: 'asc' | 'desc'
    }
  ): Promise<ApprovalQueueResponse> {
    return this.getApprovalQueue({ type }, params)
  }

  /**
   * Get specific approval request details by ID
   *
   * @param id - Approval request ID
   * @returns Complete approval request details
   */
  async getApprovalById(id: string): Promise<ApprovalQueueItem> {
    const response = await executiveApprovalApiClient.get<ApprovalQueueItem>(
      `${this.basePath}/queue/${id}`
    )
    return response.data
  }

  /**
   * Approve an approval request
   *
   * @param id - Approval request ID
   * @param request - Approval details (comments, conditions, attachments)
   * @returns Updated approval request
   */
  async approveRequest(id: string, request: ApproveRequest): Promise<ApprovalQueueItem> {
    const response = await executiveApprovalApiClient.post<ApprovalQueueItem>(
      `${this.basePath}/queue/${id}/approve`,
      request
    )
    return response.data
  }

  /**
   * Reject an approval request
   *
   * @param id - Approval request ID
   * @param request - Rejection details (reason, comments, allowResubmit)
   * @returns Updated approval request
   */
  async rejectRequest(id: string, request: RejectRequest): Promise<ApprovalQueueItem> {
    const response = await executiveApprovalApiClient.post<ApprovalQueueItem>(
      `${this.basePath}/queue/${id}/reject`,
      request
    )
    return response.data
  }

  /**
   * Delegate an approval request to another user
   *
   * @param id - Approval request ID
   * @param request - Delegation details (delegateTo, reason, comments)
   * @returns Updated approval request
   */
  async delegateRequest(id: string, request: DelegateRequest): Promise<ApprovalQueueItem> {
    const response = await executiveApprovalApiClient.post<ApprovalQueueItem>(
      `${this.basePath}/queue/${id}/delegate`,
      request
    )
    return response.data
  }

  /**
   * Get approval history
   *
   * @param filters - Optional filters for date range, type, status
   * @param params - Pagination parameters
   * @returns Paginated list of approval history items
   */
  async getApprovalHistory(
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
  ): Promise<ApprovalHistoryResponse> {
    const response = await executiveApprovalApiClient.get<ApprovalHistoryResponse>(
      `${this.basePath}/history`,
      {
        params: {
          ...filters,
          ...params,
        },
      }
    )
    return response.data
  }

  /**
   * Get pending approvals count summary
   *
   * @returns Summary of pending approvals by priority and type
   */
  async getPendingApprovalsCount(): Promise<PendingApprovalsSummary> {
    const response = await executiveApprovalApiClient.get<PendingApprovalsSummary>(
      `${this.basePath}/pending`
    )
    return response.data
  }

  /**
   * Get workflow status for an approval
   *
   * @param id - Approval request ID
   * @returns Workflow status with all steps
   */
  async getWorkflowStatus(id: string): Promise<WorkflowStatus> {
    const response = await executiveApprovalApiClient.get<WorkflowStatus>(
      `${this.basePath}/workflow/${id}/status`
    )
    return response.data
  }

  /**
   * Get available approval types
   *
   * @returns List of configured approval types with their settings
   */
  async getApprovalTypes(): Promise<ApprovalTypeConfig[]> {
    const response = await executiveApprovalApiClient.get<ApprovalTypeConfig[]>(
      `${this.basePath}/types`
    )
    return response.data
  }

  /**
   * Create a new approval request
   *
   * @param request - Approval request details
   * @returns Created approval request
   */
  async createApproval(request: CreateApprovalRequest): Promise<ApprovalQueueItem> {
    const response = await executiveApprovalApiClient.post<ApprovalQueueItem>(
      this.basePath,
      request
    )
    return response.data
  }

  /**
   * Update an existing approval request
   *
   * @param id - Approval request ID
   * @param request - Updated approval details
   * @returns Updated approval request
   */
  async updateApproval(
    id: string,
    request: Partial<CreateApprovalRequest>
  ): Promise<ApprovalQueueItem> {
    const response = await executiveApprovalApiClient.put<ApprovalQueueItem>(
      `${this.basePath}/${id}`,
      request
    )
    return response.data
  }

  /**
   * Cancel an approval request
   *
   * @param id - Approval request ID
   * @param reason - Reason for cancellation
   * @returns Updated approval request
   */
  async cancelApproval(id: string, reason: string): Promise<ApprovalQueueItem> {
    const response = await executiveApprovalApiClient.post<ApprovalQueueItem>(
      `${this.basePath}/queue/${id}/cancel`,
      { reason }
    )
    return response.data
  }

  /**
   * Bulk approve/reject/delegate multiple approvals
   *
   * @param request - Bulk action request with approval IDs and action
   * @returns Bulk action response with success/failed items
   */
  async bulkAction(request: BulkActionRequest): Promise<BulkActionResponse> {
    const response = await executiveApprovalApiClient.post<BulkActionResponse>(
      `${this.basePath}/bulk`,
      request
    )
    return response.data
  }

  /**
   * Add comment to approval request
   *
   * @param id - Approval request ID
   * @param comment - Comment content
   * @returns Updated approval request with new comment
   */
  async addComment(id: string, comment: string): Promise<ApprovalQueueItem> {
    const response = await executiveApprovalApiClient.post<ApprovalQueueItem>(
      `${this.basePath}/queue/${id}/comments`,
      { content: comment }
    )
    return response.data
  }

  /**
   * Search approval queue
   *
   * @param searchTerm - Search term to match against title, description, requester
   * @param params - Pagination parameters
   * @returns Paginated list of matching approvals
   */
  async searchApprovals(
    searchTerm: string,
    params?: {
      page?: number
      size?: number
      sortBy?: string
      sortDirection?: 'asc' | 'desc'
    }
  ): Promise<ApprovalQueueResponse> {
    return this.getApprovalQueue({ search: searchTerm }, params)
  }

  /**
   * Get approvals by date range
   *
   * @param dateRange - Start and end dates
   * @param params - Pagination parameters
   * @returns Paginated list of approvals in date range
   */
  async getApprovalsByDateRange(
    dateRange: DateRange,
    params?: {
      page?: number
      size?: number
      sortBy?: string
      sortDirection?: 'asc' | 'desc'
    }
  ): Promise<ApprovalQueueResponse> {
    return this.getApprovalQueue(
      {
        startDate: dateRange.startDate,
        endDate: dateRange.endDate,
      },
      params
    )
  }

  /**
   * Get approvals requiring urgent attention (critical or overdue)
   *
   * @param params - Pagination parameters
   * @returns Paginated list of urgent approvals
   */
  async getUrgentApprovals(params?: {
    page?: number
    size?: number
    sortBy?: string
    sortDirection?: 'asc' | 'desc'
  }): Promise<ApprovalQueueResponse> {
    const response = await executiveApprovalApiClient.get<ApprovalQueueResponse>(
      `${this.basePath}/queue/urgent`,
      { params }
    )
    return response.data
  }

  /**
   * Get approvals requested by current user
   *
   * @param params - Pagination parameters
   * @returns Paginated list of user's approval requests
   */
  async getMyApprovals(params?: {
    page?: number
    size?: number
    sortBy?: string
    sortDirection?: 'asc' | 'desc'
  }): Promise<ApprovalQueueResponse> {
    const response = await executiveApprovalApiClient.get<ApprovalQueueResponse>(
      `${this.basePath}/my-approvals`,
      { params }
    )
    return response.data
  }

  /**
   * Health check for approval service
   *
   * @returns Service health status
   */
  async healthCheck(): Promise<{ status: string; timestamp: string }> {
    const response = await executiveApprovalApiClient.get<{ status: string; timestamp: string }>(
      `${this.basePath}/health`
    )
    return response.data
  }

  // ========================================================================
  // Helper Methods
  // ========================================================================

  /**
   * Get status color for UI display
   */
  getStatusColor(status: ApprovalStatus): string {
    const colors: Record<ApprovalStatus, string> = {
      PENDING: '#FF9800',
      APPROVED: '#4CAF50',
      REJECTED: '#F44336',
      DELEGATED: '#2196F3',
    }
    return colors[status] || '#9E9E9E'
  }

  /**
   * Get priority color for UI display
   */
  getPriorityColor(priority: ApprovalPriority): string {
    const colors: Record<ApprovalPriority, string> = {
      LOW: '#9E9E9E',
      MEDIUM: '#2196F3',
      HIGH: '#FF9800',
      CRITICAL: '#F44336',
    }
    return colors[priority] || '#9E9E9E'
  }

  /**
   * Get urgency color for UI display
   */
  getUrgencyColor(urgency: ApprovalUrgency): string {
    const colors: Record<ApprovalUrgency, string> = {
      LOW: '#9E9E9E',
      MEDIUM: '#2196F3',
      HIGH: '#FF9800',
      CRITICAL: '#F44336',
    }
    return colors[urgency] || '#9E9E9E'
  }

  /**
   * Get approval type icon
   */
  getTypeIcon(type: ApprovalType): string {
    const icons: Record<ApprovalType, string> = {
      BUDGET: '$',
      HIRING: 'person',
      INITIATIVE: 'flag',
      EXPENSE: 'receipt',
      STRATEGY: 'trending_up',
    }
    return icons[type] || 'description'
  }

  /**
   * Format amount with currency
   */
  formatAmount(amount: number, currency = 'USD'): string {
    return new Intl.NumberFormat('en-US', {
      style: 'currency',
      currency,
    }).format(amount)
  }

  /**
   * Check if approval is overdue
   */
  isOverdue(dueDate: string, status: ApprovalStatus): boolean {
    return status === 'PENDING' && new Date(dueDate) < new Date()
  }

  /**
   * Calculate days until due
   */
  getDaysUntilDue(dueDate: string): number {
    const due = new Date(dueDate)
    const now = new Date()
    const diff = due.getTime() - now.getTime()
    return Math.ceil(diff / (1000 * 60 * 60 * 24))
  }

  /**
   * Get workflow progress percentage
   */
  getWorkflowProgress(workflow: WorkflowStatus): number {
    return workflow.progress
  }

  /**
   * Get workflow status color
   */
  getWorkflowStatusColor(status: WorkflowStatusType): string {
    const colors: Record<WorkflowStatusType, string> = {
      ACTIVE: '#2196F3',
      COMPLETED: '#4CAF50',
      CANCELLED: '#F44336',
      ON_HOLD: '#FF9800',
    }
    return colors[status] || '#9E9E9E'
  }

  /**
   * Get step status color
   */
  getStepStatusColor(status: WorkflowStepStatus): string {
    const colors: Record<WorkflowStepStatus, string> = {
      PENDING: '#9E9E9E',
      IN_PROGRESS: '#2196F3',
      COMPLETED: '#4CAF50',
      SKIPPED: '#FF9800',
      FAILED: '#F44336',
    }
    return colors[status] || '#9E9E9E'
  }
}

// Export singleton instance
export const approvalService = new ApprovalService()

// Export type for use in components
export type { ApprovalService }
