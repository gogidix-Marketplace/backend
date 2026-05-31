import { useState } from 'react'
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
  Tabs,
  TabsContent,
  TabsList,
  TabsTrigger,
} from '@shared/components/ui/tabs'
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
  Area,
  AreaChart,
} from 'recharts'
import { TrendingUp, TrendingDown, Download, Calendar } from 'lucide-react'
import { formatCurrency, formatNumber, getCountryFlag } from '@shared/utils/cn'
import { mockRegionalMetrics, mockRevenueChartData, mockTopPerformers } from '@shared/data/mockData'

const productData = [
  { name: 'Enterprise License', value: 3500000, growth: 15 },
  { name: 'Standard License', value: 2800000, growth: 8 },
  { name: 'Implementation', value: 1200000, growth: 22 },
  { name: 'Support Services', value: 750000, growth: 5 },
  { name: 'Training', value: 250000, growth: -3 },
]

const repPerformanceData = [
  { name: 'John Doe', target: 500000, actual: 532000, country: 'NG' },
  { name: 'Sarah Johnson', target: 400000, actual: 421000, country: 'NG' },
  { name: 'Jane Smith', target: 350000, actual: 338000, country: 'KE' },
  { name: 'Mike Peters', target: 450000, actual: 487000, country: 'ZA' },
  { name: 'Emma Wilson', target: 300000, actual: 287000, country: 'GH' },
]

const COLORS = ['#1E40AF', '#10B981', '#F59E0B', '#EF4444', '#8B5CF6']

