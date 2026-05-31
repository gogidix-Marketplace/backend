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
  LineChart,
  Line,
  BarChart,
  Bar,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  Legend,
  ResponsiveContainer,
  Area,
  AreaChart,
} from 'recharts'
import {
  TrendingUp,
  Calendar,
  Target,
  AlertCircle,
  CheckCircle2,
  Info,
} from 'lucide-react'
import { formatCurrency } from '@shared/utils/cn'
import { mockForecasts, mockSalesMetrics, mockRevenueChartData } from '@shared/data/mockData'

const forecastScenarios = [
  { name: 'Commit', color: '#10B981', description: '75% confidence - Expected to close' },
  { name: 'Upside', color: '#3B82F6', description: '50% confidence - Optimistic case' },
  { name: 'Baseline', color: '#F59E0B', description: '90% confidence - Conservative' },
]

const monthlyForecastData = [
  { month: 'Jan', commit: 8200000, upside: 9500000, baseline: 7500000 },
  { month: 'Feb', commit: 9200000, upside: 10500000, baseline: 8200000 },
  { month: 'Mar', commit: 10100000, upside: 11500000, baseline: 9000000 },
  { month: 'Apr', commit: 8800000, upside: 10000000, baseline: 7800000 },
  { month: 'May', commit: 9500000, upside: 11000000, baseline: 8500000 },
  { month: 'Jun', commit: 10200000, upside: 11800000, baseline: 9200000 },
]

