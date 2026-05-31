import { useParams } from 'react-router-dom'
import { Card, CardContent, CardHeader, CardTitle } from '@shared/components/ui/card'
import { Badge } from '@shared/components/ui/badge'
import { Button } from '@shared/components/ui/button'
import { businessUnits } from '@shared/data/business-units'
import { allServices } from '@shared/data/service-health'
import { operationalKPIs } from '@shared/data/operations'
import type { BusinessUnitSlug } from '@shared/types'

const unitMeta: Record<string, { head: string; headTitle: string; region: string; established: string }> = {
  'courier': { head: 'Ahmed Hassan', headTitle: 'SVP Courier Operations', region: 'West Africa', established: '2021' },
  'ecommerce': { head: 'Lisa Wang', headTitle: 'SVP E-Commerce', region: 'Global', established: '2020' },
  'warehousing': { head: 'Carlos Mendez', headTitle: 'VP Warehouse Operations', region: 'Nigeria', established: '2021' },
  'air-freight': { head: 'Klaus Mueller', headTitle: 'VP Air Cargo', region: 'Europe-Africa', established: '2022' },
  'ocean-shipping': { head: 'Chen Wei', headTitle: 'VP Ocean Freight', region: 'Asia-Africa', established: '2022' },
  'haulage': { head: 'Obi Nwosu', headTitle: 'SVP Haulage Operations', region: 'West Africa', established: '2020' },
  'procurement': { head: 'Maria Santos', headTitle: 'VP Procurement', region: 'Global', established: '2021' },
  'admin-core': { head: 'Grace Okafor', headTitle: 'VP Administration', region: 'Nigeria', established: '2020' },
}

export default function BusinessUnitPage() {
  const { unitSlug } = useParams<{ unitSlug: string }>()
  const unit = businessUnits.find(u => u.id === unitSlug)
  const meta = unitMeta[unitSlug || '']
  const rev = { margin: Math.round((unit.revenue * 0.22) / unit.revenue * 100), netProfit: unit.revenue * 0.22, opex: unit.revenue * 0.78, revenue: unit.revenue }
  const services = allServices.filter(s => s.businessUnit === unitSlug)
  const ops = operationalKPIs.find(o => o.unitId === unitSlug)
  const healthyCount = services.filter(s => s.status === 'healthy').length

  if (!unit || !meta) {
    return (
      <div className="flex items-center justify-center h-64">
        <p className="text-slate-500">Business unit not found</p>
      </div>
    )
  }

  return (
    <div className="space-y-6 animate-fade-in-up">
      <div className="flex items-center justify-between">
        <div className="flex items-center gap-3">
          <div className="w-10 h-10 rounded-lg flex items-center justify-center" style={{ backgroundColor: unit.color + '20', color: unit.color }}>
            <span className="text-lg font-bold">{unit.name.charAt(0)}</span>
          </div>
          <div>
            <h1 className="text-2xl font-bold text-slate-900">{unit.name}</h1>
            <p className="text-sm text-slate-500">{meta.head} · {meta.headTitle} · {meta.region}</p>
          </div>
        </div>
        <div className="flex gap-2">
          <Badge variant="outline">{unit.serviceCount} services</Badge>
          <Badge variant="outline">Est. {meta.established}</Badge>
        </div>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
        <Card><CardContent className="p-4"><p className="text-sm text-slate-500">Revenue</p><p className="text-2xl font-bold" style={{ color: unit.color }}>${unit.revenue.toFixed(1)}M</p><p className="text-xs text-green-600">+{unit.revenueGrowth}%</p></CardContent></Card>
        <Card><CardContent className="p-4"><p className="text-sm text-slate-500">Margin</p><p className="text-2xl font-bold">{rev?.margin || 0}%</p></CardContent></Card>
        <Card><CardContent className="p-4"><p className="text-sm text-slate-500">Service Health</p><p className="text-2xl font-bold" style={{ color: healthyCount === services.length ? '#10B981' : '#F59E0B' }}>{healthyCount}/{services.length}</p></CardContent></Card>
        <Card><CardContent className="p-4"><p className="text-sm text-slate-500">Revenue Model</p><p className="text-sm font-medium">{unit.revenueModel}</p></CardContent></Card>
      </div>

      {ops && (
        <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
          <Card><CardContent className="p-4"><p className="text-sm text-slate-500">OTD Rate</p><p className="text-2xl font-bold">{ops.otdRate}%</p><p className="text-xs text-slate-400">Target: {ops.targetOtd}%</p></CardContent></Card>
          <Card><CardContent className="p-4"><p className="text-sm text-slate-500">SLA Compliance</p><p className="text-2xl font-bold">{ops.slaCompliance}%</p></CardContent></Card>
          <Card><CardContent className="p-4"><p className="text-sm text-slate-500">24h Volume</p><p className="text-2xl font-bold">{ops.volume24h.toLocaleString()}</p></CardContent></Card>
          <Card><CardContent className="p-4"><p className="text-sm text-slate-500">Active Incidents</p><p className="text-2xl font-bold" style={{ color: ops.activeIncidents > 0 ? '#EF4444' : '#10B981' }}>{ops.activeIncidents}</p></CardContent></Card>
        </div>
      )}

      {rev && (
        <Card>
          <CardHeader><CardTitle>P&L Summary</CardTitle></CardHeader>
          <CardContent>
            <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
              <div><p className="text-sm text-slate-500">Revenue</p><p className="text-lg font-bold">${rev.revenue.toFixed(1)}M</p></div>
              <div><p className="text-sm text-slate-500">OpEx</p><p className="text-lg font-bold">${rev.opex.toFixed(1)}M</p></div>
              <div><p className="text-sm text-slate-500">Net Profit</p><p className="text-lg font-bold text-green-600">${rev.netProfit.toFixed(1)}M</p></div>
              <div><p className="text-sm text-slate-500">Margin</p><p className="text-lg font-bold">{rev.margin}%</p></div>
            </div>
          </CardContent>
        </Card>
      )}

      <Card>
        <CardHeader className="flex flex-row items-center justify-between">
          <CardTitle>Services ({services.length})</CardTitle>
          <div className="flex gap-2">
            <div className="flex items-center gap-1"><div className="w-2 h-2 rounded-full bg-green-500" /><span className="text-xs">{healthyCount} healthy</span></div>
            <div className="flex items-center gap-1"><div className="w-2 h-2 rounded-full bg-amber-500" /><span className="text-xs">{services.filter(s => s.status === 'degraded').length} degraded</span></div>
          </div>
        </CardHeader>
        <CardContent>
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-2">
            {services.map(s => (
              <div key={s.name} className="flex items-center gap-3 p-3 rounded-lg border" style={{ borderLeftColor: s.status === 'healthy' ? '#10B981' : s.status === 'degraded' ? '#F59E0B' : '#EF4444', borderLeftWidth: 3 }}>
                <div className="flex-1 min-w-0">
                  <p className="font-medium text-sm truncate">{s.name}</p>
                  <p className="text-xs text-slate-400">:{s.port} · {s.region}</p>
                </div>
                <div className="text-right">
                  <p className="text-sm font-medium">{s.uptime}%</p>
                  <p className="text-xs text-slate-400">{s.responseTime}ms</p>
                </div>
              </div>
            ))}
          </div>
        </CardContent>
      </Card>
    </div>
  )
}
