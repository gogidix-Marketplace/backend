import { Bell, Menu, Search, LogOut } from 'lucide-react'
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
import type { AdminUser } from '@shared/types'
import { cn } from '@shared/utils/cn'

interface HeaderProps {
  user?: AdminUser | null
  onMenuClick?: () => void
  onLogout?: () => void
}

export function Header({ user, onMenuClick, onLogout }: HeaderProps) {
  return (
    <header className="sticky top-0 z-30 flex h-16 items-center justify-between border-b bg-white px-4 md:px-6">
      <div className="flex items-center gap-4">
        <Button
          variant="ghost"
          size="icon"
          className="md:hidden"
          onClick={onMenuClick}
        >
          <Menu className="h-5 w-5" />
        </Button>

        <div className="flex items-center gap-2">
          <div className="flex h-8 w-8 items-center justify-center rounded bg-admin-blue">
            <Shield className="h-5 w-5 text-white" />
          </div>
          <span className="hidden text-lg font-semibold text-admin-navy md:block">
            System Administrator Dashboard
          </span>
        </div>
      </div>

      <div className="flex items-center gap-2 md:gap-4">
        {/* Search */}
        <div className="hidden md:flex items-center">
          <div className="relative">
            <Search className="absolute left-2.5 top-2.5 h-4 w-4 text-muted-foreground" />
            <input
              type="search"
              placeholder="Search users, incidents, configs..."
              className={cn(
                'h-9 w-80 rounded-full border border-input bg-background pl-9 pr-4 text-sm',
                'focus:outline-none focus:ring-2 focus:ring-admin-blue'
              )}
            />
          </div>
        </div>

        {/* Notifications */}
        <DropdownMenu>
          <DropdownMenuTrigger asChild>
            <Button variant="ghost" size="icon" className="relative">
              <Bell className="h-5 w-5" />
              <span className="absolute right-1 top-1 flex h-4 w-4 items-center justify-center rounded-full bg-red-500 text-[10px] font-bold text-white">
                5
              </span>
            </Button>
          </DropdownMenuTrigger>
          <DropdownMenuContent align="end" className="w-80">
            <DropdownMenuLabel>System Notifications</DropdownMenuLabel>
            <DropdownMenuSeparator />
            <div className="max-h-80 overflow-y-auto">
              <DropdownMenuItem className="flex-col items-start gap-2">
                <div className="flex w-full items-center justify-between">
                  <span className="font-medium">Security Alert</span>
                  <Badge variant="destructive">Critical</Badge>
                </div>
                <p className="text-sm text-muted-foreground">
                  Multiple failed login attempts detected from IP 192.168.1.100
                </p>
              </DropdownMenuItem>
              <DropdownMenuItem className="flex-col items-start gap-2">
                <div className="flex w-full items-center justify-between">
                  <span className="font-medium">Access Request</span>
                  <Badge variant="warning">Pending</Badge>
                </div>
                <p className="text-sm text-muted-foreground">
                  3 new access requests awaiting approval
                </p>
              </DropdownMenuItem>
              <DropdownMenuItem className="flex-col items-start gap-2">
                <div className="flex w-full items-center justify-between">
                  <span className="font-medium">System Update</span>
                  <Badge variant="info">Info</Badge>
                </div>
                <p className="text-sm text-muted-foreground">
                  Scheduled maintenance in 2 hours
                </p>
              </DropdownMenuItem>
            </div>
          </DropdownMenuContent>
        </DropdownMenu>

        {/* User Menu */}
        <DropdownMenu>
          <DropdownMenuTrigger asChild>
            <Button variant="ghost" className="gap-2">
              <Avatar className="h-8 w-8 border-2 border-slate-200">
                <AvatarImage src={user?.avatar} alt={user?.displayName} />
                <AvatarFallback className="bg-admin-blue text-white">
                  {user?.displayName?.charAt(0) || 'U'}
                </AvatarFallback>
              </Avatar>
              <span className="hidden text-sm font-medium md:block">
                {user?.displayName}
              </span>
            </Button>
          </DropdownMenuTrigger>
          <DropdownMenuContent align="end" className="w-56">
            <DropdownMenuLabel>
              <div className="flex flex-col space-y-1">
                <p className="text-sm font-medium">{user?.displayName}</p>
                <p className="text-xs text-muted-foreground">{user?.email}</p>
                <p className="text-xs font-semibold text-admin-blue">
                  {user?.role.replace(/_/g, ' ')}
                </p>
              </div>
            </DropdownMenuLabel>
            <DropdownMenuSeparator />
            <DropdownMenuItem>Profile</DropdownMenuItem>
            <DropdownMenuItem>Settings</DropdownMenuItem>
            <DropdownMenuSeparator />
            <DropdownMenuItem onClick={onLogout} className="text-destructive">
              <LogOut className="mr-2 h-4 w-4" />
              Logout
            </DropdownMenuItem>
          </DropdownMenuContent>
        </DropdownMenu>
      </div>
    </header>
  )
}
