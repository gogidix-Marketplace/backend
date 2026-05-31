import { Activity, AlertTriangle, CheckCircle, Clock, Server, Users, Lock } from 'lucide-react'
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@shared/components/ui/card'
import { Badge } from '@shared/components/ui/badge'
import { Button } from '@shared/components/ui/button'
import { Progress } from '@shared/components/ui/progress'
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from '@shared/components/ui/table'
import { cn, formatNumber, formatPercentage } from '@shared/utils/cn'
import {
  mockDashboardStats,
  mockSystemHealth,
  mockIncidents,
  mockAccessRequests,
  mockMaintenanceWindows,
} from '@shared/data/mockData'

function StatusIndicator({ status }: { status: 'healthy' | 'degraded' | 'down' | 'maintenance' }) {
  const config = {
    healthy: { color: 'bg-green-500', label: 'Healthy' },
    degraded: { color: 'bg-yellow-500', label: 'Degraded' },
    down: { color: 'bg-red-500', label: 'Down' },
    maintenance: { color: 'bg-blue-500', label: 'Maintenance' },
  }
  return (
    <div className="flex items-center gap-2">
      <span className={cn('h-2 w-2 rounded-full animate-pulse-dot', config[status].color)} />
      <span className="text-xs text-muted-foreground">{config[status].label}</span>
    </div>
  )
}

function MetricCard({
  title,
  value,
  unit,
  change,
  icon: Icon,
  status,
}: {
  title: string
  value: number
  unit?: string
  change?: number
  icon: any
  status?: 'normal' | 'warning' | 'critical'
}) {
  return (
    <Card>
      <CardHeader className="pb-3">
        <div className="flex items-center justify-between">
          <CardTitle className="text-sm font-medium">{title}</CardTitle>
          <Icon className={cn(
            'h-4 w-4',
            status === 'critical' ? 'text-red-500' : status === 'warning' ? 'text-yellow-500' : 'text-admin-blue'
          )} />
        </div>
      </CardHeader>
      <CardContent>
        <div className="flex items-baseline gap-2">
          <span className="text-2xl font-bold">{value}{unit}</span>
          {change !== undefined && (
            <span className={cn(
              'text-xs',
              change > 0 ? 'text-green-600' : change < 0 ? 'text-red-600' : 'text-gray-600'
            )}>
              {change > 0 ? '+' : ''}{change}%
            </span>
          )}
        </div>
        {status === 'critical' && (
          <Badge variant="destructive" className="mt-2 text-xs">Critical</Badge>
        )}
        {status === 'warning' && (
          <Badge variant="warning" className="mt-2 text-xs">Warning</Badge>
        )}
      </CardContent>
    </Card>
  )
}

