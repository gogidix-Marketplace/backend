import { useState } from 'react'
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@shared/components/ui/card'
import { Badge } from '@shared/components/ui/badge'
import { Button } from '@shared/components/ui/button'
import { Input } from '@shared/components/ui/input'
import { Label } from '@shared/components/ui/label'
import { Textarea } from '@shared/components/ui/textarea'
import { Switch } from '@shared/components/ui/switch'
import { Tabs, TabsContent, TabsList, TabsTrigger } from '@shared/components/ui/tabs'
import { Avatar, AvatarFallback, AvatarImage } from '@shared/components/ui/avatar'
import { User, Bell, Layout, Palette, Shield } from 'lucide-react'
import { cn } from '@shared/utils/cn'

/**
 * CEO Settings Page
 *
 * Components:
 * - ProfileSettings - Personal information, avatar, contact details
 * - NotificationPreferences - Alert types, frequency, quiet hours
 * - DashboardPreferences - Layout, widgets, defaults, theme
 * - SecuritySettings - Password, 2FA, sessions
 */

interface UserProfile {
  firstName: string
  lastName: string
  email: string
  phone: string
  title: string
  department: string
  timezone: string
  language: string
  avatar: string
}

interface NotificationSettings {
  emailAlerts: boolean
  pushAlerts: boolean
  smsAlerts: boolean
  criticalOnly: boolean
  quietHours: boolean
  quietStart: string
  quietEnd: string
  weeklyDigest: boolean
  monthlyReport: boolean
}

interface DashboardSettings {
  defaultView: 'overview' | 'analytics' | 'strategy'
  defaultTimeRange: 'today' | 'week' | 'month' | 'quarter'
  compactMode: boolean
  showSparklines: boolean
  refreshInterval: number
  widgets: string[]
}

const mockProfile: UserProfile = {
  firstName: 'John',
  lastName: 'Mitchell',
  email: 'ceo@gogidix.com',
  phone: '+1 (555) 123-4567',
  title: 'Chief Executive Officer',
  department: 'Executive',
  timezone: 'America/New_York',
  language: 'en',
  avatar: 'https://api.dicebear.com/7.x/initials/svg?seed=JM',
}

const mockNotifications: NotificationSettings = {
  emailAlerts: true,
  pushAlerts: true,
  smsAlerts: false,
  criticalOnly: false,
  quietHours: false,
  quietStart: '22:00',
  quietEnd: '08:00',
  weeklyDigest: true,
  monthlyReport: true,
}

const mockDashboard: DashboardSettings = {
  defaultView: 'overview',
  defaultTimeRange: 'month',
  compactMode: false,
  showSparklines: true,
  refreshInterval: 30,
  widgets: ['kpi', 'approvals', 'alerts', 'regional'],
}

