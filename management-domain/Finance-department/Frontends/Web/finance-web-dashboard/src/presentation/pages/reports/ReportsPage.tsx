// ============================================
// FINANCE DEPARTMENT - REPORTS PAGE
// ============================================

import { useState } from 'react'
import { formatDate } from '@shared/utils/cn'
import {
  FileText,
  Download,
  Eye,
  Trash2,
  Calendar,
  Filter,
  Search,
  Plus,
  BarChart3,
  TrendingUp,
  DollarSign,
  PieChart,
} from 'lucide-react'

type ReportType = 'profit_loss' | 'balance_sheet' | 'cash_flow' | 'budget_variance' | 'expense_summary' | 'custom'
type ReportStatus = 'generating' | 'ready' | 'failed'

interface Report {
  id: string
  name: string
  type: ReportType
  period: { start: string; end: string }
  generatedAt: string
  generatedBy: string
  status: ReportStatus
  fileUrl?: string
  fileSize?: number
}

const mockReports: Report[] = [
  {
    id: '1',
    name: 'Monthly Financial Report - February 2025',
    type: 'profit_loss',
    period: { start: '2025-02-01', end: '2025-02-28' },
    generatedAt: '2025-03-01T10:00:00Z',
    generatedBy: 'Amanda Peters',
    status: 'ready',
    fileUrl: '/reports/feb-2025-financial.pdf',
    fileSize: 2456789,
  },
  {
    id: '2',
    name: 'Q1 Budget Variance Report',
    type: 'budget_variance',
    period: { start: '2025-01-01', end: '2025-03-31' },
    generatedAt: '2025-03-10T14:30:00Z',
    generatedBy: 'Sarah Mitchell',
    status: 'ready',
    fileUrl: '/reports/q1-budget-variance.xlsx',
    fileSize: 1024000,
  },
  {
    id: '3',
    name: 'Cash Flow Statement - YTD',
    type: 'cash_flow',
    period: { start: '2025-01-01', end: '2025-03-11' },
    generatedAt: '2025-03-11T09:15:00Z',
    generatedBy: 'David Wilson',
    status: 'ready',
    fileUrl: '/reports/cash-flow-ytd.pdf',
    fileSize: 1843200,
  },
  {
    id: '4',
    name: 'Department Expense Summary',
    type: 'expense_summary',
    period: { start: '2025-03-01', end: '2025-03-11' },
    generatedAt: '2025-03-11T16:45:00Z',
    generatedBy: 'Amanda Peters',
    status: 'generating',
  },
]

const reportTypeConfig: Record<ReportType, { icon: React.ReactNode; label: string; color: string }> = {
  profit_loss: { icon: <TrendingUp size={20} />, label: 'Profit & Loss', color: 'emerald' },
  balance_sheet: { icon: <BarChart3 size={20} />, label: 'Balance Sheet', color: 'blue' },
  cash_flow: { icon: <DollarSign size={20} />, label: 'Cash Flow', color: 'purple' },
  budget_variance: { icon: <PieChart size={20} />, label: 'Budget Variance', color: 'amber' },
  expense_summary: { icon: <FileText size={20} />, label: 'Expense Summary', color: 'slate' },
  custom: { icon: <FileText size={20} />, label: 'Custom Report', color: 'slate' },
}

