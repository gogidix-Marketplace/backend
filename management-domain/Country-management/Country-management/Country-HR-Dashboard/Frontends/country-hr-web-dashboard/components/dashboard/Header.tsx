'use client'

import { Bell, Search, User, LogOut } from 'lucide-react'

export function Header() {
  const user = {
    name: 'Sarah Okon',
    email: 's.okon@gogidix.com',
    role: 'Country HR Manager',
  }

  return (
    <header className="flex items-center justify-between border-b bg-white px-6 py-4">
      {/* Search */}
      <div className="flex-1">
        <div className="relative w-96">
          <Search className="absolute left-3 top-1/2 h-4 w-4 -translate-y-1/2 text-gray-400" />
          <input
            type="text"
            placeholder="Search employees, documents, policies..."
            className="w-full rounded-lg border border-gray-300 py-2 pl-10 pr-4 text-sm focus:border-blue-500 focus:outline-none focus:ring-1 focus:ring-blue-500"
          />
        </div>
      </div>

      {/* Right Side */}
      <div className="flex items-center gap-4">
        {/* Notifications */}
        <button className="relative rounded-lg p-2 text-gray-600 hover:bg-gray-100">
          <Bell className="h-5 w-5" />
          <span className="absolute right-1 top-1 h-2 w-2 rounded-full bg-red-500" />
        </button>

        {/* User Menu */}
        <div className="flex items-center gap-3 rounded-lg border px-3 py-2">
          <div className="text-right text-sm">
            <div className="font-medium">{user.name}</div>
            <div className="text-xs text-gray-500">{user.role}</div>
          </div>
          <div className="h-8 w-8 rounded-full bg-blue-500 flex items-center justify-center text-white text-sm">
            SO
          </div>
        </div>

        {/* Logout */}
        <button className="rounded-lg p-2 text-gray-600 hover:bg-gray-100">
          <LogOut className="h-5 w-5" />
        </button>
      </div>
    </header>
  )
}
