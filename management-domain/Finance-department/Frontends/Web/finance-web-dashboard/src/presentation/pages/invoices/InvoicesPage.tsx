// ============================================
// FINANCE DEPARTMENT - INVOICES PAGE
// ============================================

import { useState } from 'react'
import { formatCurrency, formatDate, cn } from '@shared/utils/cn'
import {
  Plus,
  Search,
  Filter,
  Download,
  Eye,
  Send,
  CheckCircle,
  AlertCircle,
  FileText,
  Building,
  Calendar,
  Clock,
} from 'lucide-react'

type InvoiceStatus = 'draft' | 'sent' | 'viewed' | 'approved' | 'paid' | 'overdue' | 'cancelled'
type InvoiceType = 'accounts_receivable' | 'accounts_payable'

interface Invoice {
  id: string
  invoiceNumber: string
  type: InvoiceType
  entityName: string
  amount: number
  taxAmount: number
  totalAmount: number
  currency: string
  issueDate: string
  dueDate: string
  status: InvoiceStatus
  paidDate?: string
}

const mockInvoices: Invoice[] = [
  {
    id: '1',
    invoiceNumber: 'INV-2025-001',
    type: 'accounts_receivable',
    entityName: 'TechCorp Inc.',
    amount: 45000,
    taxAmount: 0,
    totalAmount: 45000,
    currency: 'USD',
    issueDate: '2025-02-15',
    dueDate: '2025-03-15',
    status: 'paid',
    paidDate: '2025-03-10',
  },
  {
    id: '2',
    invoiceNumber: 'INV-2025-002',
    type: 'accounts_receivable',
    entityName: 'Delta Corp',
    amount: 28500,
    taxAmount: 5700,
    totalAmount: 34200,
    currency: 'USD',
    issueDate: '2025-02-20',
    dueDate: '2025-03-20',
    status: 'sent',
  },
  {
    id: '3',
    invoiceNumber: 'INV-2025-003',
    type: 'accounts_receivable',
    entityName: 'Global Solutions Ltd',
    amount: 67500,
    taxAmount: 13500,
    totalAmount: 81000,
    currency: 'USD',
    issueDate: '2025-01-25',
    dueDate: '2025-02-25',
    status: 'overdue',
  },
  {
    id: '4',
    invoiceNumber: 'BILL-2025-001',
    type: 'accounts_payable',
    entityName: 'Amazon Web Services',
    amount: 3200,
    taxAmount: 0,
    totalAmount: 3200,
    currency: 'USD',
    issueDate: '2025-03-01',
    dueDate: '2025-03-15',
    status: 'approved',
  },
  {
    id: '5',
    invoiceNumber: 'BILL-2025-002',
    type: 'accounts_payable',
    entityName: 'Microsoft Corporation',
    amount: 8900,
    taxAmount: 1780,
    totalAmount: 10680,
    currency: 'USD',
    issueDate: '2025-03-05',
    dueDate: '2025-03-20',
    status: 'sent',
  },
  {
    id: '6',
    invoiceNumber: 'INV-2025-004',
    type: 'accounts_receivable',
    entityName: 'StartUp Ventures',
    amount: 15000,
    taxAmount: 3000,
    totalAmount: 18000,
    currency: 'USD',
    issueDate: '2025-03-08',
    dueDate: '2025-04-08',
    status: 'draft',
  },
]

const statusColors: Record<InvoiceStatus, string> = {
  draft: 'bg-slate-100 text-slate-700',
  sent: 'bg-blue-100 text-blue-700',
  viewed: 'bg-purple-100 text-purple-700',
  approved: 'bg-emerald-100 text-emerald-700',
  paid: 'bg-emerald-100 text-emerald-700',
  overdue: 'bg-red-100 text-red-700',
  cancelled: 'bg-slate-100 text-slate-500',
}

