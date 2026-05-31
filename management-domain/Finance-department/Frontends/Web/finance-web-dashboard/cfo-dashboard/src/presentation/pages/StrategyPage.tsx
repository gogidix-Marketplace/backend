import { useEffect } from 'react'
import {
  Target,
  TrendingUp,
  Sparkles,
  Globe,
  Building2,
  Loader2,
  ArrowUpRight,
} from 'lucide-react'
import { useCfoStore } from '@shared/store'
import { cn } from '@shared/utils'

export default function StrategyPage() {
  const {
    goals,
    maPipeline,
    capitalAllocation,
    growthTargets,
    aiRecommendations,
    isLoadingStrategy,
    loadStrategyData,
  } = useCfoStore()

  useEffect(() => {
    loadStrategyData()
  }, [loadStrategyData])

  if (isLoadingStrategy) {
    return (
      <div className="flex items-center justify-center h-96">
        <Loader2 className="w-8 h-8 text-indigo-500 animate-spin" />
      </div>
    )
  }

  const statusConfig: Record<string, { label: string; color: string }> = {
    on_track: { label: 'On Track', color: 'bg-emerald-100 text-emerald-700' },
    at_risk: { label: 'At Risk', color: 'bg-amber-100 text-amber-700' },
    behind: { label: 'Behind', color: 'bg-red-100 text-red-700' },
  }

  const stageConfig: Record<string, { label: string; color: string }> = {
    due_diligence: { label: 'Due Diligence', color: 'bg-blue-100 text-blue-700' },
    negotiation: { label: 'Negotiation', color: 'bg-indigo-100 text-indigo-700' },
    letter_of_intent: { label: 'Letter of Intent', color: 'bg-violet-100 text-violet-700' },
    closed: { label: 'Closed', color: 'bg-emerald-100 text-emerald-700' },
  }

  return (
    <div className="space-y-6 max-w-7xl">
      <div>
        <h1 className="text-2xl font-bold text-gray-900">Financial Strategy</h1>
        <p className="text-sm text-gray-500 mt-1">Strategic goals, M&A pipeline, and capital allocation</p>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-5">
        {goals.map((goal) => {
          const status = statusConfig[goal.status]
          return (
            <div key={goal.id} className="bg-white rounded-xl border border-gray-200 p-5 hover:shadow-lg transition-shadow">
              <div className="flex items-start justify-between mb-3">
                <div className="w-9 h-9 rounded-lg bg-indigo-50 flex items-center justify-center">
                  <Target className="w-4 h-4 text-indigo-600" />
                </div>
                <span className={cn('text-[10px] font-semibold px-2 py-0.5 rounded-full', status.color)}>
                  {status.label}
                </span>
              </div>
              <h3 className="text-sm font-semibold text-gray-900 mb-1">{goal.title}</h3>
              <p className="text-xs text-gray-500 mb-3">Target: {goal.target}</p>
              <div className="mb-2">
                <div className="flex items-center justify-between text-xs mb-1">
                  <span className="text-gray-500">Progress</span>
                  <span className="font-semibold text-indigo-600">{goal.progress}%</span>
                </div>
                <div className="w-full bg-gray-100 rounded-full h-2">
                  <div
                    className={cn(
                      'h-full rounded-full',
                      goal.progress >= 80 ? 'bg-emerald-500' : goal.progress >= 50 ? 'bg-indigo-500' : 'bg-amber-500'
                    )}
                    style={{ width: `${goal.progress}%` }}
                  />
                </div>
              </div>
              <div className="flex items-center justify-between text-[11px] text-gray-400">
                <span>Owner: {goal.owner}</span>
                <span>Due: {goal.deadline}</span>
              </div>
            </div>
          )
        })}
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-2 gap-5">
        <div className="bg-white rounded-xl border border-gray-200 p-6">
          <div className="flex items-center gap-2 mb-5">
            <Building2 className="w-5 h-5 text-indigo-600" />
            <h3 className="text-sm font-semibold text-gray-700">M&A Pipeline</h3>
          </div>
          <div className="space-y-4">
            {maPipeline.map((item) => {
              const stage = stageConfig[item.stage]
              const val = item.value >= 1000000000
                ? `$${(item.value / 1000000000).toFixed(1)}B`
                : `$${(item.value / 1000000).toFixed(0)}M`
              return (
                <div key={item.id} className="p-4 rounded-lg border border-gray-100 hover:border-indigo-200 transition-colors">
                  <div className="flex items-start justify-between mb-2">
                    <div>
                      <h4 className="text-sm font-semibold text-gray-900">{item.company}</h4>
                      <p className="text-xs text-gray-500">{item.sector}</p>
                    </div>
                    <span className={cn('text-[10px] font-semibold px-2 py-0.5 rounded-full', stage.color)}>
                      {stage.label}
                    </span>
                  </div>
                  <div className="flex items-center justify-between text-xs text-gray-500">
                    <span className="font-semibold text-gray-800">{val}</span>
                    <span>Expected close: {item.expectedClose}</span>
                  </div>
                </div>
              )
            })}
          </div>
        </div>

        <div className="bg-white rounded-xl border border-gray-200 p-6">
          <div className="flex items-center gap-2 mb-5">
            <TrendingUp className="w-5 h-5 text-indigo-600" />
            <h3 className="text-sm font-semibold text-gray-700">Capital Allocation</h3>
          </div>
          <div className="space-y-3">
            {capitalAllocation.map((item) => (
              <div key={item.category}>
                <div className="flex items-center justify-between text-sm mb-1">
                  <span className="text-gray-700">{item.category}</span>
                  <div className="flex items-center gap-2">
                    <span className="text-xs text-gray-500">${(item.amount / 1000000).toFixed(0)}M</span>
                    <span className="text-xs font-semibold text-indigo-600">{item.percentage}%</span>
                  </div>
                </div>
                <div className="w-full bg-gray-100 rounded-full h-2.5">
                  <div
                    className="h-full bg-gradient-to-r from-indigo-500 to-violet-500 rounded-full"
                    style={{ width: `${item.percentage}%` }}
                  />
                </div>
              </div>
            ))}
          </div>
        </div>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-2 gap-5">
        <div className="bg-white rounded-xl border border-gray-200 p-6">
          <div className="flex items-center gap-2 mb-5">
            <Globe className="w-5 h-5 text-indigo-600" />
            <h3 className="text-sm font-semibold text-gray-700">Growth Targets by Region (%)</h3>
          </div>
          <div className="space-y-4">
            {growthTargets.map((item) => {
              const isExceeding = item.actual >= item.target
              return (
                <div key={item.region}>
                  <div className="flex items-center justify-between text-sm mb-1">
                    <span className="text-gray-700">{item.region}</span>
                    <div className="flex items-center gap-2">
                      <span className="text-xs text-gray-500">Target: {item.target}%</span>
                      <span className={cn('text-xs font-semibold', isExceeding ? 'text-emerald-600' : 'text-amber-600')}>
                        Actual: {item.actual}%
                      </span>
                      {isExceeding && <ArrowUpRight className="w-3 h-3 text-emerald-500" />}
                    </div>
                  </div>
                  <div className="relative w-full bg-gray-100 rounded-full h-3">
                    <div
                      className="absolute h-full bg-gray-300 rounded-full"
                      style={{ width: `${(item.target / 30) * 100}%` }}
                    />
                    <div
                      className={cn(
                        'absolute h-full rounded-full',
                        isExceeding ? 'bg-emerald-500' : 'bg-indigo-500'
                      )}
                      style={{ width: `${(item.actual / 30) * 100}%` }}
                    />
                  </div>
                </div>
              )
            })}
          </div>
        </div>

        <div className="bg-gradient-to-br from-indigo-600 to-violet-700 rounded-xl p-6 text-white">
          <div className="flex items-center gap-2 mb-4">
            <Sparkles className="w-5 h-5" />
            <h3 className="text-sm font-semibold">AI Strategic Recommendations</h3>
          </div>
          <div className="space-y-3">
            {aiRecommendations.map((rec, i) => (
              <div key={i} className="flex gap-3 text-sm">
                <div className="w-1.5 h-1.5 rounded-full bg-indigo-200 mt-2 flex-shrink-0" />
                <p className="text-indigo-100">{rec}</p>
              </div>
            ))}
          </div>
        </div>
      </div>
    </div>
  )
}
