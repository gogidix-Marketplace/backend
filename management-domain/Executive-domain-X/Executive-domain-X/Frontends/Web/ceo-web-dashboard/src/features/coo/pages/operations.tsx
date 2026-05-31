import { Card, CardContent, CardHeader, CardTitle } from '@shared/components/ui/card'
import { Badge } from '@shared/components/ui/badge'

export default function COOOperationsPage() {
  const kpis = [
    { label: 'Active Orders', value: '1,247', change: '+5.3%', positive: true },
    { label: 'Fulfillment Rate', value: '96.8%', change: '+0.4%', positive: true },
    { label: 'Avg Delivery Time', value: '2.4h', change: '-0.2h', positive: true },
    { label: 'SLA Compliance', value: '98.2%', change: '+0.1%', positive: true },
    { label: 'Active Shipments', value: '892', change: '+12%', positive: true },
    { label: 'Returns Rate', value: '1.2%', change: '-0.3%', positive: true },
  ]

  const businessUnits = [
    { name: 'Courier', color: 'border-l-emerald-500', kpi: '98.2%', kpiLabel: 'OTD Rate', services: 42, active: 314 },
    { name: 'E-Commerce', color: 'border-l-blue-500', kpi: '96.8%', kpiLabel: 'Fulfillment %', services: 76, active: 428 },
    { name: 'Warehousing', color: 'border-l-purple-500', kpi: '94.1%', kpiLabel: 'Capacity Util.', services: 28, active: 67 },
    { name: 'Air Freight', color: 'border-l-amber-500', kpi: '91.5%', kpiLabel: 'Handling Rate', services: 18, active: 45 },
    { name: 'Ocean', color: 'border-l-cyan-500', kpi: '88.3%', kpiLabel: 'Container Turn.', services: 22, active: 38 },
    { name: 'Haulage', color: 'border-l-orange-500', kpi: '95.7%', kpiLabel: 'Trip Completion', services: 34, active: 52 },
    { name: 'Procurement', color: 'border-l-pink-500', kpi: '92.4%', kpiLabel: 'PO Cycle Time', services: 15, active: 21 },
    { name: 'Admin', color: 'border-l-slate-500', kpi: '99.1%', kpiLabel: 'SLA Compliance', services: 12, active: 8 },
  ]

  const hourlyVolume = [
    { hour: '08:00', volume: 142 },
    { hour: '09:00', volume: 198 },
    { hour: '10:00', volume: 231 },
    { hour: '11:00', volume: 256 },
    { hour: '12:00', volume: 189 },
    { hour: '13:00', volume: 214 },
    { hour: '14:00', volume: 247 },
    { hour: '15:00', volume: 223 },
  ]
  const maxVolume = Math.max(...hourlyVolume.map((h) => h.volume))

  const crossUnitSLA = [
    { unit: 'Courier', actual: 98.2, target: 98 },
    { unit: 'E-Commerce', actual: 96.8, target: 95 },
    { unit: 'Warehousing', actual: 94.1, target: 93 },
    { unit: 'Air Freight', actual: 91.5, target: 90 },
    { unit: 'Ocean', actual: 88.3, target: 88 },
    { unit: 'Haulage', actual: 95.7, target: 94 },
    { unit: 'Procurement', actual: 92.4, target: 90 },
    { unit: 'Admin', actual: 99.1, target: 98 },
  ]

  const bottlenecks = [
    { severity: 'Critical', description: 'Lagos Hub sorting conveyor overloaded — peak backlog 340 parcels', unit: 'Courier', eta: '45 min', color: 'border-l-red-500 bg-red-50/50 dark:bg-red-950/10' },
    { severity: 'High', description: 'Port customs clearance delayed due to new documentation requirements', unit: 'Ocean', eta: '3 hrs', color: 'border-l-amber-500 bg-amber-50/50 dark:bg-amber-950/10' },
    { severity: 'Medium', description: 'Warehouse B cold storage running at 98% capacity', unit: 'Warehousing', eta: '6 hrs', color: 'border-l-yellow-500 bg-yellow-50/50 dark:bg-yellow-950/10' },
    { severity: 'Low', description: 'Vendor onboarding queue exceeding 48h target', unit: 'Procurement', eta: '12 hrs', color: 'border-l-blue-500 bg-blue-50/50 dark:bg-blue-950/10' },
  ]

  return (
    <div className="space-y-5">
      <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
        <div>
          <h1 className="text-2xl sm:text-3xl font-bold text-slate-900 dark:text-white">Operations Center</h1>
          <p className="text-sm text-slate-500 dark:text-slate-400 mt-1">Real-time operational monitoring and control</p>
        </div>
        <div className="flex items-center gap-2">
          <span className="relative flex h-3 w-3">
            <span className="animate-ping absolute inline-flex h-full w-full rounded-full bg-emerald-400 opacity-75" />
            <span className="relative inline-flex rounded-full h-3 w-3 bg-emerald-500" />
          </span>
          <span className="text-sm font-medium text-emerald-600 dark:text-emerald-400">Live</span>
        </div>
      </div>

      <div className="grid grid-cols-2 sm:grid-cols-3 lg:grid-cols-6 gap-4">
        {kpis.map((kpi) => (
          <Card key={kpi.label}>
            <CardContent className="p-4 text-center">
              <p className="text-xs text-slate-500 dark:text-slate-400">{kpi.label}</p>
              <p className="text-xl font-bold text-slate-900 dark:text-white mt-1">{kpi.value}</p>
              <p className={`text-xs font-semibold ${kpi.positive ? 'text-emerald-600' : 'text-red-500'}`}>{kpi.change}</p>
            </CardContent>
          </Card>
        ))}
      </div>

      <div>
        <h2 className="text-lg font-semibold text-slate-900 dark:text-white mb-3">Operations by Business Unit</h2>
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
          {businessUnits.map((bu) => (
            <Card key={bu.name} className={`border-l-4 ${bu.color}`}>
              <CardContent className="p-4">
                <p className="text-sm font-semibold text-slate-900 dark:text-white">{bu.name}</p>
                <p className="text-xl font-bold text-slate-900 dark:text-white mt-1">{bu.kpi}</p>
                <p className="text-xs text-slate-500 dark:text-slate-400">{bu.kpiLabel}</p>
                <div className="flex items-center justify-between mt-3 pt-2 border-t border-slate-100 dark:border-slate-800">
                  <span className="text-xs text-slate-500">{bu.services} services</span>
                  <span className="text-xs font-medium text-slate-700 dark:text-slate-300">{bu.active} active</span>
                </div>
              </CardContent>
            </Card>
          ))}
        </div>
      </div>

      <div className="grid gap-4 sm:gap-5 lg:grid-cols-2">
        <Card>
          <CardHeader>
            <CardTitle className="text-lg">Real-time Volume</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="flex items-end gap-2 h-48">
              {hourlyVolume.map((h) => (
                <div key={h.hour} className="flex-1 flex flex-col items-center gap-1">
                  <span className="text-[10px] font-medium text-slate-600 dark:text-slate-400">{h.volume}</span>
                  <div className="w-full rounded-t bg-blue-500/80" style={{ height: `${(h.volume / maxVolume) * 140}px` }} />
                  <span className="text-[10px] text-slate-500 dark:text-slate-400">{h.hour}</span>
                </div>
              ))}
            </div>
          </CardContent>
        </Card>

        <Card>
          <CardHeader>
            <CardTitle className="text-lg">Cross-Unit SLA</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="space-y-3">
              {crossUnitSLA.map((sla) => {
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
            <CardTitle className="text-lg">Process Bottlenecks</CardTitle>
            <Badge variant="destructive">{bottlenecks.length}</Badge>
          </div>
        </CardHeader>
        <CardContent>
          <div className="space-y-3">
            {bottlenecks.map((b, i) => (
              <div key={i} className={`border-l-4 rounded-lg p-4 ${b.color}`}>
                <div className="flex items-center justify-between">
                  <div className="flex items-center gap-3">
                    <Badge variant={b.severity === 'Critical' ? 'destructive' : b.severity === 'High' ? 'warning' : b.severity === 'Medium' ? 'secondary' : 'info'} className="text-xs font-bold">{b.severity}</Badge>
                    <div>
                      <p className="text-sm font-semibold text-slate-900 dark:text-white">{b.description}</p>
                      <div className="flex items-center gap-2 mt-0.5">
                        <Badge variant="outline" className="text-[10px]">{b.unit}</Badge>
                      </div>
                    </div>
                  </div>
                  <div className="text-right flex-shrink-0 ml-4">
                    <p className="text-xs text-slate-500">ETA</p>
                    <p className="text-sm font-semibold text-slate-900 dark:text-white">{b.eta}</p>
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
