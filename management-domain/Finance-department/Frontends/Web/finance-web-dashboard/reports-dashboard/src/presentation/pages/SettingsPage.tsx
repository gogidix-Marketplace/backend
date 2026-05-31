import { useState } from 'react'
import {
  User,
  Bell,
  Palette,
  Globe,
  Shield,
  Save,
} from 'lucide-react'
import { useReportsStore } from '@shared/store/reportsStore'
import { cn } from '@shared/utils/cn'

export default function SettingsPage() {
  const { user } = useReportsStore()
  const [activeTab, setActiveTab] = useState('profile')

  const tabs = [
    { id: 'profile', label: 'Profile', icon: User },
    { id: 'notifications', label: 'Notifications', icon: Bell },
    { id: 'appearance', label: 'Appearance', icon: Palette },
    { id: 'language', label: 'Language & Region', icon: Globe },
    { id: 'security', label: 'Security', icon: Shield },
  ]

  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-2xl font-bold text-gray-900">Settings</h1>
        <p className="text-gray-500 mt-1">Manage your account preferences and application settings</p>
      </div>

      <div className="flex flex-col lg:flex-row gap-6">
        <nav className="lg:w-56 shrink-0">
          <div className="bg-white rounded-xl border border-gray-200 p-2 space-y-1">
            {tabs.map(({ id, label, icon: Icon }) => (
              <button
                key={id}
                onClick={() => setActiveTab(id)}
                className={cn(
                  'w-full flex items-center gap-3 px-3 py-2.5 rounded-lg text-sm font-medium transition-colors text-left',
                  activeTab === id
                    ? 'bg-amber-50 text-amber-700'
                    : 'text-gray-600 hover:bg-gray-50'
                )}
              >
                <Icon size={18} />
                {label}
              </button>
            ))}
          </div>
        </nav>

        <div className="flex-1 bg-white rounded-xl border border-gray-200 p-6">
          {activeTab === 'profile' && (
            <div className="space-y-6">
              <h2 className="text-lg font-semibold text-gray-900">Profile Settings</h2>
              <div className="flex items-center gap-4">
                <div className="w-16 h-16 rounded-full bg-amber-600 flex items-center justify-center text-white text-2xl font-bold">
                  {user?.name?.charAt(0) ?? 'U'}
                </div>
                <div>
                  <p className="font-semibold text-gray-900">{user?.name}</p>
                  <p className="text-sm text-gray-500">{user?.email}</p>
                  <p className="text-xs text-amber-600 mt-1">{user?.role?.replace('_', ' ')}</p>
                </div>
              </div>
              <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">Full Name</label>
                  <input
                    type="text"
                    defaultValue={user?.name}
                    className="w-full px-4 py-2.5 border border-gray-300 rounded-lg focus:ring-2 focus:ring-amber-500 focus:border-amber-500 outline-none"
                  />
                </div>
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">Email</label>
                  <input
                    type="email"
                    defaultValue={user?.email}
                    className="w-full px-4 py-2.5 border border-gray-300 rounded-lg focus:ring-2 focus:ring-amber-500 focus:border-amber-500 outline-none"
                  />
                </div>
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">Department</label>
                  <input
                    type="text"
                    defaultValue="Finance"
                    className="w-full px-4 py-2.5 border border-gray-300 rounded-lg focus:ring-2 focus:ring-amber-500 focus:border-amber-500 outline-none"
                  />
                </div>
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">Role</label>
                  <input
                    type="text"
                    defaultValue={user?.role?.replace('_', ' ')}
                    className="w-full px-4 py-2.5 border border-gray-300 rounded-lg focus:ring-2 focus:ring-amber-500 focus:border-amber-500 outline-none"
                  />
                </div>
              </div>
              <button className="flex items-center gap-2 px-4 py-2.5 bg-amber-600 hover:bg-amber-700 text-white rounded-lg font-medium transition-colors">
                <Save size={18} />
                Save Changes
              </button>
            </div>
          )}

          {activeTab === 'notifications' && (
            <div className="space-y-6">
              <h2 className="text-lg font-semibold text-gray-900">Notification Preferences</h2>
              <div className="space-y-4">
                {[
                  { label: 'Report generation completed', desc: 'Get notified when a report finishes generating', default: true },
                  { label: 'Scheduled report reminders', desc: 'Reminders before scheduled reports run', default: true },
                  { label: 'Schedule failures', desc: 'Alert when a scheduled report fails', default: true },
                  { label: 'New templates available', desc: 'Notifications about new report templates', default: false },
                  { label: 'Weekly digest', desc: 'Summary of all reports generated in the past week', default: false },
                ].map((item) => (
                  <div key={item.label} className="flex items-start justify-between p-4 rounded-lg border border-gray-200">
                    <div>
                      <p className="font-medium text-gray-900 text-sm">{item.label}</p>
                      <p className="text-xs text-gray-500 mt-0.5">{item.desc}</p>
                    </div>
                    <label className="relative inline-flex items-center cursor-pointer">
                      <input type="checkbox" defaultChecked={item.default} className="sr-only peer" />
                      <div className="w-9 h-5 bg-gray-200 peer-focus:outline-none peer-focus:ring-2 peer-focus:ring-amber-300 rounded-full peer peer-checked:after:translate-x-full peer-checked:after:border-white after:content-[''] after:absolute after:top-[2px] after:left-[2px] after:bg-white after:border-gray-300 after:border after:rounded-full after:h-4 after:w-4 after:transition-all peer-checked:bg-amber-600"></div>
                    </label>
                  </div>
                ))}
              </div>
            </div>
          )}

          {activeTab === 'appearance' && (
            <div className="space-y-6">
              <h2 className="text-lg font-semibold text-gray-900">Appearance</h2>
              <div>
                <p className="text-sm font-medium text-gray-700 mb-3">Theme</p>
                <div className="grid grid-cols-3 gap-3">
                  {[
                    { label: 'Light', bg: 'bg-white border-amber-500', active: true },
                    { label: 'Dark', bg: 'bg-gray-900', active: false },
                    { label: 'System', bg: 'bg-gradient-to-r from-white to-gray-900', active: false },
                  ].map((theme) => (
                    <button
                      key={theme.label}
                      className={cn(
                        'p-4 rounded-lg border-2 text-center transition-colors',
                        theme.active ? 'border-amber-500' : 'border-gray-200 hover:border-amber-300'
                      )}
                    >
                      <div className={cn('w-full h-8 rounded mb-2', theme.bg)} />
                      <p className="text-sm font-medium text-gray-700">{theme.label}</p>
                    </button>
                  ))}
                </div>
              </div>
            </div>
          )}

          {activeTab === 'language' && (
            <div className="space-y-6">
              <h2 className="text-lg font-semibold text-gray-900">Language & Region</h2>
              <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">Language</label>
                  <select className="w-full px-4 py-2.5 border border-gray-300 rounded-lg focus:ring-2 focus:ring-amber-500 focus:border-amber-500 outline-none">
                    <option>English (US)</option>
                    <option>English (UK)</option>
                    <option>German</option>
                    <option>French</option>
                    <option>Japanese</option>
                  </select>
                </div>
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">Timezone</label>
                  <select className="w-full px-4 py-2.5 border border-gray-300 rounded-lg focus:ring-2 focus:ring-amber-500 focus:border-amber-500 outline-none">
                    <option>UTC-5 (Eastern)</option>
                    <option>UTC-6 (Central)</option>
                    <option>UTC+0 (London)</option>
                    <option>UTC+1 (Berlin)</option>
                    <option>UTC+9 (Tokyo)</option>
                  </select>
                </div>
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">Date Format</label>
                  <select className="w-full px-4 py-2.5 border border-gray-300 rounded-lg focus:ring-2 focus:ring-amber-500 focus:border-amber-500 outline-none">
                    <option>MM/DD/YYYY</option>
                    <option>DD/MM/YYYY</option>
                    <option>YYYY-MM-DD</option>
                  </select>
                </div>
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">Currency</label>
                  <select className="w-full px-4 py-2.5 border border-gray-300 rounded-lg focus:ring-2 focus:ring-amber-500 focus:border-amber-500 outline-none">
                    <option>USD ($)</option>
                    <option>EUR (€)</option>
                    <option>GBP (£)</option>
                    <option>JPY (¥)</option>
                  </select>
                </div>
              </div>
              <button className="flex items-center gap-2 px-4 py-2.5 bg-amber-600 hover:bg-amber-700 text-white rounded-lg font-medium transition-colors">
                <Save size={18} />
                Save Preferences
              </button>
            </div>
          )}

          {activeTab === 'security' && (
            <div className="space-y-6">
              <h2 className="text-lg font-semibold text-gray-900">Security</h2>
              <div className="space-y-4">
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">Current Password</label>
                  <input
                    type="password"
                    className="w-full px-4 py-2.5 border border-gray-300 rounded-lg focus:ring-2 focus:ring-amber-500 focus:border-amber-500 outline-none"
                    placeholder="Enter current password"
                  />
                </div>
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">New Password</label>
                  <input
                    type="password"
                    className="w-full px-4 py-2.5 border border-gray-300 rounded-lg focus:ring-2 focus:ring-amber-500 focus:border-amber-500 outline-none"
                    placeholder="Enter new password"
                  />
                </div>
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">Confirm New Password</label>
                  <input
                    type="password"
                    className="w-full px-4 py-2.5 border border-gray-300 rounded-lg focus:ring-2 focus:ring-amber-500 focus:border-amber-500 outline-none"
                    placeholder="Confirm new password"
                  />
                </div>
              </div>
              <div className="pt-4 border-t border-gray-200">
                <h3 className="font-medium text-gray-900 mb-3">Two-Factor Authentication</h3>
                <div className="flex items-center justify-between p-4 rounded-lg border border-gray-200">
                  <div>
                    <p className="text-sm font-medium text-gray-900">Enable 2FA</p>
                    <p className="text-xs text-gray-500">Add an extra layer of security to your account</p>
                  </div>
                  <label className="relative inline-flex items-center cursor-pointer">
                    <input type="checkbox" className="sr-only peer" />
                    <div className="w-9 h-5 bg-gray-200 peer-focus:outline-none peer-focus:ring-2 peer-focus:ring-amber-300 rounded-full peer peer-checked:after:translate-x-full peer-checked:after:border-white after:content-[''] after:absolute after:top-[2px] after:left-[2px] after:bg-white after:border-gray-300 after:border after:rounded-full after:h-4 after:w-4 after:transition-all peer-checked:bg-amber-600"></div>
                  </label>
                </div>
              </div>
              <button className="flex items-center gap-2 px-4 py-2.5 bg-amber-600 hover:bg-amber-700 text-white rounded-lg font-medium transition-colors">
                <Save size={18} />
                Update Security
              </button>
            </div>
          )}
        </div>
      </div>
    </div>
  )
}
