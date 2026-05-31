import { useState } from 'react'
import { Card, CardContent, CardHeader, CardTitle, CardDescription } from '@shared/components/ui/card'
import { Badge } from '@shared/components/ui/badge'
import { Button } from '@shared/components/ui/button'

export default function CFOReportsPage() {
  const [dateRange, setDateRange] = useState('q1-2026')

  const reportCategories = [
    { name: 'P&L Statement', description: 'Revenue, expenses, and net income summary', lastGenerated: 'Apr 1, 2026', icon: '📊', color: 'border-l-blue-500' },
    { name: 'Balance Sheet', description: 'Assets, liabilities, and equity overview', lastGenerated: 'Mar 31, 2026', icon: '📋', color: 'border-l-emerald-500' },
    { name: 'Cash Flow', description: 'Operating, investing, and financing activities', lastGenerated: 'Apr 2, 2026', icon: '💰', color: 'border-l-amber-500' },
    { name: 'Revenue Analysis', description: 'Revenue breakdown by unit and channel', lastGenerated: 'Apr 3, 2026', icon: '📈', color: 'border-l-purple-500' },
    { name: 'Budget Variance', description: 'Budget vs actual spending comparison', lastGenerated: 'Apr 5, 2026', icon: '📉', color: 'border-l-rose-500' },
    { name: 'Tax Reports', description: 'Tax obligations and filing documentation', lastGenerated: 'Mar 28, 2026', icon: '🧾', color: 'border-l-cyan-500' },
  ]

  const recentReports = [
    { name: 'Q1 2026 P&L Statement', period: 'Q1 2026', generated: 'Apr 1, 2026', status: 'ready' },
    { name: 'March Cash Flow Report', period: 'Mar 2026', generated: 'Apr 2, 2026', status: 'ready' },
    { name: 'Q1 Budget Variance Analysis', period: 'Q1 2026', generated: 'Apr 5, 2026', status: 'ready' },
    { name: 'Annual Tax Filing Prep', period: 'FY 2025', generated: 'Mar 28, 2026', status: 'processing' },
    { name: 'Haulage Revenue Deep Dive', period: 'Mar 2026', generated: 'Apr 3, 2026', status: 'ready' },
  ]

  const scheduledReports = [
    { name: 'Monthly Financial Summary', schedule: '1st of each month', format: 'PDF', recipients: 'CFO, Board' },
    { name: 'Weekly Budget Variance', schedule: 'Every Monday 08:00', format: 'Excel', recipients: 'Finance Team' },
    { name: 'Quarterly Compliance Report', schedule: 'End of each quarter', format: 'PDF', recipients: 'Compliance, Legal' },
  ]

  return (
    <div className="space-y-5">
      <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
        <div>
          <h1 className="text-2xl sm:text-3xl font-bold text-slate-900 dark:text-white">Financial Reports</h1>
          <p className="text-sm text-slate-500 dark:text-slate-400 mt-1">Generate, view, and download financial reports</p>
        </div>
        <div className="flex items-center gap-2">
          <select
            value={dateRange}
            onChange={(e) => setDateRange(e.target.value)}
            className="px-3 py-1.5 rounded-lg border border-slate-200 dark:border-slate-700 bg-white dark:bg-slate-800 text-sm"
          >
            <option value="q1-2026">Q1 2026</option>
            <option value="q2-2026">Q2 2026</option>
            <option value="h1-2026">H1 2026</option>
            <option value="fy-2026">FY 2026</option>
          </select>
        </div>
      </div>

      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4">
        {reportCategories.map((cat) => (
          <Card key={cat.name} className={`border-l-4 ${cat.color}`}>
            <CardContent className="p-4">
              <div className="flex items-start justify-between">
                <div className="flex-1 min-w-0">
                  <div className="flex items-center gap-2">
                    <span className="text-lg">{cat.icon}</span>
                    <p className="text-sm font-semibold text-slate-900 dark:text-white">{cat.name}</p>
                  </div>
                  <p className="text-xs text-slate-500 dark:text-slate-400 mt-1.5">{cat.description}</p>
                  <p className="text-xs text-slate-400 mt-2">Last generated: {cat.lastGenerated}</p>
                </div>
              </div>
              <Button variant="outline" size="sm" className="w-full mt-3">Generate</Button>
            </CardContent>
          </Card>
        ))}
      </div>

      <Card>
        <CardHeader>
          <CardTitle className="text-lg">Recent Reports</CardTitle>
          <CardDescription>Recently generated financial documents</CardDescription>
        </CardHeader>
        <CardContent>
          <div className="overflow-x-auto">
            <table className="w-full text-sm">
              <thead>
                <tr className="border-b border-slate-200 dark:border-slate-700">
                  <th className="text-left py-2 px-3 font-semibold text-slate-600 dark:text-slate-300">Report Name</th>
                  <th className="text-left py-2 px-3 font-semibold text-slate-600 dark:text-slate-300">Period</th>
                  <th className="text-left py-2 px-3 font-semibold text-slate-600 dark:text-slate-300">Generated</th>
                  <th className="text-center py-2 px-3 font-semibold text-slate-600 dark:text-slate-300">Status</th>
                  <th className="text-right py-2 px-3 font-semibold text-slate-600 dark:text-slate-300">Action</th>
                </tr>
              </thead>
              <tbody>
                {recentReports.map((report) => (
                  <tr key={report.name} className="border-b border-slate-100 dark:border-slate-800 hover:bg-slate-50 dark:hover:bg-slate-800/50">
                    <td className="py-2.5 px-3 font-medium text-slate-900 dark:text-white">{report.name}</td>
                    <td className="py-2.5 px-3 text-slate-700 dark:text-slate-300">{report.period}</td>
                    <td className="py-2.5 px-3 text-slate-700 dark:text-slate-300">{report.generated}</td>
                    <td className="text-center py-2.5 px-3">
                      <Badge variant={report.status === 'ready' ? 'success' : 'warning'} className="text-[10px] capitalize">
                        {report.status}
                      </Badge>
                    </td>
                    <td className="text-right py-2.5 px-3">
                      <Button variant="outline" size="sm" disabled={report.status !== 'ready'}>
                        {report.status === 'ready' ? 'Download' : 'Pending'}
                      </Button>
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
          <CardTitle className="text-lg">Scheduled Reports</CardTitle>
          <CardDescription>Auto-generated reports and delivery schedules</CardDescription>
        </CardHeader>
        <CardContent>
          <div className="space-y-3">
            {scheduledReports.map((item) => (
              <div key={item.name} className="flex items-center justify-between p-3 rounded-lg border border-slate-200 dark:border-slate-700 hover:bg-slate-50 dark:hover:bg-slate-800/50">
                <div>
                  <p className="text-sm font-medium text-slate-900 dark:text-white">{item.name}</p>
                  <p className="text-xs text-slate-500 dark:text-slate-400 mt-0.5">{item.schedule} · {item.recipients}</p>
                </div>
                <div className="flex items-center gap-2">
                  <Badge variant="outline" className="text-[10px]">{item.format}</Badge>
                  <Badge variant="success" className="text-[10px]">Active</Badge>
                </div>
              </div>
            ))}
          </div>
        </CardContent>
      </Card>
    </div>
  )
}
