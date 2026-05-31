import { useState } from 'react'
import { useAuthStore } from '@shared/store/portalStore'
import { User, Mail, Building2, Globe, Phone, Save, CheckCircle, Camera } from 'lucide-react'

export function ProfilePage() {
  const { employee } = useAuthStore()
  const [saving, setSaving] = useState(false)
  const [saved, setSaved] = useState(false)
  const [form, setForm] = useState({
    phone: '+44 7700 900123',
    emergencyContact: 'Jane Davis',
    emergencyPhone: '+44 7700 900456',
    bankName: 'Barclays',
    accountNumber: '****4521',
    sortCode: '**-**-**',
    preferredCurrency: employee?.country === 'GB' ? 'GBP' : 'USD',
    notifications: { email: true, push: true, payment: true, expense: true },
  })

  const handleSave = async () => {
    setSaving(true)
    await new Promise(r => setTimeout(r, 1000))
    setSaving(false)
    setSaved(true)
    setTimeout(() => setSaved(false), 3000)
  }

  if (!employee) return null

  return (
    <div className="space-y-6 max-w-4xl">
      <div>
        <h1 className="text-2xl font-bold text-slate-900">My Profile</h1>
        <p className="text-slate-500">Manage your personal information and preferences</p>
      </div>

      <div className="bg-white rounded-xl border border-slate-200 p-6">
        <div className="flex items-start gap-6">
          <div className="relative">
            <div className="w-20 h-20 bg-sky-100 rounded-full flex items-center justify-center">
              <User size={36} className="text-sky-600" />
            </div>
            <button className="absolute -bottom-1 -right-1 w-8 h-8 bg-white border border-slate-200 rounded-full flex items-center justify-center shadow-sm hover:bg-slate-50">
              <Camera size={14} className="text-slate-500" />
            </button>
          </div>
          <div className="flex-1">
            <h2 className="text-xl font-bold text-slate-900">{employee.displayName}</h2>
            <p className="text-slate-500">{employee.email}</p>
            <div className="flex items-center gap-4 mt-2 text-sm text-slate-600">
              <span className="flex items-center gap-1"><Building2 size={14} /> {employee.department}</span>
              <span className="flex items-center gap-1"><Globe size={14} /> {employee.country}</span>
              <span className="flex items-center gap-1"><Mail size={14} /> {employee.employeeId}</span>
            </div>
          </div>
        </div>
      </div>

      <div className="bg-white rounded-xl border border-slate-200 p-6">
        <h3 className="text-lg font-semibold text-slate-900 mb-4">Contact Information</h3>
        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
          <div>
            <label className="block text-sm font-medium text-slate-700 mb-1">Phone Number</label>
            <div className="relative">
              <Phone size={16} className="absolute left-3 top-1/2 -translate-y-1/2 text-slate-400" />
              <input
                type="tel"
                value={form.phone}
                onChange={(e) => setForm({ ...form, phone: e.target.value })}
                className="w-full pl-9 pr-4 py-2 border border-slate-200 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-sky-500"
              />
            </div>
          </div>
          <div>
            <label className="block text-sm font-medium text-slate-700 mb-1">Emergency Contact</label>
            <input
              type="text"
              value={form.emergencyContact}
              onChange={(e) => setForm({ ...form, emergencyContact: e.target.value })}
              className="w-full px-4 py-2 border border-slate-200 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-sky-500"
            />
          </div>
          <div>
            <label className="block text-sm font-medium text-slate-700 mb-1">Emergency Phone</label>
            <input
              type="tel"
              value={form.emergencyPhone}
              onChange={(e) => setForm({ ...form, emergencyPhone: e.target.value })}
              className="w-full px-4 py-2 border border-slate-200 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-sky-500"
            />
          </div>
        </div>
      </div>

      <div className="bg-white rounded-xl border border-slate-200 p-6">
        <h3 className="text-lg font-semibold text-slate-900 mb-4">Payment Details</h3>
        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
          <div>
            <label className="block text-sm font-medium text-slate-700 mb-1">Bank Name</label>
            <input type="text" value={form.bankName} onChange={(e) => setForm({ ...form, bankName: e.target.value })} className="w-full px-4 py-2 border border-slate-200 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-sky-500" />
          </div>
          <div>
            <label className="block text-sm font-medium text-slate-700 mb-1">Account Number</label>
            <input type="text" value={form.accountNumber} disabled className="w-full px-4 py-2 border border-slate-200 rounded-lg text-sm bg-slate-50 text-slate-500" />
          </div>
          <div>
            <label className="block text-sm font-medium text-slate-700 mb-1">Sort Code</label>
            <input type="text" value={form.sortCode} disabled className="w-full px-4 py-2 border border-slate-200 rounded-lg text-sm bg-slate-50 text-slate-500" />
          </div>
          <div>
            <label className="block text-sm font-medium text-slate-700 mb-1">Preferred Currency</label>
            <select value={form.preferredCurrency} onChange={(e) => setForm({ ...form, preferredCurrency: e.target.value })} className="w-full px-4 py-2 border border-slate-200 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-sky-500">
              <option value="USD">USD - US Dollar</option>
              <option value="GBP">GBP - British Pound</option>
              <option value="EUR">EUR - Euro</option>
              <option value="NGN">NGN - Nigerian Naira</option>
            </select>
          </div>
        </div>
      </div>

      <div className="bg-white rounded-xl border border-slate-200 p-6">
        <h3 className="text-lg font-semibold text-slate-900 mb-4">Notification Preferences</h3>
        <div className="space-y-3">
          {[
            { key: 'email' as const, label: 'Email Notifications', desc: 'Receive updates via email' },
            { key: 'push' as const, label: 'Push Notifications', desc: 'Browser push notifications' },
            { key: 'payment' as const, label: 'Payment Alerts', desc: 'Get notified when payments are processed' },
            { key: 'expense' as const, label: 'Expense Updates', desc: 'Updates on expense approval status' },
          ].map(item => (
            <div key={item.key} className="flex items-center justify-between py-2">
              <div>
                <p className="text-sm font-medium text-slate-900">{item.label}</p>
                <p className="text-xs text-slate-500">{item.desc}</p>
              </div>
              <button
                onClick={() => setForm({ ...form, notifications: { ...form.notifications, [item.key]: !form.notifications[item.key] } })}
                className={`relative w-11 h-6 rounded-full transition-colors ${form.notifications[item.key] ? 'bg-sky-600' : 'bg-slate-200'}`}
              >
                <span className={`absolute top-0.5 w-5 h-5 bg-white rounded-full shadow transition-transform ${form.notifications[item.key] ? 'left-[22px]' : 'left-0.5'}`} />
              </button>
            </div>
          ))}
        </div>
      </div>

      <div className="flex justify-end">
        <button
          onClick={handleSave}
          disabled={saving}
          className="flex items-center gap-2 px-6 py-2.5 bg-sky-600 text-white rounded-lg text-sm font-medium hover:bg-sky-700 disabled:opacity-50 transition-colors"
        >
          {saved ? <CheckCircle size={16} /> : <Save size={16} />}
          {saving ? 'Saving...' : saved ? 'Saved!' : 'Save Changes'}
        </button>
      </div>
    </div>
  )
}
