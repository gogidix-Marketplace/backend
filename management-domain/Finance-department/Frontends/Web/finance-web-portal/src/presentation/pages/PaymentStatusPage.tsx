import { useState } from 'react'
import { CreditCard, Clock, CheckCircle, XCircle, ArrowUpRight, Filter } from 'lucide-react'
import { formatCurrency, formatDate } from '@shared/utils/cn'

interface Payment {
  id: string
  title: string
  amount: number
  currency: string
  date: string
  processedDate?: string
  method: string
  status: 'processing' | 'completed' | 'failed' | 'scheduled'
  reference: string
}

const MOCK_PAYMENTS: Payment[] = [
  { id: 'PAY-001', title: 'Travel Reimbursement - London', amount: 1250, currency: 'GBP', date: '2026-03-12', processedDate: '2026-03-15', method: 'Bank Transfer', status: 'completed', reference: 'TXN-GB-20260315-001' },
  { id: 'PAY-002', title: 'Software License Refund', amount: 49.99, currency: 'USD', date: '2026-03-14', method: 'PayPal', status: 'processing', reference: 'TXN-US-20260314-002' },
  { id: 'PAY-003', title: 'Team Lunch Reimbursement', amount: 85.50, currency: 'USD', date: '2026-03-10', processedDate: '2026-03-12', method: 'Bank Transfer', status: 'completed', reference: 'TXN-US-20260312-003' },
  { id: 'PAY-004', title: 'Office Supplies', amount: 234, currency: 'USD', date: '2026-03-08', processedDate: '2026-03-10', method: 'Bank Transfer', status: 'completed', reference: 'TXN-US-20260310-004' },
  { id: 'PAY-005', title: 'Training Course Fee', amount: 500, currency: 'USD', date: '2026-03-16', method: 'Bank Transfer', status: 'scheduled', reference: 'TXN-US-20260316-005' },
  { id: 'PAY-006', title: 'Client Dinner', amount: 180, currency: 'GBP', date: '2026-03-05', method: 'Bank Transfer', status: 'failed', reference: 'TXN-GB-20260305-006' },
]

const STATUS_CONFIG = {
  processing: { label: 'Processing', color: 'bg-blue-100 text-blue-700', icon: Clock },
  completed: { label: 'Completed', color: 'bg-emerald-100 text-emerald-700', icon: CheckCircle },
  failed: { label: 'Failed', color: 'bg-red-100 text-red-700', icon: XCircle },
  scheduled: { label: 'Scheduled', color: 'bg-amber-100 text-amber-700', icon: ArrowUpRight },
}

export function PaymentStatusPage() {
  const [statusFilter, setStatusFilter] = useState('all')

  const filtered = MOCK_PAYMENTS.filter(p => statusFilter === 'all' || p.status === statusFilter)
  const totalPaid = MOCK_PAYMENTS.filter(p => p.status === 'completed').reduce((s, p) => s + p.amount, 0)
  const totalPending = MOCK_PAYMENTS.filter(p => p.status === 'processing' || p.status === 'scheduled').reduce((s, p) => s + p.amount, 0)

  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-2xl font-bold text-slate-900">Payment Status</h1>
        <p className="text-slate-500">Track reimbursement payments and transactions</p>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
        <div className="bg-white rounded-xl border border-slate-200 p-5">
          <p className="text-sm text-slate-500 mb-1">Total Received</p>
          <p className="text-2xl font-bold text-emerald-600">{formatCurrency(totalPaid)}</p>
        </div>
        <div className="bg-white rounded-xl border border-slate-200 p-5">
          <p className="text-sm text-slate-500 mb-1">Pending Payments</p>
          <p className="text-2xl font-bold text-amber-600">{formatCurrency(totalPending)}</p>
        </div>
        <div className="bg-white rounded-xl border border-slate-200 p-5">
          <p className="text-sm text-slate-500 mb-1">Total Transactions</p>
          <p className="text-2xl font-bold text-slate-900">{MOCK_PAYMENTS.length}</p>
        </div>
      </div>

      <div className="flex items-center gap-4">
        <div className="flex items-center gap-2">
          <Filter size={16} className="text-slate-400" />
          <select value={statusFilter} onChange={(e) => setStatusFilter(e.target.value)} className="px-3 py-2 border border-slate-200 rounded-lg text-sm">
            <option value="all">All Status</option>
            <option value="processing">Processing</option>
            <option value="completed">Completed</option>
            <option value="scheduled">Scheduled</option>
            <option value="failed">Failed</option>
          </select>
        </div>
      </div>

      {filtered.length === 0 ? (
        <div className="bg-white rounded-xl border border-slate-200 p-12 text-center">
          <CreditCard size={48} className="mx-auto text-slate-300 mb-4" />
          <p className="text-slate-500">No payments found</p>
        </div>
      ) : (
        <div className="bg-white rounded-xl border border-slate-200 overflow-hidden">
          <table className="w-full">
            <thead>
              <tr className="border-b border-slate-200 bg-slate-50">
                <th className="text-left px-4 py-3 text-xs font-medium text-slate-500 uppercase tracking-wider">Transaction</th>
                <th className="text-left px-4 py-3 text-xs font-medium text-slate-500 uppercase tracking-wider">Reference</th>
                <th className="text-left px-4 py-3 text-xs font-medium text-slate-500 uppercase tracking-wider">Method</th>
                <th className="text-left px-4 py-3 text-xs font-medium text-slate-500 uppercase tracking-wider">Date</th>
                <th className="text-right px-4 py-3 text-xs font-medium text-slate-500 uppercase tracking-wider">Amount</th>
                <th className="text-center px-4 py-3 text-xs font-medium text-slate-500 uppercase tracking-wider">Status</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-slate-100">
              {filtered.map(payment => {
                const statusCfg = STATUS_CONFIG[payment.status]
                const StatusIcon = statusCfg.icon
                return (
                  <tr key={payment.id} className="hover:bg-slate-50">
                    <td className="px-4 py-3">
                      <p className="text-sm font-medium text-slate-900">{payment.title}</p>
                      <p className="text-xs text-slate-500">{payment.id}</p>
                    </td>
                    <td className="px-4 py-3">
                      <p className="text-xs text-slate-600 font-mono">{payment.reference}</p>
                    </td>
                    <td className="px-4 py-3">
                      <p className="text-sm text-slate-600">{payment.method}</p>
                    </td>
                    <td className="px-4 py-3">
                      <p className="text-sm text-slate-600">{formatDate(payment.date)}</p>
                      {payment.processedDate && (
                        <p className="text-xs text-slate-400">Processed: {formatDate(payment.processedDate)}</p>
                      )}
                    </td>
                    <td className="px-4 py-3 text-right">
                      <p className="text-sm font-semibold text-slate-900">{formatCurrency(payment.amount, payment.currency)}</p>
                    </td>
                    <td className="px-4 py-3 text-center">
                      <span className={`inline-flex items-center gap-1 px-2 py-1 rounded-full text-xs font-medium ${statusCfg.color}`}>
                        <StatusIcon size={12} />
                        {statusCfg.label}
                      </span>
                    </td>
                  </tr>
                )
              })}
            </tbody>
          </table>
        </div>
      )}
    </div>
  )
}
