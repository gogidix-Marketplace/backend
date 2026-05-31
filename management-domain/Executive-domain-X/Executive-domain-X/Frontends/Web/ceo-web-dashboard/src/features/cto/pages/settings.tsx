import { Card, CardContent, CardHeader, CardTitle, CardDescription } from '@shared/components/ui/card'
import { Badge } from '@shared/components/ui/badge'
import { Button } from '@shared/components/ui/button'

export default function CTOSettingsPage() {
  const systemConfig = [
    { label: 'Environment', value: 'Production', status: 'Active' },
    { label: 'Region', value: 'EU-West-1', status: 'Primary' },
    { label: 'Kubernetes Version', value: 'v1.29.2', status: 'Latest' },
    { label: 'Container Registry', value: 'registry.gogidix.com', status: 'Healthy' },
  ]

  const securityPolicies = [
    { label: 'MFA Required', value: 'Enabled', variant: 'success' as const },
    { label: 'Password Rotation', value: '90 days', variant: 'outline' as const },
    { label: 'IP Whitelisting', value: 'Enabled', variant: 'success' as const },
    { label: 'Audit Logging', value: 'Enabled', variant: 'success' as const },
  ]

  const thresholds = [
    { label: 'CPU Alert', value: '80%', description: 'Trigger warning at 80% utilization' },
    { label: 'Memory Alert', value: '85%', description: 'Trigger warning at 85% utilization' },
    { label: 'Disk Usage', value: '90%', description: 'Critical alert at 90% disk usage' },
    { label: 'Latency Threshold', value: '500ms', description: 'P95 latency alert threshold' },
  ]

  return (
    <div className="space-y-5">
      <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
        <div>
          <h1 className="text-2xl sm:text-3xl font-bold text-slate-900 dark:text-white">Settings</h1>
          <p className="text-sm text-slate-500 dark:text-slate-400 mt-1">System configuration and operational policies</p>
        </div>
        <Button size="sm">Save Changes</Button>
      </div>

      <Card>
        <CardHeader>
          <CardTitle className="text-lg">System Configuration</CardTitle>
          <CardDescription>Core infrastructure settings</CardDescription>
        </CardHeader>
        <CardContent>
          <div className="space-y-3">
            {systemConfig.map((sc) => (
              <div key={sc.label} className="flex items-center justify-between p-3 rounded-lg border border-slate-200 dark:border-slate-700">
                <div>
                  <p className="text-sm font-medium text-slate-900 dark:text-white">{sc.label}</p>
                </div>
                <div className="flex items-center gap-2">
                  <span className="text-sm text-slate-600 dark:text-slate-300">{sc.value}</span>
                  <Badge variant="success" className="text-[10px]">{sc.status}</Badge>
                </div>
              </div>
            ))}
          </div>
        </CardContent>
      </Card>

      <Card>
        <CardHeader>
          <CardTitle className="text-lg">Security Policies</CardTitle>
          <CardDescription>Authentication and access control policies</CardDescription>
        </CardHeader>
        <CardContent>
          <div className="space-y-3">
            {securityPolicies.map((sp) => (
              <div key={sp.label} className="flex items-center justify-between p-3 rounded-lg border border-slate-200 dark:border-slate-700">
                <p className="text-sm font-medium text-slate-900 dark:text-white">{sp.label}</p>
                <div className="flex items-center gap-2">
                  <span className="text-sm text-slate-600 dark:text-slate-300">{sp.value}</span>
                  <Badge variant={sp.variant} className="text-[10px]">{sp.value === 'Enabled' ? 'On' : sp.value}</Badge>
                </div>
              </div>
            ))}
          </div>
        </CardContent>
      </Card>

      <Card>
        <CardHeader>
          <CardTitle className="text-lg">Monitoring Thresholds</CardTitle>
          <CardDescription>Alert trigger levels for infrastructure metrics</CardDescription>
        </CardHeader>
        <CardContent>
          <div className="space-y-3">
            {thresholds.map((th) => (
              <div key={th.label} className="flex items-center justify-between p-3 rounded-lg border border-slate-200 dark:border-slate-700">
                <div>
                  <p className="text-sm font-medium text-slate-900 dark:text-white">{th.label}</p>
                  <p className="text-xs text-slate-500 dark:text-slate-400 mt-0.5">{th.description}</p>
                </div>
                <div className="flex items-center gap-2">
                  <span className="text-sm font-semibold text-slate-900 dark:text-white">{th.value}</span>
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
