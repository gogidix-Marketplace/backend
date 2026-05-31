import { Card, CardContent, CardHeader, CardTitle, CardDescription } from '@shared/components/ui/card'
import { Badge } from '@shared/components/ui/badge'

export interface BudgetCategory {
  id: string
  name: string
  budget: number
  actual: number
  variance: number
  variancePercent: number
  status: 'under' | 'on_track' | 'over'
}

const mockCategories: BudgetCategory[] = [
  {
    id: 'marketing',
    name: 'Marketing',
    budget: 5000000,
    actual: 4200000,
    variance: -800000,
    variancePercent: -16,
    status: 'under',
  },
  {
    id: 'technology',
    name: 'Technology',
    budget: 8000000,
    actual: 8200000,
    variance: 200000,
    variancePercent: 2.5,
    status: 'on_track',
  },
  {
    id: 'operations',
    name: 'Operations',
    budget: 12000000,
    actual: 11500000,
    variance: -500000,
    variancePercent: -4.2,
    status: 'under',
  },
  {
    id: 'personnel',
    name: 'Personnel',
    budget: 15000000,
    actual: 16200000,
    variance: 1200000,
    variancePercent: 8,
    status: 'over',
  },
  {
    id: 'sales',
    name: 'Sales',
    budget: 7000000,
    actual: 6800000,
    variance: -200000,
    variancePercent: -2.9,
    status: 'under',
  },
]

const statusConfig = {
  under: { label: 'Under Budget', color: 'bg-emerald-500', text: 'text-emerald-600 dark:text-emerald-400', bgLight: 'bg-emerald-50 dark:bg-emerald-950/20' },
  on_track: { label: 'On Track', color: 'bg-blue-500', text: 'text-blue-600 dark:text-blue-400', bgLight: 'bg-blue-50 dark:bg-blue-950/20' },
  over: { label: 'Over Budget', color: 'bg-red-500', text: 'text-red-600 dark:text-red-400', bgLight: 'bg-red-50 dark:bg-red-950/20' },
}

function formatCurrency(amount: number) {
  return new Intl.NumberFormat('en-US', {
    style: 'currency',
    currency: 'USD',
    minimumFractionDigits: 0,
    maximumFractionDigits: 0,
  }).format(amount)
}

export interface BudgetVsActualProps {
  categories?: BudgetCategory[]
  onCategoryClick?: (categoryId: string) => void
  className?: string
}

export function BudgetVsActual({
  categories = mockCategories,
  onCategoryClick,
  className,
}: BudgetVsActualProps) {
  const totalBudget = categories.reduce((sum, c) => sum + c.budget, 0)
  const totalActual = categories.reduce((sum, c) => sum + c.actual, 0)

  return (
    <Card className={className}>
      <CardHeader className="pb-3">
        <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-3">
          <div>
            <CardTitle className="text-lg">Budget vs Actual</CardTitle>
            <CardDescription className="text-sm">Category breakdown for current quarter</CardDescription>
          </div>
          <div className="flex items-center gap-2">
            <div className="text-right">
              <p className="text-xs text-muted-foreground">Total Budget</p>
              <p className="text-lg font-bold">{formatCurrency(totalBudget)}</p>
            </div>
            <div className="h-8 w-px bg-slate-200 dark:bg-slate-700" />
            <div className="text-right">
              <p className="text-xs text-muted-foreground">Total Actual</p>
              <p className="text-lg font-bold">{formatCurrency(totalActual)}</p>
            </div>
          </div>
        </div>
      </CardHeader>

      <CardContent className="space-y-3">
        {categories.map((category) => {
          const status = statusConfig[category.status]
          const percentUsed = (category.actual / category.budget) * 100
          const isOver = category.status === 'over'

          return (
            <button
              key={category.id}
              onClick={() => onCategoryClick?.(category.id)}
              className="w-full text-left group"
            >
              <div className={`p-3 sm:p-4 rounded-lg border ${status.bgLight} hover:shadow-md transition-all`}>
                <div className="flex items-start justify-between mb-2">
                  <div className="flex-1">
                    <h4 className="font-semibold text-sm">{category.name}</h4>
                    <div className="flex items-center gap-3 mt-1 text-xs text-muted-foreground">
                      <span>Budget: {formatCurrency(category.budget)}</span>
                      <span>Actual: {formatCurrency(category.actual)}</span>
                    </div>
                  </div>
                  <Badge className={`${status.bgLight} ${status.text} border-0 text-xs`}>
                    {status.label}
                  </Badge>
                </div>

                {/* Progress Bar */}
                <div className="h-2 bg-slate-200 dark:bg-slate-700 rounded-full overflow-hidden">
                  <div
                    className={`h-full rounded-full transition-all ${status.color}`}
                    style={{ width: `${Math.min(percentUsed, 100)}%` }}
                  />
                </div>

                {/* Variance */}
                <div className="flex items-center justify-between mt-2">
                  <span className="text-xs text-muted-foreground">
                    {percentUsed.toFixed(1)}% utilized
                  </span>
                  <span className={`text-xs font-medium ${isOver ? 'text-red-600' : 'text-emerald-600'}`}>
                    {category.variance > 0 ? '+' : ''}{formatCurrency(category.variance)}
                  </span>
                </div>
              </div>
            </button>
          )
        })}
      </CardContent>
    </Card>
  )
}
