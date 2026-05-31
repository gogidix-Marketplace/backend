// ============================================
// DIGITAL MARKETING - LAYOUT COMPONENT
// ============================================

import { Outlet, useNavigate, useLocation } from 'react-router-dom'
import { useAuthStore } from '@shared/store'
import {
  LayoutDashboard,
  Mail,
  FileText,
  Share2,
  BarChart3,
  Settings,
  LogOut,
  User,
  Bell,
  Menu,
  X,
} from 'lucide-react'
import { useState } from 'react'
import { cn } from '@shared/utils'

interface NavItem {
  label: string
  path: string
  icon: React.ElementType
}

const NAV_ITEMS: NavItem[] = [
  { label: 'Dashboard', path: '/dashboard', icon: LayoutDashboard },
  { label: 'Campaigns', path: '/campaigns', icon: Mail },
  { label: 'Content', path: '/content', icon: FileText },
  { label: 'Social', path: '/social', icon: Share2 },
  { label: 'Analytics', path: '/analytics', icon: BarChart3 },
  { label: 'Settings', path: '/settings', icon: Settings },
]

export function MarketingLayout() {
  const navigate = useNavigate()
  const location = useLocation()
  const { user, logout } = useAuthStore()
  const [sidebarCollapsed, setSidebarCollapsed] = useState(false)
  const [mobileMenuOpen, setMobileMenuOpen] = useState(false)

  const currentPage = NAV_ITEMS.find((item) =>
    location.pathname.startsWith(item.path)
  )

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
              <h1 className="font-bold text-lg">Marketing</h1>
              <p className="text-xs text-slate-400">Digital Marketing</p>
            </div>
          )}
          <button
            onClick={() => setSidebarCollapsed(!sidebarCollapsed)}
            className="p-1 hover:bg-slate-800 rounded"
          >
            {sidebarCollapsed ? <Menu size={20} /> : <X size={20} />}
          </button>
        </div>

        {/* Navigation */}
        <nav className="flex-1 p-4 space-y-1">
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
                {!sidebarCollapsed && <span>{item.label}</span>}
              </button>
            )
          })}
        </nav>

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
                <h1 className="font-bold text-lg">Marketing</h1>
                <p className="text-xs text-slate-400">Digital Marketing</p>
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
                return (
                  <button
                    key={item.path}
                    onClick={() => handleNavigation(item.path)}
                    className="w-full flex items-center gap-3 px-3 py-2 rounded-lg text-slate-300 hover:bg-slate-800"
                  >
                    <Icon size={20} />
                    <span>{item.label}</span>
                  </button>
                )
              })}
            </nav>
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
              <h2 className="font-semibold text-lg">{currentPage?.label || 'Marketing Dashboard'}</h2>
              <p className="text-sm text-slate-500">Port 3050</p>
            </div>
          </div>

          <div className="flex items-center gap-2">
            <button className="p-2 hover:bg-slate-100 rounded-lg relative">
              <Bell size={20} className="text-slate-500" />
            </button>
            <button className="flex items-center gap-2 p-2 hover:bg-slate-100 rounded-lg">
              <div className="w-8 h-8 bg-blue-600 rounded-full flex items-center justify-center">
                <User size={16} className="text-white" />
              </div>
            </button>
          </div>
        </header>

        <main className="flex-1 overflow-auto p-6">
          <Outlet />
        </main>
      </div>
    </div>
  )
}
