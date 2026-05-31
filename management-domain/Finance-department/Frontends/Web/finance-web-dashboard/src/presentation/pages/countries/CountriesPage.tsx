import { useState } from 'react'
import {
  Globe,
  TrendingUp,
  TrendingDown,
  ArrowUpRight,
  Search,
  Filter,
  Download,
  BarChart3,
} from 'lucide-react'
import { cn } from '@shared/utils/cn'
import { formatCurrency } from '@shared/utils/cn'

interface CountryData {
  code: string
  name: string
  flag: string
  revenue: number
  expenses: number
  netIncome: number
  budgetAttainment: number
  ar: number
  ap: number
  revenueTrend: 'up' | 'down' | 'neutral'
  expensesTrend: 'up' | 'down' | 'neutral'
  status: 'on_track' | 'at_risk' | 'off_track'
  currency: string
}

const MOCK_COUNTRIES: CountryData[] = [
  { code: 'NGA', name: 'Nigeria', flag: '🇳🇬', revenue: 8200000, expenses: 6100000, netIncome: 2100000, budgetAttainment: 96, ar: 1800000, ap: 1200000, revenueTrend: 'up', expensesTrend: 'up', status: 'on_track', currency: 'NGN' },
  { code: 'KEN', name: 'Kenya', flag: '🇰🇪', revenue: 5100000, expenses: 4200000, netIncome: 900000, budgetAttainment: 91, ar: 800000, ap: 600000, revenueTrend: 'up', expensesTrend: 'up', status: 'on_track', currency: 'KES' },
  { code: 'ZAF', name: 'South Africa', flag: '🇿🇦', revenue: 4800000, expenses: 4500000, netIncome: 300000, budgetAttainment: 78, ar: 1200000, ap: 1500000, revenueTrend: 'down', expensesTrend: 'up', status: 'at_risk', currency: 'ZAR' },
  { code: 'GHA', name: 'Ghana', flag: '🇬🇭', revenue: 3200000, expenses: 2800000, netIncome: 400000, budgetAttainment: 96, ar: 400000, ap: 300000, revenueTrend: 'up', expensesTrend: 'up', status: 'on_track', currency: 'GHS' },
  { code: 'IRL', name: 'Ireland', flag: '🇮🇪', revenue: 2900000, expenses: 2500000, netIncome: 400000, budgetAttainment: 89, ar: 300000, ap: 200000, revenueTrend: 'up', expensesTrend: 'up', status: 'on_track', currency: 'EUR' },
  { code: 'GBR', name: 'United Kingdom', flag: '🇬🇧', revenue: 3500000, expenses: 2900000, netIncome: 600000, budgetAttainment: 92, ar: 500000, ap: 350000, revenueTrend: 'up', expensesTrend: 'neutral', status: 'on_track', currency: 'GBP' },
  { code: 'USA', name: 'United States', flag: '🇺🇸', revenue: 4200000, expenses: 3400000, netIncome: 800000, budgetAttainment: 94, ar: 600000, ap: 400000, revenueTrend: 'up', expensesTrend: 'down', status: 'on_track', currency: 'USD' },
  { code: 'IND', name: 'India', flag: '🇮🇳', revenue: 2100000, expenses: 1700000, netIncome: 400000, budgetAttainment: 88, ar: 250000, ap: 180000, revenueTrend: 'up', expensesTrend: 'neutral', status: 'on_track', currency: 'INR' },
]

const STATUS_COLORS = {
  on_track: { bg: 'bg-emerald-100', text: 'text-emerald-700', dot: 'bg-emerald-500' },
  at_risk: { bg: 'bg-amber-100', text: 'text-amber-700', dot: 'bg-amber-500' },
  off_track: { bg: 'bg-red-100', text: 'text-red-700', dot: 'bg-red-500' },
}

