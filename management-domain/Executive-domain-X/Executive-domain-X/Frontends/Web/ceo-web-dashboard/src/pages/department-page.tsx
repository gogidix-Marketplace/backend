import { useParams } from 'react-router-dom'
import { Card, CardContent, CardHeader, CardTitle } from '@shared/components/ui/card'
import { Badge } from '@shared/components/ui/badge'
import { Button } from '@shared/components/ui/button'
import { departments } from '@shared/data/departments'
import { allServices } from '@shared/data/service-health'
import type { DepartmentSlug } from '@shared/types'

const departmentMeta: Record<string, { name: string; color: string; head: string; headTitle: string; budget: string; teamSize: number }> = {
  'digital-marketing': { name: 'Digital Marketing', color: '#E91E63', head: 'Sarah Chen', headTitle: 'VP Marketing', budget: '$2.5M', teamSize: 42 },
  'customer-support': { name: 'Customer Support', color: '#00BCD4', head: 'James Okafor', headTitle: 'VP Customer Success', budget: '$1.8M', teamSize: 156 },
  'global-business-management': { name: 'Global Business Management', color: '#4CAF50', head: 'Elena Rodriguez', headTitle: 'SVP Business Dev', budget: '$3.5M', teamSize: 85 },
  'human-resource': { name: 'Human Resources', color: '#FF9800', head: 'David Kim', headTitle: 'CHRO', budget: '$2.1M', teamSize: 64 },
  'sales': { name: 'Sales', color: '#2196F3', head: 'Michael Torres', headTitle: 'CRO', budget: '$4.0M', teamSize: 128 },
  'system-administrator': { name: 'System Administration', color: '#9C27B0', head: 'Priya Sharma', headTitle: 'VP Infrastructure', budget: '$3.8M', teamSize: 45 },
  'finance': { name: 'Finance', color: '#FF5722', head: 'Robert Williams', headTitle: 'VP Finance', budget: '$2.5M', teamSize: 38 },
  'foundation-services': { name: 'Foundation Services', color: '#607D8B', head: 'Alex Johnson', headTitle: 'VP Platform', budget: '$1.5M', teamSize: 22 },
}

export default function DepartmentPage() {
  const { departmentSlug } = useParams<{ departmentSlug: string }>()
  const meta = departmentMeta[departmentSlug || '']
  const dept = departments.find(d => d.departmentId === departmentSlug)
  const services = allServices.filter(s => s.department === departmentSlug)

  if (!meta || !dept) {
    return (
      <div className="flex items-center justify-center h-64">
        <p className="text-slate-500">Department not found</p>
      </div>
    )
  }

  return (
    <div className="space-y-6 animate-fade-in-up">
      <div className="flex items-center justify-between">
        <div className="flex items-center gap-3">
          <div className="w-10 h-10 rounded-lg flex items-center justify-center" style={{ backgroundColor: meta.color + '20', color: meta.color }}>
            <span className="text-lg font-bold">{meta.name.charAt(0)}</span>
          </div>
          <div>
            <h1 className="text-2xl font-bold text-slate-900">{meta.name}</h1>
            <p className="text-sm text-slate-500">{meta.head} · {meta.headTitle}</p>
          </div>
        </div>
        <div className="flex gap-2">
          <Badge variant="outline" className="text-sm">{meta.teamSize} team members</Badge>
          <Badge variant="outline" className="text-sm">Budget: {meta.budget}</Badge>
        </div>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
        {[
          { label: 'Health Score', value: `${dept.healthScore}/100`, color: dept.healthScore > 90 ? '#10B981' : dept.healthScore > 80 ? '#F59E0B' : '#EF4444' },
          { label: 'Key Metric', value: dept.keyMetricValue, color: meta.color },
          { label: 'Services', value: `${dept.healthyServices}/${dept.serviceCount}`, color: '#0EA5E9' },
          { label: 'Budget Used', value: `${dept.budgetPercentUsed}%`, color: dept.budgetPercentUsed > 90 ? '#EF4444' : '#10B981' },
        ].map(kpi => (
          <Card key={kpi.label}>
            <CardContent className="p-4">
              <p className="text-sm text-slate-500">{kpi.label}</p>
              <p className="text-2xl font-bold" style={{ color: kpi.color }}>{kpi.value}</p>
            </CardContent>
          </Card>
        ))}
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        <Card>
          <CardHeader><CardTitle>Budget Utilization</CardTitle></CardHeader>
          <CardContent>
            <div className="space-y-3">
              <div className="flex justify-between text-sm">
                <span>Spent: ${dept.budgetSpent.toFixed(1)}M</span>
                <span>Allocated: ${dept.budgetAllocated.toFixed(1)}M</span>
              </div>
              <div className="w-full h-3 bg-slate-100 rounded-full overflow-hidden">
                <div className="h-full rounded-full" style={{ width: `${dept.budgetPercentUsed}%`, backgroundColor: meta.color }} />
              </div>
              <p className="text-sm text-slate-500">Remaining: ${(dept.budgetAllocated - dept.budgetSpent).toFixed(1)}M ({100 - dept.budgetPercentUsed}%)</p>
            </div>
          </CardContent>
        </Card>

        <Card>
          <CardHeader><CardTitle>Alerts & Approvals</CardTitle></CardHeader>
          <CardContent>
            <div className="space-y-3">
              <div className="flex justify-between items-center">
                <span className="text-sm text-slate-600">Active Alerts</span>
                <Badge variant={dept.alertCount > 5 ? 'destructive' : 'outline'}>{dept.alertCount}</Badge>
              </div>
              <div className="flex justify-between items-center">
                <span className="text-sm text-slate-600">Critical</span>
                <Badge variant={dept.criticalCount > 0 ? 'destructive' : 'outline'}>{dept.criticalCount}</Badge>
              </div>
              <div className="flex justify-between items-center">
                <span className="text-sm text-slate-600">Pending Approvals</span>
                <Badge variant="outline">{dept.pendingApprovals}</Badge>
              </div>
            </div>
          </CardContent>
        </Card>
      </div>

      <Card>
        <CardHeader className="flex flex-row items-center justify-between">
          <CardTitle>Services ({services.length})</CardTitle>
          <Badge variant="outline">{services.filter(s => s.status === 'healthy').length} healthy</Badge>
        </CardHeader>
        <CardContent>
          <div className="flex flex-wrap gap-2">
            {services.map(s => (
              <div
                key={s.name}
                className="flex items-center gap-2 px-3 py-2 rounded-lg border text-sm"
                style={{ borderLeftColor: s.status === 'healthy' ? '#10B981' : s.status === 'degraded' ? '#F59E0B' : '#EF4444', borderLeftWidth: 3 }}
              >
                <span className="font-medium">{s.name}</span>
                <span className="text-slate-400">:{s.port}</span>
                <Badge variant="outline" className="text-xs">{s.uptime}%</Badge>
              </div>
            ))}
          </div>
        </CardContent>
      </Card>
    </div>
  )
}
