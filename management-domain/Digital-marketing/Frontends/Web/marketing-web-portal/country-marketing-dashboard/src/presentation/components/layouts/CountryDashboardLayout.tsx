import { Outlet, NavLink, useLocation } from 'react-router-dom'
import './CountryDashboardLayout.css'

interface CountryDashboardLayoutProps {
  routes: Array<{ path: string; label: string }>
}

export function CountryDashboardLayout({ routes }: CountryDashboardLayoutProps) {
  const location = useLocation()

  const currentRoute = routes.find(r => r.path === location.pathname || (r.path === '/' && location.pathname === '/'))

  return (
    <div className="country-dashboard-layout">
      {/* Sidebar */}
      <aside className="sidebar">
        <div className="sidebar-header">
          <div className="sidebar-logo">
            <span className="logo-icon">G</span>
            <div className="logo-text">
              <span className="logo-title">Gogidix</span>
              <span className="logo-subtitle">Country Dashboard</span>
            </div>
          </div>
          <div className="country-selector">
            <select className="country-select" defaultValue="US">
              <option value="US">\ud83c\uddfa\ud83c\uddf8 United States</option>
              <option value="UK">\ud83c\uddec\ud83c\udde7 United Kingdom</option>
              <option value="DE">\ud83c\udde9\ud83c\uddea Germany</option>
              <option value="FR">\ud83c\uddeb\ud83c\udff7 France</option>
              <option value="JP">\ud83c\uddef\ud83c\udff5 Japan</option>
              <option value="AU">\ud83c\udde6\ud83c\udffa Australia</option>
            </select>
          </div>
        </div>

        <nav className="sidebar-nav">
          <ul className="nav-list">
            {routes.map((route) => (
              <li key={route.path} className="nav-item">
                <NavLink
                  to={route.path}
                  className={({ isActive }) => `nav-link ${isActive ? 'nav-link-active' : ''}`}
                >
                  <span className="nav-text">{route.label}</span>
                </NavLink>
              </li>
            ))}
          </ul>
        </nav>

        <div className="sidebar-footer">
          <div className="user-info">
            <div className="user-avatar">JD</div>
            <div className="user-details">
              <div className="user-name">John Doe</div>
              <div className="user-role">Marketing Manager</div>
            </div>
          </div>
          <button className="logout-btn" title="Logout">
            \u219C
          </button>
        </div>
      </aside>

      {/* Main Content */}
      <div className="main-content">
        {/* Top Bar */}
        <header className="top-bar">
          <button className="menu-toggle" aria-label="Toggle menu">
            <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
              <line x1="3" y1="12" x2="21" y2="12" />
              <line x1="3" y1="6" x2="21" y2="6" />
              <line x1="3" y1="18" x2="21" y2="18" />
            </svg>
          </button>

          <div className="top-bar-title">
            {currentRoute?.label || 'Dashboard'}
          </div>

          <div className="top-bar-actions">
            <select className="period-selector" defaultValue="month">
              <option value="today">Today</option>
              <option value="week">This Week</option>
              <option value="month">This Month</option>
              <option value="quarter">This Quarter</option>
            </select>

            <button className="notification-btn" aria-label="Notifications">
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
          <Outlet />
        </main>
      </div>
    </div>
  )
}
