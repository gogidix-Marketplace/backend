import { NavLink } from 'react-router-dom'
import { useState } from 'react'
import {
  LayoutDashboard,
  Target,
  BarChart3,
  CheckCircle,
  FileText,
  Settings,
  ChevronLeft,
  ChevronRight,
  Building2,
  DollarSign,
  TrendingUp,
  Shield,
  Users,
  ChevronDown,
  ShoppingBag,
  Truck,
  Package,
  Wrench,
  Plane,
  Ship,
  Container,
  Layers,
  Megaphone,
  Headphones,
  Globe2,
  UserCog,
  ShoppingCart,
  Server,
  Banknote,
  Heart,
} from 'lucide-react'
import { Badge } from '../ui/badge'
import * as React from 'react'

interface NavItem {
  title: string
  href: string
  icon: string
  badge?: number
}

interface BusinessDomain {
  name: string
  code: string
  path: string
  icon: React.ComponentType<{ className?: string }>
  color: string
}

interface DepartmentNavItem {
  name: string
  slug: string
  path: string
  icon: React.ComponentType<{ className?: string }>
  color: string
}

interface SidebarProps {
  collapsed: boolean
  onCollapseChange: (collapsed: boolean) => void
  navItems: NavItem[]
  role?: 'CEO' | 'CFO' | 'COO' | 'CTO'
}

const iconMap: Record<string, React.ComponentType<{ className?: string }>> = {
  LayoutDashboard,
  Target,
  BarChart3,
  CheckCircle,
  FileText,
  Settings,
  DollarSign,
  TrendingUp,
  Shield,
  Users,
}

const businessDomains: BusinessDomain[] = [
  { name: 'E-commerce', code: 'ECOM', path: '/domain/ecommerce', icon: ShoppingBag, color: '#10b981' },
  { name: 'Courier', code: 'COURIER', path: '/domain/courier', icon: Truck, color: '#3b82f6' },
  { name: 'Warehousing', code: 'WHS', path: '/domain/warehousing', icon: Package, color: '#f59e0b' },
  { name: 'Procurement', code: 'PROC', path: '/domain/procurement', icon: Wrench, color: '#8b5cf6' },
  { name: 'Air Freight', code: 'AIR', path: '/domain/air-freight', icon: Plane, color: '#06b6d4' },
  { name: 'Ocean Shipping', code: 'OCEAN', path: '/domain/ocean-shipping', icon: Ship, color: '#14b8a6' },
  { name: 'Haulage', code: 'HAUL', path: '/domain/haulage', icon: Container, color: '#f97316' },
]

const departments: DepartmentNavItem[] = [
  { name: 'Digital Marketing', slug: 'digital-marketing', path: '/departments/digital-marketing', icon: Megaphone, color: '#E91E63' },
  { name: 'Customer Support', slug: 'customer-support', path: '/departments/customer-support', icon: Headphones, color: '#00BCD4' },
  { name: 'Global Business', slug: 'global-business-management', path: '/departments/global-business-management', icon: Globe2, color: '#4CAF50' },
  { name: 'Human Resources', slug: 'human-resource', path: '/departments/human-resource', icon: UserCog, color: '#FF9800' },
  { name: 'Sales', slug: 'sales', path: '/departments/sales', icon: ShoppingCart, color: '#2196F3' },
  { name: 'System Admin', slug: 'system-administrator', path: '/departments/system-administrator', icon: Server, color: '#9C27B0' },
  { name: 'Finance', slug: 'finance', path: '/departments/finance', icon: Banknote, color: '#FF5722' },
  { name: 'Foundation', slug: 'foundation-services', path: '/departments/foundation-services', icon: Heart, color: '#607D8B' },
]

const roleConfig = {
  CEO: { name: 'CEO Suite', color: '#0D47A1', showBusinessDomains: true, showDepartments: true },
  CFO: { name: 'CFO Suite', color: '#0D47A1', showBusinessDomains: true, showDepartments: true },
  COO: { name: 'COO Suite', color: '#0D47A1', showBusinessDomains: true, showDepartments: true },
  CTO: { name: 'CTO Suite', color: '#0D47A1', showBusinessDomains: true, showDepartments: true },
}

function getDeptPath(slug: string, role: string): string {
  const prefix = role === 'CFO' ? '/cfo' : role === 'COO' ? '/coo' : role === 'CTO' ? '/cto' : ''
  return `${prefix}/departments/${slug}`
}

