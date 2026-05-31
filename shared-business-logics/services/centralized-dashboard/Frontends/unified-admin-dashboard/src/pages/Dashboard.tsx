import React from 'react'
import { useQuery } from '@tanstack/react-query'
import { Activity, TrendingUp, Users, Database } from 'lucide-react'
import StatCard from '@/components/StatCard'
import ServiceHealthCard from '@/components/ServiceHealthCard'
import { gatewayApi } from '@/lib/api'
import wsService from '@/lib/websocket'

export default function Dashboard() {
  const { data: dashboardData, isLoading, error } = useQuery({
    queryKey: ['dashboard-data'],
    queryFn: gatewayApi.getDashboardData,
    refetchInterval: 30000,
  })

  const [connected, setConnected] = React.useState(false)
  const [messages, setMessages] = React.useState<unknown[]>([])

  React.useEffect(() => {
    wsService.connect()

    const unsubscribeConnection = wsService.onConnectionChange((isConnected) => {
      setConnected(isConnected)
    })

    const unsubscribeMessage = wsService.onMessage((message) => {
      setMessages((prev) => [...prev, message])
    })

    // Subscribe to dashboard updates
    wsService.subscribe('dashboard-updates')
    wsService.subscribe('saga-events')

    return () => {
      unsubscribeConnection()
      unsubscribeMessage()
      wsService.disconnect()
    }
  }, [])

  const sagaStats = dashboardData?.sagaStatistics || {}

  return (
    <div className="space-y-6">
      {/* Connection Status */}
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-2xl font-bold text-secondary-900">Dashboard</h1>
          <p className="text-secondary-600">Overview of system status and metrics</p>
        </div>
        <div className="flex items-center gap-2">
          <span className={`h-3 w-3 rounded-full ${connected ? 'bg-green-500' : 'bg-red-500'}`} />
          <span className="text-sm text-secondary-600">
            {connected ? 'Connected' : 'Disconnected'}
          </span>
        </div>
      </div>

      {/* Stats Grid */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
        <StatCard
          title="Active Sagas"
          value={sagaStats.inProgress || 0}
          change={12}
          icon={Activity}
          color="blue"
        />
        <StatCard
          title="Completed"
          value={sagaStats.completed || 0}
          change={8}
          icon={TrendingUp}
          color="green"
        />
        <StatCard
          title="Active Users"
          value="1,234"
          change={5}
          icon={Users}
          color="yellow"
        />
        <StatCard
          title="Data Points"
          value={dashboardData?.chartData?.totalDataPoints || 0}
          icon={Database}
          color="purple"
        />
      </div>

      {/* Service Health */}
      {dashboardData?.serviceHealth && (
        <ServiceHealthCard services={dashboardData.serviceHealth} />
      )}

      {/* Recent Activity */}
      <div className="card">
        <h3 className="text-lg font-semibold mb-4">Real-time Updates</h3>
        {messages.length > 0 ? (
          <div className="space-y-2 max-h-64 overflow-y-auto">
            {messages.slice(-10).reverse().map((msg, idx) => (
              <div key={idx} className="p-3 bg-secondary-50 rounded-lg text-sm">
                <pre className="text-secondary-700">{JSON.stringify(msg, null, 2)}</pre>
              </div>
            ))}
          </div>
        ) : (
          <p className="text-secondary-500">Waiting for real-time updates...</p>
        )}
      </div>

      {/* Saga Statistics by State */}
      <div className="card">
        <h3 className="text-lg font-semibold mb-4">Saga Statistics</h3>
        <div className="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-6 gap-4">
          <div className="text-center p-4 bg-blue-50 rounded-lg">
            <p className="text-2xl font-bold text-blue-600">{sagaStats.pending || 0}</p>
            <p className="text-sm text-blue-600">Pending</p>
          </div>
          <div className="text-center p-4 bg-yellow-50 rounded-lg">
            <p className="text-2xl font-bold text-yellow-600">{sagaStats.inProgress || 0}</p>
            <p className="text-sm text-yellow-600">In Progress</p>
          </div>
          <div className="text-center p-4 bg-green-50 rounded-lg">
            <p className="text-2xl font-bold text-green-600">{sagaStats.completed || 0}</p>
            <p className="text-sm text-green-600">Completed</p>
          </div>
          <div className="text-center p-4 bg-red-50 rounded-lg">
            <p className="text-2xl font-bold text-red-600">{sagaStats.failed || 0}</p>
            <p className="text-sm text-red-600">Failed</p>
          </div>
          <div className="text-center p-4 bg-orange-50 rounded-lg">
            <p className="text-2xl font-bold text-orange-600">{sagaStats.compensating || 0}</p>
            <p className="text-sm text-orange-600">Compensating</p>
          </div>
          <div className="text-center p-4 bg-secondary-100 rounded-lg">
            <p className="text-2xl font-bold text-secondary-600">{sagaStats.cancelled || 0}</p>
            <p className="text-sm text-secondary-600">Cancelled</p>
          </div>
        </div>
      </div>
    </div>
  )
}
