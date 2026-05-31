import { Bell, LogOut, User as UserIcon } from 'lucide-react'
import { useNavigate } from 'react-router-dom'
import { Button } from '../ui/button'
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuLabel,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from '../ui/dropdown-menu'
import { Avatar, AvatarFallback, AvatarImage } from '../ui/avatar'
import { Badge } from '../ui/badge'
import type { User } from '@shared/types'

interface HeaderProps {
  user?: User | null
  onMenuClick?: () => void
  onLogout?: () => void
  role?: 'CEO' | 'CFO' | 'COO' | 'CTO'
}

const roleConfig = {
  CEO: { name: 'CEO Suite', color: 'from-[#0D47A1] to-[#1565C0]' },
  CFO: { name: 'CFO Suite', color: 'from-[#0D47A1] to-[#FFA000]' },
  COO: { name: 'COO Suite', color: 'from-[#0D47A1] to-[#FF6B00]' },
  CTO: { name: 'CTO Suite', color: 'from-[#0D47A1] to-[#7C4DFF]' },
}

export function Header({ user, onLogout, role = 'CEO' }: HeaderProps) {
  const navigate = useNavigate()
  const config = roleConfig[role]

  const navigateToSettings = () => {
    const settingsPath = role === 'CEO' ? '/settings' : `/${role.toLowerCase()}/settings`
    navigate(settingsPath)
  }

  return (
    <header className={`h-16 border-b px-6 flex items-center justify-between bg-gradient-to-r ${config.color}`}>
      <div className="flex items-center gap-4">
        <div className="flex items-center gap-2">
          <div className="w-8 h-8 bg-white/20 rounded flex items-center justify-center">
            <span className="text-sm font-bold text-white">G</span>
          </div>
          <span className="text-lg font-semibold text-white">Gogidix {config.name}</span>
        </div>
      </div>

      <div className="flex items-center gap-4">
        {/* Notifications */}
        <DropdownMenu>
          <DropdownMenuTrigger asChild>
            <Button
              variant="ghost"
              size="icon"
              className="relative text-white hover:bg-white/10 h-9 w-9"
            >
              <Bell className="h-4 w-4" />
              <span className="absolute -top-1 -right-1 flex h-4 w-4 items-center justify-center rounded-full bg-red-500 text-[10px] font-bold text-white">
                3
              </span>
            </Button>
          </DropdownMenuTrigger>
          <DropdownMenuContent align="end" className="w-80">
            <DropdownMenuLabel>Notifications</DropdownMenuLabel>
            <DropdownMenuSeparator />
            <div className="max-h-64 overflow-y-auto">
              <DropdownMenuItem className="flex-col items-start gap-2 cursor-pointer">
                <div className="flex w-full items-center justify-between">
                  <span className="font-medium">Budget Approval</span>
                  <Badge className="bg-red-100 text-red-700">Urgent</Badge>
                </div>
                <p className="text-sm text-slate-600">
                  Finance department requires approval for Q3 budget revision
                </p>
              </DropdownMenuItem>
              <DropdownMenuItem className="flex-col items-start gap-2 cursor-pointer">
                <span className="font-medium">KPI Alert</span>
                <p className="text-sm text-slate-600">
                  Revenue KPI is at risk - 15% below target
                </p>
              </DropdownMenuItem>
            </div>
          </DropdownMenuContent>
        </DropdownMenu>

        {/* User Menu */}
        <DropdownMenu>
          <DropdownMenuTrigger asChild>
            <Button
              variant="ghost"
              className="gap-2 text-white hover:bg-white/10 h-9 px-3"
            >
              <Avatar className="h-7 w-7 border-2 border-white/30">
                <AvatarImage src={user?.avatar} alt={user?.displayName} />
                <AvatarFallback className="bg-white/20 text-white text-xs">
                  {user?.displayName?.charAt(0) || 'U'}
                </AvatarFallback>
              </Avatar>
              <span className="text-sm font-medium">{user?.displayName}</span>
            </Button>
          </DropdownMenuTrigger>
          <DropdownMenuContent align="end" className="w-48">
            <DropdownMenuLabel>
              <div className="flex flex-col space-y-1">
                <p className="text-sm font-medium">{user?.displayName}</p>
                <p className="text-xs text-slate-500">{user?.email}</p>
                <p className="text-xs font-semibold text-[#0D47A1]">{user?.role}</p>
              </div>
            </DropdownMenuLabel>
            <DropdownMenuSeparator />
            <DropdownMenuItem onClick={navigateToSettings}>
              <UserIcon className="mr-2 h-4 w-4" />
              Profile
            </DropdownMenuItem>
            <DropdownMenuItem onClick={navigateToSettings}>
              Settings
            </DropdownMenuItem>
            <DropdownMenuSeparator />
            <DropdownMenuItem onClick={onLogout} className="text-red-600">
              <LogOut className="mr-2 h-4 w-4" />
              Logout
            </DropdownMenuItem>
          </DropdownMenuContent>
        </DropdownMenu>
      </div>
    </header>
  )
}
