/**
 * AI Customer Segmentation Service
 *
 * Service: ai-customer-segmentation-service (Port 7002)
 * Base Path: /api/v1/ai/segmentation
 *
 * Features:
 * - Customer segmentation using ML clustering algorithms
 * - Segment characteristics analysis
 * - Dynamic customer assignment to segments
 * - Segment-based insights and recommendations
 * - Customer behavior prediction within segments
 */

import { aiApiClient } from '../../../client/axios-client'
import type { DateRange } from '../../../client/types'

// ============================================================================
// Types
// ============================================================================

export type SegmentStatus = 'ACTIVE' | 'INACTIVE' | 'ARCHIVED'

export type SegmentAlgorithm = 'KMEANS' | 'DBSCAN' | 'HIERARCHICAL' | 'GAUSSIAN_MIXTURE' | 'SPECTRAL'

export type SegmentType = 'DEMOGRAPHIC' | 'BEHAVIORAL' | 'PSYCHOGRAPHIC' | 'GEOGRAPHIC' | 'VALUE_BASED' | 'HYBRID'

export type CustomerLifecycleStage = 'PROSPECT' | 'NEW' | 'ACTIVE' | 'AT_RISK' | 'CHURNED'

export interface CustomerSegment {
  segmentId: string
  name: string
  description: string
  type: SegmentType
  algorithm: SegmentAlgorithm
  status: SegmentStatus

  // Segment size and metrics
  customerCount: number
  percentageOfTotal: number

  // Key characteristics
  characteristics: SegmentCharacteristic[]

  // Performance metrics
  avgOrderValue: number
  avgLifetimeValue: number
  avgPurchaseFrequency: number
  churnRate: number
  retentionRate: number

  // Behavioral indicators
  avgRecencyDays: number
  avgEngagementScore: number
  preferredChannels: string[]

  // Timestamps
  createdAt: string
  updatedAt: string
  lastAnalyzed: string
}

export interface SegmentCharacteristic {
  characteristicId: string
  name: string
  category: 'DEMOGRAPHIC' | 'BEHAVIORAL' | 'TRANSACTIONAL' | 'GEOGRAPHIC' | 'PSYCHOGRAPHIC'
  dataType: 'STRING' | 'NUMBER' | 'BOOLEAN' | 'DATE' | 'ENUM'
  value: string | number | boolean
  importance: number // 0-100, how defining this characteristic is for the segment
  displayValue: string // Human-readable representation
  comparisonToAverage?: {
    value: string | number
    deviation: number // Percentage difference from overall average
    significant: boolean
  }
}

export interface CustomerSegmentDetails extends CustomerSegment {
  // Additional details for full segment view
  trends: {
    growthRate: number // Month over month
    trendDirection: 'UP' | 'DOWN' | 'STABLE'
    projectedSize: number // Expected size next month
  }

  // Risk indicators
  riskFactors: {
    churnRisk: 'LOW' | 'MEDIUM' | 'HIGH'
    attritionProbability: number
    riskReasons: string[]
  }

  // Opportunities
  opportunities: {
    crossSellPotential: number
    upsellPotential: number
    recommendedActions: string[]
  }

  // Representative customers
  representativeCustomers: {
    customerId: string
    name: string
    score: number // How representative they are
  }[]

  // Segment stability
  stability: {
    score: number // 0-100, how stable the segment is over time
    memberChangeRate: number // Percentage of customers that moved in/out
  }
}

export interface SegmentRequest {
  name: string
  description?: string
  type: SegmentType
  algorithm: SegmentAlgorithm
  targetSegmentCount?: number // For algorithms that require specifying K
  minClusterSize?: number
  filters?: SegmentFilter[]
  dateRange?: DateRange
  includeArchived?: boolean
}

export interface SegmentFilter {
  field: string
  operator: 'EQUALS' | 'CONTAINS' | 'GREATER_THAN' | 'LESS_THAN' | 'BETWEEN' | 'IN'
  value: any
  values?: any[]
}

export interface SegmentResponse {
  analysisId: string
  status: 'PENDING' | 'IN_PROGRESS' | 'COMPLETED' | 'FAILED'
  segmentsCreated: number
  customersSegmented: number
  startedAt: string
  completedAt?: string
  error?: string
}

export interface SegmentationAnalysis {
  analysisId: string
  analysisType: SegmentType
  algorithm: SegmentAlgorithm

  // Analysis results
  segments: CustomerSegment[]

  // Overall metrics
  totalCustomers: number
  unsegmentedCustomers: number
  silhouetteScore: number // Cluster quality metric, -1 to 1
  withinClusterSumOfSquares: number

