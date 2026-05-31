import { Card, CardContent, CardHeader, CardTitle, CardDescription } from '@shared/components/ui/card'
import { Badge } from '@shared/components/ui/badge'
import { Button } from '@shared/components/ui/button'

export default function CFOCompliancePage() {
  const complianceScore = 94

  const complianceAreas = [
    { name: 'Tax Compliance', score: 98, status: 'Compliant' },
    { name: 'Regulatory Filings', score: 95, status: 'Compliant' },
    { name: 'SOX Controls', score: 92, status: 'Minor Issues' },
    { name: 'Anti-Money Laundering', score: 91, status: 'Under Review' },
  ]

  const auditTrail = [
    { id: 'AE-001', description: 'Q1 2026 financial audit completed', timestamp: 'Apr 20, 2026 14:32', status: 'completed' },
    { id: 'AE-002', description: 'SOX control testing initiated', timestamp: 'Apr 18, 2026 09:15', status: 'in-progress' },
    { id: 'AE-003', description: 'AML screening batch processed', timestamp: 'Apr 17, 2026 16:45', status: 'completed' },
    { id: 'AE-004', description: 'Tax filing review submitted to IRS', timestamp: 'Apr 15, 2026 11:20', status: 'pending' },
    { id: 'AE-005', description: 'Regulatory update: New disclosure rules', timestamp: 'Apr 12, 2026 08:00', status: 'completed' },
  ]

  const upcomingDeadlines = [
    { item: 'Q2 2026 VAT Return', due: 'Jul 25, 2026', status: 'upcoming' as const },
    { item: 'Annual SOC 2 Audit', due: 'Jun 30, 2026', status: 'upcoming' as const },
    { item: 'Q1 Tax Filing Amendment', due: 'Apr 15, 2026', status: 'overdue' as const },
    { item: 'AML Training Certification', due: 'May 20, 2026', status: 'completed' as const },
  ]

  const statusVariant = {
    completed: 'success' as const,
    'in-progress': 'info' as const,
    pending: 'warning' as const,
    upcoming: 'info' as const,
    overdue: 'destructive' as const,
  }

  const scoreColor = (score: number) => {
    if (score >= 95) return 'bg-emerald-500'
    if (score >= 90) return 'bg-blue-500'
    return 'bg-amber-500'
  }

  return (
    <div className="space-y-5">
      <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
        <div className="flex items-center gap-3">
          <div>
            <h1 className="text-2xl sm:text-3xl font-bold text-slate-900 dark:text-white">Financial Compliance</h1>
            <p className="text-sm text-slate-500 dark:text-slate-400 mt-1">Regulatory compliance and internal controls monitoring</p>
          </div>
        </div>
        <div className="flex items-center gap-2">
          <Badge variant="success" className="px-3 py-1.5 text-xs font-medium">Active</Badge>
          <Button variant="outline" size="sm">Export Report</Button>
        </div>
      </div>

      <Card>
        <CardHeader>
          <CardTitle className="text-lg">Compliance Score</CardTitle>
          <CardDescription>Overall financial compliance health</CardDescription>
        </CardHeader>
        <CardContent>
          <div className="flex items-center gap-6">
            <div className="flex-shrink-0 flex flex-col items-center">
              <div className="relative w-24 h-24">
                <svg className="w-24 h-24 transform -rotate-90">
                  <circle cx="48" cy="48" r="40" fill="none" className="stroke-slate-100 dark:stroke-slate-800" strokeWidth="8" />
                  <circle cx="48" cy="48" r="40" fill="none" className="stroke-emerald-500" strokeWidth="8" strokeDasharray={`${(complianceScore / 100) * 251.3} 251.3`} strokeLinecap="round" />
                </svg>
                <span className="absolute inset-0 flex items-center justify-center text-2xl font-bold text-slate-900 dark:text-white">{complianceScore}</span>
              </div>
              <span className="text-xs text-slate-500 mt-1">out of 100</span>
            </div>
            <div className="flex-1 space-y-3">
              {complianceAreas.map((area) => (
                <div key={area.name} className="space-y-1">
                  <div className="flex items-center justify-between">
                    <span className="text-sm font-medium text-slate-700 dark:text-slate-300">{area.name}</span>
                    <div className="flex items-center gap-2">
                      <span className="text-xs text-slate-500">{area.score}%</span>
                      <Badge variant={area.score >= 95 ? 'success' : area.score >= 90 ? 'info' : 'warning'} className="text-[10px]">{area.status}</Badge>
                    </div>
                  </div>
                  <div className="h-2 bg-slate-100 dark:bg-slate-800 rounded-full overflow-hidden">
                    <div className={`h-full rounded-full ${scoreColor(area.score)}`} style={{ width: `${area.score}%` }} />
                  </div>
                </div>
              ))}
            </div>
          </div>
        </CardContent>
      </Card>

      <Card>
        <CardHeader>
          <CardTitle className="text-lg">Audit Trail</CardTitle>
          <CardDescription>Recent compliance events and activities</CardDescription>
        </CardHeader>
        <CardContent>
          <div className="space-y-3">
            {auditTrail.map((event) => (
              <div key={event.id} className="flex items-center justify-between p-3 rounded-lg border border-slate-200 dark:border-slate-700 hover:bg-slate-50 dark:hover:bg-slate-800/50">
                <div className="flex items-center gap-3 flex-1 min-w-0">
                  <span className="text-xs text-slate-400 font-mono flex-shrink-0">{event.id}</span>
                  <div className="min-w-0">
                    <p className="text-sm font-medium text-slate-900 dark:text-white truncate">{event.description}</p>
                    <p className="text-xs text-slate-500 dark:text-slate-400 mt-0.5">{event.timestamp}</p>
                  </div>
                </div>
                <Badge variant={statusVariant[event.status as keyof typeof statusVariant]} className="text-[10px] capitalize flex-shrink-0 ml-2">
                  {event.status}
                </Badge>
              </div>
            ))}
          </div>
        </CardContent>
      </Card>

      <Card>
        <CardHeader>
          <CardTitle className="text-lg">Upcoming Deadlines</CardTitle>
          <CardDescription>Critical compliance dates and filing requirements</CardDescription>
        </CardHeader>
        <CardContent>
          <div className="space-y-3">
            {upcomingDeadlines.map((item) => (
              <div key={item.item} className="flex items-center justify-between p-3 rounded-lg border border-slate-200 dark:border-slate-700 hover:bg-slate-50 dark:hover:bg-slate-800/50">
                <div>
                  <p className="text-sm font-medium text-slate-900 dark:text-white">{item.item}</p>
                  <p className="text-xs text-slate-500 dark:text-slate-400 mt-0.5">Due: {item.due}</p>
                </div>
                <Badge variant={statusVariant[item.status]} className="text-[10px] capitalize">{item.status}</Badge>
              </div>
            ))}
          </div>
        </CardContent>
      </Card>
    </div>
  )
}
