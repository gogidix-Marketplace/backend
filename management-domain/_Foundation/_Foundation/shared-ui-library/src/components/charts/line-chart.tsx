import * as React from 'react'
import {
  LineChart as RechartsLineChart,
  Line,
  Area,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  Legend,
  ResponsiveContainer,
} from 'recharts'
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card'
import { cn } from '@/lib/utils'

export interface ChartData {
  name: string
  [key: string]: string | number
}

export interface LineChartProps {
  data: ChartData[]
  lines: {
    dataKey: string
    name: string
    color: string
    type?: 'monotone' | 'linear' | 'step' | 'stepBefore' | 'stepAfter'
    strokeWidth?: number
    showArea?: boolean
  }[]
  title?: string
  description?: string
  className?: string
  height?: number
  showGrid?: boolean
  showLegend?: boolean
  xAxisKey?: string
}

export function LineChart({
  data,
  lines,
  title,
  description,
  className,
  height = 300,
  showGrid = true,
  showLegend = true,
  xAxisKey = 'name',
}: LineChartProps) {
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
      <RechartsLineChart data={data} margin={{ top: 10, right: 10, left: 0, bottom: 0 }}>
        {showGrid && <CartesianGrid strokeDasharray="3 3" className="stroke-muted" />}
        <XAxis
          dataKey={xAxisKey}
          className="text-xs text-muted-foreground"
          tick={{ fontSize: 12 }}
        />
        <YAxis
          className="text-xs text-muted-foreground"
          tick={{ fontSize: 12 }}
        />
        <Tooltip content={<CustomTooltip />} />
        {showLegend && <Legend />}
        {lines.map((line) => (
          <React.Fragment key={line.dataKey}>
            {line.showArea ? (
              <Area
                type={line.type || 'monotone'}
                dataKey={line.dataKey}
                name={line.name}
                stroke={line.color}
                fill={line.color}
                fillOpacity={0.2}
                strokeWidth={line.strokeWidth || 2}
              />
            ) : (
              <Line
                type={line.type || 'monotone'}
                dataKey={line.dataKey}
                name={line.name}
                stroke={line.color}
                strokeWidth={line.strokeWidth || 2}
                dot={{ r: 4 }}
                activeDot={{ r: 6 }}
              />
            )}
          </React.Fragment>
        ))}
      </RechartsLineChart>
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
