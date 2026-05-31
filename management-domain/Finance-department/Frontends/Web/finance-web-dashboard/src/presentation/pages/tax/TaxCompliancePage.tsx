import { useState } from 'react'
import {
  FileCheck,
  AlertTriangle,
  CheckCircle,
  XCircle,
  Clock,
  Download,
  Shield,
  Search,
  Filter,
  Calendar,
  MapPin,
} from 'lucide-react'
import { cn } from '@shared/utils/cn'
import { formatCurrency } from '@shared/utils/cn'

interface TaxByCountry {
  country: string
  flag: string
  taxAmount: number
  rate: number
  filingStatus: 'filed' | 'pending' | 'overdue'
  nextDue: string
}

interface ComplianceItem {
  id: string
  title: string
  country: string
  flag: string
  type: 'tax' | 'regulatory' | 'audit' | 'filing'
  status: 'compliant' | 'pending' | 'overdue' | 'at_risk'
  dueDate: string
  priority: 'high' | 'medium' | 'low'
}

interface AuditItem {
  id: string
  title: string
  status: 'scheduled' | 'in_progress' | 'completed'
  scheduledDate: string
  scope: string
  findings: number
}

const MOCK_TAX_BY_COUNTRY: TaxByCountry[] = [
  { country: 'Nigeria', flag: '🇳🇬', taxAmount: 1240000, rate: 30, filingStatus: 'filed', nextDue: '2026-03-31' },
  { country: 'Kenya', flag: '🇰🇪', taxAmount: 780000, rate: 30, filingStatus: 'pending', nextDue: '2026-03-31' },
  { country: 'South Africa', flag: '🇿🇦', taxAmount: 650000, rate: 28, filingStatus: 'pending', nextDue: '2026-04-30' },
  { country: 'Ghana', flag: '🇬🇭', taxAmount: 420000, rate: 25, filingStatus: 'filed', nextDue: '2026-04-30' },
  { country: 'Ireland', flag: '🇮🇪', taxAmount: 380000, rate: 12.5, filingStatus: 'filed', nextDue: '2026-09-23' },
  { country: 'United Kingdom', flag: '🇬🇧', taxAmount: 510000, rate: 25, filingStatus: 'filed', nextDue: '2026-12-31' },
]

const MOCK_COMPLIANCE_ITEMS: ComplianceItem[] = [
  { id: 'CMP-001', title: 'Nigeria VAT Return Q1 2026', country: 'Nigeria', flag: '🇳🇬', type: 'tax', status: 'pending', dueDate: '2026-03-31', priority: 'high' },
  { id: 'CMP-002', title: 'Kenya Corporate Tax Filing', country: 'Kenya', flag: '🇰🇪', type: 'tax', status: 'pending', dueDate: '2026-03-31', priority: 'high' },
  { id: 'CMP-003', title: 'SA Employment Equity Report', country: 'South Africa', flag: '🇿🇦', type: 'regulatory', status: 'at_risk', dueDate: '2026-02-28', priority: 'high' },
  { id: 'CMP-004', title: 'Ireland Annual Return', country: 'Ireland', flag: '🇮🇪', type: 'filing', status: 'compliant', dueDate: '2026-09-23', priority: 'low' },
  { id: 'CMP-005', title: 'GDPR Compliance Review', country: 'Ireland', flag: '🇮🇪', type: 'regulatory', status: 'compliant', dueDate: '2026-06-30', priority: 'medium' },
  { id: 'CMP-006', title: 'Ghana PAYE Filing', country: 'Ghana', flag: '🇬🇭', type: 'tax', status: 'pending', dueDate: '2026-04-15', priority: 'medium' },
  { id: 'CMP-007', title: 'UK Corporation Tax Return', country: 'United Kingdom', flag: '🇬🇧', type: 'tax', status: 'compliant', dueDate: '2026-12-31', priority: 'low' },
  { id: 'CMP-008', title: 'Annual External Audit', country: 'Global', flag: '🌍', type: 'audit', status: 'pending', dueDate: '2026-04-01', priority: 'high' },
]

const MOCK_AUDITS: AuditItem[] = [
  { id: 'AUD-001', title: 'Q1 2026 Internal Audit', status: 'scheduled', scheduledDate: '2026-03-15', scope: 'Nigeria & Kenya Operations', findings: 0 },
  { id: 'AUD-002', title: 'Annual External Audit 2025', status: 'completed', scheduledDate: '2026-01-20', scope: 'Global Operations', findings: 3 },
  { id: 'AUD-003', title: 'Tax Compliance Review', status: 'in_progress', scheduledDate: '2026-02-10', scope: 'All Countries', findings: 1 },
  { id: 'AUD-004', title: 'SOX Compliance Check', status: 'scheduled', scheduledDate: '2026-04-01', scope: 'Ireland & UK Entities', findings: 0 },
]

