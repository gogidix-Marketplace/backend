import { useState } from 'react'
import { User, Bell, Palette, Save, Check } from 'lucide-react'
import { useCfoStore } from '@shared/store'
import { cn } from '@shared/utils'

export default function SettingsPage() {
  const { user } = useCfoStore()
  const [saved, setSaved] = useState(false)
  const [notifications, setNotifications] = useState({
    approvals: true,
    riskAlerts: true,
    strategyUpdates: true,
    investorComms: true,
    dailyDigest: false,
    weeklyReport: true,
  })
  const [preferences, setPreferences] = useState({
    currency: 'USD',
    timezone: 'America/New_York',
    dateFormat: 'MM/DD/YYYY',
    language: 'en',
  })

  const handleSave = () => {
    setSaved(true)
    setTimeout(() => setSaved(false), 2000)
  }

  return (
    <div className="space-y-6 max-w-3xl">
      <div>
        <h1 className="text-2xl font-bold text-gray-900">Settings</h1>
        <p className="text-sm text-gray-500 mt-1">Manage your profile and preferences</p>
      </div>

      <div className="bg-white rounded-xl border border-gray-200 p-6">
        <div className="flex items-center gap-2 mb-5">
          <User className="w-5 h-5 text-indigo-600" />
          <h3 className="text-sm font-semibold text-gray-700">Profile</h3>
        </div>
        <div className="flex items-center gap-5 mb-6">
          <div className="w-16 h-16 rounded-full bg-indigo-600 flex items-center justify-center text-white text-xl font-bold">
            {user?.name?.charAt(0) ?? 'S'}
          </div>
          <div>
            <p className="text-lg font-semibold text-gray-900">{user?.name}</p>
            <p className="text-sm text-gray-500">{user?.role}</p>
            <p className="text-sm text-indigo-600">{user?.email}</p>
          </div>
        </div>
        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Full Name</label>
            <input
              type="text"
              defaultValue={user?.name}
              className="w-full px-3 py-2 rounded-lg border border-gray-300 text-sm focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 outline-none"
            />
          </div>
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Email</label>
            <input
              type="email"
              defaultValue={user?.email}
              className="w-full px-3 py-2 rounded-lg border border-gray-300 text-sm focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 outline-none"
            />
          </div>
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Role</label>
            <input
              type="text"
              defaultValue={user?.role}
              disabled
              className="w-full px-3 py-2 rounded-lg border border-gray-200 bg-gray-50 text-sm text-gray-500"
            />
          </div>
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Department</label>
            <input
              type="text"
              defaultValue="Finance"
              disabled
              className="w-full px-3 py-2 rounded-lg border border-gray-200 bg-gray-50 text-sm text-gray-500"
            />
          </div>
        </div>
      </div>

      <div className="bg-white rounded-xl border border-gray-200 p-6">
        <div className="flex items-center gap-2 mb-5">
          <Bell className="w-5 h-5 text-indigo-600" />
          <h3 className="text-sm font-semibold text-gray-700">Notifications</h3>
        </div>
        <div className="space-y-4">
          {[
            { key: 'approvals', label: 'Approval Alerts', desc: 'Get notified when new approvals are submitted' },
            { key: 'riskAlerts', label: 'Risk Alerts', desc: 'Critical and high-risk notifications' },
            { key: 'strategyUpdates', label: 'Strategy Updates', desc: 'Milestones and progress updates' },
            { key: 'investorComms', label: 'Investor Communications', desc: 'New investor emails and meetings' },
            { key: 'dailyDigest', label: 'Daily Digest', desc: 'Daily summary of financial activities' },
            { key: 'weeklyReport', label: 'Weekly Report', desc: 'Weekly financial performance summary' },
          ].map((item) => (
            <div key={item.key} className="flex items-center justify-between">
              <div>
                <p className="text-sm font-medium text-gray-800">{item.label}</p>
                <p className="text-xs text-gray-500">{item.desc}</p>
              </div>
              <button
                onClick={() =>
                  setNotifications((prev) => ({
                    ...prev,
                    [item.key]: !prev[item.key as keyof typeof notifications],
                  }))
                }
                className={cn(
                  'relative w-11 h-6 rounded-full transition-colors',
                  notifications[item.key as keyof typeof notifications]
                    ? 'bg-indigo-600'
                    : 'bg-gray-300'
                )}
              >
                <span
                  className={cn(
                    'absolute top-0.5 left-0.5 w-5 h-5 rounded-full bg-white transition-transform shadow-sm',
                    notifications[item.key as keyof typeof notifications] && 'translate-x-5'
                  )}
                />
              </button>
            </div>
          ))}
        </div>
      </div>

      <div className="bg-white rounded-xl border border-gray-200 p-6">
        <div className="flex items-center gap-2 mb-5">
          <Palette className="w-5 h-5 text-indigo-600" />
          <h3 className="text-sm font-semibold text-gray-700">Preferences</h3>
        </div>
        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Default Currency</label>
            <select
              value={preferences.currency}
              onChange={(e) => setPreferences((p) => ({ ...p, currency: e.target.value }))}
              className="w-full px-3 py-2 rounded-lg border border-gray-300 text-sm focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 outline-none"
            >
              <option value="USD">USD - US Dollar</option>
              <option value="EUR">EUR - Euro</option>
              <option value="GBP">GBP - British Pound</option>
              <option value="JPY">JPY - Japanese Yen</option>
            </select>
          </div>
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Timezone</label>
            <select
              value={preferences.timezone}
              onChange={(e) => setPreferences((p) => ({ ...p, timezone: e.target.value }))}
              className="w-full px-3 py-2 rounded-lg border border-gray-300 text-sm focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 outline-none"
            >
              <option value="America/New_York">Eastern (ET)</option>
              <option value="America/Chicago">Central (CT)</option>
              <option value="America/Los_Angeles">Pacific (PT)</option>
              <option value="Europe/London">London (GMT)</option>
              <option value="Asia/Tokyo">Tokyo (JST)</option>
            </select>
          </div>
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Date Format</label>
            <select
              value={preferences.dateFormat}
              onChange={(e) => setPreferences((p) => ({ ...p, dateFormat: e.target.value }))}
              className="w-full px-3 py-2 rounded-lg border border-gray-300 text-sm focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 outline-none"
            >
              <option value="MM/DD/YYYY">MM/DD/YYYY</option>
              <option value="DD/MM/YYYY">DD/MM/YYYY</option>
              <option value="YYYY-MM-DD">YYYY-MM-DD</option>
            </select>
          </div>
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Language</label>
            <select
              value={preferences.language}
              onChange={(e) => setPreferences((p) => ({ ...p, language: e.target.value }))}
              className="w-full px-3 py-2 rounded-lg border border-gray-300 text-sm focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 outline-none"
            >
              <option value="en">English</option>
              <option value="es">Spanish</option>
              <option value="fr">French</option>
              <option value="de">German</option>
              <option value="ja">Japanese</option>
            </select>
          </div>
        </div>
      </div>

      <div className="flex justify-end">
        <button
          onClick={handleSave}
          className="flex items-center gap-2 px-6 py-2.5 rounded-lg bg-indigo-600 hover:bg-indigo-700 text-white text-sm font-medium transition-colors"
        >
          {saved ? (
            <>
              <Check className="w-4 h-4" />
              Saved
            </>
          ) : (
            <>
              <Save className="w-4 h-4" />
              Save Changes
            </>
          )}
        </button>
      </div>
    </div>
  )
}
