import { useEffect } from 'react'
import {
  ShieldAlert,
  AlertTriangle,
  TrendingDown,
  CheckCircle2,
  Eye,
  Loader2,
} from 'lucide-react'
import { useCfoStore } from '@shared/store'
import { cn } from '@shared/utils'

const severityColors: Record<string, { bg: string; text: string; dot: string }> = {
  critical: { bg: 'bg-red-50', text: 'text-red-700', dot: 'bg-red-500' },
  high: { bg: 'bg-orange-50', text: 'text-orange-700', dot: 'bg-orange-500' },
  medium: { bg: 'bg-amber-50', text: 'text-amber-700', dot: 'bg-amber-500' },
  low: { bg: 'bg-green-50', text: 'text-green-700', dot: 'bg-green-500' },
}

const statusIcon: Record<string, React.ElementType> = {
  active: AlertTriangle,
  monitoring: Eye,
  mitigated: CheckCircle2,
}

export default function RiskPage() {
  const {
    risks,
    fxExposure,
    complianceScore,
    isLoadingRisks,
    loadRiskData,
  } = useCfoStore()

  useEffect(() => {
    loadRiskData()
  }, [loadRiskData])

  if (isLoadingRisks) {
    return (
      <div className="flex items-center justify-center h-96">
        <Loader2 className="w-8 h-8 text-indigo-500 animate-spin" />
      </div>
    )
  }

  const matrixData: { label: string; impact: number; likelihood: number }[] = [
    ...risks.map((r) => ({ label: r.name.substring(0, 15), impact: r.impact, likelihood: r.likelihood })),
  ]

  return (
    <div className="space-y-6 max-w-7xl">
      <div>
        <h1 className="text-2xl font-bold text-gray-900">Risk Management</h1>
        <p className="text-sm text-gray-500 mt-1">Enterprise risk overview, FX exposure, and compliance</p>
      </div>

      <div className="grid grid-cols-2 lg:grid-cols-4 gap-4">
        {[
          { label: 'Critical Risks', count: risks.filter((r) => r.severity === 'critical').length, color: 'text-red-600', bg: 'bg-red-50' },
          { label: 'High Risks', count: risks.filter((r) => r.severity === 'high').length, color: 'text-orange-600', bg: 'bg-orange-50' },
          { label: 'Active Monitoring', count: risks.filter((r) => r.status === 'monitoring').length, color: 'text-blue-600', bg: 'bg-blue-50' },
          { label: 'Compliance Score', count: `${complianceScore}%`, color: 'text-emerald-600', bg: 'bg-emerald-50' },
        ].map((item) => (
          <div key={item.label} className={cn('rounded-xl border p-5', item.bg)}>
            <p className="text-xs text-gray-600 mb-1">{item.label}</p>
            <p className={cn('text-2xl font-bold', item.color)}>{item.count}</p>
          </div>
        ))}
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-2 gap-5">
        <div className="bg-white rounded-xl border border-gray-200 p-6">
          <div className="flex items-center gap-2 mb-5">
            <ShieldAlert className="w-5 h-5 text-indigo-600" />
            <h3 className="text-sm font-semibold text-gray-700">Risk Matrix (Impact vs Likelihood)</h3>
          </div>
          <div className="relative">
            <div className="flex items-end gap-1 mb-2">
              <div className="w-8" />
              <div className="flex-1 grid grid-cols-5 gap-1 text-center text-[10px] text-gray-400">
                <span>1</span><span>2</span><span>3</span><span>4</span><span>5</span>
              </div>
            </div>
            <div className="text-[10px] text-gray-400 text-center mb-2">Likelihood →</div>
            {[5, 4, 3, 2, 1].map((impact) => (
              <div key={impact} className="flex items-center gap-1 mb-1">
                <div className="w-8 text-[10px] text-gray-400 text-right pr-1">{impact}</div>
                <div className="flex-1 grid grid-cols-5 gap-1">
                  {[1, 2, 3, 4, 5].map((likelihood) => {
                    const items = risks.filter(
                      (r) => r.impact === impact && r.likelihood === likelihood
                    )
                    const hasCritical = items.some((r) => r.severity === 'critical')
                    const hasHigh = items.some((r) => r.severity === 'high')
                    const bg =
                      hasCritical
                        ? 'bg-red-200 border-red-400'
                        : hasHigh
                        ? 'bg-orange-200 border-orange-400'
                        : items.length > 0
                        ? 'bg-amber-100 border-amber-300'
                        : impact >= 4 && likelihood >= 3
                        ? 'bg-red-50 border-red-100'
                        : impact >= 3 && likelihood >= 3
                        ? 'bg-amber-50 border-amber-100'
                        : 'bg-gray-50 border-gray-100'
                    return (
                      <div
                        key={likelihood}
                        className={cn(
                          'aspect-square rounded border flex items-center justify-center text-[9px] font-medium text-gray-600',
                          bg
                        )}
                      >
                        {items.length > 0 ? items.length : ''}
                      </div>
                    )
                  })}
                </div>
              </div>
            ))}
            <div className="text-[10px] text-gray-400 text-center mt-1">↑ Impact</div>
          </div>
        </div>

        <div className="bg-white rounded-xl border border-gray-200 p-6">
          <div className="flex items-center gap-2 mb-5">
            <TrendingDown className="w-5 h-5 text-indigo-600" />
            <h3 className="text-sm font-semibold text-gray-700">FX Exposure Summary</h3>
          </div>
          <div className="space-y-3">
            {fxExposure.map((fx) => {
              const maxExp = Math.max(...fxExposure.map((f) => f.exposure))
              return (
                <div key={fx.currency}>
                  <div className="flex items-center justify-between text-sm mb-1">
                    <span className="font-medium text-gray-700">{fx.currency}</span>
                    <div className="flex items-center gap-2">
                      <span className="text-gray-900 font-semibold">
                        ${(fx.exposure / 1000000).toFixed(0)}M
                      </span>
                      <span
                        className={cn(
                          'text-xs font-medium',
                          fx.change < 0 ? 'text-red-600' : 'text-emerald-600'
                        )}
                      >
                        {fx.change > 0 ? '+' : ''}{fx.change}%
                      </span>
                    </div>
                  </div>
                  <div className="w-full bg-gray-100 rounded-full h-2">
                    <div
                      className={cn(
                        'h-full rounded-full',
                        Math.abs(fx.change) > 3 ? 'bg-red-400' : 'bg-indigo-400'
                      )}
                      style={{ width: `${(fx.exposure / maxExp) * 100}%` }}
                    />
                  </div>
                </div>
              )
            })}
          </div>
          <div className="mt-4 p-3 bg-indigo-50 rounded-lg">
            <div className="flex items-center justify-between">
              <span className="text-xs text-gray-600">Compliance Risk Score</span>
              <span className="text-lg font-bold text-indigo-700">{complianceScore}%</span>
            </div>
            <div className="w-full bg-indigo-100 rounded-full h-2 mt-1">
              <div
                className="h-full bg-indigo-600 rounded-full"
                style={{ width: `${complianceScore}%` }}
              />
            </div>
          </div>
        </div>
      </div>

      <div className="bg-white rounded-xl border border-gray-200 p-6">
        <div className="flex items-center gap-2 mb-5">
          <AlertTriangle className="w-5 h-5 text-indigo-600" />
          <h3 className="text-sm font-semibold text-gray-700">Risk Register</h3>
        </div>
        <div className="space-y-3">
          {risks.map((risk) => {
            const sev = severityColors[risk.severity]
            const StatusIcon = statusIcon[risk.status]
            return (
              <div
                key={risk.id}
                className={cn('p-4 rounded-lg border', sev.bg, 'border-opacity-50')}
              >
                <div className="flex items-start justify-between gap-4">
                  <div className="flex items-start gap-3 min-w-0">
                    <div className={cn('w-2 h-2 rounded-full mt-2 flex-shrink-0', sev.dot)} />
                    <div className="min-w-0">
                      <div className="flex items-center gap-2 flex-wrap">
                        <h4 className="text-sm font-semibold text-gray-900">{risk.name}</h4>
                        <span className={cn('text-[10px] font-semibold px-2 py-0.5 rounded-full', sev.bg, sev.text)}>
                          {risk.severity.toUpperCase()}
                        </span>
                      </div>
                      <p className="text-xs text-gray-600 mt-1">{risk.description}</p>
                      <div className="flex items-center gap-3 mt-2 text-[11px] text-gray-500">
                        <span>{risk.category}</span>
                        <span>&middot;</span>
                        <span>Owner: {risk.owner}</span>
                        <span>&middot;</span>
                        <span>Impact: {risk.impact}/5</span>
                        <span>&middot;</span>
                        <span>Likelihood: {risk.likelihood}/5</span>
                      </div>
                    </div>
                  </div>
                  <div className="flex items-center gap-1 flex-shrink-0">
                    <StatusIcon className={cn('w-4 h-4', risk.status === 'active' ? 'text-amber-500' : risk.status === 'mitigated' ? 'text-emerald-500' : 'text-blue-500')} />
                    <span className="text-[10px] text-gray-500 capitalize">{risk.status}</span>
                  </div>
                </div>
              </div>
            )
          })}
        </div>
      </div>
    </div>
  )
}
