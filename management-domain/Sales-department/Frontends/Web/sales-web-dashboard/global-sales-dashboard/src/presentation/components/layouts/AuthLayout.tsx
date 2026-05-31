// Auth Layout
// Layout for authentication pages (login, forgot password, etc.)

import './AuthLayout.css';

interface AuthLayoutProps {
  children: React.ReactNode;
  title?: string;
  subtitle?: string;
}

export function AuthLayout({ children, title, subtitle }: AuthLayoutProps) {
  return (
    <div className="auth-layout">
      <div className="auth-container">
        <div className="auth-header">
          <div className="auth-logo">
            <span className="logo-icon">G</span>
            <span className="logo-text">Gogidix HQ</span>
          </div>
          {title && <h1 className="auth-title">{title}</h1>}
          {subtitle && <p className="auth-subtitle">{subtitle}</p>}
        </div>

        <div className="auth-content">
          {children}
        </div>

        <div className="auth-footer">
          <p>&copy; 2025 Gogidix. All rights reserved.</p>
        </div>
      </div>
    </div>
  );
}
