// Main Layout
// Main application layout with sidebar navigation

import { Outlet, NavLink, useNavigate, useLocation } from 'react-router-dom';
import { useEffect } from 'react';
import { useAuthStore, useUIStore } from '@infrastructure';
import { NAV_ITEMS } from '@shared';
import './MainLayout.css';

interface MainLayoutProps {
  children?: React.ReactNode;
}

export function MainLayout({ children }: MainLayoutProps) {
  const navigate = useNavigate();
  const location = useLocation();
  const { user, logout } = useAuthStore();
  const { sidebarOpen, toggleSidebar, theme, setTheme } = useUIStore();

  useEffect(() => {
    // Apply theme to document
    document.documentElement.setAttribute('data-theme', theme);
  }, [theme]);

  const handleLogout = async () => {
    await logout();
    navigate('/login');
  };

  const toggleTheme = () => {
    setTheme(theme === 'light' ? 'dark' : 'light');
  };

  return (
    <div className="main-layout">
      {/* Sidebar */}
      <aside className={`sidebar ${sidebarOpen ? 'sidebar-open' : 'sidebar-closed'}`}>
        <div className="sidebar-header">
          <div className="sidebar-logo">
            <span className="logo-icon">G</span>
            {sidebarOpen && <span className="logo-text">Gogidix HQ</span>}
          </div>
        </div>

        <nav className="sidebar-nav">
          <ul className="nav-list">
            {NAV_ITEMS.map((item) => {
              const Icon = () => <span className="nav-icon">{item.icon}</span>;
              return (
                <li key={item.id} className="nav-item">
                  <NavLink
                    to={item.path}
                    className={({ isActive }) => `nav-link ${isActive ? 'nav-link-active' : ''}`}
                  >
                    <Icon />
                    {sidebarOpen && <span className="nav-text">{item.label}</span>}
                  </NavLink>
                </li>
              );
            })}
          </ul>
        </nav>

        <div className="sidebar-footer">
          <div className="user-info">
            {user && (
              <>
                <div className="user-avatar">
                  {user.firstName?.charAt(0)}{user.lastName?.charAt(0)}
                </div>
                {sidebarOpen && (
                  <div className="user-details">
                    <div className="user-name">{user.firstName} {user.lastName}</div>
                    <div className="user-role">{user.role.replace(/_/g, ' ')}</div>
                  </div>
                )}
              </>
            )}
          </div>
          <div className="sidebar-actions">
            <button
              className="sidebar-action-btn"
              onClick={toggleTheme}
              title={`Switch to ${theme === 'light' ? 'dark' : 'light'} mode`}
            >
              {theme === 'light' ? '\u{1F319}' : '\u{2600}'}
            </button>
            <button
              className="sidebar-action-btn"
              onClick={handleLogout}
              title="Logout"
            >
              {'\u219C'}
            </button>
          </div>
        </div>
      </aside>

      {/* Main Content */}
      <div className="main-content">
        {/* Top Bar */}
        <header className="top-bar">
          <button
            className="menu-toggle"
            onClick={toggleSidebar}
            aria-label="Toggle menu"
          >
            <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
              <line x1="3" y1="12" x2="21" y2="12" />
              <line x1="3" y1="6" x2="21" y2="6" />
              <line x1="3" y1="18" x2="21" y2="18" />
            </svg>
          </button>

          <div className="top-bar-title">
            {NAV_ITEMS.find(item => item.path === location.pathname)?.label || 'Dashboard'}
          </div>

          <div className="top-bar-actions">
            {/* Period Selector */}
            <select
              className="period-selector"
              title="Select time period"
              defaultValue="month"
            >
              <option value="today">Today</option>
              <option value="week">This Week</option>
              <option value="month">This Month</option>
              <option value="quarter">This Quarter</option>
              <option value="year">This Year</option>
            </select>

            {/* Notifications */}
            <button className="top-bar-btn" aria-label="Notifications">
              <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                <path d="M18 8A6 6 0 0 0 6 8c0 7-3 9-3 9h18s-3-2-3-9" />
                <path d="M13.73 21a2 2 0 0 1-3.46 0" />
              </svg>
              <span className="notification-badge">3</span>
            </button>
          </div>
        </header>

        {/* Page Content */}
        <main className="page-content">
          {children || <Outlet />}
        </main>
      </div>
    </div>
  );
}
