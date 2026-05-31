/**
 * CEO Report Service API Client
 *
 * Connects to: executive-report-service (Port 9043)
 * Base Path: /api/v1/reports
 *
 * Endpoints:
 * - GET /library - Get report library
 * - GET /reports/{id} - Get specific report
 * - POST /generate - Generate new report
 * - POST /schedule - Schedule report
 * - GET /scheduled - Get scheduled reports
 * - PUT /reports/{id} - Update report
 * - DELETE /reports/{id} - Delete report
 * - GET /templates - Get report templates
 * - GET /history/{reportId} - Get report generation history
 */

import { executiveReportApiClient } from '../../client/axios-client'
import type { PaginatedResponse } from '../../client/types'

// ============================================================================
// Types
// ============================================================================

/**
 * Report type enumeration
 */
export type ReportType =
  | 'STRATEGIC_OVERVIEW'
  | 'FINANCIAL_PERFORMANCE'
  | 'OPERATIONAL_HEALTH'
  | 'DOMAIN_PERFORMANCE'
  | 'KPI_SUMMARY'
  | 'EXECUTIVE_SUMMARY'
  | 'QUARTERLY_REVIEW'
  | 'ANNUAL_REPORT'
  | 'CUSTOM'

/**
 * Report format enumeration
 */
export type ReportFormat = 'PDF' | 'EXCEL' | 'CSV' | 'JSON' | 'HTML'

/**
 * Report status enumeration
 */
export type ReportStatus = 'READY' | 'GENERATING' | 'FAILED' | 'SCHEDULED' | 'ARCHIVED'

/**
 * Report frequency enumeration
 */
export type ReportFrequency = 'DAILY' | 'WEEKLY' | 'MONTHLY' | 'QUARTERLY' | 'YEARLY' | 'ON_DEMAND'

/**
 * Report entity
 */
export interface Report {
  id: string
  name: string
  description: string
  type: ReportType
  format: ReportFormat
  status: ReportStatus
  createdBy: string
  createdAt: string
  updatedAt: string
  lastGeneratedAt?: string
  nextScheduledAt?: string
  templateId?: string
  parameters?: ReportParameters
  metadata?: Record<string, any>
}

/**
 * Report parameters
 */
export interface ReportParameters {
  dateRange?: {
    startDate: string
    endDate: string
  }
  domains?: string[]
  kpiCodes?: string[]
  includeCharts?: boolean
  includeTables?: boolean
  includeRecommendations?: boolean
  customFilters?: Record<string, any>
}

/**
 * Report template
 */
export interface ReportTemplate {
  id: string
  name: string
  description: string
  type: ReportType
  format: ReportFormat
  category: string
  isSystemTemplate: boolean
  createdBy?: string
  parameters?: ReportTemplateParameter[]
  sampleData?: Record<string, any>
  createdAt: string
  updatedAt: string
}

/**
 * Report template parameter definition
 */
export interface ReportTemplateParameter {
  name: string
  label: string
  type: 'text' | 'number' | 'date' | 'dateRange' | 'select' | 'multiSelect' | 'boolean'
  required: boolean
  defaultValue?: any
  options?: Array<{ label: string; value: any }>
  validation?: {
    min?: number
    max?: number
    pattern?: string
  }
}

/**
 * Generate report request
 */
export interface GenerateReportRequest {
  name: string
  description?: string
  type: ReportType
  format: ReportFormat
  templateId?: string
  parameters?: ReportParameters
  metadata?: Record<string, any>
}

/**
 * Report generation response
 */
export interface ReportGenerationResponse {
  reportId: string
  status: ReportStatus
  message: string
  estimatedCompletionTime?: string
  downloadUrl?: string
}

/**
 * Schedule report request
 */
export interface ScheduleReportRequest {
  reportId: string
  frequency: ReportFrequency
  schedule: ScheduleConfig
  recipients?: string[]
  parameters?: ReportParameters
}

/**
 * Schedule configuration
 */
