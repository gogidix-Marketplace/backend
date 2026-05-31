import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query'
import { Activity, CheckCircle, XCircle, Clock, AlertCircle } from 'lucide-react'
import { format } from 'date-fns'
import { apiClient, type Alert, type Metric } from '@/lib/api'
import { useState } from 'react'

export default function Monitoring() {
  const queryClient = useQueryClient()
  const [selectedTransactionId, setSelectedTransactionId] = useState<string | null>(null)

  const { data: dashboard, isLoading: isLoadingDashboard } = useQuery({
    queryKey: ['monitoring-dashboard'],
    queryFn: () => apiClient.getDashboardMetrics(),
    refetchInterval: 30000,
  })

  const { data: openAlerts, isLoading: isLoadingAlerts } = useQuery<Alert[]>({
    queryKey: ['open-alerts'],
    queryFn: () => apiClient.getOpenAlerts(),
    refetchInterval: 30000,
  })

  const { data: criticalAlerts } = useQuery<Alert[]>({
    queryKey: ['critical-alerts'],
    queryFn: () => apiClient.getCriticalAlerts(),
    refetchInterval: 30000,
  })

  const { data: metrics, isLoading: isLoadingMetrics } = useQuery<Metric[]>({
    queryKey: ['metrics', selectedTransactionId],
    queryFn: () => apiClient.getTransactionMetrics(selectedTransactionId || ''),
    enabled: !!selectedTransactionId,
    refetchInterval: 15000,
  })

  const acknowledgeMutation = useMutation({
    mutationFn: ({ alertId, acknowledgedBy }: { alertId: string; acknowledgedBy: string }) =>
      apiClient.acknowledgeAlert(alertId, acknowledgedBy),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['open-alerts'] })
      queryClient.invalidateQueries({ queryKey: ['critical-alerts'] })
      queryClient.invalidateQueries({ queryKey: ['monitoring-dashboard'] })
    },
  })

  const resolveMutation = useMutation({
    mutationFn: ({ alertId, resolvedBy, notes }: { alertId: string; resolvedBy: string; notes?: string }) =>
      apiClient.resolveAlert(alertId, resolvedBy, notes),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['open-alerts'] })
      queryClient.invalidateQueries({ queryKey: ['critical-alerts'] })
      queryClient.invalidateQueries({ queryKey: ['monitoring-dashboard'] })
    },
  })

  const getSeverityColor = (severity: Alert['severity'] | Metric['severity']) => {
    switch (severity) {
      case 'CRITICAL':
        return 'text-red-600 bg-red-50 border-red-200'
      case 'WARNING':
        return 'text-yellow-600 bg-yellow-50 border-yellow-200'
      case 'ERROR':
        return 'text-red-500 bg-red-50 border-red-100'
      case 'NORMAL':
      default:
        return 'text-green-600 bg-green-50 border-green-200'
    }
  }

  const services = [
    { name: 'Audit Trail Service', port: 8081, path: '/audit/health' },
    { name: 'Onboarding Tracker', port: 8082, path: '/onboarding/health' },
    { name: 'Progress Step Service', port: 8083, path: '/progress-steps/health' },
    { name: 'Status Broadcast', port: 8084, path: '/status-broadcast/health' },
    { name: 'Transaction Monitoring', port: 8085, path: '/monitoring/health' },
  ]

  return (
    <div>
      <h1 className="text-3xl font-bold text-gray-900">System Monitoring</h1>
      <p className="mt-2 text-gray-600">Real-time metrics and alerts</p>

      {/* Key Metrics */}
      <div className="grid grid-cols-1 gap-6 mt-8 lg:grid-cols-4">
        <div className="card">
          <h3 className="text-sm font-medium text-gray-600">Open Alerts</h3>
          <p className="mt-2 text-2xl font-bold text-gray-900">{dashboard?.openAlerts || 0}</p>
          <span className="inline-block mt-2 px-2 py-1 text-xs font-medium rounded bg-yellow-100 text-yellow-800">
            {dashboard?.openAlerts === 0 ? 'No alerts' : 'Attention needed'}
          </span>
        </div>
        <div className="card">
          <h3 className="text-sm font-medium text-gray-600">Critical Alerts</h3>
          <p className="mt-2 text-2xl font-bold text-red-600">{dashboard?.criticalAlerts || 0}</p>
          <span className={`inline-block mt-2 px-2 py-1 text-xs font-medium rounded ${
            (dashboard?.criticalAlerts || 0) > 0 ? 'bg-red-100 text-red-800' : 'bg-green-100 text-green-800'
          }`}>
            {(dashboard?.criticalAlerts || 0) > 0 ? 'Action required' : 'All good'}
          </span>
        </div>
        <div className="card">
          <h3 className="text-sm font-medium text-gray-600">Warning Alerts</h3>
          <p className="mt-2 text-2xl font-bold text-yellow-600">{dashboard?.warningAlerts || 0}</p>
          <span className="inline-block mt-2 px-2 py-1 text-xs font-medium rounded bg-yellow-100 text-yellow-800">
            Monitor closely
          </span>
        </div>
        <div className="card">
          <h3 className="text-sm font-medium text-gray-600">Recent Alerts (24h)</h3>
          <p className="mt-2 text-2xl font-bold text-blue-600">{dashboard?.recentAlerts || 0}</p>
          <span className="inline-block mt-2 px-2 py-1 text-xs font-medium rounded bg-blue-100 text-blue-800">
            Last 24 hours
          </span>
        </div>
      </div>

      <div className="grid grid-cols-1 gap-6 mt-8 lg:grid-cols-2">
        {/* Critical Alerts */}
        <div className="card">
          <h2 className="text-lg font-semibold mb-4 flex items-center gap-2">
            <AlertCircle className="w-5 h-5 text-red-500" />
            Critical Alerts
          </h2>
          <div className="space-y-3 max-h-96 overflow-y-auto">
            {isLoadingAlerts ? (
              <div className="flex items-center justify-center h-32">
                <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-primary-600"></div>
              </div>
            ) : criticalAlerts && criticalAlerts.length > 0 ? (
              criticalAlerts.map((alert) => (
                <div key={alert.id} className={`p-4 rounded border ${getSeverityColor(alert.severity)}`}>
                  <div className="flex items-center justify-between mb-2">
                    <span className="font-medium">{alert.title}</span>
                    <span className="text-xs text-gray-500">
                      {format(new Date(alert.createdAt), 'PPp')}
                    </span>
                  </div>
                  {alert.description && (
                    <p className="text-sm text-gray-700 mb-3">{alert.description}</p>
                  )}
                  <div className="flex gap-2">
                    <button
                      onClick={() => acknowledgeMutation.mutate({ alertId: alert.id, acknowledgedBy: 'admin' })}
                      className="px-3 py-1 text-xs font-medium bg-white border border-gray-300 rounded hover:bg-gray-50"
                    >
                      Acknowledge
                    </button>
                    <button
                      onClick={() => resolveMutation.mutate({ alertId: alert.id, resolvedBy: 'admin' })}
                      className="px-3 py-1 text-xs font-medium bg-green-600 text-white rounded hover:bg-green-700"
                    >
                      Resolve
                    </button>
                  </div>
                </div>
              ))
            ) : (
              <div className="text-center py-8 text-gray-500">
                <CheckCircle className="w-12 h-12 mx-auto mb-2 text-green-500" />
                <p>No critical alerts</p>
              </div>
            )}
          </div>
        </div>

        {/* All Open Alerts */}
        <div className="card">
          <h2 className="text-lg font-semibold mb-4">All Open Alerts</h2>
          <div className="space-y-3 max-h-96 overflow-y-auto">
            {isLoadingAlerts ? (
              <div className="flex items-center justify-center h-32">
                <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-primary-600"></div>
              </div>
            ) : openAlerts && openAlerts.length > 0 ? (
              openAlerts.map((alert) => (
                <div key={alert.id} className={`p-3 rounded border ${getSeverityColor(alert.severity)}`}>
                  <div className="flex items-center justify-between mb-1">
                    <span className="font-medium text-sm">{alert.title}</span>
                    <span className="text-xs text-gray-500">
                      {format(new Date(alert.createdAt), 'HH:mm')}
                    </span>
                  </div>
                  {alert.description && (
                    <p className="text-xs text-gray-700">{alert.description}</p>
                  )}
                </div>
              ))
            ) : (
              <div className="text-center py-8 text-gray-500">
                <CheckCircle className="w-12 h-12 mx-auto mb-2 text-green-500" />
                <p>No open alerts</p>
              </div>
            )}
          </div>
        </div>

        {/* Service Health */}
        <div className="card lg:col-span-2">
          <h2 className="text-lg font-semibold mb-4">Service Health</h2>
          <div className="grid grid-cols-1 gap-3 sm:grid-cols-2 lg:grid-cols-5">
            {services.map((service) => (
              <a
                key={service.name}
                href={`http://localhost:${service.port}${service.path}`}
                target="_blank"
                rel="noopener noreferrer"
                className="block p-4 bg-gray-50 rounded hover:bg-gray-100 transition-colors"
              >
                <div className="flex items-center justify-between">
                  <div>
                    <p className="text-sm font-medium">{service.name}</p>
                    <p className="text-xs text-gray-500">Port: {service.port}</p>
                  </div>
                  <span className="flex items-center gap-1 text-xs font-medium text-green-600">
                    <span className="w-2 h-2 rounded-full bg-green-600" />
                    Healthy
                  </span>
                </div>
              </a>
            ))}
          </div>
        </div>
      </div>
    </div>
  )
}
