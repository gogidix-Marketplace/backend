// ============================================
// FINANCE DEPARTMENT - SETTINGS PAGE
// ============================================

import { useState } from 'react'
import { useAuthStore } from '@store'
import { Save, User, Bell, Shield, Palette, Globe } from 'lucide-react'

export function SettingsPage() {
  const { user } = useAuthStore()
  const [activeTab, setActiveTab] = useState<'profile' | 'notifications' | 'security' | 'preferences'>('profile')
  const [saving, setSaving] = useState(false)

  const tabs = [
    { id: 'profile' as const, label: 'Profile', icon: User },
    { id: 'notifications' as const, label: 'Notifications', icon: Bell },
    { id: 'security' as const, label: 'Security', icon: Shield },
    { id: 'preferences' as const, label: 'Preferences', icon: Palette },
  ]

  const handleSave = () => {
    setSaving(true)
    setTimeout(() => {
      setSaving(false)
    }, 1000)
  }

  return (
    <div className="max-w-4xl mx-auto space-y-6">
      {/* Page Header */}
      <div>
        <h1 className="text-2xl font-bold text-slate-900">Settings</h1>
        <p className="text-slate-500">Manage your account and preferences</p>
      </div>

      <div className="flex flex-col md:flex-row gap-6">
        {/* Sidebar */}
        <div className="md:w-64 flex-shrink-0">
          <nav className="bg-white rounded-xl border border-slate-200 p-2 space-y-1">
            {tabs.map((tab) => {
              const Icon = tab.icon
              return (
                <button
                  key={tab.id}
                  onClick={() => setActiveTab(tab.id)}
                  className={`w-full flex items-center gap-3 px-4 py-2 rounded-lg font-medium transition-colors ${
                    activeTab === tab.id
                      ? 'bg-blue-50 text-blue-600'
                      : 'text-slate-600 hover:bg-slate-50'
                  }`}
                >
                  <Icon size={20} />
                  {tab.label}
                </button>
              )
            })}
          </nav>
        </div>

        {/* Content */}
        <div className="flex-1">
          <div className="bg-white rounded-xl border border-slate-200 p-6">
            {activeTab === 'profile' && (
              <div className="space-y-6">
                <div>
                  <h2 className="text-lg font-semibold text-slate-900 mb-4">Profile Information</h2>
                  <div className="flex items-center gap-4 mb-6">
                    <div className="w-20 h-20 bg-blue-100 rounded-full flex items-center justify-center">
                      <span className="text-2xl font-bold text-blue-600">
                        {user?.firstName?.[0]}{user?.lastName?.[0]}
                      </span>
                    </div>
                    <button className="px-4 py-2 border border-slate-300 rounded-lg hover:bg-slate-50 font-medium">
                      Change Avatar
                    </button>
                  </div>
                  <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                    <div>
                      <label className="block text-sm font-medium text-slate-700 mb-1">First Name</label>
                      <input
                        type="text"
                        defaultValue={user?.firstName}
                        className="w-full px-3 py-2 border border-slate-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                      />
                    </div>
                    <div>
                      <label className="block text-sm font-medium text-slate-700 mb-1">Last Name</label>
                      <input
                        type="text"
                        defaultValue={user?.lastName}
                        className="w-full px-3 py-2 border border-slate-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                      />
                    </div>
                    <div>
                      <label className="block text-sm font-medium text-slate-700 mb-1">Email</label>
                      <input
                        type="email"
                        defaultValue={user?.email}
                        className="w-full px-3 py-2 border border-slate-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                      />
                    </div>
                    <div>
                      <label className="block text-sm font-medium text-slate-700 mb-1">Department</label>
                      <input
                        type="text"
                        defaultValue={user?.department}
                        disabled
                        className="w-full px-3 py-2 border border-slate-300 rounded-lg bg-slate-50 text-slate-500"
                      />
                    </div>
                  </div>
                </div>
              </div>
            )}

            {activeTab === 'notifications' && (
              <div className="space-y-6">
                <h2 className="text-lg font-semibold text-slate-900 mb-4">Notification Preferences</h2>
                <div className="space-y-4">
                  {[
                    { id: 'email_approvals', label: 'Email notifications for approvals' },
                    { id: 'email_reports', label: 'Email notifications for reports' },
                    { id: 'email_overdue', label: 'Overdue invoice alerts' },
                    { id: 'email_budget', label: 'Budget threshold warnings' },
                    { id: 'push_approvals', label: 'Push notifications for approvals' },
                    { id: 'push_updates', label: 'Push notifications for updates' },
                  ].map((item) => (
                    <label key={item.id} className="flex items-center justify-between cursor-pointer">
                      <span className="text-slate-700">{item.label}</span>
                      <div className="relative">
                        <input type="checkbox" defaultChecked className="sr-only peer" />
                        <div className="w-11 h-6 bg-slate-200 rounded-full peer peer-checked:bg-blue-600 transition-colors" />
                        <div className="absolute left-1 top-1 w-4 h-4 bg-white rounded-full peer-checked:translate-x-5 transition-transform" />
                      </div>
                    </label>
                  ))}
                </div>
              </div>
            )}

            {activeTab === 'security' && (
              <div className="space-y-6">
                <h2 className="text-lg font-semibold text-slate-900 mb-4">Security Settings</h2>
                <div className="space-y-4">
                  <div>
                    <label className="block text-sm font-medium text-slate-700 mb-1">Current Password</label>
                    <input
                      type="password"
                      className="w-full px-3 py-2 border border-slate-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                    />
                  </div>
                  <div>
                    <label className="block text-sm font-medium text-slate-700 mb-1">New Password</label>
                    <input
                      type="password"
                      className="w-full px-3 py-2 border border-slate-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                    />
                  </div>
                  <div>
                    <label className="block text-sm font-medium text-slate-700 mb-1">Confirm Password</label>
                    <input
                      type="password"
                      className="w-full px-3 py-2 border border-slate-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                    />
                  </div>
                </div>
                <div className="pt-4 border-t border-slate-200">
                  <h3 className="text-md font-semibold text-slate-900 mb-3">Two-Factor Authentication</h3>
                  <p className="text-sm text-slate-500 mb-3">Add an extra layer of security to your account</p>
                  <button className="px-4 py-2 border border-slate-300 rounded-lg hover:bg-slate-50 font-medium">
                    Enable 2FA
                  </button>
                </div>
              </div>
            )}

            {activeTab === 'preferences' && (
              <div className="space-y-6">
                <h2 className="text-lg font-semibold text-slate-900 mb-4">App Preferences</h2>
                <div className="space-y-4">
                  <div>
                    <label className="block text-sm font-medium text-slate-700 mb-1">Default Currency</label>
                    <select className="w-full px-3 py-2 border border-slate-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500">
                      <option>USD - US Dollar</option>
                      <option>EUR - Euro</option>
                      <option>GBP - British Pound</option>
                      <option>NGN - Nigerian Naira</option>
                      <option>KES - Kenyan Shilling</option>
                    </select>
                  </div>
                  <div>
                    <label className="block text-sm font-medium text-slate-700 mb-1">Date Format</label>
                    <select className="w-full px-3 py-2 border border-slate-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500">
                      <option>MM/DD/YYYY</option>
                      <option>DD/MM/YYYY</option>
                      <option>YYYY-MM-DD</option>
                    </select>
                  </div>
                  <div>
                    <label className="block text-sm font-medium text-slate-700 mb-1">Timezone</label>
                    <select className="w-full px-3 py-2 border border-slate-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500">
                      <option>Eastern Time (ET)</option>
                      <option>Pacific Time (PT)</option>
                      <option>Greenwich Mean Time (GMT)</option>
                      <option>West Africa Time (WAT)</option>
                    </select>
                  </div>
                  <div>
                    <label className="flex items-center justify-between cursor-pointer">
                      <span className="text-slate-700">Compact mode</span>
                      <input type="checkbox" className="w-5 h-5 text-blue-600 rounded" />
                    </label>
                    <p className="text-xs text-slate-500 mt-1 ml-1">Use smaller spacing throughout the app</p>
                  </div>
                </div>
              </div>
            )}

            {/* Save Button */}
            <div className="pt-6 border-t border-slate-200 mt-6 flex justify-end">
              <button
                onClick={handleSave}
                disabled={saving}
                className={`flex items-center gap-2 px-6 py-2 rounded-lg font-medium transition-colors ${
                  saving
                    ? 'bg-slate-300 text-slate-500 cursor-not-allowed'
                    : 'bg-blue-600 text-white hover:bg-blue-700'
                }`}
              >
                <Save size={20} />
                {saving ? 'Saving...' : 'Save Changes'}
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  )
}