export interface ScheduleConfig {
  cronExpression?: string
  timezone?: string
  runAt?: string // Time of day to run (HH:MM format)
  dayOfWeek?: number // 0-6 (Sunday-Saturday)
  dayOfMonth?: number // 1-31
}

/**
 * Scheduled report
 */
export interface ScheduledReport {
  id: string
  reportId: string
  reportName: string
  frequency: ReportFrequency
  schedule: ScheduleConfig
  recipients: string[]
  parameters?: ReportParameters
  isActive: boolean
  nextRunAt: string
  lastRunAt?: string
  createdBy: string
  createdAt: string
  updatedAt: string
}

/**
 * Report history
 */
export interface ReportHistory {
  reportId: string
  reportName: string
  generations: ReportHistoryItem[]
  total: number
}

/**
 * Report history item
 */
export interface ReportHistoryItem {
  id: string
  reportId: string
  status: ReportStatus
  generatedAt: string
  generatedBy: string
  completedAt?: string
  fileSize?: number
  downloadUrl?: string
  errorMessage?: string
  metadata?: Record<string, any>
}

/**
 * Update report request
 */
export interface UpdateReportRequest {
  name?: string
  description?: string
  parameters?: ReportParameters
  metadata?: Record<string, any>
}

// ============================================================================
// Service Class
// ============================================================================

class ReportService {
  private readonly basePath = '/api/v1/reports'

  /**
   * Get report library
   * Returns all available reports with pagination
   */
  async getReportLibrary(params?: {
    page?: number
    size?: number
    sortBy?: string
    sortDirection?: 'asc' | 'desc'
    type?: ReportType
    status?: ReportStatus
  }): Promise<PaginatedResponse<Report>> {
    const response = await executiveReportApiClient.get<PaginatedResponse<Report>>(
      `${this.basePath}/library`,
      { params }
    )
    return response.data
  }

  /**
   * Get specific report by ID
   */
  async getReportById(id: string): Promise<Report> {
    const response = await executiveReportApiClient.get<Report>(
      `${this.basePath}/reports/${id}`
    )
    return response.data
  }

  /**
   * Generate a new report
   */
  async generateReport(request: GenerateReportRequest): Promise<ReportGenerationResponse> {
    const response = await executiveReportApiClient.post<ReportGenerationResponse>(
      `${this.basePath}/generate`,
      request
    )
    return response.data
  }

  /**
   * Schedule a report for automatic generation
   */
  async scheduleReport(request: ScheduleReportRequest): Promise<ScheduledReport> {
    const response = await executiveReportApiClient.post<ScheduledReport>(
      `${this.basePath}/schedule`,
      request
    )
    return response.data
  }

  /**
   * Get all scheduled reports
   */
  async getScheduledReports(params?: {
    page?: number
    size?: number
    isActive?: boolean
  }): Promise<PaginatedResponse<ScheduledReport>> {
    const response = await executiveReportApiClient.get<PaginatedResponse<ScheduledReport>>(
      `${this.basePath}/scheduled`,
      { params }
    )
    return response.data
  }

  /**
   * Get scheduled report by ID
   */
  async getScheduledReportById(id: string): Promise<ScheduledReport> {
    const response = await executiveReportApiClient.get<ScheduledReport>(
      `${this.basePath}/scheduled/${id}`
    )
    return response.data
  }

  /**
   * Update a scheduled report
   */
  async updateScheduledReport(
    id: string,
    request: Partial<ScheduleReportRequest>
  ): Promise<ScheduledReport> {
    const response = await executiveReportApiClient.put<ScheduledReport>(
      `${this.basePath}/scheduled/${id}`,
      request
    )
    return response.data
  }

  /**
   * Pause/resume a scheduled report
   */
  async toggleScheduledReport(id: string, isActive: boolean): Promise<ScheduledReport> {
    const response = await executiveReportApiClient.patch<ScheduledReport>(
      `${this.basePath}/scheduled/${id}/toggle`,
      { isActive }
    )
    return response.data
  }