export function Sidebar({ collapsed, onCollapseChange, navItems, role = 'CEO' }: SidebarProps) {
  const config = roleConfig[role]
  const [businessDomainsExpanded, setBusinessDomainsExpanded] = useState(false)
  const [departmentsExpanded, setDepartmentsExpanded] = useState(false)

  return (
    <aside
      className="flex flex-col h-full border-r bg-slate-50 dark:bg-slate-900 transition-all duration-300"
      style={{ width: collapsed ? '4rem' : '16rem' }}
    >
      <div className="h-16 flex items-center justify-between px-4 border-b">
        {!collapsed && (
          <div className="flex items-center gap-2">
            <div className="w-8 h-8 rounded flex items-center justify-center" style={{ backgroundColor: config.color }}>
              <span className="text-sm font-bold text-white">G</span>
            </div>
            <span className="font-semibold text-slate-900">{config.name}</span>
          </div>
        )}
        <button
          onClick={() => onCollapseChange(!collapsed)}
          className="p-1 rounded hover:bg-slate-100 text-slate-500"
        >
          {collapsed ? (
            <ChevronRight className="w-4 h-4" />
          ) : (
            <ChevronLeft className="w-4 h-4" />
          )}
        </button>
      </div>

      <nav className="flex-1 px-3 py-4 space-y-1 overflow-y-auto scrollbar-thin">
        {navItems.map((item) => {
          const Icon = iconMap[item.icon] || LayoutDashboard
          return (
            <NavLink
              key={item.href}
              to={item.href}
              end={item.href === '/' || item.href === '/cfo' || item.href === '/coo' || item.href === '/cto'}
              className={({ isActive }) =>
                `flex items-center gap-3 rounded-lg px-3 py-2 text-sm font-medium transition-colors ${
                  isActive
                    ? 'bg-[#0D47A1] text-white'
                    : 'text-slate-700 hover:bg-slate-100'
                } ${collapsed && 'justify-center px-2'}`
              }
            >
              <Icon className="w-5 h-5 flex-shrink-0" />
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

        {config.showDepartments && !collapsed && (
          <div className="mt-4 pt-4 border-t border-slate-200 dark:border-slate-700">
            <button
              onClick={() => setDepartmentsExpanded(!departmentsExpanded)}
              className="flex items-center gap-2 w-full px-3 py-2 text-sm font-medium text-slate-600 hover:bg-slate-100 rounded-lg transition-colors"
            >
              <Layers className="w-5 h-5" />
              <span className="flex-1 text-left">Departments</span>
              <ChevronDown
                className={`w-4 h-4 transition-transform ${departmentsExpanded ? 'rotate-180' : ''}`}
              />
            </button>
            {departmentsExpanded && (
              <div className="mt-1 space-y-1">
                {departments.map((dept) => {
                  const Icon = dept.icon
                  return (
                    <NavLink
                      key={dept.slug}
                      to={getDeptPath(dept.slug, role)}
                      className={({ isActive }) =>
                        `flex items-center gap-2 rounded-lg px-3 py-2 text-xs font-medium transition-colors ${
                          isActive
                            ? 'bg-slate-200 dark:bg-slate-800 text-slate-900'
                            : 'text-slate-600 hover:bg-slate-100'
                        }`
                      }
                    >
                      <div
                        className="w-2 h-2 rounded-full"
                        style={{ backgroundColor: dept.color }}
                      />
                      <span className="w-4 h-4 flex-shrink-0 inline-flex" style={{ color: dept.color }}>
                        <Icon className="w-4 h-4" />
                      </span>
                      <span>{dept.name}</span>
                    </NavLink>
                  )
                })}
              </div>
            )}
          </div>
        )}

        {config.showBusinessDomains && !collapsed && (
          <div className="mt-4 pt-4 border-t border-slate-200 dark:border-slate-700">
            <button
              onClick={() => setBusinessDomainsExpanded(!businessDomainsExpanded)}
              className="flex items-center gap-2 w-full px-3 py-2 text-sm font-medium text-slate-600 hover:bg-slate-100 rounded-lg transition-colors"
            >
              <Building2 className="w-5 h-5" />
              <span className="flex-1 text-left">Business Domains</span>
              <ChevronDown
                className={`w-4 h-4 transition-transform ${businessDomainsExpanded ? 'rotate-180' : ''}`}
              />
            </button>
            {businessDomainsExpanded && (
              <div className="mt-1 space-y-1">
                {businessDomains.map((domain) => {
                  const Icon = domain.icon
                  return (
                    <NavLink
                      key={domain.code}
                      to={domain.path}
                      className={({ isActive }) =>
                        `flex items-center gap-2 rounded-lg px-3 py-2 text-xs font-medium transition-colors ${
                          isActive
                            ? 'bg-slate-200 dark:bg-slate-800 text-slate-900'
                            : 'text-slate-600 hover:bg-slate-100'
                        }`
                      }
                    >
                      <div
                        className="w-2 h-2 rounded-full"
                        style={{ backgroundColor: domain.color }}
                      />
                      <span className="w-4 h-4 flex-shrink-0 inline-flex" style={{ color: domain.color }}>
                        <Icon className="w-4 h-4" />
                      </span>
                      <span>{domain.name}</span>
                      <span className="ml-auto text-[10px] text-slate-500">{domain.code}</span>
                    </NavLink>
                  )
                })}
              </div>
            )}
          </div>
        )}
      </nav>

      {!collapsed && (
        <div className="p-4 border-t">
          <div className="flex items-center gap-3 rounded-lg bg-slate-50 p-3">
            <Building2 className="w-5 h-5 text-[#0D47A1]" />
            <div className="flex-1 text-xs">
              <p className="font-medium text-slate-900">Gogidix Executive</p>
              <p className="text-slate-500">Global HQ</p>
            </div>
          </div>
        </div>
      )}
    </aside>
  )
}
