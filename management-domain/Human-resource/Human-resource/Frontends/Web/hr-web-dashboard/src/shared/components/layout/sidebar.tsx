import { NavLink } from 'react-router-dom'
import {
  LayoutDashboard,
  Users,
  UserPlus,
  Calendar,
  DollarSign,
  TrendingUp,
  BookOpen,
  Heart,
  Clock,
  Shield,
  FileText,
  Settings,
  ChevronLeft,
  ChevronRight,
  Briefcase,
  CheckCircle,
  Building2,
  Globe,
} from 'lucide-react'
import { cn } from '@shared/utils/cn'
import { Badge } from '../ui/badge'
import * as React from 'react'

interface NavItem {
  title: string
  href: string
  icon: string
  badge?: number
  requiredPermission?: string
}

interface SidebarProps {
  collapsed: boolean
  onCollapseChange: (collapsed: boolean) => void
  navItems?: NavItem[]
}

const iconMap: Record<string, React.ComponentType<{ className?: string }>> = {
  LayoutDashboard,
  Users,
  UserPlus,
  Calendar,
  DollarSign,
  TrendingUp,
  BookOpen,
  Heart,
  Clock,
  Shield,
  FileText,
  Settings,
  Briefcase,
  CheckCircle,
}

const defaultNavItems: NavItem[] = [
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

export function Sidebar({
  collapsed,
  onCollapseChange,
  navItems = defaultNavItems,
}: SidebarProps) {
  return (
    <aside
      className={cn(
        'flex flex-col border-r bg-slate-50 dark:bg-slate-900 transition-all duration-300 h-full',
        collapsed ? 'w-16' : 'w-64'
      )}
    >
      {/* Logo */}
      <div className="flex h-16 items-center justify-between border-b px-4">
        {!collapsed && (
          <div className="flex items-center gap-2">
            <div className="flex h-8 w-8 items-center justify-center rounded bg-[#2563EB]">
              <span className="text-lg font-bold text-white">HR</span>
            </div>
            <div>
              <span className="text-lg font-semibold">Gogidix</span>
              <span className="block text-xs text-muted-foreground">HR Platform</span>
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
      <nav className="flex-1 space-y-1 overflow-y-auto p-2 scrollbar-thin">
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
                    ? 'bg-[#2563EB] text-white'
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
            <Building2 className="h-5 w-5 text-[#2563EB]" />
            <div className="flex-1 text-xs">
              <p className="font-medium">Gogidix HR</p>
              <p className="text-muted-foreground">Global Human Resources</p>
            </div>
          </div>
        </div>
      )}
    </aside>
  )
}
