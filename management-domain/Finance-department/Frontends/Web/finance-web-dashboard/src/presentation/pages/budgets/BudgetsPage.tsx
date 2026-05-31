// ============================================
// FINANCE DEPARTMENT - BUDGETS PAGE
// ============================================

import { useState, useEffect } from 'react'
import { useFinanceStore } from '@store'
import { formatCurrency, formatDate, cn } from '@shared/utils/cn'
import {
  Plus,
  Search,
  Filter,
  Download,
  Eye,
  Edit,
  Trash2,
  CheckCircle,
  XCircle,
  Clock,
  TrendingUp,
  TrendingDown,
  ChevronDown,
} from 'lucide-react'

type BudgetStatus = 'draft' | 'pending' | 'approved' | 'active' | 'exceeded' | 'closed'

interface Budget {
  id: string
  name: string
  code: string
  department: string
  period: string
  allocated: number
  spent: number
  remaining: number
  variance: number
  status: BudgetStatus
  manager: string
  createdAt: string
}

const mockBudgets: Budget[] = [
  {
    id: '1',
    name: 'Marketing Operations Budget',
    code: 'MKT-2025-Q1',
    department: 'Digital Marketing',
    period: 'Q1 2025',
    allocated: 150000,
    spent: 98500,
    remaining: 51500,
    variance: 65.7,
    status: 'active',
    manager: 'Amanda Peters',
    createdAt: '2025-01-01T00:00:00Z',
  },
  {
    id: '2',
    name: 'Software Licenses',
    code: 'IT-2025-Q1',
    department: 'IT',
    period: 'Q1 2025',
    allocated: 75000,
    spent: 72000,
    remaining: 3000,
    variance: 96.0,
    status: 'exceeded',
    manager: 'David Wilson',
    createdAt: '2025-01-01T00:00:00Z',
  },
  {
    id: '3',
    name: 'Office Supplies',
    code: 'ADM-2025-Q1',
    department: 'Administration',
    period: 'Q1 2025',
    allocated: 25000,
    spent: 12500,
    remaining: 12500,
    variance: 50.0,
    status: 'active',
    manager: 'Sarah Mitchell',
    createdAt: '2025-01-01T00:00:00Z',
  },
  {
    id: '4',
    name: 'Travel Expenses',
    code: 'SL-2025-Q1',
    department: 'Sales',
    period: 'Q1 2025',
    allocated: 100000,
    spent: 45000,
    remaining: 55000,
    variance: 45.0,
    status: 'active',
    manager: 'John Davis',
    createdAt: '2025-01-01T00:00:00Z',
  },
]

const statusColors: Record<BudgetStatus, string> = {
  draft: 'bg-slate-100 text-slate-700',
  pending: 'bg-amber-100 text-amber-700',
  approved: 'bg-blue-100 text-blue-700',
  active: 'bg-emerald-100 text-emerald-700',
  exceeded: 'bg-red-100 text-red-700',
  closed: 'bg-slate-100 text-slate-500',
}

