/**
 * CEO Strategy Service API Client
 *
 * Connects to: ceo-strategy-service (Port 9003)
 * Base Path: /api/v1/strategy
 *
 * Endpoints:
 * - Strategic KPIs: CRUD operations for strategic KPIs
 * - OKRs: Manage Objectives and Key Results
 * - Initiatives: Track strategic initiatives
 * - Dashboard: Strategy overview and insights
 */

import { executiveApiClient } from '../../client/axios-client'
import type {
  PaginatedResponse,
  DateRange,
  KPICategory,
  InitiativeStatus,
  ObjectivePeriod,
} from '../../client/types'

// ============================================================================
// Types
// ============================================================================

/**
 * Strategic KPI - High-level KPIs tracked at executive level
 */
export interface StrategicKPI {
  id: string
  code: string
  name: string
  description: string
  category: KPICategory
  currentValue: number
  targetValue: number
  unit: string
  baselineValue?: number
  progressPercentage: number
  status: 'ON_TRACK' | 'AT_RISK' | 'BEHIND' | 'AHEAD'
  trend: 'IMPROVING' | 'DECLINING' | 'STABLE'
  weight: number // Weight in overall strategic score (0-1)
  owner: string
  department: string
  reportingPeriod: ObjectivePeriod
  year: number
  lastUpdated: string
  nextReviewDate: string
  parentKpiId?: string
  relatedKpiIds?: string[]
  tags?: string[]
}

/**
 * Key Result - Measurable outcome for an objective
 */
export interface KeyResult {
  id: string
  objectiveId: string
  title: string
  description: string
  targetValue: number
  currentValue: number
  unit: string
  progressPercentage: number
  status: 'ON_TRACK' | 'AT_RISK' | 'BEHIND' | 'ACHIEVED'
  dueDate: string
  assignedTo: string
  weight: number // Weight in objective progress (0-1)
  relatedInitiatives?: string[]
  milestones?: KeyResultMilestone[]
  createdAt: string
  updatedAt: string
}

/**
 * Key Result Milestone
 */
export interface KeyResultMilestone {
  id: string
  keyResultId: string
  title: string
  targetValue: number
  dueDate: string
  status: 'PENDING' | 'COMPLETE' | 'OVERDUE'
  completedAt?: string
}

/**
 * OKR (Objective and Key Results)
 */
export interface OKR {
  id: string
  objectiveId: string
  title: string
  description: string
  period: ObjectivePeriod
  year: number
  owner: string
  department: string
  overallProgress: number // 0-100, calculated from key results
  status: 'ON_TRACK' | 'AT_RISK' | 'BEHIND' | 'ACHIEVED'
  confidenceLevel: 'HIGH' | 'MEDIUM' | 'LOW'
  keyResults: KeyResult[]
  startDate: string
  endDate: string
  parentOkrId?: string
  alignedOkrIds?: string[]
  relatedKpiIds?: string[]
  tags?: string[]
  createdAt: string
  updatedAt: string
  lastReviewedAt?: string
}

/**
 * Strategic Initiative - Major strategic programs/projects
 */
export interface Initiative {
  id: string
  name: string
  description: string
  objective: string // The strategic objective this supports
  status: InitiativeStatus
  priority: 'LOW' | 'MEDIUM' | 'HIGH' | 'CRITICAL'
  owner: string
  sponsor?: string // Executive sponsor
  team: string[]
  budget?: {
    allocated: number
    spent: number
    currency: string
  }
  progress: number // 0-100
  startDate: string
  endDate: string
  actualEndDate?: string
  milestones: Milestone[]
  risks: InitiativeRisk[]
  dependencies?: string[] // Other initiative IDs
  blockingIssues?: string[]
  relatedKpiIds?: string[]
  relatedOkrIds?: string[]
  tags?: string[]
  createdAt: string
  updatedAt: string
}

/**
 * Initiative Milestone
 */