  // Analysis metadata
  featuresUsed: string[]
  featureImportance: {
    feature: string
    importance: number
  }[]

  // Timestamps
  createdAt: string
  validUntil: string
}

export interface CustomerInSegment {
  customerId: string
  customerName: string
  email?: string
  phone?: string

  // Segment membership
  segmentId: string
  segmentName: string
  membershipScore: number // 0-100, confidence of belonging to this segment
  membershipDate: string

  // Customer metrics
  lifetimeValue: number
  totalOrders: number
  avgOrderValue: number
  lastPurchaseDate: string
  predictedChurnProbability: number

  // Lifecycle
  lifecycleStage: CustomerLifecycleStage
  engagementScore: number
  riskLevel: 'LOW' | 'MEDIUM' | 'HIGH'
}

export interface CharacteristicAnalysis {
  characteristicId: string
  name: string
  category: string
  type: string

  // Distribution across segments
  segmentDistribution: {
    segmentId: string
    segmentName: string
    value: string | number
    percentage: number
    count: number
  }[]

  // Overall statistics
  overallDistribution: {
    value: string | number
    percentage: number
    count: number
  }[]

  // Correlation with value
  valueCorrelation: {
    characteristic: string
    correlationCoefficient: number
    significance: 'LOW' | 'MEDIUM' | 'HIGH'
  }[]

  // Timestamps
  lastUpdated: string
}

export interface SegmentComparison {
  segment1: {
    segmentId: string
    segmentName: string
  }
  segment2: {
    segmentId: string
    segmentName: string
  }
  differences: {
    characteristic: string
    segment1Value: string | number
    segment2Value: string | number
    significance: number // 0-100
  }[]
  similarityScore: number // 0-100
}

// ============================================================================
// Service Class
// ============================================================================

class CustomerSegmentationService {
  private readonly basePath = '/api/v1/ai/segmentation'

  /**
   * Get all customer segments
   * Retrieves a list of all segments with optional filtering
   *
   * @param options - Query options for filtering segments
   * @returns Array of customer segments
   */
  async getSegments(options?: {
    status?: SegmentStatus
    type?: SegmentType
    includeInactive?: boolean
  }): Promise<CustomerSegment[]> {
    const response = await aiApiClient.get<CustomerSegment[]>(
      `${this.basePath}/segments`,
      { params: options }
    )
    return response.data
  }

  /**
   * Get specific segment details
   * Retrieves full details including trends, risks, and opportunities
   *
   * @param segmentId - The ID of the segment to retrieve
   * @returns Detailed segment information
   */
  async getSegmentById(segmentId: string): Promise<CustomerSegmentDetails> {
    const response = await aiApiClient.get<CustomerSegmentDetails>(
      `${this.basePath}/segments/${segmentId}`
    )
    return response.data
  }

  /**
   * Run segmentation analysis
   * Initiates a new customer segmentation analysis
   *
   * @param request - Segmentation analysis parameters
   * @returns Analysis response with status
   */
  async runAnalysis(request: SegmentRequest): Promise<SegmentResponse> {
    const response = await aiApiClient.post<SegmentResponse>(
      `${this.basePath}/analyze`,
      request
    )
    return response.data
  }

  /**
   * Get analysis status and results
   * Checks the status of a running or completed analysis
   *
   * @param analysisId - The ID of the analysis to check
   * @returns Full segmentation analysis results
   */
  async getAnalysis(analysisId: string): Promise<SegmentationAnalysis> {
    const response = await aiApiClient.get<SegmentationAnalysis>(
      `${this.basePath}/analyze/${analysisId}`
    )
    return response.data
  }

  /**
   * Get customers in a segment
   * Retrieves paginated list of customers belonging to a segment
   *
   * @param segmentId - The segment ID to get customers for
   * @param options - Pagination and filtering options
   * @returns Paginated list of customers in the segment
   */
  async getCustomersInSegment(
    segmentId: string,
    options?: {
      page?: number
      size?: number
      sortBy?: string
      sortDirection?: 'asc' | 'desc'
      lifecycleStage?: CustomerLifecycleStage
      riskLevel?: 'LOW' | 'MEDIUM' | 'HIGH'
    }
  ): Promise<{ content: CustomerInSegment[]; totalElements: number }> {
    const response = await aiApiClient.get<{ content: CustomerInSegment[]; totalElements: number }>(
      `${this.basePath}/customers/${segmentId}`,
      { params: options }
    )
    return response.data
  }

