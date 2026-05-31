import { useState } from 'react'
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@shared/components/ui/card'
import { Badge } from '@shared/components/ui/badge'
import { Button } from '@shared/components/ui/button'
import { Tabs, TabsContent, TabsList, TabsTrigger } from '@shared/components/ui/tabs'
import {
  LineChart,
  Line,
  AreaChart,
  Area,
  BarChart,
  Bar,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  Legend,
  ResponsiveContainer,
} from 'recharts'
import { TrendingUp, Download, Calendar, Filter } from 'lucide-react'
import { cn, formatCurrency } from '@shared/utils/cn'

/**
 * CEO Analytics Page
 *
 * Components:
 * - CrossDomainAnalytics - Performance comparison across domains
 * - RegionalPerformanceMap - Geographic performance visualization
 * - TrendAnalysis - Multi-line trend charts with forecasts
 */

// Mock data for cross-domain analytics
const crossDomainData = [
  { month: 'Jan', Executive: 8.2, Business: 9.1, Public: 7.5, Management: 8.8, Foundation: 7.2 },
  { month: 'Feb', Executive: 8.5, Business: 9.3, Public: 7.8, Management: 9.0, Foundation: 7.5 },
  { month: 'Mar', Executive: 8.7, Business: 9.5, Public: 8.0, Management: 9.2, Foundation: 7.8 },
  { month: 'Apr', Executive: 8.4, Business: 9.2, Public: 7.9, Management: 8.9, Foundation: 7.6 },
  { month: 'May', Executive: 8.9, Business: 9.7, Public: 8.2, Management: 9.4, Foundation: 8.0 },
  { month: 'Jun', Executive: 9.1, Business: 9.8, Public: 8.4, Management: 9.5, Foundation: 8.2 },
]

// Mock data for regional performance
const regionalData = [
  { region: 'North America', revenue: 18500000, growth: 12.3, customers: 450000, satisfaction: 92 },
  { region: 'Europe', revenue: 12000000, growth: -5.2, customers: 280000, satisfaction: 88 },
  { region: 'Nigeria', revenue: 7800000, growth: 18.7, customers: 320000, satisfaction: 94 },
  { region: 'Kenya', revenue: 3200000, growth: 15.2, customers: 120000, satisfaction: 91 },
  { region: 'South Africa', revenue: 1000000, growth: 8.5, customers: 45000, satisfaction: 86 },
]

// Mock data for trend analysis with AI forecast
const trendData = [
  { month: 'Jan', revenue: 38000000, actual: 38000000, forecast: null },
  { month: 'Feb', revenue: 39500000, actual: 39500000, forecast: null },
  { month: 'Mar', revenue: 41000000, actual: 41000000, forecast: null },
  { month: 'Apr', revenue: 42500000, actual: 42500000, forecast: null },
  { month: 'May', revenue: 43800000, actual: null, forecast: 43000000 },
  { month: 'Jun', revenue: 45200000, actual: null, forecast: 44500000 },
  { month: 'Jul', revenue: 46500000, actual: null, forecast: 46000000 },
  { month: 'Aug', revenue: 48000000, actual: null, forecast: 47500000 },
]

