import { useEffect, useState } from 'react'
import { useAccountantStore } from '@shared/store'
import { formatCurrency, formatDate, cn } from '@shared/utils/cn'
import { CreditCard, Loader2, Send, CheckCircle, Clock, ArrowRight } from 'lucide-react'

export default function PaymentsPage() {
  const { payments, loading, loadPayments } = useAccountantStore()
  const [activeSection, setActiveSection] = useState<'queue' | 'history' | 'batch'>('queue')

  useEffect(() => { loadPayments() }, [])

  const pendingPayments = payments.filter((p) => p.status === 'pending' || p.status === 'scheduled')
  const completedPayments = payments.filter((p) => p.status === 'completed')

  const methodBreakdown = [
    { method: 'Wire Transfer', count: payments.filter((p) => p.method === 'wire').length, amount: payments.filter((p) => p.method === 'wire').reduce((s, p) => s + p.amount, 0), color: 'bg-teal-500' },
    { method: 'ACH', count: payments.filter((p) => p.method === 'ach').length, amount: payments.filter((p) => p.method === 'ach').reduce((s, p) => s + p.amount, 0), color: 'bg-blue-500' },
    { method: 'Check', count: payments.filter((p) => p.method === 'check').length, amount: payments.filter((p) => p.method === 'check').reduce((s, p) => s + p.amount, 0), color: 'bg-amber-500' },
    { method: 'Card', count: payments.filter((p) => p.method === 'card').length, amount: payments.filter((p) => p.method === 'card').reduce((s, p) => s + p.amount, 0), color: 'bg-purple-500' },
  ]

  if (loading.payments) {
    return (
      <div className="flex items-center justify-center h-64">
        <Loader2 className="animate-spin text-teal-600" size={32} />
      </div>
    )
  }

  return (
    <div className="space-y-6">
      <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
        <div>
          <h2 className="text-xl font-bold text-gray-900">Payment Processing</h2>
          <p className="text-sm text-gray-500">Manage outgoing payments and track payment history</p>
        </div>
        <div className="flex gap-2">
          <button className="inline-flex items-center gap-2 px-4 py-2 bg-teal-600 hover:bg-teal-700 text-white text-sm font-medium rounded-lg transition-colors">
            <Send size={16} />
            Process All Pending
          </button>
        </div>
      </div>

      <div className="grid grid-cols-1 sm:grid-cols-3 gap-4">
        <div className="bg-white rounded-xl border border-gray-200 p-5">
          <div className="flex items-center gap-2 mb-3">
            <Clock size={18} className="text-amber-600" />
            <span className="text-sm font-medium text-gray-500">Pending</span>
          </div>
          <p className="text-2xl font-bold text-gray-900">{formatCurrency(pendingPayments.reduce((s, p) => s + p.amount, 0))}</p>
          <p className="text-xs text-gray-500 mt-1">{pendingPayments.length} payments</p>
        </div>
        <div className="bg-white rounded-xl border border-gray-200 p-5">
          <div className="flex items-center gap-2 mb-3">
            <CheckCircle size={18} className="text-green-600" />
            <span className="text-sm font-medium text-gray-500">Completed This Month</span>
          </div>
          <p className="text-2xl font-bold text-gray-900">{formatCurrency(completedPayments.reduce((s, p) => s + p.amount, 0))}</p>
          <p className="text-xs text-gray-500 mt-1">{completedPayments.length} payments</p>
        </div>
        <div className="bg-white rounded-xl border border-gray-200 p-5">
          <div className="flex items-center gap-2 mb-3">
            <CreditCard size={18} className="text-teal-600" />
            <span className="text-sm font-medium text-gray-500">Total Queue</span>
          </div>
          <p className="text-2xl font-bold text-gray-900">{formatCurrency(payments.reduce((s, p) => s + p.amount, 0))}</p>
          <p className="text-xs text-gray-500 mt-1">{payments.length} total records</p>
        </div>
      </div>

      <div className="flex gap-1 bg-gray-100 p-1 rounded-lg w-fit">
        {[
          { key: 'queue' as const, label: 'Payment Queue' },
          { key: 'history' as const, label: 'Payment History' },
          { key: 'batch' as const, label: 'Batch Processing' },
        ].map((tab) => (
          <button
            key={tab.key}
            onClick={() => setActiveSection(tab.key)}
            className={cn(
              'px-4 py-2 text-sm font-medium rounded-md transition-colors',
              activeSection === tab.key ? 'bg-white text-teal-700 shadow-sm' : 'text-gray-600 hover:text-gray-800'
            )}
          >
            {tab.label}
          </button>
        ))}
      </div>

      {activeSection === 'queue' && (
        <div className="space-y-3">
          {pendingPayments.map((payment) => (
            <div key={payment.id} className="bg-white rounded-xl border border-gray-200 p-5 hover:shadow-md transition-shadow">
              <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
                <div className="flex items-center gap-4">
                  <div className={cn(
                    'p-3 rounded-lg',
                    payment.status === 'pending' ? 'bg-amber-50' : 'bg-blue-50'
                  )}>
                    <Clock size={20} className={payment.status === 'pending' ? 'text-amber-600' : 'text-blue-600'} />
                  </div>
                  <div>
                    <h4 className="font-semibold text-gray-900">{payment.vendorName}</h4>
                    <div className="flex items-center gap-3 mt-1 text-xs text-gray-400">
                      <span>{payment.reference}</span>
                      <span>Due: {formatDate(payment.dueDate)}</span>
                      <span className={cn(
                        'px-1.5 py-0.5 rounded font-medium',
                        payment.method === 'wire' && 'bg-teal-50 text-teal-700',
                        payment.method === 'ach' && 'bg-blue-50 text-blue-700',
                        payment.method === 'check' && 'bg-amber-50 text-amber-700',
                        payment.method === 'card' && 'bg-purple-50 text-purple-700',
                      )}>
                        {payment.method.toUpperCase()}
                      </span>
                    </div>
                  </div>
                </div>
                <div className="flex items-center gap-3">
                  <p className="text-xl font-bold text-gray-900">{formatCurrency(payment.amount)}</p>
                  <button className="px-4 py-2 bg-teal-600 hover:bg-teal-700 text-white text-sm font-medium rounded-lg transition-colors flex items-center gap-1.5">
                    <ArrowRight size={14} />
                    Process
                  </button>
                </div>
              </div>
            </div>
          ))}
          {pendingPayments.length === 0 && (
            <div className="text-center py-12 text-gray-500">
              <CheckCircle size={32} className="mx-auto mb-2 text-green-400" />
              <p className="text-sm">No pending payments. All caught up!</p>
            </div>
          )}
        </div>
      )}

      {activeSection === 'history' && (
        <div className="bg-white rounded-xl border border-gray-200">
          <div className="overflow-x-auto">
            <table className="w-full">
              <thead>
                <tr className="text-xs text-gray-500 border-b border-gray-100">
                  <th className="text-left px-5 py-3 font-medium">Reference</th>
                  <th className="text-left px-5 py-3 font-medium">Vendor</th>
                  <th className="text-right px-5 py-3 font-medium">Amount</th>
                  <th className="text-left px-5 py-3 font-medium">Method</th>
                  <th className="text-left px-5 py-3 font-medium">Due Date</th>
                  <th className="text-left px-5 py-3 font-medium">Payment Date</th>
                  <th className="text-left px-5 py-3 font-medium">Status</th>
                </tr>
              </thead>
              <tbody>
                {payments.map((payment) => (
                  <tr key={payment.id} className="border-b border-gray-50 hover:bg-gray-50">
                    <td className="px-5 py-3 text-sm font-mono text-teal-600">{payment.reference}</td>
                    <td className="px-5 py-3 text-sm text-gray-800">{payment.vendorName}</td>
                    <td className="px-5 py-3 text-sm text-right font-semibold text-gray-900">{formatCurrency(payment.amount)}</td>
                    <td className="px-5 py-3">
                      <span className="text-xs font-medium uppercase text-gray-600">{payment.method}</span>
                    </td>
                    <td className="px-5 py-3 text-sm text-gray-600">{formatDate(payment.dueDate)}</td>
                    <td className="px-5 py-3 text-sm text-gray-600">{payment.paymentDate ? formatDate(payment.paymentDate) : '—'}</td>
                    <td className="px-5 py-3">
                      <span className={cn(
                        'inline-flex px-2 py-0.5 text-xs font-medium rounded-full',
                        payment.status === 'completed' && 'bg-green-100 text-green-700',
                        payment.status === 'pending' && 'bg-amber-100 text-amber-700',
                        payment.status === 'scheduled' && 'bg-blue-100 text-blue-700',
                        payment.status === 'processing' && 'bg-purple-100 text-purple-700',
                        payment.status === 'failed' && 'bg-red-100 text-red-700',
                      )}>
                        {payment.status}
                      </span>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      )}

      {activeSection === 'batch' && (
        <div className="space-y-6">
          <div className="bg-white rounded-xl border border-gray-200 p-6">
            <h3 className="font-semibold text-gray-800 mb-4">Batch Payment Summary</h3>
            <div className="space-y-4">
              {pendingPayments.map((p) => (
                <div key={p.id} className="flex items-center gap-3">
                  <input type="checkbox" defaultChecked className="rounded border-gray-300 text-teal-600 focus:ring-teal-500" />
                  <div className="flex-1">
                    <p className="text-sm font-medium text-gray-800">{p.vendorName}</p>
                    <p className="text-xs text-gray-500">{p.reference} — {p.method.toUpperCase()}</p>
                  </div>
                  <p className="text-sm font-semibold text-gray-900">{formatCurrency(p.amount)}</p>
                </div>
              ))}
            </div>
            <div className="mt-6 pt-4 border-t border-gray-100 flex items-center justify-between">
              <p className="text-sm text-gray-600">
                Total: <span className="font-bold text-gray-900">{formatCurrency(pendingPayments.reduce((s, p) => s + p.amount, 0))}</span>
              </p>
              <button className="px-6 py-2.5 bg-teal-600 hover:bg-teal-700 text-white text-sm font-medium rounded-lg transition-colors flex items-center gap-2">
                <Send size={16} />
                Process Batch ({pendingPayments.length})
              </button>
            </div>
          </div>

          <div className="bg-white rounded-xl border border-gray-200 p-6">
            <h3 className="font-semibold text-gray-800 mb-4">Payment Method Breakdown</h3>
            <div className="space-y-3">
              {methodBreakdown.map((item) => (
                <div key={item.method} className="flex items-center gap-4">
                  <div className={`w-3 h-3 rounded-full ${item.color}`} />
                  <span className="text-sm text-gray-700 w-32">{item.method}</span>
                  <div className="flex-1 bg-gray-100 rounded-full h-2">
                    <div
                      className={`${item.color} rounded-full h-2`}
                      style={{ width: `${payments.length > 0 ? (item.count / payments.length) * 100 : 0}%` }}
                    />
                  </div>
                  <span className="text-sm text-gray-600 w-20 text-right">{item.count} payments</span>
                  <span className="text-sm font-medium text-gray-900 w-28 text-right">{formatCurrency(item.amount)}</span>
                </div>
              ))}
            </div>
          </div>
        </div>
      )}
    </div>
  )
}
