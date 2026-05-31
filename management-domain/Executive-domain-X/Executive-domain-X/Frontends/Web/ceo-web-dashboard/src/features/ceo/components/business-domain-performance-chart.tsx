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
  LabelList,
} from 'recharts'
import { TrendingUp, TrendingDown, ChevronRight, Building2 } from 'lucide-react'

export interface BusinessDomainData {
  name: string
  code: string
  revenue: number
  target: number
  growth: number
  status: 'on_track' | 'at_risk' | 'behind' | 'ahead'
  services: number
  path: string
}

// Mock data based on shared-business-infrastructure domains
const mockBusinessDomains: BusinessDomainData[] = [
  {
    name: 'E-commerce',
    code: 'ECOM',
    revenue: 15.2,
    target: 14.0,
    growth: 18.5,
    status: 'ahead',
    services: 23,
    path: '/domain/ecommerce',
  },
  {
    name: 'Courier',
    code: 'COURIER',
    revenue: 8.7,
    target: 9.0,
    growth: 12.3,
    status: 'at_risk',
    services: 7,
    path: '/domain/courier',
  },
  {
    name: 'Warehousing',
    code: 'WHS',
    revenue: 5.4,
    target: 5.5,
    growth: 8.2,
    status: 'on_track',
    services: 5,
    path: '/domain/warehousing',
  },
  {
    name: 'Procurement',
    code: 'PROC',
    revenue: 4.8,
    target: 5.0,
    growth: -2.1,
    status: 'behind',
    services: 8,
    path: '/domain/procurement',
  },
  {
    name: 'Air Freight',
    code: 'AIR',
    revenue: 3.2,
    target: 3.0,
    growth: 15.8,
    status: 'ahead',
    services: 6,
    path: '/domain/air-freight',
  },
  {
    name: 'Ocean Shipping',
    code: 'OCEAN',
    revenue: 2.9,
    target: 3.2,
    growth: 5.4,
    status: 'on_track',
    services: 6,
    path: '/domain/ocean-shipping',
  },
  {
    name: 'Haulage',
    code: 'HAUL',
    revenue: 2.1,
    target: 2.5,
    growth: 3.7,
    status: 'behind',
    services: 5,
    path: '/domain/haulage',
  },
  {
    name: 'Admin Core',
    code: 'ADMIN',
    revenue: 0.3,
    target: 0.3,
    growth: 0,
    status: 'on_track',
    services: 4,
    path: '/domain/admin',
  },
]

const statusConfig = {
  on_track: { label: 'On Track', color: '#10b981', bgColor: 'bg-emerald-50 dark:bg-emerald-950/20' },
  at_risk: { label: 'At Risk', color: '#f59e0b', bgColor: 'bg-amber-50 dark:bg-amber-950/20' },
  behind: { label: 'Behind', color: '#ef4444', bgColor: 'bg-red-50 dark:bg-red-950/20' },
  ahead: { label: 'Ahead', color: '#3b82f6', bgColor: 'bg-blue-50 dark:bg-blue-950/20' },
}

interface BusinessDomainPerformanceChartProps {
  className?: string
}

