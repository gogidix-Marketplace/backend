import { useEffect, useState } from 'react'
import { useAccountantStore } from '@shared/store'
import { formatCurrency, formatDate, cn } from '@shared/utils/cn'
import { ArrowLeftRight, Check, X, Sparkles, Loader2, Link2, AlertTriangle } from 'lucide-react'

export default function ReconciliationPage() {
  const { reconciliation, loading, loadReconciliation } = useAccountantStore()
  const [selectedBank, setSelectedBank] = useState<string | null>(null)
  const [selectedBook, setSelectedBook] = useState<string | null>(null)

  useEffect(() => { loadReconciliation() }, [])

  const matchedCount = reconciliation.bank.filter((b) => b.matched).length
  const unmatchedBank = reconciliation.bank.filter((b) => !b.matched)
  const unmatchedBook = reconciliation.book.filter((b) => !b.matched)

  const handleMatch = () => {
    if (selectedBank && selectedBook) {
      setSelectedBank(null)
      setSelectedBook(null)
    }
  }

  if (loading.reconciliation) {
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
          <h2 className="text-xl font-bold text-gray-900">Bank Reconciliation</h2>
          <p className="text-sm text-gray-500">Match bank statement entries with book records</p>
        </div>
      </div>

      <div className="grid grid-cols-1 sm:grid-cols-3 gap-4">
        {[
          { label: 'Matched', value: matchedCount, icon: Check, color: 'bg-green-50 text-green-600 border-green-200' },
          { label: 'Unmatched', value: unmatchedBank.length + unmatchedBook.length, icon: X, color: 'bg-amber-50 text-amber-600 border-amber-200' },
          { label: 'Discrepancies', value: '0.00', icon: AlertTriangle, color: 'bg-blue-50 text-blue-600 border-blue-200' },
        ].map((card) => (
          <div key={card.label} className={cn('rounded-xl border p-5', card.color)}>
            <div className="flex items-center gap-2 mb-2">
              <card.icon size={18} />
              <span className="text-sm font-medium">{card.label}</span>
            </div>
            <p className="text-2xl font-bold">{card.value}</p>
          </div>
        ))}
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        <div className="bg-white rounded-xl border border-gray-200">
          <div className="p-4 border-b border-gray-100 flex items-center justify-between">
            <h3 className="font-semibold text-gray-800">Bank Statement Entries</h3>
            <span className="text-xs text-gray-500">{reconciliation.bank.length} entries</span>
          </div>
          <div className="divide-y divide-gray-50">
            {reconciliation.bank.map((item) => (
              <button
                key={item.id}
                onClick={() => setSelectedBank(selectedBank === item.id ? null : item.id)}
                className={cn(
                  'w-full text-left p-4 hover:bg-gray-50 transition-colors',
                  selectedBank === item.id && 'ring-2 ring-teal-500 ring-inset',
                  item.matched && 'bg-green-50/50'
                )}
              >
                <div className="flex items-center justify-between">
                  <div className="flex items-center gap-3">
                    {item.matched ? (
                      <Check size={16} className="text-green-500" />
                    ) : (
                      <div className="w-4 h-4 border-2 border-gray-300 rounded" />
                    )}
                    <div>
                      <p className="text-sm font-medium text-gray-800">{item.description}</p>
                      <p className="text-xs text-gray-500">{item.id} — {formatDate(item.date)}</p>
                    </div>
                  </div>
                  <div className="text-right">
                    <p className={cn('text-sm font-semibold', item.amount >= 0 ? 'text-green-600' : 'text-red-600')}>
                      {item.amount >= 0 ? '+' : ''}{formatCurrency(Math.abs(item.amount))}
                    </p>
                    {item.category && <p className="text-xs text-gray-400">{item.category}</p>}
                  </div>
                </div>
              </button>
            ))}
          </div>
        </div>

        <div className="bg-white rounded-xl border border-gray-200">
          <div className="p-4 border-b border-gray-100 flex items-center justify-between">
            <h3 className="font-semibold text-gray-800">Book Entries</h3>
            <span className="text-xs text-gray-500">{reconciliation.book.length} entries</span>
          </div>
          <div className="divide-y divide-gray-50">
            {reconciliation.book.map((item) => (
              <button
                key={item.id}
                onClick={() => setSelectedBook(selectedBook === item.id ? null : item.id)}
                className={cn(
                  'w-full text-left p-4 hover:bg-gray-50 transition-colors',
                  selectedBook === item.id && 'ring-2 ring-teal-500 ring-inset',
                  item.matched && 'bg-green-50/50'
                )}
              >
                <div className="flex items-center justify-between">
                  <div className="flex items-center gap-3">
                    {item.matched ? (
                      <Check size={16} className="text-green-500" />
                    ) : (
                      <div className="w-4 h-4 border-2 border-gray-300 rounded" />
                    )}
                    <div>
                      <p className="text-sm font-medium text-gray-800">{item.description}</p>
                      <p className="text-xs text-gray-500">{item.id} — {formatDate(item.date)}</p>
                    </div>
                  </div>
                  <p className={cn('text-sm font-semibold', item.amount >= 0 ? 'text-green-600' : 'text-red-600')}>
                    {item.amount >= 0 ? '+' : ''}{formatCurrency(Math.abs(item.amount))}
                  </p>
                </div>
              </button>
            ))}
          </div>
        </div>
      </div>

      {selectedBank && selectedBook && (
        <div className="fixed bottom-6 left-1/2 -translate-x-1/2 bg-white rounded-xl shadow-2xl border border-gray-200 p-4 flex items-center gap-4 z-50">
          <p className="text-sm text-gray-700">
            Match <span className="font-semibold text-teal-600">{selectedBank}</span> with <span className="font-semibold text-teal-600">{selectedBook}</span>?
          </p>
          <button onClick={handleMatch} className="px-4 py-2 bg-teal-600 hover:bg-teal-700 text-white text-sm font-medium rounded-lg transition-colors flex items-center gap-1.5">
            <Link2 size={14} />
            Match
          </button>
          <button onClick={() => { setSelectedBank(null); setSelectedBook(null) }} className="px-4 py-2 border border-gray-300 text-gray-600 text-sm rounded-lg hover:bg-gray-50 transition-colors">
            Cancel
          </button>
        </div>
      )}

      <div className="bg-white rounded-xl border border-gray-200 p-5">
        <div className="flex items-center gap-2 mb-4">
          <Sparkles size={18} className="text-teal-600" />
          <h3 className="font-semibold text-gray-800">AI Matching Suggestions</h3>
        </div>
        <div className="space-y-3">
          {[
            { bank: 'BNK-003', book: 'BK-003', confidence: 95, reason: 'Same amount ($8,750) and date — vendor payment to Acme Corp' },
            { bank: 'BNK-004', book: 'BK-006', confidence: 78, reason: 'Amount mismatch — Bank shows $12,350 vs Book shows $340. Verify deposit allocation.' },
          ].map((suggestion, idx) => (
            <div key={idx} className="flex items-center justify-between p-3 bg-gray-50 rounded-lg">
              <div>
                <p className="text-sm text-gray-800">
                  <span className="font-medium">{suggestion.bank}</span>
                  <ArrowLeftRight size={14} className="inline mx-2 text-gray-400" />
                  <span className="font-medium">{suggestion.book}</span>
                </p>
                <p className="text-xs text-gray-500 mt-0.5">{suggestion.reason}</p>
              </div>
              <div className="flex items-center gap-3">
                <span className={cn(
                  'text-xs font-semibold px-2 py-0.5 rounded-full',
                  suggestion.confidence >= 90 ? 'bg-green-100 text-green-700' : 'bg-amber-100 text-amber-700'
                )}>
                  {suggestion.confidence}% match
                </span>
                <button className="text-sm text-teal-600 hover:text-teal-700 font-medium">Apply</button>
              </div>
            </div>
          ))}
        </div>
      </div>
    </div>
  )
}
