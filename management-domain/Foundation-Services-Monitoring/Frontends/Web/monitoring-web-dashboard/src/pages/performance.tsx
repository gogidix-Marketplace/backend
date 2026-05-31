import { useState } from 'react'
import { Link } from 'react-router-dom'
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@shared/components/ui/card'
import { Badge } from '@shared/components/ui/badge'
import { Button } from '@shared/components/ui/button'
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from '@shared/components/ui/select'
import { Tabs, TabsContent, TabsList, TabsTrigger } from '@shared/components/ui/tabs'
import { MOCK_SERVICES, MOCK_METRICS } from '@shared/stores/mockData'
import { getStatusDotColor } from '@shared/utils/cn'
import {
  LineChart,
  Line,
  AreaChart,
  Area,
  BarChart,
  Bar,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  ResponsiveContainer,
  Legend,
} from 'recharts'
import { TrendingUp, TrendingDown, Activity, Zap } from 'lucide-react'

export default function PerformancePage() {
  const [timeRange, setTimeRange] = useState('24h')
  const [selectedService, setSelectedService] = useState<string>('all')

  const healthyServices = MOCK_SERVICES.filter(s => s.status !== 'critical')

  // Generate aggregated metrics data
  const generateAggregatedMetrics = () => {
    const data: { time: string; avgResponse: number; throughput: number; errors: number }[] = []
    for (let i = 0; i < 24; i++) {
      data.push({
        time: `${i}:00`,
        avgResponse: 30 + Math.random() * 50,
        throughput: 8000 + Math.random() * 5000,
        errors: Math.random() * 100,
      })
    }
    return data
  }

  const aggregatedData = generateAggregatedMetrics()

  // Generate service comparison data
  const serviceComparisonData = healthyServices.map(s => ({
    name: s.name,
    responseTime: s.responseTime,
    throughput: s.throughput,
    errorRate: s.errorRate,
  }))

  // Calculate trends
  const calculateTrend = (current: number, previous: number) => {
    const change = ((current - previous) / previous) * 100
    return {
      value: Math.abs(change).toFixed(1),
      isPositive: change >= 0,
    }
  }

  return (
    <div className="space-y-6">
      {/* Page Header */}
      <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
        <div>
          <h1 className="text-3xl font-bold text-slate-900">Performance Metrics</h1>
          <p className="text-slate-600">Monitor system-wide performance indicators</p>
        </div>
        <div className="flex items-center gap-2">
          <Select value={selectedService} onValueChange={setSelectedService}>
            <SelectTrigger className="w-[200px]">
              <SelectValue placeholder="All Services" />
            </SelectTrigger>
            <SelectContent>
              <SelectItem value="all">All Services</SelectItem>
              {MOCK_SERVICES.map(s => (
                <SelectItem key={s.id} value={s.id}>{s.name}</SelectItem>
              ))}
            </SelectContent>
          </Select>
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
        </div>
      </div>

      {/* Key Metrics */}
      <div className="grid gap-4 md:grid-cols-2 lg:grid-cols-4">
        <Card>
          <CardHeader className="flex flex-row items-center justify-between pb-2">
            <CardTitle className="text-sm font-medium">Avg Response Time</CardTitle>
            <Activity className="h-4 w-4 text-slate-500" />
          </CardHeader>
          <CardContent>
            <div className="flex items-center justify-between">
              <div className="text-3xl font-bold">
                {healthyServices.length > 0
                  ? Math.round(healthyServices.reduce((sum, s) => sum + s.responseTime, 0) / healthyServices.length)
                  : 0}
                <span className="text-lg text-slate-500">ms</span>
              </div>
              <div className="flex items-center gap-1 text-green-600 text-sm">
                <TrendingDown className="h-4 w-4" />
                <span>-5.2%</span>
              </div>
            </div>
            <p className="text-xs text-slate-500 mt-1">vs previous period</p>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="flex flex-row items-center justify-between pb-2">
            <CardTitle className="text-sm font-medium">Total Throughput</CardTitle>
            <Zap className="h-4 w-4 text-slate-500" />
          </CardHeader>
          <CardContent>
            <div className="flex items-center justify-between">
              <div className="text-3xl font-bold">
                {(healthyServices.reduce((sum, s) => sum + s.throughput, 0) / 1000).toFixed(1)}
                <span className="text-lg text-slate-500">k/s</span>
              </div>
              <div className="flex items-center gap-1 text-green-600 text-sm">
                <TrendingUp className="h-4 w-4" />
                <span>+12.3%</span>
              </div>
            </div>
            <p className="text-xs text-slate-500 mt-1">requests per second</p>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="flex flex-row items-center justify-between pb-2">
            <CardTitle className="text-sm font-medium">Error Rate</CardTitle>
            <Activity className="h-4 w-4 text-slate-500" />
          </CardHeader>
          <CardContent>
            <div className="flex items-center justify-between">
              <div className="text-3xl font-bold">
                {healthyServices.length > 0
                  ? (healthyServices.reduce((sum, s) => sum + s.errorRate, 0) / healthyServices.length).toFixed(2)
                  : 0}
                <span className="text-lg text-slate-500">%</span>
              </div>
              <div className="flex items-center gap-1 text-green-600 text-sm">
                <TrendingDown className="h-4 w-4" />
                <span>-8.1%</span>
              </div>
            </div>
            <p className="text-xs text-slate-500 mt-1">across all services</p>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="flex flex-row items-center justify-between pb-2">
            <CardTitle className="text-sm font-medium">P95 Response</CardTitle>
            <Activity className="h-4 w-4 text-slate-500" />
          </CardHeader>
          <CardContent>
            <div className="flex items-center justify-between">
              <div className="text-3xl font-bold">
                145
                <span className="text-lg text-slate-500">ms</span>
              </div>
              <div className="flex items-center gap-1 text-red-600 text-sm">
                <TrendingUp className="h-4 w-4" />
                <span>+2.1%</span>
              </div>
            </div>
            <p className="text-xs text-slate-500 mt-1">95th percentile</p>
          </CardContent>
        </Card>
      </div>

      {/* Tabs for different metrics */}
      <Tabs defaultValue="response-time">
        <TabsList>
          <TabsTrigger value="response-time">Response Time</TabsTrigger>
          <TabsTrigger value="throughput">Throughput</TabsTrigger>
          <TabsTrigger value="errors">Error Rate</TabsTrigger>
          <TabsTrigger value="comparison">Service Comparison</TabsTrigger>
        </TabsList>

        <TabsContent value="response-time" className="space-y-4">
          <Card>
            <CardHeader>
              <CardTitle>Average Response Time</CardTitle>
              <CardTitle className="text-sm font-normal text-slate-600">
                Average response time across all services over selected time range
              </CardTitle>
            </CardHeader>
            <CardContent>
              <ResponsiveContainer width="100%" height={350}>
                <AreaChart data={aggregatedData}>
                  <defs>
                    <linearGradient id="colorResponse" x1="0" y1="0" x2="0" y2="1">
                      <stop offset="5%" stopColor="#0D47A1" stopOpacity={0.8}/>
                      <stop offset="95%" stopColor="#0D47A1" stopOpacity={0}/>
                    </linearGradient>
                  </defs>
                  <CartesianGrid strokeDasharray="3 3" />
                  <XAxis dataKey="time" />
                  <YAxis />
                  <Tooltip formatter={(value: number) => [`${value}ms`, 'Response Time']} />
                  <Area type="monotone" dataKey="avgResponse" stroke="#0D47A1" fillOpacity={1} fill="url(#colorResponse)" />
                </AreaChart>
              </ResponsiveContainer>
            </CardContent>
          </Card>

          <div className="grid gap-4 md:grid-cols-2">
            <Card>
              <CardHeader>
                <CardTitle className="text-lg">Response Time by Percentile</CardTitle>
              </CardHeader>
              <CardContent>
                <div className="space-y-4">
                  {[
                    { label: 'P50 (Median)', value: 42, color: 'bg-green-500' },
                    { label: 'P95', value: 145, color: 'bg-yellow-500' },
                    { label: 'P99', value: 289, color: 'bg-orange-500' },
                    { label: 'P99.9', value: 512, color: 'bg-red-500' },
                  ].map((item) => (
                    <div key={item.label}>
                      <div className="flex justify-between text-sm mb-1">
                        <span className="text-slate-600">{item.label}</span>
                        <span className="font-medium">{item.value}ms</span>
                      </div>
                      <div className="h-2 bg-slate-200 rounded-full overflow-hidden">
                        <div
                          className={`h-full ${item.color} rounded-full`}
                          style={{ width: `${(item.value / 600) * 100}%` }}
                        />
                      </div>
                    </div>
                  ))}
                </div>
              </CardContent>
            </Card>

            <Card>
              <CardHeader>
                <CardTitle className="text-lg">Slowest Services</CardTitle>
              </CardHeader>
              <CardContent>
                <div className="space-y-3">
                  {MOCK_SERVICES
                    .filter(s => s.status !== 'critical')
                    .sort((a, b) => b.responseTime - a.responseTime)
                    .slice(0, 5)
                    .map((service) => (
                      <Link key={service.id} to={`/services/${service.id}`}>
                        <div className="flex items-center justify-between p-2 rounded hover:bg-slate-50 cursor-pointer">
                          <div className="flex items-center gap-2">
                            <span className={`h-2 w-2 rounded-full ${getStatusDotColor(service.status)}`} />
                            <span className="text-sm font-medium">{service.name}</span>
                          </div>
                          <span className={`text-sm font-medium ${service.responseTime > 200 ? 'text-yellow-600' : ''}`}>
                            {service.responseTime}ms
                          </span>
                        </div>
                      </Link>
                    ))}
                </div>
              </CardContent>
            </Card>
          </div>
        </TabsContent>

        <TabsContent value="throughput" className="space-y-4">
          <Card>
            <CardHeader>
              <CardTitle>Request Throughput</CardTitle>
              <CardTitle className="text-sm font-normal text-slate-600">
                Total requests per second across all services
              </CardTitle>
            </CardHeader>
            <CardContent>
              <ResponsiveContainer width="100%" height={350}>
                <AreaChart data={aggregatedData}>
                  <defs>
                    <linearGradient id="colorThroughput" x1="0" y1="0" x2="0" y2="1">
                      <stop offset="5%" stopColor="#10B981" stopOpacity={0.8}/>
                      <stop offset="95%" stopColor="#10B981" stopOpacity={0}/>
                    </linearGradient>
                  </defs>
                  <CartesianGrid strokeDasharray="3 3" />
                  <XAxis dataKey="time" />
                  <YAxis />
                  <Tooltip formatter={(value: number) => [`${value} req/s`, 'Throughput']} />
                  <Area type="monotone" dataKey="throughput" stroke="#10B981" fillOpacity={1} fill="url(#colorThroughput)" />
                </AreaChart>
              </ResponsiveContainer>
            </CardContent>
          </Card>

          <Card>
            <CardHeader>
              <CardTitle className="text-lg">Throughput by Service</CardTitle>
            </CardHeader>
            <CardContent>
              <ResponsiveContainer width="100%" height={300}>
                <BarChart data={serviceComparisonData.sort((a, b) => b.throughput - a.throughput).slice(0, 8)}>
                  <CartesianGrid strokeDasharray="3 3" />
                  <XAxis dataKey="name" angle={-45} textAnchor="end" height={100} />
                  <YAxis />
                  <Tooltip formatter={(value: number) => [value > 1000 ? `${(value/1000).toFixed(1)}k req/s` : `${value} req/s`, 'Throughput']} />
                  <Bar dataKey="throughput" fill="#0D47A1" />
                </BarChart>
              </ResponsiveContainer>
            </CardContent>
          </Card>
        </TabsContent>

        <TabsContent value="errors" className="space-y-4">
          <Card>
            <CardHeader>
              <CardTitle>Error Rate Over Time</CardTitle>
              <CardTitle className="text-sm font-normal text-slate-600">
                Percentage of requests resulting in errors
              </CardTitle>
            </CardHeader>
            <CardContent>
              <ResponsiveContainer width="100%" height={350}>
                <LineChart data={aggregatedData}>
                  <CartesianGrid strokeDasharray="3 3" />
                  <XAxis dataKey="time" />
                  <YAxis />
                  <Tooltip formatter={(value: number) => [`${value.toFixed(2)}%`, 'Error Rate']} />
                  <Line type="monotone" dataKey="errors" stroke="#EF4444" strokeWidth={2} />
                </LineChart>
              </ResponsiveContainer>
            </CardContent>
          </Card>

          <div className="grid gap-4 md:grid-cols-2">
            <Card>
              <CardHeader>
                <CardTitle className="text-lg">Highest Error Rates</CardTitle>
              </CardHeader>
              <CardContent>
                <div className="space-y-3">
                  {MOCK_SERVICES
                    .filter(s => s.status !== 'critical')
                    .sort((a, b) => b.errorRate - a.errorRate)
                    .slice(0, 5)
                    .map((service) => (
                      <Link key={service.id} to={`/services/${service.id}`}>
                        <div className="flex items-center justify-between p-2 rounded hover:bg-slate-50 cursor-pointer">
                          <div className="flex items-center gap-2">
                            <span className={`h-2 w-2 rounded-full ${getStatusDotColor(service.status)}`} />
                            <span className="text-sm font-medium">{service.name}</span>
                          </div>
                          <span className={`text-sm font-medium ${service.errorRate > 1 ? 'text-red-600' : service.errorRate > 0.1 ? 'text-yellow-600' : 'text-green-600'}`}>
                            {service.errorRate}%
                          </span>
                        </div>
                      </Link>
                    ))}
                </div>
              </CardContent>
            </Card>

            <Card>
              <CardHeader>
                <CardTitle className="text-lg">Error Distribution</CardTitle>
              </CardHeader>
              <CardContent className="space-y-4">
                <div className="flex items-center justify-between">
                  <span className="text-sm text-slate-600">4xx Client Errors</span>
                  <div className="flex items-center gap-2">
                    <div className="w-24 h-2 bg-slate-200 rounded-full overflow-hidden">
                      <div className="h-full bg-yellow-500 rounded-full" style={{ width: '65%' }} />
                    </div>
                    <span className="text-sm font-medium">65%</span>
                  </div>
                </div>
                <div className="flex items-center justify-between">
                  <span className="text-sm text-slate-600">5xx Server Errors</span>
                  <div className="flex items-center gap-2">
                    <div className="w-24 h-2 bg-slate-200 rounded-full overflow-hidden">
                      <div className="h-full bg-red-500 rounded-full" style={{ width: '30%' }} />
                    </div>
                    <span className="text-sm font-medium">30%</span>
                  </div>
                </div>
                <div className="flex items-center justify-between">
                  <span className="text-sm text-slate-600">Timeouts</span>
                  <div className="flex items-center gap-2">
                    <div className="w-24 h-2 bg-slate-200 rounded-full overflow-hidden">
                      <div className="h-full bg-orange-500 rounded-full" style={{ width: '5%' }} />
                    </div>
                    <span className="text-sm font-medium">5%</span>
                  </div>
                </div>
              </CardContent>
            </Card>
          </div>
        </TabsContent>

        <TabsContent value="comparison" className="space-y-4">
          <Card>
            <CardHeader>
              <CardTitle>Service Performance Comparison</CardTitle>
              <CardTitle className="text-sm font-normal text-slate-600">
                Compare key metrics across all services
              </CardTitle>
            </CardHeader>
            <CardContent>
              <div className="overflow-x-auto">
                <table className="w-full text-sm">
                  <thead>
                    <tr className="border-b">
                      <th className="text-left p-3">Service</th>
                      <th className="text-right p-3">Response Time</th>
                      <th className="text-right p-3">Throughput</th>
                      <th className="text-right p-3">Error Rate</th>
                      <th className="text-right p-3">Uptime</th>
                    </tr>
                  </thead>
                  <tbody>
                    {serviceComparisonData
                      .sort((a, b) => a.responseTime - b.responseTime)
                      .map((service) => {
                        const fullService = MOCK_SERVICES.find(s => s.name === service.name)
                        return (
                          <tr key={service.name} className="border-b hover:bg-slate-50">
                            <td className="p-3">
                              <Link to={`/services/${fullService?.id}`} className="flex items-center gap-2 font-medium hover:text-[#0D47A1]">
                                <span className={`h-2 w-2 rounded-full ${getStatusDotColor(fullService?.status || 'unknown')}`} />
                                {service.name}
                              </Link>
                            </td>
                            <td className="text-right p-3">
                              <span className={service.responseTime > 200 ? 'text-yellow-600 font-medium' : ''}>
                                {service.responseTime}ms
                              </span>
                            </td>
                            <td className="text-right p-3">
                              {service.throughput > 1000 ? `${(service.throughput/1000).toFixed(1)}k` : service.throughput}
                            </td>
                            <td className="text-right p-3">
                              <span className={service.errorRate > 1 ? 'text-red-600 font-medium' : service.errorRate > 0.1 ? 'text-yellow-600' : 'text-green-600'}>
                                {service.errorRate}%
                              </span>
                            </td>
                            <td className="text-right p-3">
                              <span className={fullService && fullService.uptime < 99 ? 'text-yellow-600 font-medium' : 'text-green-600'}>
                                {fullService?.uptime}%
                              </span>
                            </td>
                          </tr>
                        )
                      })}
                  </tbody>
                </table>
              </div>
            </CardContent>
          </Card>
        </TabsContent>
      </Tabs>
    </div>
  )
}
