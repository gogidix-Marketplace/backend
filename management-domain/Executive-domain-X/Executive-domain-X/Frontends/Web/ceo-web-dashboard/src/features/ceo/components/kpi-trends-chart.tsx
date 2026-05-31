import { useState } from 'react'
import { Card, CardContent, CardHeader, CardTitle, CardDescription } from '@shared/components/ui/card'
import { Button } from '@shared/components/ui/button'
import { Badge } from '@shared/components/ui/badge'
import {
  LineChart,
  Line,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  ResponsiveContainer,
  Legend,
  ReferenceLine,
} from 'recharts'
import { TrendingUp, Calendar, Download } from 'lucide-react'

export interface KPITrendData {
  month: string
  revenue: number
  growth: number
  domains: number
  countries: number
  users: number
}

// Mock data based on shared-business-infrastructure domains
const mockTrendData: KPITrendData[] = [
  { month: 'Oct 2025', revenue: 38.2, growth: 21.5, domains: 7, countries: 14, users: 1.05 },
  { month: 'Nov 2025', revenue: 39.8, growth: 22.3, domains: 7, countries: 14, users: 1.10 },
  { month: 'Dec 2025', revenue: 40.5, growth: 23.1, domains: 7, countries: 14, users: 1.15 },
  { month: 'Jan 2026', revenue: 41.2, growth: 23.8, domains: 8, countries: 15, users: 1.20 },
  { month: 'Feb 2026', revenue: 41.8, growth: 24.2, domains: 8, countries: 15, users: 1.22 },
  { month: 'Mar 2026', revenue: 42.5, growth: 24.5, domains: 8, countries: 15, users: 1.25 },
]

const kpiConfig = {
  revenue: { name: 'Revenue', color: '#10b981', unit: 'M $', yAxisId: 'left' },
  growth: { name: 'Growth Rate', color: '#3b82f6', unit: '%', yAxisId: 'right' },
  domains: { name: 'Active Domains', color: '#8b5cf6', unit: '', yAxisId: 'left' },
  countries: { name: 'Countries', color: '#f59e0b', unit: '', yAxisId: 'left' },
  users: { name: 'Active Users', color: '#ec4899', unit: 'M', yAxisId: 'left' },
}

interface KPITrendsChartProps {
  className?: string
  dateRange?: '3m' | '6m' | '12m'
}

