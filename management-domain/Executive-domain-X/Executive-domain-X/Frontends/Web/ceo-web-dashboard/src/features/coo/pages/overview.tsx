import { Card, CardContent, CardHeader, CardTitle, CardDescription } from '@shared/components/ui/card'
import { Badge } from '@shared/components/ui/badge'
import { Button } from '@shared/components/ui/button'

export default function COOOverviewPage() {
  const healthScore = 91
  const period = 'Q1 2026'

  const businessUnits = [
    { name: 'Courier', color: 'border-l-emerald-500', kpi: '98.2% OTD', kpiLabel: 'On-Time Delivery', services: 42, issues: 0, status: 'healthy' },
    { name: 'E-Commerce', color: 'border-l-blue-500', kpi: '96.8%', kpiLabel: 'Fulfillment Rate', services: 76, issues: 1, status: 'degraded' },
    { name: 'Warehousing', color: 'border-l-purple-500', kpi: '94.1%', kpiLabel: 'Capacity Utilization', services: 28, issues: 0, status: 'healthy' },
    { name: 'Air Freight', color: 'border-l-amber-500', kpi: '91.5%', kpiLabel: 'Handling Rate', services: 18, issues: 0, status: 'healthy' },
    { name: 'Ocean', color: 'border-l-cyan-500', kpi: '88.3%', kpiLabel: 'Container Turnaround', services: 22, issues: 1, status: 'watch' },
    { name: 'Haulage', color: 'border-l-orange-500', kpi: '95.7%', kpiLabel: 'Trip Completion', services: 34, issues: 0, status: 'healthy' },
    { name: 'Procurement', color: 'border-l-pink-500', kpi: '92.4%', kpiLabel: 'PO Cycle Time', services: 15, issues: 0, status: 'healthy' },
    { name: 'Admin', color: 'border-l-slate-500', kpi: '99.1%', kpiLabel: 'SLA Compliance', services: 12, issues: 0, status: 'healthy' },
  ]

  const liveOps = [
    { label: 'Deliveries', count: '4,218', icon: '📦' },
    { label: 'Orders', count: '8,547', icon: '📋' },
    { label: 'Shipments', count: '2,891', icon: '🚛' },
    { label: 'Containers', count: '342', icon: '🏪' },
    { label: 'Loads', count: '187', icon: '⚖️' },
    { label: 'POs', count: '94', icon: '📄' },
  ]

  const slaCompliance = [
    { unit: 'Courier', target: 98, actual: 98.2 },
    { unit: 'E-Commerce', target: 95, actual: 96.8 },
    { unit: 'Warehousing', target: 93, actual: 94.1 },
    { unit: 'Air Freight', target: 90, actual: 91.5 },
    { unit: 'Ocean', target: 88, actual: 88.3 },
    { unit: 'Haulage', target: 94, actual: 95.7 },
    { unit: 'Procurement', target: 90, actual: 92.4 },
    { unit: 'Admin', target: 98, actual: 99.1 },
  ]

  const incidents = [
    { id: 'INC-2847', severity: 'P1', title: 'Lagos Hub Sorting System Outage', unit: 'Courier', status: 'Mitigating', eta: '45 min', color: 'border-l-red-500 bg-red-50/50 dark:bg-red-950/10' },
    { id: 'INC-2844', severity: 'P1', title: 'Warehouse Conveyor Belt Failure', unit: 'Warehousing', status: 'Investigating', eta: '2 hrs', color: 'border-l-red-500 bg-red-50/50 dark:bg-red-950/10' },
    { id: 'INC-2839', severity: 'P2', title: 'Port Clearance Delays - Lagos Terminal', unit: 'Ocean', status: 'Monitoring', eta: '4 hrs', color: 'border-l-amber-500 bg-amber-50/50 dark:bg-amber-950/10' },
  ]

  const volumeSnapshot = [
    { label: 'Total Shipments', value: '12,438', change: '+8.2%', positive: true },
    { label: 'Active Orders', value: '8,547', change: '+3.1%', positive: true },
    { label: 'Pending Deliveries', value: '4,218', change: '-1.4%', positive: true },
    { label: 'Open Tickets', value: '127', change: '+12%', positive: false },
    { label: 'On-Time Rate', value: '95.8%', change: '+0.3%', positive: true },
  ]

  return (
    <div className="space-y-5">
      <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
        <div>
          <h1 className="text-2xl sm:text-3xl font-bold text-slate-900 dark:text-white">Operational Health</h1>
          <p className="text-sm text-slate-500 dark:text-slate-400 mt-1">Real-time operational performance across all business units</p>
        </div>
        <Badge variant="outline" className="px-3 py-1.5 text-xs font-medium">{period}</Badge>
      </div>

      <Card className="overflow-hidden border-2 border-emerald-500/20">
        <div className="bg-gradient-to-r from-emerald-600 to-teal-600 p-4 sm:p-5">
          <div className="flex items-center gap-4">
            <div className="flex h-12 w-12 items-center justify-center rounded-full bg-white/20">
              <svg className="h-6 w-6 text-white" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z" /></svg>
            </div>
            <div>
              <p className="text-sm text-white/80 font-medium">Operational Health Score</p>
              <div className="flex items-baseline gap-2">
                <span className="text-3xl font-bold text-white">{healthScore}</span>
                <span className="text-lg text-white/60">/100</span>
                <Badge className="ml-2 bg-emerald-400/20 text-emerald-100 border-emerald-300/30 text-xs">{healthScore >= 90 ? 'Excellent' : healthScore >= 70 ? 'Good' : 'Needs Attention'}</Badge>
              </div>
            </div>
          </div>
        </div>
      </Card>

      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
        {businessUnits.map((bu) => (
          <Card key={bu.name} className={`border-l-4 ${bu.color}`}>
            <CardContent className="p-4">
              <div className="flex items-center justify-between mb-2">
                <p className="text-sm font-semibold text-slate-900 dark:text-white">{bu.name}</p>
                <Badge variant={bu.status === 'healthy' ? 'success' : bu.status === 'degraded' ? 'warning' : 'secondary'} className="text-[10px]">
                  {bu.status === 'healthy' ? 'Healthy' : bu.status === 'degraded' ? 'Degraded' : 'Watch'}
                </Badge>
              </div>
              <p className="text-xl font-bold text-slate-900 dark:text-white">{bu.kpi}</p>
              <p className="text-xs text-slate-500 dark:text-slate-400">{bu.kpiLabel}</p>
              <div className="flex items-center justify-between mt-3 pt-2 border-t border-slate-100 dark:border-slate-800">
                <span className="text-xs text-slate-500">{bu.services} services</span>
                {bu.issues > 0 && (
                  <Badge variant="destructive" className="text-[10px]">{bu.issues} issue{bu.issues > 1 ? 's' : ''}</Badge>
                )}
              </div>
            </CardContent>
          </Card>
        ))}
      </div>

      <div className="grid gap-4 sm:gap-5 lg:grid-cols-2">
        <Card>
          <CardHeader>
            <CardTitle className="text-lg">Live Operations Summary</CardTitle>
            <CardDescription>Current operational volumes</CardDescription>
          </CardHeader>
          <CardContent>
            <div className="grid grid-cols-2 sm:grid-cols-3 gap-4">
              {liveOps.map((op) => (
                <div key={op.label} className="p-3 rounded-lg bg-slate-50 dark:bg-slate-800/50 text-center">
                  <p className="text-lg mb-0.5">{op.icon}</p>
                  <p className="text-lg font-bold text-slate-900 dark:text-white">{op.count}</p>
                  <p className="text-xs text-slate-500 dark:text-slate-400">{op.label}</p>
                </div>
              ))}
            </div>
          </CardContent>
        </Card>

        <Card>
          <CardHeader>
            <CardTitle className="text-lg">Cross-Unit SLA Compliance</CardTitle>
            <CardDescription>Actual vs target SLA performance</CardDescription>
          </CardHeader>
          <CardContent>
            <div className="space-y-3">
              {slaCompliance.map((sla) => {
                const pct = Math.min((sla.actual / sla.target) * 100, 100)
                const met = sla.actual >= sla.target
                return (
                  <div key={sla.unit}>
                    <div className="flex items-center justify-between text-sm mb-1">
                      <span className="font-medium text-slate-700 dark:text-slate-300">{sla.unit}</span>
                      <span className={`text-xs font-semibold ${met ? 'text-emerald-600' : 'text-amber-600'}`}>{sla.actual}% / {sla.target}%</span>
                    </div>
                    <div className="h-2 bg-slate-100 dark:bg-slate-800 rounded-full overflow-hidden">
                      <div className={`h-full rounded-full ${met ? 'bg-emerald-500' : 'bg-amber-500'}`} style={{ width: `${pct}%` }} />
                    </div>
                  </div>
                )
              })}
            </div>
          </CardContent>
        </Card>
      </div>

      <Card>
        <CardHeader>
          <div className="flex items-center justify-between">
            <div>
              <CardTitle className="text-lg">Active Incidents</CardTitle>
              <CardDescription>Current operational incidents requiring attention</CardDescription>
            </div>
            <Badge variant="destructive">{incidents.length}</Badge>
          </div>
        </CardHeader>
        <CardContent>
          <div className="space-y-3">
            {incidents.map((inc) => (
              <div key={inc.id} className={`border-l-4 rounded-lg p-4 ${inc.color}`}>
                <div className="flex items-center justify-between">
                  <div className="flex items-center gap-3">
                    <Badge variant={inc.severity === 'P1' ? 'destructive' : 'warning'} className="text-xs font-bold">{inc.severity}</Badge>
                    <div>
                      <p className="text-sm font-semibold text-slate-900 dark:text-white">{inc.title}</p>
                      <div className="flex items-center gap-2 mt-0.5">
                        <Badge variant="outline" className="text-[10px]">{inc.unit}</Badge>
                        <span className="text-xs text-slate-500">{inc.status}</span>
                      </div>
                    </div>
                  </div>
                  <div className="text-right flex-shrink-0 ml-4">
                    <p className="text-xs text-slate-500">ETA</p>
                    <p className="text-sm font-semibold text-slate-900 dark:text-white">{inc.eta}</p>
                  </div>
                </div>
              </div>
            ))}
          </div>
          <Button variant="outline" className="w-full mt-4">View All Incidents</Button>
        </CardContent>
      </Card>

      <div className="grid grid-cols-2 sm:grid-cols-5 gap-3">
        {volumeSnapshot.map((v) => (
          <div key={v.label} className="bg-white dark:bg-slate-900 border border-slate-200 dark:border-slate-700 rounded-lg p-3 text-center">
            <p className="text-xs text-slate-500 dark:text-slate-400">{v.label}</p>
            <p className="text-lg font-bold text-slate-900 dark:text-white mt-1">{v.value}</p>
            <p className={`text-xs font-semibold ${v.positive ? 'text-emerald-600' : 'text-red-500'}`}>{v.change}</p>
          </div>
        ))}
      </div>
    </div>
  )
}
