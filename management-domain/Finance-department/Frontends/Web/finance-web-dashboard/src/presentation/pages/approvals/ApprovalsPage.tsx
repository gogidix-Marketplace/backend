// ============================================
// FINANCE DEPARTMENT - APPROVALS PAGE
// ============================================

import { useState, useEffect } from 'react'
import { useFinanceStore } from '@store'
import { formatCurrency, formatDate, cn, getRelativeTime } from '@shared/utils/cn'
import {
  CheckCircle,
  XCircle,
  Clock,
  AlertCircle,
  FileText,
  Wallet,
  Receipt,
  User,
  Calendar,
  MessageSquare,
  Search,
  Filter,
} from 'lucide-react'

type ApprovalType = 'budget' | 'expense' | 'invoice' | 'payment'
type ApprovalStatus = 'pending' | 'approved' | 'rejected'

interface ApprovalRequest {
  id: string
  type: ApprovalType
  title: string
  description: string
  amount: number
  currency: string
  requestedBy: string
  requestedAt: string
  priority: 'low' | 'medium' | 'high' | 'urgent'
  status: ApprovalStatus
  department: string
  comments: number
}

const mockApprovals: ApprovalRequest[] = [
  {
    id: '1',
    type: 'expense',
    title: 'Client Travel - New York',
    description: 'Flight and accommodation for client meeting',
    amount: 2450,
    currency: 'USD',
    requestedBy: 'John Davis',
    requestedAt: '2025-03-11T10:30:00Z',
    priority: 'high',
    status: 'pending',
    department: 'Sales',
    comments: 0,
  },
  {
    id: '2',
    type: 'budget',
    title: 'Q2 Marketing Budget',
    description: 'Additional budget for digital marketing campaigns',
    amount: 50000,
    currency: 'USD',
    requestedBy: 'Amanda Peters',
    requestedAt: '2025-03-11T09:15:00Z',
    priority: 'medium',
    status: 'pending',
    department: 'Digital Marketing',
    comments: 2,
  },
  {
    id: '3',
    type: 'invoice',
    title: 'Vendor Payment - AWS',
    description: 'Monthly cloud services invoice',
    amount: 3200,
    currency: 'USD',
    requestedBy: 'David Wilson',
    requestedAt: '2025-03-10T16:45:00Z',
    priority: 'urgent',
    status: 'pending',
    department: 'IT',
    comments: 0,
  },
  {
    id: '4',
    type: 'expense',
    title: 'Office Equipment',
    description: 'New monitors for design team',
    amount: 1800,
    currency: 'USD',
    requestedBy: 'Sarah Mitchell',
    requestedAt: '2025-03-10T14:20:00Z',
    priority: 'medium',
    status: 'approved',
    department: 'Finance',
    comments: 1,
  },
  {
    id: '5',
    type: 'payment',
    title: 'Vendor Payment - Microsoft',
    description: 'Annual software license payment',
    amount: 12000,
    currency: 'USD',
    requestedBy: 'Mike Chen',
    requestedAt: '2025-03-09T11:00:00Z',
    priority: 'high',
    status: 'pending',
    department: 'Engineering',
    comments: 0,
  },
  {
    id: '6',
    type: 'expense',
    title: 'Team Training',
    description: 'React advanced training course',
    amount: 299,
    currency: 'USD',
    requestedBy: 'Lisa Brown',
    requestedAt: '2025-03-09T08:30:00Z',
    priority: 'low',
    status: 'rejected',
    department: 'Digital Marketing',
    comments: 1,
  },
]

const typeConfig: Record<ApprovalType, { icon: React.ReactNode; color: string; label: string }> = {
  budget: { icon: <Wallet size={20} />, color: 'purple', label: 'Budget' },
  expense: { icon: <Receipt size={20} />, color: 'amber', label: 'Expense' },
  invoice: { icon: <FileText size={20} />, color: 'blue', label: 'Invoice' },
  payment: { icon: <FileText size={20} />, color: 'emerald', label: 'Payment' },
}

const priorityConfig: Record<ApprovalRequest['priority'], { color: string; label: string }> = {
  low: { color: 'bg-slate-100 text-slate-700', label: 'Low' },
  medium: { color: 'bg-blue-100 text-blue-700', label: 'Medium' },
  high: { color: 'bg-amber-100 text-amber-700', label: 'High' },
  urgent: { color: 'bg-red-100 text-red-700', label: 'Urgent' },
}

