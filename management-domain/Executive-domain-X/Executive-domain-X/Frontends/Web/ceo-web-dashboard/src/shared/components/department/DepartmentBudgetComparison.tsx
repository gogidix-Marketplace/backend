import { departments } from '@shared/data/departments'
import { cn } from '@shared/utils/cn'

export interface DepartmentBudgetComparisonProps {
  currency?: string
}

function formatCurrency(value: number, currency = '$'): string {
  if (value >= 1000000) {
    return `${currency}${(value / 1000000).toFixed(1)}M`
  }
  if (value >= 1000) {
    return `${currency}${(value / 1000).toFixed(0)}K`
  }
  return `${currency}${value}`
}

function getBarColor(percent: number): string {
  if (percent > 90) return 'bg-red-500'
  if (percent >= 80) return 'bg-amber-500'
  return 'bg-green-500'
}

function getTextColor(percent: number): string {
  if (percent > 90) return 'text-red-600 dark:text-red-400'
  if (percent >= 80) return 'text-amber-600 dark:text-amber-400'
  return 'text-green-600 dark:text-green-400'
}

export function DepartmentBudgetComparison({
  currency = '$',
}: DepartmentBudgetComparisonProps) {
  const sorted = [...departments].sort(
    (a, b) => b.budgetPercentUsed - a.budgetPercentUsed
  )

  return (
    <div className="space-y-4">
      {sorted.map((dept) => {
        const utilization = dept.budgetPercentUsed
        const remaining = dept.budgetAllocated - dept.budgetSpent

        return (
          <div key={dept.departmentId} className="space-y-1.5">
            <div className="flex items-center justify-between">
              <div className="flex items-center gap-2">
                <span
                  className="flex h-6 w-6 items-center justify-center rounded bg-slate-100 dark:bg-slate-800 text-xs font-bold"
                  style={{ color: dept.color }}
                >
                  {dept.icon}
                </span>
                <span className="text-sm font-medium text-slate-900 dark:text-slate-100">
                  {dept.name}
                </span>
              </div>
              <div className="flex items-center gap-3 text-xs text-slate-500">
                <span>
                  {formatCurrency(dept.budgetSpent, currency)} /{' '}
                  {formatCurrency(dept.budgetAllocated, currency)}
                </span>
                <span className={cn('font-semibold', getTextColor(utilization))}>
                  {utilization}%
                </span>
              </div>
            </div>

            <div className="h-3 w-full overflow-hidden rounded-full bg-slate-100 dark:bg-slate-800">
              <div
                className={cn(
                  'h-full rounded-full transition-all duration-500',
                  getBarColor(utilization)
                )}
                style={{ width: `${Math.min(utilization, 100)}%` }}
              />
            </div>

            <div className="flex justify-between text-[11px] text-slate-500">
              <span>
                Spent: {formatCurrency(dept.budgetSpent, currency)}
              </span>
              <span>
                Remaining:{' '}
                <span
                  className={cn(
                    'font-medium',
                    remaining < dept.budgetAllocated * 0.1
                      ? 'text-red-600 dark:text-red-400'
                      : 'text-green-600 dark:text-green-400'
                  )}
                >
                  {formatCurrency(remaining, currency)}
                </span>
              </span>
            </div>
          </div>
        )
      })}
    </div>
  )
}
