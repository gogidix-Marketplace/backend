import { useParams, Link, useNavigate } from 'react-router-dom'
import { useState } from 'react'
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@shared/components/ui/card'
import { Badge } from '@shared/components/ui/badge'
import { Button } from '@shared/components/ui/button'
import { Tabs, TabsContent, TabsList, TabsTrigger } from '@shared/components/ui/tabs'
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from '@shared/components/ui/select'
import { MOCK_SERVICES, MOCK_METRICS, MOCK_ALERTS } from '@shared/stores/mockData'
import { getStatusDotColor, formatRelativeTime } from '@shared/utils/cn'
import {
  ArrowLeft,
  RefreshCw,
  Settings,
  Activity,
  AlertTriangle,
  Clock,
  TrendingUp,
  Server,
  ExternalLink,
} from 'lucide-react'
import {
  LineChart,
  Line,
  AreaChart,
  Area,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  ResponsiveContainer,
  Legend,
} from 'recharts'

export default function ServiceDetailPage() {
  const { id } = useParams<{ id: string }>()
  const navigate = useNavigate()
  const [timeRange, setTimeRange] = useState('24h')
  const [refreshing, setRefreshing] = useState(false)

  const service = MOCK_SERVICES.find(s => s.id === id)

  if (!service) {
    return (
      <div className="flex flex-col items-center justify-center h-full">
        <h1 className="text-2xl font-bold text-slate-900 mb-4">Service not found</h1>
        <Link to="/services">
          <Button variant="outline">Back to Services</Button>
        </Link>
      </div>
    )
  }

  const serviceAlerts = MOCK_ALERTS.filter(a => a.serviceId === id)
  const metrics = MOCK_METRICS[id] || []

  const handleRefresh = async () => {
    setRefreshing(true)
    await new Promise(resolve => setTimeout(resolve, 1000))
    setRefreshing(false)
  }

  const handleRestart = async () => {
    if (confirm(`Are you sure you want to restart ${service.name}?`)) {
      // Simulate restart
      await new Promise(resolve => setTimeout(resolve, 2000))
      alert(`${service.name} restart initiated`)
    }
  }

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex items-center justify-between">
        <div className="flex items-center gap-4">
          <Link to="/services">
            <Button variant="ghost" size="icon">
              <ArrowLeft className="h-5 w-5" />
            </Button>
          </Link>
          <div>
            <div className="flex items-center gap-2">
              <h1 className="text-2xl font-bold text-slate-900">{service.name}</h1>
              <span className={`h-3 w-3 rounded-full ${getStatusDotColor(service.status)}`} />
              <Badge variant={service.status === 'healthy' ? 'healthy' : service.status === 'degraded' ? 'degraded' : 'critical'}>
                {service.status}
              </Badge>
            </div>
            <p className="text-slate-600">{service.description}</p>
          </div>
        </div>
        <div className="flex items-center gap-2">
          <Select value={timeRange} onValueChange={setTimeRange}>
            <SelectTrigger className="w-[120px]">
              <SelectValue />
            </SelectTrigger>
            <SelectContent>
              <SelectItem value="1h">1 hour</SelectItem>
              <SelectItem value="6h">6 hours</SelectItem>
              <SelectItem value="24h">24 hours</SelectItem>
              <SelectItem value="7d">7 days</SelectItem>
              <SelectItem value="30d">30 days</SelectItem>
            </SelectContent>
          </Select>
          <Button variant="outline" size="icon" onClick={handleRefresh} disabled={refreshing}>
            <RefreshCw className={`h-4 w-4 ${refreshing ? 'animate-spin' : ''}`} />
          </Button>
          <Button variant="outline" onClick={handleRestart}>
            <RefreshCw className="mr-2 h-4 w-4" />
            Restart
          </Button>
          {service.endpoint && (
            <Button variant="outline" asChild>
              <a href={service.endpoint} target="_blank" rel="noopener noreferrer">
                <ExternalLink className="mr-2 h-4 w-4" />
                Open
              </a>
            </Button>
          )}
        </div>
      </div>

      {/* Quick Stats */}
      <div className="grid gap-4 md:grid-cols-2 lg:grid-cols-5">
        <Card>
          <CardHeader className="pb-2">
            <CardTitle className="text-sm font-medium text-slate-600">Status</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="flex items-center gap-2">
              <span className={`h-3 w-3 rounded-full ${getStatusDotColor(service.status)}`} />
              <span className={`text-lg font-semibold capitalize ${service.status === 'healthy' ? 'text-green-600' : service.status === 'degraded' ? 'text-yellow-600' : 'text-red-600'}`}>
                {service.status}
              </span>
            </div>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="pb-2">
            <CardTitle className="text-sm font-medium text-slate-600">Uptime</CardTitle>
          </CardHeader>
          <CardContent>
            <p className="text-lg font-semibold">{service.uptime}%</p>
            <p className="text-xs text-slate-500">Last 30 days</p>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="pb-2">
            <CardTitle className="text-sm font-medium text-slate-600">Response Time</CardTitle>
          </CardHeader>
          <CardContent>
            <p className="text-lg font-semibold">
              {service.status === 'critical' ? '-' : `${service.responseTime}ms`}
            </p>
            <p className="text-xs text-slate-500">Average</p>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="pb-2">
            <CardTitle className="text-sm font-medium text-slate-600">Error Rate</CardTitle>
          </CardHeader>
          <CardContent>
            <p className={`text-lg font-semibold ${service.errorRate > 1 ? 'text-red-600' : 'text-slate-900'}`}>
              {service.errorRate}%
            </p>
            <p className="text-xs text-slate-500">Last 24 hours</p>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="pb-2">
            <CardTitle className="text-sm font-medium text-slate-600">Throughput</CardTitle>
          </CardHeader>
          <CardContent>
            <p className="text-lg font-semibold">
              {service.throughput > 1000 ? `${(service.throughput / 1000).toFixed(1)}k` : service.throughput}
              <span className="text-sm text-slate-500"> req/s</span>
            </p>
            <p className="text-xs text-slate-500">Current</p>
          </CardContent>
        </Card>
      </div>

      {/* Tabs */}
      <Tabs defaultValue="metrics">
        <TabsList>
          <TabsTrigger value="metrics">Metrics</TabsTrigger>
          <TabsTrigger value="alerts">Alerts ({serviceAlerts.length})</TabsTrigger>
          <TabsTrigger value="dependencies">Dependencies</TabsTrigger>
          <TabsTrigger value="logs">Logs</TabsTrigger>
        </TabsList>

        <TabsContent value="metrics" className="space-y-4">
          <Card>
            <CardHeader>
              <CardTitle>Response Time</CardTitle>
              <CardTitle className="text-sm font-normal text-slate-600">Response time over selected time range</CardTitle>
            </CardHeader>
            <CardContent>
              <ResponsiveContainer width="100%" height={300}>
                <AreaChart data={metrics}>
                  <defs>
                    <linearGradient id="colorResponse" x1="0" y1="0" x2="0" y2="1">
                      <stop offset="5%" stopColor="#0D47A1" stopOpacity={0.8}/>
                      <stop offset="95%" stopColor="#0D47A1" stopOpacity={0}/>
                    </linearGradient>
                  </defs>
                  <CartesianGrid strokeDasharray="3 3" />
                  <XAxis
                    dataKey="timestamp"
                    tickFormatter={(value) => new Date(value).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })}
                  />
                  <YAxis />
                  <Tooltip
                    labelFormatter={(value) => new Date(value).toLocaleString()}
                    formatter={(value: number) => [`${value}ms`, 'Response Time']}
                  />
                  <Area type="monotone" dataKey="value" stroke="#0D47A1" fillOpacity={1} fill="url(#colorResponse)" />
                </AreaChart>
              </ResponsiveContainer>
            </CardContent>
          </Card>

          <div className="grid gap-4 md:grid-cols-2">
            <Card>
              <CardHeader>
                <CardTitle>Request Distribution</CardTitle>
              </CardHeader>
              <CardContent className="space-y-4">
                <div className="flex items-center justify-between">
                  <span className="text-sm">2xx Success</span>
                  <div className="flex items-center gap-2">
                    <div className="w-32 h-2 bg-slate-200 rounded-full overflow-hidden">
                      <div className="h-full bg-green-500 rounded-full" style={{ width: '98.5%' }} />
                    </div>
                    <span className="text-sm font-medium">98.5%</span>
                  </div>
                </div>
                <div className="flex items-center justify-between">
                  <span className="text-sm">4xx Client Errors</span>
                  <div className="flex items-center gap-2">
                    <div className="w-32 h-2 bg-slate-200 rounded-full overflow-hidden">
                      <div className="h-full bg-yellow-500 rounded-full" style={{ width: '1.2%' }} />
                    </div>
                    <span className="text-sm font-medium">1.2%</span>
                  </div>
                </div>
                <div className="flex items-center justify-between">
                  <span className="text-sm">5xx Server Errors</span>
                  <div className="flex items-center gap-2">
                    <div className="w-32 h-2 bg-slate-200 rounded-full overflow-hidden">
                      <div className="h-full bg-red-500 rounded-full" style={{ width: '0.3%' }} />
                    </div>
                    <span className="text-sm font-medium">0.3%</span>
                  </div>
                </div>
              </CardContent>
            </Card>

            <Card>
              <CardHeader>
                <CardTitle>Service Info</CardTitle>
              </CardHeader>
              <CardContent className="space-y-3 text-sm">
                <div className="flex justify-between">
                  <span className="text-slate-600">Version</span>
                  <span className="font-medium">{service.version}</span>
                </div>
                <div className="flex justify-between">
                  <span className="text-slate-600">Region</span>
                  <span className="font-medium">{service.region}</span>
                </div>
                <div className="flex justify-between">
                  <span className="text-slate-600">Type</span>
                  <span className="font-medium">{service.type.replace(/_/g, ' ')}</span>
                </div>
                <div className="flex justify-between">
                  <span className="text-slate-600">Last Check</span>
                  <span className="font-medium">{formatRelativeTime(service.lastChecked)}</span>
                </div>
                {service.endpoint && (
                  <div className="flex justify-between items-center">
                    <span className="text-slate-600">Endpoint</span>
                    <a href={service.endpoint} target="_blank" rel="noopener noreferrer" className="text-[#0D47A1] hover:underline flex items-center gap-1">
                      <ExternalLink className="h-3 w-3" />
                      Open
                    </a>
                  </div>
                )}
              </CardContent>
            </Card>
          </div>
        </TabsContent>

        <TabsContent value="alerts" className="space-y-4">
          {serviceAlerts.length > 0 ? (
            <Card>
              <CardContent className="p-4 space-y-3">
                {serviceAlerts.map((alert) => (
                  <div key={alert.id} className="flex items-center justify-between rounded-lg border p-4">
                    <div className="flex items-start gap-3">
                      <div className={`mt-0.5 h-2.5 w-2.5 rounded-full ${
                        alert.severity === 'critical' ? 'bg-red-500' :
                        alert.severity === 'error' ? 'bg-orange-500' :
                        'bg-yellow-500'
                      }`} />
                      <div>
                        <h4 className="font-semibold">{alert.title}</h4>
                        <p className="text-sm text-slate-600">{alert.description}</p>
                        <p className="text-xs text-slate-500 mt-1">{formatRelativeTime(alert.triggeredAt)}</p>
                      </div>
                    </div>
                    <Badge variant={alert.severity === 'critical' ? 'critical' : alert.severity === 'error' ? 'destructive' : 'degraded'}>
                      {alert.severity}
                    </Badge>
                  </div>
                ))}
              </CardContent>
            </Card>
          ) : (
            <Card>
              <CardContent className="p-12 text-center">
                <CheckCircle className="mx-auto h-12 w-12 text-green-500 mb-4" />
                <h3 className="text-lg font-semibold mb-2">No alerts</h3>
                <p className="text-slate-600">This service has no active alerts</p>
              </CardContent>
            </Card>
          )}
        </TabsContent>

        <TabsContent value="dependencies" className="space-y-4">
          <div className="grid gap-4 md:grid-cols-2">
            <Card>
              <CardHeader>
                <CardTitle>Dependencies</CardTitle>
                <CardDescription>Services that this service depends on</CardDescription>
              </CardHeader>
              <CardContent>
                <div className="space-y-2">
                  {service.dependencies.length > 0 ? service.dependencies.map((depId) => {
                    const depService = MOCK_SERVICES.find(s => s.id === depId)
                    return depService ? (
                      <Link key={depId} to={`/services/${depId}`}>
                        <div className="flex items-center justify-between rounded-lg border p-3 hover:bg-slate-50 cursor-pointer">
                          <div className="flex items-center gap-3">
                            <span className={`h-2.5 w-2.5 rounded-full ${getStatusDotColor(depService.status)}`} />
                            <span className="font-medium">{depService.name}</span>
                          </div>
                          <ArrowLeft className="h-4 w-4 rotate-180 text-slate-400" />
                        </div>
                      </Link>
                    ) : null
                  }) : (
                    <p className="text-slate-500 text-sm">No dependencies</p>
                  )}
                </div>
              </CardContent>
            </Card>

            <Card>
              <CardHeader>
                <CardTitle>Dependents</CardTitle>
                <CardDescription>Services that depend on this service</CardDescription>
              </CardHeader>
              <CardContent>
                <div className="space-y-2">
                  {service.dependents.length > 0 ? service.dependents.map((depId) => {
                    const depService = MOCK_SERVICES.find(s => s.id === depId)
                    return depService ? (
                      <Link key={depId} to={`/services/${depId}`}>
                        <div className="flex items-center justify-between rounded-lg border p-3 hover:bg-slate-50 cursor-pointer">
                          <div className="flex items-center gap-3">
                            <span className={`h-2.5 w-2.5 rounded-full ${getStatusDotColor(depService.status)}`} />
                            <span className="font-medium">{depService.name}</span>
                          </div>
                          <ArrowLeft className="h-4 w-4 rotate-180 text-slate-400" />
                        </div>
                      </Link>
                    ) : null
                  }) : (
                    <p className="text-slate-500 text-sm">No dependents</p>
                  )}
                </div>
              </CardContent>
            </Card>
          </div>
        </TabsContent>

        <TabsContent value="logs" className="space-y-4">
          <Card>
            <CardHeader>
              <CardTitle>Recent Logs</CardTitle>
              <CardDescription>Live log stream from this service</CardDescription>
            </CardHeader>
            <CardContent>
              <div className="bg-slate-950 text-slate-50 rounded-lg p-4 font-mono text-sm space-y-1 h-96 overflow-y-auto">
                <div className="flex gap-2">
                  <span className="text-slate-500">{new Date().toISOString()}</span>
                  <span className="text-green-500">[INFO]</span>
                  <span>Health check passed: status=healthy</span>
                </div>
                <div className="flex gap-2">
                  <span className="text-slate-500">{new Date(Date.now() - 5000).toISOString()}</span>
                  <span className="text-green-500">[INFO]</span>
                  <span>Request processed: method=GET path=/api/v1/health status=200 duration=42ms</span>
                </div>
                <div className="flex gap-2">
                  <span className="text-slate-500">{new Date(Date.now() - 10000).toISOString()}</span>
                  <span className="text-green-500">[INFO]</span>
                  <span>Cache hit: key=user:12345</span>
                </div>
                <div className="flex gap-2">
                  <span className="text-slate-500">{new Date(Date.now() - 15000).toISOString()}</span>
                  <span className="text-yellow-500">[WARN]</span>
                  <span>Slow query detected: duration=250ms threshold=200ms</span>
                </div>
                <div className="flex gap-2">
                  <span className="text-slate-500">{new Date(Date.now() - 20000).toISOString()}</span>
                  <span className="text-green-500">[INFO]</span>
                  <span>Connection pool: active=5 idle=15 max=20</span>
                </div>
                <div className="flex items-center justify-center py-8 text-slate-500">
                  <span className="animate-pulse">Loading live logs...</span>
                </div>
              </div>
            </CardContent>
          </Card>
        </TabsContent>
      </Tabs>
    </div>
  )
}
