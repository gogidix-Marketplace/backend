import { useState } from 'react'
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@shared/components/ui/card'
import { Badge } from '@shared/components/ui/badge'
import { Button } from '@shared/components/ui/button'
import { Input } from '@shared/components/ui/input'
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from '@shared/components/ui/select'
import { Tabs, TabsContent, TabsList, TabsTrigger } from '@shared/components/ui/tabs'
import { MOCK_ALERTS, MOCK_SERVICES } from '@shared/stores/mockData'
import { formatRelativeTime, getSeverityDotColor, getStatusColor } from '@shared/utils/cn'
import {
  AlertTriangle,
  CheckCircle,
  Clock,
  Filter,
  Search,
  ExternalLink,
  MoreHorizontal,
} from 'lucide-react'
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuTrigger,
} from '@shared/components/ui/dropdown-menu'

export default function AlertsPage() {
  const [searchQuery, setSearchQuery] = useState('')
  const [severityFilter, setSeverityFilter] = useState<string>('all')
  const [statusFilter, setStatusFilter] = useState<string>('active')

  const filteredAlerts = MOCK_ALERTS.filter((alert) => {
    const matchesSearch = alert.title.toLowerCase().includes(searchQuery.toLowerCase()) ||
      alert.serviceName.toLowerCase().includes(searchQuery.toLowerCase())
    const matchesSeverity = severityFilter === 'all' || alert.severity === severityFilter
    const matchesStatus = statusFilter === 'all' || alert.status === statusFilter
    return matchesSearch && matchesSeverity && matchesStatus
  })

  const activeAlerts = MOCK_ALERTS.filter(a => a.status === 'active')
  const acknowledgedAlerts = MOCK_ALERTS.filter(a => a.status === 'acknowledged')
  const resolvedAlerts = MOCK_ALERTS.filter(a => a.status === 'resolved' || a.status === 'dismissed')

  const handleAcknowledge = (alertId: string) => {
    alert(`Alert ${alertId} acknowledged`)
  }

  const handleResolve = (alertId: string) => {
    alert(`Alert ${alertId} resolved`)
  }

  const handleDismiss = (alertId: string) => {
    alert(`Alert ${alertId} dismissed`)
  }

  const renderAlert = (alert: typeof MOCK_ALERTS[0]) => (
    <div key={alert.id} className="flex items-start justify-between rounded-lg border p-4 hover:bg-slate-50 transition-colors">
      <div className="flex items-start gap-3">
        <div className={`mt-0.5 h-2.5 w-2.5 rounded-full ${getSeverityDotColor(alert.severity)}`} />
        <div>
          <div className="flex items-center gap-2 mb-1">
            <h4 className="font-semibold">{alert.title}</h4>
            <Badge variant={alert.severity === 'critical' ? 'critical' : alert.severity === 'error' ? 'destructive' : 'degraded'}>
              {alert.severity}
            </Badge>
            <Badge variant={alert.status === 'active' ? 'info' : alert.status === 'acknowledged' ? 'warning' : 'success'}>
              {alert.status}
            </Badge>
          </div>
          <p className="text-sm text-slate-600 mb-2">{alert.description}</p>
          <div className="flex items-center gap-4 text-xs text-slate-500">
            <span className="flex items-center gap-1">
              <Clock className="h-3 w-3" />
              {formatRelativeTime(alert.triggeredAt)}
            </span>
            <span>{alert.serviceName}</span>
            {alert.acknowledgedBy && (
              <span>Acknowledged by {alert.acknowledgedBy}</span>
            )}
            {alert.resolvedBy && (
              <span>Resolved by {alert.resolvedBy}</span>
            )}
          </div>
          {alert.actions.length > 0 && (
            <div className="mt-2 text-xs text-slate-500">
              <span className="font-medium">History: </span>
              {alert.actions.map((action, i) => (
                <span key={action.id}>
                  {action.type} by {action.userName}{i < alert.actions.length - 1 ? ', ' : ''}
                </span>
              ))}
            </div>
          )}
        </div>
      </div>
      <div className="flex items-center gap-2">
        <Button variant="ghost" size="icon">
          <ExternalLink className="h-4 w-4" />
        </Button>
        <DropdownMenu>
          <DropdownMenuTrigger asChild>
            <Button variant="ghost" size="icon">
              <MoreHorizontal className="h-4 w-4" />
            </Button>
          </DropdownMenuTrigger>
          <DropdownMenuContent align="end">
            {alert.status === 'active' && (
              <>
                <DropdownMenuItem onClick={() => handleAcknowledge(alert.id)}>
                  <CheckCircle className="mr-2 h-4 w-4" />
                  Acknowledge
                </DropdownMenuItem>
                <DropdownMenuItem onClick={() => handleResolve(alert.id)}>
                  <CheckCircle className="mr-2 h-4 w-4" />
                  Resolve
                </DropdownMenuItem>
              </>
            )}
            {alert.status === 'acknowledged' && (
              <DropdownMenuItem onClick={() => handleResolve(alert.id)}>
                <CheckCircle className="mr-2 h-4 w-4" />
                Resolve
              </DropdownMenuItem>
            )}
            <DropdownMenuItem onClick={() => handleDismiss(alert.id)}>
              Dismiss
            </DropdownMenuItem>
          </DropdownMenuContent>
        </DropdownMenu>
      </div>
    </div>
  )

  return (
    <div className="space-y-6">
      {/* Page Header */}
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-3xl font-bold text-slate-900">Alerts</h1>
          <p className="text-slate-600">Manage and respond to service alerts</p>
        </div>
        <div className="flex items-center gap-2">
          <Button variant="outline">Configure Rules</Button>
          <Button variant="monitoring">Create Alert Rule</Button>
        </div>
      </div>

      {/* Alert Summary */}
      <div className="grid gap-4 md:grid-cols-4">
        <Card>
          <CardHeader className="flex flex-row items-center justify-between pb-2">
            <CardTitle className="text-sm font-medium">Active</CardTitle>
            <AlertTriangle className="h-4 w-4 text-red-500" />
          </CardHeader>
          <CardContent>
            <div className="text-3xl font-bold text-red-600">{activeAlerts.length}</div>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="flex flex-row items-center justify-between pb-2">
            <CardTitle className="text-sm font-medium">Acknowledged</CardTitle>
            <Clock className="h-4 w-4 text-yellow-500" />
          </CardHeader>
          <CardContent>
            <div className="text-3xl font-bold text-yellow-600">{acknowledgedAlerts.length}</div>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="flex flex-row items-center justify-between pb-2">
            <CardTitle className="text-sm font-medium">Resolved</CardTitle>
            <CheckCircle className="h-4 w-4 text-green-500" />
          </CardHeader>
          <CardContent>
            <div className="text-3xl font-bold text-green-600">{resolvedAlerts.length}</div>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="flex flex-row items-center justify-between pb-2">
            <CardTitle className="text-sm font-medium">Critical</CardTitle>
            <AlertTriangle className="h-4 w-4 text-red-500" />
          </CardHeader>
          <CardContent>
            <div className="text-3xl font-bold text-red-600">
              {MOCK_ALERTS.filter(a => a.severity === 'critical' && a.status === 'active').length}
            </div>
          </CardContent>
        </Card>
      </div>

      {/* Filters */}
      <Card>
        <CardContent className="p-4">
          <div className="flex flex-col sm:flex-row gap-4">
            <div className="relative flex-1">
              <Search className="absolute left-3 top-1/2 h-4 w-4 -translate-y-1/2 text-slate-400" />
              <Input
                placeholder="Search alerts..."
                value={searchQuery}
                onChange={(e) => setSearchQuery(e.target.value)}
                className="pl-9"
              />
            </div>
            <Select value={severityFilter} onValueChange={setSeverityFilter}>
              <SelectTrigger className="w-full sm:w-[180px]">
                <SelectValue placeholder="Filter by severity" />
              </SelectTrigger>
              <SelectContent>
                <SelectItem value="all">All Severities</SelectItem>
                <SelectItem value="critical">Critical</SelectItem>
                <SelectItem value="error">Error</SelectItem>
                <SelectItem value="warning">Warning</SelectItem>
                <SelectItem value="info">Info</SelectItem>
              </SelectContent>
            </Select>
            <Select value={statusFilter} onValueChange={setStatusFilter}>
              <SelectTrigger className="w-full sm:w-[180px]">
                <SelectValue placeholder="Filter by status" />
              </SelectTrigger>
              <SelectContent>
                <SelectItem value="all">All Statuses</SelectItem>
                <SelectItem value="active">Active</SelectItem>
                <SelectItem value="acknowledged">Acknowledged</SelectItem>
                <SelectItem value="resolved">Resolved</SelectItem>
                <SelectItem value="dismissed">Dismissed</SelectItem>
              </SelectContent>
            </Select>
          </div>
        </CardContent>
      </Card>

      {/* Alerts Tabs */}
      <Tabs defaultValue="active">
        <TabsList>
          <TabsTrigger value="active">
            Active ({activeAlerts.length})
          </TabsTrigger>
          <TabsTrigger value="acknowledged">
            Acknowledged ({acknowledgedAlerts.length})
          </TabsTrigger>
          <TabsTrigger value="resolved">
            Resolved ({resolvedAlerts.length})
          </TabsTrigger>
        </TabsList>

        <TabsContent value="active" className="space-y-3">
          {activeAlerts.length > 0 ? (
            filteredAlerts.filter(a => a.status === 'active').map(renderAlert)
          ) : (
            <Card>
              <CardContent className="p-12 text-center">
                <CheckCircle className="mx-auto h-12 w-12 text-green-500 mb-4" />
                <h3 className="text-lg font-semibold mb-2">No active alerts</h3>
                <p className="text-slate-600">All systems are operating normally</p>
              </CardContent>
            </Card>
          )}
        </TabsContent>

        <TabsContent value="acknowledged" className="space-y-3">
          {acknowledgedAlerts.length > 0 ? (
            filteredAlerts.filter(a => a.status === 'acknowledged').map(renderAlert)
          ) : (
            <Card>
              <CardContent className="p-12 text-center">
                <Clock className="mx-auto h-12 w-12 text-slate-400 mb-4" />
                <h3 className="text-lg font-semibold mb-2">No acknowledged alerts</h3>
                <p className="text-slate-600">No alerts are currently being investigated</p>
              </CardContent>
            </Card>
          )}
        </TabsContent>

        <TabsContent value="resolved" className="space-y-3">
          {resolvedAlerts.length > 0 ? (
            filteredAlerts.filter(a => a.status === 'resolved' || a.status === 'dismissed').map(renderAlert)
          ) : (
            <Card>
              <CardContent className="p-12 text-center">
                <CheckCircle className="mx-auto h-12 w-12 text-slate-400 mb-4" />
                <h3 className="text-lg font-semibold mb-2">No resolved alerts</h3>
                <p className="text-slate-600">No alerts have been resolved recently</p>
              </CardContent>
            </Card>
          )}
        </TabsContent>
      </Tabs>
    </div>
  )
}
