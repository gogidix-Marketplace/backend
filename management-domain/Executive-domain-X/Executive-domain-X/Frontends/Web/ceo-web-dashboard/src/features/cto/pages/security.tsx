import { Card, CardContent, CardHeader, CardTitle, CardDescription } from '@shared/components/ui/card'
import { Badge } from '@shared/components/ui/badge'
import { Button } from '@shared/components/ui/button'

export default function CTOSecurityPage() {
  const frameworks = [
    { name: 'GDPR', status: 'Pass', score: 100, lastAudit: '2026-03-15', type: 'pass' },
    { name: 'SOC2', status: '98%', score: 98, lastAudit: '2026-04-01', type: 'score' },
    { name: 'ISO27001', status: '95%', score: 95, lastAudit: '2026-02-20', type: 'score' },
    { name: 'OWASP', status: '92%', score: 92, lastAudit: '2026-03-28', type: 'score' },
    { name: 'Pen Test', status: 'Pass', score: 100, lastAudit: '2026-04-10', type: 'pass' },
  ]

  const vulnerabilities = [
    { severity: 'Critical', count: 0, trend: 'down', color: 'text-red-600', bg: 'bg-red-100 dark:bg-red-900/30' },
    { severity: 'High', count: 2, trend: 'down', color: 'text-orange-600', bg: 'bg-orange-100 dark:bg-orange-900/30' },
    { severity: 'Medium', count: 8, trend: 'up', color: 'text-amber-600', bg: 'bg-amber-100 dark:bg-amber-900/30' },
    { severity: 'Low', count: 23, trend: 'down', color: 'text-blue-600', bg: 'bg-blue-100 dark:bg-blue-900/30' },
  ]

  const accessControl = [
    { label: 'Active Users', value: '2,847', icon: '👥' },
    { label: 'MFA Enabled', value: '94%', icon: '🔐' },
    { label: 'Privileged Accounts', value: '23', icon: '🔑' },
    { label: 'Failed Logins', value: '12', icon: '🚫' },
  ]

  const events = [
    { time: '10:42 AM', severity: 'Medium', description: 'Unusual login pattern detected for user jsmith@corp', resolved: true },
    { time: '09:15 AM', severity: 'Low', description: 'SSL certificate renewal completed for *.gogidix.com', resolved: true },
    { time: '08:30 AM', severity: 'High', description: 'Brute force attempt blocked from IP 203.0.113.42', resolved: true },
    { time: 'Yesterday', severity: 'Low', description: 'Password policy updated for all admin accounts', resolved: true },
    { time: 'Yesterday', severity: 'Medium', description: 'API rate limit triggered for partner integration', resolved: false },
  ]

  return (
    <div className="space-y-5">
      <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
        <div>
          <h1 className="text-2xl sm:text-3xl font-bold text-slate-900 dark:text-white">Security Posture</h1>
          <p className="text-sm text-slate-500 dark:text-slate-400 mt-1">Compliance, vulnerabilities, and access control</p>
        </div>
        <Badge variant="success" className="px-3 py-1.5 text-xs font-medium self-start">
          Compliance Score: 96/100
        </Badge>
      </div>

      <Card>
        <CardHeader>
          <CardTitle className="text-lg">Security Frameworks</CardTitle>
          <CardDescription>Compliance and audit status</CardDescription>
        </CardHeader>
        <CardContent>
          <div className="flex flex-wrap gap-3">
            {frameworks.map((f) => (
              <div key={f.name} className="flex items-center gap-2 px-4 py-2.5 rounded-lg border border-slate-200 dark:border-slate-700 bg-slate-50 dark:bg-slate-800/50">
                <span className="text-sm font-medium text-slate-700 dark:text-slate-300">{f.name}</span>
                <span className={`text-sm font-bold ${f.type === 'pass' ? 'text-emerald-600' : 'text-blue-600'}`}>
                  {f.type === 'pass' ? '● ' : ''}{f.status}
                </span>
                <span className="text-[10px] text-slate-400 ml-1">Last: {f.lastAudit}</span>
              </div>
            ))}
          </div>
        </CardContent>
      </Card>

      <div className="grid gap-4 sm:gap-5 lg:grid-cols-2">
        <Card>
          <CardHeader>
            <CardTitle className="text-lg">Threat Level</CardTitle>
            <CardDescription>Current threat assessment</CardDescription>
          </CardHeader>
          <CardContent>
            <div className="p-4 rounded-lg border border-emerald-200 dark:border-emerald-800 bg-emerald-50 dark:bg-emerald-900/20">
              <div className="flex items-center justify-between">
                <div>
                  <p className="text-sm font-medium text-slate-500 dark:text-slate-400">Current Level</p>
                  <p className="text-2xl font-bold text-emerald-700 dark:text-emerald-400">Low</p>
                </div>
                <div className="w-16 h-16 rounded-full border-4 border-emerald-500 flex items-center justify-center">
                  <span className="text-emerald-600 text-xl font-bold">✓</span>
                </div>
              </div>
              <div className="mt-3 flex gap-2">
                <Badge variant="success" className="text-[10px]">No active threats</Badge>
                <Badge variant="outline" className="text-[10px]">Last scan: 2h ago</Badge>
              </div>
            </div>
          </CardContent>
        </Card>

        <Card>
          <CardHeader>
            <CardTitle className="text-lg">Vulnerability Summary</CardTitle>
            <CardDescription>Open vulnerabilities by severity</CardDescription>
          </CardHeader>
          <CardContent>
            <div className="grid grid-cols-2 gap-3">
              {vulnerabilities.map((v) => (
                <div key={v.severity} className={`p-3 rounded-lg ${v.bg}`}>
                  <p className="text-xs font-medium text-slate-500 dark:text-slate-400">{v.severity}</p>
                  <div className="flex items-center justify-between mt-1">
                    <p className={`text-2xl font-bold ${v.color}`}>{v.count}</p>
                    <span className={`text-xs ${v.trend === 'down' ? 'text-emerald-600' : 'text-red-600'}`}>
                      {v.trend === 'down' ? '↓' : '↑'}
                    </span>
                  </div>
                </div>
              ))}
            </div>
          </CardContent>
        </Card>
      </div>

      <Card>
        <CardHeader>
          <CardTitle className="text-lg">Access Control</CardTitle>
          <CardDescription>Authentication and authorization metrics</CardDescription>
        </CardHeader>
        <CardContent>
          <div className="grid grid-cols-2 lg:grid-cols-4 gap-4">
            {accessControl.map((ac) => (
              <div key={ac.label} className="p-4 rounded-lg border border-slate-200 dark:border-slate-700 text-center">
                <span className="text-2xl mb-2 block">{ac.icon}</span>
                <p className="text-xl font-bold text-slate-900 dark:text-white">{ac.value}</p>
                <p className="text-xs text-slate-500 dark:text-slate-400 mt-0.5">{ac.label}</p>
              </div>
            ))}
          </div>
        </CardContent>
      </Card>

      <Card>
        <CardHeader>
          <CardTitle className="text-lg">Recent Security Events</CardTitle>
          <CardDescription>Latest security alerts and activities</CardDescription>
        </CardHeader>
        <CardContent>
          <div className="space-y-3">
            {events.map((e, i) => (
              <div key={i} className="flex items-start gap-3 p-3 rounded-lg border border-slate-200 dark:border-slate-700">
                <div className={`w-2 h-2 rounded-full mt-1.5 flex-shrink-0 ${e.severity === 'High' ? 'bg-orange-500' : e.severity === 'Medium' ? 'bg-amber-500' : 'bg-blue-500'}`} />
                <div className="flex-1 min-w-0">
                  <div className="flex items-center gap-2">
                    <Badge variant={e.severity === 'High' ? 'destructive' : e.severity === 'Medium' ? 'warning' : 'info'} className="text-[10px]">
                      {e.severity}
                    </Badge>
                    <span className="text-[10px] text-slate-400">{e.time}</span>
                  </div>
                  <p className="text-sm text-slate-700 dark:text-slate-300 mt-1">{e.description}</p>
                </div>
                <Badge variant={e.resolved ? 'success' : 'warning'} className="text-[10px] flex-shrink-0">
                  {e.resolved ? 'Resolved' : 'Open'}
                </Badge>
              </div>
            ))}
          </div>
        </CardContent>
      </Card>
    </div>
  )
}
