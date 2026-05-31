import { Card, CardContent, CardHeader, CardTitle } from '@shared/components/ui/card'
import { Badge } from '@shared/components/ui/badge'

export default function COOResourcesPage() {
  const summary = [
    { label: 'Total Staff', value: '2,847', change: '+23 this month', positive: true },
    { label: 'Active Shifts', value: '1,892', change: '66.4% of workforce', positive: true },
    { label: 'Utilization', value: '87.3%', change: '+2.1% vs last week', positive: true },
    { label: 'Overtime', value: '12.1%', change: '-0.8% vs last week', positive: true },
  ]

  const departments = [
    { name: 'Courier Operations', headcount: 842, active: 789, onShift: 612, utilization: 93.7 },
    { name: 'E-Commerce Fulfillment', headcount: 534, active: 498, onShift: 387, utilization: 88.2 },
    { name: 'Warehousing', headcount: 421, active: 395, onShift: 312, utilization: 91.4 },
    { name: 'Air Freight', headcount: 287, active: 261, onShift: 198, utilization: 84.7 },
    { name: 'Ocean Freight', headcount: 213, active: 194, onShift: 147, utilization: 82.1 },
    { name: 'Haulage', headcount: 198, active: 182, onShift: 138, utilization: 86.9 },
    { name: 'Procurement', headcount: 189, active: 176, onShift: 62, utilization: 78.5 },
    { name: 'Admin & Support', headcount: 163, active: 152, onShift: 36, utilization: 74.2 },
  ]

  const allocation = [
    { unit: 'Courier', allocated: 842, needed: 810, color: 'border-l-emerald-500' },
    { unit: 'E-Commerce', allocated: 534, needed: 580, color: 'border-l-blue-500' },
    { unit: 'Warehousing', allocated: 421, needed: 400, color: 'border-l-purple-500' },
    { unit: 'Air Freight', allocated: 287, needed: 310, color: 'border-l-amber-500' },
    { unit: 'Ocean', allocated: 213, needed: 240, color: 'border-l-cyan-500' },
    { unit: 'Haulage', allocated: 198, needed: 190, color: 'border-l-orange-500' },
    { unit: 'Procurement', allocated: 189, needed: 175, color: 'border-l-pink-500' },
    { unit: 'Admin', allocated: 163, needed: 160, color: 'border-l-slate-500' },
  ]

  const shiftChanges = [
    { date: 'Apr 23, 2026', department: 'Courier Operations', change: 'Night shift extended by 2 hours for peak volume' },
    { date: 'Apr 24, 2026', department: 'Warehousing', change: 'Additional 15 temp workers for inventory audit' },
    { date: 'Apr 25, 2026', department: 'E-Commerce Fulfillment', change: 'Weekend shift pattern change — 4 teams rotating' },
    { date: 'Apr 26, 2026', department: 'Air Freight', change: 'Morning crew increased from 18 to 24 handlers' },
  ]

  return (
    <div className="space-y-5">
      <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
        <div>
          <h1 className="text-2xl sm:text-3xl font-bold text-slate-900 dark:text-white">Resource Management</h1>
          <p className="text-sm text-slate-500 dark:text-slate-400 mt-1">Workforce allocation, shifts, and utilization tracking</p>
        </div>
      </div>

      <div className="grid grid-cols-2 sm:grid-cols-4 gap-4">
        {summary.map((s) => (
          <Card key={s.label}>
            <CardContent className="p-4">
              <p className="text-xs text-slate-500 dark:text-slate-400">{s.label}</p>
              <p className="text-2xl font-bold text-slate-900 dark:text-white mt-1">{s.value}</p>
              <p className={`text-xs font-medium ${s.positive ? 'text-emerald-600' : 'text-red-500'}`}>{s.change}</p>
            </CardContent>
          </Card>
        ))}
      </div>

      <Card>
        <CardHeader>
          <CardTitle className="text-lg">Staff by Department</CardTitle>
        </CardHeader>
        <CardContent>
          <div className="overflow-x-auto">
            <table className="w-full text-sm">
              <thead>
                <tr className="border-b border-slate-200 dark:border-slate-700">
                  <th className="text-left py-3 px-2 font-semibold text-slate-600 dark:text-slate-400">Department</th>
                  <th className="text-right py-3 px-2 font-semibold text-slate-600 dark:text-slate-400">Headcount</th>
                  <th className="text-right py-3 px-2 font-semibold text-slate-600 dark:text-slate-400">Active</th>
                  <th className="text-right py-3 px-2 font-semibold text-slate-600 dark:text-slate-400">On Shift</th>
                  <th className="text-right py-3 px-2 font-semibold text-slate-600 dark:text-slate-400">Utilization</th>
                </tr>
              </thead>
              <tbody>
                {departments.map((d) => (
                  <tr key={d.name} className="border-b border-slate-100 dark:border-slate-800">
                    <td className="py-3 px-2 font-medium text-slate-900 dark:text-white">{d.name}</td>
                    <td className="py-3 px-2 text-right text-slate-700 dark:text-slate-300">{d.headcount.toLocaleString()}</td>
                    <td className="py-3 px-2 text-right text-slate-700 dark:text-slate-300">{d.active.toLocaleString()}</td>
                    <td className="py-3 px-2 text-right text-slate-700 dark:text-slate-300">{d.onShift.toLocaleString()}</td>
                    <td className="py-3 px-2 text-right">
                      <span className={`text-xs font-semibold ${d.utilization >= 90 ? 'text-emerald-600' : d.utilization >= 80 ? 'text-amber-600' : 'text-red-500'}`}>{d.utilization}%</span>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </CardContent>
      </Card>

      <Card>
        <CardHeader>
          <CardTitle className="text-lg">Resource Allocation</CardTitle>
        </CardHeader>
        <CardContent>
          <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-3">
            {allocation.map((a) => {
              const gap = a.allocated - a.needed
              return (
                <div key={a.unit} className={`border-l-4 rounded-lg p-3 ${a.color} bg-white dark:bg-slate-900 shadow-sm`}>
                  <p className="text-sm font-semibold text-slate-900 dark:text-white">{a.unit}</p>
                  <div className="flex items-center justify-between mt-2">
                    <span className="text-xs text-slate-500">Allocated: <span className="font-medium text-slate-700 dark:text-slate-300">{a.allocated}</span></span>
                    <span className="text-xs text-slate-500">Needed: <span className="font-medium text-slate-700 dark:text-slate-300">{a.needed}</span></span>
                  </div>
                  <div className="mt-2">
                    <Badge variant={gap >= 0 ? 'success' : 'destructive'} className="text-[10px]">
                      {gap >= 0 ? `+${gap} surplus` : `${gap} deficit`}
                    </Badge>
                  </div>
                </div>
              )
            })}
          </div>
        </CardContent>
      </Card>

      <Card>
        <CardHeader>
          <CardTitle className="text-lg">Upcoming Shift Changes</CardTitle>
        </CardHeader>
        <CardContent>
          <div className="space-y-3">
            {shiftChanges.map((s, i) => (
              <div key={i} className="flex items-start gap-3 p-3 rounded-lg bg-slate-50 dark:bg-slate-800/50">
                <div className="text-right flex-shrink-0 w-24">
                  <p className="text-xs font-semibold text-slate-900 dark:text-white">{s.date}</p>
                </div>
                <div className="flex-1">
                  <p className="text-sm font-medium text-slate-900 dark:text-white">{s.change}</p>
                  <Badge variant="outline" className="text-[10px] mt-1">{s.department}</Badge>
                </div>
              </div>
            ))}
          </div>
        </CardContent>
      </Card>
    </div>
  )
}
