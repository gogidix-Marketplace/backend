import { useState } from 'react'
import {
  User,
  Bell,
  Shield,
  Globe,
  Users,
  Slack,
  Mail,
  Phone,
  Save,
} from 'lucide-react'
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@shared/components/ui/card'
import { Badge } from '@shared/components/ui/badge'
import { Button } from '@shared/components/ui/button'
import { Input } from '@shared/components/ui/input'
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
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from '@shared/components/ui/table'
import { Switch } from '@shared/components/ui/switch'
import { useAuthStore } from '@shared/stores/authStore'

export default function SettingsPage() {
  const { user } = useAuthStore()
  const [saveStatus, setSaveStatus] = useState<'idle' | 'saving' | 'saved'>('idle')

  const handleSave = () => {
    setSaveStatus('saving')
    setTimeout(() => {
      setSaveStatus('saved')
      setTimeout(() => setSaveStatus('idle'), 2000)
    }, 1000)
  }

  return (
    <div className="space-y-6">
      {/* Page Header */}
      <div className="page-header">
        <h1 className="page-title">Settings</h1>
        <p className="page-description">
          Manage your support dashboard settings
        </p>
      </div>

      <Tabs defaultValue="profile" className="space-y-4">
        <TabsList className="w-full justify-start">
          <TabsTrigger value="profile">Profile</TabsTrigger>
          <TabsTrigger value="notifications">Notifications</TabsTrigger>
          <TabsTrigger value="teams">Teams</TabsTrigger>
          <TabsTrigger value="channels">Channels</TabsTrigger>
          <TabsTrigger value="sla">SLA Rules</TabsTrigger>
        </TabsList>

        {/* Profile Tab */}
        <TabsContent value="profile" className="space-y-4">
          <div className="grid gap-6 md:grid-cols-3">
            <Card className="md:col-span-1">
              <CardHeader>
                <div className="flex flex-col items-center">
                  <div className="h-24 w-24 rounded-full bg-[#1976D2] text-white flex items-center justify-center text-3xl mb-4">
                    {user?.firstName?.[0]}{user?.lastName?.[0]}
                  </div>
                  <CardTitle>{user?.displayName}</CardTitle>
                  <CardDescription>{user?.role?.replace('_', ' ')}</CardDescription>
                  <Badge variant="outline" className="mt-2">{user?.department}</Badge>
                </div>
              </CardHeader>
              <CardContent className="space-y-2">
                <div className="text-sm space-y-1">
                  <p className="text-muted-foreground">Email</p>
                  <p className="font-medium">{user?.email}</p>
                </div>
                {user?.country && (
                  <div className="text-sm space-y-1">
                    <p className="text-muted-foreground">Country</p>
                    <p className="font-medium">{user.country}</p>
                  </div>
                )}
                <div className="text-sm space-y-1">
                  <p className="text-muted-foreground">Status</p>
                  <Badge variant="success">Active</Badge>
                </div>
              </CardContent>
            </Card>

            <Card className="md:col-span-2">
              <CardHeader>
                <CardTitle>Profile Information</CardTitle>
                <CardDescription>Update your personal information</CardDescription>
              </CardHeader>
              <CardContent className="space-y-4">
                <div className="grid gap-4 md:grid-cols-2">
                  <div className="space-y-2">
                    <label className="text-sm font-medium">First Name</label>
                    <Input defaultValue={user?.firstName} />
                  </div>
                  <div className="space-y-2">
                    <label className="text-sm font-medium">Last Name</label>
                    <Input defaultValue={user?.lastName} />
                  </div>
                  <div className="space-y-2 md:col-span-2">
                    <label className="text-sm font-medium">Email</label>
                    <Input defaultValue={user?.email} type="email" />
                  </div>
                  <div className="space-y-2">
                    <label className="text-sm font-medium">Phone</label>
                    <Input placeholder="+1-555-0100" />
                  </div>
                  <div className="space-y-2">
                    <label className="text-sm font-medium">Timezone</label>
                    <Select defaultValue="utc">
                      <SelectTrigger>
                        <SelectValue />
                      </SelectTrigger>
                      <SelectContent>
                        <SelectItem value="utc">UTC</SelectItem>
                        <SelectItem value="est">Eastern Time</SelectItem>
                        <SelectItem value="pst">Pacific Time</SelectItem>
                        <SelectItem value="wAT">West Africa Time</SelectItem>
                      </SelectContent>
                    </Select>
                  </div>
                  <div className="space-y-2 md:col-span-2">
                    <label className="text-sm font-medium">Bio</label>
                    <Textarea placeholder="Tell us about yourself..." rows={3} />
                  </div>
                </div>
                <div className="flex justify-end">
                  <Button onClick={handleSave} disabled={saveStatus === 'saving'}>
                    {saveStatus === 'saved' ? 'Saved!' : saveStatus === 'saving' ? 'Saving...' : 'Save Changes'}
                  </Button>
                </div>
              </CardContent>
            </Card>
          </div>
        </TabsContent>

        {/* Notifications Tab */}
        <TabsContent value="notifications" className="space-y-4">
          <Card>
            <CardHeader>
              <CardTitle>Notification Preferences</CardTitle>
              <CardDescription>Manage how you receive notifications</CardDescription>
            </CardHeader>
            <CardContent className="space-y-6">
              <div className="space-y-4">
                <div className="flex items-center justify-between">
                  <div className="space-y-0.5">
                    <div className="flex items-center gap-2">
                      <Mail className="h-4 w-4 text-muted-foreground" />
                      <label className="font-medium">Email Notifications</label>
                    </div>
                    <p className="text-sm text-muted-foreground">Receive notifications via email</p>
                  </div>
                  <Switch defaultChecked />
                </div>
                <div className="flex items-center justify-between">
                  <div className="space-y-0.5">
                    <div className="flex items-center gap-2">
                      <Bell className="h-4 w-4 text-muted-foreground" />
                      <label className="font-medium">Push Notifications</label>
                    </div>
                    <p className="text-sm text-muted-foreground">Receive browser push notifications</p>
                  </div>
                  <Switch defaultChecked />
                </div>
                <div className="flex items-center justify-between">
                  <div className="space-y-0.5">
                    <div className="flex items-center gap-2">
                      <Phone className="h-4 w-4 text-muted-foreground" />
                      <label className="font-medium">SMS Notifications</label>
                    </div>
                    <p className="text-sm text-muted-foreground">Receive critical alerts via SMS</p>
                  </div>
                  <Switch />
                </div>
                <div className="flex items-center justify-between">
                  <div className="space-y-0.5">
                    <div className="flex items-center gap-2">
                      <Slack className="h-4 w-4 text-muted-foreground" />
                      <label className="font-medium">Slack Notifications</label>
                    </div>
                    <p className="text-sm text-muted-foreground">Receive notifications in Slack</p>
                  </div>
                  <Switch />
                </div>
              </div>

              <div className="border-t pt-6">
                <h4 className="font-medium mb-4">Notification Types</h4>
                <div className="space-y-3">
                  {[
                    { label: 'New ticket assignments', email: true, push: true, sms: false },
                    { label: 'SLA breach warnings', email: true, push: true, sms: true },
                    { label: 'Ticket replies', email: true, push: true, sms: false },
                    { label: 'Mentions in comments', email: false, push: true, sms: false },
                    { label: 'Team announcements', email: true, push: true, sms: false },
                  ].map((item) => (
                    <div key={item.label} className="flex items-center justify-between py-2">
                      <span className="text-sm">{item.label}</span>
                      <div className="flex gap-4">
                        <span className={item.email ? 'text-green-600' : 'text-gray-400'} text-xs">Email</span>
                        <span className={item.push ? 'text-green-600' : 'text-gray-400'} text-xs">Push</span>
                        <span className={item.sms ? 'text-green-600' : 'text-gray-400'} text-xs">SMS</span>
                      </div>
                    </div>
                  ))}
                </div>
              </div>
            </CardContent>
          </Card>
        </TabsContent>

        {/* Teams Tab */}
        <TabsContent value="teams" className="space-y-4">
          <Card>
            <CardHeader>
              <div className="flex items-center justify-between">
                <div>
                  <CardTitle>Support Teams</CardTitle>
                  <CardDescription>Manage your support teams and assignments</CardDescription>
                </div>
                <Button>
                  <Users className="h-4 w-4 mr-2" />
                  New Team
                </Button>
              </div>
            </CardHeader>
            <CardContent>
              <Table>
                <TableHeader>
                  <TableRow>
                    <TableHead>Team Name</TableHead>
                    <TableHead>Lead</TableHead>
                    <TableHead>Members</TableHead>
                    <TableHead>Load</TableHead>
                    <TableHead>Status</TableHead>
                    <TableHead></TableHead>
                  </TableRow>
                </TableHeader>
                <TableBody>
                  <TableRow>
                    <TableCell className="font-medium">US Technical Support</TableCell>
                    <TableCell>James Wilson</TableCell>
                    <TableCell>12 members</TableCell>
                    <TableCell>
                      <div className="flex items-center gap-2">
                        <div className="h-2 w-20 overflow-hidden rounded-full bg-slate-200">
                          <div className="h-full bg-blue-500" style={{ width: '56%' }} />
                        </div>
                        <span className="text-sm">28/50</span>
                      </div>
                    </TableCell>
                    <TableCell>
                      <Badge variant="success">Active</Badge>
                    </TableCell>
                    <TableCell>
                      <Button variant="ghost" size="sm">Manage</Button>
                    </TableCell>
                  </TableRow>
                  <TableRow>
                    <TableCell className="font-medium">Nigeria Support</TableCell>
                    <TableCell>Chinedu Okonkwo</TableCell>
                    <TableCell>8 members</TableCell>
                    <TableCell>
                      <div className="flex items-center gap-2">
                        <div className="h-2 w-20 overflow-hidden rounded-full bg-slate-200">
                          <div className="h-full bg-green-500" style={{ width: '36%' }} />
                        </div>
                        <span className="text-sm">22/60</span>
                      </div>
                    </TableCell>
                    <TableCell>
                      <Badge variant="success">Active</Badge>
                    </TableCell>
                    <TableCell>
                      <Button variant="ghost" size="sm">Manage</Button>
                    </TableCell>
                  </TableRow>
                  <TableRow>
                    <TableCell className="font-medium">Kenya Support</TableCell>
                    <TableCell>Grace Mwangi</TableCell>
                    <TableCell>5 members</TableCell>
                    <TableCell>
                      <div className="flex items-center gap-2">
                        <div className="h-2 w-20 overflow-hidden rounded-full bg-slate-200">
                          <div className="h-full bg-blue-500" style={{ width: '40%' }} />
                        </div>
                        <span className="text-sm">18/45</span>
                      </div>
                    </TableCell>
                    <TableCell>
                      <Badge variant="success">Active</Badge>
                    </TableCell>
                    <TableCell>
                      <Button variant="ghost" size="sm">Manage</Button>
                    </TableCell>
                  </TableRow>
                </TableBody>
              </Table>
            </CardContent>
          </Card>
        </TabsContent>

        {/* Channels Tab */}
        <TabsContent value="channels" className="space-y-4">
          <Card>
            <CardHeader>
              <CardTitle>Channel Configuration</CardTitle>
              <CardDescription>Configure support channel settings</CardDescription>
            </CardHeader>
            <CardContent className="space-y-6">
              <div className="space-y-4">
                <div className="flex items-center justify-between p-4 border rounded-lg">
                  <div className="flex items-center gap-3">
                    <Mail className="h-8 w-8 text-muted-foreground" />
                    <div>
                      <h4 className="font-medium">Email Support</h4>
                      <p className="text-sm text-muted-foreground">support@gogidix.com</p>
                    </div>
                  </div>
                  <Switch defaultChecked />
                </div>
                <div className="flex items-center justify-between p-4 border rounded-lg">
                  <div className="flex items-center gap-3">
                    <MessageSquare className="h-8 w-8 text-muted-foreground" />
                    <div>
                      <h4 className="font-medium">Live Chat</h4>
                      <p className="text-sm text-muted-foreground">Available 24/7</p>
                    </div>
                  </div>
                  <Switch defaultChecked />
                </div>
                <div className="flex items-center justify-between p-4 border rounded-lg">
                  <div className="flex items-center gap-3">
                    <Phone className="h-8 w-8 text-muted-foreground" />
                    <div>
                      <h4 className="font-medium">Phone Support</h4>
                      <p className="text-sm text-muted-foreground">+1-800-GOGIDIX</p>
                    </div>
                  </div>
                  <Switch defaultChecked />
                </div>
                <div className="flex items-center justify-between p-4 border rounded-lg">
                  <div className="flex items-center gap-3">
                    <Globe className="h-8 w-8 text-muted-foreground" />
                    <div>
                      <h4 className="font-medium">Web Widget</h4>
                      <p className="text-sm text-muted-foreground">Embedded chat widget</p>
                    </div>
                  </div>
                  <Switch defaultChecked />
                </div>
              </div>
            </CardContent>
          </Card>
        </TabsContent>

        {/* SLA Rules Tab */}
        <TabsContent value="sla" className="space-y-4">
          <Card>
            <CardHeader>
              <div className="flex items-center justify-between">
                <div>
                  <CardTitle>SLA Policies</CardTitle>
                  <CardDescription>Configure service level agreement targets</CardDescription>
                </div>
                <Button>
                  <Save className="h-4 w-4 mr-2" />
                  Add Policy
                </Button>
              </div>
            </CardHeader>
            <CardContent>
              <Table>
                <TableHeader>
                  <TableRow>
                    <TableHead>Priority</TableHead>
                    <TableHead>First Response</TableHead>
                    <TableHead>Resolution</TableHead>
                    <TableHead>Update Interval</TableHead>
                    <TableHead></TableHead>
                  </TableRow>
                </TableHeader>
                <TableBody>
                  <TableRow>
                    <TableCell>
                      <Badge variant="destructive">Urgent</Badge>
                    </TableCell>
                    <TableCell>15 minutes</TableCell>
                    <TableCell>4 hours</TableCell>
                    <TableCell>30 minutes</TableCell>
                    <TableCell>
                      <Button variant="ghost" size="sm">Edit</Button>
                    </TableCell>
                  </TableRow>
                  <TableRow>
                    <TableCell>
                      <Badge variant="warning">High</Badge>
                    </TableCell>
                    <TableCell>30 minutes</TableCell>
                    <TableCell>8 hours</TableCell>
                    <TableCell>1 hour</TableCell>
                    <TableCell>
                      <Button variant="ghost" size="sm">Edit</Button>
                    </TableCell>
                  </TableRow>
                  <TableRow>
                    <TableCell>
                      <Badge variant="secondary">Medium</Badge>
                    </TableCell>
                    <TableCell>1 hour</TableCell>
                    <TableCell>24 hours</TableCell>
                    <TableCell>4 hours</TableCell>
                    <TableCell>
                      <Button variant="ghost" size="sm">Edit</Button>
                    </TableCell>
                  </TableRow>
                  <TableRow>
                    <TableCell>
                      <Badge variant="outline">Low</Badge>
                    </TableCell>
                    <TableCell>4 hours</TableCell>
                    <TableCell>48 hours</TableCell>
                    <TableCell>12 hours</TableCell>
                    <TableCell>
                      <Button variant="ghost" size="sm">Edit</Button>
                    </TableCell>
                  </TableRow>
                </TableBody>
              </Table>
            </CardContent>
          </Card>
        </TabsContent>
      </Tabs>
    </div>
  )
}

