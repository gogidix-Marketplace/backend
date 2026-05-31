import {
  Users,
  UserPlus,
  UserMinus,
  Briefcase,
  TrendingUp,
  TrendingDown,
  CheckCircle,
  Clock,
  Calendar,
  DollarSign,
  Award,
  BookOpen,
  Heart,
  Globe,
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
import { cn, formatCurrency, formatNumber, formatPercentage } from '@shared/utils/cn'
import {
  hrMetrics,
  departmentBreakdown,
  recentActivities,
  countries,
  approvalRequests,
} from '@shared/data/mockData'

function MetricCard({
  title,
  value,
  change,
  trend,
  icon: Icon,
  variant = 'default',
  suffix = '',
}: {
  title: string
  value: string | number
  change?: number
  trend?: 'up' | 'down' | 'neutral'
  icon: React.ComponentType<{ className?: string }>
  variant?: 'default' | 'hr' | 'teal'
  suffix?: string
}) {
  return (
    <Card>
      <CardHeader className="pb-3">
        <div className="flex items-center justify-between">
          <CardTitle className="text-sm font-medium text-muted-foreground">
            {title}
          </CardTitle>
          <div
            className={cn(
              'rounded-lg p-2',
              variant === 'hr' && 'bg-blue-100 text-blue-600 dark:bg-blue-900/20',
              variant === 'teal' && 'bg-teal-100 text-teal-600 dark:bg-teal-900/20',
              variant === 'default' && 'bg-slate-100 text-slate-600 dark:bg-slate-800'
            )}
          >
            <Icon className="h-4 w-4" />
          </div>
        </div>
      </CardHeader>
      <CardContent>
        <div className="flex items-baseline gap-2">
          <span className="text-2xl font-bold">
            {typeof value === 'number' ? formatNumber(value) : value}
          </span>
          {suffix && <span className="text-sm text-muted-foreground">{suffix}</span>}
        </div>
        {change !== undefined && (
          <div className="flex items-center gap-1 mt-1 text-sm">
            {trend === 'up' ? (
              <TrendingUp className="h-3 w-3 text-green-600" />
            ) : trend === 'down' ? (
              <TrendingDown className="h-3 w-3 text-red-600" />
            ) : null}
            <span
              className={cn(
                change > 0 ? 'text-green-600' : change < 0 ? 'text-red-600' : 'text-muted-foreground'
              )}
            >
              {formatPercentage(change)}
            </span>
            <span className="text-muted-foreground">vs last month</span>
          </div>
        )}
      </CardContent>
    </Card>
  )
}

export default function OverviewPage() {
  return (
    <div className="space-y-6">
      {/* Page Header */}
      <div className="flex flex-col md:flex-row md:items-center md:justify-between gap-4">
        <div className="page-header">
          <h1 className="page-title">HR Dashboard</h1>
          <p className="page-description">
            Workforce overview and key metrics
          </p>
        </div>
        <div className="flex items-center gap-2">
          <Button variant="outline" size="sm">
            <Globe className="mr-2 h-4 w-4" />
            Global View
          </Button>
          <Button variant="hr" size="sm">
            <Calendar className="mr-2 h-4 w-4" />
            Run Payroll
          </Button>
        </div>
      </div>

      {/* Primary Metrics */}
      <div className="metric-grid">
        <MetricCard
          title="Total Headcount"
          value={hrMetrics.headcount.total}
          change={hrMetrics.headcount.changePercent}
          trend={hrMetrics.headcount.change > 0 ? 'up' : 'down'}
          icon={Users}
          variant="hr"
        />
        <MetricCard
          title="New Hires"
          value={hrMetrics.growth.newHires}
          icon={UserPlus}
          variant="teal"
        />
        <MetricCard
          title="Departures"
          value={hrMetrics.growth.departures}
          icon={UserMinus}
        />
        <MetricCard
          title="Open Positions"
          value={hrMetrics.openPositions.total}
          change={12}
          trend="up"
          icon={Briefcase}
          variant="default"
        />
      </div>

      {/* Secondary Metrics */}
      <div className="grid gap-4 md:grid-cols-2 lg:grid-cols-4">
        <MetricCard
          title="Turnover Rate"
          value={hrMetrics.turnover.rate}
          suffix="%"
          change={-0.3}
          trend="down"
          icon={TrendingUp}
        />
        <MetricCard
          title="Time to Fill"
          value={hrMetrics.openPositions.timeToFill}
          suffix=" days"
          change={-3}
          trend="up"
          icon={Clock}
        />
        <MetricCard
          title="Engagement Score"
          value={hrMetrics.engagement.score}
          change={2}
          trend="up"
          icon={Heart}
          variant="teal"
        />
        <MetricCard
          title="Training Completion"
          value="85"
          suffix="%"
          change={5}
          trend="up"
          icon={BookOpen}
        />
      </div>

      {/* Main Content Grid */}
      <div className="grid gap-6 lg:grid-cols-3">
        {/* Department Breakdown */}
        <Card className="lg:col-span-2">
          <CardHeader>
            <div className="flex items-center justify-between">
              <div>
                <CardTitle>Headcount by Department</CardTitle>
                <CardDescription>Employee distribution across departments</CardDescription>
              </div>
              <Button variant="outline" size="sm">
                View All
              </Button>
            </div>
          </CardHeader>
          <CardContent>
            <Table>
              <TableHeader>
                <TableRow>
                  <TableHead>Department</TableHead>
                  <TableHead className="text-right">Employees</TableHead>
                  <TableHead className="text-right">% of Total</TableHead>
                  <TableHead className="text-right">Budget</TableHead>
                </TableRow>
              </TableHeader>
              <TableBody>
                {departmentBreakdown.map((dept) => (
                  <TableRow key={dept.department}>
                    <TableCell className="font-medium">{dept.department}</TableCell>
                    <TableCell className="text-right">{dept.count}</TableCell>
                    <TableCell className="text-right">
                      {((dept.count / hrMetrics.headcount.total) * 100).toFixed(1)}%
                    </TableCell>
                    <TableCell className="text-right">
                      {formatCurrency(dept.budget, 'USD')}
                    </TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          </CardContent>
        </Card>

        {/* Regional Breakdown */}
        <Card>
          <CardHeader>
            <CardTitle>Headcount by Region</CardTitle>
            <CardDescription>Employee distribution by country</CardDescription>
          </CardHeader>
          <CardContent className="space-y-4">
            {countries
              .filter((c) => c.code !== 'ALL' && c.code !== 'OTHER')
              .map((country) => {
                const percentage = (country.headcount / hrMetrics.headcount.total) * 100
                return (
                  <div key={country.code} className="space-y-2">
                    <div className="flex items-center justify-between text-sm">
                      <div className="flex items-center gap-2">
                        <span className="text-xl">{country.flag}</span>
                        <span className="font-medium">{country.name}</span>
                      </div>
                      <span className="text-muted-foreground">
                        {country.headcount} ({percentage.toFixed(1)}%)
                      </span>
                    </div>
                    <div className="h-2 w-full overflow-hidden rounded-full bg-slate-200 dark:bg-slate-800">
                      <div
                        className="h-full bg-[#2563EB]"
                        style={{ width: `${percentage}%` }}
                      />
                    </div>
                  </div>
                )
              })}
          </CardContent>
        </Card>
      </div>

      {/* Bottom Grid */}
      <div className="grid gap-6 lg:grid-cols-2">
        {/* Pending Approvals */}
        <Card>
          <CardHeader>
            <div className="flex items-center justify-between">
              <div>
                <CardTitle>Pending Approvals</CardTitle>
                <CardDescription>Actions requiring your attention</CardDescription>
              </div>
              <Badge variant="destructive">{approvalRequests.length}</Badge>
            </div>
          </CardHeader>
          <CardContent>
            <div className="space-y-3">
              {approvalRequests.map((request) => (
                <div
                  key={request.id}
                  className="flex items-start gap-3 rounded-lg border p-3 hover:bg-slate-50 dark:hover:bg-slate-800 transition-colors cursor-pointer"
                >
                  <div
                    className={cn(
                      'mt-0.5 h-2 w-2 rounded-full',
                      request.priority === 'urgent' && 'bg-red-500',
                      request.priority === 'high' && 'bg-orange-500',
                      request.priority === 'medium' && 'bg-yellow-500',
                      request.priority === 'low' && 'bg-blue-500'
                    )}
                  />
                  <div className="flex-1 min-w-0">
                    <p className="font-medium truncate">{request.title}</p>
                    <p className="text-sm text-muted-foreground truncate">
                      {request.description}
                    </p>
                    <p className="text-xs text-muted-foreground mt-1">
                      {request.requestedBy} • {new Date(request.requestedAt).toLocaleDateString()}
                    </p>
                  </div>
                  <Badge
                    variant={request.priority === 'urgent' ? 'destructive' : 'outline'}
                    className="shrink-0"
                  >
                    {request.priority}
                  </Badge>
                </div>
              ))}
            </div>
            <Button variant="outline" className="w-full mt-4" size="sm">
              View All Approvals
            </Button>
          </CardContent>
        </Card>

        {/* Recent Activity */}
        <Card>
          <CardHeader>
            <CardTitle>Recent Activity</CardTitle>
            <CardDescription>Latest HR updates across the organization</CardDescription>
          </CardHeader>
          <CardContent>
            <div className="space-y-4">
              {recentActivities.map((activity) => (
                <div key={activity.id} className="flex gap-3">
                  <div
                    className={cn(
                      'mt-0.5 h-2 w-2 rounded-full shrink-0',
                      activity.type === 'hire' && 'bg-green-500',
                      activity.type === 'leave' && 'bg-blue-500',
                      activity.type === 'review' && 'bg-purple-500',
                      activity.type === 'training' && 'bg-teal-500'
                    )}
                  />
                  <div className="flex-1 min-w-0">
                    <p className="text-sm">{activity.message}</p>
                    <p className="text-xs text-muted-foreground mt-0.5">{activity.time}</p>
                  </div>
                </div>
              ))}
            </div>
          </CardContent>
        </Card>
      </div>

      {/* Diversity Metrics */}
      <Card>
        <CardHeader>
          <CardTitle>Diversity & Inclusion Metrics</CardTitle>
          <CardDescription>Workforce diversity statistics</CardDescription>
        </CardHeader>
        <CardContent>
          <div className="grid gap-6 md:grid-cols-3">
            {/* Gender Distribution */}
            <div className="space-y-3">
              <h4 className="text-sm font-medium">Gender Distribution</h4>
              <div className="space-y-2">
                <div className="flex items-center justify-between text-sm">
                  <span>Male</span>
                  <span className="font-medium">{hrMetrics.diversity.gender.male}%</span>
                </div>
                <div className="h-2 w-full overflow-hidden rounded-full bg-slate-200 dark:bg-slate-800">
                  <div
                    className="h-full bg-blue-500"
                    style={{ width: `${hrMetrics.diversity.gender.male}%` }}
                  />
                </div>
                <div className="flex items-center justify-between text-sm">
                  <span>Female</span>
                  <span className="font-medium">{hrMetrics.diversity.gender.female}%</span>
                </div>
                <div className="h-2 w-full overflow-hidden rounded-full bg-slate-200 dark:bg-slate-800">
                  <div
                    className="h-full bg-pink-500"
                    style={{ width: `${hrMetrics.diversity.gender.female}%` }}
                  />
                </div>
                <div className="flex items-center justify-between text-sm">
                  <span>Other</span>
                  <span className="font-medium">{hrMetrics.diversity.gender.other}%</span>
                </div>
                <div className="h-2 w-full overflow-hidden rounded-full bg-slate-200 dark:bg-slate-800">
                  <div
                    className="h-full bg-purple-500"
                    style={{ width: `${hrMetrics.diversity.gender.other}%` }}
                  />
                </div>
              </div>
            </div>

            {/* Age Groups */}
            <div className="space-y-3">
              <h4 className="text-sm font-medium">Age Groups</h4>
              <div className="space-y-2">
                <div className="flex items-center justify-between text-sm">
                  <span>Under 30</span>
                  <span className="font-medium">{hrMetrics.diversity.ageGroups.under30}%</span>
                </div>
                <div className="flex items-center justify-between text-sm">
                  <span>30-40</span>
                  <span className="font-medium">{hrMetrics.diversity.ageGroups.thirtyTo40}%</span>
                </div>
                <div className="flex items-center justify-between text-sm">
                  <span>40-50</span>
                  <span className="font-medium">{hrMetrics.diversity.ageGroups.fortyTo50}%</span>
                </div>
                <div className="flex items-center justify-between text-sm">
                  <span>Over 50</span>
                  <span className="font-medium">{hrMetrics.diversity.ageGroups.over50}%</span>
                </div>
              </div>
            </div>

            {/* Regional Distribution */}
            <div className="space-y-3">
              <h4 className="text-sm font-medium">Regional Distribution</h4>
              <div className="space-y-2">
                {Object.entries(hrMetrics.diversity.regions).map(([region, count]) => (
                  <div key={region} className="flex items-center justify-between text-sm">
                    <span>{region}</span>
                    <span className="font-medium">{count}</span>
                  </div>
                ))}
              </div>
            </div>
          </div>
        </CardContent>
      </Card>
    </div>
  )
}
