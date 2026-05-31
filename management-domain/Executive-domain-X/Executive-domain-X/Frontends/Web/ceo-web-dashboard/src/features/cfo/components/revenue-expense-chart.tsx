import { Card, CardContent, CardHeader, CardTitle, CardDescription } from '@shared/components/ui/card'
import { Button } from '@shared/components/ui/button'
import {
  LineChart,
  Line,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  ResponsiveContainer,
  Legend,
} from 'recharts'
import { cn } from '@shared/utils/cn'

export interface RevenueExpenseData {
  month: string
  revenue: number
  expenses: number
  budget: number
}

const mockData: RevenueExpenseData[] = [
  { month: 'Jan', revenue: 3800000, expenses: 3200000, budget: 4000000 },
  { month: 'Feb', revenue: 4200000, expenses: 3400000, budget: 4000000 },
  { month: 'Mar', revenue: 4100000, expenses: 3500000, budget: 4200000 },
  { month: 'Apr', revenue: 4500000, expenses: 3600000, budget: 4300000 },
  { month: 'May', revenue: 4800000, expenses: 3800000, budget: 4500000 },
  { month: 'Jun', revenue: 4700000, expenses: 3900000, budget: 4700000 },
]

const CustomTooltip = ({ active, payload }: any) => {
  if (active && payload && payload.length) {
    return (
      <div className="rounded-lg border bg-white dark:bg-slate-800 p-3 shadow-md">
        <p className="font-semibold text-sm">{payload[0].payload.month}</p>
        {payload.map((entry: any, index: number) => (
          <p key={index} className="text-xs" style={{ color: entry.color }}>
            {entry.name}: ${(entry.value / 1000000).toFixed(1)}M
          </p>
        ))}
      </div>
    )
  }
  return null
}

export interface RevenueExpenseChartProps {
  data?: RevenueExpenseData[]
  period?: 'quarter' | 'half' | 'year'
  onPeriodChange?: (period: 'quarter' | 'half' | 'year') => void
  className?: string
}

export function RevenueExpenseChart({
  data = mockData,
  period = 'quarter',
  onPeriodChange,
  className,
}: RevenueExpenseChartProps) {
  return (
    <Card className={className}>
      <CardHeader className="pb-3">
        <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-3">
          <div>
            <CardTitle className="text-lg">Revenue & Expenses</CardTitle>
            <CardDescription className="text-sm">Monthly breakdown with budget comparison</CardDescription>
          </div>
          <div className="flex gap-1">
            {(['quarter', 'half', 'year'] as const).map((p) => (
              <Button
                key={p}
                variant={period === p ? 'default' : 'outline'}
                size="sm"
                onClick={() => onPeriodChange?.(p)}
                className="capitalize text-xs min-w-fit"
              >
                {p}
              </Button>
            ))}
          </div>
        </div>
      </CardHeader>

      <CardContent>
        <ResponsiveContainer width="100%" height={220} minHeight={200}>
          <LineChart
            data={data}
            margin={{ top: 10, right: 10, left: -10, bottom: 0 }}
          >
            <CartesianGrid strokeDasharray="3 3" className="stroke-muted" opacity={0.3} />
            <XAxis
              dataKey="month"
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
            <Legend />
            <Line
              type="monotone"
              dataKey="revenue"
              stroke="#10b981"
              strokeWidth={2}
              dot={{ fill: '#10b981', r: 4 }}
              activeDot={{ r: 6 }}
              name="Revenue"
            />
            <Line
              type="monotone"
              dataKey="expenses"
              stroke="#f59e0b"
              strokeWidth={2}
              dot={{ fill: '#f59e0b', r: 4 }}
              activeDot={{ r: 6 }}
              name="Expenses"
            />
            <Line
              type="monotone"
              dataKey="budget"
              stroke="#94a3b8"
              strokeWidth={2}
              strokeDasharray="5 5"
              dot={false}
              name="Budget"
            />
          </LineChart>
        </ResponsiveContainer>
      </CardContent>
    </Card>
  )
}