const STATUS_CONFIG = {
  compliant: { bg: 'bg-emerald-100', text: 'text-emerald-700', icon: CheckCircle, label: 'Compliant' },
  pending: { bg: 'bg-amber-100', text: 'text-amber-700', icon: Clock, label: 'Pending' },
  overdue: { bg: 'bg-red-100', text: 'text-red-700', icon: XCircle, label: 'Overdue' },
  at_risk: { bg: 'bg-red-100', text: 'text-red-700', icon: AlertTriangle, label: 'At Risk' },
}

const FILING_STATUS_CONFIG = {
  filed: { bg: 'bg-emerald-100', text: 'text-emerald-700', label: 'Filed' },
  pending: { bg: 'bg-amber-100', text: 'text-amber-700', label: 'Pending' },
  overdue: { bg: 'bg-red-100', text: 'text-red-700', label: 'Overdue' },
}

const AUDIT_STATUS_CONFIG = {
  scheduled: { bg: 'bg-blue-100', text: 'text-blue-700', label: 'Scheduled' },
  in_progress: { bg: 'bg-amber-100', text: 'text-amber-700', label: 'In Progress' },
  completed: { bg: 'bg-emerald-100', text: 'text-emerald-700', label: 'Completed' },
}

export function TaxCompliancePage() {
  const [selectedTab, setSelectedTab] = useState<'overview' | 'compliance' | 'audit' | 'filings'>('overview')
  const totalTax = MOCK_TAX_BY_COUNTRY.reduce((sum, t) => sum + t.taxAmount, 0)
  const compliantCount = MOCK_COMPLIANCE_ITEMS.filter(i => i.status === 'compliant').length
  const pendingCount = MOCK_COMPLIANCE_ITEMS.filter(i => i.status !== 'compliant').length

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-2xl font-bold text-slate-900">Tax & Compliance</h1>
          <p className="text-slate-500">Global tax summary, compliance status, and audit readiness</p>
        </div>
        <div className="flex items-center gap-3">
          <button className="flex items-center gap-2 px-3 py-2 text-sm border border-slate-200 rounded-lg hover:bg-slate-50">
            <Download size={16} />
            Export
          </button>
        </div>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
        <div className="bg-white rounded-xl border border-slate-200 p-5">
          <div className="flex items-center justify-between mb-2">
            <span className="text-sm text-slate-500">Total Tax Liability</span>
            <FileCheck size={18} className="text-blue-500" />
          </div>
          <p className="text-2xl font-bold text-slate-900">{formatCurrency(totalTax, 'USD')}</p>
          <p className="text-xs text-slate-500 mt-1">Across {MOCK_TAX_BY_COUNTRY.length} countries</p>
        </div>
        <div className="bg-white rounded-xl border border-slate-200 p-5">
          <div className="flex items-center justify-between mb-2">
            <span className="text-sm text-slate-500">Compliance Score</span>
            <Shield size={18} className="text-emerald-500" />
          </div>
          <p className="text-2xl font-bold text-emerald-600">{((compliantCount / MOCK_COMPLIANCE_ITEMS.length) * 100).toFixed(0)}%</p>
          <p className="text-xs text-slate-500 mt-1">{compliantCount} of {MOCK_COMPLIANCE_ITEMS.length} items compliant</p>
        </div>
        <div className="bg-white rounded-xl border border-slate-200 p-5">
          <div className="flex items-center justify-between mb-2">
            <span className="text-sm text-slate-500">Pending Items</span>
            <Clock size={18} className="text-amber-500" />
          </div>
          <p className="text-2xl font-bold text-amber-600">{pendingCount}</p>
          <p className="text-xs text-slate-500 mt-1">Require attention</p>
        </div>
        <div className="bg-white rounded-xl border border-slate-200 p-5">
          <div className="flex items-center justify-between mb-2">
            <span className="text-sm text-slate-500">Active Audits</span>
            <AlertTriangle size={18} className="text-blue-500" />
          </div>
          <p className="text-2xl font-bold text-slate-900">{MOCK_AUDITS.filter(a => a.status === 'in_progress' || a.status === 'scheduled').length}</p>
          <p className="text-xs text-slate-500 mt-1">{MOCK_AUDITS.filter(a => a.status === 'in_progress').length} in progress</p>
        </div>
      </div>

      <div className="flex gap-2 border-b border-slate-200 pb-0">
        {(['overview', 'compliance', 'audit', 'filings'] as const).map(tab => (
          <button
            key={tab}
            onClick={() => setSelectedTab(tab)}
            className={cn(
              'px-4 py-2 text-sm font-medium border-b-2 -mb-px transition-colors',
              selectedTab === tab
                ? 'border-blue-600 text-blue-600'
                : 'border-transparent text-slate-500 hover:text-slate-700'
            )}
          >
            {tab === 'overview' ? 'Tax by Country' : tab === 'compliance' ? 'Compliance Status' : tab === 'audit' ? 'Audit Readiness' : 'Regulatory Filings'}
          </button>
        ))}
      </div>

      {selectedTab === 'overview' && (
        <div className="bg-white rounded-xl border border-slate-200 overflow-hidden">
          <div className="overflow-x-auto">
            <table className="w-full">
              <thead>
                <tr className="border-b border-slate-200 bg-slate-50">
                  <th className="text-left px-4 py-3 text-sm font-medium text-slate-600">Country</th>
                  <th className="text-right px-4 py-3 text-sm font-medium text-slate-600">Tax Amount</th>
                  <th className="text-center px-4 py-3 text-sm font-medium text-slate-600">Tax Rate</th>
                  <th className="text-center px-4 py-3 text-sm font-medium text-slate-600">Filing Status</th>
                  <th className="text-right px-4 py-3 text-sm font-medium text-slate-600">Next Due</th>
                </tr>
              </thead>
              <tbody>
                {MOCK_TAX_BY_COUNTRY.map(row => {
                  const statusCfg = FILING_STATUS_CONFIG[row.filingStatus]
                  return (
                    <tr key={row.country} className="border-b border-slate-100 hover:bg-slate-50">
                      <td className="px-4 py-3">
                        <div className="flex items-center gap-2">
                          <span className="text-xl">{row.flag}</span>
                          <span className="font-medium text-slate-900">{row.country}</span>
                        </div>
                      </td>
                      <td className="px-4 py-3 text-right font-medium">{formatCurrency(row.taxAmount, 'USD')}</td>
                      <td className="px-4 py-3 text-center text-sm">{row.rate}%</td>
                      <td className="px-4 py-3 text-center">
                        <span className={cn('inline-flex px-2 py-1 rounded-full text-xs font-medium', statusCfg.bg, statusCfg.text)}>
                          {statusCfg.label}
                        </span>
                      </td>
                      <td className="px-4 py-3 text-right text-sm text-slate-600">{row.nextDue}</td>
                    </tr>
                  )
                })}
              </tbody>
            </table>
          </div>
        </div>
      )}

      {selectedTab === 'compliance' && (
        <div className="space-y-4">
          <div className="flex items-center gap-4">
            <div className="relative flex-1 max-w-md">
              <Search size={16} className="absolute left-3 top-1/2 -translate-y-1/2 text-slate-400" />
              <input
                type="text"
                placeholder="Search compliance items..."
                className="w-full pl-9 pr-4 py-2 border border-slate-200 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
              />
            </div>
            <select className="px-3 py-2 border border-slate-200 rounded-lg text-sm">
              <option value="all">All Status</option>
              <option value="compliant">Compliant</option>
              <option value="pending">Pending</option>
              <option value="at_risk">At Risk</option>
            </select>
            <select className="px-3 py-2 border border-slate-200 rounded-lg text-sm">
              <option value="all">All Types</option>
              <option value="tax">Tax</option>
              <option value="regulatory">Regulatory</option>
              <option value="audit">Audit</option>
              <option value="filing">Filing</option>
            </select>
          </div>

          <div className="space-y-3">
            {MOCK_COMPLIANCE_ITEMS.map(item => {
              const statusCfg = STATUS_CONFIG[item.status]
              const StatusIcon = statusCfg.icon
              return (
                <div key={item.id} className="bg-white rounded-xl border border-slate-200 p-4 hover:shadow-sm transition-shadow">
                  <div className="flex items-center justify-between">
                    <div className="flex items-center gap-3">
                      <span className="text-xl">{item.flag}</span>
                      <div>
                        <p className="font-medium text-slate-900">{item.title}</p>
                        <div className="flex items-center gap-2 mt-1">
                          <span className="text-xs text-slate-500">{item.id}</span>
                          <span className="text-xs text-slate-400">·</span>
                          <span className="text-xs text-slate-500 capitalize">{item.type}</span>
                          <span className="text-xs text-slate-400">·</span>
                          <span className="text-xs text-slate-500 flex items-center gap-1">
                            <Calendar size={10} />
                            {item.dueDate}
                          </span>
                        </div>
                      </div>
                    </div>
                    <div className="flex items-center gap-3">
                      <span className={cn('inline-flex items-center gap-1 px-2 py-1 rounded-full text-xs font-medium', statusCfg.bg, statusCfg.text)}>
                        <StatusIcon size={12} />
                        {statusCfg.label}
                      </span>
                      <span className={cn('text-xs font-medium px-2 py-1 rounded-full',
                        item.priority === 'high' ? 'bg-red-100 text-red-700' :
                        item.priority === 'medium' ? 'bg-amber-100 text-amber-700' :
                        'bg-slate-100 text-slate-600'
                      )}>
                        {item.priority.toUpperCase()}
                      </span>
                    </div>
                  </div>
                </div>
              )
            })}
          </div>
        </div>
      )}

      {selectedTab === 'audit' && (
        <div className="space-y-4">
          <div className="bg-white rounded-xl border border-slate-200 p-6 mb-4">
            <h3 className="font-semibold text-slate-900 mb-2">Audit Readiness Score</h3>
            <div className="flex items-center gap-4">
              <div className="flex-1">
                <div className="w-full bg-slate-200 rounded-full h-4">
                  <div className="bg-emerald-500 h-4 rounded-full" style={{ width: '82%' }} />
                </div>
              </div>
              <span className="text-2xl font-bold text-emerald-600">82%</span>
            </div>
            <p className="text-sm text-slate-500 mt-2">All critical findings from the last external audit have been addressed. 1 medium finding pending.</p>
          </div>

          <div className="space-y-3">
            {MOCK_AUDITS.map(audit => {
              const statusCfg = AUDIT_STATUS_CONFIG[audit.status]
              return (
                <div key={audit.id} className="bg-white rounded-xl border border-slate-200 p-4">
                  <div className="flex items-center justify-between mb-2">
                    <div>
                      <p className="font-medium text-slate-900">{audit.title}</p>
                      <div className="flex items-center gap-2 mt-1">
                        <span className="text-xs text-slate-500">{audit.id}</span>
                        <span className="text-xs text-slate-400">·</span>
                        <span className="text-xs text-slate-500 flex items-center gap-1">
                          <Calendar size={10} />
                          {audit.scheduledDate}
                        </span>
                        <span className="text-xs text-slate-400">·</span>
                        <span className="text-xs text-slate-500 flex items-center gap-1">
                          <MapPin size={10} />
                          {audit.scope}
                        </span>
                      </div>
                    </div>
                    <div className="flex items-center gap-3">
                      {audit.findings > 0 && (
                        <span className="text-xs text-slate-500">{audit.findings} findings</span>
                      )}
                      <span className={cn('inline-flex px-2 py-1 rounded-full text-xs font-medium', statusCfg.bg, statusCfg.text)}>
                        {statusCfg.label}
                      </span>
                    </div>
                  </div>
                </div>
              )
            })}
          </div>
        </div>
      )}

      {selectedTab === 'filings' && (
        <div className="bg-white rounded-xl border border-slate-200 p-6">
          <h3 className="font-semibold text-slate-900 mb-4">Upcoming Regulatory Filings</h3>
          <div className="space-y-3">
            {[
              { name: 'Nigeria VAT Return', deadline: '2026-03-31', country: '🇳🇬', status: 'Upcoming' },
              { name: 'Kenya Corporate Tax', deadline: '2026-03-31', country: '🇰🇪', status: 'Upcoming' },
              { name: 'SA Provisional Tax', deadline: '2026-04-30', country: '🇿🇦', status: 'Upcoming' },
              { name: 'Ghana PAYE Return', deadline: '2026-04-15', country: '🇬🇭', status: 'Upcoming' },
              { name: 'Ireland Corporation Tax', deadline: '2026-09-23', country: '🇮🇪', status: 'Scheduled' },
              { name: 'UK Corporation Tax', deadline: '2026-12-31', country: '🇬🇧', status: 'Scheduled' },
            ].map(filing => (
              <div key={filing.name} className="flex items-center justify-between p-3 rounded-lg border border-slate-100 hover:bg-slate-50">
                <div className="flex items-center gap-3">
                  <span className="text-xl">{filing.country}</span>
                  <div>
                    <p className="text-sm font-medium text-slate-900">{filing.name}</p>
                    <p className="text-xs text-slate-500">Due: {filing.deadline}</p>
                  </div>
                </div>
                <span className={cn('text-xs font-medium px-2 py-1 rounded-full',
                  filing.status === 'Upcoming' ? 'bg-amber-100 text-amber-700' : 'bg-blue-100 text-blue-700'
                )}>
                  {filing.status}
                </span>
              </div>
            ))}
          </div>
        </div>
      )}
    </div>
  )
}
