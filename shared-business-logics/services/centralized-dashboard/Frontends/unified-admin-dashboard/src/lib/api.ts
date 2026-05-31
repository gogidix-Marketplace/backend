import axios from 'axios'

const api = axios.create({
  baseURL: '/api/v1',
  headers: {
    'Content-Type': 'application/json',
    'X-Tenant-ID': 'default',
  },
})

export interface DashboardData {
  sagaStatistics?: Record<string, number>
  chartData?: Record<string, unknown>
  monitoringData?: Record<string, unknown>
  onboardingData?: Record<string, unknown>
  auditData?: Record<string, unknown>
  serviceHealth?: Record<string, ServiceHealth>
  timestamp: string
  tenantId: string
}

export interface ServiceHealth {
  serviceName: string
  status: string
  baseUrl: string
  lastChecked: string
  responseTimeMs: number
  details?: Record<string, unknown>
}

export interface ChartData {
  chartId: string
  chartName: string
  chartType: string
  data: DataPoint[]
  metadata?: Record<string, unknown>
  generatedAt: string
  tenantId: string
}

export interface DataPoint {
  label?: string
  timestamp: string
  value: number
  metadata?: Record<string, unknown>
}

export interface ChartSummary {
  totalCharts: number
  activeCharts: number
  totalDataPoints: number
  chartsByType: Record<string, number>
  timestamp: string
}

export interface WebSocketStats {
  totalConnections: number
  tenants: number
  topics: number
  timestamp: string
}

export const gatewayApi = {
  getDashboardData: async (): Promise<DashboardData> => {
    const response = await api.get<DashboardData>('/gateway/dashboard')
    return response.data
  },

  getServiceHealth: async (): Promise<Record<string, ServiceHealth>> => {
    const response = await api.get<Record<string, ServiceHealth>>('/gateway/health/services')
    return response.data
  },

  getSagaStatistics: async (): Promise<Record<string, number>> => {
    const response = await api.get<Record<string, number>>('/gateway/sagas/statistics')
    return response.data
  },
}

export const chartApi = {
  getCharts: async (): Promise<ChartSummary> => {
    const response = await api.get<ChartSummary>('/charts/summary')
    return response.data
  },

  getChartData: async (chartId: string, startTime?: string, endTime?: string): Promise<ChartData> => {
    const params = new URLSearchParams()
    if (startTime) params.append('startTime', startTime)
    if (endTime) params.append('endTime', endTime)
    const response = await api.get<ChartData>(`/charts/${chartId}/data?${params}`)
    return response.data
  },

  getMetrics: async (): Promise<Record<string, unknown>> => {
    const response = await api.get<Record<string, unknown>>('/charts/metrics')
    return response.data
  },
}

export const websocketApi = {
  getStatistics: async (): Promise<WebSocketStats> => {
    const response = await api.get<WebSocketStats>('/websocket/statistics')
    return response.data
  },

  broadcast: async (message: Record<string, unknown>, tenantId = 'all'): Promise<Record<string, string>> => {
    const response = await api.post<Record<string, string>>('/websocket/broadcast', message, {
      params: { tenantId },
    })
    return response.data
  },
}

export default api