export default function SettingsPage() {
  const [activeTab, setActiveTab] = useState('profile')
  const [profile, setProfile] = useState<UserProfile>(mockProfile)
  const [notifications, setNotifications] = useState<NotificationSettings>(mockNotifications)
  const [dashboard, setDashboard] = useState<DashboardSettings>(mockDashboard)
  const [saving, setSaving] = useState(false)

  const handleSave = async () => {
    setSaving(true)
    // Simulate API call
    await new Promise(resolve => setTimeout(resolve, 1000))
    setSaving(false)
  }

  return (
    <div className="space-y-6">
      {/* Page Header */}
      <div className="page-header">
        <h1 className="page-title">Settings</h1>
        <p className="page-description">
          Manage your profile, preferences, and account settings
        </p>
      </div>

      <Tabs value={activeTab} onValueChange={setActiveTab} className="space-y-6">
        <TabsList className="grid w-full max-w-md grid-cols-4">
          <TabsTrigger value="profile">Profile</TabsTrigger>
          <TabsTrigger value="notifications">Notifications</TabsTrigger>
          <TabsTrigger value="dashboard">Dashboard</TabsTrigger>
          <TabsTrigger value="security">Security</TabsTrigger>
        </TabsList>

        {/* Profile Settings */}
        <TabsContent value="profile" className="space-y-6">
          <Card>
            <CardHeader>
              <CardTitle className="flex items-center gap-2">
                <User className="h-5 w-5" />
                Profile Information
              </CardTitle>
              <CardDescription>
                Update your personal information and contact details
              </CardDescription>
            </CardHeader>
            <CardContent className="space-y-6">
              {/* Avatar */}
              <div className="flex items-center gap-4">
                <Avatar className="h-20 w-20">
                  <AvatarImage src={profile.avatar} />
                  <AvatarFallback>{profile.firstName[0]}{profile.lastName[0]}</AvatarFallback>
                </Avatar>
                <div>
                  <Button variant="outline" size="sm">Change Avatar</Button>
                  <p className="text-xs text-muted-foreground mt-1">
                    JPG, GIF or PNG. Max 2MB.
                  </p>
                </div>
              </div>

              {/* Form Fields */}
              <div className="grid md:grid-cols-2 gap-4">
                <div className="space-y-2">
                  <Label>First Name</Label>
                  <Input value={profile.firstName} onChange={(e) => setProfile({ ...profile, firstName: e.target.value })} />
                </div>
                <div className="space-y-2">
                  <Label>Last Name</Label>
                  <Input value={profile.lastName} onChange={(e) => setProfile({ ...profile, lastName: e.target.value })} />
                </div>
                <div className="space-y-2">
                  <Label>Email</Label>
                  <Input type="email" value={profile.email} onChange={(e) => setProfile({ ...profile, email: e.target.value })} />
                </div>
                <div className="space-y-2">
                  <Label>Phone</Label>
                  <Input type="tel" value={profile.phone} onChange={(e) => setProfile({ ...profile, phone: e.target.value })} />
                </div>
                <div className="space-y-2">
                  <Label>Title</Label>
                  <Input value={profile.title} onChange={(e) => setProfile({ ...profile, title: e.target.value })} />
                </div>
                <div className="space-y-2">
                  <Label>Department</Label>
                  <Input value={profile.department} onChange={(e) => setProfile({ ...profile, department: e.target.value })} />
                </div>
                <div className="space-y-2">
                  <Label>Timezone</Label>
                  <select className="flex h-10 w-full rounded-md border border-input bg-background px-3 py-2 text-sm">
                    <option>America/New_York</option>
                    <option>America/Chicago</option>
                    <option>Europe/London</option>
                    <option>Africa/Lagos</option>
                  </select>
                </div>
                <div className="space-y-2">
                  <Label>Language</Label>
                  <select className="flex h-10 w-full rounded-md border border-input bg-background px-3 py-2 text-sm">
                    <option>English</option>
                    <option>French</option>
                    <option>Spanish</option>
                  </select>
                </div>
              </div>

              <div className="flex justify-end">
                <Button onClick={handleSave} disabled={saving}>
                  {saving ? 'Saving...' : 'Save Changes'}
                </Button>
              </div>
            </CardContent>
          </Card>
        </TabsContent>

        {/* Notification Preferences */}
        <TabsContent value="notifications" className="space-y-6">
          <Card>
            <CardHeader>
              <CardTitle className="flex items-center gap-2">
                <Bell className="h-5 w-5" />
                Notification Preferences
              </CardTitle>
              <CardDescription>
                Choose how you want to receive alerts and updates
              </CardDescription>
            </CardHeader>
            <CardContent className="space-y-6">
              {/* Alert Channels */}
              <div className="space-y-4">
                <h3 className="text-sm font-medium">Alert Channels</h3>
                <div className="space-y-3">
                  <div className="flex items-center justify-between">
                    <div>
                      <p className="font-medium">Email Notifications</p>
                      <p className="text-sm text-muted-foreground">Receive alerts via email</p>
                    </div>
                    <Switch checked={notifications.emailAlerts} onCheckedChange={(checked) => setNotifications({ ...notifications, emailAlerts: checked })} />
                  </div>
                  <div className="flex items-center justify-between">
                    <div>
                      <p className="font-medium">Push Notifications</p>
                      <p className="text-sm text-muted-foreground">Browser push notifications for urgent alerts</p>
                    </div>
                    <Switch checked={notifications.pushAlerts} onCheckedChange={(checked) => setNotifications({ ...notifications, pushAlerts: checked })} />
                  </div>
                  <div className="flex items-center justify-between">
                    <div>
                      <p className="font-medium">SMS Alerts</p>
                      <p className="text-sm text-muted-foreground">Critical alerts via text message</p>
                    </div>
                    <Switch checked={notifications.smsAlerts} onCheckedChange={(checked) => setNotifications({ ...notifications, smsAlerts: checked })} />
                  </div>
                </div>
              </div>

              {/* Alert Priority */}
              <div className="space-y-4">
                <h3 className="text-sm font-medium">Alert Priority</h3>
                <div className="flex items-center justify-between">
                  <div>
                    <p className="font-medium">Critical Alerts Only</p>
                    <p className="text-sm text-muted-foreground">Only receive notifications for urgent matters</p>
                  </div>
                  <Switch checked={notifications.criticalOnly} onCheckedChange={(checked) => setNotifications({ ...notifications, criticalOnly: checked })} />
                </div>
              </div>

              {/* Quiet Hours */}
              <div className="space-y-4">
                <h3 className="text-sm font-medium">Quiet Hours</h3>
                <div className="flex items-center justify-between">
                  <div>
                    <p className="font-medium">Enable Quiet Hours</p>
                    <p className="text-sm text-muted-foreground">Suppress notifications during specific hours</p>
                  </div>
                  <Switch checked={notifications.quietHours} onCheckedChange={(checked) => setNotifications({ ...notifications, quietHours: checked })} />
                </div>
                {notifications.quietHours && (
                  <div className="grid grid-cols-2 gap-4 mt-2">
                    <div className="space-y-2">
                      <Label>Start Time</Label>
                      <Input type="time" value={notifications.quietStart} onChange={(e) => setNotifications({ ...notifications, quietStart: e.target.value })} />
                    </div>
                    <div className="space-y-2">
                      <Label>End Time</Label>
                      <Input type="time" value={notifications.quietEnd} onChange={(e) => setNotifications({ ...notifications, quietEnd: e.target.value })} />
                    </div>
                  </div>
                )}
              </div>

              {/* Scheduled Reports */}
              <div className="space-y-4">
                <h3 className="text-sm font-medium">Scheduled Reports</h3>
                <div className="space-y-3">
                  <div className="flex items-center justify-between">
                    <div>
                      <p className="font-medium">Weekly Digest</p>
                      <p className="text-sm text-muted-foreground">Summary of weekly metrics and activities</p>
                    </div>
                    <Switch checked={notifications.weeklyDigest} onCheckedChange={(checked) => setNotifications({ ...notifications, weeklyDigest: checked })} />
                  </div>
                  <div className="flex items-center justify-between">
                    <div>
                      <p className="font-medium">Monthly Executive Report</p>
                      <p className="text-sm text-muted-foreground">Comprehensive monthly report</p>
                    </div>
                    <Switch checked={notifications.monthlyReport} onCheckedChange={(checked) => setNotifications({ ...notifications, monthlyReport: checked })} />
                  </div>
                </div>
              </div>

              <div className="flex justify-end">
                <Button onClick={handleSave} disabled={saving}>
                  {saving ? 'Saving...' : 'Save Changes'}
                </Button>
              </div>
            </CardContent>
          </Card>
        </TabsContent>

        {/* Dashboard Preferences */}
        <TabsContent value="dashboard" className="space-y-6">
          <Card>
            <CardHeader>
              <CardTitle className="flex items-center gap-2">
                <Layout className="h-5 w-5" />
                Dashboard Preferences
              </CardTitle>
              <CardDescription>
                Customize your dashboard layout and default settings
              </CardDescription>
            </CardHeader>
            <CardContent className="space-y-6">
              {/* Default View */}
              <div className="space-y-2">
                <Label>Default View</Label>
                <select
                  className="flex h-10 w-full rounded-md border border-input bg-background px-3 py-2 text-sm"
                  value={dashboard.defaultView}
                  onChange={(e) => setDashboard({ ...dashboard, defaultView: e.target.value as any })}
                >
                  <option value="overview">Overview</option>
                  <option value="analytics">Analytics</option>
                  <option value="strategy">Strategy</option>
                </select>
              </div>

              {/* Default Time Range */}
              <div className="space-y-2">
                <Label>Default Time Range</Label>
                <select
                  className="flex h-10 w-full rounded-md border border-input bg-background px-3 py-2 text-sm"
                  value={dashboard.defaultTimeRange}
                  onChange={(e) => setDashboard({ ...dashboard, defaultTimeRange: e.target.value as any })}
                >
                  <option value="today">Today</option>
                  <option value="week">This Week</option>
                  <option value="month">This Month</option>
                  <option value="quarter">This Quarter</option>
                </select>
              </div>

              {/* Display Options */}
              <div className="space-y-4">
                <h3 className="text-sm font-medium">Display Options</h3>
                <div className="space-y-3">
                  <div className="flex items-center justify-between">
                    <div>
                      <p className="font-medium">Compact Mode</p>
                      <p className="text-sm text-muted-foreground">Use more compact widget layout</p>
                    </div>
                    <Switch checked={dashboard.compactMode} onCheckedChange={(checked) => setDashboard({ ...dashboard, compactMode: checked })} />
                  </div>
                  <div className="flex items-center justify-between">
                    <div>
                      <p className="font-medium">Show Sparklines</p>
                      <p className="text-sm text-muted-foreground">Display trend indicators on metrics</p>
                    </div>
                    <Switch checked={dashboard.showSparklines} onCheckedChange={(checked) => setDashboard({ ...dashboard, showSparklines: checked })} />
                  </div>
                </div>
              </div>

              {/* Data Refresh */}
              <div className="space-y-2">
                <Label>Data Refresh Interval (seconds)</Label>
                <Input
                  type="number"
                  value={dashboard.refreshInterval}
                  onChange={(e) => setDashboard({ ...dashboard, refreshInterval: parseInt(e.target.value) || 30 })}
                  min={10}
                  max={300}
                />
                <p className="text-xs text-muted-foreground">
                  How often to refresh real-time data (10-300 seconds)
                </p>
              </div>

              {/* Active Widgets */}
              <div className="space-y-2">
                <Label>Active Widgets</Label>
                <div className="grid grid-cols-2 gap-2">
                  {['kpi', 'approvals', 'alerts', 'regional', 'trends', 'insights'].map((widget) => (
                    <div key={widget} className="flex items-center gap-2 p-2 rounded border">
                      <Switch
                        checked={dashboard.widgets.includes(widget)}
                        onCheckedChange={(checked) => {
                          setDashboard({
                            ...dashboard,
                            widgets: checked
                              ? [...dashboard.widgets, widget]
                              : dashboard.widgets.filter(w => w !== widget)
                          })
                        }}
                      />
                      <span className="text-sm capitalize">{widget}</span>
                    </div>
                  ))}
                </div>
              </div>

              <div className="flex justify-end">
                <Button onClick={handleSave} disabled={saving}>
                  {saving ? 'Saving...' : 'Save Changes'}
                </Button>
              </div>
            </CardContent>
          </Card>
        </TabsContent>

        {/* Security Settings */}
        <TabsContent value="security" className="space-y-6">
          <Card>
            <CardHeader>
              <CardTitle className="flex items-center gap-2">
                <Shield className="h-5 w-5" />
                Security Settings
              </CardTitle>
              <CardDescription>
                Manage your password, two-factor authentication, and sessions
              </CardDescription>
            </CardHeader>
            <CardContent className="space-y-6">
              {/* Password */}
              <div className="space-y-4">
                <h3 className="text-sm font-medium">Password</h3>
                <div className="space-y-3">
                  <Button variant="outline" className="w-full justify-start">
                    Change Password
                  </Button>
                </div>
              </div>

              {/* Two-Factor Authentication */}
              <div className="space-y-4">
                <h3 className="text-sm font-medium">Two-Factor Authentication</h3>
                <div className="flex items-center justify-between p-3 rounded-lg border">
                  <div>
                    <p className="font-medium">Authenticator App</p>
                    <p className="text-sm text-muted-foreground">Use an authenticator app for 2FA</p>
                  </div>
                  <Badge variant="success" className="gap-1">
                    <span className="h-2 w-2 rounded-full bg-green-500" />
                    Enabled
                  </Badge>
                </div>
                <Button variant="outline" className="w-full">
                  Manage 2FA Settings
                </Button>
              </div>

              {/* Active Sessions */}
              <div className="space-y-4">
                <h3 className="text-sm font-medium">Active Sessions</h3>
                <div className="space-y-3">
                  <div className="flex items-center justify-between p-3 rounded-lg border border-primary bg-primary/5">
                    <div>
                      <p className="font-medium">Current Session</p>
                      <p className="text-sm text-muted-foreground">
                        Windows • Chrome • New York, USA
                      </p>
                    </div>
                    <Badge variant="outline">Current</Badge>
                  </div>
                  <div className="flex items-center justify-between p-3 rounded-lg border">
                    <div>
                      <p className="font-medium">Mobile Session</p>
                      <p className="text-sm text-muted-foreground">
                        iOS • Safari • New York, USA
                      </p>
                      <p className="text-xs text-muted-foreground">Last active: 2 hours ago</p>
                    </div>
                    <Button variant="ghost" size="sm">Revoke</Button>
                  </div>
                </div>
              </div>

              {/* Danger Zone */}
              <div className="space-y-4 pt-4 border-t">
                <h3 className="text-sm font-medium text-destructive">Danger Zone</h3>
                <div className="flex items-center justify-between">
                  <div>
                    <p className="font-medium">Sign out from all devices</p>
                    <p className="text-sm text-muted-foreground">
                      This will sign you out from all devices including mobile
                    </p>
                  </div>
                  <Button variant="destructive" size="sm">
                    Sign Out All
                  </Button>
                </div>
              </div>
            </CardContent>
          </Card>
        </TabsContent>
      </Tabs>
    </div>
  )
}
