// Header Component
// Top navigation header

import React, { useState } from 'react';
import type { CountrySalesUser } from '@domain/types';
import { COUNTRY_FLAGS } from '@shared/constants';
import { formatDate } from '@shared';

export interface HeaderProps {
  user?: CountrySalesUser | null;
  onLogout: () => void;
  onProfileClick: () => void;
  onMenuToggle: () => void;
  showMenuButton?: boolean;
}

export const Header: React.FC<HeaderProps> = ({
  user,
  onLogout,
  onProfileClick,
  onMenuToggle,
  showMenuButton = false,
}) => {
  const [notificationsOpen, setNotificationsOpen] = useState(false);
  const [profileOpen, setProfileOpen] = useState(false);

  // Mock notifications
  const notifications = [
    { id: 1, title: 'New lead assigned', message: 'You have a new high-priority lead', time: '5 min ago', read: false },
    { id: 2, title: 'Deal stage updated', message: 'Shoprite deal moved to Closing', time: '1 hour ago', read: false },
    { id: 3, title: 'Target achieved', message: 'Your team has reached 100% of monthly quota', time: '2 hours ago', read: true },
  ];

  const unreadCount = notifications.filter(n => !n.read).length;

  return (
    <header className="header">
      <div className="header-left">
        {showMenuButton && (
          <button
            type="button"
            className="header-menu-toggle"
            onClick={onMenuToggle}
            aria-label="Toggle menu"
          >
            \u2630
          </button>
        )}
        <div className="header-breadcrumb">
          <span className="breadcrumb-item">Country Dashboard</span>
          <span className="breadcrumb-separator">/</span>
          <span className="breadcrumb-item breadcrumb-current">
            {user?.country?.name || 'Nigeria'}
          </span>
        </div>
      </div>

      <div className="header-right">
        <div className="header-date">
          <span className="date-icon">\uD83D\uDCC5</span>
          <span>{formatDate(new Date(), 'MMM d, yyyy')}</span>
        </div>

        <div className={`header-notifications ${notificationsOpen ? 'notifications-open' : ''}`}>
          <button
            type="button"
            className="notification-button"
            onClick={() => setNotificationsOpen(!notificationsOpen)}
            aria-label="Notifications"
          >
            <span className="notification-icon">\uD83D\uDD14</span>
            {unreadCount > 0 && (
              <span className="notification-badge">{unreadCount}</span>
            )}
          </button>

          {notificationsOpen && (
            <div className="notifications-dropdown">
              <div className="notifications-header">
                <h4>Notifications</h4>
                <button type="button" className="mark-read">Mark all as read</button>
              </div>
              <div className="notifications-list">
                {notifications.map(notification => (
                  <div
                    key={notification.id}
                    className={`notification-item ${!notification.read ? 'notification-unread' : ''}`}
                  >
                    <div className="notification-content">
                      <span className="notification-title">{notification.title}</span>
                      <span className="notification-message">{notification.message}</span>
                      <span className="notification-time">{notification.time}</span>
                    </div>
                  </div>
                ))}
              </div>
              <div className="notifications-footer">
                <button type="button">View all notifications</button>
              </div>
            </div>
          )}
        </div>

        <div className={`header-profile ${profileOpen ? 'profile-open' : ''}`}>
          <button
            type="button"
            className="profile-button"
            onClick={() => setProfileOpen(!profileOpen)}
          >
            <div className="profile-avatar">
              {user?.avatar ? (
                <img src={user.avatar} alt="" />
              ) : (
                <span>{user?.firstName?.[0]}{user?.lastName?.[0]}</span>
              )}
            </div>
            <div className="profile-info">
              <span className="profile-name">{user?.firstName} {user?.lastName}</span>
              <span className="profile-role">
                {user?.role?.replace(/_/g, ' ').toLowerCase()}
              </span>
            </div>
            <span className="profile-chevron">\u25BC</span>
          </button>

          {profileOpen && (
            <div className="profile-dropdown">
              <div className="profile-user">
                <div className="profile-user-avatar">
                  {user?.avatar ? (
                    <img src={user.avatar} alt="" />
                  ) : (
                    <span>{user?.firstName?.[0]}{user?.lastName?.[0]}</span>
                  )}
                </div>
                <div className="profile-user-info">
                  <span className="profile-user-name">{user?.firstName} {user?.lastName}</span>
                  <span className="profile-user-email">{user?.email}</span>
                </div>
              </div>

              <div className="profile-divider" />

              <button
                type="button"
                className="profile-menu-item"
                onClick={() => {
                  onProfileClick();
                  setProfileOpen(false);
                }}
              >
                <span className="menu-item-icon">\uD83D\uDC64</span>
                <span>My Profile</span>
              </button>
              <button type="button" className="profile-menu-item">
                <span className="menu-item-icon">\u2699\uFE0F</span>
                <span>Settings</span>
              </button>
              <button type="button" className="profile-menu-item">
                <span className="menu-item-icon">\uD83D\uDDDE</span>
                <span>Help & Support</span>
              </button>

              <div className="profile-divider" />

              <button
                type="button"
                className="profile-menu-item profile-menu-item-logout"
                onClick={onLogout}
              >
                <span className="menu-item-icon">\uD83D\uDEAA</span>
                <span>Logout</span>
              </button>
            </div>
          )}
        </div>
      </div>
    </header>
  );
};

export default Header;
