import { Bell, Menu, Search, LogOut, Globe } from 'lucide-react'
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
import type { GBMUser } from '@shared/types'
import { cn } from '@shared/utils/cn'

interface HeaderProps {
  user?: GBMUser | null
  onMenuClick?: () => void
  onLogout?: () => void
}

export function Header({ user, onMenuClick, onLogout }: HeaderProps) {
  return (
    <header className="gbm-gradient sticky top-0 z-30 flex h-16 items-center justify-between border-b px-4 md:px-6 text-white">
      <div className="flex items-center gap-4">
        <Button
          variant="ghost"
          size="icon"
          className="md:hidden text-white hover:bg-white/10"
          onClick={onMenuClick}
        >
          <Menu className="h-5 w-5" />
        </Button>

        <div className="flex items-center gap-2">
          <div className="flex h-8 w-8 items-center justify-center rounded bg-white/20">
            <span className="text-lg font-bold text-white">G</span>
          </div>
          <span className="hidden text-lg font-semibold md:block">
            GBM Portal
          </span>
        </div>
      </div>

      <div className="flex items-center gap-2 md:gap-4">
        {/* Search */}
        <div className="hidden md:flex items-center">
          <div className="relative">
            <Search className="absolute left-2.5 top-2.5 h-4 w-4 text-white/60" />
            <input
              type="search"
              placeholder="Search..."
              className={cn(
                'h-9 w-64 rounded-full border border-white/20 bg-white/10 pl-9 pr-4 text-sm text-white placeholder:text-white/60',
                'focus:outline-none focus:ring-2 focus:ring-white/30'
              )}
            />
          </div>
        </div>

        {/* Region Selector */}
        <DropdownMenu>
          <DropdownMenuTrigger asChild>
            <Button
              variant="ghost"
              size="sm"
              className="gap-2 text-white hover:bg-white/10"
            >
              <Globe className="h-4 w-4" />
              <span className="hidden md:inline">Global</span>
            </Button>
          </DropdownMenuTrigger>
          <DropdownMenuContent align="end" className="w-48">
            <DropdownMenuLabel>Select Region</DropdownMenuLabel>
            <DropdownMenuSeparator />
            <DropdownMenuItem>Global View</DropdownMenuItem>
            <DropdownMenuItem>North America</DropdownMenuItem>
            <DropdownMenuItem>Europe</DropdownMenuItem>
            <DropdownMenuItem>Africa - North</DropdownMenuItem>
            <DropdownMenuItem>Africa - West</DropdownMenuItem>
            <DropdownMenuItem>Africa - East</DropdownMenuItem>
            <DropdownMenuItem>Africa - South</DropdownMenuItem>
            <DropdownMenuItem>Asia Pacific</DropdownMenuItem>
          </DropdownMenuContent>
        </DropdownMenu>

        {/* Notifications */}
        <DropdownMenu>
          <DropdownMenuTrigger asChild>
            <Button
              variant="ghost"
              size="icon"
              className="relative text-white hover:bg-white/10"
            >
              <Bell className="h-5 w-5" />
              <span className="absolute right-1 top-1 flex h-4 w-4 items-center justify-center rounded-full bg-red-500 text-[10px] font-bold">
                3
              </span>
            </Button>
          </DropdownMenuTrigger>
          <DropdownMenuContent align="end" className="w-80">
            <DropdownMenuLabel>Notifications</DropdownMenuLabel>
            <DropdownMenuSeparator />
            <div className="max-h-80 overflow-y-auto">
              <DropdownMenuItem className="flex-col items-start gap-2">
                <div className="flex w-full items-center justify-between">
                  <span className="font-medium">Data Aggregation Complete</span>
                  <Badge variant="success">New</Badge>
                </div>
                <p className="text-sm text-muted-foreground">
                  Monthly aggregation for all regions completed successfully
                </p>
              </DropdownMenuItem>
              <DropdownMenuItem className="flex-col items-start gap-2">
                <div className="flex w-full items-center justify-between">
                  <span className="font-medium">Currency Alert</span>
                  <Badge variant="warning">Alert</Badge>
                </div>
                <p className="text-sm text-muted-foreground">
                  NGN exchange rate fluctuated by more than 5%
                </p>
              </DropdownMenuItem>
              <DropdownMenuItem className="flex-col items-start gap-2">
                <span className="font-medium">Report Ready</span>
                <p className="text-sm text-muted-foreground">
                  Your scheduled regional report is ready for download
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
              className="gap-2 text-white hover:bg-white/10"
            >
              <Avatar className="h-8 w-8 border-2 border-white/20">
                <AvatarImage src={user?.avatar} alt={user?.displayName} />
                <AvatarFallback className="bg-white/20 text-white">
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
                <p className="text-xs font-semibold text-[#1E88E5]">
                  {user?.role?.replace(/_/g, ' ')}
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