export interface Milestone {
  id: string
  initiativeId: string
  title: string
  description: string
  dueDate: string
  status: 'PENDING' | 'IN_PROGRESS' | 'COMPLETE' | 'OVERDUE'
  completedAt?: string
  dependencies?: string[] // Other milestone IDs
  deliverables?: string[]
}

/**
 * Initiative Risk
 */
export interface InitiativeRisk {
  id: string
  initiativeId: string
  title: string
  description: string
  likelihood: 'LOW' | 'MEDIUM' | 'HIGH'
  impact: 'LOW' | 'MEDIUM' | 'HIGH'
  mitigation?: string
  owner?: string
  status: 'OPEN' | 'MITIGATING' | 'CLOSED'
  identifiedAt: string
  updatedAt: string
}

/**
 * Create Strategic KPI Request
 */
export interface CreateStrategicKPIRequest {
  code: string
  name: string
  description: string
  category: KPICategory
  currentValue: number
  targetValue: number
  unit: string
  baselineValue?: number
  weight: number
  owner: string
  department: string
  reportingPeriod: ObjectivePeriod
  year: number
  nextReviewDate: string
  parentKpiId?: string
  relatedKpiIds?: string[]
  tags?: string[]
}

/**
 * Update Strategic KPI Request
 */
export interface UpdateStrategicKPIRequest {
  currentValue?: number
  targetValue?: number
  baselineValue?: number
  status?: 'ON_TRACK' | 'AT_RISK' | 'BEHIND' | 'AHEAD'
  trend?: 'IMPROVING' | 'DECLINING' | 'STABLE'
  weight?: number
  nextReviewDate?: string
  description?: string
  relatedKpiIds?: string[]
  tags?: string[]
}

/**
 * Create OKR Request
 */
export interface CreateOKRRequest {
  title: string
  description: string
  period: ObjectivePeriod
  year: number
  owner: string
  department: string
  keyResults: Omit<KeyResult, 'id' | 'objectiveId' | 'progressPercentage' | 'createdAt' | 'updatedAt'>[]
  startDate: string
  endDate: string
  parentOkrId?: string
  alignedOkrIds?: string[]
  relatedKpiIds?: string[]
  tags?: string[]
}

/**
 * Update OKR Request
 */
export interface UpdateOKRRequest {
  title?: string
  description?: string
  status?: 'ON_TRACK' | 'AT_RISK' | 'BEHIND' | 'ACHIEVED'
  confidenceLevel?: 'HIGH' | 'MEDIUM' | 'LOW'
  endDate?: string
  alignedOkrIds?: string[]
  relatedKpiIds?: string[]
  tags?: string[]
}

/**
 * Create Initiative Request
 */
export interface CreateInitiativeRequest {
  name: string
  description: string
  objective: string
  priority: 'LOW' | 'MEDIUM' | 'HIGH' | 'CRITICAL'
  owner: string
  sponsor?: string
  team: string[]
  budget?: {
    allocated: number
    currency: string
  }
  startDate: string
  endDate: string
  milestones?: Omit<Milestone, 'id' | 'initiativeId'>[]
  relatedKpiIds?: string[]
  relatedOkrIds?: string[]
  tags?: string[]
}

/**
 * Update Initiative Request
 */
export interface UpdateInitiativeRequest {
  name?: string
  description?: string
  objective?: string
  priority?: 'LOW' | 'MEDIUM' | 'HIGH' | 'CRITICAL'
  owner?: string
  sponsor?: string
  team?: string[]
  budget?: {
    allocated?: number
    spent?: number
  }
  progress?: number
  endDate?: string
  relatedKpiIds?: string[]
  relatedOkrIds?: string[]
  tags?: string[]
}

/**
 * Strategy Dashboard Response
 */