export default function OverviewPage() {
  return (
    <div className="space-y-6">
      {/* Page Header */}
      <div className="page-header">
        <h1 className="page-title">System Overview</h1>
        <p className="page-description">
          Real-time monitoring and status of all Gogidix systems
        </p>
      </div>

      {/* System Health Score */}
      <Card className="admin-gradient text-white">
        <CardContent className="p-6">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-sm text-white/80">System Health Score</p>
              <div className="flex items-baseline gap-2">
                <span className="text-4xl font-bold">{mockDashboardStats.systemHealth}</span>
                <span className="text-lg text-white/80">/100</span>
                <Badge className="ml-2 bg-white/20 text-white hover:bg-white/30">
                  All Systems Operational
                </Badge>
              </div>
              <p className="mt-2 text-sm text-white/70">
                {mockDashboardStats.activeUsers} active users · {mockDashboardStats.uptime}% uptime
              </p>
            </div>
            <div className="hidden md:block">
              <div className="relative h-32 w-32">
                <svg className="h-full w-full transform -rotate-90">
                  <circle
                    cx="64"
                    cy="64"
                    r="56"
                    fill="none"
                    stroke="rgba(255,255,255,0.2)"
                    strokeWidth="12"
                  />
                  <circle
                    cx="64"
                    cy="64"
                    r="56"
                    fill="none"
                    stroke="white"
                    strokeWidth="12"
                    strokeDasharray={`${mockDashboardStats.systemHealth * 3.52} 352`}
                    strokeLinecap="round"
                  />
                </svg>
                <div className="absolute inset-0 flex items-center justify-center">
                  <CheckCircle className="h-12 w-12 text-white" />
                </div>
              </div>
            </div>
          </div>
        </CardContent>
      </Card>

      {/* Key Metrics */}
      <div className="metric-grid">
        <MetricCard
          title="Active Incidents"
          value={mockDashboardStats.activeIncidents}
          icon={AlertTriangle}
          status={mockDashboardStats.activeIncidents > 0 ? 'warning' : 'normal'}
        />
        <MetricCard
          title="Pending Requests"
          value={mockDashboardStats.pendingRequests}
          icon={Lock}
          status={mockDashboardStats.pendingRequests > 5 ? 'warning' : 'normal'}
        />
        <MetricCard
          title="Security Threats"
          value={mockDashboardStats.threatsDetected}
          icon={Shield}
          status={mockDashboardStats.threatsDetected > 0 ? 'critical' : 'normal'}
        />
        <MetricCard
          title="Active Users"
          value={mockDashboardStats.activeUsers}
          icon={Users}
        />
      </div>

      {/* Resource Usage */}
      <Card>
        <CardHeader>
          <CardTitle>Resource Usage</CardTitle>
          <CardDescription>Real-time system resource consumption</CardDescription>
        </CardHeader>
        <CardContent className="space-y-4">
          <div className="space-y-2">
            <div className="flex items-center justify-between text-sm">
              <span className="flex items-center gap-2">
                <Activity className="h-4 w-4" />
                CPU Usage
              </span>
              <span className="font-medium">{mockDashboardStats.cpuUsage}%</span>
            </div>
            <Progress value={mockDashboardStats.cpuUsage} className="h-2" />
          </div>
          <div className="space-y-2">
            <div className="flex items-center justify-between text-sm">
              <span className="flex items-center gap-2">
                <Server className="h-4 w-4" />
                Memory Usage
              </span>
              <span className="font-medium">{mockDashboardStats.memoryUsage}%</span>
            </div>
            <Progress value={mockDashboardStats.memoryUsage} className="h-2" />
          </div>
          <div className="space-y-2">
            <div className="flex items-center justify-between text-sm">
              <span className="flex items-center gap-2">
                <CheckCircle className="h-4 w-4" />
                Disk Usage
              </span>
              <span className="font-medium">{mockDashboardStats.diskUsage}%</span>
            </div>
            <Progress value={mockDashboardStats.diskUsage} className="h-2" />
          </div>
          <div className="space-y-2">
            <div className="flex items-center justify-between text-sm">
              <span className="flex items-center gap-2">
                <Activity className="h-4 w-4" />
                Network Usage
              </span>
              <span className="font-medium">{mockDashboardStats.networkUsage}%</span>
            </div>
            <Progress value={mockDashboardStats.networkUsage} className="h-2" />
          </div>
        </CardContent>
      </Card>

      <div className="grid gap-6 md:grid-cols-2">
        {/* Service Health */}
        <Card>
          <CardHeader>
            <CardTitle>Service Health</CardTitle>
            <CardDescription>Status of core services</CardDescription>
          </CardHeader>
          <CardContent>
            <Table>
              <TableHeader>
                <TableRow>
                  <TableHead>Service</TableHead>
                  <TableHead>Status</TableHead>
                  <TableHead className="text-right">Uptime</TableHead>
                </TableRow>
              </TableHeader>
              <TableBody>
                {mockSystemHealth.map((service) => (
                  <TableRow key={service.id}>
                    <TableCell className="font-medium">{service.service}</TableCell>
                    <TableCell>
                      <StatusIndicator status={service.status} />
                    </TableCell>
                    <TableCell className="text-right">{service.uptime}%</TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          </CardContent>
        </Card>

        {/* Active Incidents */}
        <Card>
          <CardHeader>
            <div className="flex items-center justify-between">
              <div>
                <CardTitle>Active Incidents</CardTitle>
                <CardDescription>Incidents requiring attention</CardDescription>
              </div>
              <Badge variant="destructive">{mockIncidents.length}</Badge>
            </div>
          </CardHeader>
          <CardContent className="space-y-3">
            {mockIncidents.map((incident) => (
              <div
                key={incident.id}
                className="flex items-start gap-3 rounded-lg border p-3 hover:bg-slate-50 dark:hover:bg-slate-800"
              >
                <AlertTriangle className={cn(
                  'mt-0.5 h-5 w-5 flex-shrink-0',
                  incident.severity === 'critical' && 'text-red-500',
                  incident.severity === 'high' && 'text-orange-500',
                  incident.severity === 'medium' && 'text-yellow-500'
                )} />
                <div className="flex-1 min-w-0">
                  <div className="flex items-start justify-between gap-2">
                    <p className="font-medium truncate">{incident.title}</p>
                    <Badge
                      variant={
                        incident.severity === 'critical'
                          ? 'destructive'
                          : incident.severity === 'high'
                          ? 'warning'
                          : 'secondary'
                      }
                      className="flex-shrink-0"
                    >
                      {incident.severity}
                    </Badge>
                  </div>
                  <p className="text-xs text-muted-foreground mt-1">
                    {incident.category} · {incident.affectedServices.join(', ')}
                  </p>
                </div>
              </div>
            ))}
            <Button variant="outline" size="sm" className="w-full">
              View All Incidents
            </Button>
          </CardContent>
        </Card>
      </div>

      {/* Pending Access Requests & Upcoming Maintenance */}
      <div className="grid gap-6 md:grid-cols-2">
        {/* Pending Access Requests */}
        <Card>
          <CardHeader>
            <div className="flex items-center justify-between">
              <div>
                <CardTitle>Pending Access Requests</CardTitle>
                <CardDescription>Requests awaiting approval</CardDescription>
              </div>
              <Badge variant="warning">{mockAccessRequests.filter(r => r.status === 'pending').length}</Badge>
            </div>
          </CardHeader>
          <CardContent className="space-y-3">
            {mockAccessRequests.filter(r => r.status === 'pending').slice(0, 4).map((request) => (
              <div
                key={request.id}
                className="flex items-start gap-3 rounded-lg border p-3 hover:bg-slate-50 dark:hover:bg-slate-800"
              >
                <Lock className="h-5 w-5 text-admin-blue flex-shrink-0 mt-0.5" />
                <div className="flex-1 min-w-0">
                  <div className="flex items-start justify-between gap-2">
                    <div>
                      <p className="font-medium">{request.userName}</p>
                      <p className="text-xs text-muted-foreground">
                        {request.resource} · {request.accessType}
                      </p>
                    </div>
                    <Badge variant="outline" className="flex-shrink-0 text-xs">
                      {request.resourceType}
                    </Badge>
                  </div>
                </div>
              </div>
            ))}
            <Button variant="outline" size="sm" className="w-full">
              Review All Requests
            </Button>
          </CardContent>
        </Card>

        {/* Upcoming Maintenance */}
        <Card>
          <CardHeader>
            <div className="flex items-center justify-between">
              <div>
                <CardTitle>Upcoming Maintenance</CardTitle>
                <CardDescription>Scheduled maintenance windows</CardDescription>
              </div>
              <Badge variant="info">
                {mockMaintenanceWindows.filter(m => m.status === 'scheduled').length}
              </Badge>
            </div>
          </CardHeader>
          <CardContent className="space-y-3">
            {mockMaintenanceWindows.filter(m => m.status === 'scheduled').map((maintenance) => (
              <div
                key={maintenance.id}
                className="flex items-start gap-3 rounded-lg border p-3 hover:bg-slate-50 dark:hover:bg-slate-800"
              >
                <Clock className="h-5 w-5 text-admin-blue flex-shrink-0 mt-0.5" />
                <div className="flex-1 min-w-0">
                  <div className="flex items-start justify-between gap-2">
                    <div>
                      <p className="font-medium">{maintenance.title}</p>
                      <p className="text-xs text-muted-foreground">
                        {new Date(maintenance.startAt).toLocaleDateString()} · {maintenance.affectedServices.length} services
                      </p>
                    </div>
                    <Badge
                      variant={maintenance.impact === 'high' ? 'destructive' : maintenance.impact === 'medium' ? 'warning' : 'secondary'}
                      className="flex-shrink-0"
                    >
                      {maintenance.impact}
                    </Badge>
                  </div>
                </div>
              </div>
            ))}
            <Button variant="outline" size="sm" className="w-full">
              View Maintenance Schedule
            </Button>
          </CardContent>
        </Card>
      </div>
    </div>
  )
}
