// Header Component
// Top header bar for the dashboard

import React from 'react'
import { useUIStore } from '../../../infrastructure/stores/uiStore'
import { SearchBar } from '../../components/common/SearchBar'
import { Button } from '../../components/common/Button'
import { ActionMenu, ActionItem } from '../../components/common/ActionMenu'
import './Header.css'

interface HeaderProps {
  title?: string
}

export const Header: React.FC<HeaderProps> = ({ title = 'Global Marketing Dashboard' }) => {
  const notifications = useUIStore((state) => state.notifications)
  const { clearNotifications } = useUIStore()

  const notificationItems: ActionItem[] = [
    { id: 'notif-1', label: 'Campaign "Q1 Brand Awareness" is 90% complete' },
    { id: 'notif-2', label: 'Budget warning: EMEA campaign at 85% spend' },
    { id: 'notif-3', label: 'New lead assigned: Enterprise opportunity ($50K)' },
  ]

  const userMenuItems: ActionItem[] = [
    { id: 'profile', label: 'Profile', icon: '👤' },
    { id: 'settings', label: 'Settings', icon: '⚙️' },
    { id: 'divider-1', label: '', divider: true },
    { id: 'logout', label: 'Logout', icon: '🚪', danger: true },
  ]

  return (
    <header className="header">
      <div className="header__left">
        <h1 className="header__title">{title}</h1>
      </div>

      <div className="header__right">
        <SearchBar
          placeholder="Search..."
          value=""
          onChange={() => {}}
          className="header__search"
          size="sm"
        />

        <ActionMenu
          trigger={
            <button className="header__icon-btn" aria-label="Notifications">
              <svg width="20" height="20" viewBox="0 0 20 20" fill="currentColor">
                <path d="M10 2a6 6 0 0 0-6 6v6a6 6 0 0 0 6 6h6a6 6 0 0 0 6-6V8a6 6 0 0 0-6-6h-6zm0 2h6a4 4 0 0 1 4 4v6a4 4 0 0 1-4 4h-6a4 4 0 0 1-4-4V8a4 4 0 0 1 4-4z" />
              </svg>
              {notifications.length > 0 && (
                <span className="header__badge">{notifications.length}</span>
              )}
            </button>
          }
          actions={notificationItems}
        />

        <ActionMenu
          trigger={
            <button className="header__icon-btn header__user-btn" aria-label="User menu">
              <span className="header__avatar">SM</span>
            </button>
          }
          actions={userMenuItems}
          align="right"
        />
      </div>
    </header>
  )
}

export default Header