export interface StrategyDashboardResponse {
  overview: {
    strategicHealthScore: number
    overallProgress: number
    activeObjectives: number
    activeInitiatives: number
    totalBudgetAllocated: number
    totalBudgetSpent: number
    period: ObjectivePeriod
    year: number
  }
  strategicKPIs: StrategicKPI[]
  okrSummary: OKRSummary[]
  initiativeSummary: InitiativeSummary[]
  upcomingMilestones: Milestone[]
  risksRequiringAttention: InitiativeRisk[]
  trends: {
    improvingKPIs: number
    decliningKPIs: number
    onTrackObjectives: number
    atRiskObjectives: number
    delayedInitiatives: number
    completedInitiatives: number
  }
  lastUpdated: string
}

/**
 * OKR Summary
 */
export interface OKRSummary {
  id: string
  title: string
  period: ObjectivePeriod
  year: number
  overallProgress: number
  status: 'ON_TRACK' | 'AT_RISK' | 'BEHIND' | 'ACHIEVED'
  keyResultsCount: number
  completedKeyResults: number
  owner: string
  department: string
}

/**
 * Initiative Summary
 */
export interface InitiativeSummary {
  id: string
  name: string
  status: InitiativeStatus
  priority: 'LOW' | 'MEDIUM' | 'HIGH' | 'CRITICAL'
  progress: number
  owner: string
  startDate: string
  endDate: string
  budgetUtilization?: number
  milestoneProgress: {
    total: number
    completed: number
  }
}

// ============================================================================
// Service Class
// ============================================================================

class StrategyService {
  private readonly basePath = '/api/v1/strategy'

  // ========================================================================
  // Strategic KPIs
  // ========================================================================

  /**
   * Get all strategic KPIs
   * @param params - Optional query parameters for filtering and pagination
   */
  async getAllStrategicKPIs(params?: {
    category?: KPICategory
    status?: string
    period?: ObjectivePeriod
    year?: number
    department?: string
    page?: number
    size?: number
    sortBy?: string
    sortDirection?: 'asc' | 'desc'
  }): Promise<PaginatedResponse<StrategicKPI>> {
    const response = await executiveApiClient.get<PaginatedResponse<StrategicKPI>>(
      `${this.basePath}/strategic-kpis`,
      { params }
    )
    return response.data
  }

  /**
   * Get a specific strategic KPI by ID
   * @param id - The strategic KPI ID
   */
  async getStrategicKPIById(id: string): Promise<StrategicKPI> {
    const response = await executiveApiClient.get<StrategicKPI>(
      `${this.basePath}/strategic-kpis/${id}`
    )
    return response.data
  }

  /**
   * Create a new strategic KPI
   * @param request - The strategic KPI data to create
   */
  async createStrategicKPI(request: CreateStrategicKPIRequest): Promise<StrategicKPI> {
    const response = await executiveApiClient.post<StrategicKPI>(
      `${this.basePath}/strategic-kpis`,
      request
    )
    return response.data
  }

  /**
   * Update an existing strategic KPI
   * @param id - The strategic KPI ID
   * @param request - The update data
   */
  async updateStrategicKPI(
    id: string,
    request: UpdateStrategicKPIRequest
  ): Promise<StrategicKPI> {
    const response = await executiveApiClient.put<StrategicKPI>(
      `${this.basePath}/strategic-kpis/${id}`,
      request
    )
    return response.data
  }

  /**
   * Delete a strategic KPI
   * @param id - The strategic KPI ID
   */
  async deleteStrategicKPI(id: string): Promise<void> {
    await executiveApiClient.delete(`${this.basePath}/strategic-kpis/${id}`)
  }

  /**
   * Update strategic KPI value (quick update endpoint)
   * @param id - The strategic KPI ID
   * @param currentValue - The new current value
   * @param notes - Optional notes about the update
   */
  async updateKPIValue(id: string, currentValue: number, notes?: string): Promise<StrategicKPI> {
    const response = await executiveApiClient.patch<StrategicKPI>(
      `${this.basePath}/strategic-kpis/${id}/value`,
      { currentValue, notes }
    )
    return response.data
  }

