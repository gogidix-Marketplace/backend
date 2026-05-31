import { Card, CardContent, CardHeader, CardTitle } from '@shared/components/ui/card'
import { Badge } from '@shared/components/ui/badge'
import { Button } from '@shared/components/ui/button'
import { BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer } from 'recharts'
import { cn } from '@shared/utils/cn'
import { Calendar } from 'lucide-react'

/**
 * CrossDomainPerformanceChart Component
 *
 * Grouped bar chart showing revenue by domain
 * Color-coded by performance status
 * Hover tooltips with detailed metrics
 * Click to navigate to domain detail
 * Date range filter (today/week/month/quarter)
 */

export interface DomainPerformance {
  domain: string
  revenue: number
  target: number
  variance: number
  status: 'on_track' | 'at_risk' | 'behind'
}

export interface CrossDomainChartProps {
  data?: DomainPerformance[]
  period?: 'today' | 'week' | 'month' | 'quarter'
  onPeriodChange?: (period: 'today' | 'week' | 'month' | 'quarter') => void
  onDomainClick?: (domain: string) => void
  loading?: boolean
}

const defaultData: DomainPerformance[] = [
  { domain: 'Executive', revenue: 8500000, target: 10000000, variance: -15, status: 'at_risk' },
  { domain: 'Business', revenue: 15000000, target: 15000000, variance: 0, status: 'on_track' },
  { domain: 'Public', revenue: 8000000, target: 8000000, variance: 0, status: 'on_track' },
  { domain: 'Management', revenue: 7000000, target: 8000000, variance: -12.5, status: 'behind' },
  { domain: 'Foundation', revenue: 4000000, target: 4000000, variance: 0, status: 'on_track' },
]

const periods = [
  { value: 'today', label: 'Today' },
  { value: 'week', label: 'This Week' },
  { value: 'month', label: 'This Month' },
  { value: 'quarter', label: 'This Quarter' },
]

const statusColors = {
  on_track: '#22c55e',
  at_risk: '#f59e0b',
  behind: '#ef4444',
}

export function CrossDomainPerformanceChart({
  data = defaultData,
  period = 'month',
  onPeriodChange,
  onDomainClick,
  loading = false,
}: CrossDomainChartProps) {
  // Format data for Recharts
  const chartData = data.map((item) => ({
    domain: item.domain,
    revenue: item.revenue / 1000000, // Convert to millions
    target: item.target / 1000000,
  }))

  const CustomBar = (props: any) => {
    const { x, y, width, height, payload } = props
    const dataPoint = data.find((d) => d.domain === payload.domain)
    const color = dataPoint ? statusColors[dataPoint.status] : '#22c55e'

    return (
      <rect
        x={x}
        y={y}
        width={width}
        height={height}
        fill={color}
        radius={[4, 4, 0, 0]}
        className="transition-opacity hover:opacity-80 cursor-pointer"
        onClick={() => onDomainClick?.(payload.domain)}
      />
    )
  }

  return (
    <Card>
      <CardHeader>
        <div className="flex items-center justify-between">
          <CardTitle>Cross-Domain Performance</CardTitle>
          <div className="flex items-center gap-2">
            <Calendar className="h-4 w-4 text-muted-foreground" />
            <div className="flex gap-1">
              {periods.map((p) => (
                <Button
                  key={p.value}
                  variant={period === p.value ? 'default' : 'ghost'}
                  size="sm"
                  className="h-7 text-xs"
                  onClick={() => onPeriodChange?.(p.value as any)}
                >
                  {p.label}
                </Button>
              ))}
            </div>
          </div>
        </div>
      </CardHeader>
      <CardContent>
        {loading ? (
          <div className="h-64 flex items-center justify-center">
            <div className="h-8 w-8 animate-spin rounded-full border-4 border-primary border-t-transparent" />
          </div>
        ) : (
          <ResponsiveContainer width="100%" height={250}>
            <BarChart data={chartData} margin={{ top: 20, right: 30, left: 20, bottom: 5 }}>
              <CartesianGrid strokeDasharray="3 3" className="stroke-muted" />
              <XAxis
                dataKey="domain"
                tick={{ fill: 'hsl(var(--muted-foreground))', fontSize: 12 }}
              />
              <YAxis
                tick={{ fill: 'hsl(var(--muted-foreground))', fontSize: 12 }}
                tickFormatter={(value) => `$${value}M`}
              />
              <Tooltip
                contentStyle={{
                  backgroundColor: 'hsl(var(--background))',
                  border: '1px solid hsl(var(--border))',
                  borderRadius: '8px',
                }}
                formatter={(value: number, name: string) => {
                  if (name === 'revenue') return [`$${value.toFixed(1)}M`, 'Revenue']
                  if (name === 'target') return [`$${value.toFixed(1)}M`, 'Target']
                  return [value, name]
                }}
              />
              <Legend />
              <Bar dataKey="revenue" name="Revenue" shape={<CustomBar />} radius={[4, 4, 0, 0]} />
              <Bar dataKey="target" name="Target" fill="hsl(var(--muted))" radius={[4, 4, 0, 0]} />
            </BarChart>
          </ResponsiveContainer>
        )}

        {/* Legend */}
        <div className="flex items-center justify-center gap-6 mt-4">
          <div className="flex items-center gap-2">
            <div className="h-3 w-3 rounded-full bg-green-500" />
            <span className="text-xs text-muted-foreground">On Track</span>
          </div>
          <div className="flex items-center gap-2">
            <div className="h-3 w-3 rounded-full bg-amber-500" />
            <span className="text-xs text-muted-foreground">At Risk</span>
          </div>
          <div className="flex items-center gap-2">
            <div className="h-3 w-3 rounded-full bg-red-500" />
            <span className="text-xs text-muted-foreground">Behind</span>
          </div>
        </div>
      </CardContent>
    </Card>
  )
}
