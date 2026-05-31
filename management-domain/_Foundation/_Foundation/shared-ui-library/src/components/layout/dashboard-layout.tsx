import * as React from 'react'
import { Outlet, useNavigate } from 'react-router-dom'
import { Header } from './header'
import { Sidebar } from './sidebar'
import type { User } from '@/types/common'
import type { NavItem } from './sidebar'

interface DashboardLayoutProps {
  user?: User | null
  department?: string
  navItems?: NavItem[]
  onLogout?: () => void
}

export function DashboardLayout({
  user,
  department,
  navItems,
  onLogout,
}: DashboardLayoutProps) {
  const [sidebarCollapsed, setSidebarCollapsed] = React.useState(false)
  const [mobileMenuOpen, setMobileMenuOpen] = React.useState(false)
  const navigate = useNavigate()

  const handleLogout = () => {
    onLogout?.()
    navigate('/login')
  }

  return (
    <div className="flex h-screen overflow-hidden bg-background">
      {/* Sidebar - Desktop */}
      <div className="hidden md:block">
        <Sidebar
          department={department}
          navItems={navItems}
          collapsed={sidebarCollapsed}
          onCollapseChange={setSidebarCollapsed}
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
        className={cn(
          'fixed inset-y-0 left-0 z-50 transition-transform duration-300 md:hidden',
          mobileMenuOpen ? 'translate-x-0' : '-translate-x-full'
        )}
      >
        <Sidebar
          department={department}
          navItems={navItems}
          collapsed={false}
          onCollapseChange={() => setMobileMenuOpen(false)}
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

// Import cn utility
import { cn } from '@/lib/utils'
