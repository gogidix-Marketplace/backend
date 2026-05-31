import React from 'react';
import { NavLink, useLocation } from 'react-router-dom';
import { useTranslation } from 'react-i18next';
import {
  LayoutDashboard,
  Globe2,
  Map,
  FileText,
  TrendingUp,
  DollarSign,
  Users,
  Settings,
  BarChart3,
  ShieldCheck,
  ChevronLeft,
  ChevronRight,
  Building2,
} from 'lucide-react';
import { cn } from '../../utils/cn';
import { useDashboardStore } from '../../stores/dashboard-store';
import { useBreakpoints } from '../../hooks/use-media-query';
import { Button } from '../common/Button';
import { Badge } from '../common/Badge';

interface NavItem {
  path: string;
  label: string;
  icon: React.ComponentType<{ className?: string }>;
  badge?: number;
}

export const Sidebar: React.FC = () => {
  const { t } = useTranslation();
  const location = useLocation();
  const { isMobile } = useBreakpoints();
  const { sidebarOpen, setSidebarOpen } = useDashboardStore();

  const navItems: NavItem[] = [
    {
      path: '/dashboard',
      label: t('nav.dashboard'),
      icon: LayoutDashboard,
    },
    {
      path: '/regions',
      label: t('nav.regions'),
      icon: Globe2,
    },
    {
      path: '/countries',
      label: t('nav.countries'),
      icon: Map,
    },
    {
      path: '/reports',
      label: t('nav.reports'),
      icon: FileText,
    },
    {
      path: '/financial',
      label: t('nav.financial'),
      icon: DollarSign,
    },
    {
      path: '/customers',
      label: t('nav.customers'),
      icon: Users,
    },
    {
      path: '/operations',
      label: t('nav.operations'),
      icon: BarChart3,
    },
    {
      path: '/compliance',
      label: t('nav.compliance'),
      icon: ShieldCheck,
    },
  ];

  // Don't render sidebar on mobile (it's in a drawer)
  if (isMobile) {
    return null;
  }

  return (
    <>
      {/* Sidebar */}
      <aside
        className={cn(
          'fixed top-16 left-0 z-20 h-[calc(100vh-4rem)] border-r bg-background transition-all duration-300',
          {
            'w-64': sidebarOpen,
            'w-16': !sidebarOpen,
          }
        )}
      >
        <nav className="flex flex-col h-full p-2 space-y-1">
          {/* Main Navigation */}
          <div className="flex-1 space-y-1">
            {navItems.map((item) => {
              const Icon = item.icon;
              const isActive = location.pathname === item.path || location.pathname.startsWith(`${item.path}/`);

              return (
                <NavLink
                  key={item.path}
                  to={item.path}
                  className={({ isActive: isNavActive }) =>
                    cn(
                      'flex items-center gap-3 rounded-lg px-3 py-2 text-sm font-medium transition-colors',
                      {
                        'bg-primary text-primary-foreground': isNavActive,
                        'text-muted-foreground hover:bg-accent hover:text-accent-foreground': !isNavActive,
                        'justify-center': !sidebarOpen,
                      }
                    )
                  }
                  title={!sidebarOpen ? item.label : undefined}
                >
                  <Icon className="h-5 w-5 flex-shrink-0" />
                  {sidebarOpen && (
                    <>
                      <span className="flex-1">{item.label}</span>
                      {item.badge && item.badge > 0 && (
                        <Badge variant="destructive" size="sm">
                          {item.badge}
                        </Badge>
                      )}
                    </>
                  )}
                </NavLink>
              );
            })}
          </div>

          {/* Bottom Actions */}
          <div className="border-t pt-2 space-y-1">
            <NavLink
              to="/settings"
              className={({ isActive }) =>
                cn(
                  'flex items-center gap-3 rounded-lg px-3 py-2 text-sm font-medium transition-colors',
                  {
                    'bg-primary text-primary-foreground': isActive,
                    'text-muted-foreground hover:bg-accent hover:text-accent-foreground': !isActive,
                    'justify-center': !sidebarOpen,
                  }
                )
              }
              title={!sidebarOpen ? t('nav.settings') : undefined}
            >
              <Settings className="h-5 w-5 flex-shrink-0" />
              {sidebarOpen && <span>{t('nav.settings')}</span>}
            </NavLink>
          </div>
        </nav>

        {/* Collapse Toggle */}
        <Button
          variant="ghost"
          size="icon"
          className="absolute -right-3 top-1/2 -translate-y-1/2 h-6 w-6 rounded-full border bg-background shadow-md"
          onClick={() => setSidebarOpen(!sidebarOpen)}
        >
          {sidebarOpen ? (
            <ChevronLeft className="h-3 w-3" />
          ) : (
            <ChevronRight className="h-3 w-3" />
          )}
        </Button>
      </aside>

      {/* Overlay for collapsed state content */}
      {!sidebarOpen && (
        <div
          className="fixed top-16 left-16 w-48 h-[calc(100vh-4rem)] z-10"
          onMouseEnter={() => setSidebarOpen(true)}
        />
      )}
    </>
  );
};
