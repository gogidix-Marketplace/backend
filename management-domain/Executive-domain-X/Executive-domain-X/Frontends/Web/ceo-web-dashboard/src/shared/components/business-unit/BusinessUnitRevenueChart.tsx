import { businessUnits } from '@shared/data/business-units'
import { cn } from '@shared/utils/cn'

export interface BusinessUnitRevenueChartProps {
  sortBy?: 'revenue' | 'growth' | 'name'
}

function formatCurrency(value: number): string {
  if (value >= 1000000) {
    return `$${(value / 1000000).toFixed(1)}M`
  }
  return `$${value.toLocaleString()}`
}

export function BusinessUnitRevenueChart({
  sortBy = 'revenue',
}: BusinessUnitRevenueChartProps) {
  const sorted = [...businessUnits].sort((a, b) => {
    if (sortBy === 'name') return a.name.localeCompare(b.name)
    if (sortBy === 'growth') return b.revenueGrowth - a.revenueGrowth
    return b.revenue - a.revenue
  })

  const maxRevenue = Math.max(...businessUnits.map((u) => u.revenue))

  return (
    <div className="space-y-3">
      {sorted.map((unit) => {
        const widthPercent = (unit.revenue / maxRevenue) * 100
        return (
          <div key={unit.id} className="flex items-center gap-3">
            <span className="w-36 truncate text-sm font-medium text-slate-700 dark:text-slate-300">
              {unit.name}
            </span>
            <div className="flex-1 h-8 rounded-md bg-slate-100 dark:bg-slate-800 overflow-hidden">
              <div
                className={cn('h-full rounded-md transition-all duration-500')}
                style={{
                  width: `${widthPercent}%`,
                  backgroundColor: unit.color,
                }}
              />
            </div>
            <span className="w-20 text-right text-sm font-semibold text-slate-900 dark:text-slate-100">
              {formatCurrency(unit.revenue)}
            </span>
          </div>
        )
      })}
    </div>
  )
}
