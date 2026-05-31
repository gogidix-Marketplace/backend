import { Bell, Menu, Search, LogOut, Activity } from 'lucide-react'
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
import { cn } from '@shared/utils/cn'

interface HeaderProps {
  user?: User | null
  onMenuClick?: () => void
  onLogout?: () => void
  activeAlerts?: number
}

export function Header({ user, onMenuClick, onLogout, activeAlerts = 0 }: HeaderProps) {
  return (
    <header className="sticky top-0 z-30 flex h-16 items-center justify-between border-b bg-white dark:bg-slate-900 px-4 md:px-6 shadow-sm">
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
          <div className="flex h-8 w-8 items-center justify-center rounded bg-[#0D47A1]">
            <Activity className="h-5 w-5 text-white" />
          </div>
          <span className="hidden text-lg font-semibold md:block">
            Monitoring Portal
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
              placeholder="Search services, alerts..."
              className={cn(
                'h-9 w-64 rounded-full border border-input bg-background pl-9 pr-4 text-sm',
                'focus:outline-none focus:ring-2 focus:ring-[#0D47A1] focus:ring-offset-2'
              )}
            />
          </div>
        </div>

        {/* Notifications */}
        <DropdownMenu>
          <DropdownMenuTrigger asChild>
            <Button variant="ghost" size="icon" className="relative">
              <Bell className="h-5 w-5" />
              {activeAlerts > 0 && (
                <span className="absolute right-1 top-1 flex h-4 w-4 items-center justify-center rounded-full bg-red-500 text-[10px] font-bold text-white">
                  {activeAlerts > 9 ? '9+' : activeAlerts}
                </span>
              )}
            </Button>
          </DropdownMenuTrigger>
          <DropdownMenuContent align="end" className="w-80">
            <DropdownMenuLabel>Notifications</DropdownMenuLabel>
            <DropdownMenuSeparator />
            <div className="max-h-80 overflow-y-auto">
              {activeAlerts > 0 ? (
                <>
                  <DropdownMenuItem className="flex-col items-start gap-2">
                    <div className="flex w-full items-center justify-between">
                      <span className="font-medium">Critical Alert</span>
                      <Badge variant="critical">Critical</Badge>
                    </div>
                    <p className="text-sm text-muted-foreground">
                      AI Orchestration service is not responding
                    </p>
                  </DropdownMenuItem>
                  <DropdownMenuItem className="flex-col items-start gap-2">
                    <div className="flex w-full items-center justify-between">
                      <span className="font-medium">Warning</span>
                      <Badge variant="degraded">Warning</Badge>
                    </div>
                    <p className="text-sm text-muted-foreground">
                      Notification Service response time degraded
                    </p>
                  </DropdownMenuItem>
                </>
              ) : (
                <div className="px-4 py-2 text-sm text-muted-foreground text-center">
                  No new notifications
                </div>
              )}
            </div>
          </DropdownMenuContent>
        </DropdownMenu>

        {/* User Menu */}
        <DropdownMenu>
          <DropdownMenuTrigger asChild>
            <Button variant="ghost" className="gap-2">
              <Avatar className="h-8 w-8">
                <AvatarImage src={user?.avatar} alt={user?.displayName} />
                <AvatarFallback className="bg-[#0D47A1] text-white">
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
                <p className="text-xs font-semibold text-[#0D47A1]">
                  {user?.role?.replace('_', ' ')}
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
