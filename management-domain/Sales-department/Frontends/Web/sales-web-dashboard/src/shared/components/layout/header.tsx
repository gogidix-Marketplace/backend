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
import type { SalesUser } from '@shared/types'
import { cn } from '@shared/utils/cn'

interface HeaderProps {
  user?: SalesUser | null
  onMenuClick?: () => void
  onLogout?: () => void
  selectedCountry?: string | null
  onCountryChange?: (country: string | null) => void
}

const COUNTRIES = [
  { code: 'all', name: 'All Countries', flag: '🌍' },
  { code: 'NG', name: 'Nigeria', flag: '🇳🇬' },
  { code: 'KE', name: 'Kenya', flag: '🇰🇪' },
  { code: 'GH', name: 'Ghana', flag: '🇬🇭' },
  { code: 'ZA', name: 'South Africa', flag: '🇿🇦' },
  { code: 'ET', name: 'Ethiopia', flag: '🇪🇹' },
  { code: 'UG', name: 'Uganda', flag: '🇺🇬' },
  { code: 'TZ', name: 'Tanzania', flag: '🇹🇿' },
]

export function Header({
  user,
  onMenuClick,
  onLogout,
  selectedCountry,
  onCountryChange,
}: HeaderProps) {
  const currentCountry = COUNTRIES.find(c => c.code === selectedCountry) || COUNTRIES[0]

  return (
    <header className="sales-gradient sticky top-0 z-30 flex h-16 items-center justify-between border-b px-4 md:px-6">
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
            <span className="text-lg font-bold text-white">S</span>
          </div>
          <span className="hidden text-lg font-semibold text-white md:block">
            Sales Dashboard
          </span>
        </div>

        {/* Country Selector */}
        {user?.permissions.includes('view:all_countries') && onCountryChange && (
          <div className="hidden md:block">
            <DropdownMenu>
              <DropdownMenuTrigger asChild>
                <Button
                  variant="ghost"
                  size="sm"
                  className="gap-2 text-white hover:bg-white/10"
                >
                  <Globe className="h-4 w-4" />
                  <span>{currentCountry.flag} {currentCountry.name}</span>
                </Button>
              </DropdownMenuTrigger>
              <DropdownMenuContent align="start" className="w-56">
                {COUNTRIES.map(country => (
                  <DropdownMenuItem
                    key={country.code}
                    onClick={() => onCountryChange(country.code === 'all' ? null : country.code)}
                    className={cn(
                      selectedCountry === country.code || (country.code === 'all' && !selectedCountry)
                        ? 'bg-accent'
                        : ''
                    )}
                  >
                    <span className="mr-2">{country.flag}</span>
                    {country.name}
                  </DropdownMenuItem>
                ))}
              </DropdownMenuContent>
            </DropdownMenu>
          </div>
        )}
      </div>

      <div className="flex items-center gap-2 md:gap-4">
        {/* Search */}
        <div className="hidden md:flex items-center">
          <div className="relative">
            <Search className="absolute left-2.5 top-2.5 h-4 w-4 text-white/60" />
            <input
              type="search"
              placeholder="Search leads, deals, customers..."
              className={cn(
                'h-9 w-80 rounded-full border border-white/20 bg-white/10 pl-9 pr-4 text-sm text-white placeholder:text-white/60',
                'focus:outline-none focus:ring-2 focus:ring-white/30'
              )}
            />
          </div>
        </div>

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
                5
              </span>
            </Button>
          </DropdownMenuTrigger>
          <DropdownMenuContent align="end" className="w-80">
            <DropdownMenuLabel>Notifications</DropdownMenuLabel>
            <DropdownMenuSeparator />
            <div className="max-h-80 overflow-y-auto">
              <DropdownMenuItem className="flex-col items-start gap-2">
                <div className="flex w-full items-center justify-between">
                  <span className="font-medium">Deal Approval</span>
                  <Badge variant="warning">Urgent</Badge>
                </div>
                <p className="text-sm text-muted-foreground">
                  $500K deal from TechCorp NG awaiting your approval
                </p>
              </DropdownMenuItem>
              <DropdownMenuItem className="flex-col items-start gap-2">
                <div className="flex w-full items-center justify-between">
                  <span className="font-medium">New Lead Assigned</span>
                  <Badge variant="info">New</Badge>
                </div>
                <p className="text-sm text-muted-foreground">
                  5 new leads assigned to your territory
                </p>
              </DropdownMenuItem>
              <DropdownMenuItem className="flex-col items-start gap-2">
                <span className="font-medium">Quota Alert</span>
                <p className="text-sm text-muted-foreground">
                  You're at 95% of your monthly quota - $2,500 to go!
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
                <p className="text-xs font-semibold text-[#1E40AF]">
                  {user?.role?.replace(/_/g, ' ')}
                </p>
              </div>
            </DropdownMenuLabel>
            <DropdownMenuSeparator />
            <DropdownMenuItem>Profile</DropdownMenuItem>
            <DropdownMenuItem>My Performance</DropdownMenuItem>
            <DropdownMenuItem>Commission Report</DropdownMenuItem>
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
