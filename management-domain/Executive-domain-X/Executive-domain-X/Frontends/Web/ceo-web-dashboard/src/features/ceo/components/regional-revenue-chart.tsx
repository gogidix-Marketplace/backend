import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { Card, CardContent, CardHeader, CardTitle, CardDescription } from '@shared/components/ui/card'
import { Button } from '@shared/components/ui/button'
import { Badge } from '@shared/components/ui/badge'
import {
  BarChart,
  Bar,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  ResponsiveContainer,
  Cell,
  PieChart,
  Pie,
  Sector,
} from 'recharts'
import { Globe2, TrendingUp, TrendingDown, MapPin } from 'lucide-react'
import * as React from 'react'

export interface RegionalData {
  country: string
  code: string
  revenue: number
  growth: number
  status: 'growing' | 'stable' | 'declining'
  domains: number
  flag: string
}

// Mock data for regions served by Gogidix
const mockRegionalData: RegionalData[] = [
  {
    country: 'Nigeria',
    code: 'NG',
    revenue: 12.5,
    growth: 22.3,
    status: 'growing',
    domains: 8,
    flag: '🇳🇬',
  },
  {
    country: 'Kenya',
    code: 'KE',
    revenue: 8.2,
    growth: 18.5,
    status: 'growing',
    domains: 7,
    flag: '🇰🇪',
  },
  {
    country: 'South Africa',
    code: 'ZA',
    revenue: 7.8,
    growth: -2.1,
    status: 'declining',
    domains: 6,
    flag: '🇿🇦',
  },
  {
    country: 'Ghana',
    code: 'GH',
    revenue: 5.4,
    growth: 15.2,
    status: 'growing',
    domains: 6,
    flag: '🇬🇭',
  },
  {
    country: 'United Kingdom',
    code: 'UK',
    revenue: 4.8,
    growth: 8.5,
    status: 'stable',
    domains: 5,
    flag: '🇬🇧',
  },
  {
    country: 'United States',
    code: 'US',
    revenue: 2.6,
    growth: 12.1,
    status: 'growing',
    domains: 4,
    flag: '🇺🇸',
  },
  {
    country: 'UAE',
    code: 'AE',
    revenue: 1.8,
    growth: -5.2,
    status: 'declining',
    domains: 3,
    flag: '🇦🇪',
  },
]

const statusConfig = {
  growing: { label: 'Growing', color: '#10b981', bgColor: 'bg-emerald-50 dark:bg-emerald-950/20' },
  stable: { label: 'Stable', color: '#3b82f6', bgColor: 'bg-blue-50 dark:bg-blue-950/20' },
  declining: { label: 'Declining', color: '#ef4444', bgColor: 'bg-red-50 dark:bg-red-950/20' },
}

interface RegionalRevenueChartProps {
  className?: string
  view?: 'bar' | 'pie'
}

