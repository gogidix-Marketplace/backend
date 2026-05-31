import { Link, useLocation } from 'react-router-dom'
import {
  LayoutDashboard,
  BarChart3,
  Activity,
  Settings,
  Menu,
  X,
  Bell,
} from 'lucide-react'
import { useDashboardStore } from '@/lib/store'

interface LayoutProps {
  children: React.ReactNode
}

export default function Layout({ children }: LayoutProps) {
  const location = useLocation()
  const { sidebarOpen, toggleSidebar, notifications, removeNotification } = useDashboardStore()

  const navigation = [
    { name: 'Dashboard', href: '/', icon: LayoutDashboard },
    { name: 'Charts', href: '/charts', icon: BarChart3 },
    { name: 'Real-time', href: '/realtime', icon: Activity },
    { name: 'Settings', href: '/settings', icon: Settings },
  ]

  return (
    <div className="min-h-screen bg-secondary-50">
      {/* Sidebar */}
      <aside
        className={`fixed inset-y-0 left-0 z-50 w-64 bg-secondary-900 text-white transition-transform duration-300 ease-in-out ${
          sidebarOpen ? 'translate-x-0' : '-translate-x-full'
        }`}
      >
        <div className="flex h-full flex-col">
          <div className="flex items-center justify-between px-6 py-4 border-b border-secondary-800">
            <h1 className="text-xl font-bold">Admin Dashboard</h1>
            <button
              onClick={toggleSidebar}
              className="lg:hidden text-secondary-400 hover:text-white"
            >
              <X className="h-6 w-6" />
            </button>
          </div>

          <nav className="flex-1 px-4 py-6 space-y-2">
            {navigation.map((item) => {
              const isActive = location.pathname === item.href
              return (
                <Link
                  key={item.name}
                  to={item.href}
                  className={`flex items-center gap-3 px-4 py-3 rounded-lg transition-colors ${
                    isActive
                      ? 'bg-primary-600 text-white'
                      : 'text-secondary-300 hover:bg-secondary-800 hover:text-white'
                  }`}
                >
                  <item.icon className="h-5 w-5" />
                  <span>{item.name}</span>
                </Link>
              )
            })}
          </nav>

          <div className="px-6 py-4 border-t border-secondary-800">
            <p className="text-sm text-secondary-400">
              Version 1.0.0
            </p>
          </div>
        </div>
      </aside>

      {/* Main content */}
      <div className={`transition-all duration-300 ${sidebarOpen ? 'lg:ml-64' : ''}`}>
        {/* Header */}
        <header className="bg-white shadow-sm border-b border-secondary-200 sticky top-0 z-40">
          <div className="flex items-center justify-between px-6 py-4">
            <div className="flex items-center gap-4">
              <button
                onClick={toggleSidebar}
                className="text-secondary-600 hover:text-secondary-900"
              >
                <Menu className="h-6 w-6" />
              </button>
              <h2 className="text-lg font-semibold text-secondary-900">
                {navigation.find((item) => item.href === location.pathname)?.name || 'Dashboard'}
              </h2>
            </div>

            <div className="flex items-center gap-4">
              {/* Notifications */}
              <div className="relative">
                <button className="text-secondary-600 hover:text-secondary-900">
                  <Bell className="h-5 w-5" />
                  {notifications.length > 0 && (
                    <span className="absolute -top-1 -right-1 h-4 w-4 bg-red-500 rounded-full text-xs text-white flex items-center justify-center">
                      {notifications.length}
                    </span>
                  )}
                </button>
              </div>

              {/* Tenant selector */}
              <select className="input-field !w-auto text-sm">
                <option value="default">Default Tenant</option>
              </select>
            </div>
          </div>
        </header>

        {/* Notifications panel */}
        {notifications.length > 0 && (
          <div className="fixed top-20 right-4 z-50 space-y-2 max-w-sm">
            {notifications.map((notification) => (
              <div
                key={notification.id}
                className={`p-4 rounded-lg shadow-lg ${
                  notification.type === 'error'
                    ? 'bg-red-50 border border-red-200'
                    : notification.type === 'warning'
                    ? 'bg-yellow-50 border border-yellow-200'
                    : notification.type === 'success'
                    ? 'bg-green-50 border border-green-200'
                    : 'bg-blue-50 border border-blue-200'
                }`}
              >
                <div className="flex justify-between items-start">
                  <div>
                    <p className="font-medium">{notification.title}</p>
                    <p className="text-sm">{notification.message}</p>
                  </div>
                  <button
                    onClick={() => removeNotification(notification.id)}
                    className="text-secondary-400 hover:text-secondary-600"
                  >
                    <X className="h-4 w-4" />
                  </button>
                </div>
              </div>
            ))}
          </div>
        )}

        {/* Page content */}
        <main className="p-6">{children}</main>
      </div>
    </div>
  )
}
