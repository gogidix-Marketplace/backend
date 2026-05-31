import { useState } from 'react'
import { Outlet, NavLink, useNavigate } from 'react-router-dom'
import {
  LayoutDashboard,
  FileText,
  CalendarClock,
  Wand2,
  History,
  Settings,
  LogOut,
  Bell,
  ChevronDown,
  ExternalLink,
  Menu,
  X,
  FileBarChart,
} from 'lucide-react'
import { useReportsStore } from '@shared/store/reportsStore'
import { cn } from '@shared/utils/cn'

const NAV_ITEMS = [
  { to: '/dashboard', label: 'Dashboard', icon: LayoutDashboard },
  { to: '/templates', label: 'Templates', icon: FileText },
  { to: '/scheduled', label: 'Scheduled', icon: CalendarClock },
  { to: '/generate', label: 'Generate Report', icon: Wand2 },
  { to: '/history', label: 'History', icon: History },
  { to: '/settings', label: 'Settings', icon: Settings },
]

const CROSS_APP_LINKS = [
  { label: 'HQ Dashboard', url: 'http://localhost:3016' },
  { label: 'Accountant Portal', url: 'http://localhost:3018' },
  { label: 'CFO Dashboard', url: 'http://localhost:3019' },
]

export default function ReportsLayout() {
  const { user, logout, notification, setNotification } = useReportsStore()
  const navigate = useNavigate()
  const [sidebarOpen, setSidebarOpen] = useState(false)
  const [userMenuOpen, setUserMenuOpen] = useState(false)

  const handleLogout = () => {
    logout()
    navigate('/login')
  }

  return (
    <div className="flex h-screen bg-gray-50">
      {notification && (
        <div className="fixed top-4 right-4 z-[100] bg-amber-600 text-white px-4 py-3 rounded-lg shadow-lg flex items-center gap-3">
          <span>{notification}</span>
          <button onClick={() => setNotification(null)} className="hover:text-amber-200">
            <X size={16} />
          </button>
        </div>
      )}

      <aside
        className={cn(
          'fixed inset-y-0 left-0 z-50 w-64 bg-amber-900 text-white transform transition-transform duration-200 ease-in-out lg:relative lg:translate-x-0',
          sidebarOpen ? 'translate-x-0' : '-translate-x-full'
        )}
      >
        <div className="flex items-center gap-3 px-6 py-5 border-b border-amber-800">
          <FileBarChart size={28} className="text-amber-300" />
          <div>
            <h1 className="text-lg font-bold">Reports</h1>
            <p className="text-xs text-amber-300">Finance Dashboard</p>
          </div>
          <button
            className="ml-auto lg:hidden text-amber-300 hover:text-white"
            onClick={() => setSidebarOpen(false)}
          >
            <X size={20} />
          </button>
        </div>

        <nav className="flex-1 px-3 py-4 space-y-1 overflow-y-auto">
          {NAV_ITEMS.map(({ to, label, icon: Icon }) => (
            <NavLink
              key={to}
              to={to}
              onClick={() => setSidebarOpen(false)}
              className={({ isActive }) =>
                cn(
                  'flex items-center gap-3 px-3 py-2.5 rounded-lg text-sm font-medium transition-colors',
                  isActive
                    ? 'bg-amber-800 text-white'
                    : 'text-amber-100 hover:bg-amber-800/60 hover:text-white'
                )
              }
            >
              <Icon size={18} />
              {label}
            </NavLink>
          ))}

          <div className="pt-4 mt-4 border-t border-amber-800">
            <p className="px-3 text-xs font-semibold text-amber-400 uppercase tracking-wider mb-2">
              Other Finance Apps
            </p>
            {CROSS_APP_LINKS.map(({ label, url }) => (
              <a
                key={url}
                href={url}
                target="_blank"
                rel="noopener noreferrer"
                className="flex items-center gap-3 px-3 py-2 rounded-lg text-sm text-amber-200 hover:bg-amber-800/60 hover:text-white transition-colors"
              >
                <ExternalLink size={16} />
                {label}
              </a>
            ))}
          </div>
        </nav>

        <div className="px-3 py-4 border-t border-amber-800">
          <div className="flex items-center gap-3 px-3">
            <div className="w-8 h-8 rounded-full bg-amber-600 flex items-center justify-center text-sm font-bold">
              {user?.name?.charAt(0) ?? 'U'}
            </div>
            <div className="flex-1 min-w-0">
              <p className="text-sm font-medium truncate">{user?.name}</p>
              <p className="text-xs text-amber-300 truncate">{user?.role}</p>
            </div>
          </div>
        </div>
      </aside>

      <div className="flex-1 flex flex-col min-w-0">
        <header className="bg-white border-b border-gray-200 px-4 lg:px-6 py-3 flex items-center gap-4">
          <button
            className="lg:hidden text-gray-600 hover:text-gray-900"
            onClick={() => setSidebarOpen(true)}
          >
            <Menu size={24} />
          </button>

          <div className="flex-1" />

          <button className="relative p-2 text-gray-500 hover:text-gray-700 rounded-lg hover:bg-gray-100">
            <Bell size={20} />
            <span className="absolute top-1 right-1 w-2 h-2 bg-amber-500 rounded-full" />
          </button>

          <div className="relative">
            <button
              onClick={() => setUserMenuOpen(!userMenuOpen)}
              className="flex items-center gap-2 px-3 py-1.5 rounded-lg hover:bg-gray-100 transition-colors"
            >
              <div className="w-8 h-8 rounded-full bg-amber-600 flex items-center justify-center text-white text-sm font-bold">
                {user?.name?.charAt(0) ?? 'U'}
              </div>
              <span className="hidden sm:block text-sm font-medium text-gray-700">{user?.name}</span>
              <ChevronDown size={16} className="text-gray-400" />
            </button>

            {userMenuOpen && (
              <>
                <div className="fixed inset-0 z-30" onClick={() => setUserMenuOpen(false)} />
                <div className="absolute right-0 mt-2 w-48 bg-white rounded-lg shadow-lg border border-gray-200 py-1 z-40">
                  <div className="px-4 py-2 border-b border-gray-100">
                    <p className="text-sm font-medium text-gray-900">{user?.name}</p>
                    <p className="text-xs text-gray-500">{user?.email}</p>
                  </div>
                  <button
                    onClick={handleLogout}
                    className="w-full flex items-center gap-2 px-4 py-2 text-sm text-red-600 hover:bg-red-50"
                  >
                    <LogOut size={16} />
                    Sign Out
                  </button>
                </div>
              </>
            )}
          </div>
        </header>

        <main className="flex-1 overflow-y-auto p-4 lg:p-6">
          <Outlet />
        </main>
      </div>
    </div>
  )
}
