import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@shared/components/ui/card'
import { Badge } from '@shared/components/ui/badge'
import { Button } from '@shared/components/ui/button'
import { Tabs, TabsContent, TabsList, TabsTrigger } from '@shared/components/ui/tabs'
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from '@shared/components/ui/select'
import { cn, formatCurrency, formatNumber, formatPercentage } from '@shared/utils/cn'
import { TrendingUp, TrendingDown, BarChart3, PieChart, Activity } from 'lucide-react'

// Mock data
const timeRanges = ['Last 7 Days', 'Last 30 Days', 'Last 90 Days', 'Last 12 Months', 'YTD']

const crossDomainMetrics = [
  {
    domain: 'Finance',
    revenue: 42500000,
    growth: 8.5,
    margin: 18.5,
    status: 'on_track',
    color: '#0D47A1',
  },
  {
    domain: 'Sales',
    revenue: 38200000,
    growth: 12.3,
    margin: 15.2,
    status: 'ahead',
    color: '#FFA000',
  },
  {
    domain: 'Marketing',
    budget: 8500000,
    roi: 320,
    leads: 45000,
    status: 'on_track',
    color: '#7C3AED',
  },
  {
    domain: 'HR',
    headcount: 2847,
    growth: 5.2,
    engagement: 78,
    status: 'at_risk',
    color: '#EC4899',
  },
  {
    domain: 'Support',
    csat: 92,
    responseTime: 1.8,
    tickets: 12450,
    status: 'ahead',
    color: '#10B981',
  },
  {
    domain: 'Operations',
    efficiency: 87,
    uptime: 99.5,
    incidents: 23,
    status: 'on_track',
    color: '#F59E0B',
  },
]

const regionalData = [
  {
    region: 'North America',
    countries: ['USA', 'Canada'],
    revenue: 18500000,
    target: 20000000,
    growth: 12.3,
    share: 43.5,
    status: 'on_track',
  },
  {
    region: 'Europe',
    countries: ['Ireland', 'UK', 'Germany'],
    revenue: 12000000,
    target: 15000000,
    growth: -5.2,
    share: 28.2,
    status: 'behind',
  },
  {
    region: 'Africa - West',
    countries: ['Nigeria', 'Ghana'],
    revenue: 7800000,
    target: 8000000,
    growth: 18.7,
    share: 18.4,
    status: 'on_track',
  },
  {
    region: 'Africa - East',
    countries: ['Kenya', 'Uganda'],
    revenue: 3200000,
    target: 4000000,
    growth: 15.2,
    share: 7.5,
    status: 'on_track',
  },
  {
    region: 'Africa - South',
    countries: ['South Africa'],
    revenue: 1000000,
    target: 2000000,
    growth: 8.5,
    share: 2.4,
    status: 'behind',
  },
]

const trendData = [
  { month: 'Sep', revenue: 32100000, target: 35000000 },
  { month: 'Oct', revenue: 35800000, target: 36500000 },
  { month: 'Nov', revenue: 37200000, target: 38000000 },
  { month: 'Dec', revenue: 39800000, target: 39000000 },
  { month: 'Jan', revenue: 38500000, target: 40000000 },
  { month: 'Feb', revenue: 42500000, target: 41500000 },
]

const topPerformers = [
  {
    category: 'Countries',
    items: [
      { name: 'Nigeria', value: 6200000, change: 22.3 },
      { name: 'USA', value: 15800000, change: 14.1 },
      { name: 'Kenya', value: 2800000, change: 18.5 },
      { name: 'Ireland', value: 7800000, change: -3.2 },
    ],
  },
  {
    category: 'Products',
    items: [
      { name: 'Enterprise Analytics', value: 18500000, change: 28.5 },
      { name: 'Cloud Platform', value: 15200000, change: 15.2 },
      { name: 'Security Suite', value: 5800000, change: 8.7 },
      { name: 'AI Services', value: 3000000, change: 45.2 },
    ],
  },
  {
    category: 'Segments',
    items: [
      { name: 'Enterprise', value: 28500000, change: 12.3 },
      { name: 'Mid-Market', value: 9800000, change: 18.7 },
      { name: 'SMB', value: 4200000, change: -5.2 },
    ],
  },
]