export function ReportsPage() {
  const [reports, setReports] = useState<Report[]>(mockReports)
  const [searchTerm, setSearchTerm] = useState('')
  const [typeFilter, setTypeFilter] = useState<ReportType | 'all'>('all')
  const [showNewReportModal, setShowNewReportModal] = useState(false)
  const [selectedReportType, setSelectedReportType] = useState<ReportType>('profit_loss')

  const filteredReports = reports.filter((report) => {
    const matchesSearch =
      report.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
      report.generatedBy.toLowerCase().includes(searchTerm.toLowerCase())
    const matchesType = typeFilter === 'all' || report.type === typeFilter
    return matchesSearch && matchesType
  })

  const formatFileSize = (bytes: number) => {
    if (bytes < 1024) return bytes + ' B'
    if (bytes < 1024 * 1024) return (bytes / 1024).toFixed(1) + ' KB'
    return (bytes / (1024 * 1024)).toFixed(1) + ' MB'
  }

  return (
    <div className="space-y-6">
      {/* Page Header */}
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-2xl font-bold text-slate-900">Reports</h1>
          <p className="text-slate-500">Generate and view financial reports</p>
        </div>
        <button
          onClick={() => setShowNewReportModal(true)}
          className="flex items-center gap-2 px-4 py-2 bg-blue-600 hover:bg-blue-700 text-white rounded-lg font-medium transition-colors"
        >
          <Plus size={20} />
          Generate Report
        </button>
      </div>

      {/* Quick Generate */}
      <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
        <button
          onClick={() => {
            setSelectedReportType('profit_loss')
            setShowNewReportModal(true)
          }}
          className="bg-white rounded-xl border border-slate-200 p-4 hover:border-blue-300 hover:shadow-md transition-all text-left"
        >
          <div className="flex items-center gap-3 mb-2">
            <div className="w-10 h-10 bg-emerald-100 rounded-lg flex items-center justify-center">
              <TrendingUp size={20} className="text-emerald-600" />
            </div>
            <span className="font-medium text-slate-900">Profit & Loss</span>
          </div>
          <p className="text-sm text-slate-500">Income and expense statement</p>
        </button>

        <button
          onClick={() => {
            setSelectedReportType('cash_flow')
            setShowNewReportModal(true)
          }}
          className="bg-white rounded-xl border border-slate-200 p-4 hover:border-blue-300 hover:shadow-md transition-all text-left"
        >
          <div className="flex items-center gap-3 mb-2">
            <div className="w-10 h-10 bg-purple-100 rounded-lg flex items-center justify-center">
              <DollarSign size={20} className="text-purple-600" />
            </div>
            <span className="font-medium text-slate-900">Cash Flow</span>
          </div>
          <p className="text-sm text-slate-500">Cash movement statement</p>
        </button>

        <button
          onClick={() => {
            setSelectedReportType('budget_variance')
            setShowNewReportModal(true)
          }}
          className="bg-white rounded-xl border border-slate-200 p-4 hover:border-blue-300 hover:shadow-md transition-all text-left"
        >
          <div className="flex items-center gap-3 mb-2">
            <div className="w-10 h-10 bg-amber-100 rounded-lg flex items-center justify-center">
              <PieChart size={20} className="text-amber-600" />
            </div>
            <span className="font-medium text-slate-900">Budget Variance</span>
          </div>
          <p className="text-sm text-slate-500">Budget vs actual analysis</p>
        </button>

        <button
          onClick={() => {
            setSelectedReportType('expense_summary')
            setShowNewReportModal(true)
          }}
          className="bg-white rounded-xl border border-slate-200 p-4 hover:border-blue-300 hover:shadow-md transition-all text-left"
        >
          <div className="flex items-center gap-3 mb-2">
            <div className="w-10 h-10 bg-slate-100 rounded-lg flex items-center justify-center">
              <FileText size={20} className="text-slate-600" />
            </div>
            <span className="font-medium text-slate-900">Expense Summary</span>
          </div>
          <p className="text-sm text-slate-500">Department expense breakdown</p>
        </button>
      </div>

      {/* Filters */}
      <div className="bg-white rounded-xl border border-slate-200 p-4">
        <div className="flex flex-col md:flex-row gap-4">
          <div className="flex-1 relative">
            <Search size={20} className="absolute left-3 top-1/2 -translate-y-1/2 text-slate-400" />
            <input
              type="text"
              placeholder="Search reports by name or creator..."
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              className="w-full pl-10 pr-4 py-2 border border-slate-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
            />
          </div>
          <div className="flex gap-2">
            <select
              value={typeFilter}
              onChange={(e) => setTypeFilter(e.target.value as ReportType | 'all')}
              className="px-4 py-2 border border-slate-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
            >
              <option value="all">All Types</option>
              <option value="profit_loss">Profit & Loss</option>
              <option value="balance_sheet">Balance Sheet</option>
              <option value="cash_flow">Cash Flow</option>
              <option value="budget_variance">Budget Variance</option>
              <option value="expense_summary">Expense Summary</option>
            </select>
          </div>
        </div>
      </div>

      {/* Reports List */}
      <div className="bg-white rounded-xl border border-slate-200 overflow-hidden">
        <div className="overflow-x-auto">
          <table className="w-full">
            <thead className="bg-slate-50 border-b border-slate-200">
              <tr>
                <th className="px-6 py-3 text-left text-xs font-semibold text-slate-600 uppercase">Report</th>
                <th className="px-6 py-3 text-left text-xs font-semibold text-slate-600 uppercase">Type</th>
                <th className="px-6 py-3 text-left text-xs font-semibold text-slate-600 uppercase">Period</th>
                <th className="px-6 py-3 text-left text-xs font-semibold text-slate-600 uppercase">Created</th>
                <th className="px-6 py-3 text-left text-xs font-semibold text-slate-600 uppercase">By</th>
                <th className="px-6 py-3 text-right text-xs font-semibold text-slate-600 uppercase">Size</th>
                <th className="px-6 py-3 text-center text-xs font-semibold text-slate-600 uppercase">Status</th>
                <th className="px-6 py-3 text-center text-xs font-semibold text-slate-600 uppercase">Actions</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-slate-200">
              {filteredReports.map((report) => {
                const typeInfo = reportTypeConfig[report.type]

                return (
                  <tr key={report.id} className="hover:bg-slate-50">
                    <td className="px-6 py-4">
                      <div className="flex items-center gap-3">
                        <div className={cn(
                          'p-2 rounded-lg',
                          report.type === 'profit_loss' && 'bg-emerald-100',
                          report.type === 'balance_sheet' && 'bg-blue-100',
                          report.type === 'cash_flow' && 'bg-purple-100',
                          report.type === 'budget_variance' && 'bg-amber-100',
                          report.type === 'expense_summary' && 'bg-slate-100'
                        )}>
                          {typeInfo.icon}
                        </div>
                        <span className="font-medium text-slate-900">{report.name}</span>
                      </div>
                    </td>
                    <td className="px-6 py-4">
                      <span className="px-2 py-1 bg-slate-100 text-slate-700 rounded text-sm">
                        {typeInfo.label}
                      </span>
                    </td>
                    <td className="px-6 py-4 text-slate-600">
                      <div className="flex items-center gap-2">
                        <Calendar size={16} className="text-slate-400" />
                        {formatDate(report.period.start)} - {formatDate(report.period.end)}
                      </div>
                    </td>
                    <td className="px-6 py-4 text-slate-600">
                      {formatDate(report.generatedAt)}
                    </td>
                    <td className="px-6 py-4 text-slate-600">{report.generatedBy}</td>
                    <td className="px-6 py-4 text-right text-slate-600">
                      {report.fileSize ? formatFileSize(report.fileSize) : '-'}
                    </td>
                    <td className="px-6 py-4">
                      {report.status === 'generating' ? (
                        <span className="inline-flex items-center gap-1 px-2 py-1 bg-blue-100 text-blue-700 rounded-full text-xs font-medium">
                          <div className="w-3 h-3 border-2 border-blue-600 border-t-transparent rounded-full animate-spin" />
                          Generating
                        </span>
                      ) : report.status === 'ready' ? (
                        <span className="inline-flex items-center gap-1 px-2 py-1 bg-emerald-100 text-emerald-700 rounded-full text-xs font-medium">
                          Ready
                        </span>
                      ) : (
                        <span className="inline-flex items-center gap-1 px-2 py-1 bg-red-100 text-red-700 rounded-full text-xs font-medium">
                          Failed
                        </span>
                      )}
                    </td>
                    <td className="px-6 py-4">
                      <div className="flex items-center justify-center gap-2">
                        {report.status === 'ready' && (
                          <>
                            <button className="p-1.5 hover:bg-slate-100 rounded-lg text-slate-500 hover:text-slate-700">
                              <Eye size={18} />
                            </button>
                            <button className="p-1.5 hover:bg-blue-50 rounded-lg text-blue-600">
                              <Download size={18} />
                            </button>
                          </>
                        )}
                        <button className="p-1.5 hover:bg-red-50 rounded-lg text-slate-500 hover:text-red-600">
                          <Trash2 size={18} />
                        </button>
                      </div>
                    </td>
                  </tr>
                )
              })}
            </tbody>
          </table>
        </div>

        {filteredReports.length === 0 && (
          <div className="text-center py-12">
            <p className="text-slate-500">No reports found matching your criteria.</p>
          </div>
        )}
      </div>

      {/* New Report Modal */}
      {showNewReportModal && (
        <div className="fixed inset-0 bg-black/50 flex items-center justify-center z-50">
          <div className="bg-white rounded-xl p-6 max-w-md w-full mx-4">
            <h3 className="text-lg font-semibold text-slate-900 mb-4">Generate New Report</h3>
            <div className="space-y-4">
              <div>
                <label className="block text-sm font-medium text-slate-700 mb-1">Report Type</label>
                <select
                  value={selectedReportType}
                  onChange={(e) => setSelectedReportType(e.target.value as ReportType)}
                  className="w-full px-3 py-2 border border-slate-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                >
                  {Object.entries(reportTypeConfig).map(([value, config]) => (
                    <option key={value} value={value}>{config.label}</option>
                  ))}
                </select>
              </div>
              <div>
                <label className="block text-sm font-medium text-slate-700 mb-1">Period</label>
                <select className="w-full px-3 py-2 border border-slate-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500">
                  <option>This Month</option>
                  <option>Last Month</option>
                  <option>This Quarter</option>
                  <option>Last Quarter</option>
                  <option>This Year</option>
                  <option>Custom Range</option>
                </select>
              </div>
              <div>
                <label className="block text-sm font-medium text-slate-700 mb-1">Format</label>
                <select className="w-full px-3 py-2 border border-slate-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500">
                  <option>PDF</option>
                  <option>Excel (XLSX)</option>
                  <option>CSV</option>
                </select>
              </div>
            </div>
            <div className="flex justify-end gap-3 mt-6">
              <button
                onClick={() => setShowNewReportModal(false)}
                className="px-4 py-2 border border-slate-300 rounded-lg hover:bg-slate-50 font-medium"
              >
                Cancel
              </button>
              <button
                onClick={() => setShowNewReportModal(false)}
                className="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 font-medium"
              >
                Generate
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  )
}