export function KPITrendsChart({ className, dateRange = '6m' }: KPITrendsChartProps) {
  const [visibleSeries, setVisibleSeries] = useState<Set<keyof typeof kpiConfig>>(
    new Set(['revenue', 'growth', 'domains', 'countries', 'users'])
  )

  const toggleSeries = (key: keyof typeof kpiConfig) => {
    const newVisible = new Set(visibleSeries)
    if (newVisible.has(key)) {
      if (newVisible.size > 1) {
        // Keep at least one series visible
        newVisible.delete(key)
      }
    } else {
      newVisible.add(key)
    }
    setVisibleSeries(newVisible)
  }

  const formatTooltipValue = (value: number, name: string) => {
    const config = Object.values(kpiConfig).find(c => c.name === name)
    const unit = config?.unit || ''
    return `${value}${unit}`
  }

  const CustomLegend = () => (
    <div className="flex flex-wrap gap-2 justify-center mt-4">
      {Object.entries(kpiConfig).map(([key, config]) => {
        const isVisible = visibleSeries.has(key as keyof typeof kpiConfig)
        return (
          <button
            key={key}
            onClick={() => toggleSeries(key as keyof typeof kpiConfig)}
            className={`flex items-center gap-1.5 px-3 py-1.5 rounded-full text-xs font-medium transition-all ${
              isVisible
                ? 'bg-slate-100 dark:bg-slate-800 text-slate-700 dark:text-slate-300'
                : 'bg-slate-50 dark:bg-slate-900 text-slate-400 dark:text-slate-600 line-through'
            }`}
          >
            <span
              className="w-2.5 h-2.5 rounded-full"
              style={{ backgroundColor: config.color }}
            />
            {config.name}
          </button>
        )
      })}
    </div>
  )

  return (
    <Card className={className}>
      <CardHeader>
        <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
          <div>
            <CardTitle className="text-lg sm:text-xl">Strategic KPI Trends</CardTitle>
            <CardDescription className="text-sm">
              6-month performance trajectory across all key metrics
            </CardDescription>
          </div>
          <div className="flex items-center gap-2">
            <Button size="sm" variant="outline" className="gap-2">
              <Calendar className="h-4 w-4" />
              {dateRange === '3m' ? '3 Months' : dateRange === '6m' ? '6 Months' : '1 Year'}
            </Button>
            <Button size="sm" variant="outline" className="gap-2">
              <Download className="h-4 w-4" />
              Export
            </Button>
          </div>
        </div>
      </CardHeader>
      <CardContent>
        {/* Summary Stats */}
        <div className="grid grid-cols-2 sm:grid-cols-5 gap-3 mb-6">
          {Object.entries(kpiConfig).map(([key, config]) => {
            const latestValue = mockTrendData[mockTrendData.length - 1][key as keyof KPITrendData] as number
            const previousValue = mockTrendData[mockTrendData.length - 2][key as keyof KPITrendData] as number
            const change = ((latestValue - previousValue) / previousValue * 100).toFixed(1)
            const isPositive = parseFloat(change) >= 0

            return (
              <div key={key} className="text-center p-2 rounded-lg bg-slate-50 dark:bg-slate-800/50">
                <div
                  className="w-3 h-3 rounded-full mx-auto mb-1"
                  style={{ backgroundColor: config.color }}
                />
                <p className="text-xs text-slate-500 dark:text-slate-400">{config.name}</p>
                <p className="text-sm font-bold text-slate-900 dark:text-white">
                  {latestValue}{config.unit}
                </p>
                <div className={`flex items-center justify-center gap-0.5 text-xs ${
                  isPositive ? 'text-emerald-600' : 'text-red-600'
                }`}>
                  <TrendingUp className="h-2.5 w-2.5" />
                  {isPositive ? '+' : ''}{change}%
                </div>
              </div>
            )
          })}
        </div>

        {/* Chart */}
        <div className="h-80">
          <ResponsiveContainer width="100%" height="100%">
            <LineChart data={mockTrendData} margin={{ top: 5, right: 20, left: 10, bottom: 5 }}>
              <CartesianGrid strokeDasharray="3 3" stroke="#e2e8f0" strokeOpacity={0.5} />
              <XAxis
                dataKey="month"
                tick={{ fill: '#64748b', fontSize: 12 }}
                stroke="#94a3b8"
              />
              <YAxis
                yAxisId="left"
                tick={{ fill: '#64748b', fontSize: 12 }}
                stroke="#94a3b8"
              />
              <YAxis
                yAxisId="right"
                orientation="right"
                tick={{ fill: '#64748b', fontSize: 12 }}
                stroke="#94a3b8"
              />
              <Tooltip
                contentStyle={{
                  backgroundColor: 'rgba(15, 23, 42, 0.95)',
                  border: 'none',
                  borderRadius: '8px',
                  color: '#fff',
                  fontSize: '12px',
                  padding: '12px',
                }}
                formatter={formatTooltipValue}
                labelStyle={{ color: '#94a3b8' }}
              />
              <ReferenceLine yAxisId="left" y={0} stroke="#94a3b8" strokeDasharray="2 2" />

              {/* Revenue Line */}
              {visibleSeries.has('revenue') && (
                <Line
                  yAxisId="left"
                  type="monotone"
                  dataKey="revenue"
                  stroke={kpiConfig.revenue.color}
                  strokeWidth={2.5}
                  dot={{ fill: kpiConfig.revenue.color, r: 4 }}
                  activeDot={{ r: 6, stroke: kpiConfig.revenue.color, strokeWidth: 2 }}
                />
              )}

              {/* Growth Line */}
              {visibleSeries.has('growth') && (
                <Line
                  yAxisId="right"
                  type="monotone"
                  dataKey="growth"
                  stroke={kpiConfig.growth.color}
                  strokeWidth={2.5}
                  dot={{ fill: kpiConfig.growth.color, r: 4 }}
                  activeDot={{ r: 6, stroke: kpiConfig.growth.color, strokeWidth: 2 }}
                  strokeDasharray="5 5"
                />
              )}

              {/* Domains Line */}
              {visibleSeries.has('domains') && (
                <Line
                  yAxisId="left"
                  type="monotone"
                  dataKey="domains"
                  stroke={kpiConfig.domains.color}
                  strokeWidth={2.5}
                  dot={{ fill: kpiConfig.domains.color, r: 4 }}
                  activeDot={{ r: 6, stroke: kpiConfig.domains.color, strokeWidth: 2 }}
                />
              )}

              {/* Countries Line */}
              {visibleSeries.has('countries') && (
                <Line
                  yAxisId="left"
                  type="monotone"
                  dataKey="countries"
                  stroke={kpiConfig.countries.color}
                  strokeWidth={2.5}
                  dot={{ fill: kpiConfig.countries.color, r: 4 }}
                  activeDot={{ r: 6, stroke: kpiConfig.countries.color, strokeWidth: 2 }}
                />
              )}

              {/* Users Line */}
              {visibleSeries.has('users') && (
                <Line
                  yAxisId="left"
                  type="monotone"
                  dataKey="users"
                  stroke={kpiConfig.users.color}
                  strokeWidth={2.5}
                  dot={{ fill: kpiConfig.users.color, r: 4 }}
                  activeDot={{ r: 6, stroke: kpiConfig.users.color, strokeWidth: 2 }}
                />
              )}
            </LineChart>
          </ResponsiveContainer>
        </div>

        {/* Custom Legend */}
        <CustomLegend />

        {/* Insight Badge */}
        <div className="mt-4 flex items-center justify-center gap-2">
          <Badge className="bg-emerald-50 dark:bg-emerald-950/20 text-emerald-700 dark:text-emerald-400 border-emerald-200 dark:border-emerald-800">
            <TrendingUp className="h-3 w-3 mr-1" />
            All metrics trending upward
          </Badge>
          <Badge variant="outline" className="text-slate-500">
            Last updated: 2 hours ago
          </Badge>
        </div>
      </CardContent>
    </Card>
  )
}