export function ApprovalsPage() {
  const { approvals, approvalsLoading, approveRequest, rejectRequest } = useFinanceStore()
  const [localApprovals, setLocalApprovals] = useState<ApprovalRequest[]>(mockApprovals)
  const [searchTerm, setSearchTerm] = useState('')
  const [typeFilter, setTypeFilter] = useState<ApprovalType | 'all'>('all')
  const [statusFilter, setStatusFilter] = useState<ApprovalStatus | 'all'>('all')
  const [showFilters, setShowFilters] = useState(false)
  const [selectedApproval, setSelectedApproval] = useState<ApprovalRequest | null>(null)
  const [showRejectDialog, setShowRejectDialog] = useState(false)
  const [rejectReason, setRejectReason] = useState('')

  const filteredApprovals = localApprovals.filter((approval) => {
    const matchesSearch =
      approval.title.toLowerCase().includes(searchTerm.toLowerCase()) ||
      approval.description.toLowerCase().includes(searchTerm.toLowerCase()) ||
      approval.requestedBy.toLowerCase().includes(searchTerm.toLowerCase())
    const matchesType = typeFilter === 'all' || approval.type === typeFilter
    const matchesStatus = statusFilter === 'all' || approval.status === statusFilter
    return matchesSearch && matchesType && matchesStatus
  })

  const pendingCount = localApprovals.filter((a) => a.status === 'pending').length
  const todayCount = localApprovals.filter((a) => {
    const today = new Date().toDateString()
    return new Date(a.requestedAt).toDateString() === today && a.status === 'pending'
  }).length

  const handleApprove = async (id: string) => {
    await approveRequest(id)
    setLocalApprovals((prev) =>
      prev.map((a) => (a.id === id ? { ...a, status: 'approved' as ApprovalStatus } : a))
    )
  }

  const handleReject = async () => {
    if (!selectedApproval || !rejectReason.trim()) return
    await rejectRequest(selectedApproval.id, rejectReason)
    setLocalApprovals((prev) =>
      prev.map((a) => (a.id === selectedApproval.id ? { ...a, status: 'rejected' as ApprovalStatus } : a))
    )
    setShowRejectDialog(false)
    setRejectReason('')
    setSelectedApproval(null)
  }

  const openRejectDialog = (approval: ApprovalRequest) => {
    setSelectedApproval(approval)
    setShowRejectDialog(true)
  }

  return (
    <div className="space-y-6">
      {/* Page Header */}
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-2xl font-bold text-slate-900">Approvals</h1>
          <p className="text-slate-500">Review and approve pending requests</p>
        </div>
      </div>

      {/* Summary Cards */}
      <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
        <div className="bg-white rounded-xl border border-slate-200 p-4">
          <div className="flex items-center gap-3">
            <div className="w-12 h-12 bg-amber-100 rounded-lg flex items-center justify-center">
              <Clock size={24} className="text-amber-600" />
            </div>
            <div>
              <p className="text-2xl font-bold text-slate-900">{pendingCount}</p>
              <p className="text-sm text-slate-500">Pending</p>
            </div>
          </div>
        </div>
        <div className="bg-white rounded-xl border border-slate-200 p-4">
          <div className="flex items-center gap-3">
            <div className="w-12 h-12 bg-emerald-100 rounded-lg flex items-center justify-center">
              <CheckCircle size={24} className="text-emerald-600" />
            </div>
            <div>
              <p className="text-2xl font-bold text-slate-900">
                {localApprovals.filter((a) => a.status === 'approved').length}
              </p>
              <p className="text-sm text-slate-500">Approved</p>
            </div>
          </div>
        </div>
        <div className="bg-white rounded-xl border border-slate-200 p-4">
          <div className="flex items-center gap-3">
            <div className="w-12 h-12 bg-red-100 rounded-lg flex items-center justify-center">
              <XCircle size={24} className="text-red-600" />
            </div>
            <div>
              <p className="text-2xl font-bold text-slate-900">
                {localApprovals.filter((a) => a.status === 'rejected').length}
              </p>
              <p className="text-sm text-slate-500">Rejected</p>
            </div>
          </div>
        </div>
        <div className="bg-white rounded-xl border border-slate-200 p-4">
          <div className="flex items-center gap-3">
            <div className="w-12 h-12 bg-blue-100 rounded-lg flex items-center justify-center">
              <AlertCircle size={24} className="text-blue-600" />
            </div>
            <div>
              <p className="text-2xl font-bold text-slate-900">{todayCount}</p>
              <p className="text-sm text-slate-500">Today</p>
            </div>
          </div>
        </div>
      </div>

      {/* Urgent Approvals */}
      {localApprovals.filter((a) => a.status === 'pending' && a.priority === 'urgent').length > 0 && (
        <div className="bg-red-50 border border-red-200 rounded-xl p-4">
          <div className="flex items-center gap-2 mb-3">
            <AlertCircle size={20} className="text-red-600" />
            <h3 className="font-semibold text-red-900">Urgent Approvals Required</h3>
          </div>
          <div className="space-y-2">
            {localApprovals
              .filter((a) => a.status === 'pending' && a.priority === 'urgent')
              .map((approval) => (
                <div
                  key={approval.id}
                  className="flex items-center justify-between bg-white rounded-lg p-3 border border-red-200"
                >
                  <div className="flex items-center gap-3">
                    {typeConfig[approval.type].icon}
                    <div>
                      <p className="font-medium text-slate-900">{approval.title}</p>
                      <p className="text-sm text-slate-500">{approval.description}</p>
                    </div>
                  </div>
                  <div className="flex items-center gap-4">
                    <p className="font-bold text-slate-900">{formatCurrency(approval.amount, approval.currency)}</p>
                    <button
                      onClick={() => handleApprove(approval.id)}
                      className="px-3 py-1.5 bg-emerald-600 hover:bg-emerald-700 text-white rounded-lg text-sm font-medium"
                    >
                      Approve
                    </button>
                  </div>
                </div>
              ))}
          </div>
        </div>
      )}

      {/* Filters */}
      <div className="bg-white rounded-xl border border-slate-200 p-4">
        <div className="flex flex-col md:flex-row gap-4">
          <div className="flex-1 relative">
            <Search size={20} className="absolute left-3 top-1/2 -translate-y-1/2 text-slate-400" />
            <input
              type="text"
              placeholder="Search approvals by title, description, or requester..."
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              className="w-full pl-10 pr-4 py-2 border border-slate-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
            />
          </div>
          <div className="flex gap-2">
            <button
              onClick={() => setShowFilters(!showFilters)}
              className="flex items-center gap-2 px-4 py-2 border border-slate-300 rounded-lg hover:bg-slate-50 transition-colors"
            >
              <Filter size={20} />
              Filters
            </button>
          </div>
        </div>

        {showFilters && (
          <div className="mt-4 pt-4 border-t border-slate-200 flex flex-wrap gap-4">
            <div>
              <label className="block text-sm font-medium text-slate-700 mb-1">Type</label>
              <select
                value={typeFilter}
                onChange={(e) => setTypeFilter(e.target.value as ApprovalType | 'all')}
                className="px-3 py-2 border border-slate-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
              >
                <option value="all">All Types</option>
                <option value="budget">Budget</option>
                <option value="expense">Expense</option>
                <option value="invoice">Invoice</option>
                <option value="payment">Payment</option>
              </select>
            </div>
            <div>
              <label className="block text-sm font-medium text-slate-700 mb-1">Status</label>
              <select
                value={statusFilter}
                onChange={(e) => setStatusFilter(e.target.value as ApprovalStatus | 'all')}
                className="px-3 py-2 border border-slate-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
              >
                <option value="all">All Statuses</option>
                <option value="pending">Pending</option>
                <option value="approved">Approved</option>
                <option value="rejected">Rejected</option>
              </select>
            </div>
          </div>
        )}
      </div>

      {/* Approvals List */}
      <div className="space-y-4">
        {filteredApprovals.length === 0 ? (
          <div className="bg-white rounded-xl border border-slate-200 p-12 text-center">
            <p className="text-slate-500">No approvals found matching your criteria.</p>
          </div>
        ) : (
          filteredApprovals.map((approval) => {
            const typeInfo = typeConfig[approval.type]
            const priorityInfo = priorityConfig[approval.priority]
            const isPending = approval.status === 'pending'

            return (
              <div
                key={approval.id}
                className={cn(
                  'bg-white rounded-xl border p-5 transition-all',
                  isPending ? 'border-slate-200 hover:border-blue-300 hover:shadow-md' : 'border-slate-200 opacity-75'
                )}
              >
                <div className="flex items-start justify-between">
                  <div className="flex items-start gap-4">
                    <div className={cn(
                      'p-3 rounded-lg',
                      approval.type === 'budget' && 'bg-purple-100',
                      approval.type === 'expense' && 'bg-amber-100',
                      approval.type === 'invoice' && 'bg-blue-100',
                      approval.type === 'payment' && 'bg-emerald-100'
                    )}>
                      {typeInfo.icon}
                    </div>

                    <div className="flex-1">
                      <div className="flex items-center gap-3 mb-1">
                        <h3 className="font-semibold text-slate-900">{approval.title}</h3>
                        <span className={cn('px-2 py-0.5 rounded-full text-xs font-medium', priorityInfo.color)}>
                          {priorityInfo.label}
                        </span>
                        <span className="px-2 py-0.5 bg-slate-100 text-slate-600 rounded-full text-xs font-medium">
                          {typeInfo.label}
                        </span>
                        {approval.status !== 'pending' && (
                          <span className={cn(
                            'px-2 py-0.5 rounded-full text-xs font-medium',
                            approval.status === 'approved' ? 'bg-emerald-100 text-emerald-700' : 'bg-red-100 text-red-700'
                          )}>
                            {approval.status.charAt(0).toUpperCase() + approval.status.slice(1)}
                          </span>
                        )}
                      </div>
                      <p className="text-slate-600 mb-3">{approval.description}</p>

                      <div className="flex flex-wrap items-center gap-4 text-sm text-slate-500">
                        <div className="flex items-center gap-1.5">
                          <User size={16} />
                          <span>{approval.requestedBy}</span>
                        </div>
                        <div className="flex items-center gap-1.5">
                          <Calendar size={16} />
                          <span>{getRelativeTime(approval.requestedAt)}</span>
                        </div>
                        {approval.comments > 0 && (
                          <div className="flex items-center gap-1.5">
                            <MessageSquare size={16} />
                            <span>{approval.comments} comments</span>
                          </div>
                        )}
                        <span>• {approval.department}</span>
                      </div>
                    </div>
                  </div>

                  <div className="text-right">
                    <p className="text-xl font-bold text-slate-900">
                      {formatCurrency(approval.amount, approval.currency)}
                    </p>

                    {isPending && (
                      <div className="flex items-center gap-2 mt-3 justify-end">
                        <button
                          onClick={() => openRejectDialog(approval)}
                          className="p-2 border border-red-300 text-red-600 hover:bg-red-50 rounded-lg transition-colors"
                          title="Reject"
                        >
                          <XCircle size={20} />
                        </button>
                        <button
                          onClick={() => handleApprove(approval.id)}
                          className="p-2 bg-emerald-600 text-white hover:bg-emerald-700 rounded-lg transition-colors"
                          title="Approve"
                        >
                          <CheckCircle size={20} />
                        </button>
                      </div>
                    )}
                  </div>
                </div>
              </div>
            )
          })
        )}
      </div>

      {/* Reject Dialog */}
      {showRejectDialog && selectedApproval && (
        <div className="fixed inset-0 bg-black/50 flex items-center justify-center z-50">
          <div className="bg-white rounded-xl p-6 max-w-md w-full mx-4">
            <h3 className="text-lg font-semibold text-slate-900 mb-2">Reject Request</h3>
            <p className="text-slate-600 mb-4">
              Please provide a reason for rejecting <strong>{selectedApproval.title}</strong>
            </p>
            <textarea
              value={rejectReason}
              onChange={(e) => setRejectReason(e.target.value)}
              placeholder="Enter rejection reason..."
              rows={4}
              className="w-full px-3 py-2 border border-slate-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 resize-none"
            />
            <div className="flex justify-end gap-3 mt-4">
              <button
                onClick={() => {
                  setShowRejectDialog(false)
                  setRejectReason('')
                  setSelectedApproval(null)
                }}
                className="px-4 py-2 border border-slate-300 rounded-lg hover:bg-slate-50 font-medium"
              >
                Cancel
              </button>
              <button
                onClick={handleReject}
                disabled={!rejectReason.trim()}
                className={cn(
                  'px-4 py-2 bg-red-600 text-white rounded-lg font-medium',
                  !rejectReason.trim() && 'opacity-50 cursor-not-allowed'
                )}
              >
                Reject
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  )
}
