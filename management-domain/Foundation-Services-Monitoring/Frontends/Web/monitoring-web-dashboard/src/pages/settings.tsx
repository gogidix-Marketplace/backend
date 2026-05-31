import { useState } from 'react'
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@shared/components/ui/card'
import { Button } from '@shared/components/ui/button'
import { Input } from '@shared/components/ui/input'
import { Label } from '@shared/components/ui/label'
import { Switch } from '@shared/components/ui/switch'
import { Tabs, TabsContent, TabsList, TabsTrigger } from '@shared/components/ui/tabs'
import { Badge } from '@shared/components/ui/badge'
import { Bell, Mail, Hash, CheckCircle, Plus, Trash2 } from 'lucide-react'

export default function SettingsPage() {
  const [emailNotifications, setEmailNotifications] = useState(true)
  const [slackEnabled, setSlackEnabled] = useState(true)
  const [pagerDutyEnabled, setPagerDutyEnabled] = useState(false)

  return (
    <div className="space-y-6">
      {/* Page Header */}
      <div>
        <h1 className="text-3xl font-bold text-slate-900">Settings</h1>
        <p className="text-slate-600">Configure monitoring alerts and notifications</p>
      </div>

      <Tabs defaultValue="notifications">
        <TabsList>
          <TabsTrigger value="notifications">Notification Channels</TabsTrigger>
          <TabsTrigger value="thresholds">Alert Thresholds</TabsTrigger>
          <TabsTrigger value="dashboard">Dashboard</TabsTrigger>
        </TabsList>

        <TabsContent value="notifications" className="space-y-4">
          {/* Email Notifications */}
          <Card>
            <CardHeader>
              <div className="flex items-center gap-3">
                <div className="flex h-10 w-10 items-center justify-center rounded-full bg-blue-100">
                  <Mail className="h-5 w-5 text-blue-600" />
                </div>
                <div>
                  <CardTitle>Email Notifications</CardTitle>
                  <CardDescription>Configure email alert settings</CardDescription>
                </div>
              </div>
            </CardHeader>
            <CardContent className="space-y-4">
              <div className="flex items-center justify-between">
                <div>
                  <Label htmlFor="email-enabled">Enable Email Notifications</Label>
                  <p className="text-sm text-slate-500">Receive alerts via email</p>
                </div>
                <Switch
                  id="email-enabled"
                  checked={emailNotifications}
                  onCheckedChange={setEmailNotifications}
                />
              </div>

              {emailNotifications && (
                <>
                  <div className="space-y-3">
                    <div className="flex items-center justify-between rounded-lg border p-3">
                      <div>
                        <p className="font-medium">Critical Alerts</p>
                        <p className="text-sm text-slate-500">Send immediately</p>
                      </div>
                      <Switch defaultChecked />
                    </div>
                    <div className="flex items-center justify-between rounded-lg border p-3">
                      <div>
                        <p className="font-medium">Error Alerts</p>
                        <p className="text-sm text-slate-500">Send immediately</p>
                      </div>
                      <Switch defaultChecked />
                    </div>
                    <div className="flex items-center justify-between rounded-lg border p-3">
                      <div>
                        <p className="font-medium">Warning Alerts</p>
                        <p className="text-sm text-slate-500">Send in batches every 15 minutes</p>
                      </div>
                      <Switch defaultChecked />
                    </div>
                    <div className="flex items-center justify-between rounded-lg border p-3">
                      <div>
                        <p className="font-medium">Daily Summary</p>
                        <p className="text-sm text-slate-500">Send at 8:00 AM</p>
                      </div>
                      <Switch defaultChecked />
                    </div>
                  </div>

                  <div className="space-y-2">
                    <Label htmlFor="email-recipients">Recipients</Label>
                    <div className="flex gap-2">
                      <Input id="email-recipients" placeholder="ops@gogidix.com" defaultValue="ops@gogidix.com" />
                      <Button variant="outline" size="icon">
                        <Plus className="h-4 w-4" />
                      </Button>
                    </div>
                    <div className="flex flex-wrap gap-2 mt-2">
                      <Badge variant="secondary" className="gap-1">
                        ops@gogidix.com
                        <button className="hover:text-red-500">
                          <Trash2 className="h-3 w-3" />
                        </button>
                      </Badge>
                      <Badge variant="secondary" className="gap-1">
                        sre@gogidix.com
                        <button className="hover:text-red-500">
                          <Trash2 className="h-3 w-3" />
                        </button>
                      </Badge>
                    </div>
                  </div>
                </>
              )}
            </CardContent>
          </Card>

          {/* Slack Integration */}
          <Card>
            <CardHeader>
              <div className="flex items-center gap-3">
                <div className="flex h-10 w-10 items-center justify-center rounded-full bg-purple-100">
                  <Hash className="h-5 w-5 text-purple-600" />
                </div>
                <div>
                  <CardTitle>Slack Integration</CardTitle>
                  <CardDescription>Send alerts to Slack channels</CardDescription>
                </div>
              </div>
            </CardHeader>
            <CardContent className="space-y-4">
              <div className="flex items-center justify-between">
                <div>
                  <Label htmlFor="slack-enabled">Enable Slack Notifications</Label>
                  <p className="text-sm text-slate-500">Post alerts to Slack channels</p>
                </div>
                <Switch
                  id="slack-enabled"
                  checked={slackEnabled}
                  onCheckedChange={setSlackEnabled}
                />
              </div>

              {slackEnabled && (
                <>
                  <div className="space-y-2">
                    <Label htmlFor="slack-webhook">Webhook URL</Label>
                    <Input
                      id="slack-webhook"
                      type="password"
                      placeholder="https://hooks.slack.com/services/..."
                      defaultValue="https://hooks.slack.com/services/T00000000/B00000000/XXXXXXXXXXXXXXXXXXXX"
                    />
                  </div>

                  <div className="space-y-2">
                    <Label htmlFor="slack-channel">Default Channel</Label>
                    <Input
                      id="slack-channel"
                      placeholder="#ops-alerts"
                      defaultValue="#ops-alerts"
                    />
                  </div>

                  <div className="space-y-2">
                    <Label>Channel Mapping</Label>
                    <div className="space-y-2">
                      <div className="grid grid-cols-3 gap-2 items-center">
                        <Badge variant="critical">Critical</Badge>
                        <Input placeholder="#ops-critical" defaultValue="#ops-critical" />
                        <Switch defaultChecked />
                      </div>
                      <div className="grid grid-cols-3 gap-2 items-center">
                        <Badge variant="destructive">Error</Badge>
                        <Input placeholder="#ops-alerts" defaultValue="#ops-alerts" />
                        <Switch defaultChecked />
                      </div>
                      <div className="grid grid-cols-3 gap-2 items-center">
                        <Badge variant="degraded">Warning</Badge>
                        <Input placeholder="#ops-warnings" defaultValue="#ops-warnings" />
                        <Switch defaultChecked />
                      </div>
                    </div>
                  </div>

                  <div className="flex gap-2">
                    <Button variant="outline">Test Connection</Button>
                    <Button>Save Changes</Button>
                  </div>
                </>
              )}
            </CardContent>
          </Card>

          {/* PagerDuty Integration */}
          <Card>
            <CardHeader>
              <div className="flex items-center gap-3">
                <div className="flex h-10 w-10 items-center justify-center rounded-full bg-orange-100">
                  <Bell className="h-5 w-5 text-orange-600" />
                </div>
                <div>
                  <CardTitle>PagerDuty Integration</CardTitle>
                  <CardDescription>Create incidents in PagerDuty for critical alerts</CardDescription>
                </div>
              </div>
            </CardHeader>
            <CardContent className="space-y-4">
              <div className="flex items-center justify-between">
                <div>
                  <Label htmlFor="pagerduty-enabled">Enable PagerDuty</Label>
                  <p className="text-sm text-slate-500">Create incidents for critical alerts</p>
                </div>
                <Switch
                  id="pagerduty-enabled"
                  checked={pagerDutyEnabled}
                  onCheckedChange={setPagerDutyEnabled}
                />
              </div>

              {pagerDutyEnabled && (
                <>
                  <div className="space-y-2">
                    <Label htmlFor="pd-api-key">API Key</Label>
                    <Input
                      id="pd-api-key"
                      type="password"
                      placeholder="Your PagerDuty API key"
                    />
                  </div>

                  <div className="space-y-2">
                    <Label htmlFor="pd-service-key">Service Integration Key</Label>
                    <Input
                      id="pd-service-key"
                      type="password"
                      placeholder="Your service integration key"
                    />
                  </div>

                  <div className="flex items-center justify-between rounded-lg border p-3">
                    <div>
                      <p className="font-medium">Critical Alerts Only</p>
                      <p className="text-sm text-slate-500">Only create incidents for critical severity</p>
                    </div>
                    <Switch defaultChecked />
                  </div>

                  <div className="flex gap-2">
                    <Button variant="outline">Test Connection</Button>
                    <Button>Save Changes</Button>
                  </div>
                </>
              )}
            </CardContent>
          </Card>
        </TabsContent>

        <TabsContent value="thresholds" className="space-y-4">
          <Card>
            <CardHeader>
              <CardTitle>Default Alert Thresholds</CardTitle>
              <CardDescription>Configure default thresholds for alert rules</CardDescription>
            </CardHeader>
            <CardContent className="space-y-4">
              <div className="space-y-4">
                <div className="grid grid-cols-3 gap-4 items-center">
                  <Label>Response Time</Label>
                  <div className="flex items-center gap-2">
                    <Input type="number" defaultValue="200" />
                    <span className="text-sm text-slate-500">ms</span>
                  </div>
                  <Badge variant="degraded">Warning</Badge>
                </div>

                <div className="grid grid-cols-3 gap-4 items-center">
                  <Label>Response Time</Label>
                  <div className="flex items-center gap-2">
                    <Input type="number" defaultValue="500" />
                    <span className="text-sm text-slate-500">ms</span>
                  </div>
                  <Badge variant="critical">Critical</Badge>
                </div>

                <div className="grid grid-cols-3 gap-4 items-center">
                  <Label>Error Rate</Label>
                  <div className="flex items-center gap-2">
                    <Input type="number" defaultValue="1" step="0.1" />
                    <span className="text-sm text-slate-500">%</span>
                  </div>
                  <Badge variant="degraded">Warning</Badge>
                </div>

                <div className="grid grid-cols-3 gap-4 items-center">
                  <Label>Error Rate</Label>
                  <div className="flex items-center gap-2">
                    <Input type="number" defaultValue="5" step="0.1" />
                    <span className="text-sm text-slate-500">%</span>
                  </div>
                  <Badge variant="critical">Critical</Badge>
                </div>

                <div className="grid grid-cols-3 gap-4 items-center">
                  <Label>Memory Usage</Label>
                  <div className="flex items-center gap-2">
                    <Input type="number" defaultValue="80" />
                    <span className="text-sm text-slate-500">%</span>
                  </div>
                  <Badge variant="degraded">Warning</Badge>
                </div>

                <div className="grid grid-cols-3 gap-4 items-center">
                  <Label>Memory Usage</Label>
                  <div className="flex items-center gap-2">
                    <Input type="number" defaultValue="95" />
                    <span className="text-sm text-slate-500">%</span>
                  </div>
                  <Badge variant="critical">Critical</Badge>
                </div>
              </div>

              <div className="flex justify-end">
                <Button>Save Thresholds</Button>
              </div>
            </CardContent>
          </Card>
        </TabsContent>

        <TabsContent value="dashboard" className="space-y-4">
          <Card>
            <CardHeader>
              <CardTitle>Dashboard Preferences</CardTitle>
              <CardDescription>Customize your monitoring dashboard</CardDescription>
            </CardHeader>
            <CardContent className="space-y-4">
              <div className="flex items-center justify-between">
                <div>
                  <Label>Auto-refresh</Label>
                  <p className="text-sm text-slate-500">Automatically refresh dashboard data</p>
                </div>
                <Switch defaultChecked />
              </div>

              <div className="space-y-2">
                <Label>Refresh Interval</Label>
                <select className="flex h-10 w-full rounded-md border border-input bg-background px-3 py-2 text-sm">
                  <option>10 seconds</option>
                  <option selected>30 seconds</option>
                  <option>1 minute</option>
                  <option>5 minutes</option>
                </select>
              </div>

              <div className="flex items-center justify-between">
                <div>
                  <Label>Show Service Icons</Label>
                  <p className="text-sm text-slate-500">Display service type icons in cards</p>
                </div>
                <Switch defaultChecked />
              </div>

              <div className="flex items-center justify-between">
                <div>
                  <Label>Compact View</Label>
                  <p className="text-sm text-slate-500">Use more compact card layout</p>
                </div>
                <Switch />
              </div>

              <div className="flex items-center justify-between">
                <div>
                  <Label>Dark Mode</Label>
                  <p className="text-sm text-slate-500">Use dark theme for dashboard</p>
                </div>
                <Switch />
              </div>

              <div className="flex justify-end">
                <Button>Save Preferences</Button>
              </div>
            </CardContent>
          </Card>
        </TabsContent>
      </Tabs>
    </div>
  )
}
