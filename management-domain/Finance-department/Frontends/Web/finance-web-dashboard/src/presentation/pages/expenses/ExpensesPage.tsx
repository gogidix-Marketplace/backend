// ============================================
// FINANCE DEPARTMENT - EXPENSES PAGE
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
  CheckCircle,
  XCircle,
  Clock,
  Receipt,
  Calendar,
  User,
  Building2,
} from 'lucide-react'

type ExpenseStatus = 'draft' | 'pending' | 'approved' | 'rejected' | 'paid' | 'reimbursed'

type ExpenseCategory = 'travel' | 'meals' | 'office_supplies' | 'software' | 'equipment' | 'training' | 'marketing' | 'utilities' | 'rent' | 'insurance' | 'other'

interface Expense {
  id: string
  title: string
  description: string
  category: ExpenseCategory
  amount: number
  currency: string
  submittedBy: string
  department: string
  date: string
  status: ExpenseStatus
  reimbursable: boolean
  receipt: boolean
}

const mockExpenses: Expense[] = [
  {
    id: '1',
    title: 'Client Meeting - London',
    description: 'Flight and accommodation for client meeting',
    category: 'travel',
    amount: 1250.00,
    currency: 'GBP',
    submittedBy: 'John Davis',
    department: 'Sales',
    date: '2025-03-10',
    status: 'pending',
    reimbursable: true,
    receipt: true,
  },
  {
    id: '2',
    title: 'Software License - Adobe CC',
    description: 'Annual subscription for design team',
    category: 'software',
    amount: 599.88,
    currency: 'USD',
    submittedBy: 'Amanda Peters',
    department: 'Digital Marketing',
    date: '2025-03-09',
    status: 'approved',
    reimbursable: false,
    receipt: true,
  },
  {
    id: '3',
    title: 'Team Lunch - Q1 Review',
    description: 'Lunch for team meeting',
    category: 'meals',
    amount: 245.50,
    currency: 'USD',
    submittedBy: 'Sarah Mitchell',
    department: 'Finance',
    date: '2025-03-08',
    status: 'paid',
    reimbursable: false,
    receipt: true,
  },
  {
    id: '4',
    title: 'Office Equipment - Monitors',
    description: 'New monitors for design team',
    category: 'equipment',
    amount: 1800.00,
    currency: 'USD',
    submittedBy: 'David Wilson',
    department: 'IT',
    date: '2025-03-07',
    status: 'pending',
    reimbursable: false,
    receipt: true,
  },
  {
    id: '5',
    title: 'Training Course - React Advanced',
    description: 'Online training course',
    category: 'training',
    amount: 299.00,
    currency: 'USD',
    submittedBy: 'Mike Chen',
    department: 'Engineering',
    date: '2025-03-06',
    status: 'approved',
    reimbursable: false,
    receipt: true,
  },
  {
    id: '6',
    title: 'Marketing Materials',
    description: 'Brochures and flyers for event',
    category: 'marketing',
    amount: 750.00,
    currency: 'USD',
    submittedBy: 'Lisa Brown',
    department: 'Digital Marketing',
    date: '2025-03-05',
    status: 'rejected',
    reimbursable: false,
    receipt: false,
  },
]

const statusColors: Record<ExpenseStatus, string> = {
  draft: 'bg-slate-100 text-slate-700',
  pending: 'bg-amber-100 text-amber-700',
  approved: 'bg-blue-100 text-blue-700',
  rejected: 'bg-red-100 text-red-700',
  paid: 'bg-emerald-100 text-emerald-700',
  reimbursed: 'bg-purple-100 text-purple-700',
}

const categoryLabels: Record<ExpenseCategory, string> = {
  travel: 'Travel',
  meals: 'Meals',
  office_supplies: 'Office Supplies',
  software: 'Software',
  equipment: 'Equipment',
  training: 'Training',
  marketing: 'Marketing',
  utilities: 'Utilities',
  rent: 'Rent',
  insurance: 'Insurance',
  other: 'Other',
}

