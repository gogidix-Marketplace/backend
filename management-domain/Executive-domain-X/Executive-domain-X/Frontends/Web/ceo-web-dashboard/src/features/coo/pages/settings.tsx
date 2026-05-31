import { Card, CardContent, CardHeader, CardTitle } from '@shared/components/ui/card'
import { Badge } from '@shared/components/ui/badge'
import { Button } from '@shared/components/ui/button'

export default function COOSettingsPage() {
  const notifications = [
    { label: 'Critical Incident Alerts', description: 'P1 and P2 incidents requiring immediate attention', enabled: true },
    { label: 'SLA Breach Warnings', description: 'Notifications when SLA targets are at risk', enabled: true },
    { label: 'Shift Change Alerts', description: 'Updates on shift schedule modifications', enabled: false },
    { label: 'Daily Operations Digest', description: 'Automated daily summary at 8:00 AM', enabled: true },
  ]

  const thresholds = [
    { label: 'SLA Compliance Floor', value: '95%', description: 'Alert when any unit drops below this threshold', pct: 95 },
    { label: 'Incident MTTR Cap', value: '2 hours', description: 'Escalate when resolution exceeds this limit', pct: 80 },
    { label: 'Overtime Threshold', value: '15%', description: 'Flag departments exceeding this overtime rate', pct: 75 },
  ]

  const slaDefaults = [
    { label: 'Courier OTD Target', value: '98%', unit: 'Courier' },
    { label: 'E-Commerce Fulfillment SLA', value: '24 hours', unit: 'E-Commerce' },
    { label: 'Warehouse Capacity Warning', value: '90%', unit: 'Warehousing' },
    { label: 'Cross-Unit Escalation Window', value: '30 minutes', unit: 'All Units' },
  ]

  return (
    <div className="space-y-5">
      <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
        <div>
          <h1 className="text-2xl sm:text-3xl font-bold text-slate-900 dark:text-white">Operations Settings</h1>
          <p className="text-sm text-slate-500 dark:text-slate-400 mt-1">Configure notifications, thresholds, and SLA defaults</p>
        </div>
        <Button>Save Changes</Button>
      </div>

      <Card>
        <CardHeader>
          <CardTitle className="text-lg">Notification Preferences</CardTitle>
        </CardHeader>
        <CardContent>
          <div className="space-y-4">
            {notifications.map((n) => (
              <div key={n.label} className="flex items-center justify-between p-3 rounded-lg bg-slate-50 dark:bg-slate-800/50">
                <div>
                  <p className="text-sm font-semibold text-slate-900 dark:text-white">{n.label}</p>
                  <p className="text-xs text-slate-500 dark:text-slate-400 mt-0.5">{n.description}</p>
                </div>
                <div className={`relative w-11 h-6 rounded-full transition-colors cursor-pointer ${n.enabled ? 'bg-emerald-500' : 'bg-slate-300 dark:bg-slate-600'}`}>
                  <div className={`absolute top-0.5 w-5 h-5 bg-white rounded-full shadow transition-transform ${n.enabled ? 'translate-x-5.5 left-0.5' : 'left-0.5'}`} />
                </div>
              </div>
            ))}
          </div>
        </CardContent>
      </Card>

      <Card>
        <CardHeader>
          <CardTitle className="text-lg">Alert Thresholds</CardTitle>
        </CardHeader>
        <CardContent>
          <div className="space-y-5">
            {thresholds.map((t) => (
              <div key={t.label}>
                <div className="flex items-center justify-between mb-1">
                  <div>
                    <p className="text-sm font-semibold text-slate-900 dark:text-white">{t.label}</p>
                    <p className="text-xs text-slate-500 dark:text-slate-400">{t.description}</p>
                  </div>
                  <Badge variant="outline" className="text-xs">{t.value}</Badge>
                </div>
                <div className="h-2 bg-slate-100 dark:bg-slate-800 rounded-full overflow-hidden mt-2">
                  <div className="h-full rounded-full bg-blue-500" style={{ width: `${t.pct}%` }} />
                </div>
              </div>
            ))}
          </div>
        </CardContent>
      </Card>

      <Card>
        <CardHeader>
          <CardTitle className="text-lg">SLA Defaults</CardTitle>
        </CardHeader>
        <CardContent>
          <div className="space-y-3">
            {slaDefaults.map((s) => (
              <div key={s.label} className="flex items-center justify-between p-3 rounded-lg bg-slate-50 dark:bg-slate-800/50">
                <div>
                  <p className="text-sm font-semibold text-slate-900 dark:text-white">{s.label}</p>
                  <Badge variant="outline" className="text-[10px] mt-1">{s.unit}</Badge>
                </div>
                <div className="flex items-center gap-3">
                  <span className="text-sm font-bold text-slate-900 dark:text-white">{s.value}</span>
                  <Button variant="outline" size="sm">Edit</Button>
                </div>
              </div>
            ))}
          </div>
        </CardContent>
      </Card>
    </div>
  )
}