export function BudgetsPage() {
  const { budgetsLoading, setBudgetFilters } = useFinanceStore()
  const [budgets, setBudgets] = useState<Budget[]>(mockBudgets)
  const [searchTerm, setSearchTerm] = useState('')
  const [statusFilter, setStatusFilter] = useState<BudgetStatus | 'all'>('all')
  const [showFilters, setShowFilters] = useState(false)

  const filteredBudgets = budgets.filter((budget) => {
    const matchesSearch =
      budget.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
      budget.code.toLowerCase().includes(searchTerm.toLowerCase()) ||
      budget.department.toLowerCase().includes(searchTerm.toLowerCase())
    const matchesStatus = statusFilter === 'all' || budget.status === statusFilter
    return matchesSearch && matchesStatus
  })

  return (
    <div className="space-y-6">
      {/* Page Header */}
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-2xl font-bold text-slate-900">Budgets</h1>
          <p className="text-slate-500">Manage and monitor departmental budgets</p>
        </div>
        <button className="flex items-center gap-2 px-4 py-2 bg-blue-600 hover:bg-blue-700 text-white rounded-lg font-medium transition-colors">
          <Plus size={20} />
          Create Budget
        </button>
      </div>

      {/* Summary Cards */}
      <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
        <div className="bg-white rounded-xl border border-slate-200 p-4">
          <p className="text-sm text-slate-500">Total Allocated</p>
          <p className="text-xl font-bold text-slate-900">
            {formatCurrency(budgets.reduce((sum, b) => sum + b.allocated, 0), 'USD')}
          </p>
        </div>
        <div className="bg-white rounded-xl border border-slate-200 p-4">
          <p className="text-sm text-slate-500">Total Spent</p>
          <p className="text-xl font-bold text-slate-900">
            {formatCurrency(budgets.reduce((sum, b) => sum + b.spent, 0), 'USD')}
          </p>
        </div>
        <div className="bg-white rounded-xl border border-slate-200 p-4">
          <p className="text-sm text-slate-500">Remaining</p>
          <p className="text-xl font-bold text-emerald-600">
            {formatCurrency(budgets.reduce((sum, b) => sum + b.remaining, 0), 'USD')}
          </p>
        </div>
        <div className="bg-white rounded-xl border border-slate-200 p-4">
          <p className="text-sm text-slate-500">Over Budget</p>
          <p className="text-xl font-bold text-red-600">
            {budgets.filter((b) => b.status === 'exceeded').length}
          </p>
        </div>
      </div>

      {/* Filters */}
      <div className="bg-white rounded-xl border border-slate-200 p-4">
        <div className="flex flex-col md:flex-row gap-4">
          <div className="flex-1 relative">
            <Search size={20} className="absolute left-3 top-1/2 -translate-y-1/2 text-slate-400" />
            <input
              type="text"
              placeholder="Search budgets by name, code, or department..."
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              className="w-full pl-10 pr-4 py-2 border border-slate-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
            />
          </div>
          <div className="flex gap-2">
            <button
              onClick={() => setShowFilters(!showFilters)}
              className="flex items-center gap-2 px-4 py-2 border border-slate-300 rounded-lg hover:bg-slate-50 transition-colors"
            >
              <Filter size={20} />
              Filters
            </button>
            <button className="flex items-center gap-2 px-4 py-2 border border-slate-300 rounded-lg hover:bg-slate-50 transition-colors">
              <Download size={20} />
              Export
            </button>
          </div>
        </div>

        {showFilters && (
          <div className="mt-4 pt-4 border-t border-slate-200 flex flex-wrap gap-4">
            <div>
              <label className="block text-sm font-medium text-slate-700 mb-1">Status</label>
              <select
                value={statusFilter}
                onChange={(e) => setStatusFilter(e.target.value as BudgetStatus | 'all')}
                className="px-3 py-2 border border-slate-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
              >
                <option value="all">All Statuses</option>
                <option value="draft">Draft</option>
                <option value="pending">Pending</option>
                <option value="approved">Approved</option>
                <option value="active">Active</option>
                <option value="exceeded">Exceeded</option>
                <option value="closed">Closed</option>
              </select>
            </div>
          </div>
        )}
      </div>

      {/* Budgets Table */}
      <div className="bg-white rounded-xl border border-slate-200 overflow-hidden">
        <div className="overflow-x-auto">
          <table className="w-full">
            <thead className="bg-slate-50 border-b border-slate-200">
              <tr>
                <th className="px-6 py-3 text-left text-xs font-semibold text-slate-600 uppercase">Budget</th>
                <th className="px-6 py-3 text-left text-xs font-semibold text-slate-600 uppercase">Department</th>
                <th className="px-6 py-3 text-left text-xs font-semibold text-slate-600 uppercase">Period</th>
                <th className="px-6 py-3 text-right text-xs font-semibold text-slate-600 uppercase">Allocated</th>
                <th className="px-6 py-3 text-right text-xs font-semibold text-slate-600 uppercase">Spent</th>
                <th className="px-6 py-3 text-right text-xs font-semibold text-slate-600 uppercase">Remaining</th>
                <th className="px-6 py-3 text-center text-xs font-semibold text-slate-600 uppercase">Usage</th>
                <th className="px-6 py-3 text-center text-xs font-semibold text-slate-600 uppercase">Status</th>
                <th className="px-6 py-3 text-center text-xs font-semibold text-slate-600 uppercase">Actions</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-slate-200">
              {filteredBudgets.map((budget) => (
                <tr key={budget.id} className="hover:bg-slate-50">
                  <td className="px-6 py-4">
                    <div>
                      <p className="font-medium text-slate-900">{budget.name}</p>
                      <p className="text-sm text-slate-500">{budget.code}</p>
                    </div>
                  </td>
                  <td className="px-6 py-4 text-slate-600">{budget.department}</td>
                  <td className="px-6 py-4 text-slate-600">{budget.period}</td>
                  <td className="px-6 py-4 text-right font-medium text-slate-900">
                    {formatCurrency(budget.allocated, 'USD')}
                  </td>
                  <td className="px-6 py-4 text-right text-slate-600">
                    {formatCurrency(budget.spent, 'USD')}
                  </td>
                  <td className={cn(
                    'px-6 py-4 text-right font-medium',
                    budget.remaining < 0 ? 'text-red-600' : 'text-emerald-600'
                  )}>
                    {formatCurrency(budget.remaining, 'USD')}
                  </td>
                  <td className="px-6 py-4">
                    <div className="flex items-center gap-2">
                      <div className="flex-1 bg-slate-200 rounded-full h-2 max-w-24">
                        <div
                          className={cn(
                            'h-2 rounded-full',
                            budget.variance > 90 ? 'bg-red-500' :
                            budget.variance > 75 ? 'bg-amber-500' : 'bg-emerald-500'
                          )}
                          style={{ width: `${Math.min(budget.variance, 100)}%` }}
                        />
                      </div>
                      <span className="text-sm text-slate-600">{budget.variance.toFixed(0)}%</span>
                    </div>
                  </td>
                  <td className="px-6 py-4">
                    <span className={cn('px-2 py-1 rounded-full text-xs font-medium', statusColors[budget.status])}>
                      {budget.status.charAt(0).toUpperCase() + budget.status.slice(1)}
                    </span>
                  </td>
                  <td className="px-6 py-4">
                    <div className="flex items-center justify-center gap-2">
                      <button className="p-1.5 hover:bg-slate-100 rounded-lg text-slate-500 hover:text-slate-700">
                        <Eye size={18} />
                      </button>
                      <button className="p-1.5 hover:bg-slate-100 rounded-lg text-slate-500 hover:text-slate-700">
                        <Edit size={18} />
                      </button>
                      <button className="p-1.5 hover:bg-red-50 rounded-lg text-slate-500 hover:text-red-600">
                        <Trash2 size={18} />
                      </button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>

        {filteredBudgets.length === 0 && (
          <div className="text-center py-12">
            <p className="text-slate-500">No budgets found matching your criteria.</p>
          </div>
        )}
      </div>
    </div>
  )
}
