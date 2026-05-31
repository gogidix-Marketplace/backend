import * as React from 'react'
import { DollarSign, TrendingUp, TrendingDown, Minus } from 'lucide-react'
import { cn } from '@lib/utils'
import { Card } from '@/components/ui/card'

export interface BudgetCardProps {
  name: string
  allocated: number
  spent: number
  remaining: number
  currency?: string
  period?: string
  className?: string
  onClick?: () => void
}

export function BudgetCard({
  name,
  allocated,
  spent,
  remaining,
  currency = '$',
  period,
  className,
  onClick,
}: BudgetCardProps) {
  const percentage = Math.round((spent / allocated) * 100)
  const isOverBudget = spent > allocated

  const getStatusColor = () => {
    if (isOverBudget) return 'text-red-600 bg-red-50'
    if (percentage >= 90) return 'text-orange-600 bg-orange-50'
    if (percentage >= 75) return 'text-yellow-600 bg-yellow-50'
    return 'text-green-600 bg-green-50'
  }

  const getTrendIcon = () => {
    if (isOverBudget) return <TrendingUp className="h-4 w-4" />
    if (percentage >= 90) return <TrendingUp className="h-4 w-4" />
    if (percentage >= 75) return <Minus className="h-4 w-4" />
    return <TrendingDown className="h-4 w-4" />
  }

  return (
    <Card
      className={cn('p-4 cursor-pointer hover:shadow-md transition-shadow', className)}
      onClick={onClick}
    >
      <div className="flex items-start justify-between mb-4">
        <div>
          <h3 className="font-semibold text-gray-900">{name}</h3>
          {period && <p className="text-sm text-gray-500">{period}</p>}
        </div>
        <div className={cn('p-2 rounded-full', getStatusColor())}>
          <DollarSign className="h-4 w-4" />
        </div>
      </div>

      {/* Budget Overview */}
      <div className="grid grid-cols-3 gap-4 mb-4">
        <div>
          <p className="text-xs text-gray-500">Allocated</p>
          <p className="text-lg font-semibold text-gray-900">
            {currency}
            {allocated.toLocaleString()}
          </p>
        </div>
        <div>
          <p className="text-xs text-gray-500">Spent</p>
          <p className={cn(
            'text-lg font-semibold',
            isOverBudget ? 'text-red-600' : 'text-gray-900'
          )}>
            {currency}
            {spent.toLocaleString()}
          </p>
        </div>
        <div>
          <p className="text-xs text-gray-500">Remaining</p>
          <p className={cn(
            'text-lg font-semibold',
            remaining < 0 ? 'text-red-600' : 'text-gray-900'
          )}>
            {currency}
            {remaining.toLocaleString()}
          </p>
        </div>
      </div>

      {/* Progress Bar */}
      <div className="space-y-2">
        <div className="flex items-center justify-between text-sm">
          <span className="text-gray-600">{percentage}% used</span>
          <div className={cn('flex items-center gap-1', getStatusColor())}>
            {getTrendIcon()}
            <span className="text-xs font-medium">
              {isOverBudget ? 'Over budget' : percentage >= 90 ? 'Critical' : percentage >= 75 ? 'Warning' : 'On track'}
            </span>
          </div>
        </div>
        <div className="w-full bg-gray-200 rounded-full h-2">
          <div
            className={cn(
              'h-2 rounded-full transition-all',
              isOverBudget ? 'bg-red-500' : percentage >= 90 ? 'bg-orange-500' : percentage >= 75 ? 'bg-yellow-500' : 'bg-green-500'
            )}
            style={{ width: `${Math.min(percentage, 100)}%` }}
          />
        </div>
      </div>
    </Card>
  )
}

// Currency Selector Component
export interface CurrencySelectorProps {
  value?: string
  onChange?: (currency: string) => void
  disabled?: boolean
  className?: string
}

const CURRENCIES = [
  { code: 'USD', symbol: '$', name: 'US Dollar' },
  { code: 'EUR', symbol: '€', name: 'Euro' },
  { code: 'GBP', symbol: '£', name: 'British Pound' },
  { code: 'NGN', symbol: '₦', name: 'Nigerian Naira' },
  { code: 'KES', symbol: 'KSh', name: 'Kenyan Shilling' },
  { code: 'GHS', symbol: 'GH₵', name: 'Ghanaian Cedi' },
  { code: 'ZAR', symbol: 'R', name: 'South African Rand' },
]

export function CurrencySelector({
  value = 'USD',
  onChange,
  disabled = false,
  className,
}: CurrencySelectorProps) {
  return (
    <select
      value={value}
      onChange={(e) => onChange?.(e.target.value)}
      disabled={disabled}
      className={cn(
        'h-10 rounded-md border border-gray-300 bg-white px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-primary-500 disabled:opacity-50',
        className
      )}
    >
      {CURRENCIES.map((currency) => (
        <option key={currency.code} value={currency.code}>
          {currency.symbol} {currency.code} - {currency.name}
        </option>
      ))}
    </select>
  )
}

// Expense Table Item
export interface Expense {
  id: string
  category: string
  description: string
  amount: number
  currency: string
  date: string
  status: 'pending' | 'approved' | 'rejected'
  submittedBy: string
}

export interface ExpenseTableProps {
  expenses: Expense[]
  onApprove?: (id: string) => void
  onReject?: (id: string) => void
  className?: string
}

export function ExpenseTable({ expenses, onApprove, onReject, className }: ExpenseTableProps) {
  const getStatusColor = (status: string) => {
    switch (status) {
      case 'approved':
        return 'bg-green-100 text-green-700'
      case 'rejected':
        return 'bg-red-100 text-red-700'
      case 'pending':
      default:
        return 'bg-yellow-100 text-yellow-700'
    }
  }

  return (
    <div className={cn('overflow-x-auto', className)}>
      <table className="min-w-full divide-y divide-gray-200">
        <thead className="bg-gray-50">
          <tr>
            <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">Description</th>
            <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">Category</th>
            <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">Amount</th>
            <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">Date</th>
            <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">Submitted By</th>
            <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">Status</th>
            {(onApprove || onReject) && (
              <th className="px-4 py-3 text-right text-xs font-medium text-gray-500 uppercase">Actions</th>
            )}
          </tr>
        </thead>
        <tbody className="bg-white divide-y divide-gray-200">
          {expenses.map((expense) => (
            <tr key={expense.id}>
              <td className="px-4 py-3 text-sm text-gray-900">{expense.description}</td>
              <td className="px-4 py-3 text-sm text-gray-600">{expense.category}</td>
              <td className="px-4 py-3 text-sm font-medium text-gray-900">
                {expense.currency} {expense.amount.toLocaleString()}
              </td>
              <td className="px-4 py-3 text-sm text-gray-600">{expense.date}</td>
              <td className="px-4 py-3 text-sm text-gray-600">{expense.submittedBy}</td>
              <td className="px-4 py-3 text-sm">
                <span className={cn('px-2 py-1 rounded-full text-xs font-medium', getStatusColor(expense.status))}>
                  {expense.status}
                </span>
              </td>
              {(onApprove || onReject) && (
                <td className="px-4 py-3 text-right text-sm space-x-2">
                  {expense.status === 'pending' && (
                    <>
                      <button
                        onClick={() => onApprove?.(expense.id)}
                        className="text-green-600 hover:text-green-800 font-medium"
                      >
                        Approve
                      </button>
                      <button
                        onClick={() => onReject?.(expense.id)}
                        className="text-red-600 hover:text-red-800 font-medium"
                      >
                        Reject
                      </button>
                    </>
                  )}
                </td>
              )}
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  )
}
