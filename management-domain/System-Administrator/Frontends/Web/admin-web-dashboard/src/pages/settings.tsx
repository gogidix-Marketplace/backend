import { useState } from 'react'
import { Settings as SettingsIcon, Save, Bell, Lock, Server, Palette, User } from 'lucide-react'
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@shared/components/ui/card'
import { Badge } from '@shared/components/ui/badge'
import { Button } from '@shared/components/ui/button'
import { Input } from '@shared/components/ui/input'
import { Label } from '@shared/components/ui/label'
import { Switch } from '@shared/components/ui/switch'
import { Textarea } from '@shared/components/ui/textarea'
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from '@shared/components/ui/select'
import {
  Tabs,
  TabsContent,
  TabsList,
  TabsTrigger,
} from '@shared/components/ui/tabs'
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from '@shared/components/ui/dialog'

export default function SettingsPage() {
  const [settings, setSettings] = useState({
    // General
    siteName: 'Gogidix Admin Portal',
    timezone: 'UTC',
    language: 'en',
    // Notifications
    emailAlerts: true,
    pushNotifications: true,
    smsAlerts: false,
    alertSeverity: 'medium',
    // Security
    sessionTimeout: 30,
    mfaRequired: false,
    passwordMinLength: 8,
    passwordExpiry: 90,
    // System
    maintenanceMode: false,
    debugMode: false,
    maxFileSize: 10,
    backupFrequency: 'daily',
  })

  const updateSetting = <K extends keyof typeof settings>(key: K, value: typeof settings[K]) => {
    setSettings(prev => ({ ...prev, [key]: value }))
  }

  return (
    <div className="space-y-6">
      {/* Page Header */}
      <div className="page-header">
        <h1 className="page-title">Settings</h1>
        <p className="page-description">
          Configure system settings and preferences
        </p>
      </div>

      <Tabs defaultValue="general">
        <TabsList>
          <TabsTrigger value="general">General</TabsTrigger>
          <TabsTrigger value="notifications">Notifications</TabsTrigger>
          <TabsTrigger value="security">Security</TabsTrigger>
          <TabsTrigger value="system">System</TabsTrigger>
          <TabsTrigger value="profile">Profile</TabsTrigger>
        </TabsList>

        {/* General Settings */}
        <TabsContent value="general">
          <Card>
            <CardHeader>
              <CardTitle>General Settings</CardTitle>
              <CardDescription>Basic system configuration</CardDescription>
            </CardHeader>
            <CardContent className="space-y-6">
              <div className="grid gap-2">
                <Label htmlFor="siteName">Site Name</Label>
                <Input
                  id="siteName"
                  value={settings.siteName}
                  onChange={(e) => updateSetting('siteName', e.target.value)}
                />
              </div>

              <div className="grid gap-2">
                <Label htmlFor="timezone">Timezone</Label>
                <Select value={settings.timezone} onValueChange={(v) => updateSetting('timezone', v)}>
                  <SelectTrigger id="timezone">
                    <SelectValue />
                  </SelectTrigger>
                  <SelectContent>
                    <SelectItem value="UTC">UTC</SelectItem>
                    <SelectItem value="America/New_York">Eastern Time</SelectItem>
                    <SelectItem value="America/Chicago">Central Time</SelectItem>
                    <SelectItem value="America/Los_Angeles">Pacific Time</SelectItem>
                    <SelectItem value="Europe/London">London</SelectItem>
                    <SelectItem value="Europe/Paris">Paris</SelectItem>
                    <SelectItem value="Africa/Lagos">Lagos</SelectItem>
                    <SelectItem value="Africa/Nairobi">Nairobi</SelectItem>
                  </SelectContent>
                </Select>
              </div>

              <div className="grid gap-2">
                <Label htmlFor="language">Language</Label>
                <Select value={settings.language} onValueChange={(v) => updateSetting('language', v)}>
                  <SelectTrigger id="language">
                    <SelectValue />
                  </SelectTrigger>
                  <SelectContent>
                    <SelectItem value="en">English</SelectItem>
                    <SelectItem value="es">Spanish</SelectItem>
                    <SelectItem value="fr">French</SelectItem>
                  </SelectContent>
                </Select>
              </div>

              <div className="flex justify-end">
                <Button variant="admin">
                  <Save className="mr-2 h-4 w-4" />
                  Save Changes
                </Button>
              </div>
            </CardContent>
          </Card>
        </TabsContent>

        {/* Notification Settings */}
        <TabsContent value="notifications">
          <Card>
            <CardHeader>
              <CardTitle className="flex items-center gap-2">
                <Bell className="h-5 w-5" />
                Notification Settings
              </CardTitle>
              <CardDescription>Configure how and when you receive notifications</CardDescription>
            </CardHeader>
            <CardContent className="space-y-6">
              <div className="flex items-center justify-between">
                <div>
                  <Label>Email Alerts</Label>
                  <p className="text-sm text-muted-foreground">Receive critical alerts via email</p>
                </div>
                <Switch
                  checked={settings.emailAlerts}
                  onCheckedChange={(v) => updateSetting('emailAlerts', v)}
                />
              </div>

              <div className="flex items-center justify-between">
                <div>
                  <Label>Push Notifications</Label>
                  <p className="text-sm text-muted-foreground">Browser push notifications</p>
                </div>
                <Switch
                  checked={settings.pushNotifications}
                  onCheckedChange={(v) => updateSetting('pushNotifications', v)}
                />
              </div>

              <div className="flex items-center justify-between">
                <div>
                  <Label>SMS Alerts</Label>
                  <p className="text-sm text-muted-foreground">Receive critical alerts via SMS</p>
                </div>
                <Switch
                  checked={settings.smsAlerts}
                  onCheckedChange={(v) => updateSetting('smsAlerts', v)}
                />
              </div>

              <div className="grid gap-2">
                <Label>Minimum Alert Severity</Label>
                <Select value={settings.alertSeverity} onValueChange={(v) => updateSetting('alertSeverity', v)}>
                  <SelectTrigger>
                    <SelectValue />
                  </SelectTrigger>
                  <SelectContent>
                    <SelectItem value="low">Low - All alerts</SelectItem>
                    <SelectItem value="medium">Medium - Important</SelectItem>
                    <SelectItem value="high">High - Critical only</SelectItem>
                  </SelectContent>
                </Select>
              </div>

              <div className="flex justify-end">
                <Button variant="admin">
                  <Save className="mr-2 h-4 w-4" />
                  Save Changes
                </Button>
              </div>
            </CardContent>
          </Card>
        </TabsContent>

        {/* Security Settings */}
        <TabsContent value="security">
          <Card>
            <CardHeader>
              <CardTitle className="flex items-center gap-2">
                <Lock className="h-5 w-5" />
                Security Settings
              </CardTitle>
              <CardDescription>Configure security policies and authentication</CardDescription>
            </CardHeader>
            <CardContent className="space-y-6">
              <div className="grid gap-2">
                <Label>Session Timeout (minutes)</Label>
                <Input
                  type="number"
                  value={settings.sessionTimeout}
                  onChange={(e) => updateSetting('sessionTimeout', parseInt(e.target.value))}
                  min={5}
                  max={480}
                />
                <p className="text-xs text-muted-foreground">Users will be logged out after inactivity</p>
              </div>

              <div className="flex items-center justify-between">
                <div>
                  <Label>Require Multi-Factor Authentication</Label>
                  <p className="text-sm text-muted-foreground">Enforce MFA for all admin users</p>
                </div>
                <Switch
                  checked={settings.mfaRequired}
                  onCheckedChange={(v) => updateSetting('mfaRequired', v)}
                />
              </div>

              <div className="grid gap-2">
                <Label>Minimum Password Length</Label>
                <Input
                  type="number"
                  value={settings.passwordMinLength}
                  onChange={(e) => updateSetting('passwordMinLength', parseInt(e.target.value))}
                  min={8}
                  max={32}
                />
              </div>

              <div className="grid gap-2">
                <Label>Password Expiry (days)</Label>
                <Input
                  type="number"
                  value={settings.passwordExpiry}
                  onChange={(e) => updateSetting('passwordExpiry', parseInt(e.target.value))}
                  min={30}
                  max={365}
                />
                <p className="text-xs text-muted-foreground">Set to 0 for no expiry</p>
              </div>

              <div className="flex justify-end">
                <Button variant="admin">
                  <Save className="mr-2 h-4 w-4" />
                  Save Changes
                </Button>
              </div>
            </CardContent>
          </Card>
        </TabsContent>

        {/* System Settings */}
        <TabsContent value="system">
          <Card>
            <CardHeader>
              <CardTitle className="flex items-center gap-2">
                <Server className="h-5 w-5" />
                System Settings
              </CardTitle>
              <CardDescription>Configure system-level settings</CardDescription>
            </CardHeader>
            <CardContent className="space-y-6">
              <div className="flex items-center justify-between">
                <div>
                  <Label>Maintenance Mode</Label>
                  <p className="text-sm text-muted-foreground">Take the system offline for maintenance</p>
                </div>
                <Switch
                  checked={settings.maintenanceMode}
                  onCheckedChange={(v) => updateSetting('maintenanceMode', v)}
                />
              </div>

              <div className="flex items-center justify-between">
                <div>
                  <Label>Debug Mode</Label>
                  <p className="text-sm text-muted-foreground">Enable detailed logging and error messages</p>
                </div>
                <Switch
                  checked={settings.debugMode}
                  onCheckedChange={(v) => updateSetting('debugMode', v)}
                />
              </div>

              <div className="grid gap-2">
                <Label>Maximum File Upload Size (MB)</Label>
                <Input
                  type="number"
                  value={settings.maxFileSize}
                  onChange={(e) => updateSetting('maxFileSize', parseInt(e.target.value))}
                  min={1}
                  max={100}
                />
              </div>

              <div className="grid gap-2">
                <Label>Backup Frequency</Label>
                <Select value={settings.backupFrequency} onValueChange={(v) => updateSetting('backupFrequency', v)}>
                  <SelectTrigger>
                    <SelectValue />
                  </SelectTrigger>
                  <SelectContent>
                    <SelectItem value="hourly">Hourly</SelectItem>
                    <SelectItem value="daily">Daily</SelectItem>
                    <SelectItem value="weekly">Weekly</SelectItem>
                    <SelectItem value="monthly">Monthly</SelectItem>
                  </SelectContent>
                </Select>
              </div>

              <div className="rounded-lg border p-4 bg-slate-50 dark:bg-slate-800">
                <h4 className="font-medium mb-2">System Information</h4>
                <div className="grid grid-cols-2 gap-4 text-sm">
                  <div>
                    <span className="text-muted-foreground">Version:</span>
                    <span className="ml-2">v2.4.0</span>
                  </div>
                  <div>
                    <span className="text-muted-foreground">Build:</span>
                    <span className="ml-2">2024.03.01</span>
                  </div>
                  <div>
                    <span className="text-muted-foreground">Environment:</span>
                    <span className="ml-2">Production</span>
                  </div>
                  <div>
                    <span className="text-muted-foreground">Database:</span>
                    <span className="ml-2">PostgreSQL 14</span>
                  </div>
                </div>
              </div>

              <div className="flex justify-end">
                <Button variant="admin">
                  <Save className="mr-2 h-4 w-4" />
                  Save Changes
                </Button>
              </div>
            </CardContent>
          </Card>
        </TabsContent>

        {/* Profile Settings */}
        <TabsContent value="profile">
          <Card>
            <CardHeader>
              <CardTitle className="flex items-center gap-2">
                <User className="h-5 w-5" />
                Profile Settings
              </CardTitle>
              <CardDescription>Manage your account settings</CardDescription>
            </CardHeader>
            <CardContent className="space-y-6">
              <div className="flex items-center gap-4">
                <div className="h-20 w-20 rounded-full bg-admin-blue/10 flex items-center justify-center">
                  <span className="text-2xl font-bold text-admin-blue">RC</span>
                </div>
                <div>
                  <Button variant="outline" size="sm">Change Avatar</Button>
                </div>
              </div>

              <div className="grid grid-cols-2 gap-4">
                <div className="grid gap-2">
                  <Label>First Name</Label>
                  <Input defaultValue="Robert" />
                </div>
                <div className="grid gap-2">
                  <Label>Last Name</Label>
                  <Input defaultValue="Chen" />
                </div>
              </div>

              <div className="grid gap-2">
                <Label>Email</Label>
                <Input type="email" defaultValue="admin@gogidix.com" />
              </div>

              <div className="grid gap-2">
                <Label>Phone</Label>
                <Input type="tel" defaultValue="+1 (555) 123-4567" />
              </div>

              <Dialog>
                <DialogTrigger asChild>
                  <Button variant="outline">Change Password</Button>
                </DialogTrigger>
                <DialogContent>
                  <DialogHeader>
                    <DialogTitle>Change Password</DialogTitle>
                    <DialogDescription>
                      Enter your current and new password
                    </DialogDescription>
                  </DialogHeader>
                  <div className="grid gap-4 py-4">
                    <div className="grid gap-2">
                      <Label>Current Password</Label>
                      <Input type="password" />
                    </div>
                    <div className="grid gap-2">
                      <Label>New Password</Label>
                      <Input type="password" />
                    </div>
                    <div className="grid gap-2">
                      <Label>Confirm Password</Label>
                      <Input type="password" />
                    </div>
                  </div>
                  <DialogFooter>
                    <Button variant="outline">Cancel</Button>
                    <Button variant="admin">Update Password</Button>
                  </DialogFooter>
                </DialogContent>
              </Dialog>

              <div className="flex justify-end">
                <Button variant="admin">
                  <Save className="mr-2 h-4 w-4" />
                  Save Changes
                </Button>
              </div>
            </CardContent>
          </Card>
        </TabsContent>
      </Tabs>
    </div>
  )
}
