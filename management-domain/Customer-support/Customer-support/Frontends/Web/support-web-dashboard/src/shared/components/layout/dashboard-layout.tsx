import * as React from 'react'
import { Outlet, useNavigate } from 'react-router-dom'
import { Header } from './header'
import { Sidebar } from './sidebar'
import { useAuthStore } from '@shared/stores/authStore'

interface DashboardLayoutProps {
  children?: React.ReactNode
}

export function DashboardLayout({ children }: DashboardLayoutProps) {
  const [sidebarCollapsed, setSidebarCollapsed] = React.useState(false)
  const [mobileMenuOpen, setMobileMenuOpen] = React.useState(false)
  const navigate = useNavigate()
  const { user, logout, currentView, selectedCountry } = useAuthStore()

  const handleLogout = () => {
    logout()
    navigate('/login')
  }

  const handleViewChange = (view: 'global' | 'country') => {
    const { setCurrentView } = useAuthStore.getState()
    setCurrentView(view)
  }

  // Mock notification count
  const notificationCount = React.useMemo(() => {
    if (user?.role === 'SUPPORT_LEAD' || user?.role === 'ADMIN') return 7
    if (user?.role === 'SUPPORT_AGENT') return 3
    return 1
  }, [user?.role])

  const showViewToggle = (user?.role === 'SUPPORT_LEAD' || user?.role === 'ADMIN') && !user?.country

  return (
    <div className="flex h-screen overflow-hidden bg-background">
      {/* Sidebar - Desktop */}
      <div className="hidden md:block">
        <Sidebar
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
        className={`fixed inset-y-0 left-0 z-50 transition-transform duration-300 md:hidden ${
          mobileMenuOpen ? 'translate-x-0' : '-translate-x-full'
        }`}
      >
        <Sidebar
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
          showViewToggle={showViewToggle}
          currentView={currentView}
          selectedCountry={selectedCountry}
          onViewChange={handleViewChange}
          notificationCount={notificationCount}
        />

        {/* Page content */}
        <main className="flex-1 overflow-y-auto p-4 md:p-6">
          {children || <Outlet />}
        </main>
      </div>
    </div>
  )
}