  /**
   * Delete a scheduled report
   */
  async deleteScheduledReport(id: string): Promise<void> {
    await executiveReportApiClient.delete(`${this.basePath}/scheduled/${id}`)
  }

  /**
   * Update report configuration
   */
  async updateReport(id: string, request: UpdateReportRequest): Promise<Report> {
    const response = await executiveReportApiClient.put<Report>(
      `${this.basePath}/reports/${id}`,
      request
    )
    return response.data
  }

  /**
   * Delete a report
   */
  async deleteReport(id: string): Promise<void> {
    await executiveReportApiClient.delete(`${this.basePath}/reports/${id}`)
  }

  /**
   * Archive a report
   */
  async archiveReport(id: string): Promise<Report> {
    const response = await executiveReportApiClient.patch<Report>(
      `${this.basePath}/reports/${id}/archive`
    )
    return response.data
  }

  /**
   * Get report templates
   */
  async getReportTemplates(params?: {
    type?: ReportType
    category?: string
  }): Promise<ReportTemplate[]> {
    const response = await executiveReportApiClient.get<ReportTemplate[]>(
      `${this.basePath}/templates`,
      { params }
    )
    return response.data
  }

  /**
   * Get report template by ID
   */
  async getReportTemplateById(id: string): Promise<ReportTemplate> {
    const response = await executiveReportApiClient.get<ReportTemplate>(
      `${this.basePath}/templates/${id}`
    )
    return response.data
  }

  /**
   * Create custom report template
   */
  async createReportTemplate(template: Omit<ReportTemplate, 'id' | 'createdAt' | 'updatedAt'>): Promise<ReportTemplate> {
    const response = await executiveReportApiClient.post<ReportTemplate>(
      `${this.basePath}/templates`,
      template
    )
    return response.data
  }

  /**
   * Update report template
   */
  async updateReportTemplate(
    id: string,
    template: Partial<Omit<ReportTemplate, 'id' | 'createdAt' | 'updatedAt'>>
  ): Promise<ReportTemplate> {
    const response = await executiveReportApiClient.put<ReportTemplate>(
      `${this.basePath}/templates/${id}`,
      template
    )
    return response.data
  }

  /**
   * Delete report template
   */
  async deleteReportTemplate(id: string): Promise<void> {
    await executiveReportApiClient.delete(`${this.basePath}/templates/${id}`)
  }

  /**
   * Get report generation history
   */
  async getReportHistory(reportId: string, params?: {
    page?: number
    size?: number
    startDate?: string
    endDate?: string
  }): Promise<ReportHistory> {
    const response = await executiveReportApiClient.get<ReportHistory>(
      `${this.basePath}/history/${reportId}`,
      { params }
    )
    return response.data
  }

  /**
   * Get report generation history item by ID
   */
  async getHistoryItemById(reportId: string, historyId: string): Promise<ReportHistoryItem> {
    const response = await executiveReportApiClient.get<ReportHistoryItem>(
      `${this.basePath}/history/${reportId}/${historyId}`
    )
    return response.data
  }

  /**
   * Download generated report
   */
  async downloadReport(reportId: string, historyId?: string): Promise<Blob> {
    const url = historyId
      ? `${this.basePath}/download/${reportId}/${historyId}`
      : `${this.basePath}/download/${reportId}`

    const response = await executiveReportApiClient.get(url, {
      responseType: 'blob',
    })
    return response.data
  }

  /**
   * Get download URL for a report
   */
  getDownloadUrl(reportId: string, historyId?: string): string {
    return historyId
      ? `${this.basePath}/download/${reportId}/${historyId}`
      : `${this.basePath}/download/${reportId}`
  }

  /**
   * Re-generate a report from history
   */
  async regenerateReport(reportId: string, historyId: string): Promise<ReportGenerationResponse> {
    const response = await executiveReportApiClient.post<ReportGenerationResponse>(
      `${this.basePath}/regenerate/${reportId}/${historyId}`
    )
    return response.data
  }

