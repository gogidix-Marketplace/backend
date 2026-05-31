import { Link } from 'react-router-dom'
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@shared/components/ui/card'
import { Badge } from '@shared/components/ui/badge'
import { Button } from '@shared/components/ui/button'
import { MOCK_SERVICES, MOCK_ALERTS, MOCK_DASHBOARD_STATS } from '@shared/stores/mockData'
import { formatRelativeTime, getStatusColor, getStatusDotColor } from '@shared/utils/cn'
import {
  Activity,
  AlertTriangle,
  CheckCircle,
  XCircle,
  AlertCircle,
  ArrowRight,
  TrendingUp,
  TrendingDown,
  Clock,
} from 'lucide-react'

export default function OverviewPage() {
  const stats = MOCK_DASHBOARD_STATS
  const activeAlerts = MOCK_ALERTS.filter(a => a.status === 'active')
  const recentAlerts = activeAlerts.slice(0, 4)

  return (
    <div className="space-y-6">
      {/* Page Header */}
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-3xl font-bold text-slate-900">Monitoring Overview</h1>
          <p className="text-slate-600">Real-time health status of all foundation services</p>
        </div>
        <div className="flex items-center gap-2">
          <div className="flex items-center gap-2 rounded-md bg-slate-100 px-3 py-2">
            <span className="relative flex h-2 w-2">
              <span className="absolute inline-flex h-full w-full animate-ping rounded-full bg-green-400 opacity-75"></span>
              <span className="relative inline-flex h-2 w-2 rounded-full bg-green-500"></span>
            </span>
            <span className="text-sm font-medium text-slate-700">Live</span>
          </div>
        </div>
      </div>

      {/* Stats Cards */}
      <div className="grid gap-4 md:grid-cols-2 lg:grid-cols-4">
        <Card>
          <CardHeader className="flex flex-row items-center justify-between pb-2">
            <CardTitle className="text-sm font-medium text-slate-600">Total Services</CardTitle>
            <Activity className="h-4 w-4 text-slate-500" />
          </CardHeader>
          <CardContent>
            <div className="text-3xl font-bold">{stats.totalServices}</div>
            <p className="text-xs text-slate-600">Foundation services monitored</p>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="flex flex-row items-center justify-between pb-2">
            <CardTitle className="text-sm font-medium text-slate-600">Healthy</CardTitle>
            <CheckCircle className="h-4 w-4 text-green-500" />
          </CardHeader>
          <CardContent>
            <div className="text-3xl font-bold text-green-600">{stats.healthyServices}</div>
            <p className="text-xs text-slate-600">{stats.averageUptime}% avg uptime</p>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="flex flex-row items-center justify-between pb-2">
            <CardTitle className="text-sm font-medium text-slate-600">Issues</CardTitle>
            <AlertTriangle className="h-4 w-4 text-yellow-500" />
          </CardHeader>
          <CardContent>
            <div className="text-3xl font-bold text-yellow-600">
              {stats.degradedServices + stats.criticalServices}
            </div>
            <p className="text-xs text-slate-600">{stats.degradedServices} degraded, {stats.criticalServices} critical</p>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="flex flex-row items-center justify-between pb-2">
            <CardTitle className="text-sm font-medium text-slate-600">Active Alerts</CardTitle>
            <AlertCircle className="h-4 w-4 text-red-500" />
          </CardHeader>
          <CardContent>
            <div className="text-3xl font-bold text-red-600">{stats.activeAlerts}</div>
            <p className="text-xs text-slate-600">{stats.criticalAlerts} critical</p>
          </CardContent>
        </Card>
      </div>

      {/* Services Health Grid */}
      <Card>
        <CardHeader>
          <div className="flex items-center justify-between">
            <div>
              <CardTitle>Services Health</CardTitle>
              <CardDescription>Real-time status of all monitored services</CardDescription>
            </div>
            <Link to="/services">
              <Button variant="outline" size="sm">
                View All
                <ArrowRight className="ml-2 h-4 w-4" />
              </Button>
            </Link>
          </div>
        </CardHeader>
        <CardContent>
          <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4">
            {MOCK_SERVICES.slice(0, 8).map((service) => (
              <Link key={service.id} to={`/services/${service.id}`}>
                <Card className="transition-all hover:shadow-md hover:border-[#0D47A1] cursor-pointer">
                  <CardContent className="p-4">
                    <div className="flex items-start justify-between mb-3">
                      <div className="flex-1">
                        <h3 className="font-semibold text-sm truncate">{service.name}</h3>
                        <p className="text-xs text-slate-500">{service.type.replace('_', ' ')}</p>
                      </div>
                      <span className={`h-2.5 w-2.5 rounded-full ${getStatusDotColor(service.status)}`} />
                    </div>
                    <div className="space-y-2">
                      <div className="flex items-center justify-between text-xs">
                        <span className="text-slate-500">Status</span>
                        <Badge variant={service.status === 'healthy' ? 'healthy' : service.status === 'degraded' ? 'degraded' : 'critical'} className="text-xs">
                          {service.status}
                        </Badge>
                      </div>
                      <div className="flex items-center justify-between text-xs">
                        <span className="text-slate-500">Response</span>
                        <span className={service.responseTime > 200 ? 'text-yellow-600 font-medium' : ''}>
                          {service.status === 'critical' ? '-' : `${service.responseTime}ms`}
                        </span>
                      </div>
                      <div className="flex items-center justify-between text-xs">
                        <span className="text-slate-500">Uptime</span>
                        <span className={service.uptime < 99 ? 'text-yellow-600 font-medium' : 'text-green-600'}>
                          {service.uptime}%
                        </span>
                      </div>
                    </div>
                  </CardContent>
                </Card>
              </Link>
            ))}
          </div>
        </CardContent>
      </Card>

      {/* Active Alerts */}
      <Card>
        <CardHeader>
          <div className="flex items-center justify-between">
            <div>
              <CardTitle>Active Alerts</CardTitle>
              <CardDescription>Alerts requiring attention</CardDescription>
            </div>
            <Link to="/alerts">
              <Button variant="outline" size="sm">
                View All
                <ArrowRight className="ml-2 h-4 w-4" />
              </Button>
            </Link>
          </div>
        </CardHeader>
        <CardContent>
          <div className="space-y-3">
            {recentAlerts.map((alert) => (
              <div
                key={alert.id}
                className="flex items-center justify-between rounded-lg border p-4 hover:bg-slate-50 transition-colors"
              >
                <div className="flex items-start gap-3">
                  <div className={`mt-0.5 h-2.5 w-2.5 rounded-full ${
                    alert.severity === 'critical' ? 'bg-red-500' :
                    alert.severity === 'error' ? 'bg-orange-500' :
                    'bg-yellow-500'
                  }`} />
                  <div>
                    <h4 className="font-semibold text-sm">{alert.title}</h4>
                    <p className="text-sm text-slate-600">{alert.description}</p>
                    <div className="flex items-center gap-3 mt-1">
                      <span className="text-xs text-slate-500 flex items-center gap-1">
                        <Clock className="h-3 w-3" />
                        {formatRelativeTime(alert.triggeredAt)}
                      </span>
                      <span className="text-xs text-slate-500">{alert.serviceName}</span>
                    </div>
                  </div>
                </div>
                <div className="flex items-center gap-2">
                  <Badge
                    variant={alert.severity === 'critical' ? 'critical' : alert.severity === 'error' ? 'destructive' : 'degraded'}
                  >
                    {alert.severity}
                  </Badge>
                  <Link to={`/alerts`}>
                    <Button variant="ghost" size="sm">View</Button>
                  </Link>
                </div>
              </div>
            ))}
          </div>
        </CardContent>
      </Card>
    </div>
  )
}
