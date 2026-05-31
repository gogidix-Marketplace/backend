// ============================================
// FINANCE PORTAL - EXPENSE SUBMIT PAGE
// ============================================

import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { useAuthStore, usePortalStore } from '@shared/store/portalStore'
import { useDropzone } from 'react-dropzone'
import { Receipt, Upload, X, Check, AlertCircle } from 'lucide-react'
import type { ExpenseCategory } from '@shared/store/portalStore'

const CATEGORIES: { value: ExpenseCategory; label: string; icon: string }[] = [
  { value: 'travel', label: 'Travel', icon: '✈️' },
  { value: 'meals', label: 'Meals', icon: '🍽️' },
  { value: 'office_supplies', label: 'Office Supplies', icon: '📦' },
  { value: 'software', label: 'Software', icon: '💻' },
  { value: 'equipment', label: 'Equipment', icon: '🖥️' },
  { value: 'training', label: 'Training', icon: '📚' },
  { value: 'marketing', label: 'Marketing', icon: '📢' },
  { value: 'other', label: 'Other', icon: '📄' },
]

export function ExpenseSubmitPage() {
  const navigate = useNavigate()
  const { employee } = useAuthStore()
  const { submitExpense, loading } = usePortalStore()

  const [title, setTitle] = useState('')
  const [description, setDescription] = useState('')
  const [category, setCategory] = useState<ExpenseCategory>('other')
  const [amount, setAmount] = useState('')
  const [date, setDate] = useState(new Date().toISOString().split('T')[0])
  const [projectCode, setProjectCode] = useState('')
  const [reimbursable, setReimbursable] = useState(false)
  const [receipt, setReceipt] = useState<File | null>(null)
  const [error, setError] = useState('')
  const [success, setSuccess] = useState(false)

  const { getRootProps, getInputProps, isDragActive } = useDropzone({
    accept: { 'image/*': ['.png', '.jpg', '.jpeg'], 'application/pdf': ['.pdf'] },
    maxFiles: 1,
    onDrop: (acceptedFiles) => {
      if (acceptedFiles[0]) {
        setReceipt(acceptedFiles[0])
        setError('')
      }
    },
  })

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    setError('')

    if (!title.trim()) {
      setError('Please enter a title')
      return
    }
    if (!amount || parseFloat(amount) <= 0) {
      setError('Please enter a valid amount')
      return
    }
    if (!receipt && category === 'travel') {
      setError('Receipt is required for travel expenses')
      return
    }

    try {
      await submitExpense({
        title: title.trim(),
        description: description.trim(),
        category,
        amount: parseFloat(amount),
        currency: employee?.country === 'GB' ? 'GBP' : employee?.country === 'NG' ? 'NGN' : 'USD',
        date,
        projectCode: projectCode || undefined,
        reimbursable,
        status: 'pending',
      })

      setSuccess(true)
      setTimeout(() => {
        navigate('/my-expenses')
      }, 2000)
    } catch (err) {
      setError('Failed to submit expense. Please try again.')
    }
  }

  const removeReceipt = () => {
    setReceipt(null)
  }

  return (
    <div className="max-w-2xl mx-auto">
      <div className="mb-6">
        <h1 className="text-2xl font-bold text-slate-900">Submit Expense</h1>
        <p className="text-slate-500">Fill in the details to submit an expense for reimbursement</p>
      </div>

      {success && (
        <div className="mb-6 p-4 bg-emerald-50 border border-emerald-200 rounded-lg flex items-center gap-3">
          <Check size={20} className="text-emerald-600" />
          <p className="text-emerald-800">Expense submitted successfully! Redirecting...</p>
        </div>
      )}

      {error && (
        <div className="mb-6 p-4 bg-red-50 border border-red-200 rounded-lg flex items-center gap-3">
          <AlertCircle size={20} className="text-red-600" />
          <p className="text-red-800">{error}</p>
        </div>
      )}

      <form onSubmit={handleSubmit} className="bg-white rounded-xl border border-slate-200 p-6 space-y-6">
        {/* Title */}
        <div>
          <label className="block text-sm font-medium text-slate-700 mb-1">
            Title <span className="text-red-500">*</span>
          </label>
          <input
            type="text"
            value={title}
            onChange={(e) => setTitle(e.target.value)}
            placeholder="e.g., Client Meeting - London"
            className="w-full px-3 py-2 border border-slate-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
          />
        </div>

        {/* Description */}
        <div>
          <label className="block text-sm font-medium text-slate-700 mb-1">Description</label>
          <textarea
            value={description}
            onChange={(e) => setDescription(e.target.value)}
            placeholder="Provide details about this expense..."
            rows={3}
            className="w-full px-3 py-2 border border-slate-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 resize-none"
          />
        </div>

        {/* Category */}
        <div>
          <label className="block text-sm font-medium text-slate-700 mb-1">
            Category <span className="text-red-500">*</span>
          </label>
          <select
            value={category}
            onChange={(e) => setCategory(e.target.value as ExpenseCategory)}
            className="w-full px-3 py-2 border border-slate-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
          >
            {CATEGORIES.map((cat) => (
              <option key={cat.value} value={cat.value}>
                {cat.icon} {cat.label}
              </option>
            ))}
          </select>
        </div>

        {/* Amount and Date */}
        <div className="grid grid-cols-2 gap-4">
          <div>
            <label className="block text-sm font-medium text-slate-700 mb-1">
              Amount <span className="text-red-500">*</span>
            </label>
            <div className="relative">
              <span className="absolute left-3 top-1/2 -translate-y-1/2 text-slate-500">
                {employee?.country === 'GB' ? '£' : employee?.country === 'NG' ? '₦' : '$'}
              </span>
              <input
                type="number"
                value={amount}
                onChange={(e) => setAmount(e.target.value)}
                placeholder="0.00"
                step="0.01"
                min="0"
                className="w-full pl-8 pr-3 py-2 border border-slate-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
              />
            </div>
          </div>
          <div>
            <label className="block text-sm font-medium text-slate-700 mb-1">
              Date <span className="text-red-500">*</span>
            </label>
            <input
              type="date"
              value={date}
              onChange={(e) => setDate(e.target.value)}
              max={new Date().toISOString().split('T')[0]}
              className="w-full px-3 py-2 border border-slate-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
            />
          </div>
        </div>

        {/* Project Code */}
        <div>
          <label className="block text-sm font-medium text-slate-700 mb-1">Project Code (Optional)</label>
          <input
            type="text"
            value={projectCode}
            onChange={(e) => setProjectCode(e.target.value)}
            placeholder="e.g., PRJ-001"
            className="w-full px-3 py-2 border border-slate-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
          />
        </div>

        {/* Receipt Upload */}
        <div>
          <label className="block text-sm font-medium text-slate-700 mb-1">
            Receipt {category === 'travel' && <span className="text-red-500">*</span>}
          </label>
          {receipt ? (
            <div className="flex items-center justify-between p-3 bg-slate-50 border border-slate-200 rounded-lg">
              <div className="flex items-center gap-2">
                <Receipt size={18} className="text-slate-500" />
                <span className="text-sm text-slate-700 truncate max-w-xs">{receipt.name}</span>
                <span className="text-xs text-slate-500">
                  {(receipt.size / 1024).toFixed(1)} KB
                </span>
              </div>
              <button
                type="button"
                onClick={removeReceipt}
                className="p-1 hover:bg-slate-200 rounded text-slate-500 hover:text-slate-700"
              >
                <X size={18} />
              </button>
            </div>
          ) : (
            <div
              {...getRootProps()}
              className={`border-2 border-dashed rounded-lg p-6 text-center cursor-pointer transition-colors ${
                isDragActive
                  ? 'border-blue-500 bg-blue-50'
                  : 'border-slate-300 hover:border-slate-400'
              }`}
            >
              <input {...getInputProps()} />
              <Upload size={32} className="mx-auto text-slate-400 mb-2" />
              <p className="text-sm text-slate-600">
                {isDragActive
                  ? 'Drop the receipt here'
                  : 'Drag & drop a receipt, or click to select'}
              </p>
              <p className="text-xs text-slate-500 mt-1">PNG, JPG, or PDF up to 10MB</p>
            </div>
          )}
        </div>

        {/* Reimbursable */}
        <label className="flex items-center gap-3 cursor-pointer">
          <input
            type="checkbox"
            checked={reimbursable}
            onChange={(e) => setReimbursable(e.target.checked)}
            className="w-5 h-5 text-blue-600 rounded"
          />
          <span className="text-slate-700">This expense is reimbursable</span>
        </label>

        {/* Submit */}
        <div className="flex gap-3 pt-4">
          <button
            type="button"
            onClick={() => navigate('/my-expenses')}
            className="flex-1 py-2.5 border border-slate-300 rounded-lg font-medium text-slate-700 hover:bg-slate-50"
          >
            Cancel
          </button>
          <button
            type="submit"
            disabled={loading}
            className={`flex-1 py-2.5 rounded-lg font-medium text-white transition-colors ${
              loading
                ? 'bg-slate-300 cursor-not-allowed'
                : 'bg-blue-600 hover:bg-blue-700'
            }`}
          >
            {loading ? 'Submitting...' : 'Submit Expense'}
          </button>
        </div>
      </form>
    </div>
  )
}