export default function AnalyticsPage() {
  const [timeRange, setTimeRange] = useState('this-quarter')
  const [region, setRegion] = useState('all')

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
        <div>
          <h1 className="text-3xl font-bold tracking-tight">Sales Analytics</h1>
          <p className="text-muted-foreground mt-1">
            Comprehensive sales performance insights
          </p>
        </div>
        <div className="flex items-center gap-2">
          <Select value={timeRange} onValueChange={setTimeRange}>
            <SelectTrigger className="w-[180px]">
              <Calendar className="h-4 w-4 mr-2" />
              <SelectValue />
            </SelectTrigger>
            <SelectContent>
              <SelectItem value="this-month">This Month</SelectItem>
              <SelectItem value="this-quarter">This Quarter</SelectItem>
              <SelectItem value="this-year">This Year</SelectItem>
            </SelectContent>
          </Select>
          <Button variant="outline" className="gap-2">
            <Download className="h-4 w-4" />
            Export
          </Button>
        </div>
      </div>

      {/* KPI Cards */}
      <div className="grid gap-4 md:grid-cols-2 lg:grid-cols-4">
        <Card>
          <CardHeader className="pb-2">
            <CardTitle className="text-sm font-medium text-muted-foreground">
              Total Revenue
            </CardTitle>
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">$8.5M</div>
            <p className="text-xs text-green-600 flex items-center gap-1 mt-1">
              <TrendingUp className="h-3 w-3" />
              +18.1% vs last period
            </p>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="pb-2">
            <CardTitle className="text-sm font-medium text-muted-foreground">
              Quota Attainment
            </CardTitle>
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">97.3%</div>
            <p className="text-xs text-green-600 flex items-center gap-1 mt-1">
              <TrendingUp className="h-3 w-3" />
              +4.2% vs last period
            </p>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="pb-2">
            <CardTitle className="text-sm font-medium text-muted-foreground">
              Win Rate
            </CardTitle>
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">32.4%</div>
            <p className="text-xs text-green-600 flex items-center gap-1 mt-1">
              <TrendingUp className="h-3 w-3" />
              +2.8% vs last period
            </p>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="pb-2">
            <CardTitle className="text-sm font-medium text-muted-foreground">
              Avg Sales Cycle
            </CardTitle>
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">42 days</div>
            <p className="text-xs text-red-600 flex items-center gap-1 mt-1">
              <TrendingDown className="h-3 w-3" />
              +3 days vs last period
            </p>
          </CardContent>
        </Card>
      </div>

      {/* Analytics Tabs */}
      <Tabs defaultValue="regional" className="space-y-4">
        <TabsList>
          <TabsTrigger value="regional">Regional</TabsTrigger>
          <TabsTrigger value="products">Products</TabsTrigger>
          <TabsTrigger value="reps">Representatives</TabsTrigger>
          <TabsTrigger value="trends">Trends</TabsTrigger>
        </TabsList>

        {/* Regional Analytics */}
        <TabsContent value="regional" className="space-y-4">
          <div className="grid gap-4 md:grid-cols-2">
            <Card>
              <CardHeader>
                <CardTitle>Revenue by Country</CardTitle>
                <CardDescription>Regional performance comparison</CardDescription>
              </CardHeader>
              <CardContent>
                <ResponsiveContainer width="100%" height={300}>
                  <BarChart data={mockRegionalMetrics}>
                    <CartesianGrid strokeDasharray="3 3" />
                    <XAxis dataKey="country" />
                    <YAxis tickFormatter={(value) => `$${(value / 1000000).toFixed(1)}M`} />
                    <Tooltip
                      formatter={(value: number) => formatCurrency(value)}
                      labelFormatter={(label) => {
                        const country = mockRegionalMetrics.find(m => m.country === label)
                        return country ? `${getCountryFlag(country!.countryCode)} ${label}` : label
                      }}
                    />
                    <Bar dataKey="revenue" fill="#1E40AF" radius={[8, 8, 0, 0]} />
                  </BarChart>
                </ResponsiveContainer>
              </CardContent>
            </Card>

            <Card>
              <CardHeader>
                <CardTitle>Quota Attainment by Country</CardTitle>
                <CardDescription>Percentage of target achieved</CardDescription>
              </CardHeader>
              <CardContent>
                <ResponsiveContainer width="100%" height={300}>
                  <BarChart data={mockRegionalMetrics}>
                    <CartesianGrid strokeDasharray="3 3" />
                    <XAxis dataKey="country" />
                    <YAxis domain={[0, 120]} />
                    <Tooltip formatter={(value: number) => `${value}%`} />
                    <Bar
                      dataKey="attainment"
                      fill="#10B981"
                      radius={[8, 8, 0, 0]}
                      name="% of Quota"
                    />
                  </BarChart>
                </ResponsiveContainer>
              </CardContent>
            </Card>
          </div>

          <Card>
            <CardHeader>
              <CardTitle>Regional Summary</CardTitle>
            </CardHeader>
            <CardContent>
              <div className="space-y-4">
                {mockRegionalMetrics.map((region) => (
                  <div key={region.countryCode} className="flex items-center gap-4">
                    <div className="text-2xl">{getCountryFlag(region.countryCode)}</div>
                    <div className="flex-1">
                      <div className="flex items-center justify-between mb-1">
                        <span className="font-medium">{region.country}</span>
                        <span className="text-sm text-muted-foreground">
                          {region.dealsClosed} deals closed • {region.teamSize} reps
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
                    <div className="text-right min-w-[120px]">
                      <div className="font-bold">{formatCurrency(region.revenue)}</div>
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
        </TabsContent>

        {/* Product Analytics */}
        <TabsContent value="products" className="space-y-4">
          <div className="grid gap-4 md:grid-cols-2">
            <Card>
              <CardHeader>
                <CardTitle>Revenue by Product</CardTitle>
                <CardDescription>Product performance breakdown</CardDescription>
              </CardHeader>
              <CardContent>
                <ResponsiveContainer width="100%" height={300}>
                  <PieChart>
                    <Pie
                      data={productData}
                      cx="50%"
                      cy="50%"
                      labelLine={false}
                      label={({ name, percent }) => `${name}: ${(percent * 100).toFixed(0)}%`}
                      outerRadius={80}
                      fill="#8884d8"
                      dataKey="value"
                    >
                      {productData.map((entry, index) => (
                        <Cell key={`cell-${index}`} fill={COLORS[index % COLORS.length]} />
                      ))}
                    </Pie>
                    <Tooltip formatter={(value: number) => formatCurrency(value)} />
                  </PieChart>
                </ResponsiveContainer>
              </CardContent>
            </Card>

            <Card>
              <CardHeader>
                <CardTitle>Product Growth</CardTitle>
                <CardDescription>Period over period growth</CardDescription>
              </CardHeader>
              <CardContent>
                <div className="space-y-4">
                  {productData.map((product) => (
                    <div key={product.name} className="flex items-center justify-between">
                      <div className="flex-1">
                        <div className="font-medium">{product.name}</div>
                        <div className="text-sm text-muted-foreground">
                          {formatCurrency(product.value)}
                        </div>
                      </div>
                      <Badge
                        variant={product.growth > 0 ? 'success' : 'destructive'}
                        className="ml-4"
                      >
                        {product.growth > 0 ? '+' : ''}{product.growth}%
                      </Badge>
                    </div>
                  ))}
                </div>
              </CardContent>
            </Card>
          </div>
        </TabsContent>

        {/* Representative Analytics */}
        <TabsContent value="reps" className="space-y-4">
          <Card>
            <CardHeader>
              <CardTitle>Representative Performance</CardTitle>
              <CardDescription>Target vs Actual by sales representative</CardDescription>
            </CardHeader>
            <CardContent>
              <ResponsiveContainer width="100%" height={350}>
                <BarChart data={repPerformanceData} layout="horizontal">
                  <CartesianGrid strokeDasharray="3 3" />
                  <XAxis type="number" tickFormatter={(value) => `$${(value / 1000).toFixed(0)}K`} />
                  <YAxis dataKey="name" type="category" width={100} />
                  <Tooltip formatter={(value: number) => formatCurrency(value)} />
                  <Legend />
                  <Bar dataKey="target" fill="#E5E7EB" name="Target" radius={[0, 0, 0, 8]} />
                  <Bar dataKey="actual" fill="#1E40AF" name="Actual" radius={[8, 0, 0, 0]} />
                </BarChart>
              </ResponsiveContainer>
            </CardContent>
          </Card>

          <Card>
            <CardHeader>
              <CardTitle>Top Performers Leaderboard</CardTitle>
            </CardHeader>
            <CardContent>
              <div className="space-y-3">
                {mockTopPerformers.map((performer, index) => (
                  <div
                    key={performer.rank}
                    className="flex items-center gap-4 p-3 bg-gray-50 rounded-lg"
                  >
                    <div className={`flex h-8 w-8 items-center justify-center rounded-full text-sm font-bold ${
                      index === 0 ? 'bg-yellow-400 text-yellow-900' :
                      index === 1 ? 'bg-gray-300 text-gray-700' :
                      index === 2 ? 'bg-orange-300 text-orange-900' :
                      'bg-gray-200 text-gray-600'
                    }`}>
                      {performer.rank}
                    </div>
                    <div className="flex-1">
                      <div className="font-medium">{performer.name}</div>
                      <div className="text-sm text-muted-foreground">{getCountryFlag(performer.country)} {performer.country}</div>
                    </div>
                    <div className="text-right">
                      <div className="font-bold">{formatCurrency(performer.revenue)}</div>
                      <div className={`text-sm ${
                        performer.attainment >= 100 ? 'text-green-600' : 'text-blue-600'
                      }`}>
                        {performer.attainment}% attainment
                      </div>
                    </div>
                  </div>
                ))}
              </div>
            </CardContent>
          </Card>
        </TabsContent>

        {/* Trends */}
        <TabsContent value="trends" className="space-y-4">
          <Card>
            <CardHeader>
              <CardTitle>Revenue Trend</CardTitle>
              <CardDescription>Monthly revenue over time</CardDescription>
            </CardHeader>
            <CardContent>
              <ResponsiveContainer width="100%" height={300}>
                <AreaChart data={mockRevenueChartData}>
                  <defs>
                    <linearGradient id="colorRevenue" x1="0" y1="0" x2="0" y2="1">
                      <stop offset="5%" stopColor="#1E40AF" stopOpacity={0.8} />
                      <stop offset="95%" stopColor="#1E40AF" stopOpacity={0} />
                    </linearGradient>
                  </defs>
                  <CartesianGrid strokeDasharray="3 3" />
                  <XAxis dataKey="name" />
                  <YAxis tickFormatter={(value) => `$${(value / 1000000).toFixed(1)}M`} />
                  <Tooltip formatter={(value: number) => formatCurrency(value)} />
                  <Area
                    type="monotone"
                    dataKey="value"
                    stroke="#1E40AF"
                    fillOpacity={1}
                    fill="url(#colorRevenue)"
                    name="Revenue"
                  />
                  <Line
                    type="monotone"
                    dataKey="target"
                    stroke="#10B981"
                    strokeWidth={2}
                    strokeDasharray="5 5"
                    name="Target"
                  />
                </AreaChart>
              </ResponsiveContainer>
            </CardContent>
          </Card>
        </TabsContent>
      </Tabs>
    </div>
  )
}
