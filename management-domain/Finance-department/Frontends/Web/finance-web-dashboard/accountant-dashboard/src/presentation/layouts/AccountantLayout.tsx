import { useState } from 'react'
import { Outlet, NavLink, useLocation } from 'react-router-dom'
import {
  LayoutDashboard,
  BookOpen,
  ArrowLeftRight,
  FileText,
  CreditCard,
  Users,
  Settings,
  Bell,
  ChevronLeft,
  ChevronRight,
  Menu,
  LogOut,
  User,
  ExternalLink,
  X,
} from 'lucide-react'
import { useAccountantStore } from '@shared/store'
import { cn } from '@shared/utils/cn'

const navItems = [
  { path: '/dashboard', label: 'Dashboard', icon: LayoutDashboard },
  { path: '/journal-entries', label: 'Journal Entries', icon: BookOpen },
  { path: '/reconciliation', label: 'Reconciliation', icon: ArrowLeftRight },
  { path: '/invoices', label: 'Invoices', icon: FileText },
  { path: '/payments', label: 'Payments', icon: CreditCard },
  { path: '/vendors', label: 'Vendors', icon: Users },
  { path: '/settings', label: 'Settings', icon: Settings },
]

const relatedApps = [
  { label: 'HQ Finance Dashboard', url: 'http://localhost:3016' },
  { label: 'CFO Dashboard', url: 'http://localhost:3019' },
  { label: 'Reports Dashboard', url: 'http://localhost:3020' },
  { label: 'Finance Portal', url: 'http://localhost:3017' },
]

const pageTitles: Record<string, string> = {
  '/dashboard': 'Dashboard',
  '/journal-entries': 'Journal Entries',
  '/reconciliation': 'Reconciliation',
  '/invoices': 'Invoice Processing',
  '/payments': 'Payment Processing',
  '/vendors': 'Vendor Management',
  '/settings': 'Settings',
}

