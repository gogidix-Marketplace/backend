import { useState } from 'react'
import { Card, CardContent, CardHeader, CardTitle, CardDescription } from '@shared/components/ui/card'
import { Badge } from '@shared/components/ui/badge'
import { Button } from '@shared/components/ui/button'

export default function CFOFinancialsPage() {
  const [period, setPeriod] = useState<'monthly' | 'quarterly' | 'annual'>('quarterly')

  const plSummary = [
    { label: 'Revenue', value: '$58.6M', pct: null, bold: true },
    { label: 'COGS', value: '-$28.2M', pct: null, bold: false },
    { label: 'Gross Profit', value: '$30.4M', pct: '51.9%', bold: true },
    { label: 'Operating Expenses', value: '-$22.7M', pct: null, bold: false },
    { label: 'EBITDA', value: '$7.8M', pct: '13.3%', bold: true },
    { label: 'Net Income', value: '$5.2M', pct: '8.9%', bold: true },
  ]

  const businessUnits = [
    { name: 'Haulage', revenue: '$12.8M', margin: '28%', growth: '+4.2%', color: 'border-l-blue-500' },
    { name: 'E-Commerce', revenue: '$9.8M', margin: '20%', growth: '+6.8%', color: 'border-l-emerald-500' },
    { name: 'Courier', revenue: '$8.2M', margin: '17%', growth: '+1.5%', color: 'border-l-amber-500' },
    { name: 'Warehousing', revenue: '$5.1M', margin: '18%', growth: '+2.4%', color: 'border-l-purple-500' },
    { name: 'Ocean', revenue: '$4.2M', margin: '12%', growth: '-2.1%', color: 'border-l-cyan-500' },
    { name: 'Air', revenue: '$3.8M', margin: '16%', growth: '+3.0%', color: 'border-l-rose-500' },
    { name: 'Procurement', revenue: '$3.2M', margin: '75%', growth: '+1.1%', color: 'border-l-indigo-500' },
    { name: 'Admin', revenue: '$1.5M', margin: '27%', growth: '-0.3%', color: 'border-l-slate-500' },
  ]

  const cashFlow = [
    { label: 'Operating Activities', value: '+$8.2M', positive: true },
    { label: 'Investing Activities', value: '-$2.1M', positive: false },
    { label: 'Financing Activities', value: '-$1.5M', positive: false },
    { label: 'Net Cash Flow', value: '+$4.6M', positive: true },
  ]

  const settlements = [
    { unit: 'Haulage', amount: '$1.2M', batch: 'Apr 25, 09:00' },
    { unit: 'E-Commerce', amount: '$890K', batch: 'Apr 25, 12:00' },
    { unit: 'Courier', amount: '$650K', batch: 'Apr 26, 09:00' },
    { unit: 'Ocean', amount: '$420K', batch: 'Apr 26, 14:00' },
    { unit: 'Air', amount: '$310K', batch: 'Apr 27, 09:00' },
  ]

  return (
    <div className="space-y-5">
      <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
        <div>
          <h1 className="text-2xl sm:text-3xl font-bold text-slate-900 dark:text-white">Financial Statements</h1>
          <p className="text-sm text-slate-500 dark:text-slate-400 mt-1">Income statement, balance sheet, and cash flow analysis</p>
        </div>
        <div className="flex items-center gap-2">
          <div className="flex rounded-md border overflow-hidden">
            {(['monthly', 'quarterly', 'annual'] as const).map((p) => (
              <button
                key={p}
                onClick={() => setPeriod(p)}
                className={`px-3 py-1.5 text-xs font-medium capitalize ${period === p ? 'bg-[#0D47A1] text-white' : 'bg-white dark:bg-slate-800 text-slate-600 hover:bg-slate-50'} ${p !== 'monthly' ? 'border-l' : ''}`}
              >
                {p}
              </button>
            ))}
          </div>
          <Button variant="outline" size="sm">Export</Button>
        </div>
      </div>

      <Card>
        <CardHeader>
          <CardTitle className="text-lg">P&L Summary</CardTitle>
          <CardDescription>Profit and loss overview for {period} period</CardDescription>
        </CardHeader>
        <CardContent>
          <div className="space-y-2">
            {plSummary.map((row) => (
              <div key={row.label} className={`flex items-center justify-between py-2 ${row.bold ? 'font-semibold' : ''} ${row.label === 'Gross Profit' || row.label === 'EBITDA' ? 'border-t border-slate-100 dark:border-slate-800 pt-3' : ''}`}>
                <span className={`text-sm ${row.bold ? 'text-slate-900 dark:text-white' : 'text-slate-600 dark:text-slate-400'}`}>
                  {row.label}
                </span>
                <div className="flex items-center gap-3">
                  {row.pct && <Badge variant="secondary" className="text-[10px]">{row.pct}</Badge>}
                  <span className={`text-sm ${row.bold ? 'font-semibold text-slate-900 dark:text-white' : 'text-slate-700 dark:text-slate-300'}`}>
                    {row.value}
                  </span>
                </div>
              </div>
            ))}
          </div>
        </CardContent>
      </Card>

      <Card>
        <CardHeader>
          <CardTitle className="text-lg">Revenue by Business Unit</CardTitle>
          <CardDescription>Performance breakdown across all units</CardDescription>
        </CardHeader>
        <CardContent>
          <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-3">
            {businessUnits.map((unit) => (
              <div key={unit.name} className={`p-3 rounded-lg border border-slate-200 dark:border-slate-700 border-l-4 ${unit.color}`}>
                <p className="text-sm font-medium text-slate-900 dark:text-white">{unit.name}</p>
                <p className="text-lg font-bold text-slate-900 dark:text-white mt-1">{unit.revenue}</p>
                <div className="flex items-center gap-2 mt-1">
                  <span className="text-xs text-slate-500 dark:text-slate-400">Margin: {unit.margin}</span>
                  <span className={`text-xs font-semibold ${unit.growth.startsWith('+') ? 'text-emerald-600' : 'text-red-500'}`}>{unit.growth}</span>
                </div>
              </div>
            ))}
          </div>
        </CardContent>
      </Card>

      <div className="grid gap-4 sm:gap-5 lg:grid-cols-2">
        <Card>
          <CardHeader>
            <CardTitle className="text-lg">Cash Flow</CardTitle>
            <CardDescription>Cash flow breakdown by activity type</CardDescription>
          </CardHeader>
          <CardContent>
            <div className="space-y-3">
              {cashFlow.map((item) => (
                <div key={item.label} className={`flex items-center justify-between p-3 rounded-lg ${item.label === 'Net Cash Flow' ? 'bg-slate-50 dark:bg-slate-800/50 border border-slate-200 dark:border-slate-700' : ''}`}>
                  <span className="text-sm font-medium text-slate-700 dark:text-slate-300">{item.label}</span>
                  <span className={`text-sm font-semibold ${item.positive ? 'text-emerald-600' : 'text-red-500'}`}>{item.value}</span>
                </div>
              ))}
            </div>
          </CardContent>
        </Card>

        <Card>
          <CardHeader>
            <CardTitle className="text-lg">Settlement Pipeline</CardTitle>
            <CardDescription>Pending batch settlements by unit</CardDescription>
          </CardHeader>
          <CardContent>
            <div className="space-y-3">
              {settlements.map((s) => (
                <div key={s.unit} className="flex items-center justify-between p-3 rounded-lg border border-slate-200 dark:border-slate-700 hover:bg-slate-50 dark:hover:bg-slate-800/50">
                  <div>
                    <p className="text-sm font-medium text-slate-900 dark:text-white">{s.unit}</p>
                    <p className="text-xs text-slate-500 dark:text-slate-400">Batch: {s.batch}</p>
                  </div>
                  <div className="text-right">
                    <p className="text-sm font-semibold text-slate-900 dark:text-white">{s.amount}</p>
                    <Badge variant="warning" className="text-[10px]">Pending</Badge>
                  </div>
                </div>
              ))}
            </div>
          </CardContent>
        </Card>
      </div>
    </div>
  )
}
