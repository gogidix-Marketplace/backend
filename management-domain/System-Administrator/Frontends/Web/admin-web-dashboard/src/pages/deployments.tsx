import { useState } from 'react'
import { Rocket, History, Play, RotateCcw, Search, Plus, Calendar } from 'lucide-react'
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@shared/components/ui/card'
import { Badge } from '@shared/components/ui/badge'
import { Button } from '@shared/components/ui/button'
import { Input } from '@shared/components/ui/input'
import { Label } from '@shared/components/ui/label'
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
import { Textarea } from '@shared/components/ui/textarea'
import { cn, formatDateTime } from '@shared/utils/cn'
import { mockDeployments } from '@shared/data/mockData'

export default function DeploymentsPage() {
  const [searchQuery, setSearchQuery] = useState('')
  const [createDialogOpen, setCreateDialogOpen] = useState(false)

  const statusConfig = {
    pending: { label: 'Pending', color: 'bg-slate-500', variant: 'secondary' as const },
    deploying: { label: 'Deploying', color: 'bg-blue-500', variant: 'info' as const },
    success: { label: 'Success', color: 'bg-green-500', variant: 'success' as const },
    failed: { label: 'Failed', color: 'bg-red-500', variant: 'destructive' as const },
    rolled_back: { label: 'Rolled Back', color: 'bg-orange-500', variant: 'warning' as const },
  }

  return (
    <div className="space-y-6">
      {/* Page Header */}
      <div className="flex items-center justify-between">
        <div className="page-header">
          <h1 className="page-title">Deployment Management</h1>
          <p className="page-description">
            Manage releases, deployments, and rollbacks across environments
          </p>
        </div>
        <Dialog open={createDialogOpen} onOpenChange={setCreateDialogOpen}>
          <DialogTrigger asChild>
            <Button variant="admin">
              <Plus className="mr-2 h-4 w-4" />
              New Deployment
            </Button>
          </DialogTrigger>
          <DialogContent className="max-w-2xl">
            <DialogHeader>
              <DialogTitle>Create New Deployment</DialogTitle>
              <DialogDescription>
                Deploy a new version to an environment
              </DialogDescription>
            </DialogHeader>
            <div className="grid gap-4 py-4">
              <div className="grid gap-2">
                <Label htmlFor="name">Deployment Name *</Label>
                <Input id="name" placeholder="e.g., Q2 Feature Release" />
              </div>
              <div className="grid grid-cols-2 gap-4">
                <div className="grid gap-2">
                  <Label htmlFor="version">Version *</Label>
                  <Input id="version" placeholder="e.g., v2.5.0" />
                </div>
                <div className="grid gap-2">
                  <Label htmlFor="environment">Environment *</Label>
                  <Select>
                    <SelectTrigger id="environment">
                      <SelectValue placeholder="Select" />
                    </SelectTrigger>
                    <SelectContent>
                      <SelectItem value="development">Development</SelectItem>
                      <SelectItem value="staging">Staging</SelectItem>
                      <SelectItem value="production">Production</SelectItem>
                    </SelectContent>
                  </Select>
                </div>
              </div>
              <div className="grid gap-2">
                <Label htmlFor="services">Affected Services *</Label>
                <Input id="services" placeholder="e.g., API Gateway, Frontend" />
              </div>
              <div className="grid gap-2">
                <Label htmlFor="changes">Changes *</Label>
                <Textarea
                  id="changes"
                  placeholder="List the changes included in this deployment"
                  rows={3}
                />
              </div>
              <div className="grid gap-2">
                <Label htmlFor="schedule">Schedule (Optional)</Label>
                <div className="flex gap-2">
                  <Input id="schedule" type="datetime-local" className="flex-1" />
                  <Button variant="outline" type="button">
                    <Calendar className="mr-2 h-4 w-4" />
                    Deploy Now
                  </Button>
                </div>
              </div>
            </div>
            <DialogFooter>
              <Button variant="outline" onClick={() => setCreateDialogOpen(false)}>Cancel</Button>
              <Button variant="admin">
                <Rocket className="mr-2 h-4 w-4" />
                Create Deployment
              </Button>
            </DialogFooter>
          </DialogContent>
        </Dialog>
      </div>

      {/* Stats */}
      <div className="grid gap-4 md:grid-cols-5">
        <Card>
          <CardHeader className="pb-3">
            <CardDescription>Total Deployments</CardDescription>
            <CardTitle className="text-2xl">{mockDeployments.length}</CardTitle>
          </CardHeader>
        </Card>
        <Card>
          <CardHeader className="pb-3">
            <CardDescription>Success Rate</CardDescription>
            <CardTitle className="text-2xl text-green-600">75%</CardTitle>
          </CardHeader>
        </Card>
        <Card>
          <CardHeader className="pb-3">
            <CardDescription>In Progress</CardDescription>
            <CardTitle className="text-2xl">{mockDeployments.filter(d => d.status === 'deploying').length}</CardTitle>
          </CardHeader>
        </Card>
        <Card>
          <CardHeader className="pb-3">
            <CardDescription>Failed</CardDescription>
            <CardTitle className="text-2xl text-red-600">{mockDeployments.filter(d => d.status === 'failed').length}</CardTitle>
          </CardHeader>
        </Card>
        <Card>
          <CardHeader className="pb-3">
            <CardDescription>Avg. Duration</CardDescription>
            <CardTitle className="text-2xl">12m</CardTitle>
          </CardHeader>
        </Card>
      </div>

      {/* Search */}
      <div className="flex gap-4">
        <div className="relative flex-1">
          <Search className="absolute left-3 top-1/2 h-4 w-4 -translate-y-1/2 text-muted-foreground" />
          <Input
            placeholder="Search deployments..."
            className="pl-9"
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
          />
        </div>
        <Select defaultValue="all">
          <SelectTrigger className="w-40">
            <SelectValue placeholder="Environment" />
          </SelectTrigger>
          <SelectContent>
            <SelectItem value="all">All Environments</SelectItem>
            <SelectItem value="production">Production</SelectItem>
            <SelectItem value="staging">Staging</SelectItem>
            <SelectItem value="development">Development</SelectItem>
          </SelectContent>
        </Select>
        <Select defaultValue="all">
          <SelectTrigger className="w-40">
            <SelectValue placeholder="Status" />
          </SelectTrigger>
          <SelectContent>
            <SelectItem value="all">All Status</SelectItem>
            <SelectItem value="success">Success</SelectItem>
            <SelectItem value="failed">Failed</SelectItem>
            <SelectItem value="deploying">Deploying</SelectItem>
          </SelectContent>
        </Select>
      </div>

      {/* Deployments Table */}
      <Card>
        <CardHeader>
          <CardTitle>Deployment History</CardTitle>
          <CardDescription>Recent deployments across all environments</CardDescription>
        </CardHeader>
        <CardContent>
          <Table>
            <TableHeader>
              <TableRow>
                <TableHead>Deployment</TableHead>
                <TableHead>Version</TableHead>
                <TableHead>Environment</TableHead>
                <TableHead>Status</TableHead>
                <TableHead>Deployed By</TableHead>
                <TableHead>Deployed At</TableHead>
                <TableHead>Duration</TableHead>
                <TableHead className="text-right">Actions</TableHead>
              </TableRow>
            </TableHeader>
            <TableBody>
              {mockDeployments.map((deployment) => (
                <TableRow key={deployment.id}>
                  <TableCell>
                    <div className="flex items-center gap-2">
                      <Rocket className={cn(
                        'h-4 w-4',
                        deployment.status === 'success' && 'text-green-500',
                        deployment.status === 'failed' && 'text-red-500',
                        deployment.status === 'deploying' && 'text-blue-500 animate-pulse'
                      )} />
                      <span className="font-medium">{deployment.name}</span>
                    </div>
                  </TableCell>
                  <TableCell>
                    <Badge variant="outline" className="font-mono">{deployment.version}</Badge>
                  </TableCell>
                  <TableCell className="capitalize">{deployment.environment}</TableCell>
                  <TableCell>
                    <Badge variant={statusConfig[deployment.status as keyof typeof statusConfig].variant}>
                      {statusConfig[deployment.status as keyof typeof statusConfig].label}
                    </Badge>
                  </TableCell>
                  <TableCell>{deployment.deployedBy}</TableCell>
                  <TableCell>
                    {deployment.deployedAt ? formatDateTime(deployment.deployedAt) : '-'}
                  </TableCell>
                  <TableCell>
                    {deployment.duration ? `${deployment.duration}m` : '-'}
                  </TableCell>
                  <TableCell className="text-right">
                    <div className="flex justify-end gap-1">
                      {deployment.status === 'deploying' && (
                        <Button variant="ghost" size="icon" className="h-8 w-8">
                          <Play className="h-4 w-4" />
                        </Button>
                      )}
                      {deployment.status === 'failed' && deployment.rollbackVersion && (
                        <Button variant="ghost" size="icon" className="h-8 w-8">
                          <RotateCcw className="h-4 w-4 text-orange-500" />
                        </Button>
                      )}
                      <Button variant="ghost" size="sm">Details</Button>
                    </div>
                  </TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </CardContent>
      </Card>

      {/* Scheduled Deployments */}
      <Card>
        <CardHeader>
          <CardTitle>Scheduled Deployments</CardTitle>
          <CardDescription>Upcoming deployments scheduled for future dates</CardDescription>
        </CardHeader>
        <CardContent>
          <div className="space-y-3">
            <div className="flex items-center justify-between rounded-lg border p-4">
              <div className="flex items-center gap-4">
                <Calendar className="h-5 w-5 text-admin-blue" />
                <div>
                  <p className="font-medium">Database Migration</p>
                  <p className="text-sm text-muted-foreground">v2.6.0 · Production · Scheduled for March 15, 2024 02:00 UTC</p>
                </div>
              </div>
              <div className="flex gap-2">
                <Button variant="outline" size="sm">Edit</Button>
                <Button variant="ghost" size="sm" className="text-destructive">Cancel</Button>
              </div>
            </div>
            <div className="flex items-center justify-between rounded-lg border p-4">
              <div className="flex items-center gap-4">
                <Calendar className="h-5 w-5 text-admin-blue" />
                <div>
                  <p className="font-medium">Feature Release - Q2</p>
                  <p className="text-sm text-muted-foreground">v2.7.0 · Staging · Scheduled for March 20, 2024 10:00 UTC</p>
                </div>
              </div>
              <div className="flex gap-2">
                <Button variant="outline" size="sm">Edit</Button>
                <Button variant="ghost" size="sm" className="text-destructive">Cancel</Button>
              </div>
            </div>
          </div>
        </CardContent>
      </Card>
    </div>
  )
}
