import * as React from 'react'
import { Target, TrendingUp, TrendingDown, DollarSign } from 'lucide-react'
import { cn } from '@lib/utils'
import { Card } from '@/components/ui/card'
import { Progress } from '@/components/ui/progress'

export interface SalesTargetProps {
  target: number
  actual: number
  period: string
  currency?: string
  previousPeriod?: number
  className?: string
}

export function SalesTargetCard({
  target,
  actual,
  period,
  currency = '$',
  previousPeriod,
  className,
}: SalesTargetProps) {
  const percentage = Math.round((actual / target) * 100)
  const isOverTarget = actual >= target
  const remaining = Math.max(0, target - actual)

  // Calculate trend
  let trend = 0
  let trendIcon: React.ReactNode = null

  if (previousPeriod) {
    const previousPercentage = Math.round((previousPeriod / target) * 100)
    trend = percentage - previousPercentage
    if (trend > 0) {
      trendIcon = <TrendingUp className="h-3 w-3 text-green-600" />
    } else if (trend < 0) {
      trendIcon = <TrendingDown className="h-3 w-3 text-red-600" />
    }
  }

  return (
    <Card className={cn('p-4', className)}>
      <div className="flex items-center justify-between mb-2">
        <div className="flex items-center gap-2">
          <Target className="h-5 w-5 text-primary-600" />
          <h3 className="font-semibold text-gray-900">Sales Target</h3>
        </div>
        <span className="text-sm text-gray-500">{period}</span>
      </div>

      {/* Main Metric */}
      <div className="mb-4">
        <div className="flex items-baseline gap-2">
          <span className="text-3xl font-bold text-gray-900">
            {currency}
            {actual.toLocaleString()}
          </span>
          <span className="text-sm text-gray-500">
            of {currency}
            {target.toLocaleString()}
          </span>
        </div>
        <div className="flex items-center gap-2 mt-1">
          {trendIcon && (
            <span className={cn(
              'text-xs flex items-center gap-1',
              trend > 0 ? 'text-green-600' : trend < 0 ? 'text-red-600' : 'text-gray-500'
            )}>
              {trendIcon}
              <span>{Math.abs(trend)}% vs last period</span>
            </span>
          )}
        </div>
      </div>

      {/* Progress */}
      <div className="space-y-2">
        <div className="flex items-center justify-between text-sm">
          <span className={cn(
            'font-medium',
            isOverTarget ? 'text-green-600' : 'text-gray-700'
          )}>
            {percentage}%
            {isOverTarget ? ' achieved!' : ` (${remaining} remaining)`}
          </span>
        </div>
        <Progress value={Math.min(percentage, 100)} className="h-2" />
      </div>
    </Card>
  )
}

// Pipeline Funnel Component
export interface PipelineStage {
  id: string
  name: string
  value: number
  currency?: string
  color: string
  deals?: Array<{
    id: string
    name: string
    value: number
  }>
}

export interface PipelineFunnelProps {
  stages: PipelineStage[]
  totalValue: number
  currency?: string
  className?: string
}

export function PipelineFunnel({ stages, totalValue, currency = '$', className }: PipelineFunnelProps) {
  const maxStageValue = Math.max(...stages.map((s) => s.value))

  return (
    <Card className={cn('p-4', className)}>
      <div className="flex items-center justify-between mb-4">
        <h3 className="font-semibold text-gray-900">Sales Pipeline</h3>
        <div className="text-right">
          <p className="text-xs text-gray-500">Total Value</p>
          <p className="text-lg font-bold text-gray-900">
            {currency}
            {totalValue.toLocaleString()}
          </p>
        </div>
      </div>

      {/* Funnel */}
      <div className="space-y-2">
        {stages.map((stage, index) => {
          const widthPercentage = Math.round((stage.value / maxStageValue) * 100)
          const stageIndex = stages.length - index

          return (
            <div key={stage.id} className="relative">
              <div
                className="p-3 rounded-md text-white text-sm relative overflow-hidden"
                style={{
                  backgroundColor: stage.color,
                  marginLeft: `${index * 8}px`,
                  width: `calc(100% - ${index * 8}px)`,
                }}
              >
                <div className="flex items-center justify-between">
                  <div>
                    <p className="font-medium">{stage.name}</p>
                    <p className="text-xs opacity-90">
                      {stage.deals?.length || 0} deals
                    </p>
                  </div>
                  <div className="text-right">
                    <p className="font-bold">{currency}{stage.value.toLocaleString()}</p>
                    <p className="text-xs opacity-90">{widthPercentage}%</p>
                  </div>
                </div>
              </div>
            </div>
          )
        })}
      </div>

      {/* Legend */}
      <div className="mt-4 pt-4 border-t border-gray-200">
        <div className="grid grid-cols-2 gap-2 text-xs text-gray-600">
          {stages.map((stage) => (
            <div key={stage.id} className="flex items-center gap-2">
              <div
                className="w-3 h-3 rounded"
                style={{ backgroundColor: stage.color }}
              />
              <span>{stage.name}</span>
            </div>
          ))}
        </div>
      </div>
    </Card>
  )
}

