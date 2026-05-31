import { Outlet, useNavigate, useLocation } from 'react-router-dom'
import { useAuthStore } from '@shared/store/portalStore'
import {
  Home,
  Receipt,
  PlusCircle,
  FileText,
  CreditCard,
  User,
  LogOut,
  Menu,
  X,
  Bell,
  ChevronDown,
  ExternalLink,
} from 'lucide-react'
import { useState } from 'react'
import { cn } from '@shared/utils/cn'

const NAV_ITEMS = [
  { label: 'Dashboard', path: '/dashboard', icon: Home },
  { label: 'My Expenses', path: '/my-expenses', icon: Receipt },
  { label: 'Submit Expense', path: '/submit', icon: PlusCircle },
  { label: 'My Invoices', path: '/my-invoices', icon: FileText },
  { label: 'Payment Status', path: '/payment-status', icon: CreditCard },
  { label: 'Profile', path: '/profile', icon: User },
]

const FINANCE_APPS = [
  { label: 'HQ Finance Dashboard', url: 'http://localhost:3016', color: 'text-blue-600' },
  { label: 'Accountant Tools', url: 'http://localhost:3018', color: 'text-teal-600' },
  { label: 'CFO Dashboard', url: 'http://localhost:3019', color: 'text-indigo-600' },
  { label: 'Reports Hub', url: 'http://localhost:3020', color: 'text-amber-600' },
]

export function PortalLayout() {
  const navigate = useNavigate()
  const location = useLocation()
  const { employee, logout } = useAuthStore()
  const [mobileMenuOpen, setMobileMenuOpen] = useState(false)
  const [userMenuOpen, setUserMenuOpen] = useState(false)

  const handleLogout = () => {
    logout()
    navigate('/login')
  }

  const currentPage = NAV_ITEMS.find(item => location.pathname.startsWith(item.path))

  return (
    <div className="min-h-screen bg-slate-50 flex">
      <aside className="hidden md:flex flex-col w-64 bg-white border-r border-slate-200">
        <div className="p-4 border-b border-slate-200">
          <div className="flex items-center gap-2">
            <div className="w-8 h-8 bg-sky-600 rounded-lg flex items-center justify-center">
              <Receipt size={16} className="text-white" />
            </div>
            <div>
              <h1 className="font-bold text-slate-900 text-sm">Finance Portal</h1>
              <p className="text-xs text-slate-500">Employee Self-Service</p>
            </div>
          </div>
        </div>

        <nav className="flex-1 p-3 space-y-1 overflow-y-auto">
          {NAV_ITEMS.map(item => {
            const Icon = item.icon
            const isActive = location.pathname === item.path || location.pathname.startsWith(item.path + '/')
            return (
              <button
                key={item.path}
                onClick={() => navigate(item.path)}
                className={cn(
                  'w-full flex items-center gap-3 px-3 py-2 rounded-lg text-sm transition-colors',
                  isActive
                    ? 'bg-sky-50 text-sky-700 font-medium'
                    : 'text-slate-600 hover:bg-slate-50'
                )}
              >
                <Icon size={18} />
                <span>{item.label}</span>
              </button>
            )
          })}
        </nav>

        <div className="p-3 border-t border-slate-200">
          <p className="text-xs text-slate-400 uppercase tracking-wider mb-2 px-3 font-medium">Internal Tools</p>
          {FINANCE_APPS.map(app => (
            <a
              key={app.url}
              href={app.url}
              target="_blank"
              rel="noopener noreferrer"
              className="flex items-center gap-2 px-3 py-1.5 text-xs rounded-lg hover:bg-slate-50 transition-colors text-slate-500 hover:text-slate-700"
            >
              <ExternalLink size={12} className={app.color} />
              {app.label}
            </a>
          ))}
        </div>

        <div className="p-3 border-t border-slate-200">
          {employee && (
            <div className="flex items-center gap-3 px-3 py-2">
              <div className="w-8 h-8 bg-slate-200 rounded-full flex items-center justify-center">
                <User size={14} className="text-slate-600" />
              </div>
              <div className="flex-1 min-w-0">
                <p className="text-sm font-medium text-slate-900 truncate">{employee.displayName}</p>
                <p className="text-xs text-slate-500 truncate">{employee.department}</p>
              </div>
            </div>
          )}
          <button
            onClick={handleLogout}
            className="w-full flex items-center gap-2 px-3 py-2 text-sm text-slate-600 hover:bg-red-50 hover:text-red-600 rounded-lg transition-colors"
          >
            <LogOut size={16} />
            Sign Out
          </button>
        </div>
      </aside>

      {mobileMenuOpen && (
        <div className="fixed inset-0 z-50 md:hidden">
          <div className="fixed inset-0 bg-black/50" onClick={() => setMobileMenuOpen(false)} />
          <div className="fixed left-0 top-0 bottom-0 w-64 bg-white">
            <div className="p-4 border-b border-slate-200 flex items-center justify-between">
              <h1 className="font-bold text-sm">Finance Portal</h1>
              <button onClick={() => setMobileMenuOpen(false)}>
                <X size={20} />
              </button>
            </div>
            <nav className="p-3 space-y-1">
              {NAV_ITEMS.map(item => {
                const Icon = item.icon
                return (
                  <button
                    key={item.path}
                    onClick={() => { navigate(item.path); setMobileMenuOpen(false) }}
                    className="w-full flex items-center gap-3 px-3 py-2 rounded-lg text-sm text-slate-600 hover:bg-slate-50"
                  >
                    <Icon size={18} />
                    {item.label}
                  </button>
                )
              })}
            </nav>
          </div>
        </div>
      )}

      <div className="flex-1 flex flex-col overflow-hidden">
        <header className="bg-white border-b border-slate-200 px-4 py-3 flex items-center justify-between">
          <div className="flex items-center gap-4">
            <button onClick={() => setMobileMenuOpen(true)} className="md:hidden p-2 hover:bg-slate-100 rounded-lg">
              <Menu size={24} />
            </button>
            <h2 className="font-semibold text-lg">{currentPage?.label || 'Finance Portal'}</h2>
          </div>
          <div className="flex items-center gap-3">
            <button className="p-2 hover:bg-slate-100 rounded-lg relative">
              <Bell size={20} className="text-slate-600" />
            </button>
            <div className="relative">
              <button onClick={() => setUserMenuOpen(!userMenuOpen)} className="flex items-center gap-2 p-1.5 hover:bg-slate-100 rounded-lg">
                <div className="w-8 h-8 bg-sky-600 rounded-full flex items-center justify-center">
                  <User size={14} className="text-white" />
                </div>
                <ChevronDown size={14} className="text-slate-500" />
              </button>
              {userMenuOpen && (
                <div className="absolute right-0 mt-2 w-48 bg-white rounded-lg shadow-lg border border-slate-200 z-50">
                  <div className="p-3 border-b border-slate-200">
                    <p className="font-medium text-sm">{employee?.displayName}</p>
                    <p className="text-xs text-slate-500">{employee?.email}</p>
                  </div>
                  <div className="p-2">
                    <button onClick={() => { navigate('/profile'); setUserMenuOpen(false) }} className="w-full text-left px-3 py-2 hover:bg-slate-100 rounded-lg text-sm flex items-center gap-2">
                      <User size={14} /> Profile
                    </button>
                    <button onClick={handleLogout} className="w-full text-left px-3 py-2 hover:bg-red-50 text-red-600 rounded-lg text-sm flex items-center gap-2">
                      <LogOut size={14} /> Sign Out
                    </button>
                  </div>
                </div>
              )}
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
