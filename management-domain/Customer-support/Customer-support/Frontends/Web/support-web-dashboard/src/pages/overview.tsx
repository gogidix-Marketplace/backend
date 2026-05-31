import { Link } from 'react-router-dom'
import {
  TrendingUp,
  TrendingDown,
  Minus,
  ArrowUpRight,
  ArrowDownRight,
  AlertTriangle,
  CheckCircle,
  Clock,
  MessageSquare,
  Phone,
  Mail,
  Users,
  Ticket,
} from 'lucide-react'
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@shared/components/ui/card'
import { Badge } from '@shared/components/ui/badge'
import { Button } from '@shared/components/ui/button'
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from '@shared/components/ui/table'
import { cn, formatDuration, formatNumber, formatPercentage, getSLAStatusColor } from '@shared/utils/cn'
import { mockSupportMetrics, mockCountryMetrics, mockSLAViolations, mockTickets } from '@shared/data/mockData'

export default function OverviewPage() {
  const metrics = mockSupportMetrics
  const countryMetrics = mockCountryMetrics
  const slaBreaches = mockSLAViolations.filter(v => v.status === 'open' || v.status === 'acknowledged')
  const recentTickets = mockTickets.slice(0, 5)

  const channelIcons = {
    email: Mail,
    chat: MessageSquare,
    phone: Phone,
    web: MessageSquare,
    api: MessageSquare,
    social: MessageSquare,
  }

  return (
    <div className="space-y-6">
      {/* Page Header */}
      <div className="page-header">
        <h1 className="page-title">Support Overview</h1>
        <p className="page-description">
          Global support operations metrics and performance
        </p>
      </div>

      {/* Key Metrics Cards */}
      <div className="metric-grid">
        {/* Total Tickets */}
        <Card>
          <CardHeader className="pb-3">
            <div className="flex items-start justify-between">
              <CardTitle className="text-sm font-medium">Total Tickets</CardTitle>
              <Ticket className="h-4 w-4 text-muted-foreground" />
            </div>
          </CardHeader>
          <CardContent className="space-y-3">
            <div className="flex items-baseline gap-2">
              <span className="text-2xl font-bold">{formatNumber(metrics.totalTickets)}</span>
              <span className="text-sm text-muted-foreground">this month</span>
            </div>
            <div className="flex items-center gap-2 text-sm">
              <TrendingUp className="h-4 w-4 text-green-500" />
              <span className="text-green-600">+12.5%</span>
              <span className="text-muted-foreground">vs last month</span>
            </div>
            <div className="grid grid-cols-2 gap-2 text-xs">
              <div className="flex items-center gap-1">
                <div className="h-2 w-2 rounded-full bg-blue-500" />
                <span>Open: {metrics.openTickets}</span>
              </div>
              <div className="flex items-center gap-1">
                <div className="h-2 w-2 rounded-full bg-green-500" />
                <span>Resolved: {metrics.resolvedTickets}</span>
              </div>
            </div>
          </CardContent>
        </Card>

        {/* SLA Compliance */}
        <Card>
          <CardHeader className="pb-3">
            <div className="flex items-start justify-between">
              <CardTitle className="text-sm font-medium">SLA Compliance</CardTitle>
              <CheckCircle className="h-4 w-4 text-muted-foreground" />
            </div>
          </CardHeader>
          <CardContent className="space-y-3">
            <div className="flex items-baseline gap-2">
              <span className="text-2xl font-bold">96.2%</span>
              <span className="text-sm text-muted-foreground">overall</span>
            </div>
            <div className="flex items-center gap-2 text-sm">
              <TrendingUp className="h-4 w-4 text-green-500" />
              <span className="text-green-600">+2.1%</span>
              <span className="text-muted-foreground">vs last month</span>
            </div>
            <div className="space-y-2">
              <div className="flex justify-between text-xs">
                <span>Breaches</span>
                <span className="text-red-600 font-medium">{metrics.slaBreaches}</span>
              </div>
              <div className="h-2 w-full overflow-hidden rounded-full bg-slate-200 dark:bg-slate-800">
                <div className="h-full bg-green-500" style={{ width: '96.2%' }} />
              </div>
            </div>
          </CardContent>
        </Card>

        {/* Response Time */}
        <Card>
          <CardHeader className="pb-3">
            <div className="flex items-start justify-between">
              <CardTitle className="text-sm font-medium">Avg Response Time</CardTitle>
              <Clock className="h-4 w-4 text-muted-foreground" />
            </div>
          </CardHeader>
          <CardContent className="space-y-3">
            <div className="flex items-baseline gap-2">
              <span className="text-2xl font-bold">{formatDuration(metrics.averageFirstResponseTime)}</span>
              <span className="text-sm text-muted-foreground">first response</span>
            </div>
            <div className="flex items-center gap-2 text-sm">
              <TrendingDown className="h-4 w-4 text-green-500" />
              <span className="text-green-600">-8%</span>
              <span className="text-muted-foreground">faster than last month</span>
            </div>
            <div className="space-y-2">
              <div className="flex justify-between text-xs">
                <span>Resolution Time</span>
                <span className="font-medium">{formatDuration(metrics.averageResolutionTime)}</span>
              </div>
              <div className="flex justify-between text-xs">
                <span>Target</span>
                <span className="text-muted-foreground">&lt;15 min</span>
              </div>
            </div>
          </CardContent>
        </Card>

        {/* CSAT Score */}
        <Card>
          <CardHeader className="pb-3">
            <div className="flex items-start justify-between">
              <CardTitle className="text-sm font-medium">CSAT Score</CardTitle>
              <Users className="h-4 w-4 text-muted-foreground" />
            </div>
          </CardHeader>
          <CardContent className="space-y-3">
            <div className="flex items-baseline gap-2">
              <span className="text-2xl font-bold">{metrics.customerSatisfactionScore}</span>
              <span className="text-sm text-muted-foreground">/ 5.0</span>
            </div>
            <div className="flex items-center gap-2 text-sm">
              <TrendingUp className="h-4 w-4 text-green-500" />
              <span className="text-green-600">+0.2</span>
              <span className="text-muted-foreground">vs last month</span>
            </div>
            <div className="flex gap-1">
              {[1, 2, 3, 4, 5].map((star) => (
                <svg
                  key={star}
                  className={`h-4 w-4 ${
                    star <= Math.round(metrics.customerSatisfactionScore)
                      ? 'fill-yellow-400 text-yellow-400'
                      : 'fill-gray-300 text-gray-300'
                  }`}
                  viewBox="0 0 20 20"
                >
                  <path d="M9.049 2.927c.3-.921 1.603-.921 1.902 0l1.07 3.292a1 1 0 00.95.69h3.462c.969 0 1.371 1.24.588 1.81l-2.8 2.034a1 1 0 00-.364 1.118l1.07 3.292c.3.921-.755 1.688-1.54 1.118l-2.8-2.034a1 1 0 00-1.175 0l-2.8 2.034c-.784.57-1.838-.197-1.539-1.118l1.07-3.292a1 1 0 00-.364-1.118L2.98 8.72c-.783-.57-.38-1.81.588-1.81h3.461a1 1 0 00.951-.69l1.07-3.292z" />
                </svg>
              ))}
            </div>
          </CardContent>
        </Card>
      </div>

      {/* Two Column Layout */}
      <div className="grid gap-6 md:grid-cols-2">
        {/* Recent Tickets */}
        <Card>
          <CardHeader>
            <div className="flex items-center justify-between">
              <div>
                <CardTitle>Recent Tickets</CardTitle>
                <CardDescription>Latest support requests</CardDescription>
              </div>
              <Link to="/tickets">
                <Button variant="outline" size="sm">View All</Button>
              </Link>
            </div>
          </CardHeader>
          <CardContent>
            <Table>
              <TableHeader>
                <TableRow>
                  <TableHead>Ticket</TableHead>
                  <TableHead>Status</TableHead>
                  <TableHead>Priority</TableHead>
                  <TableHead>SLA</TableHead>
                </TableRow>
              </TableHeader>
              <TableBody>
                {recentTickets.map((ticket) => {
                  const ChannelIcon = channelIcons[ticket.channel]
                  return (
                    <TableRow key={ticket.id}>
                      <TableCell>
                        <div className="flex items-start gap-2">
                          <ChannelIcon className="h-4 w-4 mt-0.5 text-muted-foreground" />
                          <div>
                            <p className="font-medium">{ticket.ticketNumber}</p>
                            <p className="text-xs text-muted-foreground line-clamp-1">{ticket.subject}</p>
                          </div>
                        </div>
                      </TableCell>
                      <TableCell>
                        <Badge
                          variant={
                            ticket.status === 'open' ? 'open' :
                            ticket.status === 'resolved' ? 'resolved' :
                            ticket.status === 'escalated' ? 'escalated' :
                            ticket.status === 'in_progress' ? 'in_progress' :
                            ticket.status === 'customer_reply' ? 'info' : 'secondary'
                          }
                        >
                          {ticket.status.replace('_', ' ')}
                        </Badge>
                      </TableCell>
                      <TableCell>
                        <Badge variant={ticket.priority === 'urgent' ? 'destructive' : ticket.priority === 'high' ? 'warning' : 'secondary'}>
                          {ticket.priority}
                        </Badge>
                      </TableCell>
                      <TableCell>
                        {ticket.slaBreached ? (
                          <Badge variant="destructive" className="gap-1">
                            <AlertTriangle className="h-3 w-3" />
                            Breached
                          </Badge>
                        ) : ticket.slaWarning ? (
                          <Badge variant="warning" className="gap-1">
                            <Clock className="h-3 w-3" />
                            Warning
                          </Badge>
                        ) : (
                          <Badge variant="success" className="gap-1">
                            <CheckCircle className="h-3 w-3" />
                            OK
                          </Badge>
                        )}
                      </TableCell>
                    </TableRow>
                  )
                })}
              </TableBody>
            </Table>
          </CardContent>
        </Card>

        {/* SLA Breaches */}
        <Card>
          <CardHeader>
            <div className="flex items-center justify-between">
              <div>
                <CardTitle>SLA Breaches</CardTitle>
                <CardDescription>Tickets at risk or breached</CardDescription>
              </div>
              <Link to="/sla">
                <Badge variant="destructive">{slaBreaches.length} Active</Badge>
              </Link>
            </div>
          </CardHeader>
          <CardContent>
            <div className="space-y-3">
              {slaBreaches.slice(0, 4).map((breach) => (
                <div
                  key={breach.id}
                  className="flex items-center gap-3 rounded-lg border p-3 hover:bg-slate-50 dark:hover:bg-slate-800"
                >
                  <div className={cn(
                    'h-2 w-2 rounded-full',
                    breach.status === 'open' ? 'bg-red-500' : 'bg-yellow-500'
                  )} />
                  <div className="flex-1 min-w-0">
                    <div className="flex items-center justify-between gap-2">
                      <p className="font-medium text-sm truncate">{breach.ticketNumber}</p>
                      <Badge variant="outline" className="shrink-0">
                        {breach.priority}
                      </Badge>
                    </div>
                    <div className="flex items-center gap-2 text-xs text-muted-foreground">
                      <span>{breach.slaType.replace('_', ' ')}</span>
                      <span>•</span>
                      <span>Overdue by {formatDuration(breach.overdueBy)}</span>
                    </div>
                  </div>
                </div>
              ))}
              {slaBreaches.length === 0 && (
                <div className="text-center py-6 text-muted-foreground">
                  <CheckCircle className="h-8 w-8 mx-auto mb-2 text-green-500" />
                  <p>No active SLA breaches</p>
                </div>
              )}
            </div>
          </CardContent>
        </Card>
      </div>

      {/* Country/Regional Performance */}
      <Card>
        <CardHeader>
          <div className="flex items-center justify-between">
            <div>
              <CardTitle>Regional Performance</CardTitle>
              <CardDescription>Support metrics by country</CardDescription>
            </div>
            <Link to="/analytics">
              <Button variant="outline" size="sm">View Analytics</Button>
            </Link>
          </div>
        </CardHeader>
        <CardContent>
          <Table>
            <TableHeader>
              <TableRow>
                <TableHead>Country</TableHead>
                <TableHead>Tickets</TableHead>
                <TableHead>Open</TableHead>
                <TableHead>Avg Response</TableHead>
                <TableHead>CSAT</TableHead>
                <TableHead>SLA</TableHead>
                <TableHead>Trend</TableHead>
              </TableRow>
            </TableHeader>
            <TableBody>
              {countryMetrics.map((country) => {
                const ticketProgress = (country.resolvedTickets / country.totalTickets) * 100
                return (
                  <TableRow key={country.country}>
                    <TableCell className="font-medium">{country.countryName}</TableCell>
                    <TableCell>{formatNumber(country.totalTickets)}</TableCell>
                    <TableCell>
                      <div className="flex items-center gap-2">
                        <span className="text-sm">{country.openTickets}</span>
                        <div className="h-1.5 w-16 overflow-hidden rounded-full bg-slate-200 dark:bg-slate-800">
                          <div
                            className={cn(
                              'h-full',
                              ticketProgress >= 95 ? 'bg-green-500' :
                              ticketProgress >= 85 ? 'bg-blue-500' :
                              'bg-yellow-500'
                            )}
                            style={{ width: `${ticketProgress}%` }}
                          />
                        </div>
                      </div>
                    </TableCell>
                    <TableCell>{formatDuration(country.averageResponseTime)}</TableCell>
                    <TableCell>
                      <div className="flex items-center gap-1">
                        <span>{country.satisfactionScore}</span>
                        {[1, 2, 3, 4, 5].map((star) => (
                          <svg
                            key={star}
                            className={`h-3 w-3 ${
                              star <= Math.round(country.satisfactionScore)
                                ? 'fill-yellow-400 text-yellow-400'
                                : 'fill-gray-300 text-gray-300'
                            }`}
                            viewBox="0 0 20 20"
                          >
                            <path d="M9.049 2.927c.3-.921 1.603-.921 1.902 0l1.07 3.292a1 1 0 00.95.69h3.462c.969 0 1.371 1.24.588 1.81l-2.8 2.034a1 1 0 00-.364 1.118l1.07 3.292c.3.921-.755 1.688-1.54 1.118l-2.8-2.034a1 1 0 00-1.175 0l-2.8 2.034c-.784.57-1.838-.197-1.539-1.118l1.07-3.292a1 1 0 00-.364-1.118L2.98 8.72c-.783-.57-.38-1.81.588-1.81h3.461a1 1 0 00.951-.69l1.07-3.292z" />
                          </svg>
                        ))}
                      </div>
                    </TableCell>
                    <TableCell>
                      <span className={cn(
                        'font-medium',
                        country.slaCompliance >= 95 ? 'text-green-600' :
                        country.slaCompliance >= 90 ? 'text-yellow-600' :
                        'text-red-600'
                      )}>
                        {country.slaCompliance}%
                      </span>
                    </TableCell>
                    <TableCell>
                      <span className="flex items-center gap-1 text-green-600">
                        <ArrowUpRight className="h-4 w-4" />
                        +{Math.floor(Math.random() * 5 + 2)}%
                      </span>
                    </TableCell>
                  </TableRow>
                )
              })}
            </TableBody>
          </Table>
        </CardContent>
      </Card>

      {/* Channel Breakdown */}
      <div className="grid gap-6 md:grid-cols-2 lg:grid-cols-4">
        {metrics.channelBreakdown.map((channel) => {
          const ChannelIcon = channelIcons[channel.channel]
          return (
            <Card key={channel.channel}>
              <CardContent className="p-6">
                <div className="flex items-center gap-3 mb-4">
                  <div className="rounded-lg bg-slate-100 dark:bg-slate-800 p-2">
                    <ChannelIcon className="h-5 w-5" />
                  </div>
                  <div>
                    <p className="text-sm font-medium capitalize">{channel.channel}</p>
                    <p className="text-xs text-muted-foreground">{channel.percentage}% of volume</p>
                  </div>
                </div>
                <div className="space-y-2">
                  <div className="flex justify-between text-sm">
                    <span>Tickets</span>
                    <span className="font-medium">{formatNumber(channel.count)}</span>
                  </div>
                  <div className="flex justify-between text-sm">
                    <span>Avg Response</span>
                    <span className="font-medium">{formatDuration(channel.averageResponseTime)}</span>
                  </div>
                  <div className="flex justify-between text-sm">
                    <span>Satisfaction</span>
                    <span className="font-medium">{channel.averageSatisfaction} ★</span>
                  </div>
                </div>
              </CardContent>
            </Card>
          )
        })}
      </div>
    </div>
  )
}
