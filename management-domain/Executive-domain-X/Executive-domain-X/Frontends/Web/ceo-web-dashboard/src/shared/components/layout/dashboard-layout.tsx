import * as React from 'react'
import { Outlet, useNavigate } from 'react-router-dom'
import { Layers } from 'lucide-react'
import { Header } from './header'
import { Sidebar } from './sidebar'
import { useAuthStore } from '@shared/stores/authStore'

interface NavItem {
  title: string
  href: string
  icon: string
  badge?: number
}

interface DashboardLayoutProps {
  role?: 'CEO' | 'CFO' | 'COO' | 'CTO'
}

export function DashboardLayout({ role = 'CEO' }: DashboardLayoutProps) {
  const [sidebarCollapsed, setSidebarCollapsed] = React.useState(false)
  const [mobileMenuOpen, setMobileMenuOpen] = React.useState(false)
  const navigate = useNavigate()
  const { user, logout } = useAuthStore()

  const handleLogout = () => {
    logout()
    navigate('/login')
  }

  const navItems = React.useMemo((): NavItem[] => {
    switch (role) {
      case 'CFO':
        return [
          { title: 'Overview', href: '/cfo', icon: 'LayoutDashboard' },
          { title: 'Budget', href: '/cfo/budget', icon: 'DollarSign' },
          { title: 'Financials', href: '/cfo/financials', icon: 'FileText' },
          { title: 'Forecast', href: '/cfo/forecast', icon: 'TrendingUp' },
          { title: 'Compliance', href: '/cfo/compliance', icon: 'Shield' },
          { title: 'Reports', href: '/cfo/reports', icon: 'FileText' },
          { title: 'Settings', href: '/cfo/settings', icon: 'Settings' },
        ]
      case 'COO':
        return [
          { title: 'Overview', href: '/coo', icon: 'LayoutDashboard' },
          { title: 'Operations', href: '/coo/operations', icon: 'Target' },
          { title: 'Service Health', href: '/coo/service-health', icon: 'BarChart3' },
          { title: 'Incidents', href: '/coo/incidents', icon: 'Shield', badge: 2 },
          { title: 'Resources', href: '/coo/resources', icon: 'BarChart3' },
          { title: 'Approvals', href: '/coo/approvals', icon: 'CheckCircle', badge: 8 },
          { title: 'Reports', href: '/coo/reports', icon: 'FileText' },
          { title: 'Settings', href: '/coo/settings', icon: 'Settings' },
        ]
      case 'CTO':
        return [
          { title: 'Overview', href: '/cto', icon: 'LayoutDashboard' },
          { title: 'Infrastructure', href: '/cto/infrastructure', icon: 'Target' },
          { title: 'Engineering', href: '/cto/engineering', icon: 'BarChart3' },
          { title: 'Service Health', href: '/cto/service-health', icon: 'BarChart3' },
          { title: 'Security', href: '/cto/security', icon: 'Shield' },
          { title: 'Roadmap', href: '/cto/roadmap', icon: 'TrendingUp' },
          { title: 'Users', href: '/cto/users', icon: 'Users' },
          { title: 'Approvals', href: '/cto/approvals', icon: 'CheckCircle', badge: 3 },
          { title: 'Reports', href: '/cto/reports', icon: 'FileText' },
          { title: 'Settings', href: '/cto/settings', icon: 'Settings' },
        ]
      case 'CEO':
      default:
        return [
          { title: 'Overview', href: '/', icon: 'LayoutDashboard' },
          { title: 'Strategy', href: '/strategy', icon: 'Target' },
          { title: 'Analytics', href: '/analytics', icon: 'BarChart3' },
          { title: 'Approvals', href: '/approvals', icon: 'CheckCircle', badge: 12 },
          { title: 'Reports', href: '/reports', icon: 'FileText' },
          { title: 'Settings', href: '/settings', icon: 'Settings' },
        ]
    }
  }, [role])

  return (
    <div className="flex h-screen overflow-hidden bg-slate-50">
      <div className="hidden md:block">
        <Sidebar
          collapsed={sidebarCollapsed}
          onCollapseChange={setSidebarCollapsed}
          navItems={navItems}
          role={role}
        />
      </div>

      {mobileMenuOpen && (
        <div
          className="fixed inset-0 z-40 bg-black/50 md:hidden"
          onClick={() => setMobileMenuOpen(false)}
        />
      )}

      <div
        className={`fixed inset-y-0 left-0 z-50 transition-transform duration-300 md:hidden ${
          mobileMenuOpen ? 'translate-x-0' : '-translate-x-full'
        }`}
      >
        <Sidebar
          collapsed={false}
          onCollapseChange={() => setMobileMenuOpen(false)}
          navItems={navItems}
          role={role}
        />
      </div>

      <div className="flex flex-1 flex-col overflow-hidden">
        <Header
          user={user}
          onMenuClick={() => setMobileMenuOpen(!mobileMenuOpen)}
          onLogout={handleLogout}
          role={role}
        />

        <main className="flex-1 overflow-y-auto p-4 md:p-6">
          <Outlet />
        </main>
      </div>
    </div>
  )
}
