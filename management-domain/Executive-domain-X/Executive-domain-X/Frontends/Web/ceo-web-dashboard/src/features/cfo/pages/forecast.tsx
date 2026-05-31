import { useState } from 'react'
import { Card, CardContent, CardHeader, CardTitle, CardDescription } from '@shared/components/ui/card'
import { Badge } from '@shared/components/ui/badge'
import { Button } from '@shared/components/ui/button'

export default function CFOForecastPage() {
  const [quarter, setQuarter] = useState<'Q1' | 'Q2' | 'Q3' | 'Q4'>('Q2')

  const forecastCards = [
    { title: 'Q2 Revenue', value: '$15.2M', change: '+8%', positive: true, color: 'border-l-blue-500' },
    { title: 'Q2 Expenses', value: '$9.8M', change: '-2%', positive: true, color: 'border-l-amber-500' },
    { title: 'Q2 Margin', value: '35.4%', change: '+1.2%', positive: true, color: 'border-l-emerald-500' },
    { title: 'Full Year', value: '$62.1M', change: '+14%', positive: true, color: 'border-l-purple-500' },
  ]

  const revenueTrend = [
    { month: 'Jan', value: 4.2, forecast: false },
    { month: 'Feb', value: 4.5, forecast: false },
    { month: 'Mar', value: 4.8, forecast: false },
    { month: 'Apr', value: 5.0, forecast: false },
    { month: 'May', value: 4.9, forecast: false },
    { month: 'Jun', value: 5.2, forecast: false },
    { month: 'Jul', value: 5.4, forecast: true },
    { month: 'Aug', value: 5.6, forecast: true },
    { month: 'Sep', value: 5.8, forecast: true },
    { month: 'Oct', value: 5.5, forecast: true },
    { month: 'Nov', value: 5.7, forecast: true },
    { month: 'Dec', value: 5.9, forecast: true },
  ]

  const maxRevenue = Math.max(...revenueTrend.map(m => m.value))

  const businessUnitForecasts = [
    { unit: 'Haulage', current: '$12.8M', forecast: '$14.2M', variance: '+10.9%' },
    { unit: 'E-Commerce', current: '$9.8M', forecast: '$11.5M', variance: '+17.3%' },
    { unit: 'Courier', current: '$8.2M', forecast: '$8.9M', variance: '+8.5%' },
    { unit: 'Warehousing', current: '$5.1M', forecast: '$5.8M', variance: '+13.7%' },
    { unit: 'Ocean', current: '$4.2M', forecast: '$4.0M', variance: '-4.8%' },
    { unit: 'Air', current: '$3.8M', forecast: '$4.2M', variance: '+10.5%' },
    { unit: 'Procurement', current: '$3.2M', forecast: '$3.6M', variance: '+12.5%' },
    { unit: 'Admin', current: '$1.5M', forecast: '$1.4M', variance: '-6.7%' },
  ]

  const riskFactors = [
    { factor: 'Currency exchange rate volatility', severity: 'high' as const },
    { factor: 'Regulatory changes in APAC markets', severity: 'medium' as const },
    { factor: 'Market expansion in Africa', severity: 'low' as const },
    { factor: 'Technology infrastructure investment', severity: 'medium' as const },
  ]

  const severityVariant = { high: 'destructive' as const, medium: 'warning' as const, low: 'success' as const }

  return (
    <div className="space-y-5">
      <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
        <div>
          <h1 className="text-2xl sm:text-3xl font-bold text-slate-900 dark:text-white">Financial Forecast</h1>
          <p className="text-sm text-slate-500 dark:text-slate-400 mt-1">Revenue and expense projections for the fiscal year</p>
        </div>
        <div className="flex items-center gap-2">
          <div className="flex rounded-md border overflow-hidden">
            {(['Q1', 'Q2', 'Q3', 'Q4'] as const).map((q) => (
              <button
                key={q}
                onClick={() => setQuarter(q)}
                className={`px-3 py-1.5 text-xs font-medium ${quarter === q ? 'bg-[#0D47A1] text-white' : 'bg-white dark:bg-slate-800 text-slate-600 hover:bg-slate-50'} ${q !== 'Q1' ? 'border-l' : ''}`}
              >
                {q}
              </button>
            ))}
          </div>
          <Button variant="outline" size="sm">Export</Button>
        </div>
      </div>

      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
        {forecastCards.map((card) => (
          <Card key={card.title} className={`border-l-4 ${card.color}`}>
            <CardContent className="p-4">
              <p className="text-xs font-medium text-slate-500 dark:text-slate-400">{card.title}</p>
              <p className="text-xl font-bold text-slate-900 dark:text-white mt-1">{card.value}</p>
              <div className="flex items-center gap-1 mt-1">
                <span className={`text-xs font-semibold ${card.positive ? 'text-emerald-600' : 'text-red-500'}`}>
                  {card.change}
                </span>
                <span className="text-xs text-slate-400">vs prior</span>
              </div>
            </CardContent>
          </Card>
        ))}
      </div>

      <Card>
        <CardHeader>
          <div className="flex items-center justify-between">
            <div>
              <CardTitle className="text-lg">Revenue Trend</CardTitle>
              <CardDescription>12-month actual vs forecast trajectory</CardDescription>
            </div>
            <div className="flex items-center gap-3">
              <div className="flex items-center gap-1.5 text-xs text-slate-500">
                <div className="w-3 h-3 rounded bg-blue-500" /> Actual
              </div>
              <div className="flex items-center gap-1.5 text-xs text-slate-500">
                <div className="w-3 h-3 rounded bg-blue-300 border border-blue-400 border-dashed" /> Forecast
              </div>
            </div>
          </div>
        </CardHeader>
        <CardContent>
          <div className="flex items-end gap-1.5 h-48">
            {revenueTrend.map((item) => (
              <div key={item.month} className="flex-1 flex flex-col items-center gap-1">
                <div className="w-full flex flex-col items-center justify-end h-40">
                  <div
                    className={`w-full max-w-[28px] rounded-t ${item.forecast ? 'bg-blue-200 dark:bg-blue-900 border-2 border-dashed border-blue-400' : 'bg-blue-500'}`}
                    style={{ height: `${(item.value / maxRevenue) * 100}%` }}
                  />
                </div>
                <span className="text-[10px] text-slate-500 font-medium">{item.month}</span>
              </div>
            ))}
          </div>
        </CardContent>
      </Card>

      <Card>
        <CardHeader>
          <CardTitle className="text-lg">Forecast by Business Unit</CardTitle>
          <CardDescription>Current performance vs projected year-end</CardDescription>
        </CardHeader>
        <CardContent>
          <div className="overflow-x-auto">
            <table className="w-full text-sm">
              <thead>
                <tr className="border-b border-slate-200 dark:border-slate-700">
                  <th className="text-left py-2 px-3 font-semibold text-slate-600 dark:text-slate-300">Business Unit</th>
                  <th className="text-right py-2 px-3 font-semibold text-slate-600 dark:text-slate-300">Current</th>
                  <th className="text-right py-2 px-3 font-semibold text-slate-600 dark:text-slate-300">Forecast</th>
                  <th className="text-right py-2 px-3 font-semibold text-slate-600 dark:text-slate-300">Variance</th>
                </tr>
              </thead>
              <tbody>
                {businessUnitForecasts.map((row) => (
                  <tr key={row.unit} className="border-b border-slate-100 dark:border-slate-800 hover:bg-slate-50 dark:hover:bg-slate-800/50">
                    <td className="py-2.5 px-3 font-medium text-slate-900 dark:text-white">{row.unit}</td>
                    <td className="text-right py-2.5 px-3 text-slate-700 dark:text-slate-300">{row.current}</td>
                    <td className="text-right py-2.5 px-3 font-semibold text-slate-900 dark:text-white">{row.forecast}</td>
                    <td className="text-right py-2.5 px-3">
                      <Badge variant={row.variance.startsWith('+') ? 'success' : 'destructive'} className="text-[10px]">
                        {row.variance}
                      </Badge>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </CardContent>
      </Card>

      <Card>
        <CardHeader>
          <CardTitle className="text-lg">Risk Factors</CardTitle>
          <CardDescription>Key risks affecting forecast accuracy</CardDescription>
        </CardHeader>
        <CardContent>
          <div className="space-y-3">
            {riskFactors.map((risk) => (
              <div key={risk.factor} className="flex items-center justify-between p-3 rounded-lg border border-slate-200 dark:border-slate-700 hover:bg-slate-50 dark:hover:bg-slate-800/50">
                <p className="text-sm font-medium text-slate-900 dark:text-white">{risk.factor}</p>
                <Badge variant={severityVariant[risk.severity]} className="text-[10px] capitalize">
                  {risk.severity}
                </Badge>
              </div>
            ))}
          </div>
        </CardContent>
      </Card>
    </div>
  )
}
