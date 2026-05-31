'use client'

import Link from 'next/link'
import { usePathname } from 'next/navigation'
import {
  LayoutDashboard,
  Users,
  DollarSign,
  Briefcase,
  TrendingUp,
  BookOpen,
  Calendar,
  ShieldCheck,
  FileText,
  Settings,
} from 'lucide-react'

const navigation = [
  { name: 'Overview', href: '/', icon: LayoutDashboard },
  { name: 'Employees', href: '/employees', icon: Users },
  { name: 'Payroll', href: '/payroll', icon: DollarSign },
  { name: 'Recruitment', href: '/recruitment', icon: Briefcase },
  { name: 'Performance', href: '/performance', icon: TrendingUp },
  { name: 'Training', href: '/training', icon: BookOpen },
  { name: 'Leave & Attendance', href: '/leave', icon: Calendar },
  { name: 'Compliance', href: '/compliance', icon: ShieldCheck },
  { name: 'Reports', href: '/reports', icon: FileText },
  { name: 'Settings', href: '/settings', icon: Settings },
]

export function Sidebar() {
  const pathname = usePathname()

  return (
    <div className="w-64 bg-gray-900 text-white">
      {/* Logo */}
      <div className="border-b border-gray-800 p-6">
        <h1 className="text-xl font-bold">GOGIDIX</h1>
        <p className="text-sm text-gray-400">Country HR Dashboard</p>
      </div>

      {/* Country Info */}
      <div className="border-b border-gray-800 p-4">
        <div className="flex items-center gap-3 rounded-lg bg-gray-800 p-3">
          <span className="text-2xl">🇳🇬</span>
          <div>
            <div className="text-sm font-medium">Nigeria</div>
            <div className="text-xs text-gray-400">HR Manager: Sarah Okon</div>
          </div>
        </div>
      </div>

      {/* Navigation */}
      <nav className="p-4">
        <ul className="space-y-1">
          {navigation.map((item) => {
            const isActive = pathname === item.href
            return (
              <li key={item.name}>
                <Link
                  href={item.href}
                  className={`flex items-center gap-3 rounded-lg px-3 py-2 text-sm transition-colors ${
                    isActive
                      ? 'bg-blue-600 text-white'
                      : 'text-gray-300 hover:bg-gray-800'
                  }`}
                >
                  <item.icon className="h-5 w-5" />
                  {item.name}
                </Link>
              </li>
            )
          })}
        </ul>
      </nav>

      {/* Footer */}
      <div className="absolute bottom-0 w-24 border-t border-gray-800 p-4 text-xs text-gray-500">
        <div>Data synced: 2 mins ago</div>
        <div>Version: 1.0.2</div>
      </div>
    </div>
  )
}
