import { useEffect, useState } from 'react'
import { useAccountantStore } from '@shared/store'
import { formatCurrency, formatDate, cn } from '@shared/utils/cn'
import { Users, Search, Loader2, Building2, Mail, Phone, MapPin, Calendar } from 'lucide-react'

export default function VendorsPage() {
  const { vendors, loading, loadVendors } = useAccountantStore()
  const [searchTerm, setSearchTerm] = useState('')
  const [selectedVendor, setSelectedVendor] = useState<string | null>(null)

  useEffect(() => { loadVendors() }, [])

  const filtered = vendors.filter((v) =>
    v.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
    v.category.toLowerCase().includes(searchTerm.toLowerCase()) ||
    v.email.toLowerCase().includes(searchTerm.toLowerCase())
  )

  const selected = vendors.find((v) => v.id === selectedVendor)

  if (loading.vendors) {
    return (
      <div className="flex items-center justify-center h-64">
        <Loader2 className="animate-spin text-teal-600" size={32} />
      </div>
    )
  }

  return (
    <div className="space-y-6">
      <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
        <div>
          <h2 className="text-xl font-bold text-gray-900">Vendor Management</h2>
          <p className="text-sm text-gray-500">Manage vendor relationships and payment information</p>
        </div>
        <div className="flex gap-2 text-sm">
          <span className="px-3 py-1.5 bg-green-50 text-green-700 rounded-lg font-medium">{vendors.filter((v) => v.status === 'active').length} Active</span>
          <span className="px-3 py-1.5 bg-gray-50 text-gray-600 rounded-lg font-medium">{vendors.filter((v) => v.status !== 'active').length} Inactive</span>
        </div>
      </div>

      <div className="relative">
        <Search size={16} className="absolute left-3 top-1/2 -translate-y-1/2 text-gray-400" />
        <input
          type="text"
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)}
          placeholder="Search vendors by name, category, or email..."
          className="w-full pl-9 pr-4 py-2 border border-gray-300 rounded-lg text-sm focus:ring-2 focus:ring-teal-500 focus:border-teal-500 outline-none"
        />
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        <div className="lg:col-span-2 grid grid-cols-1 sm:grid-cols-2 gap-4">
          {filtered.map((vendor) => (
            <button
              key={vendor.id}
              onClick={() => setSelectedVendor(vendor.id)}
              className={cn(
                'bg-white rounded-xl border p-5 text-left hover:shadow-md transition-all',
                selectedVendor === vendor.id ? 'border-teal-500 ring-1 ring-teal-500' : 'border-gray-200'
              )}
            >
              <div className="flex items-start justify-between mb-3">
                <div className="flex items-center gap-3">
                  <div className="w-10 h-10 bg-teal-50 rounded-lg flex items-center justify-center">
                    <Building2 size={20} className="text-teal-600" />
                  </div>
                  <div>
                    <h4 className="font-semibold text-gray-900">{vendor.name}</h4>
                    <p className="text-xs text-gray-500">{vendor.category}</p>
                  </div>
                </div>
                <span className={cn(
                  'px-2 py-0.5 text-xs font-medium rounded-full',
                  vendor.status === 'active' && 'bg-green-100 text-green-700',
                  vendor.status === 'inactive' && 'bg-gray-100 text-gray-600',
                  vendor.status === 'on-hold' && 'bg-amber-100 text-amber-700',
                )}>
                  {vendor.status}
                </span>
              </div>
              <div className="space-y-2">
                <div className="flex justify-between text-sm">
                  <span className="text-gray-500">Total Paid</span>
                  <span className="font-semibold text-gray-900">{formatCurrency(vendor.totalPaid)}</span>
                </div>
                <div className="flex justify-between text-sm">
                  <span className="text-gray-500">Outstanding</span>
                  <span className={cn('font-semibold', vendor.outstandingBalance > 0 ? 'text-red-600' : 'text-green-600')}>
                    {formatCurrency(vendor.outstandingBalance)}
                  </span>
                </div>
                <div className="flex justify-between text-sm">
                  <span className="text-gray-500">Payment Terms</span>
                  <span className="text-gray-700">{vendor.paymentTerms}</span>
                </div>
              </div>
            </button>
          ))}
        </div>

        <div className="lg:col-span-1">
          {selected ? (
            <div className="bg-white rounded-xl border border-gray-200 p-6 sticky top-0">
              <div className="flex items-center gap-3 mb-5">
                <div className="w-12 h-12 bg-teal-100 rounded-xl flex items-center justify-center">
                  <Building2 size={24} className="text-teal-600" />
                </div>
                <div>
                  <h3 className="font-semibold text-gray-900">{selected.name}</h3>
                  <p className="text-xs text-gray-500">{selected.category}</p>
                </div>
              </div>

              <div className="space-y-4">
                <div className="flex items-center gap-3 text-sm text-gray-600">
                  <Mail size={16} className="text-gray-400 shrink-0" />
                  <span className="truncate">{selected.email}</span>
                </div>
                <div className="flex items-center gap-3 text-sm text-gray-600">
                  <Phone size={16} className="text-gray-400 shrink-0" />
                  <span>{selected.phone}</span>
                </div>
                <div className="flex items-start gap-3 text-sm text-gray-600">
                  <MapPin size={16} className="text-gray-400 shrink-0 mt-0.5" />
                  <span>{selected.address}</span>
                </div>
                <div className="flex items-center gap-3 text-sm text-gray-600">
                  <Calendar size={16} className="text-gray-400 shrink-0" />
                  <span>Last Payment: {formatDate(selected.lastPaymentDate)}</span>
                </div>
              </div>

              <div className="mt-6 pt-4 border-t border-gray-100 space-y-3">
                <div className="flex justify-between">
                  <span className="text-sm text-gray-500">Tax ID</span>
                  <span className="text-sm text-gray-700 font-mono">{selected.taxId}</span>
                </div>
                <div className="flex justify-between">
                  <span className="text-sm text-gray-500">Payment Terms</span>
                  <span className="text-sm text-gray-700">{selected.paymentTerms}</span>
                </div>
                <div className="flex justify-between">
                  <span className="text-sm text-gray-500">Total Paid (YTD)</span>
                  <span className="text-sm font-semibold text-gray-900">{formatCurrency(selected.totalPaid)}</span>
                </div>
                <div className="flex justify-between">
                  <span className="text-sm text-gray-500">Outstanding Balance</span>
                  <span className={cn('text-sm font-semibold', selected.outstandingBalance > 0 ? 'text-red-600' : 'text-green-600')}>
                    {formatCurrency(selected.outstandingBalance)}
                  </span>
                </div>
              </div>

              <div className="mt-6 flex gap-2">
                <button className="flex-1 px-3 py-2 bg-teal-600 hover:bg-teal-700 text-white text-sm font-medium rounded-lg transition-colors">Edit Vendor</button>
                <button className="px-3 py-2 border border-gray-300 text-gray-600 text-sm font-medium rounded-lg hover:bg-gray-50 transition-colors">History</button>
              </div>
            </div>
          ) : (
            <div className="bg-white rounded-xl border border-gray-200 p-8 text-center">
              <Users size={32} className="mx-auto text-gray-300 mb-3" />
              <p className="text-sm text-gray-500">Select a vendor to view details</p>
            </div>
          )}
        </div>
      </div>
    </div>
  )
}
