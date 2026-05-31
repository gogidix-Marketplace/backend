import { Card, CardContent, CardHeader, CardTitle } from '@shared/components/ui/card'
import { Badge } from '@shared/components/ui/badge'
import { Button } from '@shared/components/ui/button'

export default function COOReportsPage() {
  const periods = ['Today', 'This Week', 'This Month', 'This Quarter']

  const reportCards = [
    { title: 'Daily Operations', description: 'Order volume, fulfillment, and delivery metrics', lastGenerated: '2 hrs ago', status: 'Ready', icon: '📊' },
    { title: 'SLA Performance', description: 'Cross-unit SLA compliance and breach analysis', lastGenerated: '4 hrs ago', status: 'Ready', icon: '✅' },
    { title: 'Incident Summary', description: 'Incident count, MTTR, and severity distribution', lastGenerated: '1 hr ago', status: 'Ready', icon: '🚨' },
    { title: 'Resource Utilization', description: 'Staff allocation, shift coverage, and overtime', lastGenerated: '6 hrs ago', status: 'Ready', icon: '👥' },
    { title: 'Cross-Unit Analysis', description: 'Inter-unit dependencies and performance correlation', lastGenerated: 'Yesterday', status: 'Ready', icon: '🔗' },
    { title: 'Capacity Planning', description: 'Demand forecast and capacity projections', lastGenerated: '2 days ago', status: 'Stale', icon: '📈' },
  ]

  const recentReports = [
    { name: 'Daily Ops Report — Apr 22', generated: '10:30 AM', generatedBy: 'System', format: 'PDF', size: '2.4 MB' },
    { name: 'Weekly SLA Review — W16', generated: 'Yesterday', generatedBy: 'A. Okafor', format: 'XLSX', size: '1.1 MB' },
    { name: 'Incident Trend Analysis — April', generated: 'Yesterday', generatedBy: 'B. Adeyemi', format: 'PDF', size: '3.8 MB' },
    { name: 'Q1 Resource Utilization Report', generated: '2 days ago', generatedBy: 'C. Nwosu', format: 'PDF', size: '5.2 MB' },
    { name: 'Cross-Unit Performance Dashboard', generated: '3 days ago', generatedBy: 'D. Balogun', format: 'XLSX', size: '890 KB' },
  ]

  const scheduled = [
    { name: 'Daily Operations Summary', frequency: 'Daily at 8:00 AM', recipients: 'COO, VP Operations', nextRun: 'Tomorrow 8:00 AM' },
    { name: 'Weekly SLA Performance', frequency: 'Mondays at 9:00 AM', recipients: 'COO, Unit Heads', nextRun: 'Mon Apr 27' },
    { name: 'Monthly Incident Review', frequency: '1st of each month', recipients: 'COO, CTO, CEO', nextRun: 'May 1, 2026' },
  ]

  return (
    <div className="space-y-5">
      <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
        <div>
          <h1 className="text-2xl sm:text-3xl font-bold text-slate-900 dark:text-white">Operational Reports</h1>
          <p className="text-sm text-slate-500 dark:text-slate-400 mt-1">Generate, view, and manage operational reports</p>
        </div>
        <div className="flex gap-2">
          {periods.map((p) => (
            <Button key={p} variant={p === 'Today' ? 'default' : 'outline'} size="sm">{p}</Button>
          ))}
        </div>
      </div>

      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4">
        {reportCards.map((r) => (
          <Card key={r.title}>
            <CardContent className="p-4">
              <div className="flex items-center gap-2 mb-2">
                <span className="text-lg">{r.icon}</span>
                <p className="text-sm font-semibold text-slate-900 dark:text-white">{r.title}</p>
              </div>
              <p className="text-xs text-slate-500 dark:text-slate-400 mb-3">{r.description}</p>
              <div className="flex items-center justify-between">
                <span className="text-xs text-slate-500">Updated: {r.lastGenerated}</span>
                <Badge variant={r.status === 'Ready' ? 'success' : 'warning'} className="text-[10px]">{r.status}</Badge>
              </div>
              <Button variant="outline" size="sm" className="w-full mt-3">Generate Report</Button>
            </CardContent>
          </Card>
        ))}
      </div>

      <Card>
        <CardHeader>
          <CardTitle className="text-lg">Recent Reports</CardTitle>
        </CardHeader>
        <CardContent>
          <div className="overflow-x-auto">
            <table className="w-full text-sm">
              <thead>
                <tr className="border-b border-slate-200 dark:border-slate-700">
                  <th className="text-left py-3 px-2 font-semibold text-slate-600 dark:text-slate-400">Report</th>
                  <th className="text-left py-3 px-2 font-semibold text-slate-600 dark:text-slate-400">Generated</th>
                  <th className="text-left py-3 px-2 font-semibold text-slate-600 dark:text-slate-400">By</th>
                  <th className="text-left py-3 px-2 font-semibold text-slate-600 dark:text-slate-400">Format</th>
                  <th className="text-right py-3 px-2 font-semibold text-slate-600 dark:text-slate-400">Size</th>
                </tr>
              </thead>
              <tbody>
                {recentReports.map((r, i) => (
                  <tr key={i} className="border-b border-slate-100 dark:border-slate-800">
                    <td className="py-3 px-2 font-medium text-slate-900 dark:text-white">{r.name}</td>
                    <td className="py-3 px-2 text-slate-600 dark:text-slate-400">{r.generated}</td>
                    <td className="py-3 px-2 text-slate-600 dark:text-slate-400">{r.generatedBy}</td>
                    <td className="py-3 px-2"><Badge variant="outline" className="text-[10px]">{r.format}</Badge></td>
                    <td className="py-3 px-2 text-right text-slate-600 dark:text-slate-400">{r.size}</td>
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
        </CardHeader>
        <CardContent>
          <div className="space-y-3">
            {scheduled.map((s, i) => (
              <div key={i} className="flex items-center justify-between p-3 rounded-lg bg-slate-50 dark:bg-slate-800/50">
                <div>
                  <p className="text-sm font-semibold text-slate-900 dark:text-white">{s.name}</p>
                  <div className="flex items-center gap-2 mt-0.5">
                    <span className="text-xs text-slate-500">{s.frequency}</span>
                    <span className="text-xs text-slate-400">•</span>
                    <span className="text-xs text-slate-500">{s.recipients}</span>
                  </div>
                </div>
                <div className="text-right flex-shrink-0">
                  <p className="text-xs text-slate-500">Next run</p>
                  <p className="text-xs font-medium text-slate-700 dark:text-slate-300">{s.nextRun}</p>
                </div>
              </div>
            ))}
          </div>
        </CardContent>
      </Card>
    </div>
  )
}
