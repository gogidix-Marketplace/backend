import { useState } from 'react'
import {
  Download,
  Share2,
  RotateCcw,
  Search,
  Filter,
  FileText,
} from 'lucide-react'
import { useReportsStore } from '@shared/store/reportsStore'
import { cn } from '@shared/utils/cn'

export default function HistoryPage() {
  const { reportsHistory } = useReportsStore()
  const [search, setSearch] = useState('')
  const [statusFilter, setStatusFilter] = useState<string>('All')
  const [formatFilter, setFormatFilter] = useState<string>('All')

  const filtered = reportsHistory.filter((report) => {
    const matchesSearch =
      report.name.toLowerCase().includes(search.toLowerCase()) ||
      report.type.toLowerCase().includes(search.toLowerCase())
    const matchesStatus = statusFilter === 'All' || report.status === statusFilter
    const matchesFormat = formatFilter === 'All' || report.format === formatFilter
    return matchesSearch && matchesStatus && matchesFormat
  })

  return (
    <div className="space-y-6">
      <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
        <div>
          <h1 className="text-2xl font-bold text-gray-900">Report History</h1>
          <p className="text-gray-500 mt-1">All generated reports with download and sharing options</p>
        </div>
      </div>

      <div className="flex flex-col sm:flex-row gap-3">
        <div className="relative flex-1">
          <Search size={18} className="absolute left-3 top-1/2 -translate-y-1/2 text-gray-400" />
          <input
            type="text"
            value={search}
            onChange={(e) => setSearch(e.target.value)}
            placeholder="Search reports..."
            className="w-full pl-10 pr-4 py-2.5 border border-gray-300 rounded-lg focus:ring-2 focus:ring-amber-500 focus:border-amber-500 outline-none"
          />
        </div>
        <div className="flex items-center gap-2">
          <Filter size={16} className="text-gray-400" />
          <select
            value={statusFilter}
            onChange={(e) => setStatusFilter(e.target.value)}
            className="px-3 py-2.5 border border-gray-300 rounded-lg text-sm focus:ring-2 focus:ring-amber-500 focus:border-amber-500 outline-none"
          >
            <option value="All">All Status</option>
            <option value="Completed">Completed</option>
            <option value="Processing">Processing</option>
            <option value="Failed">Failed</option>
          </select>
          <select
            value={formatFilter}
            onChange={(e) => setFormatFilter(e.target.value)}
            className="px-3 py-2.5 border border-gray-300 rounded-lg text-sm focus:ring-2 focus:ring-amber-500 focus:border-amber-500 outline-none"
          >
            <option value="All">All Formats</option>
            <option value="PDF">PDF</option>
            <option value="Excel">Excel</option>
            <option value="CSV">CSV</option>
          </select>
        </div>
      </div>

      <div className="bg-white rounded-xl border border-gray-200 overflow-hidden">
        <div className="overflow-x-auto">
          <table className="w-full">
            <thead>
              <tr className="bg-gray-50 border-b border-gray-200">
                <th className="text-left text-xs font-medium text-gray-500 uppercase px-5 py-3">Name</th>
                <th className="text-left text-xs font-medium text-gray-500 uppercase px-5 py-3">Type</th>
                <th className="text-left text-xs font-medium text-gray-500 uppercase px-5 py-3">Period</th>
                <th className="text-left text-xs font-medium text-gray-500 uppercase px-5 py-3">Generated</th>
                <th className="text-left text-xs font-medium text-gray-500 uppercase px-5 py-3">Format</th>
                <th className="text-left text-xs font-medium text-gray-500 uppercase px-5 py-3">Size</th>
                <th className="text-left text-xs font-medium text-gray-500 uppercase px-5 py-3">Status</th>
                <th className="text-right text-xs font-medium text-gray-500 uppercase px-5 py-3">Actions</th>
              </tr>
            </thead>
            <tbody>
              {filtered.map((report) => (
                <tr key={report.id} className="border-b border-gray-100 hover:bg-gray-50">
                  <td className="px-5 py-3.5">
                    <div className="flex items-center gap-2">
                      <FileText size={16} className="text-amber-500 shrink-0" />
                      <span className="text-sm font-medium text-gray-900">{report.name}</span>
                    </div>
                  </td>
                  <td className="px-5 py-3.5 text-sm text-gray-600">{report.type}</td>
                  <td className="px-5 py-3.5 text-sm text-gray-500">{report.period}</td>
                  <td className="px-5 py-3.5 text-sm text-gray-500">
                    {new Date(report.generatedDate).toLocaleDateString()}
                  </td>
                  <td className="px-5 py-3.5">
                    <span className="inline-flex items-center px-2 py-0.5 rounded text-xs font-medium bg-gray-100 text-gray-700">
                      {report.format}
                    </span>
                  </td>
                  <td className="px-5 py-3.5 text-sm text-gray-500">{report.size}</td>
                  <td className="px-5 py-3.5">
                    <span
                      className={cn(
                        'inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium',
                        report.status === 'Completed'
                          ? 'bg-green-50 text-green-700'
                          : report.status === 'Processing'
                          ? 'bg-amber-50 text-amber-700'
                          : 'bg-red-50 text-red-700'
                      )}
                    >
                      {report.status}
                    </span>
                  </td>
                  <td className="px-5 py-3.5">
                    <div className="flex items-center justify-end gap-1">
                      <button
                        className="p-1.5 text-gray-400 hover:text-amber-600 hover:bg-amber-50 rounded-lg transition-colors"
                        title="Download"
                      >
                        <Download size={16} />
                      </button>
                      <button
                        className="p-1.5 text-gray-400 hover:text-blue-600 hover:bg-blue-50 rounded-lg transition-colors"
                        title="Share"
                      >
                        <Share2 size={16} />
                      </button>
                      <button
                        className="p-1.5 text-gray-400 hover:text-green-600 hover:bg-green-50 rounded-lg transition-colors"
                        title="Regenerate"
                      >
                        <RotateCcw size={16} />
                      </button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
        {filtered.length === 0 && (
          <div className="py-12 text-center text-gray-500">
            <FileText size={40} className="mx-auto text-gray-300 mb-3" />
            <p className="font-medium">No reports found</p>
            <p className="text-sm mt-1">Try adjusting your search or filters</p>
          </div>
        )}
      </div>
    </div>
  )
}
