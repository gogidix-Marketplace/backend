import { Card, CardContent, CardHeader, CardTitle, CardDescription } from '@shared/components/ui/card'
import { Badge } from '@shared/components/ui/badge'
import { Button } from '@shared/components/ui/button'

export default function CTOOverviewPage() {
  const environment = 'Production'

  const kpis = [
    { title: 'Total Services', value: '272', subtitle: '271 healthy', change: '+3', positive: true, status: 'Nominal' },
    { title: 'Uptime', value: '99.94%', subtitle: '30-day rolling', change: '+0.02%', positive: true, status: 'SLA Met' },
    { title: 'P95 Latency', value: '340ms', subtitle: 'Cross-service avg', change: '-15ms', positive: true, status: 'Improving' },
    { title: 'Incidents Today', value: '1', subtitle: '0 P1 / 1 P2', change: '-3', positive: true, status: 'Low' },
    { title: 'Deployments Today', value: '12', subtitle: '96% success rate', change: '+2', positive: true, status: 'On Track' },
  ]

  const serviceHealth = [
    { unit: 'Courier', total: 42, healthy: 42, uptime: '99.97%', latency: '120ms', degraded: 0 },
    { unit: 'E-Commerce', total: 76, healthy: 75, uptime: '99.91%', latency: '280ms', degraded: 1 },
    { unit: 'Warehousing', total: 28, healthy: 28, uptime: '99.95%', latency: '150ms', degraded: 0 },
    { unit: 'Air Freight', total: 18, healthy: 18, uptime: '99.88%', latency: '200ms', degraded: 0 },
    { unit: 'Ocean', total: 22, healthy: 22, uptime: '99.92%', latency: '180ms', degraded: 0 },
    { unit: 'Haulage', total: 34, healthy: 34, uptime: '99.96%', latency: '130ms', degraded: 0 },
    { unit: 'Procurement', total: 15, healthy: 15, uptime: '99.99%', latency: '90ms', degraded: 0 },
    { unit: 'Admin', total: 12, healthy: 12, uptime: '99.98%', latency: '110ms', degraded: 0 },
  ]

  const pendingDeploys = [
    { service: 'search-service', type: 'hotfix', version: 'v3.2.1', description: 'Fix index corruption on bulk reindex', priority: 'P1', env: 'Production' },
    { service: 'route-optimization', type: 'feature', version: 'v2.0.0', description: 'ML-based route scoring with traffic data', priority: 'P2', env: 'Staging' },
    { service: 'dispatch-core', type: 'bugfix', version: 'v1.8.4', description: 'Fix driver assignment race condition', priority: 'P2', env: 'Production' },
  ]

  const infrastructure = [
    { name: 'Kafka', instances: 3, healthy: 3, status: 'healthy', icon: '●' },
    { name: 'MongoDB', instances: 4, healthy: 4, status: 'healthy', icon: '●' },
    { name: 'PostgreSQL', instances: 2, healthy: 2, status: 'healthy', icon: '●' },
    { name: 'Redis', instances: 5, healthy: 5, status: 'healthy', icon: '●' },
    { name: 'RabbitMQ', instances: 1, healthy: 1, status: 'healthy', icon: '●' },
    { name: 'Elasticsearch', instances: 1, healthy: 0, status: 'warning', icon: '⚠' },
  ]

  const securityPosture = [
    { name: 'GDPR', value: 'Pass', type: 'pass' },
    { name: 'SOC2', value: '98%', type: 'score' },
    { name: 'ISO27001', value: '95%', type: 'score' },
    { name: 'OWASP', value: '92%', type: 'score' },
    { name: 'Pen Test', value: 'Pass', type: 'pass' },
  ]

  return (
    <div className="space-y-5">
      <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
        <div>
          <h1 className="text-2xl sm:text-3xl font-bold text-slate-900 dark:text-white">Technology Health</h1>
          <p className="text-sm text-slate-500 dark:text-slate-400 mt-1">Infrastructure, services, and deployment status</p>
        </div>
        <div className="flex items-center gap-2">
          <Badge variant="outline" className="px-3 py-1.5 text-xs font-medium">
            <span className="inline-block w-2 h-2 rounded-full bg-emerald-500 mr-1.5" />
            {environment}
          </Badge>
        </div>
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
                <Badge variant="secondary" className="text-[10px]">{kpi.status}</Badge>
              </div>
            </CardContent>
          </Card>
        ))}
      </div>

      <Card>
        <CardHeader>
          <CardTitle className="text-lg">Service Health by Business Unit</CardTitle>
          <CardDescription>Real-time service status across all business units</CardDescription>
        </CardHeader>
        <CardContent>
          <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
            {serviceHealth.map((sh) => {
              const dots = Array.from({ length: Math.min(sh.total, 20) }, (_, i) =>
                i < Math.min(sh.healthy, 20) ? 'healthy' : 'degraded'
              )
              return (
                <div key={sh.unit} className="p-3 rounded-lg border border-slate-200 dark:border-slate-700">
                  <div className="flex items-center justify-between mb-2">
                    <p className="text-sm font-semibold text-slate-900 dark:text-white">{sh.unit}</p>
                    <Badge variant={sh.degraded > 0 ? 'warning' : 'success'} className="text-[10px]">
                      {sh.healthy}/{sh.total}
                    </Badge>
                  </div>
                  <div className="flex flex-wrap gap-0.5 mb-3">
                    {dots.map((dot, i) => (
                      <div key={i} className={`w-1.5 h-1.5 rounded-full ${dot === 'healthy' ? 'bg-emerald-500' : 'bg-amber-500'}`} />
                    ))}
                    {sh.total > 20 && <span className="text-[8px] text-slate-400 ml-1">+{sh.total - 20}</span>}
                  </div>
                  <div className="flex justify-between text-xs text-slate-500 dark:text-slate-400">
                    <span>Up: {sh.uptime}</span>
                    <span>Lat: {sh.latency}</span>
                  </div>
                  {sh.degraded > 0 && (
                    <p className="text-[10px] text-amber-600 dark:text-amber-400 mt-1 font-medium">{sh.degraded} degraded</p>
                  )}
                </div>
              )
            })}
          </div>
        </CardContent>
      </Card>

      <div className="grid gap-4 sm:gap-5 lg:grid-cols-2">
        <Card>
          <CardHeader>
            <CardTitle className="text-lg">Pending Deployments</CardTitle>
            <CardDescription>Deployments awaiting approval or in progress</CardDescription>
          </CardHeader>
          <CardContent>
            <div className="space-y-3">
              {pendingDeploys.map((d) => (
                <div key={`${d.service}-${d.version}`} className="flex items-center justify-between p-3 rounded-lg border border-slate-200 dark:border-slate-700">
                  <div className="flex-1 min-w-0">
                    <div className="flex items-center gap-2">
                      <p className="text-sm font-semibold text-slate-900 dark:text-white">{d.service}</p>
                      <Badge variant={d.type === 'hotfix' ? 'destructive' : d.type === 'feature' ? 'info' : 'warning'} className="text-[10px]">{d.type}</Badge>
                      <Badge variant="outline" className="text-[10px]">{d.version}</Badge>
                    </div>
                    <p className="text-xs text-slate-500 dark:text-slate-400 mt-0.5 truncate">{d.description}</p>
                  </div>
                  <div className="flex items-center gap-2 flex-shrink-0 ml-3">
                    <Badge variant="secondary" className="text-[10px]">{d.env}</Badge>
                    <Button variant="outline" size="sm">Review</Button>
                  </div>
                </div>
              ))}
            </div>
          </CardContent>
        </Card>

        <Card>
          <CardHeader>
            <CardTitle className="text-lg">Infrastructure Summary</CardTitle>
            <CardDescription>Core infrastructure component health</CardDescription>
          </CardHeader>
          <CardContent>
            <div className="space-y-3">
              {infrastructure.map((infra) => (
                <div key={infra.name} className="flex items-center justify-between p-3 rounded-lg border border-slate-200 dark:border-slate-700">
                  <div className="flex items-center gap-3">
                    <div className={`text-lg ${infra.status === 'healthy' ? 'text-emerald-500' : 'text-amber-500'}`}>
                      {infra.icon}
                    </div>
                    <div>
                      <p className="text-sm font-medium text-slate-900 dark:text-white">{infra.name}</p>
                      <p className="text-xs text-slate-500">{infra.instances} instance{infra.instances > 1 ? 's' : ''}</p>
                    </div>
                  </div>
                  <div className="flex items-center gap-2">
                    <div className="flex gap-1">
                      {Array.from({ length: infra.instances }, (_, i) => (
                        <div key={i} className={`w-2.5 h-2.5 rounded-full ${i < infra.instances ? (infra.status === 'healthy' ? 'bg-emerald-500' : 'bg-amber-500') : 'bg-slate-300'}`} />
                      ))}
                    </div>
                    <Badge variant={infra.status === 'healthy' ? 'success' : 'warning'} className="text-[10px]">
                      {infra.status === 'healthy' ? 'Healthy' : 'Warning'}
                    </Badge>
                  </div>
                </div>
              ))}
            </div>
          </CardContent>
        </Card>
      </div>

      <Card>
        <CardHeader>
          <CardTitle className="text-lg">Security Posture</CardTitle>
          <CardDescription>Compliance and security assessment status</CardDescription>
        </CardHeader>
        <CardContent>
          <div className="flex flex-wrap gap-3">
            {securityPosture.map((sp) => (
              <div key={sp.name} className="flex items-center gap-2 px-4 py-2 rounded-lg border border-slate-200 dark:border-slate-700 bg-slate-50 dark:bg-slate-800/50">
                <span className="text-sm font-medium text-slate-700 dark:text-slate-300">{sp.name}</span>
                <span className={`text-sm font-bold ${sp.type === 'pass' ? 'text-emerald-600' : 'text-blue-600'}`}>
                  {sp.type === 'pass' ? '●' : ''} {sp.value}
                </span>
              </div>
            ))}
          </div>
        </CardContent>
      </Card>
    </div>
  )
}
