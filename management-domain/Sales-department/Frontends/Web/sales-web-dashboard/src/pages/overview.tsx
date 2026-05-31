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
  TrendingUp,
  TrendingDown,
  DollarSign,
  Briefcase,
  Users,
  Target,
  ArrowUpRight,
  ArrowDownRight,
  Calendar,
} from 'lucide-react'
import {
  BarChart,
  Bar,
  LineChart,
  Line,
  PieChart,
  Pie,
  Cell,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  Legend,
  ResponsiveContainer,
} from 'recharts'
import { formatCurrency, formatNumber, formatPercentage, getCountryFlag } from '@shared/utils/cn'
import { mockSalesMetrics, mockRegionalMetrics, mockPipelineByStage, mockRevenueChartData, mockTopPerformers } from '@shared/data/mockData'
import { useAuthStore } from '@shared/stores/authStore'

export default function OverviewPage() {
  const { user, selectedCountry } = useAuthStore()

  const filteredRegionalMetrics = selectedCountry
    ? mockRegionalMetrics.filter(m => m.countryCode === selectedCountry)
    : mockRegionalMetrics

  const countryName = selectedCountry
    ? mockRegionalMetrics.find(m => m.countryCode === selectedCountry)?.country
    : 'All Countries'

  const COLORS = ['#1E40AF', '#10B981', '#F59E0B', '#EF4444', '#8B5CF6']

  const StatCard = ({
    title,
    value,
    target,
    change,
    icon: Icon,
    trend,
    prefix = '',
    suffix = '',
  }: {
    title: string
    value: number
    target?: number
    change?: number
    icon: React.ComponentType<{ className?: string }>
    trend: 'up' | 'down' | 'neutral'
    prefix?: string
    suffix?: string
  }) => (
    <Card>
      <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
        <CardTitle className="text-sm font-medium text-muted-foreground">
          {title}
        </CardTitle>
        <Icon className="h-4 w-4 text-muted-foreground" />
      </CardHeader>
      <CardContent>
        <div className="text-2xl font-bold">
          {prefix}{formatNumber(value)}{suffix}
        </div>
        {target && (
          <div className="mt-2">
            <div className="flex items-center justify-between text-xs text-muted-foreground mb-1">
              <span>Target: {formatNumber(target)}</span>
              <span>{Math.round((value / target) * 100)}%</span>
            </div>
            <div className="h-2 bg-gray-200 rounded-full overflow-hidden">
              <div
                className="h-full bg-blue-600 transition-all duration-300"
                style={{ width: `${Math.min((value / target) * 100, 100)}%` }}
              />
            </div>
          </div>
        )}
        {change !== undefined && (
          <p className={`text-xs mt-2 flex items-center gap-1 ${
            trend === 'up' ? 'text-green-600' : trend === 'down' ? 'text-red-600' : 'text-gray-600'
          }`}>
            {trend === 'up' ? <ArrowUpRight className="h-3 w-3" /> : trend === 'down' ? <ArrowDownRight className="h-3 w-3" /> : null}
            {formatPercentage(change)} vs last month
          </p>
        )}
      </CardContent>
    </Card>
  )

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
        <div>
          <h1 className="text-3xl font-bold tracking-tight">
            {selectedCountry ? `${getCountryFlag(selectedCountry)} ${countryName}` : 'Global Sales Overview'}
          </h1>
          <p className="text-muted-foreground mt-1">
            Track your sales performance and metrics
          </p>
        </div>
        <div className="flex items-center gap-2">
          <Select defaultValue="this-month">
            <SelectTrigger className="w-[180px]">
              <Calendar className="h-4 w-4 mr-2" />
              <SelectValue />
            </SelectTrigger>
            <SelectContent>
              <SelectItem value="this-month">This Month</SelectItem>
              <SelectItem value="last-month">Last Month</SelectItem>
              <SelectItem value="this-quarter">This Quarter</SelectItem>
              <SelectItem value="this-year">This Year</SelectItem>
            </SelectContent>
          </Select>
          <Button variant="outline" size="icon">
            <Calendar className="h-4 w-4" />
          </Button>
        </div>
      </div>

      {/* Key Metrics */}
      <div className="grid gap-4 md:grid-cols-2 lg:grid-cols-4">
        <StatCard
          title="Total Revenue"
          value={mockSalesMetrics.revenue.current}
          target={mockSalesMetrics.revenue.target}
          change={mockSalesMetrics.revenue.growth}
          icon={DollarSign}
          trend="up"
          prefix="$"
        />
        <StatCard
          title="Pipeline Value"
          value={mockSalesMetrics.pipeline.total}
          change={12}
          icon={Briefcase}
          trend="up"
          prefix="$"
        />
        <StatCard
          title="Deals Closed"
          value={mockSalesMetrics.deals.closed}
          change={8}
          icon={Target}
          trend="up"
        />
        <StatCard
          title="Active Leads"
          value={mockSalesMetrics.pipeline.count}
          change={-3}
          icon={Users}
          trend="down"
        />
      </div>

      {/* Charts Row */}
      <div className="grid gap-4 md:grid-cols-2 lg:grid-cols-7">
        {/* Revenue Chart */}
        <Card className="col-span-4">
          <CardHeader>
            <CardTitle>Revenue Trend</CardTitle>
            <CardDescription>Monthly revenue vs target (Last 6 months)</CardDescription>
          </CardHeader>
          <CardContent>
            <ResponsiveContainer width="100%" height={300}>
              <BarChart data={mockRevenueChartData}>
                <CartesianGrid strokeDasharray="3 3" />
                <XAxis dataKey="name" />
                <YAxis tickFormatter={(value) => `$${(value / 1000000).toFixed(1)}M`} />
                <Tooltip
                  formatter={(value: number) => [formatCurrency(value), 'Revenue']}
                  labelFormatter={(label) => `Month: ${label}`}
                />
                <Legend />
                <Bar dataKey="value" fill="#1E40AF" name="Revenue" radius={[8, 8, 0, 0]} />
                <Bar dataKey="target" fill="#E5E7EB" name="Target" radius={[8, 8, 0, 0]} />
              </BarChart>
            </ResponsiveContainer>
          </CardContent>
        </Card>

        {/* Pipeline by Stage */}
        <Card className="col-span-3">
          <CardHeader>
            <CardTitle>Pipeline by Stage</CardTitle>
            <CardDescription>Distribution across deal stages</CardDescription>
          </CardHeader>
          <CardContent>
            <ResponsiveContainer width="100%" height={300}>
              <PieChart>
                <Pie
                  data={mockPipelineByStage}
                  cx="50%"
                  cy="50%"
                  labelLine={false}
                  label={({ name, percent }) => `${name}: ${(percent * 100).toFixed(0)}%`}
                  outerRadius={80}
                  fill="#8884d8"
                  dataKey="value"
                >
                  {mockPipelineByStage.map((entry, index) => (
                    <Cell key={`cell-${index}`} fill={entry.color} />
                  ))}
                </Pie>
                <Tooltip formatter={(value: number) => formatCurrency(value)} />
              </PieChart>
            </ResponsiveContainer>
            <div className="mt-4 space-y-2">
              {mockPipelineByStage.map((stage) => (
                <div key={stage.name} className="flex items-center justify-between text-sm">
                  <div className="flex items-center gap-2">
                    <div
                      className="h-3 w-3 rounded-full"
                      style={{ backgroundColor: stage.color }}
                    />
                    <span>{stage.name}</span>
                  </div>
                  <span className="font-medium">{formatCurrency(stage.value)}</span>
                </div>
              ))}
            </div>
          </CardContent>
        </Card>
      </div>

      {/* Regional Performance */}
      <Card>
        <CardHeader>
          <CardTitle>Regional Performance</CardTitle>
          <CardDescription>Revenue and quota attainment by country</CardDescription>
        </CardHeader>
        <CardContent>
          <div className="space-y-4">
            {filteredRegionalMetrics.map((region) => (
              <div key={region.countryCode} className="flex items-center gap-4">
                <div className="text-2xl">{getCountryFlag(region.countryCode)}</div>
                <div className="flex-1">
                  <div className="flex items-center justify-between mb-1">
                    <span className="font-medium">{region.country}</span>
                    <span className="text-sm text-muted-foreground">
                      {formatCurrency(region.revenue)} / {formatCurrency(region.target)}
                    </span>
                  </div>
                  <div className="h-2 bg-gray-200 rounded-full overflow-hidden">
                    <div
                      className={`h-full transition-all duration-300 ${
                        region.attainment >= 100 ? 'bg-green-500' :
                        region.attainment >= 90 ? 'bg-blue-500' :
                        region.attainment >= 80 ? 'bg-yellow-500' : 'bg-red-500'
                      }`}
                      style={{ width: `${Math.min(region.attainment, 100)}%` }}
                    />
                  </div>
                </div>
                <div className="text-right min-w-[100px]">
                  <div className="font-bold">{region.attainment}%</div>
                  <Badge
                    variant={region.growth > 15 ? 'success' : region.growth > 0 ? 'info' : 'destructive'}
                    className="text-xs"
                  >
                    {region.growth > 0 ? '+' : ''}{region.growth}%
                  </Badge>
                </div>
              </div>
            ))}
          </div>
        </CardContent>
      </Card>

      {/* Top Performers and Forecast */}
      <div className="grid gap-4 md:grid-cols-2">
        {/* Top Performers */}
        <Card>
          <CardHeader>
            <CardTitle>Top Performers</CardTitle>
            <CardDescription>This month's leading sales representatives</CardDescription>
          </CardHeader>
          <CardContent>
            <div className="space-y-4">
              {mockTopPerformers.map((performer) => (
                <div key={performer.rank} className="flex items-center gap-4">
                  <div className={`flex h-8 w-8 items-center justify-center rounded-full text-sm font-bold ${
                    performer.rank === 1 ? 'bg-yellow-100 text-yellow-800' :
                    performer.rank === 2 ? 'bg-gray-100 text-gray-800' :
                    performer.rank === 3 ? 'bg-orange-100 text-orange-800' :
                    'bg-gray-50 text-gray-600'
                  }`}>
                    {performer.rank}
                  </div>
                  <div className="flex-1">
                    <div className="font-medium">{performer.name}</div>
                    <div className="text-sm text-muted-foreground">{getCountryFlag(performer.country)}</div>
                  </div>
                  <div className="text-right">
                    <div className="font-medium">{formatCurrency(performer.revenue)}</div>
                    <div className={`text-sm ${
                      performer.attainment >= 100 ? 'text-green-600' :
                      performer.attainment >= 90 ? 'text-blue-600' : 'text-orange-600'
                    }`}>
                      {performer.attainment}% of quota
                    </div>
                  </div>
                </div>
              ))}
            </div>
          </CardContent>
        </Card>

        {/* Forecast */}
        <Card>
          <CardHeader>
            <CardTitle>Forecast Summary</CardTitle>
            <CardDescription>Predicted revenue for this quarter</CardDescription>
          </CardHeader>
          <CardContent className="space-y-4">
            <div className="space-y-3">
              <div className="flex items-center justify-between">
                <span className="text-muted-foreground">Commit (75% confidence)</span>
                <span className="font-bold">{formatCurrency(mockSalesMetrics.forecast.quarter)}</span>
              </div>
              <div className="flex items-center justify-between">
                <span className="text-muted-foreground">Best Case</span>
                <span className="font-bold text-green-600">{formatCurrency(mockSalesMetrics.forecast.quarter * 1.15)}</span>
              </div>
              <div className="flex items-center justify-between">
                <span className="text-muted-foreground">Worst Case</span>
                <span className="font-bold text-orange-600">{formatCurrency(mockSalesMetrics.forecast.quarter * 0.85)}</span>
              </div>
            </div>

            <div className="pt-4 border-t">
              <div className="flex items-center justify-between mb-2">
                <span className="text-sm font-medium">Conversion Rate</span>
                <span className="text-sm">{mockSalesMetrics.conversion.opportunityToDeal}%</span>
              </div>
              <div className="flex items-center justify-between mb-2">
                <span className="text-sm font-medium">Avg Deal Size</span>
                <span className="text-sm">{formatCurrency(mockSalesMetrics.pipeline.total / mockSalesMetrics.pipeline.count)}</span>
              </div>
              <div className="flex items-center justify-between">
                <span className="text-sm font-medium">Sales Cycle</span>
                <span className="text-sm">42 days</span>
              </div>
            </div>

            <Button variant="sales" className="w-full">
              View Detailed Forecast
            </Button>
          </CardContent>
        </Card>
      </div>
    </div>
  )
}
