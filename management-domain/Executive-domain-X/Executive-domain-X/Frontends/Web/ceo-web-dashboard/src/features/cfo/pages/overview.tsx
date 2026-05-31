import { useState } from 'react'
import { Card, CardContent, CardHeader, CardTitle, CardDescription } from '@shared/components/ui/card'
import { Badge } from '@shared/components/ui/badge'
import { Button } from '@shared/components/ui/button'

export default function CFOOverviewPage() {
  const [currency, setCurrency] = useState<'USD' | 'EUR'>('USD')
  const period = 'Q1 2026'

  const kpis = [
    { title: 'Total Revenue', value: '$58.6M', change: '+12.3%', positive: true, status: 'On Track' },
    { title: 'Revenue Growth', value: '+12.3%', change: '+2.1%', positive: true, status: 'Accelerating' },
    { title: 'Expenses vs Budget', value: '$35.8M', change: '-3.2%', positive: true, status: 'Under Budget' },
    { title: 'Profit Margin', value: '18.5%', change: '+1.2%', positive: true, status: 'Improving' },
    { title: 'Cash Flow', value: 'Positive', change: 'OK', positive: true, status: 'Healthy' },
  ]

  const plRows = [
    { unit: 'Haulage', revenue: '$12.8M', opex: '$9.2M', margin: '28%', status: 'strong' },
    { unit: 'E-Commerce', revenue: '$9.8M', opex: '$7.8M', margin: '20%', status: 'strong' },
    { unit: 'Courier', revenue: '$8.2M', opex: '$6.8M', margin: '17%', status: 'stable' },
    { unit: 'Warehousing', revenue: '$5.1M', opex: '$4.2M', margin: '18%', status: 'stable' },
    { unit: 'Ocean', revenue: '$4.2M', opex: '$3.7M', margin: '12%', status: 'watch' },
    { unit: 'Air', revenue: '$3.8M', opex: '$3.2M', margin: '16%', status: 'stable' },
    { unit: 'Procurement', revenue: '$3.2M', opex: '$0.8M', margin: '75%*', status: 'strong' },
    { unit: 'Admin', revenue: '$1.5M', opex: '$1.1M', margin: '27%', status: 'stable' },
  ]

  const revenueMix = [
    { unit: 'Haulage', qoq: '+4.2%', positive: true },
    { unit: 'E-Commerce', qoq: '+6.8%', positive: true },
    { unit: 'Courier', qoq: '+1.5%', positive: true },
    { unit: 'Ocean', qoq: '-2.1%', positive: false },
    { unit: 'Air', qoq: '+3.0%', positive: true },
    { unit: 'Warehousing', qoq: '+2.4%', positive: true },
    { unit: 'Procurement', qoq: '+1.1%', positive: true },
    { unit: 'Admin', qoq: '-0.3%', positive: false },
  ]

  const settlements = [
    { unit: 'Haulage', amount: '$1.2M', pending: 8, status: 'In Review' },
    { unit: 'E-Commerce', amount: '$890K', pending: 12, status: 'Processing' },
    { unit: 'Courier', amount: '$650K', pending: 5, status: 'Approved' },
    { unit: 'Ocean', amount: '$420K', pending: 3, status: 'Pending' },
    { unit: 'Air', amount: '$310K', pending: 6, status: 'In Review' },
    { unit: 'Warehousing', amount: '$280K', pending: 4, status: 'Processing' },
  ]

  const approvals = [
    { id: 'FA-001', title: 'Q2 Capital Expenditure Request', dept: 'Haulage', deptColor: 'bg-blue-100 text-blue-700', amount: '$2.4M', priority: 'high' },
    { id: 'FA-002', title: 'Vendor Contract Renewal - DHL', dept: 'Courier', deptColor: 'bg-emerald-100 text-emerald-700', amount: '$1.8M', priority: 'medium' },
    { id: 'FA-003', title: 'Warehouse Expansion Budget', dept: 'Warehousing', deptColor: 'bg-purple-100 text-purple-700', amount: '$3.1M', priority: 'high' },
    { id: 'FA-004', title: 'Annual Software Licensing', dept: 'Technology', deptColor: 'bg-amber-100 text-amber-700', amount: '$450K', priority: 'low' },
    { id: 'FA-005', title: 'Insurance Premium Adjustment', dept: 'Admin', deptColor: 'bg-slate-100 text-slate-700', amount: '$180K', priority: 'medium' },
  ]

  return (
    <div className="space-y-5">
      <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
        <div>
          <h1 className="text-2xl sm:text-3xl font-bold text-slate-900 dark:text-white">Financial Overview</h1>
          <p className="text-sm text-slate-500 dark:text-slate-400 mt-1">Real-time financial health and budget performance</p>
        </div>
        <div className="flex items-center gap-2">
          <div className="flex rounded-md border overflow-hidden">
            <button onClick={() => setCurrency('USD')} className={`px-3 py-1.5 text-xs font-medium ${currency === 'USD' ? 'bg-[#0D47A1] text-white' : 'bg-white text-slate-600 hover:bg-slate-50'}`}>USD</button>
            <button onClick={() => setCurrency('EUR')} className={`px-3 py-1.5 text-xs font-medium border-l ${currency === 'EUR' ? 'bg-[#0D47A1] text-white' : 'bg-white text-slate-600 hover:bg-slate-50'}`}>EUR</button>
          </div>
          <Badge variant="outline" className="px-3 py-1.5 text-xs font-medium">{period}</Badge>
        </div>
      </div>

      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-5 gap-4">
        {kpis.map((kpi) => (
          <Card key={kpi.title} className="relative overflow-hidden">
            <CardContent className="p-4">
              <p className="text-xs font-medium text-slate-500 dark:text-slate-400 mb-1">{kpi.title}</p>
              <p className="text-xl font-bold text-slate-900 dark:text-white">{kpi.value}</p>
              <div className="flex items-center gap-2 mt-2">
                <span className={`inline-flex items-center text-xs font-semibold ${kpi.positive ? 'text-emerald-600' : 'text-red-600'}`}>
                  {kpi.positive ? (
                    <svg className="w-3 h-3 mr-0.5" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M5 10l7-7m0 0l7 7m-7-7v18" /></svg>
                  ) : (
                    <svg className="w-3 h-3 mr-0.5" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M19 14l-7 7m0 0l-7-7m7 7V3" /></svg>
                  )}
                  {kpi.change}
                </span>
                <Badge variant="secondary" className="text-[10px]">{kpi.status}</Badge>
              </div>
            </CardContent>
          </Card>
        ))}
      </div>

      <Card>
        <CardHeader>
          <CardTitle className="text-lg">P&L by Business Unit</CardTitle>
          <CardDescription>Revenue, OpEx, and margin breakdown for {period}</CardDescription>
        </CardHeader>
        <CardContent>
          <div className="overflow-x-auto">
            <table className="w-full text-sm">
              <thead>
                <tr className="border-b border-slate-200 dark:border-slate-700">
                  <th className="text-left py-2 px-3 font-semibold text-slate-600 dark:text-slate-300">Business Unit</th>
                  <th className="text-right py-2 px-3 font-semibold text-slate-600 dark:text-slate-300">Revenue</th>
                  <th className="text-right py-2 px-3 font-semibold text-slate-600 dark:text-slate-300">OpEx</th>
                  <th className="text-right py-2 px-3 font-semibold text-slate-600 dark:text-slate-300">Margin</th>
                  <th className="text-center py-2 px-3 font-semibold text-slate-600 dark:text-slate-300">Status</th>
                </tr>
              </thead>
              <tbody>
                {plRows.map((row) => (
                  <tr key={row.unit} className="border-b border-slate-100 dark:border-slate-800 hover:bg-slate-50 dark:hover:bg-slate-800/50">
                    <td className="py-2.5 px-3 font-medium text-slate-900 dark:text-white">{row.unit}</td>
                    <td className="text-right py-2.5 px-3 text-slate-700 dark:text-slate-300">{row.revenue}</td>
                    <td className="text-right py-2.5 px-3 text-slate-700 dark:text-slate-300">{row.opex}</td>
                    <td className="text-right py-2.5 px-3 font-semibold text-slate-900 dark:text-white">{row.margin}</td>
                    <td className="text-center py-2.5 px-3">
                      <Badge variant={row.status === 'strong' ? 'success' : row.status === 'watch' ? 'warning' : 'secondary'} className="text-[10px]">
                        {row.status === 'strong' ? 'Strong' : row.status === 'watch' ? 'Watch' : 'Stable'}
                      </Badge>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </CardContent>
      </Card>

      <div className="grid gap-4 sm:gap-5 lg:grid-cols-2">
        <Card>
          <CardHeader>
            <CardTitle className="text-lg">Revenue Mix Change</CardTitle>
            <CardDescription>Quarter-over-quarter revenue movement</CardDescription>
          </CardHeader>
          <CardContent>
            <div className="space-y-3">
              {revenueMix.map((item) => (
                <div key={item.unit} className="flex items-center gap-3">
                  <span className="text-sm font-medium text-slate-700 dark:text-slate-300 w-28 flex-shrink-0">{item.unit}</span>
                  <div className="flex-1 h-6 bg-slate-100 dark:bg-slate-800 rounded overflow-hidden relative">
                    <div
                      className={`h-full rounded ${item.positive ? 'bg-emerald-500' : 'bg-red-400'}`}
                      style={{ width: `${Math.min(Math.abs(parseFloat(item.qoq)) * 10, 100)}%` }}
                    />
                  </div>
                  <span className={`text-sm font-semibold w-14 text-right ${item.positive ? 'text-emerald-600' : 'text-red-500'}`}>{item.qoq}</span>
                </div>
              ))}
            </div>
          </CardContent>
        </Card>

        <Card>
          <CardHeader>
            <CardTitle className="text-lg">Settlement Pipeline</CardTitle>
            <CardDescription>Pending settlements per business unit</CardDescription>
          </CardHeader>
          <CardContent>
            <div className="space-y-3">
              {settlements.map((s) => (
                <div key={s.unit} className="flex items-center justify-between p-3 rounded-lg border border-slate-200 dark:border-slate-700 hover:bg-slate-50 dark:hover:bg-slate-800/50">
                  <div>
                    <p className="text-sm font-medium text-slate-900 dark:text-white">{s.unit}</p>
                    <p className="text-xs text-slate-500">{s.pending} pending transactions</p>
                  </div>
                  <div className="text-right">
                    <p className="text-sm font-semibold text-slate-900 dark:text-white">{s.amount}</p>
                    <Badge variant={s.status === 'Approved' ? 'success' : s.status === 'Processing' ? 'info' : s.status === 'In Review' ? 'warning' : 'secondary'} className="text-[10px]">
                      {s.status}
                    </Badge>
                  </div>
                </div>
              ))}
            </div>
          </CardContent>
        </Card>
      </div>

      <Card>
        <CardHeader>
          <div className="flex items-center justify-between">
            <div>
              <CardTitle className="text-lg">Pending Financial Approvals</CardTitle>
              <CardDescription>Items requiring CFO review</CardDescription>
            </div>
            <Badge variant="destructive">{approvals.length}</Badge>
          </div>
        </CardHeader>
        <CardContent>
          <div className="space-y-3">
            {approvals.map((item) => (
              <div key={item.id} className="flex items-center justify-between p-3 border border-slate-200 dark:border-slate-700 rounded-lg hover:bg-slate-50 dark:hover:bg-slate-800/50">
                <div className="flex items-center gap-3 flex-1 min-w-0">
                  <span className="text-xs text-slate-400 font-mono flex-shrink-0">{item.id}</span>
                  <div className="min-w-0">
                    <p className="text-sm font-medium text-slate-900 dark:text-white truncate">{item.title}</p>
                    <div className="flex items-center gap-2 mt-0.5">
                      <Badge className={`text-[10px] ${item.deptColor}`}>{item.dept}</Badge>
                      <Badge variant={item.priority === 'high' ? 'destructive' : item.priority === 'medium' ? 'warning' : 'secondary'} className="text-[10px]">{item.priority}</Badge>
                    </div>
                  </div>
                </div>
                <div className="flex items-center gap-3 flex-shrink-0 ml-4">
                  <span className="text-sm font-semibold text-slate-900 dark:text-white">{item.amount}</span>
                  <Button variant="outline" size="sm">Review</Button>
                </div>
              </div>
            ))}
          </div>
          <Button variant="outline" className="w-full mt-4">View All Approvals</Button>
        </CardContent>
      </Card>
    </div>
  )
}