  // ========================================================================
  // OKRs (Objectives and Key Results)
  // ========================================================================

  /**
   * Get all OKRs
   * @param params - Optional query parameters for filtering and pagination
   */
  async getAllOKRs(params?: {
    period?: ObjectivePeriod
    year?: number
    department?: string
    owner?: string
    status?: string
    page?: number
    size?: number
    sortBy?: string
    sortDirection?: 'asc' | 'desc'
  }): Promise<PaginatedResponse<OKR>> {
    const response = await executiveApiClient.get<PaginatedResponse<OKR>>(
      `${this.basePath}/okrs`,
      { params }
    )
    return response.data
  }

  /**
   * Get a specific OKR by ID
   * @param id - The OKR ID
   */
  async getOKRById(id: string): Promise<OKR> {
    const response = await executiveApiClient.get<OKR>(
      `${this.basePath}/okrs/${id}`
    )
    return response.data
  }

  /**
   * Create a new OKR
   * @param request - The OKR data to create
   */
  async createOKR(request: CreateOKRRequest): Promise<OKR> {
    const response = await executiveApiClient.post<OKR>(
      `${this.basePath}/okrs`,
      request
    )
    return response.data
  }

  /**
   * Update an existing OKR
   * @param id - The OKR ID
   * @param request - The update data
   */
  async updateOKR(id: string, request: UpdateOKRRequest): Promise<OKR> {
    const response = await executiveApiClient.put<OKR>(
      `${this.basePath}/okrs/${id}`,
      request
    )
    return response.data
  }

  /**
   * Delete an OKR
   * @param id - The OKR ID
   */
  async deleteOKR(id: string): Promise<void> {
    await executiveApiClient.delete(`${this.basePath}/okrs/${id}`)
  }

  /**
   * Update key result progress
   * @param okrId - The OKR ID
   * @param keyResultId - The key result ID
   * @param currentValue - The new current value
   * @param notes - Optional notes about the update
   */
  async updateKeyResultProgress(
    okrId: string,
    keyResultId: string,
    currentValue: number,
    notes?: string
  ): Promise<KeyResult> {
    const response = await executiveApiClient.patch<KeyResult>(
      `${this.basePath}/okrs/${okrId}/key-results/${keyResultId}/progress`,
      { currentValue, notes }
    )
    return response.data
  }

  /**
   * Add a new key result to an OKR
   * @param okrId - The OKR ID
   * @param keyResult - The key result data
   */
  async addKeyResult(
    okrId: string,
    keyResult: Omit<KeyResult, 'id' | 'objectiveId' | 'progressPercentage' | 'createdAt' | 'updatedAt'>
  ): Promise<KeyResult> {
    const response = await executiveApiClient.post<KeyResult>(
      `${this.basePath}/okrs/${okrId}/key-results`,
      keyResult
    )
    return response.data
  }

  /**
   * Update a key result
   * @param okrId - The OKR ID
   * @param keyResultId - The key result ID
   * @param updates - The update data
   */
  async updateKeyResult(
    okrId: string,
    keyResultId: string,
    updates: Partial<Pick<KeyResult, 'title' | 'description' | 'targetValue' | 'unit' | 'dueDate' | 'assignedTo' | 'weight'>>
  ): Promise<KeyResult> {
    const response = await executiveApiClient.put<KeyResult>(
      `${this.basePath}/okrs/${okrId}/key-results/${keyResultId}`,
      updates
    )
    return response.data
  }

  /**
   * Delete a key result
   * @param okrId - The OKR ID
   * @param keyResultId - The key result ID
   */
  async deleteKeyResult(okrId: string, keyResultId: string): Promise<void> {
    await executiveApiClient.delete(`${this.basePath}/okrs/${okrId}/key-results/${keyResultId}`)
  }

