import { Card, CardContent, CardHeader, CardTitle, CardDescription } from '@shared/components/ui/card'
import { Button } from '@shared/components/ui/button'
import {
  BarChart,
  Bar,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  ResponsiveContainer,
  Cell,
} from 'recharts'
import { useCrossDomainData } from '@shared/services/api'

export interface DomainData {
  domain: string
  revenue: number
  target: number
  status: 'on_track' | 'at_risk' | 'behind' | 'ahead'
  growth: number
}

// Fallback mock data for when API is unavailable
const fallbackData: DomainData[] = [
  { domain: 'Executive', revenue: 4200000, target: 5000000, status: 'on_track', growth: 8.5 },
  { domain: 'Business', revenue: 12500000, target: 12000000, status: 'ahead', growth: 15.2 },
  { domain: 'Public', revenue: 8900000, target: 10000000, status: 'at_risk', growth: -2.3 },
  { domain: 'Management', revenue: 7600000, target: 8000000, status: 'on_track', growth: 5.8 },
  { domain: 'Foundation', revenue: 9300000, target: 10000000, status: 'behind', growth: -5.1 },
]

const statusColors = {
  on_track: '#10b981',
  at_risk: '#f59e0b',
  behind: '#ef4444',
  ahead: '#3b82f6',
}

/**
 * Transform API cross-domain data to DomainData format
 */
function transformApiDomainData(apiData: any): DomainData {
  const revenue = apiData.revenue ?? apiData.totalRevenue ?? apiData.currentValue ?? 0
  const target = apiData.target ?? apiData.targetRevenue ?? apiData.budget ?? revenue * 1.1
  const growth = apiData.growth ?? apiData.growthRate ?? apiData.change ?? 0

  // Map status
  const statusMap: Record<string, DomainData['status']> = {
    on_track: 'on_track',
    ontrack: 'on_track',
    at_risk: 'at_risk',
    behind: 'behind',
    ahead: 'ahead',
    healthy: 'ahead',
    warning: 'at_risk',
    critical: 'behind',
  }
  const status = statusMap[apiData.status?.toLowerCase()] || 'on_track'

  return {
    domain: apiData.domain ?? apiData.name ?? 'Unknown',
    revenue,
    target,
    status,
    growth,
  }
}

export interface CrossDomainChartProps {
  onBarClick?: (domain: string) => void
  dateRange?: 'today' | 'week' | 'month' | 'quarter'
  onDateRangeChange?: (range: 'today' | 'week' | 'month' | 'quarter') => void
  className?: string
  /**
   * Force use of mock data (for testing/demos)
   * @default false - will try API first, fall back to mock on error
   */
  useMockData?: boolean
}

export function CrossDomainPerformanceChart({
  onBarClick,
  dateRange = 'quarter',
  onDateRangeChange,
  className,
  useMockData = false,
}: CrossDomainChartProps) {
  // Fetch cross-domain data from API
  const { data: crossDomainData, loading } = useCrossDomainData(
    {}, // Empty params object to get all domains
    useMockData ? undefined : 60000
  )

  // Transform API data or use fallback
  let data: DomainData[] = fallbackData

  if (!useMockData && crossDomainData?.domains) {
    data = crossDomainData.domains.map(transformApiDomainData)
  } else if (!useMockData && crossDomainData?.data?.domains) {
    data = crossDomainData.data.domains.map(transformApiDomainData)
  }

  const CustomTooltip = ({ active, payload }: any) => {
    if (active && payload && payload.length) {
      const d = payload[0].payload
      return (
        <div className="rounded-lg border bg-white dark:bg-slate-800 p-3 shadow-md">
          <p className="font-semibold">{d.domain}</p>
          <p className="text-sm text-muted-foreground">Revenue: ${(d.revenue / 1000000).toFixed(1)}M</p>
          <p className="text-sm text-muted-foreground">Target: ${(d.target / 1000000).toFixed(1)}M</p>
          <p className={`text-sm font-medium ${d.growth >= 0 ? 'text-emerald-600' : 'text-red-600'}`}>
            {d.growth >= 0 ? '+' : ''}{d.growth}%
          </p>
        </div>
      )
    }
    return null
  }

  return (
    <Card className={className}>
      <CardHeader className="pb-3">
        <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-3">
          <div>
            <CardTitle className="text-lg">Cross-Domain Performance</CardTitle>
            <CardDescription className="text-sm">
              {loading && !useMockData ? 'Loading...' : 'Revenue by domain with status indicators'}
            </CardDescription>
          </div>
          <div className="flex gap-1 overflow-x-auto pb-1">
            {(['today', 'week', 'month', 'quarter'] as const).map((range) => (
              <Button
                key={range}
                variant={dateRange === range ? 'default' : 'outline'}
                size="sm"
                onClick={() => onDateRangeChange?.(range)}
                className="capitalize text-xs min-w-fit"
              >
                {range}
              </Button>
            ))}
          </div>
        </div>
      </CardHeader>
      <CardContent>
        {loading && !useMockData ? (
          <div className="h-[220px] flex items-center justify-center">
            <div className="animate-pulse text-muted-foreground">Loading chart data...</div>
          </div>
        ) : (
          <ResponsiveContainer width="100%" height={220} minHeight={200}>
            <BarChart
              data={data}
              margin={{ top: 10, right: 10, left: -10, bottom: 0 }}
              onClick={(e) => {
                if (e.activePayload && onBarClick) {
                  onBarClick(e.activePayload[0].payload.domain)
                }
              }}
            >
              <CartesianGrid strokeDasharray="3 3" className="stroke-muted" opacity={0.3} />
              <XAxis
                dataKey="domain"
                tick={{ fill: 'hsl(var(--muted-foreground))', fontSize: 12 }}
                tickLine={false}
                axisLine={false}
              />
              <YAxis
                tick={{ fill: 'hsl(var(--muted-foreground))', fontSize: 11 }}
                tickLine={false}
                axisLine={false}
                tickFormatter={(value) => `$${(value / 1000000).toFixed(0)}M`}
              />
              <Tooltip content={<CustomTooltip />} />
              <Bar
                dataKey="revenue"
                radius={[4, 4, 0, 0]}
                className="cursor-pointer hover:opacity-80 transition-opacity"
              >
                {data.map((entry, index) => (
                  <Cell key={`cell-${index}`} fill={statusColors[entry.status]} />
                ))}
              </Bar>
            </BarChart>
          </ResponsiveContainer>
        )}

        {/* Legend */}
        <div className="flex flex-wrap gap-3 mt-3 pt-3 border-t">
          {Object.entries(statusColors).map(([status, color]) => (
            <div key={status} className="flex items-center gap-1.5">
              <div className="h-2.5 w-2.5 rounded-sm" style={{ backgroundColor: color }} />
              <span className="text-xs text-muted-foreground capitalize">{status.replace('_', ' ')}</span>
            </div>
          ))}
        </div>
      </CardContent>
    </Card>
  )
}
