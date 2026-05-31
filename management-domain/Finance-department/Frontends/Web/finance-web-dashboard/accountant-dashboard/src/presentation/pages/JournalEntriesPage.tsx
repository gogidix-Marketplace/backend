import { useEffect, useState } from 'react'
import { useAccountantStore } from '@shared/store'
import { formatCurrency, formatDate, cn } from '@shared/utils/cn'
import { Plus, Search, Filter, Download, BookOpen, Loader2 } from 'lucide-react'

export default function JournalEntriesPage() {
  const { journalEntries, loading, loadJournalEntries } = useAccountantStore()
  const [searchTerm, setSearchTerm] = useState('')
  const [statusFilter, setStatusFilter] = useState('all')

  useEffect(() => { loadJournalEntries() }, [])

  const filtered = journalEntries.filter((e) => {
    const matchSearch = e.description.toLowerCase().includes(searchTerm.toLowerCase()) || e.id.toLowerCase().includes(searchTerm.toLowerCase()) || e.account.toLowerCase().includes(searchTerm.toLowerCase())
    const matchStatus = statusFilter === 'all' || e.status === statusFilter
    return matchSearch && matchStatus
  })

  if (loading.journalEntries) {
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
          <h2 className="text-xl font-bold text-gray-900">Journal Entries</h2>
          <p className="text-sm text-gray-500">Manage and review accounting journal entries</p>
        </div>
        <button className="inline-flex items-center gap-2 px-4 py-2 bg-teal-600 hover:bg-teal-700 text-white text-sm font-medium rounded-lg transition-colors">
          <Plus size={16} />
          New Entry
        </button>
      </div>

      <div className="bg-white rounded-xl border border-gray-200">
        <div className="p-4 border-b border-gray-100 flex flex-col sm:flex-row gap-3">
          <div className="relative flex-1">
            <Search size={16} className="absolute left-3 top-1/2 -translate-y-1/2 text-gray-400" />
            <input
              type="text"
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              placeholder="Search entries by ID, description, or account..."
              className="w-full pl-9 pr-4 py-2 border border-gray-300 rounded-lg text-sm focus:ring-2 focus:ring-teal-500 focus:border-teal-500 outline-none"
            />
          </div>
          <select
            value={statusFilter}
            onChange={(e) => setStatusFilter(e.target.value)}
            className="px-3 py-2 border border-gray-300 rounded-lg text-sm focus:ring-2 focus:ring-teal-500 focus:border-teal-500 outline-none"
          >
            <option value="all">All Statuses</option>
            <option value="draft">Draft</option>
            <option value="posted">Posted</option>
            <option value="reviewed">Reviewed</option>
            <option value="approved">Approved</option>
          </select>
          <button className="inline-flex items-center gap-2 px-3 py-2 border border-gray-300 rounded-lg text-sm text-gray-600 hover:bg-gray-50 transition-colors">
            <Download size={16} />
            Export
          </button>
        </div>

        <div className="overflow-x-auto">
          <table className="w-full">
            <thead>
              <tr className="text-xs text-gray-500 border-b border-gray-100">
                <th className="text-left px-5 py-3 font-medium">Entry ID</th>
                <th className="text-left px-5 py-3 font-medium">Date</th>
                <th className="text-left px-5 py-3 font-medium">Description</th>
                <th className="text-left px-5 py-3 font-medium">Account</th>
                <th className="text-right px-5 py-3 font-medium">Debit</th>
                <th className="text-right px-5 py-3 font-medium">Credit</th>
                <th className="text-left px-5 py-3 font-medium">Reference</th>
                <th className="text-left px-5 py-3 font-medium">Status</th>
                <th className="text-left px-5 py-3 font-medium">Created By</th>
              </tr>
            </thead>
            <tbody>
              {filtered.map((entry) => (
                <tr key={entry.id} className="border-b border-gray-50 hover:bg-gray-50 transition-colors">
                  <td className="px-5 py-3 text-sm font-mono text-teal-600 font-medium">{entry.id}</td>
                  <td className="px-5 py-3 text-sm text-gray-600">{formatDate(entry.date)}</td>
                  <td className="px-5 py-3 text-sm text-gray-800 max-w-[200px] truncate">{entry.description}</td>
                  <td className="px-5 py-3 text-sm text-gray-600 font-mono text-xs">{entry.account}</td>
                  <td className="px-5 py-3 text-sm text-right font-medium text-gray-900">
                    {entry.debit > 0 ? formatCurrency(entry.debit) : '—'}
                  </td>
                  <td className="px-5 py-3 text-sm text-right font-medium text-gray-900">
                    {entry.credit > 0 ? formatCurrency(entry.credit) : '—'}
                  </td>
                  <td className="px-5 py-3 text-sm text-gray-500 font-mono">{entry.reference}</td>
                  <td className="px-5 py-3">
                    <span className={cn(
                      'inline-flex px-2 py-0.5 text-xs font-medium rounded-full',
                      entry.status === 'posted' && 'bg-green-100 text-green-700',
                      entry.status === 'approved' && 'bg-blue-100 text-blue-700',
                      entry.status === 'reviewed' && 'bg-purple-100 text-purple-700',
                      entry.status === 'draft' && 'bg-gray-100 text-gray-600',
                    )}>
                      {entry.status}
                    </span>
                  </td>
                  <td className="px-5 py-3 text-sm text-gray-600">{entry.createdBy}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>

        <div className="p-4 border-t border-gray-100 flex items-center justify-between">
          <p className="text-sm text-gray-500">Showing {filtered.length} of {journalEntries.length} entries</p>
          <div className="flex items-center gap-2 text-sm">
            <span className="font-medium text-gray-700">Total Debits:</span>
            <span className="text-teal-600 font-semibold">{formatCurrency(filtered.reduce((s, e) => s + e.debit, 0))}</span>
            <span className="mx-2 text-gray-300">|</span>
            <span className="font-medium text-gray-700">Total Credits:</span>
            <span className="text-teal-600 font-semibold">{formatCurrency(filtered.reduce((s, e) => s + e.credit, 0))}</span>
          </div>
        </div>
      </div>
    </div>
  )
}
