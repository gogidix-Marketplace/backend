import * as React from 'react'
import { Outlet, useNavigate } from 'react-router-dom'
import { Header } from './header'
import { Sidebar } from './sidebar'
import { useAuthStore } from '@shared/stores/authStore'

export function DashboardLayout() {
  const [sidebarCollapsed, setSidebarCollapsed] = React.useState(false)
  const [mobileMenuOpen, setMobileMenuOpen] = React.useState(false)
  const navigate = useNavigate()
  const { user, logout, selectedCountry, setSelectedCountry, hasPermission } = useAuthStore()

  const handleLogout = () => {
    logout()
    navigate('/login')
  }

  const navItems = [
    { title: 'Overview', href: '/', icon: 'LayoutDashboard' },
    {
      title: 'Leads',
      href: '/leads',
      icon: 'Users',
      permission: 'view:own_leads',
    },
    {
      title: 'Opportunities',
      href: '/opportunities',
      icon: 'Briefcase',
      permission: 'manage:own_opportunities',
    },
    {
      title: 'Customers',
      href: '/customers',
      icon: 'Building2',
      permission: 'view:own_customers',
    },
    {
      title: 'Analytics',
      href: '/analytics',
      icon: 'BarChart3',
      permission: 'view:all_reports',
    },
    {
      title: 'Territories',
      href: '/territories',
      icon: 'Map',
      permission: 'manage:country_territories',
    },
    {
      title: 'Commission',
      href: '/commission',
      icon: 'DollarSign',
      permission: 'view:own_commission',
    },
    {
      title: 'Forecast',
      href: '/forecast',
      icon: 'LineChart',
      permission: 'view:forecasts',
    },
    {
      title: 'Communications',
      href: '/communications',
      icon: 'MessageSquare',
    },
    {
      title: 'Settings',
      href: '/settings',
      icon: 'Settings',
      permission: 'manage:settings',
    },
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
          selectedCountry={selectedCountry}
          onCountryChange={
            hasPermission('view:all_countries') ? setSelectedCountry : undefined
          }
        />

        {/* Page content */}
        <main className="flex-1 overflow-y-auto p-4 md:p-6">
          <Outlet />
        </main>
      </div>
    </div>
  )
}
