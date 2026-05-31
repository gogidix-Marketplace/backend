// DashboardLayout Component
// Main layout with sidebar and header

import React, { useState } from 'react';
import { Outlet, useNavigate, useLocation } from 'react-router-dom';
import { Sidebar } from './Sidebar';
import { Header } from './Header';
import { useAuthStore } from '@infrastructure/stores';
import type { CountrySalesUser } from '@domain/types';

export interface DashboardLayoutProps {
  children?: React.ReactNode;
}

export const DashboardLayout: React.FC<DashboardLayoutProps> = ({ children }) => {
  const [sidebarCollapsed, setSidebarCollapsed] = useState(false);
  const navigate = useNavigate();
  const location = useLocation();
  const { user } = useAuthStore();

  const handleLogout = () => {
    // Clear auth and redirect to login
    navigate('/login');
  };

  const handleProfileClick = () => {
    navigate('/settings/profile');
  };

  return (
    <div className={`dashboard-layout ${sidebarCollapsed ? 'sidebar-collapsed' : ''}`}>
      <Sidebar
        collapsed={sidebarCollapsed}
        onToggle={() => setSidebarCollapsed(!sidebarCollapsed)}
        currentPath={location.pathname}
      />

      <div className="dashboard-main">
        <Header
          user={user}
          onLogout={handleLogout}
          onProfileClick={handleProfileClick}
          onMenuToggle={() => setSidebarCollapsed(!sidebarCollapsed)}
          showMenuButton={sidebarCollapsed}
        />

        <main className="dashboard-content">
          {children || <Outlet />}
        </main>
      </div>
    </div>
  );
};

export default DashboardLayout;