export function ExpensesPage() {
  const { expensesLoading } = useFinanceStore()
  const [expenses, setExpenses] = useState<Expense[]>(mockExpenses)
  const [searchTerm, setSearchTerm] = useState('')
  const [statusFilter, setStatusFilter] = useState<ExpenseStatus | 'all'>('all')
  const [categoryFilter, setCategoryFilter] = useState<ExpenseCategory | 'all'>('all')
  const [showFilters, setShowFilters] = useState(false)

  const filteredExpenses = expenses.filter((expense) => {
    const matchesSearch =
      expense.title.toLowerCase().includes(searchTerm.toLowerCase()) ||
      expense.description.toLowerCase().includes(searchTerm.toLowerCase()) ||
      expense.submittedBy.toLowerCase().includes(searchTerm.toLowerCase())
    const matchesStatus = statusFilter === 'all' || expense.status === statusFilter
    const matchesCategory = categoryFilter === 'all' || expense.category === categoryFilter
    return matchesSearch && matchesStatus && matchesCategory
  })

  const totalAmount = filteredExpenses.reduce((sum, e) => sum + e.amount, 0)
  const pendingCount = filteredExpenses.filter((e) => e.status === 'pending').length

  return (
    <div className="space-y-6">
      {/* Page Header */}
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-2xl font-bold text-slate-900">Expenses</h1>
          <p className="text-slate-500">Manage and track employee expenses</p>
        </div>
        <button className="flex items-center gap-2 px-4 py-2 bg-blue-600 hover:bg-blue-700 text-white rounded-lg font-medium transition-colors">
          <Plus size={20} />
          New Expense
        </button>
      </div>

      {/* Summary Cards */}
      <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
        <div className="bg-white rounded-xl border border-slate-200 p-4">
          <p className="text-sm text-slate-500">Total Expenses</p>
          <p className="text-xl font-bold text-slate-900">
            {formatCurrency(totalAmount, 'USD')}
          </p>
        </div>
        <div className="bg-white rounded-xl border border-slate-200 p-4">
          <p className="text-sm text-slate-500">Pending Approval</p>
          <p className="text-xl font-bold text-amber-600">{pendingCount}</p>
        </div>
        <div className="bg-white rounded-xl border border-slate-200 p-4">
          <p className="text-sm text-slate-500">This Month</p>
          <p className="text-xl font-bold text-slate-900">
            {formatCurrency(
              expenses.filter((e) => {
                const date = new Date(e.date)
                const now = new Date()
                return date.getMonth() === now.getMonth() && date.getFullYear() === now.getFullYear()
              }).reduce((sum, e) => sum + e.amount, 0),
              'USD'
            )}
          </p>
        </div>
        <div className="bg-white rounded-xl border border-slate-200 p-4">
          <p className="text-sm text-slate-500">With Receipts</p>
          <p className="text-xl font-bold text-emerald-600">
            {expenses.filter((e) => e.receipt).length}
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
              placeholder="Search expenses by title, description, or submitter..."
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
                onChange={(e) => setStatusFilter(e.target.value as ExpenseStatus | 'all')}
                className="px-3 py-2 border border-slate-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
              >
                <option value="all">All Statuses</option>
                <option value="draft">Draft</option>
                <option value="pending">Pending</option>
                <option value="approved">Approved</option>
                <option value="rejected">Rejected</option>
                <option value="paid">Paid</option>
                <option value="reimbursed">Reimbursed</option>
              </select>
            </div>
            <div>
              <label className="block text-sm font-medium text-slate-700 mb-1">Category</label>
              <select
                value={categoryFilter}
                onChange={(e) => setCategoryFilter(e.target.value as ExpenseCategory | 'all')}
                className="px-3 py-2 border border-slate-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
              >
                <option value="all">All Categories</option>
                {Object.entries(categoryLabels).map(([value, label]) => (
                  <option key={value} value={value}>{label}</option>
                ))}
              </select>
            </div>
          </div>
        )}
      </div>

      {/* Expenses Table */}
      <div className="bg-white rounded-xl border border-slate-200 overflow-hidden">
        <div className="overflow-x-auto">
          <table className="w-full">
            <thead className="bg-slate-50 border-b border-slate-200">
              <tr>
                <th className="px-6 py-3 text-left text-xs font-semibold text-slate-600 uppercase">Expense</th>
                <th className="px-6 py-3 text-left text-xs font-semibold text-slate-600 uppercase">Category</th>
                <th className="px-6 py-3 text-left text-xs font-semibold text-slate-600 uppercase">Submitted By</th>
                <th className="px-6 py-3 text-left text-xs font-semibold text-slate-600 uppercase">Department</th>
                <th className="px-6 py-3 text-left text-xs font-semibold text-slate-600 uppercase">Date</th>
                <th className="px-6 py-3 text-right text-xs font-semibold text-slate-600 uppercase">Amount</th>
                <th className="px-6 py-3 text-center text-xs font-semibold text-slate-600 uppercase">Status</th>
                <th className="px-6 py-3 text-center text-xs font-semibold text-slate-600 uppercase">Actions</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-slate-200">
              {filteredExpenses.map((expense) => (
                <tr key={expense.id} className="hover:bg-slate-50">
                  <td className="px-6 py-4">
                    <div className="flex items-start gap-3">
                      <div className={cn(
                        'p-2 rounded-lg',
                        expense.receipt ? 'bg-emerald-100' : 'bg-slate-100'
                      )}>
                        <Receipt size={18} className={cn(
                          expense.receipt ? 'text-emerald-600' : 'text-slate-400'
                        )} />
                      </div>
                      <div>
                        <p className="font-medium text-slate-900">{expense.title}</p>
                        <p className="text-sm text-slate-500 line-clamp-1">{expense.description}</p>
                      </div>
                    </div>
                  </td>
                  <td className="px-6 py-4">
                    <span className="px-2 py-1 bg-slate-100 text-slate-700 rounded text-sm">
                      {categoryLabels[expense.category]}
                    </span>
                  </td>
                  <td className="px-6 py-4">
                    <div className="flex items-center gap-2">
                      <User size={16} className="text-slate-400" />
                      <span className="text-slate-600">{expense.submittedBy}</span>
                    </div>
                  </td>
                  <td className="px-6 py-4">
                    <div className="flex items-center gap-2">
                      <Building2 size={16} className="text-slate-400" />
                      <span className="text-slate-600">{expense.department}</span>
                    </div>
                  </td>
                  <td className="px-6 py-4 text-slate-600">
                    <div className="flex items-center gap-2">
                      <Calendar size={16} className="text-slate-400" />
                      {formatDate(expense.date)}
                    </div>
                  </td>
                  <td className="px-6 py-4 text-right font-medium text-slate-900">
                    {formatCurrency(expense.amount, expense.currency)}
                  </td>
                  <td className="px-6 py-4">
                    <span className={cn('px-2 py-1 rounded-full text-xs font-medium', statusColors[expense.status])}>
                      {expense.status.charAt(0).toUpperCase() + expense.status.slice(1)}
                    </span>
                    {expense.reimbursable && expense.status === 'approved' && (
                      <span className="ml-1 px-2 py-1 rounded-full text-xs font-medium bg-purple-100 text-purple-700">
                        Reimbursable
                      </span>
                    )}
                  </td>
                  <td className="px-6 py-4">
                    <div className="flex items-center justify-center gap-2">
                      <button className="p-1.5 hover:bg-slate-100 rounded-lg text-slate-500 hover:text-slate-700">
                        <Eye size={18} />
                      </button>
                      {expense.status === 'pending' && (
                        <>
                          <button className="p-1.5 hover:bg-emerald-50 rounded-lg text-emerald-600">
                            <CheckCircle size={18} />
                          </button>
                          <button className="p-1.5 hover:bg-red-50 rounded-lg text-red-600">
                            <XCircle size={18} />
                          </button>
                        </>
                      )}
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>

        {filteredExpenses.length === 0 && (
          <div className="text-center py-12">
            <p className="text-slate-500">No expenses found matching your criteria.</p>
          </div>
        )}
      </div>
    </div>
  )
}
