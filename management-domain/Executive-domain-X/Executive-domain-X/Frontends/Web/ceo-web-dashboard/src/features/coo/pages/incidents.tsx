import { Card, CardContent, CardHeader, CardTitle } from '@shared/components/ui/card'
import { Badge } from '@shared/components/ui/badge'
import { Button } from '@shared/components/ui/button'

export default function COOIncidentsPage() {
  const filters = ['All', 'Critical', 'High', 'Medium', 'Low'] as const

  const summary = [
    { label: 'Active Incidents', value: '7', color: 'text-slate-900 dark:text-white' },
    { label: 'Critical', value: '1', color: 'text-red-600' },
    { label: 'Avg Resolution', value: '2.3h', color: 'text-slate-900 dark:text-white' },
    { label: 'MTTR', value: '45min', color: 'text-slate-900 dark:text-white' },
  ]

  const incidents = [
    { id: 'INC-2901', severity: 'P1', title: 'Lagos Hub Sorting System Outage', unit: 'Courier', status: 'Mitigating', time: '14 min ago', assignee: 'A. Okafor', eta: '45 min', severityColor: 'bg-red-500' },
    { id: 'INC-2898', severity: 'P2', title: 'Warehouse B Conveyor Belt Failure', unit: 'Warehousing', status: 'Investigating', time: '1h ago', assignee: 'B. Adeyemi', eta: '2 hrs', severityColor: 'bg-amber-500' },
    { id: 'INC-2895', severity: 'P2', title: 'Port Clearance Delays — Lagos Terminal', unit: 'Ocean', status: 'Monitoring', time: '2h ago', assignee: 'C. Nwosu', eta: '4 hrs', severityColor: 'bg-amber-500' },
    { id: 'INC-2892', severity: 'P3', title: 'E-Commerce Payment Gateway Latency', unit: 'E-Commerce', status: 'Mitigating', time: '3h ago', assignee: 'D. Balogun', eta: '1 hr', severityColor: 'bg-yellow-500' },
    { id: 'INC-2888', severity: 'P3', title: 'Air Cargo Manifest Sync Failure', unit: 'Air Freight', status: 'Investigating', time: '4h ago', assignee: 'E. Ibrahim', eta: '3 hrs', severityColor: 'bg-yellow-500' },
    { id: 'INC-2885', severity: 'P4', title: 'Procurement Portal Slow Response', unit: 'Procurement', status: 'Monitoring', time: '5h ago', assignee: 'F. Musa', eta: '6 hrs', severityColor: 'bg-blue-500' },
    { id: 'INC-2881', severity: 'P4', title: 'Admin Dashboard Intermittent 503 Errors', unit: 'Admin', status: 'Monitoring', time: '6h ago', assignee: 'G. Oyelaran', eta: '8 hrs', severityColor: 'bg-blue-500' },
  ]

  const timeline = [
    { id: 'INC-2879', title: 'Courier App Auth Service Restored', resolved: '12 min ago', duration: '38 min' },
    { id: 'INC-2876', title: 'Ocean Booking Engine Failover Complete', resolved: '1h ago', duration: '1h 12min' },
    { id: 'INC-2872', title: 'Warehouse RFID Scanner Firmware Updated', resolved: '3h ago', duration: '2h 45min' },
    { id: 'INC-2869', title: 'Haulage Route Optimization Service Restored', resolved: '5h ago', duration: '55 min' },
    { id: 'INC-2864', title: 'E-Commerce Inventory Sync Resolved', resolved: '8h ago', duration: '1h 30min' },
  ]

  const escalations = [
    { title: 'Recurring P1 at Lagos Hub — 3rd outage this week', unit: 'Courier', reason: 'Pattern exceeds SLA breach threshold', time: '30 min ago' },
    { title: 'Ocean port delays impacting 12+ shipments', unit: 'Ocean', reason: 'Revenue impact exceeds $50K', time: '2h ago' },
    { title: 'Warehouse B structural issue — safety concern', unit: 'Warehousing', reason: 'Safety compliance required', time: '4h ago' },
  ]

  return (
    <div className="space-y-5">
      <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
        <div>
          <h1 className="text-2xl sm:text-3xl font-bold text-slate-900 dark:text-white">Incident Management</h1>
          <p className="text-sm text-slate-500 dark:text-slate-400 mt-1">Track, manage, and resolve operational incidents</p>
        </div>
        <div className="flex gap-2">
          {filters.map((f) => (
            <Button key={f} variant={f === 'All' ? 'default' : 'outline'} size="sm">{f}</Button>
          ))}
        </div>
      </div>

      <div className="grid grid-cols-2 sm:grid-cols-4 gap-4">
        {summary.map((s) => (
          <Card key={s.label}>
            <CardContent className="p-4 text-center">
              <p className="text-xs text-slate-500 dark:text-slate-400">{s.label}</p>
              <p className={`text-2xl font-bold mt-1 ${s.color}`}>{s.value}</p>
            </CardContent>
          </Card>
        ))}
      </div>

      <Card>
        <CardHeader>
          <div className="flex items-center justify-between">
            <CardTitle className="text-lg">Active Incidents</CardTitle>
            <Badge variant="destructive">{incidents.length}</Badge>
          </div>
        </CardHeader>
        <CardContent>
          <div className="space-y-3">
            {incidents.map((inc) => (
              <div key={inc.id} className="flex items-center gap-3 p-3 rounded-lg bg-slate-50 dark:bg-slate-800/50">
                <Badge variant={inc.severity === 'P1' ? 'destructive' : inc.severity === 'P2' ? 'warning' : inc.severity === 'P3' ? 'secondary' : 'info'} className="text-xs font-bold">{inc.severity}</Badge>
                <div className="flex-1 min-w-0">
                  <p className="text-sm font-semibold text-slate-900 dark:text-white truncate">{inc.title}</p>
                  <div className="flex items-center gap-2 mt-0.5">
                    <Badge variant="outline" className="text-[10px]">{inc.unit}</Badge>
                    <Badge variant="outline" className="text-[10px]">{inc.status}</Badge>
                  </div>
                </div>
                <div className="text-right flex-shrink-0 hidden sm:block">
                  <p className="text-[10px] text-slate-500">{inc.time}</p>
                  <p className="text-xs text-slate-600 dark:text-slate-400">{inc.assignee}</p>
                  <p className="text-xs font-semibold text-slate-900 dark:text-white">ETA: {inc.eta}</p>
                </div>
              </div>
            ))}
          </div>
        </CardContent>
      </Card>

      <div className="grid gap-4 sm:gap-5 lg:grid-cols-2">
        <Card>
          <CardHeader>
            <CardTitle className="text-lg">Incident Timeline</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="space-y-3">
              {timeline.map((t, i) => (
                <div key={t.id} className="flex items-start gap-3">
                  <div className="flex flex-col items-center">
                    <div className="h-2 w-2 rounded-full bg-emerald-500 mt-1.5" />
                    {i < timeline.length - 1 && <div className="w-px h-8 bg-slate-200 dark:bg-slate-700" />}
                  </div>
                  <div className="flex-1">
                    <p className="text-sm font-medium text-slate-900 dark:text-white">{t.title}</p>
                    <div className="flex items-center gap-2 mt-0.5">
                      <span className="text-xs text-slate-500">{t.resolved}</span>
                      <Badge variant="success" className="text-[10px]">{t.duration}</Badge>
                    </div>
                  </div>
                </div>
              ))}
            </div>
          </CardContent>
        </Card>

        <Card>
          <CardHeader>
            <div className="flex items-center justify-between">
              <CardTitle className="text-lg">Escalation Queue</CardTitle>
              <Badge variant="warning">{escalations.length}</Badge>
            </div>
          </CardHeader>
          <CardContent>
            <div className="space-y-3">
              {escalations.map((e, i) => (
                <div key={i} className="border-l-4 border-l-amber-500 bg-amber-50/50 dark:bg-amber-950/10 rounded-lg p-4">
                  <p className="text-sm font-semibold text-slate-900 dark:text-white">{e.title}</p>
                  <div className="flex items-center gap-2 mt-1">
                    <Badge variant="outline" className="text-[10px]">{e.unit}</Badge>
                    <span className="text-xs text-slate-500">{e.reason}</span>
                  </div>
                  <p className="text-xs text-slate-500 mt-1">{e.time}</p>
                </div>
              ))}
            </div>
          </CardContent>
        </Card>
      </div>
    </div>
  )
}