// Missing imports
import { MessageSquare } from 'lucide-react'
import { Switch } from '@shared/components/ui/switch'

// Create switch component
import * as React from 'react'
import * as SwitchPrimitives from '@radix-ui/react-switch'

const Switch = React.forwardRef<
  React.ElementRef<typeof SwitchPrimitives.Root>,
  React.ComponentPropsWithoutRef<typeof SwitchPrimitives.Root>
>(({ className, ...props }, ref) => (
  <SwitchPrimitives.Root
    className={cn(
      'peer inline-flex h-5 w-9 shrink-0 cursor-pointer items-center rounded-full border-2 border-transparent shadow-sm transition-colors focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 focus-visible:ring-offset-background disabled:cursor-not-allowed disabled:opacity-50 data-[state=checked]:bg-primary data-[state=unchecked]:bg-input',
      className
    )}
    {...props}
    ref={ref}
  >
    <SwitchPrimitives.Thumb
      className={cn(
        'pointer-events-none block h-4 w-4 rounded-full bg-background shadow-lg ring-0 transition-transform data-[state=checked]:translate-x-4 data-[state=unchecked]:translate-x-0'
      )}
    />
  </SwitchPrimitives.Root>
))
Switch.displayName = SwitchPrimitives.Root.displayName

export { Switch }
