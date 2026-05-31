import { useEffect } from 'react'
import {
  Users,
  Mail,
  Phone,
  Video,
  FileText,
  Calendar,
  ArrowUpRight,
  ArrowDownRight,
  Loader2,
  Award,
  TrendingUp,
  Clock,
  BarChart3,
} from 'lucide-react'
import { useCfoStore } from '@shared/store'
import { cn } from '@shared/utils'

const typeIcons: Record<string, React.ElementType> = {
  email: Mail,
  call: Phone,
  meeting: Video,
  report: FileText,
}

const shareColors = [
  'bg-indigo-500',
  'bg-violet-500',
  'bg-blue-500',
  'bg-emerald-500',
  'bg-amber-500',
  'bg-gray-400',
]

export default function InvestorPage() {
  const {
    investorMetrics,
    shareholders,
    communications,
    boardMeetings,
    isLoadingInvestor,
    loadInvestorData,
  } = useCfoStore()

  useEffect(() => {
    loadInvestorData()
  }, [loadInvestorData])

  if (isLoadingInvestor) {
    return (
      <div className="flex items-center justify-center h-96">
        <Loader2 className="w-8 h-8 text-indigo-500 animate-spin" />
      </div>
    )
  }

  const metricIcons: Record<string, React.ElementType> = {
    'Company Valuation': Award,
    'Revenue Run Rate': TrendingUp,
    'Burn Rate': BarChart3,
    'Runway': Clock,
  }

  return (
    <div className="space-y-6 max-w-7xl">
      <div>
        <h1 className="text-2xl font-bold text-gray-900">Investor Relations</h1>
        <p className="text-sm text-gray-500 mt-1">Investor metrics, shareholder data, and board communications</p>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
        {investorMetrics.map((metric) => {
          const Icon = metricIcons[metric.label] || TrendingUp
          const isPositive = metric.change > 0
          return (
            <div key={metric.label} className="bg-white rounded-xl border border-gray-200 p-5">
              <div className="flex items-center gap-3 mb-3">
                <div className="w-9 h-9 rounded-lg bg-indigo-50 flex items-center justify-center">
                  <Icon className="w-4 h-4 text-indigo-600" />
                </div>
                <div className={cn(
                  'flex items-center gap-0.5 text-xs font-medium px-1.5 py-0.5 rounded-full',
                  isPositive ? 'text-emerald-700 bg-emerald-50' : 'text-red-700 bg-red-50'
                )}>
                  {isPositive ? <ArrowUpRight className="w-3 h-3" /> : <ArrowDownRight className="w-3 h-3" />}
                  {Math.abs(metric.change)}%
                </div>
              </div>
              <p className="text-xs text-gray-500 mb-1">{metric.label}</p>
              <p className="text-xl font-bold text-gray-900">{metric.value}</p>
              <p className="text-[11px] text-gray-400 mt-0.5">{metric.changeLabel}</p>
            </div>
          )
        })}
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-2 gap-5">
        <div className="bg-white rounded-xl border border-gray-200 p-6">
          <div className="flex items-center gap-2 mb-5">
            <Users className="w-5 h-5 text-indigo-600" />
            <h3 className="text-sm font-semibold text-gray-700">Shareholder Composition</h3>
          </div>
          <div className="flex items-center gap-6">
            <div className="relative w-40 h-40 flex-shrink-0">
              <svg viewBox="0 0 100 100" className="w-40 h-40 -rotate-90">
                {shareholders.reduce(
                  (acc, sh, i) => {
                    const offset = acc.offset
                    const length = (sh.percentage / 100) * 314
                    acc.elements.push(
                      <circle
                        key={sh.name}
                        cx="50"
                        cy="50"
                        r="50"
                        fill="none"
                        className={shareColors[i]}
                        strokeWidth="20"
                        strokeDasharray={`${length} ${314 - length}`}
                        strokeDashoffset={-offset}
                      />
                    )
                    acc.offset = offset + length
                    return acc
                  },
                  { elements: [] as React.ReactNode[], offset: 0 }
                ).elements}
                <circle cx="50" cy="50" r="40" fill="white" />
              </svg>
              <div className="absolute inset-0 flex items-center justify-center">
                <div className="text-center">
                  <p className="text-lg font-bold text-gray-900">100%</p>
                  <p className="text-[10px] text-gray-500">Total</p>
                </div>
              </div>
            </div>
            <div className="flex-1 space-y-2">
              {shareholders.map((sh, i) => (
                <div key={sh.name} className="flex items-center gap-2">
                  <div className={cn('w-3 h-3 rounded-sm flex-shrink-0', shareColors[i])} />
                  <span className="text-xs text-gray-700 flex-1">{sh.name}</span>
                  <span className="text-xs font-semibold text-gray-900">{sh.percentage}%</span>
                </div>
              ))}
            </div>
          </div>
        </div>

        <div className="bg-white rounded-xl border border-gray-200 p-6">
          <div className="flex items-center gap-2 mb-5">
            <Calendar className="w-5 h-5 text-indigo-600" />
            <h3 className="text-sm font-semibold text-gray-700">Board Meetings</h3>
          </div>
          <div className="space-y-3">
            {boardMeetings.map((meeting) => (
              <div
                key={meeting.id}
                className={cn(
                  'p-4 rounded-lg border',
                  meeting.status === 'upcoming'
                    ? 'border-indigo-200 bg-indigo-50'
                    : meeting.status === 'completed'
                    ? 'border-gray-200 bg-gray-50'
                    : 'border-red-200 bg-red-50'
                )}
              >
                <div className="flex items-start justify-between">
                  <div>
                    <h4 className="text-sm font-semibold text-gray-900">{meeting.title}</h4>
                    <div className="flex items-center gap-2 mt-1 text-xs text-gray-500">
                      <Calendar className="w-3 h-3" />
                      <span>{meeting.date}</span>
                      <span>&middot;</span>
                      <span>{meeting.time}</span>
                    </div>
                    <p className="text-xs text-gray-500 mt-1">{meeting.attendees}</p>
                  </div>
                  <span
                    className={cn(
                      'text-[10px] font-semibold px-2 py-0.5 rounded-full',
                      meeting.status === 'upcoming'
                        ? 'bg-indigo-100 text-indigo-700'
                        : meeting.status === 'completed'
                        ? 'bg-gray-200 text-gray-600'
                        : 'bg-red-100 text-red-700'
                    )}
                  >
                    {meeting.status.toUpperCase()}
                  </span>
                </div>
              </div>
            ))}
          </div>
        </div>
      </div>

      <div className="bg-white rounded-xl border border-gray-200 p-6">
        <div className="flex items-center gap-2 mb-5">
          <Mail className="w-5 h-5 text-indigo-600" />
          <h3 className="text-sm font-semibold text-gray-700">Investor Communications</h3>
        </div>
        <div className="space-y-3">
          {communications.map((comm) => {
            const Icon = typeIcons[comm.type]
            return (
              <div key={comm.id} className="flex items-start gap-4 p-4 rounded-lg border border-gray-100 hover:border-indigo-200 transition-colors">
                <div className="w-9 h-9 rounded-lg bg-indigo-50 flex items-center justify-center flex-shrink-0">
                  <Icon className="w-4 h-4 text-indigo-600" />
                </div>
                <div className="min-w-0 flex-1">
                  <div className="flex items-start justify-between gap-2">
                    <div>
                      <h4 className="text-sm font-semibold text-gray-900">{comm.subject}</h4>
                      <p className="text-xs text-gray-500 mt-0.5">{comm.contact}</p>
                    </div>
                    <span className="text-[11px] text-gray-400 flex-shrink-0">{comm.date}</span>
                  </div>
                  <p className="text-xs text-gray-600 mt-1.5">{comm.summary}</p>
                </div>
              </div>
            )
          })}
        </div>
      </div>
    </div>
  )
}
