import { useEffect, useState } from 'react'
import {
  CheckCircle2,
  XCircle,
  RotateCcw,
  Clock,
  FileText,
  TrendingUp,
  Building2,
  Truck,
  AlertTriangle,
  Loader2,
  ChevronDown,
  ChevronUp,
} from 'lucide-react'
import { useCfoStore } from '@shared/store'
import { cn } from '@shared/utils'
import type { ApprovalItem } from '@shared/store/cfoStore'

const typeConfig: Record<string, { icon: React.ElementType; color: string; bg: string }> = {
  budget: { icon: FileText, color: 'text-blue-600', bg: 'bg-blue-50' },
  expense: { icon: TrendingUp, color: 'text-amber-600', bg: 'bg-amber-50' },
  investment: { icon: Building2, color: 'text-indigo-600', bg: 'bg-indigo-50' },
  vendor: { icon: Truck, color: 'text-emerald-600', bg: 'bg-emerald-50' },
}

const statusColors: Record<string, string> = {
  pending: 'bg-amber-100 text-amber-800',
  approved: 'bg-emerald-100 text-emerald-800',
  rejected: 'bg-red-100 text-red-800',
  changes_requested: 'bg-blue-100 text-blue-800',
}

const priorityColors: Record<string, string> = {
  high: 'bg-red-100 text-red-700',
  medium: 'bg-amber-100 text-amber-700',
  low: 'bg-gray-100 text-gray-600',
}

function formatAmount(amount: number, currency: string): string {
  if (currency === 'JPY') {
    return `¥${(amount / 1000).toFixed(0)}B`
  }
  if (amount >= 1000000) return `${(amount / 1000000).toFixed(1)}M ${currency}`
  if (amount >= 1000) return `${(amount / 1000).toFixed(0)}K ${currency}`
  return `${currency} ${amount.toLocaleString()}`
}

