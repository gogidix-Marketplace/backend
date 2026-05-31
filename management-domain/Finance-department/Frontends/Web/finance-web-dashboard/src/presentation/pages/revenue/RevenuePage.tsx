import { useState } from 'react'
import {
  DollarSign,
  TrendingUp,
  TrendingDown,
  BarChart3,
  ArrowUpRight,
  Download,
  Filter,
  Brain,
  AlertTriangle,
} from 'lucide-react'
import { cn } from '@shared/utils/cn'
import { formatCurrency } from '@shared/utils/cn'

interface RevenueByCountry {
  code: string
  name: string
  flag: string
  revenue: number
  growth: number
  percentage: number
}

interface RevenueByBusinessLine {
  name: string
  revenue: number
  percentage: number
  color: string
}

interface RevenueTrend {
  month: string
  actual: number
  forecast?: number
}

const MOCK_REVENUE_BY_COUNTRY: RevenueByCountry[] = [
  { code: 'NGA', name: 'Nigeria', flag: '🇳🇬', revenue: 8200000, growth: 12, percentage: 19.3 },
  { code: 'KEN', name: 'Kenya', flag: '🇰🇪', revenue: 5100000, growth: 8, percentage: 12.0 },
  { code: 'ZAF', name: 'South Africa', flag: '🇿🇦', revenue: 4800000, growth: -3, percentage: 11.3 },
  { code: 'USA', name: 'United States', flag: '🇺🇸', revenue: 4200000, growth: 15, percentage: 9.9 },
  { code: 'GBR', name: 'United Kingdom', flag: '🇬🇧', revenue: 3500000, growth: 5, percentage: 8.2 },
  { code: 'GHA', name: 'Ghana', flag: '🇬🇭', revenue: 3200000, growth: 15, percentage: 7.5 },
  { code: 'IRL', name: 'Ireland', flag: '🇮🇪', revenue: 2900000, growth: 5, percentage: 6.8 },
  { code: 'IND', name: 'India', flag: '🇮🇳', revenue: 2100000, growth: 18, percentage: 4.9 },
  { code: 'Others', name: 'Others', flag: '🌍', revenue: 8400000, growth: 6, percentage: 19.8 },
]

const MOCK_REVENUE_BY_BUSINESS: RevenueByBusinessLine[] = [
  { name: 'E-Commerce', revenue: 15100000, percentage: 35.5, color: 'bg-blue-500' },
  { name: 'Logistics', revenue: 12000000, percentage: 28.2, color: 'bg-emerald-500' },
  { name: 'Services', revenue: 10000000, percentage: 23.5, color: 'bg-purple-500' },
  { name: 'Financial Services', revenue: 3100000, percentage: 7.3, color: 'bg-amber-500' },
  { name: 'Others', revenue: 2400000, percentage: 5.6, color: 'bg-slate-400' },
]

const MOCK_REVENUE_TREND: RevenueTrend[] = [
  { month: 'Sep 2025', actual: 35000000 },
  { month: 'Oct 2025', actual: 37500000 },
  { month: 'Nov 2025', actual: 39000000 },
  { month: 'Dec 2025', actual: 40500000 },
  { month: 'Jan 2026', actual: 41500000 },
  { month: 'Feb 2026', actual: 42500000 },
  { month: 'Mar 2026', actual: 0, forecast: 48200000 },
  { month: 'Apr 2026', actual: 0, forecast: 51000000 },
]

const MOCK_AR_AGING = [
  { range: '0-30 Days', amount: 3200000, count: 45, color: 'bg-emerald-500' },
  { range: '31-60 Days', amount: 1800000, count: 22, color: 'bg-blue-500' },
  { range: '61-90 Days', amount: 950000, count: 12, color: 'bg-amber-500' },
  { range: '90+ Days', amount: 650000, count: 8, color: 'bg-red-500' },
]

