// Sidebar Component
// Main navigation sidebar for the dashboard

import React from 'react';
import { NavLink, useLocation } from 'react-router-dom';
import { useUIStore } from '../../../infrastructure/stores/uiStore';
import './Sidebar.css';

interface Route {
  path: string
  component: React.ComponentType
  label: string
}

interface SidebarProps {
  routes: Route[]
}

export const Sidebar: React.FC<SidebarProps> = ({ routes }) => {
  const location = useLocation()
  const sidebarCollapsed = useUIStore((state) => state.sidebarCollapsed)
  const toggleSidebar = useUIStore((state) => state.toggleSidebar)

  const iconMap: Record<string, string> = {
    overview: '📊',
    campaigns: '📢',
    budgets: '💰',
    analytics: '📈',
    leads: '👥',
    brands: '🎨',
    content: '📝',
    social: '📱',
    email: '✉️',
    seo: '🔍',
    reports: '📋',
    settings: '⚙️',
  }

  return (
    <aside className={`sidebar ${sidebarCollapsed ? 'sidebar--collapsed' : ''}`}>
      <div className="sidebar__header">
        <div className="sidebar__logo">
          <span className="logo-icon">📈</span>
          {!sidebarCollapsed && <span className="logo-text">Gogidix Marketing</span>}
        </div>
        <button
          className="sidebar__toggle"
          onClick={toggleSidebar}
          aria-label="Toggle sidebar"
        >
          <svg
            width="20"
            height="20"
            viewBox="0 0 20 20"
            fill="currentColor"
          >
            {sidebarCollapsed ? (
              <path d="M3 5a1 1 0 011-1h12a1 1 0 011 1v2a1 1 0 01-1 1H3a1 1 0 01-1-1V5zM3 11a1 1 0 011-1h12a1 1 0 011 1v2a1 1 0 01-1 1H3a1 1 0 01-1-1v-2zM3 17a1 1 0 011-1h12a1 1 0 011 1v2a1 1 0 01-1 1H3a1 1 0 01-1-1v-2z" />
            ) : (
              <path d="M4 6h16M4 12h16M4 18h16" />
            )}
          </svg>
        </button>
      </div>

      <nav className="sidebar__nav">
        <ul className="sidebar__menu">
          {routes.map((route) => {
            const path = route.path === '/' ? '/overview' : route.path
            const icon = iconMap[route.path.slice(1)] || '📄'

            return (
              <li key={route.path} className="sidebar__item">
                <NavLink
                  to={path}
                  className={({ isActive }) =>
                    `sidebar__link ${isActive ? 'sidebar__link--active' : ''}`
                  }
                  end={route.path === '/'}
                >
                  <span className="sidebar__icon">{icon}</span>
                  {!sidebarCollapsed && <span className="sidebar__label">{route.label}</span>}
                </NavLink>
              </li>
            )
          })}
        </ul>
      </nav>

      {!sidebarCollapsed && (
        <div className="sidebar__footer">
          <div className="sidebar__user">
            <div className="user-avatar">SM</div>
            <div className="user-info">
              <span className="user-name">Sarah Mitchell</span>
              <span className="user-role">VP Marketing</span>
            </div>
          </div>
        </div>
      )}
    </aside>
  )
}

export default Sidebar
