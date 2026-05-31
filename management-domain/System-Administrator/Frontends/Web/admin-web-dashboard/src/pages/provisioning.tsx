import { useState } from 'react'
import { UserPlus, Users, Search, Plus, CheckCircle2, Clock } from 'lucide-react'
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@shared/components/ui/card'
import { Badge } from '@shared/components/ui/badge'
import { Button } from '@shared/components/ui/button'
import { Input } from '@shared/components/ui/input'
import { Label } from '@shared/components/ui/label'
import { Progress } from '@shared/components/ui/progress'
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from '@shared/components/ui/table'
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
import { Textarea } from '@shared/components/ui/textarea'
import { cn, formatDateTime } from '@shared/utils/cn'
import { mockProvisioningRequests, mockUsers } from '@shared/data/mockData'

export default function ProvisioningPage() {
  const [searchQuery, setSearchQuery] = useState('')
  const [selectedTab, setSelectedTab] = useState('onboarding')
  const [createDialogOpen, setCreateDialogOpen] = useState(false)

  const onboardingRequests = mockProvisioningRequests.filter(r => r.type === 'onboarding')
  const offboardingRequests = mockProvisioningRequests.filter(r => r.type === 'offboarding')

  const taskStatusConfig = {
    pending: { label: 'Pending', color: 'bg-slate-500', variant: 'secondary' as const },
    in_progress: { label: 'In Progress', color: 'bg-blue-500', variant: 'info' as const },
    completed: { label: 'Completed', color: 'bg-green-500', variant: 'success' as const },
    failed: { label: 'Failed', color: 'bg-red-500', variant: 'destructive' as const },
  }

  const requestStatusConfig = {
    pending: { label: 'Pending', variant: 'secondary' as const },
    in_progress: { label: 'In Progress', variant: 'info' as const },
    completed: { label: 'Completed', variant: 'success' as const },
    failed: { label: 'Failed', variant: 'destructive' as const },
  }

  return (
    <div className="space-y-6">
      {/* Page Header */}
      <div className="flex items-center justify-between">
        <div className="page-header">
          <h1 className="page-title">User Provisioning</h1>
          <p className="page-description">
            Manage user onboarding, offboarding, and account transfers
          </p>
        </div>
        <Dialog open={createDialogOpen} onOpenChange={setCreateDialogOpen}>
          <DialogTrigger asChild>
            <Button variant="admin">
              <Plus className="mr-2 h-4 w-4" />
              New Request
            </Button>
          </DialogTrigger>
          <DialogContent className="max-w-2xl">
            <DialogHeader>
              <DialogTitle>Create Provisioning Request</DialogTitle>
              <DialogDescription>
                Initiate user onboarding or offboarding process
              </DialogDescription>
            </DialogHeader>
            <div className="grid gap-4 py-4">
              <div className="grid gap-2">
                <Label htmlFor="type">Request Type *</Label>
                <Select>
                  <SelectTrigger id="type">
                    <SelectValue placeholder="Select type" />
                  </SelectTrigger>
                  <SelectContent>
                    <SelectItem value="onboarding">Onboarding</SelectItem>
                    <SelectItem value="offboarding">Offboarding</SelectItem>
                    <SelectItem value="transfer">Transfer</SelectItem>
                    <SelectItem value="modification">Modification</SelectItem>
                  </SelectContent>
                </Select>
              </div>
              <div className="grid grid-cols-2 gap-4">
                <div className="grid gap-2">
                  <Label htmlFor="firstName">First Name *</Label>
                  <Input id="firstName" placeholder="John" />
                </div>
                <div className="grid gap-2">
                  <Label htmlFor="lastName">Last Name *</Label>
                  <Input id="lastName" placeholder="Smith" />
                </div>
              </div>
              <div className="grid grid-cols-2 gap-4">
                <div className="grid gap-2">
                  <Label htmlFor="email">Email *</Label>
                  <Input id="email" type="email" placeholder="john.smith@gogidix.com" />
                </div>
                <div className="grid gap-2">
                  <Label htmlFor="department">Department *</Label>
                  <Select>
                    <SelectTrigger id="department">
                      <SelectValue placeholder="Select" />
                    </SelectTrigger>
                    <SelectContent>
                      <SelectItem value="system-administrator">System Administrator</SelectItem>
                      <SelectItem value="finance">Finance</SelectItem>
                      <SelectItem value="human-resource">Human Resources</SelectItem>
                      <SelectItem value="sales">Sales</SelectItem>
                    </SelectContent>
                  </Select>
                </div>
              </div>
              <div className="grid gap-2">
                <Label htmlFor="role">Role *</Label>
                <Select>
                  <SelectTrigger id="role">
                    <SelectValue placeholder="Select role" />
                  </SelectTrigger>
                  <SelectContent>
                    <SelectItem value="DOMAIN_ADMINISTRATOR">Domain Administrator</SelectItem>
                    <SelectItem value="IT_SUPPORT">IT Support</SelectItem>
                    <SelectItem value="DEVOPS_ENGINEER">DevOps Engineer</SelectItem>
                    <SelectItem value="SECURITY_ANALYST">Security Analyst</SelectItem>
                  </SelectContent>
                </Select>
              </div>
              <div className="grid gap-2">
                <Label htmlFor="notes">Notes</Label>
                <Textarea
                  id="notes"
                  placeholder="Additional information for the provisioning team"
                  rows={3}
                />
              </div>
              <div className="grid gap-2">
                <Label htmlFor="startDate">Start Date (For onboarding)</Label>
                <Input id="startDate" type="date" />
              </div>
            </div>
            <DialogFooter>
              <Button variant="outline" onClick={() => setCreateDialogOpen(false)}>Cancel</Button>
              <Button variant="admin">Create Request</Button>
            </DialogFooter>
          </DialogContent>
        </Dialog>
      </div>

      {/* Stats */}
      <div className="grid gap-4 md:grid-cols-4">
        <Card>
          <CardHeader className="pb-3">
            <CardDescription>Pending</CardDescription>
            <CardTitle className="text-2xl">{mockProvisioningRequests.filter(r => r.status === 'pending').length}</CardTitle>
          </CardHeader>
        </Card>
        <Card>
          <CardHeader className="pb-3">
            <CardDescription>In Progress</CardDescription>
            <CardTitle className="text-2xl">{mockProvisioningRequests.filter(r => r.status === 'in_progress').length}</CardTitle>
          </CardHeader>
        </Card>
        <Card>
          <CardHeader className="pb-3">
            <CardDescription>Completed (This Week)</CardDescription>
            <CardTitle className="text-2xl text-green-600">12</CardTitle>
          </CardHeader>
        </Card>
        <Card>
          <CardHeader className="pb-3">
            <CardDescription>Avg. Completion</CardDescription>
            <CardTitle className="text-2xl">1.2 days</CardTitle>
          </CardHeader>
        </Card>
      </div>

      {/* Search */}
      <div className="relative">
        <Search className="absolute left-3 top-1/2 h-4 w-4 -translate-y-1/2 text-muted-foreground" />
        <Input
          placeholder="Search by employee name, ID, or department..."
          className="pl-9"
          value={searchQuery}
          onChange={(e) => setSearchQuery(e.target.value)}
        />
      </div>

      {/* Tabs */}
      <Tabs value={selectedTab} onValueChange={setSelectedTab}>
        <TabsList>
          <TabsTrigger value="onboarding">
            Onboarding ({onboardingRequests.length})
          </TabsTrigger>
          <TabsTrigger value="offboarding">
            Offboarding ({offboardingRequests.length})
          </TabsTrigger>
          <TabsTrigger value="all">
            All Requests ({mockProvisioningRequests.length})
          </TabsTrigger>
        </TabsList>

        {/* Onboarding Tab */}
        <TabsContent value="onboarding">
          <div className="space-y-4">
            {onboardingRequests.map((request) => (
              <Card key={request.id}>
                <CardHeader>
                  <div className="flex items-start justify-between">
                    <div>
                      <CardTitle className="flex items-center gap-2">
                        <UserPlus className="h-5 w-5" />
                        {request.employeeName}
                      </CardTitle>
                      <CardDescription>
                        {request.employeeId} · {request.department.replace('-', ' ')} · {request.role.replace(/_/g, ' ')}
                      </CardDescription>
                    </div>
                    <Badge variant={requestStatusConfig[request.status].variant}>
                      {requestStatusConfig[request.status].label}
                    </Badge>
                  </div>
                </CardHeader>
                <CardContent>
                  <div className="space-y-4">
                    <div>
                      <div className="flex items-center justify-between text-sm mb-2">
                        <span>Progress</span>
                        <span className="font-medium">
                          {request.tasks.filter(t => t.status === 'completed').length} / {request.tasks.length}
                        </span>
                      </div>
                      <Progress
                        value={(request.tasks.filter(t => t.status === 'completed').length / request.tasks.length) * 100}
                      />
                    </div>
                    <div className="grid grid-cols-1 md:grid-cols-2 gap-3">
                      {request.tasks.map((task) => (
                        <div
                          key={task.id}
                          className={cn(
                            'flex items-center gap-3 rounded-lg border p-3',
                            task.status === 'completed' && 'bg-green-50 dark:bg-green-900/10',
                            task.status === 'in_progress' && 'bg-blue-50 dark:bg-blue-900/10',
                            task.status === 'failed' && 'bg-red-50 dark:bg-red-900/10'
                          )}
                        >
                          <div className={cn(
                            'h-2 w-2 rounded-full',
                            task.status === 'completed' && 'bg-green-500',
                            task.status === 'in_progress' && 'bg-blue-500 animate-pulse',
                            task.status === 'failed' && 'bg-red-500',
                            task.status === 'pending' && 'bg-slate-300'
                          )} />
                          <div className="flex-1">
                            <p className="text-sm font-medium">{task.name}</p>
                            <p className="text-xs text-muted-foreground">{task.description}</p>
                          </div>
                          {task.status === 'completed' && (
                            <CheckCircle2 className="h-4 w-4 text-green-500" />
                          )}
                          {task.status === 'in_progress' && (
                            <Clock className="h-4 w-4 text-blue-500 animate-spin" />
                          )}
                        </div>
                      ))}
                    </div>
                    <div className="flex items-center justify-between text-sm text-muted-foreground">
                      <span>Requested by {request.requestedBy}</span>
                      <span>{formatDateTime(request.requestedAt)}</span>
                    </div>
                    {request.status === 'in_progress' && (
                      <div className="flex gap-2">
                        <Button variant="outline" size="sm">View Details</Button>
                        <Button variant="admin" size="sm">Complete Tasks</Button>
                      </div>
                    )}
                  </div>
                </CardContent>
              </Card>
            ))}
          </div>
        </TabsContent>

        {/* Offboarding Tab */}
        <TabsContent value="offboarding">
          <div className="space-y-4">
            {offboardingRequests.map((request) => (
              <Card key={request.id} className="border-orange-200 dark:border-orange-900">
                <CardHeader>
                  <div className="flex items-start justify-between">
                    <div>
                      <CardTitle className="flex items-center gap-2 text-orange-700 dark:text-orange-400">
                        <Users className="h-5 w-5" />
                        {request.employeeName}
                      </CardTitle>
                      <CardDescription>
                        {request.employeeId} · {request.department.replace('-', ' ')} · {request.role.replace(/_/g, ' ')}
                      </CardDescription>
                    </div>
                    <Badge variant={requestStatusConfig[request.status].variant}>
                      {requestStatusConfig[request.status].label}
                    </Badge>
                  </div>
                </CardHeader>
                <CardContent>
                  <div className="space-y-4">
                    <div>
                      <div className="flex items-center justify-between text-sm mb-2">
                        <span>Progress</span>
                        <span className="font-medium">
                          {request.tasks.filter(t => t.status === 'completed').length} / {request.tasks.length}
                        </span>
                      </div>
                      <Progress
                        value={(request.tasks.filter(t => t.status === 'completed').length / request.tasks.length) * 100}
                      />
                    </div>
                    <div className="grid grid-cols-1 md:grid-cols-2 gap-3">
                      {request.tasks.map((task) => (
                        <div
                          key={task.id}
                          className={cn(
                            'flex items-center gap-3 rounded-lg border p-3',
                            task.status === 'completed' && 'bg-green-50 dark:bg-green-900/10',
                            task.status === 'in_progress' && 'bg-blue-50 dark:bg-blue-900/10',
                            task.status === 'failed' && 'bg-red-50 dark:bg-red-900/10'
                          )}
                        >
                          <div className={cn(
                            'h-2 w-2 rounded-full',
                            task.status === 'completed' && 'bg-green-500',
                            task.status === 'in_progress' && 'bg-blue-500 animate-pulse',
                            task.status === 'failed' && 'bg-red-500',
                            task.status === 'pending' && 'bg-slate-300'
                          )} />
                          <div className="flex-1">
                            <p className="text-sm font-medium">{task.name}</p>
                            <p className="text-xs text-muted-foreground">{task.description}</p>
                          </div>
                          {task.status === 'completed' && (
                            <CheckCircle2 className="h-4 w-4 text-green-500" />
                          )}
                        </div>
                      ))}
                    </div>
                    {request.status === 'pending' && (
                      <div className="flex gap-2">
                        <Button variant="outline" size="sm">View Details</Button>
                        <Button variant="admin" size="sm">Start Offboarding</Button>
                      </div>
                    )}
                  </div>
                </CardContent>
              </Card>
            ))}
          </div>
        </TabsContent>

        {/* All Requests Tab */}
        <TabsContent value="all">
          <Card>
            <CardHeader>
              <CardTitle>All Provisioning Requests</CardTitle>
              <CardDescription>Complete history of user provisioning</CardDescription>
            </CardHeader>
            <CardContent>
              <Table>
                <TableHeader>
                  <TableRow>
                    <TableHead>Employee</TableHead>
                    <TableHead>Type</TableHead>
                    <TableHead>Department</TableHead>
                    <TableHead>Role</TableHead>
                    <TableHead>Status</TableHead>
                    <TableHead>Requested</TableHead>
                    <TableHead className="text-right">Actions</TableHead>
                  </TableRow>
                </TableHeader>
                <TableBody>
                  {mockProvisioningRequests.map((request) => (
                    <TableRow key={request.id}>
                      <TableCell>
                        <div>
                          <p className="font-medium">{request.employeeName}</p>
                          <p className="text-xs text-muted-foreground">{request.employeeId}</p>
                        </div>
                      </TableCell>
                      <TableCell>
                        <Badge
                          variant={request.type === 'onboarding' ? 'success' : 'destructive'}
                          className="capitalize"
                        >
                          {request.type}
                        </Badge>
                      </TableCell>
                      <TableCell className="capitalize">
                        {request.department.replace('-', ' ').replace(' ', '/')}
                      </TableCell>
                      <TableCell className="text-sm">{request.role.replace(/_/g, ' ')}</TableCell>
                      <TableCell>
                        <Badge variant={requestStatusConfig[request.status].variant}>
                          {requestStatusConfig[request.status].label}
                        </Badge>
                      </TableCell>
                      <TableCell>{formatDateTime(request.requestedAt)}</TableCell>
                      <TableCell className="text-right">
                        <Button variant="ghost" size="sm">View</Button>
                      </TableCell>
                    </TableRow>
                  ))}
                </TableBody>
              </Table>
            </CardContent>
          </Card>
        </TabsContent>
      </Tabs>
    </div>
  )
}
