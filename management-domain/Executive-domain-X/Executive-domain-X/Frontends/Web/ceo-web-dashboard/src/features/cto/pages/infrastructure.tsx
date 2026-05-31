import { Card, CardContent, CardHeader, CardTitle, CardDescription } from '@shared/components/ui/card'
import { Badge } from '@shared/components/ui/badge'
import { Button } from '@shared/components/ui/button'

export default function CTOInfrastructurePage() {
  const kpis = [
    { title: 'Uptime', value: '99.94%', subtitle: '30-day rolling', change: '+0.02%', positive: true },
    { title: 'CPU Avg', value: '42%', subtitle: 'Across all nodes', change: '-5%', positive: true },
    { title: 'Memory Avg', value: '58%', subtitle: 'Across all nodes', change: '+3%', positive: false },
    { title: 'Disk I/O', value: '23MB/s', subtitle: 'Average throughput', change: '-2MB/s', positive: true },
    { title: 'Network', value: '1.2Gbps', subtitle: 'Peak bandwidth', change: '+0.1Gbps', positive: true },
  ]

  const components = [
    { name: 'Kafka', clusters: 3, healthy: 3, status: 'healthy', metric: '1.2M msg/s' },
    { name: 'MongoDB', clusters: 4, healthy: 4, status: 'healthy', metric: '48k ops/s' },
    { name: 'PostgreSQL', clusters: 2, healthy: 2, status: 'healthy', metric: '12k qps' },
    { name: 'Redis', clusters: 5, healthy: 5, status: 'healthy', metric: '0.3ms latency' },
    { name: 'RabbitMQ', clusters: 1, healthy: 1, status: 'healthy', metric: '85k msg/s' },
    { name: 'Elasticsearch', clusters: 1, healthy: 1, status: 'healthy', metric: '3.2k idx/s' },
  ]

  const serviceHealth = [
    { unit: 'Courier', total: 42, healthy: 42, uptime: '99.97%', unhealthy: 0 },
    { unit: 'E-Commerce', total: 76, healthy: 75, uptime: '99.91%', unhealthy: 1 },
    { unit: 'Warehousing', total: 28, healthy: 28, uptime: '99.95%', unhealthy: 0 },
    { unit: 'Air Freight', total: 18, healthy: 18, uptime: '99.88%', unhealthy: 0 },
    { unit: 'Ocean', total: 22, healthy: 22, uptime: '99.92%', unhealthy: 0 },
    { unit: 'Haulage', total: 34, healthy: 34, uptime: '99.96%', unhealthy: 0 },
    { unit: 'Procurement', total: 15, healthy: 15, uptime: '99.99%', unhealthy: 0 },
    { unit: 'Admin', total: 12, healthy: 12, uptime: '99.98%', unhealthy: 0 },
  ]

  const capacity = [
    { resource: 'CPU', current: 42, projected: 68, unit: '%', color: 'bg-blue-500' },
    { resource: 'Memory', current: 58, projected: 78, unit: '%', color: 'bg-amber-500' },
    { resource: 'Storage', current: 35, projected: 55, unit: '%', color: 'bg-emerald-500' },
    { resource: 'Network', current: 28, projected: 52, unit: '%', color: 'bg-purple-500' },
  ]

  return (
    <div className="space-y-5">
      <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
        <div>
          <h1 className="text-2xl sm:text-3xl font-bold text-slate-900 dark:text-white">Infrastructure Overview</h1>
          <p className="text-sm text-slate-500 dark:text-slate-400 mt-1">System resources, components, and capacity planning</p>
        </div>
        <Badge variant="success" className="px-3 py-1.5 text-xs font-medium self-start">
          <span className="inline-block w-2 h-2 rounded-full bg-white mr-1.5" />
          Production
        </Badge>
      </div>

      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-5 gap-4">
        {kpis.map((kpi) => (
          <Card key={kpi.title} className="relative overflow-hidden">
            <CardContent className="p-4">
              <p className="text-xs font-medium text-slate-500 dark:text-slate-400 mb-1">{kpi.title}</p>
              <p className="text-xl font-bold text-slate-900 dark:text-white">{kpi.value}</p>
              <p className="text-xs text-slate-500 dark:text-slate-400 mt-0.5">{kpi.subtitle}</p>
              <div className="flex items-center gap-2 mt-2">
                <span className={`inline-flex items-center text-xs font-semibold ${kpi.positive ? 'text-emerald-600' : 'text-red-600'}`}>
                  {kpi.positive ? (
                    <svg className="w-3 h-3 mr-0.5" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M5 10l7-7m0 0l7 7m-7-7v18" /></svg>
                  ) : (
                    <svg className="w-3 h-3 mr-0.5" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M19 14l-7 7m0 0l-7-7m7 7V3" /></svg>
                  )}
                  {kpi.change}
                </span>
              </div>
            </CardContent>
          </Card>
        ))}
      </div>

      <Card>
        <CardHeader>
          <CardTitle className="text-lg">Infrastructure Components</CardTitle>
          <CardDescription>Core middleware and data platform health</CardDescription>
        </CardHeader>
        <CardContent>
          <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4">
            {components.map((c) => (
              <div key={c.name} className="p-4 rounded-lg border border-slate-200 dark:border-slate-700">
                <div className="flex items-center justify-between mb-3">
                  <p className="text-sm font-semibold text-slate-900 dark:text-white">{c.name}</p>
                  <Badge variant={c.status === 'healthy' ? 'success' : 'warning'} className="text-[10px]">
                    {c.status === 'healthy' ? 'Healthy' : 'Warning'}
                  </Badge>
                </div>
                <div className="flex items-center gap-1 mb-2">
                  {Array.from({ length: c.clusters }, (_, i) => (
                    <div key={i} className={`w-3 h-3 rounded-full ${i < c.healthy ? 'bg-emerald-500' : 'bg-red-500'}`} />
                  ))}
                  <span className="text-xs text-slate-500 dark:text-slate-400 ml-2">
                    {c.healthy}/{c.clusters} healthy
                  </span>
                </div>
                <p className="text-xs text-slate-500 dark:text-slate-400">Key metric: <span className="font-medium text-slate-700 dark:text-slate-300">{c.metric}</span></p>
              </div>
            ))}
          </div>
        </CardContent>
      </Card>

      <Card>
        <CardHeader>
          <CardTitle className="text-lg">Service Health by Unit</CardTitle>
          <CardDescription>Microservice status across business units</CardDescription>
        </CardHeader>
        <CardContent>
          <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
            {serviceHealth.map((sh) => {
              const total = Math.min(sh.total, 20)
              const dots = Array.from({ length: total }, (_, i) =>
                i < Math.min(sh.healthy, total) ? 'green' : (sh.unhealthy > 0 && i >= sh.healthy ? 'red' : 'gray')
              )
              return (
                <div key={sh.unit} className="p-3 rounded-lg border border-slate-200 dark:border-slate-700">
                  <div className="flex items-center justify-between mb-2">
                    <p className="text-sm font-semibold text-slate-900 dark:text-white">{sh.unit}</p>
                    <Badge variant={sh.unhealthy > 0 ? 'warning' : 'success'} className="text-[10px]">
                      {sh.healthy}/{sh.total}
                    </Badge>
                  </div>
                  <div className="flex flex-wrap gap-0.5 mb-3">
                    {dots.map((dot, i) => (
                      <div key={i} className={`w-1.5 h-1.5 rounded-full ${dot === 'green' ? 'bg-emerald-500' : dot === 'red' ? 'bg-red-500' : 'bg-slate-300'}`} />
                    ))}
                    {sh.total > 20 && <span className="text-[8px] text-slate-400 ml-1">+{sh.total - 20}</span>}
                  </div>
                  <div className="text-xs text-slate-500 dark:text-slate-400">
                    Uptime: <span className="font-medium text-slate-700 dark:text-slate-300">{sh.uptime}</span>
                  </div>
                </div>
              )
            })}
          </div>
        </CardContent>
      </Card>

      <Card>
        <CardHeader>
          <CardTitle className="text-lg">Capacity Planning</CardTitle>
          <CardDescription>Current vs projected resource utilization (6-month forecast)</CardDescription>
        </CardHeader>
        <CardContent>
          <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
            {capacity.map((cap) => (
              <div key={cap.resource} className="p-4 rounded-lg border border-slate-200 dark:border-slate-700">
                <div className="flex items-center justify-between mb-3">
                  <p className="text-sm font-semibold text-slate-900 dark:text-white">{cap.resource}</p>
                  <span className="text-xs text-slate-500">{cap.current}{cap.unit} → {cap.projected}{cap.unit}</span>
                </div>
                <div className="space-y-2">
                  <div>
                    <div className="flex justify-between text-[10px] text-slate-400 mb-0.5">
                      <span>Current</span><span>{cap.current}{cap.unit}</span>
                    </div>
                    <div className="w-full h-2 bg-slate-100 dark:bg-slate-700 rounded-full overflow-hidden">
                      <div className={`h-full ${cap.color} rounded-full`} style={{ width: `${cap.current}%` }} />
                    </div>
                  </div>
                  <div>
                    <div className="flex justify-between text-[10px] text-slate-400 mb-0.5">
                      <span>Projected</span><span>{cap.projected}{cap.unit}</span>
                    </div>
                    <div className="w-full h-2 bg-slate-100 dark:bg-slate-700 rounded-full overflow-hidden">
                      <div className={`h-full ${cap.color} rounded-full opacity-50`} style={{ width: `${cap.projected}%` }} />
                    </div>
                  </div>
                </div>
              </div>
            ))}
          </div>
        </CardContent>
      </Card>
    </div>
  )
}
