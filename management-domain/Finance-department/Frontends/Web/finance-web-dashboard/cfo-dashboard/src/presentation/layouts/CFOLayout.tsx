import { useState } from 'react'
import { Outlet, NavLink, useNavigate } from 'react-router-dom'
import {
  LayoutDashboard,
  CheckSquare,
  Target,
  ShieldAlert,
  Users,
  Settings,
  LogOut,
  Bell,
  Menu,
  X,
  ChevronLeft,
  ChevronRight,
  ExternalLink,
  DollarSign,
} from 'lucide-react'
import { useCfoStore } from '@shared/store'
import { cn } from '@shared/utils'

const navItems = [
  { to: '/dashboard', icon: LayoutDashboard, label: 'Strategic Dashboard' },
  { to: '/approvals', icon: CheckSquare, label: 'Approvals' },
  { to: '/financial-strategy', icon: Target, label: 'Financial Strategy' },
  { to: '/risk-management', icon: ShieldAlert, label: 'Risk Management' },
  { to: '/investor-relations', icon: Users, label: 'Investor Relations' },
  { to: '/settings', icon: Settings, label: 'Settings' },
]

const crossAppLinks = [
  { label: 'HQ Finance Dashboard', url: 'http://localhost:3016', color: 'text-blue-400' },
  { label: 'Accountant Dashboard', url: 'http://localhost:3018', color: 'text-emerald-400' },
  { label: 'Reports Dashboard', url: 'http://localhost:3020', color: 'text-amber-400' },
]

export default function CFOLayout() {
  const [sidebarOpen, setSidebarOpen] = useState(false)
  const [collapsed, setCollapsed] = useState(false)
  const { user, logout } = useCfoStore()
  const navigate = useNavigate()

  const handleLogout = () => {
    logout()
    navigate('/login')
  }

  return (
    <div className="flex h-screen bg-gray-50">
      {sidebarOpen && (
        <div
          className="fixed inset-0 bg-black/50 z-20 lg:hidden"
          onClick={() => setSidebarOpen(false)}
        />
      )}

      <aside
        className={cn(
          'fixed lg:static inset-y-0 left-0 z-30 flex flex-col bg-indigo-950 text-white transition-all duration-300',
          collapsed ? 'w-20' : 'w-64',
          sidebarOpen ? 'translate-x-0' : '-translate-x-full lg:translate-x-0'
        )}
      >
        <div className={cn('flex items-center h-16 px-4 border-b border-indigo-900', collapsed ? 'justify-center' : 'justify-between')}>
          {!collapsed && (
            <div className="flex items-center gap-2">
              <DollarSign className="w-7 h-7 text-indigo-300" />
              <div>
                <h1 className="text-base font-bold text-white leading-tight">CFO Dashboard</h1>
                <p className="text-[10px] text-indigo-300 leading-tight">Gogidix Finance</p>
              </div>
            </div>
          )}
          {collapsed && <DollarSign className="w-7 h-7 text-indigo-300" />}
          <button
            onClick={() => setCollapsed(!collapsed)}
            className="hidden lg:flex items-center justify-center w-7 h-7 rounded hover:bg-indigo-900 transition-colors"
          >
            {collapsed ? <ChevronRight className="w-4 h-4" /> : <ChevronLeft className="w-4 h-4" />}
          </button>
        </div>

        <nav className="flex-1 py-4 px-3 space-y-1 overflow-y-auto">
          {navItems.map((item) => (
            <NavLink
              key={item.to}
              to={item.to}
              onClick={() => setSidebarOpen(false)}
              className={({ isActive }) =>
                cn(
                  'flex items-center gap-3 px-3 py-2.5 rounded-lg text-sm font-medium transition-colors',
                  isActive
                    ? 'bg-indigo-800 text-white'
                    : 'text-indigo-200 hover:bg-indigo-900 hover:text-white',
                  collapsed && 'justify-center px-0'
                )
              }
            >
              <item.icon className="w-5 h-5 flex-shrink-0" />
              {!collapsed && <span>{item.label}</span>}
            </NavLink>
          ))}
        </nav>

        <div className={cn('border-t border-indigo-900 p-3', collapsed && 'px-2')}>
          {!collapsed && (
            <div className="mb-3">
              <p className="text-[10px] font-semibold text-indigo-400 uppercase tracking-wider mb-2 px-2">
                Finance Apps
              </p>
              {crossAppLinks.map((link) => (
                <a
                  key={link.url}
                  href={link.url}
                  target="_blank"
                  rel="noopener noreferrer"
                  className={cn(
                    'flex items-center gap-2 px-2 py-1.5 text-xs rounded hover:bg-indigo-900 transition-colors',
                    link.color
                  )}
                >
                  <ExternalLink className="w-3 h-3 flex-shrink-0" />
                  <span>{link.label}</span>
                </a>
              ))}
            </div>
          )}
          {collapsed && (
            <div className="space-y-1 mb-3">
              {crossAppLinks.map((link) => (
                <a
                  key={link.url}
                  href={link.url}
                  target="_blank"
                  rel="noopener noreferrer"
                  className="flex items-center justify-center py-1.5 text-indigo-400 hover:text-indigo-200 transition-colors"
                  title={link.label}
                >
                  <ExternalLink className="w-4 h-4" />
                </a>
              ))}
            </div>
          )}
          <button
            onClick={handleLogout}
            className={cn(
              'flex items-center gap-2 w-full px-3 py-2 rounded-lg text-sm text-indigo-300 hover:bg-indigo-900 hover:text-white transition-colors',
              collapsed && 'justify-center px-0'
            )}
          >
            <LogOut className="w-4 h-4 flex-shrink-0" />
            {!collapsed && <span>Logout</span>}
          </button>
        </div>
      </aside>

      <div className="flex-1 flex flex-col min-w-0">
        <header className="flex items-center justify-between h-16 px-6 bg-white border-b border-gray-200">
          <button
            onClick={() => setSidebarOpen(true)}
            className="lg:hidden p-2 rounded-md hover:bg-gray-100"
          >
            {sidebarOpen ? <X className="w-5 h-5" /> : <Menu className="w-5 h-5" />}
          </button>

          <div className="hidden lg:block">
            <h2 className="text-lg font-semibold text-gray-800">Executive Financial Oversight</h2>
          </div>

          <div className="flex items-center gap-4">
            <button className="relative p-2 rounded-lg hover:bg-gray-100 transition-colors">
              <Bell className="w-5 h-5 text-gray-600" />
              <span className="absolute top-1 right-1 w-2 h-2 bg-red-500 rounded-full" />
            </button>

            <div className="flex items-center gap-3 pl-4 border-l border-gray-200">
              <div className="w-9 h-9 rounded-full bg-indigo-600 flex items-center justify-center text-white font-semibold text-sm">
                {user?.name?.charAt(0) ?? 'C'}
              </div>
              <div className="hidden sm:block">
                <p className="text-sm font-medium text-gray-800">{user?.name}</p>
                <p className="text-xs text-gray-500">{user?.role}</p>
              </div>
            </div>
          </div>
        </header>

        <main className="flex-1 overflow-auto p-6">
          <Outlet />
        </main>
      </div>
    </div>
  )
}