export function RevenuePage() {
  const [selectedTab, setSelectedTab] = useState<'overview' | 'by-country' | 'ar-aging' | 'forecast'>('overview')
  const totalRevenue = 42500000
  const previousRevenue = 39200000
  const growthPercent = ((totalRevenue - previousRevenue) / previousRevenue * 100).toFixed(1)

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-2xl font-bold text-slate-900">Global Revenue</h1>
          <p className="text-slate-500">Revenue tracking, trends, and AI-powered forecasts</p>
        </div>
        <div className="flex items-center gap-3">
          <select className="px-3 py-2 border border-slate-200 rounded-lg text-sm">
            <option>Feb 2026</option>
            <option>Jan 2026</option>
            <option>Q1 2026</option>
            <option>YTD 2026</option>
          </select>
          <select className="px-3 py-2 border border-slate-200 rounded-lg text-sm">
            <option>USD</option>
            <option>EUR</option>
            <option>GBP</option>
          </select>
          <button className="flex items-center gap-2 px-3 py-2 text-sm border border-slate-200 rounded-lg hover:bg-slate-50">
            <Download size={16} />
            Export
          </button>
        </div>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
        <div className="bg-white rounded-xl border border-slate-200 p-5">
          <div className="flex items-center justify-between mb-2">
            <span className="text-sm text-slate-500">Total Revenue</span>
            <DollarSign size={18} className="text-blue-500" />
          </div>
          <p className="text-2xl font-bold text-slate-900">{formatCurrency(totalRevenue, 'USD')}</p>
          <div className="flex items-center gap-1 mt-1">
            <TrendingUp size={14} className="text-emerald-500" />
            <span className="text-sm text-emerald-600 font-medium">+{growthPercent}%</span>
            <span className="text-xs text-slate-400">vs last month</span>
          </div>
        </div>
        <div className="bg-white rounded-xl border border-slate-200 p-5">
          <div className="flex items-center justify-between mb-2">
            <span className="text-sm text-slate-500">Revenue Target</span>
            <BarChart3 size={18} className="text-purple-500" />
          </div>
          <p className="text-2xl font-bold text-slate-900">{formatCurrency(40000000, 'USD')}</p>
          <div className="mt-2 w-full bg-slate-200 rounded-full h-2">
            <div className="bg-emerald-500 h-2 rounded-full" style={{ width: `${(totalRevenue / 40000000) * 100}%` }} />
          </div>
          <p className="text-xs text-slate-500 mt-1">{((totalRevenue / 40000000) * 100).toFixed(1)}% of target</p>
        </div>
        <div className="bg-white rounded-xl border border-slate-200 p-5">
          <div className="flex items-center justify-between mb-2">
            <span className="text-sm text-slate-500">AI Forecast (Mar)</span>
            <Brain size={18} className="text-purple-500" />
          </div>
          <p className="text-2xl font-bold text-purple-600">{formatCurrency(48200000, 'USD')}</p>
          <div className="flex items-center gap-1 mt-1">
            <TrendingUp size={14} className="text-purple-500" />
            <span className="text-sm text-purple-600 font-medium">+13.4%</span>
            <span className="text-xs text-slate-400">95% confidence</span>
          </div>
        </div>
        <div className="bg-white rounded-xl border border-slate-200 p-5">
          <div className="flex items-center justify-between mb-2">
            <span className="text-sm text-slate-500">Accounts Receivable</span>
            <ArrowUpRight size={18} className="text-amber-500" />
          </div>
          <p className="text-2xl font-bold text-slate-900">{formatCurrency(6600000, 'USD')}</p>
          <div className="flex items-center gap-1 mt-1">
            <AlertTriangle size={14} className="text-amber-500" />
            <span className="text-sm text-amber-600">8 invoices &gt;90 days</span>
          </div>
        </div>
      </div>

      <div className="flex gap-2 border-b border-slate-200 pb-0">
        {(['overview', 'by-country', 'ar-aging', 'forecast'] as const).map(tab => (
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
            {tab === 'overview' ? 'Revenue Trend' : tab === 'by-country' ? 'By Country' : tab === 'ar-aging' ? 'AR Aging' : 'AI Forecast'}
          </button>
        ))}
      </div>

      {selectedTab === 'overview' && (
        <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
          <div className="lg:col-span-2 bg-white rounded-xl border border-slate-200 p-6">
            <h3 className="font-semibold text-slate-900 mb-4">Revenue Trend</h3>
            <div className="space-y-3">
              {MOCK_REVENUE_TREND.map((item) => {
                const maxVal = 52000000
                const val = item.actual || item.forecast || 0
                const isForecast = item.actual === 0
                return (
                  <div key={item.month} className="flex items-center gap-4">
                    <span className="text-sm text-slate-600 w-24">{item.month}</span>
                    <div className="flex-1">
                      <div className="w-full bg-slate-100 rounded-full h-6 relative">
                        <div
                          className={cn('h-6 rounded-full flex items-center px-3', isForecast ? 'bg-purple-200' : 'bg-blue-500')}
                          style={{ width: `${(val / maxVal) * 100}%` }}
                        >
                          <span className={cn('text-xs font-medium whitespace-nowrap', isForecast ? 'text-purple-700' : 'text-white')}>
                            {formatCurrency(val, 'USD')}
                          </span>
                        </div>
                      </div>
                    </div>
                    {isForecast && (
                      <span className="text-xs text-purple-600 font-medium flex items-center gap-1">
                        <Brain size={12} />
                        Forecast
                      </span>
                    )}
                  </div>
                )
              })}
            </div>
          </div>
          <div className="bg-white rounded-xl border border-slate-200 p-6">
            <h3 className="font-semibold text-slate-900 mb-4">Revenue by Business Line</h3>
            <div className="space-y-4">
              {MOCK_REVENUE_BY_BUSINESS.map((line) => (
                <div key={line.name}>
                  <div className="flex items-center justify-between mb-1">
                    <span className="text-sm text-slate-700">{line.name}</span>
                    <span className="text-sm font-medium text-slate-900">{formatCurrency(line.revenue, 'USD')}</span>
                  </div>
                  <div className="w-full bg-slate-100 rounded-full h-2">
                    <div className={cn('h-2 rounded-full', line.color)} style={{ width: `${line.percentage}%` }} />
                  </div>
                  <p className="text-xs text-slate-500 mt-1">{line.percentage}%</p>
                </div>
              ))}
            </div>
          </div>
        </div>
      )}

      {selectedTab === 'by-country' && (
        <div className="bg-white rounded-xl border border-slate-200 overflow-hidden">
          <table className="w-full">
            <thead>
              <tr className="border-b border-slate-200 bg-slate-50">
                <th className="text-left px-4 py-3 text-sm font-medium text-slate-600">Country</th>
                <th className="text-right px-4 py-3 text-sm font-medium text-slate-600">Revenue</th>
                <th className="text-right px-4 py-3 text-sm font-medium text-slate-600">Growth</th>
                <th className="text-right px-4 py-3 text-sm font-medium text-slate-600">% of Total</th>
                <th className="text-center px-4 py-3 text-sm font-medium text-slate-600">Contribution</th>
              </tr>
            </thead>
            <tbody>
              {MOCK_REVENUE_BY_COUNTRY.map((country) => (
                <tr key={country.code} className="border-b border-slate-100 hover:bg-slate-50">
                  <td className="px-4 py-3">
                    <div className="flex items-center gap-2">
                      <span className="text-xl">{country.flag}</span>
                      <span className="font-medium text-slate-900">{country.name}</span>
                    </div>
                  </td>
                  <td className="px-4 py-3 text-right font-medium">{formatCurrency(country.revenue, 'USD')}</td>
                  <td className="px-4 py-3 text-right">
                    <span className={cn('flex items-center justify-end gap-1 text-sm font-medium',
                      country.growth >= 0 ? 'text-emerald-600' : 'text-red-600'
                    )}>
                      {country.growth >= 0 ? <TrendingUp size={14} /> : <TrendingDown size={14} />}
                      {country.growth >= 0 ? '+' : ''}{country.growth}%
                    </span>
                  </td>
                  <td className="px-4 py-3 text-right text-sm">{country.percentage}%</td>
                  <td className="px-4 py-3">
                    <div className="w-full bg-slate-100 rounded-full h-2">
                      <div className="bg-blue-500 h-2 rounded-full" style={{ width: `${country.percentage}%` }} />
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}

      {selectedTab === 'ar-aging' && (
        <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
          <div className="bg-white rounded-xl border border-slate-200 p-6">
            <h3 className="font-semibold text-slate-900 mb-4">AR Aging Summary</h3>
            <div className="space-y-4">
              {MOCK_AR_AGING.map((bucket) => (
                <div key={bucket.range} className="flex items-center gap-4">
                  <span className="text-sm text-slate-600 w-24">{bucket.range}</span>
                  <div className="flex-1">
                    <div className="w-full bg-slate-100 rounded-full h-8 relative">
                      <div
                        className={cn('h-8 rounded-full flex items-center px-3', bucket.color)}
                        style={{ width: `${(bucket.amount / 6600000) * 100}%` }}
                      >
                        <span className="text-xs font-medium text-white whitespace-nowrap">
                          {formatCurrency(bucket.amount, 'USD')}
                        </span>
                      </div>
                    </div>
                  </div>
                  <span className="text-sm text-slate-500 w-16 text-right">{bucket.count} invoices</span>
                </div>
              ))}
            </div>
          </div>
          <div className="bg-white rounded-xl border border-slate-200 p-6">
            <h3 className="font-semibold text-slate-900 mb-4">Top Overdue Invoices</h3>
            <div className="space-y-3">
              {[
                { id: 'INV-2026-001', customer: 'Lagos Enterprises', amount: 125000, days: 95, country: '🇳🇬' },
                { id: 'INV-2026-014', customer: 'Nairobi Logistics Co', amount: 89000, days: 78, country: '🇰🇪' },
                { id: 'INV-2026-023', customer: 'Cape Tech Solutions', amount: 67000, days: 65, country: '🇿🇦' },
                { id: 'INV-2026-031', customer: 'Accra Digital Hub', amount: 45000, days: 52, country: '🇬🇭' },
                { id: 'INV-2026-042', customer: 'Dublin Trading Ltd', amount: 38000, days: 41, country: '🇮🇪' },
              ].map(inv => (
                <div key={inv.id} className="flex items-center justify-between p-3 rounded-lg border border-slate-100 hover:bg-slate-50">
                  <div className="flex items-center gap-3">
                    <span className="text-xl">{inv.country}</span>
                    <div>
                      <p className="text-sm font-medium text-slate-900">{inv.customer}</p>
                      <p className="text-xs text-slate-500">{inv.id} · {inv.days} days overdue</p>
                    </div>
                  </div>
                  <span className="font-semibold text-red-600">{formatCurrency(inv.amount, 'USD')}</span>
                </div>
              ))}
            </div>
          </div>
        </div>
      )}

      {selectedTab === 'forecast' && (
        <div className="bg-white rounded-xl border border-slate-200 p-6">
          <div className="flex items-center justify-between mb-6">
            <div>
              <h3 className="font-semibold text-slate-900">AI Revenue Forecast</h3>
              <p className="text-sm text-slate-500 mt-1">LSTM Neural Network · 94.2% MAPE accuracy</p>
            </div>
            <div className="flex items-center gap-2">
              <Brain size={20} className="text-purple-500" />
              <span className="text-sm text-purple-600 font-medium">Powered by AI</span>
            </div>
          </div>

          <div className="grid grid-cols-1 md:grid-cols-3 gap-4 mb-6">
            {[
              { label: 'Baseline', value: 48200000, confidence: '95%', color: 'text-blue-600' },
              { label: 'Optimistic', value: 52100000, confidence: '75%', color: 'text-emerald-600' },
              { label: 'Pessimistic', value: 44800000, confidence: '82%', color: 'text-red-600' },
            ].map(scenario => (
              <div key={scenario.label} className="border border-slate-200 rounded-lg p-4">
                <p className="text-sm text-slate-500">{scenario.label}</p>
                <p className={cn('text-xl font-bold', scenario.color)}>{formatCurrency(scenario.value, 'USD')}</p>
                <p className="text-xs text-slate-400 mt-1">{scenario.confidence} confidence</p>
              </div>
            ))}
          </div>

          <h4 className="font-medium text-slate-900 mb-3">Key Drivers</h4>
          <div className="space-y-2 mb-6">
            {[
              { driver: 'Nigeria expansion', impact: '+$800K', positive: true },
              { driver: 'Enterprise renewals', impact: '+$1.2M', positive: true },
              { driver: 'Seasonal demand increase', impact: '+$500K', positive: true },
              { driver: 'FX volatility (NGN)', impact: '-$300K', positive: false },
              { driver: 'Competition in Kenya', impact: '-$200K', positive: false },
            ].map(item => (
              <div key={item.driver} className="flex items-center justify-between py-2 border-b border-slate-100">
                <span className="text-sm text-slate-700">{item.driver}</span>
                <span className={cn('text-sm font-semibold', item.positive ? 'text-emerald-600' : 'text-red-600')}>
                  {item.impact}
                </span>
              </div>
            ))}
          </div>

          <h4 className="font-medium text-slate-900 mb-3">Forecast by Country</h4>
          <div className="overflow-x-auto">
            <table className="w-full">
              <thead>
                <tr className="border-b border-slate-200">
                  <th className="text-left px-4 py-2 text-sm font-medium text-slate-600">Country</th>
                  <th className="text-right px-4 py-2 text-sm font-medium text-slate-600">Current</th>
                  <th className="text-right px-4 py-2 text-sm font-medium text-slate-600">Forecast</th>
                  <th className="text-right px-4 py-2 text-sm font-medium text-slate-600">Growth</th>
                </tr>
              </thead>
              <tbody>
                {[
                  { name: '🇳🇬 Nigeria', current: 8200000, forecast: 9500000 },
                  { name: '🇰🇪 Kenya', current: 5100000, forecast: 5800000 },
                  { name: '🇿🇦 South Africa', current: 4800000, forecast: 4900000 },
                  { name: '🇬🇭 Ghana', current: 3200000, forecast: 3800000 },
                  { name: 'Others', current: 21200000, forecast: 24200000 },
                ].map(row => (
                  <tr key={row.name} className="border-b border-slate-100">
                    <td className="px-4 py-2 text-sm font-medium text-slate-900">{row.name}</td>
                    <td className="px-4 py-2 text-right text-sm">{formatCurrency(row.current, 'USD')}</td>
                    <td className="px-4 py-2 text-right text-sm font-medium text-purple-600">{formatCurrency(row.forecast, 'USD')}</td>
                    <td className="px-4 py-2 text-right text-sm text-emerald-600 font-medium">
                      +{((row.forecast - row.current) / row.current * 100).toFixed(0)}%
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      )}
    </div>
  )
}
