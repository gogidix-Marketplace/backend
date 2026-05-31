import React from 'react';
import { Outlet } from 'react-router-dom';
import { Header } from './Header';
import { Sidebar } from './Sidebar';
import { useRegionalStore } from '../../stores/regional-store';
import { cn } from '../../utils/cn';

export const MainLayout: React.FC = () => {
  const { sidebarOpen } = useRegionalStore();

  return (
    <div className="min-h-screen bg-background">
      <Header />
      <div className="flex">
        <Sidebar />
        <main
          className={cn(
            'flex-1 transition-all duration-300',
            sidebarOpen ? 'ml-64' : 'ml-16'
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