  // ========================================================================
  // Strategic Initiatives
  // ========================================================================

  /**
   * Get all strategic initiatives
   * @param params - Optional query parameters for filtering and pagination
   */
  async getAllInitiatives(params?: {
    status?: InitiativeStatus
    priority?: string
    department?: string
    owner?: string
    page?: number
    size?: number
    sortBy?: string
    sortDirection?: 'asc' | 'desc'
  }): Promise<PaginatedResponse<Initiative>> {
    const response = await executiveApiClient.get<PaginatedResponse<Initiative>>(
      `${this.basePath}/initiatives`,
      { params }
    )
    return response.data
  }

  /**
   * Get a specific initiative by ID
   * @param id - The initiative ID
   */
  async getInitiativeById(id: string): Promise<Initiative> {
    const response = await executiveApiClient.get<Initiative>(
      `${this.basePath}/initiatives/${id}`
    )
    return response.data
  }

  /**
   * Create a new initiative
   * @param request - The initiative data to create
   */
  async createInitiative(request: CreateInitiativeRequest): Promise<Initiative> {
    const response = await executiveApiClient.post<Initiative>(
      `${this.basePath}/initiatives`,
      request
    )
    return response.data
  }

  /**
   * Update an existing initiative
   * @param id - The initiative ID
   * @param request - The update data
   */
  async updateInitiative(id: string, request: UpdateInitiativeRequest): Promise<Initiative> {
    const response = await executiveApiClient.put<Initiative>(
      `${this.basePath}/initiatives/${id}`,
      request
    )
    return response.data
  }

  /**
   * Update initiative status
   * @param id - The initiative ID
   * @param status - The new status
   * @param reason - Optional reason for status change
   */
  async updateInitiativeStatus(
    id: string,
    status: InitiativeStatus,
    reason?: string
  ): Promise<Initiative> {
    const response = await executiveApiClient.patch<Initiative>(
      `${this.basePath}/initiatives/${id}/status`,
      { status, reason }
    )
    return response.data
  }

  /**
   * Delete an initiative
   * @param id - The initiative ID
   */
  async deleteInitiative(id: string): Promise<void> {
    await executiveApiClient.delete(`${this.basePath}/initiatives/${id}`)
  }

  /**
   * Add a milestone to an initiative
   * @param id - The initiative ID
   * @param milestone - The milestone data
   */
  async addMilestone(
    id: string,
    milestone: Omit<Milestone, 'id' | 'initiativeId'>
  ): Promise<Milestone> {
    const response = await executiveApiClient.post<Milestone>(
      `${this.basePath}/initiatives/${id}/milestones`,
      milestone
    )
    return response.data
  }

  /**
   * Update a milestone
   * @param initiativeId - The initiative ID
   * @param milestoneId - The milestone ID
   * @param updates - The update data
   */
  async updateMilestone(
    initiativeId: string,
    milestoneId: string,
    updates: Partial<Pick<Milestone, 'title' | 'description' | 'dueDate' | 'status'>>
  ): Promise<Milestone> {
    const response = await executiveApiClient.put<Milestone>(
      `${this.basePath}/initiatives/${initiativeId}/milestones/${milestoneId}`,
      updates
    )
    return response.data
  }

  /**
   * Complete a milestone
   * @param initiativeId - The initiative ID
   * @param milestoneId - The milestone ID
   */
  async completeMilestone(initiativeId: string, milestoneId: string): Promise<Milestone> {
    const response = await executiveApiClient.patch<Milestone>(
      `${this.basePath}/initiatives/${initiativeId}/milestones/${milestoneId}/complete`
    )
    return response.data
  }

  /**
   * Delete a milestone
   * @param initiativeId - The initiative ID
   * @param milestoneId - The milestone ID
   */
  async deleteMilestone(initiativeId: string, milestoneId: string): Promise<void> {
    await executiveApiClient.delete(
      `${this.basePath}/initiatives/${initiativeId}/milestones/${milestoneId}`
    )
  }

