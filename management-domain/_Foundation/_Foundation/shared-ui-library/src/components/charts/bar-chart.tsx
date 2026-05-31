import * as React from 'react'
import {
  BarChart as RechartsBarChart,
  Bar,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  Legend,
  ResponsiveContainer,
  Cell,
} from 'recharts'
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card'
import { cn } from '@/lib/utils'

export interface ChartData {
  name: string
  [key: string]: string | number
}

export interface BarChartProps {
  data: ChartData[]
  bars: {
    dataKey: string
    name: string
    color: string
  }[]
  title?: string
  description?: string
  className?: string
  height?: number
  showGrid?: boolean
  showLegend?: boolean
  horizontal?: boolean
  xAxisKey?: string
  colors?: string[]
}

const DEFAULT_COLORS = [
  '#0D47A1', '#FFA000', '#4CAF50', '#F44336', '#9C27B0',
  '#00BCD4', '#FF9800', '#795548', '#607D8B', '#8BC34A'
]

export function BarChart({
  data,
  bars,
  title,
  description,
  className,
  height = 300,
  showGrid = true,
  showLegend = true,
  horizontal = false,
  xAxisKey = 'name',
  colors,
}: BarChartProps) {
  const barColors = colors || DEFAULT_COLORS

  const CustomTooltip = ({ active, payload, label }: any) => {
    if (active && payload && payload.length) {
      return (
        <div className="rounded-lg border bg-background p-3 shadow-md">
          <p className="text-sm font-medium text-muted-foreground">{label}</p>
          {payload.map((entry: any, index: number) => (
            <p key={index} className="text-sm" style={{ color: entry.color }}>
              {entry.name}: {entry.value}
            </p>
          ))}
        </div>
      )
    }
    return null
  }

  const chartContent = (
    <ResponsiveContainer width="100%" height={height}>
      <RechartsBarChart
        data={data}
        layout={horizontal ? 'vertical' : 'horizontal'}
        margin={{ top: 10, right: 10, left: 0, bottom: 0 }}
      >
        {showGrid && <CartesianGrid strokeDasharray="3 3" className="stroke-muted" />}
        <XAxis
          dataKey={horizontal ? undefined : xAxisKey}
          type={horizontal ? 'number' : 'category'}
          className="text-xs text-muted-foreground"
          tick={{ fontSize: 12 }}
        />
        <YAxis
          type={horizontal ? 'category' : 'number'}
          dataKey={horizontal ? xAxisKey : undefined}
          className="text-xs text-muted-foreground"
          tick={{ fontSize: 12 }}
        />
        <Tooltip content={<CustomTooltip />} />
        {showLegend && <Legend />}
        {bars.map((bar, index) => (
          <Bar
            key={bar.dataKey}
            dataKey={bar.dataKey}
            name={bar.name}
            fill={bar.color || barColors[index % barColors.length]}
            radius={[4, 4, 0, 0]}
          />
        ))}
      </RechartsBarChart>
    </ResponsiveContainer>
  )

  if (title) {
    return (
      <Card className={cn('', className)}>
        <CardHeader>
          <CardTitle className="text-base font-semibold">{title}</CardTitle>
          {description && (
            <p className="text-sm text-muted-foreground">{description}</p>
          )}
        </CardHeader>
        <CardContent>{chartContent}</CardContent>
      </Card>
    )
  }

  return <div className={cn('', className)}>{chartContent}</div>
}
