import { useEffect } from 'react'
import { useAccountantStore } from '@shared/store'
import { formatCurrency, formatDate } from '@shared/utils/cn'
import {
  BookOpen,
  ArrowLeftRight,
  FileText,
  CreditCard,
  Plus,
  CheckSquare,
  Send,
  Clock,
  AlertTriangle,
  TrendingUp,
} from 'lucide-react'
import { useNavigate } from 'react-router-dom'

export default function DashboardPage() {
  const { user, journalEntries, invoices, payments, reconciliation, loadJournalEntries, loadInvoices, loadPayments, loadReconciliation } = useAccountantStore()
  const navigate = useNavigate()

  useEffect(() => {
    loadJournalEntries()
    loadInvoices()
    loadPayments()
    loadReconciliation()
  }, [])

  const pendingInvoices = invoices.filter((i) => i.status === 'pending')
  const pendingPayments = payments.filter((p) => p.status === 'pending' || p.status === 'scheduled')
  const unmatchedBank = reconciliation.bank.filter((b) => !b.matched)
  const pendingTasks = [
    { id: 1, label: 'Review 3 pending invoices for approval', icon: FileText, color: 'text-amber-600', action: () => navigate('/invoices') },
    { id: 2, label: 'Complete December bank reconciliation', icon: ArrowLeftRight, color: 'text-blue-600', action: () => navigate('/reconciliation') },
    { id: 3, label: 'Process 4 scheduled payments', icon: CreditCard, color: 'text-teal-600', action: () => navigate('/payments') },
    { id: 4, label: 'Post 2 draft journal entries', icon: BookOpen, color: 'text-purple-600', action: () => navigate('/journal-entries') },
    { id: 5, label: 'Update vendor payment terms', icon: Clock, color: 'text-orange-600', action: () => navigate('/vendors') },
  ]

  return (
    <div className="space-y-6">
      <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
        <div>
          <h2 className="text-2xl font-bold text-gray-900">Welcome back, {user?.name?.split(' ')[0]}</h2>
          <p className="text-gray-500 text-sm mt-1">Here's your accounting overview for today</p>
        </div>
        <div className="flex gap-2">
          <button onClick={() => navigate('/journal-entries')} className="inline-flex items-center gap-2 px-4 py-2 bg-teal-600 hover:bg-teal-700 text-white text-sm font-medium rounded-lg transition-colors">
            <Plus size={16} />
            New Entry
          </button>
          <button onClick={() => navigate('/reconciliation')} className="inline-flex items-center gap-2 px-4 py-2 bg-white border border-gray-300 hover:bg-gray-50 text-gray-700 text-sm font-medium rounded-lg transition-colors">
            <ArrowLeftRight size={16} />
            Reconcile
          </button>
          <button onClick={() => navigate('/payments')} className="inline-flex items-center gap-2 px-4 py-2 bg-white border border-gray-300 hover:bg-gray-50 text-gray-700 text-sm font-medium rounded-lg transition-colors">
            <Send size={16} />
            Process Payments
          </button>
        </div>
      </div>

      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
        {[
          { label: 'Entries Today', value: journalEntries.length.toString(), icon: BookOpen, color: 'bg-teal-50 text-teal-600', change: '+3 from yesterday' },
          { label: 'Pending Reconciliation', value: unmatchedBank.length.toString(), icon: ArrowLeftRight, color: 'bg-amber-50 text-amber-600', change: '3 unmatched items' },
          { label: 'Outstanding Invoices', value: formatCurrency(pendingInvoices.reduce((sum, i) => sum + i.amount, 0)), icon: FileText, color: 'bg-blue-50 text-blue-600', change: `${pendingInvoices.length} invoices pending` },
          { label: 'Payments Due', value: formatCurrency(pendingPayments.reduce((sum, p) => sum + p.amount, 0)), icon: CreditCard, color: 'bg-purple-50 text-purple-600', change: `${pendingPayments.length} scheduled` },
        ].map((card) => (
          <div key={card.label} className="bg-white rounded-xl border border-gray-200 p-5 hover:shadow-md transition-shadow">
            <div className="flex items-center justify-between mb-3">
              <span className="text-sm font-medium text-gray-500">{card.label}</span>
              <div className={`p-2 rounded-lg ${card.color}`}>
                <card.icon size={18} />
              </div>
            </div>
            <p className="text-2xl font-bold text-gray-900">{card.value}</p>
            <p className="text-xs text-gray-500 mt-1">{card.change}</p>
          </div>
        ))}
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        <div className="bg-white rounded-xl border border-gray-200">
          <div className="flex items-center justify-between p-5 border-b border-gray-100">
            <h3 className="font-semibold text-gray-800">Recent Journal Entries</h3>
            <button onClick={() => navigate('/journal-entries')} className="text-sm text-teal-600 hover:text-teal-700 font-medium">View All</button>
          </div>
          <div className="overflow-x-auto">
            <table className="w-full">
              <thead>
                <tr className="text-xs text-gray-500 border-b border-gray-100">
                  <th className="text-left px-5 py-3 font-medium">ID</th>
                  <th className="text-left px-5 py-3 font-medium">Date</th>
                  <th className="text-left px-5 py-3 font-medium">Description</th>
                  <th className="text-right px-5 py-3 font-medium">Amount</th>
                  <th className="text-left px-5 py-3 font-medium">Status</th>
                </tr>
              </thead>
              <tbody>
                {journalEntries.slice(0, 5).map((entry) => (
                  <tr key={entry.id} className="border-b border-gray-50 hover:bg-gray-50">
                    <td className="px-5 py-3 text-sm font-mono text-teal-600">{entry.id}</td>
                    <td className="px-5 py-3 text-sm text-gray-600">{formatDate(entry.date)}</td>
                    <td className="px-5 py-3 text-sm text-gray-800">{entry.description}</td>
                    <td className="px-5 py-3 text-sm text-right font-medium text-gray-900">
                      {formatCurrency(Math.max(entry.debit, entry.credit))}
                    </td>
                    <td className="px-5 py-3">
                      <span className={`inline-flex px-2 py-0.5 text-xs font-medium rounded-full ${
                        entry.status === 'posted' ? 'bg-green-100 text-green-700' :
                        entry.status === 'approved' ? 'bg-blue-100 text-blue-700' :
                        entry.status === 'reviewed' ? 'bg-purple-100 text-purple-700' :
                        'bg-gray-100 text-gray-700'
                      }`}>
                        {entry.status}
                      </span>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>

        <div className="bg-white rounded-xl border border-gray-200">
          <div className="flex items-center justify-between p-5 border-b border-gray-100">
            <h3 className="font-semibold text-gray-800">Pending Tasks</h3>
            <span className="text-xs text-gray-500">{pendingTasks.length} tasks remaining</span>
          </div>
          <div className="p-2">
            {pendingTasks.map((task) => (
              <button
                key={task.id}
                onClick={task.action}
                className="w-full flex items-center gap-3 px-3 py-3 hover:bg-gray-50 rounded-lg transition-colors text-left"
              >
                <div className={`p-2 rounded-lg bg-gray-100 ${task.color}`}>
                  <task.icon size={16} />
                </div>
                <span className="text-sm text-gray-700 flex-1">{task.label}</span>
                <CheckSquare size={16} className="text-gray-300" />
              </button>
            ))}
          </div>
        </div>
      </div>

      <div className="bg-gradient-to-r from-teal-600 to-teal-700 rounded-xl p-6 text-white">
        <div className="flex items-center justify-between">
          <div>
            <h3 className="font-semibold text-lg">Month-End Close Progress</h3>
            <p className="text-teal-200 text-sm mt-1">December 2024 — 12 of 18 tasks completed</p>
          </div>
          <div className="text-right">
            <p className="text-3xl font-bold">67%</p>
            <p className="text-teal-200 text-sm">Complete</p>
          </div>
        </div>
        <div className="mt-4 bg-teal-800 rounded-full h-2">
          <div className="bg-white rounded-full h-2" style={{ width: '67%' }} />
        </div>
      </div>
    </div>
  )
}
