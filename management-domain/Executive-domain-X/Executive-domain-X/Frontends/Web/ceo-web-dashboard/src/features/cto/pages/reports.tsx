import { Card, CardContent, CardHeader, CardTitle, CardDescription } from '@shared/components/ui/card'
import { Badge } from '@shared/components/ui/badge'
import { Button } from '@shared/components/ui/button'

export default function CTOReportsPage() {
  const reportTypes = [
    { name: 'System Health', description: 'Infrastructure uptime and performance', icon: '📊', lastRun: '2h ago' },
    { name: 'Security Audit', description: 'Compliance and vulnerability assessment', icon: '🔒', lastRun: '1d ago' },
    { name: 'Deployment Log', description: 'CI/CD pipeline and release history', icon: '🚀', lastRun: '4h ago' },
    { name: 'Performance Analysis', description: 'Latency, throughput, and resource metrics', icon: '⚡', lastRun: '6h ago' },
    { name: 'Capacity Planning', description: 'Resource utilization and forecasts', icon: '📈', lastRun: '1w ago' },
    { name: 'Cost Analysis', description: 'Cloud spend and infrastructure costs', icon: '💰', lastRun: '3d ago' },
  ]

  const recentReports = [
    { name: 'Weekly System Health Report', date: 'Apr 21, 2026', type: 'System Health', status: 'Ready', size: '2.4 MB' },
    { name: 'Q1 Security Audit Summary', date: 'Apr 20, 2026', type: 'Security Audit', status: 'Ready', size: '8.1 MB' },
    { name: 'Deployment Report W16', date: 'Apr 19, 2026', type: 'Deployment Log', status: 'Ready', size: '1.2 MB' },
    { name: 'Monthly Performance Review', date: 'Apr 18, 2026', type: 'Performance', status: 'Ready', size: '5.6 MB' },
    { name: 'Cloud Cost Report March', date: 'Apr 15, 2026', type: 'Cost Analysis', status: 'Ready', size: '3.3 MB' },
  ]

  const scheduledReports = [
    { name: 'Daily System Health', frequency: 'Daily at 08:00', recipients: 5, nextRun: 'Tomorrow 08:00' },
    { name: 'Weekly Security Summary', frequency: 'Mondays at 09:00', recipients: 8, nextRun: 'Apr 27, 09:00' },
    { name: 'Monthly Capacity Forecast', frequency: '1st of month', recipients: 3, nextRun: 'May 1, 09:00' },
  ]

  return (
    <div className="space-y-5">
      <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
        <div>
          <h1 className="text-2xl sm:text-3xl font-bold text-slate-900 dark:text-white">Reports</h1>
          <p className="text-sm text-slate-500 dark:text-slate-400 mt-1">Technical reports and scheduled analytics</p>
        </div>
        <Button size="sm">Generate Report</Button>
      </div>

      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4">
        {reportTypes.map((rt) => (
          <Card key={rt.name} className="hover:shadow-md transition-shadow cursor-pointer">
            <CardContent className="p-4">
              <div className="flex items-start gap-3">
                <span className="text-2xl">{rt.icon}</span>
                <div className="flex-1">
                  <p className="text-sm font-semibold text-slate-900 dark:text-white">{rt.name}</p>
                  <p className="text-xs text-slate-500 dark:text-slate-400 mt-0.5">{rt.description}</p>
                  <div className="flex items-center gap-2 mt-2">
                    <Badge variant="outline" className="text-[10px]">Last: {rt.lastRun}</Badge>
                    <Button variant="outline" size="sm" className="text-[10px] h-6">Generate</Button>
                  </div>
                </div>
              </div>
            </CardContent>
          </Card>
        ))}
      </div>

      <Card>
        <CardHeader>
          <CardTitle className="text-lg">Recent Reports</CardTitle>
          <CardDescription>Latest generated reports</CardDescription>
        </CardHeader>
        <CardContent>
          <div className="overflow-x-auto">
            <table className="w-full text-sm">
              <thead>
                <tr className="border-b border-slate-200 dark:border-slate-700">
                  <th className="text-left py-2 px-3 text-xs font-medium text-slate-500">Report Name</th>
                  <th className="text-left py-2 px-3 text-xs font-medium text-slate-500">Date</th>
                  <th className="text-left py-2 px-3 text-xs font-medium text-slate-500">Type</th>
                  <th className="text-left py-2 px-3 text-xs font-medium text-slate-500">Status</th>
                  <th className="text-left py-2 px-3 text-xs font-medium text-slate-500">Size</th>
                  <th className="text-right py-2 px-3 text-xs font-medium text-slate-500">Action</th>
                </tr>
              </thead>
              <tbody>
                {recentReports.map((r, i) => (
                  <tr key={i} className="border-b border-slate-100 dark:border-slate-800">
                    <td className="py-2.5 px-3 font-medium text-slate-900 dark:text-white">{r.name}</td>
                    <td className="py-2.5 px-3 text-slate-500">{r.date}</td>
                    <td className="py-2.5 px-3"><Badge variant="secondary" className="text-[10px]">{r.type}</Badge></td>
                    <td className="py-2.5 px-3"><Badge variant="success" className="text-[10px]">{r.status}</Badge></td>
                    <td className="py-2.5 px-3 text-slate-500">{r.size}</td>
                    <td className="py-2.5 px-3 text-right"><Button variant="ghost" size="sm" className="text-xs h-7">Download</Button></td>
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
          <CardDescription>Automated report generation</CardDescription>
        </CardHeader>
        <CardContent>
          <div className="space-y-3">
            {scheduledReports.map((sr) => (
              <div key={sr.name} className="flex items-center justify-between p-3 rounded-lg border border-slate-200 dark:border-slate-700">
                <div>
                  <p className="text-sm font-semibold text-slate-900 dark:text-white">{sr.name}</p>
                  <p className="text-xs text-slate-500 dark:text-slate-400">{sr.frequency} · {sr.recipients} recipients</p>
                </div>
                <div className="flex items-center gap-2">
                  <Badge variant="outline" className="text-[10px]">Next: {sr.nextRun}</Badge>
                  <Button variant="outline" size="sm" className="text-[10px] h-6">Edit</Button>
                </div>
              </div>
            ))}
          </div>
        </CardContent>
      </Card>
    </div>
  )
}
