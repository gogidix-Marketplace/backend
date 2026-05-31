import * as React from 'react'
import Link, { LinkProps } from 'react-router-dom'
import {
  LayoutDashboard,
  TrendingUp,
  CheckSquare,
  BarChart3,
  FileText,
  Settings,
  ChevronLeft,
  ChevronRight,
  Building2,
  Users,
  HeadphonesIcon,
  Shield,
  Globe,
  Activity,
  ChevronDown,
} from 'lucide-react'
import { cn } from '@/lib/utils'
import { Button } from '@/components/ui/button'
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuLabel,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from '@/components/ui/dropdown-menu'
import { Badge } from '@/components/ui/badge'

export interface NavItem {
  title: string
  href?: string
  icon?: React.ReactNode
  badge?: number | string
  children?: NavItem[]
  disabled?: boolean
}

const defaultNavItems: NavItem[] = [
  {
    title: 'Overview',
    href: '/',
    icon: <LayoutDashboard className="h-4 w-4" />,
  },
  {
    title: 'Strategy',
    icon: <TrendingUp className="h-4 w-4" />,
    children: [
      { title: 'Strategic KPIs', href: '/strategy' },
      { title: 'Goals & OKRs', href: '/strategy/goals' },
      { title: 'Initiatives', href: '/strategy/initiatives' },
    ],
  },
  {
    title: 'Analytics',
    icon: <BarChart3 className="h-4 w-4" />,
    children: [
      { title: 'Cross-Domain', href: '/analytics/domains' },
      { title: 'Regional', href: '/analytics/regions' },
      { title: 'Trends', href: '/analytics/trends' },
    ],
  },
  {
    title: 'Approvals',
    href: '/approvals',
    icon: <CheckSquare className="h-4 w-4" />,
    badge: 5,
  },
  {
    title: 'Reports',
    href: '/reports',
    icon: <FileText className="h-4 w-4" />,
  },
  {
    title: 'Settings',
    href: '/settings',
    icon: <Settings className="h-4 w-4" />,
  },
]

const departmentNavItems: Record<string, NavItem[]> = {
  finance: [
    {
      title: 'Overview',
      href: '/',
      icon: <LayoutDashboard className="h-4 w-4" />,
    },
    {
      title: 'Accounts Payable',
      href: '/accounts-payable',
      icon: <Building2 className="h-4 w-4" />,
    },
    {
      title: 'Accounts Receivable',
      href: '/accounts-receivable',
      icon: <Building2 className="h-4 w-4" />,
    },
    {
      title: 'Budget Management',
      href: '/budget',
      icon: <BarChart3 className="h-4 w-4" />,
    },
    {
      title: 'Reports',
      href: '/reports',
      icon: <FileText className="h-4 w-4" />,
    },
  ],
  hr: [
    {
      title: 'Overview',
      href: '/',
      icon: <LayoutDashboard className="h-4 w-4" />,
    },
    {
      title: 'Employees',
      href: '/employees',
      icon: <Users className="h-4 w-4" />,
    },
    {
      title: 'Leave Management',
      href: '/leave',
      icon: <CheckSquare className="h-4 w-4" />,
    },
    {
      title: 'Payroll',
      href: '/payroll',
      icon: <Building2 className="h-4 w-4" />,
    },
    {
      title: 'Performance',
      href: '/performance',
      icon: <TrendingUp className="h-4 w-4" />,
    },
  ],
  sales: [
    {
      title: 'Overview',
      href: '/',
      icon: <LayoutDashboard className="h-4 w-4" />,
    },
    {
      title: 'Leads',
      href: '/leads',
      icon: <Users className="h-4 w-4" />,
    },
    {
      title: 'Opportunities',
      href: '/opportunities',
      icon: <TrendingUp className="h-4 w-4" />,
    },
    {
      title: 'Customers',
      href: '/customers',
      icon: <Building2 className="h-4 w-4" />,
    },
    {
      title: 'Analytics',
      href: '/analytics',
      icon: <BarChart3 className="h-4 w-4" />,
    },
  ],
  support: [
    {
      title: 'Overview',
      href: '/',
      icon: <LayoutDashboard className="h-4 w-4" />,
    },
    {
      title: 'Tickets',
      href: '/tickets',
      icon: <HeadphonesIcon className="h-4 w-4" />,
      badge: 12,
    },
    {
      title: 'Knowledge Base',
      href: '/knowledge',
      icon: <FileText className="h-4 w-4" />,
    },
    {
      title: 'Live Chat',
      href: '/chat',
      icon: <Activity className="h-4 w-4" />,
    },
  ],
  admin: [
    {
      title: 'Overview',
      href: '/',
      icon: <LayoutDashboard className="h-4 w-4" />,
    },
    {
      title: 'Access Control',
      href: '/access',
      icon: <Shield className="h-4 w-4" />,
    },
    {
      title: 'Incidents',
      href: '/incidents',
      icon: <Activity className="h-4 w-4" />,
      badge: 3,
    },
    {
      title: 'Monitoring',
      href: '/monitoring',
      icon: <Activity className="h-4 w-4" />,
    },
    {
      title: 'Deployment',
      href: '/deployment',
      icon: <Settings className="h-4 w-4" />,
    },
  ],
  gbm: [
    {
      title: 'Overview',
      href: '/',
      icon: <LayoutDashboard className="h-4 w-4" />,
    },
    {
      title: 'Regional Analytics',
      href: '/regional',
      icon: <Globe className="h-4 w-4" />,
    },
    {
      title: 'Country Management',
      href: '/countries',
      icon: <Building2 className="h-4 w-4" />,
    },
    {
      title: 'Reports',
      href: '/reports',
      icon: <FileText className="h-4 w-4" />,
    },
  ],
}

