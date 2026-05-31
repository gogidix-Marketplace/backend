/**
 * React Hook: useReports
 *
 * Provides report functionality from CEO Report Service
 * Handles loading, error states, and caching for report library,
 * generation, scheduling, templates, and history
 */

import { useState, useEffect, useCallback } from 'react'
import { reportService } from '../services/executive/report.service'
import type {
  Report,
  ReportType,
  ReportFormat,
  ReportStatus,
  ReportFrequency,
  ReportParameters,
  ReportTemplate,
  ScheduledReport,
  ReportHistory,
  ReportHistoryItem,
  GenerateReportRequest,
  ScheduleReportRequest,
  ReportStatistics,
  PaginatedResponse,
} from '../services/executive/report.service'

// ============================================================================
// Types
// ============================================================================

interface UseReportLibraryResult {
  reports: Report[]
  totalElements: number
  totalPages: number
  loading: boolean
  error: string | null
  refetch: () => Promise<void>
}

interface UseReportResult {
  report: Report | null
  loading: boolean
  error: string | null
  refetch: () => Promise<void>
}

interface UseGenerateReportResult {
  generateReport: (request: GenerateReportRequest) => Promise<ReportGenerationResponse | null>
  generating: boolean
  error: string | null
  reset: () => void
}

interface UseScheduledReportsResult {
  scheduledReports: ScheduledReport[]
  totalElements: number
  totalPages: number
  loading: boolean
  error: string | null
  refetch: () => Promise<void>
}

interface UseReportTemplatesResult {
  templates: ReportTemplate[]
  loading: boolean
  error: string | null
  refetch: () => Promise<void>
}

interface UseReportHistoryResult {
  history: ReportHistoryItem[]
  reportName: string
  total: number
  loading: boolean
  error: string | null
  refetch: () => Promise<void>
}

interface UseDownloadReportResult {
  downloadReport: (reportId: string, historyId?: string, filename?: string) => Promise<boolean>
  downloading: boolean
  error: string | null
  progress: number
}

interface UseReportStatisticsResult {
  statistics: ReportStatistics | null
  loading: boolean
  error: string | null
  refetch: () => Promise<void>
}

interface UseScheduleReportResult {
  scheduleReport: (request: ScheduleReportRequest) => Promise<ScheduledReport | null>
  scheduling: boolean
  error: string | null
  reset: () => void
}

interface UseUpdateReportResult {
  updateReport: (id: string, request: UpdateReportRequest) => Promise<Report | null>
  updating: boolean
  error: string | null
  reset: () => void
}

interface UseDeleteReportResult {
  deleteReport: (id: string) => Promise<boolean>
  deleting: boolean
  error: string | null
  reset: () => void
}

interface UseToggleScheduledReportResult {
  toggleScheduledReport: (id: string, isActive: boolean) => Promise<ScheduledReport | null>
  toggling: boolean
  error: string | null
  reset: () => void
}

interface UseReportSearchResult {
  results: Report[]
  totalCount: number
  loading: boolean
  error: string | null
  search: (query: string) => Promise<void>
  clearSearch: () => void
}

interface UseReportHealthResult {
  isHealthy: boolean | null
  loading: boolean
  lastChecked: Date | null
}

// Internal type for report generation response
interface ReportGenerationResponse {
  reportId: string
  status: ReportStatus
  message: string
  estimatedCompletionTime?: string
  downloadUrl?: string
}

// Internal type for update report request
interface UpdateReportRequest {
  name?: string
  description?: string
  parameters?: ReportParameters
  metadata?: Record<string, any>
}

// ============================================================================
// Hook: Report Library
// ============================================================================

export function useReportLibrary(params?: {
  page?: number
  size?: number
  sortBy?: string
  sortDirection?: 'asc' | 'desc'
  type?: ReportType
  status?: ReportStatus
}): UseReportLibraryResult {
  const [reports, setReports] = useState<Report[]>([])
  const [totalElements, setTotalElements] = useState(0)
  const [totalPages, setTotalPages] = useState(0)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  const fetchData = useCallback(async () => {
    setLoading(true)
    setError(null)
    try {
      const response = await reportService.getReportLibrary(params)
      setReports(response.content)
      setTotalElements(response.totalElements)
      setTotalPages(response.totalPages)
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to fetch report library')
      console.error('Error fetching report library:', err)
    } finally {
      setLoading(false)
    }
  }, [params])

  useEffect(() => {
    fetchData()
  }, [fetchData])

  return { reports, totalElements, totalPages, loading, error, refetch: fetchData }
}

