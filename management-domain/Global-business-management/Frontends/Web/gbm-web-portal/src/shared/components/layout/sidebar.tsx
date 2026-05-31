import { NavLink } from 'react-router-dom'
import {
  LayoutDashboard,
  Globe,
  Map,
  DollarSign,
  Database,
  FileText,
  BarChart3,
  Download,
  Languages,
  Settings,
  ChevronLeft,
  ChevronRight,
  Building2,
} from 'lucide-react'
import { cn } from '@shared/utils/cn'
import { Badge } from '@shared/components/ui/badge'
import * as React from 'react'

interface NavItem {
  title: string
  href: string
  icon: string
  badge?: number
}

interface SidebarProps {
  collapsed: boolean
  onCollapseChange: (collapsed: boolean) => void
  navItems: NavItem[]
}

const iconMap: Record<string, React.ComponentType<{ className?: string }>> = {
  LayoutDashboard,
  Globe,
  Map,
  DollarSign,
  Database,
  FileText,
  BarChart3,
  Download,
  Languages,
  Settings,
}

export function Sidebar({ collapsed, onCollapseChange, navItems }: SidebarProps) {
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
            <div className="flex h-8 w-8 items-center justify-center rounded bg-gradient-to-br from-[#1E88E5] to-[#43A047]">
              <span className="text-lg font-bold text-white">G</span>
            </div>
            <span className="text-lg font-semibold">GBM Portal</span>
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
          const Icon = iconMap[item.icon]
          return (
            <NavLink
              key={item.href}
              to={item.href}
              className={({ isActive }) =>
                cn(
                  'flex items-center gap-3 rounded-lg px-3 py-2 text-sm font-medium transition-colors',
                  isActive
                    ? 'bg-gradient-to-r from-[#1E88E5] to-[#43A047] text-white'
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
                    <Badge variant="destructive" className="ml-auto">
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
            <Building2 className="h-5 w-5 text-[#1E88E5]" />
            <div className="flex-1 text-xs">
              <p className="font-medium">Gogidix Global</p>
              <p className="text-muted-foreground">Business Management</p>
            </div>
          </div>
        </div>
      )}
    </aside>
  )
}