export default function ApprovalsPage() {
  const { approvals, isLoadingApprovals, loadApprovals, approveItem, rejectItem, requestChanges } = useCfoStore()
  const [expandedId, setExpandedId] = useState<string | null>(null)
  const [filterType, setFilterType] = useState<string>('all')
  const [filterStatus, setFilterStatus] = useState<string>('all')

  useEffect(() => {
    loadApprovals()
  }, [loadApprovals])

  if (isLoadingApprovals) {
    return (
      <div className="flex items-center justify-center h-96">
        <Loader2 className="w-8 h-8 text-indigo-500 animate-spin" />
      </div>
    )
  }

  const pending = approvals.filter((a) => a.status === 'pending')
  const countByType = (type: string) => pending.filter((a) => a.type === type).length
  const totalPendingAmount = pending.reduce((sum, a) => sum + a.amount, 0)

  const filtered = approvals.filter((a) => {
    if (filterType !== 'all' && a.type !== filterType) return false
    if (filterStatus !== 'all' && a.status !== filterStatus) return false
    return true
  })

  const history = approvals.filter((a) => a.status !== 'pending')

  return (
    <div className="space-y-6 max-w-7xl">
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-2xl font-bold text-gray-900">Approvals</h1>
          <p className="text-sm text-gray-500 mt-1">Review and manage pending financial approvals</p>
        </div>
        <div className="flex items-center gap-2 text-sm text-gray-600 bg-white px-4 py-2 rounded-lg border">
          <Clock className="w-4 h-4 text-amber-500" />
          <span className="font-medium">{pending.length}</span> pending &middot;
          <span className="font-medium">${(totalPendingAmount / 1000000).toFixed(1)}M</span> total
        </div>
      </div>

      <div className="grid grid-cols-2 lg:grid-cols-4 gap-4">
        {[
          { type: 'budget', label: 'Budget' },
          { type: 'expense', label: 'Expense' },
          { type: 'investment', label: 'Investment' },
          { type: 'vendor', label: 'Vendor' },
        ].map(({ type, label }) => {
          const cfg = typeConfig[type]
          const Icon = cfg.icon
          return (
            <div key={type} className="bg-white rounded-xl border border-gray-200 p-4">
              <div className="flex items-center gap-3">
                <div className={cn('w-10 h-10 rounded-lg flex items-center justify-center', cfg.bg)}>
                  <Icon className={cn('w-5 h-5', cfg.color)} />
                </div>
                <div>
                  <p className="text-2xl font-bold text-gray-900">{countByType(type)}</p>
                  <p className="text-xs text-gray-500">{label} Pending</p>
                </div>
              </div>
            </div>
          )
        })}
      </div>

      <div className="flex gap-2 flex-wrap">
        <select
          value={filterType}
          onChange={(e) => setFilterType(e.target.value)}
          className="px-3 py-2 text-sm border border-gray-300 rounded-lg bg-white focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 outline-none"
        >
          <option value="all">All Types</option>
          <option value="budget">Budget</option>
          <option value="expense">Expense</option>
          <option value="investment">Investment</option>
          <option value="vendor">Vendor</option>
        </select>
        <select
          value={filterStatus}
          onChange={(e) => setFilterStatus(e.target.value)}
          className="px-3 py-2 text-sm border border-gray-300 rounded-lg bg-white focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 outline-none"
        >
          <option value="all">All Status</option>
          <option value="pending">Pending</option>
          <option value="approved">Approved</option>
          <option value="rejected">Rejected</option>
          <option value="changes_requested">Changes Requested</option>
        </select>
      </div>

      <div className="space-y-3">
        {filtered.length === 0 && (
          <div className="bg-white rounded-xl border border-gray-200 p-12 text-center">
            <p className="text-gray-500">No approvals match your filters.</p>
          </div>
        )}
        {filtered.map((item) => {
          const cfg = typeConfig[item.type]
          const Icon = cfg.icon
          const isExpanded = expandedId === item.id
          return (
            <div key={item.id} className="bg-white rounded-xl border border-gray-200 overflow-hidden">
              <div className="p-5">
                <div className="flex items-start justify-between gap-4">
                  <div className="flex items-start gap-4 min-w-0">
                    <div className={cn('w-10 h-10 rounded-lg flex items-center justify-center flex-shrink-0', cfg.bg)}>
                      <Icon className={cn('w-5 h-5', cfg.color)} />
                    </div>
                    <div className="min-w-0">
                      <div className="flex items-center gap-2 flex-wrap">
                        <h3 className="text-sm font-semibold text-gray-900">{item.title}</h3>
                        <span className={cn('text-[10px] font-medium px-2 py-0.5 rounded-full', priorityColors[item.priority])}>
                          {item.priority.toUpperCase()}
                        </span>
                        <span className={cn('text-[10px] font-medium px-2 py-0.5 rounded-full', statusColors[item.status])}>
                          {item.status.replace('_', ' ').toUpperCase()}
                        </span>
                      </div>
                      <div className="flex items-center gap-3 mt-1 text-xs text-gray-500">
                        <span>{item.id}</span>
                        <span>&middot;</span>
                        <span>{item.country}</span>
                        <span>&middot;</span>
                        <span>{item.requestor}</span>
                        <span>&middot;</span>
                        <span>{item.date}</span>
                      </div>
                    </div>
                  </div>
                  <div className="flex items-center gap-3 flex-shrink-0">
                    <div className="text-right">
                      <p className="text-lg font-bold text-gray-900">{formatAmount(item.amount, item.currency)}</p>
                      <p className="text-xs text-gray-500">{item.department}</p>
                    </div>
                    <button
                      onClick={() => setExpandedId(isExpanded ? null : item.id)}
                      className="p-1.5 rounded-lg hover:bg-gray-100 transition-colors"
                    >
                      {isExpanded ? <ChevronUp className="w-4 h-4" /> : <ChevronDown className="w-4 h-4" />}
                    </button>
                  </div>
                </div>

                {isExpanded && (
                  <div className="mt-4 pt-4 border-t border-gray-100">
                    <p className="text-sm text-gray-600 mb-4">{item.description}</p>
                    {item.status === 'pending' && (
                      <div className="flex gap-2">
                        <button
                          onClick={() => approveItem(item.id)}
                          className="flex items-center gap-1.5 px-4 py-2 rounded-lg bg-emerald-600 hover:bg-emerald-700 text-white text-sm font-medium transition-colors"
                        >
                          <CheckCircle2 className="w-4 h-4" />
                          Approve
                        </button>
                        <button
                          onClick={() => rejectItem(item.id)}
                          className="flex items-center gap-1.5 px-4 py-2 rounded-lg bg-red-600 hover:bg-red-700 text-white text-sm font-medium transition-colors"
                        >
                          <XCircle className="w-4 h-4" />
                          Reject
                        </button>
                        <button
                          onClick={() => requestChanges(item.id)}
                          className="flex items-center gap-1.5 px-4 py-2 rounded-lg bg-gray-600 hover:bg-gray-700 text-white text-sm font-medium transition-colors"
                        >
                          <RotateCcw className="w-4 h-4" />
                          Request Changes
                        </button>
                      </div>
                    )}
                  </div>
                )}
              </div>
            </div>
          )
        })}
      </div>

      {history.length > 0 && (
        <div className="bg-white rounded-xl border border-gray-200 p-5">
          <h3 className="text-sm font-semibold text-gray-700 mb-3 flex items-center gap-2">
            <AlertTriangle className="w-4 h-4 text-gray-500" />
            Recent History
          </h3>
          <div className="overflow-x-auto">
            <table className="w-full text-sm">
              <thead>
                <tr className="text-left text-xs text-gray-500 border-b border-gray-100">
                  <th className="pb-2 font-medium">ID</th>
                  <th className="pb-2 font-medium">Title</th>
                  <th className="pb-2 font-medium">Amount</th>
                  <th className="pb-2 font-medium">Date</th>
                  <th className="pb-2 font-medium">Status</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-gray-50">
                {history.map((item) => (
                  <tr key={item.id}>
                    <td className="py-2 text-gray-600">{item.id}</td>
                    <td className="py-2 text-gray-900">{item.title}</td>
                    <td className="py-2 text-gray-900 font-medium">{formatAmount(item.amount, item.currency)}</td>
                    <td className="py-2 text-gray-500">{item.date}</td>
                    <td className="py-2">
                      <span className={cn('text-[10px] font-medium px-2 py-0.5 rounded-full', statusColors[item.status])}>
                        {item.status.replace('_', ' ').toUpperCase()}
                      </span>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      )}
    </div>
  )
}