export function RegionalRevenueChart({ className, view = 'bar' }: RegionalRevenueChartProps) {
  const [chartView, setChartView] = useState<'bar' | 'pie'>(view)
  const [activeIndex, setActiveIndex] = useState<number | undefined>(undefined)

  // Prepare data for pie chart
  const pieData = mockRegionalData.map(item => ({
    name: item.country,
    value: item.revenue,
    flag: item.flag,
    color: statusConfig[item.status].color,
  }))

  const onPieEnter = (_: any, index: number) => {
    setActiveIndex(index)
  }

  const formatCurrency = (value: number) => `$${value}M`

  const CustomBarTooltip = ({ active, payload }: any) => {
    if (active && payload && payload.length) {
      const data = payload[0].payload as RegionalData
      return (
        <div className="bg-slate-900/95 dark:bg-slate-800/95 backdrop-blur-sm rounded-lg p-3 shadow-xl border border-slate-700">
          <div className="flex items-center gap-2 mb-2">
            <span className="text-xl">{data.flag}</span>
            <p className="font-semibold text-white">{data.country}</p>
          </div>
          <p className="text-sm text-slate-300">Revenue: <span className="text-white font-medium">${data.revenue}M</span></p>
          <p className="text-sm text-slate-300">Growth: <span className={data.growth >= 0 ? 'text-emerald-400' : 'text-red-400'}>{data.growth >= 0 ? '+' : ''}{data.growth}%</span></p>
          <p className="text-sm text-slate-300">Active Domains: <span className="text-white font-medium">{data.domains}</span></p>
        </div>
      )
    }
    return null
  }

  return (
    <Card className={className}>
      <CardHeader>
        <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
          <div>
            <CardTitle className="text-lg sm:text-xl flex items-center gap-2">
              <Globe2 className="h-5 w-5 text-slate-500" />
              Regional Revenue Breakdown
            </CardTitle>
            <CardDescription className="text-sm">
              Performance across {mockRegionalData.length} countries
            </CardDescription>
          </div>
          <div className="flex items-center gap-2">
            <Button
              size="sm"
              variant={chartView === 'bar' ? 'default' : 'outline'}
              onClick={() => setChartView('bar')}
            >
              Bar Chart
            </Button>
            <Button
              size="sm"
              variant={chartView === 'pie' ? 'default' : 'outline'}
              onClick={() => setChartView('pie')}
            >
              Pie Chart
            </Button>
          </div>
        </div>
      </CardHeader>
      <CardContent>
        {/* Summary Stats */}
        <div className="grid grid-cols-2 sm:grid-cols-4 gap-3 mb-6">
          <div className="text-center p-3 rounded-lg bg-emerald-50 dark:bg-emerald-950/20">
            <p className="text-2xl font-bold text-emerald-700 dark:text-emerald-400">
              {mockRegionalData.filter(d => d.status === 'growing').length}
            </p>
            <p className="text-xs text-emerald-600 dark:text-emerald-500">Growing</p>
          </div>
          <div className="text-center p-3 rounded-lg bg-blue-50 dark:bg-blue-950/20">
            <p className="text-2xl font-bold text-blue-700 dark:text-blue-400">
              {mockRegionalData.filter(d => d.status === 'stable').length}
            </p>
            <p className="text-xs text-blue-600 dark:text-blue-500">Stable</p>
          </div>
          <div className="text-center p-3 rounded-lg bg-red-50 dark:bg-red-950/20">
            <p className="text-2xl font-bold text-red-700 dark:text-red-400">
              {mockRegionalData.filter(d => d.status === 'declining').length}
            </p>
            <p className="text-xs text-red-600 dark:text-red-500">Declining</p>
          </div>
          <div className="text-center p-3 rounded-lg bg-slate-100 dark:bg-slate-800">
            <p className="text-2xl font-bold text-slate-700 dark:text-slate-300">
              ${mockRegionalData.reduce((sum, d) => sum + d.revenue, 0).toFixed(1)}M
            </p>
            <p className="text-xs text-slate-600 dark:text-slate-400">Total Revenue</p>
          </div>
        </div>

        {/* Chart */}
        {chartView === 'bar' ? (
          <div className="h-72">
            <ResponsiveContainer width="100%" height="100%">
              <BarChart
                data={mockRegionalData}
                margin={{ top: 20, right: 30, left: 20, bottom: 60 }}
              >
                <CartesianGrid strokeDasharray="3 3" stroke="#e2e8f0" strokeOpacity={0.5} />
                <XAxis
                  dataKey="code"
                  tick={{ fill: '#64748b', fontSize: 11 }}
                  stroke="#94a3b8"
                  angle={-45}
                  textAnchor="end"
                  height={60}
                />
                <YAxis
                  tick={{ fill: '#64748b', fontSize: 11 }}
                  stroke="#94a3b8"
                  tickFormatter={(value) => `$${value}M`}
                />
                <Tooltip content={<CustomBarTooltip />} />
                <Bar dataKey="revenue" radius={[4, 4, 0, 0]}>
                  {mockRegionalData.map((entry, index) => (
                    <Cell key={`cell-${index}`} fill={statusConfig[entry.status].color} />
                  ))}
                </Bar>
              </BarChart>
            </ResponsiveContainer>
          </div>
        ) : (
          <div className="h-72">
            <ResponsiveContainer width="100%" height="100%">
              <PieChart>
                <Pie
                  data={pieData}
                  cx="50%"
                  cy="50%"
                  labelLine={false}
                  label={({ name, percent }) => `${name} ${(percent * 100).toFixed(0)}%`}
                  outerRadius={80}
                  fill="#8884d8"
                  dataKey="value"
                  onMouseEnter={onPieEnter}
                  onMouseLeave={() => setActiveIndex(undefined)}
                >
                  {pieData.map((entry, index) => (
                    <Sector
                      key={`sector-${index}`}
                      fill={entry.color}
                      opacity={activeIndex === undefined || activeIndex === index ? 1 : 0.5}
                    />
                  ))}
                </Pie>
                <Tooltip
                  contentStyle={{
                    backgroundColor: 'rgba(15, 23, 42, 0.95)',
                    border: 'none',
                    borderRadius: '8px',
                    color: '#fff',
                  }}
                  formatter={(value: number, name: string, props: any) => {
                    return [`$${value}M`, props.payload.flag + ' ' + name]
                  }}
                />
              </PieChart>
            </ResponsiveContainer>
          </div>
        )}

        {/* Regional Details List */}
        <div className="mt-4 space-y-2 max-h-64 overflow-y-auto">
          {mockRegionalData.map((region) => {
            const status = statusConfig[region.status]
            const marketShare = ((region.revenue / mockRegionalData.reduce((sum, d) => sum + d.revenue, 0)) * 100).toFixed(1)

            return (
              <div
                key={region.code}
                className="flex items-center justify-between p-3 rounded-lg border border-slate-200 dark:border-slate-700 hover:bg-slate-50 dark:hover:bg-slate-800/50 transition-colors"
              >
                <div className="flex items-center gap-3">
                  <span className="text-2xl">{region.flag}</span>
                  <div>
                    <div className="flex items-center gap-2">
                      <span className="font-medium text-sm">{region.country}</span>
                      <span className="text-xs text-slate-500">({region.code})</span>
                      <Badge className={`${status.bgColor} text-xs px-1.5 py-0`}>
                        {status.label}
                      </Badge>
                    </div>
                    <div className="flex items-center gap-3 text-xs text-slate-500 mt-1">
                      <span className="font-medium text-slate-700">${region.revenue}M</span>
                      <span className={region.growth >= 0 ? 'text-emerald-600' : 'text-red-600'}>
                        {region.growth >= 0 ? <TrendingUp className="h-3 w-3 inline" /> : <TrendingDown className="h-3 w-3 inline" />}
                        {region.growth >= 0 ? '+' : ''}{region.growth}%
                      </span>
                      <span className="flex items-center gap-1">
                        <MapPin className="h-3 w-3" />
                        {region.domains} domains
                      </span>
                    </div>
                  </div>
                </div>
                <div className="text-right">
                  <div className="text-lg font-bold text-slate-900 dark:text-white">
                    {marketShare}%
                  </div>
                  <div className="text-xs text-slate-500">market share</div>
                </div>
              </div>
            )
          })}
        </div>

        {/* Legend */}
        <div className="mt-4 flex flex-wrap gap-3 justify-center">
          {Object.entries(statusConfig).map(([key, config]) => (
            <div key={key} className="flex items-center gap-1.5">
              <div
                className="w-3 h-3 rounded-full"
                style={{ backgroundColor: config.color }}
              />
              <span className="text-xs text-slate-600 dark:text-slate-400">{config.label}</span>
            </div>
          ))}
        </div>
      </CardContent>
    </Card>
  )
}