// Commission Calculator Widget
export interface CommissionTier {
  min: number
  max: number
  rate: number // percentage
}

export interface CommissionCalculatorProps {
  tiers: CommissionTier[]
  sales: number
  baseSalary?: number
  currency?: string
  className?: string
}

export function CommissionCalculator({
  tiers,
  sales,
  baseSalary = 0,
  currency = '$',
  className,
}: CommissionCalculatorProps) {
  // Sort tiers by min value
  const sortedTiers = [...tiers].sort((a, b) => a.min - b.min)

  // Calculate commission
  let commission = 0
  let remainingSales = sales

  for (const tier of sortedTiers) {
    if (remainingSales <= 0) break

    const tierRange = tier.max - tier.min
    const tierSales = Math.min(remainingSales, tierRange)

    commission += (tierSales * tier.rate) / 100
    remainingSales -= tierSales
  }

  const totalCompensation = baseSalary + commission

  return (
    <Card className={cn('p-4', className)}>
      <div className="flex items-center gap-2 mb-4">
        <DollarSign className="h-5 w-5 text-primary-600" />
        <h3 className="font-semibold text-gray-900">Commission Calculator</h3>
      </div>

      <div className="space-y-4">
        {/* Sales Input */}
        <div>
          <label className="block text-sm font-medium text-gray-700 mb-1">
            Total Sales
          </label>
          <div className="text-2xl font-bold text-gray-900">
            {currency}
            {sales.toLocaleString()}
          </div>
        </div>

        {/* Commission Tiers */}
        <div className="border-t border-gray-200 pt-4">
          <p className="text-sm font-medium text-gray-700 mb-2">Commission Tiers</p>
          <div className="space-y-2">
            {sortedTiers.map((tier, index) => {
              const isInTier = sales >= tier.min
              const tierWidth = Math.min(100, Math.max(0, ((sales - tier.min) / (tier.max - tier.min)) * 100))
              const color = ['bg-blue-500', 'bg-green-500', 'bg-purple-500', 'bg-orange-500'][index % 4]

              return (
                <div key={`${tier.min}-${tier.max}`} className="relative">
                  <div className="flex items-center justify-between text-sm mb-1">
                    <span className="text-gray-600">
                      {currency}
                      {tier.min.toLocaleString()} - {tier.max.toLocaleString()}
                    </span>
                    <span className="font-medium text-gray-900">{tier.rate}%</span>
                  </div>
                  <div className="h-2 bg-gray-100 rounded-full overflow-hidden">
                    <div
                      className={cn('h-full transition-all', color)}
                      style={{ width: `${isInTier ? tierWidth : 0}%` }}
                    />
                  </div>
                </div>
              )
            })}
          </div>
        </div>

        {/* Results */}
        <div className="border-t border-gray-200 pt-4">
          <div className="flex items-center justify-between mb-2">
            <span className="text-sm text-gray-600">Commission</span>
            <span className="text-lg font-bold text-gray-900">
              {currency}
              {commission.toLocaleString()}
            </span>
          </div>
          {baseSalary > 0 && (
            <div className="flex items-center justify-between mb-2">
              <span className="text-sm text-gray-600">Base Salary</span>
              <span className="text-lg font-semibold text-gray-900">
                {currency}
                {baseSalary.toLocaleString()}
              </span>
            </div>
          )}
          <div className="flex items-center justify-between pt-2 border-t border-gray-200">
            <span className="text-sm font-medium text-gray-900">Total Compensation</span>
            <span className="text-xl font-bold text-green-600">
              {currency}
              {totalCompensation.toLocaleString()}
            </span>
          </div>
        </div>
      </div>
    </Card>
  )
}
