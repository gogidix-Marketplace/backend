import React from 'react';
import { Bell, Search, Settings, User, Moon, Sun, Globe, Menu } from 'lucide-react';
import { useTranslation } from 'react-i18next';
import { useDashboardStore } from '../../stores/dashboard-store';
import { useTheme } from '../../hooks/use-theme';
import { useBreakpoints } from '../../hooks/use-media-query';
import { Button } from '../common/Button';
import { Badge } from '../common/Badge';
import { Select } from '../common/Select';
import { languages, currencies } from '../../i18n';

export const Header: React.FC = () => {
  const { t } = useTranslation();
  const { darkMode, toggleTheme } = useTheme();
  const { isMobile } = useBreakpoints();
  const {
    sidebarOpen,
    toggleSidebar,
    notifications,
    unreadNotifications,
    language,
    setLanguage,
    currency,
    setCurrency,
    user,
  } = useDashboardStore();

  return (
    <header className="sticky top-0 z-30 w-full border-b bg-background/95 backdrop-blur supports-[backdrop-filter]:bg-background/60">
      <div className="flex h-16 items-center px-4 gap-4">
        {/* Left side */}
        <div className="flex items-center gap-4">
          {isMobile && (
            <Button variant="ghost" size="icon" onClick={toggleSidebar}>
              <Menu className="h-5 w-5" />
            </Button>
          )}
          <div className="flex items-center gap-2">
            <div className="h-8 w-8 rounded-lg bg-primary flex items-center justify-center">
              <span className="text-primary-foreground font-bold text-sm">GB</span>
            </div>
            {!isMobile && (
              <span className="font-semibold text-lg hidden sm:block">
                {t('common.appName')}
              </span>
            )}
          </div>
        </div>

        {/* Search - hidden on mobile */}
        {!isMobile && (
          <div className="flex-1 max-w-md mx-4">
            <div className="relative">
              <Search className="absolute left-3 top-1/2 -translate-y-1/2 h-4 w-4 text-muted-foreground" />
              <input
                type="text"
                placeholder={t('common.search')}
                className="h-10 w-full rounded-md border border-input bg-background pl-10 pr-4 text-sm ring-offset-background placeholder:text-muted-foreground focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2"
              />
            </div>
          </div>
        )}

        {/* Right side */}
        <div className="flex items-center gap-2 ml-auto">
          {/* Language Selector */}
          {!isMobile && (
            <Select
              value={language}
              onChange={(e) => setLanguage(e.target.value)}
              options={languages.map((lang) => ({
                value: lang.code,
                label: `${lang.flag} ${lang.name}`,
              }))}
              className="w-32"
              containerClassName="hidden md:block"
            />
          )}

          {/* Currency Selector */}
          {!isMobile && (
            <Select
              value={currency}
              onChange={(e) => setCurrency(e.target.value)}
              options={currencies.map((curr) => ({
                value: curr.code,
                label: `${curr.symbol} ${curr.code}`,
              }))}
              className="w-28"
              containerClassName="hidden lg:block"
            />
          )}

          {/* Theme Toggle */}
          <Button variant="ghost" size="icon" onClick={toggleTheme}>
            {darkMode ? (
              <Sun className="h-5 w-5" />
            ) : (
              <Moon className="h-5 w-5" />
            )}
          </Button>

          {/* Notifications */}
          <Button variant="ghost" size="icon" className="relative">
            <Bell className="h-5 w-5" />
            {unreadNotifications > 0 && (
              <Badge
                variant="destructive"
                size="sm"
                className="absolute -top-1 -right-1 h-5 w-5 flex items-center justify-center p-0 text-xs"
              >
                {unreadNotifications > 9 ? '9+' : unreadNotifications}
              </Badge>
            )}
          </Button>

          {/* Settings */}
          <Button variant="ghost" size="icon">
            <Settings className="h-5 w-5" />
          </Button>

          {/* User */}
          <Button variant="ghost" size="icon" className="relative">
            <User className="h-5 w-5" />
          </Button>
        </div>
      </div>
    </header>
  );
};
