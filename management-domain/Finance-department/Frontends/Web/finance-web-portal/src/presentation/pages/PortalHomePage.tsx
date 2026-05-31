import { useNavigate } from 'react-router-dom'
import { useAuthStore, usePortalStore } from '@shared/store/portalStore'
import { formatCurrency } from '@shared/utils/cn'
import {
  PlusCircle,
  Receipt,
  FileText,
  CreditCard,
  TrendingUp,
  Clock,
  CheckCircle,
  ArrowRight,
  Megaphone,
} from 'lucide-react'

export function PortalHomePage() {
  const navigate = useNavigate()
  const { employee } = useAuthStore()
  const { myExpenses } = usePortalStore()

  const totalExpenses = myExpenses.reduce((sum, e) => sum + e.amount, 0)
  const pendingCount = myExpenses.filter(e => e.status === 'pending').length
  const paidTotal = myExpenses.filter(e => e.status === 'paid').reduce((sum, e) => sum + e.amount, 0)

  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-2xl font-bold text-slate-900">Welcome, {employee?.firstName || 'Employee'}</h1>
        <p className="text-slate-500">Here's your financial summary</p>
      </div>

      <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
        {[
          { label: 'Submit Expense', icon: PlusCircle, color: 'bg-sky-100 text-sky-600', action: '/submit' },
          { label: 'View Expenses', icon: Receipt, color: 'bg-emerald-100 text-emerald-600', action: '/my-expenses' },
          { label: 'My Invoices', icon: FileText, color: 'bg-purple-100 text-purple-600', action: '/my-invoices' },
          { label: 'Payments', icon: CreditCard, color: 'bg-amber-100 text-amber-600', action: '/payment-status' },
        ].map(card => {
          const Icon = card.icon
          return (
            <button
              key={card.label}
              onClick={() => navigate(card.action)}
              className="bg-white rounded-xl border border-slate-200 p-5 hover:shadow-md transition-shadow text-left"
            >
              <div className={`w-10 h-10 rounded-lg flex items-center justify-center mb-3 ${card.color}`}>
                <Icon size={20} />
              </div>
              <p className="font-medium text-slate-900 text-sm">{card.label}</p>
            </button>
          )
        })}
      </div>

      <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
        <div className="bg-white rounded-xl border border-slate-200 p-5">
          <div className="flex items-center gap-3 mb-3">
            <div className="w-8 h-8 bg-blue-100 rounded-lg flex items-center justify-center">
              <TrendingUp size={16} className="text-blue-600" />
            </div>
            <span className="text-sm text-slate-500">Expenses This Month</span>
          </div>
          <p className="text-2xl font-bold text-slate-900">{formatCurrency(totalExpenses, employee?.country === 'GB' ? 'GBP' : 'USD')}</p>
          <p className="text-xs text-slate-500 mt-1">{myExpenses.length} submissions</p>
        </div>
        <div className="bg-white rounded-xl border border-slate-200 p-5">
          <div className="flex items-center gap-3 mb-3">
            <div className="w-8 h-8 bg-amber-100 rounded-lg flex items-center justify-center">
              <Clock size={16} className="text-amber-600" />
            </div>
            <span className="text-sm text-slate-500">Pending Approval</span>
          </div>
          <p className="text-2xl font-bold text-amber-600">{pendingCount}</p>
          <p className="text-xs text-slate-500 mt-1">Awaiting review</p>
        </div>
        <div className="bg-white rounded-xl border border-slate-200 p-5">
          <div className="flex items-center gap-3 mb-3">
            <div className="w-8 h-8 bg-emerald-100 rounded-lg flex items-center justify-center">
              <CheckCircle size={16} className="text-emerald-600" />
            </div>
            <span className="text-sm text-slate-500">Reimbursed</span>
          </div>
          <p className="text-2xl font-bold text-emerald-600">{formatCurrency(paidTotal, employee?.country === 'GB' ? 'GBP' : 'USD')}</p>
          <p className="text-xs text-slate-500 mt-1">Total received</p>
        </div>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        <div className="bg-white rounded-xl border border-slate-200 p-6">
          <div className="flex items-center justify-between mb-4">
            <h3 className="font-semibold text-slate-900">Recent Activity</h3>
            <button onClick={() => navigate('/my-expenses')} className="text-sm text-sky-600 font-medium flex items-center gap-1">
              View All <ArrowRight size={14} />
            </button>
          </div>
          {myExpenses.length === 0 ? (
            <p className="text-sm text-slate-500 py-8 text-center">No recent activity</p>
          ) : (
            <div className="space-y-3">
              {myExpenses.slice(0, 5).map(expense => (
                <div key={expense.id} className="flex items-center justify-between p-3 rounded-lg border border-slate-100">
                  <div className="flex items-center gap-3">
                    <Receipt size={16} className="text-slate-400" />
                    <div>
                      <p className="text-sm font-medium text-slate-900">{expense.title}</p>
                      <p className="text-xs text-slate-500">{expense.date}</p>
                    </div>
                  </div>
                  <div className="text-right">
                    <p className="text-sm font-semibold">{formatCurrency(expense.amount, expense.currency)}</p>
                    <span className={`text-xs font-medium px-2 py-0.5 rounded-full ${
                      expense.status === 'pending' ? 'bg-amber-100 text-amber-700' :
                      expense.status === 'approved' ? 'bg-blue-100 text-blue-700' :
                      expense.status === 'paid' ? 'bg-emerald-100 text-emerald-700' :
                      'bg-slate-100 text-slate-700'
                    }`}>
                      {expense.status}
                    </span>
                  </div>
                </div>
              ))}
            </div>
          )}
        </div>

        <div className="bg-white rounded-xl border border-slate-200 p-6">
          <h3 className="font-semibold text-slate-900 mb-4 flex items-center gap-2">
            <Megaphone size={18} className="text-sky-500" />
            Announcements
          </h3>
          <div className="space-y-4">
            {[
              { title: 'Q1 Expense Report Deadline', desc: 'Submit all Q1 expenses by March 31st for timely processing.', date: 'Mar 15, 2026' },
              { title: 'New Travel Policy Update', desc: 'Updated per diem rates effective April 1st. Check the documents section for details.', date: 'Mar 10, 2026' },
            ].map((item, i) => (
              <div key={i} className="p-3 rounded-lg border border-slate-100">
                <p className="text-sm font-medium text-slate-900">{item.title}</p>
                <p className="text-xs text-slate-600 mt-1">{item.desc}</p>
                <p className="text-xs text-slate-400 mt-2">{item.date}</p>
              </div>
            ))}
          </div>
        </div>
      </div>
    </div>
  )
}