const statusIcons: Record<InvoiceStatus, React.ReactNode> = {
  draft: <FileText size={16} />,
  sent: <Send size={16} />,
  viewed: <Eye size={16} />,
  approved: <CheckCircle size={16} />,
  paid: <CheckCircle size={16} />,
  overdue: <AlertCircle size={16} />,
  cancelled: <Clock size={16} />,
}

export function InvoicesPage() {
  const [invoices, setInvoices] = useState<Invoice[]>(mockInvoices)
  const [searchTerm, setSearchTerm] = useState('')
  const [typeFilter, setTypeFilter] = useState<InvoiceType | 'all'>('all')
  const [statusFilter, setStatusFilter] = useState<InvoiceStatus | 'all'>('all')
  const [showFilters, setShowFilters] = useState(false)
  const [activeTab, setActiveTab] = useState<'receivable' | 'payable' | 'all'>('all')

  const filteredInvoices = invoices.filter((invoice) => {
    const matchesSearch =
      invoice.invoiceNumber.toLowerCase().includes(searchTerm.toLowerCase()) ||
      invoice.entityName.toLowerCase().includes(searchTerm.toLowerCase())
    const matchesType = activeTab === 'all' ||
      (activeTab === 'receivable' && invoice.type === 'accounts_receivable') ||
      (activeTab === 'payable' && invoice.type === 'accounts_payable')
    const matchesStatus = statusFilter === 'all' || invoice.status === statusFilter
    return matchesSearch && matchesType && matchesStatus
  })

  const totalReceivable = invoices
    .filter((i) => i.type === 'accounts_receivable' && i.status !== 'paid' && i.status !== 'cancelled')
    .reduce((sum, i) => sum + i.totalAmount, 0)

  const totalPayable = invoices
    .filter((i) => i.type === 'accounts_payable' && i.status !== 'paid' && i.status !== 'cancelled')
    .reduce((sum, i) => sum + i.totalAmount, 0)

  const overdueCount = invoices.filter((i) => i.status === 'overdue').length

  return (
    <div className="space-y-6">
      {/* Page Header */}
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-2xl font-bold text-slate-900">Invoices</h1>
          <p className="text-slate-500">Manage accounts receivable and payable</p>
        </div>
        <div className="flex gap-2">
          <button className="flex items-center gap-2 px-4 py-2 border border-slate-300 rounded-lg hover:bg-slate-50 font-medium transition-colors">
            <Plus size={20} />
            New Bill
          </button>
          <button className="flex items-center gap-2 px-4 py-2 bg-blue-600 hover:bg-blue-700 text-white rounded-lg font-medium transition-colors">
            <Plus size={20} />
            New Invoice
          </button>
        </div>
      </div>

      {/* Summary Cards */}
      <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
        <div className="bg-white rounded-xl border border-slate-200 p-4">
          <p className="text-sm text-slate-500">Accounts Receivable</p>
          <p className="text-xl font-bold text-emerald-600">
            {formatCurrency(totalReceivable, 'USD')}
          </p>
          <p className="text-xs text-slate-500 mt-1">Outstanding from customers</p>
        </div>
        <div className="bg-white rounded-xl border border-slate-200 p-4">
          <p className="text-sm text-slate-500">Accounts Payable</p>
          <p className="text-xl font-bold text-amber-600">
            {formatCurrency(totalPayable, 'USD')}
          </p>
          <p className="text-xs text-slate-500 mt-1">Outstanding to vendors</p>
        </div>
        <div className="bg-white rounded-xl border border-slate-200 p-4">
          <p className="text-sm text-slate-500">Overdue</p>
          <p className="text-xl font-bold text-red-600">{overdueCount}</p>
          <p className="text-xs text-slate-500 mt-1">Invoices past due date</p>
        </div>
        <div className="bg-white rounded-xl border border-slate-200 p-4">
          <p className="text-sm text-slate-500">This Month</p>
          <p className="text-xl font-bold text-slate-900">
            {formatCurrency(
              invoices.filter((i) => {
                const date = new Date(i.issueDate)
                const now = new Date()
                return date.getMonth() === now.getMonth() && date.getFullYear() === now.getFullYear()
              }).reduce((sum, i) => sum + i.totalAmount, 0),
              'USD'
            )}
          </p>
        </div>
      </div>

      {/* Tabs */}
      <div className="border-b border-slate-200">
        <div className="flex gap-8">
          <button
            onClick={() => setActiveTab('all')}
            className={cn(
              'py-3 px-1 border-b-2 font-medium text-sm transition-colors',
              activeTab === 'all'
                ? 'border-blue-600 text-blue-600'
                : 'border-transparent text-slate-500 hover:text-slate-700'
            )}
          >
            All Invoices
          </button>
          <button
            onClick={() => setActiveTab('receivable')}
            className={cn(
              'py-3 px-1 border-b-2 font-medium text-sm transition-colors',
              activeTab === 'receivable'
                ? 'border-blue-600 text-blue-600'
                : 'border-transparent text-slate-500 hover:text-slate-700'
            )}
          >
            Receivable
          </button>
          <button
            onClick={() => setActiveTab('payable')}
            className={cn(
              'py-3 px-1 border-b-2 font-medium text-sm transition-colors',
              activeTab === 'payable'
                ? 'border-blue-600 text-blue-600'
                : 'border-transparent text-slate-500 hover:text-slate-700'
            )}
          >
            Payable
          </button>
        </div>
      </div>

      {/* Filters */}
      <div className="bg-white rounded-xl border border-slate-200 p-4">
        <div className="flex flex-col md:flex-row gap-4">
          <div className="flex-1 relative">
            <Search size={20} className="absolute left-3 top-1/2 -translate-y-1/2 text-slate-400" />
            <input
              type="text"
              placeholder="Search invoices by number or entity..."
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
            <button className="flex items-center gap-2 px-4 py-2 border border-slate-300 rounded-lg hover:bg-slate-50 transition-colors">
              <Download size={20} />
              Export
            </button>
          </div>
        </div>

        {showFilters && (
          <div className="mt-4 pt-4 border-t border-slate-200 flex flex-wrap gap-4">
            <div>
              <label className="block text-sm font-medium text-slate-700 mb-1">Status</label>
              <select
                value={statusFilter}
                onChange={(e) => setStatusFilter(e.target.value as InvoiceStatus | 'all')}
                className="px-3 py-2 border border-slate-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
              >
                <option value="all">All Statuses</option>
                <option value="draft">Draft</option>
                <option value="sent">Sent</option>
                <option value="viewed">Viewed</option>
                <option value="approved">Approved</option>
                <option value="paid">Paid</option>
                <option value="overdue">Overdue</option>
                <option value="cancelled">Cancelled</option>
              </select>
            </div>
          </div>
        )}
      </div>

      {/* Invoices Table */}
      <div className="bg-white rounded-xl border border-slate-200 overflow-hidden">
        <div className="overflow-x-auto">
          <table className="w-full">
            <thead className="bg-slate-50 border-b border-slate-200">
              <tr>
                <th className="px-6 py-3 text-left text-xs font-semibold text-slate-600 uppercase">Invoice</th>
                <th className="px-6 py-3 text-left text-xs font-semibold text-slate-600 uppercase">Type</th>
                <th className="px-6 py-3 text-left text-xs font-semibold text-slate-600 uppercase">Entity</th>
                <th className="px-6 py-3 text-left text-xs font-semibold text-slate-600 uppercase">Issue Date</th>
                <th className="px-6 py-3 text-left text-xs font-semibold text-slate-600 uppercase">Due Date</th>
                <th className="px-6 py-3 text-right text-xs font-semibold text-slate-600 uppercase">Amount</th>
                <th className="px-6 py-3 text-center text-xs font-semibold text-slate-600 uppercase">Status</th>
                <th className="px-6 py-3 text-center text-xs font-semibold text-slate-600 uppercase">Actions</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-slate-200">
              {filteredInvoices.map((invoice) => {
                const isOverdue = new Date(invoice.dueDate) < new Date() && invoice.status !== 'paid' && invoice.status !== 'cancelled'

                return (
                  <tr key={invoice.id} className={cn('hover:bg-slate-50', isOverdue && 'bg-red-50/50')}>
                    <td className="px-6 py-4">
                      <div className="flex items-start gap-3">
                        <div className={cn(
                          'p-2 rounded-lg',
                          invoice.type === 'accounts_receivable' ? 'bg-emerald-100' : 'bg-amber-100'
                        )}>
                          <FileText size={18} className={cn(
                            invoice.type === 'accounts_receivable' ? 'text-emerald-600' : 'text-amber-600'
                          )} />
                        </div>
                        <div>
                          <p className="font-medium text-slate-900">{invoice.invoiceNumber}</p>
                          {isOverdue && (
                            <p className="text-xs text-red-600 font-medium">Overdue by {Math.ceil((Date.now() - new Date(invoice.dueDate).getTime()) / (1000 * 60 * 60 * 24))} days</p>
                          )}
                        </div>
                      </div>
                    </td>
                    <td className="px-6 py-4">
                      <span className={cn(
                        'px-2 py-1 rounded text-xs font-medium',
                        invoice.type === 'accounts_receivable'
                          ? 'bg-emerald-100 text-emerald-700'
                          : 'bg-amber-100 text-amber-700'
                      )}>
                        {invoice.type === 'accounts_receivable' ? 'Receivable' : 'Payable'}
                      </span>
                    </td>
                    <td className="px-6 py-4">
                      <div className="flex items-center gap-2">
                        <Building size={16} className="text-slate-400" />
                        <span className="text-slate-600">{invoice.entityName}</span>
                      </div>
                    </td>
                    <td className="px-6 py-4 text-slate-600">
                      <div className="flex items-center gap-2">
                        <Calendar size={16} className="text-slate-400" />
                        {formatDate(invoice.issueDate)}
                      </div>
                    </td>
                    <td className="px-6 py-4 text-slate-600">
                      <div className={cn('flex items-center gap-2', isOverdue && 'text-red-600 font-medium')}>
                        <Calendar size={16} className={isOverdue ? 'text-red-500' : 'text-slate-400'} />
                        {formatDate(invoice.dueDate)}
                      </div>
                    </td>
                    <td className="px-6 py-4 text-right">
                      <p className="font-medium text-slate-900">{formatCurrency(invoice.totalAmount, invoice.currency)}</p>
                      {invoice.taxAmount > 0 && (
                        <p className="text-xs text-slate-500">+{formatCurrency(invoice.taxAmount, invoice.currency)} tax</p>
                      )}
                    </td>
                    <td className="px-6 py-4">
                      <span className={cn('inline-flex items-center gap-1 px-2 py-1 rounded-full text-xs font-medium', statusColors[invoice.status])}>
                        {statusIcons[invoice.status]}
                        {invoice.status.charAt(0).toUpperCase() + invoice.status.slice(1)}
                      </span>
                    </td>
                    <td className="px-6 py-4">
                      <div className="flex items-center justify-center gap-2">
                        <button className="p-1.5 hover:bg-slate-100 rounded-lg text-slate-500 hover:text-slate-700">
                          <Eye size={18} />
                        </button>
                        {invoice.status === 'draft' && (
                          <button className="p-1.5 hover:bg-blue-50 rounded-lg text-blue-600">
                            <Send size={18} />
                          </button>
                        )}
                        {(invoice.status === 'sent' || invoice.status === 'approved') && invoice.type === 'accounts_payable' && (
                          <button className="p-1.5 hover:bg-emerald-50 rounded-lg text-emerald-600">
                            <CheckCircle size={18} />
                          </button>
                        )}
                      </div>
                    </td>
                  </tr>
                )
              })}
            </tbody>
          </table>
        </div>

        {filteredInvoices.length === 0 && (
          <div className="text-center py-12">
            <p className="text-slate-500">No invoices found matching your criteria.</p>
          </div>
        )}
      </div>
    </div>
  )
}
