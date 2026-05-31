// ============================================
// FINANCE PORTAL - MY EXPENSES PAGE
// ============================================

import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { usePortalStore, useAuthStore } from '@shared/store/portalStore'
import { formatCurrency, formatDate } from '@shared/utils'
import { Plus, Search, Receipt, Eye, Clock, CheckCircle, XCircle } from 'lucide-react'

const STATUS_CONFIG = {
  draft: { label: 'Draft', color: 'bg-slate-100 text-slate-700', icon: null },
  pending: { label: 'Pending', color: 'bg-amber-100 text-amber-700', icon: Clock },
  approved: { label: 'Approved', color: 'bg-blue-100 text-blue-700', icon: CheckCircle },
  rejected: { label: 'Rejected', color: 'bg-red-100 text-red-700', icon: XCircle },
  paid: { label: 'Paid', color: 'bg-emerald-100 text-emerald-700', icon: CheckCircle },
}

export function MyExpensesPage() {
  const navigate = useNavigate()
  const { employee } = useAuthStore()
  const { myExpenses, loading, loadMyExpenses } = usePortalStore()
  const [searchTerm, setSearchTerm] = useState('')
  const [statusFilter, setStatusFilter] = useState<string>('all')

  useEffect(() => {
    loadMyExpenses()
  }, [loadMyExpenses])

  const filteredExpenses = myExpenses.filter((expense) => {
    const matchesSearch =
      expense.title.toLowerCase().includes(searchTerm.toLowerCase()) ||
      expense.description.toLowerCase().includes(searchTerm.toLowerCase())
    const matchesStatus = statusFilter === 'all' || expense.status === statusFilter
    return matchesSearch && matchesStatus
  })

  const totalPending = myExpenses.filter((e) => e.status === 'pending').length
  const totalApproved = myExpenses.filter((e) => e.status === 'approved').length
  const totalPaid = myExpenses
    .filter((e) => e.status === 'paid')
    .reduce((sum, e) => sum + e.amount, 0)

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-2xl font-bold text-slate-900">My Expenses</h1>
          <p className="text-slate-500">View and manage your submitted expenses</p>
        </div>
        <button
          onClick={() => navigate('/submit')}
          className="flex items-center gap-2 px-4 py-2 bg-blue-600 hover:bg-blue-700 text-white rounded-lg font-medium"
        >
          <Plus size={20} />
          New Expense
        </button>
      </div>

      {/* Summary Cards */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
        <div className="bg-white rounded-xl border border-slate-200 p-4">
          <p className="text-sm text-slate-500">Pending Approval</p>
          <p className="text-2xl font-bold text-amber-600">{totalPending}</p>
        </div>
        <div className="bg-white rounded-xl border border-slate-200 p-4">
          <p className="text-sm text-slate-500">Approved</p>
          <p className="text-2xl font-bold text-blue-600">{totalApproved}</p>
        </div>
        <div className="bg-white rounded-xl border border-slate-200 p-4">
          <p className="text-sm text-slate-500">Total Paid</p>
          <p className="text-2xl font-bold text-emerald-600">
            {formatCurrency(totalPaid, employee?.country === 'GB' ? 'GBP' : employee?.country === 'NG' ? 'NGN' : 'USD')}
          </p>
        </div>
      </div>

      {/* Filters */}
      <div className="bg-white rounded-xl border border-slate-200 p-4">
        <div className="flex flex-col md:flex-row gap-4">
          <div className="flex-1 relative">
            <Search size={20} className="absolute left-3 top-1/2 -translate-y-1/2 text-slate-400" />
            <input
              type="text"
              placeholder="Search expenses..."
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              className="w-full pl-10 pr-4 py-2 border border-slate-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
            />
          </div>
          <select
            value={statusFilter}
            onChange={(e) => setStatusFilter(e.target.value)}
            className="px-4 py-2 border border-slate-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
          >
            <option value="all">All Statuses</option>
            <option value="pending">Pending</option>
            <option value="approved">Approved</option>
            <option value="rejected">Rejected</option>
            <option value="paid">Paid</option>
          </select>
        </div>
      </div>

      {/* Expenses List */}
      {loading ? (
        <div className="flex justify-center py-12">
          <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600" />
        </div>
      ) : filteredExpenses.length === 0 ? (
        <div className="bg-white rounded-xl border border-slate-200 p-12 text-center">
          <Receipt size={48} className="mx-auto text-slate-300 mb-4" />
          <p className="text-slate-500 mb-4">No expenses found</p>
          <button
            onClick={() => navigate('/submit')}
            className="px-4 py-2 bg-blue-600 text-white rounded-lg font-medium"
          >
            Submit Your First Expense
          </button>
        </div>
      ) : (
        <div className="space-y-4">
          {filteredExpenses.map((expense) => {
            const status = STATUS_CONFIG[expense.status]
            const StatusIcon = status.icon

            return (
              <div
                key={expense.id}
                className="bg-white rounded-xl border border-slate-200 p-4 hover:border-slate-300 transition-colors"
              >
                <div className="flex items-start justify-between">
                  <div className="flex items-start gap-4">
                    <div className="p-2 bg-slate-100 rounded-lg">
                      <Receipt size={20} className="text-slate-600" />
                    </div>
                    <div>
                      <h3 className="font-semibold text-slate-900">{expense.title}</h3>
                      {expense.description && (
                        <p className="text-sm text-slate-600 mt-1">{expense.description}</p>
                      )}
                      <div className="flex items-center gap-4 mt-2 text-sm text-slate-500">
                        <span>{formatDate(expense.date)}</span>
                        {expense.projectCode && <span>• {expense.projectCode}</span>}
                      </div>
                    </div>
                  </div>
                  <div className="text-right">
                    <p className="text-lg font-bold text-slate-900">
                      {formatCurrency(expense.amount, expense.currency)}
                    </p>
                    <span className={`inline-flex items-center gap-1 px-2 py-1 rounded-full text-xs font-medium mt-1 ${status.color}`}>
                      {StatusIcon && <StatusIcon size={14} />}
                      {status.label}
                    </span>
                    {expense.reimbursable && expense.status === 'approved' && (
                      <span className="ml-1 inline-flex items-center gap-1 px-2 py-1 rounded-full text-xs font-medium bg-purple-100 text-purple-700">
                        Reimbursable
                      </span>
                    )}
                  </div>
                </div>
                {expense.receiptUrl && (
                  <div className="mt-3 pt-3 border-t border-slate-100">
                    <button className="flex items-center gap-2 text-sm text-blue-600 hover:text-blue-700">
                      <Eye size={16} />
                      View Receipt
                    </button>
                  </div>
                )}
              </div>
            )
          })}
        </div>
      )}
    </div>
  )
}