interface SidebarProps {
  department?: string
  navItems?: NavItem[]
  collapsed?: boolean
  onCollapseChange?: (collapsed: boolean) => void
  className?: string
}

export function Sidebar({
  department = 'executive',
  navItems,
  collapsed = false,
  onCollapseChange,
  className,
}: SidebarProps) {
  const [openSubmenus, setOpenSubmenus] = React.useState<Set<string>>(new Set())
  const location = window.location

  const items = navItems || (departmentNavItems[department] || defaultNavItems)

  const toggleSubmenu = (title: string) => {
    const newOpen = new Set(openSubmenus)
    if (newOpen.has(title)) {
      newOpen.delete(title)
    } else {
      newOpen.add(title)
    }
    setOpenSubmenus(newOpen)
  }

  const isActive = (href?: string) => {
    if (!href) return false
    return location.pathname === href || location.pathname.startsWith(href + '/')
  }

  return (
    <aside
      className={cn(
        'flex flex-col border-r bg-background transition-all duration-300',
        collapsed ? 'w-16' : 'w-64',
        className
      )}
    >
      {/* Logo */}
      <div className="flex h-16 items-center border-b px-4">
        <div className="flex items-center gap-2">
          <div className="flex h-8 w-8 items-center justify-center rounded-lg bg-[#0D47A1]">
            <span className="text-lg font-bold text-white">G</span>
          </div>
          {!collapsed && (
            <span className="text-lg font-semibold">Gogidix</span>
          )}
        </div>
      </div>

      {/* Collapse button */}
      <div className="flex items-center justify-end p-2">
        <Button
          variant="ghost"
          size="icon"
          onClick={() => onCollapseChange?.(!collapsed)}
          className="h-8 w-8"
        >
          {collapsed ? (
            <ChevronRight className="h-4 w-4" />
          ) : (
            <ChevronLeft className="h-4 w-4" />
          )}
        </Button>
      </div>

      {/* Navigation */}
      <nav className="flex-1 space-y-1 overflow-y-auto px-2 py-4 scrollbar-thin">
        {items.map((item) => {
          if (item.children) {
            const isOpen = openSubmenus.has(item.title)
            const hasActiveChild = item.children.some((child) =>
              isActive(child.href)
            )

            return (
              <DropdownMenu key={item.title} open={isOpen}>
                <DropdownMenuTrigger asChild>
                  <Button
                    variant={hasActiveChild ? 'secondary' : 'ghost'}
                    className={cn(
                      'w-full justify-start gap-2',
                      collapsed && 'justify-center px-2'
                    )}
                    onClick={() => !collapsed && toggleSubmenu(item.title)}
                  >
                    {item.icon}
                    {!collapsed && (
                      <>
                        <span className="flex-1 text-left">{item.title}</span>
                        <ChevronDown
                          className={cn(
                            'h-4 w-4 transition-transform',
                            isOpen && 'rotate-180'
                          )}
                        />
                      </>
                    )}
                  </Button>
                </DropdownMenuTrigger>
                {!collapsed && (
                  <DropdownMenuContent
                    align="start"
                    className="w-56"
                    sideOffset={0}
                  >
                    <DropdownMenuLabel>{item.title}</DropdownMenuLabel>
                    <DropdownMenuSeparator />
                    {item.children.map((child) => (
                      <DropdownMenuItem
                        key={child.title}
                        asChild
                        disabled={child.disabled}
                      >
                        <Link
                          to={child.href || '#'}
                          className={cn(
                            'flex w-full items-center gap-2',
                            isActive(child.href) && 'bg-accent'
                          )}
                        >
                          {child.icon}
                          {child.title}
                          {child.badge && (
                            <Badge variant="secondary" className="ml-auto">
                              {child.badge}
                            </Badge>
                          )}
                        </Link>
                      </DropdownMenuItem>
                    ))}
                  </DropdownMenuContent>
                )}
              </DropdownMenu>
            )
          }

          return (
            <Button
              key={item.title}
              asChild
              variant={isActive(item.href) ? 'secondary' : 'ghost'}
              className={cn(
                'w-full justify-start gap-2',
                collapsed && 'justify-center px-2'
              )}
              disabled={item.disabled}
            >
              <Link to={item.href || '#'}>
                {item.icon}
                {!collapsed && (
                  <>
                    <span className="flex-1 text-left">{item.title}</span>
                    {item.badge && (
                      <Badge variant="secondary">{item.badge}</Badge>
                    )}
                  </>
                )}
              </Link>
            </Button>
          )
        })}
      </nav>

      {/* Footer */}
      {!collapsed && (
        <div className="border-t p-4">
          <p className="text-xs text-muted-foreground">
            © 2025 Gogidix Management
          </p>
        </div>
      )}
    </aside>
  )
}
