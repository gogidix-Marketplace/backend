import { useState } from 'react'
import { Card, CardContent, CardHeader, CardTitle } from '@shared/components/ui/card'
import {
  AreaChart,
  Area,
  BarChart,
  Bar,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  Legend,
  ResponsiveContainer,
} from 'recharts'

/**
 * Operations Metrics Chart for COO Dashboard
 */

type PeriodType = 'today' | 'week' | 'month' | 'quarter'

interface OperationsMetricsChartProps {
  dateRange?: PeriodType
  onDateRangeChange?: (range: PeriodType) => void
}

const operationsData = {
  week: [
    { day: 'Mon', orders: 1850, completed: 1780, pending: 55, efficiency: 96 },
    { day: 'Tue', orders: 1920, completed: 1890, pending: 25, efficiency: 98 },
    { day: 'Wed', orders: 1880, completed: 1820, pending: 45, efficiency: 97 },
    { day: 'Thu', orders: 2050, completed: 2010, pending: 30, efficiency: 98 },
    { day: 'Fri', orders: 2100, completed: 1980, pending: 85, efficiency: 94 },
    { day: 'Sat', orders: 1420, completed: 1410, pending: 8, efficiency: 99 },
    { day: 'Sun', orders: 980, completed: 975, pending: 5, efficiency: 99 },
  ],
  month: [
    { day: 'W1', orders: 12800, completed: 12450, pending: 280, efficiency: 97 },
    { day: 'W2', orders: 13200, completed: 12900, pending: 240, efficiency: 98 },
    { day: 'W3', orders: 12950, completed: 12600, pending: 290, efficiency: 97 },
    { day: 'W4', orders: 13500, completed: 13200, pending: 260, efficiency: 98 },
  ],
}

export function OperationsMetricsChart({ dateRange = 'week', onDateRangeChange }: OperationsMetricsChartProps) {
  const data = operationsData[dateRange === 'week' || dateRange === 'today' ? 'week' : 'month']

  return (
    <Card>
      <CardHeader className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-3">
        <CardTitle className="text-lg">Operations Performance</CardTitle>

        {/* Date Filter */}
        <div className="flex gap-1 overflow-x-auto pb-1 sm:pb-0">
          {(['today', 'week', 'month', 'quarter'] as PeriodType[]).map((range) => (
            <button
              key={range}
              onClick={() => onDateRangeChange?.(range)}
              className={`px-3 py-1.5 rounded-lg text-xs font-medium capitalize whitespace-nowrap transition-colors ${
                dateRange === range
                  ? 'bg-[#0D47A1] text-white'
                  : 'text-slate-600 dark:text-slate-400 hover:bg-slate-100 dark:hover:bg-slate-800'
              }`}
            >
              {range}
            </button>
          ))}
        </div>
      </CardHeader>

      <CardContent>
        {/* Bar Chart - Orders vs Completed */}
        <div className="h-64">
          <ResponsiveContainer width="100%" height="100%">
            <BarChart data={data} layout="vertical">
              <CartesianGrid strokeDasharray="3 3" className="stroke-slate-200 dark:stroke-slate-700" />
              <XAxis
                type="number"
                className="text-xs"
                stroke="#64748b"
                tickFormatter={(value) => `${value}`}
              />
              <YAxis
                dataKey="day"
                type="category"
                className="text-xs"
                stroke="#64748b"
                width={40}
              />
              <Tooltip
                contentStyle={{
                  backgroundColor: 'rgb(15 23 42)',
                  border: '1px solid rgb(51 65 85)',
                  borderRadius: '0.5rem',
                }}
                formatter={(value: number, name: string) => [
                  value,
                  name === 'orders' ? 'Total Orders' : name === 'completed' ? 'Completed' : 'Pending',
                ]}
              />
              <Legend />
              <Bar dataKey="orders" name="orders" fill="#64748b" radius={[0, 4, 4, 0]} />
              <Bar dataKey="completed" name="completed" fill="#0D47A1" radius={[0, 4, 4, 0]} />
              <Bar dataKey="pending" name="pending" fill="#f59e0b" radius={[0, 4, 4, 0]} />
            </BarChart>
          </ResponsiveContainer>
        </div>

        {/* Summary Stats */}
        <div className="grid grid-cols-3 gap-4 mt-4 pt-4 border-t border-slate-200 dark:border-slate-700">
          <div className="text-center">
            <p className="text-xs text-slate-500 dark:text-slate-400">Total Orders</p>
            <p className="text-lg font-bold text-slate-900 dark:text-white">
              {data.reduce((sum, d) => sum + d.orders, 0).toLocaleString()}
            </p>
          </div>
          <div className="text-center">
            <p className="text-xs text-slate-500 dark:text-slate-400">Completion Rate</p>
            <p className="text-lg font-bold text-emerald-600 dark:text-emerald-400">
              {Math.round((data.reduce((sum, d) => sum + d.completed, 0) / data.reduce((sum, d) => sum + d.orders, 0)) * 100)}%
            </p>
          </div>
          <div className="text-center">
            <p className="text-xs text-slate-500 dark:text-slate-400">Avg Efficiency</p>
            <p className="text-lg font-bold text-blue-600 dark:text-blue-400">
              {Math.round(data.reduce((sum, d) => sum + d.efficiency, 0) / data.length)}%
            </p>
          </div>
        </div>
      </CardContent>
    </Card>
  )
}
