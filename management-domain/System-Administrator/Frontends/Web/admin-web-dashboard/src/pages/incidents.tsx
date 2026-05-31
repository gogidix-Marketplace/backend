import { useState } from 'react'
import { Plus, AlertTriangle, Search, Filter, Clock, CheckCircle, XCircle } from 'lucide-react'
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@shared/components/ui/card'
import { Badge } from '@shared/components/ui/badge'
import { Button } from '@shared/components/ui/button'
import { Input } from '@shared/components/ui/input'
import { Label } from '@shared/components/ui/label'
import { Textarea } from '@shared/components/ui/textarea'
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
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from '@shared/components/ui/dialog'
import { cn, formatDateTime } from '@shared/utils/cn'
import { mockIncidents } from '@shared/data/mockData'

export default function IncidentsPage() {
  const [searchQuery, setSearchQuery] = useState('')
  const [filterStatus, setFilterStatus] = useState<string>('all')
  const [filterSeverity, setFilterSeverity] = useState<string>('all')
  const [createDialogOpen, setCreateDialogOpen] = useState(false)

  const filteredIncidents = mockIncidents.filter(incident => {
    const matchesSearch =
      incident.title.toLowerCase().includes(searchQuery.toLowerCase()) ||
      incident.description.toLowerCase().includes(searchQuery.toLowerCase())
    const matchesStatus = filterStatus === 'all' || incident.status === filterStatus
    const matchesSeverity = filterSeverity === 'all' || incident.severity === filterSeverity
    return matchesSearch && matchesStatus && matchesSeverity
  })

  const statusConfig = {
    open: { label: 'Open', color: 'bg-blue-500', variant: 'info' as const },
    in_progress: { label: 'In Progress', color: 'bg-yellow-500', variant: 'warning' as const },
    resolved: { label: 'Resolved', color: 'bg-green-500', variant: 'success' as const },
    closed: { label: 'Closed', color: 'bg-slate-500', variant: 'secondary' as const },
  }

  const severityConfig = {
    low: { label: 'Low', color: 'bg-slate-500', variant: 'secondary' as const },
    medium: { label: 'Medium', color: 'bg-yellow-500', variant: 'warning' as const },
    high: { label: 'High', color: 'bg-orange-500', variant: 'warning' as const },
    critical: { label: 'Critical', color: 'bg-red-500', variant: 'destructive' as const },
  }

  return (
    <div className="space-y-6">
      {/* Page Header */}
      <div className="flex items-center justify-between">
        <div className="page-header">
          <h1 className="page-title">Incident Management</h1>
          <p className="page-description">
            Track and resolve system incidents and issues
          </p>
        </div>
        <Dialog open={createDialogOpen} onOpenChange={setCreateDialogOpen}>
          <DialogTrigger asChild>
            <Button variant="admin">
              <Plus className="mr-2 h-4 w-4" />
              Create Incident
            </Button>
          </DialogTrigger>
          <DialogContent className="max-w-2xl">
            <DialogHeader>
              <DialogTitle>Create New Incident</DialogTitle>
              <DialogDescription>
                Report a new system incident or issue
              </DialogDescription>
            </DialogHeader>
            <div className="grid gap-4 py-4">
              <div className="grid gap-2">
                <Label htmlFor="title">Title *</Label>
                <Input id="title" placeholder="Brief description of the incident" />
              </div>
              <div className="grid grid-cols-3 gap-4">
                <div className="grid gap-2">
                  <Label htmlFor="severity">Severity *</Label>
                  <Select>
                    <SelectTrigger id="severity">
                      <SelectValue placeholder="Select" />
                    </SelectTrigger>
                    <SelectContent>
                      <SelectItem value="low">Low</SelectItem>
                      <SelectItem value="medium">Medium</SelectItem>
                      <SelectItem value="high">High</SelectItem>
                      <SelectItem value="critical">Critical</SelectItem>
                    </SelectContent>
                  </Select>
                </div>
                <div className="grid gap-2">
                  <Label htmlFor="category">Category *</Label>
                  <Select>
                    <SelectTrigger id="category">
                      <SelectValue placeholder="Select" />
                    </SelectTrigger>
                    <SelectContent>
                      <SelectItem value="security">Security</SelectItem>
                      <SelectItem value="performance">Performance</SelectItem>
                      <SelectItem value="availability">Availability</SelectItem>
                      <SelectItem value="data">Data</SelectItem>
                      <SelectItem value="other">Other</SelectItem>
                    </SelectContent>
                  </Select>
                </div>
                <div className="grid gap-2">
                  <Label htmlFor="assignedTo">Assign To</Label>
                  <Select>
                    <SelectTrigger id="assignedTo">
                      <SelectValue placeholder="Select" />
                    </SelectTrigger>
                    <SelectContent>
                      <SelectItem value="unassigned">Unassigned</SelectItem>
                      <SelectItem value="robert">Robert Chen</SelectItem>
                      <SelectItem value="lisa">Lisa Park</SelectItem>
                      <SelectItem value="mike">Mike Chen</SelectItem>
                    </SelectContent>
                  </Select>
                </div>
              </div>
              <div className="grid gap-2">
                <Label htmlFor="description">Description *</Label>
                <Textarea
                  id="description"
                  placeholder="Detailed description of the incident"
                  rows={4}
                />
              </div>
              <div className="grid gap-2">
                <Label htmlFor="affectedServices">Affected Services</Label>
                <Input id="affectedServices" placeholder="e.g., API Gateway, Database" />
              </div>
            </div>
            <DialogFooter>
              <Button variant="outline" onClick={() => setCreateDialogOpen(false)}>Cancel</Button>
              <Button variant="admin">Create Incident</Button>
            </DialogFooter>
          </DialogContent>
        </Dialog>
      </div>

      {/* Stats */}
      <div className="grid gap-4 md:grid-cols-5">
        <Card>
          <CardHeader className="pb-3">
            <CardDescription>Open</CardDescription>
            <CardTitle className="text-2xl">{mockIncidents.filter(i => i.status === 'open').length}</CardTitle>
          </CardHeader>
        </Card>
        <Card>
          <CardHeader className="pb-3">
            <CardDescription>In Progress</CardDescription>
            <CardTitle className="text-2xl">{mockIncidents.filter(i => i.status === 'in_progress').length}</CardTitle>
          </CardHeader>
        </Card>
        <Card>
          <CardHeader className="pb-3">
            <CardDescription>Resolved</CardDescription>
            <CardTitle className="text-2xl text-green-600">{mockIncidents.filter(i => i.status === 'resolved').length}</CardTitle>
          </CardHeader>
        </Card>
        <Card>
          <CardHeader className="pb-3">
            <CardDescription>Critical</CardDescription>
            <CardTitle className="text-2xl text-red-600">{mockIncidents.filter(i => i.severity === 'critical').length}</CardTitle>
          </CardHeader>
        </Card>
        <Card>
          <CardHeader className="pb-3">
            <CardDescription>Avg. Resolution</CardDescription>
            <CardTitle className="text-2xl">4.2h</CardTitle>
          </CardHeader>
        </Card>
      </div>

      {/* Filters */}
      <div className="flex gap-4">
        <div className="relative flex-1">
          <Search className="absolute left-3 top-1/2 h-4 w-4 -translate-y-1/2 text-muted-foreground" />
          <Input
            placeholder="Search incidents..."
            className="pl-9"
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
          />
        </div>
        <Select value={filterStatus} onValueChange={setFilterStatus}>
          <SelectTrigger className="w-40">
            <SelectValue placeholder="Status" />
          </SelectTrigger>
          <SelectContent>
            <SelectItem value="all">All Status</SelectItem>
            <SelectItem value="open">Open</SelectItem>
            <SelectItem value="in_progress">In Progress</SelectItem>
            <SelectItem value="resolved">Resolved</SelectItem>
            <SelectItem value="closed">Closed</SelectItem>
          </SelectContent>
        </Select>
        <Select value={filterSeverity} onValueChange={setFilterSeverity}>
          <SelectTrigger className="w-40">
            <SelectValue placeholder="Severity" />
          </SelectTrigger>
          <SelectContent>
            <SelectItem value="all">All Severity</SelectItem>
            <SelectItem value="critical">Critical</SelectItem>
            <SelectItem value="high">High</SelectItem>
            <SelectItem value="medium">Medium</SelectItem>
            <SelectItem value="low">Low</SelectItem>
          </SelectContent>
        </Select>
      </div>

      {/* Incidents Table */}
      <Card>
        <CardHeader>
          <CardTitle>Active Incidents</CardTitle>
          <CardDescription>{filteredIncidents.length} incidents found</CardDescription>
        </CardHeader>
        <CardContent>
          <Table>
            <TableHeader>
              <TableRow>
                <TableHead>Incident</TableHead>
                <TableHead>Severity</TableHead>
                <TableHead>Status</TableHead>
                <TableHead>Category</TableHead>
                <TableHead>Assigned To</TableHead>
                <TableHead>Created</TableHead>
                <TableHead className="text-right">Actions</TableHead>
              </TableRow>
            </TableHeader>
            <TableBody>
              {filteredIncidents.map((incident) => (
                <TableRow key={incident.id}>
                  <TableCell>
                    <div className="flex items-start gap-3">
                      <AlertTriangle className={cn(
                        'mt-0.5 h-5 w-5 flex-shrink-0',
                        incident.severity === 'critical' && 'text-red-500',
                        incident.severity === 'high' && 'text-orange-500',
                        incident.severity === 'medium' && 'text-yellow-500'
                      )} />
                      <div>
                        <p className="font-medium">{incident.title}</p>
                        <p className="text-xs text-muted-foreground line-clamp-1">{incident.description}</p>
                      </div>
                    </div>
                  </TableCell>
                  <TableCell>
                    <Badge variant={severityConfig[incident.severity].variant}>
                      {severityConfig[incident.severity].label}
                    </Badge>
                  </TableCell>
                  <TableCell>
                    <Badge variant={statusConfig[incident.status as keyof typeof statusConfig].variant}>
                      {statusConfig[incident.status as keyof typeof statusConfig].label}
                    </Badge>
                  </TableCell>
                  <TableCell className="capitalize">{incident.category}</TableCell>
                  <TableCell>
                    {incident.assignedTo ? (
                      <span className="text-sm">{incident.assignedTo}</span>
                    ) : (
                      <Badge variant="outline">Unassigned</Badge>
                    )}
                  </TableCell>
                  <TableCell>
                    <span className="text-sm text-muted-foreground">{formatDateTime(incident.createdAt)}</span>
                  </TableCell>
                  <TableCell className="text-right">
                    <div className="flex justify-end gap-1">
                      <Button variant="ghost" size="icon" className="h-8 w-8">
                        <CheckCircle className="h-4 w-4 text-green-600" />
                      </Button>
                      <Button variant="ghost" size="icon" className="h-8 w-8">
                        <XCircle className="h-4 w-4 text-red-600" />
                      </Button>
                    </div>
                  </TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </CardContent>
      </Card>
    </div>
  )
}