export default function ForecastPage() {
  const [selectedPeriod, setSelectedPeriod] = useState<'month' | 'quarter' | 'year'>('quarter')
  const [selectedScenario, setSelectedScenario] = useState<string>('commit')

  const currentForecast = mockForecasts.find(f => f.period === selectedPeriod)

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
        <div>
          <h1 className="text-3xl font-bold tracking-tight">Sales Forecast</h1>
          <p className="text-muted-foreground mt-1">
            AI-powered revenue forecasting and predictions
          </p>
        </div>
        <div className="flex gap-2">
          <Select value={selectedPeriod} onValueChange={(v: any) => setSelectedPeriod(v)}>
            <SelectTrigger className="w-[150px]">
              <Calendar className="h-4 w-4 mr-2" />
              <SelectValue />
            </SelectTrigger>
            <SelectContent>
              <SelectItem value="month">This Month</SelectItem>
              <SelectItem value="quarter">This Quarter</SelectItem>
              <SelectItem value="year">This Year</SelectItem>
            </SelectContent>
          </Select>
          <Button>
            <Target className="h-4 w-4 mr-2" />
            Adjust Targets
          </Button>
        </div>
      </div>

      {/* Forecast Summary */}
      {currentForecast && (
        <div className="grid gap-4 md:grid-cols-4">
          <Card>
            <CardHeader className="pb-2">
              <CardTitle className="text-sm font-medium text-muted-foreground">
                Commit Forecast
              </CardTitle>
            </CardHeader>
            <CardContent>
              <div className="text-2xl font-bold">{formatCurrency(currentForecast.predictedRevenue)}</div>
              <p className="text-xs text-muted-foreground mt-1 flex items-center gap-1">
                <CheckCircle2 className="h-3 w-3 text-green-500" />
                {currentForecast.confidence}% confidence
              </p>
            </CardContent>
          </Card>

          <Card>
            <CardHeader className="pb-2">
              <CardTitle className="text-sm font-medium text-muted-foreground">
                Best Case
              </CardTitle>
            </CardHeader>
            <CardContent>
              <div className="text-2xl font-bold text-green-600">
                {formatCurrency(currentForecast.bestCase)}
              </div>
              <p className="text-xs text-muted-foreground mt-1">
                +{Math.round(((currentForecast.bestCase - currentForecast.predictedRevenue) / currentForecast.predictedRevenue) * 100)}% upside potential
              </p>
            </CardContent>
          </Card>

          <Card>
            <CardHeader className="pb-2">
              <CardTitle className="text-sm font-medium text-muted-foreground">
                Worst Case
              </CardTitle>
            </CardHeader>
            <CardContent>
              <div className="text-2xl font-bold text-orange-600">
                {formatCurrency(currentForecast.worstCase)}
              </div>
              <p className="text-xs text-muted-foreground mt-1">
                -{Math.round(((currentForecast.predictedRevenue - currentForecast.worstCase) / currentForecast.predictedRevenue) * 100)}% downside risk
              </p>
            </CardContent>
          </Card>

          <Card>
            <CardHeader className="pb-2">
              <CardTitle className="text-sm font-medium text-muted-foreground">
                Pipeline Contribution
              </CardTitle>
            </CardHeader>
            <CardContent>
              <div className="text-2xl font-bold">{formatCurrency(currentForecast.pipelineContribution)}</div>
              <p className="text-xs text-muted-foreground mt-1">
                From open pipeline
              </p>
            </CardContent>
          </Card>
        </div>
      )}

      {/* Forecast Scenarios */}
      <Card>
        <CardHeader>
          <CardTitle>Forecast Scenarios</CardTitle>
          <CardDescription>Compare different forecast scenarios</CardDescription>
        </CardHeader>
        <CardContent>
          <div className="space-y-4">
            {forecastScenarios.map((scenario) => (
              <div key={scenario.name} className="flex items-center gap-4 p-4 border rounded-lg">
                <div
                  className="h-4 w-4 rounded-full"
                  style={{ backgroundColor: scenario.color }}
                />
                <div className="flex-1">
                  <div className="font-medium">{scenario.name}</div>
                  <div className="text-sm text-muted-foreground">{scenario.description}</div>
                </div>
                <div className="text-right">
                  <div className="font-bold">
                    {formatCurrency(
                      scenario.name === 'Commit' ? currentForecast?.predictedRevenue :
                      scenario.name === 'Upside' ? currentForecast?.bestCase :
                      currentForecast?.worstCase
                    )}
                  </div>
                </div>
              </div>
            ))}
          </div>
        </CardContent>
      </Card>

      {/* Forecast Chart */}
      <div className="grid gap-4 md:grid-cols-2">
        <Card>
          <CardHeader>
            <CardTitle>6-Month Forecast Trend</CardTitle>
            <CardDescription>Projected revenue by scenario</CardDescription>
          </CardHeader>
          <CardContent>
            <ResponsiveContainer width="100%" height={300}>
              <AreaChart data={monthlyForecastData}>
                <defs>
                  <linearGradient id="colorCommit" x1="0" y1="0" x2="0" y2="1">
                    <stop offset="5%" stopColor="#10B981" stopOpacity={0.8} />
                    <stop offset="95%" stopColor="#10B981" stopOpacity={0} />
                  </linearGradient>
                  <linearGradient id="colorUpside" x1="0" y1="0" x2="0" y2="1">
                    <stop offset="5%" stopColor="#3B82F6" stopOpacity={0.8} />
                    <stop offset="95%" stopColor="#3B82F6" stopOpacity={0} />
                  </linearGradient>
                </defs>
                <CartesianGrid strokeDasharray="3 3" />
                <XAxis dataKey="month" />
                <YAxis tickFormatter={(value) => `$${(value / 1000000).toFixed(0)}M`} />
                <Tooltip formatter={(value: number) => formatCurrency(value)} />
                <Legend />
                <Area
                  type="monotone"
                  dataKey="baseline"
                  stackId="1"
                  stroke="#F59E0B"
                  fill="url(#colorBaseline)"
                  name="Baseline"
                />
                <Area
                  type="monotone"
                  dataKey="commit"
                  stackId="1"
                  stroke="#10B981"
                  fill="url(#colorCommit)"
                  name="Commit"
                />
                <Area
                  type="monotone"
                  dataKey="upside"
                  stackId="1"
                  stroke="#3B82F6"
                  fill="url(#colorUpside)"
                  name="Upside"
                />
              </AreaChart>
            </ResponsiveContainer>
          </CardContent>
        </Card>

        <Card>
          <CardHeader>
            <CardTitle>Forecast vs Actual</CardTitle>
            <CardDescription>Historical forecast accuracy</CardDescription>
          </CardHeader>
          <CardContent>
            <ResponsiveContainer width="100%" height={300}>
              <BarChart data={mockRevenueChartData}>
                <CartesianGrid strokeDasharray="3 3" />
                <XAxis dataKey="name" />
                <YAxis tickFormatter={(value) => `$${(value / 1000000).toFixed(0)}M`} />
                <Tooltip formatter={(value: number) => formatCurrency(value)} />
                <Legend />
                <Bar dataKey="value" fill="#1E40AF" name="Actual" radius={[8, 8, 0, 0]} />
                <Bar dataKey="target" fill="#E5E7EB" name="Forecast" radius={[8, 8, 0, 0]} />
              </BarChart>
            </ResponsiveContainer>
          </CardContent>
        </Card>
      </div>

      {/* Risk Factors */}
      <Card>
        <CardHeader>
          <CardTitle>Forecast Risk Factors</CardTitle>
          <CardDescription>Items that may impact forecast accuracy</CardDescription>
        </CardHeader>
        <CardContent>
          <div className="space-y-3">
            <div className="flex items-start gap-3 p-3 bg-yellow-50 border border-yellow-200 rounded-lg">
              <AlertCircle className="h-5 w-5 text-yellow-600 mt-0.5" />
              <div className="flex-1">
                <div className="font-medium text-yellow-800">3 Large Deals in Negotiation</div>
                <div className="text-sm text-yellow-700 mt-1">
                  $1.2M in deals with 80%+ probability - may close early or late
                </div>
              </div>
              <Badge variant="warning">Medium Risk</Badge>
            </div>

            <div className="flex items-start gap-3 p-3 bg-green-50 border border-green-200 rounded-lg">
              <CheckCircle2 className="h-5 w-5 text-green-600 mt-0.5" />
              <div className="flex-1">
                <div className="font-medium text-green-800">Strong Pipeline in Q2</div>
                <div className="text-sm text-green-700 mt-1">
                  45 new opportunities added this month - above target
                </div>
              </div>
              <Badge variant="success">Positive</Badge>
            </div>

            <div className="flex items-start gap-3 p-3 bg-blue-50 border border-blue-200 rounded-lg">
              <Info className="h-5 w-5 text-blue-600 mt-0.5" />
              <div className="flex-1">
                <div className="font-medium text-blue-800">Seasonal Trend Expected</div>
                <div className="text-sm text-blue-700 mt-1">
                  Historical data shows 15% increase in Q2
                </div>
              </div>
              <Badge variant="info">Information</Badge>
            </div>
          </div>
        </CardContent>
      </Card>

      {/* Forecast Accuracy */}
      <Card>
        <CardHeader>
          <CardTitle>Forecast Accuracy Metrics</CardTitle>
          <CardDescription>How well our forecasts have performed</CardDescription>
        </CardHeader>
        <CardContent>
          <div className="grid gap-4 md:grid-cols-4">
            <div className="text-center p-4">
              <div className="text-3xl font-bold text-green-600">94%</div>
              <div className="text-sm text-muted-foreground mt-1">3-Month Accuracy</div>
            </div>
            <div className="text-center p-4">
              <div className="text-3xl font-bold text-blue-600">89%</div>
              <div className="text-sm text-muted-foreground mt-1">6-Month Accuracy</div>
            </div>
            <div className="text-center p-4">
              <div className="text-3xl font-bold">+2.3%</div>
              <div className="text-sm text-muted-foreground mt-1">Avg Variance</div>
            </div>
            <div className="text-center p-4">
              <div className="text-3xl font-bold">A-</div>
              <div className="text-sm text-muted-foreground mt-1">Overall Grade</div>
            </div>
          </div>
        </CardContent>
      </Card>
    </div>
  )
}
