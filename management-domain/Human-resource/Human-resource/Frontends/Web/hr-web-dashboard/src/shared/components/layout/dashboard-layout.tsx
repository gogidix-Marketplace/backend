import * as React from 'react'
import { Outlet, useNavigate } from 'react-router-dom'
import { Header } from './header'
import { Sidebar } from './sidebar'
import { useAuthStore } from '@shared/stores/authStore'

export function DashboardLayout() {
  const [sidebarCollapsed, setSidebarCollapsed] = React.useState(false)
  const [mobileMenuOpen, setMobileMenuOpen] = React.useState(false)
  const navigate = useNavigate()
  const { user, logout } = useAuthStore()

  const handleLogout = () => {
    logout()
    navigate('/login')
  }

  const navItems = [
    { title: 'Overview', href: '/', icon: 'LayoutDashboard' },
    { title: 'Employees', href: '/employees', icon: 'Users' },
    { title: 'Recruitment', href: '/recruitment', icon: 'Briefcase', badge: 12 },
    { title: 'Onboarding', href: '/onboarding', icon: 'UserPlus', badge: 5 },
    { title: 'Leave', href: '/leave', icon: 'Calendar', badge: 8 },
    { title: 'Payroll', href: '/payroll', icon: 'DollarSign' },
    { title: 'Performance', href: '/performance', icon: 'TrendingUp' },
    { title: 'Training', href: '/training', icon: 'BookOpen' },
    { title: 'Benefits', href: '/benefits', icon: 'Heart' },
    { title: 'Time & Attendance', href: '/attendance', icon: 'Clock' },
    { title: 'Compliance', href: '/compliance', icon: 'Shield' },
    { title: 'Approvals', href: '/approvals', icon: 'CheckCircle', badge: 3 },
    { title: 'Settings', href: '/settings', icon: 'Settings' },
  ]

  return (
    <div className="flex h-screen overflow-hidden bg-background">
      {/* Sidebar - Desktop */}
      <div className="hidden md:block">
        <Sidebar
          collapsed={sidebarCollapsed}
          onCollapseChange={setSidebarCollapsed}
          navItems={navItems}
        />
      </div>

      {/* Mobile sidebar overlay */}
      {mobileMenuOpen && (
        <div
          className="fixed inset-0 z-40 bg-black/50 md:hidden"
          onClick={() => setMobileMenuOpen(false)}
        />
      )}

      {/* Sidebar - Mobile */}
      <div
        className={`fixed inset-y-0 left-0 z-50 transition-transform duration-300 md:hidden ${
          mobileMenuOpen ? 'translate-x-0' : '-translate-x-full'
        }`}
      >
        <Sidebar
          collapsed={false}
          onCollapseChange={() => setMobileMenuOpen(false)}
          navItems={navItems}
        />
      </div>

      {/* Main content */}
      <div className="flex flex-1 flex-col overflow-hidden">
        <Header
          user={user}
          onMenuClick={() => setMobileMenuOpen(!mobileMenuOpen)}
          onLogout={handleLogout}
        />

        {/* Page content */}
        <main className="flex-1 overflow-y-auto p-4 md:p-6">
          <Outlet />
        </main>
      </div>
    </div>
  )
}
