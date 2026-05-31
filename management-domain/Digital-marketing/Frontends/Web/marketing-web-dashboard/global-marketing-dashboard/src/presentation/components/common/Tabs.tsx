// Tabs Component
// Reusable tab navigation

import './Tabs.css';

interface Tab {
  id: string;
  label: string;
  icon?: string;
  disabled?: boolean;
  badge?: number | string;
}

interface TabsProps {
  tabs: Tab[];
  activeTab: string;
  onChange: (tabId: string) => void;
  variant?: 'line' | 'pill' | 'card';
  className?: string;
}

export function Tabs({
  tabs,
  activeTab,
  onChange,
  variant = 'line',
  className = '',
}: TabsProps) {
  return (
    <div className={`tabs tabs-${variant} ${className}`}>
      <div className="tabs-list" role="tablist">
        {tabs.map((tab) => (
          <button
            key={tab.id}
            className={`tab ${activeTab === tab.id ? 'tab-active' : ''} ${tab.disabled ? 'tab-disabled' : ''}`}
            onClick={() => !tab.disabled && onChange(tab.id)}
            disabled={tab.disabled}
            role="tab"
            aria-selected={activeTab === tab.id}
            aria-disabled={tab.disabled}
          >
            {tab.icon && <span className="tab-icon">{tab.icon}</span>}
            <span className="tab-label">{tab.label}</span>
            {tab.badge && (
              <span className="tab-badge">
                {typeof tab.badge === 'number' && tab.badge > 99 ? '99+' : tab.badge}
              </span>
            )}
          </button>
        ))}
      </div>
    </div>
  );
}
