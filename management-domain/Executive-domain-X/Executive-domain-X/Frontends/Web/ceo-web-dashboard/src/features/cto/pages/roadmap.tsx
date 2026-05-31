import { Card, CardContent, CardHeader, CardTitle, CardDescription } from '@shared/components/ui/card'
import { Badge } from '@shared/components/ui/badge'
import { Button } from '@shared/components/ui/button'

export default function CTORoadmapPage() {
  const quarters = [
    {
      quarter: 'Q1 2026',
      initiatives: [
        { name: 'Microservices Migration', status: 'In Progress', priority: 'High', owner: 'Platform Team', progress: 85 },
        { name: 'API Gateway v2', status: 'Completed', priority: 'High', owner: 'Integration Team', progress: 100 },
        { name: 'Real-time Analytics', status: 'In Progress', priority: 'Medium', owner: 'Data Team', progress: 60 },
      ],
    },
    {
      quarter: 'Q2 2026',
      initiatives: [
        { name: 'ML Pipeline Infrastructure', status: 'Planned', priority: 'High', owner: 'ML Team', progress: 0 },
        { name: 'Service Mesh Implementation', status: 'Planned', priority: 'Medium', owner: 'Platform Team', progress: 0 },
        { name: 'Edge Computing PoC', status: 'Planned', priority: 'Low', owner: 'Infrastructure Team', progress: 0 },
      ],
    },
    {
      quarter: 'Q3 2026',
      initiatives: [
        { name: 'Platform Consolidation', status: 'Planned', priority: 'High', owner: 'Architecture Team', progress: 0 },
        { name: 'Zero Trust Architecture', status: 'Planned', priority: 'High', owner: 'Security Team', progress: 0 },
      ],
    },
    {
      quarter: 'Q4 2026',
      initiatives: [
        { name: 'AI/ML Platform Scale', status: 'Planned', priority: 'High', owner: 'ML Team', progress: 0 },
        { name: 'Quantum-Ready Encryption', status: 'Planned', priority: 'Medium', owner: 'Security Team', progress: 0 },
      ],
    },
  ]

  const statusVariant = (s: string) =>
    s === 'Completed' ? 'success' : s === 'In Progress' ? 'info' : 'secondary'

  const priorityVariant = (p: string) =>
    p === 'High' ? 'destructive' : p === 'Medium' ? 'warning' : 'outline'

  return (
    <div className="space-y-5">
      <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
        <div>
          <h1 className="text-2xl sm:text-3xl font-bold text-slate-900 dark:text-white">Technology Roadmap</h1>
          <p className="text-sm text-slate-500 dark:text-slate-400 mt-1">Strategic initiatives and delivery timeline</p>
        </div>
        <Badge variant="outline" className="px-3 py-1.5 text-xs font-medium self-start">2026</Badge>
      </div>

      <div className="grid gap-5 lg:grid-cols-2">
        {quarters.map((q) => (
          <Card key={q.quarter}>
            <CardHeader>
              <CardTitle className="text-lg">{q.quarter}</CardTitle>
              <CardDescription>{q.initiatives.length} initiatives planned</CardDescription>
            </CardHeader>
            <CardContent>
              <div className="space-y-3">
                {q.initiatives.map((init) => (
                  <div key={init.name} className="p-3 rounded-lg border border-slate-200 dark:border-slate-700">
                    <div className="flex items-center justify-between mb-2">
                      <p className="text-sm font-semibold text-slate-900 dark:text-white">{init.name}</p>
                      <div className="flex items-center gap-1.5">
                        <Badge variant={priorityVariant(init.priority)} className="text-[10px]">{init.priority}</Badge>
                        <Badge variant={statusVariant(init.status)} className="text-[10px]">{init.status}</Badge>
                      </div>
                    </div>
                    <div className="flex items-center justify-between text-xs text-slate-500 dark:text-slate-400 mb-2">
                      <span>{init.owner}</span>
                      <span>{init.progress}%</span>
                    </div>
                    <div className="w-full h-1.5 bg-slate-100 dark:bg-slate-700 rounded-full overflow-hidden">
                      <div
                        className={`h-full rounded-full ${init.progress === 100 ? 'bg-emerald-500' : init.progress > 0 ? 'bg-blue-500' : 'bg-slate-300'}`}
                        style={{ width: `${init.progress}%` }}
                      />
                    </div>
                  </div>
                ))}
              </div>
            </CardContent>
          </Card>
        ))}
      </div>
    </div>
  )
}