// ============================================================================
// Hook: Single Report
// ============================================================================

export function useReport(reportId: string): UseReportResult {
  const [report, setReport] = useState<Report | null>(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  const fetchData = useCallback(async () => {
    if (!reportId) return

    setLoading(true)
    setError(null)
    try {
      const response = await reportService.getReportById(reportId)
      setReport(response)
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to fetch report')
      console.error('Error fetching report:', err)
    } finally {
      setLoading(false)
    }
  }, [reportId])

  useEffect(() => {
    fetchData()
  }, [fetchData])

  return { report, loading, error, refetch: fetchData }
}

// ============================================================================
// Hook: Generate Report
// ============================================================================

export function useGenerateReport(): UseGenerateReportResult {
  const [generating, setGenerating] = useState(false)
  const [error, setError] = useState<string | null>(null)

  const generateReport = useCallback(async (request: GenerateReportRequest) => {
    setGenerating(true)
    setError(null)

    try {
      const response = await reportService.generateReport(request)
      return response
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to generate report')
      return null
    } finally {
      setGenerating(false)
    }
  }, [])

  const reset = useCallback(() => {
    setError(null)
  }, [])

  return { generateReport, generating, error, reset }
}

// ============================================================================
// Hook: Scheduled Reports
// ============================================================================

export function useScheduledReports(params?: {
  page?: number
  size?: number
  isActive?: boolean
}): UseScheduledReportsResult {
  const [scheduledReports, setScheduledReports] = useState<ScheduledReport[]>([])
  const [totalElements, setTotalElements] = useState(0)
  const [totalPages, setTotalPages] = useState(0)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  const fetchData = useCallback(async () => {
    setLoading(true)
    setError(null)
    try {
      const response = await reportService.getScheduledReports(params)
      setScheduledReports(response.content)
      setTotalElements(response.totalElements)
      setTotalPages(response.totalPages)
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to fetch scheduled reports')
      console.error('Error fetching scheduled reports:', err)
    } finally {
      setLoading(false)
    }
  }, [params])

  useEffect(() => {
    fetchData()
  }, [fetchData])

  return { scheduledReports, totalElements, totalPages, loading, error, refetch: fetchData }
}

// ============================================================================
// Hook: Single Scheduled Report
// ============================================================================

export function useScheduledReport(scheduledReportId: string): UseReportResult {
  const [report, setReport] = useState<Report | null>(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  const fetchData = useCallback(async () => {
    if (!scheduledReportId) return

    setLoading(true)
    setError(null)
    try {
      const response = await reportService.getScheduledReportById(scheduledReportId)
      // Convert ScheduledReport to Report-like structure for compatibility
      setReport({
        id: response.id,
        name: response.reportName,
        description: `Scheduled ${response.frequency.toLowerCase()} report`,
        type: 'CUSTOM' as ReportType,
        format: 'PDF' as ReportFormat,
        status: response.isActive ? 'SCHEDULED' : 'ARCHIVED',
        createdBy: response.createdBy,
        createdAt: response.createdAt,
        updatedAt: response.updatedAt,
        nextScheduledAt: response.nextRunAt,
        lastGeneratedAt: response.lastRunAt,
        parameters: response.parameters,
      } as Report)
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to fetch scheduled report')
      console.error('Error fetching scheduled report:', err)
    } finally {
      setLoading(false)
    }
  }, [scheduledReportId])

  useEffect(() => {
    fetchData()
  }, [fetchData])

  return { report, loading, error, refetch: fetchData }
}

// ============================================================================
// Hook: Report Templates
// ============================================================================

export function useReportTemplates(params?: {
  type?: ReportType
  category?: string
}): UseReportTemplatesResult {
  const [templates, setTemplates] = useState<ReportTemplate[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  const fetchData = useCallback(async () => {
    setLoading(true)
    setError(null)
    try {
      const response = await reportService.getReportTemplates(params)
      setTemplates(response)
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to fetch report templates')
      console.error('Error fetching report templates:', err)
    } finally {
      setLoading(false)
    }
  }, [params])

  useEffect(() => {
    fetchData()
  }, [fetchData])

  return { templates, loading, error, refetch: fetchData }
}

// ============================================================================
// Hook: Report History
// ============================================================================

export function useReportHistory(reportId: string, params?: {
  page?: number
  size?: number
  startDate?: string
  endDate?: string
}): UseReportHistoryResult {
  const [history, setHistory] = useState<ReportHistoryItem[]>([])
  const [reportName, setReportName] = useState('')
  const [total, setTotal] = useState(0)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  const fetchData = useCallback(async () => {
    if (!reportId) return

    setLoading(true)
    setError(null)
    try {
      const response = await reportService.getReportHistory(reportId, params)
      setHistory(response.generations)
      setReportName(response.reportName)
      setTotal(response.total)
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to fetch report history')
      console.error('Error fetching report history:', err)
    } finally {
      setLoading(false)
    }
  }, [reportId, params])

  useEffect(() => {
    fetchData()
  }, [fetchData])

  return { history, reportName, total, loading, error, refetch: fetchData }
}

// ============================================================================
// Hook: Download Report
// ============================================================================

export function useDownloadReport(): UseDownloadReportResult {
  const [downloading, setDownloading] = useState(false)
  const [error, setError] = useState<string | null>(null)
  const [progress, setProgress] = useState(0)

  const downloadReport = useCallback(async (
    reportId: string,
    historyId?: string,
    filename?: string
  ) => {
    setDownloading(true)
    setError(null)
    setProgress(0)

    try {
      // Simulate progress
      const progressInterval = setInterval(() => {
        setProgress((prev) => Math.min(prev + 10, 90))
      }, 200)

      const blob = await reportService.downloadReport(reportId, historyId)

      clearInterval(progressInterval)
      setProgress(100)

      // Determine file extension from content type
      let extension = 'pdf'
      const contentType = blob.type
      if (contentType.includes('excel') || contentType.includes('spreadsheet')) {
        extension = 'xlsx'
      } else if (contentType.includes('csv')) {
        extension = 'csv'
      } else if (contentType.includes('json')) {
        extension = 'json'
      } else if (contentType.includes('html')) {
        extension = 'html'
      }

      // Create download link
      const url = window.URL.createObjectURL(blob)
      const link = document.createElement('a')
      link.href = url
      link.download = filename || `report-${reportId}.${extension}`
      document.body.appendChild(link)
      link.click()
      document.body.removeChild(link)
      window.URL.revokeObjectURL(url)

      // Reset progress after a delay
      setTimeout(() => setProgress(0), 1000)

      return true
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to download report')
      setProgress(0)
      return false
    } finally {
      setDownloading(false)
    }
  }, [])

  return { downloadReport, downloading, error, progress }
}

// ============================================================================
// Hook: Report Statistics
// ============================================================================

export function useReportStatistics(refetchInterval?: number): UseReportStatisticsResult {
  const [statistics, setStatistics] = useState<ReportStatistics | null>(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  const fetchData = useCallback(async () => {
    setLoading(true)
    setError(null)
    try {
      const response = await reportService.getReportStatistics()
      setStatistics(response)
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to fetch report statistics')
      console.error('Error fetching report statistics:', err)
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

  return { statistics, loading, error, refetch: fetchData }
}

// ============================================================================
// Hook: Schedule Report
// ============================================================================

export function useScheduleReport(): UseScheduleReportResult {
  const [scheduling, setScheduling] = useState(false)
  const [error, setError] = useState<string | null>(null)

  const scheduleReport = useCallback(async (request: ScheduleReportRequest) => {
    setScheduling(true)
    setError(null)

    try {
      const response = await reportService.scheduleReport(request)
      return response
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to schedule report')
      return null
    } finally {
      setScheduling(false)
    }
  }, [])

  const reset = useCallback(() => {
    setError(null)
  }, [])

  return { scheduleReport, scheduling, error, reset }
}

// ============================================================================
// Hook: Update Report
// ============================================================================

export function useUpdateReport(): UseUpdateReportResult {
  const [updating, setUpdating] = useState(false)
  const [error, setError] = useState<string | null>(null)

  const updateReport = useCallback(async (id: string, request: UpdateReportRequest) => {
    setUpdating(true)
    setError(null)

    try {
      const response = await reportService.updateReport(id, request)
      return response
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to update report')
      return null
    } finally {
      setUpdating(false)
    }
  }, [])

  const reset = useCallback(() => {
    setError(null)
  }, [])

  return { updateReport, updating, error, reset }
}

// ============================================================================
// Hook: Delete Report
// ============================================================================

export function useDeleteReport(): UseDeleteReportResult {
  const [deleting, setDeleting] = useState(false)
  const [error, setError] = useState<string | null>(null)

  const deleteReport = useCallback(async (id: string) => {
    setDeleting(true)
    setError(null)

    try {
      await reportService.deleteReport(id)
      return true
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to delete report')
      return false
    } finally {
      setDeleting(false)
    }
  }, [])

  const reset = useCallback(() => {
    setError(null)
  }, [])

  return { deleteReport, deleting, error, reset }
}

// ============================================================================
// Hook: Archive Report
// ============================================================================

export function useArchiveReport(): UseUpdateReportResult {
  const [updating, setUpdating] = useState(false)
  const [error, setError] = useState<string | null>(null)

  const updateReport = useCallback(async (id: string) => {
    setUpdating(true)
    setError(null)

    try {
      const response = await reportService.archiveReport(id)
      return response
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to archive report')
      return null
    } finally {
      setUpdating(false)
    }
  }, [])

  const reset = useCallback(() => {
    setError(null)
  }, [])

  return { updateReport, updating, error, reset }
}

// ============================================================================
// Hook: Toggle Scheduled Report
// ============================================================================

export function useToggleScheduledReport(): UseToggleScheduledReportResult {
  const [toggling, setToggling] = useState(false)
  const [error, setError] = useState<string | null>(null)

  const toggleScheduledReport = useCallback(async (id: string, isActive: boolean) => {
    setToggling(true)
    setError(null)

    try {
      const response = await reportService.toggleScheduledReport(id, isActive)
      return response
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to toggle scheduled report')
      return null
    } finally {
      setToggling(false)
    }
  }, [])

  const reset = useCallback(() => {
    setError(null)
  }, [])

  return { toggleScheduledReport, toggling, error, reset }
}

// ============================================================================
// Hook: Report Search
// ============================================================================

export function useReportSearch(initialParams?: {
  page?: number
  size?: number
}): UseReportSearchResult {
  const [results, setResults] = useState<Report[]>([])
  const [totalCount, setTotalCount] = useState(0)
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState<string | null>(null)

  const search = useCallback(async (query: string) => {
    if (!query || query.length < 2) {
      setResults([])
      setTotalCount(0)
      return
    }

    setLoading(true)
    setError(null)

    try {
      const response = await reportService.searchReports(query, initialParams)
      setResults(response.content)
      setTotalCount(response.totalElements)
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Search failed')
      setResults([])
      setTotalCount(0)
    } finally {
      setLoading(false)
    }
  }, [initialParams])

  const clearSearch = useCallback(() => {
    setResults([])
    setTotalCount(0)
    setError(null)
  }, [])

  return { results, totalCount, loading, error, search, clearSearch }
}

// ============================================================================
// Hook: Reports by Type
// ============================================================================

export function useReportsByType(type: ReportType): UseReportTemplatesResult {
  const [templates, setTemplates] = useState<Report[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  const fetchData = useCallback(async () => {
    if (!type) return

    setLoading(true)
    setError(null)
    try {
      const response = await reportService.getReportsByType(type)
      setTemplates(response as unknown as ReportTemplate[])
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to fetch reports by type')
      console.error('Error fetching reports by type:', err)
    } finally {
      setLoading(false)
    }
  }, [type])

  useEffect(() => {
    fetchData()
  }, [fetchData])

  return { templates, loading, error, refetch: fetchData }
}

// ============================================================================
// Hook: Report Health Check
// ============================================================================

export function useReportHealth(refetchInterval = 30000): UseReportHealthResult {
  const [isHealthy, setIsHealthy] = useState<boolean | null>(null)
  const [loading, setLoading] = useState(true)
  const [lastChecked, setLastChecked] = useState<Date | null>(null)

  useEffect(() => {
    const checkHealth = async () => {
      setLoading(true)
      try {
        await reportService.healthCheck()
        setIsHealthy(true)
      } catch {
        setIsHealthy(false)
      } finally {
        setLoading(false)
        setLastChecked(new Date())
      }
    }

    checkHealth()

    if (refetchInterval > 0) {
      const interval = setInterval(checkHealth, refetchInterval)
      return () => clearInterval(interval)
    }
  }, [refetchInterval])

  return { isHealthy, loading, lastChecked }
}

// ============================================================================
// Hook: Report Template by ID
// ============================================================================

export function useReportTemplate(templateId: string): UseReportResult {
  const [report, setReport] = useState<Report | null>(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  const fetchData = useCallback(async () => {
    if (!templateId) return

    setLoading(true)
    setError(null)
    try {
      const template = await reportService.getReportTemplateById(templateId)
      setReport(template as unknown as Report)
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to fetch report template')
      console.error('Error fetching report template:', err)
    } finally {
      setLoading(false)
    }
  }, [templateId])

  useEffect(() => {
    fetchData()
  }, [fetchData])

  return { report, loading, error, refetch: fetchData }
}

// ============================================================================
// Hook: Create Report Template
// ============================================================================

export function useCreateReportTemplate(): UseGenerateReportResult {
  const [generating, setGenerating] = useState(false)
  const [error, setError] = useState<string | null>(null)

  const generateReport = useCallback(async (template: Omit<ReportTemplate, 'id' | 'createdAt' | 'updatedAt'>) => {
    setGenerating(true)
    setError(null)

    try {
      const response = await reportService.createReportTemplate(template)
      return {
        reportId: response.id,
        status: 'READY' as ReportStatus,
        message: 'Template created successfully',
      } as ReportGenerationResponse
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to create report template')
      return null
    } finally {
      setGenerating(false)
    }
  }, [])

  const reset = useCallback(() => {
    setError(null)
  }, [])

  return { generateReport, generating, error, reset }
}

// ============================================================================
// Hook: Update Report Template
// ============================================================================

export function useUpdateReportTemplate(): UseUpdateReportResult {
  const [updating, setUpdating] = useState(false)
  const [error, setError] = useState<string | null>(null)

  const updateReport = useCallback(async (id: string, template: Partial<Omit<ReportTemplate, 'id' | 'createdAt' | 'updatedAt'>>) => {
    setUpdating(true)
    setError(null)

    try {
      const response = await reportService.updateReportTemplate(id, template)
      return response as unknown as Report
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to update report template')
      return null
    } finally {
      setUpdating(false)
    }
  }, [])

  const reset = useCallback(() => {
    setError(null)
  }, [])

  return { updateReport, updating, error, reset }
}

// ============================================================================
// Hook: Delete Report Template
// ============================================================================

export function useDeleteReportTemplate(): UseDeleteReportResult {
  const [deleting, setDeleting] = useState(false)
  const [error, setError] = useState<string | null>(null)

  const deleteReport = useCallback(async (id: string) => {
    setDeleting(true)
    setError(null)

    try {
      await reportService.deleteReportTemplate(id)
      return true
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to delete report template')
      return false
    } finally {
      setDeleting(false)
    }
  }, [])

  const reset = useCallback(() => {
    setError(null)
  }, [])

  return { deleteReport, deleting, error, reset }
}

// ============================================================================
// Hook: Regenerate Report
// ============================================================================

export function useRegenerateReport(): UseGenerateReportResult {
  const [generating, setGenerating] = useState(false)
  const [error, setError] = useState<string | null>(null)

  const generateReport = useCallback(async (reportId: string, historyId: string) => {
    setGenerating(true)
    setError(null)

    try {
      const response = await reportService.regenerateReport(reportId, historyId)
      return response
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to regenerate report')
      return null
    } finally {
      setGenerating(false)
    }
  }, [])

  const reset = useCallback(() => {
    setError(null)
  }, [])

  return { generateReport, generating, error, reset }
}

// ============================================================================
// Export all types for use in components
// ============================================================================

export type {
  Report,
  ReportType,
  ReportFormat,
  ReportStatus,
  ReportFrequency,
  ReportParameters,
  ReportTemplate,
  ReportTemplateParameter,
  ScheduledReport,
  ReportHistory,
  ReportHistoryItem,
  GenerateReportRequest,
  ScheduleReportRequest,
  ReportStatistics,
  ScheduleConfig,
  UpdateReportRequest,
  PaginatedResponse,
}

export type {
  UseReportLibraryResult,
  UseReportResult,
  UseGenerateReportResult,
  UseScheduledReportsResult,
  UseReportTemplatesResult,
  UseReportHistoryResult,
  UseDownloadReportResult,
  UseReportStatisticsResult,
  UseScheduleReportResult,
  UseUpdateReportResult,
  UseDeleteReportResult,
  UseToggleScheduledReportResult,
  UseReportSearchResult,
  UseReportHealthResult,
}

// Re-export helper methods from service for convenience
export const reportHelpers = {
  getStatusColor: reportService.getStatusColor.bind(reportService),
  getStatusLabel: reportService.getStatusLabel.bind(reportService),
  getFormatIcon: reportService.getFormatIcon.bind(reportService),
  getFrequencyLabel: reportService.getFrequencyLabel.bind(reportService),
  formatFileSize: reportService.formatFileSize.bind(reportService),
  isDownloadable: reportService.isDownloadable.bind(reportService),
  isGenerating: reportService.isGenerating.bind(reportService),
  hasFailed: reportService.hasFailed.bind(reportService),
  getDownloadUrl: reportService.getDownloadUrl.bind(reportService),
}
