// Tabs Component
// Tab navigation for different views

import React, { ReactNode } from 'react';

export interface Tab {
  id: string;
  label: string;
  icon?: string;
  content: ReactNode;
  disabled?: boolean;
  badge?: number | string;
  onClick?: () => void;
}

export interface TabsProps {
  tabs: Tab[];
  activeTab: string;
  onChange?: (tabId: string) => void;
  variant?: 'line' | 'pill' | 'underline';
  size?: 'sm' | 'md' | 'lg';
  className?: string;
  fullWidth?: boolean;
}

export const Tabs: React.FC<TabsProps> = ({
  tabs,
  activeTab,
  onChange,
  variant = 'line',
  size = 'md',
  className = '',
  fullWidth = false,
}) => {
  const handleTabClick = (tab: Tab) => {
    if (!tab.disabled) {
      onChange?.(tab.id);
      tab.onClick?.();
    }
  };

  const activeTabData = tabs.find(tab => tab.id === activeTab);

  return (
    <div className={`tabs tabs-${variant} tabs-${size} ${fullWidth ? 'tabs-full-width' : ''} ${className}`}>
      <div className="tabs-list" role="tablist">
        {tabs.map(tab => (
          <button
            key={tab.id}
            type="button"
            className={`tabs-tab ${activeTab === tab.id ? 'tabs-tab-active' : ''} ${
              tab.disabled ? 'tabs-tab-disabled' : ''
            }`}
            onClick={() => handleTabClick(tab)}
            disabled={tab.disabled}
            role="tab"
            aria-selected={activeTab === tab.id}
            aria-disabled={tab.disabled}
          >
            {tab.icon && <span className="tabs-tab-icon">{tab.icon}</span>}
            <span className="tabs-tab-label">{tab.label}</span>
            {tab.badge && (
              <span className="tabs-tab-badge">{tab.badge}</span>
            )}
          </button>
        ))}
      </div>

      {activeTabData && (
        <div className="tabs-content" role="tabpanel">
          {activeTabData.content}
        </div>
      )}
    </div>
  );
};

export default Tabs;