  /**
   * Get segment characteristics
   * Retrieves available characteristics and their distribution across segments
   *
   * @param options - Filter options for characteristics
   * @returns Array of characteristic analyses
   */
  async getCharacteristics(options?: {
    category?: string
    segmentId?: string
  }): Promise<CharacteristicAnalysis[]> {
    const response = await aiApiClient.get<CharacteristicAnalysis[]>(
      `${this.basePath}/characteristics`,
      { params: options }
    )
    return response.data
  }

  /**
   * Compare two segments
   * Generates a comparison highlighting differences and similarities
   *
   * @param segment1Id - First segment ID
   * @param segment2Id - Second segment ID
   * @returns Segment comparison details
   */
  async compareSegments(
    segment1Id: string,
    segment2Id: string
  ): Promise<SegmentComparison> {
    const response = await aiApiClient.get<SegmentComparison>(
      `${this.basePath}/compare`,
      { params: { segment1Id, segment2Id } }
    )
    return response.data
  }

  /**
   * Update segment details
   * Updates the name, description, or status of a segment
   *
   * @param segmentId - The segment ID to update
   * @param updates - Fields to update
   * @returns Updated segment details
   */
  async updateSegment(
    segmentId: string,
    updates: {
      name?: string
      description?: string
      status?: SegmentStatus
    }
  ): Promise<CustomerSegment> {
    const response = await aiApiClient.patch<CustomerSegment>(
      `${this.basePath}/segments/${segmentId}`,
      updates
    )
    return response.data
  }

  /**
   * Delete a segment
   * Removes a segment (customers become unsegmented)
   *
   * @param segmentId - The segment ID to delete
   */
  async deleteSegment(segmentId: string): Promise<void> {
    await aiApiClient.delete(`${this.basePath}/segments/${segmentId}`)
  }

  /**
   * Get quick segment overview
   * Convenience method for dashboard widgets
   *
   * @returns Summary of active segments
   */
  async getSegmentOverview(): Promise<{
    totalSegments: number
    totalCustomersSegmented: number
    unsegmentedCustomers: number
    topSegments: {
      segmentId: string
      name: string
      customerCount: number
      avgLifetimeValue: number
    }[]
    recentActivity: {
      analysisId: string
      completedAt: string
      segmentsCreated: number
    }[]
  }> {
    const response = await aiApiClient.get(`${this.basePath}/overview`)
    return response.data
  }

  // ========================================================================
  // Helper Methods
  // ========================================================================

  /**
   * Get segment color based on type
   */
  getSegmentColor(type: SegmentType): string {
    const colors: Record<SegmentType, string> = {
      DEMOGRAPHIC: '#2196F3',
      BEHAVIORAL: '#4CAF50',
      PSYCHOGRAPHIC: '#9C27B0',
      GEOGRAPHIC: '#FF9800',
      VALUE_BASED: '#F44336',
      HYBRID: '#00BCD4',
    }
    return colors[type] || '#9E9E9E'
  }

  /**
   * Get lifecycle stage color
   */
  getLifecycleStageColor(stage: CustomerLifecycleStage): string {
    const colors: Record<CustomerLifecycleStage, string> = {
      PROSPECT: '#9E9E9E',
      NEW: '#2196F3',
      ACTIVE: '#4CAF50',
      AT_RISK: '#FF9800',
      CHURNED: '#F44336',
    }
    return colors[stage]
  }

  /**
   * Get risk level color
   */
  getRiskLevelColor(level: 'LOW' | 'MEDIUM' | 'HIGH'): string {
    const colors = {
      LOW: '#4CAF50',
      MEDIUM: '#FF9800',
      HIGH: '#F44336',
    }
    return colors[level]
  }

  /**
   * Format segment size for display
   */
  formatSegmentSize(count: number, total: number): string {
    const percentage = ((count / total) * 100).toFixed(1)
    return `${count.toLocaleString()} (${percentage}%)`
  }

  /**
   * Format currency value for display
   */
  formatCurrency(amount: number, currency = 'USD'): string {
    return new Intl.NumberFormat('en-US', {
      style: 'currency',
      currency,
      minimumFractionDigits: 0,
      maximumFractionDigits: 0,
    }).format(amount)
  }

  /**
   * Calculate segment health score
   * Based on size, growth, and stability metrics
   */
  calculateSegmentHealth(segment: CustomerSegmentDetails): number {
    const sizeScore = Math.min(segment.customerCount / 100, 1) * 30
    const ltvScore = Math.min(segment.avgLifetimeValue / 1000, 1) * 30
    const retentionScore = segment.retentionRate * 0.4
    const stabilityScore = segment.stability.score * 0.3

    return Math.round(sizeScore + ltvScore + retentionScore + stabilityScore)
  }
}

// Export singleton instance
export const customerSegmentationService = new CustomerSegmentationService()

