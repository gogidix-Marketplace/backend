import { useState } from 'react'
import { FileText, Search, Eye, Download } from 'lucide-react'
import { formatCurrency, formatDate } from '@shared/utils/cn'

interface Invoice {
  id: string
  title: string
  amount: number
  currency: string
  date: string
  dueDate: string
  status: 'pending' | 'approved' | 'paid' | 'overdue'
  description: string
}

const MOCK_INVOICES: Invoice[] = [
  { id: 'INV-2026-001', title: 'March Travel Reimbursement', amount: 1250, currency: 'GBP', date: '2026-03-10', dueDate: '2026-04-10', status: 'pending', description: 'Flight and accommodation for client meeting in London' },
  { id: 'INV-2026-002', title: 'Software Subscription', amount: 49.99, currency: 'USD', date: '2026-03-08', dueDate: '2026-04-08', status: 'approved', description: 'Monthly design tool subscription' },
  { id: 'INV-2026-003', title: 'Team Lunch', amount: 85.50, currency: 'USD', date: '2026-03-05', dueDate: '2026-04-05', status: 'paid', description: 'Team lunch meeting' },
  { id: 'INV-2026-004', title: 'Office Supplies', amount: 234, currency: 'USD', date: '2026-03-01', dueDate: '2026-04-01', status: 'paid', description: 'Monitor stand and keyboard' },
  { id: 'INV-2026-005', title: 'Training Course', amount: 500, currency: 'USD', date: '2026-02-20', dueDate: '2026-03-20', status: 'overdue', description: 'AWS certification exam fee' },
  { id: 'INV-2026-006', title: 'Client Dinner', amount: 180, currency: 'GBP', date: '2026-02-15', dueDate: '2026-03-15', status: 'paid', description: 'Client entertainment dinner' },
]

const STATUS_CONFIG = {
  pending: { label: 'Pending', color: 'bg-amber-100 text-amber-700' },
  approved: { label: 'Approved', color: 'bg-blue-100 text-blue-700' },
  paid: { label: 'Paid', color: 'bg-emerald-100 text-emerald-700' },
  overdue: { label: 'Overdue', color: 'bg-red-100 text-red-700' },
}

export function InvoicesPage() {
  const [statusFilter, setStatusFilter] = useState('all')
  const [searchQuery, setSearchQuery] = useState('')

  const filtered = MOCK_INVOICES.filter(inv => {
    const matchesSearch = inv.title.toLowerCase().includes(searchQuery.toLowerCase())
    const matchesStatus = statusFilter === 'all' || inv.status === statusFilter
    return matchesSearch && matchesStatus
  })

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-2xl font-bold text-slate-900">My Invoices</h1>
          <p className="text-slate-500">View and track your expense invoices</p>
        </div>
      </div>

      <div className="flex items-center gap-4">
        <div className="relative flex-1 max-w-md">
          <Search size={16} className="absolute left-3 top-1/2 -translate-y-1/2 text-slate-400" />
          <input
            type="text"
            placeholder="Search invoices..."
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
            className="w-full pl-9 pr-4 py-2 border border-slate-200 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-sky-500"
          />
        </div>
        <select value={statusFilter} onChange={(e) => setStatusFilter(e.target.value)} className="px-3 py-2 border border-slate-200 rounded-lg text-sm">
          <option value="all">All Status</option>
          <option value="pending">Pending</option>
          <option value="approved">Approved</option>
          <option value="paid">Paid</option>
          <option value="overdue">Overdue</option>
        </select>
      </div>

      {filtered.length === 0 ? (
        <div className="bg-white rounded-xl border border-slate-200 p-12 text-center">
          <FileText size={48} className="mx-auto text-slate-300 mb-4" />
          <p className="text-slate-500">No invoices found</p>
        </div>
      ) : (
        <div className="space-y-3">
          {filtered.map(inv => {
            const statusCfg = STATUS_CONFIG[inv.status]
            return (
              <div key={inv.id} className="bg-white rounded-xl border border-slate-200 p-4 hover:border-slate-300 transition-colors">
                <div className="flex items-start justify-between">
                  <div className="flex items-start gap-4">
                    <div className="p-2 bg-slate-100 rounded-lg">
                      <FileText size={20} className="text-slate-600" />
                    </div>
                    <div>
                      <h3 className="font-semibold text-slate-900">{inv.title}</h3>
                      <p className="text-sm text-slate-600 mt-1">{inv.description}</p>
                      <div className="flex items-center gap-4 mt-2 text-xs text-slate-500">
                        <span>{inv.id}</span>
                        <span>Date: {formatDate(inv.date)}</span>
                        <span>Due: {formatDate(inv.dueDate)}</span>
                      </div>
                    </div>
                  </div>
                  <div className="text-right">
                    <p className="text-lg font-bold text-slate-900">{formatCurrency(inv.amount, inv.currency)}</p>
                    <span className={`inline-flex px-2 py-1 rounded-full text-xs font-medium mt-1 ${statusCfg.color}`}>
                      {statusCfg.label}
                    </span>
                  </div>
                </div>
                <div className="mt-3 pt-3 border-t border-slate-100 flex gap-3">
                  <button className="flex items-center gap-1 text-xs text-sky-600 hover:text-sky-700 font-medium">
                    <Eye size={14} /> View Details
                  </button>
                  {inv.status === 'paid' && (
                    <button className="flex items-center gap-1 text-xs text-slate-500 hover:text-slate-700 font-medium">
                      <Download size={14} /> Download Receipt
                    </button>
                  )}
                </div>
              </div>
            )
          })}
        </div>
      )}
    </div>
  )
}
