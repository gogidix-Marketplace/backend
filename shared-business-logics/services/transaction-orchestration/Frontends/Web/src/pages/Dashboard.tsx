import { useQuery } from '@tanstack/react-query'
import { Activity, AlertTriangle, CheckCircle, Clock, AlertCircle } from 'lucide-react'
import { format } from 'date-fns'
import { apiClient, type Alert } from '@/lib/api'

export default function Dashboard() {
  const { data: dashboard, isLoading: isLoadingStats } = useQuery({
    queryKey: ['dashboard'],
    queryFn: () => apiClient.getDashboardStats(),
    refetchInterval: 30000, // Refresh every 30 seconds
  })

  const { data: alerts, isLoading: isLoadingAlerts } = useQuery<Alert[]>({
    queryKey: ['open-alerts'],
    queryFn: () => apiClient.getOpenAlerts(),
    refetchInterval: 30000,
  })

  const { data: criticalAlerts } = useQuery<Alert[]>({
    queryKey: ['critical-alerts'],
    queryFn: () => apiClient.getCriticalAlerts(),
    refetchInterval: 30000,
  })

  if (isLoadingStats || isLoadingAlerts) {
    return (
      <div className="flex items-center justify-center h-64">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-primary-600"></div>
      </div>
    )
  }

  const stats = [
    {
      name: 'Total Transactions',
      value: dashboard?.totalTransactions || 0,
      icon: Activity,
      color: 'bg-blue-500',
    },
    {
      name: 'Active Onboardings',
      value: dashboard?.activeOnboardings || 0,
      icon: Clock,
      color: 'bg-yellow-500',
    },
    {
      name: 'Open Alerts',
      value: dashboard?.openAlerts || 0,
      icon: AlertTriangle,
      color: 'bg-red-500',
    },
    {
      name: 'Completed Today',
      value: dashboard?.completedToday || 0,
      icon: CheckCircle,
      color: 'bg-green-500',
    },
  ]

  const getSeverityColor = (severity: Alert['severity']) => {
    switch (severity) {
      case 'CRITICAL':
        return 'bg-red-100 text-red-800 border-red-200'
      case 'ERROR':
        return 'bg-red-50 text-red-700 border-red-100'
      case 'WARNING':
        return 'bg-yellow-100 text-yellow-800 border-yellow-200'
      case 'INFO':
      default:
        return 'bg-blue-100 text-blue-800 border-blue-200'
    }
  }

  return (
    <div>
      <h1 className="text-3xl font-bold text-gray-900">Dashboard</h1>
      <p className="mt-2 text-gray-600">
        Welcome to the Transaction Orchestration Dashboard - {format(new Date(), 'PPpp')}
      </p>

      <div className="grid grid-cols-1 gap-6 mt-8 sm:grid-cols-2 lg:grid-cols-4">
        {stats.map((stat) => (
          <div key={stat.name} className="card">
            <div className="flex items-center">
              <div className={`p-3 rounded-lg ${stat.color}`}>
                <stat.icon className="w-6 h-6 text-white" />
              </div>
              <div className="ml-4">
                <p className="text-sm font-medium text-gray-600">{stat.name}</p>
                <p className="text-2xl font-bold text-gray-900">{stat.value}</p>
              </div>
            </div>
          </div>
        ))}
      </div>

      <div className="grid grid-cols-1 gap-6 mt-8 lg:grid-cols-2">
        {/* Critical Alerts */}
        <div className="card">
          <h2 className="text-lg font-semibold mb-4 flex items-center gap-2">
            <AlertCircle className="w-5 h-5 text-red-500" />
            Critical Alerts
          </h2>
          <div className="space-y-3">
            {criticalAlerts && criticalAlerts.length > 0 ? (
              criticalAlerts.slice(0, 5).map((alert) => (
                <div key={alert.id} className={`p-3 rounded border ${getSeverityColor(alert.severity)}`}>
                  <div className="flex items-center justify-between mb-1">
                    <p className="font-medium">{alert.title}</p>
                    <span className="text-xs text-gray-500">
                      {format(new Date(alert.createdAt), 'HH:mm')}
                    </span>
                  </div>
                  {alert.description && (
                    <p className="text-sm text-gray-700">{alert.description}</p>
                  )}
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

        {/* Recent Alerts */}
        <div className="card">
          <h2 className="text-lg font-semibold mb-4">Recent Alerts</h2>
          <div className="space-y-3">
            {alerts && alerts.length > 0 ? (
              alerts.slice(0, 5).map((alert) => (
                <div key={alert.id} className={`p-3 rounded border ${getSeverityColor(alert.severity)}`}>
                  <div className="flex items-center justify-between mb-1">
                    <p className="font-medium">{alert.title}</p>
                    <span className="text-xs text-gray-500">
                      {format(new Date(alert.createdAt), 'HH:mm')}
                    </span>
                  </div>
                  {alert.description && (
                    <p className="text-sm text-gray-700">{alert.description}</p>
                  )}
                </div>
              ))
            ) : (
              <div className="text-center py-8 text-gray-500">
                <CheckCircle className="w-12 h-12 mx-auto mb-2 text-green-500" />
                <p>No recent alerts</p>
              </div>
            )}
          </div>
        </div>
      </div>
    </div>
  )
}
