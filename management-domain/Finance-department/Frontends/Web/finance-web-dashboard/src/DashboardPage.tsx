import { useState } from 'react'

interface KPI {
  title: string
  value: string
  change: string
  positive: boolean
}

interface Transaction {
  description: string
  amount: string
  date: string
  status: string
}

const kpis: KPI[] = [
  { title: 'Total Revenue', value: '$1,284,500', change: '+8.2%', positive: true },
  { title: 'Total Expenses', value: '$843,200', change: '+3.1%', positive: false },
  { title: 'Net Income', value: '$441,300', change: '+12.4%', positive: true },
  { title: 'Cash Balance', value: '$215,800', change: '+5.1%', positive: true }
]

const transactions: Transaction[] = [
  { description: 'Client Payment - TechCorp Inc.', amount: '+$45,000', date: 'Jan 15, 2025', status: 'Paid' },
  { description: 'Office Supplies - Amazon', amount: '-$2,350', date: 'Jan 14, 2025', status: 'Pending' },
  { description: 'Consulting Fee - Delta Corp', amount: '+$12,500', date: 'Jan 13, 2025', status: 'Paid' }
]

export default function DashboardPage() {
  const [activeTab, setActiveTab] = useState<'overview' | 'transactions'>('overview')

  return (
    <div className="min-h-screen bg-gradient-to-br from-slate-900 via-slate-800 to-slate-900 p-6">
      <div className="max-w-7xl mx-auto">
        {/* Header */}
        <header className="mb-8">
          <h1 className="text-3xl font-bold text-white">Finance Dashboard</h1>
          <p className="text-slate-400">Gogidix Finance Management System - Port 3020</p>
        </header>

        {/* KPI Cards */}
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4 mb-8">
          {kpis.map((kpi, i) => (
            <div key={i} className={`p-6 rounded-xl border backdrop-blur-sm ${
              kpi.positive ? 'bg-emerald-500/10 border-emerald-500/30' : 'bg-red-500/10 border-red-500/30'
            }`}>
              <div className="text-sm text-slate-400 mb-1">{kpi.title}</div>
              <div className="text-2xl font-bold text-white mb-1">{kpi.value}</div>
              <div className={`text-sm ${kpi.positive ? 'text-emerald-400' : 'text-red-400'}`}>
                {kpi.change} {kpi.positive ? '↑' : '↓'}
              </div>
            </div>
          ))}
        </div>

        {/* Tabs */}
        <div className="flex gap-2 mb-6">
          <button
            onClick={() => setActiveTab('overview')}
            className={`px-4 py-2 rounded-lg font-medium transition-colors ${
              activeTab === 'overview' ? 'bg-blue-600 text-white' : 'bg-slate-700 text-slate-300 hover:bg-slate-600'
            }`}
          >
            Overview
          </button>
          <button
            onClick={() => setActiveTab('transactions')}
            className={`px-4 py-2 rounded-lg font-medium transition-colors ${
              activeTab === 'transactions' ? 'bg-blue-600 text-white' : 'bg-slate-700 text-slate-300 hover:bg-slate-600'
            }`}
          >
            Transactions
          </button>
        </div>

        {/* Content */}
        {activeTab === 'overview' ? (
          <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
            <div className="bg-slate-800/50 backdrop-blur-sm rounded-xl p-6 border border-slate-700/50">
              <h2 className="text-xl font-bold text-white mb-4">Quick Actions</h2>
              <div className="space-y-3">
                <button className="w-full py-3 bg-blue-600 hover:bg-blue-700 text-white rounded-lg font-medium">
                  Create New Invoice
                </button>
                <button className="w-full py-3 bg-slate-700 hover:bg-slate-600 text-white rounded-lg font-medium">
                  Generate Report
                </button>
                <button className="w-full py-3 bg-slate-700 hover:bg-slate-600 text-white rounded-lg font-medium">
                  View Budget
                </button>
                <button className="w-full py-3 bg-slate-700 hover:bg-slate-600 text-white rounded-lg font-medium">
                  Currency Rates
                </button>
              </div>
            </div>

            <div className="bg-slate-800/50 backdrop-blur-sm rounded-xl p-6 border border-slate-700/50 md:col-span-2">
              <h2 className="text-xl font-bold text-white mb-4">Financial Summary</h2>
              <div className="space-y-3">
                <div className="flex justify-between items-center py-2 border-b border-slate-700">
                  <span className="text-slate-400">Total Revenue</span>
                  <span className="text-white font-medium">$1,284,500</span>
                </div>
                <div className="flex justify-between items-center py-2 border-b border-slate-700">
                  <span className="text-slate-400">Total Expenses</span>
                  <span className="text-white font-medium">$843,200</span>
                </div>
                <div className="flex justify-between items-center py-2 border-b border-slate-700">
                  <span className="text-slate-400">Net Income</span>
                  <span className="text-emerald-400 font-medium">$441,300</span>
                </div>
                <div className="flex justify-between items-center py-2">
                  <span className="text-slate-400">Cash Balance</span>
                  <span className="text-blue-400 font-medium">$215,800</span>
                </div>
              </div>
            </div>
          </div>
        ) : (
          <div className="bg-slate-800/50 backdrop-blur-sm rounded-xl p-6 border border-slate-700/50">
            <h2 className="text-xl font-bold text-white mb-4">Recent Transactions</h2>
            <div className="overflow-x-auto">
              <table className="w-full text-left">
                <thead>
                  <tr className="border-b border-slate-700">
                    <th className="p-3 text-left text-slate-400 font-medium">Description</th>
                    <th className="p-3 text-left text-slate-400 font-medium">Amount</th>
                    <th className="p-3 text-left text-slate-400 font-medium">Date</th>
                    <th className="p-3 text-left text-slate-400 font-medium">Status</th>
                  </tr>
                </thead>
                <tbody>
                  {transactions.map((tx, i) => (
                    <tr key={i} className="border-b border-slate-700">
                      <td className="p-3 text-white">{tx.description}</td>
                      <td className={`p-3 ${tx.amount.startsWith('+') ? 'text-emerald-400' : 'text-slate-300'}`}>
                        {tx.amount}
                      </td>
                      <td className="p-3 text-slate-400">{tx.date}</td>
                      <td className="p-3">
                        <span className={`px-2 py-1 rounded text-xs ${
                          tx.status === 'Paid' ? 'bg-emerald-500/20 text-emerald-400' :
                          tx.status === 'Pending' ? 'bg-yellow-500/20 text-yellow-400' :
                          'bg-slate-700/50 text-slate-400'
                        }`}>
                          {tx.status}
                        </span>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </div>
        )}

        {/* Status Footer */}
        <div className="mt-8 p-4 bg-emerald-500/10 border border-emerald-500/30 rounded-xl text-center">
          <p className="text-emerald-400 font-medium">● Finance Dashboard running on port 3020</p>
        </div>
      </div>
    </div>
  )
}