  /**
   * Add a risk to an initiative
   * @param id - The initiative ID
   * @param risk - The risk data
   */
  async addRisk(
    id: string,
    risk: Omit<InitiativeRisk, 'id' | 'initiativeId' | 'identifiedAt' | 'updatedAt'>
  ): Promise<InitiativeRisk> {
    const response = await executiveApiClient.post<InitiativeRisk>(
      `${this.basePath}/initiatives/${id}/risks`,
      risk
    )
    return response.data
  }

  /**
   * Update a risk
   * @param initiativeId - The initiative ID
   * @param riskId - The risk ID
   * @param updates - The update data
   */
  async updateRisk(
    initiativeId: string,
    riskId: string,
    updates: Partial<Pick<InitiativeRisk, 'likelihood' | 'impact' | 'mitigation' | 'status' | 'owner'>>
  ): Promise<InitiativeRisk> {
    const response = await executiveApiClient.put<InitiativeRisk>(
      `${this.basePath}/initiatives/${initiativeId}/risks/${riskId}`,
      updates
    )
    return response.data
  }

  /**
   * Delete a risk
   * @param initiativeId - The initiative ID
   * @param riskId - The risk ID
   */
  async deleteRisk(initiativeId: string, riskId: string): Promise<void> {
    await executiveApiClient.delete(
      `${this.basePath}/initiatives/${initiativeId}/risks/${riskId}`
    )
  }

  // ========================================================================
  // Dashboard
  // ========================================================================

  /**
   * Get strategy dashboard overview
   * This is the main endpoint for CEO strategy dashboard
   * @param period - Optional period filter
   * @param year - Optional year filter
   */
  async getDashboard(
    period?: ObjectivePeriod,
    year?: number
  ): Promise<StrategyDashboardResponse> {
    const response = await executiveApiClient.get<StrategyDashboardResponse>(
      `${this.basePath}/dashboard`,
      { params: period || year ? { period, year } : undefined }
    )
    return response.data
  }

  /**
   * Get strategy execution insights
   * @param dateRange - Optional date range for insights
   */
  async getExecutionInsights(dateRange?: DateRange): Promise<StrategyInsights> {
    const response = await executiveApiClient.get<StrategyInsights>(
      `${this.basePath}/insights/execution`,
      { params: dateRange }
    )
    return response.data
  }

  /**
   * Get performance trends over time
   * @param params - Query parameters for trend analysis
   */
  async getPerformanceTrends(params?: {
    period?: ObjectivePeriod
    year?: number
    type?: 'kpis' | 'okrs' | 'initiatives' | 'all'
  }): Promise<PerformanceTrends> {
    const response = await executiveApiClient.get<PerformanceTrends>(
      `${this.basePath}/trends`,
      { params }
    )
    return response.data
  }

  /**
   * Health check
   */
  async healthCheck(): Promise<{ status: string; timestamp: string }> {
    const response = await executiveApiClient.get<{ status: string; timestamp: string }>(
      `${this.basePath}/health`
    )
    return response.data
  }

  // ========================================================================
  // Helper Methods
  // ========================================================================

  /**
   * Get status color for display
   */
  getStatusColor(status: string): string {
    const colors: Record<string, string> = {
      ON_TRACK: '#4CAF50',
      AHEAD: '#2196F3',
      AT_RISK: '#FF9800',
      BEHIND: '#F44336',
      ACHIEVED: '#4CAF50',
      IMPROVING: '#4CAF50',
      DECLINING: '#F44336',
      STABLE: '#9E9E9E',
      PLANNED: '#2196F3',
      IN_PROGRESS: '#FF9800',
      COMPLETE: '#4CAF50',
      DELAYED: '#F44336',
      CANCELLED: '#9E9E9E',
    }
    return colors[status] || '#9E9E9E'
  }

