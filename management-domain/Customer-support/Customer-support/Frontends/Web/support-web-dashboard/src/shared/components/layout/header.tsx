import * as React from 'react'
import { Bell, Search, Settings, User, LogOut, Globe, Menu } from 'lucide-react'
import { Button } from '@shared/components/ui/button'
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuLabel,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from '@shared/components/ui/dropdown-menu'
import { Avatar, AvatarFallback, AvatarImage } from '@shared/components/ui/avatar'
import { Badge } from '@shared/components/ui/badge'
import { useAuthStore } from '@shared/stores/authStore'
import type { SupportUser } from '@shared/types'
import { cn } from '@shared/utils/cn'

interface HeaderProps {
  user: SupportUser | null
  onMenuClick?: () => void
  onLogout: () => void
  showViewToggle?: boolean
  currentView?: 'global' | 'country'
  selectedCountry?: string | null
  onViewChange?: (view: 'global' | 'country') => void
  notificationCount?: number
}

export function Header({
  user,
  onMenuClick,
  onLogout,
  showViewToggle = true,
  currentView = 'global',
  selectedCountry,
  onViewChange,
  notificationCount = 0,
}: HeaderProps) {
  const getInitials = (firstName?: string, lastName?: string) => {
    if (!firstName && !lastName) return 'U'
    return `${firstName?.[0] || ''}${lastName?.[0] || ''}`.toUpperCase()
  }

  const countryNames: Record<string, string> = {
    US: 'United States',
    CA: 'Canada',
    GB: 'United Kingdom',
    NG: 'Nigeria',
    KE: 'Kenya',
    ZA: 'South Africa',
    GH: 'Ghana',
    EG: 'Egypt',
    GLOBAL: 'Global',
  }

  return (
    <header className="sticky top-0 z-30 flex h-16 items-center gap-4 border-b bg-background px-4 md:px-6">
      {/* Mobile menu button */}
      <Button
        variant="ghost"
        size="icon"
        className="md:hidden"
        onClick={onMenuClick}
      >
        <Menu className="h-5 w-5" />
      </Button>

      {/* Search bar - hidden on mobile */}
      <div className="hidden md:flex items-center flex-1 gap-2">
        <div className="relative w-96">
          <Search className="absolute left-3 top-1/2 h-4 w-4 -translate-y-1/2 text-muted-foreground" />
          <input
            type="search"
            placeholder="Search tickets, customers, knowledge base..."
            className="h-10 w-full rounded-md border border-input bg-background pl-10 pr-4 text-sm ring-offset-background placeholder:text-muted-foreground focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring"
          />
        </div>
      </div>

      {/* Right side actions */}
      <div className="flex items-center gap-2">
        {/* View Toggle - for global leads */}
        {showViewToggle && user?.role === 'SUPPORT_LEAD' && !user.country && (
          <div className="hidden lg:flex items-center gap-1 mr-4">
            <Button
              variant={currentView === 'global' ? 'default' : 'outline'}
              size="sm"
              onClick={() => onViewChange?.('global')}
            >
              <Globe className="h-4 w-4 mr-1" />
              Global
            </Button>
            <Button
              variant={currentView === 'country' ? 'default' : 'outline'}
              size="sm"
              onClick={() => onViewChange?.('country')}
            >
              By Country
            </Button>
            {currentView === 'country' && selectedCountry && (
              <Badge variant="secondary" className="ml-2">
                {countryNames[selectedCountry] || selectedCountry}
              </Badge>
            )}
          </div>
        )}

        {/* Country badge - for country-specific users */}
        {user?.country && (
          <Badge variant="outline" className="hidden lg:flex">
            <Globe className="h-3 w-3 mr-1" />
            {countryNames[user.country] || user.country}
          </Badge>
        )}

        {/* Notifications */}
        <DropdownMenu>
          <DropdownMenuTrigger asChild>
            <Button variant="ghost" size="icon" className="relative">
              <Bell className="h-5 w-5" />
              {notificationCount > 0 && (
                <Badge
                  variant="destructive"
                  className="absolute -right-1 -top-1 h-5 w-5 flex items-center justify-center p-0 text-xs"
                >
                  {notificationCount > 9 ? '9+' : notificationCount}
                </Badge>
              )}
            </Button>
          </DropdownMenuTrigger>
          <DropdownMenuContent align="end" className="w-80">
            <DropdownMenuLabel>Notifications</DropdownMenuLabel>
            <DropdownMenuSeparator />
            <div className="max-h-96 overflow-y-auto">
              <DropdownMenuItem className="flex flex-col items-start gap-1 p-3">
                <div className="flex items-center gap-2 w-full">
                  <span className="h-2 w-2 rounded-full bg-red-500" />
                  <span className="font-medium">SLA Breach Warning</span>
                  <span className="ml-auto text-xs text-muted-foreground">2m ago</span>
                </div>
                <p className="text-sm text-muted-foreground">
                  Ticket #12345 is at risk of breaching SLA
                </p>
              </DropdownMenuItem>
              <DropdownMenuItem className="flex flex-col items-start gap-1 p-3">
                <div className="flex items-center gap-2 w-full">
                  <span className="h-2 w-2 rounded-full bg-blue-500" />
                  <span className="font-medium">New Assignment</span>
                  <span className="ml-auto text-xs text-muted-foreground">15m ago</span>
                </div>
                <p className="text-sm text-muted-foreground">
                  You have been assigned 3 new tickets
                </p>
              </DropdownMenuItem>
              <DropdownMenuItem className="flex flex-col items-start gap-1 p-3">
                <div className="flex items-center gap-2 w-full">
                  <span className="h-2 w-2 rounded-full bg-green-500" />
                  <span className="font-medium">Customer Feedback</span>
                  <span className="ml-auto text-xs text-muted-foreground">1h ago</span>
                </div>
                <p className="text-sm text-muted-foreground">
                  New 5-star rating received from satisfied customer
                </p>
              </DropdownMenuItem>
            </div>
            <DropdownMenuSeparator />
            <DropdownMenuItem className="text-center justify-center text-sm">
              View all notifications
            </DropdownMenuItem>
          </DropdownMenuContent>
        </DropdownMenu>

        {/* Settings */}
        <Button variant="ghost" size="icon">
          <Settings className="h-5 w-5" />
        </Button>

        {/* User menu */}
        <DropdownMenu>
          <DropdownMenuTrigger asChild>
            <Button variant="ghost" className="gap-2">
              <Avatar className="h-8 w-8">
                <AvatarImage src={user?.avatar} />
                <AvatarFallback className="bg-support-blue text-white">
                  {getInitials(user?.firstName, user?.lastName)}
                </AvatarFallback>
              </Avatar>
              <div className="hidden lg:block text-left">
                <p className="text-sm font-medium">{user?.displayName}</p>
                <p className="text-xs text-muted-foreground">
                  {user?.role.replace('_', ' ')}
                </p>
              </div>
            </Button>
          </DropdownMenuTrigger>
          <DropdownMenuContent align="end" className="w-56">
            <DropdownMenuLabel>My Account</DropdownMenuLabel>
            <DropdownMenuSeparator />
            <DropdownMenuItem>
              <User className="mr-2 h-4 w-4" />
              Profile
            </DropdownMenuItem>
            <DropdownMenuItem>
              <Settings className="mr-2 h-4 w-4" />
              Settings
            </DropdownMenuItem>
            <DropdownMenuItem>
              <Bell className="mr-2 h-4 w-4" />
              Notifications
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
