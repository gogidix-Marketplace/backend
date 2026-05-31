import * as React from 'react'
import {
  PieChart as RechartsPieChart,
  Pie,
  Cell,
  ResponsiveContainer,
  Tooltip,
  Legend,
} from 'recharts'
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card'
import { cn } from '@/lib/utils'

export interface PieData {
  name: string
  value: number
  color?: string
}

export interface PieChartProps {
  data: PieData[]
  title?: string
  description?: string
  className?: string
  height?: number
  innerRadius?: number
  outerRadius?: number
  showLegend?: boolean
  colors?: string[]
  showLabels?: boolean
}

const DEFAULT_COLORS = [
  '#0D47A1', '#FFA000', '#4CAF50', '#F44336', '#9C27B0',
  '#00BCD4', '#FF9800', '#795548', '#607D8B', '#8BC34A'
]

export function PieChart({
  data,
  title,
  description,
  className,
  height = 300,
  innerRadius = 0,
  outerRadius = 100,
  showLegend = true,
  colors,
  showLabels = false,
}: PieChartProps) {
  const chartColors = colors || DEFAULT_COLORS

  const CustomTooltip = ({ active, payload }: any) => {
    if (active && payload && payload.length) {
      const data = payload[0].payload
      return (
        <div className="rounded-lg border bg-background p-3 shadow-md">
          <p className="text-sm font-medium">{data.name}</p>
          <p className="text-lg font-bold" style={{ color: data.color }}>
            {data.value}
          </p>
        </div>
      )
    }
    return null
  }

  const CustomLabel = ({ cx, cy, midAngle, innerRadius, outerRadius, percent }: any) => {
    if (percent < 0.05) return null // Don't show label if slice is too small

    const RADIAN = Math.PI / 180
    const radius = innerRadius + (outerRadius - innerRadius) * 0.5
    const x = cx + radius * Math.cos(-midAngle * RADIAN)
    const y = cy + radius * Math.sin(-midAngle * RADIAN)

    return (
      <text
        x={x}
        y={y}
        fill="white"
        textAnchor={x > cx ? 'start' : 'end'}
        dominantBaseline="central"
        fontSize={12}
        fontWeight="bold"
      >
        {`${(percent * 100).toFixed(0)}%`}
      </text>
    )
  }

  const chartContent = (
    <ResponsiveContainer width="100%" height={height}>
      <RechartsPieChart>
        <Pie
          data={data}
          cx="50%"
          cy="50%"
          labelLine={false}
          label={showLabels ? CustomLabel : false}
          outerRadius={outerRadius}
          innerRadius={innerRadius}
          paddingAngle={2}
          dataKey="value"
        >
          {data.map((entry, index) => (
            <Cell
              key={`cell-${index}`}
              fill={entry.color || chartColors[index % chartColors.length]}
            />
          ))}
        </Pie>
        <Tooltip content={<CustomTooltip />} />
        {showLegend && (
          <Legend
            verticalAlign="bottom"
            height={36}
            iconType="circle"
            formatter={(value, entry: any) => (
              <span className="text-sm">{value} ({entry.payload.value})</span>
            )}
          />
        )}
      </RechartsPieChart>
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
