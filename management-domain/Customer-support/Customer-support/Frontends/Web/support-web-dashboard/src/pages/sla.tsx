import { useState } from 'react'
import { Link } from 'react-router-dom'
import {
  AlertTriangle,
  CheckCircle,
  Clock,
  Bell,
  Settings,
  TrendingUp,
  TrendingDown,
  Filter,
  Download,
} from 'lucide-react'
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@shared/components/ui/card'
import { Badge } from '@shared/components/ui/badge'
import { Button } from '@shared/components/ui/button'
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from '@shared/components/ui/select'
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from '@shared/components/ui/table'
import {
  Tabs,
  TabsContent,
  TabsList,
  TabsTrigger,
} from '@shared/components/ui/tabs'
import { cn, formatDateTime, formatDuration, formatPercentage } from '@shared/utils/cn'
import { mockSLAViolations, mockTickets, mockCountryMetrics } from '@shared/data/mockData'

export default function SLAPage() {
  const [timeFilter, setTimeFilter] = useState<'today' | 'week' | 'month'>('month')
  const [severityFilter, setSeverityFilter] = useState<'all' | 'critical' | 'warning'>('all')

  const slaBreaches = mockSLAViolations.filter(v => {
    if (severityFilter === 'critical') return v.status === 'open' || v.overdueBy > 60
    if (severityFilter === 'warning') return v.status !== 'resolved'
    return true
  })

  const atRiskTickets = mockTickets.filter(t => t.slaWarning && !t.slaBreached)
  const breachedTickets = mockTickets.filter(t => t.slaBreached)

  const slaStats = {
    overallCompliance: 96.2,
    trend: 'up' as const,
    trendValue: 2.1,
    totalBreaches: mockSLAViolations.length,
    atRisk: atRiskTickets.length,
    onTrack: mockTickets.filter(t => !t.slaBreached && !t.slaWarning).length,
    byPriority: {
      urgent: { compliance: 92, breaches: 3 },
      high: { compliance: 94, breaches: 5 },
      medium: { compliance: 97, breaches: 6 },
      low: { compliance: 99, breaches: 4 },
    },
  }

  return (
    <div className="space-y-6">
      {/* Page Header */}
      <div className="flex items-center justify-between">
        <div className="page-header">
          <h1 className="page-title">SLA Management</h1>
          <p className="page-description">
            Monitor service level agreements and handle breaches
          </p>
        </div>
        <div className="flex gap-2">
          <Button variant="outline" className="gap-2">
            <Download className="h-4 w-4" />
            Export Report
          </Button>
          <Button className="gap-2">
            <Settings className="h-4 w-4" />
            Configure SLA
          </Button>
        </div>
      </div>

      {/* Stats Overview */}
      <div className="metric-grid">
        <Card>
          <CardHeader className="pb-3">
            <div className="flex items-start justify-between">
              <CardTitle className="text-sm font-medium">Overall Compliance</CardTitle>
              <CheckCircle className="h-4 w-4 text-muted-foreground" />
            </div>
          </CardHeader>
          <CardContent className="space-y-3">
            <div className="flex items-baseline gap-2">
              <span className="text-2xl font-bold">{slaStats.overallCompliance}%</span>
            </div>
            <div className="flex items-center gap-2 text-sm">
              {slaStats.trend === 'up' ? (
                <TrendingUp className="h-4 w-4 text-green-500" />
              ) : (
                <TrendingDown className="h-4 w-4 text-red-500" />
              )}
              <span className={slaStats.trend === 'up' ? 'text-green-600' : 'text-red-600'}>
                {formatPercentage(slaStats.trendValue)}
              </span>
            </div>
            <div className="h-2 w-full overflow-hidden rounded-full bg-slate-200 dark:bg-slate-800">
              <div
                className="h-full bg-green-500"
                style={{ width: `${slaStats.overallCompliance}%` }}
              />
            </div>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="pb-3">
            <div className="flex items-start justify-between">
              <CardTitle className="text-sm font-medium">Active Breaches</CardTitle>
              <AlertTriangle className="h-4 w-4 text-muted-foreground" />
            </div>
          </CardHeader>
          <CardContent className="space-y-3">
            <div className="flex items-baseline gap-2">
              <span className="text-2xl font-bold text-red-600">{slaStats.totalBreaches}</span>
              <span className="text-sm text-muted-foreground">tickets</span>
            </div>
            <div className="text-sm text-muted-foreground">
              Requires immediate attention
            </div>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="pb-3">
            <div className="flex items-start justify-between">
              <CardTitle className="text-sm font-medium">At Risk</CardTitle>
              <Clock className="h-4 w-4 text-muted-foreground" />
            </div>
          </CardHeader>
          <CardContent className="space-y-3">
            <div className="flex items-baseline gap-2">
              <span className="text-2xl font-bold text-yellow-600">{slaStats.atRisk}</span>
              <span className="text-sm text-muted-foreground">tickets</span>
            </div>
            <div className="text-sm text-muted-foreground">
              May breach SLA soon
            </div>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="pb-3">
            <div className="flex items-start justify-between">
              <CardTitle className="text-sm font-medium">On Track</CardTitle>
              <CheckCircle className="h-4 w-4 text-muted-foreground" />
            </div>
          </CardHeader>
          <CardContent className="space-y-3">
            <div className="flex items-baseline gap-2">
              <span className="text-2xl font-bold text-green-600">{slaStats.onTrack}</span>
              <span className="text-sm text-muted-foreground">tickets</span>
            </div>
            <div className="text-sm text-muted-foreground">
              Meeting SLA targets
            </div>
          </CardContent>
        </Card>
      </div>

      {/* Tabs for different views */}
      <Tabs defaultValue="breaches" className="space-y-4">
        <TabsList>
          <TabsTrigger value="breaches">SLA Breaches</TabsTrigger>
          <TabsTrigger value="at-risk">At Risk</TabsTrigger>
          <TabsTrigger value="by-priority">By Priority</TabsTrigger>
          <TabsTrigger value="by-country">By Country</TabsTrigger>
        </TabsList>

        {/* Breaches Tab */}
        <TabsContent value="breaches" className="space-y-4">
          <Card>
            <CardHeader>
              <div className="flex items-center justify-between">
                <div>
                  <CardTitle>SLA Breaches</CardTitle>
                  <CardDescription>Tickets that have exceeded their service level targets</CardDescription>
                </div>
                <div className="flex gap-2">
                  <Select value={severityFilter} onValueChange={(v: any) => setSeverityFilter(v)}>
                    <SelectTrigger className="w-[140px]">
                      <SelectValue placeholder="Filter" />
                    </SelectTrigger>
                    <SelectContent>
                      <SelectItem value="all">All Breaches</SelectItem>
                      <SelectItem value="critical">Critical Only</SelectItem>
                      <SelectItem value="warning">Warnings</SelectItem>
                    </SelectContent>
                  </Select>
                </div>
              </div>
            </CardHeader>
            <CardContent>
              <Table>
                <TableHeader>
                  <TableRow>
                    <TableHead>Ticket</TableHead>
                    <TableHead>SLA Type</TableHead>
                    <TableHead>Priority</TableHead>
                    <TableHead>Due Date</TableHead>
                    <TableHead>Breached At</TableHead>
                    <TableHead>Overdue</TableHead>
                    <TableHead>Assigned To</TableHead>
                    <TableHead>Status</TableHead>
                    <TableHead></TableHead>
                  </TableRow>
                </TableHeader>
                <TableBody>
                  {slaBreaches.map((breach) => {
                    const ticket = mockTickets.find(t => t.id === breach.ticketId)
                    return (
                      <TableRow key={breach.id}>
                        <TableCell>
                          <Link
                            to={`/tickets/${breach.ticketId}`}
                            className="font-medium text-[#1976D2] hover:underline"
                          >
                            {breach.ticketNumber}
                          </Link>
                          {ticket && (
                            <p className="text-xs text-muted-foreground truncate max-w-[150px]">
                              {ticket.subject}
                            </p>
                          )}
                        </TableCell>
                        <TableCell className="capitalize">
                          {breach.slaType.replace('_', ' ')}
                        </TableCell>
                        <TableCell>
                          <Badge variant={breach.priority === 'urgent' ? 'destructive' : breach.priority === 'high' ? 'warning' : 'secondary'}>
                            {breach.priority}
                          </Badge>
                        </TableCell>
                        <TableCell>{formatDateTime(breach.dueDate)}</TableCell>
                        <TableCell>{formatDateTime(breach.breachedDate)}</TableCell>
                        <TableCell>
                          <span className="text-red-600 font-medium">
                            {formatDuration(breach.overdueBy)}
                          </span>
                        </TableCell>
                        <TableCell>
                          {breach.assignedTo ? (
                            <span className="text-sm">{mockAgents.find(a => a.id === breach.assignedTo)?.firstName} {mockAgents.find(a => a.id === breach.assignedTo)?.lastName}</span>
                          ) : (
                            <span className="text-muted-foreground">Unassigned</span>
                          )}
                        </TableCell>
                        <TableCell>
                          <Badge
                            variant={
                              breach.status === 'open' ? 'destructive' :
                              breach.status === 'acknowledged' ? 'warning' :
                              breach.status === 'escalated' ? 'destructive' : 'success'
                            }
                          >
                            {breach.status}
                          </Badge>
                        </TableCell>
                        <TableCell>
                          <Button variant="outline" size="sm">Action</Button>
                        </TableCell>
                      </TableRow>
                    )
                  })}
                </TableBody>
              </Table>
            </CardContent>
          </Card>
        </TabsContent>

        {/* At Risk Tab */}
        <TabsContent value="at-risk" className="space-y-4">
          <Card>
            <CardHeader>
              <CardTitle>Tickets At Risk</CardTitle>
              <CardDescription>Tickets approaching their SLA deadline</CardDescription>
            </CardHeader>
            <CardContent>
              <div className="space-y-3">
                {atRiskTickets.map((ticket) => (
                  <div
                    key={ticket.id}
                    className="flex items-center gap-4 rounded-lg border border-yellow-200 bg-yellow-50 dark:bg-yellow-900/20 dark:border-yellow-800 p-4"
                  >
                    <div className="h-10 w-10 rounded-full bg-yellow-200 dark:bg-yellow-900 flex items-center justify-center">
                      <Clock className="h-5 w-5 text-yellow-600 dark:text-yellow-400" />
                    </div>
                    <div className="flex-1 min-w-0">
                      <div className="flex items-center gap-2">
                        <Link
                          to={`/tickets/${ticket.id}`}
                          className="font-medium hover:underline"
                        >
                          {ticket.ticketNumber}
                        </Link>
                        <Badge variant="warning">{ticket.priority}</Badge>
                      </div>
                      <p className="text-sm text-muted-foreground truncate">{ticket.subject}</p>
                    </div>
                    <div className="text-right">
                      <p className="text-sm font-medium text-yellow-600">
                        Due: {ticket.dueDate ? formatDateTime(ticket.dueDate).split(',')[1] : 'N/A'}
                      </p>
                      <p className="text-xs text-muted-foreground">
                        Assigned to: {ticket.assignedAgentName || 'Unassigned'}
                      </p>
                    </div>
                    <Button variant="outline" size="sm">Take Action</Button>
                  </div>
                ))}
                {atRiskTickets.length === 0 && (
                  <div className="text-center py-8 text-muted-foreground">
                    <CheckCircle className="h-12 w-12 mx-auto mb-3 text-green-500" />
                    <p>No tickets currently at risk</p>
                  </div>
                )}
              </div>
            </CardContent>
          </Card>
        </TabsContent>

        {/* By Priority Tab */}
        <TabsContent value="by-priority" className="space-y-4">
          <Card>
            <CardHeader>
              <CardTitle>SLA by Priority</CardTitle>
              <CardDescription>Compliance rates broken down by ticket priority</CardDescription>
            </CardHeader>
            <CardContent>
              <div className="space-y-4">
                {Object.entries(slaStats.byPriority).map(([priority, data]) => (
                  <div key={priority} className="space-y-2">
                    <div className="flex items-center justify-between">
                      <div className="flex items-center gap-2">
                        <Badge variant={priority === 'urgent' ? 'destructive' : priority === 'high' ? 'warning' : 'secondary'}>
                          {priority}
                        </Badge>
                        <span className="text-sm text-muted-foreground">
                          {data.breaches} breaches
                        </span>
                      </div>
                      <span className="text-sm font-medium">{data.compliance}%</span>
                    </div>
                    <div className="h-2 w-full overflow-hidden rounded-full bg-slate-200 dark:bg-slate-800">
                      <div
                        className={cn(
                          'h-full',
                          data.compliance >= 97 ? 'bg-green-500' :
                          data.compliance >= 94 ? 'bg-yellow-500' :
                          'bg-red-500'
                        )}
                        style={{ width: `${data.compliance}%` }}
                      />
                    </div>
                  </div>
                ))}
              </div>
            </CardContent>
          </Card>
        </TabsContent>

        {/* By Country Tab */}
        <TabsContent value="by-country" className="space-y-4">
          <Card>
            <CardHeader>
              <CardTitle>SLA by Country</CardTitle>
              <CardDescription>Regional SLA compliance metrics</CardDescription>
            </CardHeader>
            <CardContent>
              <Table>
                <TableHeader>
                  <TableRow>
                    <TableHead>Country</TableHead>
                    <TableHead>Total Tickets</TableHead>
                    <TableHead>SLA Compliance</TableHead>
                    <TableHead>Breaches</TableHead>
                    <TableHead>Trend</TableHead>
                  </TableRow>
                </TableHeader>
                <TableBody>
                  {mockCountryMetrics.map((country) => (
                    <TableRow key={country.country}>
                      <TableCell className="font-medium">{country.countryName}</TableCell>
                      <TableCell>{country.totalTickets}</TableCell>
                      <TableCell>
                        <div className="flex items-center gap-2">
                          <div className="h-2 w-24 overflow-hidden rounded-full bg-slate-200 dark:bg-slate-800">
                            <div
                              className={cn(
                                'h-full',
                                country.slaCompliance >= 95 ? 'bg-green-500' :
                                country.slaCompliance >= 90 ? 'bg-yellow-500' :
                                'bg-red-500'
                              )}
                              style={{ width: `${country.slaCompliance}%` }}
                            />
                          </div>
                          <span className="text-sm font-medium">{country.slaCompliance}%</span>
                        </div>
                      </TableCell>
                      <TableCell>
                        <Badge variant={country.slaCompliance < 90 ? 'destructive' : 'success'}>
                          {Math.round(country.totalTickets * (1 - country.slaCompliance / 100))}
                        </Badge>
                      </TableCell>
                      <TableCell>
                        <span className="flex items-center gap-1 text-green-600">
                          <TrendingUp className="h-4 w-4" />
                          +{Math.floor(Math.random() * 5 + 1)}%
                        </span>
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

const mockAgents = [
  { id: 'agt-us-001', firstName: 'Emily', lastName: 'Chen' },
  { id: 'agt-spec-001', firstName: 'Michael', lastName: 'Park' },
]
