import React from 'react';
import { useTranslation } from 'react-i18next';
import {
  User,
  Bell,
  Shield,
  Globe,
  Palette,
  Mail,
  Save,
} from 'lucide-react';
import { Card, CardContent, CardHeader, CardTitle } from '../components/common/Card';
import { Button } from '../components/common/Button';
import { Input } from '../components/common/Input';
import { Select } from '../components/common/Select';
import { Tabs, TabsContent, TabsList, TabsTrigger } from '../components/common/Tabs';
import { Badge } from '../components/common/Badge';
import { useDashboardStore } from '../stores/dashboard-store';
import { useTheme } from '../hooks/use-theme';
import { languages, currencies } from '../i18n';

export const Settings: React.FC = () => {
  const { t } = useTranslation();
  const { darkMode, toggleTheme } = useTheme();
  const { settings, updateSettings, language, setLanguage, currency, setCurrency } = useDashboardStore();

  const themeOptions = [
    { value: 'light', label: 'Light' },
    { value: 'dark', label: 'Dark' },
    { value: 'system', label: 'System' },
  ];

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-3xl font-bold tracking-tight">{t('settings.title')}</h1>
          <p className="text-muted-foreground">Manage your application settings</p>
        </div>
        <Button>
          <Save className="h-4 w-4 mr-2" />
          Save Changes
        </Button>
      </div>

      {/* Tabs */}
      <Tabs defaultValue="profile">
        <TabsList>
          <TabsTrigger value="profile">Profile</TabsTrigger>
          <TabsTrigger value="preferences">Preferences</TabsTrigger>
          <TabsTrigger value="notifications">Notifications</TabsTrigger>
          <TabsTrigger value="security">Security</TabsTrigger>
        </TabsList>

        {/* Profile Tab */}
        <TabsContent value="profile" className="mt-6">
          <div className="grid gap-6 max-w-2xl">
            <Card>
              <CardHeader>
                <CardTitle className="flex items-center gap-2">
                  <User className="h-5 w-5" />
                  Profile Information
                </CardTitle>
              </CardHeader>
              <CardContent className="space-y-4">
                <div className="flex items-center gap-4">
                  <div className="w-20 h-20 rounded-full bg-primary/10 flex items-center justify-center">
                    <span className="text-2xl font-bold text-primary">JD</span>
                  </div>
                  <div>
                    <Button variant="outline" size="sm">Change Photo</Button>
                  </div>
                </div>

                <Input
                  label="Full Name"
                  defaultValue="John Doe"
                />

                <Input
                  label="Email"
                  type="email"
                  defaultValue="john.doe@company.com"
                />

                <Input
                  label="Job Title"
                  defaultValue="Global Administrator"
                />

                <Input
                  label="Department"
                  defaultValueOperations"
                />
              </CardContent>
            </Card>
          </div>
        </TabsContent>

        {/* Preferences Tab */}
        <TabsContent value="preferences" className="mt-6">
          <div className="grid gap-6 max-w-2xl">
            {/* Appearance */}
            <Card>
              <CardHeader>
                <CardTitle className="flex items-center gap-2">
                  <Palette className="h-5 w-5" />
                  Appearance
                </CardTitle>
              </CardHeader>
              <CardContent className="space-y-4">
                <Select
                  label={t('settings.theme')}
                  value={darkMode ? 'dark' : 'light'}
                  onChange={(e) => {
                    if (e.target.value === 'dark' && !darkMode) toggleTheme();
                    if (e.target.value === 'light' && darkMode) toggleTheme();
                  }}
                  options={themeOptions}
                />

                <Select
                  label={t('settings.language')}
                  value={language}
                  onChange={(e) => setLanguage(e.target.value)}
                  options={languages.map((l) => ({ value: l.code, label: `${l.flag} ${l.name}` }))}
                />

                <Select
                  label={t('settings.currency')}
                  value={currency}
                  onChange={(e) => setCurrency(e.target.value)}
                  options={currencies.map((c) => ({ value: c.code, label: `${c.symbol} ${c.code} - ${c.name}` }))}
                />
              </CardContent>
            </Card>

            {/* Display Settings */}
            <Card>
              <CardHeader>
                <CardTitle className="flex items-center gap-2">
                  <Globe className="h-5 w-5" />
                  Display
                </CardTitle>
              </CardHeader>
              <CardContent className="space-y-4">
                <div className="flex items-center justify-between">
                  <div>
                    <p className="font-medium">Compact Mode</p>
                    <p className="text-sm text-muted-foreground">Use more compact layout</p>
                  </div>
                  <input
                    type="checkbox"
                    checked={settings.display.compactMode}
                    onChange={(e) => updateSettings({
                      display: { ...settings.display, compactMode: e.target.checked }
                    })}
                    className="rounded"
                  />
                </div>

                <div className="flex items-center justify-between">
                  <div>
                    <p className="font-medium">Show Trends</p>
                    <p className="text-sm text-muted-foreground">Display trend indicators</p>
                  </div>
                  <input
                    type="checkbox"
                    checked={settings.display.showTrends}
                    onChange={(e) => updateSettings({
                      display: { ...settings.display, showTrends: e.target.checked }
                    })}
                    className="rounded"
                  />
                </div>

                <div className="flex items-center justify-between">
                  <div>
                    <p className="font-medium">Show Targets</p>
                    <p className="text-sm text-muted-foreground">Display target values</p>
                  </div>
                  <input
                    type="checkbox"
                    checked={settings.display.showTargets}
                    onChange={(e) => updateSettings({
                      display: { ...settings.display, showTargets: e.target.checked }
                    })}
                    className="rounded"
                  />
                </div>

                <div className="flex items-center justify-between">
                  <div>
                    <p className="font-medium">Chart Animations</p>
                    <p className="text-sm text-muted-foreground">Animate chart transitions</p>
                  </div>
                  <input
                    type="checkbox"
                    checked={settings.display.chartAnimations}
                    onChange={(e) => updateSettings({
                      display: { ...settings.display, chartAnimations: e.target.checked }
                    })}
                    className="rounded"
                  />
                </div>
              </CardContent>
            </Card>
          </div>
        </TabsContent>

        {/* Notifications Tab */}
        <TabsContent value="notifications" className="mt-6">
          <div className="grid gap-6 max-w-2xl">
            <Card>
              <CardHeader>
                <CardTitle className="flex items-center gap-2">
                  <Bell className="h-5 w-5" />
                  Notification Preferences
                </CardTitle>
              </CardHeader>
              <CardContent className="space-y-4">
                <div className="flex items-center justify-between">
                  <div className="flex items-center gap-3">
                    <Mail className="h-5 w-5 text-muted-foreground" />
                    <div>
                      <p className="font-medium">Email Notifications</p>
                      <p className="text-sm text-muted-foreground">Receive updates via email</p>
                    </div>
                  </div>
                  <input
                    type="checkbox"
                    checked={settings.notifications.email}
                    onChange={(e) => updateSettings({
                      notifications: { ...settings.notifications, email: e.target.checked }
                    })}
                    className="rounded"
                  />
                </div>

                <div className="flex items-center justify-between">
                  <div className="flex items-center gap-3">
                    <Bell className="h-5 w-5 text-muted-foreground" />
                    <div>
                      <p className="font-medium">Push Notifications</p>
                      <p className="text-sm text-muted-foreground">Browser push notifications</p>
                    </div>
                  </div>
                  <input
                    type="checkbox"
                    checked={settings.notifications.push}
                    onChange={(e) => updateSettings({
                      notifications: { ...settings.notifications, push: e.target.checked }
                    })}
                    className="rounded"
                  />
                </div>

                <div className="flex items-center justify-between">
                  <div>
                    <p className="font-medium">Report Ready</p>
                    <p className="text-sm text-muted-foreground">Notify when reports are ready</p>
                  </div>
                  <input
                    type="checkbox"
                    checked={settings.notifications.reportReady}
                    onChange={(e) => updateSettings({
                      notifications: { ...settings.notifications, reportReady: e.target.checked }
                    })}
                    className="rounded"
                  />
                </div>

                <div className="flex items-center justify-between">
                  <div>
                    <p className="font-medium">Deadline Reminders</p>
                    <p className="text-sm text-muted-foreground">Remind about upcoming deadlines</p>
                  </div>
                  <input
                    type="checkbox"
                    checked={settings.notifications.deadlineReminder}
                    onChange={(e) => updateSettings({
                      notifications: { ...settings.notifications, deadlineReminder: e.target.checked }
                    })}
                    className="rounded"
                  />
                </div>

                <div className="flex items-center justify-between">
                  <div>
                    <p className="font-medium">Compliance Alerts</p>
                    <p className="text-sm text-muted-foreground">Important compliance updates</p>
                  </div>
                  <input
                    type="checkbox"
                    checked={settings.notifications.complianceAlert}
                    onChange={(e) => updateSettings({
                      notifications: { ...settings.notifications, complianceAlert: e.target.checked }
                    })}
                    className="rounded"
                  />
                </div>
              </CardContent>
            </Card>
          </div>
        </TabsContent>

        {/* Security Tab */}
        <TabsContent value="security" className="mt-6">
          <div className="grid gap-6 max-w-2xl">
            <Card>
              <CardHeader>
                <CardTitle className="flex items-center gap-2">
                  <Shield className="h-5 w-5" />
                  Security Settings
                </CardTitle>
              </CardHeader>
              <CardContent className="space-y-4">
                <Input
                  label="Current Password"
                  type="password"
                />

                <Input
                  label="New Password"
                  type="password"
                />

                <Input
                  label="Confirm Password"
                  type="password"
                />

                <Button>Change Password</Button>
              </CardContent>
            </Card>

            <Card>
              <CardHeader>
                <CardTitle>Two-Factor Authentication</CardTitle>
              </CardHeader>
              <CardContent>
                <div className="flex items-center justify-between">
                  <div>
                    <p className="font-medium">Status</p>
                    <Badge variant="success" size="sm">Enabled</Badge>
                  </div>
                  <Button variant="outline">Configure</Button>
                </div>
              </CardContent>
            </Card>
          </div>
        </TabsContent>
      </Tabs>
    </div>
  );
};
