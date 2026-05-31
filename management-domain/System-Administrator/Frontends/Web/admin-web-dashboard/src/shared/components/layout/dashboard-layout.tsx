import * as React from 'react'
import { Outlet, useNavigate } from 'react-router-dom'
import { Header } from './header'
import { Sidebar } from './sidebar'
import { useAuthStore } from '@shared/stores/authStore'

interface NavItem {
  title: string
  href: string
  icon: string
  badge?: number
  permissions?: string[]
}

export function DashboardLayout() {
  const [sidebarCollapsed, setSidebarCollapsed] = React.useState(false)
  const [mobileMenuOpen, setMobileMenuOpen] = React.useState(false)
  const navigate = useNavigate()
  const { user, logout, hasPermission } = useAuthStore()

  const handleLogout = () => {
    logout()
    navigate('/login')
  }

  const navItems: NavItem[] = [
    { title: 'Overview', href: '/', icon: 'LayoutDashboard' },
    { title: 'Access Control', href: '/access-control', icon: 'Users', permissions: ['view:users'] },
    { title: 'Access Requests', href: '/access-requests', icon: 'Lock', badge: 7, permissions: ['view:access_requests'] },
    { title: 'Incidents', href: '/incidents', icon: 'AlertTriangle', badge: 2, permissions: ['view:incidents'] },
    { title: 'Infrastructure', href: '/infrastructure', icon: 'Server', permissions: ['view:monitoring'] },
    { title: 'Security', href: '/security', icon: 'Shield', badge: 3, permissions: ['view:security'] },
    { title: 'Deployments', href: '/deployments', icon: 'Activity', permissions: ['view:deployments'] },
    { title: 'Compliance', href: '/compliance', icon: 'FileCheck', permissions: ['view:compliance'] },
    { title: 'User Provisioning', href: '/provisioning', icon: 'UserPlus', permissions: ['provision:users'] },
    { title: 'Settings', href: '/settings', icon: 'Settings' },
  ]

  // Filter nav items based on user permissions
  const filteredNavItems = navItems.filter(item => {
    if (!item.permissions || item.permissions.length === 0) return true
    return item.permissions.some(permission => hasPermission(permission as any))
  })

  return (
    <div className="flex h-screen overflow-hidden bg-background">
      {/* Sidebar - Desktop */}
      <div className="hidden md:block">
        <Sidebar
          collapsed={sidebarCollapsed}
          onCollapseChange={setSidebarCollapsed}
          navItems={filteredNavItems}
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
          navItems={filteredNavItems}
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
