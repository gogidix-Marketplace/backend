import { useAccountantStore } from '@shared/store'
import { User, Bell, Palette, Shield, Save } from 'lucide-react'

export default function SettingsPage() {
  const { user } = useAccountantStore()

  return (
    <div className="max-w-3xl space-y-6">
      <div>
        <h2 className="text-xl font-bold text-gray-900">Settings</h2>
        <p className="text-sm text-gray-500">Manage your account preferences</p>
      </div>

      <div className="bg-white rounded-xl border border-gray-200 p-6">
        <div className="flex items-center gap-3 mb-6">
          <div className="p-2 bg-teal-50 rounded-lg">
            <User size={20} className="text-teal-600" />
          </div>
          <h3 className="font-semibold text-gray-800">Profile</h3>
        </div>
        <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Full Name</label>
            <input
              type="text"
              defaultValue={user?.name}
              className="w-full px-3 py-2 border border-gray-300 rounded-lg text-sm focus:ring-2 focus:ring-teal-500 focus:border-teal-500 outline-none"
            />
          </div>
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Email</label>
            <input
              type="email"
              defaultValue={user?.email}
              className="w-full px-3 py-2 border border-gray-300 rounded-lg text-sm focus:ring-2 focus:ring-teal-500 focus:border-teal-500 outline-none"
            />
          </div>
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Role</label>
            <input
              type="text"
              defaultValue={user?.role}
              disabled
              className="w-full px-3 py-2 border border-gray-200 bg-gray-50 rounded-lg text-sm text-gray-500"
            />
          </div>
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Department</label>
            <input
              type="text"
              defaultValue="Finance & Accounting"
              disabled
              className="w-full px-3 py-2 border border-gray-200 bg-gray-50 rounded-lg text-sm text-gray-500"
            />
          </div>
        </div>
        <button className="mt-4 inline-flex items-center gap-2 px-4 py-2 bg-teal-600 hover:bg-teal-700 text-white text-sm font-medium rounded-lg transition-colors">
          <Save size={16} />
          Save Profile
        </button>
      </div>

      <div className="bg-white rounded-xl border border-gray-200 p-6">
        <div className="flex items-center gap-3 mb-6">
          <div className="p-2 bg-blue-50 rounded-lg">
            <Bell size={20} className="text-blue-600" />
          </div>
          <h3 className="font-semibold text-gray-800">Notification Preferences</h3>
        </div>
        <div className="space-y-4">
          {[
            { label: 'Invoice approval requests', description: 'Get notified when invoices need your approval', enabled: true },
            { label: 'Payment reminders', description: 'Reminders for upcoming payment due dates', enabled: true },
            { label: 'Reconciliation alerts', description: 'Alerts for unmatched transactions', enabled: true },
            { label: 'Journal entry updates', description: 'Notifications when entries are posted or reviewed', enabled: false },
            { label: 'System announcements', description: 'Important updates from the finance team', enabled: true },
          ].map((pref) => (
            <div key={pref.label} className="flex items-center justify-between py-2">
              <div>
                <p className="text-sm font-medium text-gray-800">{pref.label}</p>
                <p className="text-xs text-gray-500">{pref.description}</p>
              </div>
              <label className="relative inline-flex items-center cursor-pointer">
                <input type="checkbox" defaultChecked={pref.enabled} className="sr-only peer" />
                <div className="w-9 h-5 bg-gray-200 peer-focus:outline-none peer-focus:ring-2 peer-focus:ring-teal-300 rounded-full peer peer-checked:after:translate-x-full peer-checked:after:border-white after:content-[''] after:absolute after:top-[2px] after:left-[2px] after:bg-white after:border-gray-300 after:border after:rounded-full after:h-4 after:w-4 after:transition-all peer-checked:bg-teal-600"></div>
              </label>
            </div>
          ))}
        </div>
      </div>

      <div className="bg-white rounded-xl border border-gray-200 p-6">
        <div className="flex items-center gap-3 mb-6">
          <div className="p-2 bg-purple-50 rounded-lg">
            <Palette size={20} className="text-purple-600" />
          </div>
          <h3 className="font-semibold text-gray-800">Appearance</h3>
        </div>
        <div className="flex items-center justify-between">
          <div>
            <p className="text-sm font-medium text-gray-800">Dark Mode</p>
            <p className="text-xs text-gray-500">Coming soon — currently in development</p>
          </div>
          <label className="relative inline-flex items-center cursor-not-allowed opacity-50">
            <input type="checkbox" disabled className="sr-only peer" />
            <div className="w-9 h-5 bg-gray-200 rounded-full peer peer-checked:after:translate-x-full peer-checked:after:border-white after:content-[''] after:absolute after:top-[2px] after:left-[2px] after:bg-white after:border-gray-300 after:border after:rounded-full after:h-4 after:w-4 after:transition-all peer-checked:bg-teal-600"></div>
          </label>
        </div>
      </div>

      <div className="bg-white rounded-xl border border-gray-200 p-6">
        <div className="flex items-center gap-3 mb-6">
          <div className="p-2 bg-amber-50 rounded-lg">
            <Shield size={20} className="text-amber-600" />
          </div>
          <h3 className="font-semibold text-gray-800">Security</h3>
        </div>
        <div className="space-y-3">
          <button className="w-full text-left px-4 py-3 border border-gray-200 rounded-lg hover:bg-gray-50 transition-colors">
            <p className="text-sm font-medium text-gray-800">Change Password</p>
            <p className="text-xs text-gray-500">Update your account password</p>
          </button>
          <button className="w-full text-left px-4 py-3 border border-gray-200 rounded-lg hover:bg-gray-50 transition-colors">
            <p className="text-sm font-medium text-gray-800">Two-Factor Authentication</p>
            <p className="text-xs text-gray-500">Add an extra layer of security to your account</p>
          </button>
          <button className="w-full text-left px-4 py-3 border border-gray-200 rounded-lg hover:bg-gray-50 transition-colors">
            <p className="text-sm font-medium text-gray-800">Active Sessions</p>
            <p className="text-xs text-gray-500">Manage your active login sessions</p>
          </button>
        </div>
      </div>
    </div>
  )
}