function DomainCard({ data }: { data: typeof crossDomainMetrics[0] }) {
  return (
    <Card>
      <CardHeader className="pb-3">
        <div className="flex items-start justify-between">
          <div
            className="flex h-10 w-10 items-center justify-center rounded-lg"
            style={{ backgroundColor: `${data.color}20` }}
          >
            <Activity className="h-5 w-5" style={{ color: data.color }} />
          </div>
          <Badge
            variant={data.status === 'on_track' || data.status === 'ahead' ? 'success' : 'warning'}
          >
            {data.status}
          </Badge>
        </div>
        <CardTitle className="text-sm font-medium">{data.domain}</CardTitle>
      </CardHeader>
      <CardContent className="space-y-2">
        {data.revenue && (
          <div className="flex items-baseline justify-between">
            <span className="text-2xl font-bold">{formatCurrency(data.revenue)}</span>
            <span className={cn('flex items-center text-sm', data.growth > 0 ? 'text-green-600' : 'text-red-600')}>
              {data.growth > 0 ? <TrendingUp className="mr-1 h-4 w-4" /> : <TrendingDown className="mr-1 h-4 w-4" />}
              {formatPercentage(data.growth)}
            </span>
          </div>
        )}
        {data.budget && (
          <div className="flex items-baseline justify-between">
            <span className="text-2xl font-bold">{formatCurrency(data.budget)}</span>
            <span className="text-sm text-muted-foreground">Budget</span>
          </div>
        )}
        {data.margin && (
          <div className="text-sm text-muted-foreground">Margin: {data.margin}%</div>
        )}
        {data.roi && (
          <div className="text-sm text-muted-foreground">ROI: {data.roi}%</div>
        )}
        {data.headcount && (
          <div className="text-sm text-muted-foreground">Headcount: {formatNumber(data.headcount)}</div>
        )}
        {data.csat && (
          <div className="text-sm text-muted-foreground">CSAT: {data.csat}%</div>
        )}
        {data.efficiency && (
          <div className="text-sm text-muted-foreground">Efficiency: {data.efficiency}%</div>
        )}
      </CardContent>
    </Card>
  )
}

