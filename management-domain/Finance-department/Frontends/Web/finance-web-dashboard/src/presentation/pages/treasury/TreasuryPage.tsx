import { useState } from 'react'
import {
  Building2,
  TrendingUp,
  TrendingDown,
  ArrowLeftRight,
  Download,
  Brain,
  AlertTriangle,
  DollarSign,
  Shield,
} from 'lucide-react'
import { cn } from '@shared/utils/cn'
import { formatCurrency } from '@shared/utils/cn'

interface CashByCountry {
  country: string
  flag: string
  cash: number
  utilization: number
  trend: number
}

interface FXExposure {
  currency: string
  exposure: number
  risk: 'high' | 'medium' | 'low'
  hedged: number
  unhedged: number
}

const MOCK_CASH_BY_COUNTRY: CashByCountry[] = [
  { country: 'Nigeria', flag: '🇳🇬', cash: 4200000, utilization: 95, trend: 5 },
  { country: 'Kenya', flag: '🇰🇪', cash: 2800000, utilization: 88, trend: 0 },
  { country: 'South Africa', flag: '🇿🇦', cash: 2100000, utilization: 92, trend: -2 },
  { country: 'Ghana', flag: '🇬🇭', cash: 1900000, utilization: 91, trend: 8 },
  { country: 'Ireland', flag: '🇮🇪', cash: 1700000, utilization: 85, trend: 3 },
  { country: 'Others', flag: '🌍', cash: 2500000, utilization: 82, trend: 4 },
]

const MOCK_FX_EXPOSURE: FXExposure[] = [
  { currency: 'NGN', exposure: -3200000, risk: 'high', hedged: 2500000, unhedged: 700000 },
  { currency: 'KES', exposure: -1800000, risk: 'medium', hedged: 1000000, unhedged: 800000 },
  { currency: 'ZAR', exposure: -1200000, risk: 'low', hedged: 1000000, unhedged: 200000 },
  { currency: 'GHS', exposure: -900000, risk: 'low', hedged: 700000, unhedged: 200000 },
  { currency: 'EUR', exposure: 2100000, risk: 'low', hedged: 0, unhedged: 2100000 },
]

const RISK_COLORS = {
  high: 'bg-red-100 text-red-700',
  medium: 'bg-amber-100 text-amber-700',
  low: 'bg-emerald-100 text-emerald-700',
}