  /**
   * Get priority color
   */
  getPriorityColor(priority: string): string {
    const colors: Record<string, string> = {
      LOW: '#9E9E9E',
      MEDIUM: '#FF9800',
      HIGH: '#FF5722',
      CRITICAL: '#F44336',
    }
    return colors[priority] || '#9E9E9E'
  }

  /**
   * Get progress bar color
   */
  getProgressColor(percentage: number): string {
    if (percentage >= 75) return '#4CAF50'
    if (percentage >= 50) return '#8BC34A'
    if (percentage >= 25) return '#FF9800'
    return '#F44336'
  }

  /**
   * Calculate strategic health score color
   */
  getHealthScoreColor(score: number): string {
    if (score >= 80) return '#4CAF50'
    if (score >= 60) return '#FF9800'
    return '#F44336'
  }

  /**
   * Get confidence level icon
   */
  getConfidenceIcon(level: string): string {
    const icons: Record<string, string> = {
      HIGH: '●',
      MEDIUM: '◆',
      LOW: '○',
    }
    return icons[level] || '○'
  }
}

// ============================================================================
// Additional Types for Dashboard/Insights
// ============================================================================

/**
 * Strategy Insights
 */
export interface StrategyInsights {
  overallStrategicHealth: {
    score: number
    trend: 'IMPROVING' | 'DECLINING' | 'STABLE'
    changeFromLastPeriod: number
  }
  kpiInsights: {
    totalKPIs: number
    onTrack: number
    atRisk: number
    behind: number
    topImproving: StrategicKPI[]
    topDeclining: StrategicKPI[]
  }
  okrInsights: {
    totalObjectives: number
    achieved: number
    onTrack: number
    atRisk: number
    averageProgress: number
  }
  initiativeInsights: {
    totalInitiatives: number
    completed: number
    inProgress: number
    delayed: number
    totalBudget: number
    budgetUtilization: number
  }
  recommendations: Recommendation[]
  generatedAt: string
}

/**
 * Recommendation
 */
export interface Recommendation {
  id: string
  type: 'KPI' | 'OKR' | 'INITIATIVE'
  priority: 'LOW' | 'MEDIUM' | 'HIGH'
  title: string
  description: string
  actionable: boolean
  relatedEntityId?: string
  relatedEntityType?: string
}

/**
 * Performance Trends
 */
export interface PerformanceTrends {
  period: ObjectivePeriod
  year: number
  kpiTrends: KPITrend[]
  okrTrends: OKRTrend[]
  initiativeTrends: InitiativeTrend[]
  monthlyProgress: MonthlyProgress[]
}

/**
 * KPI Trend
 */
export interface KPITrend {
  kpiId: string
  kpiName: string
  category: KPICategory
  values: {
    period: string
    value: number
    target: number
  }[]
  trend: 'IMPROVING' | 'DECLINING' | 'STABLE'
  variance: number
}

/**
 * OKR Trend
 */
export interface OKRTrend {
  okrId: string
  title: string
  progress: number
  keyResultsCompleted: number
  totalKeyResults: number
  trend: 'ON_TRACK' | 'FALLING_BEHIND' | 'AHEAD'
}

/**
 * Initiative Trend
 */
export interface InitiativeTrend {
  initiativeId: string
  name: string
  status: InitiativeStatus
  progress: number
  milestoneProgress: {
    completed: number
    total: number
  }
  budgetUtilization: number
  trend: 'ON_TRACK' | 'DELAYED' | 'AHEAD'
}

/**
 * Monthly Progress
 */
export interface MonthlyProgress {
  month: string
  overallScore: number
  kpiAverage: number
  okrAverage: number
  initiativeAverage: number
}

// Export singleton instance
export const strategyService = new StrategyService()

// Export type for use in components
export type { StrategyService }
