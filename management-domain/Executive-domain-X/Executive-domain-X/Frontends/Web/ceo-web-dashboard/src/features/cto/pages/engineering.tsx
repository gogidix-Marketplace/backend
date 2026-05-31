import { Card, CardContent, CardHeader, CardTitle, CardDescription } from '@shared/components/ui/card'
import { Badge } from '@shared/components/ui/badge'
import { Button } from '@shared/components/ui/button'

export default function CTOEngineeringPage() {
  const kpis = [
    { title: 'Deployments Today', value: '12', subtitle: '11 successful', change: '+2', positive: true },
    { title: 'Success Rate', value: '96%', subtitle: 'Last 30 days', change: '+1%', positive: true },
    { title: 'Build Time', value: '3.2min', subtitle: 'Average', change: '-0.4min', positive: true },
    { title: 'Open PRs', value: '47', subtitle: '12 awaiting review', change: '-5', positive: true },
    { title: 'Sprint Velocity', value: '89 pts', subtitle: 'Current sprint', change: '+12', positive: true },
  ]

  const deployments = [
    { service: 'search-service', version: 'v3.2.1', env: 'Production', status: 'deploying', progress: 72 },
    { service: 'route-optimization', version: 'v2.0.0', env: 'Staging', status: 'building', progress: 45 },
    { service: 'notification-engine', version: 'v1.5.3', env: 'QA', status: 'queued', progress: 0 },
  ]

  const pipeline = [
    { stage: 'Build', rate: '98%', passed: 476, failed: 10, color: 'bg-emerald-500' },
    { stage: 'Test', rate: '94%', passed: 452, failed: 34, color: 'bg-blue-500' },
    { stage: 'Stage', rate: '97%', passed: 440, failed: 12, color: 'bg-amber-500' },
    { stage: 'Prod', rate: '96%', passed: 428, failed: 12, color: 'bg-purple-500' },
  ]

  const techStack = [
    { name: 'Java 21', version: '21.0.2', usage: 84, type: 'Runtime' },
    { name: 'Node.js 20', version: '20.11.0', usage: 62, type: 'Runtime' },
    { name: 'React 18', version: '18.2.0', usage: 38, type: 'Frontend' },
    { name: 'Kafka', version: '3.6.1', usage: 92, type: 'Streaming' },
    { name: 'MongoDB', version: '7.0.4', usage: 76, type: 'Database' },
    { name: 'PostgreSQL', version: '16.1', usage: 54, type: 'Database' },
    { name: 'Redis', version: '7.2.4', usage: 88, type: 'Cache' },
    { name: 'RabbitMQ', version: '3.12.12', usage: 42, type: 'Messaging' },
  ]

  return (
    <div className="space-y-5">
      <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
        <div>
          <h1 className="text-2xl sm:text-3xl font-bold text-slate-900 dark:text-white">Engineering Hub</h1>
          <p className="text-sm text-slate-500 dark:text-slate-400 mt-1">Deployments, CI/CD pipelines, and technology stack</p>
        </div>
        <Button variant="outline" size="sm">View Pipeline</Button>
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

      <div className="grid gap-4 sm:gap-5 lg:grid-cols-2">
        <Card>
          <CardHeader>
            <CardTitle className="text-lg">Active Deployments</CardTitle>
            <CardDescription>Currently building or deploying</CardDescription>
          </CardHeader>
          <CardContent>
            <div className="space-y-3">
              {deployments.map((d) => (
                <div key={`${d.service}-${d.version}`} className="p-3 rounded-lg border border-slate-200 dark:border-slate-700">
                  <div className="flex items-center justify-between mb-2">
                    <div className="flex items-center gap-2">
                      <p className="text-sm font-semibold text-slate-900 dark:text-white">{d.service}</p>
                      <Badge variant="outline" className="text-[10px]">{d.version}</Badge>
                    </div>
                    <Badge variant={d.status === 'deploying' ? 'info' : d.status === 'building' ? 'warning' : 'secondary'} className="text-[10px]">
                      {d.status}
                    </Badge>
                  </div>
                  <div className="flex items-center justify-between mb-1">
                    <Badge variant="secondary" className="text-[10px]">{d.env}</Badge>
                    <span className="text-xs text-slate-500">{d.progress}%</span>
                  </div>
                  <div className="w-full h-1.5 bg-slate-100 dark:bg-slate-700 rounded-full overflow-hidden">
                    <div
                      className={`h-full rounded-full transition-all ${d.status === 'deploying' ? 'bg-blue-500' : d.status === 'building' ? 'bg-amber-500' : 'bg-slate-300'}`}
                      style={{ width: `${d.progress}%` }}
                    />
                  </div>
                </div>
              ))}
            </div>
          </CardContent>
        </Card>

        <Card>
          <CardHeader>
            <CardTitle className="text-lg">CI/CD Pipeline</CardTitle>
            <CardDescription>Pass rates across pipeline stages</CardDescription>
          </CardHeader>
          <CardContent>
            <div className="space-y-4">
              {pipeline.map((p, idx) => (
                <div key={p.stage}>
                  <div className="flex items-center gap-3">
                    <div className="flex items-center gap-2 w-16">
                      <div className={`w-3 h-3 rounded-full ${p.color}`} />
                      <span className="text-sm font-medium text-slate-900 dark:text-white">{p.stage}</span>
                    </div>
                    <div className="flex-1">
                      <div className="w-full h-3 bg-slate-100 dark:bg-slate-700 rounded-full overflow-hidden">
                        <div className={`h-full ${p.color} rounded-full`} style={{ width: p.rate }} />
                      </div>
                    </div>
                    <div className="flex items-center gap-2 w-24 justify-end">
                      <span className="text-sm font-bold text-slate-900 dark:text-white">{p.rate}</span>
                      <span className="text-[10px] text-slate-400">({p.passed}/{p.passed + p.failed})</span>
                    </div>
                  </div>
                  {idx < pipeline.length - 1 && (
                    <div className="flex justify-start ml-1.5 my-1">
                      <svg className="w-3 h-3 text-slate-300" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M19 14l-7 7m0 0l-7-7" /></svg>
                    </div>
                  )}
                </div>
              ))}
            </div>
          </CardContent>
        </Card>
      </div>

      <Card>
        <CardHeader>
          <CardTitle className="text-lg">Tech Stack Overview</CardTitle>
          <CardDescription>Technologies and versions across the platform</CardDescription>
        </CardHeader>
        <CardContent>
          <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-3">
            {techStack.map((t) => (
              <div key={t.name} className="p-3 rounded-lg border border-slate-200 dark:border-slate-700">
                <div className="flex items-center justify-between mb-2">
                  <p className="text-sm font-semibold text-slate-900 dark:text-white">{t.name}</p>
                  <Badge variant="outline" className="text-[10px]">{t.version}</Badge>
                </div>
                <div className="flex items-center justify-between text-xs text-slate-500 dark:text-slate-400 mb-2">
                  <span>{t.type}</span>
                  <span>{t.usage} services</span>
                </div>
                <div className="w-full h-1.5 bg-slate-100 dark:bg-slate-700 rounded-full overflow-hidden">
                  <div className="h-full bg-blue-500 rounded-full" style={{ width: `${t.usage}%` }} />
                </div>
              </div>
            ))}
          </div>
        </CardContent>
      </Card>
    </div>
  )
}