export function TreasuryPage() {
  const [selectedTab, setSelectedTab] = useState<'position' | 'forecast' | 'fx' | 'banking'>('position')
  const totalCash = 15200000
  const operatingCash = 12500000
  const investmentCash = 2700000
  const targetCash = 18000000

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-2xl font-bold text-slate-900">Global Treasury</h1>
          <p className="text-slate-500">Cash management, FX exposure, and banking</p>
        </div>
        <div className="flex items-center gap-3">
          <button className="flex items-center gap-2 px-3 py-2 text-sm border border-slate-200 rounded-lg hover:bg-slate-50">
            <Building2 size={16} />
            Manage Banks
          </button>
          <button className="flex items-center gap-2 px-3 py-2 text-sm border border-slate-200 rounded-lg hover:bg-slate-50">
            <Download size={16} />
            Export
          </button>
        </div>
      </div>

      <div className="bg-white rounded-xl border border-slate-200 p-6">
        <div className="flex items-center justify-between mb-4">
          <h3 className="font-semibold text-slate-900">Global Cash Position</h3>
          <span className="text-sm text-slate-500">{((totalCash / targetCash) * 100).toFixed(0)}% of Target</span>
        </div>
        <div className="flex items-center gap-2 mb-4">
          <span className="text-3xl font-bold text-slate-900">{formatCurrency(totalCash, 'USD')}</span>
        </div>
        <div className="w-full bg-slate-200 rounded-full h-3 mb-4">
          <div className="bg-blue-500 h-3 rounded-full" style={{ width: `${(totalCash / targetCash) * 100}%` }} />
        </div>
        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
          <div className="bg-blue-50 rounded-lg p-4">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm text-blue-600">Operating Cash</p>
                <p className="text-xl font-bold text-slate-900">{formatCurrency(operatingCash, 'USD')}</p>
              </div>
              <DollarSign size={24} className="text-blue-400" />
            </div>
            <p className="text-xs text-slate-500 mt-2">94% Utilized</p>
          </div>
          <div className="bg-emerald-50 rounded-lg p-4">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm text-emerald-600">Investment Cash</p>
                <p className="text-xl font-bold text-slate-900">{formatCurrency(investmentCash, 'USD')}</p>
              </div>
              <Shield size={24} className="text-emerald-400" />
            </div>
            <p className="text-xs text-slate-500 mt-2">Available</p>
          </div>
        </div>
      </div>

      <div className="flex gap-2 border-b border-slate-200 pb-0">
        {(['position', 'forecast', 'fx', 'banking'] as const).map(tab => (
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
            {tab === 'position' ? 'Cash by Country' : tab === 'forecast' ? 'AI Forecast' : tab === 'fx' ? 'FX Exposure' : 'Banking'}
          </button>
        ))}
      </div>

      {selectedTab === 'position' && (
        <div className="bg-white rounded-xl border border-slate-200 overflow-hidden">
          <div className="overflow-x-auto">
            <table className="w-full">
              <thead>
                <tr className="border-b border-slate-200 bg-slate-50">
                  <th className="text-left px-4 py-3 text-sm font-medium text-slate-600">Country</th>
                  <th className="text-right px-4 py-3 text-sm font-medium text-slate-600">Cash Position</th>
                  <th className="text-center px-4 py-3 text-sm font-medium text-slate-600">Utilization</th>
                  <th className="text-right px-4 py-3 text-sm font-medium text-slate-600">Trend</th>
                  <th className="text-center px-4 py-3 text-sm font-medium text-slate-600">Action</th>
                </tr>
              </thead>
              <tbody>
                {MOCK_CASH_BY_COUNTRY.map((row) => (
                  <tr key={row.country} className="border-b border-slate-100 hover:bg-slate-50">
                    <td className="px-4 py-3">
                      <div className="flex items-center gap-2">
                        <span className="text-xl">{row.flag}</span>
                        <span className="font-medium text-slate-900">{row.country}</span>
                      </div>
                    </td>
                    <td className="px-4 py-3 text-right font-medium">{formatCurrency(row.cash, 'USD')}</td>
                    <td className="px-4 py-3">
                      <div className="flex items-center gap-2">
                        <div className="flex-1 bg-slate-100 rounded-full h-2">
                          <div
                            className={cn(
                              'h-2 rounded-full',
                              row.utilization > 90 ? 'bg-red-500' : row.utilization > 80 ? 'bg-amber-500' : 'bg-emerald-500'
                            )}
                            style={{ width: `${row.utilization}%` }}
                          />
                        </div>
                        <span className="text-sm text-slate-600">{row.utilization}%</span>
                      </div>
                    </td>
                    <td className="px-4 py-3 text-right">
                      <span className={cn('flex items-center justify-end gap-1 text-sm font-medium',
                        row.trend > 0 ? 'text-emerald-600' : row.trend < 0 ? 'text-red-600' : 'text-slate-500'
                      )}>
                        {row.trend > 0 ? <><TrendingUp size={14} /> +{row.trend}%</> : row.trend < 0 ? <><TrendingDown size={14} /> {row.trend}%</> : '● 0%'}
                      </span>
                    </td>
                    <td className="px-4 py-3 text-center">
                      <button className="text-blue-600 hover:text-blue-700 text-sm font-medium">View</button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      )}

      {selectedTab === 'forecast' && (
        <div className="bg-white rounded-xl border border-slate-200 p-6">
          <div className="flex items-center justify-between mb-6">
            <div>
              <h3 className="font-semibold text-slate-900">AI Cash Flow Forecast (30 Days)</h3>
              <p className="text-sm text-slate-500 mt-1">Predictive model for cash flow management</p>
            </div>
            <div className="flex items-center gap-2">
              <Brain size={20} className="text-purple-500" />
              <span className="text-sm text-purple-600 font-medium">AI-Powered</span>
            </div>
          </div>
          <div className="space-y-4">
            {[
              { week: 'Week 1', projected: 15800000, low: 14200000, high: 17200000 },
              { week: 'Week 2', projected: 14500000, low: 13000000, high: 16000000 },
              { week: 'Week 3', projected: 11200000, low: 9800000, high: 12800000 },
              { week: 'Week 4', projected: 13800000, low: 12000000, high: 15500000 },
            ].map(week => (
              <div key={week.week}>
                <div className="flex items-center justify-between mb-1">
                  <span className="text-sm text-slate-600">{week.week}</span>
                  <span className="text-sm font-medium text-slate-900">{formatCurrency(week.projected, 'USD')}</span>
                </div>
                <div className="relative w-full bg-slate-100 rounded-full h-6">
                  <div
                    className={cn(
                      'h-6 rounded-full flex items-center px-3',
                      week.projected < 12000000 ? 'bg-red-100' : 'bg-blue-100'
                    )}
                    style={{ width: `${(week.projected / 18000000) * 100}%` }}
                  >
                    <span className="text-xs font-medium text-slate-700">
                      {formatCurrency(week.projected, 'USD')}
                    </span>
                  </div>
                </div>
                <div className="flex items-center justify-between mt-1">
                  <span className="text-xs text-slate-400">Low: {formatCurrency(week.low, 'USD')}</span>
                  <span className="text-xs text-slate-400">High: {formatCurrency(week.high, 'USD')}</span>
                </div>
              </div>
            ))}
          </div>
          <div className="mt-6 p-4 bg-amber-50 border border-amber-200 rounded-lg">
            <div className="flex items-start gap-3">
              <AlertTriangle size={20} className="text-amber-600 mt-0.5" />
              <div>
                <p className="font-medium text-amber-800">Cash Low Point: Week 3 - {formatCurrency(11200000, 'USD')}</p>
                <p className="text-sm text-amber-700 mt-1">Recommendation: Transfer $2M from investment accounts to cover operational needs.</p>
              </div>
            </div>
          </div>
        </div>
      )}

      {selectedTab === 'fx' && (
        <div className="space-y-6">
          <div className="bg-white rounded-xl border border-slate-200 overflow-hidden">
            <div className="p-4 border-b border-slate-200">
              <h3 className="font-semibold text-slate-900">FX Exposure Management</h3>
              <p className="text-sm text-slate-500">Base currency: USD</p>
            </div>
            <div className="overflow-x-auto">
              <table className="w-full">
                <thead>
                  <tr className="border-b border-slate-200 bg-slate-50">
                    <th className="text-left px-4 py-3 text-sm font-medium text-slate-600">Currency</th>
                    <th className="text-right px-4 py-3 text-sm font-medium text-slate-600">Exposure</th>
                    <th className="text-center px-4 py-3 text-sm font-medium text-slate-600">Risk Level</th>
                    <th className="text-right px-4 py-3 text-sm font-medium text-slate-600">Hedged</th>
                    <th className="text-right px-4 py-3 text-sm font-medium text-slate-600">Unhedged</th>
                  </tr>
                </thead>
                <tbody>
                  {MOCK_FX_EXPOSURE.map(row => (
                    <tr key={row.currency} className="border-b border-slate-100 hover:bg-slate-50">
                      <td className="px-4 py-3 font-medium text-slate-900">{row.currency}</td>
                      <td className={cn('px-4 py-3 text-right font-medium', row.exposure < 0 ? 'text-red-600' : 'text-emerald-600')}>
                        {row.exposure < 0 ? '-' : '+'}{formatCurrency(Math.abs(row.exposure), 'USD')}
                      </td>
                      <td className="px-4 py-3 text-center">
                        <span className={cn('inline-flex px-2 py-1 rounded-full text-xs font-medium', RISK_COLORS[row.risk])}>
                          {row.risk.charAt(0).toUpperCase() + row.risk.slice(1)}
                        </span>
                      </td>
                      <td className="px-4 py-3 text-right text-sm">{formatCurrency(row.hedged, 'USD')}</td>
                      <td className="px-4 py-3 text-right text-sm">{formatCurrency(row.unhedged, 'USD')}</td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </div>

          <div className="bg-white rounded-xl border border-slate-200 p-6">
            <h3 className="font-semibold text-slate-900 mb-4">Inter-Company Transfers</h3>
            <div className="space-y-3">
              {[
                { from: '🇳🇬 Nigeria', to: '🇮🇪 Ireland HQ', amount: 500000, status: 'Pending', date: '2026-02-18' },
                { from: '🇰🇪 Kenya', to: '🇮🇪 Ireland HQ', amount: 300000, status: 'Completed', date: '2026-02-15' },
                { from: '🇮🇪 Ireland HQ', to: '🇿🇦 South Africa', amount: 200000, status: 'Completed', date: '2026-02-12' },
              ].map((transfer, i) => (
                <div key={i} className="flex items-center justify-between p-3 rounded-lg border border-slate-100">
                  <div className="flex items-center gap-3">
                    <ArrowLeftRight size={18} className="text-blue-500" />
                    <div>
                      <p className="text-sm font-medium text-slate-900">{transfer.from} → {transfer.to}</p>
                      <p className="text-xs text-slate-500">{transfer.date}</p>
                    </div>
                  </div>
                  <div className="text-right">
                    <p className="font-medium text-slate-900">{formatCurrency(transfer.amount, 'USD')}</p>
                    <span className={cn('text-xs font-medium', transfer.status === 'Completed' ? 'text-emerald-600' : 'text-amber-600')}>
                      {transfer.status}
                    </span>
                  </div>
                </div>
              ))}
            </div>
            <button className="mt-4 w-full py-2 bg-blue-600 hover:bg-blue-700 text-white rounded-lg text-sm font-medium transition-colors">
              New Transfer
            </button>
          </div>
        </div>
      )}

      {selectedTab === 'banking' && (
        <div className="bg-white rounded-xl border border-slate-200 p-6">
          <h3 className="font-semibold text-slate-900 mb-4">Banking Partners</h3>
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
            {[
              { name: 'Standard Chartered', accounts: 3, balance: 5200000, country: 'Global' },
              { name: 'First Bank Nigeria', accounts: 2, balance: 3800000, country: 'Nigeria' },
              { name: 'KCB Bank Kenya', accounts: 2, balance: 2400000, country: 'Kenya' },
              { name: 'Standard Bank SA', accounts: 2, balance: 1900000, country: 'South Africa' },
              { name: 'Bank of Ireland', accounts: 2, balance: 1500000, country: 'Ireland' },
              { name: 'GCB Bank Ghana', accounts: 1, balance: 900000, country: 'Ghana' },
            ].map(bank => (
              <div key={bank.name} className="border border-slate-200 rounded-lg p-4 hover:shadow-sm transition-shadow">
                <div className="flex items-center justify-between mb-3">
                  <h4 className="font-medium text-slate-900">{bank.name}</h4>
                  <span className="text-xs bg-blue-100 text-blue-700 px-2 py-0.5 rounded-full">{bank.accounts} accounts</span>
                </div>
                <p className="text-xs text-slate-500">{bank.country}</p>
                <p className="text-lg font-bold text-slate-900 mt-2">{formatCurrency(bank.balance, 'USD')}</p>
              </div>
            ))}
          </div>
        </div>
      )}
    </div>
  )
}