  /**
   * Get reports by type
   */
  async getReportsByType(type: ReportType): Promise<Report[]> {
    const response = await executiveReportApiClient.get<Report[]>(
      `${this.basePath}/by-type/${type}`
    )
    return response.data
  }

  /**
   * Search reports
   */
  async searchReports(query: string, params?: {
    page?: number
    size?: number
  }): Promise<PaginatedResponse<Report>> {
    const response = await executiveReportApiClient.get<PaginatedResponse<Report>>(
      `${this.basePath}/search`,
      { params: { q: query, ...params } }
    )
    return response.data
  }

  /**
   * Get report statistics
   */
  async getReportStatistics(): Promise<ReportStatistics> {
    const response = await executiveReportApiClient.get<ReportStatistics>(
      `${this.basePath}/statistics`
    )
    return response.data
  }

  /**
   * Health check
   */
  async healthCheck(): Promise<{ status: string; timestamp: string }> {
    const response = await executiveReportApiClient.get<{ status: string; timestamp: string }>(
      `${this.basePath}/health`
    )
    return response.data
  }

  // ========================================================================
  // Helper Methods
  // ========================================================================

  /**
   * Get status color
   */
  getStatusColor(status: ReportStatus): string {
    const colors: Record<ReportStatus, string> = {
      READY: '#4CAF50',
      GENERATING: '#2196F3',
      FAILED: '#F44336',
      SCHEDULED: '#FF9800',
      ARCHIVED: '#9E9E9E',
    }
    return colors[status] || '#9E9E9E'
  }

  /**
   * Get status label
   */
  getStatusLabel(status: ReportStatus): string {
    const labels: Record<ReportStatus, string> = {
      READY: 'Ready',
      GENERATING: 'Generating',
      FAILED: 'Failed',
      SCHEDULED: 'Scheduled',
      ARCHIVED: 'Archived',
    }
    return labels[status] || status
  }

  /**
   * Get format icon
   */
  getFormatIcon(format: ReportFormat): string {
    const icons: Record<ReportFormat, string> = {
      PDF: 'PDF',
      EXCEL: 'XLS',
      CSV: 'CSV',
      JSON: 'JSON',
      HTML: 'HTML',
    }
    return icons[format] || format
  }

  /**
   * Get frequency label
   */
  getFrequencyLabel(frequency: ReportFrequency): string {
    const labels: Record<ReportFrequency, string> = {
      DAILY: 'Daily',
      WEEKLY: 'Weekly',
      MONTHLY: 'Monthly',
      QUARTERLY: 'Quarterly',
      YEARLY: 'Yearly',
      ON_DEMAND: 'On Demand',
    }
    return labels[frequency] || frequency
  }

  /**
   * Format file size
   */
  formatFileSize(bytes: number): string {
    if (bytes === 0) return '0 Bytes'
    const k = 1024
    const sizes = ['Bytes', 'KB', 'MB', 'GB']
    const i = Math.floor(Math.log(bytes) / Math.log(k))
    return Math.round((bytes / Math.pow(k, i)) * 100) / 100 + ' ' + sizes[i]
  }

  /**
   * Check if report is downloadable
   */
  isDownloadable(report: Report): boolean {
    return report.status === 'READY' && !!report.lastGeneratedAt
  }

  /**
   * Check if report is being generated
   */
  isGenerating(report: Report): boolean {
    return report.status === 'GENERATING'
  }

  /**
   * Check if report has failed
   */
  hasFailed(report: Report): boolean {
    return report.status === 'FAILED'
  }
}

/**
 * Report statistics
 */
export interface ReportStatistics {
  totalReports: number
  activeReports: number
  scheduledReports: number
  totalGenerations: number
  successfulGenerations: number
  failedGenerations: number
  averageGenerationTime: number
  mostUsedTypes: Array<{ type: ReportType; count: number }>
  recentActivity: Array<{
    reportId: string
    reportName: string
    action: string
    timestamp: string
  }>
}

// Export singleton instance
export const reportService = new ReportService()

// Export type for use in components
export type { ReportService }
