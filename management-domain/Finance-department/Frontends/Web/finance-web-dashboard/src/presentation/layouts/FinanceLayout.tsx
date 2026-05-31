// ============================================
// FINANCE DEPARTMENT - FINANCE LAYOUT
// ============================================

import { Outlet, useNavigate, useLocation } from 'react-router-dom'
import { useState, useEffect } from 'react'
import {
  LayoutDashboard,
  Globe2,
  DollarSign,
  Receipt,
  BarChart3,
  FileText,
  Building2,
  FileCheck,
  Settings,
  LogOut,
  User,
  Bell,
  Menu,
  X,
  ChevronDown,
  CheckCircle,
  XCircle,
  AlertTriangle,
} from 'lucide-react'
import { useAuthStore, useFinanceStore } from '@store'
import { cn } from '@shared/utils/cn'

interface NavItem {
  label: string
  path: string
  icon: React.ElementType
  badge?: number
}

const NAV_ITEMS: NavItem[] = [
  { label: 'Overview', path: '/dashboard', icon: LayoutDashboard },
  { label: 'Countries', path: '/countries', icon: Globe2 },
  { label: 'Revenue', path: '/revenue', icon: DollarSign },
  { label: 'Expenses', path: '/expenses', icon: Receipt },
  { label: 'Budgets & Forecasts', path: '/budgets', icon: BarChart3 },
  { label: 'Consolidated Reports', path: '/reports', icon: FileText },
  { label: 'Treasury', path: '/treasury', icon: Building2 },
  { label: 'Tax & Compliance', path: '/tax-compliance', icon: FileCheck },
  { label: 'Settings', path: '/settings', icon: Settings },
]