export default function AnalyticsPage() {
  const [timeRange, setTimeRange] = useState<'3m' | '6m' | '1y' | 'all'>('6m')
  const [selectedMetric, setSelectedMetric] = useState<'revenue' | 'growth' | 'customers'>('revenue')

  return (
    <div className="space-y-6">
      {/* Page Header */}
      <div className="page-header">
        <h1 className="page-title">Executive Analytics</h1>
        <p className="page-description">
          Deep dive into performance metrics across all domains and regions
        </p>
      </div>

      {/* Filters */}
      <div className="flex items-center justify-between">
        <div className="flex items-center gap-2">
          <Filter className="h-4 w-4 text-muted-foreground" />
          <Tabs value={timeRange} onValueChange={(v) => setTimeRange(v as any)}>
            <TabsList>
              <TabsTrigger value="3m">3 Months</TabsTrigger>
              <TabsTrigger value="6m">6 Months</TabsTrigger>
              <TabsTrigger value="1y">1 Year</TabsTrigger>
              <TabsTrigger value="all">All Time</TabsTrigger>
            </TabsList>
          </Tabs>
        </div>
        <Button variant="outline" className="gap-2">
          <Download className="h-4 w-4" />
          Export
        </Button>
      </div>

      <Tabs defaultValue="cross-domain" className="space-y-6">
        <TabsList className="grid w-full max-w-md grid-cols-3">
          <TabsTrigger value="cross-domain">Cross-Domain</TabsTrigger>
          <TabsTrigger value="regional">Regional</TabsTrigger>
          <TabsTrigger value="trends">Trends</TabsTrigger>
        </TabsList>

        {/* Cross-Domain Analytics */}
        <TabsContent value="cross-domain" className="space-y-6">
          <div className="grid gap-6">
            {/* Performance Score Comparison */}
            <Card>
              <CardHeader>
                <CardTitle>Domain Performance Scores</CardTitle>
                <CardDescription>
                  Health scores across all domains over time
                </CardDescription>
              </CardHeader>
              <CardContent>
                <ResponsiveContainer width="100%" height={300}>
                  <LineChart data={crossDomainData}>
                    <CartesianGrid strokeDasharray="3 3" className="stroke-muted" />
                    <XAxis dataKey="month" />
                    <YAxis domain={[0, 10]} />
                    <Tooltip
                      contentStyle={{
                        backgroundColor: 'hsl(var(--background))',
                        border: '1px solid hsl(var(--border))',
                        borderRadius: '8px',
                      }}
                    />
                    <Legend />
                    <Line type="monotone" dataKey="Executive" stroke="#7C4DFF" strokeWidth={2} dot={{ r: 4 }} />
                    <Line type="monotone" dataKey="Business" stroke="#22c55e" strokeWidth={2} dot={{ r: 4 }} />
                    <Line type="monotone" dataKey="Public" stroke="#3b82f6" strokeWidth={2} dot={{ r: 4 }} />
                    <Line type="monotone" dataKey="Management" stroke="#f59e0b" strokeWidth={2} dot={{ r: 4 }} />
                    <Line type="monotone" dataKey="Foundation" stroke="#ef4444" strokeWidth={2} dot={{ r: 4 }} />
                  </LineChart>
                </ResponsiveContainer>
              </CardContent>
            </Card>

            {/* Domain Comparison Table */}
            <Card>
              <CardHeader>
                <CardTitle>Domain Summary</CardTitle>
                <CardDescription>
                  Current performance metrics by domain
                </CardDescription>
              </CardHeader>
              <CardContent>
                <div className="space-y-4">
                  {[
                    { name: 'Executive', score: 9.1, revenue: 8500000, change: 8.5, status: 'On Track' },
                    { name: 'Business', score: 9.8, revenue: 15000000, change: 12.3, status: 'Ahead' },
                    { name: 'Public', score: 8.4, revenue: 8000000, change: -2.1, status: 'At Risk' },
                    { name: 'Management', score: 9.5, revenue: 7000000, change: 7.8, status: 'On Track' },
                    { name: 'Foundation', score: 8.2, revenue: 4000000, change: 5.5, status: 'On Track' },
                  ].map((domain) => (
                    <div
                      key={domain.name}
                      className="flex items-center justify-between p-3 rounded-lg border"
                    >
                      <div className="flex-1">
                        <div className="flex items-center gap-2 mb-1">
                          <span className="font-medium">{domain.name}</span>
                          <Badge variant={domain.status === 'Ahead' ? 'success' : domain.status === 'At Risk' ? 'warning' : 'secondary'}>
                            {domain.status}
                          </Badge>
                        </div>
                        <div className="flex items-center gap-4 text-sm text-muted-foreground">
                          <span>Score: {domain.score}/10</span>
                          <span>Revenue: {formatCurrency(domain.revenue)}</span>
                          <span className={cn(domain.change >= 0 ? 'text-green-600' : 'text-red-600')}>
                            {domain.change >= 0 ? '+' : ''}{domain.change}%
                          </span>
                        </div>
                      </div>
                      <Button variant="outline" size="sm">View Details</Button>
                    </div>
                  ))}
                </div>
              </CardContent>
            </Card>
          </div>
        </TabsContent>

        {/* Regional Analytics */}
        <TabsContent value="regional" className="space-y-6">
          <div className="grid gap-6">
            {/* Revenue by Region */}
            <Card>
              <CardHeader>
                <CardTitle>Revenue by Region</CardTitle>
                <CardDescription>
                  Geographic revenue distribution
                </CardDescription>
              </CardHeader>
              <CardContent>
                <ResponsiveContainer width="100%" height={250}>
                  <BarChart data={regionalData}>
                    <CartesianGrid strokeDasharray="3 3" className="stroke-muted" />
                    <XAxis dataKey="region" />
                    <YAxis tickFormatter={(value) => `$${(value / 1000000).toFixed(0)}M`} />
                    <Tooltip
                      formatter={(value: number) => formatCurrency(value)}
                      contentStyle={{
                        backgroundColor: 'hsl(var(--background))',
                        border: '1px solid hsl(var(--border))',
                        borderRadius: '8px',
                      }}
                    />
                    <Bar dataKey="revenue" fill="#7C4DFF" radius={[4, 4, 0, 0]} />
                  </BarChart>
                </ResponsiveContainer>
              </CardContent>
            </Card>

            {/* Regional Metrics Grid */}
            <div className="grid md:grid-cols-2 gap-6">
              {regionalData.map((region) => (
                <Card key={region.region}>
                  <CardHeader className="pb-3">
                    <CardTitle className="text-base">{region.region}</CardTitle>
                  </CardHeader>
                  <CardContent className="space-y-3">
                    <div className="flex justify-between items-center">
                      <span className="text-sm text-muted-foreground">Revenue</span>
                      <span className="font-semibold">{formatCurrency(region.revenue)}</span>
                    </div>
                    <div className="flex justify-between items-center">
                      <span className="text-sm text-muted-foreground">Growth</span>
                      <span className={cn('font-semibold', region.growth >= 0 ? 'text-green-600' : 'text-red-600')}>
                        {region.growth >= 0 ? '+' : ''}{region.growth}%
                      </span>
                    </div>
                    <div className="flex justify-between items-center">
                      <span className="text-sm text-muted-foreground">Customers</span>
                      <span className="font-semibold">{region.customers.toLocaleString()}</span>
                    </div>
                    <div className="flex justify-between items-center">
                      <span className="text-sm text-muted-foreground">Satisfaction</span>
                      <span className="font-semibold">{region.satisfaction}%</span>
                    </div>
                  </CardContent>
                </Card>
              ))}
            </div>
          </div>
        </TabsContent>

        {/* Trend Analysis */}
        <TabsContent value="trends" className="space-y-6">
          <div className="grid gap-6">
            {/* Revenue Trend with AI Forecast */}
            <Card>
              <CardHeader>
                <div className="flex items-center justify-between">
                  <div>
                    <CardTitle>Revenue Trend & AI Forecast</CardTitle>
                    <CardDescription>
                      Historical data with AI-powered projections
                    </CardDescription>
                  </div>
                  <Badge className="gap-1 bg-purple-100 text-purple-700 border-purple-200">
                    <TrendingUp className="h-3 w-3" />
                    AI Forecast
                  </Badge>
                </div>
              </CardHeader>
              <CardContent>
                <ResponsiveContainer width="100%" height={300}>
                  <AreaChart data={trendData}>
                    <CartesianGrid strokeDasharray="3 3" className="stroke-muted" />
                    <XAxis dataKey="month" />
                    <YAxis tickFormatter={(value) => `$${(value / 1000000).toFixed(0)}M`} />
                    <Tooltip
                      formatter={(value: number, name: string) => {
                        if (name === 'actual') return [formatCurrency(value), 'Actual']
                        if (name === 'forecast') return [formatCurrency(value), 'AI Forecast']
                        return [value, name]
                      }}
                      contentStyle={{
                        backgroundColor: 'hsl(var(--background))',
                        border: '1px solid hsl(var(--border))',
                        borderRadius: '8px',
                      }}
                    />
                    <Legend />
                    <Area
                      type="monotone"
                      dataKey="actual"
                      stroke="#7C4DFF"
                      fill="#7C4DFF"
                      fillOpacity={0.3}
                      strokeWidth={2}
                    />
                    <Area
                      type="monotone"
                      dataKey="forecast"
                      stroke="#B388FF"
                      fill="#B388FF"
                      fillOpacity={0.2}
                      strokeWidth={2}
                      strokeDasharray="5 5"
                    />
                  </AreaChart>
                </ResponsiveContainer>
                <div className="flex items-center justify-center gap-6 mt-4 text-sm">
                  <div className="flex items-center gap-2">
                    <div className="h-3 w-3 rounded-full bg-[#7C4DFF]" />
                    <span>Actual Revenue</span>
                  </div>
                  <div className="flex items-center gap-2">
                    <div className="h-3 w-3 rounded-full bg-[#B388FF] border-2 border-dashed border-[#B388FF]" />
                    <span>AI Forecast</span>
                  </div>
                </div>
              </CardContent>
            </Card>

            {/* AI Insights */}
            <Card className="border-purple-200 dark:border-purple-800">
              <CardHeader>
                <CardTitle className="flex items-center gap-2">
                  <TrendingUp className="h-5 w-5 text-purple-500" />
                  AI-Generated Insights
                </CardTitle>
              </CardHeader>
              <CardContent>
                <div className="space-y-3">
                  <div className="p-3 rounded-lg bg-purple-50 dark:bg-purple-950">
                    <div className="flex items-start gap-3">
                      <div className="flex h-8 w-8 items-center justify-center rounded-full bg-purple-100 dark:bg-purple-900">
                        <TrendingUp className="h-4 w-4 text-purple-600" />
                      </div>
                      <div>
                        <p className="font-medium">Growth Acceleration</p>
                        <p className="text-sm text-muted-foreground">
                          Revenue growth accelerating at 2.3% MoM. Projected to exceed Q2 targets by 8%.
                        </p>
                        <p className="text-xs text-purple-600 mt-1">Confidence: 87%</p>
                      </div>
                    </div>
                  </div>
                  <div className="p-3 rounded-lg bg-amber-50 dark:bg-amber-950">
                    <div className="flex items-start gap-3">
                      <div className="flex h-8 w-8 items-center justify-center rounded-full bg-amber-100 dark:bg-amber-900">
                        <TrendingUp className="h-4 w-4 text-amber-600" />
                      </div>
                      <div>
                        <p className="font-medium">Regional Anomaly Detected</p>
                        <p className="text-sm text-muted-foreground">
                          European performance deviating from forecast. Recommend investigation.
                        </p>
                        <p className="text-xs text-amber-600 mt-1">Confidence: 72%</p>
                      </div>
                    </div>
                  </div>
                </div>
              </CardContent>
            </Card>
          </div>
        </TabsContent>
      </Tabs>
    </div>
  )
}
