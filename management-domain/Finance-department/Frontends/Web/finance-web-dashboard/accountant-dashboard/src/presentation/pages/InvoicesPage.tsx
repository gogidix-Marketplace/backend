import { useEffect, useState } from 'react'
import { useAccountantStore } from '@shared/store'
import { formatCurrency, formatDate, cn } from '@shared/utils/cn'
import { FileText, CheckCircle, XCircle, Eye, Loader2, Search, X } from 'lucide-react'

type TabKey = 'all' | 'pending' | 'approved' | 'paid' | 'rejected'

export default function InvoicesPage() {
  const { invoices, loading, loadInvoices } = useAccountantStore()
  const [activeTab, setActiveTab] = useState<TabKey>('all')
  const [searchTerm, setSearchTerm] = useState('')
  const [selectedInvoice, setSelectedInvoice] = useState<string | null>(null)

  useEffect(() => { loadInvoices() }, [])

  const tabs: { key: TabKey; label: string; count: number }[] = [
    { key: 'all', label: 'All', count: invoices.length },
    { key: 'pending', label: 'Pending', count: invoices.filter((i) => i.status === 'pending').length },
    { key: 'approved', label: 'Approved', count: invoices.filter((i) => i.status === 'approved').length },
    { key: 'paid', label: 'Paid', count: invoices.filter((i) => i.status === 'paid').length },
    { key: 'rejected', label: 'Rejected', count: invoices.filter((i) => i.status === 'rejected').length },
  ]

  const filtered = invoices.filter((inv) => {
    const matchTab = activeTab === 'all' || inv.status === activeTab
    const matchSearch = inv.vendorName.toLowerCase().includes(searchTerm.toLowerCase()) || inv.invoiceNumber.toLowerCase().includes(searchTerm.toLowerCase()) || inv.description.toLowerCase().includes(searchTerm.toLowerCase())
    return matchTab && matchSearch
  })

  const selected = invoices.find((i) => i.id === selectedInvoice)

  if (loading.invoices) {
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
          <h2 className="text-xl font-bold text-gray-900">Invoice Processing</h2>
          <p className="text-sm text-gray-500">Review, approve, and manage vendor invoices</p>
        </div>
      </div>

      <div className="flex gap-1 bg-gray-100 p-1 rounded-lg w-fit">
        {tabs.map((tab) => (
          <button
            key={tab.key}
            onClick={() => setActiveTab(tab.key)}
            className={cn(
              'px-4 py-2 text-sm font-medium rounded-md transition-colors',
              activeTab === tab.key ? 'bg-white text-teal-700 shadow-sm' : 'text-gray-600 hover:text-gray-800'
            )}
          >
            {tab.label}
            <span className="ml-1.5 text-xs text-gray-400">({tab.count})</span>
          </button>
        ))}
      </div>

      <div className="relative">
        <Search size={16} className="absolute left-3 top-1/2 -translate-y-1/2 text-gray-400" />
        <input
          type="text"
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)}
          placeholder="Search invoices by vendor, number, or description..."
          className="w-full pl-9 pr-4 py-2 border border-gray-300 rounded-lg text-sm focus:ring-2 focus:ring-teal-500 focus:border-teal-500 outline-none"
        />
      </div>

      <div className="grid gap-4">
        {filtered.map((invoice) => (
          <div key={invoice.id} className="bg-white rounded-xl border border-gray-200 p-5 hover:shadow-md transition-shadow">
            <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
              <div className="flex items-start gap-4">
                <div className="p-3 bg-teal-50 rounded-lg">
                  <FileText size={20} className="text-teal-600" />
                </div>
                <div>
                  <div className="flex items-center gap-2">
                    <h4 className="font-semibold text-gray-900">{invoice.vendorName}</h4>
                    <span className={cn(
                      'px-2 py-0.5 text-xs font-medium rounded-full',
                      invoice.status === 'pending' && 'bg-amber-100 text-amber-700',
                      invoice.status === 'approved' && 'bg-blue-100 text-blue-700',
                      invoice.status === 'paid' && 'bg-green-100 text-green-700',
                      invoice.status === 'rejected' && 'bg-red-100 text-red-700',
                    )}>
                      {invoice.status}
                    </span>
                  </div>
                  <p className="text-sm text-gray-500 mt-0.5">{invoice.description}</p>
                  <div className="flex items-center gap-4 mt-1 text-xs text-gray-400">
                    <span>Invoice #{invoice.invoiceNumber}</span>
                    <span>Received: {formatDate(invoice.receivedDate)}</span>
                    <span>Due: {formatDate(invoice.dueDate)}</span>
                    {invoice.department && <span>Dept: {invoice.department}</span>}
                  </div>
                </div>
              </div>
              <div className="flex items-center gap-3 sm:shrink-0">
                <p className="text-xl font-bold text-gray-900">{formatCurrency(invoice.amount)}</p>
                <div className="flex gap-1">
                  {invoice.status === 'pending' && (
                    <>
                      <button className="p-2 bg-green-50 hover:bg-green-100 text-green-600 rounded-lg transition-colors" title="Approve">
                        <CheckCircle size={18} />
                      </button>
                      <button className="p-2 bg-red-50 hover:bg-red-100 text-red-600 rounded-lg transition-colors" title="Reject">
                        <XCircle size={18} />
                      </button>
                    </>
                  )}
                  <button onClick={() => setSelectedInvoice(invoice.id)} className="p-2 bg-gray-50 hover:bg-gray-100 text-gray-600 rounded-lg transition-colors" title="View Details">
                    <Eye size={18} />
                  </button>
                </div>
              </div>
            </div>
          </div>
        ))}
      </div>

      {selected && (
        <div className="fixed inset-0 bg-black/50 z-50 flex items-center justify-center p-4" onClick={() => setSelectedInvoice(null)}>
          <div className="bg-white rounded-2xl shadow-2xl max-w-2xl w-full max-h-[80vh] overflow-y-auto" onClick={(e) => e.stopPropagation()}>
            <div className="p-6 border-b border-gray-100 flex items-center justify-between">
              <div>
                <h3 className="text-lg font-semibold text-gray-900">Invoice Details</h3>
                <p className="text-sm text-gray-500">{selected.invoiceNumber} — {selected.vendorName}</p>
              </div>
              <button onClick={() => setSelectedInvoice(null)} className="p-2 hover:bg-gray-100 rounded-lg">
                <X size={20} />
              </button>
            </div>
            <div className="p-6 space-y-4">
              <div className="grid grid-cols-2 gap-4">
                <div>
                  <p className="text-xs text-gray-500 uppercase tracking-wider">Vendor</p>
                  <p className="text-sm font-medium text-gray-900 mt-1">{selected.vendorName}</p>
                </div>
                <div>
                  <p className="text-xs text-gray-500 uppercase tracking-wider">Amount</p>
                  <p className="text-sm font-bold text-gray-900 mt-1">{formatCurrency(selected.amount)}</p>
                </div>
                <div>
                  <p className="text-xs text-gray-500 uppercase tracking-wider">Received Date</p>
                  <p className="text-sm text-gray-700 mt-1">{formatDate(selected.receivedDate)}</p>
                </div>
                <div>
                  <p className="text-xs text-gray-500 uppercase tracking-wider">Due Date</p>
                  <p className="text-sm text-gray-700 mt-1">{formatDate(selected.dueDate)}</p>
                </div>
                <div>
                  <p className="text-xs text-gray-500 uppercase tracking-wider">Status</p>
                  <p className="mt-1">
                    <span className={cn(
                      'px-2 py-0.5 text-xs font-medium rounded-full',
                      selected.status === 'pending' && 'bg-amber-100 text-amber-700',
                      selected.status === 'approved' && 'bg-blue-100 text-blue-700',
                      selected.status === 'paid' && 'bg-green-100 text-green-700',
                      selected.status === 'rejected' && 'bg-red-100 text-red-700',
                    )}>
                      {selected.status}
                    </span>
                  </p>
                </div>
                <div>
                  <p className="text-xs text-gray-500 uppercase tracking-wider">Department</p>
                  <p className="text-sm text-gray-700 mt-1">{selected.department || 'N/A'}</p>
                </div>
              </div>
              <div>
                <p className="text-xs text-gray-500 uppercase tracking-wider mb-2">Description</p>
                <p className="text-sm text-gray-700">{selected.description}</p>
              </div>
              <div>
                <p className="text-xs text-gray-500 uppercase tracking-wider mb-2">Line Items</p>
                <table className="w-full text-sm">
                  <thead>
                    <tr className="border-b border-gray-200">
                      <th className="text-left py-2 font-medium text-gray-500">Description</th>
                      <th className="text-right py-2 font-medium text-gray-500">Qty</th>
                      <th className="text-right py-2 font-medium text-gray-500">Unit Price</th>
                      <th className="text-right py-2 font-medium text-gray-500">Total</th>
                    </tr>
                  </thead>
                  <tbody>
                    {selected.lineItems.map((li, idx) => (
                      <tr key={idx} className="border-b border-gray-50">
                        <td className="py-2 text-gray-800">{li.description}</td>
                        <td className="py-2 text-right text-gray-600">{li.quantity}</td>
                        <td className="py-2 text-right text-gray-600">{formatCurrency(li.unitPrice)}</td>
                        <td className="py-2 text-right font-medium text-gray-900">{formatCurrency(li.total)}</td>
                      </tr>
                    ))}
                  </tbody>
                  <tfoot>
                    <tr className="border-t-2 border-gray-200">
                      <td colSpan={3} className="py-2 text-right font-semibold text-gray-900">Total</td>
                      <td className="py-2 text-right font-bold text-gray-900">{formatCurrency(selected.amount)}</td>
                    </tr>
                  </tfoot>
                </table>
              </div>
            </div>
            <div className="p-6 border-t border-gray-100 flex justify-end gap-3">
              {selected.status === 'pending' && (
                <>
                  <button className="px-4 py-2 bg-red-50 hover:bg-red-100 text-red-600 text-sm font-medium rounded-lg transition-colors">Reject</button>
                  <button className="px-4 py-2 bg-teal-600 hover:bg-teal-700 text-white text-sm font-medium rounded-lg transition-colors">Approve Invoice</button>
                </>
              )}
            </div>
          </div>
        </div>
      )}
    </div>
  )
}
