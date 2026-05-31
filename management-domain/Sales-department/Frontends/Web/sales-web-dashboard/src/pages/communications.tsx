import { useState } from 'react'
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@shared/components/ui/card'
import { Badge } from '@shared/components/ui/badge'
import { Button } from '@shared/components/ui/button'
import { Input } from '@shared/components/ui/input'
import { Label } from '@shared/components/ui/label'
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
import {
  Mail,
  Phone,
  Calendar,
  MessageSquare,
  Video,
  FileText,
  Plus,
  Search,
  Clock,
  CheckCircle,
  XCircle,
} from 'lucide-react'
import { formatDate, formatDateTime } from '@shared/utils/cn'
import { mockActivities, mockLeads, mockOpportunities, mockCustomers } from '@shared/data/mockData'

const activityIcons: Record<string, React.ComponentType<{ className?: string }>> = {
  call: Phone,
  email: Mail,
  meeting: Users,
  presentation: FileText,
  demo: Video,
  site_visit: MapPin,
  follow_up: MessageSquare,
  task: CheckCircle,
  note: FileText,
}

import { Users, MapPin } from 'lucide-react'

export default function CommunicationsPage() {
  const [activeTab, setActiveTab] = useState('timeline')
  const [showNewActivity, setShowNewActivity] = useState(false)
  const [newActivityType, setNewActivityType] = useState('call')

  const upcomingActivities = mockActivities.filter(a => a.status === 'scheduled')
  const completedActivities = mockActivities.filter(a => a.status === 'completed')

  const getTypeIcon = (type: string) => {
    const Icon = activityIcons[type] || MessageSquare
    return <Icon className="h-4 w-4" />
  }

  const getTypeColor = (type: string) => {
    const colors: Record<string, string> = {
      call: 'bg-blue-100 text-blue-800',
      email: 'bg-purple-100 text-purple-800',
      meeting: 'bg-green-100 text-green-800',
      presentation: 'bg-orange-100 text-orange-800',
      demo: 'bg-pink-100 text-pink-800',
      site_visit: 'bg-teal-100 text-teal-800',
      follow_up: 'bg-yellow-100 text-yellow-800',
      task: 'bg-gray-100 text-gray-800',
      note: 'bg-indigo-100 text-indigo-800',
    }
    return colors[type] || 'bg-gray-100 text-gray-800'
  }

  const getStatusColor = (status: string) => {
    const colors: Record<string, string> = {
      scheduled: 'bg-blue-100 text-blue-800',
      in_progress: 'bg-yellow-100 text-yellow-800',
      completed: 'bg-green-100 text-green-800',
      cancelled: 'bg-red-100 text-red-800',
    }
    return colors[status] || 'bg-gray-100 text-gray-800'
  }

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
        <div>
          <h1 className="text-3xl font-bold tracking-tight">Communications</h1>
          <p className="text-muted-foreground mt-1">
            Track all customer interactions and activities
          </p>
        </div>
        <Dialog open={showNewActivity} onOpenChange={setShowNewActivity}>
          <DialogTrigger asChild>
            <Button className="gap-2">
              <Plus className="h-4 w-4" />
              Log Activity
            </Button>
          </DialogTrigger>
          <DialogContent>
            <DialogHeader>
              <DialogTitle>Log New Activity</DialogTitle>
              <DialogDescription>
                Record a customer interaction or schedule a follow-up
              </DialogDescription>
            </DialogHeader>
            <div className="space-y-4 py-4">
              <div className="space-y-2">
                <Label htmlFor="type">Activity Type</Label>
                <Select value={newActivityType} onValueChange={setNewActivityType}>
                  <SelectTrigger id="type">
                    <SelectValue />
                  </SelectTrigger>
                  <SelectContent>
                    <SelectItem value="call">Phone Call</SelectItem>
                    <SelectItem value="email">Email</SelectItem>
                    <SelectItem value="meeting">Meeting</SelectItem>
                    <SelectItem value="demo">Product Demo</SelectItem>
                    <SelectItem value="presentation">Presentation</SelectItem>
                    <SelectItem value="site_visit">Site Visit</SelectItem>
                  </SelectContent>
                </Select>
              </div>
              <div className="space-y-2">
                <Label htmlFor="regarding">Regarding</Label>
                <Select>
                  <SelectTrigger id="regarding">
                    <SelectValue placeholder="Select lead, opportunity, or customer" />
                  </SelectTrigger>
                  <SelectContent>
                    {mockLeads.slice(0, 3).map(lead => (
                      <SelectItem key={lead.id} value={lead.id}>
                        {lead.company} (Lead)
                      </SelectItem>
                    ))}
                    {mockOpportunities.slice(0, 3).map(opp => (
                      <SelectItem key={opp.id} value={opp.id}>
                        {opp.accountName} (Opportunity)
                      </SelectItem>
                    ))}
                  </SelectContent>
                </Select>
              </div>
              <div className="space-y-2">
                <Label htmlFor="subject">Subject</Label>
                <Input id="subject" placeholder="Activity subject or title" />
              </div>
              <div className="space-y-2">
                <Label htmlFor="date">Date & Time</Label>
                <Input id="date" type="datetime-local" />
              </div>
              <div className="space-y-2">
                <Label htmlFor="notes">Notes</Label>
                <Input id="notes" placeholder="Activity details and outcome" />
              </div>
            </div>
            <DialogFooter>
              <Button variant="outline" onClick={() => setShowNewActivity(false)}>
                Cancel
              </Button>
              <Button onClick={() => setShowNewActivity(false)}>
                Save Activity
              </Button>
            </DialogFooter>
          </DialogContent>
        </Dialog>
      </div>

      {/* Stats */}
      <div className="grid gap-4 md:grid-cols-4">
        <Card>
          <CardHeader className="pb-2">
            <CardTitle className="text-sm font-medium text-muted-foreground">
              Upcoming
            </CardTitle>
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">{upcomingActivities.length}</div>
            <p className="text-xs text-muted-foreground mt-1">
              Scheduled activities
            </p>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="pb-2">
            <CardTitle className="text-sm font-medium text-muted-foreground">
              Today
            </CardTitle>
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">3</div>
            <p className="text-xs text-muted-foreground mt-1">
              Activities scheduled
            </p>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="pb-2">
            <CardTitle className="text-sm font-medium text-muted-foreground">
              This Week
            </CardTitle>
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">12</div>
            <p className="text-xs text-muted-foreground mt-1">
              Total activities
            </p>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="pb-2">
            <CardTitle className="text-sm font-medium text-muted-foreground">
              Completed
            </CardTitle>
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">{completedActivities.length}</div>
            <p className="text-xs text-muted-foreground mt-1">
              This month
            </p>
          </CardContent>
        </Card>
      </div>

      {/* Activity Tabs */}
      <Tabs value={activeTab} onValueChange={setActiveTab} className="space-y-4">
        <TabsList>
          <TabsTrigger value="timeline">Timeline</TabsTrigger>
          <TabsTrigger value="upcoming">Upcoming</TabsTrigger>
          <TabsTrigger value="completed">Completed</TabsTrigger>
        </TabsList>

        <TabsContent value="timeline" className="space-y-4">
          <Card>
            <CardHeader>
              <CardTitle>Activity Timeline</CardTitle>
              <CardDescription>All your customer interactions</CardDescription>
            </CardHeader>
            <CardContent>
              <div className="space-y-6">
                {mockActivities.map((activity, idx) => (
                  <div key={activity.id} className="flex gap-4">
                    <div className="flex flex-col items-center">
                      <div className={`h-10 w-10 rounded-full flex items-center justify-center ${getTypeColor(activity.type)}`}>
                        {getTypeIcon(activity.type)}
                      </div>
                      {idx < mockActivities.length - 1 && (
                        <div className="w-0.5 h-full bg-gray-200 my-2" />
                      )}
                    </div>
                    <div className="flex-1 pb-6">
                      <div className="flex items-start justify-between">
                        <div>
                          <div className="font-medium">{activity.subject}</div>
                          <div className="text-sm text-muted-foreground">
                            {activity.regarding && (
                              <span className="font-medium">{activity.regarding}</span>
                            )}
                          </div>
                        </div>
                        <Badge className={getStatusColor(activity.status)}>
                          {activity.status}
                        </Badge>
                      </div>
                      <div className="mt-2 text-sm text-muted-foreground">
                        {formatDateTime(activity.startDate)}
                        {activity.duration && ` • ${activity.duration} min`}
                      </div>
                      {activity.outcome && (
                        <div className="mt-2 text-sm bg-gray-50 p-2 rounded">
                          {activity.outcome}
                        </div>
                      )}
                    </div>
                  </div>
                ))}
              </div>
            </CardContent>
          </Card>
        </TabsContent>

        <TabsContent value="upcoming" className="space-y-4">
          <Card>
            <CardHeader>
              <CardTitle>Upcoming Activities</CardTitle>
              <CardDescription>Scheduled calls, meetings, and follow-ups</CardDescription>
            </CardHeader>
            <CardContent>
              <div className="space-y-3">
                {upcomingActivities.map((activity) => (
                  <div
                    key={activity.id}
                    className="flex items-center gap-4 p-4 border rounded-lg hover:bg-gray-50"
                  >
                    <div className={`h-10 w-10 rounded-full flex items-center justify-center ${getTypeColor(activity.type)}`}>
                      {getTypeIcon(activity.type)}
                    </div>
                    <div className="flex-1">
                      <div className="font-medium">{activity.subject}</div>
                      <div className="text-sm text-muted-foreground">
                        {activity.regarding} • {formatDateTime(activity.startDate)}
                      </div>
                      {activity.location && (
                        <div className="text-xs text-muted-foreground mt-1">
                          {activity.location}
                        </div>
                      )}
                    </div>
                    <div className="flex gap-2">
                      <Button size="sm" variant="outline">
                        Reschedule
                      </Button>
                      <Button size="sm">Complete</Button>
                    </div>
                  </div>
                ))}
              </div>
            </CardContent>
          </Card>
        </TabsContent>

        <TabsContent value="completed" className="space-y-4">
          <Card>
            <CardHeader>
              <CardTitle>Completed Activities</CardTitle>
              <CardDescription>Past interactions and their outcomes</CardDescription>
            </CardHeader>
            <CardContent>
              <div className="space-y-3">
                {completedActivities.map((activity) => (
                  <div
                    key={activity.id}
                    className="flex items-center gap-4 p-4 border rounded-lg"
                  >
                    <div className="h-10 w-10 rounded-full bg-green-100 flex items-center justify-center">
                      <CheckCircle className="h-5 w-5 text-green-600" />
                    </div>
                    <div className="flex-1">
                      <div className="font-medium">{activity.subject}</div>
                      <div className="text-sm text-muted-foreground">
                        {activity.regarding} • {formatDate(activity.startDate)}
                      </div>
                      {activity.outcome && (
                        <div className="text-sm mt-1 p-2 bg-green-50 rounded">
                          {activity.outcome}
                        </div>
                      )}
                    </div>
                    <Badge className={getStatusColor(activity.status)}>
                      {activity.status}
                    </Badge>
                  </div>
                ))}
              </div>
            </CardContent>
          </Card>
        </TabsContent>
      </Tabs>

      {/* Quick Actions */}
      <Card>
        <CardHeader>
          <CardTitle>Quick Actions</CardTitle>
          <CardDescription>Common communication tasks</CardDescription>
        </CardHeader>
        <CardContent>
          <div className="grid gap-4 md:grid-cols-4">
            <Button variant="outline" className="h-auto py-4 flex flex-col gap-2">
              <Mail className="h-5 w-5" />
              <span>Compose Email</span>
            </Button>
            <Button variant="outline" className="h-auto py-4 flex flex-col gap-2">
              <Phone className="h-5 w-5" />
              <span>Log Call</span>
            </Button>
            <Button variant="outline" className="h-auto py-4 flex flex-col gap-2">
              <Calendar className="h-5 w-5" />
              <span>Schedule Meeting</span>
            </Button>
            <Button variant="outline" className="h-auto py-4 flex flex-col gap-2">
              <MessageSquare className="h-5 w-5" />
              <span>Send Follow-up</span>
            </Button>
          </div>
        </CardContent>
      </Card>
    </div>
  )
}
