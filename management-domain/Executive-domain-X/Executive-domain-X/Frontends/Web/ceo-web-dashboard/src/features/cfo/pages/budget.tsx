import { useState } from 'react'
import { Card, CardContent, CardHeader, CardTitle, CardDescription } from '@shared/components/ui/card'
import { Badge } from '@shared/components/ui/badge'
import { Button } from '@shared/components/ui/button'

export default function CFOBudgetPage() {
  const [fiscalYear, setFiscalYear] = useState(2026)

  const summaryCards = [
    { title: 'Total Budget', value: '$20.1M', sub: 'FY 2026', color: 'border-l-blue-500' },
    { title: 'Spent', value: '$16.8M', sub: '83.6% utilized', color: 'border-l-amber-500' },
    { title: 'Remaining', value: '$3.3M', sub: '16.4% available', color: 'border-l-emerald-500' },
    { title: 'Variance', value: '-3.2%', sub: 'Under budget', color: 'border-l-purple-500' },
    { title: 'Forecast EOY', value: '$19.5M', sub: 'Projected total', color: 'border-l-slate-500' },
  ]

  const departments = [
    { name: 'Marketing', budget: '$2.5M', spent: 82, amount: '$2.05M' },
    { name: 'Support', budget: '$1.8M', spent: 92, amount: '$1.66M' },
    { name: 'Global Biz', budget: '$3.5M', spent: 68, amount: '$2.38M' },
    { name: 'HR', budget: '$2.1M', spent: 85, amount: '$1.79M' },
    { name: 'Sales', budget: '$4.0M', spent: 88, amount: '$3.52M' },
    { name: 'SysAdmin', budget: '$3.8M', spent: 72, amount: '$2.74M' },
    { name: 'Finance', budget: '$2.5M', spent: 80, amount: '$2.00M' },
    { name: 'Foundation', budget: '$1.5M', spent: 90, amount: '$1.35M' },
  ]

  const budgetVsActual = [
    { month: 'Jan', budget: 3200, actual: 3050 },
    { month: 'Feb', budget: 3100, actual: 3180 },
    { month: 'Mar', budget: 3300, actual: 3150 },
    { month: 'Apr', budget: 3400, actual: 3500 },
    { month: 'May', budget: 3350, actual: 3200 },
    { month: 'Jun', budget: 3450, actual: 3720 },
  ]

  const topVariances = [
    { dept: 'Sales', variance: '+$280K', type: 'over' },
    { dept: 'Support', variance: '+$120K', type: 'over' },
    { dept: 'SysAdmin', variance: '-$310K', type: 'under' },
    { dept: 'Global Biz', variance: '-$420K', type: 'under' },
    { dept: 'Foundation', variance: '-$95K', type: 'under' },
  ]

  const pendingApprovals = [
    { id: 'BA-001', title: 'Q3 Marketing Campaign', dept: 'Marketing', amount: '$450K', deptColor: 'bg-pink-100 text-pink-700' },
    { id: 'BA-002', title: 'Server Infrastructure Upgrade', dept: 'SysAdmin', amount: '$890K', deptColor: 'bg-slate-100 text-slate-700' },
    { id: 'BA-003', title: 'Annual Training Program', dept: 'HR', amount: '$210K', deptColor: 'bg-violet-100 text-violet-700' },
    { id: 'BA-004', title: 'New Market Research', dept: 'Global Biz', amount: '$175K', deptColor: 'bg-sky-100 text-sky-700' },
  ]

  const getBarColor = (pct: number) => {
    if (pct >= 90) return 'bg-red-500'
    if (pct >= 80) return 'bg-amber-500'
    if (pct >= 70) return 'bg-blue-500'
    return 'bg-emerald-500'
  }

  const maxBudget = Math.max(...budgetVsActual.map(m => Math.max(m.budget, m.actual)))

  return (
    <div className="space-y-5">
      <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
        <div>
          <h1 className="text-2xl sm:text-3xl font-bold text-slate-900 dark:text-white">Budget Management</h1>
          <p className="text-sm text-slate-500 dark:text-slate-400 mt-1">Track, manage, and optimize departmental budgets</p>
        </div>
        <div className="flex items-center gap-2">
          <select
            value={fiscalYear}
            onChange={(e) => setFiscalYear(Number(e.target.value))}
            className="px-3 py-1.5 rounded-lg border border-slate-200 dark:border-slate-700 bg-white dark:bg-slate-800 text-sm"
          >
            <option value={2025}>FY 2025</option>
            <option value={2026}>FY 2026</option>
            <option value={2027}>FY 2027</option>
          </select>
          <Button variant="outline" size="sm">Export</Button>
        </div>
      </div>

      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-5 gap-4">
        {summaryCards.map((card) => (
          <Card key={card.title} className={`border-l-4 ${card.color}`}>
            <CardContent className="p-4">
              <p className="text-xs font-medium text-slate-500 dark:text-slate-400">{card.title}</p>
              <p className="text-xl font-bold text-slate-900 dark:text-white mt-1">{card.value}</p>
              <p className="text-xs text-slate-500 dark:text-slate-400 mt-1">{card.sub}</p>
            </CardContent>
          </Card>
        ))}
      </div>

      <Card>
        <CardHeader>
          <CardTitle className="text-lg">Budget by Department</CardTitle>
          <CardDescription>Allocation and utilization across all departments</CardDescription>
        </CardHeader>
        <CardContent>
          <div className="overflow-x-auto">
            <table className="w-full text-sm">
              <thead>
                <tr className="border-b border-slate-200 dark:border-slate-700">
                  <th className="text-left py-2 px-3 font-semibold text-slate-600 dark:text-slate-300">Department</th>
                  <th className="text-right py-2 px-3 font-semibold text-slate-600 dark:text-slate-300">Budget</th>
                  <th className="text-right py-2 px-3 font-semibold text-slate-600 dark:text-slate-300">Spent</th>
                  <th className="text-left py-2 px-3 font-semibold text-slate-600 dark:text-slate-300 w-40">Progress</th>
                  <th className="text-right py-2 px-3 font-semibold text-slate-600 dark:text-slate-300">Used</th>
                </tr>
              </thead>
              <tbody>
                {departments.map((dept) => (
                  <tr key={dept.name} className="border-b border-slate-100 dark:border-slate-800 hover:bg-slate-50 dark:hover:bg-slate-800/50">
                    <td className="py-2.5 px-3 font-medium text-slate-900 dark:text-white">{dept.name}</td>
                    <td className="text-right py-2.5 px-3 text-slate-700 dark:text-slate-300">{dept.budget}</td>
                    <td className="text-right py-2.5 px-3 text-slate-700 dark:text-slate-300">{dept.amount}</td>
                    <td className="py-2.5 px-3">
                      <div className="h-2 bg-slate-100 dark:bg-slate-800 rounded-full overflow-hidden">
                        <div className={`h-full rounded-full ${getBarColor(dept.spent)}`} style={{ width: `${dept.spent}%` }} />
                      </div>
                    </td>
                    <td className="text-right py-2.5 px-3">
                      <Badge variant={dept.spent >= 90 ? 'destructive' : dept.spent >= 80 ? 'warning' : 'secondary'} className="text-[10px]">
                        {dept.spent}%
                      </Badge>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </CardContent>
      </Card>

      <div className="grid gap-4 sm:gap-5 lg:grid-cols-2">
        <Card>
          <CardHeader>
            <CardTitle className="text-lg">Budget vs Actual</CardTitle>
            <CardDescription>Monthly comparison for H1 {fiscalYear}</CardDescription>
          </CardHeader>
          <CardContent>
            <div className="space-y-4">
              {budgetVsActual.map((item) => (
                <div key={item.month} className="space-y-1.5">
                  <div className="flex items-center justify-between text-xs">
                    <span className="font-medium text-slate-700 dark:text-slate-300 w-8">{item.month}</span>
                    <div className="flex items-center gap-3 text-slate-500">
                      <span>Budget: ${(item.budget / 1000).toFixed(1)}K</span>
                      <span>Actual: ${(item.actual / 1000).toFixed(1)}K</span>
                    </div>
                  </div>
                  <div className="relative h-5 bg-slate-100 dark:bg-slate-800 rounded overflow-hidden">
                    <div
                      className="absolute top-0 left-0 h-full bg-blue-200 dark:bg-blue-900 rounded"
                      style={{ width: `${(item.budget / maxBudget) * 100}%` }}
                    />
                    <div
                      className={`absolute top-0 left-0 h-full rounded ${item.actual > item.budget ? 'bg-red-400' : 'bg-blue-500'}`}
                      style={{ width: `${(item.actual / maxBudget) * 100}%`, opacity: 0.8 }}
                    />
                  </div>
                </div>
              ))}
              <div className="flex items-center gap-4 pt-2 border-t border-slate-100 dark:border-slate-800">
                <div className="flex items-center gap-1.5 text-xs text-slate-500">
                  <div className="w-3 h-3 rounded bg-blue-200 dark:bg-blue-900" /> Budget
                </div>
                <div className="flex items-center gap-1.5 text-xs text-slate-500">
                  <div className="w-3 h-3 rounded bg-blue-500 opacity-80" /> Actual
                </div>
              </div>
            </div>
          </CardContent>
        </Card>

        <Card>
          <CardHeader>
            <CardTitle className="text-lg">Top Variances</CardTitle>
            <CardDescription>Largest budget deviations this period</CardDescription>
          </CardHeader>
          <CardContent>
            <div className="space-y-3">
              {topVariances.map((item) => (
                <div key={item.dept} className="flex items-center justify-between p-3 rounded-lg border border-slate-200 dark:border-slate-700 hover:bg-slate-50 dark:hover:bg-slate-800/50">
                  <div>
                    <p className="text-sm font-medium text-slate-900 dark:text-white">{item.dept}</p>
                    <p className="text-xs text-slate-500 dark:text-slate-400">
                      {item.type === 'over' ? 'Over budget' : 'Under budget'}
                    </p>
                  </div>
                  <Badge variant={item.type === 'over' ? 'destructive' : 'success'} className="text-[10px]">
                    {item.variance}
                  </Badge>
                </div>
              ))}
            </div>
          </CardContent>
        </Card>
      </div>

      <Card>
        <CardHeader>
          <div className="flex items-center justify-between">
            <div>
              <CardTitle className="text-lg">Pending Budget Approvals</CardTitle>
              <CardDescription>Requests awaiting CFO authorization</CardDescription>
            </div>
            <Badge variant="destructive">{pendingApprovals.length}</Badge>
          </div>
        </CardHeader>
        <CardContent>
          <div className="space-y-3">
            {pendingApprovals.map((item) => (
              <div key={item.id} className="flex items-center justify-between p-3 border border-slate-200 dark:border-slate-700 rounded-lg hover:bg-slate-50 dark:hover:bg-slate-800/50">
                <div className="flex items-center gap-3 flex-1 min-w-0">
                  <span className="text-xs text-slate-400 font-mono flex-shrink-0">{item.id}</span>
                  <div className="min-w-0">
                    <p className="text-sm font-medium text-slate-900 dark:text-white truncate">{item.title}</p>
                    <Badge className={`text-[10px] mt-0.5 ${item.deptColor}`}>{item.dept}</Badge>
                  </div>
                </div>
                <div className="flex items-center gap-2 flex-shrink-0 ml-4">
                  <span className="text-sm font-semibold text-slate-900 dark:text-white">{item.amount}</span>
                  <Button variant="outline" size="sm" className="text-emerald-600 hover:text-emerald-700">Approve</Button>
                  <Button variant="outline" size="sm" className="text-red-600 hover:text-red-700">Reject</Button>
                </div>
              </div>
            ))}
          </div>
        </CardContent>
      </Card>
    </div>
  )
}