export function CountriesPage() {
  const [searchQuery, setSearchQuery] = useState('')
  const [viewMode, setViewMode] = useState<'grid' | 'table'>('grid')
  const [sortBy, setSortBy] = useState<'revenue' | 'expenses' | 'netIncome' | 'budgetAttainment'>('revenue')
  const [selectedRegion, setSelectedRegion] = useState('all')

  const filteredCountries = MOCK_COUNTRIES
    .filter(c => c.name.toLowerCase().includes(searchQuery.toLowerCase()))
    .sort((a, b) => {
      if (sortBy === 'budgetAttainment') return a[sortBy] - b[sortBy]
      return b[sortBy] - a[sortBy]
    })

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-2xl font-bold text-slate-900">Countries</h1>
          <p className="text-slate-500">Multi-country financial comparison and performance</p>
        </div>
        <div className="flex items-center gap-3">
          <button className="flex items-center gap-2 px-3 py-2 text-sm border border-slate-200 rounded-lg hover:bg-slate-50">
            <Download size={16} />
            Export
          </button>
        </div>
      </div>

      <div className="flex items-center gap-4">
        <div className="relative flex-1 max-w-md">
          <Search size={16} className="absolute left-3 top-1/2 -translate-y-1/2 text-slate-400" />
          <input
            type="text"
            placeholder="Search countries..."
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
            className="w-full pl-9 pr-4 py-2 border border-slate-200 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
          />
        </div>
        <select
          value={selectedRegion}
          onChange={(e) => setSelectedRegion(e.target.value)}
          className="px-3 py-2 border border-slate-200 rounded-lg text-sm"
        >
          <option value="all">All Regions</option>
          <option value="africa">Africa</option>
          <option value="europe">Europe</option>
          <option value="americas">Americas</option>
          <option value="asia">Asia</option>
        </select>
        <select
          value={sortBy}
          onChange={(e) => setSortBy(e.target.value as any)}
          className="px-3 py-2 border border-slate-200 rounded-lg text-sm"
        >
          <option value="revenue">Sort by Revenue</option>
          <option value="expenses">Sort by Expenses</option>
          <option value="netIncome">Sort by Net Income</option>
          <option value="budgetAttainment">Sort by Budget</option>
        </select>
        <div className="flex border border-slate-200 rounded-lg overflow-hidden">
          <button
            onClick={() => setViewMode('grid')}
            className={cn('px-3 py-2 text-sm', viewMode === 'grid' ? 'bg-blue-600 text-white' : 'bg-white hover:bg-slate-50')}
          >
            Grid
          </button>
          <button
            onClick={() => setViewMode('table')}
            className={cn('px-3 py-2 text-sm', viewMode === 'table' ? 'bg-blue-600 text-white' : 'bg-white hover:bg-slate-50')}
          >
            Table
          </button>
        </div>
      </div>

      {viewMode === 'grid' ? (
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-4">
          {filteredCountries.map((country) => {
            const statusColor = STATUS_COLORS[country.status]
            return (
              <div
                key={country.code}
                className="bg-white rounded-xl border border-slate-200 p-5 hover:shadow-md transition-shadow cursor-pointer"
              >
                <div className="flex items-center justify-between mb-4">
                  <div className="flex items-center gap-2">
                    <span className="text-2xl">{country.flag}</span>
                    <div>
                      <h3 className="font-semibold text-slate-900">{country.name}</h3>
                      <p className="text-xs text-slate-500">{country.code} · {country.currency}</p>
                    </div>
                  </div>
                  <div className={cn('flex items-center gap-1 px-2 py-1 rounded-full text-xs font-medium', statusColor.bg, statusColor.text)}>
                    <span className={cn('w-1.5 h-1.5 rounded-full', statusColor.dot)} />
                    {country.budgetAttainment}%
                  </div>
                </div>

                <div className="space-y-3">
                  <div className="flex items-center justify-between">
                    <span className="text-sm text-slate-500">Revenue</span>
                    <div className="flex items-center gap-1">
                      <span className="font-semibold text-slate-900">{formatCurrency(country.revenue, 'USD')}</span>
                      {country.revenueTrend === 'up' ? (
                        <TrendingUp size={14} className="text-emerald-500" />
                      ) : country.revenueTrend === 'down' ? (
                        <TrendingDown size={14} className="text-red-500" />
                      ) : null}
                    </div>
                  </div>
                  <div className="flex items-center justify-between">
                    <span className="text-sm text-slate-500">Expenses</span>
                    <div className="flex items-center gap-1">
                      <span className="font-semibold text-slate-900">{formatCurrency(country.expenses, 'USD')}</span>
                      {country.expensesTrend === 'up' ? (
                        <TrendingUp size={14} className="text-red-500" />
                      ) : country.expensesTrend === 'down' ? (
                        <TrendingDown size={14} className="text-emerald-500" />
                      ) : null}
                    </div>
                  </div>
                  <div className="flex items-center justify-between">
                    <span className="text-sm text-slate-500">Net Income</span>
                    <span className="font-semibold text-emerald-600">{formatCurrency(country.netIncome, 'USD')}</span>
                  </div>
                </div>

                <div className="mt-4 pt-4 border-t border-slate-100">
                  <div className="flex items-center justify-between text-xs text-slate-500">
                    <span>AR: {formatCurrency(country.ar, 'USD')}</span>
                    <span>AP: {formatCurrency(country.ap, 'USD')}</span>
                  </div>
                </div>

                <button className="mt-3 w-full flex items-center justify-center gap-1 py-2 text-sm text-blue-600 hover:bg-blue-50 rounded-lg transition-colors">
                  View Details
                  <ArrowUpRight size={14} />
                </button>
              </div>
            )
          })}
        </div>
      ) : (
        <div className="bg-white rounded-xl border border-slate-200 overflow-hidden">
          <div className="overflow-x-auto">
            <table className="w-full">
              <thead>
                <tr className="border-b border-slate-200 bg-slate-50">
                  <th className="text-left px-4 py-3 text-sm font-medium text-slate-600">Country</th>
                  <th className="text-right px-4 py-3 text-sm font-medium text-slate-600">Revenue</th>
                  <th className="text-right px-4 py-3 text-sm font-medium text-slate-600">Expenses</th>
                  <th className="text-right px-4 py-3 text-sm font-medium text-slate-600">Net Income</th>
                  <th className="text-center px-4 py-3 text-sm font-medium text-slate-600">Budget</th>
                  <th className="text-right px-4 py-3 text-sm font-medium text-slate-600">AR</th>
                  <th className="text-right px-4 py-3 text-sm font-medium text-slate-600">AP</th>
                  <th className="text-center px-4 py-3 text-sm font-medium text-slate-600">Status</th>
                  <th className="text-center px-4 py-3 text-sm font-medium text-slate-600">Action</th>
                </tr>
              </thead>
              <tbody>
                {filteredCountries.map((country) => {
                  const statusColor = STATUS_COLORS[country.status]
                  return (
                    <tr key={country.code} className="border-b border-slate-100 hover:bg-slate-50">
                      <td className="px-4 py-3">
                        <div className="flex items-center gap-2">
                          <span className="text-xl">{country.flag}</span>
                          <div>
                            <p className="font-medium text-slate-900">{country.name}</p>
                            <p className="text-xs text-slate-500">{country.currency}</p>
                          </div>
                        </div>
                      </td>
                      <td className="px-4 py-3 text-right">
                        <div className="flex items-center justify-end gap-1">
                          <span className="font-medium">{formatCurrency(country.revenue, 'USD')}</span>
                          {country.revenueTrend === 'up' ? (
                            <TrendingUp size={14} className="text-emerald-500" />
                          ) : (
                            <TrendingDown size={14} className="text-red-500" />
                          )}
                        </div>
                      </td>
                      <td className="px-4 py-3 text-right font-medium">{formatCurrency(country.expenses, 'USD')}</td>
                      <td className="px-4 py-3 text-right font-medium text-emerald-600">{formatCurrency(country.netIncome, 'USD')}</td>
                      <td className="px-4 py-3 text-center">
                        <span className="font-medium">{country.budgetAttainment}%</span>
                      </td>
                      <td className="px-4 py-3 text-right">{formatCurrency(country.ar, 'USD')}</td>
                      <td className="px-4 py-3 text-right">{formatCurrency(country.ap, 'USD')}</td>
                      <td className="px-4 py-3 text-center">
                        <span className={cn('inline-flex items-center gap-1 px-2 py-1 rounded-full text-xs font-medium', statusColor.bg, statusColor.text)}>
                          <span className={cn('w-1.5 h-1.5 rounded-full', statusColor.dot)} />
                          {country.status === 'on_track' ? 'OK' : country.status === 'at_risk' ? 'At Risk' : 'Off Track'}
                        </span>
                      </td>
                      <td className="px-4 py-3 text-center">
                        <button className="text-blue-600 hover:text-blue-700 text-sm font-medium">View</button>
                      </td>
                    </tr>
                  )
                })}
              </tbody>
            </table>
          </div>
        </div>
      )}

      <div className="bg-white rounded-xl border border-slate-200 p-6">
        <h3 className="font-semibold text-slate-900 mb-4">Performance Heat Map</h3>
        <div className="overflow-x-auto">
          <table className="w-full">
            <thead>
              <tr className="border-b border-slate-200">
                <th className="text-left px-4 py-2 text-sm font-medium text-slate-600">Metric</th>
                {filteredCountries.slice(0, 5).map(c => (
                  <th key={c.code} className="text-center px-4 py-2 text-sm font-medium text-slate-600">{c.flag} {c.name.split(' ')[0]}</th>
                ))}
                <th className="text-center px-4 py-2 text-sm font-medium text-slate-600">AVG</th>
              </tr>
            </thead>
            <tbody>
              {(['Revenue', 'Expenses', 'Budget', 'AR'] as const).map((metric) => (
                <tr key={metric} className="border-b border-slate-100">
                  <td className="px-4 py-3 text-sm font-medium text-slate-700">{metric}</td>
                  {filteredCountries.slice(0, 5).map(c => {
                    const val = metric === 'Revenue' ? c.revenue : metric === 'Expenses' ? c.expenses : metric === 'Budget' ? c.budgetAttainment : c.ar
                    const maxVal = Math.max(...filteredCountries.slice(0, 5).map(x => metric === 'Revenue' ? x.revenue : metric === 'Expenses' ? x.expenses : metric === 'Budget' ? x.budgetAttainment : x.ar))
                    const ratio = maxVal > 0 ? val / maxVal : 0
                    const color = ratio >= 0.8 ? 'bg-emerald-100 text-emerald-700' : ratio >= 0.5 ? 'bg-amber-100 text-amber-700' : 'bg-red-100 text-red-700'
                    return (
                      <td key={c.code} className="text-center px-4 py-3">
                        <span className={cn('inline-block px-2 py-1 rounded text-xs font-medium', color)}>
                          {metric === 'Budget' ? `${val}%` : formatCurrency(val, 'USD')}
                        </span>
                      </td>
                    )
                  })}
                  <td className="text-center px-4 py-3 text-sm text-slate-500">-</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  )
}
