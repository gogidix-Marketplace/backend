import { useState } from 'react'
import {
  BarChart3,
  TrendingUp,
  Users,
  MessageSquare,
  Phone,
  Mail,
  Download,
  Calendar,
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
import { cn, formatDuration, formatNumber } from '@shared/utils/cn'
import { mockSupportMetrics, mockAgents, mockCountryMetrics } from '@shared/data/mockData'

export default function AnalyticsPage() {
  const [timeRange, setTimeRange] = useState<'7d' | '30d' | '90d' | '1y'>('30d')
  const [selectedCountry, setSelectedCountry] = useState<string>('all')

  const channelIcons = {
    email: Mail,
    chat: MessageSquare,
    phone: Phone,
    web: MessageSquare,
    api: MessageSquare,
    social: MessageSquare,
  }

  // Mock trend data for charts
  const trendData = {
    labels: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun'],
    tickets: [2450, 2680, 2890, 3120, 3240, 3380],
    resolved: [2200, 2450, 2650, 2890, 3010, 3150],
    csat: [4.3, 4.4, 4.5, 4.5, 4.6, 4.6],
    sla: [92, 93, 94, 95, 95, 96],
  }

  const getTrendValue = (current: number, previous: number) => {
    const value = ((current - previous) / previous) * 100
    return {
      value: Math.abs(value).toFixed(1),
      isPositive: value >= 0,
      trend: value >= 0 ? 'up' : 'down',
    }
  }

  const ticketTrend = getTrendValue(trendData.tickets[5], trendData.tickets[4])
  const csatTrend = getTrendValue(trendData.csat[5], trendData.csat[4])

  return (
    <div className="space-y-6">
      {/* Page Header */}
      <div className="flex items-center justify-between">
        <div className="page-header">
          <h1 className="page-title">Analytics</h1>
          <p className="page-description">
            Support performance metrics and insights
          </p>
        </div>
        <div className="flex gap-2">
          <Select value={timeRange} onValueChange={(v: any) => setTimeRange(v)}>
            <SelectTrigger className="w-[140px]">
              <Calendar className="h-4 w-4 mr-2" />
              <SelectValue />
            </SelectTrigger>
            <SelectContent>
              <SelectItem value="7d">Last 7 days</SelectItem>
              <SelectItem value="30d">Last 30 days</SelectItem>
              <SelectItem value="90d">Last 90 days</SelectItem>
              <SelectItem value="1y">Last year</SelectItem>
            </SelectContent>
          </Select>
          <Button variant="outline" className="gap-2">
            <Download className="h-4 w-4" />
            Export
          </Button>
        </div>
      </div>

      {/* KPI Cards */}
      <div className="metric-grid">
        <Card>
          <CardHeader className="pb-3">
            <CardTitle className="text-sm font-medium">Total Tickets</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="flex items-baseline justify-between">
              <span className="text-2xl font-bold">{formatNumber(mockSupportMetrics.totalTickets)}</span>
              <Badge variant={ticketTrend.trend === 'up' ? 'success' : 'destructive'}>
                +{ticketTrend.value}%
              </Badge>
            </div>
            <p className="text-xs text-muted-foreground mt-2">
              {formatNumber(mockSupportMetrics.resolvedTickets)} resolved
            </p>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="pb-3">
            <CardTitle className="text-sm font-medium">CSAT Score</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="flex items-baseline justify-between">
              <span className="text-2xl font-bold">{mockSupportMetrics.customerSatisfactionScore}</span>
              <Badge variant={csatTrend.trend === 'up' ? 'success' : 'destructive'}>
                +{csatTrend.value}
              </Badge>
            </div>
            <p className="text-xs text-muted-foreground mt-2">
              Out of 5.0
            </p>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="pb-3">
            <CardTitle className="text-sm font-medium">Avg Response Time</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="flex items-baseline justify-between">
              <span className="text-2xl font-bold">{formatDuration(mockSupportMetrics.averageFirstResponseTime)}</span>
              <Badge variant="success">-12%</Badge>
            </div>
            <p className="text-xs text-muted-foreground mt-2">
              First response
            </p>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="pb-3">
            <CardTitle className="text-sm font-medium">SLA Compliance</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="flex items-baseline justify-between">
              <span className="text-2xl font-bold">96.2%</span>
              <Badge variant="success">+2.1%</Badge>
            </div>
            <p className="text-xs text-muted-foreground mt-2">
              {mockSupportMetrics.slaBreaches} breaches
            </p>
          </CardContent>
        </Card>
      </div>

      {/* Channel Performance */}
      <div className="grid gap-6 md:grid-cols-2">
        <Card className="col-span-2">
          <CardHeader>
            <CardTitle>Channel Performance</CardTitle>
            <CardDescription>Ticket volume and satisfaction by channel</CardDescription>
          </CardHeader>
          <CardContent>
            <div className="grid gap-4 md:grid-cols-2 lg:grid-cols-4">
              {mockSupportMetrics.channelBreakdown.map((channel) => {
                const ChannelIcon = channelIcons[channel.channel]
                return (
                  <div
                    key={channel.channel}
                    className="rounded-lg border p-4 hover:bg-slate-50 dark:hover:bg-slate-800 transition-colors"
                  >
                    <div className="flex items-center gap-3 mb-4">
                      <div className="rounded-lg bg-[#1976D2]/10 p-2">
                        <ChannelIcon className="h-5 w-5 text-[#1976D2]" />
                      </div>
                      <div>
                        <p className="font-medium capitalize">{channel.channel}</p>
                        <p className="text-xs text-muted-foreground">{channel.percentage}% of volume</p>
                      </div>
                    </div>
                    <div className="space-y-2 text-sm">
                      <div className="flex justify-between">
                        <span className="text-muted-foreground">Tickets</span>
                        <span className="font-medium">{formatNumber(channel.count)}</span>
                      </div>
                      <div className="flex justify-between">
                        <span className="text-muted-foreground">Response Time</span>
                        <span className="font-medium">{formatDuration(channel.averageResponseTime)}</span>
                      </div>
                      <div className="flex justify-between">
                        <span className="text-muted-foreground">Satisfaction</span>
                        <span className="font-medium">{channel.averageSatisfaction} ★</span>
                      </div>
                    </div>
                  </div>
                )
              })}
            </div>
          </CardContent>
        </Card>
      </div>

      <div className="grid gap-6 md:grid-cols-2">
        {/* Agent Performance */}
        <Card>
          <CardHeader>
            <CardTitle>Agent Performance</CardTitle>
            <CardDescription>Top performing agents this period</CardDescription>
          </CardHeader>
          <CardContent>
            <Table>
              <TableHeader>
                <TableRow>
                  <TableHead>Agent</TableHead>
                  <TableHead className="text-right">Tickets</TableHead>
                  <TableHead className="text-right">Response</TableHead>
                  <TableHead className="text-right">CSAT</TableHead>
                  <TableHead className="text-right">SLA</TableHead>
                </TableRow>
              </TableHeader>
              <TableBody>
                {mockAgents.slice(0, 5).map((agent) => (
                  <TableRow key={agent.id}>
                    <TableCell>
                      <div className="flex items-center gap-2">
                        <div className="h-8 w-8 rounded-full bg-[#1976D2] text-white flex items-center justify-center text-xs">
                          {agent.firstName[0]}{agent.lastName[0]}
                        </div>
                        <span className="font-medium">{agent.firstName} {agent.lastName}</span>
                      </div>
                    </TableCell>
                    <TableCell className="text-right">{agent.metrics.ticketsThisMonth}</TableCell>
                    <TableCell className="text-right">{formatDuration(agent.metrics.averageFirstResponseTime)}</TableCell>
                    <TableCell className="text-right">{agent.metrics.customerSatisfactionScore}</TableCell>
                    <TableCell className="text-right">
                      <Badge variant={agent.metrics.slaComplianceRate >= 95 ? 'success' : 'warning'}>
                        {agent.metrics.slaComplianceRate}%
                      </Badge>
                    </TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          </CardContent>
        </Card>

        {/* Country Performance */}
        <Card>
          <CardHeader>
            <div className="flex items-center justify-between">
              <div>
                <CardTitle>Regional Performance</CardTitle>
                <CardDescription>Metrics by country</CardDescription>
              </div>
              <Select value={selectedCountry} onValueChange={setSelectedCountry}>
                <SelectTrigger className="w-[140px]">
                  <SelectValue placeholder="All Countries" />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value="all">All Countries</SelectItem>
                  {mockCountryMetrics.map((c) => (
                    <SelectItem key={c.country} value={c.country}>
                      {c.countryName}
                    </SelectItem>
                  ))}
                </SelectContent>
              </Select>
            </div>
          </CardHeader>
          <CardContent>
            <div className="space-y-4">
              {mockCountryMetrics
                .filter(c => selectedCountry === 'all' || c.country === selectedCountry)
                .map((country) => (
                  <div key={country.country} className="space-y-2">
                    <div className="flex items-center justify-between">
                      <span className="font-medium">{country.countryName}</span>
                      <div className="flex items-center gap-3 text-sm">
                        <span className="text-muted-foreground">{formatNumber(country.totalTickets)} tickets</span>
                        <span className="font-medium">{country.satisfactionScore} ★</span>
                        <Badge variant={country.slaCompliance >= 95 ? 'success' : 'warning'}>
                          {country.slaCompliance}% SLA
                        </Badge>
                      </div>
                    </div>
                    <div className="h-2 w-full overflow-hidden rounded-full bg-slate-200 dark:bg-slate-800">
                      <div
                        className={cn(
                          'h-full transition-all',
                          country.slaCompliance >= 95 ? 'bg-green-500' :
                          country.slaCompliance >= 90 ? 'bg-yellow-500' :
                          'bg-red-500'
                        )}
                        style={{ width: `${country.slaCompliance}%` }}
                      />
                    </div>
                  </div>
                ))}
            </div>
          </CardContent>
        </Card>
      </div>

      {/* Key Metrics Trends */}
      <Card>
        <CardHeader>
          <CardTitle>Monthly Trends</CardTitle>
          <CardDescription>Key metrics over the last 6 months</CardDescription>
        </CardHeader>
        <CardContent>
          <div className="space-y-6">
            {/* Tickets Trend */}
            <div>
              <div className="flex items-center justify-between mb-2">
                <span className="text-sm font-medium">Tickets</span>
                <span className="text-sm text-muted-foreground">
                  {formatNumber(trendData.tickets[5])} this month
                </span>
              </div>
              <div className="flex gap-1 h-20 items-end">
                {trendData.tickets.map((value, i) => {
                  const max = Math.max(...trendData.tickets)
                  const height = (value / max) * 100
                  return (
                    <div
                      key={i}
                      className="flex-1 bg-[#1976D2]/20 hover:bg-[#1976D2]/40 rounded-t transition-colors relative group"
                      style={{ height: `${height}%` }}
                    >
                      <span className="absolute -bottom-6 left-1/2 -translate-x-1/2 text-xs text-muted-foreground opacity-0 group-hover:opacity-100 whitespace-nowrap">
                        {formatNumber(value)}
                      </span>
                    </div>
                  )
                })}
              </div>
              <div className="flex justify-between mt-6 text-xs text-muted-foreground">
                {trendData.labels.map((label) => (
                  <span key={label}>{label}</span>
                ))}
              </div>
            </div>

            {/* CSAT Trend */}
            <div>
              <div className="flex items-center justify-between mb-2">
                <span className="text-sm font-medium">CSAT Score</span>
                <span className="text-sm text-muted-foreground">
                  {trendData.csat[5]} / 5.0
                </span>
              </div>
              <div className="flex gap-1 h-16 items-end">
                {trendData.csat.map((value, i) => {
                  const height = (value / 5) * 100
                  return (
                    <div
                      key={i}
                      className="flex-1 bg-green-500/20 hover:bg-green-500/40 rounded-t transition-colors"
                      style={{ height: `${height}%` }}
                    />
                  )
                })}
              </div>
            </div>

            {/* SLA Trend */}
            <div>
              <div className="flex items-center justify-between mb-2">
                <span className="text-sm font-medium">SLA Compliance</span>
                <span className="text-sm text-muted-foreground">
                  {trendData.sla[5]}%
                </span>
              </div>
              <div className="flex gap-1 h-16 items-end">
                {trendData.sla.map((value, i) => {
                  const height = value
                  return (
                    <div
                      key={i}
                      className={cn(
                        'flex-1 rounded-t transition-colors',
                        value >= 95 ? 'bg-green-500/20 hover:bg-green-500/40' :
                        value >= 90 ? 'bg-yellow-500/20 hover:bg-yellow-500/40' :
                        'bg-red-500/20 hover:bg-red-500/40'
                      )}
                      style={{ height: `${height}%` }}
                    />
                  )
                })}
              </div>
            </div>
          </div>
        </CardContent>
      </Card>
    </div>
  )
}
