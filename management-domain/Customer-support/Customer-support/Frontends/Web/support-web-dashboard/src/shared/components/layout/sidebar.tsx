import { NavLink, useLocation } from 'react-router-dom'
import {
  LayoutDashboard,
  Ticket,
  Users,
  MessageSquare,
  Phone,
  BookOpen,
  AlertTriangle,
  ClipboardCheck,
  BarChart3,
  Settings,
  ChevronLeft,
  ChevronRight,
  Headphones,
  Globe,
} from 'lucide-react'
import { cn } from '@shared/utils/cn'
import { Badge } from '@shared/components/ui/badge'
import { useAuthStore } from '@shared/stores/authStore'
import * as React from 'react'

interface NavItem {
  title: string
  href: string
  icon: string
  badge?: number
  requiredPermission?: string
  requiredRole?: string
}

interface SidebarProps {
  collapsed: boolean
  onCollapseChange: (collapsed: boolean) => void
}

const iconMap: Record<string, React.ComponentType<{ className?: string }>> = {
  LayoutDashboard,
  Ticket,
  Users,
  MessageSquare,
  Phone,
  BookOpen,
  AlertTriangle,
  ClipboardCheck,
  BarChart3,
  Settings,
  Headphones,
  Globe,
}

export function Sidebar({ collapsed, onCollapseChange }: SidebarProps) {
  const { user, currentView, selectedCountry } = useAuthStore()
  const location = useLocation()

  // Define navigation items based on user role
  const getNavItems = (): NavItem[] => {
    const baseItems: NavItem[] = [
      { title: 'Overview', href: '/', icon: 'LayoutDashboard' },
    ]

    if (user?.role === 'CUSTOMER') {
      return [
        ...baseItems,
        { title: 'My Tickets', href: '/my-tickets', icon: 'Ticket' },
        { title: 'New Ticket', href: '/new-ticket', icon: 'Ticket' },
        { title: 'Knowledge Base', href: '/knowledge', icon: 'BookOpen' },
        { title: 'Live Chat', href: '/chat', icon: 'MessageSquare', badge: 1 },
      ]
    }

    if (user?.role === 'SUPPORT_AGENT' || user?.role === 'SPECIALIST_AGENT') {
      return [
        ...baseItems,
        { title: 'My Queue', href: '/queue', icon: 'Ticket', badge: 5 },
        { title: 'All Tickets', href: '/tickets', icon: 'Ticket' },
        { title: 'Customers', href: '/customers', icon: 'Users' },
        { title: 'Live Chat', href: '/chat', icon: 'MessageSquare', badge: 2 },
        { title: 'Knowledge Base', href: '/knowledge', icon: 'BookOpen' },
      ]
    }

    if (user?.role === 'SUPPORT_LEAD' || user?.role === 'ADMIN') {
      return [
        ...baseItems,
        { title: 'Tickets', href: '/tickets', icon: 'Ticket', badge: 23 },
        { title: 'Teams', href: '/teams', icon: 'Users' },
        { title: 'Customers', href: '/customers', icon: 'Users' },
        { title: 'Live Chat', href: '/chat', icon: 'MessageSquare', badge: 3 },
        { title: 'Phone Support', href: '/phone', icon: 'Phone' },
        { title: 'Knowledge Base', href: '/knowledge', icon: 'BookOpen' },
        { title: 'SLA Management', href: '/sla', icon: 'AlertTriangle', badge: 4 },
        { title: 'Quality Assurance', href: '/quality', icon: 'ClipboardCheck' },
        { title: 'Analytics', href: '/analytics', icon: 'BarChart3' },
        { title: 'Settings', href: '/settings', icon: 'Settings' },
      ]
    }

    return baseItems
  }

  const navItems = getNavItems()

  // Check if user has required permission
  const hasAccess = (item: NavItem) => {
    if (!item.requiredPermission) return true
    return user?.permissions.includes(item.requiredPermission)
  }

  const countryNames: Record<string, string> = {
    US: 'United States',
    NG: 'Nigeria',
    KE: 'Kenya',
    ZA: 'South Africa',
    GH: 'Ghana',
    GB: 'United Kingdom',
    CA: 'Canada',
    EG: 'Egypt',
  }

  return (
    <aside
      className={cn(
        'flex flex-col border-r bg-slate-50 dark:bg-slate-900 transition-all duration-300',
        collapsed ? 'w-16' : 'w-64'
      )}
    >
      {/* Logo */}
      <div className="flex h-16 items-center justify-between border-b px-4">
        {!collapsed && (
          <div className="flex items-center gap-2">
            <div className="flex h-8 w-8 items-center justify-center rounded bg-[#1976D2]">
              <Headphones className="h-5 w-5 text-white" />
            </div>
            <div>
              <span className="text-sm font-semibold">Support</span>
              {currentView === 'country' && selectedCountry && (
                <div className="flex items-center gap-1 text-xs text-muted-foreground">
                  <Globe className="h-3 w-3" />
                  {countryNames[selectedCountry] || selectedCountry}
                </div>
              )}
            </div>
          </div>
        )}
        <button
          onClick={() => onCollapseChange(!collapsed)}
          className="rounded-md p-1 hover:bg-slate-200 dark:hover:bg-slate-800"
        >
          {collapsed ? (
            <ChevronRight className="h-5 w-5" />
          ) : (
            <ChevronLeft className="h-5 w-5" />
          )}
        </button>
      </div>

      {/* Navigation */}
      <nav className="flex-1 space-y-1 overflow-y-auto p-2">
        {navItems.map((item) => {
          if (!hasAccess(item)) return null

          const Icon = iconMap[item.icon]
          const isActive = location.pathname === item.href

          return (
            <NavLink
              key={item.href}
              to={item.href}
              className={({ isActive: navIsActive }) =>
                cn(
                  'flex items-center gap-3 rounded-lg px-3 py-2 text-sm font-medium transition-colors',
                  navIsActive || isActive
                    ? 'bg-[#1976D2] text-white'
                    : 'text-slate-700 hover:bg-slate-200 dark:text-slate-300 dark:hover:bg-slate-800',
                  collapsed && 'justify-center px-2'
                )
              }
              end={item.href === '/'}
            >
              <Icon className={cn('h-5 w-5 flex-shrink-0')} />
              {!collapsed && (
                <>
                  <span className="flex-1">{item.title}</span>
                  {item.badge && (
                    <Badge
                      variant={isActive ? 'secondary' : 'destructive'}
                      className="ml-auto"
                    >
                      {item.badge}
                    </Badge>
                  )}
                </>
              )}
            </NavLink>
          )
        })}
      </nav>

      {/* Footer */}
      {!collapsed && (
        <div className="border-t p-4">
          <div className="flex items-center gap-3 rounded-lg bg-white dark:bg-slate-800 p-3 shadow-sm">
            <Headphones className="h-5 w-5 text-[#1976D2]" />
            <div className="flex-1 text-xs">
              <p className="font-medium">Gogidix Support</p>
              <p className="text-muted-foreground">
                {currentView === 'global' ? 'Global Dashboard' : 'Country View'}
              </p>
            </div>
          </div>
        </div>
      )}
    </aside>
  )
}