export function BusinessDomainPerformanceChart({ className }: BusinessDomainPerformanceChartProps) {
  const navigate = useNavigate()
  const [sortBy, setSortBy] = useState<'revenue' | 'growth' | 'status'>('revenue')

  const sortedData = [...mockBusinessDomains].sort((a, b) => {
    if (sortBy === 'revenue') return b.revenue - a.revenue
    if (sortBy === 'growth') return b.growth - a.growth
    return 0
  })

  const formatCurrency = (value: number) => `$${value}M`

  const CustomTooltip = ({ active, payload }: any) => {
    if (active && payload && payload.length) {
      const data = payload[0].payload as BusinessDomainData
      return (
        <div className="bg-slate-900/95 dark:bg-slate-800/95 backdrop-blur-sm rounded-lg p-3 shadow-xl border border-slate-700">
          <p className="font-semibold text-white mb-2">{data.name}</p>
          <p className="text-sm text-slate-300">Revenue: <span className="text-white font-medium">${data.revenue}M</span></p>
          <p className="text-sm text-slate-300">Target: <span className="text-white font-medium">${data.target}M</span></p>
          <p className="text-sm text-slate-300">Growth: <span className={data.growth >= 0 ? 'text-emerald-400' : 'text-red-400'}>{data.growth >= 0 ? '+' : ''}{data.growth}%</span></p>
          <p className="text-sm text-slate-300">Services: <span className="text-white font-medium">{data.services}</span></p>
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
              <Building2 className="h-5 w-5 text-slate-500" />
              Business Domain Performance
            </CardTitle>
            <CardDescription className="text-sm">
              Revenue and growth metrics across all business domains
            </CardDescription>
          </div>
          <div className="flex items-center gap-2">
            <Button
              size="sm"
              variant={sortBy === 'revenue' ? 'default' : 'outline'}
              onClick={() => setSortBy('revenue')}
              className="text-xs"
            >
              By Revenue
            </Button>
            <Button
              size="sm"
              variant={sortBy === 'growth' ? 'default' : 'outline'}
              onClick={() => setSortBy('growth')}
              className="text-xs"
            >
              By Growth
            </Button>
          </div>
        </div>
      </CardHeader>
      <CardContent>
        {/* Chart */}
        <div className="h-80">
          <ResponsiveContainer width="100%" height="100%">
            <BarChart
              data={sortedData}
              margin={{ top: 20, right: 30, left: 20, bottom: 60 }}
              layout="vertical"
            >
              <CartesianGrid strokeDasharray="3 3" stroke="#e2e8f0" strokeOpacity={0.5} />
              <XAxis
                type="number"
                tick={{ fill: '#64748b', fontSize: 11 }}
                stroke="#94a3b8"
                tickFormatter={(value) => `$${value}M`}
              />
              <YAxis
                type="category"
                dataKey="name"
                tick={{ fill: '#64748b', fontSize: 11 }}
                stroke="#94a3b8"
                width={80}
              />
              <Tooltip content={<CustomTooltip />} />
              <Bar dataKey="revenue" radius={[0, 4, 4, 0]}>
                {sortedData.map((entry, index) => (
                  <Cell key={`cell-${index}`} fill={statusConfig[entry.status].color} />
                ))}
              </Bar>
              {/* Target Reference */}
              <Bar
                dataKey="target"
                fill="none"
                stroke="#94a3b8"
                strokeDasharray="3 3"
                strokeWidth={1}
              />
            </BarChart>
          </ResponsiveContainer>
        </div>

        {/* Domain Details */}
        <div className="mt-4 grid gap-2">
          {sortedData.map((domain) => {
            const status = statusConfig[domain.status]
            const percentOfTarget = Math.round((domain.revenue / domain.target) * 100)

            return (
              <button
                key={domain.code}
                onClick={() => navigate(domain.path)}
                className="group flex items-center justify-between p-3 rounded-lg border border-slate-200 dark:border-slate-700 hover:bg-slate-50 dark:hover:bg-slate-800/50 transition-colors"
              >
                <div className="flex items-center gap-3 flex-1 min-w-0">
                  <div
                    className="w-1 h-10 rounded-full"
                    style={{ backgroundColor: status.color }}
                  />
                  <div className="flex-1 min-w-0 text-left">
                    <div className="flex items-center gap-2 mb-1">
                      <span className="font-medium text-sm truncate">{domain.name}</span>
                      <Badge className={`${status.bgColor} text-xs px-1.5 py-0`}>
                        {status.label}
                      </Badge>
                    </div>
                    <div className="flex items-center gap-3 text-xs text-slate-500">
                      <span>Rev: <span className="font-medium text-slate-700">${domain.revenue}M</span></span>
                      <span>Target: <span className="font-medium text-slate-700">${domain.target}M</span></span>
                      <span className={domain.growth >= 0 ? 'text-emerald-600' : 'text-red-600'}>
                        {domain.growth >= 0 ? <TrendingUp className="h-3 w-3 inline" /> : <TrendingDown className="h-3 w-3 inline" />}
                        {domain.growth >= 0 ? '+' : ''}{domain.growth}%
                      </span>
                      <span>{domain.services} services</span>
                    </div>
                  </div>
                  <div className="text-right">
                    <div className="text-lg font-bold text-slate-900 dark:text-white">
                      {percentOfTarget}%
                    </div>
                    <div className="text-xs text-slate-500">of target</div>
                  </div>
                  <ChevronRight className="h-4 w-4 text-slate-400 group-hover:text-slate-600 transition-colors" />
                </div>
              </button>
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
