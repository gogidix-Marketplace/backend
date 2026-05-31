export default function Settings() {
  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-2xl font-bold text-secondary-900">Settings</h1>
        <p className="text-secondary-600">Configure dashboard preferences</p>
      </div>

      {/* Theme Settings */}
      <div className="card">
        <h3 className="text-lg font-semibold mb-4">Appearance</h3>
        <div className="space-y-4">
          <div>
            <label className="block text-sm font-medium text-secondary-700 mb-2">
              Theme
            </label>
            <select className="input-field">
              <option>Light</option>
              <option>Dark</option>
              <option>System</option>
            </select>
          </div>
        </div>
      </div>

      {/* Notification Settings */}
      <div className="card">
        <h3 className="text-lg font-semibold mb-4">Notifications</h3>
        <div className="space-y-4">
          <label className="flex items-center gap-3">
            <input type="checkbox" className="h-4 w-4" defaultChecked />
            <span>Enable desktop notifications</span>
          </label>
          <label className="flex items-center gap-3">
            <input type="checkbox" className="h-4 w-4" defaultChecked />
            <span>Sound notifications</span>
          </label>
          <label className="flex items-center gap-3">
            <input type="checkbox" className="h-4 w-4" defaultChecked />
            <span>Real-time updates</span>
          </label>
        </div>
      </div>

      {/* API Settings */}
      <div className="card">
        <h3 className="text-lg font-semibold mb-4">API Configuration</h3>
        <div className="space-y-4">
          <div>
            <label className="block text-sm font-medium text-secondary-700 mb-2">
              Gateway URL
            </label>
            <input
              type="text"
              className="input-field"
              defaultValue="http://localhost:8907"
            />
          </div>
          <div>
            <label className="block text-sm font-medium text-secondary-700 mb-2">
              Chart Service URL
            </label>
            <input
              type="text"
              className="input-field"
              defaultValue="http://localhost:8909"
            />
          </div>
          <div>
            <label className="block text-sm font-medium text-secondary-700 mb-2">
              WebSocket URL
            </label>
            <input
              type="text"
              className="input-field"
              defaultValue="ws://localhost:8908"
            />
          </div>
        </div>
      </div>

      {/* Tenant Settings */}
      <div className="card">
        <h3 className="text-lg font-semibold mb-4">Tenant Settings</h3>
        <div className="space-y-4">
          <div>
            <label className="block text-sm font-medium text-secondary-700 mb-2">
              Default Tenant
            </label>
            <input
              type="text"
              className="input-field"
              defaultValue="default"
            />
          </div>
        </div>
      </div>

      {/* Actions */}
      <div className="flex justify-end gap-4">
        <button className="btn-secondary">Reset to Defaults</button>
        <button className="btn-primary">Save Changes</button>
      </div>
    </div>
  )
}
