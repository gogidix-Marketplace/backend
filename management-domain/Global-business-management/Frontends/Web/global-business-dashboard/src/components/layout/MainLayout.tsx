import React, { ReactNode } from 'react';
import { Outlet } from 'react-router-dom';
import { Header } from './Header';
import { Sidebar } from './Sidebar';
import { useBreakpoints } from '../../hooks/use-media-query';
import { useDashboardStore } from '../../stores/dashboard-store';
import { cn } from '../../utils/cn';

interface MainLayoutProps {
  children?: ReactNode;
}

export const MainLayout: React.FC<MainLayoutProps> = () => {
  const { isMobile } = useBreakpoints();
  const { sidebarOpen } = useDashboardStore();

  return (
    <div className="min-h-screen bg-background">
      <Header />
      <div className="flex">
        <Sidebar />
        <main
          className={cn(
            'flex-1 transition-all duration-300',
            {
              'ml-64': !isMobile && sidebarOpen,
              'ml-16': !isMobile && !sidebarOpen,
              'ml-0': isMobile,
            }
          )}
        >
          <div className="container mx-auto p-6">
            <Outlet />
          </div>
        </main>
      </div>
    </div>
  );
};
