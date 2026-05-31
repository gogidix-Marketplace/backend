// Sidebar Component
// Navigation sidebar

import React from 'react';
import { useNavigate } from 'react-router-dom';
import { NAV_ITEMS } from '@shared/constants';

export interface SidebarProps {
  collapsed: boolean;
  onToggle: () => void;
  currentPath: string;
}

export const Sidebar: React.FC<SidebarProps> = ({ collapsed, onToggle, currentPath }) => {
  const navigate = useNavigate();

  const isActive = (path: string) => {
    if (path === '/') {
      return currentPath === '/';
    }
    return currentPath.startsWith(path);
  };

  return (
    <aside className={`sidebar ${collapsed ? 'sidebar-collapsed' : ''}`}>
      <div className="sidebar-header">
        <div className="sidebar-brand">
          <span className="brand-icon">\uD83D\uDCCB</span>
          {!collapsed && (
            <div className="brand-text">
              <span className="brand-title">Gogidix</span>
              <span className="brand-subtitle">Country Sales</span>
            </div>
          )}
        </div>
        <button
          type="button"
          className="sidebar-toggle"
          onClick={onToggle}
          aria-label="Toggle sidebar"
        >
          {collapsed ? '\u25B6' : '\u25C0'}
        </button>
      </div>

      <nav className="sidebar-nav">
        <ul className="sidebar-menu">
          {NAV_ITEMS.map(item => (
            <li key={item.id} className="sidebar-menu-item">
              <button
                type="button"
                className={`sidebar-menu-link ${isActive(item.path) ? 'sidebar-menu-link-active' : ''}`}
                onClick={() => navigate(item.path)}
                title={collapsed ? item.label : ''}
              >
                <span className="menu-icon">{item.icon}</span>
                {!collapsed && (
                  <span className="menu-label">{item.label}</span>
                )}
              </button>
            </li>
          ))}
        </ul>
      </nav>

      <div className="sidebar-footer">
        {!collapsed && (
          <div className="sidebar-info">
            <span className="sidebar-version">v1.0.0</span>
          </div>
        )}
      </div>
    </aside>
  );
};

export default Sidebar;