export default function AnalyticsPage() {
  return (
    <div className="space-y-6">
      {/* Page Header */}
      <div className="flex items-center justify-between">
        <div className="page-header">
          <h1 className="page-title">Analytics Dashboard</h1>
          <p className="page-description">
            Cross-domain analytics and performance insights
          </p>
        </div>
        <div className="flex items-center gap-2">
          <Select defaultValue="last30days">
            <SelectTrigger className="w-[180px]">
              <SelectValue placeholder="Select time range" />
            </SelectTrigger>
            <SelectContent>
              {timeRanges.map((range) => (
                <SelectItem key={range} value={range.toLowerCase().replace(' ', '')}>
                  {range}
                </SelectItem>
              ))}
            </SelectContent>
          </Select>
          <Button variant="outline">Export</Button>
        </div>
      </div>

      {/* Tabs */}
      <Tabs defaultValue="cross-domain" className="space-y-6">
        <TabsList>
          <TabsTrigger value="cross-domain">
            <BarChart3 className="mr-2 h-4 w-4" />
            Cross-Domain
          </TabsTrigger>
          <TabsTrigger value="regional">
            <PieChart className="mr-2 h-4 w-4" />
            Regional
          </TabsTrigger>
          <TabsTrigger value="trends">
            <TrendingUp className="mr-2 h-4 w-4" />
            Trends
          </TabsTrigger>
        </TabsList>

        {/* Cross-Domain Analytics */}
        <TabsContent value="cross-domain" className="space-y-6">
          <div className="grid gap-4 md:grid-cols-2 lg:grid-cols-3">
            {crossDomainMetrics.map((domain) => (
              <DomainCard key={domain.domain} data={domain} />
            ))}
          </div>

          {/* Top Performers */}
          <div className="grid gap-6 md:grid-cols-3">
            {topPerformers.map((category) => (
              <Card key={category.category}>
                <CardHeader>
                  <CardTitle className="text-sm font-medium">{category.category}</CardTitle>
                </CardHeader>
                <CardContent>
                  <div className="space-y-4">
                    {category.items.map((item, idx) => (
                      <div key={idx} className="space-y-1">
                        <div className="flex items-center justify-between">
                          <span className="text-sm font-medium">{item.name}</span>
                          <span className={cn('text-xs flex items-center', item.change > 0 ? 'text-green-600' : 'text-red-600')}>
                            {item.change > 0 ? '+' : ''}{item.change}%
                          </span>
                        </div>
                        <div className="h-2 w-full overflow-hidden rounded-full bg-slate-200 dark:bg-slate-800">
                          <div
                            className="h-full bg-[#0D47A1]"
                            style={{ width: `${Math.min((item.value / 20000000) * 100, 100)}%` }}
                          />
                        </div>
                      </div>
                    ))}
                  </div>
                </CardContent>
              </Card>
            ))}
          </div>
        </TabsContent>

        {/* Regional Analytics */}
        <TabsContent value="regional" className="space-y-6">
          <div className="grid gap-6 md:grid-cols-2">
            {regionalData.map((region) => (
              <Card key={region.region}>
                <CardHeader>
                  <div className="flex items-start justify-between">
                    <div>
                      <CardTitle>{region.region}</CardTitle>
                      <CardDescription>
                        {region.countries.join(', ')}
                      </CardDescription>
                    </div>
                    <Badge
                      variant={region.status === 'on_track' ? 'success' : 'warning'}
                    >
                      {region.status}
                    </Badge>
                  </div>
                </CardHeader>
                <CardContent className="space-y-4">
                  <div className="grid grid-cols-2 gap-4">
                    <div>
                      <p className="text-sm text-muted-foreground">Revenue</p>
                      <p className="text-2xl font-bold">{formatCurrency(region.revenue)}</p>
                    </div>
                    <div>
                      <p className="text-sm text-muted-foreground">Target</p>
                      <p className="text-2xl font-bold">{formatCurrency(region.target)}</p>
                    </div>
                    <div>
                      <p className="text-sm text-muted-foreground">Growth</p>
                      <p className={cn('text-2xl font-bold', region.growth > 0 ? 'text-green-600' : 'text-red-600')}>
                        {formatPercentage(region.growth)}
                      </p>
                    </div>
                    <div>
                      <p className="text-sm text-muted-foreground">Market Share</p>
                      <p className="text-2xl font-bold">{region.share}%</p>
                    </div>
                  </div>
                  <div>
                    <div className="mb-1 flex justify-between text-sm">
                      <span>Progress to Target</span>
                      <span>{Math.round((region.revenue / region.target) * 100)}%</span>
                    </div>
                    <div className="h-2 w-full overflow-hidden rounded-full bg-slate-200 dark:bg-slate-800">
                      <div
                        className={cn('h-full', (region.revenue / region.target) >= 1 ? 'bg-green-500' : 'bg-blue-500')}
                        style={{ width: `${Math.min((region.revenue / region.target) * 100, 100)}%` }}
                      />
                    </div>
                  </div>
                </CardContent>
              </Card>
            ))}
          </div>
        </TabsContent>

        {/* Trend Analysis */}
        <TabsContent value="trends" className="space-y-6">
          <Card>
            <CardHeader>
              <CardTitle>Revenue Trend</CardTitle>
              <CardDescription>Monthly revenue vs. target (Last 6 months)</CardDescription>
            </CardHeader>
            <CardContent>
              <div className="space-y-4">
                {trendData.map((item) => (
                  <div key={item.month} className="space-y-2">
                    <div className="flex items-center justify-between text-sm">
                      <span className="font-medium">{item.month}</span>
                      <div className="flex items-center gap-4">
                        <span className="text-muted-foreground">Target: {formatCurrency(item.target)}</span>
                        <span className={cn('font-medium', item.revenue >= item.target ? 'text-green-600' : 'text-red-600')}>
                          {formatCurrency(item.revenue)}
                        </span>
                      </div>
                    </div>
                    <div className="flex h-8 w-full gap-1">
                      <div
                        className="rounded-l bg-slate-200 dark:bg-slate-700"
                        style={{ width: '50%' }}
                      >
                        <div
                          className="h-full bg-[#0D47A1]/50"
                          style={{ width: `${(item.target / 50000000) * 100}%` }}
                        />
                      </div>
                      <div
                        className="rounded-r bg-slate-200 dark:bg-slate-700"
                        style={{ width: '50%' }}
                      >
                        <div
                          className={cn('h-full', item.revenue >= item.target ? 'bg-green-500' : 'bg-red-500')}
                          style={{ width: `${(item.revenue / 50000000) * 100}%` }}
                        />
                      </div>
                    </div>
                  </div>
                ))}
              </div>
            </CardContent>
          </Card>
        </TabsContent>
      </Tabs>
    </div>
  )
}