export function FinanceLayout() {
  const navigate = useNavigate()
  const location = useLocation()
  const { user, logout } = useAuthStore()
  const {
    sidebarCollapsed,
    toggleSidebar,
    notifications,
    removeNotification,
    clearNotifications,
  } = useFinanceStore()

  const [mobileMenuOpen, setMobileMenuOpen] = useState(false)
  const [userMenuOpen, setUserMenuOpen] = useState(false)
  const [notificationPanelOpen, setNotificationPanelOpen] = useState(false)

  const currentPage = NAV_ITEMS.find((item) =>
    location.pathname.startsWith(item.path)
  )

  // Auto-remove notifications after 5 seconds
  useEffect(() => {
    notifications.forEach((notification) => {
      const timer = setTimeout(() => {
        removeNotification(notification.id)
      }, 5000)
      return () => clearTimeout(timer)
    })
  }, [notifications, removeNotification])

  const handleLogout = () => {
    logout()
    navigate('/login')
  }

  const handleNavigation = (path: string) => {
    navigate(path)
    setMobileMenuOpen(false)
  }

  return (
    <div className="min-h-screen bg-slate-50 flex">
      {/* Sidebar - Desktop */}
      <aside
        className={cn(
          'hidden md:flex flex-col bg-slate-900 text-white transition-all duration-300',
          sidebarCollapsed ? 'w-16' : 'w-64'
        )}
      >
        {/* Logo */}
        <div className="p-4 border-b border-slate-700 flex items-center justify-between">
          {!sidebarCollapsed && (
            <div>
              <h1 className="font-bold text-lg">Finance</h1>
              <p className="text-xs text-slate-400">Management Dashboard</p>
            </div>
          )}
          <button
            onClick={toggleSidebar}
            className="p-1 hover:bg-slate-800 rounded"
          >
            {sidebarCollapsed ? <Menu size={20} /> : <X size={20} />}
          </button>
        </div>

        {/* Navigation */}
        <nav className="flex-1 p-4 space-y-1 overflow-y-auto scrollbar-thin">
          {NAV_ITEMS.map((item) => {
            const Icon = item.icon
            const isActive = location.pathname === item.path ||
              location.pathname.startsWith(item.path + '/')

            return (
              <button
                key={item.path}
                onClick={() => handleNavigation(item.path)}
                className={cn(
                  'w-full flex items-center gap-3 px-3 py-2 rounded-lg transition-colors',
                  isActive
                    ? 'bg-blue-600 text-white'
                    : 'text-slate-300 hover:bg-slate-800'
                )}
              >
                <Icon size={20} />
                {!sidebarCollapsed && (
                  <>
                    <span className="flex-1 text-left">{item.label}</span>
                    {item.badge !== undefined && item.badge > 0 && (
                      <span className="bg-red-500 text-white text-xs px-2 py-0.5 rounded-full">
                        {item.badge}
                      </span>
                    )}
                  </>
                )}
              </button>
            )
          })}
        </nav>

        {/* Finance Ecosystem Links */}
        {!sidebarCollapsed && (
          <div className="px-4 pb-3">
            <p className="text-xs text-slate-500 uppercase tracking-wider mb-2 font-medium">Finance Apps</p>
            <div className="space-y-1">
              {[
                { label: 'Employee Portal', url: 'http://localhost:3017', color: 'text-sky-400' },
                { label: 'Accountant Tools', url: 'http://localhost:3018', color: 'text-teal-400' },
                { label: 'CFO Dashboard', url: 'http://localhost:3019', color: 'text-indigo-400' },
                { label: 'Reports Hub', url: 'http://localhost:3020', color: 'text-amber-400' },
              ].map(app => (
                <a
                  key={app.url}
                  href={app.url}
                  target="_blank"
                  rel="noopener noreferrer"
                  className={cn('block px-3 py-1.5 text-xs rounded-lg hover:bg-slate-800 transition-colors', app.color)}
                >
                  {app.label} ↗
                </a>
              ))}
            </div>
          </div>
        )}

        {/* User Section */}
        <div className="p-4 border-t border-slate-700">
          {!sidebarCollapsed && user && (
            <div className="flex items-center gap-3 mb-3">
              <div className="w-10 h-10 bg-blue-600 rounded-full flex items-center justify-center">
                <User size={20} />
              </div>
              <div className="flex-1 min-w-0">
                <p className="font-medium truncate">{user.displayName}</p>
                <p className="text-xs text-slate-400 truncate">{user.role}</p>
              </div>
            </div>
          )}
          <button
            onClick={handleLogout}
            className={cn(
              'w-full flex items-center gap-3 px-3 py-2 rounded-lg text-slate-300 hover:bg-slate-800 transition-colors',
              sidebarCollapsed && 'justify-center'
            )}
          >
            <LogOut size={20} />
            {!sidebarCollapsed && <span>Sign Out</span>}
          </button>
        </div>
      </aside>

      {/* Mobile Menu */}
      {mobileMenuOpen && (
        <div className="fixed inset-0 z-50 md:hidden">
          <div
            className="fixed inset-0 bg-black/50"
            onClick={() => setMobileMenuOpen(false)}
          />
          <div className="fixed left-0 top-0 bottom-0 w-64 bg-slate-900 text-white">
            <div className="p-4 border-b border-slate-700 flex items-center justify-between">
              <div>
                <h1 className="font-bold text-lg">Finance</h1>
                <p className="text-xs text-slate-400">Management Dashboard</p>
              </div>
              <button
                onClick={() => setMobileMenuOpen(false)}
                className="p-1 hover:bg-slate-800 rounded"
              >
                <X size={20} />
              </button>
            </div>
            <nav className="p-4 space-y-1">
              {NAV_ITEMS.map((item) => {
                const Icon = item.icon
                const isActive = location.pathname === item.path

                return (
                  <button
                    key={item.path}
                    onClick={() => handleNavigation(item.path)}
                    className={cn(
                      'w-full flex items-center gap-3 px-3 py-2 rounded-lg transition-colors',
                      isActive
                        ? 'bg-blue-600 text-white'
                        : 'text-slate-300 hover:bg-slate-800'
                    )}
                  >
                    <Icon size={20} />
                    <span>{item.label}</span>
                    {item.badge !== undefined && item.badge > 0 && (
                      <span className="ml-auto bg-red-500 text-white text-xs px-2 py-0.5 rounded-full">
                        {item.badge}
                      </span>
                    )}
                  </button>
                )
              })}
            </nav>
            {user && (
              <div className="p-4 border-t border-slate-700">
                <div className="flex items-center gap-3 mb-3">
                  <div className="w-10 h-10 bg-blue-600 rounded-full flex items-center justify-center">
                    <User size={20} />
                  </div>
                  <div>
                    <p className="font-medium">{user.displayName}</p>
                    <p className="text-xs text-slate-400">{user.role}</p>
                  </div>
                </div>
                <button
                  onClick={handleLogout}
                  className="w-full flex items-center gap-3 px-3 py-2 rounded-lg text-slate-300 hover:bg-slate-800"
                >
                  <LogOut size={20} />
                  <span>Sign Out</span>
                </button>
              </div>
            )}
          </div>
        </div>
      )}

      {/* Main Content */}
      <div className="flex-1 flex flex-col overflow-hidden">
        {/* Header */}
        <header className="bg-white border-b border-slate-200 px-4 py-3 flex items-center justify-between">
          <div className="flex items-center gap-4">
            <button
              onClick={() => setMobileMenuOpen(true)}
              className="md:hidden p-2 hover:bg-slate-100 rounded-lg"
            >
              <Menu size={24} />
            </button>
            <div>
              <h2 className="font-semibold text-lg">{currentPage?.label || 'Finance Dashboard'}</h2>
              <p className="text-sm text-slate-500">Finance Dashboard</p>
            </div>
          </div>

          <div className="flex items-center gap-2">
            {/* Notifications */}
            <div className="relative">
              <button
                onClick={() => setNotificationPanelOpen(!notificationPanelOpen)}
                className="p-2 hover:bg-slate-100 rounded-lg relative"
              >
                <Bell size={20} />
                {notifications.length > 0 && (
                  <span className="absolute top-1 right-1 w-2 h-2 bg-red-500 rounded-full" />
                )}
              </button>

              {/* Notification Panel */}
              {notificationPanelOpen && (
                <div className="absolute right-0 mt-2 w-80 bg-white rounded-lg shadow-lg border border-slate-200 z-50">
                  <div className="p-4 border-b border-slate-200 flex items-center justify-between">
                    <h3 className="font-semibold">Notifications</h3>
                    {notifications.length > 0 && (
                      <button
                        onClick={clearNotifications}
                        className="text-sm text-blue-600 hover:text-blue-700"
                      >
                        Clear All
                      </button>
                    )}
                  </div>
                  <div className="max-h-64 overflow-y-auto">
                    {notifications.length === 0 ? (
                      <p className="p-4 text-sm text-slate-500 text-center">No notifications</p>
                    ) : (
                      notifications.map((notification) => (
                        <div
                          key={notification.id}
                          className={cn(
                            'p-4 border-b border-slate-100 flex items-start gap-3',
                            notification.type === 'success' && 'bg-green-50',
                            notification.type === 'error' && 'bg-red-50',
                            notification.type === 'warning' && 'bg-yellow-50'
                          )}
                        >
                          {notification.type === 'success' && <CheckCircle size={16} className="text-green-600 mt-0.5" />}
                          {notification.type === 'error' && <XCircle size={16} className="text-red-600 mt-0.5" />}
                          {notification.type === 'warning' && <AlertTriangle size={16} className="text-yellow-600 mt-0.5" />}
                          {notification.type === 'info' && <Bell size={16} className="text-blue-600 mt-0.5" />}
                          <div className="flex-1 min-w-0">
                            <p className="text-sm">{notification.message}</p>
                          </div>
                          <button
                            onClick={() => removeNotification(notification.id)}
                            className="text-slate-400 hover:text-slate-600"
                          >
                            <X size={14} />
                          </button>
                        </div>
                      ))
                    )}
                  </div>
                </div>
              )}
            </div>

            {/* User Menu */}
            <div className="relative">
              <button
                onClick={() => setUserMenuOpen(!userMenuOpen)}
                className="flex items-center gap-2 p-2 hover:bg-slate-100 rounded-lg"
              >
                <div className="w-8 h-8 bg-blue-600 rounded-full flex items-center justify-center">
                  <User size={16} className="text-white" />
                </div>
                <ChevronDown size={16} className="text-slate-500" />
              </button>

              {userMenuOpen && (
                <div className="absolute right-0 mt-2 w-48 bg-white rounded-lg shadow-lg border border-slate-200 z-50">
                  <div className="p-4 border-b border-slate-200">
                    <p className="font-medium">{user?.displayName}</p>
                    <p className="text-sm text-slate-500">{user?.email}</p>
                    <p className="text-xs text-slate-400 mt-1">{user?.role}</p>
                  </div>
                  <div className="p-2">
                    <button
                      onClick={() => {
                        navigate('/settings')
                        setUserMenuOpen(false)
                      }}
                      className="w-full text-left px-3 py-2 hover:bg-slate-100 rounded-lg flex items-center gap-2"
                    >
                      <Settings size={16} />
                      <span>Settings</span>
                    </button>
                    <button
                      onClick={() => {
                        handleLogout()
                        setUserMenuOpen(false)
                      }}
                      className="w-full text-left px-3 py-2 hover:bg-slate-100 rounded-lg flex items-center gap-2 text-red-600"
                    >
                      <LogOut size={16} />
                      <span>Sign Out</span>
                    </button>
                  </div>
                </div>
              )}
            </div>
          </div>
        </header>

        {/* Main Content Area */}
        <main className="flex-1 overflow-auto p-6">
          <Outlet />
        </main>
      </div>
    </div>
  )
}