export default function AccountantLayout() {
  const { user, sidebarCollapsed, toggleSidebar, notifications, markNotificationRead, logout } = useAccountantStore()
  const location = useLocation()
  const [showNotifications, setShowNotifications] = useState(false)
  const [showUserMenu, setShowUserMenu] = useState(false)
  const [mobileMenuOpen, setMobileMenuOpen] = useState(false)

  const pageTitle = pageTitles[location.pathname] || 'Dashboard'
  const unreadCount = notifications.filter((n) => !n.read).length

  return (
    <div className="flex h-screen bg-gray-50 overflow-hidden">
      {mobileMenuOpen && (
        <div className="fixed inset-0 bg-black/50 z-40 lg:hidden" onClick={() => setMobileMenuOpen(false)} />
      )}

      <aside
        className={cn(
          'fixed lg:relative z-50 h-full bg-teal-900 text-white flex flex-col transition-all duration-300',
          sidebarCollapsed ? 'w-16' : 'w-60',
          mobileMenuOpen ? 'translate-x-0' : '-translate-x-full lg:translate-x-0'
        )}
      >
        <div className={cn('flex items-center h-16 px-4 border-b border-teal-800', sidebarCollapsed ? 'justify-center' : 'justify-between')}>
          {!sidebarCollapsed && (
            <div className="flex items-center gap-2">
              <div className="w-8 h-8 bg-teal-500 rounded-lg flex items-center justify-center font-bold text-sm">GA</div>
              <span className="font-semibold text-sm">Gogidix Accounting</span>
            </div>
          )}
          <button onClick={toggleSidebar} className="hidden lg:flex p-1 hover:bg-teal-800 rounded">
            {sidebarCollapsed ? <ChevronRight size={18} /> : <ChevronLeft size={18} />}
          </button>
        </div>

        <nav className="flex-1 py-4 overflow-y-auto">
          {navItems.map((item) => (
            <NavLink
              key={item.path}
              to={item.path}
              onClick={() => setMobileMenuOpen(false)}
              className={({ isActive }) =>
                cn(
                  'flex items-center gap-3 px-4 py-2.5 mx-2 rounded-lg text-sm transition-colors',
                  isActive ? 'bg-teal-700 text-white' : 'text-teal-100 hover:bg-teal-800',
                  sidebarCollapsed && 'justify-center px-0'
                )
              }
            >
              <item.icon size={20} />
              {!sidebarCollapsed && <span>{item.label}</span>}
            </NavLink>
          ))}
        </nav>

        {!sidebarCollapsed && (
          <div className="border-t border-teal-800 p-3">
            <p className="text-[10px] uppercase tracking-wider text-teal-400 mb-2 px-2">Related Apps</p>
            {relatedApps.map((app) => (
              <a
                key={app.url}
                href={app.url}
                target="_blank"
                rel="noopener noreferrer"
                className="flex items-center gap-2 px-2 py-1.5 text-xs text-teal-200 hover:text-white hover:bg-teal-800 rounded transition-colors"
              >
                <ExternalLink size={12} />
                <span>{app.label}</span>
              </a>
            ))}
          </div>
        )}
      </aside>

      <div className="flex-1 flex flex-col min-w-0">
        <header className="h-16 bg-white border-b border-gray-200 flex items-center justify-between px-4 lg:px-6 shrink-0">
          <div className="flex items-center gap-3">
            <button onClick={() => setMobileMenuOpen(true)} className="lg:hidden p-2 hover:bg-gray-100 rounded-lg">
              <Menu size={20} />
            </button>
            <h1 className="text-lg font-semibold text-gray-800">{pageTitle}</h1>
          </div>

          <div className="flex items-center gap-2">
            <div className="relative">
              <button
                onClick={() => { setShowNotifications(!showNotifications); setShowUserMenu(false) }}
                className="relative p-2 hover:bg-gray-100 rounded-lg transition-colors"
              >
                <Bell size={20} className="text-gray-600" />
                {unreadCount > 0 && (
                  <span className="absolute top-1 right-1 w-4 h-4 bg-red-500 text-white text-[10px] rounded-full flex items-center justify-center">
                    {unreadCount}
                  </span>
                )}
              </button>

              {showNotifications && (
                <div className="absolute right-0 top-12 w-80 bg-white rounded-xl shadow-xl border border-gray-200 z-50 max-h-96 overflow-y-auto">
                  <div className="flex items-center justify-between p-3 border-b border-gray-100">
                    <h3 className="font-semibold text-sm">Notifications</h3>
                    <button onClick={() => setShowNotifications(false)}>
                      <X size={16} className="text-gray-400" />
                    </button>
                  </div>
                  {notifications.length === 0 ? (
                    <p className="p-4 text-sm text-gray-500 text-center">No notifications</p>
                  ) : (
                    notifications.map((n) => (
                      <button
                        key={n.id}
                        onClick={() => markNotificationRead(n.id)}
                        className={cn(
                          'w-full text-left p-3 border-b border-gray-50 hover:bg-gray-50 transition-colors',
                          !n.read && 'bg-teal-50'
                        )}
                      >
                        <p className={cn('text-sm font-medium', !n.read ? 'text-gray-900' : 'text-gray-600')}>{n.title}</p>
                        <p className="text-xs text-gray-500 mt-0.5">{n.message}</p>
                      </button>
                    ))
                  )}
                </div>
              )}
            </div>

            <div className="relative">
              <button
                onClick={() => { setShowUserMenu(!showUserMenu); setShowNotifications(false) }}
                className="flex items-center gap-2 p-2 hover:bg-gray-100 rounded-lg transition-colors"
              >
                <div className="w-8 h-8 bg-teal-600 rounded-full flex items-center justify-center">
                  <User size={16} className="text-white" />
                </div>
                <span className="hidden sm:block text-sm font-medium text-gray-700">{user?.name}</span>
              </button>

              {showUserMenu && (
                <div className="absolute right-0 top-12 w-56 bg-white rounded-xl shadow-xl border border-gray-200 z-50">
                  <div className="p-3 border-b border-gray-100">
                    <p className="text-sm font-medium text-gray-900">{user?.name}</p>
                    <p className="text-xs text-gray-500">{user?.email}</p>
                    <p className="text-xs text-teal-600 mt-1">{user?.role}</p>
                  </div>
                  <button
                    onClick={() => { logout(); setShowUserMenu(false) }}
                    className="w-full flex items-center gap-2 px-3 py-2.5 text-sm text-red-600 hover:bg-red-50 rounded-b-xl transition-colors"
                  >
                    <LogOut size={16} />
                    Sign Out
                  </button>
                </div>
              )}
            </div>
          </div>
        </header>

        <main className="flex-1 overflow-y-auto p-4 lg:p-6">
          <Outlet />
        </main>
      </div>
    </div>
  )
}
